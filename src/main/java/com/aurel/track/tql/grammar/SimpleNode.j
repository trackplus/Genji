/*
 * $Id: SimpleNode.j 3495 2012-03-15 17:23:21Z tamas $
 */

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


	public void jjtOpen ()
	{
	}


	public void jjtClose ()
	{
	}


	public void jjtSetParent (Node n)
	{
		parent = n;
	}


	public Node jjtGetParent ()
	{
		return parent;
	}


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


	public Node jjtGetChild (int i)
	{
		return children[i];
	}


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


	public String toString()
	{
		return TqlParserTreeConstants.jjtNodeName[id] + (value != null ? ": " + value : "") + (timeUnit != -1 ? ": " + timeUnit : "") + " " + hashCode();
	}
}
