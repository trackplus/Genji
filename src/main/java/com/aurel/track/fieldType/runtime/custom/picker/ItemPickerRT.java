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



package com.aurel.track.fieldType.runtime.custom.picker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.execute.FilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.treeConfig.field.GeneralSettingsBL;
import com.aurel.track.admin.customize.workflow.activity.IActivityConfig;
import com.aurel.track.admin.customize.workflow.activity.IActivityExecute;
import com.aurel.track.admin.customize.workflow.activity.IValueConverter;
import com.aurel.track.admin.project.ProjectPickerBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.ISerializableLabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.bulkSetters.IBulkSetter;
import com.aurel.track.fieldType.bulkSetters.ItemPickerBulkSetter;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.fieldChange.FieldChangeValue;
import com.aurel.track.fieldType.fieldChange.apply.MultipleTreeFieldChangeApply;
import com.aurel.track.fieldType.fieldChange.config.ItemPickerFieldChangeConfig;
import com.aurel.track.fieldType.fieldChange.converter.ItemPickerSetterConverter;
import com.aurel.track.fieldType.runtime.base.LocalLookupContainer;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SelectContext;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.bl.CustomSelectUtil;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.IMatcherValue;
import com.aurel.track.fieldType.runtime.matchers.design.ItemPickerMatcherDT;
import com.aurel.track.fieldType.runtime.matchers.design.MatcherDatasourceContext;
import com.aurel.track.fieldType.types.custom.CustomItemPicker;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.massOperation.MassOperationContext;
import com.aurel.track.item.massOperation.MassOperationValue;
import com.aurel.track.item.workflow.execute.WorkflowContext;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.ReportItemJSON;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeans;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.TreeNode;

/**
 * Class for project picker runtime
 * @author Tamas Ruff
 *
 */
public class ItemPickerRT extends CustomTreePickerRT {
	private static final Logger LOGGER = LogManager.getLogger(ItemPickerRT.class);
	public static String NUMBER_TITLE_SPLITTER = ": ";

	/**
	 * Loads the settings for a specific field like default value(s),
	 * datasources (for lists) etc. by editing an existing issue
	 * It might differ from loadCreateSettings because it could be need
	 * to add existing entries in the list (also when right already revoked)
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadEditDataSource(SelectContext selectContext)	{
		TPersonBean personBean = LookupContainer.getPersonBean(selectContext.getPersonID());
		TWorkItemBean workItemBean = selectContext.getWorkItemBean();
		Object pickedItems=workItemBean.getAttribute(selectContext.getFieldID());
		Set<Integer> itemIDSet = null;
        if (pickedItems!=null) {
        	Object[] pickedItemsArr = (Object[])pickedItems;
        	itemIDSet = new HashSet<Integer>();
        	for (int i = 0; i < pickedItemsArr.length; i++) {
        		itemIDSet.add((Integer)pickedItemsArr[i]);
			}
        }
        ReportBeans reportBeans = null;
		try {
			reportBeans = getReportBeans(selectContext, personBean);
			if (reportBeans!=null) {
				return getTreeNodeFromReportBeans(reportBeans, true, itemIDSet);
			}
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Too many items to load " + e.getItemCount());
		}
		return null;

	}

	/**
	 * Returns the datasource for a userPicker field by creating a new issue.
	 * When the list is empty this field should be set explicitly to null in the workItem.
	 * (An empty HTML list will not send request parameter and it will remain the original value. This should be avoided)
	 * @param selectContext
	 * @return
	 */
	@Override
	public List loadCreateDataSource(SelectContext selectContext) {
		TPersonBean personBean = LookupContainer.getPersonBean(selectContext.getPersonID());
		ReportBeans reportBeans = null;
		try {
			reportBeans = getReportBeans(selectContext, personBean);
			if (reportBeans!=null) {
				return getTreeNodeFromReportBeans(reportBeans, true, null);
			}
		} catch (TooManyItemsToLoadException e) {
			LOGGER.info("Too many items to load " + e.getItemCount());
		}
		return null;
	}

