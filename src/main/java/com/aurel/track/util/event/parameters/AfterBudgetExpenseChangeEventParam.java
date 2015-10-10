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


package com.aurel.track.util.event.parameters;

import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;

public class AfterBudgetExpenseChangeEventParam extends AfterItemSaveEventParam {
	
	private TBudgetBean newBudget;
	private TBudgetBean oldBudget;
	private TBudgetBean newPlannedValue;
	private TBudgetBean oldPlannedValue;
	private TActualEstimatedBudgetBean newRemainingBudget;
	private TActualEstimatedBudgetBean oldRemainingBudget;
	private TCostBean newExpense;
	private TCostBean oldExpense;
	//the person who made the modifications
	private TPersonBean personBean;
	
	protected TWorkItemBean workItemBean;
	
	public TBudgetBean getNewBudget() {
		return newBudget;
	}
	public void setNewBudget(TBudgetBean newBudget) {
		this.newBudget = newBudget;
	}
	public TBudgetBean getOldBudget() {
		return oldBudget;
	}
	public void setOldBudget(TBudgetBean oldBudget) {
		this.oldBudget = oldBudget;
	}
	public TBudgetBean getNewPlannedValue() {
		return newPlannedValue;
	}
	public void setNewPlannedValue(TBudgetBean newPlannedValue) {
		this.newPlannedValue = newPlannedValue;
	}
	public TBudgetBean getOldPlannedValue() {
		return oldPlannedValue;
	}
	public void setOldPlannedValue(TBudgetBean oldPlannedValue) {
		this.oldPlannedValue = oldPlannedValue;
	}
	/**
	 * @return the oldExpense
	 */
	public TCostBean getOldExpense() {
		return oldExpense;
	}
	/**
	 * @param oldExpense the oldExpense to set
	 */
	public void setOldExpense(TCostBean oldExpense) {
		this.oldExpense = oldExpense;
	}
	/**
	 * @return the newExpense
	 */
	public TCostBean getNewExpense() {
		return newExpense;
	}
	/**
	 * @param newExpense the newExpense to set
	 */
	public void setNewExpense(TCostBean newExpense) {
		this.newExpense = newExpense;
	}
	/**
	 * @return the newRemainingBudget
	 */
	public TActualEstimatedBudgetBean getNewRemainingBudget() {
		return newRemainingBudget;
	}
	/**
	 * @param newRemainingBudget the newRemainingBudget to set
	 */
	public void setNewRemainingBudget(TActualEstimatedBudgetBean newRemainingBudget) {
		this.newRemainingBudget = newRemainingBudget;
	}
	/**
	 * @return the oldRemainingBudget
	 */
	public TActualEstimatedBudgetBean getOldRemainingBudget() {
		return oldRemainingBudget;
	}
	/**
	 * @param oldRemainingBudget the oldRemainingBudget to set
	 */
	public void setOldRemainingBudget(TActualEstimatedBudgetBean oldRemainingBudget) {
		this.oldRemainingBudget = oldRemainingBudget;
	}
	/**
	 * @return the workItemBean
	 */
	public TWorkItemBean getWorkItemBean() {
		return workItemBean;
	}
	/**
	 * @param workItemBean the workItemBean to set
	 */
	public void setWorkItemBean(TWorkItemBean workItemBean) {
		this.workItemBean = workItemBean;
	}
	/**
	 * @return the personBean
	 */
	public TPersonBean getPersonBean() {
		return personBean;
	}
	/**
	 * @param personBean the personBean to set
	 */
	public void setPersonBean(TPersonBean personBean) {
		this.personBean = personBean;
	}
	
	
}
