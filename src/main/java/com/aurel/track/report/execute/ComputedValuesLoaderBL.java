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


package com.aurel.track.report.execute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

public class ComputedValuesLoaderBL {

	public static String ISOShowValueSplitter = " ";
	
	/**
	 * Sets the show values and sort order values for a specific workItem for a pseudoField
	 * @param workItemID
	 * @param showValuesMapForWorkItemByField
	 * @param sortOrderValuesMapForWorkItemByField
	 * @param showValuesForFieldByWorkItem
	 * @param sortOrderValuesForFieldByWorkItem
	 * @param pseudoColumn
	 */
	public static void setShowAndSortOrderValues(Integer workItemID, 
			Map<Integer, String> showValuesMapForWorkItemByField, Map<Integer, String> showISOValuesMapForWorkItemByField,
			Map<Integer, Comparable> sortOrderValuesMapForWorkItemByField, 
			Map<Integer, String> showValuesForFieldByWorkItem, Map<Integer, String> showISOValuesForFieldByWorkItem,
			Map<Integer, Double> sortOrderValuesForFieldByWorkItem, Integer pseudoColumn) {
		String showValue = (String)showValuesForFieldByWorkItem.get(workItemID);
		if (showValue!=null) {
			showValuesMapForWorkItemByField.put(pseudoColumn, showValue);
		}
		String showISOValue = (String)showISOValuesForFieldByWorkItem.get(workItemID);
		if (showISOValue!=null) {
			showISOValuesMapForWorkItemByField.put(pseudoColumn, showISOValue);
		}
		Comparable sortOrderValue = sortOrderValuesForFieldByWorkItem.get(workItemID);
		if (sortOrderValue!=null) {
			sortOrderValuesMapForWorkItemByField.put(pseudoColumn, sortOrderValue);
		}
	}
	
	
	/**
	 * Get the show values and sort order values for an expense field
	 * @param workItemBeanList
	 * @param effortType
	 * @param personID
	 * @param filterByPerson
	 * @param locale
	 * @param isTotal whether the total of expenses by all persons or just the total of own expenses
	 * @param projectIDsToProjectBeansMap
	 * @param expenseShowValuesMap in-out parameter: should be initialized (not null!)
	 * @param expenseSortOrderValuesMap in-out parameter: should be initialized (not null!)
	 */
	public static void loadComputedValueMaps(List<TComputedValuesBean> computedValueBeansList, Map<Integer, TWorkItemBean> workItemBeansMap,//List<TWorkItemBean> workItemBeanList,
			/*int effortType, Integer personID,*/ Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions, Integer fieldID,
			/*boolean filterByPerson,*/Map<Integer, ProjectAccountingTO> projectAccountingTOMap, Locale locale,
			Map<Integer, String> expenseShowValuesMap,
			Map<Integer, String> expenseISOShowValuesMap, Map<Integer, Double> expenseSortOrderValuesMap) {
		/*Integer personToFilter = null;
		Integer fieldID = null;
		if (filterByPerson) {
			personToFilter = personID;
			fieldID = FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.MY_EXPENSES;
		} else {
			fieldID = FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.ALL_EXPENSES;
		}*/
		getValuesMap(computedValueBeansList, workItemBeansMap, fieldRestrictions, projectAccountingTOMap, fieldID,/* effortType, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, 
				personToFilter, true, fieldID,*/ locale, 
				expenseShowValuesMap, expenseISOShowValuesMap, expenseSortOrderValuesMap);
		
	}
	
