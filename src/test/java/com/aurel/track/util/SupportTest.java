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

package com.aurel.track.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * The class <code>SupportTest</code> contains tests for the class <code>{@link Support}</code>.
 *
 * @generatedBy CodePro at 19.03.15 01:29
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class SupportTest {
	/**
	 * Run the Support() constructor test.
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSupport_1()
		throws Exception {
		Support result = new Support();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = null;

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"\"", result);
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_2()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "";

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"\"", result);
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_3()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = null;

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"\"", result);
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_4()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = null;

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"\"", result);
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_5()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = null;

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"\"", result);
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_6()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "aa";

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"aa\"", result);
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_7()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = null;

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"\"", result);
	}

	/**
	 * Run the String csvWrite(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testCsvWrite_8()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = null;

		String result = fixture.csvWrite(value);

		// add additional test code here
		assertEquals("\"\"", result);
	}

	/**
	 * Run the String dateTimeWriter() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testDateTimeWriter_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");

		String result = fixture.dateTimeWriter().substring(0,15);
        java.util.Date today = new java.util.Date();
		SimpleDateFormat dbFormatter = new SimpleDateFormat(
							"yyyy-MM-dd H:mm:ss.S",
										   new Locale("de"));
        String orig = dbFormatter.format(today).substring(0,15);
		assertEquals(orig, result);
	}

	/**
	 * Run the String encryptPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testEncryptPassword_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String password = "";

		String result = fixture.encryptPassword(password);

		// add additional test code here
		assertEquals("-2639-5d-125e6b4bd3255-41-11-6b6018-70-51-2879", result);
	}

	/**
	 * Run the String encryptPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testEncryptPassword_2()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String password = "";

		String result = fixture.encryptPassword(password);

		// add additional test code here
		assertEquals("-2639-5d-125e6b4bd3255-41-11-6b6018-70-51-2879", result);
	}

	/**
	 * Run the String encryptPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testEncryptPassword_3()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String password = "";

		String result = fixture.encryptPassword(password);

		// add additional test code here
		assertEquals("-2639-5d-125e6b4bd3255-41-11-6b6018-70-51-2879", result);
	}

	/**
	 * Run the String encryptPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testEncryptPassword_4()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String password = "";

		String result = fixture.encryptPassword(password);

		// add additional test code here
		assertEquals("-2639-5d-125e6b4bd3255-41-11-6b6018-70-51-2879", result);
	}

	/**
	 * Run the String getLastBaseURL() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetLastBaseURL_1()
		throws Exception {

		String result = Support.getLastBaseURL();

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String getLastBaseURL() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetLastBaseURL_2()
		throws Exception {

		String result = Support.getLastBaseURL();

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String[] getWords(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetWords_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String parseString = null;

		String[] result = fixture.getWords(parseString);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String[] getWords(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetWords_2()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String parseString = "";

		String[] result = fixture.getWords(parseString);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String[] getWords(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetWords_3()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String parseString = "";

		String[] result = fixture.getWords(parseString);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String[] getWords(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetWords_4()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String parseString = "";

		String[] result = fixture.getWords(parseString);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String[] getWords(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetWords_5()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String parseString = "";

		String[] result = fixture.getWords(parseString);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String[] getWords(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testGetWords_6()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String parseString = "";

		String[] result = fixture.getWords(parseString);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the boolean isDelimiter(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testIsDelimiter_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		char search = '';

		boolean result = fixture.isDelimiter(search);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDelimiter(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testIsDelimiter_2()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		char search = '';

		boolean result = fixture.isDelimiter(search);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDelimiter(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testIsDelimiter_3()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		char search = '';

		boolean result = fixture.isDelimiter(search);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the void loadLastURIs() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testLoadLastURIs_1()
		throws Exception {

		Support.loadLastURIs();

		// add additional test code here
	}

	/**
	 * Run the String quoteString(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testQuoteString_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = null;

		String result = fixture.quoteString(value);

		// add additional test code here
		assertEquals("null", result);
	}

	/**
	 * Run the String quoteString(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testQuoteString_2()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "";

		String result = fixture.quoteString(value);

		// add additional test code here
		assertEquals("''", result);
	}

	/**
	 * Run the String quoteString(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testQuoteString_3()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "null";

		String result = fixture.quoteString(value);

		// add additional test code here
		assertEquals("null", result);
	}

	/**
	 * Run the String quoteString(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testQuoteString_4()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "NULL";

		String result = fixture.quoteString(value);

		// add additional test code here
		assertEquals("null", result);
	}

	/**
	 * Run the String quoteString(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testQuoteString_5()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "aa";

		String result = fixture.quoteString(value);

		// add additional test code here
		assertEquals("'aa'", result);
	}

	/**
	 * Run the String quoteString(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testQuoteString_6()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "aa";

		String result = fixture.quoteString(value);

		// add additional test code here
		assertEquals("'aa'", result);
	}

	/**
	 * Run the String quoteString(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testQuoteString_7()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String value = "a";

		String result = fixture.quoteString(value);

		// add additional test code here
		assertEquals("'a'", result);
	}

	/**
	 * Run the String readStackTrace(Exception) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
//	@Test
//	public void testReadStackTrace_1()
//		throws Exception {
//		Exception e = new Exception("Test");
//
//		String result = Support.readStackTrace(e);
//
//		assert(result.startsWith("java.lang.Exception: Test"));	}

	/**
	 * Run the void setCsvDelimiterChar(Character) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetCsvDelimiterChar_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		Character theDelimiter = new Character('');

		fixture.setCsvDelimiterChar(theDelimiter);

		// add additional test code here
	}

	/**
	 * Run the void setDelimiters(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetDelimiters_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		String delimiters = "";

		fixture.setDelimiters(delimiters);

		// add additional test code here
	}

	/**
	 * Run the void setURIs(HttpServletRequest) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetURIs_1()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		HttpServletRequest request = new HttpServletRequestWrapper(new MockHttpServletRequest());

		// fixture.setURIs(request);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at com.aurel.track.util.Support.setURIs(Support.java:338)
	}

	/**
	 * Run the void setURIs(HttpServletRequest) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetURIs_2()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		HttpServletRequest request = new HttpServletRequestWrapper(new MockHttpServletRequest());

		// fixture.setURIs(request);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at com.aurel.track.util.Support.setURIs(Support.java:338)
	}

	/**
	 * Run the void setURIs(HttpServletRequest) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetURIs_3()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		HttpServletRequest request = new HttpServletRequestWrapper(new MockHttpServletRequest());

		// fixture.setURIs(request);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at com.aurel.track.util.Support.setURIs(Support.java:338)
	}

	/**
	 * Run the void setURIs(HttpServletRequest) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetURIs_4()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		HttpServletRequest request = new HttpServletRequestWrapper(new MockHttpServletRequest());

		// fixture.setURIs(request);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at com.aurel.track.util.Support.setURIs(Support.java:338)
	}

	/**
	 * Run the void setURIs(HttpServletRequest) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetURIs_5()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		HttpServletRequest request = new HttpServletRequestWrapper(new MockHttpServletRequest());

		// fixture.setURIs(request);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at com.aurel.track.util.Support.setURIs(Support.java:338)
	}

	/**
	 * Run the void setURIs(HttpServletRequest) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	@Test
	public void testSetURIs_6()
		throws Exception {
		Support fixture = new Support();
		fixture.setDelimiters("");
		HttpServletRequest request = new HttpServletRequestWrapper(new MockHttpServletRequest());

		// fixture.setURIs(request);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at com.aurel.track.util.Support.setURIs(Support.java:338)
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 19.03.15 01:29
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
	 * @generatedBy CodePro at 19.03.15 01:29
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
	 * @generatedBy CodePro at 19.03.15 01:29
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(SupportTest.class);
	}
}
