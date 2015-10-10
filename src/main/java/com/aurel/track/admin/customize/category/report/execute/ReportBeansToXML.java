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


package com.aurel.track.admin.customize.category.report.execute;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.linkType.ILinkType;
import com.aurel.track.linkType.ItemLinkSpecificData;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ComputedValuesLoaderBL;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.HTMLSanitiser;
import com.aurel.track.util.emailHandling.Html2Text;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

public class ReportBeansToXML {

	static final Logger LOGGER = LogManager.getLogger(ReportBeansToXML.class);
	public static String BUDGET_EXPENSE_VALUE = "value";
	public static String BUDGET_EXPENSE_UNIT = "unit";
	
	public static String PLAIN_SUFFIX = "Plain";
	
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
			LOGGER.error ("Creating the transformer failed with TransformerConfigurationException: " + e.getMessage(), e);
			return;
		}
		try{
			transformer.transform (new DOMSource(dom), new StreamResult(outputStream));
		} catch (TransformerException e){
			LOGGER.error ("Transform failed with TransformerException: " + e.getMessage(), e);
		}
	}



	public Document convertToDOM(List<ReportBean> items, boolean withHistory, Locale locale, 
			String personName, String filterName, String filterExpression, boolean useProjectSpecificID) {
		boolean budgetActive = ApplicationBean.getApplicationBean().getBudgetActive();
		Document dom = null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder ();
			dom = builder.newDocument ();	
		}catch (FactoryConfigurationError e){
			LOGGER.error ("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage(), e);
			return null;
		}catch (ParserConfigurationException e){
			LOGGER.error ("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage(), e);
			return null;
		}
		Element root = dom.createElement ("track-report");
		appendChild(root, "createdBy", personName, dom);
		Element filter=dom.createElement("filter");
		appendChild(filter, "name", filterName, dom);
		appendChild(filter, "expression", filterExpression, dom);
		root.appendChild(filter);
		Map<Integer, ILinkType> linkTypeIDToLinkTypeMap = LinkTypeBL.getLinkTypeIDToLinkTypeMap();
		List<TFieldBean> fields = FieldBL.loadAll();
		String issueNoName = null;
		if (useProjectSpecificID) {
			for (Iterator<TFieldBean> iterator = fields.iterator(); iterator.hasNext();) {
				TFieldBean fieldBean = iterator.next();
				if (SystemFields.INTEGER_ISSUENO.equals(fieldBean.getObjectID())) {
					issueNoName = fieldBean.getName();
					break;
				}
			}
		}
		for (Iterator<TFieldBean> iterator = fields.iterator(); iterator.hasNext();) {
			//only one from INTEGER_ISSUENO and INTEGER_PROJECT_SPECIFIC_ISSUENO should be shown
			TFieldBean fieldBean = iterator.next();
			if (useProjectSpecificID) {
				if (SystemFields.INTEGER_ISSUENO.equals(fieldBean.getObjectID())) {
					iterator.remove();
				}
				if (SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO.equals(fieldBean.getObjectID())) {
					fieldBean.setName(issueNoName);
				}
			} else {
				if (SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO.equals(fieldBean.getObjectID())) {
					iterator.remove();
				}
			}
		}
		String unavailable = LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.unavailable", locale);
		for (ReportBean reportBean : items) {
			Element item = dom.createElement ("item");
			TWorkItemBean workItemBean = reportBean.getWorkItemBean();
			for (TFieldBean fieldBean : fields) {
				Integer fieldID = fieldBean.getObjectID();
				String fieldName = fieldBean.getName();
				Object attributeValue = workItemBean.getAttribute(fieldID);
				if (attributeValue!=null) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					if (fieldTypeRT!=null) {
						if (fieldTypeRT.isLong()) {
							String isoValue = (String)reportBean.getShowISOValue(fieldID);
							if (isoValue!=null && !isoValue.equals(unavailable)) {
								String plainDescription = (String)workItemBean.getAttribute(fieldID);
								try {
									plainDescription = Html2Text.getNewInstance().convert((String)attributeValue);
								} catch (Exception e) {
									LOGGER.info("Transforming the HTML to plain text for workItemID " + workItemBean.getObjectID() + " and field " +
											fieldID + " failed with " + e);
								}
								appendChild(item, fieldName + PLAIN_SUFFIX, plainDescription, dom);
								appendChild(item, fieldName, HTMLSanitiser.stripHTML((String)attributeValue), dom);
							}
						} else {
							String isoValue = (String) reportBean.getShowISOValue(fieldID);
							appendChild(item, fieldName, isoValue, dom);
						}
						if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION || fieldTypeRT.isComposite()) {
							//add custom list sortOrder
							appendChild(item, fieldBean.getName() + TReportLayoutBean.PSEUDO_COLUMN_NAMES.CUSTOM_OPTION_SYMBOL, 
								(String)reportBean.getShowISOValue(Integer.valueOf(-fieldID)), dom);	
						}
						if (hasExtraSortField(fieldID)) {
							Comparable sortOrder = reportBean.getSortOrder(fieldID);
							if (sortOrder!=null) {
								appendChild(item, fieldBean.getName() + TReportLayoutBean.PSEUDO_COLUMN_NAMES.ORDER, 
										sortOrder.toString(), dom);
							}
						}
					}
				}
			}
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.GLOBAL_ITEM_NO, 
					workItemBean.getObjectID().toString(), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.PROJECT_TYPE, 
				reportBean.getProjectType(), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_FLAG, 
				reportBean.getStateFlag().toString(), dom);
			appendChild (item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.COMMITTED_DATE_CONFLICT, 
				new Boolean(reportBean.isCommittedDateConflict()).toString(), dom);
			appendChild (item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.TARGET_DATE_CONFLICT, 
					new Boolean(reportBean.isTargetDateConflict()).toString(), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANNED_VALUE_CONFLICT, 
				new Boolean(reportBean.isPlannedValueConflict()).toString(), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_CONFLICT, 
					new Boolean(reportBean.isBudgetConflict()).toString(), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.SUMMARY, 
					new Boolean(reportBean.isSummary()).toString(), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.INDENT_LEVEL, 
					String.valueOf(reportBean.getLevel()), dom);
			String consultants = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST);
			if (consultants!=null && !"".equals(consultants)) {
				appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.CONSULTANT_LIST, consultants, dom);
			}
			String informed = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST);
			if (informed!=null && !"".equals(informed)) {
				appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.INFORMANT_LIST, informed, dom);
			}
			String linkedItems = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS);
			if (linkedItems!=null && !"".equals(linkedItems)) {
				appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.LINKED_ITEMS, linkedItems, dom);
			}
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.PRIVATE_SYMBOL, 
					(String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.OVERFLOW_SYMBOL, 
					(String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.PRIORITY_SYMBOL, 
					(String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.SEVERITY_SYMBOL, 
					(String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_SYMBOL, 
					(String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL), dom);
			appendChild(item, TReportLayoutBean.PSEUDO_COLUMN_NAMES.ISSUETYPE_SYMBOL, 
					(String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL), dom);
			String valueAndMeasurementUnit;
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_COST, valueAndMeasurementUnit, dom, item);
			
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_TIME, valueAndMeasurementUnit, dom, item);
			
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_COST, valueAndMeasurementUnit, dom, item);
				
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_TIME, valueAndMeasurementUnit, dom, item);
			if (budgetActive) {
				valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST);
				createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_COST, valueAndMeasurementUnit, dom, item);
				
				valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME);
				createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_TIME, valueAndMeasurementUnit, dom, item);
			}
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_COST, valueAndMeasurementUnit, dom, item);
			
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_TIME, valueAndMeasurementUnit, dom, item);
			
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_COST, valueAndMeasurementUnit, dom, item);
			
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_TIME, valueAndMeasurementUnit, dom, item);
			
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_COST, valueAndMeasurementUnit, dom, item);
			
			valueAndMeasurementUnit = (String)reportBean.getShowISOValue(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME);
			createComputedValueElement(TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_TIME, valueAndMeasurementUnit, dom, item);
			
			Element links = createLinkElement(reportBean.getReportBeanLinksSet(), linkTypeIDToLinkTypeMap, locale, dom);
			if (links!=null) {
				item.appendChild(links);
			}
			if (withHistory){
				ReportBeanWithHistory reportBeanWithHistory=(ReportBeanWithHistory)reportBean;
				Element historyElement = createHistoryElement(reportBeanWithHistory.getHistoryValuesMap(), dom);
				if (historyElement!=null) {
					item.appendChild(historyElement);
				}
				Element commentElement = createCommentElement(reportBeanWithHistory.getComments(), dom);
				if (commentElement!=null) {
					item.appendChild(commentElement);
				}
				Element budgetElement = createBudgetElement(reportBeanWithHistory.getBudgetHistory(), dom,locale);
				if (budgetElement!=null) {
					item.appendChild(budgetElement);
				}
				Element plannedValueElement = createBudgetElement(reportBeanWithHistory.getPlannedValueHistory(), dom,locale);
				if (plannedValueElement!=null) {
					item.appendChild(plannedValueElement);
				}
				if (plannedValueElement!=null) {
					//do not add estimated remaining budget element if no budget exists
					Element remainingBudgetElement = createRemainingBudgetElement(reportBeanWithHistory.getActualEstimatedBudgetBean(), dom,locale);
					if (remainingBudgetElement!=null) {
						item.appendChild(remainingBudgetElement);
					}
				}
				Element costElement = createExpenseElement(reportBeanWithHistory.getCosts(), dom,locale);
				if (costElement!=null) {
					item.appendChild(costElement);
				}
			}
			root.appendChild (item);
		}
		dom.appendChild (root);
		return dom;
	}

	/**
	 * Whether the field has extra sort order field
	 * @param fieldID
	 * @return
	 */
	public static boolean hasExtraSortField(Integer fieldID) {
		if (fieldID!=null && fieldID.intValue()>0) {
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null) {
				if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION || fieldTypeRT.isComposite() || fieldTypeRT.getValueType()==ValueType.SYSTEMOPTION) {
					//for lists the field name to sort by is not by the list field (would be alphabetically by label) but by they have explicit sort order field
					if (fieldTypeRT.getValueType()==ValueType.SYSTEMOPTION) {
						switch (fieldID.intValue()) {
						case SystemFields.STATE:
						case SystemFields.ISSUETYPE:
						case SystemFields.PRIORITY:
						case SystemFields.SEVERITY:
						case SystemFields.RELEASENOTICED:
						case SystemFields.RELEASESCHEDULED:
							//there is explicit sort order only for some system lists (not for persons)
							return true;
						}
					} else {
						//there is explicit sort order for each custom list
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Creates a dom elemebnt and adds to the parent
	 * @param parentElement
	 * @param elementName
	 * @param elementValue
	 * @param dom
	 */
	private void appendChild(Element parentElement, String elementName, String elementValue, Document dom) {
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
	private Element createDomElement(String elementName, String elementValue, Document dom) {
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

	/**
	 * Creates the computed value element by parsing the valueAndMeasurementUnit string
	 * TODO solve it without parsing
	 * @param elementName
	 * @param valueAndMeasurementUnit
	 * @param dom
	 * @return
	 */
	private void createComputedValueElement(String elementName, String valueAndMeasurementUnit, Document dom, Element parentElement) {
		try {
			if (notEmpty(valueAndMeasurementUnit)) {
				String[] parts = valueAndMeasurementUnit.split(ComputedValuesLoaderBL.ISOShowValueSplitter);
				if (parts != null && parts.length > 0) {
					Element computedValueElement = dom.createElement(elementName);
					computedValueElement.appendChild(createDomElement(BUDGET_EXPENSE_VALUE, parts[0], dom));
					if (parts.length>1) {
						computedValueElement.appendChild(createDomElement(BUDGET_EXPENSE_UNIT, parts[1], dom));
					}
					parentElement.appendChild(computedValueElement);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Adding the computed value for " + elementName + 
					" and value " + valueAndMeasurementUnit + " failed with " + e.getMessage(), e);
		}
	}
	
	private boolean notEmpty(String str) {
		if (str==null) {
			return false;
		}
		return str.length()>0;
	}
	
	/**
	 * Creates the trail history element
	 * @param historyList
	 * @param dom
	 * @return
	 */
	private Element createHistoryElement(Map<Integer, Map<Integer, HistoryValues>> historyTransactionsMap, Document dom) {
		//get the sorted list from map  
		List<HistoryValues> historyValuesList = HistoryLoaderBL.getHistoryValuesList(historyTransactionsMap, false);
		HistoryLoaderBL.setTransactionLimits(historyValuesList);
		if (historyValuesList!=null && !historyValuesList.isEmpty()) {
			Element historyTransactionsList = dom.createElement("historyList");
			Iterator<HistoryValues> iterator = historyValuesList.iterator();
			while (iterator.hasNext()) {
				HistoryValues historyValues = iterator.next();
				if (historyValues.isNewTransction()) {
				}
				Element fieldChangeElement = dom.createElement("fieldChange");
				if (historyValues.isNewTransction()) {
					fieldChangeElement.setAttribute("firstInTransaction", "true");
				}
				if (historyValues.isDate()) {
					fieldChangeElement.setAttribute("isDate", "true");
				}
				appendChild(fieldChangeElement, "changed-by", 
						historyValues.getChangedByName(), dom);
				appendChild(fieldChangeElement, "changed-at", 
						DateTimeUtils.getInstance().formatISODateTime(historyValues.getLastEdit()), dom);
				appendChild(fieldChangeElement, "field", historyValues.getFieldName(), dom);
				String newValue =  historyValues.getNewShowValue();
				if (newValue != null && newValue.length() > 25) {
					newValue = HTMLSanitiser.stripHTML(newValue);
				}
				String oldValue =  historyValues.getOldShowValue();
				if (oldValue != null && oldValue.length() > 25) {
					oldValue = HTMLSanitiser.stripHTML(oldValue);
				}
				appendChild(fieldChangeElement, "newValue", newValue, dom);
				appendChild(fieldChangeElement, "oldValue", oldValue, dom);
				historyTransactionsList.appendChild(fieldChangeElement);
			}
			return historyTransactionsList;
		}
		return null;
	}
	
	private Element createCommentElement(List<HistoryValues> commentsList, Document dom) {
		if (commentsList!=null && !commentsList.isEmpty()) { 
			Element comments = dom.createElement("commentList");
			Iterator<HistoryValues> iterator = commentsList.iterator();
			while (iterator.hasNext()) {
				HistoryValues historyValues = iterator.next();
				Element commentElement = dom.createElement("commentElement");
				commentElement.appendChild(createDomElement("changed-by", 
						historyValues.getChangedByName(), dom));
				commentElement.appendChild(createDomElement("changed-at", 
						DateTimeUtils.getInstance().formatISODateTime(historyValues.getLastEdit()), dom));
				if (historyValues.getTimesEdited()!=null) {
					commentElement.appendChild(createDomElement("timesEdited", 
						historyValues.getTimesEdited().toString(), dom));
				}
				commentElement.appendChild(createDomElement("comment", historyValues.getNewShowValue(), dom));
				comments.appendChild(commentElement);
			}	
			return comments;
		}
		return null;
	}
	
	private Element createLinkElement(SortedSet<ReportBeanLink> linkSet, Map<Integer, ILinkType> linkTypeIDToLinkTypeMap, Locale locale, Document dom) {
		if (linkSet!=null && !linkSet.isEmpty()) { 
			Element links = dom.createElement("links");
			Iterator<ReportBeanLink> iterator = linkSet.iterator();
			while (iterator.hasNext()) {
				ReportBeanLink reportBeanLink = iterator.next();
				Element linkElement = dom.createElement("link");
				linkElement.appendChild(createDomElement("linkTypeName", 
						reportBeanLink.getLinkTypeName(), dom));
				if (reportBeanLink.getLinkDirection()!=null) {
					linkElement.appendChild(createDomElement("linkDirection", 
						reportBeanLink.getLinkDirection().toString(), dom));
				}
				if (reportBeanLink.getWorkItemID()!=null) {
					linkElement.appendChild(createDomElement("issueNo", 
							reportBeanLink.getWorkItemID().toString(), dom));
				}
				Map<String, String> itemLinkSpecificMap = getLinkSpecificData(reportBeanLink, linkTypeIDToLinkTypeMap, locale);
				if (itemLinkSpecificMap!=null) {
					for (Map.Entry<String, String> entry : itemLinkSpecificMap.entrySet()) {
						linkElement.appendChild(createDomElement(entry.getKey(), entry.getValue(), dom));
					}
				}
		links.appendChild(linkElement);
			}	
			return links;
		}
		return null;
	}
	
	/**
	 * Gets the links specific data for a link
	 * @param reportBeanLink
	 * @param linkTypeIDToLinkTypeMap
	 * @param locale
	 * @return
	 */
	public static Map<String, String> getLinkSpecificData(ReportBeanLink reportBeanLink, Map<Integer, ILinkType> linkTypeIDToLinkTypeMap, Locale locale) {
		if (reportBeanLink!=null) {
			Integer linkTypeID = reportBeanLink.getLinkTypeID();
			ItemLinkSpecificData itemLinkSpecificData = reportBeanLink.getLinkSpecificData();
			if (linkTypeID!=null && itemLinkSpecificData!=null) {
				ILinkType linkType = linkTypeIDToLinkTypeMap.get(linkTypeID);
				if (linkType!=null) {
					return linkType.serializeItemLinkSpecificData(itemLinkSpecificData, locale);
				}
			}
		}
		return null;
	}
	/**
	 * Creates the budget history elements
	 * @param historyList
	 * @param dom
	 * @return
	 */
	private Element createBudgetElement (List historyList, Document dom, Locale locale) {
		if (historyList!=null && !historyList.isEmpty()) {
			TBudgetBean budgetBean;
			Iterator itrHistoryList;
			itrHistoryList = historyList.iterator();
			Element historyListElement = dom.createElement("budget-history-list");
			Element historyEntryElement;
			while (itrHistoryList.hasNext()) {
				historyEntryElement = dom.createElement("budget-history-element");
				budgetBean = (TBudgetBean)itrHistoryList.next();
				historyEntryElement.appendChild(createDomElement("changed-by", budgetBean.getChangedByName(), dom));
				
				historyEntryElement.appendChild(createDomElement("changed-at", DateTimeUtils.getInstance().formatISODateTime(budgetBean.getLastEdit()), dom));
				historyEntryElement.appendChild(createDomElement("cost-value", DoubleNumberFormatUtil.formatISO(budgetBean.getEstimatedCost()), dom));				
				if (budgetBean.getCurrency()!=null) {
					historyEntryElement.appendChild(createDomElement("currency", budgetBean.getCurrency(), dom));
				}
				historyEntryElement.appendChild(createDomElement("effort-value", DoubleNumberFormatUtil.formatISO(budgetBean.getEstimatedHours()), dom));
				String timeUnitString = AccountingBL.getTimeUnitOption(budgetBean.getTimeUnit(), locale);
				if (timeUnitString!=null) {
					historyEntryElement.appendChild(createDomElement(BUDGET_EXPENSE_UNIT, timeUnitString, dom));
				}
				if (budgetBean.getChangeDescription()!=null && !"".equals(budgetBean.getChangeDescription().trim())) {
					historyEntryElement.appendChild(createDomElement("change-description", budgetBean.getChangeDescription(), dom));
				}
				
				historyListElement.appendChild(historyEntryElement);
			}
			return historyListElement;
		}
		return null;
	}

	/**
	 * Creates the expense elements
	 * @param historyList
	 * @param dom
	 * @return
	 */
	private Element createExpenseElement (List historyList, Document dom,Locale locale) {
		if (historyList!=null && !historyList.isEmpty()) {
			TCostBean costBean;
			Iterator itrHistoryList;
			itrHistoryList = historyList.iterator();
			Element historyListElement = dom.createElement("expense-list");
			Element historyEntryElement;
			while (itrHistoryList.hasNext()) {
				historyEntryElement = dom.createElement("expense-element");
				costBean = (TCostBean)itrHistoryList.next();
				historyEntryElement.appendChild(createDomElement("changed-by", costBean.getChangedByName(), dom));
				if (costBean.getSubject()!=null && !"".equals(costBean.getSubject().trim())) {
					historyEntryElement.appendChild(createDomElement("subject", costBean.getSubject(), dom));
				}
				if (costBean.getAccountName()!=null && !"".equals(costBean.getAccountName().trim())) {
					historyEntryElement.appendChild(createDomElement("account", costBean.getAccountName(), dom));
				}
				if (costBean.getEffortdate()!=null) {
					historyEntryElement.appendChild(createDomElement("effortDate", DateTimeUtils.getInstance().formatISODateTime(costBean.getEffortdate()), dom));
				}
				historyEntryElement.appendChild(createDomElement("changed-at", DateTimeUtils.getInstance().formatISODateTime(costBean.getLastEdit()), dom));								
				historyEntryElement.appendChild(createDomElement("cost-value", DoubleNumberFormatUtil.formatISO(costBean.getCost()), dom));
				if (costBean.getCurrency()!=null) {
					historyEntryElement.appendChild(createDomElement("currency", costBean.getCurrency(), dom));
				}
				historyEntryElement.appendChild(createDomElement("effort-value", DoubleNumberFormatUtil.formatISO(costBean.getHours()), dom));
				//TODO: replace Hours constant
				String timeUnitString = AccountingBL.getTimeUnitOption(AccountingBL.TIMEUNITS.HOURS, locale); 
				if (timeUnitString!=null) {
					historyEntryElement.appendChild(createDomElement(BUDGET_EXPENSE_UNIT, timeUnitString, dom));
				}
				if (costBean.getDescription()!=null && !"".equals(costBean.getDescription().trim())) {
					historyEntryElement.appendChild(createDomElement("change-description", costBean.getDescription(), dom));
				}
				historyListElement.appendChild(historyEntryElement);
			}
			return historyListElement;
		}
		return null;
	}

	/**
	 * Creates the estimated remaining budget element
	 * @param remainingBudgetBean
	 * @param dom
	 * @return
	 */
	private Element createRemainingBudgetElement (TActualEstimatedBudgetBean remainingBudgetBean, Document dom,Locale locale) {
		if (remainingBudgetBean!=null) {
			Element historyListElement = dom.createElement("remaining-budget-list");
			Element historyEntryElement = dom.createElement("remaining-budget-element");
			historyEntryElement.appendChild(createDomElement("changed-by", remainingBudgetBean.getChangedByName(), dom));
			historyEntryElement.appendChild(createDomElement("changed-at", DateTimeUtils.getInstance().formatISODateTime(remainingBudgetBean.getLastEdit()), dom));
			historyEntryElement.appendChild(createDomElement("cost-value", DoubleNumberFormatUtil.formatISO(remainingBudgetBean.getEstimatedCost()), dom));
			if (remainingBudgetBean.getCurrency()!=null) {
				historyEntryElement.appendChild(createDomElement("currency", remainingBudgetBean.getCurrency(), dom));
			}
			historyEntryElement.appendChild(createDomElement("effort-value", DoubleNumberFormatUtil.formatISO(remainingBudgetBean.getEstimatedHours()), dom));
			String timeUnitString = AccountingBL.getTimeUnitOption(remainingBudgetBean.getTimeUnit(), locale);
			if (timeUnitString!=null) {
				historyEntryElement.appendChild(createDomElement(BUDGET_EXPENSE_UNIT, timeUnitString, dom));
			}
			historyListElement.appendChild(historyEntryElement);
			return historyListElement;
		}
		return null;
	}
}
