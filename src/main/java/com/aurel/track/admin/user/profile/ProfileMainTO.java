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

package com.aurel.track.admin.user.profile;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.LabelValueBean;

/**
 * Transfer object used for incoming email (POP3, IMAP) configuration.
 * @author Joerg Friedrich
 */
public class ProfileMainTO implements Serializable{

	private static final long serialVersionUID = 400L;
	
	public static class CONTEXT{
		public static final int SELFREGISTRATION = 0;
		public static final int PROFILEEDIT = 1;
		public static final int USERADMINADD = 2;
		public static final int USERADMINEDIT = 3;
	}
			
	private String userName;
	private String firstName;
	private String lastName;
	private String userEmail;
	private String passwd;
	private String passwd2;
	private Boolean ldapOn;
	private Boolean forceLdap;
	private Boolean ldapUser;
	private List<LabelValueBean> locales;
	private String locale;	
	private Boolean adminOrGuest;
	private String domainPat;
	private String userTz;
	private List<LabelValueBean> timeZones;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Boolean getLdapOn() {
		return (ldapOn==null ? new Boolean(false):ldapOn);
	}
	public void setLdapOn(Boolean ldapOn) {
		this.ldapOn = ldapOn;
	}
	
	public Boolean getForceLdap() {
		return (forceLdap==null ? new Boolean(false):forceLdap);
	}
	public void setForceLdap(Boolean force) {
		this.forceLdap = force;
	}
	
	public List<LabelValueBean> getLocales() {
		return locales;
	}
	public void setLocales(List<LabelValueBean> locale) {
		this.locales = locale;
	}
	
	public String getPasswd2() {
		return passwd2;
	}
	public void setPasswd2(String passwd2) {
		this.passwd2 = passwd2;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public Boolean getLdapUser() {
		return (ldapUser==null ? new Boolean(false) : ldapUser);
	}
	public void setLdapUser(Boolean ldapEnabled) {
		this.ldapUser = ldapEnabled;
	}
	public Boolean getAdminOrGuest() {
		return (adminOrGuest==null ? new Boolean(false) : adminOrGuest);
	}
	public void setAdminOrGuest(Boolean adminOrGuest) {
		this.adminOrGuest = adminOrGuest;
	}
	public String getDomainPat() {
		return domainPat;
	}
	public void setDomainPat(String domainPat) {
		this.domainPat = domainPat;
	}
	public String getUserTz() {
		return userTz;
	}
	public void setUserTz(String _usertz) {
		userTz = _usertz;
	}
	public List<LabelValueBean> getTimeZones() {
		return timeZones;
	}
	public void setTimeZones(List<LabelValueBean> tzs) {
		this.timeZones = tzs;
	}

}
