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

package com.aurel.track.report.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperFromQNodeTransformer;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.TreeFilterLoaderBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TQueryRepositoryBean.QUERY_PURPOSE;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.history.HistorySelectValues;
import com.aurel.track.item.history.HistoryTransactionBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.report.dashboard.DataSourceDashboardBL.CONFIGURATION_PARAMETERS;
import com.aurel.track.report.datasource.statusOverTime.StatusOverTimeDatasource;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.CalendarUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.StringArrayParameterUtils;
import com.opensymphony.xwork2.config.Configuration;

/**
 * Status over time graph
 * @author Tamas
 *
 */
public class StatusOverTimeGraph extends TimePeriodDashboardView {
	private static final Logger LOGGER = LogManager.getLogger(StatusOverTimeGraph.class);

	/**
	 * When no status is needed (new workItems in a period) then the complete map structure is still built
	 * in order to not duplicate the code
	 */
	public static Integer ENTITY_PLACEHOLDER = Integer.valueOf(0);

	//Configuration page constants
	public static interface CONFIGURATION_PARAMETERS {

		static String STATUSES = "statuses";
		static String SELECTED_STATUS = "selectedStatus";
		static String SELECTED_STATUS_SECOND = "selectedStatusSecond";
		static String SELECTED_STATUS_THIRD = "selectedStatusThird";
		static String TIME_INTERVALS = "timeIntervals";
		static String SELECTED_TIME_INTERVAL = "selectedTimeInterval";
		static String CALCULATION_MODES = "calculationModes";
		static String SELECTED_CALCULATION_MODE = "selectedCalculationMode";
		static String DISABLE_STATUS = "disableStatus";
        static String FILLED = "filled";
		static String Y_AXE = "yAxe";
		static String SELECTED_CHART_TYPE = "selectedChartType";
		static String GROUPING = "grouping";
		static String GROUP_FIRST_NAME = "groupFirstName";
		static String GROUP_SECOND_NAME = "groupSecondName";
		static String GROUP_THIRD_NAME = "groupThirdName";
		static String ISSUE_TYPE = "issueType";
		static String SELECTED_ISSUE_TYPE = "selectedIssueType";
	}

	protected int DEFAULT_HEIGHT = 250;
	protected int MIN_HEIGHT = 100;
	protected int MAX_HEIGHT = 900;

	private static interface SESSION_PARAMETERS {
		static String PROVIDER_LINK = "providerLink";
		static String PROVIDER_PARAMS = "providerParams";
	}

	public static interface PERIOD_TYPE {
		static int FROM_TO = 1;
		static int DAYS_BEFORE = 2;
	}

	public static interface TIME_INTERVAL {
		static int DAY = 1;
		static int WEEK = 2;
		static int MONTH = 3;
	}

	public static interface CHART_TYPE {
		static int LINE = 1;
		static int STACKED = 2;
	}

	public static interface ISSUE_TYPE {
		static int GENERAL = 1;
		static int DOCUMENT = 2;
	}

	public static interface CALCULATION_MODE {
		static int NEW = 1; //no status considered
		static int ACTUAL_SAMPLE = 2; //counted if the selected status is the last status at the end if time interval
		static int ACTUAL_ACTIVITY = 3; //counted if the selected status was selected at any time in the time interval
		static int ACCUMULATED_ACTIVITY = 4; //ACTUAL_ACTIVITY accumulated
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

		//the name where the ImageProvider implementation is stored in the session
		String theProvider =sessionPrefix;
		//the name where configParameters are stored
		String theParams =sessionPrefix+"Params";

		//to avoid caching if IE
		Long actualTime=new Long(new Date().getTime());

		Integer selectedCalculationMode = parseInteger(parameters, CONFIGURATION_PARAMETERS.SELECTED_CALCULATION_MODE, CALCULATION_MODE.NEW);
		//for ftl
		String userEnteredTitle = parameters.get("title");
		if (userEnteredTitle==null || "".equals(userEnteredTitle.trim())) {
			//title="statusOverTime.title." + getCalculationModeSuffix(selectedCalculationMode));
		}

//		int width=BasePluginDashboardBL.parseIntegerValue(parameters, CONFIGURATION_PARAMETERS.X_AXE, DEFAULT_WIDTH);
		int height=parseInteger(parameters, CONFIGURATION_PARAMETERS.Y_AXE, DEFAULT_HEIGHT);

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
		configParameters.put(SESSION_PARAMETERS.PROVIDER_LINK, theProvider);
		configParameters.put(SESSION_PARAMETERS.PROVIDER_PARAMS, theParams);

		//to avoid caching if IE
		configParameters.put("actualTime", new Long(new Date().getTime()));

//		configParameters.put("width",width);
		configParameters.put("height",height);

		//save the theProvider ImageProvider and the configParameters map into the session
		//they will be used in the .ftl to call the corresponding image provider action
		session.put(theProvider,this);
		session.put(theParams, configParameters);

		String url="imageServerAction!loadJSON.action?imageProviderKey="+theProvider+
			"&imageProviderParams="+theParams+"&date="+actualTime;

