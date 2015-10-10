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

package com.aurel.track.admin.customize.category.filter;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TCardFieldOptionBean;
import com.aurel.track.beans.TCardGroupingFieldBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TNavigatorLayoutBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.dbase.InitDatabase;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.itemNavigator.cardView.CardViewBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.layout.NavigatorLayoutBL;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.GeneralUtils;

/**
 *
 */
public class PredefinedQueryBL {
	
	private static final Logger LOGGER = LogManager.getLogger(PredefinedQueryBL.class); 
	
	public static interface PREDEFINED_QUERY{
		public static final int ALL_ITEMS = -11;//106;
		public static final int OUTSTANDING = -12;//107;
		public static int MY_ITEMS = -13;//101;
		public static int MANAGERS_ITEMS = -14;//102;
		public static int RESPONSIBLES_ITEMS = -15;//103;
		public static int AUTHOR_ITEMS = -16;//104;
		public static int MEETINGS = -17;//105;
		public static final int UNSCHEDULED= -18;//108;
		public static final int CLOSED_RECENTLY = -19;//109;
		public static final int ADDED_RECENTLY = -20;//110;
		public static final int UPDATED_RECENTLY = -21;//111;
		public static int WATCHER_ITEMS = -22;//112;
		public static int SCRUM_BOARD = -23;
		public static int KANBAN_BOARD = -24;
	}

	
	public static interface PREDEFINED_QUERY_NAME {
		public static String ALL_ITEMS = "AllItems"; 
		public static String OUTSTANDING = "Outstanding";
		public static String MY_ITEMS = "MyItems";
		public static String MANAGER_ITEMS = "ManagerItems";
		public static String RESPONSIBLE_ITEMS = "ResponsibleItems";
		public static String AUTHOR_ITEMS = "AuthorItems";
		public static String WATCHER_ITEMS = "WatcherItems";
		public static String MEETINGS = "Meetings";
		public static String UNSCHEDULED = "Unscheduled";
		public static String CLOSED_RECENTLY = "ClosedRecently";
		public static String ADDED_RECENTLY = "AddedRecently";
		public static String UPDATED_RECENTLY = "UpdatedRecently";
		public static String SCRUM_BOARD = "ScrumBoard";
		public static String KANBAN_BOARD = "KanbanBoard";
	}
	
