package org.geez.convert.docx;

import java.io.File;



public class ConvertDocxBrana extends ConvertDocxDuoFont {

	{
		IDs = new String[] { "BranaI", "BranaII" } ;
	}
	
	public ConvertDocxBrana( final File inputFile, final File outputFile ) {
		super( inputFile, outputFile );
		this.initialize( "bidirectional/BranaI.txt", "bidirectional/BranaII.txt", "Brana I", "Brana II" );
	}

}
