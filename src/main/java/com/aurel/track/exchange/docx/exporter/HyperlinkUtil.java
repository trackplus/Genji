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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.relationships.ObjectFactory;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.P.Hyperlink;


/**
 * Fun with hyperlinks to external resources
 * eg web pages.
 * 
 * For an example of an internal hyperlink,
 * see the BookmarkAdd sample.
 * 
 * @author Jason Harrop
 */
public class HyperlinkUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(HyperlinkUtil.class);
	
	/*
	 * <w:p>
	 * 	<w:r>
	 * 		<w:t xml:space="preserve">Here is an example of a </w:t>
	 *  </w:r>
	 *  <w:hyperlink r:id="rId4" w:history="1">
	 *  	<w:r>
	 *  		<w:rPr>
	 *  			<w:rStyle w:val="Hyperlink"/>
	 *  		</w:rPr>
	 *  		<w:t>hyperlink</w:t>
	 *  	</w:r>
	 *  </w:hyperlink>
	 *  <w:r>
	 *  	<w:t xml:space="preserve">.  </w:t>
	 *  </w:r>
	 * </w:p>
	 * 
	 * 
	 * word/_rels/document.xml.rels contains:
	 * 
	 * <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" 
	 * 		Target="http://dev.plutext.org/" TargetMode="External"/>

	 */

	public static void main(String[] args) throws Exception {
		
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
		MainDocumentPart mdp = wordMLPackage.getMainDocumentPart(); 
		
		// Create hyperlink
		Hyperlink link = createHyperlink(mdp, "http://slashdot.org", "Link text");
		
		// Add it to a paragraph
		org.docx4j.wml.P paragraph = Context.getWmlObjectFactory().createP();
		paragraph.getContent().add( link );
		mdp.addObject(paragraph);
		
		// Now save it 
		wordMLPackage.save(new java.io.File(System.getProperty("user.dir") + "/OUT_HyperlinkTest.docx") );
		
		// Uncomment to display the result as Flat OPC XML
//		FlatOpcXmlCreator worker = new FlatOpcXmlCreator(wordMLPackage);
//		worker.marshal(System.out);				
				
	}
	
	public static Hyperlink createHyperlink(MainDocumentPart mdp, String url, String linkText) {
		try {
			// We need to add a relationship to word/_rels/document.xml.rels
			// but since its external, we don't use the 
			// usual wordMLPackage.getMainDocumentPart().addTargetPart
			// mechanism
			ObjectFactory factory = new ObjectFactory();
			Relationship rel = factory.createRelationship();
			rel.setType(Namespaces.HYPERLINK);
			rel.setTarget(url);
			rel.setTargetMode("External");  						
			mdp.getRelationshipsPart().addRelationship(rel);
			
			// addRelationship sets the rel's @Id
			String hpl = "<w:hyperlink r:id=\"" + rel.getId() + "\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" " +
            "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" >" +
            "<w:r>" +
            "<w:rPr>" +
            "<w:rStyle w:val=\"Hyperlink\" />" +  // TODO: enable this style in the document!
            "</w:rPr>" +
            "<w:t>" + linkText + "</w:t>" +
            "</w:r>" +
            "</w:hyperlink>";

//			return (Hyperlink)XmlUtils.unmarshalString(hpl, Context.jc, P.Hyperlink.class);
			return (Hyperlink)XmlUtils.unmarshalString(hpl);
			
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
		
		
	}
		
}

