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

package com.aurel.track.persist;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.dao.NotifySettingsDAO;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TNotifySettingsPeer
	extends com.aurel.track.persist.BaseTNotifySettingsPeer
	implements NotifySettingsDAO
{
	public static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TNotifySettingsPeer.class);	
	
	/**
	 * Loads a notifySettingsBean by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TNotifySettingsBean loadByPrimaryKey(Integer objectID) {
		TNotifySettings tNotifySettings = null;
		try {
			tNotifySettings = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a notificationSettings by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tNotifySettings!=null) {
			return tNotifySettings.getBean();
		}
		return null;
	}

	/**
	 * Loads a list of notifySettingsBean created by a person 
	 * @param personID 
	 * @return
	 */
	@Override
	public List<TNotifySettingsBean> loadOwnSettings(Integer personID) {
		Criteria criteria = new Criteria();
		criteria.add(PERSON, personID, Criteria.EQUAL);
		criteria.addJoin(PROJECT, TProjectPeer.PKEY);
		criteria.addAscendingOrderByColumn(TProjectPeer.LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of the notificationSettings by person " + personID + " failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Loads all default projectAssignments
	 * @return
	 */
	@Override
	public List<TNotifySettingsBean> loadAllDefaultAssignments() {
		Criteria criteria = new Criteria();
		criteria.add(PERSON, (Object)null, Criteria.ISNULL);
		criteria.addJoin(PROJECT, TProjectPeer.PKEY);
		criteria.addAscendingOrderByColumn(TProjectPeer.LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of the default notificationSettings failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Loads a list of notifySettingsBean created for 
	 * default use (not linked to a person)
	 * @param allAssignments: for system administrators show all assignments
	 * @param projectIDs: assignments for all projectAdmin projects   
	 * @return
	 */
	@Override
	public List<TNotifySettingsBean> loadDefaultsByProjects(List<Integer> projectIDs) {
		List<TNotifySettingsBean> notifySettingsBeans = new LinkedList<TNotifySettingsBean>();
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList==null || projectIDChunksList.isEmpty()) {
			return new LinkedList<TNotifySettingsBean>();
		}
		Iterator<int[]> iterator = projectIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] projectIDChunk = iterator.next();
			Criteria criteria = new Criteria();	
			criteria.addJoin(PROJECT, BaseTProjectPeer.PKEY);
			criteria.addIn(PROJECT, projectIDChunk);
			criteria.add(PERSON, (Object)null, Criteria.ISNULL);
			criteria.addAscendingOrderByColumn(BaseTProjectPeer.LABEL);
			try {
				notifySettingsBeans.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch (TorqueException e) {
				LOGGER.error("Loading of the default notificationSettings failed with " + e.getMessage());
				return null;
			}
		}	
		return notifySettingsBeans;
	}
	
	/**
	 * Get all notifySettingsBean for a project
	 * @param projectID
	 * @return
	 */
	@Override
	public List<TNotifySettingsBean> loadAllByProject(Integer projectID) {
		Criteria criteria = new Criteria();
		criteria.add(PROJECT, projectID);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of the default notificationSettings for project " + projectID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Get the default notifySettingsBean for a project (person==null)
	 * @param personID
	 * @param projectID
	 * @return
	 */
	@Override
	public TNotifySettingsBean loadDefaultByProject(Integer projectID) {
		List<TNotifySettings> notifySettingsList = new LinkedList<TNotifySettings>();
		Criteria criteria;
		criteria = new Criteria();
		criteria.add(PERSON, (Object)null, Criteria.ISNULL);
		criteria.add(PROJECT, projectID);
		try {
			notifySettingsList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading of the default notificationSettings for project " + projectID + " failed with " + e.getMessage());
		}
		if (notifySettingsList==null || notifySettingsList.isEmpty()) {
			return null; 
		} else {
			TNotifySettings notifySettings = (TNotifySettings)notifySettingsList.get(0);
			return notifySettings.getBean();
		}
	}
	
	/**
	 * Get the notifySettingsBean defined by a person for a project 
	 * @param personID
	 * @param projectID
	 * @return
	 */
	@Override
	public TNotifySettingsBean loadOwnByPersonAndProject(Integer personID, Integer projectID) {
		List<TNotifySettings> notifySettingsList = new LinkedList<TNotifySettings>();
		Criteria criteria;
		criteria = new Criteria();		
		criteria.add(PERSON, personID);
		criteria.add(PROJECT, projectID);
		try {
			notifySettingsList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading of the notificationSettings by person " + personID + 
					" and project " + projectID + " failed with " + e.getMessage(), e);
		}
		if (notifySettingsList==null || notifySettingsList.isEmpty()) {
			return null; 
		} else {
			TNotifySettings notifySettings = (TNotifySettings)notifySettingsList.get(0);
			return notifySettings.getBean();
		}
	}
	
	/**
	 * Saves a notifyTriggerBean in the database
	 * @param notifyTriggerBean 
	 * @return
	 */
	@Override
	public Integer save(TNotifySettingsBean notifySettingsBean) {
		TNotifySettings tNotifySettings;
		try {
			tNotifySettings = BaseTNotifySettings.createTNotifySettings(notifySettingsBean);
			tNotifySettings.save();
			return tNotifySettings.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a notificationSettings failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Deletes a notifySettingsBean from the database
	 * @param objectID 
	 * @return
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the notificationSettings " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Converts a list of TNotifySettings torque objects to a list of TNotifySettingsBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TNotifySettingsBean> convertTorqueListToBeanList(List<TNotifySettings> torqueList) {
		List<TNotifySettingsBean> beanList = new LinkedList<TNotifySettingsBean>();
		if (torqueList!=null) {
			for (TNotifySettings tNotifySettings : torqueList) {
				beanList.add(tNotifySettings.getBean());
			}
		}
		return beanList;
	}
	
}
