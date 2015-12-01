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

package com.aurel.track.persist;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.dao.ScreenConfigDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TScreenConfigPeer
extends com.aurel.track.persist.BaseTScreenConfigPeer implements ScreenConfigDAO{

	private static final Logger LOGGER = LogManager.getLogger(TScreenConfigPeer.class);
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads the screenConfig by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TScreenConfigBean loadByPrimaryKey(Integer objectID) {
		TScreenConfig tobject = null;
		try {
			tobject = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a screenConfig by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	/**
	 * Gets the screenConfigBeans bu action from the TScreenConfig table
	 * @param actionID
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadByActionKey(Integer actionID) {		
		Criteria crit = new Criteria();
		crit.add(ACTIONKEY, actionID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all screenConfigs failed with " + e.getMessage());
			return null;
		}		
	}
	
	/**
	 * Load the screen configurations for projects 
	 * @param projectIDs
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadByProjects(Object[] projectIDs) {
		if (projectIDs!=null && projectIDs.length!=0) {					
			Criteria crit = new Criteria();
			crit.addIn(PROJECT, projectIDs);
			crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Loading screenConfigs by projects failed with " + e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * Load the screen configurations for project types
	 * @param projectTypeIDs
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadByProjectTypes(Object[] projectTypeIDs) {
		if (projectTypeIDs!=null && projectTypeIDs.length!=0) {					
			Criteria crit = new Criteria();
			crit.addIn(PROJECTTYPE, projectTypeIDs);
			crit.add(PROJECT, (Object)null, Criteria.ISNULL);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Loading screenConfigs by project types failed with " + e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * Load the screen configurations for issue types
	 * @param issueTypeIDs
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadByIssueTypes(Object[] issueTypeIDs) {
		if (issueTypeIDs!=null && issueTypeIDs.length!=0) {					
			Criteria crit = new Criteria();
			crit.addIn(ISSUETYPE, issueTypeIDs);
			crit.add(PROJECT, (Object)null, Criteria.ISNULL);
			crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Loading screenConfigs by issue types failed with " + e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * Load the global screens
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadDefaults() {				
		Criteria crit = new Criteria();
		crit.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		crit.add(PROJECT, (Object)null, Criteria.ISNULL);
		crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading screenConfigs by issue types failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets all the TScreenConfigBeans for issueType, projectType and project  
	 * @param issueType can be null, in this case the result it is not filtered by issueType
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadAllByIssueType(Integer issueType, Integer projectType, Integer project) {
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
			LOGGER.error("Loading the screen assignments by issueType for issueType " + issueType + 
					" and projectType " + projectType + " and project " + project  +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets all the TScreenConfigBeans which have a projectType specified	
	 * @param projectType if specified filter by projectType otherwise only be sure to not to be null (all projectType specific configurations)
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadAllByProjectType(Integer projectType) {
		Criteria criteria = new Criteria();
		if (projectType==null) {
			criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECTTYPE, projectType);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen assignments by projectType " + projectType + "  failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets TScreenConfigBeans by project
	 * @param project
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadAllByProject(Integer project) {
		Criteria criteria = new Criteria();
		if (project==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECT, project);
		}	
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen configs by project " + project + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets TMailTemplateConfigBeans by project
	 * @param projects
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> loadAllByProjects(List<Integer> projects) {
		Criteria criteria = new Criteria();
		if (projects==null || projects.isEmpty()) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.addIn(PROJECT, projects);
		}	
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen configs by project " + projects + " failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Saves a screenConfig in the TScreenConfig table
	 * @param bean
	 * @return
	 */
	@Override
	public Integer save(TScreenConfigBean bean){
		try {
			TScreenConfig tobject = BaseTScreenConfig.createTScreenConfig(bean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a screenConfig failed with " + e.getMessage());
			return null;
		}		
	}
	
	/**
	 * Copies a screen configuration
	 * @param screenConfigBean
	 * @param deep
	 */
	@Override
	public TScreenConfigBean copy(TScreenConfigBean screenConfigBean, boolean deep) {
		try {
			return (BaseTScreenConfig.createTScreenConfig(screenConfigBean).copy(deep)).getBean();
		} catch (TorqueException e) {
			LOGGER.error("Deep " + deep + " copying a screen config failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Deletes a screenConfig by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting a screenConfig for key " + objectID + " failed with: " + e);
		}  
	}

	/**
	 * Verify is a screenConfig can be delete 
	 * @param objectID
	 */
	@Override
	public boolean isDeletable(Integer objectID){
		return true;
	}
	/**
	 * 
	 * Load the cofiguration for parameteres:
	 * projectTypeID
	 * projectID
	 * issueID
	 * from cfg
	 * 
	 * @param cfg
	 * @return
	 */
	@Override
	public List<TScreenConfigBean> load(TScreenConfigBean cfg){
		Integer projectTypeID=null;
		Integer projectID=null;
		Integer issueID=null;
		if(cfg!=null){
			projectTypeID=cfg.getProjectType();
			projectID=cfg.getProject();
			issueID=cfg.getIssueType();
		}
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE,projectTypeID);
		crit.add(PROJECT,projectID);
		crit.add(ISSUETYPE,issueID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading  screenConfigs failed with " + e.getMessage());
			return null;
		}		
	}
	
	/**
	 * Gets the default TScreenConfigBean from the TScreenConfig table
	 * @param action
	 * @return
	 */
	@Override
	public TScreenConfigBean loadDefault(Integer action) {
		List screenConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ACTIONKEY, action);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			screenConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the default screen config for action " + action
					+ " failed with " + e.getMessage(), e);
		}
		if (screenConfigList == null || screenConfigList.isEmpty()) {
			LOGGER.error("No default screen config found for field " + action);
			return null;
		}
		if (screenConfigList.size() > 1) {
			LOGGER.warn("More than one default screen config found for action "
					+ action);
		}
		return ((TScreenConfig)screenConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TScreenConfigBean from the TScreenConfig table by issueType
	 * @param action
	 * @param issueType
	 * @return
	 */
	@Override
	public TScreenConfigBean loadByIssueType(Integer action, Integer issueType) {
		List screenConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ACTIONKEY, action);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			screenConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen config by issueType for action "
					+ action + " and issueType " + issueType + " failed with "
					+ e.getMessage(), e);
		}
		if (screenConfigList == null || screenConfigList.isEmpty()) {
			LOGGER.debug("No screen config found by issueType for action "
					+ action + " and issueType " + issueType);
			return null;
		}
		if (screenConfigList.size() > 1) {
			LOGGER.warn("More then one screen config found by issueType for action "
							+ action + " and issueType " + issueType);
		}
		return ((TScreenConfig)screenConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TScreenConfigBean from the TScreenConfig table by projectType
	 * @param action
	 * @param projectType
	 * @return
	 */
	@Override
	public TScreenConfigBean loadByProjectType(Integer action, Integer projectType) {
		List screenConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ACTIONKEY, action);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			screenConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen config by projectType for action "
					+ action + " and projectType " + projectType + " failed with "
					+ e.getMessage(), e);
		}
		if (screenConfigList == null || screenConfigList.isEmpty()) {
			LOGGER.debug("No screen config found by projectType for action "
					+ action + " and projectType " + projectType);
			return null;
		}
		if (screenConfigList.size() > 1) {
			LOGGER.warn("More then one screen config found by projectType for action "
							+ action + " and projectType " + projectType);
		}
		return ((TScreenConfig)screenConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TScreenConfigBean from the TScreenConfig table by project
	 * @param action
	 * @param project
	 * @return
	 */
	@Override
	public TScreenConfigBean loadByProject(Integer action, Integer project) {
		List screenConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ACTIONKEY, action);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			screenConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen config by project for action "
					+ action + " and project " + project + " failed with "
					+ e.getMessage(), e);
		}
		if (screenConfigList == null || screenConfigList.isEmpty()) {
			LOGGER.debug("No screen config found by project for action "
					+ action + " and project " + project);
			return null;
		}
		if (screenConfigList.size() > 1) {
			LOGGER.warn("More then one screen config found by project for action "
							+ action + " and project " + project);
		}
		return ((TScreenConfig) screenConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TScreenConfigBean from the TScreenConfig table by issueType and projectType
	 * @param action
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	@Override
	public TScreenConfigBean loadByIssueTypeAndProjectType(Integer action, Integer issueType, Integer projectType) {
		List screenConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(ACTIONKEY, action);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			screenConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen config by issueType and projectType for action " + action + 
					" and issueType " + issueType + " and project type " + projectType + " failed with " + e.getMessage(), e);
		}
		if (screenConfigList == null || screenConfigList.isEmpty()) {
			LOGGER.debug("No screen config found by issueType and projecttype for action " +
					action + " issueType " + issueType + " and projecttype" + projectType);
			return null; 
		}
		if (screenConfigList.size()>1) {
			LOGGER.warn("More than one screen config found by issueType and projecttype for action " +
					action + " issueType " + issueType + " and projecttype" + projectType);
		}
		return ((TScreenConfig)screenConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TScreenConfigBean from the TScreenConfig table by issueType and project
	 * @param action
	 * @param issueType
	 * @param project
	 * @return
	 */
	@Override
	public TScreenConfigBean loadByIssueTypeAndProject(Integer action, Integer issueType, Integer project) {
		List screenConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(ACTIONKEY, action);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			screenConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the screen config by issueType  and project for action " + action + 
					" and issueType " + issueType + " and project " + project + " failed with " + e.getMessage(), e);
		}
		if (screenConfigList == null || screenConfigList.isEmpty()) {
			LOGGER.debug("No screen config found by issueType and project for action " +
					action + " issueType " + issueType + " and project" + project);
			return null;
		}
		if (screenConfigList.size()>1) {
			LOGGER.warn("More than one screen config found by issueType and project for action " +
					action + " issueType " + issueType + " and project" + project);
		}
		return ((TScreenConfig)screenConfigList.get(0)).getBean();
	}
	
	/**
	 * Delete the configurations for given node
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 */
	@Override
	public void deleteByIssueType(Integer issueTypeID, Integer projectTypeID, Integer projectID) {
		Criteria crit = new Criteria();
		if(issueTypeID==null){
			//delete configs for all issue type
			crit.add(ISSUETYPE, issueTypeID, Criteria.NOT_EQUAL);
		}else{
			crit.add(ISSUETYPE, issueTypeID);
		}
		
		if (projectTypeID == null) {
			crit.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		} else {
			crit.add(PROJECTTYPE, projectTypeID);
		}
		// add project filter anyway
		if (projectID == null) {
			crit.add(PROJECT, (Object) null, Criteria.ISNULL);
		} else {
			crit.add(PROJECT, projectID);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Delete  screenConfigs failed with " + e.getMessage());
		}
	}
	private List<TScreenConfigBean> convertTorqueListToBeanList(List<TScreenConfig> torqueList) {
		List<TScreenConfigBean> beanList = new LinkedList<TScreenConfigBean>();
		if (torqueList!=null){
			for (TScreenConfig tScreenConfig : torqueList) {
				beanList.add(tScreenConfig.getBean());
			}
		}
		return beanList;
	}
	
	/**
	 * Delete all configs with projects
	 * if the projects is null - delete all all configs which have project set
	 * @param projectIDs
	 */
	@Override
	public void deleteByProjects(List<Integer> projectIDs) {
		Criteria crit = new Criteria();
		if (projectIDs!=null && !projectIDs.isEmpty()) {
			crit.addIn(PROJECT, projectIDs);
		} else {
			//delete all configs witch have project specified
			crit.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Delete  screenConfigs for " + projectIDs + " failed with " + e.getMessage());
		}
	}
	/**
	 * Delete The configs with given projectType pk
	 * if pk is null delete all configs for all projectTypes
	 * @param projectTypeID
	 */
	@Override
	public void deleteByProjectType(Integer projectTypeID) {
		Criteria crit = new Criteria();
		if (projectTypeID!=null) {
			crit.add(PROJECTTYPE,projectTypeID);
		} else {
			//delete all configs witch have project specified
			crit.add(PROJECTTYPE,projectTypeID,Criteria.NOT_EQUAL);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Delete  screenConfigs failed with " + e.getMessage());
		}
	}
	
}
