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


package com.aurel.track.admin.customize.notify.settings;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.CategoryBL.CATEGORY_TYPE;
import com.aurel.track.admin.customize.category.CategoryPickerBL;
import com.aurel.track.admin.customize.notify.trigger.NotifyTriggerBL;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TNotifySettingsBean;
import com.aurel.track.beans.TNotifyTriggerBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.NotifySettingsDAO;
import com.aurel.track.dao.NotifyTriggerDAO;
import com.aurel.track.dao.ProjectDAO;
import com.aurel.track.dao.QueryRepositoryDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

/**
 * Business logic class for notification settings
 * @author Tamas Ruff
 *
 */
public class NotifySettingsBL {
	
	
	private static NotifySettingsDAO notifySettingsDAO = DAOFactory.getFactory().getNotifySettingsDAO();
	private static ProjectDAO projectDAO = DAOFactory.getFactory().getProjectDAO();
	private static NotifyTriggerDAO notifyTriggerDAO = DAOFactory.getFactory().getNotifyTriggerDAO();
	private static QueryRepositoryDAO queryRepositoryDAO = DAOFactory.getFactory().getQueryRepositoryDAO();	
	//the ID of the Generic project which is used for "Other Projects" 
	//to preserve the referential integrity at db level
	public static Integer OTHERPROJECTSID = Integer.valueOf(0);
	
	/**
	 * Defines symbolic constants for the raci roles defined in the TNotifyField table 
	 * @author Tamas Ruff
	 *
	 */
	public static interface REMOVE_OPTION {		
		public static final int REMOVE_FROM_AFFECTED = 1;
		public static final int REMOVE_AFFECTED = 2;
		public static final int REPLACE_IN_AFFECTED = 3;
	}
	
	/**
	 * Loads a list of notifySettingsTO visible to the personID 
	 * @param personBean
  	 * @param defaultSettings
  	 * @param projectID not null when called from project configuration 
  	 * @param locale
	 * @return
	 */	
	static List<NotifySettingsTO> createNotifySettingsTOs(TPersonBean personBean, boolean defaultSettings, Integer projectID, Locale locale) {
		List<NotifySettingsTO> notifySettingsTOList = new LinkedList<NotifySettingsTO>();
		List<TNotifySettingsBean> notifySettingsList = loadNotifySettingsBeans(personBean, defaultSettings, projectID);
		Map<Integer, TProjectBean> projectBeansMap =
				NotifySettingsBL.getConfiguredProjectsMap(personBean.getObjectID(), defaultSettings, locale);
		Map<Integer, TNotifyTriggerBean> notifyTriggerBeansMap =
				NotifySettingsBL.loadTriggersByNotifySettings(personBean.getObjectID(), defaultSettings);
		Map<Integer, TQueryRepositoryBean> notifyFilterBeansMap =
				NotifySettingsBL.loadFiltersByNotifySettings(personBean.getObjectID(), defaultSettings);
		for (TNotifySettingsBean notifySettingsBean : notifySettingsList) {
			NotifySettingsTO notifySettingsTO = new NotifySettingsTO(notifySettingsBean.getObjectID());
			if (notifySettingsBean.getPerson()==null && !defaultSettings) {
				notifySettingsTO.setInherited(true);
			}
			TProjectBean projectBean = projectBeansMap.get(notifySettingsBean.getProject());
			if (projectBean!=null) {
				notifySettingsTO.setProjectID(projectBean.getObjectID());
				notifySettingsTO.setProjectLabel(projectBean.getLabel());
			}
			TNotifyTriggerBean notifyTriggerBean = notifyTriggerBeansMap.get(notifySettingsBean.getNotifyTrigger());
			if (notifyTriggerBean!=null) {
				notifySettingsTO.setTriggerLabel(notifyTriggerBean.getLabel());
			}
			TQueryRepositoryBean queryRepositoryBean = notifyFilterBeansMap.get(notifySettingsBean.getNotifyFilter());
			if (queryRepositoryBean!=null) {
				notifySettingsTO.setFilterLabel(queryRepositoryBean.getLabel());
			}
			notifySettingsTOList.add(notifySettingsTO);
		}
		return notifySettingsTOList;
	}
	
