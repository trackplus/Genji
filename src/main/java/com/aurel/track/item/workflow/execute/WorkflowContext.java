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

package com.aurel.track.item.workflow.execute;

import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.util.EqualUtils;


/**
 * The context for workflow connects
 * @author Tamas
 *
 */
public class WorkflowContext {
	
	public static enum Hierarchy {CHILD, PARTIAL_MATCH, ORTHOGONAL};
	
	private Integer itemTypeID = null;
	private Integer projectTypeID = null;
	private Integer projectID = null;
	
	
	public WorkflowContext(Integer itemTypeID, Integer projectTypeID,
			Integer projectID) {
		super();
		this.itemTypeID = itemTypeID;
		this.projectTypeID = projectTypeID;
		this.projectID = projectID;
	}
	
	public Integer getItemTypeID() {
		return itemTypeID;
	}
	public void setItemTypeID(Integer itemTypeID) {
		this.itemTypeID = itemTypeID;
	}
	public Integer getProjectTypeID() {
		return projectTypeID;
	}
	public void setProjectTypeID(Integer projectTypeID) {
		this.projectTypeID = projectTypeID;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	
	private boolean hasItemType() {
		return itemTypeID!=null;
	}
	
	private boolean hasProjectType() {
		return projectTypeID!=null;
	}
	
	private boolean hasProject() {
		return projectID!=null;
	}
	
	/**
	 * Whether there is a hierarchy relationship between two contexts
	 * The "this" object is supposed to be more "general" as the method parameter
	 * @param workflowContext
	 * @return
	 */
	public Hierarchy compare(WorkflowContext workflowContext) {
		if (this.itemTypeID==null && this.projectTypeID==null && this.projectID==null) {
			//"this" is standard context. Any other context is child
			return Hierarchy.CHILD;
		}
		Integer otherProjectID = workflowContext.getProjectID();
		if (this.hasItemType()) {
			if (this.hasProjectType()) {
				//"project type and item type" set for "this"
				if (workflowContext.hasItemType() && workflowContext.hasProject()) {
					//"project and item type" set for workflowContext
					TProjectBean projectBean = LookupContainer.getProjectBean(otherProjectID);
					if (projectBean!=null) {
						if (EqualUtils.equal(this.projectTypeID, projectBean.getProjectType())) {
							//if project is of same projecType: (projectType - itemType) to (project - itemType)
							return Hierarchy.CHILD;
						}
					}
				}
				//any other combination to "project type and item type" for "this" is orthogonal for workflowContext
				return Hierarchy.ORTHOGONAL;
			} else { 
				if (this.hasProject()) {
					//"project and item type" set for "this". Most specific it can't have children so it is orthogonal
					return Hierarchy.ORTHOGONAL;
				} else {
					//only "item type" set for "this"
					if (workflowContext.hasItemType()) {
						if (workflowContext.hasProjectType() || workflowContext.hasProject()) {
							//"(project type or project) and item type" set for workflowContext
							if (this.itemTypeID.equals(workflowContext.getItemTypeID())) {
								//the same item type overridden "project or project type" specific
								return Hierarchy.CHILD;
							} else {
								return Hierarchy.ORTHOGONAL;
							}
						}
					} else {
						if (workflowContext.hasProjectType() || workflowContext.hasProject()) {
							//"project type or project" without item type set for workflowContext
							return Hierarchy.PARTIAL_MATCH;
						}
					}
					return Hierarchy.ORTHOGONAL;
				}
			}
		} else {
			if (this.hasProjectType()) {
				//only project type
				if (otherProjectID!=null) {
					TProjectBean projectBean = LookupContainer.getProjectBean(otherProjectID);
					if (projectBean!=null) {
						if (EqualUtils.equal(this.projectTypeID, projectBean.getProjectType())) {
							//if project is of same projecType: "projectType" to ("project" or "project and itemType")
							return Hierarchy.CHILD;
						}
					}
				}
				//any other combination to project type is orthogonal
				return Hierarchy.ORTHOGONAL;
			} else {
				if (this.hasProject()) {
					//only project
					if (workflowContext.hasProject() && workflowContext.hasItemType()) {
						if (EqualUtils.equal(this.projectID, otherProjectID)) {
							//projectType to (project with itemType): child if project is the same
							return Hierarchy.CHILD;
						}
					}
				}
				return Hierarchy.ORTHOGONAL;
			}
		}
	}

	@Override
	public String toString() {
		if (itemTypeID==null && projectTypeID==null && projectID==null) {
			return "Context [standard]";
		}
		StringBuilder stringBuilder = new StringBuilder();
		if (itemTypeID!=null) {
			stringBuilder.append("itemTypeID=" + LookupContainer.getNotLocalizedLabelBeanLabel(SystemFields.INTEGER_ISSUETYPE, itemTypeID) + " (" + itemTypeID + ")");
		}
		if (projectTypeID!=null) {
			TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
			if (projectTypeBean!=null) {
				if (stringBuilder.length()>0) {
					stringBuilder.append(", ");
				}
				stringBuilder.append("projectTypeID=" + projectTypeBean.getLabel() +  " (" + projectTypeID + ")");
			}
		}
		if (projectID!=null) {
			if (stringBuilder.length()>0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append("projectID=" + LookupContainer.getNotLocalizedLabelBeanLabel(SystemFields.INTEGER_PROJECT, projectID) + " (" + projectID + ")");
		}
		return "Context [" + stringBuilder.toString() + "]";
	}
	
	
	
}
