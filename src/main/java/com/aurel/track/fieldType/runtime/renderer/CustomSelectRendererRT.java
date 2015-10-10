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

import com.aurel.track.fieldType.runtime.base.WorkItemContext;

/**
 *
 */
public class CustomSelectRendererRT extends SelectRendererRT {
	
	private static final Logger LOGGER = LogManager.getLogger(CustomSelectRendererRT.class);
	
	//singleton isntance
	private static CustomSelectRendererRT instance;

	/**
	 * get a singleton instance
	 * @return singleton instance
	 */
	public static CustomSelectRendererRT getInstance() {
		if (instance == null) {
			instance = new CustomSelectRendererRT();
		}
		return instance;
	}
	private CustomSelectRendererRT(){
	}
	@Override
	public String encodeJsonValue(Object value){
		Object selection=null;
		if(value!=null){
			Object[] arr=(Object[])value;
			if(arr.length>0){
				selection=arr[0];
			}
		}
		return selection==null?null: selection.toString();
	}

	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Integer result=null;
		if(value!=null){
			try{
				result=Integer.decode(value);
			}catch (NumberFormatException ex){
				LOGGER.error(ExceptionUtils.getStackTrace(ex));
				throw new TypeConversionException("common.err.invalid.number",ex);
			}
		}
		if(result==null){
			return null;
		}else{
			return new Object[]{result};
		}
	}

}
