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

package com.aurel.track.fieldType.runtime.base;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.aurel.track.beans.TWorkItemBean;


public class FieldsManagerRTTest {
	
	TWorkItemBean wib;
	List<TWorkItemBean> workItemBeansList = new ArrayList<TWorkItemBean>();

	
	
	@Before
	public void setUp(){
		wib = new TWorkItemBean();
		workItemBeansList.add(wib);
	}

	@Test
	public void testgetWorkItemContext() {
		assertNotNull(FieldsManagerRT.getWorkItemContext(wib, null, null));
	}
	
	@Test
	public void testgetExchangeContextForExisting(){
		assertNotNull(FieldsManagerRT.getExchangeContextForExisting(wib, null, null, null));
	}
	
	@Test
	public void testgetExchangeContextForNew(){
		assertNotNull(FieldsManagerRT.getExchangeContextForNew(2,null,null));
	}
	
	@Test
	public void testcreateImportContext(){
		
		FieldsManagerRT.createImportContext(workItemBeansList, null, null, null, null, null, null, null);
	}
	
	@Test
	public void testcreateImportContext2(){
		assertNotNull(FieldsManagerRT.createImportContext(wib, null, null, null, null, null));
	}
	
	@Test
	@Ignore
	public void testeditOneField(){
		assertNotNull(FieldsManagerRT.editOneField(2, 3, null, 5));
	}
	
	
	@Test
	@Ignore
	public void testviewOrEdit(){
		assertNotNull(FieldsManagerRT.viewOrEdit(2, 3, true, null, false));
	}
	
	@Test
	@Ignore
	public void testchangeStatus(){
		assertNotNull(FieldsManagerRT.changeStatus(2, 3, null));
	}
	
	@Test
	@Ignore
	public void testloadWorkItem(){
		assertNotNull(FieldsManagerRT.loadWorkItem(2, 3, null, 5));
	}
}
