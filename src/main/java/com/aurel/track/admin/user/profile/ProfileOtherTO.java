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

package com.aurel.track.admin.user.profile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.TreeNode;

/**
 * Transfer object used for user profile "other" tab configuration.
 * @author Joerg Friedrich
 */
public class ProfileOtherTO implements Serializable{

	private static final long serialVersionUID = 400L;
	private String phone;
	private List<TreeNode> departmentTree;
	private Integer department;
	private Double workingHours;
	private Double hourlyWage;
	private Double extraHourWage;
	private String employeeId;
	private Date lastLogin;
	private String csvSeparator;
	private String csvEncoding;
	private boolean saveAttachments;
	private Integer sessionTimeout;
	private List<LabelValueBean> designPaths;
	private String designPath;
	private boolean activateInlineEdit;
	private boolean activateLayout;
	private String homePage;
	private List<ILabelBean> substitutePersonsList; 
	private Integer substituteID;
	private Integer userLevel;
	private List<IntegerStringBean> userLevelList;
	private boolean readOnly;
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Double getWorkingHours() {
		return workingHours;
	}
	public void setWorkingHours(Double workingHours) {
		this.workingHours = workingHours;
	}
	
	public Double getHourlyWage() {
		return hourlyWage;
	}
	public void setHourlyWage(Double hourlyWage) {
		this.hourlyWage = hourlyWage;
	}
	public Double getExtraHourWage() {
		return extraHourWage;
	}
	public void setExtraHourWage(Double extraHourWage) {
		this.extraHourWage = extraHourWage;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String personId) {
		this.employeeId = personId;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getCsvSeparator() {
		return csvSeparator;
	}
	public void setCsvSeparator(String csvSeparator) {
		this.csvSeparator = csvSeparator;
	}
	public String getCsvEncoding() {
		return csvEncoding;
	}
	public void setCsvEncoding(String csvEncoding) {
		this.csvEncoding = csvEncoding;
	}	
	public Integer getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public List<LabelValueBean> getDesignPaths() {
		return designPaths;
	}
	public void setDesignPaths(List<LabelValueBean> designPaths) {
		this.designPaths = designPaths;
	}
	public boolean isActivateInlineEdit() {
		return activateInlineEdit;
	}
	public void setActivateInlineEdit(boolean activateInlineEdit) {
		this.activateInlineEdit = activateInlineEdit;
	}
	public String getDesignPath() {
		return designPath;
	}
	public void setDesignPath(String designPath) {
		this.designPath = designPath;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public Integer getDepartment() {
		return this.department;
	}
    public List<TreeNode> getDepartmentTree() {
        return departmentTree;
    }
    public void setDepartmentTree(List<TreeNode> departmentTree) {
        this.departmentTree = departmentTree;
    }
    public boolean isSaveAttachments() {
		return saveAttachments;
	}
	public void setSaveAttachments(boolean saveAttachments) {
		this.saveAttachments = saveAttachments;
	}
	public boolean isActivateLayout() {
		return activateLayout;
	}
	public void setActivateLayout(boolean activateLayout) {
		this.activateLayout = activateLayout;
	}

	public String getHomePage() {
		return homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public Integer getSubstituteID() {
		return substituteID;
	}
	public void setSubstituteID(Integer substituteID) {
		this.substituteID = substituteID;
	}
	public List<ILabelBean> getSubstitutePersonsList() {
		return substitutePersonsList;
	}
	public void setSubstitutePersonsList(List<ILabelBean> substitutePersonsList) {
		this.substitutePersonsList = substitutePersonsList;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public List<IntegerStringBean> getUserLevelList() {
		return userLevelList;
	}
	public void setUserLevelList(List<IntegerStringBean> userLevelList) {
		this.userLevelList = userLevelList;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
	
}
