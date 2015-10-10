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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.persist.TWorkItemPeer;


public class IntegerMatcherRT extends AbstractMatcherRT {
		
	private static final Logger LOGGER = LogManager.getLogger(IntegerMatcherRT.class);
	
	public IntegerMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext) {
		super(fieldID, relation, matchValue, matcherContext);
	}
	
	/**
	 * Whether the value matches or not
	 * @param attributeValue
	 * @return
	 */
	public boolean match(Object attributeValue) {		
		Boolean nullMatch = nullMatcher(attributeValue);
		if (nullMatch!=null) {
			return nullMatch.booleanValue();
		}
		if (attributeValue==null || matchValue==null){
			return false;
		}
		Integer attributeValueInteger = null;
		Integer matcherValueInteger = null;
		try {
			attributeValueInteger = (Integer)attributeValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the attribute value " + attributeValue + " of type " + 
					attributeValue.getClass().getName() + " to Integer failed with " + e.getMessage(), e);
			return false;
		}
		try {
			matcherValueInteger =  (Integer)matchValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
					matchValue.getClass().getName() + " to Integer failed with " + e.getMessage(), e);
			return false;
		}
		switch (relation) {
		case MatchRelations.EQUAL:			
			return attributeValueInteger.intValue()==matcherValueInteger.intValue();
		case MatchRelations.NOT_EQUAL:			
			return attributeValueInteger.intValue()!=matcherValueInteger.intValue();
		case MatchRelations.GREATHER_THAN:			
			return attributeValueInteger.intValue()>matcherValueInteger.intValue();
		case MatchRelations.GREATHER_THAN_EQUAL:			
			return attributeValueInteger.intValue()>=matcherValueInteger.intValue();
		case MatchRelations.LESS_THAN:			
			return attributeValueInteger.intValue()<matcherValueInteger.intValue();
		case MatchRelations.LESS_THAN_EQUAL:			
			return attributeValueInteger.intValue()<=matcherValueInteger.intValue();
		default:
			return false;
		}
	}
	
	/**
	 * Add a match expression to the criteria
	 */
	public void addCriteria(Criteria crit) {
		String columnName = null;
		switch (fieldID.intValue()) {
		case SystemFields.SUPERIORWORKITEM:
			columnName = TWorkItemPeer.SUPERIORWORKITEM;
			break;	
		case SystemFields.PROJECT_SPECIFIC_ISSUENO:
		case SystemFields.ISSUENO:
			columnName = TWorkItemPeer.WORKITEMKEY;
			break;
		default:
			String alias = addAliasAndJoin(crit);
			columnName = alias + "." + "INTEGERVALUE";
			break;
		}
		if (addNullExpressionToCriteria(crit, columnName)) {
			return;
		}
		Integer matcherValueInteger = null;
		try {
			matcherValueInteger = (Integer)matchValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
					matchValue.getClass().getName() + " to Integer failed with " + e.getMessage(), e);
			
		}
		switch (relation) {
		case MatchRelations.EQUAL:	
			crit.add(columnName, matcherValueInteger);
			break;
		case MatchRelations.NOT_EQUAL:			
			crit.add(columnName, matcherValueInteger, Criteria.NOT_EQUAL);
			break;
		case MatchRelations.GREATHER_THAN:			
			crit.add(columnName, matcherValueInteger, Criteria.GREATER_THAN);
			break;
		case MatchRelations.GREATHER_THAN_EQUAL:			
			crit.add(columnName, matcherValueInteger, Criteria.GREATER_EQUAL);
			break;
		case MatchRelations.LESS_THAN:			
			crit.add(columnName, matcherValueInteger, Criteria.LESS_THAN);
			break;
		case MatchRelations.LESS_THAN_EQUAL:			
			crit.add(columnName, matcherValueInteger, Criteria.LESS_EQUAL);
			break;
		}
	}

}
