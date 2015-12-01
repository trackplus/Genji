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


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import org.docx4j.dml.CTNonVisualDrawingProps;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
import org.docx4j.utils.TraversalUtilVisitor;
import org.docx4j.wml.Body;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Document;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;

import com.aurel.track.resources.LocalizeUtil;


/**
 * Adds caption to the images from the imageCaptionsMap
 * @author Tamas
 *
 */
public class PostprocessImage extends PostprocessBase {
		
	private boolean foundDrawingInParagraph = false;
	private String captionKey = null;
	private Map<P, P> imageParagraphToCaptionParagraphMap = new HashMap<P, P>();
	private Map<P, ContentAccessor> imageParagraphToParentMap = new HashMap<P, ContentAccessor>();
	
	/**
	 * Adds caption to the image
	 * @param wordMLPackage
	 * @param locale
	 * @param captionStyleId
	 * @throws Exception
	 */
	public void addCaption(WordprocessingMLPackage wordMLPackage, Locale locale, String captionStyleId, SortedMap<String, ImageOrTableCaption> imageCaptionsMap) throws Exception {
		this.figurePrefix = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.exportDocx.figure", locale);
		this.captionStyleId = captionStyleId;
		this.captionsMap = imageCaptionsMap;
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
		Document wmlDocumentEl = documentPart.getContents();
		Body body =  wmlDocumentEl.getBody();
		SingleTraversalUtilVisitorCallback imageVisitor = new SingleTraversalUtilVisitorCallback(new TraversalUtilParagraphWithImageVisitor());
		imageVisitor.walkJAXBElements(body);
		if (!imageParagraphToCaptionParagraphMap.isEmpty()) {
			for(Map.Entry<P, P> entry  : imageParagraphToCaptionParagraphMap.entrySet()) {
				P imageParagraph = entry.getKey();
				P captionParagraph =entry.getValue();
				ContentAccessor contentAccessor = imageParagraphToParentMap.get(imageParagraph);
				List<Object> contentList = contentAccessor.getContent();
				for (int i = 0; i < contentList.size(); i++) {
					Object object = contentList.get(i);
					if (imageParagraph.equals(object)) {
						//add caption right after image paragraph
						contentList.add(i+1, captionParagraph);
						break;
					}
				}
			}
		}
	}

	/**
	 * Find those paragraphs which have a drawing inside
	 * @author Tamas
	 *
	 */
	private class TraversalUtilParagraphWithImageVisitor extends TraversalUtilVisitor<P> {
		@Override
		public void apply(P imageParagraph, Object parent, List<Object> siblings) {
			SingleTraversalUtilVisitorCallback imageVisitor = new SingleTraversalUtilVisitorCallback(new TraversalUtilDrawingVisitor());
			imageVisitor.walkJAXBElements(imageParagraph);
			if (foundDrawingInParagraph) {
				ImageOrTableCaption imageCaption = captionsMap.get(captionKey);
				if (imageCaption!=null) {
					//add only for imageCaptionsMap contents i.e. programmatically added images (not for eventually existing images in word template)
					P captionParagraph = createCaption(figurePrefix, imageCaption, captionStyleId);
					ContentAccessor contentAccessor = (ContentAccessor)parent;
					List<Object> contentList = contentAccessor.getContent();
					for (int i = 0; i < contentList.size(); i++) {
						Object object = contentList.get(i);
						if (imageParagraph.equals(object)) {
							//align to center
							imageParagraph.setPPr(setAlignment(imageCaption.getAlign()));
							imageParagraphToCaptionParagraphMap.put(imageParagraph, captionParagraph);
							imageParagraphToParentMap.put(imageParagraph, contentAccessor);
							break;
						}
					}
				}
			}
			foundDrawingInParagraph = false;
			captionKey = null;
		}
	}
	
	/**
	 * Set the found drawing flag to true if drawing found in paragraph
	 * @author Tamas
	 *
	 */
	private class TraversalUtilDrawingVisitor extends TraversalUtilVisitor<Drawing> {
		@Override
		public void apply(Drawing element, Object parent, List<Object> siblings) {
			foundDrawingInParagraph = true;
			List<Object> anchorOrInline = element.getAnchorOrInline();
			if (anchorOrInline!=null && !anchorOrInline.isEmpty()) {
				Inline inline = (Inline)anchorOrInline.get(0);
				CTNonVisualDrawingProps nonvisualdrawingprops = inline.getDocPr();
				if (nonvisualdrawingprops!=null) {
					//The XHTMLImporterImpl transforms the "alt" attribute from html <img> tag to nonvisualdrawingprops.getDescr().
					//This will be used as key to lookup the caption from imageCaptionsMap
					captionKey = nonvisualdrawingprops.getDescr();
				}
			}
		}
	}
}
