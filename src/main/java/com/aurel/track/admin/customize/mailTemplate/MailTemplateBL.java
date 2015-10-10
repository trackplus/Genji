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

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.configExchange.importer.EntityImporter;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.configExchange.importer.ImportResult;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDAO;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.LocaleHandler;

/**
 * Class to handle the logic for the mail template handling.
 * Mail templates consist of mail template definitions. There is
 * a definition for each language and for HTML and plain text e-mails.
 * Thus a single mail template file encompasses a number of
 * mail template definitions for each language and e-mail format.
 * The mail template definitions can be single files or are
 * enclosed in the mail template file.
 */
public class MailTemplateBL {

	private static MailTemplateDAO mailTemplateDAO = DAOFactory.getFactory().getMailTemplateDAO();

	//restrict constructor of this utility class
	private MailTemplateBL(){
	}
	
	public static List<TMailTemplateBean> getAllMailTemplates(){
		return  mailTemplateDAO.loadAll();
	}
	public static TMailTemplateBean getMailTemplate(Integer objectID){
		return mailTemplateDAO.loadByPrimaryKey(objectID);
	}

	public static List<ImportResult> importFile(File uploadFile,ImportContext importContext) throws EntityImporterException {
		EntityImporter entityImporter=new EntityImporter();
		return entityImporter.importFile(uploadFile,importContext);
	}
	public static List<ImportResult> importFile(InputStream is,ImportContext importContext) throws EntityImporterException {
		EntityImporter entityImporter = new EntityImporter();
		return entityImporter.importFile(is, importContext);
	}
	
	/**
	 * Return mail template for system events like registration, e-mail reminders, etc.
	 * @param event - IEventSubscriber.EVENT_XXX
	 * @param personBean
	 * @return
	 */
	public static TMailTemplateDefBean getSystemMailTemplateDefBean(Integer event, TPersonBean personBean) {
		Integer templateID=findMailTemplateID(event,null,null);
		if (templateID!=null) {
			Locale locale = null;
			String prefLocale = personBean.getPrefLocale();
			if (prefLocale!=null) {
				locale = LocaleHandler.getLocaleFromString(prefLocale);
			}			
			if (locale==null) {
				locale = Locale.getDefault();
			}
			Boolean plainEmail = false;
			if (personBean.getPrefEmailType() != null && !"HTML".equals(personBean.getPrefEmailType().toUpperCase().trim())) {
				plainEmail = true;
			}
			return MailTemplateDefBL.loadByTemplateTypeAndLocale(templateID,plainEmail,locale);
		} else {
			return null;
		}
	}

	public static Integer findMailTemplateID(Integer eventID, Integer projectID, Integer issueTypeID) {
		TMailTemplateConfigBean mailTemplateConfigBean=null;
			mailTemplateConfigBean=MailTemplateConfigBL.loadDefault(eventID);
		if (mailTemplateConfigBean!=null) {
			return mailTemplateConfigBean.getMailTemplate();
		}
		return null;
	}
}
