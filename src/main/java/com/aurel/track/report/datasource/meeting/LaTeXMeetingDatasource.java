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

package com.aurel.track.report.datasource.meeting;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.category.report.execute.IDescriptionAttributes;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToLaTeXConverter;
import com.aurel.track.admin.customize.category.report.execute.ReportExporter;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.report.datasource.IPluggableDatasource;
import com.aurel.track.report.execute.ReportBeans;

/**
 * Datasource implementation for Meetings
 * 
 * @author Tamas
 * 
 */
public class LaTeXMeetingDatasource extends MeetingDatasource {

	private static final Logger LOGGER = LogManager.getLogger(LaTeXMeetingDatasource.class);

	private static String LATEXDIR = null;
	private static final String CRLF = System.getProperty("line.separator");

	/**
	 * Gets the data source object (a Document object in this case) retrieved
	 * using the parameters settings
	 * 
	 * @param parameters
	 * @param datasourceDescriptor
	 * @param contextMap
	 * @param templateDescriptionMap
	 * @param templateID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public Object getDatasource(Map<String, String[]> parameters, DatasourceDescriptor datasourceDescriptor, Map<String, Object> contextMap,
			Map<String, Object> templateDescriptionMap, Integer templateID, TPersonBean personBean, Locale locale) {
		ReportBeans reportBeans = null;
		File template = null;
		File templateDir = null;

		boolean useProjectSpecificID = false;
		Boolean projectSpecificID = (Boolean) contextMap.get(IPluggableDatasource.CONTEXT_ATTRIBUTE.USE_PROJETC_SPECIFIC_ID);
		if (projectSpecificID != null) {
			useProjectSpecificID = projectSpecificID.booleanValue();
		}
		Map<String, Object> savedParamsMap = new HashMap<String, Object>();
		String paramSettings = loadParamObjectsAndPropertyStringsAndFromStringArrParams(parameters, locale, savedParamsMap);
		saveParameters(paramSettings, personBean.getObjectID(), templateID);
		Integer meetingID = (Integer) savedParamsMap.get(MEETING_PARAMETER_NAME.MEETING);
		String linkType = (String) savedParamsMap.get(MEETING_PARAMETER_NAME.LINK_TYPE);
		Boolean onlyActiveChildren = (Boolean) savedParamsMap.get(MEETING_PARAMETER_NAME.ONLY_NOT_CLOSED_CHILDREN);
		reportBeans = getReportBeans(meetingID, onlyActiveChildren, linkType, personBean, locale);/* new ReportBeans(reportBeanList, locale, null, false);*/
		REPORT_BEANS = reportBeans;

		String exportFormat = (String) contextMap.get(CONTEXT_ATTRIBUTE.EXPORT_FORMAT);
		if (exportFormat == null && templateDescriptionMap != null) {
			exportFormat = (String) templateDescriptionMap.get(IDescriptionAttributes.FORMAT);
		}
		boolean withHistory = false;
		Boolean history = false;
		if (templateDescriptionMap != null) {
			String templateFile = (String) templateDescriptionMap.get(IDescriptionAttributes.MASTERFILE);

			template = new File(ReportBL.getDirTemplate(templateID).getAbsolutePath() + File.separator + templateFile);
			templateDir = new File(ReportBL.getDirTemplate(templateID).getAbsolutePath());

			if (!template.exists()) {
				LOGGER.error("Template file not found: " + template.getAbsolutePath());
				return null;
			}

			if (templateDescriptionMap.get(IDescriptionAttributes.HISTORY) != null) {
				try {
					history = Boolean.valueOf((String) templateDescriptionMap.get(IDescriptionAttributes.HISTORY));
					if (history != null) {
						withHistory = history.booleanValue();
					}
				} catch (Exception e) {
					LOGGER.warn("history should be a boolean string " + e.getMessage());
				}
			}
		}
		// if it will be exported to CSV or XLS then make the long texts plain,
		// otherwise simplified HTML
		boolean longTextIsPlain = ReportExporter.FORMAT_CSV.equals(exportFormat) || ReportExporter.FORMAT_XLS.equals(exportFormat)
				|| ReportExporter.FORMAT_XML.equals(exportFormat);
		return ReportBeansToLaTeXConverter.getDocumentFromReportBeans(reportBeans, withHistory, personBean, locale, null, null, longTextIsPlain, useProjectSpecificID, template,
				templateDir);
	}

}
