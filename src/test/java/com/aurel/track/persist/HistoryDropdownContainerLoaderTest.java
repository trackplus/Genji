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

package com.aurel.track.persist;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.item.history.FieldChangeBL;


@RunWith(PowerMockRunner.class)
//@PrepareForTest({FieldChangeBL.class, AttributeValueBL.class})
@PrepareForTest({FieldChangeBL.class})
//@PrepareForTest(AttributeValue.class)
@PowerMockIgnore("javax.management.*")

public class HistoryDropdownContainerLoaderTest {

	int[] workingIDs = { 0,1,2,2,3,4,5,6};
	
	@Before
	public void setUp(){
		List <TFieldChangeBean> fcbList = new ArrayList<TFieldChangeBean>();
		List<TAttributeValueBean> avbList = new ArrayList<TAttributeValueBean>();
		
		TFieldChangeBean fcb = mock(TFieldChangeBean.class);
		fcbList.add(fcb);
		
		TAttributeValueBean avb = mock(TAttributeValueBean.class);
		avbList.add(avb);
		
		
		PowerMockito.mockStatic(FieldChangeBL.class);
		PowerMockito.when(FieldChangeBL.loadHistoryCustomOptionFieldChanges(workingIDs)).thenReturn(fcbList);
		
		//PowerMockito.mockStatic(AttributeValueBL.class);
		//PowerMockito.when(AttributeValueBL.loadLuceneCustomOptionAttributeValues(workingIDs)).thenReturn(avbList);
	}
	
	@Test
	public void testLoadExporterDropDownContainer() {
		
		assertNotNull(HistoryDropdownContainerLoader.loadExporterDropDownContainer(workingIDs));
	}
	
	@Test
	public void testrepareHistoryCriteriaI(){
		
		assertNotNull(HistoryDropdownContainerLoader.prepareHistoryCriteria(workingIDs));
	}
	
	@Test
	public void testPrepareHistoryCustomOptionCriteria(){
		assertNotNull(HistoryDropdownContainerLoader.prepareHistoryCustomOptionCriteria(workingIDs,true));
		assertNotNull(HistoryDropdownContainerLoader.prepareHistoryCustomOptionCriteria(workingIDs,false));
		
	}
	
	@Test
	public void testPrepareHistorySystemOptionCriteria(){
		assertNotNull(HistoryDropdownContainerLoader.prepareHistorySystemOptionCriteria(workingIDs,true,"fieldName",0));
		assertNotNull(HistoryDropdownContainerLoader.prepareHistorySystemOptionCriteria(workingIDs,false,"fieldName2",1));
	
	}
}
