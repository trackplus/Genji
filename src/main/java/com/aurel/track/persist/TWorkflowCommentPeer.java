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

import com.aurel.track.beans.TWorkflowCommentBean;
import com.aurel.track.dao.WorkflowCommentDAO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import java.util.LinkedList;
import java.util.List;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TWorkflowCommentPeer extends com.aurel.track.persist.BaseTWorkflowCommentPeer implements WorkflowCommentDAO{

	private static final Logger LOGGER = LogManager.getLogger(TWorkflowCommentPeer.class);

	@Override
	public TWorkflowCommentBean loadByPrimaryKey(Integer objectID) {
		TWorkflowComment tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a workflow Comment by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}

	@Override
	public List<TWorkflowCommentBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all workflow comments failed with " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<TWorkflowCommentBean> loadByWorkflow(Integer workflowID) {
		Criteria crit = new Criteria();
		crit.add(WORKFLOW,workflowID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading workflow comments by workflow:"+workflowID+" failed with " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Integer save(TWorkflowCommentBean workflowCommentBean) {
		try {
			TWorkflowComment tobject = TWorkflowComment.createTWorkflowComment(workflowCommentBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a workflow Comment failed with " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void delete(Integer objectID) {
		Criteria  criteria = new Criteria();
		criteria.add(OBJECTID, objectID);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the workflow comment by id: "+objectID+" failed with " + e.getMessage(), e);
		}
	}

	private List<TWorkflowCommentBean> convertTorqueListToBeanList(List<TWorkflowComment> torqueList) {
		List<TWorkflowCommentBean> beanList = new LinkedList<TWorkflowCommentBean>();
		if (torqueList!=null){
			for (TWorkflowComment workflowComment : torqueList) {
				beanList.add(workflowComment.getBean());
			}
		}
		return beanList;
	}
}
