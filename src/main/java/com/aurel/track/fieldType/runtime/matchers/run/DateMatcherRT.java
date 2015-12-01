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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.persist.TWorkItemPeer;
import com.aurel.track.util.CalendarUtil;

public class DateMatcherRT extends AbstractMatcherRT {
	
	private static final Logger LOGGER = LogManager.getLogger(DateMatcherRT.class);
	//day begin is the first moment of the matching day
	private Date dayBegin = null;
	//day end is is the first moment of the day coming after the matching day!!!
	private Date dayEnd = null;		
	
	public DateMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext) {
		super(fieldID, relation, matchValue, matcherContext);
		setMatchValue(matchValue);
		
	}

	/**
	 * Reinitialize dayBegin and dayEnd
	 */
	@Override
	public void setMatchValue(Object matcherValue) {
		super.setMatchValue(matcherValue);
		if (relation==MatchRelations.LATER_AS_LASTLOGIN && matcherContext!=null) {
			matcherValue = matcherContext.getLastLoggedDate();
		}
		Calendar calendaMatcherValue = new GregorianCalendar(matcherContext.getLocale());
		calendaMatcherValue.setTime(new Date());
		calendaMatcherValue = CalendarUtil.clearTime(calendaMatcherValue);
		int firstDayOfWeek = calendaMatcherValue.getFirstDayOfWeek();
		switch (relation) {
			case MatchRelations.THIS_WEEK:				
				calendaMatcherValue.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
				dayBegin = calendaMatcherValue.getTime();		
				calendaMatcherValue.add(Calendar.WEEK_OF_YEAR, 1);
				calendaMatcherValue.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
				dayEnd = calendaMatcherValue.getTime();								
				return;
			case MatchRelations.LAST_WEEK:
				calendaMatcherValue.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
				dayEnd = calendaMatcherValue.getTime();
				calendaMatcherValue.add(Calendar.WEEK_OF_YEAR, -1);
				calendaMatcherValue.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);			
				dayBegin = calendaMatcherValue.getTime();
				return;
			case MatchRelations.THIS_MONTH:
				calendaMatcherValue.set(Calendar.DAY_OF_MONTH, 1);
				dayBegin = calendaMatcherValue.getTime();				
				calendaMatcherValue.add(Calendar.MONTH, 1);
				calendaMatcherValue.set(Calendar.DAY_OF_MONTH, 1);				
				dayEnd = calendaMatcherValue.getTime();								
				return;			
			case MatchRelations.LAST_MONTH:
				calendaMatcherValue.set(Calendar.DAY_OF_MONTH, 1);
				dayEnd = calendaMatcherValue.getTime();
				calendaMatcherValue.add(Calendar.MONTH, -1);
				calendaMatcherValue.set(Calendar.DAY_OF_MONTH, 1);			
				dayBegin = calendaMatcherValue.getTime();
				return;		
		}
		
		if (matcherValue==null) {
			return;
		}
		//reinitialize dayBegin and dayEnd
		Date matcherValueDate=null;
		Integer matcherValueInteger=null;
		switch (relation) {
		case MatchRelations.MORE_THAN_DAYS_AGO:
		case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.LESS_THAN_DAYS_AGO:
		case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
			try {
				matcherValueInteger =  (Integer)matchValue;
			} catch(Exception e) {
				LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
						matchValue.getClass().getName() + " to Integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			calendaMatcherValue.setTime(new Date());
			if (matcherValueInteger!=null) {
				int matcherValueInt = matcherValueInteger.intValue();				
				if (relation==MatchRelations.MORE_THAN_EQUAL_DAYS_AGO) {
					matcherValueInt--;
				}
				if (relation==MatchRelations.LESS_THAN_EQUAL_DAYS_AGO) {
					matcherValueInt++;
				}
				calendaMatcherValue.add(Calendar.DATE, -matcherValueInt);
			}
			break;
		case MatchRelations.IN_MORE_THAN_DAYS:
		case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
		case MatchRelations.IN_LESS_THAN_DAYS:
		case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
			try {
				matcherValueInteger = (Integer)matchValue;
			} catch(Exception e) {
				LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
						matchValue.getClass().getName() + " to Integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			calendaMatcherValue.setTime(new Date());
			if (matcherValueInteger!=null) {
				int matcherValueInt = matcherValueInteger.intValue();
				if (relation==MatchRelations.IN_MORE_THAN_EQUAL_DAYS) {
					matcherValueInt--;
				}
				if (relation==MatchRelations.IN_LESS_THAN_EQUAL_DAYS) {
					matcherValueInt++;
				}
				calendaMatcherValue.add(Calendar.DATE, matcherValueInt);
			}
			break;
		case MatchRelations.EQUAL_DATE:
		case MatchRelations.NOT_EQUAL_DATE:
		case MatchRelations.GREATHER_THAN_DATE:
		case MatchRelations.GREATHER_THAN_EQUAL_DATE:
		case MatchRelations.LESS_THAN_DATE:
		case MatchRelations.LESS_THAN_EQUAL_DATE:
			try {
				matcherValueDate =  (Date)matchValue;
			} catch(Exception e) {
				LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
						matchValue.getClass().getName() + " to Date failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (matcherValueDate!=null) {
				calendaMatcherValue.setTime(matcherValueDate);
				if (relation==MatchRelations.GREATHER_THAN_EQUAL_DATE) {
					calendaMatcherValue.add(Calendar.DATE, -1);
				} else {
					if (relation==MatchRelations.LESS_THAN_EQUAL_DATE) {
						calendaMatcherValue.add(Calendar.DATE, 1);
					}
				}									
			}
			break;
		case MatchRelations.LATER_AS_LASTLOGIN:
			if (matcherContext!=null) {
				dayBegin =  matcherContext.getLastLoggedDate();
				dayEnd = matcherContext.getLastLoggedDate();
			}
			//return because later the dayBegin and dayEnd will be assigned again
			return;
		}
		
		calendaMatcherValue = CalendarUtil.clearTime(calendaMatcherValue);
		dayBegin = calendaMatcherValue.getTime();
		calendaMatcherValue.add(Calendar.DATE, 1);
		dayEnd = calendaMatcherValue.getTime();
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
		//probably no value specified
		if (dayBegin==null && dayEnd==null) {
			return true;
		}
		Date attributeValueDate = null;
		try {
			attributeValueDate = (Date)attributeValue;
		} catch(Exception e) {
			LOGGER.error("Converting the attribute value " + attributeValue + " of type " + 
					attributeValue.getClass().getName() + " to Date failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		switch (relation) {
		case MatchRelations.EQUAL_DATE:
		case MatchRelations.THIS_WEEK:
		case MatchRelations.THIS_MONTH:
		case MatchRelations.LAST_WEEK:
		case MatchRelations.LAST_MONTH:
			//for dates without time value (for ex. startDate, endDate) the equality should be also tested
			return  (attributeValueDate.getTime()==dayBegin.getTime() || attributeValueDate.after(dayBegin)) 
				&& attributeValueDate.before(dayEnd);
		case MatchRelations.NOT_EQUAL_DATE:	
			//dayEnd is already the next day so the equality should be also tested
			return attributeValueDate.before(dayBegin) || attributeValueDate.after(dayEnd) || attributeValueDate.getTime()==dayEnd.getTime();
		case MatchRelations.GREATHER_THAN_DATE:
		case MatchRelations.IN_MORE_THAN_DAYS:	
			return attributeValueDate.after(dayEnd) || attributeValueDate.getTime()==dayEnd.getTime();
		case MatchRelations.LESS_THAN_DAYS_AGO:
			return (attributeValueDate.after(dayEnd) || attributeValueDate.getTime()==dayEnd.getTime()) && attributeValueDate.before(new Date());
		case MatchRelations.GREATHER_THAN_EQUAL_DATE:
		case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
			return attributeValueDate.after(dayBegin) || attributeValueDate.getTime()==dayBegin.getTime();
		case MatchRelations.LESS_THAN_DATE:
		case MatchRelations.MORE_THAN_DAYS_AGO:
			return attributeValueDate.before(dayBegin);
		case MatchRelations.IN_LESS_THAN_DAYS:
			return (attributeValueDate.before(dayBegin) || attributeValueDate.getTime()==dayBegin.getTime()) && attributeValueDate.after(new Date());
		case MatchRelations.LESS_THAN_EQUAL_DATE:
		case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
			return attributeValueDate.before(dayEnd);
		case MatchRelations.LATER_AS_LASTLOGIN:
			if (dayBegin==null) {
				//the very first logging 
				return true;
			}
			return attributeValueDate.after(dayBegin);
		default:
			return false;
		}
	}
	
	/**
	 * Add a match expression to the criteria
	 */
	@Override
	public void addCriteria(Criteria crit) {
		String columnName = null;
		switch (fieldID.intValue()) {
		case SystemFields.CREATEDATE:
			columnName = TWorkItemPeer.CREATED;
			break;
		case SystemFields.LASTMODIFIEDDATE:
			columnName = TWorkItemPeer.LASTEDIT;
			break;
		case SystemFields.STARTDATE:
			columnName = TWorkItemPeer.STARTDATE;
			break;
		case SystemFields.ENDDATE:
			columnName = TWorkItemPeer.ENDDATE;
			break;
		case SystemFields.TOP_DOWN_START_DATE:
			columnName = TWorkItemPeer.TOPDOWNSTARTDATE;
			break;
		case SystemFields.TOP_DOWN_END_DATE:
			columnName = TWorkItemPeer.TOPDOWNENDDATE;
			break;	
		default:
			String alias = addAliasAndJoin(crit);
			columnName = alias + "." + "DATEVALUE";
			break;
		}
		if (addNullExpressionToCriteria(crit, columnName)) {
			return;
		}
					
		//matcher relation requested a value but probably no value was specified
		if (dayBegin==null && dayEnd==null) {
			//if no value specified for the entry field (number of days or a date) then 
			//show all with a value specified for that field
			crit.add(columnName, (Object)null, Criteria.ISNOTNULL);
			return;
		}
		Criterion greather;
		Criterion less;
		switch (relation) {				
		case MatchRelations.EQUAL_DATE:
		case MatchRelations.THIS_WEEK:
		case MatchRelations.THIS_MONTH:
		case MatchRelations.LAST_WEEK:
		case MatchRelations.LAST_MONTH:
			//for dates without time value (for ex. startDate, endDate) the equality should be also tested
			greather = crit.getNewCriterion(columnName, dayBegin, Criteria.GREATER_EQUAL);
			less = crit.getNewCriterion(columnName, dayEnd, Criteria.LESS_THAN);
			crit.add(greather.and(less));
			break;
		case MatchRelations.NOT_EQUAL_DATE:	
			//dayEnd is already the next day so the equality should be also tested
			less = crit.getNewCriterion(columnName, dayBegin, Criteria.LESS_THAN);
			greather = crit.getNewCriterion(columnName, dayEnd, Criteria.GREATER_EQUAL);			
			crit.add(greather.or(less));
			break;
		case MatchRelations.GREATHER_THAN_DATE:
		case MatchRelations.IN_MORE_THAN_DAYS:
			crit.add(columnName, dayEnd, Criteria.GREATER_EQUAL);
			break;
		case MatchRelations.LESS_THAN_DAYS_AGO:
			greather = crit.getNewCriterion(columnName, dayEnd, Criteria.GREATER_EQUAL);
			less = crit.getNewCriterion(columnName, new Date(), Criteria.LESS_THAN);
			crit.add(greather.and(less));
			break;
		case MatchRelations.GREATHER_THAN_EQUAL_DATE:
		case MatchRelations.LESS_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_MORE_THAN_EQUAL_DAYS:
			crit.add(columnName, dayBegin, Criteria.GREATER_EQUAL);
			break;
		case MatchRelations.LESS_THAN_DATE:
		case MatchRelations.MORE_THAN_DAYS_AGO:
			crit.add(columnName, dayBegin, Criteria.LESS_THAN);
			break;
		case MatchRelations.IN_LESS_THAN_DAYS:						
			greather = crit.getNewCriterion(columnName, dayBegin, Criteria.LESS_EQUAL);
			less = crit.getNewCriterion(columnName, new Date(), Criteria.GREATER_EQUAL);
			crit.add(greather.and(less));
			break;
		case MatchRelations.LESS_THAN_EQUAL_DATE:
		case MatchRelations.MORE_THAN_EQUAL_DAYS_AGO:
		case MatchRelations.IN_LESS_THAN_EQUAL_DAYS:
			crit.add(columnName, dayEnd, Criteria.LESS_THAN);
			break;
		case MatchRelations.LATER_AS_LASTLOGIN:
			if (dayBegin!=null) {
				crit.add(columnName, dayBegin, Criteria.GREATER_THAN);
			}
			break;
		default:
			break;
		}
	}
}
