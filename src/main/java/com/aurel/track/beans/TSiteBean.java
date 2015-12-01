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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TSiteBean
	extends com.aurel.track.beans.base.BaseTSiteBean
	implements Serializable{
	private static final long serialVersionUID = 360L;

		public interface COLUMNIDENTIFIERS {
			int TRACKVERSION = 1;
			int DBVERSION = 2;
			int LICENSEKEY = 3;
			int EXPDATE = 4;
			int NUMBEROFUSERS = 5;
			int TRACKEMAIL = 6;
			int SMTPSERVERNAME = 7;
			int SMTPPORT = 8;
			int MAILENCODING = 9;
			int SMTPUSER = 10;
			int SMTPPASSWORD = 11;
			int POPSERVERNAME = 12;
			int POPPORT = 13;
			int POPUSER = 14;
			int POPPASSWORD = 15;
			int RECEIVINGPROTOCOL = 16;
			int ALLOWEDEMAILPATTERN = 17;
			int ISLDAPON = 18;
			int LDAPSERVERURL = 19;
			int LDAPATTRIBUTELOGINNAME = 20;
			int ATTACHMENTROOTDIR = 21;
			int SERVERURL = 22;
			int DESCRIPTIONLENGTH = 23;
			int ISSELFREGISTERALLOWED = 24;
			int ISDEMOSITE = 25;
			int USETRACKFROMADDRESS = 26;
			int LDAPBINDDN = 27;
			int LDAPBINDPASSW = 28;
			int PREFERENCES = 29;
			
			int HISTORYENTITY = 50;
			int HISTORYMIGRATIONID = 51;
			
			int USELUCENE = 52;
			int INDEXATTACHMENTS = 53;
			int REINDEXONSTARTUP = 54;
			int ANALYZER = 55;
			
			int LASTSERVERURL = 56;
			int LASTBASEURL = 57;
			
			int DERBY_BACKUP = 58;
			int INSTDATE = 59;
			int BACKUPDIR = 60;
			
		}
	
		private static final Logger LOGGER = LogManager.getLogger(TSiteBean.class);
		
		protected String guiDateFormat = null;

		// the properties currently known
		private final String WS_ON = "wson";
		public final static String DATBASE_BACKUP_ON = "databaseBackupon";
		
		private static final String ES_ON = "eson";// email submission on
		private static final String US_ON = "uson";// unknown sender on
		private static final String US_REG_ON = "usregon"; //unknown sender automatic registration as client on
		private static final String DEF_PROJ = "defproj"; // default project	
		private static final String MAX_ATTACHMENT_SIZE_NAME = "maxAttachSize";
		private static final String DERBY_BACKUP = "derbybackup";
		private static final String CBA_ON = "cbaon";
		private static final String FORCE_LDAP = "fldap";
		private static final String VRM_ON = "vrmon";
		public static final String KEEP_MESSAGES_ON_SERVER = "keepMessagesOnServer";
		private static final double DEFAULT_MAX_ATTACHMENT_SIZE = 10.0;

		public static final String AUTOMATIC_GUEST_LOGIN = "automaticGuestLogin";
		//private static final String NUMBER_OF_LIMITED_USERS = "numlim";
		
		public static final String EMAIL_PERSONAL_NAME = "emailPersonalName";
		public static final String SMTP_REQUIRES_AUTHENTICATION = "smtpReqAuth";
		public static final String SMTP_AUTHENTICATION_MODE = "smtpAuthMode";
		public static final String SMTP_SECURITY_CONNECTION = "smtpSecurityConnection";
		
		
		public static final String MAIL_RECEIVING_SECURITY_CONNECTION = "mailReceivingSecurityConnection";
		
		public static final String HISTORY_ENTITY = "historyEntity";
		
		public static final Integer HISTORY_STATUSCHANGE = new Integer(1);
		public static final Integer HISTORY_BASELINECHANGE = new Integer(2);
		public static final Integer HISTORY_TRAIL = new Integer(3);
		public static final Integer HISTORY_DONE = new Integer(4);
		
		public static final String HISTORY_MIGRATION_ID = "historyMigrationID";
		
		public static final String LASTSERVERURL = "lastServerURL";
		public static final String LASTBASEURL = "lastBaseURL";
		public static final String INSTDATE = "iDt";
		
		public static final String BACKUPDIR = "bupdir";
		public static final String INCLUDEATTACH="incatt";
		public static final String BACKUPONDAYS="bupdays";
		public static final String BACKUPTIME="buptime";
		public static final String NOOFBACKUPS="noofbups";
		//running or maintenance
		private static final String OPERATION_STATE = "opState";
		//maintenance message for users
		private static final String USER_MESSAGE = "userMsg";
		//whether the user message should be shown to the users
		private static final String USER_MESSAGE_ACTIV = "userMsgActiv";
		
		public static final String FSROOT="fsroot";
		//project specific issue IDs 
		public static final String PROJECT_SPECIFIC_IDS_ON="projectSpecificIDsOn";
		
		/**
		 * whether the parent item's planned value (bottom up) 
		 * is not modifiable but calculated from to the sum of descendant issues
		 * and and start and end date (bottom up) is not modifiable but calculated as
		 * the earliest start date and latest end date of the descendant issues  
		 */
		public static final String SUMMARY_ITEMS_BEHAVIOR="summaryItemsBehavior";

		/**
		 * Whether the budget field is visible anywhere in the application
		 */
		public static final String BUDGET_ACTIVE = "budgetActive";
		public static final String SHOW_BASELINE = "showBaseline";
		public static final String SHOW_BOTH_GANTT = "showBoth";
		public static final String VALIDATE_RELATIONSHIPS_GANTT = "validateRelationships";
		public static final String HIGHLIGHT_CRITICAL_PATH_GANTT = "highlightCriticalPath";
		
		
		public interface SMTP_AUTHENTICATION_MODES {
			public final int CONNECT_USING_SMTP_SETTINGS = 1;
			public final int CONNECT_WITH_SAME_SETTINGS_AS_INCOMING_MAIL_SERVER = 2;
			public final int CONNECT_TO_INCOMING_MAIL_SERVER_BEFORE_SENDING = 3;
		}
		public interface SECURITY_CONNECTIONS_MODES {
			public final int NEVER = 0;
			public final int TLS_IF_AVAILABLE = 1;
			public final int TLS = 2;
			public final int SSL = 3;
		}

		public interface SEND_FROM_MODE {
			public final int USER = 0;
			public final int SYSTEM = 1;
		}
		
		private int numberOfFullUsers = 5;
		private int numberOfLimitedUsers = 0;
		private int numberOfGanttUsers = 0;
		private String licenseHolder = null;
		

		public void setDbaseVersion(String dbv) {
			this.setDbVersion(dbv);
		}
		
		public String getDbaseVersion() {
			return this.getDbVersion();
		}
		
		public String getExpDateDisplay() {
			return DateTimeUtils.getInstance().formatISODateTime(this.getExpDate());
		}
		
		public void setIsSelfRegisterAllowedBool(boolean allowed){
			this.setIsSelfRegisterAllowed(translateBooleanToYn(allowed));
		}
		
		public Boolean getIsSelfRegisterAllowedBool(){
			return (translateYnToBoolean(this.getIsSelfRegisterAllowed()));
		}
		
		public Boolean getIsLDAPOnBool(){
			return (translateYnToBoolean(this.getIsLDAPOn()));
		}
		
		public void setIsLDAPOnBool(Boolean on){
			this.setIsLDAPOn(translateBooleanToYn(on));
		}
		
		
		public void setIsDemoSiteBool(Boolean allowed){
			this.setIsDemoSite(translateBooleanToYn(allowed));
		}
		
		public Boolean getIsDemoSiteBool(){
			return translateYnToBoolean(this.getIsDemoSite());
		}
		
		/**
		 * Method to turn permit container based authentication
		 * @param yesNo
		 */
		public void setIsCbaAllowed(Boolean yesNo) {
			setPreferenceProperty(CBA_ON, yesNo); 
		}

		/**
		 * Method to check for container based authentication
		 */
		public Boolean getIsForceLdap() {
			return getBooleanPreferenceProperty(FORCE_LDAP);
		}
		
		/**
		 * Method to turn permit container based authentication
		 * @param yesNo
		 */
		public void setIsForceLdap(Boolean yesNo) {
			setPreferenceProperty(FORCE_LDAP, yesNo); 
		}

		/**
		 * Method to check for container based authentication
		 */
		public Boolean getIsCbaAllowed() {
			return getBooleanPreferenceProperty(CBA_ON);
		}
		
		/**
		 * Method to turn permit container based authentication
		 * @param yesNo
		 */
		public void setIsVersionReminderOn(Boolean yesNo) {
				setPreferenceProperty(VRM_ON, yesNo); 
		}

		/**
		 * Method to check for container based authentication
		 */
		public Boolean getIsVersionReminderOn() {
			return getBooleanPreferenceProperty(VRM_ON);
		}
		
		
		public void setUseTrackFromAddressDisplay(Integer yes){
			if (yes != null && yes == 1) {
				this.setUseTrackFromAddress("1");
			}
			else {
				this.setUseTrackFromAddress("0");
			}
		}
		
		public Integer getUseTrackFromAddressDisplay(){
			if ("1".equals(this.getUseTrackFromAddress())
				|| "Y".equals(this.getUseTrackFromAddress())) {
				return new Integer(1);
			}
			else {
				return new Integer(0);
			}
		}
		
		/**
		 * Method to turn the web service (Axis) on or off
		 * @param yesNo
		 */
		public void setIsWSOn(Boolean yesNo) {
			setPreferenceProperty(WS_ON, yesNo); 
		}

		/**
		 * Method to check if the web service (Axis) is on or off
		 */
		public Boolean getIsWSOn() {
			return getBooleanPreferenceProperty(WS_ON);
		}
		
		/**
		 * Method to turn the database backup job on or off
		 * @param yesNo
		 */
		public void setIsDatabaseBackupJobOn(Boolean yesNo) {
			setPreferenceProperty(DATBASE_BACKUP_ON, yesNo); 
		}

		/**
		 * Method to check if database backup job is on or off
		 */
		public Boolean getIsDatabaseBackupJobOn() {
			return getBooleanPreferenceProperty(DATBASE_BACKUP_ON);
		}

		/**
		 * Method to turn the project specific itemIDs on or off
		 * @param yesNo
		 */
		public void setProjectSpecificIDsOn(Boolean yesNo) {
			setPreferenceProperty(PROJECT_SPECIFIC_IDS_ON, yesNo); 
		}

		/**
		 * Method to check if project specific itemIDs are on or off
		 */
		public Boolean getProjectSpecificIDsOn() {
			return getBooleanPreferenceProperty(PROJECT_SPECIFIC_IDS_ON);
		}
		
		/**
		 * Method to turn the summary items behavior on or off
		 * @param trueFalse
		 */
		public void setSummaryItemsBehavior(Boolean trueFalse) {
			setPreferenceProperty(SUMMARY_ITEMS_BEHAVIOR, trueFalse); 
		}

		/**
		 * Method to check if the summary items behavior is on or off
		 */
		public Boolean getSummaryItemsBehavior() {
			return getBooleanPreferenceProperty(SUMMARY_ITEMS_BEHAVIOR);
		}
		
		/**
		 * Method to turn the summary items behavior on or off
		 * @param yesNo
		 */
		public void setBudgetActive(Boolean yesNo) {
			setPreferenceProperty(BUDGET_ACTIVE, yesNo); 
		}

		/**
		 * Method to check if the summary items behavior is on or off
		 */
		public Boolean getBudgetActive() {
			return getBooleanPreferenceProperty(BUDGET_ACTIVE);
		}
		
		/**
		 * Method to turn the summary items behavior on or off
		 * @param yesNo
		 */
		public void setShowBaseline(Boolean yesNo) {			
			setPreferenceProperty(SHOW_BASELINE, yesNo); 
		}

		/**
		 * Method to check if the summary items behavior is on or off
		 */
		public Boolean getShowBaseline() {
			return getBooleanPreferenceProperty(SHOW_BASELINE);
		}
		
		/**
		 * Method to turn the Gantt view property on or off.
		 * If on the start/end, target start/target end is visible.
		 * If off only the start/end date is visible.
		 * @param yesNo
		 */
		public void setShowBothGantt(Boolean yesNo) {
			setPreferenceProperty(SHOW_BOTH_GANTT, yesNo);
		}
		
		/**
		 * Method to check if Gantt view show both property is checked.
		 * @return
		 */
		public Boolean  getShowBothGantt() {
			return getBooleanPreferenceProperty(SHOW_BOTH_GANTT);
		}
		
		/**
		 * Method to turn on/off Gantt view validate relationships properties.
		 * @param yesNo
		 */
		public void setValidateRelationshipsGantt(Boolean yesNo) {
			setPreferenceProperty(VALIDATE_RELATIONSHIPS_GANTT, yesNo);
		}
		
		/**
		 * Method to check if Gantt view validate relationships properties is turned on/off.
		 * @return
		 */
		public Boolean getValidateRelationshipsGantt() {
			return getBooleanPreferenceProperty(VALIDATE_RELATIONSHIPS_GANTT);
		}
		
		/**
		 * Method to turn on/off Gantt view highlight critical path functionality.
		 * @param yesNo
		 */
		public void setHighlightCriticalPathGantt(Boolean yesNo) {
			setPreferenceProperty(HIGHLIGHT_CRITICAL_PATH_GANTT, yesNo);
		}
		
		/**
		 * Method to check if Gantt view highlight critical path functionality is turned on/off.
		 * @return
		 */
		public Boolean getHighlightCriticalPathGantt() {
			return getBooleanPreferenceProperty(HIGHLIGHT_CRITICAL_PATH_GANTT);
		}
		
		/**
		 * Method to turn submission of issues by email is on or off
		 * @param yesNo
		 */
		public void setIsEmailSubmissionOn(Boolean yesNo) {
			setPreferenceProperty(ES_ON, yesNo); 
		}

		/**
		 * Method to check if submission of issues by email is on or off
		 */
		public Boolean getIsEmailSubmissionOn() {
			return getBooleanPreferenceProperty(ES_ON);
		}

		/**
		 * Method to turn submission of issues by unknown senders is on or off
		 * @param yesNo
		 */
		public void setIsUnknownSenderOn(Boolean yesNo) {
			setPreferenceProperty(US_ON, yesNo); 
		}

		/**
		 * Method to check if submission by unknown senders is on or off
		 */
		public Boolean getIsUnknownSenderOn() {		
			return getBooleanPreferenceProperty(US_ON);
		}
		
		
		/**
		 * Method to turn unknown sender automatic registration 
		 * @param yesNo
		 */
		public void setIsUnknownSenderRegistrationOn(Boolean yesNo) {
			setPreferenceProperty(US_REG_ON, yesNo); 
		}

		/**
		 * Method to check if unknown sender automatic registration is ON 
		 */
		public Boolean getIsUnknownSenderRegistrationOn() {		
			return getBooleanPreferenceProperty(US_REG_ON);
		}

		/**
		 * Method to set default project for submission of issues by email.
		 * This is the project submissions will be entered into.
		 * @param defaultProject
		 */
		public void setDefaultProject(Integer defaultProject) {
			if (defaultProject != null) {
			setPreferenceProperty(DEF_PROJ, defaultProject.toString());
			} 
		}

		/**
		 * Method to get the current default project for submission of 
		 * issues by email.
		 */
		public Integer getDefaultProject() {
			String defProj = getPreferenceProperty(DEF_PROJ);
			if (defProj == null || defProj.length() < 1 ) {
				return null;
			}
			else {
				try {
					Integer project = new Integer(defProj);
					return project;
				}
				catch (Exception ie) {
					return null;
				}
			}
		}
		
		/**
		 * Method to set the maximum size for an attachment.
		 * @param maxAttachmentSize
		 */
		public void setMaxAttachmentSize(Double maxAttachmentSize) {
			if (maxAttachmentSize == null || maxAttachmentSize.doubleValue() <= 0.0) {
				maxAttachmentSize = new Double(DEFAULT_MAX_ATTACHMENT_SIZE);
			}
			setPreferenceProperty(MAX_ATTACHMENT_SIZE_NAME, maxAttachmentSize.toString());
		}

		/**
		 * Method to get the current default project for submission of 
		 * issues by email.
		 */
		public Double getMaxAttachmentSize() {
			String strMaxAttachSize = getPreferenceProperty(MAX_ATTACHMENT_SIZE_NAME);
			Double maxAttachSize = new Double(DEFAULT_MAX_ATTACHMENT_SIZE);
			if (strMaxAttachSize != null && strMaxAttachSize.length()>0)
			{
				try {
					maxAttachSize = new Double(strMaxAttachSize);
				}
				catch (Exception e) {
					LOGGER.error("Getting the maximal attachment size failed with " + e.getMessage());				
				}
			}
			return maxAttachSize;
		}
		
		/**
		 * Set the useLucene flag
		 * @param yesNo
		 */
		public void setUseLucene(String yesNo) {
			setPreferenceProperty(LuceneUtil.LUCENESETTINGS.USELUCENE, yesNo); 
		}

		/**
		 * Get the useLucene flag
		 */
		public String getUseLucene() {
			return getPreferenceProperty(LuceneUtil.LUCENESETTINGS.USELUCENE);
		}
		
		/**
		 * Set the indexAttachments flag
		 * @param yesNo
		 */
		public void setIndexAttachments(String yesNo) {
			setPreferenceProperty(LuceneUtil.LUCENESETTINGS.INDEXATTACHMENTS, yesNo); 
		}

		/**
		 * Get the indexAttachments flag
		 */
		public String getIndexAttachments() {
			return getPreferenceProperty(LuceneUtil.LUCENESETTINGS.INDEXATTACHMENTS);
		}
		
		/**
		 * Set the reindexWorkItemsOnStartup flag
		 * @param yesNo
		 */
		public void setReindexOnStartup(String yesNo) {
			setPreferenceProperty(LuceneUtil.LUCENESETTINGS.REINDEXONSTARTUP, yesNo); 
		}

		/**
		 * Get the reindexWorkItemsOnStartup flag
		 */
		public String getReindexOnStartup() {		
			return getPreferenceProperty(LuceneUtil.LUCENESETTINGS.REINDEXONSTARTUP);
		}
		
		/**
		 * Set the derby backup config
		 * @param backupConfig
		 */
		public void setDerbyBackup(String backupConfig) {
			setPreferenceProperty(DERBY_BACKUP, backupConfig); 
		}

		/**
		 * Get the useLucene flag
		 */
		public String getDerbyBackup() {		
			return getPreferenceProperty(DERBY_BACKUP);
		}

		public String getKeepMessagesOnServer(){
			return getPreferenceProperty(KEEP_MESSAGES_ON_SERVER);
		}
		
		public void setKeepMessagesOnServer(String keepMessagesOnServer){
			setPreferenceProperty(KEEP_MESSAGES_ON_SERVER, keepMessagesOnServer);
		}
 
				
		public void setNumberOfFullUsers(int numFull) {
			numberOfFullUsers = numFull;			
		}
		
		public int getNumberOfFullUsers() {
			return numberOfFullUsers;
		}
		
		public void setNumberOfLimitedUsers(int limited) {
			numberOfLimitedUsers = limited;
		}
		
		public int getNumberOfLimitedUsers() {
			return numberOfLimitedUsers;
		}

		public int getNumberOfGanttUsers() {
			return numberOfGanttUsers;
		}

		public void setNumberOfGanttUsers(int numberOfGanttUsers) {
			this.numberOfGanttUsers = numberOfGanttUsers;
		}

		public void setLicenseHolder(String lh) {
			licenseHolder = lh;
		}
		
		public String getLicenseHolder() {
			return licenseHolder;
		}

		public void setAutomaticGuestLogin(Boolean automaticGuestLogin) {
			setPreferenceProperty(AUTOMATIC_GUEST_LOGIN, automaticGuestLogin);
		}
		
		public Boolean getAutomaticGuestLogin() {
			return getBooleanPreferenceProperty(AUTOMATIC_GUEST_LOGIN);
		}
		
		public String getEmailPersonalName() {
			return getPreferenceProperty(EMAIL_PERSONAL_NAME);
		}

		public void setEmailPersonalName(String emailPersonalName) {
			sendFrom.setLastName(emailPersonalName);
			sendFrom.setFirstName("");
			setPreferenceProperty(EMAIL_PERSONAL_NAME, emailPersonalName);
		}

		/**
		 * Get the smtpReqAuth string
		 * Used by rendering the user interface
		 * @return
		 */
		public String getSmtpReqAuth() {
			return getPreferenceProperty(SMTP_REQUIRES_AUTHENTICATION);
		}

		/**
		 * Set the smtpReqAuth string
		 * Used by submitting from user interface
		 * @param smtpReqAuth
		 */
		public void setSmtpReqAuth(String smtpReqAuth) {
			setPreferenceProperty(SMTP_REQUIRES_AUTHENTICATION, smtpReqAuth);
		}

		/**
		 * Get the boolean value for smtpReqAuth
		 * Used by rendering to set the disabled flag for some controls and
		 * by setting the value in the constants
		 * @return
		 */
		public boolean getBoolSmtpReqAuth() {
			String strSmtpReqAuth = getSmtpReqAuth();
			boolean smtpReqAuth;
			if (strSmtpReqAuth==null) {
				//it was not set previously try to guess: if SMTPUser was not set 
				//we suppose that no authentication is needed. Of course it is not necessarily true.
				//If so the email settings should be modified after upgrade
				if (getSmtpUser()==null || getSmtpUser().trim().length()==0) {
					smtpReqAuth = false;
				} else {
					smtpReqAuth = true;
				}
				//preselect the checkbox at the user interface
				setSmtpReqAuth(new Boolean(smtpReqAuth).toString());
			} else {
				try {
					smtpReqAuth = new Boolean(strSmtpReqAuth).booleanValue();
				} catch (Exception e) {
					LOGGER.warn("Getting the boolean value for smtpReqAuth " + strSmtpReqAuth + " failed with " + e.getMessage());
					smtpReqAuth = true;
				}
			}
			return smtpReqAuth;
		}
		
		/**
		 * Get the smtpAuthMode string
		 * Used by rendering the user interface 
		 * @return
		 */
		public String getSmtpAuthMode() {
			return getPreferenceProperty(SMTP_AUTHENTICATION_MODE);
		}

		/**
		 * Set the smtpAuthMode string
		 * Used by submitting from user interface
		 * @param smtpAuthMode
		 */
		public void setSmtpAuthMode(String smtpAuthMode) {
			setPreferenceProperty(SMTP_AUTHENTICATION_MODE, smtpAuthMode);
		}
		
		public Integer getSmtpSecurityConnection(){
			String smtpSecurityConnectionStr= getPreferenceProperty(SMTP_SECURITY_CONNECTION);
			int smtpSecurityConn=SECURITY_CONNECTIONS_MODES.NEVER;
			if(smtpSecurityConnectionStr!=null){
				try{
					smtpSecurityConn=Integer.parseInt(smtpSecurityConnectionStr);
				}catch (Exception e) {
					// ignore, use default value
				}
			}
			return Integer.valueOf(smtpSecurityConn);
		}
		
		public void setSmtpSecurityConnection(Integer smtpSecurityConnection){
			if(smtpSecurityConnection==null){
				smtpSecurityConnection=Integer.valueOf(SECURITY_CONNECTIONS_MODES.NEVER);
			}
			setPreferenceProperty(SMTP_SECURITY_CONNECTION,smtpSecurityConnection.toString());
		}
		
		public Integer getMailReceivingSecurityConnection(){
			String mailReceivingSecurityConnectionStr= getPreferenceProperty(MAIL_RECEIVING_SECURITY_CONNECTION);
			int mailReceivingSecurityConn=SECURITY_CONNECTIONS_MODES.NEVER;
			if(mailReceivingSecurityConnectionStr!=null){
				try{
					mailReceivingSecurityConn=Integer.parseInt(mailReceivingSecurityConnectionStr);
				}catch (Exception e) {
					// ignore, use default value
				}
			}
			return Integer.valueOf(mailReceivingSecurityConn);
		}
		
		public void setMailReceivingSecurityConnection(Integer mailReceivingSecurityConnection){
			if(mailReceivingSecurityConnection==null){
				mailReceivingSecurityConnection=Integer.valueOf(SECURITY_CONNECTIONS_MODES.NEVER);
			}
			setPreferenceProperty(MAIL_RECEIVING_SECURITY_CONNECTION,mailReceivingSecurityConnection.toString());
		}
		
		
		/**
		 * Get the integer value for smtpAuthMode
		 * Used by rendering to set the disabled flag for some controls and
		 * by setting the value in the constants
		 * @return
		 */
		public int getIntSmtpAuthMode() {
			int smtpAuthMode;
			String strSmtpAuthMode = getSmtpAuthMode();
			if (strSmtpAuthMode==null) {
				//it was not set previously try to guess according to the SMTPUser
				if (getSmtpUser()==null || getSmtpUser().trim().length()==0) {
					smtpAuthMode = TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_WITH_SAME_SETTINGS_AS_INCOMING_MAIL_SERVER;
				} else {
					smtpAuthMode = TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS;
				}
				//preselect the radio button at the user interface
				setSmtpAuthMode(new Integer(smtpAuthMode).toString());
			} else {
				try {
					smtpAuthMode = Integer.parseInt(strSmtpAuthMode);
				} catch (Exception e) {
					LOGGER.warn("Getting the int value for strSmtpAuthMode " + strSmtpAuthMode + " failed with " + e.getMessage());
					smtpAuthMode = TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS;
				}			
			}
			return smtpAuthMode;
		}
		
		/**
		 * Get the historyMigrationID string 
		 * @return
		 */
		public String getHistoryMigrationID() {
			return getPreferenceProperty(HISTORY_MIGRATION_ID);
		}

		/**
		 * Set the historyMigrationID string	
		 * @param historyMigrationID
		 */
		public void setHistoryMigrationID(String historyMigrationID) {
			setPreferenceProperty(HISTORY_MIGRATION_ID, historyMigrationID);
		}

		/**
		 * Get the historyMigrationID string 
		 * @return
		 */
		public String getHistoryEntity() {
			return getPreferenceProperty(HISTORY_ENTITY);
		}

		/**
		 * Set the historyMigrationID string	
		 * @param historyEntity
		 */
		public void setHistoryEntity(String historyEntity) {
			setPreferenceProperty(HISTORY_ENTITY, historyEntity);
		}
		
		/**
		 * Set the analyzer flag
		 * @param yesNo
		 */
		public void setAnalyzer(String yesNo) {
			setPreferenceProperty(LuceneUtil.LUCENESETTINGS.ANALYZER, yesNo); 
		}

		/**
		 * Get the analyzer flag
		 */
		public String getAnalyzer() {		
			return getPreferenceProperty(LuceneUtil.LUCENESETTINGS.ANALYZER);
		}

		/**
		 * Set the indexPath flag
		 * @param yesNo
		 */
		public void setIndexPath(String yesNo) {
			setPreferenceProperty(LuceneUtil.LUCENESETTINGS.INDEXPATH, yesNo); 
		}

		/**
		 * Get the indexPath flag
		 * @return the indexPath flag
		 */
		public String getIndexPath() {		
			return getPreferenceProperty(LuceneUtil.LUCENESETTINGS.INDEXPATH);
		}

						
		
		public String getLastServerURL() {
			return getPreferenceProperty(LASTSERVERURL);
		}

		public void setLastServerURL(String lastServerURL) {
			setPreferenceProperty(LASTSERVERURL, lastServerURL); 
		}

		public String getLastBaseURL() {
			return getPreferenceProperty(LASTBASEURL);
		}

		public void setLastBaseURL(String lastBaseURL) {
			setPreferenceProperty(LASTBASEURL, lastBaseURL); 
		}
		
		public String getInstDate() {
			return getPreferenceProperty(INSTDATE);
		}

		public void setInstDate(String iDate) {
			setPreferenceProperty(INSTDATE, iDate); 
		}
		
		public String getBackupDir() {
			return getPreferenceProperty(BACKUPDIR);
		}
		
		public void setBackupDir(String dir) {
			setPreferenceProperty(BACKUPDIR, dir);
		}
		
		public String getOpState() {
			String opState = getPreferenceProperty(OPERATION_STATE);
			if (opState==null) {
				opState = ApplicationBean.OPSTATE_RUNNING;
			}
			return opState;
		}
		
		public void setOpState(String opState) {
			setPreferenceProperty(OPERATION_STATE, opState);
		}
		
		public String getUserMessage() {
			return getPreferenceProperty(USER_MESSAGE);
		}
		
		public void setUserMessage(String userMessage) {
			setPreferenceProperty(USER_MESSAGE, userMessage);
		}
		
		public Boolean getUserMessageActiv() {
			return getBooleanPreferenceProperty(USER_MESSAGE_ACTIV);
		}
		
		public void setUserMessageActiv(Boolean userMessageActiv) {
			setPreferenceProperty(USER_MESSAGE_ACTIV, userMessageActiv);
		}
		
		public Boolean getIncludeAttachments() {
			return getBooleanPreferenceProperty(INCLUDEATTACH);
		}
	
		public void setIncludeAttachments(Boolean inca) {
			setPreferenceProperty(INCLUDEATTACH, inca);
		}
		
		public List<Integer> getBackupOnDays() {
			List<Integer> values = new ArrayList<Integer>();
			String bod = getPreferenceProperty(BACKUPONDAYS);
			if (bod == null || "".equals(bod)) {
				return values;
			}
			for (String value : bod.split(",")) {
				values.add(Integer.valueOf(value));
			}
			return values;
		}
		
		public void setBackupOnDays(List<Integer> days) {
			setPreferenceProperty(BACKUPONDAYS, StringArrayParameterUtils.createStringFromIntegerList(days));
		}
		
		public String getBackupTime() {
			return getPreferenceProperty(BACKUPTIME);
		}
		
		public void setBackupTime(String time) {
			setPreferenceProperty(BACKUPTIME, time);
		}

		public Integer getNoOfBackups() {
			String no = getPreferenceProperty(NOOFBACKUPS);
			if (no == null || "".equals(no) ){
				no = "1";
			}
			return new Integer(no);
		}
		
		public void setNoOfBackups(Integer no) {
			setPreferenceProperty(NOOFBACKUPS, no.toString());
		}
		
		public void setFileShareRoot(String fsroot) {
			setPreferenceProperty(FSROOT, fsroot);
		}
		
		public String getFileShareRoot() {
			return getPreferenceProperty(FSROOT);
		}
		
		/**
		 * Method to get preference properties. This is to easily expand the
		 * set of preferences without having to provide a database attribute
		 * for each. The preferences are stored in the database in regular
		 * Java property format.
		 * @param thePropertyName name of property 
		 * @return the value of the property
		 */
		public String getPreferenceProperty(String thePropertyName) {
			return PropertiesHelper.getProperty(getPreferences(), thePropertyName);
		}
		
		/**
		 * Method to get preference properties. This is to easily expand the
		 * set of preferences without having to provide a database attribute
		 * for each. The preferences are stored in the database in regular
		 * Java property format.
		 * @param thePropertyName name of property 
		 * @return the value of the property
		 */
		public Boolean getBooleanPreferenceProperty(String thePropertyName) {
			String val = PropertiesHelper.getProperty(getPreferences(), thePropertyName);
			if ("Y".equals(val)) {
				return true;
			}
			else {
				return false;
			}
		}
		
		/**
		 * Method to set preference properties. This is to easily expand the
		 * set of preferences without having to provide a database attribute
		 * for each. The preferences are stored in the database in regular
		 * Java property format.
		 * @param thePropertyName name of property
		 * @param thePropertyValue value of property
		 */
		public void setPreferenceProperty(String thePropertyName, String thePropertyValue) {
			if(thePropertyValue==null){
				setPreferences(PropertiesHelper.removeProperty(getPreferences(), thePropertyName));
			}else{
				setPreferences(PropertiesHelper.setProperty(getPreferences(), thePropertyName, thePropertyValue));
			}
		}
		
		/**
		 * Method to set preference properties. This is to easily expand the
		 * set of preferences without having to provide a database attribute
		 * for each. The preferences are stored in the database in regular
		 * Java property format.
		 * @param thePropertyName name of property
		 * @param thePropertyValue value of property
		 */
		public void setPreferenceProperty(String thePropertyName, Boolean thePropertyValue) {
			if(thePropertyValue==null){
				setPreferences(PropertiesHelper.removeProperty(getPreferences(), thePropertyName));
			}else{
				String val = translateBooleanToYn(thePropertyValue);
				setPreferences(PropertiesHelper.setProperty(getPreferences(), thePropertyName, val));
			}
		}
		
		/*
		 * Helper routine to convert to Y/N value in database from boolean value
		 */
		private String translateBooleanToYn(Boolean checkboxValue) {
			String yesNO="N";
			if(checkboxValue){
				yesNO="Y";
			}
			return yesNO;
		}
		
		/*
		 * Helper routine to convert from Y/N value in database to boolean value
		 */
		private Boolean translateYnToBoolean(String yesNoValue) {
			if ("Y".equals(yesNoValue)) {
				return true;
			}
			else {
				return false;
			}
		}

	@Override
	public void setAllowedEmailPattern(String v) {
		super.setAllowedEmailPattern(v);//To change body of overridden methods use File | Settings | File Templates.
	}
	
	@Override 
	public void setTrackEmail(String v) {
		super.setTrackEmail(v);
		sendFrom.setEmail(v);
	}

	private TPersonBean sendFrom = new TPersonBean();
	
	public TPersonBean getSendFrom() {
		if (sendFrom.getEmail() == null) {
			setTrackEmail("trackplus@yourdomain.com");
			
		}
		if (sendFrom.getLastName() == null) {
			setEmailPersonalName("Genji Server");
		}
        return sendFrom; 
    }

}
