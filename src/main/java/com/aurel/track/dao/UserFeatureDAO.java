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

package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TUserFeatureBean;

/**
 * DAO for user features
 * @author Tamas Ruff
 *
 */
public interface UserFeatureDAO {

	
	/**
	 * Count the selected but disabled users by feature activated
	 * @param personIDs
	 * @param feature
	 * @return
	 */
	int countDisabledlWithFeature(List<Integer> personIDs, String feature); 
	
	/**
	 * Count the number of users with feature 
	 * @param feature 
	 * @param active whether to count the active or inactive ones
	 * @return
	 */
	int countUsersWithFeature(String feature, boolean active);
	
	/**
	 * Gets the user feature for person and feature
	 * @param personID
	 * @param feature
	 * @return
	 */
	List<TUserFeatureBean> getByPersonAndFeature(Integer personID, String feature);
	
	/**
	 * Gets the features for users
	 * @return
	 */
	List<TUserFeatureBean> loadAll();
	
	/**
	 * Saves a user feature
	 * @param userFeatureBean
	 * @return
	 */
	Integer save(TUserFeatureBean userFeatureBean);
	
	/**
	 * Deletes a user feature
	 * @param personID
	 * @param feature
	 * @return
	 */
	void delete(Integer personID, String feature);
	
	/**
	 * Deletes all features for a list of persons
	 * @param personIDs
	 * @param feature
	 * @return
	 */
	void deleteFeaturesForPersons(List<Integer> personIDs); 
}
