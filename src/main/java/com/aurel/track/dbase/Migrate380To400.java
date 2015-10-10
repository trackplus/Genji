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

package com.aurel.track.dbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.ClobBL;
import com.aurel.track.admin.customize.category.filter.MenuitemFilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterReader;
import com.aurel.track.admin.customize.category.filter.tree.io.TreeFilterWriter;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.notify.trigger.NotifyTriggerBL;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigItemFacade;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.server.logging.LoggingConfigBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TCLOBBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListBean;
import com.aurel.track.beans.TMenuitemQueryBean;
import com.aurel.track.beans.TNotifyFieldBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TPrivateReportRepositoryBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectReportRepositoryBean;
import com.aurel.track.beans.TPublicReportRepositoryBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.ClassDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PrivateRepositoryDAO;
import com.aurel.track.dao.ProjectRepositoryDAO;
import com.aurel.track.dao.PublicRepositoryDAO;
import com.aurel.track.dao.QueryRepositoryDAO;
import com.aurel.track.dao.SubprojectDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.util.PropertiesHelper;


public class Migrate380To400 {	
	private static final Logger LOGGER = LogManager.getLogger(Migrate380To400.class);
	private static QueryRepositoryDAO queryRepositoryDAO = DAOFactory.getFactory().getQueryRepositoryDAO();
	private static PrivateRepositoryDAO privateRepositoryDAO = DAOFactory.getFactory().getPrivateRepositoryDAO();
	private static ProjectRepositoryDAO projectRepositoryDAO = DAOFactory.getFactory().getProjectRepositoryDAO();
	private static PublicRepositoryDAO publicRepositoryDAO = DAOFactory.getFactory().getPublicRepositoryDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	
	
	public static void migrate380To400() {
		LoggingConfigBL.setLevel(LOGGER, Level.INFO);
		migrateSubprojectAndClassAndSetIDNumber();
		migrateMenuItemQueries();
		migrateFilters();
		setUseReleaseAndWBS();
		migrateRoles();
		setBudgetTypes();
		migrateFullTrigger();
	}
	
	/**
	 * Add TMenuitemQueryBeans for private tree queries set as in menu
	 */
	private static void migrateMenuItemQueries() {
		LOGGER.info("Migrating the private queries in menu to menuitemQuery");
		List<TQueryRepositoryBean> privateTreeQueries = queryRepositoryDAO.loadPrivateReportQueriesInMenu();
		for (TQueryRepositoryBean queryRepositoryBean : privateTreeQueries) {
			if (queryRepositoryBean.isIncludeInMenu()) {
				TMenuitemQueryBean menuitemQueryBean = MenuitemFilterBL.loadByPersonAndQuery(
						queryRepositoryBean.getPerson(), queryRepositoryBean.getObjectID());
				if (menuitemQueryBean!=null) {
					//not yet added by a previous migration
					menuitemQueryBean.setPerson(queryRepositoryBean.getPerson());
					menuitemQueryBean.setQueryKey(queryRepositoryBean.getObjectID());
					menuitemQueryBean.setIncludeInMenu(true);
					MenuitemFilterBL.save(menuitemQueryBean);
				}
			}
		}
	}
	
