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

package com.aurel.track.admin.server.siteConfig;

import java.io.Serializable;

/**
 * Transfer object for LDAP configuration.
 * @author Adrian Bojani
 */
public class LdapTO implements Serializable{
	
	private static final long serialVersionUID = 400L;
	
	/**
	 * 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "ldap.serverURL". The base name builds the first part, the second
	 * part is the specific field.
	 *
	 */
	public interface JSONFIELDS {
		//for localizing the fields by errors
		String tabLdap = "tab.ldap";		
		String fsLdap = "fsLdap";
		
		// base name
		String ldap = "ldap.";
		
		//fields
		String serverURL = ldap + "serverURL";
		String attributeLoginName = ldap + "attributeLoginName";
		String bindDN = ldap + "bindDN";
		String enabled = ldap + "enabled";
		String forceLdap = ldap+"force";
	}
	
	private boolean enabled;
	private String serverURL;
	private String attributeLoginName;
	private String bindDN;
	private String password;

	private String loginNameTest;
	private String passwordTest;
	private boolean forceLdap;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isForce() {
		return forceLdap;
	}
	
	public void setForce(boolean force) {
		this.forceLdap = force;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getAttributeLoginName() {
		return attributeLoginName;
	}

	public void setAttributeLoginName(String attributeLoginName) {
		this.attributeLoginName = attributeLoginName;
	}

	public String getBindDN() {
		return bindDN;
	}

	public void setBindDN(String bindDN) {
		this.bindDN = bindDN;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginNameTest() {
		return loginNameTest;
	}

	public void setLoginNameTest(String loginNameTest) {
		this.loginNameTest = loginNameTest;
	}

	public String getPasswordTest() {
		return passwordTest;
	}

	public void setPasswordTest(String passwordTest) {
		this.passwordTest = passwordTest;
	}
}
