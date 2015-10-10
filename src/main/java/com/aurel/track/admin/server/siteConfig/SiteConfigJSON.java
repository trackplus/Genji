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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.project.ProjectBL;


import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.json.JSONUtility.JSON_FIELDS;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.TreeNode;
import com.trackplus.license.LicensedFeature;

/**
 * This class encodes all server configuration parameters into suitable JSON objects.
 */
public class SiteConfigJSON{

	
	/**
	 * Build the inner part of the server configuration object as JSON object to load the user interface.
	 * 
	 * @param appBean the application bean containing license and version information
	 * @param siteBean the site bean containing server configuration parameters
	 * @param locale the locale for localizing radio group selection values
	 * @return string with inner part of JSON object
	 */
	static String buildJSON(ApplicationBean appBean, TSiteBean siteBean, TPersonBean personBean, Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		
		//tabLicense
		LicenseTO licenseTO=SiteConfigBL.getLicenseTO(siteBean, appBean, locale);
		sb.append(encodeLicenseTO(licenseTO, locale));
		
		
		//outgoing email
		OutgoingEmailTO outgoingEmailTO= OutgoingEmailBL.getOutgoingEmailTO(siteBean, locale);
		sb.append(encodeOutgoingEmailTO(outgoingEmailTO));
		
		//incoming email
		
		//full text search
		FullTextSearchTO fullTextSearchTO=SiteConfigBL.getFullTextSearchTO(siteBean);
		sb.append(encodeFullTextSearchTO(fullTextSearchTO));
		
		//ldap
		LdapTO ldapTO=SiteConfigBL.getLdapTO(siteBean);
		sb.append(encodeLdapTO(ldapTO));
	
		//tabOther
		OtherSiteConfigTO otherSiteConfigTO=SiteConfigBL.getOtherSiteConfigTO(siteBean);
		sb.append(encodeOtherSiteConfigTO(otherSiteConfigTO));
		
		boolean ldapEnabled=(ApplicationBean.getApplicationBean().getEdition()>=2);
		JSONUtility.appendBooleanValue(sb,"ldapEnabled",ldapEnabled, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Build only the License JSON object, which might change when the license key
	 * is changed and saved.
	 * 
	 * @param appBean the application bean containing license and version information
	 * @param siteBean the site bean containing server configuration parameters
	 * @param locale the locale for localizing radio group selection values
	 * @return string with inner part of JSON object
	 */
	static String buildLicenseJSON(ApplicationBean appBean, TSiteBean siteBean, Locale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		JSONUtility.appendFieldName(sb, JSONUtility.JSON_FIELDS.DATA).append(":{");
		
		//tabLicense
		LicenseTO licenseTO=SiteConfigBL.getLicenseTO(siteBean, appBean, locale);
		sb.append(encodeLicenseTO(licenseTO, locale));
		
		boolean ldapEnabled=(ApplicationBean.getApplicationBean().getEdition()>=2);
		JSONUtility.appendBooleanValue(sb,"ldapEnabled",ldapEnabled, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Method to encode all license information and parameters as a JSON object. The JSON
	 * field names are equivalent to the constants last part (e.g., systemVersion).
	 * 
	 * @param licenseTO the transfer object containing the license data
	 * @param locale
	 * @return the JSON object 
	 */
	private static String encodeLicenseTO(LicenseTO licenseTO, Locale locale){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, LicenseTO.JSONFIELDS.systemVersion, licenseTO.getSystemVersion());
		JSONUtility.appendStringValue(sb, LicenseTO.JSONFIELDS.dbaseVersion, licenseTO.getDbaseVersion());
		JSONUtility.appendStringValue(sb, LicenseTO.JSONFIELDS.ipNumber, licenseTO.getIpNumber());
		JSONUtility.appendStringValue(sb, LicenseTO.JSONFIELDS.licenseExtension, licenseTO.getLicenseExtension());
		JSONUtility.appendStringValue(sb, LicenseTO.JSONFIELDS.licenseHolder, licenseTO.getLicenseHolder());
		JSONUtility.appendStringValue(sb, LicenseTO.JSONFIELDS.expDateDisplay, licenseTO.getExpDateDisplay());
		JSONUtility.appendIntegerValue(sb, LicenseTO.JSONFIELDS.numberOfUsers, licenseTO.getNumberOfUsers());
		JSONUtility.appendIntegerValue(sb, LicenseTO.JSONFIELDS.numberOfFullUsers, licenseTO.getNumberOfFullUsers());
		JSONUtility.appendIntegerValue(sb, LicenseTO.JSONFIELDS.numberOfEasyUsers, licenseTO.getNumberOfLimitedUsers());
		JSONUtility.appendFieldName(sb, LicenseTO.JSONFIELDS.numberOfUserFeatures).append(":");
		sb.append("[");
		List<LicensedFeature> licensedFeatures = licenseTO.getLicensedFeatures();
		if (licensedFeatures!=null) {
			for (Iterator<LicensedFeature> iterator = licensedFeatures.iterator(); iterator.hasNext();) {
				LicensedFeature licensedFeature = (LicensedFeature) iterator.next();
				sb.append("{");
				JSONUtility.appendStringValue(sb, JSON_FIELDS.NAME,
						LocalizeUtil.getParametrizedString("admin.server.config.numberOfUsersWithFeature", new Object[] {licensedFeature.getFeatureName()}, locale));
				JSONUtility.appendIntegerValue(sb, JSON_FIELDS.VALUE, licensedFeature.getNumberOfUsers(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("],");
		return sb.toString();
	}

	/**
	 * Method to encode all outgoing e-mail parameters (SMTP) as a JSON object. The JSON
	 * field names are equivalent to the constants last part (e.g., trackEmail).
	 * 
	 * @param outgoingEmailTO the outgoing e-mail (SMTP) transfer object to encode as a JSON object
	 * @return the JSON object
	 */
	private static String encodeOutgoingEmailTO(OutgoingEmailTO outgoingEmailTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, OutgoingEmailTO.JSONFIELDS.trackEmail, outgoingEmailTO.getTrackEmail());
		JSONUtility.appendStringValue(sb, OutgoingEmailTO.JSONFIELDS.emailPersonalName, outgoingEmailTO.getEmailPersonalName());
		JSONUtility.appendIntegerValue(sb, OutgoingEmailTO.JSONFIELDS.sendFromMode, outgoingEmailTO.getSendFromMode());
		JSONUtility.appendStringValue(sb, OutgoingEmailTO.JSONFIELDS.mailEncoding, outgoingEmailTO.getMailEncoding());
		JSONUtility.appendStringValue(sb, OutgoingEmailTO.JSONFIELDS.serverName, outgoingEmailTO.getServerName());
		JSONUtility.appendIntegerValue(sb, OutgoingEmailTO.JSONFIELDS.securityConnection, outgoingEmailTO.getSecurityConnection());
		JSONUtility.appendIntegerValue(sb, OutgoingEmailTO.JSONFIELDS.port, outgoingEmailTO.getPort());
		JSONUtility.appendBooleanValue(sb, OutgoingEmailTO.JSONFIELDS.reqAuth, outgoingEmailTO.isReqAuth());
		JSONUtility.appendIntegerValue(sb, OutgoingEmailTO.JSONFIELDS.authMode, outgoingEmailTO.getAuthMode());
		JSONUtility.appendStringValue(sb, OutgoingEmailTO.JSONFIELDS.user, outgoingEmailTO.getUser());

		JSONUtility.appendIntegerStringBeanList(sb, OutgoingEmailTO.JSONFIELDS.securityConnectionsModes, outgoingEmailTO.getSecurityConnectionsModes());
		JSONUtility.appendIntegerStringBeanList(sb, OutgoingEmailTO.JSONFIELDS.authenticationModes, outgoingEmailTO.getAuthenticationModes());
		JSONUtility.appendIntegerStringBeanList(sb, OutgoingEmailTO.JSONFIELDS.sendFromModes, outgoingEmailTO.getSendFromModes());
		
		return sb.toString();
	}
	
	


	private static String encodeFullTextSearchTO(FullTextSearchTO fullTextSearchTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendBooleanValue(sb, FullTextSearchTO.JSONFIELDS.indexAttachments, fullTextSearchTO.isIndexAttachments());
		JSONUtility.appendBooleanValue(sb, FullTextSearchTO.JSONFIELDS.reindexOnStartup, fullTextSearchTO.isReindexOnStartup());
		JSONUtility.appendStringValue(sb, FullTextSearchTO.JSONFIELDS.analyzer, fullTextSearchTO.getAnalyzer());
		JSONUtility.appendStringValue(sb, FullTextSearchTO.JSONFIELDS.indexPath, fullTextSearchTO.getIndexPath());
		JSONUtility.appendLabelValueBeanList(sb, FullTextSearchTO.JSONFIELDS.analyzers, fullTextSearchTO.getAnalyzers());
		JSONUtility.appendBooleanValue(sb, FullTextSearchTO.JSONFIELDS.useLucene, fullTextSearchTO.isUseLucene());
		return sb.toString();
	}
	
	private static String encodeLdapTO(LdapTO ldapTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, LdapTO.JSONFIELDS.serverURL, ldapTO.getServerURL());
		JSONUtility.appendStringValue(sb, LdapTO.JSONFIELDS.attributeLoginName, ldapTO.getAttributeLoginName());
		JSONUtility.appendStringValue(sb, LdapTO.JSONFIELDS.bindDN, ldapTO.getBindDN());
		JSONUtility.appendBooleanValue(sb, LdapTO.JSONFIELDS.forceLdap, ldapTO.isForce());
		JSONUtility.appendBooleanValue(sb, LdapTO.JSONFIELDS.enabled, ldapTO.isEnabled());
		return sb.toString();
	}
	
	private static String encodeOtherSiteConfigTO(OtherSiteConfigTO otherSiteConfigTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.cbaAllowed, otherSiteConfigTO.isCbaAllowed());
		JSONUtility.appendStringValue(sb, OtherSiteConfigTO.JSONFIELDS.attachmentRootDir, otherSiteConfigTO.getAttachmentRootDir());
		JSONUtility.appendStringValue(sb, OtherSiteConfigTO.JSONFIELDS.backupDir, otherSiteConfigTO.getBackupDir());
		JSONUtility.appendDoubleValue(sb, OtherSiteConfigTO.JSONFIELDS.maxAttachmentSize, otherSiteConfigTO.getMaxAttachmentSize());
		JSONUtility.appendStringValue(sb, OtherSiteConfigTO.JSONFIELDS.serverURL, otherSiteConfigTO.getServerURL());
		JSONUtility.appendIntegerValue(sb, OtherSiteConfigTO.JSONFIELDS.descriptionLength, otherSiteConfigTO.getDescriptionLength());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.selfRegisterAllowed, otherSiteConfigTO.isSelfRegisterAllowed());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.automaticGuestLogin, otherSiteConfigTO.isAutomaticGuestLogin());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.demoSite, otherSiteConfigTO.isDemoSite());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.versionReminder, otherSiteConfigTO.isVersionReminder());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.webserviceEnabled, otherSiteConfigTO.isWebserviceEnabled());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.projectSpecificIDsOn, otherSiteConfigTO.isProjectSpecificIDsOn());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.summaryItemsBehavior, otherSiteConfigTO.isSummaryItemsBehavior());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.budgetActive, otherSiteConfigTO.isBudgetActive());
		JSONUtility.appendBooleanValue(sb, OtherSiteConfigTO.JSONFIELDS.automatedDatabaseBackup, otherSiteConfigTO.isAutomatedDatabaseBackup());
		return sb.toString();
	}	
	
	/**
	 * Method to create a JSON response object in case that there have been problems
	 * found in validation process.
	 * @param list
	 * @return
	 */	
	public static String encodeJSONSiteConfigErrorList(List<ControlError> list, Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.SUCCESS, false);
		JSONUtility.appendFieldName(sb,JSONUtility.JSON_FIELDS.ERRORS).append(":");
		sb.append("[");
		if(list!=null){
			for (Iterator<ControlError> iterator = list.iterator(); iterator.hasNext();) {
				ControlError siteConfigError = iterator.next();
				sb.append("{");
				JSONUtility.appendStringList(sb, "controlPath", siteConfigError.getControlPath());
				JSONUtility.appendStringValue(sb, JSON_FIELDS.ERROR_MESSAGE, siteConfigError.getErrorMessage(), true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("],");
		JSONUtility.appendFieldName(sb,JSONUtility.JSON_FIELDS.DATA).append(":{");
		LicenseTO licenseTO=SiteConfigBL.getLicenseTO(ApplicationBean.getApplicationBean().getSiteBean(), ApplicationBean.getApplicationBean(), locale);
		sb.append(encodeLicenseTO(licenseTO, locale));
		boolean ldapEnabled=(ApplicationBean.getApplicationBean().getEdition()>=2);
		JSONUtility.appendBooleanValue(sb,"ldapEnabled",ldapEnabled, true);
		sb.append("}}");
		return sb.toString();
	}
}
