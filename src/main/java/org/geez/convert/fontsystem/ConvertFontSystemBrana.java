package org.geez.convert.fontsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class ConvertFontSystemBrana extends ConvertFontSystemDuoFont {

	public static final Set<String> supportedFonts = new HashSet<String> ( Arrays.asList( "Brana I", "Brana II" ) );
	
	{
		IDs = new String[] { "BranaI", "BranaII" } ;
	}
		
	public ConvertFontSystemBrana() {
		super();
		this.initialize( "bidirectional/BranaI.txt", "bidirectional/BranaII.txt", "Brana I", "Brana II" );
	}

}
