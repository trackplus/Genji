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

package com.aurel.track.vc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.VersionControlDescriptor;

/**
 * 
 */
public class VersionControlJSON {
	public static String encodeVersionControlDescriptors(List vcDescriptors){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(vcDescriptors!=null){
			for (int i=0;i<vcDescriptors.size();i++) {
				VersionControlDescriptor vcDesc = (VersionControlDescriptor)vcDescriptors.get(i);
				sb.append("{");
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, vcDesc.getId());
				JSONUtility.appendStringValue(sb, "jsConfigClass", vcDesc.getJsConfigClass());
				List<VersionControlDescriptor.BrowserDescriptor> browsers= vcDesc.getBrowsers();
				JSONUtility.appendJSONValue(sb,"browsers",encodeBrowserDescriptor(browsers));
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, vcDesc.getName(), true);
				sb.append("}");
				if (i<vcDescriptors.size()-1) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	public static void appendVersionControlTO(StringBuilder sb,VersionControlTO vc,boolean last){
		if(vc!=null){
			JSONUtility.appendStringValue(sb,"vc.versionControlType",vc.getVersionControlType());
			JSONUtility.appendStringValue(sb,"vc.browserID",vc.getBrowserID());
			JSONUtility.appendStringValue(sb,"vc.baseURL",vc.getBaseURL());
			JSONUtility.appendStringValue(sb,"vc.changesetLink",vc.getChangesetLink());
			JSONUtility.appendStringValue(sb,"vc.addedLink",vc.getAddedLink());
			JSONUtility.appendStringValue(sb,"vc.modifiedLink",vc.getModifiedLink());
			JSONUtility.appendStringValue(sb,"vc.replacedLink",vc.getReplacedLink());
			JSONUtility.appendStringValue(sb,"vc.deletedLink",vc.getDeletedLink());
			JSONUtility.appendBooleanValue(sb, "vc.missing", vc.isMissing());
			Map<String,String> parameters=vc.getParameters();
			appendParameters(sb,"vcmap",parameters,last);
		}
	}
	public static String encodeVersionControlTO(VersionControlTO vc,List vcDescriptors,Integer projectID){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendJSONValue(sb,"vcPluginList", encodeVersionControlDescriptors(vcDescriptors));
		boolean useVC=false;
		if(vc!=null){
			appendVersionControlTO(sb,vc,false);
			useVC=vc.isUseVersionControl();
		}
		JSONUtility.appendIntegerValue(sb,"projectID",projectID);
		JSONUtility.appendBooleanValue(sb,"vc.useVersionControl",useVC,true);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Appends
	 * @param sb
	 * @param map
	 * @param last
	 */
	public static void appendParameters(StringBuilder sb, String prefix, Map<String, String> map,boolean last){
		if(map!=null && !map.isEmpty()){
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				JSONUtility.appendStringValue(sb,prefix+"."+key,map.get(key),!iterator.hasNext());
			}
			if(!last){
				sb.append(",");
			}
		}
	}

	public static String encodeBrowserDescriptor(List<VersionControlDescriptor.BrowserDescriptor> browsers){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(browsers!=null && !browsers.isEmpty()){
			sb.append("{");
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, "-1");
			JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, " ", true);
			sb.append("},");
			for (int i=0;i<browsers.size();i++) {
				VersionControlDescriptor.BrowserDescriptor browser = browsers.get(i);
				sb.append("{");
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.ID, browser.getId());
				JSONUtility.appendStringValue(sb, "changesetLink", browser.getChangesetLink());
				JSONUtility.appendStringValue(sb, "addedLink", browser.getAddedLink());
				JSONUtility.appendStringValue(sb, "modifiedLink", browser.getModifiedLink());
				JSONUtility.appendStringValue(sb, "replacedLink", browser.getReplacedLink());
				JSONUtility.appendStringValue(sb, "deletedLink", browser.getDeletedLink());
				JSONUtility.appendStringValue(sb, "baseURL", browser.getBaseURL());
				JSONUtility.appendStringValue(sb, JSONUtility.JSON_FIELDS.LABEL, browser.getName(), true);
				sb.append("}");
				if (i<browsers.size()-1) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	

}
