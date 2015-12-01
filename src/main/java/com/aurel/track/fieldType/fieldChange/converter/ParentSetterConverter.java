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

package com.aurel.track.fieldType.fieldChange.converter;

import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;


public class ParentSetterConverter extends SystemSelectSetterConverter {
	private static final Logger LOGGER = LogManager.getLogger(ParentSetterConverter.class);
	
	public ParentSetterConverter(Integer activityType) {
		super(activityType);
	}

	/**
	 * Gets the show value transformed form the stored configuration in database 
	 * @param value
	 * @param fieldID
	 * @param setter
	 * @param locale
	 * @return
	 */
	@Override
	public String getDisplayValueFromStoredString(String value, Integer fieldID, Integer setter, Locale locale) {
		if (value!=null) {
			Integer itemID = (Integer)getActualValueFromStoredString(value, setter);
			try {
				TWorkItemBean workItemBean = ItemBL.loadWorkItem(itemID);
				if (workItemBean!=null) {
					return ItemBL.getItemNo(workItemBean) + " " + workItemBean.getSynopsis();
				}
			} catch (ItemLoaderException e) {
				LOGGER.info("Loading the item " + itemID + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}
}
