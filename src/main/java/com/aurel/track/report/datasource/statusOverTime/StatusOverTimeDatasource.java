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

package com.aurel.track.report.datasource.statusOverTime;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Document;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.dashboard.StatusOverTimeGraph;
import com.aurel.track.report.dashboard.StatusOverTimeGraph.CALCULATION_MODE;
import com.aurel.track.report.datasource.TimeIntervalWithStatusDatasource;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;
import java.util.Arrays;

public class StatusOverTimeDatasource extends TimeIntervalWithStatusDatasource {
	
	public static interface STATUS_OVER_TIME_PARAMETER_NAME {
		/**
		 * Calculation modes
		 */
		public static String CALCULATION_MODE = "calculationMode";
		public static String CALCULATION_MODE_OPTIONS = CALCULATION_MODE + PARAMETER_NAME.OPTION_SUFFIX;
		public static String CALCULATION_MODE_NAME_FIELD = CALCULATION_MODE + PARAMETER_NAME.NAME_SUFFIX;
		public static String CALCULATION_MODE_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + CALCULATION_MODE;
	}
	
	public static int GROUPED_FIRST_ID = -5;
	public static int GROUPED_SECOND_ID = -4;
	public static int GROUPED_THIRD_ID = -3;
	public static int GROUPED_FOURTH_ID = -2;
	public static int GROUPED_FIFTH_ID = -1;
	
	/**
	 * Gets the data source object (a Document object in this case) retrieved using the parameters settings
	 * @param parameters
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException 
	 */
	public Object getDatasource(Map<String, String[]> parameters, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		Map<String, Object> savedParamsMap = new HashMap<String, Object>();
		String paramSettings = loadParamObjectsAndPropertyStringsAndFromStringArrParams(parameters, locale, savedParamsMap);
		List<Integer> statusIDs;
		Integer[] statusIDsArr = (Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES);
		Boolean grouping = (Boolean)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING);
		
		Map<Integer, String>groupingIDsToLabels = new HashMap<Integer, String>();
		Set<Integer> statusIDsFirstSet = new HashSet<Integer>();
		Set<Integer> statusIDsSecondSet = new HashSet<Integer>(); 
		Set<Integer> statusIDsThirdSet = new HashSet<Integer>();
		Set<Integer> statusIDsFourthSet = new HashSet<Integer>();
		Set<Integer> statusIDsFifthSet = new HashSet<Integer>();
		if(grouping) {
			if(statusIDsArr != null) {
				List<Integer> statusIDsFirstList = Arrays.asList(statusIDsArr);
				statusIDsFirstSet = new HashSet<Integer>(statusIDsFirstList);
			}
			
			Integer[] statusIDsSecondArr = (Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_SECOND);
			if(statusIDsSecondArr != null) {
				
				List<Integer> statusIDsSecondList = Arrays.asList(statusIDsSecondArr);
				statusIDsSecondSet = new HashSet<Integer>(statusIDsSecondList);
			}
			
			Integer[] statusIDsThirdArr = (Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_THIRD);
			if(statusIDsThirdArr != null) {
				List<Integer> statusIDsThirdList = Arrays.asList(statusIDsThirdArr);
				statusIDsThirdSet = new HashSet<Integer>(statusIDsThirdList);
			}
			
			Integer[] statusIDsFourthArr = (Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FOURTH);
			if(statusIDsFourthArr != null) {
				List<Integer> statusIDsFourthList = Arrays.asList(statusIDsFourthArr);
				statusIDsFourthSet = new HashSet<Integer>(statusIDsFourthList);
			}
			
			Integer[] statusIDsFifthArr = (Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES_FIFTH);
			if(statusIDsFifthArr != null) {
				List<Integer> statusIDsFifthList = Arrays.asList(statusIDsFifthArr);
				statusIDsFifthSet = new HashSet<Integer>(statusIDsFifthList);
			}
			
			
			String groupingFirstLabel = (String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIRST_LABEL);
			String groupingSecondLabel = (String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_SECOND_LABEL);
			String groupingThirdLabel = (String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_THIRD_LABEL);
			String groupingFourthLabel = (String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FOURTH_LABEL);
			String groupingFifthLabel = (String)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.GROUPING_FIFTH_LABEL);
			groupingIDsToLabels = new HashMap<Integer, String>();
			
