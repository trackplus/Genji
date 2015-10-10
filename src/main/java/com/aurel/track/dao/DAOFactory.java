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


package com.aurel.track.dao;

import com.aurel.track.dao.torque.DAOFactoryTorque;

public abstract class DAOFactory {
	
	public static DAOFactory getFactory() {
		//get the persistenceFramework from config files
		int persistenceFramework = 1;
		switch (persistenceFramework) {
		case 1:
				return DAOFactoryTorque.getInstance();
		case 2:	 
				//return new HibernatePersistenceManager(); 
		default:
			return DAOFactoryTorque.getInstance();
		}
	}
	
	public abstract void executeUpdateStatement(String sqlStatement);
	
	public abstract WorkItemDAO getWorkItemDAO();
	
	public abstract FieldDAO getFieldDAO();
	
	public abstract FieldConfigDAO getFieldConfigDAO();

	public abstract TextBoxSettingsDAO getTextBoxSettingsDAO();
	
	public abstract OptionDAO getOptionDAO();
	
	public abstract OptionSettingsDAO getOptionSettingsDAO();
	
	public abstract GeneralSettingsDAO getGeneralSettingsDAO();
	
	public abstract ListDAO getListDAO();
	
	public abstract AttributeValueDAO getAttributeValueDAO();
	
	public abstract ScreenDAO getScreenDAO();
	public abstract ScreenTabDAO getScreenTabDAO(); 
	public abstract ScreenPanelDAO getScreenPanelDAO();
	public abstract ScreenFieldDAO getScreenFieldDAO();
	public abstract ScreenConfigDAO getScreenConfigDAO();
	public abstract ActionDAO getActionDAO();
	
	public abstract IssueTypeDAO getIssueTypeDAO();
	public abstract ProjectDAO getProjectDAO();
	public abstract ProjectTypeDAO getProjectTypeDAO();
	public abstract ClassDAO getClassDAO();
	public abstract SubprojectDAO getSubprojectDAO();
	public abstract SeverityDAO getSeverityDAO();
	public abstract PriorityDAO getPriorityDAO();
	public abstract ReleaseDAO getReleaseDAO();
	public abstract PersonDAO getPersonDAO();
	public abstract StateDAO getStateDAO();
	public abstract AccessControlListDAO getAccessControlListDAO();
	public abstract ConfigOptionRoleDAO getConfigOptionRoleDAO();
	
	public abstract StateChangeDAO getStateChangeDAO();
	public abstract BaseLineDAO getBaseLineDAO();
	public abstract TrailDAO getTrailDAO();
	
	public abstract NotifyFieldDAO getNotifyFieldDAO();
	public abstract NotifyTriggerDAO getNotifyTriggerDAO();
	public abstract NotifySettingsDAO getNotifySettingsDAO();
	public abstract NotifyDAO getNotifyDAO();	
	public abstract QueryRepositoryDAO getQueryRepositoryDAO();
	
	public abstract GroupMemberDAO getGroupMemberDAO();
	
	public abstract BudgetDAO getBudgetDAO();
	public abstract ActualEstimatedBudgetDAO getActualEstimatedBudgetDAO();
	public abstract AccountDAO getAccountDAO();
	public abstract CostDAO getCostDAO();
	public abstract SiteDAO getSiteDAO();
	public abstract ReportLayoutDAO getReportLayoutDAO();

	public abstract DashboardScreenDAO getDashboardScreenDAO();
	public abstract DashboardTabDAO getDashboardTabDAO();
	public abstract DashboardPanelDAO getDashboardPanelDAO();
	public abstract DashboardFieldDAO getDashboardFieldDAO();
	
	public abstract ClobDAO getClobDAO();
	
	public abstract PrivateRepositoryDAO getPrivateRepositoryDAO();
	public abstract PublicRepositoryDAO getPublicRepositoryDAO();
	public abstract ProjectRepositoryDAO getProjectRepositoryDAO();
	public abstract VersionControlParameterDAO getVersionControlParameterDAO();
	
	public abstract WorkItemLockDAO getWorkItemLockDAO();

