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

package com.aurel.track.admin.customize.mailTemplate;

import java.util.List;
import java.util.Locale;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDefDAO;

/**
 */
public class MailTemplateDefBL {

	private static MailTemplateDefDAO mailTemplateDefDAO = DAOFactory.getFactory().getMailTemplateDefDAO();
	private static final Logger LOGGER = LogManager.getLogger(MailTemplateBL.class);

	//restrict constructor of this utility class
	private MailTemplateDefBL(){
	}

	public static TMailTemplateDefBean loadByTemplateTypeAndLocale(Integer templateID,boolean plain, Locale locale){
		String country = locale.getCountry();
		if (country!=null && country.length()>0) {
			List<TMailTemplateDefBean> mailTemplates = mailTemplateDefDAO.loadByTemplateTypeAndLocale(templateID, plain, locale.toString());
			if(mailTemplates!=null && !mailTemplates.isEmpty()){
				return mailTemplates.get(0);
			} else {
				LOGGER.trace("No mail template definition found for template " + templateID + " plain " + plain + " locale " + locale.toString());
			}
		}
		List<TMailTemplateDefBean> mailTemplates = mailTemplateDefDAO.loadByTemplateTypeAndLocale(templateID, plain, locale.getLanguage());
		if(mailTemplates!=null && !mailTemplates.isEmpty()){
			return mailTemplates.get(0);
		} else {
			LOGGER.trace("No mail template definition found for template " + templateID + " plain " + plain + " locale " + locale.getLanguage());
			mailTemplates = mailTemplateDefDAO.loadByTemplateTypeAndLocale(templateID, plain, "en");
			if(mailTemplates!=null && !mailTemplates.isEmpty()){
				return mailTemplates.get(0);
			}
		}
		return null;
	}
}
