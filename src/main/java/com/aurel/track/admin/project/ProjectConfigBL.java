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

package com.aurel.track.admin.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.aurel.track.admin.projectCopy.ProjectCopyBL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customList.CustomListBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypePriorityAssignmentFacade;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeSeverityAssignmentFacade;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeStatusAssignmentFacade;
import com.aurel.track.admin.customize.projectType.assignments.simple.ProjectTypeChildrenAssignmentFacade;
import com.aurel.track.admin.customize.projectType.assignments.simple.ProjectTypeItemTypeAssignmentFacade;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.assignments.RoleAssignmentsBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.project.release.ReleaseConfigBL;
import com.aurel.track.admin.project.release.ReleaseJSON;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.userLevel.UserLevelBL.USER_LEVEL_ACTION_IDS;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TInitStateBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.InitStatusDAO;
import com.aurel.track.dao.ProjectDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.associatedFields.IAssociatedFieldsIndexer;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;

/**
 * Business logic for configuring a project
 * @author Tamas Ruff
 *
 */
public class ProjectConfigBL {
	
	public interface PROJECT_ASSIGNMENTS {
		public final static int GENERAL = 1;
		public final static int ASSIGN_ROLES = 2;
		public final static int PHASES = 3;
		//public final static int RELEASES = 4; //only releases?
		//public final static int RELEASES_AND_PHASES = 4;
		public final static int LISTS = 4;
		public final static int FILTERS = 5;
		public final static int REPORT_TEMPLTATES = 6;
		public final static int SCREEN_ASSIGNMENT = 7;
		public final static int FIELD_CONFIGURATION = 8;
		public final static int ASSIGN_ACCOUNTS = 9;
		public final static int AUTOMAIL = 10;
		public final static int COCKPIT = 11;
		public final static int VERSION_CONTROL = 12;
		public final static int ASSIGN_WORKFLOW = 13;
		public final static int EXPORT_TO_MSPROJECT = 14;
		public final static int IMPORT_FROM_MSPROJECT = 15;
	}

	public interface EMAIL_PROTOCOL {
		public final static String POP3 = "pop3";
		public final static String IMAP = "imap";
	}

	public interface EMAIL_DEFAULT_PORTS {
		public final static int POP3 = 110;
		public final static int POP3_SSL = 995;
		public final static int IMAP = 143;
		public final static int IMAP_SSL = 993;
	}

	private static final Logger LOGGER = LogManager.getLogger(ProjectConfigBL.class);
	private static ProjectDAO projectDAO = DAOFactory.getFactory().getProjectDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	private static InitStatusDAO initStatusDAO = DAOFactory.getFactory().getInitStatusDAO();
	
	
	private static String ASSIGN_ROLES_CLASS = "roles-ticon";
	private static String RELEASE_ICON_CLASS = ReleaseBL.RELEASE_ICON_CLASS;
	private static String LIST_ICON_CLASS = "picklists-ticon";
	private static String FILTER_ICON_CLASS = "filter-ticon";
	private static String REPORT_ICON_CLASS = "report-ticon";
	private static String ACCOUNT_ICON_CLASS = "account-ticon";
	private static String FIELD_CONFIG_ICON_CLASS = "fields-ticon";
	private static String SCREEN_ASSIGN_ICON_CLASS = "forms-ticon";
	private static String AUTOMAIL_ASSIGNMENT_ICON = "automailc-ticon";
	private static String COCKPIT_ICON_CLASS = "cockpit-ticon";
	private static String VERSION_CONTROL_ICON_CLASS = "versionControl-ticon";
	private static String WORKFLOW_ICON_CLASS = "workflow-ticon";
	private static String MS_PROJECT_CLASS = "msProject-ticon";
	
	
	
	/**
	 * Gets the hard coded assignment nodes for a selected project
	 * @param projectID
	 * @param locale
	 * @return
	 */
	static String getChildren(Integer projectID, TPersonBean personBean, Locale locale) {
		if (projectID==null) {
			return "[]";
		}
		TProjectTypeBean projectTypeBean = null;
		TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
		if(projectBean==null){
			LOGGER.error("No project found for id:"+projectID);
			return "[]";
		}
		boolean isPrivateProject =false;
		Integer projectTypeID = projectBean.getProjectType();
		if (projectTypeID!=null) {
			projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
		}
		isPrivateProject = BooleanFields.fromStringToBoolean(projectTypeBean.getDefaultForPrivate());
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		
		boolean showRoles = !isPrivateProject;
		boolean showRelease = !isPrivateProject && mightHaveRelease(projectTypeBean, projectBean);
		Map<Integer, Boolean> userLevelMap = personBean.getUserLevelMap();
		boolean sys = personBean.isSys();
		//project specific lists
		Boolean manageLists = null;
		if (userLevelMap!=null) {
			manageLists = userLevelMap.get(USER_LEVEL_ACTION_IDS.LISTS);
		}
		boolean showManageList = sys || (manageLists!=null && manageLists.booleanValue());
		Boolean manageFilters = null;
		if (userLevelMap!=null) {
			manageFilters = userLevelMap.get(USER_LEVEL_ACTION_IDS.MANAGE_FILTERS);
		}
		boolean showManageFilters = sys || (manageFilters!=null && manageFilters.booleanValue());
		Boolean manageReports = null;
		if (userLevelMap!=null) {
			manageReports = userLevelMap.get(USER_LEVEL_ACTION_IDS.REPORT_TEMPLATES);
		}
		boolean showManageReports = sys || (manageReports!=null && manageReports.booleanValue());
		Boolean manageForms = null;
		if (userLevelMap!=null) {
			manageForms = userLevelMap.get(USER_LEVEL_ACTION_IDS.FORMS);
		}
		boolean showManageForms = sys || (manageForms!=null && manageForms.booleanValue() && !isPrivateProject);
		Boolean manageFields = null;
		if (userLevelMap!=null) {
			manageFields = userLevelMap.get(USER_LEVEL_ACTION_IDS.FIELDS);
		}
		boolean showManageFields = sys || (manageFields!=null && manageFields.booleanValue()  && !isPrivateProject);
		Boolean canSeeWorkflows = null;
		if (userLevelMap!=null) {
			canSeeWorkflows = userLevelMap.get(USER_LEVEL_ACTION_IDS.WORKFLOWS);
		}
		boolean showWorkflow = !isPrivateProject &&
				(sys || (canSeeWorkflows!=null && canSeeWorkflows.booleanValue())) &&
				!ApplicationBean.getInstance().isGenji();
		boolean showAssignAccount = !ApplicationBean.getInstance().isGenji() &&
				projectTypeBean!=null && mightHaveAccounting(projectTypeBean, projectBean);
		boolean showAutomail = !isPrivateProject;
		boolean showCockpit = !ApplicationBean.getInstance().isGenji();
		boolean showVersionControl=projectTypeBean!=null && projectTypeBean.getUseVersionControl() && !isPrivateProject;
		
		boolean hasNext = showRoles || showRelease || showManageList || showManageFilters ||  showManageReports ||
				showManageForms || showManageFields || showWorkflow || showAssignAccount || showAutomail || showCockpit || showVersionControl;
		//main project settings
		ProjectTreeNodeTO mainProjectDetails = new ProjectTreeNodeTO(
				String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.GENERAL),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.lbl.general", locale),
				ProjectBL.PROJECT_ICON_CLASS);
		mainProjectDetails.setPrivateProject(isPrivateProject);
		stringBuilder.append(ProjectJSON.getChildJSON(mainProjectDetails, !hasNext));
				
