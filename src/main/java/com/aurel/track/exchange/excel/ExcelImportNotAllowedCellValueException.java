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



package com.aurel.track.exchange.excel;
/**
 * The value exists but is not allowed in the actual context
 * For example a person which has no manager right is set as manager
 * @author Tamas
 *
 */
public class ExcelImportNotAllowedCellValueException extends ExcelImportException {

	private static final long serialVersionUID = 1L;	
	
	/**
     * Empty constructor for serialization and nothing else.
     */
    public ExcelImportNotAllowedCellValueException() {
        super();
    }
    
    /**
     * Constructs a new exception for the specified reason.
     * @param reason the reason message for the exception
     */
    public ExcelImportNotAllowedCellValueException(String reason) {
        super(reason);        
    }

	
}
