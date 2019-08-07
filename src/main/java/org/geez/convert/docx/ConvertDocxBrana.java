package org.geez.convert.docx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertDocxBrana extends ConvertDocxDuoFont {

	public static final Set<String> supportedFonts = new HashSet<String> ( Arrays.asList( "Brana I", "Brana II" ) );
	
	{
		IDs = new String[] { "BranaI", "BranaII" } ;
	}
		
	public ConvertDocxBrana() {
		super();
		this.initialize( "bidirectional/BranaI.txt", "bidirectional/BranaII.txt", "Brana I", "Brana II" );
	}

}
