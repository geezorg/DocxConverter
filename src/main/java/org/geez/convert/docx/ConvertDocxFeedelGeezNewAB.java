package org.geez.convert.docx;

/*
 * javac -Xlint:deprecation -cp docx4j-6.0.1.jar:dependencies/commons-io-2.5.jar:../icu4j-63_1.jar:dependencies/slf4j-api-1.7.25.jar:slf4j-1.7.25 ConvertDocx.java
 * jar -cvf convert.jar org/geez/convert/docx/ConvertDocx.class org/geez/convert/tables/
 * java -cp convert.jar:docx4j-6.0.1.jar:dependencies/*:../icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx brana myFile-In.docx myFile-Out.docx
 *
 */

import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.Text;


import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


import com.ibm.icu.text.*;


public class ConvertDocxFeedelGeezNewAB extends ConvertDocx {

/*
	private ArrayList<String> checks = new ArrayList<String>(
		Arrays.asList("H", "K", "c", "h", "m", "v", "z", "±", "Ñ", "Ù", "\u009E", "¤", "\u0085", "\u0099", "\u00ad", "\u00ae", "÷", "Ö", "ã", "W", "X", "ª", "ç", "ë", "ì" )
	);
	public boolean isIncomplete(String text) {
		if ( text.equals( "" ) ) {
			return false;
		}
		return checks.contains( text.substring( text.length()-1 ) );
	}
*/
	private ArrayList<String> diacritics = new ArrayList<String>(
		Arrays.asList( "\"", "\u0023", "\u0025", "\u0026", "\u002a", "\u002b", "\u002c", "\u003a", "\u003b", "\u003c", "\u003d", "\u003e", "\u0040" )
	);
	public boolean isMacron(String text) {
		if ( text.equals( "\u0071" ) ) {
			return true;
		}
		return false;
	}
	private String prediacritic;
	public boolean isPrediacritic(String text) {
		char lastChar = text.charAt( text.length()-1 );
		if ( (lastChar == 0x71 ) || (lastChar == 0xff)  ) {
			prediacritic = text;
			return true;
		}
		return false;
	}
	public boolean isDiacritic(String text) {
		if ( text.equals( "" ) ) {
			return false;
		}
		return diacritics.contains( text.substring( text.length()-1 ) );
	}
	private boolean lastPrediacritic = false;

	

	public void processWithDiacritics(
		final String table1RulesFile,
		final String table2RulesFile,
		final String fontName1,
		final String fontName2,
		final File inputFile,
		final File outputFile)
	{

		try {
			// specify the transliteration file in the first argument.
			// read the input, transliterate, and write to output
			String table1Text = readRules( table1RulesFile  );
			String table2Text = readRules( table2RulesFile );

			final Transliterator translit1 = Transliterator.createFromRules( "Ethiopic-ExtendedLatin", table1Text.replace( '\ufeff', ' ' ), Transliterator.REVERSE );
			final Transliterator translit2 = Transliterator.createFromRules( "Ethiopic-ExtendedLatin", table2Text.replace( '\ufeff', ' ' ), Transliterator.REVERSE );


			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load( inputFile );		
			MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
				
			ClassFinder finder = new ClassFinder( R.class );
			new TraversalUtil(documentPart.getContent(), finder);

			Text lastTxt = null;
			String lastTxtValue = null;
		

			for (Object o : finder.results) {
				Object o2 = XmlUtils.unwrap(o);
						
				// this is ok, provided the results of the Callback
				// won't be marshalled			
			
				if (o2 instanceof org.docx4j.wml.R) {
					R r = (org.docx4j.wml.R)o2;
					RPr rpr = r.getRPr();
					RFonts rfonts = rpr.getRFonts();
				
					if( rfonts == null ) {
						t = null;
					}
					else if( fontName1.equals( rfonts.getAscii() ) ) {
						rfonts.setAscii( "Abyssinica SIL" );
						rfonts.setHAnsi( "Abyssinica SIL" );
						rfonts.setCs( "Abyssinica SIL" );
						rfonts.setEastAsia( "Abyssinica SIL" );
						t = translit1;
					}
					else if( fontName2.equals( rfonts.getAscii() ) ) {
						rfonts.setAscii( "Abyssinica SIL" );
						rfonts.setHAnsi( "Abyssinica SIL" );
						rfonts.setCs( "Abyssinica SIL" );
						rfonts.setEastAsia( "Abyssinica SIL" );
						t = translit2;
					}
					else {
						t = null;
					}

					List<Object> objects = r.getContent();
					for ( Object x : objects ) {
						Object x2 = XmlUtils.unwrap(x);
						if ( x2 instanceof org.docx4j.wml.Text ) {
							if (t != null) {
								Text txt = (org.docx4j.wml.Text)x2;
								String txtValue = txt.getValue();
								if( lastPrediacritic ) {
									txtValue = prediacritic + txtValue;
									lastPrediacritic = false;
								}

								String out = convertText( txtValue );
								txt.setValue( out );
								if( " ".equals( out ) ) { // if( Character.isWhitespace( out ) ) {
									txt.setSpace( "preserve" );
								}
								else if( isDiacritic( out ) ) {
									if( lastTxtValue != null ) {
										out = convertText( lastTxtValue + txt.getValue() );
										lastTxt.setValue( out );
										txt.setValue( "" );
									}
									lastTxt = null;
									lastTxtValue = null;
								}
								else if( isPrediacritic( out ) ) {
									txt.setValue( "" );
									lastTxt = null;
									lastTxtValue = null;
									lastPrediacritic = true;
								}
								else {
									lastTxt = txt;
									lastTxtValue = txtValue;
								}
							}
						}
						else {
							// System.err.println( "Found: " + x2.getClass() );
						}
					}
				} else {
					System.err.println( XmlUtils.marshaltoString(o, true, true) );
				}
			}
   
			// Save it zipped
			wordMLPackage.save( outputFile );

		} catch ( Exception ex ) {
			System.err.println( ex );
		}
   

	}



	public static void main( String[] args ) {
		if( args.length != 3 ) {
			System.err.println( "Exactly 3 arguements are expected: <system> <input file> <output file>" );
			System.exit(0);
		}

		String system = args[0];
		String inputFilepath  = System.getProperty("user.dir") + "/" + args[1];
		String outputFilepath = System.getProperty("user.dir") + "/" + args[2];
		File inputFile = new File ( inputFilepath );
		File outputFile = new File ( outputFilepath );


		if( "brana".equals( system ) ) {
			ConvertDocx converter = new ConvertDocx();
			converter.process( "BranaITable.txt", "BranaIITable.txt", "Brana I", "Brana II", inputFile, outputFile );
		}
		else if( "geeznewab".equals( system ) ) {
			ConvertDocxFeedelGeezNewAB converter = new ConvertDocxFeedelGeezNewAB();
			converter.processWithDiacritics( "GeezNewATable.txt", "GeezNewBTable.txt", "GeezNewA", "GeezNewB",  inputFile, outputFile );
		}
		else {
			System.err.println( "Unrecognized input system: " + system );
		}
	}
}
