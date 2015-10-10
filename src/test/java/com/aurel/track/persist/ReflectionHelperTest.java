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

package com.aurel.track.persist;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ReflectionHelperTest {
	
	Class[]peerClasses = {ReflectionHelper.class};
	String[] fields = {"String1","String2","String3"};
	int  oldint = 0;
	Integer oldOID = null;	//Initialisation
	Integer newOID = null;	//Initialisation

	@Test
	public void testHasDependentData1() {

		
		assertFalse(ReflectionHelper.hasDependentData(peerClasses, fields, oldOID));
	}
	
	@Test 
	public void testReplace(){
		ReflectionHelper.replace(peerClasses, fields, oldOID, newOID);
		assertTrue(true);
	}
	
	@Test 
	public void testHasDependentDataEmpty() {
		Class[]peerClasses = {ReflectionHelper.class};
		String[] fields = {"String1","String2","String3"};
		int oldint = 0;
		List<Integer> oldOID = new ArrayList<Integer>();
		
		assertFalse(ReflectionHelper.hasDependentData(peerClasses, fields, oldOID));
	}
	
	@Test
	public void testSetToNull(){
		ReflectionHelper.setToNull(peerClasses, fields, oldOID);
		assertTrue(true);
	}
	
	@Test
	public void testDelete(){
		ReflectionHelper.delete(peerClasses, fields, oldOID);
		assertTrue(true);
	}
	


}
