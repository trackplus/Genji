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

import java.util.Date;

import org.junit.Test;

public class EqualUtilsTest {
	
	@Test
	public void testNotEqual() {
		assertTrue(EqualUtils.notEqual(1,2));
		assertFalse(EqualUtils.notEqual(1,1));
		
		assertFalse(EqualUtils.notEqual(0,0));
		assertTrue(EqualUtils.notEqual(0,1));
	}
	
	@Test 
	public void testEqual(){
		assertTrue(EqualUtils.equal(1, 1));
		
	}
	
	@Test 
	public void testNotEqualdouble(){
		assertFalse(EqualUtils.notEqual(1.1, 1.1));
		assertTrue(EqualUtils.notEqual(1.1, 1.2));
		
	}
	
	
	@Test
	public void testValueModified(){
	Object o1 = new Object();
	Object o2 = new Object();
	
	
		assertTrue(EqualUtils.valueModified(o1,o2));
		
	}
	
	@Test
	public void testEqualstring(){
		assertFalse(EqualUtils.equal("string1", "string2"));
		assertTrue(EqualUtils.equal("string1","string1"));
		
	}
	
	@Test
	public void testNotEqualstring(){
		assertTrue(EqualUtils.notEqual("string1", "string2"));
		assertFalse(EqualUtils.notEqual("string1","string1"));
	}
	
	@Test
	public void testEqualStrict(){
		assertFalse(EqualUtils.equalStrict("string1", "string2"));
		assertTrue(EqualUtils.equalStrict("string1","string1"));
		
		assertFalse(EqualUtils.equalStrict("String", ""));	
	}
	
	@Test
	public void testNotEqualdate(){
		Date date1 = new Date();
		Date date2 = new Date();
		
		assertFalse(EqualUtils.notEqual(date1,date2));
		
		date1.setTime(1220227200);
		date2.setTime(1224227200);
		
		assertTrue(EqualUtils.notEqual(date1,date2));
	}
	
	@Test
	public void testNotEqualboolean(){
		assertFalse(EqualUtils.notEqual(true, true));
		assertTrue(EqualUtils.notEqual(false,true));
		assertTrue(EqualUtils.notEqual(true, false));
		assertFalse(EqualUtils.notEqual(false,false));
	}
	
	@Test
	public void testNotEqualDateNeglectTime(){
		Date date1 = new Date();
		Date date2 = new Date();
		
		assertFalse(EqualUtils.notEqualDateNeglectTime(date1,date2));
		
		date2 = null;
		
		assertTrue(EqualUtils.notEqualDateNeglectTime(date1,date2));
		
		date1 = null;
		
		assertFalse(EqualUtils.notEqualDateNeglectTime(date1,date2));
		
		
	}
	

}
