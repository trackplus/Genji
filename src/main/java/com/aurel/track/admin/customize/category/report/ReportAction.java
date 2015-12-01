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

package com.aurel.track.admin.customize.category.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryFacadeFactory;
import com.aurel.track.admin.customize.category.CategoryJSON;
import com.aurel.track.admin.customize.category.CategoryTokens;
import com.aurel.track.admin.customize.category.report.exportReport.ExportReportTemplateBL;
import com.aurel.track.admin.customize.category.report.importReport.ImportTemplateBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DownloadUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * A action for configuring a report 
 */
public class ReportAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware  {
	private static final long serialVersionUID = 340L;
	private String node;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private String label;
	private File reportFile;
	private String reportFileFileName;
	//add or edit
	private boolean add;

	private boolean fromIssueNavigator;
	private Integer workItemID; 
	
	//export report templates
	private String selectedReportTemplateIDs;
	//import report templates
	private File uploadFile;	
	private boolean overwriteExisting;

	private boolean hasInitData=true;
	private String initData;
	private String layoutCls="com.trackplus.layout.ReportConfigLayout";
	private String pageTitle="menu.reports";

	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	@Override
	public String execute(){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "workItemID", workItemID);
		JSONUtility.appendBooleanValue(sb,"fromIssueNavigator",fromIssueNavigator,true);
		sb.append("}");
		initData=sb.toString();
		return SUCCESS;
    }
	
	private static final Logger LOGGER = LogManager.getLogger(ReportAction.class);

	/**
	 * Loads the report detail for add or edit 
	 * @return
	 */
	public String edit() {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
		Integer objectID = categoryTokens.getObjectID();
		ReportFacade reportFacade = (ReportFacade)CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		boolean modifiable = CategoryBL.isModifiable(reportFacade, categoryType, categoryTokens.getRepository(), categoryTokens.getType(), objectID, add, personBean);
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), 
				reportFacade.getDetailJSON(objectID, add, modifiable, personBean.getObjectID(), locale));
		return null;
	}
	
	/**
	 * Saves a report (new or edited)
	 * @return
	 */
	public String save() {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		ReportFacade reportFacade = ReportFacade.getInstance();
		String errorKey = reportFacade.isValidLabel(node, personBean.getObjectID(), null, label, add);
		if (errorKey==null) {
			if (CategoryBL.isModifiable(reportFacade, categoryTokens.getCategoryType(), categoryTokens.getRepository(), categoryTokens.getType(), categoryTokens.getObjectID(), add, personBean)) {
				try {
					JSONUtility.encodeJSON(servletResponse, ReportConfigBL.save(reportFacade, node, label, reportFile, reportFileFileName, add, personBean.getObjectID(), locale));
				} catch (IOException e) {
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			} else {
				JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONFailure(
					LocalizeUtil.getParametrizedString("common.err.noModifyRight", 
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(reportFacade.getLabelKey(), locale)}, locale)));
			}
		} else {
			String errorMessage = LocalizeUtil.getParametrizedString(errorKey, 
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.reportTemplate.lbl", locale)} , locale);			
			JSONUtility.encodeJSON(servletResponse, 
					CategoryJSON.createNodeResultJSON(false, node, errorMessage), false);
		}
		return null;
	}
	
	/**
	 * Downloads the zip with the template and the other report files
	 */
	public String download() {
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		Integer objectID = categoryTokens.getObjectID();
		File templateToDownload = ReportBL.getDirTemplate(objectID);
		servletResponse.reset();
		servletResponse.setHeader("Content-Type", "application/zip");
		servletResponse.setHeader("Cache-Control", "no-cache");
		servletResponse.setHeader("Content-Disposition", "attachment; filename=\""
				+ templateToDownload.getName() + ".zip" + "\"");
		ZipOutputStream zipOut;
		try {
			ServletOutputStream outs = servletResponse.getOutputStream();
			zipOut = new ZipOutputStream(outs);
			ReportBL.zipFiles(templateToDownload, zipOut,
					templateToDownload.getAbsolutePath());
			zipOut.close();
		} catch (FileNotFoundException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Export the selected screens into an xml file
	 * @return
	 */
	public String exportReports() {
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(), servletResponse, "ExportTemplates.zip", "application/zip");
		OutputStream outputStream = null;
		try {
			outputStream = servletResponse.getOutputStream();
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (outputStream!=null) {
			selectedReportTemplateIDs = "3,5";
			ExportReportTemplateBL.exportToZip(outputStream, selectedReportTemplateIDs);
		}
		return null;
	}
	
	/**
	 * Save the zip file and import the data 
	 * @return
	 */
	public String importReports() {	
		ImportTemplateBL.updateTExportTemplate(personBean.getObjectID(), uploadFile, overwriteExisting);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	
	/**
	 * @return the nodeId
	 */
	public String getNode() {
		return node;
	}
	
	/**
	 * @param node the nodeId to set
	 */
	public void setNode(String node) {
		this.node = node;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setReportFile(File reportFile) {
		this.reportFile = reportFile;
	}

	public void setReportFileFileName(String reportFileFileName) {
		this.reportFileFileName = reportFileFileName;
	}
	
	public void setFromIssueNavigator(boolean fromIssueNavigator) {
		this.fromIssueNavigator = fromIssueNavigator;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public void setHasInitData(boolean hasInitData) {
		this.hasInitData = hasInitData;
	}
	
	public String getInitData() {
		return initData;
	}
	
	public void setInitData(String initData) {
		this.initData = initData;
	}
	
	public void setSelectedReportTemplateIDs(String selectedReportTemplateIDs) {
		this.selectedReportTemplateIDs = selectedReportTemplateIDs;
	}
	
	public String getSelectedReportTemplateIDs() {
		return selectedReportTemplateIDs;
	}
	
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}
	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public String getLayoutCls() {
		return layoutCls;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
