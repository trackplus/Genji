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


package com.aurel.track.user;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.util.PropertiesHelper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class UserPreferencesAction extends ActionSupport  implements Preparable, SessionAware{
	private static final long serialVersionUID = -811854802848686582L;
	private Map<String,Object> session;
	private TPersonBean tPerson;
	private String property;
	private String value;
	private String property2;
	private String value2;
	@Override
	public void prepare() throws Exception {
		tPerson = (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	@Override
	public String execute() throws Exception {
		if(tPerson!=null&&property!=null&&property.length()>0){
			String preferences = tPerson.getPreferences();
			if(value==null||value.trim().length()==0){
				tPerson.setPreferences(PropertiesHelper.removeProperty(preferences, property));
			}else{
				tPerson.setPreferences(PropertiesHelper.setProperty(preferences,property, value));
			}
			preferences = tPerson.getPreferences();
			if(property2!=null&&property2.length()>0){
				if(value2==null||value2.trim().length()==0){
					tPerson.setPreferences(PropertiesHelper.removeProperty(preferences, property2));
				}else{
					tPerson.setPreferences(PropertiesHelper.setProperty(preferences,property2, value2));
				}
			}
			PersonBL.saveSimple(tPerson);
		}
		return "ok";
	}
	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}
}
