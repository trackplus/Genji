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

package com.aurel.track.json;

import java.util.List;

/**
 * An error message linked with a control
 * to make markInvalid 
 * @author Tamas Ruff
 *
 */
public class ControlError {
	/**
	 * The path to the control (for ex. tab -> fieldSet -> helpWrapper -> wrapper -> control)
	 */
	private List<String> controlPath;
	/**
	 * The error message to be shown for the control
	 */
	private String errorMessage;
	
	public List<String> getControlPath() {
		return controlPath;
	}
	public void setControlPath(List<String> controlPath) {
		this.controlPath = controlPath;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public ControlError(List<String> controlPath, String errorMessage) {
		super();
		this.controlPath = controlPath;
		this.errorMessage = errorMessage;
	}
	
	
}
