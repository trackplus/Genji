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

package com.aurel.track.user;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class LogoffActionTest {

	LogoffAction la = new LogoffAction();
	HttpServletRequest mockRequest = mock(HttpServletRequest.class);
	HttpSession mockSession = mock(HttpSession.class);
	
	@Before
	public void setUp(){
		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
		
		
	}
	
	@Test
	@Ignore
	public void testPrepare() throws Exception {
		la.backToDesktop();
		assertTrue(true);
	}

}
