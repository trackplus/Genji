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


package com.aurel.track.dao.torque;


import java.util.Iterator;
import java.util.Map;

import org.apache.torque.util.Criteria;

/***
 * Adds some useful features to Torques default criteria class:
 * <li>Copy Constructor</li>
 * <li>Overwritten clone method</li>
 * <li>Handling of some custom criteria, eg. isNull
 * @author netseeker aka Michael Manske 
 */
public class SimpleCriteria extends Criteria
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2888967840339486207L;
	/*** currently used as DEFAULT_CAPACITY in Criteria is private */
	private static final int DEFAULT_CAPACITY = 10;

	/*
	 * Constructor
	 */
	public SimpleCriteria()
	{
		super( DEFAULT_CAPACITY );
	}

	/***
	 * Copy constructor
	 * @param crit
	 * FIXME: can't handle Criteria.aliases, Criteria.selectModifiers and Criteria.useTransaction
	 */
	public SimpleCriteria(Criteria crit)
	{
		this.putAll(crit);        
		this.setIgnoreCase(crit.isIgnoreCase());
		this.setSingleRecord(crit.isSingleRecord());
		this.setCascade(crit.isCascade());
		this.setDbName(crit.getDbName());
		this.setLimit(crit.getLimit());
		this.setOffset(crit.getOffset());        

		//select columns
		for(Iterator it = crit.getSelectColumns().iterator(); it.hasNext();) {
			this.addSelectColumn((String)it.next());
		}

		//order by columns
		for(Iterator it = crit.getOrderByColumns().iterator(); it.hasNext();) {     
			String column = (String)it.next();
			if(column.endsWith("ASC")) {
				this.addAscendingOrderByColumn(column.substring(0, column.indexOf(" ")));
			}
			else {
				this.addDescendingOrderByColumn(column.substring(0, column.indexOf(" ")));
			}
		}

		//as columns
		Map asColumns = crit.getAsColumns();
		for(Iterator iter = asColumns.keySet().iterator(); iter.hasNext(); ) {
			String key = (String)iter.next();
			this.addAsColumn(key, (String)asColumns.get(key));
		}
	}

	/*
	 * Represents the Greater Than in the WHERE clause of an SQL Statement
	 * 
	 * @param columnname the column name @param columnvalue the column value to
	 * be compared against
	 */
	public SimpleCriteria addGreaterThan( String columnname, int columnvalue )
	{
		super.add( columnname, columnvalue, Criteria.GREATER_THAN);
		return this;
	}

	/*
	 * Represents the Is NULL in the WHERE clause of an SQL Statement
	 * 
	 * @param columnname the column name
	 */
	public SimpleCriteria addIsNull( String columnname )
	{
		String str = columnname + " is NULL";
		super.add( columnname, (Object) str, Criteria.CUSTOM);
		return this;
	}

	/***
	 * @param columnname
	 * @return
	 */
	public SimpleCriteria addLike( String columnname )
	{
		String str = columnname + " LIKE";
		super.add( columnname, (Object) str, Criteria.CUSTOM);
		return this;
	}

	/*
	 * Represents the Is not NULL in the WHERE clause of an SQL Statement
	 * 
	 * @param columnname the column name
	 */
	public SimpleCriteria addIsNotNull( String columnname )
	{
		String str = columnname + " is not NULL";
		super.add( columnname, (Object) str, Criteria.CUSTOM);
		return this;
	}

	/*
	 * Represents the Is NULL in the WHERE clause of an SQL Statement
	 * 
	 * @param columnname the column name
	 */
	public SimpleCriteria addIsBetween( String columnname, int min, int max )
	{
		super.add( columnname, min, Criteria.GREATER_THAN);
		Criteria.Criterion criterion = super.getCriterion( columnname);
		criterion.and( super.getNewCriterion( criterion.getTable(), criterion.getColumn(), new Integer( max ),
				Criteria.LESS_EQUAL));
		return this;
	}

	/*
	 * Represents the Is NULL in the WHERE clause of an SQL Statement
	 * 
	 * @param columnname the column name
	 */
	public SimpleCriteria addIsBetween( String columnname, long min, long max )
	{
		super.add( columnname, min, Criteria.GREATER_THAN);
		Criteria.Criterion criterion = super.getCriterion( columnname);
		criterion.and( super.getNewCriterion( criterion.getTable(), criterion.getColumn(), new Long( max ),
				Criteria.LESS_EQUAL));
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone()
	{
		return new SimpleCriteria(this);
	}

}
