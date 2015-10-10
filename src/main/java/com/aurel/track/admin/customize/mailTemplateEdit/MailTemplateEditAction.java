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

package com.aurel.track.admin.customize.mailTemplateEdit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.configExchange.exporter.EntityExporterException;

import com.aurel.track.util.GeneralUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.event.EmailTemplatesContainer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *
 */
public class MailTemplateEditAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	
	private static final Logger LOGGER = LogManager.getLogger(MailTemplateEditAction.class);
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private Locale locale;
	private HttpServletResponse servletResponse;

	private Integer templateID;

	//template detail parameters
	private Integer objectID;
	private boolean plainMail;
	private String theLocale;
	private String mailSubject;
	private String mailBody;
	private boolean copy;

	//export templates
	private String mailTemplateIDs;

	//import templates
	private File uploadFile;
	private boolean overwriteExisting;

	private boolean hasInitData=true;
	private String initData;

	private String layoutCls="com.trackplus.layout.MailTemplateEditLayout";
	private String pageTitle="admin.customize.mailTemplate.edit.title";

	public void prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}


	public String execute() {
		initData=prepareInitData();
		return SUCCESS;
	}
	public String reload(){
		List<TMailTemplateDefBean> mailTemplateDefBeans= MailTemplateDefEditBL.getMailTemplateDefs(templateID);
		Map<String, String> localeMap= MailTemplateDefEditBL.getLocaleMap();
		String templateDefJSON= MailTemplatesJSON.encodeMailTemplateDefListJSON(mailTemplateDefBeans, localeMap);
		JSONUtility.encodeJSON(servletResponse, templateDefJSON);
		return null;
	}
	public String prepareInitData(){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"templateID",templateID,true);
		sb.append("}");
		return sb.toString();
	}

	public String edit() {
		JSONUtility.encodeJSON(servletResponse,MailTemplateDefEditBL.loadTemplateDef(objectID));
		return null;
	}

	public String save() {
		JSONUtility.encodeJSON(servletResponse,
				MailTemplateDefEditBL.saveTemplateDef(copy, templateID, objectID, plainMail, theLocale, mailSubject, mailBody, locale));
		EmailTemplatesContainer.resetTemplate(templateID);
		return null;
	}

	public String delete() {
		MailTemplateDefEditBL.deleteTemplateDef(mailTemplateIDs);
		EmailTemplatesContainer.resetTemplate(templateID);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}

	public String exportMailTemplates() {
		List<Integer> mailTemplateDefIDList =  GeneralUtils.createIntegerListFromString(mailTemplateIDs);
		String fileName=MailTemplateDefEditBL.getExportFileName(mailTemplateDefIDList);
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(),
				servletResponse, fileName, "text/xml");
		OutputStream outputStream = null;
		try {
			outputStream = servletResponse.getOutputStream();
			String mailTemplateXmlExport = MailTemplateDefEditBL.createDOM( mailTemplateDefIDList);
			outputStream.write(mailTemplateXmlExport.getBytes());
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (EntityExporterException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}catch (IOException e){
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}



	public String importMailTemplates() {
		try {
			MailTemplateDefEditBL.importFile(uploadFile, templateID, overwriteExisting);
			EmailTemplatesContainer.resetTemplate(templateID);
		} catch (EntityImporterException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));  //To change body of catch statement use File | Settings | File Templates.
		}
		JSONUtility.encodeJSONSuccess(servletResponse, false);
		return null;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public void setPlainMail(boolean plainMail) {
		this.plainMail = plainMail;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
	public void setTheLocale(String theLocale) {
		this.theLocale = theLocale;
	}
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	public void setCopy(boolean copy) {
		this.copy = copy;
	}


	public void setMailTemplateIDs(String mailTemplateIDs) {
		this.mailTemplateIDs = mailTemplateIDs;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public String getInitData() {
		return initData;
	}

	public Integer getTemplateID() {
		return templateID;
	}

	public void setTemplateID(Integer templateID) {
		this.templateID = templateID;
	}

	public boolean isOverwriteExisting() {
		return overwriteExisting;
	}

	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
