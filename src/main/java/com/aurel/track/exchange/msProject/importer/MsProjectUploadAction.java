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

package com.aurel.track.exchange.msProject.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.project.release.ReleasePickerBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.exchange.UploadHelper;
import com.aurel.track.exchange.excel.ExcelUploadAction;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class MsProjectUploadAction extends ActionSupport implements Preparable, SessionAware {
	
	private static final long serialVersionUID = 1L;
	private static String LAST_PROJECT_RELEASE = "lastProjectRelease";
	public static String MSPROJECT_IMPORT = "msProjectImport";
	static Logger LOGGER = LogManager.getLogger(ExcelUploadAction.class);
	private Map<String, Object> session;
    private Locale locale;
	private File uploadFile;
	private String uploadFileFileName;
	private TPersonBean personBean;	
	
	public TPersonBean getPersonBean() {
		return personBean;
	}

	public void setPersonBean(TPersonBean personBean) {
		this.personBean = personBean;
	}

	//for msProject import submit: it can be projectID (negative value) or releaseID (positive value)
	private Integer projectOrReleaseID;
	//from project edit: always positive and represents a project
	
	
	
	
	
	@Override
	public void prepare() throws Exception {
        locale = (Locale)session.get(Constants.LOCALE_KEY);
        personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}
	
	/**
	 * Render the import page
	 */
	@Override
	public String execute() {
        List<Integer> accessRights = new LinkedList<Integer>();
        accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.PROJECTADMIN));
        accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.MODIFYANYTASK));
        accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.CREATETASK));
        List<TreeNode> projectTree = ReleaseBL.getReleaseNodesEager(personBean, null, false, false, true, true, null, false, true, false, null, locale);
        String releaseTree = JSONUtility.getTreeHierarchyJSON(projectTree, false, false);
        if(projectTree != null && projectTree.size() > 0) {
        	projectOrReleaseID = Integer.valueOf(projectTree.get(0).getId());
        }else {
        	projectOrReleaseID = 0;
        }
		String accept = "application/xml";
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), 
			MsProjectImportJSON.getUploadJSON(accept,
					projectOrReleaseID, releaseTree));
		
		return null;
	}
	
	/**
	 * Save the MsProject import file
	 * @return
	 */
	public String upload() {
		session.put(LAST_PROJECT_RELEASE, projectOrReleaseID);
		return UploadHelper.upload(uploadFile, uploadFileFileName, AttachBL.getMsProjectImportDirBase() + String.valueOf(projectOrReleaseID), locale, MSPROJECT_IMPORT);
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	/**
	 * Getter also needed see result in struts.xml
	 * @return
	 */
	public Integer getProjectOrReleaseID() {
		return projectOrReleaseID;
	}
	
	public void setProjectOrReleaseID(Integer projectOrReleaseID) {
		this.projectOrReleaseID = projectOrReleaseID;
	}
	

}
