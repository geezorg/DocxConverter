package org.geez.convert.docx;

import java.io.File;
import java.util.Arrays;



public class ConvertDocxGeezTypeNet extends ConvertDocxDiacriticalSystem {

	{
		IDs = new String[] { "GeezTypeNet" } ;
	}
	
	public ConvertDocxGeezTypeNet( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
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
