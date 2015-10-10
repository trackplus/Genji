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

package com.aurel.track.report.dashboard;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBean;

/**
 *
 */
public class MyWatchJSON {
	public static String encodeReportBeanList(List<ReportBean> list, Integer personID){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<ReportBean> iterator = list.iterator(); iterator.hasNext();) {
				ReportBean reportBean = iterator.next();
				sb.append(encodeReportBean(reportBean, personID));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");

		return sb.toString();
	}
	public static String encodeReportBean(ReportBean reportBean, Integer personID){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		TWorkItemBean workItemBean=reportBean.getWorkItemBean();
		Integer stateID=workItemBean.getStateID();
		String state=reportBean.getShowValue(SystemFields.STATE);
		Integer projectID=workItemBean.getProjectID();
		String project=(String)reportBean.getShowValue(SystemFields.PROJECT);
		JSONUtility.appendIntegerValue(sb, "objectID", workItemBean.getObjectID());
		JSONUtility.appendStringValue(sb, "synopsis", workItemBean.getSynopsis());
		boolean unwatch = false;
		Set<Integer> informed = reportBean.getInformedList();
		if (informed!=null && informed.contains(personID)) {
			unwatch = true;
		} else {
			Set<Integer> consulted = reportBean.getConsultedList();
			if (consulted!=null && consulted.contains(personID)) {
				unwatch = true;
			}
		}
		JSONUtility.appendBooleanValue(sb, "unwatch", unwatch);
		JSONUtility.appendIntegerValue(sb,"stateID",stateID);
		JSONUtility.appendStringValue(sb, "state", state);
		JSONUtility.appendIntegerValue(sb,"projectID",projectID);
		JSONUtility.appendStringValue(sb, "project", project, true);
		sb.append("}");
		return  sb.toString();
	}

}
