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


package com.aurel.track.dao.torque;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;

import com.aurel.track.dao.*;
import com.aurel.track.persist.*;

public class DAOFactoryTorque extends DAOFactory {
	
	private static final Logger LOGGER = LogManager.getLogger(DAOFactoryTorque.class);
	private static DAOFactoryTorque instance;
	private FieldDAO fieldDAO;
	private WorkItemDAO workItemDAO;
	private FieldConfigDAO fieldConfigDAO;
	private OptionSettingsDAO optionSettingsDAO;
	private GeneralSettingsDAO generalSettingsDAO;
	private TextBoxSettingsDAO textBoxSettingsDAO;
	private OptionDAO optionDAO;
	private ListDAO listDAO;
	private AttributeValueDAO attributeValueDAO;
	private ScreenDAO screenDAO;
	private ScreenTabDAO screenTabDAO;
	private ScreenPanelDAO screenPanelDAO;
	private ScreenFieldDAO screenFieldDAO;
	private ScreenConfigDAO screenConfigDAO;
	private ActionDAO actionDAO;
	private IssueTypeDAO issueTypeDAO;
	private ProjectDAO projectDAO;
	private ProjectTypeDAO projectTypeDAO;
	private ClassDAO classDAO;
	private SubprojectDAO subprojectDAO;
	private SeverityDAO severityDAO;
	private PriorityDAO priorityDAO;
	private ReleaseDAO releaseDAO;
	private PersonDAO personDAO;
	private StateDAO stateDAO;
	private AccessControlListDAO accessControlListDAO;
	private ConfigOptionRoleDAO configOptionRoleDAO;
	private StateChangeDAO stateChangeDAO;
	private BaseLineDAO baseLineDAO;
	private TrailDAO trailDAO;
	private NotifyFieldDAO notifyFieldDAO;
	private NotifyTriggerDAO notifyTriggerDAO;
	private NotifySettingsDAO notifySettingsDAO;
	private NotifyDAO notifyDAO;
	private QueryRepositoryDAO queryRepositoryDAO;
	private GroupMemberDAO groupMemberDAO;
	private BudgetDAO budgetDAO;
	private ActualEstimatedBudgetDAO actualEstimatedBudgetDAO;
	private AccountDAO accountDAO;
	private CostDAO costDAO;
	private SiteDAO siteDAO;
	private ReportLayoutDAO reportLayoutDAO;
	private DashboardScreenDAO dashboardScreenDAO;
	private DashboardTabDAO dashboardTabDAO;
	private DashboardPanelDAO dashboardPanelDAO;
	private DashboardFieldDAO dashboardFieldDAO;
	private DashboardParameterDAO dashboardParameterDAO;
	private ClobDAO clobDAO;
	private PrivateRepositoryDAO privateRepositoryDAO;
	private ProjectRepositoryDAO projectRepositoryDAO;
	private PublicRepositoryDAO publicRepositoryDAO;
	private VersionControlParameterDAO versionControlParameterDAO;
	private WorkItemLockDAO workItemLockDAO;
	private ExportTemplateDAO exportTemplateDAO;
	private EmailProcessedDAO emailProcessedDAO;
	private AttachmentDAO attachmentDAO;
	private RoleDAO roleDAO;
	private DepartmentDAO departmentDAO;
	private LocalizedResourcesDAO localizedResourcesDAO;
	private ComputedValuesDAO computedValuesDAO;
	private RoleListTypeDAO roleListTypeDAO;	
	private CostCenterDAO costCenterDAO;
	private SystemStateDAO systemStateDAO;
	private HistoryTransactionDAO historyTransactionDAO;
	private FieldChangeDAO fieldChangeDAO;
	private ScriptsDAO scriptsDAO;
	private LinkTypeDAO linkTypeDAO;
	private WorkItemLinkDAO workItemLinkDAO;
	private LoggingLevelDAO loggingLevelDAO;	
	private MotdDAO mothDAO;	
	private PIssueTypeDAO pIssueTypeDAO;
	private PPriorityDAO pPriorityDAO;
	private ProjectAccountDAO projectAccountDAO;
	private PSeverityDAO pSeverityDAO;
	private PStatusDAO pStatusDAO;
	private RoleFieldDAO roleFieldDAO;
	private WorkflowDAO workflowDAO;
	private WorkflowCategoryDAO workflowCategoryDAO;
	private WorkflowRoleDAO workflowRoleDAO;
	private BlobDAO blobDAO;
	private MsProjectTaskDAO msProjectTaskDAO;
	private MsProjectExchangeDAO msProjectExchangeDAO;
	private MenuitemQueryDAO menuitemQueryDAO;
	private FilterCategoryDAO filterCategoryDAO;
	private ReportCategoryDAO reportCategoryDAO;
	private ChildIssueTypeDAO childIssueTypeDAO;
	private InitStatusDAO initStatusDAO;	
	private BasketDAO basketDAO; 
	private PersonBasketDAO personBasketDAO;
	private LastExecutedQueryDAO lastExecutedQueryDAO;
	private ReportPersonSettingsDAO reportPersonSettingsDAO;
	private WorkflowdefDAO workflowdefDAO;
	private WorkflowConnectDAO workflowConnectDAO;
	private WorkflowStationDAO workflowStationDAO;
	private WorkflowCommentDAO workflowCommentDAO;

