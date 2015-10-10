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
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TWorkflowDefBean;
import com.aurel.track.dao.WorkflowdefDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkflowDefPeer
    extends com.aurel.track.persist.BaseTWorkflowDefPeer implements WorkflowdefDAO
{
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkflowDefPeer.class);
	
	private static Class[] deletePeerClasses = {
		TWorkflowConnectPeer.class,
		TWorkflowTransitionPeer.class,
		TWorkflowStationPeer.class,
		TWorkflowCommentPeer.class,
		TWorkflowDefPeer.class
	};

	private static String[] deleteFields = {
		TWorkflowConnectPeer.WORKFLOW,
		TWorkflowTransitionPeer.WORKFLOW,
		TWorkflowStationPeer.WORKFLOW,
		TWorkflowCommentPeer.WORKFLOW,
		TWorkflowDefPeer.OBJECTID
	};
	/**
	 * Loads the screen by primary key
	 * @param objectID
	 * @return
	 */
	public TWorkflowDefBean loadByPrimaryKey(Integer objectID) {
		TWorkflowDef tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a workflow by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}
	
	/**
	 * Loads all workflows 
	 * @return 
	 */
	public List<TWorkflowDefBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workflows failed with " + e.getMessage(), e);
			return null;
		}
	}
	public List<TWorkflowDefBean> loadByIDs(List<Integer> ids) {
		Criteria crit = new Criteria();
		crit.addIn(OBJECTID, ids);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading mail templates failed with " + e.getMessage(), e);
			return null;
		}
	}

	
	/**
	 * Saves a workflow
	 * @param workflowDefBean
	 * @return
	 */
	public Integer save(TWorkflowDefBean workflowDefBean) {
		try {
			TWorkflowDef tobject = TWorkflowDef.createTWorkflowDef(workflowDefBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a workflow failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a screen by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	private List<TWorkflowDefBean> convertTorqueListToBeanList(List<TWorkflowDef> torqueList) {
		List<TWorkflowDefBean> beanList = new LinkedList<TWorkflowDefBean>();
		if (torqueList!=null){
			for (TWorkflowDef workflowDef : torqueList) {
				beanList.add(workflowDef.getBean());
			}
		}
		return beanList;
	}
}
