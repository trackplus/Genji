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

package com.aurel.track.admin.user.userLevel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.user.userLevel.UserLevelBL.USER_LEVEL_ACTION_IDS;
import com.aurel.track.admin.user.userLevel.UserLevelBL.USER_LEVEL_ACTION_KEYS;
import com.aurel.track.beans.TBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TUserLevelBean;
import com.aurel.track.beans.TUserLevelSettingBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.tree.TreeNodeBaseTO;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.IconClass;

public class UserLevelConfigBL {
	
	/**
	 * Parse the userLevelID from nodeID
	 * @param node
	 */
	private static Integer getUserLevelID(String node) {
		Integer userLevelID = null;
		if (node!=null && !"".equals(node)) {
			userLevelID = Integer.valueOf(node);
		}
		return userLevelID;
	}

	/**
	 * Gets the assigned nodes for a project
	 * @param node
	 * @return
	 */
	public static List<TreeNodeBaseTO> getCustomUserLevels(Locale locale) {
		List<TreeNodeBaseTO> nodes = new LinkedList<TreeNodeBaseTO>();
		List<TUserLevelBean> userLevelBeans = UserLevelBL.loadCustomUserLevels(locale);
		if (userLevelBeans!=null) {
			for (TUserLevelBean userLevelBean : userLevelBeans) {
				TreeNodeBaseTO treeNodeTO = new TreeNodeBaseTO(
						userLevelBean.getObjectID().toString(), userLevelBean.getLabel(), IconClass.USER_LEVEL_CLASS, true);
				nodes.add(treeNodeTO);
			}
		}
		return nodes;
	}
	
	/** Gets the assignment detail json for a node
	 * @param node
	 * @param locale	
	 * @return
	 */
	static String getUserLevelSettingsJSON(String node, Locale locale) {
		Integer userLevelID = getUserLevelID(node);
		List<TUserLevelSettingBean> assignedUserLevelSettings = UserLevelBL.loadActionsByUserLevel(userLevelID);
		Map<Integer, TUserLevelSettingBean> actionsMap = new HashMap<Integer, TUserLevelSettingBean>();
		if (assignedUserLevelSettings!=null) {
			for (TUserLevelSettingBean userLevelSettingBean : assignedUserLevelSettings) {
				actionsMap.put(userLevelSettingBean.getACTIONKEY(), userLevelSettingBean);
			}
		}
		List<UserLevelActionTO> definedUserLevels = getUserLevelActions(locale);
		for (UserLevelActionTO userLevelTO : definedUserLevels) {
			TUserLevelSettingBean userLevelSettingBean = actionsMap.get(userLevelTO.getId());
			boolean selected = false;
			if (userLevelSettingBean!=null) {
				selected = userLevelSettingBean.isActive();
			}
			userLevelTO.setSelected(selected);
		}
		return UserLevelsJSON.encodeUserLevelSettings(definedUserLevels);	
	}

