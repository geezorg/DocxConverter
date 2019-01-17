package org.geez.convert.docx;

/*
 * The non-maven way to build the jar file:
 *
 * javac -Xlint:deprecation -cp docx4j-6.0.1.jar:dependencies/commons-io-2.5.jar:../icu4j-63_1.jar:dependencies/slf4j-api-1.7.25.jar:slf4j-1.7.25 *.java
 * jar -cvf convert.jar org/geez/convert/docx/*.class org/geez/convert/tables/
 * java -cp convert.jar:docx4j-6.0.1.jar:dependencies/*:../icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx brana myFile-In.docx myFile-Out.docx
 *
 */

import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.FootnotesPart;
// import org.docx4j.openpackaging.parts.WordprocessingML.EndnotesPart;
import org.docx4j.openpackaging.parts.JaxbXmlPart;

import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.icu.text.*;


public class ConvertDocx {
	protected Transliterator t = null;
	protected String fontOut = null;
	protected String fontIn = null;
	protected char huletNeteb = 0x0;

	public void setFont(String fontOut) {
		this.fontOut = fontOut;
	}
	
	public String readRules( String fileName ) throws IOException {
		String line, segment, rules = "";

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
		return t.transliterate( text );
	}

	
	public Transliterator getTransliteratorForFont( RFonts rfonts ) {
		
		if(  rfonts == null ) {
			return null;
		}
	
		fontIn = null;
		// We assume one of these fields will be set, and not more then one legacy typeface is set per RFonts element.
		if( targetTypefaces.contains( rfonts.getAscii() ) ) {
			fontIn = rfonts.getAscii();
			rfonts.setAscii( fontOut );
		}
		if( targetTypefaces.contains( rfonts.getHAnsi() ) ) {
			fontIn = rfonts.getHAnsi();
			rfonts.setHAnsi( fontOut );
		}
		if( targetTypefaces.contains( rfonts.getCs() ) ) {
			fontIn = rfonts.getCs();
			rfonts.setCs( fontOut );
		}
		if( targetTypefaces.contains( rfonts.getEastAsia() ) ) {
			fontIn = rfonts.getEastAsia();
			rfonts.setEastAsia( fontOut );
		}

		return fontToTransliteratorMap.get(fontIn) ;
	}


	public void processObjects( final JaxbXmlPart<?> part) throws Docx4JException
	{			
			ClassFinder finder = new ClassFinder( R.class );
			new TraversalUtil(part.getContents(), finder);
		

			for (Object o : finder.results) {
				Object o2 = XmlUtils.unwrap(o);
						
				// this is ok, provided the results of the Callback
				// won't be marshalled			
			
				if (o2 instanceof org.docx4j.wml.R) {
					R r = (org.docx4j.wml.R)o2;
					RPr rpr = r.getRPr();
					if (rpr == null ) {
						continue;
					}
					RFonts rfonts = rpr.getRFonts();
				
					t =  getTransliteratorForFont(  rfonts );
					
					if( t == null ) {
						continue;
					}

					List<Object> objects = r.getContent();
					for ( Object x : objects ) {
						Object x2 = XmlUtils.unwrap(x);
						if ( x2 instanceof org.docx4j.wml.Text ) {
							Text txt = (org.docx4j.wml.Text)x2;
							String out = convertText( txt.getValue() );
							txt.setValue( out );
							if ( " ".equals( out ) ) {	
								txt.setSpace( "preserve" );
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
	}


	protected Transliterator translit1 = null;
	protected Transliterator translit2 = null;
	protected String fontName1 = null;
	protected String fontName2 = null;
	protected List<String> targetTypefaces = new  ArrayList<String>();
	protected Map<String,Transliterator> fontToTransliteratorMap = new HashMap<String,Transliterator>();

	public void initialize(
		final String table1RulesFile,
		final String table2RulesFile,
		final String fontName1,
		final String fontName2)
	{

		try {
			// specify the transliteration file in the first argument.
			// read the input, transliterate, and write to output
			String table1Text = readRules( table1RulesFile  );
			String table2Text = readRules( table2RulesFile );

			translit1 = Transliterator.createFromRules( "Ethiopic-ExtendedLatin", table1Text.replace( '\ufeff', ' ' ), Transliterator.REVERSE );
			translit2 = Transliterator.createFromRules( "Ethiopic-ExtendedLatin", table2Text.replace( '\ufeff', ' ' ), Transliterator.REVERSE );
			this.fontName1 = fontName1;
			this.fontName2 = fontName2;
			
			targetTypefaces.add( fontName1 );
			targetTypefaces.add( fontName2 );
			fontToTransliteratorMap.put( fontName1, translit1 );
			fontToTransliteratorMap.put( fontName2, translit2 );

		} catch ( Exception ex ) {
			System.err.println( ex );
		}
	}


	public void process( final File inputFile, final File outputFile )
	{
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load( inputFile );		
			MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
       		processObjects( documentPart );
            
       		if( documentPart.hasFootnotesPart() ) {
	            	FootnotesPart footnotesPart = documentPart.getFootnotesPart();
       			processObjects( footnotesPart );
       		}
  
       		wordMLPackage.save( outputFile );
		}
		catch ( Exception ex ) {
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
			ConvertDocx converter = new ConvertDocxBrana();
			converter.process( inputFile, outputFile );
		}
		else if( "geeznewab".equals( system ) ) {
			ConvertDocx converter = new ConvertDocxFeedelGeezNewAB();
			converter.process( inputFile, outputFile );
		}
		else if( "geeztype".equals( system ) ) {
			ConvertDocx converter = new ConvertDocxGeezType();
			converter.process( inputFile, outputFile );
		}
		else {
			System.err.println( "Unrecognized input system: " + system );
		}
	}
}
