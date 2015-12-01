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


package com.aurel.track.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.ProjectConfigBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SystemStateDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.exchange.excel.ExcelImportNotUniqueIdentifiersException;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.system.text.SystemProjectSpecificIssueNoRT;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.linkType.ILinkType.PARENT_CHILD_EXPRESSION;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.screen.SystemActions;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;


public class ItemBL {

	private static final Logger LOGGER = LogManager.getLogger(ItemBL.class);
	private static WorkItemDAO workItemDAO=DAOFactory.getFactory().getWorkItemDAO();
	private static SystemStateDAO systemStateDAO=DAOFactory.getFactory().getSystemStateDAO();

	public static boolean itemIsNew(Integer actionID) {
		return actionID!=null &&
			(SystemActions.NEW==actionID.intValue() ||
			SystemActions.NEW_CHILD==actionID.intValue() ||
			SystemActions.NEW_LINKED_ITEM==actionID.intValue() ||
			SystemActions.COPY==actionID.intValue());
	}

	public static boolean isMove(Integer actionID) {
		return actionID!=null &&
			SystemActions.MOVE==actionID.intValue();
	}
	/**
	 * Get the system attributes for the saved workItem
	 * @param workItemID
	 * @return
	 */
	public static TWorkItemBean loadWorkItemSystemAttributes(Integer workItemID) {
		//load the system fields from database
		TWorkItemBean workItemBean = null;
		try {
			workItemBean = workItemDAO.loadByPrimaryKey(workItemID);
		} catch (ItemLoaderException e) {
			LOGGER.info("Loading the workItem by loadWorkItemSystemAttributes() failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return workItemBean;
	}

	public static TWorkItemBean loadWorkItemSystemAttributesByProjectSpecificID(Integer projectID,Integer projectSpecificID) {
		//load the system fields from database
		TWorkItemBean workItemBean = null;
		try {
			workItemBean=workItemDAO.loadByProjectSpecificID(projectID,projectSpecificID);
		} catch (ItemLoaderException e) {
			LOGGER.info("Loading the workItem by loadWorkItemSystemAttributesByProjectSpecificID() failed with " + e.getMessage(), e);
		}
		return workItemBean;
	}

	/**
	 * Loads the workItems by and array of IDs
	 * @param workItemIDs
	 * @return
	 */
	public static List<TWorkItemBean> loadByWorkItemKeys(int[] workItemIDs) {
		return workItemDAO.loadByWorkItemKeys(workItemIDs);
	}

	/**
	 * Get the system attributes for the saved workItem
	 * @param
	 * @return
	 * @throws ExcelImportNotUniqueIdentifiersException
	 */
	public static TWorkItemBean loadWorkItemSystemAttributes(Map<Integer, Object> identifierFieldValues) throws ExcelImportNotUniqueIdentifiersException {
		TWorkItemBean workItemBean = null;
		workItemBean = workItemDAO.loadBySecondaryKeys(identifierFieldValues);
		return workItemBean;
	}

	/**
	 * Load the custom attributes for the saved workItem
	 * @param workItemBean
	 */
	public static Map<String, Object> loadWorkItemCustomAttributes(TWorkItemBean workItemBean) {
		return AttributeValueBL.loadWorkItemCustomAttributes(workItemBean);
	}

	/**
	 * Gets the last workItem created by a person
	 * @param personID
	 * @return
	 */
	public static TWorkItemBean loadLastCreated(Integer personID) {
		TWorkItemBean workItemBean = workItemDAO.loadLastCreated(personID);
		return workItemBean;
	}

	/**
	 * Get all the issue from an project, include the closed/archived/deleted issues.
	 */
	public static List<TWorkItemBean> loadAllByProject(Integer projectID, Integer archived, Integer deleted) {
		return workItemDAO.loadAllByProject(projectID, archived, deleted);
	}

	/**
	 * Prepares the errorData in case of no edit permission exists
	 * To avoid the copy-paste code of building the error messages in each edit-related action
	 * @param personID
	 * @param workItemBean
	 * @return
	 */
	public static ErrorData hasEditPermission(Integer personID, TWorkItemBean workItemBean) {
		if (!AccessBeans.isAllowedToChange(workItemBean, personID)) {
			List<ErrorParameter> errorParameters = new ArrayList<ErrorParameter>();
			ErrorParameter errorParameter = new ErrorParameter(ItemBL.getItemNo(workItemBean));
			errorParameters.add(errorParameter);
			ErrorData errorData = new ErrorData("report.reportError.error.noEditRight", errorParameters);
			return errorData;
		}
		return null;
	}
	/**
	 * Prepares the errorData in case of no read permission exists
	 * To avoid the copy-paste code of building the error messages in each edit-related action
	 * @param personID
	 * @param workItemBean
	 * @return
	 */
	public static ErrorData hasReadPermission(Integer personID, TWorkItemBean workItemBean) {
		if (!AccessBeans.isAllowedToRead(workItemBean, personID)) {
			List<ErrorParameter> errorParameters = new ArrayList<ErrorParameter>();
			ErrorParameter errorParameter = new ErrorParameter(ItemBL.getItemNo(workItemBean));
			errorParameters.add(errorParameter);
			ErrorData errorData = new ErrorData("report.reportError.error.noReadRight", errorParameters);
			return errorData;
		}
		return null;
	}


	public static boolean canMoveItemTo(Integer personID,Integer projectID,Integer issueTypeID){
		return AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID,AccessFlagIndexes.MODIFYANYTASK, true, true);
	}
	public static boolean canCreateItemIn(Integer personID,Integer projectID,Integer issueTypeID){
		return AccessBeans.hasPersonRightInProjectForIssueType(personID, projectID, issueTypeID,AccessFlagIndexes.CREATETASK, true, true);
	}


	public static WorkItemContext getWorkItemContext(Integer actionID,Integer workItemID,
			Integer projectID,Integer issueTypeID, Integer stateID, boolean accessLevelFlag,
			Integer person,Locale locale){
		return getWorkItemContext(actionID,workItemID,projectID,issueTypeID,stateID,accessLevelFlag,person,locale,true);
	}
	public static WorkItemContext getWorkItemContext(Integer actionID,Integer workItemID,
			Integer projectID,Integer issueTypeID, Integer stateID, boolean accessLevelFlag,
			Integer person,Locale locale,boolean dropDownsNeeded){
		if(actionID==null){
			//edit by default but actionID should be never null
			return editWorkItem(workItemID, person, locale,dropDownsNeeded);
		}
		switch (actionID.intValue()) {
		case SystemActions.PRINT:
			return viewWorkItem(workItemID, person, locale,dropDownsNeeded);
		case SystemActions.EDIT:
			return editWorkItem(workItemID, person, locale,dropDownsNeeded);
		case SystemActions.COPY:
			return editCopyWorkItem(workItemID, person, locale);
		case SystemActions.CHANGE_STATUS:
			return loadWorkItem4ChangeStatus(workItemID, person, locale);
		case SystemActions.MOVE:
			return moveItem(workItemID, person, locale, projectID, issueTypeID, stateID);
		case SystemActions.NEW:
			if(projectID==null){
				return null;
			}
			return createNewItem(projectID, issueTypeID, person, accessLevelFlag, locale);
		case SystemActions.NEW_CHILD:
			return createNewItemChild(projectID, issueTypeID, accessLevelFlag ,person, locale);
		}
		//other possible actions supposed to be special edits
		return loadWorkItem(workItemID, person, locale, actionID);
	}

	public static WorkItemContext createNewItem(Integer projectID,Integer issueTypeID,Integer person,boolean accessLevelFlag,Locale locale){
		return createNewItem(projectID, issueTypeID, person, accessLevelFlag, locale, true);
	}
	public static WorkItemContext createNewItem(Integer projectID,Integer issueTypeID,Integer person,boolean accessLevelFlag,Locale locale,boolean includeDropDowns){
		return FieldsManagerRT.create(person, projectID, issueTypeID, accessLevelFlag, locale,includeDropDowns);
	}
	public static WorkItemContext createNewItemChild(Integer projectID,Integer issueTypeID, boolean accessLevelFlag,
			Integer person,Locale locale){
		return FieldsManagerRT.createChild(person, projectID, issueTypeID, accessLevelFlag, locale);
	}
	public static WorkItemContext moveItem(Integer workItemID,Integer person,Locale locale,Integer projectID,Integer issueTypeID, Integer stateID){
		return FieldsManagerRT.move(workItemID,person,locale,projectID,issueTypeID,stateID);
	}
	public static WorkItemContext editWorkItem(Integer workItemID,Integer person,Locale locale,boolean dropDownsNeeded){
		return FieldsManagerRT.viewOrEdit(person, workItemID, false, locale,dropDownsNeeded);
	}
	public static WorkItemContext viewWorkItem(Integer workItemID,Integer person,Locale locale,boolean dropDownsNeeded){
		return FieldsManagerRT.viewOrEdit(person, workItemID, true, locale,dropDownsNeeded);
	}
	public static WorkItemContext editCopyWorkItem(Integer workItemID,Integer person,Locale locale){
		return FieldsManagerRT.editCopy(person, workItemID,locale);
	}
	public static WorkItemContext loadWorkItem4ChangeStatus(Integer workItemID,Integer person,Locale locale){
		return FieldsManagerRT.changeStatus(person, workItemID,locale);
	}
	public static WorkItemContext loadAllFields(Integer workItemID,Integer person,Locale locale){
		return FieldsManagerRT.loadAllFields(person, workItemID,locale);
	}
	public static Integer copyWorkItem(WorkItemContext workItemContext, List<ErrorData> errorList, boolean withNotify){
		return FieldsManagerRT.copy(workItemContext, null, new HashMap<Integer, Integer>(), errorList, true, withNotify);
	}
	public static WorkItemContext loadWorkItem(Integer workItemID,Integer person,Locale locale,Integer actionID){
		return FieldsManagerRT.loadWorkItem(person, workItemID,locale,actionID);
	}


	public static Integer saveWorkItem(WorkItemContext workItemContext,List<ErrorData> errorList,Integer actionID, boolean confirm){
		if(actionID==null){
			return saveWorkItem(workItemContext, errorList, confirm);
		}
		switch (actionID.intValue()) {
		case SystemActions.COPY:
			return copyWorkItem(workItemContext, errorList, true);
		case SystemActions.MOVE:
			return moveAndSaveWorkItem(workItemContext, errorList, confirm);
		default:
			return saveWorkItem(workItemContext, errorList, confirm);
		}
	}
	public static Integer saveWorkItem(WorkItemContext workItemContext,List<ErrorData> errorList, boolean confirm){
		TWorkItemBean workItem=workItemContext.getWorkItemBean();
		LOGGER.debug("The workItem to be saved " + workItem);
		return FieldsManagerRT.save(workItemContext, confirm, errorList, true);
	}

	public static Integer moveAndSaveWorkItem(WorkItemContext workItemContext,List<ErrorData> errorList, boolean confirm){
		TWorkItemBean workItem=workItemContext.getWorkItemBean();
		LOGGER.debug("The workItem to be saved " + workItem);
		return FieldsManagerRT.move(workItemContext, confirm, errorList, true);
	}
	/**
	 * Loads the workitem by itemID
	 * @param itemID
	 * @return
	 * @throws ItemLoaderException
	 */
	public static TWorkItemBean loadWorkItem(Integer itemID) throws ItemLoaderException {
		TWorkItemBean  workItemBean=workItemDAO.loadByPrimaryKey(itemID);
		return workItemBean;
	}

	/**
	 * Loads the workitem by itemID with custom fields
	 * @param itemID
	 * @return
	 * @throws ItemLoaderException
	 */
	public static TWorkItemBean loadWorkItemWithCustomFields(Integer itemID) {
		TWorkItemBean  workItemBean = loadWorkItemSystemAttributes(itemID);
		if (workItemBean!=null) {
			ItemBL.loadWorkItemCustomAttributes(workItemBean);
		}
		return workItemBean;
	}

	/**
	 * Add a comment after the last child was closed and optionally close the item
	 * @param parentID
	 * @param statusID
	 * @param comment
	 * @param closeParent
	 * @param personID
	 * @param locale
	 */
	public static void addCommentAfterLastItemClosed(Integer parentID, Integer statusID, String comment, boolean closeParent, Integer personID, Locale locale) {
		if (parentID!=null) {
			List<TWorkItemBean> openChildren = workItemDAO.getOpenChildren(parentID, null);
			if (openChildren==null || openChildren.isEmpty()) {
				WorkItemContext workItemContext = editWorkItem(parentID, personID, locale, false);
				TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
				if (workItemBean!=null) {
					workItemBean.setComment(comment);
					if (closeParent) {
						workItemBean.setStateID(statusID);
					}
					saveWorkItem(workItemContext, new LinkedList<ErrorData>(), false);
					addCommentAfterLastItemClosed(workItemBean.getSuperiorworkitem(), statusID, comment, closeParent, personID, locale);
				}
			}
		}
	}

    /**
     * Gets the item's ID depending on whether projectSpecificIDs are activated or not
	 * @deprecated use getItemNo with TWorkItemBean
     * @param workItemID
     * @return
     */
    public static String getItemNo(Integer workItemID) {
        if (workItemID==null) {
            return "";
        }
        if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
            TWorkItemBean workItemBean = null;
            try {
                workItemBean = ItemBL.loadWorkItem(workItemID);
            } catch (ItemLoaderException e)  {
                LOGGER.warn("Loading the workItemBean by workItemID " + workItemID + " failed with " + e.getMessage());
                LOGGER.debug(ExceptionUtils.getStackTrace(e));
            }
            if (workItemBean==null) {
                return "";
            } else {
                return SystemProjectSpecificIssueNoRT.getShowValue(workItemBean.getIDNumber(), workItemBean);
            }
        } else {
            return workItemID.toString();
        }
    }

