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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TMotdBean;
import com.aurel.track.dao.MotdDAO;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Wed Apr 19 10:51:33 CEST 2006]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TMotdPeer
    extends com.aurel.track.persist.BaseTMotdPeer
    implements MotdDAO {
    private static final long serialVersionUID = 7088751201707397436L;
    
    private static final Logger LOGGER = LogManager.getLogger(TMotdPeer.class);

    /**
     * Loads a motd bean by locale   
     * @param locale
     * @return
     */
    public List<TMotdBean> loadByLocale(String locale) {
    	Criteria crit = new Criteria();    	
    	if (locale==null) {
        	crit.add(THELOCALE, (Object)null, Criteria.ISNULL);
        } else {
        	crit.add(THELOCALE, locale);
        }
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading motd by " + locale +  " failed with " + e.getMessage(), e);
			return null;
		}
    }
    
    /**
	 * Saves a motd bean 
	 * @param motdBean
	 * @return
	 */
	public Integer save(TMotdBean motdBean) {
		TMotd tMotd;		
		try {
			tMotd = BaseTMotd.createTMotd(motdBean);
			tMotd.save();
			return tMotd.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of motd failed with " + e.getMessage(), e);
			return null;
		}	
	}
	
	/**
	 * Deletes a motd bean
	 * @param objectID
	 * @return
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
        crit.add(OBJECTID, objectID);        
        try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the motd " + objectID +  " failed with " + e.getMessage(), e);
		}
	}
	
    /**
     * This method loads the message of the day text from the database
     * for this specific locale. If a message with language plus country
     * is not found, a message for the language only is fetched. If this
     * is not contained in the database, null is returned.
     * @param locale the locale for which the message shall be retrieved.
     * @return
     */
    /*public static TMotd load(Locale locale)  {
        
        TMotd motd = null;
        
        if (locale == null) {      
            return motd;
        }
        
        String language = locale.getLanguage();
        
        Criteria crit = new Criteria();
        
        // First try the complete language plus country code
        crit.add(BaseTMotdPeer.THELOCALE, (Object) locale.toString(), Criteria.EQUAL );

        List list = null;
        try {
            list = BaseTMotdPeer.doSelect(crit);
            
            if (list == null || list.size() < 1) {
                crit.clear();
                // Tough luck, now let's try language only
                crit.add(BaseTMotdPeer.THELOCALE, (Object) language, Criteria.EQUAL );
                list = BaseTMotdPeer.doSelect(crit);
            }
            if (list != null && list.size() > 0) {
                // Great, we got a message!
                motd = (TMotd) list.get(0);
            }
        }
        catch (Exception e) {
            log.error("Exception when loading message of the day: " 
                    + e.getMessage(), e);
        }
        
        return motd;
    }*/
    
    private List<TMotdBean> convertTorqueListToBeanList(List<TMotd> torqueList) {
		List<TMotdBean> beanList = new LinkedList<TMotdBean>();
    	if (torqueList!=null) {
	    	for (TMotd tMotd : torqueList) {
	    		beanList.add(tMotd.getBean());
			}
    	}		
		return beanList;
	}
}
