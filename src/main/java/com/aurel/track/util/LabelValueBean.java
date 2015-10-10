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
 * that are utilized by the <code>&lt;form:options&gt;</code> tag.
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 * @version $Revision: 4738 $ $Date: 2013-04-26 15:13:18 +0200 (Fr, 26 Apr 2013) $
 */
public class LabelValueBean implements Serializable, Comparable<LabelValueBean> {
	// ----------------------------------------------------------- Constructors

	private static final long serialVersionUID = -3841639032187333741L;

	/**
	 * Construct a new LabelValueBean with the specified values.
	 * @param label The label to be displayed to the user
	 * @param value The value to be returned to the server
	 */
	public LabelValueBean(String label, String value) {
		this.label = label;
		this.value = value;
	}

	public LabelValueBean() {
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
	protected String value = null;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	// --------------------------------------------------------- Public Methods

	@Override
	public boolean equals(Object obj) {
		return isEqual((LabelValueBean) obj);
	}

	public boolean isEqual(LabelValueBean lvb) {
		if (this.value.equals(lvb.getValue())) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (this.value == null) {
			return 0;
		}
		else {
			try {
				return Integer.parseInt(this.value);
			}
			catch (NumberFormatException e) {
				return 0;
			}
		}
	}

	/** Return a string representation of this object. */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("value = " + this.value + ", ");
		sb.append("label = " + this.label);
		return sb.toString();
	}

	/**
	 * Comparation is made by the label
	 */
	@Override
	public int compareTo(LabelValueBean labelValueBean) {
		if (labelValueBean==null) {
			return 1;
		}
		if (this.label==null && labelValueBean.label==null) {
			return 0;
		}
		if (this.label==null) {
			return -1;
		}
		if (labelValueBean.label==null) {
			return 1;
		}
		return this.label.compareTo(labelValueBean.label);
	}



}
