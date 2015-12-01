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


package com.aurel.track.exchange.msProject.exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.mpxj.ConstraintType;
import net.sf.mpxj.Day;
import net.sf.mpxj.Duration;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectCalendarHours;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Relation;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.TaskField;
import net.sf.mpxj.TaskType;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.writer.ProjectWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TMSProjectExchangeBean;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MsProjectTaskDAO;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeBL;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeBL.MSPROJECT_TIME_UNITS;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDOMHelper;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.CONSTRAINT_TYPE;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.DAY_TYPE;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.DAY_WORKING;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.IS_BASE_CALENEDAR;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.LAG_FORMAT;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.PREDECESSOR_CROSS_PROJECT;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.TASK_SUMMARY_TYPE;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.TASK_TYPE;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.TIMEPHASEDDATA_UNIT;
import com.aurel.track.exchange.msProject.importer.LinkLagBL;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WBSComparable;
import com.aurel.track.fieldType.runtime.system.text.SystemWBSRT;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.linkType.MsProjectLinkType;
import com.aurel.track.report.execute.ComputedValuesLoaderBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;

public class MsProjectExporterBL {
	private static final Logger LOGGER = LogManager.getLogger(MsProjectExporterBL.class);
	private static MsProjectTaskDAO msProjectTaskDAO = DAOFactory.getFactory().getMsProjectTaskDAO();

	private static boolean allowOvertimeBookings = false;
	private static double maxOvertimeHoursPerDay = 0;
	private static boolean allowOverdayBookings = true;
	private static boolean allowActualWorkOverlap = true;

	private static double EPSILON = Float.MIN_NORMAL;

	/**
	 * Gets the import file info depending on whether a previous import file is
	 * found
	 * 
	 * @param projectOrReleaseID
	 * @param locale
	 * @return
	 */
	static String getImportFileInfo(Integer projectOrReleaseID, Locale locale) {
		Integer entityID;
		int entityType;
		if (projectOrReleaseID != null) {
			if (projectOrReleaseID.intValue() < 0) {
				entityID = Integer.valueOf(-projectOrReleaseID.intValue());
				entityType = SystemFields.PROJECT;
			} else {
				entityID = projectOrReleaseID;
				entityType = SystemFields.RELEASE;
			}
			// initialize a new MsProjectImporterBean
			TMSProjectExchangeBean msProjectExchangeBean = MsProjectExchangeBL.getLastImportedMSProjectExchangeBean(entityID, entityType);
			if (msProjectExchangeBean == null) {
				return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.exportMSProject.lbl.noPreviousImport", locale);
			} else {
				List<String> parameters = new LinkedList<String>();
				parameters.add(msProjectExchangeBean.getFileName());
				parameters.add(DateTimeUtils.getInstance().formatGUIDateTime(msProjectExchangeBean.getLastEdit(), locale));
				if (msProjectExchangeBean.getChangedBy() != null) {
					TPersonBean lastImportedBy = LookupContainer.getPersonBean(msProjectExchangeBean.getChangedBy());
					if (lastImportedBy != null) {
						parameters.add(lastImportedBy.getName());
					}
				}
				return LocalizeUtil.getParametrizedString("admin.actions.exportMSProject.lbl.lastImportFile", parameters.toArray(), locale);
			}
		}
		return null;
	}

