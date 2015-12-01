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

package com.aurel.track.servlet;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


public class ReportXMLServletTest {
	ReportXMLServlet rxs;
	
	MockHttpServletRequest request;
	MockHttpServletResponse response;
	

	@Before
	public void setUp(){
		rxs = new ReportXMLServlet();
		 
		request = new MockHttpServletRequest();	//Mocks
		response = new MockHttpServletResponse();	// "
	}
	
	@Test
	public void testDoGet() throws ServletException, IOException{
		rxs.doGet(request, response);
		assertTrue(true);
	}
	

	
	@Test
	public void testDoPost() throws ServletException, IOException{
		request.addParameter("query","QUERY");
		
		rxs.doPost(request, response);
		assertTrue(true);	// Smoke
	}
	
	

	@Test
	public void testCreateResponseError() {
		PrintWriter out = new PrintWriter(System.out,true);
		rxs.createResponseError(out, "errorMessage");
		assertTrue(true);	//smoke test
	}

}
