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


package com.aurel.track.fieldType.bulkSetters;

/**
 * Bulk setter for issueTypes
 * Differs from the other system selects that changing it might affect other fields
 * @author Tamas
 *
 */
public class IssueTypeBulkSetter extends SystemSelectBulkSetter {	
	public IssueTypeBulkSetter(Integer fieldID) {
		super(fieldID, true);
	}

	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	@Override
	public String getSetterValueControlClass() {
		switch (relation) {
		case BulkRelations.SET_TO:
			return BulkValueTemplates.ISSUE_TYPE_BULK_VALUE_TEMPLATE;
		}
		return BulkValueTemplates.NONE_BULK_VALUE_TEMPLATE;		
	}    	
}
