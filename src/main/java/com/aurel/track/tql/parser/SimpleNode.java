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


package com.aurel.track.tql.parser;

/**
 * @author Clemens Fuchslocher &lt;clfuit00@fht-esslingen.de&gt;
 */
public class SimpleNode implements Node
{
	protected int id;
	protected Node parent;
	protected Node[] children;
	protected TqlParser parser;

	private int type = -1;
	private int operator = -1;
	private int timeUnit = -1;
	private int dateOperator = -1;
	private int dateOperatorTimeUnit = -1;
	private String dateOperatorSign = null;
	private String value = null;


	public SimpleNode (int i)
	{
		id = i;
	}


	public SimpleNode (TqlParser p, int i)
	{
		this (i);
		parser = p;
	}


	@Override
	public void jjtOpen ()
	{
	}


	@Override
	public void jjtClose ()
	{
	}


	@Override
	public void jjtSetParent (Node n)
	{
		parent = n;
	}


	@Override
	public Node jjtGetParent ()
	{
		return parent;
	}


	@Override
	public void jjtAddChild (Node n, int i)
	{
		if (children == null)
		{
			children = new Node[i + 1];
		}
		else if (i >= children.length)
		{
			Node c[] = new Node[i + 1];
			System.arraycopy (children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}


	@Override
	public Node jjtGetChild (int i)
	{
		return children[i];
	}


	@Override
	public int jjtGetNumChildren ()
	{
		return (children == null) ? 0 : children.length;
	}


	public String toString (String prefix)
	{
		return prefix + toString ();
	}


	public void dump (String prefix)
	{
		System.out.println (toString(prefix));
		if (children != null)
		{
			for (int i = 0; i < children.length; ++i)
			{
				SimpleNode n = (SimpleNode) children[i];
				if (n != null)
				{
					n.dump (prefix + " ");
				}
			}
		}
	}


	public StringBuffer dumpGraphViz ()
	{
		StringBuffer dot = new StringBuffer ("\n");
		dot.append ("digraph DirectedGraph {\n" +
			"/*\n" +
			" * 'GraphViz - Open Source Graph Drawing Software'\n" +
			" * <http://www.research.att.com/sw/tools/graphviz/>\n" +
			" * \n" +
			" * $ dot -T png -o ast.png ast.dot && display ast.png\n" +
			" */\n");
		dumpGraphVizTraverse (new int[1], dot);

		return dot.append ("}\n");
	}


	private String dumpGraphVizTraverse (int[] unique, StringBuffer dot)
	{
		String node = TqlParserTreeConstants.jjtNodeName[id] + ++unique[0];
		dot.append ("\t" + node + " [ label = \"" + this.value + "\" ]" + "\n");

		if (children == null)
		{
			return node;
		}

		for (int i = 0; i < children.length; i++)
		{
			SimpleNode n = (SimpleNode) children[i];
			if (n != null)
			{
				unique[0]++;
				String child = n.dumpGraphVizTraverse (unique, dot);
				dot.append ("\t" + node + " -> " + child + "\n");
			}
		}

		return node;
	}


	public void setType (int type)
	{
		this.type = type;
	}


	public int getType ()
	{
		return this.type;
	}


	public void setOperator (int operator)
	{
		this.operator = operator;
	}


	public int getOperator ()
	{
		return operator;
	}


	public void setTimeUnit (int timeUnit)
	{
		this.timeUnit = timeUnit;
	}


	public int getTimeUnit ()
	{
		return this.timeUnit;
	}


	public void setDateOperator (int operator)
	{
		this.dateOperator = operator;
	}


	public int getDateOperator ()
	{
		return dateOperator;
	}


	public void setDateOperatorTimeUnit (int timeUnit)
	{
		dateOperatorTimeUnit = timeUnit;
	}


	public int getDateOperatorTimeUnit ()
	{
		return dateOperatorTimeUnit;
	}


	public void setDateOperatorSign (String sign)
	{
		this.dateOperatorSign = sign;
	}


	public String getDateOperatorSign ()
	{
		return dateOperatorSign;
	}


	public void setValue (String value)
	{
		this.value = value;
	}


	public String getValue ()
	{
		return this.value;
	}


	@Override
	public String toString()
	{
		return TqlParserTreeConstants.jjtNodeName[id] + (value != null ? ": " + value : "") + (timeUnit != -1 ? ": " + timeUnit : "") + " " + hashCode();
	}
}
