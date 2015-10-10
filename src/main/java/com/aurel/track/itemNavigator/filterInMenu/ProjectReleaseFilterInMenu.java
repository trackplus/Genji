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

package com.aurel.track.itemNavigator.filterInMenu;

import java.util.Locale;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Rendering the project/release filter in item navigator menu
 * @author Tamas
 *
 */
public class ProjectReleaseFilterInMenu extends FilterInMenuBase {

	public ProjectReleaseFilterInMenu(Integer queryID) {
		super(queryID);
	}

	/**
	 * Gets the label for the filter
	 * @param entityBean
	 * @param filterExpression
	 * @param locale
	 * @return
	 */
	public String getLabel(Object entityBean, String filterExpression, Locale locale) {
		if (queryID!=null) { 
			if (queryID>0) {
				TReleaseBean releaseBean = LookupContainer.getReleaseBean(queryID);
				if (releaseBean!=null) {
					TProjectBean projectBean = LookupContainer.getProjectBean(releaseBean.getProjectID());
					if (projectBean!=null) {
						return projectBean.getLabel()+" "+releaseBean.getLabel();
					}
				}
			} else {
				TProjectBean projectBean  =LookupContainer.getProjectBean(Integer.valueOf(-queryID.intValue()));
				if (projectBean!=null) {
					return projectBean.getLabel();
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the tooltip for the filter 
	 * @param entityBean
	 * @param filterExpression
	 * @param locale
	 * @return
	 */
	public String getTooltip(Object entityBean, String filterExpression, Locale locale) {
		if (queryID!=null) {
			if (queryID>0) {
				TReleaseBean releaseBean = LookupContainer.getReleaseBean(queryID);
				if (releaseBean!=null) {
					return LocalizeUtil.getParametrizedString("menu.findItems.release.tt", new Object[] {releaseBean.getLabel()}, locale);
				}
			} else {
				TProjectBean projectBean = LookupContainer.getProjectBean(Integer.valueOf(-queryID.intValue()));
				if(projectBean!=null) {
					return LocalizeUtil.getParametrizedString("menu.findItems.project.tt", new Object[] {projectBean.getLabel()}, locale);
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the iconCls for the filter
	 * @param entityBean
	 * @return
	 */
	public String getIconCls(Object entityBean) {
		if (queryID==null) {
			return null;
		}
		if (queryID.intValue()<0) {
			Integer projectID = Integer.valueOf(-queryID.intValue());
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				return ProjectBL.getProjectIcon(projectBean);
			}
		} else {
			TReleaseBean releaseBean = LookupContainer.getReleaseBean(queryID);
			if (releaseBean!=null) {
				return ReleaseBL.getReleaseIcon(releaseBean);
			}
		}
		return null;
	}
}
