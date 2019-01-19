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
		this.initialize( "PowerGeez.txt", "PowerGeezNumbers.txt", "Ge'ez-1", "Ge'ez-1 Numbers" );
		huletNeteb = ':';
		
		font1Typefaces.add( "Ge'ez-1" );
		font1Typefaces.add( "Ge'ez-2" );
		font1Typefaces.add( "Ge'ez-3" );
		font1Typefaces.add( "Ge'ez-1 Normal" );
		font1Typefaces.add( "Ge'ez-2 Normal" );
		font1Typefaces.add( "Ge'ez-3 Normal" );
		
		targetTypefaces.add( "Ge'ez 2" );
		targetTypefaces.add( "Ge'ez-3" );
		targetTypefaces.add( "Ge'ez-1 Normal" );
		targetTypefaces.add( "Ge'ez-2 Normal" );
		targetTypefaces.add( "Ge'ez-3 Normal" );
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		fontToTransliteratorMap.put( "Ge'ez-1 Numbers, etc" , translit2 );
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
	
	
	public String getQualifiedText( Text text, Object obj ) {
		if (! (obj instanceof org.docx4j.wml.RPr)) {
			return text.getValue();
		}
		
		String currentText = text.getValue();

		RPr rpr = (org.docx4j.wml.RPr)obj;
		if(! ((rpr.getParent()) instanceof org.docx4j.wml.R) ) {
			return text.getValue();	
		}
		R r = (org.docx4j.wml.R)rpr.getParent();
		List<Object> objects = r.getContent();
		for ( Object o : objects ) {
			Object x = XmlUtils.unwrap(o);
			if ( x instanceof org.docx4j.wml.Text ) {
				// we expect only one instance, so we'll return on the first one encountered
				Text nextText = (org.docx4j.wml.Text)x;
				String nextString =  nextText.getValue();
				char firstChar = nextString.charAt(0);
				if( isDiacritic(fontIn, String.valueOf(firstChar) ) )  {
					nextText.setValue( nextString.substring(1) );
					return currentText + firstChar  ;
				}
				else if ( (huletNeteb == firstChar) 
					 && ( huletNeteb == currentText.charAt( currentText.length() - 1 ) ) ) {
						nextText.setValue( nextString.substring(1) );
						return currentText + firstChar  ;
				}
				break;
			}
		}
		
		return currentText;
	}
	
	public void processObjects( final JaxbXmlPart<?> part ) throws Docx4JException {
			PropertiesFinder prFinder = new PropertiesFinder();
			new TraversalUtil(part.getContents(), prFinder );

			List<RFonts> rfontsNodes = new ArrayList<RFonts>( prFinder.results ); 
			int size = rfontsNodes.size();
			
			
			for (int i=0; i<size; i++) {
				RFonts rfonts = rfontsNodes.get(i);
				t =  getTransliteratorForFont( rfonts );
				
				if( t == null ) {
					continue;
				}
				
				
				/*
				 * Check if object at i+1 starts with a diacritic mark that may modify
				 * the last chart of  object i.  If true, append the i+1 first char to the end
				 * of the string at i. 
				 */
				
				if (rfonts.getParent() instanceof org.docx4j.wml.RPr) {
					R r = (org.docx4j.wml.R)((org.docx4j.wml.RPr)rfonts.getParent()).getParent();
					List<Object> objects = r.getContent();
					for ( Object o : objects ) {
						Object x = XmlUtils.unwrap(o);
						if ( x instanceof org.docx4j.wml.Text ) {
							Text txt = (org.docx4j.wml.Text)x;

							// revisit why we need this first part, maybe it was only necessary for Brana -?
							if( " ".equals( txt.getValue() ) || "".equals( txt.getValue() )) {
								// txt.setSpace( "preserve" );
							}
							else {
								String txtValue = ( (i+1) == size )
										? txt.getValue()
										: getQualifiedText( txt, ((Object)(rfontsNodes.get(i+1)).getParent()) )
								;
								String out = convertText( txtValue );
								txt.setValue( out );
							}
						}
					}
				}
				/*
				else {
					System.err.println( XmlUtils.marshaltoString(o, true, true) );
				}
				*/
			}

	}

}
