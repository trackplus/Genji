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

package com.aurel.track.admin.user.profile;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.user.avatar.AvatarBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TUserFeatureBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.trackplus.license.LicensedFeature;

/**
 * This class encodes all user profile parameters into suitable JSON objects. We use a flat list of single JSON objects,
 * so that the form load mechanism of ExtJS works directly. The objects are not nested.
 */
public class ProfileJSON{

	/**
	 * 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "incomingEmail.systemVersion". The base name builds the first part, the second
	 * part is the specific field.
	 *
	 */
	public interface JSON_FIELDS {
		String localeChange = "localeChange";
		String emailSent = "emailSent";
		String emailError = "emailError";

		String context = "context";
		//main
		String main = "main";  // base name
		String userNameGrid = "userName";
		String userName = main+'.'+userNameGrid;
		String firstName = main+'.'+"firstName";
		String lastName = main+'.'+"lastName";
		String userEmailGrid = "userEmail";
		String userEmail = main+'.'+userEmailGrid;
		String localeGrid = "locale";
		String locale = main+'.'+localeGrid;
		String locales = main+'.'+"locales";
		String passwd = main+'.'+"passwd";
		String passwd2 = main+'.'+"passwd2";
		String ldapOn = main+'.'+"ldapOn";
		String forceLdap = main+'.'+"forceLdap";
		String ldapUser = main+'.'+"ldapUser";
		
		String adminOrGuest = main+'.'+"adminOrGuest";
		String domainPat = main+'.'+"domainPat";
		String usertz = main+'.'+"userTz";
		String timeZones = main+'.'+"timeZones";
		
		//e-mail
		String remail = "remail";  // base name
		String noEmail = remail+'.'+"noEmail";
		String emailFrequency = remail+'.'+"emailFrequency";
		String emailLead = remail+'.'+"emailLead";
		String remindMeAsOriginator = remail+'.'+"remindMeAsOriginator";
		String remindMeAsManager = remail+'.'+"remindMeAsManager";
		String remindMeAsResponsible = remail+'.'+"remindMeAsResponsible";
		String remindPriorityLevels = remail+'.'+"remindPriorityLevels";
		String remindSeverityLevels = remail+'.'+"remindSeverityLevels";
		String remindPriorityLevel = remail+'.'+"remindPriorityLevel";
		String remindSeverityLevel = remail+'.'+"remindSeverityLevel";
		String prefEmailType = remail+'.'+"prefEmailType";
		String remindMeOnDaysList = remail+'.'+"remindMeOnDaysList";
		String remindMeOnDays = remail+'.'+"remindMeOnDays";
		
		//other
		String other = "other";  // base name
		String phoneGrid = "phone";
		String phone = other+'.'+phoneGrid;
		String iconName = other+'.'+"iconName";
		String iconUrl = other+'.'+"iconUrl";
		String department = "department";
	    String departmentTree = "departmentTree";
		String workingHours = other+'.'+"workingHours";
		String hourlyWage = other+'.'+"hourlyWage";
		String extraHourWage = other+'.'+"extraHourWage";
		String employeeIdGrid = "employeeId";
		String employeeId = other+'.'+employeeIdGrid;
		String lastLoginGrid = "lastLogin";
		String lastLogin = other+'.'+lastLoginGrid;
		String csvSeparator = other+'.'+"csvSeparator";
		String csvEncoding = other+'.'+"csvEncoding";
		String saveAttachments = other+'.'+"saveAttachments";
		String sessionTimeout = other+'.'+"sessionTimeout";
		String designPath = other+'.'+"designPath";
		String designPaths = other+'.'+"designPaths";
		String activateInlineEdit = other+'.'+"activateInlineEdit";  // activate inline edit of issue attributes
		String activateLayout = other+'.'+"activateLayout"; // activate issue overview layout (columns, groups, sorting, etc.
		String homePage = other+'.'+"homePage";
		String userLevel = other+'.'+"userLevel";
		String userLevelList = "userLevelList";
		String readOnly = "readOnly";
		String substitute = other+'.'+"substituteID";
		String substituteList = other+'.'+"substituteList";
		String active = "active";
		String userLevelLabel = "userLevelLabel";
		String activeLabel = "activeLabel";
		String substituteName = "substituteName";
		
		//licensed features
		String featureList = "featureList";
		String featureID = "featureID";
		String featureName = "featureName";
		String maxWithFeature = "maxWithFeature";
		String activeWithFeature = "activeWithFeature";
	}
	
