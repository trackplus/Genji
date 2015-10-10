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

package com.aurel.track.admin.project;

import java.util.List;

import com.aurel.track.beans.TAccountBean;
import com.aurel.track.util.IntegerStringBean;


/**
 * Accounting features for a project
 * @author Tamas Ruff
 *
 */
public class ProjectAccountingTO {
	private boolean accountingInherited;
	private boolean projectTypeHasAccountig;
	private boolean workTracking;
	private Double hoursPerWorkday;
	private Integer defaultWorkUnit;
	private List<IntegerStringBean> workUnitList;
	private boolean costTracking;
	private String currencyName;
	private String currencySymbol;
	private Integer defaultAccount;
	private List<TAccountBean> accountList;
	
	public boolean isWorkTracking() {
		return workTracking;
	}
	public void setWorkTracking(boolean workTracking) {
		this.workTracking = workTracking;
	}
	public boolean isCostTracking() {
		return costTracking;
	}
	public void setCostTracking(boolean costTracking) {
		this.costTracking = costTracking;
	}
	public Double getHoursPerWorkday() {
		return hoursPerWorkday;
	}
	public void setHoursPerWorkday(Double hoursPerWorkday) {
		this.hoursPerWorkday = hoursPerWorkday;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public Integer getDefaultWorkUnit() {
		return defaultWorkUnit;
	}
	public void setDefaultWorkUnit(Integer defaultWorkUnit) {
		this.defaultWorkUnit = defaultWorkUnit;
	}
	public Integer getDefaultAccount() {
		return defaultAccount;
	}
	public void setDefaultAccount(Integer defaultAccount) {
		this.defaultAccount = defaultAccount;
	}
	public boolean isProjectTypeHasAccountig() {
		return projectTypeHasAccountig;
	}
	public void setProjectTypeHasAccountig(boolean projectTypeHasAccountig) {
		this.projectTypeHasAccountig = projectTypeHasAccountig;
	}
	public List<TAccountBean> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<TAccountBean> accountList) {
		this.accountList = accountList;
	}
	public List<IntegerStringBean> getWorkUnitList() {
		return workUnitList;
	}
	public void setWorkUnitList(List<IntegerStringBean> workUnitList) {
		this.workUnitList = workUnitList;
	}
	public boolean isAccountingInherited() {
		return accountingInherited;
	}
	public void setAccountingInherited(boolean accountingInherited) {
		this.accountingInherited = accountingInherited;
	}
	
	public String getCurrency() {
		String currencySymbol = getCurrencySymbol();
		if (currencySymbol!=null && !"".equals(currencySymbol.trim())) {
			return currencySymbol;
		}
		String currencyName = getCurrencyName();
		if (currencyName!=null && !"".equals(currencyName.trim())) {
			return currencyName;
		}
		return "";
	}
	
}
