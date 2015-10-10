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


import com.aurel.track.fieldType.runtime.base.WorkItemContext;

/**
 * A renderer used for check at designTime
 * 
 *
 */
public class CheckBoxRendererRT extends AbstractTypeRendererRT{
	//singleton isntance
	private static CheckBoxRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static CheckBoxRendererRT getInstance() {
		if (instance == null) {
			instance = new CheckBoxRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public CheckBoxRendererRT() {
	}

	public String getExtClassName(){
		return "com.aurel.trackplus.field.CheckBoxTypRenderer";
	}

	@Override
	public String encodeJsonValue(Object value){
		boolean  b=(value!=null&&value.toString().equalsIgnoreCase("true"));
		return b?"true":"false";
	}
	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Boolean result=Boolean.FALSE;
		if(value!=null&&value.toString().equalsIgnoreCase("true")){
			result=Boolean.TRUE;
		}
		return result;
	}


}
