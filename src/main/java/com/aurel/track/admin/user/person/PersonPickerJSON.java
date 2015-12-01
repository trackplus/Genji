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

package com.aurel.track.admin.user.person;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;

import java.util.Iterator;
import java.util.List;

/**
 */
public class PersonPickerJSON {
	public static String encodePersonList(List<TPersonBean> personBeanList,boolean includeEmail){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(personBeanList!=null){
			for (Iterator<TPersonBean> iterator = personBeanList.iterator(); iterator.hasNext();) {
				TPersonBean personBean = iterator.next();
				sb.append(encodePerson(personBean,includeEmail));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static String encodePerson(TPersonBean personBean,boolean includeEmail){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, personBean.getObjectID().toString());
		String name=personBean.getFullName();
		if(includeEmail&&personBean.getEmail()!=null){
			name=name+"<"+personBean.getEmail()+">";
		}
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.TEXT, name);
		String iconCls;
		if(personBean.isGroup()){
			iconCls="group16";
		}else{
			iconCls="user16";
		}
		JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ICONCLS, iconCls);
		JSONUtility.appendBooleanValue(sb,  JSONUtility.JSON_FIELDS.CHECKED, false);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.LEAF, true, true);
		sb.append("}");
		return sb.toString();
	}
}
