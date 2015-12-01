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

package com.aurel.track.admin.customize.category;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.Preparable;

/**
 * Picker action for category leafs
 * @author Tamas
 *
 */
public class CategoryPickerAction implements Preparable, SessionAware, ServletResponseAware {
	private TPersonBean personBean;
	private Locale locale;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private String node;
	private boolean useChecked;
	
	//fields used in picker (by automail assignments)
	//query picker by automail assignments: in the project configuration the project specific automail filters should be limited to this project 
	private Integer projectID;
	//whether to exclude the private nodes (by default automail assignments and in project configuration automail assignments) 
	private boolean excludePrivate;

	
	/**
	 * The already selected report(s)/filter(s): important only for trees (to set the checked field) not for pickers (they are selected by form.load())
	 */
	private Integer selectedID;
	private String selectedIDs;
	
	//if set do not include this in the tree (used by selecting a replacementID)
	private Integer exceptID;
	
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Category picker
	 * @return
	 */
	public String execute() {
		Set<Integer> selectedIDSet = null;
		if (selectedID!=null) {
			selectedIDSet = new HashSet<Integer>();
			selectedIDSet.add(selectedID);
		} else {
			if (selectedIDs!=null && !"".equals(selectedIDs)) {
				selectedIDSet = GeneralUtils.createIntegerSetFromStringSplit(selectedIDs);
			}
		}
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(node);
		String categoryType = categoryTokens.getCategoryType();
		List<TreeNode> nodes = CategoryPickerBL.getPickerNodesEager(
				personBean, excludePrivate, useChecked, selectedIDSet,
				false, projectID, exceptID, locale, categoryType);
		JSONUtility.encodeJSON(servletResponse, 
				JSONUtility.getTreeHierarchyJSON(nodes, useChecked, true));
		return null;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public void setUseChecked(boolean useChecked) {
		this.useChecked = useChecked;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public void setExcludePrivate(boolean excludePrivate) {
		this.excludePrivate = excludePrivate;
	}

	public void setExceptID(Integer exceptID) {
		this.exceptID = exceptID;
	}

	public void setSelectedID(Integer selectedID) {
		this.selectedID = selectedID;
	}

	public void setSelectedIDs(String selectedIDs) {
		this.selectedIDs = selectedIDs;
	}
	
	
}
