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


package com.aurel.track.fieldType.runtime.matchers.run;

import org.apache.torque.util.Criteria;

import com.aurel.track.fieldType.runtime.matchers.AbstractMatcherBase;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.persist.TAttributeValuePeer;
import com.aurel.track.persist.TWorkItemPeer;

/**
 * Base interface for executing a matcher
 * @author Tamas Ruff
 *
 */
public abstract class AbstractMatcherRT extends AbstractMatcherBase implements IMatcherRT {
	//context with the associations between the symbolic and actual values
	protected MatcherContext matcherContext;
	
	public AbstractMatcherRT(Integer fieldID, int relation, Object matchValue, 
			MatcherContext matcherContext) {
		super(fieldID);
		this.relation = relation;
		this.matchValue = matchValue;
		this.matcherContext = matcherContext;
	}

	/**
	 * Helper method for matching null conditions 
	 * @param attributeValue
	 * @return
	 */
	protected Boolean nullMatcher(Object attributeValue) {
		if (relation==MatchRelations.IS_NULL) {
			if (attributeValue==null || "".equals(attributeValue)){
				return new Boolean(true);
			} else {
				return new Boolean(false);
			}
		}
		if (relation==MatchRelations.NOT_IS_NULL) {
			if (attributeValue!=null && !"".equals(attributeValue)){
				return new Boolean(true);
			} else {
				return new Boolean(false);
			}
		}
		return null;
	}

	/**
	 * Adds a null criteria if the relation is NULL or NOT NULL
	 * @param crit
	 * @param columnName
	 * @param isCustom
	 * @return
	 */
	protected boolean addNullExpressionToCriteria(Criteria crit, String columnName) {
		if (relation==MatchRelations.IS_NULL) {
			crit.add(columnName, (Object)null, Criteria.ISNULL); 
			return true;			
		}
		if (relation==MatchRelations.NOT_IS_NULL) {
			crit.add(columnName, (Object)null, Criteria.ISNOTNULL);
		}
		return false;
	}
	
	/**
	 * @return the matcherContext
	 */
	public MatcherContext getMatcherContext() {
		return matcherContext;
	}

	/**
	 * @param matcherContext the matcherContext to set
	 */
	public void setMatcherContext(MatcherContext matcherContext) {
		this.matcherContext = matcherContext;
	}
	
	/**
	 * Adds the matcher specific expression to the criteria
	 * @param crit
	 */
	public void addCriteria(Criteria crit) {
		//TODO implement for each matcher type
	}
	
	/**
	 * Adds an alias for TATTRIBUTEVALUE, joins with TWORKITEM and returns the alias
	 * @param crit
	 * @param fieldID
	 * @return
	 */
	protected String addAliasAndJoin(Criteria crit) {
		String alias = TAttributeValuePeer.TABLE_NAME + fieldID;
		crit.addAlias(alias, TAttributeValuePeer.TABLE_NAME);
		crit.addJoin(alias + "." + "WORKITEM", TWorkItemPeer.WORKITEMKEY);
		crit.add(alias + "." + "FIELDKEY", fieldID);
		return alias;
	}
}
