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

package com.aurel.track.admin.customize.projectType;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.json.JSONUtility;

/**
 * JSON utility for field and screen configurations
 * @author Tamas Ruff
 *
 */
public class ProjectTypeJSON {
	
	static interface JSON_FIELDS {
		String CONFIG_TYPE = "configType";
		String BRANCH_ROOT = "branchRoot";
		String PROJECT_TYPE_ID = "projectTypeID";
		
		String PROJECT_TYPE_TO = "projectTypeTO.";
		String HOURS_PER_WORKING_DAY = PROJECT_TYPE_TO + "hoursPerWorkday";
		String DEFAULT_WORK_UNIT = PROJECT_TYPE_TO + "defaultWorkUnit";
		String WORK_UNIT_LIST = "workUnitList";
		String CURRENCY_NAME = PROJECT_TYPE_TO + "currencyName";
		String CURRENCY_SYMBOL = PROJECT_TYPE_TO + "currencySymbol";
		
		String USE_RELEASES = PROJECT_TYPE_TO + "useReleases";
		String USE_ACCOUNTS = PROJECT_TYPE_TO + "useAccounts";
		String USE_VERSION_CONTROL = PROJECT_TYPE_TO + "useVersionControl";
		String USE_MSPROJECT = PROJECT_TYPE_TO + "useMsProjectExportImport";
		String PROJECT_TYPE_FLAG = PROJECT_TYPE_TO + "projectTypeFlag";
		String PROJECT_TYPE_FLAG_LIST = "projectTypeFlagList";
		String FOR_PRIVATE_PROJECTS = "forPrivateProjects";
		
		String RELOAD_TREE = "reloadTree";
	}	
	
	/**
	 * Creates the JSON string with localized list names
	 * @param localizedLabels
	 * @return
	 */
	static String createLocalizedLabelsJSON(Map<Integer, String> localizedLabels) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);	
		JSONUtility.appendIntegerStringMap(sb, JSONUtility.JSON_FIELDS.RECORDS, localizedLabels, true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the JSON string for configuration children 
	 * @param children
	 * @return
	 */
	static String getChildrenJSON(List<ProjectTypeTreeNodeTO> children){
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("[");
		for (Iterator<ProjectTypeTreeNodeTO> iterator = children.iterator(); iterator.hasNext();) {
			ProjectTypeTreeNodeTO projectTypeTreeNodeTO = iterator.next();
			stringBuilder.append(getChildJSON(projectTypeTreeNodeTO, !iterator.hasNext()));
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON string for a projectTypeTreeNodeTO
	 * @param projectTypeTreeNodeTO
	 * @return
	 */
	static String getChildJSON(ProjectTypeTreeNodeTO projectTypeTreeNodeTO) {
		return getChildJSON(projectTypeTreeNodeTO, false);
	 }
	
	/**
	 * Gets the JSON string for a projectTypeTreeNodeTO
	 * @param projectTypeTreeNodeTO
	 * @return
	 */
	static String getChildJSON(ProjectTypeTreeNodeTO projectTypeTreeNodeTO, boolean last) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ID, projectTypeTreeNodeTO.getId());
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.TEXT, projectTypeTreeNodeTO.getText());
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICON, projectTypeTreeNodeTO.getIcon());
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ICONCLS, projectTypeTreeNodeTO.getIconCls());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.CONFIG_TYPE, projectTypeTreeNodeTO.getConfigType());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.BRANCH_ROOT, projectTypeTreeNodeTO.getBranchRoot());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PROJECT_TYPE_ID, projectTypeTreeNodeTO.getProjectTypeID());
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.LEAF, projectTypeTreeNodeTO.isLeaf(), true);
		stringBuilder.append("}");
		if (!last) {
			stringBuilder.append(",");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the JSON for editing a projectType
	 * @param projectTypeTO
	 * @return
	 */
	public static String getProjectTypeEditJSON(ProjectTypeTO projectTypeTO) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(stringBuilder, JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.PROJECT_TYPE_TO + JSONUtility.JSON_FIELDS.LABEL, projectTypeTO.getLabel());
		JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.PROJECT_TYPE_FLAG_LIST, projectTypeTO.getProjectTypeFlagList());
		JSONUtility.appendDoubleValue(stringBuilder, JSON_FIELDS.HOURS_PER_WORKING_DAY, projectTypeTO.getHoursPerWorkday());
		JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.WORK_UNIT_LIST, projectTypeTO.getWorkUnitsList());
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.DEFAULT_WORK_UNIT, projectTypeTO.getDefaultWorkUnit());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.CURRENCY_NAME, projectTypeTO.getCurrencyName());
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.CURRENCY_SYMBOL, projectTypeTO.getCurrencySymbol());
		if (projectTypeTO.getUseReleases()!=null) {
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.USE_RELEASES, projectTypeTO.getUseReleases());
		}
		if (projectTypeTO.getUseAccounts()!=null) {
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.USE_ACCOUNTS, projectTypeTO.getUseAccounts());
		}
		if (projectTypeTO.getUseVersionControl()!=null) {
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.USE_VERSION_CONTROL,projectTypeTO.getUseVersionControl());
		}
		if (projectTypeTO.getUseMsProjectExportImport()!=null) {
			JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.USE_MSPROJECT,projectTypeTO.getUseMsProjectExportImport());
		}
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.FOR_PRIVATE_PROJECTS, projectTypeTO.getForPrivateProjects());
		
		JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PROJECT_TYPE_FLAG, projectTypeTO.getProjectTypeFlag(), true);
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Gets a JSON success with node
	 * @param node 
	 * @return
	 */
	public static String encodeProjectTypeSave(String node, boolean reloadTree) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendBooleanValue(stringBuilder, JSON_FIELDS.RELOAD_TREE, reloadTree);
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NODE, node, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}	
	
}
