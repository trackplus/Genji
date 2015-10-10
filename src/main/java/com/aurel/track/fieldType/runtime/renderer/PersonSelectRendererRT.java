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

import java.util.Iterator;
import java.util.List;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;

public class PersonSelectRendererRT extends SelectRendererRT{
	//singleton isntance
	private static PersonSelectRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static SelectRendererRT getInstance() {
		if (instance == null) {
			instance = new PersonSelectRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public PersonSelectRendererRT() {
	}

	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.PersonPickerRenderer";
	}

	protected String encodeOptions(List<ILabelBean> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			TPersonBean personBean;
			for (Iterator<ILabelBean> iterator = list.iterator(); iterator.hasNext();) {
				personBean = (TPersonBean)iterator.next();
				sb.append("{");
				if(personBean.getLabel()!=null){
					sb.append("\""  + JSONUtility.JSON_FIELDS.LABEL + "\"").append(":\"").append(JSONUtility.escape(personBean.getLabel())).append("\",");
				}
				String iconCls;
				if(personBean.isGroup()){
					iconCls=PersonBL.GROUP_ICON_CLASS;
				}else{
					iconCls=PersonBL.USER_ICON_CLASS;
				}
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, iconCls);
				sb.append("\"" + JSONUtility.JSON_FIELDS.ID + "\"").append(":").append(personBean.getObjectID());
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/*

	 */
}
