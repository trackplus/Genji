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


package com.aurel.track.fieldType.runtime.renderer;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.macro.*;

import java.util.ArrayList;
import java.util.List;


public class RichTextEditorRendererRT  extends AbstractTypeRendererRT{
	//singleton instance
	private static RichTextEditorRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static RichTextEditorRendererRT getInstance() {
		if (instance == null) {
			instance = new RichTextEditorRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public RichTextEditorRendererRT() {
	}
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.HtmlTypeRenderer";
	}
	@Override
	public String getExtReadOnlyClassName(){
		return "com.aurel.trackplus.field.HtmlReadOnlyTypeRenderer";
	}

	@Override
	public String encodeJsonValue(Object value){
		if(value!=null){
			List<MacroDef> macroDefList= MacroBL.parseMacros(value.toString(), MacroManager.MACRO_ISSUE);
			if(macroDefList!=null){
				IMacro macro=new MacroIssueDecorate();
				MacroContext macroContext=new MacroContext();
				value=MacroBL.replaceMacros(macroDefList,value.toString(),macroContext,macro);
			}
		}

		return value==null?null:"\""+ JSONUtility.escape(value.toString())+"\"";
	}
	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		if(value!=null){
			List<MacroDef> macroDefList= MacroBL.parseMacros(value, MacroManager.MACRO_ISSUE);
			if(macroDefList!=null){
				IMacro macro=new MacroIssueSimplify();
				value=MacroBL.replaceMacros(macroDefList,value,null,macro);
				List<Integer> inlineItems=new ArrayList<Integer>();
				for(MacroDef macroDef:macroDefList){
					Integer workItemID=MacroIssue.getItemID(macroDef);
					if(workItemID!=null){
						inlineItems.add(workItemID);
					}
				}
				workItemContext.setInlineItems(inlineItems);
			}
		}
		return value;
	}

	@Override
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"maxLength", new Integer(32000),true);
		/*String value = (String)workItemContext.getWorkItemBean().getAttribute(field.getObjectID());
		JSONUtility.appendStringValue(sb,"displayValue",ItemDetailBL.formatDescription(value, workItemContext.getLocale()));*/
		sb.append("}");
		return sb.toString();
	}
}
