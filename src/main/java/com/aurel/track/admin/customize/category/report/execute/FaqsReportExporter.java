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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.report.datasource.faqs.FaqsDatasource;

/**
 * Implementation of the ReportExporter for FAQs in XHTML format
 */
public class FaqsReportExporter implements ReportExporter {

	private static final Logger LOGGER = LogManager.getLogger(FaqsReportExporter.class);

	public interface REPORT_PARAMETERS {
		//the URL of the template directory
		static String BASE_URL = "BASE_URL";
		static String LOGO_FOLDER_URL = "LOGO_FOLDER";
		static String COMPLETE_URL = "completeURL";
		static String COPYRIGHTHOLDER = "COPYRIGHTHOLDER";
		static String DYNAMIC_ICONS_URL = "DYNAMIC_ICONS_URL";
	}

	@Override
	public void exportReport(Document document, TPersonBean personBean, Locale locale,
			Map<String, Object> parameters, OutputStream os, Map<String, Object> contextMap,
			Map<String, Object> description) throws ReportExportException {

		String format= (String) description.get(IDescriptionAttributes.FORMAT);
		if (format==null || !(format.equals(FORMAT_ZIP))) {
			throw new ReportExportException(ERROR_UNKNOWN_FORMAT);
		}

		Date start = new Date();

		try {
			FaqsDatasource.streamZippedFAQs(os);
		} catch (Exception e) {
			
		}
		finally {
				try {
					os.flush();
				} catch (IOException e) {
					LOGGER.warn("Flushing the output stream failed with " + e);
				}
			try {
				os.close();
			} catch (IOException e) {
				LOGGER.warn("Closing the output stream failed with " + e);
			}
		}

		if (LOGGER.isInfoEnabled()) {
			Date end = new Date();
			LOGGER.info("Exporting the report lasted " + Long.toString(end.getTime()-start.getTime()) + " ms");
		}
	}// end of XHTML report generation

}
