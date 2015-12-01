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

package com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria;

import java.util.List;

import org.apache.torque.util.Criteria;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.persist.TWorkItemPeer;

/**
 * Gets the criteria for responsible items
 * @author Tamas
 *
 */
public class ResponsibleCriteria {

	private ResponsibleCriteria() {
	}

    /**
     * Prepares the responsible criteria
     * @param personID
     * @return
     */
    public static Criteria prepareResponsibleCriteria(Integer personID) {
        Criteria crit = new Criteria();
        CriteriaUtil.addStateFlagUnclosedCriteria(crit);
        CriteriaUtil.addActiveInactiveProjectCriteria(crit);
        CriteriaUtil.addArchivedDeletedFilter(crit);
        CriteriaUtil.addAccessLevelFilter(crit, personID);
        List<Integer> groupIDList = AccessBeans.getMeAndSubstitutedAndGroups(personID);
        crit.addIn(TWorkItemPeer.RESPONSIBLE, groupIDList);
        return crit;
    }

}
