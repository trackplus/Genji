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

package com.aurel.track.item.massOperation;

import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TWorkItemBean;


/**
 * The context for massOperation fields
 * @author Tamas
 *
 */
public class MassOperationContext {
	private Integer personID;
	/**
	 * The list and labels for context (project/issueType) dependent custom lists 
	 */
	Map<Integer, Map<Integer, String>> fieldIDToListIDToLabels;
	/**
	 * Cache the projectBeans for labels in project specific lists (release lists) 
	 */
	private Set<Integer> requiredFields;
	
	//all the projects the selected issues are from (if no project is selected for mass operation) or the selected project
	//used for restricting the options which should be valid in each project (issueType, responsible, manager)
	//and initializing the project dependent fields (release, custom lists)
	private Integer[] involvedProjectsIDs;
	
	/**
	 * the project to issueTypes contexts
	 */
	private Map<Integer, Set<Integer>> projectIssueTypeContexts;
	
	//the first workItemBean selected: used to preset the select fields 
	//to the actual value of a field in the first workItemBean
	//(it can be that the other workItems has other values for this field, the idea is   
	//to avoid preselecting a select field to a value which is not the actual value of  
	//any of the selected workItemBeans, do not select automatically the first one from the list)
	private TWorkItemBean firstSelectedWorkItemBean;
	public Integer[] getInvolvedProjectsIDs() {
		return involvedProjectsIDs;
	}
	public void setInvolvedProjectsIDs(Integer[] affectedProjectsArr) {
		this.involvedProjectsIDs = affectedProjectsArr;
	}
	public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}	
	public TWorkItemBean getFirstSelectedWorkItemBean() {
		return firstSelectedWorkItemBean;
	}
	public void setFirstSelectedWorkItemBean(TWorkItemBean firstSelectedWorkItemBean) {
		this.firstSelectedWorkItemBean = firstSelectedWorkItemBean;
	}
	public Set<Integer> getRequiredFields() {
		return requiredFields;
	}
	public void setRequiredFields(Set<Integer> requiredFields) {
		this.requiredFields = requiredFields;
	}
	public Map<Integer, Map<Integer, String>> getFieldIDToListIDToLabels() {
		return fieldIDToListIDToLabels;
	}
	public void setFieldIDToListIDToLabels(
			Map<Integer, Map<Integer, String>> fieldIDToListIDToLabels) {
		this.fieldIDToListIDToLabels = fieldIDToListIDToLabels;
	}
	public Map<Integer, Set<Integer>> getProjectIssueTypeContexts() {
		return projectIssueTypeContexts;
	}
	public void setProjectIssueTypeContexts(
			Map<Integer, Set<Integer>> projectIssueTypeContexts) {
		this.projectIssueTypeContexts = projectIssueTypeContexts;
	}
	
	
}
