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


package com.aurel.track.admin.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.dashboard.assign.DashboardAssignBL;
import com.aurel.track.user.ActionLogger;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ProjectAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	public static final String PROJECT_ADMIN_LAST_SELECTED_TAB = "projectAdmin.lastSelectedTab";
	public static final String PROJECT_ADMIN_LAST_SELECTED_SECTION = "projectAdmin.lastSelectedSection";

	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer projectID;
	private Integer entityID;
	private Integer parentID;

	//node is the projectID
	private Integer node;

	//parameters for project CRUD
	//whether to add or edit
	private boolean add;
	//when add, whether to add a new main project or a subproject to the selected project
	private boolean addAsSubproject;
	//when add whether to add as private project
	private boolean addAsPrivateProject;
	//the delete should be confirmed
	private boolean deleteConfirmed;

	//project detail beans
	private ProjectBaseTO projectBaseTO;
	private ProjectAccountingTO projectAccountingTO;
	private ProjectDefaultsTO projectDefaultsTO;


	private boolean confirmSave;
	private Integer tabIndex;
	private String sectionID;

	private Integer entityType;
	private Integer resetID;
	private boolean isTemplate;

	private static final Logger LOGGER = LogManager.getLogger(ProjectAction.class);

	public void prepare() throws Exception {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}

	/**
	 * Expand the projects
	 * @return
	 */
	public String projects() {
		JSONUtility.encodeJSON(servletResponse,
			ProjectJSON.getProjectsJSON(ProjectBL.getAdminProjectNodesLazy(node, personBean, isTemplate), isTemplate));
		return null;
	}
	public String loadLasSelections(){
		JSONUtility.encodeJSON(servletResponse,ProjectJSON.encodeProjectLastSelections(session));
		return null;
	}
	public String storeLastSelectedTab(){
		session.put(PROJECT_ADMIN_LAST_SELECTED_TAB, tabIndex);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String storeLastSelectedSection(){
		session.put(PROJECT_ADMIN_LAST_SELECTED_SECTION, sectionID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	/**
	 * Expand the project specific nodes
	 * @return
	 */
	public String expand() {
		JSONUtility.encodeJSON(servletResponse,
				ProjectConfigBL.getChildren(node, personBean, locale));
		return null;
	}

	/**
	 * This method returns the workspace/workspace templates toolbar extra config such as:
	 * has private workspace, edit orlocked mode based on template status
	 * @return
	 */
	public String getWorkspaceAndTemplateToolbarConfig() {
		String response = ProjectJSON.encodeToolbarItemConfig(personBean, projectID);
		JSONUtility.encodeJSON(servletResponse, response);
		return null;
	}

	/**
	 * Whether the user has already a private project
	 * @return
	 */
	public String hasPrivateProject() {
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONBoolean(
				ProjectConfigBL.hasPrivateProject(personBean.getObjectID())));
		return null;
	}

	/**
	 * Load the project details
	 * @return
	 */
	public String load() {
		JSONUtility.encodeJSON(servletResponse,
				ProjectConfigBL.getProjectDetail(projectID, add, addAsSubproject, addAsPrivateProject, personBean, locale));
		return null;
	}
	/**
	 * Saves the project details
	 * @return
	 */
	public String save() {
		List<ControlError> errors=ProjectConfigBL.validateProjectDetail(projectID, add,
				addAsSubproject, projectBaseTO,locale);


		if(errors.isEmpty()){
			JSONUtility.encodeJSON(servletResponse,
					ProjectConfigBL.saveProjectDetail(projectID, add, addAsSubproject, addAsPrivateProject,
							projectBaseTO, projectAccountingTO, projectDefaultsTO,
							confirmSave, isTemplate, personBean.getObjectID(), locale));
		}else{
			JSONUtility.encodeJSON(servletResponse, ProjectJSON.encodeJSONErrorList(errors, locale));
		}
		return null;
	}


	/**
	 * Refresh system lists after projectType change
	 * @return
	 */
	public String projectTypeChange() {
		boolean submittedAccountingInherited = false;
		if (projectAccountingTO!=null) {
			submittedAccountingInherited = projectAccountingTO.isAccountingInherited();
		}
		JSONUtility.encodeJSON(servletResponse,
				ProjectConfigBL.getProjectTypeChangeDetail(add, addAsSubproject, projectID, submittedAccountingInherited, projectDefaultsTO, projectBaseTO.getProjectTypeID(), locale));
		return null;
	}

	/**
	 * Deletes a project
	 * @return
	 */
	public String delete() {
		JSONUtility.encodeJSON(servletResponse,
				ProjectConfigBL.deleteProject(projectID, deleteConfirmed, personBean, locale));
		ActionLogger.log(session,"Deleted workspace " + projectID);
		return null;
	}
	public String clearParent(){
		ProjectConfigBL.clearParent(projectID);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	public String updateParent(){
		ProjectConfigBL.updateParent(projectID,parentID);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	public String cokpitAssignment(){
		DashboardAssignBL.resetProjectDashboard(projectID,entityType,resetID);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		ActionLogger.log(session,"Changed cockpit for workspace/release (" + entityType +") " + projectID + " to " + resetID);
		return null;
	}
	public String getProjectReleaseCtxMenu(){
		if(entityID<0){
			projectID=entityID*-1;
		}else{
			TReleaseBean releaseBean= LookupContainer.getReleaseBean(entityID);
			if (releaseBean!=null) {
				projectID=releaseBean.getProjectID();
			}
		}
		List<TListTypeBean> issueTypeList=IssueTypeBL.loadByPersonAndProjectAndCreateRight(personBean.getObjectID(), projectID, null, null, locale);
		boolean isProjectAdmin=false;
		boolean mightHaveRelease = false;
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		if (personBean!=null) {
			Integer projectTypeID = projectBean.getProjectType();
			if (projectTypeID!=null) {
				TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
				mightHaveRelease = ProjectConfigBL.mightHaveRelease(projectTypeBean, projectBean);
			}
		}
		if(	personBean.isSys()){
			isProjectAdmin=true;
		}else{
			isProjectAdmin= PersonBL.isProjectAdmin(personBean.getObjectID(),projectID);
		}

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendBooleanValue(sb,"isProjectAdmin", isProjectAdmin);
		JSONUtility.appendBooleanValue(sb,"mightHaveRelease", mightHaveRelease);
		JSONUtility.appendIntegerValue(sb,"projectID", projectID);
		sb.append("issueTypes:");
		sb.append(JSONUtility.encodeIssueTypes(issueTypeList));
		sb.append("}");
		sb.append("}");
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(sb);
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}


	public String changeTemplateState() {
		TProjectBean templateBean = ProjectBL.loadByPrimaryKey(projectID);
		Integer statusFlag = SystemStatusBL.getStatusFlag(projectID, TSystemStateBean.ENTITYFLAGS.PROJECTSTATE);
		boolean success = false;
		boolean isActive = false;
		if(statusFlag != null) {
			if(statusFlag.intValue() == TSystemStateBean.STATEFLAGS.ACTIVE) {
				LOGGER.debug("The template: " + templateBean.getObjectID() + " state has been changed from active to inactive!");
				success = ProjectConfigBL.changeTemplateStateTo(templateBean, TSystemStateBean.STATEFLAGS.CLOSED, locale);
				isActive = false;
			}

			if(statusFlag.intValue() == TSystemStateBean.STATEFLAGS.CLOSED) {
				LOGGER.debug("The template: " + templateBean.getObjectID() + " state has been changed from inactive to active!");
				success = ProjectConfigBL.changeTemplateStateTo(templateBean, TSystemStateBean.STATEFLAGS.ACTIVE, locale);
				isActive = true;
			}
		}
		String response = ProjectJSON.encodeChangeTemplateState(success, isActive);
		JSONUtility.encodeJSON(servletResponse, response);
		return null;
	}

	/**
	 * This method checks if modified project prefix already exists!
	 * @return
	 */
	public String validateProjectPrefix() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		boolean projectPrefixExists = false;
		if(projectID != null && projectBaseTO.getPrefix() != null) {
			TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
			if(projectBean != null) {
				String prefix = projectBaseTO.getPrefix();
				if (prefix!=null && !"".equals(prefix)) {
					projectPrefixExists = ProjectBL.projectPrefixExists(prefix, projectBean.getParent(), projectID);
				}
				LOGGER.debug("The prefix for following project: " + projectBean.getLabel() + " " + projectBean.getObjectID() + " exists: " + projectPrefixExists);
			}else {
				LOGGER.debug("Error! Loading project failed for checking new prefix! Project ID: " + projectID);
			}
		}
		JSONUtility.appendBooleanValue(sb, "projectPrefixExists", projectPrefixExists, true);
		sb.append("}");
		JSONUtility.encodeJSON(servletResponse, sb.toString());
		return null;
	}


	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public ProjectBaseTO getProjectBaseTO() {
		return projectBaseTO;
	}

	public void setProjectBaseTO(ProjectBaseTO projectBaseTO) {
		this.projectBaseTO = projectBaseTO;
	}

	public ProjectAccountingTO getProjectAccountingTO() {
		return projectAccountingTO;
	}

	public void setProjectAccountingTO(ProjectAccountingTO projectAccountingTO) {
		this.projectAccountingTO = projectAccountingTO;
	}

	public ProjectDefaultsTO getProjectDefaultsTO() {
		return projectDefaultsTO;
	}

	public void setProjectDefaultsTO(ProjectDefaultsTO projectDefaultsTO) {
		this.projectDefaultsTO = projectDefaultsTO;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setAddAsPrivateProject(boolean addAsPrivateProject) {
		this.addAsPrivateProject = addAsPrivateProject;
	}

	public void setAddAsSubproject(boolean addAsSubproject) {
		this.addAsSubproject = addAsSubproject;
	}

	public void setDeleteConfirmed(boolean deleteConfirmed) {
		this.deleteConfirmed = deleteConfirmed;
	}

	public void setConfirmSave(boolean confirmSave) {
		this.confirmSave = confirmSave;
	}

	public Integer getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(Integer tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getSectionID() {
		return sectionID;
	}

	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	public void setResetID(Integer resetID) {
		this.resetID = resetID;
	}

	public void setEntityID(Integer entityID) {
		this.entityID = entityID;
	}

	public boolean isTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	private boolean fromAjax;

	public void setFromAjax(boolean _fa) {
		this.fromAjax = _fa;
	}


}
