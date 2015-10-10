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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.VersionControlDescriptor;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.vc.FileDiff;
import com.aurel.track.vc.RepositoryFileViewer;
import com.aurel.track.vc.Revision;
import com.aurel.track.vc.VersionControlMap;

/**
 */
public class ItemVersionControlBL {
	public static List<Revision> getVersionControlRevisions(Integer workItemID){
		Map<Integer,List<Revision>> revisionsMap;
		synchronized(VersionControlMap.class){
			revisionsMap= VersionControlMap.getMap();
		}

		List<Revision> revisions;
		if(revisionsMap==null){
			revisions=new ArrayList<Revision>();
		}else{
			revisions= revisionsMap.get(workItemID);
		}
		return revisions;
	}
	public static List<RevisionTO> getVersionControlRevisionsTO (Integer workItemID, Map<String,RepositoryFileViewer>  mapViewer,Locale locale) {
		List<Revision> revisions=getVersionControlRevisions(workItemID);
		List<RevisionTO> revisionTOs=null;
		if(revisions!=null){
			revisionTOs=new ArrayList<RevisionTO>();
			RevisionTO revisionTO=new RevisionTO();
			FileDiffTO fileDiffTO;
			for(Revision r:revisions){
				RepositoryFileViewer fileViewer=mapViewer.get(r.getRepository());
				revisionTO.setRepository(r.getRepository());
				revisionTO.setRevisionAuthor(r.getRevisionAuthor());
				revisionTO.setRevisionComment(r.getRevisionComment());
				revisionTO.setRevisionDate(r.getRevisionDate());
				revisionTO.setRevisionNo(r.getRevisionNo());
				revisionTO.setRevisionURL(createRevisionNo(r.getRevisionNo(),fileViewer));
				List<FileDiff> fileDiffList=r.getChangedPaths();
				if(fileDiffList!=null){
					List<FileDiffTO> fileDiffTOList=new ArrayList<FileDiffTO>();
					for(FileDiff fileDiff:fileDiffList){
						fileDiffTO=new FileDiffTO();
						fileDiffTO.setType(fileDiff.getType());
						fileDiffTO.setLocalizedType(getFileDiffTypeHtml(fileDiff.getType(), locale));
						fileDiffTO.setPath(fileDiff.getPath());
						fileDiffTO.setLink(getFileDiffLink(r, fileDiff, fileViewer));
						fileDiffTOList.add(fileDiffTO);
					}
					revisionTO.setChangedPaths(fileDiffTOList);
				}
				revisionTOs.add(revisionTO);
			}
		}
		return revisionTOs;
	}
	public static String encodeJSON_versionControl (List<Revision> revisions, Map<String,RepositoryFileViewer>  mapViewer,Locale locale) {
		StringBuilder sb=new StringBuilder();

		sb.append("[");
		if(revisions!=null){
			Revision rev;
			for (int i = 0; i < revisions.size(); i++) {
				rev=revisions.get(i);
				if(i>0){
					sb.append(",");
				}
				sb.append("{");
				RepositoryFileViewer fileViewer=mapViewer.get(rev.getRepository());
				JSONUtility.appendStringValue(sb, "repository", rev.getRepository());
				JSONUtility.appendStringValue(sb,"revision",rev.getRevisionNo());
				JSONUtility.appendStringValue(sb,"revisionURL",createRevisionNo(rev.getRevisionNo(),fileViewer));
				JSONUtility.appendStringValue(sb,"date",rev.getRevisionDate());
				JSONUtility.appendStringValue(sb,"user",rev.getRevisionAuthor());
				JSONUtility.appendStringValue(sb,"message",createRevisionMessage(rev,fileViewer,locale),true);
				sb.append("}");
			}
		}
		sb.append("]");

		return sb.toString();
	}

