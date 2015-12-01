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


package com.trackplus.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.exchange.excel.ExcelImportNotUniqueIdentifiersException;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.persist.TPerson;
import com.trackplus.model.Tworkitem;

/**
 * <p>
 * This interface offers functionality to access issues or work items.
 * Everything in this system is about managing issues (tasks, milestones, risks,
 * etc.).
 * </p>
 * <p>
 * An issue always related to a project, not directly but via an object called a
 * "component" or "subsystem". The database table that contains components or
 * subsystems and that links issue with projects is <code>TPROJCAT</code>.
 * </br>The issues themselves are stored in table <code>TWORKITEM</code>.
 * </p>
 * Each issues has four references to persons or groups (into table
 * <code>TPERSON</code>):
 * <ul>
 * <li>The originator (also known as "author" or "reporter") of the issue</li>
 * <li>The current manager of the issue (see <a
 * href="http://en.wikipedia.org/wiki/Responsibility_assignment_matrix">RACI
 * roles</a>)</li>
 * <li>The current responsible of the issue (see <a
 * href="http://en.wikipedia.org/wiki/Responsibility_assignment_matrix">RACI
 * roles</a>)</li>
 * <li>The last person that changed the issue</li>
 * </ul>
 * Each issue has references to two releases (into table <code>TRELEASE</code>):
 * <ul>
 * <li>The "release noticed", that is the release in which this issue was
 * detected</li>
 * <li>The "release scheduled", that is the release which should take care of
 * this issue. Releases can have states (like "planned" or "released"). Only
 * releases that have not been published or released can show up in the
 * "release scheduled" list.</li>
 * </ul>
 * There are additional links into other auxiliary tables:
 * <ul>
 * <li>A link into the issue states (database table <code>TSTATE</code>)</li>
 * <li>A link into issue priorities (database table <code>TPRIORITY</code>)</li>
 * <li>A link into issue severities (database table <code>TSEVERITY</code>)</li>
 * </ul>
 * Access to an issue is restricted via access control lists (ACL). For more
 * information see interface {@link AccessControlListDAO}.
 * 
 * @see com.trackplus.dao.PersonDAO
 * 
 */
public interface WorkItemDAO {

	/**
	 * Gets an issue from the TWorkItem table
	 * 
	 * @param objectID
	 * @return
	 */
	Tworkitem loadByPrimaryKey(Integer objectID) throws ItemLoaderException;

	Tworkitem loadByProjectSpecificID(Integer projectID, Integer idNumber)
			throws ItemLoaderException;

	/**
	 * Gets the list of workItems which should appear in the reminder email
	 * 
	 * @param personID
	 * @param lead
	 * @param remindMeAsOriginator
	 * @param remindMeAsManager
	 * @param remindMeAsResponsible
	 * @param today
	 * @return
	 */
	public List<Tworkitem> loadReminderWorkItems(Integer personID,
			Integer lead, String remindMeAsOriginator,
			String remindMeAsManager, String remindMeAsResponsible, Date today);

	/**
	 * Gets the number of entries in TWORKITEM.
	 * 
	 * @return number of table entries in TWORKITEM
	 */
	int count();

	/**
	 * sets/renumbers the wbs for all issues from a project
	 * 
	 * @param projectID
	 */
	void setWbs(Integer projectID);

	/**
	 * Drops the draggedWorkItemID on the place of droppedToWorkItemID it can be
	 * after (drag to a lower place) or before the
	 * 
	 * @param draggedWorkItemID
	 * @param droppedToWorkItemID
	 */
	void dropNearWorkItem(Integer draggedWorkItemID,
			Integer droppedToWorkItemID, boolean before);

	/**
	 * The parent was changed: recalculate the WBS Both the old parent and the
	 * new parent can be null
	 * 
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param contextInformation
	 */
	void parentChanged(Tworkitem workItemBean, Tworkitem workItemBeanOriginal,
			Map<String, Object> contextInformation);

