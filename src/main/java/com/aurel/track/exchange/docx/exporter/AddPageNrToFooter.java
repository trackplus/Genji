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

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.FldChar;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STFldCharType;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Text;

public class AddPageNrToFooter {
	private static final Logger LOGGER = LogManager.getLogger(AddPageNrToFooter.class);
    //private static WordprocessingMLPackage wordMLPackage;
    //private static ObjectFactory factory;
 
    /**
     *  First we create the package and the factory. Then we create the footer.
     *  Finally we add two pages with text to the document and save it.
     */
    /*ublic static void main (String[] args) throws Exception {
        wordMLPackage = WordprocessingMLPackage.createPackage();
        //factory = Context.getWmlObjectFactory();
 
        Relationship relationship = createFooterPart();
        createFooterReference(relationship);
 
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
 
        documentPart.addParagraphOfText("Hello World!");
 
        addPageBreak(documentPart);
 
        documentPart.addParagraphOfText("This is page 2!");
        wordMLPackage.save(new File("src/main/files/HelloWord15.docx") );
    }*/
 
    static void addFooterWithPageNo(WordprocessingMLPackage wordMLPackage) {
    	ObjectFactory factory = Context.getWmlObjectFactory();
    	Relationship relationship = null;
		try {
			relationship = createFooterPart(wordMLPackage, factory);
		} catch (InvalidFormatException e) {
			LOGGER.error("Creating the footer part failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
        createFooterReference(wordMLPackage, relationship, factory);
    }
    
    /**
     *  As in the previous example, this method creates a footer part and adds it to
     *  the main document and then returns the corresponding relationship.
     *
     *  @return
     *  @throws InvalidFormatException
     */
    private static Relationship createFooterPart(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) throws InvalidFormatException {
        FooterPart footerPart = new FooterPart();
        footerPart.setPackage(wordMLPackage);
 
        footerPart.setJaxbElement(createFooterWithPageNr(factory));
 
        return wordMLPackage.getMainDocumentPart().addTargetPart(footerPart);
    }
 
    /**
     *  As in the previous example, we create a footer and a paragraph object. But
     *  this time, instead of adding text to a run, we add a field. And just as with
     *  the table of content, we have to add a begin and end character around the
     *  actual field with the page number. Finally we add the paragraph to the
     *  content of the footer and then return it.
     *
     * @return
     */
    public static Ftr createFooterWithPageNr(ObjectFactory factory) {
        Ftr ftr = factory.createFtr();
        P paragraph = factory.createP();
        addFieldBegin(paragraph, factory);
        addPageNumberField(paragraph, factory);
        addFieldEnd(paragraph, factory);
        ftr.getContent().add(paragraph);
        return ftr;
    }
 
    /**
     *  Creating the page number field is nearly the same as creating the field in
     *  the TOC example. The only difference is in the value. We use the PAGE
     *  command, which prints the number of the current page, together with the
     *  MERGEFORMAT switch, which indicates that the current formatting should be
     *  preserved when the field is updated.
     *
     * @param paragraph
     */
    private static void addPageNumberField(P paragraph, ObjectFactory factory) {
        R run = factory.createR();
        Text txt = new Text();
        txt.setSpace("preserve");
        txt.setValue(" PAGE   \\* MERGEFORMAT ");
        run.getContent().add(factory.createRInstrText(txt));
        paragraph.getContent().add(run);
    }
 
    /**
     * Every fields needs to be delimited by complex field characters. This method
     * adds the delimiter that precedes the actual field to the given paragraph.
     * @param paragraph
     */
    private static void addFieldBegin(P paragraph, ObjectFactory factory) {
        R run = factory.createR();
        FldChar fldchar = factory.createFldChar();
        fldchar.setFldCharType(STFldCharType.BEGIN);
        run.getContent().add(fldchar);
        paragraph.getContent().add(run);
    }
 
    /**
     * Every fields needs to be delimited by complex field characters. This method
     * adds the delimiter that follows the actual field to the given paragraph.
     * @param paragraph
     */
    private static void addFieldEnd(P paragraph, ObjectFactory factory) {
        FldChar fldcharend = factory.createFldChar();
        fldcharend.setFldCharType(STFldCharType.END);
        R run3 = factory.createR();
        run3.getContent().add(fldcharend);
        paragraph.getContent().add(run3);
    }
 
    /**
     * This method fetches the document final section properties, and adds a newly
     * created footer reference to them.
     *
     * @param relationship
     */
    public static void createFooterReference(WordprocessingMLPackage wordMLPackage, Relationship relationship, ObjectFactory factory){
 
        List<SectionWrapper> sections =
            wordMLPackage.getDocumentModel().getSections();
 
        SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
        // There is always a section wrapper, but it might not contain a sectPr
        if (sectPr==null ) {
            sectPr = factory.createSectPr();
            wordMLPackage.getMainDocumentPart().addObject(sectPr);
            sections.get(sections.size() - 1).setSectPr(sectPr);
        }
 
        FooterReference footerReference = factory.createFooterReference();
        footerReference.setId(relationship.getId());
        footerReference.setType(HdrFtrRef.DEFAULT);
        sectPr.getEGHdrFtrReferences().add(footerReference);
    }
 
    /**
     * Adds a page break to the document.
     *
     * @param documentPart
     */
    /*private static void addPageBreak(MainDocumentPart documentPart) {
        Br breakObj = new Br();
        breakObj.setType(STBrType.PAGE);
        P paragraph = factory.createP();
        paragraph.getContent().add(breakObj);
        documentPart.getJaxbElement().getBody().getContent().add(paragraph);
    }*/
}
