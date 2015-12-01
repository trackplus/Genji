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


package com.aurel.track.fieldType.runtime.custom.select;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.IBeanID;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.CustomMultipleSelectBulkSetter;
import com.aurel.track.fieldType.bulkSetters.CustomSingleSelectBulkSetter;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.fieldChange.apply.CustomMultipleSelectFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.apply.SelectFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.MultipleListFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.config.SingleListFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.MultipleSelectSetterConverter;
import com.aurel.track.fieldType.runtime.base.CustomOnePieceBaseRT;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.base.ICustomFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ISelect;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MultipleSelectMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.SelectMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.CompositeSelectMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.CustomSelectMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Base class for custom selects and pickers
 * @author Tamas Ruff
 *
 */
public abstract class CustomSelectBaseRT extends CustomOnePieceBaseRT 
	implements ICustomFieldTypeRT, ISelect {
	
	private static final Logger LOGGER = LogManager.getLogger(CustomSelectBaseRT.class);
	//changed on ";" from "," for multiple user picker: "," splits the first name and last name
	public static String OPTION_SPLITTER_STRING = "; ";//", ";
	public static String OPTION_SPLITTER_VALUES_STRING = ";";//",";
	
	/**
	 * Whether the field is a lookup field (on list or tree based)
	 * @return
	 */
	public boolean isLookup() {
		return true;
	}
	
	/**
	 * Whether it is a selection field
	 * @return
	 */
	public boolean isCustomSelect() {
		return true;
	}
	
	/**
	 * Whether the values are based on custom lists or system lists
	 * @return
	 */
	public abstract boolean isCustomPicker();
	
	/**
	 * Defines whether there could be more values for the same parameterCode
	 * returns true typically for (multiple) selects
	 * True for both single and multiple selects (Object[] for both)
	 * Used in the persistence code
	 * @return
	 */
	@Override
	public boolean isMultipleValues() {
		return true;
	}
	
	protected boolean isReallyMultiple = false;
		
	
	public void setReallyMultiple(boolean isReallyMultiple) {
		this.isReallyMultiple = isReallyMultiple;
	}


	public boolean isReallyMultiple() {
		return isReallyMultiple;
	}

	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field 
	 * by editing an existing issue/creating a new issue/refreshing a composite part.
	 * The result might differ depending on create flag because it might be needed 
	 * to add the current value from the workItem explicitly to the list entries 
	 * when the right was revoked for the current value.
	 * If the parameterCode of the selectContext is not null then the field is is a part of a composite field
	 * and probably a refresh took place (not edit/create). In this case only the datasource of the 
	 * composite part specified by paramaterCode will be reloaded, not for each part of the composite 
	 * @param selectContext
	 * @param dropDownContainer 
	 * @return
	 */
	@Override
	public void processLoadDataSource(SelectContext selectContext, DropDownContainer dropDownContainer){
		List dataSource = null;
		if(selectContext.isCreate() || selectContext.isDependencyRefresh()){
			dataSource=loadCreateDataSource(selectContext);
		}else{
			dataSource=loadEditDataSource(selectContext);
		}
		if (isTree()) {
			dropDownContainer.setDataSourceTree(MergeUtil.mergeKey(selectContext.getFieldID(), selectContext.getParameterCode()),dataSource);
		} else {
			dropDownContainer.setDataSourceList(MergeUtil.mergeKey(selectContext.getFieldID(), selectContext.getParameterCode()),dataSource);
		}
	}
	
	/**
	 * Get the show value called when an item result set is implied
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale 
	 * @return
	 */
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		StringBuffer showValue = new StringBuffer();
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the show value");
			return showValue.toString();
		}
		//try from the dropdown map
		if (localLookupContainer!=null) {
			Map<Integer, ILabelBean> beansMap = localLookupContainer.getCustomOptionsMap();
			if (beansMap!=null) {
				for (int i = 0; i < optionIDs.length; i++) {
					ILabelBean labelBean = beansMap.get(optionIDs[i]);
					if (labelBean!=null) {
						showValue.append(labelBean.getLabel());
                        if (i<optionIDs.length-1) {
                            showValue.append(OPTION_SPLITTER_STRING);
                        }
					}
				}
			}
			String showValueString = showValue.toString();
			if (!"".equals(showValueString)) {
				return showValueString.trim(); 
			}
			//try from the database although quite superfluous when the dropdown container is loaded correctly
			LOGGER.debug("Getting the show value from database instead of droprdown container");
		}
		return getShowValue(optionIDs, locale);
	}
	
	/**
	 * Get the value to be shown for a matcher
	 * Typically same as for the getShowValue(), except the selects
	 * (the value object's type differs for matchers compared to issue field values in getShowValue)  
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getMatcherShowValue(Integer fieldID, Object value, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		if (value!=null) {
			Integer[] optionIDs = null;
			try {
				optionIDs = (Integer[])value;
			} catch (Exception e) {
				LOGGER.warn("Casting the value type " + value.getClass().getName() +
						" to Integer[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return "";
			}
			if (optionIDs!=null && optionIDs.length>0) {
				for (int i = 0; i < optionIDs.length; i++) {
					Integer objectID = optionIDs[i];
					if (objectID!=null) {
						String label = null;
						if (MatcherContext.PARAMETER.equals(objectID)) {
							label= MatcherContext.getLocalizedParameter(locale);
						} else {
							if (CompositeSelectMatcherRT.ANY_FROM_LEVEL.equals(objectID)) {
								label = LocalizeUtil.getLocalizedTextFromApplicationResources(
										"admin.customize.queryFilter.opt.partialMatch", locale);
							} else {
								if (CompositeSelectMatcherRT.NONE_FROM_LEVEL.equals(objectID)) {
									label = LocalizeUtil.getLocalizedTextFromApplicationResources(
											"admin.customize.queryFilter.opt.partialNotmatch", locale);
								} else {
									ILabelBean labelBean = getLabelBean(objectID, locale);
									if (labelBean!=null) {
										label = labelBean.getLabel();
									}
								}
							}
						}
						stringBuilder.append(label);
						if (i<optionIDs.length-1) {
							stringBuilder.append(",");
						}
					}
				}
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the raw string value
	 * @param value
	 * @return
	 */
	public String getStringGroupValue(Object value) {
		if (value!=null) {
			Object[] values = null;
			try {
				values = (Object[])value;
			} catch (Exception e) {
				LOGGER.warn("The type of the attributeValue source is " + 
						value.getClass().getName() + ". Casting it to Object[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (values!=null && values.length>0) {
				Object firstValue = values[0];
				if (firstValue!=null) {
					return firstValue.toString();
				}
			}
		}
		return "";
	}
	
	/**
	 * Parse the group value
	 * @param value
	 * @return
	 */
	public Object getParseGroupValue(String value) {
		return new Object[] {Integer.valueOf(value)};
	}
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null && !"".equals(isoStrValue)) {
			return new Integer(isoStrValue.toString());
		}
		return null; 
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
		Integer[] arrNewValues = CustomSelectUtil.getSelectedOptions(newValue);
		Integer[] arrOldValues = CustomSelectUtil.getSelectedOptions(oldValue);
		if (arrNewValues==null && arrOldValues==null) {
			return false;
		}
		if (arrNewValues==null || arrOldValues==null) {
			return true; 
		}		
		if (arrNewValues.length!=arrOldValues.length) {
			return true;
		}
		for (int i = 0; i < arrNewValues.length; i++) {
			if (!arrNewValues[i].equals(arrOldValues[i])) {
				return true;
			}
		}
		return false;
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
		return new SelectMatcherDT(fieldID);
	}
	
	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext replacementContext){		
		return new CustomSelectMatcherRT(fieldID, relation, matchValue, replacementContext);
	}		
	
	@Override
	public MatcherConverter getMatcherConverter() {
		//IntegerMatcherConverter would suffice, the matcher value is probably a single Integer 
		//(even if the actual item attribute value is an Integer array)
		//But unfortunately the matcher value type should be the same with the workItem attribute type 
		//because of the type conversion made in the showValue method to show the actualized matcher node in the tree
		//return IntegerArrayMatcherConverter.getInstance();
		//return SelectMatcherConverter.getInstance();
		return  MultipleSelectMatcherConverter.getInstance();
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		if (isReallyMultiple) {
			return new CustomMultipleSelectBulkSetter(fieldID);
		} else {
			return new CustomSingleSelectBulkSetter(fieldID);
		}
	}
	
	/**
	 * Gets the value for a select field by best effort:
	 * If not yet set sets the select value to the actual value 
	 * of the first selected workItemBean's corresponding field value	 
	 * (Avoid setting the value automatically to the first one)
	 * If already set verify whether the previous value is still valid
	 * according to the new possible values list 
	 * @param massOperationContext
	 * @param fieldID
	 * @param paramaterCode
	 * @param actualValue
	 * @param possibleValues
	 * @return
	 */
	protected Integer[] getBulkSelectValue(MassOperationContext massOperationContext, 
			Integer fieldID, Integer paramaterCode, Integer[] actualValue, List<IBeanID> possibleValues) {
		if ((actualValue==null || actualValue.length==0) && possibleValues!=null && !possibleValues.isEmpty()) {
			if (isReallyMultiple) {
				//do not preselect anything if multiple select
				return null;
			}
			Integer[] actualValueForFirstWorkItem = null;
			if (massOperationContext!=null) {
				TWorkItemBean firstWorkItemBean = massOperationContext.getFirstSelectedWorkItemBean();
				if (firstWorkItemBean!=null) {
					try {
						actualValueForFirstWorkItem = (Integer[])firstWorkItemBean.getAttribute(fieldID, paramaterCode);			
					} catch (Exception e) {
					}
				}
			}
			return getValidValues(actualValueForFirstWorkItem, possibleValues, !isReallyMultiple);
		} else {
			return getValidValues(actualValue, possibleValues, false);
		}
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		if (isReallyMultiple) {
			return new MultipleListFieldChangeConfig(fieldID, true, true);
		} else {
			return new SingleListFieldChangeConfig(fieldID, true, true);
		}
	}
	
	/**
	 * Gets the IFieldChangeApply object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		if (isReallyMultiple) {
			return new CustomMultipleSelectFieldChangeApply(fieldID);
		} else {
			return new SelectFieldChangeApply(fieldID, true);
		}
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		//IntegerSetterConverter would suffice, the setter value is probably a single Integer 
		//(even if the actual item attribute value is an Integer array)
		//but the setter value type should be the same with the workItem attribute type because getFieldChangeApply
		return new MultipleSelectSetterConverter(fieldID);
	}
	
	/**
	 * Verify that the each value is part of the datasource
	 * @param actualValue
	 * @param possibleValues
	 * @param forcePreselect
	 * @return
	 */
	protected static Integer[] getValidValues(Integer[] actualValue, List<IBeanID> possibleValues, boolean forcePreselect) {
		if (possibleValues==null || possibleValues.isEmpty()) {
			return null;
		}
		List<Integer> validValues = new LinkedList<Integer>();
		if (actualValue!=null && actualValue.length>0) {
			for (int i = 0; i < actualValue.length; i++) {
				Integer intValue = actualValue[i];
				if (intValue!=null) {
					for (IBeanID labelBean : possibleValues) {
						if (intValue.equals(labelBean.getObjectID())) {
							validValues.add(intValue);
							break;
						}
					}
				}
			}
		}
		if (validValues.isEmpty() && forcePreselect) {
			//get the first from the list
			return new Integer[] {possibleValues.get(0).getObjectID()};
		} else {
			return GeneralUtils.createIntegerArrFromCollection(validValues);
		}
	}
	
	/**
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	public abstract ILabelBean getLabelBean(Integer optionID, Locale locale);
	
	/**
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		if (value!=null) {
			Integer[] intArrValue = CustomSelectUtil.getSelectedOptions(value);
			if (intArrValue!=null && intArrValue.length>0) {
				StringBuffer stringBuffer = new StringBuffer();
				for (int i = 0; i < intArrValue.length; i++) {
					if (intArrValue[i]!=null) {
						stringBuffer.append(intArrValue[i].toString() + " ");
					}
				}
				return stringBuffer.toString().trim();
			}
		}
		return null;
	}
	
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
	public int getLuceneTokenized() {
		//important mainly for multiple selects
		return LuceneUtil.TOKENIZE.YES;
	}
	
	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public abstract int getLookupEntityType();
		
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean(ISerializableLabelBean serializableLabelBean) {
		return serializableLabelBean.serializeBean();
	}
		
	
	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		return getNewSerializableLabelBean().deserializeBean(attributes);
	}
	/**
	 * Convert a value to string
	 * @param value
	 * @return
	 */
	@Override
	public String convertToString(Object value){
		StringBuffer stringValue = new StringBuffer();
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the show value");
		}else{
			for (int i = 0; i < optionIDs.length; i++) {
				stringValue.append(optionIDs[i]);
				if (i<optionIDs.length-1) {
					stringValue.append(OPTION_SPLITTER_VALUES_STRING); 
				}
			}
		}
		return stringValue.toString().trim();
	}

	/**
	 * Convert a string  to object value
	 * @param value
	 * @return
	 */
	@Override
	public Object convertFromString(String value){
		Object[] result=null;
		if(value!=null){
			String[] tokens=value.split(OPTION_SPLITTER_VALUES_STRING);
			if(tokens!=null&&tokens.length>0){
				List<Integer> valueList=new ArrayList<Integer>(tokens.length);
				Integer intValue;
				for (int i = 0; i < tokens.length; i++) {
					try{
						intValue=Integer.decode(tokens[i]);
						valueList.add(intValue);
					}catch (Exception e) {
						//ignore
					}
				}
				LOGGER.debug("convertfromString :"+valueList);
				result=new Object[valueList.size()];
				valueList.toArray(result);
			}
		}
		return result;
	}
}
