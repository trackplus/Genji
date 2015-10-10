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


package com.aurel.track.item;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.json.JSONUtility;

/**
 */
public class IssuePickerJSON {

	public static String encodeIssues(List<TWorkItemBean> issues,boolean useProjectSpecificID,Map<Integer,TProjectBean> projectsMap){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(issues!=null){
			TWorkItemBean workItemBean;
			String projectSpecificID=null;
			String projectPrefix=null;
			for (Iterator<TWorkItemBean> iterator = issues.iterator(); iterator.hasNext();) {
				workItemBean = iterator.next();
				sb.append("{");
				String id=null;
				if(useProjectSpecificID){
					projectSpecificID="";
					TProjectBean projectBean=projectsMap.get(workItemBean.getProjectID());
					projectPrefix=projectBean.getPrefix();
					projectSpecificID = ItemBL.getSimpleOrPorjectSpecificItemNo(true, projectPrefix, workItemBean.getIDNumber());
					id=projectSpecificID;
				}else{
					id=workItemBean.getObjectID().toString();
				}
				JSONUtility.appendStringValue(sb, "id", id);
				JSONUtility.appendStringValue(sb, "title", workItemBean.getSynopsis());
				JSONUtility.appendIntegerValue(sb, "objectID", workItemBean.getObjectID(),true);
				sb.append("}");
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
