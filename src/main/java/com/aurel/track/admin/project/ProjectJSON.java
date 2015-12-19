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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.TreeNode;

public class ProjectJSON {
	
	private static final String TEMPLATE_IS_ACTIVE = "templateIsActive";
	private static final String HAS_PRIVATE_WORKSPACE = "hasPrivateWorkspace";
	
	private static final String MAIN_RELEASE = "mainRelease";
	private static final String CHILD_RELEASE = "childRelease";
	private static final String SHOW_CLOSED_RELEASES = "showClosedReleases";
	
	private static final String LAST_SELECTIONS = "lastSelections";
	private static final String LAST_SELECTED_SECTION = "lastSelectedSection";
	private static final String LAST_SELECTED_TAB = "lastSelectedTab";
	
	
	
	static interface JSON_FIELDS {
		//project list fields
		String MENUPATH = "menuPath"; 
		String URL = "url";
		String USE_AJAX = "useAJAX";
		String SCRIPT = "script";
		
		//project details
		//project base
		String PROJECT_BASE_BEAN_PREFIX = "projectBaseTO.";
		String PROJECT_LABEL = PROJECT_BASE_BEAN_PREFIX+JSONUtility.JSON_FIELDS.LABEL;
		String PROJECT_DESCRIPTION = PROJECT_BASE_BEAN_PREFIX+JSONUtility.JSON_FIELDS.DESCRIPTION;
		String PROJECT_TYPE_ID = PROJECT_BASE_BEAN_PREFIX+"projectTypeID";
		String PROJECT_TYPE_LIST = "projectTypeList";
		String PROJECT_STATUS_ID = PROJECT_BASE_BEAN_PREFIX+"projectStatusID";
		String PROJECT_STATUS_LIST = "projectStatusList";

		String LINKING = PROJECT_BASE_BEAN_PREFIX+"linking";
		String HAS_RELEASE = "hasRelease";
		String USE_RELEASE = PROJECT_BASE_BEAN_PREFIX+"useRelease";
		String PREFIX = PROJECT_BASE_BEAN_PREFIX+"prefix";
		String HAS_PREFIX = "hasPrefix";
		
		//project accounting
		//whether the project accounting is defined in project type (should be configurable at all)
		String HAS_ACCOUNTING = "hasAccounting";
		//bean prefix
		String PROJECT_ACCOUNTING_BEAN_PREFIX = "projectAccountingTO.";
		//whether the accounting is inherited from parent (or other ancestor)
		String ACCOUNTING_INHERITED = PROJECT_ACCOUNTING_BEAN_PREFIX+"accountingInherited";
		//whether the project accounting is defined in project
		//default account for project
		String ACCOUNT_LIST = "accountList";
		String DEFAULT_ACCOUNT = PROJECT_ACCOUNTING_BEAN_PREFIX+"defaultAccount";
		//work tracking parameters
		String WORK_TRACKING = PROJECT_ACCOUNTING_BEAN_PREFIX+"workTracking";
		String WORK_DEFAULT_UNIT = PROJECT_ACCOUNTING_BEAN_PREFIX+"defaultWorkUnit";
		String WORK_UNIT_LIST = "workUnitList";
		String HOURS_PER_WORKDAY = PROJECT_ACCOUNTING_BEAN_PREFIX+"hoursPerWorkday";
		//cost tracking parameters
		String COST_TRACKING = PROJECT_ACCOUNTING_BEAN_PREFIX+"costTracking";
		String CURRENCY_NAME = PROJECT_ACCOUNTING_BEAN_PREFIX+"currencyName";
		String CURRENCY_SYMBOL = PROJECT_ACCOUNTING_BEAN_PREFIX+"currencySymbol";
		
