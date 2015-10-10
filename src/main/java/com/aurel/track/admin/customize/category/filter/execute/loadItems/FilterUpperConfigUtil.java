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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO.RELEASE_SELECTOR;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.util.GeneralUtils;

/**
 * Configure {@link FilterUpperTO} objects for different scenarios
 * @author Tamas
 *
 */
public class FilterUpperConfigUtil {

    /****************************Project/release based FilterUpperTO*************************************/

    /**
     * Gets the FilterUpperTO by projectReleaseIDs
     * @param fromProject
     * @param projectReleaseBaseIDs
     * @param includeSubs
     * @return
     */
    public static FilterUpperTO getByProjectReleaseIDs(boolean fromProject, Integer[] projectReleaseBaseIDs,
            boolean includeSubs, boolean includeOpen, boolean includeClosed) {
        FilterUpperTO filterUpperTO = new FilterUpperTO();
        filterUpperTO.setIncludeOpen(includeOpen);
        filterUpperTO.setIncludeClosed(includeClosed);
        if (fromProject) {
            Integer[] projectIDs = null;
            if (includeSubs) {
                projectIDs = ProjectBL.getDescendantProjectIDs(projectReleaseBaseIDs);
            } else {
                projectIDs = projectReleaseBaseIDs;
            }
            filterUpperTO.setSelectedProjects(projectIDs);
        } else {
            filterUpperTO.setReleaseTypeSelector(RELEASE_SELECTOR.SCHEDULED);
            Integer[] releaseIDs = null;
            if (includeSubs) {
                releaseIDs = ReleaseBL.getDescendantReleaseIDs(projectReleaseBaseIDs);
            } else {
                releaseIDs = projectReleaseBaseIDs;
            }
            if (releaseIDs!=null && releaseIDs.length>0) {
                List<TReleaseBean> releaseBeans = ReleaseBL.loadByReleaseIDs(GeneralUtils.createIntegerListFromIntegerArr(releaseIDs));
                Set<Integer> projectSet = new HashSet<Integer>();
                if (releaseBeans!=null) {
                    for (TReleaseBean releaseBean : releaseBeans) {
                        projectSet.add(releaseBean.getProjectID());
                    }
                }
                filterUpperTO.setSelectedProjects(GeneralUtils.createIntegerArrFromSet(projectSet));
            }
            filterUpperTO.setSelectedReleases(releaseIDs);
        }
        return filterUpperTO;
    }

    /**
     * Gets the FilterUpperTO by a projectReleaseID
     * @param fromProject
     * @param projectReleaseBaseID it is always positive fromProject defines whether it is a project or releaseID
     * @param includeSubs
     */
    public static FilterUpperTO getByProjectReleaseID(boolean fromProject, Integer projectReleaseBaseID,
            boolean includeSubs, boolean includeOpen, boolean includeClosed) {
        FilterUpperTO filterUpperTO = new FilterUpperTO();
        filterUpperTO.setIncludeOpen(includeOpen);
        filterUpperTO.setIncludeClosed(includeClosed);
        if (fromProject) {
            Integer[] projectIDs = null;
            if (includeSubs) {
                projectIDs = ProjectBL.getDescendantProjectIDs(new Integer[] {projectReleaseBaseID});
            } else {
                projectIDs = new Integer[] {projectReleaseBaseID};
            }
            filterUpperTO.setSelectedProjects(projectIDs);
        } else {
            filterUpperTO.setReleaseTypeSelector(RELEASE_SELECTOR.SCHEDULED);
            Integer[] releaseIDs = null;
            if (includeSubs) {
                releaseIDs = ReleaseBL.getDescendantReleaseIDs(new Integer[] {projectReleaseBaseID});
            } else {
                releaseIDs = new Integer[] {projectReleaseBaseID};
            }
            filterUpperTO.setSelectedReleases(releaseIDs);
            TReleaseBean releaseBean = LookupContainer.getReleaseBean(projectReleaseBaseID);
            if (releaseBean!=null) {
                filterUpperTO.setSelectedProjects(new Integer[] {releaseBean.getProjectID()});
            }
        }
        return filterUpperTO;
    }

    /**
     * Gets the FilterUpperTO by a projectReleaseID
     * @param projectOrReleaseID if negative it is considered a projectID, otherwise releaseID
     * @param includeSubs
     */
    public static FilterUpperTO getByProjectReleaseID(Integer projectOrReleaseID,
            boolean includeSubs, boolean includeOpen, boolean includeClosed) {
        FilterUpperTO filterUpperTO = new FilterUpperTO();
        filterUpperTO.setIncludeOpen(includeOpen);
        filterUpperTO.setIncludeClosed(includeClosed);
        if (projectOrReleaseID<0) {
            Integer[] projectIDs = null;
            if (includeSubs) {
                projectIDs = ProjectBL.getDescendantProjectIDs(new Integer[] {-projectOrReleaseID});
            } else {
                projectIDs = new Integer[] {-projectOrReleaseID};
            }
            filterUpperTO.setSelectedProjects(projectIDs);
        } else {
            Integer[] releaseIDs = null;
            if (includeSubs) {
                releaseIDs = ReleaseBL.getDescendantReleaseIDs(new Integer[] {projectOrReleaseID});
            } else {
                releaseIDs = new Integer[] {projectOrReleaseID};
            }
            filterUpperTO.setSelectedReleases(releaseIDs);
            TReleaseBean releaseBean = LookupContainer.getReleaseBean(projectOrReleaseID);
            if (releaseBean!=null) {
                filterUpperTO.setSelectedProjects(new Integer[] {releaseBean.getProjectID()});
            }
        }
        return filterUpperTO;
    }

    /**
     * Gets the FilterUpperTO by a projectReleaseID
     * @param projectOrReleaseID always positive the entityType defines whether it is a project or releaseID
     * @param includeSubs
     */
    public static FilterUpperTO getByProjectReleaseID(Integer projectOrReleaseID, Integer entityType, boolean includeSubs, boolean includeOpen, boolean includeClosed) {
        FilterUpperTO filterUpperTO = new FilterUpperTO();
        filterUpperTO.setIncludeOpen(includeOpen);
        filterUpperTO.setIncludeClosed(includeClosed);
        if (entityType!=null && SystemFields.INTEGER_PROJECT.equals(entityType)) {
            Integer[] projectIDs = null;
            if (includeSubs) {
                projectIDs = ProjectBL.getDescendantProjectIDs(new Integer[] {-projectOrReleaseID});
            } else {
                projectIDs = new Integer[] {-projectOrReleaseID};
            }
            filterUpperTO.setSelectedProjects(projectIDs);
        } else {
            Integer[] releaseIDs = null;
            if (includeSubs) {
                releaseIDs = ReleaseBL.getDescendantReleaseIDs(new Integer[] {projectOrReleaseID});
            } else {
                releaseIDs = new Integer[] {projectOrReleaseID};
            }
            filterUpperTO.setSelectedReleases(releaseIDs);
            TReleaseBean releaseBean = LookupContainer.getReleaseBean(projectOrReleaseID);
            if (releaseBean!=null) {
                filterUpperTO.setSelectedProjects(new Integer[] {releaseBean.getProjectID()});
            }
        }
        return filterUpperTO;
    }


}
