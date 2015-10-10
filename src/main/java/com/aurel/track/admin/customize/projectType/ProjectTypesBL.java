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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.lists.ListBL.ICONS_CLS;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.assignments.itemType.ProjectTypeItemTypeAssignmentBaseFacade;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPlistTypeBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityExporter;
import com.aurel.track.configExchange.exporter.EntityExporterException;
import com.aurel.track.configExchange.impl.ProjectTypeExporter;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PIssueTypeDAO;
import com.aurel.track.dao.ProjectTypeDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IconClass;
import com.aurel.track.util.IntegerStringBean;

public class ProjectTypesBL {
	private static final Logger LOGGER = LogManager.getLogger(ProjectTypesBL.class);
	private static ProjectTypeDAO projectTypeDAO = DAOFactory.getFactory().getProjectTypeDAO();
	private static PIssueTypeDAO pIssueTypeDAO = DAOFactory.getFactory().getPIssueTypeDAO();

	public static final int ERROR_CANT_DELETE_ADMIN = 2;
	
	private static String LINK_CHAR = "_";
	public static String PROJECTTYPE_ICON_CLASS = "projecttypes-ticon";
	private static String FIELD_CONFIG_ICON_CLASS = "forms-ticon";
	private static String SCREEN_ASSIGN_ICON_CLASS = "fields-ticon";
	private static String WORKFLOW_ICON_CLASS = "workflow-ticon";

	public interface PROJECT_TYPE_ASSIGNMENTS {
		public final static int STATUS_ASSIGNMENT = 1;
		public final static int ISSUE_TYPE_ASSIGNMENT = 2;
		public final static int PRIORITY_ASSIGNMENT = 3;
		public final static int SEVERITY_ASSIGNMENT = 4;
		public final static int FIELD_CONFIGURATION = 5;
		public final static int SCREEN_ASSIGNMENT = 6;
		public final static int WORKFLOW_ASSIGNMENT = 7;
		public final static int CHILD_PROJECT_TYPE_ASSIGNMENT = 8;
		public final static int ROLE_ASSIGNMENT = 9;
	}

	public interface PROJECT_TYPE_FLAG {
		public final static int GENERAL = 1;
		public final static int HELPDESK = 2;
		public final static int SCRUM = 3;
		public final static int KANBAN = 4;
	}

