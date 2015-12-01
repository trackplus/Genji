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


package com.aurel.track.tql;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aurel.track.tql.exceptions.TqlJoinSchemaClassNotFoundException;
import com.aurel.track.tql.exceptions.TqlJoinSchemaFieldNotFoundException;
import com.aurel.track.tql.exceptions.TqlJoinSchemaXmlNotFoundException;
import com.aurel.track.tql.exceptions.TqlJoinSchemaXmlParseException;


/**
 * <p>
 * This class is used for mapping the alias names of a TQL statement into
 * the appropriate Torque field names. This also includes the information
 * about which joins must be made to get proper results. For example, if
 * a TQL statement is using the alias <code>Priority</code> then the
 * following things must be available:
 * </p>
 *
 * <ul>
 * <li>The real Torque field name for <code>Priority</code>,
 * <li>and the required joins</li>
 * </ul>
 *
 * <p>
 * For the alias <code>Priority</code>, the name of the Torque field would
 * be <code>TPriorityPeer.LABEL</code></li>. Also there must be a join between
 * <code>TWorkItemPeer.PRIORITYKEY</code> and <code>TPriorityPeer.PKEY</code>.
 * </p>
 *
 * <p>
 * The whole mapping is defined in an external XML document. For the above
 * example the entry in the <code>TqlJoinSchema.xml</code> file would looks
 * like this:
 * </p>
 *
 * <pre>
 * &lt;join-schema&gt;
 *    &lt;torque-persist-package&gt;com.aurel.track.persist&lt;/torque-persist-package&gt;
 *    ...
 *    &lt;entity&gt;
 *      &lt;alias&gt;Priority&lt;/alias&gt;
 *      &lt;field&gt;TPriorityPeer.LABEL&lt;/field&gt;
 *      &lt;join-parameter&gt;
 *          &lt;join&gt;
 *              &lt;left&gt;TWorkItemPeer.PRIORITYKEY&lt;/left&gt;
 *              &lt;right&gt;TPriorityPeer.PKEY&lt;/right&gt;
 *          &lt;/join&gt;
 *      &lt;/join-parameter&gt;
 *   &lt;/entity&gt;
 *   ...
 * &lt;/join-schema&gt;
 * </pre>
 *
 * @author Clemens Fuchslocher &lt;clfuit00@fht-esslingen.de&gt;
 */
public class TqlJoinSchema
{
	private static Hashtable aliasToJoin = new Hashtable ();
	private static Hashtable aliasToField = new Hashtable ();
	private static Hashtable aliasToLocalizationBeanIdentifier = new Hashtable ();

	private static final Logger LOGGER = LogManager.getLogger (TqlJoinSchema.class);

	private static String joinSchemaXml = "TqlJoinSchema.xml";
	private static String torquePersistPackageName = "com.aurel.track.persist";

	public static final String JOIN_SCHEMA = "joinSchema";


	/**
	 * This class represents a simple join between two related tables.
	 *
	 * @author Clemens Fuchslocher <clfuit00@fht-esslingen.de>
	 */
	public static class JoinParameter
	{
		public String right;
		public String left;

		public JoinParameter (String right, String left)
		{
			this.right = right;
			this.left = left;
		}
	}


	/**
	 * Constructs a new TqlJoinSchema object with the mapping rules
	 * from the <code>TqlJoinSchema.xml</code> file.
	 */
	public TqlJoinSchema () throws TqlJoinSchemaXmlNotFoundException, TqlJoinSchemaXmlParseException, TqlJoinSchemaClassNotFoundException, TqlJoinSchemaFieldNotFoundException
	{
		// Open a stream for reading in the TqlJoinSchema.xml file.
		InputStream schema = TqlJoinSchema.class.getClassLoader ().getResourceAsStream ("TqlJoinSchema.xml");
		if (schema == null)
		{
			LOGGER.error ("Can't find the '" + joinSchemaXml + "' file.");
			throw new TqlJoinSchemaXmlNotFoundException (joinSchemaXml);
		}

		DocumentBuilderFactory factory = null;
		try
		{
			factory = DocumentBuilderFactory.newInstance ();
		}
		catch (FactoryConfigurationError e)
		{
			LOGGER.error (e);
			e.printStackTrace ();
		}

		if (factory == null) {
			return;
		}
		// Well-Formed XML is good enough for this purpose. (No DTD or XML Schema.)
		factory.setValidating (false);
		// There are no namespaces in the XML document.
		factory.setNamespaceAware (false);

		// Create a DOM tree out of the XML stream.
		Document root = null;
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder ();
			root = builder.parse (schema);
		}
		catch (Exception e)
		{
			LOGGER.error (e);
			e.printStackTrace ();
			throw new TqlJoinSchemaXmlParseException (joinSchemaXml);
		}

