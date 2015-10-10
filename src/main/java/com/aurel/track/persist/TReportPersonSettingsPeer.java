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

package com.aurel.track.persist;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TReportPersonSettingsBean;
import com.aurel.track.dao.ReportPersonSettingsDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TReportPersonSettingsPeer
    extends com.aurel.track.persist.BaseTReportPersonSettingsPeer implements ReportPersonSettingsDAO
{
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TReportPersonSettingsPeer.class);

	/**
	 * Loads a TReportPersonSettingsBean by primary key
	 * @param objectID
	 * @return
	 */
	public TReportPersonSettingsBean loadByPrimaryKey(Integer objectID) {
		TReportPersonSettings tReportPersonSettings = null;
		try{
			tReportPersonSettings = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of TReportPersonSettings by primary key failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tReportPersonSettings!=null) {
			return tReportPersonSettings.getBean();
		}
		return null;
		
	}
	
	/**
	 * Loads a TReportPersonSettingsBean by person and templat
	 * @param personID
	 * @param templateID
	 * @return
	 */
	public List<TReportPersonSettingsBean> loadByPersonAndTemplate(Integer personID, Integer templateID) {
		Criteria criteria = new Criteria();
		criteria.add(PERSON, personID);
		criteria.add(REPORTTEMPLATE, templateID);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Getting the report person settings by person " + personID +
					" and template " + templateID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Saves report person settings
	 * @param reportPersonSettingsBean
	 * @return
	 */
	public Integer save(TReportPersonSettingsBean reportPersonSettingsBean) {
		try {
			TReportPersonSettings tReportPersonSettings =
					TReportPersonSettings.createTReportPersonSettings(reportPersonSettingsBean);
			tReportPersonSettings.save();
			Integer objectID = tReportPersonSettings.getObjectID();
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of a role failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	private static List<TReportPersonSettingsBean> convertTorqueListToBeanList(
			List<TReportPersonSettings> torqueList) {
		List<TReportPersonSettingsBean> beanList = new LinkedList<TReportPersonSettingsBean>();
		if (torqueList!=null) {
			for (TReportPersonSettings tReportPersonSettings : torqueList) {
				beanList.add(tReportPersonSettings.getBean());
			}
		}
		return beanList;
	}
}
