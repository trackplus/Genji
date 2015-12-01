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

package com.aurel.track.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.report.dashboard.ReleaseNoteWrapper;
import com.aurel.track.report.datasource.releaseNotes.ReleaseNotesDatasource;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DetectBrowser;
import com.aurel.track.util.StringArrayParameterUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class FaqAction extends ActionSupport implements Preparable, SessionAware/*, RequestAware */{
	private static final long serialVersionUID = 1543744965855309661L;
	private static final Logger LOGGER = LogManager.getLogger(ReleaseNotesAction.class);

	private Map<String,Object> session;
	private TPersonBean personBean;
	private Locale locale;
	private String project;
	private String projectId;

	private List selectedIssueTypes;


	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		if (personBean == null) {
			personBean = PersonBL.loadByLoginName(TPersonBean.GUEST_USER);
		}
	}

	@Override
	public String execute(){
		
		Integer projectIdInt = null;
		TProjectBean projectBean = null;
		if (projectId == null || "".equals(projectId)) {
			projectBean = ProjectBL.loadByLabel(project);
		} else {
			try {
				projectIdInt = new Integer(projectId);
				projectBean = LookupContainer.getProjectBean(projectIdInt);
			} catch (Exception e) {
				// ignore
			}
		}
		
		if (projectBean == null) {
			writeFaqs(null,null);
			return null;
		}
		FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(true, projectBean.getObjectID(), true, true, true);
		if (selectedIssueTypes == null || selectedIssueTypes.isEmpty()) {
			selectedIssueTypes = new ArrayList<String>();
			List<TListTypeBean> tlbs = IssueTypeBL.loadAllSelectable();
			for (TListTypeBean tlb: tlbs) {
				selectedIssueTypes.add(tlb.getObjectID().toString()); 
			}
		}

		List<TWorkItemBean> items = null;
		try {
			items = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		List<ReleaseNoteWrapper> relItems = ReleaseNotesDatasource.getReleaseNotesForSingleRelease(items, locale, selectedIssueTypes);
		writeFaqs(relItems, projectBean);
		return null;
	}

	private void writeFaqs(List<ReleaseNoteWrapper> items, TProjectBean projectBean) {
		String disposition="inline";
		disposition="attachment";
		String fileName = "releaseNotes.html";
		HttpServletResponse response=ServletActionContext.getResponse();
		HttpServletRequest request=ServletActionContext.getRequest();
		// setup the http header
		response.setHeader("Pragma", "");
		DetectBrowser dtb = new DetectBrowser();
		dtb.setRequest(request);
		// Workaround for a IE bug
		if (dtb.getIsIE()) {
			response.setHeader("Cache-Control", "private");
			response.setHeader("Expires", "0");
			response.setHeader("Content-Disposition", 
					disposition+"; filename=\"" 
							+ fileName + "\"");

		}
		else {
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-Disposition", 
					disposition+"; filename=\"" 
							+ fileName + "\"");
		}
		String contentType = "html";
		response.setHeader("Content-Type", contentType);

		// only for debugging now
		LOGGER.debug("set Content-Type: " + contentType);

		// copy to output
		LOGGER.debug("Delivering file...");
		OutputStream outstream=null;
		try {
			outstream = response.getOutputStream();
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Error obtaining output stream from request.",e);
			return;
		}
		try {
			StringBuffer bufh = new StringBuffer();
			StringBuffer buf = new StringBuffer();
			bufh.append(writeHtmlHeader(projectBean));
			if (items == null || items.isEmpty()) {
				bufh.append(LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.noItems",locale));
			} else {
				String prefix = null;
				if (projectBean != null) {
					prefix = projectBean.getPrefix();
				}
				buf.append("<h1>"
						+ LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.title",locale)
					      + " " + projectBean.getLabel() + "</h2>\n");
				for (ReleaseNoteWrapper group: items) {
					String itype = group.getIssueType().getLabel();
					buf.append("<h2>"+itype+"</h2>\n");
					buf.append("<ul class='it"+group.getIssueType().getObjectID()+"'>\n");
					for (TWorkItemBean item: group.getWorkItems()) {
						if (prefix != null && !"".equals(prefix)) {
							buf.append("<li><span class='oid'>[" + prefix+item.getIDNumber() + "]</span> &ndash; ");
						} else {
							buf.append("<li><span class='oid'>[" + item.getObjectID() + "]</span> &ndash; ");
						}
						buf.append("<span class='title'>"+item.getSynopsis() + "</span></li>\n");
					}
					buf.append("</ul>\n");
				}
			}
			bufh.append(buf.toString());
			bufh.append("<h2>" + 
			      LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.editCopy.title",locale)
			      + "</h2>\n");
			bufh.append("<p class=\"relnotecopyhint\">"
			      + LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.editCopy.hint",locale)
			      + "</p>\n");
			bufh.append("<textarea rows='40' cols='120' id='relnoteText'>\n").append(buf.toString()).append("</textarea>");
			bufh.append("</body></html>");
			outstream.write(bufh.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Error download release notes",e);
			return;
		}
		finally{
			if (outstream != null) { 
				try { 
					outstream.flush();
					outstream.close(); 
				} 
				catch (Exception t) {
					// just ignore
				}
			}
		}
	} 
	
	private String writeHtmlHeader(TProjectBean projectBean) {
		StringBuffer buf = new StringBuffer();
		buf.append("<!DOCTYPE html"
				+"PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+"<html xml:lang=\"en-us\" lang=\"en-us\">"
				+"<head><meta xmlns=\"http://www.w3.org/1999/xhtml\" name=\"description\" content=\"\"/>"
				+"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"
				+"<title>" 
				+ LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.title",locale)
			    + " " + projectBean.getLabel() + "</title>\n"
			    + "<style type=\"text/css\">"
                + "body, p, h1, h2, h3, div {"
			    + "	font-family: Arial,Helvetica,sans-serif, Tahoma, Verdana;\n"
				+ " font-size: 10pt;\n"
				+ " color: #3A394B;}\n"
				+ " h1 {font-size: 18pt;}\n"
				+ " h2 {font-size: 15pt;}\n"
				+ " .oid {color:#cc0000}\n"
				+ " .title {}\n"
				+ " .relnotecopyhint {margin-top:0.2em; margin-bottom:0.2em;}\n"
				+ " #relnoteText {font-size:10pt;font-family:Courier New, Courier,mono-spaced}\n"
                + "</style>"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href='relstyle.css'/>"
				+"</head>"
				+"<body>");
		return buf.toString();
	}


	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List getSelectedIssueTypes() {
		return selectedIssueTypes;
	}

	public void setSelectedIssueTypes(String types) {
		selectedIssueTypes = StringArrayParameterUtils.splitSelection(types);	
	}
	
}
