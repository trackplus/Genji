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

package com.aurel.track.admin.customize.lists;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.aurel.track.admin.customize.lists.ListBL.RESOURCE_TYPES;
import com.aurel.track.beans.TPersonBean;

public class ListBLTest {

	@Test
	public void testgetFirstLevelNodes() {
		ListBL.getChildList(2, 3);
		assertTrue(true);	//Smoke
		}
	
	@Test
	public void testLoad(){
		ListOptionIDTokens loit = new ListOptionIDTokens();
		TPersonBean pb = new TPersonBean();
		
		assertNotNull(ListBL.getCustomListOptionGridRows(loit, pb, null));
	}

	@Test
	public void testgetSystemListGridRows(){
		ListBL.getSystemListGridRows(null);
		assertTrue(true);
	}
	
	@Test
	public void testgetLocalizedLabel(){
		assertEquals("",ListBL.getLocalizedLabel(3, null));
	}
	
	@Test
	@Ignore
	public void testgetLocalizedLabel2(){
		Integer listID = new Integer(RESOURCE_TYPES.CUSTOM_ENTRY);
		assertEquals("",ListBL.getLocalizedLabel(listID, null));
	}
	
	@Test
	@Ignore
	public void testgetLocalizedLabel3(){
		Integer listID = new Integer(RESOURCE_TYPES.SEVERITY);
		assertEquals("",ListBL.getLocalizedLabel(listID, null));
	}
	
	@Test
	public void testgetProjectBranchNodeID(){
		assertNotNull(ListBL.getProjectBranchNodeID(3));
		//assertTrue(true);
	}
	
	@Test
	public void testgetPublicCustomListGridRows(){
		assertNotNull(ListBL.getPublicCustomListGridRows(null, null));
	}
	
	@Test
	public void testgetListOrOptionBaseInstance(){
		assertNotNull(ListBL.getListOrOptionBaseInstance(null, 4, false, true, true));
	}
}
