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

package com.aurel.track.util.calendar;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CalendarDayTest {
	
	CalendarDay cd;
	
	@Before
	public void setUp(){
		cd = new CalendarDay();
	}

	@Test
	public void testAddLineAndGetLines1() {
		cd.addLine("newline");
		assertEquals(cd.getLines(), "newline");
	}
	
	@Test
	public void testAddLineAndGetLines2() {
		cd.addLine("newline");
		cd.addLine("newline2");
		assertEquals(cd.getLines(), "newline<br/>newline2");
	}
	
	@Test
	public void testAddLineAndGetLines3() {
		cd.addLine("newline");
		cd.addLine("newline2");
		cd.addLine("newline3");
		assertEquals(cd.getLines(), "newline<br/>newline2<br/>newline3");
	}
	
	@Test
	public void testToString1(){
		cd.addLine("newline");
		assertEquals("<td width=\"14%\">" + "newline" + "</td>",cd.toString());
	}

	@Test
	public void testToString2(){
		cd.addLine("newline");
		cd.setStyleClass("styleClass");
		assertEquals("<td width=\"14%\" class=\"" + cd.getStyleClass() + "\">" + "newline" + "</td>",cd.toString());
	}
	
	@Test
	public void testToString3(){
		cd.addLine("newline");
		cd.setStyleClass("styleClass");
		cd.setStyleId("styleId");
		assertEquals("<td width=\"14%\" class=\"" + cd.getStyleClass() + "\" id=\"" + cd.getStyleId() + "\">" + "newline" + "</td>",cd.toString());
	}
}
