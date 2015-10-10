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


package com.aurel.track.fieldType.fieldChange.config;

import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.fieldChange.FieldChangeSetters;
import com.aurel.track.fieldType.fieldChange.FieldChangeTemplates;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.json.JSONUtility;

/**
 * Configure field change for parent field
 * @author Tamas
 *
 */
public class ItemPickerFieldChangeConfig extends ParentFieldChangeConfig {
	private static final Logger LOGGER = LogManager.getLogger(ItemPickerFieldChangeConfig.class);
	public ItemPickerFieldChangeConfig(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * Gets the name of the jsp fragment which contains 
	 * the control for rendering the bulk value
	 * @return
	 */
	public String getValueRendererJsClass() {
		switch (setter) {
		case FieldChangeSetters.SET_TO:
		case FieldChangeSetters.ADD_ITEMS:
		case FieldChangeSetters.REMOVE_ITEMS:
			return FieldChangeTemplates.PARENT_TEMPLATE;
		}
		return FieldChangeTemplates.NONE_TEMPLATE;
	}

	/**
	 * Adds the field type specific json content to stringBuilder (without curly brackets)
	 * @param stringBuilder
	 * @param value
	 * @param dataSource
	 * @param personBean
	 * @param locale
	 */
	public void addSpecificJsonContent(StringBuilder stringBuilder, Object value,
			Object dataSource, TPersonBean personBean, Locale locale) {
		if (value!=null) {
			Integer[] itemIDs = (Integer[])value;
			if (itemIDs.length>0) {
				Integer itemID = itemIDs[0];
				if (itemID!=null) {
					TWorkItemBean workItemBean;
					try {
						workItemBean = ItemBL.loadWorkItem(itemID);
						if (workItemBean!=null) {
							JSONUtility.appendStringValue(stringBuilder, "txtIssueNo", ItemBL.getItemNo(workItemBean));
							JSONUtility.appendStringValue(stringBuilder, "txtIssueTitle", workItemBean.getSynopsis());
						}
					} catch (ItemLoaderException e) {
						LOGGER.debug("Loading the item " + itemID + " failed with " + e.getMessage(), e);
					}
				}
				JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, itemID);
			}
		}
	}

}
