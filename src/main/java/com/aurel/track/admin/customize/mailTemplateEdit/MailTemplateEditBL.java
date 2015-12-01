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

package com.aurel.track.admin.customize.mailTemplateEdit;

import java.util.List;
import java.util.Locale;


import com.aurel.track.configExchange.exporter.EntityExporterException;

import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityExporter;
import com.aurel.track.configExchange.impl.MailTemplateExporter;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDAO;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.admin.customize.mailTemplate.MailTemplateBL;

/**
 * Class to handle the logic for the mail template handling.
 * Mail templates consist of mail template definitions. There is
 * a definition for each language and for HTML and plain text e-mails.
 * Thus a single mail template file encompasses a number of
 * mail template definitions for each language and e-mail format.
 * The mail template definitions can be single files or are
 * enclosed in the mail template file.
 */
public class MailTemplateEditBL {

	private static MailTemplateDAO mailTemplateDAO = DAOFactory.getFactory().getMailTemplateDAO();

	//restrict constructor of this utility class
	private MailTemplateEditBL(){
	}
	
	static String createDOM(List<Integer> mailTemplateIDList) throws EntityExporterException {
		MailTemplateExporter mailTemplateExporter=new MailTemplateExporter();
		List<EntityContext> entityContextList=mailTemplateExporter.exportMailTemplates(mailTemplateIDList);
		return EntityExporter.export2(entityContextList);
	}
	public static String getExportFileName(List<Integer> mailTemplateIDList){
		if(mailTemplateIDList.size()==1){
			TMailTemplateBean mailTemplateBean =  mailTemplateDAO.loadByPrimaryKey(mailTemplateIDList.get(0));
			if(mailTemplateBean!=null){
				return "MAIL_TMPL-"+mailTemplateBean.getName()+".xml";
			}
		}
		return "MailTemplates.xml";
	}

	static Integer saveMailTemplate(TMailTemplateBean mailTemplateBean){
		return mailTemplateDAO.save(mailTemplateBean);
	}

	static Integer createNewTemplate(String name,String tagLabel,String description){
		TMailTemplateBean mailTemplateBean=new TMailTemplateBean();
		mailTemplateBean.setName(name);
		mailTemplateBean.setTagLabel(tagLabel);
		mailTemplateBean.setDescription(description==null?mailTemplateBean.getDescription():description);
		return saveMailTemplate(mailTemplateBean);
	}

	static Integer copyTemplate(Integer objectID, String name, String tagLabel, String description, Locale locale){
		TMailTemplateBean mailTemplateBean=MailTemplateBL.getMailTemplate(objectID);
		TMailTemplateBean clone=new TMailTemplateBean();
		clone.setName(name);
		clone.setTagLabel(tagLabel);
		clone.setDescription(description==null?mailTemplateBean.getDescription():description);
		Integer copyedTemplateID = saveMailTemplate(clone);
		List<TMailTemplateDefBean> mailTemplateDefs = MailTemplateDefEditBL.getMailTemplateDefs(objectID);
		if (mailTemplateDefs!=null) {
			for (TMailTemplateDefBean mailTemplateDefBean : mailTemplateDefs) {
				MailTemplateDefEditBL.saveTemplateDef(true, copyedTemplateID, null, mailTemplateDefBean.isPlainEmail(),
						mailTemplateDefBean.getTheLocale(), mailTemplateDefBean.getMailSubject(), mailTemplateDefBean.getMailBody(), locale);
			}
		}
		return copyedTemplateID;
	}


	/**
	 * Deletes a mail template
	 * @param objectID
	 * @return
	 */
	static void delete(Integer objectID) {
		mailTemplateDAO.delete(objectID);
	}
}
