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

import org.junit.Ignore;
import org.junit.Test;

public class SortedOptionBeanComparatorTest {
	
	SortedOptionBeanComparator sobc;
	SortedOptionBean sob1 = new SortedOptionBean();
	SortedOptionBean sob2 = new SortedOptionBean();	
	

	@Test
	@Ignore
	public void testCompare1() {
		sobc = new SortedOptionBeanComparator(false,false);	//sortOrder =false -> compare lable
		sob1.setLabel("label");
		sob2.setLabel("label");
		
		assertTrue(""+sobc.compare(sob1, sob2), sobc.compare(sob1, sob2) == 1);
	}
	
	@Test
	@Ignore
	public void testCompare2() {
		sobc = new SortedOptionBeanComparator(false,false);	//sortOrder =false -> compare lable
		sob1.setLabel("label");
		sob2.setLabel("label2");
		
		assertTrue(sobc.compare(sob1, sob2) == 0);
	}
	
	@Test 
	@Ignore
	public void testCompare3(){
		sobc = new SortedOptionBeanComparator(true,true);	//sortOrder = -1 
		sob1.setSortOrder(true);
		sob2.setSortOrder(true);
		
		
		assertTrue(sobc.compare(sob1, sob2) == 1);
	}
	
	@Test
	@Ignore
	public void testCompare4(){
		sobc = new SortedOptionBeanComparator(true,true);	//sortOrder = -1 
		sob1.setSortOrder(true);
		sob2.setSortOrder(false);
		
		
		assertTrue(sobc.compare(sob1, sob2) == 0);
	}

}
