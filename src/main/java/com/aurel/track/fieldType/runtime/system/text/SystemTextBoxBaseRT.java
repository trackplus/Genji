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


package com.aurel.track.fieldType.runtime.system.text;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.SystemInputBaseRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;

public abstract class SystemTextBoxBaseRT extends SystemInputBaseRT {		
		
		private static final Logger LOGGER = LogManager.getLogger(SystemTextBoxBaseRT.class);
		
		/**
		 * Sets the  default values for a field on the workItem from the config settings
		 * @param fieldID
		 * @param parameterCode
		 * @param validConfig
		 * @param fieldSettings
		 * @param workItemBean
		 * @return
		 */
		@Override
		public void setDefaultAttribute(Integer fieldID, Integer parameterCode, 
				Integer validConfig, Map<String, Object> fieldSettings, TWorkItemBean workItemBean) {
			String mergedKey = MergeUtil.mergeKey(fieldID, parameterCode);
			if (fieldSettings!=null) {
			Object settingsObject = fieldSettings.get(mergedKey);
				if (settingsObject!=null) {
					TTextBoxSettingsBean textBoxSettingsBean = null;
					try {
						textBoxSettingsBean = (TTextBoxSettingsBean)settingsObject;
					} catch(Exception ex) {
						LOGGER.error("Getting the settings object for SystemTextBaseRT failed with " + ex.getMessage());
					}
					if (textBoxSettingsBean!=null) {
						Object defaultValue = getSpecificDefaultAttribute(textBoxSettingsBean);
						//set the attribute on workItem
						workItemBean.setAttribute(fieldID, parameterCode, defaultValue);
					}
				}		
			}
		}
		
		/**
		 * Get the specific attribute from TTextBoxSettingsBean for default value
		 */
		public abstract Object getSpecificDefaultAttribute(TTextBoxSettingsBean textBoxSettingsBean);			
}
