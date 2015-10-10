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

import java.util.List;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.configExchange.importer.AbstractEntityImporter;

/**
 */
public class FieldImporter extends AbstractEntityImporter<TFieldBean> {
	public TFieldBean createInstance(Map<String, String> attributes) {
		TFieldBean bean=new TFieldBean();
		bean=(TFieldBean)bean.deserializeBean(attributes);
		return bean;
	}

	public Integer save(TFieldBean bean) {
		return FieldBL.save(bean);
	}
	protected List<TFieldBean> loadSimilar(TFieldBean bean){
		return FieldBL.loadAll();
	}
}
