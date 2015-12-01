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

package com.aurel.track.tree;


/**
 * Base class for tree node transfer objects
 * @author Tamas
 *
 */
public class TreeNodeBaseTO {
	//The id of the node
	protected String id = null;
	//the text of the node
	protected String text = null;
	//the icon file
	protected String icon = null;
	//the icon class specified in a css file  
	protected String iconCls = null;
	//whether this node is a leaf
	protected boolean leaf = false;
	
	public TreeNodeBaseTO() {
		super();
	}
	
	public TreeNodeBaseTO(String id, String text, boolean leaf) {
		super();
		this.id = id;
		this.text = text;
		this.leaf = leaf;
	}
		
	public TreeNodeBaseTO(String id, String text, String iconCls, boolean leaf) {
		super();
		this.id = id;
		this.text = text;
		this.iconCls = iconCls;
		this.leaf = leaf;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
}
