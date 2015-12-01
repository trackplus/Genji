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

package com.aurel.track.admin.customize.category.filter.parameters;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action for rendering the parameters for a parameterized issue filter
 * (Submitting the specified parameters and executing the filter is directed to issueNavigator action because it handles
 * centrally the loading and rendering of issue results to avoid storing the results in session for different actions)
 */
public class FilterParamertersAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware, ServletRequestAware {
	private static final long serialVersionUID = 340L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	private TPersonBean personBean;
	private Locale locale;
	//filterID should be set only if no instant modification was made on the saved query  
	private Integer filterID;
	//the encoded query URL
	private String query;
	
	@Override
	public void prepare() {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
		
	/**
	 * Whether the saved filter contains parameter or not 
	 * @return
	 */
	public String containsParameter() {
		JSONUtility.encodeJSON(servletResponse, JSONUtility.encodeJSONSuccess(
				FilterSelectsParametersUtil.savedFilterContainsParameter(filterID, personBean, locale)));
		return null;
	}
	
	/**
	 * Render the parameters page after executing a parameterized filter 
	 * (either first opened for edit possibly "instantly" changed before or directly)
	 * (After a project change in parameters change FilterUpperRefreshAction's 
	 * execute is called, just like by "normal" filter edit) 
	 * @return
	 */
	public String renderParameters() {
		JSONUtility.encodeJSON(servletResponse, RenderParametersUtil.renderParameters(filterID, query, servletRequest, session, personBean, locale));
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

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setFilterID(Integer filterID) {
		this.filterID = filterID;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	
}
