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


package com.aurel.track.exchange.msProject.importer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.exchange.ImportJSON;
import com.aurel.track.exchange.msProject.importer.MsProjectImporterBL.CONFLICT_MAP_ENTRY;
import com.aurel.track.exchange.msProject.importer.MsProjectImporterBL.CONFLICT_ON;
import com.aurel.track.exchange.msProject.importer.MsProjectImporterBL.DELETE_RESOLUTION;
import com.aurel.track.exchange.msProject.importer.MsProjectImporterBL.UNDELETE_RESOLUTION;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.BooleanStringBean;
import com.aurel.track.util.IntegerStringBean;


/**
 * @author Tamas Ruff
 *
 */
public class MsProjectImportJSON {
			
	static interface JSON_FIELDS {
		//upload fields
		static final String ACCEPT = "accept";
		static final String SELECTED_ID = "selectedProjectReleaseID";
        static final String PROJECT_RELEASE_TREE = "projectReleaseTree";
		
		//resource mapping fields
		static final String FILE_NAME = "fileName";
		static final String RESOURCE_MAPPINGS = "resourceMappings";
		static final String RESOURCE_NAME = "resourceName";
		static final String RESOURCE_UID = "resourceUID";
		static final String PERSONID_UID = "personID";
		static final String TRACKPLUS_PERSONS = "trackplusPersons";
		
		static final String ISSUENO_LABEL = "issueNoLabel";
		
		static final String LEAVE_OVERWRITE_LIST = "leaveOverwriteList";
		static final String LEAVE_DELETE_LIST = "leaveDeleteList";
		static final String LEAVE_UNDELETE_LIST = "leaveUndeleteList";
		
		static final String CONFLICTS = "conflicts";
		static final String CONFLICT_MESSAGE = "conflictMessage";
        static final String CONFLICT_TYPE = "conflictType";
		static final String CONFLICT_VALUES = "conflictValues";
		static final String CONFLICT_MAP_NAME = "conflictMapName";
		static final String CONFLICT_RESOLUTION_LIST_NAME = "conflictResolutionListName";

		public static String UID = "uid";
		public static String WORKITEMID = "workItemID";
		public static String ITEMID = "itemID";
		public static String TITLE = "title";
		public static String MS_PROJECT_VALUE = "msProjectValue";
		public static String TRACKPLUS_VALUE = "trackplusValue";
	}
	
