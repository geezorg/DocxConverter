package org.geez.convert.docx;

import java.util.ArrayList;
import java.util.List;

import org.docx4j.TraversalUtil.CallbackImpl;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.ParaRPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;

public class  PropertiesFinder extends CallbackImpl {
    
    public List<RFonts> results = new ArrayList<RFonts>(); 
    
    @Override
    public List<Object> apply(Object o) {
    	if (o instanceof org.docx4j.wml.R) {
    		R r = (org.docx4j.wml.R)o;
			RPr rpr = r.getRPr();
			if ( (rpr == null) || (rpr.getRFonts() == null) ) return null;
			results.add( rpr.getRFonts() );
		}
		else if  (o instanceof org.docx4j.wml.P) {
			P p = (org.docx4j.wml.P)o;
			PPr ppr = p.getPPr();
			if (ppr == null ) return null;
			ParaRPr rpr = ppr.getRPr();
			if ( (rpr == null) || (rpr.getRFonts() == null) ) return null;
			results.add( rpr.getRFonts() );
		}
        return null;
    }
}  
