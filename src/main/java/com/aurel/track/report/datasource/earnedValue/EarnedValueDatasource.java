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

package com.aurel.track.report.datasource.earnedValue;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.persist.ReportBeanHistoryLoader;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.datasource.TimeIntervalWithStatusDatasource;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;

public class EarnedValueDatasource extends TimeIntervalWithStatusDatasource {
	private static final Logger LOGGER = LogManager.getLogger(EarnedValueDatasource.class);

	static final int PLANNED_VALUE = 1;
	static final int ACTUAL_EFFORT = 2;
	static final int EARNED_VALUE = 3;

	//Configuration page constants
	protected static interface EFFORT_TYPE_PARAMETER_NAME {
		static String EFFORTTYPE = "effortType";
		static String EFFORTTYPE_OPTIONS = EFFORTTYPE + PARAMETER_NAME.OPTION_SUFFIX;
		static String EFFORTTYPE_NAME_FIELD = EFFORTTYPE + PARAMETER_NAME.NAME_SUFFIX;
		static String EFFORTTYPE_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + EFFORTTYPE;
	}

	/**
	 * In this software we have two types of expenses or efforts: time (or work)
	 * and monetary expenses (or cost).
	 *
	 */
	public static interface EFFORT_TYPE {
		/**
		 * The expense records considered mean work or time.
		 */
		static int TIME = 1;
		/**
		 * The expense records considered mean cost or money.
		 */
		static int COST = 2;
	}

	/**
	 * Gets the data source object (a Document object in this case) retrieved using the parameters settings
	 * @param params
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

		int effortType = StringArrayParameterUtils.parseIntegerValue(parameters,
				EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE, EFFORT_TYPE.TIME);
		boolean isTime = false;
		if (effortType==EFFORT_TYPE.TIME) {
			isTime = true;
		}
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE, effortType);
		saveParameters(paramSettings, personBean.getObjectID(), templateID);

		Integer datasourceType = (Integer)savedParamsMap.get(PARAMETER_NAME.DATASOURCETYPE);
		Integer projectOrReleaseID = (Integer)savedParamsMap.get(PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
		Integer filterID = (Integer)savedParamsMap.get(PARAMETER_NAME.FILTER_ID);
		List<ReportBean> reportBeanList = getReportBeanList(datasourceType, projectOrReleaseID, filterID,
				contextMap, true, true, true, personBean, locale);
		if (reportBeanList==null || reportBeanList.isEmpty()) {
			return null;
		}
		LOGGER.debug("Total number of reportBeans by datasource " + reportBeanList.size());

		Date dateFrom = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_FROM);
		Date dateTo = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_TO);
		List<ReportBeanWithHistory> reportBeanWithHistoryList =
				ReportBeanHistoryLoader.getReportBeanWithHistoryList(
				reportBeanList, locale,	false, false, true,
				new Integer[]{SystemFields.INTEGER_STATE}, true, true, true, true, false, personBean.getObjectID(), null,
				dateFrom, dateTo, true, LONG_TEXT_TYPE.ISPLAIN);
		Set<Integer> projectIDs = EarnedValueBL.prefilterReportBeansGetProjectIDs(reportBeanWithHistoryList, isTime, dateFrom, dateTo);
		SortedMap<Date, EarnedValueTimeSlice> earnedValueTimeSliceMap = new TreeMap<Date, EarnedValueTimeSlice>();
		int selectedTimeInterval = (Integer)savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL);
		if (!reportBeanWithHistoryList.isEmpty()) {
			LOGGER.debug("Number of reportBeanWithHistoryList after removing items with no budget or overlapping start/end dates " + reportBeanWithHistoryList.size());
			Date minStartDate = EarnedValueBL.getMinMaxStartEndDate(reportBeanWithHistoryList, true);
			Date maxEndDate = EarnedValueBL.getMinMaxStartEndDate(reportBeanWithHistoryList, false);
			if (minStartDate!=null && maxEndDate!=null) {
				//at least one startdate and enddate is specified
				Map<Integer, Double> hoursPerWorkingDayMap = ProjectBL.getHoursPerWorkingDayMap(projectIDs);
				SortedMap<Integer, SortedMap<Integer, Double>> plannedValues = EarnedValueBL.calculatePlannedValues(
						reportBeanWithHistoryList, isTime, minStartDate, maxEndDate, selectedTimeInterval, hoursPerWorkingDayMap);
				SortedMap<Integer, SortedMap<Integer, Double>> actualValues =  EarnedValueBL.calculateActualValues(
					reportBeanWithHistoryList, isTime, selectedTimeInterval);
				Integer[] selectedStatuses = StringArrayParameterUtils.parseIntegerArrValue(parameters, TIME_INTERVAL_PARAMETER_NAME.STATUSES);
				SortedMap<Integer, SortedMap<Integer, Double>> earnedValues = EarnedValueBL.calculateEarnedValues(
					reportBeanWithHistoryList, isTime, minStartDate, maxEndDate,
							selectedTimeInterval, hoursPerWorkingDayMap, GeneralUtils.createSetFromIntegerArr(selectedStatuses));

				addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, plannedValues, false);
				addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, actualValues, false);
				addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, earnedValues, false);
				EarnedValueBL.accumulateValues(plannedValues);
				EarnedValueBL.accumulateValues(actualValues);
				EarnedValueBL.accumulateValues(earnedValues);
				SortedMap<Date, Object> plannedDateValues = transformPeriodsToDates(plannedValues, selectedTimeInterval);
				SortedMap<Date, Object> plannedActualValues = transformPeriodsToDates(actualValues, selectedTimeInterval);
				SortedMap<Date, Object> plannedEarnedValues = transformPeriodsToDates(earnedValues, selectedTimeInterval);
				EarnedValueBL.addValueTypeToBeans(earnedValueTimeSliceMap,
						plannedDateValues, PLANNED_VALUE);
				EarnedValueBL.addValueTypeToBeans(earnedValueTimeSliceMap,
						plannedActualValues, ACTUAL_EFFORT);
				EarnedValueBL.addValueTypeToBeans(earnedValueTimeSliceMap,
						plannedEarnedValues, EARNED_VALUE);
			}
		}
		//saves the parameters into the database
		return EarnedValueBL.convertToDOM(earnedValueTimeSliceMap.values(), locale,
			datasourceDescriptor.getBundleName(), personBean.getFullName(), selectedTimeInterval, effortType);
	}

	/**
	 * Serializes the datasource in an XML file
	 */
	public void serializeDatasource(OutputStream outputStream,
			Object datasource) {
		ReportBeansToXML.convertToXml(outputStream, (Document)datasource);
	}

