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

package com.aurel.track.admin.customize.treeConfig.field;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FieldConfigDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.FieldConfigTO;
import com.aurel.track.item.ItemFieldRefreshBL;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.util.GeneralUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Field configuration logic
 */
public class FieldConfigBL {
	private static final Logger LOGGER = LogManager.getLogger(FieldConfigBL.class);
	private static FieldConfigDAO fieldConfigDAO = DAOFactory.getFactory().getFieldConfigDAO();
	
	/**
	 * Gets a fieldConfigBean from the TFieldConfig table
	 * @param key
	 * @return
	 */
	public static TFieldConfigBean loadByPrimaryKey(Integer key) {
		return fieldConfigDAO.loadByPrimaryKey(key);
	}
	
	/**
	 * Gets the list of default TFieldConfigBeans 
	 * @return
	 */
	public static List<TFieldConfigBean> loadDefault() {
		return fieldConfigDAO.loadDefault();
	}
	
	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a fieldID
	 * @param fieldID
	 * @return
	 */
	public static List<TFieldConfigBean> loadAllForField(Integer fieldID) {
		return fieldConfigDAO.loadAllForField(fieldID);
	}
	
	/**
	 * Loads the default field configuration for a field 
	 * @param fieldID
	 * @return
	 */
	public static TFieldConfigBean loadDefault(Integer fieldID){
		return fieldConfigDAO.loadDefault(fieldID);
	}
	
	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table for a list of fieldIDs
	 * @param fieldIDList
	 * @return
	 */
	public static List<TFieldConfigBean> loadDefaultForFields(List<Integer> fieldIDList) {
		return fieldConfigDAO.loadDefaultForFields(fieldIDList);
	}

	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a list of fieldIDs
	 * @param fieldIDList
	 * @return
	 */
	public static List<TFieldConfigBean> loadAllForFields(Set<Integer> fieldIDSet) {
		return fieldConfigDAO.loadAllForFields(fieldIDSet);
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table which have a project specified
	 * @param project if specified filter by project otherwise only be sure to not to be null (all project specific configurations)
	 * @return
	 */
	public static List<TFieldConfigBean> loadAllByProject(Integer project) {
		return fieldConfigDAO.loadAllByProject(project);
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table	
	 * @return
	 */
	public static List<TFieldConfigBean> loadAll() {
		return fieldConfigDAO.loadAll();
	}
	
	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table for a list of fieldConfigIDs
	 * @param fieldIDList
	 * @return
	 */
	public static List<TFieldConfigBean> loadByFieldConfigIDs(List<Integer> fieldConfigIDsList) {
		return fieldConfigDAO.loadByFieldConfigIDs(fieldConfigIDsList);
	}
	
	
	/**
	 * Gets the list of TFieldConfigBeans for issueTypes and fieldIDs
	 * Project and ProjectType does not matter 
	 * @param issueTypes
	 * @param fieldIDs
	 * @return
	 */
	public static List<TFieldConfigBean> loadByIssueTypesAndFields(List<Integer> issueTypes, List<Integer> fieldIDs) {
		return fieldConfigDAO.loadByIssueTypesAndFields(issueTypes, fieldIDs);
	}
	
	/**
	 * Gets the list of TFieldConfigBeans for projectTypes and fieldIDs
	 * Project and IssueType does not matter 
	 * @param projectTypes
	 * @param fieldIDs
	 * @return
	 */
	public static List<TFieldConfigBean> loadByProjectTypesAndFields(List<Integer> projectTypes, List<Integer> fieldIDs) {
		return fieldConfigDAO.loadByProjectTypesAndFields(projectTypes, fieldIDs);
	}

	/**
	 * Gets the list of TFieldConfigBeans for an projects and fieldIDs
	 * @param projects
	 * @param fieldIDs
	 * @return
	 */
	public static List<TFieldConfigBean> loadByProjectsAndFields(List<Integer> projects, List<Integer> fieldIDs) {
		return fieldConfigDAO.loadByProjectsAndFields(projects, fieldIDs);
	}
	
	/**
	 * Saves a field config in the TFieldConfig table
	 * @param fieldConfigBean
	 */
	public static Integer save(TFieldConfigBean fieldConfigBean) {
		return fieldConfigDAO.save(fieldConfigBean);
	}
	
	/**
	 * Deletes a field configuration
	 * @param objectID
	 */
	public static void deleteFieldConfig(Integer objectID) {
		fieldConfigDAO.delete(objectID);
		LocalizeBL.removeLocalizedResources(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX, objectID);
		LocalizeBL.removeLocalizedResources(LocalizationKeyPrefixes.FIELD_TOOLTIP_KEY_PREFIX, objectID);
	}
	
	/**
	 * Copies a field configuration
	 * @param fieldConfigBean
	 * @param deep
	 */
	public static TFieldConfigBean copy(TFieldConfigBean fieldConfigBean, boolean deep) {
		return fieldConfigDAO.copy(fieldConfigBean, deep);
	}
	
	/**
	 * Gets all the localized default field configuration labels with explicit history fields
	 * The field might be set with explicit history at any level (for. ex. issue type specific)
	 * @return
	 */
	public static Map<Integer, String> loadAllWithExplicitHistory(Locale locale) {
		Set<Integer> explicitHistoryFields = new HashSet<Integer>();
		List<TFieldConfigBean> allWithExplicitHistory = fieldConfigDAO.loadAllWithExplicitHistory();
		if (allWithExplicitHistory!=null) {
			for (TFieldConfigBean fieldConfigBean : allWithExplicitHistory) {
				Integer fieldID = fieldConfigBean.getField();
				explicitHistoryFields.add(fieldID);
			}
		}
		return FieldRuntimeBL.getLocalizedDefaultFieldLabels(GeneralUtils.createIntegerListFromCollection(explicitHistoryFields), locale);
	}
	
	public static Map<Integer,FieldConfigTO> extractFieldConfigTO(WorkItemContext workItemContext,Locale locale){
		Map<Integer,TFieldBean> presentFields=workItemContext.getPresentFieldBeans();
		Map<Integer,FieldConfigTO> fieldConfigTOMap=new HashMap<Integer, FieldConfigTO>();
		for(Map.Entry<Integer,TFieldBean> entry:presentFields.entrySet()){
			Integer fieldID=entry.getKey();
			TFieldBean fieldBean=entry.getValue();
			fieldConfigTOMap.put(fieldID,createFieldConfigTO(workItemContext,fieldBean,locale));
		}
		return  fieldConfigTOMap;
	}
	public static FieldConfigTO createFieldConfigTO(WorkItemContext workItemContext,TFieldBean fieldBean,Locale locale){
		FieldConfigTO fieldConfigTO=new FieldConfigTO();
		Integer fieldID=fieldBean.getObjectID();
		fieldConfigTO.setFieldID(fieldID);
		FieldType fieldType= FieldTypeManager.getInstance().getType(fieldID);
		TypeRendererRT fieldTypeRendererRT = null;
		IFieldTypeRT fieldTypeRT = null;
		if (fieldType!=null) {
			fieldType.setFieldID(fieldID);
			fieldTypeRendererRT = fieldType.getRendererRT();
			fieldTypeRT = fieldType.getFieldTypeRT();
		}
		TFieldConfigBean fieldConfigBean=(TFieldConfigBean)workItemContext.getFieldConfigs().get(fieldID);
		String tooltip = null;
		if (fieldConfigBean!=null) {
			tooltip=fieldConfigBean.getTooltip();
			if (fieldTypeRT!=null && tooltip!=null) {
				Map<String, Object> labelContext = fieldTypeRT.getLabelContext(fieldID, workItemContext.getWorkItemBean().getAttribute(fieldID, null), locale);
				if (labelContext!=null && !labelContext.isEmpty()) {
					Template tooltipTemplate =  getTooltipTemplate(tooltip);
					StringWriter labelWriter = new StringWriter();
					try {
						tooltipTemplate.process(labelContext, labelWriter);
						tooltip = labelWriter.toString();
					} catch (Exception e) {
						LOGGER.debug("Processing template: " + labelWriter.toString() + " failed with " + e.getMessage());
					}
				}
			}
			fieldConfigTO.setLabel(fieldConfigBean.getLabel());
		}
		if(tooltip==null) {
			tooltip="";
		}
		fieldConfigTO.setTooltip(tooltip);
		Integer accesFlag=null;
		if(workItemContext.getFieldRestrictions()!=null){
			accesFlag=workItemContext.getFieldRestrictions().get(fieldID);
		}
		boolean readonly=false;
		boolean invisible=false;
		if(accesFlag!=null){
			if(accesFlag.intValue()== TRoleFieldBean.ACCESSFLAG.NOACCESS){
				readonly=true;
				invisible=true;
			}else{
				invisible=false;
				readonly = (accesFlag.intValue() == TRoleFieldBean.ACCESSFLAG.READ_ONLY);
			}
		}
		fieldConfigTO.setReadonly(readonly);
		Set<Integer> readOnlyFields = getReadOnlySet();
		if (readOnlyFields.contains(fieldID)) {
			fieldConfigTO.setReadonly(true);
		}else{
			fieldConfigTO.setRequired(fieldBean.isRequiredString()||fieldConfigBean.isRequiredString());
		}
		fieldConfigTO.setInvisible(invisible);
		boolean hasDependences = ItemFieldRefreshBL.hasDependences(fieldBean, workItemContext);
		fieldConfigTO.setHasDependences(hasDependences);
		if (hasDependences) {
			fieldConfigTO.setClientSideRefresh(ItemFieldRefreshBL.isClientSideRefresh(fieldID));
		}
		//TODO validate JSON
		if (fieldTypeRendererRT!=null) {
			fieldConfigTO.setJsonData(fieldTypeRendererRT.createJsonData(fieldBean,workItemContext));
		}
		return fieldConfigTO;
	}

	/**
	 * Gets the localized template
	 * @return
	 */
	private static Template getTooltipTemplate(String tooltip) {
		Template tooltipTemplate=null;
		try {
			if (tooltip!=null) {
				tooltipTemplate = new Template("tooltip", new StringReader(tooltip),new Configuration());
			}
		} catch (IOException e) {
			LOGGER.debug("Loading the tooltip template failed with " + e.getMessage());
			LOGGER.warn(ExceptionUtils.getStackTrace(e));
		}
		return tooltipTemplate;
	}
	
	private static Set<Integer> getReadOnlySet() {
		Set<Integer> readOnlySet = new HashSet<Integer>();
		readOnlySet.add(SystemFields.INTEGER_PROJECT);
		readOnlySet.add(SystemFields.INTEGER_ISSUETYPE);
		readOnlySet.add(SystemFields.INTEGER_ISSUENO);
		readOnlySet.add(SystemFields.INTEGER_ORIGINATOR);
		readOnlySet.add(SystemFields.INTEGER_CREATEDATE);
		readOnlySet.add(SystemFields.INTEGER_LASTMODIFIEDDATE);
		readOnlySet.add(SystemFields.INTEGER_CHANGEDBY);
		return readOnlySet;
	}
	public static Set<Integer> addBaseSystemFields(Set<Integer> presentFields){
		presentFields.add(SystemFields.INTEGER_ISSUENO);
		presentFields.add(SystemFields.INTEGER_PROJECT);
		presentFields.add(SystemFields.INTEGER_LASTMODIFIEDDATE);
		presentFields.add(SystemFields.INTEGER_ISSUETYPE);
		presentFields.add(SystemFields.INTEGER_SYNOPSIS);
		return presentFields;
	}

	
}
