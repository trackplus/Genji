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

package com.aurel.track.admin.customize.lists;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.projectType.assignments.ProjectTypeAssignmentJSON;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TChildIssueTypeBean;
import com.aurel.track.dao.ChildIssueTypeDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.resources.LocalizeUtil;

public class ChildIssueTypeAssignmentsBL {
	private static ChildIssueTypeDAO childIssueTypeDAO = DAOFactory.getFactory().getChildIssueTypeDAO();
	
	/**
	 * Load the TChildIssueTypeBean for childIssueTypes (changed rows)
	 * @param childIssueTypes
	 * @return
	 */
	public static List<TChildIssueTypeBean> loadByChildAssignments(List<Integer> childIssueTypes) {
		return childIssueTypeDAO.loadByChildAssignments(childIssueTypes);
	}
	
	/**
	 * Gets the issue type IDs assigned to the projectType
	 * @param parentIssueTypeID
	 * @return
	 */
	public static List<TChildIssueTypeBean> loadByChildAssignmentsByParent(Integer parentIssueTypeID) {
		return childIssueTypeDAO.loadByChildAssignmentsByParent(parentIssueTypeID);
	}
	
	/**
	 * Loads all TChildIssueTypeBean 
	 * @return
	 */
	public static List<TChildIssueTypeBean> loadAll() {
		return childIssueTypeDAO.loadAll();
	}
	
	/**
	 * Saves an child issue type assignment
	 * @param parentIssueTypeID
	 * @param childIssueTypeID
	 * @return
	 */
	public static Integer save(Integer parentIssueTypeID, Integer childIssueTypeID) {
		TChildIssueTypeBean childIssueTypeBean = new TChildIssueTypeBean();
		childIssueTypeBean.setIssueTypeParent(parentIssueTypeID);
		childIssueTypeBean.setIssueTypeChild(childIssueTypeID);
		return childIssueTypeDAO.save(childIssueTypeBean);
	}
	
	/**
	 * Encode the issue type assignments for a parent issueType
	 * @param parentIssueTypeNodeID
	 * @param locale
	 * @return
	 */
	static String getIssueTypeAssignmentJSON(String parentIssueTypeNodeID, Locale locale) {
		ListOptionIDTokens listOptionIDTokens = ListBL.decodeNode(parentIssueTypeNodeID);
		Integer parentIssueTypeID = listOptionIDTokens.getOptionID();
		String paerentIssueTypeLabel = IssueTypeBL.getLocalizedLabel(parentIssueTypeID, locale);
		Set<Integer> assignedIssueTypeIDs = getAssignedIssueTypeIDs(parentIssueTypeID);
		List<ILabelBean> labelBeans = IssueTypeBL.loadAll(locale);
		List<ILabelBean> assigned = getAssigned(labelBeans, assignedIssueTypeIDs, true);
		List<ILabelBean> unassigned = getAssigned(labelBeans, assignedIssueTypeIDs, false);
		String assignmentInfo = LocalizeUtil.getParametrizedString("admin.customize.list.lbl.childIssueTypeAssign",
				new Object[] {paerentIssueTypeLabel}, locale);
		return ProjectTypeAssignmentJSON.encodeSimpleAssignmentWithDynamicIcon(assigned, unassigned,
				assignmentInfo, parentIssueTypeID, ProjectTypesBL.PROJECT_TYPE_ASSIGNMENTS.ISSUE_TYPE_ASSIGNMENT);
	}
	
	/**
	 * Gets the issue type IDs assigned to the projectType
	 * @param parentIssueTypeID
	 * @return
	 */
	public static Set<Integer> getAssignedIssueTypeIDs(Integer parentIssueTypeID) {
		Set<Integer> assignedChildren = new HashSet<Integer>();
		List<TChildIssueTypeBean> childIssueTypeBeans = loadByChildAssignmentsByParent(parentIssueTypeID);
		for (TChildIssueTypeBean childIssueTypeBean : childIssueTypeBeans) {
			Integer childIssueType = childIssueTypeBean.getIssueTypeChild();
			assignedChildren.add(childIssueType);
		}
		return assignedChildren;
	}
	
	
	/**
	 * Gets the issueTypeBeans assigned to the projectType
	 * @param allLabelBeansList
	 * @param assignedIDs
	 * @param assigned
	 * @return
	 */
	private static List<ILabelBean> getAssigned(List<ILabelBean> allLabelBeansList,
			Set<Integer> assignedIDs, boolean assigned) {
		List<ILabelBean> filteredBeans = new LinkedList<ILabelBean>();
		for (ILabelBean labelBean : allLabelBeansList) {
			if ((assignedIDs.contains(labelBean.getObjectID()) && assigned) ||
					(!assignedIDs.contains(labelBean.getObjectID()) && !assigned)) {
				filteredBeans.add(labelBean);
			}
		}
		return filteredBeans;
	}	
	
	/**
	 * Assign issueTypes
	 * @param parentIssueTypeNodeID
	 * @param childIssueTypeIDs
	 */
	static void assign(String parentIssueTypeNodeID, List<Integer> childIssueTypeIDs) {
		ListOptionIDTokens listOptionIDTokens = ListBL.decodeNode(parentIssueTypeNodeID);
		Integer parentIssueTypeID = listOptionIDTokens.getOptionID();
		if (parentIssueTypeID!=null && childIssueTypeIDs!=null && !childIssueTypeIDs.isEmpty())
			for (Integer childIssueTypeID : childIssueTypeIDs) {
				save(parentIssueTypeID, childIssueTypeID);
			}
	}
	
	/**
	 * Unassign issueTypes
	 * @param parentIssueTypeNodeID
	 * @param childIssueTypeIDs
	 */
	static void unassign(String parentIssueTypeNodeID, List<Integer> childIssueTypeIDs) {
		ListOptionIDTokens listOptionIDTokens = ListBL.decodeNode(parentIssueTypeNodeID);
		Integer parentIssueTypeID = listOptionIDTokens.getOptionID();
		childIssueTypeDAO.delete(parentIssueTypeID, childIssueTypeIDs);
	}
}
