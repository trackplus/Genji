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


package com.aurel.track.fieldType.runtime.base;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.CompositeSelectBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.fieldChange.apply.CompositeSelectFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.CompositeSelectFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.CompositeSelectSetterConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.CompositSelectMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.CompositeSelectMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.CompositeSelectMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;

/**
 * A custom composite field is a composite of other custom single fields 
 * It is itself a CustomFieldTypeRT
 * @author Tamas Ruff
 *
 */
public abstract class CustomCompositeBaseRT extends AbstractFieldTypeRT implements ICustomFieldTypeRT {
		
	private static final Logger LOGGER = LogManager.getLogger(CustomCompositeBaseRT.class);
	public static String PART_SPLITTER_STRING = " | ";
	public static String PART_SPLITTER_VALUES_STRING = "|";
	
	/**
	 * Whether the field is a custom field
	 * @return
	 */
	public boolean isCustom() {
		return true;
	}
	
	/**
	 * Whether the field is composite
	 * @return
	 */
	@Override
	public boolean isComposite() {
		return true;
	}
	
	/**
	 * Whether it is a single selection field
	 * @return
	 */
	public boolean isCustomSelect() {
		return false;
	}
	
	/**
	 * Number of parts the field is contained of.  
	 * @return
	 */
	public abstract int getNumberOfParts();
		
	
	/**
	 * The custom field type of each part 
	 * @param index
	 * @return
	 */
	public abstract ICustomFieldTypeRT getCustomFieldType(int index);
	

	/**
	 * Sets the default values of each part of the composite on the workItemBean
	 */
	@Override
	public void processDefaultValue(Integer fieldID, Integer parameterCode, Integer validConfig, 
			Map<String, Object> fieldSettings, TWorkItemBean workItemBean) {
		IFieldTypeRT fieldTypeRT;
		Integer localParameterCode;	
		for (int i = 0; i < getNumberOfParts(); i++) {
			localParameterCode = new Integer(i+1);
			fieldTypeRT = getCustomFieldType(i+1);
			fieldTypeRT.processDefaultValue(
					fieldID, localParameterCode, validConfig, fieldSettings, workItemBean);
		}
	}
	