			groupingIDsToLabels.put(GROUPED_FIRST_ID, groupingFirstLabel);
			groupingIDsToLabels.put(GROUPED_SECOND_ID, groupingSecondLabel);
			groupingIDsToLabels.put(GROUPED_THIRD_ID, groupingThirdLabel);
			groupingIDsToLabels.put(GROUPED_FOURTH_ID, groupingFourthLabel);
			groupingIDsToLabels.put(GROUPED_FIFTH_ID, groupingFifthLabel);

		}
		
		//TODO title?
		String title = null;//StringArrayParameterUtils.getStringValue(configParameters, CONFIGURATION_PARAMETERS.TITLE);
		Map<Integer, Boolean> usedGroupedValues = new HashMap<Integer, Boolean>();
		if (statusIDsArr==null || statusIDsArr.length==0) {
			statusIDs = GeneralUtils.createIntegerListFromBeanList(StatusBL.loadAll());
		} else {
			if(grouping) {
				Set<Integer>allNeededStatuses = new HashSet<Integer>();
				allNeededStatuses.addAll(statusIDsFirstSet);
				usedGroupedValues.put(GROUPED_FIRST_ID, statusIDsFirstSet.size() > 0 ? true : false);
				
				allNeededStatuses.addAll(statusIDsSecondSet);
				usedGroupedValues.put(GROUPED_SECOND_ID, statusIDsSecondSet.size() > 0 ? true : false);
				
				allNeededStatuses.addAll(statusIDsThirdSet);
				usedGroupedValues.put(GROUPED_THIRD_ID, statusIDsThirdSet.size() > 0 ? true : false);
				
				allNeededStatuses.addAll(statusIDsFourthSet);
				usedGroupedValues.put(GROUPED_FOURTH_ID, statusIDsFourthSet.size() > 0 ? true : false);
				allNeededStatuses.addAll(statusIDsFifthSet);
				usedGroupedValues.put(GROUPED_FIFTH_ID, statusIDsFifthSet.size() > 0 ? true : false);
				statusIDs = new LinkedList<Integer>();
				statusIDs.addAll(allNeededStatuses);
			}else {
				statusIDs = GeneralUtils.createListFromIntArr(statusIDsArr);
			}
		}
				
		Date dateFrom = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_FROM);
		Date dateTo = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_TO);
		
		int selectedTimeInterval = (Integer)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL);
		int selectedCalculationMode = StringArrayParameterUtils.parseIntegerValue(parameters, 
				STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE, StatusOverTimeGraph.CALCULATION_MODE.NEW);
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE, selectedCalculationMode);
		saveParameters(paramSettings, personBean.getObjectID(), templateID);
	
		
		Integer datasourceType = (Integer)savedParamsMap.get(PARAMETER_NAME.DATASOURCETYPE);
		Integer projectOrReleaseID = (Integer)savedParamsMap.get(PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
		Integer filterID = (Integer)savedParamsMap.get(PARAMETER_NAME.FILTER_ID);
		
		SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToEntityIDToNumbersMap = 
			new TreeMap<Integer, SortedMap<Integer, Map<Integer, Integer>>>();
		List<TWorkItemBean> workItemBeans = getWorkItemBeans(
					datasourceType, projectOrReleaseID, filterID, contextMap, true, true, true, personBean, locale);
		if (workItemBeans==null || workItemBeans.isEmpty()) {
			return null;
		}
		int[] workItemIDs = GeneralUtils.createIntArrFromIntegerArr(GeneralUtils.getBeanIDs(workItemBeans));
		boolean newIssues = false;	
		switch (selectedCalculationMode) {
		case StatusOverTimeGraph.CALCULATION_MODE.NEW:
			yearToPeriodToEntityIDToNumbersMap = StatusOverTimeGraph.calculateNewWorkItems(workItemBeans,
					dateFrom, dateTo, selectedTimeInterval);
			newIssues = true;
			break;
		case StatusOverTimeGraph.CALCULATION_MODE.ACTUAL_SAMPLE:
			yearToPeriodToEntityIDToNumbersMap = StatusOverTimeGraph.calculateTotalInStatus(workItemIDs,
					dateFrom, dateTo, statusIDs, selectedTimeInterval, locale);
					accumulateNumbers(yearToPeriodToEntityIDToNumbersMap);
			break;
		case StatusOverTimeGraph.CALCULATION_MODE.ACTUAL_ACTIVITY:
			yearToPeriodToEntityIDToNumbersMap = StatusOverTimeGraph.calculateStatus(workItemIDs,
					dateFrom, dateTo, statusIDs, selectedTimeInterval, locale);  		
			break;
		case StatusOverTimeGraph.CALCULATION_MODE.ACCUMULATED_ACTIVITY:
			yearToPeriodToEntityIDToNumbersMap = StatusOverTimeGraph.calculateStatus(workItemIDs,
					dateFrom, dateTo, statusIDs, selectedTimeInterval, locale);
			accumulateNumbers(yearToPeriodToEntityIDToNumbersMap);
			break;
		}
	
		SortedMap numberOfIssuesInStatusByDate = transformPeriodsToDates(yearToPeriodToEntityIDToNumbersMap, selectedTimeInterval);
		if(grouping) {
			numberOfIssuesInStatusByDate = groupCalculatedValues(numberOfIssuesInStatusByDate, statusIDsFirstSet, statusIDsSecondSet, statusIDsThirdSet, 
					statusIDsFourthSet, statusIDsFifthSet);
		}
		return StatusOverTimeBL.convertToDOM(numberOfIssuesInStatusByDate, statusIDs, personBean.getFullName(), selectedTimeInterval, selectedCalculationMode, title, locale,
				grouping, groupingIDsToLabels, usedGroupedValues);

	}
	
	/**
	 * The following method converts calculated values into grouped values, for status over time chart.
	 * @param numberOfIssuesInStatusByDate The calculated date to status id to value map
	 * @param firstCriteria User defined grouping criteria 
	 * @param secondCriteria User defined grouping criteria
	 * @param thirdCriteria User defined grouping criteria
	 * @return
	 */
	private static SortedMap<Date, Map<Integer, Integer>> groupCalculatedValues(SortedMap<Date, Map<Integer, Integer>>numberOfIssuesInStatusByDate, Set<Integer> firstCriteria,
			Set<Integer> secondCriteria, Set<Integer> thirdCriteria, Set<Integer> fourthCriteria, Set<Integer> fifthCriteria) {
		SortedMap<Date, Map<Integer, Integer>> groupedNumberOfIssuesInStatusByDate = new TreeMap<Date, Map<Integer,Integer>>();
		for (Entry<Date, Map<Integer, Integer>> e : numberOfIssuesInStatusByDate.entrySet()) {
		    Map<Integer,Integer>statusIDsToValuesMap = e.getValue();
		    int first = 0;
		    int second = 0;
		    int third = 0;
		    int fourth = 0;
		    int fifth = 0;
		    for(Entry<Integer, Integer>statusIDsToValuesEntry : statusIDsToValuesMap.entrySet()) {
		    	if(firstCriteria.contains(statusIDsToValuesEntry.getKey())) {
		    		first += statusIDsToValuesEntry.getValue();
		    	}
		    	
		    	if(secondCriteria.contains(statusIDsToValuesEntry.getKey())) {
		    		second += statusIDsToValuesEntry.getValue();
		    	}
		    	
		    	if(thirdCriteria.contains(statusIDsToValuesEntry.getKey())) {
		    		third += statusIDsToValuesEntry.getValue();
		    	}
		    	
		    	if(fourthCriteria.contains(statusIDsToValuesEntry.getKey())) {
		    		fourth += statusIDsToValuesEntry.getValue();
		    	}
		    	
		    	if(fifthCriteria.contains(statusIDsToValuesEntry.getKey())) {
		    		fifth += statusIDsToValuesEntry.getValue();
		    	}
		    }
		    Map<Integer, Integer>aValueMap = new TreeMap<Integer, Integer>();
		    aValueMap.put(GROUPED_FIRST_ID, first);
		    aValueMap.put(GROUPED_SECOND_ID, second);
		    aValueMap.put(GROUPED_THIRD_ID, third);
		    aValueMap.put(GROUPED_FOURTH_ID, fourth);
		    aValueMap.put(GROUPED_FIFTH_ID, fifth);
		    groupedNumberOfIssuesInStatusByDate.put(e.getKey(), aValueMap);		    
		}
		return groupedNumberOfIssuesInStatusByDate;
	}
	
	/**
	* Add the time series to the timeSeriesCollection
	* SortedMap at first and second level (year and period) 
	* (Sorted because the accumulated should be calculated in the right order) 
	* @param yearToPeriodToEntityIDToWorkItemNumbersMap
	*/
	private static void accumulateNumbers( 
			SortedMap<Integer, SortedMap<Integer, Map<Integer, Integer>>> yearToPeriodToEntityIDToWorkItemNumbersMap) {
		Map<Integer, Integer> accumulatedMap = new HashMap<Integer, Integer>();
		for (Integer year : yearToPeriodToEntityIDToWorkItemNumbersMap.keySet()) {
			SortedMap<Integer, Map<Integer, Integer>> intervalToStatusChangeBeans = yearToPeriodToEntityIDToWorkItemNumbersMap.get(year);
			for (Integer period : intervalToStatusChangeBeans.keySet()) {
				Map<Integer, Integer> entityIDToWorkItemNumbersMap = intervalToStatusChangeBeans.get(period);
				if (entityIDToWorkItemNumbersMap!=null) {
					for (Integer entityID : entityIDToWorkItemNumbersMap.keySet()) {
						Integer numberOfStates = entityIDToWorkItemNumbersMap.get(entityID);
						if (numberOfStates!=null) {
							Integer accumuletedValueForEntity = accumulatedMap.get(entityID);
							if (accumuletedValueForEntity==null) {
								accumulatedMap.put(entityID, numberOfStates);
								accumuletedValueForEntity = accumulatedMap.get(entityID);
							} else {
								accumuletedValueForEntity = Integer.valueOf(accumuletedValueForEntity.intValue()+numberOfStates.intValue());
								accumulatedMap.put(entityID, accumuletedValueForEntity);
							}
							entityIDToWorkItemNumbersMap.put(entityID, accumuletedValueForEntity);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Serializes the datasource in an XML file
	 */
	public void serializeDatasource(OutputStream outputStream,
			Object datasource) {
		ReportBeansToXML.convertToXml(outputStream, (Document)datasource);
	}
	
	
	/**
	 * Prepares a map for rendering the config page
	 * @param savedParamsMap
	 * @param datasourceDescriptor
	 * @param personBean
	 * @param locale 
	 * @return
	 */
	protected String getTimeIntervalExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor,
			TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		List<IntegerStringBean> calculationModes = new LinkedList<IntegerStringBean>();
		calculationModes.add(new IntegerStringBean("statusOverTime.calculationMode." + 
				StatusOverTimeGraph.getCalculationModeSuffix(CALCULATION_MODE.NEW),
				Integer.valueOf(CALCULATION_MODE.NEW)));
		calculationModes.add(new IntegerStringBean("statusOverTime.calculationMode." + 
				StatusOverTimeGraph.getCalculationModeSuffix(CALCULATION_MODE.ACTUAL_SAMPLE),
				Integer.valueOf(CALCULATION_MODE.ACTUAL_SAMPLE)));
		calculationModes.add(new IntegerStringBean("statusOverTime.calculationMode." + 
				StatusOverTimeGraph.getCalculationModeSuffix(CALCULATION_MODE.ACTUAL_ACTIVITY),
				Integer.valueOf(CALCULATION_MODE.ACTUAL_ACTIVITY)));
		calculationModes.add(new IntegerStringBean("statusOverTime.calculationMode." + 
				StatusOverTimeGraph.getCalculationModeSuffix(CALCULATION_MODE.ACCUMULATED_ACTIVITY),
				Integer.valueOf(CALCULATION_MODE.ACCUMULATED_ACTIVITY)));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE_OPTIONS, calculationModes);
		JSONUtility.appendIntegerValue(stringBuilder, STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE,
				(Integer)savedParamsMap.get(STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE));
		JSONUtility.appendStringValue(stringBuilder, STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE_NAME_FIELD,
				STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE_NAME_VALUE);
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_OPTIONS,
				StatusBL.loadAll(locale));
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_SECOND_OPTIONS,
				StatusBL.loadAll(locale));
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_THIRD_OPTIONS,
				StatusBL.loadAll(locale));
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_FOURTH_OPTIONS,
				StatusBL.loadAll(locale));
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_FIFTH_OPTIONS,
				StatusBL.loadAll(locale));
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES,
				(Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES));
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_LABEL_KEY, "statusOverTime.prompt.statuses");
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.STATUS_NAME_VALUE);	
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_SECOND_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.STATUS_SECOND_NAME_VALUE);
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_THIRD_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.STATUS_THIRD_NAME_VALUE);
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_FOURTH_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.STATUS_FOURTH_NAME_VALUE);
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_FIFTH_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.STATUS_FIFTH_NAME_VALUE, true);
		return stringBuilder.toString();
		
	}
	
	/**
	 * 
	 * @param paramSettings
	 * @return
	 */
	protected Map<String, Object> loadParamObjectsFromPropertyStrings(String paramSettings) {
		Map<String, Object> paramsMap = super.loadParamObjectsFromPropertyStrings(paramSettings);
		Integer calculationMode = null;
		if (paramSettings!=null) {
			calculationMode = PropertiesHelper.getIntegerProperty(paramSettings, STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE);			
		} 
		if (calculationMode==null) {
			calculationMode = CALCULATION_MODE.ACTUAL_SAMPLE;
		}
		paramsMap.put(STATUS_OVER_TIME_PARAMETER_NAME.CALCULATION_MODE, calculationMode);
		return paramsMap;
	}
	
	public boolean isStatusOverTime() {
		return true;
	}
}
