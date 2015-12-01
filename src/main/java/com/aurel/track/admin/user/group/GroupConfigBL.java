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

package com.aurel.track.admin.user.group;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.PersonDAO;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.item.history.FieldChangeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LabelValueBean;
import com.aurel.track.util.PropertiesHelper;

public class GroupConfigBL {
	private static PersonDAO personDAO = DAOFactory.getFactory().getPersonDAO();
	
	
	private static String LINK_CHAR = "_";
	public static String USER_ICON_CLASS = "user-ticon";
	private static String GROUP_ICON_CLASS = "users-ticon";
	
	public static interface GROUP_FLAGS {
		static String ORIGINATOR = "originator";
		static String MANAGER = "manager";
		static String RESPONSIBLE = "responsible";
		static String JOIN_NEW_USER_TO_THIS_GROUP = "joinNewUserToThisGroup";	
	}
	
	/**
	 * Encode a node
	 * @param personInGroupTokens
	 * @return
	 */
	static String encodeNode(PersonInGroupTokens personInGroupTokens){
		StringBuffer stringBuffer = new StringBuffer();
		Integer groupID = personInGroupTokens.getGroupID();
		Integer personID = personInGroupTokens.getPersonID();
		if (groupID!=null) {
			stringBuffer.append(groupID);
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
	public static PersonInGroupTokens decodeNode(String id) {
		PersonInGroupTokens personInGroupTokens = new PersonInGroupTokens();
		if (id!=null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {	
				personInGroupTokens.setGroupID(Integer.valueOf(tokens[0]));
				if (tokens.length>1) {
					if (tokens[1]!=null && !"".equals(tokens[1])) {
						personInGroupTokens.setPersonID(Integer.valueOf(tokens[1]));
					}
				}
			}
		}
		return personInGroupTokens;
	}

	/**
	 * Gets the assigned nodes for a project
	 * @param node
	 * @return
	 */
	public static List<TreeNodeBaseTO> getChildren(
			String node) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		List<TreeNodeBaseTO> assignedNodes = new LinkedList<TreeNodeBaseTO>();
		Integer groupID = personInGroupTokens.getGroupID();
		List<TPersonBean> personBeans = null;
		String iconCls = null;
		boolean leaf = true;
		if (groupID==null) {
			personBeans = PersonBL.loadGroups();
			Map<Integer, Integer> numberOfPersonsInGroupsMap = GroupMemberBL.loadNumberOfPersonsInAllGroups();
			for (TPersonBean groupBean : personBeans) {
				Integer groupKey = groupBean.getObjectID();
				Integer numberOfPersons = numberOfPersonsInGroupsMap.get(groupKey);
				if (numberOfPersons!=null) {
					groupBean.setLoginName(groupBean.getLoginName()+" ("+numberOfPersons + ")");
				}
			}
			iconCls = GROUP_ICON_CLASS;
			leaf = false;
		} else {
			personBeans = PersonBL.loadPersonsForGroup(groupID);
			iconCls = USER_ICON_CLASS;
		}
		for (TPersonBean personBean : personBeans) {
			if (!leaf) {
				//group entries
				personInGroupTokens = new PersonInGroupTokens(personBean.getObjectID());
			} else {
				//account (roleID==null) or person (roleID!=null) entries
				personInGroupTokens = new PersonInGroupTokens(groupID, personBean.getObjectID());
			}
			TreeNodeBaseTO treeNodeTO = new TreeNodeBaseTO(
					encodeNode(personInGroupTokens), personBean.getLabel(), iconCls, leaf);
			assignedNodes.add(treeNodeTO);
		}
		return assignedNodes;
	}
	
	/**
	 * Gets the group with actualized label
	 * @param node
	 * @return
	 */
	static List<LabelValueBean> getGroups(String node) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		Integer groupID = personInGroupTokens.getGroupID();
		List<LabelValueBean> groupLabelBeans = new LinkedList<LabelValueBean>();
		if (groupID!=null) {
			TPersonBean groupBean = LookupContainer.getPersonBean(groupID);//PersonBL.loadByPrimaryKey(groupID);
			String groupLabel = null;
			if (groupBean!=null) {
				groupLabel = groupBean.getLoginName();
			}
			Integer numberOfPersons= GroupMemberBL.loadNumberOfPersonsInGroup(groupID);
			if (numberOfPersons!=null && numberOfPersons.intValue()!=0) {
				groupLabel = groupLabel + " ("+numberOfPersons + ")";
			}
			groupLabelBeans.add(new LabelValueBean(groupLabel, groupBean.getObjectID().toString()));
		}
		return groupLabelBeans;
	}
	
