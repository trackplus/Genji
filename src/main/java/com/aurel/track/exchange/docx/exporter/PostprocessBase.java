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


import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import javax.xml.bind.JAXBElement;

import org.docx4j.dml.CTNonVisualDrawingProps;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
import org.docx4j.utils.TraversalUtilVisitor;
import org.docx4j.wml.Body;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.CTMarkupRange;
import org.docx4j.wml.CTSimpleField;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Document;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;

import com.aurel.track.exchange.docx.exporter.ImageOrTableCaption.ALIGN;
import com.aurel.track.resources.LocalizeUtil;


/**
 * Base class for post-processing images and tables
 * @author Tamas
 *
 */
public class PostprocessBase {
		
	protected String figurePrefix = null;
	protected String captionStyleId = null;
	protected int bookmarkID = 0;
	protected SortedMap<String, ImageOrTableCaption> captionsMap = null;
	
	/**
	 * Gets a center aligned PPr for P
	 * @return
	 */
	protected PPr setAlignment(ALIGN align) {
		ObjectFactory wmlObjectFactory = new ObjectFactory();
		PPr ppr = wmlObjectFactory.createPPr();
	    Jc jc = wmlObjectFactory.createJc();
	    JcEnumeration jcEnumeration = JcEnumeration.CENTER;
	    if (align!=null) {
	    	switch (align) {
			case LEFT:
				jcEnumeration = JcEnumeration.LEFT;
				break;
			case RIGHT:
				jcEnumeration = JcEnumeration.RIGHT;
				break;
			}
	    }
	    ppr.setJc(jc); 
	    jc.setVal(jcEnumeration);
		return ppr;
	}
	
	/**
	 * Creates a caption for image
	 * @param figurePrefix
	 * @param figureName
	 * @param imageCaption
	 * @param captionStyleId
	 * @return
	 */
	protected P createCaption(String figurePrefix, ImageOrTableCaption imageCaption, String captionStyleId) {
		bookmarkID++;
		ObjectFactory wmlObjectFactory = new ObjectFactory();
	    P p = wmlObjectFactory.createP();
	    //Common name prefix for all figures/tables
	    if (figurePrefix!=null) {
		    R r = wmlObjectFactory.createR();
		    p.getContent().add(r);
		    Text text = wmlObjectFactory.createText();
		    JAXBElement<org.docx4j.wml.Text> textWrapped = wmlObjectFactory.createRT(text);
		    r.getContent().add(textWrapped);
		    text.setValue(figurePrefix + " ");
		    text.setSpace("preserve");
	    }
	    //heading 1 counter
	    CTSimpleField styleRefField = wmlObjectFactory.createCTSimpleField();
	    JAXBElement<org.docx4j.wml.CTSimpleField> styleRefFieldWrapped = wmlObjectFactory.createPFldSimple(styleRefField);
	    p.getContent().add(styleRefFieldWrapped);
	    styleRefField.setInstr("STYLEREF  \"Heading 1\" \\n");
	    R styleRefRun = wmlObjectFactory.createR();
	    styleRefField.getContent().add(styleRefRun);
	    Text styleRefFieldText = wmlObjectFactory.createText();
	    JAXBElement<org.docx4j.wml.Text> styleRefTextWrapped = wmlObjectFactory.createRT(styleRefFieldText);
	    styleRefRun.getContent().add(styleRefTextWrapped);
	    styleRefFieldText.setValue(String.valueOf(imageCaption.getChapterNo()));
	
	    //hyphen between chapter counter (heading 1 counter) and sequence within chapter 
	    R spaceRun = wmlObjectFactory.createR();
	    p.getContent().add(spaceRun);
	    Text text = wmlObjectFactory.createText();
	    JAXBElement<org.docx4j.wml.Text> textWrapped = wmlObjectFactory.createRT(text);
	    spaceRun.getContent().add(textWrapped);
	    text.setValue("-");
	    
	    //sequence within chapter (within heading 1)
	    CTSimpleField seqField = wmlObjectFactory.createCTSimpleField();
	    JAXBElement<org.docx4j.wml.CTSimpleField> seqFieldWrapped = wmlObjectFactory.createPFldSimple(seqField);
	    p.getContent().add(seqFieldWrapped);
	    R seqRun = wmlObjectFactory.createR();
	    seqField.getContent().add(seqRun);
	    Text seqText = wmlObjectFactory.createText();
	    JAXBElement<org.docx4j.wml.Text> seqTextWrapped = wmlObjectFactory.createRT(seqText);
	    seqRun.getContent().add(seqTextWrapped);
	    seqText.setValue(String.valueOf(imageCaption.getCounterWithinChapter()));
	    RPr rpr = wmlObjectFactory.createRPr();
	    seqRun.setRPr(rpr);
	    // Create object for noProof
	    BooleanDefaultTrue booleandefaulttrue = wmlObjectFactory.createBooleanDefaultTrue();
	    rpr.setNoProof(booleandefaulttrue);
	    seqField.setInstr(" SEQ " + figurePrefix + " \\* ARABIC \\s 1");
	    //user entered caption text (if exists)
	    if (imageCaption!=null && imageCaption.getCaption()!=null && !"".equals(imageCaption.getCaption())) {
		    // Create object for r
		    R r4 = wmlObjectFactory.createR();
		    p.getContent().add(r4);
		    // Create object for t (wrapped in JAXBElement)
		    Text text4 = wmlObjectFactory.createText();
		    JAXBElement<org.docx4j.wml.Text> textWrapped4 = wmlObjectFactory.createRT(text4);
		    r4.getContent().add(textWrapped4);
		    text4.setValue(": ");
		    text4.setSpace("preserve");
		    // Create object for r
		    R r5 = wmlObjectFactory.createR();
		    p.getContent().add(r5);
		    // Create object for t (wrapped in JAXBElement)
		    Text text5 = wmlObjectFactory.createText();
		    JAXBElement<org.docx4j.wml.Text> textWrapped5 = wmlObjectFactory.createRT(text5);
		    r5.getContent().add(textWrapped5);
		    text5.setValue(imageCaption.getCaption());
		    text5.setSpace("preserve");
	    }
	    CTBookmark bookmark = wmlObjectFactory.createCTBookmark();
	    JAXBElement<org.docx4j.wml.CTBookmark> bookmarkWrapped = wmlObjectFactory.createPBookmarkStart(bookmark);
	    p.getContent().add(bookmarkWrapped);
	    bookmark.setName(figurePrefix+bookmarkID);
	    bookmark.setId(BigInteger.valueOf(bookmarkID));
	    // Create object for bookmarkEnd (wrapped in JAXBElement)
	    CTMarkupRange markuprange = wmlObjectFactory.createCTMarkupRange();
	    JAXBElement<org.docx4j.wml.CTMarkupRange> markuprangeWrapped = wmlObjectFactory.createPBookmarkEnd(markuprange);
	    p.getContent().add(markuprangeWrapped);
	    markuprange.setId(BigInteger.valueOf(bookmarkID));
	    //align to center and set caption style
	    PPr ppr = setAlignment(imageCaption.getAlign());	 
	    p.setPPr(ppr);
	    if (captionStyleId!=null) {
		    // Create object for pStyle
		    PPrBase.PStyle pprbasepstyle = wmlObjectFactory.createPPrBasePStyle();
		    ppr.setPStyle(pprbasepstyle);
		    pprbasepstyle.setVal(captionStyleId);
	    }
	    return p;
	 }
}