	/**
	 * Returns all the valadators gathering the validators of each composite part 
	 */
	@Override
	public Map getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean, Integer parameterCode, 
			Object settingsObject, TWorkItemBean workItemBean){
		Map validatorsMap = new HashMap();
		IFieldTypeRT fieldTypeRT;
		Integer internalParameterCode;
		for (int i = 0; i < getNumberOfParts(); i++) {
			fieldTypeRT = getCustomFieldType(i+1);
			internalParameterCode=Integer.valueOf(i+1);
			validatorsMap.putAll(fieldTypeRT.procesLoadValidators(fieldID, 
					fieldConfigBean, internalParameterCode, 
					settingsObject,workItemBean));
		}
		return validatorsMap;
	}

	/**
	 * Loads or calculates the custom attribute value from the map of attributeValueBeans 
	 * into the workItemBean's customAttributeValues map for each composite part
	 * @param fieldID 
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param attributeValueMap 
	 * 			- key: fieldID_parameterCode
	 * 			- value: attributeValueBean or list of attributeValueBeans (for multiple selects) 	
	 */
	@Override
	public void processLoad(Integer fieldID, Integer parameterCode, TWorkItemBean workItemBean, Map<String, Object> attributeValueMap) {
		ICustomFieldTypeRT customFieldTypeRT;
		Integer localParameterCode;	
		for (int i = 0; i < getNumberOfParts(); i++) {
			localParameterCode = new Integer(i+1);
			customFieldTypeRT = getCustomFieldType(i+1);
			customFieldTypeRT.processLoad(fieldID, localParameterCode, workItemBean, attributeValueMap);
		}
	}
	
	/**
	 * Saves the custom attribute value of each composite part
	 * from the workItem customAttributeValues Map to the database  
	 * @param fieldID
	 * @param parameterCode neglected for single custom fields
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	@Override
	public void processSave(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal) {
		ICustomFieldTypeRT customFieldTypeRT;
		Integer localParameterCode;	
		for (int i = 0; i < getNumberOfParts(); i++) {
			localParameterCode = new Integer(i+1);
			customFieldTypeRT = getCustomFieldType(i+1);
			customFieldTypeRT.processSave(
					fieldID, localParameterCode, workItemBean, workItemBeanOriginal);
		}
	}
	
	/**
	 * Loads the field change attributes from the the history  
	 * @param fieldID
	 * @param parameterCode
	 * @param fieldChangeMap: 
	 * 		- key: fieldID_parameterCode
	 * 		- value: fieldChangeBean or list of fieldChangeBeans (typically for multiple selects)
	 * @param newValuesHistoryMap:
	 * 		- key: fieldID_parameterCode
	 * 		- value: the valid field value from fieldChangeBean for the new value
	 * @param oldValuesHistoryMap 
	 * 		- key: fieldID_parameterCode
	 * 		- value: the valid field value from fieldChangeBean for the old value
	 */
	@Override
	public void processHistoryLoad(Integer fieldID, Integer parameterCode, 
			Map fieldChangeMap, Map newValuesHistoryMap, Map oldValuesHistoryMap) {
		ICustomFieldTypeRT customFieldTypeRT;
		Integer localParameterCode;	
		for (int i = 0; i < getNumberOfParts(); i++) {
			localParameterCode = new Integer(i+1);
			customFieldTypeRT = getCustomFieldType(i+1);
			customFieldTypeRT.processHistoryLoad(fieldID, localParameterCode, 
					fieldChangeMap, newValuesHistoryMap, oldValuesHistoryMap);
		}
	}
	
	/**
	 * Saves the field changes explicitly into the history   
	 * @param fieldID
	 * @param parameterCode neglected for single fields
	 * @param historyTransactionID
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param fieldChangeBean the existing TFieldChange entry (for editing/deleting comments, and updating history changes in the last x minutes)
	 */
	@Override
	public void processHistorySave(Integer fieldID, Integer parameterCode, Integer historyTransactionID,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, TFieldChangeBean fieldChangeBean) {
		ICustomFieldTypeRT customFieldTypeRT;
		Integer localParameterCode;	
		for (int i = 0; i < getNumberOfParts(); i++) {
			localParameterCode = new Integer(i+1);
			customFieldTypeRT = getCustomFieldType(i+1);
			customFieldTypeRT.processHistorySave(fieldID, localParameterCode, 
					historyTransactionID, workItemBean, workItemBeanOriginal, fieldChangeBean);
		}
	}
	
	/**
	 * Actualizes the dropDownContainer with the datasources 
	 * for select type composite parts by editing an issue.
	 * The result might differ from loadCreateDataSource because it might be needed 
	 * to add the current value from the workItemBean to the list entries 
	 * (for ex. when right already revoked for the current value).
	 * @param selectContext
	 * @param dropDownContainer 
	 * @return
	 */
	@Override
	public void processLoadDataSource(SelectContext selectContext, DropDownContainer dropDownContainer) {
		for (int i = 0; i < getNumberOfParts(); i++) {
			Integer parameterCode = Integer.valueOf(i+1);
			selectContext.setParameterCode(parameterCode);
			ICustomFieldTypeRT customFieldTypeRT = getCustomFieldType(i+1);
			customFieldTypeRT.processLoadDataSource(selectContext, dropDownContainer);
		}
	}
	
	/**
	 * Loads the datasource for the matcher
	 * used by select fields to get the possible values
	 * It will be called from both field expressions and upper selects 
	 * The value can be a list for simple select or a map of lists for composite selects or a tree
	 * @param matcherValue
	 * @param matcherDatasourceContext the data source may be project dependent. 
	 * @param parameterCode for composite selects
	 * @return the datasource (list or tree)
	 */	
	@Override
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		SortedMap<Integer, List<ILabelBean>> matcherDatasourceLists =
			new TreeMap<Integer, List<ILabelBean>>(); 
		//it should be sorted map because of toXMLString(), 
		//in xml the values should be serialized in the right order
		SortedMap<Integer, Object> actualValuesMap = null;
		ICustomFieldTypeRT customFieldTypeRT;
		if (matcherValue!=null) {
			try {
				actualValuesMap = (SortedMap)matcherValue.getValue();
			} catch (Exception e) {
				LOGGER.warn("Matcher datasource: converting the value to SortedMap failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}		
			if (actualValuesMap==null) {
				actualValuesMap = new TreeMap<Integer, Object>();
				matcherValue.setValue(actualValuesMap);
			}
		}		
		for (int i=0; i<getNumberOfParts();i++) {
			customFieldTypeRT = getCustomFieldType(i+1);
			Integer localParameterCode = Integer.valueOf(i+1);
			matcherDatasourceLists.put(localParameterCode,
					(List<ILabelBean>)customFieldTypeRT.getMatcherDataSource(
							matcherValue, matcherDatasourceContext, localParameterCode));
		}
		return matcherDatasourceLists;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new CompositeSelectBulkSetter(fieldID);
	}
	
	/**
	 * Loads the datasource for the mass operation
	 * used mainly by select fields to get 
	 * the all possible options for a field (system or custom select) 
	 * It also sets a value if not yet selected
	 * The value can be a List for simple select or a Map of lists for composite selects  
	 * @param massOperationContext
	 * @param massOperationValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public void loadBulkOperationDataSource(MassOperationContext massOperationContext,
			MassOperationValue massOperationValue,
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		Map<Integer, SortedMap<Integer, List<IBeanID>>> matcherDatasourceLists = new HashMap<Integer, SortedMap<Integer,List<IBeanID>>>();
		massOperationValue.setPossibleValues(matcherDatasourceLists);
		Map<Integer, Map<Integer, Integer[]>> actualValuesMap = null;
		try {
			actualValuesMap = (Map<Integer, Map<Integer, Integer[]>>)massOperationValue.getValue();
		} catch (Exception e) {
			LOGGER.warn("Mass operation datasource: converting the value to SortedMap failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}		
		if (actualValuesMap==null) {
			actualValuesMap = new TreeMap<Integer, Map<Integer, Integer[]>>();
			massOperationValue.setValue(actualValuesMap);
		}			
		for (int i=0; i<getNumberOfParts();i++) {
			ICustomFieldTypeRT customFieldTypeRT = getCustomFieldType(i+1);
			Integer localParameterCode = new Integer(i+1);
			Integer fieldID = massOperationValue.getFieldID();
			Map<Integer, Map<Integer, String>> fieldIDToListIDToLabels = massOperationContext.getFieldIDToListIDToLabels();
			if (fieldIDToListIDToLabels!=null) {
				Map<Integer, String> listIDToLabelMap = fieldIDToListIDToLabels.get(fieldID);
				if (listIDToLabelMap!=null) {
					for (Integer listID : listIDToLabelMap.keySet()) {
						//massOperationValueAndPossibleValues.setListID(listID);
						customFieldTypeRT.loadBulkOperationDataSource(massOperationContext,
							massOperationValue, localParameterCode, personBean, locale);
					}
				}
			}
		}
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new CompositeSelectFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new CompositeSelectFieldChangeApply(fieldID);
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	@Override
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new CompositeSelectSetterConverter(fieldID);
	}
	
	/**
	 * Loads the datasource and value for configuring the field change
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, 
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		SortedMap<Integer, List<IBeanID>> matcherDatasourceLists = new TreeMap<Integer,List<IBeanID>>();
		fieldChangeValue.setPossibleValues(matcherDatasourceLists);
		Map<Integer, Map<Integer, Integer[]>> actualValuesMap = null;
		try {
			actualValuesMap = (Map<Integer, Map<Integer, Integer[]>>)fieldChangeValue.getValue();
		} catch (Exception e) {
			LOGGER.warn("Mass operation datasource: converting the value to SortedMap failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}		
		if (actualValuesMap==null) {
			actualValuesMap = new TreeMap<Integer, Map<Integer, Integer[]>>();
			fieldChangeValue.setValue(actualValuesMap);
		}			
		for (int i=0; i<getNumberOfParts();i++) {
			ICustomFieldTypeRT customFieldTypeRT = getCustomFieldType(i+1);
			Integer localParameterCode = new Integer(i+1);		
			customFieldTypeRT.loadFieldChangeDatasourceAndValue(workflowContext,
					fieldChangeValue, localParameterCode, personBean, locale);	
		}
	}
	
	/**
	 * Design time matcher object for configuring the matcher
	 * It is the same for system and custom select fields
	 * But the runtime matchers and the matcher converter differ 
	 * for system and custom fields	
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new CompositeSelectMatcherDT(fieldID);
	}
	
	/**
	 * Design time matcher object for configuring the matcher
	 * It is the same for system and custom select fields
	 * But the runtime matchers and the matcher converter differ 
	 * for system and custom fields	
	 * @param fieldID 
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext replacementContext) {		
		return new CompositeSelectMatcherRT(fieldID, relation, matchValue, replacementContext);
	}
		
	@Override
	public MatcherConverter getMatcherConverter() {
		//IntegerMatcherConverter would suffice, the matcher value is probablby a single Integer 
		//(even if the actual workitem attribute value is an Integer array)
		//But unfortunately the matcher value type should be the same with the workItem attribute type 
		//because of the type conversion made in the showValue method to show the actualized matcher node in the tree
		return CompositSelectMatcherConverter.getInstance();
	}
	
	/**
	 * Whether the value have been modified
	 * Used by assembling the history and mail
	 * @param newValue
	 * @param oldValue
	 * @return
	 */
	@Override
	public boolean valueModified(Object newValue, Object oldValue) {
		Map<Integer, Object> newValuesMap = null;
		ICustomFieldTypeRT customFieldTypeRT;
		try {
			newValuesMap = (Map)newValue;
		} catch (Exception e) {
			LOGGER.warn("ValueModified: converting the new values to map failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		Map<Integer, Object> oldValuesMap = null;
		try {
			oldValuesMap = (Map)oldValue;
		} catch (Exception e) {
			LOGGER.warn("ValueModified: converting the old values to map failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		
		if (newValuesMap==null) {
			newValuesMap = new HashMap<Integer, Object>();
		}
		if (oldValuesMap==null) {
			oldValuesMap = new HashMap<Integer, Object>();
		}
		Object newSingleValue=null;
		Object oldSingleValue=null;
		for (int i=0; i<getNumberOfParts();i++) {
			customFieldTypeRT = getCustomFieldType(i+1);
			Integer parameterCode = new Integer(i+1);
			try {
				newSingleValue = newValuesMap.get(parameterCode);
			} catch (Exception e) {
				LOGGER.info("Getting the new value part " + i+1 + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			try {
				oldSingleValue = oldValuesMap.get(parameterCode);
			} catch (Exception e) {
				LOGGER.info("Getting the old value part " + i+1 + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (customFieldTypeRT.valueModified(newSingleValue, oldSingleValue)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the sort order related to the composite value
	 * @param fieldID
	 * @param parameterCode
	 * @param value the value, the sortorder is looked for
	 * @param workItemID
	 * @param localLookupContainer 
	 * @return
	 */
	@Override
	public Comparable getSortOrderValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer) {
		ICustomFieldTypeRT customFieldTypeRT = getCustomFieldType(1);
		//returns the sortorder of the first component only. 
		//TODO we may take into account also the other parts and build
		//a new Comparable object based on all the parts
		Map valueMap = null;
		try {
			valueMap = (Map)value;
		} catch (Exception e) {
			LOGGER.warn("SortOrderValue: converting the value to map failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		Map<Integer, Comparable> sortValuesMap = new HashMap<Integer, Comparable>();
		if (valueMap!=null && !valueMap.isEmpty()) {
			for (int i=0; i<getNumberOfParts();i++) {
				customFieldTypeRT = getCustomFieldType(i+1);
				if (customFieldTypeRT!=null) {
					Integer localParameterCode = new Integer(i+1);
					try {
						Object partValue = valueMap.get(localParameterCode);
						Comparable sortOrderValue = customFieldTypeRT.getSortOrderValue(
								fieldID, localParameterCode, partValue, workItemID, localLookupContainer);
						sortValuesMap.put(localParameterCode, sortOrderValue);
					} catch (Exception e) {
						LOGGER.info("Getting the sortOrderValue for part " + i+1 + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		return new CompositeComparable(sortValuesMap, getNumberOfParts());
	}
	
	/**
	 * Get the show value called when an item result set is implied
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale
	 * 
	 * @return
	 */
	@Override
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		ICustomFieldTypeRT customFieldTypeRT;
		StringBuffer showValue = new StringBuffer();
		Map objectMap = null;
		Object object;
		try {
			objectMap = (Map)value;
		} catch (Exception e) {
			LOGGER.warn("ShowValue dd: getting the map value for fieldID " + fieldID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}		
		if (objectMap!=null) {
			boolean atLeastOnePartNotEmpty = false;
			for (int i = 0; i < getNumberOfParts(); i++) {
				Integer localParameterCode = new Integer(i+1);
				customFieldTypeRT = getCustomFieldType(i+1);
				try {
					object = objectMap.get(localParameterCode);
				} catch (Exception e) {
					LOGGER.warn("ShowValue dd: getting object value for parameterCode " + i+1 + " of the field " + fieldID + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					object=null;
				}
				String partShowValue = customFieldTypeRT.getShowValue(
						fieldID, localParameterCode, object, workItemID, localLookupContainer, locale);
				if (partShowValue!=null && !"".equals(partShowValue)) {
					atLeastOnePartNotEmpty = true;
					showValue.append(partShowValue);
				}
				if (i<getNumberOfParts()-1) {
					showValue.append(PART_SPLITTER_STRING);
				}
			}
			if (atLeastOnePartNotEmpty) {
				return showValue.toString().trim();
			}
		}
		return "";
	}
	
	/**
	 * Get the value to be shown
	 * For text fields typically the field value itself
	 * For selects the (eventaully localized) label corresponding to the value 
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		ICustomFieldTypeRT customFieldTypeRT;
		StringBuffer showValue = new StringBuffer();
		Map objectMap = null;
		Object object;
		try {
			objectMap = (Map)value;
		} catch (Exception e) {
			LOGGER.warn("ShowValue: getting the map value  failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (objectMap!=null) {
			boolean atLeastOnePartNotEmpty = false;
			for (int i = 0; i < getNumberOfParts(); i++) {
				customFieldTypeRT = getCustomFieldType(i+1);
				try {
					object = objectMap.get(Integer.valueOf(i+1));
				} catch (Exception e) {
					LOGGER.warn("ShowValue: getting the value for parameterCode " + i+1 + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					object=null;
				}
				String partShowValue = customFieldTypeRT.getShowValue(object, locale);
				if (partShowValue!=null && !"".equals(partShowValue)) {
					atLeastOnePartNotEmpty = true;
					showValue.append(partShowValue);
				}
				if (i<getNumberOfParts()-1) {
					showValue.append(PART_SPLITTER_STRING);
				}
			}
			if (atLeastOnePartNotEmpty) {
				return showValue.toString().trim();
			}
		}
		return "";
	}
	
	/**
	 * Get the value to be shown for a matcher
	 * Typically same as for the getShowValue(), except the selects
	 * (the value object's type differs for matchers compared to issue field values in getShowValue)  
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getMatcherShowValue(Integer fieldID, Object value, Locale locale) {
		ICustomFieldTypeRT customFieldTypeRT;
		StringBuffer showValue = new StringBuffer();
		Map objectMap = null;
		Object object;
		try {
			objectMap = (Map)value;
		} catch (Exception e) {
			LOGGER.warn("MtcherShowValue: getting the map value  failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (objectMap!=null) {
			boolean atLeastOnePartNotEmpty = false;
			for (int i = 0; i < getNumberOfParts(); i++) {
				customFieldTypeRT = getCustomFieldType(i+1);
				try {
					object = objectMap.get(Integer.valueOf(i+1));
				} catch (Exception e) {
					LOGGER.warn("MtcherShowValue: getting the value for parameterCode " + i+1 + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					object=null;
				}
				String partShowValue = customFieldTypeRT.getMatcherShowValue(fieldID, object, locale);
				if (partShowValue!=null && !"".equals(partShowValue)) {
					atLeastOnePartNotEmpty = true;
					showValue.append(partShowValue);
				}
				if (i<getNumberOfParts()-1) {
					showValue.append(PART_SPLITTER_STRING);
				}
			}
			if (atLeastOnePartNotEmpty) {
				return showValue.toString().trim();
			}
		}
		return "";
	}
	
	/**
	 * Gets the map of system dependences:
	 *  - key: fieldID and paremeterCode combination
	 *  - value: list of system fieldIDs 
	 * @param fieldID
	 * @return
	 */
	/*@Override
	public Map getSystemDependences(Integer fieldID)
	{
		ICustomFieldTypeRT customFieldTypeRT;
		Map systemDependencesForPart;
		Map systemDependencesMap = new HashMap();
		for (int i = 0; i < getNumberOfParts(); i++) {
			//system dependences of the parts
			customFieldTypeRT = getCustomFieldType(i+1);
			systemDependencesForPart = customFieldTypeRT.getSystemDependences(fieldID);
			if (systemDependencesForPart!=null && systemDependencesForPart.get(MergeUtil.mergeKey(fieldID, null))!=null)
			{
				systemDependencesMap.put(MergeUtil.mergeKey(fieldID, new Integer(i+1)), systemDependencesForPart.get(MergeUtil.mergeKey(fieldID, null)));
			}
		}
		return systemDependencesMap;
	}*/
	
	/**
	 * Gets the map of internal dependences:
	 *  - key: fieldID and paremeterCode combination
	 *  - value: list of internal parameterCodes 
	 * @param fieldID
	 * @return
	 */
	/*@Override
	public Map getInternalDependences(Integer fieldID)
	{
		ICustomFieldTypeRT customFieldTypeRT;
		Map internalDependencesMapForPart;
		Map internalDependencesMap = new HashMap();
		for (int i = 0; i < getNumberOfParts(); i++) {
			//internal dependences of the parts inside the composite field
			customFieldTypeRT = getCustomFieldType(i+1);
			internalDependencesMapForPart = customFieldTypeRT.getInternalDependences(fieldID);
			if (internalDependencesMapForPart!=null && internalDependencesMapForPart.get(MergeUtil.mergeKey(fieldID, null))!=null)
			{
				internalDependencesMap.put(MergeUtil.mergeKey(fieldID, new Integer(i+1)), internalDependencesMapForPart.get(MergeUtil.mergeKey(fieldID, null)));
			}
 
		}
		return internalDependencesMap;
	}*/
	
	/**
	 * The custom composite itself has no value type
	 * (only his parts) 
	 */
	@Override
	public int getValueType()
	{
		return ValueType.NOSTORE;
	}
	
	/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	/*Not really relevant*/
	/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	/**
	 * Whether the field should be stored
	 * @return
	 */
	public int getLuceneStored() {
		return LuceneUtil.STORE.NO;
	}
	
	/**
	 * Whether the field should be tokenized
	 * @return
	 */
	@Override
	public int getLuceneTokenized() {
		return LuceneUtil.TOKENIZE.NO;
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.COMPOSITE;
	}
	
	/**
	 * Get the parameterCode list filtered by the preprocessType
	 * @param preprocessType
	 * @return
	 */
	public List<Integer> getPartsByPreprocessType(int preprocessType) {
		List<Integer> partsOfPreprocessType = new LinkedList<Integer>();
		for (int i = 0; i < getNumberOfParts(); i++) {
			IFieldTypeRT fieldTypeRT = getCustomFieldType(i+1);
			if (LuceneUtil.getPreprocessType(fieldTypeRT.getLookupEntityType())==preprocessType) {
				partsOfPreprocessType.add(new Integer(i+1));
			}
		}
		return partsOfPreprocessType;
	}
	/**
	 * Convert a value to string
	 * @param value
	 * @return
	 */
	@Override
	public String convertToString(Object value){
		StringBuffer stringValue = new StringBuffer();
		Map objectMap = null;
		try {
			objectMap = (Map)value;
		} catch (Exception e) {
			LOGGER.warn("stringValue: getting the map value  failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (objectMap!=null) {
			boolean atLeastOnePartNotEmpty = false;
			for (int i = 0; i < getNumberOfParts(); i++) {
				ICustomFieldTypeRT customFieldTypeRT = getCustomFieldType(i+1);
				Object object;
				try {
					object = objectMap.get(new Integer(i+1));
				} catch (Exception e) {
					LOGGER.warn("stringValue: getting the value for parameterCode " + i + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					object=null;
				}
				String partStringValue = customFieldTypeRT.convertToString(object);
				if (partStringValue!=null && !"".equals(partStringValue)) {
					atLeastOnePartNotEmpty = true;
					stringValue.append(partStringValue);
				}
				if (i<getNumberOfParts()-1) {
					stringValue.append(PART_SPLITTER_VALUES_STRING);
				}
			}
			if (atLeastOnePartNotEmpty) {
				return stringValue.toString().trim();
			}
		}
		return "";
	}

	/**
	 * Convert a string  to object value
	 * @param value
	 * @return
	 */
	@Override
	public Object convertFromString(String value){
		ICustomFieldTypeRT customFieldTypeRT;
		Map<Integer,Object> objectMap =null;
		if(value!=null&&value.length()>0){
			
			StringTokenizer st=new StringTokenizer(value,PART_SPLITTER_VALUES_STRING);
			objectMap=new HashMap<Integer,Object>();
			Object partObject;
			String valuePart=null;
			for (int i = 0; i < getNumberOfParts(); i++) {	
				customFieldTypeRT = getCustomFieldType(i+1);
				if(st.hasMoreTokens()){
					valuePart=st.nextToken();
				}else{
					valuePart=null;
				}
				partObject=customFieldTypeRT.convertFromString(valuePart);
				objectMap.put(new Integer(i+1), partObject);
			}
		}
		return objectMap;
	}
}
