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

package com.aurel.track.admin.customize.category.filter.tree.design;

import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;

/**
 * Base for all expressions: selects, simple and "in tree" filter expressions 
 */
public class MatcherExpressionBase implements IMatcherValue {
	protected Integer field;
	protected String fieldLabel;
	protected Object value;
	protected String valueRenderer;
	
	public MatcherExpressionBase() {
		super();
	}
	
	public MatcherExpressionBase(Integer fieldID, Object value) {
		super();
		this.field = fieldID;
		this.value = value;
	}
	
	public Integer getField() {
		return field;
	}	

	public void setField(Integer field) {
		this.field = field;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getValueRenderer() {
		return valueRenderer;
	}

	public void setValueRenderer(String valueRenderer) {
		this.valueRenderer = valueRenderer;
	}
	
	
}
