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

package com.aurel.track.screen;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.json.JSONUtility;

public abstract class AbstractScreenJSON {
	public String encodeScreen(IScreen screen){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendScreenProperties(screen, sb);
		List<ITab> tabs=screen.getTabs();
		sb.append("\"tabs\":[");
		if(tabs!=null){
			ITab tab;
			for (int i = 0; i < tabs.size(); i++) {
				tab=tabs.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append(encodeTab(tab));
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
	protected void appendScreenProperties(IScreen screen, StringBuilder sb) {
		JSONUtility.appendIntegerValue(sb, "id", screen.getObjectID());
		JSONUtility.appendStringValue(sb, "label", screen.getLabel());
		JSONUtility.appendStringValue(sb, "name", screen.getName());
		JSONUtility.appendStringValue(sb, "description", screen.getDescription());
		JSONUtility.appendIntegerValue(sb, "personID", screen.getPersonID());
	}
	
	public String encodeTab(ITab tab){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendTabProperties(tab, sb);
		List<IPanel> panels=tab.getPanels();			
		sb.append("\"panels\":[");
		if(panels!=null){
			IPanel panel;
			for (int i = 0; i < panels.size(); i++) {
				panel=panels.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append(encodePanel(panel));
			}
		}
		sb.append("]");
		if (tab.getFieldTypes()!=null) {
			sb.append(",");
			List<String>fieldTypeList = new ArrayList<String>();
			fieldTypeList.addAll(tab.getFieldTypes());
			JSONUtility.appendStringList(sb, "fieldTypes", fieldTypeList, true);
		}
		sb.append("}");
		return sb.toString();
	}
	protected void appendTabProperties(ITab tab, StringBuilder sb) {
		JSONUtility.appendIntegerValue(sb, "id", tab.getObjectID());
		JSONUtility.appendStringValue(sb, "label", tab.getLabel());
		JSONUtility.appendStringValue(sb, "name", tab.getName());
		JSONUtility.appendStringValue(sb, "description", tab.getDescription());
	}
	
	public String encodePanel(IPanel panel){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		appendPanelProperties(panel, sb);
		FieldWrapper[][] fieldWrappers=panel.getFieldWrappers();
		sb.append("\"fields\":[");
		for (int k = 0; k < fieldWrappers.length; k++) {
			for (int l = 0; l < fieldWrappers[k].length; l++) {
				FieldWrapper fieldWrapper=fieldWrappers[k][l];
				if(fieldWrapper==null){
					continue;
				}
				if(l>0||k>0){
					sb.append(",");
				}
				sb.append(encodeField(fieldWrapper));
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
	protected void appendPanelProperties(IPanel panel, StringBuilder sb) {
		JSONUtility.appendIntegerValue(sb, "id", panel.getObjectID());
		JSONUtility.appendStringValue(sb, "label", panel.getLabel());
		JSONUtility.appendStringValue(sb, "name", panel.getName());
		JSONUtility.appendStringValue(sb, "description", panel.getDescription());
		JSONUtility.appendIntegerValue(sb, "colsNo", panel.getColsNo());
		JSONUtility.appendIntegerValue(sb, "rowsNo", panel.getRowsNo());
	}
	
	protected String encodeField(FieldWrapper fieldWrapper){
		StringBuilder sb=new StringBuilder();
		if(fieldWrapper.getField()==null){
			sb.append("{\"empty\":true,");
			appendEmptyFieldProperties(fieldWrapper,sb);
			sb.append("}");
		}else{
			sb.append("{\"empty\":false,");
			appendFieldProperties(fieldWrapper,sb);
			sb.append("}");
		}
		return sb.toString();
	}
	protected void appendEmptyFieldProperties(FieldWrapper fieldWrapper,StringBuilder sb){
		JSONUtility.appendIntegerValue(sb, "col", fieldWrapper.getCol());
		JSONUtility.appendJSONValue(sb, "jsonData", fieldWrapper.getJsonData());
		JSONUtility.appendStringValue(sb, "extClassName", fieldWrapper.getJsClass());
		JSONUtility.appendIntegerValue(sb, "row", fieldWrapper.getRow(),true);
	}
	protected void appendFieldProperties(FieldWrapper fieldWrapper,StringBuilder sb){
		IField field=fieldWrapper.getField();
		JSONUtility.appendStringValue(sb, "name", field.getName());
		JSONUtility.appendIntegerValue(sb, "colSpan", field.getColSpan());
		JSONUtility.appendIntegerValue(sb, "rowSpan", field.getRowSpan());
		JSONUtility.appendIntegerValue(sb,"iconRendering",field.getIconRendering());
		JSONUtility.appendIntegerValue(sb, "row", fieldWrapper.getRow());
		JSONUtility.appendIntegerValue(sb, "col", fieldWrapper.getCol());
		JSONUtility.appendJSONValue(sb, "jsonData", fieldWrapper.getJsonData());
		JSONUtility.appendStringValue(sb, "extClassName", fieldWrapper.getJsClass());
		JSONUtility.appendIntegerValue(sb, "id", field.getObjectID(),true);
		
	}
	
	//properties
	public String encodeScreenProperties(IScreen screen){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "screen.name", screen.getName());
		JSONUtility.appendStringValue(sb, "screen.description", screen.getDescription());
		JSONUtility.appendStringValue(sb, "screen.label", screen.getLabel(),true);
		sb.append("}");
		return sb.toString();
	}
	public String encodeTabProperties(ITab tab){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "tab.label", tab.getLabel());
		JSONUtility.appendStringValue(sb, "tab.description", tab.getDescription());
		JSONUtility.appendStringValue(sb, "tab.name", tab.getName(),true);
		sb.append("}");
		return sb.toString();
	}
	public String encodePanelProperties(IPanel panel){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "panel.name", panel.getName());
		JSONUtility.appendStringValue(sb, "panel.description", panel.getDescription());
		JSONUtility.appendIntegerValue(sb, "panel.rowsNo", panel.getRowsNo());
		JSONUtility.appendIntegerValue(sb, "panel.colsNo", panel.getColsNo(),true);
		sb.append("}");
		return sb.toString();
	}
	public String encodeFieldProperies(IField field,String fieldType){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "fieldType", fieldType);

		JSONUtility.appendStringValue(sb, "field.description", field.getDescription());
		JSONUtility.appendIntegerValue(sb, "field.rowSpan", field.getRowSpan());
		JSONUtility.appendIntegerValue(sb, "field.colSpan", field.getColSpan());
		sb.append(encodeFieldPropertiesExtra(field,fieldType));
		JSONUtility.appendStringValue(sb, "field.name", field.getName(),true);
		sb.append("}");
		return sb.toString();
	}
	protected String encodeFieldPropertiesExtra(IField field,String fieldType){
		return "";
	}
}
