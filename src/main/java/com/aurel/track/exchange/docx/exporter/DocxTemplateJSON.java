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

package com.aurel.track.exchange.docx.exporter;

import java.util.List;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.LabelValueBean;

public class DocxTemplateJSON {
	
	/**
	 * Creates the file upload JSON string for a template 
	 * @param success
	 * @param existingTemplates
	 * @return
	 */
	public static String createFileSelectJSON(boolean success, 
			List<LabelValueBean> existingTemplates, String selectedTemplate){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, success);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, "docxTemplateList", selectedTemplate);
		JSONUtility.appendLabelValueBeanList(sb, "existingTemplates", existingTemplates);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
