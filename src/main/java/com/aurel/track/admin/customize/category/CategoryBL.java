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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.DELETE_ERROR_CODES;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

public class CategoryBL {
	
	private static final Logger LOGGER = LogManager.getLogger(CategoryBL.class);	
	
	/**
	 * the categoryType constants
	 */
	public static interface CATEGORY_TYPE {
		public static final String ISSUE_FILTER_CATEGORY = "issueFilter";
		public static final String NOTIFICATION_FILTER_CATEGORY = "notifyFilter";
		public static final String REPORT_CATEGORY = "report";
	}
	
	/**
	 * The repository type
	 * @author Tamas
	 *
	 */
	public static interface REPOSITORY_TYPE {
		public static final int PRIVATE = 1;
		public static final int PUBLIC = 2;
		public static final int PROJECT = 3;
	}
	
	public interface TYPE {
		//project folder
		public static final int PROJECT = 0;
		//user definable category
		public static final int CATEGORY = 1;
		//leaf (report or filter) depending on category type
		public static final int LEAF = 2;
	}
	
	static String getLocaleKeyByCategoryKey(String categorykey) {
		if (CATEGORY_TYPE.ISSUE_FILTER_CATEGORY.equals(categorykey)) {
			return "admin.customize.queryFilter.lbl.issueFilter";
		}
		if (CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY.equals(categorykey)) {
			return "admin.customize.automail.filter.lblAlone";
		}
		if (CATEGORY_TYPE.REPORT_CATEGORY.equals(categorykey)) {
			return "admin.customize.reportTemplate.lbl";
		}
		return null;
	}
	
