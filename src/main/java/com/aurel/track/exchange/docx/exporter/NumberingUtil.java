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

import java.io.IOException;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.jaxb.Context;
import org.docx4j.jaxb.JaxbValidationEventHandler;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.utils.ResourceUtils;
import org.docx4j.wml.Numbering;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.NumPr;
import org.docx4j.wml.PPrBase.NumPr.Ilvl;
import org.docx4j.wml.PPrBase.NumPr.NumId;
import org.docx4j.wml.Style;

public class NumberingUtil {
	private static final Logger LOGGER = LogManager.getLogger(NumberingUtil.class);
	
	
	static void setNumbering(WordprocessingMLPackage newPkg) {
		java.io.InputStream is = null;
		try {
			is = ResourceUtils.getResource("com/aurel/track/exchange/docx/exporter/numbering.xml");
		} catch (IOException e) {
			LOGGER.error("Getting the KnownStyles.xml failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}                  
	    JAXBContext jc = Context.jc;
	    Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jc.createUnmarshaller();
		} catch (JAXBException e) {
			LOGGER.error("Creating a JAXB unmarshaller failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}         
	    try {
			unmarshaller.setEventHandler(new JaxbValidationEventHandler());
		} catch (JAXBException e) {
			LOGGER.error("Setting the event handler for JAXB unmarshaller failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	    Numbering numbering =  null;
	    try {
			numbering = (Numbering)unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			LOGGER.error("Unmarshalling the numbering.xml failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
	    NumberingDefinitionsPart numberingDefinitionsPart = null;
		try {
			numberingDefinitionsPart = new NumberingDefinitionsPart();
		} catch (InvalidFormatException e) {
			LOGGER.error("Creating the styles definition part failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	    numberingDefinitionsPart.setPackage(newPkg);
	    numberingDefinitionsPart.setJaxbElement(numbering);
	    try {
			newPkg.getMainDocumentPart().addTargetPart(numberingDefinitionsPart);
		} catch (InvalidFormatException e) {
			LOGGER.error("Adding the target part to MainDocumentPart failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Adds numbering to the style
	 * @param style
	 * @param numId the id of numbering definition instance
	 * @param ilvl the level
	 */
	static void addNumberingToStyle(Style style, BigInteger numId, BigInteger ilvl) {
		ObjectFactory factory = Context.getWmlObjectFactory();
		//Create and add <w:pPr> to style
		PPr ppr = factory.createPPr();	    
		style.setPPr(ppr);
		//Create and add <w:numPr>
	    NumPr numPr =  factory.createPPrBaseNumPr();
	    ppr.setNumPr(numPr);
	    // The <w:ilvl> element
	    Ilvl ilvlElement = factory.createPPrBaseNumPrIlvl();
	    numPr.setIlvl(ilvlElement);
	    ilvlElement.setVal(ilvl);
	    // The <w:numId> element
	    NumId numIdElement = factory.createPPrBaseNumPrNumId();
	    numPr.setNumId(numIdElement);
	    numIdElement.setVal(numId);
	}
	
	
	static final String initialNumbering = "<w:numbering xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
		    + "<w:abstractNum w:abstractNumId=\"0\">"
		    + "<w:nsid w:val=\"2DD860C0\"/>"
		    + "<w:multiLevelType w:val=\"multilevel\"/>"
		    + "<w:tmpl w:val=\"0409001D\"/>"
		    + "<w:lvl w:ilvl=\"0\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"decimal\"/>"
		        + "<w:lvlText w:val=\"%1. \"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"1\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerLetter\"/>"
		        + "<w:lvlText w:val=\"%2.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"720\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"2\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerRoman\"/>"
		        + "<w:lvlText w:val=\"%3.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"1080\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"3\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"decimal\"/>"
		        + "<w:lvlText w:val=\"%4.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"1440\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"4\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerLetter\"/>"
		        + "<w:lvlText w:val=\"%5.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"1800\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"5\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerRoman\"/>"
		        + "<w:lvlText w:val=\"%6.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"2160\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"6\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"decimal\"/>"
		        + "<w:lvlText w:val=\"%7.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"2520\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"7\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerLetter\"/>"
		        + "<w:lvlText w:val=\"%8.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"2880\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"8\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerRoman\"/>"
		        + "<w:lvlText w:val=\"%9.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"3240\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		+ "</w:abstractNum>"
		+ "<w:num w:numId=\"1\">"
		    + "<w:abstractNumId w:val=\"0\"/>"
		 + "</w:num>"
		+ "</w:numbering>";
}

/**
 * Number formats: numFmt
0 decimal
1 upperRoman
2 lowerRoman
3 upperLetter
4 lowerLetter
5 ordinal
6 cardinalText
7 ordinalText
8 hex
9 chicago
10 ideographDigital
11 japaneseCounting
12 Aiueo
13 Iroha
14 decimalFullWidth
15 decimalHalfWidth
16 japaneseLegal
17 japaneseDigitalTenThousand
18 decimalEnclosedCircle
19 decimalFullWidth2
20 aiueoFullWidth
21 irohaFullWidth
22 decimalZero
23 bullet
24 ganada
25 chosung
26 decimalEnclosedFullstop
27 decimalEnclosedParen
28 decimalEnclosedCircleChinese
29 ideographEnclosedCircle
30 ideographTraditional
31 ideographZodiac
32 ideographZodiacTraditional
33 taiwaneseCounting
34 ideographLegalTraditional
35 taiwaneseCountingThousand
36 taiwaneseDigital
37 chineseCounting
38 chineseLegalSimplified
*/

