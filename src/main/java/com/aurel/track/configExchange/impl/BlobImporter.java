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

import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.beans.TBLOBBean;
import com.aurel.track.configExchange.importer.AbstractEntityImporter;

import java.util.List;
import java.util.Map;

public class BlobImporter extends AbstractEntityImporter<TBLOBBean> {
	@Override
	public TBLOBBean createInstance(Map<String, String> attributes) {
		TBLOBBean bean=new TBLOBBean();
		bean=(TBLOBBean)bean.deserializeBean(attributes);
		return bean;
	}

	public Integer save(TBLOBBean bean) {
		return BlobBL.save(bean);
	}
	protected List<TBLOBBean> loadSimilar(TBLOBBean bean){
		return BlobBL.loadAll();
	}
}
