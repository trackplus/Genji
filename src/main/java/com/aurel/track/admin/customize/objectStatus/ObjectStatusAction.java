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

package com.aurel.track.admin.customize.objectStatus;

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

public class ObjectStatusAction extends ActionSupport
	implements Preparable, SessionAware, ServletResponseAware  {
	
	private static final long serialVersionUID = 8112488072837466572L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;	
	private Locale locale;
	//tree node
	private String node;
	private String label;
	//add or edit
	private boolean add;
	private Integer typeflag;
	private Integer replacementID;	
	private String droppedToNode;
	private boolean before;
	
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
	}
	
	public String localizedLabels() {
		JSONUtility.encodeJSON(servletResponse, 
				ObjectStatusJSON.createLocalizedLabelsJSON(ObjectStatusBL.getLocalizedLabels(locale)));		
		return null;
	}
	
	/**
	 * Expand the tree
	 */
	public String expand() {
		JSONUtility.encodeJSON(servletResponse, 
				JSONUtility.getChildrenJSON(ObjectStatusBL.getChildren(locale)));
		return null;
	}
		
	/**
	 * Load the grid data
	 * @return
	 */
	public String loadList() {
		JSONUtility.encodeJSON(servletResponse, 
				ObjectStatusBL.getLoadList(node, personBean, locale));
		return null;
	}
	
	/**
	 * Load the detail for a node
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, 
				ObjectStatusBL.load(node, add, locale));
		return null;
	}
	
	/**
	 * Save a list entry or a custom list after edit/add/copy (copy only for custom lists)
	 * @return
	 */
	public String save() {
		boolean success = false;
		String errorMessage = ObjectStatusBL.isValidLabel(node, label, add, locale);
		if (errorMessage==null) {
			success = true;
			JSONUtility.encodeJSON(servletResponse, ObjectStatusBL.save(node, label, typeflag,
				add, personBean, locale));
		} else {
			JSONUtility.encodeJSON(servletResponse, 
				ObjectStatusJSON.createNodeResultJSON(success, node, errorMessage));
		}
		return null;
	}
	
	/**
	 * Delete a list entry or a custom list
	 * @return
	 */
	public String delete() {
		JSONUtility.encodeJSON(servletResponse,
				ObjectStatusBL.delete(node));
		return null;
	}
	
	/**
	 * Whether the replace is needed or the delete is done without replacement
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(servletResponse,
				ObjectStatusBL.renderReplace(node, null, locale));
		return null;
	}
	
	/**
	 * Replace and delete
	 * @return
	 */
	public String replaceAndDelete() {
		JSONUtility.encodeJSON(servletResponse,
				ObjectStatusBL.replaceAndDelete(replacementID, droppedToNode, locale));
		return null;
	}
	
	public String droppedNear() {
		Integer optionID = ObjectStatusBL.droppedNear(node, droppedToNode, before, locale);
		JSONUtility.encodeJSON(servletResponse, 
				ObjectStatusJSON.createSaveResultJSON(true, node, optionID));
		return null;
	}
	
	public String moveUp() {
		Integer optionID = ObjectStatusBL.moveUp(node, locale);
		JSONUtility.encodeJSON(servletResponse, 
				ObjectStatusJSON.createSaveResultJSON(true, node, optionID));
		return null;
	}
	
	public String moveDown() {
		Integer optionID = ObjectStatusBL.moveDown(node, locale);
		JSONUtility.encodeJSON(servletResponse, 
				ObjectStatusJSON.createSaveResultJSON(true, node, optionID));
		return null;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	
	public void setNode(String node) {
		this.node = node;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
	public void setTypeflag(Integer typeflag) {
		this.typeflag = typeflag;
	}

	
	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}	
	
	public void setDroppedToNode(String droppedToNode) {
		this.droppedToNode = droppedToNode;
	}

	public void setBefore(boolean before) {
		this.before = before;
	}
	
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
}
