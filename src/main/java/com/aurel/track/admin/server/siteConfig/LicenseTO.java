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
import java.util.List;

import com.trackplus.license.LicensedFeature;

/**
 * A transfer object bean to be used for license tab
 * @author  Adrian Bojani
 */
public class LicenseTO implements Serializable {

	private static final long serialVersionUID = 400L;
	
	/**
	 * 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "license.systemVersion". The base name builds the first part, the second
	 * part is the specific field.
	 *
	 */
	public interface JSONFIELDS {
		//for localizing the fields by errors
		String tabLicense = "tab.license";		
		String fsLicenseData = "fsLicenseData";
		String licenseContainer = "licenseContainer";
		
		// the base name
		String license = "license."; 
		
		//fields
		String systemVersion = license + "systemVersion";
		String dbaseVersion = license + "dbaseVersion";
		String ipNumber = license + "ipNumber";
		String licenseExtension = license + "licenseExtension";
		String expDateDisplay = license + "expDateDisplay";
		String numberOfUsers = license + "numberOfUsers";
		String numberOfFullUsers = license + "numberOfFullUsers";
		String numberOfEasyUsers = license + "numberOfEasyUsers";
		String numberOfUserFeatures = license + "userFeatures";
		String licenseHolder = license + "licenseHolder";
	}
	
	private String systemVersion;
	private String dbaseVersion;
	private String ipNumber;
	private String licenseExtension;
	private String expDateDisplay;
	private Integer numberOfUsers;
	private List<LicensedFeature> licensedFeatures;
	private Integer numberOfFullUsers;
	private Integer numberOfLimitedUsers;
	private String licenseHolder;

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getDbaseVersion() {
		return dbaseVersion;
	}

	public void setDbaseVersion(String dbaseVersion) {
		this.dbaseVersion = dbaseVersion;
	}

	public String getLicenseExtension() {
		return licenseExtension;
	}

	public void setLicenseExtension(String licenseExtension) {
		this.licenseExtension = licenseExtension;
	}

	public String getExpDateDisplay() {
		return expDateDisplay;
	}

	public void setExpDateDisplay(String expDateDisplay) {
		this.expDateDisplay = expDateDisplay;
	}

	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public Integer getNumberOfFullUsers() {
		return numberOfFullUsers;
	}

	public void setNumberOfFullUsers(Integer numberOfFullUsers) {
		this.numberOfFullUsers = numberOfFullUsers;
	}

	public Integer getNumberOfLimitedUsers() {
		return numberOfLimitedUsers;
	}

	public void setNumberOfLimitedUsers(Integer numberOfLimitedUsers) {
		this.numberOfLimitedUsers = numberOfLimitedUsers;
	}

	public List<LicensedFeature> getLicensedFeatures() {
		return licensedFeatures;
	}

	public void setLicensedFeatures(List<LicensedFeature> licensedFeatures) {
		this.licensedFeatures = licensedFeatures;
	}

	public String getIpNumber() {
		return ipNumber;
	}

	public void setIpNumber(String ipNumber) {
		this.ipNumber = ipNumber;
	}
	
	public void setLicenseHolder(String licenseHolder) {
		this.licenseHolder = licenseHolder;
	}
	
	public String getLicenseHolder() {
		return this.licenseHolder;
	}

}
