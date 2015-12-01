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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityDependency;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;

/**
 */
public class ScreenConfigExporter {
	private static ScreenDAO screenDAO = DAOFactory.getFactory().getScreenDAO();

	public List<EntityContext> exportScreenConfigList(List<TScreenConfigBean> screenConfigBeanList,Map<String,Set<Integer>> exportedEntities,boolean includeFieldsConfig){
		List<EntityContext> entityList = new ArrayList<EntityContext>();
		if(screenConfigBeanList!=null&&screenConfigBeanList.size()>0){
			for(TScreenConfigBean screenConfigBean : screenConfigBeanList) {
				EntityContext entityFieldConfig = createScreenConfigContext(screenConfigBean,exportedEntities,includeFieldsConfig);
				entityList.add(entityFieldConfig);
			}
		}
		return entityList;
	}



	public EntityContext createScreenConfigContext(TScreenConfigBean screenConfigBean,Map<String,Set<Integer>> exportedEntities,boolean includeFieldsConfig) {
		EntityContext entity = new EntityContext();
		entity.setName("TScreenConfigBean");
		entity.setSerializableLabelBeans(screenConfigBean);


		//screen dependency
		//add only screen dependency, the rest of dependency should by already included
		Integer screenID=screenConfigBean.getScreen();
		EntityDependency dependency = new EntityDependency("TScreenBean",screenID, "screen");
		Set<Integer> screenExported=exportedEntities.get("TScreenBean");
		if(screenExported==null){
			screenExported=new HashSet<Integer>();
			exportedEntities.put("TScreenBean",screenExported);
		}
		boolean screeAlreadyAdded=screenExported.contains(screenID);
		if(screeAlreadyAdded){
			dependency.setEntityAdded(true);
		}else{
			ScreenExporter screenExporter=new ScreenExporter(includeFieldsConfig);
			TScreenBean screenBean = screenDAO.loadFullByPrimaryKey(screenID);
			dependency.setEntityContext(screenExporter.createContext(screenBean,exportedEntities));
			screenExported.add(screenID);
		}
		entity.addDependecy(dependency);


		//issueTypeDependency
		Integer issueTypeID=screenConfigBean.getIssueType();
		if(issueTypeID!=null){

			EntityDependency dependencyIssueType = new EntityDependency("TListTypeBean",issueTypeID, "issueType");
			TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(issueTypeID);


			Set<Integer> issueTypesExported=exportedEntities.get("TListTypeBean");
			if(issueTypesExported==null){
				issueTypesExported=new HashSet<Integer>();
				exportedEntities.put("TListTypeBean",issueTypesExported);
			}
			boolean issueTypeAlreadyAdded=issueTypesExported.contains(issueTypeID);
			if(issueTypeAlreadyAdded){
				dependencyIssueType.setEntityAdded(true);
			}else{
				IssueTypeExporter issueTypeExporter=new IssueTypeExporter();
				dependencyIssueType.setEntityContext(issueTypeExporter.createContext(listTypeBean));
				issueTypesExported.add(issueTypeID);
			}
			entity.addDependecy(dependencyIssueType);
		}
		return entity;
	}
}
