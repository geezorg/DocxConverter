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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


import com.ibm.icu.text.*;


public class ConvertDocx {
	protected Transliterator t = null;

	public String readRules( String fileName ) throws IOException {
		String line, segment, rules = "";
		/*
	    File initialFile = new File("org/geez/convert/tables/" + fileName);
	    InputStream in = new FileInputStream(initialFile);
	    */
		// InputStream in = getClass().getResourceAsStream( "/org/geez/convert/tables/" + fileName ); 
		ClassLoader classLoader = this.getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream( "tables/" + fileName ); 
		BufferedReader ruleFile = new BufferedReader( new InputStreamReader(in, "UTF-8") );
		while ( (line = ruleFile.readLine()) != null) {
			if ( line.trim().equals("") || line.charAt(0) == '#' ) {
				continue;
			}
			segment = line.replaceFirst ( "^(.*?)#(.*)$", "$1" );
			rules += ( segment == null ) ? line : segment;
		}
		ruleFile.close();
		return rules;
	}


	public String convertText( String text ) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < text.length(); i++) {
			int x =  ( 0x00ff & (int)text.charAt(i) );
			sb.append(  (char)x );
		}
		String step1 = t.transliterate( sb.toString() );
		String step2 = (step1 == null ) ? null : step1.replaceAll( "፡፡", "።"); // this usually won't work since each hulet neteb is surrounded by separate markup.
		return step2;
	}



	public void process(
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
							if ( t != null) {
								Text txt = (org.docx4j.wml.Text)x2;
								String out = convertText( txt.getValue() );
								txt.setValue( out );
								if ( " ".equals( out ) ) {	
									txt.setSpace( "preserve" );
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
