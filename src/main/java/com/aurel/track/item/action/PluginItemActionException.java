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


package com.aurel.track.item.action;

public class PluginItemActionException extends Exception {
	private static final long serialVersionUID = 8101801270817691366L;
	private String fieldName;
	private String localizedErrorKey;
	private String localizedParam;
	public PluginItemActionException() {
		super();
	}

	public PluginItemActionException(String message, Exception cause) {
		super(message, cause);
	}

	public PluginItemActionException(String message) {
		super(message);
	}

	public PluginItemActionException(Exception cause) {
		super(cause);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getLocalizedErrorKey() {
		return localizedErrorKey;
	}
	public void setLocalizedErrorKey(String localizedErrorKey) {
		this.localizedErrorKey = localizedErrorKey;
	}

	public String getLocalizedParam() {
		return localizedParam;
	}

	public void setLocalizedParam(String localizedParam) {
		this.localizedParam = localizedParam;
	}
}
