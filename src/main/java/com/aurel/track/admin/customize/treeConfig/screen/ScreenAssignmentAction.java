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

package com.aurel.track.admin.customize.treeConfig.screen;

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
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityExporter;
import com.aurel.track.configExchange.exporter.EntityExporterException;
import com.aurel.track.configExchange.impl.ScreenExporter;
import com.aurel.track.configExchange.importer.EntityImporter;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.GeneralUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
/**
 * Action for screen assignment
 * @author Tamas
 *
 */
public class ScreenAssignmentAction extends ActionSupport
	implements Preparable, ServletResponseAware, SessionAware {
	
	private static final Logger LOGGER = LogManager.getLogger(ScreenAssignmentAction.class);
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	//the ID of the assigned screen
	private Integer assignedID;
	private String node;
	
	//export selected screens
	private String selectedScreenIDs;
	//import screens
	private File uploadFile;
	private String uploadFileFileName;
	private boolean overwriteExisting;
	
	
	@Override
	public void prepare() {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Saves the screen configuration 
	 * @return
	 */
	public String save(){
		ScreenConfigBL.save(node, assignedID, servletResponse);
		return null;
	}
		
	/**
	 * Export the selected screens into an xml file
	 * @return
	 */
	public String export() {
		List<Integer> screenIDList = GeneralUtils.createIntegerListFromString(selectedScreenIDs);
		String fileName=ScreenExporter.getExportFileName(screenIDList);
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(),
				servletResponse, fileName, "text/xml");
		OutputStream outputStream = null;
		try {
			outputStream = servletResponse.getOutputStream();
			ScreenExporter screenExporter=new ScreenExporter(true);
			List<EntityContext> entityContextList = screenExporter.exportScreens(screenIDList);
			String screenXmlExport= EntityExporter.export2(entityContextList);
			outputStream.write(screenXmlExport.getBytes());
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (EntityExporterException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return "";
	}
	
	/**
	 * Save the zip file and import the data 
	 * @return
	 */
	public String importScreens() {
		try {
			EntityImporter entityImporter = new EntityImporter();
			boolean clearChildren=overwriteExisting;
			ImportContext importContext=new ImportContext();
			importContext.setEntityType("TScreenBean");
			importContext.setOverrideExisting(overwriteExisting);
			importContext.setClearChildren(clearChildren);
			entityImporter.importFile(uploadFile, importContext);
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
	
	public void setAssignedID(Integer assignedID) {
		this.assignedID = assignedID;
	}
	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	
	
	public void setSelectedScreenIDs(String selectedScreenIDs) {
		this.selectedScreenIDs = selectedScreenIDs;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
	
	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

}
