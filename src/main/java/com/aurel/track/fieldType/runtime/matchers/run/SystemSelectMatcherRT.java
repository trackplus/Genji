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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.CriteriaUtil;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.user.group.GroupMemberBL;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.persist.TWorkItemPeer;

/**
 * Matcher class for system selects. 
 * The attribute value for system selects is of type Integer
 * (unlikely to custom selects where the attribute value is Integer[])
 * @author Tamas Ruff
 *
 */
public class SystemSelectMatcherRT extends AbstractMatcherRT {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemSelectMatcherRT.class);
	
	public SystemSelectMatcherRT(Integer fieldID, int relation, Object matchValue,
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
		if (attributeValue==null || matchValue==null){
			return false;
		}
		Integer attributeValueSystemOption = null;
		Integer matcherValueSystemOption = null;
		try {
			attributeValueSystemOption = (Integer)attributeValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the attribute value " + attributeValue + " of type " + 
					attributeValue.getClass().getName() + " to Integer failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		try {
			Integer[] matchValueArr= (Integer[])matchValue;
			if(matchValueArr.length>0){
				matcherValueSystemOption = matchValueArr[0];
			}
		} catch(Exception e) {
			LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
					matchValue.getClass().getName() + " to Integer failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		
		switch (relation) {
		case MatchRelations.EQUAL:
			return attributeValueSystemOption.equals(matcherValueSystemOption);
		case MatchRelations.NOT_EQUAL:
			return !attributeValueSystemOption.equals(matcherValueSystemOption);
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
		Integer[] ids = null;
		if (matchValue!=null) {
			try {
				ids = (Integer[])matchValue;
			} catch(Exception e) {
				LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
						matchValue.getClass().getName() + " to Integer failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return;
			}
		}	
		switch (fieldID.intValue()) {
		case SystemFields.PROJECT:
			columnName = TWorkItemPeer.PROJECTKEY;
			CriteriaUtil.addActiveInactiveProjectCriteria(crit);
			break;
		case SystemFields.ISSUETYPE:
			columnName = TWorkItemPeer.CATEGORYKEY;
			break;
		case SystemFields.STATE:
			columnName = TWorkItemPeer.STATE;
			break;
		case SystemFields.MANAGER:
			columnName = TWorkItemPeer.OWNER;
			break;
		case SystemFields.RESPONSIBLE:
			columnName = TWorkItemPeer.RESPONSIBLE;
			if (ids!=null && ids.length!=0) {
				if (matcherContext.isIncludeResponsiblesThroughGroup()) {
					Set<Integer> responsibleGroupIDs = GroupMemberBL.getGroupsIDsForPersons(ids);
					Set<Integer> responsiblesSet = new HashSet<Integer>();
					for (int i = 0; i < ids.length; i++) {
						responsiblesSet.add(ids[i]);
					}
					if (responsibleGroupIDs!=null) {
						responsiblesSet.addAll(responsibleGroupIDs);
					}
					crit.addIn(columnName, responsiblesSet.toArray());
				} else {
					crit.addIn(columnName, ids);
				}
			} else {
				//from fieldExpressionSimpleList (hardcoded filter)
				addNullExpressionToCriteria(crit, columnName);
			}
			return;
		case SystemFields.RELEASE:
			if (matcherContext.getReleaseTypeSelector()==null) {
				if (ids!=null && ids.length>0) {
					crit.addIn(TWorkItemPeer.RELSCHEDULEDKEY, ids);
				}
				columnName = TWorkItemPeer.RELSCHEDULEDKEY;
			} else {
				switch (matcherContext.getReleaseTypeSelector().intValue()) {
				case FilterUpperTO.RELEASE_SELECTOR.NOTICED:
					if (ids!=null && ids.length>0) {
						crit.addIn(TWorkItemPeer.RELNOTICEDKEY, ids);
					}
					columnName = TWorkItemPeer.RELNOTICEDKEY;
					break;
				case FilterUpperTO.RELEASE_SELECTOR.SCHEDULED:
					if (ids!=null && ids.length>0) {
						crit.addIn(TWorkItemPeer.RELSCHEDULEDKEY, ids);	
					}
					columnName = TWorkItemPeer.RELNOTICEDKEY;
					break;
				case FilterUpperTO.RELEASE_SELECTOR.NOTICED_OR_SCHEDULED:
					if (ids!=null && ids.length>0) {
						Criterion relNoticed = crit.getNewCriterion(TWorkItemPeer.RELNOTICEDKEY, ids, Criteria.IN);
						Criterion relScheduled = crit.getNewCriterion(TWorkItemPeer.RELSCHEDULEDKEY, ids, Criteria.IN);
						crit.add(relNoticed.or(relScheduled));
					}
					columnName = TWorkItemPeer.RELNOTICEDKEY;
					break;
				}
			}
			//if from fieldExpressionSimpleList (hardcoded filter)
			addNullExpressionToCriteria(crit, columnName);
			return;
		case SystemFields.PRIORITY:
			columnName = TWorkItemPeer.PRIORITYKEY;
			break;
		case SystemFields.SEVERITY:
			columnName = TWorkItemPeer.SEVERITYKEY;
			break;
		case SystemFields.ORIGINATOR:
			columnName = TWorkItemPeer.ORIGINATOR;
			break;
		case SystemFields.CHANGEDBY:
			columnName = TWorkItemPeer.CHANGEDBY;
			break;
		}
		if (columnName!=null) {
			if (ids!=null && ids.length>0) {
				crit.addIn(columnName, ids);
			} else {
				//if from fieldExpressionSimpleList (hardcoded filter)
				addNullExpressionToCriteria(crit, columnName);
			}
		}
	}
}
