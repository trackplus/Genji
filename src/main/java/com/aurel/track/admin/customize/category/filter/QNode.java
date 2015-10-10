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

package com.aurel.track.admin.customize.category.filter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * The QNode element for storing the filter expressions
 * @author Tamas
 *
 */
public class QNode implements Serializable {
	private static final long serialVersionUID = 2863384913341292789L;
	public static final int OR=0;
	public static final int AND=1;
	public static final int EXP=2;
	//used only on UI but not in the stored query
	public static final int NOT_OR=3;
	public static final int NOT_AND=4;
	
	private Integer objectID;
	private Integer parentID;
	private List<QNode> children;
	private int type;
	private boolean negate;
	private boolean typeAlreadySet;

	public int getDisplayType() {
		if (type==AND) {
			if (negate) {
				return NOT_AND;
			} else {
				return AND;
			}
		} else {
			if (type==OR) {
				if (negate) {
					return NOT_OR;
				} else {
					return OR;
				}
			}
		}
		return EXP;
	}
	
	public void addChild(QNode node){
		if(children==null){
			children=new LinkedList<QNode>();
		}
		children.add(node);
		node.setParentID(this.getObjectID());
	}
	public List<QNode> getChildren() {
		return children;
	}

	public void setChildren(List<QNode> children) {
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isFolder() {
		return (children!=null && !children.isEmpty());
	}

	public boolean isNegate() {
		return negate;
	}

	public void setNegate(boolean negate) {
		this.negate = negate;
	}

	public boolean isTypeAlreadySet() {
		return typeAlreadySet;
	}

	public void setTypeAlreadySet(boolean typeAlreadySet) {
		this.typeAlreadySet = typeAlreadySet;
	}

}
