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


package com.aurel.track.admin.customize.category;
/**
 * The structure of node's id field for either a category (report- or filter- branch) 
 * or a report or a filter (leaf)
 * @author Tamas
 *
 */
public class CategoryTokens {
	//type "report" or "issueFilter" or "notifyFilter"
	private String categoryType;
	//repository
	private Integer repository;
	//either 
	//free category branch (filter or report category) or 
	//hardcoded category branch (projectIDs in project repository) or 
	//leaf (filter or report)
	private Integer type;
	//objectID: categoryID or filterID or reportID
	private Integer objectID;
	private static String LINK_CHAR = "_";
	
	public CategoryTokens() {
		super();
	}
		
	public CategoryTokens(String categoryType, 
			Integer repository, Integer type, Integer objectID) {
		super();
		this.categoryType = categoryType;
		this.repository = repository;
		this.type = type;
		this.objectID = objectID;
	}

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public Integer getRepository() {
		return repository;
	}

	public void setRepository(Integer repository) {
		this.repository = repository;
	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}	
		
	/**
	 * Encode a node
	 * @param categoryType
     * @param repository
	 * @return
	 */
	public static String encodeRootNode(String categoryType, Integer repository){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(categoryType);
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(repository);
		return stringBuilder.toString();
	}
	
	/**
	 * Encode a node
	 * @param categoryTokens
	 * @return
	 */
	public static String encodeNode(CategoryTokens categoryTokens){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(categoryTokens.getCategoryType());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(categoryTokens.getRepository());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(categoryTokens.getType());
		stringBuilder.append(LINK_CHAR);
		stringBuilder.append(categoryTokens.getObjectID());
		return stringBuilder.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	public static CategoryTokens decodeNode(String id){
		CategoryTokens categoryTokens = new CategoryTokens();
		if (id!=null) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {	
				categoryTokens.setCategoryType(tokens[0]);
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						categoryTokens.setRepository(Integer.valueOf(tokens[1]));
					}
					if (tokens.length>2) {
						if (tokens[2]!=null && !"".equals(tokens[2]) && !"null".equals(tokens[2])) {
							categoryTokens.setType(Integer.valueOf(tokens[2]));
						}
						if (tokens.length>3) {
							if (tokens[3]!=null && !"".equals(tokens[3]) && !"null".equals(tokens[3])) {
								categoryTokens.setObjectID(Integer.valueOf(tokens[3]));
							}
						}
					}
				}
			}
		}
		return categoryTokens;
	}
}
