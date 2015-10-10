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

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class SortedOptionBeanTest {
	
	SortedOptionBean sob = new SortedOptionBean();
	//Comparable<sortOrder> so = new 

	@Test
	public void testLabel() {
		sob.setLabel("label");
		assertEquals("label", sob.getLabel());
	}
	
	@Test
	@Ignore
	public void testSortOrder() {
		//sob.setSortOrder(sortOrder);
		//assertEquals("label", sob.getLabel());
	}
	
	@Test
	public void test() {
		Object value = new String("value");
		sob.setValue(value);
		assertEquals("value", sob.getValue());
	}

}
