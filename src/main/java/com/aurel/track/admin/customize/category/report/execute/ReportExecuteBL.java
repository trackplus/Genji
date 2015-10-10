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

package com.aurel.track.admin.customize.category.report.execute;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.category.report.ReportFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.report.datasource.IPluggableDatasource;
import com.aurel.track.report.export.bl.XsltTransformer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.GeneralUtils;

public class ReportExecuteBL {
	private static final Logger LOGGER = LogManager.getLogger(ReportExecuteBL.class);

	public interface REPORT_EXPORTER_TYPE {
		static String JASPER_EXPORTER = "Jasper Report";
		static String FAQ_EXPORTER = "FAQ Report";
		// static String FOP_EXPORTER = "FOP Report";
		static String EXCEL_EXPORTER = "Excel";
		static String DOCX_EXPORTER = "docx Report";
		static String LATEX_EXPORTER = "LaTeX Report";
	}

	/**
	 * Get the FieldType by class name
	 *
	 * @param fieldTypeClassName
	 * @return
	 */
	public static IPluggableDatasource pluggableDatasouceFactory(
			String pluggableDatasouceClassName) {
		Class pluggableDatasouceClass = null;
		if (pluggableDatasouceClassName == null) {
			LOGGER.warn("No PluggableDatasource specified ");
			return null;
		}
		try {
			pluggableDatasouceClass = Class
					.forName(pluggableDatasouceClassName);
		} catch (final ClassNotFoundException e) {
			LOGGER.error("The PluggableDatasouce class "
					+ pluggableDatasouceClassName
					+ "  not found found in the classpath " + e.getMessage(), e);
		}
		if (pluggableDatasouceClass != null) {
			try {
				return (IPluggableDatasource) pluggableDatasouceClass
						.newInstance();
			} catch (final Exception e) {
				LOGGER
						.error("Instantiating the PluggableDatasouce class "
								+ pluggableDatasouceClassName
								+ "  failed with " + e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Gets the context specific attributes in a map to avoid long parameter
	 * list
	 *
	 * @param fromIssueNavigator
	 * @param useProjectSpecificID
	 * @param dashboardProjectReleaseID
	 * @param dashboardEntityFlag
	 * @param exportFormat
	 */
	static Map<String, Object> getContextMap(Boolean fromIssueNavigator,
			String workItemIDs, Boolean useProjectSpecificID,
			Integer dashboardProjectReleaseID, Integer dashboardID,
			String exportFormat) {
		final Map<String, Object> contextMap = new HashMap<String, Object>();
		if (fromIssueNavigator != null) {
			contextMap
					.put(IPluggableDatasource.CONTEXT_ATTRIBUTE.FROM_ISSUE_NAVIGATOR,
							fromIssueNavigator);
		}
		/*
		 * if (workItemID!=null) {
		 * contextMap.put(IPluggableDatasource.CONTEXT_ATTRIBUTE.WORKITEMID,
		 * workItemID); }
		 */
		if (workItemIDs != null) {
			contextMap.put(IPluggableDatasource.CONTEXT_ATTRIBUTE.WORKITEMIDS,
					GeneralUtils.createIntegerListFromStringArr(workItemIDs
							.split(",")));
		}
		if (useProjectSpecificID != null) {
			contextMap
					.put(IPluggableDatasource.CONTEXT_ATTRIBUTE.USE_PROJETC_SPECIFIC_ID,
							useProjectSpecificID);
		}
		if (dashboardID != null) {
			contextMap.put(IPluggableDatasource.CONTEXT_ATTRIBUTE.DASHBOARD_ID,
					dashboardID);
		}
		if (dashboardProjectReleaseID != null) {
			contextMap
					.put(IPluggableDatasource.CONTEXT_ATTRIBUTE.DASHBOARD_PROJECT_RELEASE_ID,
							dashboardProjectReleaseID);
		}
		if (exportFormat != null) {
			contextMap.put(
					IPluggableDatasource.CONTEXT_ATTRIBUTE.EXPORT_FORMAT,
					exportFormat);
		}
		return contextMap;
	}

	/**
	 * Saves the parameters in a map to be saved in the session
	 *
	 * @param fromMap
	 * @param savedParameterNamesList
	 * @return
	 */
	static Map<String, Object> getParametersToSave(Map<String, Object> fromMap,
			List<String> savedParameterNamesList) {
		final Map<String, Object> savedParametersMap = new HashMap<String, Object>();
		if ((savedParameterNamesList != null) && (fromMap != null)) {
			for (final String parameterName : savedParameterNamesList) {
				if (fromMap.get(parameterName) != null) {
					savedParametersMap.put(parameterName,
							fromMap.get(parameterName));
				}
			}
		}
		return savedParametersMap;
	}

	/**
	 * Restores the saved parameters from the session
	 *
	 * @param fromMap
	 * @param toMap
	 * @param savedParameterNamesList
	 */
	static void restoreSavedParameters(Map<String, Object> fromMap,
			Map<String, Object> toMap, List<String> savedParameterNamesList) {
		if ((savedParameterNamesList != null) && (fromMap != null)
				&& (toMap != null)) {
			for (final String parameterName : savedParameterNamesList) {
				if (fromMap.get(parameterName) != null) {
					toMap.put(parameterName, fromMap.get(parameterName));
				}
			}
		}
	}

	public static Map<String, Object> getTemplateMap(Integer templateID) {
		Map<String, Object> descriptionMap = null;
		if (templateID != null) {
			// template exists
			final File template = ReportBL.getDirTemplate(templateID);
			descriptionMap = ReportBL.getTemplateDescription(template);
		}
		return descriptionMap;
	}

	/**
	 * Get the description map for the template
	 *
	 * @param templateID
	 * @param exportFormat
	 * @return
	 */
	static Map<String, Object> getTemplateMap(Integer templateID,
			String exportFormat) {
		Map<String, Object> descriptionMap = null;
		if (templateID != null) {
			// template exists
			return ReportExecuteBL.getTemplateMap(templateID);
		} else {
			// dynamic jasper report
			descriptionMap = new HashMap<String, Object>();
			// set the format used by jasper exporter
			descriptionMap.put(IDescriptionAttributes.FORMAT, exportFormat);
			// set the exporter to jasper exporter
			descriptionMap.put(IDescriptionAttributes.TYPE,
					REPORT_EXPORTER_TYPE.JASPER_EXPORTER);
		}
		return descriptionMap;
	}

	/**
	 * Serializes the data source into the response's output stream using a DOM
	 * to XML converter
	 *
	 * @param pluggableDatasouce
	 * @param datasource
	 * @return
	 */
	static String prepareDatasourceResponse(HttpServletResponse response,
			Integer templateID, IPluggableDatasource pluggableDatasouce,
			Map<String, Object> description, Object datasource) {
		// otherwise prepare an XML result
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(),
				response, "TrackReport.xml", "text/xml");
		try {
			final OutputStream outputStream = response.getOutputStream();
			final String xslt = (String) description.get("xslt");
			if ((datasource != null) && (xslt != null) && (templateID != null)) {
				final File template = ReportBL.getDirTemplate(templateID);
				URL baseURL = null;
				try {
					baseURL = template.toURL();
				} catch (final MalformedURLException e) {
					LOGGER.error("Wrong template URL for "
							+ template.getName() + e.getMessage(), e);
				}
				if (baseURL != null) {
					final String absolutePathXslt = baseURL.getFile() + xslt;
					try {
						datasource = XsltTransformer.getInstance().transform(
								(Document) datasource, absolutePathXslt);
					} catch (final Exception e) {
						LOGGER
								.warn("Tranforming the datasource with "
										+ absolutePathXslt + " failed with "
										+ e.getMessage(), e);
					}
				}
			}
			if (pluggableDatasouce == null) {
				// the default datasource (ReportBeans from last executed query)
				// serialize the DOM object into an XML stream
				ReportBeansToXML.convertToXml(outputStream,
						(Document) datasource);
			} else {
				// datasource specific serialization
				pluggableDatasouce
						.serializeDatasource(outputStream, datasource);
			}
		} catch (final Exception e) {
			LOGGER
					.error("Serializing the datasource to output stream failed with "
							+ e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Serializes the data source into the response's output stream using a
	 * ReportExporter
	 *
	 * @param templateID
	 * @param datasource
	 * @return
	 */
	static String prepareReportResponse(HttpServletResponse response,
			Integer templateID, Map<String, Object> contextMap,
			Map<String, Object> description, Object datasource,
			Map<String, Object> parameters, ServletContext servletContext,
			TPersonBean personBean, Locale locale) {
		URL baseURL = null;
		//URL logoFolderURL = null;
		String logoFolder = null;
		URL completeURL = null;
		String baseFileName = null;
		if (templateID == null) {
			final String baseFolder = "/design/silver/";
			// direct pdf/xls from report overview
			try {
				// set the baseURL to take some standard icons from
				// "/design/silver/icons"
				// which ale already used by the report overview anyway
				baseURL = servletContext.getResource(baseFolder + "16x16");
				LOGGER.debug("baseURL: " + baseURL.toString());
			} catch (final MalformedURLException e) {
				LOGGER.error("Getting the baseURL for "
						+ baseFolder + "16x16 failed with " + e.getMessage(), e);
			}
			//try {
				// set the baseURL to take some standard icons from
				// "/design/silver/icons"
				// which ale already used by the report overview anyway
				logoFolder = HandleHome.getTrackplus_Home()+File.separator + HandleHome.LOGOS_DIR + File.separator;
				//logoFolderURL = servletContext.getResource(baseFolder + "img/");
			/*} catch (final MalformedURLException e) {
				LOGGER.error("Getting the baseURL for "
						+ baseFolder + "img failed with " + e.getMessage(), e);
			}*/
		} else {
			// template exists
			final File template = ReportBL.getDirTemplate(templateID);
			final ILabelBean templateBean = ReportFacade.getInstance()
					.getByKey(templateID);
			if (templateBean != null) {
				baseFileName = templateBean.getLabel();
			}
			try {
				baseURL = template.toURL();
				LOGGER.debug("baseURL: " + baseURL.toString());
			} catch (final MalformedURLException e) {
				LOGGER.error("Wrong template URL for "
						+ template.getName() + e.getMessage(), e);
				return null;
			}
			try {
				completeURL = new URL(baseURL.toExternalForm() /*
																 * +
																 * "/"File.separator
																 */
						+ description.get(IDescriptionAttributes.MASTERFILE));
				completeURL.openStream();
				LOGGER.debug("completeURL: "
						+ completeURL.toString());
			} catch (final Exception me) {
				LOGGER
						.error(LocalizeUtil
								.getParametrizedString(
										"report.reportExportManager.err.masterFileTemplateNotFound",
										new String[] { me.getMessage() },
										locale)
								+ me);
				return null;
			}
		}
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		parameters.put(JasperReportExporter.REPORT_PARAMETERS.BASE_URL, baseURL);
		/*if (logoFolderURL != null) {
			parameters.put(
					JasperReportExporter.REPORT_PARAMETERS.LOGO_FOLDER_URL,
					logoFolderURL.toExternalForm());
		}*/
		if (logoFolder != null) {
			parameters.put(
					JasperReportExporter.REPORT_PARAMETERS.LOGO_FOLDER_URL,
					logoFolder);
		}
		if (completeURL != null) {
			parameters.put(JasperReportExporter.REPORT_PARAMETERS.COMPLETE_URL,
					completeURL);
		}
		if (baseFileName == null) {
			baseFileName = "TrackReport";
		}
		baseFileName += DateTimeUtils.getInstance().formatISODateTime(
				new Date());
		response.reset();
		final String format = (String) description
				.get(IDescriptionAttributes.FORMAT);
		if (ReportExporter.FORMAT_PDF.equals(format)) {
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ baseFileName + ".pdf\"");
		} else if (ReportExporter.FORMAT_RTF.equals(format)) {
			response.setHeader("Content-Type", "application/rtf");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ baseFileName + ".rtf\"");
		} else if (ReportExporter.FORMAT_XML.equals(format)) {
			response.setHeader("Content-Type", "text/xml");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ baseFileName + ".xml\"");
		} else if (ReportExporter.FORMAT_HTML.equals(format)) {
			response.setHeader("Content-Type", "text/html");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ baseFileName + ".html\"");
		} else if (ReportExporter.FORMAT_ZIP.equals(format)) {
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ baseFileName + ".zip\"");
		} else if (ReportExporter.FORMAT_XLS.equals(format)) {
			response.setHeader("Content-Type", "application/xls");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ baseFileName + ".xls\"");
		} else if (ReportExporter.FORMAT_CSV.equals(format)) {
			final String csvEncoding = personBean.getCsvEncoding();
			LOGGER.debug("csvEncoding is " + csvEncoding);
			if (csvEncoding != null) {
				response.setContentType("text/plain; " + csvEncoding);
			} else {
				response.setContentType("text/plain; charset=UTF-8");
			}
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ baseFileName + ".csv\"");
		} else if (ReportExporter.FORMAT_DOCX.equals(format)) {
			response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + baseFileName + ".docx\"");
		}
		DownloadUtil.prepareCacheControlHeader(
				ServletActionContext.getRequest(), response);
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
		} catch (final IOException e) {
			LOGGER
					.error("Getting the output stream failed with "
							+ e.getMessage(), e);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		try {
			LOGGER.debug("Exporter type is "
					+ description.get(IDescriptionAttributes.TYPE)
					+ " exporter format is "
					+ description.get(IDescriptionAttributes.FORMAT));
			final ReportExporter exporter = ReportExecuteBL
					.getExporter((String) description
							.get(IDescriptionAttributes.TYPE));
			exporter.exportReport((Document) datasource, personBean, locale,
					parameters, outputStream, contextMap, description);
			LOGGER.debug("Export done...");
		} catch (final ReportExportException e) {
			LOGGER.error("Exporting the report failed with "
					+ e.getMessage(), e);
			String actionMessage = "";
			if (e.getCause() != null) {
				actionMessage = LocalizeUtil.getParametrizedString(e
						.getMessage(),
						new String[] { e.getCause().getMessage() }, locale);
			} else {
				actionMessage = LocalizeUtil
						.getLocalizedTextFromApplicationResources(
								e.getMessage(), locale);
			}
			LOGGER.error(actionMessage);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		} catch (final Exception e) {
			LOGGER
					.error("Exporting the report failed with throwable "
							+ e.getMessage(), e);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}

	private static Map<String, ReportExporter> exporters = null;

	private static ReportExporter getExporter(String type) {
		LOGGER.debug("Get exporter for type:" + type);
		if (ReportExecuteBL.exporters == null) {
			ReportExecuteBL.exporters = ReportExecuteBL.createExporters();
		}
		return ReportExecuteBL.exporters.get(type);
	}

	private static Map<String, ReportExporter> createExporters() {
		final Map<String, ReportExporter> reportExporterMap = new HashMap<String, ReportExporter>();
		reportExporterMap.put(REPORT_EXPORTER_TYPE.JASPER_EXPORTER,
				new JasperReportExporter());
		reportExporterMap.put(REPORT_EXPORTER_TYPE.FAQ_EXPORTER,
				new FaqsReportExporter());
		reportExporterMap.put(REPORT_EXPORTER_TYPE.EXCEL_EXPORTER,
				new ExcelExporter());
		reportExporterMap.put(REPORT_EXPORTER_TYPE.LATEX_EXPORTER,
				new LaTeXReportExporter());
		reportExporterMap.put(REPORT_EXPORTER_TYPE.DOCX_EXPORTER, new DocxReportExporter());
		return reportExporterMap;
	}
}
