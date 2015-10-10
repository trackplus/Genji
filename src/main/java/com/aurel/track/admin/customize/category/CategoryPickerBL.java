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

package com.aurel.track.admin.customize.category;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.CategoryBL.REPOSITORY_TYPE;
import com.aurel.track.admin.customize.category.CategoryBL.TYPE;
import com.aurel.track.admin.customize.notify.settings.NotifySettingsBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ILabelBeanWithCategory;
import com.aurel.track.beans.ILabelBeanWithParent;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

/**
 * Get the nodes for a filter- (issue or notify) or for reports-picker
 * The nodes are loaded eagerly to ease the preselecting of a node in the hierarchy.
 * Getting the nodes are implemented pro level (implementing it recursively would increase the number of db accesses exponentially)
 * The format of the nodeIDs for folders (projects and categories) are the same as for the non picker version but the leafs are integers
 * A picker will return an integer (instead of a complicated nodeID, but for
 * folders the IDs can't be integers because that would cause nodes with possibly duplicated IDs)
 * @author Tamas Ruff
 *
 */
public class CategoryPickerBL {

	private CategoryPickerBL() {
	}

	public static List<TreeNode> getQueryrNodesEager(Integer userID,Locale locale){
		TPersonBean personBean= LookupContainer.getPersonBean(userID);
		boolean excludePrivate=false;
		boolean useChecked=false;
		Set<Integer> selectedIDSet=null;
		Integer projectID=null;
		Integer exceptID=null;
		String node="issueFilter";
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
		List<TreeNode> nodes = CategoryPickerBL.getPickerNodesEager(
				personBean, excludePrivate, useChecked, selectedIDSet,
				false, projectID, exceptID, locale, categoryType);
		return nodes;
	}


	/**
	 * Get the picker/tree filters/reports
	 * @param personBean
	 * @param excludePrivate
	 * @param selectedIDSet
	 * @param useChecked
	 * @param allIfSystemAdmin
	 * @param selectedProjectID
	 * @param exceptID
	 * @param locale
	 * @param categoryType
	 * @return
	 */
	public static List<TreeNode> getPickerNodesEager(TPersonBean personBean, boolean excludePrivate,
			boolean useChecked, Set<Integer> selectedIDSet, boolean allIfSystemAdmin,
			Integer selectedProjectID, Integer exceptID, Locale locale, String categoryType) {
		List<TreeNode> rootNodes = new LinkedList<TreeNode>();
		/**
		 * private nodes
		 */
		if (!excludePrivate) {
			TreeNode privateRepositoryNode = new TreeNode(CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PRIVATE),
					LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.private", locale));
			privateRepositoryNode.setLeaf(Boolean.FALSE);
			privateRepositoryNode.setSelectable(false);
			rootNodes.add(privateRepositoryNode);
			List<ILabelBean> privateRootCategories = getRootCategories(categoryType, REPOSITORY_TYPE.PRIVATE, null, personBean.getObjectID(), locale);
			List<ILabelBean> privateRootLeafs = getRootLeafs(categoryType, REPOSITORY_TYPE.PRIVATE, null, personBean.getObjectID(), locale);
			Map<Integer, TreeNode> privateRepositoryMap = new HashMap<Integer, TreeNode>();
			privateRepositoryMap.put(REPOSITORY_TYPE.PRIVATE, privateRepositoryNode);
			addNodesHierarchy(categoryType, REPOSITORY_TYPE.PRIVATE, privateRootCategories, privateRootLeafs, useChecked, selectedIDSet, exceptID, privateRepositoryMap);
		}
		/**
		 * project nodes
		 */
		TreeNode projectRepositoryNode = new TreeNode(
				CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PROJECT),
				LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.project", locale));
		projectRepositoryNode.setLeaf(Boolean.FALSE);
		projectRepositoryNode.setSelectable(false);
		rootNodes.add(projectRepositoryNode);
		Map<Integer, TreeNode> projectRepositoryMap = new HashMap<Integer, TreeNode>();
		projectRepositoryMap.put(REPOSITORY_TYPE.PROJECT, projectRepositoryNode);
		projectRepositoryNode.setChildren(getProjectRepositoryNodes(categoryType,
				REPOSITORY_TYPE.PROJECT, personBean, locale, allIfSystemAdmin, selectedProjectID, exceptID, useChecked, selectedIDSet));
		/**
		 * public nodes
		 */
		TreeNode publicRepositoryNode = new TreeNode(
				CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PUBLIC),
				LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.public", locale));
		publicRepositoryNode.setLeaf(Boolean.FALSE);
		publicRepositoryNode.setSelectable(false);
		rootNodes.add(publicRepositoryNode);
		List<ILabelBean> publicRootCategories = getRootCategories(categoryType, REPOSITORY_TYPE.PUBLIC, null, personBean.getObjectID(), locale);
		List<ILabelBean> publicRootLeafs = getRootLeafs(categoryType, REPOSITORY_TYPE.PUBLIC, null, personBean.getObjectID(), locale);
		Map<Integer, TreeNode> publicRepositoryMap = new HashMap<Integer, TreeNode>();
		publicRepositoryMap.put(REPOSITORY_TYPE.PUBLIC, publicRepositoryNode);
		addNodesHierarchy(categoryType, REPOSITORY_TYPE.PUBLIC, publicRootCategories, publicRootLeafs, useChecked, selectedIDSet, exceptID, publicRepositoryMap);
		removeLeaflessBranches(rootNodes);
		return rootNodes;
	}

