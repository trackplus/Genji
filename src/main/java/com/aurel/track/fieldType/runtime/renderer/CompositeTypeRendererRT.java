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


package com.aurel.track.fieldType.runtime.renderer;


import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.json.JSONUtility;

import java.util.Iterator;
import java.util.Map;

public abstract class CompositeTypeRendererRT extends AbstractTypeRendererRT{
	/**
	 * Number of parts the composite field is contained of
	 * @return
	 */
	public abstract int getNumberOfParts();
		
	
	/**
	 * The custom field type of each part 
	 * @param index
	 * @return
	 */
	public abstract TypeRendererRT getCustomTypeRenderer(int index);
	public boolean isCascading(){
		return false;
	}


	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.CompositeTypeRenderer";
	}
	@Override
	public String getExtReadOnlyClassName(){
		return "com.aurel.trackplus.field.LabelTypeRenderer";
	}
	@Override
	public String encodeJsonValue(Object value){
		if(value==null){
			return  null;
		}
		Map<Integer, Object> compositeValueMap=(Map<Integer, Object>)value;
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		Iterator<Integer> it=compositeValueMap.keySet().iterator();
		TypeRendererRT typeRendererRT;
		int parts=getNumberOfParts();
		for(int i=1;i<=parts;i++){
			typeRendererRT=getCustomTypeRenderer(i);
			sb.append("\""+i+"\":").append(typeRendererRT.encodeJsonValue(compositeValueMap.get(i)));
			if(i<parts){
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();


	}
	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		return value;
	}
	@Override
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		int parts=getNumberOfParts();
		JSONUtility.appendBooleanValue(sb,"cascading",isCascading());
		sb.append("\"parts\":[");
		for(int i=1;i<=parts;i++){
			TypeRendererRT typeRendererRT=getCustomTypeRenderer(i);
			sb.append("{");
			JSONUtility.appendJSONValue(sb,"jsonData",typeRendererRT.createJsonData(field,i,workItemContext));
			JSONUtility.appendStringValue(sb,"extClassName",typeRendererRT.getExtClassName(),true);
			sb.append("}");
			if(i<parts){
				sb.append(",");
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}


}
class CompositeTemplate{
	private String template;
	private Integer parameterCode;
	public CompositeTemplate(String template,Integer parameterCode){
		this.template=template;
		this.parameterCode=parameterCode;
	}
	/**
	 * @return the parameterCode
	 */
	public Integer getParameterCode() {
		return parameterCode;
	}
	/**
	 * @param parameterCode the parameterCode to set
	 */
	public void setParameterCode(Integer parameterCode) {
		this.parameterCode = parameterCode;
	}
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
}
