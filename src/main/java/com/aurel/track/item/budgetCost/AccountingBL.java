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


package com.aurel.track.item.budgetCost;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.numberFormatter.DoubleWithDecimalDigitsNumberFormatUtil;

/**
 * Business logic for accounting
 * Deals with costs/efforts for 
 * new issues (managed in session) and 
 * existing issues (managed in database)
 * @author Tamas Ruff
 *
 */
public abstract class AccountingBL {
	
	/**
	 * Represents the factor for the entire drawing area on the jsp, related to total budget
	 * Modifying this value needs adaptation on the jsp (table width="???")
	 */
	public static final double SCALINGFACTOR = 4;

	public static final Integer HUNDREDSCALED = Integer.valueOf ((int)(100/SCALINGFACTOR));
	
	//possible budget/expense fields
	public static class FIELDS {
		public static Integer EFFORT = Integer.valueOf(1);
		public static Integer COST = Integer.valueOf(2);
	}
	
	//possible time units
	public static interface TIMEUNITS {
		public static Integer HOURS = Integer.valueOf(1);
		public static Integer WORKDAYS = Integer.valueOf(2);
	}
	
	//int array of possible time units for getting the localized values from resources
	//(populating timeUnitOptions list)
	public static int[] TIMEUNITSARRAY= new int[]{TIMEUNITS.HOURS.intValue(), TIMEUNITS.WORKDAYS.intValue()};
	
	//the default hours per working day when not specified
	//(neither by person nor by project nor by project type)
	public static double DEFAULTHOURSPERWORKINGDAY = 8.0;
	
	//the significant decimal digits to work with
	//negative number means no limit
	public static final int COST_DECIMAL_DIGITS = 2;
	public static final int EFFORT_DECIMAL_DIGITS = 2;
	
	public static final int COST_ROUNDFACTOR = (int)Math.pow(10, COST_DECIMAL_DIGITS);
	public static final int EFFORT_ROUNDFACTOR = (int)Math.pow(10, EFFORT_DECIMAL_DIGITS);
	
	/**
	 * Actualize ancestor planned values (total and remaining)
	 * and expenses change after parent change or archive delete 
	 * @param workItemBean
	 * @param parentID
	 * @param parentOriginal
	 * @param errorsList
	 */
	public static synchronized void actualizeAncestorValues(TWorkItemBean workItemBean,
			Integer parentID, Integer parentOriginal, Integer personID) {
		Double hoursPerWorkingDay = ProjectBL.getHoursPerWorkingDay(workItemBean.getProjectID());
		Integer workItemID = workItemBean.getObjectID();
		if (parentID!=null) {
			actualizeAncestorValues(workItemID, parentID, personID, hoursPerWorkingDay);
		}
		if (parentOriginal!=null && !parentOriginal.equals(parentID)) {
			actualizeAncestorValues(workItemID, parentOriginal, personID, hoursPerWorkingDay);
		}
	}
	
	/**
	 * 
	 * @param workItemID
	 * @param parentID
	 * @param personID
	 * @param hoursPerWorkingDay
	 */
	private static synchronized void actualizeAncestorValues(Integer workItemID,
			Integer parentID, Integer personID, Double hoursPerWorkingDay) {
		if (parentID!=null) {
			//recalculate the parent hierarchy after a change
			//planned values
			ComputedValueBL.actualizeAncestorPlannedValuesOrExpenses(workItemID, parentID,
					TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, null, hoursPerWorkingDay);
			//total expenses
			ComputedValueBL.actualizeAncestorPlannedValuesOrExpenses(workItemID, parentID,
					TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null, hoursPerWorkingDay);
			//my expenses
			ComputedValueBL.actualizeAncestorPlannedValuesOrExpenses(workItemID, parentID,
					TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, personID, hoursPerWorkingDay);
			//remaining plans
			RemainingPlanBL.actualizeAncestorRemainingPlannedValues(workItemID, parentID, hoursPerWorkingDay);
		}
	}
	
	/**
	 * Round the double value according to the DECIMALDIGITS
	 * @param doubleValue
	 * @param hours hours whether to round to hours or cost
	 * @return
	 */
	public static Double roundToDecimalDigits(Double doubleValue, boolean hours) {
		if (doubleValue==null) {
			return null;
		}
		if (hours) {
			if (EFFORT_DECIMAL_DIGITS>=0) {
				return Double.valueOf((double)Math.round(doubleValue.doubleValue()*AccountingBL.EFFORT_ROUNDFACTOR)/AccountingBL.EFFORT_ROUNDFACTOR);
			}
		} else {
			if (COST_DECIMAL_DIGITS>=0) {
				return Double.valueOf((double)Math.round(doubleValue.doubleValue()*AccountingBL.COST_ROUNDFACTOR)/AccountingBL.COST_ROUNDFACTOR);
			}
		}
		return doubleValue;
	}
	
	/**
	 * Transforms the workingdays to hours when it is the case
	 * @param value
	 * @param hoursPerWorkingDay
	 * @param sourceTimeUnit
	 * @param targetTimeUnit
	 * @return
	 */
	public static Double transformToTimeUnits(Double value, Double hoursPerWorkingDay, Integer sourceTimeUnit, Integer targetTimeUnit) {
		if (value==null) {
			return new Double(0.0);
		}
		if (sourceTimeUnit==null || targetTimeUnit==null || sourceTimeUnit.intValue()==targetTimeUnit.intValue()) {
			return value;
		}
		double dblHours = value.doubleValue();
		double dblHoursPerWorkingDay = hoursPerWorkingDay.doubleValue();
		
		if (sourceTimeUnit.equals(TIMEUNITS.WORKDAYS) && targetTimeUnit.equals(TIMEUNITS.HOURS)) {
			dblHours *= dblHoursPerWorkingDay;
		} else {
			if (sourceTimeUnit.equals(TIMEUNITS.HOURS) && targetTimeUnit.equals(TIMEUNITS.WORKDAYS)) {
				dblHours /= dblHoursPerWorkingDay;
			}
		}
		return roundToDecimalDigits(new Double(dblHours), true);
	}
	
