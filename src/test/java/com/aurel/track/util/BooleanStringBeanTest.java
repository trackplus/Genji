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

public class BooleanStringBeanTest {

	BooleanStringBean bsb;
	
	@Test
	public void testDefaultConstructor() {
		 bsb = new BooleanStringBean();
		assertNotNull(bsb);
	}
	
	@Test
	public void testConstructor() {
		 bsb = new BooleanStringBean("label", false);
		assertNotNull(bsb);
		assertTrue(bsb.label == "label" && bsb.value == false);
	}	
	
	@Test
	public void testLabel() {
		 bsb = new BooleanStringBean("label", false);
		bsb.setLabel("newlabel");
		assertEquals("newlabel", bsb.getLabel());
	}	
	
	@Test
	public void testEquals(){
		bsb = new BooleanStringBean("lable", true);
		BooleanStringBean bsb2 = new BooleanStringBean("lable",true);
		
		assertTrue(bsb.equals(bsb2));
	}
	
	@Test
	public void testHashCode(){
		bsb = new BooleanStringBean();
		
		assertEquals(0,bsb.hashCode());
	}
	
	@Test
	public void testHashCode2(){
		bsb = new BooleanStringBean("lable",true);
		
		assertEquals(1,bsb.hashCode());
	}
}
