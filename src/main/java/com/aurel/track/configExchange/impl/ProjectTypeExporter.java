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

package com.aurel.track.configExchange.impl;

import java.util.*;

import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.*;
import com.aurel.track.configExchange.exporter.EntityContext;
import com.aurel.track.configExchange.exporter.EntityDependency;
import com.aurel.track.configExchange.exporter.EntityRelation;
import com.aurel.track.dao.*;
import com.aurel.track.fieldType.runtime.base.LookupContainer;

/**
 */
public class ProjectTypeExporter {

	private static PIssueTypeDAO pIssueTypeDAO = DAOFactory.getFactory().getPIssueTypeDAO();
	private static PStatusDAO pStatusDAO = DAOFactory.getFactory().getPStatusDAO();
	private static PPriorityDAO pPriorityDAO= DAOFactory.getFactory().getPPriorityDAO();
	private static PSeverityDAO pSeverityDAO= DAOFactory.getFactory().getPSeverityDAO();
	private static ChildProjectTypeDAO childProjectTypeDAO = DAOFactory.getFactory().getChildProjectTypeDAO();
	private static PRoleDAO pRoleDAO = DAOFactory.getFactory().getPRoleDAO();

	private static FieldConfigDAO fieldConfigDAO=DAOFactory.getFactory().getFieldConfigDAO();
	private static ScreenConfigDAO screenConfigDAO=DAOFactory.getFactory().getScreenConfigDAO();
	private static WorkflowConnectDAO workflowConnectDAO=DAOFactory.getFactory().getWorkflowConnectDAO();

	public List<EntityContext> export(Integer projectTypeID,boolean includeGlobal) {
		List<EntityContext> entityList = new ArrayList<EntityContext>();
		TProjectTypeBean projectTypeBean= ProjectTypesBL.loadByPrimaryKey(projectTypeID);
		Map<String,Set<Integer>> exportedEntities=new HashMap<String, Set<Integer>>();
		entityList.addAll(createContext(projectTypeBean,exportedEntities,includeGlobal));

		return entityList ;
	}
	public List<EntityContext> createContext(TProjectTypeBean projectTypeBean,Map<String,Set<Integer>> exportedEntities,boolean includeGlobal){
		List<EntityContext> result=new ArrayList<EntityContext>();
		Integer projectTypeID=projectTypeBean.getObjectID();
		List<TPlistTypeBean> projectTypeToIssueType = pIssueTypeDAO.loadByProjectType(projectTypeID);
		List<Integer> issueTypeIdList=new ArrayList<Integer>();
		for(TPlistTypeBean plistTypeBean:projectTypeToIssueType){
			issueTypeIdList.add(plistTypeBean.getCategory());
		}
		Object[] issueTypeIds=issueTypeIdList.toArray();
		if(includeGlobal){
			//fields
			FieldConfigExporter fieldConfigExporter=new FieldConfigExporter();
			//default fields config
			List<TFieldConfigBean> fieldConfigDefaultList = FieldConfigBL.loadDefault();
			List<EntityContext> defaultFieldConfigCtxList=fieldConfigExporter.exportFieldConfigList(fieldConfigDefaultList,exportedEntities);
			result.addAll(defaultFieldConfigCtxList);
			//issue types fields config
			List<TFieldConfigBean> fieldConfigIssueTypeList = fieldConfigDAO.loadByIssueTypes(issueTypeIds);
			List<EntityContext> fieldConfigIssueTypeCtxList=fieldConfigExporter.exportFieldConfigList(fieldConfigIssueTypeList, exportedEntities);
			result.addAll(fieldConfigIssueTypeCtxList);


			//screens
			ScreenConfigExporter screenConfigExporter=new ScreenConfigExporter();
			//default screens configs
			List<TScreenConfigBean> screenConfigBeanDefaultList = screenConfigDAO.loadDefaults();
			List<EntityContext> defaultScreenConfigCtxList=screenConfigExporter.exportScreenConfigList(screenConfigBeanDefaultList, exportedEntities,false);
			result.addAll(defaultScreenConfigCtxList);
			//issue type screens configs only form project type
			List<TScreenConfigBean> screenConfigBeanByIssueTypeList = screenConfigDAO.loadByIssueTypes(issueTypeIds);
			List<EntityContext> screenConfigIssueTypeCtxList=screenConfigExporter.exportScreenConfigList(screenConfigBeanByIssueTypeList,exportedEntities,false);
			result.addAll(screenConfigIssueTypeCtxList);
		}


		EntityContext entityContext = createEntityContext(projectTypeBean, exportedEntities, includeGlobal, projectTypeToIssueType);

		result.add(entityContext);
		return result;
	}

