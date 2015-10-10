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
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TCostCenterBean;
import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;

public class ImporterDropdownParser extends DefaultHandler {	
	private static final Logger LOGGER = LogManager.getLogger(ImporterDropdownParser.class);
	private SortedMap<String, List<ISerializableLabelBean>> externalDropdowns = new TreeMap<String, List<ISerializableLabelBean>>();
	
	//private SortedMap<Integer, List<TOptionBean>> externalOptions = new TreeMap<Integer, List<TOptionBean>>();
	
	private static Set<Integer> missingFields = new HashSet<Integer>();
	private Map<Integer, Integer> fieldMatcher;
	
	public SortedMap<String, List<ISerializableLabelBean>> parse(File xml, Map<Integer, Integer> fieldMatcher) {			
		this.fieldMatcher = fieldMatcher;
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			//parse the file and also register this class for call backs
			LOGGER.debug("Dropdown parser started...");
            sp.parse(xml, this);
            LOGGER.debug("Dropdown parser done");
            return externalDropdowns;
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
		if(qName.equalsIgnoreCase(ExchangeFieldNames.TRACKPLUS_EXCHANGE)){
            return;
        }
		Map<String, String> attributesMap = ParserUtil.getAttributesMap(attributes);
        if(qName.equalsIgnoreCase(ExchangeFieldNames.DROPDOWN_ELEMENT)) {
        	addLabelBean(externalDropdowns, attributesMap);
        				
        }        
        if(qName.equalsIgnoreCase(ExchangeFieldNames.COSTCENTER) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.ACCOUNT) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.DEPARTMENT) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.SYSTEMSTATE) || 
        		qName.equalsIgnoreCase(ExchangeFieldNames.PROJECTTYPE) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.LIST) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.OPTION) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.FIELDCONFIG) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.OPTIONSETTINGS) || 
        		qName.equalsIgnoreCase(ExchangeFieldNames.TEXTBOXSETTINGS) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.GENERALSETTINGS) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.ACL) ||
        		qName.equalsIgnoreCase(ExchangeFieldNames.ROLE) || 
        		qName.equalsIgnoreCase(ExchangeFieldNames.LINKED_ITEMS)) { 
        	addLabelBean(externalDropdowns, attributesMap, qName);        	
        }        
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {		
		if(qName.equalsIgnoreCase(ExchangeFieldNames.TRACKPLUS_EXCHANGE)){
			if (!missingFields.isEmpty()) {
				Iterator<Integer> iterator = missingFields.iterator();
				LOGGER.warn("The following select fields are not known in the local system and their values will be ignored");
				while (iterator.hasNext()) {
					Integer fieldID = iterator.next();
					LOGGER.warn(fieldID + " ");
				}
			}
		}
	}
	
    private void addLabelBean(SortedMap<String, List<ISerializableLabelBean>> dropDowns, Map<String, String> attributesMap) {    	    	
    	String strExternalFieldID = attributesMap.get(ExchangeFieldNames.FIELDID);
    	String strParameterCode = attributesMap.get(ExchangeFieldNames.PARAMETERCODE);
    	Integer parameterCode = null;
    	if (strParameterCode!=null) {
    		parameterCode = new Integer(strParameterCode);
    	}    	
    	Integer internalFieldID;
    	Integer externalFieldID = new Integer(strExternalFieldID);
    	if (fieldMatcher.get(externalFieldID)!=null) {    		
    		internalFieldID = fieldMatcher.get(externalFieldID);
    	} else {
    		LOGGER.warn("No matcher field found for externalFieldID " + externalFieldID);
    		internalFieldID = externalFieldID;
    	}    	
    	IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(internalFieldID, parameterCode);
    	if (fieldTypeRT==null) {    		
    		missingFields.add(externalFieldID);
    		return;
    	}    	
    	ILookup lookup = (ILookup)fieldTypeRT;
    	ISerializableLabelBean labelBean = lookup.deserializeBean(attributesMap);
    	if (labelBean==null) {
    		LOGGER.warn("Deserialized labelBean is null for field " + strExternalFieldID);
    	} else {
	    	String mergeKey = MergeUtil.mergeKey(strExternalFieldID, strParameterCode);
	    	List<ISerializableLabelBean> labelBeanList = dropDowns.get(mergeKey);
	    	if (labelBeanList == null) {
	    		labelBeanList = new ArrayList<ISerializableLabelBean>();
	    		dropDowns.put(mergeKey, labelBeanList);
	    	}
	    	labelBeanList.add(labelBean);
    	}
    }
    
    private void addLabelBean(SortedMap<String, List<ISerializableLabelBean>> dropDowns,
    		Map<String, String> attributesMap, String name) {    	    	
    	ISerializableLabelBean labelBean = null;
    	if (name==ExchangeFieldNames.COSTCENTER) {
    		labelBean = new TCostCenterBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.ACCOUNT) {
    		labelBean = new TAccountBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.DEPARTMENT) {
    		labelBean = new TDepartmentBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.SYSTEMSTATE) {
    		labelBean = new TSystemStateBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.PROJECTTYPE) {
    		labelBean = new TProjectTypeBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.LIST) {
    		labelBean = new TListBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.OPTION) {
    		labelBean = new TOptionBean().deserializeBean(attributesMap);
    	}    	
    	if (name==ExchangeFieldNames.FIELDCONFIG) {
    		labelBean = new TFieldConfigBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.OPTIONSETTINGS) {
    		labelBean = new TOptionSettingsBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.TEXTBOXSETTINGS) {
    		labelBean = new TTextBoxSettingsBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.GENERALSETTINGS) {
    		labelBean = new TGeneralSettingsBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.ROLE) {
    		labelBean = new TRoleBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.ACL) {
    		labelBean = new TAccessControlListBean().deserializeBean(attributesMap);
    	}
    	if (name==ExchangeFieldNames.LINKED_ITEMS) {
    		labelBean = new TWorkItemBean().deserializeBean(attributesMap);
    	}
    	if (labelBean==null) {
    		LOGGER.warn("Deserialized labelBean is null for " + name);
    	} else {	    	
	    	List<ISerializableLabelBean> labelBeanList = dropDowns.get(name);
	    	if (labelBeanList == null) {
	    		labelBeanList = new ArrayList<ISerializableLabelBean>();
	    		dropDowns.put(name, labelBeanList);
	    	}
	    	labelBeanList.add(labelBean);
    	}
    }
    
}
