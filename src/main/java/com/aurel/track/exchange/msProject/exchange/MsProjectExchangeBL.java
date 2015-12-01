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

package com.aurel.track.exchange.msProject.exchange;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ResourceType;
import net.sf.mpxj.Task;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mspdi.MSPDIReader;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.reader.ProjectReader;
import net.sf.mpxj.writer.ProjectWriter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TMSProjectExchangeBean;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MsProjectExchangeDAO;
import com.aurel.track.dao.MsProjectTaskDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.exchange.msProject.importer.MSProjectImportException;
import com.aurel.track.exchange.msProject.importer.MsProjectImporterBL.EXCHANGE_DIRECTION;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;

/**
 * Methods which are used in both Microsoft Project import and export
 *
 * @author Tamas
 *
 */
public class MsProjectExchangeBL {
	private static final Logger LOGGER = LogManager.getLogger(MsProjectExchangeBL.class);

	private static MsProjectExchangeDAO msProjectExchangeDAO = DAOFactory.getFactory().getMsProjectExchangeDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	private static MsProjectTaskDAO msProjectTaskDAO = DAOFactory.getFactory().getMsProjectTaskDAO();

	public static interface MSPROJECT_TIME_UNITS {
		public static String YEAR = "Y";

		// in map
		public static String MONTH = "MO";
		public static String DAY = "D";
		public static String HOUR = "H";
		public static String MINUTE = "M";
		public static String SECOND = "S";

		// in MS project duration both month and minute are M
		public static char MINUTE_OR_MONTH = 'M';
		public static char TIME_SEPARATOR = 'T';
		public static char START_CHAR = 'P';
	}

	/**
	 * Initializes the MsProjectImporterBean by import
	 *
	 * @param projectOrReleaseID
	 * @param personBean
	 * @param file
	 * @param locale
	 * @return
	 * @throws MSProjectImportException
	 */
	public static MsProjectExchangeDataStoreBean initMsProjectExchangeBeanForImport(Integer projectOrReleaseID, TPersonBean personBean, File file, Locale locale)
			throws MSProjectImportException {
		MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean = MsProjectExchangeBL.createMsProjectExchangeBean(projectOrReleaseID, personBean, file,
				locale);
		try {
			ProjectReader reader;
			ProjectFile project;
			if ("mpp".equals(FilenameUtils.getExtension(file.getName()).toLowerCase())
					|| "mpt".equals(FilenameUtils.getExtension(file.getName()).toLowerCase())) {
				reader = new MPPReader();
				project = reader.read(file);
			} else {
				reader = new MSPDIReader();
				project = reader.read(file);
			}
			msProjectExchangeDataStoreBean.setProject(project);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			LOGGER.debug(ExceptionUtils.getStackTrace(ex));
		}
		MsProjectExchangeBL.loadMSProjectData(msProjectExchangeDataStoreBean, msProjectExchangeDataStoreBean.getProject());
		List<Task> tasks = msProjectExchangeDataStoreBean.getTasks();
		if (tasks == null || tasks.isEmpty()) {
			throw new MSProjectImportException("admin.actions.importMSProject.err.noTaskFound");
		}
		// project specific data get from msProject's project
		// creation date is important only by import (for conflict handling)
		Date lastSavedDate = msProjectExchangeDataStoreBean.getProject().getProjectHeader().getLastSaved();
		if (lastSavedDate == null) {
			lastSavedDate = new Date();
		}
		msProjectExchangeDataStoreBean.setLastSavedDate(lastSavedDate);
		return msProjectExchangeDataStoreBean;
	}