	/**
	 * Get simple nodes for picker
	 * @param labelBean
	 * @param leafFacade
	 * @param useChecked
	 * @po

	 *
	 */
	private static TreeNode getSimpleNode(/*String categoryType, Integer repository,*/ ILabelBean labelBean, LeafFacade leafFacade,
			boolean useChecked, Set<Integer> selectedIDSet) {
		Integer objectID = labelBean.getObjectID();
		TreeNode treeNode = new TreeNode(labelBean.getObjectID().toString(), labelBean.getLabel(), leafFacade.getIconCls(labelBean));
		treeNode.setLeaf(Boolean.TRUE);
		if (useChecked) {
			treeNode.setChecked(getChecked(useChecked, objectID, selectedIDSet));
		}
		return treeNode;
	}

	/**
	 * Get the checked flag if useChecked is true
	 * @param useChecked
	 * @param reportOrFilterID
	 * @param selectedIDs
	 * @return
	 */
	private static Boolean getChecked(boolean useChecked, Integer reportOrFilterID, Set<Integer> selectedIDs) {
		Boolean checked = null;
		if (useChecked) {
			if (selectedIDs!=null) {
				checked = Boolean.valueOf(selectedIDs.contains(reportOrFilterID));
			} else {
				checked = Boolean.FALSE;
			}
		}
		return checked;
	}

	/**
	 * Remove the branches which does not have a leaf descendant (only leafs are selectable)
	 * @param treeNodes
	 * @return
	 */
	private static boolean removeLeaflessBranches(List<TreeNode> treeNodes) {
		boolean removeChildNode = true;
		boolean removeParentNode = true;
		for (Iterator<TreeNode> iterator = treeNodes.iterator(); iterator.hasNext();) {
			TreeNode treeNode = iterator.next();
			Boolean leaf = treeNode.getLeaf();
			if (leaf!=null && leaf.booleanValue()) {
				//leaf found
				removeChildNode = false;
				removeParentNode = false;
			} else {
				//process branch
				List<TreeNode> childenNodes = treeNode.getChildren();
				if (childenNodes!=null && !childenNodes.isEmpty()) {
					//important the order in "AND": first the recursive call then added with the partial value:
					//to guarantee that the leafless sibling subtrees will be also removed even if the parent has a leaf descendant on a sibling branch
					removeChildNode = removeLeaflessBranches(childenNodes) /*&& removeTreeNode*/;
					removeParentNode = removeParentNode && removeChildNode;
				} else {
					removeChildNode = true;
				}
			}
			if (removeChildNode) {
				iterator.remove();
			}
		}
		return removeParentNode;
	}

