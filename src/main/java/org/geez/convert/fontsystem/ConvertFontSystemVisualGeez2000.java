package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemVisualGeez2000 extends ConvertFontSystemDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> (
			Arrays.asList(
					"VG2000 Main",    "VG2 Main regular",
				    "VG2000 Agazian", "VG2 Agazian Black",
				    "VG2000 Title",   "VG2 Title Normal"
			)
	);
	
	{
		IDs = new String[] { "VisualGeez2000", "VisualGeezNumbers" } ;
	}
	
	public ConvertFontSystemVisualGeez2000() {
		super();
		init();
	}
	
	private void init() {
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
