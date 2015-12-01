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

package com.aurel.track.plugin;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.item.ItemDetailBL;
import com.aurel.track.screen.SystemActions;

/**
 * Obtain the plugins 
 */
public class PluginManager {
	public static final String DASHBOARD_ELEMENT="dashboard";
	public static final String VERSION_CONTROL_ELEMENT="version-control";	
	public static final String FIELD_TYPE_ELEMENT="fieldType";
	public static final String DATASOURCE_ELEMENT="datasource";
	public static final String LINKTYPE_ELEMENT="linkType";
	public static final String ISSUE_LIST_VIEW_ELEMENT="issueListView";
	public static final String MODULE_ELEMENT="module";
	private static PluginManager instance;
	private Map<String, List<PluginDescriptor>> allPluginDescriptorsLists;
	private Map<String, Map<String,PluginDescriptor>> allPluginDescriptorsMap;
	private static final Logger LOGGER = LogManager.getLogger(PluginManager.class);
	private PluginManager(){
		allPluginDescriptorsLists = getAllPluginDescriptors();
		allPluginDescriptorsMap = new HashMap<String, Map<String,PluginDescriptor>>();
		if (allPluginDescriptorsLists!=null) {
			Iterator<String> itrPluginType = allPluginDescriptorsLists.keySet().iterator();
			while (itrPluginType.hasNext()) {
				String pluginType = itrPluginType.next();
				List<PluginDescriptor> pluginDescriptorsList =  allPluginDescriptorsLists.get(pluginType);
				if (pluginDescriptorsList!=null) {
					Map<String,PluginDescriptor> pluginDescriptorMap = allPluginDescriptorsMap.get(pluginType);
					if (pluginDescriptorMap==null) {
						pluginDescriptorMap = new HashMap<String, PluginDescriptor>();
						allPluginDescriptorsMap.put(pluginType, pluginDescriptorMap);
					}
					Iterator<PluginDescriptor> itrPluginDescriptor = pluginDescriptorsList.iterator();
					while (itrPluginDescriptor.hasNext()) {
						PluginDescriptor pluginDescriptor = itrPluginDescriptor
								.next();
						pluginDescriptorMap.put(pluginDescriptor.getId(), pluginDescriptor);
					}
				}
			}
		}
	}		
	
	public static PluginManager getInstance(){
		if(instance==null){
			instance = new PluginManager();
		}
		return instance;
	}	
	
	public List<PluginDescriptor> getPluginDescriptorListOfType(String pluginType){
		return allPluginDescriptorsLists.get(pluginType);		
	}
	
	public Map<String, PluginDescriptor> getPluginDescriptorMapOfType(String pluginType){
		return allPluginDescriptorsMap.get(pluginType);		
	}
	
	public PluginDescriptor getPluginDescriptor(String pluginType, String pluginID){
		Map pluginDescriptorsMap = allPluginDescriptorsMap.get(pluginType);
		if (pluginDescriptorsMap!=null) {
			return (PluginDescriptor)pluginDescriptorsMap.get(pluginID);
		}
		return null;
	}
	
	public Object getPluginClass(String pluginType, String pluginID) {
		PluginDescriptor pluginDescriptor = getPluginDescriptor(pluginType, pluginID);
		if (pluginDescriptor!=null) {
			String pluginClassName = pluginDescriptor.getTheClassName();
		
			Class pluginClass = null;
			if (pluginClassName == null) {
				LOGGER.warn("No class specified for pluginType " + pluginType + " and plugin " +  pluginID);
				return null;
			}
			try {
				pluginClass = Class.forName(pluginClassName);
			} catch (ClassNotFoundException e) {
				LOGGER.error("The plugin class " + pluginClassName + "  not found found in the classpath " + e.getMessage());
			}
			if (pluginClass!=null) {
				try {
					return  pluginClass.newInstance();
				} catch (Exception e) {
					LOGGER.error("Instantiating the plugin class class " + pluginClassName + "  failed with " + e.getMessage());
				}
			}
		}
		return null;				
	}
	
	private Map getAllPluginDescriptors(){
		Map parserMap=new HashMap();
		parserMap.put(DASHBOARD_ELEMENT,new DashboardParser());
		parserMap.put(VERSION_CONTROL_ELEMENT,new VersionControlParser());
		parserMap.put(FIELD_TYPE_ELEMENT, new FieldTypeParser());
		parserMap.put(DATASOURCE_ELEMENT, new DatasourceParser());
		parserMap.put(LINKTYPE_ELEMENT, new LinkTypeParser());
		parserMap.put(ISSUE_LIST_VIEW_ELEMENT,new IssueListViewParser());
		parserMap.put(MODULE_ELEMENT,new ModuleParser());

		return getPluginDescriptors(parserMap);
	}
	
