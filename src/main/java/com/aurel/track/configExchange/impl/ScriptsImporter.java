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

import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.configExchange.importer.AbstractEntityImporter;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScriptsDAO;

import java.util.List;
import java.util.Map;

/**
 */
public class ScriptsImporter extends AbstractEntityImporter<TScriptsBean> {
	private static ScriptsDAO scriptsDAO= DAOFactory.getFactory().getScriptsDAO();
	public TScriptsBean createInstance(Map<String, String> attributes) {
		TScriptsBean bean=new TScriptsBean();
		bean.deserializeBean(attributes);
		return bean;
	}

	public Integer save(TScriptsBean bean) {
		return scriptsDAO.saveScript(bean);
	}
	protected List<TScriptsBean> loadSimilar(TScriptsBean bean){
		return scriptsDAO.getAllScripts();
	}
	public boolean clearChildren(Integer parentID){
		return true;
	}
}
