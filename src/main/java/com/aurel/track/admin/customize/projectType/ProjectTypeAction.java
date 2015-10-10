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


package com.aurel.track.admin.customize.projectType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.configExchange.exporter.EntityExporterException;
import com.aurel.track.configExchange.importer.EntityImporter;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.configExchange.importer.ImportResult;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DownloadUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ProjectTypeAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	
	private static final Logger LOGGER = LogManager.getLogger(ProjectTypeAction.class);
	private static final long serialVersionUID = 1L;
	private TPersonBean personBean;
	private Map<String,Object> session;	
	private Locale locale;
	private HttpServletResponse servletResponse;

	private String node;
	private boolean add;
	private ProjectTypeTO projectTypeTO;
	private Integer replacementID;
	private Integer projectTypeID;

	private File uploadFile;
	private boolean overwriteExisting;
	private boolean includeGlobal;

		
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Expand a tree node from tree
	 * @return
	 */
	public String expand() {
		JSONUtility.encodeJSON(servletResponse, 
				ProjectTypesBL.getChildrenJSON(node, personBean, locale));
		return null;
	}
	
	public String edit() {
		String editJSON = ProjectTypesBL.edit(node, add, locale);
		JSONUtility.encodeJSON(servletResponse, editJSON);
		return null;
	}
	
	public String save() {
		String saveJSON = ProjectTypesBL.save(node, add, projectTypeTO, locale);
		JSONUtility.encodeJSON(servletResponse, saveJSON);
		return null;
	}

	public String delete() {
		String deleteJSON = ProjectTypesBL.delete(node, locale);
		JSONUtility.encodeJSON(servletResponse, deleteJSON);
		return null;
	}

	
	public String renderReplace(){
		String renderReplaceJSON = ProjectTypesBL.renderReplace(node, null, locale);
		JSONUtility.encodeJSON(servletResponse, renderReplaceJSON);
		return null;
	}

	public String replaceAndDelete() throws Exception{
		String jsonResponse = null;
		if (replacementID == null) {
			String errorMessage = getText("common.err.replacementRequired", 
					new String[] {LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale)});			
			jsonResponse = ProjectTypesBL.renderReplace(node, errorMessage, locale);
		} else {
			ProjectTypesBL.replaceAndDelete(node, replacementID);
			jsonResponse = JSONUtility.encodeJSONSuccess();
		}
		JSONUtility.encodeJSON(servletResponse, jsonResponse);
		return null;
	}
	public String importProjectTypes(){
		try {
			ImportContext importContext=new ImportContext();
			importContext.setOverrideExisting(overwriteExisting);
			importContext.setOverrideOnlyNotModifiedByUser(false);
			EntityImporter entityImporter=new EntityImporter();
			List<ImportResult> importResultList=entityImporter.importFile(uploadFile, importContext);
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
	
	public String export(){
		TProjectTypeBean pt = DAOFactory.getFactory().getProjectTypeDAO().loadByPrimaryKey(projectTypeID);
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(),
				servletResponse, "PT-"+pt.getLabel()+".xml", "text/xml");
		OutputStream outputStream = null;
		try {
			outputStream = servletResponse.getOutputStream();
			String mailTemplateXmlExport = ProjectTypesBL.createDOM(projectTypeID,includeGlobal);
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

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setNode(String node) {
		this.node = node;
	}	
	
	public void setAdd(boolean add) {
		this.add = add;
	}
		
	public ProjectTypeTO getProjectTypeTO() {
		return projectTypeTO;
	}

	public void setProjectTypeTO(ProjectTypeTO projectTypeTO) {
		this.projectTypeTO = projectTypeTO;
	}

	public void setSession(Map<String, Object> arg0) {
		this.session=arg0;
	}

	public Integer getProjectTypeID() {
		return projectTypeID;
	}

	public void setProjectTypeID(Integer projectTypeID) {
		this.projectTypeID = projectTypeID;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public boolean isOverwriteExisting() {
		return overwriteExisting;
	}

	public boolean isIncludeGlobal() {
		return includeGlobal;
	}

	public void setIncludeGlobal(boolean includeGlobal) {
		this.includeGlobal = includeGlobal;
	}
}
