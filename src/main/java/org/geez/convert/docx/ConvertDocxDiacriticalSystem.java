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
import java.util.Map;
import java.util.regex.Pattern;



abstract class ConvertDocxDiacriticalSystem extends ConvertDocx {
	protected final List<String> font1Typefaces = new ArrayList<String>();

	protected final ArrayList<String> diacritics = new ArrayList<String>();
	protected Pattern diacriticsRE = null;
	

	public boolean isDiacritic(String fontName, String text) {
		if ( text.equals( "" ) ) {
			return false;
		}
		return diacritics.contains( text.substring( text.length()-1 ) );
	}
	
	public void localCheck( Text text ) {
		String value = text.getValue();
		value = diacriticsRE.matcher(value).replaceAll( "$1" ); // this could be put into the normalizer
		text.setValue (value );
	}
	
	public String convertText( Text text ) {
		localCheck( text );
		return t.transliterate( text.getValue() );
	}
	
	
	/*
	 * We need to first "normalize" the text before processing it.  This avoids inserting lots of confusing
	 * complexity that would be needed to check for diacritical marks separated by xml elements from their
	 * bases.  This process will check if the first letter of run text is a diacritical mark, if so, then 
	 * move it to the last character of the previous run.  Thus <w:t>....b</w:t> ... <w:t>u...</w:t>
	 * becomes <w:t>...bu</w:t> ... <w:t>...</w:t> and "bu" will be converted properly to "ቡ".
	 * 
	 * There are two scenarios to check for and correct. The first is when in the Ethiopic font is specified
	 * in adjacent w:rFonts properties, and not in a named style. For example:
	 * 
	 * <w:r>
	 *  <w:rPr>
	 *    <w:rFonts w:ascii="..." w:ansi="..."/>
	 *  <w:rPr>
	 *  <w:t>b</w:t>
	 * </w:r>
	 * <w:r>
	 *  <w:rPr>
	 *    <w:rFonts w:ascii="..." w:ansi="..."/>
	 *  <w:rPr>
	 *  <w:t>u</w:t>
	 * </w:r>
	 * 
	 * The 2nd scenario is when an Ethiopic font is defined in a style, and no rFonts are present.  For example:
	 * 
	 * <w:p>
	 *   <w:pPr><w:pStyle w:val="BodyText"/><w:rPr><w:sz w:val="20"/></w:rPr></w:pPr>
	 *   <w:r>
	 *     <w:rPr>
	 *      <w:sz w:val="20"/>
	 *     <w:rPr>
	 *     <w:t>b</w:t>
	 *   </w:r>
	 *   <w:r>
	 *    <w:rPr>
	 *      <w:sz w:val="20"/>
	 *    <w:rPr>
	 *    <w:t>u</w:t>
	 *   </w:r>
	 * </w:p>
	 * 
	 */
	public void normalizeText( final JaxbXmlPart<?> part, StyledTextFinder stFinder, UnstyledTextFinder ustFinder ) throws Docx4JException {

		if( stFinder.hasStyles() ) {
			stFinder.clearResults();
		
			new TraversalUtil( part.getContents(), stFinder );
			// fix styled text nodes:
		
			List<Text> styledText = stFinder.resultsOrdered;
			int size = styledText.size();
			for ( int i=1; i<size; i++ ) {
				Text text1 = styledText.get(i);
				String value1 = text1.getValue();
				char firstChar = value1.charAt(0);
				if( isDiacritic(fontIn, String.valueOf(firstChar) ) )  {
					Text text0 = styledText.get( i-1 );
					String value0 = text0.getValue();
				
					text0.setValue( value0 + firstChar );   // append to previous node as last char
					text1.setValue( value1.substring(1) );  // remove from current node
				}
			}
		}
		
		ustFinder.clearResults();
		new TraversalUtil( part.getContents(), ustFinder );
		
		List<Text> unstyledText = ustFinder.resultsOrdered;
		int size = unstyledText.size();
		for ( int i=1; i<size; i++ ) {
			Text text1 = unstyledText.get(i);
			String value1 = text1.getValue();
			char firstChar = value1.charAt(0);
			if( isDiacritic(fontIn, String.valueOf(firstChar) ) )  {
				Text text0 = unstyledText.get( i-1 );
				String value0 = text0.getValue();
				
				text0.setValue( value0 + firstChar );   // append to previous node as last char
				text1.setValue( value1.substring(1) );  // remove from current node
			}
		}
	}
	

}
