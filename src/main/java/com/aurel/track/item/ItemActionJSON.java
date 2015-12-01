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

package com.aurel.track.item;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.macro.*;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.renderer.TypeRendererRT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.emailHandling.Html2Text;

public class ItemActionJSON {

	private static final Logger LOGGER = LogManager.getLogger(ItemActionJSON.class);

	public static String encodeContext(WorkItemContext workItemContext,Locale locale,boolean  useProjectSpecificID,TPersonBean personBean, boolean isMobileApplication){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		Map<Integer,FieldConfigTO> fieldConfigTOMap=FieldConfigBL.extractFieldConfigTO(workItemContext,locale);
		TWorkItemBean workItem=workItemContext.getWorkItemBean();
		JSONUtility.appendIntegerValue(sb, "actionID", workItemContext.getActionID());
		JSONUtility.appendIntegerValue(sb, "workItemID", workItem.getObjectID());
		boolean allowedToChange = AccessBeans.isAllowedToChange(workItem, personBean.getObjectID());
		boolean personInlineEdit=personBean.isPrintItemEditable();
		boolean inlineEdit=allowedToChange&&personInlineEdit;
		JSONUtility.appendBooleanValue(sb, "inlineEdit", inlineEdit);
		JSONUtility.appendIntegerValue(sb, "projectID", workItem.getProjectID());
		JSONUtility.appendIntegerValue(sb, "issueTypeID", workItem.getListTypeID());
		JSONUtility.appendJSONValue(sb,"fieldConfigs",encodeFieldConfigTOMap(fieldConfigTOMap));
		Set<Integer> presentFields=FieldConfigBL.addBaseSystemFields(workItemContext.getPresentFieldIDs());
		JSONUtility.appendJSONValue(sb,"fieldValues", encodeFieldValues(presentFields,workItemContext,useProjectSpecificID, isMobileApplication));
		JSONUtility.appendJSONValue(sb,"fieldDisplayValues", encodeFieldDisplayValues(presentFields,workItemContext,useProjectSpecificID, isMobileApplication),true);
		sb.append("}");
		return sb.toString();
	}
	public static String encodeJSON_IssueLocation(ItemLocationForm form){
		if(form==null){
			return null;
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "projectID", form.getProjectID().toString());
		JSONUtility.appendIntegerValue(sb, "issueTypeID", form.getIssueTypeID());
		JSONUtility.appendIntegerValue(sb, "parentID", form.getParentID());
		JSONUtility.appendStringValue(sb, "synopsis", form.getSynopsis());
		JSONUtility.appendStringValue(sb, "description", form.getDescription());

		JSONUtility.appendStringValue(sb, "projectLabel", form.getProjectLabel());
		JSONUtility.appendStringValue(sb, "projectTooltip", form.getProjectTooltip());
		JSONUtility.appendStringValue(sb, "issueTypeLabel", form.getIssueTypeLabel());
		JSONUtility.appendStringValue(sb, "issueTypeTooltip", form.getIssueTypeTooltip());
		JSONUtility.appendBooleanValue(sb, "fixedIssueType", form.isFixedIssueType());

		sb.append("\"projectTree\":");
        sb.append(JSONUtility.getTreeHierarchyJSON(form.getProjectTree(), false, true));
		sb.append(",");

		sb.append("\"issueTypeList\":");
		sb.append(JSONUtility.encodeJSONIntegerStringBeanList(form.getIssueTypeList()));
		sb.append("}");
		return sb.toString();
	}

