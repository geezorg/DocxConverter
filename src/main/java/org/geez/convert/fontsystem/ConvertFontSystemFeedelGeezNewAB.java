package org.geez.convert.fontsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class ConvertFontSystemFeedelGeezNewAB extends ConvertFontSystemDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> (
			Arrays.asList(
					"GeezA",      "GeezB",
				    "GeezNewA",   "GeezNewB",
				    "GeezSindeA", "GeezSindeB",
				    "ZewdituA",   "ZewdituB",
				    "GeezNet"
			)
	);
	
	{
		IDs = new String[] { "GeezNewA", "GeezNewB" } ;
	}
	
	private final List<String> font2Typefaces = new ArrayList<String>();
	
	public ConvertFontSystemFeedelGeezNewAB() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/FeedelGeezNewA.txt", "bidirectional/FeedelGeezNewB.txt", "GeezNewA", "GeezNewB" );
		
		huletNeteb = '\uf022';
		
		font1Typefaces.add( "GeezA" );
		font1Typefaces.add( "GeezNewA" );
		font1Typefaces.add( "GeezSindeA" );
		font1Typefaces.add( "GeezNet" );
		font1Typefaces.add( "ZewdituA" );
		
		font2Typefaces.add( "GeezB" );
		font2Typefaces.add( "GeezNewB" );
		font2Typefaces.add( "GeezSindeB" );
		font2Typefaces.add( "ZewdituB" );
		
		targetTypefaces.add( "GeezA" );
		targetTypefaces.add( "GeezNewA" );
		targetTypefaces.add( "GeezNet" );
		targetTypefaces.add( "ZewdituA" );
		targetTypefaces.add( "GeezB" );
		targetTypefaces.add( "GeezNewB" );
		targetTypefaces.add( "ZewdituB" );

		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		for(String key: font2Typefaces) {
			fontToTransliteratorMap.put( key, translit2 );			
		}
		
		
		diacritics.addAll (
				Arrays.asList( "\uf023", "\uf025", "\uf026", "\uf02a", "\uf02b", "\uf02c", "\uf03a", "\uf03b", "\uf03c", "\uf03d", "\uf03e", "\uf040" )
		);
			
		buildRE();
		
	}


	public String convertText( String text, String fontIn ) {
		xlit = fontToTransliteratorMap.get( fontIn );
		if ( xlit == null ) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < text.length(); i++) {
			int x = ( 0x00ff & (int)text.charAt(i) );
			sb.append( (char)x );
		}
		
		return xlit.transliterate( sb.toString() );
	}
	
	
	public boolean isSpacePreservableSymbol(String space) {
		return ( space.equals("\uf020") );
	}

}
