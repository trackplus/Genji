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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.Date;
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
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;

import com.aurel.track.GeneralSettings;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.execute.ExecuteMatcherBL;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO.ARCHIVED_FILTER;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO.DELETED_FILTER;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.consInf.RaciRole;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.lucene.search.LuceneSearcher;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.GeneralUtils;

/**
 * Loads the tree filter items
 * @author Tamas
 *
 */
public class LoadTreeFilterItems {
	private static final Logger LOGGER = LogManager.getLogger(LoadTreeFilterItems.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	/**
	 * Loads the custom filter workItem beans
	 * @param filterUpperTO
	 * @param qNode
	 * @param personID
	 * @param locale
	 * @param withCustomAttributes
	 * @param projectID
	 * @param entityFlag
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<TWorkItemBean> getTreeFilterWorkItemBeans(FilterUpperTO filterUpperTO, QNode qNode, Integer filterID, TPersonBean personBean,
			Locale locale, boolean withCustomAttributes, Date startDate, Date endDate) throws TooManyItemsToLoadException {
		List<TAttributeValueBean> attributeValueBeanList = new LinkedList<TAttributeValueBean>();
		RACIBean raciBean = TreeFilterExecuterFacade.getRACIBean(filterID, personBean.getObjectID(), filterUpperTO);
		return prepareTreeFilterItems(filterUpperTO, qNode, raciBean, personBean, locale, false,
				startDate, endDate, withCustomAttributes, attributeValueBeanList, false, null, false,
				null, false, null,  false, null, false, null, false, null, false, null, false, null);
	}

	/**
	 * Loads the custom filter workItem beans
	 * @param filterUpperTO
	 * @param personID
	 * @param locale
	 * @param withCustomAttributes
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<TWorkItemBean> getTreeFilterWorkItemBeans(FilterUpperTO filterUpperTO, 
			TPersonBean personBean, Locale locale, boolean withCustomAttributes) throws TooManyItemsToLoadException {
		return getTreeFilterWorkItemBeans(filterUpperTO,  null, null, personBean, locale, withCustomAttributes, null, null);
	}
	
	/**
	 * Gets the custom filter reportBeans without parentSet but all other entities
	 * WARNING: use only if the project is explicitly set and the matcherContext is surely not needed (simple instant filters)
	 * Otherwise execute trough TreeFilterExecuterFacade.getInstantTreeFilterReportBeanList() which sets the projects if not set and initializes the matcherContext
	 * @param filterUpperTO
	 * @param qNode
	 * @param filterID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getTreeFilterReportBeans(FilterUpperTO filterUpperTO, QNode qNode, Integer filterID,
			TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		return getTreeFilterReportBeans(filterUpperTO, qNode, filterID, true, personBean, locale, true, true, true, true, true, true, true, true, false);
	}
	
	/**
	 * Gets the custom filter reportBeans with all entities
	 *  WARNING: use only if the project is explicitly set and the matcherContext is surely not needed (simple instant filters)
	 * Otherwise execute trough TreeFilterExecuterFacade.getInstantTreeFilterReportBeanList() which sets the projects if not set and initializes the matcherContext
	 * @param filterUpperTO
	 * @param qNode
	 * @param personID
	 * @param locale
	 * @param projectID
	 * @param entityFlag
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getTreeFilterReportBeansForReport(FilterUpperTO filterUpperTO, QNode qNode, Integer filterID,
			TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		return getTreeFilterReportBeans(filterUpperTO, qNode, filterID, true, personBean, locale, true, true, true, true, true, true, true, true, true);
	}
	
	/**
	 *  Gets the custom filter reportBeans
	 * @param filterUpperTO
	 * @param qNode
	 * @param personID
	 * @param locale
	 * @param projectID
	 * @param entityFlag
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getTreeFilterReportBeans(FilterUpperTO filterUpperTO, QNode qNode, Integer filterID, boolean editFlagNeeded,
			TPersonBean personBean, Locale locale,
			boolean withCustomAttributes, boolean withWatchers,
			boolean withMyExpenses, boolean withTotalExpenses,
			boolean withBudgetPlan, boolean withRemainingPlan,
			boolean withAttachment, boolean withLinks, boolean withParents) throws TooManyItemsToLoadException {
		Integer personID = personBean.getObjectID();
		List<TAttributeValueBean> attributeValueBeanList = new LinkedList<TAttributeValueBean>();
		List<TNotifyBean> watcherList = new LinkedList<TNotifyBean>();
		List<TComputedValuesBean> myExpenseList = new LinkedList<TComputedValuesBean>();
		List<TComputedValuesBean> totalExpenseList = new LinkedList<TComputedValuesBean>();
		List<TComputedValuesBean> budgetAndPlanList = new LinkedList<TComputedValuesBean>();
		List<TActualEstimatedBudgetBean> remainingPlanList = new LinkedList<TActualEstimatedBudgetBean>();
		List<TAttachmentBean> attachmentList = new LinkedList<TAttachmentBean>();
		List<TWorkItemLinkBean> itemLinkList = new LinkedList<TWorkItemLinkBean>();
		boolean summaryItemsBehavior = ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior();
		Set<Integer> parentIDsSet = new HashSet<Integer>();
		RACIBean raciBean = TreeFilterExecuterFacade.getRACIBean(filterID, personID, filterUpperTO);
		List<TWorkItemBean> workItemBeanList = prepareTreeFilterItems(filterUpperTO, qNode, raciBean, personBean, locale, editFlagNeeded, null, null,
			withCustomAttributes, attributeValueBeanList, withWatchers, watcherList,
			withMyExpenses, myExpenseList, withTotalExpenses, totalExpenseList, 
			withBudgetPlan, budgetAndPlanList, withRemainingPlan, remainingPlanList,
			withAttachment, attachmentList,
			withLinks, itemLinkList,
			summaryItemsBehavior && withParents, parentIDsSet);
		return ReportBeanLoader.populateReportBeans(workItemBeanList, attributeValueBeanList, personID, locale,
				watcherList, myExpenseList, totalExpenseList, budgetAndPlanList, remainingPlanList,
				attachmentList, itemLinkList, parentIDsSet);
	}

	/**
	 * Prepares the custom filter workItemBeans
	 * @param filterUpperTO filter's upper part
	 * @param qNode filter's tree part
	 * @param personID
	 * @param locale
	 * @param withCustomAttributes whether the custom attributes should be loaded
	 * @param projectID
	 * @param entityFlag
	 * @param startDate only items with start date after (if startDate specified)
	 * @param endDate only items with end date before (if endDate specified)
	 * @param attributeValueBeanList output parameter
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	private static List<TWorkItemBean> prepareTreeFilterItems(FilterUpperTO filterUpperTO, QNode qNode, RACIBean raciBean,
			TPersonBean personBean, Locale locale, boolean editFlagNeeded, Date startDate, Date endDate, 
			boolean withCustomAttributes, List<TAttributeValueBean> attributeValueBeanList,
			boolean withWatchers, List<TNotifyBean> watcherList,
			boolean withMyExpenses, List<TComputedValuesBean> myExpenseList,
			boolean withTotalExpenses, List<TComputedValuesBean> totalExpenseList,
			boolean withBudgetPlan, List<TComputedValuesBean> budgetAndPlanList,
			boolean withRemainingPlan, List<TActualEstimatedBudgetBean> remainingPlanList,
			boolean withAttachment, List<TAttachmentBean> attachmentList,
			boolean withLinks, List<TWorkItemLinkBean> itemLinkList,
			boolean withParents, Set<Integer> parentIDs) throws TooManyItemsToLoadException {
		Date start = null;
		int count = 0;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		Set<Integer> pseudoFieldsInTree = new HashSet<Integer>();
		ExecuteMatcherBL.gatherPseudoFieldsInTree(qNode, pseudoFieldsInTree);
		//the pseudo fields should be loaded if either has to be rendered or the filter contains the corresponding pseudo field expression
		withWatchers = withWatchers || pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST) ||
				pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST);
		boolean filterByMyExpenseTime = pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME);
		withMyExpenses = withMyExpenses || filterByMyExpenseTime ||
				pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST);
		boolean filterByTotalExpenseTime = pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME);
		withTotalExpenses = withTotalExpenses || filterByTotalExpenseTime ||
				pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST);
		boolean filterByBudgetPlanTime = pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME) ||
				pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME);
		withBudgetPlan = withBudgetPlan || filterByBudgetPlanTime ||
				pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST) ||	
				pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST);
		boolean filterByRemainingTime = pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME);
		withRemainingPlan = withRemainingPlan || filterByRemainingTime ||
				pseudoFieldsInTree.contains(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST);
		Integer personID = personBean.getObjectID();
		Boolean projectRoleItemsAboveLimit = personBean.getProjectRoleItemsAboveLimit();
		Boolean raciRoleItemsAboveLimit = personBean.getRaciRoleItemsAboveLimit();
		if (projectRoleItemsAboveLimit==null) {
			projectRoleItemsAboveLimit = Boolean.TRUE;
		}
		if (raciRoleItemsAboveLimit==null) {
			raciRoleItemsAboveLimit = Boolean.TRUE;
		}
		int maxItems = GeneralSettings.getMaxItems();
		boolean myItems = raciBean!=null;
		if (filterUpperTO.getMatcherContext()==null) {
			MatcherContext matcherContext = new MatcherContext();
			matcherContext.setLoggedInUser(personID);
			matcherContext.setLastLoggedDate(personBean.getLastButOneLogin());
			matcherContext.setLocale(locale);
			filterUpperTO.setMatcherContext(matcherContext);
			matcherContext.setIncludeResponsiblesThroughGroup(filterUpperTO.isIncludeResponsiblesThroughGroup());
			matcherContext.setReleaseTypeSelector(filterUpperTO.getReleaseTypeSelector());
			filterUpperTO.cleanParameters();
		}
		if (filterByMyExpenseTime || filterByTotalExpenseTime || filterByBudgetPlanTime || filterByRemainingTime) {
			filterUpperTO.getMatcherContext().setProjectAccountingMap(ProjectBL.getAccountingAttributesMap(GeneralUtils.createIntegerListFromIntegerArr(filterUpperTO.getSelectedProjects())));
		}
		Integer[] selectedProjectsOriginal = filterUpperTO.getSelectedProjects();
		if (selectedProjectsOriginal==null || selectedProjectsOriginal.length==0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("No Project(s) selected in filter");
			}
			return new LinkedList<TWorkItemBean>();
		}
		/**
		 * All items will be gathered in this list
		 */
		List<TWorkItemBean> workItemBeanList = new LinkedList<TWorkItemBean>();
		Set<Integer> foundWorkItemsIDs = new HashSet<Integer>();
		/**
		 * Separate the projects with roles from projects with RACI items
		 */
		List<Integer> meAndSubstitutedAndGroups = AccessBeans.getMeAndSubstitutedAndGroups(personID);
		//all selected projects and their descendants 
		//Integer[] selectedAndDescendantProjects = filterUpperTO.getSelectedProjects();
		//List<Integer> selectedProjectIDs = GeneralUtils.createIntegerListFromIntegerArr(selectedAndDescendantProjects);
		if (LOGGER.isDebugEnabled() && selectedProjectsOriginal!=null) {
			LOGGER.debug("Project(s) selected in filter: " + LookupContainer.getNotLocalizedLabelBeanListLabels(SystemFields.INTEGER_PROJECT,
					GeneralUtils.createSetFromIntegerArr(selectedProjectsOriginal)));
		}
		//get also the ancestor projects for getting the role based rights
		Integer[] ancestorProjects = ProjectBL.getAncestorProjects(selectedProjectsOriginal);
		int[] readAnyRights = new int[] { AccessFlagIndexes.READANYTASK, AccessFlagIndexes.PROJECTADMIN };
		Map<Integer, Set<Integer>> projectToIssueTypesWithReadRight = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(meAndSubstitutedAndGroups, ancestorProjects, readAnyRights);
		Map<Integer, Set<Integer>> projectToIssueTypesWithEditRight = null;
		if (editFlagNeeded) {
			int[] editRights = new int[] { AccessFlagIndexes.MODIFYANYTASK, AccessFlagIndexes.PROJECTADMIN };
			projectToIssueTypesWithEditRight = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(meAndSubstitutedAndGroups, ancestorProjects, editRights);
		}
		
		//gets the selected item types for getting the role based right eventually restricted by item types
		Integer[] selectedItemTypesOriginal = filterUpperTO.getSelectedIssueTypes();
		if (selectedItemTypesOriginal==null || selectedItemTypesOriginal.length==0) {
				List<TListTypeBean> selectableItemTypes = IssueTypeBL.loadAllSelectable();
				selectedItemTypesOriginal = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(selectableItemTypes));
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("No explicit item types selected. Number of non document item types included implicitly " + selectableItemTypes.size());
				}
		}
		Set<Integer> selectedItemTypesSet = new HashSet<Integer>();
		if (selectedItemTypesOriginal!=null) {
			selectedItemTypesSet = GeneralUtils.createSetFromIntegerArr(selectedItemTypesOriginal);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Item type(s) selected in filter: " + LookupContainer.getLocalizedLabelBeanListLabels(SystemFields.INTEGER_ISSUETYPE, selectedItemTypesSet, locale));
			}
		}
		List<TProjectBean> selectedAndDescendantProjectBeans = ProjectBL.loadByProjectIDs(GeneralUtils.createListFromIntArr(selectedProjectsOriginal));
		Map<Integer, Integer> childToParentProjectMap = ProjectBL.getChildToParentMap(selectedAndDescendantProjectBeans, null);
		Set<Integer> raciProjectIDs = new HashSet<Integer>();
		
		Date projectRoles;
		if (LOGGER.isDebugEnabled() && start!=null) {
			projectRoles = new Date();
			LOGGER.debug("Getting project roles lasted " + new Long(projectRoles.getTime()-start.getTime()).toString() + " ms");
		}
		Map<Integer, TWorkItemBean> notEditableMap = null;
		if (editFlagNeeded) {
			notEditableMap = new HashMap<Integer, TWorkItemBean>();
		}
		/**
		 * Get the issues for projects with role grouped by same item types
		 */
		Map<Set<Integer>, Set<Integer>> itemTypesToProjectsMap = getItemTypesToProjectsMap(selectedProjectsOriginal, selectedItemTypesSet, projectToIssueTypesWithReadRight, childToParentProjectMap, raciProjectIDs);
		for (Map.Entry<Set<Integer>, Set<Integer>> entry : itemTypesToProjectsMap.entrySet()) {
			Set<Integer> itemTypes = entry.getKey();
			Set<Integer> projectIDs = entry.getValue();
			boolean itemTypeRestrictions = !itemTypes.contains(null); 
			filterUpperTO.setSelectedProjects(GeneralUtils.createIntegerArrFromCollection(projectIDs));
			Integer[] itemTypesArr = null;
			if (itemTypeRestrictions) {
				itemTypesArr = GeneralUtils.createIntegerArrFromCollection(itemTypes);
			} else {
				itemTypesArr = selectedItemTypesOriginal;
			}
			filterUpperTO.setSelectedIssueTypes(itemTypesArr);
			if (projectRoleItemsAboveLimit.booleanValue()) {
				int projectRoleCount = LoadTreeFilterItemCounts.getItemCount(personID, filterUpperTO, raciBean);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Number of items found in projects " + 
							LookupContainer.getNotLocalizedLabelBeanListLabels(SystemFields.INTEGER_PROJECT, projectIDs) +
							" and item types " + LookupContainer.getLocalizedLabelBeanListLabels(SystemFields.INTEGER_ISSUETYPE,
									GeneralUtils.createSetFromIntegerArr(itemTypesArr), locale) + ": " + projectRoleCount);
				}
				count+=projectRoleCount;
				if (count>maxItems) {
					throw new TooManyItemsToLoadException("Too many items to load " + count, count);
				}
			}
			List<TWorkItemBean> projectRoleBeansList = getItems(personID, filterUpperTO, raciBean,
					withCustomAttributes, attributeValueBeanList,
					withWatchers, watcherList,
					withMyExpenses, myExpenseList,
					withTotalExpenses, totalExpenseList,
					withBudgetPlan, budgetAndPlanList,
					withRemainingPlan, remainingPlanList,
					withAttachment, attachmentList,
					withLinks, itemLinkList,
					withParents, parentIDs,
					foundWorkItemsIDs, locale,
					startDate, endDate, qNode);
			if (projectRoleBeansList!=null) {
				if (LOGGER.isDebugEnabled()) {
					String itemTypeString = null;
					if (itemTypeRestrictions) {
						StringBuilder stringBuilder = new StringBuilder("item type(s) ");
						stringBuilder.append(LookupContainer.getLocalizedLabelBeanListLabels(SystemFields.INTEGER_ISSUETYPE, itemTypes, locale));
						itemTypeString = stringBuilder.toString();
					} else {
						itemTypeString = "no item type restrictions ";
					}
					LOGGER.debug("Number of items from project(s) " + LookupContainer.getNotLocalizedLabelBeanListLabels(SystemFields.PROJECT, projectIDs) +
							" and " + itemTypeString + ": " + projectRoleBeansList.size());
				}
				for (TWorkItemBean workItemBean : projectRoleBeansList) {
					Integer itemID = workItemBean.getObjectID();
					foundWorkItemsIDs.add(itemID);
					if (editFlagNeeded) {
						Integer itemProjectID = workItemBean.getProjectID();
						Integer itemTypeID = workItemBean.getListTypeID();
						if (AccessBeans.hasExplicitRight(personID, itemID, itemProjectID, itemTypeID, projectToIssueTypesWithEditRight, childToParentProjectMap, "edit")) {
							workItemBean.setEditable(true);
						} else {
							//mark as not editable, but may be set to editable in a later RACI phase
							notEditableMap.put(itemID, workItemBean);
							//this is readable but not editable. Force loading the same item as from RACI project to possibly set later as editable 
							raciProjectIDs.add(itemProjectID);
						}
					}
				}
				workItemBeanList.addAll(projectRoleBeansList);
			}	
		}
		filterUpperTO.setSelectedProjects(selectedProjectsOriginal);
		filterUpperTO.setSelectedIssueTypes(selectedItemTypesOriginal);
	
		//the projects not completely covered in projects with roles: get the RACI items
		if (!raciProjectIDs.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("RACI projects selected in filter: " + LookupContainer.getNotLocalizedLabelBeanListLabels(SystemFields.INTEGER_PROJECT, raciProjectIDs));
			}
			List<Integer> meAndSubstitutedIDs = AccessBeans.getMeAndSubstituted(personBean);
			Integer[] selectedResponsibles = filterUpperTO.getSelectedResponsibles();
			Integer[] selectedManagers = filterUpperTO.getSelectedManagers();
			Integer[] selectedAuthors = filterUpperTO.getSelectedOriginators();
			boolean reponsiblesInFilter = selectedResponsibles!=null && selectedResponsibles.length>0;
			boolean managersInFilter = selectedManagers!=null && selectedManagers.length>0;
			boolean authorsInFilter = selectedAuthors!=null && selectedAuthors.length>0;
			filterUpperTO.setSelectedProjects(GeneralUtils.createIntegerArrFromCollection(raciProjectIDs));
			filterUpperTO.setSelectedIssueTypes(selectedItemTypesOriginal);
			Map<Integer, List<Integer>> reciprocGroups = AccessBeans.getRaciprocGroupsMap(meAndSubstitutedIDs);
			List<Integer> reciprocOriginatorGroups = reciprocGroups.get(SystemFields.INTEGER_ORIGINATOR);
			List<Integer> reciprocManagerGroups = reciprocGroups.get(SystemFields.INTEGER_MANAGER);
			List<Integer> reciprocResponsibleGroups = reciprocGroups.get(SystemFields.INTEGER_RESPONSIBLE);
			List<Integer> allGroupsForPerson = reciprocGroups.get(AccessBeans.ALL_PERSONGROUPS);
			if (!myItems) {
				//configure the RACIBean with own and reciproc persons only if not MY_ITEMS because MY_ITEMS refers only to me (no reciproc persons)
				raciBean = new RACIBean();
				/**
				 * prepare my author items
				 */
				Set<Integer> authorSet = new HashSet<Integer>();
				authorSet.addAll(meAndSubstitutedIDs);
				if (reciprocOriginatorGroups!=null) {
					List<TPersonBean> originatorsInReciprocGroups = PersonBL.getIndirectPersons(reciprocOriginatorGroups, false, null);
					List<Integer> reciprocOriginators = GeneralUtils.createIntegerListFromBeanList(originatorsInReciprocGroups);
					authorSet.addAll(reciprocOriginators);
				}
				raciBean.setAuthors(authorSet);
				/**
				 * prepare my responsible items
				 */
				Set<Integer> responsibleSet = new HashSet<Integer>();
				responsibleSet.addAll(meAndSubstitutedIDs);
				if (reciprocResponsibleGroups!=null) {
					List<TPersonBean> responsiblesInReciprocGroups = PersonBL.getIndirectPersons(reciprocResponsibleGroups, false, null);
					List<Integer> reciprocResponsibles = GeneralUtils.createIntegerListFromBeanList(responsiblesInReciprocGroups);
					responsibleSet.addAll(reciprocResponsibles);
				}
				if (allGroupsForPerson!=null) {
					responsibleSet.addAll(allGroupsForPerson);
				}
				raciBean.setResponsibles(responsibleSet);
				/**
				 * prepare my manager items
				 */
				Set<Integer> managerSet = new HashSet<Integer>();
				managerSet.addAll(meAndSubstitutedIDs);
				if (reciprocManagerGroups!=null) {
					List<TPersonBean> managersInReciprocGroups = PersonBL.getIndirectPersons(reciprocManagerGroups, false, null);
					List<Integer> reciprocManagers = GeneralUtils.createIntegerListFromBeanList(managersInReciprocGroups);
					managerSet.addAll(reciprocManagers);
				}
				raciBean.setManagers(managerSet);
			}
			if (reponsiblesInFilter && managersInFilter && authorsInFilter) {
				//if a RACI role (author or responsible or manager) is set in filter then this RACI role will be excluded from raciBean (see TreeFilterCriteria Torque limitation)
				//but if all three are set in filter then we loose the RACI filter (no RACI criterion is set at all) so possibly we would get item results the user has not right to see 
				//so in this case no common RACI search is executed (only the one by one for author and responsible and manager, see later)
				LOGGER.debug("All the authors, responsibles and managers are set in filter. No RACI projects are searched");
			} else {
				/**
				 * get the author, responsible and manager items
				 */
				LOGGER.debug("Getting my RACI items...");
				if (raciRoleItemsAboveLimit.booleanValue()) {
					int raciRoleCount = LoadTreeFilterItemCounts.getItemCount(personID, filterUpperTO, raciBean);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Items found as manager/responsible/author " + raciRoleCount);
					}
					count+=raciRoleCount;
					if (count>maxItems) {
						throw new TooManyItemsToLoadException("Too many items to load " + count, count);
					}
				}
				List<TWorkItemBean> respManAuthItems = getItems(personID, filterUpperTO, raciBean,
						withCustomAttributes, attributeValueBeanList,
						withWatchers, watcherList,
						withMyExpenses, myExpenseList,
						withTotalExpenses, totalExpenseList,
						withBudgetPlan, budgetAndPlanList,
						withRemainingPlan, remainingPlanList,
						withAttachment, attachmentList,
						withLinks, itemLinkList,
						withParents, parentIDs,
						foundWorkItemsIDs, locale,
						startDate, endDate, qNode);
				if (respManAuthItems!=null) {
					workItemBeanList.addAll(filterItemList(respManAuthItems, foundWorkItemsIDs, editFlagNeeded, notEditableMap));
				}
			}
			if (reponsiblesInFilter) {
				Set<Integer> selectedResponsiblesSet = GeneralUtils.createSetFromIntegerArr(selectedResponsibles);
				Set<Integer> raciResponsibles = raciBean.getResponsibles();
				raciResponsibles.retainAll(selectedResponsiblesSet);
				if (!raciResponsibles.isEmpty()) {
					LOGGER.debug("Getting my responsible items...");
					filterUpperTO.setSelectedResponsibles(GeneralUtils.createIntegerArrFromSet(raciResponsibles));
					if (raciRoleItemsAboveLimit.booleanValue()) {
						int raciRoleCount = LoadTreeFilterItemCounts.getItemCount(personID, filterUpperTO, null);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Items found as responsible " + raciRoleCount);
						}
						count+=raciRoleCount;
						if (count>maxItems) {
							throw new TooManyItemsToLoadException("Too many items to load " + count, count);
						}
					}
					List<TWorkItemBean> responsibleItems = getItems(personID, filterUpperTO, null,
							withCustomAttributes, attributeValueBeanList,
							withWatchers, watcherList,
							withMyExpenses, myExpenseList,
							withTotalExpenses, totalExpenseList,
							withBudgetPlan, budgetAndPlanList,
							withRemainingPlan, remainingPlanList,
							withAttachment, attachmentList,
							withLinks, itemLinkList,
							withParents, parentIDs,
							foundWorkItemsIDs, locale,
							startDate, endDate, qNode);
					if (responsibleItems!=null) {
						workItemBeanList.addAll(filterItemList(responsibleItems, foundWorkItemsIDs, editFlagNeeded, notEditableMap));
					}
					filterUpperTO.setSelectedResponsibles(selectedResponsibles);
				}
			}
			if (managersInFilter) {
				Set<Integer> selectedManagersSet = GeneralUtils.createSetFromIntegerArr(selectedManagers);
				Set<Integer> raciManagers = raciBean.getManagers();
				raciManagers.retainAll(selectedManagersSet);
				if (!raciManagers.isEmpty()) {
					LOGGER.debug("Getting my manager items...");
					filterUpperTO.setSelectedManagers(GeneralUtils.createIntegerArrFromSet(raciManagers));
					if (raciRoleItemsAboveLimit.booleanValue()) {
						int raciRoleCount = LoadTreeFilterItemCounts.getItemCount(personID, filterUpperTO, null);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Items found as manager " + raciRoleCount);
						}
						count+=raciRoleCount;
						if (count>maxItems) {
							throw new TooManyItemsToLoadException("Too many items to load " + count, count);
						}
					}
					List<TWorkItemBean> managerItems = getItems(personID, filterUpperTO, null,
							withCustomAttributes, attributeValueBeanList,
							withWatchers, watcherList,
							withMyExpenses, myExpenseList,
							withTotalExpenses, totalExpenseList,
							withBudgetPlan, budgetAndPlanList,
							withRemainingPlan, remainingPlanList,
							withAttachment, attachmentList,
							withLinks, itemLinkList,
							withParents, parentIDs,
							foundWorkItemsIDs, locale,
							startDate, endDate, qNode);
					if (managerItems!=null) {
						workItemBeanList.addAll(filterItemList(managerItems, foundWorkItemsIDs, editFlagNeeded, notEditableMap));
					}
					filterUpperTO.setSelectedManagers(selectedManagers);
				}
			}
			if (authorsInFilter) {
				Set<Integer> selectedAuthorsSet = GeneralUtils.createSetFromIntegerArr(selectedAuthors);
				Set<Integer> raciAuthors = raciBean.getAuthors();
				raciAuthors.retainAll(selectedAuthorsSet);
				if (!raciAuthors.isEmpty()) {
					LOGGER.debug("Getting my author items...");
					filterUpperTO.setSelectedOriginators(GeneralUtils.createIntegerArrFromSet(raciAuthors));
					if (raciRoleItemsAboveLimit.booleanValue()) {
						int raciRoleCount = LoadTreeFilterItemCounts.getItemCount(personID, filterUpperTO, null);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Items found as author " + raciRoleCount);
						}
						count+=raciRoleCount;
						if (count>maxItems) {
							throw new TooManyItemsToLoadException("Too many items to load " + count, count);
						}
					}
					List<TWorkItemBean> authorItems = getItems(personID, filterUpperTO, null,
							withCustomAttributes, attributeValueBeanList,
							withWatchers, watcherList,
							withMyExpenses, myExpenseList,
							withTotalExpenses, totalExpenseList,
							withBudgetPlan, budgetAndPlanList,
							withRemainingPlan, remainingPlanList,
							withAttachment, attachmentList,
							withLinks, itemLinkList,
							withParents, parentIDs,
							foundWorkItemsIDs, locale,
							startDate, endDate, qNode);
					if (authorItems!=null) {
						workItemBeanList.addAll(filterItemList(authorItems, foundWorkItemsIDs, editFlagNeeded, notEditableMap));
					}
					filterUpperTO.setSelectedOriginators(selectedAuthors);
				}
			}
			/**
			 * my watcher items
			 */
			//save temporarily to set it back after filter is executed for watchers
			Integer[] selectedWatchersOriginal = filterUpperTO.getSelectedConsultantsInformants();
			Integer watcherSelectorOriginal = filterUpperTO.getWatcherSelector();
			Set<Integer> watcherSet = new HashSet<Integer>();
			if (myItems) { 
				watcherSet.add(personID);
			} else {
				watcherSet.addAll(meAndSubstitutedAndGroups);
			}
			if (selectedWatchersOriginal!=null && selectedWatchersOriginal.length>0) {
				//if originator field has selection than retain only the selections related to me as author
				watcherSet.retainAll(GeneralUtils.createSetFromIntegerArr(selectedWatchersOriginal));
			} else {
				filterUpperTO.setWatcherSelector(FilterUpperTO.CONSINF_SELECTOR.CONSULTANT_OR_INFORMANT);
			}
			if (!watcherSet.isEmpty()) {
				filterUpperTO.setSelectedConsultantsInformants(GeneralUtils.createIntegerArrFromCollection(watcherSet));
				if (raciRoleItemsAboveLimit.booleanValue()) {
					int raciRoleCount = LoadTreeFilterItemCounts.getItemCount(personID, filterUpperTO, null);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Items found as watchers " + raciRoleCount);
					}
					count+=raciRoleCount;
					if (count>maxItems) {
						throw new TooManyItemsToLoadException("Too many items to load " + count, count);
					}
				}
				List<TWorkItemBean> watcherItems = getItems(personID, filterUpperTO, null,
						withCustomAttributes, attributeValueBeanList,
						withWatchers, watcherList,
						withMyExpenses, myExpenseList,
						withTotalExpenses, totalExpenseList,
						withBudgetPlan, budgetAndPlanList,
						withRemainingPlan, remainingPlanList,
						withAttachment, attachmentList,
						withLinks, itemLinkList,
						withParents, parentIDs,
						foundWorkItemsIDs, locale,
						startDate, endDate, qNode);
				//set both watchers and selector back to original value for the next filter execution
				filterUpperTO.setSelectedConsultantsInformants(selectedWatchersOriginal);
				filterUpperTO.setWatcherSelector(watcherSelectorOriginal);
				//helper for setting the consulted items as editable
				List<Integer> watcherItemIDs = new LinkedList<Integer>();
				if (watcherItems!=null) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Total number of watcher items: " + watcherItems.size());
					}
					for (Iterator<TWorkItemBean> iterator = watcherItems.iterator(); iterator.hasNext();) {
						TWorkItemBean workItemBean = iterator.next();
						Integer workItemID = workItemBean.getObjectID();
						if (foundWorkItemsIDs.contains(workItemID)) {
							iterator.remove();
							if (editFlagNeeded) {
								TWorkItemBean includedWorkItemBean = notEditableMap.get(workItemID);
								if (includedWorkItemBean!=null) {
									watcherItemIDs.add(workItemID);
								}
							}
						} else {
							foundWorkItemsIDs.add(workItemID);
							if (editFlagNeeded) {
								notEditableMap.put(workItemID, workItemBean);
								watcherItemIDs.add(workItemID);
							}
						}
					}
					if (!watcherItems.isEmpty()) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Number of watcher only items: " + watcherItems.size());
						}
						workItemBeanList.addAll(watcherItems);
					}
				}
				if (editFlagNeeded) {
					List<TNotifyBean> notifyBeanList = ConsInfBL.loadWatcherByItems(watcherItemIDs, RaciRole.CONSULTANT);
					if (notifyBeanList!=null) {
						for (TNotifyBean notifyBean : notifyBeanList) {
							Integer itemID = notifyBean.getWorkItem();
							TWorkItemBean workItemBean = notEditableMap.get(itemID);
							if (workItemBean!=null) {
								workItemBean.setEditable(true);
								notEditableMap.remove(itemID);
							}
						}
					}
				}
			}
			/**
			 * on behalf of items
			 */
			if (!myItems) {
				List<Integer> onBefalfUserPickerFiels = FieldBL.getOnBehalfOfUserPickerFieldIDs();
				if (onBefalfUserPickerFiels!=null && !onBefalfUserPickerFiels.isEmpty()) {
					Map<Integer, Integer[]> selectedCustomSelectsOriginal = filterUpperTO.getSelectedCustomSelects();
					if (selectedCustomSelectsOriginal==null) {
						selectedCustomSelectsOriginal = new HashMap<Integer, Integer[]>();
						filterUpperTO.setSelectedCustomSelects(selectedCustomSelectsOriginal);
					}
					Map<Integer, Integer> customSelectSimpleFieldsMap = filterUpperTO.getCustomSelectSimpleFields();
					if (customSelectSimpleFieldsMap==null) {
						customSelectSimpleFieldsMap = new HashMap<Integer, Integer>();
						filterUpperTO.setCustomSelectSimpleFields(customSelectSimpleFieldsMap);
					}
					for (Integer fieldID : onBefalfUserPickerFiels) {
						//get the filter result for each on behalf of user picker one by one
						Set<Integer> onBehalfSet = new HashSet<Integer>();
						onBehalfSet.addAll(meAndSubstitutedIDs);
						//save temporarily to set it back after filter is executed for on behalf of persons
						Integer[] selectedOnBehalfUserPickerOriginal = selectedCustomSelectsOriginal.get(fieldID);
						if (selectedOnBehalfUserPickerOriginal!=null && selectedOnBehalfUserPickerOriginal.length>0) {
							//if on behalf of field has selection than retain only the selections related to me as author
							onBehalfSet.retainAll(GeneralUtils.createSetFromIntegerArr(selectedOnBehalfUserPickerOriginal));
						} 
						if (!onBehalfSet.isEmpty()) {
							selectedCustomSelectsOriginal.put(fieldID, GeneralUtils.createIntegerArrFromCollection(onBehalfSet));
							IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
							customSelectSimpleFieldsMap.put(fieldID, fieldTypeRT.getSystemOptionType());
							if (raciRoleItemsAboveLimit.booleanValue()) {
								int raciRoleCount = LoadTreeFilterItemCounts.getItemCount(personID, filterUpperTO, null);
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("Items found as on behalf " + raciRoleCount);
								}
								count+=raciRoleCount;
								if (count>maxItems) {
									throw new TooManyItemsToLoadException("Too many items to load " + count, count);
								}
							}
							List<TWorkItemBean> onBehalfItems = getItems(personID, filterUpperTO, null,
									withCustomAttributes, attributeValueBeanList,
									withWatchers, watcherList,
									withMyExpenses, myExpenseList,
									withTotalExpenses, totalExpenseList,
									withBudgetPlan, budgetAndPlanList,
									withRemainingPlan, remainingPlanList,
									withAttachment, attachmentList,
									withLinks, itemLinkList,
									withParents, parentIDs,
									foundWorkItemsIDs, locale,
									startDate, endDate, qNode);
							if (onBehalfItems!=null) {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("Total number of on behalf items for field " + fieldID + " is " + onBehalfItems.size());
								}
								for (Iterator<TWorkItemBean> iterator = onBehalfItems.iterator(); iterator.hasNext();) {
									TWorkItemBean workItemBean = iterator.next();
									Integer workItemID = workItemBean.getObjectID();
									if (foundWorkItemsIDs.contains(workItemID)) {
										iterator.remove();
										if (editFlagNeeded) {
											TWorkItemBean includedWorkItemBean = notEditableMap.get(workItemID);
											if (includedWorkItemBean!=null) {
												includedWorkItemBean.setEditable(true);
												notEditableMap.remove(workItemID);
											}
										}
									} else {
										foundWorkItemsIDs.add(workItemID);
										if (editFlagNeeded) {
											workItemBean.setEditable(true);
										}
									}
								}
								if (!onBehalfItems.isEmpty()) {
									if (LOGGER.isDebugEnabled()) {
										LOGGER.debug("Number of on behalf only items for field " + fieldID + " is "+ onBehalfItems.size());
									}
									workItemBeanList.addAll(onBehalfItems);
								}
							}
							//set it back to original value for the next filter execution
							selectedCustomSelectsOriginal.put(fieldID, selectedOnBehalfUserPickerOriginal);
						}
					}
				}
			}
		}
		if (!workItemBeanList.isEmpty()) {
			String keyword = filterUpperTO.getKeyword();
			if (keyword!=null && keyword.length()>0) {
				//if keyword specified restrict the results
				try {
					int[] luceneWorkItems = LuceneSearcher.searchWorkItems(keyword, false, locale, null);
					if (luceneWorkItems==null || luceneWorkItems.length==0) {
						//not a single match found, clear the results got from the database
						LOGGER.debug("No match found for the keyword " + keyword + " using the lucene search.");
						workItemBeanList.clear();
					} else {
						//some match is found: combine it with the results from database search
						Set<Integer> workItemIDsSet = GeneralUtils.createSetFromIntArr(luceneWorkItems);
						for (Iterator<TWorkItemBean> iterator = workItemBeanList.iterator(); iterator.hasNext();) {
							TWorkItemBean workItemBean = iterator.next();
							if (!workItemIDsSet.contains(workItemBean.getObjectID())) {
								//report bean does not matches the keyword
								iterator.remove();
							}
						}
					}
				} catch (BooleanQuery.TooManyClauses e) {
					//do nothing because no ErrorData list available
				} catch (ParseException e) {
					LOGGER.warn("Parsing the keyword expression failed with " + e.getMessage(), e);
				}
			}
		}
		
		//gets the linked items if selected
		int[] linkedWorkItemIDs = LoadItemLinksUtil.getLinkedWorkItemIDs(filterUpperTO.getLinkTypeFilterSuperset(),
				filterUpperTO.getArchived(), filterUpperTO.getDeleted(), filterUpperTO.getItemTypeIDsForLinkType(), workItemBeanList);
		//If selected view in item navigator is Gant view, we need to add all linked work items
		if(personBean.getLastSelectedView() != null) {
			if(personBean.getLastSelectedView().equals(IssueListViewDescriptor.GANTT)) {
				int[] msProjectLinkedWorkItems = LoadItemLinksUtil.getMsProjectLinkedWorkItemIDs(
						filterUpperTO.getArchived(), filterUpperTO.getDeleted(), workItemBeanList);
				Set<Integer>msProjectLinkedWorkItemsSet = GeneralUtils.createSetFromIntArr(msProjectLinkedWorkItems);
				Set<Integer>linkedWorkItemIDsSet = GeneralUtils.createSetFromIntArr(linkedWorkItemIDs);
				//combining two set
				msProjectLinkedWorkItemsSet.addAll(linkedWorkItemIDsSet);
				linkedWorkItemIDs = GeneralUtils.createIntArrFromIntegerCollection(msProjectLinkedWorkItemsSet);
			}
		}
		
		if (linkedWorkItemIDs!=null && linkedWorkItemIDs.length>0) {
			Integer archived = filterUpperTO.getArchived();
			if (archived==null) {
				//should never happen (either both or none has value)
				archived = Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED);
			}
			Integer deleted = filterUpperTO.getDeleted();
			if (deleted==null) {
				//should never happen (either both or none has value)
				deleted = Integer.valueOf(FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED);
			}
			boolean includeArchivedDeleted = archived.intValue()!=FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED ||
					deleted.intValue()!=FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED;
			List<TWorkItemBean> linkedWorkItemBeans = LoadItemIDListItems.getItems(personID, linkedWorkItemIDs, includeArchivedDeleted, editFlagNeeded,
					withCustomAttributes, attributeValueBeanList,
					withWatchers, watcherList,
					withMyExpenses, myExpenseList,
					withTotalExpenses, totalExpenseList,
					withBudgetPlan, budgetAndPlanList,
					withRemainingPlan, remainingPlanList,
					withAttachment, attachmentList,
					withLinks, itemLinkList, withParents, parentIDs);
			workItemBeanList.addAll(linkedWorkItemBeans);
		}
		Integer archived = filterUpperTO.getArchived();
		Integer deleted = filterUpperTO.getDeleted();
		boolean archivedOrDeletedIncluded = (archived!=null && archived.intValue()!=ARCHIVED_FILTER.EXCLUDE_ARCHIVED) ||
				(deleted!=null && deleted.intValue()!=DELETED_FILTER.EXCLUDE_DELETED);
		if (archivedOrDeletedIncluded) {
			//if archived or deleted is included only the project admin might see them
			Map<Integer, Set<Integer>> projectToIssueTypesWithAdminRight = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(meAndSubstitutedAndGroups, ancestorProjects, new int[] {AccessFlagIndexes.PROJECTADMIN });
			for (Iterator<TWorkItemBean> iterator = workItemBeanList.iterator(); iterator.hasNext();) {
				TWorkItemBean workItemBean = iterator.next();
				Integer itemID = workItemBean.getObjectID();
				Integer itemProjectID = workItemBean.getProjectID();
				Integer itemTypeID = workItemBean.getListTypeID();
				boolean archivedOrDeleted = workItemBean.isArchivedOrDeleted();
				if (archivedOrDeleted && !AccessBeans.hasExplicitRight(personID, itemID, itemProjectID, itemTypeID, projectToIssueTypesWithAdminRight, childToParentProjectMap, "project admin")) {
					/**
					 * Exclude the deleted and archived workItems if the person is not project admin for the workItem
					 * a person selects more projects but is project admin only in some of them: all archived/deleted workItems
					 * in the projects where the person is not project admin should be filtered out
					 */
					iterator.remove();
				}
			}
		}
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date end = new Date();
			LOGGER.debug("Loading all filter items (" + workItemBeanList.size() + ") lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
		}
		return workItemBeanList;
	}
	
	/**
	 * Filter/set editable a partial result of list
	 * @param items
	 * @param foundWorkItemsIDs
	 * @param editFlagNeeded
	 * @param notEditableMap
	 * @return
	 */
	private static List<TWorkItemBean> filterItemList(List<TWorkItemBean> items, Set<Integer> foundWorkItemsIDs, boolean editFlagNeeded, Map<Integer, TWorkItemBean> notEditableMap) {
		if (items!=null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Number of RACI items: " + items.size());
			}
			for (Iterator<TWorkItemBean> iterator = items.iterator(); iterator.hasNext();) {
				TWorkItemBean workItemBean = iterator.next();
				Integer workItemID = workItemBean.getObjectID();
				if (foundWorkItemsIDs.contains(workItemID)) {
					iterator.remove();
					if (editFlagNeeded) {
						TWorkItemBean includedWorkItemBean = notEditableMap.get(workItemID);
						if (includedWorkItemBean!=null) {
							includedWorkItemBean.setEditable(true);
							notEditableMap.remove(workItemID);
						}
					}
				} else {
					foundWorkItemsIDs.add(workItemID);
					if (editFlagNeeded) {
						workItemBean.setEditable(true);
					}
				}
			}
			if (LOGGER.isDebugEnabled()) {
				if (!items.isEmpty()) {
					LOGGER.debug("Number of filtered RACI items: " + items.size());
				}
				
			}
		}
		return items;
	}
	
	/**
	 * Gets the items from the database
	 * @param personID
	 * @param filterUpperTO
	 * @param raciBean
	 * @param withCustomAttributes
	 * @param attributeValueBeanList
	 * @param startDate
	 * @param endDate
	 * @param qNode
	 */
	private static List<TWorkItemBean> getItems(Integer personID, FilterUpperTO filterUpperTO, RACIBean raciBean,
			boolean withCustomAttributes, List<TAttributeValueBean> attributeValueBeanList, boolean withWatchers, List<TNotifyBean> watcherList,
			boolean withMyExpenses, List<TComputedValuesBean> myExpenseList,
			boolean withTotalExpenses, List<TComputedValuesBean> totalExpenseList,
			boolean withBudgetPlan, List<TComputedValuesBean> budgetAndPlanList,
			boolean withRemainingPlan, List<TActualEstimatedBudgetBean> remainingPlanList,
			boolean withAttachment, List<TAttachmentBean> attachmentList,
			boolean withLinks, List<TWorkItemLinkBean> itemLinkList,
			boolean withParents, Set<Integer> parentIDsSet,
			Set<Integer> foundWorkItemsIDs, Locale locale,
			Date startDate, Date endDate, QNode qNode) {
		Date start = null;
		if (LOGGER.isDebugEnabled()) {
			start = new Date();
		}
		List<TWorkItemBean> workItemBeanList = workItemDAO.loadTreeFilterItems(filterUpperTO, raciBean, personID, startDate, endDate);
		if (LOGGER.isDebugEnabled() && start!=null) {
			Date itemLoadTime = new Date();
			LOGGER.debug("Loading " + workItemBeanList.size() + " items lasted " + new Long(itemLoadTime.getTime()-start.getTime()).toString() + " ms");
		}
		if (workItemBeanList!=null && !workItemBeanList.isEmpty()) {
			if (withCustomAttributes || qNode!=null) {
				Date customAttributesTime = null;
				if (LOGGER.isDebugEnabled()) {
					customAttributesTime = new Date();
				}
				//the custom attributes are needed also if the filter has tree part to apply the matchers to potential custom fields.
				//(they are loaded (together with showValue, sortOrder) also in ReportBeanLoader but this is to late for filter with tree part)
				List<TAttributeValueBean> customAttributesList = AttributeValueBL.loadTreeFilterAttributes(filterUpperTO, raciBean, personID);
				if (attributeValueBeanList!=null && customAttributesList!=null) {
					for (Iterator<TAttributeValueBean> iterator = customAttributesList.iterator(); iterator.hasNext();) {
						TAttributeValueBean attributeValueBean = iterator.next();
						Integer workItemID = attributeValueBean.getWorkItem();
						if (foundWorkItemsIDs.contains(workItemID)) {
							//do not duplicate the watchers from different getItems()
							iterator.remove();
						}
					}
					attributeValueBeanList.addAll(customAttributesList);
					LoadItemsUtil.loadCustomFields(workItemBeanList, attributeValueBeanList);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Loading " + customAttributesList.size() + " custom attributes lasted " + new Long(new Date().getTime()-customAttributesTime.getTime()).toString() + " ms");
					}
				}
			}
			if (withWatchers) {
				Date watchersTime = null;
				if (LOGGER.isDebugEnabled()) {
					watchersTime = new Date();
				}
				List<TNotifyBean> watchers = ConsInfBL.loadTreeFilterWatchers(filterUpperTO, raciBean, personID);
				if (watcherList!=null && watchers!=null) {
					for (Iterator<TNotifyBean> iterator = watchers.iterator(); iterator.hasNext();) {
						TNotifyBean notifyBean = iterator.next();
						Integer workItemID = notifyBean.getWorkItem();
						if (foundWorkItemsIDs.contains(workItemID)) {
							//do not duplicate the watchers from different getItems()
							iterator.remove();
						}
					}
					watcherList.addAll(watchers);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Loading " + watchers.size() + " watchers lasted " + new Long(new Date().getTime()-watchersTime.getTime()).toString() + " ms");
					}
				}	
			}
			if (withMyExpenses) {
				Date myExpensesTime = null;
				if (LOGGER.isDebugEnabled()) {
					myExpensesTime = new Date();
				}
				List<TComputedValuesBean> myExpenses = ComputedValueBL.loadByTreeFilterForPerson(filterUpperTO, raciBean, personID, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, true);
				if (myExpenseList!=null && myExpenses!=null) {
					for (Iterator<TComputedValuesBean> iterator = myExpenses.iterator(); iterator.hasNext();) {
						TComputedValuesBean computedValuesBean = iterator.next();
						Integer workItemID = computedValuesBean.getWorkitemKey();
						if (foundWorkItemsIDs.contains(workItemID)) {
							//do not duplicate my expenses from different getItems()
							iterator.remove();
						}
					}
					myExpenseList.addAll(myExpenses);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Loading " + myExpenses.size() + " my expenses lasted " + new Long(new Date().getTime()-myExpensesTime.getTime()).toString() + " ms");
					}
				}
			}
			if (withTotalExpenses) {
				Date totalExpensesTime = null;
				if (LOGGER.isDebugEnabled()) {
					totalExpensesTime = new Date();
				}
				List<TComputedValuesBean> totalExpenses = ComputedValueBL.loadByTreeFilterForPerson(filterUpperTO, raciBean, personID, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, false);
				if (totalExpenseList!=null && totalExpenses!=null) {
					for (Iterator<TComputedValuesBean> iterator = totalExpenses.iterator(); iterator.hasNext();) {
						TComputedValuesBean computedValuesBean = iterator.next();
						Integer workItemID = computedValuesBean.getWorkitemKey();
						if (foundWorkItemsIDs.contains(workItemID)) {
							//do not duplicate total expenses from different getItems()
							iterator.remove();
						}
					}
					totalExpenseList.addAll(totalExpenses);
					if (LOGGER.isDebugEnabled()) {
						
						LOGGER.debug("Loading " + totalExpenses.size() + " total expenses lasted " + new Long(new Date().getTime()-totalExpensesTime.getTime()).toString() + " ms");
					}
				}
			}
			if (withBudgetPlan) {
				Date budgetPlanTime = null;
				if (LOGGER.isDebugEnabled()) {
					budgetPlanTime = new Date();
				}
				int[] computedValueTypes = null;
				boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
				if (budgetActive) {
					computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, TComputedValuesBean.COMPUTEDVALUETYPE.BUDGET};
				} else {
					computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN};
				}
				List<TComputedValuesBean> budgetsAndPlans = ComputedValueBL.loadByTreeFilter(filterUpperTO, raciBean, personID, computedValueTypes);
				if (budgetAndPlanList!=null && budgetsAndPlans!=null) {
					for (Iterator<TComputedValuesBean> iterator = budgetsAndPlans.iterator(); iterator.hasNext();) {
						TComputedValuesBean computedValuesBean = iterator.next();
						Integer workItemID = computedValuesBean.getWorkitemKey();
						if (foundWorkItemsIDs.contains(workItemID)) {
							//do not duplicate the budgets/plans from different getItems()
							iterator.remove();
						}
					}
					budgetAndPlanList.addAll(budgetsAndPlans);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Loading " + budgetsAndPlans.size() + " bugets and plans (" + computedValueTypes.length + ") lasted " + new Long(new Date().getTime()-budgetPlanTime.getTime()).toString() + " ms");
					}
				}
			}
			if (withRemainingPlan) {
				Date remainingPlanTime = null;
				if (LOGGER.isDebugEnabled()) {
					remainingPlanTime = new Date();
				}
				List<TActualEstimatedBudgetBean> remainingPlans = RemainingPlanBL.loadByTreeFilter(filterUpperTO, raciBean, personID);
				if (remainingPlanList!=null && remainingPlans!=null) {
					for (Iterator<TActualEstimatedBudgetBean> iterator = remainingPlans.iterator(); iterator.hasNext();) {
						TActualEstimatedBudgetBean actualEstimatedBudgetBean = iterator.next();
						Integer workItemID = actualEstimatedBudgetBean.getWorkItemID();
						if (foundWorkItemsIDs.contains(workItemID)) {
							//do not duplicate the remaining budgets from different getItems()
							iterator.remove();
						}
					}
					remainingPlanList.addAll(remainingPlans);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Loading " + remainingPlans.size() + " remaining plans lasted " + new Long(new Date().getTime()-remainingPlanTime.getTime()).toString() + " ms");
					}
				}
			}
			if (withAttachment) {
				Date attachmentTime = null;
				if (LOGGER.isDebugEnabled()) {
					attachmentTime = new Date();
				}
				List<TAttachmentBean> attachmentsBeans = AttachBL.loadTreeFilterAttachments(filterUpperTO, raciBean, personID);
				if (attachmentList!=null && attachmentsBeans!=null) {
					for (Iterator<TAttachmentBean> iterator = attachmentsBeans.iterator(); iterator.hasNext();) {
						TAttachmentBean attachmentBean = iterator.next();
						Integer workItemID = attachmentBean.getWorkItem();
						if (foundWorkItemsIDs.contains(workItemID)) {
							//do not duplicate the attachments from different getItems()
							iterator.remove();
						}
					}
					attachmentList.addAll(attachmentsBeans);
					if (LOGGER.isDebugEnabled() ) {
						LOGGER.debug("Loading " + attachmentsBeans.size() + " attachment beans lasted " + new Long(new Date().getTime()-attachmentTime.getTime()).toString() + " ms");
					}
				}
			}
			if (withLinks) {
				Date linkTime = null;
				if (LOGGER.isDebugEnabled()) {
					linkTime = new Date();
				}
				List<TWorkItemLinkBean> itemLinkBeans = ItemLinkBL.loadTreeFilterLinks(filterUpperTO, raciBean, personID);
				if (itemLinkList!=null && itemLinkBeans!=null) {
					itemLinkList.addAll(itemLinkBeans);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Loading " + itemLinkBeans.size() + " item link beans lasted " + new Long(new Date().getTime()-linkTime.getTime()).toString() + " ms");
					}
				}
			}
			if (withParents) {
				Date parentTime = null;
				if (LOGGER.isDebugEnabled()) {
					parentTime = new Date();
				}
				Set<Integer> parentIDs = workItemDAO.loadTreeFilterParentIDs(filterUpperTO, raciBean, personID, startDate, endDate);
				if (parentIDsSet!=null && parentIDs!=null) {
					parentIDsSet.addAll(parentIDs);
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Loading " + parentIDs.size() + " parents lasted " + new Long(new Date().getTime()-parentTime.getTime()).toString() + " ms");
				}
			}
			ExecuteMatcherBL.filterByMatcherExpressions(workItemBeanList, filterUpperTO, qNode, personID,
					watcherList, myExpenseList, totalExpenseList, budgetAndPlanList, remainingPlanList);
			if (LOGGER.isDebugEnabled() && start!=null) {
				Date end = new Date();
				LOGGER.debug("Loading " + workItemBeanList.size() + " items with associated entities lasted " + new Long(end.getTime()-start.getTime()).toString() + " ms");
			}
		}
		return workItemBeanList;
	}

	
	
	/**
	 * Gets the item types for projects allowed to read
	 * @param selectedAndDescendantProjects
	 * @param selectedItemTypesSet
	 * @param projectToIssueTypesWithReadRight
	 * @param childToParentProjectIDMap
	 * @param raciProjectIDs no direct right but may have raci roles used later
	 * @return
	 */
	static Map<Set<Integer>, Set<Integer>> getItemTypesToProjectsMap(Integer[] selectedAndDescendantProjects, Set<Integer> selectedItemTypesSet,
			Map<Integer, Set<Integer>> projectToIssueTypesWithReadRight, Map<Integer, Integer> childToParentProjectIDMap, Set<Integer> raciProjectIDs) {
		Map<Set<Integer>, Set<Integer>> itemTypesToProjectsMap = new HashMap<Set<Integer>, Set<Integer>>();
		for (Integer projectKey : selectedAndDescendantProjects) {
			String projectLabel = null;
			if (LOGGER.isDebugEnabled()) {
				projectLabel = LookupContainer.getNotLocalizedLabelBeanLabel(SystemFields.INTEGER_PROJECT, projectKey);
			}
			Set<Integer> itemTypeLimitations = AccessBeans.getItemTypeLimitations(projectKey, selectedItemTypesSet, projectToIssueTypesWithReadRight, childToParentProjectIDMap);
			if (itemTypeLimitations==null) {
				raciProjectIDs.add(projectKey);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("No right for project " + projectLabel + " (" + projectKey + "). Add to RACI projects");
				}
			} else {
				if (itemTypeLimitations.contains(null)) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Project " + projectLabel + " (" + projectKey + ") has no item type limitation by role");
					}
				} else {
					if (selectedItemTypesSet.isEmpty()) {
						raciProjectIDs.add(projectKey);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("No items types selected in filter but there are item type limitations by role in project " + projectLabel + " (" + projectKey + "). Add to RACI projects");
						}
					} else {
						if (itemTypeLimitations.containsAll(selectedItemTypesSet)) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("All selected items types in filter are allowed by role in project " +	projectLabel +  " (" + projectKey + ")");
							}
						} else {
							raciProjectIDs.add(projectKey);
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Some selected items types in filter are not allowed by role in project " + projectLabel + " (" + projectKey + "). Add to RACI projects");
							}
						}
					}
				}
				Set<Integer> projectIDs = itemTypesToProjectsMap.get(itemTypeLimitations);
				if (projectIDs==null) {
					projectIDs = new HashSet<Integer>();
					itemTypesToProjectsMap.put(itemTypeLimitations, projectIDs);
				}
				projectIDs.add(projectKey);
			}
		}
		return itemTypesToProjectsMap;
	}
	
}
