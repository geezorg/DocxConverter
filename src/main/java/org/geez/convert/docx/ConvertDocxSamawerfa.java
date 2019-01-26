package org.geez.convert.docx;


/*
 * The non-maven way to build the jar file:
 *
 * javac -Xlint:deprecation -cp docx4j-6.0.1.jar:dependencies/commons-io-2.5.jar:../icu4j-63_1.jar:dependencies/slf4j-api-1.7.25.jar:slf4j-1.7.25 *.java
 * jar -cvf convert.jar org/geez/convert/docx/*.class org/geez/convert/tables/
 * java -cp convert.jar:docx4j-6.0.1.jar:dependencies/*:../icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx geeznewab myFile-In.docx myFile-Out.docx
 *
 */

import java.util.Arrays;


public class ConvertDocxSamawerfa extends ConvertDocxDiacriticalSystem {

	public ConvertDocxSamawerfa() {
		this.initialize( "Samawerfa.txt", "Samawerfa.txt", "Addis98", "Addis98" );
		huletNeteb = '\u003a';
		
		font1Typefaces.add( "Addis98" );
		font1Typefaces.add( "Addis98w" );
		font1Typefaces.add( "AddisWP" );
		font1Typefaces.add( "Addis98 regular" );
		font1Typefaces.add( "Addis98w regular" );
		font1Typefaces.add( "AddisWP regular" );
		
		targetTypefaces.add( "Addis98" );
		targetTypefaces.add( "Addis98w" );
		targetTypefaces.add( "AddisWP" );
		targetTypefaces.add( "Addis98 regular" );
		targetTypefaces.add( "Addis98w regular" );
		targetTypefaces.add( "AddisWP regular" );
		
		for(String key: font1Typefaces) {
			fontToTransliteratorMap.put( key, translit1 );			
		}

		translit2 = null;


		diacritics.addAll (
				Arrays.asList( "\u00fd", "\u00e7", "\u00e6", "\u00e5", "\u00e8", "\u00e9", "\u00ea", "\u00eb", "\u00ec", "\u00ed", "\u00ee", "\u00ef", "\u00f0", "\u00f1", "\u00f2", "\u00f9", "\u00f8", "\u00f7", "\u00f6" ,"\u00f5", "\u00f4", "\u00f3", "\u00fb", "\u00fc", "\u00fa", "\u003a" )
		);
	}

}