	/**
	 * Get the detail JSON for edit/add 
	 * @param node
	 * @param add
	 * @return
	 */
	static String getEditDetailJSON(String node, boolean add) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		Integer groupID = personInGroupTokens.getGroupID();
		String loginName = null;
		boolean originator = false;
		boolean manager = false;
		boolean responsible = false;
		boolean joinNewUserToThisGroup = false;
		if (groupID!=null && !add) {
			TPersonBean personBean = LookupContainer.getPersonBean(groupID);// PersonBL.loadByPrimaryKey(groupID);
			if (personBean!=null) {
				loginName = personBean.getLoginName();
				originator = PropertiesHelper.getBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.ORIGINATOR);
				manager = PropertiesHelper.getBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.MANAGER);
				responsible = PropertiesHelper.getBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.RESPONSIBLE);
				joinNewUserToThisGroup = PropertiesHelper.getBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.JOIN_NEW_USER_TO_THIS_GROUP);
			}
		}
		return GroupJSON.getGroupEditDetailJSON(loginName, originator, manager, responsible, joinNewUserToThisGroup);
	}
	
	/**
	 * Get the JSON for save
	 * @param node
	 * @param add
	 * @param name
	 * @param originator
	 * @param manager
	 * @param responsible
	 * @param locale
	 * @return
	 */
	static String getSaveDetailJSON(String node, boolean add, String name,
			boolean originator, boolean manager, boolean responsible, boolean joinNewUserToThisGroup, 
			Locale locale) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		Integer groupID = null;
		if (!add) {
			groupID = personInGroupTokens.getGroupID();
		}
		String errorMessage = isValidLabel(groupID, name, locale);
		String jsonResult;
		if (errorMessage==null) {
			jsonResult = save(groupID, name, originator, manager, responsible, joinNewUserToThisGroup, add);
		} else {
			jsonResult = JSONUtility.encodeJSONFailure(errorMessage);
		}
		return jsonResult;
	}
	
	/**
	 * Whether the label is valid (typically not duplicated) 
	 * @param objectID
	 * @param loginName
     * @param locale
	 * @return
	 */
	public static String isValidLabel(Integer objectID, String loginName, Locale locale) {
		if(loginName==null||loginName.trim().length()==0){
			return LocalizeUtil.getParametrizedString("common.err.required", 
					new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
							"admin.user.group.lbl.group", locale)} , locale);
		}
		TPersonBean personBean = PersonBL.loadGroupByName(loginName);
		if (personBean==null) {
			return null;
		} else {
			if (objectID==null || (objectID!=null && !personBean.getObjectID().equals(objectID))) {
				return LocalizeUtil.getParametrizedString("common.err.unique", 
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.user.group.lbl.group", locale)} , locale);
			}
		}
		return null;
	}
	
	/**
	 * Saves a group
	 * @param objectID
	 * @param name
	 * @param originator
	 * @param manager
	 * @param responsible
	 * @param add
	 * @return
	 */
	private static String save(Integer objectID, String name,
			boolean originator, boolean manager, boolean responsible, boolean joinNewUserToThisGroup, 
			boolean add) {
		TPersonBean personBean = null;
		boolean reloadTree = true;
		if (!add) {
			//edit existing
			personBean = PersonBL.loadByPrimaryKey(objectID);
		}
		if (personBean==null) {
			//either add or the node was deleted by other user/session in the meantime
			personBean = new TPersonBean();
		}
		reloadTree = EqualUtils.notEqual(name, personBean.getLoginName());
		personBean.setLoginName(name);
		personBean.setIsGroupBool(true);
		personBean.setPreferences(PropertiesHelper.setBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.ORIGINATOR, originator));
		personBean.setPreferences(PropertiesHelper.setBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.MANAGER, manager));
		personBean.setPreferences(PropertiesHelper.setBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.RESPONSIBLE, responsible));
		personBean.setPreferences(PropertiesHelper.setBooleanProperty(personBean.getPreferences(), GROUP_FLAGS.JOIN_NEW_USER_TO_THIS_GROUP, joinNewUserToThisGroup));
		Integer groupID = PersonBL.save(personBean);
		PersonInGroupTokens personInGroupTokens = new PersonInGroupTokens(groupID);
		return GroupJSON.getGroupSaveDetailJSON(encodeNode(personInGroupTokens), reloadTree);
	}
	
	
	
	/**
	 * Deletes a group
	 * @param node
	 * @return
	 */
	static String delete(List<Integer> groupIDs) {
		if (groupIDs!=null && !groupIDs.isEmpty()) {
			if (hasDependentGroupData(groupIDs)) {
				return JSONUtility.encodeJSONFailure(null, JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
			} else {
				for (Integer groupID : groupIDs) {
					deleteGroup(groupID); 
				}
				LookupContainer.resetPersonMap();
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}
	
	
	/**
	 * Whether a person has dependent data
	 * @param oldPersonID
	 * @return
	 */
	private static boolean hasDependentGroupData(List<Integer> oldPersonIDs) {
		return personDAO.hasDependentGroupData(oldPersonIDs) || 
				AttributeValueBL.isSystemOptionAttribute(oldPersonIDs, SystemFields.INTEGER_PERSON) || // .hasDependencyInUserPicker(oldPersonIDs) ||
				FieldChangeBL.isSystemOptionInHistory(oldPersonIDs, SystemFields.INTEGER_PERSON, true) ||
				FieldChangeBL.isSystemOptionInHistory(oldPersonIDs, SystemFields.INTEGER_PERSON, false);
				/*personDAO.hasDependencyInHistory(oldPersonIDs, true) ||
				personDAO.hasDependencyInHistory(oldPersonIDs, false);*/
	}
	
	/**
	 * Creates the JSON string for replacement triggers 
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String prepareReplacement(List<Integer> selectedGroupIDsList, String errorMessage, Locale locale) {
		String replacementEntity = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.group.lbl.group", locale);
		String groupName = "";
		List<TPersonBean> replacementPersonList = prepareReplacementGroups(selectedGroupIDsList);
		if (selectedGroupIDsList!=null && !selectedGroupIDsList.isEmpty()) {
			if (selectedGroupIDsList.size()==1) {
				//delete only one person
				TPersonBean groupBean = PersonBL.loadByPrimaryKey(selectedGroupIDsList.get(0));
				if (groupBean!=null) {
					groupName = groupBean.getLoginName();
				}
				return JSONUtility.createReplacementListJSON(true, groupName, replacementEntity, replacementEntity, (List)replacementPersonList, errorMessage, locale);		
			} else {
				//delete more than one person
				int totalNumber = selectedGroupIDsList.size();
				String entities = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.user.group.lbl.groups", locale);
				return JSONUtility.createReplacementListJSON(true, totalNumber, entities, replacementEntity, (List)replacementPersonList, errorMessage, locale);
			}
		}
		return JSONUtility.createReplacementListJSON(true, groupName, replacementEntity, replacementEntity, (List)replacementPersonList, errorMessage, locale);	
	}
	
	/**
	 * Prepares the replacement triggers
	 * @param personID
	 * @return
	 */
	static List<TPersonBean> prepareReplacementGroups(List<Integer> groupIDs) {
		List<TPersonBean> replacementGroupList = PersonBL.loadGroups();
		if (replacementGroupList!=null) {
			Iterator<TPersonBean> iterator = replacementGroupList.iterator();
			while (iterator.hasNext()) {
				TPersonBean personBean = iterator.next();
				if (groupIDs.contains(personBean.getObjectID())) {
					iterator.remove();
				}
			}
		}
		return replacementGroupList;
	}
	
	/**
	 * Replace and delete the group
	 * @param selectedGroupIDsList
	 * @param newGroupID
	 */
	static void replaceAndDeleteGroup(List<Integer> selectedGroupIDsList, Integer newGroupID) {
		if (newGroupID!=null && selectedGroupIDsList!=null && !selectedGroupIDsList.isEmpty()) {
			for (Integer oldGroupID : selectedGroupIDsList) {
				personDAO.replaceGroup(oldGroupID, newGroupID);
				personDAO.replaceUserPicker(oldGroupID, newGroupID);
				personDAO.replaceHistoryPerson(oldGroupID, newGroupID, true);
				personDAO.replaceHistoryPerson(oldGroupID, newGroupID, false);
				deleteGroup(oldGroupID);
			}
			LookupContainer.resetPersonMap();
		}
	}
	
	private static void deleteGroup(Integer groupID) {
		personDAO.deleteGroup(groupID);
		//delete from lucene index
		NotLocalizedListIndexer.getInstance().deleteByKeyAndType(
				groupID, LuceneUtil.LOOKUPENTITYTYPES.PERSONNAME);
		//cache and possible lucene update in other nodes
		ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_PERSON, groupID, ClusterMarkChangesBL.getChangeTypeByDelete());
		
	}
	
	/** Gets the assignment detail json for a node
	 * @param node
	 * @param locale	
	 * @return
	 */
	static String getAssignmentJSON(String node, Locale locale) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		Integer groupID = personInGroupTokens.getGroupID();
		String assignmentInfo = null;
		List<TPersonBean> assignedPersons = PersonBL.loadPersonsForGroup(groupID);
		Set<Integer> assignedPersonIDsSet = GeneralUtils.createIntegerSetFromBeanList(assignedPersons);
		List<TPersonBean> unsassignedPersons = PersonBL.loadPersons();
		for (Iterator<TPersonBean> iterator = unsassignedPersons.iterator(); iterator.hasNext();) {
			TPersonBean personBean = iterator.next();
			if (assignedPersonIDsSet.contains(personBean.getObjectID())) {
				iterator.remove();
			}
		}
		String groupLabel = "";
		TPersonBean groupBean = LookupContainer.getPersonBean(groupID);
		if (groupBean!=null) {
			groupLabel = groupBean.getLoginName();
		}
		assignmentInfo = LocalizeUtil.getParametrizedString("admin.user.group.lbl.personAssignment",
				new Object[] {groupLabel}, locale);
		String reloadFromNode = encodeNode(new PersonInGroupTokens(groupID));
		return GroupJSON.encodePersonInGroupDetail(assignedPersons, unsassignedPersons,
				assignmentInfo, null, reloadFromNode, reloadFromNode, DepartmentBL.getDepartmentMap(), locale);	
	}
	
	/**
	 * The node to reload is always a group even if the selected node is a person in group 
	 * (a leaf in the tree), so the personID should be removed from the node
	 * @param node
	 * @return
	 */
	static String getNodesToReload(String node) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		Integer groupID = personInGroupTokens.getGroupID();
		String reloadFromNode = encodeNode(new PersonInGroupTokens(groupID));
		return GroupJSON.encodeNodes(reloadFromNode);
	}
	
	/**
	 * Assign entries
	 * @param node
	 * @param assign
	 */
	static void assign(String node, String assign) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		Integer groupID = personInGroupTokens.getGroupID();
		assign(groupID,assign);
	}
	public static void assign(Integer groupID,String assign){
		List<Integer> assignIDs = null;
		if (assign!=null) {
			assignIDs = GeneralUtils.createIntegerListFromStringArr(assign.split(","));
		}
		addPersonsToGroup(groupID, assignIDs);
	}
	
	/**
	 * Unassign entries
	 * @param node
	 * @param unassign
	 */
	static void unassign(String node, String unassign) {
		PersonInGroupTokens personInGroupTokens = decodeNode(node);
		Integer groupID = personInGroupTokens.getGroupID();
		if (unassign==null) {
			Integer personID = personInGroupTokens.getPersonID();
			if (personID!=null) {
				unassign=personID.toString();
			}
		}
		unassign(groupID, unassign);
	}
	public static void unassign(Integer groupID,String unassign){
		List<Integer> unassignIDs = null;
		if (unassign!=null) {
			unassignIDs = GeneralUtils.createIntegerListFromStringArr(unassign.split(","));
		}
		removePersonsFromGroup(groupID, unassignIDs);
	}

	/**
	 * Assign persons to role in project
	 * @param groupID
	 * @param personIDs
	 */
	public static void addPersonsToGroup(Integer groupID, List<Integer> personIDs) {
		if (groupID!=null && personIDs!=null && !personIDs.isEmpty())
			for (Integer personID : personIDs) {
				GroupMemberBL.saveGroupMember(groupID, personID);
			}
	}
		
	/**
	 * Remove persons from roles in project
	 * @param groupID
	 * @param personIDs
	 */
	public static void removePersonsFromGroup(Integer groupID, List<Integer> personIDs) {
		if (groupID!=null && personIDs!=null && !personIDs.isEmpty()) {
			for (Integer personID : personIDs) {
				GroupMemberBL.delete(groupID, personID);
			}
		}
	}
}
