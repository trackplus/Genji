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

package com.aurel.track.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegerStringBeanTest {
	IntegerStringBean isb;

	@Test
	public void testEquals() {
		isb = new IntegerStringBean("lable", 2);
		assertTrue(isb.equals(isb));
	}
	
	@Test
	public void testCompareTo() {
		isb = new IntegerStringBean("lable", 2);
		IntegerStringBean isb2 = new IntegerStringBean();
		
		assertNotNull(isb.compareTo(isb2));
	}
	
	@Test
	public void testCompareTo2() {
		isb = new IntegerStringBean("lable", 2);
		IntegerStringBean isb2 = new IntegerStringBean("lable",2);
		
		assertEquals(0,isb.compareTo(isb2));
	}
	
	

}
