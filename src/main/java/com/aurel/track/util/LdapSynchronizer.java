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


package com.aurel.track.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.scripting.BINDING_PARAMS;
import com.aurel.track.admin.customize.scripting.ScriptAdminBL;
import com.aurel.track.admin.customize.scripting.ScriptUtil;
import com.aurel.track.admin.user.group.GroupMemberBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.util.event.IEventHandler;

public class LdapSynchronizer implements IEventHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(LdapSynchronizer.class);

	public Map handleEvent(Map inputBinding) {
		TSiteBean siteBean = (TSiteBean)inputBinding.get(BINDING_PARAMS.SITEBEAN);
		String ldapURL = siteBean.getLdapServerURL();
		String baseURL = LdapUtil.getBaseURL(ldapURL);
		boolean deactivateUnknown = true;
		//String groupAttributeName = null;
		Map<String, String> ldapMap = LdapUtil.getLdapMap();
		if (ldapMap!=null) {
			try {
				deactivateUnknown = Boolean.valueOf(ldapMap.get(LdapUtil.LDAP_CONFIG.DEACTIVATE_UNKNOWN));
			} catch (Exception e) {
				
			}
			//groupAttributeName = ldapMap.get(LdapUtil.LDAP_CONFIG.GROUP_NAME);
		}
	    /**
	     * Format of parameter file is "Group name : ldap URL"
	     */
	    TScriptsBean scriptsBean = ScriptAdminBL.loadByClassName("LdapSynchronizerParams");
	    List<String> lines = new ArrayList<String>();
	    if (scriptsBean==null) {
	    	LOGGER.warn("Could not find the script LdapSynchronizerParams");
	    } else {
	    	String params = scriptsBean.getSourceCode();
	    	if (params!=null) {
	    		lines = ScriptUtil.getParameterDataLines(params);
	    	}
	    }
	    Map<String, TPersonBean> ldapPersonsFromGroups = null;
	    Map<String, String> groups = ScriptUtil.getParameterMap("^(.*):\\s*(.*)", "#", lines);
	    Map<String, List<String>> groupToMemberReferencesMap = new HashMap<String, List<String>>();
	    try {
			Map<String, TPersonBean> ldapGroups = LdapUtil.getLdapGroupsByList(baseURL, siteBean, null/*groupAttributeName*/, groupToMemberReferencesMap, groups);
			Map<String, List<TPersonBean>> ldapGroupsToPersons = LdapUtil.getGroupToPersonMaps(baseURL, siteBean, ldapGroups, groupToMemberReferencesMap);
			ldapPersonsFromGroups = GroupMemberBL.synchronizeUserListWithGroup(ldapGroupsToPersons);
			if (deactivateUnknown) {
				try {
					LdapUtil.deactivateUsers(ldapPersonsFromGroups);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return ldapPersonsFromGroups;
	}
}
