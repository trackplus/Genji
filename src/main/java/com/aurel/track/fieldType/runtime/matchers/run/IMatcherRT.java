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

import org.apache.torque.util.Criteria;

import com.aurel.track.fieldType.runtime.matchers.IMatcherBase;



/**
 * Base interface for executing a matcher
 * @author Tamas Ruff
 */
public interface IMatcherRT extends IMatcherBase { 
		
	/**
	 * Gets the context with the associations between the symbolic and actual values
	 * @return
	 */
	MatcherContext getMatcherContext();
	
	/**
	 * Sets the context with the associations between the symbolic and actual values
	 * @param matcherContext
	 */
	void setMatcherContext(MatcherContext matcherContext);
	
	/**
	 * Whether the value matches or not
	 * @param attributeValue
	 * @return
	 */
	boolean match(Object attributeValue);
	
	/**
	 * Adds the matcher specific expression to the criteria
	 * @param crit
	 */
	void addCriteria(Criteria crit);
	
	
}
