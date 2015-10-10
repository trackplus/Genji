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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.person.PersonConfigBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.resources.LocalizeJSON;
import com.aurel.track.user.LogoffBL;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


/**
* Implementation of <strong>ProfileAction</strong> that processes the site
* configuration requests
*/
public class ProfileAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(ProfileAction.class);
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean currentUser;

	private Integer context;
	private Integer personId;
	//for new persons the previously uploaded avatar iconKey should be also set
	//(for existing persons the icon key is directly saved by upload independently whether the user profile will be saved or not)
	private Integer iconKey;
	private String iconName;

	private String confirmToken;


	// The transfer objects to and from the user interface
	private ProfileMainTO mainTo;
	private ProfileEmailTO emailTo;
	private ProfileOtherTO otherTo;
	private ProfileWatchlistTO watchlistTo;
	private Boolean isUser = null;
	private String initData;
	private boolean hasInitData=true;

	/**
	 * This routine is called first by Struts2 on any action.
	 */
	public void prepare() throws Exception {
		currentUser = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}

	/**
	 * This is the <code>loadSelfRegistration</code> action. It prints a suitable
	 * JSON object directly into the response stream. Previously the
	 * <prepare> and <execute> methods have run.
	 *
	 * @return usually nothing, response is directly printed into the response stream. Can also
	 * throw back to logon page in case of insufficient authorization.
	 */
	public String loadSelfRegistration () {
		if(locale==null){
			//TODO get the browser locale
			locale=Locale.getDefault();
		}
		JSONUtility.encodeJSON(servletResponse,
				ProfileBL.load(currentUser, personId, ProfileMainTO.CONTEXT.SELFREGISTRATION, locale));
		return null;
	}

	/**
	 * This is the <code>loadAddUser</code> action. It prints a suitable
	 * JSON object directly into the response stream. Previously the
	 * <prepare> and <execute> methods have run.
	 *
	 * @return usually nothing, response is directly printed into the response stream. Can also
	 * throw back to logon page in case of insufficient authorization.
	 */
	public String loadAddUser () {
		if(currentUser == null || !currentUser.isSys()) {
			// just throw her to the logon page
			return "logon";
		}
		JSONUtility.encodeJSON(servletResponse,
				ProfileBL.load(currentUser, personId, ProfileMainTO.CONTEXT.USERADMINADD, locale));
		return null;
	}

	/**
	 * This is the <code>loadEditMyProfile</code> action. It prints a suitable
	 * JSON object directly into the response stream. Previously the
	 * <prepare> and <execute> methods have run.
	 *
	 * @return usually nothing, response is directly printed into the response stream. Can also
	 * throw back to logon page in case of insufficient authorization.
	 */
	public String loadEditMyProfile () {
		if(currentUser == null) {
			// just throw her to the logon page
			return "logon";
		}
		int context = ProfileMainTO.CONTEXT.PROFILEEDIT;
		if (currentUser.isSys()) {
			//the system admin can edit her profile without "profile" restrictions (can set department, emplyeeID, ...)
			context = ProfileMainTO.CONTEXT.USERADMINEDIT;
			personId = currentUser.getObjectID();
		}
		JSONUtility.encodeJSON(servletResponse,
				ProfileBL.load(currentUser, personId, context, locale));
		return null;
	}

	/**
	 * This is the <code>loadEditUser</code> action. It prints a suitable
	 * JSON object directly into the response stream. Previously the
	 * <prepare> and <execute> methods have run.
	 *
	 * @return usually nothing, response is directly printed into the response stream. Can also
	 * throw back to logon page in case of insufficient authorization.
	 */
	public String loadEditUser () {
		if(currentUser == null || !currentUser.isSys()) {
			// just throw her to the logon page
			return "logon";
		}
		JSONUtility.encodeJSON(servletResponse,
				ProfileBL.load(currentUser, personId, ProfileMainTO.CONTEXT.USERADMINEDIT, locale));
		return null;
	}


	/**
	 * This saves the server configuration coming from the user interface to
	 * the database. Some validation is performed when it cannot be directly
	 * done at the user interface.
	 *
	 * @return usually nothing, output is directly printed into response stream
	 */
	public String save (){
		LOGGER.debug("Processing ProfileAction save()");
		if((context==ProfileMainTO.CONTEXT.USERADMINADD || context==ProfileMainTO.CONTEXT.USERADMINEDIT)&&
				(currentUser == null || !currentUser.isSys())){
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(), "No admin user",1);
			return null;
		}
		boolean localeChange = false;
		Locale personLocale = locale;
		if (context==ProfileMainTO.CONTEXT.PROFILEEDIT || context==ProfileMainTO.CONTEXT.SELFREGISTRATION) {
			Locale previousLocale =locale; //currentUser==null?null:currentUser.getLocale();
			Locale actualLocale = null;
			if(mainTo.getLocale()!=null && mainTo.getLocale().length()>0){
				actualLocale = LocaleHandler.getLocaleFromString(mainTo.getLocale());
			}
			if (previousLocale==null) {
				localeChange = (actualLocale!=null);
			}else{
				localeChange=!previousLocale.equals(actualLocale);
			}
			personLocale = actualLocale;
		}
		if(personLocale==null){
			personLocale=Locale.getDefault();
		}
		TPersonBean personBean = ProfileBL.getPersonByContext(currentUser, personId, context);
		List<LabelValueBean> errors = ProfileBL.validate(personBean,currentUser, personId, context, iconKey, iconName, mainTo, emailTo, otherTo, watchlistTo, personLocale);
		if (errors.isEmpty()) {
			if (context==ProfileMainTO.CONTEXT.USERADMINADD || context==ProfileMainTO.CONTEXT.SELFREGISTRATION) {
				String usersExceededMessage = PersonConfigBL.usersExceeded(null, locale, personBean.getUserLevel(), false);
				if (usersExceededMessage!=null) {
					personBean.setDisabled(true);
				}
			}
			if (context==ProfileMainTO.CONTEXT.SELFREGISTRATION) {
				personBean.setDisabled(true);
				Date texpDate = new Date(new Date().getTime() + 4*3600*1000); // 4 hours
				personBean.setTokenExpDate(texpDate);
				String tokenPasswd = DigestUtils.md5Hex(Long.toString(texpDate.getTime()));
				personBean.setTokenPasswd(tokenPasswd);
			}
			if (context==ProfileMainTO.CONTEXT.USERADMINADD) {
				//create user by admin
				LOGGER.debug("Create user by admin, userNam= "+personBean.getUsername());
				Date texpDate = new Date(new Date().getTime() + 4*3600*1000); // 4 hours
				personBean.setTokenExpDate(texpDate);
				String tokenPasswd = DigestUtils.md5Hex(Long.toString(texpDate.getTime()));
				personBean.setForgotPasswordKey(tokenPasswd);
			}
			Integer personID = ProfileBL.saveUserProfile(personBean, currentUser, context, isUser);
			String dpassword = mainTo.getPasswd();
			if (context == ProfileMainTO.CONTEXT.SELFREGISTRATION) {
				TPersonBean newPerson = PersonBL.loadByPrimaryKey(personID);
				dpassword=newPerson.getTokenPasswd();
			}
			boolean emailSend=ProfileBL.support(personBean, personBean, context, dpassword, session, personLocale, localeChange);
			String email=personBean.getEmail();
			if (context==ProfileMainTO.CONTEXT.SELFREGISTRATION || context==ProfileMainTO.CONTEXT.USERADMINADD) {
				if(MobileBL.isMobileApp(session)) {
					ProjectConfigBL.createWorkspaceForNewUser(personBean, personLocale);
				}
			}
			JSONUtility.encodeJSON(servletResponse, ProfileJSON.encodeSaveProfileJSON(localeChange,emailSend,email));
		} else {
			LOGGER.debug("Validation errors when saving the user profile");
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, false);
			JSONUtility.appendStringValue(sb,"errorMessage",getText("admin.user.profile.err.errorSave"));
			JSONUtility.appendErrorsExtJS(sb,errors,true);
			sb.append("}");
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		}
		return null;
	}


	/**
	 * Checks if the combination of user name and e-mail is unique
	 * @return
	 */
	public String validateUser() {
		if(context!=null&&context.intValue()!=0&&currentUser == null) {
			// just throw her to the logon page
			List<LabelValueBean> errors = new LinkedList<LabelValueBean>();
			errors.add(new LabelValueBean("no user",ProfileJSON.JSON_FIELDS.userName ));
			JSONUtility.encodeJSONErrors(servletResponse, errors);
			return null;
		}
		List<LabelValueBean> errors = new LinkedList<LabelValueBean>();
		TPersonBean personBean = ProfileBL.getPersonByContext(currentUser, personId, context);
		ProfileBL.updateProfileMainTO(personBean, currentUser, mainTo, errors, locale);
		if (errors.isEmpty()) {
			JSONUtility.encodeJSONSuccess(servletResponse);
		} else {
			LOGGER.debug("We got validation errors when validating the user profile");
			JSONUtility.encodeJSONErrors(servletResponse, errors);
		}
		return null;
	}

	public String validateLoginName() {
		if(locale==null){
			//TODO get the browser locale
			locale=Locale.getDefault();
		}
		List<LabelValueBean> errors = new LinkedList<LabelValueBean>();
		TPersonBean personBean = ProfileBL.getPersonByContext(currentUser, personId, context);
		String userName=(mainTo!=null?mainTo.getUserName():null);
		ProfileBL.validateLoginName(personBean.getObjectID(),userName,errors,locale);
		if (errors.isEmpty()) {
			JSONUtility.encodeJSONSuccess(servletResponse);
		} else {
			LOGGER.debug("We got validation errors when validating the user login name");
			JSONUtility.encodeJSONErrors(servletResponse, errors);
		}
		return null;
	}

	/**
	 * Processes the confirmation link for newly registered users.
	 * @return
	 */
	public String confirm() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		locale = request.getLocale();
		if(locale==null){
			locale=Locale.getDefault();
		}
		LogoffBL.doLogoff(session, httpSession, locale);
		boolean personFound=false;
		TPersonBean personBean = PersonBL.loadByTokenPasswd(confirmToken);
		if (personBean != null ) { //&& personBean.getTokenExpDate().getTime() > new Date().getTime()) {
			String prefLocale = personBean.getPrefLocale();
			if (prefLocale!=null) {
				locale = LocaleHandler.getLocaleFromString(prefLocale);
			}
			personBean.setDisabled(false);
			personBean.setTokenExpDate(null);
			personBean.setTokenPasswd(null);
			PersonBL.save(personBean);
			this.getSession().put("CUSER", personBean.getLoginName());
			personFound=true;
			locale = personBean.getLocale();
		}
		httpSession.setAttribute("localizationJSON", LocalizeJSON.encodeLocalization(locale));
		String extJSLocale = LocaleHandler.getExistingExtJSLocale(locale);
		httpSession.setAttribute("EXTJSLOCALE", extJSLocale);
		StringBuilder sb =  LogoffBL.createInitData(httpSession, false, request, false, null, locale);
		initData = sb.toString();
		if(personFound){
			return "confirmed";
		}else {
			return "expired";
		}
	}

	//-------------------------------------------------------------------------------------
	//
	// The setters and getters for Struts2 follow...

	/**
	* @return the session
	 */
	public Map<String, Object> getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}


	// The names of the following getters and setters must match the JSON object names
	public ProfileMainTO getMain() {
		return mainTo;
	}

	public void setMain(ProfileMainTO mainTo) {
		this.mainTo = mainTo;
	}

	public ProfileEmailTO getRemail() {
		return emailTo;
	}

	public void setRemail(ProfileEmailTO emailTo) {
		this.emailTo = emailTo;
	}

	public ProfileOtherTO getOther() {
		return otherTo;
	}

	public void setOther(ProfileOtherTO otherTo) {
		this.otherTo = otherTo;
	}

	public ProfileWatchlistTO getWatchlist() {
		return watchlistTo;
	}

	public void setWatchlist(ProfileWatchlistTO watchlistTo) {
		this.watchlistTo = watchlistTo;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setContext(Integer context) {
		this.context = context;
	}

	public void setIconKey(Integer iconKey) {
		this.iconKey = iconKey;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public void setCtk(String url) {
		this.confirmToken = url;
	}

	public boolean isHasInitData() {
		return hasInitData;
	}

	public String getInitData() {
		return initData;
	}

	public void setInitData(String initData) {
		this.initData = initData;
	}

	public boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}

}
