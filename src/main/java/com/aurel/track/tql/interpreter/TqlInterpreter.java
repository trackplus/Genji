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


package com.aurel.track.tql.interpreter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Stack;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.tql.TqlJoinSchema;
import com.aurel.track.tql.exceptions.TqlCriteriaStackIsNotEmptyException;
import com.aurel.track.tql.exceptions.TqlDateFormatException;
import com.aurel.track.tql.exceptions.TqlNumberFormatException;
import com.aurel.track.tql.exceptions.TqlUnknownAliasException;
import com.aurel.track.tql.exceptions.TqlUnknownOperatorException;
import com.aurel.track.tql.exceptions.TqlUnknownValueException;
import com.aurel.track.tql.parser.Node;
import com.aurel.track.tql.parser.SimpleNode;
import com.aurel.track.tql.parser.TqlParser;
import com.aurel.track.tql.parser.TqlParserConstants;


/**
 * This class is used for transforming an abstract syntax tree of a TQL
 * statement into a Torque Criteria object.
 *
 * @author Clemens Fuchslocher &lt;clfuit00@fht-esslingen.de&gt;
 */


public class TqlInterpreter{
	private TqlJoinSchema joinSchema;
	private static final Logger LOGGER = LogManager.getLogger (TqlInterpreter.class);
    private TPersonBean user = null;

	/**
	 * Constructs a new TqlInterpreter object.
	 *
	 * @param joinSchema the TqlJoinSchema which should be used for the alias mapping
	 */
	public TqlInterpreter (TqlJoinSchema joinSchema, TPersonBean user){
		this.joinSchema = joinSchema;
        this.user = user;
	}


	/**
	 * This method transforms an abstract syntax tree of an TQL statement into
	 * a Criteria object.
	 *
	 * @param tqlRoot the root node of the abstract syntax tree
	 *
	 * @return the Criteria object
	 */
	public Criteria start (SimpleNode tqlRoot, Locale locale) throws TqlCriteriaStackIsNotEmptyException, TqlUnknownAliasException, TqlUnknownValueException, TqlDateFormatException, TqlNumberFormatException, TqlUnknownOperatorException
	{
		Criteria tqlCriteria = new Criteria ();
		Stack tqlCriteriaStack = new Stack ();

		// Traverse the synstax tree in post-order and build up a Criteria object.
		traverseSyntaxTree (tqlRoot, tqlCriteria, tqlCriteriaStack, locale);

		/*
		 * It is a fatal error if the tqlCriteriaStack isn't empty after traversing
		 * the syntax tree. This should never happen!
		 */
		if (tqlCriteriaStack.size () > 0)
		{
			throw new TqlCriteriaStackIsNotEmptyException ();
		}

		return tqlCriteria;
	}

	private String getTableName (String field)
	{
		return field.substring (0, field.indexOf ("."));
	}

	private String convertColumnNameToAlias (String column, String alias)
	{
		return alias + column.substring (column.indexOf ("."));
	}

