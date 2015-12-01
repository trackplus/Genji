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
import com.aurel.track.beans.TBLOBBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityDependency;


/**
 */
public class IssueTypeExporter {
	public EntityContext createContext(TListTypeBean listTypeBean){
		EntityContext entityContext = new EntityContext();
		entityContext.setName("TListTypeBean");
		entityContext.setSerializableLabelBeans(listTypeBean);

		//dependency
		Integer iconKey=listTypeBean.getIconKey();
		if(iconKey!=null){
			EntityDependency dependency = new EntityDependency("TBLOBBean", iconKey, "iconKey");
			TBLOBBean blobBean = BlobBL.loadByPrimaryKey(iconKey);
			BLOBExporter blobExporter=new BLOBExporter();
			dependency.setEntityContext(blobExporter.createContext(blobBean));
			entityContext.addDependecy(dependency);
		}
		return entityContext;
	}
}
