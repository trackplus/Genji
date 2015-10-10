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


package com.aurel.track.admin.customize.treeConfig;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * A action for a generic tree configuration based on global,
 * issueType, projectType and project specific scopes: 
 * Currently used by field configuration and screen assignment  
 * 
 */
public class TreeConfigAction extends ActionSupport implements Preparable, SessionAware,
		ServletResponseAware, ServletRequestAware  {
	private static final long serialVersionUID = 340L;
	private String node;
	private Map<String, Object> session;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	private boolean fromProjectConfig;
	private Integer projectOrProjectTypeID;
	
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}

	public String load() {
		String folderInfo = TreeConfigBL.getFolderInfo(node, locale);
		 JSONUtility.encodeJSON(ServletActionContext.getResponse(), 
				 folderInfo);
		return null;
	}
	
	/**
	 * Expand a tree node from tree
	 * @return
	 */
	public String expand() {
		List<TreeConfigNodeTO> nodes=TreeConfigBL.getChildren(node, projectOrProjectTypeID, fromProjectConfig, personBean, locale);
		JSONUtility.encodeJSON(servletResponse, 
				TreeConfigJSON.getChildrenJSON(nodes));
		return null;
	}
	
	/**
	 * Reset a tree node
	 * @return
	 */
	public String reset(){
		TreeConfigBL.reset(node, locale, servletResponse);
		return null;
	}
	
	/**
	 * Reset a tree node
	 * @return
	 */
	public String overwrite(){
		TreeConfigBL.overwrite(node, locale, servletResponse);
		return null;
	}
	
	/**
	 * Downloads the file
	 * @return
	 */
	public String downloadIcon() {
		TreeConfigBL.downloadIcon(node, servletRequest, servletResponse);
		return null;
	}
	
	/**
	 * @return the nodeId
	 */
	public String getNode() {
		return node;
	}
	
	
	/**
	 * @param node the nodeId to set
	 */
	public void setNode(String node) {
		this.node = node;
	}	
	
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setFromProjectConfig(boolean fromProjectConfig) {
		this.fromProjectConfig = fromProjectConfig;
	}

	public void setProjectOrProjectTypeID(Integer projectOrProjectTypeID) {
		this.projectOrProjectTypeID = projectOrProjectTypeID;
	}

	
}
