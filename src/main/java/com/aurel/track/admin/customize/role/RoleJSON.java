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

package com.aurel.track.admin.customize.role;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.IntegerStringBooleanBean;

public class RoleJSON {
	
	
		
	static String encodeJSONRoles(List<TRoleBean> roles, Locale locale) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		Map<Integer, List<IntegerStringBean>> notVisibleFields = new HashMap<Integer, List<IntegerStringBean>>();
		Map<Integer, List<IntegerStringBean>> notModifiableFields = new HashMap<Integer, List<IntegerStringBean>>();
		FieldsRestrictionsToRoleBL.getRestrictedFields(locale, notVisibleFields, notModifiableFields);
		Map<Integer, List<Integer>> issueTypesForRoles = RoleBL.getIssueTypesForRoles();
		for (int i = 0; i < roles.size(); i++) {
			TRoleBean roleBean=roles.get(i);
			Integer roleID = roleBean.getObjectID();
			if(i>0){
				sb.append(",");
			}
			sb.append("{");
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, roleBean.getLabel());
			Boolean[] unfoldedFlags=RoleBL.getUnfoldedFlags(roleBean.getExtendedaccesskey());
			Map<Integer,IntegerStringBooleanBean> allFlags=RoleBL.createUnfoldedFlagsMap(unfoldedFlags,locale);
			List<IntegerStringBooleanBean> unfoldedFullAccessFlags=RoleBL.wrapCategoryFlags(RoleBL.fullAccessFlags,allFlags);
			List<IntegerStringBooleanBean> unfoldedRaciRoles=RoleBL.wrapCategoryFlags(RoleBL.raciRoles,allFlags);
			JSONUtility.appendFieldName(sb, "accessFlags").append(":{");
			JSONUtility.appendIntegerStringBooleanBeanList(sb,"fullAccessFlags",unfoldedFullAccessFlags);
			JSONUtility.appendIntegerStringBooleanBeanList(sb,"raciRoles",unfoldedRaciRoles,true);
			sb.append("},");
			JSONUtility.appendFieldName(sb, "issueTypesAssigned").append(":[");
			List<Integer> issueTypeIDs = issueTypesForRoles.get(roleID);
			if(issueTypeIDs!=null){
				for (Iterator<Integer> iterator = issueTypeIDs.iterator(); iterator.hasNext();) {
					Integer issueTypeID = iterator.next();
					TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(issueTypeID, locale);
					sb.append("{");
					JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, issueTypeID);
					JSONUtility.appendStringValue(sb, "symbol", listTypeBean.getSymbol());
					JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, listTypeBean.getLabel(), true);
					sb.append("}");
					if (iterator.hasNext()) {
						sb.append(",");
					}
				}
			}
			sb.append("],");
			List<IntegerStringBean> noReadFields=notVisibleFields.get(roleID);
			JSONUtility.appendIntegerStringBeanList(sb,"noReadFields",noReadFields);
			List<IntegerStringBean> noEditFields=notModifiableFields.get(roleID);
			JSONUtility.appendIntegerStringBeanList(sb,"noEditFields",noEditFields);
			JSONUtility.appendIntegerValue(sb, "id", roleID,true);
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}
	
	static String encodeRoleTO(RoleTO role) {
		StringBuilder sb=new StringBuilder();
		sb.append("{success : true, data:");
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"role.objectID",role.getObjectID());
		JSONUtility.appendStringValue(sb,"label",role.getLabel());
		JSONUtility.appendIntegerStringBooleanBeanList(sb,"fullAccessFlags",role.getFullAccessFlags());
		JSONUtility.appendIntegerStringBooleanBeanList(sb,"raciRoles",role.getRaciRoles(),true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
