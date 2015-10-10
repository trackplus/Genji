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


package com.aurel.track.fieldType.runtime.system.select;

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
import com.aurel.track.beans.ISortedBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.SystemSelectBulkSetter;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.SelectFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.SingleListFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.SystemSelectSetterConverter;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.base.SystemInputBaseRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ISelect;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MultipleSelectMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.SelectMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.matchers.run.SystemSelectMatcherRT;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.lucene.LuceneUtil;


public abstract class SystemSelectBaseRT extends SystemInputBaseRT implements ISelect {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemSelectBaseRT.class);
	
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * @return
	 */
	public int getValueType() {
		return ValueType.SYSTEMOPTION;
	}
	
	/**
	 * Whether the field is a lookup field (on list or tree based)
	 * @return
	 */
	public boolean isLookup() {
		return true;
	}
	
	/**
	 * Whether the datasource is tree or list
	 * @return
	 */
	public boolean isTree() {
		return false;
	}
	
	/**
	 * The field key for getting the corresponding map from dropdown container.
	 * The SystemSelects and the Pickers are stored under 
	 * the same key in the container (the key of the SystemSelect field)
	 * (for ex. manager, responsible, originator, changedBy and 
	 * user picker share the same person map)
	 * The CustomSelects are stored under their own fieldIDs   
	 * @return
	 */
	public Integer getDropDownMapFieldKey(Integer fieldID) {
		return getSystemOptionType();
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
		if(selectContext.isCreate()){
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
	 * Get the sort order related to the value
	 * By default the value is directly used as sortOrder
	 * the select fields has extra sortOrder columns 
	 * @param fieldID
	 * @param parameterCode
	 * @param value the value the sortorder is looked for
	 * @param workItemID
	 * @param localLookupContainer 
	 * @return
	 */
	public Comparable getSortOrderValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer) {
		Integer optionID = null;
		try	{
			optionID = (Integer)value;
		} catch (Exception e) {
			LOGGER.error("Converting the value " + value + " to integer failed with " + e.getMessage(), e);
		}
		if (optionID!=null)	{
			ISortedBean sortedBean = (ISortedBean)LookupContainer.getNotLocalizedLabelBean(getDropDownMapFieldKey(fieldID), optionID);	
			if (sortedBean!=null) {
				return sortedBean.getSortOrderValue();
			}
		}
		LOGGER.debug("The field number " + fieldID + " and parametercode "+ parameterCode + 
				" was not found in the dropdown container for getting the sort order value ");
		return super.getSortOrderValue(fieldID, parameterCode, value, workItemID, localLookupContainer);
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
		Integer optionID = null;
		if (value!=null) {
			try {
				optionID = (Integer)value;
			} catch (Exception e) {
				LOGGER.error("Casting the value type " + value.getClass().getName() +
						" to Integer in getShowValue() failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (optionID!=null) {
				ILabelBean labelBean = LookupContainer.getNotLocalizedLabelBean(getDropDownMapFieldKey(fieldID), optionID);
				if (labelBean!=null ) {
					return labelBean.getLabel();
				}
			}
		}
		//try from the database although quite superfluous when the dropdown container is loaded correcly
		LOGGER.debug("The field number " + fieldID + " and parametercode "+ parameterCode + 
				" was not found in the dropdown container for getting the show value ");
		return getShowValue(value, locale);
	}
	
	/**
	 * Gets the corresponding lookup bean map
	 * @param fieldID
	 * @param parameterCode
	 * @param locale
	 * @return
	 */
	/*Map<Integer, ILabelBean> getLookupBeanMap(Integer fieldID, Integer parameterCode, Locale locale) {
		return LookupContainer.getNotLocalizedLookupMap(getDropDownMapFieldKey(fieldID));
	}*/
	
	/**
	 * Get the value to be shown from the database 
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		ILabelBean labelBean =  getLabelBeanForShowValue(value, locale);
		if (labelBean!=null) {
			return labelBean.getLabel();
		}
		return "";
	}
	
	/**
	 * Gets the show value for a select
	 */
	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID){
		ILabelBean labelBean =  workItemContext.getILabelBean((Integer)value, fieldID);
		if (labelBean!=null) {
			return labelBean.getLabel();
		}
		return "";
	}
	
	/**
	 * Gets the label bean for show value
	 * @param value
	 * @param locale
	 * @return
	 */
	protected ILabelBean getLabelBeanForShowValue(Object value, Locale locale) {
		Integer optionID = null;
		if (value!=null) {
			try {
				optionID = (Integer)value;
				return getLabelBean(optionID, locale);
			} catch (Exception e) {
				LOGGER.warn("Casting the value type " + value.getClass().getName() +
						" to Integer in getLabelBeanForShowValue() failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
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
		Integer[] optionIDs = null;
		if (value!=null) {
			try {
				optionIDs = (Integer[])value;
			} catch (Exception e) {
				LOGGER.warn("Casting the value type " + value.getClass().getName() +
						" to Integer[] failed with " + e.getMessage(), e);
				return "";
			}
			return LookupContainer.getNotLocalizedLookupCommaSepatatedString(fieldID, optionIDs, locale);
		}
		return "";
		/*ILabelBean labelBean = getLabelBeanForMatcherShowValue(value, locale);
		if (labelBean!=null) {
			return labelBean.getLabel();
		}
		return "";*/
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
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	public abstract ILabelBean getLabelBean(Integer optionID, Locale locale);
	
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
		return new SystemSelectMatcherRT(fieldID, relation, matchValue, replacementContext);
	}
	
	@Override
	public MatcherConverter getMatcherConverter() {
		//return SelectMatcherConverter.getInstance();
		return  MultipleSelectMatcherConverter.getInstance();
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new SystemSelectBulkSetter(fieldID, hasDynamicIcons());
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
	protected Integer getBulkSelectValue(MassOperationContext massOperationContext, 
			Integer fieldID, Integer paramaterCode, Integer actualValue, List<IBeanID> possibleValues) {
		if (possibleValues==null || possibleValues.isEmpty()) {
			return null;
		}
		if (actualValue==null&&massOperationContext!=null) {
			TWorkItemBean firstWorkItemBean = massOperationContext.getFirstSelectedWorkItemBean();
			if (firstWorkItemBean!=null) {
				try {
					actualValue = (Integer)firstWorkItemBean.getAttribute(fieldID, paramaterCode);			
				} catch (Exception e) {
				}
			}
		}
		return getValidValues(actualValue, possibleValues);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new SingleListFieldChangeConfig(fieldID, hasDynamicIcons(), false);
	}
	
	/**
	 * Whether the list entries have dynamic icons
	 * @return
	 */
	public boolean hasDynamicIcons() {
		return false;
	}
	
	/**
	 * Gets the iconCls class for label bean if dynamicIcons is false
	 * @param labelBean
	 */
	public String getIconCls(ILabelBean labelBean) {
		return null;
	}
	
	/**
	 * Gets the IFieldChangeApply object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new SelectFieldChangeApply(fieldID, false);
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new SystemSelectSetterConverter(fieldID);
	}
	
	/**
	 * Verify that the each value is part of the datasource
	 * @param actualValue
	 * @param possibleValues
	 * @return
	 */
	private static Integer getValidValues(Integer actualValue, List<IBeanID> possibleValues) {
		if (actualValue!=null) {
			for (IBeanID labelBean : possibleValues) {
				if (actualValue.equals(labelBean.getObjectID())) {
					return actualValue;
				}
			}
		}
		return Integer.valueOf(possibleValues.get(0).getObjectID());
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
		return LuceneUtil.TOKENIZE.NO;
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @param serializableLabelBean
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
	 * Whether the lookupID found by label is allowed in 
	 * the context of serializableBeanAllowedContext
	 * In excel the lookup entries are not limited by the user interface controls
	 * This method should return false if the lookupID
	 * is not allowed (for ex. a person without manager role was set as manager) 
	 * @param objectID
	 * @param serializableBeanAllowedContext
	 * @return
	 */
	public boolean lookupBeanAllowed(Integer objectID, 
			SerializableBeanAllowedContext serializableBeanAllowedContext) {
		return true;
	}
	
	/**
	 * Parse the group value
	 * @param value
	 * @return
	 */
	public Object getParseGroupValue(String value) {
		if (value!=null) {
			try {
				return Integer.valueOf(value);
			} catch(Exception e) {
				LOGGER.warn("The string value for group can't be converted to Integer " + e.getMessage(), e);
			}
		}
		return null;
	}

}
