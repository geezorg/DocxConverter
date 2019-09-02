package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemFeedelGeezII extends ConvertFontSystemDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> ( Arrays.asList( "Geez", "GeezII" ) );

	{
		IDs = new String[] { "FeedelGeez", "FeedelGeezII" } ;
	}
	
	public ConvertFontSystemFeedelGeezII() {
		super();
		init();
	}
	
	private void init() {
		this.initialize( "monodirectional/FeedelGeez.txt", "bidirectional/FeedelGeezII.txt", "Geez", "GeezII" );
		
		huletNeteb = '\uf023';

				
		diacritics.addAll (
			Arrays.asList( "\uf0b3", "\uf090", "\uf0f9", "\uf03e", "\uf0c0", "\uf03f", "\uf0d6", "\uf08a", "\uf08b", "\uf0ca", "\uf0d0", "\uf05f", "\uf09d" )
		);
		
		buildRE();
		
	}

	public String convertText( String text, String fontName ) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < text.length(); i++) {
			int x =  ( 0x00ff & (int)text.charAt(i) );
			sb.append( (char)x );
		}
		
		xlit = fontToTransliteratorMap.get( fontName );
		return xlit.transliterate( sb.toString() );
	}

	
	public boolean isSpacePreservableSymbol(String space) {
		return ( space.equals("\uf020") );
	}
	
	
	public boolean combinesWithHuletNeteb(char symbol) {
		return ( (symbol == huletNeteb) || ( symbol == 0xf02d ) );
	}

}
