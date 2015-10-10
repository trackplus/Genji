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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TClassBean;
import com.aurel.track.dao.ClassDAO;

public class TClassPeer 
    extends com.aurel.track.persist.BaseTClassPeer implements ClassDAO
{
    private static final long serialVersionUID = -4119142932000459578L;

    private static final Logger LOGGER = LogManager.getLogger(TClassPeer.class);
    
	/**
	 * Loads a list with classBeans with the defined classes for a project  
	 * @param projectID 
	 * @return
	 */
	public List<TClassBean> loadByProject(Integer projectID) {		
		if (projectID==null) {
			return new ArrayList<TClassBean>();
		}
        Criteria crit = new Criteria();     
        crit.add(PROJKEY, projectID);
        crit.addAscendingOrderByColumn(LABEL);
    	try {
    		return convertTorqueListToBeanList(doSelect(crit));
        } catch (Exception e) {
            LOGGER.error("Loading the defined classes for project " + projectID + " failed with " + e.getMessage(), e);
            return null;
        }
	}
	
	
	
	private static List<TClassBean> convertTorqueListToBeanList(List<TClass> torqueList) {		
		List<TClassBean> beanList = new ArrayList<TClassBean>();
		if (torqueList!=null){
			Iterator<TClass> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TClass)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
	
	
}
