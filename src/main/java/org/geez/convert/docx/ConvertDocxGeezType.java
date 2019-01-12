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


public class ConvertDocxGeezType extends ConvertDocx {
	public ConvertDocxGeezType() {
		this.initialize( "GeezType.txt", "GeezType.txt", "GeezType", "GeezType" );
	}

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
