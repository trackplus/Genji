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


package com.aurel.track.admin.project;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ProjectDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.report.execute.SimpleTreeNode;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.TreeNode;

/**
 * General, project related business logic
 * @author Tamas Ruff
 *
 */
public class ProjectBL {
	private static final Logger LOGGER = LogManager.getLogger(ProjectBL.class);
	private static ProjectDAO projectDAO = DAOFactory.getFactory().getProjectDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	static final int INVALID_DEFAULT_ACCOUNT = 0; 
	
	public static String PROJECT_ICON_CLASS = "projects-ticon";
	public static String PROJECT_INACTIVE_ICON_CLASS = "project-Inactive-ticon";
	public static String PROJECT_CLOSED_ICON_CLASS = "project-Closed-ticon";
	
	
	
	/**
	 * Icon for not selectable project: for ex. it was already assigned but it 
	 * has selectable children so it can't be excluded from the project tree 
	 */
	public static String PROJECT_NOT_SELECTABLE_ICON_CLASS = "project-notSelectable-ticon";
	
	
	
	/**
	 * Saves a projectBean
	 * @param projectBean
	 * @return
	 */
	public static Integer save(TProjectBean projectBean) {
		boolean isNew = projectBean.getObjectID() == null;
		Integer projectID = projectDAO.save(projectBean);
		if (isNew) {
			projectBean.setObjectID(projectID);
			NotLocalizedListIndexer.getInstance().addLabelBean(
					projectBean, LuceneUtil.LOOKUPENTITYTYPES.PROJECT, isNew);
		} else {
			NotLocalizedListIndexer.getInstance().updateLabelBean(
					projectBean, LuceneUtil.LOOKUPENTITYTYPES.PROJECT);
			//reindex the affected workItems 
			int[] workItemIDsArr = workItemDAO.loadBySystemList(SystemFields.PROJECT, projectID);
			LuceneIndexer.updateWorkItemIndex(workItemIDsArr, SystemFields.INTEGER_PROJECT);
			ClusterMarkChangesBL.markDirtyWorkItemsInCluster(workItemIDsArr);
		}
		LookupContainer.reloadProject(projectID);
		//cache and possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PROJECT, projectID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdate(isNew));
		return projectID;
	}
	
	/**
	 * Saves a project bean
	 * @param projectBean
	 */
	public static Integer saveSimple(TProjectBean projectBean) {
		Integer projectID = projectDAO.save(projectBean);
		LookupContainer.reloadProject(projectID);
		//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied   
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PROJECT, projectID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
		return projectID;
	}

	/**
	 * Helper for deep and shallow copy 
	 * @param projectOriginal
	 * @param deep
	 * @return
	 */
	public static TProjectBean copy(TProjectBean projectOriginal, boolean deep) {
		return projectDAO.copy(projectOriginal, deep);
	}
	
	/**
	 * Load a projectBean by primary key
	 * @param projectID
	 * @return
	 */
	public static TProjectBean loadByPrimaryKey(Integer projectID) {
		return projectDAO.loadByPrimaryKey(projectID);
	}

	/**
	 * Load a projectBean by label
	 * @param label
	 * @return
	 */
	public static TProjectBean loadByLabel(String label) {
		return projectDAO.loadByLabel(label);
	}
	
	/**
	 * Load a projectBean by prefix
	 * @param prefix
	 * @return
	 */
	public static List<TProjectBean> loadByPrefix(String prefix) {
		return projectDAO.loadByPrefix(prefix);
	}
	
	/**
	 * Load a projectBean by primary key
	 * @param personID
	 * @return
	 */
	public static boolean loadPrivateProject(Integer personID) {
		return projectDAO.hasPrivateProject(personID);
	}
	
	/**
	 * Loads the private project for the person
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> getPrivateProject(Integer personID) {
		return projectDAO.getPrivateProject(personID);
	}
	
	/**
	 * Get the IDs of the closed projects form projectIDs
	 * @param projectIDs
	 * @return
	 */
	public static Set<Integer> getClosedProjectIDs(List<Integer> projectIDs) {
		return projectDAO.getClosedProjectIDs(projectIDs);
	}
	
	/**
	 * Whether a project with such name exists already
	 * @param name
	 * @param parentProjectID if null search in main projects
	 * @param exceptProjectID except the currently edited project
	 * @return
	 */
	public static boolean projectNameExists(String name, Integer parentProjectID, Integer exceptProjectID) {
		return projectDAO.projectNameExists(name, parentProjectID, exceptProjectID, true);
	}
	
	/**
	 * Whether a project with such name exists already
	 * @param prefix
	 * @param parentProjectID if null search in main projects
	 * @param exceptProjectID except the currently edited project
	 * @return
	 */
	public static boolean projectPrefixExists(String prefix, Integer parentProjectID, Integer exceptProjectID) {
		return projectDAO.projectPrefixExists(prefix, parentProjectID, exceptProjectID, true);
	}
	
	/**
	 * Loads a ProjectBean list by projectIDs
	 * @param projectIDs
	 * @return
	 */
	public static List<TProjectBean> loadByProjectIDs(List<Integer> projectIDs) {
		return projectDAO.loadByProjectIDs(projectIDs);
	}
	
	/**
	 * Load all projectBeans
	 * @return
	 */
	public static List<TProjectBean> loadAll() {
		return projectDAO.loadAll();
	}
	
	/**
	 * Count the number of projects
	 * @param all: if true count all projects (mainProjects parameter has no importance) 
	 * @param mainProjects: if all is false whether to count the main- or the sub-projects
	 * @return
	 */
	public static int count(boolean all, boolean mainProjects) {
		return projectDAO.count(all, mainProjects);
	}
	
	/**
	 * Loads a projectBean list by a project type
	 * @param projectTypeID
	 * @return
	 */
	public static List<TProjectBean> loadByProjectType(Integer projectTypeID) {
		return projectDAO.loadByProjectType(projectTypeID);
	}
	
	/**
	 * Loads a ProjectBean list by workItemKeys
	 * @param workItemIDs
	 * @return
	 */
	public static List<TProjectBean> loadByWorkItemKeys(int[] workItemIDs) {
		return projectDAO.loadByWorkItemKeys(workItemIDs);
	}
	
	/**
	 * Loads a ProjectBean by workItemID
	 * @param workItemID
	 * @return
	 */
	public static TProjectBean loadByWorkItemKey(Integer workItemID) {
		return projectDAO.loadByWorkItemKey(workItemID);
	}
	
	/**
	 * Load the projects from the projectIDs having any of projectStates
	 * @param projectIDs
	 * @param projectStates
	 * @return
	 */
	public static Set<Integer> filterProjectsIDsByStates(Set<Integer> projectIDs, int[] projectStates) {
		return projectDAO.filterProjectsIDsByStates(projectIDs, projectStates);
	}
	
	private static List<TProjectBean> loadByWorkItemBeans(List<TWorkItemBean> issues) {
		Set<Integer> projects=new HashSet<Integer>();
		if (issues!=null) {
			for (TWorkItemBean workItemBean : issues) {
				projects.add(workItemBean.getProjectID());
			}
		}
		return ProjectBL.loadByProjectIDs(GeneralUtils.createListFromSet(projects));
	}
	
	public static Map<Integer,TProjectBean> loadByWorkItemBeansAsMap(List<TWorkItemBean> issues){
		List<TProjectBean> projectBeanList=loadByWorkItemBeans(issues);
		return GeneralUtils.createMapFromList(projectBeanList);
	}
	
	/**
	 *****************************************************************************************************
	 * Flat project methods: returning projectBean lists
	 * (possibly containing both main and sub projects) satisfying a certain criteria
	 ***************************************************************************************************** 
	 */
	
