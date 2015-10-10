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


package com.aurel.track.dao;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.exchange.excel.ExcelImportNotUniqueIdentifiersException;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.workflow.execute.WorkflowContext;


/**
 * <p>
 * This interface offers functionality to access issues or work items. Everything
 * in this system is about managing issues (tasks, milestones, risks, etc.).
 * </p>
 * <p>
 * An issue always related to a project, not directly but via an
 * object called a "component" or "subsystem". The database
 * table that contains components or subsystems and that links 
 * issue with projects is <code>TPROJCAT</code>. 
 * </br>The issues
 * themselves are stored in table <code>TWORKITEM</code>.
 * </p>
 * Each issues has four references to persons or groups (into table <code>TPERSON</code>):
 * <ul>
 *   <li>The originator (also known as "author" or "reporter") of the issue</li>
 *   <li>The current manager of the issue (see <a href="http://en.wikipedia.org/wiki/Responsibility_assignment_matrix">RACI roles</a>)</li>
 *   <li>The current responsible of the issue (see <a href="http://en.wikipedia.org/wiki/Responsibility_assignment_matrix">RACI roles</a>)</li>
 *   <li>The last person that changed the issue</li>
 * </ul>
 * Each issue has references to two releases (into table <code>TRELEASE</code>):
 * <ul>
 *   <li>The "release noticed", that is the release in which this issue was detected</li>
 *   <li>The "release scheduled", that is the release which should take care of this
 *	   issue. Releases can have states (like "planned" or "released"). Only releases
 *	   that have not been published or released can show up in the "release scheduled"
 *	   list.</li>
 * </ul>
 * There are additional links into other auxiliary tables:
 * <ul>
 *   <li>A link into the issue states (database table <code>TSTATE</code>)</li>
 *   <li>A link into issue priorities (database table <code>TPRIORITY</code>)</li>
 *   <li>A link into issue severities (database table <code>TSEVERITY</code>)</li>
 * </ul>
 * Access to an issue is restricted via access control lists (ACL). For more information
 * see interface {@link AccessControlListDAO}.
 * 
 * @see com.aurel.track.dao.PersonDAO
 *
 */
public interface WorkItemDAO {
	
	/**
	 * Gets an issue from the TWorkItem table
	 * @param objectID
	 * @return
	 */
	TWorkItemBean loadByPrimaryKey(Integer objectID) throws ItemLoaderException;
	
	/**
	 * Loads an item by project specific ID number
	 * @param projectID
	 * @param idNumber
	 * @return
	 * @throws ItemLoaderException
	 */
	TWorkItemBean loadByProjectSpecificID(Integer projectID, Integer idNumber) throws ItemLoaderException;
	
	/**
	 * Gets the list of workItems which should appear in the reminder email 
	 * @param personID
	 * @param remindMeAsOriginator
	 * @param remindMeAsManager
	 * @param remindMeAsResponsible
	 * @param dateLimit
	 * @return
	 */
	List<TWorkItemBean> loadReminderWorkItems(Integer personID,
		String remindMeAsOriginator, String remindMeAsManager, String remindMeAsResponsible, Date dateLimit);
	
	/**
	 * Gets the number of entries in TWORKITEM.
	 * @return number of table entries in TWORKITEM
	 */
	int count();
	
	/**
	 * sets/renumbers the wbs for all issues from a project
	 * @param projectID
	 */
	void setWbs(Integer projectID);
	
	/**
	 * Get the workItemBeans filtered by the FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	Set<Integer> loadParentsInContext(Integer[] projectIDs, Integer itemTypeID);
	
	/**
	 * Drops the draggedWorkItemID on the place of droppedToWorkItemID
	 * it can be after (drag to a lower place) or before the
	 * @param draggedWorkItemID
	 * @param droppedToWorkItemID
	 */
	void dropNearWorkItem(Integer draggedWorkItemID, Integer droppedToWorkItemID, boolean before);
	
	/**
	 * The parent was changed: recalculate the WBS Both the old parent and the new parent can be null
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param contextInformation
	 */
	void parentChanged(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, 
			Map<String, Object> contextInformation);
	
	/**
	 * The project was changed: recalculate the WBS
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 */
	void projectChanged(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal);
	
	/**
	 * Gets an issue by secondary key values
	 * @param identifierFieldValues
	 * @return
	 */
	TWorkItemBean loadBySecondaryKeys(Map<Integer, Object> identifierFieldValues) throws ExcelImportNotUniqueIdentifiersException;
	
