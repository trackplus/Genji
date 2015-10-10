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


package com.aurel.track.admin.customize.treeConfig.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.treeConfig.ConfigItemAbstract;
import com.aurel.track.admin.customize.treeConfig.ConfigItemFacade;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL.ICON_CLS;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.TreeConfigNodeTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TActionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenConfigDAO;
import com.aurel.track.dao.ScreenDAO;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * An implementation of ConfigItemFacade for screenConfig
 * @author Adrian Bojani
 *
 */
public class ScreenConfigItemFacade extends ConfigItemAbstract implements ConfigItemFacade {
	
	private static final Logger LOGGER = LogManager.getLogger(ScreenConfigItemFacade.class);
	private static ScreenConfigDAO screenCfgDAO=DAOFactory.getFactory().getScreenConfigDAO();
	private static ScreenDAO screenDAO=DAOFactory.getFactory().getScreenDAO();
	private static ScreenConfigItemFacade instance;
	
	private static Integer ACTION_KEY = Integer.valueOf(1);
	private static Integer SCREEN_KEY = Integer.valueOf(2);
	
	/**
	 * Get a singleton instance
	 * @return
	 */
	public static ScreenConfigItemFacade getInstance(){
		if(instance==null){
			instance=new ScreenConfigItemFacade();
		}
		return instance;
	}
	
