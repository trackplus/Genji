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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.TextBoxSettingsBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityDependency;
import com.aurel.track.configExchange.exporter.EntityRelation;
import com.aurel.track.fieldType.runtime.base.LookupContainer;

/**
 */
public class FieldConfigExporter {
	
	public List<EntityContext> exportFieldConfigList(List<TFieldConfigBean> fieldConfigList,Map<String,Set<Integer>> exportedEntities){
		List<EntityContext> entityList = new ArrayList<EntityContext>();
		if(fieldConfigList!=null&&fieldConfigList.size()>0){
			for(TFieldConfigBean fieldConfigBean : fieldConfigList) {
				EntityContext entityFieldConfig = createFieldConfigContext(fieldConfigBean,true, exportedEntities);
				entityList.add(entityFieldConfig);
			}
		}
		return entityList;
	}
	public EntityContext createFieldConfigContext(TFieldConfigBean fieldConfigBean,boolean includeField,Map<String,Set<Integer>> exportedEntities){
		EntityContext entity = new EntityContext();
		entity.setName("TFieldConfigBean");
		entity.setSerializableLabelBeans(fieldConfigBean);
		Integer configID=fieldConfigBean.getObjectID();
		Set<Integer> fieldConfigExportedSet=exportedEntities.get("TFieldConfigBean");
		if(fieldConfigExportedSet==null){
			fieldConfigExportedSet=new HashSet<Integer>();
			exportedEntities.put("TFieldConfigBean",fieldConfigExportedSet);
		}
		fieldConfigExportedSet.add(configID);

		//general settings
		List<TGeneralSettingsBean> generalSettingsBeanList=GeneralSettingsBL.loadByConfig(configID);
		if(generalSettingsBeanList!=null&&generalSettingsBeanList.size()>0){
			EntityRelation generalSettingRelation = new EntityRelation("config");
			List<EntityContext> generalSettingEntityList = new ArrayList<EntityContext>();
			for(TGeneralSettingsBean generalSettingsBean : generalSettingsBeanList) {
				EntityContext generalSettingsEntity = createGeneralSettingContext(generalSettingsBean);
				generalSettingEntityList.add(generalSettingsEntity);
			}
			generalSettingRelation.setEntities(generalSettingEntityList);

			entity.addRelation(generalSettingRelation);
		}

		//textBox settings
		List<TTextBoxSettingsBean> textBoxSettingsBeanList=TextBoxSettingsBL.loadByConfig(configID);
		if(textBoxSettingsBeanList!=null&&textBoxSettingsBeanList.size()>0){
			EntityRelation textBoxSettingRelation = new EntityRelation("config");
			List<EntityContext> textBoxSettingEntityList = new ArrayList<EntityContext>();
			for(TTextBoxSettingsBean textBoxSettingsBean : textBoxSettingsBeanList) {
				EntityContext generalSettingsEntity = createTextBoxSettingContext(textBoxSettingsBean);
				textBoxSettingEntityList.add(generalSettingsEntity);
			}
			textBoxSettingRelation.setEntities(textBoxSettingEntityList);

			entity.addRelation(textBoxSettingRelation);
		}
		//option
		TOptionSettingsBean optionSettingsBeanList=OptionSettingsBL.loadByConfigAndParameter(configID, null);
		if(optionSettingsBeanList!=null){
			EntityRelation optionSettingRelation = new EntityRelation("config");
			List<EntityContext> optionSettingEntityList = new ArrayList<EntityContext>();
			EntityContext optionSettingsEntity = createOptionSettingContext(optionSettingsBeanList);
			optionSettingEntityList.add(optionSettingsEntity);

			optionSettingRelation.setEntities(optionSettingEntityList);

			entity.addRelation(optionSettingRelation);
		}
		Set<Integer> fieldExported=exportedEntities.get("TFieldBean");
		if(fieldExported==null){
			fieldExported=new HashSet<Integer>();
			exportedEntities.put("TFieldBean",fieldExported);
		}

		Integer fieldID=fieldConfigBean.getField();
		EntityDependency fieldDependency = new EntityDependency("TFieldBean",fieldID,"field");
		boolean fieldAlreadyAdded=!includeField||fieldExported.contains(fieldID);
		if(fieldAlreadyAdded){
			fieldDependency.setEntityAdded(true);
		}else{
			TFieldBean fieldBean = FieldBL.loadByPrimaryKey(fieldID);
			FieldExporter fieldExporter=new FieldExporter();
			fieldDependency.setEntityContext(fieldExporter.createContext(fieldBean,false,exportedEntities));
			fieldExported.add(fieldID);
		}
		entity.addDependecy(fieldDependency);
		Integer issueTypeID=fieldConfigBean.getIssueType();
		if(issueTypeID!=null){
			EntityDependency issueTypedDependency = new EntityDependency("TListTypeBean",issueTypeID,"issueTypeID");
			Set<Integer> issueTypeExportedSet=exportedEntities.get("TFieldConfigBean");
			if(issueTypeExportedSet==null){
				issueTypeExportedSet=new HashSet<Integer>();
				exportedEntities.put("TFieldConfigBean",issueTypeExportedSet);
			}
			boolean issueTypeAlreadyAdded=issueTypeExportedSet.contains(issueTypeID);
			if(issueTypeAlreadyAdded){
				issueTypedDependency.setEntityAdded(true);
			}else{
				TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(issueTypeID);
				IssueTypeExporter issueTypeExporter=new IssueTypeExporter();
				issueTypedDependency.setEntityContext(issueTypeExporter.createContext(listTypeBean));
			}
			entity.addDependecy(issueTypedDependency);
		}
		return entity;
	}
	private EntityContext createGeneralSettingContext(TGeneralSettingsBean generalSettingsBean){
		EntityContext entity = new EntityContext();
		entity.setName("TGeneralSettingsBean");
		entity.setSerializableLabelBeans(generalSettingsBean);
		return entity;
	}
	private EntityContext createTextBoxSettingContext(TTextBoxSettingsBean textBoxSettingsBean){
		EntityContext entity = new EntityContext();
		entity.setName("TTextBoxSettingsBean");
		entity.setSerializableLabelBeans(textBoxSettingsBean);
		return entity;
	}
	private EntityContext createOptionSettingContext(TOptionSettingsBean optionSettingsBean){
		EntityContext entity = new EntityContext();
		entity.setName("TOptionSettingsBean");
		entity.setSerializableLabelBeans(optionSettingsBean);

		//list
		Integer listID=optionSettingsBean.getList();
		if(listID!=null){
			TListBean listBean=ListBL.loadByPrimaryKey(listID);
			if(listBean!=null){
				ListExporter listExporter=new ListExporter();
				EntityDependency listDependency = new EntityDependency("TListBean",listID,"list");
				EntityContext listEntity = listExporter.createContext(listBean);
				listDependency.setEntityContext(listEntity);
				entity.addDependecy(listDependency);
			}
		}
		return entity;
	}
}
