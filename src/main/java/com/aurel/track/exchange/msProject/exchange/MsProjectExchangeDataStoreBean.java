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


package com.aurel.track.exchange.msProject.exchange;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;

import org.w3c.dom.Element;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
/**
 * Helper object to store the export and import related data in a structured format
 * @author Tamas
 *
 */
public class MsProjectExchangeDataStoreBean {

	/**
	 * Elements directly below project
	 * @author Tamas
	 *
	 */
	public interface PROJECT_ELEMENTS {
		//single element 		
		public static String SUBJECT = "Subject";
		public static String CREATIONDATE = "CreationDate";
		public static String LASTSAVED = "LastSaved";		
		public static String STARTDATE = "StartDate";
		public static String FINISHDATE = "FinishDate";
		public static String DEFAULTTASKTYPE = "DefaultTaskType";
		public static String CURRENCYSYMBOL = "CurrencySymbol";
		public static String MINUTESPERDAY = "MinutesPerDay";
		public static String MINUTESPERWEEK= "MinutesPerWeek";
		public static String DAYSPERMONTH= "DaysPerMonth";
		public static String DURATIONFORMAT= "DurationFormat";
		public static String WEEKSTARTDAY= "WeekStartDay";
		public static String STATUSDATE= "StatusDate";
		public static String CURRENTDATE= "CurrentDate";
		
		//project lists
		public static String OUTLINECODES = "OutlineCodes";
		public static String WBSMASKS = "WBSMasks";
		public static String EXTENDEDATTRIBUTES = "ExtendedAttributes";
		public static String CALENDARS = "Calendars";
		public static String TASKS = "Tasks";
		public static String RESOURCES = "Resources";
		public static String ASSIGNMENTS = "Assignments";
	}
	
	/**
	 * Subelements of project elements
	 * @author Tamas
	 *
	 */
	public interface PROJECT_SUBELEMENTS {
		public static String OUTLINECODE = "OutlineCode";
		public static String WBSMask = "WBSMask";
		public static String EXTENDEDATTRIBUTE = "ExtendedAttribute";
		public static String CALENDAR = "Calendar";
		public static String TASK = "Task";
		public static String RESOURCE = "Resource";
		public static String ASSIGNMENT = "Assignment";
	}
	
	/**
	 * Elements used in more contexts
	 * @author Tamas
	 *
	 */
	public interface COMMON_ELEMENTS {
		public static String UID = "UID";
		public static String ID = "ID";
		public static String NAME = "Name";
		public static String Type = "Type";
		public static String START = "Start";
		public static String FINISH = "Finish";
		public static String WORK = "Work";
		public static String OVERTIMEWORK = "OvertimeWork";
		public static String ACTUALSTART = "ActualStart";
		public static String ACTUALFINISH = "ActualFinish";	
		public static String ACTUALWORK = "ActualWork";
		public static String ACTUALOVERTIMEWORK = "ActualOvertimeWork";
		public static String REGULARWORK = "RegularWork";
		public static String REMAININGWORK = "RemainingWork";
	}
	
	/**
	 * Task specific elements 
	 * @author Tamas
	 *
	 */
	public interface TASK_ELEMENTS {
		public static String CreateDate = "CreateDate";
		public static String CONTACT = "Contact";
		public static String WBS = "WBS";
		public static String WBSLEVEL = "WBSLevel";
		public static String OUTLINENUMBER = "OutlineNumber";
		public static String OUTLINELEVEL = "OutlineLevel";
		public static String DURATION = "Duration";
		public static String DURATIONFORMAT = "DurationFormat";
		public static String ESTIMATED = "Estimated";
		public static String MILESTONE = "Milestone";
		public static String SUMMARY = "Summary";
		public static String ACTUALDURATION = "ActualDuration";
		public static String REMAININGDURATION = "RemainingDuration";
		public static String CONSTRAINTTYPE = "ConstraintType";
		public static String CONSTRAINTDATE = "ConstraintDate";
		public static String DEADLINE = "Deadline";
		public static String NOTES = "Notes";
		public static String PREDECESSORLINK = "PredecessorLink"; 
	}
	
	/**
	 * The type of the task
	 * @author Tamas
	 *
	 */
	public interface TASK_TYPE {
		public static int FIXED_UNITS = 0;
		public static int FIXED_DURATION = 1;
		public static int FIXED_WORK = 2;
	}
	
	/**
	 * Task constraint type
	 * @author Tamas
	 *
	 */
	public interface CONSTRAINT_TYPE {
		public static int AS_SOON_AS_POSSIBLE = 0;
		public static int AS_LATE_AS_POSSIBLE = 1;
		public static int MUST_START_ON = 2;
		public static int MUST_FINISH_ON = 3;
		public static int START_NO_EARLIER_THAN = 4;
		public static int START_NO_LATER_THAN = 5;
		public static int FINISH_NO_EARLIER_THAN = 6;
		public static int FINISH_NO_LATER_THAN = 7;
	}
	
