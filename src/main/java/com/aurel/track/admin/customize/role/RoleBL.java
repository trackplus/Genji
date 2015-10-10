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


package com.aurel.track.admin.customize.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.beans.TRoleListTypeBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.RoleDAO;
import com.aurel.track.dao.RoleListTypeDAO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBooleanBean;

/**
 * Business logic and helper for RoleAction
 * @author Tamas Ruff
 *
 */
public class RoleBL {
	
	private static RoleDAO roleDAO = DAOFactory.getFactory().getRoleDAO();
	private static RoleListTypeDAO roleListTypeDAO = DAOFactory.getFactory().getRoleListTypeDAO();
	
	private static int MAX_LABEL_LENGTH = 25;
	private static Integer[] flagLocations = {
		AccessFlagIndexes.READANYTASK,
		AccessFlagIndexes.MODIFYANYTASK,
		AccessFlagIndexes.CREATETASK,
		AccessFlagIndexes.CLOSEANYTASK,
		AccessFlagIndexes.CLOSETASKIFRESPONSIBLE,
		AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR,
		AccessFlagIndexes.MANAGER,
		AccessFlagIndexes.RESPONSIBLE,
		AccessFlagIndexes.CONSULTANT,
		AccessFlagIndexes.INFORMANT,
		AccessFlagIndexes.PROJECTADMIN
	};

	static List<Integer> fullAccessFlags = Arrays.asList(
			AccessFlagIndexes.READANYTASK,
			AccessFlagIndexes.MODIFYANYTASK,
			AccessFlagIndexes.CREATETASK,
			AccessFlagIndexes.CLOSEANYTASK,
			AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR,
			AccessFlagIndexes.CLOSETASKIFRESPONSIBLE,
			AccessFlagIndexes.PROJECTADMIN);
		
	static List<Integer> raciRoles = Arrays.asList(
			AccessFlagIndexes.MANAGER,
			AccessFlagIndexes.RESPONSIBLE,
			AccessFlagIndexes.INFORMANT,
			AccessFlagIndexes.CONSULTANT);
	

