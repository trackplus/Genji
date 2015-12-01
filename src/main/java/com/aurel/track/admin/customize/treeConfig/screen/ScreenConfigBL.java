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

package com.aurel.track.admin.customize.treeConfig.screen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.*;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ScreenConfigDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;

/**
 * Business logic for screen configuration 
 * @author Adrian Bojani
 *
 */
public class ScreenConfigBL {
	private static ScreenConfigDAO screenConfigDAO = DAOFactory.getFactory().getScreenConfigDAO();
	
	/**
	 * Gets TScreenConfigBeans by project
	 * @param project
	 * @return
	 */
	public static List<TScreenConfigBean> loadAllByProject(Integer project) {
		return screenConfigDAO.loadAllByProject(project);
	}
	
	/**
	 * Copies a screen configuration
	 * @param screenConfigBean
	 * @param deep
	 */
	public static TScreenConfigBean copy(TScreenConfigBean screenConfigBean, boolean deep) {
		return screenConfigDAO.copy(screenConfigBean, deep);
	}
	
	/**
	 * Save  screenConfig in the TScreenConfig table
	 * @param screenConfig
	 * @return
	 */
	public static Integer save(TScreenConfigBean screenConfig) {
		return screenConfigDAO.save(screenConfig);
	}
	
	/**
	 * Change the screen for screen configuration
	 * @param cfg
	 * @param screenID
	 */
	private static void updateConfig(TScreenConfigBean cfg, Integer screenID){
		cfg.setScreen(null);//to force isModify to true
		cfg.setTScreenBean(null);//
		cfg.setScreen(screenID);
		save(cfg);
	}
	
	/**
	 * Saves the screen configuration 
	 * @return
	 */
	static String save(String node, Integer screenID, HttpServletResponse servletResponse){
		if (screenID!=null) {
			TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
			Integer issueType = treeConfigIDTokens.getIssueTypeID();
			Integer projectType = treeConfigIDTokens.getProjectTypeID();
			Integer project = treeConfigIDTokens.getProjectID();
			Integer configRel = treeConfigIDTokens.getConfigRelID();
			TScreenConfigBean screenConfigBean = (TScreenConfigBean)ScreenConfigItemFacade.getInstance().
				getValidConfigDirect(issueType, projectType, project, configRel);
			if (screenConfigBean==null) {
				//a fallback node will be first overridden and then actualized with the screen
				screenConfigBean = (TScreenConfigBean)TreeConfigBL.overwrite(node);
			}
			updateConfig(screenConfigBean, screenID);
		}
		JSONUtility.encodeJSON(servletResponse, 
				ScreenAssignmentJSON.getScreenDetailSaveJSON(node));
		return null;
	}
	
