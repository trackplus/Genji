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

package com.aurel.track.item.possibleOptions;

import java.util.HashMap;
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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;

/**
 * Returns the possible field values for a field list and a selected item list
 * @author Tamas
 *
 */
public class PossibleFieldOptionsAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer personID;
	private String selectedItemIDs;
	private String selectedFieldIDs;
	private List<Integer> itemIDs;
	private Set<Integer> fieldIDs;
	
	/**
	 * Prepare the data 
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale)session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
		if (selectedItemIDs!=null) {
			itemIDs = GeneralUtils.createIntegerListFromStringArr(selectedItemIDs.split(","));
		}
		if (selectedFieldIDs!=null) {
			fieldIDs = GeneralUtils.createIntegerSetFromStringSplit(selectedFieldIDs);
		}
	}
	
	/**
	 * Returns the possible field values for each field
	 */
	@Override
	public String execute() {
		Map<Integer, Set<Integer>> partialValidOptionsMap = new HashMap<Integer, Set<Integer>>();
		Map<Integer, List<Integer>> possibleOptionsMap = PossibleFieldOptionsBL.getPossibleOptionsMap(itemIDs, fieldIDs, personID, locale, partialValidOptionsMap); 
		JSONUtility.encodeJSON(servletResponse, PossibleFieldOptionsJSON.getPossibleOptionsJSON(possibleOptionsMap, partialValidOptionsMap));
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

	public void setSelectedItemIDs(String selectedItemIDs) {
		this.selectedItemIDs = selectedItemIDs;
	}

	public void setSelectedFieldIDs(String selectedFieldIDs) {
		this.selectedFieldIDs = selectedFieldIDs;
	}
	
}
