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

package com.aurel.track.admin.project.release;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

/**
 * Gets the release tree
 * @author Tamas Ruff
 *
 */
public class ReleasePickerBL {
	
	/**
	 * Gets the release tree nodes
	 * @param projectIDList
	 * @param selectedReleaseIDsSet
	 * @param useChecked
	 * @param projectIsSelectable
	 * @param closedFlag
	 * @param inactiveFlag
	 * @param activeFlag
	 * @param notPlannedFlag
	 * @param withParameter
	 * @param exceptID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	public static String getTreeJSON(List<Integer> projectIDList,
			Set<Integer> selectedReleaseIDsSet,
			boolean useChecked, boolean projectIsSelectable,
			boolean closedFlag, boolean inactiveFlag, boolean activeFlag, boolean notPlannedFlag,
			boolean withParameter, Integer exceptID, TPersonBean personBean, Locale locale) {
		/**
		 * useSelecatble if both useChecked and projectIsSelectable is false: 
		 * useChecked false: only a single selection is possible
		 * but still the project nodes (which only build up the release hierarchy) are not selectable
		 * projectIsSelectable false: if projectIsSelectable is true all nodes are selectable
		 */
		boolean useSelectable =true;
		
		List<TreeNode> releaseNodes = ReleaseBL.getReleaseNodesEager(personBean,
				projectIDList, closedFlag, inactiveFlag, activeFlag, notPlannedFlag,
				selectedReleaseIDsSet, useChecked, projectIsSelectable, withParameter, exceptID, locale);
		String treeHierarchyJSON = null;
		if (useChecked) {
			//multiple selections are possible
			treeHierarchyJSON = JSONUtility.getTreeHierarchyJSON(releaseNodes, useChecked, false);
		} else {
			//useSelectable if project is not selectable
			treeHierarchyJSON = JSONUtility.getTreeHierarchyJSON(releaseNodes, false, useSelectable);
		}
		return treeHierarchyJSON;
	}
}