	/**
	 * Gets the screens accessible for the current user
	 * @param personID
	 * @return
	 */
	public static Set<Integer> getVisibleScreensForPerson(Integer personID) {
		//all projects the user hat role in
		List<TProjectBean> projectList = ProjectBL.loadUsedProjectsFlat(personID);
		Set<Integer> allProjectSet = new HashSet<Integer>();
		Set<Integer> allProjectTypeSet = new HashSet<Integer>();
		Map<Integer, Set<Integer>> projectTypeToProjectsMap = new HashMap<Integer, Set<Integer>>();
		Map<Integer, Integer> projectToProjectTypeMap = new HashMap<Integer, Integer>();
		//load the project/projectType sets/maps
		if (projectList!=null) {
			for (TProjectBean projectBean : projectList) {
				Integer projectID = projectBean.getObjectID();
				Integer projectTypeID = projectBean.getProjectType();
				allProjectSet.add(projectID);
				allProjectTypeSet.add(projectTypeID);
				projectToProjectTypeMap.put(projectID, projectTypeID);
				Set<Integer> projectForProjectTypeSet = projectTypeToProjectsMap.get(projectTypeID);
				if (projectForProjectTypeSet==null) {
					projectForProjectTypeSet = new HashSet<Integer>();
					projectTypeToProjectsMap.put(projectTypeID, projectForProjectTypeSet);
				}
				projectForProjectTypeSet.add(projectID);
			}
		}
		//issueTypes allowed for projectType: not linked with persons but if some roles 
		//does not have issue type limitations for projects then we take 
		//the possible issue types for projectType as issue type limitation. 
		//The issue types for such projects will be limited by issue types for project types
		List<TPlistTypeBean> pIssueTypeList = IssueTypeBL.loadByProjectTypes(allProjectTypeSet.toArray());
		Map<Integer, Set<Integer>> projectTypeIssueTypeMap = new HashMap<Integer, Set<Integer>>();
		if (pIssueTypeList!=null) {
			for (TPlistTypeBean plistTypeBean : pIssueTypeList) {
				Integer projectType = plistTypeBean.getProjectType();
				Integer issusType = plistTypeBean.getCategory();
				Set<Integer> issueTypeSet = projectTypeIssueTypeMap.get(projectType);
				if (issueTypeSet==null) {
					issueTypeSet = new HashSet<Integer>();
					projectTypeIssueTypeMap.put(projectType, issueTypeSet);
				}
				issueTypeSet.add(issusType);
			}
		}
		
		//role flags to search for
		int[] arrRights = new int[] {AccessFlagIndexes.READANYTASK, 
						AccessFlagIndexes.MODIFYANYTASK,
						AccessFlagIndexes.CREATETASK,
						AccessFlagIndexes.PROJECTADMIN};
		Map<Integer, Set<Integer>> projectIssueTypeMap = AccessBeans.getProjectsToIssueTypesWithRoleForPerson(
				AccessBeans.getMeAndSubstitutedAndGroups(personID), GeneralUtils.createIntegerArrFromSet(allProjectSet), arrRights);
		Iterator<Integer> iterator = allProjectSet.iterator();
		while (iterator.hasNext()) {
			Integer projectID = iterator.next();
			Set<Integer> issueTypes = projectIssueTypeMap.get(projectID);
			if (issueTypes==null || issueTypes.contains(null)) {
				//issueTypes==null - > no explicit right in project (only RACI in some issues from project) or 
				//issueTypes.contains(null) -> explicit right in project without 
				//issue type limitation add the projectType limitations for project
				Integer projectTypeID = projectToProjectTypeMap.get(projectID);
				//can be null if no issue Type limitation for project type 
				issueTypes = projectTypeIssueTypeMap.get(projectTypeID);
				projectIssueTypeMap.put(projectID, issueTypes);
			}
		}
		
		//add project and issueType specific screens for actions
		Map<Integer, Map<Integer, Set<Integer>>> projectToIssueTypeToAction = new HashMap<Integer, Map<Integer, Set<Integer>>>();
		Set<Integer> screens = new HashSet<Integer>();
		List<TScreenConfigBean> projectSpecificSceenConfigs = screenConfigDAO.loadByProjects(allProjectSet.toArray());
		if (projectSpecificSceenConfigs!=null) {
			for (TScreenConfigBean screenConfigBean : projectSpecificSceenConfigs) {
				addScreen(projectToIssueTypeToAction, projectIssueTypeMap,
					screenConfigBean.getProject(), 
					screenConfigBean.getIssueType(),
					screenConfigBean.getAction(), 
					screenConfigBean.getScreen(), screens);
			}
		}
		
		//add projectType specific screens for actions
		List<TScreenConfigBean> projectTypeSpecificSceenConfigs = screenConfigDAO.loadByProjectTypes(allProjectTypeSet.toArray());
		if (projectTypeSpecificSceenConfigs!=null) {
			for (TScreenConfigBean screenConfigBean : projectTypeSpecificSceenConfigs) {
				Integer projectTypeID = screenConfigBean.getProjectType();
				//try all projects for this projectType (add if not yet set already at project-issueType level)
				Set<Integer> projectIDs = projectTypeToProjectsMap.get(projectTypeID);
				if (projectIDs!=null) {
					Iterator<Integer> itrProject = projectIDs.iterator();
					while (itrProject.hasNext()) {
						Integer projectID = itrProject.next();
						addScreen(projectToIssueTypeToAction, projectIssueTypeMap,
							projectID, screenConfigBean.getIssueType(),
							screenConfigBean.getAction(), screenConfigBean.getScreen(), screens);
					}
				}
			}
		}
		
		//add issueType specific screens for actions
		List<TListTypeBean> issueTypeList = IssueTypeBL.loadAllSelectable();
		Set<Integer> issueTypeSet = GeneralUtils.createIntegerSetFromBeanList(issueTypeList);
		List<TScreenConfigBean> issueTypeSpecificSceenConfigs = screenConfigDAO.loadByIssueTypes(issueTypeSet.toArray());
		if (issueTypeSpecificSceenConfigs!=null) {
			for (TScreenConfigBean screenConfigBean : issueTypeSpecificSceenConfigs) {
				if (allProjectSet!=null) {
					//try to set an issueType specific screen for each project 
					//(add if not yet set already at project-issueType or projectType-issueType level)
					for (Integer projectID : allProjectSet) {
						if (addScreen(projectToIssueTypeToAction, projectIssueTypeMap,
							projectID,
							screenConfigBean.getIssueType(),
							screenConfigBean.getAction(), 
							screenConfigBean.getScreen(), screens)) {
							//break to the next TScreenConfigBean: because for all projects has the same screen anyway
							break;
						}
					}
				}
			}
		}
		
		//add default screens for actions
		List<TScreenConfigBean> defaultSceenConfigs = screenConfigDAO.loadDefaults();
		if (defaultSceenConfigs!=null) {
			for (TScreenConfigBean screenConfigBean : defaultSceenConfigs) {
				if (allProjectSet!=null) {
					for (Integer projectID : allProjectSet) {
						Set<Integer> issueTypes = projectIssueTypeMap.get(projectID);
						if (issueTypes==null || issueTypes.isEmpty()) {
							//no issue type limitation for project: take all issueTypes
							issueTypes = issueTypeSet;
						}
						boolean addedForAction = false;
						for (Integer issueTypeID : issueTypes) {
							if (addScreen(projectToIssueTypeToAction, projectIssueTypeMap, projectID, issueTypeID, 
								screenConfigBean.getAction(), screenConfigBean.getScreen(), screens)) {
								addedForAction = true;
								break;
							}
						}
						if (addedForAction) {
							//break to the next TScreenConfigBean: because for all projects and issueType it has the same screen anyway  
							break;
						}
					}
				}
			}
		}
		return screens;
	}
	
