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

package com.aurel.track.admin.customize.mailTemplateEdit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.configExchange.exporter.EntityExporterException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.configExchange.importer.ImportResult;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.event.EmailTemplatesContainer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *
 */
public class MailTemplateAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;	
	private Locale locale;
	private HttpServletResponse servletResponse;
	
	//template detail parameters
	private Integer objectID;
	private String name;
	private String tagLabel;
	private String description;
	private boolean copy;
	
	//export templates
	private String mailTemplateIDs;

	//import templates
	private File uploadFile;
	private boolean overwriteExisting;
	private boolean clearChildren;
	
	private static final Logger LOGGER = LogManager.getLogger(MailTemplateAction.class);



	@Override
	public void prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public String execute() {
		List<TMailTemplateBean> mailTemplates= MailTemplateBL.getAllMailTemplates();
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), MailTemplatesJSON.encodeJSONMailTemplateList(mailTemplates));
		return null;
	}	
	
	public String edit() {
		TMailTemplateBean mailTemplateBean=null;
		if(objectID==null){
			mailTemplateBean=new TMailTemplateBean();
		}else{
			mailTemplateBean=MailTemplateBL.getMailTemplate(objectID);
			if(copy){
				String[] args={mailTemplateBean.getName()};
				mailTemplateBean.setName(LocalizeUtil.getParametrizedString("common.copy", args, locale));
			}
		}
		JSONUtility.encodeJSON(servletResponse,
				MailTemplatesJSON.encodeMailTemplateJSON(mailTemplateBean));
		return null;
	}

	public String save() {
		Integer newPk=null;
		if(objectID!=null){
			if(copy){
				newPk = MailTemplateEditBL.copyTemplate(objectID, name, tagLabel, description, locale);
			}else{
				TMailTemplateBean originalTemplate=MailTemplateBL.getMailTemplate(objectID);
				originalTemplate.setName(name);
				originalTemplate.setTagLabel(tagLabel);
				originalTemplate.setDescription(description);
				MailTemplateEditBL.saveMailTemplate(originalTemplate);
				newPk = objectID;
			}
		}else{
			newPk=MailTemplateEditBL.createNewTemplate(name, tagLabel, description);
		}
		EmailTemplatesContainer.resetTemplate(newPk);
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONSuccessAndID(newPk));
		return null;
	}

	public String delete() {
		List<Integer> templateIDs = null;
		if (mailTemplateIDs!=null) {
			templateIDs = GeneralUtils.createIntegerListFromStringArr(mailTemplateIDs.split(","));
			if (templateIDs!=null && !templateIDs.isEmpty()) {
				for (Integer templateID : templateIDs) {
					EmailTemplatesContainer.resetTemplate(templateID);
					MailTemplateEditBL.delete(templateID);
				}
			}
		}
		JSONUtility.encodeJSON(servletResponse,JSONUtility.encodeJSONSuccess());
		return null;
	}

	public String exportMailTemplates() {
		List<Integer> mailTemplateIDList = GeneralUtils.createIntegerListFromString(mailTemplateIDs);
		String fileName=MailTemplateEditBL.getExportFileName(mailTemplateIDList);
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(),
				servletResponse, fileName, "text/xml");
		OutputStream outputStream = null;
		try {
			outputStream = servletResponse.getOutputStream();
			String mailTemplateXmlExport = MailTemplateEditBL.createDOM(mailTemplateIDList);
			outputStream.write(mailTemplateXmlExport.getBytes());
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (EntityExporterException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public String importMailTemplates() {
		try {
			ImportContext importContext=new ImportContext();
			importContext.setOverrideExisting(overwriteExisting);
			importContext.setOverrideOnlyNotModifiedByUser(false);
			importContext.setClearChildren(clearChildren);
			List<ImportResult> importResultList = MailTemplateBL.importFile(uploadFile,importContext);
			if (importResultList!=null) {
				for (ImportResult importResult : importResultList) {
					Integer templateID = importResult.getNewObjectID();
					EmailTemplatesContainer.resetTemplate(templateID);
				}
			}
		} catch (EntityImporterException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			JSONUtility.encodeJSONFailure(e.getMessage());
		} catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			JSONUtility.encodeJSONFailure(e.getMessage());
		}
		JSONUtility.encodeJSONSuccess(servletResponse, false);
		return null;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}
	@Override
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagLabel() {
		return tagLabel;
	}

	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

	public void setClearChildren(boolean clearChildren) {
		this.clearChildren = clearChildren;
	}
}
