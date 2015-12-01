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


package com.aurel.track.report.datasource.filterHistory;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.datasource.TimePeriodDatasource;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;

public class FilterHistoryDatasource extends TimePeriodDatasource {
		
	public static interface FILTER_HISTORY_PARAMETER_NAME {
		/**
		 * Exclude without history
		 */
		public static String EXCLUDE_WITHOUT_HISTORY = "excludeWithoutHistory";
		public static String EXCLUDE_WITHOUT_HISTORY_NAME_FIELD = EXCLUDE_WITHOUT_HISTORY + PARAMETER_NAME.NAME_SUFFIX;
		public static String EXCLUDE_WITHOUT_HISTORY_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + EXCLUDE_WITHOUT_HISTORY;
		
		/**
		 * Fields
		 */
		public static String FIELDS = "fields";
		public static String FIELD_OPTIONS = "fieldOptions";
		public static String FIELDS_NAME_FIELD = FIELDS + PARAMETER_NAME.NAME_SUFFIX;
		public static String FIELDS_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + FIELDS;
		
		/**
		 * Person
		 */
		public static String PERSON = "person";
		public static String PERSON_OPTIONS = PERSON + PARAMETER_NAME.OPTION_SUFFIX;
		public static String PERSON_NAME_FIELD = PERSON + PARAMETER_NAME.NAME_SUFFIX;
		public static String PERSON_NAME_VALUE = PARAMETER_NAME.MAP_PREFIX + PERSON;
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
	@Override
	public Object getDatasource(Map<String, String[]> parameters, DatasourceDescriptor datasourceDescriptor,
			Map<String, Object> contextMap, Map<String, Object> templateDescriptionMap,
			Integer templateID, TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		Map<String, Object> savedParamsMap = new HashMap<String, Object>();
		String paramSettings = loadParamObjectsAndPropertyStringsAndFromStringArrParams(parameters, locale, savedParamsMap);
		Integer datasourceType = (Integer)savedParamsMap.get(PARAMETER_NAME.DATASOURCETYPE);
		Integer projectOrReleaseID = (Integer)savedParamsMap.get(PARAMETER_NAME.PROJECT_OR_RELEASE_ID);
		Integer filterID = (Integer)savedParamsMap.get(PARAMETER_NAME.FILTER_ID);
		List<ReportBean> reportBeanList = getReportBeanList(
				datasourceType, projectOrReleaseID, filterID, contextMap, true, true, true, personBean, locale);
		Boolean excludeWithoutHistory = StringArrayParameterUtils.getBooleanValue(parameters, 
				FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY);
		Integer[] fields = GeneralUtils.createIntegerArrFromCommaSeparatedString(
				StringArrayParameterUtils.getStringValue(parameters, FILTER_HISTORY_PARAMETER_NAME.FIELDS));
		Integer person = StringArrayParameterUtils.parseIntegerValue(
			parameters, FILTER_HISTORY_PARAMETER_NAME.PERSON);
		Date fromDate = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_FROM);
		Date toDate = (Date)savedParamsMap.get(TIME_PERIOD_PARAMETER_NAME.DATE_TO);
		List<ReportBeanWithHistory> historyData = FilterHistoryBL.addHistoryData(reportBeanList,
			fromDate, toDate, excludeWithoutHistory, fields, person, personBean, locale);
		boolean useProjectSpecificID = false;
		Boolean projectSpecificID = (Boolean)contextMap.get(CONTEXT_ATTRIBUTE.USE_PROJETC_SPECIFIC_ID);
		if (projectSpecificID!=null) {
			useProjectSpecificID = projectSpecificID.booleanValue();
		}
		paramSettings = PropertiesHelper.setBooleanProperty(paramSettings, FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY, excludeWithoutHistory);
		paramSettings = PropertiesHelper.setProperty(paramSettings, FILTER_HISTORY_PARAMETER_NAME.FIELDS,
				GeneralUtils.createCommaSeparatedStringFromIntegerArr(fields));
		paramSettings = PropertiesHelper.setIntegerProperty(paramSettings, FILTER_HISTORY_PARAMETER_NAME.PERSON, person);
		saveParameters(paramSettings, personBean.getObjectID(), templateID);
		ReportBeansToXML reportBeansToXML = new ReportBeansToXML();
		return reportBeansToXML.convertToDOM((List)historyData, true, locale, personBean.getFullName(), null, null, useProjectSpecificID);
	}

	@Override
	public void serializeDatasource(OutputStream outputStream,
			Object datasource) {
		ReportBeansToXML.convertToXml(outputStream, (Document)datasource);
	}
	
	
	/**
	 * Prepares a map for rendering the config page
	 * @param params
	 * @param datasourceDescriptor
	 * @param personBean
	 * @param locale 
	 * @return
	 */
	@Override
	protected String getTimePeriodExtraParams(Map<String, Object> savedParamsMap,
			DatasourceDescriptor datasourceDescriptor,
			TPersonBean personBean, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		Boolean excludeWithoutHistory = (Boolean)savedParamsMap.get(FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY);
		if (excludeWithoutHistory!=null) {
			JSONUtility.appendBooleanValue(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY, excludeWithoutHistory.booleanValue());
		}
		JSONUtility.appendStringValue(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY_NAME_FIELD,
				FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY_NAME_VALUE);
		JSONUtility.appendIntegerStringBeanList(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.FIELD_OPTIONS, HistoryLoaderBL.getPossibleHistoryFields(locale));		
		JSONUtility.appendIntegerArrayAsArray(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.FIELDS,
				(Integer[])savedParamsMap.get(FILTER_HISTORY_PARAMETER_NAME.FIELDS));
		JSONUtility.appendStringValue(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.FIELDS_NAME_FIELD, FILTER_HISTORY_PARAMETER_NAME.FIELDS_NAME_VALUE);
		JSONUtility.appendILabelBeanList(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.PERSON_OPTIONS, (List)PersonBL.loadPersons());
		JSONUtility.appendIntegerValue(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.PERSON,
				(Integer)savedParamsMap.get(FILTER_HISTORY_PARAMETER_NAME.PERSON));
		JSONUtility.appendStringValue(stringBuilder, FILTER_HISTORY_PARAMETER_NAME.PERSON_NAME_FIELD, FILTER_HISTORY_PARAMETER_NAME.PERSON_NAME_VALUE, true);
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * @param paramSettings
	 * @return
	 */
	@Override
	protected Map<String, Object> loadParamObjectsFromPropertyStrings(String paramSettings) {
		Map<String, Object> paramsMap = super.loadParamObjectsFromPropertyStrings(paramSettings);
		if (paramSettings!=null) {
			Boolean excludeWithoutHistory = PropertiesHelper.getBooleanProperty(paramSettings, FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY);
			paramsMap.put(FILTER_HISTORY_PARAMETER_NAME.EXCLUDE_WITHOUT_HISTORY, excludeWithoutHistory);
			String fieldsArr = PropertiesHelper.getProperty(paramSettings, FILTER_HISTORY_PARAMETER_NAME.FIELDS);
			Integer[] fields = GeneralUtils.createIntegerArrFromCommaSeparatedString(fieldsArr);
			paramsMap.put(FILTER_HISTORY_PARAMETER_NAME.FIELDS, fields);
			Integer personID = PropertiesHelper.getIntegerProperty(paramSettings,
					FILTER_HISTORY_PARAMETER_NAME.PERSON);
			paramsMap.put(FILTER_HISTORY_PARAMETER_NAME.PERSON, personID);
		}
		return paramsMap;
	}

}
