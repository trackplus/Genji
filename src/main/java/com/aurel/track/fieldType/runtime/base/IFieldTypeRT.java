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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILucene;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;


/**
 * Base interface for all fields (system and custom) 
 * defining their runtime behavior
 * @author Tamas Ruff
 *
 */
public interface IFieldTypeRT extends ILucene {
	
	/**
	 * Loads or calculates the custom attribute value from the map of attributeValueBeans 
	 * into the workItemBean's customAttributeValues map  
	 * @param fieldID 
	 * @param parameterCode null for single custom fields
	 * @param workItemBean
	 * @param attributeValueMap:	
	 * 			- key: fieldID_parameterCode
	 * 			- value: attributeValueBean or list of attributeValueBeans (typically for multiple selects)
	 */
	public void processLoad(Integer fieldID, Integer parameterCode,
			TWorkItemBean workItemBean, Map<String, Object> attributeValueMap);
	
	/**
	 * Saves a custom attribute to the database
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	public void processSave(Integer fieldID, Integer parameterCode,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal);
	
	/**
	 * Preprocess a custom attribute before save:
	 * implemented when some extra save is needed for ex. by extensible select
	 * or a system field should be calculated before save (wbs)
	 * @param fieldID
	 * @param parameterCode
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param contextInformation
	 */
	public void processBeforeSave(Integer fieldID, Integer parameterCode, Integer validConfig, Map<String, Object> fieldSettings,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Map<String, Object> contextInformation);
	
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
	public void processHistoryLoad(Integer fieldID, Integer parameterCode, 
			Map<String, Object> fieldChangeMap, Map<String, Object> newValuesHistoryMap, Map<String, Object> oldValuesHistoryMap);
	
	/**
	 * Whether bottom up computing is activated for field
	 * @param fieldID
	 * @param fieldConfigBean
	 * @param fieldSettings
	 * @return true if bottom up computing is activated
	 */
	public int getHierarchicalBehavior(Integer fieldID, TFieldConfigBean fieldConfigBean, Object fieldSettings);
	
	/**
	 * Sets the parent value derived from child value (bottom up computing)
	 * @param parentWorkItem
	 * @param fieldID
	 * @param childValueNew
	 * @param childValueOriginal
	 * @param fieldConfigBean
	 * @param fieldSettings
	 * @return true if parent value was really modified
	 */
	public boolean setBottomUpParentValue(TWorkItemBean parentWorkItem, Integer fieldID,
			Object childValueNew, Object childValueOriginal, TFieldConfigBean fieldConfigBean, Object fieldSettings);
	
	/**
	 * Saves the field changes explicitly into the history   
	 * @param fieldID
	 * @param parameterCode neglected for single fields
	 * @param historyTransactionID
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param fieldChangeBean the existing TFieldChange entry (for editing/deleting comments, and updating history changes in the last x minutes)
	 */
	public void processHistorySave(Integer fieldID, Integer parameterCode, Integer historyTransactionID,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, TFieldChangeBean fieldChangeBean);
		
	/** 
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * @return
	 */
	public int getValueType();
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	public Integer getSystemOptionType();
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	public boolean isReadOnly();
	
	/**
	 * Defines whether there could be more values for the same parameterCode
	 * returns true typically for (multiple) selects 
	 * @return
	 */
	public boolean isMultipleValues();
	
	/**
	 * Whether the field is a custom field
	 * @return
	 */
	public boolean isCustom();
	
	/**
	 * Whether the field is a lookup field (on list or tree based)
	 * @return
	 */
	public boolean isLookup();
	
	/**
	 * Whether the field is composite
	 * @return
	 */
	public boolean isComposite();
	
	/**
	 * Whether this field is a calculated field
	 * If calculated and dependent from other fields then changing 
	 * the dependee should trigger the recalculation of this field  
	 * @param fieldID
	 * @param parameterCode
	 */
	
	/**
	 * Whether this field is a custom label field
	 * @param fieldID
	 * @param parameterCode
	 */
	public boolean isComputed(Integer fieldID, Integer parameterCode);

