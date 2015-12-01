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
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.IntegerStringBean;


/**
 *
 */
public class ItemSaveJSON {

	public static String encodeErrorOutOfDate(WorkItemContext workItemContext,Locale locale,boolean  useProjectSpecificID,TPersonBean personBean, boolean isMobileApplication){
		StringBuilder sb=new StringBuilder();
		Date originalLastModifiedDate=workItemContext.getWorkItemBean().getLastEdit();
		//DateTimeUtils dtu=DateTimeUtils.getInstance();
		//errors.add(new LabelValueBean("Item already saved on "+dtu.formatGUIDateTime(originalLastModifiedDate,locale)+"!",
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
		sb.append("\"data\":{");
		JSONUtility.appendIntegerValue(sb, "errorCode", ItemSaveBL.ERROR_CODE.OUT_OF_DATE);
		Integer modifiedByID=workItemContext.getWorkItemBean().getChangedByID();
		String modifiedByName="";
		JSONUtility.appendIntegerValue(sb, "modifiedByID", modifiedByID);
		JSONUtility.appendStringValue(sb, "modifiedByName", modifiedByName);
		JSONUtility.appendJSONValue(sb, "workItemContext",ItemActionJSON.encodeContext(workItemContext,locale, useProjectSpecificID,personBean, isMobileApplication));
		JSONUtility.appendStringValue(sb, "lastModified", DateTimeUtils.getInstance().formatISODateTime(originalLastModifiedDate), true);

		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	public static String encodeConversionErrors(List<IntegerStringBean> conversionErrors){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
		sb.append("\"data\":{");
		JSONUtility.appendIntegerValue(sb, "errorCode", ItemSaveBL.ERROR_CODE.CONVERSION);
		JSONUtility.appendIntegerStringBeanList(sb, "errors", conversionErrors, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	public static String encodeErrorData(List<ErrorData> errorDataList, int errorCode, Locale locale) {
		List<IntegerStringBean> errors = ErrorHandlerJSONAdapter.handleErrorListAsIntegerStringBean(errorDataList, /*errorCode==ItemSaveBL.ERROR_CODE.CONFIRMATION,*/ locale);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
		sb.append("\"data\":{");
		JSONUtility.appendIntegerValue(sb,"errorCode", errorCode);
		JSONUtility.appendIntegerStringBeanList(sb, "errors", errors, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	public static String encodeSuccess(Integer newWorkItemID,String workItemIDDisplay,String title){
		return encodeSuccess(newWorkItemID,workItemIDDisplay,title,null);
	}
	public static String encodeSuccess(Integer newWorkItemID,String workItemIDDisplay,String title,String description){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("\"data\":{");
		JSONUtility.appendStringValue(sb,"title",title);
		JSONUtility.appendStringValue(sb,"workItemIDDisplay",workItemIDDisplay);
		JSONUtility.appendStringValue(sb,"description",description);
		JSONUtility.appendIntegerValue(sb,"workItemID",newWorkItemID,true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