		/*
		 * Now we are walking through the DOM tree and collect all the
		 * needed data from the nodes. The XML file (the DOM tree) has
		 * the following structure:
		 *
		 * <join-schema>
		 *     <torque-persist-package>...</torque-persist-package>
		 *
		 *     <entity>
		 *         <alias>...</alias>
         *         <field>...</field>
         *         <localization-bean-identifier>....</localization-bean-identifier>
         *         <join-parameter>
         *             <join>
         *                 <left>...</left>
         *                 <right>...</right>
         *             </join>
         *             <join>
         *             ...
         *             </join>
         *         </join-parameter>
		 *     </entity>
		 *
		 *     <entity>
		 *     ...
		 *     </entity>
		 * </join-schema>
		 */

		// Search for a node named join-schema.
		NodeList children = root.getChildNodes ();
		for (int n = 0; n < children.getLength (); n++)
		{
			Node node = children.item (n);
			switch (node.getNodeType ())
			{
				case Node.ELEMENT_NODE:
				{
					String nodeName = node.getNodeName ();
					if (nodeName.equals ("join-schema"))
					{
						parseJoinSchema (node);
					}
					break;
				}
			}
		}
	}


	/*
	 * The join-schema node has two types of child nodes:
	 *     o entity nodes
	 *     o torque-persist-package nodes (leaf)
	 */
	private void parseJoinSchema (Node root) throws TqlJoinSchemaClassNotFoundException, TqlJoinSchemaFieldNotFoundException
	{
		NodeList children = root.getChildNodes ();
		for (int n = 0; n < children.getLength (); n++)
		{
			Node node = children.item (n);
			switch (node.getNodeType ())
			{
				case Node.ELEMENT_NODE:
				{
					String nodeName = node.getNodeName ();
					if (nodeName.equals ("entity"))
					{
						parseEntity (node);
					}
					else if (nodeName.equals ("torque-persist-package"))
					{
						torquePersistPackageName = parseText (node);
					}
					break;
				}
			}
		}
	}


	/*
	 * The entity node has three types of child nodes:
	 *     o alias node (leaf)
	 *     o field node (leaf)
	 *     o a localization bean identifier (leaf)
	 *     o join-parameter node
	 */
	private void parseEntity (Node root) throws TqlJoinSchemaClassNotFoundException, TqlJoinSchemaFieldNotFoundException
	{
		String alias = "";
		String field = "";
		String beanIdentifier = "";
		JoinParameter[] joins = new JoinParameter[0];

		NodeList children = root.getChildNodes ();
		for (int n = 0; n < children.getLength (); n++)
		{
			Node node = children.item (n);
			switch (node.getNodeType ())
			{
				case Node.ELEMENT_NODE:
				{
					String nodeName = node.getNodeName ();
					if (nodeName.equals ("alias"))
					{
						alias = parseText (node);
					}
					else if (nodeName.equals ("field"))
					{
						field = torqueFieldToSqlField (parseText (node));
					}
					else if (nodeName.equals ("localization-bean-identifier"))
					{
						beanIdentifier = getLocalizationBeanIdentifier (parseText (node));
					}
					else if (nodeName.equals ("join-parameter"))
					{
						joins = parseJoinParameter (node);
					}
					break;
				}
			}
		}

		aliasToField.put (alias, field);
		aliasToJoin.put (alias, joins);
		aliasToLocalizationBeanIdentifier.put (alias, beanIdentifier);
	}


	/*
	 * The join-parameter node has one child node type:
	 *     o join node
	 */
	private JoinParameter[] parseJoinParameter (Node root) throws TqlJoinSchemaClassNotFoundException, TqlJoinSchemaFieldNotFoundException
	{
		Vector joins = new Vector ();

		NodeList children = root.getChildNodes ();
		for (int n = 0; n < children.getLength (); n++)
		{
			Node node = children.item (n);
			switch (node.getNodeType ())
			{
				case Node.ELEMENT_NODE:
				{
					String nodeName = node.getNodeName ();
					if (nodeName.equals ("join"))
					{
						joins.add (parseJoin (node));
					}
					break;
				}
			}
		}

		return (JoinParameter[]) joins.toArray (new JoinParameter[0]);
	}


	/*
	 * The join node has two types of child nodes:
	 *     o left node (leaf)
	 *     o right node (leaf)
	 */
	private JoinParameter parseJoin (Node root) throws TqlJoinSchemaClassNotFoundException, TqlJoinSchemaFieldNotFoundException
	{
		String left = null;
		String right = null;

		NodeList children = root.getChildNodes ();
		for (int n = 0; n < children.getLength (); n++)
		{
			Node node = children.item (n);
			switch (node.getNodeType ())
			{
				case Node.ELEMENT_NODE:
				{
					String nodeName = node.getNodeName ();
					if (nodeName.equals ("left"))
					{
						left = parseText (node);
					}
					else if (nodeName.equals ("right"))
					{
						right = parseText (node);
					}
					break;
				}
			}
		}

		left = torqueFieldToSqlField (left);
		right = torqueFieldToSqlField (right);

		return new JoinParameter (left, right);
	}


	private String parseText (Node node)
	{
		Node text = node.getChildNodes ().item (0);
		if (text == null)
		{
			return "";
		}
		return text.getNodeValue ();
	}


	private String torqueFieldToSqlField (String text) throws TqlJoinSchemaClassNotFoundException, TqlJoinSchemaFieldNotFoundException
	{
		String klassName = null;
		String fieldName = null;

		try
		{
			StringTokenizer tokenizer = new StringTokenizer (text, ".");
			if (tokenizer.hasMoreTokens () == false)
			{
				klassName = "";
			}
			klassName = tokenizer.nextToken ();

			if (tokenizer.hasMoreTokens () == false)
			{
				fieldName = "";
			}
			fieldName = tokenizer.nextToken ();
		}
		catch (NoSuchElementException e)
		{
			klassName = "";
			fieldName = "";
		}

  		return getValueFromJavaField (torquePersistPackageName + "." + klassName, fieldName);
	}


	private String getLocalizationBeanIdentifier (String fieldName)
	{
		return fieldName;
		//return getValueFromJavaField ("com.aurel.track.Constants", fieldName);
	}


	private String getValueFromJavaField (String fullQualifiedClassName, String fieldName) throws TqlJoinSchemaClassNotFoundException, TqlJoinSchemaFieldNotFoundException
	{
		Class klass = null;
		try
		{
			klass = Class.forName (fullQualifiedClassName);
		}
		catch (ClassNotFoundException e)
		{
			LOGGER.error (e);
			e.printStackTrace ();
			throw new TqlJoinSchemaClassNotFoundException (fullQualifiedClassName);
		}

		String field = null;
		try
		{
			field = (String) klass.getField (fieldName).get (null);
		}
		catch (Exception e)
		{
			LOGGER.error (e);
			e.printStackTrace ();
			throw new TqlJoinSchemaFieldNotFoundException (fullQualifiedClassName + "." + field);
		}

		return field;
	}


	/**
 	 * Returns the field name for the given alias.
 	 */
	public String getFieldForAlias (String alias)
	{
		return (String) aliasToField.get (alias);
	}


	/**
	 * Returns the join parameters for the given alias.
	 */
	public JoinParameter[] getJoinParametersForAlias (String alias)
	{
		return (JoinParameter[]) aliasToJoin.get (alias);
	}


	/**
	 * Returns the localization bean identifier for the given alias.
	 */
	public String getLocalizationBeanIdentifierForAlias (String alias)
	{
		return (String) aliasToLocalizationBeanIdentifier.get (alias);
	}


	/**
	 * Returns all available alias names.
	 */
	public Enumeration getAliasNames ()
	{
		return aliasToField.keys ();
	}
}
