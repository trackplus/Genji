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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.dao.MailTemplateConfigDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TMailTemplateConfigPeer
    extends com.aurel.track.persist.BaseTMailTemplateConfigPeer implements MailTemplateConfigDAO {

	private static final Logger LOGGER = LogManager.getLogger(TMailTemplateConfigPeer.class);
	public static final long serialVersionUID = 400L;

	/**
	 * Loads the screenConfig by primary key
	 * @param objectID
	 * @return
	 */
	public TMailTemplateConfigBean loadByPrimaryKey(Integer objectID) {
		TMailTemplateConfig tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a mailConfig by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	/**
	 * Gets the mailTemplateConfigBeans bu action from the TMailTemplateConfig table
	 * @param actionID
	 * @return
	 */
	public List<TMailTemplateConfigBean> loadByEventKey(Integer actionID) {
		Criteria crit = new Criteria();
		crit.add(EVENTKEY, actionID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all mailTemplateConfigs failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Load the mailTemplate configurations for projects
	 * @param projectIDs
	 * @return
	 */
	/*public List loadByProjects(Object[] projectIDs) {
		List torqueList = null;
		if (projectIDs!=null && projectIDs.length!=0) {
			Criteria crit = new Criteria();
			crit.addIn(PROJECT, projectIDs);
			crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
			try {
				torqueList = doSelect(crit);
			} catch (TorqueException e) {
				LOGGER.error("Loading mailTemplateConfigs by projects failed with " + e.getMessage(), e);
			}
		}
		return convertTorqueListToBeanList(torqueList);
	}*/

	/**
	 * Load the mailTemplate configurations for project types
	 * @param projectTypeIDs
	 * @return
	 */
	/*public List loadByProjectTypes(Object[] projectTypeIDs) {
		List torqueList = null;
		if (projectTypeIDs!=null && projectTypeIDs.length!=0) {
			Criteria crit = new Criteria();
			crit.addIn(PROJECTTYPE, projectTypeIDs);
			crit.add(PROJECT, (Object)null, Criteria.ISNULL);
			try {
				torqueList = doSelect(crit);
			} catch (TorqueException e) {
				LOGGER.error("Loading mailTemplateConfigs by project types failed with " + e.getMessage(), e);
			}
		}
		return convertTorqueListToBeanList(torqueList);
	}*/

	/**
	 * Load the mailTemplate configurations for issue types
	 * @param issueTypeIDs
	 * @return
	 */
	/*public List loadByIssueTypes(Object[] issueTypeIDs) {
		List torqueList = null;
		if (issueTypeIDs!=null && issueTypeIDs.length!=0) {
			Criteria crit = new Criteria();
			crit.addIn(ISSUETYPE, issueTypeIDs);
			crit.add(PROJECT, (Object)null, Criteria.ISNULL);
			crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
			try {
				torqueList = doSelect(crit);
			} catch (TorqueException e) {
				LOGGER.error("Loading mailTemplateConfigs by issue types failed with " + e.getMessage(), e);
			}
		}
		return convertTorqueListToBeanList(torqueList);
	}*/

	/**
	 * Load the global mailTemplates
	 * @return
	 */
	public List<TMailTemplateConfigBean> loadDefaults() {
		Criteria crit = new Criteria();
		crit.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		crit.add(PROJECT, (Object)null, Criteria.ISNULL);
		crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading mailTemplateConfigs by issue types failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Loads all mailTemplateConfigs from TMailTemplateConfig table
	 * @return
	 */
	/*public List loadAll() {
		List torqueList = null;
		Criteria crit = new Criteria();
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading all mailTemplateConfigs failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(torqueList);
	}*/

	/**
	 * Gets all the TMailTemplateConfigBeans for issueType, projectType and project
	 * @param issueType can be null, in this case the result it is not filtered by issueType
	 * @param projectType include into filtering even if null
	 * @param project include into filtering even if null
	 * @return
	 */
	public List<TMailTemplateConfigBean> loadAllByIssueType(Integer issueType, Integer projectType, Integer project) {
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
			LOGGER.error("Loading the mailTemplate assignments by issueType for issueType " + issueType +
					" and projectType " + projectType + " and project " + project  +  " failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets all the TMailTemplateConfigBeans which have a projectType specified
	 * @param projectType if specified filter by projectType otherwise only be sure to not to be null (all projectType specific configurations)
	 * @return
	 */
	public List<TMailTemplateConfigBean> loadAllByProjectType(Integer projectType) {
		Criteria criteria = new Criteria();
		if (projectType==null) {
			criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECTTYPE, projectType);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate assignments by projectType " + projectType + "  failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets TMailTemplateConfigBeans by project
	 * @param project
	 * @return
	 */
	public List<TMailTemplateConfigBean> loadAllByProject(Integer project) {
		Criteria criteria = new Criteria();
		if (project==null) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.add(PROJECT, project);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate configs by project " + project + " failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets TMailTemplateConfigBeans by projects
	 * @param projects
	 * @return
	 */
	public List<TMailTemplateConfigBean> loadAllByProjects(List<Integer> projects) {
		Criteria criteria = new Criteria();
		if (projects==null || projects.isEmpty()) {
			criteria.add(PROJECT, (Object)null, Criteria.ISNOTNULL);
		} else {
			criteria.addIn(PROJECT, projects);
		}
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate configs by project " + projects + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Saves a mailTemplateConfig in the TMailTemplateConfig table
	 * @param bean
	 * @return
	 */
	public Integer save(TMailTemplateConfigBean bean){
		try {
			TMailTemplateConfig tobject = BaseTMailTemplateConfig.createTMailTemplateConfig(bean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a mailTemplateConfig failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Copies a mailTemplate configuration
	 * @param mailTemplateConfigBean
	 * @param deep
	 */
	public TMailTemplateConfigBean copy(TMailTemplateConfigBean mailTemplateConfigBean, boolean deep) {
		try {
			return (BaseTMailTemplateConfig.createTMailTemplateConfig(mailTemplateConfigBean).copy(deep)).getBean();
		} catch (TorqueException e) {
			LOGGER.error("Deep " + deep + " copying a mailTemplate config failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Deletes a mailTemplateConfig by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting a mailTemplateConfig for key " + objectID + " failed with: " + e);
		}
	}

	/**
	 * Verify is a mailTemplateConfig can be delete
	 * @param objectID
	 */
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
	public List<TMailTemplateConfigBean> load(TMailTemplateConfigBean cfg){
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
			LOGGER.error("Loading  mailTemplateConfigs failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets the default TMailTemplateConfigBean from the TMailTemplateConfig table
	 * @param event
	 * @return
	 */
	public TMailTemplateConfigBean loadDefault(Integer event) {
		List<TMailTemplateConfig> mailTemplateConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, event);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the default mailTemplate config for event " + event
					+ " failed with " + e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.warn("No default mailTemplate config found for field " + event);
			return null;
		}
		if (mailTemplateConfigList.size() > 1) {
			LOGGER.warn("More than one default mailTemplate config found for event "
					+ event);
		}
		return ((TMailTemplateConfig)mailTemplateConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by issueType
	 * @param event
	 * @param issueType
	 * @return
	 */
	public TMailTemplateConfigBean loadByIssueType(Integer event, Integer issueType) {
		List<TMailTemplateConfig> mailTemplateConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, event);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate config by issueType for event "
					+ event + " and issueType " + issueType + " failed with "
					+ e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.debug("No mailTemplate config found by issueType for event "
					+ event + " and issueType " + issueType);
			return null;
		}
		if (mailTemplateConfigList.size() > 1) {
			LOGGER.warn("More then one mailTemplate config found by issueType for event "
					+ event + " and issueType " + issueType);
		}
		return ((TMailTemplateConfig)mailTemplateConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by projectType
	 * @param event
	 * @param projectType
	 * @return
	 */
	public TMailTemplateConfigBean loadByProjectType(Integer event, Integer projectType) {
		List<TMailTemplateConfig> mailTemplateConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, event);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object) null, Criteria.ISNULL);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate config by projectType for event "
					+ event + " and projectType " + projectType + " failed with "
					+ e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.debug("No mailTemplate config found by projectType for event "
					+ event + " and projectType " + projectType);
			return null;
		}
		if (mailTemplateConfigList.size() > 1) {
			LOGGER.warn("More then one mailTemplate config found by projectType for event "
					+ event + " and projectType " + projectType);
		}
		return ((TMailTemplateConfig)mailTemplateConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by project
	 * @param event
	 * @param project
	 * @return
	 */
	public TMailTemplateConfigBean loadByProject(Integer event, Integer project) {
		List<TMailTemplateConfig> mailTemplateConfigList = null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, event);
		criteria.add(ISSUETYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECTTYPE, (Object) null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate config by project for event "
					+ event + " and project " + project + " failed with "
					+ e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.debug("No mailTemplate config found by project for event "
					+ event + " and project " + project);
			return null;
		}
		if (mailTemplateConfigList.size() > 1) {
			LOGGER.warn("More then one mailTemplate config found by project for event "
					+ event + " and project " + project);
		}
		return ((TMailTemplateConfig) mailTemplateConfigList.get(0)).getBean();
	}
	
	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by issueType and projectType
	 * @param event
	 * @param issueType
	 * @param projectType
	 * @return
	 */
	public TMailTemplateConfigBean loadByIssueTypeAndProjectType(Integer event, Integer issueType, Integer projectType) {
		List<TMailTemplateConfig> mailTemplateConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, event);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, projectType);
		criteria.add(PROJECT, (Object)null, Criteria.ISNULL);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate config by issueType and projectType for event " + event +
					" and issueType " + issueType + " and project type " + projectType + " failed with " + e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.debug("No mailTemplate config found by issueType and projecttype for event " +
					event + " issueType " + issueType + " and projecttype" + projectType);
			return null;
		}
		if (mailTemplateConfigList.size()>1) {
			LOGGER.warn("More than one mailTemplate config found by issueType and projecttype for event " +
					event + " issueType " + issueType + " and projecttype" + projectType);
		}
		return ((TMailTemplateConfig)mailTemplateConfigList.get(0)).getBean();
	}

	/**
	 * Gets a TMailTemplateConfigBean from the TMailTemplateConfig table by issueType and project
	 * @param event
	 * @param issueType
	 * @param project
	 * @return
	 */
	public TMailTemplateConfigBean loadByIssueTypeAndProject(Integer event, Integer issueType, Integer project) {
		List<TMailTemplateConfig> mailTemplateConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, event);
		criteria.add(ISSUETYPE, issueType);
		criteria.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, project);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate config by issueType  and project for event " + event +
					" and issueType " + issueType + " and project " + project + " failed with " + e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.debug("No mailTemplate config found by issueType and project for event " +
					event + " issueType " + issueType + " and project" + project);
			return null;
		}
		if (mailTemplateConfigList.size()>1) {
			LOGGER.warn("More than one mailTemplate config found by issueType and project for event " +
					event + " issueType " + issueType + " and project" + project);
		}
		return ((TMailTemplateConfig)mailTemplateConfigList.get(0)).getBean();
	}
	public TMailTemplateConfigBean loadByEventAndTemplate(Integer eventType,Integer mailTemplateID){
		List<TMailTemplateConfig> mailTemplateConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, eventType);
		criteria.add(MAILTEMPLATE, mailTemplateID);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate config by eventType "+eventType+"  and mailTemplateID "+mailTemplateID+
					" failed with " + e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.debug("No mailTemplate config found by eventType " +
					" and mailTemplateID " + mailTemplateID);
			return null;
		}
		if (mailTemplateConfigList.size()>1) {
			LOGGER.warn("More than one mailTemplate config found by event " +
					eventType + " and mailTemplateID " + mailTemplateID);
			//delete extra configs. This was generated from previous build bug
			for(int i=1;i<mailTemplateConfigList.size();i++){
				Integer deleteID=((TMailTemplateConfig)mailTemplateConfigList.get(i)).getObjectID();
				LOGGER.warn("delete config by id:"+deleteID);
				delete(deleteID);
			}
		}
		return ((TMailTemplateConfig)mailTemplateConfigList.get(0)).getBean();
	}
	public TMailTemplateConfigBean loadByEvent(Integer eventType){
		List<TMailTemplateConfig> mailTemplateConfigList =  null;
		Criteria criteria = new Criteria();
		criteria.add(EVENTKEY, eventType);
		try {
			mailTemplateConfigList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the mailTemplate config by eventType "+eventType+" failed with " + e.getMessage(), e);
		}
		if (mailTemplateConfigList == null || mailTemplateConfigList.isEmpty()) {
			LOGGER.debug("No mailTemplate config found by eventType ");
			return null;
		}
		if (mailTemplateConfigList.size()>1) {
			LOGGER.warn("More than one mailTemplate config found by event " +eventType);
			//delete extra configs. This was generated from previous build bug
			for(int i=1;i<mailTemplateConfigList.size();i++){
				Integer deleteID=((TMailTemplateConfig)mailTemplateConfigList.get(i)).getObjectID();
				LOGGER.warn("delete config by id:"+deleteID);
				delete(deleteID);
			}
		}
		return ((TMailTemplateConfig)mailTemplateConfigList.get(0)).getBean();
	}

	/**
	 * Delete the configurations for given node
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 */
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
			LOGGER.error("Delete  mailTemplateConfigs failed with " + e.getMessage(), e);
		}
	}
	private List<TMailTemplateConfigBean> convertTorqueListToBeanList(List<TMailTemplateConfig> torqueList) {
		List<TMailTemplateConfigBean> beanList = new LinkedList<TMailTemplateConfigBean>();
		if (torqueList!=null){
			for (TMailTemplateConfig tMailTemplateConfig : torqueList) {
				beanList.add(tMailTemplateConfig.getBean());
			}
		}
		return beanList;
	}
	/**
	 * Delete The configs with given project pk
	 * if pk is null delete all configs for all projects
	 * @param projectID
	 */
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
			LOGGER.error("Delete mailTemplateConfigs for " + projectIDs + " failed with " + e.getMessage(), e);
		}
	}
	/**
	 * Delete The configs with given projectType pk
	 * if pk is null delete all configs for all projectTypes
	 * @param projectTypeID
	 */
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
			LOGGER.error("Delete  mailTemplateConfigs failed with " + e.getMessage(), e);
		}
	}
}
