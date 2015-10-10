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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.dao.ProjectDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.util.GeneralUtils;
import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;
import com.workingdogs.village.Value;

/** 
 *
 */
public class TProjectPeer 
	extends com.aurel.track.persist.BaseTProjectPeer implements ProjectDAO {
	
	private static final long serialVersionUID = -9027581282625320080L;
	private static final Logger LOGGER = LogManager.getLogger(TProjectPeer.class); 
	
	private static Class[] deletePeerClasses = {
		TWorkItemPeer.class,
		TReleasePeer.class, //overrided doDelete
		TProjectCategoryPeer.class, //overrided doDelete
		TClassPeer.class, //overrided doDelete
		TAccessControlListPeer.class,
		TReportLayoutPeer.class,
		TProjectAccountPeer.class,
		TProjectReportRepositoryPeer.class,
		TVersionControlParameterPeer.class,
		TFieldConfigPeer.class, //overrided doDelete
		TFieldPeer.class, //overrided doDelete
		TScreenConfigPeer.class,
		TScreenPeer.class,
		TInitStatePeer.class,
		TEventPeer.class,
		TQueryRepositoryPeer.class, //overrided doDelete
		TNotifySettingsPeer.class,
		TExportTemplatePeer.class, 
		TScriptsPeer.class,
		TFilterCategoryPeer.class,
		TReportCategoryPeer.class,
		TDashboardScreenPeer.class,
		TWorkflowConnectPeer.class,
		TOrgProjectSLAPeer.class,
		TMailTemplateConfigPeer.class,
		TMailTextBlockPeer.class
		//lastly delete the project itself
		//TProjectPeer.class,
	};
	
	private static String[] deleteFields = {
		TWorkItemPeer.PROJECTKEY,
		TReleasePeer.PROJKEY,
		TProjectCategoryPeer.PROJKEY,
		TClassPeer.PROJKEY,
		TAccessControlListPeer.PROJKEY, 
		TReportLayoutPeer.PROJECT, 
		TProjectAccountPeer.PROJECT,
		TProjectReportRepositoryPeer.PROJECT,
		TVersionControlParameterPeer.PROJECT,
		TFieldConfigPeer.PROJECT,
		TFieldPeer.PROJECT,
		TScreenConfigPeer.PROJECT,
		TScreenPeer.PROJECT,
		TInitStatePeer.PROJECT,
		TEventPeer.PROJECT,
		TQueryRepositoryPeer.PROJECT,
		TNotifySettingsPeer.PROJECT,
		TExportTemplatePeer.PROJECT,
		TScriptsPeer.PROJECT,
		TFilterCategoryPeer.PROJECT,
		TReportCategoryPeer.PROJECT,
		TDashboardScreenPeer.PROJECT,
		TWorkflowConnectPeer.PROJECT,
		TOrgProjectSLAPeer.PROJECT,
		TMailTemplateConfigPeer.PROJECT,
		TMailTextBlockPeer.PROJECT
		//lastly delete the project itself
		//TProjectPeer.PKEY
	};
	
	/**
	 * Loads a project by primary key
	 * @param objectID
	 * @return
	 */
	public TProjectBean loadByPrimaryKey(Integer objectID) {
		TProject tProject = null;
		try {
			tProject = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.debug("Loading of a project by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tProject!=null) {
			return tProject.getBean();
		}
		return null;
	}
	
	/**
	 * Gets an projectBean by label
	 * @param label
	 * @return
	 */
	public TProjectBean loadByLabel(String label) {
		List<TProject> torqueList = null;
		Criteria crit = new Criteria();
		crit.add(LABEL, label);
		try {
			torqueList =  doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading the project by label " + label +  " failed with " + e.getMessage(), e);
			return null;
		}
		if (torqueList==null || torqueList.isEmpty()) {
			return null;
		}
		return ((TProject)torqueList.get(0)).getBean();
	}
	
	/**
	 * Gets a projectBean by prefix
	 * @param prefix
	 * @return
	 */
	public List<TProjectBean> loadByPrefix(String prefix) {
		Criteria crit = new Criteria();
		crit.add(PREFIX, prefix);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the project by prefix " + prefix +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the private project for the person
	 * @param personID
	 * @return
	 */
	public boolean hasPrivateProject(Integer personID) {
		List<TProject> torqueList = null;
		Criteria crit = new Criteria();
		//the negative projectType is for private projects
		crit.add(PROJECTTYPE, 0, Criteria.LESS_THAN);
		crit.add(DEFOWNER, personID);
		try {
			torqueList = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading the private project for person " + personID +  " failed with " + e.getMessage(), e);
			return true;
		}
		return torqueList!=null && !torqueList.isEmpty();
	}

	
	/**
	 * Loads the private project for the person
	 * @param personID
	 * @return
	 */
	public List<TProjectBean> getPrivateProject(Integer personID) {
		Criteria crit = new Criteria();
		//the negative projectType is for private projects
		crit.add(PROJECTTYPE, 0, Criteria.LESS_THAN);
		crit.add(DEFOWNER, personID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading the private project for person " + personID +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Gets the projectBeans by uuid list
	 * @param uuids
	 * @return
	 */
	public List<TProjectBean> loadByUUIDs(List<String> uuids) {
		return loadByFieldValues(uuids, TPUUID);
	}
	
	
	/**
	 * Loads a projectBean list by a project type
	 * @param projectTypeID
	 * @return
	 */
	public List<TProjectBean> loadByProjectType(Integer projectTypeID) {
		Criteria crit = new Criteria();
		crit.add(PROJECTTYPE, projectTypeID);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of projects by project type " + projectTypeID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Count the number of projects
	 * @param all: if true count all projects (mainProjects parameter has no importance) 
	 * @param mainProjects: if all is false whether to count the main- or the sub-projects
	 * @return
	 */	
	public int count(boolean all, boolean mainProjects) {
		String COUNT = "count(" + PKEY + ")";
		Criteria crit = new Criteria();
		if (!all) {
			if (mainProjects) {
				crit.add(PARENT, (Object)null, Criteria.ISNULL);
			} else {
				crit.add(PARENT, (Object)null, Criteria.ISNOTNULL);
			}
		}
		crit.addSelectColumn(COUNT);
		try {
			return ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asInt();
		} catch (Exception e) {
			LOGGER.error("Counting the projects all " + all + " mainProjects " + mainProjects +  " failed with " + e.getMessage(), e);
			return 0;
		}
	} 
	
	/**
	 * Gets the projectBeans by label list
	 * @param labels
	 * @return
	 */
	public List<TProjectBean> loadByLabels(List<String> labels) {
		return loadByFieldValues(labels, LABEL);
	}	
	
	/**
	 * Load the projects by a String field
	 * @param fieldValues
	 * @param fieldName
	 * @return
	 */
	private List<TProjectBean> loadByFieldValues(List<String> fieldValues, String fieldName) {
		if (fieldValues==null || fieldValues.isEmpty()) {
			return new LinkedList<TProjectBean>();
		}
		List<TProject> projectBeanList = new LinkedList<TProject>();
		List<List<String>> chunksList = GeneralUtils.getListOfStringChunks(fieldValues);
		if (chunksList==null) {
			return new LinkedList<TProjectBean>();
		}
		Iterator<List<String>> iterator = chunksList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			List<String> chunk = iterator.next();
			Criteria criteria = new Criteria();
			criteria.addIn(fieldName, chunk);
			try {
				projectBeanList.addAll(doSelect(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the projectBeans by " + fieldName + " and the chunk number " + 
						i + " of length  "  + chunk.size() + " failed with " + e.getMessage(), e);
			}
		}
		return convertTorqueListToBeanList(projectBeanList);
	}
	
	/**
	 * Loads a ProjectBean list by projectIDs
	 * @param projectIDs
	 * @return
	 */
	public List<TProjectBean> loadByProjectIDs(List<Integer> projectIDs) {
		if (projectIDs==null || projectIDs.isEmpty()) {
			LOGGER.info("No projectIDs specified " + projectIDs);
			return new LinkedList<TProjectBean>();
		}
		Criteria crit = new Criteria();
		crit.addIn(PKEY, projectIDs);
		crit.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of projects by IDs failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads all projects
	 * @return
	 */
	public List<TProjectBean> loadAll() {
		List<TProject> torqueList = null;
		Criteria crit = new Criteria();		
		crit.addAscendingOrderByColumn(LABEL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		try {
			torqueList =  doSelect(crit);
		}
		catch (Exception e) {
			LOGGER.error("Loading of all projects failed with " + e.getMessage(), e);
			return null;
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	
	
	/**
	 * Loads all main projects (those without parent project)
	 * @return
	 */
	public List<TProjectBean> loadAllMainProjects() {
		Criteria crit = new Criteria();		
		crit.addAscendingOrderByColumn(LABEL);
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		crit.add(PARENT, (Object)null, Criteria.ISNULL);
		crit.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of all main projects failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the main (no child) projects with specific states 
	 * @param states
	 * @return
	 */
	public List<TProjectBean> loadMainProjectsByStates(int[] states) {
		Criteria crit = new Criteria();
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		crit.add(PARENT, (Object)null, Criteria.ISNULL);
		if (states!=null && states.length>0) {
			crit.addIn(STATUS, states);
		}
		crit.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			if (states != null) {
				LOGGER.error("Loading the main projects with states " + states.length + " failed with " + e.getMessage(), e);
			}
			return null;
		}
	}
	
	
	/**
	 * Loads all subprojects for a project
	 * @param projectID
	 * @return
	 */
	public List<TProjectBean> loadSubrojects(Integer projectID) {
		Criteria crit = new Criteria();		
		crit.addAscendingOrderByColumn(LABEL);
		crit.add(PARENT, projectID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of all subprojects of the project " + projectID +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads the subprojects with specific states for parent projects
	 * @param projectID
	 * @param states
	 * @return
	 */
	public List<TProjectBean> loadSubprojectsByParentsAndStates(List<Integer> projectIDs, int[] states) {
		List<TProjectBean> projectBeans = new LinkedList<TProjectBean>();
		if (projectIDs==null || projectIDs.isEmpty()) {
			return projectBeans;
		}			
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(projectIDs);
		if (projectIDChunksList==null) {
			return projectBeans;
		}
		Iterator<int[]> iterator = projectIDChunksList.iterator();
		while (iterator.hasNext()) {
			Criteria criteria = new Criteria();
			int[] projectIDChunk = iterator.next();
			if (states!=null && states.length>0) {
				criteria.addIn(STATUS, states);
			}	
			criteria.addIn(PARENT, projectIDChunk);
			criteria.addAscendingOrderByColumn(LABEL);
			try {
				projectBeans.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the projectBeans by parent IDs " + projectIDs.size() + " and states " + states + "  failed with " + e.getMessage(), e);
			}
		}
		return projectBeans;
	}
	
	
	/**
	 * Loads the subprojects with specific states for a parent project
	 * @param projectID
	 * @param states
	 * @return
	 */
	public List<TProjectBean> loadSubprojectsByParentAndStates(Integer projectID, int[] states) {
		Criteria crit = new Criteria();
		crit.add(PARENT, projectID);
		if (states!=null && states.length>0) {
			crit.addIn(STATUS, states);
		}	
		crit.addAscendingOrderByColumn(LABEL);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (Exception e) {
			LOGGER.error("Loading of all subprojects of the project " + projectID +  " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Load all ProjectBeans except those closed
	 * Should be called only be system admin
	 * @return
	 */
	public List<TProjectBean> loadProjectsByStateIDs(int[] stateIDs) {
		Criteria crit = new Criteria();
		//states only when specified
		if (stateIDs != null && stateIDs.length>0) {
			crit.addIn(STATUS, stateIDs);
		}
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		crit.addAscendingOrderByColumn(LABEL);
		try { 
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading the active and inactive projects failed with " + e.getMessage(), e);
			return null;
		}
		
	}
	
	/**
	 * Whether this project name already exists on the same level 
	 * @param name
	 * @param parentProject
	 * @param exceptProjectID
	 * @param nonPrivate verify only the non private projects
	 * @return
	 */
	public boolean projectNameExists(String name, Integer parentProjectID, Integer exceptProjectID, boolean nonPrivate) {
		List<TProject> torqueList = null;
		Criteria crit = new Criteria();
		crit.add(LABEL, name);
		if (parentProjectID==null) {
			crit.add(PARENT, (Object)null, Criteria.EQUAL);
		} else {
			crit.add(PARENT, parentProjectID, Criteria.EQUAL);
		}
		if (exceptProjectID!=null) {
			crit.add(PKEY, exceptProjectID, Criteria.NOT_EQUAL);
		}
		if (nonPrivate) {
			crit.addJoin(PROJECTTYPE, TProjectTypePeer.OBJECTID);
			crit.add(TProjectTypePeer.DEFAULTFORPRIVATE, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		}
		try {
			torqueList = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading the projects by name " + name + " and parentProject " + parentProjectID + " failed with " + e.getMessage(), e);
		}
		return torqueList!=null && !torqueList.isEmpty();
	}
	
	/**
	 * Whether this project name already exists 
	 * @param prefix
	 * @param parentProject
	 * @param exceptProjectID
	 * @param nonPrivate verify only the non private projects
	 * @return
	 */
	public boolean projectPrefixExists(String prefix, Integer parentProjectID, Integer exceptProjectID, boolean nonPrivate) {
		List<TProject> torqueList = null;
		Criteria crit = new Criteria();
		crit.add(PREFIX, prefix);
//		if (parentProjectID==null) {
//			crit.add(PARENT, (Object)null, Criteria.EQUAL);
//		} else {
//			crit.add(PARENT, parentProjectID, Criteria.EQUAL);
//		}
		if (exceptProjectID!=null) {
			crit.add(PKEY, exceptProjectID, Criteria.NOT_EQUAL);
		}
		if (nonPrivate) {
			crit.addJoin(PROJECTTYPE, TProjectTypePeer.OBJECTID);
			crit.add(TProjectTypePeer.DEFAULTFORPRIVATE, (Object)BooleanFields.TRUE_VALUE, Criteria.NOT_EQUAL);
		}
		try {
			torqueList = doSelect(crit);
		} catch (Exception e) {
			LOGGER.error("Loading the projects by prefix " + prefix + " failed with " + e.getMessage(), e);
		}
		return torqueList!=null && !torqueList.isEmpty();
	}
	
	/**
	 * Loads a ProjectBean list by workItemKeys
	 * @param workItemKeys
	 * @return
	 */
	public List<TProjectBean> loadByWorkItemKeys(int[] workItemIDs) {
		List<TProject> projects = new ArrayList<TProject>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return convertTorqueListToBeanList(projects);
		}			
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return convertTorqueListToBeanList(projects);
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			Criteria criteria = new Criteria();
			criteria.addJoin(BaseTWorkItemPeer.PROJECTKEY, PKEY);
			criteria.addIn(BaseTWorkItemPeer.WORKITEMKEY, workItemIDChunk);
			criteria.setDistinct();
			try {
				projects.addAll(doSelect(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the projectBeans by workItemKeys failed with " + e.getMessage(), e);
			}
		}
		//remove duplicate projects
		Set<Integer> projectIDs = new HashSet<Integer>();
		for (Iterator<TProject> itrProject = projects.iterator(); itrProject.hasNext();) {
			TProject tProject = itrProject.next();
			Integer projectID = tProject.getObjectID();
			if (projectIDs.contains(projectID)) {
				itrProject.remove();
			} else {
				projectIDs.add(projectID);
			}
		}
		return convertTorqueListToBeanList(projects);
	}
	
	/**
	 * Loads a ProjectBean by workItemID
	 * @param workItemIDs
	 * @return
	 */
	public TProjectBean loadByWorkItemKey(Integer workItemID) {
		List projects = null;
		Criteria criteria = new Criteria();
		criteria.addJoin(BaseTWorkItemPeer.PROJECTKEY, PKEY);
		criteria.add(BaseTWorkItemPeer.WORKITEMKEY, workItemID);
		try {
			projects = doSelect(criteria);
		} catch(Exception e) {
			LOGGER.error("Loading the projectBean by workItemID failed with " + e.getMessage(), e);
		}
		if (projects==null || projects.isEmpty()) {
			return null;
		}
		return ((TProject)projects.get(0)).getBean();
	
	}
	
	/**
	 * Load by users and right and state independently of the project level (main or subproject)
	 * @param personIDs
	 * @param right
	 * @param states
	 * @return
	 */
	public List<TProjectBean> loadByUserAndRightAndState(List<Integer> personIDs, int[] right, int[] states) {
		if (personIDs==null || personIDs.isEmpty()) {
			return new LinkedList<TProjectBean>();
		}
		Criteria crit = new Criteria();
 		crit.addJoin(TAccessControlListPeer.PROJKEY, PKEY);
		TAccessControlListPeer.addExtendedAccessKeyCriteria(crit, right);
		crit.addIn(TAccessControlListPeer.PERSONKEY, personIDs);
		//states only when specified
		if (states!=null && states.length>0) {
			crit.addIn(STATUS, states);
		}
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		crit.setDistinct();
		crit.addAscendingOrderByColumn(LABEL);
		try { 
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading the projects by " + personIDs.size() + " user and right " + right + " and states failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load subprojects by persons, parent project, explicit right and states
	 * @param personIDs
	 * @param parentProjectID
	 * @param right
	 * @param states
	 * @return
	 */
	public List<TProjectBean> loadSubprojectsByUserAndRightAndState( 
			List<Integer> personIDs, Integer parentProjectID, int[] right, int[] states) {
		Criteria crit = new Criteria();
 		crit.addJoin(TAccessControlListPeer.PROJKEY, PKEY);
		TAccessControlListPeer.addExtendedAccessKeyCriteria(crit, right);
		crit.addIn(TAccessControlListPeer.PERSONKEY, personIDs);
		//states only when specified
		if (states!=null && states.length>0) {
			crit.addIn(STATUS, states);
		}
		crit.add(PKEY, 0, Criteria.GREATER_THAN);
		crit.add(PARENT, parentProjectID);
		crit.setDistinct();		
		crit.addAscendingOrderByColumn(LABEL);
		try { 
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(Exception e) {
			LOGGER.error("Loading the subprojects by parent project " + parentProjectID + 
					", " + personIDs.size() + " users, right " + right[0] + " and states failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Load the distinct projects containing issues having the field set 
	 * @param fieldID
	 * @return
	 */
	public List<TProjectBean> getProjectsWithField(Integer fieldID) {
		Criteria crit = new Criteria();
		crit.add(TAttributeValuePeer.FIELDKEY, fieldID);
		crit.addJoin(TAttributeValuePeer.WORKITEM, TWorkItemPeer.WORKITEMKEY);
		crit.addJoin(TWorkItemPeer.PROJECTKEY, TProjectPeer.PKEY);
		crit.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Getting projects with field " + 
					fieldID + " failed with: " + e);
			return null;
		}
	}
	

	/**
	 * Loads a list of projectBeans which has notificationSettings 
	 * (set either explicitely by a person or inherited the default)  
	 * @param personID
	 * @return
	 */
	public List<TProjectBean> loadByOwnOrDefaultNotifySettings(Integer personID) {
		Criteria criteria = new Criteria();
		criteria.addJoin(PKEY, BaseTNotifySettingsPeer.PROJECT);
		Criterion personCrit = criteria.getNewCriterion(BaseTNotifySettingsPeer.PERSON, personID, Criteria.EQUAL);
		Criterion defaultCrit = criteria.getNewCriterion(BaseTNotifySettingsPeer.PERSON, (Object)null, Criteria.ISNULL);
		criteria.add(personCrit.or(defaultCrit));
		//if a project has default and own notifiy settings then take only one time 
		criteria.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of projects with notify assignments either as default or by person " + personID + " failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Loads a list of projectBeans which has default notificationSettings set
	 * @return
	 */
	public List<TProjectBean> loadByDefaultNotifySettings() {
		Criteria criteria = new Criteria();
		criteria.addJoin(PKEY, BaseTNotifySettingsPeer.PROJECT);
		criteria.add(BaseTNotifySettingsPeer.PERSON, (Object)null, Criteria.ISNULL);
		criteria.setDistinct();
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading of the projects having default notify assignments failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Saves a projectBean in the TProject table
	 * @param projectBean
	 * @return
	 */
	public Integer save(TProjectBean projectBean) {
		try {
			TProject tProject = BaseTProject.createTProject(projectBean);
			tProject.save();
			return tProject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Simple saving of a project failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes the project dependencies
	 * @param projectID
	 * @return
	 */
	public void deleteDependencies(Integer projectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, projectID);
	}
	
	/**
	 * @param projectID primary key of a project
	 * @throws Exception
	 */
	public void delete(Integer projectID) {
		LOGGER.fatal("Project with ID " +  projectID + " is deleted...");
		Criteria  criteria = new Criteria();
		criteria.add(PKEY, projectID);
		try {
			doDelete(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the project " + projectID + " failed with " + e.getMessage(), e);
		}
	}	
	
	
	
	/**
	 * Helper for deep and shallow copy 
	 * @param workItemBean
	 * @param deep
	 * @return
	 */
	public TProjectBean copy(TProjectBean projectOriginal, boolean deep) {
		TProjectBean tProjectCopy = null;
		if (projectOriginal!=null) {
			try {
				tProjectCopy = (BaseTProject.createTProject(projectOriginal).copy(deep)).getBean();
			} catch (TorqueException e) {
				String copyMode;
				if (deep) {
					copyMode = "Deep";
				} else {
					copyMode = "Shallow";
				}
				LOGGER.error(copyMode + " copy failed with " + e.getMessage(), e);
			}
		}
		return tProjectCopy;
	}
	
	/**
	 * Filter the projects from the projectIDs having specific projectStates
	 * @param projectIDs
	 * @param projectStates
	 * @return
	 */
	public Set<Integer> filterProjectsIDsByStates(Set<Integer> projectIDs, int[] projectStates) {
		Set<Integer> filteredProjectIDs = new HashSet<Integer>();
		if (projectIDs==null || projectIDs.isEmpty()) {
			return filteredProjectIDs;
		}			
		List<int[]> projectIDChunksList = GeneralUtils.getListOfChunks(GeneralUtils.createIntegerListFromCollection(projectIDs));
		if (projectIDChunksList==null) {
			return filteredProjectIDs;
		}
		Iterator<int[]> iterator = projectIDChunksList.iterator();
		while (iterator.hasNext()) {
			Criteria criteria = new Criteria();
			int[] projectIDChunk = iterator.next();
			if (projectStates!=null && projectStates.length>0) {
				criteria.addIn(STATUS, projectStates);
			}	
			criteria.addIn(PKEY, projectIDChunk);
			try {
				filteredProjectIDs.addAll(getProjectIDs(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the projectBeans by projectIDs " + projectIDs.size() + " and states " + projectStates + "  failed with " + e.getMessage(), e);
			}
		}
		return filteredProjectIDs;
	}
	
	/**
	 * Gets the projectIDs from resultset
	 * @param criteria
	 * @param raciRoles
	 * @return
	 */
	private static Set<Integer> getProjectIDs(Criteria criteria) {
		Set<Integer> projectIDs = new HashSet<Integer>();
		try {
			criteria.addSelectColumn(PKEY);
			criteria.setDistinct();
			List<Record> projectIDRecords  = doSelectVillageRecords(criteria);
			if (projectIDRecords!=null) {
				for (Record record : projectIDRecords) {
					try {
						Value value =  record.getValue(1);
						if (value!=null) {
							Integer projectID = value.asIntegerObj();
							if (projectID!=null) {
								projectIDs.add(projectID);
							}
						}
					} catch (DataSetException e) {
						LOGGER.error("Getting the projectID failed with " + e.getMessage(), e);
					}
				}
			}
		} catch (TorqueException e) {
			LOGGER.error("Loading of projectIDs failed with " + e.getMessage(), e);
		}
		return projectIDs;
	}
	
	/*********************************************************
	* Manager-, Responsible-, My- and Custom Reports methods * 
	*********************************************************/
	
	/**
	 * Gets the projectBeans for a prepared criteria
	 * @param preparedCriteria
	 * @return
	 * @throws TorqueException
	 */
	private static List<TProjectBean> getReportProjects(Criteria preparedCriteria) throws TorqueException {
		return convertTorqueListToBeanList(doSelect(preparedCriteria));
	}
	
	/**
	 * Get the IDs of the closed projects form projectIDs
	 * @param projectIDs
	 * @return
	 */
	public Set<Integer> getClosedProjectIDs(List<Integer> projectIDs) {
		Set<Integer> closedProjectIDs = new HashSet<Integer>();
		if (projectIDs==null || projectIDs.isEmpty()) {
			return closedProjectIDs;
		}	
		List projects = new ArrayList();
		Criteria crit = new Criteria();
		crit.addJoin(BaseTProjectPeer.STATUS, BaseTSystemStatePeer.OBJECTID);
		crit.addIn(BaseTProjectPeer.PKEY, projectIDs);
		crit.add(BaseTSystemStatePeer.STATEFLAG, TSystemStateBean.STATEFLAGS.CLOSED);
		try {
			projects = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Filtering the closed projects failed with " + e.getMessage(), e);
		}
		Iterator iterator = projects.iterator();
		while (iterator.hasNext()) {
			TProject tProject = (TProject)iterator.next();
			closedProjectIDs.add(tProject.getObjectID());
		}		
		return closedProjectIDs;
	}
	
	
	private static List<TProjectBean> convertTorqueListToBeanList(List<TProject> torqueList) {
		List<TProjectBean> beanList = new ArrayList<TProjectBean>();
		if (torqueList!=null){
			Iterator<TProject> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}

	/**
	 * Get the projectBeans from the history of the workItemIDs, a project might appear more times in this list!
	 * @param workItemIDs
	 * @return
	 */
	public List<TProjectBean> loadHistoryProjects(int[] workItemIDs) {
		List<TProjectBean> projectBeanList = new ArrayList<TProjectBean>();
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList!=null && !workItemIDChunksList.isEmpty()) {
			Iterator<int[]> iterator = workItemIDChunksList.iterator();
			while (iterator.hasNext()) {
				int[] workItemIDChunk = iterator.next();
				Criteria criteria = HistoryDropdownContainerLoader.prepareHistorySystemOptionCriteria(
						workItemIDChunk, true, PKEY, SystemFields.PROJECT);
				try {
					projectBeanList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Loading the new history projectBeans  for workItems and personID failed with " + e.getMessage(), e);
				}
				criteria = HistoryDropdownContainerLoader.prepareHistorySystemOptionCriteria(
						workItemIDChunk, false, PKEY, SystemFields.PROJECT);
				try {
					projectBeanList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
				} catch(Exception e) {
					LOGGER.error("Loading the old history projectBeans for workItems failed with " + e.getMessage(), e);
				}
			}
		}
		return projectBeanList;
	}
}