		JSONUtility.appendStringValue(sb, "url",url);
//		JSONUtility.appendIntegerValue(sb, "width",width);
		JSONUtility.appendIntegerValue(sb, "height",height);
        if ( (configParameters.get("dateTo") != null && configParameters.get("dateTo") != null && !configParameters.get("dateTo").equals("null") &&
        		!configParameters.get("dateFrom").equals("null")) || (configParameters.get("daysBefore") != null) ) {
            JSONUtility.appendBooleanValue(sb, "empty", false);
            String JSONResponse = generateJSONResponse(configParameters);
            JSONUtility.appendJSONValue(sb, "chartData", JSONResponse);

            if (configParameters.get("daysBefore") != null) {
                SimpleDateFormat dateFormat;
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                int daysBefore = BasePluginDashboardBL.parseIntegerValue(configParameters, TIMEPERIOD_PARAMETERS.DAYS_BEFORE, 0);
                Date dateTo = calendar.getTime();
                Date dateFrom = calendar.getTime();
                if(daysBefore!=0){
                    calendar.add(Calendar.DATE, -daysBefore);
                    dateFrom = calendar.getTime();
                }
                JSONUtility.appendStringValue(sb, "dateTo", dateFormat.format(dateTo).toString());
                JSONUtility.appendStringValue(sb, "dateFrom", dateFormat.format(dateFrom).toString());
            } else {
                JSONUtility.appendStringValue(sb, "dateTo", configParameters.get("dateTo").toString());
                JSONUtility.appendStringValue(sb, "dateFrom", configParameters.get("dateFrom").toString());
            }

            String xAxesLabel = "";
            if (configParameters.get("selectedTimeInterval").toString().equals("2")) {
            	xAxesLabel = LocalizeUtil.getLocalizedText("statusOverTime.xAxesLabelMonthly", locale, bundleName);
            }else {
            	xAxesLabel = LocalizeUtil.getLocalizedText("statusOverTime.xAxesLabel", locale, bundleName);
            }
            String yAxesLabel = LocalizeUtil.getLocalizedText("statusOverTime.yAxesLabel." + getCalculationModeSuffix(selectedCalculationMode), locale, bundleName);

            JSONUtility.appendStringValue(sb, "xAxesLabel", xAxesLabel);
            JSONUtility.appendStringValue(sb, "yAxesLabel", yAxesLabel);
            JSONUtility.appendStringValue(sb, "selectedTimeInterval", configParameters.get("selectedTimeInterval").toString());
            JSONUtility.appendStringValue(sb, "locale", locale.toString());

            if (configParameters.get("selectedStatus") != null) {
                JSONUtility.appendStringValue(sb, "selectedStatus", configParameters.get("selectedStatus").toString());
            }

            if (configParameters.get("selectedStatusSecond") != null) {
                JSONUtility.appendStringValue(sb, "selectedStatusSecond", configParameters.get("selectedStatusSecond").toString());
            }

            if (configParameters.get("selectedStatusThird") != null) {
                JSONUtility.appendStringValue(sb, "selectedStatusThird", configParameters.get("selectedStatusThird").toString());
            }
            JSONUtility.appendILabelBeanList(sb,CONFIGURATION_PARAMETERS.STATUSES, StatusBL.loadAll(locale));
            LOGGER.debug(("Status over time config parameters: " + configParameters.toString()));
            LOGGER.debug(("Status over time  JSON Response: " + JSONResponse));
        }else {
            JSONUtility.appendBooleanValue(sb, "empty", true);
            LOGGER.debug("Status over time chart not configured yet!");
        }
        JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_CHART_TYPE, parseInteger(parameters, CONFIGURATION_PARAMETERS.SELECTED_CHART_TYPE, CHART_TYPE.LINE));

        if (parameters.get(CONFIGURATION_PARAMETERS.FILLED) != null) {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.FILLED, true);
        } else {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.FILLED, false);
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUPING) != null) {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.GROUPING, true);
        } else {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.GROUPING, false);
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUP_FIRST_NAME) != null) {
            JSONUtility.appendStringValue(sb, CONFIGURATION_PARAMETERS.GROUP_FIRST_NAME, configParameters.get(CONFIGURATION_PARAMETERS.GROUP_FIRST_NAME).toString());
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUP_SECOND_NAME) != null) {
            JSONUtility.appendStringValue(sb, CONFIGURATION_PARAMETERS.GROUP_SECOND_NAME, configParameters.get(CONFIGURATION_PARAMETERS.GROUP_SECOND_NAME).toString());
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUP_THIRD_NAME) != null) {
            JSONUtility.appendStringValue(sb, CONFIGURATION_PARAMETERS.GROUP_THIRD_NAME, configParameters.get(CONFIGURATION_PARAMETERS.GROUP_THIRD_NAME).toString());
        }

        return sb.toString();
	}
     /*fill config windows items */
	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();

		Locale locale = user.getLocale();
		//DateTimeUtils dateTimeUtils = DateTimeUtils.getInstance();
		DashboardDescriptor dashboardDescriptor = getDescriptor();
		sb.append(getDatasourceConfig(parameters, entityId, entityType, locale));
		//DataSourceDashboardBL.appendJSONExtraDataConfig_DataSource(sb,dashboardDescriptor,parameters,user,entityId,entityType);
		sb.append(getTimePeriodConfig(parameters, locale));

		JSONUtility.appendILabelBeanList(sb,CONFIGURATION_PARAMETERS.STATUSES, StatusBL.loadAll(locale));

		JSONUtility.appendStringList(sb,CONFIGURATION_PARAMETERS.SELECTED_STATUS,
				StringArrayParameterUtils.splitSelection(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS)));

		JSONUtility.appendStringList(sb,CONFIGURATION_PARAMETERS.SELECTED_STATUS_SECOND,
				StringArrayParameterUtils.splitSelection(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS_SECOND)));

		JSONUtility.appendStringList(sb,CONFIGURATION_PARAMETERS.SELECTED_STATUS_THIRD,
				StringArrayParameterUtils.splitSelection(parameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS_THIRD)));

		JSONUtility.appendIntegerStringBeanList(sb,CONFIGURATION_PARAMETERS.TIME_INTERVALS, possibleTimeIntervals(locale));

		int selectedTimeInterval = parseInteger(parameters, CONFIGURATION_PARAMETERS.SELECTED_TIME_INTERVAL, TIME_INTERVAL.MONTH);
		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.SELECTED_TIME_INTERVAL, selectedTimeInterval);

		JSONUtility.appendIntegerStringBeanList(sb,CONFIGURATION_PARAMETERS.CALCULATION_MODES, possibleCalculationModes(locale));

		int selectedCalculationMode = parseInteger(parameters, CONFIGURATION_PARAMETERS.SELECTED_CALCULATION_MODE, CALCULATION_MODE.NEW);
		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.SELECTED_CALCULATION_MODE, new Integer(selectedCalculationMode));

		JSONUtility.appendBooleanValue(sb,CONFIGURATION_PARAMETERS.DISABLE_STATUS, new Boolean(selectedCalculationMode == CALCULATION_MODE.NEW));

		JSONUtility.appendIntegerValue(sb,CONFIGURATION_PARAMETERS.Y_AXE, parseInteger(parameters, CONFIGURATION_PARAMETERS.Y_AXE, DEFAULT_HEIGHT));

		int chartType = parseInteger(parameters, CONFIGURATION_PARAMETERS.SELECTED_CHART_TYPE, CHART_TYPE.LINE);
	    JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_CHART_TYPE, parseInteger(parameters, CONFIGURATION_PARAMETERS.SELECTED_CHART_TYPE, CHART_TYPE.LINE));

	    JSONUtility.appendIntegerValue(sb, CONFIGURATION_PARAMETERS.SELECTED_ISSUE_TYPE,  parseInteger(parameters, CONFIGURATION_PARAMETERS.ISSUE_TYPE, ISSUE_TYPE.GENERAL));
	    boolean userHasWiki = false;
	    if(user.getLicensedFeaturesMap().get("wiki") != null) {
	    	userHasWiki = user.getLicensedFeaturesMap().get("wiki");
	    }
	    JSONUtility.appendBooleanValue(sb, "userHasWiki",  userHasWiki);

        if (parameters.get(CONFIGURATION_PARAMETERS.FILLED) != null) {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.FILLED, true);
        } else {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.FILLED, false);
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUPING) != null) {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.GROUPING, true);
        } else {
            JSONUtility.appendBooleanValue(sb, CONFIGURATION_PARAMETERS.GROUPING, false);
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUP_FIRST_NAME) != null) {
            JSONUtility.appendStringValue(sb, CONFIGURATION_PARAMETERS.GROUP_FIRST_NAME, parameters.get(CONFIGURATION_PARAMETERS.GROUP_FIRST_NAME));
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUP_SECOND_NAME) != null) {
            JSONUtility.appendStringValue(sb, CONFIGURATION_PARAMETERS.GROUP_SECOND_NAME, parameters.get(CONFIGURATION_PARAMETERS.GROUP_SECOND_NAME));
        }

        if (parameters.get(CONFIGURATION_PARAMETERS.GROUP_THIRD_NAME) != null) {
            JSONUtility.appendStringValue(sb, CONFIGURATION_PARAMETERS.GROUP_THIRD_NAME, parameters.get(CONFIGURATION_PARAMETERS.GROUP_THIRD_NAME));
        }


		int prefWidth=880;
		int prefHeight=665;
		JSONUtility.appendIntegerValue(sb,"prefWidth",prefWidth);
		JSONUtility.appendIntegerValue(sb,"prefHeight",prefHeight);
		return sb.toString();
	}


    /**
     * Creates a JSON String, consisting of series data.
     * @throws TooManyItemsToLoadException
     */
    public String generateJSONResponse(Map configParameters) throws TooManyItemsToLoadException {
        Locale locale = (Locale)configParameters.get("locale");
        TPersonBean personBean = (TPersonBean)configParameters.get("personBean");

        String statuses = "";
        if(configParameters.get(CONFIGURATION_PARAMETERS.GROUPING) != null) {
        	statuses = (String)configParameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS) + ',' + (String)configParameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS_SECOND) + "," + (String)configParameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS_THIRD.toString());
        	if(statuses.indexOf("null") != -1) {
        		statuses = null;
        	}
        }else {
        	statuses = (String)configParameters.get(CONFIGURATION_PARAMETERS.SELECTED_STATUS);
        }

        List<Integer> statusIDs;
       	statusIDs = splitSelectedEntities(statuses);

        if (statusIDs.isEmpty()) {
            statusIDs = GeneralUtils.createListFromIntArr(GeneralUtils.getBeanIDs(StatusBL.loadAll()));
        }
        Date dateFrom = null;
        Date dateTo = null;
        Calendar calendar = Calendar.getInstance();
        if (BasePluginDashboardBL.parseIntegerValue(configParameters, TIMEPERIOD_PARAMETERS.SELECTED_PERIOD_TYPE, PERIOD_TYPE.FROM_TO)==PERIOD_TYPE.FROM_TO) {
            String strDateFrom = (String)configParameters.get(TIMEPERIOD_PARAMETERS.DATE_FROM);
            String strDateTo = (String)configParameters.get(TIMEPERIOD_PARAMETERS.DATE_TO);
            dateFrom = DateTimeUtils.getInstance().parseISODate(strDateFrom);
            dateTo = DateTimeUtils.getInstance().parseISODate(strDateTo);
        } else {
            int daysBefore = BasePluginDashboardBL.parseIntegerValue(configParameters, TIMEPERIOD_PARAMETERS.DAYS_BEFORE, 0);
            dateTo = calendar.getTime();
            if(daysBefore!=0){
                calendar.add(Calendar.DATE, -daysBefore);
                dateFrom = calendar.getTime();
            }
        }
        if (dateFrom!=null) {
            calendar.setTime(dateFrom);
            CalendarUtil.clearTime(calendar);
            //the beginning of fromDate day
            dateFrom = calendar.getTime();
        }
        if (dateTo!=null) {
            calendar.setTime(dateTo);
            CalendarUtil.clearTime(calendar);
            calendar.add(Calendar.DATE, 1);
            //the end of toDate day (actually the beginning of the day after toDate)
            dateTo = calendar.getTime();
        }

        int selectedTimeInterval = parseInteger(configParameters, CONFIGURATION_PARAMETERS.SELECTED_TIME_INTERVAL, TIME_INTERVAL.MONTH);
        int selectedCalculationMode = parseInteger(configParameters, CONFIGURATION_PARAMETERS.SELECTED_CALCULATION_MODE, CALCULATION_MODE.NEW);

        Integer selectedQueryID = parseInteger(configParameters, "selectedQueryID", 0);
        Integer selectedProjectOrRelease = parseInteger(configParameters, "selectedProjectOrRelease", 0);

        SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToEntityIDToNumbersMap =
                new TreeMap<Integer, SortedMap<Integer, Map<Integer, Integer>>>();
        Map<Integer, TStateBean> statusMap = null;

        configParameters.put(DataSourceDashboardBL.INCLUDE_CLOSED,Boolean.toString(true));
        int issueType = parseInteger(configParameters, CONFIGURATION_PARAMETERS.ISSUE_TYPE, ISSUE_TYPE.GENERAL);
        boolean loadOnlyDocuments;

        if(issueType == ISSUE_TYPE.GENERAL) {
        	loadOnlyDocuments = false;
        }else {
        	loadOnlyDocuments = true;
        }