	/**
	 * Builds the repository for a hierarchy
	 * @param categoryType
	 * @param repository
	 * @param personBean
	 * @return
	 */
	private static List<TreeNode> getProjectRepositoryNodes(String categoryType, Integer repository,
			TPersonBean personBean, Locale locale, boolean allIfSystemAdmin, Integer selectedProjectID, Integer exceptID,
			boolean useChecked, Set<Integer> selectedIDSet) {
		List<TreeNode> mainProjectNodes = new LinkedList<TreeNode>();
		List<TProjectBean> projectBeans = getRootProjects(personBean, allIfSystemAdmin, selectedProjectID);
		/**
		 * Add the root projects
		 */
		Map<Integer, TreeNode> projectNodeMap = new HashMap<Integer, TreeNode>();
		List<Integer> parentProjectIDs = new LinkedList<Integer>();

		Map<Integer, TSystemStateBean> projectStatusMap = GeneralUtils.createMapFromList(
				SystemStatusBL.getSystemStatesByByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
						new int[] {TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.ACTIVE}));
		for (TProjectBean projectBean : projectBeans) {
			Integer projectID = projectBean.getObjectID();
			CategoryTokens projectCategoryTokens = new CategoryTokens(categoryType,
					repository, Integer.valueOf(TYPE.PROJECT), projectID);
			TreeNode rootProjectNode = new TreeNode(CategoryTokens.encodeNode(projectCategoryTokens),
					projectBean.getLabel(), ProjectBL.getProjectIcon(ProjectBL.getProjectStateFlag(projectStatusMap, projectBean.getStatus())));
			//not known previously whether this project has subprojects
			rootProjectNode.setLeaf(Boolean.FALSE);
			rootProjectNode.setSelectable(false);
			parentProjectIDs.add(projectID);
			projectNodeMap.put(projectID, rootProjectNode);
			mainProjectNodes.add(rootProjectNode);
		}
		/**
		 * Add the main project specific categories and leafs
		 */
		List<ILabelBean> projectRootCategories = getProjectRootCategories(categoryType, parentProjectIDs, locale);
		List<ILabelBean> projectRootLeafs = getProjectRootLeafs(categoryType, parentProjectIDs, locale);
		addNodesHierarchy(categoryType, repository, projectRootCategories, projectRootLeafs, useChecked, selectedIDSet, exceptID, projectNodeMap);

