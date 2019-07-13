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


public class ConvertDocxNCIC extends ConvertDocxDiacriticalSystem {
	
	public ConvertDocxNCIC( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
		this.initialize( "monodirectional/NCIC.txt", "monodirectional/NCIC.txt", "geezBasic", "geezBasic" );
		
		huletNeteb = ':';
		
		font1Typefaces.addAll(
			Arrays.asList(
			    "AGF - Zemen",
			    "AGF - Dawit",
			    "AGF - Ejji Tsihuf",
			    "AGF - Rejim",
			    "AGF - Yigezu Bisrat"
			)
		);
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}
		
		diacritics.addAll (
			Arrays.asList ( "\u00fa", "\u00fb", "\u00fc", "\u00fd", "\u00fe","\u00ff" )
		);
		
		translit2 = null;
		
		buildRE();
	}

	
}
