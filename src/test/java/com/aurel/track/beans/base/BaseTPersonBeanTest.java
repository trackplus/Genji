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

package com.aurel.track.beans.base;

import static org.junit.Assert.*;

import org.junit.Test;

import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.persist.TDepartment;

public class BaseTPersonBeanTest {

	BaseTPersonBean pb;
	
	@Test
	public void testConstructor() {
		pb = new BaseTPersonBean() {};
		pb.setObjectID(42);
		assertEquals((Integer)42, pb.getObjectID());
		assertTrue(pb.isModified());
	}
	
	@Test 
	public void testTDepartmentBean() {
		pb = new BaseTPersonBean(){};
		pb.setTDepartmentBean(null);
		assertNull(pb.getTDepartmentBean());
	}
	
	@Test 
	public void testTDepartmentBean2() {
		pb = new BaseTPersonBean(){};
		TDepartmentBean db = new TDepartmentBean();
		pb.setTDepartmentBean(db);
		assertNotNull(pb.getTDepartmentBean());
	}
}
