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



package com.aurel.track.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.IHierarchicalBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.report.execute.SimpleTreeNode;

public class HierarchicalBeanUtils {
	
	/**
	 * Get the project hierarchy list based on the selected projects: if the projects have descendant projects 
	 * (for example parent and grandChild but without the child)
	 * then the entire hierarchy will be shown (the child will be added) even if the path between the
	 * selected projects contain holes (not selected projects in the hierarchy path)
	 * the project nodes are with modified IDs because they are used on the same tree with the releases,
	 * and the duplication of IDs should be avoided 
	 * @param involvedIDs
	 * @param fieldID
	 * @param simpleTreeNodeMap
	 * @return
	 */
	public static List<SimpleTreeNode> getSimpleProjectTreeWithCompletedPath(List<Integer> involvedIDs, Integer fieldID,
			Map<Integer, SimpleTreeNode> simpleTreeNodeMap) {
		List<SimpleTreeNode> simpleTreeNodesInFirstLevel = new LinkedList<SimpleTreeNode>();
		if (involvedIDs!=null && !involvedIDs.isEmpty()) {
			Set<Integer> baseIDsSet = new HashSet<Integer>();
			Set<Integer> childIDs = new HashSet<Integer>();
			List<IHierarchicalBean> baseBeansList = null;
			switch(fieldID.intValue()) {
				case SystemFields.PROJECT:
					baseBeansList = (List)ProjectBL.loadByProjectIDs(involvedIDs);
					break;
				case SystemFields.RELEASENOTICED:
				case SystemFields.RELEASESCHEDULED:
					baseBeansList = (List)ReleaseBL.loadByReleaseIDs(involvedIDs);
					break;
			}
			Set<Integer> firstLevelSet = new HashSet<Integer>();
			for (IHierarchicalBean hierarchicalBean : baseBeansList) {
				Integer projectID = hierarchicalBean.getObjectID();
				baseIDsSet.add(projectID);
				Integer parentID = hierarchicalBean.getParent();
				SimpleTreeNode projectTreeNode = new SimpleTreeNode(projectID, hierarchicalBean.getLabel(), hierarchicalBean.getSortOrderValue());
				simpleTreeNodeMap.put(projectID, projectTreeNode);
				if (parentID!=null) {
					/**
					 * gather the child projects for further processing
					 */
					childIDs.add(projectID);
				} else {
					/**
					 * the topmost parent projects are on the first level nodes anyway
					 */
					simpleTreeNodesInFirstLevel.add(projectTreeNode);
					firstLevelSet.add(projectID);
				}
			}
			Map<Integer, IHierarchicalBean> involvedHierarchicalBeansMap = new HashMap<Integer, IHierarchicalBean>();
			//get the child hierarchy till the topmost projects: this might include ancestors which are not in the base project list
			Map<Integer, Integer> childToParentMap = getChildToParentMap(baseBeansList, fieldID, involvedHierarchicalBeansMap);
			//add the highest ancestor projects (they are not topmost parent projects) from the base projects list as first level nodes
			//and remove all those ancestors of the base projects which are not in the base projects list
			for (Integer childID : childIDs) {
				Integer parentID = childID;
				List<Integer> ancestorsToRemove = new LinkedList<Integer>();
				Integer highestSelectedAncestor = null;
				while (parentID!=null) {
					parentID = childToParentMap.get(parentID);
					if (parentID!=null) {
						ancestorsToRemove.add(parentID);
						//an ancestor project of this project is also selected  
						if (baseIDsSet.contains(parentID)) {
							//the ancestor (and the other ancestors up to it) should remain: clear the ancestorsToRemove list to avoid removing them
							ancestorsToRemove.clear();
							highestSelectedAncestor = parentID;
						}
					}
				}
				if (highestSelectedAncestor!=null) {
					// add only if not added already this ancestor by a previous sibling branch
					if (!firstLevelSet.contains(highestSelectedAncestor)) {
						/**
						 * add the highest selected ancestor (a child project, not a topmost one) as first level node
						 */
						firstLevelSet.add(highestSelectedAncestor);
						SimpleTreeNode projectTreeNode = simpleTreeNodeMap.get(highestSelectedAncestor);
						if (projectTreeNode!=null) {
							simpleTreeNodesInFirstLevel.add(projectTreeNode);
						}
					}
				} else {
					/**
					 * none of the ancestors is member of the base projects list:
					 * add as first level project
					 */
					SimpleTreeNode projectTreeNode = simpleTreeNodeMap.get(childID);
					if (projectTreeNode!=null) {
						if (!firstLevelSet.contains(childID)) {
							firstLevelSet.add(childID);
							simpleTreeNodesInFirstLevel.add(projectTreeNode);
						}
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
			for (Integer childID : childToParentMap.keySet()) {
				//the children whose ancestor path contains selected projects: add them in the hierarchy
				SimpleTreeNode childTreeNode = simpleTreeNodeMap.get(childID);
				if (childTreeNode==null) {
					IHierarchicalBean hierarchicalBean = involvedHierarchicalBeansMap.get(childID);
					childTreeNode = new SimpleTreeNode(childID, hierarchicalBean.getLabel(), hierarchicalBean.getSortOrderValue());
					simpleTreeNodeMap.put(childID, childTreeNode);
				}
				Integer parentID = childToParentMap.get(childID);
				SimpleTreeNode parentTreeNode = simpleTreeNodeMap.get(parentID);
				if (parentTreeNode==null) {
					IHierarchicalBean projectBean = involvedHierarchicalBeansMap.get(parentID);
					parentTreeNode = new SimpleTreeNode(parentID, projectBean.getLabel(), projectBean.getSortOrderValue()); 
					simpleTreeNodeMap.put(parentID, parentTreeNode);
				}
				List<SimpleTreeNode> childrenList = parentTreeNode.getChildren();
				if (childrenList==null) {
					childrenList = new LinkedList<SimpleTreeNode>();
					parentTreeNode.setChildren(childrenList);
				}
				childrenList.add(childTreeNode);
			}
		}
		return simpleTreeNodesInFirstLevel;
	}
	
	
	/**
	 * Gets the child to parent map for the child projects starting from projectBeans up to the main projects
	 * @param hierarchicalBeans
	 * @param involvedProjectsMap: out parameter
	 * @return
	 */
	private static Map<Integer, Integer> getChildToParentMap(List<IHierarchicalBean> hierarchicalBeans, Integer fieldID, Map<Integer, IHierarchicalBean> involvedBeanMap) {
		//implicit project administrator projects: child to parent map with the 
		//entire project hierarchies starting from project administrator projects
		Map<Integer, Integer> childToParentMap = new HashMap<Integer, Integer>();
		//set with all ancestor projectIDs 
		Set<Integer> ancestorIDs = new HashSet<Integer>();
		if (hierarchicalBeans!=null) {
			for (IHierarchicalBean hierarchicalBean : hierarchicalBeans) {
				Integer objectID = hierarchicalBean.getObjectID();
				Integer parentID = hierarchicalBean.getParent();
				if (parentID!=null) {
					ancestorIDs.add(parentID);
					childToParentMap.put(objectID, parentID);
				}
				if (involvedBeanMap!=null) {
					involvedBeanMap.put(objectID, hierarchicalBean);
				}
			}
		}
		Set<Integer> parentIDs = ancestorIDs;
		//up to the main projects level (projects without a parent)
		while (!parentIDs.isEmpty()) {
			//get the next level of parents (hierarchically speaking the previous level)
			List<IHierarchicalBean> parentBeans = null;
			switch(fieldID.intValue()) {
			case SystemFields.PROJECT:
				parentBeans = (List)ProjectBL.loadByProjectIDs(GeneralUtils.createListFromSet(parentIDs));
				break;
			case SystemFields.RELEASENOTICED:
			case SystemFields.RELEASESCHEDULED:
				parentBeans = (List)ReleaseBL.loadByReleaseIDs(GeneralUtils.createListFromSet(parentIDs));
				break;
			}
			
			parentIDs = new HashSet<Integer>();
			for (IHierarchicalBean parentBean : parentBeans) {
				Integer objectID = parentBean.getObjectID();
				Integer parentID = parentBean.getParent();
				if (parentID!=null && !ancestorIDs.contains(parentID)) {
					//only if ancestorProjectIDs not contain already (by processing a sibling branch starting from an upper level)
					parentIDs.add(parentID);
					ancestorIDs.add(parentID);
				}
				if (parentID!=null) {
					childToParentMap.put(objectID, parentID);
				}
				if (involvedBeanMap!=null) {
					involvedBeanMap.put(objectID, parentBean);
				}
			}
		}
		return childToParentMap;
	}
	/**
	 * Get the Hierarchical descendants map
	 * 
	 * @param hierarchySelectedItems the hierarchy elements for which we select descendants
	 * @param allHierarchyDescendants all available descendants
	 * @return a map from hierarchy id to it descendants hierarchy id
	 */
	public static Map<Integer,Set<Integer>> getDescendantMap(List<? extends IHierarchicalBean> hierarchySelectedItems,List<? extends IHierarchicalBean> allHierarchyDescendants){
		Map<Integer, Set<Integer>> result = initializeEmptyDescendantMap(hierarchySelectedItems);
		
		for (Map.Entry<Integer, Set<Integer>> entry : result.entrySet()) {
		    Integer selectedHierarchyId = entry.getKey();
		    
		    for(IHierarchicalBean descendant : allHierarchyDescendants) {
		    	if (descendantProjectBelongsTo(selectedHierarchyId, descendant, allHierarchyDescendants)) {
		    		result.get(selectedHierarchyId).add(descendant.getObjectID());
		    	}
		    }
		}
		return result;
	}		
	
	/**
	 * Check if descendant belongs to selectedHierarchyId
	 * 
	 * @param selectedHierarchyId the root hierarchy Id
	 * @param descendant the descendant hierarchy to check if belongs to the root hierarchy Id ( selectedHierarchyId )
	 * @param allHierarchyDescendants all available descendants (the list is needed to navigate to the root project)
	 * @return true if descendantProject belongs to selectedHierarchyId
	 */
	private static boolean descendantProjectBelongsTo(
			Integer selectedHierarchyId, IHierarchicalBean descendant,
			List<? extends IHierarchicalBean> allHierarchyDescendants) {
		
		if (descendant.getParent()==null) {
			return false;
		} else if (selectedHierarchyId.equals(descendant.getParent())) {
			return true;
		} else {
			 Integer parentHierarchyId = descendant.getParent();
			 IHierarchicalBean parentHierarchyProject = findParent(parentHierarchyId,allHierarchyDescendants);
			if (parentHierarchyProject!=null) {
				return descendantProjectBelongsTo(selectedHierarchyId, parentHierarchyProject, allHierarchyDescendants);
			}
		}
		
		return false;
	}

	/**
	 * find hierarchy element for given parent hierarchy id
	 * 
	 * @param parentId the parent hierarchy id
	 * @param hierarchyElements a hierarchy list with possible matches
	 * @return the found hierarchy element, could return null is nothing is find
	 */
	private static IHierarchicalBean findParent(Integer parentId,
			List<? extends IHierarchicalBean> hierarchyElements) {
		for (IHierarchicalBean element : hierarchyElements) {
			if (parentId.equals(element.getObjectID())) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Initialize an empty map that contains only key that are given by hierarchy elements id
	 * @param hierarchySelectedItems the hierarchy elements for which we select descendants
	 * @return empty map that contains only key and values are empty lists
	 */
	private static Map<Integer, Set<Integer>> initializeEmptyDescendantMap(
			List<? extends IHierarchicalBean> selectedProjects) {
		Map<Integer,Set<Integer>> result=new HashMap<Integer, Set<Integer>>();
		for(IHierarchicalBean selectedProject : selectedProjects){
			result.put(selectedProject.getObjectID(), new HashSet<Integer>());
		}
		return result;
	}
}
