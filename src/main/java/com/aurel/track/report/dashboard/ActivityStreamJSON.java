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

package com.aurel.track.report.dashboard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.item.FileDiffTO;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.item.history.HistoryEntry;
import com.aurel.track.json.JSONUtility;

/**
 * 
 */
public class ActivityStreamJSON {
	public static String encodeActivityItemList(List<ActivityStream.ActivityStreamItem> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<ActivityStream.ActivityStreamItem> iterator = list.iterator(); iterator.hasNext();) {
				ActivityStream.ActivityStreamItem activityStreamItem = iterator.next();
				sb.append(encodeActivityItem(activityStreamItem));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");

		return sb.toString();
	}
	
	public static String encodeActivityHTMLData(List<FlatHistoryBean>beanList) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(beanList != null){
			for (Iterator<FlatHistoryBean> iterator = beanList.iterator(); iterator.hasNext();) {
				FlatHistoryBean aBean = iterator.next();
				sb.append("{");
				JSONUtility.appendStringValue(sb, "workItemID", aBean.getItemID() == null ? "" : aBean.getItemID());
				JSONUtility.appendStringValue(sb, "title", aBean.getTitle() == null ? "" : aBean.getTitle());
				JSONUtility.appendStringValue(sb, "lastEdit", aBean.getLastEdit() == null ? aBean.getLastEdit().toString() : "");
				JSONUtility.appendStringValue(sb, "dateFormatted", aBean.getDateFormatted() == null ? "" : aBean.getDateFormatted());
				JSONUtility.appendStringValue(sb, "changedByName", aBean.getChangedByName() == null ? "" : aBean.getChangedByName());
				JSONUtility.appendStringValue(sb, "personID", aBean.getPersonID() == null ? "" : aBean.getPersonID().toString());
				JSONUtility.appendStringValue(sb, "type", Integer.toString(aBean.getType()) == null ? "" : Integer.toString(aBean.getType()));
				JSONUtility.appendStringValue(sb, "iconName", aBean.getIconName() == null ? "" : aBean.getIconName());
				JSONUtility.appendStringValue(sb, "projectID", aBean.getProjectID() == null ? "" : aBean.getProjectID().toString());
				JSONUtility.appendStringValue(sb, "project", aBean.getProject() == null ? "" : aBean.getProject());
				JSONUtility.appendStringValue(sb, "revisionNo", aBean.getRevisionNo() == null ? "" : aBean.getRevisionNo());
				JSONUtility.appendStringValue(sb, "revisionComment", aBean.getRevisionComment() == null ? "" : aBean.getRevisionComment());
				JSONUtility.appendStringValue(sb, "repository", aBean.getRepository() == null ? "" : aBean.getRepository());
				JSONUtility.appendJSONValue(sb, "historyEntries", appendHistoryEntries(aBean.getHistoryEntries()));
			    JSONUtility.appendJSONValue(sb, "historyLongEntries", appendHistoryLongEntries(aBean.getHistoryLongEntries()));
				JSONUtility.appendJSONValue(sb, "changedPaths", appendChangedPaths(aBean.getChangedPaths()), true);
				sb.append("}");
				if(iterator.hasNext()) {
					sb.append(",");				
				}
			}
		}
		sb.append("]");		
		return sb.toString();
	}
	
	public static String encodeAvatarCheckSum(Map<Integer, String>personIDToAvatarCheckSum) {
		  Iterator it = personIDToAvatarCheckSum.entrySet().iterator();
		  StringBuilder sb = new StringBuilder();
		  sb.append("{");
		  if(personIDToAvatarCheckSum.size() > 0) {
			  while (it.hasNext()) {
				  Map.Entry<Integer, String> pairs = (Map.Entry)it.next();
				  JSONUtility.appendStringValue(sb, pairs.getKey().toString(), pairs.getValue().toString(), true);
				  if(it.hasNext()) {
					  sb.append(",");
				  }
			  }
		  }
		  sb.append("}");
		  return sb.toString();
	}
	
	public static String appendHistoryEntries(List<HistoryEntry>historyList) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if(historyList != null) {
			String changedText = "";
			String newValue = "";
			String oldValue = "";
			String fieldLabel = "";
			String newLine = "<br>";
			for (Iterator<HistoryEntry> iterator = historyList.iterator(); iterator.hasNext();) {
				if(!iterator.hasNext()) {
					newLine = "";
				}
				HistoryEntry aBean = iterator.next();
				changedText += aBean.getChangedText()  + newLine;
				newValue += aBean.getNewValue() + newLine;
				oldValue += aBean.getOldValue() + newLine;
				fieldLabel += aBean.getFieldLabel() + newLine;
				
						
			}
			changedText = changedText.replaceAll("<strong>","<b>");
			changedText = changedText.replaceAll("</strong>","</b>");
			changedText = changedText.replaceAll("<span class=\"histOldValue\">","<font color='red'>");
			changedText = changedText.replaceAll("<span class=\"histNewValue\">","<font color='red'>");
			changedText = changedText.replaceAll("</span>","</font>");

			JSONUtility.appendStringValue(sb, "changedText", changedText);
			JSONUtility.appendStringValue(sb, "newValue", newValue);
			JSONUtility.appendStringValue(sb, "oldValue", oldValue);
			JSONUtility.appendStringValue(sb, "fieldLabel", fieldLabel, true);
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static String appendHistoryLongEntries(List<HistoryEntry>historyList) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if(historyList != null) {
			String diff = "";
			String fieldLabel = "";
			String newValue = "";
			String oldValue = "";
			String newLine = "<br>";
			for (Iterator<HistoryEntry> iterator = historyList.iterator(); iterator.hasNext();) {
				if(!iterator.hasNext()) {
					newLine = "";
				}
				HistoryEntry aBean = iterator.next();
				diff += aBean.getDiff() + newLine;
				fieldLabel += aBean.getFieldLabel() + newLine;
				newValue += aBean.getNewValue() + newLine;
				oldValue += aBean.getOldValue() + newLine;						
			}
			diff = diff.replaceAll("<span class=\\\"diff-html-removed\\\" id=\\\"removed-diff.+?\\\">(.+?)</span>",
					                "<font color='red'><s>$1</s></font>");			
			diff = diff.replaceAll("<span class=\\\"diff-html-added\\\" id=\\\"added-diff.+?\\\">(.+?)</span>",
	                "<font color='green'>$1</font>");
			JSONUtility.appendStringValue(sb, "diff", diff);			
			JSONUtility.appendStringValue(sb, "fieldLabel", fieldLabel);
			JSONUtility.appendStringValue(sb, "newValue", newValue);
			JSONUtility.appendStringValue(sb, "oldValue", oldValue, true);
					
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static String appendChangedPaths(List<FileDiffTO>changedPaths) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String localizedType = "";
		String link = "";
		String path = "";
		String newLine = "<br>";
		if(changedPaths != null)  {
			for (Iterator<FileDiffTO> iterator = changedPaths.iterator(); iterator.hasNext();) {
				if(!iterator.hasNext()) {
					newLine = "";
				}
				FileDiffTO aBean = iterator.next();
				localizedType += aBean.getLocalizedType() + newLine;
				link += aBean.getLink() + newLine;
				path += aBean.getPath() + newLine;
				
			}
			JSONUtility.appendStringValue(sb, "localizedType", localizedType);			
			JSONUtility.appendStringValue(sb, "link", link);
			JSONUtility.appendStringValue(sb, "path", path, true);
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static String encodeActivityItem(ActivityStream.ActivityStreamItem activityStreamItem){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendDateValue(sb,"date",activityStreamItem.getDate());
		JSONUtility.appendStringValue(sb, "dateFormatted", activityStreamItem.getDateFormatted());
		JSONUtility.appendIntegerValue(sb, "itemID", activityStreamItem.getItemID());
		JSONUtility.appendStringValue(sb, "itemPrefixID", activityStreamItem.getItemPrefixID());
		JSONUtility.appendStringValue(sb, "itemTitle", activityStreamItem.getItemTitle());
		JSONUtility.appendStringValue(sb, "changeByName", activityStreamItem.getChangeByName());
		JSONUtility.appendStringValue(sb, "changes", activityStreamItem.getChanges());
		JSONUtility.appendIntegerValue(sb, "transactionID", activityStreamItem.getTransactionID());
		JSONUtility.appendIntegerValue(sb, "projectID", activityStreamItem.getProjectID());
		JSONUtility.appendStringValue(sb, "project", activityStreamItem.getProject(),true);
		sb.append("}");
		return  sb.toString();
	}
}
