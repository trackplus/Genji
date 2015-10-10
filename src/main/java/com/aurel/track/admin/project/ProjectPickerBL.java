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

package com.aurel.track.admin.project;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.util.TreeNode;

public class ProjectPickerBL {
	
	/**
	 * Gets the treeNodes for projects having arrRight rights for personBean
	 * @param selectedProjectIDsSet
	 * @param exclusiveProjectID
	 * @param useChecked
	 * @param useSelectable
	 * @param withParameter
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static List<TreeNode> getTreeNodesForRead(Set<Integer> selectedProjectIDsSet, 
			boolean useChecked, boolean withParameter, TPersonBean personBean, Locale locale) {
		int[] arrRight = new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN,
				AccessBeans.AccessFlagIndexes.READANYTASK};
		List<TreeNode> usedProjectNodes = null;
			usedProjectNodes = ProjectBL.getUsedProjectNodesEager(personBean, false, arrRight,
					selectedProjectIDsSet, useChecked, true, false, false, withParameter, locale,
					new HashMap<Integer, TreeNode>());
		return usedProjectNodes;
	}
	
	/**
	 * Gets the treeNodes for projects having arrRight rights for personBean
	 * @param selectedProjectIDsSet
	 * @param exclusiveProjectID
	 * @param useChecked
	 * @param useSelectable
	 * @param withParameter
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static List<TreeNode> getTreeNodesForCreateModify(Set<Integer> selectedProjectIDsSet, 
			boolean useChecked, TPersonBean personBean, Locale locale) {
		int[] arrRight = new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN,
				AccessBeans.AccessFlagIndexes.MODIFYANYTASK,
				AccessBeans.AccessFlagIndexes.CREATETASK};
		List<TreeNode> usedProjectNodes = null;
			usedProjectNodes = ProjectBL.getUsedProjectNodesEager(personBean, true, arrRight,
					selectedProjectIDsSet, useChecked, true, false, false, false, locale,
					new HashMap<Integer, TreeNode>());
		return usedProjectNodes;
	}
	
}
