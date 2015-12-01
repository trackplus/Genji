/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.exchange.docx.exporter;

import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
import org.docx4j.utils.TraversalUtilVisitor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.ParaRPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.RStyle;
import org.docx4j.wml.Text;

/**
 * Apply inline style for inline content
 * @author Tamas
 *
 */
public class InlineStyleUtil {
	private String inlineStyleId;
	private boolean inlineContentStyleIsParagraphStyle = false;
	private boolean textFound = false;
	private boolean runWithTextFound = false;
	/**
	 * Adds caption to the image
	 * @param wordMLPackage
	 * @param locale
	 * @param captionStyleId
	 * @throws Exception
	 */
	public void addInlineStyle(List<Object> contents, Locale locale, String captionStyleId, boolean inlineContentStyleIsParagraphStyle) {
		if (captionStyleId!=null) {
			this.inlineStyleId = captionStyleId;
			this.inlineContentStyleIsParagraphStyle = inlineContentStyleIsParagraphStyle;
			SingleTraversalUtilVisitorCallback paragraphVisitor = new SingleTraversalUtilVisitorCallback(new TraversalUtilParagraphVisitor());
			paragraphVisitor.walkJAXBElements(contents);
		}
	}

	/**
	 * Apply the inline style for paragraph 
	 * @author Tamas
	 *
	 */
	private class TraversalUtilParagraphVisitor extends TraversalUtilVisitor<P> {
		@Override
		public void apply(P p, Object parent, List<Object> siblings) {
			//apply the inline style for each run
			SingleTraversalUtilVisitorCallback runVisitor = new SingleTraversalUtilVisitorCallback(new TraversalUtilRunVisitor());
			runVisitor.walkJAXBElements(p);
			ObjectFactory wmlObjectFactory = new ObjectFactory();
			PPr ppr = p.getPPr();
			if (ppr==null) {
				ppr = wmlObjectFactory.createPPr();
			    p.setPPr(ppr);
			}
			//apply the inline style for run at paragraph level
		    if (runWithTextFound) {
		    	if (inlineContentStyleIsParagraphStyle) {
				    // Create object for pStyle
				    PPrBase.PStyle pprbasepstyle = wmlObjectFactory.createPPrBasePStyle();
				    ppr.setPStyle(pprbasepstyle);
				    pprbasepstyle.setVal(inlineStyleId);
				    
		    	} else {
	            // Create object for rStyle
	            RStyle rstyle = wmlObjectFactory.createRStyle(); 
	            ParaRPr paraRpr = ppr.getRPr();
	            if (paraRpr==null) {
			    	paraRpr = wmlObjectFactory.createParaRPr();
			    	ppr.setRPr(paraRpr);
			    }
	            paraRpr.setRStyle(rstyle); 
	            rstyle.setVal(inlineStyleId); 
		    	}
		    }
		    runWithTextFound = false;
		}
	}
	
	/**
	 * Set the inline style on each run with text
	 * @author Tamas
	 *
	 */
	private class TraversalUtilRunVisitor extends TraversalUtilVisitor<R> {
		@Override
		public void apply(R r, Object parent, List<Object> siblings) {
			SingleTraversalUtilVisitorCallback runVisitor = new SingleTraversalUtilVisitorCallback(new TraversalUtiltextVisitor());
			runVisitor.walkJAXBElements(r);
			if (textFound) {
				runWithTextFound = true;
				ObjectFactory wmlObjectFactory = new ObjectFactory();
				RPr rpr = wmlObjectFactory.createRPr();
				r.setRPr(rpr);
	            // Create object for rStyle
	            RStyle rstyle = wmlObjectFactory.createRStyle(); 
	            rpr.setRStyle(rstyle); 
	            rstyle.setVal(inlineStyleId);
			}
			textFound = false;
		}
	}
	
	/**
	 * Whether the run has text
	 * @author Tamas
	 *
	 */
	private class TraversalUtiltextVisitor extends TraversalUtilVisitor<Text> {
		@Override
		public void apply(Text text, Object parent, List<Object> siblings) {
			textFound = true; 
		}
	}
}
