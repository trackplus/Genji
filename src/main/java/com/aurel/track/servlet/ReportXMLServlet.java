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

package com.aurel.track.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.report.execute.ReportBeansToXML;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.itemNavigator.ItemNavigatorBL;
import com.aurel.track.itemNavigator.QueryContext;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.query.ReportQueryBL;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.emailHandling.Html2Text;

public class ReportXMLServlet extends HttpServlet {
	private static final long serialVersionUID = -8476302503217501620L;
	private static final Logger LOGGER = LogManager.getLogger(ReportXMLServlet.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String linkReportEncoded = request.getParameter("query");
		String linkReport = ReportQueryBL.b(linkReportEncoded);
		Map<String, String> mapParams = ReportQueryBL.decodeMapFromUrl(linkReport);

		String user = mapParams.get("user");
		String pswd = mapParams.get("pswd");
		String queryIDStr = mapParams.get("queryID");
		String reportTypeStr = mapParams.get("reportType");
		String repositoryTypeStr = mapParams.get("repositoryType");

		response.setContentType("text/xml");
		Integer queryID = null;
		if (queryIDStr != null) {
			queryID = Integer.decode(queryIDStr);
		}
		Integer reportType = null;
		if (reportTypeStr != null) {
			reportType = Integer.decode(reportTypeStr);
		}
		Integer repositoryType = null;
		if (repositoryTypeStr != null) {
			repositoryType = Integer.decode(repositoryTypeStr);
		}

		TPersonBean personBean = login(user, pswd);
		if (personBean == null) {
			createResponseError(response.getWriter(), "error.invalidUserOrPassword");
		} else {
			Locale locale = personBean.getLocale();
			OutputStream outputStream = response.getOutputStream();
			executeReport(request, outputStream, queryID, reportType, repositoryType, personBean, locale);
			outputStream.flush();
			outputStream.close();
		}

	}

	private TPersonBean login(String user, String pswd) {
		TPersonBean personBean = PersonBL.loadByLoginName(user);
		if (personBean != null && pswd.equals(personBean.getPasswd())) {
			return personBean;
		} else {
			// try guest login
			if (ApplicationBean.getInstance().getSiteBean().getAutomaticGuestLogin()) {
				TPersonBean anonimus = PersonBL.getAnonymousIfActive();
				if (anonimus != null) {
					return anonimus;
				}
			}
		}
		return null;
	}

