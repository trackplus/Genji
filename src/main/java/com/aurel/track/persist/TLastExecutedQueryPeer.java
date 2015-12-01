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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TLastExecutedQueryBean;
import com.aurel.track.dao.LastExecutedQueryDAO;

/**
 * store the last n executed queries with all data needed to execute them again
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TLastExecutedQueryPeer extends com.aurel.track.persist.BaseTLastExecutedQueryPeer implements LastExecutedQueryDAO{

	private static final Logger LOGGER = LogManager.getLogger(TLastExecutedQueryPeer.class);

	public static final long serialVersionUID = 400L;
	
	@Override
	public TLastExecutedQueryBean loadByPrimaryKey(Integer objectID) {
		TLastExecutedQueryBean lastExecutedQueryBean = null;
		try{
			TLastExecutedQuery tobject = retrieveByPK(objectID);
			if (tobject!=null){
				lastExecutedQueryBean=tobject.getBean();
			}
		}
		catch(Exception e){
			LOGGER.info("Loading of a LastExecutedQuery by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return lastExecutedQueryBean;
	}

	/**
	 * Loads the last executed filters by a person
	 */
	@Override
	public List<TLastExecutedQueryBean> loadByPerson(Integer personID) {
		Criteria crit = new Criteria();
		crit.add(PERSON,personID);
		crit.addDescendingOrderByColumn(LASTEXECUTEDTIME);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading by person failed with " + e.getMessage());
			return  null;
		}
	}
	/**
	 * @param personID
	 * @return
	 */
	@Override
	public TLastExecutedQueryBean loadLastByPerson(Integer personID){
		TLastExecutedQueryBean lastExecutedQueryBean = null;
		Criteria crit = new Criteria();
		crit.add(PERSON,personID);
		crit.addDescendingOrderByColumn(LASTEXECUTEDTIME);
		try {
			List torqueList = doSelect(crit);
			if(torqueList!=null && !torqueList.isEmpty()){
				lastExecutedQueryBean=((TLastExecutedQuery)torqueList.get(0)).getBean();
			}
		} catch (TorqueException e) {
			LOGGER.error("Loading last by person failed with " + e.getMessage());
		}
		return lastExecutedQueryBean;
	}

	@Override
	public Integer save(TLastExecutedQueryBean lastExecutedQuery) {
		try {
			TLastExecutedQuery tobject = BaseTLastExecutedQuery.createTLastExecutedQuery(lastExecutedQuery);
			tobject.setLastExecutedTime(new Date());
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a LastExecutedQuery failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Deletes the last executed quieries by filterID and filterType
	 */
	@Override
	public void deleteByFilterIDAndFilterType(Integer filterID, Integer filterType) {
		deleteByFilterIDAndFilterType(filterID, filterType, null);
	}
	
	/**
	 * Deletes the last executed quieries by filterID and filterType
	 */
	@Override
	public void deleteByFilterIDAndFilterType(Integer filterID, Integer filterType, Connection con) {
		Criteria crit = new Criteria();
		crit.add(QUERYKEY, filterID);
		crit.add(QUERYTYPE, filterType);
		doDelete(crit, con);
	}
	
	/**
	 * Deletes the TQueryRepositoryBean satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit, Connection con) {
		List<TLastExecutedQuery> list = null;
		try {
			list = doSelect(crit, con);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TNotifyTriggers to be deleted failed with " + e.getMessage());
		}			
        if (list==null || list.isEmpty()) {
            return;
        }
        try {
			BaseTLastExecutedQueryPeer.doDelete(crit, con);
		} catch (TorqueException e) {
			LOGGER.warn("Deleting the last executed query failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
        for (TLastExecutedQuery lastExecutedQuery : list) {
        	Integer queryClobID = lastExecutedQuery.getQueryClob();
        	if (queryClobID!=null) {
	        	try {
					BaseTCLOBPeer.doDelete(SimpleKey.keyFor(queryClobID), con);
				} catch (TorqueException e) {
					LOGGER.warn("Deleting the last executed query failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
        	}
		}
	}
	
	@Override
	public void delete(Integer objectID) {
		Connection con = null;
		try {
			con = Transaction.begin(DATABASE_NAME);
			delete(objectID,con);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Deleting a DashboardField for key " + objectID + " failed with: " + e);
		}
	}
	
	public static void delete(Integer objectID,Connection con) {
		try {
			TLastExecutedQuery lastExecutedQuery=retrieveByPK(objectID,con);
			doDelete(SimpleKey.keyFor(objectID),con);
			if(lastExecutedQuery!=null&&lastExecutedQuery.getQueryClob()!=null){
				BaseTCLOBPeer.doDelete(SimpleKey.keyFor(lastExecutedQuery.getQueryClob()),con);
			}
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Deleting a DashboardField for key " + objectID + " failed with: " + e);
		}
	}

	/**
	 * Converts the last executed filters to beans
	 * @param torqueList
	 * @return
	 */
	private List<TLastExecutedQueryBean> convertTorqueListToBeanList(List<TLastExecutedQuery> torqueList) {
		List<TLastExecutedQueryBean> beanList = new ArrayList<TLastExecutedQueryBean>();
		if (torqueList!=null) {
			for (TLastExecutedQuery lastExecutedQuery : torqueList) {
				beanList.add(lastExecutedQuery.getBean());
			}
		}
		return beanList;
	}
}
