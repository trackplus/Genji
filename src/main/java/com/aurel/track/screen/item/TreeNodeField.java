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

package com.aurel.track.screen.item;


/**
 * A model used to render a field
 * @author Adrian Bojani
 *
 */
public class TreeNodeField implements Comparable<TreeNodeField>{
	private Integer objectID;
	private String name;
	private String label;
	private String description;
	private String img;
	public TreeNodeField(){
	}

	@Override
	public int compareTo(TreeNodeField treeNodeField) {
		if (treeNodeField==null) {
			return 1;
		}
		if (this.label==null && treeNodeField.label==null) {
			return 0;
		}
		if (this.label==null) {
			return -1;
		}
		if (treeNodeField.label==null) {
			return 1;
		}
		return this.label.compareTo(treeNodeField.label);
	}
	
	@Override
	public boolean equals(Object treeNodeField) {
		if (treeNodeField==null) {
			return false;
		}
		if (this.label==null && ((TreeNodeField)treeNodeField).label==null) {
			return true;
		}
		if (this.label == null) {
			return false;
		}
		return this.label.equals(((TreeNodeField)treeNodeField).label);
	}

	@Override
	public int hashCode() {
		return objectID;
	}
	
	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}
	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the objectID
	 */
	public Integer getObjectID() {
		return objectID;
	}
	/**
	 * @param objectID the objectID to set
	 */
	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
