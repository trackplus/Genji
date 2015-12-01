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

import com.aurel.track.beans.TWorkflowConnectBean;
import com.aurel.track.dao.WorkflowConnectDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkflowConnectPeer
    extends com.aurel.track.persist.BaseTWorkflowConnectPeer implements WorkflowConnectDAO
{
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkflowConnectPeer.class);
	
	/**
	 * Gets a TWorkflowConnectBean by promary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TWorkflowConnectBean loadByPrimaryKey(Integer objectID) {
		TWorkflowConnect tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a screenConfig by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	/**
	 * Gets all TWorkflowConnectBeans
	 * @return
	 */
	@Override
	public List<TWorkflowConnectBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workflowConfigs failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Save a workflowConfig
	 * @param workflowConfig
	 * @return
	 */
	@Override
	public Integer save(TWorkflowConnectBean workflowConfig) {
		try {
			TWorkflowConnect tobject = TWorkflowConnect.createTWorkflowConnect(workflowConfig);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a workflowConfig failed with " + e.getMessage());
			return null;
		}		
	}

	/**
	 * Deletes a screenConfig from the TScreenConfig table
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting a workflow config for key " + objectID + " failed with: " + e);
		}
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
	public List<TWorkflowConnectBean> load(TWorkflowConnectBean cfg) {
		Integer projectTypeID=null;
		Integer projectID=null;
		Integer issueTypeID=null;
		if(cfg!=null){
			projectTypeID=cfg.getProjectType();
			projectID=cfg.getProject();
			issueTypeID=cfg.getIssueType();
		}
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE,projectTypeID);
		crit.add(PROJECT,projectID);
		crit.add(ISSUETYPE,issueTypeID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading  workflowConfigs failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets the default TWorkflowConnectBean
	 * @return
	 */
	@Override
	public TWorkflowConnectBean loadDefault() {
		List<TWorkflowConnect> workflowConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			workflowConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the default window config failed with " + e.getMessage());
		}
		if (workflowConfigList == null || workflowConfigList.isEmpty()) {
			LOGGER.debug("No default workflow config found");
			return null;
		}
		if (workflowConfigList.size() > 1) {
			LOGGER.warn("More than one default workflow config found");
		}
		return (workflowConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TWorkflowConnectBean by issueType
	 * @param issueType
	 * @return
	 */
	@Override
	public TWorkflowConnectBean loadByIssueType(Integer issueType) {
		List<TWorkflowConnect> workflowConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			workflowConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the workflow config by issueType " +
					issueType + " failed with " + e.getMessage(), e);
		}
		if (workflowConfigList == null || workflowConfigList.isEmpty()) {
			LOGGER.debug("No workflow config found by issueType " + issueType);
			return null;
		}
		if (workflowConfigList.size() > 1) {
			LOGGER.warn("More then one workflow config found by issueType " + issueType);
		}
		return (workflowConfigList.get(0)).getBean();
	}

	@Override
	public List<TWorkflowConnectBean> loadByIssueTypes(Object[] issueTypeIDs){
		if (issueTypeIDs!=null && issueTypeIDs.length!=0) {
			Criteria crit = new Criteria();
			crit.addIn(ISSUETYPE, issueTypeIDs);
			crit.add(PROJECT, (Object)null, Criteria.ISNULL);
			crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
			try {
				return convertTorqueListToBeanList(doSelect(crit));
			} catch (TorqueException e) {
				LOGGER.error("Loading the field configs by issueTypes  failed with  " + e.getMessage());
				return null;
			}
		}
		return new LinkedList<TWorkflowConnectBean>();
	}

	/**
	 * Gets a TWorkflowConnectBean by projectType
	 * @param projectType
	 * @return
	 */
	@Override
	public TWorkflowConnectBean loadByProjectType(Integer projectType) {
		List<TWorkflowConnect> workflowConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			workflowConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the workflow config by projectType "
					+ projectType + " failed with " + e.getMessage(), e);
		}
		if (workflowConfigList == null || workflowConfigList.isEmpty()) {
			LOGGER.debug("No workflow config found by projectType " + projectType);
			return null;
		}
		if (workflowConfigList.size() > 1) {
			LOGGER.warn("More then one workflow config found by projectType " + projectType);
		}
		return ((TWorkflowConnect)workflowConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TWorkflowConnectBean by project
	 * @param project
	 * @return
	 */
	@Override
	public TWorkflowConnectBean loadByProject(Integer project) {
		List<TWorkflowConnect> workflowConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			workflowConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the workflow config by project " + project + " failed with "
					+ e.getMessage(), e);
		}
		if (workflowConfigList == null || workflowConfigList.isEmpty()) {
			LOGGER.debug("No workflow config found by project " + project);
			return null;
		}
		if (workflowConfigList.size() > 1) {
			LOGGER.warn("More then one workflow config found by project " + project);
		}
		return ((TWorkflowConnect) workflowConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets all the TWorkflowConnectBean for issueType, projectType and project  
	 * @param issueType can be null, in this case the result it is not filtered by issueType
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @return
	 */
	@Override
	public List<TWorkflowConnectBean> loadAllByIssueType(Integer issueType, Integer projectType, Integer project) {
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
			LOGGER.error("Loading the workfow assignments by issueType " + issueType + 
					" and projectType " + projectType + " and project " + project + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets all the TWorkflowConnectBean by projectType
	 * @param projectType if specified filter by projectType otherwise only be sure to not to be null (all projectType specific configurations)
	 * @param excludeWithIssueType
	 * @return
	 */
	@Override
	public List<TWorkflowConnectBean> loadAllByProjectType(Integer projectType, boolean excludeWithIssueType) {
		Criteria criteria = new Criteria();
		if (projectType==null) {
			criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECTTYPE, projectType);
		}
		if (excludeWithIssueType) {
			criteria.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the workfow assignments by projectType " + projectType + "  failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets TWorkflowConnectBean by project
	 * @param project project if specified filter by project otherwise only be sure to not to be null (all project specific configurations)
	 * @param excludeWithIssueType
	 * @return
	 */
	@Override
	public List<TWorkflowConnectBean> loadAllByProject(Integer project, boolean excludeWithIssueType) {
		Criteria criteria = new Criteria();
		if (project==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECT, project);
		}
		if (excludeWithIssueType) {
			criteria.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the workfow configs by project " + project + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets TWorkflowConnectBean by project
	 * @param projects if specified filter by projects otherwise only be sure to not to be null (all project specific configurations)
	 * @return
	 */
	@Override
	public List<TWorkflowConnectBean> loadAllByProjects(List<Integer> projects) {
		Criteria criteria = new Criteria();
		if (projects==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.addIn(PROJECT, projects);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the workfow configs by project " + projects + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets a TWorkflowConnectBean by issueType and projectType
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	@Override
	public TWorkflowConnectBean loadByIssueTypeAndProjectType(Integer issueType, Integer projectType) {
		List workflowConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			workflowConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the workflow config by issueType " + issueType + " and projectType " + projectType + " failed with " + e.getMessage());
		}
		if (workflowConfigList == null || workflowConfigList.isEmpty()) {
			LOGGER.debug("No workflow config found by issueType " + issueType + " and projecttype" + projectType);
			return null; 
		}
		if (workflowConfigList.size()>1) {
			LOGGER.warn("More than one workflow config found by issueType " + issueType + " and projecttype" + projectType);
		}
		return ((TWorkflowConnect)workflowConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TWorkflowConnectBean from the TScreenConfig table by issueType and project
	 * @param issueType
	 * @param project
	 * @return
	 */
	@Override
	public TWorkflowConnectBean loadByIssueTypeAndProject(Integer issueType, Integer project) {
		List workflowConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			workflowConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the workflow config by issueType " + issueType +
					" and project " + project + " failed with " + e.getMessage(), e);
		}
		if (workflowConfigList == null || workflowConfigList.isEmpty()) {
			LOGGER.debug("No workflow workflow found by issueType " + issueType + " and project" + project);
			return null;
		}
		if (workflowConfigList.size()>1) {
			LOGGER.warn("More than one workflow config found by issueType " + issueType + " and project" + project);
		}
		return ((TWorkflowConnect)workflowConfigList.get(0)).getBean();
	}

	
	/**
	 * Delete the configurations for given node
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 */
	@Override
	public void deleteByIssueType(Integer issueTypeID) {
		Criteria crit = new Criteria();
		if(issueTypeID==null){
			//delete configs for all issue type
			crit.add(ISSUETYPE, issueTypeID, Criteria.NOT_EQUAL);
		}else{
			crit.add(ISSUETYPE, issueTypeID);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Delete workflowConfigs by issueType " + issueTypeID + " failed with " + e.getMessage());
		}
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
			LOGGER.error("Delete workflowConfigs by projects " + projectIDs +  " failed with " + e.getMessage());
		}
	}
	
	/**
	 * Delete all configs with given projectType pk
	 * if the project pk is null - delete all all configs which have projectType set
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
			LOGGER.error("Delete  workflowConfigs by projectType " + projectTypeID + " failed with " + e.getMessage());
		}
	}
	
	private List<TWorkflowConnectBean> convertTorqueListToBeanList(List<TWorkflowConnect> torqueList) {
		List<TWorkflowConnectBean> beanList = new LinkedList<TWorkflowConnectBean>();
		if (torqueList!=null){
			for (TWorkflowConnect workflowConnect : torqueList) {
				beanList.add(workflowConnect.getBean());
			}
		}
		return beanList;
	}
}