	private WorkflowTransitionDAO workflowTransitionDAO;
	private WorkflowActivityDAO workflowActivityDAO;
	private WorkflowGuardDAO workflowGuardDAO;
	private MailTemplateDAO mailTemplateDAO;
	private MailTemplateDefDAO mailTemplateDefDAO;
	private MailTemplateConfigDAO mailTemplateConfigDAO;
	private EntityChangesDAO entityChangesDAO;
	private ClusterNodeDAO clusterNodeDAO;
	private LoggedInUsersDAO loggedInUsersDAO;
	private ChildProjectTypeDAO childProjectTypeDAO;
	private PRoleDAO pRoleDAO;
	private ItemTransitionDAO itemTransitionDAO;
	private WfActivityContextParamsDAO wfActivityContextParamsDAO;

	private NavigatorLayoutDAO navigatorLayoutDAO;
	private NavigatorColumnDAO navigatorFieldsDAO;
	private NavigatorGroupingSortingDAO navigatorGroupingSortingDAO;;
	private CardGroupingFieldDAO cardGroupingFieldDAO;
	private CardFieldOptionDAO cardFieldOptionsDAO;
	private CardPanelDAO cardPanelDAO;
	private CardFieldDAO cardFieldDAO;
	private UserFeatureDAO userFeatureDAO;
	private UserLevelDAO userLevelDAO;
	private UserLevelSettingDAO userLevelSettingDAO;

	public static DAOFactoryTorque getInstance(){
		if(instance==null){
			 instance=new DAOFactoryTorque();
		}
		return instance;
	}
	
