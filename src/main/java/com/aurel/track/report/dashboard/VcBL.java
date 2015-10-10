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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.item.FileDiffTO;
import com.aurel.track.item.ItemVersionControlBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.emailHandling.Html2Text;
import com.aurel.track.vc.FileDiff;
import com.aurel.track.vc.RepositoryFileViewer;
import com.aurel.track.vc.Revision;
import com.aurel.track.vc.VersionControlMap;
import com.aurel.track.vc.VersionControlMap.ItemInfo;


public class VcBL {
	private static final Logger LOGGER = LogManager.getLogger(VcBL.class);
	public static List<VCActivityItem> getLastRevisions(Integer projectID,int commitsNo,Locale locale,boolean replaceComment){
		List<Revision> revisions=getRevisions(projectID);
		return  formatRevisions(revisions,commitsNo,locale,replaceComment);
	}
	public static List<VCActivityItem> getLastRevisions(List<Integer> selectedReleases,int commitsNo,Locale locale, boolean replaceComment) {
		List<Revision> revisions=getRevisions(selectedReleases);
		return  formatRevisions(revisions,commitsNo,locale,replaceComment);
	}
	public static List<VCActivityItem> getLastRevisionsByItems(List<Integer> workItemIDList,int commitsNo,Locale locale,boolean replaceComment){
		List<Revision> revisions=getRevisionsByItems(workItemIDList);
		return  formatRevisions(revisions,commitsNo,locale,replaceComment);
	}
	public static List<VCActivityItem> formatRevisions(List<Revision> revisions,int commitsNo,Locale locale,boolean replaceComment){
		Map<String, RepositoryFileViewer> mapViewer=VersionControlMap.getMapViewer();
		DateTimeUtils dtu=DateTimeUtils.getInstance();
		List<VCActivityItem> validRevisionsItems=new ArrayList<VCActivityItem>();
		if(!revisions.isEmpty()){
			List<VCActivityItem> allRevisionsItems=new ArrayList<VCActivityItem>();
			for(Revision r:revisions) {
				VCActivityItem vcActivityItem=new VCActivityItem(r);
				RepositoryFileViewer fileViewer=mapViewer.get(r.getRepository());
				List<FileDiff> fileDiffList=r.getChangedPaths();
				if(fileDiffList!=null){
					List<FileDiffTO> fileDiffTOList=new ArrayList<FileDiffTO>();
					FileDiffTO fileDiffTO=null;
					for(FileDiff fileDiff:fileDiffList){
						fileDiffTO=new FileDiffTO();
						fileDiffTO.setType(fileDiff.getType());
						fileDiffTO.setLocalizedType(ItemVersionControlBL.getFileDiffTypeHtml(fileDiff.getType(), locale));
						fileDiffTO.setPath(fileDiff.getPath());
						fileDiffTO.setLink(ItemVersionControlBL.getFileDiffLink(r, fileDiff, fileViewer));
						fileDiffTOList.add(fileDiffTO);
					}
					vcActivityItem.setChangedPaths(fileDiffTOList);
				}
				allRevisionsItems.add(vcActivityItem);
			}
			Collections.sort(allRevisionsItems);

			if(commitsNo==0||allRevisionsItems.size()<=commitsNo){
				validRevisionsItems=allRevisionsItems;
			}else{
				validRevisionsItems=new ArrayList<VCActivityItem>();
				for (int j = 0; j <commitsNo; j++) {
					validRevisionsItems.add(allRevisionsItems.get(j));
				}
			}
			VCActivityItem rev;
			for (int j = 0; j < validRevisionsItems.size(); j++) {
				rev=validRevisionsItems.get(j);
				if(replaceComment){
					rev.setRevisionComment(replaceIssueNumber(rev.getRevisionComment()));
				}
				RepositoryFileViewer fileViewer=mapViewer.get(rev.getRepository());
				rev.setRevisionNo(ItemVersionControlBL.createRevisionNo(rev.getRevisionNo(), fileViewer));
				rev.setRevisionDate(dtu.formatGUIDateTime(rev.getDate(), locale));
			}
		}
		return validRevisionsItems;
	}
	private static String replaceIssueNumber(String comment){
		String s;
		try {
			s = Html2Text.getCustomInstance().convert(comment);
		} catch (Exception e) {
			s=comment;
		}
		Set<Integer>  workItemdIDs=getWorkItemIDs(comment,VersionControlMap.prefixIssueNumber);
		String url=Constants.getHyperLink();
		if(workItemdIDs!=null){
			Iterator<Integer> it=workItemdIDs.iterator();
			while (it.hasNext()) {
				Integer key = it.next();
				s=s.replaceAll(VersionControlMap.prefixIssueNumber+key, "<a class=\"synopsis_blue\" href=\""+url+key+"\">"+key+"</a>");
			}
		}
		return s;
	}
	public static Set<Integer> getWorkItemIDs(String s,String prefixIssueNumber){
		Set<Integer> integers = new HashSet<Integer>();
		if(s==null||s.length()==0){
			return integers;
		}
		int index=s.indexOf(prefixIssueNumber);
		int sizePrefixIssueNumber=prefixIssueNumber.length();
		if (index==-1||index==s.length()-sizePrefixIssueNumber) {
			return integers;
		}
		int count;
		while(index!=-1&&index<s.length()){
			index=index+sizePrefixIssueNumber;
			count = index;
			while (count < s.length() && Character.isDigit(s.charAt(count))){
				count++;
			}
			if (count > index){
				try{
					integers.add(new Integer(s.substring(index,count)));
				}catch (Exception e) {
					// just ignore
				}
			}
			index=s.indexOf(prefixIssueNumber,count-sizePrefixIssueNumber+1);
		}
		return integers;
	}
	public static List<Revision> getRevisions(Integer projectID){
		LOGGER.debug("getRevision by project: "+projectID);
		if(projectID==null){
			return new ArrayList<Revision>();
		}
		List<Revision> validRevisions=new ArrayList<Revision>();
		Map<Integer,ItemInfo> mapItems=VersionControlMap.getMapItems();
		if(mapItems!=null){
			LOGGER.debug("VersionControlMap.mapItems.size=:"+mapItems.size());
		}
		Map<Integer,List<Revision>>  mapRev=VersionControlMap.getMap();
		if(mapRev!=null){
			LOGGER.debug("VersionControlMap.mapRev.size=:"+mapRev.size());
		}
		if(mapItems!=null){
			Iterator<Integer> it=mapItems.keySet().iterator();
			ItemInfo itemInfo;
			while(it.hasNext()){
				itemInfo=mapItems.get(it.next());
				if(projectID.equals(itemInfo.getProjectID())){
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
		}
		LOGGER.debug("project validRevisions.size()="+validRevisions.size());
		return validRevisions;
	}
	public static List<Revision> getRevisions(List<Integer> selectedReleases){
		LOGGER.debug("getRevision by releases: "+selectedReleases);
		if(selectedReleases==null||selectedReleases.isEmpty()){
			return new ArrayList<Revision>();
		}
		List<Revision> validRevisions=new ArrayList<Revision>();
		Map<Integer,ItemInfo> mapItems=VersionControlMap.getMapItems();
		LOGGER.debug("VersionControlMap.mapItems.size=:"+mapItems.size());
		Map<Integer,List<Revision>>  mapRev=VersionControlMap.getMap();
		LOGGER.debug("VersionControlMap.mapRev.size=:"+mapRev.size());
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
		}
		LOGGER.debug("release validRevisions.size()="+validRevisions.size());
		return validRevisions;
	}

	public static List<Revision> getRevisionsByItems(List<Integer> items){
		LOGGER.debug("getRevision by items: "+items);
		if(items==null||items.isEmpty()){
			return new ArrayList<Revision>();
		}
		List<Revision> validRevisions=new ArrayList<Revision>();
		Map<Integer,ItemInfo> mapItems=VersionControlMap.getMapItems();
		if (mapItems!=null) {
			LOGGER.debug("VersionControlMap.mapItems.size=:"+mapItems.size());
			Map<Integer,List<Revision>>  mapRev=VersionControlMap.getMap();
			LOGGER.debug("VersionControlMap.mapRev.size=:"+mapRev.size());
			if(mapItems!=null){
				Iterator<Integer> it=mapItems.keySet().iterator();
				ItemInfo itemInfo;
				while(it.hasNext()){
					itemInfo=mapItems.get(it.next());
					if(items.contains(itemInfo.getItemID())){
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
			}
		}
		LOGGER.debug("getRevisionsByItems validRevisions.size()="+validRevisions.size());
		return validRevisions;
	}

	public static String encodeActivityItemList(List<VCActivityItem> list){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<VCActivityItem> iterator = list.iterator(); iterator.hasNext();) {
				VCActivityItem activityItem = iterator.next();
				sb.append(encodeActivityItem(activityItem));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");

		return sb.toString();
	}
	public static String encodeActivityItem(VCActivityItem activityItem){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendDateValue(sb, "date", activityItem.getDate());
		JSONUtility.appendStringValue(sb, "revisionNo", activityItem.getRevisionNo());
		JSONUtility.appendStringValue(sb, "revisionDate", activityItem.getRevisionDate());
		JSONUtility.appendStringValue(sb, "revisionAuthor", activityItem.getRevisionAuthor());
		JSONUtility.appendStringValue(sb, "revisionComment", activityItem.getRevisionComment());
		JSONUtility.appendStringValue(sb, "repository", activityItem.getRepository(),true);
		sb.append("}");
		return  sb.toString();
	}
}
