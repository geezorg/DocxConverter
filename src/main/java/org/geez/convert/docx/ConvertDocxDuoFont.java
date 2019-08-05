package org.geez.convert.docx;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ibm.icu.text.Transliterator;


/*
 * The non-maven way to build the jar file:
 *
 * javac -Xlint:deprecation -cp docx4j-6.0.1.jar:dependencies/commons-io-2.5.jar:../icu4j-63_1.jar:dependencies/slf4j-api-1.7.25.jar:slf4j-1.7.25 *.java
 * jar -cvf convert.jar org/geez/convert/docx/*.class org/geez/convert/tables/
 * java -cp convert.jar:docx4j-6.0.1.jar:dependencies/*:../icu4j-63_1.jar:slf4j-1.7.25/slf4j-nop-1.7.25.jar org.geez.convert.docx.ConvertDocx brana myFile-In.docx myFile-Out.docx
 *
 */


abstract class ConvertDocxDuoFont extends ConvertDocx {
	protected Transliterator translit1 = null;
	protected Transliterator translit2 = null;
	protected String fontName1 = null;
	protected String fontName2 = null;


	public ConvertDocxDuoFont( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
	}
	
	
	public void initialize(
		final String table1RulesFile,
		final String table2RulesFile,
		final String fontName1,
		final String fontName2)
	{
		try {
			String table1Text = readRulesResourceFile( table1RulesFile  );
			String table2Text = readRulesResourceFile( table2RulesFile );

			translit1 = Transliterator.createFromRules( IDs[0], table1Text.replace( '\ufeff', ' ' ), Transliterator.REVERSE );
			translit2 = Transliterator.createFromRules( IDs[1], table2Text.replace( '\ufeff', ' ' ), Transliterator.REVERSE );
			this.fontName1 = fontName1;
			this.fontName2 = fontName2;
			
			targetTypefaces.add( fontName1 );
			targetTypefaces.add( fontName2 );
			fontToTransliteratorMap.put( fontName1, translit1 );
			fontToTransliteratorMap.put( fontName2, translit2 );

		} catch ( Exception ex ) {
			System.err.println( ex );
		}
	}
}
