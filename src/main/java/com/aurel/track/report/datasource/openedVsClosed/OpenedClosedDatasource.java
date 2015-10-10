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

package com.aurel.track.report.datasource.openedVsClosed;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.item.history.HistorySelectValues;
import com.aurel.track.item.history.HistoryTransactionBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.datasource.TimeIntervalWithStatusDatasource;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;

public class OpenedClosedDatasource extends TimeIntervalWithStatusDatasource {
	
	private static final Logger LOGGER = LogManager.getLogger(OpenedClosedDatasource.class);
	
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
		saveParameters(paramSettings, personBean.getObjectID(), templateID);
		Integer datasourceType = (Integer)savedParamsMap.get(PARAMETER_NAME.DATASOURCETYPE);
		Integer projectOrReleaseID = (Integer)savedParamsMap.get(PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
		Integer filterID = (Integer)savedParamsMap.get(PARAMETER_NAME.FILTER_ID);
		List<TWorkItemBean> workItemBeans = getWorkItemBeans(datasourceType, projectOrReleaseID, filterID, contextMap, true, true, true, personBean, locale);
		if (workItemBeans==null || workItemBeans.isEmpty()) {
			return null;
		}
		int[] workItemIDs = GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromBeanList(workItemBeans));
		Date dateFrom = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_FROM);
		Date dateTo = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_TO);
		//get the selected closed statuses 
		List<Integer> selectedStatusList = GeneralUtils.createIntegerListFromIntegerArr((Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES));
		if (selectedStatusList==null || selectedStatusList.isEmpty()) {
			//if no status selected select all closed statuses
			selectedStatusList = GeneralUtils.createIntegerListFromBeanList(StatusBL.loadClosedStates());
		}
		//List stateChangeBeansList = stateChangeDAO.loadForWorkItemsInTimeInterval(dateFrom, dateTo, workItemIDs, selectedStatusList);				 	
		List<HistorySelectValues> historySelectValuesList = HistoryTransactionBL.getByWorkItemsFieldNewValuesDates(
				workItemIDs, SystemFields.INTEGER_STATE, selectedStatusList, dateFrom, dateTo);
		
		//get the selected time interval
		int selectedTimeInterval = StringArrayParameterUtils.parseIntegerValue(parameters, TIME_INTERVAL_PARAMETER_NAME.TIME_INTERVAL, TIME_INTERVAL.MONTH);
		SortedMap<Date, OpenedClosedDateData> openedClosedTimeSliceMap = new TreeMap<Date, OpenedClosedDateData>();
		
		List<TWorkItemBean> filteredWorkItemBeans = OpenedClosedBL.filterNewWorkItemsForPeriod(dateFrom, dateTo, workItemBeans);
		SortedMap<Integer, SortedMap<Integer, Integer>> yearToIntervalToWorkItemCount = OpenedClosedBL.getNewWorkItemsMap(filteredWorkItemBeans, selectedTimeInterval);
		addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, yearToIntervalToWorkItemCount, true);
		
		OpenedClosedBL.openedClosedJavaBean(openedClosedTimeSliceMap, 
				transformPeriodsToDates(yearToIntervalToWorkItemCount, selectedTimeInterval), true);
		
		
		SortedMap<Integer, SortedMap<Integer, Integer>> periodStatusChanges = OpenedClosedBL.getNumbersInTimeIntervalMap(historySelectValuesList, selectedTimeInterval);		   	
		addZerosForEmptyIntervals(dateFrom, dateTo, selectedTimeInterval, periodStatusChanges, true);
		//transformPeriodsToDates(periodStatusChanges, selectedTimeInterval);
		
		OpenedClosedBL.openedClosedJavaBean(openedClosedTimeSliceMap, 
				transformPeriodsToDates(periodStatusChanges, selectedTimeInterval), false);
		return OpenedClosedBL.convertToDOM(openedClosedTimeSliceMap.values(), locale, personBean.getFullName(), selectedTimeInterval);
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
		JSONUtility.appendILabelBeanList(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_OPTIONS,
				LocalizeUtil.localizeDropDownList(StatusBL.loadClosedStates(), locale));
		Integer[] selectedStatuses = (Integer[])savedParamsMap.get(TIME_INTERVAL_PARAMETER_NAME.STATUSES);
		if (selectedStatuses==null || selectedStatuses.length==0) {
			JSONUtility.appendIntegerArrayAsArray(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUSES, selectedStatuses);
		}
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_LABEL_KEY, "openedClosed.prompt.statuses");
		JSONUtility.appendStringValue(stringBuilder, TIME_INTERVAL_PARAMETER_NAME.STATUS_NAME_FIELD, TIME_INTERVAL_PARAMETER_NAME.STATUS_NAME_VALUE, true);		
		return stringBuilder.toString();
	}
	
}
