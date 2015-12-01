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

package com.aurel.track.admin.customize.lists.systemOption;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.lists.ChildIssueTypeAssignmentsBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPlistTypeBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TRoleListTypeBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.IssueTypeDAO;
import com.aurel.track.dao.PIssueTypeDAO;
import com.aurel.track.dao.ProjectTypeDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

public class IssueTypeBL {
	
	private static final Logger LOGGER = LogManager.getLogger(IssueTypeBL.class);
	private static IssueTypeDAO issueTypeDAO = DAOFactory.getFactory().getIssueTypeDAO();
	private static ProjectTypeDAO projectTypeDAO = DAOFactory.getFactory().getProjectTypeDAO();
	private static PIssueTypeDAO pIssueTypeDAO = DAOFactory.getFactory().getPIssueTypeDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Loads a status by primary key
	 * @param objectID
	 * @return
	 */
	public static TListTypeBean loadByPrimaryKey(Integer objectID) {
		return issueTypeDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads a status by primary key
	 * @param objectID
	 * @return
	 */
	public static String getLocalizedLabel(Integer objectID, Locale locale) {
		TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(objectID, locale);
		if (listTypeBean!=null) {
			return listTypeBean.getLabel();
		}
		return "";
	}
	
	/**
	 * Load all selectable item types (exclude item types with Document typeflags)
	 * @return
	 */
	public static List<TListTypeBean> loadAllSelectable() {
		return issueTypeDAO.loadAllSelectable();
	}
	
	/**
	 * Loads all document type item types
	 * @return
	 */
	public static List<TListTypeBean> loadAllDocumentTypes() {
		return issueTypeDAO.loadAllDocumentTypes();
	}
	
	/**
	 * Loads the strict document item types
	 * @return
	 */
	public static List<TListTypeBean> loadStrictDocumentTypes() {
		return issueTypeDAO.loadStrictDocumentTypes();
	}
	
	/**
	 * Load all selectable item types localized (exclude item types with Document typeflags)
	 * @return
	 */
	public static List<TListTypeBean> loadAllSelectable(Locale locale) {
		return LocalizeUtil.localizeDropDownList(issueTypeDAO.loadAllSelectable(), locale);
	}
	
	/**
	 * Load all define item types
	 * @return
	 */
	public static List<TListTypeBean> loadAll() {
		return issueTypeDAO.loadAll();
	}
	
	/**
	 * Load all defined item types localized
	 * @param locale
	 * @return
	 */
	public static List<ILabelBean> loadAll(Locale locale) {
		return LocalizeUtil.localizeDropDownList(loadAll(), locale);
	}
	
	/**
	 * Load all possible item types which can be created by the user
	 * @param personID
	 * @param locale
	 * @return
	 */
	public static List<TListTypeBean> loadAllByPerson(Integer personID, Locale locale) {
		//get all create item role assignments for person
		List<TAccessControlListBean> accessControlListBeans = AccessBeans.loadByPersonAndRight(personID, 
				new int[] { AccessFlagIndexes.CREATETASK, AccessFlagIndexes.PROJECTADMIN}, true);
		Map<Integer, Set<Integer>> projectsToRolesMap = new HashMap<Integer, Set<Integer>>();
		Set<Integer> allPersonRolesSet = new HashSet<Integer>();
		if (accessControlListBeans!=null) {
			for (TAccessControlListBean accessControlListBean : accessControlListBeans) {
				Integer projectID = accessControlListBean.getProjectID();
				Integer roleID = accessControlListBean.getRoleID();
				allPersonRolesSet.add(roleID);
				Set<Integer> rolesInProject = projectsToRolesMap.get(projectID);
				if (rolesInProject==null) {
					rolesInProject = new HashSet<Integer>();
					projectsToRolesMap.put(projectID, rolesInProject);
				}
				rolesInProject.add(roleID);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(accessControlListBeans.size() + " assignments found for person " + personID +
					": " + allPersonRolesSet.size() + " roles " + " in " + projectsToRolesMap.size() + " projects ");
			}
		} else {
			return new LinkedList<TListTypeBean>();
		}
		//gets the itemType assignments for the assigned roles 
		Map<Integer, Set<Integer>> roleToItemTypesMap = new HashMap<Integer, Set<Integer>>();
		List<TRoleListTypeBean> itemTypesInRoles = RoleBL.loadByRoles(GeneralUtils.createIntegerListFromCollection(allPersonRolesSet));
		if (itemTypesInRoles!=null) {
			for (TRoleListTypeBean roleListTypeBean : itemTypesInRoles) {
				Integer roleID = roleListTypeBean.getRole();
				Integer itemTypeID = roleListTypeBean.getListType();
				Set<Integer> itemTypesInRoleSet = roleToItemTypesMap.get(roleID);
				if (itemTypesInRoleSet==null) {
					itemTypesInRoleSet = new HashSet<Integer>();
					roleToItemTypesMap.put(roleID, itemTypesInRoleSet);
				}
				itemTypesInRoleSet.add(itemTypeID);
			}
		}
		Set<Integer> projectSet = projectsToRolesMap.keySet();
		if (projectSet.isEmpty()) {
			LOGGER.info("No project assignment for person " + personID);
			return new LinkedList<TListTypeBean>();
		}
		//gets the project to projectType map
		List<TProjectBean> projectList = ProjectBL.loadByProjectIDs(GeneralUtils.createIntegerListFromCollection(projectSet));
		Map<Integer, Integer> projectToProjectTypeMap = new HashMap<Integer, Integer>();
		Set<Integer> projectTypesSet = new HashSet<Integer>();
		for (TProjectBean projectBean : projectList) {
			Integer projectTypeID = projectBean.getProjectType();
			projectToProjectTypeMap.put(projectBean.getObjectID(),projectTypeID);
			projectTypesSet.add(projectTypeID);
		}
		//gets the item type assignments for project types
		List<TPlistTypeBean> plistTypes = loadByProjectTypes(projectTypesSet.toArray());
		Map<Integer, Set<Integer>> projectTypeToItemTypesMap = new HashMap<Integer, Set<Integer>>();
		if (plistTypes!=null) {
			for (TPlistTypeBean plistTypeBean : plistTypes) {
				Integer projectTypeID = plistTypeBean.getProjectType();
				Integer itemTypeID = plistTypeBean.getCategory();
				Set<Integer> itemTypesSet = projectTypeToItemTypesMap.get(projectTypeID);
				if (itemTypesSet==null) {
					itemTypesSet = new HashSet<Integer>();
					projectTypeToItemTypesMap.put(projectTypeID, itemTypesSet);
				}
				itemTypesSet.add(itemTypeID);
			}
		}
		Set<Integer> allowedItemTypeIDs = new HashSet<Integer>();
		List<TListTypeBean>  allSelectableitemTypeBeans = IssueTypeBL.loadAllSelectable(locale);
		Set<Integer> allSelectableItemTypeIDs = GeneralUtils.createIntegerSetFromBeanList(allSelectableitemTypeBeans);
		for (Map.Entry<Integer, Set<Integer>> rolesInProjectEntry : projectsToRolesMap.entrySet()) {
			Integer projectID = rolesInProjectEntry.getKey();
			Set<Integer> roleIDs = rolesInProjectEntry.getValue();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(roleIDs.size() + " roles found in project " + projectID);
			}
			Integer projectTypeID = projectToProjectTypeMap.get(projectID);
			Set<Integer> projectTypeLimitedItemTypes = projectTypeToItemTypesMap.get(projectTypeID);
			Set<Integer> rolesLimitedItemTypes = new HashSet<Integer>();
			//get the item types limited in all roles
			for (Integer roleID : roleIDs) {
				Set<Integer> roleLimitedItemTypes = roleToItemTypesMap.get(roleID);
				if (roleLimitedItemTypes!=null) {
					rolesLimitedItemTypes.addAll(roleLimitedItemTypes);
				} else {
					//at least one role has no item type limitations
					rolesLimitedItemTypes.clear();
					break;
				}
			}
			if ((projectTypeLimitedItemTypes==null || projectTypeLimitedItemTypes.isEmpty()) && rolesLimitedItemTypes.isEmpty()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("No roles or project type specific limitation found for project " + projectID);
				}
				return allSelectableitemTypeBeans;
			} else {
				if (projectTypeLimitedItemTypes==null || projectTypeLimitedItemTypes.isEmpty()) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Add role specific item type limitations for project " + projectID);
					}
					allowedItemTypeIDs.addAll(rolesLimitedItemTypes);
				} else {
					if (rolesLimitedItemTypes.isEmpty()) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Add project type specific item type limitations for project " + projectID);
						}
						allowedItemTypeIDs.addAll(projectTypeLimitedItemTypes);
					} else {
						Collection<Integer> intersection = CollectionUtils.intersection(projectTypeLimitedItemTypes, rolesLimitedItemTypes);
						if (intersection!=null) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Add project type specific and role specific item type limitations for project " + projectID);
							}
							allowedItemTypeIDs.addAll(intersection);
						}
					}
				}
			}
		}
		allowedItemTypeIDs.retainAll(allSelectableItemTypeIDs);
		if (allowedItemTypeIDs.isEmpty()) {
			return new LinkedList<TListTypeBean>();
		} else {
			return LocalizeUtil.localizeDropDownList(loadByIssueTypeIDs(GeneralUtils.createIntegerListFromCollection(allowedItemTypeIDs)), locale);
		}
	}

	/**
	 * Gets the item types by projects involved in context and the person
	 * @param involvedProjects
	 * @param personID
	 * @param locale
	 * @param restrict whether to restrict the item types:
	 * 				true a subset valid for all involved projects (any issue type change in subset is valid)
	 * 				false a superset valid for any involved project (certain issue change may not be possible)
	 * @return
	 */
	public static List<TListTypeBean> getByProjectsAndPerson(Integer[] involvedProjects, Integer personID, Locale locale, boolean restrict) {
		List<TListTypeBean> datasource = null;
		if (involvedProjects!=null) {
			if (involvedProjects.length==1) {
				//an explicit project is selected for bulk operation or 
				//all selected issues are from the same project
				datasource = IssueTypeBL.loadByPersonAndProjectAndRight(personID, involvedProjects[0], 
						new int[] {AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.AccessFlagIndexes.PROJECTADMIN});
			} else {
				if (involvedProjects.length>1) {
					//the project field is not selected and the selected issues are from more projects:
					//get the intersection of the issueTypes allowed in all involved projects
					Set<Integer> intersection = null;
					for (int i = 0; i < involvedProjects.length; i++) {
						List<TListTypeBean> issueTypeBeans = IssueTypeBL.loadByPersonAndProjectAndRight(
								personID, involvedProjects[i], 
								new int[]{AccessBeans.AccessFlagIndexes.CREATETASK,
								AccessBeans.AccessFlagIndexes.PROJECTADMIN});
						Set<Integer> issueTypesIDsForProject = GeneralUtils.createIntegerSetFromBeanList(issueTypeBeans);
						if (intersection==null) {
							intersection = issueTypesIDsForProject;
						} else {
							if (restrict) {
								intersection.retainAll(issueTypesIDsForProject);
							} else {
								intersection.addAll(issueTypesIDsForProject);
							}
						}
					}
					datasource = IssueTypeBL.loadByIssueTypeIDs(GeneralUtils.createListFromCollection(intersection));
				} else {
					//no project selected for bulk operation: we  should work with 
					//the intersection of the issueTypes allowed in all involved projects
					datasource = IssueTypeBL.loadAllSelectable();
				}
			}
		}
		return LocalizeUtil.localizeDropDownList(datasource, locale);
	}
	
	/**
	 * Loads the issue types by IDs
	 * @param issueTypeIDs
	 */
	public static List<TListTypeBean> loadByIssueTypeIDs(List<Integer> issueTypeIDs) {
		return issueTypeDAO.loadByIssueTypeIDs(issueTypeIDs);
	}
	
	/**
	 * Load the issue types by label
	 * @param label
	 * @return
	 */
	public static List<TListTypeBean> loadByLabel(String label) {
		return issueTypeDAO.loadByLabel(label);
	}
	
	/**
	 * Return the issueTypeBeans with a specific typeFlag
	 * @param typeFlag
	 * @return
	 */
	public static List<TListTypeBean> loadByTypeFlag(int typeFlag) {
		return issueTypeDAO.loadByTypeFlag(typeFlag);
	}
	
	/**
	 * Saves a new/modified list entry
	 * If sortOrder is not set it will be set to be the next available sortOrder
	 * @param labelBean
	 * @param locale
	 * @return
	 */
	public static synchronized Integer save(ILabelBean labelBean, Locale locale) {
		TListTypeBean listTypeBean = (TListTypeBean)labelBean;
		boolean isNew = listTypeBean.getObjectID()==null;
		if (listTypeBean.getSortorder()==null) {
			Integer sortOrder = issueTypeDAO.getNextSortOrder(); 
			listTypeBean.setSortorder(sortOrder);
		}
		Integer objectID = issueTypeDAO.save(listTypeBean);
		if (isNew) {
			listTypeBean.setObjectID(objectID);
			addFilterAssignments(objectID);
			LocalizedListIndexer.getInstance().addLabelBean(listTypeBean,
					LuceneUtil.LOOKUPENTITYTYPES.LISTTYPE, isNew);
		} else {
			LocalizedListIndexer.getInstance().updateLabelBean(listTypeBean,
				LuceneUtil.LOOKUPENTITYTYPES.LISTTYPE);
		}
		LocalizeBL.saveSystemFieldLocalizedResource(
				LocalizeBL.RESOURCE_TYPES.ISSUETYPE, objectID, listTypeBean.getLabel(), locale);
		LookupContainer.resetLookupMap(SystemFields.INTEGER_ISSUETYPE);
		//cache and possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_ISSUETYPE, objectID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdate(isNew));
		return objectID;
	}
	
	/**
	 * Create an entry with the new issue type for each  
	 * project type in the associated TPCATEGORY table, 
	 * which have at least one other issue type assigned.
	 * This is to initially enable a new issue type
	 * for all project types. Users may then go and remove
	 * certain entries so that the new value is only enabled for some
	 * project types.
	 * @param newIssueTypeID
	 */
	private static void addFilterAssignments(Integer newIssueTypeID) {
		List<TPlistTypeBean> plistTypeBeanList = pIssueTypeDAO.loadAll();
		Set<Integer> projectTypesWithIssueTypes = new HashSet<Integer>(); 
		//probably not needed if this is called only by creating a new issue type 
		Set<Integer> projectTypesWithThisIssueType = new HashSet<Integer>();
		for (TPlistTypeBean plistTypeBean : plistTypeBeanList) {
			Integer projectTypeID = plistTypeBean.getProjectType();
			Integer issuetypeID = plistTypeBean.getCategory();
			if (projectTypeID!=null && issuetypeID!=null) {
				projectTypesWithIssueTypes.add(projectTypeID);
				if (issuetypeID.equals(newIssueTypeID)) {
					projectTypesWithThisIssueType.add(projectTypeID);
				}
			}
		}		
		for (Integer projectTypeID : projectTypesWithIssueTypes) {
			//do not add for the same issue type again (not the case for new issue types)
			if (!projectTypesWithThisIssueType.contains(projectTypeID)) {
				insertAssignment(projectTypeID, newIssueTypeID);
			}
		}
	}
	
	/**
	 * Inserts a new assignment
	 * @param row
	 * @param column
	 * @return
	 */
	private static Integer insertAssignment(Integer row, Integer column) {
		TPlistTypeBean plistTypeBean = new TPlistTypeBean();
		plistTypeBean.setProjectType(row);
		plistTypeBean.setCategory(column);
		return pIssueTypeDAO.save(plistTypeBean);
	}
	
	/**
	 * Saves the label bean into the database  
	 */
	public static Integer saveSimple(TListTypeBean listTypeBean) {
		Integer itemTypeID = issueTypeDAO.save(listTypeBean);
		LookupContainer.reloadNotLocalizedLabelBean(SystemFields.INTEGER_ISSUETYPE, itemTypeID);
		//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied   
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_ISSUETYPE, itemTypeID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
		return itemTypeID;
	}
	
	/**
	 * Deletes the issue type
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		if (objectID!=null) {
			TListTypeBean listTypeBean = IssueTypeBL.loadByPrimaryKey(objectID);
			Integer iconKey = null;
			if (listTypeBean!=null) {
				iconKey = listTypeBean.getIconKey();
			}
			issueTypeDAO.delete(objectID);
			if (iconKey!=null) {
				BlobBL.delete(iconKey);
			}
			//delete the issue type from lucene index
			LocalizedListIndexer.getInstance().deleteByKeyAndType(
					objectID, LuceneUtil.LOOKUPENTITYTYPES.LISTTYPE);
			//remove localized resource
			LocalizeBL.removeLocalizedResources(new TListTypeBean().getKeyPrefix(), objectID);
			LookupContainer.resetLookupMap(SystemFields.INTEGER_ISSUETYPE);
			//cache and possible lucene update in other nodes
			ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_ISSUETYPE, objectID, ClusterMarkChangesBL.getChangeTypeByDelete());
		}
	}
	
	/** 
	 * This method replaces all occurrences of issue type value oldOID with
	 * state value newOID and then deletes the issue type
	 * @param oldOID
	 * @param newOID
	 */
	public static void replaceAndDelete(Integer oldOID, Integer newOID) {
		if (newOID!=null) {
			int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.ISSUETYPE, oldOID);
			issueTypeDAO.replace(oldOID, newOID);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_ISSUETYPE, true);
			FieldChangeBL.replaceSystemOptionInHistory(oldOID, newOID, SystemFields.INTEGER_ISSUETYPE, false);
			//reindex the affected workItems 
			LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_ISSUETYPE);
			ClusterMarkChangesBL.markDirtyWorkItemsInCluster(reindexWorkItems);
		}
		delete(oldOID);
	}
	
	/**
	 * Loads the edited child filter assignments for issue types 
	 * @param childIssueTypes
	 * @return
	 */
	public static Map<Integer, Map<Integer, Integer>> loadEditedChildAssignments(List<Integer> childIssueTypes) {
		Map<Integer, Map<Integer, Integer>> projectTypeToIssueTypeByProjectTypeMap = new HashMap<Integer, Map<Integer,Integer>>();
		List<TChildIssueTypeBean> projectTypeToIssueTypeByProjectType = ChildIssueTypeAssignmentsBL.loadByChildAssignments(childIssueTypes);
		for (TChildIssueTypeBean childIssueTypeBean : projectTypeToIssueTypeByProjectType) {
			Integer childIssueTypeID = childIssueTypeBean.getIssueTypeChild();
			Integer parentIssueType = childIssueTypeBean.getIssueTypeParent();
			Map<Integer, Integer> issueTypesForRow = projectTypeToIssueTypeByProjectTypeMap.get(childIssueTypeID);
			if (issueTypesForRow==null) {
				issueTypesForRow = new HashMap<Integer, Integer>();
				projectTypeToIssueTypeByProjectTypeMap.put(childIssueTypeID, issueTypesForRow);
			}
			issueTypesForRow.put(parentIssueType, childIssueTypeBean.getObjectID());
		}
		return projectTypeToIssueTypeByProjectTypeMap;
	}
	
	/**
	 * Load all issue types allowed for the project type the project belongs to 
	 * @param projectType
	 * @return
	 */
	public static List<TListTypeBean> loadAllowedByProjectType(Integer projectType) {
		return loadAllowedByProjectType(projectType, null);
	}
	
	/**
	 * Load all issue types allowed for the project type the project belongs to 
	 * @param projectType
	 * @param whether to get only the document type item types or the non document type item types or all item types
	 * @return
	 */
	public static List<TListTypeBean> loadAllowedByProjectType(Integer projectType, Boolean documents) {
		List<TListTypeBean> allowedByIssueTypes=null;
		if (documents==null) {
			allowedByIssueTypes = issueTypeDAO.loadAllowedByProjectType(projectType);
		} else {
			if (documents.booleanValue()==true) {
				allowedByIssueTypes = issueTypeDAO.loadAllowedDocumentTypesByProjectType(projectType);
			} else {
				allowedByIssueTypes = issueTypeDAO.loadAllowedNonDocumentTypesByProjectType(projectType);
			}
		}
		if (allowedByIssueTypes==null || allowedByIssueTypes.isEmpty()) {
			if (documents==null) {
				return IssueTypeBL.loadAll();
			} else {
				if (documents.booleanValue()==true){
				  return IssueTypeBL.loadAllDocumentTypes();
				} else {
					return IssueTypeBL.loadAllSelectable();
				}
			}
		} else {
			return allowedByIssueTypes;
		}
	}
	
	/**
	 * Load all issueTypes which are allowed in the projectTypes
	 * @param projectTypeIDs
	 * @return
	 */
	public static List<TListTypeBean> loadAllowedByProjectTypes(Integer[] projectTypeIDs) {	
		boolean allProjectTypesHaveIssueTypeRestriction = projectTypeDAO.allHaveIssueTypeRestrictions(projectTypeIDs);
		if (allProjectTypesHaveIssueTypeRestriction) {
			return issueTypeDAO.loadAllowedByProjectTypes(projectTypeIDs);
		} else {
			return IssueTypeBL.loadAllSelectable();
		}
	}
	
	/**
	 * Loads the issue types with corresponding right for a person in a project
	 * @param personID
	 * @param projectID
	 * @param rightFlags
	 * @return
	 */
	public static List<TListTypeBean> loadByPersonAndProjectAndRight(Integer personID, Integer projectID, int[] rightFlags) {
		return loadByPersonAndProjectAndRight(personID,projectID,rightFlags,false);
	}
	public static List<TListTypeBean> loadByPersonAndProjectAndRight(Integer personID, Integer projectID, int[] rightFlags, boolean documents) {
		List<TListTypeBean> resultList = new LinkedList<TListTypeBean>();
		if (projectID==null) {
			return resultList;
		}
		//allowed by project type
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		List<TListTypeBean> issueTypesAllowedByProjectType = loadAllowedByProjectType(projectBean.getProjectType(),documents);
		
		//get the roles
		Set<Integer> rolesSet = AccessBeans.getRolesWithRightForPersonInProject(personID, projectID, rightFlags);
		if (rolesSet==null || rolesSet.isEmpty()) {
			return resultList;
		}
		Object[] roleIDs = rolesSet.toArray();
		
		//roles with issueType restrictions
		List<TRoleBean> roleWithExplicitIssueType = RoleBL.loadWithExplicitIssueType(roleIDs);
		
		//at least one role has no restrictions for listType 
		if (roleWithExplicitIssueType!=null && roleWithExplicitIssueType.size()<rolesSet.size()) {
			//load all list types allowed in project type
			return issueTypesAllowedByProjectType;
		}		
		//all roles have explicit issue type restrictions
		//get all those explicit issueTypes for roles
		List<TListTypeBean> allowedByRoles = issueTypeDAO.loadForRoles(roleIDs);
		Map<Integer, TListTypeBean> allowedByProjectTypeMap = GeneralUtils.createMapFromList(issueTypesAllowedByProjectType);
		if (allowedByRoles!=null) {
			Iterator<TListTypeBean> iterator = allowedByRoles.iterator();
			while (iterator.hasNext()) {
				TListTypeBean listTypeBean = iterator.next();
				if (!allowedByProjectTypeMap.containsKey(listTypeBean.getObjectID())) {
					iterator.remove();
				}
			}
		}
		return allowedByRoles;
	}
	
	/**
	 * Loads the issue types with corresponding right for a person in a project and for a parent's workItemID if set
	 * @param personID
	 * @param projectID
	 * @param currentIssueType
	 * @param parentID
	 * @return
	 */
	public static List<TListTypeBean> loadByPersonAndProjectAndCreateRight(Integer personID, Integer projectID, Integer currentIssueType, Integer parentID, Locale locale) {
		return loadByPersonAndProjectAndCreateRight(personID, projectID, currentIssueType, parentID, locale,false);
	}
	public static List<TListTypeBean> loadDocumentsByPersonAndProjectAndCreateRight(Integer personID, Integer projectID, Integer currentIssueType, Integer parentID, Locale locale) {
		return loadByPersonAndProjectAndCreateRight(personID, projectID, currentIssueType, parentID, locale,true);
	}

	public static List<TListTypeBean> loadByPersonAndProjectAndCreateRight(Integer personID, Integer projectID, Integer currentIssueType, Integer parentID, Locale locale,boolean documents) {
		List<TListTypeBean> issueTypeBeans = loadByPersonAndProjectAndRight(personID, projectID,
				new int[] {AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.AccessFlagIndexes.PROJECTADMIN},documents);
		if (parentID!=null) {
			TWorkItemBean workItemBeanParent = null;
			try {
				workItemBeanParent = ItemBL.loadWorkItem(parentID);
			} catch (ItemLoaderException e) {
			}
			if (workItemBeanParent!=null) {
				//allowed issue types by parent issue type
				List<TChildIssueTypeBean> childIssueTypes = ChildIssueTypeAssignmentsBL.loadByChildAssignmentsByParent(workItemBeanParent.getListTypeID());
				if (childIssueTypes!=null && !childIssueTypes.isEmpty()) {
					Set<Integer> childIssueTypeIDs = new HashSet<Integer>();
					for (TChildIssueTypeBean childIssueTypeBean : childIssueTypes) {
						childIssueTypeIDs.add(childIssueTypeBean.getIssueTypeChild());
					}
					List<TListTypeBean> filteredIssueTypeBeans = new LinkedList<TListTypeBean>();
					for (TListTypeBean listTypeBean : issueTypeBeans) {
						if (childIssueTypeIDs.contains(listTypeBean.getObjectID()) ||
								(currentIssueType!=null && listTypeBean.getObjectID().equals(currentIssueType))) {
							filteredIssueTypeBeans.add(listTypeBean);
						}
					}
					return LocalizeUtil.localizeDropDownList(filteredIssueTypeBeans, locale);
				}
			}
		}
		return LocalizeUtil.localizeDropDownList(issueTypeBeans, locale);
	}

	/**
	 * Load the TPlistTypeBean for projectTypeIDs
	 * @param projectTypeIDs
	 * @return
	 */
	public static List<TPlistTypeBean> loadByProjectTypes(Object[] projectTypeIDs) {
		return pIssueTypeDAO.loadByProjectTypes(projectTypeIDs);
	}


	
	
	
	
	
	
	
	
	
	
	
	
}
