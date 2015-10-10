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

package com.aurel.track.exchange.track.importer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.exchange.ImportCounts;
import com.aurel.track.exchange.ImportJSON;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class TrackImportAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	static Logger LOGGER = LogManager.getLogger(TrackImportAction.class);
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private File uploadFile;
	private String uploadFileFileName;
	private Integer personID;
	private Locale locale;
	
	
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personID = ((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
	}
	
	/**
	 * Render the import page
	 */
	@Override
	/*public String execute() {
		String warningMessage = getText("admin.actions.importTp.warning");
		String accept = "application/zip";
		JSONUtility.encodeJSON(servletResponse, 
				TrackplusImportJSON.getUploadJSON(accept, warningMessage));
		return null;
	}*/
	
	/**
	 * Save the zip file and import the data 
	 * @return
	 */
	public String execute() {
		LOGGER.info("Import started");
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(uploadFile);
		} catch (FileNotFoundException e) {
			LOGGER.error("Getting the input stream for the zip failed with " + e.getMessage(), e);
			JSONUtility.encodeJSON(servletResponse,
					JSONUtility.encodeJSONFailure(getText("admin.actions.importTp.err.failed")));
			return null;
		}
		
		/**
		 * delete the old temporary attachment directory if exists from previous imports 
		 */
		String tempAttachmentDirectory = AttachBL.getAttachDirBase() + File.separator + AttachBL.tmpAttachments;
		AttachBL.deleteDirectory(new File(tempAttachmentDirectory));
		
		/**
		 * extract the zip to a temporary directory
		 */
		ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
		final int BUFFER = 2048;		
		File unzipTempDirectory= new File(tempAttachmentDirectory);
		if (!unzipTempDirectory.exists()) {
			unzipTempDirectory.mkdirs();
		}
		BufferedOutputStream dest = null;
		ZipEntry zipEntry;
		try {
			while((zipEntry = zipInputStream.getNextEntry()) != null) {
				File destFile = new File(unzipTempDirectory, zipEntry.getName());
				// grab file's parent directory structure			
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(destFile);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zipInputStream.read(data, 0, BUFFER))!= -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zipInputStream.close();
		} catch (Exception e) {
			LOGGER.error("Extracting the zip to the temporary directory failed with " +e.getMessage(), e);
			JSONUtility.encodeJSON(servletResponse,
					JSONUtility.encodeJSONFailure(getText("admin.actions.importTp.err.failed")), false);
			return null;
		}
		
		/**
		 * get the data file (the only file from the zip which is not an attachment)  
		 */
		File importDataFile = new File(tempAttachmentDirectory, ExchangeFieldNames.EXCHANGE_ZIP_ENTRY);
		if (!importDataFile.exists()) {
			LOGGER.error("The file " + ExchangeFieldNames.EXCHANGE_ZIP_ENTRY + " not found in the zip");
			JSONUtility.encodeJSON(servletResponse,
				JSONUtility.encodeJSONFailure(getText("admin.actions.importTp.err.failed")), false);
			return null;
		}
		
		/**
		 * field parser
		 */
		LOGGER.debug("Parsing the fields");
		List<ISerializableLabelBean> customFieldsBeans = new ImporterFieldParser().parse(importDataFile);
		Map<Integer, Integer> fieldsMatcherMap = null;
		try {
			fieldsMatcherMap = TrackImportBL.getFieldMatchMap(customFieldsBeans);
		} catch (ImportExceptionList importExceptionList) {
			LOGGER.error("Getting the field match map failed ");
			JSONUtility.encodeJSON(servletResponse,
				ImportJSON.importErrorMessageListJSON(ErrorHandlerJSONAdapter.handleErrorList(importExceptionList.getErrorDataList(),
					locale), null, true));
			return null;
		}
		
		/**
		 * dropdown parser
		 */
		LOGGER.debug("Parsing the external dropdowns");
		SortedMap<String, List<ISerializableLabelBean>> externalDropdowns = new ImporterDropdownParser().parse(importDataFile, fieldsMatcherMap);
		
		/**
		 * data parser
		 */
		LOGGER.debug("Parsing the items");
		List<ExchangeWorkItem> externalReportBeansList = new ImporterDataParser().parse(importDataFile, fieldsMatcherMap);
		try {
			LOGGER.debug("Importing the items");
			ImportCounts importCounts = TrackImportBL.importWorkItems(externalReportBeansList, externalDropdowns, fieldsMatcherMap, personID, locale);
			LOGGER.debug("Imported " + importCounts.getNoOfCreatedIssues() + " new issues " + " modified " + importCounts.getNoOfUpdatedIssues());
			JSONUtility.encodeJSON(servletResponse, ImportJSON.importMessageJSON(true,
					getText("admin.actions.importTp.lbl.result", 
							new String[] {Integer.valueOf(importCounts.getNoOfCreatedIssues()).toString(),
							Integer.valueOf(importCounts.getNoOfUpdatedIssues()).toString()}), true, locale), false);
		} catch (ImportExceptionList importExceptionList) {
			JSONUtility.encodeJSON(servletResponse,
				ImportJSON.importErrorMessageListJSON(
						ErrorHandlerJSONAdapter.handleErrorList(importExceptionList.getErrorDataList(),locale), null, true), false);
			return null;
		} catch (ImportException importException) {
			JSONUtility.encodeJSON(servletResponse, ImportJSON.importMessageJSON(false, getText(importException.getMessage()), true, locale), false);
			return null;
		} catch (Exception e) {
			JSONUtility.encodeJSON(servletResponse, ImportJSON.importMessageJSON(false, getText("admin.actions.importTp.err.failed"), true, locale), false);
			return null;
		}
		LOGGER.info("Import done");
		return null;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
		
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
