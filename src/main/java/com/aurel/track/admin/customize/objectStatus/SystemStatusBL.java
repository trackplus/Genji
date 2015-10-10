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


package com.aurel.track.admin.customize.objectStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.aurel.track.admin.customize.lists.BlobBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SystemStateDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * DB helper methods for configuring the option statuses 
 * @author Tamas Ruff
 *
 */
public class SystemStatusBL {
			
	private static SystemStateDAO systemStateDAO = DAOFactory.getFactory().getSystemStateDAO();
	
	/**
	 * Gets the options for an entityFlag.
	 * Do not modify because the LookupContainer is loaded using this method (avoid infinite cycles)
	 * @param entityFlag
	 * @return
	 */
	public static List<TSystemStateBean> getStatusOptions(Integer entityFlag) {
		return systemStateDAO.loadAllForEntityFlag(entityFlag);
	}
	
	/**
	 * Get the stateIDs by entityType an state flags
	 * @param entityType
	 * @param stateFlag
	 * @return
	 */
	public static List<TSystemStateBean> getSystemStatesByByEntityAndStateFlags(Integer entityType, int[] stateFlagArr) {
		List<TSystemStateBean> systemList = LookupContainer.getSystemStateList(entityType);
		List<TSystemStateBean> systemListByStateFlag = new LinkedList<TSystemStateBean>();
		Set<Integer> stateFlagsSet = GeneralUtils.createSetFromIntArr(stateFlagArr);
		if (systemList!=null) {
			for (TSystemStateBean systemStateBean : systemList) {
				Integer stateFlag = systemStateBean.getStateflag();
				if (stateFlagsSet.contains(stateFlag)) {
					systemListByStateFlag.add(systemStateBean);
				}
			}
		}
		return systemListByStateFlag;//systemStateDAO.loadWithStateFlagForEntity(entityType, stateFlag);
	}
	
	/**
	 * Get the stateIDs by entityType an state flags
	 * @param entityType
	 * @param stateFlag
	 * @return
	 */
	public static int[] getStateIDsByEntityAndStateFlags(Integer entityType, int[] stateFlag) {
		int[] stateIDs = null;
		List<TSystemStateBean> projectStates = getSystemStatesByByEntityAndStateFlags(entityType, stateFlag);
		if (projectStates!=null) {
			stateIDs = new int[projectStates.size()];
			for (int i=0; i<projectStates.size(); i++) {
				stateIDs[i]=(projectStates.get(i)).getObjectID().intValue();
			}
		}
		return stateIDs;
	}
	
	
	
	/**
	 * Gets the localized options for an entity
	 * @param locale
	 * @return
	 */
	public static List<TSystemStateBean> getStatusOptions(Integer entityFlag, Locale locale) {
		List<TSystemStateBean> statusOptions = LookupContainer.getSystemStateList(entityFlag);
		if (statusOptions!=null) {
			for (TSystemStateBean systemStateBean : statusOptions) {
				systemStateBean.setLabel(LocalizeUtil.localizeSystemStateEntry(systemStateBean, entityFlag, locale));
			}
		}
		return statusOptions;
	}
	
	/**
	 * Gets the status flag for an objectID
	 * @param statusID
	 * @return
	 */
	public static Integer getStatusFlagForStatusID(Integer entityType, Integer statusID) {
		List<TSystemStateBean> systemStateList = LookupContainer.getSystemStateList(entityType);
		if (systemStateList!=null) {
			for (TSystemStateBean systemStateBean : systemStateList) {
				Integer objectID = systemStateBean.getObjectID();
				if (objectID.equals(statusID)) {
					return systemStateBean.getStateflag();
				}
			}
		}
		TSystemStateBean systemStateBean = systemStateDAO.loadByPrimaryKey(statusID);
		if (systemStateBean!=null) {
			return systemStateBean.getStateflag();
		}
		return null;
	}
	
	/**
	 * Get the status flag of the entity
	 * @param entityID
	 * @return
	 */
	public static int getStatusFlag(Integer entityID, int entityFlag) {
		return systemStateDAO.getStatusFlag(entityID, entityFlag);
	}
	
	/**
	 * Gets the specific label bean by optionID 
	 * @param optionID
	 * @return 
	 */
	static TSystemStateBean getExistingSystemOptionBeanByOptionID(Integer optionID) {
		return systemStateDAO.loadByPrimaryKey(optionID);
	}
	