	/**
	 * The predecessor link specific elements
	 * @author Tamas
	 *
	 */
	public interface PREDECESSOR_ELEMENTS {
		public static String PREDECESSORUID = "PredecessorUID";
		public static String CROSSPROJECT = "CrossProject";
		public static String CROSSPROJECTNAME = "CrossProjectName";
		public static String LINKLAG = "LinkLag";
		public static String LAGFORMAT = "LagFormat";
	}
	
	/**
	 * Predecessor link type
	 * @author Tamas
	 *
	 */
	public interface PREDECESSOR_ELEMENT_TYPE {
		public static int FF = 0;
		public static int FS = 1;
		public static int SF = 2;
		public static int SS = 3;
	}
	
	/**
	 * Predecessor link type
	 * @author Tamas
	 *
	 */
	public interface PREDECESSOR_CROSS_PROJECT {
		public static int NOT_CROSS_PROJECT = 0;
		public static int CROSS_PROJECT = 1;
	}
	
	/**
	 * Assignment specific elements   
	 * @author Tamas
	 *
	 */
	public interface ASSIGNMENT_ELEMENTS {
		public static String TASKUID = "TaskUID";
		public static String RESOURCEUID = "ResourceUID";
		public static String UNITS = "Units";
		public static String TIMEPHASEDDATA = "TimephasedData";
	}
	
	/**
	 * Calendar specific elements
	 * @author Tamas
	 *
	 */
	public interface CALENDAR_ELEMENTS {
		public static String IsBaseCalendar = "IsBaseCalendar";
		public static String BaseCalendarUID = "BaseCalendarUID";
		public static String WeekDays = "WeekDays";
	}
	
	/**
	 * Is base or not
	 * @author Tamas
	 *
	 */
	public interface IS_BASE_CALENEDAR {
		public static int NOT_BASE = 0;
		public static int BASE = 1;
	}
	
	/**
	 * WeekDays contain WeekDay elements
	 * @author Tamas
	 *
	 */
	public interface WEEKDAYS_ELEMENTS {
		public static String WeekDay = "WeekDay";
	}
	
	/**
	 * Weekday specific elements
	 * @author Tamas
	 *
	 */
	public interface WEEKDAY_ELEMENTS {
		public static String DayType = "DayType";
		public static String DayWorking = "DayWorking";
		public static String TimePeriod = "TimePeriod";
		public static String WorkingTimes = "WorkingTimes";
	}
	
	/**
	 * Day types: Exception - exception day with specific WorkingTimes
	 * 			Weekday - specific WorkingTimes for a weekday
	 * @author Tamas
	 *
	 */
	public interface DAY_TYPE {
		public static int Exception = 0;
		public static int Sunday = 1;
		public static int Monday = 2;
		public static int Tuesday = 3;
		public static int Wednesday = 4;
		public static int Thursday = 5;
		public static int Friday = 6;
		public static int Saturday = 7;
	}
	
	/**
	 * Only the works on a workDay are taken into account
	 * @author Tamas
	 *
	 */
	public interface DAY_WORKING {
		public static int NON_WORKING = 0;
		public static int WORKING = 1;
	}
	
	/**
	 * Timeperiod intervals for workingTimes
	 * @author Tamas
	 *
	 */
	public interface TIMEPERIOD_ELEMENTS {
		public static String FromDate = "FromDate";
		public static String ToDate = "ToDate";
	}
	
	/**
	 * WorkingTimes contain WorkingTime elements
	 * @author Tamas
	 *
	 */
	public interface WORKINGTIMES_ELEMENTS {
		public static String WorkingTime = "WorkingTime";
	}
	
	/**
	 * Worktime limits
	 * @author Tamas
	 *
	 */
	public interface WORKINGTIME_ELEMENTS {
		public static String FromTime = "FromTime";
		public static String ToTime = "ToTime";
	}
	
	/**
	 * Resource specific elements   
	 * @author Tamas
	 *
	 */
	public interface RESOURCE_ELEMENTS {
		public static String NTAccount = "NTAccount";
		public static String EmailAddress = "EmailAddress";
		public static String CalendarUID = "CalendarUID";
	}
		
	/**
	 * Resource type
	 * @author Tamas
	 *
	 */
	public interface RESOURCE_TYPE {
		public static int MATERIAL = 0;
		public static int WORK = 1;	
		public static int COST = 2;
	}
	
	/**
	 * The number entered by the user as Work is taken by default as the WorkFormat set for the project
	 * The user can still enter a number and another WorkFormat character(s). 
	 * In this case this WorkFormat character(s) is taken into account 
	 * but the result in UI will be the number multiplied/divided according to the WorkFormat set by project
	 * At the XML level the Work values are stored as duration for ex. PT24H0M0S 
	 * So there is no any reason to export/import the workFormat
	 * @author Tamas	 
	 */
	/*public interface WORK_FORMAT {
		public static int m = 1;
		public static int h = 2;
		public static int d = 3;
		public static int w = 4;
		public static int mo = 5;	
	}*/
	
