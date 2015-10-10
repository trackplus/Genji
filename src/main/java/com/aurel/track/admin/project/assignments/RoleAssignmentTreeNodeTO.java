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

package com.aurel.track.admin.project.assignments;

import com.aurel.track.tree.TreeNodeBaseTO;


/**
 * Transfer object for a role assignment for project tree node
 * @author Tamas
 *
 */
public class RoleAssignmentTreeNodeTO extends TreeNodeBaseTO {	
	/**
	 * The rootID of the configuration specific tree (project specific branch): it should 
	 * be the same format as in the "entire" tree 
	 */
	private boolean group;
		
	
	public RoleAssignmentTreeNodeTO(String id, String text, String iconCls, boolean leaf,
			boolean group) {
		super(id, text, iconCls, leaf);		
		this.group = group;
	}

	public boolean isGroup() {
		return group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}
	
	
}
