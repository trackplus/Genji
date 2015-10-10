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

package com.aurel.track.admin.customize.lists.systemOption;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeStatusAssignmentFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TInitStateBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TPstateBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.InitStatusDAO;
import com.aurel.track.dao.PStatusDAO;
import com.aurel.track.dao.StateDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

public class StatusBL {
	private static final Logger LOGGER = LogManager.getLogger(StatusBL.class);
	private static StateDAO stateDAO = DAOFactory.getFactory().getStateDAO();
	private static PStatusDAO pStateDAO = DAOFactory.getFactory().getPStatusDAO();
	private static InitStatusDAO initStatusDAO = DAOFactory.getFactory().getInitStatusDAO();
	private static PStatusDAO pStatusDAO = DAOFactory.getFactory().getPStatusDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Loads a status by primary key
	 * @param objectID
	 * @return
	 */
	public static TStateBean loadByPrimaryKey(Integer objectID) {
		return stateDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Gets the localized status label for a status
	 * @param statusID
	 * @param locale
	 * @return
	 */
	public static String getStatusDisplay(Integer statusID, Locale locale) {
		TStateBean stateBean=LookupContainer.getStatusBean(statusID, locale);
		if(stateBean!=null){
			return stateBean.getLabel();
		}
		return "";
	}
	
	/**
	 * Load all defined statuses
	 * @return
	 */
	public static List<TStateBean> loadAll() {
		return stateDAO.loadAll();
	}
	
	public static List<TStateBean> loadByKeys(Object[] keys) {
		return stateDAO.loadByKeys(keys);
	}
	
	/**
	 * Load the states by label
	 * @param label
	 * @return
	 */
	public static List<TStateBean> loadByLabel(String label) {
		return stateDAO.loadByLabel(label);
	}
	
	/**
	 * Load all defined statuses localized according to locale
	 * @param locale
	 * @return
	 */
	public static List<ILabelBean> loadAll(Locale locale) {
		return LocalizeUtil.localizeDropDownList(stateDAO.loadAll(), locale);
	}

	/**
	 * Saves a status
	 * @param labelBean
	 * @param copy
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static synchronized Integer save(ILabelBean labelBean, Locale locale) {
		TStateBean stateBean = (TStateBean)labelBean;
		boolean isNew = stateBean.getObjectID()==null;
		if (stateBean.getSortorder()==null) {
			Integer sortOrder = stateDAO.getNextSortOrder(); 
			stateBean.setSortorder(sortOrder);
		}
		Integer objectID = stateDAO.save(stateBean);
		if (isNew) {
			stateBean.setObjectID(objectID);
			addFilterAssignments(objectID);
			LocalizedListIndexer.getInstance().addLabelBean(stateBean,
					LuceneUtil.LOOKUPENTITYTYPES.STATE, isNew);
		} else {
			LocalizedListIndexer.getInstance().updateLabelBean(stateBean,
					LuceneUtil.LOOKUPENTITYTYPES.STATE);
		}
		LocalizeBL.saveSystemFieldLocalizedResource(
				LocalizeBL.RESOURCE_TYPES.STATUS, objectID, stateBean.getLabel(), locale);
		LookupContainer.resetLookupMap(SystemFields.INTEGER_STATE);
		//cache and possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_STATE, objectID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdate(isNew));
		return objectID;
	}
	
	/**
	 * Create an entry with the new status for each issue type 
	 * and project type in the associated TPSTATE table
	 * which have at least one other status assigned. 
	 * This is to initially enable a new status
	 * for all project types and issue types. Users may then go and remove
	 * certain entries so that the new value is only enabled for some
	 * project types and/or issue types.
	 * @param newStatusID	 
	 */
	private static void addFilterAssignments(Integer newStatusID) {
		List<TPstateBean> pstatusBeansList = pStatusDAO.loadAll();
		Map<Integer, Set<Integer>> projectTypesToIssueTypesSetWithStatus = new HashMap<Integer, Set<Integer>>();
		//probably not needed if this is called only by creating a new status
		Map<Integer, Integer> projectTypesToIssueTypesSetWithThisStatus = new HashMap<Integer, Integer>();
		for (TPstateBean pstatusBean : pstatusBeansList) {
			Integer projectTypeID = pstatusBean.getProjectType();
			Integer issueTypeID = pstatusBean.getListType();
			Integer statusID = pstatusBean.getState();
			if (projectTypeID!=null && issueTypeID!=null && statusID!=null) {
				Set<Integer> issueTypesSetWithStatus = projectTypesToIssueTypesSetWithStatus.get(projectTypeID);
				if (issueTypesSetWithStatus==null) {
					issueTypesSetWithStatus = new HashSet<Integer>();
					projectTypesToIssueTypesSetWithStatus.put(projectTypeID, issueTypesSetWithStatus);
				}
				issueTypesSetWithStatus.add(issueTypeID);
				if (statusID.equals(newStatusID)) {
					projectTypesToIssueTypesSetWithThisStatus.put(projectTypeID, issueTypeID);
				}
			}
		}	
		for (Integer projectTypeID : projectTypesToIssueTypesSetWithStatus.keySet()) {
			Set<Integer> issueTypesSetWithStatus = projectTypesToIssueTypesSetWithStatus.get(projectTypeID);
			for (Integer issueTypeID : issueTypesSetWithStatus) {
				//do not add for the same status again (not the case for new status)
				if (!(projectTypesToIssueTypesSetWithThisStatus.get(projectTypeID)!=null && 
						projectTypesToIssueTypesSetWithThisStatus.get(projectTypeID).equals(issueTypeID))) {
					insertAssignment(newStatusID, issueTypeID, projectTypeID);					
				}
			}
		}
	}
	
	/**
	 * Inserts a new assignment
	 * @param row
	 * @param column
	 * @param projectType
	 * @return
	 */
	private static Integer insertAssignment(Integer row, Integer column, Integer projectType) {
		TPstateBean pstateBean = new TPstateBean();
		pstateBean.setProjectType(projectType);
		pstateBean.setState(row);
		pstateBean.setListType(column);
		return pStatusDAO.save(pstateBean);
	}
	
	/**
	 * Saves a state bean
	 * @param stateBean
	 * @return
	 */
	public static Integer saveSimple(TStateBean stateBean) {
		Integer statusID = stateDAO.save(stateBean);
		LookupContainer.reloadNotLocalizedLabelBean(SystemFields.INTEGER_STATE, statusID);
		//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied   
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_STATE, statusID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
		return statusID;
	}
	
	
	/**
	 * Deletes the status
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		if (objectID!=null) {
			TStateBean stateBean = loadByPrimaryKey(objectID);
			Integer iconKey = null;
			if (stateBean!=null) {
				iconKey = stateBean.getIconKey();
			}
			stateDAO.delete(objectID);
			if (iconKey!=null) {
				BlobBL.delete(iconKey);
			}
			//delete the state from lucene index
			LocalizedListIndexer.getInstance().deleteByKeyAndType(objectID, 
					LuceneUtil.LOOKUPENTITYTYPES.STATE);
			//remove localized resource
			LocalizeBL.removeLocalizedResources(new TStateBean().getKeyPrefix(), objectID);
			LookupContainer.resetLookupMap(SystemFields.INTEGER_STATE);
			//cache and possible lucene update in other nodes
			ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_STATE, objectID, ClusterMarkChangesBL.getChangeTypeByDelete());
		}
	}
	
	/** 
	 * This method replaces all occurrences of state value oldOID with
	 * state value newOID and then deletes the state
	 * @param oldOID
	 * @param newOID
	 */
	public static void replaceAndDelete(Integer oldOID, Integer newOID) {
		if (newOID!=null) {
			int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.STATE, oldOID);						
			stateDAO.replace(oldOID, newOID);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_STATE, true);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_STATE, false);						
			//reindex the affected workItems 
			LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_STATE);
			ClusterMarkChangesBL.markDirtyWorkItemsInCluster(reindexWorkItems);
		}
		delete(oldOID);
	}
	/**
	 * Load the states with closed flag
	 * @return
	 */
	public static List<TStateBean> loadClosedStates() {
		return stateDAO.loadByStateFlag(TStateBean.STATEFLAGS.CLOSED);
	}
	
	/**
	 * Load the states with active flag
	 * @return
	 */
	public static List<TStateBean> loadActiveStates() {
		return stateDAO.loadByStateFlag(TStateBean.STATEFLAGS.ACTIVE);
	}
	
	/**
	 * Load the states with active or inactive flag
	 * @return
	 */
	public static List<TStateBean> loadNotClosedStates() {
		return stateDAO.loadByStateFlags( new int[] {TStateBean.STATEFLAGS.ACTIVE, TStateBean.STATEFLAGS.INACTIVE, TStateBean.STATEFLAGS.DISABLED});
	}
	
	public static Map<Integer,ILabelBean> getAsMap(List<ILabelBean> list){
		Map<Integer,ILabelBean> map=new HashMap<Integer, ILabelBean>();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				ILabelBean iLabelBean=list.get(i);
				map.put(iLabelBean.getObjectID(),iLabelBean);
			}
		}
		return map;
	}
	public static Map<Integer,TStateBean> getAsMapBean(List<TStateBean> list){
		Map<Integer,TStateBean> map=new HashMap<Integer, TStateBean>();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				TStateBean iLabelBean=list.get(i);
				map.put(iLabelBean.getObjectID(),iLabelBean);
			}
		}
		return map;
	}
	
	/**
	 * Loads the allowed statuses by project types
	 * @param projectTypeIDs
	 * @return
	 */
	public static List<TStateBean> loadAllowedByProjectTypesAndIssueTypes(Integer[] projectTypeIDs, Integer[] selectedItemTypeIDs) {
		if (projectTypeIDs==null || projectTypeIDs.length==0) {
			return StatusBL.loadAll();
		}
		Set<Integer> statuses = new HashSet<Integer>();
		for (int i = 0; i < projectTypeIDs.length; i++) {
			Integer projectTypeID = projectTypeIDs[i];
			//get the list types allowed for the projectType
			List<TListTypeBean> allowedIssueTypeBeans = IssueTypeBL.loadAllowedByProjectType(projectTypeID);
			List<Integer> issueTypeIDs = GeneralUtils.createIntegerListFromBeanList(allowedIssueTypeBeans);
			if (selectedItemTypeIDs!=null && selectedItemTypeIDs.length>0) {
				List<Integer> selectedItemTypeIDList = GeneralUtils.createIntegerListFromIntegerArr(selectedItemTypeIDs);
				issueTypeIDs.retainAll(selectedItemTypeIDList);
			}
			Map<Integer, List<Integer>> issueTypeToStatuses = loadStatesAllowedByProjectTypeAndIssueTypes(projectTypeID, issueTypeIDs);
			if (issueTypeToStatuses==null || issueTypeToStatuses.isEmpty()) {
				//none of the issue types for a projectType has status restrictions
				return StatusBL.loadAll();
			} else {
				for (Integer issueType : issueTypeIDs) {
					List<Integer> statusesForIssueType = issueTypeToStatuses.get(issueType);
					if (statusesForIssueType==null) {
						//at least one issueType for a projectType has no status restrictions at all
						return StatusBL.loadAll();
					} else {
						statuses.addAll(statusesForIssueType);
					}
				}
			}
		}
		return loadByKeys(statuses.toArray());
	}
	
	public static List<ILabelBean> loadAllowedByProjectTypesAndIssueTypes(Integer[] projectTypeIDs, Integer[] selectedItemTypeIDs, Locale locale) {
		return LocalizeUtil.localizeDropDownList(loadAllowedByProjectTypesAndIssueTypes(projectTypeIDs, selectedItemTypeIDs), locale);
	}

	
	/**
	 * Whether a status is restricted in all issue types in a project type 
	 * @param projectTypeID
	 * @param issueTypes
	 * @return
	 */
	private static Map<Integer, List<Integer>> loadStatesAllowedByProjectTypeAndIssueTypes(Integer projectTypeID, List<Integer> issueTypes) {
		Map<Integer, List<Integer>> issueTypeToStatusesMap = new HashMap<Integer, List<Integer>>();
		List<TPstateBean> allowedPStatuses = pStateDAO.loadByProjectTypeAndIssueTypes(projectTypeID, issueTypes);
		if (allowedPStatuses==null || allowedPStatuses.isEmpty()) {
			return issueTypeToStatusesMap;
		}
		Iterator<TPstateBean> iterator = allowedPStatuses.iterator();
		while (iterator.hasNext()) {
			TPstateBean pStateBean = iterator.next();
			Integer issueType = pStateBean.getListType();
			List<Integer> statusesList = issueTypeToStatusesMap.get(issueType);
			if (statusesList==null) {
				statusesList = new LinkedList<Integer>();
				issueTypeToStatusesMap.put(issueType, statusesList);
			}
			statusesList.add(pStateBean.getState());
		}
		return issueTypeToStatusesMap;
	}
	
	/**
	 * Gets the statuses by projectType and issueType assignments (independently of the workflow)
	 * @param projectTypeID
	 * @param issueTypeID
	 * @param stateIDFrom
	 * @return
	 */
	public static List<TStateBean> getByProjectTypeIssueTypeAssignments(Integer projectTypeID, Integer issueTypeID, Integer stateIDFrom) {
		Set<Integer> statusIDs = ProjectTypeItemTypeStatusAssignmentFacade.getInstance().getAssignedIDsByProjectTypeIDAndItemType(projectTypeID, issueTypeID);
		if (statusIDs.isEmpty()) {
			//no projectType/issueType restriction
			LOGGER.debug("No assignment found for project type " + projectTypeID + " item type " + issueTypeID);
			return loadAll();
		} else {
			//get by projectType/issueType restriction
			LOGGER.debug(statusIDs.size() + " assignments found for project type " + projectTypeID + " item type " + issueTypeID);
			if (stateIDFrom!=null) {
				//the original value should appear in the list
				statusIDs.add(stateIDFrom);
			}
			if (!statusIDs.isEmpty()) {
				//get the assigned statuses
				return loadByKeys(statusIDs.toArray());
			} else {
				//the actual status was the only one assigned 
				return new LinkedList<TStateBean>();
			}
		}
	}

	/**
	 * Filter out the closed status beans
	 * @param statusIDFrom
	 * @param statusBeans
	 * @param workItemBean
	 * @param personID
	 */
	public static  List<TStateBean> filterClosedStateBeans(Integer statusIDFrom, List<TStateBean> statusBeans, TWorkItemBean workItemBean, Integer personID) {
		if (statusBeans!=null && !statusBeans.isEmpty()) {
			boolean allowedToClose = AccessBeans.isAllowedToClose(workItemBean, personID);
			if (!allowedToClose) {
				for (Iterator<TStateBean> iterator = statusBeans.iterator(); iterator.hasNext();) {
					TStateBean stateBean = iterator.next();
					if (statusIDFrom==null || !statusIDFrom.equals(stateBean.getObjectID())) {
						Integer stateFlag = stateBean.getStateflag();
						if (stateFlag!=null && stateFlag.intValue()==TStateBean.STATEFLAGS.CLOSED) {
							LOGGER.debug("Not allowed to close. Remove status " + stateBean.getLabel() + " (" + stateBean.getObjectID() + ")");
							iterator.remove();
						}
					}
				}
			}
		}
		return statusBeans;
	}
	
	/**
	 * Returns the most specific initial state
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	public static Integer getInitialState(Integer projectID, Integer issueTypeID) {
		//first try project and issueType specific initial state
		List<TInitStateBean> initStatusList = initStatusDAO.loadByProjectAndIssueType(projectID, issueTypeID);
		if (initStatusList!=null && !initStatusList.isEmpty()) {
			TInitStateBean initStateBean = initStatusList.get(0);
			if (initStateBean!=null) {
				Integer statusID = initStateBean.getStateKey();
				if (statusID!=null) {
					return statusID;
				}
			}
		}
		//then try the project specific initial state
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		if (projectBean!=null) {
			return projectBean.getDefaultInitStateID();
		}
		return null;
	}

}
