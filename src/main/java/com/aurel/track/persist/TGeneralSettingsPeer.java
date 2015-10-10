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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.dao.GeneralSettingsDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TGeneralSettingsPeer
    extends com.aurel.track.persist.BaseTGeneralSettingsPeer implements GeneralSettingsDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TGeneralSettingsPeer.class);		
    
	public static final long serialVersionUID = 400L;
	
	/**
     * Loads a general settings by primary key
     * @param roleID
     * @return
     */
    public TGeneralSettingsBean loadByPrimaryKey(Integer objectID)  {    	
    	TGeneralSettings tGeneralSettings = null;
    	try {
    		tGeneralSettings = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of a general settings by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	}
    	if (tGeneralSettings!=null) {
    		return tGeneralSettings.getBean();
    	}
    	return null;
	}
    
    /**
	 * Saves a generalSettingsBean in the TGeneralSettings table
	 * @param generalSettingsBean
	 * @return
	 */
	public Integer save(TGeneralSettingsBean generalSettingsBean)
	{
		TGeneralSettings tGeneralSettings;		
		try {
			tGeneralSettings = BaseTGeneralSettings.createTGeneralSettings(generalSettingsBean);
			tGeneralSettings.save();
			return tGeneralSettings.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a general settings failed with " + e.getMessage(), e);
			return null;
		}
	}
	    
    
	/**
	 * Deletes the records from the TGeneralSettings table by a config
	 * @param configID
	 */
    public void deleteByConfig(Integer configID) {        
    	Criteria crit = new Criteria();
        crit.add(CONFIG, configID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the general settings by config " + configID + " failed with: " + e);
        }  
    }
    
    /**
	 * Gets the generalSettingsBeans from the 
	 * TGeneralSettings table for a configID 
	 * @param configID
	 * @return
	 */
    public List<TGeneralSettingsBean> loadByConfig(Integer configID) {
		List generalSettingsList =  null;
		Criteria crit = new Criteria();
        crit.add(CONFIG, configID);
        try {
        	generalSettingsList = doSelect(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Loading the general settings by config " + configID + " failed with: " + e);
        } 
		return convertTorqueListToBeanList(generalSettingsList);
	}
		
	/**
	 * Gets a generalSettingsBean from the TGeneralSettings table
	 * Should be used if it is part of a composit field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
    public TGeneralSettingsBean loadSingleByConfigAndParameter(Integer configID, Integer parameterCode) {	
		List generalSettingsList =  null;
		Criteria crit = new Criteria();
	    crit.add(CONFIG, configID);
	    if (parameterCode == null) {
	    	crit.add(PARAMETERCODE, (Object)null, Criteria.ISNULL);
	    } else {
	    	crit.add(PARAMETERCODE, parameterCode);
	    }
	    try {
	    	generalSettingsList = doSelect(crit);
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the general settings by config " + configID + 
	    			" and parameterCode " +  parameterCode + " failed with: " + e);
	    } 
	    if (generalSettingsList == null || generalSettingsList.isEmpty()) {
			/*LOGGER.error("No general settings found for the config " + configID +
					" and parameterCode " +  parameterCode);*/
			return null; 
		}
		if (generalSettingsList.size()>1) {
			LOGGER.warn("More than one general settings found for the config " + configID+ 
					" and parameterCode " +  parameterCode);
			//should never occure but if does occur return the last entered one
			return ((TGeneralSettings)generalSettingsList.get(generalSettingsList.size()-1)).getBean();
		}
		return ((TGeneralSettings)generalSettingsList.get(0)).getBean();
	}
    
    /**
	 * Gets a generalSettingsBean list from the TGeneralSettings table for the same parameterCode
	 * Used by multiple select lists 
	 * Should be used if it is part of a composit field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
    public List<TGeneralSettingsBean> loadListByConfigAndParameter(Integer configID, Integer parameterCode) {	
		List generalSettingsList =  null;
		Criteria crit = new Criteria();
	    crit.add(CONFIG, configID);
	    if (parameterCode == null) {
	    	crit.add(PARAMETERCODE, (Object)null, Criteria.ISNULL);
	    } else {
	    	crit.add(PARAMETERCODE, parameterCode);
	    }
	    try {
	    	generalSettingsList = doSelect(crit);
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the general settings by config " + configID + 
	    			" and parameterCode " +  parameterCode + " failed with: " + e);
	    }
	    return convertTorqueListToBeanList(generalSettingsList);	    
	}
    
    /**
	 * Loads all general settings for a list of configIDs
	 * @param configIDs
	 * @return
	 */
    public List<TGeneralSettingsBean> loadByConfigList(List<Integer> configIDs) { 
    	if (configIDs==null || configIDs.isEmpty()) {
    		return new ArrayList<TGeneralSettingsBean>();
    	}
    	Criteria crit = new Criteria();
	    crit.addIn(CONFIG, configIDs);
	    try {
	    	return convertTorqueListToBeanList(doSelect(crit));
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the general settings by config list " + configIDs + " failed with: " + e);
	    	return null;
	    }	    
    }
    
    
    /**
	 * Loads all genaral settings for a list of configIDs
	 * - Key: a combination of configID and parameterCode
	 * - Value TGeneralSettingsBean
	 * @param configIDs
	 * @return
	 */
	public List<TGeneralSettingsBean> loadByConfigListAndParametercode(List<Integer> configIDs, Integer parameterCode) {
		if (configIDs==null || configIDs.isEmpty()) {
    		return new ArrayList<TGeneralSettingsBean>();
    	}
    	Criteria crit = new Criteria();
	    crit.addIn(CONFIG, configIDs);
	    crit.add(PARAMETERCODE, parameterCode);
	    try {
	    	return convertTorqueListToBeanList(doSelect(crit));
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the general settings by config list " + configIDs +
	    			" and  parameterCode " + parameterCode + " failed with: " + e);
	    	return null;
	    }
	}
    
	  /**
	    * Loads all generalsettings	from table
		* @return
		*/
	public List<TGeneralSettingsBean> loadAll() {		
		Criteria criteria = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading all field configs failed with " + e.getMessage(), e);
			return null;
		}		
	}

		
    /**
	 * Converts a list of TGeneralSettings torque objects to a list of TGeneralSettingsBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List convertTorqueListToBeanList(List torqueList) {		
		List beanList = new ArrayList();
		TGeneralSettings generalSettings;
		if (torqueList!=null) {
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				generalSettings = (TGeneralSettings)itrTorqueList.next();
				beanList.add(generalSettings.getBean());
			}
		}
		return beanList;
	}
}
