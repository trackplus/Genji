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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPpriorityBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PPriorityDAO;
import com.aurel.track.dao.PriorityDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;

public class PriorityBL {
	private static PriorityDAO priorityDAO = DAOFactory.getFactory().getPriorityDAO();
	private static PPriorityDAO pPriorityDAO = DAOFactory.getFactory().getPPriorityDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Loads a priority by primary key
	 * @param objectID
	 * @return
	 */
	public static TPriorityBean loadByPrimaryKey(Integer objectID) {
		return priorityDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads all priorities not localized
	 * @return
	 */
	public static List<TPriorityBean> loadAll() {
		return priorityDAO.loadAll();
	}
	
	/**
	 * Loads all priorities localized
	 * @param locale
	 * @return
	 */
	public static List<ILabelBean> loadAll(Locale locale) {
		return LocalizeUtil.localizeDropDownList(loadAll(), locale);
	}
	
	/**
	 * Loads the priorities by IDs 
	 * @param priorityIDs
	 * @return
	 */
	public static List<TPriorityBean> loadByPriorityIDs(List<Integer> priorityIDs) {
		return priorityDAO.loadByPriorityIDs(priorityIDs);
	}
	
	/**
	 * Gets priorityBeans by label
	 * @param label
	 * @return
	 */
	public static List<TPriorityBean> loadByLabel(String label) {
		return priorityDAO.loadByLabel(label);
	}
	
	/**
	 * Loads the priorityBeans for a project and issueType
	 * @param projectID
	 * @param issueType
	 * @return
	 */
	public static List<TPriorityBean> loadByProjectAndIssueType(Integer projectID, 
			Integer issueType, Integer currentPriority) {
		List<TPriorityBean> prioritiesByProjectIssueType = priorityDAO.loadByProjectAndIssueType(projectID, issueType);
		//there are some assignments
		if (prioritiesByProjectIssueType!=null && !prioritiesByProjectIssueType.isEmpty()) {
			if (currentPriority!=null) {
				List<Integer> priorityIDs = new ArrayList<Integer>();
				boolean found = false;
				for (TPriorityBean priorityBean : prioritiesByProjectIssueType) {
					Integer priorityID = priorityBean.getObjectID();
					priorityIDs.add(priorityID);
					if (priorityID.equals(currentPriority)) {
						found = true;
						break;
					}
				}
				if (found) {
					return prioritiesByProjectIssueType;
				} else {
					//this priority was removed form projectType-issueType assignment
					//add it and get the list again in order to maintain the sortorder
					priorityIDs.add(currentPriority);
					return loadByPriorityIDs(priorityIDs);
				}
			} else {
				//currentPriority is null, take the actual assignments
				return prioritiesByProjectIssueType;
			}
		} else {
			//no assignments, take all priorities
			return loadAll();
		}
	}
	
	/**
	 * Saves a new/modified list entry
	 * If sortOrder is not set it will be set to be the next available sortOrder
	 * @param labelBean
	 * @param copy
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static synchronized Integer save(ILabelBean labelBean, Locale locale) {
		TPriorityBean priorityBean = (TPriorityBean)labelBean;
		boolean isNew = priorityBean.getObjectID()==null;
		if (priorityBean.getSortorder()==null) {
			Integer sortOrder = priorityDAO.getNextSortOrder(); 
			priorityBean.setSortorder(sortOrder);
		}
		Integer objectID = priorityDAO.save(priorityBean);
		if (isNew) {
			priorityBean.setObjectID(objectID);
			addFilterAssignments(objectID);
			LocalizedListIndexer.getInstance().addLabelBean(priorityBean,
					LuceneUtil.LOOKUPENTITYTYPES.PRIORITY, isNew);
		} else {
			LocalizedListIndexer.getInstance().updateLabelBean(priorityBean,
					LuceneUtil.LOOKUPENTITYTYPES.PRIORITY);
		}
		LocalizeBL.saveSystemFieldLocalizedResource(
				LocalizeBL.RESOURCE_TYPES.PRIORITY, objectID, priorityBean.getLabel(), locale);
		LookupContainer.resetLookupMap(SystemFields.INTEGER_PRIORITY);
		//cache and possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PRIORITY, objectID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdate(isNew));
		return objectID;
	}	
	
	/**
	 * Create an entry with the new priority for each issue type 
	 * and project type in the associated TPPRIORITY table
	 * which have at least one other priority assigned. 
	 * This is to initially enable a new priority
	 * for all project types and issue types. Users may then go and remove
	 * certain entries so that the new value is only enabled for some
	 * project types and/or issue types.  
	 * @param newPriorityID   
	 */
	private static void addFilterAssignments(Integer newPriorityID) {
		List<TPpriorityBean> ppriorityBeansList = pPriorityDAO.loadAll();
		Map<Integer, Set<Integer>> projectTypesToIssueTypesSetWithPriority = new HashMap<Integer, Set<Integer>>();
		//probably not needed if this is called only by creating a new priority
		Map<Integer, Integer> projectTypesToIssueTypesSetWithThisPriority = new HashMap<Integer, Integer>();
		for (TPpriorityBean ppriorityBean : ppriorityBeansList) {
			Integer projectTypeID = ppriorityBean.getProjectType();
			Integer issueTypeID = ppriorityBean.getListType();
			Integer priorityID = ppriorityBean.getPriority();
			if (projectTypeID!=null && issueTypeID!=null && priorityID!=null) {
				Set<Integer> issueTypesSetWithPriority = projectTypesToIssueTypesSetWithPriority.get(projectTypeID);
				if (issueTypesSetWithPriority==null) {
					issueTypesSetWithPriority = new HashSet<Integer>();
					projectTypesToIssueTypesSetWithPriority.put(projectTypeID, issueTypesSetWithPriority);
				}
				issueTypesSetWithPriority.add(issueTypeID);							
				if (priorityID.equals(newPriorityID)) {
					projectTypesToIssueTypesSetWithThisPriority.put(projectTypeID, issueTypeID);
				}
			}
		}	
		for (Integer projectTypeID : projectTypesToIssueTypesSetWithPriority.keySet()) {
			Set<Integer> issueTypesSetWithPriority = projectTypesToIssueTypesSetWithPriority.get(projectTypeID);
			for (Integer issueTypeID : issueTypesSetWithPriority) {
				//do not add for the same priority again (not the case for new priorities)
				if (!(projectTypesToIssueTypesSetWithThisPriority.get(projectTypeID)!=null && 
						projectTypesToIssueTypesSetWithThisPriority.get(projectTypeID).equals(issueTypeID))) {
					insertAssignment(newPriorityID, issueTypeID, projectTypeID);
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
		TPpriorityBean ppriorityBean = new TPpriorityBean();
		ppriorityBean.setProjectType(projectType);
		ppriorityBean.setPriority(row);		
		ppriorityBean.setListType(column);
		return pPriorityDAO.save(ppriorityBean);
	}
	
	/**
	 * Saves a priorityBean
	 * @param labelBean
	 * @return
	 */
	public static Integer saveSimple(TPriorityBean priorityBean) {
		Integer priorityID = priorityDAO.save(priorityBean);
		LookupContainer.reloadNotLocalizedLabelBean(SystemFields.INTEGER_PRIORITY, priorityID);
		//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied   
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PRIORITY, priorityID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
		return priorityID;
	}
	
	
	/**
	 * Deletes the priority
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		if (objectID!=null) {
			TPriorityBean priorityBean = loadByPrimaryKey(objectID);
			Integer iconKey = null;
			if (priorityBean!=null) {
				iconKey = priorityBean.getIconKey();
			}
			priorityDAO.delete(objectID);
			if (iconKey!=null) {
				BlobBL.delete(iconKey);
			}
			//delete the priority from lucene index
			LocalizedListIndexer.getInstance().deleteByKeyAndType(objectID, 
					LuceneUtil.LOOKUPENTITYTYPES.PRIORITY);
			//remove localized resource
			LocalizeBL.removeLocalizedResources(new TPriorityBean().getKeyPrefix(), objectID);
			LookupContainer.resetLookupMap(SystemFields.INTEGER_PRIORITY);
			//cache and possible lucene update in other nodes
			ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PRIORITY, objectID, ClusterMarkChangesBL.getChangeTypeByDelete());
		}
	}
	
	/** 
	 * This method replaces all occurrences of oldOID priority with
	 * newOID priority and then deletes the priority
	 * @param oldOID
	 * @param newOID
	 */
	public static void replaceAndDelete(Integer oldOID, Integer newOID) {
		if (newOID!=null) {
			int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.PRIORITY, oldOID);
			priorityDAO.replace(oldOID, newOID);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_PRIORITY, true);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_PRIORITY, false);
			//reindex the affected workItems 
			LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_PRIORITY);
			ClusterMarkChangesBL.markDirtyWorkItemsInCluster(reindexWorkItems);
		}
		delete(oldOID);
	}
	
