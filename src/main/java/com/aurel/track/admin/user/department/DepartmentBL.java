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

package com.aurel.track.admin.user.department;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.user.group.GroupJSON;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TDepartmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.DepartmentDAO;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.TreeNode;

/**
 * This class contains the application logic for the handling of organizational units.
 *
 */
public class DepartmentBL {	
	private static DepartmentDAO departmentDAO = DAOFactory.getFactory().getDepartmentDAO();
	
	private static String LINK_CHAR = "_";
		
	public static String USER_ICON_CLASS = "user-ticon";
	private static String DEPARTMENT_ICON_CLASS = "department-ticon";
	
	public static List<TDepartmentBean> getAllDepartments() {
		return departmentDAO.loadAll();
	}
	
	/**
	 * Load an organizational unit by its primary key or object id.
	 * @param objectID
	 * @return the department
	 */
	public static TDepartmentBean loadByPrimaryKey(Integer objectID) {
		return departmentDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Gets the default department
	 * @return object id of default department
	 */
	public static Integer getDefaultDepartment() {
		return departmentDAO.getDefaultDepartment();
	}
	
	/**
	 * Saves a department
	 * @param departmentBean
	 * @return the object id of the department
	 */
	public static Integer save(TDepartmentBean departmentBean) {
		return departmentDAO.save(departmentBean);
	}

	/**
	 * Deletes a department
	 * @param departmentID the object id of the department to be deleted
	 */
	public static void delete(Integer departmentID) {
		departmentDAO.delete(departmentID);
	}
	
	/**
	 * Encode a node
	 * @param personInDepartmentTokens
	 * @return
	 */
	public static String encodeNode(PersonInDepartmentTokens personInDepartmentTokens){
		StringBuffer stringBuffer = new StringBuffer();
		Integer departmentID = personInDepartmentTokens.getDepartmentID();
		Integer personID = personInDepartmentTokens.getPersonID();
		if (departmentID!=null) {
			stringBuffer.append(departmentID);
			if (personID!=null) {
				stringBuffer.append(LINK_CHAR);
				stringBuffer.append(personID);
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	public static PersonInDepartmentTokens decodeNode(String id) {
		PersonInDepartmentTokens personInDepartmentTokens = new PersonInDepartmentTokens();
		if (id!=null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {	
				personInDepartmentTokens.setDepartmentID(Integer.valueOf(tokens[0]));
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						personInDepartmentTokens.setPersonID(Integer.valueOf(tokens[1]));
					}
				}
			}
		}
		return personInDepartmentTokens;
	}

	/**
	 * Gets the assigned nodes for a project
	 * @param node
     * @param includePersons
	 * @param locale
	 * @return
	 */
	public static List<TreeNodeBaseTO> getChildren(
			String node, boolean includePersons, Locale locale) {
		PersonInDepartmentTokens personInDepartmentTokens = decodeNode(node);
		List<TreeNodeBaseTO> assignedNodes = new LinkedList<TreeNodeBaseTO>();
		Integer departmentID = personInDepartmentTokens.getDepartmentID();
		List<TDepartmentBean> departmentBeans = null;
		if (departmentID==null) {
			departmentBeans = departmentDAO.loadMainDepartments();
		} else {
			departmentBeans = departmentDAO.loadSubdepartments(departmentID);
		}
		if (departmentBeans!=null) {
			List<Integer> departmentIDs = GeneralUtils.createIntegerListFromBeanList(departmentBeans);
			Map<Integer, Integer> numberOfPersonsInDepartments = PersonBL.loadNumberOfPersonsInDepartments(departmentIDs);
			for (TDepartmentBean departmentBean : departmentBeans) {
				//department entries
				Integer localDepartmentID = departmentBean.getObjectID();
				personInDepartmentTokens = new PersonInDepartmentTokens(localDepartmentID);
				String label = departmentBean.getLabel();
				Integer numberOfPersons = numberOfPersonsInDepartments.get(localDepartmentID);
				if (numberOfPersons!=null) {
					label = label + " (" + numberOfPersons + ")";
				}
				TreeNodeBaseTO treeNodeTO = new TreeNodeBaseTO(
						encodeNode(personInDepartmentTokens), label, DEPARTMENT_ICON_CLASS, false);
				assignedNodes.add(treeNodeTO);
			}
		}
		if (includePersons && departmentID!=null) {
			List<TPersonBean> personBeans = PersonBL.loadPersonsForDepartment(departmentID);
			if (personBeans!=null) {
				for (TPersonBean personBean : personBeans) {
					personInDepartmentTokens = new PersonInDepartmentTokens(departmentID, personBean.getObjectID());
					TreeNodeBaseTO treeNodeTO = new TreeNodeBaseTO(
							encodeNode(personInDepartmentTokens), personBean.getLabel(), USER_ICON_CLASS, true);
					assignedNodes.add(treeNodeTO);
				}
			}
		}
		return assignedNodes;
	}
	
	/**
	 * Gets the personBeans with actualized label
	 * @param node
     * @param oldDepartments
	 * @return
	 */
	public static List<LabelValueBean> getParents(String node, String oldDepartments) {
		PersonInDepartmentTokens personInDepartmentTokens = decodeNode(node);
		Integer newDepartmentID = personInDepartmentTokens.getDepartmentID();
		List<Integer> departmentIDsList = new LinkedList<Integer>();
		departmentIDsList.add(newDepartmentID);
		if (oldDepartments!=null && !"".equals(oldDepartments)) {
			departmentIDsList.addAll(GeneralUtils.createIntegerListFromStringArr(oldDepartments.split(",")));
		}
		List<LabelValueBean> groupLabelBeans = new LinkedList<LabelValueBean>(); 
		List<TDepartmentBean> departmentBeans = departmentDAO.loadByKeys(departmentIDsList);
		Map<Integer, Integer> numberOfPersonsInGroupsMap = PersonBL.loadNumberOfPersonsInDepartments(departmentIDsList);
		for (TDepartmentBean departmentBean : departmentBeans) {
			Integer departmentKey = departmentBean.getObjectID();
			Integer numberOfPersons = numberOfPersonsInGroupsMap.get(departmentKey);
			String departmentLabel = departmentBean.getLabel();
			if (numberOfPersons!=null) {
				departmentLabel = departmentLabel + " ("+numberOfPersons + ")";
			}
			groupLabelBeans.add(new LabelValueBean(departmentLabel, departmentKey.toString()));
		}
		return groupLabelBeans;
	}
	
	public static List<TreeNode> getDepartmentNodesEager(Integer excludeID,
			Set<Integer> selectedDepartmentIDs, boolean useChecked) {
		List<TDepartmentBean> departmentBeans = departmentDAO.loadMainDepartments();
		return getDepartmentNodesEager(excludeID, departmentBeans, selectedDepartmentIDs, useChecked);
	}
	
	/**
	 * Gets releases eagerly (the entire hierarchy at once)
     * @param excludeID
	 * @param departmentBeans
	 * @param selectedDepartmentIDs
	 * @param useChecked
	 * @return
	 */
	private static List<TreeNode> getDepartmentNodesEager(Integer excludeID, List<TDepartmentBean> departmentBeans,
			Set<Integer> selectedDepartmentIDs, boolean useChecked) {
		List<TreeNode> departmentTreeNodesList = new LinkedList<TreeNode>();
		List<Integer> parentIDs = new LinkedList<Integer>();
		Map<Integer, TreeNode> departmentTreeNodeMap = new HashMap<Integer, TreeNode>();
		//add the main departments to the list
		for (TDepartmentBean departmentBean : departmentBeans) {
			Integer departmentID = departmentBean.getObjectID();
			if (excludeID==null || !excludeID.equals(departmentID)) {
				boolean checked = false;
				if (useChecked) {
					checked = selectedDepartmentIDs!=null && selectedDepartmentIDs.contains(departmentID);
				}
				TreeNode mainReleaseTreeNode = createDepartmentTreeNode(departmentID, departmentBean.getLabel(), useChecked, checked);
				//not known previously whether this project has subprojects 
				mainReleaseTreeNode.setLeaf(Boolean.TRUE);
				parentIDs.add(departmentID);
				departmentTreeNodeMap.put(departmentID, mainReleaseTreeNode);
				departmentTreeNodesList.add(mainReleaseTreeNode);
			}
		}
		//get the subdepartments
		while (!parentIDs.isEmpty()) {
			departmentBeans = departmentDAO.loadSubdepartments(parentIDs);
			parentIDs = new LinkedList<Integer>();
			for (TDepartmentBean departmentBean : departmentBeans) {
				Integer departmentID = departmentBean.getObjectID();
				if (excludeID==null || !excludeID.equals(departmentID)) {
					Integer parentID = departmentBean.getParent();
					boolean checked = false;
					if (useChecked) {
						checked = selectedDepartmentIDs!=null && selectedDepartmentIDs.contains(departmentID);
					}
					TreeNode childNode = createDepartmentTreeNode(departmentID, departmentBean.getLabel(), useChecked, checked);
					TreeNode parentTreeNode = departmentTreeNodeMap.get(parentID);
					if (parentTreeNode!=null) {
						List<TreeNode> childrenNodes = parentTreeNode.getChildren();
						if (childrenNodes==null) {
							//reset leaf to false once a child is found
							parentTreeNode.setLeaf(Boolean.FALSE);
							childrenNodes = new LinkedList<TreeNode>();
							parentTreeNode.setChildren(childrenNodes);
						}
						departmentTreeNodeMap.put(departmentID, childNode);
						childrenNodes.add(childNode);
					}
					parentIDs.add(departmentID);
				}
			}
		}
		return departmentTreeNodesList;
	}
	
	/**
	 * Creates a treeNode from a projectBean
	 * @param objectID
     * @param label
     * @param useChecked
     * @param checked
	 * @return
	 */
	public static TreeNode createDepartmentTreeNode(Integer objectID, String label, boolean useChecked, boolean checked) {
		TreeNode treeNode = new TreeNode(objectID.toString(), label, DEPARTMENT_ICON_CLASS);
		if (useChecked) {
			treeNode.setChecked(Boolean.valueOf(checked));
		}
		return treeNode;
	}
	
	/**
	 * Get the detail JSON for edit/add 
	 * @param node
	 * @param add
	 * @return
	 */
	public static String getEditDetailJSON(String node, boolean add) {
		PersonInDepartmentTokens personInDepartmentTokens = decodeNode(node);
		Integer departmentID = personInDepartmentTokens.getDepartmentID();
		String departmentName = null;
		if (departmentID!=null && !add) { 
			TDepartmentBean departmentBean = departmentDAO.loadByPrimaryKey(departmentID);
			if (departmentBean!=null) {
				departmentName = departmentBean.getLabel();
			}
		}
		return GroupJSON.getDepartmentEditDetailJSON(departmentName);
	}
	
	/**
	 * Get the detail JSON for save
	 * @param node
	 * @param add
	 * @param name
	 * @param locale
	 * @return
	 */
	public static String getSaveDetailJSON(String node, boolean add, boolean addAsSubdepartment, String name, Locale locale) {
		PersonInDepartmentTokens personInDepartmentTokens = decodeNode(node);
		Integer departmentID = personInDepartmentTokens.getDepartmentID();
		String errorMessage = isValidLabel(departmentID, name, add, addAsSubdepartment, locale);
		String jsonResult;
		if (errorMessage==null) {
			jsonResult = save(departmentID, name, add, addAsSubdepartment);
		} else {
			jsonResult = JSONUtility.encodeJSONFailure(errorMessage);
		}
		return jsonResult;
	}
	
	/**
	 * Whether the label is valid (typically not duplicated) 
	 * @param objectID
	 * @param add
	 * @return
	 */
	private static String isValidLabel(Integer objectID, String name,
			boolean add, boolean addAsSubdepartment, Locale locale) {
		Integer parentID = null;
		if (addAsSubdepartment) {
			parentID = objectID;
		}
		List<TDepartmentBean> departmentBeans = departmentDAO.loadByName(name, parentID);
		if (departmentBeans==null || departmentBeans.isEmpty()) {
			return null;
		} else {
			if (departmentBeans.size()>1 || objectID==null ||
					(objectID!=null && !departmentBeans.get(0).getObjectID().equals(objectID))) {
				return LocalizeUtil.getParametrizedString("common.err.unique", 
					new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.user.department.lbl.department", locale)} , locale);
			}
		}
		return null;
	}
	
	/**
	 * Saves a new or existing department
	 * @param objectID
	 * @param name
	 * @param add
	 * @param addAsSubdepartment
	 */
	private static String save(Integer objectID, String name, boolean add, boolean addAsSubdepartment) {
		TDepartmentBean departmentBean = null;
		boolean reloadTree = true; 
		if (!add) {
			//edit existing
			departmentBean = departmentDAO.loadByPrimaryKey(objectID);
		}
		if (departmentBean==null) {
			//either add or the node was deleted by other user/session in the meantime
			departmentBean = new TDepartmentBean();
		}
		if (add && addAsSubdepartment) {
			departmentBean.setParent(objectID);
		}
		reloadTree = EqualUtils.notEqual(name, departmentBean.getLabel());
		departmentBean.setLabel(name);
		Integer departmentID = save(departmentBean);
		PersonInDepartmentTokens personInGroupTokens = new PersonInDepartmentTokens(departmentID);
		return GroupJSON.getGroupSaveDetailJSON(encodeNode(personInGroupTokens), reloadTree);
	}
	
	/**
	 * Copies a department with the entire subtree
	 * @param nodeFrom
	 * @param nodeTo
	 * @return
	 * 
	 */
	public static String copy(String nodeFrom, String nodeTo){
		PersonInDepartmentTokens departmentFromToken = decodeNode(nodeFrom);
		PersonInDepartmentTokens departmentToToken = decodeNode(nodeTo);
		Integer departmentFrom = departmentFromToken.getDepartmentID();
		Integer personFrom = departmentFromToken.getPersonID();
		Integer departmentTo = departmentToToken.getDepartmentID();
		if (EqualUtils.equal(departmentFrom, departmentTo)) {
			return JSONUtility.encodeJSONSuccessAndNode(nodeFrom);
		}
		if (personFrom!=null && departmentTo!=null) {
			TPersonBean personBean = LookupContainer.getPersonBean(personFrom);
			if (personBean!=null) {
				personBean.setDepartmentID(departmentTo);
				PersonBL.saveSimple(personBean);
			}
		} else {
			if (departmentFrom!=null && departmentTo!=null) {
				TDepartmentBean departmentBean = DepartmentBL.loadByPrimaryKey(departmentFrom);
				if (departmentBean!=null) {
					departmentBean.setParent(departmentTo);
					save(departmentBean);
				}
			}
		}
		return JSONUtility.encodeJSONSuccessAndNode(nodeFrom);
	}
	
	/**
	 * Clears the department's parent
	 * @param node
	 * @return
	 */
	public static String clearParent(String node) {
		PersonInDepartmentTokens departmentToken = decodeNode(node);
		Integer departmentID = departmentToken.getDepartmentID();
		if (departmentID!=null) {
			TDepartmentBean departmentBean = DepartmentBL.loadByPrimaryKey(departmentID);
			Integer parentDepartment = departmentBean.getParent();
			if (parentDepartment!=null) {
				departmentBean.setParent(null);
				save(departmentBean);
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}
	/**
	 * Deletes a department node
	 * @param node
	 * @return
	 */
	public static String delete(String node) {
		PersonInDepartmentTokens personInDepartmentToken = decodeNode(node);
		Integer departmentID = personInDepartmentToken.getDepartmentID();
		if (departmentID!=null) {
			if (departmentDAO.hasDependentData(departmentID)){
				return JSONUtility.encodeJSONFailure(null, JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
			} else {
				delete(departmentID);
				return JSONUtility.encodeJSONSuccess();
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}
	
	/**
	 * Creates the JSON string for replacement triggers 
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	public static String prepareReplacement(String node, String errorMessage, Locale locale) {
		PersonInDepartmentTokens personInDepartmentToken = decodeNode(node);
		Integer departmentID = personInDepartmentToken.getDepartmentID();
		String departmentName = "";
		TDepartmentBean departmentBean = loadByPrimaryKey(departmentID);
		if (departmentBean!=null) {
			departmentName = departmentBean.getLabel();
		}
        String departmentTree = JSONUtility.getTreeHierarchyJSON(DepartmentBL.getDepartmentNodesEager(departmentID, null, false), false, false);
		String localizedLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.department.lbl.department", locale);
        return JSONUtility.createReplacementTreeJSON(true, departmentName, localizedLabel, localizedLabel, departmentTree, errorMessage, locale);
	}
	
	/**
	 * Replace and delete the department
	 * @param node
	 * @param newDepartmentID
	 */
	public static void replaceAndDeleteDepartment(String node, Integer newDepartmentID) {
		PersonInDepartmentTokens personInDepartmentToken = decodeNode(node);
		Integer oldDepartmentID = personInDepartmentToken.getDepartmentID();
		if (newDepartmentID!=null) {
			departmentDAO.replace(oldDepartmentID, newDepartmentID);
			delete(oldDepartmentID);
		}
	}
	
	/**
	 * Gets the assignment detail json for a node
	 * @param node
	 * @param parentsToReload
	 * @param locale	
	 * @return
	 */
	public static String getAssignmentJSON(String node, List<String> parentsToReload, Locale locale) {
		PersonInDepartmentTokens personInDepartmentToken = decodeNode(node);
		Integer departmentID = personInDepartmentToken.getDepartmentID();
		String assignmentInfo = null;
		List<TPersonBean> assignedPersons = PersonBL.loadPersonsForDepartment(departmentID);
		Set<Integer> assignedPersonIDsSet = GeneralUtils.createIntegerSetFromBeanList(assignedPersons);
		List<TPersonBean> unsassignedPersons = PersonBL.loadPersons();
		for (Iterator<TPersonBean> iterator = unsassignedPersons.iterator(); iterator.hasNext();) {
			TPersonBean personBean = iterator.next();
			if (assignedPersonIDsSet.contains(personBean.getObjectID())) {
				iterator.remove();
			}
		}
		String departmentLabel = loadByPrimaryKey(departmentID).getLabel();
		assignmentInfo = LocalizeUtil.getParametrizedString("admin.user.department.lbl.personAssignment",
				new Object[] {departmentLabel}, locale);
		String reloadFromNode = encodeNode(new PersonInDepartmentTokens(departmentID));
		return GroupJSON.encodePersonInGroupDetail(assignedPersons, unsassignedPersons,
				assignmentInfo, parentsToReload, reloadFromNode, reloadFromNode, DepartmentBL.getDepartmentMap(), locale);	
	}
	
	/**
	 * Assign entries
	 * @param node
	 * @param assign
	 */
	public static List<String> assign(String node, String assign) {
		PersonInDepartmentTokens personInDepartmentToken = decodeNode(node);
		Integer departmentID = personInDepartmentToken.getDepartmentID();
		List<Integer> assignIDs = null;
		if (assign!=null) {
			assignIDs = GeneralUtils.createIntegerListFromStringArr(assign.split(","));
		}
		return addPersonsToDepartmemt(departmentID, assignIDs);
	}
	
	/**
	 * Unassign entries
	 * @param node
	 * @param unassign
	 */
	public static void unassign(String node, String unassign) {
		PersonInDepartmentTokens personInDepartmentToken = decodeNode(node);
		Integer departmentID = personInDepartmentToken.getDepartmentID();
		List<Integer> unassignIDs = null;
		if (unassign==null) {
			unassignIDs = new LinkedList<Integer>();
			Integer personID = personInDepartmentToken.getPersonID();
			if (personID!=null) {
				//unassingn from tree
				unassignIDs.add(personID);
			}
		} else {
			unassignIDs = GeneralUtils.createIntegerListFromStringArr(unassign.split(","));
		}
		removePersonsFromDepartmemt(departmentID, unassignIDs);
	}
	
	/**
	 * Assign persons to department
	 * @param departmentID
	 * @param personIDs
	 */
	private static List<String> addPersonsToDepartmemt(Integer departmentID, List<Integer> personIDs) {
		List<String> departmentsOfRemovedNodes = new LinkedList<String>();
		if (departmentID!=null && personIDs!=null && !personIDs.isEmpty()) {
			List<TPersonBean> personBeans = PersonBL.loadByKeys(personIDs);
			for (TPersonBean personBean : personBeans) {
				Integer oldDapartmentID = personBean.getDepartmentID();
				if (oldDapartmentID!=null) {
					departmentsOfRemovedNodes.add(encodeNode(new PersonInDepartmentTokens(oldDapartmentID)));
				}
				personBean.setDepartmentID(departmentID);
				PersonBL.saveSimple(personBean);
			}
		}
		return departmentsOfRemovedNodes;
	}
	
	/**
	 * Remove persons from roles in project
	 * @param groupID
	 * @param personIDs
	 */
	private static void removePersonsFromDepartmemt(Integer groupID, List<Integer> personIDs) {
		if (groupID!=null && personIDs!=null && !personIDs.isEmpty()) {
			List<TPersonBean> personBeans = PersonBL.loadByKeys(personIDs);
			for (TPersonBean personBean : personBeans) {
				personBean.setDepartmentID(null);
				PersonBL.saveSimple(personBean);
			}
		}
	}

	public static Map<Integer, String> getDepartmentMap() {
		List<TDepartmentBean> departmentBeans = getAllDepartments();
		return GeneralUtils.createLabelMapFromList((List)departmentBeans);
	}
}
