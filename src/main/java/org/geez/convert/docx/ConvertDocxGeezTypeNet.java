package org.geez.convert.docx;

import java.io.File;
import java.util.Arrays;

/*
 * The non-maven way to build the jar file:
 *
 * javac -Xlint:deprecation -cp docx4j-6.0.1.jar:dependencies/commons-io-2.5.jar:../icu4j-63_1.jar:dependencies/slf4j-api-1.7.25.jar:slf4j-1.7.25 *.java
 * jar -cvf convert.jar org/geez/convert/docx/*.class org/geez/convert/tables/
 * java -cp convert.jar:docx4j-6.0.1.jar:dependencies/*:../icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx geeznewab myFile-In.docx myFile-Out.docx
 *
 */


public class ConvertDocxGeezTypeNet extends ConvertDocxDiacriticalSystem {
	
	public ConvertDocxGeezTypeNet( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
		this.initialize( "monodirectional/GeezTypeNet.txt", "monodirectional/GeezTypeNet.txt", "GeezTypeNet", "GeezTypeNet" );
		
		huletNeteb = ':';
		
		diacritics.addAll (
			Arrays.asList( "\u00e8", "\u00e9", "\u00ea", "\u00eb", "\u00ec", "\u00ed", "\u00ee", "\u00ef", "\u00f0", "\u00f1", 
					"\u00f2", "\u00f3", "\u00f4", "\u00f5", "\u00f6", "\u00f7", "\u00f8", "\u00f9", "\u00fa", "\u00fb", "\u00fc"  )
		);
		
		translit2 = null;
		
		buildRE();
	}

	
	/*
	public boolean isMacron(String text) {
		if ( text.equals( "\u0068" ) ) {
			return true;
		}
		return false;
	}
	*/
	
}
