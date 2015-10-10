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


package com.aurel.track.admin.projectCopy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.beans.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.GeneralSettings;
import com.aurel.track.accessControl.AccessControlBL;
import com.aurel.track.admin.customize.category.CategoryBL;
import com.aurel.track.admin.customize.category.CategoryFacade;
import com.aurel.track.admin.customize.category.CategoryFacadeFactory;
import com.aurel.track.admin.customize.category.CopyInDescendantException;
import com.aurel.track.admin.customize.category.LeafFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItemCounts;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.customList.CustomListBL;
import com.aurel.track.admin.customize.notify.settings.NotifySettingsBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.admin.customize.treeConfig.screen.ScreenConfigBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.project.ProjectJSON;
import com.aurel.track.admin.project.assignments.AccountAssignmentsBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.projectCopy.ProjectCopyAction.WP_TPL_COPY_TARGET;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectBaseRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;


public class ProjectCopyBL {
	private static final Logger LOGGER = LogManager.getLogger(ProjectCopyBL.class); 
	
	private static interface ASSOCIATED_ENTITY {
		int PROJECT_RIGHTS=1;
		int PROJECT_ISSUE_FILTERS=2;
		int PROJECT_REPORT_TEMPLATES=3;
		int PROJECT_ACCOUNTS=4;	
		int PROJECT_FIELDCONFIGS=5;	
		int PROJECT_SCREENCONFIGS=6;
		int PROJECT_WORKFLOW_CONFIGS=7;
		int PROJECT_NOTIFYSETTINGS=8;
		int PROJECT_DASHBOARDCONFIGS=9;
	}
	