	/**
	 * Migrate the TQL filters into query repository table
	 */
	private static void migrateFilters() {
		//migrate the TQL queries into query repository	  
		List<TPrivateReportRepositoryBean> privateTQLs = privateRepositoryDAO.loadAll();
		LOGGER.info("Migrating private TQLs");
		if (privateTQLs!=null && !privateTQLs.isEmpty()) {
			LOGGER.info("Migrating " + privateTQLs.size() + " private TQLs ");
			for (TPrivateReportRepositoryBean privateReportRepositoryBean : privateTQLs) {
				Integer queryID = saveQueryRepositoryBeanForTQL(privateReportRepositoryBean.getName(), privateReportRepositoryBean.getOwnerID(), 
						privateReportRepositoryBean.getQuery(), Integer.valueOf(TQueryRepositoryBean.REPOSITORY_TYPE.PRIVATE));
				if (privateReportRepositoryBean.isMenuItem() && queryID!=null) {
					TMenuitemQueryBean menuitemQueryBean = new TMenuitemQueryBean();
					menuitemQueryBean.setPerson(privateReportRepositoryBean.getOwnerID());
					menuitemQueryBean.setQueryKey(queryID);
					MenuitemFilterBL.save(menuitemQueryBean);
				}
			}
		}
		List<TProjectReportRepositoryBean> projectTQLs = projectRepositoryDAO.loadAll();
		LOGGER.info("Migrating project TQLs");
		if (projectTQLs!=null && !projectTQLs.isEmpty()) {
			LOGGER.info("Migrating " + projectTQLs.size() + " project TQLs ");
			for (TProjectReportRepositoryBean projectReportRepositoryBean : projectTQLs) {
				saveQueryRepositoryBeanForTQL(projectReportRepositoryBean.getName(), projectReportRepositoryBean.getProjectID(), 
						projectReportRepositoryBean.getQuery(), Integer.valueOf(TQueryRepositoryBean.REPOSITORY_TYPE.PROJECT));
			}
		}
		List<TPublicReportRepositoryBean> publicTQLs = publicRepositoryDAO.loadAll();
		LOGGER.info("Migrating public TQLs");
		if (publicTQLs!=null && !publicTQLs.isEmpty()) {
			LOGGER.info("Migrating " + publicTQLs.size() + " public TQLs ");
			for (TPublicReportRepositoryBean publicReportRepositoryBean : publicTQLs) {
				saveQueryRepositoryBeanForTQL(publicReportRepositoryBean.getName(), publicReportRepositoryBean.getOwnerID(), 
						publicReportRepositoryBean.getQuery(), Integer.valueOf(TQueryRepositoryBean.REPOSITORY_TYPE.PUBLIC));
			}
		}
	}
	
	
	/**
	 * Saves a queryRepositoryBean with a TQL expression
	 * @param name
	 * @param ownerID
	 * @param queryExpression
	 * @param repository
	 * @return
	 */
	 private static Integer saveQueryRepositoryBeanForTQL(String name, Integer ownerID, String queryExpression, Integer repository) {
		if (queryExpression!=null && !"".equals(queryExpression)) {
			TQueryRepositoryBean queryRepositoryBean = new TQueryRepositoryBean();
			if (queryExpression.startsWith("#")) {
				queryRepositoryBean.setQueryType(Integer.valueOf(TQueryRepositoryBean.QUERY_PURPOSE.TQLPLUS_FILTER));
				//remove the # from the query expression
				queryExpression = queryExpression.substring(1);
			} else {
				queryRepositoryBean.setQueryType(Integer.valueOf(TQueryRepositoryBean.QUERY_PURPOSE.TQL_FILTER));
			}
			TCLOBBean clobBean = new TCLOBBean();
			clobBean.setCLOBValue(queryExpression);
			Integer clobID = ClobBL.save(clobBean);
			queryRepositoryBean.setQueryKey(clobID);
			if (repository.intValue()==TQueryRepositoryBean.REPOSITORY_TYPE.PROJECT) {
				queryRepositoryBean.setProject(ownerID);
			} else {
				queryRepositoryBean.setPerson(ownerID);
			}
			queryRepositoryBean.setLabel(name);
			queryRepositoryBean.setRepositoryType(repository);
			return queryRepositoryDAO.save(queryRepositoryBean);
		}
		return null;
	}
		
	/**
	 * Migrate the subproject and class entries to custom selects and configure the fields project specific	
	 */
	private static void migrateSubprojectAndClassAndSetIDNumber() {
		//set the type for subsystem and class to custom select
		TFieldBean subprojectFieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_SUBPROJECT);
		if (subprojectFieldBean!=null) {
			LOGGER.info("Changing subproject field to CustomSelectSimple");
			subprojectFieldBean.setFieldType("com.aurel.track.fieldType.types.custom.CustomSelectSimple");
			subprojectFieldBean.setIsCustom(BooleanFields.TRUE_VALUE);
			subprojectFieldBean.setRequired(BooleanFields.FALSE_VALUE);
			subprojectFieldBean.setFilterFieldString(true);
			FieldBL.save(subprojectFieldBean);
		}
		
