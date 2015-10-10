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

package com.aurel.track.admin.user.person;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 */
public class PersonPickerAction extends ActionSupport implements Preparable, SessionAware{

	private Map<String,Object> session;
	private Locale locale;
	private TPersonBean personBean;
	private boolean includeEmail;
	private boolean includeGroups;

	public static final long serialVersionUID = 400L;


	public void prepare() {
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}

	@Override
	public String execute() {
		List<TPersonBean> personBeanList;
		if(includeGroups) {
			personBeanList = PersonBL.loadActivePersonsAndGroups();
		}else{
			personBeanList = PersonBL.loadActivePersons();
		}
		Map<Integer, String> departmentMap = DepartmentBL.getDepartmentMap();

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb,JSONUtility.JSON_FIELDS.SUCCESS,true);
		JSONUtility.appendJSONValue(sb,"data",PersonPickerJSON.encodePersonList(personBeanList,includeEmail),true);
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());

		return null;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	public void setIncludeEmail(boolean includeEmail) {
		this.includeEmail = includeEmail;
	}

	public void setIncludeGroups(boolean includeGroups) {
		this.includeGroups = includeGroups;
	}
}
