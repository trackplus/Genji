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

package com.aurel.track.admin.customize.category.report.execute;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.category.report.ReportJSON;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DatasourceDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.datasource.BasicDatasource;
import com.aurel.track.report.datasource.DatasourceDescriptorUtil;
import com.aurel.track.report.datasource.IPluggableDatasource;
import com.aurel.track.report.datasource.ReportBeanDatasource;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action class for report templates
 * @author Tamas
 *
 */
public class ReportExecuteAction extends ActionSupport implements Preparable, SessionAware, ServletContextAware, ServletResponseAware, ApplicationAware  {
	private static final Logger LOGGER = LogManager.getLogger(ReportExecuteAction.class);
	private static final long serialVersionUID = 1L;
	private Map<String, Object> application;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	/**
	 * the ID of the TExportTemplateBean set through 
	 * request parameters or submitted hidden parameters 
	 */
	private Integer templateID;
	/**
	 * the configuration parameters map
	 */
	private Map<String, String[]> params;
	
	/**
	 * when the report executing form a project/release specific dashboard:
	 * positive for release, negative for project
	 */
	private Integer projectID;
	/**
	 * The dashboardID when executing the report from a dashboard
	 */
	private Integer dashboardID;
	
	private ServletContext servletContext;
	
	/**
	 * whether we are coming from an issue navigator
	 */
	private boolean fromIssueNavigator;
	/**
	 * from issue navigator as result of a right click of a single issue from issue navigator
	 */
	
	/**
	 * row selections from issue navigator 
	 */
	private String workItemIDs;

	//format of the direct export from the report overview: pdf/xsd/csv/xml
	private String exportFormat;
	
	private Boolean useProjectSpecificID;
	private Map<String, Object> descriptionMap;
	private DatasourceDescriptor datasourceDescriptor;
	private Map<String, Object> contextMap;
	private IPluggableDatasource pluggableDatasouce;
	
