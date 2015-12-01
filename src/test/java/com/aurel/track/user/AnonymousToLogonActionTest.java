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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Test;

import com.aurel.track.Constants;

public class AnonymousToLogonActionTest {
	
	AnonymousToLogonAction a2la = new AnonymousToLogonAction();
	HashMap<String,Object> newmap = new HashMap<String,Object>();
	

	@Test
	public void testConstruktor() {
		assertNotNull(a2la);
	}
	
	@Test
	public void testSession() throws Exception {
		a2la.setSession(newmap);
		assertEquals(newmap,a2la.getSession());
	}
	
	@Test
	public void testExecute() throws Exception {
		a2la.setForwardURL("forwardURL");
		assertEquals("forwardURL",a2la.getForwardURL());
		
	
		a2la.setForwardURL("forwardURL");
		//assertEquals("logon",a2la.execute());
	}

}
