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


package com.aurel.track.admin.customize.treeConfig.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.admin.customize.treeConfig.TreeNodeConfigUtils;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ConfigItem;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TGeneralSettingsBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TTextBoxSettingsBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.FieldDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.design.IFieldTypeDT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.types.FieldType;
import com.aurel.track.fieldType.types.FieldTypeDescriptorUtil;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.ItemScreenCache;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.FieldTypeDescriptor;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;

public class FieldDesignBL {
	
	private static FieldDAO fieldDAO = DAOFactory.getFactory().getFieldDAO();
	
	public static interface ICON_CLS {
		static String CALENDAR_ICONCLS = "calendar-ticon";
		static String CASCADE_SELECT_ICONCLS = "cascadeSelect-ticon";
		static String CHECK_ICONCLS = "check-ticon";
		static String COMBO_ICONCLS = "combo-ticon";
		static String LABEL_ICONCLS = "label-ticon";
		static String TEXTFIELD_ICONCLS = "textField-ticon";
	}
	
	public static boolean isDeprecated(Integer objectID) {
		TFieldBean fieldBean = FieldBL.loadByPrimaryKey(objectID);
		if (fieldBean!=null) {
			return fieldBean.isDeprecatedString();
		} else {
			return true;
		}
	}
	
	/**
	 * Returns whether the name is unique for the field
	 * @param name
	 * @param fieldID
	 * @return
	 */
	public static boolean isNameUnique(String name, Integer fieldID) {
		return fieldDAO.isNameUnique(name, fieldID);
	}
	
	/**
	 * Load all field beans
	 * @return
	 */
	public static List<TFieldBean> loadByName(String name) {
		return fieldDAO.loadByName(name);
	}
	