	/**
	 * Loads the sizes from a property file
	 * 
	 * @return
	 */
	private static void loadConfigFromFile() {
		Properties properties = new Properties();
		InputStream inputStream = MsProjectExporterBL.class.getResourceAsStream("MsProjectExporter.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			LOGGER.warn("Loading the MsProjectExporter.properties from classpath failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return;
		}
		try {
			allowOvertimeBookings = new Boolean(properties.getProperty("allowOvertimeBookings").trim()).booleanValue();
		} catch (Exception e) {
			LOGGER.warn("Loading the allowOvertimeBookings failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			String maxOvertimeHoursPerDayStr = properties.getProperty("maxOvertimeHoursPerDay").trim();
			if (maxOvertimeHoursPerDayStr != null && !"".equals(maxOvertimeHoursPerDayStr)) {
				maxOvertimeHoursPerDay = new Double(maxOvertimeHoursPerDayStr).doubleValue();
			}
		} catch (Exception e) {
			LOGGER.info("Loading the maxOvertimeHours failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			allowOverdayBookings = new Boolean(properties.getProperty("allowOverdayBookings").trim()).booleanValue();
		} catch (Exception e) {
			LOGGER.warn("Loading the allowOverdayBookings failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			allowActualWorkOverlap = new Boolean(properties.getProperty("allowActualWorkOverlap").trim()).booleanValue();
		} catch (Exception e) {
			LOGGER.warn("Loading the allowActualWorkOverlap failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

	}

	/**
	 * Export the MsProject data
	 * 
	 * @param msProjectExchangeDataStoreBean
	 * @param notClosed
	 * @return
	 */
	public static ProjectFile export(MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean, boolean notClosed, Integer personID) {
		loadConfigFromFile();
		Integer entityID = msProjectExchangeDataStoreBean.getEntityID();
		int entityType = msProjectExchangeDataStoreBean.getEntityType();
		Integer projectID = msProjectExchangeDataStoreBean.getProjectID();
		Integer issueType = msProjectExchangeDataStoreBean.getIssueType();

		TProjectBean projectBean = msProjectExchangeDataStoreBean.getProjectBean();
		String name = msProjectExchangeDataStoreBean.getName();
		ProjectFile project = msProjectExchangeDataStoreBean.getProject();
		
		Integer defaultTaskType = PropertiesHelper.getIntegerProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.DEFAULT_TASK_TYPE);
		if (defaultTaskType == null) {
			defaultTaskType = project.getProjectHeader().getDefaultTaskType().getValue();
			if (defaultTaskType == null) {
				defaultTaskType = Integer.valueOf(TASK_TYPE.FIXED_UNITS);
			}
		}
		Integer durationFormat = PropertiesHelper.getIntegerProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.DURATION_FORMAT);
		Double hoursPerWorkDay = ProjectBL.getHoursPerWorkingDay(projectBean.getObjectID());
		Map<Integer, Integer> resourceUIDToPersonIDMap = msProjectExchangeDataStoreBean.getResourceUIDToPersonIDMap();
		// int maxResourceID = getMaxInteger(resourceUIDToPersonIDMap.keySet());
		// can be that the same Genji person is mapped for more than one
		// MSProject resource
		Map<Integer, List<Integer>> personIDToResourceUIDMap = GeneralUtils.getInvertedDuplicatedValuesMap(resourceUIDToPersonIDMap);
		/**
		 * add/update the resources and calendars
		 */
		// get the potential resource persons, this
		// TODO get only the persons with no read restrictions for
		// TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME (old).
		List<TPersonBean> addExpenseRolePersons = PersonBL.getPersonsWithRightInProjectAndListType(projectID, issueType,
				AccessBeans.AccessFlagIndexes.MODIFYANYTASK, true, TPersonBean.PERSON_CATEGORY.ALL_PERSONS, null, true, true);
		List<TPersonBean> personsWithWork = PersonBL.getPersonsWithWorkInProject(entityID, entityType, issueType);
		List<Integer> addExpenseRolePersonIDs = GeneralUtils.createIntegerListFromBeanList(addExpenseRolePersons);
		List<Integer> personIDsWithWork = GeneralUtils.createIntegerListFromBeanList(personsWithWork);
		Set<Integer> personIDs = new HashSet<Integer>();
		personIDs.addAll(addExpenseRolePersonIDs);
		personIDs.addAll(personIDsWithWork);
		/*
		 * List<TPersonBean> personBeans = PersonBL.getPersonsByCategory(
		 * personIDs, TPersonBean.PERSON_CATEGORY.ALL_DIRECT, false, true,
		 * null);
		 */
		// TODO check
		// exportResourcesAndCalendars(personBeans, project,
		// personIDToResourceUIDMap,maxResourceID);

		/**
		 * get the Genji side data
		 */
		// 'Task' type workItems already existing in track+ left joined with
		// TMSProjectTask (ordered by outlineNumber and created)
		List<TWorkItemBean> existingTrackPlusWorkItemsList = MsProjectExchangeBL.getTaskWorkItemsForExport(entityID, entityType, notClosed);
		Map<Integer, TWorkItemBean> existingTrackPlusWorkItemsMap = GeneralUtils.createMapFromList(existingTrackPlusWorkItemsList);
		int[] workItemIDs = GeneralUtils.createIntArrFromSet(existingTrackPlusWorkItemsMap.keySet());
		LocalLookupContainer lookupContainer = ItemBL.getItemHierarchyContainer(existingTrackPlusWorkItemsList);

		Map<Integer, TMSProjectTaskBean> existingTrackPlusTasks = MsProjectExchangeBL.getMsProjectTasksForExport(entityID, entityType, notClosed);
		// the taskUID to workItemID mapping for existing tasks
		// the workItemID to taskUID mapping for existing tasks
		Map<Integer, Integer> workItemIDToTaskUIDMap = MsProjectExchangeBL.getWorkItemIDToTaskUIDMap(existingTrackPlusTasks);
		// the new workItems (without corresponding task elements) should be put
		// at the end
		// (after a database ASC ordering those with null values are the first
		// ones)
		List<TWorkItemBean> newWorkItemBeans = new ArrayList<TWorkItemBean>();
		for (Iterator<TWorkItemBean> iterator = existingTrackPlusWorkItemsList.iterator(); iterator.hasNext();) {
			TWorkItemBean workItemBean = iterator.next();
			if (!workItemIDToTaskUIDMap.containsKey(workItemBean.getObjectID())) {
				iterator.remove();
				newWorkItemBeans.add(workItemBean);
			}
		}
		existingTrackPlusWorkItemsList.addAll(newWorkItemBeans);
		// last imported tasks
		List<Task> lastImportedMsProjectTasksList = msProjectExchangeDataStoreBean.getTasks();
		Map<Integer, Task> lastImportedMsProjectTasksMap = MsProjectExchangeDOMHelper.createIntegerElementMapFromListForTasks(lastImportedMsProjectTasksList);
		// get the parent to children map
		Map<Integer, List<Integer>> parentToChildrenListMap = getParentToChildrenMap(existingTrackPlusWorkItemsList, existingTrackPlusTasks,
				workItemIDToTaskUIDMap);
		// get the last planned work for existing task workItems independently
		// of the date
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		List<TComputedValuesBean> plannedTimesList = ComputedValuesLoaderBL.loadByWorkItemKeys(existingTrackPlusWorkItemsList,
				AccessBeans.getFieldRestrictions(personID, existingTrackPlusWorkItemsList, fieldIDs, false), TComputedValuesBean.EFFORTTYPE.TIME,
				TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, null, false, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		Map<Integer, TComputedValuesBean> plannedTimesMap = new HashMap<Integer, TComputedValuesBean>();
		for (TComputedValuesBean computedValuesBean : plannedTimesList) {
			plannedTimesMap.put(computedValuesBean.getWorkitemKey(), computedValuesBean);
		}
		List<TCostBean> costBeanList = ExpenseBL.loadByWorkItemKeys(workItemIDs, null, null, null, null, true);
		Map<Integer, List<TCostBean>> costBeanMap = ExpenseBL.getCostBeansByWorkItemMap(costBeanList);
		// get the remaining work for existing task workItems
		Map<Integer, TActualEstimatedBudgetBean> remainingBudgetMap = RemainingPlanBL.loadRemainingBudgetMapByItemIDs(workItemIDs);
		// set project elements
		Date actualStartDate = getProjectActualStartDate(costBeanList);
		Date startDate = getProjectDate(existingTrackPlusWorkItemsList, true);
		if (actualStartDate != null) {
			if (startDate == null || startDate.after(actualStartDate)) {
				startDate = actualStartDate;
			}
		}
		String projectStartDate = MsProjectExchangeBL.formatDateTime(startDate);
		String projectEndDate = MsProjectExchangeBL.formatDateTime(getProjectDate(existingTrackPlusWorkItemsList, false));

		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (projectStartDate == null || projectStartDate == "") {
			projectStartDate = dateTimeFormat.format(new Date()).toString();
		}
		if (projectEndDate == null || projectEndDate == "") {
			projectEndDate = dateTimeFormat.format(new Date()).toString();
		}

		setProjectElements(project, name, projectStartDate, projectEndDate, projectBean, defaultTaskType, durationFormat, hoursPerWorkDay);
		Set<Integer> personsHavingWork = getPersonsWithWork(costBeanList);
		Map<Integer, Resource> UIDBasedResourceElementsMap = getUIDBasedResources(project);
		Map<Integer, ProjectCalendar> UIDBasedCalendarElementsMap = getUIDBasedCalendars(project);
		NumberFormat numberFormatTimeSpan = new DecimalFormat("00");
		/**
		 * get the working times for base calendars
		 */
		Map<Integer, Map<String, List<String[]>>> calendarUIDBasedBaseCalendarExceptionWorkingTimes = new HashMap<Integer, Map<String, List<String[]>>>();
		Map<Integer, Map<Integer, List<String[]>>> calendarUIDBasedBaseCalendarWeekDayWorkingTimes = new HashMap<Integer, Map<Integer, List<String[]>>>();

		getUIDBasedBaseCalendarWorkingTimes(project, allowOvertimeBookings, maxOvertimeHoursPerDay, numberFormatTimeSpan,
				calendarUIDBasedBaseCalendarExceptionWorkingTimes, calendarUIDBasedBaseCalendarWeekDayWorkingTimes);
		/**
		 * get the person specific working times
		 */
		Map<Integer, Integer> personIDToBaseCalendar = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<String[]>>> personBasedCalendarExceptionWorkingTimes = new HashMap<Integer, Map<String, List<String[]>>>();
		Map<Integer, Map<Integer, List<String[]>>> personBasedCalendarWeekDayWorkingTimes = new HashMap<Integer, Map<Integer, List<String[]>>>();
		getPersonIDBasedBaseCalendarWorkingTimes(personsHavingWork, personIDToResourceUIDMap, UIDBasedResourceElementsMap, UIDBasedCalendarElementsMap,
				allowOvertimeBookings, maxOvertimeHoursPerDay, numberFormatTimeSpan, personIDToBaseCalendar, personBasedCalendarExceptionWorkingTimes,
				personBasedCalendarWeekDayWorkingTimes);
		IDCounter idCounter = new IDCounter();
		// initialize the taskUID with the maximal existing value
		idCounter.setTaskUID(getMaxInteger(existingTrackPlusTasks.keySet()));
		int currentLevel = -1;

		for (Iterator it = existingTrackPlusWorkItemsList.iterator(); it.hasNext();) {
			TWorkItemBean workItemBean = (TWorkItemBean) it.next();
			Integer parentID = workItemBean.getSuperiorworkitem();
			// first add those at the upper level (without task parents)
			if (parentID == null || !existingTrackPlusWorkItemsMap.containsKey(parentID))
				listWorkItems(workItemBean, new OutlineStructure(++currentLevel), idCounter, existingTrackPlusWorkItemsMap, parentToChildrenListMap, project,
						workItemIDToTaskUIDMap, lastImportedMsProjectTasksMap, existingTrackPlusTasks, plannedTimesMap, costBeanMap, remainingBudgetMap,
						hoursPerWorkDay, defaultTaskType, durationFormat, calendarUIDBasedBaseCalendarExceptionWorkingTimes,
						calendarUIDBasedBaseCalendarWeekDayWorkingTimes, lookupContainer);
		}
		// remove the tasks not any more present in the track+ side but present
		// in the last imported file
		// (if a Task type workItem was closed or deleted or changed to other
		// issueType or mover to other project or...
		// the task should be removed from the last exported document together
		// with all assignments to that task)
		Set<Integer> removedTaskIDs = new HashSet<Integer>();
		for (Iterator<Integer> itrLastImportedTask = lastImportedMsProjectTasksMap.keySet().iterator(); itrLastImportedTask.hasNext();) {
			Integer taskID = itrLastImportedTask.next();
			if (!existingTrackPlusTasks.containsKey(taskID)) {
				Task lastImportedTaskElement = lastImportedMsProjectTasksMap.get(taskID);
				if (MsProjectExchangeDOMHelper.UIDIsValidTask(lastImportedTaskElement)) {
					// TODO Possible probelem in old xml version it was:
					// lastImportedTaskElement.getParentNode().removeChild(lastImportedTaskElement);
					removedTaskIDs.add(taskID);
				}

			}
		}
		exportTaskDependencyLinks(project, workItemIDToTaskUIDMap);
		exportAssignments(project, existingTrackPlusWorkItemsMap, costBeanList, workItemIDToTaskUIDMap, personIDToResourceUIDMap, hoursPerWorkDay,
				removedTaskIDs, allowOvertimeBookings, maxOvertimeHoursPerDay, allowOverdayBookings, allowActualWorkOverlap,
				calendarUIDBasedBaseCalendarExceptionWorkingTimes, calendarUIDBasedBaseCalendarWeekDayWorkingTimes, personBasedCalendarExceptionWorkingTimes,
				personBasedCalendarWeekDayWorkingTimes, personIDToBaseCalendar);
		try {
			OutputStream out = new ByteArrayOutputStream();
			ProjectWriter writer = new MSPDIWriter();
			writer.write(msProjectExchangeDataStoreBean.getProject(), out);
			String content = out.toString();
			MsProjectExchangeBL.saveMSProjectExportFile(msProjectExchangeDataStoreBean, content);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		setTasksOrderByWbs(project);
		setTrackPlusAliasForIdMapping(project);
		updatingWorkBudget(project, costBeanList);
		return project;
	}

	private static void updatingWorkBudget(ProjectFile project, List<TCostBean> costBeanList) {
		Map<Integer, TCostBean> workItemIDToCostBean = new HashMap<Integer, TCostBean>();
		int[] workItemKeys = new int[project.getAllTasks().size()];
		int i = 0;
		for (TCostBean cBean : costBeanList) {
			workItemIDToCostBean.put(cBean.getWorkItemID(), cBean);
		}
		for (Task aTask : project.getAllTasks()) {
			Integer UIDTmp = (Integer) aTask.getFieldByAlias("trackPlusId");
			if (UIDTmp == null) {
				UIDTmp = aTask.getUniqueID();
			}
			if (workItemIDToCostBean.get(UIDTmp) != null) {
				aTask.setActualDuration(Duration.getInstance(workItemIDToCostBean.get(UIDTmp).getHours() / 8 * aTask.getResourceAssignments().size(),
						TimeUnit.HOURS));
			}
			workItemKeys[i] = UIDTmp;
			i++;
			aTask.setActualStart(null);
			aTask.setActualFinish(null);
			// aTask.setActualDuration(Duration.getInstance(2, TimeUnit.HOURS));
		}
		// BudgetDAO plannedDao = DAOFactory.getFactory().getBudgetDAO();
		// List<TBudgetBean>budgetValuesList =
		// plannedDao.loadByWorkItemKeys(workItemKeys, null, false, null, null);
		// Map<Integer, TBudgetBean> workItemKeyToBudgetBean = new
		// HashMap<Integer, TBudgetBean>();
		// for(TBudgetBean budget : budgetValuesList) {
		// workItemKeyToBudgetBean.put(budget.getWorkItemID(), budget);
		// }
		// for(Task aTask : project.getAllTasks()) {
		// Integer UIDTmp = (Integer)aTask.getFieldByAlias("trackPlusId");
		// if(UIDTmp == null) {
		// UIDTmp = aTask.getUniqueID();
		// }
		// if(workItemKeyToBudgetBean.get(UIDTmp) != null) {
		// aTask.setBu
		// }
		// }
	}

	private static void setTrackPlusAliasForIdMapping(ProjectFile project) {
		project.setTaskFieldAlias(TaskField.ENTERPRISE_NUMBER1, "trackPlusId");
		for (Task task : project.getAllTasks()) {
			task.setFieldByAlias("trackPlusId", task.getUniqueID());
		}
	}

	private static void setTasksOrderByWbs(ProjectFile project) {
		List<WBSComparable> comparableList = new LinkedList<WBSComparable>();
		for (Task task : project.getAllTasks()) {
			WBSComparable wbsComparable = new WBSComparable(task.getOutlineNumber(), "\\.", task.getUniqueID());
			comparableList.add(wbsComparable);
		}
		Collections.sort(comparableList);
		Map<Integer, Integer> tasksOrder = new HashMap<Integer, Integer>();
		int i = 1;
		for (WBSComparable wbsComparable : comparableList) {
			tasksOrder.put(wbsComparable.getWorkItemID(), i);
			i++;
		}
		for (Task task : project.getAllTasks()) {
			task.setID(tasksOrder.get(task.getUniqueID()));
		}
	}

	/**
	 * Import (add/update/delete) the task dependency links Update or delete a
	 * link only if his lastEdit is before the MSProject import file creation
	 * 
	 * @param msProjectImporterBean
	 * @param UIDToWorkItemID
	 * @param importCounts
	 */
	private static void exportTaskDependencyLinks(ProjectFile project, Map<Integer, Integer> workItemIDToTaskUIDMap) {
		List<Task> exportedTasksList = project.getAllTasks();

		Map<Integer, Task> UIDBasedTasks = MsProjectExchangeDOMHelper.getSubelementBasedElementMapTask(exportedTasksList);

		Map<Integer, Map<Integer, Relation>> importedDependentToPredecessorLinksMap = MsProjectExchangeDOMHelper
				.getDependentPredecessorLinksMap(exportedTasksList);

		// get all msProject specific links from track+
		MsProjectLinkType msProjectLinkType = MsProjectLinkType.getInstance();
		List<TWorkItemLinkBean> existingTrackPlusWorkItemLinks = ItemLinkBL.loadUnidirectionalLinksByWorkItemsAndLinkTypes(
				GeneralUtils.createListFromSet(workItemIDToTaskUIDMap.keySet()), LinkTypeBL.getLinkTypesByPluginClass(msProjectLinkType),
				msProjectLinkType.getPossibleDirection());
		Set<Integer> linkedItemIDs = new HashSet<Integer>();
		for (TWorkItemLinkBean workItemLinkBean : existingTrackPlusWorkItemLinks) {
			Integer predLink = workItemLinkBean.getLinkPred();
			if (predLink != null) {
				linkedItemIDs.add(predLink);
			}
		}
		List<TWorkItemBean> linkedItemBeans = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(linkedItemIDs));
		Map<Integer, Integer> itemIDToProjectIDMap = new HashMap<Integer, Integer>();
		Map<Integer, Double> projectWorkingHoursMap = new HashMap<Integer, Double>();
		for (TWorkItemBean workItemBean : linkedItemBeans) {
			Integer workItemID = workItemBean.getObjectID();
			Integer projectID = workItemBean.getProjectID();
			itemIDToProjectIDMap.put(workItemID, projectID);
			Double hoursPerDay = projectWorkingHoursMap.get(projectID);
			if (hoursPerDay == null) {
				hoursPerDay = ProjectBL.getHoursPerWorkingDay(projectID);
				if (hoursPerDay == null) {
					hoursPerDay = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
				}
				projectWorkingHoursMap.put(projectID, hoursPerDay);
			}
		}

		for (TWorkItemLinkBean workItemLinkBean : existingTrackPlusWorkItemLinks) {
			Integer predLink = workItemLinkBean.getLinkPred();
			Integer succLink = workItemLinkBean.getLinkSucc();
			Integer type = workItemLinkBean.getIntegerValue1();
			Double hoursPerWorkday = null;
			Integer projectID = itemIDToProjectIDMap.get(predLink);
			if (projectID != null) {
				hoursPerWorkday = projectWorkingHoursMap.get(projectID);
			}
			if (hoursPerWorkday == null) {
				hoursPerWorkday = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
			}
			// Double hoursPerWorkday =
			// MsProjectLinkTypeBL.getHoursPerWorkingDayForWorkItem(workItemLinkBean.getLinkPred());
			Double convertedLinkLag = LinkLagBL.getUILinkLagFromMinutes(workItemLinkBean.getLinkLag(), workItemLinkBean.getLinkLagFormat(), hoursPerWorkday);

			// Integer dependentTaskUID = workItemIDToTaskUIDMap.get(predLink);
			Integer dependentTaskUID = predLink;
			// Integer predecessorUID = workItemIDToTaskUIDMap.get(succLink);
			Integer predecessorUID = succLink;
			if (dependentTaskUID != null && predecessorUID != null) {
				Map<Integer, Relation> predecessorLinksMap = importedDependentToPredecessorLinksMap.get(dependentTaskUID);
				Relation predecessorLinkElement = null;
				if (predecessorLinksMap != null && predecessorLinksMap.size() > 0) {
					// predecessorLink existed already in the previous import
					// get the predecessor element but also remove from map
					// because later all remaining predecessors
					// (representing links deleted from Genji) should be
					// removed from the document dom
					predecessorLinkElement = predecessorLinksMap.remove(predecessorUID);
				}
				if (predecessorLinkElement == null) {
					// new link
					Task dependentTaskElement = UIDBasedTasks.get(dependentTaskUID);
					if (dependentTaskElement != null) {
						// TODO This append is in the certain place? (child
						// order is important?)

						Integer msLagFormat = convertTrackLagToMsProject(workItemLinkBean.getLinkLagFormat());
						if (msLagFormat != -1) {
							UIDBasedTasks.get(predecessorUID).addPredecessor(dependentTaskElement, RelationType.getInstance(type),
									Duration.getInstance(convertedLinkLag, TimeUnit.getInstance(msLagFormat)));
						} else {
							UIDBasedTasks.get(predecessorUID).addPredecessor(dependentTaskElement, RelationType.getInstance(type),
									Duration.getInstance(0, TimeUnit.MINUTES));
						}

					}
				}

				// TODO The MPXJ API's Relation class doesn't have UniqueID
				// field.
				// MsProjectExchangeDOMHelper.setChildTextByName(predecessorLinkElement,
				// PREDECESSOR_ELEMENTS.PredecessorUID,
				// predecessorUID.toString(), true);

				// TODO Unused in MPXJ, because if the predecessorLinkElement !=
				// null that means this is a previous import and the predecessor
				// is already set.
				/*
				 * if (type != null) {
				 * MsProjectExchangeDOMHelper.setChildTextByName
				 * (predecessorLinkElement, COMMON_ELEMENTS.Type,
				 * type.toString(), true); }
				 */

				Integer crossProject = Integer.valueOf(PREDECESSOR_CROSS_PROJECT.CROSS_PROJECT);
				if (workItemIDToTaskUIDMap.containsKey(succLink)) {
					// from MS Project point of view a Task is cross project
					// event if it is in another
					// release of the same project because it would mean another
					// MS Project file
					crossProject = Integer.valueOf(PREDECESSOR_CROSS_PROJECT.NOT_CROSS_PROJECT);
				}

				// TODO The MPXJ API Relation doesn't have cross project
				// attribute.
				/*
				 * MsProjectExchangeDOMHelper.setChildTextByName(
				 * predecessorLinkElement, PREDECESSOR_ELEMENTS.CrossProject,
				 * crossProject.toString(), false);
				 */

				// TODO MPXJ Relation doesn't has setter methods for: link lag,
				// cross project, lag format.
				/*
				 * if (linkLag!=null) {
				 * MsProjectExchangeDOMHelper.setChildTextByName
				 * (predecessorLinkElement, PREDECESSOR_ELEMENTS.LinkLag,
				 * Integer.valueOf(linkLag.intValue()).toString(), true);
				 * Element crossProjectNameElement =
				 * MsProjectExchangeDOMHelper.getChildByName(
				 * predecessorLinkElement,
				 * PREDECESSOR_ELEMENTS.CrossProjectName); if
				 * (crossProjectNameElement!=null) {
				 * predecessorLinkElement.removeChild(crossProjectNameElement);
				 * }
				 * 
				 * } if (lagFormat!=null) {
				 * MsProjectExchangeDOMHelper.setChildTextByName
				 * (predecessorLinkElement, PREDECESSOR_ELEMENTS.LagFormat,
				 * lagFormat.toString(), true); }
				 */
			}
		}
		// all links which remained in this map are present only in last
		// imported msProject file and not in track+: they will be removed
		if (importedDependentToPredecessorLinksMap.size() > 0) {
			Iterator<Integer> itrPredLinks = importedDependentToPredecessorLinksMap.keySet().iterator();
			while (itrPredLinks.hasNext()) {
				Integer predLink = itrPredLinks.next();
				Map<Integer, Relation> succMap = importedDependentToPredecessorLinksMap.get(predLink);
				if (succMap != null) {
					for (int i = 0; i < succMap.size(); i++) {
						// TODO Actually without remove the result is correct,
						// because it was a trick with building the xml,
						// relation removing unused in MPXJ.
						// Relation predecessorElement = succMap.get(i);
						// predecessorElement.getParentNode().removeChild(predecessorElement);
					}
				}
			}
		}
	}

	/**
	 * Get the UID based calendars map
	 * 
	 * @param document
	 * @return
	 */
	public static Map<Integer, Resource> getUIDBasedResources(ProjectFile project) {
		List<Resource> resourceElementList = project.getAllResources();
		return MsProjectExchangeDOMHelper.getSubelementBasedElementMapResource(resourceElementList);
	}

	/**
	 * Get the UID based tasks
	 * 
	 * @param document
	 * @return
	 */
	private static Map<Integer, Task> getUIDBasedTasks(ProjectFile projectElement) {
		List<Task> calendars = projectElement.getAllTasks();
		return MsProjectExchangeDOMHelper.getSubelementBasedElementMapTask(calendars);
	}

	/**
	 * Get the UID based calendars map
	 * 
	 * @param document
	 * @return
	 */
	private static Map<Integer, ProjectCalendar> getUIDBasedCalendars(ProjectFile project) {
		List<ProjectCalendar> calendars = project.getCalendars();
		return MsProjectExchangeDOMHelper.getSubelementBasedElementMap(calendars);
	}

	/**
	 * Get the calendarUID based exceptionWorkingTimes and weekDayWorkingTimes
	 * for base calendars
	 * 
	 * @param document
	 * @param calendarUIDBasedBaseCalendarExceptionWorkingTimes
	 * @param calendarUIDBasedBaseCalendarWeekDayWorkingTimes
	 */
	private static void getUIDBasedBaseCalendarWorkingTimes(ProjectFile project, boolean allowOvertimeBookings, double maxOvertimeHoursPerDay,
			NumberFormat numberFormatTimeSpan, Map<Integer, Map<String, List<String[]>>> calendarUIDBasedBaseCalendarExceptionWorkingTimes,
			Map<Integer, Map<Integer, List<String[]>>> calendarUIDBasedBaseCalendarWeekDayWorkingTimes) {
		List<ProjectCalendar> calendars = project.getCalendars();
		if (calendars != null) {
			for (int i = 0; i < calendars.size(); i++) {
				ProjectCalendar calendarElement = calendars.get(i);

				Integer UID = calendarElement.getUniqueID();
				// TODO isBaseCalendar checking correct?
				// Integer isBaseCalendar =
				// MsProjectExchangeDOMHelper.getSubelementInteger(calendarElement,
				// CALENDAR_ELEMENTS.IsBaseCalendar);
				Integer isBaseCalendar;
				if (calendarElement.isDerived()) {
					isBaseCalendar = IS_BASE_CALENEDAR.NOT_BASE;
				} else {
					isBaseCalendar = IS_BASE_CALENEDAR.BASE;
				}
				if (isBaseCalendar != null && isBaseCalendar.intValue() == IS_BASE_CALENEDAR.BASE) {
					Map<String, List<String[]>> baseCalendarExceptionWorkingTimes = new HashMap<String, List<String[]>>();
					Map<Integer, List<String[]>> baseCalendarWeekDayWorkingTimes = new HashMap<Integer, List<String[]>>();

					loadPersonWorkingTimesForCalendar(calendarElement, baseCalendarExceptionWorkingTimes, baseCalendarWeekDayWorkingTimes,
							allowOvertimeBookings, maxOvertimeHoursPerDay, numberFormatTimeSpan);
					calendarUIDBasedBaseCalendarExceptionWorkingTimes.put(UID, baseCalendarExceptionWorkingTimes);
					calendarUIDBasedBaseCalendarWeekDayWorkingTimes.put(UID, baseCalendarWeekDayWorkingTimes);
				}
			}
		}
	}

	private static void getPersonIDBasedBaseCalendarWorkingTimes(Set<Integer> personsWithWork, Map<Integer, List<Integer>> personIDToResourceUIDMap,
			Map<Integer, Resource> UIDBasedResourceElementsMap, Map<Integer, ProjectCalendar> UIDBasedCalendarElementsMap, boolean allowOvertimeBookings,
			double maxOvertimeHoursPerDay, NumberFormat numberFormatTimeSpan,
			// output parameters
			Map<Integer, Integer> personIDToBaseCalendar, Map<Integer, Map<String, List<String[]>>> personBasedCalendarExceptionWorkingTimes,
			Map<Integer, Map<Integer, List<String[]>>> personBasedCalendarWeekDayWorkingTimes) {
		if (personsWithWork.size() > 0) {
			for (Integer personID : personsWithWork) {
				List<Integer> resourceUIDForPersonList = personIDToResourceUIDMap.get(personID);
				if (resourceUIDForPersonList == null || resourceUIDForPersonList.isEmpty()) {
					// unknown person, should never happen
					continue;
				}
				Integer resourceUID = resourceUIDForPersonList.get(0);
				Resource resourceElement = UIDBasedResourceElementsMap.get(resourceUID);
				if (resourceElement != null) {
					// TODO Resource element doesn't have getCalendarUID getter
					// method.
					Integer personCalendarUID = resourceElement.getUniqueID();
					if (personCalendarUID != null) {
						ProjectCalendar personCalendarElement = UIDBasedCalendarElementsMap.get(personCalendarUID);
						Integer baseCalendarUID = personCalendarElement.getUniqueID();
						personIDToBaseCalendar.put(personID, baseCalendarUID);
						Map<String, List<String[]>> personSpecificExceptionWorkingTimes = new HashMap<String, List<String[]>>();
						Map<Integer, List<String[]>> personSpecificWeekDayWorkingTimes = new HashMap<Integer, List<String[]>>();
						loadPersonWorkingTimesForCalendar(personCalendarElement, personSpecificExceptionWorkingTimes, personSpecificWeekDayWorkingTimes,
								allowOvertimeBookings, maxOvertimeHoursPerDay, numberFormatTimeSpan);
						personBasedCalendarExceptionWorkingTimes.put(personID, personSpecificExceptionWorkingTimes);
						personBasedCalendarWeekDayWorkingTimes.put(personID, personSpecificWeekDayWorkingTimes);
					}
				}
			}
		}
	}

	/**
	 * Gets the most specific workingTime intervals for a specific day
	 * 
	 * @param personSpecificExceptionWorkingTimes
	 * @param personSpecificWeekDayWorkingTimes
	 * @param baseCalendarExceptionWorkingTimes
	 * @param baseCalendarWeekDayWorkingTimes
	 * @param effortDate
	 * @param allowOvertimeBookings
	 * @return
	 */
	private static List<String[]> getWorkingTimeForDay(Map<String, List<String[]>> personSpecificExceptionWorkingTimes,
			Map<Integer, List<String[]>> personSpecificWeekDayWorkingTimes, Map<String, List<String[]>> baseCalendarExceptionWorkingTimes,
			Map<Integer, List<String[]>> baseCalendarWeekDayWorkingTimes, Date effortDate) {
		String effortDateStr = MsProjectExchangeBL.formatDate(effortDate);
		List<String[]> workingTimeList = null;
		if (personSpecificExceptionWorkingTimes != null) {
			workingTimeList = personSpecificExceptionWorkingTimes.get(effortDateStr);
		}
		if (workingTimeList == null) {
			if (baseCalendarExceptionWorkingTimes != null) {
				workingTimeList = baseCalendarExceptionWorkingTimes.get(effortDateStr);
			}
			if (workingTimeList == null) {
				Calendar calendar = Calendar.getInstance();
				;
				calendar.setTime(effortDate);
				Integer weekDay = Integer.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
				if (personSpecificWeekDayWorkingTimes != null) {
					workingTimeList = personSpecificWeekDayWorkingTimes.get(weekDay);
				}
				if (workingTimeList == null) {
					if (baseCalendarWeekDayWorkingTimes != null) {
						workingTimeList = baseCalendarWeekDayWorkingTimes.get(weekDay);
					}
				}
			}
		}
		return workingTimeList;
	}

	/**
	 * Get the working periods for a calendar element: loads the exception days
	 * map and the weekDay map with the corresponding workingTime periods
	 * 
	 * @param calendarElement
	 * @param exceptionWorkingTimes
	 * @param weekDayWorkingTimes
	 */
	private static void loadPersonWorkingTimesForCalendar(ProjectCalendar calendarElement, Map<String, List<String[]>> exceptionWorkingTimes,
			Map<Integer, List<String[]>> weekDayWorkingTimes, boolean allowOvertimeBookings, double maxOvertimeHoursPerDay, NumberFormat numberFormatTimeSpan) {
		Calendar calendar = Calendar.getInstance();
		if (calendarElement != null) {
			Day[] weekDays = Day.values();
			if (weekDays != null && weekDays.length > 0) {
				for (int i = 0; i < weekDays.length; i++) {
					if (weekDays[i].getValue() == DAY_WORKING.WORKING) {
						ProjectCalendarHours hours = calendarElement.getCalendarHours(weekDays[i]);
						List<String[]> fromTimeToTimePairs = new ArrayList<String[]>();
						if (hours != null) {
							for (int j = 0; j < hours.getRangeCount(); j++) {
								String fromTime = hours.getRange(j).getStart().toString();
								String toTime = hours.getRange(j).getEnd().toString();
								if (fromTime != null && toTime != null) {
									fromTimeToTimePairs.add(new String[] { fromTime, toTime });
								}
							}
						}
						if (allowOvertimeBookings && fromTimeToTimePairs != null && fromTimeToTimePairs.size() > 0) {
							String[] lastWorkingPeriod = fromTimeToTimePairs.get(fromTimeToTimePairs.size() - 1);
							String lastWorkingPeriodEnd = lastWorkingPeriod[1];
							String overtimeWorkingPeriodEnd = MsProjectExchangeBL.addHoursToCalendarTime(lastWorkingPeriodEnd, maxOvertimeHoursPerDay,
									numberFormatTimeSpan);
							// if official "homego" time is already midnight do
							// not add it again
							if (!overtimeWorkingPeriodEnd.equals(lastWorkingPeriodEnd)) {
								fromTimeToTimePairs.add(new String[] { lastWorkingPeriodEnd, overtimeWorkingPeriodEnd });
							}
						}
						// exception date
						// TODO MPXJ returnes dates otherwise this part is
						// unuesd.
						// Element timePeriodElement =
						// MsProjectExchangeDOMHelper.getChildByName(weekDayElement,
						// WEEKDAY_ELEMENTS.TimePeriod);
						if (weekDays[i].getValue() == DAY_TYPE.Exception) {// &&
																			// timePeriodElement!=null)
																			// {
							/*
							 * Date fromDate =
							 * MsProjectExchangeDOMHelper.getSubelementDate
							 * (timePeriodElement,
							 * TIMEPERIOD_ELEMENTS.FromDate); Date toDate =
							 * MsProjectExchangeDOMHelper
							 * .getSubelementDate(timePeriodElement,
							 * TIMEPERIOD_ELEMENTS.ToDate);
							 * calendar.setTime(fromDate);
							 * CalendarUtil.clearTime(calendar); while
							 * (calendar.getTime().before(toDate)) {
							 * exceptionWorkingTimes
							 * .put(MsProjectExchangeBL.formatDate
							 * (calendar.getTime()), fromTimeToTimePairs);
							 * calendar.add(Calendar.DATE, 1); }
							 */
						} else {
							// weekday type
							if (weekDays[i].getValue() > DAY_TYPE.Exception) {
								weekDayWorkingTimes.put(weekDays[i].getValue(), fromTimeToTimePairs);
							}
						}

					}
				}
			}
		}
	}

	private static Integer getBestResourceID(List<Integer> personIDs, Map<Integer, List<Integer>> personIDToResourceUIDMap) {
		if (personIDs != null && personIDToResourceUIDMap != null) {
			for (Iterator<Integer> iterator = personIDs.iterator(); iterator.hasNext();) {
				Integer personID = iterator.next();
				if (personID != null) {
					List<Integer> resoureUIDList = personIDToResourceUIDMap.get(personID);
					if (resoureUIDList != null && resoureUIDList.size() > 0) {
						return resoureUIDList.get(0);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Export the assignments to actual work
	 * 
	 * @param document
	 * @param assignmentElementTemplate
	 * @param timephasedDataElementTemplate
	 * @param existingTrackPlusWorkItemsMap
	 * @param allCostBeansList
	 * @param workItemIDToTaskUIDMap
	 * @param personIDToResourceUIDMap
	 * @param hoursPerWorkday
	 * @param removedTaskIDs
	 * @param allowOvertimeBookings
	 * @param maxOvertimeHoursPerDay
	 * @param allowOverdayBookings
	 * @param allowActualWorkOverlap
	 * @param allowDayRestartIfOverlappedActualWork
	 */
	private static void exportAssignments(ProjectFile project, Map<Integer, TWorkItemBean> existingTrackPlusWorkItemsMap, List<TCostBean> allCostBeansList,
			Map<Integer, Integer> workItemIDToTaskUIDMap, Map<Integer, List<Integer>> personIDToResourceUIDMap, Double hoursPerWorkday,
			Set<Integer> removedTaskIDs, boolean allowOvertimeBookings, double maxOvertimeHoursPerDay, boolean allowOverdayBookings,
			boolean allowActualWorkOverlap,/*
											 * boolean
											 * allowDayRestartIfOverlappedActualWork
											 * ,
											 */
			Map<Integer, Map<String, List<String[]>>> calendarUIDBasedBaseCalendarExceptionWorkingTimes,
			Map<Integer, Map<Integer, List<String[]>>> calendarUIDBasedBaseCalendarWeekDayWorkingTimes,
			Map<Integer, Map<String, List<String[]>>> personBasedCalendarExceptionWorkingTimes,
			Map<Integer, Map<Integer, List<String[]>>> personBasedCalendarWeekDayWorkingTimes, Map<Integer, Integer> personIDToBaseCalendar) {
		// TODO unused MPXJ
		/*
		 * Element assignmentsElement =
		 * MsProjectExchangeDOMHelper.getChildByName(projectElement,
		 * PROJECT_ELEMENTS.Assignments); Element assignmentsElement =
		 * project.getAss() if (assignmentsElement==null) { assignmentsElement =
		 * document.createElement(PROJECT_ELEMENTS.Assignments);
		 * projectElement.appendChild(assignmentsElement); }
		 */
		// get last imported assignments
		// List<Element> importedAssignmentsList =
		// MsProjectExchangeDOMHelper.getElementList(
		// projectElement, PROJECT_ELEMENTS.Assignments,
		// PROJECT_SUBELEMENTS.Assignment);
		List<ResourceAssignment> importedAssignmentsList = project.getAllResourceAssignments();
		/*
		 * for(int i = 0; i < importedAssignmentsList.size(); i++) {
		 * ResourceAssignment tmp = importedAssignmentsList.get(i);
		 * tmp.setStart(tmp.getTask().getStart());;
		 * tmp.setFinish(tmp.getTask().getFinish()); tmp.setActualStart(null);
		 * tmp.setActualFinish(null); tmp.setActualWork(null); }
		 */

		if (removedTaskIDs != null && removedTaskIDs.size() > 0) {
			// remove the assignments associated with removed tasks
			for (int i = 0; i < importedAssignmentsList.size(); i++) {
				ResourceAssignment assignmentElement = importedAssignmentsList.get(i);
				Integer taskID = assignmentElement.getTaskUniqueID();
				if (removedTaskIDs.contains(taskID)) {
					importedAssignmentsList.remove(i);
				}
			}
		}
		Map<Integer, ResourceAssignment> UIDBasedAssignmentsMap = MsProjectExchangeDOMHelper.getSubelementBasedElementMapAssignment(importedAssignmentsList);
		int maxAssignmentUID = getMaxInteger(UIDBasedAssignmentsMap.keySet());

		Map<Integer, Map<Integer, ResourceAssignment>> importedTaskUIDToResourceUIDToAssignmentsMap = MsProjectExchangeDOMHelper
				.getAssignmentsMap(importedAssignmentsList);
		Map<Integer, Task> UIDBasedTaskElementMap = getUIDBasedTasks(project);
		String emptyTimeSpan = MsProjectExchangeBL.getTimeSpanFromDouble(0.0);
		// NumberFormat numberFormatTimeSpan = new DecimalFormat("00");

		/**
		 * Add empty assignment: if a workItem has no cost in Genji and it was
		 * not imported before from MSPRoject a default assignment (with
		 * actualWork=0 and work=total task budget and remainingWork=remaining
		 * task budget) should still be added to the task, to assign the
		 * workItem's responsible as resource to the task (otherwise the task
		 * will have no duration)
		 */
		Map<Integer, List<TCostBean>> worksByWorkItemMap = getWorksByWorkItem(allCostBeansList);
		Iterator<Integer> itrExistingWorkItems = existingTrackPlusWorkItemsMap.keySet().iterator();
		while (itrExistingWorkItems.hasNext()) {
			Integer workItemID = itrExistingWorkItems.next();
			TWorkItemBean workItemBean = existingTrackPlusWorkItemsMap.get(workItemID);
			Integer taskUID = workItemIDToTaskUIDMap.get(workItemID);
			if (!worksByWorkItemMap.containsKey(workItemID) && !importedTaskUIDToResourceUIDToAssignmentsMap.containsKey(taskUID)) {
				// it has no actualWork and was not imported before: it will not
				// be processed in the "existing costs" loop below
				List<Integer> personList = new ArrayList<Integer>();
				personList.add(workItemBean.getResponsibleID());
				personList.add(workItemBean.getOwnerID());
				personList.add(workItemBean.getOriginatorID());
				Integer resourceID = getBestResourceID(personList, personIDToResourceUIDMap);
				if (resourceID != null && taskUID != null) {
					Task taskElement = UIDBasedTaskElementMap.get(taskUID);
					if (taskElement != null) {
						ResourceAssignment assignmentElement = new ResourceAssignment(project);
						ResourceAssignment existing = importedTaskUIDToResourceUIDToAssignmentsMap.get(taskUID).get(resourceID);
						assignmentElement.setUniqueID(++maxAssignmentUID);
						assignmentElement.setTaskUniqueID(taskUID);
						assignmentElement.setResourceUniqueID(resourceID);
						// TODO set emptyTimeSpan string as duration not
						// possible in MPXJ
						// assignmentElement.setActualWork(Duration.emptyTimeSpan);
						// get the values directly from the task: the Work and
						// RemainingWork are probably equal
						// String work =
						// MsProjectExchangeDOMHelper.getSubelementText(taskElement,
						// COMMON_ELEMENTS.Work);
						if (importedTaskUIDToResourceUIDToAssignmentsMap.get(resourceID) != null) {
							assignmentElement.setWork(existing.getWork());
							assignmentElement.setRemainingWork(existing.getRemainingWork());
							assignmentElement.setStart(existing.getStart());
							assignmentElement.setStart(existing.getFinish());
						}
					}
				}
			}
		}

		/**
		 * The actual booked calendar for a certain date for a certain user
		 */
		Map<Integer, Map<String, Calendar>> effortDateToCalBookedForPersonMap = new HashMap<Integer, Map<String, Calendar>>();
		/**
		 * The exhausted effort dates for a certain user
		 */
		Map<Integer, Map<String, Boolean>> effortDateExhaustedForPersonMap = new HashMap<Integer, Map<String, Boolean>>();
		// loop through the existing costs in track+ based on workItemID and
		// personID
		Iterator<Integer> itrWorksByWorkItemIDMap = worksByWorkItemMap.keySet().iterator();
		while (itrWorksByWorkItemIDMap.hasNext()) {
			Integer workItemID = itrWorksByWorkItemIDMap.next();
			List<TCostBean> costBeansWorkItem = worksByWorkItemMap.get(workItemID);
			// get the workItem costs pro person
			Map<Integer, List<TCostBean>> costsByPerson = getCostsByPerson(costBeansWorkItem);
			Map<Integer, Double> personActualWorkMap = new HashMap<Integer, Double>();
			for (Iterator<Integer> iterator = costsByPerson.keySet().iterator(); iterator.hasNext();) {
				Integer personID = iterator.next();
				List<TCostBean> costs = costsByPerson.get(personID);
				personActualWorkMap.put(personID, getSumOfActualWorks(costs));
			}
			Integer taskUID = workItemIDToTaskUIDMap.get(workItemID);
			if (taskUID != null) {
				Task taskElement = UIDBasedTaskElementMap.get(taskUID);
				if (taskElement != null) {
					Date taskActualStart = null;
					Date taskActualFinish = null;
					// all assignments for workItem: both last imported (even if
					// there is no actual work (costBean) associated) and new
					// ones
					Map<Integer, ResourceAssignment> resourceBasedAssignmentsForWorkItem = new HashMap<Integer, ResourceAssignment>();
					Set<Integer> resoucesWithImportedRemainingWork = new HashSet<Integer>();
					// iterate through costBeans for each person
					Iterator<Integer> itrPerson = costsByPerson.keySet().iterator();
					while (itrPerson.hasNext()) {
						Integer personID = itrPerson.next();
						List<TCostBean> costBeansPerson = costsByPerson.get(personID);
						List<Integer> resourceUIDForPersonList = personIDToResourceUIDMap.get(personID);
						Integer resourceUID = null;
						if (resourceUIDForPersonList == null || resourceUIDForPersonList.isEmpty()) {
							// unknown person, should never happen
							continue;
						} else {
							if (resourceUIDForPersonList.size() == 1) {
								// unambiguous person <-> resource: the
								// corresponding person was mapped only to this
								// resource
								resourceUID = resourceUIDForPersonList.get(0);
							}
						}
						ResourceAssignment assignmentElement = null;
						Map<Integer, ResourceAssignment> importedResourceUIDToAssignmentsMap = importedTaskUIDToResourceUIDToAssignmentsMap.get(taskUID);
						// last imported assignments for task exist
						if (importedResourceUIDToAssignmentsMap != null && importedResourceUIDToAssignmentsMap.size() > 0) {
							if (resourceUID == null && importedResourceUIDToAssignmentsMap.size() > 0) {
								// ambiguous: the same person was mapped for
								// more resources:
								// find the resourceID based on the available
								// assignments from the previous import:
								// get the first last imported resourceID found
								// which was mapped for the person.
								// (as best effort but still error prone)
								Iterator<Integer> itrResource = importedResourceUIDToAssignmentsMap.keySet().iterator();
								while (itrResource.hasNext()) {
									resourceUID = itrResource.next();
									if (resourceUIDForPersonList.contains(resourceUID)) {
										break;
									} else {
										resourceUID = null;
									}
								}
							}
							// person had assignment to task both in the
							// previous import and in track+: remove it if found
							// to process them now
							// assignments not processed (removed) here should
							// be looped at the end for removing (probably their
							// costBeans were removed)
							assignmentElement = importedResourceUIDToAssignmentsMap.remove(resourceUID);

						}
						if (resourceUID == null) {
							// no previous import for this assignment, so there
							// is no way to uniquely identify the resourceID
							// for adding the assignment for, as best effort
							// take the first mapped for the person
							resourceUID = resourceUIDForPersonList.get(0);
						}
						// the timephased data will be calculated depending on
						// the unit
						Double unit = new Double(1.0); // default 1.0 = 100%
						boolean isNew = false;

						if (assignmentElement == null) {
							assignmentElement = new ResourceAssignment(project);
							// new assignment: the workItem has actual work
							// booked by a person but the previous
							// import file contains no assignments: actual
							// work(s) added in track+ exclusively since the
							// last import
							isNew = true;
						} else {
							// get the unit from the previously imported
							// assignment
							// unit =
							// MsProjectExchangeDOMHelper.getSubelementDouble(assignmentElement,
							// ASSIGNMENT_ELEMENTS.Units);
							unit = assignmentElement.getUnits().doubleValue();
							if (unit == null || Math.abs(unit.doubleValue()) < EPSILON) {
								unit = new Double(1.0);
							}
							/*
							 * if assignmentElement was already imported try to
							 * preserve the remaining work pro person (taking
							 * into account the corresponding actual work
							 * difference). This counts by calculating the
							 * duration of the task
							 */
							Double lastActualWorkPerson = assignmentElement.getActualWork().getDuration();
							if (lastActualWorkPerson == null) {
								lastActualWorkPerson = new Double(0.0);
							}
							Double lastRemainingWorkPerson = assignmentElement.getRemainingWork().getDuration();
							if (lastRemainingWorkPerson == null) {
								lastRemainingWorkPerson = new Double(0.0);
							}
							Double currentActualWorkPerson = personActualWorkMap.get(personID);
							if (currentActualWorkPerson == null) {
								currentActualWorkPerson = new Double(0.0);
							}
							double actualWorkDifference = currentActualWorkPerson.doubleValue() - lastActualWorkPerson.doubleValue();
							double newRemainingWorkPerson = 0.0;
							if (lastRemainingWorkPerson.doubleValue() >= actualWorkDifference) {
								newRemainingWorkPerson = lastRemainingWorkPerson.doubleValue() - actualWorkDifference;
							}
							assignmentElement.setRemainingWork(Duration.getInstance(newRemainingWorkPerson, TimeUnit.HOURS));

						}

						// because it is not trivial to combine two lists of
						// works for changes (last imported and actual from
						// Genji),
						// all imported ASSIGNMENT_ACTUAL_WORK and
						// ASSIGNMENT_REMAINING_WORK type
						// and without value timePhasedData elements will be
						// removed
						// (and later the ASSIGNMENT_ACTUAL_WORK elements from
						// track+ will be added again based on Genji costBeans)
						project.getAllResourceAssignments().remove(assignmentElement);
						resourceBasedAssignmentsForWorkItem.put(resourceUID, assignmentElement);
						if (isNew && costBeansPerson != null && costBeansPerson.size() > 0) {
							ResourceAssignment newAssignmentElement = new ResourceAssignment(project);
							newAssignmentElement.setUniqueID(++maxAssignmentUID);
							assignmentElement.setTaskUniqueID(taskUID);
							assignmentElement.setResourceUniqueID(resourceUID);
						}
						/**
						 * add TimephasedData elements
						 */
						// if actualStart does not comply with the
						// timephasedData elements
						// then the Actual work may have the wrong value in
						// MSProject
						Date assignmentActualStart = null;
						Date assignmentActualFinish = null;
						int assignmentActualOvertimeHours = 0;
						int assignmentActualOverTimeMinutes = 0;
						// formatted effortDate mapped to calendar with with
						// actualized times for
						// a certain user to avoid double allocation for a user
						// in a certain time
						Map<String, Calendar> effortDateToCalBookedMap = null;
						Map<String, Boolean> effortDateExhaustedMap = null;
						if (!allowActualWorkOverlap) {
							effortDateToCalBookedMap = effortDateToCalBookedForPersonMap.get(personID);
							effortDateExhaustedMap = effortDateExhaustedForPersonMap.get(personID);
						}
						if (effortDateToCalBookedMap == null) {
							effortDateToCalBookedMap = new HashMap<String, Calendar>();
							if (!allowActualWorkOverlap) {
								effortDateToCalBookedForPersonMap.put(personID, effortDateToCalBookedMap);
							}
						}
						// whether the date was exhausted by previous works
						if (effortDateExhaustedMap == null) {
							effortDateExhaustedMap = new HashMap<String, Boolean>();
							if (!allowActualWorkOverlap) {
								effortDateExhaustedForPersonMap.put(personID, effortDateExhaustedMap);
							}
						}
						if (costBeansPerson != null && costBeansPerson.size() > 0) {
							// add the ASSIGNMENT_ACTUAL_WORK timephasedData
							// elements from Genji
							Date effortDate = null;
							Date previousEffortDate = null;
							for (TCostBean costBean : costBeansPerson) {
								if (effortDate != null) {
									// effort date is null at the beginning and
									// after by overbooked days if
									// allowOvertimeBookings=false
									// in previousEffortDate the last valid
									// (non-null) effortDate should be kept
									previousEffortDate = effortDate;
								}
								effortDate = costBean.getEffortdate();
								if (previousEffortDate != null && effortDate != null && previousEffortDate.before(effortDate)) {
									// add empty timespans for the gaps between
									// actual work,
									// otherwise the gap is considered as
									// actually worked
									addEmptyIntervals(project, assignmentElement, previousEffortDate, effortDate, emptyTimeSpan, effortDateToCalBookedMap,
											effortDateExhaustedMap, personBasedCalendarExceptionWorkingTimes.get(personID),
											personBasedCalendarWeekDayWorkingTimes.get(personID),
											calendarUIDBasedBaseCalendarExceptionWorkingTimes.get(personIDToBaseCalendar.get(personID)),
											calendarUIDBasedBaseCalendarWeekDayWorkingTimes.get(personIDToBaseCalendar.get(personID)), allowOvertimeBookings);
								}
								Double hours = costBean.getHours();
								if (effortDate != null && hours != null && hours.intValue() > 0.0) {
									// calculate the unit proportional values,
									// because in TimephasedData element the
									// Start and Finish
									// date fields are based on unit
									// proportional hours, but the Value field
									// is based on real hours
									// depending on Assignment element's Unit
									// sub-element
									Double unitHours = hours.doubleValue() / unit.doubleValue();
									int unitProportionalHours = unitHours.intValue();
									double unitDecimalHours = unitHours - unitProportionalHours;
									int unitProportionalMinutes = (int) Math.round(unitDecimalHours * 60);
									// date over-booked by previous costBeans
									effortDate = getNextValidDate(effortDate, effortDateExhaustedMap, personBasedCalendarExceptionWorkingTimes.get(personID),
											personBasedCalendarWeekDayWorkingTimes.get(personID),
											calendarUIDBasedBaseCalendarExceptionWorkingTimes.get(personIDToBaseCalendar.get(personID)),
											calendarUIDBasedBaseCalendarWeekDayWorkingTimes.get(personIDToBaseCalendar.get(personID)), allowOverdayBookings);
									if (effortDate == null) {
										if (allowOverdayBookings) {
											LOGGER.warn("No reasonable effort date found for person " + personID + " workItem " + workItemID + " costID "
													+ costBean.getObjectID());
										} else {
											LOGGER.warn("The work " + hours.doubleValue() + " for person " + personID + " workItem " + workItemID + " costID "
													+ costBean.getObjectID() + " could not be added because the worktime for date "
													+ MsProjectExchangeBL.formatDateTime(effortDate) + " is exhausted by previous works");
										}
										continue;// to the next costBean
									}
									String effortDateStr = MsProjectExchangeBL.formatDate(effortDate);
									Calendar calToTime = Calendar.getInstance();
									calToTime.setTime(effortDate);
									boolean jumpToNextWorkingTimeInterval = false;
									List<String[]> workingPeriods = getWorkingTimeForDay(personBasedCalendarExceptionWorkingTimes.get(personID),
											personBasedCalendarWeekDayWorkingTimes.get(personID),
											calendarUIDBasedBaseCalendarExceptionWorkingTimes.get(personIDToBaseCalendar.get(personID)),
											calendarUIDBasedBaseCalendarWeekDayWorkingTimes.get(personIDToBaseCalendar.get(personID)), effortDate);
									if (workingPeriods == null) {
										// should never happen,
										continue;
									} // else {
									int realHours = 0;
									int realMinutes = 0;
									boolean isOvertime = false;
									// the Start date of the timephasedData
									Date timePhasedDataStart = null;
									for (Iterator itrWorkPeriod = workingPeriods.iterator(); itrWorkPeriod.hasNext();) {
										// try each workingTime interval to see
										// where the work can be added
										String[] workingTime = (String[]) itrWorkPeriod.next();
										String fromTime = workingTime[0];
										String toTime = workingTime[1];
										Map<String, Integer> timeUnitsMapFromTime = MsProjectExchangeBL.getTimeUnitsMapFromCalendarTime(fromTime);
										Map<String, Integer> timeUnitsMapToTime = MsProjectExchangeBL.getTimeUnitsMapFromCalendarTime(toTime);
										Integer previousHour = calToTime.get(Calendar.HOUR_OF_DAY);
										calToTime.set(Calendar.HOUR_OF_DAY, timeUnitsMapToTime.get(MSPROJECT_TIME_UNITS.HOUR));
										calToTime.set(Calendar.MINUTE, timeUnitsMapToTime.get(MSPROJECT_TIME_UNITS.MINUTE));
										Integer nextHour = calToTime.get(Calendar.HOUR_OF_DAY);
										if (!itrWorkPeriod.hasNext() && allowOvertimeBookings) {
											// midnight means the start of the
											// next day
											if (previousHour > nextHour) {
												// overtime over the midnight
												calToTime.add(Calendar.DATE, 1);
											}
											isOvertime = true;
										} else {
											isOvertime = false;
										}
										Calendar calBookedForPerson = effortDateToCalBookedMap.get(effortDateStr);
										if (calBookedForPerson == null) {
											calBookedForPerson = Calendar.getInstance();
											calBookedForPerson.setTime(effortDate);
											calBookedForPerson.set(Calendar.HOUR_OF_DAY, timeUnitsMapFromTime.get(MSPROJECT_TIME_UNITS.HOUR));
											calBookedForPerson.set(Calendar.MINUTE, timeUnitsMapFromTime.get(MSPROJECT_TIME_UNITS.MINUTE));
											effortDateToCalBookedMap.put(effortDateStr, calBookedForPerson);
										}
										// jumped first time or from the
										// previous workingTime interval
										if (jumpToNextWorkingTimeInterval) {
											calBookedForPerson.set(Calendar.HOUR_OF_DAY, timeUnitsMapFromTime.get(MSPROJECT_TIME_UNITS.HOUR));
											calBookedForPerson.set(Calendar.MINUTE, timeUnitsMapFromTime.get(MSPROJECT_TIME_UNITS.MINUTE));
											jumpToNextWorkingTimeInterval = false;
										}
										if (calBookedForPerson.getTime().after(calToTime.getTime())
												|| calBookedForPerson.getTimeInMillis() == calToTime.getTimeInMillis()) {
											// try the next workingTime interval
											// because till this time
											// the user booked previous
											// costBeans
											LOGGER.debug("Period from " + MsProjectExchangeBL.formatDateTime(calToTime.getTime()) + " for personID " + personID
													+ " workItemID " + workItemID + " costID " + costBean.getObjectID() + " added at date "
													+ MsProjectExchangeBL.formatDateTime(effortDate) + " expense work " + hours.doubleValue()
													+ " already booked by other costBeans.");
											if (calBookedForPerson.getTimeInMillis() == calToTime.getTimeInMillis()) {
												jumpToNextWorkingTimeInterval = true;
											}
											continue; // to the next working
														// time of the current
														// date
										} else {
											// available working time found
											if (timePhasedDataStart == null) {
												timePhasedDataStart = calBookedForPerson.getTime();
											}
											long millisecondsLeftInInterval = calToTime.getTimeInMillis() - calBookedForPerson.getTimeInMillis();
											if (millisecondsLeftInInterval == 0) {
												continue; // to the next working
															// time of the
															// current date
											}
											long diffHours = millisecondsLeftInInterval / (60 * 60 * 1000);
											long diffMinutes = (millisecondsLeftInInterval - diffHours * 60 * 60 * 1000) / (60 * 1000);
											if (unitProportionalHours < diffHours
													|| (unitProportionalHours == diffHours && unitProportionalMinutes <= diffMinutes)) {
												// the current workingTime
												// interval is enough for work
												// to add
												calBookedForPerson.add(Calendar.HOUR_OF_DAY, unitProportionalHours);
												calBookedForPerson.add(Calendar.MINUTE, unitProportionalMinutes);
												Double unitPropRemainingHours = new Double(unitProportionalHours + (double) unitProportionalMinutes / 60);
												// add the real hours
												Double realHoursDouble = new Double(unitPropRemainingHours * unit.doubleValue());
												realHours += realHoursDouble.intValue();
												if (isOvertime) {
													assignmentActualOvertimeHours += realHoursDouble.intValue();
												}
												double decimalHours = realHoursDouble - realHoursDouble.intValue();
												realMinutes += (int) Math.round(decimalHours * 60);
												if (isOvertime) {
													assignmentActualOverTimeMinutes += (int) Math.round(decimalHours * 60);
												}
												addTimephasedDataElement(project, assignmentElement, timePhasedDataStart, calBookedForPerson.getTime(),
														MsProjectExchangeBL.getTimeSpanFromHoursAndMinutes(realHours, realMinutes));
												if (assignmentActualStart == null || assignmentActualStart.after(timePhasedDataStart)) {
													assignmentActualStart = timePhasedDataStart;
												}
												if (assignmentActualFinish == null || assignmentActualFinish.before(calBookedForPerson.getTime())) {
													assignmentActualFinish = calBookedForPerson.getTime();
												}
												LOGGER.debug("Added actual work for person " + personID + " workItem " + workItemID + " cost "
														+ costBean.getObjectID() + " added at date " + MsProjectExchangeBL.formatDateTime(effortDate)
														+ " start " + MsProjectExchangeBL.formatDateTime(timePhasedDataStart) + " finish "
														+ MsProjectExchangeBL.formatDateTime(calBookedForPerson.getTime()) + " expense work "
														+ hours.doubleValue() + " actual work " + realHours + ":" + realMinutes);
												if (!itrWorkPeriod.hasNext() && allowOverdayBookings && unitProportionalHours == diffHours
														&& unitProportionalMinutes == diffMinutes) {
													// if terminated exactly at
													// midnight reset the
													// effortDateToEffortDateWithTime
													// to start again at the
													// first FromTime
													// effortDateToCalBookedForPersonMap.remove(effortDateStr);
													effortDateExhaustedMap.put(effortDateStr, Boolean.TRUE);
												}
												break; // successfully added for
														// this day, break to
														// continue with the
														// next CostBean
											} else {
												// the current workingTime
												// interval is not enough for
												// the entire work to add:
												// add only a part of the work
												// till the end of the current
												// interval
												int remainingHoursInWorkingTimePeriod = Long.valueOf(diffHours).intValue();
												int remainingMinutesInWorkingTimePeriod = Long.valueOf(diffMinutes).intValue();
												// book up to the end of the
												// current workingTime
												calBookedForPerson.setTime(calToTime.getTime());
												jumpToNextWorkingTimeInterval = true;
												unitProportionalHours -= remainingHoursInWorkingTimePeriod;
												if (unitProportionalMinutes < remainingMinutesInWorkingTimePeriod) {
													unitProportionalMinutes += 60;
													unitProportionalHours--;
												}
												unitProportionalMinutes -= remainingMinutesInWorkingTimePeriod;
												// add only a part of the work
												// till the end of this
												// workingTime interval
												// add the real hours
												Double remainingHours = new Double(millisecondsLeftInInterval * unit.doubleValue() / (60 * 60 * 1000));
												realHours += remainingHours.intValue();
												if (isOvertime) {
													assignmentActualOvertimeHours += remainingHours.intValue();
												}
												double decimalHours = remainingHours - remainingHours.intValue();
												realMinutes += (int) Math.round(decimalHours * 60);
												if (isOvertime) {
													assignmentActualOverTimeMinutes += (int) Math.round(decimalHours * 60);
												}
												if (!itrWorkPeriod.hasNext()) {
													// the user consumed the
													// entire workDay and
													// overtime
													// till midnight but this is
													// still not enough
													// add the biggest possible
													// part of the work as best
													// effort
													addTimephasedDataElement(project, assignmentElement, timePhasedDataStart, calBookedForPerson.getTime(),
															MsProjectExchangeBL.getTimeSpanFromHoursAndMinutes(realHours, realMinutes));
													if (assignmentActualStart == null || assignmentActualStart.after(timePhasedDataStart)) {
														assignmentActualStart = timePhasedDataStart;
													}
													if (assignmentActualFinish == null || assignmentActualFinish.before(calBookedForPerson.getTime())) {
														assignmentActualFinish = calBookedForPerson.getTime();
													}
													LOGGER.debug("Added actual work part for person " + personID + " workItem " + workItemID + " cost "
															+ costBean.getObjectID() + " start " + MsProjectExchangeBL.formatDateTime(timePhasedDataStart)
															+ " finish " + MsProjectExchangeBL.formatDateTime(calBookedForPerson.getTime()) + " expense work "
															+ hours.doubleValue() + " actual work " + realHours + ":" + realMinutes);
													// effortDateToCalBookedForPersonMap.remove(effortDateStr);
													effortDateExhaustedMap.put(effortDateStr, Boolean.TRUE);
													// force to initialize again
													// at the first FromTime
													calBookedForPerson = null;
													timePhasedDataStart = null;
													realHours = 0;
													realMinutes = 0;
													effortDate = getNextValidDate(effortDate, effortDateExhaustedMap,
															personBasedCalendarExceptionWorkingTimes.get(personID),
															personBasedCalendarWeekDayWorkingTimes.get(personID),
															calendarUIDBasedBaseCalendarExceptionWorkingTimes.get(personIDToBaseCalendar.get(personID)),
															calendarUIDBasedBaseCalendarWeekDayWorkingTimes.get(personIDToBaseCalendar.get(personID)),
															allowOverdayBookings);
													if (effortDate == null) {
														if (allowOvertimeBookings) {
															LOGGER.warn("No reasonable effort date found for person " + personID + " workItem " + workItemID
																	+ " costID " + costBean.getObjectID());
														} else {
															LOGGER.warn("The work " + unitProportionalHours + ":" + unitProportionalMinutes + " for person "
																	+ personID + " workItem " + workItemID + " costID " + costBean.getObjectID()
																	+ " could not be added because the worktime for date "
																	+ MsProjectExchangeBL.formatDateTime(effortDate) + " is exhausted");
														}
														break;
													}
													effortDateStr = MsProjectExchangeBL.formatDate(effortDate);
													// calToTime is now midnight
													// (starting of the next
													// day).
													// set to the actual
													// effortDate day because
													// the
													// iterator for workPeriods
													// is started again with
													// this new date
													calToTime.setTime(effortDate);
													workingPeriods = getWorkingTimeForDay(personBasedCalendarExceptionWorkingTimes.get(personID),
															personBasedCalendarWeekDayWorkingTimes.get(personID),
															calendarUIDBasedBaseCalendarExceptionWorkingTimes.get(personIDToBaseCalendar.get(personID)),
															calendarUIDBasedBaseCalendarWeekDayWorkingTimes.get(personIDToBaseCalendar.get(personID)),
															effortDate);

													itrWorkPeriod = workingPeriods.iterator();

												}
											}
										}
									}
								}
							}
						}
						// assignment level ActualStart, Start and ActualFinish
						// dates
						if (assignmentActualStart != null) {
							assignmentElement.setActualStart(assignmentActualStart);
							if (taskActualStart == null || taskActualStart.after(assignmentActualStart)) {
								taskActualStart = assignmentActualStart;
							}
						}
						// the Start should be not later as the ActualStart
						Date startElement = assignmentElement.getStart();
						if (startElement != null) {
							Date startDate = assignmentElement.getStart();
							if (startDate != null && assignmentActualStart != null && startDate.after(assignmentActualStart)) {
								assignmentElement.setStart(assignmentActualStart);
							}
						}
						if (assignmentActualFinish != null) {
							// it is actually finished only when remaining work
							// is 0
							// so it might be removed later
							assignmentElement.setActualFinish(assignmentActualFinish);
							if (taskActualFinish == null || taskActualFinish.before(assignmentActualFinish)) {
								taskActualFinish = assignmentActualFinish;
							}
						}
						// do we have ActualOvertimeWork?
						if (assignmentActualOvertimeHours > 0 || assignmentActualOverTimeMinutes > 0) {

							String actualOverTimeHoursTimeSpan = MsProjectExchangeBL.getTimeSpanFromHoursAndMinutes(assignmentActualOvertimeHours,
									assignmentActualOverTimeMinutes);
							DateFormat df = new SimpleDateFormat();
							Double actualOverTimeWork = assignmentElement.getActualOvertimeWork().getDuration();
							if (actualOverTimeWork == null) {
								actualOverTimeWork = new Double(0);
							}
							Double overtimeWork = assignmentElement.getOvertimeWork().getDuration();
							if (overtimeWork == null) {
								overtimeWork = new Double(0);
							}
							// the OvertimeWork will not be less than the
							// ActualOvertimeWork
							if (actualOverTimeWork.doubleValue() > overtimeWork.doubleValue()) {
								// TODO MPXJ, set setOvertimeWork check.
								try {
									Calendar myCal = Calendar.getInstance();
									myCal.setTime(df.parse(actualOverTimeHoursTimeSpan));
									int hours = myCal.get(Calendar.HOUR_OF_DAY);
									assignmentElement.setOvertimeWork(Duration.getInstance(hours, TimeUnit.HOURS));
								} catch (Exception ex) {
									LOGGER.error("Date parse error: " + ex.getMessage());
								}
							}
						}
					}// end person loop

					/**
					 * actualize the remaining work and work for each assignment
					 * of the current task it is important because the duration
					 * is calculated based on the works assigned to each person
					 * (for example if a user is set to 100% but no Work and
					 * RemainingWork is set for him then it will not shorten the
					 * duration as expected)
					 */
					// process those assignments of the current task which were
					// imported last time but
					// no TCostBean corresponding to person were found for them
					// consequently
					// they wern't processed (removed) above in the loop for
					// persons.
					// Probably the TCostBean was removed since the last import.
					// The assignment will not be deleted (that should be made
					// in MsProject) but the
					// actual work will be set to 0 (no TCostBean). We still
					// need the assignment because of the
					// remaining work: if we would remove the entire assignment
					// the task duration would increase
					// because the remaining work of this assignment should be
					// taken by other resources
					Map<Integer, ResourceAssignment> importedResourceUIDToAssignmentsMap = importedTaskUIDToResourceUIDToAssignmentsMap.get(taskUID);
					if (importedResourceUIDToAssignmentsMap != null && importedResourceUIDToAssignmentsMap.size() > 0) {
						Iterator<Integer> itrResource = importedResourceUIDToAssignmentsMap.keySet().iterator();
						while (itrResource.hasNext()) {
							Integer resouceID = itrResource.next();

							ResourceAssignment noCostAssignmentElement = importedResourceUIDToAssignmentsMap.remove(resouceID);
							// String emptyValue =
							// MsProjectExchangeBL.getTimeSpanFromDouble(0.0);
							// reset the ActualWork to 0 because there is no
							// costBean for this
							// TODO MPXJ
							// noCostAssignmentElement.setActualWork(emptyTimeSpan);
							resoucesWithImportedRemainingWork.add(resouceID);
							resourceBasedAssignmentsForWorkItem.put(resouceID, noCostAssignmentElement);
							// TODO MPXJ
							// removeTimephasedData(noCostAssignmentElement);
						}
					}
					/**
					 * "normalize" the RemainingWork and Work for assignments
					 */
					// sum up the remaining work from each resource
					// (both new and imported assignments, but for new it is 0
					// anyway)
					double allPersonsRemainingWork = 0.0;
					for (Map.Entry<Integer, ResourceAssignment> entry : resourceBasedAssignmentsForWorkItem.entrySet()) {
						// Integer resourceID = entry.getKey();
						ResourceAssignment assignmentElement = entry.getValue();
						Double remainingWork = assignmentElement.getRemainingWork().getDuration();
						if (remainingWork != null) {
							allPersonsRemainingWork += remainingWork.doubleValue();
						}
					}
					// remaining work for the entire task
					Double taskRemainingHours = taskElement.getRemainingWork().getDuration();
					if (taskRemainingHours == null) {
						taskRemainingHours = new Double(0.0);
					}
					if (Math.abs(allPersonsRemainingWork - taskRemainingHours.doubleValue()) > 0.001) {
						// "normalization" needed
						if (allPersonsRemainingWork > taskRemainingHours.doubleValue()) {
							// the task level remaining work is less than the
							// sum if remaining work for assignments:
							// (probably was explicitly set in track+ to a
							// smaller value)
							// we must remove some person level remainingWork at
							// best effort
							double difference = allPersonsRemainingWork - taskRemainingHours.doubleValue();
							for (Iterator itrAssignments = resourceBasedAssignmentsForWorkItem.keySet().iterator(); itrAssignments.hasNext();) {
								Integer resourceID = (Integer) itrAssignments.next();
								ResourceAssignment assignmentElement = resourceBasedAssignmentsForWorkItem.get(resourceID);
								Double remainingWork = assignmentElement.getRemainingWork().getDuration();
								if (remainingWork != null && Math.abs(remainingWork.doubleValue()) > EPSILON) {
									if (difference > remainingWork.doubleValue()) {
										// TODO MPXJ set setRemainingWork
										// validation.
										assignmentElement.setRemainingWork(Duration.getInstance(remainingWork, TimeUnit.HOURS));
										difference -= remainingWork.doubleValue();
									} else {
										assignmentElement.setRemainingWork(Duration.getInstance(remainingWork.doubleValue() - difference, TimeUnit.HOURS));
										break;
									}
								}
							}
						} else {
							// the difference should be added to one or more
							// person specific remainingWork at best effort:
							double difference = taskRemainingHours.doubleValue() - allPersonsRemainingWork;
							List<ResourceAssignment> newRemainingWorkElements = new ArrayList<ResourceAssignment>();
							// see whether there are new assignments (those
							// which doesn't appear in the last import)
							// because for them the remainingWork should not be
							// preserved
							for (Iterator itrAssignments = resourceBasedAssignmentsForWorkItem.keySet().iterator(); itrAssignments.hasNext();) {
								Integer resourceID = (Integer) itrAssignments.next();
								if (!resoucesWithImportedRemainingWork.contains(resourceID)) {
									newRemainingWorkElements.add(resourceBasedAssignmentsForWorkItem.get(resourceID));
								}
							}
							if (newRemainingWorkElements.isEmpty() && resourceBasedAssignmentsForWorkItem.size() > 0) {
								// no new assignment found, we must mess up a
								// "to be preserved" remainingWork of an
								// existing assignment
								ResourceAssignment assignmentElement = resourceBasedAssignmentsForWorkItem.values().iterator().next();
								double remainingWork = assignmentElement.getRemainingWork().getDuration();
								assignmentElement.setRemainingWork(Duration.getInstance(remainingWork + difference, TimeUnit.HOURS));
							} else {
								// new assignment(s) found: divide the remaining
								// work equally for each new assignment
								Double remainingWorkProPerson = difference / newRemainingWorkElements.size();
								for (Iterator<ResourceAssignment> iterator = newRemainingWorkElements.iterator(); iterator.hasNext();) {
									ResourceAssignment assignmentElement = iterator.next();
									assignmentElement.setRemainingWork(Duration.getInstance(remainingWorkProPerson, TimeUnit.HOURS));
								}
							}
						}
					}
					/*
					 * set the Works for each assignment: it is always the sum
					 * of actual work and remaining work (doesn't matter the
					 * value from track+ total budget)
					 */
					double taskActualOvertimeWork = 0.0;
					if (resourceBasedAssignmentsForWorkItem != null && resourceBasedAssignmentsForWorkItem.size() > 0) {
						for (Iterator itrAssignments = resourceBasedAssignmentsForWorkItem.keySet().iterator(); itrAssignments.hasNext();) {
							Integer resourceID = (Integer) itrAssignments.next();
							ResourceAssignment assignmentElement = resourceBasedAssignmentsForWorkItem.get(resourceID);
							Double remainingWorkHours = assignmentElement.getRemainingWork().getDuration();
							if (remainingWorkHours != null && Math.abs(remainingWorkHours.doubleValue()) > EPSILON) {
								// if there is remaining work then remove the
								// actual finish date
								Date actualFinishElement = assignmentElement.getActualFinish();
								if (actualFinishElement != null) {
									project.getAllResourceAssignments().remove(assignmentElement);
								}
							}
							// assignment level OvertimeWork,
							// ActualOvertimeWork, RegularWork
							Duration actualWorkDuration = assignmentElement.getActualWork();
							Double actualWorkHours = null;
							if (actualWorkDuration != null) {
								actualWorkHours = actualWorkDuration.getDuration();
							}
							if (actualWorkHours == null) {
								actualWorkHours = Double.valueOf(0);
							}
							Double work = remainingWorkHours + actualWorkHours;
							assignmentElement.setWork(Duration.getInstance(work, TimeUnit.HOURS));
							Duration overtimeWorkDuration = assignmentElement.getOvertimeWork();
							Double assignmentOvertimeWork = null;
							if (overtimeWorkDuration != null) {
								assignmentOvertimeWork = overtimeWorkDuration.getDuration();
							}
							if (assignmentOvertimeWork == null) {
								assignmentOvertimeWork = new Double(0);
							}
							/*
							 * Double assignmentActualOvertimeWork =
							 * assignmentElement.getActualWork().getDuration();
							 * if (assignmentActualOvertimeWork==null) {
							 * assignmentActualOvertimeWork = new Double(0); }
							 * taskActualOvertimeWork +=
							 * assignmentActualOvertimeWork.doubleValue();
							 */
							taskActualOvertimeWork += actualWorkHours;
							assignmentElement.setRegularWork(Duration.getInstance(work - assignmentOvertimeWork, TimeUnit.HOURS));

						}
					}
					// task level OvertimeWork, ActualOvertimeWork, RegularWork
					Duration overtimeWorkDuration = taskElement.getOvertimeWork();
					Double taskOvertimeWork = null;
					if (overtimeWorkDuration != null) {
						taskOvertimeWork = overtimeWorkDuration.getDuration();
					}
					if (taskOvertimeWork == null) {
						taskOvertimeWork = new Double(0);
					}
					// the OvertimeWork will not be less than the
					// ActualOvertimeWork
					if (taskActualOvertimeWork > taskOvertimeWork.doubleValue()) {
						taskElement.setOvertimeWork(Duration.getInstance(taskActualOvertimeWork, TimeUnit.HOURS));
						taskOvertimeWork = taskActualOvertimeWork;
					}
					if (taskActualOvertimeWork > 0.0) {
						// taskElement.setActualOvertimeWork(Duration.getInstance(taskActualOvertimeWork,
						// TimeUnit.HOURS));
					}
					Double work = taskElement.getWork().getDuration();
					if (work == null) {
						work = new Double(0);
					}
					taskElement.setRegularWork(Duration.getInstance(work.doubleValue() - taskOvertimeWork.doubleValue(), TimeUnit.HOURS));
					// task level ActualStart, Start and ActualFinish dates
					if (taskActualStart != null) {
					}
					Date startElement = taskElement.getStart();
					if (startElement != null) {
						Date startDate = taskElement.getStart();
						if (startDate != null && taskActualStart != null && startDate.after(taskActualStart)) {
							// taskElement.setStart(taskActualStart);
						}
					}
					if (taskActualFinish != null && taskRemainingHours != null && Math.abs(taskRemainingHours.doubleValue()) < 0.0001) {
						taskElement.setActualFinish(taskActualFinish);
					}
				}
			}
		}
		// assignments which still remained are probably from tasks which
		// doesn't have actualWork.
		// The actual work should be set to 0 (no TCostBean) anyway for each
		// one, but ideally none of them should
		// be deleted (the assignment is still needed because of their remaining
		// work assigned to a person).
		// If the remainingWork is changed for the workItem then we would have
		// the evenly distribute the difference
		// among the previously imported assignments' remainingWorks. This is
		// too complicated, instead we assign
		// the task's entire remainingWork to the first person and delete the
		// assignments for the other persons
		if (importedTaskUIDToResourceUIDToAssignmentsMap.size() > 0) {
			// TODO Parsing correctness validation
			for (Integer key : importedTaskUIDToResourceUIDToAssignmentsMap.keySet()) {
				Integer taskUID = key;
				Task taskElement = UIDBasedTaskElementMap.get(taskUID);
				if (taskElement != null && taskElement.getRemainingWork() != null) {
					Double taskRemainingWork = taskElement.getRemainingWork().getDuration();
					Map<Integer, ResourceAssignment> resourceUIDToAssignmentsMap = importedTaskUIDToResourceUIDToAssignmentsMap.get(taskUID);
					if (resourceUIDToAssignmentsMap != null) {
						boolean first = true;
						for (Integer key2 : resourceUIDToAssignmentsMap.keySet()) {
							Integer resourceID = resourceUIDToAssignmentsMap.get(key2).getUniqueID();
							// only the real resourceIDs are important
							if (first) {
								// TODO assignmentElement REMAINING_WORK field
								// missing
								first = false;
								ResourceAssignment assignmentElement = resourceUIDToAssignmentsMap.get(resourceID);
								// assignmentElement.getTim
								// reset the ActualWork to 0 because there is no
								// costBean for this
								// removeTimephasedData(assignmentElement);
								// TODO MPXJ!
								// assignmentElement.setActualWork(Duration.getInstance(emptyTimeSpan,
								// TimeUnit.HOURS));
								// work = actualWork + remainingWork
								// so if we set actual work to emptyTimeSpan we
								// should make work=remainingWork, otherwise the
								// file doesn't open
								// TODO MPXJ!!
								// assignmentElement.setRemainingWork(Duration.getInstance(taskRemainingWork,
								// TimeUnit.HOURS));
								// MsProjectExchangeDOMHelper.setChildTextByName(assignmentElement,
								// COMMON_ELEMENTS.Work, taskRemainingWork,
								// true);
								// TODO remove if statement MPXJ
								if (assignmentElement != null) {
									assignmentElement.setWork(Duration.getInstance(taskRemainingWork, TimeUnit.HOURS));
								}
							} else {
								ResourceAssignment assignmentElement = resourceUIDToAssignmentsMap.get(resourceID);
								project.getAllResourceAssignments().remove(assignmentElement);
								// reset the ActualWork to 0 because there is no
								// costBean for this
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Adds timephasedData entries between two actual works (costBeans) on
	 * different days to fill the gap with empty time spans, otherwise the gap
	 * is considered as actually worked
	 * 
	 * @param document
	 * @param assignmentElement
	 * @param timephasedDataElementTemplate
	 * @param previousEffortDate
	 * @param effortDate
	 * @param emptyTimeSpan
	 * @param effortDateToCalBookedForPersonMap
	 * @param effortDateExhaustedMap
	 * @param personBasedCalendarExceptionWorkingTimes
	 * @param personBasedCalendarWeekDayWorkingTimes
	 * @param calendarUIDBasedBaseCalendarExceptionWorkingTimes
	 * @param calendarUIDBasedBaseCalendarWeekDayWorkingTimes
	 */
	private static void addEmptyIntervals(ProjectFile project, ResourceAssignment assignmentElement, Date previousEffortDate, Date effortDate,
			String emptyTimeSpan, Map<String, Calendar> effortDateToCalBookedForPersonMap, Map<String, Boolean> effortDateExhaustedMap,
			Map<String, List<String[]>> personBasedCalendarExceptionWorkingTimes, Map<Integer, List<String[]>> personBasedCalendarWeekDayWorkingTimes,
			Map<String, List<String[]>> calendarUIDBasedBaseCalendarExceptionWorkingTimes,
			Map<Integer, List<String[]>> calendarUIDBasedBaseCalendarWeekDayWorkingTimes, boolean allowOvertimeBookings) {
		if (previousEffortDate != null && effortDate != null && previousEffortDate.before(effortDate)) {
			// if not the first costBean (actualWork) executed by a person on a
			// workItem (previousEffortDate!=null)
			// and we have a gap (previousEffortDate.before(effortDate))
			String previousEffortDateStr = MsProjectExchangeBL.formatDate(previousEffortDate);
			Calendar previousEffortDateCalendar = effortDateToCalBookedForPersonMap.get(previousEffortDateStr);
			if (previousEffortDateCalendar != null) {
				while (previousEffortDateCalendar.getTime().before(effortDate)) {
					// by first loop may be the same day (if at
					// previousEffortDateCalendar
					// there is still some empty period
					// previousEffortDate==nextEmptyEffortDate)
					Date nextEmptyEffortDate = getNextValidDate(previousEffortDateCalendar.getTime(), effortDateExhaustedMap,
							personBasedCalendarExceptionWorkingTimes, personBasedCalendarWeekDayWorkingTimes,
							calendarUIDBasedBaseCalendarExceptionWorkingTimes, calendarUIDBasedBaseCalendarWeekDayWorkingTimes, true);
					if (nextEmptyEffortDate != null && nextEmptyEffortDate.before(effortDate)) {
						// we are still before effortDate
						List<String[]> workingPeriods = getWorkingTimeForDay(personBasedCalendarExceptionWorkingTimes, personBasedCalendarWeekDayWorkingTimes,
								calendarUIDBasedBaseCalendarExceptionWorkingTimes, calendarUIDBasedBaseCalendarWeekDayWorkingTimes, effortDate);
						if (workingPeriods != null && workingPeriods.size() > 0) {
							// typically not null by the first loop:
							// add start of the timephasedData from the
							// remaining empty workPeriod for this day
							Calendar calNextEmptyEffortStart = effortDateToCalBookedForPersonMap.get(MsProjectExchangeBL.formatDate(nextEmptyEffortDate));
							if (calNextEmptyEffortStart == null) {
								// add start of the timephasedData from the
								// start of the day
								calNextEmptyEffortStart = Calendar.getInstance();
								String[] workingTimeStart = workingPeriods.get(0);
								String fromTime = workingTimeStart[0];
								Map<String, Integer> timeUnitsMapFromTime = MsProjectExchangeBL.getTimeUnitsMapFromCalendarTime(fromTime);
								calNextEmptyEffortStart.setTime(nextEmptyEffortDate);
								calNextEmptyEffortStart.set(Calendar.HOUR_OF_DAY, timeUnitsMapFromTime.get(MSPROJECT_TIME_UNITS.HOUR));
								calNextEmptyEffortStart.set(Calendar.MINUTE, timeUnitsMapFromTime.get(MSPROJECT_TIME_UNITS.MINUTE));
							}
							// add end of the timephasedData till the end of the
							// day
							String[] workingTimeEnd = null;
							if (allowOvertimeBookings && workingPeriods.size() > 1) {
								// if allowOvertimeBookings then the last period
								// is the overtimePeriod, so take the last
								// regular time period
								workingTimeEnd = workingPeriods.get(workingPeriods.size() - 2);
							} else {
								// the last regular time period
								workingTimeEnd = workingPeriods.get(workingPeriods.size() - 1);
							}
							String toTime = workingTimeEnd[1];
							Map<String, Integer> timeUnitsMapToTime = MsProjectExchangeBL.getTimeUnitsMapFromCalendarTime(toTime);
							Calendar calNextEmptyEffortEnd = Calendar.getInstance();
							calNextEmptyEffortEnd.setTime(nextEmptyEffortDate);
							calNextEmptyEffortEnd.set(Calendar.HOUR_OF_DAY, timeUnitsMapToTime.get(MSPROJECT_TIME_UNITS.HOUR));
							calNextEmptyEffortEnd.set(Calendar.MINUTE, timeUnitsMapToTime.get(MSPROJECT_TIME_UNITS.MINUTE));
							addTimephasedDataElement(project, assignmentElement, calNextEmptyEffortStart.getTime(), calNextEmptyEffortEnd.getTime(),
									emptyTimeSpan);
						}
					} else {
						// arrived at effortDate, the gap was filled
						break;
					}
					previousEffortDateCalendar.setTime(nextEmptyEffortDate);
					previousEffortDateCalendar.add(Calendar.DATE, 1);
				}
			}
		}
	}

	/**
	 * Get the
	 * 
	 * @param effortDate
	 * @param effortDateExhaustedMap
	 * @param personBasedCalendarExceptionWorkingTimes
	 * @param personBasedCalendarWeekDayWorkingTimes
	 * @param calendarUIDBasedBaseCalendarExceptionWorkingTimes
	 * @param calendarUIDBasedBaseCalendarWeekDayWorkingTimes
	 * @return
	 */
	private static Date getNextValidDate(Date effortDate, Map<String, Boolean> effortDateExhaustedMap,
			Map<String, List<String[]>> personBasedCalendarExceptionWorkingTimes, Map<Integer, List<String[]>> personBasedCalendarWeekDayWorkingTimes,
			Map<String, List<String[]>> calendarUIDBasedBaseCalendarExceptionWorkingTimes,
			Map<Integer, List<String[]>> calendarUIDBasedBaseCalendarWeekDayWorkingTimes, boolean allowOverdayBookings) {
		Calendar calendarNextDay = Calendar.getInstance();
		calendarNextDay.setTime(effortDate);
		int nonWorkingDays = 0;
		Boolean dateExhausted = null;
		do {
			String effortDateStr = MsProjectExchangeBL.formatDate(effortDate);
			dateExhausted = effortDateExhaustedMap.get(effortDateStr);
			if (dateExhausted == null || !dateExhausted.booleanValue()) {
				// try to get the workPeriods for effortDate only if date was
				// not exhausted previously
				List<String[]> workingPeriods = getWorkingTimeForDay(personBasedCalendarExceptionWorkingTimes, personBasedCalendarWeekDayWorkingTimes,
						calendarUIDBasedBaseCalendarExceptionWorkingTimes, calendarUIDBasedBaseCalendarWeekDayWorkingTimes, effortDate);
				if (workingPeriods == null) {
					// weekend or holiday (exception day)
					nonWorkingDays++;
					if (nonWorkingDays > 60) {
						// not a single working day found in the next two months
						// probably none of the days is WorkingTime in a week
						// no should never happen, but should be tested to avoid
						// infinitely searching a working day
						return null;
					} else {
						// weekend or holiday (exception day) is counted as
						// dateExhausted
						dateExhausted = Boolean.TRUE;
						effortDateExhaustedMap.put(effortDateStr, dateExhausted);
					}
				} else {
					// working periods found
					return effortDate;
				}
			}
			if (allowOverdayBookings) {
				// can we try the next day?
				calendarNextDay.add(Calendar.DATE, 1);
				effortDate = calendarNextDay.getTime();
			} else {
				return null;
			}
		} while (dateExhausted != null && dateExhausted.booleanValue());
		return effortDate;
	}

	private static void addTimephasedDataElement(ProjectFile project, ResourceAssignment assignmentElement, Date startDate, Date finishDate, String timespan) {
		ResourceAssignment timephasedDataElement = new ResourceAssignment(project);
		// TODO ASSIGNMENT_ACTUAL_WORK field missing
		// MsProjectExchangeDOMHelper.setChildTextByName(timephasedDataElement,
		// COMMON_ELEMENTS.Type,
		// Integer.valueOf(TIMEPHASEDDATA_TYPE.ASSIGNMENT_ACTUAL_WORK).toString(),
		// true);
		timephasedDataElement.setUniqueID(assignmentElement.getUniqueID());
		timephasedDataElement.setStart(startDate);
		timephasedDataElement.setFinish(finishDate);
		assignmentElement.setUnits(Integer.valueOf(TIMEPHASEDDATA_UNIT.HOURS));
		// TODO set Value
		// MsProjectExchangeDOMHelper.setChildTextByName(timephasedDataElement,
		// TIMEPHASEDDATA_ELEMENTS.Value, timespan, true);

	}

	/**
	 * Set the project elements
	 * 
	 * @param rootElement
	 * @param name
	 * @param existingTrackPlusWorkItems
	 * @param projectBean
	 * @param defaultTaskType
	 * @param durationFormat
	 * @param hoursPerWorkDay
	 */
	private static void setProjectElements(ProjectFile project, String name, String projectStartDate, String projectEndDate, TProjectBean projectBean,
			Integer defaultTaskType, Integer durationFormat, Double hoursPerWorkDay) {
		Integer oldMinutesPerDay = PropertiesHelper.getIntegerProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.MINUTES_PER_DAY);
		Integer oldMinutesPerWeek = PropertiesHelper.getIntegerProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.MINUTES_PER_WEEK);
		if (oldMinutesPerDay == null) {
			oldMinutesPerDay = Integer.valueOf(8 * 60);
		}
		if (oldMinutesPerWeek == null) {
			oldMinutesPerWeek = Integer.valueOf(5 * 8 * 60);
		}
		Integer newMinutesPerDay = oldMinutesPerDay;
		Integer newMinutesPerWeek = oldMinutesPerWeek;
		if (hoursPerWorkDay != null && Math.abs(hoursPerWorkDay.doubleValue()) > EPSILON) {
			newMinutesPerDay = new Double(hoursPerWorkDay * 60).intValue();
			if (EqualUtils.notEqual(newMinutesPerDay, oldMinutesPerDay)) {
				// the hoursPerWorkDay has been changed since the last project
				// import:
				// change also the minutes per week according to daysPerWeek
				double daysPerWeek = oldMinutesPerWeek / oldMinutesPerDay;
				newMinutesPerWeek = new Double(newMinutesPerDay * daysPerWeek).intValue();
			}
		}
		Date date = new Date();
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			project.getProjectHeader().setName(name);
			project.getProjectHeader().setSubject(projectBean.getDescription());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			project.getProjectHeader().setCreationDate(cal.getTime());
			project.getProjectHeader().setLastSaved(cal.getTime());
			project.getProjectHeader().setStatusDate(dateTimeFormat.parse(projectStartDate));
			project.getProjectHeader().setCurrencyCode("EUR");
			project.getProjectHeader().setCurrentDate(cal.getTime());
			project.getProjectHeader().setStartDate(dateTimeFormat.parse(projectStartDate));
			project.getProjectHeader().setFinishDate(dateTimeFormat.parse(projectEndDate));
		} catch (Exception ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
			LOGGER.error("Date parse error: " + ex.getMessage());
		}

		if (defaultTaskType != null) {
			project.getProjectHeader().setDefaultTaskType(TaskType.getInstance(defaultTaskType));
		}

		ProjectAccountingTO projectAccountingTO = ProjectBL.getProjectAccountingTO(projectBean.getObjectID());
		String curencySymbol = projectAccountingTO.getCurrencySymbol();// projectBean.getCurrencySymbol();
		if (curencySymbol != null) {
			project.getProjectHeader().setCurrencySymbol(curencySymbol);
		}
		if (newMinutesPerDay != null) {
			project.getProjectHeader().setMinutesPerDay(newMinutesPerDay);
		}
		if (newMinutesPerWeek != null) {
			project.getProjectHeader().setMinutesPerWeek(newMinutesPerWeek);
		}
		Integer daysPerMonth = PropertiesHelper.getIntegerProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.DAYS_PER_MONTH);
		if (daysPerMonth != null) {
			project.getProjectHeader().setDaysPerMonth(daysPerMonth);
		}
		// TODO Duration format field missing
		// if (durationFormat!=null) {
		// MsProjectExchangeDOMHelper.setChildTextByName(rootElement,
		// PROJECT_ELEMENTS.DurationFormat, durationFormat.toString(), false);
		// project.getProjectHeader().setDurat
		// }
		Integer weekStartDay = project.getProjectHeader().getWeekStartDay().getValue();
		if (weekStartDay == null) {
			project.getProjectHeader().setWeekStartDay(Day.getInstance(Calendar.getInstance().getFirstDayOfWeek()));
		}
	}

	/**
	 * This method recursively generates all task elements
	 * 
	 * @param parentWorkItemBean
	 * @param outlineStructure
	 * @param workItemBeansMap
	 * @param parentChildrenList
	 * @param tasksElements
	 * @param workItemIDToTaskUIDMap
	 * @param lastImportedMsProjectTasksMap
	 * @param existingTrackPlusTasks
	 * @param totalBudgetBeanMap
	 * @param costBeanMap
	 * @param remainingBudgetMap
	 * @param taskElementPattern
	 * @param hoursPerWorkday
	 * @param defaultTaskType
	 * @param durationFormat
	 */
	private static void listWorkItems(TWorkItemBean parentWorkItemBean, OutlineStructure outlineStructure, IDCounter idCounter,
			Map<Integer, TWorkItemBean> workItemBeansMap, Map<Integer, List<Integer>> parentChildrenList,
			ProjectFile project,
			Map<Integer, Integer> workItemIDToTaskUIDMap,
			Map<Integer, Task> lastImportedMsProjectTasksMap,
			Map<Integer, TMSProjectTaskBean> existingTrackPlusTasks,
			// SortedMap<Integer, TBudgetBean> totalBudgetBeanMap,
			Map<Integer, TComputedValuesBean> plannedTimesMap, Map<Integer, List<TCostBean>> costBeanMap,
			Map<Integer, TActualEstimatedBudgetBean> remainingBudgetMap, Double hoursPerWorkday, Integer defaultTaskType, Integer durationFormat,
			Map<Integer, Map<String, List<String[]>>> calendarUIDBasedBaseCalendarExceptionWorkingTimes,
			Map<Integer, Map<Integer, List<String[]>>> calendarUIDBasedBaseCalendarWeekDayWorkingTimes, LocalLookupContainer localLookupContainer) {

		Integer workItemID = parentWorkItemBean.getObjectID();
		Integer taskUID = workItemIDToTaskUIDMap.get(workItemID);
		Task taskElement = null;
		TMSProjectTaskBean msProjectTaskBean = null;
		boolean isNew = false;
		if (taskUID == null) {
			// newly added task (not imported previously and TMSProjectTaskBean
			// not even exists)
			isNew = true;
		} else {
			// taskUID (TMSProjectTaskBean) already exists
			msProjectTaskBean = existingTrackPlusTasks.get(taskUID);
			taskElement = lastImportedMsProjectTasksMap.get(taskUID);
			if (taskElement == null) {
				// but task doesn't exist in the last import file: it is a new
				// task but also a TMSProjectTaskBean was saved
				// (typically will be the case when by saving a new Task also a
				// TMSProjectTaskBean will be saved. Not yet the case.)
				taskElement = project.addTask();
				isNew = true;
			}
		}
		List<Integer> childrenIDs = parentChildrenList.get(workItemID);
		boolean hasChildren = childrenIDs != null && childrenIDs.size() > 0;
		// saving the msProjectTaskBean
		if (msProjectTaskBean == null) {
			msProjectTaskBean = new TMSProjectTaskBean();
			msProjectTaskBean.setWorkitem(workItemID);
			taskUID = idCounter.getTaskUID();
			idCounter.setTaskUID(++taskUID);
			msProjectTaskBean.setUniqueID(taskUID);
			msProjectTaskBean.setTaskType(defaultTaskType);
			msProjectTaskBean.setConstraintType(CONSTRAINT_TYPE.AS_SOON_AS_POSSIBLE);
			msProjectTaskBean.setOutlineNumber(outlineStructure.getFullOutlineNumber());

			if (hasChildren) {
				msProjectTaskBean.setSummary(BooleanFields.TRUE_VALUE);
			}
			// since none of the msProjectTaskBean attributes are modifiable at
			// UI a save is needed only if the bean is new
			msProjectTaskDAO.save(msProjectTaskBean);
			workItemIDToTaskUIDMap.put(workItemID, taskUID);
		}

		TWorkItemBean workItemBean = workItemBeansMap.get(workItemID);
		TComputedValuesBean computedValueBean = plannedTimesMap.get(workItemID);
		TActualEstimatedBudgetBean actualEstimatedBudgetBean = remainingBudgetMap.get(workItemID);
		List<TCostBean> workItemCosts = costBeanMap.get(workItemBean.getObjectID());

		int taskID = idCounter.getTaskID();
		idCounter.setTaskID(++taskID);
		addUpdateTask(project, taskElement, workItemBean, msProjectTaskBean, taskID, outlineStructure, computedValueBean, actualEstimatedBudgetBean,
				hoursPerWorkday, workItemCosts, hasChildren, defaultTaskType, durationFormat, isNew, calendarUIDBasedBaseCalendarExceptionWorkingTimes,
				calendarUIDBasedBaseCalendarWeekDayWorkingTimes, localLookupContainer);
		boolean firstChild = false;
		OutlineStructure newTaskLevel = null;
		if (childrenIDs != null && parentChildrenList.size() > 0) {
			for (Iterator iterator = childrenIDs.iterator(); iterator.hasNext();) {
				Integer childID = (Integer) iterator.next();
				TWorkItemBean childWorkItemBean = workItemBeansMap.get(childID);
				if (firstChild == false) {
					newTaskLevel = outlineStructure.newLevel();
				}
				listWorkItems(childWorkItemBean, newTaskLevel.copy(), idCounter, workItemBeansMap, parentChildrenList, project, workItemIDToTaskUIDMap,
						lastImportedMsProjectTasksMap, existingTrackPlusTasks, plannedTimesMap, costBeanMap, remainingBudgetMap, hoursPerWorkday,
						defaultTaskType, durationFormat, calendarUIDBasedBaseCalendarExceptionWorkingTimes, calendarUIDBasedBaseCalendarWeekDayWorkingTimes,
						localLookupContainer);
				firstChild = true;
			}
		}
	}

	/**
	 * Get the parent to children map: order the children by outlineNumber
	 * (Integer comparison not string comparison)
	 * 
	 * @param existingTrackPlusWorkItemsList
	 * @param existingTrackPlusTasksMap
	 * @param workItemIDToTaskUIDMap
	 * @return
	 */
	private static Map<Integer, List<Integer>> getParentToChildrenMap(List<TWorkItemBean> existingTrackPlusWorkItemsList,
			Map<Integer, TMSProjectTaskBean> existingTrackPlusTasksMap, Map<Integer, Integer> workItemIDToTaskUIDMap) {
		Map<Integer, List<Integer>> parentToChildrenListMap = new HashMap<Integer, List<Integer>>();
		for (Iterator iterator = existingTrackPlusWorkItemsList.iterator(); iterator.hasNext();) {
			TWorkItemBean workItemBean = (TWorkItemBean) iterator.next();
			Integer parentID = workItemBean.getSuperiorworkitem();
			Integer childID = workItemBean.getObjectID();
			List<Integer> children;
			if (parentID != null) {
				children = parentToChildrenListMap.get(parentID);
				if (children == null) {
					children = new ArrayList<Integer>();
					parentToChildrenListMap.put(parentID, children);
				}
				children.add(childID);
			}
		}
		Comparator<Integer> outlineNumberComparator = new MsProjectExporterComparator(existingTrackPlusTasksMap, workItemIDToTaskUIDMap);
		for (Iterator<List<Integer>> iterator = parentToChildrenListMap.values().iterator(); iterator.hasNext();) {
			List<Integer> children = iterator.next();
			Collections.sort(children, outlineNumberComparator);
		}
		return parentToChildrenListMap;
	}

	/**
	 * Get the maximal taskUID value
	 * 
	 * @param UIDs
	 * @return
	 */
	private static int getMaxInteger(Collection<Integer> UIDs) {
		int maxUID = 0;
		for (Iterator iterator = UIDs.iterator(); iterator.hasNext();) {
			Integer UID = (Integer) iterator.next();
			if (UID.intValue() > maxUID) {
				maxUID = UID;
			}
		}
		return maxUID;
	}

	/**
	 * Gets the cost list by persons and workItems
	 * 
	 * @param costBeanList
	 * @return
	 */
	private static Map<Integer, List<TCostBean>> getWorksByWorkItem(List<TCostBean> costBeanList) {
		Map<Integer, List<TCostBean>> worksByWorkItem = new HashMap<Integer, List<TCostBean>>();
		for (Iterator iterator = costBeanList.iterator(); iterator.hasNext();) {
			TCostBean costBean = (TCostBean) iterator.next();
			Integer workItemID = costBean.getWorkitem();
			List<TCostBean> works = worksByWorkItem.get(workItemID);
			if (works == null) {
				works = new ArrayList<TCostBean>();
				worksByWorkItem.put(workItemID, works);
			}
			works.add(costBean);
		}
		return worksByWorkItem;
	}

	/**
	 * Gets the cost list by persons and workItems
	 * 
	 * @param costBeanList
	 * @return
	 */
	private static Set<Integer> getPersonsWithWork(List<TCostBean> costBeanList) {
		Set<Integer> personsWithWork = new HashSet<Integer>();
		for (Iterator iterator = costBeanList.iterator(); iterator.hasNext();) {
			TCostBean costBean = (TCostBean) iterator.next();
			Integer personID = costBean.getPerson();
			if (personID != null) {
				personsWithWork.add(personID);
			}
		}
		return personsWithWork;
	}

	/**
	 * Get the earliest date
	 * 
	 * @param existingWorkItems
	 * @return
	 */
	private static Date getProjectDate(Collection<TWorkItemBean> existingWorkItems, boolean start) {
		Date extremeDate = null;
		for (Iterator<TWorkItemBean> it = existingWorkItems.iterator(); it.hasNext();) {
			TWorkItemBean workItemBean = it.next();
			Date currentDate;
			if (start) {
				currentDate = workItemBean.getStartDate();
			} else {
				currentDate = workItemBean.getEndDate();
			}
			if (currentDate != null) {
				if (extremeDate == null) {
					extremeDate = currentDate;
				} else {
					if (start) {
						if (currentDate.before(extremeDate)) {
							extremeDate = currentDate;
						}
					} else {
						if (currentDate.after(extremeDate)) {
							extremeDate = currentDate;
						}
					}
				}
			}
		}
		return extremeDate;
	}

	/**
	 * Get the earliest date
	 * 
	 * @param existingWorkItems
	 * @return
	 */
	private static Date getProjectActualStartDate(Collection<TCostBean> costBeanList) {
		Date extremeDate = null;
		for (Iterator<TCostBean> it = costBeanList.iterator(); it.hasNext();) {
			TCostBean costBean = it.next();
			Date currentDate = costBean.getEffortdate();
			if (currentDate != null) {
				if (extremeDate == null || currentDate.before(extremeDate)) {
					extremeDate = currentDate;
				}
			}
		}
		return extremeDate;
	}

	/**
	 * Add or update the task
	 * 
	 * @param taskElement
	 * @param workItemBean
	 * @param msProjectTaskBean
	 * @param taskID
	 * @param outlineStructure
	 * @param budgetBean
	 * @param actualEstimatedBudgetBean
	 * @param hoursPerWorkday
	 * @param workItemCosts
	 * @param hasChildren
	 * @param defaultTaskType
	 * @param durationFormat
	 * @param isNew
	 */
	private static void addUpdateTask(ProjectFile project, Task taskElement, TWorkItemBean workItemBean, TMSProjectTaskBean msProjectTaskBean, int taskID,
			OutlineStructure outlineStructure, TComputedValuesBean computedValueBean, TActualEstimatedBudgetBean actualEstimatedBudgetBean,
			Double hoursPerWorkday, List<TCostBean> workItemCosts, boolean hasChildren, Integer defaultTaskType, Integer durationFormat, boolean isNew,
			Map<Integer, Map<String, List<String[]>>> calendarUIDBasedBaseCalendarExceptionWorkingTimes,
			Map<Integer, Map<Integer, List<String[]>>> calendarUIDBasedBaseCalendarWeekDayWorkingTimes, LocalLookupContainer localLookupContainer) {

		outlineStructure.incCurrentLevel();
		SystemWBSRT systemWbs = new SystemWBSRT();
		if (msProjectTaskBean.getUniqueID() != null) {
			if (taskElement == null) {
				taskElement = project.addTask();
			}
			// taskElement.setUniqueID( msProjectTaskBean.getUniqueID());
			taskElement.setConstraintType(ConstraintType.getInstance(msProjectTaskBean.getConstraintType()));
			taskElement.setConstraintDate(msProjectTaskBean.getConstraintDate());
			taskElement.setDeadline(msProjectTaskBean.getDeadline());
		}
		// taskElement.setID(Integer.valueOf(taskID));
		taskElement.setName(workItemBean.getSynopsis());
		taskElement.setUniqueID(workItemBean.getObjectID());
		if (isNew && defaultTaskType != null) {
			taskElement.setType(TaskType.getInstance(defaultTaskType));
		}
		taskElement.setCreateDate(workItemBean.getCreated());

		String wbs = systemWbs.getShowValue(SystemFields.WBS, null, workItemBean.getWBSOnLevel(), workItemBean.getObjectID(), localLookupContainer, null);
		taskElement.setOutlineNumber(wbs);
		// taskElement.setID(workItemBean.getIDNumber());
		StringTokenizer stringTokenizer = new StringTokenizer(wbs, ".");
		int numberOfLevels = stringTokenizer.countTokens() - 1;
		if (numberOfLevels == 0) {
			taskElement.setOutlineLevel(1);
		} else {
			taskElement.setOutlineLevel(numberOfLevels + 1);
		}
		/*
		 * if(msProjectTaskBean.getOutlineNumber() != null) {
		 * taskElement.setOutlineNumber(msProjectTaskBean.getOutlineNumber());
		 * }else { //
		 * taskElement.setOutlineLevel(outlineStructure.getAbsoluteLevel()); }
		 */
		if (workItemBean.getStartDate() != null && workItemBean.getEndDate() != null) {
			taskElement.setStart(workItemBean.getTopDownStartDate());

			taskElement.setActualStart(workItemBean.getStartDate());
			taskElement.setActualFinish(workItemBean.getEndDate());

			/*
			 * taskElement.setEarlyStart(workItemBean.getStartDate());
			 * taskElement.setLateStart(workItemBean.getStartDate());
			 * taskElement.setEarlyFinish(workItemBean.getEndDate());
			 * taskElement.setLateFinish(workItemBean.getEndDate());
			 */

			taskElement.setFinish(workItemBean.getTopDownEndDate());

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			if (formatter.format(workItemBean.getStartDate()).compareTo(formatter.format(workItemBean.getEndDate())) == 0) {
				taskElement.setDuration(Duration.getInstance(1, TimeUnit.DAYS));
				taskElement.setManualDuration(Duration.getInstance(1, TimeUnit.DAYS));
			} else {
				int days = Days.daysBetween(new DateTime(workItemBean.getStartDate()), new DateTime(workItemBean.getEndDate())).getDays();
				taskElement.setDuration(Duration.getInstance(days, TimeUnit.DAYS));
			}
		} else if (workItemBean.getStartDate() != null && workItemBean.getEndDate() == null) {
			taskElement.setStart(workItemBean.getStartDate());
			taskElement.setMilestone(true);
		}
		taskElement.setPercentageComplete(0);
		taskElement.setPercentageWorkComplete(0);
		taskElement.setNotes(workItemBean.getDescription());
		// from budgetBean we need only the time unit because the
		// exported budget is the sum of actual work and remaining work
		if (computedValueBean != null) {
			// prepare this transformed values for hasChanged because the
			// MSProject work seems to be in hours TODO?
			/*
			 * Double transformedHours =
			 * AccountingBL.transformToTimeUnits(budgetBean.getEstimatedHours(),
			 * hoursPerWorkday, budgetBean.getTimenit(),
			 * AccountingBL.TIMEUNITS.HOURS); if (transformedHours!=null) {
			 * plannedWorkHours = transformedHours.doubleValue(); }
			 */
			if (isNew && durationFormat == null) {
				// for new projects (without previous import the project level
				// durationFormat is null)
				if (AccountingBL.TIMEUNITS.HOURS.equals(computedValueBean.getMeasurementUnit())) {
					durationFormat = LAG_FORMAT.h;
				} else {
					durationFormat = LAG_FORMAT.d;
				}
			}
		}
		// TODO should we set the duration?
		// Element durationElement =
		// MsProjectExchangeDOMHelper.getChildByName(taskElement,
		// TASK_ELEMENTS.Duration);
		if (isNew && durationFormat != null) {
			// TODO setDuartionFormat method missing
			// MsProjectExchangeDOMHelper.setChildTextByName(taskElement,
			// TASK_ELEMENTS.DurationFormat, durationFormat.toString(), false);
			// taskElement.setDuration(Duration.getInstance(arg0,
			// durationFormat));
		}
		int summary;
		if (hasChildren) {
			summary = TASK_SUMMARY_TYPE.SUMMARY;
		} else {
			summary = TASK_SUMMARY_TYPE.NOT_SUMMARY;
		}
		taskElement.setSummary(summary > 0 ? true : false);
		double actualHours = getSumOfActualWorks(workItemCosts);
		try {
			// taskElement.setActualDuration(Duration.getInstance(actualHours,
			// TimeUnit.HOURS));

		} catch (Exception ex) {
			LOGGER.error("Date parse error: " + ex.getMessage());
		}
		double remainingWorkHours = 0.0;
		if (actualEstimatedBudgetBean != null) {
			Double transformedHours = AccountingBL.transformToTimeUnits(actualEstimatedBudgetBean.getEstimatedHours(), hoursPerWorkday,
					actualEstimatedBudgetBean.getTimeUnit(), AccountingBL.TIMEUNITS.HOURS);
			if (transformedHours != null) {
				remainingWorkHours = transformedHours.doubleValue();
			}
		} else {
			if (isNew && Math.abs(actualHours) < EPSILON && workItemBean.getStartDate() != null && workItemBean.getEndDate() != null) {
				// "simulate" a remaining value for issue in order to appear the
				// startDate and endDate in MSProject
				// as it was set in Genji although to work was set in Genji.
				Date dateFrom = workItemBean.getStartDate();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateFrom);
				CalendarUtil.clearTime(calendar);
				List<String[]> workingPeriods = null;
				while (calendar.getTime().before(workItemBean.getEndDate()) || calendar.getTime().getTime() == workItemBean.getEndDate().getTime()) {
					try {
						workingPeriods = getWorkingTimeForDay(null, null, calendarUIDBasedBaseCalendarExceptionWorkingTimes.values().iterator().next(),
								calendarUIDBasedBaseCalendarWeekDayWorkingTimes.values().iterator().next(), calendar.getTime());
					} catch (Exception ex) {
						LOGGER.debug("Element missing: " + ex.getMessage());
					}
					if (workingPeriods != null) {
						for (Iterator itrWorkPeriod = workingPeriods.iterator(); itrWorkPeriod.hasNext();) {
							// try each workingTime interval to see where the
							// work can be added
							String[] workingTime = (String[]) itrWorkPeriod.next();
							String fromTime = workingTime[0];
							String toTime = workingTime[1];
							Map<String, Integer> timeUnitsMapFromTime = MsProjectExchangeBL.getTimeUnitsMapFromCalendarTime(fromTime);
							Map<String, Integer> timeUnitsMapToTime = MsProjectExchangeBL.getTimeUnitsMapFromCalendarTime(toTime);
							double hourDiff = MsProjectExchangeBL.getHoursDiff(timeUnitsMapFromTime, timeUnitsMapToTime);
							remainingWorkHours += hourDiff;
						}
					}
					calendar.add(Calendar.DATE, 1);
				}
			}
		}
		taskElement.setRemainingWork(Duration.getInstance(remainingWorkHours, TimeUnit.HOURS));
		taskElement.setRemainingDuration(Duration.getInstance(remainingWorkHours, TimeUnit.HOURS));
		// the planned work is forced to be the sum of actual work and actual
		// hours
		// (instead of the plannedWorkHours) because otherwise MSProject result
		// is not consistent
		taskElement.setWork(Duration.getInstance(remainingWorkHours + actualHours, TimeUnit.HOURS));
		int days = Days.daysBetween(new DateTime(workItemBean.getStartDate()), new DateTime(workItemBean.getEndDate())).getDays();
		taskElement.setDuration(Duration.getInstance(days, TimeUnit.DAYS));
		taskElement.setActualWork(null);
		// not really needed to update because in track+ UI they are not
		// modifiable: for new tasks
		// the msProjectTaskBean is new for existing tasks they can be taken
		// from the last imported file
		if (isNew && Math.abs(actualHours) < EPSILON) {
			if (workItemBean.getStartDate() != null) {
				msProjectTaskBean.setConstraintType(CONSTRAINT_TYPE.START_NO_EARLIER_THAN);
				msProjectTaskBean.setConstraintDate(workItemBean.getStartDate());
			} else {
				if (workItemBean.getEndDate() != null) {
					msProjectTaskBean.setConstraintType(CONSTRAINT_TYPE.FINISH_NO_LATER_THAN);
					msProjectTaskBean.setConstraintDate(workItemBean.getEndDate());
				}
			}
		}
	}

	/**
	 * This method generates resource and calendar elements
	 * 
	 * @param persons
	 * @param document
	 * @param resourceElementPattern
	 * @param calendarElementPattern
	 * @param resourceToPersonMappings
	 */
	/*
	 * private static void exportResourcesAndCalendars(List<TPersonBean>
	 * persons, ProjectFile project, Map<Integer, List<Integer>>
	 * personIDToResourceUIDMap, int maxResourceUID) { //get the calendars from
	 * the last import // Element calendarsElement =
	 * MsProjectExchangeDOMHelper.getChildByName(projectElement,
	 * PROJECT_ELEMENTS.Calendars); ProjectCalendar calendarsElement =
	 * project.getCalendar();
	 * 
	 * if (calendarsElement==null) { ProjectCalendar newCalendar = new
	 * ProjectCalendar(project); // calendarsElement =
	 * document.createElement(PROJECT_ELEMENTS.Calendars); //
	 * projectElement.appendChild(calendarsElement); } else { //
	 * List<ProjectCalendar> existingCalendars =
	 * MsProjectExchangeDOMHelper.getElementList( // projectElement,
	 * PROJECT_ELEMENTS.Calendars, PROJECT_SUBELEMENTS.Calendar);
	 * List<ProjectCalendar> existingCalendars = project.getCalendars();
	 * 
	 * //TODO Unused with MPXJ. } Map<Integer, ProjectCalendar>
	 * lastImportedCalendarElementMap = getUIDBasedCalendars(project);
	 * //MsProjectExchangeDOMHelper
	 * .getSubelementBasedElementMap(lastImportedCalendarElementsList,
	 * COMMON_ELEMENTS.UID); int maxCalendarUID =
	 * getMaxInteger(lastImportedCalendarElementMap.keySet()); //get the
	 * resources from the last import
	 * 
	 * //TODO Unused with MPXJ ?
	 * 
	 * Map<Integer, Resource> UIDBasedResourceElementsMap =
	 * getUIDBasedResources(project); Resource resourceElement = null;
	 * resourceElement = UIDBasedResourceElementsMap.get(Integer.valueOf(0)); if
	 * (resourceElement==null) { resourceElement = project.addResource();
	 * resourceElement = addResourceElement(project, resourceElement,"", 0, 1);
	 * }
	 * 
	 * 
	 * int resourceID = 0; for(TPersonBean personBean: persons) {
	 * resourceElement = null; String personName =
	 * personBean.getSimpleFullName(); Integer personID =
	 * personBean.getObjectID(); List<Integer> resourceUIDList =
	 * personIDToResourceUIDMap.get(personID); if (resourceUIDList!=null &&
	 * resourceUIDList.size()>0) { //resource already existed by the last import
	 * //get the first one for this person resourceElement =
	 * UIDBasedResourceElementsMap.get(resourceUIDList.get(0)); } if
	 * (resourceElement==null) { //resource is new resourceElement =
	 * project.addResource(); resourceElement = addResourceElement(project,
	 * resourceElement, personName, ++maxResourceUID, ++maxCalendarUID); //add
	 * the new person to resource mapping, needed later by assignments
	 * List<Integer> resourcesForPerson =
	 * personIDToResourceUIDMap.get(personID); if (resourcesForPerson==null) {
	 * resourcesForPerson = new ArrayList<Integer>();
	 * personIDToResourceUIDMap.put(personID, resourcesForPerson); }
	 * resourcesForPerson.add(maxResourceUID);
	 * UIDBasedResourceElementsMap.put(maxResourceUID, resourceElement); //add
	 * also a calendar for the resource addCalendarElement(project,
	 * calendarsElement, personName, maxCalendarUID); } //elements always
	 * overwritten independently whether it existed already in a previous import
	 * resourceElement.setUniqueID(++resourceID);
	 * resourceElement.setNtAccount(personBean.getLoginName());
	 * resourceElement.setEmailAddress(personBean.getEmail());
	 * 
	 * }
	 * 
	 * }
	 */

	/**
	 * Creates and adds a resource element
	 * 
	 * @param document
	 * @param resourcesElement
	 * @param resourceElementPattern
	 * @param personName
	 * @param resourceUID
	 * @param calendarUID
	 * @return
	 */
	public static Resource addResourceElement(ProjectFile project, Resource resourcesElement, String personName, int resourceUID, int calendarUID) {
		resourcesElement.setUniqueID(resourceUID);
		resourcesElement.setName(personName);
		// TODO for resourceElement setCalendarUID setter missing.
		// MsProjectExchangeDOMHelper.setChildTextByName(resourceElement,
		// RESOURCE_ELEMENTS.CalendarUID,
		// Integer.valueOf(calendarUID).toString(), true);
		return resourcesElement;
	}

	/**
	 * Creates and adds a calendar element
	 * 
	 * @param document
	 * @param calendarsElement
	 * @param calendarElementPattern
	 * @param personBean
	 * @param calendarUID
	 */
	public static void addCalendarElement(ProjectFile project, ProjectCalendar calendarsElement, String personName, int calendarUID) {
		if (calendarsElement == null) {
			calendarsElement = new ProjectCalendar(project);
		}
		calendarsElement.setUniqueID(calendarUID);
		calendarsElement.setName(personName);
	}

	/**
	 * Gets the sum of the works from the list
	 * 
	 * @param costBeanList
	 * @return
	 */
	private static double getSumOfActualWorks(List<TCostBean> costBeanList) {
		double actualHours = 0.0;
		if (costBeanList != null) {
			Iterator<TCostBean> iterator = costBeanList.iterator();
			while (iterator.hasNext()) {
				TCostBean costBean = iterator.next();
				Double hours = costBean.getHours();
				if (hours != null) {
					actualHours += hours.doubleValue();
				}
			}
		}
		return actualHours;
	}

	/**
	 * Gets the sum of the works from the list
	 * 
	 * @param costBeanList
	 * @return
	 */
	private static Map<Integer, List<TCostBean>> getCostsByPerson(List<TCostBean> costBeanList) {
		Map<Integer, List<TCostBean>> costsByPerson = new HashMap<Integer, List<TCostBean>>();
		if (costBeanList != null) {
			Iterator<TCostBean> iterator = costBeanList.iterator();
			while (iterator.hasNext()) {
				TCostBean costBean = iterator.next();
				Integer person = costBean.getPerson();
				if (person != null) {
					List<TCostBean> costs = costsByPerson.get(person);
					if (costs == null) {
						costs = new ArrayList<TCostBean>();
						costsByPerson.put(person, costs);
					}
					costs.add(costBean);
				}
			}
		}
		return costsByPerson;
	}

	/**
	 * This method convert Genji lag format to MPXJ lag format
	 * 
	 * @param lag
	 * @return
	 */
	public static Integer convertTrackLagToMsProject(Integer lag) {
		switch (lag) {
		// Day
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.d:
			return 2;
			// elapsed day
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.ed:
			return 9;
			// elapsed hours
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.eh:
			return 8;
			// elapsed minutes
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.em:
			return 7;
			// elapsed months
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.emo:
			return 11;
			// elapsed percent
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.ePERCENT:
			return 13;
			// elapsed weeks
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.ew:
			return 10;
			// hours
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.h:
			return 1;
			// minutes
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.m:
			return 0;
			// months
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.mo:
			return 4;
			// percent
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.PERCENT:
			return 5;
			// weeks
		case MsProjectExchangeDataStoreBean.LAG_FORMAT.w:
			return 3;
		default:
			return -1;
		}
	}

}
