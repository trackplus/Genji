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

package com.aurel.track.admin.customize.category.report.execute;

import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.w3c.dom.Document;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.exchange.docx.exporter.AssembleWordprocessingMLPackage;
import com.aurel.track.report.datasource.meeting.MeetingDatasource;

/* !!! This is not done and doesn't work! Students attempt to do something here */

public class DocxReportExporter implements ReportExporter {

	private static final Logger LOGGER = LogManager.getLogger(DocxReportExporter.class);

	/**
	 * Export the items in the given output
	 * 
	 * @param document W3C XML DOM document
	 * @param personBean current user
	 * @param locale
	 *            current locale
	 * @param parameters
	 *            the directory of the DOCX template enriched with special placeholders
	 * @param os
	 *            the output stream to write to
	 * @param contextMap
	 * @param description
	 */
	@Override
	public void exportReport(Document document, 
			                 TPersonBean personBean,
			                 Locale locale, 
			                 Map<String, Object> parameters, 
			                 OutputStream os,
			                 Map<String, Object> contextMap, 
			                 Map<String, Object> description)
					throws ReportExportException {

		String format= (String) description.get(IDescriptionAttributes.FORMAT);

		if (format == null || !format.equals(FORMAT_DOCX)) {
			throw new ReportExportException(ERROR_UNKNOWN_FORMAT);
		}

		/* Caution! Because the me
		 * based on the template file name, the template has to be uploaded in the wiki!
		 */
		String docxTemplatePath = null;
		URL completeURL = (URL)parameters.get(JasperReportExporter.REPORT_PARAMETERS.COMPLETE_URL);
		if (completeURL!=null) {
			docxTemplatePath = completeURL.getFile();
			WordprocessingMLPackage wpMLP = AssembleWordprocessingMLPackage.getWordMLPackage(
					MeetingDatasource.WORK_ITEM_BEAN, MeetingDatasource.REPORT_BEANS, 
					docxTemplatePath, personBean.getObjectID(), locale);
			try {
				Docx4J.save(wpMLP, os, Docx4J.FLAG_NONE);
			} catch (Exception e) {
				LOGGER.error("Exporting the docx failed with throwable " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}

	}

}
