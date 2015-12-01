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
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;

import com.aurel.track.beans.TPersonBean;

/**
 * Export the result of a query in different formats
 */
public interface ReportExporter {
	public static final  String FORMAT_PDF="pdf";
	public static final  String FORMAT_RTF="rtf";
	public static final  String FORMAT_XML="xml";
	public static final  String FORMAT_XLS="xls";
	public static final  String FORMAT_HTML="html";
	public static final  String FORMAT_CSV="csv";
	public static final  String FORMAT_TEXT="text";
	public static final  String FORMAT_ZIP="zip";
	public static final  String FORMAT_DOCX="docx";
	
	public static final String DEFAULT_ENCODING="Cp1252";
	
	public static final String ERROR_UNKNOWN_FORMAT="unknownFormat";
	public static final String ERROR_CREATE_REPORT="report.reportExportManager.err.errorCreatingReport";

	/**
	 * Export the items in the given output
	 * 
	 * @param document
	 * @param personBean
	 * @param locale
	 *            current locale
	 * @param parameters
	 *            the directory of template
	 * @param os
	 *            the output stream to write to
	 * @param contextMap
	 * @param description
	 */
	public void exportReport(Document document, TPersonBean personBean,
			Locale locale, Map<String, Object> parameters, OutputStream os,
			Map<String, Object> contextMap, Map<String, Object> description)
			throws ReportExportException;
}
