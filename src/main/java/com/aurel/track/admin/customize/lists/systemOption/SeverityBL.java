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
import com.aurel.track.beans.TPseverityBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PSeverityDAO;
import com.aurel.track.dao.SeverityDAO;
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

public class SeverityBL {
	private static SeverityDAO severityDAO = DAOFactory.getFactory().getSeverityDAO();
	private static PSeverityDAO pSeverityDAO = DAOFactory.getFactory().getPSeverityDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Loads a severity by primary key
	 * @param objectID
	 * @return
	 */
	public static TSeverityBean loadByPrimaryKey(Integer objectID) {
		return severityDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads all severities not localized
	 * @return
	 */
	public static List<TSeverityBean> loadAll() {
		return severityDAO.loadAll();
	}
	
	/**
	 * Loads all severities localized
	 * @param locale
	 * @return
	 */
	public static List<ILabelBean> loadAll(Locale locale) {
		return LocalizeUtil.localizeDropDownList(loadAll(), locale);
	}
	
	/**
	 * Loads the severities by IDs
	 * @param severityIDs
	 */
	public static List<TSeverityBean> loadBySeverityIDs(List<Integer> severityIDs) {
		return severityDAO.loadBySeverityIDs(severityIDs);
	}
	
	/**
	 * Gets an severityBean by label
	 * @param label
	 * @return
	 */
	public static List<TSeverityBean> loadByLabel(String label) {
		return severityDAO.loadByLabel(label);
	}
	
	/**
	 * Loads the severityBeans for a project and issueType
	 * @param projectID
	 * @param issueType
	 * @return
	 */
	public static List<TSeverityBean> loadByProjectAndIssueType(Integer projectID,
			Integer issueType, Integer currentSeverity) {
		List<TSeverityBean> severitiesByProjectIssueType = severityDAO.loadByProjectAndIssueType(projectID, issueType);
		//there are some assignments
		if (severitiesByProjectIssueType!=null && !severitiesByProjectIssueType.isEmpty()) {
			if (currentSeverity!=null) {
				List<Integer> severityIDs = new ArrayList<Integer>();
				boolean found = false;
				for (TSeverityBean severityBean : severitiesByProjectIssueType) {
					Integer severityID = severityBean.getObjectID();
					severityIDs.add(severityID);
					if (severityID.equals(currentSeverity)) {
						found = true;
						break;
					}
				}
				if (found) {
					return severitiesByProjectIssueType;
				} else {
					//this severity was removed form projectType-issueType assignment
					//add it and get the list again in order to maintain the sortorder
					severityIDs.add(currentSeverity);
					return loadBySeverityIDs(severityIDs);
				}
			} else {
				//currentSeverity is null, take the actual assignments
				return severitiesByProjectIssueType;
			}
		} else {
			//no assignments, take all severities
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
		TSeverityBean severityBean = (TSeverityBean)labelBean;
		boolean isNew = severityBean.getObjectID()==null;
		if (severityBean.getSortorder()==null) {
			Integer sortOrder = severityDAO.getNextSortOrder(); 
			severityBean.setSortorder(sortOrder);
		}
		Integer objectID = severityDAO.save(severityBean);
		if (isNew) {
			severityBean.setObjectID(objectID);
			addFilterAssignments(objectID);
			LocalizedListIndexer.getInstance().addLabelBean(severityBean,
					LuceneUtil.LOOKUPENTITYTYPES.SEVERITY, isNew);
		} else {
			LocalizedListIndexer.getInstance().updateLabelBean(severityBean,
					LuceneUtil.LOOKUPENTITYTYPES.SEVERITY);
		}
		LocalizeBL.saveSystemFieldLocalizedResource(
				LocalizeBL.RESOURCE_TYPES.SEVERITY, objectID, severityBean.getLabel(), locale);
		LookupContainer.resetLookupMap(SystemFields.INTEGER_SEVERITY);
		//cache and possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_SEVERITY, objectID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdate(isNew));
		return objectID;
	}
	
	/**
	 * Create an entry with the new severity for each issue type 
	 * and project type in the associated TPSEVERITY table
	 * which have at least one other severity assigned. 
	 * This is to initially enable a new severity
	 * for all project types and issue types. Users may then go and remove
	 * certain entries so that the new value is only enabled for some
	 * project types and/or issue types.
	 * @param newSeverityID	 
	 */
	private static void addFilterAssignments(Integer newSeverityID) {
		List<TPseverityBean> pseverityBeansList = pSeverityDAO.loadAll();
		Map<Integer, Set<Integer>> projectTypesToIssueTypesSetWithSeverity = new HashMap<Integer, Set<Integer>>();
		//probably not needed if this is called only by creating a new severity
		Map<Integer, Integer> projectTypesToIssueTypesSetWithThisSeverity = new HashMap<Integer, Integer>();
		for (TPseverityBean pseverityBean : pseverityBeansList) {
			Integer projectTypeID = pseverityBean.getProjectType();
			Integer issueTypeID = pseverityBean.getListType();
			Integer severityID = pseverityBean.getSeverity();
			if (projectTypeID!=null && issueTypeID!=null && severityID!=null) {
				Set<Integer> issueTypesSetWithSeverity = projectTypesToIssueTypesSetWithSeverity.get(projectTypeID);
				if (issueTypesSetWithSeverity==null) {
					issueTypesSetWithSeverity = new HashSet<Integer>();
					projectTypesToIssueTypesSetWithSeverity.put(projectTypeID, issueTypesSetWithSeverity);
				}
				issueTypesSetWithSeverity.add(issueTypeID);
				if (severityID.equals(newSeverityID)) {
					projectTypesToIssueTypesSetWithThisSeverity.put(projectTypeID, issueTypeID);
				}
			}
		}	
		for (Integer projectTypeID : projectTypesToIssueTypesSetWithSeverity.keySet()) {
			Set<Integer> issueTypesSetWithSeverity = projectTypesToIssueTypesSetWithSeverity.get(projectTypeID);
			for (Integer issueTypeID : issueTypesSetWithSeverity) {
				//do not add for the same severity again (not the case for new severities)
				if (!(projectTypesToIssueTypesSetWithThisSeverity.get(projectTypeID)!=null && 
						projectTypesToIssueTypesSetWithThisSeverity.get(projectTypeID).equals(issueTypeID))) {
					insertAssignment(newSeverityID, issueTypeID, projectTypeID);
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
		TPseverityBean pseverityBean = new TPseverityBean();
		pseverityBean.setProjectType(projectType);
		pseverityBean.setSeverity(row);		
		pseverityBean.setListType(column);
		return pSeverityDAO.save(pseverityBean);
	}
	
	/**
	 * Saves the label bean into the database  
	 */
	public static Integer saveSimple(TSeverityBean severityBean) {
		Integer severityID = severityDAO.save(severityBean);
		LookupContainer.reloadNotLocalizedLabelBean(SystemFields.INTEGER_SEVERITY, severityID);
		//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied   
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_SEVERITY, severityID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
		return severityID;
	}
	
	
	/**
	 * Deletes the severity
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		if (objectID!=null) {
			TSeverityBean severityBean = loadByPrimaryKey(objectID);
			Integer iconKey = null;
			if (severityBean!=null) {
				iconKey = severityBean.getIconKey();
			}
			severityDAO.delete(objectID);
			if (iconKey!=null) {
				BlobBL.delete(iconKey);
			}
			//delete the severity from lucene index
			LocalizedListIndexer.getInstance().deleteByKeyAndType(objectID, 
					LuceneUtil.LOOKUPENTITYTYPES.SEVERITY);
			//remove localized resource
			LocalizeBL.removeLocalizedResources(new TSeverityBean().getKeyPrefix(), objectID);
			LookupContainer.resetLookupMap(SystemFields.INTEGER_SEVERITY);
			//cache and possible lucene update in other nodes
			ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_SEVERITY, objectID, ClusterMarkChangesBL.getChangeTypeByDelete());
		}
	}
	
	/** 
	 * This method replaces all occurrences of severity value oldOID with
	 * state value newOID and then deletes the severity
	 * @param oldOID
	 * @param newOID
	 */
	public static void replaceAndDelete(Integer oldOID, Integer newOID) {
		if (newOID!=null) {
			int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.SEVERITY, oldOID);
			severityDAO.replace(oldOID, newOID);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_SEVERITY, true);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_SEVERITY, false);
			//reindex the affected workItems 
			LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_SEVERITY);
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
		List<TSeverityBean> severityBeans = (List)loadAll(locale);
		List<IntegerStringBean> beansWithWarningLevel = new LinkedList<IntegerStringBean>();
		beansWithWarningLevel.add(new IntegerStringBean("", null));
		for (TSeverityBean severityBean : severityBeans) {
			StringBuilder stringBuilder = new StringBuilder();
			String label = severityBean.getLabel();
			if (label!=null) {
				stringBuilder.append(label);
			}
			Integer warningLevel = severityBean.getWlevel();
			if (warningLevel!=null) {
				stringBuilder.append(" (").append(warningLevel).append(")");
			}
			beansWithWarningLevel.add(new IntegerStringBean(stringBuilder.toString(), severityBean.getObjectID()));
		}
		return beansWithWarningLevel;
	}
	
	public static List<TSeverityBean> loadAllowedByProjectTypesAndIssueTypes(Integer[] projectTypeIDs, Integer[] selectedItemTypeIDs) {
		if (projectTypeIDs==null || projectTypeIDs.length==0) {
			return SeverityBL.loadAll();
		}
		Set<Integer> severities = new HashSet<Integer>();
		for (int i = 0; i < projectTypeIDs.length; i++) {
			Integer projectTypeID = projectTypeIDs[i];
			//get the list types allowed for the projectType
			List<TListTypeBean> allowedIssueTypeBeans = IssueTypeBL.loadAllowedByProjectType(projectTypeID);
			List<Integer> issueTypeIDs = GeneralUtils.createIntegerListFromBeanList(allowedIssueTypeBeans);
			if (selectedItemTypeIDs!=null && selectedItemTypeIDs.length>0) {
				List<Integer> selectedItemTypeIDList = GeneralUtils.createIntegerListFromIntegerArr(selectedItemTypeIDs);
				issueTypeIDs.retainAll(selectedItemTypeIDList);
			}
			Map<Integer, List<Integer>> issueTypeToSeverities = loadSeveritiesAllowedByProjectTypeAndIssueTypes(projectTypeID, issueTypeIDs);
			if (issueTypeToSeverities==null || issueTypeToSeverities.isEmpty()) {
				//none of the issue types for a projectType has severity restrictions
				return severityDAO.loadAll();
			} else {
				for (Integer issueType : issueTypeIDs) {
					List<Integer> severitiesForIssueType = issueTypeToSeverities.get(issueType);
					if (severitiesForIssueType==null) {
						//at least one issueType for a projectType has no severity restrictions at all
						return severityDAO.loadAll();
					} else {
						severities.addAll(severitiesForIssueType);
					}
				}
			}
		}
		return loadBySeverityIDs(GeneralUtils.createListFromSet(severities));
	}
	
	/**
	 * Whether a status is restricted in all issue types in a project type 
	 * @param projectTypeID
	 * @param issueTypes
	 * @return
	 */
	private static Map<Integer, List<Integer>> loadSeveritiesAllowedByProjectTypeAndIssueTypes(Integer projectTypeID, List<Integer> issueTypes) {
		Map<Integer, List<Integer>> issueTypeToPrioritiesMap = new HashMap<Integer, List<Integer>>();
		List<TPseverityBean> allowedPPriorities = pSeverityDAO.loadByProjectTypeAndIssueTypes(projectTypeID, issueTypes);
		if (allowedPPriorities==null || allowedPPriorities.isEmpty()) {
			return issueTypeToPrioritiesMap;
		}
		Iterator<TPseverityBean> iterator = allowedPPriorities.iterator();
		while (iterator.hasNext()) {
			TPseverityBean pSeverityBean = iterator.next();
			Integer issueType = pSeverityBean.getListType();
			List<Integer> prioritiesList = issueTypeToPrioritiesMap.get(issueType);
			if (prioritiesList==null) {
				prioritiesList = new LinkedList<Integer>();
				issueTypeToPrioritiesMap.put(issueType, prioritiesList);
			}
			prioritiesList.add(pSeverityBean.getSeverity());
		}
		return issueTypeToPrioritiesMap;
	}
}
