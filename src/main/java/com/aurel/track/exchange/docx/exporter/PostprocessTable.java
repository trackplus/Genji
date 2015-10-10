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


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import javax.xml.bind.JAXBElement;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
import org.docx4j.utils.TraversalUtilVisitor;
import org.docx4j.wml.Body;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Document;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblPr;

import com.aurel.track.exchange.docx.exporter.ImageOrTableCaption.ALIGN;
import com.aurel.track.resources.LocalizeUtil;


/**
 * Adds caption to the images from the imageCaptionsMap
 * @author Tamas
 *
 */
public class PostprocessTable extends PostprocessBase {
		
	private Map<Tbl, P> tableToCaptionParagraphMap = new HashMap<Tbl, P>();
	private Map<Tbl, ContentAccessor> tableToParentMap = new HashMap<Tbl, ContentAccessor>();
	
	/**
	 * Adds caption to the image
	 * @param wordMLPackage
	 * @param locale
	 * @param captionStyleId
	 * @throws Exception
	 */
	public void addCaption(WordprocessingMLPackage wordMLPackage, Locale locale, String captionStyleId, SortedMap<String, ImageOrTableCaption> tableCaptionsMap) throws Exception {
		this.figurePrefix = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.exportDocx.table", locale);
		this.captionStyleId = captionStyleId;
		this.captionsMap = tableCaptionsMap;
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
		Document wmlDocumentEl = documentPart.getContents();
		Body body =  wmlDocumentEl.getBody();
		SingleTraversalUtilVisitorCallback imageVisitor = new SingleTraversalUtilVisitorCallback(new TraversalUtilParagraphWithImageVisitor());
		imageVisitor.walkJAXBElements(body);
		if (!tableToCaptionParagraphMap.isEmpty()) {
			for(Map.Entry<Tbl, P> entry  : tableToCaptionParagraphMap.entrySet()) {
				Tbl table = entry.getKey();
				P captionParagraph =entry.getValue();
				ContentAccessor contentAccessor = tableToParentMap.get(table);
				List<Object> contentList = contentAccessor.getContent();
				for (int i = 0; i < contentList.size(); i++) {
					Object object = contentList.get(i);
					if (table.equals(object)) {
						//add caption right after image paragraph
						contentList.add(i+1, captionParagraph);
						break;
					}
				}
			}
		}
	}

	/**
	 * Find the tables
	 * @author Tamas
	 *
	 */
	private class TraversalUtilParagraphWithImageVisitor extends TraversalUtilVisitor<Tbl> {
		@Override
		public void apply(Tbl table, Object parent, List<Object> siblings) {
			if (captionsMap!=null) {
				ContentAccessor contentAccessor = (ContentAccessor)parent;
				List<Object> contentList = contentAccessor.getContent();
				String tableBookmarkName = null;
				for (int i = 0; i < contentList.size(); i++) {
					Object object = contentList.get(i);
					if (table.equals(object)) {
						if (i>0) {
							Object previousSibling = contentList.get(i-1);
							if (previousSibling.getClass().getName().equals("javax.xml.bind.JAXBElement")) {
								JAXBElement bookmarkWrapped = (JAXBElement)previousSibling;
								Object jaxbValue = bookmarkWrapped.getValue();
								//the html id is transformed into bookmark lo look up the map
								if (jaxbValue.getClass().getName().equals("org.docx4j.wml.CTBookmark")) {
									CTBookmark ctBookmark = (CTBookmark)jaxbValue;
									tableBookmarkName = ctBookmark.getName();
									if (tableBookmarkName!=null) {
										ImageOrTableCaption caption = captionsMap.get(tableBookmarkName);
										if (caption!=null) {
											P captionParagraph = createCaption(figurePrefix, caption, captionStyleId);
											tableToCaptionParagraphMap.put(table, captionParagraph);
											tableToParentMap.put(table, contentAccessor);
											setAlignment(table, caption.getAlign());
											break;
										}
										
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gets a center aligned PPr for P
	 * @return
	 */
	private void setAlignment(Tbl table, ALIGN align) {
	    if (align!=null) {
	    	JcEnumeration jcEnumeration = JcEnumeration.CENTER;
	    	switch (align) {
			case LEFT:
				jcEnumeration = JcEnumeration.LEFT;
				break;
			case RIGHT:
				jcEnumeration = JcEnumeration.RIGHT;
				break;
			}
	    	ObjectFactory wmlObjectFactory = new ObjectFactory();
	    	TblPr tblPr = table.getTblPr();
	 		if (tblPr==null) {
	 			tblPr = wmlObjectFactory.createTblPr(); 
	 			table.setTblPr(tblPr);
	 		}
	 		Jc jc = wmlObjectFactory.createJc();
	 		jc.setVal(jcEnumeration);
 			tblPr.setJc(jc); 
	    }
	}
}
