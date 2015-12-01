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

import com.aurel.track.beans.TPpriorityBean;
import com.aurel.track.configExchange.importer.AbstractEntityImporter;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PPriorityDAO;

import java.util.List;
import java.util.Map;

/**
 */
public class PpriorityImporter extends AbstractEntityImporter<TPpriorityBean> {
	private final static PPriorityDAO dao = DAOFactory.getFactory().getPPriorityDAO();
	@Override
	public TPpriorityBean createInstance(Map<String, String> attributes) {
		TPpriorityBean bean=new TPpriorityBean();
		bean=(TPpriorityBean)bean.deserializeBean(attributes);
		return bean;
	}

	public Integer save(TPpriorityBean bean) {
		return dao.save(bean);
	}
	protected List<TPpriorityBean> loadSimilar(TPpriorityBean bean){
		return dao.loadAll();
	}
}