    /**
     * Gets the item's ID depending on whether projectSpecificIDs are activated or not
     * @param workItemBean
     * @return
     */
    public static String getItemNo(TWorkItemBean workItemBean) {
        if (workItemBean==null) {
            return "";
        }
        if (ApplicationBean.getInstance().getSiteBean().getProjectSpecificIDsOn()) {
            return SystemProjectSpecificIssueNoRT.getShowValue(workItemBean.getIDNumber(), workItemBean);
        } else {
            return workItemBean.getObjectID().toString();
        }
    }

	/**
	 * Loads a workItemBean by project specific ID.
	 * @param projectID the projectID from the excel or the default
	 * @param mappedProject the projectID is from excel
	 * @param projectBeansMap
	 * @param projectSpecificID
	 * @return
	 * @throws ItemLoaderException
	 */
	public static TWorkItemBean loadWorkItemByProjectSpecificID(Integer projectID, boolean mappedProject, Map<Integer, TProjectBean> projectBeansMap, String projectSpecificID) {
		if (projectSpecificID!=null) {
			Integer projectFound = null;
			Integer idNumberFound = null;
			if (projectID!=null && mappedProject) {
				//the project identified from the mapped excel column
				TProjectBean projectBean = projectBeansMap.get(projectID);
				if (projectBean!=null) {
					String label = projectBean.getLabel();
					projectFound = projectID;
					String projectPrefix = projectBean.getPrefix();
					if (projectPrefix==null || "".equals(projectPrefix)) {
						try {
							//because the project is identified we can deal also with the error prone case when no project prefix is specified
							idNumberFound = Integer.parseInt(projectSpecificID);
						} catch(Exception e) {
							LOGGER.warn("Parsing " + projectSpecificID + " to integer failed with " + e.getMessage());
						}
					} else {
						String intStr = projectSpecificID.substring(projectPrefix.length());
						if (intStr!=null) {
							try {
								idNumberFound = Integer.parseInt(intStr.trim());
							} catch(Exception e) {
								LOGGER.warn("Parsing " + projectSpecificID + " to integer failed with " + e.getMessage());
							}
						}
					}
					if (projectFound!=null && idNumberFound!=null){
						TWorkItemBean workItemBean = null;
						try {
							workItemBean = workItemDAO.loadByProjectSpecificID(projectFound, idNumberFound);
						} catch (ItemLoaderException e) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Loading the workItem by projectPrefix " + projectPrefix + " and number part " + idNumberFound + " faild with " + e.getMessage());
								LOGGER.error(ExceptionUtils.getStackTrace(e));
							}
						}
						if (workItemBean!=null) {
							LOGGER.debug("Workitem  with ID " + projectSpecificID +  " found in project " + label + " with id " + projectID);
							return workItemBean;
						} else {
							LOGGER.debug("Workitem  with ID " + projectSpecificID +  " not found in project " + label + " with id " + projectID);
						}
					}
				}
			}
			//the project is not identified in the mapped excel column: either because no excel column was mapped for project or
			//although mapped, no projectID was specified in the excel column or
			//according to projectSpecificIDs the items are in another projects as projectID and consequently a project move is expected
			for (TProjectBean projectBean : projectBeansMap.values()) {
				projectID = projectBean.getObjectID();
				String projectPrefix = projectBean.getPrefix();
				if (projectPrefix!=null && !"".equals(projectPrefix) && projectSpecificID.startsWith(projectPrefix)){
					String idNumberStr = projectSpecificID.substring(projectPrefix.length());
					Integer idNumber=null;
					if (idNumberStr!=null) {
						try {
							idNumber = Integer.parseInt(idNumberStr.trim());
							projectID = projectBean.getObjectID();
							String label = projectBean.getLabel();
							TWorkItemBean workItemBean=null;
							if (projectID!=null && idNumber!=null){
								workItemBean=workItemDAO.loadByProjectSpecificID(projectID,idNumber);
								if (workItemBean!=null) {
									LOGGER.debug("Workitem  with ID " + projectSpecificID +  " found in project " + label + " with id " + projectID);
									if (projectID!=null && mappedProject) {
										LOGGER.debug("Workitem  with ID " + projectSpecificID +  " will be moved to project " + label + " with id " + projectID);
									}
									return workItemBean;
								}
							}
							break;
						}catch (Exception ex) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Loading by projectPrefix " + projectPrefix + " and number part " + idNumberStr + " faild with " + ex.getMessage());
								LOGGER.error(ExceptionUtils.getStackTrace(ex));
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static TWorkItemBean loadWorkItemByProjectSpecificID(Integer personID,String projectSpecificID) throws ItemLoaderException {
		List<TProjectBean> projects = ProjectBL.loadUsedProjectsFlat(personID);
		Integer projectID=null;
		Integer idNumber=null;
		TProjectBean projectBean;
		String projectPrefix;
		for(int i=0;i<projects.size();i++){
			projectBean=projects.get(i);
			projectPrefix=projectBean.getPrefix();
			if(projectPrefix!=null&&projectSpecificID.startsWith(projectPrefix)){
				int idx=projectSpecificID.indexOf(projectPrefix);
				Integer tmpID=null;
				try{
					tmpID=Integer.parseInt(projectSpecificID.substring(idx+projectPrefix.length()));
					projectID=projectBean.getObjectID();
					idNumber=tmpID;
					break;
				}catch (Exception ex){
				}
			}
		}
		TWorkItemBean  workItemBean=null;
		if(projectID!=null&&idNumber!=null){
			workItemBean=workItemDAO.loadByProjectSpecificID(projectID,idNumber);
		}
		return workItemBean;
	}

	/**
	 * Gets the workitem by projectSpecificID
	 * @param projectSpecificID
	 * @return
	 * @throws ItemLoaderException
	 */
	public static TWorkItemBean loadWorkItemByProjectSpecificID(String projectSpecificID) throws ItemLoaderException {
		if (projectSpecificID!=null) {
			StringBuilder prefixBuilder = new StringBuilder();
			StringBuilder idBuilder = new StringBuilder();
			for (int i = 0; i < projectSpecificID.length(); i++) {
				char charValue = projectSpecificID.charAt(i);
				if (Character.isDigit(charValue)) {
					idBuilder.append(charValue);
				} else {
					if (idBuilder.length()>0) {
						prefixBuilder.append(idBuilder);
						idBuilder = new StringBuilder();
					}
					prefixBuilder.append(charValue);
				}
			}
			String prefix = prefixBuilder.toString();
			Integer idNumber=null;
			try {
				idNumber = Integer.parseInt(idBuilder.toString());
			} catch (Exception ex){
			}
			List<TProjectBean> projetctsWithPrefix = ProjectBL.loadByPrefix(prefix);
			if (projetctsWithPrefix!=null) {
				for (TProjectBean projectBean : projetctsWithPrefix) {
					TWorkItemBean workItemBean=workItemDAO.loadByProjectSpecificID(projectBean.getObjectID(),idNumber);
					if (workItemBean!=null) {
						return workItemBean;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Load an project
	 * @param workItemKey
	 * @param personID
	 * @return
	 */
	public static boolean workItemExists(Integer workItemKey, Integer personID){
		TWorkItemBean workItemBean = null;
		try {
			workItemBean = workItemDAO.loadByPrimaryKey(workItemKey);
		} catch (ItemLoaderException e) {
			LOGGER.warn("Loading the workItem by workItemExists() faield with " + e.getMessage());
		}
		if(workItemBean==null){
			return false;
		}
		if (workItemBean.isArchivedOrDeleted()) {
			return PersonBL.isProjectAdmin(personID, workItemBean.getProjectID());
		}
		return true;
	}

	/**
	 * Get the parent synopsis
	 * @param workItem
	 * @return
	 */
	public static String getParentSynopsis(TWorkItemBean workItem) {
		Integer itemID=workItem.getSuperiorworkitem();
		return getParentSynopsis(itemID);
	}
	public static String getParentSynopsis(Integer itemID) {
		String parentSynopsis="";
		if(itemID!=null){
			TWorkItemBean parent = null;
			try {
				parent = workItemDAO.loadByPrimaryKey(itemID);
			} catch (ItemLoaderException e) {
				LOGGER.error("Getting the parent failed with " + e.getMessage());
			}
			if(parent!=null){
				parentSynopsis=parent.getSynopsis();
			}
		}
		return parentSynopsis;
	}

	public static  List<TWorkItemBeanNode> getChildrenTree(Integer workItemID,Integer personID){
		List<TWorkItemBeanNode> result=new ArrayList<TWorkItemBeanNode>();
		List<TWorkItemBean> workItemBeanList=getChildren(workItemID, personID);
		if(workItemBeanList!=null && !workItemBeanList.isEmpty()){
			for(TWorkItemBean workItemBean:workItemBeanList){
				TWorkItemBeanNode node=new TWorkItemBeanNode(workItemBean);
				node.setChildren(getChildrenTree(workItemBean.getObjectID(),personID));
				result.add(node);
			}
		}
		return result;

	}
	public static  List<TWorkItemBeanNode> getChildrenTreeByWBS(ReportBeans reportBeans){
		List<TWorkItemBeanNode> result=new ArrayList<TWorkItemBeanNode>();
		List<ReportBean> items=reportBeans.getReportBeansFirstLevel();
		if(items!=null && !items.isEmpty()){
			result=getChildrenTree(items);
		}
		return result;
	}
	private static List<TWorkItemBeanNode> getChildrenTree(List<ReportBean> items){
		List<TWorkItemBeanNode> result=new ArrayList<TWorkItemBeanNode>();
		for(ReportBean reportBean:items) {
			TWorkItemBean workItemBean=reportBean.getWorkItemBean();
			TWorkItemBeanNode node=new TWorkItemBeanNode(workItemBean);
			List<ReportBean> children=reportBean.getChildren();
			if(children!=null && !children.isEmpty()){
				node.setChildren(getChildrenTree(children));
			}
			result.add(node);
		}
		return result;
	}

	public static List<TWorkItemBean> getChildren(Integer workItemID){
		if(workItemID==null){
			return new LinkedList<TWorkItemBean>();
		}
		return workItemDAO.getChildren(workItemID);
	}

	public static boolean hasChildren(Integer workItemID) {
		List<TWorkItemBean> children = getChildren(workItemID);
		if (children==null || children.isEmpty()) {
			return false;
		}
		return true;
	}


	public static List<TWorkItemBean> getChildren(Integer workItemID, Integer personID){
		if(workItemID==null){
			return new LinkedList<TWorkItemBean>();
		}
		List<TWorkItemBean> workItemList = workItemDAO.getChildren(workItemID);
		if (workItemList==null || workItemList.isEmpty()) {
			return new LinkedList<TWorkItemBean>();
		}
		int[] workItemIDs = GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromBeanList(workItemList));
		List<TWorkItemBean> workItemBeans = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(workItemIDs, personID, false, false, false);
		Collections.sort(workItemBeans, new Comparator<TWorkItemBean>() {
			public int compare(TWorkItemBean workItemBean1, TWorkItemBean workItemBean2) {
				Date startDate1 = workItemBean1.getStartDate();
				if (startDate1==null) {
					startDate1 = workItemBean1.getCreated();
				}
				Date startDate2 = workItemBean2.getStartDate();
				if (startDate2==null) {
					startDate2 = workItemBean2.getCreated();
				}
				return startDate1.compareTo(startDate2);
			}
		});
		return workItemBeans;
	}

	/**
	 * Whether
	 * @param childID
	 * @param ascendentID
	 * @return
	 */
	public static boolean isAscendant(Integer ascendentID, Integer childID) {
		Integer parentID = childID;
		while (parentID!=null) {
			TWorkItemBean parentWorkItem = null;
			try {
				parentWorkItem = ItemBL.loadWorkItem(parentID);
			} catch (ItemLoaderException e) {
			}
            if (parentWorkItem==null) {
                LOGGER.warn("The parent workItem " + parentID + " for the workItem " + ascendentID + " does not exist");
                return false;
            }  else {
				parentID = parentWorkItem.getSuperiorworkitem();
				if (parentID!=null && parentID.equals(ascendentID)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * extension for viewing responsible-name in childrens-issue list
	 * by Andreas Hemetsberger, Amatic Industries GmbH, 27.07.2010
	 */
	public static Map<Integer, TPersonBean> getChildrenResponsiblesMap(List<TWorkItemBean> children) {
		Set<Integer> responsibleIDs = new HashSet<Integer>();
		if (children != null && children.size() > 0) {
			Iterator<TWorkItemBean> itrWorkItemBean = children.iterator();
			while (itrWorkItemBean.hasNext()) {
				TWorkItemBean workItemBean = itrWorkItemBean.next();
				responsibleIDs.add(workItemBean.getResponsibleID());
			}
		}
		if (!responsibleIDs.isEmpty()) {
			return GeneralUtils.createMapFromList(PersonBL.loadByKeys(
					GeneralUtils.createIntegerListFromCollection(responsibleIDs)));
		} else {
			return new HashMap<Integer, TPersonBean>();
		}
	}

	public static Map<Integer, TPersonBean> getChildrenOriginatorsMap(List<TWorkItemBean> children) {
		Set<Integer> originatorsIDs = new HashSet<Integer>();
		if (children != null && children.size() > 0) {
			Iterator<TWorkItemBean> itrWorkItemBean = children.iterator();
			while (itrWorkItemBean.hasNext()) {
				TWorkItemBean workItemBean = itrWorkItemBean.next();
				originatorsIDs.add(workItemBean.getOriginatorID());
			}
		}
		if (!originatorsIDs.isEmpty()) {
			return GeneralUtils.createMapFromList(PersonBL.loadByKeys(
					GeneralUtils.createIntegerListFromCollection(originatorsIDs)));
		} else {
			return new HashMap<Integer, TPersonBean>();
		}
	}

	/**
	 * accounting tab is typically always visible
	 * @return
	 */
	public static boolean getShowAccountingTab(TWorkItemBean workItem, Integer userID){
		if (!ApplicationBean.getInstance().isGenji()) {
			Integer projectID=workItem.getProjectID();
			Integer issueTypeID=workItem.getListTypeID();
			ProjectAccountingTO projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
			if (projectAccountingTO.isWorkTracking() || projectAccountingTO.isCostTracking()) {
				return AccessBeans.showAccountingTab(userID, projectID, issueTypeID);
			}
		}
		return false;
	}

	/**
	 * Gets the direct children for an array of workItemIDs
	 * @param workItemIDs
	 * @param notClosed
	 * @param archived
	 * @param deleted
	 * @param itemTypesList
	 * @return
	 */
	public static List<TWorkItemBean> getChildren(int[] workItemIDs, boolean notClosed, Integer archived, Integer deleted, List<Integer> itemTypesList){
		return workItemDAO.getChildren(workItemIDs, notClosed, archived, deleted, itemTypesList);
	}

	public static Set<Integer> getChildHierarchy(int[] baseWorkItemIDsArr) {
		return getChildHierarchy(baseWorkItemIDsArr,PARENT_CHILD_EXPRESSION.ALL_CHILDREN, null, null, null);
	}

	/**
	 * Get all descendants which are not included in the original query result
	 * @param baseWorkItemIDsArr
	 * @param direction
	 * @param archived
	 * @param deleted
	 * @param itemTypesList
	 * @return
	 */
	public static Set<Integer> getChildHierarchy(int[] baseWorkItemIDsArr,
			Integer direction, Integer archived, Integer deleted, List<Integer> itemTypesList) {
		Set<Integer> allDescendentIDsSet = new HashSet<Integer>();
		Set<Integer> directChildrenIDsSet;
		int[] directChildrenArr = baseWorkItemIDsArr;
		Set<Integer> toRemoveSet = GeneralUtils.createSetFromIntArr(baseWorkItemIDsArr);
		do {
			//get the next level of children workItems
			List<TWorkItemBean> directChildrenWorkItems = getChildren(
					directChildrenArr, PARENT_CHILD_EXPRESSION.ALL_NOT_CLOSED_CHILDREN==direction,
					archived, deleted, itemTypesList);
			//get the ID set of children
			directChildrenIDsSet = GeneralUtils.createIntegerSetFromBeanList(directChildrenWorkItems);
			//remove the children which are already present in the gathered list (theoretically remove might occur only first time)
			directChildrenIDsSet.removeAll(toRemoveSet);

			//gather the not yet present children to the list
			allDescendentIDsSet.addAll(directChildrenIDsSet);
			toRemoveSet.addAll(directChildrenIDsSet);
			directChildrenArr = GeneralUtils.createIntArrFromSet(directChildrenIDsSet);
		} while (directChildrenIDsSet!=null && !directChildrenIDsSet.isEmpty());
		return allDescendentIDsSet;
	}

	/**
	 * Get all descendants which are not included in the original query result
	 * @param baseWorkItemIDsArr
	 * @return
	 */
	public static List<TWorkItemBean> getAncestorItems(int[] baseWorkItemIDsArr) {
		List<TWorkItemBean> ancestorWorkItemBeans = new LinkedList<TWorkItemBean>();
		Set<Integer> allDescendentIDsSet = new HashSet<Integer>();
		Set<Integer> directChildrenIDsSet;
		int[] directChildrenArr = baseWorkItemIDsArr;
		Set<Integer> toRemoveSet = GeneralUtils.createSetFromIntArr(baseWorkItemIDsArr);
		do {
			//get the next level of children workItems
			List<TWorkItemBean> childWorkItemBeans = getChildren(
					directChildrenArr, false, null, null, null);
			ancestorWorkItemBeans.addAll(childWorkItemBeans);
			//get the ID set of children
			directChildrenIDsSet = GeneralUtils.createIntegerSetFromBeanList(childWorkItemBeans);
			//remove the children which are already present in the gathered list (theoretically remove might occur only first time)
			directChildrenIDsSet.removeAll(toRemoveSet);

			//gather the not yet present children to the list
			allDescendentIDsSet.addAll(directChildrenIDsSet);
			toRemoveSet = allDescendentIDsSet;
			directChildrenArr = GeneralUtils.createIntArrFromSet(directChildrenIDsSet);
		} while (directChildrenIDsSet!=null && !directChildrenIDsSet.isEmpty());
		return ancestorWorkItemBeans;
	}

	public static void updateItemLocationFormLabels(ItemLocationForm form,Locale locale,Integer projectID, Integer issueTypeID, TPersonBean user){
		TFieldBean fieldBean;
		String tooltip;
		Set<Integer> fieldIDs = new HashSet<Integer>();
		fieldIDs.add(SystemFields.INTEGER_PROJECT);
		fieldIDs.add(SystemFields.INTEGER_ISSUETYPE);
		Map<Integer, TFieldConfigBean> fieldConfigs = FieldRuntimeBL.getLocalizedFieldConfigs(fieldIDs, projectID, issueTypeID, locale);

		//project tooltip
		TFieldConfigBean projectConfigBean = fieldConfigs.get(SystemFields.INTEGER_PROJECT);
		String projectLabel = projectConfigBean.getLabel();
		fieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_PROJECT);
		tooltip=projectConfigBean.getTooltip();
		if(tooltip==null){
			tooltip="";
		}
		String projectTooltip = fieldBean.getName() + " : " + tooltip;

		//issueType tooltip
		TFieldConfigBean issueTypeConfigBean = fieldConfigs.get(SystemFields.INTEGER_ISSUETYPE);
		String issueTypeLabel = issueTypeConfigBean.getLabel();
		fieldBean = FieldBL.loadByPrimaryKey(SystemFields.INTEGER_ISSUETYPE);
		tooltip=issueTypeConfigBean.getTooltip();
		if(tooltip==null){
			tooltip="";
		}
		String issueTypeTooltip = fieldBean.getName() + " : " + tooltip;
		form.setProjectLabel(projectLabel);
		form.setProjectTooltip(projectTooltip);

		form.setIssueTypeLabel(issueTypeLabel);
		form.setIssueTypeTooltip(issueTypeTooltip);
	}
	public static ItemLocationForm getItemLocation(Locale locale, TPersonBean user,
			Integer defaultProjectID, Integer defaultIssueTypeID, Integer parentWorkItemID) {
		LOGGER.debug("getItemLocation() defaultProjectID="+defaultProjectID+", defaultIssueTypeID="+defaultIssueTypeID);
		ItemLocationForm form=new ItemLocationForm();
		Integer personID=user.getObjectID();
        List<Integer> accessRights = new LinkedList<Integer>();
        accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.CREATETASK));
        int[] rights = GeneralUtils.createIntArrFromIntegerList(accessRights);
        List<TreeNode> projectTree = ProjectBL.getProjectNodesByRightEager(true, user, false, rights, false, false);
        form.setProjectTree(projectTree);
		if(projectTree==null || projectTree.isEmpty()){
			return null;
		}
		Integer projectID=null;
		Integer issueTypeID=null;
		TWorkItemBean defaultWorkItemBean = ItemBL.loadLastCreated(personID);
		//projectID
		if(defaultProjectID==null&&defaultWorkItemBean!=null){
			defaultProjectID=defaultWorkItemBean.getProjectID();
		}
        List<Integer> projectIDs = ProjectBL.getProjectIDsFromTree(projectTree);
		if(defaultProjectID!=null){
			Iterator<Integer> iterator = projectIDs.iterator();
			while (iterator.hasNext()) {
                Integer projectIDCrt = iterator.next();
				if (projectIDCrt.equals(defaultProjectID)) {
					projectID=defaultProjectID;
					break;
				}
			}
		}
        if (projectID==null && !projectIDs.isEmpty()) {
            projectID = projectIDs.get(0);
        }
		List<TListTypeBean> issueTypeList = IssueTypeBL.loadByPersonAndProjectAndCreateRight(
				personID, projectID, null, parentWorkItemID, locale);
		if(issueTypeList==null || issueTypeList.isEmpty()){
			LOGGER.warn("No issue type is allowed in project " + projectID + " for person " + personID + " parent workItem " + parentWorkItemID);
		} else {
			//issueTypeID
			if(defaultIssueTypeID==null){
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				Integer prefill = null;
				if (projectBean!=null) {
					prefill = ProjectConfigBL.getPrefillValue(projectBean);
				}
				if (prefill!=null && prefill.intValue()==TProjectBean.PREFILL.PROJECTDEFAULT) {
					defaultIssueTypeID = ProjectConfigBL.getDefaultFieldValueForProject(SystemFields.INTEGER_ISSUETYPE, projectBean, personID, issueTypeID, null);
				} else {
					if (defaultWorkItemBean!=null) {
						defaultIssueTypeID = defaultWorkItemBean.getListTypeID();
					}
				}
			}
			Iterator<TListTypeBean> iterator = issueTypeList.iterator();
			while (iterator.hasNext()) {
				TListTypeBean issueTypeBean = iterator.next();
				if (issueTypeBean.getObjectID().equals(defaultIssueTypeID)) {
					issueTypeID = defaultIssueTypeID;
					break;
				}
			}
			if(issueTypeID==null){
				issueTypeID=issueTypeList.get(0).getObjectID();
			}
		}
		form.setProjectID(projectID);
		//form.setProjectList(GeneralUtils.createIntegerStringBeanListFromLabelBeanList(projectList));

		form.setIssueTypeID(issueTypeID);
		form.setIssueTypeList(GeneralUtils.createIntegerStringBeanListFromLabelBeanList(issueTypeList));

		updateItemLocationFormLabels(form,locale,projectID,issueTypeID,user);

		return form;
	}
	public static ItemLocationForm getItemLocationFixedIssueType(Locale locale, TPersonBean user,Integer defaultProjectID, Integer issueTypeID) {
		ItemLocationForm form=new ItemLocationForm();
		Integer projectID=null;
		Integer personID=user.getObjectID();
		TWorkItemBean defaultWorkItemBean = ItemBL.loadLastCreated(personID);
		//projectID
		if(defaultProjectID==null&&defaultWorkItemBean!=null){
			defaultProjectID=defaultWorkItemBean.getProjectID();
		}
		List<TreeNode> projectTree = ProjectBL.loadProjectsWithCreateIssueRightForItemType(personID, issueTypeID);
		form.setProjectTree(projectTree);
		if(projectTree==null || projectTree.isEmpty()){
			return null;
		}
		List<Integer> projectIDs = ProjectBL.getProjectIDsFromTree(projectTree);
		if(defaultProjectID!=null){
			Iterator<Integer> iterator = projectIDs.iterator();
			while (iterator.hasNext()) {
				Integer projectIDCrt = iterator.next();
				if (projectIDCrt.equals(defaultProjectID)) {
					projectID=defaultProjectID;
					break;
				}
			}
		}
		if (projectID==null && !projectIDs.isEmpty()) {
			projectID = projectIDs.get(0);
		}
		form.setProjectID(projectID);
		form.setIssueTypeID(issueTypeID);
		List<IntegerStringBean> fixedList=new ArrayList<IntegerStringBean>();
		TListTypeBean listTypeBean=LookupContainer.getItemTypeBean(issueTypeID, locale);
		String localizedTex = listTypeBean.getLabel();
		fixedList.add(new IntegerStringBean(localizedTex,listTypeBean.getObjectID()));
		form.setIssueTypeList(fixedList);
		form.setFixedIssueType(true);
		updateItemLocationFormLabels(form, locale, projectID, issueTypeID, user);
		return form;
	}

	public static String formatIssueTitle(WorkItemContext workItemCtx,boolean  useProjectSpecificID){
		StringBuilder sb=new StringBuilder();
		TWorkItemBean workItemBean=workItemCtx.getWorkItemBean();
		if(workItemBean.getObjectID()!=null){
			sb.append("<strong><span class=\"emphasize\">&nbsp;");
			String id="";
			if(useProjectSpecificID){
				id=ItemBL.getSpecificProjectID(workItemCtx);
			}else{
				id=workItemBean.getObjectID().toString();
			}
			sb.append(id).append("</span>&nbsp;:&nbsp;");
			String statusDisplay=getStatusDisplay(workItemCtx);
			sb.append("<span class=\"dataEmphasize\">").append(statusDisplay).append("</span>");
			sb.append("&nbsp;:&nbsp;").append(workItemBean.getSynopsis()).append("</strong>");
		}
		return sb.toString();
	}
	public static String getStatusDisplay(WorkItemContext workItemCtx) {
		TWorkItemBean workItemBean=workItemCtx.getWorkItemBean();
		if(workItemBean!=null){
			Integer stateID = workItemBean.getStateID();
			ILabelBean stateBean=null;
				stateBean=LookupContainer.getStatusBean(stateID,workItemCtx.getLocale());
			if(stateBean!=null){
				return stateBean.getLabel();
			}
		}
		return "";
	}
	public static String getStatusDisplay(TWorkItemBean workItemBean,Locale locale) {
		if(workItemBean!=null){
			Integer stateID = workItemBean.getStateID();
			ILabelBean stateBean=null;
			stateBean=LookupContainer.getStatusBean(stateID,locale);
			if(stateBean!=null){
				return stateBean.getLabel();
			}
		}
		return "";
	}

	public static String getIssueTypeDisplay(WorkItemContext workItemCtx) {
		TWorkItemBean workItemBean=workItemCtx.getWorkItemBean();
		if(workItemBean!=null){
			Integer issueTypeID = workItemBean.getListTypeID();
			ILabelBean issueTypeBean=null;
				issueTypeBean=LookupContainer.getItemTypeBean(issueTypeID,workItemCtx.getLocale());
			if(issueTypeBean!=null){
				return issueTypeBean.getLabel();
			}
		}
		return "";
	}
	public static String getIssueTypeDisplay(TWorkItemBean workItemBean,Locale locale) {
		if(workItemBean!=null){
			Integer issueTypeID = workItemBean.getListTypeID();
			ILabelBean issueTypeBean=null;
			issueTypeBean=LookupContainer.getItemTypeBean(issueTypeID,locale);
			if(issueTypeBean!=null){
				return issueTypeBean.getLabel();
			}
		}
		return "";
	}
	public static String getProjectDisplay(TWorkItemBean workItemBean,Locale locale) {
		if(workItemBean!=null){
			Integer projectID = workItemBean.getProjectID();
			ILabelBean projectBen=null;
			projectBen=LookupContainer.getProjectBean(projectID);
			if(projectBen!=null){
				return projectBen.getLabel();
			}
		}
		return "";
	}


	public static TScreenBean loadFullRuntimeScreenBean(Integer screenID){
		return ItemScreenCache.getInstance().getScreen(screenID);
	}

	/**
	 * Gets the project specific issueIDs map by general issueIDs
	 * @param workItemBeans
	 * @return
	 */
	public static Map<Integer, String> getProjectSpecificIssueIDsMap(List<TWorkItemBean> workItemBeans) {
		Map<Integer, String> idMap = new HashMap<Integer, String>();
		if (workItemBeans!=null) {
			for (TWorkItemBean workItemBean : workItemBeans) {
				Integer workItemID = workItemBean.getObjectID();
				Integer projectID = workItemBean.getProjectID();
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				String projectPrefix = null;
				if (projectBean!=null) {
					projectPrefix = projectBean.getPrefix();
				}
				Integer projectSpecificIssueNo = workItemBean.getIDNumber();
				idMap.put(workItemID, ItemBL.getSimpleOrPorjectSpecificItemNo(true, projectPrefix, projectSpecificIssueNo));
			}
		}
		return idMap;
	}

	public static String getSpecificProjectID(TWorkItemBean workItemBean){
		TProjectBean projectBean = LookupContainer.getProjectBean(workItemBean.getProjectID());
		String projectPrefix = null;
		if (projectBean!=null) {
			projectPrefix = projectBean.getPrefix();
		}
		Integer projectSpecificIssueNo = workItemBean.getIDNumber();
		return ItemBL.getSimpleOrPorjectSpecificItemNo(true, projectPrefix, projectSpecificIssueNo);
	}

	public static String getSpecificProjectID(WorkItemContext workItemContext) {
		TProjectBean projectBean = null;
		Integer projectSpecificIssueNo = null;
		TWorkItemBean workItemBean = workItemContext.getWorkItemBean();
		if (workItemBean!=null) {
			projectSpecificIssueNo = workItemBean.getIDNumber();
			Integer projectID = workItemBean.getProjectID();
			if (projectID!=null) {
				projectBean = LookupContainer.getProjectBean(projectID);
			}
		}
		String projectPrefix = null;
		if (projectBean!=null) {
			projectPrefix = projectBean.getPrefix();
		}
		return ItemBL.getSimpleOrPorjectSpecificItemNo(true, projectPrefix, projectSpecificIssueNo);
	}

	/**
	 * Gets all combinations for project issue type in the selected workItemBeans
	 * @param selectedWorkItemBeans
	 * @return
	 */
	public static Map<Integer, Set<Integer>> getProjectIssueTypeContexts(
			List<TWorkItemBean> selectedWorkItemBeans) {
		Map<Integer, Set<Integer>> projectsIssueTypesMap = new HashMap<Integer, Set<Integer>>();
		for (TWorkItemBean workItemBean : selectedWorkItemBeans) {
			Integer projectID = workItemBean.getProjectID();
			Integer issueTypeID = workItemBean.getListTypeID();
			Set<Integer> issueTypesSet = projectsIssueTypesMap.get(projectID);
			if (issueTypesSet==null) {
				issueTypesSet = new HashSet<Integer>();
				projectsIssueTypesMap.put(projectID, issueTypesSet);
			}
			issueTypesSet.add(issueTypeID);
		}
		return projectsIssueTypesMap;
	}

	/**
	 * Compute the dates/planned values/expenses for all summary items
	 */
	public static void computeSummaryItems() {
		List<TWorkItemBean> workItemBeansWithParent = workItemDAO.loadAllWithParent();
		Set<Integer> workItemIDs = new HashSet<Integer>();
		Set<Integer> parentIDs = new HashSet<Integer>();
		for (TWorkItemBean workItemBean : workItemBeansWithParent) {
			workItemIDs.add(workItemBean.getObjectID());
			parentIDs.add(workItemBean.getSuperiorworkitem());
		}
		//get only the top level parents
		parentIDs.removeAll(workItemIDs);
		List<TWorkItemBean> topLevelParents = null;
		if (!parentIDs.isEmpty()) {
			LOGGER.debug("Computing summary date / planned value / remaining plan / totl expense for a number of " + parentIDs.size() + " top level workItems...");
			topLevelParents = loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(parentIDs));
			workItemBeansWithParent.addAll(topLevelParents);
		}
		Map<Integer, TWorkItemBean> workItemBeansMap = GeneralUtils.createMapFromList(workItemBeansWithParent);
		Map<Integer, List<Integer>> parentToChildrenMap = getParentToChildrenMap(workItemBeansWithParent);
		if(topLevelParents!=null){
			for (TWorkItemBean workItemBean : topLevelParents) {
				LOGGER.debug("Computing summary date and planned value for workItem " + workItemBean.getObjectID());
				Map<Integer, Double> hoursPerWorkdayForProject = new HashMap<Integer, Double>();
				//compute planned works
				ComputedValueBL.computeBottomUpValues(workItemBean, parentToChildrenMap,
						workItemBeansMap, TComputedValuesBean.EFFORTTYPE.TIME,
						TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, hoursPerWorkdayForProject);
				//compute planned costs
				ComputedValueBL.computeBottomUpValues(workItemBean, parentToChildrenMap,
						workItemBeansMap, TComputedValuesBean.EFFORTTYPE.COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, hoursPerWorkdayForProject);
				//compute work expenses
				ComputedValueBL.computeBottomUpValues(workItemBean, parentToChildrenMap,
						workItemBeansMap, TComputedValuesBean.EFFORTTYPE.TIME,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, hoursPerWorkdayForProject);
				//compute cost expenses
				ComputedValueBL.computeBottomUpValues(workItemBean, parentToChildrenMap,
						workItemBeansMap, TComputedValuesBean.EFFORTTYPE.COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, hoursPerWorkdayForProject);
				ComputedValueBL.computeBottomUpPersonValues(workItemBean, parentToChildrenMap,
						workItemBeansMap, TComputedValuesBean.EFFORTTYPE.TIME,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, hoursPerWorkdayForProject);
				ComputedValueBL.computeBottomUpPersonValues(workItemBean, parentToChildrenMap,
						workItemBeansMap, TComputedValuesBean.EFFORTTYPE.COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, hoursPerWorkdayForProject);
				RemainingPlanBL.computeBottomUpRemainingPlannedValues(workItemBean, parentToChildrenMap,
						workItemBeansMap, hoursPerWorkdayForProject);
			}
		}
		LOGGER.debug("Summary date and planned values computed");
	}

	/**
	 * Gets the parent to children map
	 * @param workItemBeansWithParent
	 * @return
	 */
	public static Map<Integer, List<Integer>>  getParentToChildrenMap(List<TWorkItemBean> workItemBeansWithParent) {
		Map<Integer, List<Integer>> parentToChildrenMap = new HashMap<Integer, List<Integer>>();
		for (TWorkItemBean workItemBean : workItemBeansWithParent) {
			Integer workItemID = workItemBean.getObjectID();
			Integer parentID = workItemBean.getSuperiorworkitem();
			if (parentID!=null) {
				List<Integer> childrenList = parentToChildrenMap.get(parentID);
				if (childrenList==null) {
					childrenList = new LinkedList<Integer>();
					parentToChildrenMap.put(parentID, childrenList);
				}
				childrenList.add(workItemID);
			}
		}
		return parentToChildrenMap;
	}
	/**
	 * Reset the computed values for planned values and expenses for all parent items to their own values
	 * (not computed from ancestors) after the summary behavior was reset
	 * computed value for planned value: actualized to the last planned value from history (if any)
	 * computed value for expenses: the sum of expenses for the workItem (if any was introduces before summary behavior was activated)
	 * Not really typical to set and then reset the summary behavior but if it is the case, the original values
	 * (before activating the summary behavior) should be possible to restore.
	 * Exception is the remaining planned value which is not historicized consequently it remains as calculated based on summary behavior
	 * TODO in this case do we have to calculate it as the planned value - total expenses?
	 */
	public static void resetSummaryItems() {
		List<TWorkItemBean> workItemBeansWithParent = workItemDAO.loadAllWithParent();
		Set<Integer> parentIDs = new HashSet<Integer>();
		for (TWorkItemBean workItemBean : workItemBeansWithParent) {
			parentIDs.add(workItemBean.getSuperiorworkitem());
		}
		List<Integer> workItemIDs = GeneralUtils.createIntegerListFromCollection(parentIDs);
		ComputedValueBL.resetExpenseSumToDirect(workItemIDs);
		ComputedValueBL.resetPlannedValuesToLastFromHistory(workItemIDs);
		RemainingPlanBL.resetRemainingPlanTo100Percent(workItemIDs);
	}

	/**
	 * Loads the watcher workItems for a person by project oe release if specified
	 * @param personID
	 * @param projectID
	 * @param releaseID
	 * @return
	 */


	public static boolean isSynopsysReadonly(WorkItemContext workItemContext){
		Integer accesFlag=null;
		if(workItemContext.getFieldRestrictions()!=null){
			accesFlag=workItemContext.getFieldRestrictions().get(SystemFields.INTEGER_SYNOPSIS);
		}
		boolean readonly=false;
		if(accesFlag!=null){
			if(accesFlag.intValue()== TRoleFieldBean.ACCESSFLAG.NOACCESS){
				readonly=true;
			}else{
				readonly = (accesFlag.intValue() == TRoleFieldBean.ACCESSFLAG.READ_ONLY);
			}
		}
		return  readonly;
	}

	/**
	 * Gets the list of workItems which should appear in the reminder email
	 * @param personID
	 * @param remindMeAsOriginator
	 * @param remindMeAsManager
	 * @param remindMeAsResponsible
	 * @param dateLimit
	 * @return
	 */
	public static List<TWorkItemBean> loadReminderWorkItems(Integer personID,
		String remindMeAsOriginator, String remindMeAsManager, String remindMeAsResponsible, Date dateLimit) {
		return workItemDAO.loadReminderWorkItems(personID, remindMeAsOriginator, remindMeAsManager, remindMeAsResponsible, dateLimit);
	}



	/**
	 * Whether the project is active
	 * @param projectID
	 * @return
	 */
	public static boolean projectIsActive(Integer projectID){
		return systemStateDAO.getStatusFlag(projectID, TSystemStateBean.ENTITYFLAGS.PROJECTSTATE) == TSystemStateBean.STATEFLAGS.ACTIVE;
	}

	/**
	 * Gets the item hierarchy for the workItem result set
	 * @param workItemBeansList
	 * @return
	 */
	public static LocalLookupContainer getItemHierarchyContainer(List<TWorkItemBean> workItemBeansList) {
		LocalLookupContainer localLookupContainer = new LocalLookupContainer();
		Map<Integer, TWorkItemBean> selectedAndParentWorkItemsMap = new HashMap<Integer, TWorkItemBean>();
		Map<Integer, Integer> workItemIDToParentIDMap = getWorkItemIDToParentIDMap(workItemBeansList, selectedAndParentWorkItemsMap);
		localLookupContainer.setWorkItemToParentMap(workItemIDToParentIDMap);
		localLookupContainer.setWorkItemsMap(selectedAndParentWorkItemsMap);
		return localLookupContainer;
	}

	/**
	 * Load the workItemID to parentID map
	 * Gets also parents not present in the workItemBeanList because the wbs needs the entire parent hierarchy
	 * @param workItemBeanListBase
	 * @param selectedAndTheirParents
	 */
	public static Map<Integer, Integer> getWorkItemIDToParentIDMap(List<TWorkItemBean> workItemBeanListBase,
			Map<Integer, TWorkItemBean> selectedAndTheirParents) {
		Map<Integer, Integer> workItemIDToParentIDMap = new HashMap<Integer, Integer>();
		if (workItemBeanListBase!=null) {
			for (TWorkItemBean workItemBean : workItemBeanListBase) {
				Integer workItemID = workItemBean.getObjectID();
				Integer parentID = workItemBean.getSuperiorworkitem();
				if (parentID!=null) {
					workItemIDToParentIDMap.put(workItemID, parentID);
				}
				selectedAndTheirParents.put(workItemID, workItemBean);
			}
		}
		//gather the parents which are not in the query result
		Set<Integer> notPresentParents = new HashSet<Integer>();
		for (Integer parentID : workItemIDToParentIDMap.values()) {
			if (!workItemIDToParentIDMap.containsKey(parentID) &&
					!selectedAndTheirParents.containsKey(parentID)) {
				//the second part is to avoid infinite cycle in case of inconsistent parent data
				notPresentParents.add(parentID);
			}
		}
		if (!notPresentParents.isEmpty()) {
			List<TWorkItemBean> parentsList = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(notPresentParents));
			workItemIDToParentIDMap.putAll(getWorkItemIDToParentIDMap(parentsList, selectedAndTheirParents));
		}
		return workItemIDToParentIDMap;
	}

	/**
	 * Returns the show value for issue no.: if workspace specific ID is true:  returned value => prefix-issue no.
	 * otherwise just issue no.
	 * @param useProjectSpecificID
	 * @param projectPrefix
	 * @param issueNo
	 * @return
	 */
	public static String getSimpleOrPorjectSpecificItemNo(boolean useProjectSpecificID, String projectPrefix, Integer issueNo) {
		if (issueNo!=null) {
			if(useProjectSpecificID) {
				return projectPrefix + "-" + issueNo.toString();
			}else {
				return issueNo.toString();
			}
		}
		return "";
	}

}
