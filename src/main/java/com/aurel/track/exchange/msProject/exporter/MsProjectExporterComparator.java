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

package com.aurel.track.exchange.msProject.exporter;

import java.util.Comparator;
import java.util.Map;

import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.util.SortOrderUtil;

/**
 * This class handles sorting of workItemBeans by OutlineNumber
 * At database level the string ordering does not works correctly 
 * because for ex. 1.11 would be before 1.2 
 * @author Tamas Ruff
 *
 */
public class MsProjectExporterComparator implements Comparator {	

	Map<Integer, TMSProjectTaskBean> msProjectTasksMap;
	Map<Integer, Integer> workItemIDToTaskUIDMap;
			
	public MsProjectExporterComparator(Map<Integer, TMSProjectTaskBean> msProjectTasksMap, 
			Map<Integer, Integer> workItemIDToTaskUIDMap) {
		super();
		this.msProjectTasksMap = msProjectTasksMap;
		this.workItemIDToTaskUIDMap = workItemIDToTaskUIDMap;
	}

	/**
	 * Compares two ReportBean according to the configured field and ascending order
	 */
	public int compare(Object arg1, Object arg2) {

		if (arg1==null && arg2==null) {
			return 0;
		}
		if (arg1==null) {
			return -1;
		}
		if (arg2==null) {
			return 1;
		}						
		Integer workItemID1 = (Integer) arg1;
		Integer workItemID2 = (Integer) arg2;		
		Integer UID1 = workItemIDToTaskUIDMap.get(workItemID1);
		Integer UID2 = workItemIDToTaskUIDMap.get(workItemID2);		
		if (UID1==null && UID2==null) {
			return 0;
		}
		if (UID1==null) {
			return 1;
		}
		if (UID2==null) {
			return -1;
		}
		TMSProjectTaskBean msProjectTaskBean1 = msProjectTasksMap.get(UID1);
		TMSProjectTaskBean msProjectTaskBean2 = msProjectTasksMap.get(UID2);
		if (msProjectTaskBean1==null && msProjectTaskBean2==null) {
			return 0;
		}
		if (msProjectTaskBean1==null) {
			return 1;
		}
		if (msProjectTaskBean2==null) {
			return -1;
		}
		String outlineNumber1 = msProjectTaskBean1.getOutlineNumber();
		String outlineNumber2 = msProjectTaskBean2.getOutlineNumber();
		if (outlineNumber1==null && outlineNumber2==null) {
			return 0;
		}
		if (outlineNumber1==null) {
			return 1;
		}
		if (outlineNumber2==null) {
			return -1;
		}
		String[] outlineLevels1 = outlineNumber1.split("\\.");
		String[] outlineLevels2 = outlineNumber2.split("\\.");
		
		if ((outlineLevels1==null || outlineLevels1.length==0) && (outlineLevels2==null || outlineLevels2.length==0)) {
			return 0;
		}
		if (outlineLevels1==null || outlineLevels1.length==0) {
			return 1;
		}
		if (outlineLevels2==null || outlineLevels2.length==0) {
			return -1;
		}
		int longerLength = outlineLevels1.length;
		if (outlineLevels2.length>longerLength) {
			longerLength = outlineLevels2.length;
		}		
		int compareResult;
		for (int i = 0; i < longerLength; i++) {
			Integer leveli1 = Integer.valueOf(0);
			Integer leveli2 =Integer.valueOf(0);
			if (outlineLevels1.length>i) {
				try {
					leveli1 = Integer.valueOf(outlineLevels1[i]);
				}catch (Exception e) {
				}
			}
			if (outlineLevels2.length>i) {
				try {
					leveli2 = Integer.valueOf(outlineLevels2[i]);	
				} catch (Exception e) {
				}				
			}
			compareResult = SortOrderUtil.compareValue(leveli1, leveli2, 1);
			if (compareResult!=0) {
				return compareResult;
			}
		}
		return 0;		
	}
	
	
}
