package org.geez.convert.docx;

import java.io.File;
import java.util.Arrays;



public class ConvertDocxFeedelGeezigna extends ConvertDocxDiacriticalSystem {
	
	{
		IDs = new String[] { "Geezigna" } ;
	}
	
	public ConvertDocxFeedelGeezigna( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
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
