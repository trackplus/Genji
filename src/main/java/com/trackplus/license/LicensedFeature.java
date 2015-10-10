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

package com.trackplus.license;

import java.util.Date;

/**
 * A licensed feature
 * @author Joerg Friedrich
 *
 */
public class LicensedFeature {
	
	private String featureId;
	private String featureName;
	private Integer numberOfUsers;
	private Integer numberOfConcurrentUsers;
	private Date expirationDate;
	private Integer minVersion;
	private Integer maxVersion;
	
	/**
	 * Constructor
	 * @param fid
	 * @param fname
	 * @param noOfUsers
	 */
	public LicensedFeature(String fid, String fname, Integer noOfUsers) {
	   this.featureId = fid;
	   this.featureName = fname;
	   this.numberOfUsers = noOfUsers;
	}
	
	/**
	 * The feature id, looks like "gantt", "wiki", "alm"
	 * @return
	 */
	public String getFeatureId() {
		return featureId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	/**
	 * The user interface name of the feature, like "Gantt Editor", "Technician", etc.
	 * @return
	 */
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	/**
	 * The number of named user licenses for this feature.
	 * @return
	 */
	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	
	/**
	 * The number of concurrent user licenses for this feature.
	 * @return
	 */
	public Integer getNumberOfConcurrentUsers() {
		return numberOfConcurrentUsers;
	}
	public void setNumberOfConcurrentUsers(Integer numberOfConcurrentUsers) {
		this.numberOfConcurrentUsers = numberOfConcurrentUsers;
	}
	
	/**
	 * The expiration date for this feature license. Note that usually the entire 
	 * license package has an expiration date, not a single feature.
	 * @return
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

}
