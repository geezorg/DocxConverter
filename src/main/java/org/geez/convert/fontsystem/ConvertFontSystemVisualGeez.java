package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemVisualGeez extends ConvertFontSystemDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> (
			Arrays.asList(
					"VG2 Main",    "VG2 Main regular",
				    "VG2 Agazian", "VG2 Agazian regular",
				    "VG2 Title",   "VG2 Title regular"
			)
	);
	
	{
		IDs = new String[] { "VisualGeez", "VisualGeezNumbers" } ;
	}
	
	public ConvertFontSystemVisualGeez() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/VisualGeez.txt", "bidirectional/VisualGeezNumbers.txt", "VG2 Main", "VG Geez Numbers" );
		
		huletNeteb = '\u003a';
		
		font1Typefaces.add( "VG2 Main" );
		font1Typefaces.add( "VG2 Agazian" );
		font1Typefaces.add( "VG2 Title" );
		font1Typefaces.add( "VG2 Main regular" );
		font1Typefaces.add( "VG2 Agazian regular" );
		font1Typefaces.add( "VG2 Title regular" );
		
		targetTypefaces.add( "VG2 Agazian" );
		targetTypefaces.add( "VG2 Title" );
		targetTypefaces.add( "VG2 Main regular" );
		targetTypefaces.add( "VG2 Agazian regular" );
		targetTypefaces.add( "VG2 Title regular" );
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		fontToTransliteratorMap.put( "VG Geez Numbers" , translit2 );


		diacritics.addAll (
			Arrays.asList( "\u0021", "\u0023", "\u0024", "\u0026", "\u002a", "\u0040", "\u0045", "\u00a4", "\u00ba", "\u00d6" )
		);
				
		buildRE();
		
	}

}