	static String encodeSaveProfileJSON(boolean localeChange,boolean emailSent,String email) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.localeChange, localeChange);
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.emailSent, emailSent);
		JSONUtility.appendStringValue(sb, "email", email);
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true,true);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the available user levels
	 * @return
	 */
	public static String getConfigValues(List<IntegerStringBean> userLevelList, List<LicensedFeature> licensedFeatures, Map<String, Integer> actuallyUsedFeatureMap) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.userLevelList, userLevelList);
		JSONUtility.appendFieldName(sb, JSON_FIELDS.featureList);
		sb.append(":[");
		if (licensedFeatures!=null) {
			for (Iterator<LicensedFeature> iterator = licensedFeatures.iterator(); iterator.hasNext();) {
				LicensedFeature licensedFeature = (LicensedFeature) iterator.next();
				String featureID = licensedFeature.getFeatureId();
				sb.append("{");
				JSONUtility.appendStringValue(sb, "featureName", licensedFeature.getFeatureName());
				JSONUtility.appendIntegerValue(sb, "maxWithFeature", licensedFeature.getNumberOfUsers());
				JSONUtility.appendIntegerValue(sb, "activeWithFeature", actuallyUsedFeatureMap.get(featureID));
				JSONUtility.appendStringValue(sb, "featureID", featureID, true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("],");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true, true);	
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Creates the JSON string list for locale grid rows lists 
	 * @param personBeanList
     * @param userLevelMap
     * @param departmentMap
     * @param languagesMap
     * @param locale
	 * @return
	 */
	public static String encodePersonListJSON(List<TPersonBean> personBeanList, Map<Integer, TPersonBean> personsMap,
			Map<Integer, String> userLevelMap, Map<Integer, String> departmentMap, Map<String, String> languagesMap,
			Map<Integer, List<TUserFeatureBean>> userFeaturesMap, Locale locale) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if (personBeanList!=null) {
			for (Iterator<TPersonBean> iterator = personBeanList.iterator(); iterator.hasNext();) {
				TPersonBean personBean = iterator.next();
				Integer personID = personBean.getObjectID();
				sb.append("{");
				JSONUtility.appendStringValue(sb, JSON_FIELDS.userNameGrid, personBean.getLoginName());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.NAME, personBean.getLabel());
				JSONUtility.appendStringValue(sb, JSON_FIELDS.userEmailGrid, personBean.getEmail());
				JSONUtility.appendStringValue(sb, JSON_FIELDS.localeGrid, languagesMap.get(personBean.getPrefLocale()));
				JSONUtility.appendStringValue(sb, JSON_FIELDS.phoneGrid, personBean.getPhone());
				JSONUtility.appendStringValue(sb, JSON_FIELDS.employeeIdGrid, personBean.getEmployeeID());
				JSONUtility.appendDateTimeValue(sb, JSON_FIELDS.lastLoginGrid, personBean.getLastButOneLogin());
				boolean active = !personBean.isDisabled();
				JSONUtility.appendBooleanValue(sb, JSON_FIELDS.active, active);
				JSONUtility.appendStringValue(sb, JSON_FIELDS.activeLabel, getBooleanLabel(active, locale));
				JSONUtility.appendStringValue(sb, JSON_FIELDS.userLevelLabel, userLevelMap.get(personBean.getUserLevel()));
				JSONUtility.appendStringValue(sb, JSON_FIELDS.department, departmentMap.get(personBean.getDepartmentID()));
				Integer substituteID = personBean.getSubstituteID();
				if (substituteID!=null) {
					TPersonBean substitutePersonBean = personsMap.get(substituteID);
					if (substitutePersonBean!=null) {
						JSONUtility.appendStringValue(sb, JSON_FIELDS.substituteName, substitutePersonBean.getLabel());
					}
				}
				List<TUserFeatureBean> userFeatureBeans = userFeaturesMap.get(personID);
				if (userFeatureBeans!=null) {
					for (TUserFeatureBean userFeatureBean : userFeatureBeans) {
						JSONUtility.appendBooleanValue(sb, userFeatureBean.getFeatureName(), userFeatureBean.isActive());
					}
				}
				JSONUtility.appendIntegerValue(sb, JSONUtility.JSON_FIELDS.ID, personID, true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String getBooleanLabel(boolean boolValue, Locale locale) {
		String activeLabelKey;
		if (boolValue) {
			activeLabelKey = "common.btn.yes";
		} else {
			activeLabelKey = "common.btn.no";
		}
		return LocalizeUtil.getLocalizedTextFromApplicationResources(activeLabelKey, locale);
	}
	
	/**
	 * Build the inner part of the user profile object as JSON object to load the user interface.
	 * 
	 * @param context
	 * @param profileMainTO
	 * @param profileEmailTO
	 * @param profileOtherTO
	 * @param watchlistTO the current session locale
	 * @return string with inner part of JSON object
	 */
	static String buildJSON(Integer context, Integer personID, ProfileMainTO profileMainTO, 
			ProfileEmailTO profileEmailTO, ProfileOtherTO profileOtherTO,
			ProfileWatchlistTO watchlistTO) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("\"" + JSONUtility.JSON_FIELDS.DATA + "\"").append(":{");
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.context, context);
		//tab email
		sb.append(ProfileJSON.encodeEmailTO(profileEmailTO));
		//tab other
		sb.append(ProfileJSON.encodeOtherTO(profileOtherTO, personID));
		//tab main
		sb.append(ProfileJSON.encodeMainTO(profileMainTO));
		//tab watch list
		//sb.append(ProfileJSON.encodeWatchlistTO(watchlistTO, true));
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	
	/**
	 * Method to encode user profile information (tab "main") and parameters as a JSON object. The JSON
	 * field names are equivalent to the constants last part (e.g., filters).
	 * 
	 * @param mainTO the transfer object containing the "main" tab user profile data
	 * @return the JSON object 
	 */
	private static String encodeMainTO(ProfileMainTO mainTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringValue(sb, JSON_FIELDS.userName, mainTO.getUserName());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.firstName, mainTO.getFirstName());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.lastName, mainTO.getLastName());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.userEmail, mainTO.getUserEmail());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.locale, mainTO.getLocale());
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.timeZones, mainTO.getTimeZones());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.usertz, mainTO.getUserTz());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.passwd, mainTO.getPasswd());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.passwd2, mainTO.getPasswd2());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.adminOrGuest, mainTO.getAdminOrGuest());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.domainPat, mainTO.getDomainPat());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.ldapUser, mainTO.getLdapUser());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.ldapOn, mainTO.getLdapOn());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.forceLdap, mainTO.getForceLdap());
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.locales, mainTO.getLocales(), true);
		return sb.toString();
	}

	/**
	 * Method to encode user profile information (tab "email") and parameters as a JSON object. The JSON
	 * field names are equivalent to the constants last part (e.g., filters).
	 *  
	 * @param emailTO the "email" tab transfer object for the user profile to encode as a JSON object
	 * @return the JSON object
	 */
	private static String encodeEmailTO(ProfileEmailTO emailTO){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.noEmail, emailTO.isNoEmail());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.prefEmailType, emailTO.getPrefEmailType());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.remindMeAsOriginator, emailTO.isRemindMeAsOriginator());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.remindMeAsManager, emailTO.isRemindMeAsManager());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.remindMeAsResponsible, emailTO.isRemindMeAsResponsible());
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.remindPriorityLevels, emailTO.getRemindPriorityLevels());
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.remindSeverityLevels, emailTO.getRemindSeverityLevels());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.remindPriorityLevel, emailTO.getRemindPriorityLevel());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.remindSeverityLevel, emailTO.getRemindSeverityLevel());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.emailLead, emailTO.getEmailLead());
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.remindMeOnDaysList, emailTO.getRemindMeOnDaysList());
		JSONUtility.appendIntegerListAsArray(sb, JSON_FIELDS.remindMeOnDays, emailTO.getRemindMeOnDays());
		return sb.toString();
	}
	
	/**
	 * Method to encode user profile information (tab "other") and parameters as a JSON object. The JSON
	 * field names are equivalent to the constants last part (e.g., filters).
	 * 
	 * @param otherTO the "Other" tab transfer object for the user profile to encode as a JSON object
	 * @return the JSON object
	 */
	private static String encodeOtherTO(ProfileOtherTO otherTO, Integer personID){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendDateTimeValue(sb, JSON_FIELDS.lastLogin, otherTO.getLastLogin());
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.departmentTree, JSONUtility.getTreeHierarchyJSON(otherTO.getDepartmentTree()));
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.department, otherTO.getDepartment());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.phone, otherTO.getPhone());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.iconUrl, AvatarBL.getAvatarDownloadURL(personID, null, true));
		JSONUtility.appendStringValue(sb, JSON_FIELDS.employeeId, otherTO.getEmployeeId());
		JSONUtility.appendDoubleValue(sb, JSON_FIELDS.workingHours, otherTO.getWorkingHours());
		JSONUtility.appendDoubleValue(sb, JSON_FIELDS.hourlyWage, otherTO.getHourlyWage());
		JSONUtility.appendDoubleValue(sb, JSON_FIELDS.extraHourWage, otherTO.getExtraHourWage());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.csvEncoding, otherTO.getCsvEncoding());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.csvSeparator, otherTO.getCsvSeparator());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.saveAttachments, otherTO.isSaveAttachments());
		JSONUtility.appendLabelValueBeanList(sb, JSON_FIELDS.designPaths, otherTO.getDesignPaths());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.designPath, otherTO.getDesignPath());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.activateInlineEdit, otherTO.isActivateInlineEdit());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.activateLayout, otherTO.isActivateLayout());
		JSONUtility.appendStringValue(sb, JSON_FIELDS.homePage, otherTO.getHomePage());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.userLevel, otherTO.getUserLevel());
		JSONUtility.appendIntegerStringBeanList(sb, JSON_FIELDS.userLevelList, otherTO.getUserLevelList());
		JSONUtility.appendBooleanValue(sb, JSON_FIELDS.readOnly, otherTO.isReadOnly());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.sessionTimeout, otherTO.getSessionTimeout());
		JSONUtility.appendIntegerValue(sb, JSON_FIELDS.substitute, otherTO.getSubstituteID());
		JSONUtility.appendILabelBeanList(sb, JSON_FIELDS.substituteList, otherTO.getSubstitutePersonsList());
		return sb.toString();
	}

}
