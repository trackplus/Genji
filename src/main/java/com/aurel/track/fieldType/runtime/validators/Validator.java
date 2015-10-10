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


package com.aurel.track.fieldType.runtime.validators;

import com.aurel.track.errors.ErrorData;

/**
 * Interface for validating a fields before saving it
 * Cannot be implemented by composite custom fields
 * but can be implemented by any other field  
 * @author Tamas Ruff
 *
 */
public interface Validator {
	
	
	/**
	 * Validates an value for a field and returns ErrorData in case of validation error
	 * @param value 
	 * @return
	 */
	public abstract ErrorData validateField(Object value);
	
	
}
