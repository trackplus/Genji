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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.Preparable;

/**
 * Picker action for category paths
 * @author Tamas
 *
 */
public class CategoryPathPickerAction implements Preparable, SessionAware, ServletResponseAware {
	private TPersonBean personBean;
	private Locale locale;
	private String categoryType;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Path picker
	 * @return
	 */
	public String execute() {
		List<TreeNode> nodes = CategoryPathPickerBL.getPickerNodesEager(personBean, locale, categoryType);
		JSONUtility.encodeJSON(servletResponse, CategoryJSON.encodeCategoryPathPickerJSON(nodes));
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

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	
	
}
