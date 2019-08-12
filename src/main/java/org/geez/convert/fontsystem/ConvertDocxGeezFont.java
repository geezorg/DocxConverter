package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertDocxGeezFont extends ConvertDocxDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> (
			Arrays.asList(
				"GeezAddis",
				"geezDirib",
				"geezLong",
				"GeezThin" 
			)
	);
	
	{
		IDs = new String[] { "GeezFont" } ;
	}
	
	public ConvertDocxGeezFont() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/GeezFont.txt", "geezBasic" );
		
		huletNeteb = ':';
		
		font1Typefaces.addAll(
			Arrays.asList ( 
				"GeezAddis",
				"geezDirib",
				"geezLong",
				"GeezThin" 
			)
		);
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		diacritics.addAll (
			Arrays.asList ( "\u003c", "\u003d", "\u003e",
					"\u004f", "\u005e", "\u005f",
					"\u00b9", "\u00e1", "\u00ec", "\u00ee", "\u00ef",
					"\u00fa", "\u00fb", "\u00fc", "\u00fd", "\u00fe", "\u00ff"
			)
		);
		
		translit2 = null;
		
		buildRE();
	}

	
}