	/**
	 * Get the show values and sort order values for a total budget field
	 * @param workItemBeanList
	 * @param personID
	 * @param effortType
	 * @param locale
	 * @param projectIDsToProjectBeansMap
	 * @param totalBudgetShowValuesMap in-out parameter: should be initialized (not null!)
	 * @param totalBudgetShowISOValuesMap in-out parameter: should be initialized (not null!)
	 * @param totalBudgetSortOrderValuesMap in-out parameter: should be initialized (not null!)
	 * @param plan whether it is plan (bottom-up) or budget (top-down)
	 */
	/*public static void loadBudgetAndPlannedValueMaps(List<TWorkItemBean> workItemBeanList, int effortType,
			Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions,
			Locale locale, Map<Integer, String> totalBudgetShowValuesMap,
			Map<Integer, String> totalBudgetShowISOValuesMap,
			Map<Integer, Double> totalBudgetSortOrderValuesMap,
			Map<Integer, ProjectAccountingTO> projectAccountingTOMap, boolean plan) {
		Integer fieldID = null;
		Integer computedField = null;
		if (plan) {
			fieldID = FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.PLAN;
			computedField = TComputedValuesBean.COMPUTEDVALUETYPE.PLAN;
		} else {
			fieldID = FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.BUDGET;
			computedField = TComputedValuesBean.COMPUTEDVALUETYPE.BUDGET;
		}
		getValuesMap(workItemBeanList, fieldRestrictions, effortType, computedField, 
				null, false, fieldID, locale, 
				totalBudgetShowValuesMap, totalBudgetShowISOValuesMap, totalBudgetSortOrderValuesMap, projectAccountingTOMap);
	}*/
	/**
	 * Get the show values and sort order values for a remaining budget field
	 * @param workItemBeanList
	 * @param locale
	 * @param projectIDsToProjectBeansMap
	 * @param timeShowValuesMap in-out parameter: should be initialized (not null!)
	 * @param timeSortOrderValuesMap in-out parameter: should be initialized (not null!)
	 * @param costShowValuesMap in-out parameter: should be initialized (not null!)
	 * @param costSortOrderValuesMap in-out parameter: should be initialized (not null!)
	 */
	public static void setRemainingBudgetValuesMap(List<TActualEstimatedBudgetBean> remainingBudgetBeansList, Map<Integer, TWorkItemBean> workItemBeansMap,
			/*List<TWorkItemBean> workItemBeanList,*/ Locale locale, 
			Map<Integer, String> timeShowValuesMap, Map<Integer, String> timeShowISOValuesMap, Map<Integer, Double> timeSortOrderValuesMap,
			Map<Integer, String> costShowValuesMap, Map<Integer, String> costShowISOValuesMap, Map<Integer, Double> costSortOrderValuesMap,
			Map<Integer, ProjectAccountingTO> projectAccountingTOMap) {
		/*int[] workItemIDArr = GeneralUtils.createIntArrFromIntegerList(
				GeneralUtils.createIntegerListFromBeanList(workItemBeanList));
		Map<Integer, Integer> workItemIDToProjectID = getWorkItemIDToProjectID(workItemBeanList);
		List<TActualEstimatedBudgetBean> remainingBudgetBeansList = RemainingPlanBL.loadByWorkItemKeys(workItemIDArr);*/
		if (remainingBudgetBeansList!=null) {
			for (TActualEstimatedBudgetBean actualEstimatedBudgetBean : remainingBudgetBeansList) {
				Integer workItemKey = actualEstimatedBudgetBean.getWorkItemID();
				TWorkItemBean workItemBean = workItemBeansMap.get(workItemKey);
				if (workItemBean!=null) {
					Integer projectID = workItemBean.getProjectID();
					ProjectAccountingTO projectAccountingTO = projectAccountingTOMap.get(projectID);
					if (projectAccountingTO==null) {
						projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
						projectAccountingTOMap.put(projectID, projectAccountingTO);
					}
					Double timeValue = actualEstimatedBudgetBean.getEstimatedHours();
					if (timeValue!=null) {
						timeShowValuesMap.put(workItemKey, 
								getTimeShowValue(actualEstimatedBudgetBean, locale, false));
						timeShowISOValuesMap.put(workItemKey, 
								getTimeShowValue(actualEstimatedBudgetBean, locale, true));
						Integer measurementUnit = actualEstimatedBudgetBean.getTimeUnit();
						//transform the workdays to hours, because otherwise the sort order value is not real 
						if (measurementUnit!=null && measurementUnit.equals(AccountingBL.TIMEUNITS.WORKDAYS) && timeValue!=null) {
							Double hoursPerWorkingDay = projectAccountingTO.getHoursPerWorkday();
							if (hoursPerWorkingDay!=null) {
								timeValue =  new Double(timeValue.doubleValue() * hoursPerWorkingDay.doubleValue());
							}
						}
						timeSortOrderValuesMap.put(workItemKey, timeValue);
					}
					Double costValue = actualEstimatedBudgetBean.getEstimatedCost();
					if (costValue!=null) {
						costShowValuesMap.put(workItemKey, 
								getCostShowValue(actualEstimatedBudgetBean, locale, projectAccountingTO.getCurrency(), false));
						costShowISOValuesMap.put(workItemKey, 
								getCostShowValue(actualEstimatedBudgetBean, locale, projectAccountingTO.getCurrency(), true));
						costSortOrderValuesMap.put(workItemKey, costValue);
					}
				}
			}
		}
	}
	
