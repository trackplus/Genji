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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.layout.group.GroupFieldTO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanExpandContext;
import com.aurel.track.util.PropertiesHelper;

/**
 */
public class FlatGridViewPlugin extends BaseIssueListViewPlugin {
	public static final int DEFAULT_PAGE_SIZE=25;
	@Override
	public ReportBeanExpandContext updateReportBeanExpandContext(TPersonBean personBean, List<ReportBean> reportBeanList,
			ReportBeanExpandContext reportBeanExpandContext, Map session, QueryContext queryContext, boolean forcePersonLayout) {
		ReportBeanExpandContext result=new ReportBeanExpandContext();
		if (reportBeanExpandContext!=null) {
			result.setSortFieldTO(reportBeanExpandContext.getSortFieldTO());
		}
		return result;
	}
	@Override
	public String getExtraJSON(Map<String, Object> session, List<ReportBean> reportBeanList, QueryContext queryContext, boolean forcePersonLayout) {
		Locale locale = (Locale) session.get(Constants.LOCALE_KEY);
		TPersonBean personBean = (TPersonBean) session.get(Constants.USER_KEY);
		int pageSize=DEFAULT_PAGE_SIZE;
		Integer prefferdPageSize=getPageSize(personBean);
		if(prefferdPageSize!=null){
			pageSize=prefferdPageSize.intValue();
		}
		boolean paginate=personBean.isPaginate();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, "paginate", paginate);
		JSONUtility.appendIntegerValue(sb, "pageSize", pageSize, true);

		sb.append("}");
		return sb.toString();


	}

	private Integer getPageSize(TPersonBean personBean){
		Integer pageSize=null ;
		String pageSizedStr = PropertiesHelper.getProperty(personBean.getMoreProperties(), TPersonBean.ITEM_NAVIGATOR_PAGE_SIZE);
		if(pageSizedStr!=null){
			try{
				pageSize=Integer.parseInt(pageSizedStr);
			}catch (Exception ex){}
		}
		return pageSize;
	}
	
	/**
	 * Gets the hardcoded groupings
	 * If no hardcoded grouping exist take it from layout
	 * Force it to empty list (not null!) to not to take from layout because flat view does not allow grouping
	 * @return
	 */
	@Override
	public List<GroupFieldTO> getGroupFieldBeans(Locale locale) {
		return new LinkedList<GroupFieldTO>();
	}
}