	/**
	 * Whether the value have been modified
	 * Used by assembling the history and mail
	 * @param newValue
	 * @param oldValue
	 * @return
	 */
	public boolean valueModified(Object newValue, Object oldValue);
	
	
	/**
	 * Gets the context with actual item data for injecting content into the labels
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	public Map<String, Object> getLabelContext(Integer fieldID, Object value, Locale locale);
	
	/**
	 * Loads a default attribute for a new issue
	 * @param fieldID
	 * @param parameterCode
	 * @param validConfigID
	 * @param fieldSettings
	 * @param workItemBean
	 */
	public void processDefaultValue(Integer fieldID, Integer parameterCode, 
			Integer validConfigID, Map<String, Object> fieldSettings, TWorkItemBean workItemBean);
	
	
	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field 
	 * by editing an existing issue/creating a new issue/refreshing a composite part.
 	 * The result might differ depending on create flag because it might be needed 
	 * to add the current value from the workItem explicitely to the list entries 
	 * when the right was revoked for the current value.
	 * If the parameterCode of the selectContext is not null then the field is a part of a composite field
	 * and probably a refresh took place (not edit/create). In this case only the datasource of the 
	 * composite part specified by paramaterCode will be reloaded, not for each part of the composite 
	 * @param selectContext
	 * @param dropDownContainer 
	 * @return
	 */
	public void processLoadDataSource(SelectContext selectContext, DropDownContainer dropDownContainer);
	
