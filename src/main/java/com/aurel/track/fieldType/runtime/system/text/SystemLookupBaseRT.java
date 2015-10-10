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


package com.aurel.track.fieldType.runtime.system.text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.ISortedBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.exchange.track.NameMappingBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.DropDownContainer;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.base.SystemOutputBaseRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MultipleSelectMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.SelectMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.matchers.run.SystemSelectMatcherRT;
import com.aurel.track.lucene.LuceneUtil;

/**
 * Runtime field type for Originator and LastEditedBy
 * @author Tamas Ruff
 *
 */
public abstract class SystemLookupBaseRT extends SystemOutputBaseRT implements ILookup {

	private static final Logger LOGGER = LogManager.getLogger(SystemLookupBaseRT.class);
	/**
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_PERSON;
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
		if (labelBean==null) {
			return null;
		} else {
			TPersonBean personBean = (TPersonBean)labelBean;
			if (personBean.isGroup()) {
				return PersonBL.GROUP_ICON_CLASS;
			}else{
				return PersonBL.USER_ICON_CLASS;
			}
		}
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
	 * Defines the data type of the field attribute
	 * Used by saving field attributes for custom fields
	 * and saving explicit history for both system and custom fields
	 * Should be a @ValueType constant
	 * Here not really meaningful because this fields will be never historized
	 * @return
	 */
	public int getValueType() {
		return ValueType.SYSTEMOPTION;
	}
		
	
	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field 
	 * by editing an existing issue or creating a new issue.
	 * The result might differ from loadCreateDataSource because it might be needed 
	 * to add the current value from the workItem to the list entries 
	 * (when right already revoked for the current value).
	 * The datasource for originator should contain only the originator person itself, and that just for existing workItem
	 * @param selectContext
	 * @param dropDownContainer 
	 * @return
	 */
	@Override
	public abstract void processLoadDataSource(SelectContext selectContext, DropDownContainer dropDownContainer);
	
	
	/**
	 * Gets the context with actual item data for injecting content into the labels
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public Map<String, Object> getLabelContext(Integer fieldID, Object value, Locale locale) {
		Map<String, Object> labelContextMap = null;
		Integer selectedValue = null;
		if (value!=null) {
			try {
				selectedValue = (Integer)value;
			} catch(Exception ex) {
				
			}
			if (selectedValue!=null) {
				TPersonBean personBean = LookupContainer.getPersonBean(selectedValue);
				if (personBean!=null) {
					labelContextMap = new HashMap<String, Object>();
					labelContextMap.put("email", personBean.getEmail());
					labelContextMap.put("phone", personBean.getPhone());
					labelContextMap.put("name", personBean.getName());
				}
				
			}
		}
		return labelContextMap;
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
		if (optionID!=null) {
			ISortedBean sortedBean = LookupContainer.getPersonBean(optionID);
			if (sortedBean!=null) {
				return sortedBean.getSortOrderValue();
			}
		}
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
		try {
			optionID = (Integer)value;
		}catch (Exception e) {
			LOGGER.error("Converting the value to integer failed with " + e.getMessage(), e);
		}
		if (optionID!=null) {	
			ILabelBean labelBean = LookupContainer.getPersonBean(optionID);
			if (labelBean!=null) {
				String label = labelBean.getLabel();
				if (label!=null) {
					return label;
				}
			}
		}
		return getShowValue(value, locale);
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
		if (value!=null) {
			ILabelBean labelBean = getLabelBean((Integer)value, locale);
			if (labelBean!=null) {
				return labelBean.getLabel();
			}
		}
		return "";
	}

	public String getShowValue(Object value, WorkItemContext workItemContext,Integer fieldID){
		ILabelBean labelBean =  workItemContext.getILabelBean((Integer)value, fieldID);
		if (labelBean!=null) {
			return labelBean.getLabel();
		}
		return "";
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
	}
	
	/**
	 * Parses the string value into the corresponding object value
	 * @param isoStrValue
	 * @return
	 */
	@Override
	public Object parseISOValue(Object isoStrValue) {
		if (isoStrValue!=null&& !"".equals(isoStrValue)) {
			return new Integer(isoStrValue.toString());
		}
		return null;
	}
	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new SelectMatcherDT(fieldID);
	}
	
	/**
	 * Loads the matcher for the field 
	 * @param fieldID 
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext){
		IMatcherRT matcherRT = new SystemSelectMatcherRT(fieldID, relation, matchValue, matcherContext);
		//replace the symbolic value with the actual value
		Integer[] matchValueArr;
		if (matchValue!=null) {
			try {
				matchValueArr = (Integer[])matchValue;
				if (matchValueArr!=null) {
					for (int i = 0; i < matchValueArr.length; i++) {
						Integer intMatchValue = matchValueArr[i];
						if (intMatchValue!=null && intMatchValue.equals(MatcherContext.LOGGED_USER_SYMBOLIC)) {
							Map<Integer, Integer> loggedUserReplacement = 
								(Map<Integer, Integer>)(matcherContext.getContextMap().get(MatcherContext.LOGGED_USER));
							if (loggedUserReplacement!=null && loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC)!=null) {
								matchValueArr[i] = loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC);
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Converting the match value to Integer[] failed with " + e.getMessage(), e);
			}
		}
		return matcherRT;
	}
	
	@Override
	public MatcherConverter getMatcherConverter() {
		//return SelectMatcherConverter.getInstance();
		return MultipleSelectMatcherConverter.getInstance();
	}
		
	
	/**
	 * Get the ILabelBean by primary key 
	 * @return
	 */
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		if (optionID!=null && (optionID.equals(MatcherContext.LOGGED_USER_SYMBOLIC) ||
				optionID.equals(MatcherContext.PARAMETER))) {
			TPersonBean personBean = new TPersonBean();
			String localizedName;
			if (optionID.equals(MatcherContext.LOGGED_USER_SYMBOLIC)) {
				localizedName = MatcherContext.getLocalizedLoggedInUser(locale);
			} else {
				localizedName = MatcherContext.getLocalizedParameter(locale);
			}
			personBean.setLastName(localizedName);
			personBean.setObjectID(optionID);
			return personBean;
		}
		return PersonBL.loadByPrimaryKey(optionID);
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
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.PERSONNAME;
	}
	
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean(ISerializableLabelBean serializableLabelBean) {
		return serializableLabelBean.serializeBean();
	}
	
	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TPersonBean();
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
	 * Gets the ID by the label
	 * @param fieldID
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @param label
	 * @param lookupBeansMap
	 * @param componentPartsMap
	 * @return
	 */
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID, 
			Locale locale, String label,
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		Integer objectID = NameMappingBL.getExactOrSimilarMatch(label, lookupBeansMap);
		if (objectID!=null) {
			return objectID;
		} else {
			//try to match the login name
			if (lookupBeansMap!=null) {
				Map<String, ILabelBean> loginToLabelBeanMap = new HashMap<String, ILabelBean>();		
				for (Iterator<ILabelBean> iterator = lookupBeansMap.values().iterator(); iterator.hasNext();) {
					TPersonBean labelBean = (TPersonBean)iterator.next();
					String loginName = labelBean.getLoginName();
					if (loginName!=null) {
						if (loginName.equals(label)) {
							return labelBean.getObjectID();
						} else {
							loginToLabelBeanMap.put(loginName, labelBean);
						}
					}
				}
				String similarName = NameMappingBL.getSimilarName(label, loginToLabelBeanMap.keySet());
				if (similarName!=null) {
					ILabelBean labelBean  = loginToLabelBeanMap.get(similarName);
					if (labelBean!=null) {
						return labelBean.getObjectID();
					}
				}
			}
		}	
		TPersonBean personBean = PersonBL.loadByLabel(label);
		if (personBean!=null) {
			return personBean.getObjectID();
		}
		return null;
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
	 * Gets the datasource for all possible list entries 
	 * @param fieldID
	 * @return
	 */
	public List<ILabelBean> getDataSource(Integer fieldID) {
		return (List)PersonBL.loadPersonsAndGroups();
	}
}
