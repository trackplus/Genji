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

package com.trackplus.model;

import static javax.persistence.GenerationType.TABLE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TSiteBean;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.StringArrayParameterUtils;



/**
 * The persistent class for the TSITE database table.
 * 
 */
@Entity
@Table(name="TSITE")
@TableGenerator(name="TSITE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SITE, allocationSize = 10)

public class Tsite extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TSiteBean.class);
	
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

	@Transient
	protected String guiDateFormat = null;

	// the properties currently known
	@Transient
	private static final String WS_ON = "wson";
	public final static String DATBASE_BACKUP_ON = "databaseBackupon";
	
	private static final String ES_ON = "eson";// email submission on
	private static final String US_ON = "uson";// unknown sender on
	private static final String DEF_PROJ = "defproj"; // default project	
	private static final String MAX_ATTACHMENT_SIZE_NAME = "maxAttachSize";
	private static final String DERBY_BACKUP = "derbybackup";
	private static final String CBA_ON = "cbaon";
	private static final String FORCE_LDAP = "fldap";
	private static final String VRM_ON = "vrmon";
	public static final String KEEP_MESSAGES_ON_SERVER = "keepMessagesOnServer";
	private static final double DEFAULT_MAX_ATTACHMENT_SIZE = 10.0;

	public static final String AUTOMATIC_GUEST_LOGIN = "automaticGuestLogin";
	//private static final String NUMBER_OF_FULL_USERS = "numfull";
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
	@Transient
	private int numberOfFullUsers = 5;
	@Transient
	private int numberOfLimitedUsers = 0;
	@Transient
	private String licenseHolder = null;


	@Id
    @GeneratedValue(generator="TSITE_GEN", strategy = TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String allowedemailpattern;

	@Lob
	private String attachmentrootdir;

	@Column(length=10)
	private String dbversion;

	private int descriptionlength;

	@Column(length=255)
	private String executable1;

	@Column(length=255)
	private String executable2;

	@Temporal(TemporalType.DATE)
	private Date expdate;

	@Column(length=1)
	private String isdemosite;

	@Column(length=1)
	private String isldapon;

	@Column(length=1)
	private String isselfregisterallowed;

	@Column(length=30)
	private String ldapattributeloginname;

	@Column(length=255)
	private String ldapbinddn;

	@Column(length=20)
	private String ldapbindpassw;

	@Column(length=100)
	private String ldapserverurl;

	@Column(length=255)
	private String licenseext;

	@Column(length=80)
	private String licensekey;

	@Column(length=20)
	private String mailencoding;

	private int numberofusers;

	@Column(length=30)
	private String poppassword;

	private int popport;

	@Column(length=100)
	private String popservername;

	@Column(length=100)
	private String popuser;

	@Lob
	private String preferences;

	@Column(length=6)
	private String receivingprotocol;

	private int reserveduse;

	@Column(length=100)
	private String serverurl;

	@Column(length=30)
	private String smtppassword;

	private int smtpport;

	@Column(length=100)
	private String smtpservername;

	@Column(length=100)
	private String smtpuser;

	@Column(length=36)
	private String tpuuid;

	@Column(length=100)
	private String trackemail;

	@Column(length=10)
	private String trackversion;

	@Column(length=1)
	private String usetrackfromaddress;

	public Tsite() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getAllowedEmailPattern() {
		return this.allowedemailpattern;
	}

	public void setAllowedEmailPattern(String allowedemailpattern) {
		this.allowedemailpattern = allowedemailpattern;
	}

	public String getAttachmentRootDir() {
		return this.attachmentrootdir;
	}

	public void setAttachmentRootDir(String attachmentrootdir) {
		this.attachmentrootdir = attachmentrootdir;
	}

	public String getDbVersion() {
		return this.dbversion;
	}

	public void setDbVersion(String dbversion) {
		this.dbversion = dbversion;
	}

	public int getDescriptionLength() {
		return this.descriptionlength;
	}

	public void setDescriptionLength(int descriptionlength) {
		this.descriptionlength = descriptionlength;
	}

	public String getExecutable1() {
		return this.executable1;
	}

	public void setExecutable1(String executable1) {
		this.executable1 = executable1;
	}

	public String getExecutable2() {
		return this.executable2;
	}

	public void setExecutable2(String executable2) {
		this.executable2 = executable2;
	}

	public Date getExpDate() {
		return this.expdate;
	}

	public void setExpDate(Date expdate) {
		this.expdate = expdate;
	}

	public String getIsDemoSite() {
		return this.isdemosite;
	}

	public void setIsDemoSite(String isdemosite) {
		this.isdemosite = isdemosite;
	}

	public String getIsLDAPOn() {
		return this.isldapon;
	}

	public void setIsLDAPOn(String isldapon) {
		this.isldapon = isldapon;
	}

	public String getIsSelfRegisterAllowed() {
		return this.isselfregisterallowed;
	}

	public void setIsSelfRegisterAllowed(String isselfregisterallowed) {
		this.isselfregisterallowed = isselfregisterallowed;
	}

	public String getLdapAttributeLoginName() {
		return this.ldapattributeloginname;
	}

	public void setLdapAttributeLoginName(String ldapattributeloginname) {
		this.ldapattributeloginname = ldapattributeloginname;
	}

	public String getLdapBindDN() {
		return this.ldapbinddn;
	}

	public void setLdapBindDN(String ldapbinddn) {
		this.ldapbinddn = ldapbinddn;
	}

	public String getLdapBindPassword() {
		return this.ldapbindpassw;
	}

	public void setLdapBindPassword(String ldapbindpassw) {
		this.ldapbindpassw = ldapbindpassw;
	}

	public String getLdapServerURL() {
		return this.ldapserverurl;
	}

	public void setLdapServerURL(String ldapserverurl) {
		this.ldapserverurl = ldapserverurl;
	}

	public String getLicenseExtension() {
		return this.licenseext;
	}

	public void setLicenseExtension(String licenseext) {
		this.licenseext = licenseext;
	}

	public String getLicenseKey() {
		return this.licensekey;
	}

	public void setLicenseKey(String licensekey) {
		this.licensekey = licensekey;
	}

	public String getMailEncoding() {
		return this.mailencoding;
	}

	public void setMailEncoding(String mailencoding) {
		this.mailencoding = mailencoding;
	}

	public int getNumberOfUsers() {
		return this.numberofusers;
	}

	public void setNumberOfUsers(int numberofusers) {
		this.numberofusers = numberofusers;
	}

	public String getMailReceivingPassword() {
		return this.poppassword;
	}

	public void setMailReceivingPassword(String poppassword) {
		this.poppassword = poppassword;
	}

	public int getMailReceivingPort() {
		return this.popport;
	}

	public void setMailReceivingPort(int popport) {
		this.popport = popport;
	}

	public String getMailReceivingServerName() {
		return this.popservername;
	}

	public void setMailReceivingServerName(String popservername) {
		this.popservername = popservername;
	}

	public String getMailReceivingUser() {
		return this.popuser;
	}

	public void setMailReceivingUser(String popuser) {
		this.popuser = popuser;
	}

	public String getPreferences() {
		return this.preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getMailReceivingProtocol() {
		return this.receivingprotocol;
	}

	public void setMailReceivingProtocol(String receivingprotocol) {
		this.receivingprotocol = receivingprotocol;
	}

	public int getReservedUse() {
		return this.reserveduse;
	}

	public void setReservedUse(int reserveduse) {
		this.reserveduse = reserveduse;
	}

	public String getServerURL() {
		return this.serverurl;
	}

	public void setServerURL(String serverurl) {
		this.serverurl = serverurl;
	}

	public String getSmtpPassword() {
		return this.smtppassword;
	}

	public void setSmtpPassword(String smtppassword) {
		this.smtppassword = smtppassword;
	}

	public int getSmtpPort() {
		return this.smtpport;
	}

	public void setSmtpPort(int smtpport) {
		this.smtpport = smtpport;
	}

	public String getSmtpServerName() {
		return this.smtpservername;
	}

	public void setSmtpServerName(String smtpservername) {
		this.smtpservername = smtpservername;
	}

	public String getSmtpUser() {
		return this.smtpuser;
	}

	public void setSmtpUser(String smtpuser) {
		this.smtpuser = smtpuser;
	}	

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public String getTrackEmail() {
		return this.trackemail;
	}

	public void setTrackEmail(String trackemail) {
		this.trackemail = trackemail;
	}

	public String getTrackVersion() {
		return this.trackversion;
	}

	public void setTrackversion(String trackversion) {
		this.trackversion = trackversion;
	}

	public String getUseTrackFromAddress() {
		return this.usetrackfromaddress;
	}

	public void setUseTrackFromAddress(String usetrackfromaddress) {
		this.usetrackfromaddress = usetrackfromaddress;
	}
	

	public void setDbaseVersion(String dbv) {
		this.setDbVersion(dbv);
	}
	
	public String getDbaseVersion() {
		return this.getDbVersion();
	}
	
	public String getExpDateDisplay() {
		return DateTimeUtils.getInstance().formatISODateTime(this.getExpDate());
	}
	
	@Transient
	public void setIsSelfRegisterAllowedBool(boolean allowed){
		this.setIsSelfRegisterAllowed(translateBooleanToYn(allowed));
	}
	
	@Transient
	public Boolean getIsSelfRegisterAllowedBool(){
		return (translateYnToBoolean(this.getIsSelfRegisterAllowed()));
	}
	
	@Transient
	public Boolean getIsLDAPOnBool(){
		return (translateYnToBoolean(this.getIsLDAPOn()));
	}
	
	@Transient
	public void setIsLDAPOnBool(Boolean on){
		this.setIsLDAPOn(translateBooleanToYn(on));
	}
	
	@Transient
	public void setIsDemoSiteBool(Boolean allowed){
		this.setIsDemoSite(translateBooleanToYn(allowed));
	}
	
	@Transient
	public Boolean getIsDemoSiteBool(){
		return translateYnToBoolean(this.getIsDemoSite());
	}
	
	/**
	 * Method to turn permit container based authentication
	 * @param yesNo
	 */
	@Transient
	public void setIsCbaAllowed(Boolean yesNo) {
		setPreferenceProperty(CBA_ON, yesNo); 
	}

	/**
	 * Method to check for container based authentication
	 */
	@Transient
	public Boolean getIsForceLdap() {
		return getBooleanPreferenceProperty(FORCE_LDAP);
	}
	
	/**
	 * Method to turn permit container based authentication
	 * @param yesNo
	 */
	@Transient
	public void setIsForceLdap(Boolean yesNo) {
		setPreferenceProperty(FORCE_LDAP, yesNo); 
	}

	/**
	 * Method to check for container based authentication
	 */
	@Transient
	public Boolean getIsCbaAllowed() {
		return getBooleanPreferenceProperty(CBA_ON);
	}
	
	/**
	 * Method to turn permit container based authentication
	 * @param yesNo
	 */
	@Transient
	public void setIsVersionReminderOn(Boolean yesNo) {
			setPreferenceProperty(VRM_ON, yesNo); 
	}

	/**
	 * Method to check for container based authentication
	 */
	@Transient
	public Boolean getIsVersionReminderOn() {
		return getBooleanPreferenceProperty(VRM_ON);
	}
	
	@Transient
	public void setUseTrackFromAddressDisplay(Integer yes){
		if (yes == 1) {
			this.setUseTrackFromAddress("1");
		}
		else {
			this.setUseTrackFromAddress("0");
		}
	}
	
	@Transient
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
	@Transient
	public void setIsWSOn(Boolean yesNo) {
		setPreferenceProperty(WS_ON, yesNo); 
	}

	/**
	 * Method to check if the web service (Axis) is on or off
	 */
	@Transient
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
				LOGGER.error("Getting the maximal attachment size failed with " + e.getMessage(), e);				
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
				LOGGER.warn("Getting the boolean value for smtpReqAuth " + strSmtpReqAuth + " failed with " + e.getMessage(), e);
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
				LOGGER.warn("Getting the int value for strSmtpAuthMode " + strSmtpAuthMode + " failed with " + e.getMessage(), e);
				smtpAuthMode = TSiteBean.SMTP_AUTHENTICATION_MODES.CONNECT_USING_SMTP_SETTINGS;
			}			
		}
		return smtpAuthMode;
	}
	
	/**
	 * Get the historyMigrationID string 
	 * @return
	 */
	@Transient
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

					
	@Transient
	public String getLastServerURL() {
		return getPreferenceProperty(LASTSERVERURL);
	}

	@Transient
	public void setLastServerURL(String lastServerURL) {
		setPreferenceProperty(LASTSERVERURL, lastServerURL); 
	}

	@Transient
	public String getLastBaseURL() {
		return getPreferenceProperty(LASTBASEURL);
	}

	@Transient
	public void setLastBaseURL(String lastBaseURL) {
		setPreferenceProperty(LASTBASEURL, lastBaseURL); 
	}
	
	@Transient
	public String getInstDate() {
		return getPreferenceProperty(INSTDATE);
	}

	@Transient
	public void setInstDate(String iDate) {
		setPreferenceProperty(INSTDATE, iDate); 
	}
	
	@Transient
	public String getBackupDir() {
		return getPreferenceProperty(BACKUPDIR);
	}
	
	@Transient
	public void setBackupDir(String dir) {
		setPreferenceProperty(BACKUPDIR, dir);
	}
	
	@Transient
	public Boolean getIncludeAttachments() {
		return getBooleanPreferenceProperty(INCLUDEATTACH);
	}
	
	@Transient
	public void setIncludeAttachments(Boolean inca) {
		setPreferenceProperty(INCLUDEATTACH, inca);
	}
	
	@Transient
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
	
	@Transient
	public void setBackupOnDays(List<Integer> days) {
		setPreferenceProperty(BACKUPONDAYS, StringArrayParameterUtils.createStringFromIntegerList(days));
	}
	
	@Transient
	public String getBackupTime() {
		return getPreferenceProperty(BACKUPTIME);
	}
	
	@Transient
	public void setBackupTime(String time) {
		setPreferenceProperty(BACKUPTIME, time);
	}

	@Transient
	public Integer getNoOfBackups() {
		String no = getPreferenceProperty(NOOFBACKUPS);
		if (no == null || "".equals(no) ){
			no = "1";
		}
		return new Integer(no);
	}
	
	@Transient
	public void setNoOfBackups(Integer no) {
		setPreferenceProperty(NOOFBACKUPS, no.toString());
	}
	
	@Transient
	public void setFileShareRoot(String fsroot) {
		setPreferenceProperty(FSROOT, fsroot);
	}
	
	@Transient
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
	@Transient
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
	@Transient
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
	@Transient
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
	@Transient
	private Boolean translateYnToBoolean(String yesNoValue) {
		if ("Y".equals(yesNoValue)) {
			return true;
		}
		else {
			return false;
		}
	}
	

}
