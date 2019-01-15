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


public class ConvertDocxFeedelGeezNewAB extends ConvertDocx {

	public ConvertDocxFeedelGeezNewAB() {
		this.initialize( "GeezNewATable.txt", "GeezNewBTable.txt", "GeezNewA", "GeezNewB" );
	}

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
	

	public String convertText( String text ) {
		/* Revalidate this bit masking, it was necessary for GeezNewA, B but 
		 * may not be needed with the others.
		 */
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < text.length(); i++) {
			int x =  ( 0x00ff & (int)text.charAt(i) );
			sb.append(  (char)x );
		}
		String step1 = t.transliterate( sb.toString() );
		String step2 = (step1 == null ) ? null : step1.replaceAll( "፡፡", "።"); // this usually won't work since each hulet neteb is surrounded by separate markup.
		return step2;
	}


	public void processObjects( final JaxbXmlPart<?> part ) throws Docx4JException
		{

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
						t = null;
					}
					else if( fontName1.equals( rfonts.getAscii() ) ) {
						rfonts.setAscii( fontOut );
						rfonts.setHAnsi( fontOut );
						rfonts.setCs( fontOut );
						rfonts.setEastAsia( fontOut );
						t = translit1;
					}
					else if( fontName2.equals( rfonts.getAscii() ) ) {
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

	}

}
