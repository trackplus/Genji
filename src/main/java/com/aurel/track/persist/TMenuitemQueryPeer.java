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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TMenuitemQueryBean;
import com.aurel.track.dao.MenuitemQueryDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.util.GeneralUtils;

/**
 * Describes which queries are included in the person's menu
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TMenuitemQueryPeer
	extends com.aurel.track.persist.BaseTMenuitemQueryPeer implements MenuitemQueryDAO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(TMenuitemQueryPeer.class);
	
	
	/**
	 * Loads the menuitemQueryBeans by person
	 * @param personID
	 * @param queryIDs
	 * @return
	 */
	@Override
	public List<TMenuitemQueryBean> loadByPersonAndQueries(Integer personID, List<Integer> queryIDs) {
		if (personID==null || queryIDs==null || queryIDs.isEmpty()) {
			return new LinkedList<TMenuitemQueryBean>();
		}
		List<TMenuitemQueryBean> menuItemQueryList = new LinkedList<TMenuitemQueryBean>();
		List<int[]> filterIDChunksList = GeneralUtils.getListOfChunks(queryIDs);
		for (int[] filterIDChunk : filterIDChunksList) {
			Criteria criteria = new Criteria();
			criteria.add(PERSON, personID);
			criteria.addIn(QUERYKEY, filterIDChunk);
			criteria.add(INCLUDEINMENU, BooleanFields.TRUE_VALUE);
			try {
				menuItemQueryList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Getting the menuitemQueryBean by personID " + personID + " and queryIDs failed with: " + e);
			}
		}
		return menuItemQueryList;
	}
	
	/**
	 * Loads the menuitemQueryBeans by persons
	 * @param personIDs
	 * @return
	 */
	@Override
	public List<TMenuitemQueryBean> loadByPersons(List<Integer> personIDs) {
		if (personIDs==null || personIDs.isEmpty()) {
			return new LinkedList<TMenuitemQueryBean>();
		}
		List<TMenuitemQueryBean> menuItemQueryList = new LinkedList<TMenuitemQueryBean>();
		List<int[]> personIDChunksList = GeneralUtils.getListOfChunks(personIDs);
		for (int[] personIDChunk : personIDChunksList) {
			Criteria criteria = new Criteria();
			criteria.addIn(PERSON, personIDChunk);
			criteria.add(INCLUDEINMENU, BooleanFields.TRUE_VALUE);
			try {
				menuItemQueryList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Getting the menuitemQueryBean for personIDs " + personIDs + " failed with: " + e);
			}
		}
		return menuItemQueryList;
	}

	/**
	 * Loads the menuitemQueryBeans by persons and queryIDs
	 * @param personIDs
	 * @param queryIDs
	 * @return
	 */
	@Override
	public List<TMenuitemQueryBean> loadByPersonsAndQueries(List<Integer> personIDs, List<Integer> queryIDs) {
		if (personIDs==null || personIDs.isEmpty() || queryIDs==null || queryIDs.isEmpty()) {
			return new LinkedList<TMenuitemQueryBean>();
		}
		List<TMenuitemQueryBean> menuItemQueryList = new LinkedList<TMenuitemQueryBean>();
		List<int[]> personIDChunksList = GeneralUtils.getListOfChunks(personIDs);
		for (int[] personIDChunk : personIDChunksList) {
			Criteria criteria = new Criteria();
			criteria.addIn(PERSON, personIDChunk);
			criteria.addIn(QUERYKEY, queryIDs);
			try {
				menuItemQueryList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Getting the menuitemQueryBean for personIDs " + personIDs + " failed with: " + e);
			}
		}
		return menuItemQueryList;
	}
	
	/**
	 * Loads the menuitemQueryBeans by person and query
	 * @param personID
	 * @param queryID
	 * @return
	 */
	@Override
	public TMenuitemQueryBean loadByPersonAndQuery(Integer personID, Integer queryID) {
		Criteria crit = new Criteria();
		crit.add(PERSON, personID);
		crit.add(QUERYKEY, queryID);
		try {
			List<TMenuitemQuery> torqueList=doSelect(crit);
			if(torqueList!=null && !torqueList.isEmpty()){
				return  torqueList.get(0).getBean();
			}
			return null;
		} catch (TorqueException e) {
			LOGGER.error("Getting the menuitemQueryBean by personID " + personID + " and queryID "  + queryID + " failed with: " + e);
			return null;
		}
	}
	
	/**
	 * Saves a menuitemQueryBean in the database
	 * @param menuitemQueryBean 
	 * @return
	 */
	@Override
	public Integer save(TMenuitemQueryBean menuitemQueryBean) {
		TMenuitemQuery tMenuitemQuery;
		try {
			tMenuitemQuery = BaseTMenuitemQuery.createTMenuitemQuery(menuitemQueryBean);
			tMenuitemQuery.save();
			return tMenuitemQuery.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a menuitemQueryBean failed with " + e.getMessage());
			return null;
		}
	}
		
	
	/**
	 * Deletes a menuitemQueryBean from the database
	 * @param personID
	 * @param queryID 
	 * @return
	 */
	@Override
	public void delete(Integer personID, Integer queryID) {
		Criteria crit = new Criteria();
		crit.add(PERSON, personID);
		crit.add(QUERYKEY, queryID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting a menuitemQueryBean by personID " + personID + " and queryID "  + queryID + " failed with: " + e);
		}
	}
	
	/**
	 * Converts a list of TQueryRepository torque objects to a list of TQueryRepositoryBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TMenuitemQueryBean> convertTorqueListToBeanList(List<TMenuitemQuery> torqueList) {
		List<TMenuitemQueryBean> beanList = new ArrayList<TMenuitemQueryBean>();
		if (torqueList!=null) {
			for (TMenuitemQuery tMenuitemQuery : torqueList) {
				beanList.add(tMenuitemQuery.getBean());
			}
		}
		return beanList;
	}
}
