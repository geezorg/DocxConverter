package org.geez.convert.docx;

import java.io.File;
import java.util.Arrays;



public class ConvertDocxVisualGeez2000 extends ConvertDocxDiacriticalSystem {

	{
		IDs = new String[] { "VisualGeez2000", "VisualGeezNumbers" } ;
	}
	
	public ConvertDocxVisualGeez2000( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
		this.initialize( "monodirectional/VisualGeez2000.txt", "bidirectional/VisualGeezNumbers.txt", "VG2000 Main", "VG Geez Numbers" );
		
		huletNeteb = '\u201d';
		
		font1Typefaces.add( "VG2000 Main" );
		font1Typefaces.add( "VG2000 Agazian" );
		font1Typefaces.add( "VG2000 Title" );
		font1Typefaces.add( "VG2000 Main regular" );
		font1Typefaces.add( "VG2000 Agazian Black" );
		font1Typefaces.add( "VG2000 Title Normal" );
		
		targetTypefaces.add( "VG2000 Agazian" );
		targetTypefaces.add( "VG2000 Title" );
		targetTypefaces.add( "VG2000 Main regular" );
		targetTypefaces.add( "VG2000 Agazian Black" );
		targetTypefaces.add( "VG2000 Title Normal" );
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		// not sure what the number font is with VG2000
		fontToTransliteratorMap.put( "VG Geez Numbers" , translit2 );


		diacritics.addAll (
			Arrays.asList( "\u0026", "\u002a", "\u0045", "\u00a4", "\u00b7", "\u00bd", "\u00d6", "\u00dc", "\u00f9", "\u00fe", "\u201c" )
		);
				
		buildRE();
		
	}

}
