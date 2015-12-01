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

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.persist.TAttributeValuePeer;
import com.aurel.track.persist.TWorkItemPeer;

public class CompositeSelectMatcherRT extends AbstractMatcherRT {
private static final Logger LOGGER = LogManager.getLogger(CustomSelectMatcherRT.class);
	
	public static final Integer ANY_FROM_LEVEL = Integer.valueOf(-1);
	public static final Integer NONE_FROM_LEVEL = Integer.valueOf(-2);

	public CompositeSelectMatcherRT(Integer fieldID, int relation, Object matchValue,
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
		Map<Integer, Object> attributeValueMap = null;		
		try {
			attributeValueMap = (Map<Integer, Object>) attributeValue;
		} catch (Exception e) {
			LOGGER.warn("Converting the attribute value of type " + 
					attributeValue.getClass().getName() + " to Map<Integer, Object failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		SortedMap<Integer, Object> matcherValueMap = null;
		try {
			matcherValueMap = (SortedMap<Integer, Object>)matchValue;
		} catch (Exception e) {
			LOGGER.warn("Converting the matcher value of type " + 
					matchValue.getClass().getName() + " to SortedMap<Integer, Object> failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (attributeValueMap==null || matcherValueMap==null) {
			return false;
		}
		Iterator<Integer> iterator = matcherValueMap.keySet().iterator();
		while (iterator.hasNext()) {
			Integer parameterCode = iterator.next();
			Object[] attributeValueCustomOption = null;		
			try {
				attributeValueCustomOption = (Object[])attributeValueMap.get(parameterCode);
			} catch(Exception e) {
				LOGGER.warn("Converting the attribute value for part " + parameterCode +  
						" to Object[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return false;
			}
			Integer[] matcherValueCustomOption = null;
			try {
				matcherValueCustomOption =  (Integer[])matcherValueMap.get(parameterCode);
			} catch(Exception e) {
				LOGGER.error("Converting the matcher value for part " + parameterCode + 
						" to Integer[] failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return false;
			}

			if (attributeValueCustomOption==null) {
				attributeValueCustomOption = new Object[0];
			}
			if (matcherValueCustomOption==null) {
				matcherValueCustomOption = new Integer[0];
			}
			if (attributeValueCustomOption.length!=matcherValueCustomOption.length) {
				return false;
			}
			
			if (matcherValueCustomOption.length==0) {
				return matcherValueCustomOption.length==0;
			}
			Integer matcherValueAtLevel = matcherValueCustomOption[0];
			switch (relation) {
			case MatchRelations.EQUAL:			
				if (!containsValue(matcherValueAtLevel, attributeValueCustomOption)) {
					return false;
				}
				break;
			case MatchRelations.NOT_EQUAL:
				if (!containsValue(matcherValueAtLevel, attributeValueCustomOption)) {
					return true;		
				}
				break;
			case MatchRelations.PARTIAL_MATCH:
				if (matcherValueAtLevel.equals(ANY_FROM_LEVEL)) {
					return true;
				} else {
					if (!containsValue(matcherValueAtLevel, attributeValueCustomOption)) {
						return false;		
					}	
				}
				break;
			case MatchRelations.PARTIAL_NOTMATCH:
				if (!containsValue(matcherValueAtLevel, attributeValueCustomOption) && !matcherValueCustomOption[0].equals(NONE_FROM_LEVEL)) {
						return true;
				}
				break;
			default:
				return false;
			}
		}
		if (relation==MatchRelations.NOT_EQUAL || relation==MatchRelations.PARTIAL_NOTMATCH) {
			return false;
		} else {
			return true;
		}
	}

	private boolean containsValue(Integer intValue, Object[] intArr) {		
		for (int i = 0; i < intArr.length; i++) {
			if (intValue.equals(intArr[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a match expression to the criteria
	 */
	@Override
	public void addCriteria(Criteria crit) {
		if (relation==MatchRelations.IS_NULL || relation==MatchRelations.NOT_IS_NULL) {
			String alias = addAliasAndJoin(crit);
			addNullExpressionToCriteria(crit, alias + "." + "CUSTOMOPTIONID"); 
			return;
		}
		SortedMap<Integer, Object> matcherValueMap = null;
		try {
			matcherValueMap = (SortedMap<Integer, Object>)matchValue;
		} catch (Exception e) {
			LOGGER.warn("Converting the matcher value of type " + 
					matchValue.getClass().getName() + " to SortedMap<Integer, Object> failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (matcherValueMap!=null && matcherValueMap.size()!=0) {
			Iterator<Integer> iterator = matcherValueMap.keySet().iterator();
			Criterion criterion = null;
			while (iterator.hasNext()) {
				Integer parameterCode = iterator.next();
				Integer[] matcherValueCustomOption = null;
				try {
					matcherValueCustomOption =  (Integer[])matcherValueMap.get(parameterCode);
				} catch(Exception e) {
					LOGGER.error("Converting the matcher value for part " + parameterCode + 
							" to Integer[] failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
					return;
				}
				if (matcherValueCustomOption==null || matcherValueCustomOption.length==0) {
					matcherValueCustomOption = new Integer[0];
				}
				String alias = null;
				switch (relation) {
				case MatchRelations.EQUAL:			
					alias = addAliasAndJoin(crit, parameterCode);
					crit.addIn(alias + "." + "CUSTOMOPTIONID", matcherValueCustomOption);
					break;
				case MatchRelations.NOT_EQUAL:
					alias = addAliasAndJoin(crit, parameterCode);
					if (criterion==null) {
						criterion = crit.getNewCriterion(alias + "." + "CUSTOMOPTIONID", matcherValueCustomOption, Criteria.NOT_IN);
					} else {
						criterion = criterion.or(crit.getNewCriterion(alias + "." + "CUSTOMOPTIONID", matcherValueCustomOption, Criteria.NOT_IN));
					}
					break;
				case MatchRelations.PARTIAL_MATCH:
					if (matcherValueCustomOption[0].equals(ANY_FROM_LEVEL)) {
						return;
					} else {
						alias = addAliasAndJoin(crit, parameterCode);
						crit.addIn(alias + "." + "CUSTOMOPTIONID", matcherValueCustomOption);
					}
					break;
				case MatchRelations.PARTIAL_NOTMATCH:
					if (!matcherValueCustomOption[0].equals(NONE_FROM_LEVEL)) {
						alias = addAliasAndJoin(crit, parameterCode);
						if (criterion==null) {
							criterion = crit.getNewCriterion(alias + "." + "CUSTOMOPTIONID", matcherValueCustomOption, Criteria.NOT_IN);
						} else {
							criterion = criterion.or(crit.getNewCriterion(alias + "." + "CUSTOMOPTIONID", matcherValueCustomOption, Criteria.NOT_IN));
						}
					}
					break;
				}
			}
			if (criterion!=null) {
				crit.add(criterion);
			}
		}
	}
	
	/**
	 * Adds an alias for TATTRIBUTEVALUE, joins with TWORKITEM and returns the alias
	 * @param crit
	 * @param fieldID
	 * @return
	 */
	protected String addAliasAndJoin(Criteria crit, Integer parameterCode) {
		String alias = TAttributeValuePeer.TABLE_NAME + fieldID + "_" + parameterCode;
		crit.addAlias(alias, TAttributeValuePeer.TABLE_NAME);
		crit.addJoin(alias + "." + "WORKITEM", TWorkItemPeer.WORKITEMKEY);
		crit.add(alias + "." + "FIELDKEY", fieldID);
		crit.add(alias + "." + "PARAMETERCODE", parameterCode);
		return alias;
	}
}
