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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.aurel.track.dbase.HandleHome;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.PluginUtils;
import com.opensymphony.xwork2.ActionSupport;

/**
 * An action to get JavaScript classes from plugins directories
 * 
 */
public class JavaScriptPathExtenderAction extends ActionSupport 
	implements ServletResponseAware {

	private static final Logger LOGGER = LogManager.getLogger(JavaScriptPathExtenderAction.class);
	
    private static final long serialVersionUID = 404L;
	private HttpServletResponse servletResponse;
	private String jsFile = null;
    private String pluginDir = null;

	
	public static String getDirs() {
		List<File> dirs = PluginUtils.getJavaScriptExtensionDirs();
		if (dirs == null || dirs.isEmpty()) {
			return "";
		}
		StringBuffer pathMappings = new StringBuffer();
		for (File dir: dirs) {
			String nameSpace = dir.getName();
			String path = dir.getAbsolutePath();
            //for ex. if a plugin name is 'reportPlugin' then: Ext.Loader.setPath('reportPlugin', 'loadJavaScript!load.action?file=reportPlugin/js');
            //that means all js files defined in plugin should be defined as Ext.define('reportPlugin.<ReportConfigJSName>',{...})

            pathMappings.append("Ext.Loader.setPath('"+nameSpace+"', 'loadJavaScript.action?pluginDir=" + nameSpace + "&file=');\n");
		}
		return pathMappings.toString();
	}

    /**'
     * Return the textual content of the js file row by row
     * By first creating/instantiating such a report configuration class the loader will search for <TRACKPLUS_HOME>/plugins/<pluginName>/<className>
     * pluginName is configured in getDirs and put as get parameter
     * className is automatically passed by Ext js loader mechanism to the path/URL mapping specified by Ext.Loader.setPath()
     * @return
     * @throws Exception
     */
	@Override
	public String execute() throws Exception {
        String tpHome = HandleHome.getTrackplus_Home();
        String jsDir = tpHome + File.separator+  HandleHome.PLUGINS_DIR + File.separator + pluginDir + File.separator + "js";
        int parameterIndex = jsFile.indexOf("?");
        if (parameterIndex!=-1) {
            //if caching is not disabled a timestamp parameter is added it should be removed from the file name because otherwise it will not be found on the disk
            jsFile = jsFile.substring(0, parameterIndex);
        }
		File file = new File(jsDir + jsFile);
		if (file != null && file.exists() && file.canRead()) {
			StringBuffer sb = new StringBuffer();
			BufferedReader br = null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(file));
				while ((sCurrentLine = br.readLine()) != null) {
					sb.append(sCurrentLine);
				}
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (br != null) {
                        br.close();
                    }
				} catch (IOException ex) {
					LOGGER.error(ExceptionUtils.getStackTrace(ex));
				}
			}
			JSONUtility.encodeJSON(servletResponse, sb.toString());
		}
		return null;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setFile(String pathToFile) {
		this.jsFile = pathToFile;
	}

    public void setPluginDir(String pluginDir) {
        this.pluginDir = pluginDir;
    }
}
