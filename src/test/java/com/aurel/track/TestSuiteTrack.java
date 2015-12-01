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

package com.aurel.track;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

/**
 * The class <code>TestAll</code> builds a suite that can be used to run all
 * of the tests within its package as well as within any subpackages of its
 * package.
 *
 * @author Joerg Friedrich
 * @version $Revision: 1.0 $
 */
@PowerMockIgnore("javax.management.*")
@RunWith(Suite.class)
@Suite.SuiteClasses({
//	com.aurel.track.accessControl.TestSuiteAccessControl.class,
//	com.aurel.track.dbase.StartServletTest.class,	//Takes a lot of time
////	com.aurel.track.exchange.importer.TestSuiteMsProjectImport.class,	//getApplicationBean is deprecated
//	com.aurel.track.IconClassTest.class,
//	com.aurel.track.json.JSONUtilityTestSuit.class,
//	com.aurel.track.lucene.util.TestSuiteUtil.class,
////	com.aurel.track.move.TestSuiteMove.class,	// Initialisation ERROR - beforeClass() should be static
//	com.aurel.track.persist.AllTestsPersist.class,
//	com.aurel.track.prop.TestSuiteProp.class,	// ERROR at ApplicationBeanTest.testClone_1
//	com.aurel.track.servlet.ReportXMLServletTest.class,
//	com.aurel.track.tab.TestSuiteTab.class,
//	com.aurel.track.teamgeist.TeamgeistServicesTest.class,
//	com.aurel.track.util.TestSuiteUtil.class,
//	// TestSuites
////	com.aurel.track.DBScriptTest.class,	// Initialisation ERROR
//	com.aurel.track.IconClassTest.class,	//Takes a lot of time
//	com.aurel.track.StartServerTest.class,	//Takes a lot of time
//	com.aurel.track.TestSuitComplex.class
})
	public class TestSuiteTrack {

		public static void main(String[] args) {

		}
}
