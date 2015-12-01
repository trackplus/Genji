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


package com.aurel.track.fieldType.runtime.matchers;

/**
 * Base class fot for AbstractMatcherDT and AbstractMatcherRT
 * @author Tamas Ruff
 *
 */
public abstract class AbstractMatcherBase implements IMatcherBase {
	//the match value, the actual attribute value will be compared with
	protected Object matchValue;
	//the relation between the match value and the the actual attribute
	protected int relation;
	//the field id
	protected Integer fieldID;


	public AbstractMatcherBase(Integer fieldID) {
		this.fieldID = fieldID;
	}

	/**
	 * @return the fieldID
	 */
	public Integer getFieldID() {
		return fieldID;
	}


	/**
	 * @return the matchValue
	 */
	@Override
	public Object getMatchValue() {
		return matchValue;
	}


	/**
	 * @param matchValue the matchValue to set
	 */
	@Override
	public void setMatchValue(Object matchValue) {
		this.matchValue = matchValue;
	}


	/**
	 * @return the relation
	 */
	@Override
	public int getRelation() {
		return relation;
	}


	/**
	 * @param relation the relation to set
	 */
	public void setRelation(int relation) {
		this.relation = relation;
	}


}
