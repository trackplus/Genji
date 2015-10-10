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

package com.aurel.track.exchange.track.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.exchange.track.ExchangeHistoryFieldEntry;
import com.aurel.track.exchange.track.ExchangeHistoryTransactionEntry;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * SAX parser to parse the query tree
 */
public class ImporterDataParser extends DefaultHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(ImporterDataParser.class);
	private String fieldID;
	private String parameterCode;
	//boolean values to specify the actual parent for the character data
	private boolean isActualValue;
	private boolean isNewValue;
	private boolean isOldValue;
	private boolean isBudgetDescription;
	private boolean isCostDescription;
	private boolean isAttachmentDescription;
	
	private List<ExchangeWorkItem> exchangeWorkItemsList = new ArrayList<ExchangeWorkItem>();
	private ExchangeWorkItem currentWorkItem;
	private List<ExchangeHistoryTransactionEntry> historyValues;
	private List<ExchangeHistoryFieldEntry> historyFieldChanges;
	private List<Integer> consultedList;
	private List<Integer> informedList;
	private ExchangeHistoryFieldEntry actualHistoryFieldChange;
	private List<TBudgetBean> budgetBeanList;
	private List<TBudgetBean> plannedValueBeanList;
	private TBudgetBean actualBudgetBean; 
	private List<TCostBean> expenseBeanList;
	private TCostBean actualCostBean;
	private TAttachmentBean actualAttachmentBean;
	private List<TAttachmentBean> attachmentBeanList;
	//those fields which are not present in the target track+ application
	private Set<Integer> missingFields = new HashSet<Integer>();
	private Map<Integer, Integer> fieldMatcher;
	private String textContent;
	
	private int numberOfWorkItems = 0;

	public List<ExchangeWorkItem> parse(File xml, Map<Integer, Integer> fieldMatcher) {
		this.fieldMatcher = fieldMatcher;
		//get a factory
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			LOGGER.debug("Data parser started...");
			saxParser.parse(xml, this);
			LOGGER.debug("Data parser done");
			return exchangeWorkItemsList;
		}catch(SAXException se) {
			LOGGER.error(ExceptionUtils.getStackTrace(se));
		}catch(ParserConfigurationException pce) {
			LOGGER.error(ExceptionUtils.getStackTrace(pce));
		}catch (IOException ie) {
			LOGGER.error(ExceptionUtils.getStackTrace(ie));
		}
		return null;
	}
	
	//Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		textContent = null;
		if (qName.equalsIgnoreCase(ExchangeFieldNames.ITEM)) {
			currentWorkItem=new ExchangeWorkItem(); 
			currentWorkItem.setUuid(attributes.getValue(ExchangeFieldNames.UUID));
			String strWorkItemID = attributes.getValue(ExchangeFieldNames.WORKITEMID);
			if (strWorkItemID!=null) {
				currentWorkItem.setWorkItemID(new Integer(strWorkItemID));
			}
			exchangeWorkItemsList.add(currentWorkItem);
			numberOfWorkItems++;
		}
		if (qName.equalsIgnoreCase(ExchangeFieldNames.ITEM_ATTRIBUTE)) {
			fieldID = attributes.getValue(ExchangeFieldNames.FIELDID);
			parameterCode = attributes.getValue(ExchangeFieldNames.PARAMETERCODE);
			isActualValue = true;
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.HISTORY_LIST)) {
			historyValues = new ArrayList<ExchangeHistoryTransactionEntry>(); 
			currentWorkItem.setHistoryValues(historyValues);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.TRANSACTION)) {
			ExchangeHistoryTransactionEntry exchangeHistoryTransactionEntry = 
				new ExchangeHistoryTransactionEntry().deserializeBean(ParserUtil.getAttributesMap(attributes));
			historyFieldChanges = new ArrayList<ExchangeHistoryFieldEntry>();
			exchangeHistoryTransactionEntry.setHistoryFieldChanges(historyFieldChanges);
			historyValues.add(exchangeHistoryTransactionEntry);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.FIELDCHANGE)) {
			actualHistoryFieldChange = new ExchangeHistoryFieldEntry().deserializeBean(
					ParserUtil.getAttributesMap(attributes));
			historyFieldChanges.add(actualHistoryFieldChange);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.NEWVALUE)) {
			isNewValue = true;	
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.OLDVALUE)) {
			isOldValue = true;
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.CONSULTANT_LIST)) {
			consultedList = new ArrayList<Integer>();
			currentWorkItem.setConsultedList(consultedList);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.CONSULTANT)) {
			String strPerson = attributes.getValue(ExchangeFieldNames.PERSON);
			if (strPerson!=null) {
				currentWorkItem.getConsultedList().add(new Integer(strPerson));
			}
		}
		
		if(qName.equalsIgnoreCase(ExchangeFieldNames.INFORMANT_LIST)) {
			informedList = new ArrayList<Integer>();
			currentWorkItem.setInformedList(informedList);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.INFORMANT)) {
			String strPerson = attributes.getValue(ExchangeFieldNames.PERSON);
			if (strPerson!=null) {
				currentWorkItem.getInformedList().add(new Integer(strPerson));
			}
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.BUDGET_LIST)) {
			budgetBeanList = new ArrayList<TBudgetBean>();
			currentWorkItem.setBudgetBeanList(budgetBeanList);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.BUDGET_ELEMENT)) {
			actualBudgetBean = new TBudgetBean().deserializeBean(ParserUtil.getAttributesMap(attributes));
			actualBudgetBean.setWorkItemID(currentWorkItem.getWorkItemID());
			budgetBeanList.add(actualBudgetBean);
			isBudgetDescription = true;
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.PLANNED_VALUE_LIST)) {
			plannedValueBeanList = new ArrayList<TBudgetBean>();
			currentWorkItem.setPlannedValueBeanList(plannedValueBeanList);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.PLANNED_VALUE_ELEMENT)) {
			actualBudgetBean = new TBudgetBean().deserializeBean(ParserUtil.getAttributesMap(attributes));
			actualBudgetBean.setWorkItemID(currentWorkItem.getWorkItemID());
			plannedValueBeanList.add(actualBudgetBean);
			isBudgetDescription = true;
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.REMAINING_BUDGET_ELEMENT)) {
			TActualEstimatedBudgetBean actualEstimatedBudgetBean = 
				new TActualEstimatedBudgetBean().deserializeBean(ParserUtil.getAttributesMap(attributes));
			actualEstimatedBudgetBean.setWorkItemID(currentWorkItem.getWorkItemID());
			currentWorkItem.setActualEstimatedBudgetBean(actualEstimatedBudgetBean);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.EXPENSE_LIST)) {
			expenseBeanList = new ArrayList<TCostBean>();
			currentWorkItem.setExpenseBeanList(expenseBeanList);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.EXPENSE_ELEMENT)) {
			actualCostBean = new TCostBean().deserializeBean(ParserUtil.getAttributesMap(attributes));
			actualCostBean.setWorkitem(currentWorkItem.getWorkItemID());
			expenseBeanList.add(actualCostBean);
			isCostDescription = true;
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.ATTACHMENT_LIST)) {
			attachmentBeanList = new ArrayList<TAttachmentBean>();
			currentWorkItem.setAttachmentBeanList(attachmentBeanList);
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.ATTACHMENT_ELEMENT)) {
			actualAttachmentBean = new TAttachmentBean().deserializeBean(ParserUtil.getAttributesMap(attributes));
			actualAttachmentBean.setWorkItem(currentWorkItem.getWorkItemID());
			attachmentBeanList.add(actualAttachmentBean);
			isAttachmentDescription = true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase(ExchangeFieldNames.TRACKPLUS_EXCHANGE)){
			if (!missingFields.isEmpty()) {
				Iterator<Integer> iterator = missingFields.iterator();
				LOGGER.warn("The following fields are not known in the local system and their values will be ignored");
				while (iterator.hasNext()) {
					Integer fieldID = iterator.next();
					LOGGER.warn(fieldID + " ");
				}
			}
			LOGGER.info("Number of workItems parsed: " + numberOfWorkItems);
		}		if (qName.equalsIgnoreCase(ExchangeFieldNames.ITEM_ATTRIBUTE)) {
			isActualValue = false;
		}		if (qName.equalsIgnoreCase(ExchangeFieldNames.NEWVALUE)) {
			isNewValue = false;	
		}
		
		if (qName.equalsIgnoreCase(ExchangeFieldNames.OLDVALUE)) {
			isOldValue = false;
		}
		if (qName.equalsIgnoreCase(ExchangeFieldNames.BUDGET_ELEMENT) ||
				qName.equalsIgnoreCase(ExchangeFieldNames.PLANNED_VALUE_ELEMENT)) {
			isBudgetDescription = false;
		}
		if (qName.equalsIgnoreCase(ExchangeFieldNames.EXPENSE_ELEMENT)) {
			isCostDescription = false;
		}
		if (qName.equalsIgnoreCase(ExchangeFieldNames.ATTACHMENT_ELEMENT)) {
			isAttachmentDescription = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (textContent==null) {
			textContent = new String(ch,start,length);
		} else {
			textContent = textContent.concat(new String(ch,start,length));
		}
		Integer externalFieldID = null;
		Integer intParameterCode = null;
		if (fieldID!=null) {
			externalFieldID = new Integer(fieldID);
		}
		if (parameterCode!=null) {
			intParameterCode = new Integer(parameterCode); 
		}
		if (isActualValue) {
			Map<String, Object> actualFieldValues = currentWorkItem.getActualFieldValuesMap();
			Integer internalFieldID;
			if (fieldMatcher.get(externalFieldID)!=null) {
				//custom fields
				internalFieldID = fieldMatcher.get(externalFieldID);
			} else {
				//system fields
				internalFieldID = externalFieldID;
			}
			String externalMergeKey = MergeUtil.mergeKey(externalFieldID, intParameterCode);
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(internalFieldID, intParameterCode);
			if (fieldTypeRT==null) {
				missingFields.add(externalFieldID);
				//LOGGER.warn("No field found for fieldID " + intFieldID + " and parameterCode " + intParameterCode);
			} else {
				if (fieldTypeRT.isMultipleValues()) {
					List<String> multipleValues = null;
					try {
						multipleValues = (List)actualFieldValues.get(externalMergeKey);
					} catch (Exception e) {
						LOGGER.warn(actualFieldValues.get(externalMergeKey).getClass().getName());
					}
					if (multipleValues==null) {
						multipleValues = new ArrayList<String>();
						actualFieldValues.put(externalMergeKey, multipleValues);
					}
					multipleValues.add(textContent);
				} else {
					actualFieldValues.put(externalMergeKey, textContent);
				}
			}
			//isActualValue = false;
		}
		if (isNewValue) {
			actualHistoryFieldChange.setNewStringValue(textContent);
		}
		if (isOldValue) {
			actualHistoryFieldChange.setOldStringValue(textContent);
		}
		if (isBudgetDescription) {
			actualBudgetBean.setChangeDescription(textContent);
			//isBudgetDescription = false;
		}
		if (isCostDescription) {
			actualCostBean.setDescription(textContent);
		}
		if (isAttachmentDescription) {
			actualAttachmentBean.setDescription(textContent);
		}
	}
	
}
