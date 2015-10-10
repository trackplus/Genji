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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TProjectAccountBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;

/**
 * BL for account assignments for project
 * @author Tamas Ruff
 *
 */
public class AccountAssignmentsBL {
	private static String ACCOUNT_ICON_CLASS =  "account-ticon";
	
	/**
	 * Gets the assignment detail json for a node
	 * @param node
	 * @param projectID
	 * @param locale	
	 * @return
	 */
	static String getAssignmentJSON(Integer projectID, Locale locale) {
		String projectLabel = "";
		if (projectID!=null) {
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				projectLabel = projectBean.getLabel();
			}
		}
		Set<Integer> assignedAccountIDs = getAssignedAccountIDs(projectID);
		List<TAccountBean> labelBeans = AccountBL.getAllAccounts();
		List<TAccountBean> assigned = getAssigned(labelBeans, assignedAccountIDs, true);
		List<TAccountBean> unassigned = getAssigned(labelBeans, assignedAccountIDs, false);
		//String accountLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.account.lbl.account", locale);			
		String assignmentInfo = LocalizeUtil.getParametrizedString("admin.project.accountAssignment.lbl.infoGeneral",
				new Object[] {/*accountLabel, assignedFor,*/ projectLabel}, locale);
		return ProjectAssignmentJSON.encodeAccountAssignment(assigned, unassigned, assignmentInfo, ACCOUNT_ICON_CLASS);						
	}
	
	/**
	 * Load the accounts for project
	 * @param projectID
	 * @return
	 */
	public static List<TProjectAccountBean> loadAssignmnetsByProject(Integer projectID) {
		return DAOFactory.getFactory().getProjectAccountDAO().loadByProject1(projectID);
	}
	
	/**
	 * Saves an account to project assignmnet
	 * @param projectAccountBean
	 */
	public static void save(TProjectAccountBean projectAccountBean) {
		DAOFactory.getFactory().getProjectAccountDAO().save(projectAccountBean);
	}
	
	/**
	 * Gets the status IDs assigned to the projectType for an issueType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	public static Set<Integer> getAssignedAccountIDs(Integer projectID) {
		Set<Integer> assignedAccounts = new HashSet<Integer>();
		List<TProjectAccountBean> projectAccountBeans = loadAssignmnetsByProject(projectID);
		for (TProjectAccountBean projectAccountBean : projectAccountBeans) {
			Integer accountID = projectAccountBean.getAccount();
			assignedAccounts.add(accountID);
		}
		return assignedAccounts;
	}
	
	/**
	 * Gets the issueTypeBeans assigned to the projectType
	 * @param allLabelBeansList
	 * @param assignedIDs
	 * @param assigned
	 * @return
	 */
	public static List<TAccountBean> getAssigned(List<TAccountBean> allLabelBeansList,
			Set<Integer> assignedIDs, boolean assigned) {		
		List<TAccountBean> filteredBeans = new LinkedList<TAccountBean>();	
		for (TAccountBean labelBean : allLabelBeansList) {
			if ((assignedIDs.contains(labelBean.getObjectID()) && assigned) ||
					(!assignedIDs.contains(labelBean.getObjectID()) && !assigned)) {
				filteredBeans.add(labelBean);	
			}
		}
		return filteredBeans;
	}	
	
	/**
	 * Assign entries
	 * @param node
	 * @param assign
	 */
	static void assign(Integer projectID, List<Integer> accountIDs) {				
		if (projectID!=null && accountIDs!=null && !accountIDs.isEmpty()) {
			for (Integer accountID : accountIDs) {
				TProjectAccountBean projectAccountBean = new TProjectAccountBean();
				projectAccountBean.setProject(projectID);
				projectAccountBean.setAccount(accountID);
				save(projectAccountBean);
			}
		}
	}
	
	/**
	 * Unassign entries
	 * @param node
	 * @param unassign
	 */
	static void unassign(Integer projectID, List<Integer> accountIDs) {		
		if (projectID!=null && accountIDs!=null && !accountIDs.isEmpty()) {
			for (Integer accountID : accountIDs) {
				DAOFactory.getFactory().getProjectAccountDAO().deleteByProjectAndAccount(projectID, accountID);
			}
		}			
	}
}
