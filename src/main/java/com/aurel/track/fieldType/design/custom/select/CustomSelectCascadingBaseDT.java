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


package com.aurel.track.fieldType.design.custom.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.OptionSettingsDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.custom.select.CascadingHierarchy;
import com.aurel.track.resources.LocalizeUtil;

/**
 * Custom cascading select for design time
 * Is similar to CustomSelectSimpleDT because it is 
 * configured like a single list at design time.
 * @author Tamas Ruff
 *
 */
public abstract class CustomSelectCascadingBaseDT extends CustomSelectSimpleDT {
	
	public CustomSelectCascadingBaseDT(Integer parameterCode, String pluginID) {
		super(parameterCode, pluginID);		
	}

	public CustomSelectCascadingBaseDT(String pluginID) {
		super(pluginID);
	}

	/**
	 * Get the structure of the cascading selects
	 * @return
	 */
	public abstract CascadingHierarchy getCascadingHierarchy(); 

	
	@Override
	public List<ErrorData> isValidSettings(Map<Integer, Object> settings) {
		List<ErrorData> errorDataList = new ArrayList<ErrorData>();		
		TOptionSettingsBean optionSettingsBean = (TOptionSettingsBean)settings.get(mapParameterCode);
		if (optionSettingsBean.getList()==null) {
			errorDataList.add(new ErrorData("customSelectParentChild.error.noList"));
		}
		return errorDataList;
	}
	
	/**
	 * Before calling this method the struts action 
	 * should prepare the map in such a way that it contains
	 * the parameterCode as key and optionSettingsBean as value
	 * 
	 * Very important: cascading select behaves like a composite field
	 * at the runtime (in contrast to the design time behavior),
	 * so in OptionSettings there should be specified the parameterCode,
	 * which means the parameretCode of the parent list inside the composite
	 * (In this case new Integer(1)) 
	 * @param settings
	 * @param configID
	 */
	@Override
	public void saveSettings(Map<Integer, Object> settings, Integer configID) {		
		TOptionSettingsBean optionSettingsBean = (TOptionSettingsBean)settings.get(mapParameterCode);		
		optionSettingsBean.setConfig(configID);
		OptionSettingsBL.save(optionSettingsBean);		
	}
			
	
	@Override
	protected String getListLabel(Locale locale, String bundleName) {
		return LocalizeUtil.getLocalizedText("customSelectParentChild.prompt.list",
				locale, bundleName);
	}
	
	/**
	 * Gets the data specific to configuring the field
	 * Specifically useful when select lists should 
	 * be present at configuration time
     * @param projectID
	 * @param locale
	 * @param fieldConfigID
	 * @return
	 */
	@Override
	protected List<ILabelBean> getLists(Integer projectID, Locale locale, Integer fieldConfigID) {		
		Integer currentValue = null;
		if (fieldConfigID!=null) {					
			 TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(fieldConfigID, null);
			 if (optionSettingsBean!=null) {
				 currentValue = optionSettingsBean.getList();
			 }
		}		
		return (List)ListBL.getSelectsOfType(projectID, TListBean.LIST_TYPE.CASCADINGPARENT, getCascadingHierarchy(), currentValue);		
	}
}
