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


package com.aurel.track.util.event;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.mailTemplate.MailTemplateDefBL;
import com.aurel.track.beans.TMailTemplateDefBean;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Cache for e-mail templates
 * @author Tamas
 *
 */
public class EmailTemplatesContainer {
	private static final Logger LOGGER = LogManager.getLogger(EmailTemplatesContainer.class);
	
	protected static Map<Integer, Map<Boolean, Map<String, MailPartTemplates>>> templateTypeToPlainOrHtmlToLocaleToSubjectOrBodyToTemplate = 
			new HashMap<Integer,  Map<Boolean, Map<String, MailPartTemplates>>>();
	
	/**
	 * Gets the localized lookups for a list
	 * @param locale
	 * @param fieldID
	 * @return
	 */
	public static MailPartTemplates getMailPartTemplates(Integer templateID, boolean plain, Locale locale) {
		Map<Boolean, Map<String, MailPartTemplates>> templateMap = templateTypeToPlainOrHtmlToLocaleToSubjectOrBodyToTemplate.get(templateID);
		if (templateMap==null) {
			templateMap = new HashMap<Boolean, Map<String, MailPartTemplates>>();
			templateTypeToPlainOrHtmlToLocaleToSubjectOrBodyToTemplate.put(templateID, templateMap);
		}
		Boolean plainValue = Boolean.valueOf(plain);
		Map<String, MailPartTemplates> plainHtmlMap = templateMap.get(plainValue);
		if (plainHtmlMap==null) {
			plainHtmlMap = new HashMap<String, MailPartTemplates>();
			templateMap.put(plainValue, plainHtmlMap);
		}
		MailPartTemplates mailPartTemplates = plainHtmlMap.get(locale.toString());
		if (mailPartTemplates==null) {
			mailPartTemplates = new MailPartTemplates();
			TMailTemplateDefBean mailTemplateDefBean = MailTemplateDefBL.loadByTemplateTypeAndLocale(templateID, plain, locale);
			if (mailTemplateDefBean!=null) {
				mailPartTemplates.setSubjectTemplate(getSubjectTemplate(mailTemplateDefBean));
				mailPartTemplates.setBodyTemplate(getBodyTemplate(mailTemplateDefBean));
				plainHtmlMap.put(locale.toString(), mailPartTemplates);
			}
		}
		return mailPartTemplates;
	}

	/**
	 * Resets the localized for a field and locale
	 * @param fieldID
	 * @return
	 */
	public static void resetTemplate(Integer templateID) {
		Map<Boolean, Map<String, MailPartTemplates>> templateMap = templateTypeToPlainOrHtmlToLocaleToSubjectOrBodyToTemplate.get(templateID);
		if (templateMap!=null) {
			templateMap.clear();
			LOGGER.debug("Resetting the mail template " + templateID);
		}
	}
	
	/**
	 * Resets all lists
	 * @return
	 */
	public static void resetAllTemplates() {
		templateTypeToPlainOrHtmlToLocaleToSubjectOrBodyToTemplate.clear();
		LOGGER.debug("Resetting all mail templates");
	}
	
	/**
	 * Gets the localized template
	 * @return
	 */
	public static Template getBodyTemplate(TMailTemplateDefBean mailTemplateDefBean) {
		String templateStr=mailTemplateDefBean.getMailBody();
		Template bodyTemplate=null;
		try {
			if (templateStr!=null) {
				bodyTemplate = new Template("body", new StringReader(templateStr),new Configuration());
			}
		} catch (IOException e) {
			LOGGER.debug("Loading the body template failed with " + e.getMessage(), e);
		}
		return bodyTemplate;
	}
	
	/**
	 * Gets the localized template
	 * @return
	 */
	public static Template getSubjectTemplate(TMailTemplateDefBean mailTemplateDefBean) {
		String templateStr=mailTemplateDefBean.getMailSubject();
		Template subjectTemplate=null;
		try {
			if (templateStr!=null) {
				subjectTemplate = new Template("subject", new StringReader(templateStr),new Configuration());
			}
		} catch (IOException e) {
			LOGGER.debug("Loading the subject template failed with " + e.getMessage(), e);
		}
		return subjectTemplate;
	}
}
