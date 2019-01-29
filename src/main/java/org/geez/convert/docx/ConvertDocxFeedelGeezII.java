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
import java.util.regex.Pattern;
import java.util.Arrays;



public class ConvertDocxFeedelGeezII extends ConvertDocxDiacriticalSystem {

	public ConvertDocxFeedelGeezII() {
		this.initialize( "FeedelGeez.txt", "FeedelGeezII.txt", "Geez", "GeezII" );
		huletNeteb = '\uf023';

				
		diacritics.addAll (
				Arrays.asList( "\uf0b3", "\uf090", "\uf0f9", "\uf03e", "\uf0c0", "\uf03f", "\uf0d6", "\uf08a", "\uf08b", "\uf0ca", "\uf0d0", "\uf05f", "\uf09d" )
		);
		
		StringBuilder sb = new StringBuilder();
		for (String s : diacritics) {
			sb.append(s);
		}
		
		diacriticsRE = Pattern.compile(
				"([" + sb + "])([" + sb + "])"
		);
		
	}


	public String convertText( String text ) {
		StringBuilder sb = new StringBuilder();
		text = diacriticsRE.matcher(text).replaceAll( "$1" );
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
