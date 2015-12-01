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

package com.aurel.track.admin.customize.treeConfig;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
/**
 * A facade to DAO for ConfigItem and some business logic
 * @author Adrian Bojani
 *
 */
public interface ConfigItemFacade {

	/**
	 * Expand a tree node from tree
	 * @return
	 */
	List<TreeConfigNodeTO> getFirstLevelNodes(TPersonBean personBean, 
			Locale locale);
		
	/**
	 * Whether there are issue type specific configurations
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @return
	 */
	boolean hasIssueTypeSpecificConfig(Integer issueType, Integer projectType, Integer project);
	
	/**
	 * Whether there are project type specific configurations
	 * @param projectType
	 * @return
	 */
	boolean hasProjectTypeSpecificConfig(Integer projectType);
	
	/**
	 * Whether there are project specific configurations
	 * @param projects
	 * @return
	 */
	//boolean hasProjectSpecificConfig(List<Integer> projects);
	
	/**
	 * Load all project specific configurations (also those which have item type specified)
	 * @param projectIDs if null get all project specific configurations
	 * @return
	 */
	List<ConfigItem> loadAllProjectSpecificConfig(List<Integer> projectIDs);
	
	/**
	 * Whether there are specific configurations for system fields or custom fields
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param isCustom
	 * @return
	 */
	public boolean hasFieldTypeSpecificConfig(Integer issueType, Integer projectType,  Integer project, boolean isCustom);
	
	/**
	 * Gets the configuration which corresponds to the domain exactly
	 * This might be null
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param configRel
	 * @return
	 */
	ConfigItem getValidConfigDirect(Integer issueType, 
			Integer projectType, Integer project, Integer configRel);
	
	/**
	 * Gets the nearest fall back configuration corresponding to the domain
	 * This should never be null (it should be at least the default configuration) 
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param configRel
	 * @return
	 */
	ConfigItem getValidConfigFallback(Integer issueType, 
			Integer projectType, Integer project, Integer configRel);
	
	/**
	 * Deletes configurations for a node
	 * @param treeConfigIDTokens
	 */
	void deleteConfig(TreeConfigIDTokens treeConfigIDTokens);
	
	/**
	 * Overwrites the inherited configuration for originalConfig 
	 * @param directConfig
	 * @param fallbackConfig
	 */
	Integer overwriteConfig(ConfigItem directConfig, ConfigItem fallbackConfig);
	
	/**
	 * Whether after a reset operation the entire tree should be refreshed or only the node which was reset
	 * @return
	 */
	boolean refreshEntireTreeAfterReset();
	
	/**
	 * Get the lookup map for title
	 * @param locale
	 * @return
	 */
	Map<Integer, Map<Integer, ILabelBean>> getLookupMap(Locale locale);
	
	/**
	 * Get the title for a config
	 * @param cfg
	 * @return
	 */
	String getTitle(ConfigItem cfg, Map<Integer, Map<Integer, ILabelBean>> lookupMap);
	
	/**
	 * Load all configs that respect the ConfigItem schema as parameter
	 * @param cfg
	 * @return
	 */
	List<ConfigItem> load(ConfigItem cfg);
	
	/**
	 * Copies the config(s) from a srcConfigItem to the destConfigItem
	 * @param srcConfigItem
	 * @param destConfigItem
	 * @param type
	 * @param id
	 */
	/*void copyConfig(ConfigItem srcConfigItem, 
			ConfigItem destConfigItem, String type, Integer id);*/
	
	/**
	 * Create a new ConfigItem
	 * @return
	 */
	ConfigItem createConfigItem();

	
	/**
	 * Copy values from one config to another
	 * do not copy the objectID
	 * @param cfgSource
	 * @param cfgTarget
	 */
	void copyExtraInfo(ConfigItem cfgSource,ConfigItem cfgTarget);
	
	
	/**
	 * Save a config item
	 * @param cfg
	 */
	Integer saveConfig(ConfigItem cfg);
	
	
	/**
	 * Load a config item with given objectID
	 * @param objectID
	 * @return
	 */
	ConfigItem loadConfigByPk(Integer objectID);
}
