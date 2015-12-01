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
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.dashboard.ReleaseNoteWrapper;
import com.aurel.track.report.datasource.releaseNotes.ReleaseNotesDatasource;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DownloadUtil;
import com.aurel.track.util.StringArrayParameterUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class ReleaseNotesAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware, ServletRequestAware{
	private static final long serialVersionUID = 1543744965855309661L;
	private static final Logger LOGGER = LogManager.getLogger(ReleaseNotesAction.class);

	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private HttpServletRequest servletRequest;
	private TPersonBean personBean;
	private Locale locale;
	private Integer project;
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
			personBean = PersonBL.loadByLoginName("guest");
		}
	}

	@Override
	public String execute() {
		boolean isProject = false;
		Integer projectOrReleaseID;
		Integer projectID = null;
		Integer releaseID = null;
		TProjectBean projectBean = null;
		String projectLabel = "";
		String releaseLabel = "";
		if (project.intValue()<0) {
			projectID = Integer.valueOf(-project.intValue());
			projectBean = LookupContainer.getProjectBean(projectID);
			projectOrReleaseID = projectID;
			isProject = true;
		} else {
			releaseID = project;
			TReleaseBean releaseBean = LookupContainer.getReleaseBean(releaseID);
			if (releaseBean!=null) {
				releaseLabel = releaseBean.getLabel();
				projectID = releaseBean.getProjectID();
				projectBean = LookupContainer.getProjectBean(projectID);
			}
			projectOrReleaseID = releaseID;
		}
		if (projectBean==null) {
			writeReleaseNotes(null, null, null, null);
			return null;
		} else {
			projectLabel = projectBean.getLabel();
		}
		if (selectedIssueTypes == null || selectedIssueTypes.isEmpty()) {
			selectedIssueTypes = new ArrayList<String>();
			List<TListTypeBean> tlbs = IssueTypeBL.loadAllSelectable();
			for (TListTypeBean tlb: tlbs) {
				selectedIssueTypes.add(tlb.getObjectID().toString()); 
			}
		}
		FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(isProject, projectOrReleaseID, true, false, true);
		List<TWorkItemBean> closedItems = null;
		try {
			closedItems = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		List<ReleaseNoteWrapper> closedItemWrappers = ReleaseNotesDatasource.getReleaseNotesForSingleRelease(closedItems, locale, selectedIssueTypes);
		filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(isProject, projectOrReleaseID, true, true, false);
		List<TWorkItemBean> openedItems = null;
		try {
			openedItems = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		} 
		List<ReleaseNoteWrapper> openItemWrappers = ReleaseNotesDatasource.getReleaseNotesForSingleRelease(openedItems, locale, selectedIssueTypes);
		writeReleaseNotes(closedItemWrappers, openItemWrappers, projectLabel, releaseLabel/*, GeneralUtils.createMapFromList(projectBeans)*/);
		return null;
	}

	private void writeReleaseNotes(List<ReleaseNoteWrapper> closedItems, List<ReleaseNoteWrapper> openItems, String projectLabel, String releaseLabel/*, Map<Integer, TProjectBean> projectBeansMap*/) {
		String fileName = "releaseNotes.html";
		DownloadUtil.prepareResponse(servletRequest, servletResponse, fileName, "html", null, false);
		// copy to output
		LOGGER.debug("Delivering file...");
		OutputStream outstream=null;
		try {
			outstream = servletResponse.getOutputStream();
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Error obtaining output stream from request.",e);
			return;
		}
		boolean useProjectSpecificIDs = false;
		if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
			useProjectSpecificIDs = true;
		}
		try {
			StringBuffer bufh = new StringBuffer();
			StringBuffer buf = new StringBuffer();
			bufh.append(writeHtmlHeader(projectLabel, releaseLabel));
			boolean hasClosedItems = closedItems!=null && !closedItems.isEmpty();
			boolean hasOpenItems = openItems!=null && !openItems.isEmpty();
			if (!hasClosedItems && !hasOpenItems) {
				bufh.append(LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.noItems",locale));
			} else {
				if (hasClosedItems) {
					buf.append(writeItems(closedItems, projectLabel, releaseLabel, "report.releaseNotes.resolved", useProjectSpecificIDs/*, projectBeansMap*/));
				}
				if (hasOpenItems) {
					buf.append(writeItems(openItems, projectLabel, releaseLabel, "report.releaseNotes.unresolved", useProjectSpecificIDs/*, projectBeansMap*/));
				}
			}
			bufh.append(buf);
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
	
	/**
	 * Write the items into a buffer
	 * @param items
	 * @param projectLabel
	 * @param releaseLabel
	 * @return
	 */
	private String writeItems(List<ReleaseNoteWrapper> items, String projectLabel, String releaseLabel, String resolvedKey, boolean useProjectSpecificIDs/*, Map<Integer, TProjectBean> projectBeansMap*/) {
		StringBuffer buf = new StringBuffer();
		buf.append("<h1>"
				+ LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.title",locale)
			      + " " + projectLabel + " " + releaseLabel + " " + LocalizeUtil.getLocalizedTextFromApplicationResources(resolvedKey, locale) + "</h1>\n");
		for (ReleaseNoteWrapper group: items) {
			String itype = group.getIssueType().getLabel();
			buf.append("<h2>"+itype+"</h2>\n");
			buf.append("<ul class='it"+group.getIssueType().getObjectID()+"'>\n");
			for (TWorkItemBean item: group.getWorkItems()) {
				if (useProjectSpecificIDs) {
					TProjectBean projectBean = LookupContainer.getProjectBean(item.getProjectID());
					String projectPrefix = null;
					if (projectBean!=null) {
						projectPrefix = projectBean.getPrefix();
					}
					if (projectPrefix==null) {
						projectPrefix = "";
					}
					buf.append("<li><span class='oid'>[" + projectPrefix+item.getIDNumber() + "]</span> &ndash; ");
				} else {
					buf.append("<li><span class='oid'>[" + item.getObjectID() + "]</span> &ndash; ");
				}
				buf.append("<span class='title'>"+item.getSynopsis() + "</span></li>\n");
			}
			buf.append("</ul>\n");
		}
		return buf.toString();
	}
	
	private String writeHtmlHeader(String projectLabel, String releaseLabel) {
		StringBuffer buf = new StringBuffer();
		buf.append("<!DOCTYPE html"
				+"PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+"<html xml:lang=\"en-us\" lang=\"en-us\">"
				+"<head><meta xmlns=\"http://www.w3.org/1999/xhtml\" name=\"description\" content=\"\"/>"
				+"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"
				+"<title>" 
				+ LocalizeUtil.getLocalizedTextFromApplicationResources("report.releaseNotes.title",locale)
			    + " " + projectLabel + " " + releaseLabel + "</title>\n"
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


	public void setProject(Integer project) {
		this.project = project;
	}


	public List getSelectedIssueTypes() {
		return selectedIssueTypes;
	}

	public void setSelectedIssueTypes(String types) {
		selectedIssueTypes = StringArrayParameterUtils.splitSelection(types);	
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

}
