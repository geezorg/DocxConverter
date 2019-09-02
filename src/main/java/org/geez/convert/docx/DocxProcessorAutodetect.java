package org.geez.convert.docx;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.io.FilenameUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.geez.convert.fontsystem.ConvertFontSystem;
import org.geez.convert.fontsystem.ConvertFontSystemBrana;
import org.geez.convert.fontsystem.ConvertFontSystemFeedelGeezII;
import org.geez.convert.fontsystem.ConvertFontSystemFeedelGeezNewAB;
import org.geez.convert.fontsystem.ConvertFontSystemFeedelGeezigna;
import org.geez.convert.fontsystem.ConvertFontSystemGeezFont;
import org.geez.convert.fontsystem.ConvertFontSystemGeezTypeNet;
import org.geez.convert.fontsystem.ConvertFontSystemNCIC;
import org.geez.convert.fontsystem.ConvertFontSystemPowerGeez;
import org.geez.convert.fontsystem.ConvertFontSystemSamawerfa;
import org.geez.convert.fontsystem.ConvertFontSystemVisualGeez;
import org.geez.convert.fontsystem.ConvertFontSystemVisualGeez2000;




public class DocxProcessorAutodetect extends DocxProcessor {

	public DocxProcessorAutodetect(List<File> inputFileList) { super(inputFileList); }
	
	private Map<String,Class<?>> fontToConverterClassMap = new HashMap<String,Class<?>> ();
	
	public void readFonts() {
		if ( targetTypefaces.isEmpty() ) {
			for(String font: ConvertFontSystemBrana.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemBrana.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemFeedelGeezigna.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemFeedelGeezigna.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemFeedelGeezII.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemFeedelGeezII.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemFeedelGeezNewAB.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemFeedelGeezNewAB.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemGeezFont.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemGeezFont.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemGeezTypeNet.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemGeezTypeNet.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemNCIC.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemNCIC.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemPowerGeez.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemPowerGeez.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemSamawerfa.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemSamawerfa.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemVisualGeez.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemVisualGeez.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertFontSystemVisualGeez2000.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertFontSystemVisualGeez2000.class );
				targetTypefaces.add( font );
			}
		}

		Map<Class<?>, ConvertFontSystem> classToInstanceMap = new HashMap<Class<?>, ConvertFontSystem> ();
    	try {
	    	for(File file: inputFileList) {
	    		String extension = FilenameUtils.getExtension( file.getPath() );
	    		if( "docx".equals( extension) ) {
					WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load( file );		
					MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
					for( String font: documentPart.fontsInUse() ) {
						if( targetTypefaces.contains( font ) && !(fontToConverterMap.containsKey(font)) ) {
							Class<?> clazz = fontToConverterClassMap.get(font);
							if(! classToInstanceMap.containsKey(clazz) ) {
								classToInstanceMap.put( clazz, (ConvertFontSystem) clazz.getDeclaredConstructor().newInstance() );
							}
							fontToConverterMap.put( font, classToInstanceMap.get(clazz) );
						}
		       		}
	    		}
	    	}
    	}
    	catch( Docx4JException ex ) {
    		System.err.printf( "An error occured while reading documents.\n" + ex );
    	}
    	catch( IllegalAccessException ex ) {
    		System.err.printf( "An error occured while reading documents.\n" + ex );    		
    	}
    	catch ( InstantiationException ex ) {
    		System.err.printf( "An error occured while reading documents.\n" + ex );  
		} catch (Exception ex) {
    		System.err.printf( "An error occured while reading documents.\n" + ex ); 
		} 
    	
    	//
		return;
	}

}