		LOGGER.info("Changing class field to CustomSelectSimple");
		TFieldBean classFieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_CLASS);
		if (classFieldBean!=null) {
			classFieldBean.setFieldType("com.aurel.track.fieldType.types.custom.CustomSelectSimple");
			classFieldBean.setIsCustom(BooleanFields.TRUE_VALUE);
			classFieldBean.setRequired(BooleanFields.FALSE_VALUE);
			DAOFactory.getFactory().getFieldDAO().save(classFieldBean);
		}
		
		//set the previously required subproject and class to not required because the "-" subprojects/classes will not be migrated
		//as project specific custom lists and if subsystem is required then no new issue can be created
		Set<Integer> fieldSet = new HashSet<Integer>();
		fieldSet.add(SystemFields.INTEGER_SUBPROJECT);
		fieldSet.add(SystemFields.INTEGER_CLASS);
		List<TFieldConfigBean> fieldConfigBeanList = FieldConfigBL.loadAllForFields(fieldSet);
		if (fieldConfigBeanList!=null) {
			for (TFieldConfigBean fieldConfigBean : fieldConfigBeanList) {
				fieldConfigBean.setRequiredString(Boolean.FALSE);
				FieldConfigBL.save(fieldConfigBean);
			}
		}
		
		boolean includeSubproject = false;
		boolean includeClass = false;
		
		Map<Integer, Integer> subprojectOptionMatchMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> classOptionMatchMap = new HashMap<Integer, Integer>();
		//if (subprojectFieldBean!=null || classFieldBean!=null) {
			List<TProjectBean> projectBeanList = ProjectBL.loadAll();
			SubprojectDAO subprojectDAO = DAOFactory.getFactory().getSubprojectDAO();
			ClassDAO classDAO = DAOFactory.getFactory().getClassDAO();
			for (TProjectBean projectBean : projectBeanList) {
				Integer projectID = projectBean.getObjectID();
				List<TWorkItemBean> projectWorkItemBeansList = ItemBL.loadAllByProject(projectID,
						Integer.valueOf(FilterUpperTO.ARCHIVED_FILTER.INCLUDE_ARCHIVED),
						Integer.valueOf(FilterUpperTO.DELETED_FILTER.INCLUDE_DELETED));
				if (projectWorkItemBeansList!=null) {
					LOGGER.info("Migrate " + projectWorkItemBeansList.size() + " items for project " + projectID + ": " +  projectBean.getLabel());
					int i = 0;
					for (TWorkItemBean workItemBean : projectWorkItemBeansList) {
						//set the project specific ID number
						workItemBean.setIDNumber(++i);
						Integer parentID = workItemBean.getSuperiorworkitem();
						if (parentID!=null && parentID.equals(workItemBean.getObjectID())) {
							//remove parent if parent is the issue itself
							workItemBean.setSuperiorworkitem(null);
						}
						try {
							workItemDAO.saveSimple(workItemBean);
						} catch (ItemPersisterException e) {
							LOGGER.error("Saving IDNumber " + i + " for project " + projectID + ": " +  projectBean.getLabel() +
									" and item " + workItemBean.getObjectID() + " failed with " + e.getMessage(), e);
						}
					}
					Map<Integer, List<TWorkItemBean>> subprojectItems = getWorkItemsByEntity(projectWorkItemBeansList, true);
					if (subprojectFieldBean!=null) {
						Integer defaultSubproject = ProjectConfigBL.getDefaultSubproject(projectBean);
						List<ILabelBean> subprojectBeans = (List)subprojectDAO.loadByProject(projectID);
						if (subprojectBeans!=null && !subprojectBeans.isEmpty()) {
							if (subprojectBeans.size()>1 || !"-".equals(subprojectBeans.get(0).getLabel())) {
								includeSubproject = true;
								saveProjectSpecificFieldConfig(SystemFields.INTEGER_SUBPROJECT, subprojectBeans,
									projectID, defaultSubproject, subprojectFieldBean.getName(), subprojectItems, subprojectOptionMatchMap);
							} else {
								LOGGER.info("Disregard subprojects for project " + projectID + ": " +  projectBean.getLabel());
							}
						}
					}
					Map<Integer, List<TWorkItemBean>> classItems = getWorkItemsByEntity(projectWorkItemBeansList, false);
					if (classFieldBean!=null) {
						Integer defaultClass = ProjectConfigBL.getDefaultClass(projectBean);
						List<ILabelBean> classBeans = (List)classDAO.loadByProject(projectID);
						if (classBeans!=null && !classBeans.isEmpty()) {
							if (classBeans.size()>1 || !"-".equals(classBeans.get(0).getLabel())) {
								includeClass = true;
								saveProjectSpecificFieldConfig(SystemFields.INTEGER_CLASS, classBeans,
										projectID, defaultClass, classFieldBean.getName(), classItems, classOptionMatchMap);
							} else {
								LOGGER.info("Disregard classes for project " + projectID + ": " +  projectBean.getLabel());
							}
						}
					}
				}
			}
		//}		
		List<TCLOBBean> clobList = ClobBL.loadAll();
		for (TCLOBBean clobBean : clobList) {
			QNode qNode = TreeFilterReader.getInstance().readQueryTree(clobBean.getCLOBValue());
			if (optionReplaced(qNode, subprojectOptionMatchMap, classOptionMatchMap)) {
				clobBean.setCLOBValue(TreeFilterWriter.getInstance().toXML(qNode));
				DAOFactory.getFactory().getClobDAO().save(clobBean);
			}
		}		
		InitDatabase.setFilterFields(includeSubproject, includeClass);
	}
		
	private static Map<Integer, List<TWorkItemBean>> getWorkItemsByEntity(
			List<TWorkItemBean> projectWorkItemBeansList, boolean subproject) {	
		Map<Integer, List<TWorkItemBean>> subprojectWorkItemBeansMap = new HashMap<Integer, List<TWorkItemBean>>();
		for (TWorkItemBean workItemBean : projectWorkItemBeansList) {
			Integer entityID = null;
			if (subproject) {
				entityID = workItemBean.getProjectCategoryID();
			} else {
				entityID = workItemBean.getClassID();
			}
			if (entityID!=null) {
				List<TWorkItemBean> subprojectItems = subprojectWorkItemBeansMap.get(entityID);
				if (subprojectItems==null) {
					subprojectItems = new ArrayList<TWorkItemBean>();
					subprojectWorkItemBeansMap.put(entityID, subprojectItems);
				}
				subprojectItems.add(workItemBean);
			}
		}
		return subprojectWorkItemBeansMap;
	}
	
	/**
	 * Saves a project specific configuration and defines the corresponding custom list 
	 * @param fieldID
	 * @param options
	 * @param projectID
	 * @param defaultValue
	 * @param listName
	 * @param optionMatchMap out parameter
	 */
	private static void saveProjectSpecificFieldConfig(Integer fieldID, List<ILabelBean> options,
		Integer projectID, Integer defaultValue, String listName,
		Map<Integer, List<TWorkItemBean>> workItemsByEntityMap, Map<Integer, Integer> optionMatchMap) {
		if (options!=null && !options.isEmpty()) {
			optionMatchMap = new HashMap<Integer, Integer>();
			List<TListBean> existingListBeans = DAOFactory.getFactory().getListDAO().loadByNameInContext(
					listName, TListBean.REPOSITORY_TYPE.PROJECT, projectID);
			Integer listID = null;
			if (existingListBeans==null || existingListBeans.isEmpty()) {
				TListBean listBean = new TListBean();
				listBean.setName(listName);
				listBean.setListType(Integer.valueOf(TListBean.LIST_TYPE.SIMPLE));
				listBean.setRepositoryType(Integer.valueOf(TListBean.REPOSITORY_TYPE.PROJECT));
				listBean.setProject(projectID);
				listID = ListBL.save(listBean);
				LOGGER.info("List saved for fieldID " + fieldID + " name " + listName + " and project " + projectID);
			} else {
				LOGGER.info("List exists for " + fieldID + " name " + listName + " and project " + projectID);
				listID = existingListBeans.get(0).getObjectID();
			}
			Integer fieldConfigID = null;
			TFieldConfigBean fieldConfigBean = (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigDirect(
					null, null, projectID, fieldID);
			if (fieldConfigBean==null) {
				TFieldConfigBean fieldConfigBeanFallBack = (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigFallback(
						null, null, projectID, fieldID);
				//no project specific field configuration existed create a new one 
				fieldConfigBean = new TFieldConfigBean();
				fieldConfigBean.setField(fieldID);
				fieldConfigBean.setLabel(fieldConfigBeanFallBack.getLabel());
				fieldConfigBean.setTooltip(fieldConfigBeanFallBack.getTooltip());
				fieldConfigBean.setRequiredString(Boolean.FALSE/*fieldConfigBeanFallBack.getRequired()*/);
				fieldConfigBean.setHistory(fieldConfigBeanFallBack.getHistory());
				fieldConfigBean.setProject(projectID);
				fieldConfigBean.setDescription(fieldConfigBeanFallBack.getDescription());
				fieldConfigID = FieldConfigBL.save(fieldConfigBean);
				LOGGER.info("Field configuration " + fieldConfigID + " saved for field " + fieldID + " and project " + projectID);
			} else {
				fieldConfigID = fieldConfigBean.getObjectID();
			}
			//make the list assignment specific for project 
			TOptionSettingsBean optionSettingsBean = new TOptionSettingsBean();
			optionSettingsBean.setList(listID);
			optionSettingsBean.setConfig(fieldConfigID);
			DAOFactory.getFactory().getOptionSettingsDAO().save(optionSettingsBean);
			LOGGER.info("Option settings saved for fieldConfig " + fieldConfigID + " and list " + listID);
			LOGGER.info("Migrating options for field " + fieldID + ", a total of " + options.size());
			int i = 1;
			for (ILabelBean optionToMigrate : options) {
				//saves the list options
				boolean defaultOption = defaultValue!=null && defaultValue.equals(optionToMigrate.getObjectID());
				List<TOptionBean> existingOptionBeans = OptionBL.loadByLabel(listID, null, optionToMigrate.getLabel());
				Integer optionID;
				if (existingOptionBeans==null || existingOptionBeans.isEmpty()) {
					TOptionBean optionBean = new TOptionBean();	
					optionBean.setList(listID);
					optionBean.setLabel(optionToMigrate.getLabel());
					optionBean.setDefault(defaultOption);
					optionBean.setSortOrder(i++);
					optionID = OptionBL.save(optionBean);
				} else {
					optionID = existingOptionBeans.get(0).getObjectID();
				}
				optionMatchMap.put(optionToMigrate.getObjectID(), optionID);
				List<TWorkItemBean> workItemBeans = workItemsByEntityMap.get(optionToMigrate.getObjectID());
				if (workItemBeans!=null) {
					LOGGER.info("Migrating field " + fieldID + " option " + optionToMigrate.getLabel() + " for " + workItemBeans.size() + " items");
					for (TWorkItemBean workItemBean : workItemBeans) {
						insertAttributeValue(fieldID, workItemBean.getObjectID(), optionID);
					}
				}
			}
		}
	}
	
	/**
	 * Inserts the attribute value for subproject/class
	 * @param fieldID
	 * @param workItemID
	 * @param attributeValue
	 */
	private static  void insertAttributeValue(Integer fieldID, Integer workItemID, Integer attributeValue) {
		TAttributeValueBean attributeValueBean = AttributeValueBL.loadBeanByFieldAndWorkItemAndParameter(fieldID, workItemID, null);
		if (attributeValueBean==null) {
			attributeValueBean = new TAttributeValueBean();
			attributeValueBean.setField(fieldID);
			attributeValueBean.setWorkItem(workItemID);
			attributeValueBean.setCustomOptionID(attributeValue);
			attributeValueBean.setValidValue(ValueType.CUSTOMOPTION);	
			attributeValueBean.setLastEdit(new Date());
			AttributeValueBL.save(attributeValueBean);
		}
	}
	
	/**
	 * Execute the matcher tree for a workItem
	 * @param qNode
	 * @return
	 */
	private static boolean optionReplaced(QNode qNode,
			Map<Integer, Integer> subprojectOptionMatchMap, Map<Integer, Integer> classOptionMatchMap) {
		if (qNode==null) {
			return false;
		}		
		switch (qNode.getType()) {
		case QNode.OR:
		case QNode.AND:
			boolean replaced = false;
			List<QNode> children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					replaced = replaced || optionReplaced(childNode, subprojectOptionMatchMap, classOptionMatchMap);
				}
			}
			return replaced;
		case QNode.EXP:
			QNodeExpression qNodeExpression = (QNodeExpression)qNode;
			Integer fieldID = qNodeExpression.getField();
			Integer matcherID = qNodeExpression.getMatcherID();
			Object value = qNodeExpression.getValue();
			if (fieldID==null || matcherID==null || value==null) {
				//probably an unknown expression (not yet specified): do not filter by this expression 
				return false;
			}			
			Integer subprojectOption = null;
			if (fieldID.equals(SystemFields.INTEGER_SUBPROJECT)) {
				try {
					subprojectOption =  ((Integer[])value)[0];
				} catch(Exception e) {
					LOGGER.info("Converting the subproject matcher value " + value + " of type " + 
							value.getClass().getName() + " to Integer failed with " + e.getMessage(), e);
				}
				if (subprojectOption!=null) {
					Integer customOption = subprojectOptionMatchMap.get(subprojectOption);
					//if (customOption!=null) {
						qNodeExpression.setValue(customOption);
						return true;
					//}
				}
			}			
			Integer classOption = null;
			if (fieldID.equals(SystemFields.INTEGER_CLASS)) {
				try {
					classOption =  ((Integer[])value)[0];
				} catch(Exception e) {
					LOGGER.info("Converting the class matcher value " + value + " of type " + 
							value.getClass().getName() + " to Integer failed with " + e.getMessage(), e);
				}
				if (classOption!=null) {
					Integer customOption = classOptionMatchMap.get(classOption);
					//if (customOption!=null) {
						qNodeExpression.setValue(customOption);
						return true;
					//}
				}
			}
		}
		return false;
	}
	
	/**
	 * Sets the project with release (for backward compatibility) 
	 * and sets the wbs for the workItems
	 */
	private static void setUseReleaseAndWBS() {
		//add WBS code for each workItem
		List<TProjectBean> allProjectBeans = ProjectBL.loadAll();
		LOGGER.info("Setting WBS in each project");
		for (TProjectBean projectBean : allProjectBeans) {
			//by default the existing projects use release 
			projectBean.setMoreProps(PropertiesHelper.setBooleanProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.USE_RELEASE, Boolean.TRUE));
			ProjectBL.saveSimple(projectBean);
			LOGGER.info("Adding WBS for items from project " + projectBean.getLabel());
			workItemDAO.setWbs(projectBean.getObjectID());
		}
		
	}
	
	
	/**
	 * Add two new permission flags at the database level (none of them is yet implemented in the user interface)
	 */
	private static void migrateRoles() {
		//StringBuffer extendedAccessKey;
		List<TRoleBean> roleBeans = RoleBL.loadAll();
		for (TRoleBean roleBean : roleBeans) {
			String strExtendedAccessKey = roleBean.getExtendedaccesskey().trim();
			if (strExtendedAccessKey!=null && !"".equals(strExtendedAccessKey)){
				//extendedAccessKey = new StringBuffer(strExtendedAccessKey);
				String reducedAccessKey = strExtendedAccessKey.substring(0, AccessBeans.NUMBER_OF_ACCESS_FLAGS);
				roleBean.setExtendedaccesskey(reducedAccessKey);
				RoleBL.save(roleBean);
				Integer roleID = roleBean.getObjectID();
				if (strExtendedAccessKey.length()>AccessBeans.AccessFlagIndexes.ADDMODIFYDELETEBUDGET) {
					char modifyBudget = strExtendedAccessKey.charAt(AccessBeans.AccessFlagIndexes.ADDMODIFYDELETEBUDGET);
					if (modifyBudget==AccessBeans.OFFVALUE) {
						saveFieldRestriction(roleID, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN, TRoleFieldBean.ACCESSFLAG.READ_ONLY);
					}
				}
				if (strExtendedAccessKey.length()>AccessBeans.AccessFlagIndexes.ADDMODIFYDELETEDUEDATES) {
					char modifyDueDates = strExtendedAccessKey.charAt(AccessBeans.AccessFlagIndexes.ADDMODIFYDELETEDUEDATES);
					if (modifyDueDates==AccessBeans.OFFVALUE) {
						saveFieldRestriction(roleID, SystemFields.INTEGER_STARTDATE, TRoleFieldBean.ACCESSFLAG.READ_ONLY);
						saveFieldRestriction(roleID, SystemFields.INTEGER_ENDDATE, TRoleFieldBean.ACCESSFLAG.READ_ONLY);
					}
				}
				if (strExtendedAccessKey.length()>AccessBeans.AccessFlagIndexes.ADDMODIFYDELETEOWNHOURSCOST) {
					char modifyOwnExpenses = strExtendedAccessKey.charAt(AccessBeans.AccessFlagIndexes.ADDMODIFYDELETEOWNHOURSCOST);
					if (modifyOwnExpenses==AccessBeans.OFFVALUE) {
						saveFieldRestriction(roleID, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES, TRoleFieldBean.ACCESSFLAG.NOACCESS);
					}
				}
				if (strExtendedAccessKey.length()>AccessBeans.AccessFlagIndexes.VIEWALLHOURSCOST) {
					char viewAllExpenses = strExtendedAccessKey.charAt(AccessBeans.AccessFlagIndexes.VIEWALLHOURSCOST);
					if (viewAllExpenses==AccessBeans.OFFVALUE) {
						saveFieldRestriction(roleID, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES, TRoleFieldBean.ACCESSFLAG.NOACCESS);
					}
				}
				if (strExtendedAccessKey.length()>AccessBeans.AccessFlagIndexes.ASSIGNTASKTORESPONSIBLEORMANAGER) {
					char changeResponsibleManager = strExtendedAccessKey.charAt(AccessBeans.AccessFlagIndexes.ASSIGNTASKTORESPONSIBLEORMANAGER);
					if (changeResponsibleManager==AccessBeans.OFFVALUE) {
						saveFieldRestriction(roleID, SystemFields.INTEGER_RESPONSIBLE, TRoleFieldBean.ACCESSFLAG.READ_ONLY);
						saveFieldRestriction(roleID, SystemFields.INTEGER_MANAGER, TRoleFieldBean.ACCESSFLAG.READ_ONLY);
					}
				}
				if (strExtendedAccessKey.length()>AccessBeans.AccessFlagIndexes.MODIFYCONSULTANTSINFORMANTANDOTHERWATCHERS) {
					char changeWatchers = strExtendedAccessKey.charAt(AccessBeans.AccessFlagIndexes.MODIFYCONSULTANTSINFORMANTANDOTHERWATCHERS);
					if (changeWatchers==AccessBeans.OFFVALUE) {
						saveFieldRestriction(roleID, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS, TRoleFieldBean.ACCESSFLAG.READ_ONLY);
					}
				}
				if (strExtendedAccessKey.length()>AccessBeans.AccessFlagIndexes.VIEWCONSULTANTSINFORMANTANDOTHERWATCHERS) {
					char seeWatchers = strExtendedAccessKey.charAt(AccessBeans.AccessFlagIndexes.VIEWCONSULTANTSINFORMANTANDOTHERWATCHERS);
					if (seeWatchers==AccessBeans.OFFVALUE) {
						saveFieldRestriction(roleID, FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS, TRoleFieldBean.ACCESSFLAG.NOACCESS);
					}
				}
			}
		}
	}
	
	private static void saveFieldRestriction(Integer roleID, Integer fieldID, Integer accessFlag) {
		TRoleFieldBean roleFieldBean = new TRoleFieldBean();
		roleFieldBean.setRoleKey(roleID);
		roleFieldBean.setFieldKey(fieldID);
		roleFieldBean.setAccessRight(accessFlag);
		FieldsRestrictionsToRoleBL.save(roleFieldBean);
	}

	/**
	 * Set the budget type of all budget records to planned value (bottom up value)
	 */
	private static void setBudgetTypes() {
		DAOFactory.getFactory().getBudgetDAO().updateWithoutBudgetType();
	}
	
	/**
	 * The observer flags for Full trigger will be resetted to avoid sending a large number of e-mails to non RACI persosn
	 */
	public static void migrateFullTrigger() {
		List<TNotifyFieldBean> notifyFieldBeansList = NotifyTriggerBL.getNotifyFieldsForTrigger(Integer.valueOf(1));
		if (notifyFieldBeansList!=null) {
			for (TNotifyFieldBean notifyFieldBean : notifyFieldBeansList) {
				notifyFieldBean.setObserver(false);
				NotifyTriggerBL.save(notifyFieldBean);
			}
		}
	}
}
