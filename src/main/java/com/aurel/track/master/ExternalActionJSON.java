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

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.vc.VersionControlTO;
import com.aurel.track.vc.bl.VersionControlBL;
import java.util.List;
import java.util.Locale;

public class ExternalActionJSON {

	public static String encodeProjectsForWebSVN(List<TProjectBean> projectsBeanList, Locale locale, String repoType) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, "success", true);
		sb.append("\"projectsDetials\": [");
		for (int i = 0; i < projectsBeanList.size(); i++) {
			String projectName = projectsBeanList.get(i).getLabel();
			VersionControlTO versionControlTO = VersionControlBL.loadVersionControl(projectsBeanList.get(i).getObjectID());
			encodeProjectVersionControlConfig(versionControlTO, sb, projectName, repoType);
		}
		if(sb.toString().endsWith(",")) {
			sb = new StringBuilder(sb.toString().substring(0, sb.length() - 1));
		}
		sb.append("],");
		String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("browseProject.err.noProjectToBrowse", locale);
		JSONUtility.appendStringValue(sb, "errorMessage", errorMessage, true);
		sb.append("}");
		return sb.toString();
	}

	private static void encodeProjectVersionControlConfig(VersionControlTO versionControlTO, StringBuilder sb, String projectName, String repoType) {
		if(versionControlTO.isUseVersionControl() && versionControlTO.getVersionControlType().equals(repoType)) {
			String protocol = versionControlTO.getParameters().get("accessMethod");
			String serverName = versionControlTO.getParameters().get("serverName");
			String repositoryPath = versionControlTO.getParameters().get("repositoryPath");

			if(repositoryPath != null && versionControlTO.isUseVersionControl()) {
				if((repoType == "git") || (serverName != null && repoType == "svn")) {
					String userName = versionControlTO.getParameters().get("userName");
					String pwd = versionControlTO.getParameters().get("password");
					if(userName == null) {
						userName = "";
					}
					if(pwd == null) {
						pwd = "";
					}
					sb.append("{");
					JSONUtility.appendStringValue(sb, "projectName", projectName);
					JSONUtility.appendStringValue(sb, "protocol", protocol);
					JSONUtility.appendStringValue(sb, "serverName", serverName);
					JSONUtility.appendStringValue(sb, "repositoryPath", repositoryPath);
					JSONUtility.appendStringValue(sb, "userName", userName);
					JSONUtility.appendStringValue(sb, "password", pwd, true);
					sb.append("},");
				}
			}
		}
	}

}