	/**
	 * Gets the list of all issues.
	 * @return a list with all items as {@link TWorkItemBean}
	 */
	List<TWorkItemBean> loadAll();
	/**
	 * Gets the list of all issues with parent
	 * @return
	 */
	List<TWorkItemBean> loadAllWithParent();
	
	/**
	 * Gets the last issue created by a person 
	 * @param personID - the object identifier of the person the last created issue is to be retrieved for.
	 * <p/>
	 * @return The {@link TWorkItemBean} that was last created by this person.
	 */
	TWorkItemBean loadLastCreated(Integer personID);
	
	/**
	 * Gets the last issue created by a person for a project and issueType.<br/>
	 * When for there could be no issue found for this project-issueType combination, 
	 * get the last one by project only.
	 * @param personID - object identifier for the person who created this issue
	 * @param projectID - object identifier for the project in which this issue was created
	 * @param issueTypeID - object identifier for the issue type this issue was created
	 * <p/>
	 * @return The last issue created by this user, in this project, of this issue type.
	 */
	TWorkItemBean loadLastCreatedByProjectIssueType(Integer personID, Integer projectID, Integer issueTypeID);
	
	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the object identifiers in parameter.
	 * @param workItemKeys - the array of object identifiers for the <code>TWorkItemBean</code>s to be retrieved.
	 * <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<TWorkItemBean> loadByWorkItemKeys(int[] workItemKeys);
	List<TWorkItemBean> loadByWorkItemKeys(int[] workItemKeys,Date startDate,Date endDate);
	
	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the object identifiers in parameter.
	 * @param workItemKeys - the array of object identifiers for the <code>TWorkItemBean</code>s to be retrieved.
	 * @param archived
	 * @param deleted 
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<TWorkItemBean> loadByWorkItemKeys(int[] workItemKeys, Integer archived, Integer deleted);
	
	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by synopsis list in parameter.
	 * @param workItemKeys - synopsisList
	 * <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<TWorkItemBean> loadBySynopsisList(List<String> synopsisList);
	
	/**
	 * Loads a list with {@link TWorkItemBean}s as specified by the uuids in parameter.
	 * @param uuids - the list of object uuids for the TWorkItemBeans to be retrieved.
	 * <p/>
	 * @return A list with {@link TWorkItemBean}s.
	 */
	List<TWorkItemBean> loadByUuids(List<String> uuids);
	
	/**
	 * Loads issues for import which has associated msProjectTaskBean objects 
	 * and are of type task, include also the closed and deleted, archived tasks
	 * @param entityID
	 * @param entityType
	 * @return
	 */
	List<TWorkItemBean> loadMsProjectTasksForImport(Integer entityID, int entityType);
	
	/**
	 * Loads issues for export which has associated msProjectTaskBean objects and are of type task
	 * exclude the archived and deleted tasks depending on "closed" also the closed tasks
	 * @param entityID
	 * @param entityType
	 * @param notClosed
	 * @return
	 */
	List<TWorkItemBean> loadMsProjectTasksForExport(Integer entityID, int entityType, boolean notClosed);
	
	/**
	 * Loads all indexable issues and sets the projectID fields
	 * @return
	 */
	List<TWorkItemBean> loadIndexable();
	
	/**
	 * Gets the maximum objectID
	 */
	Integer getMaxObjectID();
	
	/**
	 * Gets the next chunk
	 * @param actualValue
	 * @param chunkInterval
	 * @return
	 */
	List<TWorkItemBean> getNextChunk(Integer actualValue, Integer chunkInterval);
	
	/**
	 * Load the workItems by a systemListID depending on a systemListType
	 * @param systemListType
	 * @param systemListID
	 * @return
	 */
	int[] loadBySystemList(int systemListType, Integer systemListID);
	
	/**
	 * Get all the issue from an project, include the closed/archived/deleted issues.
	 */
	List<TWorkItemBean> loadAllByProject(Integer projectID, Integer archived, Integer deleted);
	
	/**
	 * Saves an issue in the TWorkItem table
	 * @param issue the issue to save
	 */
	Integer save(TWorkItemBean workItemBean) throws ItemPersisterException;
	
	/**
	 * Saves an issue in the TWorkItem table
	 * @param issue the issue to save
	 */
	Integer saveSimple(TWorkItemBean workItemBean) throws ItemPersisterException;
	
	/**
	 * Gets the children of an issue.
	 * @param workItemID
	 * @return
	 */
	public List<TWorkItemBean> getChildren(Integer workItemID);
	