	/**
	 * Loads a list of notifySettingsBean visible to the personID 
	 * @param personBean
	 * @param isDefault
	 * @param projectID
	 * @return
	 */
	private static List<TNotifySettingsBean> loadNotifySettingsBeans(TPersonBean personBean, boolean isDefault, Integer projectID) {
		if (isDefault) {
			if (projectID==null) {
				//all default configurations
				if (personBean.isSys()) {
					return notifySettingsDAO.loadAllDefaultAssignments();
				} else {
					List<TProjectBean> adminProjects = ProjectBL.getAllNotClosedAdminProjectBeansFlat(personBean, true);
					return loadDefaultsByProjects(GeneralUtils.createIntegerListFromBeanList(adminProjects));
				}
			} else {
				//project specific default assignment by project configuration 
				List<TNotifySettingsBean> notifySettingsList = new LinkedList<TNotifySettingsBean>();
				TNotifySettingsBean notifySettingsBean = loadDefaultByProject(projectID);
				if (notifySettingsBean!=null) {
					notifySettingsList.add(notifySettingsBean);
				}
				return notifySettingsList;
			}
		} else {
			return loadOwnAndDefaultNotifySettingsBeans(personBean.getObjectID());
		}
	}
	
	/**
	 * Load notifySettingsBeans for a person: the explicitly set by the person and the defaults
	 * If for one project there is both own and default then show only the own
	 * @param personID
	 * @return
	 */
	private static List<TNotifySettingsBean> loadOwnAndDefaultNotifySettingsBeans(Integer personID) {
		List<TNotifySettingsBean> ownSettings = notifySettingsDAO.loadOwnSettings(personID);
		Set<Integer> ownProjectsSet = new HashSet<Integer>();
		if (ownSettings!=null && !ownSettings.isEmpty()) {
			for (TNotifySettingsBean notifySettingsBean : ownSettings) {
				if (notifySettingsBean.getPerson()!=null) {
					ownProjectsSet.add(notifySettingsBean.getProject());
				}
			}
		}
		List<TProjectBean> readableProjectBean = ProjectBL.loadActiveProjectsWithReadIssueRight(personID);
		List<Integer> readableProjectIDs = GeneralUtils.createIntegerListFromBeanList(readableProjectBean);
		readableProjectIDs.add(OTHERPROJECTSID);
		readableProjectIDs.removeAll(ownProjectsSet);
		List<TNotifySettingsBean> defaultProjectSettings = loadDefaultsByProjects(readableProjectIDs);
		ownSettings.addAll(defaultProjectSettings);
		return ownSettings;
	}
	
	/**
	 * Saves a new/modified/overwritten notification setting
	 * @param notifySettingsID
	 * @param defaultSettings
	 * @param project
	 * @param trigger
	 * @param filter
	 * @param personID
	 */
	static Integer save(Integer notifySettingsID, boolean defaultSettings, 
			Integer project, Integer trigger, Integer filter, Integer personID) {
		TNotifySettingsBean notifySettingsBean = null;
		if (notifySettingsID != null) {
			//edit or overwrite
			notifySettingsBean = NotifySettingsBL.loadNotifySettingsBeanByPrimaryKey(notifySettingsID);
			if (!defaultSettings && notifySettingsBean!=null && notifySettingsBean.getPerson()==null) {
				//overwrite exiting default: create a new settings bean
				notifySettingsBean = new TNotifySettingsBean();
			}
		} else {
			//add new: create a new settings bean
			notifySettingsBean = new TNotifySettingsBean();
		}
		notifySettingsBean.setProject(project);
		notifySettingsBean.setNotifyTrigger(trigger);
		notifySettingsBean.setNotifyFilter(filter);
		if (!defaultSettings) {
			notifySettingsBean.setPerson(personID);
		}
		return NotifySettingsBL.saveNotifySettingsBean(notifySettingsBean);
	}
	
