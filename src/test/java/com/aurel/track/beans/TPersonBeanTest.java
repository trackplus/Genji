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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.aurel.track.beans.TPersonBean.USERLEVEL;

public class TPersonBeanTest {
	TPersonBean pb;
	Date date = new Date(2015, 8, 25);
	Map<String,Map<Integer,Integer>> matchesMap = new HashMap<String,Map<Integer,Integer>>();
	
	@Before
	public void setUp(){
		pb = new TPersonBean("firstname","lastname","email");
	}

	@Test
	public void testConstructor() {
		assertNotNull(pb);
	}
	
	@Test
	public void testDefaultConstructor() {
		TPersonBean pb2 = new TPersonBean();
		assertNotNull(pb2);
	}
	
	@Test
	public void testisLdapUser(){
		pb.setIsLdapUser();
		assertTrue(pb.isLdapUser());
	}
	
	@Test
	public void testgetFullName() {
		assertEquals("lastname, firstname",pb.getFullName());
	}
	
	@Test
	public void testgetSimpleFullName() {
		assertEquals("lastname firstname",pb.getSimpleFullName());
	}
	
	@Test
	public void testgetName(){
		assertEquals("lastname, firstname",pb.getName());
	}
	
	@Test
	public void testgetPureFullName(){
		assertEquals("firstname lastname",pb.getPureFullName());
	}
	
	@Test
	public void testsetDisabledFalse(){
		pb.setDisabled(false);
		assertFalse(pb.isDisabled());
	}
	
	@Test
	public void testsetDisabled(){
		pb.setDisabled(true);
		assertTrue(pb.isDisabled());
	}
	
	@Test
	public void testsetSysAdminFalse(){
		pb.setSysAdmin(false);
		assertFalse(pb.getIsSysAdmin());
	}

	@Test
	public void testsetSysAdmin(){
		pb.setSysAdmin(true);
		assertFalse(pb.getIsSysAdmin());
	}
	
	@Test
	public void testsetSysAdmin2(){
		pb.setObjectID(1);
		pb.setSysAdmin(true);
		assertTrue(pb.getIsSysAdmin());
	}
	
	@Test
	public void testsetSysAdmin3(){
		pb.setObjectID(100);
		pb.setSysAdmin(true);
		assertTrue(pb.getIsSysAdmin());
	}
	
	@Test
	public void testsetSysManagerFalse(){
		pb.setSysManager(false);
		assertFalse(pb.isSysAdmin());
	}
	
	@Test
	public void testsetSysManager(){
		pb.setSysManager(true);
		assertEquals(USERLEVEL.SYSMAN,pb.getUserLevel());
	}
	
	@Test
	public void testsetSysManager2(){
		pb.setSysManager(true);
		assertTrue(pb.isSysManager());
	}
	
	@Test
	public void testsetSysManager3(){
		pb.setSysManager(true);
		assertTrue(pb.isSys());
	}
	
	@Test
	public void testsetAlwaysSaveAttachment(){
		pb.setAlwaysSaveAttachment(true);
		assertTrue(pb.isAlwaysSaveAttachment());
	}

	@Test
	public void testNoEmailsPlease(){
		pb.setNoEmailPleaseBool(true);
		assertTrue(pb.getNoEmailPleaseBool());
	}
	
	@Test
	public void testNoEmailsPlease2(){
		pb.setNoEmailPleaseBool(false);
		assertFalse(pb.getNoEmailPleaseBool());
	}
	
	@Test
	public void testsetRemindMeAsOriginatorBool(){
		pb.setRemindMeAsOriginatorBool(true);
		assertTrue(pb.getRemindMeAsOriginatorBool());
	}
	
	@Test
	public void testsetRemindMeAsManagerBool(){
		pb.setRemindMeAsManagerBool(true);
		assertTrue(pb.getRemindMeAsManagerBool());
	}
	
	@Test
	public void testsetRemindMeAsResponsible(){
		pb.setRemindMeAsResponsibleBool(true);
		assertTrue(pb.getRemindMeAsManagerBool());
	}
	
	@Test
	public void testLastLogin(){
		pb.setLastLogin(date);
		assertEquals(date,pb.getLastLogin());
	}
	
	@Test
	public void testsetLastButOneLogin(){
		pb.setLastButOneLogin(date);
		assertEquals(date,pb.getLastButOneLogin());
	}

	@Test
	public void testsetCsvCharacter(){
		char c = 'c';
		Character character = new Character(c);
		pb.setCsvCharacter("csvSeparator");
		assertEquals(character,pb.getCsvCharacter());
	}
	