	/**
	 * Loads a role by key
	 * @param objectID
	 * @return
	 */
	private static TRoleBean loadRoleByKey(Integer objectID) {
		return roleDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Loads a role by key
	 * @param objectID
	 * @return
	 */
	public static TRoleBean loadRoleByKey(Integer objectID, Locale locale) {
		TRoleBean roleBean = roleDAO.loadByPrimaryKey(objectID);
		roleBean.setLabel(LocalizeUtil.localizeDropDownEntry(roleBean, locale));
		return roleBean;
	}
	
	/**
	 * Load the visible roles localized
	 * @return
	 */
	public static List<TRoleBean> loadAll() {
		return roleDAO.loadAll();
	}
	public static Map<Integer,TRoleBean> loadAllAsMap() {
		List<TRoleBean> roleBeanList= roleDAO.loadAll();
		Map<Integer,TRoleBean> map=new HashMap<Integer, TRoleBean>();
		if(roleBeanList!=null && !roleBeanList.isEmpty()){
			for(TRoleBean roleBean:roleBeanList){
				map.put(roleBean.getObjectID(),roleBean);
			}
		}
		return map;
	}

	/**
	 * Load the visible roles localized
	 * @return
	 */
	public static List<TRoleBean> loadVisible(Locale locale) {
		return LocalizeUtil.localizeDropDownList(roleDAO.loadVisible(), locale);
	}
	
	/**
	 * Load the visible roles localized
	 * @return
	 */
	public static List<TRoleBean> loadVisible() {
		return roleDAO.loadVisible();
	}
	
	/**
	 * Load the not visible roles
	 * @return
	 */
	public static List<TRoleBean> loadNotVisible() {
		return roleDAO.loadNotVisible();
	}
	
	/**
	 * Load the roles which have explicit issueType(s) assignments
	 * @param roleIDs
	 * @return
	 */
	public static List<TRoleBean> loadWithExplicitIssueType(Object[] roleIDs) {
		return roleDAO.loadWithExplicitIssueType(roleIDs);
	}
	
	/**
	 * Saves a roleBean
	 * @param roleBean
	 * @return
	 */
	public static Integer save(TRoleBean roleBean) {
		return roleDAO.save(roleBean);
	}
	
	/**
	 * Loads the role list types by roles and listType 
	 * @param roleIDs
	 * @param listType if listType is null get all by roles
	 * @return
	 */
	public static List<TRoleListTypeBean> loadByRolesAndListType(Object[] roleIDs, Integer listType) {
		return roleListTypeDAO.loadByRolesAndListType(roleIDs, listType);
	}
	
	/**
	 * Saves a role
	 * @param label
	 * @param extendedAccessKey
	 * @param roleID
	 * @param copy
	 * @param locale
	 * @return
	 */
	static String saveRole(String label, String extendedAccessKey, Integer roleID, boolean copy, Locale locale) {
		String errorMessage = isValidLabel(roleID, label, locale);
		if (errorMessage==null) {
			TRoleBean roleBean;
			if (roleID==null || copy) {
				roleBean=new TRoleBean();
			}else{
				roleBean=RoleBL.loadRoleByKey(roleID);
			}
			roleBean.setLabel(label);
			roleBean.setExtendedaccesskey(extendedAccessKey);
			Integer newRoleID = save(roleBean);
			//default locale
			LocalizeBL.saveLocalizedResource(
					new TRoleBean().getKeyPrefix(), newRoleID, label, null);
			//actual locale
			LocalizeBL.saveLocalizedResource(
					new TRoleBean().getKeyPrefix(), newRoleID, label, locale);
			if (copy && roleID!=null) {
				List<TRoleFieldBean> fieldsForRole = FieldsRestrictionsToRoleBL.getByRole(roleID);
				if (fieldsForRole!=null) {
					for (TRoleFieldBean roleFieldBean : fieldsForRole) {
						TRoleFieldBean roleFieldBeanCopy = new TRoleFieldBean();
						roleFieldBeanCopy.setAccessRight(roleFieldBean.getAccessRight());
						roleFieldBeanCopy.setFieldKey(roleFieldBean.getFieldKey());
						roleFieldBeanCopy.setRoleKey(newRoleID);
						FieldsRestrictionsToRoleBL.saveBean(roleFieldBean);
					}
				}
				Set<Integer> issueTypeIDs = getIssueTypeIDSetForRole(roleID);
				if (issueTypeIDs!=null) {
					for (Integer issueTypeID : issueTypeIDs) {
						TRoleListTypeBean roleListTypeBean = new TRoleListTypeBean();
						roleListTypeBean.setListType(issueTypeID);
						roleListTypeBean.setRole(newRoleID);
						roleListTypeDAO.save(roleListTypeBean);
					}
				}
			}
			return JSONUtility.encodeJSONSuccess();
		} else {
			return JSONUtility.encodeJSONFailure(errorMessage);
		}
	}
	
	/**
	 * Whether the label is valid (typically not duplicated) 
	 * @param objectID
	 * @param roleName
     * @param locale
	 * @return
	 */
	private static String isValidLabel(Integer objectID, String roleName, Locale locale) {
		TRoleBean roleBean = roleDAO.loadByName(roleName);
		if (roleBean==null) {
			return null;
		} else {
			if (objectID==null || (objectID!=null && !roleBean.getObjectID().equals(objectID))) {
				return LocalizeUtil.getParametrizedString("common.err.unique",
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.customize.role.lbl.role", locale)} , locale);
			}
		}
		return null;
	}
	
	/**
	 * Deletes a role
	 * @param objectID
	 */
	public static void deleteRole(Integer objectID) {
		roleDAO.delete(objectID);
		LocalizeBL.removeLocalizedResources(new TRoleBean().getKeyPrefix(), objectID);
	}
	
	/**
	 * Loads a new/existing/copied role for edit
	 * @param roleID
	 * @param copy
	 * @param locale
	 * @return
	 */
	static RoleTO loadRoleTO(Integer roleID, boolean copy, Locale locale) {
		RoleTO roleTO = new RoleTO();
		Boolean[] unfoldedFlags = null;
		if (roleID==null) {
			//new
			unfoldedFlags=getUnfoldedFlags(null);
		} else {
			TRoleBean roleBean=loadRoleByKey(roleID, locale);
			unfoldedFlags=getUnfoldedFlags(roleBean.getExtendedaccesskey());
			String label = roleBean.getLabel();
			if (copy) {
				//copy
				String[] args={label};
				label = LocalizeUtil.getParametrizedString("common.copy", args, locale);
				if (label.length()>MAX_LABEL_LENGTH) {
					label = label.substring(0, MAX_LABEL_LENGTH);
				}
				roleTO.setLabel(label);
				//roleBean=RoleBL.copyRoleTO(roleID,locale);
			} else {
				//edit
				roleTO.setLabel(label);
				roleTO.setObjectID(roleBean.getObjectID());
			}
		}
		Map<Integer,IntegerStringBooleanBean> allFlags=createUnfoldedFlagsMap(unfoldedFlags,locale);
		List<IntegerStringBooleanBean> unfoldedFullAccessFlags=wrapCategoryFlags(fullAccessFlags,allFlags);
		List<IntegerStringBooleanBean> unfoldedRaciRoles=wrapCategoryFlags(raciRoles,allFlags);
		roleTO.setFullAccessFlags(unfoldedFullAccessFlags);
		roleTO.setRaciRoles(unfoldedRaciRoles);
		return roleTO;
	}
	
	
	static Map<Integer, IntegerStringBooleanBean> createUnfoldedFlagsMap(Boolean[] unfoldedFlags, Locale locale){
		Map<Integer,IntegerStringBooleanBean> result=new HashMap<Integer, IntegerStringBooleanBean>();
		Map<Integer,String> flagNamesMap=RoleBL.getFlagNameMap(locale);
		boolean  checked=false;
		for (int k=0;k<flagLocations.length;k++){
			Integer flag=flagLocations[k];
			if(unfoldedFlags!=null && unfoldedFlags[flag]!=null && unfoldedFlags[flag].booleanValue()){
				checked=true;
			}else{
				checked=false;
			}
			IntegerStringBooleanBean isb=new IntegerStringBooleanBean();
			isb.setLabel(flagNamesMap.get(flag));
			isb.setValue(flag);
			isb.setSelected(checked);
			result.put(flag,isb);
		}
		return result;
	}

	static List<IntegerStringBooleanBean> wrapCategoryFlags(List<Integer> categoryFlags,Map<Integer,IntegerStringBooleanBean> allFlags){
		List<IntegerStringBooleanBean> categoryUnfoldedFlags=new ArrayList<IntegerStringBooleanBean>();
		for(int i=0;i<categoryFlags.size();i++){
			Integer flag=categoryFlags.get(i);
			categoryUnfoldedFlags.add(allFlags.get(flag));
		}
		return categoryUnfoldedFlags;
	}
	
	public static void assignRoleListTypes(Integer roleID, Integer[] issueTypeIDs) {
		if (roleID!=null && issueTypeIDs!=null && issueTypeIDs.length>0)
			for (int i = 0; i < issueTypeIDs.length; i++) {
				TRoleListTypeBean roleListTypeBean = new TRoleListTypeBean();
				roleListTypeBean.setRole(roleID);
				roleListTypeBean.setListType(issueTypeIDs[i]);
				roleListTypeDAO.save(roleListTypeBean);
			}
	}
	
	public static void unassignRoleListTypes(Integer roleID, Integer[] issueTypeIDs) {
		if (roleID!=null && issueTypeIDs!=null && issueTypeIDs.length>0)
			for (int i = 0; i < issueTypeIDs.length; i++) {
				roleListTypeDAO.delete(roleID, issueTypeIDs[i]);
			}
	}
	
	
	/**
	 * Get the unfolded flags array of the role.
	 * @param roleBean
	 * @return
	 */
	static Boolean[] getUnfoldedFlags(String extendedAccessKey) {
		Boolean[] unfoldedFlags = new Boolean[AccessBeans.NUMBER_OF_ACCESS_FLAGS];
		StringBuilder accessKeys;
		if (extendedAccessKey==null) {
			accessKeys = new StringBuilder();
			for (int i=0; i<unfoldedFlags.length; i++) {
				accessKeys.append(AccessBeans.OFFVALUE);
			}
		} else {
			accessKeys = new StringBuilder(extendedAccessKey.substring(0, unfoldedFlags.length));
		}
		int noCheckBoxFlag = 0;
		for (int i=0; i<accessKeys.length(); i++) {
			char flagChar = accessKeys.charAt(i);
			Boolean flagBool;
			if (flagChar==AccessBeans.ONVALUE) {
				flagBool = Boolean.TRUE;
			} else {
				flagBool = Boolean.FALSE;
			}
			unfoldedFlags[noCheckBoxFlag++] = flagBool;
		}
		return unfoldedFlags;
	}
	
	/**
	 * Loads a map with:
	 * 	- key: roleID
	 * 	- value: list of issueTypeIDs
	 * @return
	 */
	static List<TListTypeBean> getIssueTypesForRole(Integer roleID,
			boolean assigned, List<TListTypeBean> issueTypesList) {
		List<TListTypeBean> issueTypesForRole = new LinkedList<TListTypeBean>(); 
		Set<Integer> assignedIssueTypeIDsSet = getIssueTypeIDSetForRole(roleID);
		for (TListTypeBean listTypeBean : issueTypesList) {
			if ((assignedIssueTypeIDsSet.contains(listTypeBean.getObjectID()) && assigned) || 
					(!assignedIssueTypeIDsSet.contains(listTypeBean.getObjectID()) && !assigned)) {
				issueTypesForRole.add(listTypeBean);
			}
		}
		return issueTypesForRole; 
	}
	
	private static Set<Integer> getIssueTypeIDSetForRole(Integer roleID) {
		Set<Integer> issueTypeIDsSet = new HashSet<Integer>();
		List<TRoleListTypeBean> roleIssueTypesList = roleListTypeDAO.loadByRole(roleID);
		if (roleIssueTypesList!=null) {
			for (TRoleListTypeBean roleListTypeBean : roleIssueTypesList) {
				issueTypeIDsSet.add(roleListTypeBean.getListType());
			}
		}
		return issueTypeIDsSet;
	}
	
	/**
	 * Loads a map with:
	 * 	- key: roleID
	 * 	- value: list of issueTypeIDs
	 * @return
	 */
	static Map<Integer, List<Integer>> getIssueTypesForRoles() {
		Map<Integer, List<Integer>> issueTypesForRoleMap = new HashMap<Integer, List<Integer>>();
		List<TRoleListTypeBean> assignedIssueTypeIDs = roleListTypeDAO.loadAll();
		for (TRoleListTypeBean roleListTypeBean : assignedIssueTypeIDs) {
			Integer roleID = roleListTypeBean.getRole();
			Integer issueType = roleListTypeBean.getListType();
			List<Integer> issueTypes = issueTypesForRoleMap.get(roleID);
			if (issueTypes==null) {
				issueTypes = new LinkedList<Integer>();
				issueTypesForRoleMap.put(roleID, issueTypes);
			}
			issueTypes.add(issueType);
		}
		return issueTypesForRoleMap; 
	}

	public static Map<Integer,String> getFlagNameMap(Locale locale) {
		Map<Integer,String> flagNameMap = new HashMap<Integer, String>();
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.READANYTASK),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.readAny", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.MODIFYANYTASK),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.modifyAny", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.CREATETASK),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.create", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.CLOSEANYTASK),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.closeAny", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.CLOSETASKIFRESPONSIBLE),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.closeIfResponsible", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.closeIfManagerOrOrOriginator", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.MANAGER),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.isManager", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.RESPONSIBLE),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.isResonsible", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.CONSULTANT),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.isConsultant", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.INFORMANT),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.isInformant", locale));
		flagNameMap.put(Integer.valueOf(AccessFlagIndexes.PROJECTADMIN),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.isProjectAdmin", locale));
		/*flagNameMap.put(Integer.valueOf(AccessFlagMigrationIndexes.EXTERN),
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.role.permissionflag.extern", locale));*/
		return flagNameMap;
	}
	
	/**
	 * Load roleBeans by roleIDs
	 * @return
	 */
	public static List<TRoleBean> loadByRoleIDs(List<Integer> roleIDs) {
		return roleDAO.loadByRoleIDs(roleIDs);
	}
	
	/**
	 * Load the role to list type assignments for roles
	 * @param roleIDs
	 * @return
	 */
	public static List<TRoleListTypeBean> loadByRoles(List<Integer> roleIDs) {
		return roleListTypeDAO.loadByRoles(roleIDs);
	}
	
	/**
	 * Filter out the roles which have no correspondent issueType in the TRoleListType table
	 * but have at least one other issueType assigned
	 * @param selectedRoleIDs
	 * @param issueType
	 * @return
	 */
	public static List<Integer> filterRolesByIssueType(List<Integer> selectedRoleIDs, Integer issueType) {
		List<Integer> filteredRoleIDs = new LinkedList<Integer>();
		if (selectedRoleIDs==null || selectedRoleIDs.isEmpty()) {
			return filteredRoleIDs;
		}
		List<TRoleListTypeBean> assignmentsList = loadByRoles(selectedRoleIDs);
		//assignments found with this issue
		Set<Integer> foundWithAccordingIssueType = new HashSet<Integer>();
		//assignments found with other issue
		Set<Integer> foundWithOtherIssueType = new HashSet<Integer>();
		if (assignmentsList!=null) {
			for (TRoleListTypeBean roleListTypeBean : assignmentsList) {
				if (issueType.equals(roleListTypeBean.getListType())) {
					foundWithAccordingIssueType.add(roleListTypeBean.getRole());
				} else {
					foundWithOtherIssueType.add(roleListTypeBean.getRole());
				}
			}
		}
		for (Integer roleID : selectedRoleIDs) {
			if (foundWithAccordingIssueType.contains(roleID) || !foundWithOtherIssueType.contains(roleID)) {
				filteredRoleIDs.add(roleID);
			}
		}
		return filteredRoleIDs;
	}

}
