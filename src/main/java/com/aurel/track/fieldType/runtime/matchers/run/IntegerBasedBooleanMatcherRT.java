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

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.persist.TWorkItemPeer;

/**
 * Boolean runtime matcher for integer based attribute values
 * Used for systemAccesLevel field where 1=true, all other values=false 
 * @author Tamas Ruff
 *
 */
public class IntegerBasedBooleanMatcherRT extends AbstractMatcherRT {
		
	private static final Logger LOGGER = LogManager.getLogger(IntegerBasedBooleanMatcherRT.class);
	
	public IntegerBasedBooleanMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext) {
		super(fieldID, relation, matchValue, matcherContext);
	}
	
	/**
	 * Whether the value matches or not
	 * @param attributeValue
	 * @return
	 */
	public boolean match(Object attributeValue) {				
		if (attributeValue==null){
			attributeValue = new Integer(0);
		}		
		Integer attributeValueInteger = null;
		try {
			attributeValueInteger = (Integer)attributeValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the attribute value " + attributeValue + " of type " + 
					attributeValue.getClass().getName() + " to Integer failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		switch (relation) {
		case MatchRelations.SET:			
			return TWorkItemBean.ACCESS_LEVEL_PRIVATE.equals(attributeValueInteger.intValue());
		case MatchRelations.RESET:			
			return !TWorkItemBean.ACCESS_LEVEL_PRIVATE.equals(attributeValueInteger.intValue());
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
		case SystemFields.ACCESSLEVEL:
			columnName = TWorkItemPeer.ACCESSLEVEL;
			break;
		}
		switch (relation) {
		case MatchRelations.SET:			
			crit.add(columnName, TWorkItemBean.ACCESS_LEVEL_PRIVATE);
			break;	
		case MatchRelations.RESET:
			Criterion resetCriterion = crit.getNewCriterion(columnName, TWorkItemBean.ACCESS_LEVEL_PUBLIC, Criteria.EQUAL);
			Criterion nullCriterion = crit.getNewCriterion(columnName, (Object)null, Criteria.ISNULL);
			crit.add(resetCriterion.or(nullCriterion));
			break;
		}
	
	

	}
}