	/**
	 * Initializes the MsProjectImporterBean for export
	 *
	 * @param projectOrReleaseID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws MSProjectImportException
	 */
	public static MsProjectExchangeDataStoreBean initMsProjectExchangeBeanForExport(Integer projectOrReleaseID, TPersonBean personBean, Locale locale)
			throws MSProjectImportException {

		MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean = MsProjectExchangeBL.createMsProjectExchangeBean(projectOrReleaseID, personBean, null,
				locale);
		TMSProjectExchangeBean msProjectExchangeBean = getLastImportedMSProjectExchangeBean(msProjectExchangeDataStoreBean.getEntityID(),
				msProjectExchangeDataStoreBean.getEntityType());
		String xmlDocumentContent = "";
		ProjectFile project = new ProjectFile();
		if (msProjectExchangeBean != null) {
			xmlDocumentContent = msProjectExchangeBean.getFileContent();
			try {
				ProjectReader reader2 = new MSPDIReader();
				InputStream stream = new ByteArrayInputStream(xmlDocumentContent.getBytes("UTF-8"));
				project = reader2.read(stream);
				msProjectExchangeDataStoreBean.setName(msProjectExchangeBean.getFileName());
				msProjectExchangeDataStoreBean.setTasks(project.getAllTasks());
				msProjectExchangeDataStoreBean.setAssignments(project.getAllResourceAssignments());
				msProjectExchangeDataStoreBean.setWorkResources(project.getAllResources());

			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}
		}
		msProjectExchangeDataStoreBean.setProject(project);
		// load the detailed data from the previous import
		return msProjectExchangeDataStoreBean;
	}

	/**
	 * Gets the last MSProjectExchangeBean for a project or release
	 *
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	public static TMSProjectExchangeBean getLastImportedMSProjectExchangeBean(Integer entityID, int entityType) {
		List<TMSProjectExchangeBean> msProjectExchangeBeanList = msProjectExchangeDAO.loadByProjectOrRelease(entityID, entityType, EXCHANGE_DIRECTION.IMPORT);
		if (msProjectExchangeBeanList != null && !msProjectExchangeBeanList.isEmpty()) {
			// get in descending order by lastEdit, so get the first as last
			// imported
			return msProjectExchangeBeanList.get(0);
		}
		return null;
	}

	/**
	 * Gets the last MSProjectExchangeBean for a project or release
	 *
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	public static List<TMSProjectExchangeBean> getMSProjectExchangeBeans(Integer entityID, int entityType, int exchangeDirection) {
		return msProjectExchangeDAO.loadByProjectOrRelease(entityID, entityType, exchangeDirection);
	}

	/**
	 * Saves the msProjectExchangeBean
	 *
	 * @param msProjectExchangeDataStoreBean
	 * @return
	 */
	public static void saveMSProjectExportFile(MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean, String content) {
		TMSProjectExchangeBean msProjectExchangeBean = new TMSProjectExchangeBean();
		msProjectExchangeBean.setExchangeDirection(EXCHANGE_DIRECTION.EXPORT);
		msProjectExchangeBean.setEntityID(msProjectExchangeDataStoreBean.getEntityID());
		msProjectExchangeBean.setEntityType(msProjectExchangeDataStoreBean.getEntityType());
		msProjectExchangeBean.setChangedBy(msProjectExchangeDataStoreBean.getPersonBean().getObjectID());
		msProjectExchangeBean.setLastEdit(new Date());
		msProjectExchangeBean.setFileContent(content);
		msProjectExchangeDAO.save(msProjectExchangeBean);
	}

	/**
	 * Saves the msProjectExchangeBean
	 *
	 * @param msProjectExchangeDataStoreBean
	 * @return
	 */
	public static void saveMSProjectImportFile(MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean) {
		File file = msProjectExchangeDataStoreBean.getFile();
		String importFileContent = "";
		try {
			ProjectWriter writer = new MSPDIWriter();
			OutputStream out = new ByteArrayOutputStream();
			writer.write(msProjectExchangeDataStoreBean.getProject(), out);
			importFileContent = out.toString();
		} catch (Exception ex) {
			LOGGER.error("Errors while reading XML: " + ex.getMessage(), ex);
		}
		if (importFileContent != null) {
			TMSProjectExchangeBean msProjectExchangeBean = new TMSProjectExchangeBean();
			msProjectExchangeBean.setExchangeDirection(EXCHANGE_DIRECTION.IMPORT);
			msProjectExchangeBean.setEntityID(msProjectExchangeDataStoreBean.getEntityID());
			msProjectExchangeBean.setEntityType(msProjectExchangeDataStoreBean.getEntityType());
			msProjectExchangeBean.setChangedBy(msProjectExchangeDataStoreBean.getPersonBean().getObjectID());
			msProjectExchangeBean.setFileContent(importFileContent);
			msProjectExchangeBean.setFileName(file.getName());
			msProjectExchangeBean.setLastEdit(new Date());
			msProjectExchangeDAO.save(msProjectExchangeBean);
		}
		if (file != null) {
			file.delete();
		}
	}