	public EntityContext createEntityContext(TProjectTypeBean projectTypeBean, Map<String, Set<Integer>> exportedEntities, boolean includeGlobal, List<TPlistTypeBean> projectTypeToIssueType) {
		Integer projectTypeID=projectTypeBean.getObjectID();
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TProjectTypeBean");
		entityContext.setSerializableLabelBeans(projectTypeBean);

		if(projectTypeToIssueType==null){
			projectTypeToIssueType = pIssueTypeDAO.loadByProjectType(projectTypeID);
		}


		//fields project type specific config
		List<TFieldConfigBean> fieldConfigProjectTypeList = fieldConfigDAO.loadAllByProjectType(projectTypeID);

		if(fieldConfigProjectTypeList!=null&&fieldConfigProjectTypeList.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			FieldConfigExporter fieldExporter=new FieldConfigExporter();
			boolean includeField=!includeGlobal;
			for(TFieldConfigBean fieldConfigBean : fieldConfigProjectTypeList) {
				EntityContext entityFieldConfig = fieldExporter.createFieldConfigContext(fieldConfigBean,includeField,exportedEntities);
				entityList.add(entityFieldConfig);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}

		//issueTypes
		if(projectTypeToIssueType!=null&&projectTypeToIssueType.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			for(TPlistTypeBean plistTypeBean : projectTypeToIssueType) {
				EntityContext entityPlist = createPListTypeContext(plistTypeBean);
				entityList.add(entityPlist);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}

		//status
		List<TPstateBean> pStatusList = pStatusDAO.loadByProjectType(projectTypeID);
		if(pStatusList!=null&&pStatusList.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			for(TPstateBean pstateBean : pStatusList) {
				EntityContext entityPlist = createPStateContext(pstateBean);
				entityList.add(entityPlist);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}

		//priority
		List<TPpriorityBean> tPpriorityBeanList = pPriorityDAO.loadByProjectType(projectTypeID);
		if(tPpriorityBeanList!=null&&tPpriorityBeanList.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			for(TPpriorityBean ppriorityBean : tPpriorityBeanList) {
				EntityContext entityPlist = createPPriorityContext(ppriorityBean);
				entityList.add(entityPlist);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}

		//severity
		List<TPseverityBean> tPseverityBeanList = pSeverityDAO.loadByProjectType(projectTypeID);
		if(tPseverityBeanList!=null&&tPseverityBeanList.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			for(TPseverityBean pseverityBean : tPseverityBeanList) {
				EntityContext entityPlist = createPSeverityContext(pseverityBean);
				entityList.add(entityPlist);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}


		//roles
		List<TPRoleBean> pRoleBeanList = pRoleDAO.loadByProjectType(projectTypeID);
		if(pRoleBeanList!=null&&pRoleBeanList.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			for(TPRoleBean tpRoleBean : pRoleBeanList) {
				EntityContext entityPlist = createPRoleContext(tpRoleBean,exportedEntities);
				entityList.add(entityPlist);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}





		//child workspaceType
		List<TChildProjectTypeBean> childProjectTypeBeans = childProjectTypeDAO.loadAssignmentsByParent(projectTypeID);
		if(childProjectTypeBeans!=null&&childProjectTypeBeans.size()>0){
			EntityRelation relation = new EntityRelation("projectTypeParent");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			for(TChildProjectTypeBean childProjectTypeBean : childProjectTypeBeans) {
				EntityContext entityProjectType = createChildProjectTypeContext(childProjectTypeBean,exportedEntities,includeGlobal);
				entityList.add(entityProjectType);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}

		//project type specific screens configs
		if(includeGlobal){
			exportSpecificScreenConfigs(exportedEntities, includeGlobal, projectTypeID, entityContext);
		}else{
			//export all screen config
			exportExplictScreenConfigs(exportedEntities, includeGlobal, projectTypeID, entityContext,projectTypeToIssueType);
		}
		return entityContext;
	}

	private void exportSpecificScreenConfigs(Map<String, Set<Integer>> exportedEntities, boolean includeGlobal, Integer projectTypeID, EntityContext entityContext) {
		List<TScreenConfigBean> screenConfigBeanProjectTypeList = screenConfigDAO.loadAllByProjectType(projectTypeID);
		if(screenConfigBeanProjectTypeList!=null&&screenConfigBeanProjectTypeList.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			ScreenConfigExporter screenConfigExporter=new ScreenConfigExporter();
			boolean includeFieldConfig=!includeGlobal;
			for(TScreenConfigBean screenConfigBean : screenConfigBeanProjectTypeList) {
				EntityContext entityScreenConfig = screenConfigExporter.createScreenConfigContext(screenConfigBean,exportedEntities,includeFieldConfig);
				entityList.add(entityScreenConfig);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}
	}
	private void exportExplictScreenConfigs(Map<String, Set<Integer>> exportedEntities, boolean includeGlobal, Integer projectTypeID, EntityContext entityContext,List<TPlistTypeBean> projectTypeToIssueType) {
		List<TScreenConfigBean> screenConfigBeanProjectTypeListDB = screenConfigDAO.loadAllByProjectType(projectTypeID);
		Map<Integer,Map<Integer,TScreenConfigBean>> explicitScreenConfig=new HashMap<Integer,Map<Integer,TScreenConfigBean>>();
		Map<Integer,TScreenConfigBean> screenConfigByActions;
		if(screenConfigBeanProjectTypeListDB!=null){
			Integer issueTypeID;
			for(TScreenConfigBean screenConfigBean:screenConfigBeanProjectTypeListDB){
				issueTypeID=screenConfigBean.getIssueType();
				screenConfigByActions=explicitScreenConfig.get(issueTypeID);
				if(screenConfigByActions==null){
					screenConfigByActions=new HashMap<Integer,TScreenConfigBean> ();
					explicitScreenConfig.put(issueTypeID,screenConfigByActions);
				}
				screenConfigByActions.put(screenConfigBean.getAction(),screenConfigBean);
			}
		}
		List<Integer> issueTypeIdList=new ArrayList<Integer>();
		for(TPlistTypeBean plistTypeBean:projectTypeToIssueType){
			issueTypeIdList.add(plistTypeBean.getCategory());
		}
		List<TScreenConfigBean> screenConfigBeanDefaultList = screenConfigDAO.loadDefaults();
		List<TScreenConfigBean> screenConfigBeanByIssueTypeList = screenConfigDAO.loadByIssueTypes(issueTypeIdList.toArray());


		List<TActionBean> actionList =DAOFactory.getFactory().getActionDAO().loadAll();
		List<Integer> actionIDList=new ArrayList<Integer>();
		for(TActionBean a:actionList){
			actionIDList.add(a.getObjectID());
		}
		int id=-1;
		for(Integer issueTypeID:issueTypeIdList){
			screenConfigByActions=explicitScreenConfig.get(issueTypeID);
			if(screenConfigByActions==null){
				screenConfigByActions=new HashMap<Integer,TScreenConfigBean> ();
				explicitScreenConfig.put(issueTypeID,screenConfigByActions);
			}
			for(Integer actionID:actionIDList){
				if(!screenConfigByActions.containsKey(actionID)){
					TScreenConfigBean screenConfigBean=findScreenConfig(screenConfigBeanByIssueTypeList,issueTypeID,actionID);
					if(screenConfigBean!=null){
						screenConfigByActions.put(actionID,copyScreenConfig(screenConfigBean,id,projectTypeID,issueTypeID));
					}else{
						screenConfigBean=findScreenConfig(screenConfigBeanDefaultList,actionID);
						screenConfigByActions.put(actionID,copyScreenConfig(screenConfigBean,id,projectTypeID,issueTypeID));
					}
					id--;
				}
			}
		}
		List<TScreenConfigBean> screenConfigBeanProjectTypeList=new ArrayList<TScreenConfigBean>();

		Iterator<Integer> it=explicitScreenConfig.keySet().iterator();
		while(it.hasNext()){
			Integer issueTypeID=it.next();
			screenConfigByActions=explicitScreenConfig.get(issueTypeID);
			Iterator<Integer> itActions=screenConfigByActions.keySet().iterator();
			while(itActions.hasNext()){
				Integer actionID=itActions.next();
				TScreenConfigBean screenConfigBean=screenConfigByActions.get(actionID);
				screenConfigBeanProjectTypeList.add(screenConfigBean);
			}
		}


		if(screenConfigBeanProjectTypeList!=null&&screenConfigBeanProjectTypeList.size()>0){
			EntityRelation relation = new EntityRelation("projectType");
			List<EntityContext> entityList = new ArrayList<EntityContext>();
			ScreenConfigExporter screenConfigExporter=new ScreenConfigExporter();
			boolean includeFieldConfig=!includeGlobal;
			for(TScreenConfigBean screenConfigBean : screenConfigBeanProjectTypeList) {
				EntityContext entityScreenConfig = screenConfigExporter.createScreenConfigContext(screenConfigBean,exportedEntities,includeFieldConfig);
				entityList.add(entityScreenConfig);
			}
			relation.setEntities(entityList);
			entityContext.addRelation(relation);
		}
	}
	private TScreenConfigBean copyScreenConfig(TScreenConfigBean screenConfigBean, int id,Integer projectTypeID,Integer issueTypeID){
		TScreenConfigBean result=new TScreenConfigBean();
		result.setObjectID(new Integer(id));

		result.setName(screenConfigBean.getName());
		result.setDescription(screenConfigBean.getDescription());
		result.setScreen(screenConfigBean.getScreen());
		result.setAction(screenConfigBean.getAction());

		result.setProjectType(projectTypeID);
		result.setIssueType(issueTypeID);

		return result;
	}

	private TScreenConfigBean findScreenConfig(List<TScreenConfigBean> screenConfigBeanByIssueTypeList, Integer issueTypeID, Integer actionID) {
		for(TScreenConfigBean screenConfigBean:screenConfigBeanByIssueTypeList){
			if(issueTypeID.equals(screenConfigBean.getIssueType())&&actionID.equals(screenConfigBean.getAction())){
				return screenConfigBean;
			}
		}
		return null;
	}
	private TScreenConfigBean findScreenConfig(List<TScreenConfigBean> screenConfigBeanByIssueTypeList, Integer actionID) {
		for(TScreenConfigBean screenConfigBean:screenConfigBeanByIssueTypeList){
			if(actionID.equals(screenConfigBean.getAction())){
				return screenConfigBean;
			}
		}
		return null;
	}

	public EntityContext createPListTypeContext(TPlistTypeBean plistTypeBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TPlistTypeBean");
		entityContext.setSerializableLabelBeans(plistTypeBean);

		//dependency
		Integer issueTypeID=plistTypeBean.getCategory();
		EntityDependency dependency = new EntityDependency("TListTypeBean",issueTypeID, "category");
		TListTypeBean listTypeBean = LookupContainer.getItemTypeBean(issueTypeID);

		IssueTypeExporter issueTypeExporter=new IssueTypeExporter();
		dependency.setEntityContext(issueTypeExporter.createContext(listTypeBean));
		entityContext.addDependecy(dependency);

		return entityContext;
	}
	public EntityContext createPStateContext(TPstateBean pstateBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TPstateBean");
		entityContext.setSerializableLabelBeans(pstateBean);

		//dependency
		Integer statusID=pstateBean.getState();
		EntityDependency dependency = new EntityDependency("TStateBean", statusID, "state");
		TStateBean stateBean = LookupContainer.getStatusBean(statusID);

		StateExporter stateExporter=new StateExporter();
		dependency.setEntityContext(stateExporter.createContext(stateBean));
		entityContext.addDependecy(dependency);

		return entityContext;
	}
	public EntityContext createPPriorityContext(TPpriorityBean ppriorityBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TPpriorityBean");
		entityContext.setSerializableLabelBeans(ppriorityBean);

		//dependency
		Integer priorityID=ppriorityBean.getPriority();
		EntityDependency dependency = new EntityDependency("TPriorityBean", priorityID,"priority");
		TPriorityBean priorityBean = LookupContainer.getPriorityBean(priorityID);

		PriorityExporter priorityExporter=new PriorityExporter();
		dependency.setEntityContext(priorityExporter.createContext(priorityBean));
		entityContext.addDependecy(dependency);

		return entityContext;
	}

	public EntityContext createPSeverityContext(TPseverityBean pseverityBean){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TPseverityBean");
		entityContext.setSerializableLabelBeans(pseverityBean);

		//dependency
		Integer severityID=pseverityBean.getSeverity();
		EntityDependency dependency = new EntityDependency("TSeverityBean", severityID, "severity");
		TSeverityBean severityBean = LookupContainer.getSeverityBean(severityID);

		SeverityExporter severityExporter=new SeverityExporter();
		dependency.setEntityContext(severityExporter.createContext(severityBean));
		entityContext.addDependecy(dependency);
		return entityContext;
	}

	public EntityContext createPRoleContext(TPRoleBean tpRoleBean,Map<String,Set<Integer>> exportedEntities){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TPRoleBean");
		entityContext.setSerializableLabelBeans(tpRoleBean);

		//dependency
		Integer roleID=tpRoleBean.getRole();
		EntityDependency dependency = new EntityDependency("TRoleBean", roleID, "role");

		Set<Integer> roleExported=exportedEntities.get("TRoleBean");
		if(roleExported==null){
			roleExported=new HashSet<Integer>();
			exportedEntities.put("TRoleBean",roleExported);
		}
		boolean roleAlreadyAdded=roleExported.contains(roleID);
		if(roleAlreadyAdded){
			dependency.setEntityAdded(true);
		}else{
			TRoleBean roleBean = RoleBL.loadRoleByKey(roleID,Locale.getDefault());
			RoleExporter roleExporter=new RoleExporter();
			dependency.setEntityContext(roleExporter.createContext(roleBean));
			roleExported.add(roleID);
		}
		entityContext.addDependecy(dependency);
		return entityContext;
	}





	public EntityContext createChildProjectTypeContext(TChildProjectTypeBean childProjectTypeBean,Map<String,Set<Integer>> exportedEntities,boolean includeGlobal){
		EntityContext entityContext=new EntityContext();
		entityContext.setName("TChildProjectTypeBean");
		entityContext.setSerializableLabelBeans(childProjectTypeBean);

		//dependency
		Integer projectTypeChildID=childProjectTypeBean.getProjectTypeChild();
		EntityDependency dependency = new EntityDependency("TProjectTypeBean", projectTypeChildID, "projectTypeChild");

		Set<Integer> projectTypeExported=exportedEntities.get("TProjectTypeBean");
		if(projectTypeExported==null){
			projectTypeExported=new HashSet<Integer>();
			exportedEntities.put("TProjectTypeBean",projectTypeExported);
		}
		boolean projectTypeAlreadyAdded=projectTypeExported.contains(projectTypeChildID);
		if(projectTypeAlreadyAdded){
			dependency.setEntityAdded(true);
		}else{
			TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeChildID);
			ProjectTypeExporter projectTypeExporter=new ProjectTypeExporter();
			projectTypeExported.add(projectTypeChildID);
			dependency.setEntityContext(projectTypeExporter.createEntityContext(projectTypeBean,exportedEntities,false,null));
		}

		entityContext.addDependecy(dependency);
		return entityContext;
	}
}
