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


package com.aurel.track.admin.server.logging;

import java.io.Serializable;

import com.aurel.track.admin.customize.role.FieldForRoleBean;
import com.jgoodies.common.base.Objects;

/**
 * Simple JavaBean to represent label-value pairs for use in collections that
 * are utilized by the <code>&lt;form:options&gt;</code> tag.
 *
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 * @version $Revision: 6757 $ $Date: 2014-11-25 18:43:08 +0100 (Di, 25 Nov 2014)
 *          $
 */
public class LoggingLevelBean implements Serializable, Comparable<LoggingLevelBean> {
	// ----------------------------------------------------------- Constructors

	private static final long serialVersionUID = 340L;

	protected String className = null;
	protected String level = null;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	protected int value = 0;

	/**
	 * Construct a new LoggingLevelBean with the specified values.
	 *
	 * @param className
	 *            The class name
	 * @param level
	 *            The logging level
	 *
	 */
	public LoggingLevelBean(String className, String level, int value) {
		this.className = className;
		this.level = level;
		this.value = value;
	}

	public LoggingLevelBean() {
		this.className = null;
		this.level = null;
	}

	// ------------------------------------------------------------- Properties

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public int compareTo(LoggingLevelBean llb) {
		if (llb != null && llb.getClass().equals(this.getClass())) {
			return this.className.compareTo(llb.getClassName());
		} else {
			return -1;
		}
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof FieldForRoleBean)) {
			return false;
		}
		return Objects.equals(((LoggingLevelBean)obj).getClassName(), this.getClassName());
	}

	/** Return a string representation of this object. */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.className + "=" + this.level);
		return sb.toString();
	}



}
