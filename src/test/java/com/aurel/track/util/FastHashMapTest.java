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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class FastHashMapTest {

	private FastHashMap fhm;
	
	@Before
	public void testFastHashMap() {
		fhm = new FastHashMap(5);
		assertNotNull(fhm);
	}
	
	@Test
	public void testClear() {
		fhm.clear();
		assertTrue(fhm.size() == 0);
	}
	
	@Test
	public void testClearFast() {
		fhm.fast = true;
		fhm.clear();
		assertTrue(fhm.size() == 0);
	}
	
	@Test 
	public void testCloneFast(){
		fhm.fast = true;
		assertEquals(fhm.clone(),fhm);
	}
	
	@Test 
	public void testEquals(){
		fhm.fast = true;
		assertTrue(fhm.equals(fhm));
	}
	
	@Test
	public void testContainsKey(){
		Object key = new Object();
		assertFalse(fhm.containsKey(key));
		
		fhm.setFast(true);
		//assertTrue(fhm.containsKey(key));
	}
	

}
