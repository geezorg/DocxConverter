package org.geez.convert.docx;

import java.io.File;
import java.util.Arrays;



public class ConvertDocxNCIC extends ConvertDocxDiacriticalSystem {

	{
		IDs = new String[] { "NCIC" } ;
	}
	
	public ConvertDocxNCIC( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
		this.initialize( "monodirectional/NCIC.txt", "AGF - Zemen" );
		
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
