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

public abstract class AbstractTypeRendererRT implements TypeRendererRT{
	public String getInnerTemplateName(){
		return null;
	}
	/*public void updateCustomProperties(Map map,TFieldBean field,WorkItemContext workItemContext){
	}*/

	public boolean isLabelInsideTemplate() {
		return false;
	}
	public String getExtClassName(){
		return "com.aurel.trackplus.field.LabelTypeRenderer";
	}
	public String getExtReadOnlyClassName(){
		return "com.aurel.trackplus.field.LabelTypeRenderer";
	}
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		return "{}";
	}
	public String createJsonData(TFieldBean field,Integer parameterCode,WorkItemContext workItemContext){
		return createJsonData(field,workItemContext);
	}

	/**
	 * TODO remove from here
	 * @param value
	 * @return
	 */
	public String encodeJsonValue(Object value){
		return value==null?null:"\""+ JSONUtility.escape(value.toString())+"\"";
	}

	/**
	 * TODO remove from here
	 *
	 * @param value
	 * @param fieldID
	 * @param workItemContext @return
	 * @throws TypeConversionException
	 */
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		return value;
	}
}
