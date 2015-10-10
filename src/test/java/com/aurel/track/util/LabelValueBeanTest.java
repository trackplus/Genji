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

package com.aurel.track.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class LabelValueBeanTest {

	LabelValueBean lvb,lvb2;
	
	@Before
	public void setUp(){
		 lvb = new LabelValueBean("lable","value");
		 lvb2 = new LabelValueBean("lable2","value2");
	}
	@Test
	public void testConstructor() {
		assertNotNull(lvb);
	
	}
	
	@Test
	public void testEquals() {
		assertTrue(lvb.equals(lvb));
	
	}
	
	@Test
	public void testCompareTo() {
		assertEquals(0,lvb.compareTo(lvb));
		assertNotEquals(0, lvb.compareTo(lvb2));
		
		
	
	}
}