	/**
	 * Render the project copy
	 * @param projectID
	 * @param locale
	 * @return
	 */
	static String loadCopy(Integer projectID, TPersonBean personBean, Locale locale, Integer copyActionTarget) {
		TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
		List<ProjectCopyExpression> associatedEntitiesList = new LinkedList<ProjectCopyExpression>();
		List<ProjectCopyExpression> customListsList = null;
		String projectName = null;
		boolean showAsSibling = false;
		boolean showCopySubprojects = false;
		boolean showCopyReleases = false;
		boolean showCopyItems = false;
		if (projectBean!=null) {
			if(copyActionTarget.intValue() == WP_TPL_COPY_TARGET.COPY_ACTION_TPL_FROM_WP.intValue() ||
				copyActionTarget.intValue() == WP_TPL_COPY_TARGET.COPY_ACTION_WP_FROM_TPL.intValue()) {
				projectName = "";
			}else {
				projectName = LocalizeUtil.getParametrizedString("common.copy", new Object[] {projectBean.getLabel()}, locale);
			}
			if (projectName.length()>35) {
				projectName = projectName.substring(0, 34);
			}			
			if (projectBean.getParent()!=null) {
				showAsSibling = true;
			}
			List<TProjectBean> subprojects = ProjectBL.loadSubrojects(projectID);
			if (subprojects!=null && !subprojects.isEmpty()) {
				showCopySubprojects = true;
			}			
			//releases
			if (hasReleases(projectID)) {
				showCopyReleases = true;
			}
			if (hasOpenIssues(personBean, projectID, locale)) {
				showCopyItems = true;
			}
			//roles
			if (hasRoleAssignments(projectID)) {
				associatedEntitiesList.add(new ProjectCopyExpression(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.rights", locale),
					getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_RIGHTS)));
			}
			//issue filters
			if (hasProjectSpecificCategoryOrLeaf(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY, projectID, locale)) {
				associatedEntitiesList.add(new ProjectCopyExpression(
						LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.issueFilters", locale),
						getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_ISSUE_FILTERS)));
			}
			//report templates
			if (hasProjectSpecificCategoryOrLeaf(CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY, projectID, locale)) {
				associatedEntitiesList.add(new ProjectCopyExpression(
						LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.reportRemplates", locale),
						getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_REPORT_TEMPLATES)));
			}
			//account assignments
			if (hasAccounts(projectID)) {
				associatedEntitiesList.add(new ProjectCopyExpression(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.accounts", locale),
					getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_ACCOUNTS)));
			}
			//field configurations
			if (hasFieldConfigs(projectID)) {
				associatedEntitiesList.add(new ProjectCopyExpression(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.fieldConfigs", locale),
					getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_FIELDCONFIGS)));
			}
			//screen configurations
			if (hasScreeenConfigs(projectID)) {
				associatedEntitiesList.add(new ProjectCopyExpression(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.screenConfigs", locale),
					getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_SCREENCONFIGS)));
			}
			//workflow configurations
			//notify settings
			if (hasProjectSpecificCategoryOrLeaf(CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY, projectID, locale)) {
				associatedEntitiesList.add(new ProjectCopyExpression(
						LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.notifySettings", locale),
						getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_NOTIFYSETTINGS)));
			}
			//project cockpit configurations
			associatedEntitiesList.add(new ProjectCopyExpression(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.copy.lbl.dashboardConfigs", locale),
					getAssociatedEntityMapKeyName(ASSOCIATED_ENTITY.PROJECT_DASHBOARDCONFIGS)));
			//project specific lists
			List<TListBean> projectSpecificLists = ListBL.getListsByProject(projectID, true);
			
			if (projectSpecificLists!=null && !projectSpecificLists.isEmpty()) {
				customListsList = new LinkedList<ProjectCopyExpression>();
				for (TListBean listBean : projectSpecificLists) {
					customListsList.add(new ProjectCopyExpression(listBean.getLabel(),
						getCustomListMapKeyName(listBean.getObjectID())));
				}
			}
		}
		return ProjectCopyJSON.getProjectCopyExpressionListJSON(projectName, associatedEntitiesList, customListsList, showAsSibling, showCopySubprojects, showCopyReleases, showCopyItems);
	}
	
	/**
	 * Generate the name for entity checkboxes 
	 * @param entityID
	 * @return
	 */
	private static String getAssociatedEntityMapKeyName(int entityID) {
		return "associatedEntitiyMap[" + entityID + "]";
	}
	
	/**
	 * Generate the name for custom list checkboxes 
	 * @param listID
	 * @return
	 */
	private static String getCustomListMapKeyName(int listID) {
		return "customListsMap[" + listID + "]";
	}
	
	/**
	 * Saves a copy of a project specified by projectID
	 * copies also the linked entities to the selected entities map 
	 * @param projectID
	 * @param associatedEntitiesMap
	 * @param customListsMap
	 * @param projectName
	 * @param personBean
	 * @param asSibling
	 * @param copySubprojects
	 * @param projectParent
	 * @param copyOpenItems
	 * @param copyAttachments
	 * @param copyReleases
	 * @param locale
	 * @return
	 */
	static String copyProject(Integer projectID, Map<Integer, Boolean> associatedEntitiesMap, 
			Map<Integer, Boolean> customListsMap, String projectName,
			TPersonBean personBean, boolean asSibling, boolean copySubprojects,
			Integer projectParent, boolean copyOpenItems, boolean copyAttachments, boolean copyReleases, Locale locale, boolean recursiveCall, 
			Integer copyActionTarget) {
		TProjectBean projectOriginal = ProjectBL.loadByPrimaryKey(projectID);
		Integer personID = personBean.getObjectID();
		Integer newProjectID = null;
		if (ProjectBL.projectNameExists(projectName, asSibling ? projectOriginal.getParent(): null, null)) {
			return JSONUtility.encodeJSONFailure(LocalizeUtil.getParametrizedString("common.err.duplicateName",
					new Object[] {FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_PROJECT, locale)}, locale));
		}
		//shallow copy
		TProjectBean projectCopy = ProjectBL.copy(projectOriginal, false);
		if(projectCopy.getStatus()==null){
			projectCopy.setStatus(getProjectStatusActive());
		}
		if (asSibling) {
			projectCopy.setParent(projectOriginal.getParent());
		} else {
			projectCopy.setParent(projectParent);
		}
		projectCopy.setLabel(projectName);
		
		if(copyActionTarget.intValue() == WP_TPL_COPY_TARGET.COPY_ACTION_COPY_TPL.intValue() ||
				copyActionTarget.intValue() == WP_TPL_COPY_TARGET.COPY_ACTION_TPL_FROM_WP.intValue()) {
			projectCopy.setIsTemplate(BooleanFields.fromBooleanToString(true));
		}else {
			projectCopy.setIsTemplate(BooleanFields.fromBooleanToString(false));
		}
		if (projectOriginal.getPrefix() != null && !"".equals(projectOriginal.getPrefix())) {
			projectCopy.setPrefix("C-"+projectOriginal.getPrefix());
			if (projectCopy.getPrefix().length() > 50) {
				projectCopy.setPrefix(projectCopy.getPrefix().substring(0,49));
			}
		}
		try {
			newProjectID = ProjectBL.save(projectCopy);
		} catch (Exception e) {
			LOGGER.error("Copying the project with id " + projectID + " failed with " + e.getMessage(), e); 
		}
		
		
		//if the plain project is successfully copied
		if (newProjectID!=null) {
			//copy releases
			Map<Integer, Integer> oldNewReleasesMap = new HashMap<Integer, Integer>();
			if (copyReleases || copyOpenItems) {
				List<TReleaseBean> mainReleases = ReleaseBL.getReleases(projectID, null, true);
				for (TReleaseBean mainReleaseBean : mainReleases) {
					ReleaseBL.copyReleaseHierarchy(newProjectID, null, mainReleaseBean, oldNewReleasesMap);
				}
				//force reloading the releases only after all releases are copied 
				LookupContainer.resetReleaseMap();
			}
			//copy project specific custom lists
			Map<Integer, Integer> copiedListsMap = new HashMap<Integer, Integer>();
			Map<Integer, Integer> copiedOptionsMap = new HashMap<Integer, Integer>();
			if (customListsMap!=null || copyOpenItems) {
				//copy the selected project specific lists
				List<TListBean> projectSpecificLists = ListBL.getListsByProject(projectID, true);
				if (projectSpecificLists!=null && !projectSpecificLists.isEmpty()) {
					for (TListBean listBean : projectSpecificLists) {
						if (copyOpenItems || entitySelected(customListsMap, listBean.getObjectID()) || recursiveCall) {
							//the custom list for descendant projects are not rendered but if there for the parent they were selected (customListsMap!=null) then 
							//we copy also the custom lists specific to the descendant projects 
							ILabelBean labelBean = CustomListBL.getInstance().prepareLabelBean(
									listBean.getObjectID(), listBean.getLabel(), listBean.getDescription(), 
									listBean.getRepositoryType(), newProjectID, personBean);
							Integer newListID = CustomListBL.copyList((TListBean)labelBean, personID, copiedOptionsMap);
							copiedListsMap.put(listBean.getObjectID(), newListID);
						}
					}
				}
			}
			if (associatedEntitiesMap!=null || copyOpenItems) {
				//copy the role assignments if selected
				if (entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_RIGHTS)) {
					List<TAccessControlListBean> acessControlList = AccessControlBL.loadByProject(projectID);
					if (acessControlList!=null) {
						for (TAccessControlListBean accessControlListBeanSrc : acessControlList) {
							//if the primary key is a composed key, not a generated key 
							//(and typically the project is part of the primary key)
							//the generated torque code for copying the 
							//associated entities sets each key components to null,
							//which results in exception during save
							//it means that copying such entities should be made explicitly
							AccessControlBL.save(accessControlListBeanSrc.getPersonID(), newProjectID, accessControlListBeanSrc.getRoleID());
						}
					}
				}
				//copy the filters if selected
				if (entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_ISSUE_FILTERS)) {
					copyProjectSpecificCategory(CategoryBL.CATEGORY_TYPE.ISSUE_FILTER_CATEGORY, projectID, newProjectID, personID, locale);
				}
				//copy the report templates if selected
				if (entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_REPORT_TEMPLATES)) {
					copyProjectSpecificCategory(CategoryBL.CATEGORY_TYPE.REPORT_CATEGORY, projectID, newProjectID, personID, locale);
				}
				//copy the account assignments if selected
				if (entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_ACCOUNTS)) {
					List<TProjectAccountBean> accessControlList = AccountAssignmentsBL.loadAssignmnetsByProject(projectID);
					if (accessControlList!=null) {
						for (TProjectAccountBean projectAccountBeanSrc : accessControlList) {
							//if the primary key is a composed key, not a generated key 
							//(and typically the project is part of the primary key)
							//the generated torque code for copying the 
							//associated entities sets each key components to null,
							//which results in exception during save
							//it means that copying such entities should be made explicitely
							TProjectAccountBean projectAccountBeanDest = new TProjectAccountBean();
							try {
								projectAccountBeanDest.setAccount(projectAccountBeanSrc.getAccount());
								projectAccountBeanDest.setProject(newProjectID);
								AccountAssignmentsBL.save(projectAccountBeanDest);
							} catch (Exception e) {
								LOGGER.error("Saving the account assignments for the copied project failed with " + e.getMessage(), e);
							}	
						}
					}
				}
				//copy the field configurations if selected
				if (copyOpenItems || entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_FIELDCONFIGS)) {
					List<TFieldConfigBean> fieldConfigsList = FieldConfigBL.loadAllByProject(projectID);
					if (fieldConfigsList!=null) {
						for (TFieldConfigBean fieldConfigSrcBean : fieldConfigsList) {
							Integer fieldID = fieldConfigSrcBean.getField();
							//deep true to copy also the settings entries (TTextBoxSettings, TOptionSettings, TGeneralSettings)
							TFieldConfigBean fieldConfigDestBean = FieldConfigBL.copy(fieldConfigSrcBean, true);
							if (fieldConfigDestBean!=null) {
								fieldConfigDestBean.setProject(newProjectID);
								Integer newfieldConfigID = FieldConfigBL.save(fieldConfigDestBean);
								IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);	
								//is it a custom list field?
								if (fieldTypeRT!=null && fieldTypeRT.isCustom() && 
											((fieldTypeRT.isLookup() && fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION) || fieldTypeRT.isComposite())) {
									TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(newfieldConfigID, null);
									if (optionSettingsBean!=null) {
										Integer originalListID = optionSettingsBean.getList();
										if (originalListID!=null) {
											Integer copiedListID = copiedListsMap.get(originalListID);
											if (copiedListID!=null) {
													optionSettingsBean.setList(copiedListID);
													OptionSettingsBL.save(optionSettingsBean);
											}
										}
									}
								}
							}							
						}
					}
				}
				//copy the screen configurations if selected
				if (entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_SCREENCONFIGS)) {
					List<TScreenConfigBean> screenConfigsList = ScreenConfigBL.loadAllByProject(projectID);
					if (screenConfigsList!=null) {
						for (TScreenConfigBean screenConfigSrcBean : screenConfigsList) {
							TScreenConfigBean screenConfigDestBean = ScreenConfigBL.copy(screenConfigSrcBean, false);
							if (screenConfigDestBean!=null) {
								screenConfigDestBean.setProject(newProjectID);
								ScreenConfigBL.save(screenConfigDestBean);
							}
						}
					}
				}
				//copy the workflow assignments if selected
				//copy the automail assignments if selected
				if (entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_NOTIFYSETTINGS)) {
					Map<Integer, Integer> oldToNewLeafIDs = copyProjectSpecificCategory(
							CategoryBL.CATEGORY_TYPE.NOTIFICATION_FILTER_CATEGORY, projectID, newProjectID, personID, locale);
					List<TNotifySettingsBean> automailAssignmentsList = NotifySettingsBL.loadAllByProject(projectID);
					if (automailAssignmentsList!=null) {
						for (TNotifySettingsBean notifySettingsBeanSrc : automailAssignmentsList) {
							TNotifySettingsBean notifySettingsBeanDest = new TNotifySettingsBean();
							notifySettingsBeanDest.setPerson(notifySettingsBeanSrc.getPerson());
							notifySettingsBeanDest.setNotifyTrigger(notifySettingsBeanSrc.getNotifyTrigger());
							notifySettingsBeanDest.setProject(newProjectID);
							Integer notifyFilter = oldToNewLeafIDs.get(notifySettingsBeanSrc.getNotifyFilter());
							if (notifyFilter==null) {
								//if it is not a (recently copied) project specific notify filter then leave the original filter (private or public)
								notifyFilter = notifySettingsBeanSrc.getNotifyFilter();
							}
							notifySettingsBeanDest.setNotifyFilter(notifyFilter);
							NotifySettingsBL.saveNotifySettingsBean(notifySettingsBeanDest);
						}
					}
				}
				if (entitySelected(associatedEntitiesMap, ASSOCIATED_ENTITY.PROJECT_DASHBOARDCONFIGS)) {
					//copy project dashboard
					Integer entityType=SystemFields.PROJECT;
					TDashboardScreenBean originalScreen=DashboardScreenDesignBL.getInstance().loadByProject(personID, projectID, entityType);
					if(originalScreen!=null){
						DashboardScreenDesignBL.getInstance().copyScreen(originalScreen.getObjectID(), personID, newProjectID, entityType);
					}
					//copy release dashboard
					entityType=SystemFields.RELEASESCHEDULED;
					originalScreen=DashboardScreenDesignBL.getInstance().loadByProject(personID, projectID, entityType);
					if(originalScreen!=null){
						DashboardScreenDesignBL.getInstance().copyScreen(originalScreen.getObjectID(), personID, newProjectID, entityType);
					}
				}
			}
			
			if (copyOpenItems) {
				List<ReportBean> reportBeanList = getOpenItems(personBean, newProjectID, locale);
				ReportBeans reportBeans = new ReportBeans(reportBeanList, locale, null, false);
				List<ReportBean> parentFirstFlatList = reportBeans.getItems();
				List<TWorkItemBean> workItemBeans = new LinkedList<TWorkItemBean>();
				List<Integer> workItemIDs = new LinkedList<Integer>();
				for (ReportBean reportBean : parentFirstFlatList) {
					TWorkItemBean workItemBean = reportBean.getWorkItemBean();
					workItemBeans.add(workItemBean);
					workItemIDs.add(workItemBean.getObjectID());
				}
				//gather the user defined custom list fields 
				Set<Integer> selectedFieldsSet = new HashSet<Integer>();
				selectedFieldsSet.add(SystemFields.INTEGER_PROJECT);
				selectedFieldsSet.add(SystemFields.INTEGER_RELEASENOTICED);
				selectedFieldsSet.add(SystemFields.INTEGER_RELEASESCHEDULED);
				Set<Integer> customListFieldsSet = new HashSet<Integer>();
				Set<Integer> compositeListFieldsSet = new HashSet<Integer>();
				List<TFieldBean> customFields = FieldBL.loadCustom();
				if (customFields!=null) {
					for (TFieldBean fieldBean : customFields) {
						Integer fieldID = fieldBean.getObjectID();
						IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
						if (fieldTypeRT!=null && fieldTypeRT.isLookup() && fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION) {
							customListFieldsSet.add(fieldID);
						} else {
							if (fieldTypeRT.isComposite()) {
								//FIXME: now each composite field is a composite custom select: change this if other composite types will be defined
								customListFieldsSet.add(fieldID);
								compositeListFieldsSet.add(fieldID);
							}
						}
					}
				}
				selectedFieldsSet.addAll(customListFieldsSet);
				Map<Integer, Set<Integer>> projectIssueTypeContexts = ItemBL.getProjectIssueTypeContexts(workItemBeans);
				Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap = 
						FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
							projectIssueTypeContexts, selectedFieldsSet, locale, null, null);
				//get the fieldSettings for each fieldConfigBean
				Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap =
						FieldRuntimeBL.getFieldSettingsForFieldConfigs(projectsIssueTypesFieldConfigsMap);
				Map<Integer, WorkItemContext> workItemContextsMap = FieldsManagerRT.createImportContext(
						workItemBeans, selectedFieldsSet, projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap, null, null, personID, locale);
				for (TWorkItemBean workItemBean : workItemBeans) {
					workItemBean.setProjectID(newProjectID);
					Integer origReleaseNoticedID = workItemBean.getReleaseNoticedID();
					if (origReleaseNoticedID!=null) {
						Integer newReleaseNoticed = oldNewReleasesMap.get(origReleaseNoticedID);
						if (newReleaseNoticed!=null) {
							workItemBean.setReleaseNoticedID(newReleaseNoticed);
						}
					}
					Integer origReleaseScheduledID = workItemBean.getReleaseScheduledID();
					if (origReleaseScheduledID!=null) {
						Integer newReleasScheduledID = oldNewReleasesMap.get(origReleaseScheduledID);
						if (newReleasScheduledID!=null) {
							workItemBean.setReleaseScheduledID(newReleasScheduledID);
						}
					}
					for (Integer customListFieldID : customListFieldsSet) {
						Object originalValue = workItemBean.getAttribute(customListFieldID);
						if (originalValue!=null) {
							if (compositeListFieldsSet.contains(customListFieldID)) {
								IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(customListFieldID);
								CustomCompositeBaseRT customCompositeBaseRT = (CustomCompositeBaseRT)fieldTypeRT;
								for (int i=0; i<customCompositeBaseRT.getNumberOfParts();i++) {
									Integer parameterCode = new Integer(i+1);
									originalValue = workItemBean.getAttribute(customListFieldID, parameterCode);
									if (originalValue!=null) {
										Integer[] originalValueArr = CustomSelectUtil.getSelectedOptions(originalValue);
										if (originalValueArr!=null && originalValueArr.length>0) {
											List<Integer> newValuesList = new LinkedList<Integer>();
											for (Integer optionID : originalValueArr) {
												Integer copiedOption = copiedOptionsMap.get(optionID);
												if (copiedOption!=null) {
													newValuesList.add(copiedOption);
												}
											}
											workItemBean.setAttribute(customListFieldID, parameterCode, newValuesList.toArray());
										}
									}
								}
							} else {
								Integer[] originalValueArr = CustomSelectUtil.getSelectedOptions(originalValue);
								if (originalValueArr!=null && originalValueArr.length>0) {
									List<Integer> newValuesList = new LinkedList<Integer>();
									for (Integer optionID : originalValueArr) {
										Integer copiedOption = copiedOptionsMap.get(optionID);
										if (copiedOption!=null) {
											newValuesList.add(copiedOption);
										}
									}
									workItemBean.setAttribute(customListFieldID, newValuesList.toArray());
								}
							}
						}
					}
				}
				Map<Integer, Integer> originalToCopyWorkItemIDs = new HashMap<Integer, Integer>();	
				for (Integer workItemID : workItemIDs) {
					//is the workItem selected for change
					WorkItemContext workItemContext = workItemContextsMap.get(workItemID);
					if (workItemContext!=null) {
						List<ErrorData> errorList = new LinkedList<ErrorData>();
						TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
						workItemBean.setDeepCopy(false);
						workItemBean.setCopyAttachments(copyAttachments);
						workItemBean.setCopyWatchers(false);
						workItemBean.setCopyChildren(false);
						Integer newWorkItemID = FieldsManagerRT.copy(workItemContext, null, originalToCopyWorkItemIDs, errorList, false, false);
						if (newWorkItemID!=null) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Item " + workItemBean.getSynopsis() + " copied with itemID " + newWorkItemID);
							}
						} else {
							LOGGER.warn("Copying the item " + workItemBean.getSynopsis() + " failed");
							if (LOGGER.isDebugEnabled() && !errorList.isEmpty()) {
								for (ErrorData errorData : errorList) {
									LOGGER.debug(ErrorHandlerJSONAdapter.createMessage(errorData, locale));
								}
							}
						}
					}
				}
			}
			//recursively save the subprojects
			if (copySubprojects) {
				List<TProjectBean> subprojects = ProjectBL.loadSubrojects(projectID);
				for (TProjectBean subprojectBean : subprojects) {
					copyProject(subprojectBean.getObjectID(), associatedEntitiesMap, customListsMap,
							subprojectBean.getLabel(), personBean, false, copySubprojects, newProjectID, copyOpenItems, copyAttachments, copyReleases, locale, true, copyActionTarget);
				}
			}
		}
		String projectNodeToReload = null;
		if (asSibling && projectOriginal.getParent()!=null) {
			projectNodeToReload = projectOriginal.getParent().toString();
		}
		String projectNodeToSelect = newProjectID.toString();
		String projectConfigTypeNodeToSelect = String.valueOf(ProjectConfigBL.PROJECT_ASSIGNMENTS.GENERAL);
		return ProjectJSON.saveProjectDetailJSON(projectNodeToReload, projectNodeToSelect,
				true, projectConfigTypeNodeToSelect, true);
	}
	
	/**
	 * 
	 * @return
	 */
	private static Set<Integer> getCustomListFields() {
		Set<Integer> cutsomListFieldsSet = new HashSet<Integer>();
		List<TFieldBean> customFields = FieldBL.loadCustom();
		if (customFields!=null) {
			for (TFieldBean fieldBean : customFields) {
				Integer fieldID = fieldBean.getObjectID();
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.isCustom() && fieldTypeRT.isLookup() && fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION) {
					CustomSelectBaseRT customSelectBaseRT = (CustomSelectBaseRT)fieldTypeRT;
					if (!customSelectBaseRT.isCustomPicker()) {
						cutsomListFieldsSet.add(fieldID);
					}
				}
			}
		}
		return cutsomListFieldsSet;
	}
	
	private static boolean entitySelected(Map<Integer, Boolean> selectedEntitiesMap, int entityID) {
		if (selectedEntitiesMap==null) {
			return false;
		}
		Boolean entitySelected = selectedEntitiesMap.get(entityID);
		if (entitySelected!=null && entitySelected.booleanValue()) {
			return true;
		}
		return false;
	}
	
	private static List<ReportBean> getOpenItems(TPersonBean personBean, Integer projectID, Locale locale) {
		FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(true, projectID, true, true, false);
		try {
			return LoadTreeFilterItems.getTreeFilterReportBeans(filterUpperTO, null, null, personBean, locale);
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Number of items to load " + e.getItemCount());
			return new LinkedList<ReportBean>();
		}
	}
	
	/**
	 * Whether there are open issues in the project
	 * @param personBean
	 * @param projectID
	 * @return
	 */
	private static boolean hasOpenIssues(TPersonBean personBean, Integer projectID, Locale locale) {
		FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(true, projectID, true, true, false);
		int numberOfOpenItems = LoadTreeFilterItemCounts.countTreeFilterProjectRoleItems(filterUpperTO, personBean, locale, GeneralSettings.getMaxItems());
		return numberOfOpenItems>0;
	}
	
	/**
	 * Whether the project has releases
	 * @param projectID
	 * @return
	 */
	private static boolean hasReleases(Integer projectID) {
		List<TReleaseBean> mainReleases = ReleaseBL.getReleases(projectID, null, true);
		return mainReleases!=null && !mainReleases.isEmpty();
	}
	
	/**
	 * Whether the project has role assignments
	 * @param projectID
	 * @return
	 */
	private static boolean hasRoleAssignments(Integer projectID) {
		List<TAccessControlListBean> acessControlList = AccessControlBL.loadByProject(projectID);
		return acessControlList!=null && !acessControlList.isEmpty();
	}
	
	/**
	 * Whether the project has associated accounts
	 * @param projectID
	 * @return
	 */
	private static boolean hasAccounts(Integer projectID) {
		List<TProjectAccountBean> accessControlList = AccountAssignmentsBL.loadAssignmnetsByProject(projectID);
		return accessControlList!=null && !accessControlList.isEmpty();
	}
	
	/**
	 * Whether the project has specific fieldConfigs
	 * @param projectID
	 * @return
	 */
	private static boolean hasFieldConfigs(Integer projectID) {
		List<TFieldConfigBean> fieldConfigsList = FieldConfigBL.loadAllByProject(projectID);
		return fieldConfigsList!=null && !fieldConfigsList.isEmpty();
	}
	
	/**
	 * Whether the project has specific screenConfigs
	 * @param projectID
	 * @return
	 */
	private static boolean hasScreeenConfigs(Integer projectID) {
		List<TScreenConfigBean> screenConfigsList = ScreenConfigBL.loadAllByProject(projectID);
		return screenConfigsList!=null && !screenConfigsList.isEmpty();
	}
	
	/**
	 * Whether the project has specific screenConfigs
	 * @param projectID
	 * @return
	 */
	private static boolean hasWorkflowConfigs(Integer projectID) {
		return false;
	}
	
	
	/**
	 * Whether the project has project specific filter, report
	 * @param categoryType
	 * @param srcProjectID
	 * @param locale
	 * @return
	 */
	private static boolean hasProjectSpecificCategoryOrLeaf(String categoryType, Integer srcProjectID, Locale locale) {
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		//first the categories in alphabetical order
		List<ILabelBean> projectCategories = categoryFacade.getRootObjects(CategoryBL.REPOSITORY_TYPE.PROJECT, srcProjectID, null, locale);
		if (projectCategories!=null && !projectCategories.isEmpty()) {
			return true;
		}
		//then the leafs in alphabetical order
		List<ILabelBean> projectFiltersWithoutCategory = leafFacade.getRootObjects(CategoryBL.REPOSITORY_TYPE.PROJECT, srcProjectID, null, locale);
		if (projectFiltersWithoutCategory!=null && !projectFiltersWithoutCategory.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Copies all categories/leafs (issue/notify filters, report templates) below a project
	 * @param categoryType
	 * @param srcProjectID
	 * @param personID
	 * @param locale
	 */
	private static Map<Integer, Integer> copyProjectSpecificCategory(String categoryType,
			Integer srcProjectID, Integer destProjectID, Integer personID, Locale locale) {
		Map<Integer, Integer> oldToNewLeafIDs = new HashMap<Integer, Integer>();
		int projectRepository = CategoryBL.REPOSITORY_TYPE.PROJECT;
		CategoryFacade categoryFacade = CategoryFacadeFactory.getInstance().getCategoryFacade(categoryType);
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);					
		//first the categories in alphabetical order
		List<ILabelBean> projectCategories = categoryFacade.getRootObjects(CategoryBL.REPOSITORY_TYPE.PROJECT, srcProjectID, null, locale);
		for (ILabelBean iLabelBean : projectCategories) {
			try {
				CategoryBL.copy(categoryType, CategoryBL.TYPE.CATEGORY, CategoryBL.TYPE.PROJECT,
					projectRepository, projectRepository, iLabelBean.getObjectID(), destProjectID, personID, true, locale, oldToNewLeafIDs);
			} catch (CopyInDescendantException e) {
				LOGGER.error("Copy descendant for categoty type " + categoryType + " by category copy");
			}
		}
		//then the leafs in alphabetical order
		List<ILabelBean> projectFiltersWithoutCategory = leafFacade.getRootObjects(CategoryBL.REPOSITORY_TYPE.PROJECT, srcProjectID, null, locale);
		for (ILabelBean iLabelBean : projectFiltersWithoutCategory) {
			try {
				CategoryBL.copy(categoryType, CategoryBL.TYPE.LEAF, CategoryBL.TYPE.PROJECT,
						projectRepository, projectRepository, iLabelBean.getObjectID(), destProjectID, personID, true, locale, oldToNewLeafIDs);				
			} catch (CopyInDescendantException e) {
				LOGGER.error("Copy descendant for categoty type " + categoryType + " by leaf copy");
			}
		}
		return oldToNewLeafIDs;
	}

	public static Integer getProjectStatusActive(){
		List<TSystemStateBean> statusOptions = LookupContainer.getSystemStateList(TSystemStateBean.ENTITYFLAGS.PROJECTSTATE);
		for(TSystemStateBean status:statusOptions){
			Integer projectStatus=status.getStateflag();
			if(projectStatus.intValue()==TSystemStateBean.STATEFLAGS.ACTIVE){
				return status.getObjectID();
			}
		}
		return null;
	}
}
