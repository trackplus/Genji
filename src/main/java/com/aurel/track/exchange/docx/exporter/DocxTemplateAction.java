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

package com.aurel.track.exchange.docx.exporter;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DocxTemplateAction extends ActionSupport 
	implements Preparable, SessionAware, ServletResponseAware, ServletRequestAware  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	//private Locale locale;
	private TPersonBean personBean;	
	private File docxTemplate;
	private String docxTemplateFileName;
	private String docxTemplateList;
	
	public void prepare() throws Exception {
		//locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}

	/**
	 * Renders the upload form
	 */
	@Override
	public String execute() {
		JSONUtility.encodeJSON(servletResponse, DocxTemplateBL.renderSelectAndUploadTemplates(personBean.getDocxLastTemplate()));
		return null;
	}

	/**
	 * Saves the uploaded file and preselects the uploaded file for export
	 * @return
	 */
	public String upload() {
		DocxTemplateBL.upload(docxTemplate, docxTemplateFileName);
		JSONUtility.encodeJSON(servletResponse, DocxTemplateBL.renderSelectAndUploadTemplates(docxTemplateFileName));
		return null;
	}

	/**
	 * Deletes the uploaded file if any
	 * @return
	 */
	public String delete() {
		DocxTemplateBL.delete(docxTemplateList);
		JSONUtility.encodeJSON(servletResponse, DocxTemplateBL.renderSelectAndUploadTemplates(null));
		return null;
	}

	/**
	 * Downloads the file (inline or not)
	 * @return
	 */
	public String download() {
		//boolean inline = !personBean.isAlwaysSaveAttachment();
		DocxTemplateBL.download(docxTemplateFileName, servletRequest, servletResponse, false);
		return null;
	}

	public void setDocxTemplate(File docxTemplate) {
		this.docxTemplate = docxTemplate;
	}

	public void setDocxTemplateFileName(String docxTemplateFileName) {
		this.docxTemplateFileName = docxTemplateFileName;
	}

	
	public void setDocxTemplateList(String docxTemplateList) {
		this.docxTemplateList = docxTemplateList;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	
}
