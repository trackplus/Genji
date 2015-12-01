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


package com.aurel.track.admin.user.profile;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;
import org.apache.struts2.ServletActionContext;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.MenuitemFilterBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.admin.user.group.GroupConfigBL;
import com.aurel.track.admin.user.group.PersonInGroupTokens;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.person.PersonConfigBL;
import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TUserLevelBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuJSON;
import com.aurel.track.itemNavigator.filterInMenu.FilterInMenuTO;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LdapUtil;
import com.aurel.track.util.LocaleHandler;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.emailHandling.MailSender;
import com.aurel.track.util.event.IEventSubscriber;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Business logic class for user profile management. It manages the user name, password, e-mail,
 * preferred e-mail reminder settings, etc.
 *
 * @author Joerg Friedrich
 *
 */
public class ProfileBL {
	private static final Logger LOGGER = LogManager.getLogger(ProfileBL.class);

	public static interface EMAIL_TYPE {
		static String HTML = "HTML";
		static String PLAIN = "Plain";
	}

	/**
	 * This is the <code>load</code> action. It prints a suitable
	 * JSON object directly into the response stream. Previously the
	 * <prepare> and <execute> methods have run.
	 *
	 * @return usually nothing, response is directly printed into the response stream. Can also
	 * throw back to logon page in case of insufficient authorization.
	 */
	static String load(TPersonBean currentUser, Integer personID, Integer context, Locale locale) {
		LOGGER.debug("Processing load for context " + context);
		TPersonBean personBean = ProfileBL.getPersonByContext(currentUser, personID, context);
		ProfileMainTO profileMainTO=getProfileMainTO(personBean, context);
		ProfileEmailTO profileEmailTO=getProfileEmailTO(personBean, locale);
		ProfileOtherTO profileOtherTO=getProfileOtherTO(personBean, currentUser, locale);
		ProfileWatchlistTO watchlistTO=getProfileWatchlistTO();
		return ProfileJSON.buildJSON(context, personBean.getObjectID(), profileMainTO, profileEmailTO, profileOtherTO, watchlistTO);
	}

	/**
	 * Gets the personBean by context
	 * @param currentUser
	 * @param personID
	 * @param context
	 * @return
	 */
	static TPersonBean getPersonByContext(TPersonBean currentUser, Integer personID, int context) {
		switch (context) {
		case ProfileMainTO.CONTEXT.USERADMINEDIT:
			if (personID!=null) {
				TPersonBean personBean = PersonBL.loadByPrimaryKey(personID);
				if (personBean!=null) {
					return personBean;
				}
			}
			return new TPersonBean();
		case ProfileMainTO.CONTEXT.PROFILEEDIT:
			return currentUser;
		default:
			//ProfileMainTO.CONTEXT.SELFREGISTRATION or ProfileMainTO.CONTEXT.USERADMINADD
			TPersonBean pb = new TPersonBean();
			pb.setPrefLocale(Locale.getDefault().getLanguage());
			pb.setPrefEmailType("HTML");
			pb.setHoursPerWorkDay(AccountingBL.DEFAULTHOURSPERWORKINGDAY);
			pb.setPrintItemEditable(true);
			pb.setEnableQueryLayout(true);
			pb.setCsvCharacter(",");
			pb.setCsvEncoding("UTF-8");
			pb.setEnableQueryLayout(true);
			pb.setEmailLead(7);
			ArrayList<Integer> days = new ArrayList<Integer>();
			days.add(2); days.add(3); days.add(4); days.add(5); days.add(6);
			pb.setReminderDays(days);
			pb.setRemindMeAsManagerBool(true);
			pb.setRemindMeAsResponsibleBool(true);
			return pb;
		}
	}