	/**
	 * The project was changed: recalculate the WBS
	 * 
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	void projectChanged(Tworkitem workitem, Tworkitem workitemOriginal);

	/**
	 * Gets an issue by secondary key values
	 * 
	 * @param identifierFieldValues
	 * @return
	 */
	Tworkitem loadBySecondaryKeys(Map<Integer, Object> identifierFieldValues)
			throws ExcelImportNotUniqueIdentifiersException;

	/**
	 * Gets the list of all issues.
	 * 
	 * @return a list with all items as {@link TWorkItemBean}
	 */
	List<Tworkitem> loadAll();

	/**
	 * Gets the list of all issues with parent
	 * 
	 * @return
	 */
	List<Tworkitem> loadAllWithParent();

	/**
	 * Gets the last issue created by a person
	 * 
	 * @param personID
	 *            - the object identifier of the person ({@link TPerson}) the
	 *            last created issue is to be retrieved for.
	 *            <p/>
	 * @return The {@link TWorkItemBean} that was last created by this person.
	 */
	Tworkitem loadLastCreated(Integer personID);

	/**
	 * Gets the last issue created by a person for a project and issueType.<br/>
	 * When for there could be no issue found for this project-issueType
	 * combination, get the last one by project only.
	 * 
	 * @param personID
	 *            - object identifier for the person who created this issue
	 * @param projectID
	 *            - object identifier for the project in which this issue was
	 *            created
	 * @param issueTypeID
	 *            - object identifier for the issue type this issue was created
	 *            <p/>
	 * @return The last issue created by this user, in this project, of this
	 *         issue type.
	 */
	Tworkitem loadLastCreatedByProjectIssueType(Integer personID,
			Integer projectID, Integer issueTypeID);

	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the object
	 * identifiers in parameter.
	 * 
	 * @param workItemKeys
	 *            - the array of object identifiers for the
	 *            <code>TWorkItemBean</code>s to be retrieved.
	 *            <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<Tworkitem> loadByWorkItemKeys(int[] workItemKeys);

	List<Tworkitem> loadByWorkItemKeys(int[] workItemKeys, Date startDate,
			Date endDate);

	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the object
	 * identifiers in parameter.
	 * 
	 * @param workItemKeys
	 *            - the array of object identifiers for the
	 *            <code>TWorkItemBean</code>s to be retrieved.
	 * @param archived
	 * @param deleted
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<Tworkitem> loadByWorkItemKeys(int[] workItemKeys, Integer archived,
			Integer deleted);

	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by synopsis list in
	 * parameter.
	 * 
	 * @param workItemKeys
	 *            - synopsisList
	 *            <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<Tworkitem> loadBySynopsisList(List<String> synopsisList);

	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the uuids in
	 * parameter.
	 * 
	 * @param uuids
	 *            - the list of object uuids for the TWorkItemBeans to be
	 *            retrieved.
	 *            <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<Tworkitem> loadByUuids(List<String> uuids);

	/**
	 * Loads issues for import which has associated msProjectTaskBean objects
	 * and are of type task, include also the closed and deleted, archived tasks
	 * 
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	List<Tworkitem> loadMsProjectTasksForImport(Integer entityID, int entityType);

	/**
	 * Loads issues for export which has associated msProjectTaskBean objects
	 * and are of type task exclude the archived and deleted tasks depending on
	 * "closed" also the closed tasks
	 * 
	 * @param entityID
	 * @param entityType
	 * @param notClosed
	 * @return
	 */
	List<Tworkitem> loadMsProjectTasksForExport(Integer entityID,
			int entityType, boolean notClosed);

	/**
	 * Loads all indexable issues and sets the projectID fields
	 * 
	 * @return
	 */
	List<Tworkitem> loadIndexable();

	/**
	 * Gets the maximum objectID
	 */
	Integer getMaxObjectID();

