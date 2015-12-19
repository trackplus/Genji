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
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.ReportBeanLoader;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.ListBL;
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
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.report.dashboard.ProjectFilterDashboardView.DATASOURCE_TYPE;
import com.aurel.track.report.datasource.IPluggableDatasource.CONTEXT_ATTRIBUTE;
import com.aurel.track.report.datasource.earnedValue.EarnedValueBL;
import com.aurel.track.report.datasource.earnedValue.EarnedValueDatasource;
import com.aurel.track.report.datasource.earnedValue.EarnedValueTimeSlice;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.StringArrayParameterUtils;
import com.sun.tools.ws.ant.Apt.Option;

public class BurnDownChart  extends TimePeriodDashboardView {
    private static final Logger LOGGER = LogManager.getLogger(BurnDownChart.class);

    private List<ReportBean> reportBeanList;
    private List<ReportBeanWithHistory> reportBeanWithHistoryList;
    private Date dateTo;
    private Date dateFrom;
	private HashMap<Integer, Integer> storyPointValueIDToValueMap;
	private int sumOfStoryPointForAllItems;
	private Integer customFieldIDForStoryPoint;

    static final int PLANNED_VALUE = 1;
    static final int ACTUAL_EFFORT = 2;
    static final int EARNED_VALUE = 3;
    static String ISO_FORMATTED_DATE = "yyyy-MM-dd";

    //Configuration page constants
    public static interface CONFIGURATION_PARAMETERS {
        static String STATUSES = "statuses";
        static String SELECTED_STATUS = "selectedStatus";
        static String TIME_INTERVALS = "timeIntervals";
        static String EFFORT_TYPE = "effortType";
        static String CUSTOM_FIELD_FOR_STORY_POINT = "customFieldForStoryPoint";
        static String SELECTED_EFFORT_TYPE = "selectedEffortType";
        static String SELECTED_CUSTOM_FIELD_FOR_STORY_POINT = "selectedCustomFieldForStoryPoint";
        static String REPORTING_INTERVAL = "reportingInterval";
        static String SELECTED_REPORTING_INTERVAL = "selectedReportingInterval";
        static String Y_AXE = "yAxe";
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

    // Effort type constants
    public static interface EFFORT_TYPE {
        static int TIME = 1;
        static int NR_OF_ITEMS_IN_INTERVAL = 2;
        static int STORY_POINTS = 3;
    }

    // Reporting interval constants
    public static interface REPORTING_INTERVAL {
        static int DAILY= 1;
        static int WEEKLY = 2;
        static int MONTHLY = 3;
    }

    // Property parameters
    public static interface PROPERTY_PARAMS {
        static String EFFORT_TYPE_EFFORT = "burnDownChart.tooltip.effortTypeEffort";
        static String EFFORT_TYPE_NR_OF_ITEMS_IN_INTERVAL = "burnDownChart.tooltip.effortTypeNrOfItemsInInterval";
        static String EFFORT_TYPE_STORY_POINTS = "burnDownChart.tooltip.effortTypeStoryPoints";
        static String EFFORT_TYPE_COST = "burnDownChart.tooltip.effortTypeCost";
        static String REPORTING_INTERVAL_DAILY = "burnDownChart.tooltip.reportingInterval.daily";
        static String REPORTING_INTERVAL_WEEKLY = "burnDownChart.tooltip.reportingInterval.weekly";
        static String REPORTING_INTERVAL_MONTHLY = "burnDownChart.tooltip.reportingInterval.monthly";
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
            if(chartData == null) {
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
            Integer selectedEffortType = Integer.parseInt(parameters.get(CONFIGURATION_PARAMETERS.EFFORT_TYPE).toString());

            customFieldIDForStoryPoint = parseInteger(parameters, CONFIGURATION_PARAMETERS.CUSTOM_FIELD_FOR_STORY_POINT, -1);


            String yAxesLabel = "";
            if(selectedEffortType == EFFORT_TYPE.NR_OF_ITEMS_IN_INTERVAL) {
            	yAxesLabel = LocalizeUtil.getLocalizedText("burnDownChart.yAxesLabel.no.of.item", locale, bundleName);
            }else if(selectedEffortType == EFFORT_TYPE.STORY_POINTS) {
            	yAxesLabel = LocalizeUtil.getLocalizedText("burnDownChart.yAxesLabel.story.point", locale, bundleName);
            }else if(selectedEffortType == EFFORT_TYPE.TIME) {
            	yAxesLabel = LocalizeUtil.getLocalizedText("burnDownChart.yAxesLabel.effort", locale, bundleName);
            }

            JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_EFFORT_TYPE, selectedEffortType);
            JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_CUSTOM_FIELD_FOR_STORY_POINT, customFieldIDForStoryPoint);

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
        DashboardDescriptor dashboardDescriptor = getDescriptor();
        String bundleName = dashboardDescriptor.getBundleName();

