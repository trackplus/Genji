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

package com.aurel.track.lucene.util;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>ValidatorTest</code> contains tests for the class <code>{@link Validator}</code>.
 *
 * @generatedBy CodePro at 14.04.15 01:48
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class ValidatorTest {
	/**
	 * Run the boolean isAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsAddress_1()
		throws Exception {
		String address = "";

		boolean result = Validator.isAddress(address);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsAddress_2()
		throws Exception {
		String address = "";

		boolean result = Validator.isAddress(address);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsAddress_3()
		throws Exception {
		String address = "";

		boolean result = Validator.isAddress(address);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsAddress_4()
		throws Exception {
		String address = "";

		boolean result = Validator.isAddress(address);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsAddress_5()
		throws Exception {
		String address = "";

		boolean result = Validator.isAddress(address);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsAddress_6()
		throws Exception {
		String address = "";

		boolean result = Validator.isAddress(address);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isChar(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsChar_1()
		throws Exception {
		char c = '';

		boolean result = Validator.isChar(c);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isChar(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsChar_2()
		throws Exception {
		char c = '';

		boolean result = Validator.isChar(c);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isChar(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsChar_3()
		throws Exception {
		String s = "";

		boolean result = Validator.isChar(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isChar(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsChar_4()
		throws Exception {
		String s = "";

		boolean result = Validator.isChar(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isChar(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsChar_5()
		throws Exception {
		String s = "ghgfg";

		boolean result = Validator.isChar(s);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isChar(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsChar_6()
		throws Exception {
		String s = "999";

		boolean result = Validator.isChar(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDate(int,int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDate_1()
		throws Exception {
		int month = 1;
		int day = 1;
		int year = 1;

		boolean result = Validator.isDate(month, day, year);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isDate(int,int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDate_2()
		throws Exception {
		int month = 1;
		int day = 1;
		int year = 1;

		boolean result = Validator.isDate(month, day, year);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isDigit(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_1()
		throws Exception {
		char c = '0';

		boolean result = Validator.isDigit(c);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isDigit(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_2()
		throws Exception {
		char c = '';

		boolean result = Validator.isDigit(c);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDigit(char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_3()
		throws Exception {
		char c = ':';

		boolean result = Validator.isDigit(c);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDigit(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_4()
		throws Exception {
		String s = "";

		boolean result = Validator.isDigit(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDigit(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_5()
		throws Exception {
		String s = "";

		boolean result = Validator.isDigit(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDigit(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_6()
		throws Exception {
		String s = "6654";

		boolean result = Validator.isDigit(s);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isDigit(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_7()
		throws Exception {
		String s = "";

		boolean result = Validator.isDigit(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isDigit(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsDigit_8()
		throws Exception {
		String s = "";

		boolean result = Validator.isDigit(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_1()
		throws Exception {
		String ea = "";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_2()
		throws Exception {
		String ea = "";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_3()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_4()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_5()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_6()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_7()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_8()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_9()
		throws Exception {
		String ea = "xyt@ghhghh.com";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_10()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_11()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_12()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_13()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_14()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_15()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isEmailAddress(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsEmailAddress_16()
		throws Exception {
		String ea = "aaaaaa";

		boolean result = Validator.isEmailAddress(ea);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isGregorianDate(int,int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsGregorianDate_1()
		throws Exception {
		int month = 1;
		int day = 1;
		int year = 1;

		boolean result = Validator.isGregorianDate(month, day, year);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isGregorianDate(int,int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsGregorianDate_2()
		throws Exception {
		int month = 1;
		int day = 1;
		int year = 1;

		boolean result = Validator.isGregorianDate(month, day, year);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isHTML(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsHTML_1()
		throws Exception {
		String s = "";

		boolean result = Validator.isHTML(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isHTML(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsHTML_2()
		throws Exception {
		String s = "";

		boolean result = Validator.isHTML(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isHTML(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsHTML_3()
		throws Exception {
		String s = "";

		boolean result = Validator.isHTML(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isHTML(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsHTML_4()
		throws Exception {
		String s = "";

		boolean result = Validator.isHTML(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isHTML(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsHTML_5()
		throws Exception {
		String s = "";

		boolean result = Validator.isHTML(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isHex(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsHex_1()
		throws Exception {
		String s = "";

		boolean result = Validator.isHex(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isHex(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsHex_2()
		throws Exception {
		String s = "";

		boolean result = Validator.isHex(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isJulianDate(int,int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsJulianDate_1()
		throws Exception {
		int month = 1;
		int day = 1;
		int year = 1;

		boolean result = Validator.isJulianDate(month, day, year);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isJulianDate(int,int,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsJulianDate_2()
		throws Exception {
		int month = 1;
		int day = 1;
		int year = 1;

		boolean result = Validator.isJulianDate(month, day, year);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isLUHN(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsLUHN_1()
		throws Exception {
		String number = null;

		boolean result = Validator.isLUHN(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isLUHN(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsLUHN_2()
		throws Exception {
		String number = "a";

		boolean result = Validator.isLUHN(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isLUHN(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsLUHN_3()
		throws Exception {
		String number = "a";

		boolean result = Validator.isLUHN(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isLUHN(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsLUHN_4()
		throws Exception {
		String number = "a";

		boolean result = Validator.isLUHN(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isLUHN(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsLUHN_5()
		throws Exception {
		String number = "a";

		boolean result = Validator.isLUHN(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isLUHN(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsLUHN_6()
		throws Exception {
		String number = "9999";

		boolean result = Validator.isLUHN(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isLUHN(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsLUHN_7()
		throws Exception {
		String number = "";

		boolean result = Validator.isLUHN(number);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isName(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsName_1()
		throws Exception {
		String name = "";

		boolean result = Validator.isName(name);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isName(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsName_2()
		throws Exception {
		String name = "";

		boolean result = Validator.isName(name);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isName(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsName_3()
		throws Exception {
		String name = "";

		boolean result = Validator.isName(name);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isName(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsName_4()
		throws Exception {
		String name = "";

		boolean result = Validator.isName(name);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isName(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsName_5()
		throws Exception {
		String name = "";

		boolean result = Validator.isName(name);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isNotNull(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNotNull_1()
		throws Exception {
		String s = "";

		boolean result = Validator.isNotNull(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isNotNull(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNotNull_2()
		throws Exception {
		String s = "";

		boolean result = Validator.isNotNull(s);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isNull(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNull_1()
		throws Exception {
		String s = null;

		boolean result = Validator.isNull(s);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isNull(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNull_2()
		throws Exception {
		String s = "";

		boolean result = Validator.isNull(s);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isNull(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNull_3()
		throws Exception {
		String s = "";

		boolean result = Validator.isNull(s);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isNull(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNull_4()
		throws Exception {
		String s = "";

		boolean result = Validator.isNull(s);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isNumber(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNumber_1()
		throws Exception {
		String number = "";

		boolean result = Validator.isNumber(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isNumber(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNumber_2()
		throws Exception {
		String number = "";

		boolean result = Validator.isNumber(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isNumber(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNumber_3()
		throws Exception {
		String number = "";

		boolean result = Validator.isNumber(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isNumber(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsNumber_4()
		throws Exception {
		String number = "";

		boolean result = Validator.isNumber(number);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPassword_1()
		throws Exception {
		String password = "";

		boolean result = Validator.isPassword(password);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPassword_2()
		throws Exception {
		String password = "";

		boolean result = Validator.isPassword(password);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPassword_3()
		throws Exception {
		String password = "aaaa";

		boolean result = Validator.isPassword(password);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPassword_4()
		throws Exception {
		String password = "aaaa";

		boolean result = Validator.isPassword(password);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPassword_5()
		throws Exception {
		String password = "aaaa";

		boolean result = Validator.isPassword(password);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isPassword(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPassword_6()
		throws Exception {
		String password = "aaaa";

		boolean result = Validator.isPassword(password);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isPhoneNumber(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPhoneNumber_1()
		throws Exception {
		String phoneNumber = "";

		boolean result = Validator.isPhoneNumber(phoneNumber);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean isPhoneNumber(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	@Test
	public void testIsPhoneNumber_2()
		throws Exception {
		String phoneNumber = "";

		boolean result = Validator.isPhoneNumber(phoneNumber);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 14.04.15 01:48
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
	 * @generatedBy CodePro at 14.04.15 01:48
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
	 * @generatedBy CodePro at 14.04.15 01:48
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(ValidatorTest.class);
	}
}
