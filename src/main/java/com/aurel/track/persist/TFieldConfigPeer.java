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

package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.dao.FieldConfigDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TFieldConfigPeer
	extends com.aurel.track.persist.BaseTFieldConfigPeer implements FieldConfigDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TFieldConfigPeer.class);
	
	public static final long serialVersionUID = 400L;
	
	private static Class[] deletePeerClasses = {
		TTextBoxSettingsPeer.class, 
		TOptionSettingsPeer.class,
		TGeneralSettingsPeer.class,
		TConfigOptionsRolePeer.class,
		//use the superclass doDelete() methode!!!	
		BaseTFieldConfigPeer.class,
		};
	
	private static String[] deleteFields = {
		TTextBoxSettingsPeer.CONFIG, 
		TOptionSettingsPeer.CONFIG,
		TGeneralSettingsPeer.CONFIG,
		TConfigOptionsRolePeer.CONFIGKEY,
		BaseTFieldConfigPeer.OBJECTID
	};

	
	/**
	 * Loads the field config by primary key
	 * @param objectID
	 * @return
	 */
	public TFieldConfigBean loadByPrimaryKey(Integer objectID) {
		TFieldConfig tFieldConfig = null;
		try {
			tFieldConfig = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a field config by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tFieldConfig!=null) {
			return tFieldConfig.getBean();
		}
		return null;
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table	
	 * @return
	 */
	public List<TFieldConfigBean> loadAll() {
		Criteria criteria = new Criteria();
		criteria.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading all field configs failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Gets all the TFieldConfigBean's with explicit history from the TFieldConfig table	
	 * @return
	 */
	public List<TFieldConfigBean> loadAllWithExplicitHistory() {
		Criteria criteria = new Criteria();
		criteria.add(HISTORY, BooleanFields.TRUE_VALUE);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading all field configs failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table for a list of fieldConfigIDs
	 * @param fieldIDList
	 * @return
	 */
	public List<TFieldConfigBean> loadByFieldConfigIDs(List<Integer> fieldConfigIDsList) {
		List<TFieldConfigBean> fieldConfigBeansList = new ArrayList<TFieldConfigBean>();
		if (fieldConfigIDsList==null || fieldConfigIDsList.isEmpty()) {
			return fieldConfigBeansList;
		}
		Criteria criteria;
		List<int[]> fieldConfigIDChunksList = GeneralUtils.getListOfChunks(fieldConfigIDsList);
		if (fieldConfigIDChunksList==null) {
			return fieldConfigBeansList;
		}
		Iterator<int[]> iterator = fieldConfigIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] fieldConfigIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(OBJECTID, fieldConfigIDChunk);
			try {
				fieldConfigBeansList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the defined fieldConfigs for " + fieldConfigIDChunk.length +
						" failed with " + e.getMessage(), e);
			}
		}
		return fieldConfigBeansList;
	}
	
	/**
	 * Returns whether the label is unique for the field
	 * @param label
	 * @param fieldID
	 * @return
	 */
	/*public boolean isLabelUnique(String label, Integer fieldID) {
		List fieldConfigsList = null;
		Criteria crit = new Criteria();
		if (fieldID!=null) {
			crit.add(FIELDKEY, fieldID, Criteria.NOT_EQUAL);
		}
		crit.add(LABEL, label);
		try {
			fieldConfigsList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Verifying the uniqueness of the label " + label + " and field " + fieldID + " failed with " + e.getMessage(), e);
		}
		if (fieldConfigsList==null || fieldConfigsList.isEmpty()) {
			return true;
		}
		return false;
	}*/
	
	/**
	 * Saves a field config in the TFieldConfig table
	 * @param fieldConfigBean
	 */
	public Integer save(TFieldConfigBean fieldConfigBean) {
		try {
			TFieldConfig tFieldConfig = BaseTFieldConfig.createTFieldConfig(fieldConfigBean);
			tFieldConfig.save();
			return tFieldConfig.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a field config failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Copies a field config
	 * @param fieldConfigBean
	 * @param deep
	 */
	public TFieldConfigBean copy(TFieldConfigBean fieldConfigBean, boolean deep) {
		try {
			return (BaseTFieldConfig.createTFieldConfig(fieldConfigBean).copy(deep)).getBean();
		} catch (TorqueException e) {
			LOGGER.error("Deep " + deep + " copying a field config failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a fieldConfigBean by primary key
	 * First deletes all settings related to the field
	 * then deletes the field config itself
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * Deletes the TFieldConfigs satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		List<TFieldConfig> list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TFieldConfigs to be deleted failed with " + e.getMessage(), e);
		}			
		if (list == null || list.size() < 1) {
			return;
		}
		for (TFieldConfig fieldConfig : list) {
			FieldConfigBL.deleteFieldConfig(fieldConfig.getObjectID());
		}
	}
	
	private void addDomainConditions(Criteria criteria, Integer issueType, Integer projectType, Integer project) {
		//add issueType filter
		if (issueType==null) {
			criteria.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(ISSUETYPE, issueType);
		}
		//add projectType filter
		if (projectType==null) {
			criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECTTYPE, projectType);
		}
		//add project filter
		if (project==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECT, project);
		}
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table 
	 * for a given issueType, projectType, project and isCustom
	 * @param issueType include into filtering even if null
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @param isCustom: is null: do not include into filtering
	 * 					is false: filter by system fields
	 * 					is true: filter by custom fields 
	 * @return
	 */
	public List<TFieldConfigBean> loadAllByFieldType(Integer issueType, Integer projectType, Integer project, Boolean isCustom) {
		Criteria criteria = new Criteria();
		if (isCustom!=null) {
			criteria.addJoin(TFieldPeer.OBJECTID, FIELDKEY);
			if (isCustom.booleanValue()) {
				criteria.add(TFieldPeer.ISCUSTOM, BooleanFields.TRUE_VALUE);
			} else {
				criteria.add(TFieldPeer.ISCUSTOM, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
			}
		}
		addDomainConditions(criteria, issueType, projectType, project);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by field type for custom flag " + isCustom + " and issueType " + issueType + 
					" and projectType " + projectType + " and project " + project  +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table for issueType, projectType and project
	 * The issueType  
	 * @param issueType can be null, in this case the issue type should not be null to exclude the standard (global) settings
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @return
	 */
	public List<TFieldConfigBean> loadAllByIssueType(Integer issueType, Integer projectType, Integer project) {
		Criteria criteria = new Criteria();
		//add issueType filter only if issueType is specified
		if (issueType != null) {
			criteria.add(ISSUETYPE, issueType);
		} else {
			//exclude the standard (global) settings
			criteria.add(ISSUETYPE, (Object) null, Criteria.ISNOTNULL);
		}
		// add projectType filter anyway
		if (projectType == null) {
			criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECTTYPE, projectType);
		}
		// add project filter anyway
		if (project == null) {
			criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		} else {
			criteria.add(PROJECT, project);
		}		
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by issueType for issueType " + issueType + 
					" and projectType " + projectType + " and project " + project  +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table which have a projectType specified	
	 * @param projectType if specified filter by projectType otherwise only be sure to not to be null (all projectType specific configurations)
	 * @return
	 */
	public List<TFieldConfigBean> loadAllByProjectType(Integer projectType) {
		Criteria criteria = new Criteria();
		if (projectType==null) {
			criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECTTYPE, projectType);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by projectType " + projectType + "  failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table which have a project specified
	 * @param project if specified filter by project otherwise only be sure to not to be null (all project specific configurations)
	 * @return
	 */
	public List<TFieldConfigBean> loadAllByProject(Integer project) {
		Criteria criteria = new Criteria();
		if (project==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECT, project);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by project " + project + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets all the TFieldConfigBean's  from the TFieldConfig table which have a project specified
	 * @param project if specified filter by project otherwise only be sure that project is not null (all project specific configurations)
	 * @return
	 */
	public List<TFieldConfigBean> loadAllByProjects(List<Integer> projects) {
		Criteria criteria = new Criteria();
		if (projects==null || projects.isEmpty()) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.addIn(PROJECT, projects);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by projects " + projects + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the default TFieldConfigBean from the TFieldConfig table
	 * 
	 * @param field
	 * @return
	 */
	public TFieldConfigBean loadDefault(Integer field) {
		List fieldConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, field);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			fieldConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the default field config for field " + field
					+ " failed with " + e.getMessage(), e);
		}
		if (fieldConfigList == null || fieldConfigList.isEmpty()) {
			LOGGER.error("No default field config found for field " + field);
			return null;
		}
		if (fieldConfigList.size() > 1) {
			LOGGER.error("More than one default field config found for field " + field);
		}
		return ((TFieldConfig)fieldConfigList.get(0)).getBean();
	}

	/**
	 * Gets the list of default TFieldConfigBeans from the TFieldConfig table for a list of fieldIDs
	 * @param fieldIDList
	 * @return
	 */
	public List<TFieldConfigBean> loadDefaultForFields(List<Integer> fieldIDList) {
		if (fieldIDList==null || fieldIDList.isEmpty()) {
			return new LinkedList<TFieldConfigBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(FIELDKEY, fieldIDList);
		criteria.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the default field config for fieldID list failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a list of fieldIDs
	 * @param fieldIDSet
	 * @return
	 */
	public List<TFieldConfigBean> loadAllForFields(Set<Integer> fieldIDSet) {
		if (fieldIDSet==null || fieldIDSet.isEmpty()) {
			return new LinkedList<TFieldConfigBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(FIELDKEY, fieldIDSet.toArray());
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the all field configs for fieldID list of length " + fieldIDSet.size() + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of all TFieldConfigBeans from the TFieldConfig table for a fieldID
	 * @param fieldID
	 * @return
	 */
	public List<TFieldConfigBean> loadAllForField(Integer fieldID) {
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, fieldID);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the all field configs for fieldID  " + fieldID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by issueType
	 * @param field
	 * @param issueType
	 * @return
	 */
	public TFieldConfigBean loadByIssueType(Integer field, Integer issueType) {
		List fieldConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, field);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			fieldConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the field config by issueType for field "
					+ field + " and issueType " + issueType + " failed with "
					+ e.getMessage(), e);
		}
		if (fieldConfigList == null || fieldConfigList.isEmpty()) {
			LOGGER.debug("No field config found by issueType for field "
					+ field + " and issueType " + issueType);
			return null;
		}
		if (fieldConfigList.size() > 1) {
			LOGGER.warn("More then one field config found by issueType for field "
							+ field + " and issueType " + issueType);
		}
		return ((TFieldConfig) fieldConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by projectType
	 * @param field
	 * @param projectType
	 * @return
	 */
	public TFieldConfigBean loadByProjectType(Integer field, Integer projectType) {
		List fieldConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, field);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			fieldConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the field config by projectType for field "
					+ field + " and projectType " + projectType + " failed with "
					+ e.getMessage(), e);
		}
		if (fieldConfigList == null || fieldConfigList.isEmpty()) {
			LOGGER.debug("No field config found by projectType for field "
					+ field + " and projectType " + projectType);
			return null;
		}
		if (fieldConfigList.size() > 1) {
			LOGGER.warn("More then one field config found by projectType for field "
							+ field + " and projectType " + projectType);
		}
		return ((TFieldConfig) fieldConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by project
	 * @param field
	 * @param project
	 * @return
	 */
	public TFieldConfigBean loadByProject(Integer field, Integer project) {
		List fieldConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, field);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			fieldConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the field config by project for field "
					+ field + " and project " + project + " failed with "
					+ e.getMessage(), e);
		}
		if (fieldConfigList == null || fieldConfigList.isEmpty()) {
			LOGGER.debug("No field config found by project for field "
					+ field + " and project " + project);
			return null;
		}
		if (fieldConfigList.size() > 1) {
			LOGGER.warn("More then one field config found by project for field "
							+ field + " and project " + project);
		}
		return ((TFieldConfig) fieldConfigList.get(0)).getBean();
	}
 	
	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by issueType and projectType
	 * @param field
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	public TFieldConfigBean loadByIssueTypeAndProjectType(Integer field, Integer issueType, Integer projectType) {
		List fieldConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, field);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			fieldConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the field config by issueType and projectType for field " + field + 
					" and issueType " + issueType + " and project type " + projectType + " failed with " + e.getMessage(), e);
		}
		if (fieldConfigList == null || fieldConfigList.isEmpty()) {
			LOGGER.debug("No field config found by issueType and projecttype for field " + field + " issueType " + issueType + " and projecttype" + projectType);
			return null; 
		}
		if (fieldConfigList.size()>1) {
			LOGGER.warn("More than one field config found by issueType and projecttype for field " + field + " issueType " + issueType + " and projecttype" + projectType);
		}
		return ((TFieldConfig)fieldConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TFieldConfigBean from the TFieldConfig table by issueType and project
	 * @param field
	 * @param issueType
	 * @param project
	 * @return
	 */
	public TFieldConfigBean loadByIssueTypeAndProject(Integer field, Integer issueType, Integer project) {
		List fieldConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, field);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, project);		
		try {
			fieldConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the field config by issueType  and project for field " + field + 
					" and issueType " + issueType + " and project " + project + " failed with " + e.getMessage(), e);
		}
		if (fieldConfigList == null || fieldConfigList.isEmpty()) {
			LOGGER.debug("No field config found by issueType and project for field " + field + " issueType " + issueType + " and project" + project);
			return null; 
		}
		if (fieldConfigList.size()>1) {
			LOGGER.warn("More than one field config found by issueType and project for field " + field + " issueType " + issueType + " and project" + project);
		}
		return ((TFieldConfig)fieldConfigList.get(0)).getBean();
	}

	/**
	 * 
	 * Load the configuration for parameteres:
	 * projectTypeID
	 * projectID
	 * issueID
	 * from cfg
	 * 
	 * @param cfg
	 * @return
	 */
	public List<TFieldConfigBean> loadByFieldConfigParameters(TFieldConfigBean cfg){
		return loadByFieldConfigParameters(cfg,false);
	}
	public List<TFieldConfigBean> loadByFieldConfigParameters(TFieldConfigBean cfg,boolean ignoreCustom){
		Integer projectTypeID=null;
		Integer projectID=null;
		Integer issueTypeID=null;
		boolean isCustom = false;
		if(cfg!=null){
			projectTypeID=cfg.getProjectType();
			projectID=cfg.getProject();
			issueTypeID=cfg.getIssueType();
			isCustom = cfg.isCustom();
		}
		Criteria crit = new Criteria();
		crit.addJoin(FIELDKEY, BaseTFieldPeer.OBJECTID);
		addDomainConditions(crit, issueTypeID, projectTypeID, projectID);
		crit.addAscendingOrderByColumn(TFieldPeer.NAME);
		if(!ignoreCustom){
			if (isCustom) {
				crit.add(BaseTFieldPeer.ISCUSTOM, "Y");
			} else {
				crit.add(BaseTFieldPeer.ISCUSTOM, (Object) "Y", Criteria.NOT_EQUAL);
			}
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading  fieldConfigs failed with " + e.getMessage(), e);
			return null;
		}
	}

	
	/* Runtime methods */
		
	/**
	 * Gets the list of default TFieldConfigBeans 
	 * @return
	 */
	public List<TFieldConfigBean> loadDefault() {
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the default field configs failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of TFieldConfigBeans for an issueType
	 * @param issueType
	 * @return
	 */
	public List<TFieldConfigBean> loadByIssueType(Integer issueType) {
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by issueType " 
					+ issueType +  " failed with " + e.getMessage(), e);
			return null;
		}
			
	}
	public List<TFieldConfigBean> loadByIssueTypes(Object[] issueTypeIDs){
		List torqueList = null;
		if (issueTypeIDs!=null && issueTypeIDs.length!=0) {
			Criteria crit = new Criteria();
			crit.addIn(ISSUETYPE, issueTypeIDs);
			crit.add(PROJECT, (Object)null, Criteria.ISNULL);
			crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
			try {
				torqueList = doSelect(crit);
			} catch (TorqueException e) {
				LOGGER.error("Loading the field configs by issueTypes  failed with  " + e.getMessage(), e);
			}
		}
		return convertTorqueListToBeanList(torqueList);
	}

	
	
	/**
	 * Gets the list of TFieldConfigBeans for an projectType
	 * @param projectType
	 * @return
	 */
	public List<TFieldConfigBean> loadByProjectType(Integer projectType) {
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by projectType " 
					+ projectType + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of TFieldConfigBeans for an project
	 * @param project
	 * @return
	 */
	public List<TFieldConfigBean> loadByProject(Integer project) {
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by projectType " 
					+ project +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of TFieldConfigBeans for an issueType and a projectType
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	public List<TFieldConfigBean> loadByIssueTypeAndProjectType(Integer issueType, Integer projectType) {
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by issueType " + issueType  +
					" and project type " + projectType + " failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets the list of TFieldConfigBeans for an issueType and project 
	 * @param issueType
	 * @param project
	 * @return
	 */
	public List<TFieldConfigBean> loadByIssueTypeAndProject(Integer issueType, Integer project) {
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by issueType " + issueType + 
					" and project " + project + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of TFieldConfigBeans for issueTypes and fieldIDs
	 * Project and ProjectType does not matter 
	 * @param issueTypes
	 * @param fieldIDs
	 * @return
	 */
	public List<TFieldConfigBean> loadByIssueTypesAndFields(List<Integer> issueTypes, List<Integer> fieldIDs) {
		if (issueTypes==null || issueTypes.isEmpty() || fieldIDs==null || fieldIDs.isEmpty()) {
			return new LinkedList<TFieldConfigBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(ISSUETYPE, issueTypes);
		criteria.addIn(FIELDKEY, fieldIDs);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by issueTypes " + 
					issueTypes.size() + " and fields " + fieldIDs.size() +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of TFieldConfigBeans for projectTypes and fieldIDs
	 * Project and IssueType does not matter 
	 * @param projectTypes
	 * @param fieldIDs
	 * @return
	 */
	public List<TFieldConfigBean> loadByProjectTypesAndFields(List<Integer> projectTypes, List<Integer> fieldIDs) {
		if (projectTypes==null || projectTypes.isEmpty() || fieldIDs==null || fieldIDs.isEmpty()) {
			return new LinkedList<TFieldConfigBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(PROJECTTYPE, projectTypes);
		criteria.addIn(FIELDKEY, fieldIDs);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by projectTypes " + 
					projectTypes.size() + " and fields " + fieldIDs.size() +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the list of TFieldConfigBeans for an projects and fieldIDs
	 * @param projects
	 * @param fieldIDs
	 * @return
	 */
	public List<TFieldConfigBean> loadByProjectsAndFields(List<Integer> projects, List<Integer> fieldIDs) {
		if (projects==null || projects.isEmpty() || fieldIDs==null || fieldIDs.isEmpty()) {
			return new LinkedList<TFieldConfigBean>();
		}
		Criteria criteria = new Criteria();
		criteria.addIn(PROJECT, projects);
		criteria.addIn(FIELDKEY, fieldIDs);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the field configs by projects " + 
					projects.size() + " and fields " + fieldIDs.size() +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	private List<TFieldConfigBean> convertTorqueListToBeanList(List<TFieldConfig> torqueList) {
		List<TFieldConfigBean> beanList = new ArrayList<TFieldConfigBean>();
		if (torqueList!=null) {
			Iterator<TFieldConfig> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add((itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
}
