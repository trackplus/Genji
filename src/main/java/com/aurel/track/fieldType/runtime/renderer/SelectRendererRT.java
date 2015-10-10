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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;

/**
 * 
 * A renderer for combo at designTime
 *
 */
public class SelectRendererRT extends AbstractTypeRendererRT {
	//singleton isntance
	private static SelectRendererRT instance;

	/**
	 * get a singleton instance
	 * @return singleton instance
	 */
	public static SelectRendererRT getInstance() {
		if (instance == null) {
			instance = new SelectRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public SelectRendererRT() {
	}
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.SelectTypeRenderer";
	}
	@Override
	public String encodeJsonValue(Object value){
		return value==null?null:value.toString();
	}

	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Integer result=null;
		if(value!=null){
			try{
				result=Integer.decode(value);
			}catch (NumberFormatException ex){
				throw new TypeConversionException("common.err.invalid.number",ex);
			}
		}
		return result;
	}
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		return createJsonData(field,null,workItemContext);
	}
	@Override
	public String createJsonData(TFieldBean field,Integer parameterCode, WorkItemContext workItemContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(parameterCode!=null){
			//cascade select
			sb.append("\"forceSelection\"").append(":true,");
		}
		List<ILabelBean> possibleValues=workItemContext.getDropDownContainer().getDataSourceList(MergeUtil.mergeKey(field.getObjectID(), parameterCode));
		sb.append("\"list\"").append(":");
		String listJSON=encodeOptions(possibleValues);
		sb.append(listJSON);
		sb.append("}");
		return sb.toString();
	}
	protected String encodeOptions(List<ILabelBean> possibleValues){
		return JSONUtility.encodeILabelBeanList(possibleValues);
	}
}
