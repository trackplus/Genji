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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TBLOBBean;
import com.aurel.track.dao.BlobDAO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TBLOBPeer
    extends com.aurel.track.persist.BaseTBLOBPeer
    implements BlobDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TBLOBPeer.class);

	public static final long serialVersionUID = 400L;
	
	/**
	 * Gets a TBLOBBean by primary key 
	 * @param objectID
	 * @return
	 */
	@Override
	public TBLOBBean loadByPrimaryKey(Integer objectID) {
		TBLOB tBlob = null;
    	try {
    		tBlob = retrieveByPK(objectID);    		
    	} catch(Exception e) {
    		LOGGER.debug("Loading of a blob by primary key " + objectID + " failed with " + e.getMessage());
    		LOGGER.debug(ExceptionUtils.getStackTrace(e));
    	} 
    	if (tBlob!=null) {
			return tBlob.getBean();
		}
		return null;
	}

	/**
	 * Saves a TBLOBBean
	 * @return
	 */
	@Override
	public Integer save(TBLOBBean blobBean) {
		TBLOB tBlob;	
		try {
			tBlob = BaseTBLOB.createTBLOB(blobBean);
			tBlob.save();
			return tBlob.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a blob failed with " + e.getMessage());
			return null;
		}
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
        	LOGGER.error("Deleting a blob for key " + objectID + " failed with: " + e);
        }
	}

	@Override
	public List<TBLOBBean> loadAll(){
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all custom options failed with " + e.getMessage());
			return null;
		}
	}

	public static List<TBLOBBean> convertTorqueListToBeanList(List<TBLOB> torqueList) {
		List<TBLOBBean> beanList = new LinkedList<TBLOBBean>();
		TBLOB tblob;
		if (torqueList!=null) {
			Iterator<TBLOB> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				tblob = itrTorqueList.next();
				beanList.add(tblob.getBean());
			}
		}
		return beanList;
	}
}