	/**
	 * Encode a node
	 * 
	 * @param projectTypeIDTokens
	 * @return
	 */
	static String encodeNode(ProjectTypeIDTokens projectTypeIDTokens) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(projectTypeIDTokens.getProjectTypeID());
		Integer configType = projectTypeIDTokens.getConfigType();
		if (configType != null) {
			stringBuffer.append(LINK_CHAR);
			stringBuffer.append(configType);
		}
		return stringBuffer.toString();
	}

	/**
	 * Decode a node
	 * 
	 * @param id
	 * @return
	 */
	public static ProjectTypeIDTokens decodeNode(String id) {
		ProjectTypeIDTokens projectTypeIDTokens = new ProjectTypeIDTokens();
		if (id != null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens != null && tokens.length > 0) {
				projectTypeIDTokens.setProjectTypeID(Integer.valueOf(tokens[0]));
				if (tokens.length > 1) {
					if (tokens[1] != null && !"".equals(tokens[1])) {
						projectTypeIDTokens.setConfigType(Integer.valueOf(tokens[1]));
					}
				}
			}
		}
		return projectTypeIDTokens;
	}

	public static List<TProjectTypeBean> loadNonPrivate() {
		return projectTypeDAO.loadNonPrivate();
	}

	public static List<TProjectTypeBean> loadPrivate() {
		return projectTypeDAO.loadPrivate();
	}

	public static List<TProjectTypeBean> loadAll() {
		return projectTypeDAO.loadAll();
	}

	public static TProjectTypeBean loadByPrimaryKey(Integer projectTypeID) {
		return projectTypeDAO.loadByPrimaryKey(projectTypeID);
	}

	/**
	 * Gets the projectType bean for a project
	 * 
	 * @param projectID
	 * @return
	 */
	public static TProjectTypeBean getProjectTypeByProject(Integer projectID) {
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		if (projectBean != null) {
			Integer projectTypeID = projectBean.getProjectType();
			if (projectTypeID != null) {
				return loadByPrimaryKey(projectTypeID);
			}
		}
		return null;
	}

	/**
	 * Gets the project type node
	 * 
	 * @param locale
	 * @return
	 */
	private static List<ProjectTypeTreeNodeTO> getFirstLevelNodes(Locale locale) {
		List<ProjectTypeTreeNodeTO> projectTypeTreeNodeList = new LinkedList<ProjectTypeTreeNodeTO>();
		List<TProjectTypeBean> projectTypeBeans = loadAll();
		for (TProjectTypeBean projectTypeBean : projectTypeBeans) {
			ProjectTypeTreeNodeTO projectTypeTreeNodeTO = new ProjectTypeTreeNodeTO(encodeNode(new ProjectTypeIDTokens(projectTypeBean.getObjectID())),
					projectTypeBean.getLabel(), PROJECTTYPE_ICON_CLASS, false);
			projectTypeTreeNodeTO.setProjectTypeID(projectTypeBean.getObjectID());
			projectTypeTreeNodeList.add(projectTypeTreeNodeTO);
		}
		return projectTypeTreeNodeList;
	}

	/**
	 * Gets the hard coded assignment nodes for a selected project type
	 * 
	 * @param locale
	 * @return
	 */
	private static String getAssignmentNodes(Integer projectTypeID, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(SystemFields.INTEGER_STATE);
		fieldIDs.add(SystemFields.INTEGER_PRIORITY);
		fieldIDs.add(SystemFields.INTEGER_SEVERITY);
		fieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		// issueType assignment
		String issueTypeLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.assignItemType", locale);
		String itemTypeNodeID = ProjectTypeItemTypeAssignmentBaseFacade.getProjectTypeAssignmnentBranchNodeID(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.ISSUE_TYPE_ASSIGNMENT));
		ProjectTypeTreeNodeTO issueTypeAssignmnet = new ProjectTypeTreeNodeTO(itemTypeNodeID, issueTypeLabel, ICONS_CLS.ISSUETYPE_ICON,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.ISSUE_TYPE_ASSIGNMENT));
		stringBuilder.append(ProjectTypeJSON.getChildJSON(issueTypeAssignmnet));
		// status assignment
		String statusLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.assignStatus", locale);
		String statusNodeID = ProjectTypeItemTypeAssignmentBaseFacade.getProjectTypeAssignmnentBranchNodeID(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.STATUS_ASSIGNMENT));
		ProjectTypeTreeNodeTO statusAssignmnet = new ProjectTypeTreeNodeTO(statusNodeID, statusLabel, ICONS_CLS.STATUS_ICON,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.STATUS_ASSIGNMENT));
		stringBuilder.append(ProjectTypeJSON.getChildJSON(statusAssignmnet));
		// priority assignment
		String priorityLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.assignPriority", locale);
		String priorityNodeID = ProjectTypeItemTypeAssignmentBaseFacade.getProjectTypeAssignmnentBranchNodeID(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.PRIORITY_ASSIGNMENT));
		ProjectTypeTreeNodeTO priorityAssignmnet = new ProjectTypeTreeNodeTO(priorityNodeID, priorityLabel, ICONS_CLS.PRIORITY_ICON,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.PRIORITY_ASSIGNMENT));
		stringBuilder.append(ProjectTypeJSON.getChildJSON(priorityAssignmnet));
		// severity assignment
		String severityLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.assignSeverity", locale);
		String severityNodeID = ProjectTypeItemTypeAssignmentBaseFacade.getProjectTypeAssignmnentBranchNodeID(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.SEVERITY_ASSIGNMENT));
		ProjectTypeTreeNodeTO severityAssignmnet = new ProjectTypeTreeNodeTO(severityNodeID, severityLabel, ICONS_CLS.SEVERITY_ICON,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.SEVERITY_ASSIGNMENT));
		stringBuilder.append(ProjectTypeJSON.getChildJSON(severityAssignmnet));
		// child project type assignment
		String childProjectTypeNodeID = ProjectTypeItemTypeAssignmentBaseFacade.getProjectTypeAssignmnentBranchNodeID(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.CHILD_PROJECT_TYPE_ASSIGNMENT));
		String childProjectTypeAssignmentLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.assignChildProjectType",
				locale);
		ProjectTypeTreeNodeTO childProjectTypeAssignmnet = new ProjectTypeTreeNodeTO(childProjectTypeNodeID, childProjectTypeAssignmentLabel,
				PROJECTTYPE_ICON_CLASS, Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.CHILD_PROJECT_TYPE_ASSIGNMENT));
		stringBuilder.append(ProjectTypeJSON.getChildJSON(childProjectTypeAssignmnet));
		// child project type assignment
		String roleNodeID = ProjectTypeItemTypeAssignmentBaseFacade.getProjectTypeAssignmnentBranchNodeID(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.ROLE_ASSIGNMENT));
		String roleAssignmentLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.assignRole", locale);
		ProjectTypeTreeNodeTO roleAssignment = new ProjectTypeTreeNodeTO(roleNodeID, roleAssignmentLabel, IconClass.ROLE,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.ROLE_ASSIGNMENT));
		stringBuilder.append(ProjectTypeJSON.getChildJSON(roleAssignment));
		// field config
		String fieldLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.customField", locale);
		String fieldConfigBranchNodeID = TreeConfigBL.getProjectTypeBranchNodeID(projectTypeID, TreeConfigBL.FIELD_CONFIG);
		ProjectTypeTreeNodeTO fieldConfigNode = new ProjectTypeTreeNodeTO(encodeNode(new ProjectTypeIDTokens(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.FIELD_CONFIGURATION))), fieldLabel, FIELD_CONFIG_ICON_CLASS,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.FIELD_CONFIGURATION), fieldConfigBranchNodeID);
		stringBuilder.append(ProjectTypeJSON.getChildJSON(fieldConfigNode));
		// screen assignment
		String formLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.customForms", locale);
		String screenConfigBranchNodeID = TreeConfigBL.getProjectTypeBranchNodeID(projectTypeID, TreeConfigBL.SCREEN_CONFIG);
		ProjectTypeTreeNodeTO screenConfigNode = new ProjectTypeTreeNodeTO(encodeNode(new ProjectTypeIDTokens(projectTypeID,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.SCREEN_ASSIGNMENT))), formLabel, SCREEN_ASSIGN_ICON_CLASS,
				Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.SCREEN_ASSIGNMENT), screenConfigBranchNodeID);
		boolean isBugs = ApplicationBean.getApplicationBean().isGenji();
		stringBuilder.append(ProjectTypeJSON.getChildJSON(screenConfigNode, isBugs));
		// workflow: the projectType node should be also visible (not only the
		// projectType children): so the projectType part of the nodeID is null
		// (that means all project types) but the projectTypeID will be sent as
		// request parameter to show only this project type as main node
		if (!isBugs) {
			String assignWorkflowLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("menu.admin.custom.workflow", locale);
			assignWorkflowLabel = LocalizeUtil.getParametrizedString("common.lbl.assign", new Object[] { assignWorkflowLabel }, locale);
			String workflowConfigBranchNodeID = TreeConfigBL.getProjectTypeBranchNodeID(null, TreeConfigBL.WORKFLOW_CONFIG);
			ProjectTypeTreeNodeTO workflowAssignment = new ProjectTypeTreeNodeTO(encodeNode(new ProjectTypeIDTokens(projectTypeID,
					Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.WORKFLOW_ASSIGNMENT))), assignWorkflowLabel, WORKFLOW_ICON_CLASS,
					Integer.valueOf(PROJECT_TYPE_ASSIGNMENTS.WORKFLOW_ASSIGNMENT), workflowConfigBranchNodeID);
			workflowAssignment.setProjectTypeID(projectTypeID);
			stringBuilder.append(ProjectTypeJSON.getChildJSON(workflowAssignment, true));
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	/**
	 * Get the children node lists of a node having the given id
	 * @param node
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static String getChildrenJSON(String node, TPersonBean personBean, Locale locale) {
		ProjectTypeIDTokens projectTypeIDTokens = ProjectTypesBL.decodeNode(node);
		Integer projectTypeID = projectTypeIDTokens.getProjectTypeID();
		if (projectTypeID == null) {
			// root node
			return ProjectTypeJSON.getChildrenJSON(getFirstLevelNodes(locale));
		} else {
			// a project type node
			return getAssignmentNodes(projectTypeID, locale);
		}
	}
	
	/**
	 * Edit a project type
	 * 
	 * @param node
	 * @param add
	 * @param locale
	 */
	public static String edit(String node, boolean add, Locale locale) {
		ProjectTypeTO projectTypeTO = new ProjectTypeTO();
		List<IntegerStringBean> projectTypeFlagList = getProjectTypeFlagList(locale);
		projectTypeTO.setProjectTypeFlagList(projectTypeFlagList);
		List<IntegerStringBean> workUnitsList = AccountingBL.getTimeUnitOptionsList(locale);
		projectTypeTO.setWorkUnitsList(workUnitsList);
		TProjectTypeBean projectTypeBean = null;
		if (add) {
			projectTypeTO.setProjectTypeFlag(projectTypeFlagList.get(0).getValue());
			projectTypeTO.setHoursPerWorkday(AccountingBL.DEFAULTHOURSPERWORKINGDAY);
			projectTypeTO.setDefaultWorkUnit(workUnitsList.get(0).getValue());
			projectTypeTO.setCurrencyName("EUR");
			projectTypeTO.setCurrencySymbol("\u20ac");
		} else {
			ProjectTypeIDTokens projectTypeIDTokens = decodeNode(node);
			Integer projectTypeID = projectTypeIDTokens.getProjectTypeID();
			if (projectTypeID != null) {
				projectTypeBean = loadByPrimaryKey(projectTypeID);
				if (projectTypeBean != null) {
					projectTypeTO.setLabel(projectTypeBean.getLabel());
					projectTypeTO.setProjectTypeFlag(projectTypeBean.getProjectTypeFlag());
					projectTypeTO.setHoursPerWorkday(projectTypeBean.getHoursPerWorkDay());
					projectTypeTO.setDefaultWorkUnit(projectTypeBean.getDefaultWorkUnit());
					projectTypeTO.setCurrencyName(projectTypeBean.getCurrencyName());
					projectTypeTO.setCurrencySymbol(projectTypeBean.getCurrencySymbol());
					projectTypeTO.setUseReleases(projectTypeBean.getUseReleases());
					projectTypeTO.setUseAccounts(projectTypeBean.getUseAccounting());
					projectTypeTO.setUseVersionControl(projectTypeBean.getUseVersionControl());
					projectTypeTO.setUseMsProjectExportImport(projectTypeBean.getUseMsProjectExportImport());
					projectTypeTO.setForPrivateProjects(BooleanFields.fromStringToBoolean(projectTypeBean.getDefaultForPrivate()));
				}
			}
		}
		return ProjectTypeJSON.getProjectTypeEditJSON(projectTypeTO);
	}

	private static List<IntegerStringBean> getProjectTypeFlagList(Locale locale) {
		List<IntegerStringBean> projectTypeFlagList = new LinkedList<IntegerStringBean>();
		projectTypeFlagList.add(new IntegerStringBean(LocalizeUtil
				.getLocalizedTextFromApplicationResources("admin.customize.projectType.flag.standard", locale), PROJECT_TYPE_FLAG.GENERAL));
		projectTypeFlagList.add(new IntegerStringBean(LocalizeUtil
				.getLocalizedTextFromApplicationResources("admin.customize.projectType.flag.helpdesk", locale), PROJECT_TYPE_FLAG.HELPDESK));
		projectTypeFlagList.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.flag.scrum", locale),
				PROJECT_TYPE_FLAG.SCRUM));
		projectTypeFlagList.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.flag.kanban", locale),
				PROJECT_TYPE_FLAG.KANBAN));
		return projectTypeFlagList;
	}

	/**
	 * Save a project type
	 * 
	 * @param node
	 * @param add
	 * @param projectTypeTO
	 */
	static String save(String node, boolean add, ProjectTypeTO projectTypeTO, Locale locale) {
		TProjectTypeBean projectTypeBean = null;
		boolean reloadTree = false;
		if (add) {
			reloadTree = true;
		} else {
			ProjectTypeIDTokens projectTypeIDTokens = decodeNode(node);
			Integer projectTypeID = projectTypeIDTokens.getProjectTypeID();
			if (projectTypeID != null) {
				projectTypeBean = loadByPrimaryKey(projectTypeID);
			}
		}
		if (projectTypeBean == null) {
			projectTypeBean = new TProjectTypeBean();
		}
		if (EqualUtils.notEqual(projectTypeTO.getLabel(), projectTypeBean.getLabel())) {
			reloadTree = true;
		}
		projectTypeBean.setLabel(projectTypeTO.getLabel());
		projectTypeBean.setHoursPerWorkDay(projectTypeTO.getHoursPerWorkday());
		projectTypeBean.setDefaultWorkUnit(projectTypeTO.getDefaultWorkUnit());
		projectTypeBean.setCurrencyName(projectTypeTO.getCurrencyName());
		projectTypeBean.setCurrencySymbol(projectTypeTO.getCurrencySymbol());
		projectTypeBean.setUseReleases(projectTypeTO.getUseReleases());
		projectTypeBean.setUseAccounting(projectTypeTO.getUseAccounts());
		projectTypeBean.setUseVersionControl(projectTypeTO.getUseVersionControl());
		projectTypeBean.setUseMsProjectExportImport(projectTypeTO.getUseMsProjectExportImport());
		projectTypeBean.setProjectTypeFlag(projectTypeTO.getProjectTypeFlag());
		Integer projectTypeID = projectTypeDAO.save(projectTypeBean);
		ProjectTypeIDTokens projectTypeIDTokens = new ProjectTypeIDTokens(projectTypeID);
		return ProjectTypeJSON.encodeProjectTypeSave(encodeNode(projectTypeIDTokens), reloadTree);
	}

	/**
	 * Deletes a projectType if possible or forwards to replacement
	 * 
	 * @param node
	 * @param locale
	 * @return
	 */
	static String delete(String node, Locale locale) {
		ProjectTypeIDTokens projectTypeIDTokens = decodeNode(node);
		Integer projectTypeID = projectTypeIDTokens.getProjectTypeID();
		if (projectTypeID != null) {
			if (projectTypeID.intValue() == 0) {
				return JSONUtility.encodeJSONFailure(
						LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.projectType.err.mayNotDelete", locale),
						ProjectTypesBL.ERROR_CANT_DELETE_ADMIN);
			} else {
				if (projectTypeDAO.hasDependentData(projectTypeID)) {
					return JSONUtility.encodeJSONFailure(JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
				} else {
					projectTypeDAO.delete(projectTypeID);
					return JSONUtility.encodeJSONSuccess();
				}
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}

	/**
	 * Whether the replace is needed or the delete is done without replacement
	 * 
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String renderReplace(String node, String errorMessage, Locale locale) {
		ProjectTypeIDTokens projectTypeIDTokens = decodeNode(node);
		Integer projectTypeID = projectTypeIDTokens.getProjectTypeID();
		if (projectTypeID != null) {
			List<ILabelBean> replacementList = new LinkedList<ILabelBean>();
			List<TProjectTypeBean> projectTypeBeans = loadAll();
			for (TProjectTypeBean projectTypeBean : projectTypeBeans) {
				if (!projectTypeBean.getObjectID().equals(projectTypeID)) {
					replacementList.add(projectTypeBean);
				}
			}
			TProjectTypeBean projectTypeBean = loadByPrimaryKey(projectTypeID);
			String label = null;
			if (projectTypeBean != null) {
				label = projectTypeBean.getLabel();
			}
			String localizedLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale);
			return JSONUtility.createReplacementListJSON(true, label, localizedLabel, localizedLabel, replacementList, errorMessage, locale);
		}
		return JSONUtility.encodeJSONSuccess();
	}

	/**
	 * Replaces and deletes a projectType
	 * 
	 * @param node
	 * @param newOID
	 */
	static void replaceAndDelete(String node, Integer newOID) {
		ProjectTypeIDTokens projectTypeIDTokens = decodeNode(node);
		Integer projectTypeID = projectTypeIDTokens.getProjectTypeID();
		if (projectTypeID != null) {
			projectTypeDAO.replaceAndDelete(projectTypeID, newOID);
		}
	}

	/****************** General methods (not linked with the ProjectTypeAction) ****************/

	/**
	 * Get the projectTypeID arrays for projectID arrays
	 * 
	 * @param selectedProjects
	 * @return
	 */
	public static Integer[] getProjectTypeIDsForProjectIDs(Integer[] selectedProjects) {
		if (selectedProjects == null || selectedProjects.length == 0) {
			return new Integer[0];
		}
		List<TProjectBean> projectBeans = ProjectBL.loadByProjectIDs(GeneralUtils.createListFromIntArr(selectedProjects));
		return getProjectTypeIDsForProjects(projectBeans);
	}

	public static Integer[] getProjectTypeIDsForProjects(List<TProjectBean> projectBeans) {
		Set<Integer> projectTypesSet = new HashSet<Integer>();
		if (projectBeans != null) {
			for (TProjectBean projectBean : projectBeans) {
				projectTypesSet.add(projectBean.getProjectType());
			}
			return GeneralUtils.createIntegerArrFromSet(projectTypesSet);
		}
		return new Integer[0];
	}

	/**
	 * Loads project type beans by IDs
	 * 
	 * @param projectTypeIDs
	 * @return
	 */
	public static Map<Integer, TProjectTypeBean> loadProjectTypeMapByIDs(List<Integer> projectTypeIDs) {
		return GeneralUtils.createMapFromList(projectTypeDAO.loadByProjectTypeIDs(projectTypeIDs));
	}

	public static List<TProjectTypeBean> loadProjectTypeByIDs(List<Integer> projectTypeIDs) {
		return projectTypeDAO.loadByProjectTypeIDs(projectTypeIDs);
	}
	
	/**
	 * Removes those project types from the set which do not allow an item type
	 * @param involvedProjectTypes
	 * @param itemTypeID
	 * @return
	 */
	public static Set<Integer> removeNotAllowingItemType(Set<Integer> involvedProjectTypes, Integer itemTypeID) {
		Set<Integer> expicitProjectTypeAssignments = getAssignedProjectTypeIDs(itemTypeID);
		return removeNotAllowingItemTypeAssignments(involvedProjectTypes, expicitProjectTypeAssignments);
	}
	
	/**
	 * Removes those project types from the set which do not allow item type flags from list
	 * @param involvedProjectTypes
	 * @param itemTypeFlags
	 * @return
	 */
	public static Set<Integer> removeNotAllowingItemTypeFlags(Set<Integer> involvedProjectTypes, int[] itemTypeFlags) {
		Set<Integer> expicitProjectTypeAssignments = getAssignedProjectTypeIDsByItemTypeFlags(itemTypeFlags);
		return removeNotAllowingItemTypeAssignments(involvedProjectTypes, expicitProjectTypeAssignments);
	}
	
	/**
	 * Gets the projectType IDs assigned to the itemType
	 * @param itemTypeID
	 * @return
	 */
	private static Set<Integer> getAssignedProjectTypeIDs(Integer itemTypeID) {
		return getProjectTypeSet(pIssueTypeDAO.loadByItemType(itemTypeID));
	}
	
	/**
	 * Gets the projectType IDs assigned to the itemType
	 * @param itemTypeID
	 * @return
	 */
	private static Set<Integer> getAssignedProjectTypeIDsByItemTypeFlags(int[] itemTypeFlags) {
		return getProjectTypeSet(pIssueTypeDAO.loadByItemTypeFlags(itemTypeFlags));
	}
	
	/**
	 * Gets the set of project types having item type assignment 
	 * @param projectTypeToIssueTypeBeans
	 * @return
	 */
	private static Set<Integer> getProjectTypeSet(List<TPlistTypeBean> projectTypeToIssueTypeBeans) {
		Set<Integer> assignedProjectTypes = new HashSet<Integer>();
		if (projectTypeToIssueTypeBeans!=null) {
			for (TPlistTypeBean plistTypeBean : projectTypeToIssueTypeBeans) {
				Integer projectType = plistTypeBean.getProjectType();
				assignedProjectTypes.add(projectType);
			}
		}
		return assignedProjectTypes;
	}
	
	/**
	 * Removes those project types from the set which do not allow an item type
	 * @param involvedProjectTypes
	 * @param itemTypeID
	 * @return
	 */
	private static Set<Integer> removeNotAllowingItemTypeAssignments(Set<Integer> involvedProjectTypes, Set<Integer> expicitProjectTypeAssignments) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("All project types involved: " + getCollectionString(involvedProjectTypes));
		}
		if (expicitProjectTypeAssignments!=null && !expicitProjectTypeAssignments.isEmpty()) {
			expicitProjectTypeAssignments.retainAll(involvedProjectTypes);
			involvedProjectTypes.removeAll(expicitProjectTypeAssignments);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Item type(s) explicitely allowed in the following projectTypes: " + getCollectionString(expicitProjectTypeAssignments));
			}
		}
		List<TPlistTypeBean> assignments = IssueTypeBL.loadByProjectTypes(involvedProjectTypes.toArray());
		for (TPlistTypeBean plistTypeBean : assignments) {
			Integer projectTypeID = plistTypeBean.getProjectType();
			if (involvedProjectTypes.contains(projectTypeID)) {
				involvedProjectTypes.remove(projectTypeID);
				LOGGER.debug("Item type(s) not allowed in projectType " + projectTypeID);
			}
		}
		involvedProjectTypes.addAll(expicitProjectTypeAssignments);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("All allowed project types: " + getCollectionString(involvedProjectTypes));
		}
		return involvedProjectTypes;
	}
	
	private static String getCollectionString(Set<Integer> set) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext();) {
			Integer ID = iterator.next();
			stringBuilder.append(ID);
			if (iterator.hasNext()) {
				stringBuilder.append(", ");
			}
		}
		return stringBuilder.toString();
	}

	public static String createDOM(Integer projectTypeID, boolean includeGlobal) throws EntityExporterException {
		ProjectTypeExporter projectTypeExporter = new ProjectTypeExporter();
		List<EntityContext> entityContextList = projectTypeExporter.export(projectTypeID, includeGlobal);
		String dom = EntityExporter.export2(entityContextList);
		return dom;
	}

}
