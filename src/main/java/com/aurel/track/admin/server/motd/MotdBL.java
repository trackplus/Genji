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

package com.aurel.track.admin.server.motd;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TMotdBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MotdDAO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;

/**
 * Class to handle the logic for the message of the day editor. The MOTD
 * is displayed on the login page.
 */
public class MotdBL {
		
	private static MotdDAO motdDAO = DAOFactory.getFactory().getMothDAO();	
	/*
	 * Load the MOTD for this locale
	 * @param theLocale
	 * @return
	 */
	public static TMotdBean loadMotd(String theLocale){
		if (theLocale == null || "".equals(theLocale)) {
			theLocale = Locale.getDefault().getLanguage();
		}
		List<TMotdBean> motdBeans = motdDAO.loadByLocale(theLocale);		
		if (motdBeans!=null && !motdBeans.isEmpty()) {
			return motdBeans.get(0);			
		} else {
			TMotdBean motdBean = new TMotdBean();
			motdBean.setTheLocale(theLocale);
			motdBean.setTheMessage("");
			return motdBean;
		}
	}
	
	/**
	 * Saves the MOTD for a specific locale.
	 * @param theLocale the locale this MOTD is to be saved under
	 * @param theMessage the message to be saved for this locale
	 * @return
	 */
	static Integer saveMotd(String theLocale, String theMessage, String teaserText){
		TMotdBean motdBean=loadMotd(theLocale);
		motdBean.setTheMessage(theMessage);
		motdBean.setTeaserText(teaserText);	
		return saveMotd(motdBean);
	}
	
	/**
	 * Saves the MOTD for a specific locale.
	 * @param motdBean the locale this MOTD is to be saved under
	 * @return
	 */
	public static Integer saveMotd(TMotdBean motdBean){		
		return motdDAO.save(motdBean);
	}

	/**
	 * Encodes the MOTD into a JSON string,
	 * @param motdBean the MOTD to be encoded
	 * @return the JSON encoded MOTD 
	 */
	public static String encodeMotd(TMotdBean motdBean){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");		
		JSONUtility.appendStringValue(sb,"theLocale",motdBean.getTheLocale());
		String teaserText = motdBean.getTeaserText();
		if (teaserText==null) {
			//if null the .getForm().load() does not actualize the form (remains the text for the old language)
			teaserText = "";
		}
		JSONUtility.appendStringValue(sb,"teaserText", teaserText);
		String message = motdBean.getTheMessage();
		if (message==null) {
			//if null the .getForm().load() does not actualize the form (remains the text for the old language)
			message = "";
		}
		JSONUtility.appendStringValue(sb,"theMessage", message);
		JSONUtility.appendLabelValueBeanList(sb,"localeList", LocaleHandler.getAvailableLocales(), true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
}
