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

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.aurel.track.beans.IBeanID;

public class GeneralUtilsTest {
	
	Integer[] intarray;
	static ArrayList<LabelValueBean> beanIDList;
	
	@BeforeClass
	public static void setUp(){

		//GeneralUtils gu = new GeneralUtils();
		LabelValueBean lvb1 = new LabelValueBean("label1", "value1");
		LabelValueBean lvb2 = new LabelValueBean("label2", "value2");
		LabelValueBean lvb3 = new LabelValueBean("label3", "value3");
		
		beanIDList = new ArrayList<LabelValueBean>();
		beanIDList.add(lvb1);
		beanIDList.add(lvb2);
		beanIDList.add(lvb3);
	}
	
	@Test
	@Ignore
	public void testGetBeanIDs(){

		intarray = GeneralUtils.getBeanIDs(beanIDList);	
		assertTrue(intarray.length == 3);
	}

}
