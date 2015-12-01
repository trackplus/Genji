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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.item.history.HistoryEntry;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;

public class PrintItemBL {
	public static String getPrintItemDataJSON(TPersonBean personBean,WorkItemContext ctx,Locale locale){
		TWorkItemBean workItemBean = ctx.getWorkItemBean();
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"workItemID",workItemBean.getObjectID());
		String statusDisplay=ItemBL.getStatusDisplay(ctx);
		JSONUtility.appendStringValue(sb,"statusDisplay",statusDisplay);
		JSONUtility.appendStringValue(sb,"synopsis",workItemBean.getSynopsis());
		String issueNoLabel= LocalizeUtil.getLocalizedTextFromApplicationResources("printItem.lbl.itemNumber", locale);

		List<TWorkItemBean> children = ItemBL.getChildren(ctx.getWorkItemBean().getObjectID(), personBean.getObjectID());
		if(children!=null && !children.isEmpty()){
			includeChildern(sb,children,locale);
		}

		//flatAttachment
		List<FlatHistoryBean> flatHistoryList = PrintItemDetailsBL.getFlatAttachments(workItemBean.getObjectID(), locale);
		String flatValueLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("item.printItem.lbl.tab.attachments", locale);
		if(flatHistoryList!=null && !flatHistoryList.isEmpty()){
			includeFlatValues(sb,"flatAttachment",flatHistoryList,flatValueLabel,locale);
		}
		JSONUtility.appendStringValue(sb,"issueNoLabel",issueNoLabel,true);
		sb.append("}");
		return sb.toString();
	}
	private static void includeChildern(StringBuilder sb,List<TWorkItemBean> children,Locale locale){
		String lblChildIssues=LocalizeUtil.getLocalizedTextFromApplicationResources("item.printItem.lbl.childIssues", locale);
		String lblItemNumber=LocalizeUtil.getLocalizedTextFromApplicationResources("item.lbl.itemNumber", locale);
		String lblState=FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.STATE,locale);
		String lblSynopsys=FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.SYNOPSIS, locale);

		JSONUtility.appendStringValue(sb,"lblChildIssues",lblChildIssues);
		JSONUtility.appendStringValue(sb,"lblItemNumber",lblItemNumber);
		JSONUtility.appendStringValue(sb,"lblState",lblState);
		JSONUtility.appendStringValue(sb,"lblSynopsys",lblSynopsys);
		sb.append("children:[");
		TWorkItemBean workItemBean=null;
		for(int i=0;i<children.size();i++){
			workItemBean=children.get(i);
			sb.append("{");
			String stateDisplay=LookupContainer.getStatusBean(workItemBean.getStateID(), locale).getLabel();
			JSONUtility.appendStringValue(sb,"stateDisplay",stateDisplay);
			JSONUtility.appendStringValue(sb,"synopsis",workItemBean.getSynopsis());
			JSONUtility.appendIntegerValue(sb,"objectID",workItemBean.getObjectID(),true);
			sb.append("}");
			if(i<children.size()-1){
				sb.append(",");
			}
		}
		sb.append("],");
	}
	private static void includeFlatValues(StringBuilder sb,String name,List<FlatHistoryBean> flatHistoryList,String flatValueLabel,Locale locale){
		sb.append(name).append(":{");
		sb.append("flatHistoryList:[");
		for(int i=0;i<flatHistoryList.size();i++){
			sb.append(encodeJSON(flatHistoryList.get(i),locale));
			if(i<flatHistoryList.size()-1){
				sb.append(",");
			}
		}
		sb.append("],");
		JSONUtility.appendStringValue(sb,"flatValueLabel",flatValueLabel,true);
		sb.append("},");
	}
	private static String encodeJSON(FlatHistoryBean flatHistoryBean,Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"title",flatHistoryBean.getTitle());
		JSONUtility.appendIntegerValue(sb, "renderType", flatHistoryBean.getRenderType());
		JSONUtility.appendStringValue(sb, "lastEdit", DateTimeUtils.getInstance().formatGUIDateTime(flatHistoryBean.getLastEdit(), locale));
		JSONUtility.appendStringValue(sb,"changedByName",flatHistoryBean.getChangedByName());
		JSONUtility.appendIntegerValue(sb, "type", flatHistoryBean.getType());
		JSONUtility.appendStringValue(sb,"iconName",flatHistoryBean.getIconName());

		appendHistoryEntries(sb,"historyEntries",flatHistoryBean.getHistoryEntries());
		appendHistoryEntries(sb,"historyLongEntries",flatHistoryBean.getHistoryLongEntries());

		JSONUtility.appendIntegerValue(sb,"workItemID",flatHistoryBean.getWorkItemID(),true);
		sb.append("}");
		return sb.toString();
	}
	private static void appendHistoryEntries(StringBuilder sb,String name,List<HistoryEntry> historyEntries){
		if(historyEntries!=null && !historyEntries.isEmpty()){
			sb.append(name).append(":[");
			HistoryEntry he;
			for(int i=0;i<historyEntries.size();i++){
				he=historyEntries.get(i);
				sb.append("{");
				JSONUtility.appendStringValue(sb,"fieldLabel",he.getFieldLabel());
				JSONUtility.appendStringValue(sb,"changedText",he.getChangedText());
				JSONUtility.appendStringValue(sb,"newValue",he.getNewValue());
				JSONUtility.appendStringValue(sb,"oldValue",he.getOldValue()==null?"":he.getOldValue(),true);
				sb.append("}");
				if(i<historyEntries.size()-1){
					sb.append(",");
				}
			}

			sb.append("],");
		}
	}

}