	public static String createRevisionNo(String rev,RepositoryFileViewer fileViewer){
		if(fileViewer==null||fileViewer.getBrowser()==null){
			return rev;
		}
		String s=null;
		String pattern=null;
		if(!fileViewer.getBrowser().getId().equals("integratedwebsvn") && !fileViewer.getBrowser().getId().equals("gitView")) {
			if(fileViewer.getChangesetLink()!=null&&fileViewer.getChangesetLink().trim().length()>0){
				pattern=fileViewer.getChangesetLink().trim();
			}else{
				VersionControlDescriptor.BrowserDescriptor browser=fileViewer.getBrowser();
				if(browser!=null&&fileViewer.getBaseURL()!=null&&fileViewer.getBaseURL().trim().length()>0){
					pattern=fileViewer.getBaseURL().trim()+browser.getChangesetLink();
				}

			}
			if(pattern!=null){
				pattern=ItemDetailBL.replacePattern(pattern, null, rev);
				//window.location.href='externalAction.action?moduleID='+ m.id;
				s="<a href=\""+pattern+"\" class=\"synopsis_blue\" target=\"filedef\">"+rev+"</a>";
			}else{
				s=rev;
			}
		}else {
			String link = fileViewer.getChangesetLink();
			if(link != null && link.length() > 0) {
				if(fileViewer.getBrowser().getId().equals("integratedwebsvn")) {
					String repName = link.substring(link.indexOf("repname") + 8, link.indexOf("&"));
					s = "<a href=\"externalAction!getWebSVNModuleJSONForVersionControlActivity.action?vcRepName=" + GeneralUtils.encodeURL(repName) + "&vcRevisionNo=" + rev + "\" class=\"synopsis_blue\" target=\"_self\">"+rev+"</a>";
				}else if(fileViewer.getBrowser().getId().equals("gitView")) {
					String path = link.substring(link.indexOf("p=") + 8, link.indexOf(";"));
					s = "<a href=\"externalAction!getViewGitModuleJSONForVersionControlActivity.action?" + "vcPath=" + GeneralUtils.encodeURL(path) + "&vcRevisionNo=" + rev + "\" class=\"synopsis_blue\" target=\"_self\">"+rev+"</a>";
				}
			}
		}
		return s;
	}
	private static String createRevisionMessage(Revision rev,RepositoryFileViewer fileViewer,Locale locale){
		StringBuffer sb=new StringBuffer();
		if(rev.getRevisionComment()!=null){
			sb.append(rev.getRevisionComment());
		}
		List<FileDiff> changedPaths=rev.getChangedPaths();
		sb.append("<br/>");
		sb.append("<table>");
		for (int i = 0; i < changedPaths.size(); i++) {
			sb.append("<tr>");
			FileDiff fileDiff=changedPaths.get(i);
			sb.append("<td>");
			sb.append("<b>").append(getFileDiffTypeHtml(fileDiff.getType(), locale)).append("</b>&nbsp;");
			sb.append("</td>");
			sb.append("<td>");
			sb.append(getFileDiffPath(rev, fileDiff, fileViewer));
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	public static String getFileDiffTypeHtml(char type,Locale locale){
		String key="";
		switch (type) {
			case FileDiff.TYPE_ADDED:
				key="item.tabs.versionControl.commitType.add";
				break;
			case FileDiff.TYPE_DELETED:
				key="item.tabs.versionControl.commitType.deleted";
				break;
			case FileDiff.TYPE_MODIFIED:
				key="item.tabs.versionControl.commitType.modified";
				break;
			case FileDiff.TYPE_REPLACED:
				key="item.tabs.versionControl.commitType.replaced";
				break;
			default:
				key="item.tabs.versionControl.commitType.modified";
		}
		String result= LocalizeUtil.getLocalizedTextFromApplicationResources(key, locale);
		if(result==null||result.trim().length()==0){
			result=key;
		}
		return result;
	}

	@SuppressWarnings("null") // the compiler doesn't get it here, we are too smart
	public static String getFileDiffLink(Revision rev,FileDiff fileDif,RepositoryFileViewer fileViewer){
		String pattern=null;
		VersionControlDescriptor.BrowserDescriptor browser=fileViewer.getBrowser();
		boolean hasBaseUrl=(browser!=null&&fileViewer.getBaseURL()!=null&&fileViewer.getBaseURL().trim().length()>0);
		switch (fileDif.getType()) {
			case FileDiff.TYPE_ADDED:
				if(fileViewer.getAddedLink()!=null&&fileViewer.getAddedLink().trim().length()>0){
					pattern=fileViewer.getAddedLink();
				}else{
					if(hasBaseUrl){
						pattern=fileViewer.getBaseURL()+browser.getAddedLink();
					}
				}
				break;
			case FileDiff.TYPE_DELETED:
				if(fileViewer.getDeletedLink()!=null&&fileViewer.getDeletedLink().trim().length()>0){
					pattern=fileViewer.getDeletedLink();
				}else{
					if(hasBaseUrl){
						pattern=fileViewer.getBaseURL()+browser.getDeletedLink();
					}
				}
				break;
			case FileDiff.TYPE_MODIFIED:
				if(fileViewer.getModifiedLink()!=null&&fileViewer.getModifiedLink().trim().length()>0){
					pattern=fileViewer.getModifiedLink();
				}else{
					if(hasBaseUrl){
						pattern=fileViewer.getBaseURL()+browser.getModifiedLink();
					}
				}
				break;
			case FileDiff.TYPE_REPLACED:
				if(fileViewer.getReplacedLink()!=null&&fileViewer.getReplacedLink().trim().length()>0){
					pattern=fileViewer.getReplacedLink();
				}else{
					if(hasBaseUrl){
						pattern=fileViewer.getBaseURL()+browser.getReplacedLink();
					}
				}
				break;
			default:
				if(fileViewer.getModifiedLink()!=null&&fileViewer.getModifiedLink().trim().length()>0){
					pattern=fileViewer.getModifiedLink();
				}else{
					if(hasBaseUrl){
						pattern=fileViewer.getBaseURL()+browser.getModifiedLink();
					}
				}
				break;
		}
		if(pattern!=null){
			pattern=fileDif.replacePattern(pattern, rev.getRevisionNo());
		}
		if(fileViewer!=null&&fileViewer.getBrowser()!=null){
			if(fileViewer.getBrowser().getId().equals("integratedwebsvn")) {
				if(pattern.contains("?")) {
					String arguments = pattern.substring(pattern.indexOf("?"), pattern.length());
					String urlToReturn = "externalAction!getWebSVNModuleJSONForVersionControlActivity.action" + arguments;
					urlToReturn = urlToReturn.replace("repname=", "vcRepName=");
					urlToReturn = urlToReturn.replace("path=", "vcPath=");
					urlToReturn = urlToReturn.replace("rev=", "vcRevisionNo=");
					return urlToReturn;
				}
			}

			if(fileViewer.getBrowser().getId().equals("gitView")) {
				if(pattern.contains("?")) {
					String arguments = pattern.substring(pattern.indexOf("?"), pattern.length());
					String urlToReturn = "externalAction!getViewGitModuleJSONForVersionControlActivity.action" + arguments;
					urlToReturn = urlToReturn.replace("repname=", "vcRepName=");
					urlToReturn = urlToReturn.replace("p=", "vcPath=");
					urlToReturn = urlToReturn.replace("hb=", "vcRevisionNo=");
					urlToReturn = urlToReturn.replace(";", "&");
					return urlToReturn;
				}
			}
		}
		return pattern;
	}
	public static String getFileDiffPath(Revision rev,FileDiff fileDif,RepositoryFileViewer fileViewer){
		String s=null;
		String link=getFileDiffLink(rev, fileDif, fileViewer);
		if(link!=null){
			s="<a href=\""+link+"\" target=\"filedef\">"+fileDif.getPath()+"</a>";
		}else{
			s=fileDif.getPath();
		}
		return s;
	}
}
