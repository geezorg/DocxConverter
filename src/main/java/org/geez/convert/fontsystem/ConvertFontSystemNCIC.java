package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemNCIC extends ConvertFontSystemDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> (
			Arrays.asList(
					"AGF - Zemen  Zemen",
					"AGF - Dawit  Zemen",
					"AGF - Ejji Tsihuf  Ejji",
					"AGF - Rejim",
					"AGF - Yigezu Bisrat Normal"
			)
	);

	{
		IDs = new String[] { "NCIC" } ;
	}
	
	public ConvertFontSystemNCIC() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/NCICAgafari.txt", "AGF - Zemen  Zemen" );
		
		huletNeteb = ':';
		
		font1Typefaces.addAll(
			Arrays.asList(
			    "AGF - Zemen  Zemen",
			    "AGF - Dawit  Zemen",
			    "AGF - Ejji Tsihuf  Ejji",
			    "AGF - Rejim",
			    "AGF - Yigezu Bisrat Normal"
			)
		);
		targetTypefaces.addAll( font1Typefaces );
		targetTypefaces.remove(0); // duplicate
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, xlit );			
		}
		
		diacritics.addAll (
			Arrays.asList ( "\u00fa", "\u00fb", "\u00fc", "\u00fd", "\u00fe","\u00ff" )
		);
		
		translit2 = null;
		
		buildRE();
	}

	
}
