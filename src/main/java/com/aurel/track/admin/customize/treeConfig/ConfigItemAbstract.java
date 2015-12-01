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

import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.util.EqualUtils;

public abstract class ConfigItemAbstract {
	
	/**
	 *  Gets the configuration by itemType and project
	 * @param configRel
	 * @param itemTypeID
	 * @param projectID
	 * @return
	 */
	public abstract ConfigItem loadForConfigRelByIssueTypeAndProject(Integer configRel, Integer itemTypeID, Integer projectID);
		
	/**
	 * Gets the configuration by itemType and projectType
	 * @param configRel
	 * @param itemTypeID
	 * @param projectTypeID
	 * @return
	 */
	public abstract ConfigItem loadForConfigRelByIssueTypeAndProjectType(Integer configRel, Integer itemTypeID, Integer projectTypeID);
	
	/**
	 * Gets the configuration by project
	 * @param configRel
	 * @param projectID
	 * @return
	 */
	public abstract ConfigItem loadForConfigRelByProject(Integer configRel, Integer projectID);
	
	/**
	 * Gets the configuration by projectType
	 * @param configRel
	 * @param projectTypeID
	 * @return
	 */
	public abstract ConfigItem loadForConfigRelByProjectType(Integer configRel, Integer projectTypeID);
	
	/**
	 * Gets the configuration by itemType
	 * @param configRel
	 * @param itemTypeID
	 * @return
	 */
	public abstract ConfigItem loadForConfigRelByItemType(Integer configRel, Integer itemTypeID);
	
	/**
	 * Gets the default configuration
	 * @param configRel
	 * @return
	 */
	public abstract ConfigItem loadForConfigRelDefault(Integer configRel);
	
	/**
	 * Whether the only project or only projectTyp specific configuration can be defined
	 * @return
	 */
	public boolean hasProjectOrProjectTypSpecificConfigs() {
		return true;
	}
	
	/**
	 * Gets the configuration which corresponds to the domain exactly
	 * This might be null
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 * @param configRel
	 * @return
	 */
	public ConfigItem getValidConfigDirect(Integer issueTypeID, 
			Integer projectTypeID, Integer projectID, Integer configRel) {
		//try the issueType-project combination
		if (issueTypeID!=null && projectID!=null) {
			return loadForConfigRelByIssueTypeAndProject(configRel, issueTypeID, projectID);
		}
		//try the issueType-projectType combination
		if (issueTypeID!=null && projectTypeID!=null) {
			return loadForConfigRelByIssueTypeAndProjectType(configRel, issueTypeID, projectTypeID);
		}
		if (hasProjectOrProjectTypSpecificConfigs()) {
			//try the project only
			if (projectID!=null) {
				return loadForConfigRelByProject(configRel, projectID);
			}
			//try the projectType only
			if (projectTypeID!=null) {
				return loadForConfigRelByProjectType(configRel, projectTypeID);
			}
		}
		//try the issueType only		
		if (issueTypeID!=null) {
			return loadForConfigRelByItemType(configRel, issueTypeID);
		}
		//try the default		
		return loadForConfigRelDefault(configRel);
	}
	
	/**
	 * Gets the nearest valid configuration
	 * @param issueTypeID
	 * @param projectTypeID
	 * @param projectID
	 * @param configRel
	 * @return
	 */
	public ConfigItem getValidConfigFallback(Integer issueTypeID, Integer projectTypeID, Integer projectID, Integer configRel) {
		ConfigItem configItem;
		//try the issueType-project combination
		if (issueTypeID!=null && projectID!=null) {
			configItem = loadForConfigRelByIssueTypeAndProject(configRel, issueTypeID, projectID);
			if (configItem!=null) {
				return configItem;
			} else {
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean!=null) {
					Integer parentProjectID = projectBean.getParent();
					//get the ancestor project and issueType combination
					while (parentProjectID!=null) {
							TProjectBean parentProjectBean = LookupContainer.getProjectBean(parentProjectID);
							if (parentProjectBean!=null) {
								if (EqualUtils.equal(projectBean.getProjectType(), parentProjectBean.getProjectType())) {
									configItem =  loadForConfigRelByIssueTypeAndProject(configRel, issueTypeID, parentProjectID);
									if (configItem!=null) {
										return configItem;
									}
								}
								parentProjectID = parentProjectBean.getParent();
							} else {
								break;
							}
					}
					//get the projectType of the project and fall back to the issueType-projectType combination
					configItem = loadForConfigRelByIssueTypeAndProjectType(configRel, issueTypeID, projectBean.getProjectType());
					if (configItem!=null) {
						return configItem;
					}
				}
			}
		}
		//try the issueType-projectType combination
		if (issueTypeID!=null && projectTypeID!=null) {
			configItem = loadForConfigRelByIssueTypeAndProjectType(configRel, issueTypeID, projectTypeID);
			if (configItem!=null) {
				return configItem;
			}
		}
		//try the project only but fall back to the corresponding projectType if it is the case
		if (hasProjectOrProjectTypSpecificConfigs()) {
			if (projectID!=null) {
				configItem = loadForConfigRelByProject(configRel, projectID);
				if (configItem!=null) {
					return configItem;
				} else {
					TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
					if (projectBean!=null) {
						Integer parentProjectID = projectBean.getParent();
						//try an ancestor project
						while (parentProjectID!=null) {
								TProjectBean parentProjectBean = LookupContainer.getProjectBean(parentProjectID);
								if (parentProjectBean!=null) {
									if (EqualUtils.equal(projectBean.getProjectType(), parentProjectBean.getProjectType())) {
										configItem = loadForConfigRelByProject(configRel, parentProjectID);
										if (configItem!=null) {
											return configItem;
										}
									}
									parentProjectID = parentProjectBean.getParent();
								} else {
									break;
								}
						}
						//try the projectType of the project
						configItem = loadForConfigRelByProjectType(configRel, projectBean.getProjectType());
						if (configItem!=null) {
							return configItem;
						}
					}
				}
			}
			//try the projectType only	
			if (projectTypeID!=null) {
				configItem =  loadForConfigRelByProjectType(configRel, projectTypeID);
				if (configItem!=null) {
					return configItem;
				}
			}
		}
		//try the issueType	only	
		if (issueTypeID!=null) {
			configItem = loadForConfigRelByItemType(configRel, issueTypeID);
			if (configItem!=null) {
				return configItem;
			}
		}
		//try the default		
		return loadForConfigRelDefault(configRel);
	}
}
