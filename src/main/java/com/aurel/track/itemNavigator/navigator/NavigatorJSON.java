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

package com.aurel.track.itemNavigator.navigator;

import java.util.Iterator;
import java.util.List;

import com.aurel.track.json.JSONUtility;

/**
 *
 */
public class NavigatorJSON {
	public static String encode(Navigator nav){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "name", nav.getName());
		JSONUtility.appendStringValue(sb, "text", nav.getName());
		JSONUtility.appendJSONValue(sb,"queryView",encodeView(nav.getQueryView()));
		JSONUtility.appendIntegerValue(sb, "objectID", nav.getObjectID());
		JSONUtility.appendJSONValue(sb,"subFilterView",encodeView(nav.getSubFilterView()), true);
		
		sb.append("}");
		return sb.toString();
	}
	public static String encodeView( View view){
		if(view==null){
			return null;
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"objectID",view.getObjectID());
		JSONUtility.appendStringValue(sb,"text",view.getName());
		JSONUtility.appendStringValue(sb,"iconCls",view.getIconCls());
		JSONUtility.appendJSONValue(sb,"children",encodeSectionList(view.getSections()),true);
		sb.append("}");
		return sb.toString();
	}

	private static String encodeSectionList(List<Section> list) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<Section> iterator = list.iterator(); iterator.hasNext();) {
				Section section = iterator.next();
				sb.append(encodeSection(section));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static String encodeSection(Section section) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"text",section.getName());
		boolean leaf=(section.getMenu()==null||section.getMenu().isEmpty());
		JSONUtility.appendBooleanValue(sb,"leaf",leaf);
		JSONUtility.appendStringValue(sb,"icon",section.getIcon());
		JSONUtility.appendStringValue(sb,"cls","treeItem-level-0");
		JSONUtility.appendStringValue(sb,"iconCls",section.getIconCls());
		if(section.getMenu()!=null){
			JSONUtility.appendJSONValue(sb,"children",encodeMenuList(section.getMenu()));
		}
		JSONUtility.appendStringValue(sb,"id",section.getId(),true);
		sb.append("}");
		return sb.toString();
	}

	public static String encodeMenuList(List<MenuItem> list) {
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(list!=null){
			for (Iterator<MenuItem> iterator = list.iterator(); iterator.hasNext();) {
				MenuItem menuItem = iterator.next();
				sb.append(encodeMenuItem(menuItem));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String encodeMenuItem(MenuItem menuItem) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb,"id",menuItem.getId());
		JSONUtility.appendStringValue(sb,"text",menuItem.getName());
		JSONUtility.appendStringValue(sb,"nodeType",menuItem.getType());
		JSONUtility.appendStringValue(sb,"filterViewID",menuItem.getFilterViewID());
		JSONUtility.appendBooleanValue(sb, "maySaveFilterLayout", menuItem.isMaySaveFilterLayout());
		JSONUtility.appendStringValue(sb,"icon",menuItem.getIcon());
		JSONUtility.appendStringValue(sb,"iconCls",menuItem.getIconCls());
		String cls="treeItem-level-"+menuItem.getLevel();
		if(menuItem.getCls()!=null){
			cls=cls+" "+menuItem.getCls();
		}
		JSONUtility.appendStringValue(sb,"cls",cls);
		JSONUtility.appendStringValue(sb,"url",menuItem.getUrl());
		JSONUtility.appendStringValue(sb,"dropHandlerCls",menuItem.getDropHandlerCls());
		JSONUtility.appendBooleanValue(sb,"useAJAX",menuItem.isUseAJAX());
		boolean leaf=true;
		if(menuItem.isLazyChildren()){
			leaf=false;
		}else{
			leaf=(menuItem.getChildren()==null||menuItem.getChildren().isEmpty());
		}
		JSONUtility.appendBooleanValue(sb,"leaf",leaf);
		JSONUtility.appendBooleanValue(sb,"canDrop",menuItem.isAllowDrop());
		JSONUtility.appendBooleanValue(sb,"verifyAllowDropAJAX",menuItem.isVerifyAllowDropAJAX());
		JSONUtility.appendBooleanValue(sb,"useFilter",menuItem.isUseFilter());
		if(menuItem.getChildren()!=null){
			JSONUtility.appendBooleanValue(sb,"expanded",menuItem.isExpanded());
			JSONUtility.appendJSONValue(sb,"children",encodeMenuList(menuItem.getChildren()));
		}
		JSONUtility.appendIntegerValue(sb,"projectID",menuItem.getProjectID());
		JSONUtility.appendIntegerValue(sb,"objectID",menuItem.getObjectID(),true);
		sb.append("}");
		return sb.toString();
	}

}