	/**
	 * Creates the JSON string for editing/adding/deriving a notification setting
	 * @param accept
	 * @param projectID
	 * @param projectReleaseTree
	 * @return
	 */
	static String getUploadJSON(String accept,
			Integer projectID, String projectReleaseTree) {
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append(JSONUtility.JSON_FIELDS.DATA).append(":{");
		JSONUtility.appendStringValue(sb, JSON_FIELDS.SELECTED_ID, projectID.toString()); //the intern value
		JSONUtility.appendJSONValue(sb, JSON_FIELDS.PROJECT_RELEASE_TREE, projectReleaseTree);
		JSONUtility.appendStringValue(sb, JSON_FIELDS.ACCEPT, accept, true);
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Gets the json string for field mapping
	 * @param trackPlusPersons
	 * @param resourceNameToResourceUIDMap
	 * @param resourceUIDToPersonIDMap
	 * @return
	 */
	static String getResourceMappingJSON(			
			List<IntegerStringBean> trackPlusPersons,
			SortedMap<String, Integer> resourceNameToResourceUIDMap,
			Map<Integer, Integer> resourceUIDToPersonIDMap) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");		
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, true);
		stringBuilder.append(JSONUtility.JSON_FIELDS.DATA).append(":{");	
		JSONUtility.appendIntegerStringBeanList(stringBuilder, JSON_FIELDS.TRACKPLUS_PERSONS, trackPlusPersons);		
		if (resourceNameToResourceUIDMap!=null && !resourceNameToResourceUIDMap.isEmpty()) {
			stringBuilder.append(JSON_FIELDS.RESOURCE_MAPPINGS).append(":[");			
			for (Iterator<Map.Entry<String, Integer>> iterator =
					resourceNameToResourceUIDMap.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<String, Integer>  resourceNameToResourceUIDEntry = iterator.next();
				String resourceName = resourceNameToResourceUIDEntry.getKey();
				Integer resourceUID = resourceNameToResourceUIDEntry.getValue();											
				stringBuilder.append("{");
				JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.RESOURCE_NAME, resourceName);		
				JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.PERSONID_UID, resourceUIDToPersonIDMap.get(resourceUID));
				JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.RESOURCE_UID, resourceUID, true);
				stringBuilder.append("}");
				if (iterator.hasNext()) {
					stringBuilder.append(",");
				}
			}			
			stringBuilder.append("]");
		}
		stringBuilder.append("}");
		stringBuilder.append("}");
		return stringBuilder.toString();
	}		
	
	/**
	 * Gets the json string for invalid value handling
	 * @param conflictsMap
	 * @param projectID
	 * @param issueTypeID
     * @param locale
     * @param disableFinal
	 * @return
	 */
	static String getMsProjectConflictsJSON(
			Map<Integer, Map<Integer, Map<Integer, String>>> conflictsMap, Integer projectID, Integer issueTypeID,
			Locale locale, boolean disableFinal) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{");
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.SUCCESS, false);
		JSONUtility.appendIntegerValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_CODE, ImportJSON.ERROR_CODES.CONFLICTS);
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.ERROR_MESSAGE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.lbl.conflictResolution", locale));
		JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ISSUENO_LABEL,
				FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale));		
		if (conflictsMap.containsKey(CONFLICT_ON.BUDGET) ||
				conflictsMap.containsKey(CONFLICT_ON.STARTDATE) ||
				conflictsMap.containsKey(CONFLICT_ON.ENDDATE)) {
			JSONUtility.appendBooleanStringBeanList(stringBuilder, JSON_FIELDS.LEAVE_OVERWRITE_LIST, getLeaveOverwriteList(locale));
		}
		if (conflictsMap.containsKey(CONFLICT_ON.DELETED)) {
			JSONUtility.appendBooleanStringBeanList(stringBuilder, JSON_FIELDS.LEAVE_DELETE_LIST, getLeaveDeleteList(locale));
		}
		if (conflictsMap.containsKey(CONFLICT_ON.UNDELETED)) {
			JSONUtility.appendBooleanStringBeanList(stringBuilder, JSON_FIELDS.LEAVE_UNDELETE_LIST, getLeaveUndeleteList(locale));
		}
		JSONUtility.appendBooleanValue(stringBuilder, ImportJSON.JSON_FIELDS.DISABLE_FINAL, disableFinal);
		if (conflictsMap!=null && !conflictsMap.isEmpty()) {
			stringBuilder.append(JSON_FIELDS.CONFLICTS).append(":[");
			for (Iterator<Integer> itrConflictType = conflictsMap.keySet().iterator(); itrConflictType.hasNext();) {
				Integer conflictType = itrConflictType.next();
				stringBuilder.append("{");				
				JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.CONFLICT_MESSAGE,
						getMsProjectConflictMessage(conflictType, projectID, issueTypeID, locale));
                JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.CONFLICT_TYPE, conflictType);
				JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.CONFLICT_MAP_NAME,
						getMsProjectConflictMapName(conflictType));
				JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.CONFLICT_RESOLUTION_LIST_NAME,
						getMsProjectConflictResolutionListName(conflictType));				
				Map<Integer, Map<Integer, String>> taskConflicts = conflictsMap.get(conflictType);
				stringBuilder.append(JSON_FIELDS.CONFLICT_VALUES).append(":[");
				for (Iterator<Integer> itrTask = taskConflicts.keySet().iterator(); itrTask.hasNext();) {
					Integer workItemID = itrTask.next();
					Map<Integer, String> values = taskConflicts.get(workItemID);
					stringBuilder.append("{");																									
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.UID, values.get(CONFLICT_MAP_ENTRY.UID));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.MS_PROJECT_VALUE, values.get(CONFLICT_MAP_ENTRY.MS_PROJECT_VALUE));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.TRACKPLUS_VALUE, values.get(CONFLICT_MAP_ENTRY.TRACKPLUS_VALUE));
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.TITLE, values.get(CONFLICT_MAP_ENTRY.SYNOPSIS));
					String itemID = values.get(CONFLICT_MAP_ENTRY.ITEM_ID);
					if(itemID == null) {
						itemID = workItemID.toString();
					}
					JSONUtility.appendStringValue(stringBuilder, JSON_FIELDS.ITEMID, itemID);
					JSONUtility.appendIntegerValue(stringBuilder, JSON_FIELDS.WORKITEMID, workItemID, true);										
					stringBuilder.append("}");
					if (itrTask.hasNext()) {
						stringBuilder.append(",");						
					}					
					
				}
				stringBuilder.append("]");
				stringBuilder.append("}");
				if (itrConflictType.hasNext()) {
					stringBuilder.append(",");
				}				
			}
			stringBuilder.append("]");			
		}			
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Get the conflict message according to the conflict type 
	 * @param conflictType
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @return
	 */
	private static String getMsProjectConflictMessage(Integer conflictType, Integer projectID, Integer issueTypeID, Locale locale) {
		switch (conflictType) {
		case CONFLICT_ON.BUDGET:
			return LocalizeUtil.getParametrizedString("admin.actions.importMSProject.conflict.err.conflictMessage",
					new String[] {LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.plan", locale)}, locale);			
		case CONFLICT_ON.STARTDATE:
			TFieldConfigBean startDateFieldConfigBean = LocalizeUtil.localizeFieldConfig(
        			FieldRuntimeBL.getValidConfig(SystemFields.INTEGER_STARTDATE, issueTypeID, projectID), locale);			
			return LocalizeUtil.getParametrizedString("admin.actions.importMSProject.conflict.err.conflictMessage",
					new String[] {startDateFieldConfigBean.getLabel()}, locale);
		case CONFLICT_ON.ENDDATE:
			TFieldConfigBean endDateFieldConfigBean = LocalizeUtil.localizeFieldConfig(
        			FieldRuntimeBL.getValidConfig(SystemFields.INTEGER_ENDDATE, issueTypeID, projectID), locale);
			return LocalizeUtil.getParametrizedString("admin.actions.importMSProject.conflict.err.conflictMessage",
					new String[] {endDateFieldConfigBean.getLabel()}, locale);		
		case CONFLICT_ON.DELETED:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.err.taskDelete", locale);
		case CONFLICT_ON.UNDELETED:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.err.taskUndelete", locale);		
		}
		return "";
	}
		
	/**
	 * Get the conflict message according to the conflict type 
	 * @param conflictType
	 * @return
	 */
	private static String getMsProjectConflictMapName(Integer conflictType) {
		switch (conflictType) {
		case CONFLICT_ON.BUDGET:
			return "budgetOverwriteMap";					
		case CONFLICT_ON.STARTDATE:
			return "startDateOverwriteMap";
		case CONFLICT_ON.ENDDATE:
			return "endDateOverwriteMap";		
		case CONFLICT_ON.DELETED:
			return "deleteTaskMap";
		case CONFLICT_ON.UNDELETED:
			return "undeleteTaskMap";		
		}
		return "";
	}
	
	/**
	 * Get the conflict message according to the conflict type 
	 * @param conflictType
	 * @return
	 */
	private static String getMsProjectConflictResolutionListName(Integer conflictType) {
		switch (conflictType) {
		case CONFLICT_ON.BUDGET:			
		case CONFLICT_ON.STARTDATE:			
		case CONFLICT_ON.ENDDATE:
			return JSON_FIELDS.LEAVE_OVERWRITE_LIST;		
		case CONFLICT_ON.DELETED:
			return JSON_FIELDS.LEAVE_DELETE_LIST;
		case CONFLICT_ON.UNDELETED:
			return JSON_FIELDS.LEAVE_UNDELETE_LIST;	
		}
		return "";
	}
	
	private static List<BooleanStringBean> getLeaveOverwriteList(Locale locale) {
		List<BooleanStringBean> dateList = new LinkedList<BooleanStringBean>();		
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.opt.conflict.overwrite", locale),
				MsProjectImporterBL.CONFLICT_RESOLUTION.OVERWRITE));
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.opt.conflict.leave", locale),
				MsProjectImporterBL.CONFLICT_RESOLUTION.LEAVE));
		return dateList;    	
    }

	private static List<BooleanStringBean> getLeaveDeleteList(Locale locale) {
		List<BooleanStringBean> dateList = new LinkedList<BooleanStringBean>();	
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.opt.delete.leave", locale),
				DELETE_RESOLUTION.LEAVE));
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.opt.delete.delete", locale),
				DELETE_RESOLUTION.DELETE));
		return dateList;    	
    }
	
	private static List<BooleanStringBean> getLeaveUndeleteList(Locale locale) {
		List<BooleanStringBean> dateList = new LinkedList<BooleanStringBean>();
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.opt.undelete.leave", locale),
				UNDELETE_RESOLUTION.LEAVE));
		dateList.add(new BooleanStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importMSProject.conflict.opt.undelete.undelete", locale),
				UNDELETE_RESOLUTION.UNDELETE));
		return dateList;    	
    }
}