	/**
	 * Format the double value according to a locale
	 * If doubleValue is null it means probably a conversion error occured
	 * In this case return null and resolve this situation on the jsp
	 * @param doubleValue
	 * @param locale
	 * @param hours whether to format hours or cost
	 * @return
	 */
	public static String getFormattedDouble(Double doubleValue, Locale locale, boolean hours) {
		if (doubleValue!=null) {
			DoubleWithDecimalDigitsNumberFormatUtil doubleNumberFormatUtil;
			if (hours) {
				doubleNumberFormatUtil = DoubleWithDecimalDigitsNumberFormatUtil.getInstance(EFFORT_DECIMAL_DIGITS);
			} else {
				doubleNumberFormatUtil = DoubleWithDecimalDigitsNumberFormatUtil.getInstance(COST_DECIMAL_DIGITS);
			}
			return doubleNumberFormatUtil.formatGUI(doubleValue, locale);
		}
		return null;
	}
	
	//**********************************************************************************************************
	
	public static List<IntegerStringBean> getTimeUnitOptionsList(Locale locale) {
		return LocalizeUtil.getLocalizedList(
			LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX, AccountingBL.TIMEUNITSARRAY, locale);
	}
	
	public static String getTimeUnitOption(Integer timeUnit, Locale locale) {
		if (timeUnit!=null) {
			return LocalizeUtil.getLocalizedTextFromApplicationResources(LocalizationKeyPrefixes.TIME_UNIT_KEY_PREFIX + timeUnit, locale);
		}
		return "";
	}
	
	/**
	 * Save the budgets, efforts/costs form editBudgetCostsForm to database
	 * called after saving the new item (when workItemKey is available) 
	 * @param user
	 * @param editBudgetCostsForm
	 * @param workItemKey
	 */
	@SuppressWarnings("null") // the compiler doesn't get it here
	public static void saveAllFromSessionToDb(AccountingForm accountingForm, 
			TWorkItemBean workItemBean, TPersonBean personBean) {
		//save budget
		TBudgetBean budgetBean = accountingForm.getBudgetBean();
		if (budgetBean!=null && 
				((budgetBean.getEstimatedHours()!=null && !budgetBean.getEstimatedHours().equals(new Double(0.0))) ||
			(budgetBean.getEstimatedCost()!=null && !budgetBean.getEstimatedCost().equals(new Double(0.0))))) {
			//save only if specified a value either for effort or for cost
			//(it doesn't make sense to add 0.0 valued efforts and costs,
			//especially when accounting is not activated for project)
			BudgetBL.saveBudgetOrPlanToDb(budgetBean, null, personBean, false, workItemBean, false);
		}
		//save planned values
		budgetBean = accountingForm.getPlannedValueBean();
		if (budgetBean!=null && 
			(budgetBean.getEstimatedHours()!=null && !budgetBean.getEstimatedHours().equals(new Double(0.0))) ||
			(budgetBean.getEstimatedCost()!=null && !budgetBean.getEstimatedCost().equals(new Double(0.0)))) {
			//save only if specified a value either for effort or for cost
			//(it doesn't make sense to add 0.0 valued efforts and costs,
			//especially when accounting is not activated for project)
			BudgetBL.saveBudgetOrPlanToDb(budgetBean, null, personBean, true, workItemBean, false);
			/*if (ApplicationBean.getApplicationBean().getSiteBean().getSummaryItemsBehavior()) {
				BudgetPlanConsistencyBL.actualizeAncestorPlannedValues(workItemBean.getSuperiorworkitem(),
						AccountingBL.getHoursPerWorkingDay(null, workItemBean.getProjectID()));
			}*/
		}
		//first save the efforts/costs
		ExpenseBL.saveCostsToDb(accountingForm.getSessionCostBeans(), personBean, workItemBean);
		//just after the estimated remaining budget (for the case of auto adjust)
		//not firstBudget (do not recalculate the estimated remaining budget) and no costBean (do not modify the estimated remaining budget)
		//just leave the value calculated during adding the expenses and budget
		TActualEstimatedBudgetBean actualEstimatedBudgetBean =  accountingForm.getActualEstimatedBudgetBean();
		if (actualEstimatedBudgetBean!=null && (actualEstimatedBudgetBean.getEstimatedHours()!=null && !actualEstimatedBudgetBean.getEstimatedHours().equals(new Double(0.0))) ||
			(actualEstimatedBudgetBean.getEstimatedCost()!=null && !actualEstimatedBudgetBean.getEstimatedCost().equals(new Double(0.0)))) {
			RemainingPlanBL.saveRemainingPlanAndNotify(accountingForm.getActualEstimatedBudgetBean(),
				null, personBean, workItemBean, false);
		}
		//actualize ancestor hierarchy
		Integer parentID = workItemBean.getSuperiorworkitem();
		if (parentID!=null && ApplicationBean.getApplicationBean().getSiteBean().getSummaryItemsBehavior()) {
			Double hoursPerWorkingDay = ProjectBL.getHoursPerWorkingDay(workItemBean.getProjectID());
			actualizeAncestorValues(workItemBean.getObjectID(), parentID, personBean.getObjectID(), hoursPerWorkingDay);
		}
	}

	
}