	/**
	 * The possible lag formats or duration formats shown to the user
	 * LAG_FORMAT context: at the XML level LinkLags are stored in tents of minute. 
	 * 		The LAG_FORMAT is used as calculation base from tents of minute to the corresponding format 
	 * As DURATION_FORMAT context: at the XML level times are stored as duration for ex. PT24H0M0S
	 * 		Contrary to WORK_FORMAT the format specified by the user remains stored and is not automatically 
	 * 		recalculated to the DurationFormat set at project level
	 * @author Tamas
	 *
	 */
	public interface LAG_FORMAT {
		//e->elapsed, % -> PERCENT, ? -> EST(IMATED)
		public static int m = 3;
		public static int em = 4;
		public static int h = 5;
		public static int eh = 6;
		public static int d = 7;
		public static int ed = 8;
		public static int w = 9;
		public static int ew = 10;
		public static int mo = 11;
		public static int emo = 12;
		public static int PERCENT=19;
		public static int ePERCENT = 20;
		public static int mEST = 35;
		public static int emEST = 36;
		public static int hEST = 37;
		public static int ehEST = 38;
		public static int dEST = 39;
		public static int edEST = 40;
		public static int wEST = 41;
		public static int ewEST= 42;
		public static int moEST = 43;
		public static int emoEST = 44;
		public static int PERCENTEST = 51;
		public static int ePERCENTEST = 52;
	}  
	
	/**
	 * TimephasedData elements   
	 * @author Tamas
	 *
	 */
	public interface TIMEPHASEDDATA_ELEMENTS {
		public static String Unit = "Unit";
		public static String Value = "Value";
	}	
	
	/**
	 * TimephasedData type
	 * @author Tamas
	 *
	 */
	public interface TIMEPHASEDDATA_TYPE {
		public static int ASSIGNMENT_REMAINING_WORK = 1;
		public static int ASSIGNMENT_ACTUAL_WORK = 2;
	}
	
	/**
	 * TimephasedData type
	 * @author Tamas
	 *
	 */
	public interface TIMEPHASEDDATA_UNIT {
		public static int HOURS = 1;
		public static int DAYS = 2;
	}
	
	/**
	 * Whether a task is summary
	 * @author Tamas
	 *
	 */
	public interface TASK_SUMMARY_TYPE {
		public static int NOT_SUMMARY = 0;
		public static int SUMMARY = 1;
	}
	
	//direct data		
	private TPersonBean personBean;
	private Integer entityID;
	private int entityType;
	private File file = null;
	private ProjectFile project;
	private Locale locale;

	//derived data
	private Integer projectID = null;
	private TProjectBean projectBean = null;
	
	private Integer releaseScheduledID = null;
	private Integer issueType; 
	private Map<Integer, Integer> resourceUIDToPersonIDMap;
	
	//the entire document	
	//ms project configuration data 
	private String name;
	private String subject;
	private Date lastSavedDate;
	private Element rootElement;
	private List<Task> tasks;
	private List<Resource> resources;
	private List<Resource> workResources;
	private List<ResourceAssignment> assignments;
	
	public MsProjectExchangeDataStoreBean(TPersonBean personBean, Locale locale){
		this.personBean = personBean;
		this.locale = locale;
	}

	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}

	public List<Task> getTasks() {
		return tasks;
	}


	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}


	public List<Resource> getResources() {
		return resources;
	}


	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}


	public List<Resource> getWorkResources() {
		return workResources;
	}


	public void setWorkResources(List<Resource> workResources) {
		this.workResources = workResources;
	}

	public List<ResourceAssignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<ResourceAssignment> assignments) {
		this.assignments = assignments;
	}

	public TPersonBean getPersonBean() {
		return personBean;
	}

	public Integer getEntityID() {
		return entityID;
	}

	public void setEntityID(Integer entityID) {
		this.entityID = entityID;
	}

	public int getEntityType() {
		return entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public Locale getLocale() {
		return locale;
	}


	public Integer getProjectID() {
		return projectID;
	}


	public TProjectBean getProjectBean() {
		return projectBean;
	}

	
	public void setProjectBean(TProjectBean projectBean) {
		this.projectBean = projectBean;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public Integer getReleaseScheduledID() {
		return releaseScheduledID;
	}
	
	public void setReleaseScheduledID(Integer releaseScheduledID) {
		this.releaseScheduledID = releaseScheduledID;
	}

	public Integer getIssueType() {
		return issueType;
	}

	public void setIssueType(Integer issueType) {
		this.issueType = issueType;
	}

	public Map<Integer, Integer> getResourceUIDToPersonIDMap() {
		return resourceUIDToPersonIDMap;
	}

	public void setResourceUIDToPersonIDMap(
			Map<Integer, Integer> resourceUIDToPersonIDMap) {
		this.resourceUIDToPersonIDMap = resourceUIDToPersonIDMap;
	}

	public Date getLastSavedDate() {
		return lastSavedDate;
	}

	public void setLastSavedDate(Date lastSavedDate) {
		this.lastSavedDate = lastSavedDate;
	}

	public Element getRootElement() {
		return rootElement;
	}

	public void setRootElement(Element rootElement) {
		this.rootElement = rootElement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public ProjectFile getProject() {
		return project;
	}

	public void setProject(ProjectFile project) {
		this.project = project;
	}

	
}
