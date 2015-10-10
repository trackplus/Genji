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


package com.aurel.track.fieldType.design;


/**
 * Some system fields does not need the required flag even if 
 * they are not required: for ex. superiorWorkitem or comment
 * Also for priority and lastEditedBy: although they are required at database 
 * level accidentally they was marked as not required at TField table
 * @author Tamas Ruff
 *
 */
public class BaseFieldTypeNotRenderRequiredDT extends BaseFieldTypeDT {

	public BaseFieldTypeNotRenderRequiredDT(Integer parameterCode,
			String pluginID) {
		super(parameterCode, pluginID);	
	}

	public BaseFieldTypeNotRenderRequiredDT(String pluginID) {
		super(pluginID);
	}

	/**
	 * Whether by configuring a system or custom field 
	 * the required checkbox should appear or not.
	 * It should not appear for the required system fields 
	 * and for the types where this doesn't makes sense: ex. lables, checkboxes
	 * Overwrite it to return false for such fields 
	 * @return
	 */
	@Override
	public boolean renderRequiredFlag() {
		return false;
	}
	
	
	
		
}