	public abstract ExportTemplateDAO getExportTemplateDAO();
	public abstract EmailProcessedDAO getEmailProcessedDAO();
	public abstract AttachmentDAO getAttachmentDAO();
	public abstract RoleDAO getRoleDAO();
	public abstract DepartmentDAO getDepartmentDAO();
	public abstract LocalizedResourcesDAO getLocalizedResourcesDAO();
	public abstract ComputedValuesDAO getComputedValuesDAO();
	public abstract RoleListTypeDAO getRoleListTypeDAO();
	public abstract DashboardParameterDAO getDashboardParameterDAO();
	public abstract CostCenterDAO getCostCenterDAO();
	public abstract SystemStateDAO getSystemStateDAO();
	public abstract HistoryTransactionDAO getHistoryTransactionDAO();
	public abstract FieldChangeDAO getFieldChangeDAO();
	public abstract ScriptsDAO getScriptsDAO();
	public abstract LinkTypeDAO getLinkTypeDAO();
	public abstract WorkItemLinkDAO getWorkItemLinkDAO();
	
	public abstract LoggingLevelDAO getLoggingLevelDAO();
	public abstract MotdDAO getMothDAO();
	public abstract PIssueTypeDAO getPIssueTypeDAO();
	public abstract PPriorityDAO getPPriorityDAO();
	public abstract ProjectAccountDAO getProjectAccountDAO();
	public abstract PSeverityDAO getPSeverityDAO();
	public abstract PStatusDAO getPStatusDAO();
	public abstract RoleFieldDAO getRoleFieldDAO();
	public abstract WorkflowDAO getWorkflowDAO();
	public abstract WorkflowCategoryDAO getWorkflowCategoryDAO();
	public abstract WorkflowRoleDAO getWorkflowRoleDAO();
	public abstract BlobDAO getBlobDAO();
	public abstract MsProjectTaskDAO getMsProjectTaskDAO();
	public abstract MsProjectExchangeDAO getMsProjectExchangeDAO();
	
	public abstract MenuitemQueryDAO getMenuitemQueryDAO();
	public abstract FilterCategoryDAO getFilterCategoryDAO();
	public abstract ReportCategoryDAO getReportCategoryDAO();
	public abstract ChildIssueTypeDAO getChildIssueTypeDAO();
	public abstract InitStatusDAO getInitStatusDAO();
	public abstract BasketDAO getBasketDAO();
	public abstract PersonBasketDAO getPersonBasketDAO();

	public abstract LastExecutedQueryDAO getLastExecutedQueryDAO();
	public abstract ReportPersonSettingsDAO getReportPersonSettingsDAO();
	public abstract WorkflowdefDAO getWorkflowdefDAO();
	public abstract WorkflowConnectDAO getWorkflowConnectDAO();
	public abstract WorkflowStationDAO getWorkflowStationDAO();
	public abstract WorkflowCommentDAO getWorkflowCommentDAO();

	public abstract WorkflowTransitionDAO getWorkflowTransitionDAO();
	public abstract WorkflowActivityDAO getWorkflowActivityDAO();
	public abstract WorkflowGuardDAO getWorkflowGuardDAO();

	public abstract MailTemplateConfigDAO getMailTemplateConfigDAO();
	public abstract MailTemplateDAO getMailTemplateDAO();
	public abstract MailTemplateDefDAO getMailTemplateDefDAO();
	
	public abstract EntityChangesDAO getEntityChangesDAO();
	public abstract ClusterNodeDAO getClusterNodeDAO();
	public abstract LoggedInUsersDAO getLoggedInUsersDAO();
	public abstract ChildProjectTypeDAO getChildProjectTypeDAO();
	public abstract PRoleDAO getPRoleDAO();
	public abstract ItemTransitionDAO getItemTransitionDAO();
	public abstract WfActivityContextParamsDAO getWfActivityContextParamsDAO();

	public abstract NavigatorLayoutDAO getNavigatorLayoutDAO();
	public abstract NavigatorColumnDAO getNavigatorFieldsDAO();
	public abstract NavigatorGroupingSortingDAO getNavigatorGroupingSortingDAO();
	public abstract CardGroupingFieldDAO getCardGroupingFieldDAO();
	public abstract CardFieldOptionDAO getCardFieldOptionsDAO();
	public abstract CardPanelDAO getCardPanelDAO();
	public abstract CardFieldDAO getCardFieldDAO();
	public abstract UserFeatureDAO getUserFeatureDAO();
	
	public abstract UserLevelDAO getUserLevelDAO();
	public abstract UserLevelSettingDAO getUserLevelSettingDAO();

}
