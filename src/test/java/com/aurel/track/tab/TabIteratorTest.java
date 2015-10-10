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

package com.aurel.track.tab;

import static org.junit.Assert.*;

import org.junit.Test;

public class TabIteratorTest {

	TabIterator ti = new TabIterator();
	
	@Test
	public void testBaseURL() {
		ti.setBaseURL("baseURL");
		assertEquals("baseURL", ti.getBaseURL());
	}
	
	@Test
	public void testSetItem() {
	TabItem ti = new TabItem();
	
		this.ti.setItem(ti);
		assertEquals(ti,this.ti.getItems().get(0));
	}
	
	@Test
	public void testTabParameterName() {
		ti.setTabParameterName("tabParameterName");
		assertEquals("tabParameterName", ti.getTabParameterName());
	}

}
