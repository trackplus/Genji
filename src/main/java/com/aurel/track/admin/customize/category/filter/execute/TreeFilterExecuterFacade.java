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

package com.aurel.track.admin.customize.category.filter.execute;


import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.FilterFacade;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL.PREDEFINED_QUERY;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterSelectsListsLoader;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.GeneralUtils;

/**
 * Facade implementation for for executing a Tree filter
 * @author Tamas Ruff
 *
 */
public class TreeFilterExecuterFacade extends FilterExecuterFacade {
	private static TreeFilterExecuterFacade instance;
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static TreeFilterExecuterFacade getInstance(){
		if(instance==null){
			instance=new TreeFilterExecuterFacade();
		}
		return instance;
	}
	
	@Override
	public List<TWorkItemBean> getInstantFilterWorkItemBeans(String filterExpression, Integer filterID,
			Locale locale, TPersonBean personBean, List<ErrorData> errors, boolean withCustomAttributes,
			Integer projectID, Integer entityFlag, Date startDate, Date endDate) throws TooManyItemsToLoadException {
		QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
		FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(extendedRootNode, true, false, personBean, locale, true);
		QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
		return getInstantTreeFilterWorkItemBeans(filterUpperTO, 
				rootNode, filterID, personBean, locale, withCustomAttributes, projectID, entityFlag, startDate, endDate);
	}

