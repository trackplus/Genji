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

package com.aurel.track.itemNavigator.layout.column;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.admin.customize.treeConfig.screen.ScreenConfigBL;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TNavigatorColumnBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;

/**
 * Logic for configuring the layout columns independently of a concrete layout
 * @author Tamas
 *
 */
public class ColumnFieldsBL {

	/**
	 * The default widths for fields
	 * @author Tamas
	 *
	 */
	public interface DEFAULT_WIDTHS {
		public static final int MASS_OP_DEFAULT_WIDTH=40;
		public static final int SYMBOL_DEFAULT_WIDTH=25;
		public static final int TEXT_DEFAULT_WIDTH=100;
		public static final int PSEUDO_FIELD_WIDTH=100;
		public static final int ATTACHMENT_FIELD_WIDTH=50;
	}

	private static final String DEPRECATED_SUFFIX = "*";

	/**
	 * Whether the person has accounting in any project
	 * @param personID
	 * @return
	 */
	public static boolean hasAccounting(Integer personID) {
		boolean hasAccounting = false;
		if (personID!=null) {
			if (!ApplicationBean.getInstance().isGenji()) {
				int[] arrRights = new int[] {
						AccessFlagIndexes.READANYTASK,
						AccessFlagIndexes.PROJECTADMIN};
				//all projects the user has budget/expense role in
				List<TProjectBean> projectBeans = ProjectBL.loadActiveInactiveProjectsByRights(personID, arrRights);
				if (projectBeans!=null) {
					for (TProjectBean projectBean : projectBeans) {
						ProjectAccountingTO projectAccountingTO = ProjectBL.getProjectAccountingTO(projectBean.getObjectID());
						if (projectAccountingTO.isWorkTracking() || projectAccountingTO.isCostTracking()) {
							hasAccounting = true;
							break;
						}
					}
				}
			}

		}
		return hasAccounting;
	}