	/**
	 * Loads the field, the field configuration and the field settings objects
	 * name -> history are submitted after fieldType change by add for not to reset 
	 * them after each fieldType change (otherwise they are null)
	 * @param node
	 * @param name
	 * @param fieldTypeString
	 * @param filterField
	 * @param deprecated
	 * @param description
	 * @param label
	 * @param tooltip
	 * @param required
	 * @param history
	 * @param add
	 * @param personBean
	 * @param locale
	 * @param servletResponse
	 */
	public static void loadFieldConfigDetail(String node, String name,
			String fieldTypeString, boolean filterField, boolean deprecated, String description,
			String label, String tooltip, boolean required, boolean history,
			boolean add, TPersonBean personBean, Locale locale, HttpServletResponse servletResponse) {
		TreeConfigIDTokens treeConfigIDTokens = null;
		Integer issueTypeID = null;
		Integer projectTypeID = null;
		Integer projectID = null;
		Integer configRel = null;
		if (node!=null) {
			//not add from an "unknown" node
			treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
			issueTypeID = treeConfigIDTokens.getIssueTypeID();
			projectTypeID = treeConfigIDTokens.getProjectTypeID();
			projectID = treeConfigIDTokens.getProjectID();
			configRel = treeConfigIDTokens.getConfigRelID();
		}		
		TFieldBean fieldBean = null;
		boolean mayModifyField = false;
		boolean fieldAlwaysRequired = false;
		boolean isDefaultConfig = issueTypeID==null && projectTypeID==null && projectID==null;
		boolean inheritedConfig = false;
		boolean isCustomField = false;
		boolean fieldTypeDisabled = false;
		List<FieldTypeDescriptor> customFieldTypesList = null;
		IFieldTypeDT fieldTypeDT;
		IFieldTypeRT fieldTypeRT;
		boolean canDelete = false;
		String specificConfigJSON = "";
		FieldTypeDescriptor fieldTypeDescriptor = null;
		if (add) {
			mayModifyField = true;
			fieldBean = new TFieldBean();
			isCustomField = true;
			customFieldTypesList = getCustomFieldTypeDescriptors(locale, add);
			if (fieldTypeString==null || "".equals(fieldTypeString)) {
				//first rendering by add
				if (customFieldTypesList!=null && !customFieldTypesList.isEmpty()) {
					fieldTypeDescriptor = customFieldTypesList.get(0);
					fieldTypeString = fieldTypeDescriptor.getTheClassName();
				}
			} else {
				fieldTypeDescriptor = FieldTypeDescriptorUtil.getFieldTypeDescriptor(fieldTypeString);
			}
			FieldType fieldType = FieldType.fieldTypeFactory(fieldTypeString);
			fieldTypeDT = fieldType.getFieldTypeDT();
			fieldTypeRT = fieldType.getFieldTypeRT();
			if (fieldTypeDT!=null && fieldTypeDescriptor != null) {
				//gets the default configuration for a new field 
				specificConfigJSON = fieldTypeDT.getDefaultSettingsJSON(personBean, locale, fieldTypeDescriptor.getBundleName()); 
			}
		} else {
			fieldTypeDisabled = true;
			//existing field: field configuration either direct or inherited  
			ConfigItem configItem = FieldConfigItemFacade.getInstance().getValidConfigDirect(
					issueTypeID, projectTypeID, projectID, configRel);
			if (configItem==null) {
				inheritedConfig = true;
				configItem = FieldConfigItemFacade.getInstance().getValidConfigFallback(
						issueTypeID, projectTypeID, projectID, configRel);
			}
			TFieldConfigBean fieldConfigBean = (TFieldConfigBean)configItem;
			fieldBean = FieldBL.loadByPrimaryKey(configRel);
			//field attributes
			mayModifyField = personBean.isSys() || personBean.getObjectID().equals(fieldBean.getOwner());
			name = fieldBean.getName();
			String originalFieldTypeString =  fieldBean.getFieldType();
			if (fieldTypeString==null) {
				//not submitted (probably disabled)
				fieldTypeString = originalFieldTypeString;
			}
			filterField = fieldBean.isFilterFieldString();
			deprecated = fieldBean.isDeprecatedString();
			description = fieldBean.getDescription();
			fieldAlwaysRequired = fieldBean.isRequiredString();
			//field configuration attributes
			Integer configID = fieldConfigBean.getObjectID();
			label = fieldConfigBean.getLabel();
			tooltip = fieldConfigBean.getTooltip();
			required = fieldAlwaysRequired || fieldConfigBean.isRequiredString();
			history = fieldConfigBean.isHistoryString();
			canDelete = isOwner(personBean, fieldBean);
			/*if (personBean.isSys()) {
				canDelete = true;
			} else {
				if (personBean.isProjAdmin()) {
					//was the field created by the currently logged person?
					//if not - do not allow to delete even if it has no dependences at all
					if (fieldBean.getOwner()!=null && personBean.getObjectID().equals(fieldBean.getOwner())) {
						canDelete = true;
					}
				}
			}*/
			isCustomField = fieldBean.isCustomString();
			if (isCustomField) {
				FieldType fieldType = FieldType.fieldTypeFactory(originalFieldTypeString);
				if (fieldType!=null) {
					List<FieldType> compatibleFieldTypes = fieldType.getCompatibleFieldTypes();
					if (compatibleFieldTypes!=null && !compatibleFieldTypes.isEmpty()) {
						fieldTypeDisabled = false;
						Set<String> compatibleFieldTypeIDs = new HashSet<String>();
						//the actual field type is always present
						compatibleFieldTypeIDs.add(fieldType.getPluginID());
						for (FieldType compatibleFieldType : compatibleFieldTypes) {
							compatibleFieldTypeIDs.add(compatibleFieldType.getPluginID());
						}
						customFieldTypesList = getCustomFieldTypeDescriptors(locale, compatibleFieldTypeIDs);
					}
				}
				if (customFieldTypesList==null) {
					customFieldTypesList = getCustomFieldTypeDescriptors(locale, add);
				}
			}
			fieldTypeDT = FieldTypeManager.getFieldTypeDT(configRel);
			fieldTypeRT =FieldTypeManager.getFieldTypeRT(configRel);
			fieldTypeDescriptor = FieldTypeDescriptorUtil.getFieldTypeDescriptor(fieldTypeString);
			//gets the existing configuration for a field
			if (fieldTypeDT!=null && fieldTypeDescriptor!=null) {
				specificConfigJSON = fieldTypeDT.getSettingsJSON(configID, treeConfigIDTokens, personBean, locale, fieldTypeDescriptor.getBundleName());
			}
		}
		String specificFieldConfigClass = null;
		if (fieldTypeDescriptor!=null) {
			specificFieldConfigClass = fieldTypeDescriptor.getJsConfigClass();
		}
		if (fieldTypeDT != null) {
			JSONUtility.encodeJSON(servletResponse, 
					FieldConfigJSON.createFieldDetailJSON(node, name, 
							inheritedConfig || !mayModifyField,
							fieldTypeString, customFieldTypesList,
							isCustomField, fieldTypeDisabled, 
							filterField,
							isDefaultConfig && (fieldTypeRT.processLoadMatcherDT(configRel)!=null || SystemFields.INTEGER_ARCHIVELEVEL.equals(configRel)),
							deprecated, 
							isDefaultConfig && fieldTypeDT.renderDeprecatedFlag(),
							description, canDelete,
							label, tooltip, 
							required, !fieldAlwaysRequired && fieldTypeDT.renderRequiredFlag(),
							history, fieldTypeDT.renderHistoryFlag(),
							inheritedConfig, specificFieldConfigClass, specificConfigJSON));
		}
	}
	
