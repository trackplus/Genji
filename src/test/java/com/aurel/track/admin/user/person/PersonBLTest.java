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

package com.aurel.track.admin.user.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.aurel.track.beans.TPersonBean;

public class PersonBLTest {
	TPersonBean bean1 = new TPersonBean();

	@Test
	public void testIsLdapPersonSame() {

		TPersonBean bean2 = bean1;
		
		PersonBL.isLdapPersonSame(bean1, bean2);
	}
	
	@Test 
	public void testUpdateLdapPerson(){
		PersonBL.updateLdapPerson(bean1, "firstName", "lastName", "email", "phone");
		assertEquals("firstName", bean1.getFirstName());
		assertEquals("lastName", bean1.getLastName());
		assertEquals("email", bean1.getEmail());
		assertEquals("phone", bean1.getPhone());
	}
	
	@Test
	public void testCreateLdapPerson(){
		TPersonBean personBean2 = PersonBL.createLdapPerson("loginName", "firstName", "lastName", "email", "phone");
		assertNotNull(personBean2);
		assertEquals("loginName", personBean2.getLoginName());
	}
	
	@Test
	public void testcreateMsProjectImportNewUser(){
		TPersonBean personBean = PersonBL.createMsProjectImportNewUser("username", "firstName", "lastName", "email");
		assertNotNull(personBean);
		assertEquals("username", personBean.getUsername());
		assertEquals("firstName", personBean.getFirstName());
		assertEquals("lastName", personBean.getLastName());
		assertEquals("email", personBean.getEmail());

	}
	
	@Test
	public void testSave(){
		TPersonBean personBean = PersonBL.createMsProjectImportNewUser("username", "firstName", "lastName", "email");
		PersonBL.save(personBean);
		assertTrue(true);	//Smoke
	}
	
	@Test
	public void testLoadByLabelNull(){
		
		assertNull(PersonBL.loadByLabel(null));
	}
	
	@Test
	public void testLoadByLabel(){
	
		List<Integer> personsList = new ArrayList<>();
		PersonBL.setUserLevelPersons(personsList, 4);
		assertTrue(true);	// Smoke
	}
	
	@Test
	public void testsetLicensedFeatures(){
		TPersonBean personBean = PersonBL.createMsProjectImportNewUser("username", "firstName", "lastName", "email");
		PersonBL.setLicensedFeatures(personBean);
		assertTrue(true);	
	}
	

	
}
