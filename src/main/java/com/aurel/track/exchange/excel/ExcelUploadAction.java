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

package com.aurel.track.exchange.excel;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.exchange.UploadHelper;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ExcelUploadAction extends ActionSupport implements Preparable, SessionAware {
	
	private static final long serialVersionUID = 1L;
	static Logger LOGGER = LogManager.getLogger(ExcelUploadAction.class);
	private static String EXCEL_IMPORT = "excelImport";	
	private Map<String, Object> session;
	private File uploadFile;
	private String uploadFileFileName;
	private TPersonBean personBean;
	private Integer personID;
	//private boolean isSysAdmin;
	private Locale locale;
	
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
	}
	
	/**
	 * Render the import page
	 */
	@Override
	public String execute() {
		String accept = "application/excel";
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), 
			ExcelImportJSON.getUploadJSON(accept));
		return null;
	}
	
	/**
	 * Upload the excel file
	 * @return
	 */
	public String upload() {
		return UploadHelper.upload(uploadFile, uploadFileFileName, AttachBL.getExcelImportDirBase() + String.valueOf(personID), locale, EXCEL_IMPORT);
		/*InputStream inputStream;
		try {
			inputStream = new FileInputStream(uploadFile);
		} catch (FileNotFoundException e) {
			LOGGER.error("Getting the input stream for the excel file failed with " + e.getMessage(), e);
			JSONUtility.encodeJSON(ServletActionContext.getResponse(),
					JSONUtility.encodeJSONFailure(getText("admin.actions.importTp.err.failed")));
			//addActionError(getText("admin.actions.importTp.err.failed"));
			return null;
		}
		String excelImportDirectory = AttachBL.getExcelImportDirBase();
		String excelImportDirectoryForPerson = excelImportDirectory + String.valueOf(personID);
		UploadHelper.ensureDir(excelImportDirectoryForPerson);
		File excelFile = new File(excelImportDirectoryForPerson, uploadFileFileName);
        if (excelFile.exists())  {
            excelFile.delete();
        }
		try {
			OutputStream outputStream = new FileOutputStream(excelFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0){
				outputStream.write(buf, 0, len);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			LOGGER.error("Saving the excel to the temporary directory failed with " +e.getMessage(), e);
			JSONUtility.encodeJSON(ServletActionContext.getResponse(),
					JSONUtility.encodeJSONFailure(getText("admin.actions.importTp.err.failed")));
			return null;
		}		
		return EXCEL_IMPORT;*/
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
}
