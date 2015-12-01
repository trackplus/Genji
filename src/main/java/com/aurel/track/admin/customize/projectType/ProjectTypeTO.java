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

package com.aurel.track.admin.customize.projectType;

import java.util.List;

import com.aurel.track.util.IntegerStringBean;

/**
 * Transfer object used for project type fields
 * @author Tamas Ruff
 *
 */
public class ProjectTypeTO {
	private String label;
	private Integer projectTypeFlag;
	private List<IntegerStringBean> projectTypeFlagList;
	private List<IntegerStringBean> workUnitsList;
	private Double hoursPerWorkday;
	private Integer defaultWorkUnit;
	private String currencyName;
	private String currencySymbol;
	protected Boolean useReleases;
	protected Boolean useVersionControl;
	protected Boolean useAccounts;
	protected Boolean useMsProjectExportImport;
	//not used: negative projectTypeID means private project type
	protected boolean forPrivateProjects;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getProjectTypeFlag() {
		return projectTypeFlag;
	}
	public void setProjectTypeFlag(Integer projectTypeFlag) {
		this.projectTypeFlag = projectTypeFlag;
	}
	public List<IntegerStringBean> getProjectTypeFlagList() {
		return projectTypeFlagList;
	}
	public void setProjectTypeFlagList(List<IntegerStringBean> projectTypeFlagList) {
		this.projectTypeFlagList = projectTypeFlagList;
	}
	public Double getHoursPerWorkday() {
		return hoursPerWorkday;
	}
	public void setHoursPerWorkday(Double hoursPerWorkday) {
		this.hoursPerWorkday = hoursPerWorkday;
	}
	public Boolean getUseReleases() {
		return useReleases;
	}
	public void setUseReleases(Boolean useReleases) {
		this.useReleases = useReleases;
	}
	public Boolean getUseVersionControl() {
		return useVersionControl;
	}
	public void setUseVersionControl(Boolean useVersionControl) {
		this.useVersionControl = useVersionControl;
	}
	public Boolean getUseAccounts() {
		return useAccounts;
	}
	public void setUseAccounts(Boolean useAccounts) {
		this.useAccounts = useAccounts;
	}
	public boolean getForPrivateProjects() {
		return forPrivateProjects;
	}
	public void setForPrivateProjects(boolean forPrivateProjects) {
		this.forPrivateProjects = forPrivateProjects;
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
	public List<IntegerStringBean> getWorkUnitsList() {
		return workUnitsList;
	}
	public void setWorkUnitsList(List<IntegerStringBean> workUnitsList) {
		this.workUnitsList = workUnitsList;
	}
	public Boolean getUseMsProjectExportImport() {
		return useMsProjectExportImport;
	}
	public void setUseMsProjectExportImport(Boolean useMsProjectExportImport) {
		this.useMsProjectExportImport = useMsProjectExportImport;
	}
	
	
}