		//project defaults
		String PROJECT_DEFAULTS_BEAN_PREFIX = "projectDefaultsTO.";
		String DEFAULT_MAMAGER_ID = PROJECT_DEFAULTS_BEAN_PREFIX+"defaultManagerID";
		String MANAGER_LIST  = "managerList";
		String DEFAULT_RESPONSIBLE_ID = PROJECT_DEFAULTS_BEAN_PREFIX+"defaultResponsibleID";
		String RESPONSIBLE_LIST  = "responsibleList";
		String INITIAL_STATUS_ID = PROJECT_DEFAULTS_BEAN_PREFIX+"initialStatusID";
		String STATUS_LIST  = "statusList";
		String DEFAULT_ISSUETYPE_ID = PROJECT_DEFAULTS_BEAN_PREFIX+"defaultIssueTypeID";
		String ISSUETYPE_LIST = "issueTypeList";
		String DEFAULT_PRIORITY_ID = PROJECT_DEFAULTS_BEAN_PREFIX+"defaultPriorityID";
		String PRIORITY_LIST = "priorityList";
		String DEFAULT_SEVERITY_ID = PROJECT_DEFAULTS_BEAN_PREFIX+"defaultSeverityID";
		String SEVERITY_LIST = "severityList";
		String PREFILL_BY = PROJECT_DEFAULTS_BEAN_PREFIX+"prefillBy";
		
		//initial statuses pro issueType
		String INIT_STATUSES = "initStatuses";
		String ISSUE_TYPE = "issueType";
		String STATUS = "status";
		String ACTIVE = "active";

		//project reload after save/delete
		String NODE_TO_RELOAD = "nodeToReload";
		String RELOAD_PROJECT_TREE = "reloadProjectTree";
		String RELOAD_PROJECT_CONFIG_TREE = "reloadProjectConfigTree";
		String DELETED_PROJECT_ID = "deletedProjectID";
		
		
		//project configuration type
		String BRANCH_ROOT = "branchRoot";
		String PROJECT_ID = "projectID";
		String PRIVATE_PROJECT = "privateProject";
		String SUBPROJECT = "subproject";
		
		//assignment nodes
		String PROJECT_NODE_TO_RELOAD = "projectNodeToReload";
		String PROJECT_NODE_TO_SELECT = "projectNodeToSelect";
		String PROJECT_CONFIG_TYPE_NODE_TO_SELECT = "projectConfigTypeNodeToSelect";
	}
	
