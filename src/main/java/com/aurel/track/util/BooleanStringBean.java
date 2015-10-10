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


package com.aurel.track.util;

import java.io.Serializable;

/**
 * Simple JavaBean to represent label-value pairs for use in collections
 * The label is String and the value is boolean
 * @author Tamas Ruff
 */
public class BooleanStringBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The label to be displayed to the user. */
	protected String label = null;

	/** The value to be returned to the server. */
	protected Boolean value = null;

	
	/**
	 * Construct a new LabelValueBean with the specified values.
	 * @param label The label to be displayed to the user
	 * @param value The value to be returned to the server
	 */
	public BooleanStringBean(String label, Boolean value) {
		this.label = label;
		this.value = value;
	}

	public BooleanStringBean() {
		this.label = null;
		this.value = null;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

   
	public void setValue(Boolean value) {
		this.value = value;
	}

	public Boolean getValue() {
		return this.value;
	}
	
	@Override
	public boolean equals(Object obj) {
		BooleanStringBean booleanStringBean = null;
		try {
			booleanStringBean = (BooleanStringBean)obj;
		} catch(Exception e) {
			return false;
		}
		if (booleanStringBean==null) {
			return false;
		}
		if (this.value==null || booleanStringBean.value==null) {
			return false;
		}
		return this.value.equals(booleanStringBean.getValue());
	}
	
	@Override
	public int hashCode() {
		if (this.value == null || this.value.booleanValue() == false) {
			return 0;
		} else {
			return 1;
		}
	}

}

