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


package com.aurel.track.fieldType.runtime.matchers.design;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;


public class BooleanMatcherDT extends AbstractMatcherDT {
		
	public BooleanMatcherDT(Integer fieldID) {
		super(fieldID);
	}

	/**
	 * Gets the possible relations for a field type
	 * @param withParameter
	 * @param inTree
	 * @return
	 */
	@Override
	public List<Integer> getPossibleRelations(boolean withParameter, boolean inTree) {
		List <Integer> relations = new ArrayList<Integer>();		
		relations.add(Integer.valueOf(MatchRelations.IS_NULL));
		relations.add(Integer.valueOf(MatchRelations.NOT_IS_NULL));
		relations.add(Integer.valueOf(MatchRelations.SET));
		relations.add(Integer.valueOf(MatchRelations.RESET));
		if (withParameter) {
			relations.add(MatcherContext.PARAMETER);
		}
		return relations;
	}
	
	/**
	 * Gets the negated matcher to maintain the backward compatibility with the older
	 * filters where the negation of the leaf node was possible 
	 * @param matcher
	 * @return
	 */
	@Override
	public int getNegatedMatcher(int matcher) {
		switch (matcher) {
		case MatchRelations.IS_NULL:			
			return MatchRelations.NOT_IS_NULL; 
		case MatchRelations.NOT_IS_NULL:			
			return MatchRelations.IS_NULL;
		case MatchRelations.SET:
			return MatchRelations.RESET;			
		case MatchRelations.RESET:
			return MatchRelations.SET;
		default: 
			return matcher;
		}
	}
	
	/**
	 * The name of the jsp fragment which contains 
	 * the control for rendering the matcher value
	 * @return
	 */
	@Override
	public String getMatchValueControlClass() {
		return MatchValueJSPNames.NONE_MATCHER_CLASS;
	}
	
}
