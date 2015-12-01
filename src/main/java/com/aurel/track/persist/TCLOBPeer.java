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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.dao.ClobDAO;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TCLOBPeer
	extends com.aurel.track.persist.BaseTCLOBPeer
	implements ClobDAO
{

	private static final Logger LOGGER = LogManager.getLogger(TCLOBPeer.class);

	public static final long serialVersionUID = 400L;
	
	/**
	 * Gets a CLOBBean by primary key 
	 * @param objectID
	 * @return
	 */
	@Override
	public TCLOBBean loadByPrimaryKey(Integer objectID) {
		TCLOB tClob = null;
		try {
			tClob = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of a clob by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tClob!=null) {
			return tClob.getBean();
		}
		return null;
	}

	/**
	 * Loads CLOBBeans by primary keys
	 * @param objectIDs
	 * @return
	 */
	@Override
	public List<TCLOBBean> loadByPrimaryKeys(List<Integer> objectIDs) {
		List<TCLOBBean> clobBeansList = new ArrayList<TCLOBBean>();
		if (objectIDs==null || objectIDs.isEmpty()) {
			return clobBeansList;
		}
		List<int[]> clobIDChunksList = GeneralUtils.getListOfChunks(objectIDs);
		if (clobIDChunksList==null) {
			return clobBeansList;
		}
		Iterator<int[]> iterator = clobIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] clobIDChunk = iterator.next();
			Criteria criteria = new Criteria();
			criteria.addIn(OBJECTID, clobIDChunk);
			try {
				clobBeansList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the clobs for " + clobIDChunk.length +
						" failed with " + e.getMessage(), e);
			}
		}
		return clobBeansList;
	}
	
	/**
	 * Load all clobs
	 * @return
	 */
	@Override
	public List<TCLOBBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch(Exception e) {
			LOGGER.error("Loading all clobs failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves a CLOBBean
	 * @param clobBean
	 * @return
	 */
	@Override
	public Integer save(TCLOBBean clobBean) {
		TCLOB tClob;	
		try {
			tClob = BaseTCLOB.createTCLOB(clobBean);
			tClob.save();
			return tClob.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a clob failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Saves a CLOBBean
	 * @param clobBean
	 * @return
	 */
	public Integer copy(Integer objectID) {
		try {
			TCLOBBean clobBean = loadByPrimaryKey(objectID);
			if (clobBean!=null) {
				TCLOB tClobSrc = BaseTCLOB.createTCLOB(clobBean);
				TCLOB tClobCopy = tClobSrc.copy(false);
				if (tClobCopy!=null) {
					tClobCopy.save();
					return tClobCopy.getObjectID();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Copying of a clob failed with " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Deletes an CLOBBean by primary key
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting a clob for key " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes more CLOBBeans by primary keys
	 * @param objectID
	 */
	public void delete(Object[] objectIDs) {
		Criteria crit = new Criteria();
		if (objectIDs==null || objectIDs.length==0) {
			return;
		}
		crit.addIn(OBJECTID, objectIDs);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting a clob for key " + objectIDs + " failed with: " + e);
		}
	}
	
	private List<TCLOBBean> convertTorqueListToBeanList(List<TCLOB> torqueList) {
		List<TCLOBBean> beanList = new LinkedList<TCLOBBean>();
		if (torqueList!=null) {
			for (TCLOB tCLOB : torqueList) {
				beanList.add(tCLOB.getBean());
			}
		}
		return beanList;
	}
}
