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
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;

import com.aurel.track.beans.TPersonBean;

/**
 * Implementation of the ReportExporter for FAQs in XHTML format
 */
public class ExcelExporter implements ReportExporter {

	private static final Logger LOGGER = LogManager.getLogger(ExcelExporter.class);

	@Override
	public void exportReport(Document document, TPersonBean personBean, Locale locale,
			Map<String, Object> parameters, OutputStream os, Map<String, Object> contextMap,
			Map<String, Object> description) throws ReportExportException {

		String format= (String) description.get(IDescriptionAttributes.FORMAT);
		if (format==null || !(format.equals(FORMAT_XLS))) {
			throw new ReportExportException(ERROR_UNKNOWN_FORMAT);
		}

		Date start = new Date();

		if (LOGGER.isInfoEnabled()) {
			Date end = new Date();
			LOGGER.info("Exporting the report lasted " + Long.toString(end.getTime()-start.getTime()) + " ms");
		}
	}// end of XHTML report generation

}
