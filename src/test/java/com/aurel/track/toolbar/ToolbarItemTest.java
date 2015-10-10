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

package com.aurel.track.toolbar;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.aurel.track.tree.TreeNodeBaseTO;

public class ToolbarItemTest {

	ToolbarItem tti;
	
	@Before
	public void setUp(){
		tti = new ToolbarItem();
	}
	
	@Test
	public void testCondition() {
		tti.setCondition(true);
		assertTrue(tti.isCondition());
	}
	
	@Test
	public void testCssClass() {
		tti.setCssClass("cssClass");
		assertEquals("cssClass", tti.getCssClass());
	}
	
	@Test
	public void testImageInactive() {
		tti.setImageInactive("imageInactive");
		assertEquals("imageInactive", tti.getImageInactive());
	}

	@Test
	public void testLabelKey() {
		tti.setLabelKey("labelKey");
		assertEquals("labelKey", tti.getLabelKey());
	}
	
	@Test
	public void testTooltipKey() {
		tti.setTooltipKey("tooltipKey");
		assertEquals("tooltipKey", tti.getTooltipKey());
	}
	
	@Test
	public void testID() {
		tti.setId(42);
		assertEquals(42, tti.getId());
	}
	
	@Test
	public void testJsonData() {
		tti.setJsonData("jsonData");
		assertEquals("jsonData", tti.getJsonData());
	}
	
	@Test
	public void testMore() {
		tti.setMore(true);
		assertEquals(true, tti.isMore());
	}
}
