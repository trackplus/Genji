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

package com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria;

import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TStatePeer;
import com.aurel.track.persist.TSystemStatePeer;
import com.aurel.track.persist.TWorkItemPeer;

/**
 * Utility methods for fine tuning the item load criteria
 * @author Tamas
 *
 */
public class CriteriaUtil {

    private CriteriaUtil() {
    }

    /**
     * Add the not closed items condition
     * @param crit
     */
    public static Criteria addStateFlagUnclosedCriteria(Criteria crit) {
        crit.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        crit.add(TStatePeer.STATEFLAG, TStateBean.STATEFLAGS.CLOSED, Criteria.NOT_EQUAL);
        return crit;
    }

    /**
     * Add the closed items filter.
     * @param crit
     */
    public static Criteria addStateFlagClosedCriteria(Criteria crit) {
        crit.addJoin(TWorkItemPeer.STATE, TStatePeer.PKEY);
        crit.add(TStatePeer.STATEFLAG, TStateBean.STATEFLAGS.CLOSED, Criteria.EQUAL);
        return crit;
    }

    /**
     * Add the active projects condition
     * @param crit
     */
    public static Criteria addActiveProjectCriteria(Criteria crit) {
        crit.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        crit.addJoin(TProjectPeer.STATUS, TSystemStatePeer.OBJECTID);
        crit.add(TSystemStatePeer.STATEFLAG, TSystemStateBean.STATEFLAGS.ACTIVE);
        return crit;
    }

    /**
     * Add the active or inactive projects condition
     * @param crit
     */
    public static Criteria addActiveInactiveProjectCriteria(Criteria crit) {
        crit.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
        crit.addJoin(TProjectPeer.STATUS, TSystemStatePeer.OBJECTID);
        crit.addIn(TSystemStatePeer.STATEFLAG, new Object[] {TSystemStateBean.STATEFLAGS.ACTIVE, TSystemStateBean.STATEFLAGS.INACTIVE});
        return crit;
    }

    /**
     * Add condition to get only issues that are not archived.
     * @param crit the <code>Criteria</code> object that is augmented with the unarchived criterion
     * @return the augmented <code>Criteria</code> object
     */
    public static Criteria addArchivedDeletedFilter(Criteria crit) {
        // we need to consider issues that are explicitly unarchived and
        // also those that have a null entry in that attribute
        Criterion unarchivedCrit  = crit.getNewCriterion(
                TWorkItemPeer.ARCHIVELEVEL,
                TWorkItemBean.ARCHIVE_LEVEL_UNARCHIVED,
                Criteria.EQUAL);
        Criterion nullCrit = crit.getNewCriterion(TWorkItemPeer.ARCHIVELEVEL,
                (Object)null, Criteria.ISNULL);
        crit.add(nullCrit.or(unarchivedCrit));
        return crit;
    }

    /**
     * add the criteria for archived/deleted
     * @param crit, the criteria to add to
     * @param archived
     * @param deleted
     */
    public static void addArchivedCriteria(Criteria crit, Integer archived, Integer deleted) {
        if ((archived==null || archived.intValue()==FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED) &&
                (deleted==null || deleted.intValue()==FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED)) {
            //archived and deleted select not shown to the user (no project admin)
            //means exclude archived and deleted
            addArchivedDeletedFilter(crit);
        } else {
            if (archived==null) {
                //should never happen (either both or none has value)
                archived = Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED);
            }
            if (deleted==null) {
                //should never happen (either both or none has value)
                deleted = Integer.valueOf(FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED);
            }
            Criterion archiveCriterion = null;
            Criterion nullCriterion = null;
            switch (archived.intValue()) {
            case FilterUpperTO.ARCHIVED_FILTER.EXCLUDE_ARCHIVED:
                archiveCriterion = crit.getNewCriterion(TWorkItemPeer.ARCHIVELEVEL,
                        TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED, Criteria.NOT_EQUAL);
                nullCriterion = crit.getNewCriterion(TWorkItemPeer.ARCHIVELEVEL, (Object)null, Criteria.ISNULL);
                archiveCriterion.or(nullCriterion);
                break;
            case FilterUpperTO.ARCHIVED_FILTER.ONLY_ARCHIVED:
                archiveCriterion = crit.getNewCriterion(TWorkItemPeer.ARCHIVELEVEL,
                        TWorkItemBean.ARCHIVE_LEVEL_ARCHIVED, Criteria.EQUAL);
                break;
            default:
            	break;
            }
            Criterion deleteCriterion = null;
            switch (deleted.intValue()) {
            case FilterUpperTO.DELETED_FILTER.EXCLUDE_DELETED:
                deleteCriterion = crit.getNewCriterion(TWorkItemPeer.ARCHIVELEVEL,
                        TWorkItemBean.ARCHIVE_LEVEL_DELETED, Criteria.NOT_EQUAL);
                nullCriterion = crit.getNewCriterion(TWorkItemPeer.ARCHIVELEVEL, (Object)null, Criteria.ISNULL);
                deleteCriterion.or(nullCriterion);
                break;
            case FilterUpperTO.DELETED_FILTER.ONLY_DELETED:
                deleteCriterion = crit.getNewCriterion(TWorkItemPeer.ARCHIVELEVEL,
                        TWorkItemBean.ARCHIVE_LEVEL_DELETED, Criteria.EQUAL);
                break;
            default:
            	break;
            }
            if (archiveCriterion!=null && deleteCriterion!=null) {
                crit.add(archiveCriterion.and(deleteCriterion));
            } else {
                if (archiveCriterion!=null) {
                    crit.add(archiveCriterion);
                }
                if (deleteCriterion!=null) {
                    crit.add(deleteCriterion);
                }
            }
        }
    }

    /**
     * Add criteria condition to exclude other person's private items
     * @param crit
     * @param personID
     * @return
     */
    public static Criteria addAccessLevelFilter(Criteria crit, Integer personID) {
        Criterion publicCrit  = crit.getNewCriterion(TWorkItemPeer.ACCESSLEVEL,
                TWorkItemBean.ACCESS_LEVEL_PUBLIC, Criteria.EQUAL);
        Criterion nullCrit = crit.getNewCriterion(TWorkItemPeer.ACCESSLEVEL,
                (Object)null, Criteria.ISNULL);
        Criterion privateCrit  = crit.getNewCriterion(TWorkItemPeer.ACCESSLEVEL,
                TWorkItemBean.ACCESS_LEVEL_PRIVATE, Criteria.EQUAL);
        Criterion personIsOriginator = crit.getNewCriterion(
                TWorkItemPeer.ORIGINATOR, personID, Criteria.EQUAL);
        crit.add(nullCrit.or(publicCrit).or(privateCrit.and(personIsOriginator)));
        return crit;
    }

    /**
     * Add criteria condition to exclude other person's private items
     * @param crit
     * @param personID
     * @return
     */
    public static Criteria addAccessLevelPublicFilter(Criteria crit) {
        Criterion publicCrit  = crit.getNewCriterion(TWorkItemPeer.ACCESSLEVEL,
                TWorkItemBean.ACCESS_LEVEL_PUBLIC, Criteria.EQUAL);
        Criterion nullCrit = crit.getNewCriterion(TWorkItemPeer.ACCESSLEVEL,
                (Object)null, Criteria.ISNULL);
        crit.add(nullCrit.or(publicCrit));
        return crit;
    }
}
