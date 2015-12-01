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

package com.aurel.track.admin.customize.treeConfig.field;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FieldDAO;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.ICustomFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.custom.picker.CustomPickerRT;
import com.aurel.track.fieldType.runtime.custom.picker.UserPickerRT;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectBaseRT;
import com.aurel.track.fieldType.types.FieldTypeManager;

/**
 * BL class for fields
 * @author Tamas Ruff
 *
 */
public class FieldBL {
	private static final Logger LOGGER = LogManager.getLogger(FieldBL.class); 

	private static FieldDAO fieldDAO = DAOFactory.getFactory().getFieldDAO();

	/**
	 * Load all field beans
	 * @return
	 */
	public static TFieldBean loadByPrimaryKey(Integer objectID) {
		return fieldDAO.loadByPrimaryKey(objectID);
	}

	/**
	 * Loads all Fields from table
	 * @return 
	 */
	public static List<TFieldBean> loadAll() {
		return fieldDAO.loadAll();
	}

	/**
	 * Loads all active fields from TField table 
	 * @return 
	 */
	public static List<TFieldBean> loadActive() {
		return fieldDAO.loadActive();
	}

	/**
	 * Loads all custom fields from TField table
	 * @return
	 */
	public static List<TFieldBean> loadCustom() {
		return fieldDAO.loadCustom();
	}

	/**
	 * Loads all system fields from TField table
	 * @return
	 */
	public static List<TFieldBean> loadSystem() {
		return fieldDAO.loadSystem();
	}

	/**
	 * Loads the fields by names
	 * @param names
	 * @return
	 */
	public static List<TFieldBean> loadByNames(List<String> names) {
		return fieldDAO.loadByNames(names);
	}

	/**
	 * Loads all filter fields from TField table (present in the upper part filter)
	 * @return
	 */
	public static List<TFieldBean> loadFilterFields() {
		return fieldDAO.loadFilterFields();
	}

	/**
	 * Loads all custom fieldBeans which are specified for a workItem 
	 * @param workItemID
	 * @return
	 */
	public static List<TFieldBean> loadSpecifiedCustomFields(Integer workItemID) {
		return fieldDAO.loadSpecifiedCustomFields(workItemID);
	}

	/**
	 * Load all fields from screen
	 * @param screenID
	 * @return
	 */
	public static List<TFieldBean> loadAllFields(Integer screenID) {
		return fieldDAO.loadAllFields(screenID);
	}

	/**
	 * Load all fields from screens
	 * @param screenIDs
	 * @return
	 */
	public static List<TFieldBean> loadByScreens(Object[] screenIDs) {
		return fieldDAO.loadByScreens(screenIDs);
	}

	public static List<TFieldBean> loadAllFieldsOnCard(Integer cardPanelID) {
		return fieldDAO.loadAllFieldsOnCard(cardPanelID);
	}

	/**
	 * Load fields by fieldID 
	 * @param fieldIDs
	 * @return
	 */
	public static List<TFieldBean> loadByFieldIDs(Object[] fieldIDs) {
		return fieldDAO.loadByFieldIDs(fieldIDs);
	}

	/**
	 * Saves a field in the TField table
	 * @param fieldBean
	 * @return
	 */
	public static Integer save(TFieldBean fieldBean) {
		return fieldDAO.save(fieldBean);
	}

	/**
	 * Gets the deprecated flag for an array of fields
	 * @param fieldIDs
	 * @return
	 */
	public static Map<Integer, Boolean> getDeprecatedMap(Object[] fieldIDs) {
		Map<Integer, Boolean> deprecatedMap = new HashMap<Integer, Boolean>();
		List<TFieldBean> fieldBeans = FieldBL.loadByFieldIDs(fieldIDs);
		if (fieldBeans!=null) {
			for (TFieldBean fieldBean : fieldBeans) {
				deprecatedMap.put(fieldBean.getObjectID(), Boolean.valueOf(fieldBean.isDeprecatedString()));
			}
		}
		return deprecatedMap;
	}

