package org.geez.convert.docx;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.io.FilenameUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;




public class DocxProcessorAutodetect extends DocxProcessor {

	public DocxProcessorAutodetect(List<File> inputFileList) { super(inputFileList); }
	
	private Map<String,Class<?>> fontToConverterClassMap = new HashMap<String,Class<?>> ();
	
	public void readFonts() {
		if ( targetTypefaces.isEmpty() ) {
			for(String font: ConvertDocxBrana.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxBrana.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxFeedelGeezigna.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxFeedelGeezigna.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxFeedelGeezII.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxFeedelGeezII.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxFeedelGeezNewAB.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxFeedelGeezNewAB.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxGeezFont.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxGeezFont.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxGeezTypeNet.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxGeezTypeNet.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxNCIC.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxNCIC.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxPowerGeez.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxPowerGeez.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxSamawerfa.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxSamawerfa.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxVisualGeez.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxVisualGeez.class );
				targetTypefaces.add( font );
			}
			for(String font: ConvertDocxVisualGeez2000.supportedFonts) {
				fontToConverterClassMap.put( font, ConvertDocxVisualGeez2000.class );
				targetTypefaces.add( font );
			}
		}

		Map<Class<?>, ConvertDocx> classToInstanceMap = new HashMap<Class<?>, ConvertDocx> ();
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
								classToInstanceMap.put( clazz, (ConvertDocx)clazz.newInstance() );
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
		}
    	
    	//
		return;
	}

}
