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


package com.aurel.track.admin.customize.notify.trigger;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.beans.TNotifyTriggerBean;
import com.aurel.track.json.JSONUtility;


/**
 * Business logic class for notification settings
 * @author Tamas Ruff
 *
 */
public class NotifyTriggerJSON {
		
	
	static interface JSON_FIELDS {
		static final String TYPE_LABEL = "typeLabel";
		static final String OWN = "own";
		static final String ACTION_TYPE_LABEL = "actionTypeLabel";
		static final String FIELD_TYPE_LABEL = "fieldTypeLabel";
		static final String FIELD_LABEL = "fieldLabel";
		static final String ORIGINATOR = "originator";
		static final String MANAGER = "manager";
		static final String RESPONSIBLE = "responsible";
		static final String CONSULTED = "consulted";
		static final String INFORMED = "informed";
		static final String OBSERVER = "observer";
	}
	
	/**
	 * Creates the JSON string for trigger fields 
	 * @param notifyTriggerBeans
	 * @return
	 */
	static String createTriggerListJSON(List<TNotifyTriggerBean> notifyTriggerBeans){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<TNotifyTriggerBean> iterator = notifyTriggerBeans.iterator(); iterator.hasNext();) {
			TNotifyTriggerBean notifyTriggerBean = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, notifyTriggerBean.getObjectID());
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, notifyTriggerBean.getLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.TYPE_LABEL, notifyTriggerBean.getTypeLabel());
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.OWN, notifyTriggerBean.isOwn(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string for trigger fields 
	 * @param triggerFields
	 * @param label
	 * @param disabled
	 * @return
	 */
	static String createEditTriggerJSON(List<NotifyTriggerFieldTO> triggerFields, String label, boolean disabled){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, label);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.DISABLED, disabled);
		boolean allAsOriginator = true;
		boolean allAsManager = true;
		boolean allAsResponsible = true;
		boolean allAsConsulted = true;
		boolean allAsInformed = true;
		boolean allAsObserver = true;
		sb.append(JSONUtility.JSON_FIELDS.RECORDS).append(":[");
		for (Iterator<NotifyTriggerFieldTO> iterator = triggerFields.iterator(); iterator.hasNext();) {
			NotifyTriggerFieldTO notifyTriggerFieldTO = iterator.next();
			sb.append("{");
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, notifyTriggerFieldTO.getId());
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.OBJECT_ID, notifyTriggerFieldTO.getObjectID());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.ACTION_TYPE_LABEL, notifyTriggerFieldTO.getActionTypeLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.FIELD_TYPE_LABEL, notifyTriggerFieldTO.getFieldTypeLabel());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.FIELD_LABEL, notifyTriggerFieldTO.getFieldLabel());
			boolean isOriginator = notifyTriggerFieldTO.isOriginator();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.ORIGINATOR, isOriginator);
			allAsOriginator = allAsOriginator && isOriginator;
			boolean isManager = notifyTriggerFieldTO.isManager();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MANAGER, isManager);
			allAsManager = allAsManager && isManager;
			boolean isResponsible = notifyTriggerFieldTO.isResponsible();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RESPONSIBLE, isResponsible);
			allAsResponsible = allAsResponsible && isResponsible;
			boolean isConsulted = notifyTriggerFieldTO.isConsulted();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CONSULTED, isConsulted);
			allAsConsulted = allAsConsulted && isConsulted;
			boolean isInformed = notifyTriggerFieldTO.isInformed();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INFORMED, isInformed);
			allAsInformed = allAsInformed && isInformed;
			boolean isObserver = notifyTriggerFieldTO.isObserver();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.OBSERVER, isObserver, true);
			allAsObserver = allAsObserver && isObserver;
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("],");
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.ORIGINATOR, allAsOriginator);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.MANAGER, allAsManager);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.RESPONSIBLE, allAsResponsible);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CONSULTED, allAsConsulted);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.INFORMED, allAsInformed);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.OBSERVER, allAsObserver, true);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Creates the JSON string for replacement triggers 
	 * @param triggerLabel
	 * @param replacementTriggerList
	 * @param errorMessage
	 * @return
	 */
	static String createReplacementTriggerJSON(String triggerLabel,
			List<TNotifyTriggerBean> replacementTriggerList, String errorMessage){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		if (errorMessage!=null) {
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ERROR_MESSAGE, errorMessage);
		}
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, triggerLabel);
		JSONUtility.appendILabelBeanList(sb, JSONUtility.JSON_FIELDS.REPLACEMENT_LIST, (List)replacementTriggerList);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
