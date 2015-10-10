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

package com.aurel.track.report.dashboard;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;

public class CalendarDashTest {
	
	@Before
	public void setUp(){

	}

	@Test
	public void test() throws TooManyItemsToLoadException{
		
		CalendarDash cd = new CalendarDash();
		
		Map<String,Object> session = new HashMap<String,Object>();
		TPersonBean personMock = mock(TPersonBean.class);
		session.put(Constants.USER_KEY, null);
		
		Map<String,String> parameters= new HashMap<String,String>();
		Map<String,String> ajaxParams = new HashMap<String,String>();

		Integer projectID = new Integer(10);
		Integer releaseID = new Integer(20);
		
		
		cd.encodeJSONExtraData(42, session, parameters, projectID, releaseID, ajaxParams);
	}

}
