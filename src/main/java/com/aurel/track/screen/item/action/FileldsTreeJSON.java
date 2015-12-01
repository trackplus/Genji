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

package com.aurel.track.screen.item.action;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.item.TreeNodeField;

/**
 * JSON utility for fields on screeen
 *
 */
public class FileldsTreeJSON {

	/**
	 * Encodes the system and custom fields
	 * @param systemFields
	 * @param customFields
	 * @param locale
	 * @return
	 */
	static String encodeFieldList(List<TreeNodeField> systemFields, List<TreeNodeField> customFields, Locale locale){
		return encodeFieldList(systemFields,customFields,null,locale);
	}

	static String encodeFieldList(List<TreeNodeField> systemFields, List<TreeNodeField> customFields,List<TreeNodeField> extraFields, Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "text", ".");
		sb.append("children:[");

		//system fields
		sb.append(encodeFieldList(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.lbl.systemField", locale), TreeConfigBL.ICON_CLS.SYSTEM_FIELD,systemFields));

		sb.append(",");

		//custom fields
		sb.append(encodeFieldList(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.lbl.customField", locale),TreeConfigBL.ICON_CLS.CUSTOM_FIELD,customFields));

		//custom fields
		if(extraFields!=null&&!extraFields.isEmpty()){
			sb.append(",");
			sb.append(encodeFieldList(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.lbl.extraField", locale),TreeConfigBL.ICON_CLS.CUSTOM_FIELD,extraFields));
		}

		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
	private static String encodeFieldList(String label,String iconCls,List<TreeNodeField> fields){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"label", label);
		JSONUtility.appendStringValue(sb,"iconCls",iconCls);
		JSONUtility.appendBooleanValue(sb,"expanded",true);
		JSONUtility.appendJSONValue(sb,"children",encodeTreeNodeFields(fields),true);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Encodes a list of fields
	 * @param list
	 * @return
	 */
	private static String encodeTreeNodeFields(List<TreeNodeField> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<TreeNodeField> iterator = list.iterator(); iterator.hasNext();) {
				TreeNodeField treeNodeField = iterator.next();
				sb.append(encodeTreeNodeField(treeNodeField));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Encodes a tree field
	 * @param treeNodeField
	 * @return
	 */
	private static String encodeTreeNodeField(TreeNodeField treeNodeField){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(treeNodeField!=null){
			JSONUtility.appendStringValue(sb,"name",treeNodeField.getName());
			JSONUtility.appendStringValue(sb,"label",treeNodeField.getLabel());
			JSONUtility.appendStringValue(sb,"description",treeNodeField.getDescription());
			JSONUtility.appendStringValue(sb,"iconCls",treeNodeField.getImg());
			JSONUtility.appendBooleanValue(sb, "leaf", true);
			JSONUtility.appendIntegerValue(sb, "id", treeNodeField.getObjectID(),true);
		}
		sb.append("}");
		return sb.toString();
	}	
}
