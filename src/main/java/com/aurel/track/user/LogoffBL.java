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

package com.aurel.track.user;

import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.server.motd.MotdBL;
import com.aurel.track.beans.TMotdBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.item.lock.ItemLockBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.mobile.MobileBL;
import com.aurel.track.persist.TMotd;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeJSON;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LocaleHandler;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;

/**
 *
 */
public class LogoffBL {
	private static final Logger accessLogger = LogManager.getLogger("Access");
	private static final Logger LOGGER = LogManager.getLogger(LogoffBL.class);


	public static void doLogoff(Map<String, Object> session,HttpSession httpSession, Locale locale){
		String nonce = UUID.randomUUID().toString();
		nonce = nonce.replaceAll("\\-", "");

		TPersonBean user = null;
		if (session != null) {
			user = (TPersonBean) session.get(Constants.USER_KEY);
		}

		// Process this users logoff
		if (user != null && user.getLoginName() != null) {
			accessLogger.info("LOGOFF: User '" + user.getLoginName().trim()
					+ "' logged off at " + new Date().toString());
		}

		// Do a few things just to make sure
		try {
			/*httpSession.removeAttribute(Constants.USER_KEY);
			httpSession.removeAttribute("DESIGNPATH");
			httpSession.removeAttribute(WorkItemLockUtil.LOCKEDISSUE);*/
			//remove all locked items
			if (user!=null) {
				ItemLockBL.removeLockedIssuesByUser(user.getObjectID());
			}

			if (!httpSession.isNew()) {
				Enumeration attributeNames = httpSession.getAttributeNames();
				String attributeName;
				while (attributeNames.hasMoreElements()) {
					attributeName = (String) attributeNames.nextElement();
					if(attributeName.equals(Constants.POSTLOGINFORWARD)){
						//skip the forward url
						continue;
					}
					httpSession.removeAttribute(attributeName);
				}
				/*httpSession.invalidate();
				 httpSession = request.getSession(true);*/
//		        if (SecurityContextHolder.getContext() != null
//		            	&& SecurityContextHolder.getContext().getAuthentication() != null) {
//		        	SecurityContextHolder.getContext().setAuthentication(null);
//		        }
			}
		} catch (Exception e) {
			accessLogger.error("Exception thrown when logging off: " + e.getMessage(), e);
		}

		EventPublisher evp = EventPublisher.getInstance();
		if (evp != null) {
			List<Integer> events = new LinkedList<Integer>();
			events.add(Integer.valueOf(IEventSubscriber.EVENT_PRE_USER_LOGGED_OUT));
			evp.notify(events, user);
		}

		httpSession.setAttribute("localizationJSON", LocalizeJSON.encodeLocalization(locale));

		//List<ILabelBean> issueTypes= IssueTypeBL.loadAll(locale);
		//httpSession.setAttribute("issueTypesJSON",JSONUtility.encodeIssueTypes(issueTypes));

		String extJSLocale = LocaleHandler.getExistingExtJSLocale(locale);

		httpSession.setAttribute("EXTJSLOCALE", extJSLocale);

		httpSession.setAttribute("NONCE", nonce);


	}

	public static StringBuilder  createInitData(HttpSession httpSession, boolean checkForMobile, HttpServletRequest request, boolean isMobileApplication, Integer mobileApplicationVersionNo, Locale locale){
		// A crude attempt to detect mobile devices, don't rely on it
		boolean mayBeMobile;
		if (checkForMobile) {
			mayBeMobile = isThisAMobileDevice(request);
		} else {
			mayBeMobile = false;
		}


		//in case of tablet application
		httpSession.setAttribute("isMobileApplication", isMobileApplication);

		httpSession.setAttribute("mobile", new Boolean(mayBeMobile));

		// Ignore for now...
		mayBeMobile = false;

		TMotdBean motd = MotdBL.loadMotd(locale.getLanguage());

		if (motd == null) {
			motd = MotdBL.loadMotd("en");
		}

		String teaserText =  motd.getTeaserText();

		TSiteBean siteBean = DAOFactory.getFactory().getSiteDAO().load1();

		//if (ApplicationBean.getApplicationBean().getOpState().equals(ApplicationBean.OPSTATE_MAINTENNANCE)) {
		if (ApplicationBean.OPSTATE_MAINTENNANCE.endsWith(siteBean.getOpState())) {
			String msg = null;
			//if (ApplicationBean.getApplicationBean().getHasUserMsg()){
			String userMessage = siteBean.getUserMessage();
			Boolean userMessageActiv = siteBean.getUserMessageActiv();
			if (userMessageActiv!=null && userMessageActiv.booleanValue() &&  userMessage!=null && !"".equals(userMessage)) {
				msg = userMessage; /*ApplicationBean.getApplicationBean().getUserMsg();*/
			} else {
				msg = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.status.opstate.infoHeader", locale);;
			}
			teaserText = "<div class='maintenance'>" + msg + "</div>";
		}

		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "nonce", httpSession.getAttribute("NONCE").toString());
		JSONUtility.appendStringValue(sb, "sessionId", httpSession.getId());
		if (motd != null) {
			JSONUtility.appendStringValue(sb, "motd", motd.getTheMessage());
			// JSONUtility.appendStringValue(sb, "teaserText",Html2Text.getNewInstance().convert(motd.getTeaserText()) ,true);
			JSONUtility.appendStringValue(sb, "teaserText", teaserText );
		} else { // in case of database connection problems
			JSONUtility.appendStringValue(sb, "motd", "" );
		}
		JSONUtility.appendBooleanValue(sb, "maybeMobile", mayBeMobile);
		JSONUtility.appendStringValue(sb, "serverVersion", ApplicationBean.getApplicationBean().getVersion());
		JSONUtility.appendIntegerValue(sb, "serverVersionNo", ApplicationBean.getApplicationBean().getVersionNo());
		JSONUtility.appendIntegerValue(sb, "clientCompatibility", MobileBL.checkClientCompatibility(mobileApplicationVersionNo, true), true);
		sb.append("}");
		return sb;
	}


	/**
	 * Helper routine to detect mobile devices based on user-agent
	 * @param request
	 * @return
	 */
	public static Boolean isThisAMobileDevice(HttpServletRequest request) {
		Boolean mayBeMobile = false;
		String userAgent = request.getHeader("user-agent");
		Pattern pat1 = Pattern.compile(".*(iphone|ipod|ipad|android|symbian|nokia|blackberry| rim |" + "opera mini|opera mobi|windows ce|windows phone|up\\.browser|netfront|" + "palm\\-|palm os|pre\\/|palmsource|avantogo|webos|hiptop|iris|kddi|kindle|" + "lg\\-|lge|mot\\-|motorola|nintendo ds|nitro|playstation portable|samsung|" + "sanyo|sprint|sonyericsson|symbian).*");
		Pattern pat2 = Pattern.compile("(alcatel|audiovox|bird|coral|cricket|docomo|edl|huawei|htc|gt-|lava|" + "lct|lg|lynx|mobile|lenovo|maui|micromax|mot|myphone|nec|nexian|nook|" + "pantech|pg|polaris|ppc|sch|sec|spice|tianyu|ustarcom|utstarcom|videocon|" + "vodafone|winwap|zte).*");
		if (userAgent != null) {
			mayBeMobile = pat1.matcher(userAgent.toLowerCase()).matches() || pat2.matcher(userAgent.toLowerCase()).matches();

			if (mayBeMobile) {
				LOGGER.info("Detected access from mobile device. User agent is " + userAgent);
			}
		}
		return mayBeMobile;
	}
}