	/**
	 * Load computed values for an array of workItems accessible by a person
	 * @param workItemBeanList
	 * @param loggedInPerson
	 * @param effortType 
	 * @param computedValueType
	 * @param personToFilter if filterByPerson then filter by the person (if null filter by IS NULL) 
	 * @param filterByPerson
	 * @param fieldID
	 * @return
	 */
	public static List<TComputedValuesBean> loadByWorkItemKeys(List<TWorkItemBean> workItemBeanList, Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions,
			int effortType, int computedValueType, Integer personToFilter, boolean filterByPerson, Integer fieldID) {
		int[] workItemIDArr = GeneralUtils.createIntArrFromIntegerCollection(
				GeneralUtils.createIntegerListFromBeanList(workItemBeanList));
		Map<Integer, TWorkItemBean> workItemsMap = GeneralUtils.createMapFromList(workItemBeanList);
		List<TComputedValuesBean> computedValueBeansList = null;
		if (filterByPerson) {
			//person can be null even when filterByPerson is true!
			computedValueBeansList = ComputedValueBL.loadByWorkItemsAndTypesAndPerson(workItemIDArr, effortType, computedValueType, personToFilter);
		} else {
			computedValueBeansList = ComputedValueBL.loadByWorkItemsAndTypes(workItemIDArr, effortType, computedValueType);
		}
		return filterByFieldRestrictions(computedValueBeansList, workItemsMap, fieldRestrictions, fieldID);
	}
	
	/**
	 * Filter the computed values by field restrictions
	 * @param computedValueBeansList
	 * @param workItemsMap
	 * @param fieldRestrictions
	 * @param fieldID
	 * @return
	 */
	private static List<TComputedValuesBean> filterByFieldRestrictions(List<TComputedValuesBean> computedValueBeansList, 
			Map<Integer, TWorkItemBean> workItemsMap, Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions, Integer fieldID) {
		if (fieldRestrictions!=null && !fieldRestrictions.isEmpty()) {
			//remove the budgets/planned values not visible for personID
			for (Iterator<TComputedValuesBean> iterator = computedValueBeansList.iterator(); iterator.hasNext();) {
				TComputedValuesBean budgetBean = iterator.next();
				Integer workItemID = budgetBean.getWorkitemKey(); 
				TWorkItemBean workItemBean = workItemsMap.get(workItemID);
				if (workItemBean!=null) {
					Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions.get(workItemBean.getProjectID());
					Map<Integer, Integer> hiddenFields = null;
					if (issueTypeRestrictions!=null) {
						hiddenFields = issueTypeRestrictions.get(workItemBean.getListTypeID());
					}
					if (hiddenFields!=null) {
						if (hiddenFields.containsKey(fieldID)) {
							iterator.remove();
						}
					}
				}
			}
		}
		return computedValueBeansList;
	}
	
	/**
	 * Load the showValuesMap and sortOrderValuesMap for a workItemIDArr
	 * @param workItemBeanList
	 * @param fieldRestrictions
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @param filterByPerson
	 * @param fieldID
	 * @param locale
	 * @param showValuesMap in-out parameter: should be initialized (not null!)
	 * @param sortOrderValuesMap in-out parameter: should be initialized (not null!)
	 * @param sortOrderValuesMap
	 * @param projectAccountingTOMap
	 * 
	 */
	private static void getValuesMap(List<TComputedValuesBean> computedValueBeansList, Map<Integer, TWorkItemBean> workItemsMap,
			Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions, Map<Integer, ProjectAccountingTO> projectAccountingTOMap,
			Integer fieldID, Locale locale, Map<Integer, String> showValuesMap, Map<Integer, String> showISOValuesMap, Map<Integer, Double> sortOrderValuesMap) {
		computedValueBeansList = filterByFieldRestrictions(computedValueBeansList, workItemsMap, fieldRestrictions, fieldID);
			/*loadByWorkItemKeys(workItemBeanList,
				fieldRestrictions, effortType, computedValueType, person, filterByPerson, fieldID);*/
		//Map<Integer, Integer> workItemIDToProjectID = getWorkItemIDToProjectID(workItemBeanList);
		if (computedValueBeansList!=null) {
			for (TComputedValuesBean computedValuesBean : computedValueBeansList) {
				Integer workItemKey = computedValuesBean.getWorkitemKey();
				TWorkItemBean workItemBean = workItemsMap.get(workItemKey);
				if (workItemBean!=null) {
					Integer projectID = workItemBean.getProjectID();
					ProjectAccountingTO projectAccountingTO = projectAccountingTOMap.get(projectID);
					if (projectAccountingTO==null) {
						projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
						projectAccountingTOMap.put(projectID, projectAccountingTO);
					}
					showValuesMap.put(workItemKey, getShowValue(computedValuesBean, locale, projectAccountingTO.getCurrency(), false));
					showISOValuesMap.put(workItemKey, getShowValue(computedValuesBean, locale, projectAccountingTO.getCurrency(), true));
					Double computedValue = computedValuesBean.getComputedValue();
					Integer measurementUnit = computedValuesBean.getMeasurementUnit();
					//transform the workdays to hours, because otherwise the sort order value is not real 
					if (measurementUnit!=null && measurementUnit.equals(AccountingBL.TIMEUNITS.WORKDAYS) && computedValue!=null) {
						Double hoursPerWorkingDay = projectAccountingTO.getHoursPerWorkday();
						if (hoursPerWorkingDay!=null) {
							computedValue = AccountingBL.transformToTimeUnits(computedValue, hoursPerWorkingDay, measurementUnit, AccountingBL.TIMEUNITS.HOURS);
						}
					}
					sortOrderValuesMap.put(workItemKey, computedValue);
				}
			}
		}
	}