	public static String encodeJSON_Children(List<TWorkItemBean> children,Locale locale,boolean useProjectSpecificID){
		Map<Integer,TProjectBean> projectsMap=null;
		if(useProjectSpecificID && children!=null && !children.isEmpty()){
			Set<Integer> projectIds=new HashSet<Integer>();
			for (int i = 0; i < children.size(); i++) {
				TWorkItemBean item=children.get(i);
				projectIds.add(item.getProjectID());
			}
			List<TProjectBean> projects=ProjectBL.loadByProjectIDs(GeneralUtils.createIntegerListFromCollection(projectIds));
			projectsMap=new HashMap<Integer, TProjectBean>();
			for (int i = 0; i < projects.size(); i++) {
				TProjectBean p=projects.get(i);
				projectsMap.put(p.getObjectID(),p);
			}
		}

		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(children!=null && !children.isEmpty()){
			Map<Integer, TPersonBean> originatorsMap=ItemBL.getChildrenOriginatorsMap(children);
			Map<Integer, TPersonBean> responsiblesMap=ItemBL.getChildrenResponsiblesMap(children);
			for (int i = 0; i < children.size(); i++) {
				TWorkItemBean item=children.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				JSONUtility.appendIntegerValue(sb,"workItemID",item.getObjectID());
				String id=null;
				if(useProjectSpecificID){
					String projectPrefix=projectsMap.get(item.getProjectID()).getPrefix();
					if(projectPrefix!=null){
						id=projectPrefix+item.getIDNumber();
					}else{
						id=item.getIDNumber().toString();
					}
				}else{
					id=item.getObjectID().toString();
				}
				JSONUtility.appendStringValue(sb,"id",id);
				JSONUtility.appendStringValue(sb, "status", LookupContainer.getStatusBean(item.getStateID(), locale).getLabel());
				JSONUtility.appendStringValue(sb, "originator", originatorsMap.get(item.getOriginatorID()).getLabel());
				JSONUtility.appendStringValue(sb, "responsible", responsiblesMap.get(item.getResponsibleID()).getLabel());
				JSONUtility.appendStringValue(sb, "synopsis", item.getSynopsis(),true);
				sb.append("}");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static  String encodeFieldValues(Set<Integer> presentFields,WorkItemContext workItemContext,boolean useProjectSpecificID, boolean isMobileApplication){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		for (Iterator<Integer> iterator = presentFields.iterator(); iterator.hasNext();) {
			Integer fieldID=iterator.next();
			Object value=workItemContext.getWorkItemBean().getAttribute(fieldID);
			String jsonValue = null;
			if(useProjectSpecificID&&fieldID.intValue()==SystemFields.ISSUENO){
				jsonValue="\""+ItemBL.getSpecificProjectID(workItemContext)+"\"";
			}else{
				FieldType fieldType= FieldTypeManager.getInstance().getType(fieldID);
				fieldType.setFieldID(fieldID);
				//set
				if (fieldType!=null) {
					TypeRendererRT fieldTypeRendererRT= fieldType.getRendererRT();
					int valueType=fieldType.getFieldTypeRT().getValueType();
					switch (valueType) {
						case ValueType.DATE:
							jsonValue="\""+ DateTimeUtils.getInstance().formatGUIDate((Date)value,workItemContext.getLocale())+"\"";
							break;
						case ValueType.LONGTEXT:
							if(value!=null){
								List<MacroDef> macroDefList= MacroBL.parseMacros(value.toString(), MacroManager.MACRO_ISSUE);
								if(macroDefList!=null){
									IMacro macro=new MacroIssueDecorate();
									MacroContext macroContext=new MacroContext();
									macroContext.setUseProjectSpecificID(useProjectSpecificID);
									value=MacroBL.replaceMacros(macroDefList,value.toString(),macroContext,macro);
								}
							}

							jsonValue = value==null?null:"\""+ JSONUtility.escape(value.toString())+"\"";
							//
							break;
						default:
							jsonValue = fieldTypeRendererRT.encodeJsonValue(value);
							break;
					}
				}
			}

			//append "," after each value. we need to remove the last one
			JSONUtility.appendJSONValue(sb, "f" + fieldID, jsonValue);
		}
		if(sb.length()>1){
			//remove last ","
			sb.deleteCharAt(sb.length()-1);
		}

		sb.append("}");
		return sb.toString();
	}
	public static  String encodeFieldDisplayValues(Set<Integer> presentFields,WorkItemContext workItemContext,boolean  useProjectSpecificID, boolean isMobileApplication){
		StringBuilder sb=new StringBuilder();
		sb.append("{");

		for (Iterator<Integer> iterator = presentFields.iterator(); iterator.hasNext();) {
			Integer fieldID=iterator.next();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);

			Object value=workItemContext.getWorkItemBean().getAttribute(fieldID);
			String displayValue = null;

			if(useProjectSpecificID&&fieldID.intValue()==SystemFields.ISSUENO){
				displayValue=ItemBL.getSpecificProjectID(workItemContext);
			}else{
				if (fieldTypeRT!=null) {
					displayValue=fieldTypeRT.getShowValue(value,workItemContext,fieldID);
				}
			}
			if(fieldID==SystemFields.DESCRIPTION){
				displayValue=ItemDetailBL.formatDescription(displayValue,workItemContext.getLocale());
			}
			if(isMobileApplication) {
				try {
					if(fieldTypeRT.getValueType() == ValueType.LONGTEXT) {
						displayValue = Html2Text.getNewInstance().convert(displayValue);
						displayValue = displayValue.replaceAll("[\n\r]", "");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
			//append "," after each value. we need to remove the last one
			JSONUtility.appendStringValue(sb,"f"+fieldID,displayValue);
		}
		if(sb.length()>1){
			//remove last ","
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("}");
		return sb.toString();
	}



	public static String encodeFieldConfigTOMap(Map<Integer,FieldConfigTO> fieldConfigTOMap){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(fieldConfigTOMap!=null){
			Integer fieldID;
			FieldConfigTO fieldConfigTO;
			Map.Entry<Integer,FieldConfigTO> entry;
			for (Iterator<Map.Entry<Integer,FieldConfigTO>> iterator=fieldConfigTOMap.entrySet().iterator();iterator.hasNext();){
				entry=iterator.next();
				fieldID=entry.getKey();
				fieldConfigTO = entry.getValue();
				JSONUtility.appendJSONValue(sb,"f"+fieldID,encodeFieldConfigTO(fieldConfigTO),!iterator.hasNext());
			}
		}
		sb.append("}");
		return sb.toString();
	}
	public static String encodeFieldConfigTO(FieldConfigTO fieldConfigTO){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "label", fieldConfigTO.getLabel());
		JSONUtility.appendStringValue(sb, "tooltip", fieldConfigTO.getTooltip());

		JSONUtility.appendJSONValue(sb, "jsonData", fieldConfigTO.getJsonData());

		JSONUtility.appendBooleanValue(sb,"hasDependences",fieldConfigTO.isHasDependences());
		JSONUtility.appendBooleanValue(sb,"clientSideRefresh",fieldConfigTO.isClientSideRefresh());
		
		JSONUtility.appendBooleanValue(sb,"required",fieldConfigTO.isRequired());
		JSONUtility.appendBooleanValue(sb,"readonly",fieldConfigTO.isReadonly());
		JSONUtility.appendBooleanValue(sb,"invisible",fieldConfigTO.isInvisible());

		JSONUtility.appendIntegerValue(sb, "fieldID", fieldConfigTO.getFieldID(), true);

		sb.append("}");
		return sb.toString();

	}
}
