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

package com.aurel.track.exchange.latex.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.admin.customize.category.report.execute.ReportBeansToLaTeXConverter;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.docx.exporter.DocxExportBL;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.DownloadUtil;

/**
 * Logic for exporting a latex file
 * @author Tamas
 *
 */
public class LaTeXExportBL extends DocxExportBL{

	private static final Logger LOGGER = LogManager.getLogger(LaTeXExportBL.class);


	/**
	 * Serializes the docx content into the response's output stream
	 * @param response
	 * @param wordMLPackage
	 * @return
	 */
	public static String prepareReportResponse(HttpServletResponse response,
			TWorkItemBean workItem,
			ReportBeans reportBeans,
			TPersonBean user,
			Locale locale,
			String templateDir,
			String templateFile) {
		ReportBeansToLaTeXConverter rl = new ReportBeansToLaTeXConverter();
		File pdf = rl.generatePdf(workItem, reportBeans.getItems(), true, locale, user, "", "", false, new File(templateFile), new File(templateDir));
		String fileName=workItem.getSynopsis()+".pdf";
		String contentType = "application/pdf";
		if (pdf.length() < 10) {
			pdf = new File(pdf.getParent()+"/errors.txt");
			fileName=workItem.getSynopsis()+".txt";
			contentType = "text";
		}
		OutputStream outputStream = null;
		try {
			response.reset();
			response.setHeader("Content-Type", contentType);
			response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
			DownloadUtil.prepareCacheControlHeader(ServletActionContext.getRequest(), response);
			outputStream = response.getOutputStream();
			InputStream is = new FileInputStream(pdf);
			IOUtils.copy(is, outputStream);
			is.close();
		} catch (FileNotFoundException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}
		catch (IOException e) {
			LOGGER.error("Getting the output stream failed with " + e.getMessage(), e);
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e), e);
		}


		//			try {
		//				Docx4J.save(wordMLPackage, outputStream, Docx4J.FLAG_NONE);
		//				//wordMLPackage.save(outputStream);
		//				/*SaveToZipFile saver = new SaveToZipFile(wordMLPackage);
		//			saver.save(outputStream);*/
		//			} catch (Exception e) {
		//				LOGGER.error("Exporting the docx failed with throwable " + e.getMessage(), e);
		//				LOGGER.debug(ExceptionUtils.getStackTrace(e));
		//			}
		return null;
	}

}

