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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.Docx4J;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.wml.Body;
import org.docx4j.wml.FldChar;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STFldCharType;
import org.docx4j.wml.Text;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import com.aurel.track.GeneralSettings;
import com.aurel.track.GeneralSettings.GENERAL_CONFIG;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.docx.exporter.StyleUtil.STANDARD_STYLE_NAMES;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.GeneralUtils;

/**
 * This sample converts a fragment of XHTML to docx.  The fragment should
 * be one or more block level objects.
 *
 * For best results, be sure to include src/main/resources on your classpath.
 *
 */
public class AssembleWordprocessingMLPackage {

	private static final Logger LOGGER = LogManager.getLogger(AssembleWordprocessingMLPackage.class);
	static String ITEM_BOOKMARK_PREFIX = "itemBookmark";
	//define the html entities, to avoid SAX parsing error
	/*private static String entityDeclaration = "<?xml version=\"1.0\"?>" +
		     "<!DOCTYPE doc_type [" +
		         "<!ENTITY nbsp \"&#160;\">" +
		         "<!ENTITY amp \"&#38;\">" +
		         "]>";
	//to assure that the html part is a valid xml: it should have a single root
	private static String startTag = "<div>";
	private static String endTag = "</div>";*/

	/**
	 * Gets the WordprocessingMLPackage from report beans
	 * @param workItemBean
	 * @param reportBeans
	 * @param docxTemplate
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static WordprocessingMLPackage getWordMLPackage(TWorkItemBean workItemBean, ReportBeans reportBeans, String docxTemplateFilePath, Integer personID, Locale locale) {
		Set<Integer> explicitItemIDs = new HashSet<Integer>();
		WordprocessingMLPackage wordMLPackage = null;
		File templateFile = new java.io.File(docxTemplateFilePath);
		if (templateFile.exists()) {
			try {
				wordMLPackage = WordprocessingMLPackage.load(templateFile);
			} catch (Docx4JException e) {
				LOGGER.error("Creating the WordprocessingMLPackage failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		} else {
			LOGGER.warn("The template file " + docxTemplateFilePath + " does not exist");
			try {
				wordMLPackage = WordprocessingMLPackage.createPackage();
			} catch (InvalidFormatException e) {
				LOGGER.error("Creating the WordprocessingMLPackage failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return null;
			}
		}
		if (wordMLPackage==null) {
			LOGGER.warn("No template found");
			return null;
		}

		Map<String, String> paragaphStylesMap = new HashMap<String, String>();
		Map<String, String> characterStylesMap = new HashMap<String, String>();
		Map<Integer, String> outlineStylesMap = new HashMap<Integer, String>();
		StyleUtil.getStyles(wordMLPackage.getMainDocumentPart(), paragaphStylesMap, characterStylesMap, outlineStylesMap);

		addCustomXMLPart(wordMLPackage, workItemBean, personID, locale);

		boolean highlightInlineContent = GeneralSettings.isHighlightInlineContent();
		String inlineContentStyleID = null;
		boolean inlineContentStyleIsParagraphStyle = false;
		if (highlightInlineContent) {
			String configuredInlineContentStyle = GeneralSettings.getExportDocxInlineContentStyle();
			String defaultInlineContentStyleName = STANDARD_STYLE_NAMES.SUBTLE_EMPHASIS;
			String inlineContentStyleKey = GENERAL_CONFIG.EXPORT_DOCX_STYLE_INLINE_CONTENT;
			//search among the paragraph styles
			inlineContentStyleID = StyleUtil.getStyle(paragaphStylesMap, configuredInlineContentStyle, inlineContentStyleKey, defaultInlineContentStyleName);
			if (inlineContentStyleID==null) {
				//search among the character styles
				inlineContentStyleID = StyleUtil.getStyle(characterStylesMap, configuredInlineContentStyle, inlineContentStyleKey, defaultInlineContentStyleName);
			} else {
				inlineContentStyleIsParagraphStyle = true;
			}
		}
		SortedMap<String, ImageOrTableCaption> imageCaptionMap = new TreeMap<String, ImageOrTableCaption>();
		SortedMap<String, ImageOrTableCaption> tableCaptionMap = new TreeMap<String, ImageOrTableCaption>();
		PreprocessImage preprocessImage = new PreprocessImage();
		PreprocessTable preprocessTable = new PreprocessTable();
		assembleWordMLPackage(wordMLPackage, reportBeans.getReportBeansFirstLevel(), explicitItemIDs, personID, locale, outlineStylesMap, 0, 0, imageCaptionMap, tableCaptionMap,
				highlightInlineContent, inlineContentStyleID, inlineContentStyleIsParagraphStyle, preprocessImage, preprocessTable);

		String temporaryDirPath = DocxTemplateBL.getWordTemplatesDir() + personID;
		File temporaryDir = new File(temporaryDirPath);
		if (temporaryDir.exists()) {
			AttachBL.deleteDirectory(temporaryDir);
		}
		String configuredImageCaptionStyle = GeneralSettings.getExportDocxImageCaptionStyle();
		String defaultImageCaptionStyleName = STANDARD_STYLE_NAMES.CAPTION_NAME;
		String imageStyleKey = GENERAL_CONFIG.EXPORT_DOCX_STYLE_IMAGE_CAPTION;
		String imageCaptionStyle = StyleUtil.getStyle(paragaphStylesMap, configuredImageCaptionStyle, imageStyleKey, defaultImageCaptionStyleName);
		try {
			new PostprocessImage().addCaption(wordMLPackage, locale, imageCaptionStyle, imageCaptionMap);
		} catch (Exception e) {
			LOGGER.warn("Adding the image captions failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}

		try {
			new PostprocessTable().addCaption(wordMLPackage, locale, imageStyleKey, tableCaptionMap);
		} catch (Exception e) {
			LOGGER.warn("Adding the table captions failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return wordMLPackage;
	}

	/**
	 * Add the actual values of custom xml
	 * @param wordprocessingMLPackage
	 * @param workItemBean
	 * @param personID
	 * @param locale
	 */
	private static void addCustomXMLPart(WordprocessingMLPackage wordprocessingMLPackage, TWorkItemBean workItemBean, Integer personID, Locale locale) {
		try {
			Document customXMLDocument = CustomXML.convertToDOM(workItemBean, personID, locale);
			if (LOGGER.isDebugEnabled()) {
				File file = new File(DocxTemplateBL.getWordTemplatesDir()+"Custom.xml");
				if (file.exists()) {
					file.delete();
				}
				FileOutputStream fileOutputStream = null;
				try {
					fileOutputStream = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
				CustomXML.convertToXml(fileOutputStream, customXMLDocument);
			}
			Docx4J.bind(wordprocessingMLPackage, customXMLDocument, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML);
		} catch (Docx4JException e) {
			LOGGER.info("Binding failed with Docx4JException " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.info("Binding failed with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}



	/**
	 * Gets the WordprocessingMLPackage from report beans
	 * @param reportBeans
	 * @return
	 */
	private static void assembleWordMLPackage(WordprocessingMLPackage wordMLPackage,
			List<ReportBean> reportBeansList,
			Set<Integer> explicitItemIDs,
			Integer personID,
			Locale locale,
			Map<Integer, String> outlineStylesMap,
			int headingLevel,
			int pheading1No,
			SortedMap<String,
			ImageOrTableCaption> imageCaptionMap,
			SortedMap<String, ImageOrTableCaption> tableCaptionMap,
			boolean highlightInlineContent,
			String inlineContentStyleID,
			boolean inlineContentStyleIsParagraphStyle,
			PreprocessImage preprocessImage,
			PreprocessTable preprocessTable) {

		MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
		String styleId = outlineStylesMap.get(Integer.valueOf(headingLevel));
		int heading1No = pheading1No;
		if (styleId==null) {
			//fall back to last heading style
			styleId = outlineStylesMap.get(Integer.valueOf(9));
		}

		for (ReportBean reportBean : reportBeansList) {
			TWorkItemBean workItemBean = reportBean.getWorkItemBean();
			Integer workItemID = workItemBean.getObjectID();
			String title = workItemBean.getSynopsis();
			if (title!=null) {
				mainDocumentPart.addObject(addHeaderParagraphOfText(mainDocumentPart, workItemID, title, styleId));
			}
			if (headingLevel==0) {
				heading1No++;
				preprocessImage.setChapterNo(heading1No);
				preprocessTable.setChapterNo(heading1No);
				//reset the image counter after a new Heading 1
				preprocessImage.setCounterWithinChapter(0);
				//reset the table counter after a new Heading 1
				preprocessTable.setCounterWithinChapter(0);
			}
			List<Integer> itemIDs = new LinkedList<Integer>();
			String description = workItemBean.getDescription();
			if (description!=null && !"".equals(description)) {
				//remove HTML headers
				description = removeHtmlHeadings(description);

				if (highlightInlineContent) {

					List<Section> sections = new LinkedList<AssembleWordprocessingMLPackage.Section>();
					exportDescription(description, itemIDs, sections, false);
					for (Section section : sections) {
						String content = section.getContent();
						boolean inline = section.isInline();
						List<Object> contents = transformHTMLContent(wordMLPackage, content, title, workItemID, personID, imageCaptionMap, tableCaptionMap, preprocessImage, preprocessTable);
						if (contents!=null) {
							mainDocumentPart.getContent().addAll(contents);
							if (inline) {
								new InlineStyleUtil().addInlineStyle(contents, locale, inlineContentStyleID, inlineContentStyleIsParagraphStyle);
							}
						}
					}
				} else {
					description = exportDescription(description, itemIDs);
					List<Object> contents = transformHTMLContent(wordMLPackage, description, title, workItemID, personID, imageCaptionMap, tableCaptionMap, preprocessImage, preprocessTable);
					if (contents!=null) {
						mainDocumentPart.getContent().addAll(contents);
					}
				}
			}
			//add image attachments

			//gather the report bean links
			List<ReportBean> reportBeansWithLinks = new LinkedList<ReportBean>();
			SortedSet<ReportBeanLink> reportBeanLinks = reportBean.getReportBeanLinksSet();
			if (reportBeanLinks!=null && !reportBeanLinks.isEmpty()) {
				reportBeansWithLinks.add(reportBean);
			}
			//gather the links of the inline items
			int[] workItemIDs = GeneralUtils.createIntArrFromIntegerList(itemIDs);
			if (workItemIDs!=null && workItemIDs.length>0) {
				List<ReportBean> internalReportBeans = LoadItemIDListItems.getReportBeansByWorkItemIDs(workItemIDs, false, personID, locale, false, false, false, false, false, false, false, true, false);
				if (internalReportBeans!=null) {
					for (ReportBean internalReportBean : internalReportBeans) {
						SortedSet<ReportBeanLink> internalReportBeanLinks = internalReportBean.getReportBeanLinksSet();
						if (internalReportBeanLinks!=null && !internalReportBeanLinks.isEmpty()) {
							reportBeansWithLinks.add(internalReportBean);
							for (ReportBeanLink reportBeanLink : internalReportBeanLinks) {
								boolean included = reportBeanLink.isLinkedItemIncluded();
								if (!included) {
									Integer linkedItem = reportBeanLink.getWorkItemID();
									if (linkedItem!=null && explicitItemIDs.contains(linkedItem)) {
										reportBeanLink.setLinkedItemIncluded(true);
									}
								}
							}
						}
					}
				}
			}
			if (!reportBeansWithLinks.isEmpty()) {
				TableWithBorders.addLinkedItemsTable(reportBeansWithLinks, locale, mainDocumentPart);
			}

			List<ReportBean> reportBeanChildrenList = reportBean.getChildren();
			if (reportBeanChildrenList!=null) {
				assembleWordMLPackage(wordMLPackage, reportBeanChildrenList, explicitItemIDs, personID, locale, outlineStylesMap, headingLevel+1, heading1No,
						imageCaptionMap, tableCaptionMap,
						highlightInlineContent, inlineContentStyleID, inlineContentStyleIsParagraphStyle, preprocessImage, preprocessTable);
			}
		}
	}

	/**
	 * Transform the html content to word content
	 * @param wordMLPackage
	 * @param htmlContent
	 * @param title
	 * @param workItemID
	 * @param personID
	 * @param imageCaptionMap
	 * @return
	 */
	private static List<Object> transformHTMLContent(WordprocessingMLPackage wordMLPackage, String htmlContent,
			String title, Integer workItemID, Integer personID, Map<String, ImageOrTableCaption> imageCaptionMap, Map<String, ImageOrTableCaption> tableCaptionMap,
			PreprocessImage preprocessImage, PreprocessTable preprocessTable) {
		//HTML processor libraries are used:
		//1. jsoup: cleans up the HTML fragment, knows HTML5 (figure and figurecaption is pre-processed) but the resulted HTML is not usable by XHTMLImporterImpl
		//2. tidy: throws away HTML5 tags (anyway removed previously in jsoup pre-processing) and produces usable input for XHTMLImporterImpl
		List<Object> contents = null;
		if (htmlContent!=null && htmlContent.length()>0) {
			String jsoupContent = preprocessImage.preprocessImages(htmlContent, personID, imageCaptionMap);
			jsoupContent = preprocessTable.prepocessTableCaption(jsoupContent, tableCaptionMap);
			//jsoup does not prepare the HTML usable by XHTMLImporterImpl so we use tidy for that (tidy throws away HTML5 tags so figure and figurecaption would be removed)
			Tidy tidy = new Tidy(); //obtain a new Tidy instance
			tidy.setXHTML(true); //set desired config options using tidy setters
			InputStream inputStream = null;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				inputStream = new ByteArrayInputStream(jsoupContent.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				LOGGER.warn("UnsupportedEncodingException " + e1.getMessage());
			}
			try {
				tidy.parse(inputStream, outputStream); // run tidy, providing an input and output stream
			} catch(Exception e) {
				LOGGER.error("Tidy parsing the xhtml content for item " + workItemID + " with title " + title + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			try {
				jsoupContent = outputStream.toString("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LOGGER.debug(e1);
			}
			if (jsoupContent!=null && jsoupContent.length()>0) {
				try {
					XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
					contents = xhtmlImporter.convert(jsoupContent, null);
				} catch (Docx4JException e) {
					LOGGER.error("Converting the xhtml content for item " + workItemID + " with title " + title + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return contents;
	}

	/**
	 * The XHTMLImporterImpl would create Word headings from HTML headings which is not desired.
	 * The headings in Word are programatically added based on the item hierarchies
	 * @param phtmlContent
	 */
	private static String removeHtmlHeadings(String phtmlContent) {
		String htmlContent = phtmlContent;
		if (GeneralSettings.isRemoveHTMLHeaders()) {
			htmlContent = htmlContent.replaceAll("\\<h[1-6]\\>", "");
			htmlContent = htmlContent.replaceAll("\\</h[1-6]\\>", "");
		}
		return htmlContent;
	}

	/**
	 * Replace the [issue <no>/] with the description of the item
	 * @param source
	 * @param itemIDs
	 * @return
	 */
	private static void exportDescription(String source, List<Integer> itemIDs, List<Section> sections, boolean isInline) {
		if (source==null) {
			return;
		}
		replaceIssueLinksWithDescription(source, itemIDs, sections, isInline);
	}

	private static String	EMPTY		= "";
	private static String	ISSUE_TAG	= "[issue";
	private static String	CLOSE		= "/]";

	private static void replaceIssueLinksWithDescription(String psrc, List<Integer> itemIDs, List<Section> sections, boolean isInline) {
		StringBuilder src = new StringBuilder(psrc);
		int startIndex = src.indexOf(ISSUE_TAG);

		String paragraph = "<p>";
		if (startIndex==-1) {
			//add the original description (no inline content)
			sections.add(new Section(isInline, src.toString()));
		} else {
			while ((startIndex = src.toString().indexOf(ISSUE_TAG/*, startIndex*/))!=-1) {
				int endIndex = src.toString().indexOf(CLOSE, startIndex);
				if (endIndex==-1 || (endIndex<=startIndex)) {
					//no closing tag found: remove the opening tag and go on
					src = src.replace(startIndex, startIndex + ISSUE_TAG.length(), EMPTY);
					continue;
				}
				String key = src.substring(startIndex + ISSUE_TAG.length(), endIndex).trim();
				try {
					Integer itemID=Integer.decode(key);
					LOGGER.debug("ItemID " + itemID + " found");
					itemIDs.add(itemID);
					TWorkItemBean itemBean = null;
					try {
						itemBean = ItemBL.loadWorkItem(itemID);
					} catch (ItemLoaderException e) {
						LOGGER.warn("Loading the workItemID " + itemID + " failed with " + e.getMessage());
					}
					if (itemBean==null) {
						//item not found, neglect the link to this item
						src =src.replace(startIndex, endIndex + CLOSE.length(), EMPTY);
					} else {
						//item found
						sections.add(new Section(isInline, src.substring(0, startIndex)));
						String description = itemBean.getDescription();
						boolean noDescription = false;
						if (description==null || description.length()==0) {
							noDescription = true;
							description = itemBean.getSynopsis();
						} else {
							description = removeHtmlHeadings(description);
						}
						//add itemNo before the inline description
						String itemNo = AssembleWordprocessingMLPackage.getItemNo(itemBean);
						if (description.startsWith(paragraph)) {
							description = paragraph + itemNo + description.substring(paragraph.length());
						} else {
							description = itemNo + description;
						}
						if (noDescription) {
							sections.add(new Section(true, description));
						} else {
							exportDescription(description, itemIDs, sections, true);
						}

						src = new StringBuilder(src.substring(endIndex + CLOSE.length()));
					}
				} catch (NumberFormatException e ) {
					LOGGER.info("The key " + key + " is not a number. Remove the start and end tag but do not remove the content between the two");
					src = src.replace(startIndex, startIndex + ISSUE_TAG.length(), EMPTY);
					//recalculate end index
					endIndex = src.toString().indexOf(CLOSE, startIndex);
					src = src.replace(endIndex, endIndex + CLOSE.length(), EMPTY);
				}
			}
			sections.add(new Section(isInline, src.toString()));
		}
	}

	/**
	 * Replace the [issue <no>/] with the description of the item
	 * @param source
	 * @param itemIDs
	 * @return
	 */
	private static String exportDescription(String source, List<Integer> itemIDs) {
		if (source==null) {
			return null;
		}
		return replaceIssueLinksWithDescription(new StringBuilder(source), itemIDs).toString();
	}

	private static StringBuilder replaceIssueLinksWithDescription(StringBuilder src, List<Integer> itemIDs) {
		StringBuilder lsrc = new StringBuilder(src);
		int startIndex = 0;
		String paragraph = "<p>";
		while ((startIndex = lsrc.toString().indexOf(ISSUE_TAG, startIndex))!=-1) {
			int endIndex = lsrc.toString().indexOf(CLOSE, startIndex );
			if (endIndex==-1 || (endIndex<=startIndex)) {
				lsrc = lsrc.replace(startIndex, startIndex + ISSUE_TAG.length(), EMPTY);
			} else {
				String key = lsrc.substring(startIndex + ISSUE_TAG.length(), endIndex).trim();
				try {
					Integer itemID=Integer.decode(key);
					LOGGER.debug("ItemID " + itemID + " found");
					itemIDs.add(itemID);
					TWorkItemBean itemBean = null;
					try {
						itemBean = ItemBL.loadWorkItem(itemID);
					} catch (ItemLoaderException e) {
						LOGGER.warn("Loading the workItemID " + itemID + " failed with " + e.getMessage());
					}
					if (itemBean==null){
						lsrc = lsrc.replace(startIndex, endIndex + CLOSE.length(), EMPTY);
					} else {
						String description = itemBean.getDescription();
						if (description==null || description.length()==0) {
							description = itemBean.getSynopsis();
						} else {
							description = exportDescription(description, itemIDs);
						}
						if (description==null || description.length()==0) {
							break;
						}
						//add itemNo before the inline description
						if (description.startsWith(paragraph)) {
							description = paragraph + AssembleWordprocessingMLPackage.getItemNo(itemBean) + description.substring(paragraph.length());
						} else {
							description = AssembleWordprocessingMLPackage.getItemNo(itemBean) + description;
						}
						lsrc = lsrc.replace(startIndex, endIndex + CLOSE.length(), description);
						startIndex = startIndex + description.length();

					}
				} catch (NumberFormatException e ) {
					lsrc=lsrc.replace(startIndex, startIndex + ISSUE_TAG.length(), EMPTY );
					//recalculate end index
					endIndex = lsrc.toString().indexOf(CLOSE, startIndex );
					lsrc=lsrc.replace(endIndex, endIndex + CLOSE.length(), EMPTY);
				}
			}
		}
		return lsrc;
	}


	static class Section {
		private boolean isInline;
		private String content;

		public Section(boolean isInline, String content) {
			super();
			this.isInline = isInline;
			this.content = content;
		}

		public boolean isInline() {
			return isInline;
		}

		public void setInline(boolean isInline) {
			this.isInline = isInline;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

	}

	/**
	 * Gets the item number in square brackets
	 * @param workItemBean
	 * @return
	 */
	public static String getItemNo(TWorkItemBean workItemBean) {
		return getItemNo(ItemBL.getItemNo(workItemBean));
	}

	/**
	 * Gets the item number in square brackets
	 * @param workItemBean
	 * @return
	 */
	public static String getItemNo(String itemNo) {
		return "[" + itemNo + "] ";
	}

	/**
	 *
	 * @param mainDocumentPart
	 * @param workItemID
	 * @param simpleText
	 * @param styleId
	 * @return
	 */
	private static P addHeaderParagraphOfText(MainDocumentPart mainDocumentPart, Integer workItemID, String simpleText, String styleId) {
		ObjectFactory factory = Context.getWmlObjectFactory();
		P p = factory.createP();
		if (simpleText!=null) {
			Text t = factory.createText();
			t.setValue(simpleText);
			R run = factory.createR();
			run.getContent().add(t);
			p.getContent().add(run);
			BookmarkAdd.bookmarkRun(p, run, ITEM_BOOKMARK_PREFIX+workItemID, workItemID);
		}
		if (mainDocumentPart.getPropertyResolver().activateStyle(styleId)) {
			// Style is available
			org.docx4j.wml.PPr  pPr = factory.createPPr();
			p.setPPr(pPr);
			org.docx4j.wml.PPrBase.PStyle pStyle = factory.createPPrBasePStyle();
			pPr.setPStyle(pStyle);
			pStyle.setVal(styleId);
		} else {
			LOGGER.debug("StyleID " + styleId + " not available");
		}
		return p;
	}

	private static void addTOC(MainDocumentPart documentPart) throws Exception {

		org.docx4j.wml.Document wmlDocumentEl = (org.docx4j.wml.Document)documentPart.getJaxbElement();
		Body body =  wmlDocumentEl.getBody();

        ObjectFactory factory = Context.getWmlObjectFactory();

        /* Create the following:
         *
		    <w:p>
		      <w:r>
		        <w:fldChar w:dirty="true" w:fldCharType="begin"/>
		        <w:instrText xml:space="preserve">TOC \o &quot;1-3&quot; \h \z \ u \h</w:instrText>
		      </w:r>
		      <w:r/>
		      <w:r>
		        <w:fldChar w:fldCharType="end"/>
		      </w:r>
		    </w:p>         */
        P paragraphForTOC = factory.createP();
        R r = factory.createR();

        FldChar fldchar = factory.createFldChar();
        fldchar.setFldCharType(STFldCharType.BEGIN);
        fldchar.setDirty(true);
        r.getContent().add(getWrappedFldChar(fldchar));
        paragraphForTOC.getContent().add(r);

        R r1 = factory.createR();
        Text txt = new Text();
        txt.setSpace("preserve");
        txt.setValue("TOC \\o \"1-3\" \\h \\z \\u \\h");
        r.getContent().add(factory.createRInstrText(txt) );
        paragraphForTOC.getContent().add(r1);

        FldChar fldcharend = factory.createFldChar();
        fldcharend.setFldCharType(STFldCharType.END);
        R r2 = factory.createR();
        r2.getContent().add(getWrappedFldChar(fldcharend));
        paragraphForTOC.getContent().add(r2);

		body.getContent().add(paragraphForTOC);



	}

	public static JAXBElement getWrappedFldChar(FldChar fldchar) {

		return new JAXBElement( new QName(Namespaces.NS_WORD12, "fldChar"),
				FldChar.class, fldchar);

	}
}
