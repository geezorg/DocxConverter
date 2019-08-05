package org.geez.convert.docx;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;



public class ConvertDocxPowerGeez extends  ConvertDocxDiacriticalSystem {

	{
		IDs = new String[] { "PowerGeez", "PowerGeezNumbers" } ;
	}

	public ConvertDocxPowerGeez( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
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
