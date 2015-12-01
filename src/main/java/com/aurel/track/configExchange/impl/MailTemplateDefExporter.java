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

package com.aurel.track.configExchange.impl;

import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.configExchange.IExchangeEntityNames;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDefDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class MailTemplateDefExporter {
	private static MailTemplateDefDAO mailTemplateDefDAO = DAOFactory.getFactory().getMailTemplateDefDAO();

	public List<EntityContext> exportMailTemplateDefs(List<Integer> mailTemplateDefIDList) {
		List<EntityContext> entityList = new ArrayList<EntityContext>();
		List<TMailTemplateDefBean> mailTemplateDefBeans =  mailTemplateDefDAO.loadByIDs(mailTemplateDefIDList);
		TMailTemplateDefBean mailTemplateDefBean;
		Iterator<TMailTemplateDefBean> it=mailTemplateDefBeans.iterator();
		while (it.hasNext()){
			mailTemplateDefBean=it.next();
			entityList.add(createContext(mailTemplateDefBean));
		}
		return entityList ;
	}

	public EntityContext createContext(TMailTemplateDefBean mailTemplateDefBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TMailTemplateDefBean");
		entityContext.setSerializableLabelBeans(mailTemplateDefBean);
		return entityContext;
	}
}