	/**
	 * Gather the plugins from possible more trackplus-plugin.xml files found in the classpath
	 * @param parserMap
	 * @return
	 */
	private Map<String, List<PluginDescriptor>> getPluginDescriptors(Map<String, DescriptorParser> parserMap){
		
		Map<String, List<PluginDescriptor>> allDescriptors=new HashMap<String, List<PluginDescriptor>>();
		
		try {
			Enumeration<URL> resources = PluginManager.class.getClassLoader().getResources("trackplus-plugin.xml");
			if (resources != null) {
				while (resources.hasMoreElements()) {
					URL url = (URL)resources.nextElement();
					LOGGER.debug("trackplus-plugin.xml at " + url.toString());
					InputStream is=url.openStream();
					Map<String, List<PluginDescriptor>> currentFileDescriptors = PluginParser.parseDocument(is,parserMap);
					is.close();

					for (String key: parserMap.keySet()) {
						List<PluginDescriptor> allPluginsOfType =  allDescriptors.get(key);
						List<PluginDescriptor> currentDescriptorsOfType  = currentFileDescriptors.get(key);
						if(allPluginsOfType==null){
							allPluginsOfType=new ArrayList<PluginDescriptor>();
						}
						if(currentDescriptorsOfType!=null){
							LOGGER.debug("Found plug-in with key " + key);
							allPluginsOfType.addAll(currentDescriptorsOfType);
						}
						allDescriptors.put(key,allPluginsOfType);
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return allDescriptors;
	}
	
		
	/**
	 * Gather the plugins from possible more trackplus-plugin.xml files found in the classpath
	 * @param parserMap
	 * @return
	 */
	private Map getPluginDescriptors2(Map<String, DescriptorParser> parserMap){
		Map allDescriptors=new HashMap();
		try {
			Enumeration resurces =PluginManager.class.getClassLoader().getResources("trackplus-plugin.xml");
			if (resurces != null) {
				while (resurces.hasMoreElements()) {
					URL url = (URL)resurces.nextElement();
					LOGGER.debug("trackplus-plugin.xml at " + url.toString());
					InputStream is=url.openStream();
					Map<String, List<PluginDescriptor>> currentFileDescriptors=PluginParser.parseDocument(is,parserMap);
					is.close();
					Iterator it= parserMap.keySet().iterator();
					while (it.hasNext()) {
						Object key = it.next();
						List allPluginsOfType= (List) allDescriptors.get(key);
						List currentDescriptorsOfType= currentFileDescriptors.get(key);
						if(allPluginsOfType==null){
							allPluginsOfType=new ArrayList();
						}
						if(currentDescriptorsOfType!=null){
							LOGGER.debug("Found plug-in with key " + key);
							allPluginsOfType.addAll(currentDescriptorsOfType);
						}
						allDescriptors.put(key,allPluginsOfType);
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return allDescriptors;
	}

	
	/**
	 * Obtain the list of dashboard plugin descriptors
	 * @return
	 */
	public List getDashboardDescriptors(){
		Map parserMap=new HashMap();
		parserMap.put(DASHBOARD_ELEMENT,new DashboardParser());
		return (List) getPluginDescriptors(parserMap).get(DASHBOARD_ELEMENT);
	}
	
	
	public List<PluginDescriptor> getVersionControlDescriptors(){
		Map<String,DescriptorParser> parserMap=new HashMap<String,DescriptorParser>();
		parserMap.put(VERSION_CONTROL_ELEMENT,new VersionControlParser());
		return (List) getPluginDescriptors(parserMap).get(VERSION_CONTROL_ELEMENT);
	}
	
	public List getCustomFieldTypeDescriptors(){
		Map parserMap=new HashMap();
		parserMap.put(FIELD_TYPE_ELEMENT, new FieldTypeParser());
		return (List) getPluginDescriptors(parserMap).get(FIELD_TYPE_ELEMENT);
	}
	
	public List getDatasourceDescriptors(){
		Map parserMap=new HashMap();
		parserMap.put(DATASOURCE_ELEMENT, new DatasourceParser());
		return (List) getPluginDescriptors(parserMap).get(DATASOURCE_ELEMENT);
	}
	
	public List getLinkTypeDescriptors() {
		Map parserMap=new HashMap();
		parserMap.put(LINKTYPE_ELEMENT, new LinkTypeParser());
		return (List) getPluginDescriptors(parserMap).get(LINKTYPE_ELEMENT);
	}

	public List<IssueListViewDescriptor> getIssueListViewDescriptors(){
		Map parserMap=new HashMap();
		parserMap.put(ISSUE_LIST_VIEW_ELEMENT,new IssueListViewParser());
		List desc = (List) getPluginDescriptors(parserMap).get(ISSUE_LIST_VIEW_ELEMENT);
		return desc;
	}

	/**
	 * Obtain the list of dashboard plugin descriptors
	 * @return
	 */
	public List getModuleDescriptors(){
		Map parserMap=new HashMap();
		parserMap.put(MODULE_ELEMENT,new ModuleParser());
		return (List) getPluginDescriptors(parserMap).get(MODULE_ELEMENT);
	}
	
	/**
	 * Obtain the dashboard plugin descriptor by ID
	 * @return
	 */
	public ModuleDescriptor getModuleByID(String moduleID){
		Map parserMap=new HashMap();
		parserMap.put(MODULE_ELEMENT,new ModuleParser());
		List modules = (List)getPluginDescriptors(parserMap).get(MODULE_ELEMENT);
		ModuleDescriptor moduleDescriptor = null;
		for (int i=0;i<modules.size();i++) {
			ModuleDescriptor md = (ModuleDescriptor)modules.get(i);
			if(md.getId().equals(moduleID)){
				moduleDescriptor=md;
				break;
			}
		}
		return moduleDescriptor;
	}
	   
	
	/**
	 * @TODO implement me with plugins
	 * @return
	 */
	public List<ItemActionDescription> getItemActionDescriptors(){
		List<ItemActionDescription> l=new ArrayList<ItemActionDescription>();
		ItemActionDescription item;
		
		//printItem
		//<put-attribute name="title" value="item.view.title"/>
		item=new ItemActionDescription();
		item.setId(SystemActions.PRINT+"");
		item.setUseWizard(false);
		item.setUseBottomTabs(true);
		item.setCssClass("buttonViewAll");
		item.setTooltipKey("common.btn.viewAll");
		item.setLabelKey("common.btn.viewAll");
		item.setImageInactive("view-all-inactive.gif");
		item.setTheClassName("com.aurel.track.item.action.PrintItemActionPlugin");
		item.setTitle("item.view.title");
		item.setMenuPath("item.view.title");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/cIssueOverview.html");
		l.add(item);
		
		//copy
		item=new ItemActionDescription();
		item.setId(SystemActions.COPY+"");
		item.setUseWizard(true);
		item.setFirstPageTemplate("com.trackplus.item.action.CopyItem1StepRenderer");
		item.setUseBottomTabs(false);
		item.setCssClass("itemAction_copy");
		item.setTooltipKey("common.btn.copyItem.tt");
		item.setLabelKey("common.btn.copy");
		item.setImageInactive("copy-inactive.gif");
		item.setTheClassName("com.aurel.track.item.action.CopyItemActionPlugin");
		item.setFinishLabelKey("common.btn.copy");
		
		item.setTitle1("item.action.copy.firstStep.title");
		item.setMenuPath1("item.action.copy.firstStep");
		item.setHelpPage1("WebHelp/Concepts/02ForTeamMembers/issues/cCopyIssue.html");
		
		item.setTitle("item.action.copy.secondStep.title");
		item.setMenuPath("item.action.copy.secondStep");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/cCreateIssue.html");
		l.add(item);
		
		//new
		item=new ItemActionDescription();
		item.setId(SystemActions.NEW+"");
		item.setUseWizard(true);
		item.setFirstPageTemplate("com.trackplus.item.action.ItemLocationStepRenderer");
		item.setFinishLabelKey("common.btn.save");
		item.setUseBottomTabs(true);
		item.setTheClassName("com.aurel.track.item.action.NewItemActionPlugin");
		
		item.setTitle1("item.action.create.firstStep.title");
		item.setMenuPath1("item.action.create.firstStep");
		item.setHelpPage1("WebHelp/Concepts/02ForTeamMembers/issues/cCreateIssue.html");
		
		item.setTitle("item.action.create.secondStep.title");
		item.setMenuPath("item.action.create.secondStep");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/cCreateIssue.html");
		l.add(item);
		
		//EDIT
		item=new ItemActionDescription();
		item.setId(SystemActions.EDIT+"");
		item.setUseWizard(false);
		item.setUseBottomTabs(true);
		item.setCssClass("itemAction_edit");
		item.setTooltipKey("common.btn.editItem.tt");
		item.setLabelKey("common.btn.edit");
		item.setImageInactive("edit-inactive.gif");
		item.setTheClassName("com.aurel.track.item.action.EditItemActionPlugin");
		
		item.setTitle("item.action.edit.title");
		item.setMenuPath("menu.item.editItem");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/cIssueOverview.html");
		
		l.add(item);
		
		//MOVE
		item=new ItemActionDescription();
		item.setId(SystemActions.MOVE+"");
		item.setUseWizard(true);
		item.setFirstPageTemplate("com.trackplus.item.action.MoveItemStepRenderer");
		item.setSecondPageTemplate("plugins/itemAction/templates/editMoveItemDetail.ftl");
		item.setFinishLabelKey("common.btn.move");
		item.setUseBottomTabs(false);
		item.setCssClass("itemAction_move");
		item.setTooltipKey("common.btn.moveItem.tt");
		item.setLabelKey("common.btn.move");
		item.setImageInactive("move-inactive.gif");
		item.setTheClassName("com.aurel.track.item.action.MoveItemActionPlugin");
	   
		item.setTitle1("item.action.move.firstStep");
		item.setMenuPath1("item.action.move.firstStep");
		item.setHelpPage1("WebHelp/Concepts/02ForTeamMembers/issues/cMoveIssue.html");
		
		item.setTitle("item.action.move.secondStep");
		item.setMenuPath("item.action.move.secondStep");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/cMoveIssue.html");
		
		l.add(item);
		
		//NEW_CHILD
		item=new ItemActionDescription();
		item.setId(SystemActions.NEW_CHILD+"");
		item.setUseWizard(true);
		item.setFirstPageTemplate("com.trackplus.item.action.ItemLocationStepRenderer");
		item.setFinishLabelKey("common.btn.addChild");
		item.setUseBottomTabs(true);
		item.setCssClass("itemAction_addChild");
		item.setTooltipKey("common.btn.addChild.tt");
		item.setLabelKey("common.btn.addChild");
		item.setImageInactive("addChild-inactive.gif");
		item.setTheClassName("com.aurel.track.item.action.NewItemChildActionPlugin");
		
		item.setTitle1("item.action.create.firstStep.title");
		item.setMenuPath1("item.action.create.firstStep");
		item.setHelpPage1("WebHelp/Concepts/02ForTeamMembers/issues/cCreateIssue.html");
		
		item.setTitle("item.action.create.secondStep.title");
		item.setMenuPath("item.action.create.secondStep");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/cCreateIssue.html");
		
		l.add(item);
		
		//new linked item
		item=new ItemActionDescription();
		item.setId(SystemActions.NEW_LINKED_ITEM+"");
		item.setUseWizard(true);
		item.setFirstPageTemplate("com.trackplus.item.action.ItemLocationStepRenderer");
		item.setFinishLabelKey("common.btn.addLinkedItem");
		item.setUseBottomTabs(true);
		item.setCssClass("links");
		item.setPreselectedTab(new Integer(ItemDetailBL.TAB_LINKS));
		item.setTooltipKey("common.btn.addLinkedItem.tt");
		item.setLabelKey("common.btn.addLinkedItem");
		item.setTheClassName("com.aurel.track.item.action.NewLinkedItemActionPlugin");
		
		item.setTitle1("item.action.create.firstStep.title");
		item.setMenuPath1("item.action.create.firstStep");
		item.setHelpPage1("WebHelp/Concepts/02ForTeamMembers/issues/cCreateIssue.html");
		
		item.setTitle("item.action.create.secondStep.title");
		item.setMenuPath("item.action.create.secondStep");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/cCreateIssue.html");
		
		l.add(item);
		
		//CHANGE_STATUS
		item=new ItemActionDescription();
		item.setId(SystemActions.CHANGE_STATUS+"");
		item.setUseWizard(false);
		item.setUseBottomTabs(false);
		item.setCssClass("itemAction_changeStatus");
		item.setTooltipKey("common.btn.changeStatus.tt");
		item.setLabelKey("common.btn.changeStatus");
		item.setImageInactive("changeStatus-inactive.gif");
		item.setTheClassName("com.aurel.track.item.action.ChangeStatusActionPlugin");
	   
		item.setTitle("item.action.edit.title");
		item.setMenuPath("menu.item.editItem");
		item.setHelpPage("WebHelp/Concepts/02ForTeamMembers/issues/changeStatus.html");
		
		l.add(item);
		
		//ADD_COMMENT
		return l;
	}
}
