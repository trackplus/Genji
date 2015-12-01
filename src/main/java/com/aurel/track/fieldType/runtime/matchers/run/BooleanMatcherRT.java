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


package com.aurel.track.fieldType.runtime.matchers.run;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.persist.TWorkItemPeer;

/**
 * Boolean runtime matcher for boolean based attribute values
 * (in the background it is based on single char value 
 * but it is converted to a boolean by loading the workItem) 
 * @author Tamas Ruff
 *
 */
public class BooleanMatcherRT extends AbstractMatcherRT {
		
	private static final Logger LOGGER = LogManager.getLogger(BooleanMatcherRT.class);
	
	public BooleanMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext) {
		super(fieldID, relation, matchValue, matcherContext);
	}
	
	/**
	 * Whether the value matches or not
	 * @param attributeValue
	 * @return
	 */
	@Override
	public boolean match(Object attributeValue) {				
		Boolean nullMatch = nullMatcher(attributeValue);
		if (nullMatch!=null) {
			return nullMatch.booleanValue();
		}
		if (attributeValue==null){
			return false;
		}		
		Boolean attributeValueBoolean = null;
		try {
			attributeValueBoolean = (Boolean)attributeValue;
		} catch(Exception e) {
			LOGGER.error("Converting the attribute value " + attributeValue + " of type " + 
					attributeValue.getClass().getName() + " to Boolean failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		switch (relation) {
		case MatchRelations.SET:			
			return attributeValueBoolean.booleanValue();
		case MatchRelations.RESET:			
			return !attributeValueBoolean.booleanValue();
		default:
			return false;
		}
	}
	
	/**
	 * Adds the matcher specific expression to the criteria
	 * @param crit
	 * @param index used for aliasing the joined attribute value table
	 */
	@Override
	public void addCriteria(Criteria crit) {
		String columnName = null;
		switch (fieldID.intValue()) {
		case SystemFields.TASK_IS_MILESTONE:
			columnName = TWorkItemPeer.TASKISMILESTONE;
			break;		
		default:
			String alias = addAliasAndJoin(crit);
			columnName = alias + "." + "CHARACTERVALUE";
			break;
		}
		if (addNullExpressionToCriteria(crit, columnName)) {
			return;
		}
		switch (relation) {
		case MatchRelations.SET:			
			crit.add(columnName, BooleanFields.TRUE_VALUE);
			break;	
		case MatchRelations.RESET:
			Criterion resetCriterion = crit.getNewCriterion(columnName, BooleanFields.FALSE_VALUE, Criteria.EQUAL);
			Criterion nullCriterion = crit.getNewCriterion(columnName, (Object)null, Criteria.ISNULL);
			crit.add(resetCriterion.or(nullCriterion));
			break;
		}
	}

}
