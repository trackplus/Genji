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

/**
 * The structure of a list option node's id field
 * @author Tamas
 *
 */
public class TreeConfigIDTokens {
	private String configType;//screen or field
	private String type;//fieldType//issue type/project type/project/configItem
	private Integer projectID;
	private Integer projectTypeID;
	private Integer issueTypeID;
	private Integer configRelID;//fieldID or actionID, not
	private static String LINK_CHAR = "_";	
			
	public TreeConfigIDTokens(String configType, String type, 
			ConfigItem configItem) {
		super();
		this.configType = configType;
		this.type = type;
		if (configItem!=null) {
			this.issueTypeID = configItem.getIssueType();
			this.projectTypeID = configItem.getProjectType();
			this.projectID = configItem.getProject();
			this.configRelID = configItem.getConfigRel();
		}
	}
		
	public TreeConfigIDTokens() {
		super();
	}

	public String getConfigType() {
		return configType;
	}
	public void setConfigType(String configType) {
		this.configType = configType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public Integer getProjectTypeID() {
		return projectTypeID;
	}
	public void setProjectTypeID(Integer projectTypeID) {
		this.projectTypeID = projectTypeID;
	}
	public Integer getIssueTypeID() {
		return issueTypeID;
	}
	public void setIssueTypeID(Integer issueTypeID) {
		this.issueTypeID = issueTypeID;
	}
	public Integer getConfigRelID() {
		return configRelID;
	}
	public void setConfigRelID(Integer configRelID) {
		this.configRelID = configRelID;
	}	
	
	/**
	 * Encode a node
	 * @param treeConfigIDTokens
	 * @return
	 */
	public static String encodeNode(TreeConfigIDTokens treeConfigIDTokens){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(treeConfigIDTokens.getConfigType());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(treeConfigIDTokens.getType());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(treeConfigIDTokens.getProjectID());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(treeConfigIDTokens.getProjectTypeID());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(treeConfigIDTokens.getIssueTypeID());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(treeConfigIDTokens.getConfigRelID());
		return stringBuilder.toString();
	}
	
	/**
	 * Encode a node
	 * @param configType
     * @param type
	 * @return
	 */
	public static String encodeRootNode(String configType, String type){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(configType);
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(type);
		return stringBuilder.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	public static TreeConfigIDTokens decodeNode(String id){
		TreeConfigIDTokens treeConfigIDTokens = new TreeConfigIDTokens();
		String[] tokens = id.split(LINK_CHAR);
		if (tokens!=null && tokens.length>0) {	
			treeConfigIDTokens.setConfigType(tokens[0]);
			if (tokens.length>1) {
				if (tokens[1]!=null && !"".equals(tokens[1])) {
					treeConfigIDTokens.setType(tokens[1]);
				}
				if (tokens.length>2) {
					if (tokens[2]!=null && !"".equals(tokens[2]) && !"null".equals(tokens[2])) {
						treeConfigIDTokens.setProjectID(Integer.valueOf(tokens[2]));
					}
					if (tokens.length>3) {
						if (tokens[3]!=null && !"".equals(tokens[3]) && !"null".equals(tokens[3])) {
							treeConfigIDTokens.setProjectTypeID(Integer.valueOf(tokens[3]));
						}
						if (tokens.length>4) {
							if (tokens[4]!=null && !"".equals(tokens[4])&& !"null".equals(tokens[4])) {
								treeConfigIDTokens.setIssueTypeID(Integer.valueOf(tokens[4]));
							}
							if (tokens.length>5) {
								if (tokens[5]!=null && !"".equals(tokens[5]) && !"null".equals(tokens[5])) {
									treeConfigIDTokens.setConfigRelID(Integer.valueOf(tokens[5]));
								}
							}
						}
					}
				}
			}
		}
		return treeConfigIDTokens;
	}
}
