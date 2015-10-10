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

package com.aurel.track.admin.user.person.feature;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TUserFeatureBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.UserFeatureDAO;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.trackplus.license.LicenseManager;
import com.trackplus.license.LicensedFeature;

/**
 * Logic for user features
 * @author Tamas
 *
 */
public class UserFeatureBL {

	private static UserFeatureDAO userFeatureDAO = DAOFactory.getFactory().getUserFeatureDAO();

	/**
	 * Count the selected but disabled users by feature activated
	 * @param personIDs
	 * @param feature
	 * @return
	 */
	public static int countDisabledlWithFeature(List<Integer> personIDs, String feature) {
		return userFeatureDAO.countDisabledlWithFeature(personIDs, feature);
	}
	
	/**
	 * Count the number of users with feature 
	 * @param feature 
	 * @param active whether to count the active or inactive ones
	 * @return
	 */
	public static int countUsersWithFeature(String feature, boolean active) {
		return userFeatureDAO.countUsersWithFeature(feature, active);
	}
	
	/**
	 * Gets the user feature for a list of personIDs and a feature
	 * @param personID
	 * @param feature
	 * @return
	 */
	public static List<TUserFeatureBean> getByPersonAndFeature(Integer personID, String feature) {
		return userFeatureDAO.getByPersonAndFeature(personID, feature);
	}
	
	/**
	 * Gets the features for users
	 * @return
	 */
	public static List<TUserFeatureBean> loadAll() {
		return userFeatureDAO.loadAll();
	}
	
	/**
	 * Saves a user feature
	 * @param userFeatureBean
	 * @return
	 */
	public static Integer save(TUserFeatureBean userFeatureBean) {
		return userFeatureDAO.save(userFeatureBean);
	}
	
	/**
	 * Deletes a user feature
	 * @param personID
	 * @param feature
	 * @return
	 */
	public static void delete(Integer personID, String feature) {
		userFeatureDAO.delete(personID, feature);
	}
	
	/**
	 * Deletes all features for a list of persons
	 * @param personIDs
	 * @param feature
	 * @return
	 */
	public static void deleteFeaturesForPersons(List<Integer> personIDs) {
		userFeatureDAO.deleteFeaturesForPersons(personIDs);
	}
	
	/**
	 * Whether the user has a feature
	 * @param personID
	 * @param featureID
	 */
	public static boolean hasUserFeature(Integer personID, String featureID) {
		List<TUserFeatureBean> userFeatureList = UserFeatureBL.getByPersonAndFeature(personID, featureID);
		if (userFeatureList!=null && !userFeatureList.isEmpty()) {
			TUserFeatureBean userFeatureBean = userFeatureList.get(0);
			if (userFeatureBean!=null) {
				return userFeatureBean.isActive();
			}
		} 
		return false;
	
	}
	
	/**
	 * Number of users having a user level flag is exceeded
	 * @param actualyHavingFeature
	 * @param selectedPersonIDList
	 * @param maxHavingFeature
	 * @param newUserLevel
	 * @param locale
	 * @param userLevelFlag
	 * @param errorKey
	 * @param activateDisabledUsers
	 * @return
	 */
	public static String featureExceeded(List<Integer> selectedPersonIDList, int maxHavingFeature,
			String featureID, String featureName, Locale locale) {
		int numberOfPersonsToChange = selectedPersonIDList.size();
		int countActiveHavingFeature = countUsersWithFeature(featureID, true);
		if (countActiveHavingFeature + numberOfPersonsToChange>maxHavingFeature) {
			//count only the really disabled persons from the selected ones (the active ones are already counted in actualyHavingFeature)
			int countDisabledWithFeatureToActivate = countDisabledlWithFeature(selectedPersonIDList, featureID);
			if (countActiveHavingFeature + countDisabledWithFeatureToActivate > maxHavingFeature) {
				return LocalizeUtil.getParametrizedString("admin.user.manage.err.featureExceeded", new Object[] {maxHavingFeature, countActiveHavingFeature, featureName}, locale);
			}
		}
		return null;
	}
	
	/**
	 * Gets the map with actually used features
	 * @param licensedFeatures
	 * @return
	 */
	public static Map<String, Integer> getActuallyUsedFeaturesMap(List<LicensedFeature> licensedFeatures) {
		Map<String, Integer> actuallyUsedFeatureMap = new HashMap<String, Integer>();
		if (licensedFeatures!=null) {
			for (LicensedFeature licensedFeature : licensedFeatures) {
				String featureID = licensedFeature.getFeatureId();
				int activeWithFeature = countUsersWithFeature(featureID, true);
				actuallyUsedFeatureMap.put(featureID, activeWithFeature);
			}
		}
		return actuallyUsedFeatureMap;
	}
	
	/**
	 * Gets the available licensed features
	 * @return
	 */
	public static List<LicensedFeature> getLicensedFeatures() {
		List<LicensedFeature> userFeatures = new LinkedList<LicensedFeature>();
		LicenseManager licenseManager = ApplicationBean.getInstance().getLicenseManager();
		if (licenseManager!=null) {
			List<LicensedFeature> licensedFeatures = licenseManager.getLicensedFeatures(true);
			if (licensedFeatures!=null) {
				for (LicensedFeature licensedFeature : licensedFeatures) {
					Integer numberOfUsers = licensedFeature.getNumberOfUsers();
					if (numberOfUsers!=null && numberOfUsers.intValue()>0) {
						userFeatures.add(licensedFeature);
					}
				}
			}
		}
		return userFeatures;
	}
	
	/**
	 * Actualizes the user feature in the database
	 * @param personID
	 * @param featureID
	 * @param featureValue
	 */
	public static void actualizeUserFeature(Integer personID, String featureID, boolean featureValue) {
		if (featureValue) {
			List<TUserFeatureBean> userFeatureList = UserFeatureBL.getByPersonAndFeature(personID, featureID);
			TUserFeatureBean userFeatureBean = null;
			if (userFeatureList==null || userFeatureList.isEmpty()) {
				userFeatureBean = new TUserFeatureBean();
				userFeatureBean.setPerson(personID);
				userFeatureBean.setFeatureName(featureID);
			} else {
				userFeatureBean = userFeatureList.get(0);
			}
			userFeatureBean.setActive(true);
			UserFeatureBL.save(userFeatureBean);
		} else {
			UserFeatureBL.delete(personID, featureID);
		}
	}
	
	/**
	 * Gets the selected features for all users
	 * @return
	 */
	public static Map<Integer, List<TUserFeatureBean>> getAllUserFeaturesMap() {
		Map<Integer, List<TUserFeatureBean>> featureMap = new HashMap<Integer, List<TUserFeatureBean>>();
		List<TUserFeatureBean> allFeatures = loadAll();
		if (allFeatures!=null) {
			for (TUserFeatureBean userFeatureBean : allFeatures) {
				Integer personID = userFeatureBean.getPerson();
				List<TUserFeatureBean> featurtesForPerson = featureMap.get(personID);
				if (featurtesForPerson==null) {
					featurtesForPerson = new LinkedList<TUserFeatureBean>();
					featureMap.put(personID, featurtesForPerson);
				}
				featurtesForPerson.add(userFeatureBean);
			}
		}
		return featureMap;
	}
}