	/**
	 * Gets the list options
	 * @param locale
	 * @return
	 */
	public static List<IntegerStringBean> loadWithWarningLevel(Locale locale) {
		List<TPriorityBean> priorityBeans = (List)loadAll(locale);
		List<IntegerStringBean> beansWithWarningLevel = new LinkedList<IntegerStringBean>();
		beansWithWarningLevel.add(new IntegerStringBean("", null));
		for (TPriorityBean priorityBean : priorityBeans) {
			StringBuilder stringBuilder = new StringBuilder();
			String label = priorityBean.getLabel();
			if (label!=null) {
				stringBuilder.append(label);
			}
			Integer warningLevel = priorityBean.getWlevel();
			if (warningLevel!=null) {
				stringBuilder.append(" (").append(warningLevel).append(")");
			}
			beansWithWarningLevel.add(new IntegerStringBean(stringBuilder.toString(), priorityBean.getObjectID()));
		}
		return beansWithWarningLevel;
	}
	
	/**
	 * Gets the allowed priorities for project types
	 * @param projectTypeIDs
	 * @return
	 */
	public static List<TPriorityBean> loadAllowedByProjectTypesAndIssueTypes(Integer[] projectTypeIDs, Integer[] selectedItemTypeIDs) {
		if (projectTypeIDs==null || projectTypeIDs.length==0) {
			return PriorityBL.loadAll();
		}
		Set<Integer> priorities = new HashSet<Integer>();
		for (int i = 0; i < projectTypeIDs.length; i++) {
			Integer projectTypeID = projectTypeIDs[i];
			//get the list types allowed for the projectType
			List<TListTypeBean> allowedIssueTypeBeans = IssueTypeBL.loadAllowedByProjectType(projectTypeID);
			List<Integer> issueTypeIDs = GeneralUtils.createIntegerListFromBeanList(allowedIssueTypeBeans);
			if (selectedItemTypeIDs!=null && selectedItemTypeIDs.length>0) {
				List<Integer> selectedItemTypeIDList = GeneralUtils.createIntegerListFromIntegerArr(selectedItemTypeIDs);
				issueTypeIDs.retainAll(selectedItemTypeIDList);
			}
			Map<Integer, List<Integer>> issueTypeToPriorities = loadPrioritiesAllowedByProjectTypeAndIssueTypes(projectTypeID, issueTypeIDs);
			if (issueTypeToPriorities==null || issueTypeToPriorities.isEmpty()) {
				//none of the issue types for a projectType has priority restrictions
				return loadAll();
			} else {
				for (Integer issueType : issueTypeIDs) {
					List<Integer> prioritiesForIssueType = issueTypeToPriorities.get(issueType);
					if (prioritiesForIssueType==null) {
						//at least one issueType for a projectType has no priority restrictions at all
						return loadAll();
					} else {
						priorities.addAll(prioritiesForIssueType);
					}
				}
			}
		}
		return loadByPriorityIDs(GeneralUtils.createListFromSet(priorities));
	}
	
