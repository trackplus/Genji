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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.exchange.docx.exporter.ImageOrTableCaption.ALIGN;

/**
 * Preprocess the html images before feeding into XHTMLImporterImpl
 * @author Tamas
 *
 */
public class PreprocessImage extends PreprocessBase {
	private static final Logger LOGGER = LogManager.getLogger(PreprocessImage.class);

	/**
	 * Cleans the figure tag (HTML5 wich is not accespted by Tidy) and, gets the image captions map
	 * for later use in word and creates the temporary image files to be embedded by XHTMLImporterImpl
	 * @param htmlContent
	 * @param personID
	 * @param imageCaptionsMap
	 * @return
	 */
	String preprocessImages(String htmlContent, Integer personID, Map<String, ImageOrTableCaption> imageCaptionsMap) {
		Document doc = removeFigureSaveFigcaption(htmlContent);
		return getImageCaptions(doc, personID, imageCaptionsMap);
	}
	
	/**
	 * Removes the HTML5 figure tag and saves the figcaption in the <img> tag's "alt" attribute for later use
	 * @param htmlContent
	 * @return
	 */
	private Document removeFigureSaveFigcaption(String htmlContent) {
		Document doc = Jsoup.parseBodyFragment(htmlContent);
		//figure is a HTML5 tag not accepted by Tidy, so it should be replaced by the content <img>-tag, and the figcaption is saved in the "alt" attribute
		Elements figureElements = doc.select("figure");
		Element figcaptionNode = null;
		if (figureElements!=null) {
			for (Iterator<Element> iterator = figureElements.iterator(); iterator.hasNext();) {
				Element figureElement = iterator.next();
				Elements figureChildren = figureElement.getAllElements();
				Node imageNode = null;
				if (figureChildren!=null) {
					for (Element figureChild : figureChildren) {
						if ("img".equals(figureChild.nodeName())) {
							imageNode = figureChild;
						} else {
							if ("figcaption".equals(figureChild.nodeName())) {
								figcaptionNode = figureChild;
								//set "figcaption" text as value for "alt" attribute  
								if (imageNode!=null) {
									imageNode.attr("alt", figcaptionNode.text());
								}
							}
						}
					}
				}
				if (imageNode!=null) {
					figureElement.replaceWith(imageNode);
				}
			}
		}
		return doc;
	}
	
	/**
	 * Gets the image captions in a map keyed by itemID_attachmentID
	 * The key is saved also in the <img> tag's "alt" attribute for later use from word
	 * @param doc
	 * @param personID
	 * @param imageCaptionsMap
	 * @return
	 */
	private String getImageCaptions(Document doc, Integer personID, Map<String, ImageOrTableCaption> imageCaptionsMap) {
		Elements imgElements = doc.select("img");
		if (imgElements!=null) {
			for (Iterator<Element> iterator = imgElements.iterator(); iterator.hasNext();) {
				Element imageElement = iterator.next();
				String sourceAttribute = imageElement.attr("src");
				String style = imageElement.attr("style");
				//remove the width and height attributes from html img to avoid java.lang.OutOfMemoryError: Java heap space
				imageElement.removeAttr("width");
				imageElement.removeAttr("height");
				ALIGN align = null;
				if (style!=null) {
					if (style.contains("float:left")) {
						align = ALIGN.LEFT;
					} else {
						if (style.contains("float:right")) {
							align = ALIGN.RIGHT;
						} 
					}
				}
				String altAttribute = imageElement.attr("alt");
				Map<String, String> map = getTemporaryFilePathMap(sourceAttribute, personID);
				if (map!=null) {
					imageElement.attr("src", map.get("temporaryFilePath"));
					//save imageCaption into the map and now use the "alt" attribute for storing the merged key
					//which will be transformed  in nonvisualdrawingprops.getDescr() by XHTMLImporterImpl to set the caption on the ms word side
					String imageCaption = null;
					if (altAttribute!=null && !"".equals(altAttribute)) {
						//probably from previously removed figcaption but it may also be explicitly set
						imageCaption = altAttribute;
					} else {
						imageCaption = map.get("description");
					}
					globalCounter++;
					counterWithinChapter++;
					imageElement.attr("alt", String.valueOf(globalCounter));
					if (imageCaption==null) {
						//add anyway to the map even as empty string because this marks the image to be added to the List of figures 
						imageCaption = "";
					}
					imageCaptionsMap.put(String.valueOf(globalCounter), new ImageOrTableCaption(chapterNo, counterWithinChapter, imageCaption, align));
				}
			}
		}
		return doc.body().html();
	}
	
	private static Map<String, String> getTemporaryFilePathMap(String source, Integer personID) {
		Map<String, String> map = null;
		String WORKITEM_TAG = "workItemID=";
		String ATTACHMENT_TAG = "attachKey=";
		String FILE_PROTOCOL = "file:///";
		Integer itemID = parseNumber(source, WORKITEM_TAG);
		Integer attachmentID = parseNumber(source, ATTACHMENT_TAG);
		TAttachmentBean attachmentBean = AttachBL.loadAttachment(attachmentID, itemID, false);
		if (attachmentBean!=null && AttachBL.isImage(attachmentBean)) {
			if (itemID!=null && attachmentID!=null) {
				String attachmentPath = AttachBL.getFullFileName(null, attachmentID, itemID);
				File attachmentFile = new File(attachmentPath);
				if (attachmentFile.exists()) {
					map = new HashMap<String, String>();
					LOGGER.debug("Attachment for item + " + itemID + " attachmentID " + attachmentID + " on disk " + attachmentPath);
					String temporaryDirPath = DocxTemplateBL.getWordTemplatesDir() + personID + File.separator + itemID;
					File temporaryDir = new File(temporaryDirPath);
					if (!temporaryDir.exists()) {
						temporaryDir.mkdirs();
					}
					String temporaryFilePath = temporaryDir + File.separator + attachmentBean.getFileName();
					File temporaryFile = new File(temporaryFilePath);
					try {
						FileUtils.copyFile(attachmentFile, temporaryFile);
						//TODO a more exact identifier should be invented
						//imageDescriptions.put(imageDescriptions.size(), attachmentBean.getDescription());
					} catch (IOException e) {
						LOGGER.info("Copying the temporary file failed with " + e.getMessage(), e);
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
					temporaryFilePath = temporaryFilePath.replaceAll("\\\\", "/");
					temporaryFilePath = FILE_PROTOCOL + temporaryFilePath;
					map.put("itemID", itemID.toString());
					map.put("attachmentID", attachmentID.toString());
					map.put("description", attachmentBean.getDescription());
					map.put("temporaryFilePath", temporaryFilePath);
				} else {
					LOGGER.info("Attachment for item + " + itemID + " attachmentID " + attachmentID + " on disk " + attachmentPath + " not found");
				}
			}
		}
		return map;
	}
	
	private static Integer parseNumber(String imgSrc, String tag) {
		int workItemIndex = imgSrc.indexOf(tag);
		if (workItemIndex!=-1) {
			String workItemString = imgSrc.substring(workItemIndex);
			StringBuilder stringBuilder = new StringBuilder(workItemString);
			stringBuilder.replace(0, tag.length(), "");
			int i = 0;
			char charValue;
			StringBuilder numberString = new StringBuilder();
			do {		
				charValue = stringBuilder.charAt(i++);
				if (Character.isDigit(charValue)) {
					numberString.append(charValue);
				}
			} while (stringBuilder.length()>i && Character.isDigit(charValue));
			return Integer.decode(numberString.toString());
		}
		return null;
	}
}
