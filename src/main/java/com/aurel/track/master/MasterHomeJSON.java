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

package com.aurel.track.master;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.ModuleDescriptor;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;

public class MasterHomeJSON {
	public static String encodeModules(List list, TPersonBean personBean) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (list != null && personBean.getUserLevel().intValue() != TPersonBean.USERLEVEL.CLIENT.intValue()) {
			ModuleDescriptor moduleDescriptor;
			for (int i = 0; i < list.size(); i++) {
				moduleDescriptor = (ModuleDescriptor) list.get(i);
				sb.append(encodeModule(moduleDescriptor, true, null));
				if (i < list.size() - 1) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * This method creates a JSON object which include information about
	 * modules. The object is used in two different places: 1) For creating the
	 * menu item in top left corner => isForList == true, PersonBean can be null
	 * 2) For loading the module after selecting one menu item from top left
	 * corner: isForList == false, PersonBean musn't be null !
	 *
	 * @param moduleDescriptor
	 * @param isForList
	 * @return
	 */
	public static String encodeModule(ModuleDescriptor moduleDescriptor, boolean isForList, TPersonBean personBean) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "name", moduleDescriptor.getName());
		JSONUtility.appendStringValue(sb, "iconCls", moduleDescriptor.getIconCls());
		JSONUtility.appendBooleanValue(sb, "useHeader", moduleDescriptor.isUseHeader());
		JSONUtility.appendStringValue(sb, "description", moduleDescriptor.getDescription());
		String mdurl = moduleDescriptor.getCleanedUrl();
		moduleDescriptor.setUrl(mdurl);
		if (isForList) {
			JSONUtility.appendStringValue(sb, "url", mdurl);
		} else {
			JSONUtility.appendStringValue(sb, "url", getModuleUrl(moduleDescriptor, personBean));
		}
		JSONUtility.appendStringValue(sb, "id", moduleDescriptor.getId(), true);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * The following method creates a web svn module JSON for: clicking to a
	 * revision number from Version Control Activity
	 *
	 * @param moduleDescriptor
	 * @param isForList
	 * @param PERSON_BEAN
	 * @return
	 */
	public static String encodeWebSVNModuleAfterOperation(ModuleDescriptor moduleDescriptor, String repName, String revNo, String vcPath) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "name", moduleDescriptor.getName());
		JSONUtility.appendStringValue(sb, "iconCls", moduleDescriptor.getIconCls());
		JSONUtility.appendBooleanValue(sb, "useHeader", moduleDescriptor.isUseHeader());
		JSONUtility.appendStringValue(sb, "description", moduleDescriptor.getDescription());
		String url = moduleDescriptor.getCleanedUrl();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();

		url = StringArrayParameterUtils.appendSlashToURLString(url) + "listing.php?repname=" + GeneralUtils.encodeURL(repName) + "&rev=" + revNo + "&sessionID=" + httpSession.getId()
				+ "&baseURL=" + getBaseUrl(request);
		if (vcPath != null) {
			url += "&path=" + vcPath;
		}
		url = url.replace("${SERVER}", ApplicationBean.getApplicationBean().getSiteBean().getServerURL());
		JSONUtility.appendStringValue(sb, "url", url);
		JSONUtility.appendStringValue(sb, "id", moduleDescriptor.getId(), true);
		sb.append("}");
		return sb.toString();
	}

	public static String encodeViewGitModuleAfterOperation(ModuleDescriptor moduleDescriptor, String repName, String revNo, String vcPath) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "name", moduleDescriptor.getName());
		JSONUtility.appendStringValue(sb, "iconCls", moduleDescriptor.getIconCls());
		JSONUtility.appendBooleanValue(sb, "useHeader", moduleDescriptor.isUseHeader());
		JSONUtility.appendStringValue(sb, "description", moduleDescriptor.getDescription());
		String url = moduleDescriptor.getCleanedUrl();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		url = StringArrayParameterUtils.appendSlashToURLString(url) + "?p=" + GeneralUtils.encodeURL(vcPath) + ";" + "a=commit" + "h=" + revNo;
		if (vcPath != null) {
			url += "&path=" + vcPath;
		}
		url = url.replace("${SERVER}", ApplicationBean.getApplicationBean().getSiteBean().getServerURL());
		JSONUtility.appendStringValue(sb, "url", url);
		JSONUtility.appendStringValue(sb, "id", moduleDescriptor.getId(), true);
		sb.append("}");
		return sb.toString();
	}

	public static String encodeWenSVNModuleForVCActStream(ModuleDescriptor moduleDescriptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "name", moduleDescriptor.getName());
		JSONUtility.appendStringValue(sb, "iconCls", moduleDescriptor.getIconCls());
		JSONUtility.appendBooleanValue(sb, "useHeader", moduleDescriptor.isUseHeader());
		JSONUtility.appendStringValue(sb, "description", moduleDescriptor.getDescription());
		JSONUtility.appendStringValue(sb, "id", moduleDescriptor.getId(), true);
		sb.append("}");
		return sb.toString();
	}

	public static String getBaseUrl(HttpServletRequest request) {

		if ((request.getServerPort() == 80) || (request.getServerPort() == 443)) {
			return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		} else {
			return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		}

	}

	/**
	 * This method returns the requested module URL and do some initialization:
	 * for ex: in case of jenkins tries to log in.
	 *
	 * @param moduleDescriptor
	 * @param personBean
	 * @return
	 */
	private static String getModuleUrl(ModuleDescriptor moduleDescriptor, TPersonBean personBean) {
		String moduleURL = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();

		moduleURL = moduleDescriptor.getCleanedUrl();

		if (moduleDescriptor.getId().equals(ModuleDescriptor.WEB_SVN_MODULE) || moduleDescriptor.getId().equals(ModuleDescriptor.VIEW_VC_MODULE)
				|| moduleDescriptor.getId().equals(ModuleDescriptor.VIEWGIT)) {

			String webSVNParams = "?sessionID=" + httpSession.getId() + "&baseURL=" + getBaseUrl(request);
			String moduleBaseUrl = moduleDescriptor.getCleanedUrl();

			if (!moduleBaseUrl.substring(moduleBaseUrl.length() - 1).equals("/")) {
				moduleBaseUrl += "/";
			}
			moduleURL = moduleBaseUrl + webSVNParams;
			if (moduleURL.startsWith("http://")) {
				moduleURL = moduleURL.replaceFirst("http://", "");
			}

			if (moduleURL.startsWith("https://")) {
				moduleURL = moduleURL.replaceFirst("https://", "");
			}
			moduleURL = request.getScheme() + "://" + moduleURL;
			return moduleURL;
		}

		if (moduleDescriptor.getId().equals("jenkins")) {

			Cookie cookie = ModuleBL.sendPOSTRequest(ModuleBL.getJenkinsUrl(moduleDescriptor, personBean.getUsername(), personBean.getPlainPwd()));
			moduleURL = moduleDescriptor.getCleanedUrl();
			if (cookie != null) {
				ServletActionContext.getResponse().addCookie(cookie);
			}
			return moduleURL;
		}

		if (moduleDescriptor.getId().equals(ModuleDescriptor.SONAR_MODUL)) {
			moduleURL = moduleDescriptor.getCleanedUrl();

			Cookie cookie = ModuleBL.sendPOSTRequest(ModuleBL.getSonarURL(moduleDescriptor, personBean.getUsername(), personBean.getPlainPwd()));
			moduleURL = moduleDescriptor.getCleanedUrl();
			if (cookie != null) {
				ServletActionContext.getResponse().addCookie(cookie);
			}
			return moduleURL;

		}
		return moduleURL;
	}
}
