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

package com.aurel.track.beans;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.aurel.track.fieldType.constants.SystemFields;

public class TWorkItemBeanTest {
	TWorkItemBean wib;
	
	@Before
	public void setUp(){
		wib = new TWorkItemBean();
	}

	@Test
	public void testConstuctor() {
		assertNotNull(wib);
	}
	
	@Test
	public void testAccessLevelFlagFalse(){
		wib.setAccessLevelFlag(false);
		assertFalse(wib.isAccessLevelFlag());
	}
	
	@Test
	public void testAccessLevelFlagTrue(){
		wib.setAccessLevelFlag(true);
		assertTrue(wib.isAccessLevelFlag());
	}
	
	@Test
	public void testisArchivedOrDeletedFalse(){
		wib.setArchiveLevel(null);
		assertFalse(wib.isArchivedOrDeleted());
	}
	
	@Test
	public void testisArchivedOrDeleted(){
		wib.setArchiveLevel(0);
		assertFalse(wib.isArchivedOrDeleted());
	}
	
	@Test
	public void testIsArchievedNull(){
		wib.setAccessLevel(null);
		assertFalse(wib.isArchived());
	}
	
	@Test
	public void testIsArchieved(){
		wib.setAccessLevel(0);
		assertFalse(wib.isArchived());
	}
	
	@Test
	public void testisDeleted(){
		wib.setAccessLevel(null);
		assertFalse(wib.isDeleted());
	}
	
	@Test
	public void testisDeleted2(){
		wib.setAccessLevel(0);
		assertFalse(wib.isDeleted());
	}
	
	@Test
	public void testMilestoneFalse(){
		wib.setMilestone(false);
		assertFalse(wib.isMilestone());
	}
	
	@Test
	public void testMilestoneTrue(){
		wib.setMilestone(true);
		assertTrue(wib.isMilestone());
	}
	
	@Test
	public void testGetLabel(){
		assertEquals(null,wib.getLabel());
	}
	
	@Test
	public void testCustomAttributeValues(){
		wib.setCustomAttributeValues(null);
		assertNotNull(wib.getCustomAttributeValues());
	}
	
	@Test
	public void testgetAttribute(){
		assertNull(wib.getAttribute(2));
	}
	
	@Test
	public void testAttribute(){
		wib.setMilestone(true);
		assertEquals(true,wib.getAttribute(SystemFields.TASK_IS_MILESTONE));
	}
	
	@Test
	public void testgetCustomFieldIDSet(){
		Map<String,Object> customAttributeValues = new HashMap<String,Object>();
		Set<Integer> integerSet = new HashSet<Integer>();
		
		wib.setCustomAttributeValues(customAttributeValues);
		assertEquals(integerSet,wib.getCustomFieldIDSet());
	}
	
	@Test
	public void testgetPropertyName(){
		assertEquals("projectID",wib.getPropertyName(SystemFields.PROJECT));
	}
	
	@Test
	public void testgetPropertyName2(){
		assertEquals("isMilestone",wib.getPropertyName(SystemFields.TASK_IS_MILESTONE));
	}
	
	@Test
	public void testgetPropertyNameDefault(){
		Integer i = new Integer(SystemFields.CLASS);
		//assertEquals("customAttributeValues.",wib.getPropertyName(i));
		assertNotNull(wib.getPropertyName(i));
	}
	
	@Test
	public void testtoString(){
		assertNotNull("",wib.toString());
	}
	
	@Test
	public void testAttribute2(){
		Integer i = new Integer(SystemFields.PROJECT);
		assertNull(wib.getAttribute(i,3));
	}
	
	@Test
	public void testAttribute3(){
		Integer i = new Integer(SystemFields.TASK_IS_MILESTONE);
		assertEquals(false,wib.getAttribute(i,3));
	}
	
	@Test
	public void testAttribute4(){
		Integer i = new Integer(0);
		assertNull(wib.getAttribute(i,null));
	}
	
	@Test
	public void testAttribute5(){
		Integer i = new Integer(0);
		assertNull(wib.getAttribute(i,3));
	}
	
	@Test
	public void testsetAttribute(){
		Integer i = new Integer(0);
		wib.setAttribute(i, 0, wib);
		assertNull(wib.getAttribute(i));
	}
	
	@Test
	public void testsetAttribute2(){
		Integer i = new Integer(0);
		wib.setAttribute(i, i, wib);
		assertNull(wib.getAttribute(i));
	}
	
	@Test
	public void testsetAttributeComposite(){
		Integer fieldID = new Integer(0);
		Map map = new HashMap<>();
		wib.setAttributeComposite(fieldID, map);
		assertTrue(true);	//Smoke
		wib.removeAttribute(fieldID, fieldID);
		assertTrue(true);
	}
	
	@Test
	public void testcopyShallow(){
		assertNotNull(wib.copyShallow());
	}
	
	@Test
	public void testcopyDeep(){
		assertNotNull(wib.copyDeep());
	}
	
	@Test
	public void testDeepCopyFalse(){
		wib.setDeepCopy(false);
		assertFalse(wib.isDeepCopy());
	}
	
	@Test
	public void testDeepCopy(){
		wib.setDeepCopy(true);
		assertTrue(wib.isDeepCopy());
	}
	
	@Test
	public void testsetCopyAttachmentsFalse(){
		wib.setCopyAttachments(false);
		assertFalse(wib.isCopyAttachments());
	}
	
	@Test
	public void testsetCopyAttachments(){
		wib.setCopyAttachments(true);
		assertTrue(wib.isCopyAttachments());
	}
	
	@Test
	public void testcalculateBottomUpDueDateOnPlan(){
		assertEquals(0,wib.calculateBottomUpDueDateOnPlan(3, 4));
	}
	
	@Test
	public void testcalculateTopDownDueDateOnPlan(){
		assertEquals(0,wib.calculateTopDownDueDateOnPlan(3, 4));
	}

	@Test
	public void testisDateConflict(){
		assertTrue(wib.isDateConflict(TWorkItemBean.DUE_FLAG.OVERDUE));
	}
	
	@Test
	public void testisDateConflict2(){
		assertFalse(wib.isDateConflict(TWorkItemBean.DUE_FLAG.ON_PLAN));
	}
	
	@Test
	public void testisDateConflictDefault(){
		assertFalse(wib.isDateConflict(6));
	}
	
	@Test
	public void testcalculateTargetDateConflict(){
		assertFalse(wib.calculateTargetDateConflict(true, true));
	}
	
	@Test
	public void testisOverdue(){
		assertFalse(wib.isOverdue());
	}
	
	@Test
	public void testEditableFalse(){
		wib.setEditable(false);
		assertFalse(wib.isEditable());
	}
	
	@Test
	public void testEditable(){
		wib.setEditable(true);
		assertTrue(wib.isEditable());
	}
	
	@Test
	public void testserializeBean(){
		wib.setObjectID(2);
		assertNotNull(wib.serializeBean());
	}
	
	@Test
	public void testdeserializeBean(){
		Map<String,String> map = new HashMap<>();
		assertNotNull(wib.deserializeBean(map));
	}
	
	@Test
	public void testconsiderAsSame(){
		assertFalse(wib.considerAsSame(wib, null));
	}
	
	@Test
	public void testsaveBean(){
		assertNull(wib.saveBean(wib, null));
	}
}
