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

package com.aurel.track.configExchange.impl;

import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.configExchange.IExchangeEntityNames;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityRelation;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDAO;
import com.aurel.track.dao.MailTemplateDefDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class MailTemplateExporter {
	private static MailTemplateDAO mailTemplateDAO = DAOFactory.getFactory().getMailTemplateDAO();
	private static MailTemplateDefDAO mailTemplateDefDAO = DAOFactory.getFactory().getMailTemplateDefDAO();

	public List<EntityContext> exportMailTemplates(List<Integer> mailTemplateIDList) {
		List<EntityContext> entityList = new ArrayList<EntityContext>();
		List<TMailTemplateBean> mailTemplateBeans =  mailTemplateDAO.loadByIDs(mailTemplateIDList);
		TMailTemplateBean mailTemplateBean;
		Iterator<TMailTemplateBean> it=mailTemplateBeans.iterator();
		while (it.hasNext()){
			mailTemplateBean=it.next();
			entityList.add(createContext(mailTemplateBean));
		}
		return entityList ;
	}
	public EntityContext createContext(TMailTemplateBean mailTemplateBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TMailTemplateBean");
		entityContext.setSerializableLabelBeans(mailTemplateBean);

		List<TMailTemplateDefBean> mailTemplateDefBeans =  mailTemplateDefDAO.loadByTemplate(mailTemplateBean.getObjectID());


		if(mailTemplateDefBeans!=null&&mailTemplateDefBeans.size()>0){
			EntityRelation defRelation = new EntityRelation("mailTemplateID");
			List<EntityContext> defEntityList = new ArrayList<EntityContext>();
			MailTemplateDefExporter mailTemplateDefExporter=new MailTemplateDefExporter();
			for(TMailTemplateDefBean mailTemplateDefBean : mailTemplateDefBeans) {
				EntityContext defEntity = mailTemplateDefExporter.createContext(mailTemplateDefBean);
				defEntityList.add(defEntity);
			}
			defRelation.setEntities(defEntityList);

			entityContext.addRelation(defRelation);
		}
		return entityContext;
	}
}
