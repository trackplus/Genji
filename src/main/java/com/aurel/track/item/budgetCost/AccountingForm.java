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

import java.util.SortedMap;

import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;

/**
 * Bean component for storing all the data linked with efforts/costs for bugets/expenses
 * @author Tamas Ruff
 *
 */
public class AccountingForm  {

		
	public static int DESCRIPTIONLENGTH = 255;
	public static int SUBJECTLENGTH = 255;
	

	//******************************************
	private SortedMap<Integer, TCostBean> sessionCostBeans;
	private Integer sessionCounter;
	private TBudgetBean plannedValueBean = new TBudgetBean();
	private TBudgetBean budgetBean = new TBudgetBean();
	private TActualEstimatedBudgetBean actualEstimatedBudgetBean = new TActualEstimatedBudgetBean();
	
	public SortedMap<Integer, TCostBean> getSessionCostBeans() {
		return sessionCostBeans;
	}

	public void setSessionCostBeans(SortedMap<Integer, TCostBean> sessionCostBeans) {
		this.sessionCostBeans = sessionCostBeans;
	}
	
	public TBudgetBean getBudgetBean() {
		return budgetBean;
	}

	public void setBudgetBean(TBudgetBean budgetBean) {
		this.budgetBean = budgetBean;
	}

	public TBudgetBean getPlannedValueBean() {
		return plannedValueBean;
	}

	public void setPlannedValueBean(TBudgetBean budgetBean) {
		this.plannedValueBean = budgetBean;
	}
	
	public TActualEstimatedBudgetBean getActualEstimatedBudgetBean() {
		return actualEstimatedBudgetBean;
	}

	public void setActualEstimatedBudgetBean(
			TActualEstimatedBudgetBean actualEstimatedBudgetBean) {
		this.actualEstimatedBudgetBean = actualEstimatedBudgetBean;
	}

	public Integer getSessionCounter() {
		return sessionCounter;
	}

	public void setSessionCounter(Integer sessionCounter) {
		this.sessionCounter = sessionCounter;
	}
}
