package org.geez.convert.docx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.docx4j.wml.Text;



public class ConvertDocxFeedelGeezII extends ConvertDocxDiacriticalSystem {

	public static final Set<String> supportedFonts = new HashSet<String> ( Arrays.asList( "GeezI", "GeezII" ) );

	{
		IDs = new String[] { "FeedelGeez", "FeedelGeezII" } ;
	}
	
	public ConvertDocxFeedelGeezII() {
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

	public String convertText( Text text ) {
		localCheck( text );
		String value = text.getValue();
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < value.length(); i++) {
			int x =  ( 0x00ff & (int)value.charAt(i) );
			sb.append( (char)x );
		}
		
		return xlit.transliterate( sb.toString() );
	}
	
	
	public void localCheck( Text text ) {
		super.localCheck( text );
		if( "\uf020".equals( text.getValue() ) ) {
			text.setSpace( "preserve" );
		}
	}
	
	
	protected boolean combinesWithHuletNeteb(char symbol) {
		return ( (symbol == huletNeteb) || ( symbol == 0xf02d ) );
	}

}