	/**
	 * Create an association map between workItemID and projectID
	 * @param workItemBeanList
	 * @return
	 */
	private static Map<Integer, Integer> getWorkItemIDToProjectID(List<TWorkItemBean> workItemBeanList) {
		Map<Integer, Integer> workItemToProjectMap = new HashMap<Integer, Integer>();
		for (TWorkItemBean workItemBean : workItemBeanList) {
				workItemToProjectMap.put(workItemBean.getObjectID(), workItemBean.getProjectID());
		}
		return workItemToProjectMap;
	}
	
	/**
	 * Gets the show value for a TComputedValuesBean bean
	 * TODO in the future when more effort type will be possible this code should be implemented specific to each effort type  
	 * @param computedValuesBean
	 * @param locale
	 * @param numberUtils
	 * @param projectBean
	 * @return
	 */
	private static String getShowValue(TComputedValuesBean computedValuesBean, 
			Locale locale, String currency, boolean isISOValue) {
		StringBuffer stringBuffer = new StringBuffer();
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		int effortType = computedValuesBean.getEffortType().intValue();
		Double computedValue = computedValuesBean.getComputedValue();
		if (computedValue!=null) {
			if (isISOValue){
				stringBuffer.append(DoubleNumberFormatUtil.formatISO(computedValue));
			} else {
				stringBuffer.append(doubleNumberFormatUtil.formatGUI(computedValue, locale));
			}
		}
		if (effortType==TComputedValuesBean.EFFORTTYPE.TIME) {
			Integer measurementUnit = computedValuesBean.getMeasurementUnit();
			if (measurementUnit!=null) {
				stringBuffer.append(ISOShowValueSplitter + LocalizeUtil.getLocalizedTextFromApplicationResources(LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX + measurementUnit, locale));
			}
		} else {
			if (effortType==TComputedValuesBean.EFFORTTYPE.COST) {
				stringBuffer.append(ISOShowValueSplitter + currency);
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Gets the show value for time from an TActualEstimatedBudgetBean bean  
	 * @param actualEstimatedBudgetBean
	 * @param locale
	 * @param numberUtils	 
	 * @return
	 */
	private static String getTimeShowValue(TActualEstimatedBudgetBean actualEstimatedBudgetBean, 
			Locale locale, boolean isISOValue) {
		StringBuffer stringBuffer = new StringBuffer();
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		Double computedValue = actualEstimatedBudgetBean.getEstimatedHours();
		if (computedValue!=null) {
			if (isISOValue){
				stringBuffer.append(DoubleNumberFormatUtil.formatISO(computedValue));
			} else {
				stringBuffer.append(doubleNumberFormatUtil.formatGUI(computedValue, locale));
			}
		}
		Integer measurementUnit = actualEstimatedBudgetBean.getTimeUnit();
		if (measurementUnit!=null) {
				stringBuffer.append(ISOShowValueSplitter + LocalizeUtil.getLocalizedTextFromApplicationResources(LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX + measurementUnit, locale));
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Gets the show value for cost from an TActualEstimatedBudgetBean bean  
	 * @param actualEstimatedBudgetBean
	 * @param locale
	 * @param numberUtils
	 * @param projectBean
	 * @return
	 */
	private static String getCostShowValue(TActualEstimatedBudgetBean actualEstimatedBudgetBean, 
			Locale locale, String currency, boolean isISOValue) {
		StringBuffer stringBuffer = new StringBuffer();
		DoubleNumberFormatUtil doubleNumberFormatUtil = DoubleNumberFormatUtil.getInstance();
		Double computedValue = actualEstimatedBudgetBean.getEstimatedCost();
		if (computedValue!=null) {
			if (isISOValue){
				stringBuffer.append(DoubleNumberFormatUtil.formatISO(computedValue));
			} else {
				stringBuffer.append(doubleNumberFormatUtil.formatGUI(computedValue, locale));
			}
		}
		stringBuffer.append(ISOShowValueSplitter + currency);
		return stringBuffer.toString();
	}
}
