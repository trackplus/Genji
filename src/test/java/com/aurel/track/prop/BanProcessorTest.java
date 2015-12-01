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

import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>BanProcessorTest</code> contains tests for the class <code>{@link BanProcessor}</code>.
 *
 * @generatedBy CodePro at 05.01.15 23:21
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class BanProcessorTest {
	/**
	 * Run the BanProcessor getBanProcessor() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testGetBanProcessor_1()
		throws Exception {

		BanProcessor result = BanProcessor.getBanProcessor();

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the BanProcessor getBanProcessor() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testGetBanProcessor_2()
		throws Exception {

		BanProcessor result = BanProcessor.getBanProcessor();

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the boolean isBanned(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testIsBanned_1()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		boolean result = fixture.isBanned(ipNumber);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isBanned(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testIsBanned_2()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		boolean result = fixture.isBanned(ipNumber);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isBanned(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testIsBanned_3()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		boolean result = fixture.isBanned(ipNumber);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isBanned(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testIsBanned_4()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		boolean result = fixture.isBanned(ipNumber);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the void markBadAttempt(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testMarkBadAttempt_1()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		fixture.markBadAttempt(ipNumber);

		// add additional test code here
	}

	/**
	 * Run the void markBadAttempt(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testMarkBadAttempt_2()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		fixture.markBadAttempt(ipNumber);

		// add additional test code here
	}

	/**
	 * Run the void markBadAttempt(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testMarkBadAttempt_3()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		fixture.markBadAttempt(ipNumber);

		// add additional test code here
	}

	/**
	 * Run the void markBadAttempt(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testMarkBadAttempt_4()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		fixture.markBadAttempt(ipNumber);

		// add additional test code here
	}

	/**
	 * Run the void removeBanEntry(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testRemoveBanEntry_1()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();
		String ipNumber = "";

		fixture.removeBanEntry(ipNumber);

		// add additional test code here
	}

	/**
	 * Run the void setBanTime(int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testSetBanTime_1()
		throws Exception {
		int _banTime = 1;

		BanProcessor.setBanTime(_banTime);

		// add additional test code here
	}

	/**
	 * Run the void setMaxNoOfBadAttempts(int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testSetMaxNoOfBadAttempts_1()
		throws Exception {
		int maxatt = 1;

		BanProcessor.setMaxNoOfBadAttempts(maxatt);

		// add additional test code here
	}

	/**
	 * Run the void updateBanMap() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testUpdateBanMap_1()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();

		fixture.updateBanMap();

		// add additional test code here
	}

	/**
	 * Run the void updateBanMap() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testUpdateBanMap_2()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();

		fixture.updateBanMap();

		// add additional test code here
	}

	/**
	 * Run the void updateBanMap() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Test
	public void testUpdateBanMap_3()
		throws Exception {
		BanProcessor fixture = BanProcessor.getBanProcessor();
		fixture.banMap = new HashMap();

		fixture.updateBanMap();

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 05.01.15 23:21
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(BanProcessorTest.class);
	}
}
