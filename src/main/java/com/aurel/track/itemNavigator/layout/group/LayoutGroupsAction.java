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

package com.aurel.track.itemNavigator.layout.group;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportExpandAction;
import com.aurel.track.report.group.ReportGroupBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.BooleanStringBean;
import com.aurel.track.util.IntegerStringBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Render and save the grouping configuration
 */
public class LayoutGroupsAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	
	public static final long serialVersionUID = 400L;
	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private Integer filterID;
	private Integer filterType;
	private List<GroupFieldTO> groupFields = new ArrayList<GroupFieldTO>();
	
	public void prepare() throws Exception {
		personBean = (TPersonBean)session.get(Constants.USER_KEY);
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}

	/**
	 * Render the grouping configuration
	 */
	public String execute() {
		List<GroupFieldTO> groupFields = new LinkedList<GroupFieldTO>();
		LayoutGroupsBL.loadGroupFields(personBean, locale, filterType, filterID, groupFields);
		LayoutGroupsBL.completeGroupLevels(groupFields, ReportGroupBL.MAXIMUM_GROUPING_LEVEL);
		//the possible group fields
		List<IntegerStringBean> groupFieldList = LayoutGroupsBL.getGroupableFields(locale);
		List<BooleanStringBean> ascendingDescendingList = LayoutGroupsBL.getOrderList(locale);
		List<BooleanStringBean> expandCollapseList = LayoutGroupsBL.getExpandCollapseList(locale);
		JSONUtility.encodeJSON(servletResponse, LayoutGroupsJSON.encodeGrouping(groupFields, groupFieldList, ascendingDescendingList, expandCollapseList));
		return null;
	}
	
	/**
	 * Change the grouping fields 
	 * @return
	 */
	public String save() {
		Integer duplicateFieldID = LayoutGroupsBL.duplicatedGroupByColumn(groupFields);
		if (duplicateFieldID!=null) {
			TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(duplicateFieldID, locale);
			JSONUtility.encodeJSONFailure(ServletActionContext.getResponse(),
					LocalizeUtil.getParametrizedString("itemov.err.grouped", new Object[] {fieldConfigBean.getLabel()}, locale), null);
		} else {
			LayoutGroupsBL.saveGrouping(personBean, filterType, filterID, groupFields);
			session.remove(ReportExpandAction.OTHER_GROUPS_SET);
			JSONUtility.encodeJSONSuccess(servletResponse);
		}
		return null;
	}

	public void setSession(Map<String, Object> stringObjectMap) {
		this.session=stringObjectMap;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}

	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}

	public List<GroupFieldTO> getGroupFields() {
		return groupFields;
	}

	public void setGroupFields(List<GroupFieldTO> groupFields) {
		this.groupFields = groupFields;
	}
}
