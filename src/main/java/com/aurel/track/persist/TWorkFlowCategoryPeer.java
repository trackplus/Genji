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
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TWorkFlowCategoryBean;
import com.aurel.track.dao.WorkflowCategoryDAO;

/**  
 * @author Sergej Redel <sergej@schoene-hochzeit.com>
 * @author Martin Bierkoch <martin.bierkoch@gmx.de>
 * @version $Revision: 1795 $ $Date: 2015-11-12 13:07:18 +0100 (Do, 12 Nov 2015) $
 */
public class TWorkFlowCategoryPeer
    extends com.aurel.track.persist.BaseTWorkFlowCategoryPeer
    implements WorkflowCategoryDAO
{

    private static final long serialVersionUID = 8083198830986584687L;

    private static final Logger LOGGER = LogManager.getLogger(TWorkFlowCategoryPeer.class);
    
    /**
     * @return a List with all project types defined in this system
     * If the mandatory entry with object id 0 for generic projects
     * does not exist, it is being created and saved to the database.
     * Thus, this method should always return a List with at least
     * one entry.
     */
/*    public static List load() {
        Criteria crit = new Criteria();
        // first check if there is the mandatory entry with object id of 0
        crit.add(BaseTWorkFlowPeer.OBJECTID, new Integer(0), Criteria.EQUAL );        
        try {
            List workFlows = doSelect(crit);
            
            Iterator i = workFlows.iterator();
            TProjectType workFlow = null;
            if (!i.hasNext())
            {
                LOGGER.error("Database not consistent, "
                             + "forgot to run update script?");
            }
            crit = new Criteria(); // get a fresh one
            workFlows = doSelect(crit);
            LOGGER.error("TProjectTypePeer "+workFlows.toString());
            
            return (workFlows);
        }
        catch (Exception e) {
            return null;
        }
    }*/
    
    /*public static List load(Integer projectType,
            Integer stateFrom,
            Integer stateTo,
            int categoryKey) throws TorqueException {
        
        Criteria crit = new Criteria();
        crit.add(BaseTWorkFlowPeer.PROJECTTYPE, projectType, Criteria.EQUAL);
        crit.add(BaseTWorkFlowPeer.STATEFROM, stateFrom, Criteria.EQUAL);
        crit.add(BaseTWorkFlowPeer.STATETO, stateTo, Criteria.EQUAL);
        crit.add(BaseTWorkFlowCategoryPeer.CATEGORY, new Integer(categoryKey), Criteria.EQUAL);
        crit.addJoin(BaseTWorkFlowCategoryPeer.WORKFLOW, BaseTWorkFlowPeer.OBJECTID);        
        return doSelect(crit);
        
    }*/
	
	/*public static void doDelete(List categoryKeys) throws TorqueException
    {    	
    	for(int i=0; i < categoryKeys.size(); i++ )
    	{
    		TWorkFlowCategory tCategory = (TWorkFlowCategory) categoryKeys.get(i);
    		BaseTWorkFlowCategoryPeer.doDelete(tCategory.getPrimaryKey());
    		
    	}
    }*/
	
	/*public static void doDeleteByObjects(List twfc) throws TorqueException
	{
	    if (twfc != null && twfc.size() > 0) {
	        Iterator it = twfc.iterator();
	        while (it.hasNext()) {
	            TWorkFlowCategory tWorkFlowCategory 
	            = (TWorkFlowCategory) it.next();
	            // just to make sure we stay clean we delete them all
	            BaseTWorkFlowCategoryPeer.doDelete(tWorkFlowCategory);
	        }
	    }
	}*/
	
	/**
	 * Load all workflow categories
	 * @return
	 */
	@Override
	public List<TWorkFlowCategoryBean> loadAll() {
		try {
			Criteria crit = new Criteria();
			return convertTorqueListToBeanList(doSelect(crit));
		}
		catch (Exception e) {
			LOGGER.error("Loading all workflow categories for migration failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Convert the torque objects to beans
	 * @param torqueList
	 * @return
	 */
	private static List<TWorkFlowCategoryBean> convertTorqueListToBeanList(List<TWorkFlowCategory> torqueList) {
		List<TWorkFlowCategoryBean> beanList = new ArrayList<TWorkFlowCategoryBean>();
		if (torqueList!=null){
			for (TWorkFlowCategory tWorkFlowCategory : torqueList) {
				beanList.add(tWorkFlowCategory.getBean());
			}
		}
		return beanList;
	}
}
