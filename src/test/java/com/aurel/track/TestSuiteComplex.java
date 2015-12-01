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
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	//Complex Classes
	com.aurel.track.util.event.FreemarkerMailHandlerIssueChangeTest.class,
	com.aurel.track.beans.TWorkItemBeanTest.class,
	com.aurel.track.beans.TPersonBeanTest.class,
	com.aurel.track.attachment.AttachBLTest.class,
	com.aurel.track.admin.user.person.PersonBLTest.class,
	com.aurel.track.admin.project.release.ReleaseConfigBLTest.class,
	//Complex Methods
	com.aurel.track.fieldType.runtime.base.FieldsManagerRTTest.class,
	com.aurel.track.lucene.search.LuceneSearcherTest.class,
	com.aurel.track.admin.customize.lists.ListBLTest.class,
	com.aurel.track.admin.project.ProjectConfigBLTest.class,
	com.aurel.track.exchange.msProject.importer.MsProjectImporterBLTest.class
})
public class TestSuiteComplex {


}
