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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.fieldChange.converter.StringSetterConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.StringMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.validators.Validator;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxDate.HIERARCHICAL_BEHAVIOR_OPTIONS;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;

/**
 * The base class for runtime field types with 
 * typical default implementations
 * @author Tamas Ruff
 *
 */
public abstract class AbstractFieldTypeRT implements IFieldTypeRT {
	private static final Logger LOGGER = LogManager.getLogger(AbstractFieldTypeRT.class);
	/**
	 * Saves a custom attribute to the database
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	@Override
	public void processSave(Integer fieldID, Integer parameterCode, 
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal){
	}
	
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
	@Override
	public void processBeforeSave(Integer fieldID, Integer parameterCode, Integer validConfig, Map<String, Object> fieldSettings,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Map<String, Object> contextInformation) {
	}
	
	/**
	 * Loads the field change attributes from the the history
	 * Not needed for calculated fields and those fields which 
	 * are not registered in the history: issueNo, Originator, LastEditedBy, CreatedAt, LastEdit  
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
			Map<String, Object> fieldChangeMap, Map<String, Object> newValuesHistoryMap, Map<String, Object> oldValuesHistoryMap) {
		
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
		
	}
	
	/**
	 * Whether bottom up computing is activated for field
	 * @param fieldID
	 * @param fieldConfigBean
	 * @param fieldSettings
	 * @return true if bottom up computing is activated
	 */
	@Override
	public int getHierarchicalBehavior(Integer fieldID, TFieldConfigBean fieldConfigBean, Object fieldSettings) {
		return HIERARCHICAL_BEHAVIOR_OPTIONS.NO_RESTRICTION;
	}
	
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
	@Override
	public boolean setBottomUpParentValue(TWorkItemBean parentWorkItem, Integer fieldID,
			Object childValueNew, Object childValueOriginal, TFieldConfigBean fieldConfigBean, Object fieldSettings) {
		return false;
	}
	
	/**
	 * Whether the field is a lookup field (on list or tree based)
	 * @return
	 */
	@Override
	public boolean isLookup() {
		return false;
	}
	
	/**
	 * Whether the field is a custom field
	 * @return
	 */
	@Override
	public boolean isCustom() {
		return false;
	}
	
	/**
	 * Defines whether there could be more values for the same parameterCode
	 * returns true typically for (multiple) selects 
	 * @return
	 */
	@Override
	public boolean isMultipleValues() {
		return false;
	}
	