	public static String encodeProjectLastSelections(boolean hasPrivateWorkspace, boolean templateIsActive, String lastSelectedSection, Integer lastSelectedTab, String mainRelease, String childRelease, boolean showClosedReleases){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, HAS_PRIVATE_WORKSPACE, hasPrivateWorkspace);
		JSONUtility.appendBooleanValue(sb, TEMPLATE_IS_ACTIVE, templateIsActive);
		JSONUtility.appendStringValue(sb, MAIN_RELEASE, mainRelease);
		JSONUtility.appendStringValue(sb, CHILD_RELEASE, childRelease);
		JSONUtility.appendBooleanValue(sb, SHOW_CLOSED_RELEASES, showClosedReleases);
		JSONUtility.appendFieldName(sb, LAST_SELECTIONS).append(":{");
		JSONUtility.appendStringValue(sb, LAST_SELECTED_SECTION, lastSelectedSection, lastSelectedTab==null);
		JSONUtility.appendIntegerValue(sb, LAST_SELECTED_TAB,lastSelectedTab,true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	public static String getProjectsEagerJSON(List<TreeNode> nodes) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		Collections.sort(nodes);
		for (Iterator<TreeNode> iterator = nodes.iterator(); iterator.hasNext();) {
			TreeNode treeNode = iterator.next();
			stringBuilder.append("{");
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, treeNode.getId());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, treeNode.getLabel());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, treeNode.getIcon());
			JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, treeNode.getLeaf());
			List<TreeNode> children = treeNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				JSONUtility.appendJSONValue(stringBuilder, JSONUtility.JSON_FIELDS.CHILDREN, getProjectsEagerJSON(children));
			}			
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");//end data
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for tree folder children 
	 * @param children
	 * @return
	 */
	public static String getProjectsJSON(List<TreeNodeBaseTO> children, boolean isTemplate){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		for (Iterator<TreeNodeBaseTO> iterator = children.iterator(); iterator.hasNext();) {
			TreeNodeBaseTO projectTreeNodeTO = iterator.next();
			stringBuilder.append("{");
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, projectTreeNodeTO.getId());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, projectTreeNodeTO.getText());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TT, projectTreeNodeTO.getText());
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, projectTreeNodeTO.getIconCls());
			JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, projectTreeNodeTO.isLeaf());
			JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.MENUPATH, "menu.admin.project");
			JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.URL, "com.trackplus.admin.projectConfig(" + projectTreeNodeTO.getId() + ", " + isTemplate + ")");			
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.USE_AJAX, true);
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.SCRIPT, true, true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");//end data
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for configuration children 
	 * @param projectTypeTreeNodeTO
	 * @return
	 */
	static String getChildJSON(ProjectTreeNodeTO projectTypeTreeNodeTO){
		return getChildJSON(projectTypeTreeNodeTO, false);
	}

	/**
	 * Gets the JSON string for configuration children 
	 * @param projectTreeNodeTO
	 * @return
	 */
	private static String getNodeJSON(ProjectTreeNodeTO projectTreeNodeTO){
		StringBuilder stringBuilder=new StringBuilder();
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, projectTreeNodeTO.getId());
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, projectTreeNodeTO.getText());
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, projectTreeNodeTO.getIconCls());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PROJECT_ID, projectTreeNodeTO.getProjectID());
		if (projectTreeNodeTO.isPrivateProject()) {
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.PRIVATE_PROJECT, projectTreeNodeTO.isPrivateProject());
		}
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.BRANCH_ROOT, projectTreeNodeTO.getBranchRoot());
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, projectTreeNodeTO.isLeaf(), true);
		return stringBuilder.toString();
	}
	
	
	/**
	 * Gets the JSON string for configuration children 
	 * @param projectTreeNodeTO
	 * @param last
	 * @return
	 */
	static String getChildJSON(ProjectTreeNodeTO projectTreeNodeTO, boolean last){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append(getNodeJSON(projectTreeNodeTO));
		stringBuilder.append("}");
		if (!last) {
			stringBuilder.append(",");
		}
		return stringBuilder.toString();
	}
	
	
	
	/**
	 * Project details by loading a project
	 * @param projectBaseTO
	 * @param projectDefaultsTO
	 * @return
	 */
	static String loadProjectDetailJSON(boolean subproject, ProjectBaseTO projectBaseTO, ProjectAccountingTO projectAccountingTO, ProjectDefaultsTO projectDefaultsTO,
			Map<Integer, String> invalidFieldDefaults
			)
	{
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(stringBuilder, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.SUBPROJECT, subproject);
		//projectBaseTO
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PROJECT_LABEL, projectBaseTO.getLabel());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PROJECT_DESCRIPTION, projectBaseTO.getDescription());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PROJECT_TYPE_ID, projectBaseTO.getProjectTypeID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.PROJECT_TYPE_LIST, projectBaseTO.getProjectTypeList());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PROJECT_STATUS_ID, projectBaseTO.getProjectStatusID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.PROJECT_STATUS_LIST, (List)projectBaseTO.getProjectStatusList());
		stringBuilder.append(getAccountingJSON(projectAccountingTO));
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.LINKING, projectBaseTO.isLinking());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.HAS_RELEASE, projectBaseTO.isHasRelease());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.USE_RELEASE, projectBaseTO.isUseRelease());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PREFIX, projectBaseTO.getPrefix());
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.HAS_PREFIX, projectBaseTO.isHasPrefix());
		//projectDefaultsTO
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_MAMAGER_ID, projectDefaultsTO.getDefaultManagerID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.MANAGER_LIST, (List)projectDefaultsTO.getManagerList());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_RESPONSIBLE_ID, projectDefaultsTO.getDefaultResponsibleID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.RESPONSIBLE_LIST, (List)projectDefaultsTO.getResponsibleList());
		
		stringBuilder.append(getDefaultSystemListsJSON(projectDefaultsTO));
		
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PREFILL_BY, projectDefaultsTO.getPrefillBy());

		
		JSONUtility.appendFieldName(stringBuilder, JSON_FIELDS.INIT_STATUSES).append(":[");
		Map<Integer, Integer> issueTypeToInitStatusMap = projectDefaultsTO.getIssueTypeToInitStatusMap();
		Map<Integer, List<ILabelBean>> issueTypeToStatusListMap = projectDefaultsTO.getIssueTypeToStatusListMap();
		for (Iterator<ILabelBean> iterator = projectDefaultsTO.getIssueTypeList().iterator(); iterator.hasNext();) {
			ILabelBean listTypeBean = iterator.next();
			Integer issueTypeID = listTypeBean.getObjectID(); 
			Integer initialStatusForIssueType = issueTypeToInitStatusMap.get(issueTypeID);
			Integer statusID = null;
			boolean active = false;
			if (initialStatusForIssueType!=null) {
				statusID = initialStatusForIssueType;
				active = true;
			} else {
				statusID = projectDefaultsTO.getInitialStatusID();
			}
			stringBuilder.append("{");
			JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.LABEL, listTypeBean.getLabel());
			JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.ISSUE_TYPE, issueTypeID);
			JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.STATUS_LIST, issueTypeToStatusListMap.get(issueTypeID));
			JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.STATUS, statusID);
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.ACTIVE, active, true);
			stringBuilder.append("}");
			if (iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("],");
		stringBuilder.append(getErrorsJSON(invalidFieldDefaults));

		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}



	/**
	 * Gets the errors JSON
	 * @param invalidFieldDefaults
	 * @return
	 */
	private static String getErrorsJSON(Map<Integer, String> invalidFieldDefaults) {
		StringBuilder stringBuilder = new StringBuilder();
		if (invalidFieldDefaults!=null && !invalidFieldDefaults.isEmpty()) {
			stringBuilder.append(JSONUtility.JSON_FIELDS.ERRORS).append(":{");
			for (Iterator<Integer> iterator = invalidFieldDefaults.keySet().iterator(); iterator.hasNext();) {
				Integer fieldID = iterator.next();
				String errorMessage = invalidFieldDefaults.get(fieldID);
				String fieldName = null;
				switch (fieldID.intValue()) {
				case SystemFields.MANAGER:
					fieldName = JSON_FIELDS.DEFAULT_MAMAGER_ID;
					break;
				case SystemFields.RESPONSIBLE:
					fieldName = JSON_FIELDS.DEFAULT_RESPONSIBLE_ID;
					break;
				case SystemFields.ISSUETYPE:
					fieldName = JSON_FIELDS.DEFAULT_ISSUETYPE_ID;
					break;
				case SystemFields.STATE:
					fieldName = JSON_FIELDS.INITIAL_STATUS_ID;
					break;
				case SystemFields.PRIORITY:
					fieldName = JSON_FIELDS.DEFAULT_PRIORITY_ID;
					break;
				case SystemFields.SEVERITY:
					fieldName = JSON_FIELDS.DEFAULT_SEVERITY_ID;
					break;
				case ProjectBL.INVALID_DEFAULT_ACCOUNT:
					fieldName = JSON_FIELDS.DEFAULT_ACCOUNT;
					break;
				}
				JSONUtility.appendStringValue(stringBuilder, fieldName, errorMessage, !iterator.hasNext());
			}
			stringBuilder.append("},");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON for project accounting 
	 * @param projectAccountingTO
	 * @return
	 */
	private static String getAccountingJSON(ProjectAccountingTO projectAccountingTO) {
		StringBuilder stringBuilder=new StringBuilder();
		if (projectAccountingTO==null) {
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.HAS_ACCOUNTING, false);
		} else {
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.HAS_ACCOUNTING, true);
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.ACCOUNTING_INHERITED, projectAccountingTO.isAccountingInherited());
			JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_ACCOUNT, projectAccountingTO.getDefaultAccount());
			JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.ACCOUNT_LIST, (List)projectAccountingTO.getAccountList());
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.WORK_TRACKING, projectAccountingTO.isWorkTracking());
			JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.WORK_DEFAULT_UNIT, projectAccountingTO.getDefaultWorkUnit());
			JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.WORK_UNIT_LIST, projectAccountingTO.getWorkUnitList());
			JSONUtility.appendDoubleValue(stringBuilder, JSON_FIELDS.HOURS_PER_WORKDAY, projectAccountingTO.getHoursPerWorkday());
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.COST_TRACKING, projectAccountingTO.isCostTracking());
			JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.CURRENCY_NAME, projectAccountingTO.getCurrencyName());
			JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.CURRENCY_SYMBOL, projectAccountingTO.getCurrencySymbol());
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON for project accounting 
	 * @param projectDefaultsTO
	 * @return
	 */
	private static String getDefaultSystemListsJSON(ProjectDefaultsTO projectDefaultsTO) {
		StringBuilder stringBuilder=new StringBuilder();
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.INITIAL_STATUS_ID, projectDefaultsTO.getInitialStatusID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.STATUS_LIST, projectDefaultsTO.getStatusList());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_ISSUETYPE_ID, projectDefaultsTO.getDefaultIssueTypeID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.ISSUETYPE_LIST, projectDefaultsTO.getIssueTypeList());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_PRIORITY_ID, projectDefaultsTO.getDefaultPriorityID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.PRIORITY_LIST, projectDefaultsTO.getPriorityList());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_SEVERITY_ID, projectDefaultsTO.getDefaultSeverityID());
		JSONUtility.appendILabelBeanList(stringBuilder, JSON_FIELDS.SEVERITY_LIST, projectDefaultsTO.getSeverityList());
		return stringBuilder.toString();
	}
	
	/**
	 * JSON data for refreshing the system fields after projectType change
	 * @param projectDefaultsTO
	 * @return
	 */
	static String loadProjectTypeChangeJSON(ProjectAccountingTO projectAccountingTO,
			boolean hasReleases, ProjectDefaultsTO projectDefaultsTO, Map<Integer, String> invalidDefaults) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(stringBuilder, JSONUtility.JSON_FIELDS.DATA).append(":{");
		stringBuilder.append(getAccountingJSON(projectAccountingTO));
		stringBuilder.append(getDefaultSystemListsJSON(projectDefaultsTO));
		stringBuilder.append(getErrorsJSON(invalidDefaults));
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.HAS_RELEASE, hasReleases, true);
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	
	
	/**
	 * Save result JSON
	 * @param projectNodeToReload
	 * @param projectNodeToSelect
	 * @param reloadProjectTree
	 * @param projectConfigTypeNodeToSelect
	 * @param reloadProjectConfigTree
	 * @return
	 */
	public static String saveProjectDetailJSON(String projectNodeToReload, String projectNodeToSelect, boolean reloadProjectTree,
			String projectConfigTypeNodeToSelect, boolean reloadProjectConfigTree) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECT_NODE_TO_RELOAD, projectNodeToReload);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECT_NODE_TO_SELECT, projectNodeToSelect);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RELOAD_PROJECT_TREE, reloadProjectTree);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECT_CONFIG_TYPE_NODE_TO_SELECT, projectConfigTypeNodeToSelect);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RELOAD_PROJECT_CONFIG_TREE, reloadProjectConfigTree);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Save result JSON
	 * @param nodeToReload
	 * @param nodeToSelect
	 * @param projectID
	 * @return
	 */
	static String saveDeleteDetailJSON(String nodeToReload, String nodeToSelect, Integer projectID, boolean reloadProjectTree) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECT_NODE_TO_RELOAD, nodeToReload);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECT_NODE_TO_SELECT, nodeToSelect);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RELOAD_PROJECT_TREE, reloadProjectTree);	
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.DELETED_PROJECT_ID, projectID);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();
	}
	public static String encodeJSONErrorList(List<ControlError> list, Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
		sb.append(JSONUtility.JSON_FIELDS.ERRORS).append(":");
		sb.append("[");
		if(list!=null){
			for (Iterator<ControlError> iterator = list.iterator(); iterator.hasNext();) {
				ControlError controlError = iterator.next();
				sb.append("{");
				JSONUtility.appendStringList(sb, "controlPath", controlError.getControlPath());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, controlError.getErrorMessage(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
	
	/*public static String encodeToolbarItemConfig(TPersonBean personBean, Integer projectID) {
		StringBuilder sb = new StringBuilder();
		Integer statusFlag = SystemStatusBL.getStatusFlag(projectID, TSystemStateBean.ENTITYFLAGS.PROJECTSTATE);
		boolean templateIsActive = false;
		if(statusFlag != null) {
			if(statusFlag.intValue() == TSystemStateBean.STATEFLAGS.ACTIVE) {
				templateIsActive = true;
			}
			if(statusFlag.intValue() == TSystemStateBean.STATEFLAGS.CLOSED) {
				templateIsActive = false;
			}
		}
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		
		JSONUtility.appendBooleanValue(sb, TEMPLATE_IS_ACTIVE, templateIsActive, true);
		sb.append("}");
		return sb.toString();
	}*/
	
	public static String encodeChangeTemplateState(boolean success, boolean isActive) {
		StringBuilder sb = new StringBuilder();
		if(success) {
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
			JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
			JSONUtility.appendBooleanValue(sb,TEMPLATE_IS_ACTIVE, isActive, true);
			sb.append("}");
			sb.append("}");
		}else {
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
			sb.append("data:{");
			sb.append("}");
			sb.append("}");
		}
		return sb.toString();
	}
}
