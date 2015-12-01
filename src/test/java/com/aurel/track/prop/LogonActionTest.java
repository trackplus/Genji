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

package com.aurel.track.prop;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.aurel.track.prop.actions.LogonAction;
import com.opensymphony.xwork2.ActionProxy;

/**
 * The class <code>LogonActionTest</code> contains tests for the class <code>{@link LogonAction}</code>.
 *
 * @generatedBy CodePro at 05.01.15 23:18, using the Struts generator
 * @author friedj
 * @version $Revision: 1661 $
 */
public class LogonActionTest extends StrutsTestCase{
	
	final ServletActionContext ctx = Mockito.mock(ServletActionContext.class);
	// final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	
	/**
	 * Run the LogonAction() constructor test.
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@Test
	public void testLogonAction_1()
		throws Exception {
		LogonAction result = new LogonAction();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Run the String execute() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@Test
	public void testExecute_1()
		throws Exception {

		request.setParameter("j_username", "XXX");
		request.setParameter("password", "XXX");
		request.setParameter("testMode", "true");
		
		ActionProxy actionProxy = getActionProxy("/logon!login.action") ;
		LogonAction action = (LogonAction) actionProxy.getAction();
		assertNotNull("The action is null but should not be.", action);
		String result = actionProxy.execute();
		assertEquals("The execute method did not return " + "loading" + " but should have.", "loading", result);
	}
	
	@Test
	public void testExecute_1_1()
		throws Exception {

		request.setParameter("j_username", "");
		request.setParameter("password", "");
		request.setParameter("testMode", "");
		
		ActionProxy actionProxy = getActionProxy("/logon.action") ;
		LogonAction action = (LogonAction) actionProxy.getAction();
		assertNotNull("The action is null but should not be.", action);
		String result = actionProxy.execute();
		assertEquals("The execute method did not return " + "forwardToLogin" + " but should have.", "forwardToLogin", result);
	}

	/**
	 * Run the String execute() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@Test
	public void testExecute_2()
		throws Exception {

		request.setParameter("j_username", "admin");
		request.setParameter("password", "tissi");
		request.setParameter("testMode", "true");

		ActionProxy actionProxy = getActionProxy("/logon!login.action") ;
		LogonAction action = (LogonAction) actionProxy.getAction();
		assertNotNull("The action is null but should not be.", action);
		String result = actionProxy.execute();
		// System.err.println(result);
		// assertEquals("The execute method did not return " + "forwardToLogin" + " but should have.", "forwardToLogin", result);
		assertEquals("The execute method did not return " + "loading" + " but should have.", "loading", result);

	}

	/**
	 * Run the String execute() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@Test
	public void testExecute_3()
		throws Exception {

		request.setParameter("j_username", "admin");
		request.setParameter("password", "tissi");
		request.setParameter("testMode", "");
		request.setParameter("isMobileApplication", "true");
		request.setParameter("mobileApplicationVersionNo", "2");
		
		ActionProxy actionProxy = getActionProxy("/logon!login.action") ;
		LogonAction action = (LogonAction) actionProxy.getAction();
		assertNotNull("The action is null but should not be.", action);
		String result = actionProxy.execute();
		assertEquals("The execute method did not return " + "loading" + " but should have.", "loading", result);
	}


	/**
	 * Run the String failLogin() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@Test
	public void testFailLogin_1()
		throws Exception {

		request.setParameter("j_username", "admin");
		request.setParameter("password", "FFFFF");
		request.setParameter("testMode", "true");
		request.setParameter("isMobileApplication", "false");
		request.setParameter("mobileApplicationVersionNo", "2");
		
		ActionProxy actionProxy = getActionProxy("/logon!login.action") ;
		LogonAction action = (LogonAction) actionProxy.getAction();
		assertNotNull("The action is null but should not be.", action);
		String result = actionProxy.execute();
		assertEquals("The execute method did not return " + "loading" + " but should have.", "loading", result);
	}

	/**
	 * Run the void prepare() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@Test
	public void testPrepare_1()
		throws Exception {
		LogonAction fixture = new LogonAction();
		fixture.setPassword("");
		fixture.setMobileApplicationVersionNo(new Integer(1));
		fixture.setJ_username("");
		fixture.setTestMode("");
		fixture.setIsMobileApplication(new Boolean(true));

		fixture.prepare();

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		// add additional set up code here
		
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}
	
	// @Override
	protected String getConfigPath() {
	    return "src/main/resources/struts.xml";
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 05.01.15 23:18
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(LogonActionTest.class);
	}
}