	/**
	 * Validates an attribute for a field 
	 * and returns the resource key(s) in case of validation error
	 * @param fieldID TODO
	 * @param fieldConfigBean
     * @param parameterCode
	 * @param settingsObject should be either TTextBoxSettingsBean,
	 * 						TOptionSettingsBean or TGeneralSettingsBean
	 * @param workItemBean the value to validate
	 * @return
	 */
	public Map<Integer, List<Validator>> procesLoadValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean, Integer parameterCode, 
			Object settingsObject, TWorkItemBean workItemBean);
	
	/**
	 * Loads the matcher object for configuring the matcher
	 * @param fieldID
	 */
	public IMatcherDT processLoadMatcherDT(Integer fieldID);
	
	
	/**
	 * Loads the  matcher object for executing the matcher
	 * fieldID would suffice as parameter for loading the specific Matcher, 
	 * while the other parameters could be set after the matcher is loaded.
	 * But we need the matchValue parameter in the getMatcher() already set
	 * in case of replacement parameters, to replace the symbolic value with the actual value
	 * The symbolic and replacement values will be stored in the matcherContext
	 * (ex. $USER for the current user as originator/manager/responsible:
	 * the symbolic value for $USER should be replaced 
	 * with the value of the user currently logged in)
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 * @param matcherContext
	 */
	public IMatcherRT processLoadMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext);

	/**
	 * Get the converter used for matcher
	 * @return
	 */
	public MatcherConverter getMatcherConverter();

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
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode);
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	public IBulkSetter getBulkSetterDT(Integer fieldID);
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID);
	
	/**
	 * Gets the IActivityExecute object for executing the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID);
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	public IValueConverter getFieldValueConverter(Integer fieldID);
	
	/**
	 * Loads the datasource for the bulk operation
	 * used mainly by select fields to get 
	 * the all possible options for a field (system or custom select) 
	 * It also sets a value if not yet selected (Important by composite selects where the
	 * child should have access to the parent's value at any time)
	 * The value can be a List for simple select or a Map of lists for composite selects
	 * (this method was preferable to be part of the IBulkSetter interface but for composite parts
	 * the structural fields are needed. See CustomDependentSelect parentID childNumber) 
	 * @param massOperationContext
	 * @param massOperationValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	public void loadBulkOperationDataSource(MassOperationContext massOperationContext,
			MassOperationValue massOperationValue,
			Integer parameterCode, TPersonBean personBean, Locale locale);
	
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
			Integer parameterCode, TPersonBean personBean, Locale locale);
	
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
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale);
	
	/**
	 * Get the ISO show value for locale independent exporting to xml
	 * typically same as show value, date and number values are formatted by iso format 
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale lookups should be localized also in ISO format
	 * @return
	 */
	public String getShowISOValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale);
	
	/**
	 * Get the show value when a single item is implied
	 * @param value
	 * @param locale
	 * @return
	 */
	public String getShowValue(Object value, Locale locale);
	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID);
	
	/**
	 * Get the value to be shown for a matcher
	 * Typically same as for the getShowValue(), except the selects
	 * (the value object's type differs for matchers compared to issue field values in getShowValue)
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	public String getMatcherShowValue(Integer fieldID, Object value, Locale locale);
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue: typically a single value but for multiple selects is an Object[]
	 * @return
	 */
	public Object parseISOValue(Object isoStrValue);
	
	/**
	 * Returns whether the iso show value differs from 
	 * the locale specific show value for this field type.
	 * This is needed to avoid loading the iso values 
	 * by repeating the load for locale specific show value
	 * (which takes surprisingly long time)   
	 * @return
	 */
	public boolean isoDiffersFromLocaleSpecific();

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
			Integer workItemID, LocalLookupContainer localLookupContainer);
	
	/**
	 * Get the map of system dependences
	 * - key: a String of fieldID and parameterCode combination (parameterCode is null for system-, or custom single fields)
	 * (see CustomCompositeFieldTypeRT.getInternalDependences() and FieldsManagerRT.getDependences() 
	 * 	why do we need also the parameterCode as part of the key even if it is null)
	 * For composite parts it would be enough to set only the fieldID as key because the paremeterCode will be synthetized 
	 * only by CustomCompositeFieldTypeRT.getInternalDependences() but for single custom fields the key should be
	 * prepared with the fieldID and parameterCode combination 
	 * (even if parameterCode is null, the format of the key should be in the expected format).
	 * - value: List of system fieldsIDs as Integers which defines the system dependencies for this part
	 * Important: The dependences on project and issueType are hardcoded so only
	 * the other system dependences should be specified 
	 * @return
	 */
	
	/**
	 * Get the map of internal dependences 
	 * - key: a String of fieldID and parameterCode (of the dependent part) combination.
	 * (see CustomCompositeFieldTypeRT.getInternalDependences() and FieldsManagerRT.getDependences() 
	 * 	why do we need also the parameterCode as part of the key)  
	 * For composite parts it would be enough to set only the fieldID as key because the paremeterCode will be synthetized 
	 * only by CustomCompositeFieldTypeRT.getInternalDependences() but for single custom fields the key should be
	 * prepared with the fieldID and parameterCode combination 
	 * (even if parameterCode is null, the format of the key should be in the expected format).
	 * - value: List of parameterCodes as Integers which defines the internal dependencies 
	 * of the composite part specified in key's parameterCode
	 * i.e. the indexes inside the composite this part depends on
	 * Important: This makes sense only for parts of a custom composite field and for the custom composite itself 
	 * Do not override for system or custom single fields 
	 * @return
	 */
			
	/**
	 * Whether this field is a UserPicker
	 * Special care should be taken by user pickers because of the notification (automail)
	 * A person may receive an automail just because he/she is selected in a userPicker 
	 * @return
	 */
	public boolean isUserPicker();
	
	/**
	 * Whether this field is a long text because then the read only
	 * rendering (on the report overview, printItem, etc.)
	 * should be made different as the rest of the "short" fields
	 * This can be the case for Description, Comment and longtext custom fields  
	 * @return
	 */
	public boolean isLong();
	
	/**
	 * Whether this field is a date field by rendering it after exporting to xml
	 * Exporting it to xml should be made in a locale independent way but by creating 
	 * the document using the jasper report it should be specified wheteher a field is date or not
	 * to be interpreted correctly according to the reports's locale  
	 * @return
	 */
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable
	 * @return
	 */
	public boolean isGroupable();
	
	/**
	 * Gets the raw group value as a string
	 * @param value
	 * @return
	 */
	public String getStringGroupValue(Object value);
	
	/**
	 * Parse the group value
	 * @param value
	 * @return
	 */
	public Object getParseGroupValue(String value);
	
	/**
	 * Gets the label for the group. It is typically the same
	 * as the showValue() of the workItems from the same group,
	 * but it will be called only for fields for which groupLabelDiffersFromShowValue() returns true
	 * (For create/last modification date it should differ from the show showValue() 
	 * because the time part of the date should be removed from the label for group)
	 * @return
	 */
	public String getGroupLabel(Object value, Locale locale);
	
	/**
	 * Whether the group label differs from the show value of the workItems from the same group
	 * It is needed to differentiate because for getting the label for group either  
	 * the getGroupLabel() will be called or the reportBean.getShowValue(fieldID) method   
	 * (not the fieldTypeRT's getShowValue(Object value, Locale locale) which is the  
	 * default implementation for getGroupLabel() because the reportBean
	 * contains the showValue already, and calling the getShowValue(Object value, Locale locale)
	 * might result in unnecessary database access: see. SystemSelectBase getLabelBean())
	 * @return
	 */
	public boolean groupLabelDiffersFromShowValue();
	
	/**
	 * Whether the field can be rendered on a form
	 * @return
	 */
	public boolean mightAppearOnForm();
	
	/**
	 * Whether the field can be matched in for an excel column
	 * @return
	 */
	public boolean mightMatchExcelColumn();
}
