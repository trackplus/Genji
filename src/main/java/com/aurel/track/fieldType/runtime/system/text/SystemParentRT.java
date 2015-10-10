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


import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.ParentBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.fieldChange.apply.ParentFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.ParentFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.ParentSetterConverter;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.SystemInputBaseRT;
import com.aurel.track.fieldType.runtime.matchers.converter.IntegerMatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.ItemPickerMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IntegerMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.prop.ApplicationBean;

/**
 * Runtime type for Parent issue
 * @author Tamas Ruff
 *
 */
public class SystemParentRT extends SystemInputBaseRT {

	private static final Logger LOGGER = LogManager.getLogger(SystemParentRT.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	

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
		workItemDAO.parentChanged(workItemBean, workItemBeanOriginal, contextInformation);
	}
	
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
	 * In case of a custom picker or system selects select the list type
	 * Used by saving custom pickers and 
	 * explicit history for both system and custom fields
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_ISSUENO;
	}
	
	/**
	 * Creates the matcher object for configuring the matcher
	 * @param fieldID 
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new ItemPickerMatcherDT(fieldID, false);
	}
	
	/**
	 * Creates the  matcher object for executing the matcher
	 * @param fieldID
	 * @param relation
	 * @param matchValue
	 */
	@Override
	public IMatcherRT getMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext){		
		return new IntegerMatcherRT(fieldID, relation, matchValue, matcherContext);
	}
	
	@Override
	public MatcherConverter getMatcherConverter() {
		return IntegerMatcherConverter.getInstance();
	}
	
	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new ParentBulkSetter(fieldID);
	}
	
	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new ParentFieldChangeConfig(fieldID);
	}
	
	/**
	 * Gets the IActivityExecute object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new ParentFieldChangeApply(fieldID);
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new ParentSetterConverter(fieldID);
	}
	
	/**
	 * Get the value to be shown
	 * For text fields typically the field value itself
	 * For selects the (eventually localized) label corresponding to the value 	
	 * @param value
	 * @param locale
	 * @return
	 */
	public String getShowValue(Object value, Locale locale) {
		Integer optionID = null;
		try {
			optionID = (Integer)value;
		} catch (Exception e) {
			LOGGER.error("Converting the value to integer failed with " + e.getMessage(), e);
		}
		if (optionID!=null) {
			if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
				TWorkItemBean parentWorkItemBean = null;
				try {
					parentWorkItemBean = ItemBL.loadWorkItem(optionID);
					if (parentWorkItemBean!=null) {
						return SystemProjectSpecificIssueNoRT.getShowValue(parentWorkItemBean.getIDNumber(), parentWorkItemBean);
					}
				} catch (ItemLoaderException e) {	
				}
			} else {
				return optionID.toString();
			}
		} else {
			LOGGER.debug("The parentID " + optionID + " was not found");
		}
		return "";
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
		} catch (Exception e) {
			LOGGER.error("Converting the value to integer failed with " + e.getMessage(), e);
		}
		if (optionID!=null) {
			TWorkItemBean parentWorkItemBean = null;
			Map<Integer, TWorkItemBean> beansMap = null;
			//then try from the dropdown list
			//Map beansMap = dropDownContainer.getDataSourceMap(MergeUtil.mergeKey(fieldID, parameterCode))
			if (localLookupContainer!=null) {
				beansMap = localLookupContainer.getWorkItemsMap();
				if (beansMap!=null) {
					parentWorkItemBean = beansMap.get(optionID);
				}
			}
			//it can be that the parent workItem is not in the result set then it should be get from the database
			//if if is too often the case it can result in performance problems
			if (parentWorkItemBean==null) {
				try {
					parentWorkItemBean = workItemDAO.loadByPrimaryKey(optionID);
					LOGGER.debug("The parentID " + optionID + " was not found in container and was loaded from database");
					if (beansMap!=null) {
						beansMap.put(optionID, parentWorkItemBean);
					}
				} catch (ItemLoaderException e) {
					LOGGER.debug("Loading the parentID " + optionID + " failed with " + e.getMessage(), e);
				}
			}
			if (parentWorkItemBean!=null) {
				String itemID = "";
				if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO);
					Integer parentItemID = parentWorkItemBean.getIDNumber();
					if (parentItemID!=null) {
						itemID = fieldTypeRT.getShowValue(fieldID, parameterCode, parentItemID, workItemID, localLookupContainer, locale);
					}
				} else {
					itemID = parentWorkItemBean.getObjectID().toString();
				}
				String parentLabel = itemID + " " + parentWorkItemBean.getSynopsis();
				if (parentLabel.trim().length()>0) {
					return parentLabel;
				}
			} else {
				LOGGER.debug("The parentID " + optionID + " was not found");
			}
		}
		return "";
	}
	
	@Override
	public boolean isoDiffersFromLocaleSpecific() {
		return true;
	}
	
	
	/**
	 * Get the ISO show value for locale independent exporting to xml
	 * typically same as show value, date and number values are formatted by iso format 
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale
	 * @return
	 */
	public String getShowISOValue(Integer fieldID, Integer parameterCode, Object value, 
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		Integer optionID = null;
		try {
			optionID = (Integer)value;
		} catch (Exception e) {
			LOGGER.error("Converting the value to integer failed with " + e.getMessage(), e);
		}
		if (optionID!=null) {
			return optionID.toString();
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
		if (isoStrValue!=null) {
			return new Integer(isoStrValue.toString());
		}
		return null; 
	}
	
	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable   
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return false;
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
		return LuceneUtil.LOOKUPENTITYTYPES.DIRECTINTEGER;
	}
}