	private void executeReport(HttpServletRequest request, OutputStream outputStream, Integer queryID, Integer reportType, Integer repositoryType,
			TPersonBean personBean, Locale locale) {
		QueryContext queryContext = new QueryContext();
		queryContext.setQueryID(queryID);
		queryContext.setQueryType(ItemNavigatorBL.QUERY_TYPE.SAVED);
		List<ReportBean> reportBeanList = new ArrayList<ReportBean>();
		try {
			reportBeanList = ItemNavigatorBL.executeQuery(personBean, locale, queryContext);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
		}
		String title = ItemNavigatorBL.getFilterTitle(queryID, locale);
		writeBeansToMavenXML(outputStream, reportBeanList, personBean, locale, title);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	private void writeBeansToMavenXML(OutputStream outputStream, List<ReportBean> items, TPersonBean personBean, Locale locale, String title) {
		Document dom = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			dom = builder.newDocument();
		} catch (FactoryConfigurationError e) {
			LOGGER.error("Creating the DOM document failed with FactoryConfigurationError:" + e.getMessage());
			return;
		} catch (ParserConfigurationException e) {
			LOGGER.error("Creating the DOM document failed with ParserConfigurationException: " + e.getMessage());
			return;
		}
		Element root = dom.createElement("document");
		dom.appendChild(root);
		root.setAttribute("xmlns", "http://maven.apache.org/changes/1.0.0");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://maven.apache.org/changes/1.0.0 http://maven.apache.org/xsd/changes-1.0.0.xsd");

		/*
		 * <properties> <title>Changes Tester Project</title> <author
		 * email="adib@exist.com">Johnny R. Ruiz III</author> </properties>
		 */
		Element propertiesEl = dom.createElement("properties");
		root.appendChild(propertiesEl);
		Element titleEl = createDomElement("title", title, dom);
		propertiesEl.appendChild(titleEl);
		Element authorEl = createDomElement("author", personBean.getFullName(), dom);
		authorEl.setAttribute("email", personBean.getEmail());
		propertiesEl.appendChild(authorEl);

		// body
		Element bodyEl = dom.createElement("body");
		root.appendChild(bodyEl);
		List<TReleaseBean> allReleases = ReleaseBL.loadAll();
		Map<Integer, List<ReportBean>> groupedByRelease = new HashMap<Integer, List<ReportBean>>();
		int[] workItemIDs = null;
		if (items != null) {
			workItemIDs = new int[items.size()];
			for (int i = 0; i < items.size(); i++) {
				ReportBean rb = items.get(i);
				workItemIDs[i] = rb.getWorkItemBean().getObjectID().intValue();
				Integer releaseID = rb.getWorkItemBean().getReleaseScheduledID();
				if (releaseID != null) {
					List<ReportBean> lst = groupedByRelease.get(releaseID);
					if (lst == null) {
						lst = new ArrayList<ReportBean>();
					}
					lst.add(rb);
					groupedByRelease.put(releaseID, lst);
				}
			}
		}
		for (int i = 0; i < allReleases.size(); i++) {
			TReleaseBean release = (TReleaseBean) allReleases.get(i);
			List<ReportBean> lst = groupedByRelease.get(release.getObjectID());
			if (lst != null && lst.size() > 0) {
				// <release version="1.1" date="2010-03-01"
				// description="extradescr release">*/
				Element releaseEl = dom.createElement("release");
				bodyEl.appendChild(releaseEl);
				releaseEl.setAttribute("version", release.getLabel());
				if (release.getDueDate() != null) {
					releaseEl.setAttribute("date", DateTimeUtils.getInstance().formatISODate(release.getDueDate()));
				}
				if (release.getDescription() != null) {
					releaseEl.setAttribute("description", release.getDescription());
				}
				for (int j = 0; j < lst.size(); j++) {
					// <action dev="adib" type="add" issue="isuue-12"
					// due-to="Allan Ramirez"
					// due-to-email="aramirez@exist.com>desc</action>
					ReportBean rb = lst.get(j);
					TWorkItemBean workItemBean = rb.getWorkItemBean();
					Element actionEl = createDomElement("action", workItemBean.getSynopsis(), dom);
					releaseEl.appendChild(actionEl);
					TPersonBean resp = LookupContainer.getPersonBean(workItemBean.getResponsibleID());
					actionEl.setAttribute("dev", resp.getUsername());
					actionEl.setAttribute("due-to", resp.getFullName());
					actionEl.setAttribute("due-to-email", resp.getEmail());
					TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(workItemBean.getListTypeID(), locale);
					if (listTypeBean != null) {
						actionEl.setAttribute("issueType", listTypeBean.getLabel());
					}
					actionEl.setAttribute("issue", workItemBean.getObjectID().toString());
				}

			}

		}
		ReportBeansToXML.convertToXml(outputStream, dom);
	}

	private Element createDomElement(String elementName, String elementValue, Document dom) {
		Element element = null;
		try {
			try {
				element = dom.createElement(elementName);
			} catch (DOMException e) {
				LOGGER.warn("Creating an XML node with the element name " + elementName + " failed with dom exception " + e);
			}
			if (element == null) {
				return null;
			}
			if (elementValue == null || "".equals(elementValue.trim())) {
				element.appendChild(dom.createTextNode(""));
			} else {
				try {
					element.appendChild(dom.createTextNode(Html2Text.stripNonValidXMLCharacters(elementValue)));
				} catch (DOMException e) {
					LOGGER.info("Creating the node for text element " + elementName + " and the value " + elementValue + " failed with dom exception " + e);
					element.appendChild(dom.createTextNode(""));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Creating an XML node with the element name " + elementName + " failed with " + e);
		}
		return element;
	}

	protected void createResponseError(PrintWriter out, String errorMessage) {
		out.println("<?xml version=\"1.0\"?>");
		out.println("<response>");
		out.println("<status>");
		out.println("error");
		out.println("</status>");
		out.println("<errorMessage>");
		out.println(errorMessage);
		out.println("</errorMessage>");
		out.println("</response>");
		out.println("\n");
	}

}