	/**
	 * Gets the next chunk
	 * 
	 * @param actualValue
	 * @param chunkInterval
	 * @return
	 */
	List<Tworkitem> getNextChunk(Integer actualValue, Integer chunkInterval);

	/**
	 * Load the workItems by a systemListID depending on a systemListType
	 * 
	 * @param systemListType
	 * @param systemListID
	 * @return
	 */
	int[] loadBySystemList(int systemListType, Integer systemListID);

	/**
	 * Get all the issue from an project, include the closed/archived/deleted
	 * issues.
	 */
	public List<Tworkitem> loadAllByProject(Integer projectID);

	/**
	 * Saves an issue in the TWorkItem table
	 * 
	 * @param issue
	 *            the issue to save
	 */
	Integer save(Tworkitem workItemBean) throws ItemPersisterException;

	/**
	 * Saves an issue in the TWorkItem table
	 * 
	 * @param issue
	 *            the issue to save
	 */
	Integer saveSimple(Tworkitem workItemBean) throws ItemPersisterException;

	/**
	 * Copies a workItemBean
	 * 
	 * @param workItemBean
	 * @param deep
	 */

	/**
	 * Gets the children of an issue.
	 * 
	 * @param workItemID
	 * @return
	 */
	public List<Tworkitem> getChildren(Integer workItemID);

	/**
	 * Gets the children of a issue array
	 * 
	 * @param workItemIDs
	 * @param notClosed
	 *            whether to get only the unclosed child issues, or all child
	 *            issues
	 * @param archived
	 * @param deleted
	 * @return
	 */
	public List<Tworkitem> getChildren(int[] workItemIDs, boolean notClosed,
			Integer archived, Integer deleted);

	/**
	 * Whether the item has open children besides exceptChildrenIDs
	 * 
	 * @param objectID
	 * @return
	 */
	public List<TWorkItemBean> getOpenChildren(Integer objectID,
			List<Integer> exceptChildrenIDs);

	/**
	 * Is any issue from the array still open?
	 * 
	 * @param workItemIDs
	 * @return
	 */
	public boolean isAnyWorkItemOpen(int[] workItemIDs);

	/**
	 * Returns a deep copy of the workItemBean
	 * 
	 * @param workItemBean
	 * @return
	 */
	public Tworkitem copydeep(Tworkitem workitem);

	/*********************************************************
	 * Manager-, Responsible-, My- and Custom Reports methods *
	 *********************************************************/

	/**
	 * Gets the set of issue oids from a project/release for which the person
	 * has direct or indirect consulted or informed role.
	 * 
	 * @param projectReleaseID
	 * @param personID
	 * @param isProject
	 *            <ul>
	 *            <li>true: projectReleaseID is a projectID</li>
	 *            <li>false: projectReleaseID is a releaseID false</li>
	 *            </ul>
	 */
	// Set<Integer> getProjectReleaseWorkItemsWithConsInfRoles(Integer

	/**
	 * Gets the set of issue oids from a project/release ID list for which the
	 * person has direct or indirect consulted or informed role.
	 * 
	 * @param projectReleaseIDList
	 * @param personID
	 * @param isProject
	 *            <ul>
	 *            <li>true: projectReleaseID is a projectID</li>
	 *            <li>false: projectReleaseID is a releaseID false</li>
	 *            </ul>
	 */
	Set<Integer> getProjectIDsReleaseIDsWorkItemsWithConsInfRoles(
			List<Integer> projectReleaseIDList, Integer personID,
			boolean isProject);

	/**
	 * Gets the set of issue oids from a project/release for which the person is
	 * indirectly responsible through a group membership.
	 * 
	 * @param projectReleaseID
	 *            list
	 * @param personID
	 * @param isProject
	 *            <ul>
	 *            <li>true: projectReleaseID is a projectID</li>
	 *            <li>false: projectReleaseID is a releaseID false</li>
	 *            </ul>
	 */
	Set<Integer> getProjectReleaseWorkItemsWithResponsiblesThroughGroup(
			Integer projectReleaseID, Integer personID, boolean isProject);

