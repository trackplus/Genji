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


package com.aurel.track.fieldType.design.system.date;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.fieldType.constants.DateOptions;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.design.text.DateDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxDate.HIERARCHICAL_BEHAVIOR_OPTIONS;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;

public class SystemDateDT extends DateDT {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemDateDT.class);
	private static WorkItemDAO workItemDAO=DAOFactory.getFactory().getWorkItemDAO();

	public SystemDateDT(String pluginID) {
		super(pluginID);
	}

	/**
	 * Executes a command: compute the bottom up date for items in context
	 * @param configItem
	 */
	@Override
	public void executeCommand(ConfigItem configItem) {
		Integer fieldID = configItem.getConfigRel();
		Integer projectID = configItem.getProject();
		Integer projectTypeID = configItem.getProjectType();
		Integer itemTypeID = configItem.getIssueType();
		Integer[] projectIDs = null;
		if (itemTypeID!=null) {
			LOGGER.debug("Item type specific bottom up computing. itemType is " + itemTypeID);
		}
		if (projectID!=null) {
			//project specific configuration
			projectIDs = ProjectBL.getDescendantProjectIDs(new Integer[] {projectID});
			LOGGER.debug("Project specific bottom up computing. Project is " + projectID);
		} else {
			if (projectTypeID!=null) {
				//project type specific configuration
				List<TProjectBean> projectBeans = ProjectBL.loadByProjectType(projectTypeID);
				projectIDs = GeneralUtils.getBeanIDs(projectBeans);
				LOGGER.debug("Project type specific bottom up computing. Projecttype is " + projectTypeID + " number of projects " + projectIDs.length);
			}
		}
		//gets the parent workItems from context: those which should be computed
		Set<Integer> topLevelParentsSet = workItemDAO.loadParentsInContext(projectIDs, itemTypeID);
		List<TWorkItemBean> involvedWorkItemBeans = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromIntegerCollection(topLevelParentsSet));
		LOGGER.debug("Total number of parent items to recalculate found for field " + fieldID + ": " + topLevelParentsSet.size());	
		//gets all ancestors of the implied parents: the context is not important for ancestors
		List<TWorkItemBean> ancestorItemBeans = ItemBL.getAncestorItems(GeneralUtils.createIntArrFromIntegerCollection(topLevelParentsSet));
		LOGGER.debug("Total number of ancestor items found " + ancestorItemBeans.size());
		for (TWorkItemBean ancestorWorkItemBean : ancestorItemBeans) {
			//ancestorItemBeans can't be added with addAll() to parentWorkItemBeans because some items might be present in both parentWorkItemBeans and ancestorItemBeans
			Integer childItemID = ancestorWorkItemBean.getObjectID();
			if (topLevelParentsSet.contains(childItemID)) {
				//ancestor of another parent: remove from top level
				topLevelParentsSet.remove(childItemID);
			} else {
				//gather all ancestors for calculating the context settings
				involvedWorkItemBeans.add(ancestorWorkItemBean);
			}
		}
		LOGGER.debug("Total number of top level parent items to recalculate found for field " + fieldID + ": " + topLevelParentsSet.size());	
		//context settings for each implied worItem from top level to lowest ancestor 
		Map<Integer, Set<Integer>> projectIssueTypeContexts = ItemBL.getProjectIssueTypeContexts(involvedWorkItemBeans);
		Set<Integer> selectedFieldsSet = new HashSet<Integer>();
		selectedFieldsSet.add(fieldID);
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap = 
				FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
					projectIssueTypeContexts, selectedFieldsSet, null, null, null);
		Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap =
			FieldRuntimeBL.getFieldSettingsForFieldConfigs(projectsIssueTypesFieldConfigsMap);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap<Integer, TWorkItemBean>();
		Map<Integer, List<Integer>> parentToChildrenMap = new HashMap<Integer, List<Integer>>();
		for (TWorkItemBean workItemBean : involvedWorkItemBeans) {
			Integer workItemID = workItemBean.getObjectID();
			Integer parentID = workItemBean.getSuperiorworkitem();
			workItemBeansMap.put(workItemID, workItemBean);
			if (parentID!=null) {
				List<Integer> childrenList = parentToChildrenMap.get(parentID);
				if (childrenList==null) {
					childrenList = new LinkedList<Integer>();
					parentToChildrenMap.put(parentID, childrenList);
				}
				childrenList.add(workItemID);
			}
		}
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		boolean earliest = SystemFields.INTEGER_STARTDATE.equals(fieldID) || SystemFields.INTEGER_TOP_DOWN_START_DATE.equals(fieldID);
		for (Integer topLevelWorkItemID : topLevelParentsSet) {
			//start from top level parents and compute the bottom up date deep first
			computeBottomUpDate(workItemBeansMap.get(topLevelWorkItemID), fieldID, earliest, fieldTypeRT, parentToChildrenMap, workItemBeansMap, projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap);
		}
		
	}
		
	/**
	 * Compute the bottom up dates recursively starting from top parents, deep first
	 * @param workItemBean
	 * @param fieldID
	 * @param parentToChildrenMap
	 * @param workItemBeansMap
	 * @param startDate
	 * @return
	 */
	private static Date computeBottomUpDate(TWorkItemBean workItemBean, Integer fieldID, boolean earliest, IFieldTypeRT fieldTypeRT, Map<Integer, List<Integer>> parentToChildrenMap,
			Map<Integer, TWorkItemBean> workItemBeansMap, Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap,
			Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap) {
		Integer itemID = workItemBean.getObjectID();
		Date actualParentDate = (Date)workItemBean.getAttribute(fieldID);
		List<Integer> childrenList = parentToChildrenMap.get(itemID);
		if (childrenList==null) {
			//leaf workItem
			return actualParentDate;
		} else {
			//parent having children
			Date parentDateComputed = null;
			Integer projectID = workItemBean.getProjectID();
			Integer itemTypeID = workItemBean.getListTypeID();
			TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getFieldConfigForProjectIssueTypeField(
					projectsIssueTypesFieldConfigsMap, projectID, 
					itemTypeID, fieldID);
			Object fieldSettings = FieldRuntimeBL.getFieldSettingsForProjectIssueTypeField(
					projectsIssueTypesFieldSettingsMap, projectID,itemTypeID, fieldID);
			boolean computeBottomUp = fieldTypeRT.getHierarchicalBehavior(fieldID, fieldConfigBean, fieldSettings)==HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP;
			for (Integer childID : childrenList) {
				TWorkItemBean childWorkItem = workItemBeansMap.get(childID);
				Date childDate = null;
				if (childWorkItem!=null) {
					//calculate first for children, independently of computeBottomUp (the parent might not need bottom up but a child branch might need)
					childDate = computeBottomUpDate(childWorkItem, fieldID, earliest, fieldTypeRT, parentToChildrenMap, workItemBeansMap, projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap);
				}
				if (computeBottomUp) {			
					if (childDate!=null &&
							(parentDateComputed==null || (earliest && childDate.before(parentDateComputed)) ||
							(!earliest && childDate.after(parentDateComputed)))) {
						parentDateComputed = childDate;
					}
				}
			}
			if (parentDateComputed!=null) {
				if (EqualUtils.notEqual(parentDateComputed, actualParentDate)) {
					workItemBean.setAttribute(fieldID, parentDateComputed);
						try {
							workItemDAO.saveSimple(workItemBean);
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("Date field " + fieldID + " for item " + workItemBean.getSynopsis() + " (" + itemID + ") set to " + workItemBean.getAttribute(fieldID));
							}
						} catch (ItemPersisterException e) {
					}
				}
				return parentDateComputed;
			} else {
				return actualParentDate;
			}
		}
	}
	
	@Override
	protected List <IntegerStringBean> getOptions(Locale locale, String bundleName) {
		List <IntegerStringBean> options = new ArrayList <IntegerStringBean>();
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.field.type.systemTextBoxDate.opt.empty", locale), Integer.valueOf(DateOptions.EMPTY)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.field.type.systemTextBoxDate.opt.now", locale), Integer.valueOf(DateOptions.NOW)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.field.type.systemTextBoxDate.opt.date", locale), Integer.valueOf(DateOptions.DATE)));
		return options;
	}
	
	@Override
	protected List<IntegerStringBean> getDefaultOptions(Locale locale, String bundleName) {
		List <IntegerStringBean> options = new ArrayList <IntegerStringBean>();
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.field.type.systemTextBoxDate.opt.empty", locale),
						Integer.valueOf(DateOptions.EMPTY)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.field.type.systemTextBoxDate.opt.now", locale),
						Integer.valueOf(DateOptions.NOW)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.field.type.systemTextBoxDate.opt.date", locale),Integer.valueOf(DateOptions.DATE)));
		options.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.field.type.systemTextBoxDate.opt.createdPlusDays", locale),
						Integer.valueOf(DateOptions.CREATED_PLUS_DAYS)));
		return options;
	}
	
	@Override
	protected String getDefaultMaxDateErrorKey() {
		return "systemTextBoxDate.error.minMaxDate";
	}

	@Override
	protected String getDefaultMinDateErrorKey() {
		return "systemTextBoxDate.error.defaultMinDate";
	}

	@Override
	protected String getMinMaxDateErrorKey() {
		return "systemTextBoxDate.error.defaultMaxDate";
	}
	
	/**   
	 * Whether the "History" check box should be rendered 
	 * (an explicit history should be stored for the field or not)
	 * It should not be rendered for the status, start/end date system fields
	 * (the explicit fields from the old history) and comment
	 * @return
	 */
	@Override
	public boolean renderHistoryFlag() {
		return false;
	}
}
