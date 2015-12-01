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


package com.aurel.track.exchange.msProject.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mspdi.MSPDIWriter;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeBL;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.exchange.msProject.importer.MSProjectImportException;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Exporting the into an msProject xml file
 * @author Tamas
 *
 */
public class MsProjectExportAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private Locale locale;
	//the submitted selected project (negative value) or release (positive value)
	private Integer projectOrReleaseID;
	private TPersonBean personBean;
	private boolean notClosed;
	private HttpServletResponse servletResponse;

	/**
	 * Prepares the standard data
	 */
	@Override
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
	}

	/**
	 * Render project export first time
	 */
	@Override
	public String execute() {
		List<Integer> accessRights = new LinkedList<Integer>();
		accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.PROJECTADMIN));
		accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.MODIFYANYTASK));
		accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.CREATETASK));
		int[] rights = GeneralUtils.createIntArrFromIntegerList(accessRights);
		List<TreeNode> projectTree = ProjectBL.getProjectNodesByRightEager(
				false, personBean, false, rights, true, true);
		String releaseTree = MsProjectExportJSON.encodeProjectPickerData(projectTree);
		if(projectTree != null && projectTree.size() > 0) {
			projectOrReleaseID = Integer.valueOf(projectTree.get(0).getId()) * (-1);
		}else {
			projectOrReleaseID = 0;
		}
		String importFileInfo = MsProjectExporterBL.getImportFileInfo(projectOrReleaseID, locale);
		JSONUtility.encodeJSON(servletResponse, MsProjectExportJSON.getLoadJSON(projectOrReleaseID, releaseTree, importFileInfo));
		return null;
	}

    public String importFileInfo() {
        JSONUtility.encodeJSON(servletResponse, MsProjectExportJSON.getImportFileInfoJSON(MsProjectExporterBL.getImportFileInfo(projectOrReleaseID, locale)));
        return null;
    }


	/**
	 * Save the field mappings for the next use
	 * @return
	 * @throws MSProjectImportException
	 */
	public String export() {
		try {
			//initialize a new MsProjectImporterBean
			MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean =
				MsProjectExchangeBL.initMsProjectExchangeBeanForExport(
						projectOrReleaseID, personBean, locale);

			ProjectFile project = MsProjectExporterBL.export(msProjectExchangeDataStoreBean, notClosed, personBean.getObjectID());

			DownloadUtil.prepareResponse(ServletActionContext.getRequest(),
					servletResponse, "TrackReport.xml", "text/xml");
			OutputStream outputStream = servletResponse.getOutputStream();
			MSPDIWriter writer = new MSPDIWriter();
			writer.write(project, outputStream);
			return null;
		} catch (MSProjectImportException e) {
			addActionError(getText(e.getMessage()));
		} catch (IOException e) {
			addActionError(e.getMessage());
		}
		return null;
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setProjectOrReleaseID(Integer projectOrReleaseID) {
		this.projectOrReleaseID = projectOrReleaseID;
	}

    public void setNotClosed(boolean notClosed) {
		this.notClosed = notClosed;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