	/**
	 *
	 * @param paramSettings
	 * @return
	 */
	protected Map<String, Object> loadParamObjectsFromPropertyStrings(String paramSettings) {
		Map<String, Object> paramsMap = super.loadParamObjectsFromPropertyStrings(paramSettings);
		if (paramSettings!=null) {
			Integer effortType = PropertiesHelper.getIntegerProperty(paramSettings, EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE);
			paramsMap.put(EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE, effortType);
		} else {
			paramsMap.put(EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE, EFFORT_TYPE.TIME);
		}
		return paramsMap;
	}

	/**
	 * Prepares a map for rendering the config page
	 * @param params
	 * @param datasourceDescriptor
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected String getTimeIntervalExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor,
			TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		List<IntegerStringBean> effortTypeOptions = new LinkedList<IntegerStringBean>();
		effortTypeOptions.add(new IntegerStringBean("common.effort", Integer.valueOf(EFFORT_TYPE.TIME)));
		effortTypeOptions.add(new IntegerStringBean("common.cost", Integer.valueOf(EFFORT_TYPE.COST)));
		JSONUtility.appendIntegerStringBeanList(stringBuilder, EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE_OPTIONS, effortTypeOptions);
		JSONUtility.appendIntegerValue(stringBuilder, EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE,
				(Integer)savedParamsMap.get(EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE));
		JSONUtility.appendStringValue(stringBuilder, EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE_NAME_FIELD,
				EFFORT_TYPE_PARAMETER_NAME.EFFORTTYPE_NAME_VALUE);
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_OPTIONS, StatusBL.loadAll(locale));
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_SECOND_OPTIONS, StatusBL.loadAll(locale));
		Integer[] selectedStatuses = (Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES);
		if (selectedStatuses==null || selectedStatuses.length==0) {
			selectedStatuses = GeneralUtils.getBeanIDs(StatusBL.loadClosedStates());
		}
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES, selectedStatuses);
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_LABEL_KEY, "earnedValue.prompt.statuses");
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_NAME_FIELD,
				TIME_INTERVAL_PARAMETER_NAME.STATUS_NAME_VALUE, true);
		return stringBuilder.toString();

	}


}
