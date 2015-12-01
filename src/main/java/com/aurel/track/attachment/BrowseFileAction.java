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


package com.aurel.track.attachment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.LabelValueBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Used to browse the files from server
 */
public class BrowseFileAction extends ActionSupport implements Preparable, SessionAware, ServletRequestAware, ApplicationAware {

	private static final long serialVersionUID = 500L;

	private static final Logger LOGGER = LogManager.getLogger(BrowseFileAction.class);
	private HttpServletRequest servletRequest;
	private Map<String, Object> session;
	private Integer workItemID;
	private Integer documentID;
	private Map application;

	private String CKEditor;
	private String CKEditorFuncNum;
	private String langCode;
	private List<TAttachmentBean> attachments;
	private String imageData;

	private Locale locale;
	private TPersonBean person;
	private String source;

	/**
	 * 
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		person = ((TPersonBean) session.get(Constants.USER_KEY));
	}

	/**
	 * 
	 */
	@Override
	public String execute() {
		attachments = new ArrayList<TAttachmentBean>();
		if (workItemID != null) {
			List<TAttachmentBean> itemAttachments = AttachBL.getAttachmentsImage(workItemID);
			attachments.addAll(itemAttachments);
		}
		if (documentID != null) {
			List<TAttachmentBean> docuemntAttachments = AttachBL.getAttachmentsImage(documentID);
			attachments.addAll(docuemntAttachments);
		}
		if(workItemID==null&&documentID==null){
			LOGGER.debug("get attachments for new item ");
			//new item
			WorkItemContext ctx=(WorkItemContext)session.get("workItemContext");
			if(ctx==null){
				LOGGER.error("No context on session");
			}else {
				List<TAttachmentBean> allAttachments = ctx.getAttachmentsList();
				attachments = AttachBL.gettAttachmentImages(allAttachments);
			}
		}
		return SUCCESS;
	}

	/**
	 * Upload file to server.
	 * @return always null
	 */
	public String uploadFile() {

		String error = null;
		String url = "";
		try {
			MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) servletRequest;
			HttpServletResponse response = ServletActionContext.getResponse();

			if (wrapper.hasErrors()) {
				StringBuilder sb = new StringBuilder();
				Collection<String> errors = wrapper.getErrors();
				Iterator<String> i = errors.iterator();
				while (i.hasNext()) {
					sb.append((String) i.next()).append("</br>");
				}
			}

			if (wrapper.getFiles("upload") == null) {
				List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
				errors.add(new LabelValueBean(getText("common.err.required", new String[] { getText("report.export.manager.upload.file") }), "theFile"));
			}
			File[] files = wrapper.getFiles("upload");
			String[] fileNames = wrapper.getFileNames("upload");

			String description = null;
			ApplicationBean applicationBean = (ApplicationBean) application.get(Constants.APPLICATION_BEAN);
			Double maxAttachmentSizeInMb = AttachBL.getMaxAttachmentSizeInMb(applicationBean);
			int maxFILESIZE = AttachBL.getMaxFileSize(applicationBean);

			List<LabelValueBean> errors = new ArrayList<LabelValueBean>();
			Integer attachKey = null;
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					attachKey = AttachBL.storeFile(workItemID, person.getObjectID(), description, locale, session, response, files[i], fileNames[i],
							maxAttachmentSizeInMb, maxFILESIZE, errors, false);
					if (attachKey == null) {
						if (errors.size() > 0) {
							error = errors.get(0).getLabel();
						}
						break;
					}
				}
			}
			if (attachKey != null) {
				url = /*servletRequest.getContextPath() +*/ "downloadAttachment.action?attachKey=" + attachKey;
				if(workItemID!=null){
					url+="&workItemID=" + workItemID;
				}
			}
		} catch (Exception ex) {
			LOGGER.debug(ExceptionUtils.getStackTrace(ex), ex);
		}
		String s = "<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",\"" + url + "\", \"" + error
				+ "\");</script>";
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), s);
		return null;
	}

	/**
	 * @return the session
	 */
	public Map<String, Object> getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * @return the servletRequest
	 */
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	/**
	 * @param servletRequest
	 *            the servletRequest to set
	 */
	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	/**
	 * /**
	 * 
	 * @return the workItemID
	 */
	public Integer getWorkItemID() {
		return workItemID;
	}

	/**
	 * @param workItemID
	 *            the workItemID to set
	 */
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public Map getApplication() {
		return application;
	}

	@Override
	public void setApplication(Map application) {
		this.application = application;
	}

	public String getCKEditor() {
		return CKEditor;
	}

	public void setCKEditor(String CKEditor) {
		this.CKEditor = CKEditor;
	}

	public String getCKEditorFuncNum() {
		return CKEditorFuncNum;
	}

	public void setCKEditorFuncNum(String CKEditorFuncNum) {
		this.CKEditorFuncNum = CKEditorFuncNum;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public List<TAttachmentBean> getAttachments() {
		return attachments;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public Integer getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Integer documentID) {
		this.documentID = documentID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String _source) {
		source = _source;
	}
}
