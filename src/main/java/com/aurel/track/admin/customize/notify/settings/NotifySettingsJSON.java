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


package com.aurel.track.admin.customize.notify.settings;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TNotifyTriggerBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

/**
 * Business logic class for notification settings
 * @author Tamas Ruff
 *
 */
public class NotifySettingsJSON {
		
	
	static interface JSON_FIELDS {
		static final String INHERITED = "inherited";		
		static final String PROJECT = "project";
		static final String PROJECT_LABEL = "projectLabel";
        static final String PROJECT_TREE = "projectTree";
		static final String TRIGGER = "trigger";		
		static final String TRIGGER_LABEL = "triggerLabel";
        static final String TRIGGERS_LIST = "triggersList";
		static final String FILTER = "filter";
        static final String FILTER_LABEL = "filterLabel";
        static final String FILTER_TREE = "filterTree";
	}
	
	/**
	 *  Creates the JSON string for notification settings list 
	 * @param personBean
	 * @param defaultSettings
     * @param projectID
	 * @param locale
	 * @return
	 */
	static String createNotifySettingsListJSON(TPersonBean personBean, boolean defaultSettings, Integer projectID, Locale locale){
		List<NotifySettingsTO> notifySettingsTOList = NotifySettingsBL.createNotifySettingsTOs(personBean, defaultSettings, projectID, locale);
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<NotifySettingsTO> iterator = notifySettingsTOList.iterator(); iterator.hasNext();) {
			NotifySettingsTO notifySettingsTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.PROJECT, notifySettingsTO.getProjectID());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECT_LABEL, notifySettingsTO.getProjectLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.TRIGGER_LABEL, notifySettingsTO.getTriggerLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.FILTER_LABEL, notifySettingsTO.getFilterLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INHERITED, notifySettingsTO.isInherited());
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, notifySettingsTO.getObjectID(),true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for editing/adding/deriving a notification setting
	 * @param id
	 * @param actualProjectID
     * @param projectTree
	 * @param actualTriggerID
	 * @param triggers
	 * @param actualFilterID
	 * @param filterTree
	 * @return
	 */
	static String createNotifySettingsEditJSON(Integer id, Integer actualProjectID, List<TreeNode> projectTree,
			Integer actualTriggerID, List<TNotifyTriggerBean> triggers, 
			Integer actualFilterID, List<TreeNode> filterTree) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
        if (actualProjectID!=null) {
		    JSONUtility.appendStringValue(sb, JSON_FIELDS.PROJECT, actualProjectID.toString());
        }
        JSONUtility.appendJSONValue(sb, JSON_FIELDS.PROJECT_TREE, JSONUtility.getTreeHierarchyJSON(projectTree, false, true));
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.TRIGGER, actualTriggerID);
        if (actualFilterID!=null) {
		    JSONUtility.appendStringValue(sb, JSON_FIELDS.FILTER, actualFilterID.toString());
        }
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.FILTER_TREE, JSONUtility.getTreeHierarchyJSON(filterTree));
		JSONUtility.appendILabelBeanList(sb, JSON_FIELDS.TRIGGERS_LIST, (List)triggers, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for actualizing the notification filters' list after a project change
	 * @param categoryTree
	 * @param filterID
	 * @return
	 */
	static String createNotifySettingsFilterTreeJSON(List<TreeNode> categoryTree, Integer filterID) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
        if (filterID!=null) {
            JSONUtility.appendStringValue(sb, JSON_FIELDS.FILTER, filterID.toString());
        }
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.FILTER_TREE, JSONUtility.getTreeHierarchyJSON(categoryTree, false, true));
        JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);
		sb.append("}");
		return sb.toString();		
	}
}