	/**
	 * This method gets all data relevant for the main tab of the user
	 * profile management.
	 * @param personBean contains the users data from the database, unless this is a new user
	 * @param context if currently a new user is registering himself, contrary to an administrator
	 * registering a new user. In self registration mode, the other tabs are deactivated
	 * @return a transfer object for the main user profile data
	 */
	private static ProfileMainTO getProfileMainTO(TPersonBean personBean, Integer context){
		ProfileMainTO mainTO=new ProfileMainTO();
		mainTO.setUserName(personBean.getLoginName());
		mainTO.setFirstName(personBean.getFirstName());
		mainTO.setLastName(personBean.getLastName());
		mainTO.setUserEmail(personBean.getEmail());
		mainTO.setLdapOn(ApplicationBean.getInstance().getSiteBean().getIsLDAPOnBool());
		mainTO.setForceLdap(ApplicationBean.getInstance().getSiteBean().getIsForceLdap());
		if (context==ProfileMainTO.CONTEXT.USERADMINADD || context==ProfileMainTO.CONTEXT.SELFREGISTRATION) {
			mainTO.setLdapUser(mainTO.getLdapOn()!=null && mainTO.getLdapOn().booleanValue() && mainTO.getForceLdap()!=null &&  mainTO.getForceLdap().booleanValue());
		} else {
			mainTO.setLdapUser(personBean.isLdapUser());
		}
		if (context==ProfileMainTO.CONTEXT.USERADMINEDIT || context==ProfileMainTO.CONTEXT.USERADMINADD ||
				(context==ProfileMainTO.CONTEXT.PROFILEEDIT && mainTO.getLdapUser()!=null && !mainTO.getLdapUser().booleanValue())) {
			//admin can reset the ldapUser flag by adding new/editing user so set forceLdap to false to enable changing by admin
			//if admin resets the ldapUser flag for a user then the user can authenticate locally and also can change the local password in her profile
			//this should be an exception from the rule of forcing all users to be ldap users in case a user is not (yet) registered in ldap
			mainTO.setForceLdap(false);
		}
		mainTO.setLocales(LocaleHandler.getPossibleLocales());
		mainTO.setLocale(personBean.getPrefLocale());
		mainTO.setUserTz(personBean.getUserTimeZoneId());
		mainTO.setTimeZones(DateTimeUtils.getTimeZones(personBean.getLocale()));
		mainTO.setDomainPat(ApplicationBean.getInstance().getSiteBean().getAllowedEmailPattern());
		mainTO.setAdminOrGuest(TPersonBean.ADMIN_USER.equals(personBean.getUsername()) || TPersonBean.GUEST_USER.equals(personBean.getUsername()));
		return mainTO;
	}

	/**
	 * This method gets all data relevant for the reminder email tab of the user
	 * profile management.
	 *
	 * @param person contains the users data from the database, unless this is a new user
	 * @param locale the current locale
	 * @return a transfer object for the reminder email data
	 */
	private static ProfileEmailTO getProfileEmailTO(TPersonBean person, Locale locale){
		ProfileEmailTO emailTO=new ProfileEmailTO();
		emailTO.setNoEmail(person.getNoEmailPleaseBool());
		String prefEmailType = person.getPrefEmailType();
		if (prefEmailType==null) {
			prefEmailType = EMAIL_TYPE.HTML;
		}
		emailTO.setPrefEmailType(prefEmailType);

		emailTO.setRemindMeAsOriginator(person.getRemindMeAsOriginatorBool());
		emailTO.setRemindMeAsManager(person.getRemindMeAsManagerBool());
		emailTO.setRemindMeAsResponsible(person.getRemindMeAsResponsibleBool());

		List<IntegerStringBean> priorityBeans = PriorityBL.loadWithWarningLevel(locale);
		emailTO.setRemindPriorityLevels(priorityBeans);
		emailTO.setRemindPriorityLevel(person.getEmailRemindPriorityLevel());
		List<IntegerStringBean> severityBeans = SeverityBL.loadWithWarningLevel(locale);
		emailTO.setRemindSeverityLevels(severityBeans);
		emailTO.setRemindSeverityLevel(person.getEmailRemindSeverityLevel());

		emailTO.setEmailLead(person.getEmailLead());
		List<LabelValueBean> daysOfTheWeek = DateTimeUtils.getDaysOfTheWeek(locale);
		emailTO.setRemindMeOnDaysList(daysOfTheWeek);
		emailTO.setRemindMeOnDays(person.getReminderDays());
		return emailTO;
	}