	/**
	 * Get the sort order related to the value
	 * By default the value is directly used as sortOrder
	 * the select fields has extra sortOrder columns
	 * @param fieldID
	 * @param parameterCode
	 * @param value the value the sortorder is looked for
	 * @param workItemID
	 * @param localLookupContainer
	 * @return
	 */
	@Override
	public Comparable getSortOrderValue(Integer fieldID, Integer parameterCode, Object value,
			Integer workItemID, LocalLookupContainer localLookupContainer) {
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the sort order value");
			return null;
		} else {
			//not ideal but better than nothing (wbs sort order would be ideal)
			return optionIDs[0];
		}
	}

	/**
	 * Gets the flat datasource
	 * @param personID
	 * @return
	 */
	@Override
	public List<ILabelBean> getFlatDataSource(Integer personID) {
		return null;
	}

	/**
	 * Identifies the fieldID of the fieldType the picker is based of
	 * A project picker is based on projects
	 * @return
	 */
	@Override
	public Integer getSystemOptionType() {
		return SystemFields.INTEGER_ISSUENO;
	}

	/**
	 * Gets the selected label beans
	 * @param optionIDs
	 * @param locale
	 * @return
	 */

	/**
	 * Gets the label bean for an objectID
	 * @param objectID
	 * @param locale
	 * @return
	 */
	@Override
	protected ILabelBean lookupLabelBean(Integer objectID, Locale locale) {
		//actually never called because getShowValue is overridden
		try {
			return ItemBL.loadWorkItem(objectID);
		} catch (ItemLoaderException e) {
			return null;
		}
	}

	/**
	 * Get the show value called when an item result set is implied
	 * @param fieldID
	 * @param parameterCode
	 * @param value
	 * @param workItemID
	 * @param localLookupContainer
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Integer fieldID, Integer parameterCode, Object value,
			Integer workItemID, LocalLookupContainer localLookupContainer, Locale locale) {
		return getShowValue(value, locale);
	}

	/**
	 * Get the value to be shown from the database
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getShowValue(Object value, Locale locale) {
		StringBuffer showValue = new StringBuffer();
		Integer[] optionIDs = CustomSelectUtil.getSelectedOptions(value);
		if (optionIDs==null || optionIDs.length==0) {
			LOGGER.debug("No option seleced by getting the show value");
			return showValue.toString();
		}
		List<TWorkItemBean> itemList = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromIntegerArr(optionIDs));
		if (itemList!=null) {
			boolean first = true;
			for (TWorkItemBean workItemBean : itemList) {
				String itemNo = ItemBL.getItemNo(workItemBean);
				String title = workItemBean.getSynopsis();
				if (!first) {
					showValue.append(OPTION_SPLITTER_STRING);
				}
				showValue.append(itemNo);
				showValue.append(NUMBER_TITLE_SPLITTER);
				showValue.append(title);
				first = false;
			}
		}
		return showValue.toString();
	}

	/**
	 * Get the value to be shown for a matcher
	 * Typically same as for the getShowValue(), except the selects
	 * (the value object's type differs for matchers compared to issue field values in getShowValue)
	 * @param fieldID
	 * @param value
	 * @param locale
	 * @return
	 */
	@Override
	public String getMatcherShowValue(Integer fieldID, Object value, Locale locale) {
		return getShowValue(value, locale);
	}

	/**
	 * Loads the datasource for the matcher
	 * used by select fields to get the possible values
	 * It will be called from both field expressions and upper selects
	 * The value can be a List for simple select or a Map of lists for composite selects
	 * @param matcherValue
	 * @param matcherDatasourceContext the data source may be project dependent.
	 * @param parameterCode for composite selects
	 * @return the datasource (list or tree)
	 */
	@Override
	public Object getMatcherDataSource(IMatcherValue matcherValue, MatcherDatasourceContext matcherDatasourceContext, Integer parameterCode) {
		TPersonBean personBean = matcherDatasourceContext.getPersonBean();
		Integer projectID = null;
		Integer[] projectIDs = matcherDatasourceContext.getProjectIDs();
		if (projectIDs!=null && projectIDs.length>0) {
			projectID = projectIDs[0];
			LOGGER.debug("Get config for project " + projectID);
		}
		Integer itemTypeID = null;
		Integer[] itemTypeIDs = matcherDatasourceContext.getItemTypeIDs();
		if (itemTypeIDs!=null && itemTypeIDs.length>0) {
			itemTypeID = itemTypeIDs[0];
			LOGGER.debug("Get config for itemType " + itemTypeID);
		}
		TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getValidConfig(matcherValue.getField(), itemTypeID, projectID);
		if (fieldConfigBean!=null) {
			LOGGER.debug("Config found " + fieldConfigBean.getObjectID());
			try {
				ReportBeans reportBeans = getReportBeans(fieldConfigBean.getObjectID(), matcherValue.getField(), personBean, matcherDatasourceContext.getLocale());
				if (reportBeans!=null) {
					return getTreeNodeFromReportBeans(reportBeans, true, null);
				}
			} catch (TooManyItemsToLoadException e) {
				LOGGER.info("Too many items to load " + e.getItemCount());
			}
		}
		return null;
	}

	/**
	 * Design time matcher object for configuring the matcher
	 * It is the same for system and custom select fields
	 * But the runtime matchers and the matcher converter differ
	 * for system and custom fields
	 * @param fieldID
	 */
	@Override
	public IMatcherDT getMatcherDT(Integer fieldID) {
		return new ItemPickerMatcherDT(fieldID, true);
	}



	/**
	 * Loads the IBulkSetter object for configuring the bulk operation
	 * @param fieldID
	 */
	@Override
	public IBulkSetter getBulkSetterDT(Integer fieldID) {
		return new ItemPickerBulkSetter(fieldID);
	}

	/**
	 * Loads the datasource for the mass operation
	 * used mainly by select fields to get
	 * the all possible options for a field (system or custom select)
	 * It also sets a value if not yet selected
	 * The value can be a List for simple select or a Map of lists for composite selects
	 * @param massOperationContext
	 * @param massOperationValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public void loadBulkOperationDataSource(MassOperationContext massOperationContext,
			MassOperationValue massOperationValue,
			Integer parameterCode, TPersonBean personBean, Locale locale) {
        List<TreeNode> projectTree = ProjectPickerBL.getTreeNodesForCreateModify(null, true, personBean, locale);
        massOperationValue.setPossibleValues(projectTree);
	}

	/**
	 * Gets the IFieldChangeConfig object for configuring the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityConfig getFieldChangeConfig(Integer fieldID) {
		return new ItemPickerFieldChangeConfig(fieldID);
	}

	/**
	 * Gets the IFieldChangeApply object for applying the field change operation
	 * @param fieldID
	 * @return
	 */
	@Override
	public IActivityExecute getFieldChangeApply(Integer fieldID) {
		return new MultipleTreeFieldChangeApply(fieldID);
	}

	/**
	 * Get the converter used for field
	 * @param fieldID
	 * @return
	 */
	@Override
	public IValueConverter getFieldValueConverter(Integer fieldID) {
		return new ItemPickerSetterConverter(fieldID);
	}

	/**
	 * Loads the datasource and value for configuring the field change
	 * @param workflowContext
	 * @param fieldChangeValue
	 * @param parameterCode
	 * @param personBean
	 * @param locale
	 */
	@Override
	public void loadFieldChangeDatasourceAndValue(WorkflowContext workflowContext,
			FieldChangeValue fieldChangeValue,
			Integer parameterCode, TPersonBean personBean, Locale locale) {
		 List<TreeNode> projectTree = ProjectPickerBL.getTreeNodesForCreateModify(null, true, personBean, locale);
		 fieldChangeValue.setPossibleValues(projectTree);
	}

	/**
	 * Get the ILabelBean by primary key
	 * @param personID
	 * @return
	 */
	@Override
	public ILabelBean getLabelBean(Integer optionID, Locale locale) {
		try {
			return ItemBL.loadWorkItem(optionID);
		} catch (ItemLoaderException e) {
			LOGGER.info("Loading the label bean failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	/**
	 * Returns the lookup entity type related to the fieldType
	 * @return
	 */
	@Override
	public int getLookupEntityType() {
		return LuceneUtil.LOOKUPENTITYTYPES.NOSTORE;
	}

	/**
	 * Creates a new empty serializableLabelBean
	 * @return
	 */
	@Override
	public ISerializableLabelBean getNewSerializableLabelBean() {
		return new TWorkItemBean();
	}

	/**
	 * Whether the field should appear in the groupable fields list
	 * Typically fields which are typically unique should not be groupable
	 * @return
	 */
	@Override
	public boolean isGroupable() {
		return false;
	}

	/**
	 * Gets the ID by the label
	 * @param fieldID
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @param label
	 * @param lookupBeansMap
	 * @param componentPartsMap
	 * @return
	 */
	@Override
	public Integer getLookupIDByLabel(Integer fieldID,
			Integer projectID, Integer issueTypeID,
			Locale locale, String label,
			Map<String, ILabelBean> lookupBeansMap, Map<Integer, Integer> componentPartsMap) {
		if (label!=null) {
			String[] labelArray = label.split(NUMBER_TITLE_SPLITTER);
			if (labelArray!=null && labelArray.length>0) {
				String idString = labelArray[0];
				if (idString!=null) {
					ApplicationBean appBean = ApplicationBean.getInstance();
					boolean useProjectSpecificID = false;
					if(appBean.getSiteBean().getProjectSpecificIDsOn()!=null){
						useProjectSpecificID=appBean.getSiteBean().getProjectSpecificIDsOn().booleanValue();
					}
					TWorkItemBean workItemBean = null;
					if (useProjectSpecificID) {
						try {
							workItemBean = ItemBL.loadWorkItemByProjectSpecificID(idString);
						} catch (ItemLoaderException e) {
							LOGGER.debug("Loading the workitem " + idString + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					} else {
						try {
							workItemBean = ItemBL.loadWorkItem(Integer.decode(idString));
						} catch (NumberFormatException e) {
							LOGGER.debug("Parsing the item no " + idString + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						} catch (ItemLoaderException e) {
							LOGGER.debug("Loading the workitem " + idString + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
					if (workItemBean!=null) {
						return workItemBean.getObjectID();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Whether the lookupID found by label is allowed in
	 * the context of serializableBeanAllowedContext
	 * In excel the lookup entries are not limited by the user interface controls
	 * This method should return false if the lookupID
	 * is not allowed (for ex. a person without manager role was set as manager)
	 * @param objectID
	 * @param serializableBeanAllowedContext
	 * @return
	 */
	@Override
	public boolean lookupBeanAllowed(Integer objectID,
			SerializableBeanAllowedContext serializableBeanAllowedContext) {
		return true;
	}

	/**
	 * Gets the datasource for all possible list entries
	 * @param fieldID
	 * @return
	 */
	@Override
	public List<ILabelBean> getDataSource(Integer fieldID) {
		//Initialize with an empty list. Important to be not null.
		//but not all items are loaded because it would be a very big amount of data in memory (all items)
		return new ArrayList<ILabelBean>();
	}

	/**
	 * Gets the TreeNode list from the ReportBeans hierarchy
	 * @param reportBeans
	 * @return
	 */
	public static List<TreeNode> getTreeNodeFromReportBeans(ReportBeans reportBeans, boolean useChecked, Set<Integer> selectedIDs) {
		return getTreeNodeFromReportBeans(reportBeans.getReportBeansFirstLevel(), useChecked, selectedIDs);
	}

	/**
	 * Gets the TreeNode list from a ReportBean list
	 * @param reportBeanList
	 * @return
	 */
	private static List<TreeNode> getTreeNodeFromReportBeans(List<ReportBean> reportBeanList, boolean useChecked, Set<Integer> selectedIDs) {
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		if (reportBeanList!=null) {
			for (ReportBean reportBean : reportBeanList) {
				Boolean checked = null;
				if (useChecked) {
					if (selectedIDs==null) {
						checked = Boolean.FALSE;
					} else {
						checked = Boolean.valueOf(selectedIDs.contains(reportBean.getWorkItemBean().getObjectID()));
					}
				}
				TreeNode treeNode = getTreeNode(reportBean, checked);
				treeNodeList.add(treeNode);
				List<ReportBean> children = reportBean.getChildren();
				if (children!=null && !children.isEmpty()) {
					List<TreeNode> nodeChildren = getTreeNodeFromReportBeans(children, useChecked, selectedIDs);
					treeNode.setChildren(nodeChildren);
					treeNode.setLeaf(false);
				}
			}
		}
		return treeNodeList;
	}

	/**
	 * Gets a treeNode from a reportBean
	 * @param reportBean
	 * @return
	 */
	private static TreeNode getTreeNode(ReportBean reportBean, Boolean checked) {
		TreeNode treeNode = new TreeNode();
		TWorkItemBean workItemBean = reportBean.getWorkItemBean();
		treeNode.setId(workItemBean.getObjectID().toString());
		treeNode.setLabel(ItemBL.getItemNo(workItemBean)  + ItemPickerRT.NUMBER_TITLE_SPLITTER + workItemBean.getLabel());
		treeNode.setSelectable(true);
		treeNode.setLeaf(true);
		treeNode.setChecked(checked);
		treeNode.setIcon(ReportItemJSON.getIconCls(reportBean));
		return treeNode;
	}

	/**
	 * Gets the reportBeans for datasouces
	 * @param selectContext
	 * @param personBean
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	protected ReportBeans getReportBeans(SelectContext selectContext, TPersonBean personBean) throws TooManyItemsToLoadException {
		Locale locale = selectContext.getLocale();
		TFieldConfigBean fieldConfigBean = selectContext.getFieldConfigBean();
		Integer configID = fieldConfigBean.getObjectID();
		Integer fieldID = fieldConfigBean.getField();
		return getReportBeans(configID, fieldID, personBean, locale);
	}

	/**
	 * Gets the reportBeans for item picker for a specific configuration
	 * @param configID
	 * @param fieldID
	 * @param personBean
	 * @param locale
	 * @return
	 * @throws TooManyItemsToLoadException
	 */
	protected ReportBeans getReportBeans(Integer configID, Integer fieldID,  TPersonBean personBean, Locale locale) throws TooManyItemsToLoadException {
		List<TGeneralSettingsBean> datasourceConfigBeans = GeneralSettingsBL.loadByConfig(configID);
		if (datasourceConfigBeans==null || datasourceConfigBeans.isEmpty()) {
			LOGGER.debug("No config found for field " + fieldID + " and config " + configID);
		}
		TGeneralSettingsBean dataSourceSelectionBean = null;
		TGeneralSettingsBean projectReleaseSelectionBean = null;
		TGeneralSettingsBean includeClosedSelectionBean = null;
		TGeneralSettingsBean filterSelectionBean = null;
		for (TGeneralSettingsBean generalSettingsBean : datasourceConfigBeans) {
			Integer parameterCode = generalSettingsBean.getParameterCode();
			if (parameterCode!=null) {
				switch (parameterCode.intValue()) {
				case CustomItemPicker.PARAMETERCODES.DATASOURCE_OPTION:
					dataSourceSelectionBean = generalSettingsBean;
					break;
				case CustomItemPicker.PARAMETERCODES.PROJECT_RELEASE:
					projectReleaseSelectionBean = generalSettingsBean;
					break;
				case CustomItemPicker.PARAMETERCODES.INCLUDE_CLOSED:
					includeClosedSelectionBean = generalSettingsBean;
					break;
				case CustomItemPicker.PARAMETERCODES.FILTER:
					filterSelectionBean = generalSettingsBean;
					break;
				}
			}
		}
		if (dataSourceSelectionBean==null) {
			LOGGER.warn("The datasource selection bean is missing");
			return new ReportBeans(new ArrayList<ReportBean>(), locale);
		}
		Integer selectedDataSource = dataSourceSelectionBean.getIntegerValue();
		if (selectedDataSource==null) {
			LOGGER.warn("The datasource selection value is missing");
			return new ReportBeans(new ArrayList<ReportBean>(), locale);
		}
		//get the users by role
		if (selectedDataSource.intValue()==CustomItemPicker.DATASOURCE_OPTIONS.PROJECT_RELEASE) {
			if (projectReleaseSelectionBean==null) {
				LOGGER.warn("The project/release selection bean is missing");
				return new ReportBeans(new ArrayList<ReportBean>(), locale);
			}
			Integer projectReleaseID = projectReleaseSelectionBean.getIntegerValue();
			if (projectReleaseID==null) {
				LOGGER.warn("The project/release selection value is missing");
				return new ReportBeans(new ArrayList<ReportBean>(), locale);
			}
			boolean includeClosed = false;
			if (includeClosedSelectionBean!=null) {
				includeClosed = includeClosedSelectionBean.isCharacterValueString();
			}
			FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(-projectReleaseID, true, true, includeClosed);
			List<ReportBean> reportBeanList = LoadTreeFilterItems.getTreeFilterReportBeansForReport(filterUpperTO, null, null, personBean, locale);
			return new ReportBeans(reportBeanList, locale);
		} else {
			if (filterSelectionBean==null) {
				LOGGER.warn("The filter selection bean is missing");
				return new ReportBeans(new ArrayList<ReportBean>(), locale);
			}
			Integer filterID = filterSelectionBean.getIntegerValue();
			if (filterID==null) {
				LOGGER.warn("The filter selection value is missing");
				return new ReportBeans(new ArrayList<ReportBean>(), locale);
			}
			List<ReportBean> reportBeanList =  FilterExecuterFacade.getSavedFilterReportBeanList(filterID, locale,
					personBean, new LinkedList<ErrorData>(), null, null, true);
			return new ReportBeans(reportBeanList, locale);
		}
	}
}
