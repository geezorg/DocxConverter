package org.geez.convert.docx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertDocxFeedelGeezigna extends ConvertDocxDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> ( Arrays.asList( "Geezigna" ) );
	
	{
		IDs = new String[] { "Geezigna" } ;
	}
	
	public ConvertDocxFeedelGeezigna() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/Geezigna.txt", "Geezigna" );
		
		huletNeteb = ':';
		
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
