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



package com.aurel.track.exchange.msProject.importer;

public class MSProjectImportException extends Exception {

	private static final long serialVersionUID = 1L;	
	
	/**
     * Empty constructor for serialization and nothing else.
     */
    public MSProjectImportException() {
        super();
    }
    
    /**
     * Constructs a new exception for the specified reason.
     * @param reason the reason message for the exception
     */
    public MSProjectImportException(String reason) {
        super(reason);        
    }

	
}