	/**
	* Whether a priority is restricted in all issue types in a project type 
	* @param projectTypeID
	* @param issueTypes
	* @return
	*/
	private static Map<Integer, List<Integer>> loadPrioritiesAllowedByProjectTypeAndIssueTypes(Integer projectTypeID, List<Integer> issueTypes) {
		Map<Integer, List<Integer>> issueTypeToPrioritiesMap = new HashMap<Integer, List<Integer>>();
		List<TPpriorityBean> allowedPPriorities = pPriorityDAO.loadByProjectTypeAndIssueTypes(projectTypeID, issueTypes);
		if (allowedPPriorities==null || allowedPPriorities.isEmpty()) {
			return issueTypeToPrioritiesMap;
		}
		Iterator<TPpriorityBean> iterator = allowedPPriorities.iterator();
		while (iterator.hasNext()) {
			TPpriorityBean pPriorityBean = iterator.next();
			Integer issueType = pPriorityBean.getListType();
			List<Integer> prioritiesList = issueTypeToPrioritiesMap.get(issueType);
			if (prioritiesList==null) {
				prioritiesList = new LinkedList<Integer>();
				issueTypeToPrioritiesMap.put(issueType, prioritiesList);
			}
			prioritiesList.add(pPriorityBean.getPriority());
		}
		return issueTypeToPrioritiesMap;
	}
}