	/**
	 * Gets the visible pseudo fields
	 * @param personID
	 * @param hasAccounting
	 * @return
	 */
	public static Map<Integer, Boolean> getVisisblePseudoFields(Integer personID, boolean hasAccounting) {
		Map<Integer, Boolean> pseudoFieldsVisible = new HashMap<Integer, Boolean>();
		//budget/expense fields are needed?
		boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
		List<Integer> fieldIDs = new LinkedList<Integer>();
		if (!ApplicationBean.getInstance().isGenji()) {
			if (budgetActive) {
				fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
			}
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
			fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		}
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
		Map<Integer, Integer> fieldRestrictions = AccessBeans.getFieldRestrictions(personID, null, null, fieldIDs, false);
		if (!ApplicationBean.getInstance().isGenji()) {
			if (budgetActive) {
				Integer budgetFlag = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
				if (budgetFlag==null || budgetFlag.intValue()!=TRoleFieldBean.ACCESSFLAG.NOACCESS) {
					pseudoFieldsVisible.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET, hasAccounting);
				}
			}
			Integer planFlag = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
			if (planFlag==null || planFlag.intValue()!=TRoleFieldBean.ACCESSFLAG.NOACCESS) {
				pseudoFieldsVisible.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN, hasAccounting);
			}
			Integer myExpenseFlag = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
			if (myExpenseFlag==null || myExpenseFlag.intValue()!=TRoleFieldBean.ACCESSFLAG.NOACCESS) {
				pseudoFieldsVisible.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES, hasAccounting);
			}
			Integer totalExpenseFlag = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
			if (totalExpenseFlag==null || totalExpenseFlag.intValue()!=TRoleFieldBean.ACCESSFLAG.NOACCESS) {
				pseudoFieldsVisible.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES, hasAccounting);
				pseudoFieldsVisible.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES, hasAccounting);
			}
		}
		Integer watchersFlag = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
		if (watchersFlag==null || watchersFlag.intValue()!=TRoleFieldBean.ACCESSFLAG.NOACCESS) {
			pseudoFieldsVisible.put(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS, true);
		}
		return pseudoFieldsVisible;
	}

	/**
	 * Loads all  column fields available in navigator. The list contains
	 * the system and custom fields, history fields, pseudo fields bulk edit and index columns
	 * @param personID
	 * @param locale
	 * @param excludeHistory
	 * @return
	 */
	public static List<ColumnFieldTO> loadAvailableColumnFields(Integer personID, Locale locale, boolean excludeHistory) {
		boolean hasAccounting = hasAccounting(personID);
		Map<Integer, Boolean> pseudoFieldsVisible = getVisisblePseudoFields(personID, hasAccounting);
		Set<Integer> realFieldIDs = getAvailableRealFieldIDs(personID);
		List<ColumnFieldTO> columnFieldsList = loadRealFields(realFieldIDs, null, locale);
		columnFieldsList.addAll(loadAvailableCustomListIconColumns(realFieldIDs, locale));
		columnFieldsList.addAll(loadAvailablePseudoFields(realFieldIDs, locale, pseudoFieldsVisible));
		if (!excludeHistory) {
			Boolean ownExpense = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
			Boolean viewAllExpenses = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
			columnFieldsList.addAll(loadAvailableHistoryFields(locale, hasAccounting, (ownExpense!=null && ownExpense.booleanValue()) ||
					(viewAllExpenses!=null && viewAllExpenses.booleanValue())));
		}
		Collections.sort(columnFieldsList, new ColumnFieldAlphabeticComparator());
		//the bulk operation always the first
		columnFieldsList.add(0, getBulkOperationReportField(locale));
		columnFieldsList.add(1, getIndexNumberReportField(locale));
		return columnFieldsList;
	}

	/**
	 * Sorting the column fields
	 * @author Tamas
	 *
	 */
	static class ColumnFieldAlphabeticComparator implements Comparator<ColumnFieldTO> {
		public int compare(ColumnFieldTO arg0, ColumnFieldTO arg1) {
			String left =  arg0.getLabel();
			String right = arg1.getLabel();
			if((left == null) && (right == null)) {
				return 0;
			}
			if(left == null) {
				return -1;
			}
			if(right == null) {
				return 1;
			}
			return left.compareTo(right);
		}
	}

	/**
	 * Gets the real fieldIDs a user can choose in navigator
	 * @param personID
	 * @return
	 */
	public static Set<Integer> getAvailableRealFieldIDs(Integer personID) {
		Set<Integer> screenIDs = ScreenConfigBL.getVisibleScreensForPerson(personID);
		List<TFieldBean> fieldsOnScreens = FieldBL.loadByScreens(screenIDs.toArray());
		Set<Integer> fieldsIDs = GeneralUtils.createIntegerSetFromBeanList(fieldsOnScreens);
		//add some system fields which should be available
		//even if they are not explicitly present in any of the screens
		fieldsIDs.add(SystemFields.INTEGER_PROJECT);
		fieldsIDs.add(SystemFields.INTEGER_ISSUETYPE);
		fieldsIDs.add(SystemFields.INTEGER_STATE);
		fieldsIDs.add(SystemFields.INTEGER_MANAGER);
		fieldsIDs.add(SystemFields.INTEGER_RESPONSIBLE);
		fieldsIDs.add(SystemFields.INTEGER_ISSUENO);
		fieldsIDs.add(SystemFields.INTEGER_ORIGINATOR);
		fieldsIDs.add(SystemFields.INTEGER_CREATEDATE);
		fieldsIDs.add(SystemFields.INTEGER_LASTMODIFIEDDATE);
		fieldsIDs.add(SystemFields.INTEGER_SUPERIORWORKITEM);
		fieldsIDs.add(SystemFields.INTEGER_CHANGEDBY);
		fieldsIDs.add(SystemFields.INTEGER_SYNOPSIS);
		fieldsIDs.add(SystemFields.INTEGER_DESCRIPTION);
		fieldsIDs.add(SystemFields.INTEGER_COMMENT);
		fieldsIDs.add(SystemFields.INTEGER_SUBMITTEREMAIL);
		fieldsIDs.add(SystemFields.INTEGER_WBS);
		return fieldsIDs;
	}

	/**
	 * Load the field columns related to a real (system or custom) field
	 * @param fieldsIDs
	 * @param navigatorColumnBeansByFieldMap
	 * @param locale
	 * @return
	 */
	public static List<ColumnFieldTO> loadRealFields(Set<Integer> fieldsIDs, Map<Integer, TNavigatorColumnBean> navigatorColumnBeansByFieldMap, Locale locale) {
		List<ColumnFieldTO> realFields = new ArrayList<ColumnFieldTO>();
		List<TFieldBean> fieldBeans = FieldBL.loadByFieldIDs(fieldsIDs.toArray());
		Map<Integer, TFieldConfigBean> defaultConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
		if (fieldBeans!=null) {
			for (TFieldBean fieldBean : fieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				TNavigatorColumnBean navigatorColumnBean = null;
				if (navigatorColumnBeansByFieldMap!=null) {
					navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
				}
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && (!(fieldTypeRT.isComputed(fieldID, null) && fieldTypeRT.isCustom()))) {
					TFieldConfigBean fieldConfigBean = defaultConfigsMap.get(fieldID);
					String label = null;
					if (fieldConfigBean==null || fieldConfigBean.getLabel()==null) {
						label = fieldBean.getName();
					} else {
						label = fieldConfigBean.getLabel();
					}
					ColumnFieldTO columnFieldTO = new ColumnFieldTO(fieldID, label);
					columnFieldTO.setName(fieldBean.getName());
					if (fieldBean.isDeprecatedString()) {
						columnFieldTO.setLabel(columnFieldTO.getLabel()+DEPRECATED_SUFFIX);
					}
					//is it a long field?
					columnFieldTO.setRenderAsLong(fieldTypeRT.isLong());
					int fieldWidth = ColumnFieldsBL.DEFAULT_WIDTHS.TEXT_DEFAULT_WIDTH;
					if (navigatorColumnBean!=null ) {
						columnFieldTO.setObjectID(navigatorColumnBean.getObjectID());
						if (navigatorColumnBean.getFieldWidth()!=null) {
							fieldWidth = navigatorColumnBean.getFieldWidth();
						}
					}
					columnFieldTO.setFieldWidth(fieldWidth);
					realFields.add(columnFieldTO);
				}
			}
		}
		return realFields;
	}

	/**
	 * Gets yhe possible pseudo fields
	 * @param realFieldIDs
	 * @param locale
	 * @param pseudoFieldsVisible
	 * @return
	 */
	private static List<ColumnFieldTO> loadAvailablePseudoFields(Set<Integer> realFieldIDs,
			Locale locale, Map<Integer, Boolean> pseudoFieldsVisible) {
		List<ColumnFieldTO> pseudoFields = new ArrayList<ColumnFieldTO>();
		Boolean watcherFieldsAllowed = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
		if (watcherFieldsAllowed!=null && watcherFieldsAllowed.booleanValue()) {
			//consulted
			pseudoFields.add(getConsultedColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
			//informed
			pseudoFields.add(getInformedColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
		}
		//attachment
		pseudoFields.add(getAttachmnentColumn(locale, null, DEFAULT_WIDTHS.ATTACHMENT_FIELD_WIDTH));
		//issue type symbol
		pseudoFields.add(getItemTypeSymbolColumn(locale, null, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH));
		//status symbol
		pseudoFields.add(getStatusSymbolColumn(locale, null, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH));
		//priority symbol
		if (realFieldIDs.contains(SystemFields.INTEGER_PRIORITY)) {
			pseudoFields.add(getPrioritySymbolColumn(locale, null, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH));
		}
		//severity symbol
		if (realFieldIDs.contains(SystemFields.INTEGER_SEVERITY)) {
			pseudoFields.add(getSeveritySymbolColumn(locale, null, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH));
		}
		Boolean ownExpense = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES);
		if (ownExpense!=null && ownExpense.booleanValue()) {
			//my time expense
			pseudoFields.add(getMyTimeExpenseColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
			//my cost expense
			pseudoFields.add(getMyCostExpenseColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
		}
		Boolean viewAllExpenses = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES);
		if (viewAllExpenses!=null && viewAllExpenses.booleanValue()) {
			//total time expense
			pseudoFields.add(getTotalTimeExpenseColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
			//total cost expense
			pseudoFields.add(getTotalCostExpenseColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
		}
		Boolean budgetVisible = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET);
		if (budgetVisible!=null && budgetVisible) {
			//budget time
			pseudoFields.add(getTimeBudgetColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
			//budget cost
			pseudoFields.add(getCostBugetColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
		}
		Boolean planVisible = pseudoFieldsVisible.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN);
		if (planVisible!=null && planVisible.booleanValue()) {
			//total planned time
			pseudoFields.add(getTimePlanColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
			//total planned cost
			pseudoFields.add(getCostPlanColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
			//remaining planned time
			pseudoFields.add(getRemainingTimePlanColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
			//remaining planned cost
			pseudoFields.add(getRemainingCostPlanColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
		}
		//linked workItemIDs
		pseudoFields.add(getLinkedItemsColumn(locale, null, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH));
		//private issue symbol
		boolean accessLevelDeprected = FieldDesignBL.isDeprecated(SystemFields.INTEGER_ACCESSLEVEL);
		if (!accessLevelDeprected) {
			pseudoFields.add(getAccessLevelColumn(locale, null, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH));
		}
		//out of bound symbols
		pseudoFields.add(getOutOfBoundSymbolColumn(locale, null, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH*3));
		return pseudoFields;
	}

	/**
	 * Load the history pseudo fields
	 * It is not added to the other pseudo fields because
	 * it will be the last selectable field in order to:
	 * 1. not to be frequently selected because of the performance problems
	 * 2. to be always the last long field shown on the report overview when selected
	 * @param locale
	 * @return
	 */
	static private List<ColumnFieldTO> loadAvailableHistoryFields(Locale locale, boolean hasAccounting, boolean hasExpense) {
		List<ColumnFieldTO> historyFields = new LinkedList<ColumnFieldTO>();
		historyFields.add(getFieldChangeHistoryColumn(locale, null));
		if (hasAccounting) {
			historyFields.add(getBudgetHistoryColumn(locale, null));
			historyFields.add(getPlanHistoryColumn(locale, null));
		}
		if (hasExpense) {
			historyFields.add(getExpenseListColumn(locale, null));
		}
		return historyFields;
	}

	/**
	 * Gets the stored fieldWidth for a field. If no width is stored falls back to default
	 * @param navigatorColumnBeansByFieldMap
	 * @param defaultWidth
	 * @return
	 */
	private static Integer getFieldWidth(TNavigatorColumnBean navigatorColumnBean, Integer defaultWidth) {
		if (navigatorColumnBean!=null && navigatorColumnBean.getFieldWidth()!=null) {
			return navigatorColumnBean.getFieldWidth();
		}
		return defaultWidth;
	}

	/**
	 * Gets the stored fieldWidth for a field. If no width is stored falls back to default
	 * @param navigatorColumnBeansByFieldMap
	 * @param fieldID
	 * @param defaultWidth
	 * @return
	 */
	private static Integer getColumnID(TNavigatorColumnBean navigatorColumnBean) {
		if (navigatorColumnBean!=null) {
			return navigatorColumnBean.getObjectID();
		}
		return null;
	}

	/**
	 * Load the pseudo fields from the set
	 * @param pseudoFieldIDs
	 * @param locale
	 * @return
	 */
	public static List<ColumnFieldTO> loadSelectedPseudoFields(Set<Integer> pseudoFieldIDs, Map<Integer, TNavigatorColumnBean> navigatorColumnBeansByFieldMap, Locale locale) {
		List<ColumnFieldTO> pseudoFields = new ArrayList<ColumnFieldTO>();
		//consulted
		Integer fieldID = TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getConsultedColumn(locale, getColumnID(navigatorColumnBean),  getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//informed
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getInformedColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//attachment symbol
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getAttachmnentColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.ATTACHMENT_FIELD_WIDTH)));
		}
		//issue type symbol
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getItemTypeSymbolColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH)));
		}
		//status symbol
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getStatusSymbolColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH)));
		}
		//priority symbol
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getPrioritySymbolColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH)));
		}
		//severity symbol
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getSeveritySymbolColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH)));
		}
		//my time expense
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getMyTimeExpenseColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//my cost expense
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getMyCostExpenseColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//total time expense
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getTotalTimeExpenseColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//total cost expense
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getTotalCostExpenseColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//budget time
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getTimeBudgetColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//budget cost
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getCostBugetColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//total planned time
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getTimePlanColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//total planned cost
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getCostPlanColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//remaining planned time
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getRemainingTimePlanColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//remaining planned cost
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getRemainingCostPlanColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//linked workItemIDs
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getLinkedItemsColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH)));
		}
		//private issue symbol
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getAccessLevelColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH)));
		}
		//out of bound symbols
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getOutOfBoundSymbolColumn(locale, getColumnID(navigatorColumnBean), getFieldWidth(navigatorColumnBean, DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH*3)));
		}
		//general history
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getFieldChangeHistoryColumn(locale, getColumnID(navigatorColumnBean)));
		}
		//budget history
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_HISTORY_LIST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getBudgetHistoryColumn(locale, getColumnID(navigatorColumnBean)));
		}
		//plan history
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.PLAN_HISTORY_LIST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getPlanHistoryColumn(locale, getColumnID(navigatorColumnBean)));
		}
		//cost list
		fieldID = TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST;
		if (pseudoFieldIDs.contains(fieldID)) {
			TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(fieldID);
			pseudoFields.add(getExpenseListColumn(locale, getColumnID(navigatorColumnBean)));
		}
		return pseudoFields;
	}

	/**
	 * Gets the bulk operation reportField
	 * @param locale
	 * @return
	 */
	public static ColumnFieldTO getBulkOperationReportField(Locale locale) {
		//bulk operation
		ColumnFieldTO columnField = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.CHECKBOX_FIELD_ID,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.MASS_OPERATION, locale));
		columnField.setFieldWidth(ColumnFieldsBL.DEFAULT_WIDTHS.MASS_OP_DEFAULT_WIDTH);
		return columnField;
	}

	/**
	 * Gets the index number report field
	 * @param locale
	 * @return
	 */
	public static ColumnFieldTO getIndexNumberReportField(Locale locale) {
		//index number
		ColumnFieldTO columnField = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.INDEX_NUMBER,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.INDEX_NUMBER, locale));
		columnField.setFieldWidth(ColumnFieldsBL.DEFAULT_WIDTHS.PSEUDO_FIELD_WIDTH);
		return columnField;
	}

	/**
	 * Gets the possible custom list icon columns
	 * @param fieldsIDs
	 * @param locale
	 * @return
	 */
	private static List<ColumnFieldTO> loadAvailableCustomListIconColumns(Set<Integer> fieldsIDs, Locale locale) {
		List<ColumnFieldTO> customListIconColumns = new ArrayList<ColumnFieldTO>();
		List<Integer> customListFieldIDs = new LinkedList<Integer>();
		if (fieldsIDs!=null) {
			for (Integer fieldID : fieldsIDs) {
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null) {
					if (fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION || fieldTypeRT.isComposite()) {
						customListFieldIDs.add(fieldID);
					}
				}
			}
			if (!customListFieldIDs.isEmpty()) {
				List<TFieldBean> fieldBeans = FieldBL.loadByFieldIDs(customListFieldIDs.toArray());
				Map<Integer, TFieldConfigBean> defaultConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
				if (fieldBeans!=null) {
					for (TFieldBean fieldBean : fieldBeans) {
						Integer fieldID = fieldBean.getObjectID();
						TFieldConfigBean fieldConfigBean = defaultConfigsMap.get(fieldID);
						String label = null;
						if (fieldConfigBean==null || fieldConfigBean.getLabel()==null) {
							label = fieldBean.getName();
						} else {
							label = fieldConfigBean.getLabel();
						}
						ColumnFieldTO columnFieldTO = new ColumnFieldTO(getIconFieldFromCustomListField(fieldID),
										LocalizeUtil.getParametrizedString(TReportLayoutBean.PSEUDO_COLUMN_LABELS.CUSTOM_OPTION_SYMBOL, new Object[] {label}, locale));
						columnFieldTO.setName(fieldBean.getName() + TReportLayoutBean.PSEUDO_COLUMN_NAMES.CUSTOM_OPTION_SYMBOL);
						columnFieldTO.setFieldWidth(ColumnFieldsBL.DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH);
						columnFieldTO.setRenderContentAsImg(true);
						columnFieldTO.setRenderHeaderAsImg(true);
						columnFieldTO.setHeaderImgName("customFields.png");
						columnFieldTO.setFieldIsCustomIcon(true);
						customListIconColumns.add(columnFieldTO);
					}
				}
			}
		}
		return customListIconColumns;
	}

	/**
	 * Gets the custom list icon columns
	 * @param customListIconFieldIDs
	 * @param navigatorColumnBeansByFieldMap
	 * @param locale
	 * @return
	 */
	public static List<ColumnFieldTO> getSelectedCustomListIconColumns(Set<Integer> customListIconFieldIDs,
			Map<Integer, TNavigatorColumnBean> navigatorColumnBeansByFieldMap, Locale locale) {
		List<ColumnFieldTO> columnFieldTOs = new LinkedList<ColumnFieldTO>();
		if (customListIconFieldIDs==null || customListIconFieldIDs.isEmpty()) {
			return columnFieldTOs;
		}
		List<Integer> customListFieldIDs = new LinkedList<Integer>();
		Map<Integer, Integer> customFieldToIconFieldMap = new HashMap<Integer, Integer>();
		for (Integer customListIconFieldID : customListIconFieldIDs) {
			Integer customListFieldID =  getCustomListFieldFromIconField(customListIconFieldID);
			customFieldToIconFieldMap.put(customListFieldID, customListIconFieldID);
			customListFieldIDs.add(customListFieldID);
		}
		List<TFieldBean> fieldBeans = FieldBL.loadByFieldIDs(customListFieldIDs.toArray());
		Map<Integer, TFieldConfigBean> defaultConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
		if (fieldBeans!=null) {
			for (TFieldBean fieldBean : fieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.getValueType()==ValueType.CUSTOMOPTION || fieldTypeRT.isComposite()) {
					TFieldConfigBean fieldConfigBean = defaultConfigsMap.get(fieldID);
					String label = null;
					if (fieldConfigBean==null || fieldConfigBean.getLabel()==null) {
						label = fieldBean.getName();
					} else {
						label = fieldConfigBean.getLabel();
					}
					Integer customListIconFieldID = customFieldToIconFieldMap.get(fieldID);
					TNavigatorColumnBean navigatorColumnBean = navigatorColumnBeansByFieldMap.get(customListIconFieldID);
					ColumnFieldTO columnFieldTO = new ColumnFieldTO(customListIconFieldID,
							LocalizeUtil.getParametrizedString(TReportLayoutBean.PSEUDO_COLUMN_LABELS.CUSTOM_OPTION_SYMBOL, new Object[] {label}, locale));
					columnFieldTO.setName(fieldBean.getName() + TReportLayoutBean.PSEUDO_COLUMN_NAMES.CUSTOM_OPTION_SYMBOL);
					int fieldWidth = ColumnFieldsBL.DEFAULT_WIDTHS.SYMBOL_DEFAULT_WIDTH;
					if (navigatorColumnBean!=null && navigatorColumnBean.getFieldWidth()!=null) {
						fieldWidth = navigatorColumnBean.getFieldWidth();
					}
					columnFieldTO.setObjectID(navigatorColumnBean.getObjectID());
					columnFieldTO.setFieldWidth(fieldWidth);
					columnFieldTO.setRenderContentAsImg(true);
					columnFieldTO.setRenderHeaderAsImg(true);
					columnFieldTO.setHeaderImgName("customFields.png");
					columnFieldTO.setFieldIsCustomIcon(true);
					columnFieldTOs.add(columnFieldTO);
				}
			}
		}
		return columnFieldTOs;
	}

	/**
	 * Gets the consulted list column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getConsultedColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.CONSULTANT_LIST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.CONSULTANT_LIST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the informed list column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getInformedColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO =  new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.INFORMANT_LIST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.INFORMANT_LIST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the attachment column
	 * @param locale
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getAttachmnentColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.ATTACHMENT_SYMBOL,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.ATTACHMENT_SYMBOL, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		columnFieldTO.setRenderHeaderAsImg(true);
		columnFieldTO.setHeaderImgName("attachment.png");
		return columnFieldTO;
	}

	/**
	 * Gets the itemType symbol column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getItemTypeSymbolColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.ISSUETYPE_SYMBOL,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.ISSUETYPE_SYMBOL, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderContentAsImg(true);
		columnFieldTO.setRenderHeaderAsImg(true);
		columnFieldTO.setHeaderImgName("issueType.png");
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the status symbol column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getStatusSymbolColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_SYMBOL,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.STATUS_SYMBOL, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderContentAsImg(true);
		columnFieldTO.setRenderHeaderAsImg(true);
		columnFieldTO.setHeaderImgName("status.png");
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the priority symbol column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getPrioritySymbolColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.PRIORITY_SYMBOL,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.PRIORITY_SYMBOL, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderContentAsImg(true);
		columnFieldTO.setRenderHeaderAsImg(true);
		columnFieldTO.setHeaderImgName("priority.png");
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the severity symbol column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getSeveritySymbolColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.SEVERITY_SYMBOL,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.SEVERITY_SYMBOL, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderContentAsImg(true);
		columnFieldTO.setRenderHeaderAsImg(true);
		columnFieldTO.setHeaderImgName("severity.png");
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the my time expense column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getMyTimeExpenseColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_TIME,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_TIME, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the my cost expense column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getMyCostExpenseColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_COST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_COST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the total time expense column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getTotalTimeExpenseColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_TIME,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_TIME, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the total cost expense column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getTotalCostExpenseColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_COST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_COST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the time budget column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getTimeBudgetColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_TIME,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_TIME, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the cost budget column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getCostBugetColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_COST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_COST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the time plan column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getTimePlanColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_TIME,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_TIME, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the cost budget column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getCostPlanColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_COST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_COST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the remaining time plan column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getRemainingTimePlanColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_TIME,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_TIME, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the remaining cost plan column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getRemainingCostPlanColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_COST,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_COST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the linked items column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getLinkedItemsColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.LINKED_ITEMS,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.LINKED_ITEMS, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the access level column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getAccessLevelColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.PRIVATE_SYMBOL,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.PRIVATE_SYMBOL, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderContentAsImg(true);
		columnFieldTO.setRenderHeaderAsImg(true);
		columnFieldTO.setHeaderImgName("lock.gif");
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 *  Gets the out of bound icon column
	 * @param locale
	 * @param columnID
	 * @param fieldWidth
	 * @return
	 */
	private static ColumnFieldTO getOutOfBoundSymbolColumn(Locale locale, Integer columnID, Integer fieldWidth) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.OVERFLOW_SYMBOL,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.OVERFLOW_ICONS, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderContentAsImg(true);
		columnFieldTO.setRenderHeaderAsImg(true);
		columnFieldTO.setHeaderImgName("budgetOverflow.gif");
		columnFieldTO.setFieldWidth(fieldWidth);
		return columnFieldTO;
	}

	/**
	 * Gets the field change history column
	 * @param locale
	 * @param columnID
	 * @return
	 */
	private static ColumnFieldTO getFieldChangeHistoryColumn(Locale locale, Integer columnID) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.HISTORY,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.HISTORY_LIST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderAsLong(true);
		return columnFieldTO;
	}

	/**
	 * Gets the budget history column
	 * @param locale
	 * @param columnID
	 * @return
	 */
	private static ColumnFieldTO getBudgetHistoryColumn(Locale locale, Integer columnID) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_HISTORY_LIST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGETHISTORY,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_HISTORY_LIST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderAsLong(true);
		return columnFieldTO;
	}

	/**
	 * Gets the plan history column
	 * @param locale
	 * @param columnID
	 * @return
	 */
	private static ColumnFieldTO getPlanHistoryColumn(Locale locale, Integer columnID) {
		ColumnFieldTO columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.PLAN_HISTORY_LIST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANHISTORY,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.PLAN_HISTORY_LIST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderAsLong(true);
		return columnFieldTO;
	}

	/**
	 * Gets the expense list column
	 * @param locale
	 * @param columnID
	 * @return
	 */
	private static ColumnFieldTO getExpenseListColumn(Locale locale, Integer columnID) {
		ColumnFieldTO  columnFieldTO = new ColumnFieldTO(TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST,
				TReportLayoutBean.PSEUDO_COLUMN_NAMES.COSTS,
				LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.COST_LIST, locale));
		columnFieldTO.setObjectID(columnID);
		columnFieldTO.setRenderAsLong(true);
		return columnFieldTO;
	}

	public static int CUSTOM_LIST_ICON_FIELD_START = -10000;
	public static Integer getIconFieldFromCustomListField(Integer fieldID) {
		return Integer.valueOf(CUSTOM_LIST_ICON_FIELD_START-fieldID.intValue());
	}

	public static Integer getCustomListFieldFromIconField(Integer iconFieldID) {
		return Integer.valueOf(-(iconFieldID.intValue()-CUSTOM_LIST_ICON_FIELD_START));
	}

	/**
	 * Gets the name of the pseudo column
	 * Used by sorting (no grouping by pseudo columns)
	 * The field name is used mainly by sorting the jasper reports by generating the jasper design dynamically
	 * @param fieldID
	 * @return
	 */
	public static String getPseudoColumnName(Integer fieldID) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.CONSULTANT_LIST;
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.INFORMANT_LIST;
			case TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.ATTACHMENT_SYMBOL;
			case TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.ISSUETYPE_SYMBOL;
			case TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.STATUS_SYMBOL;
			case TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.PRIORITY_SYMBOL;
			case TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.SEVERITY_SYMBOL;
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_TIME;
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_COST;
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_TIME;
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_COST;
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_TIME;
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_COST;
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_TIME;
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_COST;
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_TIME;
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_COST;
			case TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.LINKED_ITEMS;
			case TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.PRIVATE_SYMBOL;
			case TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS:
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.OVERFLOW_SYMBOL;
			}
		}
		return "";
	}

	/**
	 * Gets the label of the pseudo column
	 * Used by sorting (no grouping by pseudo columns)
	 * @param fieldID
	 * @param locale
	 * @return
	 */
	public static String getPseudoColumnLabel(Integer fieldID, Locale locale) {
		if (fieldID!=null) {
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.CONSULTANT_LIST, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.INFORMANT_LIST, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.ATTACHMENT_SYMBOL, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.ISSUETYPE_SYMBOL:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.ISSUETYPE_SYMBOL, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.STATUS_SYMBOL:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.STATUS_SYMBOL, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.PRIORITY_SYMBOL:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.PRIORITY_SYMBOL, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.SEVERITY_SYMBOL:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.SEVERITY_SYMBOL, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_TIME, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_COST, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_TIME, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_COST, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_TIME, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_COST, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_TIME, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_COST, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_TIME, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_COST, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.LINKED_ITEMS, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.PRIVATE_SYMBOL, locale);
			case TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS:
				return LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.OVERFLOW_ICONS, locale);
			}
		}
		return "";
	}
}
