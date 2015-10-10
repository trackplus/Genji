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
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TItemTransitionBean;
import com.aurel.track.beans.TWfActivityContextParamsBean;
import com.aurel.track.dao.WfActivityContextParamsDAO;
import com.aurel.track.item.workflow.execute.WorkflowContext;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWfActivityContextParamsPeer
    extends com.aurel.track.persist.BaseTWfActivityContextParamsPeer implements WfActivityContextParamsDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TWfActivityContextParamsPeer.class);
	/**
	 * Gets a wfActivityContextParamsBean by workflowContext
	 * @param workflowContext
	 * @return
	 */
	public List<TWfActivityContextParamsBean> loadByContext(WorkflowContext workflowContext) {
		Criteria criteria = new Criteria();
		addWorkflowContextCriteria(criteria, workflowContext);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch(Exception e) {
			LOGGER.error("Loading the params by context " + workflowContext.toString() + " failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Saves a wfActivityContextParamsBean
	 * @param wfActivityContextParamsBean
	 * @return
	 */
	public Integer save(TWfActivityContextParamsBean wfActivityContextParamsBean) {
		try {
			TWfActivityContextParams wfActivityContextParams = TWfActivityContextParams.createTWfActivityContextParams(wfActivityContextParamsBean);
			wfActivityContextParams.save();
			return wfActivityContextParams.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving wfActivityContextParamsBean failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Delete the workflow parameter by primary key
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting an activity parameterfor key " + objectID + " failed with: " + e);
		}
	}

	/**
	 * Deletes all parameters for a workflow context
	 * Is deletable should return true before calling this method
	 * @param workflowContext
	 */
	public void deleteByContext(WorkflowContext workflowContext) {
		Criteria  criteria = new Criteria();
		addWorkflowContextCriteria(criteria, workflowContext);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the wfActivityContextParamsBeans by context " + workflowContext.toString() + " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Filter by workflowContext
	 * @param crit
	 * @param workflowContext
	 * @return
	 */
	private static Criteria addWorkflowContextCriteria(Criteria crit, WorkflowContext workflowContext) {
		Integer itemTypeID = workflowContext.getItemTypeID();
		Integer projectTypeID = workflowContext.getProjectTypeID();
		Integer projectID = workflowContext.getProjectID();
		if (itemTypeID==null) {
			crit.add(ISSUETYPE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(ISSUETYPE, itemTypeID);
		}
		if (projectTypeID==null) {
			crit.add(PROJECTTYPE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PROJECTTYPE, projectTypeID);
		}
		if (projectID==null) {
			crit.add(PROJECT, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PROJECT, projectID);
		}
		return crit;
	}
	
	/**
	 * Delete the configurations for given node
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 */
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
			LOGGER.error("Delete workflowConfigs by issueType " + issueTypeID + " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Delete all configs with projects
	 * if the projects is null - delete all all configs which have project set
	 * @param projectIDs
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
			LOGGER.error("Delete workflowConfigs by projects " + projectIDs +  " failed with " + e.getMessage(), e);
		}
	}
	
	/**
	 * Delete all configs with given projectType pk
	 * if the project pk is null - delete all all configs which have projectType set
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
			LOGGER.error("Delete  workflowConfigs by projectType " + projectTypeID + " failed with " + e.getMessage(), e);
		}
	}
	
	
	private List<TWfActivityContextParamsBean> convertTorqueListToBeanList(List<TWfActivityContextParams> torqueList) {
		List<TWfActivityContextParamsBean> beanList = new LinkedList<TWfActivityContextParamsBean>();
		if (torqueList!=null) {
			for (TWfActivityContextParams wfActivityContextParams : torqueList) {
				beanList.add(wfActivityContextParams.getBean());
			}
		}
		return beanList;
	}
}