	/**
	 * Gets the list of ReportBeans for a filterExpression
	 * @param filterExpression
	 * @param filterID
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param projectID
	 * @param entityFlag
	 * @param withParents
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	@Override
	public List<ReportBean> getInstantFilterReportBeanList(String filterExpression, Integer filterID, Locale locale,
			TPersonBean personBean, List<ErrorData> errors, Integer projectID, Integer entityFlag, boolean withParents) throws TooManyItemsToLoadException {
		QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
		FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
				extendedRootNode, true, false, personBean, locale, true);
		QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
		return getInstantTreeFilterReportBeanList(filterUpperTO, 
				rootNode, filterID, personBean, locale, projectID, entityFlag, withParents);
	}
	
	/**
	* Gets the list of ReportBeans for a filterExpression loading only the necessary associated entities
	* @param filterExpression
	* @param filterID TODO remove the filterID parameter as soon as filter's tree part is compiled into criteria
	* @param locale
	* @param personBean
	* @param errors
	* @param projectID
	* @param entityFlag
	* @return
	* @throws TooManyItemsToLoadException 
	*/
	@Override
	public List<ReportBean> getInstantFilterReportBeanList(String filterExpression, Integer filterID,
			Locale locale, TPersonBean personBean, boolean editFlagNeeded, List<ErrorData> errors, Integer projectID, Integer entityFlag,
			boolean withCustomAttributes, boolean withWatchers,
			boolean withMyExpenses, boolean withTotalExpenses,
			boolean withBudgetPlan, boolean withRemainingPlan,
			boolean withAttachment, boolean withLinks, boolean withParents) throws TooManyItemsToLoadException {
		QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
		FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
				extendedRootNode,true, false, personBean, locale, true);
		QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
		return getInstantTreeFilterReportBeanList(filterUpperTO, 
				rootNode, filterID, personBean, locale, editFlagNeeded, projectID, entityFlag,
				withCustomAttributes, withWatchers,
				withMyExpenses, withTotalExpenses,
				withBudgetPlan, withRemainingPlan,
				withAttachment, withLinks, withParents);
	}
	
	/**
	 * Gets the ReportBeans object for a filterExpression
	 * @param filterExpression
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param reportBeanExpandContext
	 * @param projectID
	 * @param entityFlag
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	@Override
	public ReportBeans getInstantFilterReportBeans(String filterExpression, Integer filterID, Locale locale, TPersonBean personBean, 
			List<ErrorData> errors, ReportBeanExpandContext reportBeanExpandContext, Integer projectID, Integer entityFlag, boolean withParents) throws TooManyItemsToLoadException {
		List<ReportBean> reportBeansList = getInstantFilterReportBeanList(filterExpression, filterID, locale, personBean, errors, projectID, entityFlag, withParents); 
		return new ReportBeans(reportBeansList, locale, reportBeanExpandContext, true);
	}
	
	/**
	 * Gets the ReportBeans object for a filterExpression
	 * @param filterUpperTO
     * @param rootNode
	 * @param locale
	 * @param personBean
	 * @param errors
	 * @param reportBeanExpandContext
	 * @param projectID
	 * @param projectID
	 * @param entityFlag
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */   
	public ReportBeans getInstantFilterReportBeans(FilterUpperTO filterUpperTO, QNode rootNode, Integer filterID, Locale locale, TPersonBean personBean, 
			List<ErrorData> errors, ReportBeanExpandContext reportBeanExpandContext, Integer projectID, Integer entityFlag) throws TooManyItemsToLoadException {
		List<ReportBean> reportBeansList = getInstantTreeFilterReportBeanList(filterUpperTO, rootNode, filterID, personBean, locale, null, null, false);
		return new ReportBeans(reportBeansList, locale, reportBeanExpandContext, true);
	}
	
	/**
	 * Get the list of workItemBeans for a tree filter 
	 * @param filterUpperTO
	 * @param qNode
	 * @param personBean
	 * @param locale
	 * @param withCustomAttributes
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<TWorkItemBean> getInstantTreeFilterWorkItemBeans(FilterUpperTO filterUpperTO,
			QNode qNode, Integer filterID, TPersonBean personBean, Locale locale, boolean withCustomAttributes,
			Integer projectID, Integer entityFlag, Date startDate, Date endDate) throws TooManyItemsToLoadException {
		prepareFilterUpperTO(filterUpperTO, personBean, locale, projectID, entityFlag);
		boolean hasListSelection = filterUpperTO.hasListSelection();
		boolean hasTQLExpression = filterUpperTO.hasTQLExpression();
		List<TWorkItemBean> workItemsList = null;
		if (hasTQLExpression && !hasListSelection) {
			//the new art of making a TQLPLus filter:
			//no list selected, but a lucene expression  is present
			//nothing to look into database
			workItemsList = TQLPlusFilterExecuterFacade.getInstance().getInstantFilterWorkItemBeans(
					filterUpperTO.getKeyword(), null, locale, personBean,
					new LinkedList<ErrorData>(), withCustomAttributes, projectID, entityFlag, startDate,endDate);
			if (workItemsList!=null) {
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
				for (Iterator<TWorkItemBean> iterator = workItemsList.iterator(); iterator.hasNext();) {
					TWorkItemBean workItemBean = iterator.next();
					if (removeArchivedDeleted(workItemBean.getArchiveLevel(), archived, deleted)) {
						iterator.remove();
					}
				}
			}
		} else {
			//get the potential items from the database according to the filterUpperTO
			workItemsList = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, qNode, filterID, personBean, locale, withCustomAttributes, startDate, endDate);
		}
		return workItemsList;
	}
	
	/**
	 * Getting the ReportBeans for a tree filter
	 * @param filterUpperTO
	 * @param qNode
	 * @param personBean
	 * @param locale
	 * @param projectID
	 * @param entityFlag
	 * @param withParents
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getInstantTreeFilterReportBeanList(FilterUpperTO filterUpperTO, 
			QNode qNode, Integer filterID, TPersonBean personBean, Locale locale, Integer projectID, Integer entityFlag, boolean withParents) throws TooManyItemsToLoadException {
		return getInstantTreeFilterReportBeanList(filterUpperTO, qNode, filterID, personBean, locale, true,
				projectID, entityFlag, true, true, true, true, true, true, true, true, withParents);
	}
	
	/**
	 * Getting the ReportBeans for a tree filter which will not be exported as report datasource
	 * (parentSet is not loaded, consequently summary flag is not set)
	 * @param filterUpperTO
	 * @param qNode
	 * @param personBean
	 * @param locale
	 * @param projectID
	 * @param entityFlag
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getInstantTreeFilterReportBeanListNoReport(FilterUpperTO filterUpperTO, 
			QNode qNode, Integer filterID, TPersonBean personBean, Locale locale, Integer projectID, Integer entityFlag) throws TooManyItemsToLoadException {
		return getInstantTreeFilterReportBeanList(filterUpperTO, qNode, filterID, personBean, locale, true,
				projectID, entityFlag, true, true, true, true, true, true, true, true, false);
	}
	
	/**
	 * Adds additional select condition to a saved filter and the executes the filter
	 * @param filterID
	 * @param locale
	 * @param personBean
	 * @param projectID
	 * @param entityFlag
	 * @param selectFieldID
	 * @param selectFieldValue
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public static List<ReportBean> getTreeFilterReportBeanListWithAdditionalCondition(Integer filterID, Locale locale,
			TPersonBean personBean, Integer projectID, Integer entityFlag, Integer selectFieldID, Integer selectFieldValue, boolean includeResponsiblesThroughGroup) throws TooManyItemsToLoadException {
		FilterFacade filterFacade = TreeFilterFacade.getInstance();
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)filterFacade.getByKey(filterID);
		String filterExpression = FilterBL.getFilterExpression(queryRepositoryBean);
		QNode extendedRootNode = TreeFilterReader.getInstance().readQueryTree(filterExpression);
		FilterUpperTO filterUpperTO = FilterUpperFromQNodeTransformer.getFilterSelectsFromTree(
			extendedRootNode, true, false, personBean, locale, true);
		QNode rootNode = TreeFilterLoaderBL.getOriginalTree(extendedRootNode);
		filterUpperTO.setSelectedValuesForField(selectFieldID, new Integer[] {selectFieldValue});
		filterUpperTO.setIncludeResponsiblesThroughGroup(includeResponsiblesThroughGroup);
		return TreeFilterExecuterFacade.getInstantTreeFilterReportBeanListNoReport(filterUpperTO, rootNode, filterID, personBean, locale, projectID, entityFlag);		
	}
	
	/**
	 * Prepares the filterUpperTO matcher context and the selected projects if called from project dashboard
	 * @param filterUpperTO
	 * @param personBean
	 * @param locale
	 * @param projectID
	 * @param entityFlag
	 * @return
	 */
	public static boolean prepareFilterUpperTO(FilterUpperTO filterUpperTO, TPersonBean personBean, Locale locale, Integer projectID, Integer entityFlag) {
		Integer personID = personBean.getObjectID();
		MatcherContext matcherContext = new MatcherContext();
		matcherContext.setLoggedInUser(personID);
		matcherContext.setLastLoggedDate(personBean.getLastButOneLogin());
		matcherContext.setLocale(locale);
		filterUpperTO.setMatcherContext(matcherContext);
		matcherContext.setIncludeResponsiblesThroughGroup(filterUpperTO.isIncludeResponsiblesThroughGroup());
		matcherContext.setReleaseTypeSelector(filterUpperTO.getReleaseTypeSelector());
		filterUpperTO.cleanParameters();
		return setSelectedProjects(filterUpperTO, personID, projectID, entityFlag);
	}
	
	/**
	 * Gets the RACI bean for MY_ITEMS. Forces changes in FilterUpperTO
	 * @param filterID
	 * @param personID
	 * @param filterUpperTO
	 * @return
	 */
	public static RACIBean getRACIBean(Integer filterID, Integer personID, FilterUpperTO filterUpperTO) {
		RACIBean raciBean = null;
		if (filterID!=null && filterID.intValue()==PREDEFINED_QUERY.MY_ITEMS) {
			Integer[] selectedStates = filterUpperTO.getSelectedStates();
			if (selectedStates==null || selectedStates.length==0) {
				List<TStateBean> notClosedStateBeans = StatusBL.loadNotClosedStates();
				List<Integer> notClosedStateIDs = GeneralUtils.createIntegerListFromBeanList(notClosedStateBeans);
				Integer[] notClosedStatesArr = GeneralUtils.createIntegerArrFromCollection(notClosedStateIDs);
				filterUpperTO.setSelectedStates(notClosedStatesArr);
			}
			filterUpperTO.setSelectedManagers(null);
			filterUpperTO.setSelectedResponsibles(null);
			filterUpperTO.setSelectedOriginators(null);
			//FIXME MY ITEMS is hardcoded because of performance issues (ORs in tree part)
			Set<Integer> meAsRaci = new HashSet<Integer>();
			meAsRaci.add(personID);
			raciBean = new RACIBean();
			raciBean.setAuthors(meAsRaci);
			raciBean.setResponsibles(meAsRaci);
			raciBean.setManagers(meAsRaci);
		}
		return raciBean;
	}
	
	/**
	 * Getting the ReportBeans for a tree filter
	 * @param filterUpperTO
	 * @param qNode
	 * @param personBean
	 * @param locale
	 * @param projectID
	 * @param entityFlag
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	private static List<ReportBean> getInstantTreeFilterReportBeanList(FilterUpperTO filterUpperTO, 
			QNode qNode, Integer filterID, TPersonBean personBean, Locale locale, boolean editFlagNeeded, Integer projectID, Integer entityFlag,
			boolean withCustomAttributes, boolean withWatchers,
			boolean withMyExpenses, boolean withTotalExpenses,
			boolean withBudgetPlan, boolean withRemainingPlan,
			boolean withAttachment, boolean withLinks, boolean withParents) throws TooManyItemsToLoadException {
		boolean forcedSelectionWasNeeded = prepareFilterUpperTO(filterUpperTO, personBean, locale, projectID, entityFlag);
		boolean hasListSelection = filterUpperTO.hasListSelection();
		boolean hasTQLExpression = filterUpperTO.hasTQLExpression();
		List<ReportBean> reportBeansList = null;
		if (hasTQLExpression && !hasListSelection) {
			//the new art of making a TQLPLus filter:
			//no list selected, but a lucene expression is present
			//nothing to look into database
			reportBeansList = TQLPlusFilterExecuterFacade.getInstance().getInstantFilterReportBeanList(
					filterUpperTO.getKeyword(), null, locale, personBean, editFlagNeeded, new LinkedList<ErrorData>(), projectID, entityFlag,
					withCustomAttributes, withWatchers,
					withMyExpenses, withTotalExpenses,
					withBudgetPlan, withRemainingPlan,
					withAttachment, withLinks, withParents);
			if (reportBeansList!=null) {
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
				for (Iterator<ReportBean> iterator = reportBeansList.iterator(); iterator.hasNext();) {
					ReportBean reportBean = iterator.next();
					TWorkItemBean workItemBean = reportBean.getWorkItemBean();
					if (removeArchivedDeleted(workItemBean.getArchiveLevel(), archived, deleted)) {
						iterator.remove();
					}
				}
			}
		} else {
			//get the potential items from the database according to the filterUpperTO
			reportBeansList = LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, qNode, filterID, editFlagNeeded, personBean, locale, 
					withCustomAttributes, withWatchers,
					withMyExpenses, withTotalExpenses,
					withBudgetPlan, withRemainingPlan,
					withAttachment, withLinks, withParents);
			if (forcedSelectionWasNeeded) {
				//reset it back for user interface: do not appear as all selected when none 
				//of them was selected even if it means the same thing
				filterUpperTO.setSelectedProjects(new Integer[0]);
			}
		}
		return reportBeansList;
	}
	
	/**
	 * Whether to remove a workItemBean from the lucene result because of the archived/deleted criteria 
	 * @param workItemArchiveLevel
	 * @param archived
	 * @param deleted
	 * @return
	 */
	private static boolean removeArchivedDeleted(Integer workItemArchiveLevel, Integer archived, Integer deleted) {
		boolean removeByArchived = false; 
		if (archived==null) {
			//should never happen (either both or none has value)
			archived = Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED);
		}
		switch (archived.intValue()) {
		case FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED:
			removeByArchived = workItemArchiveLevel!=null && !workItemArchiveLevel.equals(TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED);
			break;
		case FilterUpperTO.ARCHIVED_FILTER.ONLY_ARCHIVED:
			removeByArchived = !workItemArchiveLevel.equals(TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED);
			break;
		}
		boolean removeByDeleted = false; 
		if (deleted==null) {
			//should never happen (either both or none has value)
			deleted = Integer.valueOf(FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED);
		}
		switch (archived.intValue()) {
		case FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED:
			removeByDeleted = workItemArchiveLevel!=null && !workItemArchiveLevel.equals(TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED);
			break;
		case FilterUpperTO.DELETED_FILTER.ONLY_DELETED:
			removeByDeleted =  !workItemArchiveLevel.equals(TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED);
			break;
		}
		return removeByArchived && removeByDeleted;
	}
	
	/**
	 * If no project is selected, it will select all projects from the list
	 * If projectID is not null it filters also by projectID/releaseID
	 * @param filterUpperTO
	 * @param personID
	 * @param projectOrReleaseID
	 * @param entityFlag
	 * @return
	 */
	private static boolean setSelectedProjects(FilterUpperTO filterUpperTO, Integer personID, Integer projectOrReleaseID, Integer entityFlag) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		Integer[] selectedReleases = filterUpperTO.getSelectedReleases();
		if (projectOrReleaseID==null || entityFlag==null) {
			if (selectedProjects==null || selectedProjects.length==0) {
				//no project selection at all get all projects where the current user has right
				Integer[] projectsWithRole = GeneralUtils.createIntegerArrFromCollection(
						GeneralUtils.createIntegerListFromBeanList(FilterSelectsListsLoader.getProjects(personID)));
				if (projectsWithRole!=null && projectsWithRole.length>0) {
					//select the descendants of the selected projects also
					filterUpperTO.setSelectedProjects(ProjectBL.getDescendantProjectIDs(projectsWithRole));
				}
				if (selectedReleases!=null && selectedReleases.length>0) {
					//add descendant releases if any release was selected
					filterUpperTO.setSelectedReleases(ReleaseBL.getDescendantReleaseIDs(selectedReleases));
				}
				return true;
			} else {
				//add descendant projects to the selected projects
				filterUpperTO.setSelectedProjects(ProjectBL.getDescendantProjectIDs(selectedProjects));
				if (selectedReleases!=null && selectedReleases.length>0) {
					//add descendant releases if any release was selected
					filterUpperTO.setSelectedReleases(ReleaseBL.getDescendantReleaseIDs(selectedReleases));
				}
			}
		} else {
			//explicit project/release present (from browse projects)
			if (SystemFields.INTEGER_PROJECT.equals(entityFlag)) {
				if (selectedProjects==null || selectedProjects.length==0) {
					//no project selection in filter, force the projectOrReleaseID as project selection
					filterUpperTO.setSelectedProjects(ProjectBL.getDescendantProjectIDs(new Integer[] {projectOrReleaseID}));
				} else {
					//there is project selection in filter
					boolean selected = false;
					for (Integer selectedProject : selectedProjects) {
						if (projectOrReleaseID.equals(selectedProject)) {
							//the projectID is also selected
							selected = true;
							break;
						}
					}
					if (selected) {
						//found among the selected projects: leave only this as selected
						filterUpperTO.setSelectedProjects(ProjectBL.getDescendantProjectIDs(new Integer[] {projectOrReleaseID}));
					} else {
						//some projects are selected but projectID is not among them: set selected projects to null
						filterUpperTO.setSelectedProjects(null);
					}
				}
			} else {
				//explicit releaseID
				if (selectedReleases==null || selectedReleases.length==0) {
					//no release selection, force the projectOrReleaseID as release selection
					filterUpperTO.setSelectedReleases(ReleaseBL.getDescendantReleaseIDs(new Integer[] {projectOrReleaseID}));
					TReleaseBean releaseBean = LookupContainer.getReleaseBean(projectOrReleaseID);
					if (releaseBean!=null) {
						//force the project selection also (because without project selection there is no item result)
						filterUpperTO.setSelectedProjects(new Integer[] {releaseBean.getProjectID()});
					}
				} else {
					//there is release selection
					boolean selected = false;
					for (Integer selectedRelease : selectedReleases) {
						if (projectOrReleaseID.equals(selectedRelease)) {
							//the releaseID is also selected
							selected = true;
							break;
						}
					}
					if (selected) {
						//found among the selected releases: leave only this as selected
						filterUpperTO.setSelectedReleases(ReleaseBL.getDescendantReleaseIDs(new Integer[] {projectOrReleaseID}));
						TReleaseBean releaseBean = LookupContainer.getReleaseBean(projectOrReleaseID);
						if (releaseBean!=null) {
							//force the project selection also (because without project selection there is no item result)
							filterUpperTO.setSelectedProjects(new Integer[] {releaseBean.getProjectID()});
						}
					} else {
						//some releases are selected but projectOrReleaseID is not among them
						filterUpperTO.setSelectedProjects(null);
						filterUpperTO.setSelectedReleases(null);
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Replaces the symbolic values with the actual ones before execute
	 * @param filterSelectsTO
	 * @param replacementContext
	 * @return
	 */
	public static FilterUpperTO replaceSymbolicWithActual(
			FilterUpperTO filterSelectsTO,  
			MatcherContext replacementContext) {
		if (filterSelectsTO!=null && replacementContext!=null) {
			replaceSymbolicWithActual(filterSelectsTO.getSelectedManagers(), replacementContext);
			replaceSymbolicWithActual(filterSelectsTO.getSelectedResponsibles(), replacementContext);
			replaceSymbolicWithActual(filterSelectsTO.getSelectedOriginators(), replacementContext);
			replaceSymbolicWithActual(filterSelectsTO.getSelectedChangedBys(), replacementContext);
			replaceSymbolicWithActual(filterSelectsTO.getSelectedConsultantsInformants(), replacementContext);
		}
		return filterSelectsTO;
	}
	
	/**
	 * Replace the symbolic value with the actual one before execute
	 * @param selectedValues
	 * @param replacementContext
	 */
	private static void replaceSymbolicWithActual(Integer[] selectedValues, 
			MatcherContext replacementContext) {
		if (selectedValues!=null && selectedValues.length>0) {
			for (int i=0; i<selectedValues.length;i++) {
				Integer selectedValue = selectedValues[i];
				if (MatcherContext.LOGGED_USER_SYMBOLIC.equals(selectedValue)) {
					Map<Integer, Integer> loggedUserReplacement = 
						(Map<Integer, Integer>)(replacementContext.getContextMap().get(
								MatcherContext.LOGGED_USER));
					if (loggedUserReplacement!=null && loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC)!=null) {
						selectedValues[i] = ((Integer)loggedUserReplacement.get(MatcherContext.LOGGED_USER_SYMBOLIC));
						return;
					}
				}
			}
		}
	}
}