	/**
	 * Get the detail JSON for edit/add 
	 * @param node
	 * @param add
	 * @return
	 */
	public static String getEditDetailJSON(String node, boolean add, Locale locale) {
		Integer userLevelID = getUserLevelID(node);
		String name = null;
		String description = null;
		if (userLevelID!=null && !add) { 
			TUserLevelBean userLevelBean = UserLevelBL.loadByPrimaryKey(userLevelID);
			if (userLevelBean!=null) {
				name = LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.USER_LEVEL, userLevelID, locale);
				description = userLevelBean.getDescription();
				if (name==null) {
					name = userLevelBean.getLabel();
				}
			}
		}
		return UserLevelsJSON.getUserLevelEditDetailJSON(name, description);
	}
	
	/**
	 * Whether the label is valid (typically not duplicated) 
	 * @param objectID
	 * @param name
     * @param locale
	 * @return
	 */
	/*public static String isValidLabel(Integer objectID, String name, Locale locale) {
		if(name==null||name.trim().length()==0){
			return LocalizeUtil.getParametrizedString("common.err.required", 
					new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
							"admin.user.group.lbl.group", locale)} , locale);
		}
		TPersonBean personBean = PersonBL.loadGroupByName(name);
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
	}*/
	
	/**
	 * Saves a group
	 * @param objectID
	 * @param name
	 * @param add
	 * @return
	 */
	static String saveUserLevel(String node, boolean add, String name, String description, Locale locale) {
		Integer userLevelID = getUserLevelID(node);
		TUserLevelBean userLevelBean = null;
		boolean reloadTree = true;
		if (!add) {
			//edit existing
			userLevelBean = UserLevelBL.loadByPrimaryKey(userLevelID);
		}
		if (userLevelBean==null) {
			//either add or the node was deleted by other user in the meantime
			userLevelBean = new TUserLevelBean();
		}
		reloadTree = EqualUtils.notEqual(name, userLevelBean.getLabel());
		userLevelBean.setLabel(name);
		userLevelBean.setDescription(description);
		userLevelID = UserLevelBL.save(userLevelBean);
		LocalizeBL.saveLocalizedResource(new TUserLevelBean().getKeyPrefix(), userLevelID, name, locale);
		//reload cache
		UserLevelsProxy.getInstance().loadUserLevels();
		return UserLevelsJSON.getUserLevelSaveDetailJSON(userLevelID.toString(), reloadTree);
	}
	
	
	
	/**
	 * Deletes a group
	 * @param node
	 * @return
	 */
	static String delete(String node) {
		Integer userLevelID = getUserLevelID(node);
		if (userLevelID!=null) {
			if (hasDependentGroupData(userLevelID)) {
				return JSONUtility.encodeJSONFailure(null, JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
			} else {
				deleteUserLevel(userLevelID);
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}
	
	private static void  deleteUserLevel(Integer userLevelID) {
		UserLevelBL.delete(userLevelID);
		LocalizeBL.removeLocalizedResources(new TRoleBean().getKeyPrefix(), userLevelID);
		//reload cache
		UserLevelsProxy.getInstance().loadUserLevels();
	}
	
	/**
	 * Whether a person has dependent data
	 * @param oldPersonID
	 * @return
	 */
	private static boolean hasDependentGroupData(Integer userLevelID) {
		return UserLevelBL.hasDependentData(userLevelID);
	}
	
	/**
	 * Creates the JSON string for replacement triggers 
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String prepareReplacement(String node, String errorMessage, Locale locale) {
		Integer userLevelID = getUserLevelID(node);
		String replacementEntity = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.userLevel.lbl.userLevel", locale);
		String name = "";
		List<TUserLevelBean> replacementUserLevelList = prepareReplacementUserLevels(userLevelID, locale);
		boolean isGengi = ApplicationBean.getApplicationBean().isGenji();
		if (!isGengi) {
			//client and full levels are hardcoded and does not exist in database
			TUserLevelBean clientUserLevel = new TUserLevelBean();
			clientUserLevel.setObjectID(TPersonBean.USERLEVEL.CLIENT);
			clientUserLevel.setLabel(LocalizeUtil.getLocalizedEntity(
	        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.CLIENT, locale));
			replacementUserLevelList.add(0, clientUserLevel);
		}
		TUserLevelBean fullUserLevel = new TUserLevelBean();
		fullUserLevel.setObjectID(TPersonBean.USERLEVEL.FULL);
		fullUserLevel.setLabel(LocalizeUtil.getLocalizedEntity(
        		new TUserLevelBean().getKeyPrefix(), TPersonBean.USERLEVEL.FULL, locale));
		replacementUserLevelList.add(0, fullUserLevel);
		if (userLevelID!=null) {
			TUserLevelBean userLevelBean = UserLevelBL.loadByPrimaryKey(userLevelID);
				if (userLevelBean!=null) {
					name = userLevelBean.getLabel();
				}
				return JSONUtility.createReplacementListJSON(true, name, replacementEntity, replacementEntity, (List)replacementUserLevelList, errorMessage, locale);
		}
		return JSONUtility.createReplacementListJSON(true, name, replacementEntity, replacementEntity, (List)replacementUserLevelList, errorMessage, locale);
	}
	
	/**
	 * Prepares the replacement triggers
	 * @param userLevelIDs
	 * @return
	 */
	static List<TUserLevelBean> prepareReplacementUserLevels(Integer userLevelID, Locale locale) {
		List<TUserLevelBean> replacementUserLevelsList = UserLevelBL.loadCustomUserLevels(locale);
		if (userLevelID!=null) {
			if (replacementUserLevelsList!=null) {
				Iterator<TUserLevelBean> iterator = replacementUserLevelsList.iterator();
				while (iterator.hasNext()) {
					TUserLevelBean userLevelBean = iterator.next();
					if (userLevelID.equals(userLevelBean.getObjectID())) {
						iterator.remove();
						break;
					}
				}
			}
		}
		return replacementUserLevelsList;
	}
	
	/**
	 * Replace and delete the group
	 * @param selectedGroupIDsList
	 * @param replacementID
	 */
	static void replaceAndDeleteUserLevel(String node, Integer replacementID) {
		Integer userLevelID = getUserLevelID(node);
		if (userLevelID!=null && replacementID!=null) {
			UserLevelBL.replace(userLevelID, replacementID);
		}
		delete(node);
	}
	
	/**
	 * Actualizes the user feature in the database
	 * @param userLevelID
	 * @param actionID
	 * @param checked
	 */
	public static void changeUserLevelAction(String node, Integer actionID, boolean checked) {
		Integer userLevelID = getUserLevelID(node);
		if (userLevelID!=null && actionID!=null) {
			if (checked) {
				List<TUserLevelSettingBean> userLevelActions = UserLevelBL.loadActionsByUserLevelAndAction(userLevelID, actionID);
				TUserLevelSettingBean userLevelSettingBean = null;
				if (userLevelActions==null || userLevelActions.isEmpty()) {
					userLevelSettingBean = new TUserLevelSettingBean();
					userLevelSettingBean.setUserLevel(userLevelID);
					userLevelSettingBean.setACTIONKEY(actionID);
				} else {
					userLevelSettingBean = userLevelActions.get(0);
				}
				userLevelSettingBean.setActive(checked);
				UserLevelBL.saveAction(userLevelSettingBean);
			} else {
				UserLevelBL.deleteAction(userLevelID, actionID);
			}
			//reload cache
			UserLevelsProxy.getInstance().loadUserLevels();
		}
	}
	
	public static List<UserLevelActionTO> getUserLevelActions(Locale locale) {
    	List<UserLevelActionTO> userLevels = new LinkedList<UserLevelActionTO>();
    	userLevels.add(new UserLevelActionTO(USER_LEVEL_ACTION_IDS.COCKPIT,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.COCKPIT, locale),
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_MENU, locale)));
    	
    	userLevels.add(new UserLevelActionTO(USER_LEVEL_ACTION_IDS.CONFIGURE_COCKPIT,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CONFIGURE_COCKPIT, locale),
    			
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.COCKPIT, locale)));
    	userLevels.add(new UserLevelActionTO(USER_LEVEL_ACTION_IDS.PROJECT_COCKPIT,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.PROJECT_COCKPIT, locale),
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_MENU, locale)));
    	
    	userLevels.add(new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale),
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_MENU, locale)));
    	
    	userLevels.add(new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_MAIN_FILTER,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale),
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale)));
    	
    	UserLevelActionTO filterProjects = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.MAIN_FILTER_PROJECT,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_PROJECT, locale));
    	List<String> pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	filterProjects.setPath(getFullPath(pathList));
    	
    	userLevels.add(filterProjects);
    	UserLevelActionTO filterFilters = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.MAIN_FILTER_FILTER,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_FILTER, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	filterFilters.setPath(getFullPath(pathList));
    	
    	userLevels.add(filterFilters);
    	List<Integer> fieldsInvolved = new LinkedList<Integer>();
    	fieldsInvolved.add(SystemFields.INTEGER_STATE);
    	fieldsInvolved.add(SystemFields.INTEGER_ISSUETYPE);
    	fieldsInvolved.add(SystemFields.INTEGER_PRIORITY);
    	Map<Integer, String> fieldLabels = FieldRuntimeBL.getLocalizedDefaultFieldLabels(fieldsInvolved, locale);
    	
    	UserLevelActionTO filterStatus = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.MAIN_FILTER_STATUS,
    			fieldLabels.get(SystemFields.INTEGER_STATE));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	filterStatus.setPath(getFullPath(pathList));
    	userLevels.add(filterStatus);
    	
    	UserLevelActionTO filterBaskets = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.MAIN_FILTER_BASKET,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	filterBaskets.setPath(getFullPath(pathList));
    	userLevels.add(filterBaskets);
    	
    	UserLevelActionTO subfilter = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_SUBFILTER,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_SUBFILTER, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	subfilter.setPath(getFullPath(pathList));
    	userLevels.add(subfilter);
    	
    	UserLevelActionTO subfilterProject = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.SUBFILTER_PROJECT,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.SUBFILTER_PROJECT, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_SUBFILTER, locale));
    	subfilterProject.setPath(getFullPath(pathList));
    	userLevels.add(subfilterProject);
    	
    	UserLevelActionTO subfilterStatus = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.SUBFILTER_STATUS,
    			fieldLabels.get(SystemFields.INTEGER_STATE));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_SUBFILTER, locale));
    	subfilterStatus.setPath(getFullPath(pathList));
    	userLevels.add(subfilterStatus);
    	
    	UserLevelActionTO subfilterPriority = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.SUBFILTER_PRIORITY,
    			fieldLabels.get(SystemFields.INTEGER_PRIORITY));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_SUBFILTER, locale));
    	subfilterPriority.setPath(getFullPath(pathList));
    	userLevels.add(subfilterPriority);
    	
    	UserLevelActionTO subfilterIssueType = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.SUBFILTER_ITEMTYPE,
    			fieldLabels.get(SystemFields.INTEGER_ISSUETYPE));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_SUBFILTER, locale));
    	subfilterIssueType.setPath(getFullPath(pathList));
    	userLevels.add(subfilterIssueType);
    	
    	UserLevelActionTO subfilterBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.SUBFILTER_BASKET,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.SUBFILTER_BASKET, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_SUBFILTER, locale));
    	subfilterBasket.setPath(getFullPath(pathList));
    	userLevels.add(subfilterBasket);
    	
    	UserLevelActionTO subfilterSchedule = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.SUBFILTER_SCHEDULE,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.SUBFILTER_SCHEDULE, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_SUBFILTER, locale));
    	subfilterSchedule.setPath(getFullPath(pathList));
    	userLevels.add(subfilterSchedule);
    	
    	UserLevelActionTO plannedItemBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.PLANNED_ITEMS_BASKET,
    			LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.BASKET_KEYPREFIX, TBasketBean.BASKET_TYPES.PLANNED_ITEMS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	plannedItemBasket.setPath(getFullPath(pathList));
    	userLevels.add(plannedItemBasket);
    	
    	UserLevelActionTO nextActionsBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.NEXT_ACTIONS_BASKET,
    			LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.BASKET_KEYPREFIX, TBasketBean.BASKET_TYPES.NEXT_ACTIONS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	nextActionsBasket.setPath(getFullPath(pathList));
    	userLevels.add(nextActionsBasket);
    	
    	UserLevelActionTO reminderBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.REMINDER_BASKET,
    			LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.BASKET_KEYPREFIX, TBasketBean.BASKET_TYPES.CALENDAR, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	reminderBasket.setPath(getFullPath(pathList));
    	userLevels.add(reminderBasket);
    	
    	UserLevelActionTO delegatedBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.DELEGATED_BASKET,
    			LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.BASKET_KEYPREFIX, TBasketBean.BASKET_TYPES.DELEGATED, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	delegatedBasket.setPath(getFullPath(pathList));
    	userLevels.add(delegatedBasket);
    	
    	UserLevelActionTO trashBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.TRASH_BASKET,
    			LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.BASKET_KEYPREFIX, TBasketBean.BASKET_TYPES.TRASH, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	trashBasket.setPath(getFullPath(pathList));
    	userLevels.add(trashBasket);
    	
    	UserLevelActionTO incubatorBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.INCUBATOR_BASKET,
    			LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.BASKET_KEYPREFIX, TBasketBean.BASKET_TYPES.INCUBATOR, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	incubatorBasket.setPath(getFullPath(pathList));
    	userLevels.add(incubatorBasket);
    	
    	UserLevelActionTO referenceBasket = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.REFERENCE_BASKET,
    			LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.BASKET_KEYPREFIX, TBasketBean.BASKET_TYPES.REFERENCE, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_MAIN_FILTER, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_FILTER_BASKET, locale));
    	referenceBasket.setPath(getFullPath(pathList));
    	userLevels.add(referenceBasket);
    	
    	UserLevelActionTO flatGridView = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_FLAT_GRID_VIEW,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_FLAT_GRID_VIEW, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_VIEW, locale));
    	flatGridView.setPath(getFullPath(pathList));
    	userLevels.add(flatGridView);
    	
    	UserLevelActionTO treeGridView = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_TREE_GRID_VIEW,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_TREE_GRID_VIEW, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_VIEW, locale));
    	treeGridView.setPath(getFullPath(pathList));
    	userLevels.add(treeGridView);
    	
    	UserLevelActionTO hierarchicalView = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_HIERARCHICAL_VIEW,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_HIERARCHICAL_VIEW, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_VIEW, locale));
    	hierarchicalView.setPath(getFullPath(pathList));
    	userLevels.add(hierarchicalView);
    	
    	/*UserLevelActionTO ganttView = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_GANTT_VIEW,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_GANTT_VIEW, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_VIEW, locale));
    	ganttView.setPath(getFullPath(pathList));
    	userLevels.add(ganttView);*/
    	
    	UserLevelActionTO agileView = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ITEM_NAVIGATOR_AGILE_VIEW,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_AGILE_VIEW, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ITEM_NAVIGATOR_VIEW, locale));
    	agileView.setPath(getFullPath(pathList));
    	userLevels.add(agileView);
    	
    	UserLevelActionTO privateWorkspace = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.PRIVATE_WORKSPACE,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.PRIVATE_WORKSPACE, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.PROJECTS, locale));
    	privateWorkspace.setPath(getFullPath(pathList));
    	userLevels.add(privateWorkspace);
    	
    	userLevels.add(new UserLevelActionTO(USER_LEVEL_ACTION_IDS.REPORTS,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.REPORTS, locale),
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_MENU, locale)));
    	
    	userLevels.add(new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ADMINISTARTION,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale),
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MAIN_MENU, locale)));
    	
    	UserLevelActionTO manageFilters = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.MANAGE_FILTERS,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.MANAGE_FILTERS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	manageFilters.setPath(getFullPath(pathList));
    	userLevels.add(manageFilters);
    	
    	UserLevelActionTO reportTemplates = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.REPORT_TEMPLATES,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.REPORT_TEMPLATES, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	reportTemplates.setPath(getFullPath(pathList));
    	userLevels.add(reportTemplates);
    	
    	UserLevelActionTO roles = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ROLES,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ROLES, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	roles.setPath(getFullPath(pathList));
    	userLevels.add(roles);
    	
    	UserLevelActionTO forms = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.FORMS,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.FORMS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	forms.setPath(getFullPath(pathList));
    	userLevels.add(forms);
    	
    	UserLevelActionTO fields = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.FIELDS,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.FIELDS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	fields.setPath(getFullPath(pathList));
    	userLevels.add(fields);
    	
    	
    	UserLevelActionTO lists = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.LISTS,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.LISTS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	lists.setPath(getFullPath(pathList));
    	userLevels.add(lists);
    	
    	UserLevelActionTO userLevelConfig = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.USER_LEVEL,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.USER_LEVEL, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	userLevelConfig.setPath(getFullPath(pathList));
    	userLevels.add(userLevelConfig);
    	
    	UserLevelActionTO workflows = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.WORKFLOWS,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.WORKFLOWS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.CUSTOMIZE, locale));
    	workflows.setPath(getFullPath(pathList));
    	userLevels.add(workflows);
    	
    	UserLevelActionTO adminActions = new UserLevelActionTO(USER_LEVEL_ACTION_IDS.ADMINISTARTION_ACTIONS,
    			LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION_ACTIONS, locale));
    	pathList = new LinkedList<String>();
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION, locale));
    	pathList.add(LocalizeUtil.getLocalizedTextFromApplicationResources(USER_LEVEL_ACTION_KEYS.ADMINISTARTION_ACTIONS, locale));
    	adminActions.setPath(getFullPath(pathList));
    	userLevels.add(adminActions);
    	return userLevels;
    }
   
    /**
     * Build the full path from path list
     * @param pathParts
     * @return
     */
    private static String getFullPath(List<String> pathParts) {
    	StringBuilder stringBuilder = new StringBuilder();
    	if (pathParts!=null && !pathParts.isEmpty()) {
    		for (Iterator<String> iterator = pathParts.iterator(); iterator.hasNext();) {
				String pathPart = iterator.next();
				stringBuilder.append(pathPart);
				if (iterator.hasNext()) {
					stringBuilder.append(" -> ");
				}
			}
    	}
    	return stringBuilder.toString();
    }
    
    public static class UserLevelActionTO {
    	private int id;
    	private String label;
    	private String path;
		//private List<String> pathList;
		private boolean selected;
    	
    	public UserLevelActionTO(int id, String label) {
			super();
			this.id = id;
			this.label = label;
		}

		public UserLevelActionTO(int id, String label, String path) {
			super();
			this.id = id;
			this.label = label;
			this.path = path;
		}
    	
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
    	
    }
}