	private boolean leaf;
	private String iconCls;

	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		ApplicationBean appBean =(ApplicationBean)application.get(Constants.APPLICATION_BEAN);
		if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
			useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn();
		}
		descriptionMap = ReportExecuteBL.getTemplateMap(templateID, exportFormat);
		if (exportFormat!=null) {
			//if export directly with dynamic templates from issue navigator not needed 
			//to send fromIssueNavigator as request parameter but it will be set explicitly
			fromIssueNavigator = true;
			LOGGER.debug("Export format is " + exportFormat);
		}
		String datasourceID = (String)descriptionMap.get(IDescriptionAttributes.DATASOURCEPLUGIN);
		if (datasourceID!=null) {
			LOGGER.debug("Prepare: datasourceID is " +  datasourceID);
			datasourceDescriptor = DatasourceDescriptorUtil.getDatasourceDescriptor(datasourceID);
		}
		if (datasourceDescriptor==null) {
			pluggableDatasouce = new ReportBeanDatasource();
			if (templateID!=null) {
				//for chooseReport() it should not debugged
				LOGGER.debug("Datasource is the default ReportBeanDatasource");
			}
		} else {
			String datasourceClass = datasourceDescriptor.getTheClassName();
			pluggableDatasouce = ReportExecuteBL.pluggableDatasouceFactory(datasourceClass);
			LOGGER.debug("Prepare: datasource class is " + datasourceClass);
		}
		contextMap = ReportExecuteBL.getContextMap(fromIssueNavigator, workItemIDs, useProjectSpecificID, projectID, dashboardID, exportFormat);
	}

    /**
     * Get the report tree and preselected report by executing the report from item navigator
     * @return
     */
    public String chooseReport() {
        List<TreeNode> categoryTree = CategoryPickerBL.getPickerNodesEager(
                personBean, false, false, null, false, null, null, locale, CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY);
        Integer reportID = (Integer)session.get("lastSelectedReport");
        JSONUtility.encodeJSON(servletResponse,  ReportJSON.chooseReportJSON(categoryTree, reportID));
        return null;
    }

	/**
	 * Whether the report has explicit datasource
	 * @return
	 */
	public String hasDatasourcePlugin() {
		boolean hasExtraParameters = pluggableDatasouce!=null &&
				((BasicDatasource)pluggableDatasouce).getDatasourceExtraParams(new HashMap<String, Object>(), datasourceDescriptor, contextMap, personBean, locale)!=null;
		if (pluggableDatasouce!=null) {
			LOGGER.debug("The datasource " + pluggableDatasouce.getClass().getName() + " has extra parameters: " + hasExtraParameters);
		}
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONBoolean(hasExtraParameters));
		return null;
	}
	
	/**
	 * Generates the report using an internal template (freemarker)
	 * or an external report engine
	 * into the servlet response's output stream
	 * If needed forwards to other action
	 */
	@Override
	public String execute() {
        session.put("lastSelectedReport", templateID);
		Object datasource = null;
		try {
			datasource = pluggableDatasouce.getDatasource(params, datasourceDescriptor,
					contextMap, descriptionMap, templateID, personBean, locale);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		Map<String, Object> parameters = pluggableDatasouce.getJasperReportParameters(params,
				datasourceDescriptor, contextMap, descriptionMap, templateID, personBean, locale);
		if (datasource==null) {
			LOGGER.error("The datasource for templateID " + templateID + " is null");
		} else {
			LOGGER.debug("Datasource is not null");
		}
		ReportExecuteBL.prepareReportResponse(servletResponse, templateID, contextMap,
				descriptionMap, datasource, parameters, servletContext, personBean, locale);
		return null;
	}


	/**
	 * Serializes the data source in the servlet response's output stream 
	 * @return
	 */
	public String serializeDatasource() {
		Object datasource = null;
		try {
			datasource = pluggableDatasouce.getDatasource(params, datasourceDescriptor,
					contextMap, descriptionMap, templateID, personBean, locale);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		if (datasource==null) {
			LOGGER.error("The datasource for datasourceDescriptor " + datasourceDescriptor.toString() + " is null");
		}
		ReportExecuteBL.prepareDatasourceResponse(servletResponse, templateID, 
				pluggableDatasouce, descriptionMap, datasource);
		return null;
	}
	
	/**
	 * Renders the jsConfigClass
	 * Sets the configTemplate field (to dynamically set the template in struts.xml) 
	 * and prepares the parameters to be shown in the freemarker template
	 * @return
	 */
	public String configPage() {
		String configJson = pluggableDatasouce.prepareParameters(templateID, datasourceDescriptor, contextMap, personBean, locale);
		JSONUtility.encodeJSON(servletResponse, configJson);
		return null;
	}
	
	/**
	 * If refreshing of some parameters through ajax is needed 
	 * @return
	 */
	public String refreshParameters() {
		String configJson = pluggableDatasouce.refreshParameters(params, templateID, datasourceDescriptor, contextMap, personBean, locale);
		JSONUtility.encodeJSON(servletResponse, configJson);
		return null;
	}
	
	/**
	 * Writes the preview image into the servlet response
	 * @return
	 */
	public String showPreviewImage() {
		
		
		File imgFile = null;
		if(!leaf) {
			
			String folderDirectory = "";			
			if("projects-ticon".equals(iconCls)) {
				folderDirectory = ApplicationBean.getApplicationBean().getServletContext().getRealPath("/"+Constants.DESIGN_DIRECTORY + "/" + Constants.DEFAULTDESIGNPATH + "/img/project.png");
			}else {
				folderDirectory = ApplicationBean.getApplicationBean().getServletContext().getRealPath("/"+Constants.DESIGN_DIRECTORY + "/" + Constants.DEFAULTDESIGNPATH + "/img/folder.png");
			}
			imgFile = new File(folderDirectory);
		}else {
			File templateDir=ReportBL.getDirTemplate(templateID);
			String imageName = (String)descriptionMap.get(IDescriptionAttributes.PREVIEW_GIF);
			imgFile = new File(templateDir,imageName);
		}
		if (templateID!=null) {
			if (imgFile!=null && imgFile.exists()) {
				try {
					FileImageInputStream input = new FileImageInputStream(imgFile);
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					byte[] data;
					int numBytesRead;
					while ((numBytesRead = input.read(buf)) != -1) {
						output.write(buf, 0, numBytesRead);
					}
					data = output.toByteArray();
					output.close();
					input.close();
					servletResponse.setContentType("image/gif");
					OutputStream outputStream = servletResponse.getOutputStream();
					outputStream.write(data);
					outputStream.flush();
				} catch (Exception e) {
					LOGGER.warn("Getting the preview image failed with " + e.getMessage(), e);
				}
			}
		}
		return null;
	}
	

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public Map<String, String[]> getParams() {
		return params;
	}

	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}
	
	public void setTemplateID(Integer templateID) {
		this.templateID = templateID;
	}
	
	public void setFromIssueNavigator(boolean fromIssueNavigator) {
		this.fromIssueNavigator = fromIssueNavigator;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	/*public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}*/

	
	public void setWorkItemIDs(String workItemIDs) {
		this.workItemIDs = workItemIDs;
	}

	public void setDashboardID(Integer dashboardID) {
		this.dashboardID = dashboardID;
	}
	
	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	

}
