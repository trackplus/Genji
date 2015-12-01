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


package com.aurel.track.fieldType.runtime.callbackInterfaces;

import java.util.List;

import com.aurel.track.fieldType.runtime.base.SelectContext;

/**
 * Implemented by list based controls which need a dataSource to select from:
 * system selects, custom simple selects, simple select parts of custom composites,
 * custom radio buttons etc. 
 * @author Tamas Ruff
 *
 */
public interface ISelect extends ILookup {
	
	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field by editing an existing issue.
	 * The result might differ from loadCreateDataSource because it might be needed 
	 * to add the current value from the workItem to the list entries 
	 * (when right already revoked for the current value).
	 * @param selectContext
	 * @return
	 */
	List loadEditDataSource(SelectContext selectContext);
	
	/**
	 * Actualizes the dropDownContainer with the datasource for a select type field by creating a new issue.
	 * It might differ from loadEditDataSource because it might update this field in a workItem
	 * if the actual value from the workItem is not contained in the resulted select list.
	 * In this case the when a default value is defined (for ex. default manager, responsible, issueType for project) 
	 * the field will be set to this value (if the default value is contained in the resulted list), 
	 * otherwise to the first item from the list. 
	 * When the list is empty this field should be set explicitly to null in the workItem. 
	 * (An empty HTML list will not send request parameter and it will remain the original value. This should be avoided)
	 * So don't forget to actualize the workItem if needed. It is needed mainly by multiple cascading selects  
	 * @param selectContext
	 * @return
	 */
	List loadCreateDataSource(SelectContext selectContext);
	
	
	
}
