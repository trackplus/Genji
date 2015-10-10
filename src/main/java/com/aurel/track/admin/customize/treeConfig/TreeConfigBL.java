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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.ListOptionIconBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;

/**
 * Business logic for a generic tree config designer 
 * @author Adrian Bojani
 *
 */
public class TreeConfigBL {
	
	/*
	 * the constants to specify the field types: custom or system 
	 */
	public static final Integer SYSTEM_FIELD = Integer.valueOf(0);
	public static final Integer CUSTOM_FIELD = Integer.valueOf(1);
	
	/*
	 * the config type constants
	 */
	public static final String SCREEN_CONFIG="screen";
	public static final String FIELD_CONFIG="field";
	public static final String WORKFLOW_CONFIG="workflow";

	public static final String MAIL_TEMPLATE_CONFIG="mailTemplate";

	/*
	 * the node type constants
	 */
	//the first node in the tree and each "System field" and "Custom field" node has this type
	public static final String FIELD="field"; 
	//only the first node in the tree has this type
	public static final String SCREEEN_TYPE="screen";
	public static final String WORKFLOW_TYPE="workflow";
	public static final String MAIL_TEMPLATE_TYPE="mailTemplate";
	public static final String ISSUE_TYPE="issueType";
	public static final String PROJECT_TYPE="projectType";
	public static final String PROJECT="project";
	public static final String CONFIG_ITEM="configItem";
	
	public static interface ICON_CLS {
		static String GLOBAL_CONFIG = "config-global-ticon";
		static String ISSUE_TYPE_CONFIG = "config-issueType-ticon";
		static String PROJECT_TYPE_CONFIG = "config-projectType-ticon";
		static String PROJECT_CONFIG = "config-project-ticon";
        static String PROJECT = "project-ticon";
		
		static String SYSTEM_FIELD = "systemFields-ticon";
		static String CUSTOM_FIELD = "customFields-ticon";
		
		static String ACTION = "config-configItem-ticon";
		static String EVENT = "config-event-ticon";

	}
	
	
	
	static String getFolderInfo(String node, Locale locale) {
		String domainDelimiter = " - ";
		String labelDelimiter = ": ";
		
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
		String configType = treeConfigIDTokens.getConfigType();
		String type = treeConfigIDTokens.getType();
		Integer issueTypeID = treeConfigIDTokens.getIssueTypeID();
		Integer projectTypeID = treeConfigIDTokens.getProjectTypeID();
		Integer projectID = treeConfigIDTokens.getProjectID();
		Integer configRelID = treeConfigIDTokens.getConfigRelID();
	
		String info = null;
		String subdomain = "";
		if (FIELD_CONFIG.equals(configType)) {
			info=LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.field.info", locale);
			if (configRelID!=null && FIELD.equals(type)) {
				if (Integer.valueOf(SYSTEM_FIELD).equals(configRelID)) {
					subdomain = domainDelimiter + LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.lbl.systemField", locale);
				} else {
					if (CUSTOM_FIELD.equals(configRelID)) {
						subdomain = domainDelimiter + LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.lbl.customField", locale);
					} 
				}
			}
		} else {
			if (SCREEN_CONFIG.equals(configType)) {
				info=LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.screen.info", locale);
			} else {
				if (WORKFLOW_CONFIG.equals(configType)) {
					info=LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.workflow.info", locale);
				}else {
					if (MAIL_TEMPLATE_CONFIG.equals(configType)) {
						info=LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.mailTemplate.info", locale);
					}
				}
			}
		}
		List<Integer> domainFields = new LinkedList<Integer>();
		domainFields.add(SystemFields.INTEGER_ISSUETYPE);
		domainFields.add(SystemFields.INTEGER_PROJECT);
		Map<Integer, String> domainLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(domainFields, locale);
		String projectLabel = domainLabels.get(SystemFields.INTEGER_PROJECT);
		String issueTypeLabel = domainLabels.get(SystemFields.INTEGER_ISSUETYPE);
		String projectTypeLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.projectType", locale);
		
		
		String domainLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.domain", locale) + labelDelimiter;	
		String issueTypeName = "";
		String projectTypeName = "";
		String projectName = "";
		
		if (issueTypeID!=null) {
			ILabelBean listTypeBean = LookupContainer.getItemTypeBean(issueTypeID, locale);
			if (listTypeBean!=null) {
				issueTypeName = labelDelimiter + listTypeBean.getLabel();
				issueTypeLabel += issueTypeName;
			}
		}	
		if (projectTypeID!=null) {
			TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
			if (projectTypeBean!=null) {
				projectTypeName = projectTypeBean.getLabel();
				projectTypeLabel += labelDelimiter + projectTypeName;
			}
		}
		if (projectID!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				projectName = projectBean.getLabel();
				projectLabel += labelDelimiter + projectName;
			}	
		} 
		
