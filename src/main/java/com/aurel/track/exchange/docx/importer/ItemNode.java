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

package com.aurel.track.exchange.docx.importer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * The ItemNode element for storing the item hierarchy in a docx
 * @author Tamas
 *
 */
public class ItemNode implements Serializable {
	private static final long serialVersionUID = 2863384913341292789L;
	
	private String title;
	private String description;
	private Integer objectID;
	private Integer parentID;
	private List<ItemNode> children;
	private int headingLevel;
	
	
	public void addChild(ItemNode node){
		if(children==null){
			children=new ArrayList<ItemNode>();
		}
		children.add(node);
		node.setParentID(this.getObjectID());
	}
	
	public List<ItemNode> getChildren() {
		return children;
	}

	public void setChildren(List<ItemNode> children) {
		this.children = children;
	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	public boolean isFolder() {
		return (children!=null && !children.isEmpty());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getHeadingLevel() {
		return headingLevel;
	}

	public void setHeadingLevel(int headingLevel) {
		this.headingLevel = headingLevel;
	}

	
}