	private void traverseSyntaxTree (Node node, Criteria tqlCriteria, 
			Stack tqlCriteriaStack, Locale locale) 
			throws TqlUnknownAliasException, TqlUnknownValueException, 
			TqlDateFormatException, TqlNumberFormatException, TqlUnknownOperatorException
	{
		SimpleNode simpleNode = (SimpleNode) node;

		if (simpleNode.getType () == TqlParser.ORDER_BY_EXPRESSION)
		{
			LOGGER.debug("Found ORDER_BY_EXPRESSION");
		    // Which field is addressed by this alias?
			String alias = simpleNode.getValue ();
			String field = getFieldForAliasAndAlsoAddTheNeededJoinsToTheCriteria (alias, tqlCriteria);
			LOGGER.debug("Found ORDER_BY_EXPRESSION " + alias + " , " + field);

			// Ascending or descending order?
			if (simpleNode.getOperator () == TqlParser.ORDER_BY_DESCENDING)
			{
				tqlCriteria.addDescendingOrderByColumn (field);
			}
			else
			{
				tqlCriteria.addAscendingOrderByColumn (field);
			}
		}
		else if (simpleNode.getType () == TqlParser.RELATIONAL_EXPRESSION)
		{
			// Operand(left) Operator(RELATIONAL_EXPRESSION) Operand(right)
			SimpleNode left = (SimpleNode) simpleNode.jjtGetChild (0);
			SimpleNode right = (SimpleNode) simpleNode.jjtGetChild (1);

			// Which field is addressed by this alias?
			String alias = left.getValue ();
			Object value = right.getValue();
			String field = getFieldForAliasAndAlsoAddTheNeededJoinsToTheCriteria (alias, tqlCriteria);

			// Is this alias using localized values?
			String localizationBeanIdentifier = joinSchema.getLocalizationBeanIdentifierForAlias (alias);
			if (!localizationBeanIdentifier.equals (""))
			{	
				Integer fieldID = new Integer(localizationBeanIdentifier);
				ILookup selectBaseLocalizedRT  = (ILookup)FieldTypeManager.getFieldTypeRT(fieldID);				
				//List localizedDataSource = selectBaseLocalizedRT.getMatcherDataSource(fieldID, null, locale);
				//value = LocalizeUtil.getKeyByLocalizedLabel(localizedDataSource, (String) value);
				value = selectBaseLocalizedRT.getLookupIDByLabel(fieldID, null, null, locale, (String)value, null, null);
                /*BaseDropDowns localizationBean =null;// (BaseDropDowns) session.getAttribute (localizationBeanIdentifier);

				String key = localizationBean.getValue ((String) value);
				if (key.equals ("-1"))
				{
					throw new TqlUnknownValueException (alias, (String) value);
				}
				value =new Integer(key);*/
			}

			// Should the right-handed value act like a DATE_LITERAL? Example: "Lastedit <= DATE(2000-01-13)".
			if (right.getType () == TqlParserConstants.DATE_LITERAL)
			{
				String dateFormats[] = {
					"yyyy-MM-dd HH:mm:ss",
					"yyyy-MM-dd HH:mm",
					"yyyy-MM-dd HH",
					"yyyy-MM-dd"
				};

				for (int n = 0; n < dateFormats.length; n++)
				{
					SimpleDateFormat date = new SimpleDateFormat (dateFormats[n]);
					date.setLenient (false);

					try
					{
						value = date.parse (right.getValue ());
						break;
					}
					catch (java.text.ParseException e)
					{
						if (n == (dateFormats.length - 1))
						{
							throw new TqlDateFormatException (right.getValue ());
						}
					}
				}
			}

			if ("$USER.Firstname".equals(value)) {
			    value = user.getFirstName();
			}

			if ("$USER.Lastname".equals(value)) {
			    value = user.getLastName();
			}

			/*
			 * Create a Criteria.Criterion object for the actual expression
			 * and push them onto the tqlCriteriaStack.
			 *
			 * Operand(field) Operator(RELATIONAL_EXPRESSION) Operand(value)
			 */
			Criteria.Criterion tqlCriterion = null;

			int operator = simpleNode.getOperator();
			switch (operator)
			{
				case TqlParserConstants.EQUAL:
				{
					tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.EQUAL);
					break;
				}

				case TqlParserConstants.NOT_EQUAL:
				{
					tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.NOT_EQUAL);
					break;
				}

				case TqlParserConstants.GREATER_EQUAL:
				{
					tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.GREATER_EQUAL);
					break;
				}

