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

package com.aurel.track.admin.customize.mailTemplateEdit;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.configExchange.exporter.EntityExporterException;
import com.aurel.track.dao.MailTemplateDAO;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityExporter;
import com.aurel.track.configExchange.impl.MailTemplateDefExporter;
import com.aurel.track.configExchange.importer.EntityImporter;
import com.aurel.track.configExchange.importer.EntityImporterException;
import com.aurel.track.configExchange.importer.ImportContext;
import com.aurel.track.configExchange.importer.ImportResult;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDefDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.LocaleHandler;

/**
 */
public class MailTemplateDefEditBL {

	private static MailTemplateDefDAO mailTemplateDefDAO = DAOFactory.getFactory().getMailTemplateDefDAO();
	private static MailTemplateDAO mailTemplateDAO = DAOFactory.getFactory().getMailTemplateDAO();
	private static final Logger LOGGER = LogManager.getLogger(MailTemplateDefEditBL.class);

	//restrict constructor of this utility class
	private MailTemplateDefEditBL(){
	}

	static List<TMailTemplateDefBean> getMailTemplateDefs(Integer templateID){
		return mailTemplateDefDAO.loadByTemplate(templateID);
	}

	static Map<String, String> getLocaleMap(){
		List<LabelValueBean> localeList = LocaleHandler.getAvailableLocales();
		Map<String, String> localeMap = new HashMap<String, String>();
		for (LabelValueBean labelValueBean : localeList) {
			localeMap.put(labelValueBean.getValue(), labelValueBean.getLabel());
		}
		return localeMap;
	}

	/**
	 * Loads the details for a template
	 * @param objectID
	 * @return
	 */
	static String loadTemplateDef(Integer objectID) {
		List<LabelValueBean> localeList = LocaleHandler.getAvailableLocales();
		TMailTemplateDefBean mailTemplateDefBean = null;
		if (objectID!=null) {
			mailTemplateDefBean = mailTemplateDefDAO.loadByPrimaryKey(objectID);
		}
		if (mailTemplateDefBean==null) {
			mailTemplateDefBean = new TMailTemplateDefBean();
			//set template to the first (template type is required, locale is not required)
			mailTemplateDefBean.setIsPlainEmailBool(false);
		}
		return MailTemplatesJSON.encodeMailTemplateDefJSON(mailTemplateDefBean, localeList);
	}

	/**
	 * Saves a mail template
	 * @param copy
	 * @param objectID
	 * @param templateID
	 * @param plain
	 * @param theLocale
	 * @param mailSubject
	 * @param mailBody
	 * @param locale
	 * @return
	 */
	static String saveTemplateDef(boolean copy,Integer templateID,Integer objectID, boolean plain, String theLocale,
								  String mailSubject, String mailBody, Locale locale) {
		TMailTemplateDefBean mailTemplateDefBean = null;
		if (objectID!=null && !copy) {
			LOGGER.info("Load by primary key=" + objectID.toString());
			mailTemplateDefBean = mailTemplateDefDAO.loadByPrimaryKey(objectID);
		}
		List<TMailTemplateDefBean> mailTemplates = mailTemplateDefDAO.loadByTemplateTypeAndLocale(templateID,plain, theLocale);
		if(mailTemplates!=null && !mailTemplates.isEmpty()) {
			TMailTemplateDefBean existingMailTemplateBean = mailTemplates.get(0);
			LOGGER.info("Existing TemplateBean ID=" + existingMailTemplateBean.getObjectID().toString());
			if (objectID==null || copy || !existingMailTemplateBean.getObjectID().equals(objectID)) {
				return JSONUtility.encodeJSONFailure(
						LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.customize.mailTemplate.err.existingTemplate", locale));
			}
		}
		if(mailTemplateDefBean==null) {
			mailTemplateDefBean = new TMailTemplateDefBean();
		}
		LOGGER.debug("setting data");
		mailTemplateDefBean.setMailTemplate(templateID);
		mailTemplateDefBean.setTheLocale(theLocale);
		mailTemplateDefBean.setIsPlainEmailBool(plain);
		mailTemplateDefBean.setMailSubject(mailSubject);
		mailTemplateDefBean.setMailBody(mailBody);
		mailTemplateDefBean.setTemplateChanged(BooleanFields.TRUE_VALUE); // mark it private
		mailTemplateDefDAO.save(mailTemplateDefBean);
		LOGGER.debug("saving data");
		return JSONUtility.encodeJSONSuccess();
	}
	static void deleteTemplateDef(String mailTemplateIDs) {
		List<Integer> ids=GeneralUtils.createIntegerListFromString(mailTemplateIDs);
		for(Integer id:ids){
			deleteTemplateDef(id);
		}
	}
	/**
	 * Deletes a mail template
	 * @param objectID
	 * @return
	 */
	static void deleteTemplateDef(Integer objectID) {
		mailTemplateDefDAO.delete(objectID);
	}

	static String createDOM(List<Integer> mailTemplateDefIDList) throws EntityExporterException {
		MailTemplateDefExporter mailTemplateDefExporter=new MailTemplateDefExporter();
		List<EntityContext> entityContextList=mailTemplateDefExporter.exportMailTemplateDefs(mailTemplateDefIDList);
		String dom= EntityExporter.export2(entityContextList);
		return dom;
	}

	public static String getExportFileName(List<Integer> mailTemplateDefIDList){
		if(mailTemplateDefIDList.size()==1){
			TMailTemplateDefBean mailTemplateDefBean =  mailTemplateDefDAO.loadByPrimaryKey(mailTemplateDefIDList.get(0));
			if(mailTemplateDefBean!=null){
				Integer mailTemplateID=mailTemplateDefBean.getMailTemplate();
				TMailTemplateBean mailTemplateBean =  mailTemplateDAO.loadByPrimaryKey(mailTemplateID);
				String type=mailTemplateDefBean.isPlainEmail()?"plain":"HTML";
				return "MAIL_TMPL_DEF-"+mailTemplateBean.getName()+"_"+mailTemplateDefBean.getTheLocale()+"_"+type+".xml";
			}
		}
		return "MailTemplateDefs.xml";
	}


	static List<ImportResult> importFile(File uploadFile,Integer templateID, boolean overwriteExisting) throws EntityImporterException {
		EntityImporter entityImporter = new EntityImporter();
		String type="TMailTemplateDefBean";
		String parentAttributeName="mailTemplateID";
		Map<String,String> extraAttributes=new HashMap<String, String>();
		extraAttributes.put(parentAttributeName,templateID==null?null:templateID.toString());
		ImportContext importContext=new ImportContext();
		importContext.setEntityType(type);
		importContext.setOverrideExisting(overwriteExisting);
		importContext.setClearChildren(false);
		importContext.setAttributeMap(extraAttributes);
		return entityImporter.importFile(uploadFile, importContext);
	}
}
