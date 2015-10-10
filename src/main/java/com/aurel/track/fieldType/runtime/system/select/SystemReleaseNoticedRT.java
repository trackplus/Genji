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


package com.aurel.track.fieldType.runtime.system.select;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.report.execute.SimpleTreeNode;
import com.aurel.track.util.HierarchicalBeanUtils;
import com.aurel.track.util.TreeNode;

public class SystemReleaseNoticedRT extends SystemReleaseBaseRT { 
	
	/**
	 * Gets the complete hierarchy of the involved values by completing the eventual holes between
	 * @param involvedValues
	 * @return
	 */
	public List<SimpleTreeNode> getSimpleTreeNodesWithCompletePath(List<Integer> involvedValues) {
		return HierarchicalBeanUtils.getSimpleProjectTreeWithCompletedPath(
				involvedValues, SystemFields.INTEGER_RELEASENOTICED, new HashMap<Integer, SimpleTreeNode>());
	}
	
	/**
	 * Gets the treeNode datasource for a project
	 * @param projectID
	 * @param selectedReleasesID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	protected List<TreeNode> getProjectDataSource(Integer projectID, Set<Integer> selectedReleasesID, TPersonBean personBean, Locale locale) {
		List<Integer> projectIDList = new LinkedList<Integer>();
		projectIDList.add(projectID);
		return ReleaseBL.getReleaseNodesEager(personBean,
				projectIDList, false, true, true, false,
				selectedReleasesID, false, false, false, null, locale);
	}
	
	/**
	 * Gets the treeNode datasource for a project
	 * @param projectID
	 * @param personBean
	 * @param locale
	 * @return
	 */
	protected List<TreeNode> getGlobalDataSource(TPersonBean personBean, Locale locale) {
		return ReleaseBL.getReleaseNodesEager(personBean,
				null, false, true, true, false,
				null, false, false, false, null, locale);
	}
}
