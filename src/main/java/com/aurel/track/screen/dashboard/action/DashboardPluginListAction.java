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

package com.aurel.track.screen.dashboard.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.lucene.util.poi.XLSTextStripper;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 *
 */
public class DashboardPluginListAction extends ActionSupport implements Preparable, SessionAware {
	
	private static final Logger LOGGER = LogManager.getLogger(DashboardPluginListAction.class);

	private static final long serialVersionUID = 340L;
	private Map session;
	private Locale locale;
	@Override
	public void prepare() throws Exception {
		locale=(Locale) session.get(Constants.LOCALE_KEY);
	}
	public String list(){
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		if(locale==null){
			locale=Locale.getDefault();
		}
		List< DashboardDescriptor > pluginList=DashboardUtil.getDashboardPlugins();
		try {
			JSONUtility.prepareServletResponseJSON(ServletActionContext.getResponse());
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.println(encodePluginList(pluginList));
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;

	}

	private String encodePluginList(List< DashboardDescriptor > pluginList){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(pluginList!=null){
			for (Iterator<DashboardDescriptor> iterator = pluginList.iterator(); iterator.hasNext();) {
				DashboardDescriptor dashboardDescriptor = iterator.next();
				sb.append(encodeDashboardDescriptor(dashboardDescriptor,locale));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String encodeDashboardDescriptor(DashboardDescriptor dashboardDescriptor,Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");

		if(dashboardDescriptor!=null){
			JSONUtility.appendStringValue(sb, "id", dashboardDescriptor.getId());
			JSONUtility.appendStringValue(sb,"name",dashboardDescriptor.getName());
			JSONUtility.appendStringValue(sb,"label",
					LocalizeUtil.getLocalizedText(dashboardDescriptor.getLabel(), locale, ResourceBundleManager.DASHBOARD_RESOURCES));
			JSONUtility.appendStringValue(sb,"description",
					LocalizeUtil.getLocalizedText(dashboardDescriptor.getDescription(), locale, ResourceBundleManager.DASHBOARD_RESOURCES),true);
		}
		sb.append("}");
		return sb.toString();
	}
	public Map getSession() {
		return session;
	}

	@Override
	public void setSession(Map session) {
		this.session = session;
	}
}
