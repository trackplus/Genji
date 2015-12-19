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

package com.aurel.track.plugin;

import com.aurel.track.prop.ApplicationBean;

public class ModuleDescriptor extends BasePluginDescriptor {
	private String iconCls;
	private String url;
	private String target;
	private boolean useHeader=false;
	private boolean haveIE9Css=false;
	public static final String WIKI_MODULE = "wiki";
	public static final String WEB_SVN_MODULE = "websvn";
	public static final String VIEW_VC_MODULE = "viewvc";
	public static final String SONAR_MODUL = "sonar";
	public static final String TRACKPLUS_MODULE = "trackplus";
	public static final String VIEWGIT = "viewgit";


	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	//This method returns the URL set in config xml.
	public String getUrl() {
		return url;
	}
	//This method returns the URL set in config xml and if the url contains ${SERVER} word will replace with server url.
	public String getCleanedUrl() {
		String urlToReturn = this.url.replace("${SERVER}", ApplicationBean.getInstance().getSiteBean().getServerURL());
		if(urlToReturn == null) {
			urlToReturn = this.url;
		}
		return urlToReturn;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isUseHeader() {
		return useHeader;
	}

	public void setUseHeader(boolean useHeader) {
		this.useHeader = useHeader;
	}

	public boolean isHaveIE9Css() {
		return haveIE9Css;
	}

	public void setHaveIE9Css(boolean haveIE9Css) {
		this.haveIE9Css = haveIE9Css;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