	/**
	 * Load by roleIDs
	 * @return
	 */
	public static List<TSystemStateBean> loadByStateIDs(List<Integer> primaryKeys) {
		return systemStateDAO.loadByStateIDs(primaryKeys);
	}
	
	/**
	 * Loads all SystemStateBeans
	 * @return 
	 */
	public static List<TSystemStateBean> loadAll() {
		return systemStateDAO.loadAll();
	}
	
	
	/**
	 * Gets the specific label bean by label
	 * @param listID
	 * @param label
	 * @return 
	 */
	static List<TSystemStateBean> getExistingLabelBeansByLabel(Integer listID, String label) {
		return systemStateDAO.loadByLabel(listID, label);
	}	
	
	/**
	 * Gets the sort order column name 
	 * @return
	 */
	static String getSortOrderColumn() {
		return systemStateDAO.getSortOrderColumn();
	}
	
	/**
	 * Gets the table the sorting is made in 
	 * @return
	 */
	static String getTableName() {
		return systemStateDAO.getTableName();
	}
	
	/**
	 * Saves the label bean into the database  
	 */
	static Integer saveSimple(ILabelBean labelBean) {
		TSystemStateBean systemStateBean = (TSystemStateBean)labelBean;
		Integer systemStatusID = systemStateDAO.save(systemStateBean);
		LookupContainer.resetSystemStateMap(systemStateBean.getEntityflag());
		ClusterMarkChangesBL.markDirtySystemStatesListEntryInCluster(systemStateBean.getEntityflag(), systemStatusID);
		return systemStatusID;
	}
	
	/**
	 * Saves a new/modified list entry
	 * If sortOrder is not set it will be set to be the 
	 * next available sortOrder according to listID and parentID   
	 * @param systemStateBean
	 * @param locale
	 * @return
	 */
	static synchronized Integer save(TSystemStateBean systemStateBean, Locale locale) {
		boolean isNew = systemStateBean.getObjectID()==null;
		if (systemStateBean.getSortorder()==null) {
			Integer sortOrder = systemStateDAO.getNextSortOrder(systemStateBean.getEntityflag()); 
			systemStateBean.setSortorder(sortOrder);
		}
		Integer objectID = systemStateDAO.save(systemStateBean);
		if (isNew) {
			systemStateBean.setObjectID(objectID);
		} else {
			//The localization is loaded from localized resources table, and if not found, 
			//from the localized property files, and if not found from the "native" table. 
			//The value should be saved also with the default locale only if it is edited 
			//(new entities have no correspondence in the localized property files)  
			LocalizeBL.saveSystemStateLocalizedResource(systemStateBean.getEntityflag(), 
					systemStateBean.getObjectID(), systemStateBean.getLabel(), locale);
		}
		//cache and possible lucene update in other cluster nodes
		LookupContainer.resetSystemStateMap(systemStateBean.getEntityflag());
		ClusterMarkChangesBL.markDirtySystemStatesListEntryInCluster(systemStateBean.getEntityflag(), objectID);
		return objectID;
	}
	
	/**
	 * Whether the option has dependent data
	 * @param objectID
	 * @return
	 */
	static boolean hasDependentData(Integer objectID) {
		return systemStateDAO.hasDependentData(objectID);
	}
	
	/** 
	 * This method replaces all occurrences of severity value oldOID with
	 * state value newOID and then deletes the severity
	 * @param oldOID
	 * @param newOID
	 */
	static void replaceAndDelete(Integer oldOID, Integer newOID) {
		if (newOID!=null) {
			systemStateDAO.replace(oldOID, newOID);
		}
		delete(oldOID);
	}
	
	/**
	 * Deletes the severity
	 * @param objectID
	 */
	static void delete(Integer objectID) {
		if (objectID!=null) {
			TSystemStateBean systemStateBean = systemStateDAO.loadByPrimaryKey(objectID);
			Integer iconKey = null;
			if (systemStateBean!=null) {
				iconKey = systemStateBean.getIconKey();
			}
			systemStateDAO.delete(objectID);
			if (iconKey!=null) {
				BlobBL.delete(iconKey);
			}
			//remove localized resource
			LocalizeBL.removeLocalizedResources(new TSystemStateBean().getKeyPrefix() + systemStateBean.getEntityflag() + ".", objectID);
			LookupContainer.resetSystemStateMap(systemStateBean.getEntityflag());
			ClusterMarkChangesBL.markDirtySystemStatesListEntryInCluster(systemStateBean.getEntityflag(), objectID);
		}
	}
}
