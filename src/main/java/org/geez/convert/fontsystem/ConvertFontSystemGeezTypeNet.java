package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemGeezTypeNet extends ConvertFontSystemDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> ( Arrays.asList( "GeezTypeNet" ) );
	
	{
		IDs = new String[] { "GeezTypeNet" } ;
	}
	
	public ConvertFontSystemGeezTypeNet() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/GeezTypeNet.txt", "GeezTypeNet" );
		
		huletNeteb = ':';
		
		diacritics.addAll (
			Arrays.asList( "\u00e8", "\u00e9", "\u00ea", "\u00eb", "\u00ec", "\u00ed", "\u00ee", "\u00ef", "\u00f0", "\u00f1", 
					"\u00f2", "\u00f3", "\u00f4", "\u00f5", "\u00f6", "\u00f7", "\u00f8", "\u00f9", "\u00fa", "\u00fb", "\u00fc"  )
		);
		
		translit2 = null;
		
		buildRE();
	}

	
	/*
	public boolean isMacron(String text) {
		if ( text.equals( "\u0068" ) ) {
			return true;
		}
		return false;
	}
	*/
	
}
