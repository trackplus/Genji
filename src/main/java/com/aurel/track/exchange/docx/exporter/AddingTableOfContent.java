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

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.wml.FldChar;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STFldCharType;
import org.docx4j.wml.Text;

public class AddingTableOfContent {
    //private static ObjectFactory factory;
 
    /**
     *  First we create the factory and the package and extract the document part
     *  from the package. Then we add the table of content, followed by some
     *  paragraphs with assorted heading styles. Finally we save the package.
     */
    public static void main(String[] args) throws Docx4JException {
        //factory = Context.getWmlObjectFactory();
        WordprocessingMLPackage wordMLPackage =
            WordprocessingMLPackage.createPackage();
        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
        addTableOfContent(documentPart);
        documentPart.addStyledParagraphOfText("Heading1", "Hello 1");
        documentPart.addStyledParagraphOfText("Heading2", "Hello 2");
        documentPart.addStyledParagraphOfText("Heading3", "Hello 3");
        documentPart.addStyledParagraphOfText("Heading1", "Hello 1");
        wordMLPackage.save(new File("src/main/files/HelloWord10.docx"));
    }
 
    /**
     *  Adds the table of content to the document.
     *
     *  First we create a paragraph. Then we add the indicator to mark the start of
     *  the field. Then we add the content of the field (with the actual table of
     *  content), followed by the indicator to mark the end of the field. Finally
     *  we add the paragraph to the JAXB elements of the given document part.
     *
     *  @param documentPart
     */
    static void addTableOfContent(MainDocumentPart documentPart) {
    	ObjectFactory factory = Context.getWmlObjectFactory();
        P paragraph = factory.createP();
        addFieldBegin(paragraph, factory);
        addTableOfContentField(paragraph, factory);
        addFieldEnd(paragraph, factory);
        documentPart.getJaxbElement().getBody().getContent().add(paragraph);
    }
 
    /**
     *  Adds the field that Word uses to create a table of content to the paragraph.
     *
     *  First we create a run and a text. Then we indicate that all spaces in the
     *  text are to be preserved and set the value to that of the TOC field.
     *  This field definition takes some arguments. The exact definition can be
     *  found in paragraph 17.16.5.58 of the Office Open XML standard. In this case we
     *  specify that we want to include all paragraphs formatted with headings of
     *  levels 1-3 (\0 1-3). We also specify that we want all entries to be
     *  hyperlinks (\h), that we want to hide tab leader and page numbers in Web
     *  layout view (\z), and that we want to use the applied paragraph outline
     *  level (\\u).
     *  Finally we take the text and use it to create a JAXB element containing text
     *  and add this to the run, which we then add to the given paragraph.
     *  @param paragraph
     */
    private static void addTableOfContentField(P paragraph, ObjectFactory factory) {
        R run = factory.createR();
        Text txt = new Text();
        txt.setSpace("preserve");
        txt.setValue("TOC \\o \"1-3\" \\h \\z \\u");
        run.getContent().add(factory.createRInstrText(txt));
        paragraph.getContent().add(run);
    }
 
    /**
     *  Every fields needs to be delimited by complex field characters. This method
     *  adds the delimiter that precedes the actual field to the given paragraph.
     *
     *  Once again, we start by creating a run. Then we create a field character to
     *  mark the beginning of the field and mark the field as dirty as we want the
     *  content to be updated after the entire document is generated.
     *  Finally we convert the field character to a JAXB element and add it to
     *  the run, and add the run to the paragraph.
     *
     *  @param paragraph
     */
    private static void addFieldBegin(P paragraph, ObjectFactory factory) {
        R run = factory.createR();
        FldChar fldchar = factory.createFldChar();
        fldchar.setFldCharType(STFldCharType.BEGIN);
        fldchar.setDirty(true);
        run.getContent().add(getWrappedFldChar(fldchar));
        paragraph.getContent().add(run);
    }
 
    /**
     *  Every fields needs to be delimited by complex field characters. This method
     *  adds the delimiter that follows the actual field to the given paragraph.
     *
     *  As before, we start by creating a run. Then we create a field character to
     *  mark the end of the field. Finally we convert the field character to a
     *  JAXB element and add it to the run, and add the run to the paragraph.
     *
     *  @param paragraph
     */
    private static void addFieldEnd(P paragraph, ObjectFactory factory) {
        R run = factory.createR();
        FldChar fldcharend = factory.createFldChar();
        fldcharend.setFldCharType(STFldCharType.END);
        run.getContent().add(getWrappedFldChar(fldcharend));
        paragraph.getContent().add(run);
    }
 
    /**
     *  Convenience method that creates the JAXBElement to contain the given complex
     *  field character.
     *
     *  @param fldchar
     *  @return
     */
    public static JAXBElement getWrappedFldChar(FldChar fldchar) {
        return new JAXBElement(new QName(Namespaces.NS_WORD12, "fldChar"),
                             FldChar.class, fldchar);
    }
}
