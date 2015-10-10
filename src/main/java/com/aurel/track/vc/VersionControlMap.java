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

package com.aurel.track.vc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VersionControlMap {

	private static Map<Integer,List<Revision>>  map = null;
	private static Map<String,RepositoryFileViewer>  mapViewer = null;
	private static Map<Integer,ItemInfo> mapItems=null;
	public static String prefixIssueNumber;
	public synchronized static Map<String, RepositoryFileViewer> getMapViewer() {
		return mapViewer;
	}

	public synchronized static void setMapViewer(Map<String, RepositoryFileViewer> mapViewer) {
		VersionControlMap.mapViewer = mapViewer;
	}

	public synchronized static Map<Integer,List<Revision>> getMap() {
		return map;
	}

	public synchronized static void clearMap() {
		map = new HashMap<Integer, List<Revision>>();
	}

	public static synchronized void mergeMap(Map<Integer,List<Revision>>  m) {
        if (map == null) {
			map = new HashMap<Integer, List<Revision>>();
		}
		if (m == null) {
			return;
		}
		Iterator<Integer> it = m.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			List<Revision> value =  m.get(key);
			List<Revision> logs = map.get(key);
			if (logs == null) {
				map.put(key, value);
			} else {
				logs.addAll(value);
				map.put(key, logs);
			}
		}
	}
	public static class ItemInfo{
		private Integer itemID;
		private String title;
		private Integer releaseScheduledID;
		private Integer projectID;
		public Integer getItemID() {
			return itemID;
		}
		public void setItemID(Integer itemID) {
			this.itemID = itemID;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public Integer getReleaseScheduledID() {
			return releaseScheduledID;
		}
		public void setReleaseScheduledID(Integer releaseScheduledID) {
			this.releaseScheduledID = releaseScheduledID;
		}

		public Integer getProjectID() {
			return projectID;
		}

		public void setProjectID(Integer projectID) {
			this.projectID = projectID;
		}
	}
	public static Map<Integer, ItemInfo> getMapItems() {
		return mapItems;
	}

	public static void setMapItems(Map<Integer, ItemInfo> mapItems) {
		VersionControlMap.mapItems = mapItems;
	}
}
