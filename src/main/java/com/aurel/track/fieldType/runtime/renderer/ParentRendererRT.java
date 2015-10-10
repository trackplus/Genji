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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.json.JSONUtility;


public class ParentRendererRT extends AbstractTypeRendererRT{
	
	private static final Logger LOGGER = LogManager.getLogger(ParentRendererRT.class);
	
	//singleton instance
	private static ParentRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ParentRendererRT getInstance() {
		if (instance == null) {
			instance = new ParentRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public ParentRendererRT() {
	}
	public String getExtClassName(){
		return "com.aurel.trackplus.field.ParentTypeRenderer";
	}
	public String getExtReadOnlyClassName(){
		return "com.aurel.trackplus.field.ParentTypeRendererReadOnly";
	}
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		Integer parentID=workItemContext.getWorkItemBean().getSuperiorworkitem();
		if(parentID!=null){
			TWorkItemBean parentBean=null;
			try {
				parentBean=ItemBL.loadWorkItem(parentID);
			} catch (ItemLoaderException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));  //To change body of catch statement use File | Settings | File Templates.
			}
			if(parentBean!=null){
				if(workItemContext.isUseProjectSpecificID()){
					String projectSpecificID=ItemBL.getSpecificProjectID(parentBean);
					JSONUtility.appendStringValue(sb,"projectSpecificID",projectSpecificID);
				}
				String title=parentBean.getSynopsis();
				JSONUtility.appendStringValue(sb,"title",title,true);

			}
		}
		sb.append("}");
		return sb.toString();
	}
	public String encodeJsonValue(Object value){
		return value==null?null:"\""+ JSONUtility.escape(value.toString())+"\"";
	}
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
}