		//search for subprojects only if selectedProjectID is not specified
		if (selectedProjectID==null) {
			//while projects are found in the next level
			while (!parentProjectIDs.isEmpty()) {
				projectBeans = ProjectBL.loadActiveInactiveSubrojects(parentProjectIDs);
				parentProjectIDs = new LinkedList<Integer>();
				for (TProjectBean projectBean : projectBeans) {
					Integer projectID = projectBean.getObjectID();
					Integer parentID = projectBean.getParent();
					CategoryTokens projectCategoryTokens = new CategoryTokens(categoryType,
							repository, Integer.valueOf(TYPE.PROJECT), projectID);
					TreeNode childNode = new TreeNode(CategoryTokens.encodeNode(projectCategoryTokens),
							projectBean.getLabel(), ProjectBL.getProjectIcon(ProjectBL.getProjectStateFlag(projectStatusMap, projectBean.getStatus())));
					childNode.setLeaf(Boolean.FALSE);
					TreeNode parentTreeNode = projectNodeMap.get(parentID);
					if (parentTreeNode!=null) {
						List<TreeNode> childrenNodes = parentTreeNode.getChildren();
						if (childrenNodes==null) {
							//reset leaf to false once a child is found
							childrenNodes = new LinkedList<TreeNode>();
							parentTreeNode.setChildren(childrenNodes);
						}
						projectNodeMap.put(projectID, childNode);
						childrenNodes.add(childNode);
					}
					parentProjectIDs.add(projectID);
				}
				/**
				 * add the categories and leafs  for this level of subprojects
				 */
				projectRootCategories = getProjectRootCategories(categoryType, parentProjectIDs, locale);
				projectRootLeafs = getProjectRootLeafs(categoryType, parentProjectIDs, locale);
				addNodesHierarchy(categoryType, repository, projectRootCategories, projectRootLeafs, useChecked, selectedIDSet, exceptID, projectNodeMap);
			}
		}
		return mainProjectNodes;
	}

	/**
	 * Adds the nodes hierarchically
	 * @param categoryType
	 * @param repository
	 * @param rootCategories
	 * @param rootLeafs
	 * @param treeNodeMap
	 */
	private static void addNodesHierarchy(String categoryType, Integer repository,
			List<ILabelBean> rootCategories, List<ILabelBean> rootLeafs,
			boolean useChecked, Set<Integer> selectedIDSet,
			Integer exceptID,  Map<Integer, TreeNode> treeNodeMap) {
		/**
		 * Add the root categories
		 */
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		List<Integer> parentCategoryIDs = new LinkedList<Integer>();
		Map<Integer, TreeNode> categoryNodeMap = new HashMap<Integer, TreeNode>();
		if (rootCategories!=null) {
			for (ILabelBean rootCategory : rootCategories) {
				Integer categoryID = rootCategory.getObjectID();
				Integer projectID = ((ILabelBeanWithParent)rootCategory).getProject();
				TreeNode rootCategoryNode = categoryFacade.getSimpleNode(categoryType, repository, rootCategory);
				//not known previously whether this category has children
				rootCategoryNode.setLeaf(Boolean.FALSE);
				rootCategoryNode.setSelectable(false);
				parentCategoryIDs.add(categoryID);
				categoryNodeMap.put(categoryID, rootCategoryNode);
				TreeNode parentNode = null;
				if (projectID==null) {
					//repository parent
					parentNode = treeNodeMap.get(repository);
				} else {
					//project parent
					parentNode = treeNodeMap.get(projectID);
				}
				if (parentNode!=null) {
					List<TreeNode> childNodes = parentNode.getChildren();
					if (childNodes==null) {
						childNodes = new LinkedList<TreeNode>();
						parentNode.setChildren(childNodes);
					}
					childNodes.add(rootCategoryNode);
				}
			}
		}
		/**
		 * Add the root leafs
		 */
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		if (rootLeafs!=null) {
			for (ILabelBean rootLeaf : rootLeafs) {
				if (exceptID==null || !exceptID.equals(rootLeaf.getObjectID())) {
					Integer projectID = ((ILabelBeanWithCategory)rootLeaf).getProject();
					TreeNode rootLeafNode = getSimpleNode(rootLeaf, leafFacade, useChecked, selectedIDSet);
					TreeNode parentNode = null;
					if (projectID==null) {
						//repository parent
						parentNode = treeNodeMap.get(repository);
					} else {
						//project parent
						parentNode = treeNodeMap.get(projectID);
					}
					if (parentNode!=null) {
						List<TreeNode> childNodes = parentNode.getChildren();
						if (childNodes==null) {
							childNodes = new LinkedList<TreeNode>();
							parentNode.setChildren(childNodes);
						}
						childNodes.add(rootLeafNode);
					}
				}
			}
		}
		/**
		 * Add the next level of categories and leafs
		 */
		while (!parentCategoryIDs.isEmpty()) {
			/**
			 * add subcategories at level
			 */
			List<Integer> subcategoryIDs = addSubcategoriesByCategories(categoryNodeMap, categoryType, repository, parentCategoryIDs);
			/**
			 * add leafs at level
			 */
			addLeafsByCategories(categoryNodeMap, categoryType, parentCategoryIDs, useChecked, selectedIDSet);

			parentCategoryIDs = subcategoryIDs;
		}
	}

	/**
	 * Get the root projects
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param projectID
	 * @return
	 */
	private static List<TProjectBean> getRootProjects(
			TPersonBean personBean, boolean allIfSystemAdmin, Integer projectID) {
		List<TProjectBean> rootProjectBeans = null;
		if (projectID==null) {
			rootProjectBeans = ProjectBL.getActiveInactiveFirstLevelProjectsByRights(personBean, allIfSystemAdmin,
					new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN,
					AccessBeans.AccessFlagIndexes.READANYTASK});
		} else {
			rootProjectBeans = new LinkedList<TProjectBean>();
			if (!projectID.equals(NotifySettingsBL.OTHERPROJECTSID)) {
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					rootProjectBeans.add(projectBean);
				}
			}
		}
		return rootProjectBeans;
	}

	/**
	 * Get the root categories
	 * @param categoryType
	 * @param projectIDs
	 * @return
	 */
	private static List<ILabelBean> getProjectRootCategories(String categoryType, List<Integer> projectIDs, Locale locale) {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		return categoryFacade.getRootObjectsByProjects(projectIDs, locale);
	}

	/**
	 * Get the root categories
	 * @param categoryType
	 * @param projectIDs
	 * @return
	 */
	private static List<ILabelBean> getProjectRootLeafs(String categoryType, List<Integer> projectIDs, Locale locale) {
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		List<ILabelBean> projectRootLeafs =  leafFacade.getRootObjectsByProjects(projectIDs, locale);
		return leafFacade.filterFromIssueNavigator(projectRootLeafs, true);
	}

	/**
	 * Get the root categories
	 * @param categoryType
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @return
	 */
	private static List<ILabelBean> getRootCategories(String categoryType,
			Integer repository, Integer projectID, Integer personID, Locale locale) {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		Integer filterPerson = null;
		if (repository.intValue()==REPOSITORY_TYPE.PRIVATE) {
			//filter the filters by person only if the repository is private
			filterPerson = personID;
		}
		return categoryFacade.getRootObjects(repository, projectID, filterPerson, locale);
	}

	/**
	 * Get the root leafs
	 *
	 * @param categoryType
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @return
	 */
	private static List<ILabelBean> getRootLeafs(String categoryType,
			Integer repository, Integer projectID, Integer personID,
			Locale locale) {
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance()
				.getLeafFacade(categoryType);
		Integer filterPerson = null;
		if (repository.intValue() == REPOSITORY_TYPE.PRIVATE) {
			// filter the filters by person only if the repository is private
			filterPerson = personID;
		}
		List<ILabelBean> leafLabelBeans = leafFacade.getRootObjects(repository,
				projectID, filterPerson, locale);
		return leafFacade.filterFromIssueNavigator(leafLabelBeans, true);
	}

	/**
	 * Add the subcategory nodes to the parent category nodes for a level
	 * @param categoryNodeMap
	 * @param categoryType
	 * @param repository
	 * @param parentCategoryIDs
	 * @return the category parents for the next level
	 */
	private static List<Integer> addSubcategoriesByCategories(Map<Integer, TreeNode> categoryNodeMap,
			String categoryType, Integer repository, List<Integer> parentCategoryIDs) {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		List<ILabelBeanWithParent> subcategories = (List)categoryFacade.getByParents(parentCategoryIDs);
		List<Integer> subcategoryIDs = new LinkedList<Integer>();
		for (ILabelBeanWithParent categoryBean : subcategories) {
			Integer categoryID = categoryBean.getObjectID();
			Integer parentCategoryID = categoryBean.getParentID();
			TreeNode childNode = categoryFacade.getSimpleNode(categoryType, repository, categoryBean);
			childNode.setSelectable(false);
			TreeNode parentTreeNode = categoryNodeMap.get(parentCategoryID);
			if (parentTreeNode!=null) {
				List<TreeNode> childrenNodes = parentTreeNode.getChildren();
				if (childrenNodes==null) {
					//reset leaf to false once a child is found
					parentTreeNode.setLeaf(Boolean.FALSE);
					childrenNodes = new LinkedList<TreeNode>();
					parentTreeNode.setChildren(childrenNodes);
				}
				childrenNodes.add(childNode);
				categoryNodeMap.put(categoryID, childNode);
			}
			subcategoryIDs.add(categoryID);
		}
		return subcategoryIDs;
	}

	/**
	 * Get the leaf nodes by categories and add them to the corresponding category nodes
	 * @param categoryNodeMap
	 * @param categoryType
	 * @param parentCategoryIDs
	 */
	private static void addLeafsByCategories(Map<Integer, TreeNode> categoryNodeMap,
			String categoryType, List<Integer> parentCategoryIDs, boolean useChecked, Set<Integer> selectedIDSet) {
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		List<ILabelBeanWithCategory> leafs = (List)leafFacade.getByParents(parentCategoryIDs);
		leafs = (List)leafFacade.filterFromIssueNavigator((List)leafs, true);
		for (ILabelBeanWithCategory leafBean : leafs) {
			Integer categoryID = leafBean.getCategoryKey();
			TreeNode childNode = getSimpleNode(leafBean, leafFacade, useChecked, selectedIDSet);
			TreeNode parentTreeNode = categoryNodeMap.get(categoryID);
			if (parentTreeNode!=null) {
				List<TreeNode> childrenNodes = parentTreeNode.getChildren();
				if (childrenNodes==null) {
					//reset leaf to false once a child is found
					parentTreeNode.setLeaf(Boolean.FALSE);
					childrenNodes = new LinkedList<TreeNode>();
					parentTreeNode.setChildren(childrenNodes);
				}
				childrenNodes.add(childNode);
			}
		}
	}
}
