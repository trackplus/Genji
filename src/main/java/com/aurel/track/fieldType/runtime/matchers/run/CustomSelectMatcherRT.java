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

import com.aurel.track.fieldType.runtime.matchers.MatchRelations;

/**
 * Matcher class for custom selects. 
 * The attribute value for custom selects is of type Integer[] 
 * i.e. more options could be selected
 * (unlikely to system selects where the attribute value is Integer)
 * @author Tamas Ruff
 *
 */
public class CustomSelectMatcherRT extends AbstractMatcherRT {
		
	private static final Logger LOGGER = LogManager.getLogger(CustomSelectMatcherRT.class);
	/**
	 * set for pickers, null for custom lists
	 */
	private Integer systemOptionType;
	
	public CustomSelectMatcherRT(Integer fieldID, int relation, Object matchValue,
			MatcherContext matcherContext) {
		super(fieldID, relation, matchValue, matcherContext);
	}
	
	
	public void setSystemOptionType(Integer systemOptionType) {
		this.systemOptionType = systemOptionType;
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
		Object[] attributeValueCustomOption = null;
		Integer[] matcherValueCustomOption = null;
		try {
			attributeValueCustomOption = (Object[])attributeValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the attribute value " + attributeValue + " of type " + 
					attributeValue.getClass().getName() + " to Object[] failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}
		try {
			matcherValueCustomOption = (Integer[])matchValue;
		} catch(Exception e) {
			LOGGER.warn("Converting the matcher value " + matchValue + " of type " + 
					matchValue.getClass().getName() + " to Integer[] failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return false;
		}

		if (attributeValueCustomOption.length==0) {
			//attributeValueCustomOption should have at least one element but could have more
			return false;
		}
		if (matcherValueCustomOption==null || matcherValueCustomOption.length==0) {
			//matcherValueCustomOption should have one element
			return false;
		}
		
		switch (relation) {
		case MatchRelations.EQUAL:
			return containsValue(matcherValueCustomOption, attributeValueCustomOption);
		case MatchRelations.NOT_EQUAL:
			return !containsValue(matcherValueCustomOption, attributeValueCustomOption);		
		default:
			return false;
		}
	}

	private boolean containsValue(Integer[] matcherValues, Object[] attributeValues) {
		for (Integer matcherValue : matcherValues) {
			if (matcherValue!=null) {
				for (Object attributeValue : attributeValues) {
					if (matcherValue.equals(attributeValue)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Add a match expression to the criteria
	 */
	@Override
	public void addCriteria(Criteria crit) {
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
		if (ids!=null && ids.length!=0) {
			String alias = addAliasAndJoin(crit);
			if (systemOptionType==null) {
				crit.addIn(alias + "." + "CUSTOMOPTIONID", ids);
			} else {
				crit.addIn(alias + "." + "SYSTEMOPTIONID", ids);
				crit.add(alias + "." + "SYSTEMOPTIONTYPE", systemOptionType);
			}
		} else {
			String alias = addAliasAndJoin(crit);
			if (systemOptionType==null) {
				addNullExpressionToCriteria(crit, alias + "." + "CUSTOMOPTIONID");
			} else {
				addNullExpressionToCriteria(crit, alias + "." + "SYSTEMOPTIONID");
				crit.add(alias + "." + "SYSTEMOPTIONTYPE", systemOptionType);
			}
		}
	}
}
