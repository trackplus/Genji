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

import java.util.*;

import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.beans.TBLOBBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityDependency;
import com.aurel.track.configExchange.exporter.EntityRelation;

/**
 */
public class ListExporter {
	
	public EntityContext createContext(TListBean listBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TListBean");
		entityContext.setSerializableLabelBeans(listBean);

		Integer listID=listBean.getObjectID();

		List<TOptionBean> optionBeans = OptionBL.loadByListID(listID);
		if(optionBeans!=null&&optionBeans.size()>0){
			EntityRelation optionRelation = new EntityRelation("list");
			List<EntityContext> optionEntityList = new ArrayList<EntityContext>();
			for(TOptionBean optionBean : optionBeans) {
				EntityContext optionEntity = createOptionContext(optionBean);
				optionEntityList.add(optionEntity);
			}
			optionRelation.setEntities(optionEntityList);
			entityContext.addRelation(optionRelation);
		}

		List<TListBean> childrenList=ListBL.loadByParent(listID);
		if(childrenList!=null&&childrenList.size()>0){
			EntityRelation childListRelation = new EntityRelation("parentList");
			List<EntityContext> childListEntityList = new ArrayList<EntityContext>();
			for(TListBean l : childrenList) {
				EntityContext childListEntity = createContext(l);
				childListEntityList.add(childListEntity);
			}
			childListRelation.setEntities(childListEntityList);
			entityContext.addRelation(childListRelation);
		}



		return entityContext;
	}

	public EntityContext createOptionContext(TOptionBean optionBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TOptionBean");
		entityContext.setSerializableLabelBeans(optionBean);

		Integer parentOption=optionBean.getParentOption();
		if(parentOption!=null){
			EntityDependency parentOptionDependency = new EntityDependency("TOptionBean",parentOption,"parentOption");
			parentOptionDependency.setEntityAdded(true);
			entityContext.addDependecy(parentOptionDependency);
		}

		//dependency
		Integer iconKey=optionBean.getIconKey();
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
