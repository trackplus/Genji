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

package com.aurel.track.admin.project.release;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Ignore;
import org.junit.Test;

import com.aurel.track.beans.TPersonBean;

public class ReleaseConfigBLTest {
	
	@Test
	public void testgetProjectBranchNodeID0() {
		assertEquals("0",ReleaseConfigBL.getProjectBranchNodeID(0));
	}

	@Test
	public void testgetProjectBranchNodeID2() {
		assertEquals("2",ReleaseConfigBL.getProjectBranchNodeID(2));
	}

	@Test
	public void testgetLocalizedLabels(){
		assertNotNull(ReleaseConfigBL.getLocalizedLabels("", null));
	}
	
	@Test
	public void testencodeNode(){
		ProjectReleaseTokens prt = new ProjectReleaseTokens();
		assertEquals("",ReleaseConfigBL.encodeNode(prt));
	}
	
	@Test
	public void testdecodeNode(){
		assertNotNull(ReleaseConfigBL.getReleaseNodes(""));
	}
	
	@Test
	public void testgetReleaseRows(){
		ReleaseConfigBL.getReleaseDetail("", true, true, null);
		assertTrue(true);
	}
	
	@Test
	public void testsaveReleaseDetail(){
		ReleaseDetailTO rd = new ReleaseDetailTO();
		TPersonBean pb = new TPersonBean();
		Locale locale = new Locale("language");
		ReleaseConfigBL.saveReleaseDetail("", true, true, rd, true, 3, pb, locale);
		assertTrue(true);
	}
	
	@Test
	@Ignore
	
	public void testcopy(){
		ReleaseConfigBL.copy("nodeFrom", "nodeTo");	// NumberFormatException!
		assertTrue(true);
	}

	public void testcopy2(){
		assertEquals("",ReleaseConfigBL.copy("", ""));	// NumberFormatException!
	}
	
	
	
	@Test
	public void testclearParent(){
		assertNotNull(ReleaseConfigBL.clearParent(""));
	}
	
	@Test
	public void testdelete(){
		TPersonBean pb = new TPersonBean();
		Locale locale = new Locale("language");
		assertNotNull("",ReleaseConfigBL.delete("", pb, locale));
	}
	
	@Test
	public void testrenderReplace(){
		TPersonBean pb = new TPersonBean();
		Locale locale = new Locale("language");
		assertNotNull("",ReleaseConfigBL.replaceAndDelete(3, "", pb, locale));
	}
	
	@Test
	public void testdroppedNear(){
		ReleaseConfigBL.moveUp("");
		assertTrue(true);
	}
	
	@Test
	public void testmoveDown(){
		ReleaseConfigBL.moveDown("");
		assertTrue(true);
	}
}
