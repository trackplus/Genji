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

package com.aurel.track.admin.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;

public class ProjectConfigBLTest {
	Integer i = new Integer(2);
	TPersonBean personBean = new TPersonBean();
	Locale locale;
	TProjectBean pb = new TProjectBean();

	
	
	@Test
	public void testgetChildren() {
		assertEquals("[]",ProjectConfigBL.getChildren(i, personBean, locale));
	}
	
	@Test
	public void testmightHaveRelease(){
		assertFalse(ProjectConfigBL.mightHaveRelease(null, null));
	}
	
	@Test
	public void testgetDefaultSubproject(){
		
		assertNull(ProjectConfigBL.getDefaultSubproject(pb));
	}
	
	@Test
	public void testgetDefaultClass(){
		assertNull(ProjectConfigBL.getDefaultClass(pb));
	}
	
	@Test
	public void testgetDefaultRelease(){
		assertNull(ProjectConfigBL.getDefaultRelease(pb, true, null));
	}
	
	@Test
	public void testgetDefaultIssueType(){
		assertNull(ProjectConfigBL.getDefaultIssueType(pb, i));
	}

}
