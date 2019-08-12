package org.geez.convert.docx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertDocxNCIC extends ConvertDocxDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> (
			Arrays.asList(
					"AGF - Zemen",
				    "AGF - Dawit",
				    "AGF - Ejji Tsihuf",
				    "AGF - Rejim",
				    "AGF - Yigezu Bisrat"
			)
	);
	
	public static boolean isSupportedFont(String font) {
		return supportedFonts.contains( font );
	}
	
	{
		IDs = new String[] { "NCIC" } ;
	}
	
	public ConvertDocxNCIC() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/NCICAgafari.txt", "AGF - Zemen" );
		
		huletNeteb = ':';
		
		font1Typefaces.addAll(
			Arrays.asList(
			    "AGF - Zemen",
			    "AGF - Dawit",
			    "AGF - Ejji Tsihuf",
			    "AGF - Rejim",
			    "AGF - Yigezu Bisrat"
			)
		);
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		diacritics.addAll (
			Arrays.asList ( "\u00fa", "\u00fb", "\u00fc", "\u00fd", "\u00fe","\u00ff" )
		);
		
		translit2 = null;
		
		buildRE();
	}

	
}
