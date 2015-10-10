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

package com.aurel.track.exchange.track.importer;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.errors.ErrorData;

/**
 * Exception list for gathering more exceptions and throw it just later if it is the case 
 * @author Tamas
 *
 */
public class ImportExceptionList extends Exception {
		
	private static final long serialVersionUID = 1L;
	//the exception may not be thrown instantly, but they will be collected in a list
	//and thrown only if the list is not empty to avoid importing again and again for the same type of error
	protected List<ErrorData> errorDataList = new ArrayList<ErrorData>();
		
	public ImportExceptionList() {
		super();
	}

	public ImportExceptionList(String message, Exception cause) {
		super(message, cause);
	}

	public ImportExceptionList(String message) {
		super(message);
	}

	public ImportExceptionList(Exception cause) {
		super(cause);
	}

	public List<ErrorData> getErrorDataList() {		
		return errorDataList;
	}
	
	
	public void setErrorDataList(List<ErrorData> errorDataList) {
		this.errorDataList = errorDataList;
	}

	public boolean containsException() {
		return errorDataList!=null && errorDataList.size()!=0;
	}

}
