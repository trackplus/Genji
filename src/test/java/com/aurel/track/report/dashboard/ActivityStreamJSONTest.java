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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aurel.track.item.FileDiffTO;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.item.history.HistoryEntry;
import com.aurel.track.persist.HistoryDropdownContainerLoader;
import com.aurel.track.report.dashboard.ActivityStream.ActivityStreamItem;

public class ActivityStreamJSONTest {

	ActivityStreamItem asi = new ActivityStreamItem();
	
	@Test
	public void testEncodeActivityItem() {
		
		ActivityStreamJSON.encodeActivityItem(asi);
		
		assertTrue(true);
	}
	
	@Test
	public void testEncodeActivityItemList() {
		List<ActivityStreamItem> list = new ArrayList<ActivityStreamItem>();	// = new List<ActivityStreamItem>();
		list.add(asi);
		ActivityStreamJSON.encodeActivityItemList(list);
		
		assertTrue(true);
	}
	
	@Test
	public void testEncodeActivityHTMLData(){
		List<FlatHistoryBean> fhbList= new ArrayList<FlatHistoryBean>();
		FlatHistoryBean fhb = new FlatHistoryBean();
		
		//fhbList.add(fhb);
		
		ActivityStreamJSON.encodeActivityHTMLData(fhbList);
		assertTrue(true);
	}
	
	@Test
	public void testEncodeAvatarCheckSum(){
		Map<Integer, String> personIDToAvatarCheckSum = new HashMap<Integer,String>();
		
		
		assertEquals("{}",ActivityStreamJSON.encodeAvatarCheckSum(personIDToAvatarCheckSum));
	}
	
	@Test
	public void testappendHistoryEntries(){
		List<HistoryEntry> historyList = new ArrayList<HistoryEntry>();
		ActivityStreamJSON.appendHistoryEntries(historyList);
		assertTrue(true);
	}
	
	@Test
	public void testappendHistoryEntriesLong(){
		List<HistoryEntry> historyList = new ArrayList<HistoryEntry>();
		ActivityStreamJSON.appendHistoryLongEntries(historyList);
		assertTrue(true);
	}
	
	@Test
	public void testAppendChangedPaths(){
		List<FileDiffTO> list = new ArrayList<FileDiffTO>();
		ActivityStreamJSON.appendChangedPaths(list);
		assertTrue(true);
	}
	

}
