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

package com.aurel.track.linkType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.calendar.WorkDaysConfig;
import com.aurel.track.calendar.WorkDaysConfigImplementation;
import com.aurel.track.calendar.WorkItemWithNewCascadedDates;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemLinkDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean.PREDECESSOR_ELEMENT_TYPE;
import com.aurel.track.exchange.msProject.importer.LinkLagBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.prop.ApplicationBean;

public class MsProjectLinkTypeBL {

	public static Logger LOGGER = LogManager.getLogger(MsProjectLinkTypeBL.class);

	private static WorkItemLinkDAO workItemLinkDao = DAOFactory.getFactory().getWorkItemLinkDAO();

	/**
	 * In case of a changed work item violates one or more pred. dependency: this method returns a map containing violated dependencies id.
	 * (This method check if actual (changed) work item new start/end date is valid when actual work item is represented as a succ.)
	 * @param startDate
	 * @param endDate
	 * @param workItemParam
	 * @return
	 * @throws ItemLoaderException
	 */
	public static Set<Integer> checkSuccMoveValidity(Date startDate, Date endDate,
			Integer workItemID, Integer personID, TWorkItemLinkBean actualCreatedLink) {
		Map<Integer, Integer> msProjectIdMap = new HashMap<Integer, Integer>();
		MsProjectLinkType msProjectLinkType = MsProjectLinkType.getInstance();
		List<Integer> msProjectLink = LinkTypeBL.getLinkTypesByPluginClass(msProjectLinkType);
		for (int i = 0; i < msProjectLink.size(); i++) {
			int type = (Integer)msProjectLink.get(i);
			msProjectIdMap.put(type, type);
		}
		Set<Integer> problems = new HashSet<Integer>();
		List<TWorkItemLinkBean> links = workItemLinkDao.loadByWorkItemSucc(workItemID);
		if(actualCreatedLink != null) {
			links.add(actualCreatedLink);
		}

		int[] workItemIds;
		ArrayList<Integer> workItemIdsList = new ArrayList<Integer>();
		for (TWorkItemLinkBean actualLinkBean : links) {
			if(msProjectIdMap.containsKey(actualLinkBean.getLinkType())) {
				workItemIdsList.add(actualLinkBean.getLinkPred());
			}
		}

		workItemIds = new int[workItemIdsList.size()];
		for (int i = 0; i < workItemIdsList.size(); i++) {
			workItemIds[i] = workItemIdsList.get(i);
		}
		List<TWorkItemBean> predList = new ArrayList<TWorkItemBean>();
		predList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIds, personID, false, false, false);
		Map<Integer, TWorkItemBean> predMap = new HashMap<Integer, TWorkItemBean>();
		for (int i = 0; i < predList.size(); i++) {
			predMap.put(predList.get(i).getObjectID(), predList.get(i));
		}
		for (TWorkItemLinkBean actualLinkBean : links) {
			if(msProjectIdMap.containsKey(actualLinkBean.getLinkType())) {
				Integer dependencyType = actualLinkBean.getIntegerValue1();
				int predWorkItemId = actualLinkBean.getLinkPred();
				TWorkItemBean predWorkItemBean = predMap.get(predWorkItemId);
				Date predWorkItemStartDate = getStartDate(predWorkItemBean);
				//in case of milestone work item end date is missing
				Date predWorkItemEndDate = getEndDate(predWorkItemBean);
				if(predWorkItemEndDate != null && predWorkItemStartDate != null) {
					if(predWorkItemBean.isMilestone()) {
						predWorkItemEndDate = predWorkItemStartDate;
					}
					if(actualLinkBean.getLinkLag() != 0 && checkLinkLagAvaibilityForCascade(actualLinkBean.getLinkLagFormat())) {
						int linkLagInDays = getLinkLagInDays(actualLinkBean, workItemID);
						if(linkLagInDays > 0) {
							predWorkItemEndDate = stepForward(predWorkItemEndDate, linkLagInDays, true);
							predWorkItemStartDate = stepBack(predWorkItemStartDate, linkLagInDays, true);
						}else if(linkLagInDays < 0) {
							predWorkItemEndDate = stepBack(predWorkItemEndDate, Math.abs(linkLagInDays), true);
							predWorkItemStartDate = stepForward(predWorkItemStartDate, Math.abs(linkLagInDays), true);
						}
					}
					if (dependencyType!=null) {
						switch(dependencyType) {
						case PREDECESSOR_ELEMENT_TYPE.FS:
							if (predWorkItemEndDate.compareTo(startDate) >= 0) {
								if(!problems.contains(actualLinkBean.getObjectID())) {
									problems.add(actualLinkBean.getObjectID());
								}
							}
							break;
						case PREDECESSOR_ELEMENT_TYPE.FF:
							if (predWorkItemEndDate.compareTo(endDate) >= 0) {
								if(!problems.contains(actualLinkBean.getObjectID())) {
									problems.add(actualLinkBean.getObjectID());
								}
							}
							break;
						case PREDECESSOR_ELEMENT_TYPE.SF:
							if (predWorkItemStartDate.compareTo(endDate) > 0) {
								if(!problems.contains(actualLinkBean.getObjectID())) {
									problems.add(actualLinkBean.getObjectID());
								}
							}
							break;
						case PREDECESSOR_ELEMENT_TYPE.SS:
							if (predWorkItemStartDate.compareTo(startDate) > 0) {
								if(!problems.contains(actualLinkBean.getObjectID())) {
									problems.add(actualLinkBean.getObjectID());
								}
							}
							break;
						}
					}
				}
			}
		}
		return problems;
	}

	/**
	 * This method checks if the parameters work item's new start/end date dependencies are valid with his preds.
	 * @param originalMovedWorkItemBean
	 * @param workItemParam
	 * @param startdateParam
	 * @param endDateParam
	 * @param workItemLinkDao
	 * @return
	 * @throws ItemLoaderException
	 */
	public static boolean checkWorkItemAndPredsDependencyViolation(TWorkItemBean originalMovedWorkItemBean, TWorkItemBean workItemParam, Date startdateParam,
			Date endDateParam, Integer personID, Map<Integer, WorkItemWithNewCascadedDates> itemCascadeMap,
			List<Integer> msProjectLinkTypeIDs, Date startDate, Date endDate) throws ItemLoaderException {
		boolean violates = false;
		List<TWorkItemLinkBean> links = workItemLinkDao.loadByWorkItemSucc(workItemParam.getObjectID());
		List<Integer> workItemIDsList = new ArrayList<Integer>();
		for (TWorkItemLinkBean workItemLinkBean : links) {
			if(msProjectLinkTypeIDs.contains(workItemLinkBean.getLinkType())) {
				workItemIDsList.add(workItemLinkBean.getLinkPred());
			}
		}
		int[]workItemIDs = org.apache.commons.lang.ArrayUtils.toPrimitive(workItemIDsList.toArray(new Integer[workItemIDsList.size()]));
		List<TWorkItemBean> predList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIDs, personID, false, false, false);
		Map<Integer, TWorkItemBean> predMap = new HashMap<Integer, TWorkItemBean>();
		for (int i = 0; i < predList.size(); i++) {
			predMap.put(predList.get(i).getObjectID(), predList.get(i));
		}
		for(int i = 0; i < links.size(); i++) {
			TWorkItemLinkBean actualLinkBean = links.get(i);
			if (msProjectLinkTypeIDs.contains(actualLinkBean.getLinkType())) {
				int dependencyType = actualLinkBean.getIntegerValue1();
				int predWorkItemId = actualLinkBean.getLinkPred();
				TWorkItemBean predWorkItemBean = predMap.get(predWorkItemId);
				Date predWorkItemStartDate = getStartDate(predWorkItemBean);
				Date predWorkItemEndDate = getEndDate(predWorkItemBean);
				if(originalMovedWorkItemBean.getObjectID().equals(predWorkItemBean.getObjectID())) {
					predWorkItemStartDate = startDate;
					predWorkItemEndDate = endDate;
				}
				if(itemCascadeMap.get(predWorkItemId) == null && predWorkItemId != originalMovedWorkItemBean.getObjectID()) {
					switch(dependencyType) {
						case PREDECESSOR_ELEMENT_TYPE.FS:
							if (predWorkItemEndDate!=null && predWorkItemEndDate.compareTo(startdateParam) >= 0) {
								violates = true;
							}
							break;
						case PREDECESSOR_ELEMENT_TYPE.FF:
							if (predWorkItemEndDate!=null && predWorkItemEndDate.compareTo(endDateParam) >= 0) {
								violates = true;
							}
							break;
						case PREDECESSOR_ELEMENT_TYPE.SF:
							if (predWorkItemStartDate!=null && predWorkItemStartDate.compareTo(endDateParam) > 0) {
								violates = true;
							}
							break;
						case PREDECESSOR_ELEMENT_TYPE.SS:
							if (predWorkItemStartDate!=null && predWorkItemStartDate.compareTo(startdateParam) > 0) {
								violates = true;
							}
							break;
					}
				}
			}
		}
		return violates;
	}

	/**
	 * This method returns true if the link lag unit is: d(day), mo(month), w(week)
	 * @param linkLagType
	 * @return
	 */
	public static boolean checkLinkLagAvaibilityForCascade(int linkLagType) {
		if(MsProjectExchangeDataStoreBean.LAG_FORMAT.d == linkLagType ||
				MsProjectExchangeDataStoreBean.LAG_FORMAT.mo == linkLagType
				|| MsProjectExchangeDataStoreBean.LAG_FORMAT.w == linkLagType ) {

			return true;
		}
		return false;
	}

	/**
	 * This method returns true if actualWorkItem new start/end dates require cascading childWorkItem.
	 * This method returns false if actualWorkItem new start/end dates not violating dependency with childWorkItem => childWorkItem doesn't needs cascading.
	 * @param actualWorkItem
	 * @param childWorkItem
	 * @param link
	 * @return
	 */
	public static boolean checkWhichChildWorkItemNeedCascadingRight(TWorkItemBean actualWorkItem, TWorkItemBean childWorkItem, TWorkItemLinkBean link, Date startDate, Date endDate) {
		boolean cascadeNeed = false;
		if(getStartDate(childWorkItem) == null && getEndDate(childWorkItem) == null) {
			return cascadeNeed;
		}
		int dependencyType = link.getIntegerValue1();
		Date newStartDate = startDate;
		Date newEndDate = endDate;
		if(actualWorkItem.isMilestone()) {
			newEndDate = newStartDate;
		}
		if(link.getLinkLag() != 0 && checkLinkLagAvaibilityForCascade(link.getLinkLagFormat())) {

			int linkLagInDays = getLinkLagInDays(link, actualWorkItem.getObjectID());

			if(linkLagInDays > 0) {
				newEndDate = stepForward(newEndDate, linkLagInDays, true);
				newStartDate = stepBack(newStartDate, linkLagInDays, true);
			}
		}
		switch(dependencyType) {
			case PREDECESSOR_ELEMENT_TYPE.FS:
				if (newEndDate.compareTo(getStartDate(childWorkItem)) >= 0) {
					cascadeNeed = true;
				}
				break;
			case PREDECESSOR_ELEMENT_TYPE.FF:
				if (newEndDate.compareTo(getEndDate(childWorkItem)) >= 0) {
					cascadeNeed = true;
				}
				break;
			case PREDECESSOR_ELEMENT_TYPE.SF:
				if (newStartDate.compareTo(getEndDate(childWorkItem)) > 0) {
					cascadeNeed = true;
				}
				break;
			case PREDECESSOR_ELEMENT_TYPE.SS:
				if (newStartDate.compareTo(getStartDate(childWorkItem)) > 0) {
					cascadeNeed = true;
				}
				break;
		}
		return cascadeNeed;
	}


	/**
	 * This method subtracts steps from dateParam and returns. If withCheckingFreeDays == true
	 * then weekends, and free days doesn't treats as work day.
	 * @param dateParam
	 * @param steps
	 * @param withCheckingFreeDays
	 * @return
	 */
	public static Date stepBack(Date dateParam, int steps, boolean withCheckingFreeDays) {
		if (dateParam==null) {
			return null;
		}
		int actualSteps = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateParam);
		Date newDate = new Date();
		while(actualSteps < steps) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			newDate = cal.getTime();
			if(withCheckingFreeDays) {
				if (!WorkDaysConfigImplementation.isSaturday(newDate) && !WorkDaysConfigImplementation.isSunday(newDate) && !WorkDaysConfigImplementation.isFreeDay(newDate)) {
					actualSteps++;
				}
			}else {
				actualSteps++;
			}
		}
		return newDate;
	}

	/**
	 * This method adds steps to dateParam and returns. If withCheckingFreeDays == true
	 * then weekends, and free days doesn't treats as work day.
	 * @param dateParam
	 * @param steps
	 * @param withCheckingFreeDays
	 * @return
	 */
	public static Date stepForward(Date dateParam, int steps, boolean withCheckingFreeDays) {
		if (dateParam==null) {
			return null;
		}
		int actualSteps = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateParam);
		Date newDate = new Date();
		while(actualSteps < steps) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			newDate = cal.getTime();
			if(withCheckingFreeDays) {
				if (!WorkDaysConfigImplementation.isSaturday(newDate) && !WorkDaysConfigImplementation.isSunday(newDate) && !WorkDaysConfigImplementation.isFreeDay(newDate)) {
					actualSteps++;
				}
			}else {
				actualSteps++;
			}
		}
		return newDate;
	}

	/**
	 * This method returns true if workItemBean has parent(s).
	 * @param workItemBean
	 * @return
	 */
	public static boolean hasParent(List<Integer> msProjectLinkTypeIDs, Integer workItemID) {
		List<TWorkItemLinkBean> workItemLinkBeans = workItemLinkDao.loadByWorkItemSucc(workItemID);
		if (workItemLinkBeans!=null && !workItemLinkBeans.isEmpty()) {
			for (TWorkItemLinkBean workItemLinkBean : workItemLinkBeans) {
				if (msProjectLinkTypeIDs.contains(workItemLinkBean.getLinkType())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method returns true if actualWorkItem is a leaf.
	 * @param actualWorkItem
	 * @return
	 */
	public static boolean isLeaf(TWorkItemBean actualWorkItem, List<Integer> msProjectLinkTypeIDs) {
		List<TWorkItemLinkBean> workItemLinkBeans = workItemLinkDao.loadByWorkItemPred(actualWorkItem.getObjectID());
		if (workItemLinkBeans!=null && workItemLinkBeans.size()!=0) {
			for (TWorkItemLinkBean workItemLinkBean : workItemLinkBeans) {
				if (msProjectLinkTypeIDs.contains(workItemLinkBean.getLinkType())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns number of free days from given interval, start date included always, endDate only if includeLastDay == true!
	 * @param startDateParam
	 * @param endDateParam
	 * @param includeLastDay
	 * @return
	 */
	public static Integer getNumberOfDaysBetweenDates(Date startDateParam, Date endDateParam, boolean includeLastDay) {
		DateTime dateTime1 = new DateTime(startDateParam);
		DateTime dateTime2 = new DateTime(endDateParam);
		if(includeLastDay) {
			dateTime2 = dateTime2.plusDays(1);
		}
		int numberOfDays = Days.daysBetween(dateTime1, dateTime2).getDays();
		return numberOfDays;
	}

	/**
	 * Returns number of workdays from  interval(startDateParam -> endDateParam).
	 * @param startDateParam
	 * @param endDateParam
	 * @return
	 */
	public static int getNumberOfWorkDaysFromMovedIntervall(Date startDateParam, Date endDateParam) {
		int numberOfDays = getNumberOfDaysBetweenDates(startDateParam, endDateParam, false);
		if (!WorkDaysConfig.SATURDAY_WORK_DAY) {
			numberOfDays = numberOfDays - getNumberOfSaturdaysFromInterval(startDateParam, endDateParam, false);
		}
		if (!WorkDaysConfig.SUNDAY_WORK_DAY) {
			numberOfDays = numberOfDays - getNumberOfSundaysFromInterval(startDateParam, endDateParam, false);
		}
		numberOfDays = numberOfDays - getNumberOfFreeDaysFromIntervall(startDateParam, endDateParam);
		return numberOfDays;
	}

	/**
	 * Returns number of saturdays from interval. Start date included always, endDate only if includeLastDay == true!
	 * @param startDateParam
	 * @param endDateParam
	 * @param includeLastDay
	 * @return
	 */
	public static Integer getNumberOfSaturdaysFromInterval(Date startDateParam, Date endDateParam, boolean includeLastDay) {
		Date realEndDate = endDateParam;
		if (includeLastDay) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(realEndDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			realEndDate = cal.getTime();
		}
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(startDateParam);
	    int numOfSaturdays = 0;
	    while(!calendar.getTime().after(realEndDate)) {
	        if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {
	        	numOfSaturdays++;
	        }
	        calendar.add(Calendar.DAY_OF_MONTH, 1);
	    }
	    return numOfSaturdays;
	}

	/**
	 * Returns number of sundays from interval. Start date included always, endDate only if includeLastDay == true!
	 * @param startDateParam
	 * @param endDateParam
	 * @param includeLastDay
	 * @return
	 */
	public static Integer getNumberOfSundaysFromInterval(Date startDateParam, Date endDateParam, boolean includeLastDay) {
		Date realEndDate = endDateParam;
		if (includeLastDay) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(realEndDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			realEndDate = cal.getTime();
		}
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(startDateParam);
	    int numOfSunday = 0;
	    while(!calendar.getTime().after(realEndDate)) {
	        if ( calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {
	        	numOfSunday++;
	        }
	        calendar.add(Calendar.DAY_OF_MONTH, 1);
	    }
	    return numOfSunday;
	}

	/**
	 * Returns number of days from interval. start date included always, endDate only if includeLastDay == true!
	 * @param startDateParam
	 * @param endDateParam
	 * @return
	 */
	public static Integer getNumberOfFreeDaysFromIntervall(Date startDateParam, Date endDateParam) {
		int numberOfFreeDays = 0;
		for (Map.Entry<Date, String> entry : WorkDaysConfig.exceptionFromWorkDays.entrySet()) {
		    if (entry.getKey().getTime() <= endDateParam.getTime() && entry.getKey().getTime() >= startDateParam.getTime()) {
		    	numberOfFreeDays++;
		    }
		}
		return numberOfFreeDays;
	}

	/**
	 * Returns number of workdays from given interval, including start and end date
	 * @param startDateParam
	 * @param endDateParam
	 * @return
	 */
	public static int getNumberOfWorkDaysBetweenDates(Date startDateParam, Date endDateParam) {
		int numberOfDays = getNumberOfDaysBetweenDates(startDateParam, endDateParam, true);
		if (!WorkDaysConfig.SATURDAY_WORK_DAY) {
			numberOfDays = numberOfDays - getNumberOfSaturdaysFromInterval(startDateParam, endDateParam, false);
		}
		if (!WorkDaysConfig.SUNDAY_WORK_DAY) {
			numberOfDays = numberOfDays - getNumberOfSundaysFromInterval(startDateParam, endDateParam, false);
		}
		numberOfDays = numberOfDays - getNumberOfFreeDaysFromIntervall(startDateParam, endDateParam);
		return numberOfDays;
	}

	/**
	 * This method checks if the new start/end dates is valid. (not violating some preds. dependency)
	 * If new state is not valid then workItemBean and workItemBean's all succss will not be saved.s
	 * @param workItemBean
	 */
	//TODO Remove db. access from cycle
	public static void leftMovePredDependencyViolation(TWorkItemBean workItemBean, Integer personID, Map<Integer,
			WorkItemWithNewCascadedDates> itemCascadeMap, List<Integer> msProjectLinkTypeIDs, Date startDate, Date endDate) {
		for(Map.Entry<Integer, WorkItemWithNewCascadedDates> entry: itemCascadeMap.entrySet()) {
			if(entry.getValue().isSave()) {
				try {
					if(checkWorkItemAndPredsDependencyViolation(workItemBean, entry.getValue().getWorkItemBean(), entry.getValue().getStarDate(),
							entry.getValue().getEndDate(), personID, itemCascadeMap, msProjectLinkTypeIDs, startDate, endDate)) {
						entry.getValue().setSave(false);
						int actualWorkItemID = entry.getValue().getWorkItemBean().getObjectID();
						List<TWorkItemLinkBean> links = workItemLinkDao.loadByWorkItemPred(actualWorkItemID);
						if(links.size() > 0) {
							for(int i = 0; i < links.size(); i++) {
								itemCascadeMap.get(links.get(i).getLinkSucc()).setSave(false);
							}
						}
					}
				} catch (ItemLoaderException e) {
					LOGGER.error("Cascading error: " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * This method saves into database all cascaded work items, where save flag is true.
	 */
	public static void storeAllChangedBean(Locale locale, Map<Integer, WorkItemWithNewCascadedDates> itemCascadeMap, Integer personID, List<Integer> msProjectLinkTypeIDs) {
		for(Map.Entry<Integer, WorkItemWithNewCascadedDates> entry: itemCascadeMap.entrySet()) {
			if(entry.getValue().isSave()) {
				Set<Integer> presentField = new HashSet<Integer>();
				setPresentedFields(presentField);
				WorkItemContext workItemContext = FieldsManagerRT.getExchangeContextForExisting(entry.getValue().getWorkItemBean(), personID, locale, presentField);
				workItemContext.setExchangeImport(false);
				setStartDate(workItemContext.getWorkItemBean(), entry.getValue().getStarDate());
				setEndDate(workItemContext.getWorkItemBean(), entry.getValue().getEndDate());
				if(entry.getValue().getWorkItemBean().isMilestone()) {
					setEndDate(workItemContext.getWorkItemBean(), null);
				}
				List<ErrorData> errorsList = new LinkedList<ErrorData>();
				boolean result = FieldsManagerRT.performSave(workItemContext, errorsList, false, false/*, false*/);
				LOGGER.debug("Work Item: " + entry.getKey() + " drag drop/resize saved: Errors: " + errorsList + " Result: " + result);
			}
		}
	}

	/**
	 * This method stores work items from touchedWorkItem list, in case of this work item not exists or isSave flag is false from itemCascadeMap.
	 * @param touchedWorkItem
	 * @param personID
	 * @param locale
	 * @param msProjectLinkTypeIDs
	 * @param itemCascadeMap
	 */
	private static void storeAllTouchedDescendantWorkItem(List<WorkItemWithNewCascadedDates> touchedWorkItem, Integer personID, Locale locale, List<Integer> msProjectLinkTypeIDs,
			Map<Integer, WorkItemWithNewCascadedDates> itemCascadeMap) {
		for(WorkItemWithNewCascadedDates entry : touchedWorkItem) {
			planItem(entry.getWorkItemBean(), getStartDate(entry.getWorkItemBean()), getEndDate(entry.getWorkItemBean()), entry.getStarDate(), entry.getEndDate(), locale, msProjectLinkTypeIDs, personID, false);
			boolean isSave = true;
			if(itemCascadeMap != null) {
				if (itemCascadeMap.get(entry.getWorkItemBean().getObjectID()) != null) {
					if (!itemCascadeMap.get(entry.getWorkItemBean().getObjectID()).isSave()) {
						isSave = false;
					}
				}
			}
			if(isSave) {
				Set<Integer> presentField = new HashSet<Integer>();
				presentField.add(SystemFields.INTEGER_STARTDATE);
				presentField.add(SystemFields.INTEGER_ENDDATE);
				WorkItemContext workItemContext = FieldsManagerRT.getExchangeContextForExisting(entry.getWorkItemBean(), personID, locale, presentField);
				workItemContext.setExchangeImport(false);
				setStartDate(workItemContext.getWorkItemBean(), entry.getStarDate());
				setEndDate(workItemContext.getWorkItemBean(), entry.getEndDate());
				if(entry.getWorkItemBean().isMilestone()) {
					setEndDate(workItemContext.getWorkItemBean(), null);
				}
				List<ErrorData> errorsList = new LinkedList<ErrorData>();
				boolean result = FieldsManagerRT.performSave(workItemContext, errorsList, false, false/*, false*/);
				LOGGER.debug("Work Item: " + entry.getWorkItemBean().getObjectID() + " drag drop/resize saved: Errors: " + errorsList + " Result: " + result);
			}
		}
	}

	/**
	 * This method parses workItemBeanParam all successors recursively, cascades work items if needed.
	 * @param workItemBeanParam
	 * @param numberOfWorkedDaysFromMovedDays
	 * @param numberOfMovedDays
	 * @throws ItemLoaderException
	 */
	private static void parseLinks(TWorkItemBean workItemBeanParam, int numberOfWorkedDaysFromMovedDays, int numberOfMovedDays,
			Integer personID, Map<Integer, WorkItemWithNewCascadedDates> itemCascadeMap, List<Integer> msProjectLinkTypeIDs, List<TWorkItemBean> linkedDescendantChildren) throws ItemLoaderException {

		List<TWorkItemLinkBean> linksList =  workItemLinkDao.loadByWorkItemPred(workItemBeanParam.getObjectID());
		List<Integer> workItemsIDSList = new ArrayList<Integer>();
		for (TWorkItemLinkBean workItemLinkBean : linksList) {
			if(msProjectLinkTypeIDs.contains(workItemLinkBean.getLinkType())) {
				workItemsIDSList.add(workItemLinkBean.getLinkSucc());
			}
		}
		int[] workItemIDs = org.apache.commons.lang.ArrayUtils.toPrimitive(workItemsIDSList.toArray(new Integer[workItemsIDSList.size()]));
		List<TWorkItemBean> succList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIDs, personID, false, false, false);
		for (int i = 0; i < succList.size(); i++) {
			TWorkItemBean newWorkItemBean = succList.get(i);
			if(getStartDate(newWorkItemBean) != null && getEndDate(newWorkItemBean) != null) {
				if(newWorkItemBean.isMilestone()) {
					setEndDate(newWorkItemBean, getStartDate(newWorkItemBean));
				}

				linkedDescendantChildren.add(newWorkItemBean);
				if(itemCascadeMap.get(newWorkItemBean.getObjectID()) != null) {
					if(itemCascadeMap.get(newWorkItemBean.getObjectID()).isNeedsCascade()) {
						int newWorkItemWorkDaysNumber = getNumberOfWorkDaysBetweenDates(getStartDate(newWorkItemBean), getEndDate(newWorkItemBean));
						WorkItemWithNewCascadedDates tmp = new WorkItemWithNewCascadedDates();
						if (numberOfMovedDays < 0) {
							tmp.setStarDate(stepBack(getStartDate(newWorkItemBean), Math.abs(numberOfWorkedDaysFromMovedDays), true));
							if(Math.abs(newWorkItemWorkDaysNumber) == 1) {
								tmp.setEndDate(tmp.getStarDate());
							}else {
								tmp.setEndDate(stepBack(getEndDate(newWorkItemBean), Math.abs(numberOfWorkedDaysFromMovedDays), true));
							}
							tmp.setWorkItemBean(newWorkItemBean);
							tmp.setNeedsCascade(false);
							tmp.setSave(true);
							itemCascadeMap.put(newWorkItemBean.getObjectID(), tmp);
						}else {
							tmp.setStarDate(stepForward(getStartDate(newWorkItemBean), Math.abs(numberOfWorkedDaysFromMovedDays), true));
							if(newWorkItemWorkDaysNumber == 1) {
								tmp.setEndDate(tmp.getStarDate());
							}else {
								tmp.setEndDate(stepForward(tmp.getStarDate(), newWorkItemWorkDaysNumber - 1, true));
							}
							tmp.setWorkItemBean(newWorkItemBean);
							tmp.setNeedsCascade(false);
							tmp.setSave(true);
							itemCascadeMap.put(newWorkItemBean.getObjectID(), tmp);
						}
						parseLinks(newWorkItemBean, numberOfWorkedDaysFromMovedDays, numberOfMovedDays, personID, itemCascadeMap, msProjectLinkTypeIDs, linkedDescendantChildren);
					}
				}else {
					int newWorkItemWorkDaysNumber = getNumberOfWorkDaysBetweenDates(getStartDate(newWorkItemBean), getEndDate(newWorkItemBean));
					WorkItemWithNewCascadedDates tmp = new WorkItemWithNewCascadedDates();
					if (numberOfMovedDays < 0) {
						tmp.setStarDate(stepBack(getStartDate(newWorkItemBean), Math.abs(numberOfWorkedDaysFromMovedDays), true));
						if(newWorkItemWorkDaysNumber == 1) {
							tmp.setEndDate(tmp.getStarDate());
						}else {
							tmp.setEndDate(stepForward(tmp.getStarDate(), newWorkItemWorkDaysNumber - 1, true));
						}
						tmp.setWorkItemBean(newWorkItemBean);
						tmp.setNeedsCascade(false);
						tmp.setSave(true);
						itemCascadeMap.put(newWorkItemBean.getObjectID(), tmp);
					}else {
						tmp.setStarDate(stepForward(getStartDate(newWorkItemBean), Math.abs(numberOfWorkedDaysFromMovedDays), true));
						if(newWorkItemWorkDaysNumber == 1) {
							tmp.setEndDate(tmp.getStarDate());
						}else {
							tmp.setEndDate(stepForward(tmp.getStarDate(), newWorkItemWorkDaysNumber - 1, true));
						}
						tmp.setWorkItemBean(newWorkItemBean);
						tmp.setNeedsCascade(false);
						tmp.setSave(true);
						itemCascadeMap.put(newWorkItemBean.getObjectID(), tmp);
					}
					parseLinks(newWorkItemBean, numberOfWorkedDaysFromMovedDays, numberOfMovedDays, personID, itemCascadeMap, msProjectLinkTypeIDs, linkedDescendantChildren);
				}
			}
		}
	}

	/**
	 * This method check moved/resied work item direction (left or right), and based on direction
	 * calls helper methods for cascading the successor work items.
	 * @param workItemBean
	 * @param numberOfWorkedDaysFromMovedDays
	 * @param numberOfMovedDays
	 * @return
	 * @throws ItemLoaderException
	 */
	private static Map<String, Date> cascadeWorkItemChanges(TWorkItemBean workItemBean, int numberOfWorkedDaysFromMovedDays, int numberOfMovedDays,
			Integer personID, Locale locale, Map<Integer, WorkItemWithNewCascadedDates> itemCascadeMap,
				List<Integer> msProjectLinkTypeIDs, Integer workItemID, Date startDate, Date endDate,
					boolean storeCurrentWorkitem) throws ItemLoaderException {
		ArrayList<TWorkItemBean> linkedDescendantChildren = new ArrayList<TWorkItemBean>();
		List<TWorkItemLinkBean> links = workItemLinkDao.loadByWorkItemPred(workItemID);
		int[] workItemIDs;
		List<Integer> workItemIDsList = new ArrayList<Integer>();
		for (TWorkItemLinkBean workItemLinkBean : links) {
			if(msProjectLinkTypeIDs.contains(workItemLinkBean.getLinkType())) {
				workItemIDsList.add(workItemLinkBean.getLinkSucc());
			}
		}
		workItemIDs = org.apache.commons.lang.ArrayUtils.toPrimitive(workItemIDsList.toArray(new Integer[workItemIDsList.size()]));
		List<TWorkItemBean> succList = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIDs, personID, false, false, false);
		if(numberOfMovedDays < 0){
			for(int i = 0; i < succList.size(); i++) {
				TWorkItemBean childWorkItem = succList.get(i);
				WorkItemWithNewCascadedDates tmp = new WorkItemWithNewCascadedDates();
				tmp.setWorkItemBean(childWorkItem);
				tmp.setEndDate(getEndDate(childWorkItem));
				tmp.setStarDate(getStartDate(childWorkItem));
				tmp.setNeedsCascade(true);
				itemCascadeMap.put(childWorkItem.getObjectID(), tmp);
			}
			parseLinks(workItemBean, numberOfWorkedDaysFromMovedDays, numberOfMovedDays, personID, itemCascadeMap, msProjectLinkTypeIDs, linkedDescendantChildren);
			leftMovePredDependencyViolation(workItemBean, personID, itemCascadeMap, msProjectLinkTypeIDs, startDate, endDate);
		}else {
			if (succList.size() > 0) {
				for(int i = 0; i < succList.size(); i++) {
					TWorkItemBean childWorkItem = succList.get(i);
					if(checkWhichChildWorkItemNeedCascadingRight(workItemBean, childWorkItem, links.get(i), startDate, endDate)) {
						WorkItemWithNewCascadedDates tmp = new WorkItemWithNewCascadedDates();
						tmp.setWorkItemBean(childWorkItem);
						tmp.setEndDate(getEndDate(childWorkItem));
						tmp.setStarDate(getStartDate(childWorkItem));
						tmp.setNeedsCascade(true);
						itemCascadeMap.put(childWorkItem.getObjectID(), tmp);
					}else {
						WorkItemWithNewCascadedDates tmp = new WorkItemWithNewCascadedDates();
						tmp.setWorkItemBean(childWorkItem);
						tmp.setEndDate(getEndDate(childWorkItem));
						tmp.setStarDate(getStartDate(childWorkItem));
						tmp.setNeedsCascade(false);
						itemCascadeMap.put(childWorkItem.getObjectID(), tmp);
					}
				}
			}
			parseLinks(workItemBean, numberOfWorkedDaysFromMovedDays, numberOfMovedDays, personID, itemCascadeMap, msProjectLinkTypeIDs, linkedDescendantChildren);
		}
		for(TWorkItemBean bean : linkedDescendantChildren) {
			WorkItemWithNewCascadedDates tmp = itemCascadeMap.get(bean.getObjectID());
			if(tmp.isSave()) {
				checkChildrens(bean.getObjectID(), tmp.getWorkItemBean(), personID, getStartDate(tmp.getWorkItemBean()), getEndDate(tmp.getWorkItemBean()), tmp.getStarDate(), tmp.getEndDate(), locale, msProjectLinkTypeIDs, itemCascadeMap);
			}
		}
		storeAllChangedBean(locale, itemCascadeMap, personID, msProjectLinkTypeIDs);
		return null;
	}

	/**
	 * This method remove links. THe link id's are in Set.
	 * @param conflictingLinkIDs
	 */
	public static void removeViolatedParentDependencies(Set<Integer> conflictingLinkIDs) {
		if (conflictingLinkIDs!=null) {
			for (Integer conflictingLinkID : conflictingLinkIDs) {
				ItemLinkBL.deleteLink(conflictingLinkID);
			}
		}
	}

	/**
	 * This method returns all superior work items for the given parameter.
	 * @param parentID
	 * @param workItemBean
	 * @return
	 */
	public static Set<TWorkItemBean> checkParent(Integer parentID, TWorkItemBean workItemBean) {
		Set<TWorkItemBean> parents = new HashSet<TWorkItemBean>();
		while (parentID != null) {
			TWorkItemBean parentWorkItem = null;
			try {
				parentWorkItem = ItemBL.loadWorkItem(parentID);
				if(parentWorkItem != null) {
					parents.add(parentWorkItem);
				}
			} catch (ItemLoaderException e) {
				LOGGER.error("Error: " + e.getMessage());
			}
			parentID = null;
			parentID = parentWorkItem.getSuperiorworkitem();
		}
		return parents;
	}

	/**
	 * This method returns a work item all descendants.
	 * @param childrens
	 * @param allDescendant
	 * @param workItemID
	 */
	public static void getAllChildrens(List<TWorkItemBean>childrens, List<TWorkItemBean> allDescendant, Integer workItemID) {
		for(TWorkItemBean bean : childrens) {
			allDescendant.add(bean);
			List<TWorkItemBean> newChildrens = ItemBL.getChildren(bean.getObjectID());
			if(newChildrens.size() > 0) {
				getAllChildrens(newChildrens, allDescendant, workItemID);
			}
		}
	}


	/**
	 * This method creates a data set with work items and descendant's
	 * WorkItemID=>WorkItemBean
	 * 			 =>Set of children work items
	 * @param movedWorkItemAllDescendants
	 * @param changedWorkItem
	 * @return
	 */
	private static Map<Integer, Map<TWorkItemBean, Set<TWorkItemBean>>> createWorkItemIDToWorkItemToChildIDMap(List<TWorkItemBean> movedWorkItemAllDescendants, TWorkItemBean changedWorkItem) {
		Map<Integer, Map<TWorkItemBean, Set<TWorkItemBean>>> workItemIDToWorkItemToChildIDMap = new LinkedHashMap<Integer, Map<TWorkItemBean,Set<TWorkItemBean>>>();
		Map<TWorkItemBean,Set<TWorkItemBean>> changedWorkItemToChildIDMap = new LinkedHashMap<TWorkItemBean, Set<TWorkItemBean>>();
		Set<TWorkItemBean> changedWorkItemChildsBeanSet = new HashSet<TWorkItemBean>();
		changedWorkItemToChildIDMap.put(changedWorkItem, changedWorkItemChildsBeanSet);
		workItemIDToWorkItemToChildIDMap.put(changedWorkItem.getObjectID(), changedWorkItemToChildIDMap);
		for(TWorkItemBean bean : movedWorkItemAllDescendants) {
			if(workItemIDToWorkItemToChildIDMap.get(bean.getSuperiorworkitem()) != null) {
				TWorkItemBean key = workItemIDToWorkItemToChildIDMap.get(bean.getSuperiorworkitem()).keySet().iterator().next();
				workItemIDToWorkItemToChildIDMap.get(bean.getSuperiorworkitem()).get(key).add(bean);
			}else {
				Map<TWorkItemBean,Set<TWorkItemBean>> newParentToBeanMap = new LinkedHashMap<TWorkItemBean, Set<TWorkItemBean>>();
				Set<TWorkItemBean> newParentWorkItemChildsBeanSet= new HashSet<TWorkItemBean>();
				newParentWorkItemChildsBeanSet.add(bean);
				TWorkItemBean parent = null;
				for(TWorkItemBean parentWorkItem : movedWorkItemAllDescendants) {
					if(parentWorkItem.getObjectID().intValue() == bean.getSuperiorworkitem().intValue()) {
						parent = parentWorkItem;
						break;
					}
				}
				newParentToBeanMap.put(parent, newParentWorkItemChildsBeanSet);
				workItemIDToWorkItemToChildIDMap.put(parent.getObjectID(), newParentToBeanMap);
			}
		}
		return workItemIDToWorkItemToChildIDMap;
	}

	/**
	 * This method checks if moving a parent work item and descendant's violates MS. Proj. link dependency.
	 * In case of violation returns a Set with violated Dependencies, otherwise saves moved work items and
	 * cascades all descendants. In case of one descendant work item is linked the successor work item
	 * will be cascaded.
	 *
	 * @param workItemID
	 * @param movedWorkItem
	 * @param personID
	 * @param oldStartDate
	 * @param oldEndDate
	 * @param startDate
	 * @param endDate
	 * @param locale
	 * @param msProjectLinkTypeIDs
	 * @param itemCascadeMap
	 * @return
	 */
	private static Set<Integer> checkChildrens(Integer workItemID, TWorkItemBean movedWorkItem, Integer personID,
			Date oldStartDate, Date oldEndDate, Date startDate, Date endDate, Locale locale, List<Integer> msProjectLinkTypeIDs, Map<Integer, WorkItemWithNewCascadedDates> itemCascadeMap) {
		Set<Integer> violatedDependencies = new HashSet<Integer>();
		List<TWorkItemBean>childrens = ItemBL.getChildren(workItemID);
		if(childrens.isEmpty() ) {
			return violatedDependencies;
		}
		int numberOfMovedDays = 0;
		int numberOfWorkedDaysFromMovedDays = 0;
		if(oldStartDate.compareTo(startDate ) != 0 && oldEndDate.compareTo(endDate) != 0) {
			numberOfMovedDays = getNumberOfDaysBetweenDates(oldStartDate, startDate, false);
			if(numberOfMovedDays < 0) {
				numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(endDate, oldEndDate);
			}else {
				numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(oldStartDate, startDate);
			}
		}
		List<WorkItemWithNewCascadedDates> touchedDescendantWorkItems = new ArrayList<WorkItemWithNewCascadedDates>();
		List<TWorkItemBean> allDescendant = new ArrayList<TWorkItemBean>();
		getAllChildrens(childrens, allDescendant, workItemID);
		Map<Integer, Map<TWorkItemBean, Set<TWorkItemBean>>> workItemIDToWorkItemToChildIDMap = createWorkItemIDToWorkItemToChildIDMap(allDescendant, movedWorkItem);
		for (Map.Entry<Integer, Map<TWorkItemBean, Set<TWorkItemBean>>> entry : workItemIDToWorkItemToChildIDMap.entrySet()) {
			Map<TWorkItemBean, Set<TWorkItemBean>> parentToChild = entry.getValue();
			TWorkItemBean parent = parentToChild.keySet().iterator().next();
			Set<TWorkItemBean> childs = parentToChild.get(parent);
			for(TWorkItemBean childWorkItem : childs) {
				Set<Integer> violatedPredDependencies = checkSuccMoveValidity(getStartDate(childWorkItem), getEndDate(childWorkItem), childWorkItem.getObjectID(), personID, null);
				if(violatedPredDependencies.isEmpty()) {
					if(getStartDate(childWorkItem) != null && getEndDate(childWorkItem) != null) {
						Date[]  newStartEndDate = cascadeWorkItemBySuperior(numberOfMovedDays, numberOfWorkedDaysFromMovedDays, childWorkItem);
						Set<Integer> problems = checkSuccMoveValidity(newStartEndDate[0], newStartEndDate[1], childWorkItem.getObjectID(), personID, null);
						if(problems.size() > 0 ){
							for(Integer id : problems) {
								violatedDependencies.add(id);
							}
						}
						WorkItemWithNewCascadedDates tmp = new WorkItemWithNewCascadedDates();
						tmp.setWorkItemBean(childWorkItem);
						tmp.setStarDate(newStartEndDate[0]);
						tmp.setEndDate(newStartEndDate[1]);
						touchedDescendantWorkItems.add(tmp);
					}
				}
			}
		}
		if(violatedDependencies.isEmpty()) {
			storeAllTouchedDescendantWorkItem(touchedDescendantWorkItems, personID, locale, msProjectLinkTypeIDs, null);
			return violatedDependencies;
		}else {
			return violatedDependencies;
		}
	}

	/**
	 * This method check if a child work item is moved and the actualized parent violates MS. Proj.
	 * dependency.
	 * @param workItemBean
	 * @param startDate
	 * @param endDate
	 * @param personID
	 * @return
	 */
	public static Set<Integer> validateAncestorBottomUpLinks(TWorkItemBean workItemBean, Date startDate, Date endDate, Integer personID) {
		Integer parentID = workItemBean.getSuperiorworkitem();
		Set<Integer>problems = new HashSet<Integer>();

		while(parentID != null) {
			try {
				TWorkItemBean parentWorkItem = ItemBL.loadWorkItem(parentID);
				checkSuccMoveValidity(startDate, endDate, workItemBean.getObjectID(), personID, null);
				Set<Integer> tmp = checkSuccMoveValidity(startDate, endDate, parentWorkItem.getObjectID(), personID, null);
				problems.addAll(tmp);
				parentID = parentWorkItem.getSuperiorworkitem();
			} catch (ItemLoaderException e) {
			}
		}
		return problems;
	}


	/**
	 * This method cascades child work items.
	 * @param numberOfMovedDays
	 * @param numberOfWorkedDaysFromMovedDays
	 * @param child
	 * @return
	 */
	public static Date[] cascadeWorkItemBySuperior(int numberOfMovedDays, int numberOfWorkedDaysFromMovedDays, TWorkItemBean child) {
		int newWorkItemWorkDaysNumber = getNumberOfWorkDaysBetweenDates(getStartDate(child), getEndDate(child));
		Date[] startEndDate = new Date[2];
		if (numberOfMovedDays < 0) {
			startEndDate[0] = stepBack(getStartDate(child), Math.abs(numberOfWorkedDaysFromMovedDays), true);
			if(Math.abs(newWorkItemWorkDaysNumber) == 1) {
				startEndDate[1] = startEndDate[0];
			}else {
				startEndDate[1] = stepForward(startEndDate[0], newWorkItemWorkDaysNumber - 1, true);
			}
		}else {
			startEndDate[0] = stepForward(getStartDate(child), Math.abs(numberOfWorkedDaysFromMovedDays), true);
			if(newWorkItemWorkDaysNumber == 1) {
				startEndDate[1] = startEndDate[0];
			}else {
				startEndDate[1] = stepForward(startEndDate[0], newWorkItemWorkDaysNumber - 1, true);
			}
		}
		return startEndDate;
	}

	/**
	 * This method saves work items new start/end date in case of drag and drop, resizing and cascades dependent work items in case.
	 * New start and end date is in startDate and endDate attribute.
	 * Bryntum increase end date with one day: for ex. work item start date: 2013-11-25 and the duration is 3 days => the end date is: 2013-11-29
	 * Because Bryntum increase end date with one day when saving new start/end date it must be reduced with one day.
	 * (In proper method when returning the JSON for bryntum the endDate must be increased with one day!)
	 * The bean contains reduced interval, the parameters (startDate, endDate) contains increased, Bryntum compatible interval.
	 * This method checks moved work item and his preds violation,  in case returns proper error message.
	 * @param workItemBean
	 * @param startDateParam
	 * @param endDateParam
	 * @param sessionParam
	 * @param localeParam
	 * @param removeViolatedLinksAndCascade
	 * @param wrongDependencies
	 * @param msProjectIdMapParam
	 * @param personBeanParam
	 * @param servletResponse
	 * @return
	 */
	public static void planItem(TWorkItemBean workItemBean, Date oldStartDate, Date oldEndDate,  Date startDate, Date endDate, Locale locale,
			List<Integer> msProjectLinkTypeIDs, Integer personID, boolean storeCurrentWorkitem) {
		Integer workItemID = workItemBean.getObjectID();
		Map<Integer, WorkItemWithNewCascadedDates>  itemCascadeMap = new HashMap<Integer, WorkItemWithNewCascadedDates>();
		int numberOfMovedDays = 0;
		int numberOfWorkedDaysFromMovedDays = 0;
		if(workItemBean.isMilestone()) {
			setEndDate(workItemBean, getStartDate(workItemBean));
			endDate = startDate;
		}
		boolean onlyStartDateChanged = false;
		try {
			if(startDate != null && endDate != null && oldStartDate != null && oldEndDate != null) {

				if(oldStartDate.compareTo(startDate) != 0 && oldEndDate.compareTo(endDate) != 0) {
					numberOfMovedDays = getNumberOfDaysBetweenDates(oldStartDate, startDate, false);
					if(numberOfMovedDays < 0) {
						numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(endDate, oldEndDate);
					}else {
						numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(oldStartDate, startDate);
					}
				} else {
					if(oldStartDate.compareTo(startDate) != 0) {
						numberOfMovedDays = getNumberOfDaysBetweenDates(oldStartDate, startDate, false);
						onlyStartDateChanged = true;
						if(numberOfMovedDays < 0) {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(startDate, oldStartDate);
						}else {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(oldStartDate, startDate);
						}
					}
					if(oldEndDate.compareTo(endDate) != 0) {
						numberOfMovedDays = getNumberOfDaysBetweenDates(oldEndDate, endDate, false);
						if(numberOfMovedDays < 0) {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(endDate, oldEndDate);
						}else {
							numberOfWorkedDaysFromMovedDays = getNumberOfWorkDaysFromMovedIntervall(oldEndDate, endDate);
						}
					}
				}
				if (numberOfWorkedDaysFromMovedDays < 0) {
					numberOfWorkedDaysFromMovedDays = Math.abs(numberOfWorkedDaysFromMovedDays);
				}
				if (!onlyStartDateChanged) {
					cascadeWorkItemChanges(workItemBean,
							numberOfWorkedDaysFromMovedDays,
							numberOfMovedDays, personID, locale,
							itemCascadeMap, msProjectLinkTypeIDs,
							workItemID, startDate, endDate,
							storeCurrentWorkitem);
				}
			}
		} catch(Exception ex) {
			LOGGER.error("Error: " + ex.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(ExceptionUtils.getStackTrace(ex));
			}
		}
	}

	



	/**
	 * In Gantt we are handling link lags: day, week, month,
	 * This method returns link lag in days.
	 * Foe ex: -1) 1 mo link lag is 20 work day.
	 * 		   -2) 1 w link lag is 5 work days
	 * @param actualLinkBean
	 * @param workItemID
	 * @return
	 */
	public static int getLinkLagInDays(TWorkItemLinkBean actualLinkBean, Integer workItemID) {
		Double hoursPerWorkday = MsProjectLinkType.getHoursPerWorkingDayForWorkItem(workItemID);
		Double convertedLinkLag = LinkLagBL.getUILinkLagFromMinutes(actualLinkBean.getLinkLag(), actualLinkBean.getLinkLagFormat(), hoursPerWorkday);
		int linkLagInDays = 0;
		Integer actualLinkLagFormat = actualLinkBean.getLinkLagFormat();
		if (actualLinkLagFormat==null) {
			actualLinkLagFormat = MsProjectExchangeDataStoreBean.LAG_FORMAT.d;
		}
		switch(actualLinkLagFormat) {
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.d:
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.ed:
				linkLagInDays = convertedLinkLag.intValue();
				break;
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.mo:
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.emo:
				linkLagInDays = convertedLinkLag.intValue() * 20;
				break;
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.w:
			case  MsProjectExchangeDataStoreBean.LAG_FORMAT.ew:
				linkLagInDays = convertedLinkLag.intValue() * 5;
				break;
		}
		return linkLagInDays;
	}

	/**
	 * This method saves actual link bean after drag and drop/resize.
	 * This method calls perform save which will call cascade mechanism.
	 * @param workItemBean
	 * @param personID
	 * @param locale
	 * @param startDate
	 * @param endDate
	 */
	private static void saveActualBean(TWorkItemBean workItemBean, Integer personID, Locale locale, Date startDate, Date endDate) {
		if (startDate!=null || endDate!=null) {
			Set<Integer> presentField = new HashSet<Integer>();

			setPresentedFields(presentField);
			WorkItemContext workItemContext = FieldsManagerRT.getExchangeContextForExisting(workItemBean, personID, locale, presentField);
			workItemContext.setExchangeImport(false);
			setStartDate(workItemContext.getWorkItemBean(), startDate);
			if (workItemContext.getWorkItemBean().isMilestone()) {
				setEndDate(workItemContext.getWorkItemBean(), null);
			} else {
				setEndDate(workItemContext.getWorkItemBean(), endDate);
			}
			List<ErrorData> errorsList = new LinkedList<ErrorData>();
			boolean result = FieldsManagerRT.performSave(workItemContext, errorsList, false, false/*, true*/);
			LOGGER.debug("Work Item: " + workItemBean.getObjectID() + " drag drop/resize saved: Errors: " + errorsList + " Result: " + result);
		}
	}

	/**
	 * Based on link format: FF, FS, SF, SS this method check number of work days between two linked tasks.
	 * For ex.: Task1 successor is Task2. Task1 end date is 2014-01-20 and Task2 start date is 2014-01-23.
	 * Task1 and Task2 is linked with FS link type. In this case this method returns 2 because between this two tasks
	 * is 2 work days. It is important when setting link lag to 1 days, in this case do not cascade Task2.
	 * @param link
	 * @param pred
	 * @param succ
	 * @return
	 */
	private static Integer getNumberOfDaysBetweenTaskBasedOnLinkType(TWorkItemLinkBean link, TWorkItemBean pred, TWorkItemBean succ) {
		int dependencyType = link.getIntegerValue1();
		int numberOfWorkDays = 0;

		Calendar cal;
		Date predDate;
		Date succDate;
		//the start dates are pushed back one day
		switch(dependencyType) {
			case PREDECESSOR_ELEMENT_TYPE.FS:
				succDate = getStartDate(succ);
				if (succDate==null) {
					return null;
				}
				cal = Calendar.getInstance();
				cal.setTime(succDate);
				cal.add(Calendar.DAY_OF_YEAR, -1);
				succDate = cal.getTime();
				predDate = getEndDate(pred);
				if (predDate==null) {
					return null;
				}
				numberOfWorkDays = getNumberOfWorkDaysBetweenDates(predDate, succDate);
				break;
			case PREDECESSOR_ELEMENT_TYPE.FF:
				predDate = getEndDate(pred);
				if (predDate==null) {
					return null;
				}
				succDate = getEndDate(succ);
				if (succDate==null) {
					return null;
				}
				numberOfWorkDays = getNumberOfWorkDaysBetweenDates(predDate, succDate);
				break;
			case PREDECESSOR_ELEMENT_TYPE.SF:
				predDate = getStartDate(pred);
				if (predDate==null) {
					return null;
				}
				cal = Calendar.getInstance();
				cal.setTime(predDate);
				cal.add(Calendar.DAY_OF_YEAR, -1);
				predDate = cal.getTime();
				succDate = getEndDate(succ);
				if (succDate==null) {
					return null;
				}
				numberOfWorkDays = getNumberOfWorkDaysBetweenDates(predDate, succDate);
				break;
			case PREDECESSOR_ELEMENT_TYPE.SS:
				predDate = getStartDate(pred);
				if (predDate==null) {
					return null;
				}
				cal = Calendar.getInstance();
				cal.setTime(predDate);
				cal.add(Calendar.DAY_OF_YEAR, -1);
				predDate = cal.getTime();

				succDate = getStartDate(succ);
				if (succDate==null) {
					return null;
				}
				cal.setTime(succDate);
				cal.add(Calendar.DAY_OF_YEAR, -1);
				succDate = cal.getTime();

				numberOfWorkDays = getNumberOfWorkDaysBetweenDates(predDate, succDate);
				break;
		}
		//start, end date must be removed.
		numberOfWorkDays -= 1;
		return numberOfWorkDays;
	}

	/**
	 * This method handle link lag changing, and creating.
	 * For example: if a link lag increasing from 0 to 2 days the item and successors will be cascaded (if needed).
	 * @param lagInDaysOrDiffOldLagNewLag
	 * @param linkBean
	 * @param pred
	 * @param succ
	 * @param personID
	 * @param locale
	 * @param isNew
	 * @return
	 */
	static Set<Integer> handleLagAndCascade(int lagInDaysOrDiffOldLagNewLag, TWorkItemLinkBean linkBean, TWorkItemBean pred,
			TWorkItemBean succ, Integer personID, Locale locale, boolean isNew) {
		Set<Integer> violatedSuccessorDependencies = new HashSet<Integer>();
		Set<Integer> violatedDescendantDependencies = new HashSet<Integer>();
		int allLinkLag = lagInDaysOrDiffOldLagNewLag;
		if(!isNew) {
			allLinkLag = getLinkLagInDays(linkBean, pred.getObjectID());
		}
		Date newStartDate = new Date();
		Date newEndDate = new Date();
		if (lagInDaysOrDiffOldLagNewLag > 0 || (lagInDaysOrDiffOldLagNewLag==0 && isNew)) {
			Integer numberOfWorkdaysBetweenTasksBaseLinkType = getNumberOfDaysBetweenTaskBasedOnLinkType(linkBean, pred, succ);
			if (numberOfWorkdaysBetweenTasksBaseLinkType==null) {
				//start or end date not specified, allow to add link without restriction
				return violatedSuccessorDependencies;
			}
			int difference = allLinkLag - numberOfWorkdaysBetweenTasksBaseLinkType;
			if (difference>0) {
				//increase in lag should not be a problem
				newStartDate = MsProjectLinkTypeBL.stepForward(getStartDate(succ), difference, true);
				newEndDate = MsProjectLinkTypeBL.stepForward(getEndDate(succ), difference, true);
				MsProjectLinkTypeBL.saveActualBean(succ, personID, locale, newStartDate, newEndDate);
			}
		} else if (lagInDaysOrDiffOldLagNewLag < 0) {
			//decrease should be verified in linked successors and child items
			newStartDate = MsProjectLinkTypeBL.stepBack(getStartDate(succ), Math.abs(lagInDaysOrDiffOldLagNewLag), true);
			newEndDate = MsProjectLinkTypeBL.stepBack(getEndDate(succ), Math.abs(lagInDaysOrDiffOldLagNewLag), true);
			violatedSuccessorDependencies = MsProjectLinkTypeBL.checkSuccMoveValidity(getStartDate(succ), getEndDate(succ), succ.getObjectID(), personID, linkBean);
			Map<Integer, WorkItemWithNewCascadedDates> itemCascadeMap = new HashMap<Integer, WorkItemWithNewCascadedDates>();
			MsProjectLinkType msProjectLinkType = MsProjectLinkType.getInstance();
			List<Integer> msProjectLinkTypeIDs = LinkTypeBL.getLinkTypesByPluginClass(msProjectLinkType);
			violatedDescendantDependencies = MsProjectLinkTypeBL.checkChildrens(succ.getObjectID(), succ, personID, getStartDate(succ), getEndDate(succ), newStartDate, newEndDate, locale, msProjectLinkTypeIDs, itemCascadeMap);
			Set<Integer> allProblems = new HashSet<Integer>();
			allProblems.addAll(violatedSuccessorDependencies);
			allProblems.addAll(violatedDescendantDependencies);
			if(allProblems.size() > 0 ) {
				return allProblems;
			}
			MsProjectLinkTypeBL.saveActualBean(succ, personID, locale, newStartDate, newEndDate);
		}
		return violatedSuccessorDependencies;
	}

	public static Date getStartDate(TWorkItemBean bean) {
		boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
		if (bean!=null) {
			if (showBaseline) {
				return bean.getTopDownStartDate();
			}else {
				return bean.getStartDate();
			}
		} else {
			return null;
		}
	}

	public static Date getEndDate(TWorkItemBean bean) {
		boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
		if (bean!=null) {
			if(showBaseline) {
				return bean.getTopDownEndDate();
			}else {
				return bean.getEndDate();
			}
		} else {
			return null;
		}
	}

	public static void setStartDate(TWorkItemBean bean, Date newStartDate) {
		if (bean!=null) {
			boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
			if(showBaseline) {
				bean.setTopDownStartDate(newStartDate);
			}else {
				bean.setStartDate(newStartDate);
			}
		}
	}

	public static void setEndDate(TWorkItemBean bean, Date newEndDate) {
		if (bean!=null) {
			boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
			if(showBaseline) {
				bean.setTopDownEndDate(newEndDate);
			}else {
				bean.setEndDate(newEndDate);
			}
		}
	}

	public static void setPresentedFields(Set<Integer> presentField) {
		boolean showBaseline = ApplicationBean.getInstance().getSiteBean().getShowBaseline();
		if(showBaseline) {
			presentField.add(SystemFields.TOP_DOWN_START_DATE);
			presentField.add(SystemFields.TOP_DOWN_END_DATE);
		}else {
			presentField.add(SystemFields.INTEGER_STARTDATE);
			presentField.add(SystemFields.INTEGER_ENDDATE);
		}
	}
}


