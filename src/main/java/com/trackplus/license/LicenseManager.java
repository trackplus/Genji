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


package com.trackplus.license;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.aurel.track.errors.ErrorData;
import com.aurel.track.util.LabelValueBean;


/**
 * $Id:$
 */
public interface LicenseManager {

	/**
	 * Get the URL for the license provider
	 */
	public String getLicenseProviderAddress1();
	
	public String getLicenseProviderAddress2();
	
	/**
	 * Retrieve a list with license errors.
	 * @return the list with license errors, or null if there are none.
	 */
	public List<LabelValueBean> getLicenseErrors();
	
	/**
	 * Returns the error message key.
	 * @return the error message key.
	 */
	public String getErrorMessage();
	
	/**
	 * Get the license error text if any
	 * @return
	 */
	public ErrorData getLicenseErrorText();
	
	/**
	 * Get the reason for the last license error in encoded form
	 * @return
	 */
	public int getErrorCode();
	
	/**
	 * Returns the warning message key.
	 * @return the warning message key.
	 */
	public String getWarningMessage();
	
	/**
	 * Retrieve the list of licensed features.
	 * @param isUserFeature
	 * @return
	 */
	public List<LicensedFeature> getLicensedFeatures(boolean isUserFeature);
	
	/**
	 * Retrieve a single licensed feature.
	 * @return
	 */
	public LicensedFeature getLicensedFeature(String feature);

	/**
	 * Get HTMl string with links to style sheets that need to be included for the licensed features.
	 * @param request
	 * @return
	 */
	public String getFeatureStyles(HttpServletRequest request);
	
	/**
	 * Get HTML string with links to the JavaScript scripts to be included for the licensed features.
	 * @param request
	 * @param debug to include the debug versions of the scripts
	 * @return
	 */
	public String getFeatureScripts(HttpServletRequest request, String debug);
	
	
	/**
	 * Get HTML string with JavaScript global variable definitions.
	 * @param request
	 * @return
	 */
	public String getFeatureGlobalVars(HttpServletRequest request);
	
	/**
	 * Check the permission for some action. The action name is usually a method name.
	 * @param action
	 * @return
	 */
	public boolean getFeatureDetailChecker(String action);
	
	/**
	 * Check the count permission for some action. The action name is usually a method name.
	 * @param action
	 * @return
	 */
	public Integer getFeatureDetailNumberChecker(String action);
	
	/**
	 * Get the URL where we can obtain a license key.
	 * @param locale
	 * @return
	 */
	public String getLicenseUrl(Locale locale);
	
	/**
	 * Set the license key and perform checks. Retrieve any errors with
	 * a call to <code>getErrors()</code>.
	 * @param licenseKey the license key
	 * @return 
	 */
	public void setLicenseKey(String _licenseKey);
	
	/**
	 * Retrieve the expiration date for the licenses in this manager
	 * @return
	 */
	public Date getLicenseExpirationDate();
	
	/**
	 * Retrieves the license owner string.
	 * @return the string with the license owner
	 */
	public String getLicenseOwner();
	
	/**
	 * Get the number of days this license is valid yet.
	 * @return
	 */
	public Integer getDaysValid();

}
