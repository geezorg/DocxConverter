package org.geez.convert.docx;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.XmlUtils;
import org.docx4j.TraversalUtil.CallbackImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.PStyle;
import org.docx4j.wml.ParaRPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Style;
import org.docx4j.wml.Text;

public class  StyledTextFinder extends CallbackImpl {
    
    private Map<String,String> fontToIdMap = new HashMap<String,String>();
    public Map<Text,String> results = new HashMap<Text,String>();
    
    public void readStyles( WordprocessingMLPackage  wordMLPackage,  List<String> targetTypefaces, String fontOut ) {
    	StyleDefinitionsPart sdp = wordMLPackage.getMainDocumentPart().getStyleDefinitionsPart();	
    	boolean isSet = false;
    	List<Style> styleList = sdp.getJaxbElement().getStyle();
    	for (Style style : styleList) {
    		String name = style.getName().getVal();
    		String id = sdp.getIDForStyleName(name);
    		RPr rpr = style.getRPr();
    		if( (rpr != null) && (rpr.getRFonts() != null) ) {
    			RFonts rfonts = rpr.getRFonts();
    			String ascii = rfonts.getAscii();
    			if( (ascii != null) && targetTypefaces.contains( ascii ) ) {
    				fontToIdMap.put( id, ascii );
    				rfonts.setAscii( fontOut );
    				isSet = true;
    			}
    			String hAnsi = rfonts.getHAnsi();
    			if( (hAnsi != null) &&  targetTypefaces.contains( hAnsi ) ) {
    				if(! isSet)  {
    					fontToIdMap.put( id, hAnsi );
    				}
    				rfonts.setHAnsi( fontOut );
    				isSet = true;
    			}
    			String cs = rfonts.getCs();
    			if( (cs != null) &&  targetTypefaces.contains( cs ) ) {
    				if(! isSet)  {
    					fontToIdMap.put( id, hAnsi );
    				}
    				rfonts.setCs( fontOut );
    				isSet = true;
    			}
    			String eastAsia = rfonts.getEastAsia();
    			if( (hAnsi != null) &&  targetTypefaces.contains( eastAsia ) ) {
    				if(! isSet)  {
    					fontToIdMap.put( id, eastAsia );
    				}
    				rfonts.setEastAsia( fontOut );
    				isSet = true;
    			}
    			
    		}
    		if( style.getBasedOn() != null ) {
    			String basedOn = style.getBasedOn().getVal();
    			if( fontToIdMap.containsKey( basedOn ) ) {
    				fontToIdMap.put( basedOn, fontToIdMap.get( basedOn ) );
    			}
    		}
    	}
    	
    	
    }
    
    public boolean hasStyles() { 
    	return !( fontToIdMap.isEmpty() );
    }
    
    public void clearResults() {
    	results.clear();
    }
    
    
    @Override
    public List<Object> apply(Object o) {
    	if  (o instanceof org.docx4j.wml.P) {
			P p = (org.docx4j.wml.P)o;
			
			PPr ppr = p.getPPr();
			if (ppr == null) return null;
			String styleName = "Normal";

			if ( (ppr != null) && (ppr.getPStyle() != null)) {
				PStyle style = ppr.getPStyle();
				styleName = style.getVal();
			}
			
			if( fontToIdMap.containsKey( styleName ) ) {
				ParaRPr prpr = ppr.getRPr();
				// if the rpr has an rFonts setting then we have already visited
				// this text node and should not re-process.  Though it would be
				// to have both a w:style and w:rFonts set:
				if ( (prpr != null) && (prpr.getRFonts() != null) ) return null;  // we've been here
				List<Object> pObjects = p.getContent();
				for(Object pobj: pObjects) {
					if( pobj instanceof org.docx4j.wml.R ) {
						R r = (org.docx4j.wml.R)pobj;
						RPr rpr = r.getRPr();
						if ( (rpr != null) && (rpr.getRFonts() != null) ) continue;  // we've been here
						List<Object> rObjects = r.getContent();
						for(Object robj: rObjects) {
							Object tobj = XmlUtils.unwrap(robj);
							if ( tobj instanceof org.docx4j.wml.Text ) {
								results.put( (org.docx4j.wml.Text)tobj, fontToIdMap.get(styleName) );
							}
						}
					}
					else {
						// w:t node is a direct child of the w:p node:
						Object tobj = XmlUtils.unwrap(pobj);
						if( tobj instanceof org.docx4j.wml.Text ) {
							results.put( (org.docx4j.wml.Text)tobj, fontToIdMap.get(styleName) );
						}
					}
				}
			}
		}
        return null;
    }
}  
