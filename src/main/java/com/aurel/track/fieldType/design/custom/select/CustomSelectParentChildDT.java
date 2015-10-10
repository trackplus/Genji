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


package com.aurel.track.fieldType.design.custom.select;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.fieldType.runtime.custom.select.CascadingHierarchy;

/**
 * Custom cascading select for design time
 * Is similar to CustomSelectSimpleDT because it is 
 * configured like a single list at design time.
 * @author Tamas Ruff
 *
 */
public class CustomSelectParentChildDT extends CustomSelectCascadingBaseDT {
	
	public CustomSelectParentChildDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public CustomSelectParentChildDT(String pluginID) {
		super(pluginID);
	}

	@Override
	public CascadingHierarchy getCascadingHierarchy() {
		CascadingHierarchy cascadingHierarchyParent = new CascadingHierarchy();
		cascadingHierarchyParent.setParentID(Integer.valueOf(1));
		
		List childList = new ArrayList();
		CascadingHierarchy cascadingHierarchyChild = new CascadingHierarchy();
		cascadingHierarchyChild.setParentID(Integer.valueOf(2));
		childList.add(cascadingHierarchyChild);		
		cascadingHierarchyParent.setChildLists(childList);
		
		return cascadingHierarchyParent;
	}
					
}
