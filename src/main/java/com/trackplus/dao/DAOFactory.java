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


package com.trackplus.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;

import com.aurel.track.persist.BaseTWorkItemPeer;
import com.trackplus.persist.TSitePeer;


public class DAOFactory {
	
	private static final Logger LOGGER = LogManager.getLogger(DAOFactory.class);
	private static DAOFactory instance;

//	private FieldDAO fieldDAO;
//	private WorkItemDAO workItemDAO;
//	private FieldConfigDAO fieldConfigDAO;
//	private OptionSettingsDAO optionSettingsDAO;
//	private GeneralSettingsDAO generalSettingsDAO;
//	private TextBoxSettingsDAO textBoxSettingsDAO;
//	private OptionDAO optionDAO;
//	private ListDAO listDAO;
//	private AttributeValueDAO attributeValueDAO;
//	private ScreenDAO screenDAO;
//	private ScreenTabDAO screenTabDAO;
//	private ScreenPanelDAO screenPanelDAO;
//	private ScreenFieldDAO screenFieldDAO;
//	private ScreenConfigDAO screenConfigDAO;
//	private ActionDAO actionDAO;
//	private IssueTypeDAO issueTypeDAO;
//	private ProjectDAO projectDAO;
//	private ProjectTypeDAO projectTypeDAO;
//	private ClassDAO classDAO;
//	private SubprojectDAO subprojectDAO;
//	private SeverityDAO severityDAO;
//	private PriorityDAO priorityDAO;
//	private ReleaseDAO releaseDAO;
//	private PersonDAO personDAO;
//	private StateDAO stateDAO;
//	private AccessControlListDAO accessControlListDAO;
//	private ConfigOptionRoleDAO configOptionRoleDAO;
//	private StateChangeDAO stateChangeDAO;
//	private BaseLineDAO baseLineDAO;
//	private TrailDAO trailDAO;
//	private NotifyFieldDAO notifyFieldDAO;
//	private NotifyTriggerDAO notifyTriggerDAO;
//	private NotifySettingsDAO notifySettingsDAO;
//	private NotifyDAO notifyDAO;
//	private QueryRepositoryDAO queryRepositoryDAO;
//	private GroupMemberDAO groupMemberDAO;
//	private BudgetDAO budgetDAO;
//	private ActualEstimatedBudgetDAO actualEstimatedBudgetDAO;
//	private AccountDAO accountDAO;
//	private CostDAO costDAO;
//	private SiteDAO siteDAO;
//	private ReportLayoutDAO reportLayoutDAO;
//	private DashboardScreenDAO dashboardScreenDAO;
//	private DashboardTabDAO dashboardTabDAO;
//	private DashboardPanelDAO dashboardPanelDAO;
//	private DashboardFieldDAO dashboardFieldDAO;
//	private DashboardParameterDAO dashboardParameterDAO;
//	private ClobDAO clobDAO;
//	private PrivateRepositoryDAO privateRepositoryDAO;
//	private ProjectRepositoryDAO projectRepositoryDAO;
//	private PublicRepositoryDAO publicRepositoryDAO;
//	private VersionControlParameterDAO versionControlParameterDAO;
//	private WorkItemLockDAO workItemLockDAO;
//	private ExportTemplateDAO exportTemplateDAO;
//	private EmailProcessedDAO emailProcessedDAO;
//	private AttachmentDAO attachmentDAO;
//	private RoleDAO roleDAO;
//	private DepartmentDAO departmentDAO;
//	private LocalizedResourcesDAO localizedResourcesDAO;
//	private ComputedValuesDAO computedValuesDAO;
//	private RoleListTypeDAO roleListTypeDAO;	
//	private CostCenterDAO costCenterDAO;
//	private SystemStateDAO systemStateDAO;
//	private HistoryTransactionDAO historyTransactionDAO;
//	private FieldChangeDAO fieldChangeDAO;
//	private ScriptsDAO scriptsDAO;
//	private LinkTypeDAO linkTypeDAO;
//	private WorkItemLinkDAO workItemLinkDAO;
//	
//	private LoggingLevelDAO loggingLevelDAO;	
//	private MotdDAO mothDAO;	
//	private PIssueTypeDAO pIssueTypeDAO;
//	private PPriorityDAO pPriorityDAO;
//	private ProjectAccountDAO projectAccountDAO;
//	private PSeverityDAO pSeverityDAO;
//	private PStatusDAO pStatusDAO;
//	private RoleFieldDAO roleFieldDAO;
//	private WorkflowDAO workflowDAO;
//	private WorkflowCategoryDAO workflowCategoryDAO;
//	private WorkflowRoleDAO workflowRoleDAO;
//	private BlobDAO blobDAO;
//	private MsProjectTaskDAO msProjectTaskDAO;
//	private MsProjectExchangeDAO msProjectExchangeDAO;
//	
//	private MenuitemQueryDAO menuitemQueryDAO;
//	private FilterCategoryDAO filterCategoryDAO;
//	private ReportCategoryDAO reportCategoryDAO;
//	private ChildIssueTypeDAO childIssueTypeDAO;
//	private InitStatusDAO initStatusDAO;	
//	private BasketDAO basketDAO; 
//	private PersonBasketDAO personBasketDAO;
//
//	private LastExecutedQueryDAO lastExecutedQueryDAO;
//	private ReportPersonSettingsDAO reportPersonSettingsDAO;
//	private WorkflowdefDAO workflowdefDAO;
//	private WorkflowConnectDAO workflowConnectDAO;
//	private WorkflowStationDAO workflowStationDAO;
//	private WorkflowTransitionDAO workflowTransitionDAO;
//	private WorkflowActivityDAO workflowActivityDAO;
//	private WorkflowGuardDAO workflowGuardDAO;
//
//	private MailTemplateDAO mailTemplateDAO;
//	private MailTemplateDefDAO mailTemplateDefDAO;
//	private MailTemplateConfigDAO mailTemplateConfigDAO;

