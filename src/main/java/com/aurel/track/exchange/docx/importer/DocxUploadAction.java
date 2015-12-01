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

package com.aurel.track.exchange.docx.importer;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.exchange.UploadHelper;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DocxUploadAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	private static String DOCX_IMPORT = "docxImport";	
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private File uploadFile;
	private String uploadFileFileName;
	private TPersonBean personBean;
	private Locale locale;
	private Integer personID;
	private Integer projectOrReleaseID;
	private Integer parentID;
	private Integer workItemID;
	private Integer attachmentID;
	
	@Override
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
		String accept = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		JSONUtility.encodeJSON(servletResponse, 
				DocxImportJSON.getUploadJSON(accept));
		return null;
	}
	
	/**
	 * Upload the docx file
	 * @return
	 */
	public String upload() {
		return UploadHelper.upload(uploadFile, uploadFileFileName, AttachBL.getDocxImportDirBase() + String.valueOf(personID), locale, DOCX_IMPORT);	
		//FIXME as request parameter
		//return DOCX_IMPORT;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
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
	
	public Integer getProjectOrReleaseID() {
		return projectOrReleaseID;
	}
	
	public void setProjectOrReleaseID(Integer projectOrReleaseID) {
		this.projectOrReleaseID = projectOrReleaseID;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	/**
	 * Gets the attachmentID
	 * Used in struts.xml
	 * @return
	 */
	public Integer getAttachmentID() {
		return attachmentID;
	}
	
	
}
