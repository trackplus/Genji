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
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TUserFeatureBean;
import com.aurel.track.dao.UserFeatureDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TUserFeaturePeer
    extends com.aurel.track.persist.BaseTUserFeaturePeer implements UserFeatureDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TUserFeaturePeer.class); 

	
	/**
	 * Count the selected but disabled users by feature activated
	 * @param personIDs
	 * @param feature
	 * @return
	 */
	public int countDisabledlWithFeature(List<Integer> personIDs, String feature) {
		if (personIDs!=null && !personIDs.isEmpty()) {
			String COUNT = "count(" + OBJECTID + ")";
			Criteria crit = new Criteria();
			crit.add(FEATURENAME, feature);
			crit.addIn(PERSON, personIDs);
			crit.addJoin(TPersonPeer.PKEY, PERSON);
			crit.add(TPersonPeer.DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.EQUAL);
			crit.add(ISACTIVE, (Object)BooleanFields.TRUE_VALUE);
			crit.addSelectColumn(COUNT);
			try {
				return ((Record)doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
			} catch (Exception e) {
				LOGGER.error("Count the selected but disabled users  " + personIDs.size() + " with feature " + feature + " failed with " + e.getMessage(), e);
			}
		}
		return 0;
	}
	
	/**
	 * Count the number of users with feature 
	 * @param feature 
	 * @param active whether to count the active or inactive ones
	 * @return
	 */
	public int countUsersWithFeature(String feature, boolean active) {
		Criteria crit = new Criteria();
		String count = "COUNT(" + OBJECTID + ")";
		crit.addSelectColumn(count);
		crit.add(FEATURENAME, feature);
		crit.addJoin(TPersonPeer.PKEY, PERSON);
		crit.add(ISACTIVE, (Object)BooleanFields.TRUE_VALUE);
		if (active) {
			crit.add(TPersonPeer.DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		} else {
			crit.add(TPersonPeer.DELETED, (Object)BooleanFields.TRUE_VALUE, Criteria.EQUAL);
		}
		crit.getCriterion(TPersonPeer.DELETED).setIgnoreCase(true);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		}
		catch (Exception e) {
			LOGGER.error("Counting active " + active + " persons with feature " + feature + " failed with " + e.getMessage(), e);
		}
		return 0;
	}
	
	/**
	 * Gets the user feature for person and feature
	 * @param personID
	 * @param feature
	 * @return
	 */
	public List<TUserFeatureBean> getByPersonAndFeature(Integer personID, String feature) {
		Criteria crit = new Criteria();
		crit.add(PERSON, personID);
		crit.add(FEATURENAME, feature);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading feature bean for person " + personID  +" and feature " + feature + " failed with " + e);
			return null;
		}
	}
	
	/**
	 * Gets the features for users
	 * @return
	 */
	public List<TUserFeatureBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading features for all persons failed with " + e);
			return null;
		}
	}
	
	/**
	 * Saves a user feature
	 * @param userFeatureBean
	 * @return
	 */
	public Integer save(TUserFeatureBean userFeatureBean) {
		try {
			TUserFeature tUserFeature = BaseTUserFeature.createTUserFeature(userFeatureBean);
			tUserFeature.save();
			return tUserFeature.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a user feature failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}
	
	/**
	 * Deletes a user feature
	 * @param personID
	 * @param feature
	 * @return
	 */
	public void delete(Integer personID, String feature) {
		Criteria crit = new Criteria();
		crit.add(PERSON, personID);
		crit.add(FEATURENAME, feature);
		try {
			doDelete(crit);
		} catch (Exception e) {
			LOGGER.error("Deleting the user feature by person  " + personID +
					", feature " +	feature + " failed with " + e.getMessage(), e);
		}	
	}
	
	/**
	 * Deletes all features for a list of persons
	 * @param personIDs
	 * @param feature
	 * @return
	 */
	public void deleteFeaturesForPersons(List<Integer> personIDs) {
		if (personIDs!=null && !personIDs.isEmpty()) {
			Criteria crit = new Criteria();
			crit.addIn(PERSON, personIDs);
			try {
				doDelete(crit);
			} catch (Exception e) {
				LOGGER.error("Deleting the features for " + personIDs.size() +
						" persons failed with " + e.getMessage(), e);
			}
		}
	}
	
	private static List<TUserFeatureBean> convertTorqueListToBeanList(List<TUserFeature> torqueList) {
		List<TUserFeatureBean> beanList = new LinkedList<TUserFeatureBean>();
		if (torqueList!=null){
			for (TUserFeature tUserFeature : torqueList) {
				beanList.add(tUserFeature.getBean());
			}
		}
		return beanList;
	}
}