	public static DAOFactory getInstance(){
		if(instance==null){
			 instance=new DAOFactory();
		}
		return instance;
	}
	
	
	public void executeUpdateStatement(String sqlStatement) {
		Connection db = null;
		try {
			//get the database name from any peer
			db = Torque.getConnection(BaseTWorkItemPeer.DATABASE_NAME);
			// it's the same name for all tables here, so we don't care
			Statement stmt;
			stmt = db.createStatement();
			stmt.executeUpdate(sqlStatement);
		} catch (TorqueException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} finally {
			Torque.closeConnection(db);
		}
	}
	
/*	
	public WorkItemDAO getWorkItemDAO(){
		if(workItemDAO==null){
			workItemDAO = new TWorkItemPeer();
		}
		return workItemDAO;
	}
	
	
	public FieldDAO getFieldDAO(){
		if(fieldDAO==null){
			fieldDAO=new TFieldPeer();
		}
		return fieldDAO;
	}
	
	
	public FieldConfigDAO getFieldConfigDAO(){
		if(fieldConfigDAO==null){
			fieldConfigDAO=new TFieldConfigPeer();
		}
		return fieldConfigDAO;
	}

	
	public OptionSettingsDAO getOptionSettingsDAO() {
		if(optionSettingsDAO==null){
			optionSettingsDAO=new TOptionSettingsPeer();
		}
		return optionSettingsDAO;
	}

	
	public GeneralSettingsDAO getGeneralSettingsDAO() {		
	  if (generalSettingsDAO == null) {
		  generalSettingsDAO = new TGeneralSettingsPeer();
	  }
	  return generalSettingsDAO;
	}

	
	public TextBoxSettingsDAO getTextBoxSettingsDAO() {
		if (textBoxSettingsDAO == null) {
		  textBoxSettingsDAO = new TTextBoxSettingsPeer();
		}
		return textBoxSettingsDAO;
	}

	
	public OptionDAO getOptionDAO() {
		if (optionDAO == null) {
			optionDAO = new TOptionPeer();
		}
		return optionDAO;
 	}

	
	public ListDAO getListDAO() {
		if (listDAO == null) {
		  listDAO = new TListPeer();
		}
		return listDAO;
	}

	
	public AttributeValueDAO getAttributeValueDAO() {
		if (attributeValueDAO == null) {
			attributeValueDAO = new TAttributeValuePeer();
		}
		return attributeValueDAO;
	}
	
	
	public ScreenDAO getScreenDAO() {
		if (screenDAO == null) {
			screenDAO = new TScreenPeer();
		}
		return screenDAO;
	}
	
	public ScreenTabDAO getScreenTabDAO() {
		if (screenTabDAO == null) {
			screenTabDAO = new TScreenTabPeer();
		}
		return screenTabDAO;
	}
	
	public ScreenPanelDAO getScreenPanelDAO() {
		if (screenPanelDAO == null) {
			screenPanelDAO = new TScreenPanelPeer();
		}
		return screenPanelDAO;
	}
	
	public ScreenFieldDAO getScreenFieldDAO() {
		if (screenFieldDAO == null) {
			screenFieldDAO = new TScreenFieldPeer();
		}
		return screenFieldDAO;
	}
	
	public ScreenConfigDAO getScreenConfigDAO() {
		if (screenConfigDAO == null) {
			screenConfigDAO = new TScreenConfigPeer();
		}
		return screenConfigDAO;
	}
	
	public ActionDAO getActionDAO() {
		if (actionDAO == null) {
			actionDAO = new TActionPeer();
		}
		return actionDAO;
	}

	
	public IssueTypeDAO getIssueTypeDAO() {
		if (issueTypeDAO == null) {
			issueTypeDAO = new TListTypePeer();
		}
		return issueTypeDAO;
	}

	
	public ProjectDAO getProjectDAO() {
		if (projectDAO == null) {
			projectDAO = new TProjectPeer();
		}
		return projectDAO;
	}

	
	public ProjectTypeDAO getProjectTypeDAO() {
		if (projectTypeDAO == null) {
			projectTypeDAO = new TProjectTypePeer();
		}
		return projectTypeDAO;
	}
	
	
	public ClassDAO getClassDAO() {
		if (classDAO == null) {
			classDAO = new TClassPeer();
		}
		return classDAO;

	}
	
	public SubprojectDAO getSubprojectDAO() {
		if (subprojectDAO == null) {
			subprojectDAO = new TProjectCategoryPeer();
		}
		return subprojectDAO;
	}
	
	
	public SeverityDAO getSeverityDAO() {
		if (severityDAO == null) {
			severityDAO = new TSeverityPeer();
		}
		return severityDAO;
	}
	
	
	public PriorityDAO getPriorityDAO() {
		if (priorityDAO == null) {
			priorityDAO = new TPriorityPeer();
		}
		return priorityDAO;
	}
	
	
	public ReleaseDAO getReleaseDAO() {
		if (releaseDAO == null) {
			releaseDAO = new TReleasePeer();
		}
		return releaseDAO;
	}
	
	
	public PersonDAO getPersonDAO() {
		if (personDAO == null) {
			personDAO = new TPersonPeer();
		}
		return personDAO;
	}
	
	
	public StateDAO getStateDAO() {
		if (stateDAO == null) {
			stateDAO = new TStatePeer();
		}
		return stateDAO;
	}

	
	public AccessControlListDAO getAccessControlListDAO() {
		if (accessControlListDAO == null) {
			accessControlListDAO = new TAccessControlListPeer();
		}
		return accessControlListDAO;
	}

	
	public ConfigOptionRoleDAO getConfigOptionRoleDAO() {
		if (configOptionRoleDAO == null) {
			configOptionRoleDAO = new TConfigOptionsRolePeer();
		}
		return configOptionRoleDAO;
	}
	
	public StateChangeDAO getStateChangeDAO(){
		if (stateChangeDAO == null) {
			stateChangeDAO = new TStateChangePeer();
		}
		return stateChangeDAO;
	}
	
	
	public BaseLineDAO getBaseLineDAO(){
		if (baseLineDAO == null) {
			baseLineDAO = new TBaseLinePeer();
		}
		return baseLineDAO;
	}
	
	
	public TrailDAO getTrailDAO(){
		if (trailDAO == null) {
			trailDAO = new TTrailPeer();
		}
		return trailDAO;
	}

	
	public NotifyFieldDAO getNotifyFieldDAO(){
		if (notifyFieldDAO == null) {
			notifyFieldDAO = new TNotifyFieldPeer();
		}
		return notifyFieldDAO;
	}
	
	
	public NotifyTriggerDAO getNotifyTriggerDAO() {
		if (notifyTriggerDAO == null) {
			notifyTriggerDAO = new TNotifyTriggerPeer();
		}
		return notifyTriggerDAO;
	}
	
	public NotifySettingsDAO getNotifySettingsDAO() {
		if (notifySettingsDAO == null) {
			notifySettingsDAO = new TNotifySettingsPeer();
		}
		return notifySettingsDAO;
	}

	
	public NotifyDAO getNotifyDAO() {
		if (notifyDAO == null) {
			notifyDAO = new TNotifyPeer();
		}
		return notifyDAO;
	}

		
	
	public QueryRepositoryDAO getQueryRepositoryDAO() {
		if (queryRepositoryDAO == null) {
			queryRepositoryDAO = new TQueryRepositoryPeer();
		}
		return queryRepositoryDAO;
	}

	
	public GroupMemberDAO getGroupMemberDAO() {
		if (groupMemberDAO == null) {
			groupMemberDAO = new TGroupMemberPeer();
		}
		return groupMemberDAO;
	}

	
	public BudgetDAO getBudgetDAO() {
		if (budgetDAO == null) {
			budgetDAO = new TBudgetPeer();
		}
		return budgetDAO;
	}

	
	public ActualEstimatedBudgetDAO getActualEstimatedBudgetDAO() {
		if (actualEstimatedBudgetDAO == null) {
			actualEstimatedBudgetDAO = new TActualEstimatedBudgetPeer();
		}
		return actualEstimatedBudgetDAO;
	}

	
	public AccountDAO getAccountDAO() {
		if (accountDAO == null) {
			accountDAO = new TAccountPeer();
		}
		return accountDAO;
	}

	
	public CostDAO getCostDAO() {
		if (costDAO == null) {
			costDAO = new TCostPeer();
		}
		return costDAO;
	}
	*/
	
	
	public static SiteDAO getSiteDAO() {
		return new TSitePeer();
	}
	
