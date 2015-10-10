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


package com.aurel.track.beans;
/**
 * This class represent a general schema for configItem
 * 
 *
 */
public interface ConfigItem {
  	/**
	 * @return the objectID
	 */
	public Integer getObjectID();

	/**
	 * @param objectID the configID to set
	 */
	public void setObjectID(Integer objectID);

	/**
	 * @return the configRel
	 */
	public Integer getConfigRel();

	/**
	 * @param configRel the configRel to set
	 */
	public void setConfigRel(Integer configRel);

	
	/**
	 * @return the issueType
	 */
	public Integer getIssueType();

	
	/**
	 * @param issueType the issueType to set
	 */
	public void setIssueType(Integer issueType);

	
	/**
	 * @return the project
	 */
	public Integer getProject();

	
	/**
	 * @param project the project to set
	 */
	public void setProject(Integer project);

	
	/**
	 * @return the projectType
	 */
	public Integer getProjectType();

	
	/**
	 * @param projectType the projectType to set
	 */
	public void setProjectType(Integer projectType);

	/**
	 * Whether the config is a default (global) one 
	 * @return
	 */
	public boolean isDefault();
}
