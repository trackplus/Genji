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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.aurel.track.prop.LoginBL;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoginBL.class)
@PowerMockIgnore("javax.management.*")
public class BypassLoginHelperTest{

	HttpServletRequest request = mock(HttpServletRequest.class);
	HashMap<String,Object> sessionMap = new HashMap<String,Object>();
	

	@Before
	public void setUp(){
		PowerMockito.mockStatic(LoginBL.class);
			//LoginBL.setEnvironment(TPersonBean.GUEST_USER, "trackplus", "nonce", request, sessionMap, true, false, false);
		PowerMockito.when(LoginBL.setEnvironment(anyString(),anyString(),anyString(),any(HttpServletRequest.class), any(HashMap.class), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(sessionMap);
	}
	
	
	@Test
	public  void testLoginAsGuestLicenceProblems() {
		sessionMap.put("mappingEnum", 6);
		assertTrue(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	
	@Test
	public  void testLoginAsGuestForwardURL() {
		sessionMap.put("mappingEnum", 7);
		assertTrue(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}

	@Test
	public  void testLoginAsGuestSuccess() {
		sessionMap.put("mappingEnum", 9);
		assertTrue(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	
	@Test
	public  void testLoginAsGuestRequestLicense() {
		sessionMap.put("mappingEnum", 18);
		assertTrue(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	

	@Test
	public  void testLoginAsGuestFail0() {
		sessionMap.put("mappingEnum", 0);
		assertFalse(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	
	@Test
	public  void testLoginAsGuestFail1() {
		sessionMap.put("mappingEnum", 5);
		assertFalse(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	
	@Test
	public  void testLoginAsGuestFail2() {
		sessionMap.put("mappingEnum", 8);
		assertFalse(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	
	@Test
	public  void testLoginAsGuestFail3() {
		sessionMap.put("mappingEnum", 10);
		assertFalse(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	
	@Test
	public  void testLoginAsGuestFail4() {
		sessionMap.put("mappingEnum", 17);
		assertFalse(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}
	
	@Test
	public  void testLoginAsGuestFail5() {
		sessionMap.put("mappingEnum", 19);
		assertFalse(BypassLoginHelper.loginAsGuest(request, sessionMap));
	}

}
