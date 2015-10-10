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

package com.aurel.track.admin.project.release;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ReleaseDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

public class ReleaseBL {
	private static ReleaseDAO releaseDAO = DAOFactory.getFactory().getReleaseDAO();

	public static String RELEASE_ICON_CLASS = "release-ticon";
	private static String RELEASE_INACTIVE_CLASS = "release-Inactive-ticon";
	private static String RELEASE_UNSCHEDULED_CLASS = "release-Unscheduled-ticon";
	private static String RELEASE_CLOSED_CLASS = "release-Closed-ticon";
	
	/**
	 * Loads a release by primary key
	 * @param objectID
	 * @return
	 */
	public static TReleaseBean loadByPrimaryKey(Integer objectID) {
		return releaseDAO.loadByPrimaryKey(objectID);
	}

	/**
	 * Loads all releases
	 * @return
	 */
	public static List<TReleaseBean> loadAll() {
		return releaseDAO.loadAll();
	}
	
	/**
	 * Loads a ReleaseBean list by releaseIDs
	 * @param releaseIDs
	 * @return
	 */
	public static List<TReleaseBean> loadByReleaseIDs(List<Integer> releaseIDs) {
		return releaseDAO.loadByReleaseIDs(releaseIDs);
	}
	
	/**
	 * Saves a releaseBean in the TRelease table
	 * @param releaseBean
	 * @return
	 */
	public static Integer save(TReleaseBean releaseBean) {
		boolean isNew = (releaseBean.getObjectID() == null);
		Integer releaseID = saveSimple(releaseBean);
		if (isNew) {
			releaseBean.setObjectID(releaseID);
			NotLocalizedListIndexer.getInstance().addLabelBean(
					releaseBean, LuceneUtil.LOOKUPENTITYTYPES.RELEASE, isNew);
		} else {
			NotLocalizedListIndexer.getInstance().updateLabelBean(
					releaseBean, LuceneUtil.LOOKUPENTITYTYPES.RELEASE);
		}
		//cache and possible lucene update in other cluster nodes
		//if (!ClusterBL.indexInstantly()) {
			//otherwise it is already marked in saveSimple
			ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_RELEASE, releaseID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdate(isNew));
		//}
		return releaseID;
	}

	/**
	 * Saves a releaseBean
	 * @param releaseBean
	 * @return
	 */
	public static Integer saveSimple(TReleaseBean releaseBean) {
		Integer releaseID = releaseDAO.save(releaseBean);
		LookupContainer.reloadRelease(releaseID);
		//cache update in other cluster nodes (sortOrder, icon) but no lucene update is implied   
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_RELEASE, releaseID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
		return releaseID;
	}
		
	/** 
	 * Loads all releaseBeans for a project with specific states 
	 * Gets all (main and child) independently of hierarchy
	 * @param projectID
	 * @param stateFlags
	 * @return
	 */
	public static List<TReleaseBean> loadAllEagerByProjectAndStates(Integer projectID, int[] stateFlags) {
		int[] releaseStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, stateFlags);
		List<TReleaseBean> baseReleaseBeanList = releaseDAO.loadAllByProjectAndStates(projectID, releaseStates);
		List<Integer> releaseIDList = GeneralUtils.createIntegerListFromBeanList(baseReleaseBeanList);
		Integer[] releaseIDs = getDescendantReleaseIDs(GeneralUtils.createIntegerArrFromCollection(releaseIDList), stateFlags);
		return loadByReleaseIDs(GeneralUtils.createListFromIntArr(releaseIDs));
	}

	
	/***********************************************************
	 * Plain release methods (main and child without hierarchy)
	 ***********************************************************/
	
	/**
	 * Gets a releaseBean by project by label
	 * @param projectID
	 * @param label
	 * @return
	 */
	public static TReleaseBean loadByProjectAndLabel(Integer projectID, String label) {
		return releaseDAO.loadByProjectAndLabel(projectID, label);
	}
	
	/** 
	 * Loads all releaseBeans for a project with specific states 
	 * Gets all (main and child) independently of hierarchy
	 * @param projectIDs
	 * @return
	 */
	public static List<TReleaseBean> loadAllByProjects(List<Integer> projectIDs) {
		return releaseDAO.loadByProjectKeys(projectIDs);
	}
	
	/**
	 * Load all active and inactive releases for a list of projects
	 * @return
	 */
	public static List<TReleaseBean> loadNotClosedByProjectIDs(List<Integer> projectKeys) {
		return releaseDAO.loadNotClosedByProjectIDs(projectKeys);
	}
	
	/** 
	 * Loads all releaseBeans for a project with specific states 
	 * Gets all (main and child) independently of hierarchy
	 * @param projectID
	 * @param stateFlags
	 * @return
	 */
	public static List<TReleaseBean> loadAllByProjectAndStates(Integer projectID, int[] stateFlags) {
		int[] releaseStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, stateFlags);
		return releaseDAO.loadAllByProjectAndStates(projectID, releaseStates);
	}
	
	/**
	 * Loads the active releases of a project
	 * @param projectID
	 * @return
	 */
	public static List<TReleaseBean> loadActiveByProject(Integer projectID) {
		return loadAllByProjectAndStates(projectID,  new int[] {TSystemStateBean.STATEFLAGS.ACTIVE});
	}
	
	/**
	 * Loads the active and inactive releases of a project
	 * @param projectID
	 * @return
	 */
	public static List<TReleaseBean> loadActiveInactiveByProject(Integer projectID) {
		return loadAllByProjectAndStates(projectID,  new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, 
				TSystemStateBean.STATEFLAGS.INACTIVE});
	}
	
	/**
	 * Loads the active and not planned releases of a project
	 * @param projectID
	 * @return
	 */
	public static List<TReleaseBean> loadActiveOrNotPlannedByProject(Integer projectID) {
		return loadAllByProjectAndStates(projectID,  new int[] {TSystemStateBean.STATEFLAGS.ACTIVE,
				TSystemStateBean.STATEFLAGS.NOT_PLANNED});
	}
	
	/**
	 * Loads the not closed releases of a project
	 * @param projectID
	 * @return
	 */
	public static List<TReleaseBean> loadNotClosedByProject(Integer projectID) {
		return loadAllByProjectAndStates(projectID,  new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, 
				TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.NOT_PLANNED});	
	}
	
	/***********************************************************
	 * Main release methods: get the first level releases
	 ***********************************************************/
	
	/**
	 * Loads the main (not child) not closed releases by project
	 * @param projectID
	 * @return
	 */
	public static List<TReleaseBean> loadMainNotClosedByProject(Integer projectID) {
		int[] releaseStateFlags = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.NOT_PLANNED});
		return releaseDAO.loadMainByProjectAndStates(projectID, releaseStateFlags);
	}

	/**
	 * Loads the main (not child) releases by projects and stateFlags
	 * @param projectIDs
	 * @param stateFlags
	 * @return
	 */
	public static List<TReleaseBean> loadMainByProjectsAndStates(List<Integer> projectIDs, int[] stateFlags, Set<Integer> selectedReleaseIDsSet) {
		int[] releaseStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, stateFlags);
		return releaseDAO.loadMainByProjectsAndStates(projectIDs, releaseStates, selectedReleaseIDsSet);
	}

	/***********************************************************
	 * Child release methods: get the first level releases
	 ***********************************************************/
	
	/**
	 * Loads the  not closed child releases for a release
	 * @param releaseID
	 * @return
	 */
	public static List<TReleaseBean> loadChildrenNotClosed(Integer releaseID) {
		int[] releaseStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.NOT_PLANNED});
		return releaseDAO.loadChildrenByParentAndStates(releaseID, releaseStates);
	}

	/**
	 * Loads the  not closed child releases for a release
	 * @param releaseID
	 * @return
	 */
	public static List<TReleaseBean> loadChildrenActiveNotPlanned(Integer releaseID) {
		int[] releaseStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.NOT_PLANNED});
		return releaseDAO.loadChildrenByParentAndStates(releaseID, releaseStates);
	}
	
	/**
	 * Loads the  not closed child releases for a release
	 * @param releaseID
	 * @return
	 */
	public static List<TReleaseBean> loadChildrenScheduled(Integer releaseID) {
		int[] releaseStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE,
				new int[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.CLOSED});
		return releaseDAO.loadChildrenByParentAndStates(releaseID, releaseStates);
	}
	
	/**
	 * Loads the child releaseBeans with specific states for parent releases
	 * @param releaseIDs
	 * @param stateFlags
	 * @return
	 */
	public static List<TReleaseBean> loadChildrenByParentsAndStates(List<Integer> releaseIDs, int[] stateFlags, Set<Integer> selectedReleaseIDsSet) {
		int[] releaseStates = SystemStatusBL.getStateIDsByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, stateFlags);
		return releaseDAO.loadChildrenByParentsAndStates(releaseIDs, releaseStates, selectedReleaseIDsSet);
	}

	/**
	 * Gets the releases for a project or children for a release
	 * @param projectID
	 * @return
	 */
	public static List<TReleaseBean> getReleases(Integer projectID, Integer releaseID, boolean showArchived) {
		if (releaseID==null) {
			if (showArchived) {
				return releaseDAO.loadMainByProject(projectID);
			} else {
				return loadMainNotClosedByProject(projectID);
			}
		} else {
			if (showArchived) {
				return releaseDAO.loadChildren(releaseID);
			} else {
				return loadChildrenNotClosed(releaseID);
			}
		}
	}

	/**
	 * Gets the ancestor projects, including the projectIDs also
	 * @param arrReleaseIDs
	 * @return
	 */
	public static Integer[] getDescendantReleaseIDs(Integer[] arrReleaseIDs) {
		return getDescendantReleaseIDs(arrReleaseIDs, getStateFlags(false, true, true, true));
	}

	public static Set<Integer> getDescendantReleasesAsSet(Integer releaseID) {
		return getDescendantReleaseIDsAsSet(new Integer[]{releaseID}, getStateFlags(false, true, true, true));
	}
	
	/**
	 * Gets the ancestor projects, including the projectIDs also
	 * @param arrReleaseIDs
	 * @param  stateFlags
	 * @return
	 */
	private static Set<Integer> getDescendantReleaseIDsAsSet(Integer[] arrReleaseIDs, int[] stateFlags) {
		Set<Integer> descendentReleaseIDs = new HashSet<Integer>();
		if (arrReleaseIDs!=null && arrReleaseIDs.length>0) {
			List<Integer> releaseIDs = GeneralUtils.createListFromIntArr(arrReleaseIDs);
			List<TReleaseBean> releaseBeans = loadByReleaseIDs(releaseIDs);
			for (TReleaseBean releaseBean : releaseBeans) {
				Integer releaseID = releaseBean.getObjectID();
				descendentReleaseIDs.add(releaseID);
			}
			while (!releaseIDs.isEmpty()) {
				releaseBeans = loadChildrenByParentsAndStates(releaseIDs, stateFlags, null);
				releaseIDs = new LinkedList<Integer>();
				for (TReleaseBean releaseBean : releaseBeans) {
					Integer releaseID = releaseBean.getObjectID();
					if (!descendentReleaseIDs.contains(releaseID)) {
						descendentReleaseIDs.add(releaseID);
						releaseIDs.add(releaseID);
					}
				}
			}
		}
		return descendentReleaseIDs;
	}
	
	/**
	 * Gets the descendant releases for a list of releases
	 * @param releaseBeans
	 * @param selfIncluded
	 * @return
	 */
	public static List<TReleaseBean> getDescendantReleases(List<TReleaseBean> releaseBeans,boolean selfIncluded) {
		return getDescendantReleases(releaseBeans, selfIncluded, getStateFlags(false, true, true, true));
	}
	
	/**
	 * Gets the descendant releases for a list of releases
	 * @param releaseBeans
	 * @param selfIncluded
	 * @param stateFlags
	 * @return
	 */
	private static List<TReleaseBean> getDescendantReleases(List<TReleaseBean> releaseBeans,boolean selfIncluded,int[] stateFlags) {
		List<TReleaseBean> result = new ArrayList<TReleaseBean>();
		if (releaseBeans!=null && !releaseBeans.isEmpty()) {
			Set<Integer> descendentReleaseIDs = new HashSet<Integer>();
			List<Integer> releaseIDs = GeneralUtils.createIntegerListFromBeanList(releaseBeans);
			for (TReleaseBean releaseBean : releaseBeans) {
				Integer releaseID = releaseBean.getObjectID();
				descendentReleaseIDs.add(releaseID);
				if(selfIncluded){
					result.add(releaseBean);
				}
			}
			while (!releaseIDs.isEmpty()) {
				releaseBeans = loadChildrenByParentsAndStates(releaseIDs, stateFlags, null);
				releaseIDs = new LinkedList<Integer>();
				for (TReleaseBean releaseBean : releaseBeans) {
					Integer releaseID = releaseBean.getObjectID();
					if (!descendentReleaseIDs.contains(releaseID)) {
						descendentReleaseIDs.add(releaseID);
						releaseIDs.add(releaseID);
						result.add(releaseBean);
					}
				}
			}
		}
		return result;
	}

	private static Integer[] getDescendantReleaseIDs(Integer[] arrReleaseIDs, int[] stateFlags) {
		Set<Integer> descendentReleaseIDs = getDescendantReleaseIDsAsSet(arrReleaseIDs, stateFlags);
		return GeneralUtils.createIntegerArrFromCollection(descendentReleaseIDs);
	}
	
	public static List<TreeNode> getReleaseNodesEager(WorkItemContext workItemContext,boolean isScheduled){
		return getReleaseNodesEager(workItemContext.getPerson(),workItemContext.getWorkItemBean(),isScheduled,workItemContext.getLocale());
	}
	
	/**
	 * Gets the releases tree for a workItemBean
	 * @param userID
	 * @param workItemBean
	 * @param isScheduled
	 * @param locale
	 * @return
	 */
	public static List<TreeNode> getReleaseNodesEager(Integer userID, TWorkItemBean workItemBean, boolean isScheduled, Locale locale){
		Integer releaseID=null;
		if(isScheduled){
			releaseID=workItemBean.getReleaseScheduledID();
		}else{
			releaseID=workItemBean.getReleaseNoticedID();
		}
		boolean inactive = !isScheduled;
		boolean notPlanned = isScheduled;
		boolean closed = false;
		boolean create = workItemBean.getObjectID()==null;
		if (!create) {
			//for existing issues depending on actual release type (in addition to those releases for new issue)
			//also the inactive or notPlanned or closed releases are also included because the actual release
			//should be included always in the release list independently of release status
			if(releaseID!=null) {
				TReleaseBean releaseBean= LookupContainer.getReleaseBean(releaseID);
				if(releaseBean!=null){
					Integer statusFlag = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, releaseBean.getStatus());
					if (statusFlag!=null) {
						switch (statusFlag.intValue()) {
							case TSystemStateBean.STATEFLAGS.INACTIVE:
								inactive = true;
								break;
							case TSystemStateBean.STATEFLAGS.CLOSED:
								closed = true;
								break;
							case TSystemStateBean.STATEFLAGS.NOT_PLANNED:
								notPlanned = true;
								break;
						}
					}
				}
			}
		}
		Integer projectID=workItemBean.getProjectID();
		List<Integer> projectIDList = new LinkedList<Integer>();
		projectIDList.add(projectID);
		TPersonBean personBean = LookupContainer.getPersonBean(userID);
		//boolean useSelectable = !useChecked && !projectIsSelectable;
		return ReleaseBL.getReleaseNodesEager(personBean,
				projectIDList, closed, inactive, true, notPlanned,
				null, false, false, false, null, locale);
	}

	/**
	 * Gets releases eagerly (the entire hierarchy at once)	
	 * @param personBean
	 * @param projectIDs
	 * @param closed
	 * @param inactive
	 * @param active
	 * @param notPlanned
	 * @param selectedReleaseIDsSet
	 * @param useChecked
	 * @param projectIsSelectable
	 * @param withParameter
	 * @param locale
	 * @return
	 */
	public static List<TreeNode> getReleaseNodesEager(TPersonBean personBean, List<Integer> projectIDs, boolean closed,
			boolean inactive, boolean active, boolean notPlanned, Set<Integer> selectedReleaseIDsSet,
			boolean useChecked, boolean projectIsSelectable, boolean withParameter, Integer exceptID, Locale locale) {
		int[] stateFlags = getStateFlags(closed, inactive, active, notPlanned);
		/**
		 * get the selected projects in the project/release picker by inverting the negative projectIDs back
		 */
		Set<Integer> selectedProjectIDSet = null;
		if (projectIsSelectable && selectedReleaseIDsSet!=null && !selectedReleaseIDsSet.isEmpty()) {
			selectedProjectIDSet = new HashSet<Integer>();
			for (Iterator<Integer> iterator = selectedReleaseIDsSet.iterator(); iterator.hasNext();) {
				Integer objectID = iterator.next();
				if (objectID.intValue()<0) {
					selectedProjectIDSet.add(Integer.valueOf(-objectID.intValue()));
					//remove the selected project from selected releases
					iterator.remove();
				}
			}
		}
		List<TreeNode> treeNodesFirstLevel = null;
		Integer projectID = null;
		Map<Integer, TreeNode> projectTreeNodesMap = new HashMap<Integer, TreeNode>();
		if (projectIDs==null || projectIDs.isEmpty() || projectIDs.contains(MatcherContext.PARAMETER)) {
			//if no explicit project is selected for tree filter releases or project/release picker/tree get all used projects
			//projects for release picker/tree or project/release picker/tree
			treeNodesFirstLevel = ProjectBL.getUsedProjectNodesEager(personBean, false,
					new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN,
					AccessBeans.AccessFlagIndexes.READANYTASK}, selectedProjectIDSet, useChecked && projectIsSelectable,
					projectIsSelectable, projectIsSelectable, !projectIsSelectable, false, locale, projectTreeNodesMap);
			if (projectTreeNodesMap.size()==1 && !projectIsSelectable) {
				projectID = projectTreeNodesMap.keySet().iterator().next();
			} else {
				projectIDs = GeneralUtils.createListFromSet(projectTreeNodesMap.keySet());
			}
		} else {
			if ((projectIDs.size()==1) && !projectIsSelectable) {
				//releases on the first level
				projectID = projectIDs.get(0);
			} else {
				//projects for release picker
				treeNodesFirstLevel = ProjectBL.getProjectTreeForReleasesWithCompletedPath(projectIDs, false, 
						null, projectIsSelectable, projectIsSelectable, !projectIsSelectable, projectTreeNodesMap);
			}
		}
		//filter out projects whose project types does not allow releases
		if (projectIDs!=null && !projectIDs.isEmpty()) {
			List<TProjectBean> projectBeanList = ProjectBL.loadByProjectIDs(projectIDs);
			Set<Integer> projectTypeIDs = new HashSet<Integer>();
			if (projectBeanList!=null) {
				for (TProjectBean projectBean : projectBeanList) {
					projectTypeIDs.add(projectBean.getProjectType());
				}
			}
			Map<Integer, TProjectTypeBean> projectTypeMap = ProjectTypesBL.loadProjectTypeMapByIDs(GeneralUtils.createIntegerListFromCollection(projectTypeIDs));
			for (Iterator<TProjectBean> iterator = projectBeanList.iterator(); iterator.hasNext();) {
				TProjectBean projectBean = iterator.next();
				Integer projectType = projectBean.getProjectType(); 
				if (!ProjectConfigBL.mightHaveRelease(projectTypeMap.get(projectType), projectBean)) {
					iterator.remove();
				}
			}
			projectIDs = GeneralUtils.createIntegerListFromBeanList(projectBeanList);
		}
		if (projectID!=null) {
			//only releases for one project: the tree contains only releases
			List<TReleaseBean> releaseBeans = loadMainByProjectsAndStates(projectIDs, stateFlags, selectedReleaseIDsSet);
			treeNodesFirstLevel = getReleaseNodesEager(releaseBeans, stateFlags, selectedReleaseIDsSet, useChecked, exceptID, null, null);
		} else {
			//the tree contain project nodes (which are selectable or not) and the the releases below projects 
			List<TReleaseBean> releaseBeans = loadMainByProjectsAndStates(projectIDs, stateFlags, selectedReleaseIDsSet);	
			getReleaseNodesEager(releaseBeans, stateFlags, selectedReleaseIDsSet, useChecked, exceptID, treeNodesFirstLevel, projectTreeNodesMap);
			if (!projectIsSelectable) {
				removeProjectsWithoutRelease(treeNodesFirstLevel);
			}
		}
		if (withParameter && treeNodesFirstLevel != null) {
			treeNodesFirstLevel.add(createReleaseTreeNode(MatcherContext.PARAMETER, MatcherContext.getLocalizedParameter(locale),
					RELEASE_ICON_CLASS, useChecked,
					selectedReleaseIDsSet!=null && selectedReleaseIDsSet.contains(MatcherContext.PARAMETER)));
		}
		return treeNodesFirstLevel;
	}
	
	/**
	 * Gets releases eagerly (the entire hierarchy at once)	
	 * @param releaseBeans
	 * @param stateFlags
	 * @param selectedReleaseIDsSet
	 * @param useChecked
	 * @param projectTreeNodesList
	 * @return
	 */
	private static List<TreeNode> getReleaseNodesEager(List<TReleaseBean> releaseBeans,
			int[] stateFlags, Set<Integer> selectedReleaseIDsSet, boolean useChecked, Integer exceptID,
			List<TreeNode> projectTreeNodesList, Map<Integer, TreeNode> projectTreeNodesMap) {
		boolean projectsAtFirstLevel = false;
		Map<Integer, TSystemStateBean> releaseStatusMap = GeneralUtils.createMapFromList(LookupContainer.getSystemStateList(TSystemStateBean.ENTITYFLAGS.RELEASESTATE));
				/*GeneralUtils.createMapFromList(
				SystemStatusBL.getSystemStatesByByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, stateFlags));*/
		if (projectTreeNodesList!=null) {
			projectsAtFirstLevel = true;
		} else {
			//only releases
			projectTreeNodesList = new LinkedList<TreeNode>();
		}
		List<Integer> parentIDs = new LinkedList<Integer>();
		Map<Integer, TreeNode> releaseTreeNodeMap = new HashMap<Integer, TreeNode>();
		//add the main releases to the list
		for (TReleaseBean releaseBean : releaseBeans) {
			Integer releaseID = releaseBean.getObjectID();
			if (exceptID==null || !exceptID.equals(releaseID)) {
				boolean checked = false;
				if (useChecked) {
					checked = selectedReleaseIDsSet!=null && selectedReleaseIDsSet.contains(releaseID);
				}
				TreeNode mainReleaseTreeNode = createReleaseTreeNode(releaseID, releaseBean.getLabel(),
						getReleaseIcon(getReleaseStateFlag(releaseStatusMap, releaseBean.getStatus())), useChecked, checked);
				//not known previously whether this release has subreleases 
				mainReleaseTreeNode.setLeaf(Boolean.TRUE);
				parentIDs.add(releaseID);
				releaseTreeNodeMap.put(releaseID, mainReleaseTreeNode);
				if (projectsAtFirstLevel) {
					Integer projectID = releaseBean.getProjectID();
					TreeNode projectTreeNode = projectTreeNodesMap.get(projectID);
					List<TreeNode> projectChildren = projectTreeNode.getChildren();
					if (projectChildren==null) {
						projectChildren =  new LinkedList<TreeNode>();
						projectTreeNode.setChildren(projectChildren);
						projectTreeNode.setLeaf(Boolean.FALSE);
					}
					projectChildren.add(mainReleaseTreeNode);
				} else {
					projectTreeNodesList.add(mainReleaseTreeNode);
				}
			}
		}
		//get the subreleases
		while (!parentIDs.isEmpty()) {
			releaseBeans = loadChildrenByParentsAndStates(parentIDs, stateFlags, selectedReleaseIDsSet);
			parentIDs = new LinkedList<Integer>();
			for (TReleaseBean releaseBean : releaseBeans) {
				Integer releaseID = releaseBean.getObjectID();
				if (exceptID==null || !exceptID.equals(releaseID)) {
					Integer parentID = releaseBean.getParent();
					boolean checked = false;
					if (useChecked) {
						checked = selectedReleaseIDsSet!=null && selectedReleaseIDsSet.contains(releaseID);
					}
					TreeNode childNode = createReleaseTreeNode(releaseID, releaseBean.getLabel(),
							getReleaseIcon(getReleaseStateFlag(releaseStatusMap, releaseBean.getStatus())), useChecked, checked);
					TreeNode parentTreeNode = releaseTreeNodeMap.get(parentID);
					if (parentTreeNode!=null) {
						List<TreeNode> childrenNodes = parentTreeNode.getChildren();
						if (childrenNodes==null) {
							//reset leaf to false once a child is found
							parentTreeNode.setLeaf(Boolean.FALSE);
							childrenNodes = new LinkedList<TreeNode>();
							parentTreeNode.setChildren(childrenNodes);
						}
						releaseTreeNodeMap.put(releaseID, childNode);
						childrenNodes.add(childNode);
					}
					parentIDs.add(releaseID);
				}
			}
		}
		return projectTreeNodesList;
	}
	
	/**
	 * Remove projects without releases
	 * Should be applied when only the release is selectable but no project
	 * @param projectNodeList
	 */
	private static void removeProjectsWithoutRelease(List<TreeNode> projectNodeList) {
		if (projectNodeList!=null) {
			for (Iterator<TreeNode> iterator = projectNodeList.iterator(); iterator.hasNext();) {
				TreeNode treeNode = (TreeNode) iterator.next();
				if (hasRelease(treeNode)) {
					removeProjectsWithoutRelease(treeNode.getChildren());
				} else {
					iterator.remove();
				}
			}
		}
	}
	
	/**
	 * Whether the node has a release descendant
	 * @param treeNode
	 * @return
	 */
	private static boolean hasRelease(TreeNode treeNode) {
		String id = treeNode.getId();
		Integer objectID = null;
		try {
			objectID = Integer.valueOf(id);
		} catch(Exception e) {
		}
		if (objectID==null || objectID.intValue()<0) {
			//project
			List<TreeNode> children = treeNode.getChildren();
			if (children==null || children.isEmpty()) {
				return false;
			} else {
				for (TreeNode childTreeNode : children) {
					boolean hasRelease = hasRelease(childTreeNode);
					if (hasRelease) {
						return true;
					}
				}
				return false;
			}
		} else {
			//release
			return true;
		}
		
	}
	
	/**
	 * Gets the state flag for release
	 * @param releaseStatusMap
	 * @param releaseStatus
	 * @return
	 */
	static Integer getReleaseStateFlag(Map<Integer, TSystemStateBean> releaseStatusMap, Integer releaseStatus) {
		Integer stateFlag = null;
		if (releaseStatusMap!=null && releaseStatus!=null) {
			TSystemStateBean systemStateBean = releaseStatusMap.get(releaseStatus);
			if (systemStateBean!=null) {
				stateFlag = systemStateBean.getStateflag();
			}
		}
		return stateFlag;
	}
	
	static int[] getStateFlags(boolean closed, 
			boolean inactive, boolean active, boolean notPlanned) {
		List<Integer> stateFlags = new LinkedList<Integer>();
		if (closed) {
			stateFlags.add(TSystemStateBean.STATEFLAGS.CLOSED);
		}
		if (inactive) {
			stateFlags.add(TSystemStateBean.STATEFLAGS.INACTIVE);
		}
		if (active) {
			stateFlags.add(TSystemStateBean.STATEFLAGS.ACTIVE);
		}
		if (notPlanned) {
			stateFlags.add(TSystemStateBean.STATEFLAGS.NOT_PLANNED);
		}
		return GeneralUtils.createIntArrFromIntegerCollection(stateFlags);
	}
	
	/**
	 * Gets the release icon
	 * @param releaseBean
	 * @return
	 */
	public static String getReleaseIcon(TReleaseBean releaseBean) {
		return getReleaseIcon(SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, releaseBean.getStatus()));
	}
	
	/**
	 * Creates a treeNode from a projectBean
	 * @param stateFlag
	 * @return
	 */
	static String getReleaseIcon(Integer stateFlag) {
		String iconCls = null;
		if (stateFlag!=null) {
			switch (stateFlag) {
			case TSystemStateBean.STATEFLAGS.ACTIVE:
				iconCls = RELEASE_ICON_CLASS;
				break;
			case TSystemStateBean.STATEFLAGS.INACTIVE:
				iconCls = RELEASE_INACTIVE_CLASS;
				break;
			case TSystemStateBean.STATEFLAGS.NOT_PLANNED:
				iconCls = RELEASE_UNSCHEDULED_CLASS;
				break;
			case TSystemStateBean.STATEFLAGS.CLOSED:
				iconCls = RELEASE_CLOSED_CLASS;
				break;
			}
		} else {
			iconCls = RELEASE_ICON_CLASS;
		}
		return iconCls;
	}
	
	
	/**
	 * Creates a treeNode from a projectBean
	 * @return
	 */
	private static TreeNode createReleaseTreeNode(Integer objectID, String label, String iconCls, boolean useChecked, boolean checked) {		
		TreeNode treeNode = new TreeNode(objectID.toString(), label, iconCls);
		if (useChecked) {
			treeNode.setChecked(Boolean.valueOf(checked));
		}
		return treeNode;
	}
	
	/**
	 * Copy a release hierarchy
	 * @param projectID
	 * @param partentRelease
	 * @param releaseBean
	 * @param oldNewReleaseIDs
	 */
	public static void copyReleaseHierarchy(Integer projectID, Integer partentRelease, TReleaseBean releaseBean, Map<Integer, Integer> oldNewReleaseIDs) {
		TReleaseBean releaseBeanCopy = releaseDAO.copy(releaseBean, false); //should be false otherwise copies also the workItems!
		releaseBeanCopy.setProjectID(projectID);
		releaseBeanCopy.setParent(partentRelease);
		Integer newReleaseID = save(releaseBeanCopy);
		oldNewReleaseIDs.put(releaseBean.getObjectID(), newReleaseID);
		List<TReleaseBean> childReleases = releaseDAO.loadChildren(releaseBean.getObjectID());
		if (childReleases!=null && !childReleases.isEmpty()) {
			for (TReleaseBean childReleaseBean : childReleases) {
				copyReleaseHierarchy(projectID, newReleaseID, childReleaseBean, oldNewReleaseIDs);
			}
		}
	}

}
