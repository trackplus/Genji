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

import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.dao.TextBoxSettingsDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TTextBoxSettingsPeer
    extends com.aurel.track.persist.BaseTTextBoxSettingsPeer implements TextBoxSettingsDAO 
{
	private static final Logger LOGGER = LogManager.getLogger(TTextBoxSettingsPeer.class);		
	public static final long serialVersionUID = 400L;
	
	/**
     * Loads a textbox settings by primary key
     * @param objectID
     * @return
     */
    public TTextBoxSettingsBean loadByPrimaryKey(Integer objectID)  {    	
    	TTextBoxSettings tTextBoxSettings = null;
    	try {
    		tTextBoxSettings = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.warn("Loading of a text box settings by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (tTextBoxSettings!=null) {
			return tTextBoxSettings.getBean();
		}
		return null;
	}
    
    /**
	 * Saves a textBoxSettingsBean in the TTextBoxSettings table
	 * @param textBoxSettingsBean
	 * @return
	 */
	public Integer save(TTextBoxSettingsBean textBoxSettingsBean)
	{
		TTextBoxSettings tTextBoxSettings;		
		try {
			tTextBoxSettings = BaseTTextBoxSettings.createTTextBoxSettings(textBoxSettingsBean);
			tTextBoxSettings.save();
			return tTextBoxSettings.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a textbox settings failed with " + e.getMessage(), e);
			return null;
		}
	}
	
    /**
	 * Inserts a new record in the TTextBoxSettings table
	 * @param textBoxSettingsBean
	 * @return
	 */
	/*public Integer insert(TTextBoxSettingsBean textBoxSettingsBean)
	{
		TTextBoxSettings tTextBoxSettings;		
		try {
			tTextBoxSettings = TTextBoxSettings.createTTextBoxSettings(textBoxSettingsBean);
			tTextBoxSettings.save();
			return tTextBoxSettings.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Inserting of a textbox settings failed with " + e.getMessage(), e);
			return null;
		}
	}*/
	
	
	/**
	 * Updates a record in TTextBoxSettings table 
	 * @param textBoxSettingsBean should contain a valid fieldID
	 */
	/*public void update(TTextBoxSettingsBean textBoxSettingsBean)
	{
		TTextBoxSettings tTextBoxSettings;		
		try {
			tTextBoxSettings = TTextBoxSettings.createTTextBoxSettings(textBoxSettingsBean);
			tTextBoxSettings.save();			
		} catch (Exception e) {
			LOGGER.error("Updating of a textbox settings failed with " + e.getMessage(), e);
			
		}
	}*/
	
	/**
	 * Deletes by configID
	 * @param configID
	 */
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
	 * Gets a textBoxSettingsBean from the TTextBoxSettings table
	 * Should be used by simple list
	 * @param configID
	 * @return
	 */
    /*public TTextBoxSettingsBean loadByConfig(Integer configID)
	{
		List textBoxSettingsList =  null;
		Criteria crit = new Criteria();
        crit.add(CONFIG, configID);
        try {
        	textBoxSettingsList = doSelect(crit);
        } catch (TorqueException e) {
        	LOGGER.error("Loading the textbox settings by config " + configID + " failed with: " + e);
        } 
        if (textBoxSettingsList == null || textBoxSettingsList.isEmpty())
		{
			LOGGER.error("No textbox settings found for the config " + configID);
			return null; 
		}
		if (textBoxSettingsList.size()>1)
		{
			LOGGER.error("More than one textbox settings found for the config " + configID);
		}
		return ((TTextBoxSettings)textBoxSettingsList.get(0)).getBean();
	}*/
    /**
	 * Gets a textBoxSettingsBean from the TTextBoxSettings table
	 * Should be used by simple list
	 * @param configID
	 * @return
	 */
    public List<TTextBoxSettingsBean> loadByConfig(Integer configID) {		
		Criteria crit = new Criteria();
        crit.add(CONFIG, configID);
        try {
        	return convertTorqueListToBeanList(doSelect(crit));
        } catch (TorqueException e) {
        	LOGGER.error("Loading the textbox settings by config " + configID + " failed with: " + e);
        	return null;
        }        
	}

    
		
	/**
	 * Gets a textBoxSettingsBean from the TTextBoxSettings table
	 * Should be used if it is part of a composit field
	 * @param configID
	 * @param parameterCode
	 * @return
	 */
    public TTextBoxSettingsBean loadByConfigAndParameter(Integer configID, Integer parameterCode)
	{	
		List textBoxSettingsList =  null;
		Criteria crit = new Criteria();
	    crit.add(CONFIG, configID);
	    if (parameterCode == null)
	    {
	    	crit.add(PARAMETERCODE, (Object)null, Criteria.ISNULL);
	    }
	    else
	    {
	    	crit.add(PARAMETERCODE, parameterCode);
	    }
	    try {
	    	textBoxSettingsList = doSelect(crit);
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the textbox settings by config " + configID + 
	    			" and parameterCode " +  parameterCode + " failed with: " + e);
	    } 
	    if (textBoxSettingsList == null || textBoxSettingsList.isEmpty())
		{
			/*LOGGER.error("No textbox settings found for the config " + configID +
					" and parameterCode " +  parameterCode);*/
			return null; 
		}
		if (textBoxSettingsList.size()>1)
		{
			LOGGER.warn("More than one textbox settings found for the config " + configID+ 
					" and parameterCode " +  parameterCode);
			//should never occure but if does occur return the last entered one
			return ((TTextBoxSettings)textBoxSettingsList.get(textBoxSettingsList.size()-1)).getBean();
		}
		return ((TTextBoxSettings)textBoxSettingsList.get(0)).getBean();
	}
    
    /**
	 * Loads all textbox settings for a list of configIDs
	 * @param configIDs
	 * @return
	 */
    public List<TTextBoxSettingsBean> loadByConfigList(List configIDs) {
    	//Map textBoxSettingsMap = new HashMap();
    	if (configIDs==null || configIDs.isEmpty()) {
    		return new ArrayList<TTextBoxSettingsBean>();
    	}
    	List textBoxSettingsList =  null;    	
    	//TTextBoxSettingsBean textBoxSettingsBean;
    	Criteria crit = new Criteria();
	    crit.addIn(CONFIG, configIDs);
	    try {
	    	textBoxSettingsList = doSelect(crit);
	    } catch (TorqueException e) {
	    	LOGGER.error("Loading the textbox settings by config list " + configIDs + " failed with: " + e);
	    }
	    return convertTorqueListToBeanList(textBoxSettingsList);
	    /*if (textBoxSettingsList!=null) {
	    	Iterator itrTextBoxSettings = textBoxSettingsList.iterator();
	    	while (itrTextBoxSettings.hasNext()) {
	    		textBoxSettingsBean = ((TTextBoxSettings) itrTextBoxSettings.next()).getBean();	    		
    			textBoxSettingsMap.put(textBoxSettingsBean.getConfig() + 
    					TWorkItemBean.LINKCHAR + textBoxSettingsBean.getParameterCode(), 
    					textBoxSettingsBean);

			}
	    }
	    return textBoxSettingsMap;*/
    }
    
    /**
     * Loads all TextBoxSettings from table
     * @return
     */
    public List<TTextBoxSettingsBean> loadAll() {    	
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all text box settings failed with " + e.getMessage(), e);
			return null;
		}		
	}

    
    /**
	 * Converts a list of TTextBoxSettings torque objects to a list of TTextBoxSettingsBean objects 
	 * @param torqueList
	 * @return
	 */
	private List convertTorqueListToBeanList(List torqueList) {		
		List beanList = new ArrayList();
		TTextBoxSettings tTextBoxSettings;
		if (torqueList!=null) {
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				tTextBoxSettings = (TTextBoxSettings)itrTorqueList.next();
				beanList.add(tTextBoxSettings.getBean());
			}
		}
		return beanList;
	}
}