	/**
	 * Gets the custom select fields: exclude the custom pickers
	 * @return
	 */
	public static List<Integer> getCustomSelectFieldIDs() {
		List<Integer> customSelectFieldIDs = new LinkedList<Integer>();
		List<TFieldBean> customFieldBeans = FieldBL.loadCustom();
		if (customFieldBeans!=null && !customFieldBeans.isEmpty()) {
			for (TFieldBean fieldBean : customFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.isCustom()) {
					ICustomFieldTypeRT customFieldTypeRT = (ICustomFieldTypeRT)fieldTypeRT;
					if (customFieldTypeRT.isCustomSelect()) {
						CustomSelectBaseRT customSelectBaseRT = null;
						try {
							customSelectBaseRT = (CustomSelectBaseRT)customFieldTypeRT;
						} catch(Exception e) {
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
						if (customSelectBaseRT!=null && !customSelectBaseRT.isCustomPicker()) {
							LOGGER.debug("Custom select field found " + fieldID);
							customSelectFieldIDs.add(fieldID);
						}
					}
				}
			}
		}
		return customSelectFieldIDs;
	}
	
	/**
	 * Gets the custom select fields: exclude the custom pickers
	 * @return
	 */
	public static List<Integer> getCustomPickerFieldIDs(Integer systemFieldID) {
		List<Integer> customSelectFieldIDs = new LinkedList<Integer>();
		List<TFieldBean> customFieldBeans = FieldBL.loadCustom();
		if (customFieldBeans!=null && !customFieldBeans.isEmpty()) {
			for (TFieldBean fieldBean : customFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.isCustom()) {
					ICustomFieldTypeRT customFieldTypeRT = (ICustomFieldTypeRT)fieldTypeRT;
					if (customFieldTypeRT.isCustomSelect() && customFieldTypeRT.isLookup()) {
						CustomSelectBaseRT customSelectBaseRT = null;
						try {
							customSelectBaseRT = (CustomSelectBaseRT)customFieldTypeRT;
						} catch(Exception e) {
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
						if (customSelectBaseRT!=null) {
							if (customSelectBaseRT.isCustomPicker()) {
								CustomPickerRT customPickerRT = (CustomPickerRT)customSelectBaseRT;
								if (customPickerRT!=null) {
									Integer systemOptionType = customPickerRT.getSystemOptionType();
									if (systemOptionType!=null && systemOptionType.equals(systemFieldID)) {
										LOGGER.debug("Custom picker field based on " + systemFieldID + " found " + fieldID);
										customSelectFieldIDs.add(fieldID);
									}
								}
							}
						}
					}
				}
			}
		}
		return customSelectFieldIDs;
	}