	@Override
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
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			Torque.closeConnection(db);
		}
	}
	
	@Override
	public WorkItemDAO getWorkItemDAO(){
		if(workItemDAO==null){
			workItemDAO =new TWorkItemPeer();
		}
		return workItemDAO;
	}
	
	@Override
	public FieldDAO getFieldDAO(){
		if(fieldDAO==null){
			fieldDAO=new TFieldPeer();
		}
		return fieldDAO;
	}
	
	@Override
	public FieldConfigDAO getFieldConfigDAO(){
		if(fieldConfigDAO==null){
			fieldConfigDAO=new TFieldConfigPeer();
		}
		return fieldConfigDAO;
	}

	@Override
	public OptionSettingsDAO getOptionSettingsDAO() {
		if(optionSettingsDAO==null){
			optionSettingsDAO=new TOptionSettingsPeer();
		}
		return optionSettingsDAO;
	}

	@Override
	public GeneralSettingsDAO getGeneralSettingsDAO() {		
	  if (generalSettingsDAO == null) {
		  generalSettingsDAO = new TGeneralSettingsPeer();
	  }
	  return generalSettingsDAO;
	}

	@Override
	public TextBoxSettingsDAO getTextBoxSettingsDAO() {
		if (textBoxSettingsDAO == null) {
		  textBoxSettingsDAO = new TTextBoxSettingsPeer();
		}
		return textBoxSettingsDAO;
	}

	@Override
	public OptionDAO getOptionDAO() {
		if (optionDAO == null) {
			optionDAO = new TOptionPeer();
		}
		return optionDAO;
 	}

	@Override
	public ListDAO getListDAO() {
		if (listDAO == null) {
		  listDAO = new TListPeer();
		}
		return listDAO;
	}

	@Override
	public AttributeValueDAO getAttributeValueDAO() {
		if (attributeValueDAO == null) {
			attributeValueDAO = new TAttributeValuePeer();
		}
		return attributeValueDAO;
	}
	
	@Override
	public ScreenDAO getScreenDAO() {
		if (screenDAO == null) {
			screenDAO = new TScreenPeer();
		}
		return screenDAO;
	}
	@Override
	public ScreenTabDAO getScreenTabDAO() {
		if (screenTabDAO == null) {
			screenTabDAO = new TScreenTabPeer();
		}
		return screenTabDAO;
	}
	@Override
	public ScreenPanelDAO getScreenPanelDAO() {
		if (screenPanelDAO == null) {
			screenPanelDAO = new TScreenPanelPeer();
		}
		return screenPanelDAO;
	}
	@Override
	public ScreenFieldDAO getScreenFieldDAO() {
		if (screenFieldDAO == null) {
			screenFieldDAO = new TScreenFieldPeer();
		}
		return screenFieldDAO;
	}
	@Override
	public ScreenConfigDAO getScreenConfigDAO() {
		if (screenConfigDAO == null) {
			screenConfigDAO = new TScreenConfigPeer();
		}
		return screenConfigDAO;
	}
	@Override
	public ActionDAO getActionDAO() {
		if (actionDAO == null) {
			actionDAO = new TActionPeer();
		}
		return actionDAO;
	}

	@Override
	public IssueTypeDAO getIssueTypeDAO() {
		if (issueTypeDAO == null) {
			issueTypeDAO = new TListTypePeer();
		}
		return issueTypeDAO;
	}

	@Override
	public ProjectDAO getProjectDAO() {
		if (projectDAO == null) {
			projectDAO = new TProjectPeer();
		}
		return projectDAO;
	}

	@Override
	public ProjectTypeDAO getProjectTypeDAO() {
		if (projectTypeDAO == null) {
			projectTypeDAO = new TProjectTypePeer();
		}
		return projectTypeDAO;
	}
	
	@Override
	public ClassDAO getClassDAO() {
		if (classDAO == null) {
			classDAO = new TClassPeer();
		}
		return classDAO;

	}
	@Override
	public SubprojectDAO getSubprojectDAO() {
		if (subprojectDAO == null) {
			subprojectDAO = new TProjectCategoryPeer();
		}
		return subprojectDAO;
	}
	
	@Override
	public SeverityDAO getSeverityDAO() {
		if (severityDAO == null) {
			severityDAO = new TSeverityPeer();
		}
		return severityDAO;
	}
	
	@Override
	public PriorityDAO getPriorityDAO() {
		if (priorityDAO == null) {
			priorityDAO = new TPriorityPeer();
		}
		return priorityDAO;
	}
	
	@Override
	public ReleaseDAO getReleaseDAO() {
		if (releaseDAO == null) {
			releaseDAO = new TReleasePeer();
		}
		return releaseDAO;
	}
	
	@Override
	public PersonDAO getPersonDAO() {
		if (personDAO == null) {
			personDAO = new TPersonPeer();
		}
		return personDAO;
	}
	
	@Override
	public StateDAO getStateDAO() {
		if (stateDAO == null) {
			stateDAO = new TStatePeer();
		}
		return stateDAO;
	}

	@Override
	public AccessControlListDAO getAccessControlListDAO() {
		if (accessControlListDAO == null) {
			accessControlListDAO = new TAccessControlListPeer();
		}
		return accessControlListDAO;
	}

	@Override
	public ConfigOptionRoleDAO getConfigOptionRoleDAO() {
		if (configOptionRoleDAO == null) {
			configOptionRoleDAO = new TConfigOptionsRolePeer();
		}
		return configOptionRoleDAO;
	}
	@Override
	public StateChangeDAO getStateChangeDAO(){
		if (stateChangeDAO == null) {
			stateChangeDAO = new TStateChangePeer();
		}
		return stateChangeDAO;
	}
	
	@Override
	public BaseLineDAO getBaseLineDAO(){
		if (baseLineDAO == null) {
			baseLineDAO = new TBaseLinePeer();
		}
		return baseLineDAO;
	}
	
	@Override
	public TrailDAO getTrailDAO(){
		if (trailDAO == null) {
			trailDAO = new TTrailPeer();
		}
		return trailDAO;
	}

	@Override
	public NotifyFieldDAO getNotifyFieldDAO(){
		if (notifyFieldDAO == null) {
			notifyFieldDAO = new TNotifyFieldPeer();
		}
		return notifyFieldDAO;
	}
	
	@Override
	public NotifyTriggerDAO getNotifyTriggerDAO() {
		if (notifyTriggerDAO == null) {
			notifyTriggerDAO = new TNotifyTriggerPeer();
		}
		return notifyTriggerDAO;
	}
	@Override
	public NotifySettingsDAO getNotifySettingsDAO() {
		if (notifySettingsDAO == null) {
			notifySettingsDAO = new TNotifySettingsPeer();
		}
		return notifySettingsDAO;
	}

	@Override
	public NotifyDAO getNotifyDAO() {
		if (notifyDAO == null) {
			notifyDAO = new TNotifyPeer();
		}
		return notifyDAO;
	}

		
	@Override
	public QueryRepositoryDAO getQueryRepositoryDAO() {
		if (queryRepositoryDAO == null) {
			queryRepositoryDAO = new TQueryRepositoryPeer();
		}
		return queryRepositoryDAO;
	}

	@Override
	public GroupMemberDAO getGroupMemberDAO() {
		if (groupMemberDAO == null) {
			groupMemberDAO = new TGroupMemberPeer();
		}
		return groupMemberDAO;
	}

	@Override
	public BudgetDAO getBudgetDAO() {
		if (budgetDAO == null) {
			budgetDAO = new TBudgetPeer();
		}
		return budgetDAO;
	}

	@Override
	public ActualEstimatedBudgetDAO getActualEstimatedBudgetDAO() {
		if (actualEstimatedBudgetDAO == null) {
			actualEstimatedBudgetDAO = new TActualEstimatedBudgetPeer();
		}
		return actualEstimatedBudgetDAO;
	}

	@Override
	public AccountDAO getAccountDAO() {
		if (accountDAO == null) {
			accountDAO = new TAccountPeer();
		}
		return accountDAO;
	}

	@Override
	public CostDAO getCostDAO() {
		if (costDAO == null) {
			costDAO = new TCostPeer();
		}
		return costDAO;
	}
	@Override
	public SiteDAO getSiteDAO() {
		if (siteDAO == null) {
			siteDAO = new TSitePeer();
		}
		return siteDAO;
	}
	@Override
	public ReportLayoutDAO getReportLayoutDAO() {
		if (reportLayoutDAO == null) {
			reportLayoutDAO = new TReportLayoutPeer();
		}
		return reportLayoutDAO;
	}
	@Override
	public DashboardScreenDAO getDashboardScreenDAO() {
		if (dashboardScreenDAO == null) {
			dashboardScreenDAO = new TDashboardScreenPeer();
		}
		return dashboardScreenDAO;
	}

	@Override
	public DashboardTabDAO getDashboardTabDAO() {
		if (dashboardTabDAO == null) {
			dashboardTabDAO = new TDashboardTabPeer();
		}
		return dashboardTabDAO;
	}

	@Override
	public DashboardPanelDAO getDashboardPanelDAO() {
		if (dashboardPanelDAO == null) {
			dashboardPanelDAO = new TDashboardPanelPeer();
		}
		return dashboardPanelDAO;
	}

	@Override
	public DashboardFieldDAO getDashboardFieldDAO() {
		if (dashboardFieldDAO == null) {
			dashboardFieldDAO = new TDashboardFieldPeer();
		}
		return dashboardFieldDAO;
	}
   
	@Override
	public ClobDAO getClobDAO() {
		if (clobDAO == null) {
			clobDAO = new TCLOBPeer();
		}
		return clobDAO;
	}
	
	@Override
	public PrivateRepositoryDAO getPrivateRepositoryDAO() {
		if (privateRepositoryDAO == null) {
			privateRepositoryDAO = new TPrivateReportRepositoryPeer();
		}
		return privateRepositoryDAO;
	}
	@Override
	public ProjectRepositoryDAO getProjectRepositoryDAO() {
		if (projectRepositoryDAO == null) {
			projectRepositoryDAO = new TProjectReportRepositoryPeer();
		}
		return projectRepositoryDAO;
	}

	@Override
	public PublicRepositoryDAO getPublicRepositoryDAO() {
		if (publicRepositoryDAO == null) {
			publicRepositoryDAO = new TPublicReportRepositoryPeer();
		}
		return publicRepositoryDAO;
	}


	@Override
	public VersionControlParameterDAO getVersionControlParameterDAO() {
		if (versionControlParameterDAO == null) {
			versionControlParameterDAO = new TVersionControlParameterPeer();
		}
		return versionControlParameterDAO;
	}
	
	@Override
	public WorkItemLockDAO getWorkItemLockDAO() {
		if (workItemLockDAO == null) {
			workItemLockDAO = new TWorkItemLockPeer();
		}
		return workItemLockDAO;
	}

	@Override
	public ExportTemplateDAO getExportTemplateDAO() {
		if (exportTemplateDAO == null) {
			exportTemplateDAO = new TExportTemplatePeer();
		}
		return exportTemplateDAO;
	}

	@Override
	public EmailProcessedDAO getEmailProcessedDAO() {
		if (emailProcessedDAO == null) {
			emailProcessedDAO = new TEmailProcessedPeer();
		}
		return emailProcessedDAO;
	}
	@Override
	public AttachmentDAO getAttachmentDAO() {
		if(attachmentDAO==null){
			attachmentDAO=new TAttachmentPeer();
		}
		return attachmentDAO;
	}
	
	@Override
	public RoleDAO getRoleDAO() {
		if(roleDAO==null) {
			roleDAO=new TRolePeer();
		}
		return roleDAO;
	}
	
	@Override
	public DepartmentDAO getDepartmentDAO() {
		if(departmentDAO==null) {
			departmentDAO=new TDepartmentPeer();
		}
		return departmentDAO;
	}
	
	@Override
	public LocalizedResourcesDAO getLocalizedResourcesDAO() {
		if(localizedResourcesDAO==null) {
			localizedResourcesDAO=new TLocalizedResourcesPeer();
		}
		return localizedResourcesDAO;
	}
	
	@Override
	public ComputedValuesDAO getComputedValuesDAO() {
		
		if(computedValuesDAO==null) {
			computedValuesDAO=new TComputedValuesPeer();
		}
		return computedValuesDAO;
	}
	
	@Override
	public RoleListTypeDAO getRoleListTypeDAO() {
		if(roleListTypeDAO==null) {
			roleListTypeDAO=new TRoleListTypePeer();
		}
		return roleListTypeDAO;
	}
	
	@Override
	public  DashboardParameterDAO getDashboardParameterDAO() {
		if(dashboardParameterDAO==null) {
			dashboardParameterDAO=new TDashboardParameterPeer();
		}
		return dashboardParameterDAO;
	}
	
	@Override
	public CostCenterDAO getCostCenterDAO() {
		if(costCenterDAO==null) {
			costCenterDAO=new TCostCenterPeer();
		}
		return costCenterDAO;
	}
	
	@Override
	public SystemStateDAO getSystemStateDAO() {
		if(systemStateDAO==null) {
			systemStateDAO=new TSystemStatePeer();
		}
		return systemStateDAO;
	}
	
	@Override
	public HistoryTransactionDAO getHistoryTransactionDAO() {
		if(historyTransactionDAO==null) {
			historyTransactionDAO=new THistoryTransactionPeer();
		}
		return historyTransactionDAO;
	}
	
	@Override
	public FieldChangeDAO getFieldChangeDAO() {
		if(fieldChangeDAO==null) {
			fieldChangeDAO=new TFieldChangePeer();
		}
		return fieldChangeDAO;
	}
	
	@Override
	public ScriptsDAO getScriptsDAO() {
		if(scriptsDAO==null) {
			scriptsDAO=new TScriptsPeer();
		}
		return scriptsDAO;
	}	

	@Override
	public LinkTypeDAO getLinkTypeDAO() {
		if(linkTypeDAO==null) {
			linkTypeDAO=new TLinkTypePeer();
		}
		return linkTypeDAO;
	}
	@Override
	public WorkItemLinkDAO getWorkItemLinkDAO() {
		if(workItemLinkDAO==null) {
			workItemLinkDAO=new TWorkItemLinkPeer();
		}
		return workItemLinkDAO;
	}
	
		
	
	
	@Override
	public LoggingLevelDAO getLoggingLevelDAO() {
		if (loggingLevelDAO == null) {
			loggingLevelDAO = new TLoggingLevelPeer();
		}
		return loggingLevelDAO;
	}
	
	@Override
	public MotdDAO getMothDAO() {
		if (mothDAO == null) {
			mothDAO = new TMotdPeer();
		}
		return mothDAO;
	}
	
	@Override
	public PIssueTypeDAO getPIssueTypeDAO() {
		if (pIssueTypeDAO == null) {
			pIssueTypeDAO = new TPlistTypePeer();
		}
		return pIssueTypeDAO;
	}
		
	@Override
	public PPriorityDAO getPPriorityDAO() {
		if (pPriorityDAO == null) {
			pPriorityDAO = new TPpriorityPeer();
		}
		return pPriorityDAO;
	}
	@Override
	public ProjectAccountDAO getProjectAccountDAO() {
		if (projectAccountDAO == null) {
			projectAccountDAO = new TProjectAccountPeer();
		}
		return projectAccountDAO;
	}
	
	@Override
	public PSeverityDAO getPSeverityDAO() {
		if (pSeverityDAO == null) {
			pSeverityDAO = new TPseverityPeer();
		}
		return pSeverityDAO;
	}
	
	@Override
	public PStatusDAO getPStatusDAO() {
		if (pStatusDAO == null) {
			pStatusDAO = new TPstatePeer();
		}
		return pStatusDAO;

	}
	
	@Override
	public RoleFieldDAO getRoleFieldDAO() {
		if (roleFieldDAO == null) {
			roleFieldDAO = new TRoleFieldPeer();
		}
		return roleFieldDAO;
	}
	
	@Override
	public WorkflowDAO getWorkflowDAO() {
		if (workflowDAO == null) {
			workflowDAO = new TWorkFlowPeer();
		}
		return workflowDAO;
	}
	
	@Override
	public WorkflowCategoryDAO getWorkflowCategoryDAO() {
		if (workflowCategoryDAO == null) {
			workflowCategoryDAO = new TWorkFlowCategoryPeer();
		}
		return workflowCategoryDAO;	
	}
	
	@Override
	public WorkflowRoleDAO getWorkflowRoleDAO() {
		if (workflowRoleDAO == null) {
			workflowRoleDAO = new TWorkFlowRolePeer();
		}
		return workflowRoleDAO;
	}
   
	@Override
	public BlobDAO getBlobDAO() {
		if (blobDAO == null) {
			blobDAO = new TBLOBPeer();
		}
		return blobDAO;
	}
	
	@Override
	public MsProjectTaskDAO getMsProjectTaskDAO() {
		if (msProjectTaskDAO == null) {
			msProjectTaskDAO = new TMSProjectTaskPeer();
		}
		return msProjectTaskDAO; 
	}
	
	@Override
	public MsProjectExchangeDAO getMsProjectExchangeDAO() {
		if (msProjectExchangeDAO == null) {
			msProjectExchangeDAO = new TMSProjectExchangePeer();
		}
		return msProjectExchangeDAO; 
	}
	
	@Override
	public MenuitemQueryDAO getMenuitemQueryDAO() {		
		if (menuitemQueryDAO == null) {
			menuitemQueryDAO = new TMenuitemQueryPeer();
		}
		return menuitemQueryDAO; 
	}
	
	@Override
	public FilterCategoryDAO getFilterCategoryDAO() {		
		if (filterCategoryDAO == null) {
			filterCategoryDAO = new TFilterCategoryPeer();
		}
		return filterCategoryDAO; 
	}
	
	@Override
	public ReportCategoryDAO getReportCategoryDAO() {
		if (reportCategoryDAO == null) {
			reportCategoryDAO = new TReportCategoryPeer();
		}
		return reportCategoryDAO; 
	}
	
	@Override
	public ChildIssueTypeDAO getChildIssueTypeDAO() {
		if (childIssueTypeDAO == null) {
			childIssueTypeDAO = new TChildIssueTypePeer();
		}
		return childIssueTypeDAO; 
	}
	 
	@Override
	public InitStatusDAO getInitStatusDAO() {
		if (initStatusDAO == null) {
			initStatusDAO = new TInitStatePeer();
		}
		return initStatusDAO; 
	}
	
	@Override
	public BasketDAO getBasketDAO() {
		if (basketDAO == null) {
			basketDAO = new TBasketPeer();
		}
		return basketDAO; 
	}
	
	@Override
	public PersonBasketDAO getPersonBasketDAO() {
		if (personBasketDAO == null) {
			personBasketDAO = new TPersonBasketPeer();
		}
		return personBasketDAO; 
	}
	
	

	@Override
	public LastExecutedQueryDAO getLastExecutedQueryDAO() {
		if (lastExecutedQueryDAO == null) {
			lastExecutedQueryDAO = new TLastExecutedQueryPeer();
		}
		return lastExecutedQueryDAO;
	}
	
	@Override
	public ReportPersonSettingsDAO getReportPersonSettingsDAO() {
		if (reportPersonSettingsDAO == null) {
			reportPersonSettingsDAO = new TReportPersonSettingsPeer();
		}
		return reportPersonSettingsDAO;
	}
	
	@Override
	public WorkflowdefDAO getWorkflowdefDAO() {
		if (workflowdefDAO == null) {
			workflowdefDAO = new TWorkflowDefPeer();
		}
		return workflowdefDAO;
	}
	
	@Override
	public WorkflowConnectDAO getWorkflowConnectDAO() {
		if (workflowConnectDAO == null) {
			workflowConnectDAO = new TWorkflowConnectPeer();
		}
		return workflowConnectDAO;
	}
	@Override
	public WorkflowStationDAO getWorkflowStationDAO(){
		if (workflowStationDAO == null) {
			workflowStationDAO = new TWorkflowStationPeer();
		}
		return workflowStationDAO;
	}
	@Override
	public WorkflowCommentDAO getWorkflowCommentDAO(){
		if (workflowCommentDAO == null) {
			workflowCommentDAO = new TWorkflowCommentPeer();
		}
		return workflowCommentDAO;
	}

	@Override
	public WorkflowTransitionDAO getWorkflowTransitionDAO(){
		if (workflowTransitionDAO == null) {
			workflowTransitionDAO = new TWorkflowTransitionPeer();
		}
		return workflowTransitionDAO;
	}
	@Override
	public WorkflowActivityDAO getWorkflowActivityDAO(){
		if (workflowActivityDAO == null) {
			workflowActivityDAO = new TWorkflowActivityPeer();
		}
		return workflowActivityDAO;
	}

	@Override
	public WorkflowGuardDAO getWorkflowGuardDAO(){
		if (workflowGuardDAO == null) {
			workflowGuardDAO = new TWorkflowGuardPeer();
		}
		return workflowGuardDAO;
	}

	@Override
	public MailTemplateDAO getMailTemplateDAO() {
		if (mailTemplateDAO == null) {
			mailTemplateDAO = new TMailTemplatePeer();
		}
		return mailTemplateDAO;
	}
	@Override
	public MailTemplateDefDAO getMailTemplateDefDAO() {
		if (mailTemplateDefDAO == null) {
			mailTemplateDefDAO = new TMailTemplateDefPeer();
		}
		return mailTemplateDefDAO;
	}

	@Override
	public MailTemplateConfigDAO getMailTemplateConfigDAO() {
		if (mailTemplateConfigDAO == null) {
			mailTemplateConfigDAO = new TMailTemplateConfigPeer();
		}
		return mailTemplateConfigDAO;
	}
	
	@Override
	public EntityChangesDAO getEntityChangesDAO() {
		if (entityChangesDAO == null) {
			entityChangesDAO = new TEntityChangesPeer();
		}
		return entityChangesDAO;
	}
	
	@Override
	public ClusterNodeDAO getClusterNodeDAO() {
		if (clusterNodeDAO == null) {
			clusterNodeDAO = new TClusterNodePeer();
		}
		return clusterNodeDAO;
	}
	
	@Override
	public LoggedInUsersDAO getLoggedInUsersDAO() {
		if (loggedInUsersDAO == null) {
			loggedInUsersDAO = new TLoggedInUsersPeer();
		}
		return loggedInUsersDAO;
	}
	
	@Override
	public ChildProjectTypeDAO getChildProjectTypeDAO() {
		if (childProjectTypeDAO == null) {
			childProjectTypeDAO = new TChildProjectTypePeer();
		}
		return childProjectTypeDAO;
	}
	
	@Override
	public PRoleDAO getPRoleDAO() {
		if (pRoleDAO == null) {
			pRoleDAO = new TPRolePeer();
		}
		return pRoleDAO;
	}
	
	@Override
	public ItemTransitionDAO getItemTransitionDAO() {
		if (itemTransitionDAO==null) {
			itemTransitionDAO = new TItemTransitionPeer();
		}
		return itemTransitionDAO;
	}
	
	@Override
	public WfActivityContextParamsDAO getWfActivityContextParamsDAO() {
		if (wfActivityContextParamsDAO==null) {
			wfActivityContextParamsDAO = new TWfActivityContextParamsPeer();
		}
		return wfActivityContextParamsDAO;
	}


	@Override
	public  NavigatorLayoutDAO getNavigatorLayoutDAO(){
		if (navigatorLayoutDAO==null) {
			navigatorLayoutDAO = new TNavigatorLayoutPeer();
		}
		return navigatorLayoutDAO;
	}
	
	@Override
	public NavigatorColumnDAO getNavigatorFieldsDAO() {
		if (navigatorFieldsDAO==null) {
			navigatorFieldsDAO = new TNavigatorColumnPeer();
		}
		return navigatorFieldsDAO;
	}
	
	@Override
	public NavigatorGroupingSortingDAO getNavigatorGroupingSortingDAO() {
		if (navigatorGroupingSortingDAO==null) {
			navigatorGroupingSortingDAO = new TNavigatorGroupingSortingPeer();
		}
		return navigatorGroupingSortingDAO;
	}
	
	@Override
	public CardGroupingFieldDAO getCardGroupingFieldDAO(){
		if (cardGroupingFieldDAO==null) {
			cardGroupingFieldDAO = new TCardGroupingFieldPeer();
		}
		return cardGroupingFieldDAO;
	}
	@Override
	public CardFieldOptionDAO getCardFieldOptionsDAO(){
		if (cardFieldOptionsDAO==null) {
			cardFieldOptionsDAO = new TCardFieldOptionPeer();
		}
		return cardFieldOptionsDAO;
	}
	@Override
	public CardPanelDAO getCardPanelDAO(){
		if (cardPanelDAO==null) {
			cardPanelDAO = new TCardPanelPeer();
		}
		return cardPanelDAO;
	}
	@Override
	public CardFieldDAO getCardFieldDAO(){
		if (cardFieldDAO==null) {
			cardFieldDAO = new TCardFieldPeer();
		}
		return cardFieldDAO;
	}
	
	@Override
	public UserFeatureDAO getUserFeatureDAO(){
		if (userFeatureDAO==null) {
			userFeatureDAO = new TUserFeaturePeer();
		}
		return userFeatureDAO;
	}
	
	
	@Override
	public UserLevelDAO getUserLevelDAO() {
		if (userLevelDAO==null) {
			userLevelDAO = new TUserLevelPeer();
		}
		return userLevelDAO;
	}
	
	@Override
	public UserLevelSettingDAO getUserLevelSettingDAO() {
		if (userLevelSettingDAO==null) {
			userLevelSettingDAO = new TUserLevelSettingPeer();
		}
		return userLevelSettingDAO;
	}
}
