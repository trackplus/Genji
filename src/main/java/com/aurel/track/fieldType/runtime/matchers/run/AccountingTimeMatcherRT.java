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

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkflowTransitionBean.TIME_UNIT;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.budgetCost.AccountingTimeTO;
import com.aurel.track.item.budgetCost.AccountingBL.TIMEUNITS;

public class AccountingTimeMatcherRT extends AbstractMatcherRT {

	private static final Logger LOGGER = LogManager.getLogger(AccountingTimeMatcherRT.class);

	public AccountingTimeMatcherRT(Integer fieldID, int relation, Object matchValue, MatcherContext matcherContext) {
		super(fieldID, relation, matchValue, matcherContext);
	}

	/**
	 * Whether the value matches or not
	 * 
	 * @param attributeValue
	 * @return
	 */
	@Override
	public boolean match(Object attributeValue) {
		Boolean nullMatch = nullMatcher(attributeValue);
		if (nullMatch != null) {
			return nullMatch.booleanValue();
		}
		if (attributeValue==null || matchValue==null) {
			return false;
		}
		AccountingTimeTO attributeValueAccountingTime = null;
		AccountingTimeTO matcherValueAccountingTime = null;
		try {
			attributeValueAccountingTime = (AccountingTimeTO) attributeValue;
		} catch (Exception e) {
			LOGGER.error("Converting the attribute value " + attributeValue + " of type " +
						attributeValue.getClass().getName() + " to AccountingTimeTO failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		try {
			matcherValueAccountingTime = (AccountingTimeTO) matchValue;
		} catch (Exception e) {
			LOGGER.warn(
					"Converting the matcher value " + matchValue + " of type " + matchValue.getClass().getName() + " to AccountingTimeTO failed with " + e.getMessage(),
					e);
			return false;
		}
		
		Double attributeValueDouble = attributeValueAccountingTime.getValue();
		Double matcherValueDouble = matcherValueAccountingTime.getValue();
		if (attributeValueDouble==null || matcherValueDouble==null) {
			return false;
		}
		Integer attributeValueUnit = attributeValueAccountingTime.getUnit();
		Integer matcherValueUnit = matcherValueAccountingTime.getUnit();
		if (attributeValueUnit==null) {
			attributeValueUnit = TIMEUNITS.HOURS;
		}
		if (matcherValueUnit==null) {
			matcherValueUnit = TIMEUNITS.HOURS;
		}
		if (!attributeValueUnit.equals(matcherValueUnit)) {
			if (attributeValueUnit.intValue()!=TIME_UNIT.HOUR) {
				
				
				attributeValueDouble = AccountingBL.transformToTimeUnits(attributeValueDouble,
						this.getHourPerWorkday(), attributeValueUnit, TIME_UNIT.HOUR).doubleValue();
			}
			if (matcherValueUnit.intValue()!=TIME_UNIT.HOUR) {
				matcherValueDouble = AccountingBL.transformToTimeUnits(matcherValueDouble,
						this.getHourPerWorkday(), matcherValueUnit, TIME_UNIT.HOUR).doubleValue();
			}
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

	private double getHourPerWorkday() {
		Double hoursPerWorkday = null;
		Map<Integer, ProjectAccountingTO> projectAccountingMap = matcherContext.getProjectAccountingMap();
		if (projectAccountingMap!=null) {
			TWorkItemBean workItemBean = matcherContext.getWorkItemBean();
			if (workItemBean!=null) {
				Integer projectID = workItemBean.getProjectID();
				if (projectID!=null) {
					ProjectAccountingTO projectAccountingTO = projectAccountingMap.get(projectID);
					if (projectAccountingTO!=null) {
						hoursPerWorkday = projectAccountingTO.getHoursPerWorkday();
					}
				}
			}
		}
		if (hoursPerWorkday==null) {
			hoursPerWorkday = AccountingBL.DEFAULTHOURSPERWORKINGDAY;
		}
		return hoursPerWorkday.doubleValue();
	}
	
	/**
	 * Add a match expression to the criteria
	 */
	@Override
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
