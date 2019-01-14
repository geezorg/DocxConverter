package org.geez.convert.docx;

/*
 * The non-maven way to build the jar file:
 *
 * javac -Xlint:deprecation -cp docx4j-6.0.1.jar:dependencies/commons-io-2.5.jar:../icu4j-63_1.jar:dependencies/slf4j-api-1.7.25.jar:slf4j-1.7.25 *.java
 * jar -cvf convert.jar org/geez/convert/docx/*.class org/geez/convert/tables/
 * java -cp convert.jar:docx4j-6.0.1.jar:dependencies/*:../icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx geeznewab myFile-In.docx myFile-Out.docx
 *
 */

import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
// import org.docx4j.openpackaging.parts.WordprocessingML.EndnotesPart;
import org.docx4j.openpackaging.parts.JaxbXmlPart;

import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


// import com.ibm.icu.text.*;


public class ConvertDocxPowerGeez extends ConvertDocx {
	private final List<String> font1Typefaces = new ArrayList<String>();

	public ConvertDocxPowerGeez() {
		this.initialize( "PowerGeez.txt", "PowerGeezNumbers.txt", "Ge'ez-1", "Ge'ez 1 Numbers, etc" );
		font1Typefaces.add( "Ge'ez-1" );
		font1Typefaces.add( "Ge'ez-2" );
		font1Typefaces.add( "Ge'ez-3" );
		font1Typefaces.add( "Ge'ez-1 Normal" );
	}

	private ArrayList<String> diacritics123 = new ArrayList<String>(
		Arrays.asList( "\u003c", "\u003d", "\u003e", "\u003f", "\u0040", "\u0041", "\u0042", "\u0043", "\u0044", "\u0045", "\u0046" )
	);
	private ArrayList<String> diacriticsNumbers = new ArrayList<String>(
			Arrays.asList( "\u002b", "\u002c" )
	);

	public boolean isDiacritic(String fontName, String text) {
		if ( text.equals( "" ) ) {
			return false;
		}
		if( fontName.equals( fontName2 ) ) {
			return diacriticsNumbers.contains( text.substring( text.length()-1 ) );
		}
		return diacritics123.contains( text.substring( text.length()-1 ) );
	}


	public String convertText( String text ) {
		/* Revlidate this bit masking, it was necessary for GeezNewA, B but 
		 * may not be needed with the others.
		 */
		String textIn = "";
		try {
			// byte[] b = text.getBytes("UTF-8"); 
			// System.out.println( text + " has size " + b.length );

			textIn = new String( text.getBytes("UTF-8"), "UTF-16"  );
			byte[] b = text.getBytes("UTF-16");
			System.out.println( "[" + text + "] has size " + text.length() );
		} catch(Exception ex) {}
		

		

		/*
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < text.length(); i++) {
			int x =  ( 0x00ff & (int)text.charAt(i) );
			sb.append(  (char)x );
		}*/
		String step1 = t.transliterate( text );
		System.out.println( "Unicode: " +  String.format("\\u%04x", (int) text.charAt(0)) + " Out: " + step1 );
		String step2 = (step1 == null ) ? null : step1.replaceAll( "፡፡", "።"); // this usually won't work since each hulet neteb is surrounded by separate markup.
		return step2;
	}
	
	public void processObjects( final JaxbXmlPart<?> part ) throws Docx4JException {
			ClassFinder finder = new ClassFinder( R.class );
			new TraversalUtil(part.getContents(), finder);

			Text lastTxt = null;
			String lastTxtValue = null;


			for (Object o : finder.results) {
				Object o2 = XmlUtils.unwrap(o);
						
				// this is ok, provided the results of the Callback
				// won't be marshalled			
			
				if (o2 instanceof org.docx4j.wml.R) {
					R r = (org.docx4j.wml.R)o2;
					RPr rpr = r.getRPr();
					if (rpr == null ) continue;
					RFonts rfonts = rpr.getRFonts();
					
				
					if( rfonts == null ) {
						continue;
					}
					String fontName = rfonts.getAscii();
					
					if( font1Typefaces.contains( fontName ) ) {
						rfonts.setAscii( fontOut );
						rfonts.setHAnsi( fontOut );
						rfonts.setCs( fontOut );
						rfonts.setEastAsia( fontOut );
						t = translit1;
					}
					else if( fontName2.equals( fontName ) ) {
						rfonts.setAscii( fontOut );
						rfonts.setHAnsi( fontOut );
						rfonts.setCs( fontOut );
						rfonts.setEastAsia( fontOut );
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

								String out = convertText( txtValue );
								txt.setValue( out );
								if( " ".equals( out ) ) { // if( Character.isWhitespace( out ) ) {
									txt.setSpace( "preserve" );
								}
								else if( isDiacritic( fontName, out ) ) {
									if( lastTxtValue != null ) {
										out = convertText( lastTxtValue + txt.getValue() );
										lastTxt.setValue( out );
										txt.setValue( "" );
									}
									lastTxt = null;
									lastTxtValue = null;
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
				}
				else {
					System.err.println( XmlUtils.marshaltoString(o, true, true) );
				}
			}

	}

}
