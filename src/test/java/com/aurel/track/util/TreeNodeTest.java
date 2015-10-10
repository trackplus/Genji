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

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>TreeNodeTest</code> contains tests for the class <code>{@link TreeNode}</code>.
 *
 * @generatedBy CodePro at 19.03.15 01:23
 * @author friedj
 * @version $Revision: 1.0 $
 */
public class TreeNodeTest {
	/**
	 * Run the TreeNode() constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testTreeNode_1()
		throws Exception {

		TreeNode result = new TreeNode();

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getChildren());
		assertEquals(null, result.getLabel());
		assertEquals(null, result.getIcon());
		assertEquals(null, result.getId());
		assertEquals(Boolean.TRUE, result.getLeaf());
		assertEquals(true, result.isSelectable());
		assertEquals(null, result.getChecked());
	}

	/**
	 * Run the TreeNode(String,String) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testTreeNode_2()
		throws Exception {
		String id = "";
		String label = "";

		TreeNode result = new TreeNode(id, label);

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getChildren());
		assertEquals("", result.getLabel());
		assertEquals(null, result.getIcon());
		assertEquals("", result.getId());
		assertEquals(Boolean.TRUE, result.getLeaf());
		assertEquals(true, result.isSelectable());
		assertEquals(null, result.getChecked());
	}

	/**
	 * Run the TreeNode(String,String,String) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testTreeNode_3()
		throws Exception {
		String id = "";
		String label = "";
		String icon = "";

		TreeNode result = new TreeNode(id, label, icon);

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getChildren());
		assertEquals("", result.getLabel());
		assertEquals("", result.getIcon());
		assertEquals("", result.getId());
		assertEquals(Boolean.TRUE, result.getLeaf());
		assertEquals(true, result.isSelectable());
		assertEquals(null, result.getChecked());
	}

	/**
	 * Run the int compareTo(TreeNode) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testCompareTo_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		TreeNode o = null;

		int result = fixture.compareTo(o);

		// add additional test code here
		assertEquals(1, result);
	}

	/**
	 * Run the int compareTo(TreeNode) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testCompareTo_2()
		throws Exception {
		TreeNode fixture = new TreeNode("", (String) null);
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		TreeNode o = new TreeNode("", (String) null);

		int result = fixture.compareTo(o);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int compareTo(TreeNode) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testCompareTo_3()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		TreeNode o = new TreeNode("", "");

		int result = fixture.compareTo(o);

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testEquals_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		Object obj = null;

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testEquals_2()
		throws Exception {
		TreeNode fixture = new TreeNode((String) null, "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		Object obj = new TreeNode();

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testEquals_3()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		Object obj = new TreeNode("", "");

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testEquals_4()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		Object obj = new TreeNode("", "");

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the Boolean getChecked() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetChecked_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		Boolean result = fixture.getChecked();

		// add additional test code here
		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the List<TreeNode> getChildren() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetChildren_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		List<TreeNode> result = fixture.getChildren();

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the String getIcon() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetIcon_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		String result = fixture.getIcon();

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String getId() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetId_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		String result = fixture.getId();

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String getLabel() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetLabel_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		String result = fixture.getLabel();

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the Boolean getLeaf() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetLeaf_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf((Boolean) null);
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		Boolean result = fixture.getLeaf();

		// add additional test code here
		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the Boolean getLeaf() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetLeaf_2()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf((Boolean) null);
		fixture.setChildren(null);
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		Boolean result = fixture.getLeaf();

		// add additional test code here
		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the Boolean getLeaf() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testGetLeaf_3()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		Boolean result = fixture.getLeaf();

		// add additional test code here
		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the int hashCode() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testHashCode_1()
		throws Exception {
		TreeNode fixture = new TreeNode((String) null, "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		int result = fixture.hashCode();

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the int hashCode() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testHashCode_2()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		int result = fixture.hashCode();

		// add additional test code here
		assertEquals(0, result);
	}

	/**
	 * Run the boolean isSelectable() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testIsSelectable_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		boolean result = fixture.isSelectable();

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Run the boolean isSelectable() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testIsSelectable_2()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(false);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");

		boolean result = fixture.isSelectable();

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the void setChecked(Boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testSetChecked_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		Boolean checked = new Boolean(true);

		fixture.setChecked(checked);

		// add additional test code here
	}

	/**
	 * Run the void setChildren(List<TreeNode>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testSetChildren_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		List<TreeNode> children = new ArrayList();

		fixture.setChildren(children);

		// add additional test code here
	}

	/**
	 * Run the void setIcon(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testSetIcon_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		String icon = "";

		fixture.setIcon(icon);

		// add additional test code here
	}

	/**
	 * Run the void setId(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testSetId_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		String id = "";

		fixture.setId(id);

		// add additional test code here
	}

	/**
	 * Run the void setLabel(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testSetLabel_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		String label = "";

		fixture.setLabel(label);

		// add additional test code here
	}

	/**
	 * Run the void setLeaf(Boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testSetLeaf_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		Boolean leaf = new Boolean(true);

		fixture.setLeaf(leaf);

		// add additional test code here
	}

	/**
	 * Run the void setSelectable(boolean) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	@Test
	public void testSetSelectable_1()
		throws Exception {
		TreeNode fixture = new TreeNode("", "");
		fixture.setLeaf(new Boolean(true));
		fixture.setChildren(new ArrayList());
		fixture.setSelectable(true);
		fixture.setChecked(new Boolean(true));
		fixture.setIcon("");
		boolean selectable = true;

		fixture.setSelectable(selectable);

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 19.03.15 01:23
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
	 * @generatedBy CodePro at 19.03.15 01:23
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
	 * @generatedBy CodePro at 19.03.15 01:23
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(TreeNodeTest.class);
	}
}
