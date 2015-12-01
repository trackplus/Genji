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
import static org.mockito.Mockito.*;

import javax.naming.NamingException;

import org.junit.Ignore;
import org.junit.Test;

import com.aurel.track.beans.TSiteBean;

public class LdapUtilTest {

	@Test
	public void testGetBaseURL() {
		assertEquals("http://www.trackplus.com:-1/", LdapUtil.getBaseURL("http://www.trackplus.com"));
	}
	
	@Test
	public void testGetBaseURL2() {
		assertEquals("http://www.trackplus.com:-1/", LdapUtil.getBaseURL("http://www.trackplus.com/de"));
	}
	
	@Test
	public void testGetBaseURL3() {
		assertEquals("null://null:-1/", LdapUtil.getBaseURL("www.trackplus.com"));
	}
	
	@Test
	public void testGetBaseURL4() {
		assertEquals("null://null:-1/", LdapUtil.getBaseURL("trackplus.com"));
	}
	
	@Test
	public void testGetBaseURL5() {
		assertEquals("null://null:-1/", LdapUtil.getBaseURL(""));
	}
	
	@Test
	@Ignore
	public void testAuthenticate() throws NamingException{
		TSiteBean sb= mock(TSiteBean.class);
		 assertTrue(LdapUtil.authenticate(sb,  "loginName", "ppassword"));
	}
	
	
	@Test
	@Ignore
	public void testIsOnLdapServer(){
		LdapUtil.isOnLdapServer("loginName");
	}

}
