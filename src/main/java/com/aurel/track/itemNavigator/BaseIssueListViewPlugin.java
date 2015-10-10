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

package com.aurel.track.itemNavigator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.plugin.IssueListViewDescriptor;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;

/**
 *
 */
public class BaseIssueListViewPlugin implements IssueListViewPlugin{
	public BaseIssueListViewPlugin(){
	}
	public String encodeJSON(IssueListViewDescriptor descriptor,Object data){
		return "";
	}
	public ReportBeanExpandContext updateReportBeanExpandContext(TPersonBean personBean, List<ReportBean> reportBeanList,
			ReportBeanExpandContext reportBeanExpandContext, Map session, QueryContext queryContext, boolean forcePersonLayout){
		return reportBeanExpandContext;
	}
	public String getExtraJSON(Map<String, Object> session, List<ReportBean> reportBeanList, QueryContext queryContext, boolean forcePersonLayout) {
		return null;
	}
	public Set<Integer> getExclusiveShortFields(TPersonBean personBean, QueryContext queryContext){
		return null;
	}
	public boolean includePercentDone(){
		return false;
	}
	/**
	 * Gets the hardcoded groupings
	 * If no hardcoded grouping exist take it from layout
	 * @return
	 */
	@Override
	public List<GroupFieldTO> getGroupFieldBeans(Locale locale) {
		return null;
	}
}