	/**
	 * Creates a new MsProjectImporterBean
	 *
	 * @param projectOrReleaseID
	 * @param personBean
	 * @param file
	 * @param locale
	 * @return
	 * @throws MSProjectImportException
	 */
	private static MsProjectExchangeDataStoreBean createMsProjectExchangeBean(Integer projectOrReleaseID, TPersonBean personBean, File file, Locale locale)
			throws MSProjectImportException {

		MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean = new MsProjectExchangeDataStoreBean(personBean, locale);
		Integer entityID = null;
		Integer entityType = null;
		Integer projectID = null;
		if (projectOrReleaseID != null) {
			if (projectOrReleaseID.intValue() < 0) {
				entityID = Integer.valueOf(-projectOrReleaseID.intValue());
				entityType = SystemFields.PROJECT;
				projectID = entityID;
				msProjectExchangeDataStoreBean.setProjectID(entityID);
			} else {
				entityID = projectOrReleaseID;
				entityType = SystemFields.RELEASE;
				msProjectExchangeDataStoreBean.setReleaseScheduledID(entityID);
				TReleaseBean releaseBean = LookupContainer.getReleaseBean(entityID);
				if (releaseBean != null) {
					projectID = releaseBean.getProjectID();
					msProjectExchangeDataStoreBean.setProjectID(releaseBean.getProjectID());
					// get the previous resource to person mappings if exists
					msProjectExchangeDataStoreBean.setResourceUIDToPersonIDMap(MsProjectExchangeBL.transformResourceMappingsToMap(PropertiesHelper.getProperty(
							releaseBean.getMoreProps(), TReleaseBean.MOREPPROPS.RESOURCE_PERSON_MAPPINGS)));
				}
			}
		}
		msProjectExchangeDataStoreBean.setEntityID(entityID);
		if (entityType != null) {
			msProjectExchangeDataStoreBean.setEntityType(entityType);
		}
		msProjectExchangeDataStoreBean.setFile(file);

		if (projectID != null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean != null) {
				msProjectExchangeDataStoreBean.setProjectBean(projectBean);
				// do not set from track+ project but from msProject later
				// get the previous resource to person mappings if exists
				if (entityType == SystemFields.PROJECT) {
					msProjectExchangeDataStoreBean.setResourceUIDToPersonIDMap(MsProjectExchangeBL.transformResourceMappingsToMap(PropertiesHelper.getProperty(
							projectBean.getMoreProps(), TProjectBean.MOREPPROPS.RESOURCE_PERSON_MAPPINGS)));
				}
			}
		}
		Integer issueType = getIssueType();
		if (issueType == null) {
			throw new MSProjectImportException("admin.actions.importMSProject.err.noTasktypeFound");
		}
		msProjectExchangeDataStoreBean.setIssueType(issueType);
		return msProjectExchangeDataStoreBean;
	}

	private static Integer getIssueType() {
		List<TListTypeBean> taskIssueTypes = IssueTypeBL.loadByTypeFlag(TListTypeBean.TYPEFLAGS.TASK);
		if (taskIssueTypes != null && !taskIssueTypes.isEmpty()) {
			TListTypeBean issueTypeBean = taskIssueTypes.get(0);
			return issueTypeBean.getObjectID();
		}
		return null;
	}

	/**
	 * Load the project data into the msProjectImporterBean
	 *
	 * @param msProjectExchangeDataStoreBean
	 * @throws MSProjectImportException
	 */
	private static void loadMSProjectData(MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean, ProjectFile project) throws MSProjectImportException {
		if (project == null) {
			throw new MSProjectImportException("admin.actions.importMSProject.err.wrongFile");
		}
		msProjectExchangeDataStoreBean.setProject(project);

		// if there were no previous import to a project, but the project was
		// copied from a project
		// which had previous imports then the copied resource to person
		// mappings are not correct:
		// remove all resourceIDs for which no resource element is found in the
		// document
		Map<Integer, Integer> resourceUIDToPersonIDMap = msProjectExchangeDataStoreBean.getResourceUIDToPersonIDMap();
		Map<Integer, net.sf.mpxj.Resource> uidBasedResources = new HashMap<Integer, net.sf.mpxj.Resource>();

		List<net.sf.mpxj.Resource> allResources = msProjectExchangeDataStoreBean.getProject().getAllResources();
		List<net.sf.mpxj.Resource> workResources = new ArrayList<net.sf.mpxj.Resource>();
		for (net.sf.mpxj.Resource resource : allResources) {
			if (resource.getType() == ResourceType.WORK) {
				workResources.add(resource);
			}
			if (resource.getUniqueID() != null) {
				uidBasedResources.put(resource.getUniqueID(), resource);
			}
		}
		for (Iterator<Integer> iterator = resourceUIDToPersonIDMap.keySet().iterator(); iterator.hasNext();) {
			Integer resourceID = iterator.next();
			if (!uidBasedResources.containsKey(resourceID)) {
				iterator.remove();
			}
		}
		msProjectExchangeDataStoreBean.setTasks(msProjectExchangeDataStoreBean.getProject().getAllTasks());
		msProjectExchangeDataStoreBean.setResources(msProjectExchangeDataStoreBean.getProject().getAllResources());
		msProjectExchangeDataStoreBean.setWorkResources(workResources);
		msProjectExchangeDataStoreBean.setAssignments(msProjectExchangeDataStoreBean.getProject().getAllResourceAssignments());
	}

	/**
	 * Gets the existing workItem tasks as a workItemID based map Get all
	 * workItems which have MSProjectTask correspondents and are of type Task.
	 * No matter that they are deleted/archived/closed (can be that they should
	 * be undeleted by returning to an earlier state by importing an older MS
	 * Project file which still contains some tasks which were set as deleted by
	 * a previous import of a newer MS project File ) We are interested just in
	 * Tasks, so if a Task's issue type is changed to something else then it is
	 * not recognized any more as existing Task by import even if it have
	 * MSProjectTask correspondents
	 *
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, TWorkItemBean> getTaskWorkItemsForImport(Integer entityID, int entityType) {
		List<TWorkItemBean> workItemBeansList = workItemDAO.loadMsProjectTasksForImport(entityID, entityType);
		return (Map<Integer, TWorkItemBean>) GeneralUtils.createMapFromList(workItemBeansList);
	}

	/**
	 * Get Task workItems (which have or haven't MSProjectTask correspondents)
	 * and are not deleted/archived.
	 *
	 * @param entityID
	 * @param entityType
	 * @param notClosed
	 *            whether to get all or only the not closed tasks
	 * @return
	 */
	public static List<TWorkItemBean> getTaskWorkItemsForExport(Integer entityID, int entityType, boolean notClosed) {
		return workItemDAO.loadMsProjectTasksForExport(entityID, entityType, notClosed);
	}

	/**
	 * Gets the existing task as a UID based map
	 *
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	public static Map<Integer, TMSProjectTaskBean> getMsProjectTasksForImport(Integer entityID, int entityType) {
		List<TMSProjectTaskBean> tasks = msProjectTaskDAO.loadMsProjectTasksForImport(entityID, entityType);
		return getUIDBasedMsProjectTasks(tasks);
	}

	/**
	 * Gets the existing task as a UID based map
	 *
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	public static Map<Integer, TMSProjectTaskBean> getMsProjectTasksForExport(Integer entityID, int entityType, boolean notClosed) {
		List<TMSProjectTaskBean> tasks = msProjectTaskDAO.loadMsProjectTasksForExport(entityID, entityType, notClosed);
		return getUIDBasedMsProjectTasks(tasks);
	}

	private static Map<Integer, TMSProjectTaskBean> getUIDBasedMsProjectTasks(List<TMSProjectTaskBean> tasks) {
		Map<Integer, TMSProjectTaskBean> tasksMap = new HashMap<Integer, TMSProjectTaskBean>();
		if (tasks != null) {
			Iterator<TMSProjectTaskBean> iterator = tasks.iterator();
			while (iterator.hasNext()) {
				TMSProjectTaskBean projectTaskBean = iterator.next();
				tasksMap.put(projectTaskBean.getUniqueID(), projectTaskBean);
			}
		}
		return tasksMap;
	}

	/**
	 * Parse the date according to dateFormat
	 */
	public static Date parseDateTime(String dateStr) {
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (dateStr != null) {
			try {
				return dateTimeFormat.parse(dateStr);
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}

	/**
	 * Parse the date according to dateFormat
	 */
	public static String formatDateTime(Date date) {
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (date != null) {
			try {
				return dateTimeFormat.format(date);
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return "";
	}

	/**
	 * Parse the date according to dateFormat
	 */
	public static String formatDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			try {
				return dateFormat.format(date);
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return "";
	}

	private static String PAIR_SEPARATOR = "P";
	private static String ENTRY_SEPARATOR = "E";

	/**
	 * Transform the resource to person map to a string
	 *
	 * @param resourceUIDToPersonIDMap
	 * @return
	 */
	public static String transformResourceMappingsToString(Map<Integer, Integer> resourceUIDToPersonIDMap) {
		if (resourceUIDToPersonIDMap != null && !resourceUIDToPersonIDMap.isEmpty()) {
			StringBuilder resourceMappings = new StringBuilder();
			for (Iterator<Map.Entry<Integer, Integer>> iterator = resourceUIDToPersonIDMap.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<Integer, Integer> resourceToPerson = (Map.Entry<Integer, Integer>) iterator.next();
				resourceMappings.append(resourceToPerson.getKey());
				resourceMappings.append(PAIR_SEPARATOR);
				resourceMappings.append(resourceToPerson.getValue());
				resourceMappings.append(ENTRY_SEPARATOR);
			}
			return resourceMappings.toString();
		}
		return null;
	}

	/**
	 * Transform the resource to person string to a map
	 *
	 * @param resourceMappings
	 * @return
	 */
	public static Map<Integer, Integer> transformResourceMappingsToMap(String resourceMappings) {
		Map<Integer, Integer> resourceMappingsMap = new HashMap<Integer, Integer>();
		if (resourceMappings != null) {
			String[] resourceMappingsArr = resourceMappings.split(ENTRY_SEPARATOR);
			if (resourceMappingsArr != null) {
				for (int i = 0; i < resourceMappingsArr.length; i++) {
					String resourceMapping = resourceMappingsArr[i];
					if (resourceMapping != null) {
						String[] resourceMappingArr = resourceMapping.split(PAIR_SEPARATOR);
						if (resourceMappingArr != null && resourceMappingArr.length > 1) {
							resourceMappingsMap.put(Integer.valueOf(resourceMappingArr[0]), Integer.valueOf(resourceMappingArr[1]));
						}
					}
				}
			}
		}
		return resourceMappingsMap;
	}

	/*************************************************************************/
	/***************** time span helpers ***************************************/
	/*************************************************************************/
	private static String calendarTimeDelimiter = ":";

	/**
	 * Get the time span fields into a map
	 *
	 * @param calendarTime
	 * @return
	 */
	public static Map<String, Integer> getTimeUnitsMapFromCalendarTime(String calendarTime) {
		Map<String, Integer> timeUnitsMap = new HashMap<String, Integer>();
		String[] units = calendarTime.split(calendarTimeDelimiter);
		if (units != null && units.length > 0) {
			timeUnitsMap.put(MSPROJECT_TIME_UNITS.HOUR, Integer.valueOf(units[0]));
			if (units.length > 1) {
				timeUnitsMap.put(MSPROJECT_TIME_UNITS.MINUTE, Integer.valueOf(units[1]));
			}
			if (units.length > 2) {
				timeUnitsMap.put(MSPROJECT_TIME_UNITS.SECOND, Integer.valueOf(units[2]));
			}
		}
		return timeUnitsMap;
	}

	public static double getHoursDiff(Map<String, Integer> fromTime, Map<String, Integer> toTime) {
		Integer fromHours = fromTime.get(MSPROJECT_TIME_UNITS.HOUR);
		Integer fromMinutes = fromTime.get(MSPROJECT_TIME_UNITS.MINUTE);
		Integer toHours = toTime.get(MSPROJECT_TIME_UNITS.HOUR);
		Integer toMinutes = toTime.get(MSPROJECT_TIME_UNITS.MINUTE);

		double hoursDiff = 0;
		if (toHours != null && fromHours != null) {
			hoursDiff = (double) (toHours.intValue()) - fromHours.intValue();
		}
		int minutesDiff = 0;
		if (toMinutes != null && fromMinutes != null) {
			minutesDiff = toMinutes.intValue() - fromMinutes.intValue();
			if (minutesDiff < 0) {
				minutesDiff += 60;
				hoursDiff = hoursDiff - 1;
			}
		}
		if (fromMinutes != null) {
			hoursDiff += minutesDiff / (double) 60;
		}
		return AccountingBL.roundToDecimalDigits(hoursDiff, true);
	}

	private static Map<String, Integer> getTimeUnitsMapFromDouble(double hours) {
		Map<String, Integer> timeUnitsMap = new HashMap<String, Integer>();
		int entireHours = (int) hours;
		double hoursFraction = hours - entireHours;
		double decimalMinutes = hoursFraction * 60;
		long minutes = (long) Math.floor(decimalMinutes);
		double minutesFraction = decimalMinutes - minutes;
		double decimalSeconds = minutesFraction * 60;
		long seconds = Math.round(decimalSeconds);
		timeUnitsMap.put(MSPROJECT_TIME_UNITS.HOUR, Integer.valueOf(entireHours));
		timeUnitsMap.put(MSPROJECT_TIME_UNITS.MINUTE, Integer.valueOf((int) minutes));
		timeUnitsMap.put(MSPROJECT_TIME_UNITS.SECOND, Integer.valueOf((int) seconds));
		return timeUnitsMap;
	}

	public static String addHoursToCalendarTime(String calendarTime, double hours, NumberFormat numberFormatTimeSpan) {
		if (Math.abs(hours) < 0.000001) {
			// midnight this day
			return "00:00:00";
		}
		Map<String, Integer> originalCalendarTimeUnits = getTimeUnitsMapFromCalendarTime(calendarTime);
		Map<String, Integer> differenceTimeUnits = getTimeUnitsMapFromDouble(hours);
		Integer originalHours = originalCalendarTimeUnits.get(MSPROJECT_TIME_UNITS.HOUR);
		if (originalHours == null) {
			originalHours = Integer.valueOf(0);
		}
		Integer differenceHours = differenceTimeUnits.get(MSPROJECT_TIME_UNITS.HOUR);
		if (differenceHours == null) {
			differenceHours = Integer.valueOf(0);
		}
		Integer originalMinutes = originalCalendarTimeUnits.get(MSPROJECT_TIME_UNITS.MINUTE);
		if (originalMinutes == null) {
			originalMinutes = Integer.valueOf(0);
		}
		Integer differenceMinutes = differenceTimeUnits.get(MSPROJECT_TIME_UNITS.MINUTE);
		if (differenceMinutes == null) {
			differenceMinutes = Integer.valueOf(0);
		}
		Integer originalSeconds = originalCalendarTimeUnits.get(MSPROJECT_TIME_UNITS.SECOND);
		if (originalSeconds == null) {
			originalSeconds = Integer.valueOf(0);
		}
		Integer differenceSeconds = differenceTimeUnits.get(MSPROJECT_TIME_UNITS.SECOND);
		if (differenceSeconds == null) {
			differenceSeconds = Integer.valueOf(0);
		}
		int newHours = originalHours.intValue() + differenceHours.intValue();
		int newMinutes = originalMinutes.intValue() + differenceMinutes.intValue();
		int newSeconds = originalSeconds.intValue() + differenceSeconds.intValue();
		if (newMinutes >= 60) {
			newHours += newMinutes / 60;
			newMinutes = newMinutes % 60;
		}
		if (newHours > 24) {
			newHours = newHours % 24;
		}
		if (newSeconds >= 60) {
			newMinutes += newSeconds / 60;
			newSeconds = newSeconds % 60;
		}
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append(numberFormatTimeSpan.format(newHours));// "0");
		stringBuffer.append(calendarTimeDelimiter);
		stringBuffer.append(numberFormatTimeSpan.format(newMinutes));
		stringBuffer.append(calendarTimeDelimiter);
		stringBuffer.append(numberFormatTimeSpan.format(newSeconds));
		return stringBuffer.toString();
	}

	/**
	 * Get the time span fields into a map
	 *
	 * @param str
	 * @return
	 */
	private static Map<String, Double> getTimeSpanFields(String str) {
		Map<String, Double> timeUnitsMap = new HashMap<String, Double>();
		if (str == null || str.charAt(0) != MSPROJECT_TIME_UNITS.START_CHAR) {
			return timeUnitsMap;
		}
		StringBuilder stringBuffer = new StringBuilder(str.substring(1));
		StringBuilder gatherUnitValue = new StringBuilder();
		boolean isMinute = false;
		for (int i = 0; i < stringBuffer.length(); i++) {
			char c = stringBuffer.charAt(i);
			if (Character.isDigit(c)) {
				gatherUnitValue.append(c);
			} else {
				if (c == MSPROJECT_TIME_UNITS.TIME_SEPARATOR) {
					isMinute = true;
					continue;
				}
				if (c == MSPROJECT_TIME_UNITS.MINUTE_OR_MONTH) {
					if (isMinute) {
						timeUnitsMap.put(MSPROJECT_TIME_UNITS.MINUTE, Double.valueOf(gatherUnitValue.toString()));
					} else {
						timeUnitsMap.put(MSPROJECT_TIME_UNITS.MONTH, Double.valueOf(gatherUnitValue.toString()));
					}
				} else {
					timeUnitsMap.put(Character.toString(c), Double.valueOf(gatherUnitValue.toString()));
				}
				gatherUnitValue = new StringBuilder();
			}
		}
		return timeUnitsMap;
	}

	/**
	 * Get hours from timeSpan
	 *
	 * @param timeUnitsMap
	 * @return
	 */
	private static Double getHours(Map<String, Double> timeUnitsMap) {
		if (timeUnitsMap == null || timeUnitsMap.isEmpty()) {
			return null;
		}
		Double year = timeUnitsMap.get(MSPROJECT_TIME_UNITS.YEAR);
		Double month = timeUnitsMap.get(MSPROJECT_TIME_UNITS.MONTH);
		Double days = timeUnitsMap.get(MSPROJECT_TIME_UNITS.DAY);
		Double hours = timeUnitsMap.get(MSPROJECT_TIME_UNITS.HOUR);
		Double minutes = timeUnitsMap.get(MSPROJECT_TIME_UNITS.MINUTE);
		Double seconds = timeUnitsMap.get(MSPROJECT_TIME_UNITS.SECOND);
		double doubleHours = 0.0;
		if (year != null) {
			doubleHours += year.doubleValue() * 8 * 20 * 12;
		}
		if (month != null) {
			doubleHours += month.doubleValue() * 8 * 20;
		}
		if (days != null) {
			doubleHours += days.doubleValue() * 8;
		}
		if (hours != null) {
			doubleHours = hours.doubleValue();
		}
		if (minutes != null) {
			doubleHours += minutes.doubleValue() / 60;
		}
		if (seconds != null) {
			doubleHours += seconds.doubleValue() / (60 * 60);
		}
		return AccountingBL.roundToDecimalDigits(doubleHours, true);
	}

	public static Double getHoursFromDuration(String duration) {
		return getHours(getTimeSpanFields(duration));
	}

	/**
	 * Gets an MSProject timespan formatted string form double hours
	 *
	 * @param hours
	 * @return
	 */
	public static String getTimeSpanFromDouble(double hours) {
		int entireHours = (int) hours;
		double hoursFraction = hours - entireHours;
		double decimalMinutes = hoursFraction * 60;
		long minutes = (long) Math.floor(decimalMinutes);
		double minutesFraction = decimalMinutes - minutes;
		if (minutes >= 60) {
			entireHours += minutes / 60;
			minutes = minutes % 60;
		}
		double decimalSeconds = minutesFraction * 60;
		long seconds = Math.round(decimalSeconds);
		if (seconds >= 60) {
			// actually only ==60, never >60
			// in the case when adding two two decimal value doubles
			// results in a double with a lot of decimals (for ex.
			// 59.99999999999744)
			minutes += seconds / 60;
			seconds = seconds % 60;
		}

		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append(MSPROJECT_TIME_UNITS.START_CHAR);
		stringBuffer.append(MSPROJECT_TIME_UNITS.TIME_SEPARATOR);
		stringBuffer.append(entireHours);
		stringBuffer.append(MSPROJECT_TIME_UNITS.HOUR);
		stringBuffer.append(minutes);
		stringBuffer.append(MSPROJECT_TIME_UNITS.MINUTE);
		stringBuffer.append(seconds);
		stringBuffer.append(MSPROJECT_TIME_UNITS.SECOND);
		return stringBuffer.toString();
	}

	/**
	 * Gets an MSProject timespan formatted string form int hours and int
	 * minutes
	 *
	 * @param hours
	 * @return
	 */
	public static String getTimeSpanFromHoursAndMinutes(int _hours, int _minutes) {
		StringBuilder stringBuffer = new StringBuilder();

		int ihours = _hours;
		int iminutes = _minutes;

		if (iminutes >= 60) {
			ihours += iminutes / 60;
			iminutes = iminutes % 60;
		}
		stringBuffer.append(MSPROJECT_TIME_UNITS.START_CHAR);
		stringBuffer.append(MSPROJECT_TIME_UNITS.TIME_SEPARATOR);
		stringBuffer.append(ihours);
		stringBuffer.append(MSPROJECT_TIME_UNITS.HOUR);
		stringBuffer.append(iminutes);
		stringBuffer.append(MSPROJECT_TIME_UNITS.MINUTE);
		stringBuffer.append(0);
		stringBuffer.append(MSPROJECT_TIME_UNITS.SECOND);
		return stringBuffer.toString();
	}

	/**
	 * Gets the taskUID to workItemID mapping for existing tasks
	 *
	 * @param trackPlusTasks
	 * @return
	 */
	public static Map<Integer, Integer> getTaskUIDToWorkItemIDMap(Map<Integer, TMSProjectTaskBean> trackPlusTasks) {
		Map<Integer, Integer> taskUIDToWorkItemIDMap = new HashMap<Integer, Integer>();
		Iterator<TMSProjectTaskBean> itrMSProjectTasks = trackPlusTasks.values().iterator();
		while (itrMSProjectTasks.hasNext()) {
			TMSProjectTaskBean projectTaskBean = itrMSProjectTasks.next();
			taskUIDToWorkItemIDMap.put(projectTaskBean.getUniqueID(), projectTaskBean.getWorkitem());
		}
		return taskUIDToWorkItemIDMap;
	}

	/**
	 * Gets the workItemID to taskUID mapping for existing tasks
	 *
	 * @param trackPlusTasks
	 * @return
	 */
	public static Map<Integer, Integer> getWorkItemIDToTaskUIDMap(Map<Integer, TMSProjectTaskBean> trackPlusTasks) {
		Map<Integer, Integer> workItemIDToTaskUIDToMap = new HashMap<Integer, Integer>();
		Iterator<TMSProjectTaskBean> itrMSProjectTasks = trackPlusTasks.values().iterator();
		while (itrMSProjectTasks.hasNext()) {
			TMSProjectTaskBean projectTaskBean = itrMSProjectTasks.next();
			workItemIDToTaskUIDToMap.put(projectTaskBean.getWorkitem(), projectTaskBean.getUniqueID());
		}
		return workItemIDToTaskUIDToMap;
	}
}
