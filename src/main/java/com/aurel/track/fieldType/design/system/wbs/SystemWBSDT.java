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

package com.aurel.track.fieldType.design.system.wbs;

import java.util.Locale;

import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.design.BaseFieldTypeNotRenderRequiredAndHistoryDT;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;

public class SystemWBSDT extends BaseFieldTypeNotRenderRequiredAndHistoryDT {
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	public SystemWBSDT(Integer parameterCode,
			String pluginID) {
		super(parameterCode, pluginID);	
	}

	public SystemWBSDT(String pluginID) {
		super(pluginID);
	}
	
	/**
	 * Executes a command
	 * @param configItem
	 */
	@Override
	public void executeCommand(ConfigItem configItem) {
		Integer projectID = configItem.getProject();
		if (projectID!=null) {
			workItemDAO.setWbs(projectID);
		}
	}
	
	/**
	 * Gets the specific JSON string for a field
	 * @param configID ID of the direct or nearest fallback configuration 
	 * @param treeConfigIDTokens a decoded node
	 * @param personBean
	 * @param locale
	 * @param bundleName
	 * @return
	 */
	@Override
	public String getSettingsJSON(Integer configID, 
			TreeConfigIDTokens treeConfigIDTokens, TPersonBean personBean, Locale locale, String bundleName) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendBooleanValue(stringBuilder,
				"projectSpecific", treeConfigIDTokens!=null && treeConfigIDTokens.getProjectID()!=null);				
		return stringBuilder.append(getLocalizationJSON(locale, bundleName)).toString(); 
	}
	
	/**
	 * Gets the localized labels used in field specific configuration
	 * (are common for getSettingsJSON() and getDefaultSettingsJSON())  
	 * @param locale
	 * @param bundleName 
	 * @return
	 */
	@Override
	public String getLocalizationJSON(Locale locale, String bundleName) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONUtility.appendStringValue(stringBuilder,
				"renumberLabel",
				LocalizeUtil.getLocalizedText("common.btn.renumber",
						locale, bundleName));	
		return stringBuilder.toString(); 
	}
}
