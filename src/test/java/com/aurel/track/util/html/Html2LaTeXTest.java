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

package com.aurel.track.util.html;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.*;

import static org.junit.Assert.*;

import com.aurel.track.persist.TProject;
import com.aurel.track.util.html.*;
import com.trackplus.track.util.html.Html2LaTeX;

/**
 * The class <code>TagReplacerTest</code> contains tests for the class <code>{@link TagReplacer}</code>.
 *
 * @generatedBy CodePro at 19.03.15 01:28
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class Html2LaTeXTest {
    private static String html1 = "<h2 id=\"workItem_synopsis_5\">The User Interface</h2>\r\n\r\n<p><span class=\"ph\"><span class=\"ph\">Genji</span></span> is a powerful web-based issue tracker and Scrum tool for teams. <span class=\"ph\"><span class=\"ph\">Genji</span></span> supports large numbers of projects, tasks, and users. <span class=\"ph\"><span class=\"ph\">Genji</span></span> is highly configurable. To use <span class=\"ph\"><span class=\"ph\">Genji</span></span> you just need a standard web browser.</p>\r\n\r\n<p><span class=\"ph\"><span class=\"ph\">Genji</span></span> is used for</p>\r\n\r\n<ul>\r\n\t<li>bug and issue tracking in software and hardware development</li>\r\n\t<li>Scrum tool and Kanban board</li>\r\n\t<li>mile stone tracking</li>\r\n\t<li>meeting agendas and protocols with follow-up tracking of action items</li>\r\n</ul>\r\n\r\n"
    		+"<p>Compared to managing tasks and projects with spread sheets, <span class=\"ph\"><span class=\"ph\">Genji</span></span> will give you the following benefits:</p>\r\n\r\n"
    		+"<ul>\r\n\t<li>[issue 8 /]</li>\r\n\t<li>Fully hierarchical, permits you to handle thousands of projects with thousands of tasks with thousands of people</li>\r\n\t<li>Powerful role-based access control, you only make visible to others what they are supposed to see</li>\r\n\t<li>Highly configurable, it will exactly fit your terminology and needs</li>\r\n\t<li>Full tracking of all activities, all changes are recorded</li>\r\n</ul>\r\n\r\n<p>Compared to using a standard issue tracker or bug tracker, <span class=\"ph\"><span class=\"ph\">Genji</span></span> will give you the following benefits:</p>\r\n\r\n<ul>\r\n\t<li>Fully hierarchical, permits you to handle thousands of projects with thousands of tasks with thousands of people</li>\r\n\t<li>Support for agile methods like Scrum and Kanban</li>\r\n\t<li>Powerful role-based access control down to single properties You only make visible to others what they are supposed to see</li>\r\n\t<li>Highly configurable via the user interface. You can make it exactly fit your terminology and needs without programming skills</li>\r\n\t<li>Support for top-down and bottom-up planning, effort tracking, and project monitoring</li>\r\n\t<li>Full tracking of all activities, all changes are recorded</li>\r\n</ul>\r\n\r\n<p><span class=\"ph\"><span class=\"ph\">Genji</span></span> implements proven project management methods and build upon methods used for personal productivity management. <span class=\"ph\"><span class=\"ph\">Genji</span></span> can be configured to a wide extend, and it therefore takes a little time to understand how to adapt it to a particular application scenario.</p>\r\n\r\n<p> </p>\r\n\r\n<div><img alt=\"\" height=\"342\" src=\"http://localhost:8080/track/help/WebHelp/Topics/01GettingStarted/figures/overview.png\" width=\"508\" /></div>\r\n\r\n<div>\r\n<h2 id=\"workItem_synopsis_7\">Installing <span class=\"ph\"><span class=\"ph\">Genji</span></span></h2>\r\n\r\n<div>\r\n<p>Unless a server is provided to you, you need to install <span class=\"ph\"><span class=\"ph\">Genji</span></span> on your own personal computer or on another computer. Use the Windows installer for an easy installation.</p>\r\n\r\n<p>To install <span class=\"ph\"><span class=\"ph\">Genji</span></span> on Unix based operating systems or with a MySQL, Oracle, MS SSQL Server, or DB2 database, follow the \u201E<a class=\"xref\" href=\"http://www.trackplus.com/task-management-support/documentation.html\" target=\"_blank\"><span class=\"ph\"><span class=\"ph\">Genji</span></span> Installation Guide</a>\u201C.</p>\r\n\r\n<p>You can install the <span class=\"ph\"><span class=\"ph\">Genji</span></span> server on any computer system supporting Java 7 or up. It runs on all Windows operating systems, Linux, Mac OS/X, Solaris, and others.</p>\r\n\r\n<h3 id=\"workItem_synopsis_14\">Details on Installation</h3>\r\n\r\n<div>\r\n<p>Unless a server is provided to you, you need to install <span class=\"ph\">Genji</span> on your own personal computer or on another computer. Use the Windows installer for an easy installation.</p>\r\n\r\n<p>To install <span class=\"ph\">Genji</span> on Unix based operating systems or with a MySQL, Oracle, MS SSQL Server, or DB2 database, follow the \u201E<a class=\"xref\" href=\"http://www.trackplus.com/task-management-support/documentation.html\" target=\"_blank\"><span class=\"ph\"><span class=\"ph\">Genji</span></span> Installation Guide</a>\u201C.</p>\r\n\r\n<p>You can install the <span class=\"ph\">Genji</span> server on any computer system supporting Java 7 or up. It runs on all Windows operating systems, Linux, Mac OS/X, Solaris, and others.</p>\r\n\r\n<h4 id=\"workItem_synopsis_15\">Even more Details</h4>\r\n\r\n<p>MySQL, Oracle, MS SSQL Server, or DB2 database, follow the \u201E<a class=\"xref\" href=\"http://www.trackplus.com/task-management-support/documentation.html\" target=\"_blank\"><span class=\"ph\"><span class=\"ph\">Genji</span></span> Installation Guide</a>\u201C.</p>\r\n\r\n<p>You can install the <span class=\"ph\">Genji</span> server on any computer system supporting Java 7 or up. It runs on all Windows operating systems, Linux, Mac OS/X, Sol</p>\r\n</div>\r\n</div>\r\n</div>\r\n";
    private static String html2 = "\t<p>Compared to \"managing\" tasks <inlineLink>and projects with spread sheets</inlineLink>, <span class=\"ph\"><span class=\"ph\">Genji</span></span> will give you the following benefits:</p>\r\n\r\n\t<ul>\r\n\t\t<li>[issue 8/]</li>\r\n\t\t<li>Fully hierarchical, permits you to handle thousands of projects with thousands of tasks with thousands of people</li>\r\n\t\t<li>Powerful role-based access control, ";
    private static String html3 = "<p>All essential \"products\" are required, and while there is little to omit there should be no significant gain when adding \"products\" for in the table type \"software design document\". In find a \"software design document controller\", a \"software design document user interface\", etc. Some product types however just have a single instance, like this document or the project plan.</p>";
    private static String html4 = "<p>This &lt;Configuration&gt; ° ! \"dd\" §4 %def &amp; / (zu) {paran} [dd] \\hgz = ? ´hh´&nbsp; #kjkl $DEV ^zz ^ hjh</p>";
    private static String html5 = "<p>This Word dhsgggggggggggggggggggggggggggggggggg ggggggg word word2 <span style=\"color:#FF0000;\">SPANNED STUFF</span> the tail</p>";
    private static String html6 = 
//    "<p>All products — as far as practical and possible — shall be collected and maintained in product libraries. There are two libraries defined in this project:</p>\n" +

    "<ol>\n" +
 //   "	<li>System engineering documents and process related documents shall be managed using the <span style=\"color:#FF0000\"><strong>Genji project workspace Wiki</strong></span> as much as possible</li>\n"+
    "	<li>All other documents, code, build scripts, etc. shall be maintained in the projects <span style=\"color:#FF0000\">Subversion repository</span>.</li>\n"+
    "</ol>\n";

    /**
	 */
	@Test
	public void testHtml2LaTeXTest_1()
		throws Exception {
		
		String result = Html2LaTeX.getLaTeX(html6);

		System.err.println(result);
		
		// add additional test code here
		assertNotNull(result);
	}
	
	//@Test
	public void testHtml2LaTeXTest_2()
		throws Exception {
		
		Pattern pattern = Pattern.compile(".*\\[issue ([0-9]+)/\\]");
		
		String s = html2;

        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
        	Integer oid = Integer.valueOf(matcher.group(1));
        	Integer inlineOid = Integer.valueOf(matcher.group(1));
        	s = s.replaceAll("\\[issue [0-9]+\\/\\]", "XXXX "+inlineOid + " XXXX");
        };
        
		String result = Html2LaTeX.getLaTeX(s);

		System.err.println(result);
		
		// add additional test code here
		assertNotNull(result);
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
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(Html2LaTeX.class);
	}
}