	/**
	 * Whether the field is composite
	 * @return
	 */
	@Override
	public boolean isComposite() {
		return false;
	}
	
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return null;
	}
	
	/**
	 * Whether the value of this field can be changed
	 * @return
	 */
	@Override
	public boolean isReadOnly() {
		return false;
	}
	
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
	@Override
	public boolean isComputed(Integer fieldID, Integer parameterCode) {
		return false;
	}
	
	/**
	 * Whether the value have been modified
	 * Considered by assembling the history and mail
	 * @param newValue
	 * @param oldValue
	 * @return
	 */
	@Override
	public boolean valueModified(Object newValue, Object oldValue) {
		//works only for basic types: it should be overrided 
		//especially in composite fields, because each part should be compared
		if (newValue==null) {
			newValue="";
		}
		if (oldValue==null) {
			oldValue="";
		}
		return !newValue.equals(oldValue);
	}
	
	/**
	 * Gets the context with actual item data for injecting content into the labels
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, Object> getLabelContext(Integer fieldID, Object value, Locale locale) {
		return null;
	}
	
	/**
	 * Loads a default attribute for a new issue
	 * @param fieldID
	 * @param parameterCode
	 * @param validConfig
	 * @param fieldSettings
	 * @param workItemBean
	 */
	@Override
	public void processDefaultValue(Integer fieldID, Integer parameterCode, 
			Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean){
	}
	
	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field 
	 * by editing an existing issue or creating a new issue.
	 * The result might differ from loadCreateDataSource because it might be needed 
	 * to add the current value from the workItem to the list entries 
	 * (when right already revoked for the current value).
	 * @param selectContext
	 * @param dropDownContainer 
	 * @return
	 */
	@Override
	public void processLoadDataSource(SelectContext selectContext, DropDownContainer dropDownContainer) {
	}
	
	/**
	 * Validates an attribute for a field 
	 * and returns the resource key(s) in case of validation error
	 * @param fieldConfigBean
	 * @param settingsObject should be either TTextBoxSettingsBean,
	 * 						TOptionSettingsBean or TGeneralSettingsBean
	 * @return
	 */
	@Override
	public Map<Integer, List<Validator>> procesLoadValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean, Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean){
		return getValidators(fieldID,fieldConfigBean,parameterCode,
				settingsObject,workItemBean);
	}
	
	/**
	 * It would be enough to introduce getValidators() only at InputFieldType level
	 * (and return a null from procesLoadValidators() of this class) but
	 * than the composite fields (implementing CustomCompositeFieldTypeRT)
	 * would not call the getValidators() defined in them because CustomCompositeFieldTypeRT
	 * does not implement InputFieldType just AbstractFieldTypeRT and the procesLoadValidators()
	 * from this class would return always null
	 * @param fieldID
	 * @param fieldConfigBean
	 * @param parameterCode
	 * @param settingsObject
	 * @param workItemBean
	 * @return
	 */
	public Map<Integer, List<Validator>> getValidators(Integer fieldID, 
			TFieldConfigBean fieldConfigBean,Integer parameterCode,
			Object settingsObject, TWorkItemBean workItemBean) {
		return null;
	}
	
	/**
	 * Loads the matcher object for configuring the matcher
	 * @param fieldID
	 */
	@Override
	public IMatcherDT processLoadMatcherDT(Integer fieldID) {
		return getMatcherDT(fieldID);
	}
	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return null;
	}
	
	/**
	 * Loads the  matcher object for executing the matcher
	 * fieldID would suffice as parameter for loading the specific Matcher, 
	 * while the other parameters could be set after the matcher is loaded.
	 * But we need the matchValue parameter in the getMatcher() already set
	 * in case of replacement parameters, to replace the symbolic value with the actual value
	 * The symbolic and replacement values will be stored in the replacementContext
	 * (ex. $USER for the current user as originator/manager/responsible:
	 * the symbolic value for $USER should be replaced 
	 * with the value of the user currently logged in)
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 * @param matcherContext
	 */
	@Override
	public IMatcherRT processLoadMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext) {
		return getMatcherRT(fieldID, relation, matchValue, matcherContext);
	}	
	
	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID 
	 */
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext replacementContext) {		
		return null;
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
		return null;
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return null;
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
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return null;
	}
	
	/**
	 * Gets the IFieldChangeApply object for executing the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return null;
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	@Override
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new StringSetterConverter(fieldID);
	}
	
	/**
	 * Loads the datasource and value for configuring the field change
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	@Override
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue, 
			Integer parameterCode, TPersonBean personBean, Locale locale) {
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
	@Override
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		if (value!=null) {
			return value.toString();
		}
		return "";
	}
	
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
	@Override
	public String getShowISOValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		return getShowValue(fieldID, parameterCode, value, workItemID, localLookupContainer, locale);
	}
	
	/**
	 * Get the value to be shown
	 * For text fields typically the field value itself
	 * For selects the (eventually localized) label corresponding to the value 	
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		if (value!=null) {
			return value.toString();
		}
		return "";
	}
	
	@Override
	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID){
		return getShowValue(value,workItemContext.getLocale());
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
		return getShowValue(value, locale);
	}

	/**
	 ** Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		return isoStrValue;
	}
	
	/**
	 * Returns whether the iso show value differs from 
	 * the locale specific show value for this field type.
	 * This is needed to avoid loading the iso values 
	 * by repeating the load for locale specific show value
	 * (which takes surprisingly long time)   
	 * @return
	 */
	@Override
	public boolean isoDiffersFromLocaleSpecific() {
		return false;
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
	@Override
	public Comparable getSortOrderValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer) {
		if (value!=null) {
			try {
				return (Comparable)value;
			} catch (Exception e) {
				LOGGER.warn("Casting value " + value.toString() + " of class " + value.getClass().getName() + 
						" to comparable for field " + fieldID + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}
	
	/**
	 * Get the map of system dependences
	 * - key: a String of fieldID and parameterCode combination (parameterCode is null for system-, or custom single fields)
	 * - value: List of system fieldsIDs as Integers which defines the system dependencies for this part
	 * Important: The dependences on project and issueType are hardcoded so only
	 * the other system dependences should be specified 
	 * @return
	 */
	
	/**
	 * Get the map of internal dependences 
	 * - key: a String of fieldID and parameterCode combination (parameterCode is null for system-, or custom single fields)
	 * - value: List of parameterCodes as Integers which defines the internal dependencies for this key
	 * i.e. the indexes inside the composite this part depends on
	 * Important: This makes sense only for parts of a custom composite field and for the custom composite itself 
	 * Do not override for system or custom single fields 
	 * @return
	 */
	
	/**
	 * Gets the Matcher converter corresponding to the type
	 */
	@Override
	public MatcherConverter getMatcherConverter() {
		return StringMatcherConverter.getInstance();
	}
	
	/**
	 * Gets the string value to be stored by lucene
	 * @param value the value of the field
	 * @param workItemBean the lucene value might depend on other fields of the workItem
	 * @return
	 */
	@Override
	public String getLuceneValue(Object value, TWorkItemBean workItemBean) {
		if (value!=null) {
			return value.toString();
		}
		return null;
	}
	
	/**
	 * Whether the field will be used in lucene highlighter
	 */
	@Override
	public boolean isHighlightedField() {
		return false;
	}
	
	/**
	 * Whether this field is a UserPicker
	 * Special care should be taken by user pickers because of the notification (automail)
	 * A person may receive an automail just because he/she is selected in a userPicker 
	 * @return
	 */
	@Override
	public boolean isUserPicker() {
		return false;
	}
	
	/**
	 * Whether this field is a long text because then the read only
	 * rendering (on the report overview, printItem, etc.)
	 * should be made different as the rest of the "short" fields
	 * This can be the case for Description, Comment and longtext custom fields  
	 * @return
	 */
	@Override
	public boolean isLong() {
		return false;
	}
	
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return true;
	}
	
	/**
	 * Gets the raw string value
	 * @param value
	 * @return
	 */
	@Override
	public String getStringGroupValue(Object value) {
		if (value!=null) {
			return value.toString();
		}
		return "";
	}
	
	/**
	 * Parse the group value
	 * @param value
	 * @return
	 */
	@Override
	public Object getParseGroupValue(String value) {
		return value;
	}
	
	/**
	 * Gets the label for the group. It is typically the same
	 * as the showValue() of the workItems from the same group,
	 * but it will be called only for fields for which groupLabelDiffersFromShowValue() returns true
	 * (For create/last modification date it should differ from the show showValue() 
	 * because the time part of the date should be removed from the label for group)
	 * @return
	 */
	@Override
	public String getGroupLabel(Object value, Locale locale) {
		//actually the default implementation has no importance
		return getShowValue(value, locale);
	}
	
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
	@Override
	public boolean groupLabelDiffersFromShowValue() {
		return false;
	}
	
	/**
	 * Whether the field can be rendered on a form
	 * @return
	 */
	@Override
	public boolean mightAppearOnForm() {
		return true;
	}
	
	/**
	 * Whether the field might be matched in for an excel column
	 * @return
	 */
	@Override
	public boolean mightMatchExcelColumn() {
		return true;
	}
	
}