	/**
	 * Gets the set of issue oids from a project/release ID list for which the
	 * person is indirectly responsible through a group membership.
	 * 
	 * @param projectReleaseIDList
	 * @param personID
	 * @param isProject
	 *            <ul>
	 *            <li>true: projectReleaseID is a projectID</li>
	 *            <li>false: projectReleaseID is a releaseID false</li>
	 *            </ul>
	 */
	Set<Integer> getProjectIDsReleaseIDsWorkItemsWithResponsiblesThroughGroup(
			List<Integer> projectReleaseIDList, Integer personID,
			boolean isProject);

	/**
	 * Gets the issues the person is manager for. If project is not null then
	 * just gets issues for that project.
	 * 
	 * @param personID
	 * @param entity
	 *            oid of either project, release noticed, release scheduled,
	 *            project category, or class
	 * @param entityFlag
	 *            the meaning of the <code>entity</code> parameter. Can be
	 *            <code>SystemFields.PROJECT</code>,
	 *            <code>SystemFields.RELEASENOTICED</code>,
	 *            <code>SystemFields.RELEASESCHEDULED</code>,
	 *            <code>SystemFields.SUBPROJECT</code>, or
	 *            <code>SystemFields.CLASS</code>.
	 * @return
	 */
	List<Tworkitem> loadManagerWorkItems(Integer personID, Integer entity,
			Integer entityFlag);

	/**
	 * Gets the issues the person is responsible for. If project is not null
	 * then just gets issues for that project.
	 * 
	 * @param personID
	 * @param entity
	 *            oid of either project, release noticed, release scheduled,
	 *            project category, or class
	 * @param entityFlag
	 *            the meaning of the <code>entity</code> parameter. Can be
	 *            <code>SystemFields.PROJECT</code>,
	 *            <code>SystemFields.RELEASENOTICED</code>,
	 *            <code>SystemFields.RELEASESCHEDULED</code>,
	 *            <code>SystemFields.SUBPROJECT</code>, or
	 *            <code>SystemFields.CLASS</code>.
	 * @return
	 */
	List<Tworkitem> loadResponsibleWorkItems(Integer personID, Integer entity,
			Integer entityFlag);

	/**
	 * Get the workItemBeans form the InBasket If project is not null then just
	 * for that project
	 * 
	 * @param personID
	 * @param project
	 * @param entityFlag
	 *            the meaning of the project: can be project or release
	 * @return
	 */
	List<Tworkitem> loadInBasketWorkItems(Integer personID, Integer entity,
			Integer entityFlag);

	/**
	 * Gets the issues the person is originator for. If project is not null then
	 * just gets issues for that project.
	 * 
	 * @param personID
	 * @param entity
	 *            oid of either project, release noticed, release scheduled,
	 *            project category, or class
	 * @param entityFlag
	 *            the meaning of the <code>entity</code> parameter. Can be
	 *            <code>SystemFields.PROJECT</code>,
	 *            <code>SystemFields.RELEASENOTICED</code>,
	 *            <code>SystemFields.RELEASESCHEDULED</code>,
	 *            <code>SystemFields.SUBPROJECT</code>, or
	 *            <code>SystemFields.CLASS</code>.
	 * @return
	 */
	List<Tworkitem> loadReporterWorkItems(Integer personID, Integer entity,
			Integer entityFlag);

