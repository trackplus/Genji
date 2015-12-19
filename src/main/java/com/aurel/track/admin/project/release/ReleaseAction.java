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


package com.aurel.track.admin.project.release;

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

public class ReleaseAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private String node;
	//whether to add or edit
	private boolean add;
	//when add, whether to add a new main project or a subproject to the selected project
	private boolean addAsChild;
	private Integer replacementID;

	//same bean for release and phase
	private ReleaseDetailTO releaseDetailTO;
	private boolean confirmSave;
	//for copy/cut paste
	private String nodeFrom;
	private String nodeTo;

	/**
	 * Whether to show the closed releases. Can be null, this means that
	 * according to the showClosedReleases flag stored in the project
	 */
	private Boolean showClosedReleases;
	private String droppedToNode;
	private boolean before;


	@Override
	public void prepare() throws Exception {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}

	/**
	 * Expand the project specific nodes
	 * @return
	 */
	public String expand() {
		JSONUtility.encodeJSON(servletResponse,
				ReleaseJSON.getChildrenJSON(ReleaseConfigBL.getReleaseNodes(node, showClosedReleases)));
		return null;
	}

	/**
	 * Get the release rows for grid
	 * @return
	 */
	public String loadList() {
		JSONUtility.encodeJSON(servletResponse, ReleaseJSON.getGridRowJSON(
				ReleaseConfigBL.getReleaseRows(node, showClosedReleases, locale)));
		return null;
	}

	/**
	 * Load the project details
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse,
				ReleaseConfigBL.getReleaseDetail(node, add, addAsChild, locale));
		return null;
	}
	/**
	 * Saves the project details
	 * @return
	 */
	public String save() {
		JSONUtility.encodeJSON(servletResponse,
				ReleaseConfigBL.saveReleaseDetail(node, add, addAsChild, releaseDetailTO, confirmSave, replacementID, personBean, locale));
		return null;
	}

	/**
	 * Not a real copy but move after drag and drop
	 * @return
	 */
	public String copy() {
		JSONUtility.encodeJSON(servletResponse, ReleaseConfigBL.copy(nodeFrom, nodeTo));
		return null;
	}

	/**
	 * Clears the parent department
	 * @return
	 */
	public String clearParent() {
		JSONUtility.encodeJSON(servletResponse, ReleaseConfigBL.clearParent(node));
		return null;
	}

	/**
	 * Delete a list entry or a custom list
	 * @return
	 */
	public String delete() {
		JSONUtility.encodeJSON(servletResponse,
				ReleaseConfigBL.delete(node, personBean, locale));
		return null;
	}

	/**
	 * Whether the replace is needed or the delete is done without replacement
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(servletResponse,
				ReleaseConfigBL.renderReplace(node, null, personBean, locale));
		return null;
	}

	/**
	 * Replace and delete
	 * @return
	 */
	public String replaceAndDelete() {
		JSONUtility.encodeJSON(servletResponse,
				ReleaseConfigBL.replaceAndDelete(replacementID, node, personBean, locale));
		return null;
	}

	/**
	 * Change the release sort order by drag and drop
	 * @return
	 */
	public String droppedNear() {
		Integer optionID = ReleaseConfigBL.droppedNear(node, droppedToNode, before);
		JSONUtility.encodeJSON(servletResponse,
				ReleaseJSON.createSortOrderResultJSON(true, node, optionID));
		return null;
	}

	/**
	 * Move up a release
	 * @return
	 */
	public String moveUp() {
		Integer optionID = ReleaseConfigBL.moveUp(node);
		JSONUtility.encodeJSON(servletResponse,
				ReleaseJSON.createSortOrderResultJSON(true, node, optionID));
		return null;
	}

	/**
	 * Move down a release
	 * @return
	 */
	public String moveDown() {
		Integer optionID = ReleaseConfigBL.moveDown(node);
		JSONUtility.encodeJSON(servletResponse,
				ReleaseJSON.createSortOrderResultJSON(true, node, optionID));
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

	public void setNode(String node) {
		this.node = node;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setAddAsChild(boolean addAsChild) {
		this.addAsChild = addAsChild;
	}

	public ReleaseDetailTO getReleaseDetailTO() {
		return releaseDetailTO;
	}

	public void setReleaseDetailTO(ReleaseDetailTO releaseDetailTO) {
		this.releaseDetailTO = releaseDetailTO;
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

	public void setShowClosedReleases(Boolean showClosedReleases) {
		this.showClosedReleases = showClosedReleases;
	}

	public void setConfirmSave(boolean confirmSave) {
		this.confirmSave = confirmSave;
	}

	public void setNodeFrom(String nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public void setNodeTo(String nodeTo) {
		this.nodeTo = nodeTo;
	}

	private boolean fromAjax;

	public void setFromAjax(boolean _fa) {
		this.fromAjax = _fa;
	}

}
