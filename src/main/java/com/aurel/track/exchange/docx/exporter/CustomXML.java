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

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.emailHandling.Html2Text;

public class CustomXML {

	static Logger LOGGER = LogManager.getLogger(CustomXML.class);
	
	private interface CUSTOM_XML_ELEMENTS {
		String MAIN_ELEMENT = "TrackplusDocument";
		String PRESENT_SUFFIX = "Present";
		String CONSULTED_LIST = "ConsultedPersons";
		String CONSULTED_PERSON = "ConsultedPerson";
		String INFORMED_LIST = "InformedPersons";
		String INFORMED_PERSON = "InformedPerson";
		String COMMENTS = "Comments";
		String COMMENT = "Comment";
		String COMMENT_TEXT = "CommentText";
		String COMMENTED_BY = "CommentedBy";
		String COMMENTED_AT = "CommentedAt";
		String NAME = "Name";
	}
	
	/**
	 * @param items
	 * @return
	 */
	public static void convertToXml(OutputStream outputStream, Document dom) {
		Transformer transformer = null;
		try{
			TransformerFactory factory = TransformerFactory.newInstance();
			transformer = factory.newTransformer ();
			transformer.setOutputProperty (OutputKeys.INDENT, "yes");
			transformer.setOutputProperty (OutputKeys.METHOD, "xml");
			transformer.setOutputProperty (OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "4");
		} catch (TransformerConfigurationException e){
			LOGGER.error ("Creating the transformer failed with TransformerConfigurationException: " + e.getMessage());
			return;
		}
		try{
			transformer.transform (new DOMSource(dom), new StreamResult(outputStream));
		} catch (TransformerException e){
			LOGGER.error ("Transform failed with TransformerException: " + e.getMessage());
		}
	}
	
	public static Document convertToDOM(TWorkItemBean documentItem, Integer personID, Locale locale) {
		Document dom = null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder ();
			dom = builder.newDocument ();	
		}catch (FactoryConfigurationError e){
			LOGGER.error ("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage());
			return null;
		}catch (ParserConfigurationException e){
			LOGGER.error ("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage());
			return null;
		}
		Element root = dom.createElement (CUSTOM_XML_ELEMENTS.MAIN_ELEMENT);
		if (documentItem!=null) {
			Integer itemID = documentItem.getObjectID();
			List<TWorkItemBean> itemList = new LinkedList<TWorkItemBean>();
			itemList.add(documentItem);
			List<ReportBean> reportBeansList = LoadItemIDListItems.getReportBeansByWorkItems(itemList, personID, locale, true, false, false, false, false, false, false, false, false);
			ReportBean showableWorkItem = reportBeansList.get(0);
			Map<Integer, String> showValuesMap = showableWorkItem.getShowValuesMap();
			if (showValuesMap!=null) {
				List<TFieldBean> fieldBeansList = FieldBL.loadAll();
				for (TFieldBean fieldBean : fieldBeansList) {
					Integer fieldID = fieldBean.getObjectID();
					String fieldName = fieldBean.getName();
					String showValue = showValuesMap.get(fieldID);
					if (showValue!=null && !"".equals(showValue)) {
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null) {
							if (fieldTypeRT.isLong()) {
								showValue = StringEscapeUtils.escapeHtml4(showValue);
							}
						}
						appendChild(root, fieldName, showValue, dom);
						appendChild(root, fieldName + CUSTOM_XML_ELEMENTS.PRESENT_SUFFIX, Boolean.TRUE.toString(), dom);
					} else {
						appendChild(root, fieldName + CUSTOM_XML_ELEMENTS.PRESENT_SUFFIX, Boolean.FALSE.toString(), dom);
						appendChild(root, fieldName, "", dom);
					}
				}
				
				List<TPersonBean> consultedPersonBeans = PersonBL.getDirectConsultants(itemID);
				addWatcherNodes(consultedPersonBeans, root, dom, CUSTOM_XML_ELEMENTS.CONSULTED_LIST, CUSTOM_XML_ELEMENTS.CONSULTED_PERSON);
				List<TPersonBean> informedPersonBeans = PersonBL.getDirectInformants(itemID);
				addWatcherNodes(informedPersonBeans, root, dom, CUSTOM_XML_ELEMENTS.INFORMED_LIST, CUSTOM_XML_ELEMENTS.INFORMED_PERSON);
				
				List<HistoryValues> comments = HistoryLoaderBL.getRestrictedWorkItemComments(personID, itemID, locale, false, /*LONG_TEXT_TYPE.ISPLAIN*/ LONG_TEXT_TYPE.ISFULLHTML);
				addCommentNodes(comments, root, dom, locale);
				
			}
		}
		dom.appendChild(root);
		return dom;
	}

