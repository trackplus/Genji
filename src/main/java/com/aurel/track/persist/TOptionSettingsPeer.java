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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.dao.OptionSettingsDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TOptionSettingsPeer
    extends com.aurel.track.persist.BaseTOptionSettingsPeer implements OptionSettingsDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TOptionSettingsPeer.class);		
    
	public static final long serialVersionUID = 400L;
	
	/**
     * Loads an option settings by primary key
     * @param objectID
     * @return
     */
    @Override
    public TOptionSettingsBean loadByPrimaryKey(Integer objectID)  {    	
    	TOptionSettings tOptionSettings = null;
    	try {
    		tOptionSettings = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of an option settings by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (tOptionSettings!=null) {
			return tOptionSettings.getBean();
		}
		return null;
	}
    
    /**
	 * Saves an optionSettingsBean in the TOptionSettings table
	 * @param optionSettingsBean
	 * @return
	 */
	@Override
	public Integer save(TOptionSettingsBean optionSettingsBean) {
		Criteria criteria = new Criteria();
		if (optionSettingsBean!=null && optionSettingsBean.getObjectID()==null 
				&& optionSettingsBean.getConfig()!=null) {			
			criteria.add(CONFIG, optionSettingsBean.getConfig());
			if (optionSettingsBean.getParameterCode()==null) {
				criteria.add(PARAMETERCODE, (Object)null, Criteria.ISNULL);
			} else {
				criteria.add(PARAMETERCODE, optionSettingsBean.getParameterCode());
			}
			try {
				doDelete(criteria);
			} catch (TorqueException e) {
				LOGGER.error("Deleting the old configuration objects failed with");
			}
		}
		TOptionSettings tOptionSettings;
		try {
			tOptionSettings = BaseTOptionSettings.createTOptionSettings(optionSettingsBean);
			tOptionSettings.save();
			return tOptionSettings.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of an option settings failed with " + e.getMessage());
			return null;
		}
	}	  
    
	/**
	 * Deletes by configID
	 * @param configID
	 */
	@Override
	public void delete(Integer configID) {        
    	Criteria crit = new Criteria();
        crit.add(CONFIG, configID);
        try {
            doDelete(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Deleting the option settings by configID " + configID + " failed with: " + e);
        }  
    }
        
	
    /**
	 * Gets a optionSettingsBean from the TOptionSettings table
	 * Should be used by simple list
	 * @param configID
	 * @return
	 */
    /*public TOptionSettingsBean loadByConfig(Integer configID)
	{
		List optionSettingsList =  null;
		Criteria crit = new Criteria();
        crit.add(CONFIG, configID);
        try {
        	optionSettingsList = doSelect(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Loading the option settings by config " + configID + " failed with: " + e);
        } 
        if (optionSettingsList == null || optionSettingsList.isEmpty())
		{
			LOGGER.error("No option settings found for the config " + configID);
			return null; 
		}
		if (optionSettingsList.size()>1)
		{
			LOGGER.error("More than one option settings found for the config " + configID);
		}
		return ((TOptionSettings)optionSettingsList.get(0)).getBean();		
	}*/
		
	/**
	 * Gets a optionSettingsBean from the TOptionSettings table
	 * Should be used if it is part of a composit field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
    @Override
    public TOptionSettingsBean loadByConfigAndParameter(Integer configID, Integer parameterCode) {	
		List optionSettingsList =  null;
		Criteria crit = new Criteria();
	    crit.add(CONFIG, configID);
	    if (parameterCode == null) {
	    	crit.add(PARAMETERCODE, (Object)null, Criteria.ISNULL);
	    } else {
	    	crit.add(PARAMETERCODE, parameterCode);
	    }
	    try {
	    	optionSettingsList = doSelect(crit);
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the option settings by config " + configID + 
	    			" and parameterCode " +  parameterCode + " failed with: " + e);
	    } 
	    if (optionSettingsList == null || optionSettingsList.isEmpty()) {
			/*LOGGER.error("No option settings found for the config " + configID +
					" and parameterCode " +  parameterCode);*/
			return null; 
		}
		if (optionSettingsList.size()>1) {
			LOGGER.warn("More than one option settings found for the config " + configID+ 
					" and parameterCode " +  parameterCode);
			//should never occure but if does occur return the last entered one
			return ((TOptionSettings)optionSettingsList.get(optionSettingsList.size()-1)).getBean();
		}
		return ((TOptionSettings)optionSettingsList.get(0)).getBean();
	}
    
    /**
	 * Loads all option settings for a list of configIDs
	 * @param configIDs
	 * @return
	 */
    @Override
    public List<TOptionSettingsBean> loadByConfigList(List<Integer> configIDs) {
    	//Map optionSettingsMap = new HashMap();
    	if (configIDs==null || configIDs.isEmpty()) {
    		return new ArrayList<TOptionSettingsBean>();
    	}
    	List optionSettingsList =  null;    	
    	//TOptionSettingsBean optionSettingsBean;
    	Criteria crit = new Criteria();
	    crit.addIn(CONFIG, configIDs);
	    try {
	    	optionSettingsList = doSelect(crit);
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the option settings by config list " + configIDs + " failed with: " + e);
	    }
	    return convertTorqueListToBeanList(optionSettingsList);
	    /*if (optionSettingsList!=null) {
	    	Iterator itrOptionSettings = optionSettingsList.iterator();
	    	while (itrOptionSettings.hasNext()) {
	    		optionSettingsBean = ((TOptionSettings) itrOptionSettings.next()).getBean();	    		
    			optionSettingsMap.put(optionSettingsBean.getConfig() +
    					TWorkItemBean.LINKCHAR + optionSettingsBean.getParameterCode(), 
    					optionSettingsBean);
			}
	    }
	    return optionSettingsMap;*/
    }
    
    /**
	 * Converts a list of TOptionSettings torque objects to a list of TOptionSettingsBean objects 
	 * @param torqueList
	 * @return
	 */
	private List<TOptionSettingsBean> convertTorqueListToBeanList(List<TOptionSettings> torqueList) {		
		List<TOptionSettingsBean> beanList = new LinkedList<TOptionSettingsBean>();		
		if (torqueList!=null) {
			for (TOptionSettings tOptionSettings : torqueList) {
				beanList.add(tOptionSettings.getBean());
			}			
		}
		return beanList;
	}
	
	
	/**A
	 * Loads all option settings for the specified ListID
	 * @param listObjectId
	 * @return
	 */
	@Override
	public List<TOptionSettingsBean> loadByListID(Integer listObjectID) {		
		Criteria crit = new Criteria();
        crit.add(LIST, listObjectID);
        try {
        	return convertTorqueListToBeanList(doSelect(crit));
        } catch (TorqueException e) {
        	LOGGER.error("Loading the textbox settings by config " + listObjectID + " failed with: " + e);
        	return null;
        }         
	}
	
	/**A
	 * Loads all option settings
	 * @return
	 */
	@Override
	public List<TOptionSettingsBean> loadAll() {		
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all custom options failed with " + e.getMessage());
			return null;
		}		
	}
	
	
}
