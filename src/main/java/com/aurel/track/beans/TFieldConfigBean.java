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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.util.EqualUtils;

/**
 * This class manages the configuration of a field (attribute/property).
 */
public class TFieldConfigBean extends com.aurel.track.beans.base.BaseTFieldConfigBean implements Serializable, ConfigItem, ILocalizedLabelBean,
		ISerializableLabelBean, Comparable {

	private static final long serialVersionUID = 1L;
	/*
	 * loadByFieldConfigParameters(TFieldConfigBean cfg) should be able to
	 * search either to system fields or to custom fields
	 */
	private boolean custom;

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	/*
	 * for field config the rel entity is the field
	 * 
	 * @see com.aurel.track.beans.ConfigItem#getConfigRel()
	 */
	public Integer getConfigRel() {
		return getField();
	}

	public void setConfigRel(Integer configRel) {
		setField(configRel);
	}

	public String getKeyPrefix() {
		return LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX;
	}

	/**
	 * Whether the config is a default (global) one
	 * 
	 * @return
	 */
	public boolean isDefault() {
		return getProjectType() == null && getProject() == null && getIssueType() == null;
	}

	public boolean isRequiredString() {
		if (BooleanFields.TRUE_VALUE.equals(getRequired())) {
			return true;
		}
		return false;
	}

	public void setRequiredString(Boolean required) {
		if (required == null || !required.booleanValue()) {
			setRequired(BooleanFields.FALSE_VALUE);
		} else {
			setRequired(BooleanFields.TRUE_VALUE);
		}
	}

	public boolean isHistoryString() {
		if (BooleanFields.TRUE_VALUE.equals(getHistory())) {
			return true;
		}
		return false;
	}

	public void setHistoryString(Boolean history) {
		if (history == null || !history.booleanValue()) {
			setHistory(BooleanFields.FALSE_VALUE);
		} else {
			setHistory(BooleanFields.TRUE_VALUE);
		}
	}

	/**
	 * Serialize a label bean to a dom element
	 * 
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		Integer fieldID = getField();
		if (fieldID != null) {
			attributesMap.put("field", fieldID.toString());
		}
		attributesMap.put("label", getLabel());
		attributesMap.put("tooltip", getTooltip());
		attributesMap.put("required", getRequired());
		attributesMap.put("history", getHistory());
		Integer issueType = getIssueType();
		if (issueType != null) {
			attributesMap.put("issueType", issueType.toString());
		}
		Integer projectType = getProjectType();
		if (projectType != null) {
			attributesMap.put("projectType", projectType.toString());
		}
		Integer project = getProject();
		if (project != null) {
			attributesMap.put("project", project.toString());
		}
		attributesMap.put("description", getDescription());
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}

	/**
	 * Deserialize the labelBean
	 * 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TFieldConfigBean fieldConfigBean = new TFieldConfigBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID != null) {
			fieldConfigBean.setObjectID(new Integer(strObjectID));
		}
		String strFieldID = attributes.get("field");
		if (strFieldID != null) {
			fieldConfigBean.setField(new Integer(strFieldID));
		}
		fieldConfigBean.setLabel(attributes.get("label"));
		fieldConfigBean.setTooltip(attributes.get("tooltip"));
		fieldConfigBean.setRequired(attributes.get("required"));
		fieldConfigBean.setHistory(attributes.get("history"));
		String strIssueType = attributes.get("issueType");
		if (strIssueType != null) {
			fieldConfigBean.setIssueType(Integer.valueOf(strIssueType));
		}
		String strProjectType = attributes.get("projectType");
		if (strProjectType != null) {
			fieldConfigBean.setProjectType(Integer.valueOf(strProjectType));
		}
		String strProject = attributes.get("project");
		if (strProject != null) {
			fieldConfigBean.setProject(Integer.valueOf(strProject));
		}
		fieldConfigBean.setDescription(attributes.get("description"));
		fieldConfigBean.setUuid(attributes.get("uuid"));
		return fieldConfigBean;
	}

	/**
	 * Whether two label beans are equivalent
	 * 
	 * @param serializableLabelBean
	 * @param matchesMap
	 *            key: fieldID_paramaterCode value: map of already mapped
	 *            external vs. internal objectIDs
	 * @return
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean == null) {
			return false;
		}
		TFieldConfigBean fieldConfigBean = (TFieldConfigBean) serializableLabelBean;
		String externalUuid = getUuid();
		String internalUuid = fieldConfigBean.getUuid();
		if (EqualUtils.equalStrict(externalUuid, internalUuid)) {
			return true;
		}
		Integer externalField = getField();
		Integer internalField = fieldConfigBean.getField();
		Integer externalIssueType = getIssueType();
		Integer internalIssueType = fieldConfigBean.getIssueType();
		Integer externalProjectType = getProjectType();
		Integer internalProjectType = fieldConfigBean.getProjectType();
		Integer externalProject = getProject();
		Integer internalProject = fieldConfigBean.getProject();
		Map<Integer, Integer> fieldMatches = matchesMap.get(ExchangeFieldNames.FIELD);
		if (fieldMatches == null || fieldMatches.isEmpty()) {
			return EqualUtils.equal(externalField, internalField) && EqualUtils.equal(externalIssueType, internalIssueType)
					&& EqualUtils.equal(externalProjectType, internalProjectType) && EqualUtils.equal(externalProject, internalProject);
		}
		// a fieldConfig matches if the field matches and the (issueType,
		// projectType, project) context matches)
		if (externalField == null || internalField == null || fieldMatches == null || fieldMatches.get(externalField) == null
				|| !fieldMatches.get(externalField).equals(internalField)) {
			// the field does not match
			return false;
		}
		// whether the (issueType, projectType, project) context matches
		if (externalIssueType != null && internalIssueType != null) {
			Map<Integer, Integer> issueTypeMatches = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUETYPE, null));
			if (!issueTypeMatches.get(externalIssueType).equals(internalIssueType)) {
				return false;
			}
		} else {
			if (externalIssueType != null || internalIssueType != null) {
				return false;
			}
		}
		if (externalProjectType != null && internalProjectType != null) {
			Map<Integer, Integer> projectTypeMatches = matchesMap.get(ExchangeFieldNames.PROJECTTYPE);
			if (!projectTypeMatches.get(externalProjectType).equals(internalProjectType)) {
				return false;
			}
		} else {
			if (externalProjectType != null || internalProjectType != null) {
				return false;
			}
		}
		if (externalProject != null && internalProject != null) {
			Map<Integer, Integer> projectMatches = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			if (!projectMatches.get(externalProject).equals(internalProject)) {
				return false;
			}
		} else {
			if (externalProject != null || internalProject != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Saves a serializableLabelBean into the database
	 * 
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TFieldConfigBean fieldConfigBean = (TFieldConfigBean) serializableLabelBean;
		Integer field = fieldConfigBean.getField();
		if (field != null) {
			Map<Integer, Integer> fieldsMap = matchesMap.get(ExchangeFieldNames.FIELD);
			fieldConfigBean.setField(fieldsMap.get(field));
		}
		Integer issueType = fieldConfigBean.getIssueType();
		if (issueType != null) {
			Map<Integer, Integer> issueTypeMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUETYPE, null));
			fieldConfigBean.setIssueType(issueTypeMap.get(issueType));
		}
		Integer projectType = fieldConfigBean.getProjectType();
		if (projectType != null) {
			Map<Integer, Integer> projectTypeMap = matchesMap.get(ExchangeFieldNames.PROJECTTYPE);
			fieldConfigBean.setProjectType(projectTypeMap.get(projectType));
		}
		Integer project = fieldConfigBean.getProject();
		if (project != null) {
			Map<Integer, Integer> projectMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			fieldConfigBean.setProject(projectMap.get(project));
		}
		return FieldConfigBL.save(fieldConfigBean);
	}

	@Override
	public int compareTo(Object o) {
		TFieldConfigBean fieldConfigBean = (TFieldConfigBean) o;
		String thisLabel = getLabel();
		String compareToLabel = fieldConfigBean.getLabel();
		if (thisLabel == null && compareToLabel == null) {
			return 0;
		}
		if (thisLabel == null) {
			return -1;
		}
		if (compareToLabel == null) {
			return 1;
		}
		return thisLabel.compareTo(compareToLabel);
	}

	@Override
	public boolean equals(Object o) {
		TFieldConfigBean fieldConfigBean = (TFieldConfigBean) o;
		String thisLabel = getLabel();
		String compareToLabel = fieldConfigBean.getLabel();
		if (thisLabel == null && compareToLabel == null) {
			return true;
		}
		if (thisLabel != null && compareToLabel != null) {
			if (thisLabel.equals(compareToLabel)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getObjectID();
	}

}
