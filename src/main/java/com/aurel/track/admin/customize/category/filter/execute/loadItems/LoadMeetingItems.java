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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;

/**
 * Loads the meeting items
 * @author Tamas
 *
 */
public class LoadMeetingItems {
    private static final Logger LOGGER = LogManager.getLogger(LoadMeetingItems.class);
    private static WorkItemDAO workItemDAO=DAOFactory.getFactory().getWorkItemDAO();

    private LoadMeetingItems() {
    }

    /**
     * Get the meetings where the user is either originator, manager, responsible, consulted or informed
     * @param personID
     * @param entityID
     * @param entityFlag
     * @return
     */
    public static List<TWorkItemBean> getAllMeetings(Integer personID, Integer[] projectIDs, Integer[] releaseIDs) {
        List<TWorkItemBean> origManRespMeetings = workItemDAO.loadOrigManRespMeetings(personID, projectIDs, releaseIDs);
        List<TWorkItemBean> consInfMeetings = workItemDAO.loadConsInfMeetings(personID, projectIDs, releaseIDs);
        Set<Integer> itemIDSet = new HashSet<Integer>();
        if (origManRespMeetings==null) {
            origManRespMeetings = new LinkedList<TWorkItemBean>();
        }
        LOGGER.debug("Number of meetings where I'm either originator or manager or responsible " + origManRespMeetings.size());
        for (TWorkItemBean workItemBean : origManRespMeetings) {
            itemIDSet.add(workItemBean.getObjectID());
        }
        if (consInfMeetings!=null) {
            LOGGER.debug("Number of meetings where I'm either consulted or informed " + consInfMeetings.size());
            for (TWorkItemBean workItemBean : consInfMeetings) {
                Integer itemID = workItemBean.getObjectID();
                if (!itemIDSet.contains(itemID)) {
                    itemIDSet.add(itemID);
                    origManRespMeetings.add(workItemBean);
                }
            }
        }
        LOGGER.debug("Number of my RACI meetings" + origManRespMeetings.size());
        return origManRespMeetings;
    }

}