        sb.append(getDatasourceConfig(parameters, entityId, entityType, locale));
        sb.append(getTimePeriodConfig(parameters, locale));

        JSONUtility.appendILabelBeanList(sb,CONFIGURATION_PARAMETERS.STATUSES, StatusBL.loadAll(locale));
        JSONUtility.appendStringList(sb,CONFIGURATION_PARAMETERS.SELECTED_STATUS,
                StringArrayParameterUtils.splitSelection(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS)));

        //Reporting interval
        JSONUtility.appendIntegerStringBeanList(sb, BurnDownChart.CONFIGURATION_PARAMETERS.REPORTING_INTERVAL, getPossibleReportingIntervals(locale, bundleName));
        int reportingInterval = parseInteger(parameters, CONFIGURATION_PARAMETERS.REPORTING_INTERVAL, TIME_INTERVAL.MONTH);
        JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_REPORTING_INTERVAL, reportingInterval);

        JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.Y_AXE, parseInteger(parameters, CONFIGURATION_PARAMETERS.Y_AXE, DEFAULT_HEIGHT));
        //Effort type
        JSONUtility.appendIntegerStringBeanList(sb, BurnDownChart.CONFIGURATION_PARAMETERS.EFFORT_TYPE, getPossibleEffortTypes(locale, bundleName));
        //selected Effort type
        Integer selectedEffortType = parseIntegerValue(parameters, CONFIGURATION_PARAMETERS.EFFORT_TYPE, EFFORT_TYPE.NR_OF_ITEMS_IN_INTERVAL);
        JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_EFFORT_TYPE, selectedEffortType);


        JSONUtility.appendIntegerStringBeanList(sb, BurnDownChart.CONFIGURATION_PARAMETERS.CUSTOM_FIELD_FOR_STORY_POINT, getPossibleCustomFieldsAsStoryPointValue());

        customFieldIDForStoryPoint = -1;
        if(parameters.get(CONFIGURATION_PARAMETERS.CUSTOM_FIELD_FOR_STORY_POINT) != null) {
        	customFieldIDForStoryPoint = parseIntegerValue(parameters, CONFIGURATION_PARAMETERS.CUSTOM_FIELD_FOR_STORY_POINT, -1);
        }
        JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_CUSTOM_FIELD_FOR_STORY_POINT, customFieldIDForStoryPoint);
        JSONUtility.appendIntegerValue(sb, "storyPointEffortTypeID", EFFORT_TYPE.STORY_POINTS);
        int prefWidth = 480;
        int prefHeight = 680;
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

        boolean isTime = true;
        Map<String, Object>contextMap = new HashMap<String, Object>();
        contextMap.put("fromIssueNavigator", false);
        contextMap.put("useProjectSpecificID", false);
     	Integer selectedEffortType = Integer.parseInt(configParameters.get(CONFIGURATION_PARAMETERS.EFFORT_TYPE).toString());
        reportBeanList = getReportBeanList(datasourceType, projectOrReleaseID, filterID, contextMap, true, true, true, personBean, locale);
        reportBeanWithHistoryList = ReportBeanHistoryLoader.getReportBeanWithHistoryList(
              reportBeanList, locale,	false, false, true, new Integer[]{SystemFields.INTEGER_STATE}, true, true, true, true, false, personBean.getObjectID(), null,
                 dateFrom, dateTo, true, LONG_TEXT_TYPE.ISPLAIN);
            SortedMap<Date, EarnedValueTimeSlice> earnedValueTimeSliceMap = new TreeMap<Date, EarnedValueTimeSlice>();

            int reportingInterval = parseIntegerValue(configParameters, CONFIGURATION_PARAMETERS.REPORTING_INTERVAL, 1);
            if (!reportBeanWithHistoryList.isEmpty()) {
                LOGGER.debug("Number of reportBeanWithHistoryList after removing items with no budget or overlapping start/end dates " + reportBeanWithHistoryList.size());
                Date minStartDate = EarnedValueBL.getMinMaxStartEndDate(reportBeanWithHistoryList, true);
                Date maxEndDate = EarnedValueBL.getMinMaxStartEndDate(reportBeanWithHistoryList, false);
                if (minStartDate!=null && maxEndDate!=null) {
                	Integer[] selectedStatuses = getSelectedStatuses(configParameters);
                	SortedMap<Integer, SortedMap<Integer, Double>> plannedValues = null;
                	SortedMap<Integer, SortedMap<Integer, Double>> earnedValues  = null;
                	if(selectedEffortType == EFFORT_TYPE.TIME) {
                   		Set<Integer> projectIDs = EarnedValueBL.prefilterReportBeansGetProjectIDs(reportBeanWithHistoryList, isTime, dateFrom, dateTo);
                    	Map<Integer, Double> hoursPerWorkingDayMap = ProjectBL.getHoursPerWorkingDayMap(projectIDs);
                		plannedValues = EarnedValueBL.calculatePlannedValues(reportBeanWithHistoryList, isTime, minStartDate, maxEndDate, reportingInterval, hoursPerWorkingDayMap);
                		earnedValues = EarnedValueBL.calculateEarnedValues(reportBeanWithHistoryList, isTime, minStartDate, maxEndDate, reportingInterval, hoursPerWorkingDayMap, GeneralUtils.createSetFromIntegerArr(selectedStatuses));
                		BurnDownChartBL.addZerosForEmptyIntervals(dateFrom, dateTo, reportingInterval, plannedValues, false);
                        BurnDownChartBL.addZerosForEmptyIntervals(dateFrom, dateTo, reportingInterval, earnedValues, false);
                        EarnedValueBL.accumulateValues(plannedValues);
                        EarnedValueBL.accumulateValues(earnedValues);
                	}else if(selectedEffortType == EFFORT_TYPE.NR_OF_ITEMS_IN_INTERVAL) {
                   		plannedValues = calculatePlannedValuesDependingOnEffortType(minStartDate, maxEndDate, reportingInterval, selectedEffortType);
                		earnedValues = calculateEarnedValuesDependingOnEffortType(plannedValues, reportingInterval,
                				GeneralUtils.createSetFromIntegerArr(selectedStatuses), selectedEffortType);
                		   BurnDownChartBL.addZerosForEmptyIntervals(dateFrom, dateTo, reportingInterval, plannedValues, false);
                           BurnDownChartBL.addZerosForEmptyIntervals(dateFrom, dateTo, reportingInterval, earnedValues, false);
                	}else if(selectedEffortType == EFFORT_TYPE.STORY_POINTS) {
                		customFieldIDForStoryPoint =  Integer.parseInt(configParameters.get(CONFIGURATION_PARAMETERS.CUSTOM_FIELD_FOR_STORY_POINT).toString());
                		storyPointValueIDToValueMap = createStoryPointValueIDToValueMap(reportBeanList);

                		sumOfStoryPointForAllItems = calculateSumOfStoryPointForAllItems(reportBeanList);

                		plannedValues = calculatePlannedValuesDependingOnEffortType(minStartDate, maxEndDate, reportingInterval, selectedEffortType);
                		earnedValues = calculateEarnedValuesDependingOnEffortType(plannedValues, reportingInterval,
                				GeneralUtils.createSetFromIntegerArr(selectedStatuses), selectedEffortType);

                		BurnDownChartBL.addZerosForEmptyIntervals(dateFrom, dateTo, reportingInterval, plannedValues, false);
                        BurnDownChartBL.addZerosForEmptyIntervals(dateFrom, dateTo, reportingInterval, earnedValues, false);
                	}
                    SortedMap<Date, Object> plannedDateValues = BurnDownChartBL.transformPeriodsToDates(plannedValues, reportingInterval);
                    SortedMap<Date, Object> plannedEarnedValues = BurnDownChartBL.transformPeriodsToDates(earnedValues, reportingInterval);
                    EarnedValueBL.addValueTypeToBeans(earnedValueTimeSliceMap,
                            plannedDateValues, PLANNED_VALUE);
                    EarnedValueBL.addValueTypeToBeans(earnedValueTimeSliceMap,
                            plannedEarnedValues, EARNED_VALUE);
                    Double maxValPlanned = getMaxPlannedValue(earnedValueTimeSliceMap);
                    Double maxValEarned = getMaxEarnedValue(earnedValueTimeSliceMap);
                    return convertResultMapToJSONData(earnedValueTimeSliceMap, maxValPlanned, maxValEarned, reportingInterval, selectedEffortType, reportingInterval).toString();
                }
            }
         return null;
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


     private String convertResultMapToJSONData(SortedMap<Date, EarnedValueTimeSlice> earnedValueTimeSliceMap,Double maxValPlanned, Double maxEarned, int reportingInterval, int effortType, Integer timeInterval) {
        StringBuilder local = new StringBuilder();
        local.append("[");
        boolean last = false;
        for (Map.Entry<Date, EarnedValueTimeSlice>  entry : earnedValueTimeSliceMap.entrySet()){
            local.append("{");
            JSONUtility.appendStringValue(local, "date", getDateBasedOnReportInterval(entry.getKey(), reportingInterval));
            Double plannedVal = entry.getValue().getPlannedValue();
            if(effortType == EFFORT_TYPE.TIME) {
            	plannedVal = maxValPlanned - entry.getValue().getPlannedValue();
            }
            JSONUtility.appendDoubleValue(local, "plannedValue", plannedVal, true);
            Double earnValue = 0.0;
            if(entry.getValue().getEarnedvalue() != null) {
                earnValue = entry.getValue().getEarnedvalue();

            }
            if(effortType == EFFORT_TYPE.TIME) {
            	earnValue = maxEarned -  earnValue;
            }

            int calendarInterval = EarnedValueDatasource.getCalendarInterval(timeInterval);
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(new Date());
    		calendar.add(calendarInterval, 1);
            if(DateTimeUtils.lessOrEqual(entry.getKey(), calendar.getTime())) {
            	local.append(",");
            	JSONUtility.appendDoubleValue(local, "earnedValue", earnValue, true);
            }
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

    public List<IntegerStringBean> getPossibleEffortTypes(Locale locale, String bundleName) {
        List<IntegerStringBean> result = new ArrayList<IntegerStringBean>();
        result.add(new IntegerStringBean(LocalizeUtil.getLocalizedText(PROPERTY_PARAMS.EFFORT_TYPE_EFFORT,
                locale, bundleName), new Integer(EFFORT_TYPE.TIME)));
        result.add(new IntegerStringBean(LocalizeUtil.getLocalizedText(PROPERTY_PARAMS.EFFORT_TYPE_NR_OF_ITEMS_IN_INTERVAL,
                locale, bundleName), new Integer(EFFORT_TYPE.NR_OF_ITEMS_IN_INTERVAL)));
        result.add(new IntegerStringBean(LocalizeUtil.getLocalizedText(PROPERTY_PARAMS.EFFORT_TYPE_STORY_POINTS,
                locale, bundleName), new Integer(EFFORT_TYPE.STORY_POINTS)));

        return result;
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

    private List<IntegerStringBean> getPossibleCustomFieldsAsStoryPointValue() {
    	List<TFieldBean> customFieldBeans = FieldBL.loadCustom();
   	 	List<IntegerStringBean> result = new ArrayList<IntegerStringBean>();
   	 	if (customFieldBeans!=null) {
   	 		for (TFieldBean fieldBean : customFieldBeans) {
   			 	if(fieldBean.getFieldType().equalsIgnoreCase("com.aurel.track.fieldType.types.custom.CustomSelectSimple")) {
   			 		result.add(new IntegerStringBean(fieldBean.getName(), fieldBean.getObjectID()));
   			 	}
   	 		}
   	 	}
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

	public SortedMap<Integer, SortedMap<Integer, Double>> calculatePlannedValuesDependingOnEffortType(Date minStartDate, Date maxEndDate, Integer timeInterval, Integer selectedEffortType) {

		SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToPlannedValue = new TreeMap<Integer, SortedMap<Integer, Double>>();
		if (reportBeanWithHistoryList!=null) {
			Calendar calendarStartDate = Calendar.getInstance();
			Calendar calendarEndDate = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			//to avoid that the last days of the year to be taken as the first
			//week of the now year but the year remains the old one
			int calendarInterval = EarnedValueDatasource.getCalendarInterval(timeInterval);
			for (ReportBean reportBean : reportBeanWithHistoryList) {
				TWorkItemBean workItemBean = reportBean.getWorkItemBean();
				Date startDate = workItemBean.getStartDate();
				Date endDate = workItemBean.getEndDate();
				if (startDate == null || endDate == null) {
					continue;
				}

				calendarStartDate.setTime(dateFrom);
				calendarEndDate.setTime(dateTo);
				calendar.setTime(dateFrom);
				int yearValue = calendarStartDate.get(Calendar.YEAR);
				int intervalValue = calendarStartDate.get(calendarInterval);
				boolean isFirst = true;
				while (calendar.before(calendarEndDate) || isFirst) {
					if (isFirst) {
						isFirst = false;
					} else {
						calendar.add(calendarInterval, 1);
					}
					yearValue = calendar.get(Calendar.YEAR);
					intervalValue = calendar.get(calendarInterval);
					SortedMap<Integer, Double> intervalToPlannedValues = yearToIntervalToPlannedValue.get(Integer.valueOf(yearValue));
					if (intervalToPlannedValues==null) {
						yearToIntervalToPlannedValue.put(new Integer(yearValue), new TreeMap<Integer, Double>());
						intervalToPlannedValues = yearToIntervalToPlannedValue.get(Integer.valueOf(yearValue));
					}
					Double totalPlannedValueForInterval = intervalToPlannedValues.get(Integer.valueOf(intervalValue));
					if (totalPlannedValueForInterval==null) {
						totalPlannedValueForInterval = 0.0;
					}
					if(DateTimeUtils.less(calendar.getTime(), endDate)) {
						if(selectedEffortType == EFFORT_TYPE.NR_OF_ITEMS_IN_INTERVAL) {
							totalPlannedValueForInterval += 1.0;
						}else {
							Integer storyPointValueID = getStoryPointValueID(workItemBean, customFieldIDForStoryPoint);
							if(storyPointValueID != null) {
								Integer storyPointValue = storyPointValueIDToValueMap.get(storyPointValueID);
				    			if(storyPointValue != null) {
				    				totalPlannedValueForInterval += storyPointValue;
				    			}
				    		}
						}
					}
					intervalToPlannedValues.put(Integer.valueOf(intervalValue), totalPlannedValueForInterval);
				}
			}
		}
		return yearToIntervalToPlannedValue;
	}


	public SortedMap<Integer, SortedMap<Integer, Double>> calculateEarnedValuesDependingOnEffortType(SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToPlannedValue, Integer timeInterval, Set<Integer> finalStates, Integer selectedEffortType) {

		SortedMap<Integer, SortedMap<Integer, Double>> yearToIntervalToEarnedValue = new TreeMap<Integer, SortedMap<Integer, Double>>();

		if (reportBeanWithHistoryList != null) {
			Calendar calendarStartDate = Calendar.getInstance();
			Calendar calendarEndDate = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			//to avoid that the last days of the year to be taken as the first
			//week of the now year but the year remains the old one
			int calendarInterval = EarnedValueDatasource.getCalendarInterval(timeInterval);
			for (ReportBeanWithHistory reportBeanWithHistory : reportBeanWithHistoryList) {
				TWorkItemBean workItemBean = reportBeanWithHistory.getWorkItemBean();
				Date startDate = workItemBean.getStartDate();
				Date endDate = workItemBean.getEndDate();
				if (startDate == null || endDate == null) {
					continue;
				}
				calendarStartDate.setTime(dateFrom);
				calendarEndDate.setTime(dateTo);

				Map<Integer, Map<Integer, HistoryValues>> historyMap = reportBeanWithHistory.getHistoryValuesMap();
				List<HistoryValues> historyValuesList = HistoryLoaderBL.getHistoryValuesList(historyMap, false);
				if (historyValuesList==null || historyValuesList.isEmpty()) {
					continue;
				}
				//the first HistoryValue should be the most recent because of sorting in HistoryLoaderBL.getHistoryValuesList()
				HistoryValues historyValues = historyValuesList.get(0);
				if (!SystemFields.INTEGER_STATE.equals(historyValues.getFieldID())) {
					//should never happen
					continue;
				}
				Integer stateID = (Integer)historyValues.getNewValue();
				if (stateID==null) {
					continue;
				}
				Date lastStateChangeDate = historyValues.getLastEdit();
				calendar.setTime(dateFrom);
				int yearValue = calendarStartDate.get(Calendar.YEAR);
				int intervalValue = calendarStartDate.get(calendarInterval);
				boolean isFirst = true;
				while (calendar.before(calendarEndDate) || isFirst) {
					if (isFirst) {
						isFirst = false;
					} else {
						calendar.add(calendarInterval, 1);
					}
					yearValue = calendar.get(Calendar.YEAR);

					intervalValue = calendar.get(calendarInterval);
					SortedMap<Integer, Double> intervalToPlannedValues = yearToIntervalToEarnedValue.get(Integer.valueOf(yearValue));
					if (intervalToPlannedValues==null) {
						yearToIntervalToEarnedValue.put(new Integer(yearValue), new TreeMap<Integer, Double>());
						intervalToPlannedValues = yearToIntervalToEarnedValue.get(Integer.valueOf(yearValue));
					}
					Double totalPlannedValueForInterval = intervalToPlannedValues.get(Integer.valueOf(intervalValue));
					if (totalPlannedValueForInterval==null) {
						SortedMap<Integer, Double> firstIntervalToPlannedvalues = yearToIntervalToPlannedValue.get(yearToIntervalToPlannedValue.firstKey());
						totalPlannedValueForInterval = firstIntervalToPlannedvalues.get(firstIntervalToPlannedvalues.firstKey());
					}
					if(DateTimeUtils.less(lastStateChangeDate, calendar.getTime())) {
						if(finalStates.contains(stateID)) {
							if(selectedEffortType == EFFORT_TYPE.NR_OF_ITEMS_IN_INTERVAL) {
								totalPlannedValueForInterval -= 1.0;
							}else {
								Integer storyPointValueID = getStoryPointValueID(workItemBean, customFieldIDForStoryPoint);
								if(storyPointValueID != null) {
					    			Integer storyPointValue = storyPointValueIDToValueMap.get(storyPointValueID);
					    			if(storyPointValue != null) {
					    				totalPlannedValueForInterval -= (double)storyPointValue;
					    			}
					    		}
							}
						}
					}
					intervalToPlannedValues.put(Integer.valueOf(intervalValue), totalPlannedValueForInterval);
				}
			}
		}
		return yearToIntervalToEarnedValue;
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


    private HashMap<Integer, Integer> createStoryPointValueIDToValueMap(List<ReportBean> reportBeanList) {
    	HashMap<Integer, Integer>storyPointValueIDToValue = new HashMap<Integer, Integer>();
    	HashSet<Integer>valueIDs = new HashSet<Integer>();
    	for (ReportBean reportBean : reportBeanList) {
    		Integer storyPointValueID = getStoryPointValueID(reportBean.getWorkItemBean(), customFieldIDForStoryPoint);
    		if(storyPointValueID != null) {
    			valueIDs.add(storyPointValueID);
    		}
    	}
    	List<TOptionBean>optionBeanList = OptionBL.loadByKeys(GeneralUtils.createIntegerArrFromSet(valueIDs));
    	for (TOptionBean tOptionBean : optionBeanList) {
    		storyPointValueIDToValue.put(tOptionBean.getObjectID(), Integer.parseInt(tOptionBean.getLabel()));
		}
    	return storyPointValueIDToValue;
    }

    private int calculateSumOfStoryPointForAllItems(List<ReportBean> reportBeanList) {
    	int sum = 0;
    	for (ReportBean reportBean : reportBeanList) {
    		Integer storyPointValueID = getStoryPointValueID(reportBean.getWorkItemBean(), customFieldIDForStoryPoint);
    		if(storyPointValueID != null) {
    			Integer storyPointValue = storyPointValueIDToValueMap.get(storyPointValueID);
    			if(storyPointValue != null) {
    				sum += storyPointValue;
    			}
    		}
    	}
    	return sum;
    }

    private Integer getStoryPointValueID(TWorkItemBean workItemBean, Integer storyPointFieldID) {
    	if(workItemBean.getAttribute(storyPointFieldID) != null) {
			Object[] values = (Object[])workItemBean.getAttribute(storyPointFieldID);
			if(values.length > 0) {
				return (Integer)values[0];
			}
    	}
    	return null;
    }
}
