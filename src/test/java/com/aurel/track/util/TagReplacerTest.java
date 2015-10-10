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

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import com.aurel.track.persist.TProject;

/**
 * The class <code>TagReplacerTest</code> contains tests for the class <code>{@link TagReplacer}</code>.
 *
 * @generatedBy CodePro at 19.03.15 01:28
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class TagReplacerTest {
	/**
	 * Run the TagReplacer(Locale) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testTagReplacer_1()
		throws Exception {
		Locale _locale = Locale.getDefault();

		TagReplacer result = new TagReplacer(_locale);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String formatDescription(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testFormatDescription_1()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String source = "";

		String result = fixture.formatDescription(source);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String formatDescription(String,boolean,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testFormatDescription_2()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String source = null;
		boolean simple = true;
		boolean export = true;
		boolean useProjectSpecificID = true;

		String result = fixture.formatDescription(source, simple, export, useProjectSpecificID);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String formatDescription(String,boolean,boolean,boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testFormatDescription_3()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String source = "";
		boolean simple = true;
		boolean export = true;
		boolean useProjectSpecificID = true;

		String result = fixture.formatDescription(source, simple, export, useProjectSpecificID);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the List<Integer> gatherIssueLinks(StringBuilder) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testGatherIssueLinks_1()
		throws Exception {
		StringBuilder src = new StringBuilder();

		List<Integer> result = TagReplacer.gatherIssueLinks(src);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Integer> gatherIssueLinks(StringBuilder) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testGatherIssueLinks_2()
		throws Exception {
		StringBuilder src = new StringBuilder();

		List<Integer> result = TagReplacer.gatherIssueLinks(src);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Integer> gatherIssueLinks(StringBuilder) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testGatherIssueLinks_3()
		throws Exception {
		StringBuilder src = new StringBuilder();

		List<Integer> result = TagReplacer.gatherIssueLinks(src);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Integer> gatherIssueLinks(StringBuilder) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testGatherIssueLinks_4()
		throws Exception {
		StringBuilder src = new StringBuilder();

		List<Integer> result = TagReplacer.gatherIssueLinks(src);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Integer> gatherIssueLinks(StringBuilder) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testGatherIssueLinks_5()
		throws Exception {
		StringBuilder src = new StringBuilder();

		List<Integer> result = TagReplacer.gatherIssueLinks(src);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<Integer> gatherIssueLinks(StringBuilder) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testGatherIssueLinks_6()
		throws Exception {
		StringBuilder src = new StringBuilder();

		List<Integer> result = TagReplacer.gatherIssueLinks(src);

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the String processSquareBracketsTags(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testProcessSquareBracketsTags_1()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String toFormat = null;

		String result = fixture.processSquareBracketsTags(toFormat);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String processSquareBracketsTags(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testProcessSquareBracketsTags_2()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String toFormat = "";

		String result = fixture.processSquareBracketsTags(toFormat);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String removeListLineBreaks(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testRemoveListLineBreaks_1()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String input = "";

		String result = fixture.removeListLineBreaks(input);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String replace(String,TProject) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplace_1()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String input = null;
		TProject _project = new TProject();

		String result = fixture.replace(input, _project);

		// add additional test code here
		assertEquals(null, result);
	}

	/**
	 * Run the String replace(String,TProject) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplace_2()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String input = "";
		TProject _project = new TProject();

		String result = fixture.replace(input, _project);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the boolean replaceIssueLinks(StringBuilder,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplaceIssueLinks_1()
		throws Exception {
		StringBuilder src = new StringBuilder();
		Map<Integer, Integer> workItemIDsMap = new Hashtable();

		boolean result = TagReplacer.replaceIssueLinks(src, workItemIDsMap);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean replaceIssueLinks(StringBuilder,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplaceIssueLinks_2()
		throws Exception {
		StringBuilder src = new StringBuilder();
		Map<Integer, Integer> workItemIDsMap = new Hashtable();

		boolean result = TagReplacer.replaceIssueLinks(src, workItemIDsMap);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean replaceIssueLinks(StringBuilder,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplaceIssueLinks_3()
		throws Exception {
		StringBuilder src = new StringBuilder();
		Map<Integer, Integer> workItemIDsMap = new Hashtable();

		boolean result = TagReplacer.replaceIssueLinks(src, workItemIDsMap);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean replaceIssueLinks(StringBuilder,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplaceIssueLinks_4()
		throws Exception {
		StringBuilder src = new StringBuilder();
		Map<Integer, Integer> workItemIDsMap = new Hashtable();

		boolean result = TagReplacer.replaceIssueLinks(src, workItemIDsMap);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean replaceIssueLinks(StringBuilder,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplaceIssueLinks_5()
		throws Exception {
		StringBuilder src = new StringBuilder();
		Map<Integer, Integer> workItemIDsMap = new Hashtable();

		boolean result = TagReplacer.replaceIssueLinks(src, workItemIDsMap);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean replaceIssueLinks(StringBuilder,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplaceIssueLinks_6()
		throws Exception {
		StringBuilder src = new StringBuilder();
		Map<Integer, Integer> workItemIDsMap = new Hashtable();

		boolean result = TagReplacer.replaceIssueLinks(src, workItemIDsMap);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean replaceIssueLinks(StringBuilder,Map<Integer,Integer>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testReplaceIssueLinks_7()
		throws Exception {
		StringBuilder src = new StringBuilder();
		Map<Integer, Integer> workItemIDsMap = new Hashtable();

		boolean result = TagReplacer.replaceIssueLinks(src, workItemIDsMap);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the void setContextPath(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testSetContextPath_1()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String contextPath = null;

		fixture.setContextPath(contextPath);

		// add additional test code here
	}

	/**
	 * Run the void setContextPath(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testSetContextPath_2()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String contextPath = "";

		fixture.setContextPath(contextPath);

		// add additional test code here
	}

	/**
	 * Run the void setContextPath(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testSetContextPath_3()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String contextPath = null;

		fixture.setContextPath(contextPath);

		// add additional test code here
	}

	/**
	 * Run the void setContextPath(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Test
	public void testSetContextPath_4()
		throws Exception {
		TagReplacer fixture = new TagReplacer(Locale.getDefault());
		fixture.replace("", new TProject());
		String contextPath = "/";

		fixture.setContextPath(contextPath);

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
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
	 * @generatedBy CodePro at 19.03.15 01:28
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
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(TagReplacerTest.class);
	}
}