	/**
	 * Add watcher nodes to xml
	 * @param watcherPersonBeans
	 * @param root
	 * @param dom
	 * @param watcherListName
	 * @param watcherElement
	 */
	private static void addWatcherNodes(List<TPersonBean> watcherPersonBeans, Element root, Document dom, String watcherListName, String watcherElement) {
		if (watcherPersonBeans==null || watcherPersonBeans.isEmpty()) {
			appendChild(root, watcherListName + CUSTOM_XML_ELEMENTS.PRESENT_SUFFIX, Boolean.FALSE.toString(), dom);
		} else {
			Element watcherList = dom.createElement(watcherListName);
			for (TPersonBean personBean : watcherPersonBeans) {
				Element nameElement = createDomElement(CUSTOM_XML_ELEMENTS.NAME, personBean.getName(), dom);
				Element person = createDomElement(watcherElement, "", dom);
				person.appendChild(nameElement);
				watcherList.appendChild(person);
			}
			root.appendChild(watcherList);
			appendChild(root, watcherListName + CUSTOM_XML_ELEMENTS.PRESENT_SUFFIX, Boolean.TRUE.toString(), dom);
		}
	}
	
	/**
	 * Add watcher nodes to xml
	 * @param watcherPersonBeans
	 * @param root
	 * @param dom
	 * @param commentsName
	 * @param watcherElement
	 */
	private static void addCommentNodes(List<HistoryValues> comments, Element root, Document dom, Locale locale) {
		if (comments==null || comments.isEmpty()) {
			appendChild(root, CUSTOM_XML_ELEMENTS.COMMENTS + CUSTOM_XML_ELEMENTS.PRESENT_SUFFIX, Boolean.FALSE.toString(), dom);
		} else {
			Element consultedList = dom.createElement(CUSTOM_XML_ELEMENTS.COMMENTS);
			for (HistoryValues historyValue : comments) {
				String commentText = historyValue.getNewShowValue();
				commentText = StringEscapeUtils.escapeHtml3(commentText);
				Element commentTextElement = createDomElement(CUSTOM_XML_ELEMENTS.COMMENT_TEXT, commentText, dom);
				Element commentedByElement = createDomElement(CUSTOM_XML_ELEMENTS.COMMENTED_BY, historyValue.getChangedByName(), dom);
				Element commentedAtElement = createDomElement(CUSTOM_XML_ELEMENTS.COMMENTED_AT, DateTimeUtils.getInstance().formatGUIDate(historyValue.getLastEdit(), locale) , dom);
				Element commentElement = createDomElement(CUSTOM_XML_ELEMENTS.COMMENT, "", dom);
				commentElement.appendChild(commentTextElement);
				commentElement.appendChild(commentedByElement);
				commentElement.appendChild(commentedAtElement);
				consultedList.appendChild(commentElement);
			}
			root.appendChild(consultedList);
			appendChild(root, CUSTOM_XML_ELEMENTS.COMMENTS + CUSTOM_XML_ELEMENTS.PRESENT_SUFFIX, Boolean.TRUE.toString(), dom);
		}
	}
	
	/**
	 * Creates a dom elemebnt and adds to the parent
	 * @param parentElement
	 * @param elementName
	 * @param elementValue
	 * @param dom
	 */
	private static void appendChild(Element parentElement, String elementName, String elementValue, Document dom) {
		if (parentElement!=null && elementValue!=null) {
			Element domElement = createDomElement(elementName, elementValue, dom);
			if (domElement!=null) {
				parentElement.appendChild(domElement);
			}
		}
	}
	
	/**
	 * Creates a dom element
	 * @param elementName
	 * @param elementValue
	 * @param dom
	 * @return
	 */
	private static Element createDomElement(String elementName, String elementValue, Document dom) {
		Element element = null;
		try {
			try {
				element = dom.createElement(elementName);
			} catch (DOMException e) {
				LOGGER.warn("Creating an XML node with the element name " + elementName + " failed with dom exception " + e);
			}
			if (element==null) {
				return null;
			}
			if (elementValue == null || "".equals(elementValue.trim())) {
				element.appendChild (dom.createTextNode (""));
			} else {
				try {
					element.appendChild(dom.createTextNode(Html2Text.stripNonValidXMLCharacters(elementValue)));
				} catch (DOMException e) {
					LOGGER.info("Creating the node for text element " + elementName + " and the value " + 
							elementValue + " failed with dom exception " + e);
					element.appendChild(dom.createTextNode (""));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Creating an XML node with the element name " + elementName + " failed with " + e);
		}					
		return element;
	}

	
}
