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


package com.aurel.track.exchange.docx.importer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.release.ReleasePickerBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Rendering preview from docx file
 * @author Tamas
 *
 */
public class DocxImportAction extends ActionSupport 
	implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
    private TPersonBean personBean;
	private Integer personID;
	private Locale locale;
	private Integer selectedSheet;
	private String fileName;
	private String docxFilePath;
	
	private Integer projectOrReleaseID;
	private Integer parentID;
	private Integer workItemID;
	
	private Map<Integer, Integer> invalidValueHandlingMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> defaultValuesMap = new HashMap<Integer, Integer>();
	private Map<String, Boolean> overwriteMap = new HashMap<String, Boolean>();
	
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
        personBean =  (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
		docxFilePath = AttachBL.getDocxImportDirBase() + personID + File.separator + fileName;
	}
	
	/**
	 * Render the addTo wizard panel for 
	 * 1. adding a new document hierarchy in a project/release or
	 * 2. adding a document hierarchy an existing parent item or
	 * 3. updating an existing document item's document part hierarchy
	 * @return
	 */
	public String addTo() {
		//workItemID=275;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		AttachBL.download(docxFilePath, byteArrayOutputStream);
		if (workItemID==null) {
			String projectReleaseTree =  ReleasePickerBL.getTreeJSON(
	                null, null, false, true,
	                false, true, true, true,
	                false, null, personBean, locale);
			JSONUtility.encodeJSON(servletResponse, 
					DocxImportJSON.getAddToItemJSON(fileName, projectReleaseTree));
		} else {
			TWorkItemBean workItemBean = null;
			try {
				workItemBean = ItemBL.loadWorkItem(workItemID);
			} catch (ItemLoaderException e) {
			}
			if (workItemBean!=null) {
				String itemNo = ItemBL.getItemNo(workItemBean);
				String itemTitle = workItemBean.getSynopsis();
				JSONUtility.encodeJSON(servletResponse, 
						DocxImportJSON.getUpdateItemJSON(workItemID, itemNo, itemTitle));
			}
		}
		return null;
	}
	
	/**
	 * Render the invalid value handling page
	 */
	public String preview() {
		//FIXME as request parameter
		//workItemID=275;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		AttachBL.download(docxFilePath, byteArrayOutputStream);
		HTMLParser htmlParser = new HTMLParser();
		HTMLContent htmlContent = htmlParser.parseDocument(byteArrayOutputStream, locale);
		List<ItemNode> itemNodeList = htmlContent.getChapters();
		List<TreeNode> treeNodes = PreviewBL.convertToTreeNodeFromItemNode(itemNodeList);
		JSONUtility.encodeJSON(servletResponse, DocxImportJSON.createPreviewJSON(treeNodes));
		return null;
	}
	
	/**
	 * Execute the import from excel 
	 * @return
	 */
	public String execute() {
		if (workItemID==null) {
			
		} else {
			
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		AttachBL.download(docxFilePath, byteArrayOutputStream);
		HTMLParser htmlParser = new HTMLParser();
		HTMLContent htmlContent = htmlParser.parseDocument(byteArrayOutputStream, locale);
		List<ItemNode> itemNodeList = htmlContent.getChapters();
		String styles = htmlContent.getStyle();
		return null;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<Integer, Integer> getInvalidValueHandlingMap() {
		return invalidValueHandlingMap;
	}

	public void setInvalidValueHandlingMap(
			Map<Integer, Integer> invalidValueHandlingMap) {
		this.invalidValueHandlingMap = invalidValueHandlingMap;
	}

	public Map<Integer, Integer> getDefaultValuesMap() {
		return defaultValuesMap;
	}

	public void setDefaultValuesMap(Map<Integer, Integer> defaultValuesMap) {
		this.defaultValuesMap = defaultValuesMap;
	}

	public Integer getSelectedSheet() {
		return selectedSheet;
	}

	public void setSelectedSheet(Integer selectedSheet) {
		this.selectedSheet = selectedSheet;
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, Boolean> getOverwriteMap() {
		return overwriteMap;
	}

	public void setOverwriteMap(Map<String, Boolean> overwriteMap) {
		this.overwriteMap = overwriteMap;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public Integer getProjectOrReleaseID() {
		return projectOrReleaseID;
	}

	public void setProjectOrReleaseID(Integer projectOrReleaseID) {
		this.projectOrReleaseID = projectOrReleaseID;
	}

	
	public Integer getParentID() {
		return parentID;
	}

	public void setParentID(Integer parentID) {
		this.parentID = parentID;
	}

	
	public Integer getWorkItemID() {
		return workItemID;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
	
	
}
