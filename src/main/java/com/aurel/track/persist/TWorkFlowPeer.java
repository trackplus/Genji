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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TWorkFlowBean;
import com.aurel.track.dao.WorkflowDAO;

/** 
 * @author Sergej Redel <sergej@schoene-hochzeit.com>
 * @author Martin Bierkoch <martin.bierkoch@gmx.de>
 * @author Joerg Friedrich
 * @version $Revision: 1795 $ $Date: 2015-11-12 13:07:18 +0100 (Do, 12 Nov 2015) $
 */

public class TWorkFlowPeer
	extends com.aurel.track.persist.BaseTWorkFlowPeer
	implements WorkflowDAO
{
	private static final long serialVersionUID = -8749177320783708897L;
	private static final Logger LOGGER = LogManager.getLogger(TWorkFlowPeer.class);

	private static Class[] deletePeerClasses = {		 
		TWorkFlowCategoryPeer.class,
		TWorkFlowRolePeer.class		
	};
	
	private static String[] deleteFields = {		 
		TWorkFlowCategoryPeer.WORKFLOW,
		TWorkFlowRolePeer.WORKFLOW		
	};
	

	
	
	
	/**
	 *Gets the workflow transition from stateFrom to stateTo for projectType and issueType
	 * @param projectType
	 * @param issueType
	 * @param stateFrom
	 * @param stateTo
	 */
	@Override
	public TWorkFlowBean getWorkflow(Integer projectType, 
			Integer  issueType,	Integer stateFrom, Integer stateTo) {
		List<TWorkFlow> workflows = new ArrayList();
		if (projectType==null || issueType==null || stateFrom==null || stateTo==null) {
			LOGGER.error("All the following fields should have valid values: " + 
					"projectType=" + projectType + " issueType="+issueType + 
					" stateFrom=" + stateFrom +" stateTo=" + stateTo);
			return null;
		}
		Criteria criteria = new Criteria();
		criteria.add(BaseTWorkFlowPeer.STATEFROM, stateFrom);
		criteria.add(BaseTWorkFlowPeer.STATETO, stateTo);
		criteria.add(BaseTWorkFlowPeer.PROJECTTYPE, projectType);
		criteria.addJoin(BaseTWorkFlowPeer.OBJECTID, BaseTWorkFlowCategoryPeer.WORKFLOW);
		criteria.add(BaseTWorkFlowCategoryPeer.CATEGORY, issueType);
	 	try {
	 		workflows = doSelect(criteria);
		} catch (Exception e) {
			LOGGER.error("Getting the workflows for projectType " + projectType + ", issueType " + issueType 
					+ ", stateFrom " + stateFrom + " stateTo " + stateTo + " failed with " + e.getMessage(), e);
		}
		if (workflows!=null && !workflows.isEmpty()) {
			return workflows.get(0).getBean();
		}
		return null;
	}
	
	/**
	 * Load all workflows
	 * @return
	 */
	@Override
	public List<TWorkFlowBean> loadAll() {
		try {
			Criteria crit = new Criteria();
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch (Exception e) {
			LOGGER.error("Loading all workflows for migration failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Convert the torque objects to beans
	 * @param torqueList
	 * @return
	 */
	private static List<TWorkFlowBean> convertTorqueListToBeanList(List<TWorkFlow> torqueList) {
		List<TWorkFlowBean> beanList = new ArrayList<TWorkFlowBean>();
		if (torqueList!=null){
			for (TWorkFlow tWorkFlow : torqueList) {
				beanList.add(tWorkFlow.getBean());
			}
		}
		return beanList;
	}
}