	/**
	 * Gets the user picker fields
	 * @return
	 */
	public static List<Integer> getUserPickerFieldIDs() {
		List<Integer> userPickerFieldIDs = new LinkedList<Integer>();
		List<TFieldBean> customFieldBeans = FieldBL.loadCustom();
		if (customFieldBeans!=null && !customFieldBeans.isEmpty()) {
			for (TFieldBean fieldBean : customFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.isUserPicker()) {
					LOGGER.debug("User picker field found " + fieldID);
					userPickerFieldIDs.add(fieldID);
				}
			}
		}
		return userPickerFieldIDs;
	}

	/**
	 * Gets the custom date fields
	 * @return
	 */
	public static List<Integer> getCustomDateFieldIDs() {
		List<Integer> customDateFieldIDs = new LinkedList<Integer>();
		List<TFieldBean> customFieldBeans = FieldBL.loadCustom();
		if (customFieldBeans!=null && !customFieldBeans.isEmpty()) {
			for (TFieldBean fieldBean : customFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.getValueType()==ValueType.DATE) {
					LOGGER.debug("Custom date field found " + fieldID);
					customDateFieldIDs.add(fieldID);
				}
			}
		}
		return customDateFieldIDs;
	}
	
	/**
	 * Gets the on behalf of user picker fields
	 * @return
	 */
	public static List<Integer> getOnBehalfOfUserPickerFieldIDs() {
		List<Integer> onBehalfPickerFieldIDs = new LinkedList<Integer>();
		List<TFieldBean> customFieldBeans = FieldBL.loadCustom();
		if (customFieldBeans!=null && !customFieldBeans.isEmpty()) {
			for (TFieldBean fieldBean : customFieldBeans) {
				Integer fieldID = fieldBean.getObjectID();
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null && fieldTypeRT.isUserPicker()) {
					UserPickerRT userPickerRT = (UserPickerRT)fieldTypeRT;
					if (userPickerRT.inheritsOriginatorRole()) {
						LOGGER.debug("On behalf user picker field found " + fieldID);
						onBehalfPickerFieldIDs.add(fieldID);
					}
				}
			}
		}
		return onBehalfPickerFieldIDs;
	}

	/**
	 * Whether the field is a custom select field
	 * @param fieldID
	 * @return
	 */
	public static boolean isCustomSelect(Integer fieldID) {
		if (fieldID!=null) {
			IFieldTypeRT fieldTypeRT = null;
			if (fieldID.intValue()>0) {
				//not a pseudo field
				fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			}
			if (fieldTypeRT!=null && fieldTypeRT.isCustom()) {
				ICustomFieldTypeRT customFieldTypeRT = (ICustomFieldTypeRT)fieldTypeRT;
				return customFieldTypeRT.isCustomSelect() && customFieldTypeRT.isLookup();
			}
		}
		return false;
	}

	/**
	 * Whether the field is a custom select field
	 * @param fieldID
	 * @return
	 */
	public static boolean isCustomPicker(Integer fieldID) {
		if (fieldID!=null) {
			IFieldTypeRT fieldTypeRT = null;
			if (fieldID.intValue()>0) {
				//not a pseudo field
				fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			}
			if (fieldTypeRT!=null && fieldTypeRT.isCustom()) {
				ICustomFieldTypeRT customFieldTypeRT = (ICustomFieldTypeRT)fieldTypeRT;
				if (customFieldTypeRT.isCustomSelect()) {
					try {
						CustomSelectBaseRT customSelectBaseRT = (CustomSelectBaseRT)customFieldTypeRT;
						if (customSelectBaseRT!=null) {
							return customSelectBaseRT.isCustomPicker();
						}
					} catch(Exception e) {
						LOGGER.info(e.getMessage()); 
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the system option type corresponding to the picker
	 * @param fieldID
	 * @return
	 */
	public static Integer getSystemOptionType(Integer fieldID) {
		if (fieldID!=null) {
			IFieldTypeRT fieldTypeRT = null;
			if (fieldID.intValue()>0) {
				//not a pseudo field
				fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			}
			if (fieldTypeRT!=null && fieldTypeRT.isCustom() && fieldTypeRT.isLookup()) {
				CustomSelectBaseRT customSelectBaseRT = (CustomSelectBaseRT)fieldTypeRT;
				if (customSelectBaseRT.isCustomPicker()) {
					CustomPickerRT customPickerRT = (CustomPickerRT)customSelectBaseRT;
					return customPickerRT.getSystemOptionType();
				}	
			}
		}
		return null;
	}

	/**
	 * Whether the field is a tree based custom picker
	 * @param fieldTypeRT
	 * @return
	 */
	public static boolean isTree(Integer fieldID) {
		if (fieldID!=null) {
			IFieldTypeRT fieldTypeRT = null;
			if (fieldID.intValue()>0) {
				//not a pseudo field
				fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			}
			if (fieldTypeRT!=null && fieldTypeRT.isLookup()) {
				try {
					ILookup lookup = (ILookup)fieldTypeRT;
					return lookup.isTree();
				} catch(Exception e) {
					LOGGER.info(e.getMessage()); 
					LOGGER.debug(ExceptionUtils.getStackTrace(e));;
				}
			}
		}
		return false;
	}

}
