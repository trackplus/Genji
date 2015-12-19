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

package com.aurel.track.admin.customize.projectType.assignments.itemType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.ListOptionIconBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.projectType.assignments.ProjectTypeAssignmentBaseFacade;
import com.aurel.track.admin.customize.projectType.assignments.ProjectTypeAssignmentJSON;
import com.aurel.track.admin.customize.projectType.assignments.ProjectTypeAssignmentTokens;
import com.aurel.track.admin.customize.projectType.assignments.simple.ProjectTypeItemTypeAssignmentFacade;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.LabelValueBean;

/**
 * Facade for project type - item type specific assignments
 * @author Tamas
 *
 */
public abstract class ProjectTypeItemTypeAssignmentBaseFacade extends ProjectTypeAssignmentBaseFacade {
	
	public static String LINK_CHAR = "_";
	public static String iconLoadURL="projectTypeListAssignments!downloadIcon.action?node=";
	
	/**
	 * Encode a node
	 * @param projectTypeAssignmentTokens
	 * @return
	 */
	public static String encodeNode(ProjectTypeAssignmentTokens projectTypeAssignmentTokens){
		StringBuffer stringBuffer = new StringBuffer();
		Integer projectTypeID = projectTypeAssignmentTokens.getProjectTypeID();
		if (projectTypeID!=null) {
			stringBuffer.append(projectTypeID);
			Integer assignmentType = projectTypeAssignmentTokens.getAssignmentType();
			if (assignmentType!=null) {
				//roleID is null for account assignments but the LINK_CHAR 
				//should be added anyway to remain consistent over different assignments 
				stringBuffer.append(LINK_CHAR);
				stringBuffer.append(assignmentType);
				Integer issueTypeID = projectTypeAssignmentTokens.getIssueTypeID();
				if (issueTypeID!=null) {
					stringBuffer.append(LINK_CHAR);
					stringBuffer.append(issueTypeID);
					Integer entityID = projectTypeAssignmentTokens.getEntityID();
					if (entityID!=null) {
						stringBuffer.append(LINK_CHAR);
						stringBuffer.append(entityID);
					}
				}
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	public static ProjectTypeAssignmentTokens decodeNode(String id) {
		ProjectTypeAssignmentTokens projetcTypeAssignmentTokens = new ProjectTypeAssignmentTokens();
		if (id!=null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {
				projetcTypeAssignmentTokens.setProjectTypeID(Integer.valueOf(tokens[0]));
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						projetcTypeAssignmentTokens.setAssignmentType(Integer.valueOf(tokens[1]));
						if (tokens.length>2) {
							if (tokens[2]!=null && !"".equals(tokens[2])) {
								projetcTypeAssignmentTokens.setIssueTypeID(Integer.valueOf(tokens[2]));
								if (tokens.length>3) {
									if (tokens[3]!=null && !"".equals(tokens[3])) {
										projetcTypeAssignmentTokens.setEntityID(Integer.valueOf(tokens[3]));
									}
								}
							}
						}
					}
				}
			}
		}
		return projetcTypeAssignmentTokens;
	}
	
	public static String getProjectTypeAssignmnentBranchNodeID(Integer projectTypeID, Integer assignmentType) {
		return encodeNode(new ProjectTypeAssignmentTokens(projectTypeID, assignmentType));
	}
	
	/**
	 * Gets the assigned nodes for a project type
	 * @param node
	 * @param locale
	 * @return
	 */
	public List<TreeNodeBaseTO> getAssignedNodes(ProjectTypeAssignmentTokens projetcTypeAssignmentTokens, Locale locale) {
		List<TreeNodeBaseTO> assignedIssueTypeNodes = new LinkedList<TreeNodeBaseTO>();
		Integer projectTypeID = projetcTypeAssignmentTokens.getProjectTypeID();
		Integer assignmentType = projetcTypeAssignmentTokens.getAssignmentType();
		Integer issueTypeID = projetcTypeAssignmentTokens.getIssueTypeID();
		Map<Integer, Integer> entitiesForIssueType = loadNumberOfAssignmentsForIssueTypesInProjectType(projectTypeID);
		if (issueTypeID==null) {
			//the first level for status, priority and severity is the issueType level
			//the only level for issueType
			Set<Integer> assignedIssueTypeIDs = ProjectTypeItemTypeAssignmentFacade.getInstance().getAssignedIDsByProjectTypeID(projectTypeID);
			List<ILabelBean> labelBeans = IssueTypeBL.loadAll(locale);
			for (ILabelBean labelBean : labelBeans) {
				Integer localeIssueTypeID = labelBean.getObjectID();
				if (assignedIssueTypeIDs.isEmpty() || assignedIssueTypeIDs.contains(localeIssueTypeID)) {
					String nodeID = ProjectTypeItemTypeAssignmentBaseFacade.encodeNode(new ProjectTypeAssignmentTokens(projectTypeID, assignmentType, localeIssueTypeID));
					String label = labelBean.getLabel();
					if (entitiesForIssueType!=null) {
						Integer noOfEntities = entitiesForIssueType.get(localeIssueTypeID);
						if (noOfEntities!=null) {
							label = label + " (" + noOfEntities + ")";
						}
					}
					TreeNodeBaseTO treeNodeBaseTO = new TreeNodeBaseTO(
							nodeID, label, false);
					treeNodeBaseTO.setIcon(iconLoadURL+nodeID);
					assignedIssueTypeNodes.add(treeNodeBaseTO);
				}
			}
		} else {
			//the second level 
			Set<Integer> assigneIDs = getAssignedIDsByProjectTypeIDAndItemType(projectTypeID, issueTypeID);
			if (assigneIDs!=null) {
				List<ILabelBean> labelBeans = getAllAssignables(locale);
				for (ILabelBean labelBean : labelBeans) {
					if (assigneIDs.isEmpty() || assigneIDs.contains(labelBean.getObjectID())) {
						String nodeID = ProjectTypeItemTypeAssignmentBaseFacade.encodeNode(new ProjectTypeAssignmentTokens(projectTypeID, assignmentType, issueTypeID, labelBean.getObjectID()));
						TreeNodeBaseTO treeNodeBaseTO = new TreeNodeBaseTO(nodeID, labelBean.getLabel(), true);
						treeNodeBaseTO.setIcon(iconLoadURL+nodeID);
						assignedIssueTypeNodes.add(treeNodeBaseTO);
					}
				}
			}
		}
		return assignedIssueTypeNodes;
	}
	
	/**
	 * Gets the role with actualized label
	 * @param node
	 * @param locale
	 * @return
	 */
	public List<LabelValueBean> getIssueType(String node, ProjectTypeAssignmentTokens projetcTypeAssignmentTokens, Locale locale) {
		Integer projectTypeID = projetcTypeAssignmentTokens.getProjectTypeID();
		Integer issueTypeID = projetcTypeAssignmentTokens.getIssueTypeID();
		List<LabelValueBean> issueTypeLabelBeans = new LinkedList<LabelValueBean>();
		if (projectTypeID!=null && issueTypeID!=null) {
			String issueTypeLabel = LookupContainer.getLocalizedLabelBeanLabel(SystemFields.INTEGER_ISSUETYPE, issueTypeID, locale);
			Integer noOfEntities = loadNumberOfAssignmentsForIssueTypeInProjectType(projectTypeID, issueTypeID);
			if (noOfEntities!=null && noOfEntities.intValue()!=0) {
				issueTypeLabel = issueTypeLabel + " ("+noOfEntities + ")";
			}
			issueTypeLabelBeans.add(new LabelValueBean(issueTypeLabel, node));
		}
		return issueTypeLabelBeans;
	}
	
	/**
	 * Downloads the icon file
	 * @return
	 */
	public static void downloadTreeIcon(String node, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) {
		ProjectTypeAssignmentTokens projetcTypeAssignmentTokens = ProjectTypeItemTypeAssignmentBaseFacade.decodeNode(node); 
		Integer assignmentType = projetcTypeAssignmentTokens.getAssignmentType();
		Integer listID = null;
		Integer optionID = projetcTypeAssignmentTokens.getEntityID();
		if (optionID==null) {
			//issueType for status/priority/severity (first level):
			//get the issueTypeID as optionID
			optionID = projetcTypeAssignmentTokens.getIssueTypeID();
			//and ISSUETYPE as list
			listID = ListBL.RESOURCE_TYPES.ISSUETYPE;
		} else {
			switch (assignmentType.intValue()) {
			case ProjectTypesBL.PROJECT_TYPE_ASSIGNMENTS.ISSUE_TYPE_ASSIGNMENT:
				listID = ListBL.RESOURCE_TYPES.ISSUETYPE;
				break;
			case ProjectTypesBL.PROJECT_TYPE_ASSIGNMENTS.STATUS_ASSIGNMENT:
				listID = ListBL.RESOURCE_TYPES.STATUS;
				break;
			case ProjectTypesBL.PROJECT_TYPE_ASSIGNMENTS.PRIORITY_ASSIGNMENT:
				listID = ListBL.RESOURCE_TYPES.PRIORITY;
				break;
			case ProjectTypesBL.PROJECT_TYPE_ASSIGNMENTS.SEVERITY_ASSIGNMENT:
				listID = ListBL.RESOURCE_TYPES.SEVERITY;
				break;
			}
		}
		ListOptionIconBL.downloadForField(servletRequest, servletResponse, listID, optionID);
	}
	
	
	/**
	 * Gets the possible labelBeans to select, depending on the restrictions for projectType
	 * The labelBean of an existing assignment should always be included even if it is not any more valid for projectType  
	 * @param projectTypeID
	 * @param actualAssignedID
	 * @param locale
	 * @return
	 */
	public List<ILabelBean> getPossibleBeans(Integer projectTypeID, Integer itemTypeID, Integer actualAssignedID, Locale locale) {
		List<ILabelBean> labelBeans = getAllAssignables(locale);
		Set<Integer> assignedIDs = null;
		if (projectTypeID!=null) {
			assignedIDs = getAssignedIDsByProjectTypeIDAndItemType(projectTypeID, itemTypeID);
		}	
		if (labelBeans!=null && assignedIDs!=null && !assignedIDs.isEmpty()) {
			for (Iterator<ILabelBean> iterator = labelBeans.iterator(); iterator.hasNext();) {
				ILabelBean labelBean = iterator.next();
				Integer objectID = labelBean.getObjectID();
				if (!assignedIDs.contains(objectID) && (actualAssignedID==null || !objectID.equals(actualAssignedID))) {
						iterator.remove();
				}
			}
		}
		return labelBeans;
	}
	
	/**
	 * Encode the status assignments for a projectType and issueType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @param locale
	 * @return
	 */
	public String encodeJSON(Integer projectTypeID, Integer assignmentType, Integer issueTypeID, Locale locale) {
		String projectTypeLabel = "";
		if (projectTypeID!=null) {
			TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
			if (projectTypeBean!=null) {
				projectTypeLabel = projectTypeBean.getLabel();
			}
		}
		String issueTypeLabel = "";
		if (issueTypeID!=null) {
			ILabelBean labelBean = LookupContainer.getLocalizedLabelBean(SystemFields.INTEGER_ISSUETYPE, issueTypeID, locale);
			if (labelBean!=null) {
				issueTypeLabel = labelBean.getLabel();
			}
		}
		String assignmentInfo = null;
		List<ILabelBean> assigned = null;
		List<ILabelBean> unassigned = null;
		if (issueTypeID==null) {
			assignmentInfo = LocalizeUtil.getLocalizedTextFromApplicationResources(getAssignmentInfoGeneralKey(), locale);
		} else {
			Set<Integer> assignedIDs = getAssignedIDsByProjectTypeIDAndItemType(projectTypeID, issueTypeID);
			List<ILabelBean> labelBeans = getAllAssignables(locale);
			assigned = getAssignedBeans(labelBeans, assignedIDs, true);
			unassigned = getAssignedBeans(labelBeans, assignedIDs, false);
			assignmentInfo = LocalizeUtil.getParametrizedString(getAssignmentInfoKey(),
					new Object[] {projectTypeLabel, issueTypeLabel}, locale);
		}
		String reloadFromNode = ProjectTypeItemTypeAssignmentBaseFacade.encodeNode(new ProjectTypeAssignmentTokens(projectTypeID, assignmentType, issueTypeID));
		return ProjectTypeAssignmentJSON.encodeAssignment(assigned, unassigned, assignmentInfo,
				reloadFromNode, reloadFromNode, projectTypeID, assignmentType, issueTypeID);
	}		
	
	/**
	 * Whether the assignmnent is also item type based
	 * @return
	 */
	public boolean isItemTypeBased() {
		return true;
	}
	
	/**
	 * Gets the map with number of assignments for issue types in a projectType
	 * @param projectTypeID
	 * @return
	 */
	public abstract Map<Integer, Integer> loadNumberOfAssignmentsForIssueTypesInProjectType(Integer projectTypeID);
	
	/**
	 * Gets the number of assignments for an issue type in a projectType
	 * @param projectTypeID
	 * @param issueTypeID
	 * @return
	 */
	public abstract Integer loadNumberOfAssignmentsForIssueTypeInProjectType(Integer projectTypeID, Integer issueTypeID);
	
	/**
	 * Gets the already assigned IDs
	 * @param projectTypeID
	 * @return
	 */
	public abstract Set<Integer> getAssignedIDsByProjectTypeIDAndItemType(Integer projectTypeID, Integer itemTypeID);
	
	
	/**
	 * Gets the key for the assignment info general
	 * @return
	 */
	public abstract String getAssignmentInfoGeneralKey();
	
	
	
	
	
	
}