	/**
	 * This method gets all data relevant for the "other" tab of the user
	 * profile management.
	 *
	 * @param personBean contains the users data from the database, unless this is a new user
	 * @param locale contains the current session locale
	 * @return a transfer object for the "other" tab data
	 */
	private static ProfileOtherTO getProfileOtherTO(TPersonBean personBean, TPersonBean currentUser, Locale locale){
		ProfileOtherTO otherTO = new ProfileOtherTO();
		if (personBean.getLastButOneLogin()==null) {
			personBean.setLastButOneLogin(personBean.getLastLogin());
		}
		otherTO.setLastLogin(personBean.getLastButOneLogin());
		Integer departmentID = personBean.getDepartmentID();
		if (departmentID!=null) {
			otherTO.setDepartment(departmentID);
		}
        otherTO.setDepartmentTree(DepartmentBL.getDepartmentNodesEager(null, null, false));
		otherTO.setPhone(personBean.getPhone());
		otherTO.setEmployeeId(personBean.getEmployeeID());
		otherTO.setWorkingHours(personBean.getHoursPerWorkDay());
		otherTO.setHourlyWage(personBean.getHourlyWage());
		otherTO.setExtraHourWage(personBean.getExtraHourWage());
		String csvEncoding = personBean.getCsvEncoding();
		if (csvEncoding==null || csvEncoding.trim().length()==0) {
			//force to UTF-8 if not specified
			csvEncoding = "UTF-8";
		}
		otherTO.setCsvEncoding(csvEncoding);
		otherTO.setCsvSeparator(personBean.getCsvCharacter().toString());
		otherTO.setSaveAttachments(personBean.isAlwaysSaveAttachment());
		otherTO.setDesignPath(personBean.getDesignPath());
		otherTO.setDesignPaths(ApplicationBean.getInstance().getDesigns());
		otherTO.setActivateInlineEdit(personBean.isPrintItemEditable());
		otherTO.setActivateLayout(personBean.isEnableQueryLayout());
		otherTO.setHomePage(personBean.getHomePage());
		otherTO.setUserLevel(personBean.getUserLevel());
		List<IntegerStringBean> userLevelList = new LinkedList<IntegerStringBean>();
		Integer actualUserLevel = null;
		if (currentUser!=null) {
			actualUserLevel = currentUser.getUserLevel();
		}
		if (actualUserLevel==null) {
			actualUserLevel = TPersonBean.USERLEVEL.FULL;
		}
		if (!ApplicationBean.getInstance().isGenji()) {
			userLevelList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.CLIENT, locale), TPersonBean.USERLEVEL.CLIENT));
		}
		if (!TPersonBean.USERLEVEL.CLIENT.equals(actualUserLevel)) {
			userLevelList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.FULL, locale), TPersonBean.USERLEVEL.FULL));
			if (TPersonBean.USERLEVEL.SYSADMIN.equals(actualUserLevel)) {
				//a syadmin can set another person to any level
				userLevelList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSADMIN, locale), TPersonBean.USERLEVEL.SYSADMIN));
				userLevelList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSMAN, locale), TPersonBean.USERLEVEL.SYSMAN));
			} else {
				if (TPersonBean.USERLEVEL.SYSMAN.equals(actualUserLevel)) {
					Integer editedUsersUserLevel = personBean.getUserLevel();
					if (editedUsersUserLevel!=null && TPersonBean.USERLEVEL.SYSADMIN.equals(editedUsersUserLevel)) {
						//a sysman can't set a sys admin but if the person edited is sys admin than it should appear in the list
						otherTO.setReadOnly(true);
						userLevelList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSADMIN, locale), TPersonBean.USERLEVEL.SYSADMIN));
					}
					userLevelList.add(new IntegerStringBean(LocalizeUtil.getLocalizedEntity(new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.SYSMAN, locale), TPersonBean.USERLEVEL.SYSMAN));
				}
			}
			userLevelList.addAll(UserLevelBL.getExtraUserLevelsList(locale));
		}
		otherTO.setUserLevelList(userLevelList);
		otherTO.setSessionTimeout(personBean.getSessionTimeoutMinutes());
		otherTO.setSubstituteID(personBean.getSubstituteID());
		List<Integer> personIDs = null;
		Integer personID = personBean.getObjectID();
		if (personID!=null) {
			personIDs = new LinkedList<Integer>();
			personIDs.add(personID);
		}
		List<TPersonBean> replacementPersonBeans = PersonConfigBL.prepareReplacementPersons(personIDs);
		TPersonBean dummyPersonBean = new TPersonBean();
		dummyPersonBean.setLastName("-");
		replacementPersonBeans.add(0, dummyPersonBean);
		otherTO.setSubstitutePersonsList((List)replacementPersonBeans);
		return otherTO;
	}

	/**
	 * This method gets all data relevant for the "watch list" tab of the user
	 * profile management.
	 *
	 * @param person contains the users data from the database, unless this is a new user
	 * @return a transfer object for the watch list data
	 */
	private static ProfileWatchlistTO getProfileWatchlistTO(){
		ProfileWatchlistTO watchlistTO=new ProfileWatchlistTO();
		return watchlistTO;
	}

	/**
	 * This is the <code>load</code> action. It prints a suitable
	 * JSON object directly into the response stream. Previously the
	 * <prepare> and <execute> methods have run.
	 *
	 * @return usually nothing, response is directly printed into the response stream. Can also
	 * throw back to logon page in case of insufficient authorization.
	 */
	static List<LabelValueBean> validate(TPersonBean personBean, TPersonBean currentUser, Integer personID,  Integer context,
			Integer iconKey, String iconName, ProfileMainTO mainTo, ProfileEmailTO emailTo, ProfileOtherTO otherTo,
			ProfileWatchlistTO watchlistTo,Locale locale) {
		if (personID==null && iconKey!=null) {
			personBean.setIconKey(iconKey);
			personBean.setSymbol(iconName);
		}
		List<LabelValueBean> errors = new LinkedList<LabelValueBean>();
		ProfileBL.updateProfileMainTO(personBean, currentUser, mainTo, errors, locale);
		if(emailTo!=null){
			ProfileBL.updateProfileEmailTO(personBean, emailTo);
		}
		if(otherTo!=null){
			ProfileBL.updateProfileOtherTO(personBean, otherTo, context, errors, locale);
		}
		if(watchlistTo!=null){
			ProfileBL.updateProfileWatchlistTO();
		}
		return errors;
	}

	public static void validateLoginName(Integer personKey, String userName,List<LabelValueBean> errors, Locale locale){
		TPersonBean personByLogin = PersonBL.loadByLoginName(userName);
		if (personByLogin!=null && (personKey==null || !personByLogin.getObjectID().equals(personKey))) {
			errors.add(new LabelValueBean(
					getText("admin.user.profile.err.loginName.unique", locale),
					ProfileJSON.JSON_FIELDS.userName ));
		}
	}


	/**
	 * Updates the user profile profile bean with new information from the user interface.
	 *
	 * @param personBean the person whose profile is being edited
	 * @param mainTO a JSON transfer object with data from the user interface
	 * @param errors a list of LabelValueBeans holding possible errors
	 * @param locale the current session locale
	 */
	static void updateProfileMainTO(TPersonBean personBean, TPersonBean currentUser,
			ProfileMainTO mainTO, List<LabelValueBean> errors, Locale locale) {
		Integer personKey = personBean.getObjectID();
		// login name should be unique
		validateLoginName(personKey,mainTO.getUserName(),errors,locale);
		//combination of lastname, firstname, email should be unique
		if (PersonBL.nameAndEmailExist(mainTO.getLastName(), mainTO.getFirstName(), mainTO.getUserEmail(), personKey)) {
			errors.add(new LabelValueBean(
					getText("admin.user.profile.err.nameAndEmail.unique", locale),
					ProfileJSON.JSON_FIELDS.lastName));
			errors.add(new LabelValueBean(
					getText("admin.user.profile.err.nameAndEmail.unique", locale),
					ProfileJSON.JSON_FIELDS.firstName));
			errors.add(new LabelValueBean(
					getText("admin.user.profile.err.nameAndEmail.unique", locale),
					ProfileJSON.JSON_FIELDS.userEmail));
		}
		//if ldap-auth is selected, check if the user is on the ldap-server
		if (mainTO.getLdapUser()!= null && mainTO.getLdapUser() && !LdapUtil.isOnLdapServer(mainTO.getUserName().trim())) {
			errors.add(new LabelValueBean(
					getText("admin.user.profile.err.loginName.notInLdap", locale),
					ProfileJSON.JSON_FIELDS.userName));
		}
		// Never change user name of "admin" user
		if (TPersonBean.ADMIN_USER.equalsIgnoreCase(personBean.getLoginName())) {
			mainTO.setUserName(personBean.getLoginName());
		}

		if (TPersonBean.GUEST_USER.equalsIgnoreCase(personBean.getLoginName())) {
			mainTO.setUserName(personBean.getLoginName());
			// only the sysadmin can change the guest password, firstname and lastname
			// but the loginname not even the system admin can change because there is hardcoded
			// functionality linked with this name
			if (!currentUser.getIsSysAdmin()) {
				mainTO.setPasswd("");
				mainTO.setPasswd2("");
				mainTO.setFirstName(personBean.getFirstName());
				mainTO.setLastName(personBean.getLastName());
			}
		}
		//Handle the password.
		if (mainTO.getPasswd()==null) {
			mainTO.setPasswd("");
		}
		if (!"".equals(mainTO.getPasswd().trim())) {
			//if password specified save the encripted format
			personBean.setPasswdEncrypted(mainTO.getPasswd());
		}
		if (mainTO.getLdapUser() != null && mainTO.getLdapUser()) {
			personBean.setPasswdEncrypted(Constants.LdapUserPwd);
		}
		personBean.setLoginName(mainTO.getUserName());
		personBean.setLastName(mainTO.getLastName());
		personBean.setFirstName(mainTO.getFirstName());
		TSiteBean siteBean=ApplicationBean.getInstance().getSiteBean();
		Perl5Pattern allowedEmailPattern = SiteConfigBL.getSiteAllowedEmailPattern(siteBean);
		Perl5Matcher pm = new Perl5Matcher();
		if (allowedEmailPattern!=null) {
			if (!pm.contains(mainTO.getUserEmail().trim(), allowedEmailPattern)) {
				LOGGER.warn("The email:'"+mainTO.getUserEmail()+"' is not accepted the pattern:"+allowedEmailPattern.getPattern());
				errors.add(new LabelValueBean(
						getText("admin.user.profile.err.emailAddress.format", locale),
						ProfileJSON.JSON_FIELDS.userEmail));
			}
		}
		personBean.setEmail(mainTO.getUserEmail());
		personBean.setPrefLocale(mainTO.getLocale());
		personBean.setUserTimeZoneId(mainTO.getUserTz());
	}


	/**
	 * Updates the user profile profile bean with new information from the user interface.
	 *
	 * @param personBean the person whos profile is being edited
	 * @param emailTO a JSON transfer object with data from the user interface
	 * @param errors a list of LabelValueBeans holding possible errors
	 * @param loaderResourceBundleMessages the current session locale
	 */
	private static void updateProfileEmailTO(TPersonBean personBean, ProfileEmailTO emailTO){
		personBean.setNoEmailPleaseBool(emailTO.isNoEmail());
		personBean.setPrefEmailType(emailTO.getPrefEmailType());
		personBean.setRemindMeAsOriginatorBool(emailTO.isRemindMeAsOriginator());
		personBean.setRemindMeAsManagerBool(emailTO.isRemindMeAsManager());
		personBean.setRemindMeAsResponsibleBool(emailTO.isRemindMeAsResponsible());
		personBean.setEmailRemindPriorityLevel(emailTO.getRemindPriorityLevel());
		personBean.setEmailRemindSeverityLevel(emailTO.getRemindSeverityLevel());
		personBean.setEmailLead(emailTO.getEmailLead());
		personBean.setReminderDays(emailTO.getRemindMeOnDays());
	}

	/**
	 * Updates the user profile profile bean with new information from the user interface.
	 * @param personBean the person who's profile is being edited
	 * @param otherTO a JSON transfer object with data from the user interface
	 * @param errors a list of LabelValueBeans holding possible errors
	 * @param locale the current session locale
	 */
	private static void updateProfileOtherTO(TPersonBean personBean,
			ProfileOtherTO otherTO, int context, List<LabelValueBean> errors, Locale locale){
		if (context == ProfileMainTO.CONTEXT.SELFREGISTRATION) {
			Integer departmentID = DepartmentBL.getDefaultDepartment();
			if (departmentID!=null) {
				otherTO.setDepartment(departmentID);
			}
		} else {
			if (context == ProfileMainTO.CONTEXT.PROFILEEDIT) {
				if (otherTO.getDepartment()==null) {
					otherTO.setDepartment(personBean.getDepartmentID());
					personBean.setTokenPasswd(null);
				}
			}
		}
		if (context == ProfileMainTO.CONTEXT.SELFREGISTRATION ||
				context == ProfileMainTO.CONTEXT.PROFILEEDIT) {
			if (otherTO.getEmployeeId()==null) {
				otherTO.setEmployeeId(personBean.getEmployeeID());
			}
		}
		personBean.setLastEdit(new Date());
		personBean.setDepartmentID(otherTO.getDepartment());
		personBean.setPhone(otherTO.getPhone());
		personBean.setEmployeeID(otherTO.getEmployeeId());
		personBean.setHoursPerWorkDay(otherTO.getWorkingHours());
		personBean.setHourlyWage(otherTO.getHourlyWage());
		personBean.setExtraHourWage(otherTO.getExtraHourWage());
		personBean.setCsvEncoding(otherTO.getCsvEncoding());
		personBean.setCsvCharacter(otherTO.getCsvSeparator());
		personBean.setAlwaysSaveAttachment(otherTO.isSaveAttachments());
		personBean.setDesignPath(otherTO.getDesignPath());
		personBean.setPrintItemEditable(otherTO.isActivateInlineEdit());
		personBean.setEnableQueryLayout(otherTO.isActivateLayout());
		personBean.setHomePage(otherTO.getHomePage());
		personBean.setSessionTimeoutMinutes(otherTO.getSessionTimeout());
		Integer oldSubstituteID = personBean.getSubstituteID();
		Integer newSubstituteID = otherTO.getSubstituteID();
		if (EqualUtils.notEqual(oldSubstituteID, newSubstituteID)) {
			if (oldSubstituteID!=null) {
				//reload the cache for the substitutes (old and new to force loading the substitutes lists again from the database)
				LookupContainer.reloadPerson(oldSubstituteID);
				ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, oldSubstituteID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
			}
			if (newSubstituteID!=null) {
				LookupContainer.reloadPerson(newSubstituteID);
				ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, newSubstituteID, ClusterBL.CHANGE_TYPE.UPDATE_CACHE);
			}
		}
		personBean.setSubstituteID(newSubstituteID);
		if (context == ProfileMainTO.CONTEXT.USERADMINEDIT ||
				context == ProfileMainTO.CONTEXT.PROFILEEDIT) {
			Integer actualUserLevel = personBean.getUserLevel();
			Integer modifiedUserLevel = otherTO.getUserLevel();
			if (actualUserLevel!=null && actualUserLevel.equals(TPersonBean.USERLEVEL.SYSADMIN)) {
				if (modifiedUserLevel==null || !modifiedUserLevel.equals(actualUserLevel)) {
					List<TPersonBean> systemAdmins = PersonBL.loadSystemAdmins();
					if (systemAdmins==null || systemAdmins.isEmpty() || (systemAdmins.size()==1 && systemAdmins.get(0).getObjectID().equals(personBean.getObjectID()))) {
						errors.add(new LabelValueBean(
								LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.manage.err.revokeAdmins", locale),
								ProfileJSON.JSON_FIELDS.userLevel));
					}
				}
			}
			List<Integer> selectedPersonIDList = new LinkedList<Integer>();
			selectedPersonIDList.add(personBean.getObjectID());
			String usersExceededMessage = PersonConfigBL.usersExceeded(selectedPersonIDList, locale, modifiedUserLevel, false);
			if (usersExceededMessage!=null) {
				errors.add(new LabelValueBean(usersExceededMessage, ProfileJSON.JSON_FIELDS.userLevel));

			}

		}
		personBean.setUserLevel(otherTO.getUserLevel());
	}

	/**
	 * Updates the user profile profile bean with new information from the user interface.
	 *
	 * @param person the person whos profile is being edited
	 * @param watchlistTO a JSON transfer object with data from the user interface
	 * @param errors a list of LabelValueBeans holding possible errors
	 * @param loaderResourceBundleMessages the current session locale
	 */
	private static void updateProfileWatchlistTO(){
	}

	static Integer saveUserProfile(TPersonBean personBean, TPersonBean currentUser, Integer context, Boolean isUser) {
		ApplicationBean appBean = ApplicationBean.getInstance();
		Integer personID = null;
		if (currentUser != null || context == ProfileMainTO.CONTEXT.SELFREGISTRATION) {
			LOGGER.debug("Updating user profile information");
			if (context == ProfileMainTO.CONTEXT.USERADMINADD ||
					context == ProfileMainTO.CONTEXT.SELFREGISTRATION) {
				Integer userLevel = personBean.getUserLevel();
				if (userLevel==null) {
					boolean isGenji = ApplicationBean.getInstance().isGenji();
					if (context == ProfileMainTO.CONTEXT.SELFREGISTRATION) {
						if (isGenji) {
							//no client modus in Genji
							personBean.setUserLevel(TPersonBean.USERLEVEL.FULL);
						} else {
							personBean.setUserLevel(TPersonBean.USERLEVEL.CLIENT);
						}
					}
					if (context == ProfileMainTO.CONTEXT.USERADMINADD) {
						if(isUser != null) {
							if(isUser) {
								personBean.setUserLevel(TPersonBean.USERLEVEL.FULL);
							}else {
								personBean.setUserLevel(TPersonBean.USERLEVEL.CLIENT);
							}
						}
					}
					if(personBean.getUserLevel() == null) {
						personBean.setUserLevel(TPersonBean.USERLEVEL.FULL);
					}
				}
			}

			if (context == ProfileMainTO.CONTEXT.SELFREGISTRATION) {
				personBean.setHomePage("cockpit");
			}

			if (context != ProfileMainTO.CONTEXT.SELFREGISTRATION) {
				personBean.setTokenPasswd(null);
			}
			personID=PersonBL.save(personBean);
			personBean.setObjectID(personID);

			//In case of self registration the new client will be added into proper groups /based on flag/.
			if(personID != null) {
				if (context == ProfileMainTO.CONTEXT.SELFREGISTRATION ||
						context == ProfileMainTO.CONTEXT.USERADMINADD) {
					addNewClientToGroups(personID);
				}
			}

			if (context == ProfileMainTO.CONTEXT.USERADMINADD ||
					context == ProfileMainTO.CONTEXT.SELFREGISTRATION) {
				appBean.setActualUsers();
				if (personID!=null) {
					List<Integer> filterIDs = MenuitemFilterBL.getFilterIDsToSubscribe();
					MenuitemFilterBL.subscribePersonsToFilters(personID, filterIDs);
				}
			}
		}
		return personID;
	}


	static boolean support(TPersonBean personBean, TPersonBean currentUser, Integer context,
			String password, Map<String, Object> session, Locale locale, boolean localeChange) {
		// Change the session locale and design path only if self registration or own profile edit
		LOGGER.debug("support profile: context="+context);
		if (context == ProfileMainTO.CONTEXT.SELFREGISTRATION ||
				context == ProfileMainTO.CONTEXT.PROFILEEDIT) {
			session.put("DESIGNPATH", personBean.getDesignPath());
			if (locale==null) {
				if (currentUser.getPrefLocale() == null ||
						"".equals(currentUser.getPrefLocale())) { // rely on browser settings
					locale = LocaleHandler.getExistingLocale(ServletActionContext.getRequest().getLocales());
					currentUser.setLocale(locale);
				}
				else { // get as stored in user profile
					locale = currentUser.getLocale();
				}
			}
			if (localeChange) {
				currentUser.setLocale(locale);
				LocaleHandler.exportLocaleToSession(session, locale);
				List<FilterInMenuTO> myFilters=FilterBL.loadMyMenuFiltersWithTooltip(personBean, locale);
				session.put(FilterBL.MY_MENU_FILTERS_JSON, FilterInMenuJSON.encodeFiltersInMenu(myFilters));
			}
		}
		boolean emailSent = false;
		if (context.equals(ProfileMainTO.CONTEXT.SELFREGISTRATION) ||
			context.equals(ProfileMainTO.CONTEXT.USERADMINADD)) {
			emailSent = ProfileBL.sendMail(context, password, personBean);
			if(emailSent){
				LOGGER.debug("An e-mail with registration/update profile was send to "+personBean.getFullName()+"<"+personBean.getEmail()+">");
			}
		}
		return emailSent;
	}

	/**
	 * Sends two mails by creating a new user: to the newly registered user and to the admin
	 * @return
	 */
	private static boolean sendMail(Integer context,String password, TPersonBean personBean) {
		LOGGER.debug("Try to send registration/edit profile e-mail...");
		boolean emailSent = false;
		TSiteBean siteBean = ApplicationBean.getInstance().getSiteBean();
		if (!personBean.getNoEmailPleaseBool()) {
			StringWriter w = new StringWriter();
			Template template = null;
			Map<String,String> root = new HashMap<String,String>();
			TMailTemplateDefBean mailTemplateDefBean;
			if(context.equals(ProfileMainTO.CONTEXT.SELFREGISTRATION)) {
				mailTemplateDefBean = MailTemplateBL.getSystemMailTemplateDefBean(IEventSubscriber.EVENT_POST_USER_SELF_REGISTERED, personBean);
			}else{
				mailTemplateDefBean = MailTemplateBL.getSystemMailTemplateDefBean(IEventSubscriber.EVENT_POST_USER_REGISTERED, personBean);
			}
			String subject;
			if(mailTemplateDefBean!=null){
				String templateStr=mailTemplateDefBean.getMailBody();
				subject = mailTemplateDefBean.getMailSubject();
				try {
					template = new Template("name", new StringReader(templateStr), new Configuration());
				} catch (IOException e) {
					LOGGER.debug("Loading the template failed with " + e.getMessage(), e);
				}
			} else {
				LOGGER.error("Can't get mail template for registration");
				return false;
			}

			if (template==null) {
					LOGGER.error("No valid template found for registration e-mail (maybe compilation errors).");
				return false;
			}
			Locale locale = new  Locale(personBean.getPrefLocale());

			String tokenExpDate=DateTimeUtils.getInstance().formatGUIDateTime(personBean.getTokenExpDate(),locale);

			//The path starts with a "/" character but does not end with a "/"
			String contextPath=ApplicationBean.getInstance().getServletContext().getContextPath();
			String siteURL=siteBean.getServerURL();
			if(siteURL==null){
				siteURL="";
			}else if(siteURL.endsWith("/")){
				siteURL=siteURL.substring(0,siteURL.lastIndexOf("/"));
			}
			String confirmURL = siteURL+contextPath+"/userProfile!confirm.action?ctk=" + personBean.getTokenPasswd();
			if(context.equals(ProfileMainTO.CONTEXT.USERADMINADD)){
				confirmURL = siteURL + contextPath+"/resetPassword!confirm.action?ctk=" + personBean.getForgotPasswordKey();
			}
			String serverURL = siteURL+contextPath;
			root.put("loginname", personBean.getUsername());
			root.put("password", password);
			root.put("firstname", personBean.getFirstName());
			root.put("lastname", personBean.getLastName());
			root.put("serverurl", serverURL);
			root.put("confirmUrl", confirmURL);
			root.put("tokenExpDate",tokenExpDate);
			root.put("ldap", new Boolean(personBean.isLdapUser()).toString());
			try {
				template.process(root, w);
			} catch (Exception e) {
				LOGGER.error("Processing registration template " + template.getName() + " failed with " + e.getMessage());
			}
			w.flush();
			String messageBody = w.toString();
			//Send mail to user with password
			// Send mail to newly registered user
			try {
				MailSender ms = new MailSender(new InternetAddress(siteBean.getTrackEmail(), siteBean.getEmailPersonalName()),
						new InternetAddress(personBean.getEmail(), personBean.getFullName()), subject, messageBody, mailTemplateDefBean.isPlainEmail());
				emailSent=ms.send(); // wait for sending email
				LOGGER.debug("emailSend:"+emailSent);
			} catch (Exception t) {
				   LOGGER.error("Registration.populate.SendMail", t);
			}
		}

		try {
			// Notify administrator of system
			String CRLF="\r\n";
			TPersonBean admin = PersonBL.loadByLoginName(TPersonBean.ADMIN_USER);
			if (admin == null) {
				LOGGER.error("No 'admin' user found!");
			} else {
				Locale adminLocale =  admin.getLocale();
				Object[] sbjArguments = {personBean.getFirstName()
										 + " " + personBean.getLastName()
										 + " [" + personBean.getEmail() + "]"};
				String theSubject = LocalizeUtil.getParametrizedString("registration.mail.admin.subject", sbjArguments, adminLocale);
				StringBuffer theBody = new StringBuffer();
				theBody.append(CRLF+CRLF);
				theBody.append(LocalizeUtil.getLocalizedTextFromApplicationResources("registration.mail.admin.line2", adminLocale));
				theBody.append(CRLF);
				theBody.append(LocalizeUtil.getLocalizedTextFromApplicationResources("registration.mail.admin.line3", adminLocale));
				theBody.append(CRLF+CRLF);
				theBody.append(LocalizeUtil.getLocalizedTextFromApplicationResources("registration.mail.admin.line4", adminLocale));
				theBody.append(CRLF);
				MailSender ms = new MailSender(new InternetAddress(siteBean.getTrackEmail(), siteBean.getEmailPersonalName()),new InternetAddress(admin.getEmail(), admin.getFullName()), theSubject, theBody.toString(), true);
				ms.start(); // run in own thread
			}
		}
		catch (Exception t) {
		   LOGGER.error("Registration.populate.SendMail", t);
		}
		return emailSent;
	}

	private static String getText(String s, Locale locale){
		return LocalizeUtil.getLocalizedTextFromApplicationResources(s, locale);
	}

	/**
	 * The following method iterates available groups.
	 * If addNewClientToThisGroup flag is true for a group item then this
	 * person will be added into this group.
	 * @param clientID
	 */
	public static void addNewClientToGroups(Integer clientID) {
		List<TreeNodeBaseTO> tmp = GroupConfigBL.getChildren("");
		boolean addNewClientToThisGroup = false;
		for(TreeNodeBaseTO treeNodeBase : tmp) {
			PersonInGroupTokens personInGroupTokens = GroupConfigBL.decodeNode(treeNodeBase.getId());
			Integer groupID = personInGroupTokens.getGroupID();
			TPersonBean personBean = LookupContainer.getPersonBean(groupID);
			addNewClientToThisGroup = PropertiesHelper.getBooleanProperty(personBean.getPreferences(), GroupConfigBL.GROUP_FLAGS.JOIN_NEW_USER_TO_THIS_GROUP);
			if(addNewClientToThisGroup) {
				LOGGER.debug("The following person: " + clientID.toString() + " will be added to following group: " + groupID);
				GroupConfigBL.assign(groupID, clientID.toString());
			}
		}
	}

}
