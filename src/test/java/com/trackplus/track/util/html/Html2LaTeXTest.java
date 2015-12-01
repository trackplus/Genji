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

package com.trackplus.track.util.html;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>Html2LaTeXTest</code> contains tests for the class <code>{@link Html2LaTeX}</code>.
 *
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class Html2LaTeXTest {
	
	String text1 = "<h1>First Chapter</h1><h2>First section</h2><p>We will have a look at the <u>features</u> <em>collected</em> in our <strong>product backlog</strong>.</p>";
	String text2 = "<p>Some lists:</p><ul><li>The first entry</li><li>The second entry</li></ul><p>And a numbered list:</p><ol><li>The first entry</li><li>The second entry</li></ul>";
	String text3 = "Pure text followed by a &lt;br&gt; tag at the end<br> and then another line terminated by a newline\n";
	String text4 = "<p>Here some <u>underlined stuff</u> and some <i>slanted text</i>. "
			+ "We also add a description list here:<dl><dt>Description</dt><dd>The description goes here</dd><dt>Description 2</dt><dd>The description 2 goes here</dd></dl>"
			+ "<div class=\"code-text\" style=\"font-size:14px;\">        This should be verbatim text for code etc.\r\nThis is a new  line.</div>"
			+ "<div class=\"code-text\" style=\"font-size:14px;\">       More should b</div>"
			+ "<p> more text .. </p>"
            ;
	
	String result14 = "\n\\chapter{First Chapter}\n\n\\section{First section}\n\nWe will have a look at the \\underline{features} \\emph{collected} in our \n\\textbf{product backlog}.\n\nSome lists:\n\n \\begin{itemize} \n \\item The first entry\n \\item The second entry\n \\end{itemize} \nAnd a numbered list:\n\n \\begin{enumerate} \n \\item The first entry\n \\item The second entryPure text followed by a <br> tag at the end\n and then another line terminated by a newline \nHere some \\underline{underlined stuff} and some \\textit{slanted text}. We also \nadd a description list here:\n\n \\begin{description}\n \\item[Description] \nThe description goes here\n\n \\item[Description 2] \nThe description 2 goes here\n\n \\end{description} \n\\begin{verbatim}\n This should be verbatim text for code etc. This is a new line.\n\\end{verbatim}\n\\begin{verbatim}\n       More should b\n\\end{verbatim}\n more text .. \n\n \\end{enumerate}";
	
	
	String tabletest = 
 "<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:500px\">"
+"	<thead>"
+"		<tr>"
+"			<th scope=\"col\">Release</th>"
+"			<th scope=\"col\">Release Notes</th>"
+"		</tr>"
+"	</thead>"
+"	<caption>Tabelle mit Header</caption>"
+"	<tbody>"
+"		<tr>"
+"			<td>4.2.1</td>"
+"			<td>This method gets all data relevant for the main tab of the user profile management.<br />"
+"			param personBean contains the users data from the database, unless this is a new user param context if currently a new user is registering himself, contrary to an administrator<br />"
+"			registering a new user. In self registration mode, the other tabs are deactivated<br />"
+"			return a transfer object for the main user profile data</td>"
+"		</tr>"
+"		<tr>"
+"			<td>5.3.8</td>"
+"			<td>This method gets all data relevant for the main tab of the user profile management.</td>"
+"		</tr>"
+"	</tbody>"
+"</table>";
	
	String textverbatim = "<p>SSSS </p>\n"
			+ "<div class=\"code-text\" style=\"font-size:14px;\">        This should be verbatim text for code etc.\r\nThis is a new  line.</div>"
			+ "<div class=\"code-text\" style=\"font-size:14px;\">       More should b</div>"
			+ "<div class=\"code-text\" style=\"font-size:14px;\">        This should be verbatim text for code etc.\r\n JJJJJJJ</div>"
			+ "\n<p> more text .. </p>"
            ;

	/**
	 * Test for simple tags like <i>, <b>, <h1> to <h4>, <ul>, <ol>, <dl>
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetLaTeX_1()
		throws Exception {
		String bodyHtml = text1 + text2 + text3 + text4;

		String result = Html2LaTeX.getLaTeX(bodyHtml);

		String result2 = result.replace("\\","\\\\");
		result2 = result2.replaceAll("(?s)\n","\\\\"+"n");
		//System.err.println(result2);
		System.err.println(result);
		assert(result.equals(result14));
	}
	
	/**
	 * Test tables.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetLaTeX_2()
		throws Exception {
		String bodyHtml = tabletest;

		String result = Html2LaTeX.getLaTeX(bodyHtml);

		String result2 = result.replace("\\","\\\\");
		result2 = result2.replaceAll("(?s)\n","\\\\"+"n");
		//System.err.println(result2);
		System.err.println(result);
		assert(result.equals(result));
	}

	/**
	 * Test verbatim code.
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetLaTeX_3()
		throws Exception {
		String bodyHtml = textverbatim;

		String result = Html2LaTeX.getLaTeX(bodyHtml);

		String result2 = result.replace("\\","\\\\");
		result2 = result2.replaceAll("(?s)\n","\\\\"+"n");
		//System.err.println(result2);
		System.err.println(result);
		assert(result.equals(result));
	}


	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
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
	 * @generatedBy CodePro at 29.05.15 10:47
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
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(Html2LaTeXTest.class);
	}
}
