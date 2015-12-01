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



package com.aurel.track.fieldType.runtime.custom.picker;

import java.util.List;

import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.fieldType.fieldChange.converter.MultipleSelectSetterConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MatcherConverter;
import com.aurel.track.fieldType.runtime.matchers.converter.MultipleSelectMatcherConverter;

/**
 * Class for project picker runtime
 * @author Tamas Ruff
 *
 */
public abstract class CustomTreePickerRT extends CustomPickerRT {
	/**
	 * Gets the flat datasource
	 * @param personID
	 * @return
	 */
	public abstract List<ILabelBean> getFlatDataSource(Integer personID);
	
	/**
	 * Whether the datasource for the picker is tree or list
	 * @return
	 */
	@Override
	public boolean isTree() {
		return true;
	}
	
	/**
	 * Gets the iconCls class for label bean if dynamicIcons is false
	 * @param labelBean
	 */
	@Override
	public String getIconCls(ILabelBean labelBean) {
		//for tree based pickers the icon is in datasource tree nodes
		return null;
	}
		
	@Override
	public MatcherConverter getMatcherConverter() {
		return MultipleSelectMatcherConverter.getInstance();
	}
	
	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	@Override
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new MultipleSelectSetterConverter(fieldID);
	}
}
