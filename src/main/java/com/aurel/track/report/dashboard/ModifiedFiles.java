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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemVersionControlBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.vc.FileDiff;
import com.aurel.track.vc.RepositoryFileViewer;
import com.aurel.track.vc.Revision;
import com.aurel.track.vc.VersionControlMap;
import com.aurel.track.vc.VersionControlMap.ItemInfo;

public class ModifiedFiles extends BasePluginDashboardView{
	
	
	//Configuration page constants
	private static interface CONFIGURATION_PARAMETERS {
		static String SELECTED_PROJECTS = "selectedProjects";
	}

	@Override
	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams){
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		StringBuilder sb=new StringBuilder();
		String labelProjectAndRelease;
		Locale locale = user.getLocale();
		Integer selectedProjectorRelease=null;
		if(projectID!=null){
			if(releaseID!=null){
				selectedProjectorRelease=releaseID;
			}else{
				selectedProjectorRelease=(projectID.intValue()*-1);
			}
		}else{
			String selectdProjectOrReleaseStr = parameters.get(CONFIGURATION_PARAMETERS.SELECTED_PROJECTS);
			if(selectdProjectOrReleaseStr!=null){
				try{
					selectedProjectorRelease=Integer.valueOf(selectdProjectOrReleaseStr);
				}catch (Exception e) {
				}
			}
		}
		List<FileItem> files;
		String selectedReleaseLabel;
		if(selectedProjectorRelease==null){
			selectedReleaseLabel="";
			files=new ArrayList<ModifiedFiles.FileItem>();
		}else{
			List<Integer> selectedReleases=new ArrayList<Integer>();
			selectedReleaseLabel="";
			if(selectedProjectorRelease.intValue()<0){
				//project
				Integer prjID=new Integer(selectedProjectorRelease.intValue()*-1);
				TProjectBean projectBean=LookupContainer.getProjectBean(prjID);
				if (projectBean!=null) {
					selectedReleaseLabel=projectBean.getLabel();
				}
				List<TReleaseBean> releases = null;
				releases = ReleaseBL.loadNotClosedByProject(prjID);
				if(releases!=null){
					for (int i = 0; i < releases.size(); i++) {
						selectedReleases.add(releases.get(i).getObjectID());
					}
				}
			}else{//release
				TReleaseBean releaseBean=LookupContainer.getReleaseBean(selectedProjectorRelease);
				if(releaseBean!=null){
					Integer prjID=releaseBean.getProjectID();
					TProjectBean projectBean=LookupContainer.getProjectBean(prjID);
					if (projectBean!=null) {
						selectedReleaseLabel=projectBean.getLabel()+" - "+releaseBean.getLabel();
					}
				}
				selectedReleases.add(selectedProjectorRelease);
			}
			files=getFiles(selectedReleases,locale);
		}
		JSONUtility.appendJSONValue(sb,"files",encodeFileItemList(files));
		JSONUtility.appendStringValue(sb, "selectedReleaseLabel", selectedReleaseLabel);

		return sb.toString();
	}
	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();
		JSONUtility.appendIntegerListAsArray(sb, ModifiedFiles.CONFIGURATION_PARAMETERS.SELECTED_PROJECTS,
				StringArrayParameterUtils.splitSelectionAsInteger(parameters.get(ModifiedFiles.CONFIGURATION_PARAMETERS.SELECTED_PROJECTS)));
		return sb.toString();
	}

	private List<FileItem> getFiles(List<Integer> selectedReleases,Locale locale){
		List<FileItem> result=new ArrayList<ModifiedFiles.FileItem>();
		if(selectedReleases==null||selectedReleases.isEmpty()){
			return result;
		}
		List<Revision> validRevisions=new ArrayList<Revision>();
		Map<Integer,ItemInfo> mapItems=VersionControlMap.getMapItems();
		Map<Integer,List<Revision>>  mapRev=VersionControlMap.getMap();
		Map<String, RepositoryFileViewer> mapViewer=VersionControlMap.getMapViewer();
		if(mapItems!=null){
			Iterator<Integer> it=mapItems.keySet().iterator();
			ItemInfo itemInfo;
			while(it.hasNext()){
				itemInfo=mapItems.get(it.next());
				if(selectedReleases.contains(itemInfo.getReleaseScheduledID())){
					List<Revision> revList=mapRev.get(itemInfo.getItemID());
					Revision r;
					for (int i = 0; i < revList.size(); i++) {
						r=revList.get(i);
						if(!validRevisions.contains(r)){
							validRevisions.add(r);
						}
					}
				}
			}
			Revision r;
			List<FileDiff> fileDiffList;
			FileDiff fd;
			for (int i = 0; i < validRevisions.size(); i++) {
				r=validRevisions.get(i);
				fileDiffList=r.getChangedPaths();
				RepositoryFileViewer fileViewer=mapViewer.get(r.getRepository());
				for (int j = 0; j < fileDiffList.size(); j++) {
					fd=fileDiffList.get(j);
					FileItem fi=new FileItem();
					fi.setType(ItemVersionControlBL.getFileDiffTypeHtml(fd.getType(), locale));
					fi.setName(fd.getPath());
					fi.setLink(ItemVersionControlBL.getFileDiffLink(r, fd, fileViewer) );
					result.add(fi);
				}
			}
		}
		
		return result;
	}
	public static String encodeFileItemList(List<FileItem> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<FileItem> iterator = list.iterator(); iterator.hasNext();) {
				FileItem fileItem = iterator.next();
				sb.append(encodeFileItem(fileItem));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");

		return sb.toString();
	}
	public static String encodeFileItem(FileItem fileItem){
		StringBuilder sb=new StringBuilder();
		sb.append("{");


		JSONUtility.appendStringValue(sb, "type", fileItem.getType());
		JSONUtility.appendStringValue(sb, "name", fileItem.getName());
		JSONUtility.appendStringValue(sb, "link", fileItem.getLink(),true);
		sb.append("}");
		return  sb.toString();
	}


	public static class FileItem{
		private String type;
		private String name;
		private String link;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
