/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.exchange.docx.exporter;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.CTMarkupRange;
import org.docx4j.wml.CTSimpleField;
import org.docx4j.wml.Jc;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;

import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Adding attached images to a docx 
 * @author Tamas
 *
 */
public class ImageUtil {
	private static final Logger LOGGER = LogManager.getLogger(ImageUtil.class);
	
	static int addAttachments(WordprocessingMLPackage wordMLPackage, List<TAttachmentBean> attachmentBeanList, Integer workItemID, String captionStyleId, int id, Locale locale) {
		if (attachmentBeanList!=null) {
			for (TAttachmentBean attachmentBean : attachmentBeanList) {
				Integer attachmentID = attachmentBean.getObjectID();
				boolean isImage = AttachBL.isImage(attachmentBean);
				if (isImage) {
					String realNameWithPath = AttachBL.getFullFileName(null, attachmentBean.getObjectID(), workItemID);
					String originalFilename = attachmentBean.getFileName();
					try {
						addImage(wordMLPackage, realNameWithPath, originalFilename, attachmentBean.getDescription(), attachmentID, captionStyleId, id, locale);
						id = id+1;
					} catch (Exception e) {
						LOGGER.warn("Adding the file " + originalFilename + " for item " + workItemID + " and attachmentID " + attachmentID + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		return id;
	}
	
	public static byte[] getImageBytes(String filePath) throws IOException {
		File file = new File(filePath);
		// Our utility method wants that as a byte array
		java.io.InputStream is = new java.io.FileInputStream(file );
        long length = file.length();    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        if (length > Integer.MAX_VALUE) {
        	LOGGER.warn("File " + filePath + " too large and will not be added");
        	is.close();
        	return null;
        } else {
	        byte[] bytes = new byte[(int)length];
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length
	               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	            offset += numRead;
	        }
	        // Ensure all the bytes have been read in
	        if (offset < bytes.length) {
	            System.out.println("Could not completely read file "+file.getName());
	        }
	        is.close();
	        return bytes;
        }
	}
	
	/**
	 * Add the image to docx
	 * @param wordMLPackage
	 * @param realNameWithPath
	 * @param originalFilename
	 * @param attachmentID
	 * @param id
	 * @param locale
	 * @throws Exception
	 */
	private static void addImage(WordprocessingMLPackage wordMLPackage, String realNameWithPath, String originalFilename,
			String description, Integer attachmentID, String captionStyleId, int id, Locale locale) throws Exception {
		File file = new File(realNameWithPath);
		// Our utility method wants that as a byte array
		java.io.InputStream is = new java.io.FileInputStream(file );
        long length = file.length();    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        if (length > Integer.MAX_VALUE) {
        	LOGGER.warn("File " + originalFilename + " too large and will not be added");
        } else {
	        byte[] bytes = new byte[(int)length];
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length
	               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	            offset += numRead;
	        }
	        // Ensure all the bytes have been read in
	        if (offset < bytes.length) {
	            System.out.println("Could not completely read file "+file.getName());
	        }
	        String filenameHint = originalFilename;
	        String altText = null;
	        int id1 = id;
	        int id2 = id;//+1;
	        //no width specified
	        org.docx4j.wml.P imageP = newImage( wordMLPackage, bytes, 
	        		filenameHint, altText, 
	    			id1, id2);
			wordMLPackage.getMainDocumentPart().addObject(imageP);
			org.docx4j.wml.P captionP = createCaption(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.exportDocx.figure", locale),
					id, originalFilename, description, captionStyleId, id);
			wordMLPackage.getMainDocumentPart().addObject(captionP);
			/*if (description!=null && !"".equals(description)) {
				org.docx4j.wml.P descriptionP =createImageDescription(description, captionStyleId);
				wordMLPackage.getMainDocumentPart().addObject(descriptionP);
			}*/
        }
        is.close();
	}

	/**
	 * Create image, without specifying width
	 */
	private static org.docx4j.wml.P newImage(WordprocessingMLPackage wordMLPackage,
			byte[] bytes,
			String filenameHint, String altText, 
			int id1, int id2) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
        Inline inline = imagePart.createImageInline( filenameHint, altText, id1, id2, false);
        // Now add the inline in w:p/w:r/w:drawing
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.P  p = factory.createP();
		org.docx4j.wml.R  run = factory.createR();		
		p.getContent().add(run);        
		org.docx4j.wml.Drawing drawing = factory.createDrawing();		
		run.getContent().add(drawing);		
		drawing.getAnchorOrInline().add(inline);
		return p;
	}	
	
	/**
	 * Creates the caption/bookmark/figure sequence for an image
	 * add this (see caption markup in document.xml):
	 * <w:p w:rsidR="00324188" w:rsidRDefault="00E8619E" w:rsidP="00E8619E">
			<w:pPr>
				<w:pStyle w:val="Caption"/>
			</w:pPr>
			<w:r>
				<w:t xml:space="preserve">Figure </w:t>
			</w:r>
			<w:fldSimple w:instr=" SEQ Figure \* ARABIC ">
				<w:r>
					<w:rPr>
						<w:noProof/>
					</w:rPr>
					<w:t>1</w:t>
				</w:r>
			</w:fldSimple>
			<w:bookmarkStart w:id="2" w:name="_GoBack"/>
			<w:bookmarkEnd w:id="2"/>
		</w:p>
	 * 
	 * @param figurePrefix
	 * @param figureNumber
	 * @param figureName
	 * @param bookmarkID
	 * @return
	 */
	private static P createCaption(String figurePrefix, int figureNumber, String figureName, String description, String captionStyleId, int bookmarkID) {
	    ObjectFactory wmlObjectFactory = new ObjectFactory();
	    P p = wmlObjectFactory.createP();
	    if (figurePrefix!=null) {
		    // Create object for r
		    R r = wmlObjectFactory.createR();
		    p.getContent().add(r);
		    // Create object for t (wrapped in JAXBElement)
		    Text text = wmlObjectFactory.createText();
		    JAXBElement<org.docx4j.wml.Text> textWrapped = wmlObjectFactory.createRT(text);
		    r.getContent().add(textWrapped);
		    text.setValue(figurePrefix + " ");
		    text.setSpace("preserve");
	    }
	    // Create object for fldSimple (wrapped in JAXBElement)
	    CTSimpleField simplefield = wmlObjectFactory.createCTSimpleField();
	    JAXBElement<org.docx4j.wml.CTSimpleField> simplefieldWrapped = wmlObjectFactory.createPFldSimple(simplefield);
	    p.getContent().add(simplefieldWrapped);
	    // Create object for r
	    R r2 = wmlObjectFactory.createR();
	    simplefield.getContent().add(r2);
	    // Create object for t (wrapped in JAXBElement)
	    Text text2 = wmlObjectFactory.createText();
	    JAXBElement<org.docx4j.wml.Text> textWrapped2 = wmlObjectFactory.createRT(text2);
	    r2.getContent().add(textWrapped2);
	    text2.setValue(Integer.valueOf(figureNumber).toString());
	    // Create object for rPr
	    RPr rpr = wmlObjectFactory.createRPr();
	    r2.setRPr(rpr);
	    // Create object for noProof
	    BooleanDefaultTrue booleandefaulttrue = wmlObjectFactory.createBooleanDefaultTrue();
	    rpr.setNoProof(booleandefaulttrue);
	    simplefield.setInstr(" SEQ " + figurePrefix + " \\* ARABIC ");
	    if (description!=null && !"".equals(description)) {
		    // Create object for r
		    R r4 = wmlObjectFactory.createR();
		    p.getContent().add(r4);
		    // Create object for t (wrapped in JAXBElement)
		    Text text4 = wmlObjectFactory.createText();
		    JAXBElement<org.docx4j.wml.Text> textWrapped4 = wmlObjectFactory.createRT(text4);
		    r4.getContent().add(textWrapped4);
		    text4.setValue(" - ");
		    text4.setSpace("preserve");
		    // Create object for r
		    R r5 = wmlObjectFactory.createR();
		    p.getContent().add(r5);
		    // Create object for t (wrapped in JAXBElement)
		    Text text5 = wmlObjectFactory.createText();
		    JAXBElement<org.docx4j.wml.Text> textWrapped5 = wmlObjectFactory.createRT(text5);
		    r5.getContent().add(textWrapped5);
		    text5.setValue(description);
		    text5.setSpace("preserve");
		    // Create object for bookmarkStart (wrapped in JAXBElement)
	    }
	    CTBookmark bookmark = wmlObjectFactory.createCTBookmark();
	    JAXBElement<org.docx4j.wml.CTBookmark> bookmarkWrapped = wmlObjectFactory.createPBookmarkStart(bookmark);
	    p.getContent().add(bookmarkWrapped);
	    bookmark.setName(figurePrefix+figureName);
	    bookmark.setId(BigInteger.valueOf(bookmarkID));
	    // Create object for bookmarkEnd (wrapped in JAXBElement)
	    CTMarkupRange markuprange = wmlObjectFactory.createCTMarkupRange();
	    JAXBElement<org.docx4j.wml.CTMarkupRange> markuprangeWrapped = wmlObjectFactory.createPBookmarkEnd(markuprange);
	    p.getContent().add(markuprangeWrapped);
	    markuprange.setId(BigInteger.valueOf(bookmarkID));
	    // Create object for pPr
	    PPr ppr = wmlObjectFactory.createPPr();
	    p.setPPr(ppr);
	    if (captionStyleId!=null) {
		    // Create object for pStyle
		    PPrBase.PStyle pprbasepstyle = wmlObjectFactory.createPPrBasePStyle();
		    ppr.setPStyle(pprbasepstyle);
		    pprbasepstyle.setVal(captionStyleId);
	    }
	    // Create object for jc
	    Jc jc = wmlObjectFactory.createJc();
	    ppr.setJc(jc);
	    jc.setVal(org.docx4j.wml.JcEnumeration.LEFT);
	    return p;
	 }
	
	/*private static P createImageDescription(String description, String captionStyleId) {
		ObjectFactory wmlObjectFactory = new ObjectFactory();
	    P p = wmlObjectFactory.createP();
	    if (description!=null) {
	    	// Create object for r
		    R r = wmlObjectFactory.createR();
		    p.getContent().add(r);
		    // Create object for t (wrapped in JAXBElement)
		    Text text = wmlObjectFactory.createText();
		    JAXBElement<org.docx4j.wml.Text> textWrapped = wmlObjectFactory.createRT(text);
		    r.getContent().add(textWrapped);
		    text.setValue(description);
		    text.setSpace("preserve");
	    }
	    // Create object for pPr
	    PPr ppr = wmlObjectFactory.createPPr();
	    p.setPPr(ppr);
	    if (captionStyleId!=null) {
		    // Create object for pStyle
		    PPrBase.PStyle pprbasepstyle = wmlObjectFactory.createPPrBasePStyle();
		    ppr.setPStyle(pprbasepstyle);
		    pprbasepstyle.setVal(captionStyleId);
	    }
	    Jc jc = wmlObjectFactory.createJc();
	    ppr.setJc(jc);
	    jc.setVal(org.docx4j.wml.JcEnumeration.LEFT);
	    return p;
	}*/
}
