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

package com.aurel.track.admin.customize.treeConfig.mailTemplate;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 */
public class MailTemplateAssignmentAction extends ActionSupport
		implements Preparable, ServletResponseAware, SessionAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	//the ID of the assigned screen
	private Integer assignedID;
	private String node;



	public void prepare() {
	}

	/**
	 * Saves the screen configuration
	 * @return
	 */
	public String save(){
		MailTemplateConfigEditBL.save(node, assignedID, servletResponse);
		return null;
	}

	public void setAssignedID(Integer assignedID) {
		this.assignedID = assignedID;
	}
	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
