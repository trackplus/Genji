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


package com.aurel.track.admin.customize.treeConfig;

import java.util.StringTokenizer;

import com.aurel.track.beans.ConfigItem;

/**
 * A utility class to encode and decode a TreeNodeConfig and a ConfigItem
 * @author Adrian Bojani
 *
 */
public class TreeNodeConfigUtils {
	
	/**
	 * Get the node id from encoded data
	 * @param data
	 * @return
	 */
	/*public static String getNodeWidgetId(String data){
		String property="widgetId";
		int index=data.indexOf("\""+property+"\":");
		int endindex=data.indexOf(",",index);
		return data.substring(index+property.length()+4,endindex-1);
	}*/
	
	
	/**
	 * Create an unique id from a config item
	 * @param configType
	 * @param type
	 * @param cfg
	 * @param id
	 * @return
	 */
	public static String encodeWidgetID(String configType, String type,ConfigItem cfg,Integer id){
		return configType+"_"+type+"_"+cfg.getProject()+"_"+cfg.getProjectType()+"_"+cfg.getIssueType()+"_"+cfg.getConfigRel()+"_"+id;
	}
	
	
	/**
	 * Return the config type from an ecoded widget id
	 * eg screen for screen configuration, field for field configuration
	 * @param widgetID
	 * @return
	 */
	public static String decodeConfigType(String widgetID){
		StringTokenizer st=new StringTokenizer(widgetID,"_");
		return st.nextToken();
	}
	
	
	/**
	 * Return the type for a node from an encoded node
	 * eg: project, projectType, issueType, configItem
	 * @param widgetID
	 * @return
	 */
	public static String decodeType(String widgetID){
		StringTokenizer st=new StringTokenizer(widgetID,"_");
		st.nextToken();//configType
		return st.nextToken();
	}
	
	
	/**
	 *Return the id of node from a encoded node
	 * @param widgetID
	 * @return
	 */
	public static String decodeId(String widgetID){
		StringTokenizer st=new StringTokenizer(widgetID,"_");
		if(st.countTokens()==2){
			// are only "configType_type
			return null;
		}
		return widgetID.substring(widgetID.lastIndexOf("_")+1);
	}
	
	
	/**
	 * Obtain a configItem from an encoded string
	 * @param widgetId
	 * @return
	 */
	public static ConfigItem decodeConfigItem(String widgetId){
		ConfigItem result=null;
		StringTokenizer st=new StringTokenizer(widgetId,"_");
		String configType=st.nextToken();//configType
		ConfigItemFacade configItemFacade=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		ConfigItem cfg=configItemFacade.createConfigItem();
		String type=st.nextToken();//type
		if (st.hasMoreTokens()) {
			String project=st.nextToken();
			String projectType=st.nextToken();
			String issueType=st.nextToken();
			String configRel=st.nextToken();
			if(project!=null&&!project.equals("null")) {
				try {
					cfg.setProject(new Integer(project));
				} catch (Exception ex) {}
			}
			if (projectType!=null&&!projectType.equals("null")) {
				try {
					cfg.setProjectType(new Integer(projectType));
				} catch(Exception ex) {}
			}
			if (issueType!=null&&!issueType.equals("null")) {
				try {
					cfg.setIssueType(new Integer(issueType));
				} catch(Exception ex) {}
			}
			if (configRel!=null&&!configRel.equals("null")) {
				try {
					cfg.setConfigRel(new Integer(configRel));
				} catch(Exception ex){}
			}
		}
		if (type.equals(TreeConfigBL.CONFIG_ITEM)) {
			String id=decodeId(widgetId);
			ConfigItem cfgOrginal=configItemFacade.loadConfigByPk(new Integer(id));
			//can happen when the parent config is deleted but the node is not refreshed 
			if (cfgOrginal!=null) {
				if(TreeConfigBL.isTheSameConfig(cfgOrginal, cfg)){
					result=cfgOrginal;
				}else{
					result=cfg;
					configItemFacade.copyExtraInfo(cfgOrginal, result);
				}
			} else {
				result=cfg;
			}
		} else {
			result=cfg;
		}
		return result;
	}

	
	/**
	 * Obtain a configItem from an encoded string	 
	 * @param widgetId
	 * @return
	 */
	public static ConfigItem decodeConfigItemComponents(String widgetId) {
		StringTokenizer st=new StringTokenizer(widgetId,"_");
		String configType=st.nextToken();//configType
		ConfigItemFacade configItemFacade=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		ConfigItem cfg=configItemFacade.createConfigItem();
		st.nextToken();//type
		if(st.hasMoreTokens()){
			String project=st.nextToken();
			String projectType=st.nextToken();
			String issueType=st.nextToken();
			String configRel=st.nextToken();
			if(project!=null&&!project.equals("null")){
				try{
				cfg.setProject(new Integer(project));
				}catch(Exception ex){}
			}
			if(projectType!=null&&!projectType.equals("null")){
				try{
				cfg.setProjectType(new Integer(projectType));
				}catch(Exception ex){}
			}
			if(issueType!=null&&!issueType.equals("null")){
				try{
				cfg.setIssueType(new Integer(issueType));
				}catch(Exception ex){}
			}
			if(configRel!=null&&!configRel.equals("null")){
				try{
				cfg.setConfigRel(new Integer(configRel));
				}catch(Exception ex){}
			}
		}	
		return cfg;
	}
}