	/**
	 * Gets the meetings the person is manager or responsible or owner of. If
	 * project is not null then just for that project
	 * 
	 * @param personID
	 * @param entity
	 *            oid of either project, release noticed, release scheduled,
	 *            project category, or class
	 * @param entityFlag
	 *            the meaning of the <code>entity</code> parameter. Can be
	 *            <code>SystemFields.PROJECT</code>,
	 *            <code>SystemFields.RELEASENOTICED</code>,
	 *            <code>SystemFields.RELEASESCHEDULED</code>,
	 *            <code>SystemFields.SUBPROJECT</code>, or
	 *            <code>SystemFields.CLASS</code>.
	 * @return
	 */
	List<Tworkitem> loadOrigManRespMeetings(Integer personID, Integer entity,
			Integer entityFlag);

	/**
	 * Gets meetings the person is registered as consulted or informed for. If
	 * project is not null then just for that project/release
	 * 
	 * @param personID
	 * @param entity
	 *            oid of either project, release noticed, release scheduled,
	 *            project category, or class
	 * @param entityFlag
	 *            the meaning of the <code>entity</code> parameter. Can be
	 *            <code>SystemFields.PROJECT</code>,
	 *            <code>SystemFields.RELEASENOTICED</code>,
	 *            <code>SystemFields.RELEASESCHEDULED</code>,
	 *            <code>SystemFields.SUBPROJECT</code>, or
	 *            <code>SystemFields.CLASS</code>.
	 * @return
	 */
	List<Tworkitem> loadConsInfMeetings(Integer personID, Integer entity,
			Integer entityFlag);

	/**
	 * Gets items the person is registered as consulted or informed for. If
	 * project is not null then just for that project/release
	 * 
	 * @param personID
	 * @param entity
	 *            oid of either project, release noticed, release scheduled,
	 *            project category, or class
	 * @param entityFlag
	 *            the meaning of the <code>entity</code> parameter. Can be
	 *            <code>SystemFields.PROJECT</code>,
	 *            <code>SystemFields.RELEASENOTICED</code>,
	 *            <code>SystemFields.RELEASESCHEDULED</code>,
	 *            <code>SystemFields.SUBPROJECT</code>, or
	 *            <code>SystemFields.CLASS</code>.
	 * @return
	 */
	List<Tworkitem> loadConsInf(Integer personID, Integer entity,
			Integer entityFlag);

	/**
	 * Gets the issues filtered by the FilterSelectsTO.
	 * 
	 * @param filterSelectsTO
	 * @return
	 */
	List<Tworkitem> loadCustomReportWorkItems(FilterUpperTO filterSelectsTO);

	List<Tworkitem> loadCustomReportWorkItems(FilterUpperTO filterSelectsTO,
			Date startDate, Date enddDate);

	/**
	 * Gets the set of workItemIDs filtered by FilterSelectsTO for which the
	 * person has direct or indirect consultant or informant role
	 * 
	 * @param filterSelectsTO
	 * @param personID
	 * @return
	 */
	Set<Integer> getCustomWorkItemsWithConsInfRoles(
			FilterUpperTO filterSelectsTO, Integer personID);

	/**
	 * Gets the set of workItemIDs filtered by FilterSelectsTO for which the
	 * person is indirectly responsible
	 * 
	 * @param filterSelectsTO
	 * @param personID
	 * @return
	 */
	Set<Integer> getCustomWorkItemsWithResponsiblesThroughGroup(
			FilterUpperTO filterSelectsTO, Integer personID);

	/**
	 * Gets the set of workItemIDs by an array of workItemIDs for which the
	 * person has direct or indirect consultant or informant role
	 * 
	 * @param workItemIDs
	 * @param personID
	 * @return
	 */
	Set<Integer> getLuceneWorkItemIDsWithConsInfRoles(int[] workItemIDs,
			Integer personID);

	/**
	 * Gets the set of workItemIDs by an array of workItemIDs for which the
	 * person is indirectly responsible
	 * 
	 * @param reportConfigBean
	 * @param personID
	 * @return
	 */
	Set<Integer> getLuceneWorkItemIDsWithResponsiblesThroughGroup(
			int[] workItemIDs, Integer personID);

}
