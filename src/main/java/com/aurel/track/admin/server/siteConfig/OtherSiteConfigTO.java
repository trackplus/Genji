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

package com.aurel.track.admin.server.siteConfig;

import java.io.Serializable;

/**
 * Transfer object for other site configurations.
 * @author  Adrian Bojani
 */
public class OtherSiteConfigTO implements Serializable{

	private static final long serialVersionUID = 400L;

	/**
	 * 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "otherSiteConfig.serverURL". The base name builds the first part, the second
	 * part is the specific field.
	 *
	 */
	public interface JSONFIELDS {
		//for localizing the fields by errors
		String tabOtherSiteConfig = "tab.otherSiteConfig";
		String fsDirectories = "fsDirectories";	
		
		// base name
		String otherSiteConfig = "otherSiteConfig."; 
		
		//fields
		String cbaAllowed = otherSiteConfig + "cbaAllowed";
		String attachmentRootDir = otherSiteConfig + "attachmentRootDir";
		String backupDir = otherSiteConfig + "backupDir";
		String maxAttachmentSize = otherSiteConfig + "maxAttachmentSize";
		String serverURL = otherSiteConfig + "serverURL";
		String descriptionLength = otherSiteConfig + "descriptionLength";
		String selfRegisterAllowed = otherSiteConfig + "selfRegisterAllowed";
		String automaticGuestLogin =  otherSiteConfig + "automaticGuestLogin";
		String demoSite = otherSiteConfig + "demoSite";
		String versionReminder = otherSiteConfig + "versionReminder";
		String webserviceEnabled =  otherSiteConfig + "webserviceEnabled";
		String automatedDatabaseBackup = otherSiteConfig + "automatedDatabaseBackup";
		String projectSpecificIDsOn = otherSiteConfig + "projectSpecificIDsOn";
		String summaryItemsBehavior = otherSiteConfig + "summaryItemsBehavior";
		String budgetActive = otherSiteConfig + "budgetActive";
	}
	
	private boolean cbaAllowed;
	private String attachmentRootDir;
	private String backupDir;
	private Double maxAttachmentSize;
	private String serverURL;
	private Integer descriptionLength;
	private boolean selfRegisterAllowed;
	private boolean automaticGuestLogin;
	private boolean demoSite;
	private boolean versionReminder;
	private boolean webserviceEnabled;
	private boolean automatedDatabaseBackup;
	private boolean  projectSpecificIDsOn;
	private boolean  summaryItemsBehavior;
	private boolean  budgetActive;
	private String fileShareRoot;

	public boolean isAutomatedDatabaseBackup() {
		return automatedDatabaseBackup;
	}

	public void setAutomatedDatabaseBackup(boolean automatedDatabaseBackup) {
		this.automatedDatabaseBackup = automatedDatabaseBackup;
	}

	public boolean isCbaAllowed() {
		return cbaAllowed;
	}

	public void setCbaAllowed(boolean cbaAllowed) {
		this.cbaAllowed = cbaAllowed;
	}

	public String getAttachmentRootDir() {
		return attachmentRootDir;
	}

	public void setAttachmentRootDir(String attachmentRootDir) {
		this.attachmentRootDir = attachmentRootDir;
	}
	
	public String getBackupDir() {
		return backupDir;
	}

	public void setBackupDir(String backupDir) {
		this.backupDir = backupDir;
	}

	public Double getMaxAttachmentSize() {
		return maxAttachmentSize;
	}

	public void setMaxAttachmentSize(Double maxAttachmentSize) {
		this.maxAttachmentSize = maxAttachmentSize;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public Integer getDescriptionLength() {
		return descriptionLength;
	}

	public void setDescriptionLength(Integer descriptionLength) {
		this.descriptionLength = descriptionLength;
	}

	public boolean isSelfRegisterAllowed() {
		return selfRegisterAllowed;
	}

	public void setSelfRegisterAllowed(boolean selfRegisterAllowed) {
		this.selfRegisterAllowed = selfRegisterAllowed;
	}

	public boolean isAutomaticGuestLogin() {
		return automaticGuestLogin;
	}

	public void setAutomaticGuestLogin(boolean automaticGuestLogin) {
		this.automaticGuestLogin = automaticGuestLogin;
	}

	public boolean isDemoSite() {
		return demoSite;
	}

	public void setDemoSite(boolean demoSite) {
		this.demoSite = demoSite;
	}

	public boolean isVersionReminder() {
		return versionReminder;
	}

	public void setVersionReminder(boolean versionReminder) {
		this.versionReminder = versionReminder;
	}

	public boolean isWebserviceEnabled() {
		return webserviceEnabled;
	}

	public void setWebserviceEnabled(boolean webserviceEnabled) {
		this.webserviceEnabled = webserviceEnabled;
	}

	public boolean isProjectSpecificIDsOn() {
		return projectSpecificIDsOn;
	}

	public void setProjectSpecificIDsOn(boolean projectSpecificIDsOn) {
		this.projectSpecificIDsOn = projectSpecificIDsOn;
	}
	
	public boolean isSummaryItemsBehavior() {
		return summaryItemsBehavior;
	}

	public void setSummaryItemsBehavior(boolean summaryItemsBehavior) {
		this.summaryItemsBehavior = summaryItemsBehavior;
	}
	
	public boolean isBudgetActive() {
		return budgetActive;
	}

	public void setBudgetActive(boolean budgetActive) {
		this.budgetActive = budgetActive;
	}

	public void setFileShareRoot(String fsroot) {
		this.fileShareRoot = fsroot;
	}
	
	public String getFileShareRoot() {
		return fileShareRoot;
	}
	
}