	/**
	 * Whether the person o owner of the field
	 * @param personBean
	 * @param fieldBean
	 * @return
	 */
	private static boolean isOwner(TPersonBean personBean, TFieldBean fieldBean) {
		if (personBean.isSys()) {
			return true;
		} else {
			if (personBean.isProjAdmin()) {
				//was the field created by the currently logged person?
				//if not - do not allow to delete even if it has no dependences at all
				if (fieldBean.getOwner()!=null && personBean.getObjectID().equals(fieldBean.getOwner())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Validates the input	
	 */
	private static String validate(String name, String fieldType, Integer fieldID, Map<Integer, Object> settings, Locale locale) {
		StringBuilder stringBuilder = new StringBuilder(); 
		if (name==null || name.trim().equals("")) {
			stringBuilder.append(LocalizeUtil.getParametrizedString("common.err.required", 
					new String[]{LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.name", locale)}, locale)).append("\n");
		} else {
			if (!isNameUnique(name, fieldID)) {
				stringBuilder.append(LocalizeUtil.getParametrizedString("common.err.unique", 
						new String[]{LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.name", locale)}, locale)).append("\n");
			} else {
				String trimmedName = name.trim();
				String patternString="[^\\s|:|\\<|\\>|\\.|\\(|\\)|\\'|\\\"|\\,]*";
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(trimmedName);
				if (!matcher.matches()) {
					stringBuilder.append(LocalizeUtil.getParametrizedString("common.err.regexPattern", 
							new String[]{LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.name", locale)}, locale)).append("\n");
				}
			}
		}
		//gather now the settings, it will be used also in save()
		FieldType fieldTypeObj = null;
		if (fieldType==null) {
			//save existing
			fieldTypeObj = FieldTypeManager.getFieldType(fieldID);
		} else {
			//save new
			fieldTypeObj = FieldType.fieldTypeFactory(fieldType);
		}
		IFieldTypeDT fieldTypeDT = fieldTypeObj.getFieldTypeDT();
		List<ErrorData> errorDataList = fieldTypeDT.isValidSettings(settings);
		if (errorDataList!=null) {
			for (ErrorData errorData : errorDataList) {
				String resourceKey = errorData.getResourceKey();
				String bundleName = FieldTypeDescriptorUtil.getFieldTypeDescriptor(fieldTypeObj.getPluginID()).getBundleName();
				List<ErrorParameter> resourceParameters = errorData.getResourceParameters();
				if (resourceParameters!=null && !resourceParameters.isEmpty()) {
					stringBuilder.append(LocalizeUtil.getLocalizedTextWithParams(
							resourceKey, locale, bundleName, resourceParameters.toArray())).append("\n");
				} else {
					stringBuilder.append(LocalizeUtil.getLocalizedText(resourceKey, locale, bundleName)).append("\n");
				}
			}
		}
		return stringBuilder.toString();
	}
	
	
	/**
	 * Saves a field configuration
	 * @param node
	 * @param name
	 * @param renameConfirmed
	 * @param fieldType
	 * @param filterField
	 * @param deprecated
	 * @param description
	 * @param label
	 * @param tooltip
	 * @param required
	 * @param history
	 * @param settings
	 * @param add
	 * @param person
	 * @param locale
	 * @param servletResponse
	 */
	public static void saveFieldConfigDetail(String node, 
			String name, boolean renameConfirmed, String fieldType, boolean filterField, boolean deprecated, String description,
			String label, String tooltip, boolean required,
			boolean history, Map<Integer, Object> settings, boolean add, 
			TPersonBean person, Locale locale, HttpServletResponse servletResponse) {
		TreeConfigIDTokens treeConfigIDTokens = null;
		Integer issueTypeID = null;
		Integer projectTypeID = null;
		Integer projectID = null;
		Integer configRel = null;
		boolean addAlsoDomainConfig = false;
		boolean invalidateFields = false;
		boolean invalidateScreens = false;
		String addedToType = null; 
		Integer addedToConfigRelID = null; 
		if (node!=null && !"".equals(node)) {
			treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
			issueTypeID = treeConfigIDTokens.getIssueTypeID();
			projectTypeID = treeConfigIDTokens.getProjectTypeID();
			projectID = treeConfigIDTokens.getProjectID();
			configRel = treeConfigIDTokens.getConfigRelID();
			if (add) {
				//add from a "known" node
				addAlsoDomainConfig = issueTypeID!=null || projectTypeID!=null || projectID!=null;
				addedToType = treeConfigIDTokens.getType();
				addedToConfigRelID = treeConfigIDTokens.getConfigRelID();
			}
		} else {
			if (add) {
				//add from an unknown node (no node is selected in the tree)
				treeConfigIDTokens = new TreeConfigIDTokens();
				treeConfigIDTokens.setConfigType(TreeConfigBL.FIELD_CONFIG);
				treeConfigIDTokens.setType(TreeConfigBL.CONFIG_ITEM);
			}
		}
		
		TFieldConfigBean fieldConfigBean;		
		TFieldBean fieldBean = null;
		//name, fieldType, filter flag, deprecated flag description are submitted only for owner, because the are disabled i.e. not submitted 
		boolean isOwner = false; 
		if (add) {
			//new field
			isOwner = true;
			String validationResult = validate(name, fieldType, configRel, settings, locale);
			if (validationResult!=null && validationResult.length()>0) {
				JSONUtility.encodeJSON(servletResponse,	JSONUtility.encodeJSONFailure(validationResult));
				return;
			}
			fieldBean = new TFieldBean();
			fieldBean.setFieldType(fieldType);
			fieldBean.setIsCustom(BooleanFields.TRUE_VALUE);
			fieldBean.setOwner(person.getObjectID());
			fieldConfigBean = new TFieldConfigBean();
			invalidateFields = true;
		} else {
			//existing field  
			fieldConfigBean = (TFieldConfigBean)FieldConfigItemFacade.getInstance().getValidConfigDirect(
					issueTypeID, projectTypeID, projectID, configRel);
			fieldBean = FieldBL.loadByPrimaryKey(configRel);
			if (fieldType!=null && EqualUtils.notEqual(fieldType, fieldBean.getFieldType())) {
				//compatible fieldType submitted
				fieldBean.setFieldType(fieldType);
				invalidateScreens = true;
				invalidateFields = true;
			}
			isOwner = isOwner(person, fieldBean);
			if (isOwner) {
				String validationResult = validate(name, fieldType, configRel, settings, locale);
				if (validationResult!=null && validationResult.length()>0) {
					JSONUtility.encodeJSON(servletResponse,	JSONUtility.encodeJSONFailure(validationResult));
					return;
				}
				if (EqualUtils.notEqual(fieldBean.getName(), name)) {
					invalidateFields = true;
					if (!renameConfirmed) {
						JSONUtility.encodeJSON(servletResponse,	JSONUtility.encodeJSONSuccess(
							LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.detail.warning.rename", locale)));
						return;
					}
				}
			}
		}
		if (isOwner) {
			fieldBean.setName(name);
			fieldBean.setFilterFieldString(filterField);
			fieldBean.setDeprecatedString(deprecated);
			fieldBean.setDescription(description);
		}
		Integer fieldID = FieldBL.save(fieldBean);
		if (add) {
			//by add create a new node 
			fieldConfigBean.setField(fieldID);
			if (treeConfigIDTokens != null) {
				treeConfigIDTokens.setConfigRelID(fieldID);
				treeConfigIDTokens.setType(TreeConfigBL.CONFIG_ITEM);
			}
			node = TreeConfigIDTokens.encodeNode(treeConfigIDTokens);
		}
		fieldConfigBean.setLabel(label);
		fieldConfigBean.setTooltip(tooltip);
		fieldConfigBean.setRequiredString(required);
		fieldConfigBean.setHistoryString(history);
		Integer fieldConfigID = FieldConfigBL.save(fieldConfigBean);
		//default locale
		LocalizeBL.saveLocalizedResource(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX,
				fieldConfigID, fieldConfigBean.getLabel(), null);
		LocalizeBL.saveLocalizedResource(LocalizationKeyPrefixes.FIELD_TOOLTIP_KEY_PREFIX,
				fieldConfigID, fieldConfigBean.getTooltip(), null);
		//actual locale
		LocalizeBL.saveLocalizedResource(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX,
				fieldConfigID, fieldConfigBean.getLabel(), locale);
		LocalizeBL.saveLocalizedResource(LocalizationKeyPrefixes.FIELD_TOOLTIP_KEY_PREFIX,
				fieldConfigID, fieldConfigBean.getTooltip(), locale);
		IFieldTypeDT fieldTypeDT = FieldTypeManager.getFieldTypeDT(fieldID);
		if (fieldTypeDT!=null) {
			if (!add) {
				//first delete the settings: important mainly by general settings like user picker
				fieldTypeDT.deleteSettings(fieldConfigID);
				/*FieldType fieldTypeInstance = FieldTypeManager.getFieldType(fieldID);
				if (fieldTypeInstance.getCompatibleFieldTypes()!=null) {
					//1. after changing the field type for existing field to a compatible field the screens should be refreshed because of the possible different renderer
					//2. same if multiple flag is modified (different renderer) but 2. is a subset of 1.
					//In case of changing UserPicker, or UserPickerBehalf wee need to clear screen cache to be reloaded with right config.
					ItemScreenCache.getInstance().clearCache();
				}*/
			}
			fieldTypeDT.saveSettings(settings, fieldConfigID);
		}
		if (addAlsoDomainConfig) {
			ConfigItem configItemDirect = TreeConfigBL.getNewConfigItem(treeConfigIDTokens);
			configItemDirect.setConfigRel(fieldID);
			FieldConfigItemFacade.getInstance().overwriteConfig(configItemDirect, fieldConfigBean);
		}
		if (invalidateFields) {
			FieldTypeManager.getInstance().invalidateCache();
		}
		if (invalidateScreens) {
			//invalidate the screens for changed field type (for. ex. select tp extensible select)
			ItemScreenCache.getInstance().clearCache();
		}
		List<String> pathToExpand = new ArrayList<String>();
		if (add) {
			//by add from a not custom field node get the path to the nearest custom field to expand.
			//It can be that none of the nodes is expanded, but in client side refresh algorithm only the already 
			//loaded nodes are reloaded again. After add the new field should be selected, that's why we should prepare the parents' path						
			if (treeConfigIDTokens != null) {
				treeConfigIDTokens.setType(TreeConfigBL.FIELD);
				treeConfigIDTokens.setConfigRelID(TreeConfigBL.CUSTOM_FIELD);
			}
			String nearestCustomFieldNodeID = TreeConfigIDTokens.encodeNode(treeConfigIDTokens);
			if (!(TreeConfigBL.FIELD.equals(addedToType) &&
					TreeConfigBL.CUSTOM_FIELD.equals(addedToConfigRelID))) {
				//new field added to a non-custom field node, get the path to the nearest custom field node
				if (addAlsoDomainConfig) {
					//the first level nodes are not needed in the pathToExpand to be added because 
					//if either project or projectType or issueType is
					//specified then the corresponding first level node is expanded already 
					if (projectID!=null) {
						if (treeConfigIDTokens != null) {
							treeConfigIDTokens.setType(TreeConfigBL.PROJECT);
							treeConfigIDTokens.setConfigRelID(projectID);
						}
						ConfigItem configItem = TreeConfigBL.getNewConfigItem(treeConfigIDTokens);
						pathToExpand.add(TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(TreeConfigBL.FIELD_CONFIG,
							TreeConfigBL.PROJECT, configItem)));
					} else {
						if (projectTypeID!=null) {
							if (treeConfigIDTokens != null) {
								treeConfigIDTokens.setType(TreeConfigBL.PROJECT_TYPE);
								treeConfigIDTokens.setConfigRelID(projectTypeID);
							}
							ConfigItem configItem = TreeConfigBL.getNewConfigItem(treeConfigIDTokens);
							pathToExpand.add(TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(TreeConfigBL.FIELD_CONFIG,
								TreeConfigBL.PROJECT_TYPE, configItem)));
						} else {
							if (issueTypeID!=null) {
								if (treeConfigIDTokens != null) {
									treeConfigIDTokens.setType(TreeConfigBL.ISSUE_TYPE);
									treeConfigIDTokens.setConfigRelID(issueTypeID);
								}
								ConfigItem configItem = TreeConfigBL.getNewConfigItem(treeConfigIDTokens);
								pathToExpand.add(TreeConfigIDTokens.encodeNode(new TreeConfigIDTokens(TreeConfigBL.FIELD_CONFIG,
									TreeConfigBL.ISSUE_TYPE, configItem)));
							}
						}
					}
				} else {
					//field with only global configuration: needed only if Add was clicked when no node was selected
					pathToExpand.add(TreeConfigIDTokens.encodeRootNode(TreeConfigBL.FIELD_CONFIG, TreeConfigBL.FIELD));
				}
			}
			pathToExpand.add(nearestCustomFieldNodeID);
		}
		//refresh the tree only by saving a new field or changing the name of a field 
		JSONUtility.encodeJSON(servletResponse,
				FieldConfigJSON.getSaveJSON(node, pathToExpand, invalidateFields));
	}
	
	/**
	 * Gets the custom field types found as plugins
	 * @param locale
	 * @return
	 */
	private static List<FieldTypeDescriptor> getCustomFieldTypeDescriptors(Locale locale, boolean add) {
		List<FieldTypeDescriptor> fieldTypeDecriptors = FieldTypeDescriptorUtil.getCustomFieldTypeDescriptors();
		if (fieldTypeDecriptors!=null) {
			for (Iterator<FieldTypeDescriptor> iter = fieldTypeDecriptors.iterator(); iter.hasNext();) {
				FieldTypeDescriptor pluginDescriptor = iter.next();
				if (add && "com.aurel.track.fieldType.types.custom.CustomSelectParentChilds".equals(pluginDescriptor.getId())) {
					//remove parent children for new field types
					iter.remove();
				} else {
					pluginDescriptor.setLocalizedLabel(LocalizeUtil.getLocalizedText(pluginDescriptor.getLabel(), 
							locale, pluginDescriptor.getBundleName()));
				}
			}
		}
		return fieldTypeDecriptors;
	}
	
	/**
	 * Gets the compatible custom field type descriptors
	 * @param locale
	 * @return
	 */
	private static List<FieldTypeDescriptor> getCustomFieldTypeDescriptors(Locale locale, Set<String> compatibleFieldTypeIDs) {
		List<FieldTypeDescriptor> compatibleFieldTypeDescriptors = new LinkedList<FieldTypeDescriptor>(); 
		List<FieldTypeDescriptor> fieldTypeDecriptors = FieldTypeDescriptorUtil.getCustomFieldTypeDescriptors();
		if (fieldTypeDecriptors!=null) {
			for (Iterator<FieldTypeDescriptor> iter = fieldTypeDecriptors.iterator(); iter.hasNext();) {
				FieldTypeDescriptor pluginDescriptor = iter.next();
				if (compatibleFieldTypeIDs.contains(pluginDescriptor.getId())) {
					pluginDescriptor.setLocalizedLabel(LocalizeUtil.getLocalizedText(pluginDescriptor.getLabel(), 
							locale, pluginDescriptor.getBundleName()));
					compatibleFieldTypeDescriptors.add(pluginDescriptor);
				} 
			}
		}
		return compatibleFieldTypeDescriptors;
	}
	
	private static void deleteField(Integer fieldID, TreeConfigIDTokens treeConfigIDTokens, HttpServletResponse servletResponse) {
		fieldDAO.delete(fieldID);
		FieldTypeManager.getInstance().invalidateCache();
		ItemScreenCache.getInstance().clearCache();
		//encode the deleted node's parent
		treeConfigIDTokens.setConfigRelID(null);
		treeConfigIDTokens.setType(TreeConfigBL.ISSUE_TYPE);
		JSONUtility.encodeJSON(servletResponse,
					JSONUtility.encodeJSONSuccessAndNode(TreeConfigIDTokens.encodeNode(treeConfigIDTokens)));
	}
	
	/**
	 * Deletes a field with all associated field configurations and field settings
	 * @param node
	 * @param deleteConfirmed
	 * @param person
	 * @param locale
	 * @param servletResponse
	 */
	public static void deleteField(String node, boolean deleteConfirmed, TPersonBean person, 
			Locale locale, HttpServletResponse servletResponse) {
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
		Integer fieldID = treeConfigIDTokens.getConfigRelID();
		if (deleteConfirmed) {
			deleteField(fieldID, treeConfigIDTokens, servletResponse);
		} else {
			boolean deletable = isDeletable(fieldID);
			if (deletable) {
				deleteField(fieldID, treeConfigIDTokens, servletResponse);
			} else {
				String errorMessage;
				if (person.isSys()) {
					errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.detail.confirm.question", locale);
					JSONUtility.encodeJSON(servletResponse,
							JSONUtility.encodeJSONFailure(errorMessage, JSONUtility.DELETE_ERROR_CODES.NOT_EMPTY_WARNING));
				} else {
					if (fieldPresentInOtherProjects(fieldID, person.getObjectID())) {
						errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.detail.err.noDelete.issuesExist", locale);
						JSONUtility.encodeJSON(servletResponse,
								JSONUtility.encodeJSONFailure(errorMessage, JSONUtility.DELETE_ERROR_CODES.NO_RIGHT_TO_DELETE));
					} else {
						errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.field.config.detail.confirm.question", locale);
						JSONUtility.encodeJSON(servletResponse,
							JSONUtility.encodeJSONFailure(errorMessage, JSONUtility.DELETE_ERROR_CODES.NOT_EMPTY_WARNING));
					}
				}
			}
		}
	}
	
	/**
	 * Executes a command for a field
	 * @param node
	 * @param servletResponse
	 */
	public static void executeCommand(String node, HttpServletResponse servletResponse) {
		TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
		Integer fieldID = treeConfigIDTokens.getConfigRelID();
		TFieldBean fieldBean = FieldBL.loadByPrimaryKey(fieldID);
		ConfigItem configItem = TreeNodeConfigUtils.decodeConfigItem(node);
		if (fieldBean!=null && fieldBean.getObjectID()!=null) {
			FieldType fieldType = FieldType.fieldTypeFactory(fieldBean.getFieldType());
			if (fieldType!=null) {
				IFieldTypeDT fieldTypeDT = fieldType.getFieldTypeDT();
				if (fieldTypeDT!=null) {
					fieldTypeDT.executeCommand(configItem);
				}
			}
		}
		JSONUtility.encodeJSON(servletResponse,
				JSONUtility.encodeJSONSuccessAndNode(node));
	}
	
	/**
	 * Whether the field has dependent entities
	 * @param fieldID
	 */
	public static boolean isDeletable(Integer fieldID) {
		return fieldDAO.isDeletable(fieldID);
	}
	
	/**
	 * Whether the field has dependent entities
	 * @param fieldID
     * @param personID
	 */
	public static boolean fieldPresentInOtherProjects(Integer fieldID, Integer personID) {
		List<TProjectBean> projectsWithField = DAOFactory.getFactory().getProjectDAO().getProjectsWithField(fieldID);
		List<TProjectBean> adminProjects = ProjectBL.getAllNotClosedAdminProjectBeansFlat(
				LookupContainer.getPersonBean(personID), false);
		Set<Integer> projectsIDsWithField = GeneralUtils.createIntegerSetFromBeanList(projectsWithField);
		Set<Integer> adminProjectIDs = GeneralUtils.createIntegerSetFromBeanList(adminProjects);
		projectsIDsWithField.removeAll(adminProjectIDs);
		return !projectsIDsWithField.isEmpty();
	}
	
	/**
	 * Gather the different settings after a submit.
	 * The submitted data can populate element(s) of 
	 * optionSettingsList and/or textBoxSettingsList and/or generalSettingsList
	 * The resulting map is a concatenation of the three lists taking the indexes of the elements in the lists as the keys in the map
	 * @return
	 */
	public static Map<Integer, Object> gatherSettings(List<TOptionSettingsBean> optionSettingsList,
			List<TTextBoxSettingsBean> textBoxSettingsList, List<TGeneralSettingsBean> generalSettingsList, 
			List<String> multipleIntegerList) {
		Map<Integer, Object> allSettings = new HashMap<Integer, Object>();
		if (optionSettingsList!=null) {
			for (int i = 0; i < optionSettingsList.size(); i++) {
				if (optionSettingsList.get(i)!=null) {
					TOptionSettingsBean optionSettingsBean = optionSettingsList.get(i);
					allSettings.put(Integer.valueOf(i), optionSettingsBean);
				}
			}
		}
		if (textBoxSettingsList!=null) {
			for (int i = 0; i < textBoxSettingsList.size(); i++) {
				if (textBoxSettingsList.get(i)!=null) {
					TTextBoxSettingsBean textBoxSettingsBean = textBoxSettingsList.get(i);
					allSettings.put(Integer.valueOf(i), textBoxSettingsBean);
				}
			}
		}
		if (generalSettingsList!=null) {
			for (int i = 0; i < generalSettingsList.size(); i++) {
				if (generalSettingsList.get(i)!=null) {
					TGeneralSettingsBean generalSettingsBean = generalSettingsList.get(i);
					generalSettingsBean.setParameterCode(new Integer(i));
					//force objectID to null and new to true in order to save it in the database (otherwise it doesn't get saved)
					//the datasource and/or the automail TGeneralSettingBean has an objectID because it is loaded in the prepareJsp
					//even before save.
					generalSettingsBean.setObjectID(null);
					generalSettingsBean.setNew(true);
					allSettings.put(Integer.valueOf(i), generalSettingsBean);
				}
			}
		}
		if (multipleIntegerList!=null) {
			for (int i = 0; i < multipleIntegerList.size(); i++) {
				if (multipleIntegerList.get(i)!=null) {
					List<TGeneralSettingsBean> generalSettingsBeanList = new LinkedList<TGeneralSettingsBean>();
					String multipleIntegers = multipleIntegerList.get(i);
					Set<Integer> multipleIntegerSet = GeneralUtils.createIntegerSetFromStringSplit(multipleIntegers);
					if (multipleIntegerSet!=null && !multipleIntegerSet.isEmpty()) {
						for (Integer integerValue : multipleIntegerSet) {
							TGeneralSettingsBean generalSettingsBean = new TGeneralSettingsBean();
							generalSettingsBean.setParameterCode(Integer.valueOf(i));
							generalSettingsBean.setIntegerValue(Integer.valueOf(integerValue));
							generalSettingsBeanList.add(generalSettingsBean);
						}
					}
					allSettings.put(Integer.valueOf(i), generalSettingsBeanList);
				}
			}
		}
		return allSettings;
	}
}