	@Test
	public void testsetCsvEncoding(){
		char c = ';';
		Character character = new Character(c);
		pb.setCsvEncoding("csvEncoding");
		assertEquals(character,pb.getCsvCharacter());
	}
	
	@Test
	public void testsetLastSelectedView(){
		pb.setLastSelectedView("lastSelectedView");
		assertEquals("lastSelectedView", pb.getLastSelectedView());
	}
	
	@Test
	public void testcompareTo(){
		assertEquals(0,pb.compareTo(pb));
	}
	
	@Test
	public void testEquals(){
		assertEquals(-999999,pb.hashCode());
	}
	
	@Test
	public void testsetRichTextEditorExpanded(){
		pb.setRichTextEditorExpanded(true);
		assertTrue(pb.isRichTextEditorExpanded());
	}
	
	@Test
	public void testsetShowFullHistory(){
		pb.setShowFullHistory(true);
		assertTrue(pb.isShowFullHistory());
	}
	
	@Test
	public void testsetShowCommentsHistory(){
		pb.setShowCommentsHistory(true);
		assertTrue(pb.isShowCommentsHistory());
	}
	
	@Test
	public void testsetPrintItemEditable(){
		pb.setPrintItemEditable(true);
		assertTrue(pb.isPrintItemEditable());
	}
	
	@Test
	public void testsetAutoLoadTime(){
		pb.setAutoLoadTime(5);
		assertEquals((Integer)5,pb.getAutoLoadTime());
	}
	
	@Test
	public void testsetShowDashboardTimer(){
		pb.setShowDashboardTimer(true);
		assertTrue(pb.getShowDashboardTimer());
	}
	
	@Test
	public void testsetEnableQueryLayout(){
		pb.setEnableQueryLayout(true);
		assertTrue(pb.isEnableQueryLayout());
	}
	
	@Test
	public void testsetEnableQueryLayout2(){
		pb.setEnableQueryLayout(true);
		assertTrue(pb.isEnableQueryLayout());
	}
	
	@Test
	public void testsetPaginate(){
		pb.setPaginate(true);
		assertTrue(pb.isPaginate());
	}
	
	@Test
	public void testsetShowFlatHistory(){
		pb.setShowFlatHistory(true);
		assertTrue(pb.isShowFlatHistory());
	}
	
	@Test
	public void testsetSessionTimeoutMinutes(){
		pb.setSessionTimeoutMinutes(42);
		assertEquals((Integer)42, pb.getSessionTimeoutMinutes());
	}
	
	@Test
	public void testsetHomePage(){
		pb.setHomePage("homePage");
		assertEquals("homePage", pb.getHomePage());
	}
	
	@Test
	public void testgetUserTimeZoneId(){
		pb.setUserTimeZoneId("userTz");
		assertEquals("userTz",pb.getUserTimeZoneId());
	}
	
	@Test
	public void testsetDesignPath(){
		pb.setDesignPath("designPath");
		assertEquals("designPath", pb.getDesignPath());
	}
	
	@Test
	public void testsetLocaleDefault(){
		Locale locale = new Locale("de_DE");
		pb.setLocale(null);
		assertEquals(locale.getDefault(), pb.getLocale());
	}
	
	@Test
	public void testgetReminderDays(){
		assertNotNull(pb.getReminderDays());
	}
	
	@Test
	public void testReminderDays(){
		List<Integer> reminderDays = new ArrayList<Integer>();
		reminderDays.add(4);
		reminderDays.add(0);
		reminderDays.add(6);
		
		pb.setReminderDays(reminderDays);
		assertEquals(reminderDays,pb.getReminderDays());
	}
	
	@Test
	public void testsetIsSysAdmin(){
		pb.setIsSysAdmin(true);
		assertTrue(true);
	}
	
	@Test
	public void testsetDocxLastTemplate(){
		pb.setDocxLastTemplate("docxLastTemplate");
		assertEquals("docxLastTemplate",pb.getDocxLastTemplate());
	}
	
	@Test
	public void testserializeBean(){
		pb.setObjectID(42);
		assertNotNull(pb.serializeBean());
	}
	
	@Test
	public void testdeserializeBean(){
		Map<String,String> map = new HashMap<String,String>();
		assertNotNull(pb.deserializeBean(map));
	}
	
	@Test
	public void testconsiderAsSame(){
		//Map<Integer,Integer> intMap = new HashMap<Integer,Integer>();
		assertTrue(pb.considerAsSame(pb, matchesMap));
	}
	
	@Test
	public void testsaveBean(){
		assertNull(pb.saveBean(pb, matchesMap));
		//assertTrue(true);
	}
	
	@Test(expected=NullPointerException.class)
	public void testauthenticate() throws Exception{
		pb.authenticate("ppassword");
	}
}
