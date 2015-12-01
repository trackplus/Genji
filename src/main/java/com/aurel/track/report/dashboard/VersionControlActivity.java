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

import java.util.*;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.StringArrayParameterUtils;

/**
 * 
 * This shows the last X commits for a configurable list of projects or a project.
 * 
 * @author Adi
 *
 */
public class VersionControlActivity extends BasePluginDashboardView {

	@Override
	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();

		String commitsStr=parameters.get("commits");
		int commitsNo=10;
		if(commitsStr!=null){
			try{
				commitsNo=Integer.parseInt(commitsStr);
			}catch (Exception e) {
			}
		}
		if(commitsNo<=0){
			commitsNo=10;
		}
		Locale locale=user.getLocale();
		if(projectID!=null){
			VCActivityProject vcProject=new VCActivityProject();
			TProjectBean projectBean=LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				vcProject.setObjectID(projectBean.getObjectID());
				vcProject.setLabel(projectBean.getLabel());
			}
			List<VCActivityItem> activityItems;
			if(releaseID!=null){
				List<Integer> releases=new ArrayList<Integer>();
				releases.add(releaseID);
				activityItems=VcBL.getLastRevisions(releases, commitsNo, locale,true);
			}else{
				activityItems=VcBL.getLastRevisions(projectID, commitsNo, locale,true);
			}
			vcProject.setRevisions(activityItems);
			Collections.sort(activityItems);
			JSONUtility.appendJSONValue(sb,"activityItems",VcBL.encodeActivityItemList(activityItems));
		}else{
			List<String> selectedProjects;
			selectedProjects = StringArrayParameterUtils.splitSelection(parameters.get("selectedProjects"));
			List<VCActivityItem> allActivityItems=new ArrayList<VCActivityItem>();
			List<VCActivityProject> projects=new ArrayList<VCActivityProject>();
			if(selectedProjects!=null){
				for (int i = 0; i < selectedProjects.size(); i++) {
					Integer projID=Integer.valueOf(selectedProjects.get(i));
					VCActivityProject vcProject=new VCActivityProject();
					if(projID.intValue()<0){
						projID=new Integer(projID.intValue()*-1);
						TProjectBean projectBean=ProjectBL.loadByPrimaryKey(projID);
						vcProject.setObjectID(projectBean.getObjectID());
						vcProject.setLabel(projectBean.getLabel());
						List<VCActivityItem> activityItems=VcBL.getLastRevisions(projID, commitsNo, locale,true);
						vcProject.setRevisions(activityItems);
						if(!activityItems.isEmpty()){
							projects.add(vcProject);
						}
						allActivityItems.addAll(activityItems);
					}else{
						List<Integer> releases=new ArrayList<Integer>();
						releases.add(projID);
						List<VCActivityItem> activityItems=VcBL.getLastRevisions(releases, commitsNo, locale,true);
						vcProject.setObjectID(projID);
						TReleaseBean releaseBean=LookupContainer.getReleaseBean(projID);
						TProjectBean projectBean=ProjectBL.loadByPrimaryKey(releaseBean.getProjectID());
						vcProject.setLabel(projectBean.getLabel()+" - "+releaseBean.getLabel());
						vcProject.setRevisions(activityItems);
						if(!activityItems.isEmpty()){
							projects.add(vcProject);
						}
						allActivityItems.addAll(activityItems);
					}
				}

			}
			Collections.sort(allActivityItems);
			JSONUtility.appendJSONValue(sb,"activityItems",VcBL.encodeActivityItemList(allActivityItems));

		}

		return sb.toString();
	}
	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendStringList(sb,"selectedProjects", StringArrayParameterUtils.splitSelection(parameters.get("selectedProjects")));
		JSONUtility.appendStringValue(sb,"commits",parameters.get("commits"));
		return sb.toString();
	}

}
