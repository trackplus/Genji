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

package com.aurel.track.itemNavigator.layout.column;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *
 */
public class LayoutColumnsAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	public static final long serialVersionUID = 400L;
	
	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private Integer filterID;
	private Integer filterType;
	private boolean includeLongFields;
	private Integer width;
	private Integer fieldID;
	private Integer fromIdx;
	private Integer toIdx;
	private Map<String,Boolean> selectedColumnsMap;

	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * Loads all columns to use in layout
	 * @return
	 */
	@Override
	public String execute() {
		List<ChooseColumnTO> chooseColumns = LayoutColumnsBL.getAllColumns(personBean, locale, filterType, filterID, includeLongFields);
		JSONUtility.encodeJSON(servletResponse, LayoutColumnsJSON.encodeAllColumns(chooseColumns));
		return null;
	}

	/**
	 * Saves the selected columns in layout
	 * @return
	 */
	public String save() {
		LayoutColumnsBL.saveLayout(personBean, selectedColumnsMap, locale, filterType, filterID);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	
	/**
	 * Resize a column width
	 * @return
	 */
	public String resizeColumn(){
		LayoutColumnsBL.resizeColumn(personBean, filterType, filterID, fieldID, width);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	
	/**
	 * Move a column from fromIdx to toIdx
	 * @return
	 */
	public String moveColumn(){
		String errorKey = LayoutColumnsBL.moveColumn(personBean, filterType, filterID, fromIdx, toIdx);
		if (errorKey!=null){
			JSONUtility.encodeJSONFailure(servletResponse, errorKey);
			return null;
		}
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}

	@Override
	public void setSession(Map<String, Object> stringObjectMap) {
		this.session=stringObjectMap;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setSelectedColumnsMap(Map<String, Boolean> selectedColumnsMap) {
		this.selectedColumnsMap = selectedColumnsMap;
	}

	public Map<String, Boolean> getSelectedColumnsMap() {
		return selectedColumnsMap;
	}

	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}

	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setFromIdx(Integer fromIdx) {
		this.fromIdx = fromIdx;
	}

	public void setToIdx(Integer toIdx) {
		this.toIdx = toIdx;
	}

	public void setIncludeLongFields(boolean includeLongFields) {
		this.includeLongFields = includeLongFields;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}
	
	
}
