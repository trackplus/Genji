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

package com.aurel.track.admin.customize.scripting;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;

public class ScriptJSON {
	
	static interface JSON_FIELDS {
		static final String SCRIPT_TYPE = "scriptType";
		static final String TYPE_LABEL = "typeLabel";
		static final String SCRIPT_TYPE_LIST = "scriptTypeList";
		
		static final String CLASS_NAME = "clazzName";
		static final String SOURCE_CODE = "sourceCode";
	}
	
	/**
	 * Encode all scripts for grid
	 * @param scriptBeans
	 * @param typeLabelMap
	 * @return
	 */
	public static String encodeJSONScripts(List<TScriptsBean> scriptBeans, Map<Integer, String> typeLabelMap) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for (Iterator<TScriptsBean> iterator = scriptBeans.iterator(); iterator.hasNext();) {
			TScriptsBean scriptsBean = iterator.next(); 
			sb.append("{");
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, scriptsBean.getClazzName());
			Integer scriptType = scriptsBean.getScriptType();
			JSONUtility.appendIntegerValue(sb, JSON_FIELDS.SCRIPT_TYPE, scriptType);
			JSONUtility.appendStringValue(sb, JSON_FIELDS.TYPE_LABEL, typeLabelMap.get(scriptType));
			JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, scriptsBean.getObjectID(), true);
			sb.append("}");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Encode a script for edit
	 * @param scriptBean
	 * @param scriptTypeList
	 * @return
	 */
	public static String encodeScript(TScriptsBean scriptBean, List<IntegerStringBean> scriptTypeList){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.CLASS_NAME, scriptBean.getClazzName());
		Integer scriptType = scriptBean.getScriptType();
		if (scriptType==null) {
			scriptType = TScriptsBean.SCRIPT_TYPE.WORKFLOW_ACTIVITY;
		}
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.SCRIPT_TYPE, scriptType);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.SOURCE_CODE, scriptBean.getSourceCode());
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.SCRIPT_TYPE_LIST, scriptTypeList, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Gets the json string for import failure
	 * @param success the result message
	 * @param message
     * @param locale
	 * @return
	 */
	public static String compileResultJSON(boolean success, String message, Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");		
		JSONUtility.appendStringValue(sb,  JSONUtility.JSON_FIELDS.TITLE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.script.title.compileMessage", locale));
		JSONUtility.appendStringValue(sb,  JSONUtility.JSON_FIELDS.MESSAGE, message);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success, true);
		sb.append("}");
		return sb.toString();
	}	
}
