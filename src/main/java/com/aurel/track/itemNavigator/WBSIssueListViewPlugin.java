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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.itemNavigator.layout.group.SortFieldTO;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.report.execute.ReportExpandAction;

/**
 */
public class WBSIssueListViewPlugin extends BaseIssueListViewPlugin {
	@Override
	public ReportBeanExpandContext updateReportBeanExpandContext(TPersonBean personBean, List<ReportBean> reportBeanList,
			ReportBeanExpandContext reportBeanExpandContext, Map session, QueryContext queryContext, boolean forcePersonLayout) {
		Boolean allItemsExpanded = Boolean.TRUE;
		Set<Integer> otherItemsSet = null;
		Set<String> groupOtherExpandCollapse = null;
		if (reportBeanExpandContext!=null) {
			allItemsExpanded = reportBeanExpandContext.getAllItemsExpanded();
			otherItemsSet = reportBeanExpandContext.getOtherItemsSet();
		}
		groupOtherExpandCollapse=(Set)session.get(ReportExpandAction.OTHER_GROUPS_SET+".wbs");

		ReportBeanExpandContext result=new ReportBeanExpandContext();
		result.setAllItemsExpanded(allItemsExpanded);
		result.setOtherItemsSet(otherItemsSet); 
		result.setGroupBy(getGroupFieldBeans(personBean.getLocale()));
		result.setOtherGroupsSet(groupOtherExpandCollapse);
		SortFieldTO sortFieldTO = new SortFieldTO(SystemFields.WBS);
		result.setSortFieldTO(sortFieldTO);
		return result;
	}

	/**
	 * Gets the hardcoded groupings
	 * If no hardcoded grouping exist take it from layout
	 * @return
	 */
	public List<GroupFieldTO> getGroupFieldBeans(Locale locale) {
		List<GroupFieldTO> groupByList=new ArrayList<GroupFieldTO>();
		GroupFieldTO groupFieldTO = new GroupFieldTO(SystemFields.PROJECT, false, false);
		TFieldBean fieldBean = FieldBL.loadByPrimaryKey(SystemFields.PROJECT);
		if (fieldBean!=null) {
			groupFieldTO.setName(fieldBean.getName());
		}
		TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(SystemFields.PROJECT, locale);
		if (fieldConfigBean!=null) {
			groupFieldTO.setLabel(fieldConfigBean.getLabel());
		}
		groupByList.add(groupFieldTO);
		return groupByList;
	}
}
