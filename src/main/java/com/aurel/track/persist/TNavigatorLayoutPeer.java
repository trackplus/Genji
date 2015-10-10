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

import com.aurel.track.beans.TNavigatorLayoutBean;
import com.aurel.track.dao.NavigatorLayoutDAO;

/**
 * layout for item navigator
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TNavigatorLayoutPeer
	extends com.aurel.track.persist.BaseTNavigatorLayoutPeer implements NavigatorLayoutDAO{

	private static final long serialVersionUID = 500L;

	private static final Logger LOGGER = LogManager.getLogger(TNavigatorLayoutPeer.class);

	private static Class[] deletePeerClasses = {
	    	TNavigatorColumnPeer.class,
	    	TNavigatorGroupingSortingPeer.class,
	    	TCardGroupingFieldPeer.class,
	    	//use the superclass doDelete() method!!!
	    	BaseTNavigatorLayoutPeer.class
	    };
	    
	    private static String[] deleteFields = {  
	    	TNavigatorColumnPeer.NAVIGATORLAYOUT,
	    	TNavigatorGroupingSortingPeer.NAVIGATORLAYOUT,
	    	TCardGroupingFieldPeer.NAVIGATORLAYOUT,
	    	BaseTNavigatorLayoutPeer.OBJECTID  	
	    };
	    
	/**
	 * Load the default navigator layout
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadDefault() {
		return loadByContext(null, null, null);
	}
	
	/**
	 * Load navigator layout by user
	 * @param personID
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByPerson(Integer personID) {
		return loadByContext(personID, null, null);
	}
	
	/**
	 * Load navigator layout by user and filter
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByPersonAndFilter(Integer personID,Integer filterID,Integer filterType){
		return loadByContext(personID, filterID, filterType);
	}
	
	/**
	 * Load navigator layout filter and view
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByFilterWithView(Integer filterID, Integer filterType) {
		return loadByContext(null, filterID, filterType);
	}
	
	/**
	 * Loads the layout by context
	 * @param personID
	 * @param filterID
	 * @param filterType
	 * @return
	 */
	public List<TNavigatorLayoutBean> loadByContext(Integer personID, Integer filterID, Integer filterType){
		List<TNavigatorLayout> torqueList = null;
		Criteria crit = new Criteria();
		if (personID!=null) {
			crit.add(PERSON, personID);
		} else {
			crit.add(PERSON, personID, Criteria.ISNULL);
		}
		if (filterType==null){
			crit.add(FILTERTYPE,(Object)null,Criteria.ISNULL);
			crit.add(FILTERID,(Object)null,Criteria.ISNULL );
		} else {
			crit.add(FILTERTYPE, filterType);
			if (filterID==null) {
				crit.add(FILTERID,(Object)null,Criteria.ISNULL );
			} else {
				crit.add(FILTERID, filterID);
			}
		}
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading NavigatorLayout for key:"+personID+" failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(torqueList);
	}

	/**
	 *
	 * @param navigatorLayoutBean
	 * @return
	 */
	public Integer save(TNavigatorLayoutBean navigatorLayoutBean){
		try {
			TNavigatorLayout navigatorLayout = BaseTNavigatorLayout.createTNavigatorLayout(navigatorLayoutBean);
			navigatorLayout.save();
			return navigatorLayout.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a navigator layout failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	/**
	 * Deletes a navigator layout with dependencies
	 * @param layoutID
	 * @return
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * Deletes the TQueryRepositoryBean satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		List<TNavigatorLayout> list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TNotifyTriggers to be deleted failed with " + e.getMessage(), e);
		}			
        if (list == null || list.size() < 1) {
            return;
        }
        for (TNavigatorLayout navigatorLayout : list) {
        	ReflectionHelper.delete(deletePeerClasses, deleteFields, navigatorLayout.getObjectID());		
			
		}
	}
	
	private List<TNavigatorLayoutBean> convertTorqueListToBeanList(List<TNavigatorLayout> torqueList) {
		List<TNavigatorLayoutBean> beanList = new ArrayList<TNavigatorLayoutBean>();
		if (torqueList!=null){
			Iterator<TNavigatorLayout> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
}
