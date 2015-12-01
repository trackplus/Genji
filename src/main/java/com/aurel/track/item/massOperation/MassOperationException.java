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

package com.aurel.track.item.massOperation;

import java.util.List;

/**
 * Stores the error data for a failed mass operation
 * @author Tamas Ruff
 *
 */
public class MassOperationException extends Exception {
	
	public static final long serialVersionUID = 400L;
	
	private Integer errorCode;
	private List<String> errorList;
	private String allowedMessage;
	
	public MassOperationException(Integer errorCode, List<String> errorList,
			String allowedMessage) {
		super();
		this.errorCode = errorCode;
		this.errorList = errorList;
		this.allowedMessage = allowedMessage;
	}
	
	public MassOperationException(List<String> errorList) {
		super();		
		this.errorList = errorList;		
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public List<String> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}
	public String getAllowedMessage() {
		return allowedMessage;
	}
	public void setAllowedMessage(String allowedMessage) {
		this.allowedMessage = allowedMessage;
	}
	
	
}
