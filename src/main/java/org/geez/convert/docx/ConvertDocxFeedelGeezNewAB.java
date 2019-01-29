package org.geez.convert.docx;

/*
 * The non-maven way to build the jar file:
 *
 * javac -Xlint:deprecation -cp docx4j-6.0.1.jar:dependencies/commons-io-2.5.jar:../icu4j-63_1.jar:dependencies/slf4j-api-1.7.25.jar:slf4j-1.7.25 *.java
 * jar -cvf convert.jar org/geez/convert/docx/*.class org/geez/convert/tables/
 * java -cp convert.jar:docx4j-6.0.1.jar:dependencies/*:../icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx geeznewab myFile-In.docx myFile-Out.docx
 *
 */


import org.docx4j.wml.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;



public class ConvertDocxFeedelGeezNewAB extends ConvertDocxDiacriticalSystem {
	private final List<String> font2Typefaces = new ArrayList<String>();

	public ConvertDocxFeedelGeezNewAB() {
		this.initialize( "FeedelGeezNewA.txt", "FeedelGeezNewB.txt", "GeezNewA", "GeezNewB" );
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
		
	}


	public String convertText( String text ) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < text.length(); i++) {
			int x =  ( 0x00ff & (int)text.charAt(i) );
			sb.append( (char)x );
		}
		return t.transliterate( sb.toString() );
	}
	
	
	public void localCheck( Text text ) {
		if( " ".equals( text.getValue() ) ) {
			text.setSpace( "preserve" );
		}
	}

}
