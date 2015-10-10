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

package com.aurel.track.json;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.aurel.track.dbase.DatabaseHandler;
import com.aurel.track.persist.TSite;
import com.aurel.track.persist.TSitePeer;
import com.aurel.track.prop.ApplicationBean;

@RunWith(Suite.class)
@SuiteClasses({
	com.aurel.track.json.JSONUtilityTest.class
})
public class JSONUtilityTestSuit {
	@BeforeClass
	public static void setUp() throws Exception {

		DatabaseHandler.initializeDatabase();

		TSite bean = TSitePeer.load();
		ApplicationBean.getApplicationBean().setSiteBean(bean.getBean());
		ApplicationBean.getApplicationBean().setSiteParams(bean.getBean());
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}
}