	/**
	 * Expand a tree node from tree
	 * @return
	 */
	public List<TreeConfigNodeTO> getFirstLevelNodes(TPersonBean personBean, Locale locale){
		List<TreeConfigNodeTO> rootNodes = new ArrayList<TreeConfigNodeTO>();
		if (personBean.isSys()) {
			TreeConfigNodeTO standardScreenAssignments = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.SCREEEN_TYPE), 
					TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.SCREEEN_TYPE, 
					LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.screen.standard", locale), false);
			standardScreenAssignments.setIconCls(ICON_CLS.GLOBAL_CONFIG);
			standardScreenAssignments.setDefaultConfig(true);
			standardScreenAssignments.setChildrenAreLeaf(true);
			rootNodes.add(standardScreenAssignments);
			List<TScreenConfigBean> issueTypeSpecificFieldConfigList = screenCfgDAO.loadAllByIssueType(null, null, null);
			String issueTypeTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.screen.issueType", locale);
			if (issueTypeSpecificFieldConfigList==null || issueTypeSpecificFieldConfigList.isEmpty()) {
				issueTypeTitle = TreeConfigBL.addInheritedMarkup(issueTypeTitle);
			}
			TreeConfigNodeTO issueTypeFields = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.ISSUE_TYPE), 
					TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.ISSUE_TYPE, 
					issueTypeTitle, false);
			issueTypeFields.setIconCls(ICON_CLS.ISSUE_TYPE_CONFIG);
			rootNodes.add(issueTypeFields);
			List<TScreenConfigBean> projectTypeSpecificFieldConfigList = screenCfgDAO.loadAllByProjectType(null);
			String projectTypeTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.screen.projectType", locale);
			if (projectTypeSpecificFieldConfigList==null || projectTypeSpecificFieldConfigList.isEmpty()) {
				projectTypeTitle = TreeConfigBL.addInheritedMarkup(projectTypeTitle);
			}
			TreeConfigNodeTO projectTypeFields = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.PROJECT_TYPE), 
					TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.PROJECT_TYPE, 
					projectTypeTitle, false);
			projectTypeFields.setIconCls(ICON_CLS.PROJECT_TYPE_CONFIG);
			rootNodes.add(projectTypeFields);
		}
		List<TScreenConfigBean> projectSpecificFieldConfigList = screenCfgDAO.loadAllByProject(null);
		String projectTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.screen.project", locale);
		if (projectSpecificFieldConfigList==null || projectSpecificFieldConfigList.isEmpty()) {
			projectTitle = TreeConfigBL.addInheritedMarkup(projectTitle);
		}
		TreeConfigNodeTO projectFields = new TreeConfigNodeTO(
				TreeConfigIDTokens.encodeRootNode(TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.PROJECT), 
				TreeConfigBL.SCREEN_CONFIG, TreeConfigBL.PROJECT, 
				projectTitle, false);
		projectFields.setIconCls(ICON_CLS.PROJECT_CONFIG);
		rootNodes.add(projectFields);
		return rootNodes;
	}
	
	/**
	 * Whether there are issue type specific configurations
	 */
	public boolean hasIssueTypeSpecificConfig(Integer issueType, Integer projectType, Integer project) {
		List<TScreenConfigBean> screenConfigBeans = screenCfgDAO.loadAllByIssueType(issueType, projectType, project);
		return screenConfigBeans!=null && !screenConfigBeans.isEmpty();
	}
	
	/**
	 * Whether there are project type specific configurations
	 */
	public boolean hasProjectTypeSpecificConfig(Integer projectType) {
		List<TScreenConfigBean> screenConfigBeans = screenCfgDAO.loadAllByProjectType(projectType);
		return screenConfigBeans!=null && !screenConfigBeans.isEmpty();
	}
	
	/**
	 * Load all project specific configurations (also those which have item type specified)
	 * @param projectIDs if null get all project specific configurations
	 * @return
	 */
	public List<ConfigItem> loadAllProjectSpecificConfig(List<Integer> projectIDs) {
		return (List)screenCfgDAO.loadAllByProjects(projectIDs);
	}
	
	/**
	 * Whether there are specific configurations for system fields or custom fields
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param isCustom
	 * @return
	 */
	public boolean hasFieldTypeSpecificConfig(Integer issueType, Integer projectType,  Integer project, boolean isCustom) {
		return true;
	}
	
	/**
	 * Gets the configuration which corresponds to the domain exactly
	 * This might be null
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param field
	 * @return
	 */
	/*public TScreenConfigBean getValidConfigDirect(Integer issueType, 
			Integer projectType, Integer project, Integer field) {
		//try the issueType-project combination
		if (issueType!=null && project!=null) {
			return screenCfgDAO.loadByIssueTypeAndProject(field, issueType, project);
		}
		//try the issueType-projectType combination
		if (issueType!=null && projectType!=null) {
			return screenCfgDAO.loadByIssueTypeAndProjectType(field, issueType, projectType);
		}	
		//try the project only
		if (project!=null) {
			return screenCfgDAO.loadByProject(field, project);
		}
		//try the projectType only
		if (projectType!=null) {
			return screenCfgDAO.loadByProjectType(field, projectType);
		}
		//try the issueType only
		if (issueType!=null) {
			return screenCfgDAO.loadByIssueType(field, issueType);
		}
		//try the default
		return screenCfgDAO.loadDefault(field);
	}*/
	
	/**
	 *  Gets the configuration by itemType and project
	 * @param configRel
	 * @param itemTypeID
	 * @param projectID
	 * @return
	 */
	public ConfigItem loadForConfigRelByIssueTypeAndProject(Integer configRel, Integer itemTypeID, Integer projectID) {
		return screenCfgDAO.loadByIssueTypeAndProject(configRel, itemTypeID, projectID);
	}
		
	/**
	 * Gets the configuration by itemType and projectType
	 * @param configRel
	 * @param itemTypeID
	 * @param projectTypeID
	 * @return
	 */
	public ConfigItem loadForConfigRelByIssueTypeAndProjectType(Integer configRel, Integer itemTypeID, Integer projectTypeID) {
		return screenCfgDAO.loadByIssueTypeAndProjectType(configRel, itemTypeID, projectTypeID);
	}
	
	/**
	 * Gets the configuration by project
	 * @param configRel
	 * @param projectID
	 * @return
	 */
	public ConfigItem loadForConfigRelByProject(Integer configRel, Integer projectID) {
		return screenCfgDAO.loadByProject(configRel, projectID);
	}
	
	/**
	 * Gets the configuration by projectType
	 * @param configRel
	 * @param projectTypeID
	 * @return
	 */
	public ConfigItem loadForConfigRelByProjectType(Integer configRel, Integer projectTypeID) {
		return screenCfgDAO.loadByProjectType(configRel, projectTypeID);
	}
	
	/**
	 * Gets the configuration by itemType
	 * @param configRel
	 * @param itemTypeID
	 * @return
	 */
	public ConfigItem loadForConfigRelByItemType(Integer configRel, Integer itemTypeID) {
		return screenCfgDAO.loadByIssueType(configRel, itemTypeID);
	}
	
	/**
	 * Gets the default configuration
	 * @param configRel
	 * @return
	 */
	public ConfigItem loadForConfigRelDefault(Integer configRel) {
		return screenCfgDAO.loadDefault(configRel);
	}
	
	/**
	 * Whether the only project or only projectTyp specific configuration can be defined
	 * @return
	 */
	public boolean hasProjectOrProjectTypSpecificConfigs() {
		return false;
	}
	
	/**
	 * Gets the nearest fall back configuration corresponding to the domain
	 * This should never be null (it should be at least the default configuration) 
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param action
	 * @return
	 */
	/*public TScreenConfigBean getValidConfigFallback(Integer issueType, Integer projectType, Integer project, Integer action) {
		TScreenConfigBean screenConfigBean;
		//try the issueType-project combination
		if (issueType!=null && project!=null) {
			screenConfigBean = screenCfgDAO.loadByIssueTypeAndProject(action, issueType, project);
			if (screenConfigBean!=null) {
				return screenConfigBean;
			} else {
				//get the projectType of the project and fall back to the issueType-projectType combination
				TProjectBean projectBean = LookupContainer.getProjectBean(project);
				if (projectBean!=null) {
					screenConfigBean = screenCfgDAO.loadByIssueTypeAndProjectType(action, issueType, projectBean.getProjectType());
					if (screenConfigBean!=null) {
						return screenConfigBean;
					}
				}
			}
		}
		//try the issueType-projectType combination
		if (issueType!=null && projectType!=null) {
			screenConfigBean = screenCfgDAO.loadByIssueTypeAndProjectType(action, issueType, projectType);
			if (screenConfigBean!=null) {
				return screenConfigBean;
			}
		}
		
		//try the project only but fall back to the corresponding projectType if it is the case
		if (project!=null) {
			screenConfigBean =  screenCfgDAO.loadByProject(action, project);
			if (screenConfigBean!=null) {
				return screenConfigBean;
			} else {
				//try the projectType of the project
				TProjectBean projectBean = LookupContainer.getProjectBean(project);
				if (projectBean!=null) {
					screenConfigBean = screenCfgDAO.loadByProjectType(action, projectBean.getProjectType());
					if (screenConfigBean!=null) {
						return screenConfigBean;
					}
				}
			}
		}
		
		//try the projectType only	
		if (projectType!=null) {
			screenConfigBean =  screenCfgDAO.loadByProjectType(action, projectType);
			if (screenConfigBean!=null) {
				return screenConfigBean;
			}
		}
		
		//try the issueType	only	
		if (issueType!=null) {
			screenConfigBean = screenCfgDAO.loadByIssueType(action, issueType);
			if (screenConfigBean!=null) {
				return screenConfigBean;
			}
		}
		//try the default
		return screenCfgDAO.loadDefault(action);
	}*/
	
	/**
	 * Deletes a single configuration
	 * @param treeConfigIDTokens
	 */
	public void deleteConfig(TreeConfigIDTokens treeConfigIDTokens) {
		if (!(treeConfigIDTokens.getIssueTypeID()==null && treeConfigIDTokens.getProjectTypeID()==null && 
				treeConfigIDTokens.getProjectID()==null) || !TreeConfigBL.SCREEEN_TYPE.equals(treeConfigIDTokens.getType())) {
			String type = treeConfigIDTokens.getType();
			Integer issueTypeID = treeConfigIDTokens.getIssueTypeID();
			Integer projectTypeID = treeConfigIDTokens.getProjectTypeID();
			Integer projectID = treeConfigIDTokens.getProjectID();
			Integer actionID = treeConfigIDTokens.getConfigRelID();
			//Integer id = treeConfigIDTokens.getObjectID();
			if(type.equals(TreeConfigBL.CONFIG_ITEM)){
				TScreenConfigBean screenConfigBean = (TScreenConfigBean)getValidConfigDirect(
						issueTypeID, projectTypeID, projectID, actionID);
				if(screenConfigBean==null){
					LOGGER.info("Can't reset a ScreenConfigItem node that don't have a origanl value!");
				} else {
					screenCfgDAO.delete(screenConfigBean.getObjectID());
				}
			} else if (type.equals(TreeConfigBL.PROJECT)) {
				List<Integer> projectIDs = null;
				if (projectID!=null) {
					projectIDs = ProjectBL.getDescendantProjectIDsAsList(projectID);
				}
				screenCfgDAO.deleteByProjects(projectIDs);
			} else if (type.equals(TreeConfigBL.PROJECT_TYPE)){
				screenCfgDAO.deleteByProjectType(projectTypeID);
			} else if (type.equals(TreeConfigBL.ISSUE_TYPE)){
				screenCfgDAO.deleteByIssueType(issueTypeID, projectTypeID, projectID);
			}
		}
	}
	
	/**
	 * Overwrites the inherited configuration for originalConfig 
	 * @param directConfig
	 * @param fallbackConfig
	 */
	public Integer overwriteConfig(ConfigItem directConfig, ConfigItem fallbackConfig) {
		if (fallbackConfig==null) {
			return null;
		}
		copyExtraInfo(fallbackConfig, directConfig);
		return ScreenConfigBL.save((TScreenConfigBean)directConfig);	
	}
	
	/**
	 * Whether after a reset operation a tree refresh is needed
	 * @return
	 */
	public boolean refreshEntireTreeAfterReset() {
		//true because the assignment info is shown in the node's label
		return true;
	}
	
	/**
	 * Get the lookup map for title
	 * @param locale
	 * @return
	 */
	public Map<Integer, Map<Integer, ILabelBean>> getLookupMap(Locale locale) {
		Map<Integer, Map<Integer, ILabelBean>> lookupMap = new HashMap<Integer, Map<Integer,ILabelBean>>();
		List<TActionBean> actionList = LocalizeUtil.localizeDropDownList(ActionBL.loadAll(), locale);
		lookupMap.put(ACTION_KEY, GeneralUtils.createMapFromList(actionList));
		List<TScreenBean> screenList = screenDAO.loadAll();
		lookupMap.put(SCREEN_KEY, GeneralUtils.createMapFromList(screenList));
		return lookupMap;
	}
	
	/**
	 * Get the title for a config
	 * @param cfg
	 * @return
	 */
	public String getTitle(ConfigItem cfg, Map<Integer, Map<Integer, ILabelBean>> lookupMap) {
		Map<Integer, ILabelBean> actionMap = lookupMap.get(ACTION_KEY);
		TActionBean actionBean = (TActionBean)actionMap.get(((TScreenConfigBean)cfg).getAction());
		Map<Integer, ILabelBean> screenMap = lookupMap.get(SCREEN_KEY);
		TScreenBean screenBean = (TScreenBean)screenMap.get(((TScreenConfigBean)cfg).getScreen());
		return "<span class=\'action\'>"+actionBean.getLabel()+"</span>:" + "<span class=\'formName\'>"+ screenBean.getName()+"</span>";
	}
	
	public List load(ConfigItem cfg) {
		return screenCfgDAO.load((TScreenConfigBean)cfg);
	}
	
	public ConfigItem createConfigItem() {
		return new TScreenConfigBean();
	}
	
	public void copyExtraInfo(ConfigItem cfgSource, ConfigItem cfgTarget) {
		//copy (action,screen)
		TScreenConfigBean newCfg=(TScreenConfigBean)cfgTarget;
		TScreenConfigBean cfg=(TScreenConfigBean)cfgSource;
		//newCfg.setAction(cfg.getAction());
		newCfg.setScreen(cfg.getScreen());
		
	}
	
	public Integer saveConfig(ConfigItem cfg) {
		return ScreenConfigBL.save((TScreenConfigBean)cfg);
	}

	public ConfigItem loadConfigByPk(Integer pk) {
		return screenCfgDAO.loadByPrimaryKey(pk);
	}
}