	/**
	 * Gets the children of a issue array
	 * @param workItemIDs
	 * @param notClosed whether to get only the unclosed child issues, or all child issues
	 * @param archived
	 * @param deleted
	 * @param itemTypesList
	 * @return
	 */
	List<TWorkItemBean> getChildren(int[] workItemIDs, boolean notClosed, Integer archived, Integer deleted, List<Integer> itemTypesList);
		
	/**
	 * Whether the item has open children besides exceptChildrenIDs
	 * @param objectID
	 * @return
	 */
	List<TWorkItemBean> getOpenChildren(Integer objectID, List<Integer> exceptChildrenIDs);

	/**
	 * Is any issue from the array still open? 
	 * @param workItemIDs
	 * @return
	 */
	boolean isAnyWorkItemOpen(int[] workItemIDs);
	
	/**
	 * Returns a deep copy of the workItemBean 
	 * @param workItemBean
	 * @return
	 */
	TWorkItemBean copyDeep(TWorkItemBean workItemBean);
	
	
	/*********************************************************
	* Manager-, Responsible-, My- and Custom Reports methods * 
	*********************************************************/
	
	/**
	 * Count the number of items in InBasket
	 * @param personID
	 * @return
	 */
	Integer countResponsibleWorkItems(Integer personID);
	
	/**
	 * Gets the issues the person is responsible for.
	 * @param personID
	 * @return
	 */
	List<TWorkItemBean> loadResponsibleWorkItems(Integer personID);

	/**
	 * Gets the meetings the person is manager or responsible or owner of.
	 * @param personID
	 * @param projectIDs
	 * @param releaseIDs
	 * @return
	 */
	List<TWorkItemBean> loadOrigManRespMeetings(Integer personID, Integer[] projectIDs, Integer[] releaseIDs);
		
	/**
	 * Gets meetings the person is registered as consulted or informed for.
	 * @param personID
	 * @param projectIDs
	 * @param releaseIDs
	 * @return
	 */
	List<TWorkItemBean> loadConsInfMeetings(Integer personID, Integer[] projectIDs, Integer[] releaseIDs);
	
	/**
	 * Count the number of filter items
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	Integer countTreeFilterItems(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID);
	
	/**
	 * Gets the items filtered by filterSelectsTO and raciBean
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<TWorkItemBean> loadTreeFilterItems(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, Date startDate, Date endDate);
	
	/**
	 * Gets the itemIDs filtered by filterSelectsTO and raciBean which are parents
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Set<Integer> loadTreeFilterParentIDs(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, Date startDate, Date endDate);
	
	/**
	 * Loads the items filtered by a TQL expression 
	 * @param filterString
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<TWorkItemBean> loadTQLFilterItems(String filterString, TPersonBean personBean, Locale locale, List<ErrorData> errors, Date startDate, Date endDate);
	
	/**
	 * Loads the itemIDs filtered by a TQL expression which are parents
	 * @param filterString
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	Set<Integer> loadTQLFilterParentIDs(String filterString, TPersonBean personBean, Locale locale, List<ErrorData> errors);
	
	/**
	 * Load projectsIDs where the user has
	 * originator/manager/responsible role for at least one workItem
	 * @param meAndMySubstituted
	 * @param meAndMySubstitutedAndGroups
	 * @return
	 */
	Set<Integer> loadOrigManRespProjects(List<Integer> meAndMySubstituted, List<Integer> meAndMySubstitutedAndGroups);
	
	/**
	 * Load projectsIDs where the user has consultant/informant
	 * role for at least one workItem
	 * @param meAndMySubstitutedAndGroups
	 * @return
	 */
	Set<Integer> loadConsultantInformantProjects(List<Integer> meAndMySubstitutedAndGroups);
	
	
	/**
	 * Load the not closed projects where the user has on behalf of
	 * role for at least one workItem
	 * @param meAndMySubstitutedAndGroups
	 * @return
	 */
	Set<Integer> loadOnBehalfOfProjects(List<Integer> meAndMySubstitutedAndGroups, List<Integer> onBehalfPickerFieldIDs);

	
	/**
	 * Gets the itemIDs from the context in a certain status without a status change after lastModified
	 * @param workflowContext
	 * @param statusID
	 * @param lastModified
	 * @return
	 */
	List<Integer> getInStatusNoStatusChangeAfter(WorkflowContext workflowContext, Integer statusID, Date lastModified);

	/**
	 * Gets the items from the context in a certain status which were not modified after lastModified
	 * @param workflowContext
	 * @param statusID
	 * @param lastModified
	 * @return
	 */
	List<TWorkItemBean> getInStatusUnmodifiedAfter(WorkflowContext workflowContext, Integer statusID, Date lastModified);
}
