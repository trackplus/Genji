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

import java.util.Date;
import java.util.Locale;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;


/**
 * A renderer for date in designTime envirorment
 * 
 *
 */
public class DateRendererRT extends AbstractTypeRendererRT{
	//singleton instance
	private static DateRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static DateRendererRT getInstance() {
		if (instance == null) {
			instance = new DateRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public DateRendererRT() {
	}
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.DateTypeRenderer";
	}
	@Override
	public String getExtReadOnlyClassName(){
		return "com.aurel.trackplus.field.LabelTypeRenderer";
	}

	@Override
	public String encodeJsonValue(Object value){
		return value==null?null:"\""+ DateTimeUtils.getInstance().formatISODate((Date)value)+"\"";
	}
	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Date date=null;
		if(value!=null) {
			//try{
				//date=DateTimeUtils.getInstance().parseISODateEx(value);
				date=DateTimeUtils.getInstance().parseGUIDate(value, workItemContext.getLocale());
			/*}catch (ParseException ex){
				throw new TypeConversionException("common.err.invalid.date",ex);
			}*/
		}
		return date;
	}

	@Override
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		Date value=(Date)workItemContext.getWorkItemBean().getAttribute(field.getObjectID());
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		Locale locale = workItemContext.getLocale();
		String dateFormatted= DateTimeUtils.getInstance().formatISODate(value);
		JSONUtility.appendStringValue(sb,"dateFormat", DateTimeUtils.getInstance().getExtJSDateFormat(locale));
		JSONUtility.appendStringValue(sb,"dateFormatted",dateFormatted,true);
		sb.append("}");
		return sb.toString();
	}
}
