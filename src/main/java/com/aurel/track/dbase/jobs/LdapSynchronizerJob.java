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


package com.aurel.track.dbase.jobs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import com.aurel.track.admin.customize.scripting.GroovyScriptExecuter;
import com.aurel.track.admin.user.group.GroupMemberBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.LdapUtil;

public class LdapSynchronizerJob implements Job{


	private static final Logger LOGGER = LogManager.getLogger(LdapSynchronizerJob.class);
	private static boolean enabledAutomaticSync = false;
	private static boolean enabledUserSync = true;
	private static boolean enabledGroupSync = false;
	private static boolean deactivateUnknown = false;
	private static String filter = "*";
	private static String ldapFilterGroups = "*";
	private static String baseDnGroup;
	private static String groupName;
	
	private static long LOGINTERVAL = 1000*60*60*6; // 6 hours
	private static long attemptTimeStamp = new Date().getTime() - LOGINTERVAL;
	
	/**
	 * This permits to synchronize an LDAP user directory with the
	 * Genji database.  We just synchronize uid (login name as
	 * defined in the Genji LDAP configuration page), e-mail ("mail"),
	 * first name ("gn"), and last name ("sn"). We can apply a filter
	 * expression so that we do not synchronize all entries in the
	 * LDAP directory.
	 */
	@Override
	public void execute(JobExecutionContext context) {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		setJobDataMap(jobDataMap);
		if (!ApplicationBean.getInstance().getSiteBean().getIsLDAPOnBool()
				|| !enabledAutomaticSync) {
				return;
			}
			
		LOGGER.debug("LDAP/Genji user database synchronization...");
		if (ClusterBL.getIAmTheMaster()) {
			LOGGER.debug("executing LDAP/Genji user database synchronization...");
			try {
				synchronizeWithLdap();
			}
			catch (Exception e) {
				LOGGER.error("Problem with running scheduler " + e.getMessage());  
			}
		}
	}
	
	public static void setJobDataMap(JobDataMap jobDataMap) {
		String enableStr = jobDataMap.getString(LdapUtil.LDAP_CONFIG.ENABLE_AUTOMATIC_SYNC);
		if (enableStr!=null) {
			try {
				enabledAutomaticSync = Boolean.valueOf(enableStr);
				LOGGER.debug(LdapUtil.LDAP_CONFIG.ENABLE_AUTOMATIC_SYNC + ": " + enabledAutomaticSync);
			} catch(Exception e) {
				LOGGER.warn("Converting " + LdapUtil.LDAP_CONFIG.ENABLE_AUTOMATIC_SYNC + " value " + enableStr + " to boolean failed with " + e.getMessage());
			}
		}
		String enableUserSyncStr = jobDataMap.getString(LdapUtil.LDAP_CONFIG.ENABLE_USER_SYNC);
		if (enableUserSyncStr!=null) {
			try {
				enabledUserSync = Boolean.valueOf(enableUserSyncStr);
				LOGGER.debug(LdapUtil.LDAP_CONFIG.ENABLE_USER_SYNC + ": " + enabledUserSync);
			} catch(Exception e) {
				LOGGER.warn("Converting " + LdapUtil.LDAP_CONFIG.ENABLE_USER_SYNC + " value " + enableUserSyncStr + " to boolean failed with " + e.getMessage());
			}
		}
		String enableGroupSyncStr = jobDataMap.getString(LdapUtil.LDAP_CONFIG.ENABLE_GROUP_SYNC);
		if (enableGroupSyncStr!=null) {
			try {
				enabledGroupSync = Boolean.valueOf(enableGroupSyncStr);
				LOGGER.debug(LdapUtil.LDAP_CONFIG.ENABLE_GROUP_SYNC + ": " + enabledGroupSync);
			} catch(Exception e) {
				LOGGER.warn("Converting " + LdapUtil.LDAP_CONFIG.ENABLE_GROUP_SYNC + " value " + enableGroupSyncStr + " to boolean failed with " + e.getMessage());
			}
		}
		String deactivateUnknownStr = jobDataMap.getString(LdapUtil.LDAP_CONFIG.DEACTIVATE_UNKNOWN);
		if (deactivateUnknownStr!=null) {
			try {
				deactivateUnknown = Boolean.valueOf(deactivateUnknownStr);
				LOGGER.debug(LdapUtil.LDAP_CONFIG.DEACTIVATE_UNKNOWN + ": " + deactivateUnknown);
			} catch(Exception e) {
				LOGGER.warn("Converting " + LdapUtil.LDAP_CONFIG.DEACTIVATE_UNKNOWN + " value " + deactivateUnknownStr + " to boolean failed with " + e.getMessage());
			}
		}
		filter = jobDataMap.getString(LdapUtil.LDAP_CONFIG.LDAP_FILTER_PERSONS);
		ldapFilterGroups = jobDataMap.getString(LdapUtil.LDAP_CONFIG.LDAP_FILTER_GROUPS);
		baseDnGroup = jobDataMap.getString(LdapUtil.LDAP_CONFIG.BASE_DN_FOR_GROUP);
		groupName = jobDataMap.getString(LdapUtil.LDAP_CONFIG.GROUP_NAME);
		HashMap<String,String> ldapFieldsMap = new HashMap<String,String>();
		for (Object key: jobDataMap.keySet()) {
			String skey = (String)key;
			String value = jobDataMap.getString(skey);
			ldapFieldsMap.put(skey, value);
			LOGGER.debug("Key: " + skey + " value: " + value);
		}
		LdapUtil.setLdapMap(ldapFieldsMap);
        long now = new Date().getTime();
        if ((now - attemptTimeStamp) >= LOGINTERVAL) {
        	attemptTimeStamp = now;
        }
        if (now == attemptTimeStamp) {
        	LOGGER.info("Initialized LDAP synchronizer mapping table from quartz-jobs.xml");
        }
	}
	