				case TqlParserConstants.GREATER_THAN:
				{
					tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.GREATER_THAN);
					break;
				}

				case TqlParserConstants.LESS_EQUAL:
				{
					tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.LESS_EQUAL);
					break;
				}

				case TqlParserConstants.LESS_THAN:
				{
					tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.LESS_THAN);
					break;
				}

				case TqlParserConstants.LIKE:
				{	
					if (value instanceof Integer) {
						tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.EQUAL);
					} else {
						tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.LIKE);
					}
					break;
				}

				case TqlParserConstants.NOT_LIKE:
				{
					if (value instanceof Integer) {
						tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.NOT_EQUAL);
					} else {
						tqlCriterion = tqlCriteria.getNewCriterion (field, value, Criteria.NOT_LIKE);
					}
					break;
				}

				case TqlParserConstants.NEWER_THAN:
				case TqlParserConstants.OLDER_THAN:
				{
					int rightValue = 0;

					int type = right.getType();
					if (type == TqlParser.LITERAL_EXPRESSION)
					{
						rightValue = getValue(right, alias);
					}
					else if (type == TqlParser.DATE_OPERATOR)
					{
						if (right.jjtGetNumChildren() == 1)
						{
							SimpleNode child = (SimpleNode) right.jjtGetChild (0);
							rightValue = getValue(child, alias);
						}
					}

					Calendar calendar = null;

					int dateOperator = right.getDateOperator();
					if (dateOperator == -1)
					{
						calendar = handleOperator(simpleNode, rightValue, alias);
					}
					else
					{
						calendar = handleDateOperator(right, rightValue, alias);
					}

					if (operator == TqlParserConstants.OLDER_THAN)
					{	// OLDER_THAN
						tqlCriterion = tqlCriteria.getNewCriterion (field, calendar.getTime (), Criteria.LESS_THAN);
					}
					else
					{	// NEWER_THAN
						tqlCriterion = tqlCriteria.getNewCriterion (field, calendar.getTime (), Criteria.GREATER_EQUAL);
					}

					break;
				}

				default:
				{
					throw new TqlUnknownOperatorException (simpleNode.getValue ());
				}
			}

			tqlCriteriaStack.push (tqlCriterion);
			return;
		}

		// Now we visit all the children of this node.
		for (int n = 0; n < node.jjtGetNumChildren (); n++)
		{
			traverseSyntaxTree (node.jjtGetChild (n), tqlCriteria, tqlCriteriaStack, locale);
		}

		/*
		 * Pop as many Criteria.Criterion objects from the stack as this node has
		 * child nodes. And then concatenate them with Criteria.Critrion.or() or
		 * Criteria.Criterion.and(). After that push this concatenated node onto
		 * the stack.
		 */
		if (simpleNode.getType () == TqlParser.OR_EXPRESSION)
		{
			// Concatenate
			Criteria.Criterion right = (Criteria.Criterion) tqlCriteriaStack.pop ();
			for (int n = 0; n < simpleNode.jjtGetNumChildren () - 1; n++)
			{
				Criteria.Criterion left = (Criteria.Criterion) tqlCriteriaStack.pop ();
				right = left.or (right);
			}
			// Push
			tqlCriteriaStack.push (right);
		}
		else if (simpleNode.getType () == TqlParser.AND_EXPRESSION)
		{
			// Concatenate
			Criteria.Criterion right = (Criteria.Criterion) tqlCriteriaStack.pop ();
			for (int n = 0; n < simpleNode.jjtGetNumChildren () - 1; n++)
			{
				Criteria.Criterion left = (Criteria.Criterion) tqlCriteriaStack.pop ();
				right = left.and (right);
			}
			// Push
			tqlCriteriaStack.push (right);
		}
		/*
		 * When we are back to the ROOT node we pop the last Criteria.Criterion
		 * object from the stack and add them to the tqlCriteria object. The TQL
		 * statement is now completely transformed into a Criteria object.
		 */
		else if (simpleNode.getType () == TqlParser.ROOT)
		{
			Criteria.Criterion criterion = (Criteria.Criterion) tqlCriteriaStack.pop ();
			tqlCriteria.add (criterion);
		}
	}


	private int getValue(SimpleNode node, String alias) throws TqlNumberFormatException
	{
		try
		{
			return Integer.parseInt (node.getValue ());
		}
		catch (NumberFormatException e)
		{
			throw new TqlNumberFormatException (alias);
		}
	}


	private Calendar handleOperator(SimpleNode node, int value, String alias)
	{
		Calendar now = Calendar.getInstance ();

		int timeunit = node.getTimeUnit();
		switch (timeunit)
		{
			case TqlParserConstants.HOURS:
			{
				now.add (Calendar.HOUR_OF_DAY, value * -1);
				break;
			}

			case TqlParserConstants.DAYS:
			{
				now.add (Calendar.DATE, value * -1);
				break;
			}

			case TqlParserConstants.WEEKS:
			{
				now.add (Calendar.DATE, value * -7);
				break;
			}

			case TqlParserConstants.MONTHS:
			{
				now.add (Calendar.MONTH, value * -1);
				break;
			}

			case TqlParserConstants.YEARS:
			{
				now.add (Calendar.YEAR, value * -1);
				break;
			}

			default:
			{	// Default timeUnit are days.
				now.add (Calendar.DATE, value * -1);
				break;
			}
		}

		LOGGER.debug(alias + " " + TqlParserConstants.tokenImage[timeunit] + " " + value + " == " + now.getTime());

		return now;
	}


	private Calendar handleDateOperator(SimpleNode node, int value, String alias) throws TqlUnknownOperatorException
	{
		Calendar now = Calendar.getInstance ();
		Calendar calendar = null;

		int operator = node.getDateOperator();
		switch (operator)
		{
			case TqlParserConstants.BEGINNING_OF_THIS_YEAR:
			{
				calendar = new GregorianCalendar(now.get(Calendar.YEAR), 0, 1);
				break;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_QUARTER:
			{
				calendar = new GregorianCalendar(now.get(Calendar.YEAR), (now.get(Calendar.MONTH) / 3) * 3, 1);
				break;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_MONTH:
			{
				calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1);
				break;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_WEEK:
			{				
				//calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), (now.get(Calendar.WEEK_OF_MONTH) - 1) * 7);
				calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 
						(now.get(Calendar.DATE) - now.get(Calendar.DAY_OF_WEEK)));
				break;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_DAY:
			{
				calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
				break;
			}

			case TqlParserConstants.LAST_LOGIN:
			{
				calendar = getLastLoginDate(user);
				break;
			}

			default:
			{
				throw new TqlUnknownOperatorException ("dateOperator == " + node.getDateOperator());
			}
		}

		int field = -1;

		int timeunit = node.getDateOperatorTimeUnit();
		if (timeunit == -1)
		{
			field = getCalendarFieldByTimeUnit(operator);

			if (operator == TqlParserConstants.BEGINNING_OF_THIS_QUARTER)
			{
				value *= 3;
			}
		}
		else
		{
			field = getCalendarFieldByTimeUnit(timeunit);
		}

		String sign = node.getDateOperatorSign();
		if (sign != null && sign.equals("-"))
		{
			value *= -1;
		}

		calendar.add(field, value);

		return calendar;
	}


	private Calendar getLastLoginDate(TPersonBean user)
	{
		GregorianCalendar ll = new GregorianCalendar();
		//ll.setTime(user.getLastEdit());
		ll.setTime(user.getLastLogin());
		return ll;
	}


	private int getCalendarFieldByTimeUnit(int timeUnit)
	{
		switch (timeUnit)
		{
			case TqlParserConstants.HOURS:
			{
				return Calendar.HOUR_OF_DAY;
			}

			case TqlParserConstants.DAYS:
			{
				return Calendar.DAY_OF_YEAR;
			}

			case TqlParserConstants.WEEKS:
			{
				return Calendar.WEEK_OF_YEAR;
			}

			case TqlParserConstants.MONTHS:
			{
				return Calendar.MONTH;
			}

			case TqlParserConstants.YEARS:
			{
				return Calendar.YEAR;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_DAY:
			{
				return Calendar.DAY_OF_YEAR;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_WEEK:
			{
				return Calendar.WEEK_OF_YEAR;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_MONTH:
			{
				return Calendar.MONTH;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_QUARTER:
			{
				return Calendar.MONTH;
			}

			case TqlParserConstants.BEGINNING_OF_THIS_YEAR:
			{
				return Calendar.YEAR;
			}

			case TqlParserConstants.LAST_LOGIN:
			{
				return Calendar.DAY_OF_YEAR;
			}

			default:
			{
				return -1;
			}
		}
	}


	private String getFieldForAliasAndAlsoAddTheNeededJoinsToTheCriteria (String alias, Criteria criteria) throws TqlUnknownAliasException
	{
		String field = joinSchema.getFieldForAlias (alias);
		if (field == null)
		{
			throw new TqlUnknownAliasException (alias);
		}

		String aliasName = null;
		String tableName = getTableName (field);

		// Some tables should not be aliased!
		if (!tableName.equals ("TWORKITEM")
            && !tableName.equals("TSTATE")
            && !tableName.equals("TPRIORITY")
            && !tableName.equals("TSEVERITY")
            && !tableName.equals("TCATEGORY")
            && !tableName.equals("TPROJECT")
            && !tableName.equals("TPROJCAT")
            && !tableName.equals("TCLASS"))
		{
			aliasName = alias + "Alias";
			criteria.addAlias (aliasName, tableName);
		}

		// Get the required joins for reaching the according field.
		TqlJoinSchema.JoinParameter[] joinParameters = joinSchema.getJoinParametersForAlias (alias);
		if (joinParameters == null)
		{
			throw new TqlUnknownAliasException (alias);
		}

		// Add the required joins to the Criteria.
		for (int n = 0; n < joinParameters.length; n++)
		{
			if (aliasName == null)
			{
				criteria.addJoin (joinParameters[n].left, joinParameters[n].right);
			}
			else
			{
				if (tableName.equals (getTableName (joinParameters[n].left)))
				{
					criteria.addJoin (convertColumnNameToAlias (joinParameters[n].left, aliasName), joinParameters[n].right);
				}
				else if (tableName.equals (getTableName (joinParameters[n].right)))
				{
					criteria.addJoin (joinParameters[n].left, convertColumnNameToAlias (joinParameters[n].right, aliasName));
				}
				else
				{
					criteria.addJoin (joinParameters[n].left, joinParameters[n].right);
				}
			}
		}

		if (aliasName == null)
		{
			return field;
		}
		else
		{
			return convertColumnNameToAlias (field, aliasName);
		}
	}
}
