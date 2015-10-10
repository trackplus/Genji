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

package com.aurel.track.admin.customize.role;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

public class FieldRestrictionsToRoleJSON {
	
	static interface JSON_FIELDS {
		static final String FIELD_LABEL = "fieldLabel";
		static final String HIDDEN = "hidden";
		static final String CONFIGURED_READ_ONLY = "configuredReadOnly";
		static final String FORCED_READ_ONLY = "forcedReadOnly";
	}
	
	/**
	 * Creates the JSON string for role fields 
	 * @param roleFields
	 * @return
	 */
	static String createEditFieldRestrictionsJSON(List<FieldForRoleBean> roleFields){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		boolean allHidden = true;
		boolean allConfiguredReadOnly = true;
		sb.append(JSONUtility.JSON_FIELDS.RECORDS).append(":[");
		for (Iterator<FieldForRoleBean> iterator = roleFields.iterator(); iterator.hasNext();) {
			FieldForRoleBean roleFieldTO = iterator.next();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.OBJECT_ID, roleFieldTO.getObjectID());
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, roleFieldTO.getFieldID());
			JSONUtility.appendStringValue(sb, JSON_FIELDS.FIELD_LABEL, roleFieldTO.getFieldLabel());
			boolean isHidden = roleFieldTO.isHidden();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HIDDEN, isHidden);
			allHidden = allHidden && isHidden;
			boolean isForcedReadOnly = roleFieldTO.isForcedReadOnly();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.FORCED_READ_ONLY, isForcedReadOnly);
			boolean isConfiguredReadOnly = roleFieldTO.isConfiguredReadOnly();
			JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CONFIGURED_READ_ONLY, isConfiguredReadOnly,true);
			allConfiguredReadOnly = allConfiguredReadOnly && isConfiguredReadOnly;
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("],");
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.HIDDEN, allHidden);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.CONFIGURED_READ_ONLY, allConfiguredReadOnly, true);
		sb.append("}");//end data
		return sb.toString();
	}
}
