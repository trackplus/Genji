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
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TEmailProcessedBean;
import com.aurel.track.dao.EmailProcessedDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TEmailProcessedPeer
    extends com.aurel.track.persist.BaseTEmailProcessedPeer implements EmailProcessedDAO {
    private static final Logger LOGGER = LogManager.getLogger(TEmailProcessedPeer.class);

	public static final long serialVersionUID = 400L;
	
       /* Gets a exportTemplateBean from the TExportTemplate table
       * @param objectID
       * @return
       */
        public TEmailProcessedBean loadByPrimaryKey(Integer objectID) {
            TEmailProcessedBean templateBean = null;
            TEmailProcessed tobject = null;
            try {
                tobject = retrieveByPK(objectID);
            } catch (TorqueException e) {
               LOGGER.warn("Loading of a EmailProcessed by primary key " + objectID + " failed with " + e.getMessage());
               LOGGER.debug(ExceptionUtils.getStackTrace(e));
            }
            if (tobject != null) {
            	templateBean=tobject.getBean();
            }
            return templateBean;

        }

        public List<TEmailProcessedBean> loadAll() {
            List torqueList = null;
            Criteria crit = new Criteria();
            try {
                torqueList = doSelect(crit);
            } catch (TorqueException e) {
                LOGGER.error("Loading all exportTemplate failed with " + e.getMessage(), e);
            }
            return convertTorqueListToBeanList(torqueList);
        }

        public Integer save(TEmailProcessedBean emailProcessedBean) {
            try {
                TEmailProcessed tobject = BaseTEmailProcessed.createTEmailProcessed(emailProcessedBean);
                tobject.save();
                return tobject.getObjectID();
            } catch (Exception e) {
                LOGGER.error("Saving of a EmailProcessed failed with " + e.getMessage(), e);
                return null;
            }
        }

        public void delete(Integer objectID) {
            try {
                doDelete(SimpleKey.keyFor(objectID));
            } catch (TorqueException e) {
                LOGGER.error("Deleting a exportTemplate for key " + objectID + " failed with: " + e);
            }
        }

        public boolean isDeletable(Integer objectID) {
            return true;
        }

     private List<TEmailProcessedBean> convertTorqueListToBeanList(List torqueList) {
		List<TEmailProcessedBean> beanList = new ArrayList<TEmailProcessedBean>();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TEmailProcessed)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
    
}
