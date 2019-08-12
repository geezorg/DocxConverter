package org.geez.convert.docx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.docx4j.TraversalUtil;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.JaxbXmlPart;
import org.docx4j.wml.Text;



public class ConvertDocxDiacriticalSystem extends ConvertDocxDuoFont {
	protected final List<String> font1Typefaces = new ArrayList<String>();

	protected final ArrayList<String> diacritics = new ArrayList<String>();
	protected Pattern diacriticsRE = null;
	
	
	public ConvertDocxDiacriticalSystem() {
		super();
	}
	
	protected void buildRE() {
		
		StringBuilder sb = new StringBuilder();
		for (String s : diacritics) {
			sb.append(s);
		}
		
		diacriticsRE = Pattern.compile(
			"([" + sb + "])([" + sb + "])"
		);
		
	}

	
	public boolean isDiacritic(String fontName, String text) {
		if ( text.equals( "" ) ) {
			return false;
		}
		return diacritics.contains( text.substring( text.length()-1 ) );
	}
	
	
	public boolean isContinuant(String fontName, String text) {
		
		return ( isDiacritic(fontName, text) || (text.charAt(0) == huletNeteb) );
		
	}
	
	
	public void localCheck( Text text ) {
		String value = text.getValue();
		// remove extra diacritical symbol
		// value = diacriticsRE.matcher(value).replaceAll( "$1" ); // this could be put into the normalizer
		text.setValue (value );
	}
	
	/*
	public String convertText( Text text, String fontIn ) {
		localCheck( text );
		return xlit.transliterate( text.getValue() );
	}
	*/
	
	
	protected boolean combinesWithHuletNeteb(char symbol) {
		return ( symbol == huletNeteb );
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
		
			Map<Text,String> styledText = stFinder.results;
			List<Text> styledTextOrdered = stFinder.resultsOrdered;
			int size = styledTextOrdered.size();
			for ( int i=1; i<size; i++ ) {
				Text text1 = styledTextOrdered.get(i);
				String value1 = text1.getValue();
				if( value1.length() > 0 ) {
					char firstChar = value1.charAt(0);
					String fontIn = styledText.get( text1 );
					if( isDiacritic( fontIn, String.valueOf(firstChar) ) )  {
						Text text0 = styledTextOrdered.get( i-1 );
						String value0 = text0.getValue();
					
						text0.setValue( value0 + firstChar );   // append to previous node as last char
						text1.setValue( value1.substring(1) );  // remove from current node
					}
					else if( combinesWithHuletNeteb( firstChar ) ) {
						Text text0 = styledTextOrdered.get( i-1 );
						String value0 = text0.getValue();
						if( ( value0.length() > 0 ) && ( value0.charAt( value0.length() - 1) ) == huletNeteb ) {
							text0.setValue( value0 + firstChar );   // append to previous node as last char
							text1.setValue( value1.substring(1) );  // remove from current node	
						}
					}
				}
			}
		}
		
		ustFinder.clearResults();
		new TraversalUtil( part.getContents(), ustFinder );
		
		Map<Text,String> unstyledText = ustFinder.results;
		List<Text> unstyledTextOrdered = ustFinder.resultsOrdered;
		int size = unstyledTextOrdered.size();
		for ( int i=1; i<size; i++ ) {
			Text text1 = unstyledTextOrdered.get(i);
			String value1 = text1.getValue();
			if( value1.length() > 0 ) {
				char firstChar = value1.charAt(0);
				String fontIn = unstyledText.get( text1 );
				if( isDiacritic( fontIn, String.valueOf(firstChar) ) )  {
					Text text0 = unstyledTextOrdered.get( i-1 );
					String value0 = text0.getValue();
					
					text0.setValue( value0 + firstChar );   // append to previous node as last char
					text1.setValue( value1.substring(1) );  // remove from current node
				}
				else if( combinesWithHuletNeteb( firstChar ) ) {
					Text text0 = unstyledTextOrdered.get( i-1 );
					String value0 = text0.getValue();
					if( ( value0.length() > 0 ) && ( value0.charAt( value0.length() - 1) ) == huletNeteb ) {
						text0.setValue( value0 + firstChar );   // append to previous node as last char
						text1.setValue( value1.substring(1) );  // remove from current node	
					}
				}
			}
		}
	}
	

}