	/*
	public ReportLayoutDAO getReportLayoutDAO() {
		if (reportLayoutDAO == null) {
			reportLayoutDAO = new TReportLayoutPeer();
		}
		return reportLayoutDAO;
	}
	
	public DashboardScreenDAO getDashboardScreenDAO() {
		if (dashboardScreenDAO == null) {
			dashboardScreenDAO = new TDashboardScreenPeer();
		}
		return dashboardScreenDAO;
	}

	
	public DashboardTabDAO getDashboardTabDAO() {
		if (dashboardTabDAO == null) {
			dashboardTabDAO = new TDashboardTabPeer();
		}
		return dashboardTabDAO;
	}

	
	public DashboardPanelDAO getDashboardPanelDAO() {
		if (dashboardPanelDAO == null) {
			dashboardPanelDAO = new TDashboardPanelPeer();
		}
		return dashboardPanelDAO;
	}

	
	public DashboardFieldDAO getDashboardFieldDAO() {
		if (dashboardFieldDAO == null) {
			dashboardFieldDAO = new TDashboardFieldPeer();
		}
		return dashboardFieldDAO;
	}
   
	
	public ClobDAO getClobDAO() {
		if (clobDAO == null) {
			clobDAO = new TCLOBPeer();
		}
		return clobDAO;
	}
	
	
	public PrivateRepositoryDAO getPrivateRepositoryDAO() {
		if (privateRepositoryDAO == null) {
			privateRepositoryDAO = new TPrivateReportRepositoryPeer();
		}
		return privateRepositoryDAO;
	}
	
	public ProjectRepositoryDAO getProjectRepositoryDAO() {
		if (projectRepositoryDAO == null) {
			projectRepositoryDAO = new TProjectReportRepositoryPeer();
		}
		return projectRepositoryDAO;
	}

	
	public PublicRepositoryDAO getPublicRepositoryDAO() {
		if (publicRepositoryDAO == null) {
			publicRepositoryDAO = new TPublicReportRepositoryPeer();
		}
		return publicRepositoryDAO;
	}


	
	public VersionControlParameterDAO getVersionControlParameterDAO() {
		if (versionControlParameterDAO == null) {
			versionControlParameterDAO = new TVersionControlParameterPeer();
		}
		return versionControlParameterDAO;
	}
	
	
	public WorkItemLockDAO getWorkItemLockDAO() {
		if (workItemLockDAO == null) {
			workItemLockDAO = new TWorkItemLockPeer();
		}
		return workItemLockDAO;
	}

	
	public ExportTemplateDAO getExportTemplateDAO() {
		if (exportTemplateDAO == null) {
			exportTemplateDAO = new TExportTemplatePeer();
		}
		return exportTemplateDAO;
	}

	
	public EmailProcessedDAO getEmailProcessedDAO() {
		if (emailProcessedDAO == null) {
			emailProcessedDAO = new TEmailProcessedPeer();
		}
		return emailProcessedDAO;
	}
	
	public AttachmentDAO getAttachmentDAO() {
		if(attachmentDAO==null){
			attachmentDAO=new TAttachmentPeer();
		}
		return attachmentDAO;
	}
	
	
	public RoleDAO getRoleDAO() {
		if(roleDAO==null) {
			roleDAO=new TRolePeer();
		}
		return roleDAO;
	}
	
	
	public DepartmentDAO getDepartmentDAO() {
		if(departmentDAO==null) {
			departmentDAO=new TDepartmentPeer();
		}
		return departmentDAO;
	}
	
	
	public LocalizedResourcesDAO getLocalizedResourcesDAO() {
		if(localizedResourcesDAO==null) {
			localizedResourcesDAO=new TLocalizedResourcesPeer();
		}
		return localizedResourcesDAO;
	}
	
	
	public ComputedValuesDAO getComputedValuesDAO() {
		
		if(computedValuesDAO==null) {
			computedValuesDAO=new TComputedValuesPeer();
		}
		return computedValuesDAO;
	}
	
	
	public RoleListTypeDAO getRoleListTypeDAO() {
		if(roleListTypeDAO==null) {
			roleListTypeDAO=new TRoleListTypePeer();
		}
		return roleListTypeDAO;
	}
	
	
	public  DashboardParameterDAO getDashboardParameterDAO() {
		if(dashboardParameterDAO==null) {
			dashboardParameterDAO=new TDashboardParameterPeer();
		}
		return dashboardParameterDAO;
	}
	
	
	public CostCenterDAO getCostCenterDAO() {
		if(costCenterDAO==null) {
			costCenterDAO=new TCostCenterPeer();
		}
		return costCenterDAO;
	}
	
	
	public SystemStateDAO getSystemStateDAO() {
		if(systemStateDAO==null) {
			systemStateDAO=new TSystemStatePeer();
		}
		return systemStateDAO;
	}
	
	
	public HistoryTransactionDAO getHistoryTransactionDAO() {
		if(historyTransactionDAO==null) {
			historyTransactionDAO=new THistoryTransactionPeer();
		}
		return historyTransactionDAO;
	}
	
	
	public FieldChangeDAO getFieldChangeDAO() {
		if(fieldChangeDAO==null) {
			fieldChangeDAO=new TFieldChangePeer();
		}
		return fieldChangeDAO;
	}
	
	
	public ScriptsDAO getScriptsDAO() {
		if(scriptsDAO==null) {
			scriptsDAO=new TScriptsPeer();
		}
		return scriptsDAO;
	}	

	
	public LinkTypeDAO getLinkTypeDAO() {
		if(linkTypeDAO==null) {
			linkTypeDAO=new TLinkTypePeer();
		}
		return linkTypeDAO;
	}
	
	public WorkItemLinkDAO getWorkItemLinkDAO() {
		if(workItemLinkDAO==null) {
			workItemLinkDAO=new TWorkItemLinkPeer();
		}
		return workItemLinkDAO;
	}
	
		
	
	
	
	public LoggingLevelDAO getLoggingLevelDAO() {
		if (loggingLevelDAO == null) {
			loggingLevelDAO = new TLoggingLevelPeer();
		}
		return loggingLevelDAO;
	}
	
	
	public MotdDAO getMothDAO() {
		if (mothDAO == null) {
			mothDAO = new TMotdPeer();
		}
		return mothDAO;
	}
	
	
	public PIssueTypeDAO getPIssueTypeDAO() {
		if (pIssueTypeDAO == null) {
			pIssueTypeDAO = new TPlistTypePeer();
		}
		return pIssueTypeDAO;
	}
		
	
	public PPriorityDAO getPPriorityDAO() {
		if (pPriorityDAO == null) {
			pPriorityDAO = new TPpriorityPeer();
		}
		return pPriorityDAO;
	}
	
	public ProjectAccountDAO getProjectAccountDAO() {
		if (projectAccountDAO == null) {
			projectAccountDAO = new TProjectAccountPeer();
		}
		return projectAccountDAO;
	}
	
	
	public PSeverityDAO getPSeverityDAO() {
		if (pSeverityDAO == null) {
			pSeverityDAO = new TPseverityPeer();
		}
		return pSeverityDAO;
	}
	
	
	public PStatusDAO getPStatusDAO() {
		if (pStatusDAO == null) {
			pStatusDAO = new TPstatePeer();
		}
		return pStatusDAO;

	}
	
	
	public RoleFieldDAO getRoleFieldDAO() {
		if (roleFieldDAO == null) {
			roleFieldDAO = new TRoleFieldPeer();
		}
		return roleFieldDAO;
	}
	
	
	public WorkflowDAO getWorkflowDAO() {
		if (workflowDAO == null) {
			workflowDAO = new TWorkFlowPeer();
		}
		return workflowDAO;
	}
	
	
	public WorkflowCategoryDAO getWorkflowCategoryDAO() {
		if (workflowCategoryDAO == null) {
			workflowCategoryDAO = new TWorkFlowCategoryPeer();
		}
		return workflowCategoryDAO;	
	}
	
	
	public WorkflowRoleDAO getWorkflowRoleDAO() {
		if (workflowRoleDAO == null) {
			workflowRoleDAO = new TWorkFlowRolePeer();
		}
		return workflowRoleDAO;
	}
   
	
	public BlobDAO getBlobDAO() {
		if (blobDAO == null) {
			blobDAO = new TBLOBPeer();
		}
		return blobDAO;
	}
	
	
	public MsProjectTaskDAO getMsProjectTaskDAO() {
		if (msProjectTaskDAO == null) {
			msProjectTaskDAO = new TMSProjectTaskPeer();
		}
		return msProjectTaskDAO; 
	}
	
	
	public MsProjectExchangeDAO getMsProjectExchangeDAO() {
		if (msProjectExchangeDAO == null) {
			msProjectExchangeDAO = new TMSProjectExchangePeer();
		}
		return msProjectExchangeDAO; 
	}
	
	
	public MenuitemQueryDAO getMenuitemQueryDAO() {		
		if (menuitemQueryDAO == null) {
			menuitemQueryDAO = new TMenuitemQueryPeer();
		}
		return menuitemQueryDAO; 
	}
	
	
	public FilterCategoryDAO getFilterCategoryDAO() {		
		if (filterCategoryDAO == null) {
			filterCategoryDAO = new TFilterCategoryPeer();
		}
		return filterCategoryDAO; 
	}
	
	
	public ReportCategoryDAO getReportCategoryDAO() {
		if (reportCategoryDAO == null) {
			reportCategoryDAO = new TReportCategoryPeer();
		}
		return reportCategoryDAO; 
	}
	
	
	public ChildIssueTypeDAO getChildIssueTypeDAO() {
		if (childIssueTypeDAO == null) {
			childIssueTypeDAO = new TChildIssueTypePeer();
		}
		return childIssueTypeDAO; 
	}
	 
	
	public InitStatusDAO getInitStatusDAO() {
		if (initStatusDAO == null) {
			initStatusDAO = new TInitStatePeer();
		}
		return initStatusDAO; 
	}
	
	
	public BasketDAO getBasketDAO() {
		if (basketDAO == null) {
			basketDAO = new TBasketPeer();
		}
		return basketDAO; 
	}
	
	
	public PersonBasketDAO getPersonBasketDAO() {
		if (personBasketDAO == null) {
			personBasketDAO = new TPersonBasketPeer();
		}
		return personBasketDAO; 
	}
	
	

	
	public LastExecutedQueryDAO getLastExecutedQueryDAO() {
		if (lastExecutedQueryDAO == null) {
			lastExecutedQueryDAO = new TLastExecutedQueryPeer();
		}
		return lastExecutedQueryDAO;
	}
	
	
	public ReportPersonSettingsDAO getReportPersonSettingsDAO() {
		if (reportPersonSettingsDAO == null) {
			reportPersonSettingsDAO = new TReportPersonSettingsPeer();
		}
		return reportPersonSettingsDAO;
	}
	
	
	public WorkflowdefDAO getWorkflowdefDAO() {
		if (workflowdefDAO == null) {
			workflowdefDAO = new TWorkflowDefPeer();
		}
		return workflowdefDAO;
	}
	
	
	public WorkflowConnectDAO getWorkflowConnectDAO() {
		if (workflowConnectDAO == null) {
			workflowConnectDAO = new TWorkflowConnectPeer();
		}
		return workflowConnectDAO;
	}
	public WorkflowStationDAO getWorkflowStationDAO(){
		if (workflowStationDAO == null) {
			workflowStationDAO = new TWorkflowStationPeer();
		}
		return workflowStationDAO;
	}
	public WorkflowTransitionDAO getWorkflowTransitionDAO(){
		if (workflowTransitionDAO == null) {
			workflowTransitionDAO = new TWorkflowTransitionPeer();
		}
		return workflowTransitionDAO;
	}
	public WorkflowActivityDAO getWorkflowActivityDAO(){
		if (workflowActivityDAO == null) {
			workflowActivityDAO = new TWorkflowActivityPeer();
		}
		return workflowActivityDAO;
	}

	public WorkflowGuardDAO getWorkflowGuardDAO(){
		if (workflowGuardDAO == null) {
			workflowGuardDAO = new TWorkflowGuardPeer();
		}
		return workflowGuardDAO;
	}

	public MailTemplateDAO getMailTemplateDAO() {
		if (mailTemplateDAO == null) {
			mailTemplateDAO = new TMailTemplatePeer();
		}
		return mailTemplateDAO;
	}
	public MailTemplateDefDAO getMailTemplateDefDAO() {
		if (mailTemplateDefDAO == null) {
			mailTemplateDefDAO = new TMailTemplateDefPeer();
		}
		return mailTemplateDefDAO;
	}

	public MailTemplateConfigDAO getMailTemplateConfigDAO() {
		if (mailTemplateConfigDAO == null) {
			mailTemplateConfigDAO = new TMailTemplateConfigPeer();
		}
		return mailTemplateConfigDAO;
	}
	*/
}