		if (issueTypeID==null && projectID==null && projectTypeID==null) {
			if (ISSUE_TYPE.equals(type)) {
				domainLabel += issueTypeLabel;
			} else {
				if (PROJECT_TYPE.equals(type)) {
					domainLabel += projectTypeLabel;
				} else {
					if (PROJECT.equals(type)) {
						domainLabel += projectLabel;
					} else {
						if (SCREEN_CONFIG.equals(configType)){
							domainLabel += LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.screen.standard", locale);
						} else {
							domainLabel += LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.field.standard", locale);
						}
					}
				}
			}			
		} else {
			if (issueTypeID!=null) {
				if (projectTypeID!=null) {
					domainLabel += projectTypeLabel + domainDelimiter + issueTypeLabel;
				} else {
					if (projectID!=null) {
						domainLabel += projectLabel + domainDelimiter + issueTypeLabel;
					} else {
						domainLabel += issueTypeLabel;
					}
				}
			} else {
				if (projectTypeID!=null) {
					domainLabel += projectTypeLabel;
				} else {
					if (projectID!=null) {
						domainLabel += projectLabel;
					}
				}
			}
		}
		return TreeConfigJSON.getFolderJSON(domainLabel + subdomain + "<BR>" + info);
	}
		
		
	/**
	 * Get the children of node having the given id
	 * @param nodeID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static List<TreeConfigNodeTO> getChildren(String nodeID, Integer projectOrProjectTypeID,
			boolean fromProjectConfig, TPersonBean personBean, Locale locale){
		List<TreeConfigNodeTO> result=new LinkedList<TreeConfigNodeTO>();
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(nodeID);
		String configType = treeConfigIDTokens.getConfigType();
		String type = treeConfigIDTokens.getType();
		if (type==null) {
			ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
			return configDAO.getFirstLevelNodes(personBean, locale);
		}
		ConfigItem configItem = getNewConfigItem(treeConfigIDTokens);
		if(type.equals(SCREEEN_TYPE)){
			return getItemConfigs(configType, configItem, locale);
		}
		if(type.equals(FIELD)){
			return getFieldTypes(configType, treeConfigIDTokens.getConfigRelID(), configItem, locale);
		}
		if(type.equals(ISSUE_TYPE)){
			return getIssueTypes(configType, treeConfigIDTokens.getIssueTypeID(), configItem, locale, true);
		}
		if(type.equals(PROJECT_TYPE)){
			return getProjectTypes(configType, treeConfigIDTokens.getProjectTypeID(), projectOrProjectTypeID, locale);
		}
		if(type.equals(PROJECT)){
			return getProjects(configType, treeConfigIDTokens.getProjectID(), projectOrProjectTypeID, fromProjectConfig, personBean, true, locale);
		}
		if(type.equals(MAIL_TEMPLATE_TYPE)){
			return getItemConfigs(configType, configItem, locale);
		}
		return result;
	}
		
	/**
	 * Resets the settings for the current configuration
	 * The node can be a leaf node or a folder node
	 * @param nodeID
	 * @param locale
	 * @param servletResponse
	 */
	static void reset(String nodeID, Locale locale, HttpServletResponse servletResponse) {
		String text = null;
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(nodeID);
		String configType = treeConfigIDTokens.getConfigType();
		ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		configDAO.deleteConfig(treeConfigIDTokens);
		String type = treeConfigIDTokens.getType();
		boolean isLeaf = false;
		if (TreeConfigBL.CONFIG_ITEM.equals(type)) {
			//if a leaf node was reseted
			ConfigItem configItemFallback = configDAO.getValidConfigFallback(treeConfigIDTokens.getIssueTypeID(), 
				treeConfigIDTokens.getProjectTypeID(), 
				treeConfigIDTokens.getProjectID(), treeConfigIDTokens.getConfigRelID());
			Map<Integer, Map<Integer, ILabelBean>> lookupMap = configDAO.getLookupMap(locale);
			text = addInheritedMarkup(configDAO.getTitle(configItemFallback, lookupMap));
			isLeaf = true;
		}
		//refresh the tree 
		JSONUtility.encodeJSON(servletResponse, 
			TreeConfigJSON.getResetJSON(nodeID, text,
					!isLeaf, configDAO.refreshEntireTreeAfterReset(),
					treeConfigIDTokens.getIssueTypeID(), treeConfigIDTokens.getProjectTypeID(), treeConfigIDTokens.getProjectID()));
	}
	
	/**
	 * Overwrites the inherited configuration
	 * The node is always a leaf node
	 * @param nodeID
	 * @param locale
	 * @param servletResponse
	 * @return
	 */
	static synchronized void overwrite(String nodeID, Locale locale, HttpServletResponse servletResponse) {
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(nodeID);
		String configType = treeConfigIDTokens.getConfigType();
		ConfigItemFacade configDAO = ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		ConfigItem configItemDirect = overwrite(nodeID);
		Map<Integer, Map<Integer, ILabelBean>> lookupMap = configDAO.getLookupMap(locale);
		String text = configDAO.getTitle(configItemDirect, lookupMap);
		JSONUtility.encodeJSON(servletResponse, 
			TreeConfigJSON.getOverwriteJSON(nodeID, text, false));
	}
	
	/**
	 * Overwrites the inherited configuration
	 * The node is always a leaf node
	 * @param nodeID
	 * @return
	 */
	public static ConfigItem overwrite(String nodeID) {
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(nodeID);
		String configType = treeConfigIDTokens.getConfigType();
		ConfigItemFacade configDAO = ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		Integer issueType = treeConfigIDTokens.getIssueTypeID();
		Integer projectType = treeConfigIDTokens.getProjectTypeID();
		Integer project = treeConfigIDTokens.getProjectID();
		Integer configRel = treeConfigIDTokens.getConfigRelID();
		ConfigItem configItemDirect = configDAO.getValidConfigDirect(issueType, projectType, project, configRel);
		if (configItemDirect==null) {
			//an other user has not overwritten already
			ConfigItem configItemFallback = configDAO.getValidConfigFallback(issueType, projectType, project, configRel);
			configItemDirect = getNewConfigItem(treeConfigIDTokens);
			configItemDirect.setConfigRel(configRel);
			Integer newConfigID = configDAO.overwriteConfig(configItemDirect, configItemFallback);
			if (newConfigID!=null) {
				configItemDirect = configDAO.loadConfigByPk(newConfigID);
			}
		}
		return configItemDirect;
	}	
	
	/**
	 * Downloads the icon file
	 * @return
	 */
	static void downloadIcon(String node, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
		String nodeType = treeConfigIDTokens.getType();
		if (ISSUE_TYPE.equals(nodeType)) {
			Integer optionID = treeConfigIDTokens.getIssueTypeID();
			ListOptionIconBL.downloadForField(servletRequest, servletResponse, ListBL.RESOURCE_TYPES.ISSUETYPE, optionID);
		}
	}
	
	public static ConfigItem getNewConfigItem(TreeConfigIDTokens treeConfigIDTokens) {
		String configType = treeConfigIDTokens.getConfigType();
		ConfigItemFacade configDAO = ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		ConfigItem configItem = configDAO.createConfigItem();
		configItem.setIssueType(treeConfigIDTokens.getIssueTypeID());
		configItem.setProjectType(treeConfigIDTokens.getProjectTypeID());
		configItem.setProject(treeConfigIDTokens.getProjectID());
		return configItem;
	}

	/**
	 * Loads the configuration nodes
	 * @param configType
	 * @param configItem
	 * @param locale
	 * @return
	 */
	private static List<TreeConfigNodeTO> getItemConfigs(String configType,
			ConfigItem configItem, Locale locale){
		ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		//the configurations matching exactly
		List<ConfigItem> exactMatchConfigList=configDAO.load(configItem);
		ConfigItem defaultConfig=configDAO.createConfigItem();
		//for defaults (issueType, projectType and project is null) at least the isCustom flag should be specified
		if (defaultConfig instanceof TFieldConfigBean) {
			((TFieldConfigBean)defaultConfig).setCustom(((TFieldConfigBean)configItem).isCustom());
		}
		//the default configurations
		List<ConfigItem> defaultConfigList=configDAO.load(defaultConfig);
		//other, possibly more specific fallback lists
		List<ConfigItem> issueTypeSpecificConfigList = new LinkedList<ConfigItem>();
		List<ConfigItem> projectTypeSpecificConfigList = new LinkedList<ConfigItem>();
		List<ConfigItem> issueTypeProjectTypeSpecificConfigList = new LinkedList<ConfigItem>();
		List<ConfigItem> projectSpecificConfigList = new LinkedList<ConfigItem>();
		List<ConfigItem> ancestorProjectsSpecificList = new LinkedList<ConfigItem>();
		List<ConfigItem> issueTypeAncestorProjectsSpecificList = new LinkedList<ConfigItem>();
		Integer issueTypeID = configItem.getIssueType();
		Integer projectID = configItem.getProject();
		Integer projectTypeID = configItem.getProjectType();
		if (projectID!=null) {
				//get project specific fallbacks: only if issueType is specified (otherwise the projectList is exactMatchConfigList)
				if (issueTypeID!=null) {
					//get ancestor project - issueType specific combination
					issueTypeAncestorProjectsSpecificList = getAncestorProjectsItems(configItem, configDAO);
					configItem.setIssueType(null);
					projectSpecificConfigList = configDAO.load(configItem);
					//get ancestor project specific configurations (without explicit issue type)
					ancestorProjectsSpecificList = getAncestorProjectsItems(configItem, configDAO);
					//set back the issue type for further processing the configItem
					configItem.setIssueType(issueTypeID);
				}
				//get the projectType fallbacks
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					configItem.setProjectType(projectBean.getProjectType());
					configItem.setProject(null);
					if (issueTypeID!=null) {
						//get projectType/issueType fallbacks
						issueTypeProjectTypeSpecificConfigList = configDAO.load(configItem);
					}
				}
				//get the projectType specific fallbacks
				configItem.setIssueType(null);
				projectTypeSpecificConfigList = configDAO.load(configItem);
				//reset the projectType (which was set before) to null and leave the project null
				configItem.setProjectType(null);
				configItem.setIssueType(issueTypeID);
				//get the issueType specific fallbacks
				if (issueTypeID!=null) {
					issueTypeSpecificConfigList = configDAO.load(configItem);
				}
				configItem.setProject(projectID);
		} else {
			//try the issueType-projectType combination 
			if (projectTypeID!=null) {
				if (issueTypeID!=null) {
					configItem.setProjectType(null);
					//load issueType fallback configurations list
					issueTypeSpecificConfigList = configDAO.load(configItem);
					//restore the original projectType
					configItem.setProjectType(projectTypeID);
				}
			}
		}
		List<ConfigItem> fallbackList=combine(defaultConfigList, issueTypeSpecificConfigList, projectTypeSpecificConfigList,
				issueTypeProjectTypeSpecificConfigList, projectSpecificConfigList, ancestorProjectsSpecificList, issueTypeAncestorProjectsSpecificList);
		List<TreeConfigNodeTO> treeNodes = mix(configType, exactMatchConfigList, fallbackList, configItem, locale);
		return treeNodes;
	}
	
	/**
	 * Gets the ancestor project specific items
	 * @param configItem
	 * @param configDAO
	 * @return
	 */
	private static List<ConfigItem> getAncestorProjectsItems(ConfigItem configItem, ConfigItemFacade configDAO) {
		List<ConfigItem> ancestorProjectsSpecificList = new LinkedList<ConfigItem>();
		Integer projectID = configItem.getProject();
		TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
		if (projectBean!=null) {
			Integer parentProjectID = projectBean.getParent();
			while (parentProjectID!=null) {
				TProjectBean parentProjectBean = LookupContainer.getProjectBean(parentProjectID);
				if (parentProjectBean!=null) {
					if (EqualUtils.equal(projectBean.getProjectType(), parentProjectBean.getProjectType())) {
						//get only for ancestor projects with the same project type
						configItem.setProject(parentProjectID);
						List<ConfigItem> ancestorProjectSpecificList =  configDAO.load(configItem);
						if (ancestorProjectSpecificList!=null) {
							for (ConfigItem configItemAncestorProject : ancestorProjectSpecificList) {
								Integer configRelNewAncestorProject = configItemAncestorProject.getConfigRel();
								boolean found = false;
								for (ConfigItem configItemAll : ancestorProjectsSpecificList) {
									Integer configRelAlreadyAddedAncestor = configItemAll.getConfigRel();
									if (configRelNewAncestorProject.equals(configRelAlreadyAddedAncestor)) {
										//add the nearest ancestor for the same configRelation
										found = true;
										break;
									}
								}
								if (!found) {
									ancestorProjectsSpecificList.add(configItemAncestorProject);
								}
							}
						}
					}
					parentProjectID = parentProjectBean.getParent();
				} else {
					break;
				}
			}
		}
		//set the project back to original value for further processing
		configItem.setProject(projectID);
		return ancestorProjectsSpecificList;
	}
	
	/**
	 * Creates a ConfigItem map by configRel (fieldID or actionID)
	 * @param configItemList
	 * @return
	 */
	private static Map<Integer, ConfigItem> getConfigItemsByConfigRel(List<ConfigItem> configItemList) {
		Map<Integer, ConfigItem> configItemsByConfigRel = new HashMap<Integer, ConfigItem>();
		if (configItemList!=null) {
			for (ConfigItem configItem : configItemList) {
				configItemsByConfigRel.put(configItem.getConfigRel(), configItem);
			}
		}
		return configItemsByConfigRel;
	}
	
	/**
	 * Combine the lists the more specific will be added to the final list
	 * @param defaultList
	 * @param issueTypeList
	 * @param issueTypeProjectTypeList
	 * @param projectList
	 * @return
	 */
	private static List<ConfigItem> combine(List<ConfigItem> defaultList, List<ConfigItem> issueTypeList, 
			List<ConfigItem> projectTypeList, List<ConfigItem> issueTypeProjectTypeList, List<ConfigItem> projectList,
			List<ConfigItem> ancestorProjectsList, List<ConfigItem> issueTypeAncestorProjectsList) {
		List<ConfigItem> result=new LinkedList<ConfigItem>();
		Map<Integer, ConfigItem> issueTypeMap = getConfigItemsByConfigRel(issueTypeList);
		Map<Integer, ConfigItem> issueTypeProjectTypeMap = getConfigItemsByConfigRel(issueTypeProjectTypeList);
		Map<Integer, ConfigItem> projectTypeMap = getConfigItemsByConfigRel(projectTypeList);
		Map<Integer, ConfigItem> projectMap = getConfigItemsByConfigRel(projectList);
		Map<Integer, ConfigItem> ancestorProjectMap = getConfigItemsByConfigRel(ancestorProjectsList);
		Map<Integer, ConfigItem> issueTypeAncestorProjectMap = getConfigItemsByConfigRel(issueTypeAncestorProjectsList);
		for (ConfigItem cfgDefault : defaultList) {
			Integer configRel = cfgDefault.getConfigRel();
			ConfigItem cfgSpecific = issueTypeAncestorProjectMap.get(configRel);
			if (cfgSpecific!=null){
				result.add(cfgSpecific);
			} else {
				cfgSpecific = projectMap.get(configRel);
				if (cfgSpecific!=null){
					result.add(cfgSpecific);
				} else {
					cfgSpecific = ancestorProjectMap.get(configRel);
					if (cfgSpecific!=null){
						result.add(cfgSpecific);
					} else {
						cfgSpecific = issueTypeProjectTypeMap.get(configRel);
						if (cfgSpecific!=null){
							result.add(cfgSpecific);
						} else {
							cfgSpecific = projectTypeMap.get(configRel);
							if (cfgSpecific!=null) {
								result.add(cfgSpecific);
							} else {
								cfgSpecific = issueTypeMap.get(configRel);
								if (cfgSpecific!=null) {
									result.add(cfgSpecific);
								} else {
									result.add(cfgDefault);
								} 
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * TODO format the node title using an ext js tree node renderer (once available) 
	 * @param text
	 * @return
	 */
	public static String addInheritedMarkup(String text) {
		return "<span class='configItemUnChanged'>" + text + "</span>";
	}
	
	/**
	 * Get a list of TreeNodeConfig objects.
	 * Each TreeNodeConfig will have the actual issueType, projectType and project attributes
	 * but the objectID will be either the actual configID or the nearest more specific fallback configID 
	 * @param exactMatchList
	 * @param fallbackList
	 * @param configItem
	 * @return
	 */
	private static List<TreeConfigNodeTO> mix(String configType, List<ConfigItem> exactMatchList, 
			List<ConfigItem> fallbackList, ConfigItem configItem, Locale locale){
		ConfigItemFacade configDAO = ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		Map<Integer, ConfigItem> exactMatchMap = getConfigItemsByConfigRel(exactMatchList);
		List<TreeConfigNodeTO> result=new ArrayList<TreeConfigNodeTO>();
		Map<Integer, Map<Integer, ILabelBean>> lookupMap = configDAO.getLookupMap(locale);
		for (int i = 0; i < fallbackList.size(); i++) {
			ConfigItem fallbackConfig=fallbackList.get(i);
			String nodeID=null;
			TreeConfigNodeTO treeConfigNodeTO=null;
			ConfigItem exactMatchConfig= exactMatchMap.get(fallbackConfig.getConfigRel());
			Integer assignedID = null;
			String iconCls=null;
			if(exactMatchConfig!=null){
				if (configType.equals(SCREEN_CONFIG)) {
					TScreenConfigBean screenConfigBean = (TScreenConfigBean)exactMatchConfig;
					assignedID = screenConfigBean.getScreen();
					iconCls=ICON_CLS.ACTION;
				}
				if (configType.equals(MAIL_TEMPLATE_CONFIG)) {
					TMailTemplateConfigBean mailTemplateConfigBean = (TMailTemplateConfigBean)exactMatchConfig;
					assignedID = mailTemplateConfigBean.getMailTemplate();
					iconCls=ICON_CLS.EVENT;
				}
				nodeID=TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(configType, CONFIG_ITEM, exactMatchConfig));
				treeConfigNodeTO=new TreeConfigNodeTO(nodeID, configType, 
						CONFIG_ITEM, configDAO.getTitle(exactMatchConfig, lookupMap), true);
				treeConfigNodeTO.setInherited(false);
				treeConfigNodeTO.setDefaultConfig(exactMatchConfig.isDefault());
			} else {
				if (configType.equals(SCREEN_CONFIG)) {
					iconCls=ICON_CLS.ACTION;
				}
				if (configType.equals(MAIL_TEMPLATE_CONFIG)) {
					iconCls=ICON_CLS.EVENT;
				}
				fallbackConfig.setIssueType(configItem.getIssueType());
				fallbackConfig.setProjectType(configItem.getProjectType());
				fallbackConfig.setProject(configItem.getProject());
				nodeID=TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(configType, CONFIG_ITEM, fallbackConfig));
				treeConfigNodeTO=new TreeConfigNodeTO(nodeID, configType, 
						CONFIG_ITEM, addInheritedMarkup(configDAO.getTitle(fallbackConfig, lookupMap)), true);
				treeConfigNodeTO.setInherited(true);
			}
			//set the icon for node	
			if (configType.equalsIgnoreCase(FIELD_CONFIG)){
				Integer fieldID=null;
				if(exactMatchConfig!=null){
					fieldID=((TFieldConfigBean)exactMatchConfig).getField();
				}else{
					fieldID=((TFieldConfigBean)fallbackConfig).getField();
				}
				FieldType fieldType = FieldTypeManager.getFieldType(fieldID);
				if(fieldType!=null){
					treeConfigNodeTO.setIconCls(fieldType.getIconName());
				}
			} else {
				treeConfigNodeTO.setIconCls(iconCls);
				treeConfigNodeTO.setAssignedID(assignedID);
			}
			result.add(treeConfigNodeTO);
		}
		return result;
	}
	
	private static void setLastBranchNodes(TreeConfigNodeTO node, ConfigItem configItem) {
		node.setChildrenAreLeaf(true);
		node.setIssueType(configItem.getIssueType());
		node.setProjectType(configItem.getProjectType());
		node.setProject(configItem.getProject());
	}
	
	/**
	 * Gets the fields hierarchy: 
	 * 	-	for "Field" (objectID=null) get the "System Fields" and "Custom Fields"
	 * 	-	for either "System Fields" or "Custom Fields" get the system fields and custom fields list
	 * @return
	 */
	private static List<TreeConfigNodeTO> getFieldTypes(String configType, Integer objectID, 
			ConfigItem configItem, Locale locale){
		List<TreeConfigNodeTO> result=new ArrayList<TreeConfigNodeTO>();
		boolean defaultConfig = configItem.getIssueType()==null && configItem.getProjectType()==null && configItem.getProject()==null;
		TFieldConfigBean cfg = (TFieldConfigBean)configItem;
		if (objectID==null) {
			//add the system fields node
			cfg.setCustom(false);
			cfg.setConfigRel(Integer.valueOf(SYSTEM_FIELD));
			String nodeID=TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(configType, FIELD, cfg));
			ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
			String systemFields = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.lbl.systemField", locale);
			if (configDAO.hasFieldTypeSpecificConfig(configItem.getIssueType(), configItem.getProjectType(), configItem.getProject(), false)) {
				systemFields = addInheritedMarkup(systemFields);
			}
			TreeConfigNodeTO node=new TreeConfigNodeTO(nodeID, configType, FIELD,systemFields, false);
			node.setIconCls(ICON_CLS.SYSTEM_FIELD);
			node.setDefaultConfig(defaultConfig);
			setLastBranchNodes(node, configItem);
			result.add(node);
			//add the custom fields node
			cfg.setCustom(true);
			cfg.setConfigRel(CUSTOM_FIELD);
			nodeID=TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(configType, FIELD, cfg));
			String customFields = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.lbl.customField", locale);
			if (configDAO.hasFieldTypeSpecificConfig(configItem.getIssueType(), configItem.getProjectType(), configItem.getProject(), true)) {
				customFields = addInheritedMarkup(customFields);
			}
			node=new TreeConfigNodeTO(nodeID,configType, FIELD, customFields, false);
			node.setIconCls(ICON_CLS.CUSTOM_FIELD);
			node.setDefaultConfig(defaultConfig);
			setLastBranchNodes(node, configItem);
			result.add(node);
		} else {
			if (Integer.valueOf(SYSTEM_FIELD).equals(objectID)) {
				//for this node get the system fields
				cfg.setCustom(false);
			} else {
				if  (Integer.valueOf(CUSTOM_FIELD).equals(objectID)) {
					//for this node get the system fields
					cfg.setCustom(true);
				}
			}
			return getItemConfigs(configType, cfg, locale);
		}
		return result;
	}
	
	/**
	 * Get only those issue types which are allowed for projectType or project
	 * If neither projectType nor project is specified then load all issue types
	 * @param configItem
	 * @param locale
	 * @return
	 */
	private static List<TListTypeBean> getIssueTypesByConfigItem(ConfigItem configItem, Locale locale) {
		Integer projectTypeID = configItem.getProjectType();
		Integer projectID = configItem.getProject();
		if (configItem.getProjectType()!=null) {
			//only the allowed issue types by project type
			return LocalizeUtil.localizeDropDownList(
					IssueTypeBL.loadAllowedByProjectType(projectTypeID), locale);
		} else {
			if (projectID!=null) {
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					//only the allowed issue types by project type
					return LocalizeUtil.localizeDropDownList(
							IssueTypeBL.loadAllowedByProjectType(projectBean.getProjectType()), locale);
				}
			}
		}
		return (List)IssueTypeBL.loadAll(locale);
	}
	
	/**
	 * Gets the issueTypes hierarchy: 
	 * 	-	for "Issue Type" (objectID=null) get the defined issue types
	 * 	-	for a concrete issue type get the fields hierarchy
	 * @param configType
	 * @param objectID
	 * @param configItem
	 * @param locale
	 * @param global
	 * @return
	 */
	private static List<TreeConfigNodeTO> getIssueTypes(String configType, Integer objectID, 
			ConfigItem configItem, Locale locale, boolean global){
		List<TreeConfigNodeTO> result=new LinkedList<TreeConfigNodeTO>();
		if(objectID==null){
			//"global" issue types node or a project type node or a project node: issue type children
			if (FIELD_CONFIG.equals(configType) && !global) {
				//field types for project/project type specific but not issue type specific
				result.addAll(getFieldTypes(configType, null, configItem, locale));
			}
			ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
			//add each valid issue type
			List<TListTypeBean> issueTypeList=getIssueTypesByConfigItem(configItem, locale);
				for (TListTypeBean listTypeBean : issueTypeList) {
					Integer issueTypeID = listTypeBean.getObjectID();
					String name = listTypeBean.getLabel();
					if (!configDAO.hasIssueTypeSpecificConfig(
							issueTypeID, configItem.getProjectType(), configItem.getProject())) {
						name = addInheritedMarkup(name);
					}
					configItem.setIssueType(issueTypeID);
					String nodeID = TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(configType, ISSUE_TYPE, configItem));
					
					TreeConfigNodeTO node = new TreeConfigNodeTO(nodeID, configType, ISSUE_TYPE, name, false);
					node.setIcon("treeConfig!downloadIcon.action?node="+nodeID);
					if (SCREEN_CONFIG.equals(configType)||MAIL_TEMPLATE_CONFIG.equals(configType)) {
						setLastBranchNodes(node, configItem);
					}
					result.add(node);
				}
		} else {
			//children for a specific issue type
			configItem.setIssueType(objectID);
			if (SCREEN_CONFIG.equals(configType)||MAIL_TEMPLATE_CONFIG.equals(configType)) {
				return getItemConfigs(configType, configItem, locale);
			} else {
				if (FIELD_CONFIG.equals(configType)) {
					return getFieldTypes(configType, null, configItem, locale);
				}
			}
		}
		return result;
	}

	/**
	 * Gets the projectTypes hierarchy: 
	 * 	-	for "Project Type" (objectID=null) get the defined project types
	 * 	-	for a concrete project type get the issue types hierarchy
	 * @param configType
	 * @param objectID
	 * @param projectTypeID if specified we are in workflow configuration by projectType configuration
	 * @param locale
	 * @return
	 */
	private static List<TreeConfigNodeTO> getProjectTypes(String configType,
			Integer objectID, Integer projectTypeID, Locale locale){
		ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		List<TreeConfigNodeTO> result=new ArrayList<TreeConfigNodeTO>();
		if (objectID==null) {
			//the project types node: get the project type children
			List<TProjectTypeBean> projectTypeBeans = null;
				projectTypeBeans=ProjectTypesBL.loadAll();
				for (TProjectTypeBean projectTypeBean: projectTypeBeans) {
					TreeConfigNodeTO treeConfigNodeTO = getProjectTypeNode(projectTypeBean.getObjectID(),
							projectTypeBean.getLabel(), configType, ICON_CLS.PROJECT_TYPE_CONFIG);
					result.add(treeConfigNodeTO);
				}
		} else {
			//children for a specific project type
			ConfigItem cfg=configDAO.createConfigItem();
			cfg.setProjectType(objectID);
			return getIssueTypes(configType, null, cfg, locale, false);
		}
		return result;
	}
	
	public static TreeConfigNodeTO getProjectTypeNode(Integer projectTypeID, String text, String configType, String iconCls) {
		ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);	
		if (!configDAO.hasProjectTypeSpecificConfig(projectTypeID)) {
			text = addInheritedMarkup(text);
		}
		String nodeID = getProjectTypeBranchNodeID(projectTypeID, configType);
		TreeConfigNodeTO treeConfigNodeTO = new TreeConfigNodeTO(nodeID, configType, PROJECT_TYPE, text, false);
		treeConfigNodeTO.setIconCls(ICON_CLS.PROJECT_TYPE_CONFIG);
		return treeConfigNodeTO;
	}
	
	/**
	 * Get the nodeID of the (not visible) root node for project specific field/form configurations
	 * @param projectTypeID
	 * @param configType
	 * @return
	 */
	public static  String getProjectTypeBranchNodeID(Integer projectTypeID, String configType) {
		ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		ConfigItem cfg=configDAO.createConfigItem();
		cfg.setProjectType(projectTypeID);
		return TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(configType, PROJECT_TYPE, cfg));
	}
	
	/**
	 * Gets the projects hierarchy: 
	 * 	-	for "Project" (objectID=null) get the defined projects
	 * 	-	for a concrete project get the issue types hierarchy
	 * @param configType
	 * @param objectID
	 * @param projectID if specified we are in workflow configuration by project configuration
	 * @param fromProjectConfig
	 * @param personBean
	 * @param allIfSystemAdmin
	 * @param locale
	 * @return
	 */
	private static List<TreeConfigNodeTO> getProjects(String configType, Integer objectID, Integer projectID,
			boolean fromProjectConfig, TPersonBean personBean, boolean allIfSystemAdmin, Locale locale){
		ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		boolean isWorkflow = WORKFLOW_CONFIG.equals(configType);
		if (objectID==null) {
			//the main project nodes
			boolean firstLevel = false;
			List<TProjectBean> projectBeans = null;
				firstLevel = personBean.isSys();
				projectBeans = ProjectBL.getActiveInactiveFirstLevelProjectsByProjectAdmin(personBean, allIfSystemAdmin);
				return getProjectNodes(projectBeans, configType, firstLevel);
		} else {
			//children for a specific project
			List<TreeConfigNodeTO> result = new LinkedList<TreeConfigNodeTO>();
			ConfigItem cfg=configDAO.createConfigItem();
			cfg.setProject(objectID);
			if (!fromProjectConfig) {
				//show subprojects
				List<TProjectBean> subprojectBeanList = ProjectBL.loadActiveInactiveSubrojects(objectID);
				if (subprojectBeanList!=null && !subprojectBeanList.isEmpty()) {
						result.addAll(getProjectNodes(subprojectBeanList, configType, false));
				}
			}
			result.addAll(getIssueTypes(configType, null, cfg, locale, false));
			return result;
		}
	}
	
	/**
	 * Creates the project nodes based on projectBeans
	 * @param projectBeans
	 * @param configType
	 * @return
	 */
	private static List<TreeConfigNodeTO> getProjectNodes(List<TProjectBean> projectBeans, String configType, boolean firstLevel) {
		List<TreeConfigNodeTO> result=new LinkedList<TreeConfigNodeTO>();
		if (projectBeans!=null) {
			Map<Integer, Set<Integer>> descendantMap = ProjectBL.getDescendantProjectHierarchyMap(projectBeans);
			ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
			Map<Integer, ConfigItem> projectConfigMap = null;
			if (firstLevel) {
				List<ConfigItem> projectItemTypeConfigList = configDAO.loadAllProjectSpecificConfig(null);
				if (projectItemTypeConfigList!=null) {
					projectConfigMap = new HashMap<Integer, ConfigItem>();
					for (ConfigItem configItem : projectItemTypeConfigList) {
						Integer projectID = configItem.getProject();
						projectConfigMap.put(projectID, configItem);
					}
				}
			} else {
				Set<Integer> allDescendantsSet = new HashSet<Integer>();
				for (TProjectBean projectBean : projectBeans) {
					Integer projectID = projectBean.getObjectID();
					allDescendantsSet.add(projectID);
					Set<Integer> descendantsSet = descendantMap.get(projectID);
					if (descendantsSet!=null) {
						allDescendantsSet.addAll(descendantsSet);
					}
				}
				List<ConfigItem> projectConfigList = configDAO.loadAllProjectSpecificConfig(GeneralUtils.createListFromSet(allDescendantsSet));
				if (projectConfigList!=null) {
					projectConfigMap = new HashMap<Integer, ConfigItem>();
					for (ConfigItem configItem : projectConfigList) {
						Integer projectID = configItem.getProject();
						projectConfigMap.put(projectID, configItem);
					}
				}
			}
			for (TProjectBean projectBean : projectBeans) {
				Integer projectID = projectBean.getObjectID();
				boolean descendantHasConfig = false;
				if (projectConfigMap.get(projectID)!=null) {
					descendantHasConfig = true;
				} else {
					Set<Integer> descendantProjectIDs = descendantMap.get(projectID);
					if (descendantProjectIDs!=null) {
						for (Integer descendantProjectID : descendantProjectIDs) {
							if (projectConfigMap.containsKey(descendantProjectID)) {
								descendantHasConfig = true;
								break;
							}
						}
					}
				}
				String text = projectBean.getLabel(); 
				if (!descendantHasConfig) {
					text = addInheritedMarkup(text);
				}
				String nodeID = getProjectBranchNodeID(projectID, configType); 
				TreeConfigNodeTO treeConfigNodeTO = new TreeConfigNodeTO(nodeID, configType, PROJECT, text, false);
				treeConfigNodeTO.setIconCls(ICON_CLS.PROJECT);
				result.add(treeConfigNodeTO);
			}
		}
		return result;
	}
	
	/**
	 * Get the nodeID of the (not visible) root node for project specific field/form configurations
	 * @param projectID
	 * @param configType
	 * @return
	 */
	public static  String getProjectBranchNodeID(Integer projectID, String configType) {
		ConfigItemFacade configDAO=ConfigItemFacadeFactory.getInstance().getConfigItemFacade(configType);
		ConfigItem cfg=configDAO.createConfigItem();
		cfg.setProject(projectID);
		return TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(configType, PROJECT, cfg)); 
	}
	
	/**
	 * Compare to config items if have the same values 
	 * for issueType,project,projectType and config relation 
	 * 
	 * @param cfg1
	 * @param cfg2
	 * @return
	 */
	public static boolean isTheSameConfig(ConfigItem cfg1,ConfigItem cfg2){
		if(!compare(cfg1.getIssueType(), cfg2.getIssueType())){
			return false;
		}
		if(!compare(cfg1.getConfigRel(), cfg2.getConfigRel())){
			return false;
		}
		if(!compare(cfg1.getProject(), cfg2.getProject())){
			return false;
		}
		if(!compare(cfg1.getProjectType(), cfg2.getProjectType())){
			return false;
		}
		return true;
	}
	
	
	private static boolean compare(Object o1,Object o2){
		if(o1==o2) return true;
		if(o1==null||o2==null) return false;
		return o1.equals(o2);
	}
	
	/**
	 * Obtain a configItem from an encoded string
	 * @param widgetId
	 * @return
	 */
}
