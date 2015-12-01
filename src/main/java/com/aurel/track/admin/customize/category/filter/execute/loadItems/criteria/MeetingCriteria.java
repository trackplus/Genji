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
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.persist.TListTypePeer;
import com.aurel.track.persist.TNotifyPeer;
import com.aurel.track.persist.TWorkItemPeer;

public class MeetingCriteria {

	private MeetingCriteria() {
	}

    /**
     * @param personID
     * @param projectIDs
     * @param releaseIDs
     * @return
     */
    public static Criteria prepareOrigManRespMeetingsCriteria(Integer personID, Integer[] projectIDs, Integer[] releaseIDs) {
        Criteria crit = new Criteria();
        CriteriaUtil.addStateFlagUnclosedCriteria(crit);
        CriteriaUtil.addActiveProjectCriteria(crit);
        if (projectIDs!=null && projectIDs.length>0) {
            crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDs);
        }
        if (releaseIDs!=null && releaseIDs.length>0) {
            crit.addIn(TWorkItemPeer.RELSCHEDULEDKEY, releaseIDs);
        }
        CriteriaUtil.addArchivedDeletedFilter(crit);
        CriteriaUtil.addAccessLevelFilter(crit, personID);
        addMeetingTypeCriteria(crit);
        List<Integer> meAndSubstituted = AccessBeans.getMeAndSubstituted(personID);
        List<Integer> groupIDList = AccessBeans.getMeAndSubstitutedAndGroups(personID);//GroupMemberBL.getGroupsIDsForPerson(personID);
        Criterion manCrit = crit.getNewCriterion(TWorkItemPeer.OWNER, meAndSubstituted, Criteria.IN);
        Criterion respCrit = crit.getNewCriterion(TWorkItemPeer.RESPONSIBLE, groupIDList, Criteria.IN);
        Criterion origCrit = crit.getNewCriterion(TWorkItemPeer.ORIGINATOR, meAndSubstituted, Criteria.IN);
        crit.add(manCrit.or(respCrit).or(origCrit));
        return crit;
    }

    /**
     * @param personID
     * @param projectIDs
     * @param releaseIDs
     * @return
     */
    public static Criteria prepareConsInfMeetingsCriteria(Integer personID, Integer[] projectIDs, Integer[] releaseIDs) {
        Criteria crit = new Criteria();
        CriteriaUtil.addStateFlagUnclosedCriteria(crit);
        CriteriaUtil.addActiveProjectCriteria(crit);
        if (projectIDs!=null && projectIDs.length>0) {
            crit.addIn(TWorkItemPeer.PROJECTKEY, projectIDs);
        }
        if (releaseIDs!=null && releaseIDs.length>0) {
            crit.addIn(TWorkItemPeer.RELSCHEDULEDKEY, releaseIDs);
        }
        CriteriaUtil.addArchivedDeletedFilter(crit);
        CriteriaUtil.addAccessLevelFilter(crit, personID);
        addMeetingTypeCriteria(crit);
        crit.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        crit.add(TListTypePeer.TYPEFLAG, TListTypeBean.TYPEFLAGS.MEETING);
        List<Integer> groupIDList = AccessBeans.getMeAndSubstitutedAndGroups(personID);//GroupMemberBL.getGroupsIDsForPerson(personID);
        crit.addJoin(TWorkItemPeer.WORKITEMKEY, TNotifyPeer.WORKITEM);
        crit.add(TNotifyPeer.PERSONKEY, groupIDList, Criteria.IN);
        crit.setDistinct();
        return crit;
    }

    /**
     * Add the not closed items condition
     * @param crit
     */
    public static Criteria addMeetingTypeCriteria(Criteria crit) {
        crit.addJoin(TWorkItemPeer.CATEGORYKEY, TListTypePeer.PKEY);
        crit.add(TListTypePeer.TYPEFLAG, TListTypeBean.TYPEFLAGS.MEETING);
        return crit;
    }
}
