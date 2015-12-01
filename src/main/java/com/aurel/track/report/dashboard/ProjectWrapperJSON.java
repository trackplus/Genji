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

package com.aurel.track.report.dashboard;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

/**
 * 
 */
public class ProjectWrapperJSON {
	public static String encodeProjectWrapperList(List<ProjectWrapper> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<ProjectWrapper> iterator = list.iterator(); iterator.hasNext();) {
				ProjectWrapper projectWrapper = iterator.next();
				sb.append(encodeProjectWrapper(projectWrapper));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");

		return sb.toString();
	}
	public static String encodeProjectWrapper(ProjectWrapper projectWrapper){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendJSONValue(sb,"list",encodeElementWrapperList(projectWrapper.getList()));
		JSONUtility.appendJSONValue(sb,"secondList",encodeElementWrapperList(projectWrapper.getSecondList()));
		JSONUtility.appendStringValue(sb, "label", projectWrapper.getLabel());
		JSONUtility.appendStringValue(sb,"symbol",projectWrapper.getSymbol());
		JSONUtility.appendIntegerValue(sb,"numberResolved",projectWrapper.getNumberResolved());
		JSONUtility.appendIntegerValue(sb,"numberOpen",projectWrapper.getNumberOpen());
		JSONUtility.appendIntegerValue(sb,"number",projectWrapper.getNumber());
		JSONUtility.appendIntegerValue(sb,"widthResolved",projectWrapper.getWidthResolved());
		JSONUtility.appendIntegerValue(sb,"projectID",projectWrapper.getProjectID());
		JSONUtility.appendIntegerValue(sb,"releaseID",projectWrapper.getReleaseID());
		JSONUtility.appendIntegerValue(sb,"groupByFieldType",projectWrapper.getGroupByFieldType());
		JSONUtility.appendIntegerValue(sb,"groupByFieldType2",projectWrapper.getGroupByFieldType2());
		JSONUtility.appendBooleanValue(sb,"openOnly",projectWrapper.isOpenOnly());
		JSONUtility.appendBooleanValue(sb,"areResolved",projectWrapper.getAreResolved());
		JSONUtility.appendIntegerValue(sb,"widthOpen",projectWrapper.getWidthOpen(),true);
		sb.append("}");
		return  sb.toString();
	}
	public static String encodeElementWrapperList(List<ElementWrapper> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<ElementWrapper> iterator = list.iterator(); iterator.hasNext();) {
				ElementWrapper elementWrapper = iterator.next();
				sb.append(encodeElementWrapper(elementWrapper));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");

		return sb.toString();
	}
	public static String encodeElementWrapper(ElementWrapper elementWrapper){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"label",elementWrapper.getLabel());
		JSONUtility.appendIntegerValue(sb,"number",elementWrapper.getNumber());
		JSONUtility.appendIntegerValue(sb,"width",elementWrapper.getWidth());
		JSONUtility.appendIntegerValue(sb,"groupByField",elementWrapper.getGroupByField());
		JSONUtility.appendStringValue(sb,"icon",elementWrapper.getIcon());
		JSONUtility.appendIntegerValue(sb,"percent",elementWrapper.getPercent());
		JSONUtility.appendIntegerValue(sb,"percentLate",elementWrapper.getPercentLate());
		JSONUtility.appendIntegerValue(sb,"widthLate",elementWrapper.getWidthLate(),true);
		sb.append("}");
		return sb.toString();
	}
}
