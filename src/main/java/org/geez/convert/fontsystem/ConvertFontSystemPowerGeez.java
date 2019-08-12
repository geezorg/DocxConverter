package org.geez.convert.fontsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemPowerGeez extends  ConvertFontSystemDiacriticalSystem {
	
	public static final Set<String> supportedFonts = new HashSet<String> (
			Arrays.asList(
					"Ge'ez-1", "Ge'ez-1 Normal", "Ge'ez-1 Numbers",
				    "Ge'ez-2", "Ge'ez-2 Normal",
				    "Ge'ez-3", "Ge'ez-3 Normal"
			)
	);
	
	{
		IDs = new String[] { "PowerGeez", "PowerGeezNumbers" } ;
	}
	
	public ConvertFontSystemPowerGeez() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/PowerGeez.txt", "bidirectional/PowerGeezNumbers.txt", "Ge'ez-1", "Ge'ez-1 Numbers" );
		
		huletNeteb = ':';
		
		font1Typefaces.add( "Ge'ez-1" );
		font1Typefaces.add( "Ge'ez-2" );
		font1Typefaces.add( "Ge'ez-3" );
		font1Typefaces.add( "Ge'ez-1 Normal" );
		font1Typefaces.add( "Ge'ez-2 Normal" );
		font1Typefaces.add( "Ge'ez-3 Normal" );
		
		targetTypefaces.add( "Ge'ez-2" );
		targetTypefaces.add( "Ge'ez-3" );
		targetTypefaces.add( "Ge'ez-1 Normal" );
		targetTypefaces.add( "Ge'ez-2 Normal" );
		targetTypefaces.add( "Ge'ez-3 Normal" );
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		fontToTransliteratorMap.put( "Ge'ez-1 Numbers, etc" , translit2 );
		diacritics.addAll(
			Arrays.asList( "\u003c", "\u003d", "\u003e", "\u003f", "\u0040", "\u0041", "\u0042", "\u0043", "\u0044", "\u0045", "\u0046" )
		);
			
		buildRE();
		
	}
	private ArrayList<String> diacriticsNumbers = new ArrayList<String>(
			Arrays.asList( "\u002b", "\u002c" )
	);

	public boolean isDiacritic(String fontName, String text) {
		if ( text.equals( "" ) ) {
			return false;
		}
		if( fontName.equals( fontName2 ) ) {
			return diacriticsNumbers.contains( text.substring( text.length()-1 ) );
		}
		return diacritics.contains( text.substring( text.length()-1 ) );
	}

}