	/**
	 * Gets the filter expression for all issues
	 * @return
	 */
	public static String getAllIssuesExpression() {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the all issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for outstanding (unresolved) issues
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getOutstandingExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the unresolved issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for my issues
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getMyItemsExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		String filterExpression = null;
		
		List<FieldExpressionInTreeTO> fieldExpressionInTreeTOList = new LinkedList<FieldExpressionInTreeTO>();
		
		FieldExpressionInTreeTO managerExpression = new FieldExpressionInTreeTO();
		managerExpression.setField(SystemFields.INTEGER_MANAGER);
		managerExpression.setSelectedMatcher(MatchRelations.EQUAL);
		managerExpression.setValue(new Integer[] { MatcherContext.LOGGED_USER_SYMBOLIC });
		managerExpression.setSelectedOperation(QNode.OR);
		fieldExpressionInTreeTOList.add(managerExpression);
		
		FieldExpressionInTreeTO responsibleExpression = new FieldExpressionInTreeTO();
		responsibleExpression.setField(SystemFields.INTEGER_RESPONSIBLE);
		responsibleExpression.setSelectedMatcher(MatchRelations.EQUAL);
		responsibleExpression.setValue(new Integer[] { MatcherContext.LOGGED_USER_SYMBOLIC });
		responsibleExpression.setSelectedOperation(QNode.OR);
		fieldExpressionInTreeTOList.add(responsibleExpression);
		
		FieldExpressionInTreeTO authorExpression = new FieldExpressionInTreeTO();
		authorExpression.setField(SystemFields.INTEGER_ORIGINATOR);
		authorExpression.setSelectedMatcher(MatchRelations.EQUAL);
		authorExpression.setValue(new Integer[] { MatcherContext.LOGGED_USER_SYMBOLIC });
		authorExpression.setSelectedOperation(QNode.OR);
		fieldExpressionInTreeTOList.add(authorExpression);
		
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, fieldExpressionInTreeTOList);
		} catch (Exception e) {
			LOGGER.info("Getting my issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for manager issues
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getManagerItemsExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedManagers(new Integer[] {MatcherContext.LOGGED_USER_SYMBOLIC});
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the manager issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}

	/**
	 * Gets the filter expression for responsible issues
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getResponsibleItemsExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedResponsibles(new Integer[] {MatcherContext.LOGGED_USER_SYMBOLIC});
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the responsible issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for reporter issues
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getReporterItemsExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedOriginators(new Integer[] {MatcherContext.LOGGED_USER_SYMBOLIC});
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the author issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for reporter issues
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getWatcherItemsExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedConsultantsInformants(new Integer[] {MatcherContext.LOGGED_USER_SYMBOLIC});
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the consulted/informed issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for meeting issues
	 * TODO add only the RACI meetings: like by My Issues, but the informed/consulted persons cannot be added in the lower/tree part (lower part because OR not AND)
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getMeetingItemsExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		List<TListTypeBean> meetingIssueTypes = IssueTypeBL.loadByTypeFlag(TListTypeBean.TYPEFLAGS.MEETING);
		filterUpperTO.setSelectedIssueTypes(GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(meetingIssueTypes)));
		List<FieldExpressionInTreeTO> fieldExpressionInTreeTOList = new LinkedList<FieldExpressionInTreeTO>();
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, fieldExpressionInTreeTOList);
		} catch (Exception e) {
			LOGGER.info("Getting the meeting issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	
	/**
	 * Gets the filter expression for unscheduled issues
	 * @param notClosedStatesArr
	 * @return
	 */
	public static String getUnscheduledItemsExpression(Integer[] notClosedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		filterUpperTO.setSelectedStates(notClosedStatesArr);
		FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO();
		fieldExpressionSimpleTO.setField(SystemFields.INTEGER_ENDDATE);
		fieldExpressionSimpleTO.setSelectedMatcher(MatchRelations.IS_NULL);
		List<FieldExpressionSimpleTO> filterExpressionSimpleList = new LinkedList<FieldExpressionSimpleTO>();
		filterExpressionSimpleList.add(fieldExpressionSimpleTO);
		filterUpperTO.setFieldExpressionSimpleList(filterExpressionSimpleList);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the unscheduled issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for recently closed issues
	 * @param closedStatesArr
	 * @return
	 */
	public static String getRecentlyClosedItemsExpression(Integer[] closedStatesArr) {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO();
		filterUpperTO.setSelectedStates(closedStatesArr);
		fieldExpressionSimpleTO.setField(SystemFields.INTEGER_LASTMODIFIEDDATE);
		fieldExpressionSimpleTO.setSelectedMatcher(MatchRelations.LESS_THAN_DAYS_AGO);
		fieldExpressionSimpleTO.setValue(Integer.valueOf(MatcherContext.DAYS_BEFORE));
		List<FieldExpressionSimpleTO> filterExpressionSimpleList = new LinkedList<FieldExpressionSimpleTO>();
		filterExpressionSimpleList.add(fieldExpressionSimpleTO);
		filterUpperTO.setFieldExpressionSimpleList(filterExpressionSimpleList);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the recently closed issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for recently added issues
	 * @return
	 */
	public static String getRecentlyAddedItemsExpression() {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO();
		fieldExpressionSimpleTO.setField(SystemFields.INTEGER_CREATEDATE);
		fieldExpressionSimpleTO.setSelectedMatcher(MatchRelations.LESS_THAN_DAYS_AGO);
		fieldExpressionSimpleTO.setValue(Integer.valueOf(MatcherContext.DAYS_BEFORE));
		List<FieldExpressionSimpleTO> filterExpressionSimpleList = new LinkedList<FieldExpressionSimpleTO>();
		filterExpressionSimpleList.add(fieldExpressionSimpleTO);
		filterUpperTO.setFieldExpressionSimpleList(filterExpressionSimpleList);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the recently added issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}
	
	/**
	 * Gets the filter expression for recently updated issues
	 * @return
	 */
	public static String getRecentlyUpdatedItemsExpression() {
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		FilterUpperTO filterUpperTO = new FilterUpperTO();
		FieldExpressionSimpleTO fieldExpressionSimpleTO = new FieldExpressionSimpleTO();
		fieldExpressionSimpleTO.setField(SystemFields.INTEGER_LASTMODIFIEDDATE);
		fieldExpressionSimpleTO.setSelectedMatcher(MatchRelations.LESS_THAN_DAYS_AGO);
		fieldExpressionSimpleTO.setValue(Integer.valueOf(7));
		List<FieldExpressionSimpleTO> filterExpressionSimpleList = new LinkedList<FieldExpressionSimpleTO>();
		filterExpressionSimpleList.add(fieldExpressionSimpleTO);
		filterUpperTO.setFieldExpressionSimpleList(filterExpressionSimpleList);
		String filterExpression = null;
		try {
			filterExpression = filterFacade.getFilterExpression(null, filterUpperTO, null);
		} catch (Exception e) {
			LOGGER.info("Getting the recently updated issues expression failed with " + e.getMessage(), e);
		}
		return filterExpression;
	}

	public static List<ReportBean> getReportBeans(TPersonBean personBean, Integer filterID,
			Integer project, Integer entityFlag, Locale locale) throws TooManyItemsToLoadException {
		List<ErrorData> errorData = new LinkedList<ErrorData>();
		return TreeFilterExecuterFacade.getSavedFilterReportBeanList(filterID, locale, personBean, errorData, project, entityFlag);
	}
	
	
	
	/**
	 * Add the hardcoded filters Use JDBC because negative objectIDs should be
	 * added
	 */
	public static void addHardcodedFilters() {
		LOGGER.info("Add hardcoded filters");
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(
						TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		List<TPersonBean> personBeans = PersonBL.loadPersons();
		// Get not closed stateIDs
		List<TStateBean> notClosedStateBeans = StatusBL.loadNotClosedStates();
		List<Integer> notClosedStateIDs = GeneralUtils.createIntegerListFromBeanList(notClosedStateBeans);
		Integer[] notClosedStatesArr = GeneralUtils.createIntegerArrFromCollection(notClosedStateIDs);
		// get closed stateIDs
		List<TStateBean> closedStateBeans = StatusBL.loadClosedStates();
		List<Integer> closedStateIDs = GeneralUtils.createIntegerListFromBeanList(closedStateBeans);
		Integer[] closedStatesArr = GeneralUtils.createIntegerArrFromCollection(closedStateIDs);
		List<String> predefinedFilterClobStms = new ArrayList<String>();
		List<String> predefinedFilterStms = new ArrayList<String>();
		ILabelBean allItemsFilterBean = filterFacade.getByKey(PREDEFINED_QUERY.ALL_ITEMS);
		if (allItemsFilterBean == null) {
			LOGGER.info("Add 'All issues' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.ALL_ITEMS,
					getAllIssuesExpression()));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.ALL_ITEMS, "All issues"));
		}
		ILabelBean unresolvedBean = filterFacade.getByKey(PREDEFINED_QUERY.OUTSTANDING);
		if (unresolvedBean == null) {
			LOGGER.info("Add 'Outstanding' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.OUTSTANDING,
					getOutstandingExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.OUTSTANDING, "Outstanding"));
		}
		ILabelBean myFilterBean = filterFacade.getByKey(PREDEFINED_QUERY.MY_ITEMS);
		if (myFilterBean == null) {
			LOGGER.info("Add 'My items' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.MY_ITEMS,
							getMyItemsExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.MY_ITEMS, "My items"));
		}
		ILabelBean managersBean = filterFacade.getByKey(PREDEFINED_QUERY.MANAGERS_ITEMS);
		if (managersBean == null) {
			LOGGER.info("Add manager filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.MANAGERS_ITEMS,
					getManagerItemsExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.MANAGERS_ITEMS, "I''m the manager"));
		}
		ILabelBean responsibleBean = filterFacade.getByKey(PREDEFINED_QUERY.RESPONSIBLES_ITEMS);
		if (responsibleBean == null) {
			LOGGER.info("Add responsible filter");
			predefinedFilterClobStms
					.add(addPredefinedQueryClob(PREDEFINED_QUERY.RESPONSIBLES_ITEMS,
							getResponsibleItemsExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.RESPONSIBLES_ITEMS, "I''m responsible"));
		}
		ILabelBean reporterBean = filterFacade.getByKey(PREDEFINED_QUERY.AUTHOR_ITEMS);
		if (reporterBean == null) {
			LOGGER.info("Add author filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.AUTHOR_ITEMS,
					getReporterItemsExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.AUTHOR_ITEMS, "I''m the author"));
		}
		ILabelBean watcherBean = filterFacade.getByKey(PREDEFINED_QUERY.WATCHER_ITEMS);
		if (watcherBean == null) {
			LOGGER.info("Add watcher filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.WATCHER_ITEMS,
					getWatcherItemsExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.WATCHER_ITEMS, "I''m watcher"));
		}
		ILabelBean meetingsBean = filterFacade.getByKey(PREDEFINED_QUERY.MEETINGS);
		if (meetingsBean == null) {
			LOGGER.info("Add 'Meetings' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.MEETINGS,
					getMeetingItemsExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.MEETINGS, "Meetings"));
		}
		ILabelBean unscheduledBean = filterFacade.getByKey(PREDEFINED_QUERY.UNSCHEDULED);
		if (unscheduledBean == null) {
			LOGGER.info("Add 'Unscheduled' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.UNSCHEDULED,
							getUnscheduledItemsExpression(notClosedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.UNSCHEDULED, "Unscheduled"));
		}
		ILabelBean closedRecentlyBean = filterFacade.getByKey(PREDEFINED_QUERY.CLOSED_RECENTLY);
		if (closedRecentlyBean == null) {
			LOGGER.info("Add 'Closed recently' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.CLOSED_RECENTLY,
							getRecentlyClosedItemsExpression(closedStatesArr)));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.CLOSED_RECENTLY, "Closed recently"));
		}
		ILabelBean addedRecentlyBean = filterFacade.getByKey(PREDEFINED_QUERY.ADDED_RECENTLY);
		if (addedRecentlyBean == null) {
			LOGGER.info("Add 'Added recently' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.ADDED_RECENTLY,
					getRecentlyAddedItemsExpression()));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.ADDED_RECENTLY, "Added recently"));
		}
		ILabelBean updatedRecentlyBean = filterFacade.getByKey(PREDEFINED_QUERY.UPDATED_RECENTLY);
		if (updatedRecentlyBean == null) {
			LOGGER.info("Add 'Updated recently' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(PREDEFINED_QUERY.UPDATED_RECENTLY,
					getRecentlyUpdatedItemsExpression()));
			predefinedFilterStms.add(addPredefinedQuery(
					PREDEFINED_QUERY.UPDATED_RECENTLY, "Updated recently"));
		}
		ILabelBean scrumBoardFilterBean = filterFacade.getByKey(PREDEFINED_QUERY.SCRUM_BOARD);
		if (scrumBoardFilterBean == null) {
			LOGGER.info("Add 'Scrum board' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(
					PREDEFINED_QUERY.SCRUM_BOARD,
					getAllIssuesExpression()));
			predefinedFilterStms.add(addPredefinedFilterWitView(
					PREDEFINED_QUERY.SCRUM_BOARD, "Scrum board", IssueListViewDescriptor.CARD));
		}
		
		ILabelBean kanbanBoardFilterBean = filterFacade.getByKey(PREDEFINED_QUERY.KANBAN_BOARD);
		if (kanbanBoardFilterBean == null) {
			LOGGER.info("Add 'Kanban board' filter");
			predefinedFilterClobStms.add(addPredefinedQueryClob(
					PREDEFINED_QUERY.KANBAN_BOARD,
					getAllIssuesExpression()));
			predefinedFilterStms.add(addPredefinedFilterWitView(
					PREDEFINED_QUERY.KANBAN_BOARD, "Kanban board", IssueListViewDescriptor.CARD));
		}
		Connection cono = null;
		try {
			cono = InitDatabase.getConnection();
			Statement ostmt = cono.createStatement();
			cono.setAutoCommit(false);
			for (String filterClobStmt : predefinedFilterClobStms) {
				ostmt.executeUpdate(filterClobStmt);
			}
			for (String filterStmt : predefinedFilterStms) {
				ostmt.executeUpdate(filterStmt);
			}
			cono.commit();
			cono.setAutoCommit(true);
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (cono != null) {
					cono.close();
				}
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		// include in menu for all users
		List<Integer> filterIDs = MenuitemFilterBL.getFilterIDsToSubscribe();
		LOGGER.info("Number of filters to subscribe " + filterIDs.size());
		if (!filterIDs.isEmpty()) {
			for (TPersonBean personBean : personBeans) {
				Integer personID = personBean.getObjectID();
				MenuitemFilterBL.subscribePersonsToFilters(personID, filterIDs);
			}
		}
		scrumBoardFilterBean = filterFacade.getByKey(PREDEFINED_QUERY.SCRUM_BOARD);
		if (scrumBoardFilterBean!=null) {
			addCardLayout(scrumBoardFilterBean.getObjectID(), SystemFields.INTEGER_STATE, SystemFields.INTEGER_PRIORITY, (List)notClosedStateBeans);	
		}
		kanbanBoardFilterBean = filterFacade.getByKey(PREDEFINED_QUERY.KANBAN_BOARD);
		if (kanbanBoardFilterBean!=null) {
			addCardLayout(kanbanBoardFilterBean.getObjectID(), SystemFields.INTEGER_RESPONSIBLE, SystemFields.INTEGER_PRIORITY, (List)PersonBL.loadActivePersons());	
		}
	}
	
	/**
	 * Add the card layout details
	 * @param filterID
	 * @param groupingFieldID
	 * @param sortingFieldID
	 * @param labelBeans
	 */
	private static void addCardLayout(Integer filterID, Integer groupingFieldID, Integer sortingFieldID, List<ILabelBean> labelBeans) {
		if (filterID!=null) {
			TNavigatorLayoutBean scrumBoardLayoutBean = new TNavigatorLayoutBean();
			scrumBoardLayoutBean.setFilterID(filterID);
			scrumBoardLayoutBean.setFilterType(QUERY_TYPE.SAVED);
			Integer layoutID = NavigatorLayoutBL.saveLayout(scrumBoardLayoutBean);
			if (layoutID!=null) {
				TCardGroupingFieldBean cardGroupingFieldBean=new TCardGroupingFieldBean();
				cardGroupingFieldBean.setNavigatorLayout(layoutID);
				cardGroupingFieldBean.setCardField(groupingFieldID);
				cardGroupingFieldBean.setSortField(sortingFieldID);
				cardGroupingFieldBean.setIsDescending(BooleanFields.FALSE_VALUE);
				cardGroupingFieldBean.setIsActiv(BooleanFields.TRUE_VALUE);
				Integer cardGroupingFieldID = CardViewBL.saveCardGroupingField(cardGroupingFieldBean);
				if (labelBeans!=null) {
					int counter = labelBeans.size();
					if (labelBeans.size()>10) {
						counter = 10;
					}
					for (int i = 0; i < counter; i++) {
						ILabelBean labelBean = labelBeans.get(i);
						if (labelBean!=null) {
							TCardFieldOptionBean cardFieldOptionBean=new TCardFieldOptionBean();
							cardFieldOptionBean.setGroupingField(cardGroupingFieldID);
							cardFieldOptionBean.setOptionID(labelBean.getObjectID());
							cardFieldOptionBean.setOptionPosition(i);
							cardFieldOptionBean.setOptionWidth(CardViewBL.DEFAULT_OPTION_WIDTH);
						}
					}
				}
			}	
		}
	}

	/**
	 * We forgot to add also the watcher at Migrate400To410.
	 * This will be added at Migrate410To412
	 * Although it will not appear implicitly in the menu items for users we should define it
	 * because otherwise the watcher part of MyItems dashboard does not work
	 */
	public static void addWatcherFilter() {
		LOGGER.info("Add watcher filters");
		FilterFacade filterFacade = FilterFacadeFactory.getInstance().getFilterFacade(
				TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER, true);
		// Get not closed stateIDs
		List<TStateBean> notClosedStateBeans = StatusBL.loadNotClosedStates();
		List<Integer> notClosedStateIDs = GeneralUtils
				.createIntegerListFromBeanList(notClosedStateBeans);
		Integer[] notClosedStatesArr = GeneralUtils
				.createIntegerArrFromCollection(notClosedStateIDs);
		ILabelBean watcherBean = filterFacade
				.getByKey(PREDEFINED_QUERY.WATCHER_ITEMS);
		if (watcherBean == null) {
			Connection cono = null;
			try {
				cono = InitDatabase.getConnection();
				Statement ostmt = cono.createStatement();
				cono.setAutoCommit(false);
				ostmt.executeUpdate(addPredefinedQueryClob(PREDEFINED_QUERY.WATCHER_ITEMS,
						PredefinedQueryBL.getWatcherItemsExpression(notClosedStatesArr)));
				ostmt.executeUpdate(addPredefinedQuery(
						PREDEFINED_QUERY.WATCHER_ITEMS, "I''m watcher"));
				cono.commit();
				cono.setAutoCommit(true);
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (cono != null) {
						cono.close();
					}
				} catch (Exception e) {
					LOGGER.info("Closing the connection failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
	
	/**
	 * Insert a query clob
	 * @param objectID
	 * @param filterExpression
	 * @return
	 */
	private static String addPredefinedQueryClob(Integer objectID,
			String filterExpression) {
		return "INSERT INTO TCLOB (OBJECTID, CLOBVALUE) VALUES (" + objectID
				+ ", '" + filterExpression + "')";
	}

	/**
	 * Inserts a predefined filter
	 * @param objectID
	 * @param filterLabel
	 * @return
	 */
	private static String addPredefinedQuery(Integer objectID,
			String filterLabel) {
		return "INSERT INTO TQUERYREPOSITORY (OBJECTID, LABEL, QUERYTYPE, REPOSITORYTYPE, QUERYKEY) VALUES ("
				+ objectID
				+ ", '"
				+ filterLabel
				+ "',"
				+ TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER
				+ ",  "
				+ CategoryBL.REPOSITORY_TYPE.PUBLIC + ", " + objectID + ")";
	}
	
	/**
	 * Inserts a predefined filter with view
	 * @param objectID
	 * @param filterLabel
	 * @param viewID
	 * @return
	 */
	private static String addPredefinedFilterWitView(Integer objectID,
			String filterLabel, String viewID) {
		return "INSERT INTO TQUERYREPOSITORY (OBJECTID, LABEL, QUERYTYPE, REPOSITORYTYPE, QUERYKEY, VIEWID) VALUES ("
				+ objectID
				+ ", '"
				+ filterLabel
				+ "',"
				+ TQueryRepositoryBean.QUERY_PURPOSE.TREE_FILTER
				+ ",  "
				+ CategoryBL.REPOSITORY_TYPE.PUBLIC + ", " + objectID + ", '"+ viewID + "')";
	}
}
