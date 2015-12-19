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

package com.aurel.track.report.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.ReportBeanLoader;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.report.dashboard.BurnDownChart.CONFIGURATION_PARAMETERS;
import com.aurel.track.report.dashboard.BurnDownChart.EFFORT_TYPE;
import com.aurel.track.report.dashboard.BurnDownChart.PROPERTY_PARAMS;
import com.aurel.track.report.dashboard.BurnDownChart.REPORTING_INTERVAL;
import com.aurel.track.report.dashboard.BurnDownChart.TIME_INTERVAL;
import com.aurel.track.report.dashboard.ProjectFilterDashboardView.DATASOURCE_TYPE;
import com.aurel.track.report.dashboard.TimePeriodDashboardView.TIMEPERIOD_PARAMETERS;
import com.aurel.track.report.datasource.IPluggableDatasource.CONTEXT_ATTRIBUTE;
import com.aurel.track.report.datasource.earnedValue.EarnedValueBL;
import com.aurel.track.report.datasource.earnedValue.EarnedValueDatasource;
import com.aurel.track.report.datasource.earnedValue.EarnedValueTimeSlice;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.StringArrayParameterUtils;

public class AverageTimeToCloseItem extends TimePeriodDashboardView{
    private static final Logger LOGGER = LogManager.getLogger(AverageTimeToCloseItem.class);

    private List<ReportBean> reportBeanList;
    private List<ReportBeanWithHistory> reportBeanWithHistoryList;
    private Date dateTo;
    private Date dateFrom;
    private Map<Integer, Double> hoursPerWorkingDayMap;

    static final int PLANNED_VALUE = 1;
    static final int ACTUAL_EFFORT = 2;
    static final int EARNED_VALUE = 3;
    static String ISO_FORMATTED_DATE = "yyyy-MM-dd";

    //Configuration page constants
    public static interface CONFIGURATION_PARAMETERS {
        static String STATUSES = "statuses";
        static String SELECTED_STATUS = "selectedStatus";
        static String TIME_INTERVALS = "timeIntervals";
        static String REPORTING_INTERVAL = "reportingInterval";
        static String SELECTED_REPORTING_INTERVAL = "selectedReportingInterval";
        static String TIME_FORMAT = "timeFormat";
        static String SELECTED_TIME_FORMAT = "selectedTimeFormat";
        static String Y_AXE = "yAxe";
        static String RESPONSE_TIME_LIMIT= "responseTimeLimitValue";
    }

    protected int DEFAULT_HEIGHT = 400;
    protected int MIN_HEIGHT = 250;
    protected int MAX_HEIGHT = 900;

    public static interface PERIOD_TYPE {
        static int FROM_TO = 1;
        static int DAYS_BEFORE = 2;
    }

    public static interface TIME_INTERVAL {
        static int DAY = 1;
        static int WEEK = 2;
        static int MONTH = 3;
    }

    // Reporting interval constants
    public static interface REPORTING_INTERVAL {
        static int DAILY= 1;
        static int WEEKLY = 2;
        static int MONTHLY = 3;
    }

 // Reporting interval constants
    public static interface TIME_FORMAT {
        static int WORKING_DAY = 0;
        static int WORKING_HOURS = 1;
    }

    // Property parameters
    public static interface PROPERTY_PARAMS {
        static String REPORTING_INTERVAL_DAILY = "averageTimeToCloseItem.tooltip.reportingInterval.daily";
        static String REPORTING_INTERVAL_WEEKLY = "averageTimeToCloseItem.tooltip.reportingInterval.weekly";
        static String REPORTING_INTERVAL_MONTHLY = "averageTimeToCloseItem.tooltip.reportingInterval.monthly";
        static String TIME_FORMAT_WORKING_DAY = "averageTimeToCloseItem.tooltip.time.format.working.day";
        static String TIME_FORMAT_WORKING_HOURS = "averageTimeToCloseItem.tooltip.time.format.working.hours";
    }

    protected boolean isUseConfig(){
        return true;
    }

    @Override
    protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
                Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
        TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
        Locale locale = user.getLocale();
        DashboardDescriptor dashboardDescriptor = getDescriptor();
        String bundleName = dashboardDescriptor.getBundleName();

        StringBuilder sb=new StringBuilder();

