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

package com.aurel.track.admin.customize.lists;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.beans.TListBean;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.lists.exportList.ExportListBL;
import com.aurel.track.admin.customize.lists.importList.ImportListBL;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.configExchange.exporter.EntityExporterException;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.DownloadUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ListOptionsAction extends ActionSupport implements Preparable, 
	SessionAware, ServletResponseAware {

	
	private static final long serialVersionUID = 8112488072837466572L;
	private static final Logger LOGGER = LogManager.getLogger(ListOptionsAction.class);
	
	private Map<String, Object> session;
	private ApplicationBean applicationBean;
	private HttpServletResponse servletResponse;
	private TPersonBean personBean;
	private Locale locale;
	//tree node
	private String node;
	//field for moving a list (between projects or from a project to global or inversely) by drag and drop
	private String nodeFrom;
	private String nodeTo;
	
	private String label;
	private CssStyleBean cssStyleBean;
	
	//add or edit
	private boolean add;
	//whether it is a copy operation
	private boolean copy;
	
	//system list option specific fields
	private Integer typeflag;
	private Integer percentComplete;
	private boolean defaultOption;	
	private Integer replacementID;
	
	//custom list specific fields
	private String description;	

	private String droppedToNode;
	private boolean before;
	private boolean fromProjectConfig;
	
	//import list from file
	private File uploadFile;
	boolean overwriteExisting;
	boolean clearChildren;
	
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale = (Locale) session.get(Constants.LOCALE_KEY);
		applicationBean = ApplicationBean.getInstance();
	}
	
	public String localizedLabels() {
		JSONUtility.encodeJSON(servletResponse, 
				ListOptionsJSON.createLocalizedLabelsJSON(ListBL.getLocalizedLabels(locale)));
		return null;
	}
	
	
	/**
	 * Add/remove a licensed feature
	 * @return
	 */
	public String changeDefault() {
		if (node!=null) {
			ListOptionIDTokens listOptionIDTokens = ListBL.decodeNode(node);
			Integer optionID = listOptionIDTokens.getOptionID();
			if (optionID!=null) {
				TOptionBean optionBean = OptionBL.loadByPrimaryKey(optionID);
				if (optionBean!=null) {
					optionBean.setDefault(defaultOption);
					OptionBL.save(optionBean);
				}
			}
		}
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	
	/**
	 * Expand the tree
	 */
	public String expand() {
		JSONUtility.encodeJSON(servletResponse, 
				ListOptionsJSON.createChildrenJSON(ListBL.getChildren(
						node, fromProjectConfig, personBean, locale, applicationBean)));
		return null;
	}
		
	/**
	 * Load the grid data
	 * @return
	 */
	public String loadList() {
		JSONUtility.encodeJSON(servletResponse, 
				ListBL.getLoadList(node, personBean, locale, applicationBean));
		return null;
	}
	
	/**
	 * Load the detail for a node
	 * @return
	 */
	public String edit() {
		JSONUtility.encodeJSON(servletResponse, 
				ListBL.load(node, add, copy, personBean, locale));
		return null;
	}
	
	/**
	 * Save a list entry or a custom list after edit/add/copy (copy only for custom lists)
	 * @return
	 */
	public String save() {
		boolean success = false;
		String errorMessage = ListBL.isValidLabel(node, label, add, copy, locale);
		if (errorMessage==null) {
			success = true;
			JSONUtility.encodeJSON(servletResponse, ListBL.save(node, label, cssStyleBean, typeflag,
				percentComplete, defaultOption, add, copy, description, personBean, locale));
		} else {
			JSONUtility.encodeJSON(servletResponse, 
				ListOptionsJSON.createNodeResultJSON(success, node, errorMessage));
		}
		return null;
	}
	
	/**
	 * Not a real copy but move after drag and drop
	 * @return
	 */
	public String copy() {
		JSONUtility.encodeJSON(servletResponse, ListBL.copy(nodeFrom, nodeTo, personBean.getObjectID(), locale));
		return null;
	}
	
	/**
	 * Delete a list entry or a custom list
	 * @return
	 */
	public String delete() {
		JSONUtility.encodeJSON(servletResponse,
				ListBL.delete(node, personBean, locale));
		return null;
	}
	
	/**
	 * Whether the replace is needed or the delete is done without replacement
	 * @return
	 */
	public String renderReplace() {
		JSONUtility.encodeJSON(servletResponse,
				ListBL.renderReplace(node, null, locale));
		return null;
	}
	
	/**
	 * Replace and delete
	 * @return
	 */
	public String replaceAndDelete() {
		JSONUtility.encodeJSON(servletResponse,
				ListBL.replaceAndDelete(replacementID, node, locale));
		return null;
	}	
	
	public String droppedNear() {
		Integer optionID = ListBL.droppedNear(node, droppedToNode, before, locale, applicationBean);
		JSONUtility.encodeJSON(servletResponse, 
				ListOptionsJSON.createSaveResultJSON(true, node, optionID));
		return null;
	}
	
	public String moveUp() {
		Integer optionID = ListBL.moveUp(node, locale, applicationBean);
		JSONUtility.encodeJSON(servletResponse, 
				ListOptionsJSON.createSaveResultJSON(true, node, optionID));
		return null;
	}
	
	public String moveDown() {
		Integer optionID = ListBL.moveDown(node, locale, applicationBean);
		JSONUtility.encodeJSON(servletResponse, 
				ListOptionsJSON.createSaveResultJSON(true, node, optionID));
		return null;
	}
	
	public String exportList() {
		TListBean tListBean=ExportListBL.getListBean(node);
		String fileName="LIST-"+tListBean.getName()+".xml";
		DownloadUtil.prepareResponse(ServletActionContext.getRequest(),
				servletResponse, fileName, "text/xml");
		OutputStream outputStream = null;
		try {
			outputStream = servletResponse.getOutputStream();
			String dom = ExportListBL.exportList(tListBean);
			outputStream.write(dom.getBytes());
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (EntityExporterException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public String importList() {
		ImportListBL.importList(uploadFile, node, overwriteExisting, clearChildren);
		JSONUtility.encodeJSONSuccess(servletResponse, false);
		return null;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	
	public void setNode(String node) {
		this.node = node;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTypeflag(Integer typeflag) {
		this.typeflag = typeflag;
	}

	public void setPercentComplete(Integer percentComplete) {
		this.percentComplete = percentComplete;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public void setReplacementID(Integer replacementID) {
		this.replacementID = replacementID;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public void setDroppedToNode(String droppedToNode) {
		this.droppedToNode = droppedToNode;
	}

	public void setBefore(boolean before) {
		this.before = before;
	}

	public void setDefaultOption(boolean defaultOption) {
		this.defaultOption = defaultOption;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setNodeFrom(String nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public void setNodeTo(String nodeTo) {
		this.nodeTo = nodeTo;
	}

	public void setFromProjectConfig(boolean fromProjectConfig) {
		this.fromProjectConfig = fromProjectConfig;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public CssStyleBean getCssStyleBean() {
		return cssStyleBean;
	}

	public void setCssStyleBean(CssStyleBean cssStyleBean) {
		this.cssStyleBean = cssStyleBean;
	}

	public boolean isOverwriteExisting() {
		return overwriteExisting;
	}

	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

	public boolean isClearChildren() {
		return clearChildren;
	}

	public void setClearChildren(boolean clearChildren) {
		this.clearChildren = clearChildren;
	}
}