	/**
	 * Add a screen for a project-issueType-action if it is not yet set
	 * @param projectToIssueTypeToAction
	 * @param projectID
	 * @param issueTypeID
	 * @param actionID
	 * @param screenID
	 * @param screens
	 * @return
	 */
	private static boolean addScreen(Map<Integer, Map<Integer, Set<Integer>>> projectToIssueTypeToAction,
			Map<Integer, Set<Integer>> projectIssueTypeMap,
			Integer projectID, Integer issueTypeID, Integer actionID, Integer screenID, Set<Integer> screens) {
		Set<Integer> issueTypes = projectIssueTypeMap.get(projectID);
		//add the screen only for issueTypes accessible by the user 
		if (issueTypes==null || (issueTypes.contains(issueTypeID) || issueTypes.contains(null))) {
			Map<Integer, Set<Integer>> issueTypeToAction = projectToIssueTypeToAction.get(projectID);
			if (issueTypeToAction==null) {
				issueTypeToAction = new HashMap<Integer, Set<Integer>>();
				projectToIssueTypeToAction.put(projectID, issueTypeToAction);
			}
			if (issueTypeID==null) {
				//not yet possible, because there is (yet) no possibility to add project or projectType specific screens 
				//(only project-issueType or projectType-issueType specific screens are possible now)  
				issueTypeID = Integer.valueOf(0);
			}
			Set<Integer> actions = issueTypeToAction.get(issueTypeID);
			if (actions==null) {
				actions = new HashSet<Integer>();
				issueTypeToAction.put(issueTypeID, actions);
			}		
			if (!actions.contains(actionID)) {
				//no screen exists for this action
				actions.add(actionID);
				screens.add(screenID);
				return true;
			} 
		}
		return false;
	}
	
	/**
	 * Obtain the most appropriate screens for actions in given context
	 * @param action
	 * @param projectBean
	 * @param issueTypeID
	 * @return
	 */
	public static Map<Integer, Integer> getScreenForAction(Integer projectID, Integer issueTypeID){
		Map<Integer, Integer> screenForAction = new HashMap<Integer, Integer>();
		TScreenConfigBean screenConfigBean;		
		if (projectID!=null) {
			//project and issue type specific screens
			screenConfigBean=new TScreenConfigBean();
			screenConfigBean.setProject(projectID);
			screenConfigBean.setIssueType(issueTypeID);
			addNotPresentToMap(screenConfigDAO.load(screenConfigBean), screenForAction);
			TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
			if (projectBean!=null) {
				Integer parentProjectID = projectBean.getParent();
				//ancestor project - issueType configuration
				while (parentProjectID!=null) {
					TProjectBean parentProjectBean = LookupContainer.getProjectBean(parentProjectID);
					if (parentProjectBean!=null) {
						if (EqualUtils.equal(projectBean.getProjectType(), parentProjectBean.getProjectType())) {
							screenConfigBean.setProject(parentProjectID);
							List<TScreenConfigBean> issueTypeaAncestorProjectScreenConfigs = screenConfigDAO.load(screenConfigBean);
							addNotPresentToMap(issueTypeaAncestorProjectScreenConfigs, screenForAction);
						}
						parentProjectID = parentProjectBean.getParent();
					} else {
						break;
					}
				}
				//projectType and issueType specific screens
				screenConfigBean=new TScreenConfigBean();
				screenConfigBean.setProjectType(projectBean.getProjectType());
				screenConfigBean.setIssueType(issueTypeID);
				addNotPresentToMap(screenConfigDAO.load(screenConfigBean), screenForAction);
			}
		}
		//issue type specific screens
		screenConfigBean=new TScreenConfigBean();
		screenConfigBean.setIssueType(issueTypeID);
		addNotPresentToMap(screenConfigDAO.load(screenConfigBean), screenForAction);
		
		//get the defaultConfig screens
		screenConfigBean=new TScreenConfigBean();
		addNotPresentToMap(screenConfigDAO.load(screenConfigBean), screenForAction);
		return screenForAction;
	}
	
	/**
	 * Adds the screen for the action only if a more specific was not added already
	 * @param screenConfigBeanList
	 * @param screenForAction
	 */
	private static void addNotPresentToMap(List<TScreenConfigBean> screenConfigBeanList, Map<Integer, Integer> screenForAction) {
		if (screenConfigBeanList!=null && !screenConfigBeanList.isEmpty()) {
			for (TScreenConfigBean screenConfigBean : screenConfigBeanList) {
				Integer action = screenConfigBean.getAction();
				if (!screenForAction.containsKey(action)) {		
					screenForAction.put(action, screenConfigBean.getScreen());
				}
			}
		}
	}
}
