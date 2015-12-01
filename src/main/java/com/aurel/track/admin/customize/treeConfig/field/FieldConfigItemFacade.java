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


package com.aurel.track.admin.customize.treeConfig.field;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.ConfigItemAbstract;
import com.aurel.track.admin.customize.treeConfig.ConfigItemFacade;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL.ICON_CLS;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.TreeConfigNodeTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FieldConfigDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
/**
 * An implementation of ConfigItemFacade for fieldConfig
 * @author Tamas Ruff
 *
 */
public class FieldConfigItemFacade extends ConfigItemAbstract implements ConfigItemFacade {
		
	private static FieldConfigDAO fieldConfigDAO=DAOFactory.getFactory().getFieldConfigDAO();
	
	private static FieldConfigItemFacade instance;
	private static Integer FIELD_KEY = Integer.valueOf(1); 
	
	/**
	 * Return a FieldConfigItemFacade instance which implements the ConfigItemFacade 
	 * @return
	 */
	public static FieldConfigItemFacade getInstance(){
		if(instance==null){
			instance=new FieldConfigItemFacade();
		}
		return instance;
	}

	/**
	 * Expand a tree node from tree
	 * @return
	 */
	@Override
	public List<TreeConfigNodeTO> getFirstLevelNodes(TPersonBean personBean, 
			Locale locale) {
		List<TreeConfigNodeTO> rootNodes = new ArrayList<TreeConfigNodeTO>();
		if (personBean.isSys()) {
			TreeConfigNodeTO standardFieldConfigs = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.FIELD_CONFIG, TreeConfigBL.FIELD), 
					TreeConfigBL.FIELD_CONFIG, TreeConfigBL.FIELD, 
					LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.field.standard", locale), false);
			standardFieldConfigs.setIconCls(ICON_CLS.GLOBAL_CONFIG);
			standardFieldConfigs.setDefaultConfig(true);
			rootNodes.add(standardFieldConfigs);
			//exists at least one issue type specific configuration?
			List<TFieldConfigBean> issueTypeSpecificFieldConfigList = fieldConfigDAO.loadAllByIssueType(null, null, null);
			String issueTypeTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.field.issueType", locale);
			if (issueTypeSpecificFieldConfigList==null || issueTypeSpecificFieldConfigList.isEmpty()) {
				issueTypeTitle = TreeConfigBL.addInheritedMarkup(issueTypeTitle);
			}			
			TreeConfigNodeTO issueTypeFields = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.FIELD_CONFIG, TreeConfigBL.ISSUE_TYPE), 
					TreeConfigBL.FIELD_CONFIG, TreeConfigBL.ISSUE_TYPE, 
					issueTypeTitle, false);
			issueTypeFields.setIconCls(ICON_CLS.ISSUE_TYPE_CONFIG);
			rootNodes.add(issueTypeFields);
			//exists at least one project type specific configuration?
			List<TFieldConfigBean> projectTypeSpecificFieldConfigList = fieldConfigDAO.loadAllByProjectType(null);
			String projectTypeTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.field.projectType", locale);
			if (projectTypeSpecificFieldConfigList==null || projectTypeSpecificFieldConfigList.isEmpty()) {
				projectTypeTitle = TreeConfigBL.addInheritedMarkup(projectTypeTitle);
			}
			TreeConfigNodeTO projectTypeFields = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.FIELD_CONFIG, TreeConfigBL.PROJECT_TYPE), 
					TreeConfigBL.FIELD_CONFIG, TreeConfigBL.PROJECT_TYPE, 
					projectTypeTitle, false);
			projectTypeFields.setIconCls(ICON_CLS.PROJECT_TYPE_CONFIG);
			rootNodes.add(projectTypeFields);
		}
		//exists at least one project specific configuration?
		List<TFieldConfigBean> projectSpecificFieldConfigList = fieldConfigDAO.loadAllByProject(null);
		String projectTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.field.project", locale);
		if (projectSpecificFieldConfigList==null || projectSpecificFieldConfigList.isEmpty()) {
			projectTitle = TreeConfigBL.addInheritedMarkup(projectTitle);
		}
		TreeConfigNodeTO projectFields = new TreeConfigNodeTO(
				TreeConfigIDTokens.encodeRootNode(TreeConfigBL.FIELD_CONFIG, TreeConfigBL.PROJECT), 
				TreeConfigBL.FIELD_CONFIG, TreeConfigBL.PROJECT, 
				projectTitle, false);
		projectFields.setIconCls(ICON_CLS.PROJECT_CONFIG);
		rootNodes.add(projectFields);
		return rootNodes;
	}
	
	/**
	 * Whether there are issue type specific configurations
	 */
	@Override
	public boolean hasIssueTypeSpecificConfig(Integer issueType, Integer projectType, Integer project) {
		List<TFieldConfigBean> fieldConfigBeans = fieldConfigDAO.loadAllByIssueType(issueType, projectType, project);
		return fieldConfigBeans!=null && !fieldConfigBeans.isEmpty();
	}
	
	/**
	 * Whether there are project type specific configurations
	 */
	@Override
	public boolean hasProjectTypeSpecificConfig(Integer projectType) {
		List<TFieldConfigBean> fieldConfigBeans = fieldConfigDAO.loadAllByProjectType(projectType);
		return fieldConfigBeans!=null && !fieldConfigBeans.isEmpty();
	}
	
	/**
	 * Load all project specific configurations (also those which have item type specified)
	 * @param projectIDs if null get all project specific configurations
	 * @return
	 */
	@Override
	public List<ConfigItem> loadAllProjectSpecificConfig(List<Integer> projectIDs) {
		return (List)fieldConfigDAO.loadAllByProjects(projectIDs);
	}
	
	/**
	 * Whether there are specific configurations for system fields or custom fields
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param isCustom
	 * @return
	 */
	@Override
	public boolean hasFieldTypeSpecificConfig(Integer issueType, Integer projectType,  Integer project, boolean isCustom) {
		List<TFieldConfigBean> fieldConfigBeans = fieldConfigDAO.loadAllByFieldType(issueType, projectType, project, Boolean.valueOf(isCustom));
		return fieldConfigBeans==null || fieldConfigBeans.isEmpty();
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
	/*public TFieldConfigBean getValidConfigDirect(Integer issueType, 
			Integer projectType, Integer project, Integer field) {
		//try the issueType-project combination
		if (issueType!=null && project!=null) {
			return fieldConfigDAO.loadByIssueTypeAndProject(field, issueType, project);
		}
		//try the issueType-projectType combination
		if (issueType!=null && projectType!=null) {
			return fieldConfigDAO.loadByIssueTypeAndProjectType(field, issueType, projectType);
		}	
		//try the project only
		if (project!=null) {
			return fieldConfigDAO.loadByProject(field, project);
		}
		//try the projectType only
		if (projectType!=null) {
			return fieldConfigDAO.loadByProjectType(field, projectType);
		}
		//try the issueType only		
		if (issueType!=null) {
			return fieldConfigDAO.loadByIssueType(field, issueType);
		}
		//try the default		
		return fieldConfigDAO.loadDefault(field);
	}*/
	
	/**
	 *  Gets the configuration by itemType and project
	 * @param configRel
	 * @param itemTypeID
	 * @param projectID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByIssueTypeAndProject(Integer configRel, Integer itemTypeID, Integer projectID) {
		return fieldConfigDAO.loadByIssueTypeAndProject(configRel, itemTypeID, projectID);
	}
		
	/**
	 * Gets the configuration by itemType and projectType
	 * @param configRel
	 * @param itemTypeID
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByIssueTypeAndProjectType(Integer configRel, Integer itemTypeID, Integer projectTypeID) {
		return fieldConfigDAO.loadByIssueTypeAndProjectType(configRel, itemTypeID, projectTypeID);
	}
	
	/**
	 * Gets the configuration by project
	 * @param configRel
	 * @param projectID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByProject(Integer configRel, Integer projectID) {
		return fieldConfigDAO.loadByProject(configRel, projectID);
	}
	
	/**
	 * Gets the configuration by projectType
	 * @param configRel
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByProjectType(Integer configRel, Integer projectTypeID) {
		return fieldConfigDAO.loadByProjectType(configRel, projectTypeID);
	}
	
	/**
	 * Gets the configuration by itemType
	 * @param configRel
	 * @param itemTypeID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByItemType(Integer configRel, Integer itemTypeID) {
		return fieldConfigDAO.loadByIssueType(configRel, itemTypeID);
	}
	
	/**
	 * Gets the default configuration
	 * @param configRel
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelDefault(Integer configRel) {
		return FieldConfigBL.loadDefault(configRel);
	}
	
	/**
	 * Deletes the configuration for a leaf or the configurations from a branch 
	 */
	@Override
	public void deleteConfig(TreeConfigIDTokens treeConfigIDTokens) {
		//delete only when not the default configuration!!!
		if (!(treeConfigIDTokens.getIssueTypeID()==null && treeConfigIDTokens.getProjectTypeID()==null && 
				treeConfigIDTokens.getProjectID()==null) || !TreeConfigBL.FIELD.equals(treeConfigIDTokens.getType())) {
			List<TFieldConfigBean> fieldConfigList = loadConfigList(treeConfigIDTokens);
			if (fieldConfigList!=null) {
				for (Iterator<TFieldConfigBean> iter = fieldConfigList.iterator(); iter.hasNext();) {
					TFieldConfigBean fieldConfigBean = iter.next();
					FieldConfigBL.deleteFieldConfig(fieldConfigBean.getObjectID());
				}
			}
		}
	}
	
	/**
	 * Overwrites the inherited configuration for originalConfig 
	 * @param directConfig
	 * @param fallbackConfig
	 */
	@Override
	public Integer overwriteConfig(ConfigItem directConfig, ConfigItem fallbackConfig) {
		if (fallbackConfig==null) {
			return null;
		}
		//set the destFieldConfig configuration properties
		copyExtraInfo(fallbackConfig, directConfig);
		Integer destFieldConfigID = FieldConfigBL.save((TFieldConfigBean)directConfig);
		//copies the field configuration settings (other T...Settings table(s))
		//the settings for destination configuration could exist
		//if exists not the appropriate entries in the T...Settings table(s) should be overwritten, otherwise created
		TFieldBean fieldBean = FieldBL.loadByPrimaryKey(directConfig.getConfigRel());
		FieldType fieldType = FieldType.fieldTypeFactory(fieldBean.getFieldType());
		if (fieldType!=null) {
			IFieldTypeDT fieldTypeDT = fieldType.getFieldTypeDT();
			if (fieldTypeDT!=null) {
				Map<Integer, Object> fallbackSettings = fieldTypeDT.loadSettings(fallbackConfig.getObjectID());
				Map<Integer, Object> directSettings = new HashMap<Integer, Object>();
				fieldTypeDT.copySettings(fallbackSettings, directSettings, destFieldConfigID);
				fieldTypeDT.saveSettings(directSettings, destFieldConfigID);
			}
		}
		return destFieldConfigID;
	}

	/**
	 * Whether after a reset operation a tree refresh is needed
	 * @return
	 */
	@Override
	public boolean refreshEntireTreeAfterReset() {
		//only branch refresh is needed
		return false;
	}
	
	/**
	 * Get the lookup map
	 * @param locale
	 * @return
	 */
	@Override
	public Map<Integer, Map<Integer, ILabelBean>> getLookupMap(Locale locale) {
		Map<Integer, Map<Integer, ILabelBean>> lookupMap = new HashMap<Integer, Map<Integer,ILabelBean>>();
		List<TFieldBean> fieldList = FieldBL.loadAll();
		lookupMap.put(FIELD_KEY, GeneralUtils.createMapFromList(fieldList));
		return lookupMap;
	}	
	
	/**
	 * Get the title for a config
	 * @param cfg
	 * @return
	 */
	@Override
	public String getTitle(ConfigItem cfg, Map<Integer, Map<Integer, ILabelBean>> lookupMap) {
		Map<Integer, ILabelBean> fieldMap = lookupMap.get(FIELD_KEY);
		TFieldBean fieldBean = (TFieldBean)fieldMap.get(((TFieldConfigBean)cfg).getField());
		return fieldBean.getName();
	}
	
	/**
	 * Returns a new instance of TFieldConfigBean
	 */
	@Override
	public ConfigItem createConfigItem() {
		return new TFieldConfigBean();
	}
		
	/**
	 * Deletes the config(s) by by configItem, type and id  
	 * @param configItem
	 * @param type
	 * @param id
	 */
	
	/**
	 * Copies the source configs to the destination configs
	 * If destination configs existed previously they will be deleted or replaced
	 * @param configItem
	 * @param type
	 * @param id
	 */
	/*public void copyConfig(ConfigItem srcConfigItem, ConfigItem destConfigItem, String type, Integer id) {		
		TFieldConfigBean srcFieldConfigBean;
		TFieldConfigBean destFieldConfigBean;
		Integer destFieldConfigID;
		deleteConfig(destConfigItem, type, id);
		List<TFieldConfigBean> fieldConfigList = loadConfigList(srcConfigItem, type, id);
		if (fieldConfigList!=null) {
			for (Iterator<TFieldConfigBean> iter = fieldConfigList.iterator(); iter.hasNext();) {
				srcFieldConfigBean = iter.next();
				destFieldConfigBean = new TFieldConfigBean();
				//set the destination config domain
				FieldBeansHelper.copyDomainProperties(destConfigItem, destFieldConfigBean);
				//set the fieldID because it is probably null for destConfigItem
				destFieldConfigBean.setField(srcFieldConfigBean.getField());
				//set the destination config properties
				FieldBeansHelper.copyFieldConfigProperties(srcFieldConfigBean, destFieldConfigBean);
				
				destFieldConfigID = fieldConfigDAO.save(destFieldConfigBean);

				//copies the field config settings (other T...Settings table(s))
				//the settings for destination config could exist
				//if exists not the appropriate entries in the T...Settings table(s) should be overwrited, othervise created
				//FieldType fieldType = FieldType.getCustomFieldTypeByFieldID(srcFieldConfigBean.getField());
				FieldType fieldType = FieldTypeManager.getFieldType(srcFieldConfigBean.getField());
				if (fieldType!=null) {
					IFieldTypeDT fieldTypeDT = fieldType.getFieldTypeDT();
					if (fieldTypeDT!=null) {
						Map srcSettings = (fieldTypeDT).loadSettings(srcFieldConfigBean.getObjectID());
						Map destSettings = new HashMap();			
						fieldTypeDT.copySettings(srcSettings, destSettings, destFieldConfigID);							
						fieldTypeDT.saveSettings(destSettings, destFieldConfigID);		
					}
				}
			}
		}
	}*/

	/**
	 * @param cfg
	 */
	@Override
	public List load(ConfigItem cfg) {
		List<TFieldConfigBean> fieldConfigs = fieldConfigDAO.loadByFieldConfigParameters((TFieldConfigBean)cfg);
		for (Iterator<TFieldConfigBean> iterator = fieldConfigs.iterator(); iterator.hasNext();) {
			TFieldConfigBean fieldConfigBean = iterator.next();
			//exclude the project specific issue no field from filter
			//configuration because only one issueNo field should be available  
			if (SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO.equals(fieldConfigBean.getField())) {
				iterator.remove();
			}
		}
		return fieldConfigs;
	}

	/**
	 * The extra info to be copied
	 * @param cfgSource
	 * @param cfgTarget
	 */
	@Override
	public void copyExtraInfo(ConfigItem cfgSource, ConfigItem cfgTarget) {
		TFieldConfigBean destFieldConfigBean=(TFieldConfigBean)cfgTarget;
		TFieldConfigBean srcFieldConfigBean=(TFieldConfigBean)cfgSource;
		destFieldConfigBean.setLabel(srcFieldConfigBean.getLabel());
		destFieldConfigBean.setTooltip(srcFieldConfigBean.getTooltip());
		destFieldConfigBean.setRequired(srcFieldConfigBean.getRequired());
		destFieldConfigBean.setHistory(srcFieldConfigBean.getHistory());
	}
	
	@Override
	public Integer saveConfig(ConfigItem cfg) {
		return FieldConfigBL.save((TFieldConfigBean)cfg);
		
	}
	
	@Override
	public ConfigItem loadConfigByPk(Integer pk) {
		return FieldConfigBL.loadByPrimaryKey(pk);
	}
	
	/**
	 * Helper method returning a list of TFieldConfigBean objects by configItem, type and id
	 * Used by delete and copy  
	 * @param treeConfigIDTokens
	 * @return
	 */
	private List<TFieldConfigBean> loadConfigList(TreeConfigIDTokens treeConfigIDTokens) {
		List<TFieldConfigBean> fieldConfigList = new ArrayList<TFieldConfigBean>();
		String type = treeConfigIDTokens.getType();
		Integer issueTypeID = treeConfigIDTokens.getIssueTypeID();
		Integer projectTypeID = treeConfigIDTokens.getProjectTypeID();
		Integer projectID = treeConfigIDTokens.getProjectID();
		Integer fieldID = treeConfigIDTokens.getConfigRelID(); 							
		if (TreeConfigBL.CONFIG_ITEM.equals(type)) {
			//if a concrete field only the direct config is important (used by delete and copy) 
			TFieldConfigBean fieldConfigBean = (TFieldConfigBean)getValidConfigDirect(issueTypeID, 
					projectTypeID, projectID, fieldID);
			if (fieldConfigBean!=null) {
				fieldConfigList.add(fieldConfigBean);
				return fieldConfigList;
			}					
		}		
		if (TreeConfigBL.FIELD.equals(type)) {
			Boolean isCustom = null;
			//SystemFields or CustomFields list at any hierarchy level: get all of them according to configItem
			//if FIELD the entityID should be specified: SYSTEM_FIELD or CUSTOM_FIELD
			if (fieldID!=null) {
				if (fieldID.intValue()==TreeConfigBL.SYSTEM_FIELD) {
					isCustom = Boolean.FALSE;
				} else {
					isCustom = Boolean.TRUE;
				}
			}
			fieldConfigList = fieldConfigDAO.loadAllByFieldType(issueTypeID, projectTypeID, projectID, isCustom);
		} else {
			if (TreeConfigBL.ISSUE_TYPE.equals(type)) {					
					//1. global issueType specific configuration (the second main node from the tree, id==null)
					//2. a concrete issueType configuration from the global issueType specific configuration (child of 1)
					//3. child of a projectType
					//4. child of a project
					fieldConfigList = fieldConfigDAO.loadAllByIssueType(issueTypeID, projectTypeID, projectID);				
			} else {
				if (TreeConfigBL.PROJECT_TYPE.equals(type)) {					
					fieldConfigList = fieldConfigDAO.loadAllByProjectType(projectTypeID);
				} else {
					if (TreeConfigBL.PROJECT.equals(type)) {
						List<Integer> descendantProjectIDs = ProjectBL.getDescendantProjectIDsAsList(projectID);
						fieldConfigList = fieldConfigDAO.loadAllByProjects(descendantProjectIDs);
					}
				}
			}
		}
		return fieldConfigList;
	}
		
}
