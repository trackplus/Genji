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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.export.bl.XsltTransformer;
import com.aurel.track.resources.ResourceBundleFileManager;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * Implementation of the ReportExporter for JasperReport
 */
public class JasperReportExporter implements ReportExporter {

	private static final Logger LOGGER = LogManager.getLogger(JasperReportExporter.class);

	public interface REPORT_PARAMETERS {
		//the URL of the template directory
		static String BASE_URL = "BASE_URL";
		static String LOGO_URL = "LOGO_URL";
		static String LOGO_FOLDER_URL = "LOGO_FOLDER_URL";
		static String COMPLETE_URL = "completeURL";
		static String COPYRIGHTHOLDER = "COPYRIGHTHOLDER";
		static String DYNAMIC_ICONS_URL = "DYNAMIC_ICONS_URL";
	}

	public void exportReport(Document document, TPersonBean personBean, Locale locale,
			Map<String, Object> parameters, OutputStream os, Map<String, Object> contextMap,
			Map<String, Object> description)	throws ReportExportException {
		String format= (String) description.get(IDescriptionAttributes.FORMAT);
		if (format==null || !(format.equals(FORMAT_PDF) ||
				format.equals(FORMAT_XLS) ||
				format.equals(FORMAT_RTF) ||
				format.equals(FORMAT_XML) ||
				format.equals(FORMAT_HTML) ||
				format.equals(FORMAT_CSV) ||
				format.equals(FORMAT_TEXT))) {
			throw new ReportExportException(ERROR_UNKNOWN_FORMAT);
		}
		Date start = null;
		if (LOGGER.isInfoEnabled()) {
			start = new Date();
		}
		URL baseURL = (URL)parameters.get(REPORT_PARAMETERS.BASE_URL);
		String templateDir = null;
		if (baseURL!=null) {
			templateDir = baseURL.getFile();
		}
		String xslt= (String) description.get(IDescriptionAttributes.XSLT);
		if(xslt!=null){
			String absolutePathXslt =templateDir+File.separator+xslt;
			document= XsltTransformer.getInstance().transform(document,absolutePathXslt);
			if (LOGGER.isInfoEnabled() && start!=null) {
				Date transformation = new Date();
				LOGGER.info("XSLT transformation lasted " + Long.toString(transformation.getTime()-start.getTime()) + " ms");
				start = transformation;
			}
		}
		//Jasper Reports creation
		JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance(); 
		LOGGER.info("Using " + JRPropertiesUtil.getInstance(jasperReportsContext).getProperty("net.sf.jasperreports.xpath.executer.factory"));
		JasperReport jasperReport = null;
		ResourceBundle resourceBundle=null;
		String csvSeparator = ";";
		String csvEncoding = null;
		URL completeURL = (URL)parameters.get(REPORT_PARAMETERS.COMPLETE_URL);
		if (completeURL==null) {
			//direct pdf/xls from report overview	
			csvSeparator = personBean.getCsvCharacter().toString(); 
			csvEncoding = personBean.getCsvEncoding();
			JasperDesign jasperDesign = ReportOverviewJasperDesign.getJasperDesign(personBean, locale, format, contextMap);
			try {
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
			} catch (JRException e) {
				LOGGER.error("Compiling the report failed with failed with " + e.getMessage(), e);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				return;
			}
			//get the resource bundle from the classPath 
			resourceBundle = ResourceBundleManager.getInstance().getResourceBundle(ResourceBundleManager.DATABASE_RESOURCES, locale);
			parameters.put(REPORT_PARAMETERS.DYNAMIC_ICONS_URL, Constants.getBaseURL() + "/optionIconStream.action?");
		} else {
			//prepared template file
			InputStream templateIn = null;
			try {
				templateIn = completeURL.openStream();
			} catch (Exception te) {
				LOGGER.error("Openeing the template file failed with " + te.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(te));
				throw new ReportExportException(
						ERROR_CREATE_REPORT, te);
			}
			try {
				jasperReport = (JasperReport) JRLoader.loadObject(templateIn);
			} catch (JRException e) {
				LOGGER.error("Loading the template from " + completeURL + " failed with " + e.getMessage(), e);
			}
			String resourceBundleName=jasperReport.getResourceBundle();
			if(resourceBundleName!=null) {
				//get the resource bundle from the disk 
				String absolutePathBundelName =templateDir+File.separator+resourceBundleName;
				resourceBundle= ResourceBundleFileManager.getBundle(absolutePathBundelName,locale);
			}
		}

		Date reportTemplateLoadingTime = null;
		if (LOGGER.isInfoEnabled() && start!=null) {
			reportTemplateLoadingTime = new Date();
			LOGGER.info("Loading the report template took " + Long.toString(reportTemplateLoadingTime.getTime()-start.getTime()) + " ms");
		}

		JasperPrint jasperPrint = null;

		parameters.put(REPORT_PARAMETERS.BASE_URL, baseURL.toExternalForm() + "/" /*File.separator*/);
		parameters.put(REPORT_PARAMETERS.LOGO_URL, "logo-254x105.png");
		URL imageURL = null;
		InputStream in=null;
		in=this.getClass().getClassLoader().getResourceAsStream("logo-254x105.png");
		try {
			if (in != null) {
				imageURL = new URL(ApplicationBean.getApplicationBean().getServletContext().getResource("/") + "logoAction.action?type=r");
			} else {
				String theResource = "/WEB-INF/classes/resources/Logos/logo-254x105.png";
				imageURL = ApplicationBean.getApplicationBean().getServletContext().getResource(theResource);
				parameters.put(REPORT_PARAMETERS.LOGO_URL, imageURL.toExternalForm());
			}
		} catch (Exception e) {
			System.err.println(e.getStackTrace());	
		}

		String copyrightHolder = ApplicationBean.getApplicationBean().getLicenseHolder();
		if (copyrightHolder != null && !"".equals(copyrightHolder)) {
			parameters.put(REPORT_PARAMETERS.COPYRIGHTHOLDER, copyrightHolder);
		}
		parameters.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
		parameters.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, DateTimeUtils.ISO_DATE_TIME_FORMAT);
		parameters.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, DoubleNumberFormatUtil.getIsoFormatString());
		parameters.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);

		//set the resource bundle
		if(resourceBundle!=null){
			parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
		}
		//set the locale
		parameters.put(JRParameter.REPORT_LOCALE, locale);
		Date reportFillingTime = null;
		JRFileVirtualizer virtualizer = null;
		try {
			//creating the virtualizer
			virtualizer = new JRFileVirtualizer(100, getTempDir());
			parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer); 
			LOGGER.debug("Before filling the report");
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
			if (LOGGER.isInfoEnabled() && reportTemplateLoadingTime!=null) {
				reportFillingTime = new Date();
				LOGGER.info("Filling the report lasted " + Long.toString(reportFillingTime.getTime()-reportTemplateLoadingTime.getTime()) + " ms");
			}
			LOGGER.debug("After filling the report");
			virtualizer.setReadOnly(true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			throw new ReportExportException(ERROR_CREATE_REPORT, e);
		}		
		if (jasperPrint != null) {
			try {
				String encoding = (String) description.get("encoding");
				LOGGER.debug("Encoding from description:"+encoding);
				if (encoding==null || encoding.length()==0){
					LOGGER.debug("Using default Encoding:"+DEFAULT_ENCODING);
					encoding=DEFAULT_ENCODING;
				}
				JRAbstractExporter exporter = null;

				if (format.equals(FORMAT_PDF)) {

					exporter = new JRPdfExporter(jasperReportsContext);
					exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
					LOGGER.debug("pdf export");
				}
				else if (format.equals(FORMAT_XLS)) {
					// Excel via POI
					exporter = new JExcelApiExporter();
					exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsAbstractExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					//These are the parameters, the first is to put the border cell and the second is to disable de background
					exporter.setParameter(JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER, false);
					exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
					exporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, true);
					LOGGER.debug("xls export");
				}
				else if (format.equals(FORMAT_RTF)) {
					exporter = new JRRtfExporter();
					LOGGER.debug("rtf export");
				} else if (format.equals(FORMAT_HTML)) {

					exporter = new JRHtmlExporter();
					exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
					exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
					LOGGER.debug("html export");
				} else if (format.equals(FORMAT_XML)) {
					exporter = new JRXmlExporter();
					LOGGER.debug("xml export");
				} else if (format.equals(FORMAT_CSV)) {
					exporter = new JRCsvExporter();
					if (csvEncoding!=null) {
						exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, csvEncoding);
					}
					exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, csvSeparator);
					LOGGER.debug("csv export");

				} else if (format.equals(FORMAT_TEXT)) {
					exporter = new JRTextExporter();
					exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, Integer.valueOf(10));
					exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, Integer.valueOf(10));
					LOGGER.debug("text export");
				} else {
					LOGGER.debug("defaults to pdf export");
					os.write(JasperExportManager.exportReportToPdf(jasperPrint));
					LOGGER.debug("after default export to pdf");
				}
				if (exporter!=null) {
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
					LOGGER.debug("before export");
					exporter.exportReport();
					LOGGER.debug("after export");
				}
			} catch (IOException e) {				
				LOGGER.error(e.getMessage(),e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e));

				throw new ReportExportException(ERROR_CREATE_REPORT, e);
			} catch (JRException e) {
				LOGGER.error(e.getMessage(), e);
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				throw new ReportExportException(ERROR_CREATE_REPORT, e);
			} catch (Exception e) {
				LOGGER.error("Export failed with " + e.getMessage(),e);

				LOGGER.debug(ExceptionUtils.getStackTrace(e));
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
				try {
					if (virtualizer!=null) {
						virtualizer.cleanup();
					}
				} catch (Exception e) {
					LOGGER.warn("Cleaning up the virtualized temporary files failed with " + e.getMessage(), e);
				}
			}

		} else {
			LOGGER.error("Jasper print report is null");
			throw new ReportExportException(ERROR_CREATE_REPORT);
		}

		if (LOGGER.isInfoEnabled() && reportTemplateLoadingTime!=null) {
			Date exportToSpecificFormatTime = new Date();
			LOGGER.info("Exporting the report lasted " + Long.toString(exportToSpecificFormatTime.getTime()-reportFillingTime.getTime()) + " ms");
		}
	}// end of jasper report generation

	/**
	 * Get the temporary directory name where the report will be eventually serialized
	 * If the directory does not exist create it first then return the name  
	 * @return
	 */
	private static String getTempDir(){
		File dir;
		if(HandleHome.getTrackplus_Home()==null){
			dir = new File(HandleHome.TEMP_DIR);
		}else{
			dir= new File(HandleHome.getTrackplus_Home() + File.separator
					+ HandleHome.TEMP_DIR);
		}
		try {
			if(!dir.exists()) {
				dir.mkdirs();
			}
		} catch (Exception e) {
			LOGGER.error("Creating the temporary directory failed with " + e.getMessage(), e);
		}
		LOGGER.debug("Temp directory " + dir.getAbsolutePath());
		return dir.getAbsolutePath();
	}
}