        String sessionPrefix= "dashboard"+"."+dashboardID;
        //the name where configParameters are stored
        String theParams =sessionPrefix+"Params";
        //to avoid caching if IE
        String userEnteredTitle = parameters.get("title");
        if (userEnteredTitle==null || "".equals(userEnteredTitle.trim())) {
            userEnteredTitle = "burnDownChart.label";
        }
        int height = parseInteger(parameters, CONFIGURATION_PARAMETERS.Y_AXE, DEFAULT_HEIGHT);
        Map<String,Object> configParameters=new HashMap<String, Object>();
        Iterator<String> it=parameters.keySet().iterator();
        while(it.hasNext()){
            String key=it.next();
            configParameters.put(key,parameters.get(key));
        }
        //used in preparing the locale specific chart
        configParameters.put("locale", locale);
        configParameters.put("personBean", user);
        configParameters.put("projectID", projectID);
        configParameters.put("releaseID", releaseID);
        session.put(theParams, configParameters);
        JSONUtility.appendIntegerValue(sb, "height", height);

        dateTo = null;
        dateFrom = null;

        if ( (configParameters.get("dateTo") != null && configParameters.get("dateTo") != null && !configParameters.get("dateTo").equals("null") &&
                !configParameters.get("dateFrom").equals("null")) || (configParameters.get("daysBefore") != null) ) {

            SimpleDateFormat dateFormat;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (configParameters.get("daysBefore") != null) {

                Calendar calendar = Calendar.getInstance();
                int daysBefore = BasePluginDashboardBL.parseIntegerValue(configParameters, TIMEPERIOD_PARAMETERS.DAYS_BEFORE, 0);
                dateTo = calendar.getTime();
                dateFrom = calendar.getTime();
                if(daysBefore!=0){
                    calendar.add(Calendar.DATE, -daysBefore);
                    dateFrom = calendar.getTime();
                }
            } else {
                try {
                    dateFrom = dateFormat.parse(configParameters.get("dateFrom").toString());
                    dateTo = dateFormat.parse(configParameters.get("dateTo").toString());
                }catch(Exception ex) {
                	LOGGER.error(ExceptionUtils.getStackTrace(ex),ex);
                }
            }
            JSONUtility.appendStringValue(sb, "dateTo", dateFormat.format(dateTo).toString());
            JSONUtility.appendStringValue(sb, "dateFrom", dateFormat.format(dateFrom).toString());
            String chartData = generateJSONResponse(configParameters, user, locale);
            boolean chartDataIsEmpty = false;
            if(chartData == null || "[]".equals(chartData)) {
                chartData = "[]";
                chartDataIsEmpty = true;
            }
            JSONUtility.appendJSONValue(sb, "chartData", chartData);
            JSONUtility.appendBooleanValue(sb, "empty", chartDataIsEmpty);
            String xAxesLabel = "";
            if (Integer.parseInt(configParameters.get("reportingInterval").toString()) == REPORTING_INTERVAL.WEEKLY) {
                xAxesLabel = LocalizeUtil.getLocalizedText("burnDownChart.xAxesLabelWeekly", locale, bundleName);
            }else {
                xAxesLabel = LocalizeUtil.getLocalizedText("burnDownChart.xAxesLabel", locale, bundleName);
            }

            int selectedTimeFormat = parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.TIME_FORMAT, 0);
            String yAxesLabel = "";
            if(selectedTimeFormat == TIME_FORMAT.WORKING_DAY) {
            	yAxesLabel = LocalizeUtil.getLocalizedText("averageTimeToCloseItem.yAxesLabel.working.day", locale, bundleName);
            }else {
            	yAxesLabel = LocalizeUtil.getLocalizedText("averageTimeToCloseItem.yAxesLabel.working.hours", locale, bundleName);
            }
            JSONUtility.appendStringValue(sb, "xAxesLabel", xAxesLabel);
            JSONUtility.appendStringValue(sb, "yAxesLabel", yAxesLabel);
            JSONUtility.appendStringValue(sb, "reportingInterval", configParameters.get("reportingInterval").toString());
            JSONUtility.appendStringValue(sb, "locale", locale.toString());
        }else {
            JSONUtility.appendBooleanValue(sb, "empty", true);
            LOGGER.debug("Burn-Down Chart not configured yet!");
        }
        return sb.toString();
    }

    @Override
    public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
        StringBuilder sb=new StringBuilder();
        Locale locale = user.getLocale();
        //DateTimeUtils dateTimeUtils = DateTimeUtils.getInstance();
        DashboardDescriptor dashboardDescriptor = getDescriptor();
        String bundleName = dashboardDescriptor.getBundleName();

        sb.append(getDatasourceConfig(parameters, entityId, entityType, locale));
        //DataSourceDashboardBL.appendJSONExtraDataConfig_DataSource(sb,dashboardDescriptor,parameters,user,entityId,entityType);
        sb.append(getTimePeriodConfig(parameters, locale));

        JSONUtility.appendILabelBeanList(sb,CONFIGURATION_PARAMETERS.STATUSES, StatusBL.loadAll(locale));
        JSONUtility.appendStringList(sb,CONFIGURATION_PARAMETERS.SELECTED_STATUS,
                StringArrayParameterUtils.splitSelection(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS)));

        //Reporting interval
        JSONUtility.appendIntegerStringBeanList(sb, CONFIGURATION_PARAMETERS.REPORTING_INTERVAL, getPossibleReportingIntervals(locale, bundleName));
        int reportingInterval = parseInteger(parameters, CONFIGURATION_PARAMETERS.REPORTING_INTERVAL, TIME_INTERVAL.MONTH);
        JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_REPORTING_INTERVAL, reportingInterval);

        //Time format
        int timeFormat = parseInteger(parameters, CONFIGURATION_PARAMETERS.TIME_FORMAT, TIME_FORMAT.WORKING_DAY);
        JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_TIME_FORMAT, timeFormat);

        JSONUtility.appendIntegerValue(sb, "timeFormatWorkingDayID", TIME_FORMAT.WORKING_DAY);
        JSONUtility.appendIntegerValue(sb, "timeFormatWorkingHoursID", TIME_FORMAT.WORKING_HOURS);

        JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.Y_AXE, parseInteger(parameters, CONFIGURATION_PARAMETERS.Y_AXE, DEFAULT_HEIGHT));

        int responseTimeLimitValue = parseIntegerValue(parameters, CONFIGURATION_PARAMETERS.RESPONSE_TIME_LIMIT, 1);
        JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.RESPONSE_TIME_LIMIT, responseTimeLimitValue);

        int prefWidth = 480;
        int prefHeight = 660;
        JSONUtility.appendIntegerValue(sb,"prefWidth",prefWidth);
        JSONUtility.appendIntegerValue(sb,"prefHeight",prefHeight);
        return sb.toString();
    }

     public String generateJSONResponse(Map configParameters, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
         int datasourceType = parseInteger(configParameters, "selectedDatasourceType", 1);
         Integer projectOrReleaseID = null;
         Integer filterID = null;
         if(configParameters.get("projectID") != null || configParameters.get("releaseID") != null) {
        	 if(configParameters.get("releaseID") != null) {
        		 projectOrReleaseID = Integer.valueOf(configParameters.get("releaseID").toString());
        	 }else {
        		 projectOrReleaseID = Integer.valueOf(configParameters.get("projectID").toString());
        		 projectOrReleaseID = projectOrReleaseID * (-1);
        	 }
        	 datasourceType = DATASOURCE_TYPE.PROJECT_RELEASE;
         }else {
	         if(datasourceType == 1) {
	             projectOrReleaseID = parseInteger(configParameters, "selectedProjectOrRelease", 1);
	         }else {
	             filterID = parseInteger(configParameters,  "selectedQueryID", 1);
	        }
        }

        Map<String, Object>contextMap = new HashMap<String, Object>();
        contextMap.put("fromIssueNavigator", false);
        contextMap.put("useProjectSpecificID", false);
        reportBeanList = getReportBeanList(datasourceType, projectOrReleaseID, filterID, contextMap, true, true, true, personBean, locale);
        reportBeanWithHistoryList = ReportBeanHistoryLoader.getReportBeanWithHistoryList(
              reportBeanList, locale,	false, false, true, new Integer[]{SystemFields.INTEGER_STATE}, true, true, true, true, false, personBean.getObjectID(), null,
                 dateFrom, dateTo, true, LONG_TEXT_TYPE.ISPLAIN);
            SortedMap<Date, EarnedValueTimeSlice> earnedValueTimeSliceMap = new TreeMap<Date, EarnedValueTimeSlice>();
            int reportingInterval = parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.REPORTING_INTERVAL, 1);
            int selectedTimeFormat = parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.TIME_FORMAT, 1);
            int responseTimeLimitValue = parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.RESPONSE_TIME_LIMIT, 1);
            if (!reportBeanWithHistoryList.isEmpty()) {
                LOGGER.debug("Number of reportBeanWithHistoryList after removing items with no budget or overlapping start/end dates " + reportBeanWithHistoryList.size());
                Set<Integer> projectIDs = getProjectIDs(reportBeanWithHistoryList);
                hoursPerWorkingDayMap = ProjectBL.getHoursPerWorkingDayMap(projectIDs);
                Integer[] selectedStatuses = getSelectedStatuses(configParameters);
                SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToAverageValue = createYearToIntervalToAvgTimeMap(reportingInterval, GeneralUtils.createSetFromIntegerArr(selectedStatuses), selectedTimeFormat);
                BurnDownChartBL.addZerosForEmptyIntervals(dateFrom, dateTo, reportingInterval, yearToIntervalToAverageValue, false);
                SortedMap<Date, Object> dateToAvgTime = transformPeriodsToDates(yearToIntervalToAverageValue, reportingInterval);
                String retValue = convertResultMapToJsonResponse(dateToAvgTime, reportingInterval, responseTimeLimitValue);
                return retValue;
            }
            return null;
     }

     private String convertResultMapToJsonResponse(SortedMap<Date, Object> dateToAvgTime, int reportingInterval, int responseTimeLimitValue) {
    	 StringBuilder sb = new StringBuilder();
    	 boolean isEmpty = true;
    	 if (dateToAvgTime != null) {
    		sb.append("[");
 			for (Map.Entry<Date, Object> entry : dateToAvgTime.entrySet()) {
 				if(isEmpty) {
 					isEmpty = false;
				}
 				Date date = entry.getKey();
				Double avgValue = (Double)entry.getValue();
				sb.append("{");
				JSONUtility.appendStringValue(sb, "date", getDateBasedOnReportInterval(date, reportingInterval));
				JSONUtility.appendDoubleValue(sb, "avgTime", avgValue);
				JSONUtility.appendIntegerValue(sb, "respTimeLimitValue", responseTimeLimitValue, true);

				sb.append("},");
 			}
 			if(sb != null && sb.length() > 0) {
 	            if(sb.toString().endsWith(",")) {
 	            	sb = new StringBuilder(sb.substring(0,  sb.length() - 1));
 	            }
 	        }
 			sb.append("]");
    	 }
    	 if(isEmpty) {
    		 sb = new StringBuilder();
    		 sb.append("[]");
    	 }
    	 return sb.toString();
     }

     private SortedMap<Integer, SortedMap<Integer, ArrayList<ReportBeanWithHistory>>> createYearToIntervalToReportBeanListMap(int timeInterval, Set<Integer> finalStates) {
    	 SortedMap<Integer, SortedMap<Integer, ArrayList<ReportBeanWithHistory>>> yearToIntervalToReportBeanList = new TreeMap<Integer, SortedMap<Integer, ArrayList<ReportBeanWithHistory>>>();
    	 if (reportBeanWithHistoryList != null) {
    		 Calendar calendar = Calendar.getInstance();
    		 Calendar calendarEndDate = Calendar.getInstance();
    		 int calendarInterval = getCalendarInterval(timeInterval);

    		 for (ReportBeanWithHistory reportBean : reportBeanWithHistoryList) {
    			 TWorkItemBean workItemBean = reportBean.getWorkItemBean();
    			 calendar.setTime(dateFrom);
    			 calendarEndDate.setTime(dateTo);
    			 int yearValue = calendar.get(Calendar.YEAR);
    			 int intervalValue = calendar.get(calendarInterval);
    			 boolean isFirst = true;
    			 while (calendar.before(calendarEndDate) || isFirst) {
    				 if (isFirst) {
    					 isFirst = false;
    				 } else {
    					 calendar.add(calendarInterval, 1);
    				 }
    				 yearValue = calendar.get(Calendar.YEAR);
    				 intervalValue = calendar.get(calendarInterval);
    				 SortedMap<Integer, ArrayList<ReportBeanWithHistory>> intervalToReportBeans = yearToIntervalToReportBeanList.get(Integer.valueOf(yearValue));
    				 if (intervalToReportBeans == null) {
    					 yearToIntervalToReportBeanList.put(new Integer(yearValue), new TreeMap<Integer, ArrayList<ReportBeanWithHistory>>());
    					 intervalToReportBeans = yearToIntervalToReportBeanList.get(Integer.valueOf(yearValue));
    				 }
    				 ArrayList<ReportBeanWithHistory> reportBeanList = intervalToReportBeans.get(Integer.valueOf(intervalValue));
    				 if(reportBeanList == null) {
    					 reportBeanList = new ArrayList<ReportBeanWithHistory>();
    					 intervalToReportBeans.put(Integer.valueOf(intervalValue), reportBeanList);
    				 }
   					 Integer stateID = getReportBeanStateID(reportBean);
   					 Date lastStateChangeDate = getReportBeanLastStateChange(reportBean);
   					 if (stateID == null || lastStateChangeDate == null) {
   						 continue;
   					 }
   					 if(finalStates.contains(stateID)) {
    					 if(timeInterval == TIME_INTERVAL.DAY) {
    						 if(DateTimeUtils.compareTwoDatesWithoutTimeValue(workItemBean.getCreated(), calendar.getTime()) == 0
    								 && DateTimeUtils.compareTwoDatesWithoutTimeValue(lastStateChangeDate, calendar.getTime()) == 0) {
    							 reportBeanList.add(reportBean);
    						 }
    					 }else {
    						 Calendar actualReportinIntervalEndCalendar = Calendar.getInstance();
    	   					 actualReportinIntervalEndCalendar.setTime(calendar.getTime());
    	   					 actualReportinIntervalEndCalendar.add(calendarInterval, 1);
    	   					 if(DateTimeUtils.greaterOrEqual(workItemBean.getCreated(), calendar.getTime()) &&
    								 DateTimeUtils.greater(lastStateChangeDate, calendar.getTime()) &&
	    								 DateTimeUtils.lessOrEqual(lastStateChangeDate, actualReportinIntervalEndCalendar.getTime())) {
    							 reportBeanList.add(reportBean);
    						 }
    					 }
   					 }
    			 }
    		 }
    	 }
    	 return yearToIntervalToReportBeanList;
     }


     private SortedMap<Integer, SortedMap<Integer, Double>> createYearToIntervalToAvgTimeMap(Integer timeInterval, Set<Integer> finalStates, int selectedTimeFormat) {

    	 SortedMap<Integer, SortedMap<Integer, ArrayList<ReportBeanWithHistory>>> yearToIntervalToReportBeanList = createYearToIntervalToReportBeanListMap(timeInterval, finalStates);
    	 SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToAverageValue = new TreeMap<Integer, SortedMap<Integer, Double>>();

    	 for (Map.Entry<Integer, SortedMap<Integer, ArrayList<ReportBeanWithHistory>>> yearToIntervalEntry : yearToIntervalToReportBeanList.entrySet()) {
    		 Integer year = yearToIntervalEntry.getKey();
    		 SortedMap<Integer, ArrayList<ReportBeanWithHistory>>intervalToReportBeanList = yearToIntervalEntry.getValue();
    		 yearToIntervalToAverageValue.put(year, new TreeMap<Integer, Double>());
    		 for (Entry<Integer, ArrayList<ReportBeanWithHistory>> intervalToWorkItemsListEntry: intervalToReportBeanList.entrySet()) {
    			 Integer interval = intervalToWorkItemsListEntry.getKey();
    			 ArrayList<ReportBeanWithHistory>reportBeanLists = intervalToWorkItemsListEntry.getValue();
    			 int count = 0;
    			 int sumOfDaysOrWorkingHours = 0;
    			 for (ReportBeanWithHistory reportBean : reportBeanLists) {
    				Integer stateID = getReportBeanStateID(reportBean);
    				Date lastStateChangeDate = getReportBeanLastStateChange(reportBean);
    				if (stateID==null || lastStateChangeDate == null) {
    					continue;
    				}
    				count++;
    				int valueToAdd = getNumberOfDaysBetweenDates(reportBean.getWorkItemBean().getCreated(), lastStateChangeDate);
    				//It was closed on same day when it was created => 1 day
    				if(valueToAdd == 0) {
    					valueToAdd = 1;
    				}
    				if(selectedTimeFormat == TIME_FORMAT.WORKING_HOURS) {
	    				Double workingDaysHourse = hoursPerWorkingDayMap.get(reportBean.getWorkItemBean().getProjectID());
	    				if(workingDaysHourse == null) {
	    					workingDaysHourse = 8.0;
	    				}
	    				valueToAdd *= workingDaysHourse;
    				}
    				sumOfDaysOrWorkingHours += valueToAdd;
    			 }
    			 Double avg = 0.0;
    			 if(count > 0) {
    				 avg = (double) sumOfDaysOrWorkingHours / count;
    			 }
    			 yearToIntervalToAverageValue.get(year).put(interval, avg);
    		 }
    	 }
    	 return yearToIntervalToAverageValue;
     }


     private Integer getReportBeanStateID(ReportBeanWithHistory reportBean) {
    	 Map<Integer, Map<Integer, HistoryValues>> historyMap = reportBean.getHistoryValuesMap();
    	 List<HistoryValues> historyValuesList = HistoryLoaderBL.getHistoryValuesList(historyMap, false);
    	 if (historyValuesList==null || historyValuesList.isEmpty()) {
    		 return null;
    	 }
    	 HistoryValues historyValues = historyValuesList.get(0);
    	 if (!SystemFields.INTEGER_STATE.equals(historyValues.getFieldID())) {
    		 return null;
    	 }
    	 Integer stateID = (Integer)historyValues.getNewValue();
    	 return stateID;
     }

     private Date getReportBeanLastStateChange(ReportBeanWithHistory reportBean) {
    	 Map<Integer, Map<Integer, HistoryValues>> historyMap = reportBean.getHistoryValuesMap();
    	 List<HistoryValues> historyValuesList = HistoryLoaderBL.getHistoryValuesList(historyMap, false);
    	 if (historyValuesList==null || historyValuesList.isEmpty()) {
    		 return null;
    	 }
    	 HistoryValues historyValues = historyValuesList.get(0);
    	 if (!SystemFields.INTEGER_STATE.equals(historyValues.getFieldID())) {
    		 return null;
    	 }
    	 Date lastStateChangeDate = historyValues.getLastEdit();
    	 return lastStateChangeDate;
     }

     public Double getMaxPlannedValue(SortedMap<Date, EarnedValueTimeSlice> earnedValueTimeSliceMap) {
         Double maxValue = -1.0;
         for (Map.Entry<Date, EarnedValueTimeSlice> entry : earnedValueTimeSliceMap.entrySet()) {
             Double d1 = entry.getValue().getPlannedValue();
             if (Double.compare(d1, maxValue) > 0) {
                 maxValue = entry.getValue().getPlannedValue();
             }
         }
         return maxValue;
     }

     public Double getMaxEarnedValue(SortedMap<Date, EarnedValueTimeSlice> earnedValueTimeSliceMap) {
         Double maxValue = -1.0;
         for (Map.Entry<Date, EarnedValueTimeSlice> entry : earnedValueTimeSliceMap.entrySet()) {
             if(entry.getValue().getEarnedvalue() != null) {
                 Double d1 = entry.getValue().getEarnedvalue();
                 if (Double.compare(d1, maxValue) > 0) {
                     maxValue = entry.getValue().getEarnedvalue();
                 }
             }
         }
         return maxValue;
     }


     private String convertResultMapToJSONData(SortedMap<Date, EarnedValueTimeSlice> earnedValueTimeSliceMap,Double maxValPlanned, Double maxEarned, int reportingInterval, int effortType) {
        StringBuilder local = new StringBuilder();
        local.append("[");
        for (Map.Entry<Date, EarnedValueTimeSlice>  entry : earnedValueTimeSliceMap.entrySet()){
            local.append("{");
            JSONUtility.appendStringValue(local, "date", getDateBasedOnReportInterval(entry.getKey(), reportingInterval));
            Double plannedVal = entry.getValue().getPlannedValue();
            if(effortType == EFFORT_TYPE.TIME) {
            	plannedVal = maxValPlanned - entry.getValue().getPlannedValue();
            }
            JSONUtility.appendDoubleValue(local, "plannedValue", plannedVal);
            Double earnValue = 0.0;
            if(entry.getValue().getEarnedvalue() != null) {
                earnValue = entry.getValue().getEarnedvalue();

            }
            if(effortType == EFFORT_TYPE.TIME) {
            	earnValue = maxEarned -  earnValue;
            }
            JSONUtility.appendDoubleValue(local, "earnedValue", earnValue, true);
            local.append("},");
        }
        if(local != null && local.length() > 0) {
            if(local.toString().endsWith(",")) {
                local = new StringBuilder(local.substring(0,  local.length() - 1));
            }
        }
        local.append("]");
        return local.toString();
     }

     private String getDateBasedOnReportInterval(Date date, int reportingInterval) {
        String formattedDate = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        if(reportingInterval == REPORTING_INTERVAL.DAILY) {
            SimpleDateFormat dtf = new SimpleDateFormat(ISO_FORMATTED_DATE);
            return dtf.format(date).toString();
        }else if(reportingInterval == REPORTING_INTERVAL.MONTHLY) {
            return year + "-" + month;
        }else if(reportingInterval == REPORTING_INTERVAL.WEEKLY) {
            return week + "/" + year;
        }
        return formattedDate;
     }

    public List<IntegerStringBean> getPossibleReportingIntervals(Locale locale, String bundleName) {
        List<IntegerStringBean> result = new ArrayList<IntegerStringBean>();
        result.add(new IntegerStringBean(LocalizeUtil.getLocalizedText(PROPERTY_PARAMS.REPORTING_INTERVAL_DAILY,
                locale, bundleName),	new Integer(REPORTING_INTERVAL.DAILY)));
        result.add(new IntegerStringBean(LocalizeUtil.getLocalizedText(PROPERTY_PARAMS.REPORTING_INTERVAL_WEEKLY,
                locale, bundleName),	new Integer(REPORTING_INTERVAL.WEEKLY)));
        result.add(new IntegerStringBean(LocalizeUtil.getLocalizedText(PROPERTY_PARAMS.REPORTING_INTERVAL_MONTHLY,
                locale, bundleName),	new Integer(REPORTING_INTERVAL.MONTHLY)));

        return result;
    }

    public int parseIntegerValue(Map configParameters, String fieldName, int defaultValue) {
        int result = defaultValue;
        try {
            result = Integer.parseInt((String)configParameters.get(fieldName));
        } catch (Exception e) {
        	LOGGER.debug("There is no value set for field: " + fieldName + " The default value will be used: " + defaultValue);
        }
        return result;
    }

    protected List<ReportBean> getReportBeanList(Integer datasourceType,
            Integer projectOrReleaseID, Integer filterID, Map<String, Object> contextMap,
            boolean includeOpen, boolean includeClosed, boolean includeSubs,
            TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
        List<ReportBean> reportBeanList = null;
        Boolean fromIssueNavigator = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR);
        Integer dashboardProjectOrReleaseID = (Integer)contextMap.get(CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID);
        if (fromIssueNavigator!=null && fromIssueNavigator.booleanValue()) {
            List<Integer> workItemIDs = (List<Integer>)contextMap.get(CONTEXT_ATTRIBUTE.WORKITEMIDS);
            if (workItemIDs!=null && !workItemIDs.isEmpty()) {
                reportBeanList = LoadItemIDListItems.getReportBeansByWorkItemIDs(GeneralUtils.createIntArrFromIntegerList(workItemIDs), false,
                        personBean.getObjectID(), locale, true, true, true, true, true, true, false, true, false);
            } else {
                    QueryContext queryContext=ItemNavigatorBL.loadLastQuery(personBean.getObjectID(),locale);
                    if (queryContext!=null){
                        reportBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
                    }
            }
        } else {
            if (dashboardProjectOrReleaseID!=null) {
                FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(dashboardProjectOrReleaseID, true, includeOpen, includeClosed);
                reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeansForReport(filterUpperTO, null, filterID, personBean, locale);
            } else {
                //saves the parameters into the database
                if (datasourceType==null) {
                    LOGGER.warn("No datasourceType was selected");
                    return null;
                }
                if (datasourceType.intValue()==DATASOURCE_TYPE.PROJECT_RELEASE) {
                    if (projectOrReleaseID==null) {
                        LOGGER.warn("No project/release was selected");
                        return null;
                    } else {
                        FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(projectOrReleaseID, true, includeOpen, includeClosed);
                        reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeansForReport(filterUpperTO, null, filterID, personBean, locale);
                    }
                } else {
                    if (filterID==null) {
                        LOGGER.warn("No filter was selected");
                        return null;
                    } else {
                        reportBeanList = FilterExecuterFacade.getSavedFilterReportBeanList(filterID, locale,
                                personBean, new LinkedList<ErrorData>(), null, null, true);
                    }
                }
            }
        }
        return ReportBeanLoader.addISOValuesToReportBeans(reportBeanList, personBean.getObjectID(), locale);
    }

	private Integer[] getSelectedStatuses(Map configParameters) {
		String values = configParameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS).toString();
		Integer[] integerValues = null;
		if(values != null) {
			String[] strArr = values.split(",");
			if (strArr!=null && strArr.length>0 && !"".equals(strArr[0])) {
				integerValues = new Integer[strArr.length];
				for (int i = 0; i < strArr.length; i++) {
					try {
						integerValues[i]=new Integer(strArr[i]);
					} catch (Exception e) {
						LOGGER.info("Converting the " + strArr[i] + " as the " + i + "th parameter to Integer failed with " + e.getMessage(), e);
						LOGGER.error(ExceptionUtils.getStackTrace(e),e);
					}
				}
			}
		}
		return integerValues;
	}

	public static int  getCalendarInterval(int timeInterval) {
		switch (timeInterval) {
		case TIME_INTERVAL.DAY:
			return Calendar.DAY_OF_YEAR;
		case TIME_INTERVAL.WEEK:
			return Calendar.WEEK_OF_YEAR;
		default:
			return Calendar.MONTH;
		}
	}

	/**
	 * Returns number of free days from given interval, start date included always, endDate only if includeLastDay == true!
	 * @param startDateParam
	 * @param endDateParam
	 * @param includeLastDay
	 * @return
	 */
	public static Integer getNumberOfDaysBetweenDates(Date startDateParam, Date endDateParam/*, boolean includeLastDay*/) {
		return (int)( (startDateParam.getTime() - endDateParam.getTime()) / (1000 * 60 * 60 * 24));
	}

	private Set<Integer> getProjectIDs(List<ReportBeanWithHistory> reportBeanWithHistoryList) {
		Set<Integer>projectIDs = new HashSet<Integer>();
		for (ReportBeanWithHistory reportBeanWithHistory : reportBeanWithHistoryList) {
			projectIDs.add(reportBeanWithHistory.getWorkItemBean().getProjectID());
		}
		return projectIDs;
	}

	/**
	 * Add the time series to the timeSeriesCollection
	 * SortedMap at first and second level (year and period)
	 * (Sorted because the accumulated should be calculated in the right order)
	 * @param timeSeriesCollection
	 * @param yearToPeriodToOpenedWorkItemCountMap
	 * @param selectedTimeInterval
	*/
	public static SortedMap<Date, Object> transformPeriodsToDates(
			SortedMap/*<Integer, SortedMap<Integer, Object>>*/ yearToPeriodToValuesMap,
			int selectedTimeInterval) {
		SortedMap<Date, Object> dateToValue = new TreeMap<Date, Object>();
		for (Iterator iterator = yearToPeriodToValuesMap.keySet().iterator(); iterator.hasNext();) {
			Integer year  = (Integer) iterator.next();
			SortedMap<Integer, Object> intervalToStatusChangeBeans = (SortedMap<Integer, Object>)yearToPeriodToValuesMap.get(year);
			Iterator<Integer> periodIterator = intervalToStatusChangeBeans.keySet().iterator();
			while (periodIterator.hasNext()) {
				Integer period = periodIterator.next();
				Object periodValue = intervalToStatusChangeBeans.get(period);
				if (periodValue!=null) {
					dateToValue.put(createDate(period.intValue(), year.intValue(), selectedTimeInterval), periodValue);
				}
			}
		}
		return dateToValue;
	}

	/**
	 * Created a regular time period
	 * @param period
	 * @param year
	 * @param timeInterval
	 * @return
	 */
	public static Date createDate(int period, int year, int timeInterval) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setLenient(true);
		calendar.set(Calendar.YEAR, year);
		switch (timeInterval) {
		case TIME_INTERVAL.DAY:
			calendar.set(Calendar.DAY_OF_YEAR, period);
			break;
		case TIME_INTERVAL.WEEK:
			calendar.set(Calendar.WEEK_OF_YEAR, period);
			break;
		default:
			calendar.set(Calendar.MONTH, period);
		}
		return calendar.getTime();
	}
}
