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

import com.aurel.track.beans.TRoleBean;
import com.aurel.track.configExchange.importer.AbstractEntityImporter;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.RoleDAO;

import java.util.List;
import java.util.Map;

/**
 */
public class RoleImporter extends AbstractEntityImporter<TRoleBean> {
	private final static RoleDAO roleDAO = DAOFactory.getFactory().getRoleDAO();
	public TRoleBean createInstance(Map<String, String> attributes) {
		TRoleBean bean=new TRoleBean();
		bean=(TRoleBean)bean.deserializeBean(attributes);
		return bean;
	}

	public Integer save(TRoleBean bean) {
		return roleDAO.save(bean);
	}
	protected List<TRoleBean> loadSimilar(TRoleBean bean){
		return roleDAO.loadAll();
	}
}