	/**
	 * Get the children of a node
	 * @param nodeID
	 * @param locale
	 * @return
	 */
	static String getLeafDetail(String nodeID, Locale locale) {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(nodeID);
		String categoryType = categoryTokens.getCategoryType();
		Integer objectID = categoryTokens.getObjectID();
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType, objectID);
		return leafFacade.getFilterExpressionString(objectID, locale);
	}
	
	/**
	 * Get the children of a node
	 * @param nodeID
	 * @param personBean
	 * @param tree children list for tree or for grid
	 * @param configuredProjectID
	 * @param excludePrivate
     * @param allIfSystemAdmin
     * @param fromIssueNavigator
	 * @param locale
	 * @return
	 */
	static List<CategoryTO> getChildren(String nodeID, 
			TPersonBean personBean, boolean tree, Integer configuredProjectID,
			boolean excludePrivate, boolean allIfSystemAdmin, boolean fromIssueNavigator, Locale locale){
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(nodeID);
		String categoryType = categoryTokens.getCategoryType();
		Integer repository = categoryTokens.getRepository();
		Integer type = categoryTokens.getType();
		Integer objectID = categoryTokens.getObjectID();
		Integer personID = personBean.getObjectID();
        Map<Integer, TSystemStateBean> projectStatusMap = GeneralUtils.createMapFromList(
                SystemStatusBL.getSystemStatesByByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE,
                        new int[]{TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE, TSystemStateBean.STATEFLAGS.CLOSED}));
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		if (repository==null) {
			//get repository nodes
			return getFirstLevelNodes(personBean, locale, categoryType, configuredProjectID, excludePrivate);
		} else {
			if (type==null) {
				//repository node
				if (REPOSITORY_TYPE.PROJECT==repository.intValue()) {
					//project nodes	
					List<TProjectBean> readableMainProjectList = null;
					List<TProjectBean> adminMainProjectsList;
					if (configuredProjectID==null) {
						//all accessible projects
						adminMainProjectsList = ProjectBL.getActiveInactiveFirstLevelProjectsByProjectAdmin(personBean, allIfSystemAdmin);
						readableMainProjectList = ProjectBL.getActiveInactiveFirstLevelProjectsByRights(personBean, allIfSystemAdmin,
								new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN,
									AccessBeans.AccessFlagIndexes.READANYTASK});
					} else {
						//automail filter: project is specified
						adminMainProjectsList = new LinkedList<TProjectBean>();
						TProjectBean projectBean = LookupContainer.getProjectBean(configuredProjectID);
						if (projectBean!=null) {
							adminMainProjectsList.add(projectBean);
							readableMainProjectList = adminMainProjectsList;
						}
					}
					Set<Integer> adminProjectIDs = GeneralUtils.createIntegerSetFromBeanList(adminMainProjectsList);
					return getProjectNodes(readableMainProjectList, adminProjectIDs, projectStatusMap, categoryType, repository, tree, locale);
					
				} else {
					//get the first level categories and reports/filters without a category for public and private repository
					boolean modifiable = isModifiable(categoryFacade, categoryType, repository, type, objectID, false, personBean);
					return getRootCategoriesAndLeafs(categoryType,
							repository, null, personID, modifiable, tree, fromIssueNavigator, locale);
				}
			} else {
				if (Integer.valueOf(TYPE.PROJECT).equals(type)) {
					//get the first level categories and reports/filters without a category for a project
					Integer projectID = objectID;
					List<CategoryTO> childNodes = new LinkedList<CategoryTO>();
					if (configuredProjectID==null) {
						//get the subprojects only when we are not in a project specific category config 
						//the readable ones are get without verifying the rights: all subprojects are readable
						//because each have a readable ancestor
						List<TProjectBean> readableSubprojectList = ProjectBL.loadActiveInactiveSubrojects(projectID);
						//get the admin subprojects: either implicit (has an admin ancestor) or explicit admin subproject
						if (readableSubprojectList!=null && !readableSubprojectList.isEmpty()) {
							List<TProjectBean> adminSubprojectList;
							Integer parentProject = projectID;
							boolean adminAncestorFound = false;
							while (parentProject!=null) {
								TProjectBean projectBean = LookupContainer.getProjectBean(parentProject);
								boolean parentIsAdmin = PersonBL.isProjectAdmin(personID, parentProject);
								if (parentIsAdmin) {
									adminAncestorFound = true;
									break;
								} else {
									parentProject = projectBean.getParent();
								}
							}
							if (adminAncestorFound) {
								//implicit project admin
								adminSubprojectList = readableSubprojectList;
							} else {
								//explicit project admin
								adminSubprojectList = ProjectBL.loadNotClosedAdminSubprojects(personID, projectID);
							}
							Set<Integer> adminProjectIDs = GeneralUtils.createIntegerSetFromBeanList(adminSubprojectList);
							childNodes = getProjectNodes(readableSubprojectList, adminProjectIDs, projectStatusMap, categoryType, repository, tree, locale);
						}
					}
					boolean modifiable = isModifiable(categoryFacade, categoryType, repository, type, objectID, false, personBean);
					childNodes.addAll(getRootCategoriesAndLeafs(categoryType,
							repository, projectID, personID, modifiable, tree, fromIssueNavigator, locale));
					return childNodes;
				} else {
					boolean modifiable = isModifiable(categoryFacade, categoryType, repository, type, objectID, false, personBean);
					//children of this category: subcategories and either filters or reports
					if (Integer.valueOf(TYPE.CATEGORY).equals(type)) {
						return getChildrenByCategory(categoryType, repository, objectID,
								personID, modifiable, tree, fromIssueNavigator, locale);
					} else {
						if (CATEGORY_TYPE.REPORT_CATEGORY.equals(categoryType)) {
							return getLeaf(categoryType, repository, objectID, personID, modifiable, tree, fromIssueNavigator, locale);
						}
					}
				}
			}
		}
		return new LinkedList<CategoryTO>();
	}
	
	/**
	 * Get the report repositories 
	 * @param personBean
	 * @param locale
	 * @param categoryType
	 * @return
	 */
	private static List<CategoryTO> getFirstLevelNodes(TPersonBean personBean, 
			Locale locale, String categoryType, Integer projectID, boolean excludePrivate) {
		List<CategoryTO> rootNodes = new LinkedList<CategoryTO>();
		if (projectID==null) {
			//not in project settings
			//by automail settings in project configuration do not show the private notification filters
			if (!excludePrivate) {
				CategoryNodeTO privateRepositoryNode = new CategoryNodeTO(
						CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PRIVATE),  categoryType, 
						LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.private", locale), true, false, true);
				privateRepositoryNode.setLevel(0);
				rootNodes.add(privateRepositoryNode);
			}
			CategoryNodeTO projectRepositoryNode = new CategoryNodeTO(
					CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PROJECT), categoryType, 
					LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.project", locale), false, false, true);
			projectRepositoryNode.setLevel(0);
			rootNodes.add(projectRepositoryNode);
			CategoryNodeTO publicRepositoryNode = new CategoryNodeTO(
					CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PUBLIC), categoryType, 
					LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.public", locale), personBean.isSys(), false, true);
			publicRepositoryNode.setLevel(0);
			rootNodes.add(publicRepositoryNode);
		} else {
			//in project settings
			if (CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY.equals(categoryType)) {
				//default automail settings for project: show both project (only for selected project) and public filters
				//(also public because they could also be selected for default automail settings for project)
				CategoryNodeTO projectRepositoryNode = new CategoryNodeTO(
						CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PROJECT), categoryType, 
						LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.project", locale), false, false, true);							
				rootNodes.add(projectRepositoryNode);
				CategoryNodeTO publicRepositoryNode = new CategoryNodeTO(
						CategoryTokens.encodeRootNode(categoryType, REPOSITORY_TYPE.PUBLIC), categoryType, 
						LocalizeUtil.getLocalizedTextFromApplicationResources("common.opt.repositoryType.public", locale), personBean.isSys(), false, true);										
				rootNodes.add(publicRepositoryNode);
			}
		}
		return rootNodes;
	}	
	
	/**
	 * Get the nodeID of the (not visible) root node for project specific issueFilters/reports 
	 * @param projectID
	 * @param categoryType
	 * @return
	 */
	public static String getProjectBranchNodeID(Integer projectID, String categoryType) {
		CategoryTokens projectFilterCategoryTokens = new CategoryTokens(categoryType, 
				REPOSITORY_TYPE.PROJECT, Integer.valueOf(TYPE.PROJECT), projectID);
		return CategoryTokens.encodeNode(projectFilterCategoryTokens);	
	}
	
	/**
	 * Gets the project nodes
	 * @param readableProjectList
	 * @param adminProjectIDs
	 * @param categoryType
	 * @param repository
	 * @param tree
	 * @param locale
	 * @return
	 */
	private static List<CategoryTO> getProjectNodes(List<TProjectBean> readableProjectList, Set<Integer> adminProjectIDs, Map<Integer, TSystemStateBean> projectStatusMap,
			String categoryType, Integer repository,  boolean tree, Locale locale) {
		List<CategoryTO> projectNodes = new ArrayList<CategoryTO>();
		String typeLabel = null;
		if (!tree) {
			typeLabel = FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale);
		}
		if (readableProjectList!=null) {
			for (TProjectBean projectBean : readableProjectList) {
				Integer projectID = projectBean.getObjectID();
				CategoryTokens projectCategoryTokens = new CategoryTokens(categoryType, 
						repository, Integer.valueOf(TYPE.PROJECT), projectID);
				CategoryTO categoryTO = null;
				if (tree) {
					categoryTO = new CategoryNodeTO(
							CategoryTokens.encodeNode(projectCategoryTokens), categoryType,
							projectBean.getLabel(), false, adminProjectIDs.contains(projectID), false, false);
					((CategoryNodeTO)categoryTO).setIconCls(ProjectBL.getProjectIcon(ProjectBL.getProjectStateFlag(projectStatusMap, projectBean.getStatus())));
				} else {
					categoryTO = new CategoryGridRowTO(CategoryTokens.encodeNode(projectCategoryTokens),
							categoryType, projectBean.getLabel(), typeLabel, false, false);
					categoryTO.setIconCls(ProjectBL.getProjectIcon(ProjectBL.getProjectStateFlag(projectStatusMap, projectBean.getStatus())));
				}
				//project node is not modifiable
				categoryTO.setReadOnly(true);
				projectNodes.add(categoryTO);
			}
		}
		return projectNodes;
	}
	
	/**
	 * Get the children for root nodes
	 * @param categoryType
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @param modifiable
	 * @param tree
	 * @param fromIssueNavigator
	 * @param locale
	 * @return
	 */
	private static List<CategoryTO> getRootCategoriesAndLeafs(String categoryType, Integer repository, Integer projectID,
			Integer personID, boolean modifiable, boolean tree, boolean fromIssueNavigator, Locale locale) {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		List<CategoryTO> nodes = new LinkedList<CategoryTO>();
		Integer filterPerson = null;
		if (repository.intValue()==REPOSITORY_TYPE.PRIVATE) {
			//filter the filters by person only if the repository is private
			filterPerson = personID;
		}
		List<ILabelBean> rootCategories = categoryFacade.getRootObjects(repository, projectID, filterPerson, locale);
		//first the categories in alphabetical order
		categoryFacade.addCategories(categoryType, repository,
				rootCategories, modifiable, tree, locale, nodes);
		//then the filters in alphabetical order
		List<ILabelBean> filtersWithoutCategory = leafFacade.getRootObjects(repository, projectID, filterPerson, locale);
		leafFacade.filterFromIssueNavigator(filtersWithoutCategory, fromIssueNavigator);
		//MenuitemQuery is person specific use the logged in person (not the filterPerson)
		leafFacade.addLeafs(repository, filtersWithoutCategory, personID, modifiable, tree, projectID, locale, nodes);
		return nodes;
	}
	
	/**
	 * Get the children for a category: subcategories and leafs
	 * @param repository
	 * @param categoryID
	 * @param personID
	 * @param modifiable
	 * @param tree whether to load CategoryNodeTO or CategoryGridRow objects
	 * @param locale
	 * @return
	 */
	private static List<CategoryTO> getChildrenByCategory(String categoryType, Integer repository,
			Integer categoryID, Integer personID, boolean modifiable,
			boolean tree, boolean fromIssueNavigator, Locale locale) {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		List<CategoryTO> nodes = new LinkedList<CategoryTO>();
		List<ILabelBean> subcategories = categoryFacade.getByParent(categoryID, locale);
		//first the categories in alphabetical order
		categoryFacade.addCategories(categoryType, repository,
				subcategories, modifiable, tree, locale, nodes);
		//then the filters in alphabetical order
		List<ILabelBean> leafsForCategory = leafFacade.getByParent(categoryID, locale);
		leafFacade.filterFromIssueNavigator(leafsForCategory, fromIssueNavigator);
		leafFacade.addLeafs(repository, leafsForCategory, personID, modifiable, tree,
				getProjectID(categoryType, repository, TYPE.CATEGORY, categoryID), locale, nodes);
		return nodes;
	}
	
	private static List<CategoryTO> getLeaf(String categoryType, Integer repository,
			Integer leafID, Integer personID, boolean modifiable,
			boolean tree, boolean fromIssueNavigator, Locale locale) {
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		List<CategoryTO> nodes = new LinkedList<CategoryTO>();
		ILabelBean leafObject = leafFacade.getByKey(leafID, locale);
		List<ILabelBean> leafsForCategory = new LinkedList<ILabelBean>();
		leafsForCategory.add(leafObject);
		leafFacade.filterFromIssueNavigator(leafsForCategory, fromIssueNavigator);
		leafFacade.addLeafs(repository, leafsForCategory, personID, modifiable, tree,
				getProjectID(categoryType, repository, TYPE.LEAF, leafID), locale, nodes);
		return nodes;
	}
	/**
	 * Whether the filter is modifiable
	 * @param categoryBaseFacade
	 * @param node
	 * @param add
	 * @param personBean
	 * @return
	 */
	public static boolean isModifiable(CategoryBaseFacade categoryBaseFacade,
			String categoryType, Integer repository, Integer type, Integer objectID, boolean add, TPersonBean personBean) {
		if (add) {
			return true;
		}
		Integer projectID = CategoryBL.getProjectID(categoryType, repository, type, objectID);
		return categoryBaseFacade.isModifiable(repository, objectID, projectID, personBean);
		
	}
	
	/**
	 * The main repository nodes and projects are read only independently of any rights  
	 * @param categoryBaseFacade
	 * @param node
	 * @param add
	 * @return
	 */
	public static boolean isReadOnly(String node, boolean add) {
			if (add) {
				return false;
			}
			CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
			Integer type = categoryTokens.getType();
			if (type==null || type.intValue()==TYPE.PROJECT) {
				return true;
			}
			return false;
	}
	
	/**
	 * Adds the nodes hierarchically
	 * @param categoryType
	 * @param repository
	 * @param rootCategories
	 * @param rootLeafs
	 * @param treeNodeMap
	 */
	public static Integer hasPredefinedFilter(String categoryType, Integer categoryID) {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		List<Integer> parentCategoryIDs = new LinkedList<Integer>();
		parentCategoryIDs.add(categoryID);
		while (parentCategoryIDs!=null && !parentCategoryIDs.isEmpty()) {
			List<ILabelBean> leafs = (List)leafFacade.getByParents(parentCategoryIDs);
			for (ILabelBean leafBean : leafs) {
				if (FilterBL.filterIsPredefined(leafBean.getObjectID())) {
					return leafBean.getObjectID();
				}
			}
			List<ILabelBean> parentCategoryBeans = (List)categoryFacade.getByParents(parentCategoryIDs);
			parentCategoryIDs = GeneralUtils.createIntegerListFromBeanList(parentCategoryBeans);
		}
		return null;
	}
	
	
	/**
	 * Get the projectID for a node by edit
	 * @param categoryType
	 * @param repository
	 * @param objectID
	 * @return
	 */
	public static Integer getProjectID(String categoryType, Integer repository, Integer type, Integer objectID) {
		//edit: a categoryID or a leafID depending on leaf
		if (repository!=null && repository.intValue()==REPOSITORY_TYPE.PROJECT) {
			ILabelBean labelBean = null;
			if (type!=null) {
			switch (type.intValue()) {
				case TYPE.PROJECT:
					return objectID;
				case TYPE.LEAF:
					LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
					labelBean = leafFacade.getByKey(objectID);
					if (labelBean!=null) {
						return leafFacade.getProjectID(labelBean);
					}
					break;
				case TYPE.CATEGORY:
					if (type.intValue()==TYPE.CATEGORY) {
						CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
						labelBean = categoryFacade.getByKey(objectID);
						if (labelBean!=null) {
							return categoryFacade.getProjectID(labelBean);
						}
					}
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * Saves a new or existing custom list or system or custom list entry
	 * @param nodeID
	 * @param label
	 * @param add
	 * @param personID
	 */
	static String save(String nodeID, String label, boolean add,
			Integer personID) {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(nodeID);
		String categoryType = categoryTokens.getCategoryType();
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		ILabelBean labelBean = categoryFacade.prepareBeanAfterAddEdit(categoryTokens, label, personID, add);		
		Integer categoryID = categoryFacade.save(labelBean);
		categoryTokens.setType(CategoryBL.TYPE.CATEGORY);	
		categoryTokens.setObjectID(categoryID);
		return CategoryJSON.createSaveResultJSON(true, CategoryTokens.encodeNode(categoryTokens), categoryID);
	}
	
	/**
	 * Copies a filter category with the entire subtree
	 * @param nodeFrom
	 * @param nodeTo
	 * @param personID
     * @param isCopy
	 * @param locale
	 * @return
	 */
	public static String copy(String nodeFrom, String nodeTo, 
			Integer personID, boolean isCopy, Locale locale){
		CategoryTokens categoryTokenFrom = CategoryTokens.decodeNode(nodeFrom);
		CategoryTokens categoryTokenTo = CategoryTokens.decodeNode(nodeTo);
		String categoryType = categoryTokenFrom.getCategoryType();
		Integer repositoryFrom = categoryTokenFrom.getRepository();
		Integer repositoryTo = categoryTokenTo.getRepository();
		Integer typeFrom = categoryTokenFrom.getType();
		Integer typeTo = categoryTokenTo.getType();
		Integer objectIDFrom = categoryTokenFrom.getObjectID();
		Integer objectIDTo = categoryTokenTo.getObjectID();
		Integer objectID = null;
		try {
			objectID = copy(categoryType, typeFrom, typeTo, repositoryFrom, repositoryTo, objectIDFrom, objectIDTo, personID, isCopy, locale, new HashMap<Integer, Integer>());
		} catch (CopyInDescendantException e) {			
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return CategoryJSON.createNodeResultJSON(false, nodeFrom,
					LocalizeUtil.getLocalizedTextFromApplicationResources("common.err.copyToDescendent", locale));
		}
		categoryTokenFrom.setObjectID(objectID);
		categoryTokenFrom.setRepository(repositoryTo);
		return CategoryJSON.createSaveResultJSON(true, CategoryTokens.encodeNode(categoryTokenFrom), objectID);
	}
	
	/**
	 * Copies a filter category with the entire subtree
	 * @param categoryType
	 * @param typeFrom
     * @param typeTo
     * @param repositoryFrom
     * @param repositoryTo
     * @param objectIDFrom
     * @param objectIDTo
	 * @param personID
     * @param isCopy
	 * @param locale
     * @param oldToNewLeafIDs
	 * @return
	 */
	public static Integer copy(String categoryType, Integer typeFrom, Integer typeTo,
			Integer repositoryFrom, Integer repositoryTo, Integer objectIDFrom, Integer objectIDTo,
			Integer personID, boolean isCopy, Locale locale, Map<Integer, Integer> oldToNewLeafIDs) throws CopyInDescendantException {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		ILabelBean labelBeanTo = null;
		Integer categoryTo = null;
		Integer objectID = null;
		Integer projectTo = null;
		if (typeTo!=null) {
			switch (typeTo.intValue()) {
			case CategoryBL.TYPE.PROJECT:
				projectTo = objectIDTo;
				break;
			case CategoryBL.TYPE.CATEGORY:
				labelBeanTo = categoryFacade.getByKey(objectIDTo);
				projectTo = categoryFacade.getProjectID(labelBeanTo);
				categoryTo = objectIDTo;
				break;
			case CategoryBL.TYPE.LEAF:
				labelBeanTo = leafFacade.getByKey(objectIDTo);
				projectTo = leafFacade.getProjectID(labelBeanTo);
				categoryTo = leafFacade.getParentID(labelBeanTo);
				break;
			}
		}
		if (typeFrom!=null) {
			ILabelBean labelBeanFrom = null;
			switch (typeFrom.intValue()) {
			case CategoryBL.TYPE.LEAF:
				labelBeanFrom = leafFacade.getByKey(objectIDFrom);
				objectID = leafFacade.saveBeanAfterCutCopy(labelBeanFrom, repositoryTo,
						projectTo, categoryTo, personID, locale, true, isCopy);
				oldToNewLeafIDs.put(labelBeanFrom.getObjectID(), objectID);
				break;
			case CategoryBL.TYPE.CATEGORY:
				labelBeanFrom = categoryFacade.getByKey(objectIDFrom);
				//Integer categoryFrom = objectIDFrom;
				if (categoryTo!=null) {
					//if not repository node but category
					if (categoryFacade.isAncestor(objectIDFrom, categoryTo)) {
						throw new CopyInDescendantException();
						/*return CategoryJSON.createNodeResultJSON(false, nodeFrom, 
								LocalizeUtil.getLocalizedTextFromApplicationResources("common.err.copyToDescendent", locale));*/
					}
				}
				objectID = categoryFacade.copySubtree(labelBeanFrom, repositoryFrom, repositoryTo,
						projectTo, categoryTo, personID, locale, true, isCopy, leafFacade, oldToNewLeafIDs);
				break;
			}
		}
		return objectID;
	}
	
	/**
	 * Deletes a category or leaf
	 * @param node
	 * @param deleteConfirmed
	 * @param personBean
	 * @param locale
	 * @param servletResponse
	 */
	public static void delete(String node, boolean deleteConfirmed, TPersonBean personBean, 
			Locale locale, HttpServletResponse servletResponse) {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		Integer type = categoryTokens.getType();
		String categoryType = categoryTokens.getCategoryType();
		Integer objectID = categoryTokens.getObjectID();
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		CategoryBaseFacade categoryBaseFacade = null;
		boolean leafReplaceNeeded = false;
		if (type.equals(Integer.valueOf(TYPE.LEAF))) {
			categoryBaseFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
			leafReplaceNeeded = leafFacade.replaceNeeded(objectID);
			
			if (FilterBL.filterIsPredefined(objectID)) {
				String localizedLabel = LocalizeUtil.getLocalizedEntity(
						LocalizationKeyPrefixes.FILTER_LABEL_PREFIX, objectID, locale);
				String errorMessage = LocalizeUtil.getParametrizedString("admin.customize.queryFilter.deletePredefinedFilter", 
						new Object[] {localizedLabel}, locale);
				JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(
						errorMessage, JSONUtility.DELETE_ERROR_CODES.NO_RIGHT_TO_DELETE));
				return;
			}
		}
		boolean categoryReplaceNeeded = false;
		boolean categoryHasDependentData = false;
		if (type.equals(Integer.valueOf(TYPE.CATEGORY))) {
			categoryReplaceNeeded = categoryFacade.replaceNeeded(objectID);
			categoryHasDependentData = categoryFacade.hasDependentData(objectID);
			categoryBaseFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
			Integer predefinedFilter = CategoryBL.hasPredefinedFilter(categoryType, objectID);
			if (predefinedFilter!=null) {
				String localizedLabel = LocalizeUtil.getLocalizedEntity(
						LocalizationKeyPrefixes.FILTER_LABEL_PREFIX, predefinedFilter, locale);
				String errorMessage = LocalizeUtil.getParametrizedString("admin.customize.queryFilter.deleteCategoryWithPredefinedFilter", 
						new Object[] {localizedLabel}, locale);
				JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(
						errorMessage, JSONUtility.DELETE_ERROR_CODES.NO_RIGHT_TO_DELETE));
				return;
			}
		}
		//whether the user has delete right on this node
		if (!CategoryBL.isModifiable(categoryBaseFacade, categoryType, categoryTokens.getRepository(), type, objectID, false, personBean)) {
			String errorMessage = LocalizeUtil.getParametrizedString("common.lbl.noDeleteRight", 
					new Object[] {getLocaleKeyByCategoryKey(categoryType)}, locale);
			JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(
					errorMessage, JSONUtility.DELETE_ERROR_CODES.NO_RIGHT_TO_DELETE));
			return;
		}
		
		if (deleteConfirmed || (type.equals(Integer.valueOf(TYPE.LEAF)) && !leafReplaceNeeded) || 
				(type.equals(Integer.valueOf(TYPE.CATEGORY)) && 
						!(categoryReplaceNeeded || categoryHasDependentData))) {
			//delete if it has no dependency
			if (categoryBaseFacade != null) {
				categoryBaseFacade.delete(objectID, categoryType);
			}
			JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess());
		} else {
			if (type.equals(Integer.valueOf(TYPE.LEAF))) {
				//dependent leaf: prepare replacement tree
				JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(DELETE_ERROR_CODES.NEED_REPLACE));
			} else {
				if (categoryReplaceNeeded) {
					//category has dependent leafs: prepare replacement tree
					JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(DELETE_ERROR_CODES.NEED_REPLACE));
				} else {
					//a not empty category but does not has dependent leafs: just ask for confirmation
					String errorMessage = LocalizeUtil.getParametrizedString("admin.customize.queryFilter.deleteWarning",
							new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
									"admin.customize.queryFilter.lbl.category", locale)}, locale);
					JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(errorMessage, DELETE_ERROR_CODES.NOT_EMPTY_WARNING));
				}
			}
		}
	}
	
	/**
	 * Get the next unique name in the specified context
	 * @param objectID
	 * @param label
	 * @param repository
	 * @param categoryID
	 * @param projectID
	 * @param personID
	 * @param locale
	 * @param baseFacade
	 * @return
	 */
	public static String getNextUniqueName(Integer objectID, String label, 
			Integer repository, Integer categoryID, Integer projectID, 
			Integer personID, Integer subtype, Locale locale, CategoryBaseFacade baseFacade) {
		String labelToReturn = "";
		if (!isUniqueName(objectID, label, repository, categoryID, projectID, personID, subtype, baseFacade)) {
			String copyName = LocalizeUtil.getParametrizedString("common.lbl.copy", new Object[] {label}, locale);
			int i=0;
			while (!isUniqueName(objectID, copyName, repository, categoryID, projectID, personID, subtype, baseFacade)) {
				copyName = LocalizeUtil.getParametrizedString("common.lbl.copyWithNumber", 
					new Object[] {Integer.valueOf(++i), label}, locale);
			}
			labelToReturn = copyName;
		}
		return labelToReturn;
	}
	
	/**
	 * Whether a name is unique in the specified context
	 * @param objectID
	 * @param label
	 * @param repository
	 * @param categoryID
	 * @param projectID
	 * @param personID
	 * @param loaderResourceBundleMessages
	 * @param baseFacade
	 * @return
	 */
	private static boolean isUniqueName(Integer objectID, String label,
			Integer repository, Integer categoryID, Integer projectID,
			Integer personID, Integer subtype, CategoryBaseFacade baseFacade) {
		boolean retValue = false;
		List<ILabelBean> sameNameList = baseFacade.getListByLabel(
				repository, categoryID, projectID, personID, subtype, label);
		if (sameNameList==null || sameNameList.isEmpty()) {
			retValue =  true;
		} else {
			if (sameNameList.size()>1 || objectID==null || (objectID!=null && !sameNameList.get(0).getObjectID().equals(objectID))) {
				retValue = false;
			} else {
				retValue = true;
			}
		}
		return retValue;
	}
	
	/**
	 * Gets the replacement string for delete
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String getReplacementString(String node, String errorMessage, TPersonBean personBean, Locale locale) {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
        Integer repository = categoryTokens.getRepository();
        boolean excludePrivate = false;
        if (repository!=null && repository.intValue()!=REPOSITORY_TYPE.PRIVATE) {
            //replacing a non-private only with a non-private: mostly correct although by user specific automail assignments the privat notification filters should not be excluded
            excludePrivate = true;
        }
		Integer type = categoryTokens.getType();
		Integer objectID = categoryTokens.getObjectID();

		String filterName = null;
		String replacementWarningLabel = null;
		String replacementFieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.filter.lblReplace", locale);
		CategoryBaseFacade categoryBaseFacade;
		if (type.equals(Integer.valueOf(TYPE.LEAF))) {
			categoryBaseFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
			replacementWarningLabel = replacementFieldLabel;
		} else {
			categoryBaseFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
			replacementWarningLabel = LocalizeUtil.getParametrizedString("common.lbl.below", new Object[] { replacementFieldLabel,
						LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.lbl.category", locale),
						}, locale);
		}
		ILabelBean labelBean = categoryBaseFacade.getByKey(objectID);
        Integer projectID = null;
        if (repository!=null && repository.intValue()==REPOSITORY_TYPE.PROJECT) {
            projectID =  categoryBaseFacade.getProjectID(labelBean);
        }
		if (labelBean!=null) {
			filterName = labelBean.getLabel();
		}
        List<TreeNode> categoryTree =  CategoryPickerBL.getPickerNodesEager(
                personBean, excludePrivate, false, null, true, projectID, objectID, locale, categoryType);
		return JSONUtility.createReplacementTreeJSON(true, filterName, replacementWarningLabel, replacementFieldLabel,
                JSONUtility.getTreeHierarchyJSON(categoryTree, false, true), errorMessage, locale);
	}
	
	/**
	 * Replaces and deletes a node
	 * @param node
	 * @param replacementID
	 */
	static void replaceAndDelete(String node, Integer replacementID) {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
		Integer type = categoryTokens.getType();
		Integer objectID = categoryTokens.getObjectID();
		if (type.equals(Integer.valueOf(TYPE.LEAF))) {
			LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
			leafFacade.replace(objectID, replacementID);
		} else {
			CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
			categoryFacade.replace(objectID, replacementID);
		}
	}
}
