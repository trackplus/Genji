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

package com.aurel.track.report.datasource;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.category.report.execute.IDescriptionAttributes;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToLaTeXConverter;
import com.aurel.track.admin.customize.category.report.execute.ReportExporter;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.report.execute.ReportBeans;

/**
 * Datasource implementation for ReportBeans that are rendered based on LaTeX template
 *
 */
public class LaTeXReportBeanDatasource extends ReportBeanDatasource {
	
	private static final Logger LOGGER = LogManager.getLogger(LaTeXReportBeanDatasource.class);
	
	/**
	 * Overwrite this method for other types of export formats.
	 * @param templateDescriptionMap
	 * @param contextMap
	 * @param reportBeans
	 * @param personBean
	 * @param locale
	 * @param queryName
	 * @param queryExpression
	 * @param useProjectSpecificID
	 * @return a DOM document, but be aware that the real output might be generated as a side oroduct.
	 */
	protected Object getDocument(Map<String,Object> templateDescriptionMap,
								 Map<String, Object> contextMap,
			                     ReportBeans reportBeans,
			                     TPersonBean personBean,
			                     Locale locale,
			                     String queryName,
			                     String queryExpression,
			                     boolean useProjectSpecificID,
			                     Integer templateID){
		boolean withInlineComment = false;
		String exportFormat = (String)contextMap.get(CONTEXT_ATTRIBUTE.EXPORT_FORMAT);
		if (exportFormat==null) {
			//not dynamically generated, get it from description
			if (templateDescriptionMap!=null) {
				exportFormat = (String)templateDescriptionMap.get(IDescriptionAttributes.FORMAT);
			}
		} else {
			//dynamically generated content
			withInlineComment = true;
		}
		boolean withHistory = false;
		Boolean history = false;
		File template = null;
		File templateDir = null;
		
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
					LOGGER.warn("history should be a boolean string " + e.getMessage(), e);
				}
			}
		}
		//if it will be exported to CSV or XLS then make the long texts plain, otherwise simplified HTML
		boolean longTextIsPlain = ReportExporter.FORMAT_CSV.equals(exportFormat) || 
			ReportExporter.FORMAT_XLS.equals(exportFormat) || 
			ReportExporter.FORMAT_XML.equals(exportFormat);
		return ReportBeansToLaTeXConverter.getDocumentFromReportBeans(reportBeans, 
				               withHistory, personBean, locale, null, null, 
				               longTextIsPlain, useProjectSpecificID, 
				               template,
				               templateDir);
	}


}
