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
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.configExchange.importer.AbstractEntityImporter;
import com.aurel.track.configExchange.importer.IEntityImporter;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDAO;
import com.aurel.track.dao.MailTemplateDefDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Import MailTemplateBean from XML
 * @author  Adrian Bojani
 * @since 4.0.0
 */
public class MailTemplateImporter extends AbstractEntityImporter<TMailTemplateBean> {
	private final static MailTemplateDAO mailTemplateDAO = DAOFactory.getFactory().getMailTemplateDAO();
	private final static MailTemplateDefDAO mailTemplateDefDAO = DAOFactory.getFactory().getMailTemplateDefDAO();

	public TMailTemplateBean createInstance(Map<String, String> attributes) {
		TMailTemplateBean bean=new TMailTemplateBean();
		bean.deserializeBean(attributes);
		return bean;
	}

	public Integer save(TMailTemplateBean bean) {
		return mailTemplateDAO.save(bean);
	}
	protected  List<TMailTemplateBean> loadSimilar(TMailTemplateBean bean){
		return mailTemplateDAO.loadAll();
	}
	public boolean clearChildren(Integer parentID){
		List<TMailTemplateDefBean> defBeans=mailTemplateDefDAO.loadByTemplate(parentID);
		if(defBeans!=null&&defBeans.size()>0){
			for(TMailTemplateDefBean bean:defBeans){
				mailTemplateDefDAO.delete(bean.getObjectID());
			}
		}
		return true;
	}
}