		//assigned roles
		if (showRoles) {
			hasNext = showRelease || showManageList || showManageFilters ||  showManageReports || showManageForms ||
					showManageFields ||showWorkflow || showAssignAccount || showAutomail || showCockpit || showVersionControl;
			String assignRolesLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.lbl.roles", locale);
			assignRolesLabel = LocalizeUtil.getParametrizedString("common.lbl.assign",
					new Object[] {assignRolesLabel}, locale);
			String roleAssignmentBranchNode = RoleAssignmentsBL.getProjectBranchNodeID(projectID);
			ProjectTreeNodeTO assignRoles =  new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.ASSIGN_ROLES),
					assignRolesLabel,
					ASSIGN_ROLES_CLASS,
					roleAssignmentBranchNode);
			stringBuilder.append(ProjectJSON.getChildJSON(assignRoles, !hasNext));
		}
		//releases
		if (showRelease) {
			hasNext = showManageList || showManageFilters ||  showManageReports || showManageForms || showManageFields ||
					showWorkflow || showAssignAccount || showAutomail || showCockpit || showVersionControl;
			String reasesBranchRoot = ReleaseConfigBL.getProjectBranchNodeID(projectID);
			ProjectTreeNodeTO phases = new ProjectTreeNodeTO(
				String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.PHASES),
				LocalizeUtil.getLocalizedTextFromApplicationResources(ReleaseConfigBL.RELEAESE_KEY, locale),
				RELEASE_ICON_CLASS, reasesBranchRoot);
			stringBuilder.append(ProjectJSON.getChildJSON(phases, !hasNext));
		}
		if (showManageList) {
			hasNext = showManageFilters ||  showManageReports || showManageForms || showManageFields ||
					showWorkflow || showAssignAccount || showAutomail || showCockpit || showVersionControl;
			String projectListsBranchRoot = ListBL.getProjectBranchNodeID(projectID);
			ProjectTreeNodeTO projectSpecificLists = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.LISTS),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.list", locale),
					LIST_ICON_CLASS,
					projectListsBranchRoot); 
			stringBuilder.append(ProjectJSON.getChildJSON(projectSpecificLists, !hasNext));
		}
		//project specific issue filters
		if (showManageFilters) {
			hasNext = showManageReports || showManageForms || showManageFields || showWorkflow ||
					showAssignAccount || showAutomail || showCockpit || showVersionControl;
			String projectFilterBranchRoot = CategoryBL.getProjectBranchNodeID(
					projectID, CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY);
			ProjectTreeNodeTO projectSpecificFilters = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.FILTERS),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.queryFilters", locale),
					FILTER_ICON_CLASS,
					projectFilterBranchRoot); 
			stringBuilder.append(ProjectJSON.getChildJSON(projectSpecificFilters, !hasNext));
		}
		//project specific reports
		if (showManageReports) {
			hasNext = showManageForms || showManageFields || showWorkflow ||
					showAssignAccount || showAutomail || showCockpit || showVersionControl;
			String projectReportBranchRoot = CategoryBL.getProjectBranchNodeID(
					projectID, CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY);
			ProjectTreeNodeTO projectSpecificReports = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.REPORT_TEMPLTATES),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.reportTemplates", locale),
					REPORT_ICON_CLASS,
					projectReportBranchRoot); 
			stringBuilder.append(ProjectJSON.getChildJSON(projectSpecificReports, !hasNext));
		}
		//screen assignment
		if (showManageForms) {
			hasNext = showManageFields || showWorkflow || showAssignAccount ||
					showAutomail || showCockpit || showVersionControl;
			String screenAssignmentBranchRoot = TreeConfigBL.getProjectBranchNodeID(projectID, TreeConfigBL.SCREEN_CONFIG);
			ProjectTreeNodeTO projectSpecificScreenAssignments= new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.SCREEN_ASSIGNMENT),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.customForms", locale),
					SCREEN_ASSIGN_ICON_CLASS,
					screenAssignmentBranchRoot); 
			stringBuilder.append(ProjectJSON.getChildJSON(projectSpecificScreenAssignments, !hasNext));
		}
		//field config
		if (showManageFields) {
			hasNext = showWorkflow || showAssignAccount ||
					showAutomail || showCockpit || showVersionControl;
			String fieldConfigBranchRoot = TreeConfigBL.getProjectBranchNodeID(projectID, TreeConfigBL.FIELD_CONFIG);
			ProjectTreeNodeTO projectSpecificFieldConfigs = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.FIELD_CONFIGURATION),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.customField", locale),
					FIELD_CONFIG_ICON_CLASS,
					fieldConfigBranchRoot); 
			stringBuilder.append(ProjectJSON.getChildJSON(projectSpecificFieldConfigs, !hasNext));
		}
		//workflow assignment
		if (showWorkflow) {
			hasNext =  showAssignAccount || showAutomail || showCockpit || showVersionControl;
			String workflowAssignmentBranchRoot = TreeConfigBL.getProjectBranchNodeID(/*projectID*/null, TreeConfigBL.WORKFLOW_CONFIG);
			ProjectTreeNodeTO projectSpecificWorkflowAssignments= new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.ASSIGN_WORKFLOW),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.workflow", locale),
					WORKFLOW_ICON_CLASS, workflowAssignmentBranchRoot);
			projectSpecificWorkflowAssignments.setProjectID(projectID);
			stringBuilder.append(ProjectJSON.getChildJSON(projectSpecificWorkflowAssignments, !hasNext));
		}
		//assigned accounts
		if (showAssignAccount) {
			hasNext =  showAutomail || showCockpit || showVersionControl;
			String assignAccountLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.account.lbl.accounts", locale);
			assignAccountLabel = LocalizeUtil.getParametrizedString("common.lbl.assign",
					new Object[] {assignAccountLabel}, locale);
			ProjectTreeNodeTO assignAccounts =  new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.ASSIGN_ACCOUNTS),
					assignAccountLabel,
					ACCOUNT_ICON_CLASS);
			stringBuilder.append(ProjectJSON.getChildJSON(assignAccounts, !hasNext));
		}
		//project specific automail
		if (showAutomail) {
			hasNext =  showCockpit || showVersionControl;
			ProjectTreeNodeTO automail = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.AUTOMAIL),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.automail", locale),
					AUTOMAIL_ASSIGNMENT_ICON);
			stringBuilder.append(ProjectJSON.getChildJSON(automail, !hasNext));
		}
		//project cockpit
		
		if (showCockpit) {
			hasNext =  showVersionControl;
			ProjectTreeNodeTO cockpit = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.COCKPIT),
					LocalizeUtil.getLocalizedTextFromApplicationResources("menu.cockpit", locale),
					COCKPIT_ICON_CLASS);
			stringBuilder.append(ProjectJSON.getChildJSON(cockpit, !hasNext));
		}
		//version control
		if (showVersionControl) {
			ProjectTreeNodeTO versionControl = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.VERSION_CONTROL), 
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.lbl.versionControl", locale),
					VERSION_CONTROL_ICON_CLASS);
			stringBuilder.append(ProjectJSON.getChildJSON(versionControl, true));
		}	
		//export to MS Project
		/*if (useExportMsProject) {
			hasNext=useImportMsProject;
			ProjectTreeNodeTO exportToMsProject = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.EXPORT_TO_MSPROJECT),
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.export.lbl.msProject", locale),
					MS_PROJECT_CLASS);
			stringBuilder.append(ProjectJSON.getChildJSON(exportToMsProject,!hasNext));
		}*/
		//import from MS Project
		/*if (useImportMsProject) {
			ProjectTreeNodeTO importFromMsProject = new ProjectTreeNodeTO(
					String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.IMPORT_FROM_MSPROJECT),
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.import.lbl.msProject", locale),
					MS_PROJECT_CLASS);
			stringBuilder.append(ProjectJSON.getChildJSON(importFromMsProject, true));
		}*/
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
	
	static boolean hasPrivateProject(Integer personID) {
		return ProjectBL.loadPrivateProject(personID);
	}
	
	/**
	 * Loads the details for a new/existing project 
	 * @param projectID
	 * @param add
	 * @param locale
	 * @return
	 */
	static String getProjectDetail(Integer projectID, boolean add, boolean addAsSubproject,
			boolean addAsPrivateProject, TPersonBean personBean, Locale locale) {
		boolean isSubproject = false;
		ProjectBaseTO projectBaseTO = new ProjectBaseTO();
		ProjectAccountingTO projectAccountingTO = null;
		ProjectDefaultsTO projectDefaultsTO = new ProjectDefaultsTO();


		TProjectBean projectBean = null;
		Integer projectTypeID = null;
		List<ILabelBean> projectTypeBeans = null;
		if (add) {
			//add project
			Integer parentProjectID = null;
			if (addAsSubproject) {
				//get parent project to inherit from
				projectBean = ProjectBL.loadByPrimaryKey(projectID);
				parentProjectID = projectBean.getProjectType();
			}
			if (projectBean==null) {
				projectBean = new TProjectBean();
			}
			if (addAsPrivateProject) {
				//get private projectTypes
				projectTypeBeans = (List)ProjectTypesBL.loadPrivate();
			} else {
				//get non private projectTypes
				projectTypeBeans = ProjectTypeChildrenAssignmentFacade.getInstance().getPossibleBeans(parentProjectID, null, locale);
			}	
		} else {
			projectBean = ProjectBL.loadByPrimaryKey(projectID);
			if (projectBean!=null) {
				isSubproject = projectBean.getParent()!=null;
				projectTypeID = projectBean.getProjectType();
				if (projectTypeID!=null && projectTypeID<0) {
					//get private project types
					projectTypeBeans = (List)ProjectTypesBL.loadPrivate();
				} else {
					//get non private project types
					Integer parentProjectTypeID = null;
					if (isSubproject) {
						Integer parentProjectID = projectBean.getParent();
						TProjectBean parentProjectBean = LookupContainer.getProjectBean(parentProjectID);
						if (parentProjectBean!=null) {
							parentProjectTypeID = parentProjectBean.getProjectType();
						}
					}
					projectTypeBeans = ProjectTypeChildrenAssignmentFacade.getInstance().getPossibleBeans(parentProjectTypeID, projectTypeID, locale);
				}
			}
		}
		
		if (projectTypeID==null) {
			if (projectTypeBeans!=null && !projectTypeBeans.isEmpty()) {
				projectTypeID = projectTypeBeans.get(0).getObjectID();
			}
		}
		projectBaseTO.setProjectTypeID(projectTypeID);
		projectBaseTO.setProjectTypeList(projectTypeBeans);
		List<TPersonBean> managerBeans = null;
		List<TPersonBean> responsibleBeans = null;
		if (addAsPrivateProject || projectTypeID<0) {
			//for new or existing private project limit the managers and responsibles to the current person 
			managerBeans = new LinkedList<TPersonBean>();
			responsibleBeans = new LinkedList<TPersonBean>();
			managerBeans.add(personBean);
			responsibleBeans.add(personBean);
		} else {
			//initialize project defaults
			managerBeans = PersonBL.loadActivePersons();
			responsibleBeans = PersonBL.loadActivePersonsAndGroups();
		}
		projectDefaultsTO.setManagerList(managerBeans);
		projectDefaultsTO.setResponsibleList(responsibleBeans);
		Map<Integer, String> invalidDefaults = setSystemLists(projectBean, projectDefaultsTO, projectTypeID, locale, false);
		List<TSystemStateBean> projectStatusList = SystemStatusBL.getStatusOptions(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, locale);
		projectBaseTO.setProjectStatusList(projectStatusList);
		TSiteBean siteBean = SiteConfigBL.loadTSite();
		if (siteBean!=null) {
			projectBaseTO.setHasPrefix(siteBean.getProjectSpecificIDsOn());
		}
		if (projectTypeID!=null) {
			TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
			if (projectTypeBean!=null) {
				projectBaseTO.setHasRelease(projectTypeBean.getUseReleases());
				boolean projectTypeHasAccountig = projectTypeBean.getUseAccounting();
				if (!ApplicationBean.getInstance().isGenji() && projectTypeHasAccountig) {
					projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID, add, addAsSubproject, false, projectTypeBean, false);
					invalidDefaults.putAll(ProjectBL.setAccountList(projectID, projectAccountingTO, add, addAsSubproject, locale));
					projectAccountingTO.setWorkUnitList(AccountingBL.getTimeUnitOptionsList(locale));
				}
			}
		}
		if (add) {
			//project base
			if (addAsPrivateProject) {
				projectBaseTO.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.lbl.privateProjectName", locale));
			}
			Integer projectStatus = null;
			Integer defaultManager = null;
			Integer defaultResponsible = null;
			Integer prefillBy = null;
			if (addAsSubproject) {
				//inherit from parent project
				projectStatus = projectBean.getStatus();
				String moreProps = projectBean.getMoreProps();
				projectBaseTO.setLinking(projectBean.isLinkingActive());
				projectBaseTO.setUseRelease(PropertiesHelper.getBooleanProperty(moreProps, TProjectBean.MOREPPROPS.USE_RELEASE));
				prefillBy = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.PREFILL);
				defaultManager = projectBean.getDefaultManagerID();
				defaultResponsible = projectBean.getDefaultOwnerID();
			}
			if (projectStatus==null) {
				if (projectStatusList!=null && !projectStatusList.isEmpty()) {
					projectStatus = projectStatusList.get(0).getObjectID();
				}
			}
			projectBaseTO.setProjectStatusID(projectStatus);
			if (defaultManager==null) {
				if (managerBeans!=null && !managerBeans.isEmpty()) {
					defaultManager = managerBeans.get(0).getObjectID();
				}
			}
			projectDefaultsTO.setDefaultManagerID(defaultManager);
			if (defaultResponsible==null) {
				if (responsibleBeans!=null && !responsibleBeans.isEmpty()) {
					defaultResponsible = responsibleBeans.get(0).getObjectID();
				}
			}
			projectDefaultsTO.setDefaultResponsibleID(defaultResponsible);
			if (prefillBy==null) {
				prefillBy = Integer.valueOf(TProjectBean.PREFILL.LASTWORKITEM);
			}
			projectDefaultsTO.setPrefillBy(prefillBy);


		} else {
			//project base
			String moreProps = projectBean.getMoreProps();
			projectBaseTO.setLabel(projectBean.getLabel());
			projectBaseTO.setDescription(projectBean.getDescription());
			projectBaseTO.setProjectTypeID(projectBean.getProjectType());
			projectBaseTO.setProjectStatusID(projectBean.getStatus());
			projectBaseTO.setLinking(PropertiesHelper.getBooleanProperty(moreProps, TProjectBean.MOREPPROPS.LINKING_PROPERTY));
			projectBaseTO.setPrefix(projectBean.getPrefix());
			projectBaseTO.setUseRelease(PropertiesHelper.getBooleanProperty(moreProps, TProjectBean.MOREPPROPS.USE_RELEASE));
			//project defaults
			//default manager
			Integer defaultManager = projectBean.getDefaultManagerID();
			if (defaultManager==null) {
				if (managerBeans!=null && !managerBeans.isEmpty()) {
					defaultManager = managerBeans.get(0).getObjectID();
				}
			} else {
				boolean found = foundInList((List)managerBeans, defaultManager);
				if (!found) {
					TPersonBean defaultManagerPerson = LookupContainer.getPersonBean(defaultManager);
					if (defaultManagerPerson!=null) {
						//person was deactivated add forced to the list  
						managerBeans.add(0, defaultManagerPerson);
						Object[] arguments = new Object[] {
								defaultManagerPerson.getLabel(),
								FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_MANAGER, locale)};
						invalidDefaults.put(SystemFields.INTEGER_MANAGER,
								LocalizeUtil.getParametrizedString("admin.project.err.invalidPerson", arguments, locale));
					}
				}
			}
			projectDefaultsTO.setDefaultManagerID(defaultManager);
			//default responsible
			Integer defaultResponsible = projectBean.getDefaultOwnerID();
			if (defaultResponsible==null) {
				if (responsibleBeans!=null && !responsibleBeans.isEmpty()) {
					defaultResponsible = responsibleBeans.get(0).getObjectID();
				}
			} else {
				boolean found = foundInList((List)responsibleBeans, defaultResponsible);
				if (!found) {
					TPersonBean defaultResponsiblePerson = LookupContainer.getPersonBean(defaultResponsible);
					if (defaultResponsiblePerson!=null) {
						//person was deactivated add forced to the list
						responsibleBeans.add(0, defaultResponsiblePerson);
						Object[] arguments = new Object[] {
								defaultResponsiblePerson.getLabel(),
								FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_RESPONSIBLE, locale)};
						invalidDefaults.put(SystemFields.INTEGER_RESPONSIBLE,
								LocalizeUtil.getParametrizedString("admin.project.err.invalidPerson", arguments, locale));
					}
				}
			}
			projectDefaultsTO.setDefaultResponsibleID(defaultResponsible);
			Integer prefillBy = getPrefillValue(projectBean);
			if (prefillBy==null) {
				prefillBy = Integer.valueOf(TProjectBean.PREFILL.LASTWORKITEM);
			}
			projectDefaultsTO.setPrefillBy(prefillBy);

		}
		return ProjectJSON.loadProjectDetailJSON(isSubproject, projectBaseTO, projectAccountingTO, projectDefaultsTO, invalidDefaults
		);
	}

	/**
	 * Refresh the project accounting/release/system lists after projectType change 
	 * @param add
	 * @param addAsSubproject
	 * @param projectID
	 * @param submittedAccountingInherited
	 * @param projectDefaultsTO
	 * @param projectTypeID
	 * @param locale
	 */
	static String getProjectTypeChangeDetail(boolean add, boolean addAsSubproject, Integer projectID,
			boolean submittedAccountingInherited, ProjectDefaultsTO projectDefaultsTO,
			Integer projectTypeID, Locale locale) {
		TProjectBean projectBean;
		if (add) {
			projectBean = new TProjectBean();
		} else {
			projectBean = ProjectBL.loadByPrimaryKey(projectID);
		}
		TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
		boolean projectTypeHasAccountig = false;
		boolean hasReleases = false;
		ProjectAccountingTO projectAccountingTO = null;
		Map<Integer, String> invalidDefaults = new HashMap<Integer, String>();
		if (projectTypeBean!=null) {
			projectTypeHasAccountig = projectTypeBean.getUseAccounting();
			if (!ApplicationBean.getInstance().isGenji() && projectTypeHasAccountig) {
				projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID, add,
						addAsSubproject, true, projectTypeBean, submittedAccountingInherited);
				invalidDefaults.putAll(ProjectBL.setAccountList(projectID, projectAccountingTO, add, addAsSubproject, locale));
				projectAccountingTO.setWorkUnitList(AccountingBL.getTimeUnitOptionsList(locale));
			}
			hasReleases = projectTypeBean.getUseReleases();
		}
		invalidDefaults.putAll(setSystemLists(projectBean, projectDefaultsTO, projectTypeID, locale, true));
		return ProjectJSON.loadProjectTypeChangeJSON(projectAccountingTO, hasReleases, projectDefaultsTO, invalidDefaults);
		
	}
	
	/**
	 * Sets the list entries and list values for system lists
	 * @param projectBean
	 * @param projectDefaultsTO
	 * @param projectTypeID
	 * @param locale
	 * @param afterProjectTypeChange whether this is called after a projectType change or after a project load
	 * @return
	 */
	static Map<Integer, String> setSystemLists(TProjectBean projectBean, ProjectDefaultsTO projectDefaultsTO,
			Integer projectTypeID, Locale locale, boolean afterProjectTypeChange) {
		Map<Integer, String> invalidFieldDefaults = new HashMap<Integer, String>();
		Integer projectID = projectBean.getObjectID();
		String moreProps = projectBean.getMoreProps();
		//issue type
		Integer issueTypeID = null;
		if (afterProjectTypeChange) {
			issueTypeID = projectDefaultsTO.getDefaultIssueTypeID();
		} else {
			issueTypeID = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.DEFAULT_ISSUETYPE);
		}
		List<ILabelBean> allowedIssueTypeBeans = ProjectTypeItemTypeAssignmentFacade.getInstance().getPossibleBeans(projectTypeID, issueTypeID, locale);
		projectDefaultsTO.setIssueTypeList(allowedIssueTypeBeans);
		if (allowedIssueTypeBeans!=null && !allowedIssueTypeBeans.isEmpty()) {
			if (issueTypeID==null) {
				//either new or existing but the initial status was not yet set
				issueTypeID = allowedIssueTypeBeans.get(0).getObjectID();
			} else {
				//existing project, default issueType was set
				boolean issueTypeValid = foundInList(allowedIssueTypeBeans, issueTypeID);
				if (!issueTypeValid) {
					if (afterProjectTypeChange) {
						//not found after projectType change, set the first valid one
						issueTypeID = allowedIssueTypeBeans.get(0).getObjectID();
					} else {
						//by project loading leave the original value but mark as erroneous
						TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(issueTypeID, locale);
						if (listTypeBean!=null) {
							allowedIssueTypeBeans.add(0, listTypeBean);
							TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
							Object[] arguments = new Object[] {
									FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUETYPE, locale),
									LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale),
									projectTypeBean.getLabel(),
									listTypeBean.getLabel()};
							invalidFieldDefaults.put(SystemFields.INTEGER_ISSUETYPE, 
									LocalizeUtil.getParametrizedString("admin.project.err.invalidSystemListEntry", arguments, locale));
						}
					}
				}
			}
			projectDefaultsTO.setDefaultIssueTypeID(issueTypeID);
		}
		
		//status
		Integer statusID = null;
		if (afterProjectTypeChange) {
			statusID = projectDefaultsTO.getInitialStatusID();
		} else {
			statusID = projectBean.getDefaultInitStateID();
		}
		List<ILabelBean> allowedStatusBeans = ProjectTypeItemTypeStatusAssignmentFacade.getInstance().getPossibleBeans(projectTypeID, issueTypeID, statusID, locale);
		projectDefaultsTO.setStatusList(allowedStatusBeans);
		if (allowedStatusBeans!=null && !allowedStatusBeans.isEmpty()) {
			if (statusID==null) {
				//either new or existing but the initial status was not yet set
				statusID = allowedStatusBeans.get(0).getObjectID();
			} else {
				//existing projectBean, status was set previously				
				boolean statusValid = foundInList(allowedStatusBeans, statusID);
				if (!statusValid) {
					if (afterProjectTypeChange) {
						//not found after projectType change, set the first valid one
						statusID = allowedStatusBeans.get(0).getObjectID();
					} else {
						//by project loading leave the original value but mark as erroneous
						TStateBean stateBean = LookupContainer.getStatusBean(statusID);
						if (stateBean!=null) {
							allowedStatusBeans.add(0, stateBean);
							TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
							Object[] arguments = new Object[] {
									FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_STATE, locale),
									LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale),
									projectTypeBean.getLabel(),
									stateBean.getLabel()};
							invalidFieldDefaults.put(SystemFields.INTEGER_STATE, 
									LocalizeUtil.getParametrizedString("admin.project.err.invalidSystemListEntry", arguments, locale));
						}
					}
				}
			}
			projectDefaultsTO.setInitialStatusID(statusID);
		}
		
		Map<Integer, Integer> issueTypeToInitStatusMap = new HashMap<Integer, Integer>();
		Map<Integer, List<ILabelBean>> issueTypeToStatusListMap = new HashMap<Integer, List<ILabelBean>>();
		List<TInitStateBean> initalStates = initStatusDAO.loadByProjectAndIssueTypes(projectID,
				GeneralUtils.createIntegerListFromBeanList(allowedIssueTypeBeans));
		for (ILabelBean itemTypeBean : allowedIssueTypeBeans) {
			Integer initIssueTypeID = itemTypeBean.getObjectID();//initStateBean.getListType();
			Integer initStatusID = getInitStatus(initalStates, projectID, initIssueTypeID);
			allowedStatusBeans = ProjectTypeItemTypeStatusAssignmentFacade.getInstance().getPossibleBeans(projectTypeID, initIssueTypeID, initStatusID, locale);
			//existing projectBean, status was set previously
			boolean statusValid = foundInList(allowedStatusBeans, initStatusID);
			if (!statusValid) {
				if (afterProjectTypeChange) {
					//not found after projectType change, set the first valid one
					initStatusID = allowedStatusBeans.get(0).getObjectID();
					issueTypeToInitStatusMap.put(initIssueTypeID, initStatusID);
				} else {
					//by project loading leave the original value but mark as erroneous
					TStateBean stateBean = LookupContainer.getStatusBean(initStatusID);
					if (stateBean!=null) {
						allowedStatusBeans.add(0, stateBean);
						TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
						Object[] arguments = new Object[] {
								FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_STATE, locale),
								LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale),
								projectTypeBean.getLabel(),
								stateBean.getLabel()};
						invalidFieldDefaults.put(SystemFields.INTEGER_STATE, 
								LocalizeUtil.getParametrizedString("admin.project.err.invalidSystemListEntry", arguments, locale));
					}
				}
			} else {
				issueTypeToInitStatusMap.put(initIssueTypeID, initStatusID);
			}
			issueTypeToStatusListMap.put(initIssueTypeID, allowedStatusBeans);
		}
		projectDefaultsTO.setIssueTypeToInitStatusMap(issueTypeToInitStatusMap);
		projectDefaultsTO.setIssueTypeToStatusListMap(issueTypeToStatusListMap);
		
		//priorities	
		Integer priorityID = null;
		if (afterProjectTypeChange) {
			priorityID = projectDefaultsTO.getDefaultPriorityID();
		} else {
			priorityID = PropertiesHelper.getIntegerProperty(moreProps,TProjectBean.MOREPPROPS.DEFAULT_PRIORITY);
		}
		List<ILabelBean> allowedPriorityBeans = ProjectTypeItemTypePriorityAssignmentFacade.getInstance().getPossibleBeans(projectTypeID, issueTypeID, priorityID, locale);
		projectDefaultsTO.setPriorityList(allowedPriorityBeans);
		if (allowedPriorityBeans!=null && !allowedPriorityBeans.isEmpty()) {
			if (priorityID==null) {
				//either new or existing but the initial priority was not yet set
				priorityID = allowedPriorityBeans.get(0).getObjectID();
			} else {
				//existing projectBean, priority was set previously				
				boolean priorityValid = foundInList(allowedPriorityBeans, priorityID);
				if (!priorityValid) {
					if (afterProjectTypeChange) {
						//not found after projectType change, set the first valid one
						priorityID = allowedPriorityBeans.get(0).getObjectID();
					} else {
						//by project loading leave the original value but mark as erroneous
						TPriorityBean priorityBean = LookupContainer.getPriorityBean(priorityID, locale);
						if (priorityBean!=null) {
							allowedPriorityBeans.add(0, priorityBean);
							TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
							Object[] arguments = new Object[] {
									FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PRIORITY, locale),
									LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale),
									projectTypeBean.getLabel(),
									priorityBean.getLabel()};
							invalidFieldDefaults.put(SystemFields.INTEGER_PRIORITY, 
									LocalizeUtil.getParametrizedString("admin.project.err.invalidSystemListEntry", arguments, locale));
						}
					}
				}
			}
			projectDefaultsTO.setDefaultPriorityID(priorityID);
		}
		
		//severities
		Integer severityID = null;
		if (afterProjectTypeChange) {
			severityID = projectDefaultsTO.getDefaultSeverityID();
		} else {
			severityID = PropertiesHelper.getIntegerProperty(moreProps, TProjectBean.MOREPPROPS.DEFAULT_SEVERITY);
		}
		List<ILabelBean> allowedSeverityBeans = ProjectTypeItemTypeSeverityAssignmentFacade.getInstance().getPossibleBeans(projectTypeID, issueTypeID, severityID, locale);
		projectDefaultsTO.setSeverityList(allowedSeverityBeans);
		if (allowedSeverityBeans!=null && !allowedSeverityBeans.isEmpty()) {
			if (severityID==null) {
				//either new or existing but the initial severity was not yet set
				severityID = allowedSeverityBeans.get(0).getObjectID();
			} else {
				//existing projectBean, severity was set previously
				boolean severityValid = foundInList(allowedSeverityBeans, severityID);
				if (!severityValid) {
					if (afterProjectTypeChange) {
						//not found after projectType change, set the first valid one
						severityID = allowedSeverityBeans.get(0).getObjectID();
					} else {
						//by project loading leave the original value but mark as erroneous
						TSeverityBean severityBean = LookupContainer.getSeverityBean(severityID, locale);
						if (severityBean!=null) {
							allowedSeverityBeans.add(0, severityBean);
							TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
							Object[] arguments = new Object[] {
									FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_SEVERITY, locale),
									LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale),
									projectTypeBean.getLabel(),
									severityBean.getLabel()};
							invalidFieldDefaults.put(SystemFields.INTEGER_SEVERITY, 
									LocalizeUtil.getParametrizedString("admin.project.err.invalidSystemListEntry", arguments, locale));
						}
					}
				}
			}
			projectDefaultsTO.setDefaultSeverityID(severityID);
		}
		return invalidFieldDefaults;
	}
	
	/**
	 * Whether an entry is found in a list 
	 * @return
	 */
	private static Integer getInitStatus(List<TInitStateBean> initStateBeans, Integer projectID, Integer itemTypeID) {
		if (initStateBeans!=null && projectID!=null && itemTypeID!=null) {
			for (TInitStateBean initStateBean : initStateBeans) {
				Integer assignedProjectID = initStateBean.getProject();
				Integer assignedItemTypeID = initStateBean.getListType();
				Integer assignedInitStatusID = initStateBean.getStateKey();
				if (projectID.equals(assignedProjectID) && itemTypeID.equals(assignedItemTypeID)) {
					return assignedInitStatusID;
				}
			}
		}
		return null;
	}
	
	/**
	 * Validates the user entered project details
	 * @param projectID
	 * @param add
	 * @param addAsSubproject
	 * @param projectBaseTO
	 * @param locale
	 * @return
	 */
	static List<ControlError> validateProjectDetail(Integer projectID, boolean add, boolean addAsSubproject,
			ProjectBaseTO projectBaseTO,Locale locale) {
		List<ControlError> errors=new ArrayList<ControlError>();
		Integer projectStatusNew = projectBaseTO.getProjectStatusID();
		Integer statusFlagNew = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, projectStatusNew);
		boolean projectNameExists=false;
		boolean projectPrefixExists = false;
		TProjectBean projectBean;
		if (add) {
			projectBean=new TProjectBean();
			if (addAsSubproject) {
				validateChildStatus(add, statusFlagNew, projectID, locale, errors);
				projectNameExists = ProjectBL.projectNameExists(projectBaseTO.getLabel(), projectID, null);
			} else {
				projectNameExists = ProjectBL.projectNameExists(projectBaseTO.getLabel(), null, null);
			}
		} else {
			projectBean = ProjectBL.loadByPrimaryKey(projectID);
			if (projectBean==null) {
				projectBean = new TProjectBean();
			}
			//validateChildStatus(add, statusFlagNew, projectBean.getParent(), locale, errors);
			projectNameExists = ProjectBL.projectNameExists(projectBaseTO.getLabel(), projectBean.getParent(), projectID);
			if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
				String prefix = projectBaseTO.getPrefix();
				if (prefix!=null && !"".equals(prefix)) {
					projectPrefixExists = ProjectBL.projectPrefixExists(prefix, projectBean.getParent(), projectID);
				}
			}
		}
		if (projectNameExists) {
			List<String> controlPath = new LinkedList<String>();
			controlPath.add("mainTab");
			controlPath.add("fsbasic");
			controlPath.add("projectBaseTO.label");
			errors.add(new ControlError(controlPath, LocalizeUtil.getParametrizedString("common.err.duplicateName",
					new Object[] {FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale)}, locale)));
		}
		if (projectPrefixExists) {
			List<String> controlPath = new LinkedList<String>();
			controlPath.add("mainTab");
			controlPath.add("fsprefix");
			controlPath.addAll(JSONUtility.getPathInHelpWrapper("projectBaseTO.prefix"));
			errors.add(new ControlError(controlPath, LocalizeUtil.getParametrizedString("common.err.duplicatePrefix",
					new Object[] {FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale)}, locale)));
		}
		return errors;
	}
	
	/**
	 * Validates the child status depending on parent status
	 * @param add
	 * @param statusFlagChild
	 * @param parentProjectID
	 * @param locale
	 * @param errors
	 */
	private static void validateChildStatus(boolean add, Integer statusFlagChild, Integer parentProjectID, Locale locale, List<ControlError> errors) {
		if (parentProjectID!=null && statusFlagChild!=null && statusFlagChild.intValue()!=TSystemStateBean.STATEFLAGS.CLOSED) {
			TProjectBean parentProject = ProjectBL.loadByPrimaryKey(parentProjectID);
			if (parentProject!=null) {
				Integer parentProjectStatus = parentProject.getStatus();
				if (parentProjectStatus!=null) {
					Integer parentStatusFlag = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, parentProjectStatus);
					if (parentStatusFlag!=null) {
						if (parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.CLOSED) {
							List<String> controlPath = new LinkedList<String>();
							controlPath.add("mainTab");
							controlPath.add("fsbasic");
							controlPath.add("projectStatus");
							String errorKey = null;
							if (add) {
								errorKey = "admin.project.err.addOpenChildToClosedParent";
							} else {
								errorKey = "admin.project.err.closedDescendant";
							}
							errors.add(new ControlError(controlPath, LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale)));
						} else {
							if (parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE &&
									statusFlagChild.intValue()==TSystemStateBean.STATEFLAGS.ACTIVE) {
								List<String> controlPath = new LinkedList<String>();
								controlPath.add("mainTab");
								controlPath.add("fsbasic");
								controlPath.add("projectStatus");
								String errorKey = null;
								if (add) {
									errorKey = "admin.project.err.addActiveChildToInactiveParent";
								} else {
									errorKey = "admin.project.err.activChildInInactiveParent";
								}
								errors.add(new ControlError(controlPath, LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale)));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Validates the child status depending on parent status
	 */
	private static String validateStatus(boolean confirmSave, TProjectBean projectBean, Integer projectStatusOld, Integer projectStatusNew, Locale locale) {
		if (projectStatusOld!=null && projectStatusNew!=null && !projectStatusOld.equals(projectStatusNew)) {
			Integer statusFlagOld = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, projectStatusOld);
			Integer statusFlagNew = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, projectStatusNew);
			if (statusFlagOld!=null && statusFlagNew!=null && EqualUtils.notEqual(statusFlagOld, statusFlagNew)) {
				boolean notClosedToClosed = statusFlagOld.intValue()!=TSystemStateBean.STATEFLAGS.CLOSED &&
						statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.CLOSED;
				boolean activeToInactive = statusFlagOld.intValue()==TSystemStateBean.STATEFLAGS.ACTIVE &&
						statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE;
				boolean closedToNotClosed = statusFlagOld.intValue()==TSystemStateBean.STATEFLAGS.CLOSED &&
						statusFlagNew.intValue()!=TSystemStateBean.STATEFLAGS.CLOSED;
				boolean inactiveToActive = statusFlagOld.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE &&
						statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.ACTIVE;
				Integer projectID = projectBean.getObjectID();
				if (notClosedToClosed || activeToInactive) {
					List<TProjectBean> inconsistenSubrojects = null;
					if (projectID!=null) {
						if (notClosedToClosed) {
							inconsistenSubrojects = ProjectBL.loadActiveInactiveSubrojects(projectID);
						} else {
							inconsistenSubrojects = ProjectBL.loadActiveSubrojects(projectID);
						}
					}
					if (inconsistenSubrojects!=null && !inconsistenSubrojects.isEmpty()) {
						if (confirmSave) {
							for (TProjectBean childProjectBean : inconsistenSubrojects) {
								changeDescendantsStatus(childProjectBean, activeToInactive, projectStatusNew);
							}
						} else {
							String titleKey = null;
							String errorReasonKey = null;
							String errorResolutionKey = null;
							if (notClosedToClosed) {
								titleKey = "admin.project.err.closeOpenDescendantTitle";
								errorReasonKey = "admin.project.err.closedDescendant";
								errorResolutionKey = "admin.project.err.closeOpenDescendant";
							} else {
								titleKey = "admin.project.err.deactivateActivDescendantTitle";
								errorReasonKey = "admin.project.err.activChildInInactiveParent";
								errorResolutionKey = "admin.project.err.deactivateActivDescendant";
							}
							return ReleaseJSON.saveFailureJSON(JSONUtility.EDIT_ERROR_CODES.NEED_CONFIRMATION,
									LocalizeUtil.getLocalizedTextFromApplicationResources(
											titleKey, locale),
									LocalizeUtil.getLocalizedTextFromApplicationResources(
											errorReasonKey, locale)	+ " " +
									LocalizeUtil.getLocalizedTextFromApplicationResources(
											errorResolutionKey, locale));
						}
					}
				} else {
					if (closedToNotClosed || inactiveToActive) {
						Integer parentProjectID = projectBean.getParent();
						if (parentProjectID!=null) {
							TProjectBean parentProjectBean = ProjectBL.loadByPrimaryKey(parentProjectID);
							if (parentProjectBean!=null) {
								Integer projectStatusParent = parentProjectBean.getStatus();
								if (projectStatusParent!=null) {
									Integer parentStatusFlag = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, projectStatusParent);
									boolean inconsistentParent = false;
									if (closedToNotClosed) {
										inconsistentParent =  parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.CLOSED;
									} else {
										inconsistentParent =  parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE;
									}
									if (inconsistentParent) {
										if (confirmSave) {
											changeAscendentsStatus(parentProjectBean, inactiveToActive, projectStatusNew);
										} else {
											String titleKey = null;
											String errorReasonKey = null;
											String errorResolutionKey = null;
											if (closedToNotClosed) {
												titleKey = "admin.project.err.openClosedAncestorsTitle";
												errorReasonKey = "admin.project.err.closedDescendant";
												errorResolutionKey = "admin.project.err.openClosedAncestors";
											} else {
												titleKey = "admin.project.err.activateInactiveAscendentTitle";
												errorReasonKey = "admin.project.err.activChildInInactiveParent";
												errorResolutionKey = "admin.project.err.activateInactiveAscendent";
											}
											return ReleaseJSON.saveFailureJSON(JSONUtility.EDIT_ERROR_CODES.NEED_CONFIRMATION,
												LocalizeUtil.getLocalizedTextFromApplicationResources(
														titleKey, locale),
												LocalizeUtil.getLocalizedTextFromApplicationResources(
														errorReasonKey, locale)	+ " " +	
												LocalizeUtil.getLocalizedTextFromApplicationResources(
														errorResolutionKey, locale));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Saves a new/existing project
	 * @param projectID
	 * @param add
	 * @param addAsSubproject
	 * @param projectBaseTO
	 * @param projectDefaultsTO
	 * @param confirmSave
	 * @param locale
	 * @return
	 */
	static String saveProjectDetail(Integer projectID, boolean add,
			boolean addAsSubproject, boolean addAsPrivateProject,
			ProjectBaseTO projectBaseTO, ProjectAccountingTO projectAccountingTO, ProjectDefaultsTO projectDefaultsTO,
			boolean confirmSave, boolean isTemplate, Integer personID, Locale locale) {
		TProjectBean projectBean = null;
		//whether the root or a branch should be reloaded after project save
		boolean reloadProjectTree = false;
		boolean prefixChanged = false;
		//if null (and reloadTree is true) reload the root node
		String projectNodeToReload = null;
		//which project to select after save: set only if reloadTree==true
		//if this is not null a reload is made in the client 
		String projectNodeToSelect = null;
		Integer projectStatusOld = null;
		Integer projectStatusNew = projectBaseTO.getProjectStatusID();
		if (add) {
			reloadProjectTree=true;
			//prefixChanged = true;
			//projectTypeChanged = true;
			projectBean = new TProjectBean();
			if (addAsSubproject) {
				if (projectID!=null) {
					projectBean.setParent(projectID);
					projectNodeToReload = projectID.toString();
				}
			}
			if (addAsPrivateProject) {
				projectBean.setPrivate(addAsPrivateProject);
			}
		} else {
			projectBean = ProjectBL.loadByPrimaryKey(projectID);
			if (projectBean==null) {
				projectBean = new TProjectBean();
			}
			prefixChanged = EqualUtils.notEqual(projectBean.getPrefix(), projectBaseTO.getPrefix());
			if (EqualUtils.notEqual(projectBean.getLabel(), projectBaseTO.getLabel()) ||
					EqualUtils.notEqual(projectBean.getStatus(), projectBaseTO.getProjectStatusID())) {
				//either name or project status changed: change the node in the projects tree: name or icon
				reloadProjectTree=true;
				Integer parentProjectID = projectBean.getParent();
				if (parentProjectID!=null) {
					projectNodeToReload = parentProjectID.toString(); 
				}
			}
			projectStatusOld = projectBean.getStatus();
			//test the parent statuses
			String needReplaceMessage = validateStatus(confirmSave, projectBean, projectStatusOld, projectStatusNew, locale);
			if (needReplaceMessage!=null) {
				return needReplaceMessage;
			}
		}
		//projectBase
		projectBean.setLabel(projectBaseTO.getLabel());
		projectBean.setDescription(projectBaseTO.getDescription());
		Integer newProjectType = projectBaseTO.getProjectTypeID();
		Integer oldProjectType = projectBean.getProjectType();
		projectBean.setProjectType(newProjectType);
		if (newProjectType<0) {
			projectBean.setPrivate(true);
		}
		projectBean.setStatus(projectBaseTO.getProjectStatusID());
		if(projectBaseTO.getProjectStatusID()==null&&add&&isTemplate){
			projectBean.setStatus(ProjectCopyBL.getProjectStatusActive());
		}

		Properties properties = PropertiesHelper.getProperties(projectBean.getMoreProps());
		PropertiesHelper.setBooleanProperty(properties, TProjectBean.MOREPPROPS.LINKING_PROPERTY, projectBaseTO.isLinking());
		boolean newUseRelease = projectBaseTO.isUseRelease();
		boolean oldUseRelease = PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.USE_RELEASE);
		//whether to show release/account/version control nodes
		boolean reloadProjectConfigTree = newUseRelease!=oldUseRelease || EqualUtils.notEqual(newProjectType, oldProjectType);
		String projectConfigTypeNodeToSelect = String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.GENERAL);
		PropertiesHelper.setBooleanProperty(properties, TProjectBean.MOREPPROPS.USE_RELEASE, newUseRelease);
		projectBean.setPrefix(projectBaseTO.getPrefix());
		//project accounting
		if (projectAccountingTO==null) {
			projectAccountingTO = new ProjectAccountingTO();
		}
		PropertiesHelper.setBooleanProperty(properties, TProjectBean.MOREPPROPS.ACCOUNTING_INHERITED,
				projectAccountingTO.isAccountingInherited());
		boolean oldWorkTracking = PropertiesHelper.getBooleanProperty(properties, TProjectBean.MOREPPROPS.WORK_TRACKING);
		boolean newWorkTracking = projectAccountingTO.isWorkTracking();
		PropertiesHelper.setBooleanProperty(properties, TProjectBean.MOREPPROPS.WORK_TRACKING, newWorkTracking);
		PropertiesHelper.setIntegerProperty(properties, TProjectBean.MOREPPROPS.WORK_DEFAULT_UNIT,
				projectAccountingTO.getDefaultWorkUnit());
		projectBean.setHoursPerWorkDay(projectAccountingTO.getHoursPerWorkday());
		boolean oldCostTracking =PropertiesHelper.getBooleanProperty(properties, TProjectBean.MOREPPROPS.COST_TRACKING);
		boolean newCostTracking = projectAccountingTO.isCostTracking(); 
		PropertiesHelper.setBooleanProperty(properties, TProjectBean.MOREPPROPS.COST_TRACKING, newCostTracking);
		projectBean.setCurrencyName(projectAccountingTO.getCurrencyName());
		projectBean.setCurrencySymbol(projectAccountingTO.getCurrencySymbol());
		PropertiesHelper.setIntegerProperty(properties, TProjectBean.MOREPPROPS.DEFAULT_ACCOUNT,
				projectAccountingTO.getDefaultAccount());
		if (!reloadProjectConfigTree) {
			reloadProjectConfigTree = (oldWorkTracking || oldCostTracking)!=(newWorkTracking || newCostTracking);
		}
		
		//project defaults
		projectBean.setDefaultManagerID(projectDefaultsTO.getDefaultManagerID());
		projectBean.setDefaultOwnerID(projectDefaultsTO.getDefaultResponsibleID());
		projectBean.setDefaultInitStateID(projectDefaultsTO.getInitialStatusID());
		projectBean.setIsTemplate(BooleanFields.fromBooleanToString(isTemplate));
		PropertiesHelper.setIntegerProperty(properties, TProjectBean.MOREPPROPS.DEFAULT_ISSUETYPE,
				projectDefaultsTO.getDefaultIssueTypeID());
		PropertiesHelper.setIntegerProperty(properties, TProjectBean.MOREPPROPS.DEFAULT_PRIORITY,
				projectDefaultsTO.getDefaultPriorityID());
		PropertiesHelper.setIntegerProperty(properties, TProjectBean.MOREPPROPS.DEFAULT_SEVERITY,
				projectDefaultsTO.getDefaultSeverityID());
		PropertiesHelper.setIntegerProperty(properties, TProjectBean.MOREPPROPS.PREFILL,
				projectDefaultsTO.getPrefillBy());


		projectBean.setMoreProps(PropertiesHelper.serializeProperties(properties));

		projectID = ProjectBL.save(projectBean);
		if (prefixChanged) {
			//update the lucene index for workItems from project if prefix was changed
			int[] workItemIDsArr = workItemDAO.loadBySystemList(SystemFields.PROJECT, projectID);
			LuceneIndexer.updateWorkItemIndex(workItemIDsArr, SystemFields.INTEGER_PROJECT);
			ClusterMarkChangesBL.markDirtyWorkItemsInCluster(workItemIDsArr);
		}
		if (projectID!=null) {
			//should be specified only if reload of branch is needed
			projectNodeToSelect = projectID.toString();
		}
		
		/**
		 * Issue type specific initial statuses
		 */
		Map<Integer, Integer> issueTypeToInitStatusMap = projectDefaultsTO.getIssueTypeToInitStatusMap();
		if (issueTypeToInitStatusMap==null) {
			issueTypeToInitStatusMap = new HashMap<Integer, Integer>();
		}
		Map<Integer, Integer> issueTypeNegativeToInitStatusMap = projectDefaultsTO.getIssueTypeNegativeToInitStatusMap();
		for (Integer issueTypeID : issueTypeNegativeToInitStatusMap.keySet()) {
			//turn back to negative the values turned at positive on the client (due to struts2 bug with negative numbers)
			issueTypeToInitStatusMap.put(Integer.valueOf(-issueTypeID.intValue()), issueTypeNegativeToInitStatusMap.get(issueTypeID));
		}
		
		//get the existing ones
		List<TInitStateBean> initStates = initStatusDAO.loadByProject(projectID);
		for (TInitStateBean initStateBean : initStates) {
			Integer existingIssueTypeID = initStateBean.getListType();
			Integer existingStatus = initStateBean.getStateKey();
			if (issueTypeToInitStatusMap.containsKey(existingIssueTypeID)) {
				Integer newStatus = issueTypeToInitStatusMap.get(existingIssueTypeID);
				//issue type specific initial status changed
				if (EqualUtils.notEqual(existingStatus, newStatus)) {
					initStateBean.setStateKey(newStatus);
					initStatusDAO.save(initStateBean);
				}
				issueTypeToInitStatusMap.remove(existingIssueTypeID);
			} else {
				//issue type specific initial status removed
				initStatusDAO.delete(initStateBean.getObjectID());
			}
		}
		//issue type specific initial status added
		for (Integer issueTypeID : issueTypeToInitStatusMap.keySet()) {
			Integer statusID = issueTypeToInitStatusMap.get(issueTypeID);
			TInitStateBean initStateBean = new TInitStateBean();
			initStateBean.setListType(issueTypeID);
			initStateBean.setStateKey(statusID);
			initStateBean.setProject(projectID);
			initStatusDAO.save(initStateBean);
		}
		
		if (addAsPrivateProject) {
			//add full rights to the private project for the actual user by adding the negative role to the person in project
			List<TRoleBean> roleBeans = RoleBL.loadNotVisible();
			if (roleBeans!=null && !roleBeans.isEmpty()) {
				List<Integer> personIDs = new LinkedList<Integer>();
				personIDs.add(personID);
				RoleAssignmentsBL.addPersonsToRoles(projectID, roleBeans.get(0).getObjectID(), personIDs);
			}
		}
		return ProjectJSON.saveProjectDetailJSON(projectNodeToReload, projectNodeToSelect,
				reloadProjectTree, projectConfigTypeNodeToSelect, reloadProjectConfigTree);
	}



	/**
	 * Close the not closed descendants
	 * @param projectBean
	 * @param onlyActive
	 * @param newStatusID
	 * @return
	 */
	private static void changeDescendantsStatus(TProjectBean projectBean, boolean onlyActive, Integer newStatusID) {
		List<TProjectBean> childProjects = null;
		if (onlyActive) {
			childProjects = ProjectBL.loadActiveSubrojects(projectBean.getObjectID());
		} else {
			childProjects = ProjectBL.loadActiveInactiveSubrojects(projectBean.getObjectID());
		}
		if (childProjects!=null) {
			for (TProjectBean childProjectBean : childProjects) {
				changeDescendantsStatus(childProjectBean, onlyActive, newStatusID);
			}
		}
		projectBean.setStatus(newStatusID);
		ProjectBL.saveSimple(projectBean);
	}
	
	/**
	 * Open the closed assignments up to the first opened ascendent
	 * @param projectBean
	 * @param activateAscendant
	 * @param newStatusID
	 */
	private static void changeAscendentsStatus(TProjectBean projectBean, boolean activateAscendant, Integer newStatusID) {
		if (projectBean!=null) {
			Integer projectStatus = projectBean.getStatus();
			Integer statusFlag= SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, projectStatus);
			if (statusFlag!=null) {
				boolean changeAscendent = false;
				if (activateAscendant) {
					changeAscendent = statusFlag.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE;
				} else {
					changeAscendent = statusFlag.intValue()==TSystemStateBean.STATEFLAGS.CLOSED;
				}
				if (changeAscendent) {
					projectBean.setStatus(newStatusID);
					ProjectBL.saveSimple(projectBean);
					Integer parentID = projectBean.getParent();
					if (parentID!=null) {
						TProjectBean parentProjectBean = ProjectBL.loadByPrimaryKey(parentID);
						changeAscendentsStatus(parentProjectBean, activateAscendant, newStatusID);
					}
				}
			}
		}
	}
	
	/**
	 * Clears the parent for a project
	 * @param projectID
	 * @return
	 */
	static boolean clearParent(Integer projectID){
		TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
		if(projectBean!=null){
			projectBean.setParent(null);
			ProjectBL.saveSimple(projectBean);
			return true;
		}
		return false;
	}

	/**
	 * Updates the parent for a project
	 * @param projectID
	 * @param parentID
	 * @return
	 */
	static boolean updateParent(Integer projectID,Integer parentID){
		TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
		if(projectBean!=null){
			if(parentID!=null){
				TProjectBean parent = ProjectBL.loadByPrimaryKey(parentID);
				if(parent!=null){
					projectBean.setParent(parentID);
					ProjectBL.saveSimple(projectBean);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Deletes a project with all subprojects and issues
	 * @param projectID
	 * @param deleteConfirmed
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static String deleteProject(Integer projectID, boolean deleteConfirmed, TPersonBean personBean, Locale locale) {
		String errorMessage;
		if (!personBean.isSys()) {
			//false to do not allow to delete a project as substitute
			boolean isProjectAdmin = AccessBeans.isPersonProjectAdminForProject(personBean.getObjectID(), projectID, false);
			if (!isProjectAdmin) {
				errorMessage = LocalizeUtil.getParametrizedString("common.lbl.noDeleteRight", 
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.lbl.project", locale)}, locale);
				LOGGER.warn("User " + personBean.getLoginName() + " tried to delete the project " + projectID + " but has no delete right");
				return JSONUtility.encodeJSONFailure(
						errorMessage, JSONUtility.DELETE_ERROR_CODES.NO_RIGHT_TO_DELETE);
			}
		}
		if (!deleteConfirmed && hasDependentIssues(projectID)) {
			errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.err.hasIssues", locale); 
			errorMessage = errorMessage + " " + LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cancelDeleteAlert", locale);
			return JSONUtility.encodeJSONFailure(
					errorMessage, JSONUtility.DELETE_ERROR_CODES.NOT_EMPTY_WARNING);
		}
		if (!deleteConfirmed && hasSubprojects(projectID)) {
			errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.err.hasSubprojects", locale);
			errorMessage = errorMessage + LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cancelDeleteAlert", locale);
			return JSONUtility.encodeJSONFailure(
					errorMessage, JSONUtility.DELETE_ERROR_CODES.NOT_EMPTY_WARNING);
		}
		String nodeToReload = null;
		TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
		if (projectBean!=null) {
			Integer parentProjectId = projectBean.getParent();
			if (parentProjectId!=null) {
				nodeToReload = parentProjectId.toString();
			}
			LOGGER.fatal("Deleting the project " + projectBean.getLabel() + " with ID " + projectID + " by " + personBean.getLoginName() + " at " + new Date());
			deleteRecursively(projectBean, personBean);
		}
		//force reloading of projects only after all projects are deleted
		LookupContainer.resetProjectMap();
		return ProjectJSON.saveDeleteDetailJSON(nodeToReload, nodeToReload, projectID, true);
	}
	
	/**
	 * Deletes all subprojects and then the project
	 * @param projectBean
	 */
	private static void deleteRecursively(TProjectBean projectBean, TPersonBean personBean) {
		Integer projectID = projectBean.getObjectID(); 
		List<TProjectBean> subprojectBeans = ProjectBL.loadSubrojects(projectID);
		if (subprojectBeans!=null) {
			for (TProjectBean subprojectBean : subprojectBeans) {
				LOGGER.fatal("Deleting the subproject " + subprojectBean.getLabel() + " with ID " +
						subprojectBean.getObjectID() + " parent ID " + projectID + " by " + personBean.getLoginName() + " at " + new Date());
				deleteRecursively(subprojectBean, personBean);
			}
		}
		deleteProject(projectBean);
	}
	
	/**
	 * Deletes a project with all subprojects and issues
	 * @param projectBean
	 */
	private static void deleteProject(TProjectBean projectBean) {
		if (projectBean!=null) {
			Integer projectID = projectBean.getObjectID();
			LOGGER.fatal("Project " + projectBean.getLabel() + " with ID " +  projectID + " is now deleted...");
			//delete all the workItems contained in this project from the corresponding lucene indexes
			List<TWorkItemBean> workItemBeans = ItemBL.loadAllByProject(projectID,
					Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.INCLUDE_ARCHIVED),
					Integer.valueOf(FilterUpperTO.DELETED_FILTER.INCLUDE_DELETED));
			if (workItemBeans!=null) {
				LOGGER.fatal("Number of items to delete from project " + projectBean.getLabel() + " with ID " +  projectID + " is " + workItemBeans.size());
			}
			List<Integer> workItemIDs = GeneralUtils.createIntegerListFromBeanList(workItemBeans);
			//delete the workItem index entries for this list of workItems
			LuceneIndexer.deleteFromWorkItemIndex(workItemBeans, SystemFields.INTEGER_PROJECT, true);
			//delete from lucene indexes
			List<IAssociatedFieldsIndexer> associatedFieldIndexers = LuceneIndexer.getAssociatedFieldIndexers();
			for (IAssociatedFieldsIndexer associatedFieldsIndexer : associatedFieldIndexers) {
				associatedFieldsIndexer.deleteByWorkItems(workItemIDs);
			}
			
			//delete all his workItems and other direct (foreign key) dependencies.
			projectDAO.deleteDependencies(projectID);
			
			/**
			 * Delete the project and release specific dashboards
			 */
			/*DashboardScreenDAO dashboardScreenDAO = DAOFactory.getFactory().getDashboardScreenDAO();
			TDashboardScreenBean projectDashboardScreenBean = dashboardScreenDAO.loadByProject(projectID, SystemFields.INTEGER_PROJECT);
			if (projectDashboardScreenBean!=null) {
				dashboardScreenDAO.delete(projectDashboardScreenBean.getObjectID());
			}
			TDashboardScreenBean releaseDashboardScreenBean = dashboardScreenDAO.loadByProject(projectID, SystemFields.INTEGER_RELEASE);
			if (releaseDashboardScreenBean!=null) {
				dashboardScreenDAO.delete(releaseDashboardScreenBean.getObjectID());
			}*/
			
			//the project related lists cannot be deleted easily, just through business logic
			//by now the project's workItems are deleted, also the TOptionSettings related to 
			//this project (through deleting the TFieldConfigs) by the reflection code above.
			//now try to delete the lists (and also the child lists) and the entries from the list (and child lists)
			List<TListBean> listBeanList = ListBL.getListsByProject(projectID, false);
			if (listBeanList!=null) {
				Iterator<TListBean> iterator = listBeanList.iterator();
				while (iterator.hasNext()) {
					TListBean listBean = iterator.next();
					CustomListBL.getInstance().delete(listBean.getObjectID());
				}
			}
			//if some lists can't be deleted for some reason, then set the project field to null,
			//then at least the project itself can be deleted. 
			listBeanList = ListBL.getListsByProject(projectID, false);
			if (listBeanList!=null) {
				Iterator<TListBean> iterator = listBeanList.iterator();
				while (iterator.hasNext()) {
					TListBean listBean = iterator.next();
					listBean.setDeleted(BooleanFields.TRUE_VALUE);
					listBean.setProject(null);
					ListBL.save(listBean);
				}
			}
			/**
			 * Remove the occurrences of project in project pickers
			 */
			List<Integer> projectPickerFields = FieldBL.getCustomPickerFieldIDs(SystemFields.INTEGER_PROJECT);
			if (projectPickerFields!=null && !projectPickerFields.isEmpty()) {
				for (Integer fieldID : projectPickerFields) {
					AttributeValueBL.deleteBySystemOption(fieldID, projectID, SystemFields.INTEGER_PROJECT);
				}
			}
			/**
			 * Set the occurrences in history to null instead of removing, because then the history would not have any clue about removed project from project picker
			 */
			FieldChangeBL.setSystemOptionToNullInHistory(projectID, SystemFields.INTEGER_PROJECT, true);
			FieldChangeBL.setSystemOptionToNullInHistory(projectID, SystemFields.INTEGER_PROJECT, false);
			
			LastExecutedBL.deleteByFilterIDAndFilterType(-projectID, QUERY_TYPE.PROJECT_RELEASE);
			//now hopefully the project has no foreign key dependences and can be deleted without problem
			projectDAO.delete(projectID);
			//delete from lucene index
			NotLocalizedListIndexer.getInstance().deleteByKeyAndType(projectID, 
					LuceneUtil.LOOKUPENTITYTYPES.PROJECT);
			//cache and possible lucene update in other nodes
			ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PROJECT, projectID, ClusterMarkChangesBL.getChangeTypeByDelete());
		}
	}
	
	/**
	 * @param projectID primary key of a project
	 * @return true, if project has workItems
	 */
	private static boolean hasDependentIssues(Integer projectID) {
		List<TWorkItemBean> workItemBeans = ItemBL.loadAllByProject(projectID,
				Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.INCLUDE_ARCHIVED),
				Integer.valueOf(FilterUpperTO.DELETED_FILTER.INCLUDE_DELETED));
		return (workItemBeans!= null) && (workItemBeans.size() > 0);
	}
	
	/**
	 * @param projectID primary key of a project
	 * @return true, if project has workItems
	 */
	private static boolean hasSubprojects(Integer projectID) {
		List<TProjectBean> projectBeans = ProjectBL.loadSubrojects(projectID);
		return (projectBeans!= null) && (projectBeans.size() > 0);
	}
	
	/**
	 * Whether an entry is found in a list 
	 * @param labelBeans
	 * @param objectID
	 * @return
	 */
	static boolean foundInList(List<ILabelBean> labelBeans, Integer objectID) {
		if (objectID!=null && labelBeans!=null) {
			for (ILabelBean labelBean : labelBeans) {
				if (labelBean.getObjectID().equals(objectID)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Whether the project or projectType might have releases
	 * @param projectTypeBean
	 * @param projectBean
	 * @return
	 */
	public static boolean mightHaveRelease(TProjectTypeBean projectTypeBean, TProjectBean projectBean) {
		return projectTypeBean!=null && projectTypeBean.getUseReleases() && projectHasRelease(projectBean);
	}
	
	/**
	 * Whether the project might have releases
	 * @param projectBean
	 * @return
	 */
	private static boolean projectHasRelease(TProjectBean projectBean) {
		if (projectBean!=null) {
			return PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.USE_RELEASE);
		}
		return false;
	}

	/**
	 * Whether the project or projectType might have releases
	 * @param projectTypeBean
	 * @param projectBean
	 * @return
	 */
	private static boolean mightHaveAccounting(TProjectTypeBean projectTypeBean, TProjectBean projectBean) {
		return projectTypeBean!=null && projectTypeBean.getUseAccounting() && projectHasAccounting(projectBean);
	}
	
	/**
	 * Whether the project might have releases
	 * @param projectBean
	 * @return
	 */
	private static boolean projectHasAccounting(TProjectBean projectBean) {
		if (projectBean!=null) {
			return projectBean.isWokTrackingActive() || projectBean.isCostTrackingActive();
		}
		return false;
	}
	/**
	 * Get default subproject for project
	 * @deprecated only used for migration
	 * @param projectBean
	 * @return
	 */
	public static Integer getDefaultSubproject(TProjectBean projectBean) {
		Integer projectID = projectBean.getObjectID();
		String strDefaultSubproject = PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_SUBPROJECT);
		//Integer defaultSubproject = null;
		if (strDefaultSubproject!=null && !"".equals(strDefaultSubproject)) {
			try {
				return Integer.valueOf(strDefaultSubproject);
			} catch (Exception e) {
				LOGGER.warn("Converting the default subproject " + strDefaultSubproject + " for project " + projectID + " to integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
		//true because subproject is required
		//return getDefaultIfSpecifiedAndExists(defaultSubproject, subprojectDAO.loadByProject1(projectID), true);
	}
	
	/**
	 * Get default class for project
	 * @deprecated only used for migration
	 * @param projectBean
	 * @return
	 */
	public static Integer getDefaultClass(TProjectBean projectBean) {
		Integer projectID = projectBean.getObjectID();
		String strDefaultClass = PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_CLASS);
		//Integer defaultClass = null;
		if (strDefaultClass!=null && !"".equals(strDefaultClass)) {
			try {
				return Integer.valueOf(strDefaultClass);
			} catch (Exception e) {
				LOGGER.warn("Converting the default class " + strDefaultClass + " for project " + projectID + " to integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}
	
	
	
	/**
	 * Get default release for project
	 * @param projectBean
	 * @param noticed release noticed or scheduled
	 * @return
	 */
	static Integer getDefaultRelease(TProjectBean projectBean, boolean noticed, TFieldConfigBean fieldConfigBean) {
		Integer projectID = projectBean.getObjectID();
		String morePropsKey;
		List<TReleaseBean> releases = null;
		if (noticed) {
			morePropsKey = TReleaseBean.MOREPPROPS.DEFAULT_RELEASENOTICED;
			releases = ReleaseBL.loadActiveInactiveByProject(projectID);
		} else {
			morePropsKey = TReleaseBean.MOREPPROPS.DEFAULT_RELEASESCHEDULED;
			releases = ReleaseBL.loadActiveOrNotPlannedByProject(projectID);
		}
		if (releases!=null && !releases.isEmpty()) {
			Iterator<TReleaseBean> iterator = releases.iterator();
			while (iterator.hasNext()) {
				TReleaseBean releaseBean = iterator.next();
				String strDefaulRelease = PropertiesHelper.getProperty(
						releaseBean.getMoreProperties(),	morePropsKey);
				if (strDefaulRelease!=null && Boolean.TRUE.toString().equals(strDefaulRelease)) {
					return releaseBean.getObjectID();
				}
			}
			if (fieldConfigBean!=null && fieldConfigBean.isRequiredString()) {
				//none of them is default, get the first one
				TReleaseBean releaseBean = releases.get(0);
				return releaseBean.getObjectID();
			}
		}
		return null;
		
	}
	
	/**
	 * Get default issue type for project
	 * @param projectBean
	 * @param originatorID
	 * @return
	 */
	static Integer getDefaultIssueType(TProjectBean projectBean, Integer originatorID) {
		Integer projectID = projectBean.getObjectID();
		String strDefaultIssueType = PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_ISSUETYPE);
		Integer defaultIssueType = null;
		if (strDefaultIssueType!=null && !"".equals(strDefaultIssueType)) {
			try {
				defaultIssueType = Integer.valueOf(strDefaultIssueType);
			} catch (Exception e) {
				LOGGER.warn("Converting the default issue type " + strDefaultIssueType + " for project " + projectID + " to integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return getDefaultIfSpecifiedAndExists(defaultIssueType, 
				(List)IssueTypeBL.loadByPersonAndProjectAndRight(originatorID, projectID, 
						new int[]{AccessBeans.AccessFlagIndexes.CREATETASK}), true);
	}
	
	/**
	 * Get default priority for project
	 * @param projectBean
	 * @return
	 */
	static Integer getDefaultPriority(TProjectBean projectBean) {
		Integer projectID = projectBean.getObjectID();
		String strDefaultPriority = PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_PRIORITY);
		Integer defaultPriority = null;
		if (strDefaultPriority!=null && !"".equals(strDefaultPriority)) {
			try {
				defaultPriority = Integer.valueOf(strDefaultPriority);
			} catch (Exception e) {
				LOGGER.warn("Converting the default priority " + strDefaultPriority + " for project " + projectID + " to integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return getDefaultIfSpecifiedAndExists(defaultPriority,
				(List)PriorityBL.loadAllowedByProjectTypesAndIssueTypes(new Integer[] {projectBean.getProjectType()}, null), true);
	}
	
	/**
	 * Get default severity for project
	 * @param projectBean
	 * @return
	 */
	static Integer getDefaultSeverity(TProjectBean projectBean, TFieldConfigBean fieldConfigBean) {
		Integer projectID = projectBean.getObjectID();
		String strDefaultSeverity = PropertiesHelper.getProperty(projectBean.getMoreProperties(), TProjectBean.MOREPPROPS.DEFAULT_SEVERITY);
		Integer defaultSeverity = null;
		if (strDefaultSeverity!=null && !"".equals(strDefaultSeverity)) {
			try {
				defaultSeverity = Integer.valueOf(strDefaultSeverity);
			} catch (Exception e) {
				LOGGER.warn("Converting the default severity " + strDefaultSeverity + " for project " + projectID + " to integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		boolean fallback = false;
		if (fieldConfigBean!=null && fieldConfigBean.isRequiredString()) {
			fallback = true;
		}
		return getDefaultIfSpecifiedAndExists(defaultSeverity,
				(List)SeverityBL.loadAllowedByProjectTypesAndIssueTypes(
						new Integer[] {projectBean.getProjectType()}, null), fallback);
	}

	/**
	 * Get the selected prefill value
	 * @param projectBean
	 * @return
	 */
	public static Integer getPrefillValue(TProjectBean projectBean) {
		return PropertiesHelper.getIntegerProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.PREFILL); 
	}

	/**
	 * Gets the default system list values set for the project  
	 * @param fieldID
	 * @param projectBean
	 * @param originatorID
	 * @param issueTypeID
	 * @param fieldConfigBean is taken into account only for the optional system lists. 
	 * For mandatory system lists we always fall back to the 
	 * first entry if the default value is not valid, independently 
	 * of the fieldConfigBean's required flag 
	 * @return
	 */
	public static Integer getDefaultFieldValueForProject(Integer fieldID, 
			TProjectBean projectBean, Integer originatorID, Integer issueTypeID, TFieldConfigBean fieldConfigBean) {
		switch (fieldID.intValue()) {
		case SystemFields.ISSUETYPE:
			return getDefaultIssueType(projectBean, originatorID);
		case SystemFields.STATE:
			return ProjectConfigBL.getDefaultIfSpecifiedAndExists(projectBean.getDefaultInitStateID(), 
					(List)StatusBL.getByProjectTypeIssueTypeAssignments(projectBean.getProjectType(), issueTypeID, null), true);
		case SystemFields.MANAGER:
			return ProjectConfigBL.getDefaultIfSpecifiedAndExists(projectBean.getDefaultManagerID(), 
					(List)PersonBL.loadManagersByProjectAndIssueType(projectBean.getObjectID(), issueTypeID), true);
		case SystemFields.RESPONSIBLE:
			return ProjectConfigBL.getDefaultIfSpecifiedAndExists(projectBean.getDefaultOwnerID(), 
					(List)PersonBL.loadResponsiblesByProjectAndIssueType(projectBean.getObjectID(), issueTypeID), true);
		case SystemFields.RELEASENOTICED:
			return getDefaultRelease(projectBean, true, fieldConfigBean);
		case SystemFields.RELEASESCHEDULED:
			return getDefaultRelease(projectBean, false, fieldConfigBean);
		case SystemFields.PRIORITY:
			return getDefaultPriority(projectBean);
		case SystemFields.SEVERITY:
			return getDefaultSeverity(projectBean, fieldConfigBean);
		}
		return null; 
	}

	/**
	 * Verify whether the default value is valid
	 * @param defaultValue
	 * @param lableBeanList
	 * @param fallBackToFirst
	 * @return
	 */
	public static Integer getDefaultIfSpecifiedAndExists(Integer defaultValue, List<ILabelBean> lableBeanList, boolean fallBackToFirst) {
		if (lableBeanList==null || lableBeanList.isEmpty()) {
			return null;
		}
		if (defaultValue!=null) {
			for (ILabelBean labelBean : lableBeanList) {
				if (labelBean.getObjectID().equals(defaultValue)) {
					return defaultValue;
				}
			}
		}
		if (fallBackToFirst) {
			ILabelBean labelBean = lableBeanList.get(0);
			return labelBean.getObjectID();
		}
		return null;
	}
	private static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}
	
	
	/**
	 * This method creates a private workspace.
	 * @param personBean Private workspace owner.
	 * @param locale 
	 */
	public static void createWorkspaceForNewUser(TPersonBean personBean, Locale locale) {
		List<TProjectBean> privateProjects = ProjectBL.getPrivateProject(personBean.getObjectID());
		if (privateProjects==null || privateProjects.isEmpty()) {
			//be sure to add only if not already exists
			TProjectBean projectBean = new TProjectBean();
			ProjectBaseTO projectBaseTO = new ProjectBaseTO();
			List<TSystemStateBean> projectStatusList = SystemStatusBL.getStatusOptions(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, locale);
			if(projectStatusList != null && projectStatusList.size() > 0) {
				projectBaseTO.setProjectStatusID(projectStatusList.get(0).getObjectID());
			}
			List<TProjectTypeBean>projectTypesList = ProjectTypesBL.loadPrivate();
			if(projectTypesList != null && projectTypesList.size() > 0) {
				projectBaseTO.setProjectTypeID(projectTypesList.get(0).getObjectID());
			}
			projectBaseTO.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.lbl.privateProjectName", locale));
			ProjectDefaultsTO projectDefaultsTO = new ProjectDefaultsTO();				
			Integer projectTypeID = projectBean.getProjectType();			
			projectDefaultsTO.setDefaultManagerID(personBean.getObjectID());
			projectDefaultsTO.setDefaultResponsibleID(personBean.getObjectID());
			Map<Integer, String> invalidDefaults = setSystemLists(projectBean, projectDefaultsTO, projectTypeID, locale, false);
			if(invalidDefaults.isEmpty()) {
				saveProjectDetail(null, true, false, true, projectBaseTO, null, projectDefaultsTO, 
						false, false, personBean.getObjectID(), locale);
			}
		}
	}
	
	public static boolean changeTemplateStateTo(TProjectBean templateBean, Integer stateFlagToChange, Locale locale) {
		List<TSystemStateBean> projectStatusList = SystemStatusBL.getStatusOptions(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, locale);
		TSystemStateBean  properState= null;
		for (TSystemStateBean aStateBean : projectStatusList) {
			if(aStateBean.getStateflag().intValue() == stateFlagToChange.intValue()) {
				properState = aStateBean;
			}
		}
		if(properState != null) {
			templateBean.setStatus(properState.getObjectID());
			ProjectBL.saveSimple(templateBean);
			return true;
		}
		return false;
	}
}


