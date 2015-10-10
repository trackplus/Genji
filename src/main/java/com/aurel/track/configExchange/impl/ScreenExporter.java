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

import java.util.*;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.*;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityDependency;
import com.aurel.track.configExchange.exporter.EntityRelation;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenConfigDAO;
import com.aurel.track.dao.ScreenDAO;
import org.apache.commons.collections.map.HashedMap;

/**
 */
public class ScreenExporter{

	private static ScreenDAO screenDAO = DAOFactory.getFactory().getScreenDAO();

	private boolean includeFieldConfig=true;
	public ScreenExporter(boolean includeFieldConfig){
		this.includeFieldConfig=includeFieldConfig;
	}
	public List<EntityContext> exportScreens(List<Integer> screenIDList) {
		Map<String,Set<Integer>> exportedEntities=new HashMap<String, Set<Integer>>();
		List<EntityContext> screenEntityList = new ArrayList<EntityContext>();
		for (Integer screenId : screenIDList) {
			TScreenBean screenBean = screenDAO.loadFullByPrimaryKey(screenId);
			screenEntityList.add(createContext(screenBean,exportedEntities));
		}
		return screenEntityList ;
	}

	public EntityContext createContext(TScreenBean screenBean,Map<String,Set<Integer>> exportedEntities) {
		EntityContext screenEntity = new EntityContext();
		screenEntity.setName("TScreenBean");
		screenEntity.setSerializableLabelBeans(screenBean);
		//children
		List<ITab> screenTabs = screenBean.getTabs();
		if(screenTabs!=null&&screenTabs.size()>0){
			EntityRelation screenTabRelation = new EntityRelation("parent");
			List<EntityContext> screenTabEntityList = new ArrayList<EntityContext>();
			for(ITab screenTab : screenTabs) {
				EntityContext screenTabEntity = createTabContext((TScreenTabBean) screenTab,exportedEntities);
				screenTabEntityList.add(screenTabEntity);
			}
			screenTabRelation.setEntities(screenTabEntityList);

			screenEntity.addRelation(screenTabRelation);
		}
		return screenEntity;
	}

	private EntityContext createTabContext(TScreenTabBean screenTab,Map<String,Set<Integer>> exportedEntities){
		EntityContext screenTabEntity = new EntityContext();
		screenTabEntity.setName("TScreenTabBean");
		screenTabEntity.setSerializableLabelBeans( screenTab);
		//children
		List<IPanel> panels=screenTab.getPanels();
		if(panels!=null&&panels.size()>0){
			EntityRelation screenPanelRelation = new EntityRelation("parent");
			List<EntityContext> screenPanelEntityList = new ArrayList<EntityContext>();
			for(IPanel screenPanel : panels) {
				EntityContext screenPanelEntity = createPanelContext((TScreenPanelBean) screenPanel,exportedEntities);
				screenPanelEntityList.add(screenPanelEntity);
			}
			screenPanelRelation.setEntities(screenPanelEntityList);

			screenTabEntity.addRelation(screenPanelRelation);
		}
		return screenTabEntity;
	}

	private EntityContext createPanelContext(TScreenPanelBean screenPanel,Map<String,Set<Integer>> exportedEntities){
		EntityContext screenPanelEntity = new EntityContext();
		screenPanelEntity.setName("TScreenPanelBean");
		screenPanelEntity.setSerializableLabelBeans(screenPanel);
		//children
		List<IField> screenFields = screenPanel.getFields();
		if(screenFields!=null&&screenFields.size()>0){
			EntityRelation screenFieldRelation = new EntityRelation("parent");
			List<EntityContext> screenFieldEntityList = new ArrayList<EntityContext>();
			for(IField screenField : screenFields) {
				EntityContext screenFieldEntity = createScreenFieldContext((TScreenFieldBean) screenField,exportedEntities);
				screenFieldEntityList.add(screenFieldEntity);
			}
			screenFieldRelation.setEntities(screenFieldEntityList);

			screenPanelEntity.addRelation(screenFieldRelation);
		}
		return screenPanelEntity;
	}

	private EntityContext createScreenFieldContext(TScreenFieldBean screenField,Map<String,Set<Integer>> exportedEntities){
		EntityContext screenFieldEntity = new EntityContext();
		screenFieldEntity.setName("TScreenFieldBean");
		screenFieldEntity.setSerializableLabelBeans(screenField);
		//dependency
		Integer fieldID=screenField.getField();
		EntityDependency screenFieldDependency = new EntityDependency("TFieldBean",fieldID,"field");
		Set<Integer> fieldExported=exportedEntities.get("TFieldBean");
		if(fieldExported==null){
			fieldExported=new HashSet<Integer>();
			exportedEntities.put("TFieldBean",fieldExported);
		}
		boolean fieldAlreadyAdded=fieldExported.contains(fieldID);
		if(fieldAlreadyAdded){
			screenFieldDependency.setEntityAdded(true);
		}else{
			fieldExported.add(fieldID);
			FieldExporter fieldExporter=new FieldExporter();
			TFieldBean fieldBean = FieldBL.loadByPrimaryKey(screenField.getField());
			screenFieldDependency.setEntityContext(fieldExporter.createContext(fieldBean,includeFieldConfig,exportedEntities));
		}
		screenFieldEntity.addDependecy(screenFieldDependency);

		return screenFieldEntity;
	}
	public boolean isIncludeFieldConfig() {
		return includeFieldConfig;
	}

	public void setIncludeFieldConfig(boolean includeFieldConfig) {
		this.includeFieldConfig = includeFieldConfig;
	}

	public static String getExportFileName(List<Integer> screenIDList){
		if(screenIDList.size()==1){
			TScreenBean screenBean = screenDAO.loadFullByPrimaryKey(screenIDList.get(0));
			if(screenBean!=null){
				return "SCREEN-"+screenBean.getName()+".xml";
			}
		}
		return "ScreenData.xml";
	}
}
