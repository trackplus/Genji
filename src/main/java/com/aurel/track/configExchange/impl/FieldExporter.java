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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityRelation;

/**
 *
 */
public class FieldExporter {
	public EntityContext createContext(TFieldBean fieldBean){
		return createContext(fieldBean,true,new HashMap<String, Set<Integer>>());
	}
	public EntityContext createContext(TFieldBean fieldBean,boolean includeConfig,Map<String,Set<Integer>> exportedEntities){
		EntityContext fieldEntity = new EntityContext();
		fieldEntity.setName("TFieldBean");
		fieldEntity.setSerializableLabelBeans(fieldBean);
		if(includeConfig){
			FieldConfigExporter fieldConfigExporter=new FieldConfigExporter();
			EntityRelation fieldConfigRelation = new EntityRelation("field");
			List<EntityContext> fieldConfigEntityList = new ArrayList<EntityContext>();
			TFieldConfigBean fieldConfigBean=FieldConfigBL.loadDefault(fieldBean.getObjectID());
			EntityContext fieldConfigContext = fieldConfigExporter.createFieldConfigContext(fieldConfigBean,false,exportedEntities);
			fieldConfigEntityList.add(fieldConfigContext);
			fieldConfigRelation.setEntities(fieldConfigEntityList);
			fieldEntity.addRelation(fieldConfigRelation);
		}
		return fieldEntity;
	}



}
