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

package com.aurel.track.admin.customize.category.filter;

import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionInTreeTO;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;

/**
 * The QNodeExpression element for storing the filter expressions
 * @author Tamas
 *
 */
public class QNodeExpression extends QNode implements IMatcherValue {
		
	private static final long serialVersionUID = 3989022005578474062L;
	private Integer field;
	private Integer fieldMoment;
	private Integer matcherID;
	private Object value;
	
	
	
	public QNodeExpression() {
		super();
		this.setType(QNode.EXP);
	}

	public QNodeExpression(FieldExpressionInTreeTO fieldExpressionInTree) {
		super();
		this.field = fieldExpressionInTree.getField();
		this.fieldMoment = fieldExpressionInTree.getFieldMoment();
		this.matcherID = fieldExpressionInTree.getSelectedMatcher();
		this.value = fieldExpressionInTree.getValue();	
		this.setType(QNode.EXP);
	}
	
	public Integer getField() {
		return field;
	}

	public void setField(Integer fieldID) {
		this.field = fieldID;
	}

	public Integer getMatcherID() {
		return matcherID;
	}

	public void setMatcherID(Integer matcherID) {
		this.matcherID = matcherID;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the fieldMoment
	 */
	public Integer getFieldMoment() {
		return fieldMoment;
	}

	/**
	 * @param fieldMoment the fieldMoment to set
	 */
	public void setFieldMoment(Integer fieldMoment) {
		this.fieldMoment = fieldMoment;
	}
}
