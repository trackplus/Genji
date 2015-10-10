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

/**
 * A renderer for duration
 */
public class DurationRendererRT extends DoubleRendererRT {
	//singleton instance
	private static DurationRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static DurationRendererRT getInstance() {
		if (instance == null) {
			instance = new DurationRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public DurationRendererRT() {
	}
	
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.DurationRenderer";
	}
	
	/**
	 * No negative numbers are allowed
	 */
	@Override
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"minValue", 0, true);
		sb.append("}");
		return sb.toString();
	}
}
