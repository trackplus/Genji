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

import com.aurel.track.beans.TMailTemplateDefBean;

import com.aurel.track.configExchange.importer.AbstractEntityImporter;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateDAO;
import com.aurel.track.dao.MailTemplateDefDAO;
import com.aurel.track.fieldType.constants.BooleanFields;

import java.util.List;
import java.util.Map;

/**
 * Import MailTemplateDefBean from XML
 * @author  Adrian Bojani
 * @since 4.0.0
 */
public class MailTemplateDefImporter  extends AbstractEntityImporter<TMailTemplateDefBean> {
	private final static MailTemplateDefDAO mailTemplateDefDAO = DAOFactory.getFactory().getMailTemplateDefDAO();

	public TMailTemplateDefBean createInstance(Map<String, String> attributes) {
		TMailTemplateDefBean bean=new TMailTemplateDefBean();
		bean.deserializeBean(attributes);
		return bean;
	}

	public Integer save(TMailTemplateDefBean bean) {
		return mailTemplateDefDAO.save(bean);
	}
	protected  List<TMailTemplateDefBean> loadSimilar(TMailTemplateDefBean bean){
		return mailTemplateDefDAO.loadAll();
	}

	@Override
	public boolean isChanged(TMailTemplateDefBean bean) {
		return BooleanFields.TRUE_VALUE.equals(bean.getTemplateChanged());
	}
}
