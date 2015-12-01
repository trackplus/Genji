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

package com.aurel.track.lucene.util;


import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * <a href="StringUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 3495 $
 *
 */
public class StringUtilTest {

	@Test
	public void testAdd() {
		assertTrue(StringUtil.add("string","add").equals("string,add,"));
		assertTrue(StringUtil.add("String", "add", ";", false).equals("String;add;"));
		assertTrue(StringUtil.add("String;Zwei", "add;78", ";", true).equals("String;Zwei;add;78;"));
		assertTrue(StringUtil.add(null, "add;78", ";", true).equals("add;78;"));
		assertTrue(StringUtil.add("String;Zwei;", "add;78", ";", false).equals("String;Zwei;add;78;"));
	}



	/**
	 * Run the String add(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_1()
		throws Exception {
		String s = "";
		String add = "";

		String result = StringUtil.add(s, add);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String add(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_2()
		throws Exception {
		String s = "";
		String add = "";
		String delimiter = "";

		String result = StringUtil.add(s, add, delimiter);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String add(String,String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_3()
		throws Exception {
		String s = "";
		String add = null;
		String delimiter = "";
		boolean allowDuplicates = true;

		String result = StringUtil.add(s, add, delimiter, allowDuplicates);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String add(String,String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_4()
		throws Exception {
		String s = "";
		String add = "";
		String delimiter = null;
		boolean allowDuplicates = true;

		String result = StringUtil.add(s, add, delimiter, allowDuplicates);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String add(String,String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_5()
		throws Exception {
		String s = "";
		String add = "";
		String delimiter = "";
		boolean allowDuplicates = false;

		String result = StringUtil.add(s, add, delimiter, allowDuplicates);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String add(String,String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_6()
		throws Exception {
		String s = "";
		String add = "";
		String delimiter = "";
		boolean allowDuplicates = true;

		String result = StringUtil.add(s, add, delimiter, allowDuplicates);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String add(String,String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_7()
		throws Exception {
		String s = null;
		String add = "";
		String delimiter = "";
		boolean allowDuplicates = false;

		String result = StringUtil.add(s, add, delimiter, allowDuplicates);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String add(String,String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testAdd_8()
		throws Exception {
		String s = "";
		String add = "";
		String delimiter = "";
		boolean allowDuplicates = false;

		String result = StringUtil.add(s, add, delimiter, allowDuplicates);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the boolean contains(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_1()
		throws Exception {
		String s = "";
		String text = "";

		boolean result = StringUtil.contains(s, text);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean contains(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_2()
		throws Exception {
		String s = "";
		String text = "";

		boolean result = StringUtil.contains(s, text);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean contains(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_3()
		throws Exception {
		String s = null;
		String text = "";
		String delimiter = "";

		boolean result = StringUtil.contains(s, text, delimiter);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean contains(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_4()
		throws Exception {
		String s = "";
		String text = null;
		String delimiter = "";

		boolean result = StringUtil.contains(s, text, delimiter);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean contains(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_5()
		throws Exception {
		String s = "";
		String text = "";
		String delimiter = null;

		boolean result = StringUtil.contains(s, text, delimiter);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean contains(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_6()
		throws Exception {
		String s = "";
		String text = "";
		String delimiter = "";

		boolean result = StringUtil.contains(s, text, delimiter);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean contains(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_7()
		throws Exception {
		String s = "";
		String text = "";
		String delimiter = "";

		boolean result = StringUtil.contains(s, text, delimiter);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean contains(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testContains_8()
		throws Exception {
		String s = "";
		String text = "";
		String delimiter = "";

		boolean result = StringUtil.contains(s, text, delimiter);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the int count(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testCount_1()
		throws Exception {
		String s = null;
		String text = "";

		int result = StringUtil.count(s, text);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int count(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testCount_2()
		throws Exception {
		String s = "";
		String text = null;

		int result = StringUtil.count(s, text);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int count(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testCount_3()
		throws Exception {
		String s = "";
		String text = "";

		int result = StringUtil.count(s, text);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int count(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testCount_4()
		throws Exception {
		String s = "xx yy dd ff yy dd";
		String text = "yy";

		int result = StringUtil.count(s, text);

		// add additional test code here
		assertEquals(2, result);
	}

	/**
	 * Run the boolean endsWith(String,char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testEndsWith_1()
		throws Exception {
		String s = "";
		char end = '';

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(false, result);
	}
	
	@Test
	public void testEndsWith_12()
		throws Exception {
		String s = "huhuhu";
		char end = 'u';

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(true, result);
	}
	
	@Test
	public void testEndsWith_13()
		throws Exception {
		String s = "huhuhu";
		char end = 'h';

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(false, result);
	}


	/**
	 * Run the boolean endsWith(String,char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testEndsWith_2()
		throws Exception {
		String s = "";
		char end = '';

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean endsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testEndsWith_3()
		throws Exception {
		String s = null;
		String end = "";

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean endsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testEndsWith_4()
		throws Exception {
		String s = "";
		String end = null;

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean endsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testEndsWith_5()
		throws Exception {
		String s = "a";
		String end = "a";

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean endsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testEndsWith_6()
		throws Exception {
		String s = "";
		String end = "";

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean endsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testEndsWith_7()
		throws Exception {
		String s = "";
		String end = "";

		boolean result = StringUtil.endsWith(s, end);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the String extractChars(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractChars_1()
		throws Exception {
		String s = null;

		String result = StringUtil.extractChars(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String extractChars(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractChars_2()
		throws Exception {
		String s = "";

		String result = StringUtil.extractChars(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String extractChars(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractChars_3()
		throws Exception {
		String s = "";

		String result = StringUtil.extractChars(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String extractChars(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractChars_4()
		throws Exception {
		String s = "";

		String result = StringUtil.extractChars(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String extractDigits(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractDigits_1()
		throws Exception {
		String s = null;

		String result = StringUtil.extractDigits(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String extractDigits(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractDigits_2()
		throws Exception {
		String s = "";

		String result = StringUtil.extractDigits(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String extractDigits(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractDigits_3()
		throws Exception {
		String s = "";

		String result = StringUtil.extractDigits(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String extractDigits(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testExtractDigits_4()
		throws Exception {
		String s = "";

		String result = StringUtil.extractDigits(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String merge(List) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testMerge_1()
		throws Exception {
		List list = new LinkedList();

		String result = StringUtil.merge(list);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String merge(String[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testMerge_2()
		throws Exception {
		String[] array = new String[] {};

		String result = StringUtil.merge(array);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String merge(List,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testMerge_3()
		throws Exception {
		List list = new LinkedList();
		String delimiter = "";

		String result = StringUtil.merge(list, delimiter);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String merge(String[],String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testMerge_4()
		throws Exception {
		String[] array = new String[] {};
		String delimiter = "";

		String result = StringUtil.merge(array, delimiter);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String merge(String[],String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testMerge_5()
		throws Exception {
		String[] array = new String[] {"", ""};
		String delimiter = "";

		String result = StringUtil.merge(array, delimiter);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String merge(String[],String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testMerge_6()
		throws Exception {
		String[] array = new String[] {""};
		String delimiter = "";

		String result = StringUtil.merge(array, delimiter);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String merge(String[],String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testMerge_7()
		throws Exception {
		String[] array = new String[] {};
		String delimiter = "";

		String result = StringUtil.merge(array, delimiter);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String read(InputStream) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test(expected = java.io.IOException.class)
	public void testRead_1()
		throws Exception {
		InputStream is = new PipedInputStream();

		String result = StringUtil.read(is);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String read(InputStream) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test(expected = java.io.IOException.class)
	public void testRead_2()
		throws Exception {
		InputStream is = new PipedInputStream();

		String result = StringUtil.read(is);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String read(InputStream) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test(expected = java.io.IOException.class)
	public void testRead_3()
		throws Exception {
		InputStream is = new PipedInputStream();

		String result = StringUtil.read(is);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String read(InputStream) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test(expected = java.io.IOException.class)
	public void testRead_4()
		throws Exception {
		InputStream is = new PipedInputStream();

		String result = StringUtil.read(is);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String read(ClassLoader,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRead_5()
		throws Exception {
		ClassLoader classLoader = new URLClassLoader(new URL[] {});
		String name = "";

		String result = StringUtil.read(classLoader, name);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at java.io.Reader.<init>(Reader.java:78)
		//       at java.io.InputStreamReader.<init>(InputStreamReader.java:72)
		//       at com.aurel.track.lucene.util.StringUtil.read(StringUtil.java:216)
		//       at com.aurel.track.lucene.util.StringUtil.read(StringUtil.java:212)
		assertNotNull(result);
	}

	/**
	 * Run the String read(ClassLoader,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRead_6()
		throws Exception {
		ClassLoader classLoader = new URLClassLoader(new URL[] {});
		String name = "";

		String result = StringUtil.read(classLoader, name);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at java.io.Reader.<init>(Reader.java:78)
		//       at java.io.InputStreamReader.<init>(InputStreamReader.java:72)
		//       at com.aurel.track.lucene.util.StringUtil.read(StringUtil.java:216)
		//       at com.aurel.track.lucene.util.StringUtil.read(StringUtil.java:212)
		assertNotNull(result);
	}

	/**
	 * Run the String remove(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_1()
		throws Exception {
		String s = "Some text here";
		String remove = "text";

		String result = StringUtil.remove(s, remove);

		// add additional test code here
		assertEquals("Some text here,",result);
	}

	/**
	 * Run the String remove(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_2()
		throws Exception {
		String sp = null;
		String remove = "";
		String delimiter = "";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String remove(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_3()
		throws Exception {
		String sp = "XXX";
		String remove = null;
		String delimiter = ";";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertEquals(sp, result);
	}

	/**
	 * Run the String remove(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_4()
		throws Exception {
		String sp = "";
		String remove = "";
		String delimiter = null;

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertEquals(sp, result);
	}

	/**
	 * Run the String remove(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_5()
		throws Exception {
		String sp = "";
		String remove = "";
		String delimiter = "";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertNotNull(result);
	}
	
	@Test
	public void testRemove_51()
		throws Exception {
		String sp = "Some,text,here";
		String remove = "text";
		String delimiter = ",";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertEquals("Some,here,", result);
	}

	/**
	 * Run the String remove(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_6()
		throws Exception {
		String sp = "";
		String remove = "";
		String delimiter = "";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String remove(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_7()
		throws Exception {
		String sp = "";
		String remove = "";
		String delimiter = "";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String remove(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testRemove_8()
		throws Exception {
		String sp = "";
		String remove = "";
		String delimiter = "";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertNotNull(result);
	}
	
	@Test
	public void testRemove_81()
		throws Exception {
		String sp = "XXX , hghghg , hjhjh";
		String remove = "XXX ";
		String delimiter = ",";

		String result = StringUtil.remove(sp, remove, delimiter);

		// add additional test code here
		assertEquals(" hghghg , hjhjh,",result);
	}

	/**
	 * Run the String replace(String,char,char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_1()
		throws Exception {
		String s = "";
		char oldSub = '';
		char newSub = '';

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,char,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_2()
		throws Exception {
		String s = null;
		char oldSub = '';
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String replace(String,char,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_3()
		throws Exception {
		String s = "";
		char oldSub = '';
		String newSub = null;

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals(s, result);
	}

	/**
	 * Run the String replace(String,char,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_4()
		throws Exception {
		String s = "";
		char oldSub = '';
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,char,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_5()
		throws Exception {
		String s = "";
		char oldSub = '';
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,char,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_6()
		throws Exception {
		String s = "";
		char oldSub = '';
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_7()
		throws Exception {
		String s = null;
		String oldSub = "";
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String replace(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_8()
		throws Exception {
		String s = "";
		String oldSub = null;
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals(s, result);
	}
	
	@Test
	public void testReplace_88()
		throws Exception {
		String s = "This is X and Y";
		char oldSub = 'X';
		String newSub = "YYY";

		String result = StringUtil.replace(s, oldSub, newSub);

		assertEquals("This is YYY and Y", result);
	}

	/**
	 * Run the String replace(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_9()
		throws Exception {
		String s = "";
		String oldSub = "";
		String newSub = null;

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals(s, result);
	}

	/**
	 * Run the String replace(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_10()
		throws Exception {
		String s = "";
		String oldSub = "";
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		assertEquals(s,result);
	}

	/**
	 * Run the String replace(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_11()
		throws Exception {
		String s = "";
		String oldSub = "";
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals(s,result);
	}

	/**
	 * Run the String replace(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_12()
		throws Exception {
		String s = "";
		String oldSub = "";
		String newSub = "";

		String result = StringUtil.replace(s, oldSub, newSub);

		// add additional test code here
		assertEquals(s,result);
	}

	/**
	 * Run the String replace(String,String[],String[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_13()
		throws Exception {
		String sp = null;
		String[] oldSubs = new String[] {};
		String[] newSubs = new String[] {};

		String result = StringUtil.replace(sp, oldSubs, newSubs);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String replace(String,String[],String[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_14()
		throws Exception {
		String sp = "";
		String[] oldSubs = new String[] {};
		String[] newSubs = new String[] {};

		String result = StringUtil.replace(sp, oldSubs, newSubs);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,String[],String[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_15()
		throws Exception {
		String sp = "";
		String[] oldSubs = new String[] {};
		String[] newSubs = new String[] {};

		String result = StringUtil.replace(sp, oldSubs, newSubs);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,String[],String[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_16()
		throws Exception {
		String sp = "";
		String[] oldSubs = new String[] {};
		String[] newSubs = new String[] {};

		String result = StringUtil.replace(sp, oldSubs, newSubs);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,String[],String[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_17()
		throws Exception {
		String sp = "";
		String[] oldSubs = new String[] {""};
		String[] newSubs = new String[] {""};

		String result = StringUtil.replace(sp, oldSubs, newSubs);

		// add additional test code here
		assertEquals(sp,result);
	}

	/**
	 * Run the String replace(String,String[],String[]) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReplace_18()
		throws Exception {
		String sp = "";
		String[] oldSubs = new String[] {};
		String[] newSubs = new String[] {};

		String result = StringUtil.replace(sp, oldSubs, newSubs);

		// add additional test code here
		assertEquals("", result);
	}
	
	@Test
	public void testReplace_19()
		throws Exception {
		String sp = "ABC DEF GHI, KLM";
		String[] oldSubs = new String[] {"ABC", "DEF"};
		String[] newSubs = new String[] {"CBA", "FED"};

		String result = StringUtil.replace(sp, oldSubs, newSubs);

		// add additional test code here
		assertEquals("CBA FED GHI, KLM", result);
	}

	/**
	 * Run the String reverse(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReverse_1()
		throws Exception {
		String s = null;

		String result = StringUtil.reverse(s);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String reverse(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReverse_2()
		throws Exception {
		String s = "";

		String result = StringUtil.reverse(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String reverse(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testReverse_3()
		throws Exception {
		String s = "";

		String result = StringUtil.reverse(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String shorten(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testShorten_1()
		throws Exception {
		String s = "";

		String result = StringUtil.shorten(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String shorten(String,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testShorten_2()
		throws Exception {
		String s = "";
		int length = 1;

		String result = StringUtil.shorten(s, length);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String shorten(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testShorten_3()
		throws Exception {
		String s = "";
		String suffix = "";

		String result = StringUtil.shorten(s, suffix);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String shorten(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testShorten_4()
		throws Exception {
		String sp = null;
		int length = 1;
		String suffix = "";

		String result = StringUtil.shorten(sp, length, suffix);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String shorten(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testShorten_5()
		throws Exception {
		String sp = "";
		int length = 1;
		String suffix = null;

		String result = StringUtil.shorten(sp, length, suffix);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String shorten(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testShorten_6()
		throws Exception {
		String sp = "";
		int length = 1;
		String suffix = "";

		String result = StringUtil.shorten(sp, length, suffix);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String shorten(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testShorten_7()
		throws Exception {
		String sp = "";
		int length = 1;
		String suffix = "";

		String result = StringUtil.shorten(sp, length, suffix);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String[] split(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_1()
		throws Exception {
		String s = "";

		String[] result = StringUtil.split(s);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}
	
	@Test
	public void testSplit_110()
		throws Exception {
		String s = "x,y,z,4";

		String[] result = StringUtil.split(s);

		// add additional test code here
		assertNotNull(result);
		assertEquals(4, result.length);
	}
	
	@Test
	public void testSplit_111()
		throws Exception {
		String s = "x,y,z,4";

		String[] result = StringUtil.split(s,",");

		// add additional test code here
		assertNotNull(result);
		assertEquals(4, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_2()
		throws Exception {
		String sp = null;
		String delimiter = "";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_3()
		throws Exception {
		String sp = "";
		String delimiter = null;

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_4()
		throws Exception {
		String sp = "";
		String delimiter = "";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_5()
		throws Exception {
		String sp = "";
		String delimiter = "";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_6()
		throws Exception {
		String sp = "";
		String delimiter = "";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_7()
		throws Exception {
		String sp = "";
		String delimiter = "\r";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_8()
		throws Exception {
		String sp = "";
		String delimiter = "\r";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_9()
		throws Exception {
		String sp = "";
		String delimiter = "\r";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the String[] split(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_10()
		throws Exception {
		String sp = "";
		String delimiter = "\n";

		String[] result = StringUtil.split(sp, delimiter);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the double[] split(String,String,double) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_11()
		throws Exception {
		String s = "";
		String delimiter = "";
		double x = 1.0;

		double[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}
	
	@Test
	public void testSplit_116()
		throws Exception {
		String s = "10.0, 11.0, hehe,  13.0";
		String delimiter = ",";
		double x = 1.0;

		double[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(4, result.length);
		assertTrue (Math.abs(1.0 - result[2]) < 0.01);
		assertTrue (Math.abs(11.0 - result[1]) < 0.01);
	}

	/**
	 * Run the double[] split(String,String,double) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_12()
		throws Exception {
		String s = "";
		String delimiter = "";
		double x = 1.0;

		double[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the float[] split(String,String,float) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_13()
		throws Exception {
		String s = "";
		String delimiter = "";
		float x = 1.0f;

		float[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}
	
	@Test
	public void testSplit_133()
		throws Exception {
		String s = "Some breaks in\n this sentence";
		String delimiter = "\n";

		String[] result = StringUtil.split(s, delimiter);

		assertEquals(2, result.length);
		assertEquals("Some breaks in", result[0]);
	}

	/**
	 * Run the float[] split(String,String,float) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_14()
		throws Exception {
		String s = "";
		String delimiter = "";
		float x = 1.0f;

		float[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the int[] split(String,String,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_15()
		throws Exception {
		String s = "";
		String delimiter = "";
		int x = 1;

		int[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the int[] split(String,String,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_16()
		throws Exception {
		String s = "";
		String delimiter = "";
		int x = 1;

		int[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the long[] split(String,String,long) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_17()
		throws Exception {
		String s = "";
		String delimiter = "";
		long x = 1L;

		long[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the long[] split(String,String,long) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_18()
		throws Exception {
		String s = "";
		String delimiter = "";
		long x = 1L;

		long[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the short[] split(String,String,short) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_19()
		throws Exception {
		String s = "";
		String delimiter = "";
		short x = (short) 1;

		short[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}
	
	@Test
	public void testSplit_192()
		throws Exception {
		String s = "17, dd,19, 20";
		String delimiter = ",";
		short x = (short) 55;

		short[] result = StringUtil.split(s, delimiter, x);

		assertNotNull(result);
		assertEquals(4, result.length);
		assertTrue (Math.abs(55 - result[1]) < 0.01);
		assertTrue (Math.abs(19 - result[2]) < 0.01);
	}
	
	@Test
	public void testSplit_194()
		throws Exception {
		String s = "17,dd,19,20";
		String delimiter = ",";
		long x = (long) 55;

		long[] result = StringUtil.split(s, delimiter, x);

		assertNotNull(result);
		assertEquals(4, result.length);
		assertTrue (Math.abs(55 - result[1]) < 0.01);
		assertTrue (Math.abs(19 - result[2]) < 0.01);
	}
	
	@Test
	public void testSplit_197()
		throws Exception {
		String s = "17, dd, 19, 20";
		String delimiter = ",";
		int x = (int) 55;

		int[] result = StringUtil.split(s, delimiter, x);

		assertNotNull(result);
		assertEquals(4, result.length);
		assertTrue (Math.abs(55 - result[1]) < 0.01);
		assertTrue (Math.abs(19 - result[2]) < 0.01);
	}
	
	@Test
	public void testSplit_195()
		throws Exception {
		String s = "17.0,dd,19.0,20.0";
		String delimiter = ",";
		float x = (float) 55.0;

		float[] result = StringUtil.split(s, delimiter, x);

		assertNotNull(result);
		assertEquals(4, result.length);
		assertTrue (Math.abs(55.0 - result[1]) < 0.01);
		assertTrue (Math.abs(19.0 - result[2]) < 0.01);
	}
	
	@Test
	public void testSplit_196()
		throws Exception {
		String s = "false,true,true,true";
		String delimiter = ",";
		boolean x = true;

		boolean[] result = StringUtil.split(s, delimiter, x);

		assertNotNull(result);
		assertEquals(4, result.length);

		assertTrue (result[1]);
		assertFalse (result[0]);
	}

	/**
	 * Run the short[] split(String,String,short) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_20()
		throws Exception {
		String s = "";
		String delimiter = "";
		short x = (short) 1;

		short[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the boolean[] split(String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_21()
		throws Exception {
		String s = "";
		String delimiter = "";
		boolean x = true;

		boolean[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the boolean[] split(String,String,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testSplit_22()
		throws Exception {
		String s = "";
		String delimiter = "";
		boolean x = true;

		boolean[] result = StringUtil.split(s, delimiter, x);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * Run the boolean startsWith(String,char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testStartsWith_1()
		throws Exception {
		String s = "";
		char begin = '';

		boolean result = StringUtil.startsWith(s, begin);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean startsWith(String,char) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testStartsWith_2()
		throws Exception {
		String s = "";
		char begin = '';

		boolean result = StringUtil.startsWith(s, begin);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean startsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testStartsWith_3()
		throws Exception {
		String s = null;
		String start = "";

		boolean result = StringUtil.startsWith(s, start);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean startsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testStartsWith_4()
		throws Exception {
		String s = "";
		String start = null;

		boolean result = StringUtil.startsWith(s, start);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean startsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testStartsWith_5()
		throws Exception {
		String s = "a";
		String start = "a";

		boolean result = StringUtil.startsWith(s, start);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean startsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testStartsWith_6()
		throws Exception {
		String s = "";
		String start = "";

		boolean result = StringUtil.startsWith(s, start);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean startsWith(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testStartsWith_7()
		throws Exception {
		String s = "";
		String start = "";

		boolean result = StringUtil.startsWith(s, start);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the String trimLeading(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testTrimLeading_1()
		throws Exception {
		String s = "aa";

		String result = StringUtil.trimLeading(s);

		// add additional test code here
		assertEquals("aa", result);
	}

	/**
	 * Run the String trimLeading(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testTrimLeading_2()
		throws Exception {
		String s = "aa";

		String result = StringUtil.trimLeading(s);

		// add additional test code here
		assertEquals("aa", result);
	}

	/**
	 * Run the String trimLeading(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testTrimLeading_3()
		throws Exception {
		String s = "";

		String result = StringUtil.trimLeading(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String trimTrailing(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testTrimTrailing_1()
		throws Exception {
		String s = "aa";

		String result = StringUtil.trimTrailing(s);

		// add additional test code here
		assertEquals("aa", result);
	}

	/**
	 * Run the String trimTrailing(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testTrimTrailing_2()
		throws Exception {
		String s = "aa";

		String result = StringUtil.trimTrailing(s);

		// add additional test code here
		assertEquals("aa", result);
	}

	/**
	 * Run the String trimTrailing(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testTrimTrailing_3()
		throws Exception {
		String s = "";

		String result = StringUtil.trimTrailing(s);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String wrap(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_1()
		throws Exception {
		String text = "";

		String result = StringUtil.wrap(text);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String wrap(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_2()
		throws Exception {
		String text = null;
		int width = 1;
		String lineSeparator = "";

		String result = StringUtil.wrap(text, width, lineSeparator);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String wrap(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_3()
		throws Exception {
		String text = "a";
		int width = 1;
		String lineSeparator = "";

		String result = StringUtil.wrap(text, width, lineSeparator);

		// add additional test code here
		assertEquals("a", result);
	}

	/**
	 * Run the String wrap(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_4()
		throws Exception {
		String text = "";
		int width = 1;
		String lineSeparator = "";

		String result = StringUtil.wrap(text, width, lineSeparator);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String wrap(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_5()
		throws Exception {
		String text = "";
		int width = 1;
		String lineSeparator = "";

		String result = StringUtil.wrap(text, width, lineSeparator);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String wrap(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_6()
		throws Exception {
		String text = "";
		int width = 1;
		String lineSeparator = "";

		String result = StringUtil.wrap(text, width, lineSeparator);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String wrap(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_7()
		throws Exception {
		String text = "";
		int width = 1;
		String lineSeparator = "";

		String result = StringUtil.wrap(text, width, lineSeparator);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String wrap(String,int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	@Test
	public void testWrap_8()
		throws Exception {
		String text = "";
		int width = 1;
		String lineSeparator = "";

		String result = StringUtil.wrap(text, width, lineSeparator);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 13.04.15 23:38
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
	 * @generatedBy CodePro at 13.04.15 23:38
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
	 * @generatedBy CodePro at 13.04.15 23:38
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(StringUtilTest.class);
	}
}
