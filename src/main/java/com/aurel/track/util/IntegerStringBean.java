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


package com.aurel.track.util;

import java.io.Serializable;

/**
 * Simple JavaBean to represent label-value pairs for use in collections
 * The label is String and the value is Integer
 * @author Tamas Ruff
 */
public class IntegerStringBean implements Serializable, Comparable<IntegerStringBean> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ----------------------------------------------------------- Constructors

	

	/**
     * Construct a new LabelValueBean with the specified values.
     * @param label The label to be displayed to the user
     * @param value The value to be returned to the server
     */
    public IntegerStringBean(String label, Integer value) {
        this.label = label;
        this.value = value;
	}

	public IntegerStringBean() {
		this.label = null;
		this.value = null;
	}
    // ------------------------------------------------------------- Properties

    /** The label to be displayed to the user. */
    protected String label = null;

	public void setLabel(String label) {
		this.label = label;
	}

    public String getLabel() {
        return this.label;
    }

    /** The value to be returned to the server. */
    protected Integer value = null;

	public void setValue(Integer value) {
		this.value = value;
	}

    public Integer getValue() {
		return this.value;
    }

    // --------------------------------------------------------- Public Methods


	@Override
	public boolean equals(Object obj) {
		IntegerStringBean integerStringBean = null;
		try {
			integerStringBean = (IntegerStringBean)obj;
		} catch(Exception e) {
			return false;
		}
		if (integerStringBean==null) {
			return false;
		}
		if (this.value==null || integerStringBean.value==null) {
			return false;
		}
		return this.value.equals(integerStringBean.getValue());
	}

	@Override
	public int hashCode() {
		if (this.value == null) {
			return 0;
		}
		else {
			return this.value.intValue();
		}
	}

	/**
	 * Comparation is made by the label
	 */
	public int compareTo(IntegerStringBean integerStringBean) {
		if (integerStringBean==null) {
			return 1;
		}
		if (this.label==null && integerStringBean.label==null) {
			return 0;
		}
		if (this.label==null) {
			return -1;
		}
		if (integerStringBean.label==null) {
			return 1;
		}
		return this.label.compareTo(integerStringBean.label);
	}

	
}

