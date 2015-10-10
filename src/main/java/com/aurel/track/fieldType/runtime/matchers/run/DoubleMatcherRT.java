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

import com.aurel.track.fieldType.runtime.matchers.MatchRelations;

public class DoubleMatcherRT extends AbstractMatcherRT {

	private static final Logger LOGGER = LogManager.getLogger(DoubleMatcherRT.class);

	public DoubleMatcherRT(Integer fieldID, int relation, Object matchValue, MatcherContext matcherContext) {
		super(fieldID, relation, matchValue, matcherContext);
	}

	/**
	 * Whether the value matches or not
	 * 
	 * @param attributeValue
	 * @return
	 */
	public boolean match(Object attributeValue) {
		Boolean nullMatch = nullMatcher(attributeValue);
		if (nullMatch != null) {
			return nullMatch.booleanValue();
		}
		if (attributeValue == null || matchValue == null) {
			return false;
		}
		Double attributeValueDouble = null;
		Double matcherValueDouble = null;
		try {
			attributeValueDouble = (Double) attributeValue;
		} catch (Exception e) {
			LOGGER.error(
					"Converting the attribute value " + attributeValue + " of type " + attributeValue.getClass().getName() + " to Double failed with "
							+ e.getMessage(), e);
			return false;
		}
		try {
			matcherValueDouble = (Double) matchValue;
		} catch (Exception e) {
			LOGGER.warn(
					"Converting the matcher value " + matchValue + " of type " + matchValue.getClass().getName() + " to Double failed with " + e.getMessage(),
					e);
			return false;
		}

		switch (relation) {
		case MatchRelations.EQUAL:
			return (Double.doubleToRawLongBits(attributeValueDouble.doubleValue()) - Double.doubleToRawLongBits(matcherValueDouble.doubleValue()) == 0);
		case MatchRelations.NOT_EQUAL:
			return (Double.doubleToRawLongBits(attributeValueDouble.doubleValue()) - Double.doubleToRawLongBits(matcherValueDouble.doubleValue()) != 0);
		case MatchRelations.GREATHER_THAN:
			return attributeValueDouble.doubleValue() > matcherValueDouble.doubleValue();
		case MatchRelations.GREATHER_THAN_EQUAL:
			return attributeValueDouble.doubleValue() >= matcherValueDouble.doubleValue();
		case MatchRelations.LESS_THAN:
			return attributeValueDouble.doubleValue() < matcherValueDouble.doubleValue();
		case MatchRelations.LESS_THAN_EQUAL:
			return attributeValueDouble.doubleValue() <= matcherValueDouble.doubleValue();
		default:
			return false;
		}
	}

	/**
	 * Add a match expression to the criteria
	 */
	public void addCriteria(Criteria crit) {
		String alias = addAliasAndJoin(crit);
		String columnName = alias + "." + "DOUBLEVALUE";
		if (addNullExpressionToCriteria(crit, columnName)) {
			return;
		}
		Double matcherValueDouble = null;
		try {
			matcherValueDouble = (Double) matchValue;
		} catch (Exception e) {
			LOGGER.warn(
					"Converting the matcher value " + matchValue + " of type " + matchValue.getClass().getName() + " to Double failed with " + e.getMessage(),
					e);

		}
		switch (relation) {
		case MatchRelations.EQUAL:
			crit.add(columnName, matcherValueDouble);
			break;
		case MatchRelations.NOT_EQUAL:
			crit.add(columnName, matcherValueDouble, Criteria.NOT_EQUAL);
			break;
		case MatchRelations.GREATHER_THAN:
			crit.add(columnName, matcherValueDouble, Criteria.GREATER_THAN);
			break;
		case MatchRelations.GREATHER_THAN_EQUAL:
			crit.add(columnName, matcherValueDouble, Criteria.GREATER_EQUAL);
			break;
		case MatchRelations.LESS_THAN:
			crit.add(columnName, matcherValueDouble, Criteria.LESS_THAN);
			break;
		case MatchRelations.LESS_THAN_EQUAL:
			crit.add(columnName, matcherValueDouble, Criteria.LESS_EQUAL);
			break;
		default:
			break;
		}
	}

	private static Double zeroDouble = 0.0d;

	/**
	 * Safe compare double value with 0
	 * 
	 * @param value
	 * @return true if this value is really zero (bitwise compare)
	 */
	public static boolean isZero(Double value) {
		return (Double.doubleToRawLongBits(value) - Double.doubleToRawLongBits(zeroDouble) == 0);
	}

}