//        List<TWorkItemBean> workItemBeans = DataSourceDashboardBL.getGeneralOrDocumentWorkItems(configParameters, personBean, locale, loadOnlyDocuments);
        int datasourceType = BasePluginDashboardBL.parseIntegerValue(configParameters, DataSourceDashboardBL.CONFIGURATION_PARAMETERS.SELECTED_DATASOURCE_TYPE, DataSourceDashboardBL.DATASOURCE_TYPE.PROJECT_RELEASE);
        ArrayList<Integer>projectOrReleaseIDs = new ArrayList<Integer>();
        boolean isRelease = true;
        boolean isQuery;
        if (datasourceType==DATASOURCE_TYPE.PROJECT_RELEASE) {
        	isQuery = false;
        	if(selectedProjectOrRelease < 0) {
        		isRelease = false;
        		selectedProjectOrRelease = selectedProjectOrRelease * (-1);
        	}
        	projectOrReleaseIDs.add(selectedProjectOrRelease);
		}else {
			isQuery = true;
        	projectOrReleaseIDs.add(selectedQueryID);
		}
        List<TWorkItemBean> workItemBeans = DataSourceDashboardBL.getGeneralOrDocumentWorkItems(projectOrReleaseIDs, datasourceType, personBean, locale, loadOnlyDocuments, isRelease, isQuery);
		String value;
        List<TStateBean>statusList;
        Set<String>statusOrder;
        switch (selectedCalculationMode) {
            case CALCULATION_MODE.NEW:
                yearToPeriodToEntityIDToNumbersMap = calculateNewWorkItems(workItemBeans,
                        dateFrom, dateTo, selectedTimeInterval);
                value = generateAxesValues(yearToPeriodToEntityIDToNumbersMap, null, selectedTimeInterval, false, null);
                return value;
            case CALCULATION_MODE.ACTUAL_SAMPLE:
                yearToPeriodToEntityIDToNumbersMap = calculateTotalInStatus(getWorkItemIDs(workItemBeans),
                        dateFrom, dateTo, statusIDs, selectedTimeInterval, locale);
                statusList = LocalizeUtil.localizeDropDownList(StatusBL.loadAll(), locale);
                statusMap =  GeneralUtils.createMapFromList(statusList);
                statusOrder = createStatusOrder(statusList);
                value = generateAxesValues(yearToPeriodToEntityIDToNumbersMap, statusMap, selectedTimeInterval, true, statusOrder);
                return value;
            case CALCULATION_MODE.ACTUAL_ACTIVITY:
                yearToPeriodToEntityIDToNumbersMap = calculateStatus(getWorkItemIDs(workItemBeans),
                        dateFrom, dateTo, statusIDs, selectedTimeInterval, locale);
                statusList = LocalizeUtil.localizeDropDownList(StatusBL.loadAll(), locale);
                statusMap = GeneralUtils.createMapFromList(statusList);
                statusOrder = createStatusOrder(statusList);
                value = generateAxesValues(yearToPeriodToEntityIDToNumbersMap, statusMap, selectedTimeInterval, false, statusOrder);
                return value;
            case CALCULATION_MODE.ACCUMULATED_ACTIVITY:
                yearToPeriodToEntityIDToNumbersMap = calculateStatus(getWorkItemIDs(workItemBeans),
                        dateFrom, dateTo, statusIDs, selectedTimeInterval, locale);
                statusList = LocalizeUtil.localizeDropDownList(StatusBL.loadAll(), locale);
                statusMap = GeneralUtils.createMapFromList(statusList);
                statusOrder = createStatusOrder(statusList);
                value = generateAxesValues(yearToPeriodToEntityIDToNumbersMap, statusMap, selectedTimeInterval, true, statusOrder);
                return value;
        }
        return null;
    }


    private static Set<String> createStatusOrder(List<TStateBean>statList) {
    	Set<String>statusOrder = new LinkedHashSet<String>();
    	for(TStateBean aBean : statList) {
    		statusOrder.add(aBean.getLabel().toString());
    	}
    	return statusOrder;
    }



	public static int[]  getWorkItemIDs(List workItemBeans) {
		return GeneralUtils.createIntArrFromIntegerArr(GeneralUtils.getBeanIDs(workItemBeans));
	}

	/**
	 * Computes the hierarchical data for new issues

	 * @return
	 */
	public static SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> calculateNewWorkItems(
			List<TWorkItemBean> workItemBeans, Date dateFrom, Date dateTo,	int selectedTimeInterval) {
		SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToProjectIDToWorkItemNumbersMap =
			new TreeMap<Integer, SortedMap<Integer, Map<Integer, Integer>>>();

		if (workItemBeans==null || workItemBeans.isEmpty()) {
			LOGGER.debug("No workItems in datasource");
			return yearToPeriodToProjectIDToWorkItemNumbersMap;
		}
		SortedMap<Integer, SortedMap<Integer, List<TWorkItemBean>>> periodNewWorkItems = getNewWorkItemsMap(workItemBeans, selectedTimeInterval, dateFrom, dateTo);
		List entityList = new ArrayList();
		//for new WorkItems we have a single entity (a single graphic), hardcoded with Integer(0)
		//Integer hardCodedentityID = Integer.valueOf(0);
		entityList.add(ENTITY_PLACEHOLDER);

		Iterator<Integer> yearIterator = periodNewWorkItems.keySet().iterator();
		while (yearIterator.hasNext()) {
			Integer year = (Integer) yearIterator.next();
			SortedMap<Integer, List<TWorkItemBean>> intervalToStatusChangeBeans = periodNewWorkItems.get(year);
			Iterator<Integer> periodIterator = intervalToStatusChangeBeans.keySet().iterator();
			while (periodIterator.hasNext()) {
				Integer period = (Integer) periodIterator.next();
				List<TWorkItemBean> workItemBeansForInterval = intervalToStatusChangeBeans.get(period);
				if (workItemBeansForInterval!=null) {
					Iterator<TWorkItemBean> workItemBeansIterator = workItemBeansForInterval.iterator();
					while (workItemBeansIterator.hasNext()) {
						TWorkItemBean workItemBean = (TWorkItemBean) workItemBeansIterator.next();
						if (workItemBean!=null) {
								setCount(yearToPeriodToProjectIDToWorkItemNumbersMap,
										year, period, ENTITY_PLACEHOLDER, 1);
						}
					}
				}
			}
		}
		addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, yearToPeriodToProjectIDToWorkItemNumbersMap, entityList);
		//addTimeSeries(timeSeriesCollection, yearToPeriodToProjectIDToWorkItemNumbersMap, null, selectedTimeInterval, accumulated);
		return yearToPeriodToProjectIDToWorkItemNumbersMap;
	}

	/**
	 * Computes the hierarchical data for status changes
	 *
	 * @return
	 */
	public static SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> calculateTotalInStatus(
			int[] workItemIDs, Date dateFrom, Date dateTo,
			List<Integer> statusIDs, int selectedTimeInterval, Locale locale) {
		SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToStatusIDToStatusNumbersMap =
			new TreeMap<Integer, SortedMap<Integer, Map<Integer, Integer>>>();

		if (statusIDs!=null && statusIDs.isEmpty()) {
			LOGGER.warn("No status specified");
			return yearToPeriodToStatusIDToStatusNumbersMap;
		}
		Set<Integer> statusIDsSet = GeneralUtils.createIntegerSetFromIntegerList(statusIDs);
		if (workItemIDs == null ||workItemIDs.length==0) {
			// LOGGER.warn("No issues satisfy the filtering condition (read right revoked, project/release deleted?)");
			return yearToPeriodToStatusIDToStatusNumbersMap;
		}

		Map<Integer, Integer> statusForWorkItems = new HashMap<Integer, Integer>();
		List<HistorySelectValues> historySelectValuesBefore = null;
		if (dateFrom!=null) {
			//get all status changes till the beginning of the reporting period
			//include all statuses (not just the selected ones)
			//because we are interested only in the status at the end of each period
			historySelectValuesBefore = HistoryTransactionBL.getByWorkItemsFieldNewValuesDates(
				workItemIDs, SystemFields.INTEGER_STATE, null, null, dateFrom);
		}
		//get all status changes for the reporting period
		//include all statuses (not just the selected ones)
		//because we are interested only in the status at the end of each period
		List<HistorySelectValues> historySelectValuesReportingPeriod = HistoryTransactionBL.getByWorkItemsFieldNewValuesDates(
				workItemIDs, SystemFields.INTEGER_STATE, null, dateFrom, dateTo);
		SortedMap<Integer, SortedMap<Integer, List<HistorySelectValues>>> periodStatusChangesReportingPeriod =
			getStatusChangesMap(historySelectValuesReportingPeriod, selectedTimeInterval, true/*, statusIDs*/);

		Integer year = null;
		Integer period = null;
		Iterator yearIterator;

		//calculate the values for the beginning of the first reporting period
		if (historySelectValuesBefore!=null) {
			//get the first year and period
			if (dateFrom!=null) {
				//explicit dateFrom specified by user
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateFrom);
				year = Integer.valueOf(calendar.get(Calendar.YEAR));
				int calendarInterval = getCalendarInterval(selectedTimeInterval);
				period = Integer.valueOf(calendar.get(calendarInterval));
			} else {
				//no explicit dateFrom specified by the user, get the first found entry in the history
				yearIterator = periodStatusChangesReportingPeriod.keySet().iterator();
				if (yearIterator.hasNext()) {
					year = (Integer) yearIterator.next();
					SortedMap<Integer, List<HistorySelectValues>> intervalToStatusChangeBeans = periodStatusChangesReportingPeriod.get(year);
					Iterator<Integer> periodIterator = intervalToStatusChangeBeans.keySet().iterator();
					period = periodIterator.next();
				}
			}

			if (year==null || period==null) {
				//nothing found
				return yearToPeriodToStatusIDToStatusNumbersMap;
			}

			Iterator<HistorySelectValues> iterator = historySelectValuesBefore.iterator();
			while (iterator.hasNext()) {
				//count the workItems in status till the beginning of the reporting period
				HistorySelectValues historySelectValues = iterator.next();
				Integer workItemID = historySelectValues.getWorkItemID();
				Integer statusID = historySelectValues.getNewValue();
				if (statusForWorkItems.get(workItemID)==null) {
					//take into account only the last stateChange for the workItem
					statusForWorkItems.put(workItemID, statusID);
					if (statusIDsSet.contains(statusID)) {
						//count only if selected status
						setCount(yearToPeriodToStatusIDToStatusNumbersMap, year, period, statusID, 1);
					}
				}
			}
		}
		yearIterator = periodStatusChangesReportingPeriod.keySet().iterator();
		while (yearIterator.hasNext()) {
			year = (Integer) yearIterator.next();
			SortedMap intervalToStatusChangeBeans = periodStatusChangesReportingPeriod.get(year);
			Iterator<Integer> periodIterator = intervalToStatusChangeBeans.keySet().iterator();
			while (periodIterator.hasNext()) {
				period = periodIterator.next();
				List statusChangeBeansForInterval = (List) intervalToStatusChangeBeans.get(period);
				if (statusChangeBeansForInterval!=null) {
					Iterator statusChangeBeansIterator = statusChangeBeansForInterval.iterator();
					while (statusChangeBeansIterator.hasNext()) {
						HistorySelectValues historySelectValues = (HistorySelectValues) statusChangeBeansIterator.next();
						Integer workItemID = historySelectValues.getWorkItemID();
						Integer nextStatusID = historySelectValues.getNewValue();
						Integer previousStatus = statusForWorkItems.get(workItemID);
						if (previousStatus==null) {
							//probably the item was created in the actual period
							statusForWorkItems.put(workItemID, nextStatusID);
							if (statusIDsSet.contains(nextStatusID)) {
								setCount(yearToPeriodToStatusIDToStatusNumbersMap,
									year, period, nextStatusID, 1);
							}
						} else {
							if (!previousStatus.equals(nextStatusID)) {
								statusForWorkItems.put(workItemID, nextStatusID);
								//add as new status
								if (statusIDsSet.contains(nextStatusID)) {
									setCount(yearToPeriodToStatusIDToStatusNumbersMap,
										year, period, nextStatusID, 1);
								}
								//decrement the count for the previous status
								if (statusIDsSet.contains(previousStatus)) {
									setCount(yearToPeriodToStatusIDToStatusNumbersMap,
										year, period, previousStatus, -1);
								}
							}
						}
					}
				}
			}
		}
		addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, yearToPeriodToStatusIDToStatusNumbersMap, statusIDs);
		//addTimeSeries(timeSeriesCollection, yearToPeriodToStatusIDToStatusNumbersMap, statusMap, selectedTimeInterval, true);
		return yearToPeriodToStatusIDToStatusNumbersMap;
	}

	/**
	 * Computes the hierarchical data for status changes
	 * @return
	 */
	public static SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> calculateStatus(int[] workItemIDs,
			Date dateFrom, Date dateTo, List<Integer> statusIDs, int selectedTimeInterval, Locale locale) {
		SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToStatusIDToStatusNumbersMap =
			new TreeMap<Integer, SortedMap<Integer, Map<Integer, Integer>>>();

		if (statusIDs!=null && statusIDs.isEmpty()) {
			LOGGER.debug("No status specified");
			return yearToPeriodToStatusIDToStatusNumbersMap;
		}
		if (workItemIDs == null ||workItemIDs.length==0) {
			// LOGGER.warn("No issues satisfy the filtering condition (read right revoked, project/release deleted?)");
			return yearToPeriodToStatusIDToStatusNumbersMap;
		}
		List<HistorySelectValues> historySelectValuesList = HistoryTransactionBL.getByWorkItemsFieldNewValuesDates(
				workItemIDs, SystemFields.INTEGER_STATE, statusIDs, dateFrom, dateTo);
		SortedMap<Integer, SortedMap<Integer, List<HistorySelectValues>>> periodStatusChanges = getStatusChangesMap(historySelectValuesList, selectedTimeInterval, false/*, statusIDs*/);
		Iterator<Integer> yearIterator = periodStatusChanges.keySet().iterator();
		while (yearIterator.hasNext()) {
			Integer year = yearIterator.next();
			SortedMap<Integer, List<HistorySelectValues>> intervalToStatusChangeBeans = periodStatusChanges.get(year);
			Iterator<Integer> periodIterator = intervalToStatusChangeBeans.keySet().iterator();
			while (periodIterator.hasNext()) {
				Integer period = periodIterator.next();
				List<HistorySelectValues> statusChangeBeansForInterval = intervalToStatusChangeBeans.get(period);
				if (statusChangeBeansForInterval!=null) {
					Iterator statusChangeBeansIterator = statusChangeBeansForInterval.iterator();
					while (statusChangeBeansIterator.hasNext()) {
						HistorySelectValues stateChangeBean = (HistorySelectValues) statusChangeBeansIterator.next();
							Integer statusID = stateChangeBean.getNewValue();
							setCount(yearToPeriodToStatusIDToStatusNumbersMap,
									year, period, statusID, 1);
					}
				}
			}
		}
		addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, yearToPeriodToStatusIDToStatusNumbersMap, statusIDs);
		//addTimeSeries(timeSeriesCollection, yearToPeriodToStatusIDToStatusNumbersMap, statusMap, selectedTimeInterval, accumulated);
		return yearToPeriodToStatusIDToStatusNumbersMap;
	}

	/**
	 * Set 0 values for the empty time intervals
	 * @param dateFrom
	 * @param dateTo
	 * @param selectedTimeInterval
	 * @param yearToPeriodToEntityNumbersMap
	 * @param entityIDs
	 */
	private static void addZerosForEmptyIntervals(Date dateFrom, Date dateTo, int selectedTimeInterval,
			SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToEntityNumbersMap, List entityIDs) {
		if (entityIDs==null || entityIDs.isEmpty()) {
			return;
		}
		int calendarInterval = getCalendarInterval(selectedTimeInterval);
		Calendar calendarFrom = StatusOverTimeDatasource.calculateDateFrom(dateFrom, (SortedMap)yearToPeriodToEntityNumbersMap, calendarInterval);
		if (calendarFrom==null) {
			return;
		}
		Calendar calendarTo = StatusOverTimeDatasource.calculateDateTo(dateTo, (SortedMap)yearToPeriodToEntityNumbersMap, calendarInterval);
		if (calendarTo==null) {
			return;
		}
		//previous-next issue: to avoid adding the first week of the new year as the first week of the old year,
		//because it can be that the year is the old one but the last days of the year belong to the first week of the next year
		//and that would add an entry with the first week of the old year
		int previousYearValue = calendarFrom.get(Calendar.YEAR);
		int nextYearValue = previousYearValue;
		int previousCalendarIntervalValue = calendarFrom.get(calendarInterval);
		int nextCalendarIntervalValue = previousCalendarIntervalValue;
		boolean jumpYearFirstTime = false;
		if (selectedTimeInterval==TIME_INTERVAL.WEEK && calendarFrom.get(Calendar.MONTH)==11 && nextCalendarIntervalValue==1 ) {
			jumpYearFirstTime = true;
		}
		while (calendarFrom.before(calendarTo)) {
			if ((nextCalendarIntervalValue<previousCalendarIntervalValue && nextYearValue==previousYearValue) || jumpYearFirstTime) {
				nextYearValue +=1;
			}
			jumpYearFirstTime = false;
			addZerosForInterval(new Integer(nextYearValue),
					new Integer(nextCalendarIntervalValue), yearToPeriodToEntityNumbersMap, entityIDs);
			previousYearValue = nextYearValue;
			previousCalendarIntervalValue = nextCalendarIntervalValue;
			calendarFrom.add(calendarInterval, 1);
			nextYearValue = calendarFrom.get(Calendar.YEAR);
			nextCalendarIntervalValue = calendarFrom.get(calendarInterval);
		}
	}

	/**
	 * Set 0 value for a time interval
	 * @param year
	 * @param periode
	 * @param yearToPeriodToEntityNumbersMap
	 * @param entityIDs
	 */
	private static void addZerosForInterval(Integer year, Integer periode, SortedMap yearToPeriodToEntityNumbersMap, List entityIDs) {
		Iterator iterator = entityIDs.iterator();
		while (iterator.hasNext()) {
			Integer entityID = (Integer) iterator.next();
			setCount(yearToPeriodToEntityNumbersMap,
					year, periode, entityID, 0);
		}
	}


    /**
     * Add the time series to the timeSeriesCollection
     * SortedMap at first and second level (year and period)
     * (Sorted because the accumulated should be calculated in the right order)
     */
    private static String generateAxesValues(
                                           SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToEntityIDToWorkItemNumbersMap,
                                           Map entityMap, int selectedTimeInterval, boolean accumulated, Set<String>stateNameOrder) {

        Map timeSeriesMap = new HashMap();
        Map accumulatedMap = new HashMap();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Map<String, String>valuesMap;
        Iterator yearIterator = yearToPeriodToEntityIDToWorkItemNumbersMap.keySet().iterator();
        while (yearIterator.hasNext()) {
            Integer year = (Integer) yearIterator.next();
            SortedMap<Integer, Map<Integer, Integer>> intervalToStatusChangeBeans = yearToPeriodToEntityIDToWorkItemNumbersMap.get(year);
            Iterator periodIterator = intervalToStatusChangeBeans.keySet().iterator();
            while (periodIterator.hasNext()) {
                Integer period = (Integer) periodIterator.next();
                Map<Integer, Integer> entityIDToWorkItemNumbersMap = intervalToStatusChangeBeans.get(period);
                if (entityIDToWorkItemNumbersMap!=null) {
                    Iterator entityIDIterator = entityIDToWorkItemNumbersMap.keySet().iterator();
                    sb.append("{");
                    valuesMap = new HashMap<String, String>();
                    JSONUtility.appendStringValue(sb, "date", createRegularTimePeriodForJSON(period.intValue(), year.intValue(), selectedTimeInterval));
                    while (entityIDIterator.hasNext()) {
                        Integer entityID = (Integer) entityIDIterator.next();
                        Integer numberOfStates = entityIDToWorkItemNumbersMap.get(entityID);
                        if (numberOfStates!=null) {
                            String sb1 = (String) timeSeriesMap.get(entityID);
                            Integer accumuletedValueForEntity = (Integer)accumulatedMap.get(entityID);
                            String label = "";
                            if (sb1==null) {
                                ILabelBean iLabelBean = null;
                                if (entityMap!=null) {
                                    iLabelBean = (ILabelBean)entityMap.get(entityID);
                                }

                                if (iLabelBean!=null) {
                                    label = iLabelBean.getLabel();
                                }
                               String axeValue = new String();
                               timeSeriesMap.put(entityID, axeValue);
                               accumulatedMap.put(entityID, Integer.valueOf(0));
                               accumuletedValueForEntity = (Integer)accumulatedMap.get(entityID);
                            }

                            Integer timeSeriesValue;
                            if (accumulated) {
                                accumulatedMap.put(entityID, Integer.valueOf(accumuletedValueForEntity.intValue()+numberOfStates.intValue()));
                                timeSeriesValue = (Integer)accumulatedMap.get(entityID);
                            } else {
                                timeSeriesValue = numberOfStates;
                            }
                            String status = "";
                            ILabelBean iLabelBeanJSON = null;
                            if (entityMap!=null) {
                                iLabelBeanJSON = (ILabelBean)entityMap.get(entityID);
                            }
                            if (iLabelBeanJSON != null) {
                                status = iLabelBeanJSON.getLabel();
                            }
                            if (status.equals("")) {
                                status = "opened";
                            }
//                            JSONUtility.appendStringValue(sb, status, timeSeriesValue.toString(), true);
//                            sb.append(",");
                            valuesMap.put(status, timeSeriesValue.toString());
                            timeSeriesMap.put(entityID, sb.toString());
                        }
                    }

                    if(entityMap != null) {
                    	for(String stateName : stateNameOrder) {
                    		if(valuesMap.get(stateName) != null) {
                    			JSONUtility.appendStringValue(sb, stateName, valuesMap.get(stateName), true);
                    			sb.append(",");
                    		}
                    	}
                    }else {
                    	if(valuesMap.get("opened") != null) {
                			JSONUtility.appendStringValue(sb, "opened", valuesMap.get("opened"), true);
                			sb.append(",");
                		}
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append("},");
                }
            }
        }
        sb.deleteCharAt(sb.length()-1);
        if (sb.length() != 0) {
            sb.append("]");
        }else {
            sb.append("[{}]");
        }
        return  sb.toString();
    }


   /**
	* Increment the value in the corresponding map
	* @param yearToPeriodToEntityIDToEntityCountMap
	* @param year
	* @param period
	* @param entityID
	*/
	private static void setCount(SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToEntityIDToEntityCountMap,
			Integer year, Integer period, Integer entityID, int value) {
		if (yearToPeriodToEntityIDToEntityCountMap==null ||
				year==null || period==null || entityID==null) {
			return;
		}
		SortedMap<Integer, Map<Integer, Integer>> periodToEntityIDToWorkItemNumbersMap = yearToPeriodToEntityIDToEntityCountMap.get(year);
		if (periodToEntityIDToWorkItemNumbersMap==null) {
			yearToPeriodToEntityIDToEntityCountMap.put(year, new TreeMap());
			periodToEntityIDToWorkItemNumbersMap = yearToPeriodToEntityIDToEntityCountMap.get(year);
		}
		Map<Integer, Integer> entityIDToWorkItemNumbersMap = periodToEntityIDToWorkItemNumbersMap.get(period);
		if (entityIDToWorkItemNumbersMap==null) {
			periodToEntityIDToWorkItemNumbersMap.put(period, new HashMap());
			entityIDToWorkItemNumbersMap = periodToEntityIDToWorkItemNumbersMap.get(period);
		}
		Integer previousValue = entityIDToWorkItemNumbersMap.get(entityID);
		if (previousValue==null) {
			entityIDToWorkItemNumbersMap.put(entityID, new Integer(value));
		} else {
			entityIDToWorkItemNumbersMap.put(entityID, new Integer(previousValue.intValue()+value));
		}
	}

	/**
	* Create a map of hierarchical data with TStateChangeBeans for the workItems in the periods
	* -	key: year
	* -	value: map
	* 			-	key: period
	* 			-	Set of HistorySelectValues
	* @param statusChangeList
	* @param timeInterval
	* @param sample sample (only last from the period) or action (all statuses during a period)
	* @return
	*/
	private static SortedMap<Integer, SortedMap<Integer, List<HistorySelectValues>>> getStatusChangesMap(
			List<HistorySelectValues> statusChangeList, int timeInterval, boolean sample) {
		SortedMap<Integer, SortedMap<Integer, List<HistorySelectValues>>> yearToIntervalToStatusChangeBeans =
			new TreeMap<Integer, SortedMap<Integer, List<HistorySelectValues>>>();
		Map yearToIntervalToWorkItemIDs = new HashMap();
		int yearValue;
		int intervalValue;
		Integer workItemID;
		if (statusChangeList!=null && !statusChangeList.isEmpty()) {
			Calendar calendarLastModified = Calendar.getInstance();
			//get them in the reverse order (the later first)
			Iterator iterator = statusChangeList.iterator();
			int calendarInterval = getCalendarInterval(timeInterval);
			Set workItemsIDsForInterval = new HashSet();
			while (iterator.hasNext()) {
				HistorySelectValues stateChangeBean = (HistorySelectValues) iterator.next();
				workItemID = stateChangeBean.getWorkItemID();
				calendarLastModified.setTime(stateChangeBean.getLastEdit());
				yearValue = calendarLastModified.get(Calendar.YEAR);
				intervalValue = calendarLastModified.get(calendarInterval);
				if (Calendar.WEEK_OF_YEAR==calendarInterval) {
					//avoid adding the first week of the new year as the first week of the old year,
					//because it can be that the year is the old one but the last days of the year belong to the first week of the next year
					//and that would add an entry with the first week of the old year
					int monthValue = calendarLastModified.get(Calendar.MONTH);
					if (monthValue>=11 && intervalValue==1) {
						yearValue = yearValue+1;
					}
				}
				if (sample) {
					Map intervalToWorkItems = (Map)yearToIntervalToWorkItemIDs.get(new Integer(yearValue));
					if (intervalToWorkItems==null) {
						yearToIntervalToWorkItemIDs.put(new Integer(yearValue), new HashMap());
						intervalToWorkItems = (Map)yearToIntervalToWorkItemIDs.get(new Integer(yearValue));
					}
					workItemsIDsForInterval = (Set)intervalToWorkItems.get(new Integer(intervalValue));
					if (workItemsIDsForInterval==null) {
						intervalToWorkItems.put(new Integer(intervalValue), new HashSet());
						workItemsIDsForInterval = (Set)intervalToWorkItems.get(new Integer(intervalValue));
					}
				}
				//add the stateChangeBean if:
					//1. if not sample add all stateChangeBeans
					//2. if sample: add only one (the latest i.e. the first one from the list) for each period
				if (!sample || !workItemsIDsForInterval.contains(workItemID)) {
					if (sample) {
						//add workItemID to forbid adding the same workItemID again for the same period
						workItemsIDsForInterval.add(workItemID);
						//the last state change bean for the period for the workItem is
						//a change to a status which is not selected to be shown, so simply neglect it
						/*if (!statusIDsSet.contains(stateChangeBean.getNewValue())) {
							continue;
						}*/
					}
					//workItemIDsSet.add(workItemID);
					SortedMap<Integer, List<HistorySelectValues>> intervalToStatusChangeBeans = yearToIntervalToStatusChangeBeans.get(new Integer(yearValue));
					if (intervalToStatusChangeBeans==null) {
						yearToIntervalToStatusChangeBeans.put(new Integer(yearValue), new TreeMap<Integer, List<HistorySelectValues>>());
						intervalToStatusChangeBeans = yearToIntervalToStatusChangeBeans.get(new Integer(yearValue));
					}
					List<HistorySelectValues> statusChangeBeansForInterval = intervalToStatusChangeBeans.get(new Integer(intervalValue));
					if (statusChangeBeansForInterval==null) {
						intervalToStatusChangeBeans.put(new Integer(intervalValue), new ArrayList());
						statusChangeBeansForInterval = intervalToStatusChangeBeans.get(new Integer(intervalValue));
					}
					statusChangeBeansForInterval.add(stateChangeBean);
				}
			}
		}
		return yearToIntervalToStatusChangeBeans;
	}

	/**
	* Create a map of hierarchical data with TWorkItemBeans in the periods
	* -	key: year
	* -	value: map
	* 			-	key: period
	* 			-	Set of TStateChangeBeans, one for each workItem
	* @param timeInterval
	* @return
	*/
	private static SortedMap<Integer, SortedMap<Integer, List<TWorkItemBean>>> getNewWorkItemsMap(List workItemList, int timeInterval, Date dateFrom, Date dateTo) {
		SortedMap<Integer, SortedMap<Integer, List<TWorkItemBean>>> yearToIntervalToWorkItemBeans = new TreeMap();
		int yearValue;
		int intervalValue;
		if (workItemList!=null) {
			Calendar calendarCreated = Calendar.getInstance();
			Iterator iterator = workItemList.iterator();
			int calendarInterval = getCalendarInterval(timeInterval);
			while (iterator.hasNext()) {
				TWorkItemBean workItemBean = (TWorkItemBean)iterator.next();
				Date createDate = workItemBean.getCreated();
				if (createDate==null) {
					continue;
				}
				if (dateFrom!=null && dateFrom.after(createDate) ||
						dateTo!=null && dateTo.before(createDate)) {
					continue;
				}
				calendarCreated.setTime(workItemBean.getCreated());
				yearValue = calendarCreated.get(Calendar.YEAR);
				intervalValue = calendarCreated.get(calendarInterval);
				if (Calendar.WEEK_OF_YEAR==calendarInterval) {
					//avoid adding the first week of the new year as the first week of the old year,
					//because it can be that the year is the old one but the last days of the year belong to the first week of the next year
					//and that would add an entry with the first week of the old year
					int monthValue = calendarCreated.get(Calendar.MONTH);
					if (monthValue>=11 && intervalValue==1) {
						yearValue = yearValue+1;
					}
				}
				SortedMap<Integer, List<TWorkItemBean>> intervalToWorkItemBeans =
					yearToIntervalToWorkItemBeans.get(new Integer(yearValue));
				if (intervalToWorkItemBeans==null) {
					yearToIntervalToWorkItemBeans.put(new Integer(yearValue), new TreeMap());
					intervalToWorkItemBeans = yearToIntervalToWorkItemBeans.get(new Integer(yearValue));
				}
				List<TWorkItemBean> workItemBeansForInterval = intervalToWorkItemBeans.get(new Integer(intervalValue));
				if (workItemBeansForInterval==null) {
					intervalToWorkItemBeans.put(new Integer(intervalValue), new ArrayList());
					workItemBeansForInterval = intervalToWorkItemBeans.get(new Integer(intervalValue));
				}
				workItemBeansForInterval.add(workItemBean);
			}
		}
		return yearToIntervalToWorkItemBeans;
	}

	/**
	 * Get the corresponding calendar constant
	 * @param timeInterval
	 * @return
	 */
	private static int  getCalendarInterval(int timeInterval) {
		switch (timeInterval) {
		case TIME_INTERVAL.DAY:
			return Calendar.DAY_OF_YEAR;
		case TIME_INTERVAL.WEEK:
			return Calendar.WEEK_OF_YEAR;
		default:
			return Calendar.MONTH;
		}
	}


    /*
      Transforming the period (into date) for the JSON response.
     */
    public static String createRegularTimePeriodForJSON (int period, int year, int timeInterval) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat;
        switch (timeInterval) {
            case TIME_INTERVAL.DAY:
                calendar.setLenient(true);
                calendar.clone();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.DAY_OF_YEAR, period);
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.format(calendar.getTime());
            case TIME_INTERVAL.WEEK:
                return period + "/" + year;
            default:
                period++;
                if (period < 10) {
                    return year + "-0" + period;
                }else {
                    return year + "-" + period;
                }
        }
    }

	/**
	 * Transform the string with comma separated values
	 * into a list of integers for further processing
	 * @param entitySelection
	 * @return
	 */
	private static List<Integer> splitSelectedEntities(String entitySelection) {
		List<Integer> entityIDsList = new ArrayList<Integer>();
		if (entitySelection == null || "".equals(entitySelection)) {
			return entityIDsList;
		}
		String[] entityArr = entitySelection.split(",");
		if (entityArr == null || entityArr.length == 0) {
			return entityIDsList;
		}
		for (int i=0; i < entityArr.length; ++i) {
			try {
				if (entityArr[i]!=null) {
					entityIDsList.add(Integer.valueOf(entityArr[i].trim()));
				}
			} catch (Exception e) {
				LOGGER.warn("The " + i + "'th component " +  entityArr[i] + " can't be converted to Integer " + e.getMessage(), e);
			}
		}
		return entityIDsList;
	}


	/**
	 * The suffix for the title and Y-Axes label resource keys
	 * @param selectedCalculationMode
	 * @return
	 */
	public static String getCalculationModeSuffix(int selectedCalculationMode) {
		switch (selectedCalculationMode) {
		case CALCULATION_MODE.NEW:
			return "new";
		case CALCULATION_MODE.ACTUAL_SAMPLE:
			return "actualSample";
		case CALCULATION_MODE.ACTUAL_ACTIVITY:
			return "actualActivity";
		case CALCULATION_MODE.ACCUMULATED_ACTIVITY:
			return "accumulatedActivity";
		}
		return null;
	}

	/**
	 * Localized list of possible time intervals
	 * @param locale
	 * @return
	 */
	private List<IntegerStringBean> possibleTimeIntervals(Locale locale) {
		List<IntegerStringBean> timeIntervals = new ArrayList<IntegerStringBean>();
		timeIntervals.add(new IntegerStringBean(
				localizeText("statusOverTime.timeInterval.day", locale),
				new Integer(TIME_INTERVAL.DAY)));
		timeIntervals.add(new IntegerStringBean(
				localizeText("statusOverTime.timeInterval.week", locale),
				new Integer(TIME_INTERVAL.WEEK)));
		timeIntervals.add(new IntegerStringBean(
				localizeText("statusOverTime.timeInterval.month", locale),
				new Integer(TIME_INTERVAL.MONTH)));
		return timeIntervals;
	}

	/**
	 * Localized list of possible calculation modes
	 * @param locale
	 * @return
	 */
	private List<IntegerStringBean> possibleCalculationModes(Locale locale) {
		List<IntegerStringBean> timeIntervals = new ArrayList<IntegerStringBean>();
		timeIntervals.add(new IntegerStringBean(
				localizeText("statusOverTime.calculationMode." +
						getCalculationModeSuffix(CALCULATION_MODE.NEW), locale),
				new Integer(CALCULATION_MODE.NEW)));
		timeIntervals.add(new IntegerStringBean(
				localizeText("statusOverTime.calculationMode." +
						getCalculationModeSuffix(CALCULATION_MODE.ACTUAL_SAMPLE), locale),
				new Integer(CALCULATION_MODE.ACTUAL_SAMPLE)));
		timeIntervals.add(new IntegerStringBean(
				localizeText("statusOverTime.calculationMode." +
						getCalculationModeSuffix(CALCULATION_MODE.ACTUAL_ACTIVITY), locale),
				new Integer(CALCULATION_MODE.ACTUAL_ACTIVITY)));
		timeIntervals.add(new IntegerStringBean(
				localizeText("statusOverTime.calculationMode." +
						getCalculationModeSuffix(CALCULATION_MODE.ACCUMULATED_ACTIVITY), locale),
				new Integer(CALCULATION_MODE.ACCUMULATED_ACTIVITY)));
		return timeIntervals;
	}

}
