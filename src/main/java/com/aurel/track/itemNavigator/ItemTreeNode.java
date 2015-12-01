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

package com.aurel.track.itemNavigator;

import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.group.GroupLimitBean;
import com.aurel.track.util.TreeNode;

public class ItemTreeNode extends TreeNode {		
	private static final long serialVersionUID = 1L;
	private GroupLimitBean groupLimitBean;
	private ReportBean reportBean;	
	
	public ItemTreeNode(String id, String label) {
		super(id, label);
		this.leaf = Boolean.FALSE;
	}
	
	public GroupLimitBean getGroupLimitBean() {
		return groupLimitBean;
	}
	public void setGroupLimitBean(GroupLimitBean groupLimitBean) {
		this.groupLimitBean = groupLimitBean;
	}
	public ReportBean getReportBean() {
		return reportBean;
	}
	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}	
	
}
