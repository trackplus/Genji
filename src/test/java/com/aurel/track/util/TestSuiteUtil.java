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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The class <code>TestAll</code> builds a suite that can be used to run all
 * of the tests within its package as well as within any subpackages of its
 * package.
 *
 * @generatedBy CodePro at 05.01.15 23:30
 * @author friedj
 * @version $Revision: 1.0 $
 */

@RunWith(Suite.class)
@SuiteClasses({
				// Calendar
				com.aurel.track.util.calendar.CalendarDayTest.class,
				// EmailHandling
				com.aurel.track.util.emailHandling.JavaMailBeanTest.class,
				// Event
				com.aurel.track.util.event.FreemarkerMailHandlerIssueChangeTest.class,
				// HTML
				com.aurel.track.util.html.Html2LaTeXTest.class,
				
				// Util
				BooleanStringBeanTest.class,
				BypassLoginHelperTest.class,
				CalendarUtilTest.class,
				DateTimeUtilsTest.class,
				EqualUtilsTest.class,
				FastHashMapTest.class,
				GeneralUtilsTest.class,
				ImageUtilsTest.class,
				IntegerStringBeanTest.class,
				IntegerStringBooleanBeanTest.class,
				LabelValueBeanTest.class,
				LdapUtilTest.class,
				SortedOptionBeanComparatorTest.class,
				SortedOptionBeanTest.class,
	            SupportTest.class,
	            TagReplacerTest.class,
	            TreeNodeTest.class,
	            UserInfoTest.class

})
public class TestSuiteUtil {

}