	/**
	 * Load all project beans (possibly containing both main and sub projects) except those closed: not explicit project
	 * right dependent but should be called only be system administrator
	 * @return
	 */
	public static List<TProjectBean> loadActiveInactiveProjectsFlat() {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE});
		return projectDAO.loadProjectsByStateIDs(projectStates);
	}
	
	/**
	 * Loads all projects with explicit right for the personID (flat list possibly containing both main and sub projects) 
	 * @param personID
	 * @param rights
	 * @return
	 */
	public static List<TProjectBean> loadActiveInactiveProjectsByRights(Integer personID, int[] rights) {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE});
		List<Integer> personIDs = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		return projectDAO.loadByUserAndRightAndState(personIDs, rights, projectStates);
	}
	
	/**
	 * Loads the not closed projects where the person has
	 * read right or raci role in at least one workItem form the project 
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> loadUsedProjectsFlat(Integer personID) {
		Set<Integer> projectIDs = new HashSet<Integer>();
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE});
		List<Integer> meAndMySubstituted = AccessBeans.getMeAndSubstituted(personID);
		List<Integer> meAndMySubstitutedAndGroups = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		List<TProjectBean> projectsWithRight = projectDAO.loadByUserAndRightAndState(meAndMySubstitutedAndGroups, null, projectStates);
		List<TProjectBean> projectsWithRightByParents = getProjectBeansFlatByParents(projectsWithRight, false);
		addToSet(projectIDs, projectsWithRightByParents);
		Set<Integer> raciProjects = getRACIProjectsSet(meAndMySubstituted, meAndMySubstitutedAndGroups, projectStates);
		if (raciProjects!=null && !raciProjects.isEmpty()) {
			projectIDs.addAll(raciProjects);
		}
		if (projectIDs.isEmpty()) {
			return new LinkedList<TProjectBean>();
		}
		List<TProjectBean> projectBeans  = loadByProjectIDs(GeneralUtils.createListFromSet(projectIDs));
		removeNotOwnedPrivateProjects(projectBeans, personID);
		return projectBeans;
	}
	
	/**
	 * Gets the RACI projects
	 * @param meAndMySubstituted
	 * @param meAndMySubstitutedAndGroups
	 * @param projectStates
	 * @return
	 */
	private static Set<Integer> getRACIProjectsSet(List<Integer> meAndMySubstituted, List<Integer> meAndMySubstitutedAndGroups, int[] projectStates) {
		Set<Integer> raciProjects = new HashSet<Integer>();
		//avoid the joins for the projects already found
		Set<Integer> origManRespManProjectsIDs = workItemDAO.loadOrigManRespProjects(meAndMySubstituted, meAndMySubstitutedAndGroups);
		if (origManRespManProjectsIDs!=null && !origManRespManProjectsIDs.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Number of originator/manager/responsible projects: " + origManRespManProjectsIDs.size());
			}
			raciProjects.addAll(origManRespManProjectsIDs);
		}
		Set<Integer> watcherProjectsIDs = workItemDAO.loadConsultantInformantProjects(meAndMySubstitutedAndGroups);
		if (watcherProjectsIDs!=null && !watcherProjectsIDs.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Number of watcher projects: " + watcherProjectsIDs.size());
			}
			raciProjects.addAll(watcherProjectsIDs);
		}
		List<Integer> onBehalfPickerFieldIDs = FieldBL.getOnBehalfOfUserPickerFieldIDs();
		if (onBehalfPickerFieldIDs!=null && !onBehalfPickerFieldIDs.isEmpty()) {
			Set<Integer> onBehalfPickerProjectIDs = workItemDAO.loadOnBehalfOfProjects(meAndMySubstitutedAndGroups, onBehalfPickerFieldIDs);
			if (onBehalfPickerProjectIDs!=null && !onBehalfPickerProjectIDs.isEmpty()) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Number of on behalf of projects: " + onBehalfPickerProjectIDs.size());
				}
				raciProjects.addAll(onBehalfPickerProjectIDs);
			}
		}
		if (!raciProjects.isEmpty()) {
			raciProjects = filterProjectsIDsByStates(raciProjects, projectStates);
			if (LOGGER.isDebugEnabled() && raciProjects!=null) {
				LOGGER.debug("Number of not closed RACI projects: " + raciProjects.size());
			}
		}
		return raciProjects;
	}
	
	private static void addToSet(Set<Integer> projectIDs, List<TProjectBean> projectList) {
		if (projectIDs!=null ) {
			for (TProjectBean projectBean : projectList) {
				projectIDs.add(projectBean.getObjectID());
			}
			return;
		}
	}
	
	/**
	 * Loads the active projects where the person has read right
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> loadProjectsFlatByRight(Integer personID, int[] rights, boolean activeOnly) {
		List<TProjectBean> firstLevelProjectsWithReadIssueRight = null;
		if (activeOnly) {
			firstLevelProjectsWithReadIssueRight = getActiveFirstLevelProjectsByRights(
				LookupContainer.getPersonBean(personID), false, rights);
		} else {
			firstLevelProjectsWithReadIssueRight = getActiveInactiveFirstLevelProjectsByRights(
				LookupContainer.getPersonBean(personID), false, rights);
		}
		//get the descendants of those projects
		return getProjectBeansFlatByParents(firstLevelProjectsWithReadIssueRight, activeOnly);
	}
	
	/**
	 * Loads the active projects where the person has read right
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> loadActiveProjectsWithReadIssueRight(Integer personID) {
		int[] rights = new int[] {AccessBeans.AccessFlagIndexes.READANYTASK,
				AccessBeans.AccessFlagIndexes.PROJECTADMIN};
		List<TProjectBean> firstLevelProjectsWithReadIssueRight = getActiveFirstLevelProjectsByRights(
				LookupContainer.getPersonBean(personID), false, rights);
		//get the descendants of those projects
		return getProjectBeansFlatByParents(firstLevelProjectsWithReadIssueRight, true);
	}

	/**
	 * Loads the active and inactive projects where the person has read right
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> loadProjectsWithReadIssueRight(Integer personID) {
		int[] rights = new int[] {AccessBeans.AccessFlagIndexes.READANYTASK,
			AccessBeans.AccessFlagIndexes.PROJECTADMIN};
		List<TProjectBean> firstLevelProjectsWithReadIssueRight = getActiveInactiveFirstLevelProjectsByRights(
			LookupContainer.getPersonBean(personID), false, rights);
		//get the descendants of those projects 
		return getProjectBeansFlatByParents(firstLevelProjectsWithReadIssueRight, false);
	}

	/**
	 * Loads the not closed projects where the person has create right
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> loadProjectsWithCreateIssueRight(Integer personID) {
		int[] rights = new int[]{AccessBeans.AccessFlagIndexes.CREATETASK,
				AccessBeans.AccessFlagIndexes.PROJECTADMIN};
		return loadProjectsFlatByRight(personID, rights, true);
	 }
	
	
	/**
	 * Loads the not closed projects where the person has modify right
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> loadProjectsWithModifyIssueRight(Integer personID) {
		int[] rights = new int[]{AccessBeans.AccessFlagIndexes.MODIFYANYTASK,
				AccessBeans.AccessFlagIndexes.PROJECTADMIN};
		return loadProjectsFlatByRight(personID, rights, true);
	}
	
	/**
	 * Loads the not closed projects where the person has any right from right array and the project is allowed for any of the itemTypes 
	 * @param personID
	 * @return
	 */
	public static List<TProjectBean> loadFirstLevelProjectsByRightsAndItemTypeFlags(Integer personID, int[] rights, int[] itemTypeFlags) {
		List<TProjectBean> projectBeans = loadProjectsFlatByRight(personID, rights, false);
		List<TProjectBean> allProjectsWithRight = getDescendantProjects(projectBeans, true);
		Set<Integer> involvedProjectTypes = new HashSet<Integer>();
		for (TProjectBean projectBean : allProjectsWithRight) {
			involvedProjectTypes.add(projectBean.getProjectType());
		}
		involvedProjectTypes = ProjectTypesBL.removeNotAllowingItemTypeFlags(involvedProjectTypes, itemTypeFlags);
		for (Iterator<TProjectBean> iterator = allProjectsWithRight.iterator(); iterator.hasNext();) {
			TProjectBean projectBean = iterator.next();
			Integer projectTypeID = projectBean.getProjectType();
			if (!involvedProjectTypes.contains(projectTypeID)) {
				iterator.remove();
			}
		}
		return filterProjectsIfNotAllAncestorPresent(allProjectsWithRight);
	}
	
	/**
	 * Loads the not closed subprojects allowed for any of the itemTypes 
	 * @param projectID
	 * @param itemTypeFlags
	 * @return
	 */
	public static List<TProjectBean> loadSubprojectsByItemTypeFlags(Integer projectID, int[] itemTypeFlags) {
		List<TProjectBean> subprojectBeans = loadActiveInactiveSubrojects(projectID);
		Set<Integer> involvedProjectTypes = new HashSet<Integer>();
		for (TProjectBean projectBean : subprojectBeans) {
			involvedProjectTypes.add(projectBean.getProjectType());
		}
		involvedProjectTypes = ProjectTypesBL.removeNotAllowingItemTypeFlags(involvedProjectTypes, itemTypeFlags);
		for (Iterator<TProjectBean> iterator = subprojectBeans.iterator(); iterator.hasNext();) {
			TProjectBean projectBean = iterator.next();
			Integer projectTypeID = projectBean.getProjectType();
			if (!involvedProjectTypes.contains(projectTypeID)) {
				iterator.remove();
			}
		}
		return subprojectBeans;
	 }

	
	/**
	 * Gets all not closed projects eagerly (the entire hierarchy at once)
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @return
	 */
	public static List<TProjectBean> getAllNotClosedAdminProjectBeansFlat(TPersonBean personBean, boolean allIfSystemAdmin) {
		//get first level admin projects
		List<TProjectBean> projectBeans = getActiveInactiveFirstLevelProjectsByProjectAdmin(personBean, allIfSystemAdmin);
		//get the descendants of those projects 
		return getProjectBeansFlatByParents(projectBeans, false);
	}
	
	/**
	 * Gets all not closed projects eagerly (the entire hierarchy at once)
	 * @param firstLevelProjectBeans
	 * @return
	 */
	private static List<TProjectBean> getProjectBeansFlatByParents(List<TProjectBean> firstLevelProjectBeans, boolean activeOnly) {
		List<TProjectBean> flatProjectBeans = new LinkedList<TProjectBean>();
		List<Integer> parentIDs = new LinkedList<Integer>();
		Set<Integer> ancestorIDs = new HashSet<Integer>();
		if (firstLevelProjectBeans!=null) {
			for (TProjectBean projectBean : firstLevelProjectBeans) {	
				flatProjectBeans.add(projectBean);
				Integer projectID = projectBean.getObjectID();
				parentIDs.add(projectID);
				ancestorIDs.add(projectID);
			}
			//now get the subprojects
			while (!parentIDs.isEmpty()) {
				List<TProjectBean> childProjectBeans = null;
				if (activeOnly) {
					childProjectBeans = loadActiveSubrojects(parentIDs);
				} else {
					childProjectBeans = loadActiveInactiveSubrojects(parentIDs);
				}
				parentIDs = new LinkedList<Integer>();
				for (TProjectBean projectBean : childProjectBeans) {
					flatProjectBeans.add(projectBean);
					Integer projectID = projectBean.getObjectID();
					if (!ancestorIDs.contains(projectID)) {
						parentIDs.add(projectID);
						ancestorIDs.add(projectID);
					}
				}
			}
		}
		return flatProjectBeans;
	}
	
	/**
	 * Gets the project ancestor projects, including the project also
	 * @param projectID
	 * @return
	 */
	public static List<Integer> getAncestorProjects(Integer projectID) {
		List<Integer> ancestorProjectIDs = new LinkedList<Integer>();
		Integer parentProject = projectID;
		while (parentProject!=null) {
			ancestorProjectIDs.add(parentProject);
			TProjectBean projectBean = loadByPrimaryKey(parentProject);
			if (projectBean!=null) {
				parentProject = projectBean.getParent();
			} else {
				parentProject = null;
			}
		}
		return ancestorProjectIDs;
	}

	/**
	 * Gets the ancestor projects, including the projectIDs also
	 * @param arrProjectIDs
	 * @return
	 */
	public static Integer[] getAncestorProjects(Integer[] arrProjectIDs) {
		Set<Integer> ancestorProjectIDs = new HashSet<Integer>();
		List<Integer> projectIDs = GeneralUtils.createListFromIntArr(arrProjectIDs);
		while (!projectIDs.isEmpty()) {
			List<TProjectBean> projectBeans = loadByProjectIDs(projectIDs);
			projectIDs = new LinkedList<Integer>();
			if (projectBeans!=null) {
				for (TProjectBean projectBean : projectBeans) {
					Integer projectID = projectBean.getObjectID();
					if (!projectIDs.contains(projectID)) {
						//was not added already by another descendant
						ancestorProjectIDs.add(projectID);
						Integer parentID = projectBean.getParent();
						if (parentID!=null&&!ancestorProjectIDs.contains(parentID)) {
							projectIDs.add(parentID);
						}
					}
				}
			}
		}
		return GeneralUtils.createIntegerArrFromCollection(ancestorProjectIDs);
	}
	
	/**
	 * Gets the ancestor projects, including the projectIDs also
	 * @param arrProjectIDs
	 * @return
	 */
	public static Map<Integer, Integer> getAncestorProjectHierarchy(Integer[] arrProjectIDs) {
		Map<Integer, Integer> projectToParent = new HashMap<Integer, Integer>();
		Set<Integer> ancestorProjectIDs = new HashSet<Integer>();
		List<Integer> projectIDs = GeneralUtils.createListFromIntArr(arrProjectIDs);
		while (!projectIDs.isEmpty()) {
			List<TProjectBean> projectBeans = loadByProjectIDs(projectIDs);
			projectIDs = new LinkedList<Integer>();
			for (TProjectBean projectBean : projectBeans) {
				Integer projectID = projectBean.getObjectID();
				if (!projectIDs.contains(projectID)) {
					ancestorProjectIDs.add(projectID);
					Integer parentID = projectBean.getParent();
					if (parentID!=null&&!ancestorProjectIDs.contains(parentID)) {
						projectIDs.add(parentID);
					}
					//parent could be null!!!!
					projectToParent.put(projectID, parentID);
				}
			}
		}
		return projectToParent;
	}
	
	
	/**
	 * Gets the ancestor projects, including the projectIDs also
	 * @param arrProjectIDs
	 * @return
	 */
	public static Integer[] getDescendantProjectIDs(Integer[] arrProjectIDs) {
		List<Integer> descendentProjectIDs = getDescendantProjectIDsAsList(arrProjectIDs);
		return GeneralUtils.createIntegerArrFromCollection(descendentProjectIDs);
	}

	public static List<Integer> getDescendantProjectIDsAsList(Integer[] arrProjectIDs) {
		List<Integer> descendentProjectIDs = new ArrayList<Integer>();
		if (arrProjectIDs!=null && arrProjectIDs.length>0) {
			List<Integer> projectIDs = GeneralUtils.createListFromIntArr(arrProjectIDs);
			List<TProjectBean> projectBeans = loadByProjectIDs(projectIDs);
			if (projectBeans!=null) {
				for (TProjectBean projectBean : projectBeans) {
					Integer projectID = projectBean.getObjectID();
					descendentProjectIDs.add(projectID);
				}
				while (!projectIDs.isEmpty()) {
					projectBeans = loadActiveInactiveSubrojects(projectIDs);
					projectIDs = new LinkedList<Integer>();
					for (TProjectBean projectBean : projectBeans) {
						Integer projectID = projectBean.getObjectID();
						if (!descendentProjectIDs.contains(projectID)) {
							descendentProjectIDs.add(projectID);
							projectIDs.add(projectID);
						}
					}
				}
			}
		}
		return descendentProjectIDs;
	}

	public static List<Integer> getDescendantProjectIDsAsList(Integer projectID) {
		return getDescendantProjectIDsAsList(new Integer[]{projectID});
	}
	public static List<TProjectBean> getDescendantProjects(List<TProjectBean> projectBeans) {
		return getDescendantProjects(projectBeans,true);
	}
	
	/**
	 * Gets the descendant projects for a base list
	 * @param projectBeans
	 * @param selfIncluded
	 * @return
	 */
	public static List<TProjectBean> getDescendantProjects(List<TProjectBean> projectBeans, boolean selfIncluded) {
		List<TProjectBean> result = new ArrayList<TProjectBean>();
		if (projectBeans!=null && !projectBeans.isEmpty()) {
			List<Integer> descendentProjectIDs = new ArrayList<Integer>();
			List<Integer> projectIDs = GeneralUtils.createIntegerListFromBeanList(projectBeans);
			for (TProjectBean projectBean : projectBeans) {
				Integer projectID = projectBean.getObjectID();
				descendentProjectIDs.add(projectID);
				if(selfIncluded){
					result.add(projectBean);
				}
			}
			while (!projectIDs.isEmpty()) {
				projectBeans = loadActiveInactiveSubrojects(projectIDs);
				projectIDs = new LinkedList<Integer>();
				for (TProjectBean projectBean : projectBeans) {
					Integer projectID = projectBean.getObjectID();
					if (!descendentProjectIDs.contains(projectID)) {
						descendentProjectIDs.add(projectID);
						result.add(projectBean);
						projectIDs.add(projectID);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Gets the descendant projects map for a base list
	 * @param baseProjectBeans
	 * @return
	 */
	public static Map<Integer, Set<Integer>> getDescendantProjectHierarchyMap(List<TProjectBean> baseProjectBeans) {
		List<TProjectBean> projectBeans = getDescendantProjects(baseProjectBeans, true);
		Map<Integer, Set<Integer>> childrenMap = new HashMap<Integer, Set<Integer>>();
		Map<Integer, Set<Integer>> descendantMap = new HashMap<Integer, Set<Integer>>();
		//gets the direct children sets
		for (TProjectBean projectBean : projectBeans) {
			Integer objectID = projectBean.getObjectID();
			Integer parentID = projectBean.getParent();
			if (parentID!=null) {
				Set<Integer> childrenSet = childrenMap.get(parentID);
				if (childrenSet==null) {
					childrenSet = new HashSet<Integer>();
					childrenMap.put(parentID, childrenSet);
				}
				childrenSet.add(objectID);
			}
		}
		//expand the direct children to all descendants at any level
		for (Map.Entry<Integer, Set<Integer>> entry : childrenMap.entrySet()) {
			Integer parentID = entry.getKey();
			Set<Integer> children = entry.getValue();
			Set<Integer> descendantSet = descendantMap.get(parentID);
			if (descendantSet==null) {
				descendantSet = new HashSet<Integer>();
				for (Integer childID : children) {
					descendantSet.add(childID);
					descendantSet.addAll(getAllDescendants(childID, descendantMap, childrenMap));
				}
				descendantMap.put(parentID, descendantSet);
			}
		}
		return descendantMap;
	}
	
	/**
	 * Get all descendants for a child
	 * @param childID
	 * @param descendantMap
	 * @param childrenMap
	 * @return
	 */
	private static Set<Integer> getAllDescendants(Integer childID, Map<Integer, Set<Integer>> descendantMap, Map<Integer, Set<Integer>> childrenMap) {
		Set<Integer> descendantSet = descendantMap.get(childID);
		if (descendantSet!=null) {
			return descendantSet;
		} else {
			descendantSet = new HashSet<Integer>();
			descendantMap.put(childID, descendantSet);
			Set<Integer> childrenSet = childrenMap.get(childID);
			if (childrenSet!=null) {
				for (Integer grandChildID : childrenSet) {
					descendantSet.addAll(getAllDescendants(grandChildID, descendantMap, childrenMap));
				}
			}
		}
		return descendantSet;
	}
	
	/**
	 *************************************************************************************
	 * First level projects: returning projectBean lists containing either main projects
	 * or the highest level subproject where the user hat the corresponding rights
	 *************************************************************************************
	 */

	/**
	 * Gets the first level not closed projects by rights
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param right
	 * @return
	 */
	public static List<TProjectBean> getActiveInactiveFirstLevelProjectsByRights(
			TPersonBean personBean, boolean allIfSystemAdmin, int[] right) {
		int[] projectStateFlags = new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE};
		return getFirstLevelProjectsByRightsAndProjectStateFlags(personBean, allIfSystemAdmin, right, projectStateFlags);
	}
	
	/**
	 * Gets the first level active projects by rights
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param right
	 * @return
	 */
	public static List<TProjectBean> getActiveFirstLevelProjectsByRights(
			TPersonBean personBean, boolean allIfSystemAdmin, int[] right) {
		int[] projectStateFlags = new int[] {TSystemStateBean.STATEFLAGS.ACTIVE};
		return getFirstLevelProjectsByRightsAndProjectStateFlags(personBean, allIfSystemAdmin, right, projectStateFlags);
	}
	
	/**
	 * Gets the first level not closed projects by rights
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param right
	 * @return
	 */
	private static List<TProjectBean> getFirstLevelProjectsByRightsAndProjectStateFlags(
			TPersonBean personBean, boolean allIfSystemAdmin, int[] right, int[] projectStateFlags) {
		if (personBean.isSys() && allIfSystemAdmin) {
			return loadMainProjectsByStateFlags(projectStateFlags, personBean.getObjectID());
		} else {
			return loadFirstLevelProjectsByRightsAndStateFlags(personBean.getObjectID(), right, projectStateFlags);
		}
	}	
	
	/**
	 * Gets the first level not closed projects by project administrator right
	 * @param personBean
	 * @param allIfSystemAdmin	
	 * @return
	 */
	public static List<TProjectBean> getActiveInactiveFirstLevelProjectsByProjectAdmin(
			TPersonBean personBean, boolean allIfSystemAdmin) {
		int[] projectStateFlags = new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE};
		if (personBean.isSys() && allIfSystemAdmin) {
			return loadMainProjectsByStateFlags(projectStateFlags, personBean.getObjectID());
		} else {
			return loadFirstLevelProjectsByRightsAndStateFlags(personBean.getObjectID(),
				new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN}, projectStateFlags);
		}
	}
	
	/**
	 * Load all first level projects by a project administrator (also closed projects)
	 * @param personID
	 * @return
	 */
	private static List<TProjectBean> loadAllFirstLevelProjectsByProjectAdmin(Integer personID) {
		return loadFirstLevelProjectsByRightsAndStateFlags(personID,
				new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN}, null);		
	}
	
	/**
	 * Gets the first level projects by rights and project state flags
	 * @param personID
	 * @param rights
	 * @param stateFlags
	 * @return
	 */
	private static List<TProjectBean> loadFirstLevelProjectsByRightsAndStateFlags(Integer personID, int[] rights, int[] stateFlags) {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(
				TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, stateFlags);
		List<Integer> personIDs = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		List<TProjectBean> explicitAdminProjects = projectDAO.loadByUserAndRightAndState(personIDs, rights, projectStates);
		return filterProjectsIfAnyAncestorPresent(explicitAdminProjects);
	}
	
	/** 
	 * Those subprojects which have any ancestor project in list (based on some explicit right)
	 * will be removed to not appear in two or more different branches (one explicit and one or more implicit (inherited) branches) 
	 * @param projectsWithExplicitRight
	 * @return
	 */
	private static List<TProjectBean> filterProjectsIfAnyAncestorPresent(List<TProjectBean> projectsWithExplicitRight) {
		if (projectsWithExplicitRight!=null) {
			//get all explicit project administrator projects
			//child to parent map with explicit project administrator projects
			Set<Integer> explicitProjectIDSet = GeneralUtils.createIntegerSetFromBeanList(projectsWithExplicitRight);
			//implicit project administrator projects: child to parent map with the 
			//entire project hierarchies starting from project administrator projects
			Map<Integer, Integer> childToParentMap = getChildToParentMap(projectsWithExplicitRight, null);
			//climb up in the ancestor hierarchy 
			for (Iterator<TProjectBean> iterator = projectsWithExplicitRight.iterator(); iterator.hasNext();) {
				TProjectBean projectBean = iterator.next();
				Integer objectID = projectBean.getObjectID();
				Integer parentID = projectBean.getParent();
				if (parentID!=null) {
					parentID = objectID;
					while (parentID!=null) {
						parentID = childToParentMap.get(parentID);
						if (parentID!=null&&explicitProjectIDSet.contains(parentID)){
							//an ancestor project of this project has explicit rights, remove this descendant  
							iterator.remove();
							break;
						}
					}
				}
			}
		}
		return projectsWithExplicitRight;
	}
	
	/** 
	 * Those subprojects which have their parent project in list
	 * will be removed to not appear in two different branches (one explicit and one or more implicit (inherited) branches) 
	 * @param projectsWithExplicitRight
	 * @return
	 */
	private static List<TProjectBean> filterProjectsIfNotAllAncestorPresent(List<TProjectBean> projectsWithExplicitRight) {
		Set<Integer> explicitProjectIDSet = GeneralUtils.createIntegerSetFromBeanList(projectsWithExplicitRight);
		for (Iterator<TProjectBean> iterator = projectsWithExplicitRight.iterator(); iterator.hasNext();) {
			TProjectBean projectBean = iterator.next();
			Integer parentID = projectBean.getParent();
			if (parentID!=null&&explicitProjectIDSet.contains(parentID)) {
				iterator.remove();
			}
		}
		return projectsWithExplicitRight;
	}
	
	/**
	 * Loads the not closed projects where the person has create right
	 * @param personID
	 * @return
	 */
	public static List<TreeNode> loadProjectsWithCreateIssueRightForItemType(Integer personID, Integer itemTypeID) {
		int[] rights = new int[]{AccessBeans.AccessFlagIndexes.CREATETASK,
				AccessBeans.AccessFlagIndexes.PROJECTADMIN};
		List<TProjectBean> projectBeans = loadProjectsFlatByRight(personID, rights, true);
		List<TProjectBean> allProjectsWithCreateRight = getDescendantProjects(projectBeans, true);
		Set<Integer> involvedProjectTypes = new HashSet<Integer>();
		for (TProjectBean projectBean : allProjectsWithCreateRight) {
			involvedProjectTypes.add(projectBean.getProjectType());
		}
		involvedProjectTypes = ProjectTypesBL.removeNotAllowingItemType(involvedProjectTypes, itemTypeID);
		for (Iterator<TProjectBean> iterator = allProjectsWithCreateRight.iterator(); iterator.hasNext();) {
			TProjectBean projectBean = iterator.next();
			Integer projectTypeID = projectBean.getProjectType();
			if (!involvedProjectTypes.contains(projectTypeID)) {
				iterator.remove();
			}
		}
		return getProjectTreeForReleasesWithCompletedPath(GeneralUtils.createIntegerListFromBeanList(allProjectsWithCreateRight),
				false, null, true, false, false, new HashMap<Integer, TreeNode>());
	 }

	/**
	 * Get the project hierarchy list based on the selected projects: if the projects have descendant projects 
	 * (for example parent and grandChild but without the child)
	 * then the entire hierarchy will be shown (the child will be added) even if the path between the
	 * selected projects contain holes (not selected projects in the hierarchy path)
	 * the project nodes are with modified IDs because they are used on the same tree with the releases,
	 * and the duplication of IDs should be avoided 
	 * @param projectIDs: the list of base projects might contain descendant projects
	 * @param useChecked
	 * @param baseProjectIDs the projects to preselect
	 * @param selectable
	 * @param negateNodeID
	 * @param modifiedNodeID
	 * @param projectTreeNodeMap out parameter
	 * @return
	 */
	public static List<TreeNode> getProjectTreeForReleasesWithCompletedPath(List<Integer> projectIDs,
			boolean useChecked, Set<Integer> baseProjectIDs, boolean selectable,
			boolean negateNodeID, boolean modifiedNodeID, Map<Integer, TreeNode> projectTreeNodeMap) {
		List<TreeNode> projectTreeNodesInFirstLevel = new LinkedList<TreeNode>();
		if (projectIDs!=null && !projectIDs.isEmpty()) {
			Set<Integer> baseProjectIDsSet = new HashSet<Integer>();
			Set<Integer> childProjectIDs = new HashSet<Integer>();
			List<TProjectBean> baseProjectBeansList = ProjectBL.loadByProjectIDs(projectIDs);
			Map<Integer, TSystemStateBean> projectStatusMap = GeneralUtils.createMapFromList(
				SystemStatusBL.getSystemStatesByByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
					new int[] {TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.ACTIVE}));
			Set<Integer> projectIDsInFirstLevelSet = new HashSet<Integer>();
			for (TProjectBean projectBean : baseProjectBeansList) {
				Integer projectID = projectBean.getObjectID();
				baseProjectIDsSet.add(projectID);
				Integer parentID = projectBean.getParent();
				TreeNode projectTreeNode = createProjectTreeNode(
						getNodeID(projectID, negateNodeID, modifiedNodeID),
						projectBean.getLabel(), getProjectIcon(getProjectStateFlag(projectStatusMap, projectBean.getStatus())),
						getChecked(useChecked, projectID, baseProjectIDs), selectable);
				projectTreeNodeMap.put(projectID, projectTreeNode);
				if (parentID!=null) {
					/**
					 * gather the child projects for further processing
					 */
					childProjectIDs.add(projectID);
				} else {
					/**
					 * the topmost parent projects are on the first level nodes anyway
					 */
					projectTreeNodesInFirstLevel.add(projectTreeNode);
					projectIDsInFirstLevelSet.add(projectID);
				}
			}
			Map<Integer, TProjectBean> involvedProjectsMap = new HashMap<Integer, TProjectBean>();
			//get the child hierarchy till the topmost projects: this might include ancestors which are not in the base project list
			Map<Integer, Integer> childToParentMap = getChildToParentMap(baseProjectBeansList, involvedProjectsMap);
			//add the highest ancestor projects (they are not topmost parent projects) from the base projects list as first level nodes
			//and remove all those ancestors of the base projects which are not in the base projects list
			for (Integer projectID : childProjectIDs) {
				Integer parentID = projectID;
				List<Integer> ancestorsToRemove = new LinkedList<Integer>();
				Integer highestSelectedAncestor = null;
				while (parentID!=null) {
					parentID = childToParentMap.get(parentID);
					if (parentID!=null) {
						ancestorsToRemove.add(parentID);
						//an ancestor project of this project is also selected  
						if (baseProjectIDsSet.contains(parentID)) {
							//the ancestor (and the other ancestors up to it) should remain: clear the ancestorsToRemove list to avoid removing them
							ancestorsToRemove.clear();
							highestSelectedAncestor = parentID;
						}
					}
				}
				if (highestSelectedAncestor!=null) {
					// add only if not added already this ancestor by a previous sibling branch
					if (!projectIDsInFirstLevelSet.contains(highestSelectedAncestor)) {
						/**
						 * add the highest selected ancestor (a child project, not a topmost one) as first level node
						 */
						projectIDsInFirstLevelSet.add(highestSelectedAncestor);
						TreeNode projectTreeNode = projectTreeNodeMap.get(highestSelectedAncestor);
						if (projectTreeNode!=null) {
							projectTreeNodesInFirstLevel.add(projectTreeNode);
						}
					}
				} else {
					/**
					 * none of the ancestors is member of the base projects list:
					 * add as first level project
					 */
					TreeNode projectTreeNode = projectTreeNodeMap.get(projectID);
					if (projectTreeNode!=null&&!projectIDsInFirstLevelSet.contains(projectID)) {
						projectIDsInFirstLevelSet.add(projectID);
						projectTreeNodesInFirstLevel.add(projectTreeNode);
					}
				}
				/**
				 * Cut those ancestor branches of a child project from the base projects list, which do not have any occurrence in the base projects list
				 */
				if (!ancestorsToRemove.isEmpty()) {
					//remove all ancestors when none of them was selected
					for (Integer ancestorID : ancestorsToRemove) {
						childToParentMap.remove(ancestorID);
					}
				}
			}
			/**
			 * the remaining project hierarchy should be added independently whether they are in the base project list or not 
			 * If not in base list then only to complete the path to avoid holes in the hierarchy.
			 * The remaining project in childToParentMap are either in base projects which have an ancestor in the 
			 * projectTreeNodesInFirstLevel or "hole projects" which should be added only to show the complete hierarchy
			 * but the "hole projects" are not selectable
			 */
			for (Integer projectID : childToParentMap.keySet()) {
				//the children whose ancestor path contains selected projects: add them in the hierarchy
				TreeNode childTreeNode = projectTreeNodeMap.get(projectID);
				if (childTreeNode==null) {
					TProjectBean projectBean = involvedProjectsMap.get(projectID);
					childTreeNode = createProjectTreeNode(
						getNodeID(projectID, negateNodeID, modifiedNodeID), projectBean.getLabel(),
						getProjectIcon(getProjectStateFlag(projectStatusMap, projectBean.getStatus())),
						getChecked(useChecked, projectID, baseProjectIDs), selectable && childProjectIDs.contains(projectID));
					projectTreeNodeMap.put(projectID, childTreeNode);
				}
				Integer parentID = childToParentMap.get(projectID);
				TreeNode parentTreeNode = projectTreeNodeMap.get(parentID);
				if (parentTreeNode==null) {
					TProjectBean projectBean = involvedProjectsMap.get(parentID);
					parentTreeNode = createProjectTreeNode(
						getNodeID(parentID, negateNodeID, modifiedNodeID), projectBean.getLabel(), 
						getProjectIcon(getProjectStateFlag(projectStatusMap, projectBean.getStatus())),
						getChecked(useChecked, projectID, baseProjectIDs), selectable);
					projectTreeNodeMap.put(parentID, parentTreeNode);
				}
				List<TreeNode> childrenList = parentTreeNode.getChildren();
				if (childrenList==null) {
					childrenList = new LinkedList<TreeNode>();
					parentTreeNode.setChildren(childrenList);
				}
				childrenList.add(childTreeNode);
			}
		}
		return projectTreeNodesInFirstLevel;
	}
	

	/**
	 * Gets the child to parent map for the child projects starting from projectBeans up to the main projects
	 * @param projectBeans
	 * @param involvedProjectsMap: out parameter
	 * @return
	 */
	public static Map<Integer, Integer> getChildToParentMap(List<TProjectBean> projectBeans, Map<Integer, TProjectBean> involvedProjectsMap) {
		//implicit project administrator projects: child to parent map with the 
		//entire project hierarchies starting from project administrator projects
		Map<Integer, Integer> childToParentMap = new HashMap<Integer, Integer>();
		//set with all ancestor projectIDs 
		Set<Integer> ancestorProjectIDs = new HashSet<Integer>();
		if (projectBeans!=null) {
			for (TProjectBean projectBean : projectBeans) {
				Integer objectID = projectBean.getObjectID();
				Integer parentID = projectBean.getParent();
				if (parentID!=null) {
					ancestorProjectIDs.add(parentID);
					childToParentMap.put(objectID, parentID);
				}
				if (involvedProjectsMap!=null) {
					involvedProjectsMap.put(objectID, projectBean);
				}
			}
		}
		Set<Integer> parentProjectIDs = ancestorProjectIDs;
		//up to the main projects level (projects without a parent)
		while (!parentProjectIDs.isEmpty()) {
			//get the next level of parents (hierarchically speaking the previous level) 
			List<TProjectBean> parentProjectBeans = loadByProjectIDs(GeneralUtils.createListFromSet(parentProjectIDs));
			parentProjectIDs = new HashSet<Integer>();
			for (TProjectBean projectBean : parentProjectBeans) {
				Integer objectID = projectBean.getObjectID();
				Integer parentID = projectBean.getParent();
				if (parentID!=null && !ancestorProjectIDs.contains(parentID)) {
					//only if ancestorProjectIDs not contain already (by processing a sibling branch starting from an upper level)
					parentProjectIDs.add(parentID);
					ancestorProjectIDs.add(parentID);
				}
				if (parentID!=null) {
					childToParentMap.put(objectID, parentID);
				}
				if (involvedProjectsMap!=null) {
					involvedProjectsMap.put(objectID, projectBean);
				}
			}
		}
		return childToParentMap;
	}
	
	
	/*******************************************************
	 *********************TreeNode methods******************
	 *******************************************************/

    public static List<Integer> getProjectIDsFromTree(List<TreeNode> treeNodes) {
        List<Integer> ids = new LinkedList<Integer>();
        if (treeNodes!=null) {
            for (TreeNode treeNode : treeNodes) {
                try {
                    Integer id = Integer.valueOf(treeNode.getId());
                    ids.add(id);
                } catch (Exception e) {

                }
                List<TreeNode> childNodes = treeNode.getChildren();
                if (childNodes!=null) {
                    ids.addAll(getProjectIDsFromTree(childNodes));
                }
            }
        }
        return ids;
    }

    /**
     * Gets the plain list with indented children from the simple node tree
     * @param treeNodes
     * @param indentLevel
     * @return
     */
    public static List<IntegerStringBean> getPlainListFromTree(List<SimpleTreeNode> treeNodes, int indentLevel) {
        List<IntegerStringBean> plainList = new ArrayList<IntegerStringBean>();
        if (treeNodes!=null) {
            for (SimpleTreeNode treeNode : treeNodes) {
            	String label = treeNode.getLabel();
            	if (indentLevel>0) {
    				StringBuilder stringBuilder = new StringBuilder();
    				for (int i = 0; i < indentLevel; i++) {
    					stringBuilder.append("&nbsp;&nbsp;");
    				}
    				label = stringBuilder.toString() + label;
            	}
                plainList.add(new IntegerStringBean(label, treeNode.getObjectID()));
                List<SimpleTreeNode> childNodes = treeNode.getChildren();
                if (childNodes!=null) {
                    plainList.addAll(getPlainListFromTree(childNodes, indentLevel+1));
                }
            }
        }
        return plainList;
    }
    
    /**
     * Gets the plain list with indented children from the simple node tree
     * @param treeNodes
     * @param projectToDecendentIDsMap
     * @return
     */
    public static void loadProjectToDecendentIDsMap(List<SimpleTreeNode> treeNodes, Map<Integer, Set<Integer>> projectToDecendentIDsMap) {
    	if (treeNodes!=null) {
    		for (SimpleTreeNode simpleTreeNode : treeNodes) {
    			loadProjectToDecendentIDsMap(simpleTreeNode, projectToDecendentIDsMap);
			}
    	}
    }
    
    /**
     * Gets the plain list with indented children from the simple node tree
     * @param treeNode
     * @param projectToDecendentIDsMap
     * @return
     */
    private static void loadProjectToDecendentIDsMap(SimpleTreeNode treeNode, Map<Integer, Set<Integer>> projectToDecendentIDsMap) {
    	Integer parentID = treeNode.getObjectID();
        List<SimpleTreeNode> childNodes = treeNode.getChildren();
        if (childNodes!=null) {
        	Set<Integer> parentDecendentsSet = new HashSet<Integer>();
        	for (SimpleTreeNode childNode : childNodes) {
        		Integer childID = childNode.getObjectID();
        		//add the direct children
        		parentDecendentsSet.add(childID);
        		//add the children's descendants
        		loadProjectToDecendentIDsMap(childNode, projectToDecendentIDsMap);
        		Set<Integer> childDecendentsSet = projectToDecendentIDsMap.get(childID);
        		if (childDecendentsSet!=null) {
        			parentDecendentsSet.addAll(childDecendentsSet);
        		}
			}
        	projectToDecendentIDsMap.put(parentID, parentDecendentsSet);
        }
    }
    
    
    
    
    /**
	 * Verify that the each value is part of the datasource
	 * @param actualValue
	 * @param treeNodes
	 * @return
	 */
	public static boolean idExists(Integer actualValue, List<TreeNode> treeNodes) {
		if (actualValue!=null) {
			for (TreeNode treeNode : treeNodes) {
				Integer id = null;
				try {
					id = Integer.valueOf(treeNode.getId());
				} catch(Exception ex) {
					LOGGER.debug("Converting " + treeNode.getId() + " to integer failed with " + ex.getMessage());
				}
				if (actualValue.equals(id)) {
					return true;
				}
				List<TreeNode> children = treeNode.getChildren();
				if (children!=null && !children.isEmpty()) {
					boolean exists = idExists(actualValue, children);
					if (exists) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * Gets all not closed projects with explicit rights eagerly (the entire hierarchy at once)
	 * @param activeOnly
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param right
	 * @return
	 */
	public static List<TreeNode> getProjectNodesByRightEager(boolean activeOnly,
			TPersonBean personBean, boolean allIfSystemAdmin, int[] right, boolean useChecked, boolean excludePrivateProject) {
		return getProjectNodesByRightEager(activeOnly, personBean, allIfSystemAdmin, excludePrivateProject,
				right, false, null, useChecked, true, false, false, new HashMap<Integer, TreeNode>());
	}
	
	/**
	 * Gets the not closed projects by rights and the RACI projects eagerly (the entire hierarchy at once)
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param right
	 * @param selectedProjectIDSet the projects to preselect
	 * @param useChecked whether the treeNode should have "checked" field
	 * @param selectable whether to reset the selectable flag of the treeNode. Makes sense only if useChecked is false
	 * @param negateNodeID whether to negate the nodeID (not for project picker/tree and yes for project/release picker/tree)
     * @param modifyNodeID
	 * @param withParameter
	 * @param locale
	 * @param treeNodeMap output parameter: map with all project nodes 
	 * @return
	 */
	public static List<TreeNode> getUsedProjectNodesEager(TPersonBean personBean,
			boolean allIfSystemAdmin, int[] right, Set<Integer> selectedProjectIDSet,
			boolean useChecked, boolean selectable, boolean negateNodeID, boolean modifyNodeID, boolean withParameter, Locale locale, Map<Integer, TreeNode> treeNodeMap) {
		List<TreeNode> usedProjectNodes = getProjectNodesByRightEager(false, personBean, allIfSystemAdmin, false, right,
				true, selectedProjectIDSet, useChecked, selectable, negateNodeID, modifyNodeID, treeNodeMap);
		if (withParameter) {
			TreeNode treeNode = new TreeNode(MatcherContext.PARAMETER.toString(),
					MatcherContext.getLocalizedParameter(locale), PROJECT_ICON_CLASS);
			treeNode.setChecked(getChecked(useChecked, MatcherContext.PARAMETER, selectedProjectIDSet));
			usedProjectNodes.add(treeNode);
		}
		return usedProjectNodes;
	}

	
	/**
	 * Gets all not closed projects eagerly (the entire hierarchy at once)
	 * @param activeOnly whether to include both active inactive or only active projects
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param right
	 * @param loadRaciProjects
	 * @param selectedProjectIDSet
	 * @param useChecked
	 * @param negateNodeID
	 * @param treeNodeMap out parameter (to build the release nodes upon)
	 * @return
	 */
	private static List<TreeNode> getProjectNodesByRightEager(boolean activeOnly, TPersonBean personBean,
			boolean allIfSystemAdmin, boolean excludePrivateProject, int[] right, boolean loadRaciProjects, Set<Integer> selectedProjectIDSet,
			boolean useChecked, boolean selectable, boolean negateNodeID, boolean modifyNodeID, Map<Integer, TreeNode> treeNodeMap) {
		List<TreeNode> treeNodes = new LinkedList<TreeNode>();
		//first level projects
		List<TProjectBean> projectBeans = null;
		if (activeOnly) {
			projectBeans = getActiveFirstLevelProjectsByRights(personBean, allIfSystemAdmin, right);
		} else {
			projectBeans = getActiveInactiveFirstLevelProjectsByRights(personBean, allIfSystemAdmin, right);
		}
		removeNotOwnedPrivateProjects(projectBeans, personBean.getObjectID());
		treeNodes = getProjectNodesHierarchy(projectBeans, activeOnly, excludePrivateProject, selectedProjectIDSet, useChecked, selectable, negateNodeID, modifyNodeID, treeNodeMap);
		if (loadRaciProjects) {
			//the RACI projects if present are without explicit rights (they or any of their ascendent)
			//so they have a parallel hierarchy to the projects with explicit rights
			List<Integer> meAndMySubstituted = AccessBeans.getMeAndSubstituted(personBean);
			List<Integer> meAndMySubstitutedAndGroups = AccessBeans.getMeAndSubstitutedAndGroups(personBean.getObjectID());
			int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
					new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE});
			Set<Integer> raciProjectsSet = getRACIProjectsSet(meAndMySubstituted, meAndMySubstitutedAndGroups, projectStates);
			for (Iterator<Integer> iterator = raciProjectsSet.iterator(); iterator.hasNext();) {
				Integer raciProject = iterator.next();
				if (treeNodeMap.containsKey(raciProject)) {
					iterator.remove();
				}
			}
			if (!raciProjectsSet.isEmpty()) {
				List<TProjectBean> raciProjectBeans = loadByProjectIDs(GeneralUtils.createListFromSet(raciProjectsSet));
				removeNotOwnedPrivateProjects(raciProjectBeans, personBean.getObjectID());
				if (!raciProjectBeans.isEmpty()) {
					List<TreeNode> raciTreeNodes = getProjectTreeForReleasesWithCompletedPath(GeneralUtils.createIntegerListFromBeanList(raciProjectBeans), useChecked, 
							selectedProjectIDSet, selectable, negateNodeID, modifyNodeID, treeNodeMap);
					if (!raciTreeNodes.isEmpty()) {
						treeNodes.addAll(raciTreeNodes);
						//sort the list containing the RACI Projects again
						Collections.sort(treeNodes);
					}
				}
			}
		}
		return treeNodes;
	}
	
	/**
	 * Remove the not owned private projects
	 * @param projectBeans
	 * @param personID
	 * @return
	 */
	private static void removeNotOwnedPrivateProjects(List<TProjectBean> projectBeans, Integer personID) {
		if (projectBeans!=null && !projectBeans.isEmpty()) {
			for (Iterator<TProjectBean> iterator = projectBeans.iterator(); iterator.hasNext();) {
				TProjectBean projectBean = iterator.next();
				if (projectBean.isPrivate() && EqualUtils.notEqual(projectBean.getDefaultManagerID(), personID)) {
					iterator.remove();
				} 
			}	
		}
	}
	/**
	 * Gets the exclusive project and their subprojects
	 * @param exclusiveProjectID
	 * @param selectedProjectIDsSet
	 * @param useChecked
	 * @return
	 */
	static List<TreeNode> getExclusiveProjectHierarchy(Integer exclusiveProjectID, Set<Integer> selectedProjectIDsSet, boolean useChecked) {
		if (exclusiveProjectID!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(exclusiveProjectID);
			if (projectBean!=null) {
				List<TProjectBean> projectBeans = new LinkedList<TProjectBean>();
				projectBeans.add(projectBean);
				return getProjectNodesHierarchy(projectBeans, false, false, selectedProjectIDsSet, useChecked, true, false, false, new HashMap<Integer, TreeNode>());
			}
		}
		return new LinkedList<TreeNode>();
	}
	/**
	 * Gets all not closed projects eagerly (the entire hierarchy at once)
     * @param firstLevelProjectBeans
	 * @param activeOnly whether to include both active inactive or only active projects
	 * @param excludePrivateProject
	 * @param selectedProjectIDSet
	 * @param useChecked
	 * @param selectable
     * @param negateNodeID
     * @param modifyNodeID
	 * @param selectedProjectIDSet
	 * @param treeNodeMap out parameter (to build the release nodes upon)
	 * @return
	 */
	private static List<TreeNode> getProjectNodesHierarchy(List<TProjectBean> firstLevelProjectBeans, boolean activeOnly,
			boolean excludePrivateProject, Set<Integer> selectedProjectIDSet,
			boolean useChecked, boolean selectable, boolean negateNodeID, boolean modifyNodeID, Map<Integer, TreeNode> treeNodeMap) {
		List<TreeNode> treeNodes = new LinkedList<TreeNode>();
		List<Integer> projectsWithRight = new LinkedList<Integer>();
		//first level projects
		Map<Integer, TSystemStateBean> projectStatusMap = null;
		if (!activeOnly) {
			projectStatusMap = GeneralUtils.createMapFromList(
				SystemStatusBL.getSystemStatesByByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
					new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.CLOSED}));
		}
		//now the subprojects
		List<Integer> parentIDs = new LinkedList<Integer>();
		if (firstLevelProjectBeans!=null) {
			for (TProjectBean projectBean : firstLevelProjectBeans) {
				Integer projectID = projectBean.getObjectID();
				if (excludePrivateProject) {
					Integer projectType = projectBean.getProjectType();
					if (projectType!=null && projectType.intValue()<0) {
						continue;
					}
				}
				TreeNode treeNode = createProjectTreeNode(
						getNodeID(projectID, negateNodeID, modifyNodeID),
						projectBean.getLabel(), getProjectIcon(getProjectStateFlag(projectStatusMap, projectBean.getStatus())),
						getChecked(useChecked, projectID, selectedProjectIDSet), selectable);
				//not known previously whether this project has subprojects 
				treeNode.setLeaf(Boolean.TRUE);
				parentIDs.add(projectID);
				projectsWithRight.add(projectID);
				treeNodeMap.put(projectID, treeNode);
				treeNodes.add(treeNode);
			}
		}
		while (!parentIDs.isEmpty()) {
			if (activeOnly) {
				firstLevelProjectBeans = loadActiveSubrojects(parentIDs);
			} else {
				firstLevelProjectBeans = loadActiveInactiveSubrojects(parentIDs);
			}
			parentIDs = new LinkedList<Integer>();
			if (firstLevelProjectBeans!=null) {
				for (TProjectBean projectBean : firstLevelProjectBeans) {
					Integer projectID = projectBean.getObjectID();
					projectsWithRight.add(projectID);
					//parentID surely not null
					Integer parentID = projectBean.getParent();
					TreeNode childNode = createProjectTreeNode(
							getNodeID(projectID, negateNodeID, modifyNodeID),
							projectBean.getLabel(), getProjectIcon(getProjectStateFlag(projectStatusMap, projectBean.getStatus())),
							getChecked(useChecked, projectID, selectedProjectIDSet), selectable);
					TreeNode parentTreeNode = treeNodeMap.get(parentID);
					if (parentTreeNode!=null) {
						List<TreeNode> childrenNodes = parentTreeNode.getChildren();
						if (childrenNodes==null) {
							//reset leaf to false once a child is found
							parentTreeNode.setLeaf(Boolean.FALSE);
							childrenNodes = new LinkedList<TreeNode>();
							parentTreeNode.setChildren(childrenNodes);
						}
						treeNodeMap.put(projectID, childNode);
						childrenNodes.add(childNode);
					}
					parentIDs.add(projectID);
				}
			}
		}
		return treeNodes;
	}
	
	/**
	 * Creates a treeNode from a projectBean
	 * @param nodeID
     * @param label
     * @param icon
     * @param checked
     * @param selectable
	 * @return
	 */
	public static TreeNode createProjectTreeNode(String nodeID, String label, String icon, Boolean checked, boolean selectable) {
		TreeNode treeNode = new TreeNode(nodeID, label, icon);
		if (checked!=null) {
			treeNode.setChecked(checked);
		}
		treeNode.setSelectable(selectable);
		return treeNode;
	}
	
	/**
	 * Gets the project icon
	 * @param projectBean
	 * @return
	 */
	public static String getProjectIcon(TProjectBean projectBean) {
		return getProjectIcon(
			SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, projectBean.getStatus()));
	}
	
	/**
	 * Gets the state flag for release
	 * @param projectStatusMap
	 * @param projectStatus
	 * @return
	 */
	public static Integer getProjectStateFlag(Map<Integer, TSystemStateBean> projectStatusMap, Integer projectStatus) {
		Integer stateFlag = null;
		if (projectStatusMap!=null && projectStatus!=null) {
			TSystemStateBean systemStateBean = projectStatusMap.get(projectStatus);
			if (systemStateBean!=null) {
				stateFlag = systemStateBean.getStateflag();
			}
		}
		return stateFlag;
	}
	
	/**
	 * Gets the project icon
	 * @param stateFlag
	 * @return
	 */
	public static String getProjectIcon(Integer stateFlag) {
		String iconCls = null;
		if (stateFlag!=null) {
			switch (stateFlag) {
			case TSystemStateBean.STATEFLAGS.ACTIVE:
				iconCls = PROJECT_ICON_CLASS;
				break;
			case TSystemStateBean.STATEFLAGS.INACTIVE:
				iconCls = PROJECT_INACTIVE_ICON_CLASS;
				break;
			case TSystemStateBean.STATEFLAGS.CLOSED:
				iconCls = PROJECT_CLOSED_ICON_CLASS;
				break;
			}
		} else {
			iconCls = PROJECT_ICON_CLASS;
		}
		return iconCls;
	}
	
	
	/**
	 * Get the checked flag if useChecked is true
	 * @param useChecked
	 * @param projectID
	 * @param selectedProjects
	 * @return
	 */
	static Boolean getChecked(boolean useChecked, Integer projectID, Set<Integer> selectedProjects) {
		Boolean checked = null;
		if (useChecked) { 
			if (selectedProjects!=null) {
				checked = Boolean.valueOf(selectedProjects.contains(projectID));
			} else {
				checked = Boolean.FALSE;
			}
		}
		return checked;
	}

	/**
	 * Get the nodeID for tree node
	 * @param projectID
	 * @param negateNodeID negate for projectRelease picker/tree do not negate for project picker/tree
	 * @return
	*/
	private static String getNodeID(Integer projectID, boolean negateNodeID, boolean modify) {
		String nodeID = null;
		if (negateNodeID) {
			nodeID = Integer.valueOf(-projectID.intValue()).toString();
		} else {
			if (modify) {
				nodeID = "project" + projectID.toString();
			} else {
				nodeID = projectID.toString();
			}
		}
		return nodeID;
	}
	
	/**
	 * Get the not closed projects lazily
	 * @param personBean
	 * @param projectID
	 * @param allIfSystemAdmin if true: only the projects the user is projectadmin for
     * @param right
	 * @return
	 */
	public static List<TProjectBean> getNotClosedProjectsLazy(TPersonBean personBean,
			Integer projectID, boolean allIfSystemAdmin, int[] right){
		List<TProjectBean> projectBeans;
		if (projectID==null) {
			//first level projects
			projectBeans = getActiveInactiveFirstLevelProjectsByRights(personBean, allIfSystemAdmin, right);
		} else {
			projectBeans = loadActiveInactiveSubrojects(projectID);
		}
		return projectBeans;
	}
	
	/**
	 * Get nodes with all projects (also closed) lazily (by managing the projects)
	 * @param projectID
	 * @param personBean
	 * @return
	 */
	static List<TreeNodeBaseTO> getAdminProjectNodesLazy(Integer projectID, TPersonBean personBean, boolean isTemplate) {
		List<TreeNodeBaseTO> children = new LinkedList<TreeNodeBaseTO>();
		List<TProjectBean> projectBeans=getAdminProjectsLazy(personBean, projectID, true);
		Map<Integer, TSystemStateBean> projectStatusMap = GeneralUtils.createMapFromList(
				LookupContainer.getSystemStateList(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE)/*SystemStatusBL.getStatusOptions(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE)*/);		
		for (TProjectBean projectBean : projectBeans) {			
			boolean leaf = false; 
			Integer projectType = projectBean.getProjectType();
			if (projectType!=null && projectType.intValue()<0) {
				//make the private projects leaf
				leaf = true;
			}
			
			if(isTemplate) {
				if(BooleanFields.fromStringToBoolean(projectBean.getIsTemplate())) {
					children.add(new TreeNodeBaseTO(String.valueOf(projectBean.getObjectID()),
							projectBean.getLabel(), getProjectIcon(getProjectStateFlag(projectStatusMap, projectBean.getStatus())), leaf));
				}
			}else {
				if(!BooleanFields.fromStringToBoolean(projectBean.getIsTemplate())) {
					children.add(new TreeNodeBaseTO(String.valueOf(projectBean.getObjectID()),
							projectBean.getLabel(), getProjectIcon(getProjectStateFlag(projectStatusMap, projectBean.getStatus())), leaf));
				}
			}
	
		}
		return children;
	}
	
	/**
	 * Get all (also closed) project beans lazily (by managing the projects)
	 * @param personBean
	 * @param parentProjectID
	 * @param allIfSystemAdmin if true: only the projects the user is projectadmin for
	 * @return
	 */
	public static List<TProjectBean> getAdminProjectsLazy(TPersonBean personBean,
			Integer parentProjectID, boolean allIfSystemAdmin){
		List<TProjectBean> projectBeans = null;		
		if (parentProjectID==null) {
			//first level projects
			if (personBean.isSys() && allIfSystemAdmin) {
				//all main projects
				projectBeans = loadAllMainProjects(personBean.getObjectID());
			} else {
				//all main by project admin
				projectBeans = loadAllFirstLevelProjectsByProjectAdmin(personBean.getObjectID());
			}
		} else {
			projectBeans = loadSubrojects(parentProjectID);
		}
		return projectBeans;
	}
	
	/**
	 * Load all first level projects by system administrator (also closed projects but not the other user's private projects)
	 * @param personID
	 * @return
	 */
	private static List<TProjectBean> loadAllMainProjects(Integer personID) {
		return removeNotOwnPrivateProjects(projectDAO.loadAllMainProjects(), personID);
	}
	
	/**
	 * Loads the main (no child) projects with specific states 
	 * @param stateFlags
	 * @return
	 */
	private static List<TProjectBean> loadMainProjectsByStateFlags(int[] stateFlags, Integer personID) {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, stateFlags);
		return removeNotOwnPrivateProjects(projectDAO.loadMainProjectsByStates(projectStates), personID);
	}
	
	/**
	 * Removes the other user's main projects
	 * @param mainProjects
	 * @param personID
	 * @return
	 */
	private static List<TProjectBean> removeNotOwnPrivateProjects(List<TProjectBean> mainProjects, Integer personID) {
		if (mainProjects!=null) {
			for (Iterator<TProjectBean> iterator = mainProjects.iterator(); iterator.hasNext();) {
				TProjectBean projectBean = iterator.next();
				Integer projectType = projectBean.getProjectType();
				if (projectType!=null && projectType<0) {
					//private project
					Integer defaultManager = projectBean.getDefaultManagerID();
					if (defaultManager!=null && !defaultManager.equals(personID)) {
						iterator.remove();
					}
				}
			}
		}
		return mainProjects;
	}
	
	/**
	 * Load all subprojects of a project
	 * @param projectID
	 * @return
	 */
	public static List<TProjectBean> loadSubrojects(Integer projectID) {
		return projectDAO.loadSubrojects(projectID);
	}
	
	/**
	 * Load not closed subprojects for projects
	 * @param projectIDs
	 * @return
	 */
	public static List<TProjectBean> loadActiveInactiveSubrojects(List<Integer> projectIDs) {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, 
				TSystemStateBean.STATEFLAGS.INACTIVE});
		return projectDAO.loadSubprojectsByParentsAndStates(projectIDs, projectStates);	
	}
	
	/**
	 * Load not closed subprojects for projects
	 * @param projectIDs
	 * @return
	 */
	public static List<TProjectBean> loadActiveSubrojects(List<Integer> projectIDs) {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE});
		return projectDAO.loadSubprojectsByParentsAndStates(projectIDs, projectStates);	
	}
	
	/**
	 * Load the active subprojects of a project
	 * @param projectID
	 * @return
	 */
	public static List<TProjectBean> loadActiveSubrojects(Integer projectID) {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE});
		return loadSubprojectsByParentAndStates(projectID, projectStates);
	}
	
	/**
	 * Load the not closed subprojects of a project
	 * @param projectID
	 * @return
	 */
	public static List<TProjectBean> loadActiveInactiveSubrojects(Integer projectID) {
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, 
				TSystemStateBean.STATEFLAGS.INACTIVE});
		return loadSubprojectsByParentAndStates(projectID, projectStates);
	}
	
	/**
	 * Loads the subprojects with specific states 
	 * @param projectID
	 * @param states
	 * @return
	 */
	public static List<TProjectBean> loadSubprojectsByParentAndStates(Integer projectID, int[] states) {
		return projectDAO.loadSubprojectsByParentAndStates(projectID, states);
	}
	
	/**
	 * Gets the not closed subprojects with project administrator rights 
	 * @param personID
	 * @param parentProjectID
	 * @return
	 */
	public static List<TProjectBean> loadNotClosedAdminSubprojects( 
			Integer personID, Integer parentProjectID) {
		int[] right = new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN};
		int[] projectStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE});
		List<Integer> personIDs = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		return  projectDAO.loadSubprojectsByUserAndRightAndState(personIDs, parentProjectID, right, projectStates);
	}
	
	/**
	 * Gets the ProjectAccountingTO for a list of projects
	 * @param projectIDs
	 * @return
	 */
	public static Map<Integer, ProjectAccountingTO> getAccountingAttributesMap(Collection<Integer> projectIDs) {
		Map<Integer, ProjectAccountingTO> projectAccountingTOMap = new HashMap<Integer, ProjectAccountingTO>();
		if (projectIDs!=null) {
			for (Integer projectID : projectIDs) {
				ProjectAccountingTO projectAccountingTO = getProjectAccountingTO(projectID);
				projectAccountingTOMap.put(projectID, projectAccountingTO);
			}
		}
		return projectAccountingTOMap;
	}
	
	/**
	 * Gets the hours per workday for a proejct
	 * @param projectID
	 * @return
	 */
	public static Double getHoursPerWorkingDay(Integer projectID) {
		ProjectAccountingTO projectAccountingTO = getProjectAccountingTO(projectID);
		return projectAccountingTO.getHoursPerWorkday();
	}
	
	/**
	 * Gets the hours per workday for a proejct
	 * @param projectID
	 * @return
	 */
	public static Integer getDefaultWorkUnit(Integer projectID) {
		ProjectAccountingTO projectAccountingTO = getProjectAccountingTO(projectID);
		return projectAccountingTO.getDefaultWorkUnit();
	}
	
	/**
	 * Gets the hours per working days for a list of projects
	 * @param projectIDs
	 * @return
	 */
	public static Map<Integer, Double> getHoursPerWorkingDayMap(Collection<Integer> projectIDs) {
		Map<Integer, ProjectAccountingTO> projectToAccountingMap = getAccountingAttributesMap(projectIDs);
		Map<Integer, Double> projectHoursPerWorkingDayMap = new HashMap<Integer, Double>();
		if (projectToAccountingMap!=null) {
			for (Integer projectID : projectToAccountingMap.keySet()) {
				ProjectAccountingTO projectAccountingTO = projectToAccountingMap.get(projectID);
				projectHoursPerWorkingDayMap.put(projectID, projectAccountingTO.getHoursPerWorkday());
			}
		}
		return projectHoursPerWorkingDayMap;
	}
	
	
	/**
	 * Gets the hours per workday for a proejct
	 * @param projectID
	 * @return
	 */
	public static String getCurrency(Integer projectID) {
		ProjectAccountingTO projectAccountingTO = getProjectAccountingTO(projectID);
		return projectAccountingTO.getCurrency();
	}
	
	/**
	 * Gets the accounting properties for project 
	 * @param projectID
	 * @return
	 */
	public static ProjectAccountingTO getProjectAccountingTO(Integer projectID) {
		return getProjectAccountingTO(projectID, false, false, false, null, false);
	}
	
	/**
	 * Gets the accounting properties for project 
	 * @param projectID
	 * @param add
	 * @param addAsSubproject
	 * @param projectTypeChange
     * @param  projectTypeBean
     * @param submittedAccountingInherited
	 * @return
	 */
	public static ProjectAccountingTO getProjectAccountingTO(Integer projectID,
			boolean add, boolean addAsSubproject, boolean projectTypeChange, TProjectTypeBean projectTypeBean, boolean submittedAccountingInherited) {
		ProjectAccountingTO projectAccountingTO = new ProjectAccountingTO();
		boolean accountingInherited = false;
		TProjectBean projectBean = null;
		if (projectTypeChange) {
			accountingInherited = submittedAccountingInherited;
		} else {
			if (!add && projectID!=null) {
				projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					accountingInherited = projectBean.getParent()!=null &&
						PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(),
								TProjectBean.MOREPPROPS.ACCOUNTING_INHERITED, true);
				}
			} else {
				accountingInherited = addAsSubproject;
			}
		}
		if ((add && !addAsSubproject) || (projectTypeChange && !accountingInherited)) {
			if (projectTypeBean!=null && projectTypeBean.getUseAccounting()) {
				projectAccountingTO.setWorkTracking(true);
				projectAccountingTO.setCostTracking(true);
				projectAccountingTO.setHoursPerWorkday(projectTypeBean.getHoursPerWorkDay());
				projectAccountingTO.setDefaultWorkUnit(projectTypeBean.getDefaultWorkUnit());
				projectAccountingTO.setCurrencyName(projectTypeBean.getCurrencyName());
				projectAccountingTO.setCurrencySymbol(projectTypeBean.getCurrencySymbol());
			}
		} else {
			while (projectID!=null) {
				//projectBean = loadByPrimaryKey(projectID);
				projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					Properties properties = projectBean.getMoreProperties();
					//default, if not set it means inherited 
					boolean accountingInheritedFromParent = projectBean.getParent()!=null &&
							PropertiesHelper.getBooleanProperty(properties, TProjectBean.MOREPPROPS.ACCOUNTING_INHERITED, true);
					projectID = projectBean.getParent();
					/*if (projectID!=null) {
						projectAccountingTO.setAncestorProjectID(projectID);
					} else {
						projectAccountingTO.setAncestorProjectID(projectBean.getObjectID());
					}*/
					if (projectID==null || !accountingInheritedFromParent) {
						//main project or not inherited
						boolean workTracking = false;
						String workTrackingStr = PropertiesHelper.getProperty(properties, TProjectBean.MOREPPROPS.WORK_TRACKING);
						if (workTrackingStr!=null) {
							//workTracking exist (saved in or after track+ 4.0)
							workTracking = PropertiesHelper.getBooleanProperty(properties, TProjectBean.MOREPPROPS.WORK_TRACKING);
						} else {
							//fall back to track+ previous to 4.0 (work and cost together in accounting) 
							workTracking = PropertiesHelper.getBooleanProperty(properties, TProjectBean.MOREPPROPS.ACCOUNTING_PROPERTY);
						}
						projectAccountingTO.setWorkTracking(workTracking);
						Double hoursPerWorkDay = projectBean.getHoursPerWorkDay();
						if (hoursPerWorkDay==null) {
							projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectBean.getProjectType());
							if (projectTypeBean!=null && projectTypeBean.getHoursPerWorkDay()!=null && projectTypeBean.getHoursPerWorkDay().doubleValue() > 0.0) {
								hoursPerWorkDay = projectTypeBean.getHoursPerWorkDay();
							}
						}
						if (hoursPerWorkDay==null) {
							hoursPerWorkDay = new Double(AccountingBL.DEFAULTHOURSPERWORKINGDAY);
						}
						projectAccountingTO.setHoursPerWorkday(hoursPerWorkDay);
						Integer defaultWorkUnit = PropertiesHelper.getIntegerProperty(properties, TProjectBean.MOREPPROPS.WORK_DEFAULT_UNIT);
						if (defaultWorkUnit==null) {
							defaultWorkUnit = AccountingBL.TIMEUNITS.HOURS;
						}
						projectAccountingTO.setDefaultWorkUnit(defaultWorkUnit);
						String costTrackingStr = PropertiesHelper.getProperty(properties, TProjectBean.MOREPPROPS.COST_TRACKING);
						boolean costTracking = false;
						if (costTrackingStr!=null) {
							//costTracking exist (saved in or after track+ 4.0)
							costTracking = PropertiesHelper.getBooleanProperty(properties, TProjectBean.MOREPPROPS.COST_TRACKING);
						} else {
							//fall back to track+ previous to 4.0 (work and cost together in accounting)
							costTracking = PropertiesHelper.getBooleanProperty(properties, TProjectBean.MOREPPROPS.ACCOUNTING_PROPERTY);
						}
						projectAccountingTO.setCostTracking(costTracking);
						projectAccountingTO.setCurrencyName(projectBean.getCurrencyName());
						projectAccountingTO.setCurrencySymbol(projectBean.getCurrencySymbol());
						//default account
						Integer defaultAccount = PropertiesHelper.getIntegerProperty(properties, TProjectBean.MOREPPROPS.DEFAULT_ACCOUNT);
						projectAccountingTO.setDefaultAccount(defaultAccount);
						break;
					}
				} else {
					break;
				}
			}
		}
		projectAccountingTO.setAccountingInherited(accountingInherited);
		return projectAccountingTO;
	}
	
	/**
	 * Sets the list entries and list values for system lists
	 * @param projectID
	 * @param projectAccountingTO
     * @param add
     * @param addAsSubproject
	 * @param locale
	 * @return
	 */
	public static Map<Integer, String> setAccountList(Integer projectID,
			ProjectAccountingTO projectAccountingTO, boolean add, boolean addAsSubproject, Locale locale) {
		Map<Integer, String> invalidFieldDefaults = new HashMap<Integer, String>();
		Integer accountID = projectAccountingTO.getDefaultAccount();
		List<TAccountBean> assignedAccountBeans = null;
		if (add && !addAsSubproject) {
			//no account assignment exist to the project (project is new): offer all possible accounts 
			assignedAccountBeans = AccountBL.loadAllActive();
		} else {
			//add as subproject or edit existing project:  
			assignedAccountBeans = AccountBL.loadActiveByProject(projectID);
			if (assignedAccountBeans==null || assignedAccountBeans.isEmpty()) {
				//get from project or from the nearest ancestor project
				//assignedAccountBeans = AccountBL.loadActiveByProject(projectAccountingTO.getAncestorProjectID());
				assignedAccountBeans = AccountBL.loadAllActive();
			}	
		}
		projectAccountingTO.setAccountList(assignedAccountBeans);
		if (!projectAccountingTO.isAccountingInherited()&&assignedAccountBeans!=null && !assignedAccountBeans.isEmpty()) {
			if (accountID==null) {
				//either new or existing but the default account was not yet set
				accountID = assignedAccountBeans.get(0).getObjectID();
			} else {
				//existing projectBean, account was set previously
				boolean accountValid = ProjectConfigBL.foundInList((List)assignedAccountBeans, accountID);
				if (!accountValid) {
						//by project loading leave the original value but mark as erroneous
						TAccountBean accountBean = AccountBL.loadByPrimaryKey(accountID);
						if (accountBean!=null) {
							assignedAccountBeans.add(0, accountBean);
						}
						invalidFieldDefaults.put(Integer.valueOf(INVALID_DEFAULT_ACCOUNT),
							LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.err.invalidAccount", locale));
				}
			}
			projectAccountingTO.setDefaultAccount(accountID);
		}
		return invalidFieldDefaults;
	}

	/**
	 * Whether the project is active  
	 * @param projectID
	 * @return
	 */
	public static boolean projectIsActive(Integer projectID){
		return SystemStatusBL.getStatusFlag(projectID, TSystemStateBean.ENTITYFLAGS.PROJECTSTATE) == TSystemStateBean.STATEFLAGS.ACTIVE;
	}
	
}