	/**
	 * Loads a notifySettingsBean by a primary key
	 * @param objectID
	 * @return
	 */
	public static TNotifySettingsBean loadNotifySettingsBeanByPrimaryKey(Integer objectID) {
		return notifySettingsDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Get all notifySettingsBean for a project
	 * @param projectID
	 * @return
	 */
	public static List<TNotifySettingsBean> loadAllByProject(Integer projectID) {
		return notifySettingsDAO.loadAllByProject(projectID);
	}
	
	/**
	 * Loads the default settings for projects
	 * @param projectIDs
	 * @return
	 */
	public static List<TNotifySettingsBean> loadDefaultsByProjects(List<Integer> projectIDs) {
		return notifySettingsDAO.loadDefaultsByProjects(projectIDs);
	}
	
	/**
	 * Get the default notifySettingsBean for a project (person==null)
	 * @param projectID
	 * @return
	 */
	public static TNotifySettingsBean loadDefaultByProject(Integer projectID) {
		return notifySettingsDAO.loadDefaultByProject(projectID);
	}
	
	/**
	 * Get the notifySettingsBean defined by a person for a project 
	 * @param personID
	 * @param projectID
	 * @return
	 */
	public static TNotifySettingsBean loadOwnByPersonAndProject(Integer personID, Integer projectID) {
		return notifySettingsDAO.loadOwnByPersonAndProject(personID, projectID);
	}
	
	/**
	 * Saves a notifySettingsBean
	 * @param notifySettingsBean
	 */
	public static Integer saveNotifySettingsBean(TNotifySettingsBean notifySettingsBean) {
		return notifySettingsDAO.save(notifySettingsBean);
	}

	/**
	 * Deletes a notifySettingsBean
	 * @param objectID
	 */
	public static void deleteNotifySettingsByPrimaryKey(Integer objectID) {
		notifySettingsDAO.delete(objectID);
	}
	
	/**
	 * Verifies whether the person has right to delete the assignment
	 * @param objectID
	 */
	public static boolean deleteNotifySettingsAllowed(Integer objectID, TPersonBean personBean, boolean defaultSettings) {
		if (personBean.isSys()) {
			return true;
		} else {
			if (defaultSettings) {
				TNotifySettingsBean notifySettingsBean = notifySettingsDAO.loadByPrimaryKey(objectID);
				if (notifySettingsBean!=null) {
					return PersonBL.isProjectAdmin(personBean.getObjectID(), notifySettingsBean.getProject());
				} else {
					//already deleted?
					return true;
				}
			} else {
				//delete own settings
				return true;
			}
		}
	}
	
	/**
	 * Get a map of TProjectBeans with default configured projects 
	 * @return map 
	 * 	- key: projectID
	 * 	- value: TProjectBean 
	 */
	public static Map<Integer, TProjectBean> getConfiguredProjectsMap(Integer person, boolean isDefault, Locale locale) {
		List<TProjectBean> projectBeansList;
		if (isDefault) {
			projectBeansList = projectDAO.loadByDefaultNotifySettings();
		} else {
			projectBeansList = projectDAO.loadByOwnOrDefaultNotifySettings(person);
		}
		Map<Integer, TProjectBean> projectBeansMap = GeneralUtils.createMapFromList(projectBeansList);
		//Overwrite the "Generic project" label to "Other projects" for the project lookup in jsp 
		TProjectBean projectBean = projectBeansMap.get(OTHERPROJECTSID);
		if (projectBean!=null) {
			projectBean.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.assignments.lbl.otherProjects", locale));
		}
		return projectBeansMap;
	}
	
	/**
	 * Creates the JSON string for editing/adding/deriving a notification setting
	 * @param notifySettingsID
     * @param exclusiveProjectID
	 * @param personBean
	 * @param defaultSettings
	 * @param locale
	 * @return
	 */
	static String createNotifySettingsEditJSON(Integer notifySettingsID,  Integer exclusiveProjectID,
            TPersonBean personBean, boolean defaultSettings, Locale locale) {
		Integer personID = personBean.getObjectID();
		Integer actualProjectID = null;
		Integer actualTriggerID = null;
		Integer actualFilterID = null;
		Integer id = null;
		if (notifySettingsID!=null) {
			TNotifySettingsBean notifySettingsBean = NotifySettingsBL.loadNotifySettingsBeanByPrimaryKey(notifySettingsID);
			if (notifySettingsBean!=null) {
				id = notifySettingsBean.getObjectID();
				actualProjectID = notifySettingsBean.getProject();
				actualTriggerID = notifySettingsBean.getNotifyTrigger();
				actualFilterID = notifySettingsBean.getNotifyFilter();
			}
		}
		List<TNotifyTriggerBean> triggers = NotifyTriggerBL.loadNotifyTriggerBeans(personID, !defaultSettings);
        List<TreeNode> projectTree = getNotConfiguredProjects(
                personBean, notifySettingsID, defaultSettings, exclusiveProjectID, locale);
        if (exclusiveProjectID==null && actualProjectID!=null) {
        	//filter according to the selected project
        	exclusiveProjectID = actualProjectID;
        }
        List<TreeNode> filterTree = CategoryPickerBL.getPickerNodesEager(
                personBean, defaultSettings, false, null, false, exclusiveProjectID, null, locale, CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY);
		return NotifySettingsJSON.createNotifySettingsEditJSON(id, actualProjectID, projectTree, actualTriggerID, triggers, actualFilterID, filterTree);
	}
	
	/**
	 * Get a list of TProjectBeans with not yet configured projects
	 * @param personBean
	 * @param notifySettingsID by edit include this project also, by add it is null
	 * @param isDefault
     * @param exclusiveProjectID
	 * @param locale
	 * @return
	 */
	private static List<TreeNode> getNotConfiguredProjects(TPersonBean personBean, Integer notifySettingsID, 
			boolean isDefault, Integer exclusiveProjectID, Locale locale) {
		Integer actualProjectID = null;
		if (notifySettingsID!=null) {
			TNotifySettingsBean notifySettingsBean = NotifySettingsBL.loadNotifySettingsBeanByPrimaryKey(notifySettingsID);
			if (notifySettingsBean!=null) {
				actualProjectID = notifySettingsBean.getProject();
			}
		}
		if (exclusiveProjectID!=null) {
			List<TreeNode> projectNodes = new LinkedList<TreeNode>();
			TProjectBean projectBean = LookupContainer.getProjectBean(exclusiveProjectID);
			if (projectBean!=null) {
				Integer projectState = projectBean.getStatus();
				projectNodes.add(0, ProjectBL.createProjectTreeNode(projectBean.getObjectID().toString(),
						projectBean.getLabel(), ProjectBL.getProjectIcon(SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE, projectState)), null, true));
			}
			return projectNodes;
		}
		boolean isSysAdmin = personBean.isSys();
		//get the already configured projects
		Set<Integer> configuredProjects = new HashSet<Integer>();
		//get all projects which has notifySettings set
		List<TNotifySettingsBean> notifySettings = loadNotifySettingsBeans(personBean, isDefault, exclusiveProjectID);
		Iterator<TNotifySettingsBean> itrSettings = notifySettings.iterator();
		while (itrSettings.hasNext()) {
			TNotifySettingsBean notifySettingsBean = itrSettings.next();
			configuredProjects.add(notifySettingsBean.getProject());
		}		
		//get all available projects depending on isDefault
		List<TreeNode> treeNodes = null;
		if (isDefault) {
			treeNodes = ProjectBL.getProjectNodesByRightEager(false, personBean, true,
					new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN}, false, false);
		} else {
			treeNodes = ProjectBL.getProjectNodesByRightEager(false, personBean, false,
					new int[] {AccessBeans.AccessFlagIndexes.PROJECTADMIN,
						AccessBeans.AccessFlagIndexes.READANYTASK}, false, false);
		}
		removeSelectedOrSetNotSelectable(treeNodes, configuredProjects, actualProjectID); 	
		//the Other projects is not in the list of available projects so we have to add it explicitly if
		//((it is for own setting) or (the current user is system admin)) and ((not yet set) or (already set but currently edited)) 
		if ((!configuredProjects.contains(OTHERPROJECTSID) || OTHERPROJECTSID.equals(actualProjectID)) && (!isDefault || isSysAdmin)) {
			//add the other projects 
			treeNodes.add(0, createTreeNodeForOtherProjects(locale));
		}
		boolean currentFound = currentIsFound(treeNodes, actualProjectID);
		// a project which has notification settings but was closed afterwards is not contained
		// in the availableProjects. It should be added because the actual value should be always present
		if (actualProjectID!=null && !currentFound && !actualProjectID.equals(OTHERPROJECTSID)) {
			TProjectBean projectBean = LookupContainer.getProjectBean(actualProjectID);
			if (projectBean!=null) {
				treeNodes.add(0, ProjectBL.createProjectTreeNode(projectBean.getObjectID().toString(),
					projectBean.getLabel(), ProjectBL.PROJECT_CLOSED_ICON_CLASS, null, false));
			}
		}
		return treeNodes;
	}
	
	private static boolean currentIsFound(List<TreeNode> treeNodes, Integer actualProjectID) {
		if (actualProjectID==null) {
			//return true to not add forcibly
			return true;
		}
		boolean currentFound = false;
		for (TreeNode treeNode : treeNodes) {
			Integer projectID = null;
			if (treeNode.getId()!=null) {
				projectID = Integer.valueOf(treeNode.getId());
				if (projectID!=null && projectID.equals(actualProjectID)) {
					currentFound = true;
				}
				List<TreeNode> childenNodes = treeNode.getChildren();
				if (childenNodes!=null && !childenNodes.isEmpty()) {
					currentFound = currentFound || currentIsFound(childenNodes, actualProjectID);
				}
			}
		}
		return currentFound;
	}
	
	/**
	 * Remove the already selected projects or set them not selectable (by iconCls) if they have not yet assigned children
	 * @param treeNodes 
	 * @param configuredProjects the projects already configured
	 * @param actualProjectID the actual project in the assignment
	 * @return
	 */
	private static boolean removeSelectedOrSetNotSelectable(List<TreeNode> treeNodes, Set<Integer> configuredProjects, Integer actualProjectID) {				
		boolean removeTreeNode = false;
		for (Iterator<TreeNode> iterator = treeNodes.iterator(); iterator.hasNext();) {
			removeTreeNode = false;
			TreeNode treeNode = iterator.next();
			Integer projectID = null;
			if (treeNode.getId()!=null) {
				projectID = Integer.valueOf(treeNode.getId());
				if (projectID!=null) {
					if ((actualProjectID==null || (actualProjectID!=null && !projectID.equals(actualProjectID))) &&
							configuredProjects.contains(projectID)) {
						treeNode.setIcon(ProjectBL.PROJECT_NOT_SELECTABLE_ICON_CLASS);
						treeNode.setSelectable(false);
						removeTreeNode = true;
					}
				}
			}
			List<TreeNode> childrenNodes = treeNode.getChildren();
			if (childrenNodes!=null /*&& !configuredProjects.isEmpty()*/) {
				//important the order in and: first the recursive call then the partial value:
				//to guarantee that the already selected subprojects will be also removed even if the parent project is valid (not yet selected) 
				removeTreeNode = removeSelectedOrSetNotSelectable(childrenNodes, configuredProjects, actualProjectID) && removeTreeNode;
			}
			if (removeTreeNode) {
				iterator.remove();
			}
		}
		return removeTreeNode;
	}
	
	/**
	 * Creates the tree node for "other projects"
	 * @param locale
	 * @return
	 */
	private static TreeNode createTreeNodeForOtherProjects(Locale locale) {
		TreeNode treeNode = new TreeNode();
		treeNode.setId(OTHERPROJECTSID.toString());
		treeNode.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.automail.assignments.lbl.otherProjects", locale));
		return treeNode;
	}
	
	
	/**
	 * Loads a list of notifyTriggerBeans 
	 * @return
	 */
	public static Map<Integer, TNotifyTriggerBean> loadTriggersByNotifySettings(Integer personID, boolean isDefault) {
		List<TNotifyTriggerBean> notifyTriggers;
		if (isDefault) {
			notifyTriggers = notifyTriggerDAO.loadByDefaultNotifySettings();
		} else {
			notifyTriggers = notifyTriggerDAO.loadByOwnOrDefaultNotifySettings(personID);
		}
		return GeneralUtils.createMapFromList(notifyTriggers);
	}
	
	/**
	 * Loads a list of queryRepositoryBeans 
	 * @return
	 */
	public static Map<Integer, TQueryRepositoryBean> loadFiltersByNotifySettings(Integer personID, boolean isDefault) {
		List<TQueryRepositoryBean> notifyFilters;
		if (isDefault) {
			notifyFilters = queryRepositoryDAO.loadByDefaultNotifySettings();
		} else {
			notifyFilters = queryRepositoryDAO.loadByOwnOrDefaultNotifySettings(personID);
		}
		return GeneralUtils.createMapFromList(notifyFilters);
	}
	
}
