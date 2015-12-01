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

package com.aurel.track.exchange.msProject.importer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TMSProjectTaskBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;

import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.Resource;

public class MsProjectImporterBLTest {
	TPersonBean pb  = new TPersonBean();
	Locale locale;
	MsProjectExchangeDataStoreBean eds = mock(MsProjectExchangeDataStoreBean.class);
	Map<Integer, TWorkItemBean> existingTrackPlusWorkItems = new HashMap<Integer,TWorkItemBean>();
	Map<Integer, TMSProjectTaskBean> existingTrackPlusTasks = new HashMap<Integer, TMSProjectTaskBean>();
	Map<Integer, WorkItemContext> workItemContextsMap = new HashMap<Integer,WorkItemContext>();
	Map<Integer, Integer> taskUIDToWorkItemIDMap = new HashMap<Integer,Integer>();
	Map<Integer, Boolean> overwriteBudgetMap = new HashMap<Integer,Boolean>();
	Map<Integer, String> projectSpecificItemIDsMap = new HashMap<Integer,String>();
	Map<Integer, Map<Integer, String>> budgetConflictValuesMap = new HashMap<Integer,Map<Integer, String>>();	
	
	SortedMap<Integer, List<ErrorData>> errorsMap;
	Map<Integer, TCostBean> workItemIdToCostBean = new HashMap<Integer, TCostBean>();
	
	int[]workItemIDs = {1,2,3};
	

	@Test
	public void testprepareResourceMatch() {
		MsProjectImporterBL.budgetConflictResolution(eds, 6.5, taskUIDToWorkItemIDMap, overwriteBudgetMap, budgetConflictValuesMap, projectSpecificItemIDsMap);
		assertTrue(true);
	}
	
	@Test 
	public void testdateConflictResolution(){
		MsProjectImporterBL.dateConflictResolution(eds, existingTrackPlusWorkItems, taskUIDToWorkItemIDMap, overwriteBudgetMap, overwriteBudgetMap, budgetConflictValuesMap, budgetConflictValuesMap, workItemContextsMap, projectSpecificItemIDsMap);
		assertTrue(true);
	}
	
	@Test
	public void testgetDeleteUndeleteTrackWorkItems(){
		MsProjectImporterBL.getDeleteUndeleteTrackWorkItems(eds, existingTrackPlusWorkItems, taskUIDToWorkItemIDMap, budgetConflictValuesMap, overwriteBudgetMap, budgetConflictValuesMap, overwriteBudgetMap, projectSpecificItemIDsMap);
		assertTrue(true);
	}
	
	@Test
	public void testImportTasks(){
		List<Resource> workResources = new ArrayList<Resource>();
		assertNotNull(MsProjectImporterBL.getResourceNameToResourceUIDMap(workResources));
	}

}
