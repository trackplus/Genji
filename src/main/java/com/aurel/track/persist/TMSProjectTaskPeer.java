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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.CriteriaUtil;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.dao.MsProjectTaskDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TMSProjectTaskPeer
    extends com.aurel.track.persist.BaseTMSProjectTaskPeer
    implements MsProjectTaskDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TMSProjectTaskPeer.class);
	
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads an MsProjectTaskBean by primary key
	 * @param objectID
	 * @return 
	 */
	@Override
	public TMSProjectTaskBean loadByPrimaryKey(Integer objectID) {
		TMSProjectTask msProjectTask = null;
    	try {
    		msProjectTask = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of an msProject task by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (msProjectTask!=null) {
			return msProjectTask.getBean();
		}
		return null;
	}
	
	/**
	 * Loads an MsProjectTaskBean by workItemID
	 * @param workItemID
	 * @return
	 */
	@Override
	public TMSProjectTaskBean loadByPrimaryWorkItemID(Integer workItemID) {
		List msProjectTasks = new ArrayList();
        Criteria crit = new Criteria();        
        crit.add(BaseTMSProjectTaskPeer.WORKITEM, WORKITEM);
                                       
        try {
        	msProjectTasks = doSelect(crit);
    	} catch(Exception e) {
    		LOGGER.error("Loading MsProjectTaskBean by workItemID " + workItemID + " failed with " + e.getMessage());
    	}
    	if (msProjectTasks==null || msProjectTasks.isEmpty()) {
    		return null;
    	}
    	return ((TMSProjectTask)msProjectTasks.get(0)).getBean();
	}
	
	/**
	 * Loads an MsProjectTaskBean by UID
	 * @param UID
	 * @return 
	 */
	@Override
	public TMSProjectTaskBean loadByUIDAndRelease(Integer UID, Integer entityID, int entityType) {
		List msProjectTasks = new ArrayList();
        Criteria crit = new Criteria();        
        crit.add(TMSProjectTaskPeer.UNIQUEID, UID);
        crit.addJoin(TWorkItemPeer.WORKITEMKEY, WORKITEM);
        switch (entityType) {
		case SystemFields.RELEASESCHEDULED:
			crit.add(TWorkItemPeer.RELSCHEDULEDKEY, entityID);
			break;
		case SystemFields.PROJECT:
        	crit.add(TWorkItemPeer.PROJECTKEY, entityID);	
			break;
		default:
			return null;			
		}                       
        try {
        	msProjectTasks = doSelect(crit);
    	} catch(Exception e) {
    		LOGGER.error("Loading MsProjectTaskBean by UID " + UID + " entityID " + entityID + " entityType " + entityType + " failed with " + e.getMessage());
    	}
    	if (msProjectTasks==null || msProjectTasks.isEmpty()) {
    		return null;
    	}
    	return ((TMSProjectTask)msProjectTasks.get(0)).getBean();
	}
	
	/**
	 * Loads list of TMSProjectTaskBeans for a release/project for import
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	@Override
	public List<TMSProjectTaskBean> loadMsProjectTasksForImport(Integer entityID, int entityType) {
		List msProjectTasks = new ArrayList();
        Criteria crit = new Criteria();                
        crit.addJoin(TWorkItemPeer.WORKITEMKEY, WORKITEM);
        switch (entityType) {
		case SystemFields.RELEASESCHEDULED:
			crit.add(TWorkItemPeer.RELSCHEDULEDKEY, entityID);
			break;
		case SystemFields.PROJECT:
        	crit.add(TWorkItemPeer.PROJECTKEY, entityID);	
			break;
		default:
			return null;			
		}
        crit.addAscendingOrderByColumn(OUTLINENUMBER);                
        try {
        	msProjectTasks = doSelect(crit);
    	} catch(Exception e) {
    		LOGGER.error("Loading MsProjectTaskBean by entityID " + entityID + " entityType " + entityType + " failed with " + e.getMessage());
    	}    	
    	return convertTorqueListToBeanList(msProjectTasks);
	}
	
	/**
	 * Loads list of TMSProjectTaskBeans for a release/project for export
	 * @param entityID
	 * @param entityType
	 * @param notClosed
	 * @return
	 */
	@Override
	public List<TMSProjectTaskBean> loadMsProjectTasksForExport(Integer entityID, int entityType, boolean notClosed) {
		List msProjectTasks = new ArrayList();
        Criteria crit = new Criteria();                
        crit.addJoin(TWorkItemPeer.WORKITEMKEY, WORKITEM);
        crit.addJoin(TWorkItemPeer.CATEGORYKEY, BaseTListTypePeer.PKEY);
        crit.add(BaseTListTypePeer.TYPEFLAG, TListTypeBean.TYPEFLAGS.TASK);
        switch (entityType) {
		case SystemFields.RELEASESCHEDULED:
			crit.add(TWorkItemPeer.RELSCHEDULEDKEY, entityID);
			break;
		case SystemFields.PROJECT:
        	crit.add(TWorkItemPeer.PROJECTKEY, entityID);	
			break;
		default:
			return null;			
		}
        CriteriaUtil.addArchivedDeletedFilter(crit);        
        if (notClosed) {
        	CriteriaUtil.addStateFlagUnclosedCriteria(crit);
        }
        crit.addAscendingOrderByColumn(OUTLINENUMBER);                
        try {
        	msProjectTasks = doSelect(crit);
    	} catch(Exception e) {
    		LOGGER.error("Loading MsProjectTaskBean by entityID " + entityID + " entityType " + entityType + " failed with " + e.getMessage());
    	}    	
    	return convertTorqueListToBeanList(msProjectTasks);
	}
	
	/**
	 * Saves an MsProjectTaskBean
	 * @param mSProjectTaskBean
	 * @return
	 */
	@Override
	public Integer save(TMSProjectTaskBean mSProjectTaskBean) {
		try {			
			TMSProjectTask msProjectTask = BaseTMSProjectTask.createTMSProjectTask(mSProjectTaskBean);
			msProjectTask.save();
			return msProjectTask.getObjectID();						
		} catch (Exception e) {
			LOGGER.error("Saving of a msProjectTask failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
	
	/**
	 * Deletes an MsProjectTaskBean
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
        crit.add(OBJECTID, objectID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the msProjectTask " + objectID + " failed with: " + e);
        }
	}
	
	private static List convertTorqueListToBeanList(List torqueList) {		
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TMSProjectTask)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
	
	/**
	 * The returned work item ID list is milestone (from the parameters ID arrray). 
	 * @param workItemIDs
	 * @return
	 */
	@Override
	public List<Integer> getMilestones(int[] workItemIDs) {
		List<Integer> milestoneWorkItems = new ArrayList<Integer>();
		List<TMSProjectTaskBean> msProjectTaskBean = new ArrayList<TMSProjectTaskBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return milestoneWorkItems;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		
		if (workItemIDChunksList==null) {
			return milestoneWorkItems;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(TMSProjectTaskPeer.WORKITEM, workItemIDChunk);
			criteria.add(TMSProjectTaskPeer.MILESTONE, BooleanFields.TRUE_VALUE);
			try {
				msProjectTaskBean.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the workItemBeans by children and the chunk number " + 
						i + " of length  "  + workItemIDChunk.length + " failed with " + e.getMessage(), e);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
				}
			}
		}
		if(msProjectTaskBean != null) {
			for(int j = 0; j < msProjectTaskBean.size(); j++) {
				if(msProjectTaskBean.get(j).getMilestone() != null) {
					if ("Y".equals(msProjectTaskBean.get(j).getMilestone())) {
						milestoneWorkItems.add(msProjectTaskBean.get(j).getWorkitem());
					}
				}
			}
		}
		return milestoneWorkItems;
	}
}