	/**
	 * Calls the scripts and other logic to synchronize information from
	 * an LDAP directory server with structures in the Genji database.
	 * The default behavior of the script is to load all users in the
	 * LDAP server and synchronize them with the list of Genji users.
	 * 
	 * If the GroovyScriptExecuter returns null or an empty map
	 * the default behavior is executed.
	 */
	public static void synchronizeWithLdap() throws Exception {
		Map<String, Object> returnMap = GroovyScriptExecuter.executeLdapScript(GroovyScriptExecuter.EVENT_HANDLER_LDAP_SYNCHRONIZER_CLASS, 
				                        ApplicationBean.getInstance().getSiteBean(), filter); 
		if (returnMap==null/*|| returnMap.isEmpty()*/) {
			TSiteBean siteBean = ApplicationBean.getInstance().getSiteBean();
			Map<String, TPersonBean> ldapPersons = new HashMap<String, TPersonBean>();
			if (enabledUserSync) {
				LOGGER.info("User sync start");
				Map<String, TPersonBean> ldapDirectPersons = LdapUtil.getAllLdapPersonsPaged(siteBean, filter);
				if (ldapDirectPersons!=null && !ldapDirectPersons.isEmpty()) {
					LdapUtil.synchronizeUserList(ldapDirectPersons);
					ldapPersons.putAll(ldapDirectPersons);
				}
				LOGGER.info("User sync end");
			}
			String baseURL = LdapUtil.getBaseURL(siteBean.getLdapServerURL());
			if (enabledGroupSync) {
				LOGGER.info("Group sync start");
				Map<String, List<String>> groupToMemberReferencesMap = new HashMap<String, List<String>>();
				Map<String, TPersonBean> ldapGroups = LdapUtil.getLdapGroupsPaged(baseURL, siteBean, baseDnGroup, ldapFilterGroups, groupName, groupToMemberReferencesMap);
				Map<String, List<TPersonBean>> ldapGroupsToPersons = LdapUtil.getGroupToPersonMaps(baseURL, siteBean, ldapGroups, groupToMemberReferencesMap);
				Map<String, TPersonBean> ldapPersonsFromGroups = GroupMemberBL.synchronizeUserListWithGroup(ldapGroupsToPersons);
				if (ldapPersonsFromGroups!=null) {
					ldapPersons.putAll(ldapPersonsFromGroups);
				}
				LOGGER.info("Group sync end");
			}
			if (deactivateUnknown && (enabledUserSync || enabledGroupSync)) {
				LdapUtil.deactivateUsers(ldapPersons);
			}
		}
	}
	
}
