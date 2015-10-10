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


package com.aurel.track.exchange.excel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.accessControl.AccessBeans.AccessFlagIndexes;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.lists.systemOption.StatusBL;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.project.release.ReleaseBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.errors.ErrorParameter;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.fieldType.runtime.base.CustomCompositeBaseRT;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.SerializableBeanAllowedContext;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.callbackInterfaces.ILookup;
import com.aurel.track.fieldType.runtime.custom.select.CustomSelectBaseRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.system.select.SystemManagerRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxDate.HIERARCHICAL_BEHAVIOR_OPTIONS;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistorySaverBL;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.item.workflow.execute.StatusWorkflow;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ConsultedInformedLoaderBL;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.emailHandling.Text2HTML;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

public class ExcelImportBL {

	private static final Logger LOGGER = LogManager.getLogger(ExcelImportBL.class);
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();

	static Integer DEFAULT_IF_NOT_EXIST_OR_EMPTY = Integer.valueOf(1);
	static Integer REJECT_IF_NOT_EXIST_OR_EMPTY = Integer.valueOf(2);

	// grid error IDs

	private static final int NOT_EXISTING_ERRORS = 1;
	private static final int NOT_ALLOWED_ERRORS = 2;
	private static final int INVALID_ERRORS = 3;
	private static final int NOT_EDITABLE_ERRORS = 4;
	// workItem identified by issueNo does not exist
	private static final int WORKITEM_NOTEXIST_ERRORS = 5;
	private static final int NOT_ALLOWED_DEFAULT_VALUES_ERRORS = 6;
	// the composite value contains more or less parts as the corresponding
	// field's size
	private static final int WRONG_COMPOSITE_SIZE = 7;
	private static final int INCONSISTENT_HIERARCHY_ERRORS = 8;

	// row error IDs
	private static final int WORKITEM_NO_CREATE_RIGHT = 1;
	private static final int WORKITEM_NO_EDIT_RIGHT = 2;
	private static final int WORKITEM_MORE_THAN_ONE_EXIST = 3;

	// required fields

	private static String SPLITTER = "s";

	public interface IConflictMapEntry {
		public static Integer WORKITEMID = Integer.valueOf(1);
		public static Integer COLUMN_LETTER = Integer.valueOf(2);
		public static Integer FELED_NAME = Integer.valueOf(3);
		public static Integer EXCEL_VALUE = Integer.valueOf(4);
		public static Integer TRACKPLUS_VALUE = Integer.valueOf(5);
		public static Integer WORKITEMID_FIELDID = Integer.valueOf(6);
	}

	public interface IConflictResolution {
		static public Boolean OVERWRITE = Boolean.valueOf(true);
		static public Boolean LEAVE = Boolean.valueOf(false);
	}

	/**
	 * Fields to update after project change/issue change fields
	 * 
	 * @return
	 */
	static List<Integer> getFieldsToUpdate() {
		List<Integer> requiredFields = new ArrayList<Integer>();
		requiredFields.add(SystemFields.INTEGER_ISSUETYPE);
		requiredFields.add(SystemFields.INTEGER_STATE);
		requiredFields.add(SystemFields.INTEGER_MANAGER);
		requiredFields.add(SystemFields.INTEGER_RESPONSIBLE);
		requiredFields.add(SystemFields.INTEGER_PRIORITY);
		return requiredFields;
	}

	/**
	 * Required fields
	 * 
	 * @return
	 */
	static List<Integer> getRequiredFields() {
		List<Integer> requiredFields = new ArrayList<Integer>();
		requiredFields.add(SystemFields.INTEGER_PROJECT);
		requiredFields.addAll(getFieldsToUpdate());
		return requiredFields;
	}

	/**
	 * Field labels for required fields
	 * 
	 * @param locale
	 * @return
	 */
	static List<IntegerStringBean> prepareFieldLabels(Locale locale) {
		List<Integer> fieldIDList = getRequiredFields();
		List<TFieldConfigBean> fieldConfigBeans = LocalizeUtil.localizeFieldConfigs(FieldConfigBL.loadDefaultForFields(fieldIDList), locale);
		List<IntegerStringBean> fieldBeans = new LinkedList<IntegerStringBean>();
		for (TFieldConfigBean fieldConfigBean : fieldConfigBeans) {
			fieldBeans.add(new IntegerStringBean(fieldConfigBean.getLabel(), fieldConfigBean.getField()));
		}
		return fieldBeans;
	}

	/**
	 * Initialize the default values selected by the first rendering. Defaults
	 * to reject if not exist or empty
	 * 
	 * @return
	 */
	static Map<Integer, Integer> initDefaultValuesSelected() {
		Map<Integer, Integer> defaultValuesSelected = new HashMap<Integer, Integer>();
		List<Integer> fieldIDList = getRequiredFields();
		Iterator<Integer> iterator = fieldIDList.iterator();
		while (iterator.hasNext()) {
			Integer fieldID = iterator.next();
			defaultValuesSelected.put(fieldID, REJECT_IF_NOT_EXIST_OR_EMPTY);
		}
		return defaultValuesSelected;
	}

	/**
	 * The list for radio buttons
	 * 
	 * @param locale
	 * @return
	 */
	public static List<IntegerStringBean> getInvalidValueHandlingList(Locale locale) {
		List<IntegerStringBean> defaultSelectedList = new ArrayList<IntegerStringBean>();
		defaultSelectedList.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.actions.importExcel.invalidValueHadling.opt.reject", locale), REJECT_IF_NOT_EXIST_OR_EMPTY));
		defaultSelectedList.add(new IntegerStringBean(LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.actions.importExcel.invalidValueHadling.opt.default", locale), DEFAULT_IF_NOT_EXIST_OR_EMPTY));
		return defaultSelectedList;
	}

	/**
	 * Get the possible values list
	 * 
	 * @param personID
	 * @param projectID
	 * @param issueTypeID
	 * @param locale
	 * @return
	 */
	static Map<Integer, List<ILabelBean>> getPossibleValues(Integer personID, Integer projectID, Integer issueTypeID, Locale locale) {
		Map<Integer, List<ILabelBean>> possibleValuesMap = new HashMap<Integer, List<ILabelBean>>();
		List<TProjectBean> projectsList = ProjectBL.loadProjectsFlatByRight(personID, new int[] { AccessBeans.AccessFlagIndexes.MODIFYANYTASK,
				AccessBeans.AccessFlagIndexes.CREATETASK }, true);
		Integer[] possibleProjects = GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectsList));
		// issue types with create right for the actual user and project
		List<TListTypeBean> issueTypeList;
		if (projectID == null) {
			issueTypeList = IssueTypeBL.loadAllSelectable();
		} else {
			issueTypeList = IssueTypeBL.loadByPersonAndProjectAndRight(personID, projectID, new int[] { AccessBeans.AccessFlagIndexes.CREATETASK,
					AccessBeans.AccessFlagIndexes.MODIFYANYTASK, AccessBeans.AccessFlagIndexes.PROJECTADMIN });
		}
		possibleValuesMap.put(SystemFields.INTEGER_ISSUETYPE, LocalizeUtil.localizeDropDownList(issueTypeList, locale));

		// initial states for project and issue type
		List<TStateBean> stateList = null;
		if (projectID == null) {
			stateList = StatusBL.loadActiveStates();
		} else {
			// issueType might be null: in this case the project specific
			// initial state will be taken
			stateList = StatusWorkflow.loadExcelImportStatuses(projectID, issueTypeID);
		}
		possibleValuesMap.put(SystemFields.INTEGER_STATE, LocalizeUtil.localizeDropDownList(stateList, locale));

		// managers for project and issue type
		List managerList = null;
		if (projectID == null) {
			// manager in any of the project the user has edit/create right
			int[] arrRights = new int[] { AccessFlagIndexes.MANAGER, AccessFlagIndexes.PROJECTADMIN };
			Set<Integer> managersByRoleSet = AccessBeans.getPersonSetByProjectsRights(possibleProjects, arrRights);
			managerList = PersonBL.getDirectAndIndirectPersons(GeneralUtils.createIntegerListFromCollection(managersByRoleSet), true, true, null);
		} else {
			// issue type may be null: in this case the users with manager
			// role in the project will be taken, independently of issue type
			managerList = PersonBL.loadManagersByProjectAndIssueType(projectID, issueTypeID);
		}
		possibleValuesMap.put(SystemFields.INTEGER_MANAGER, managerList);

		// responsibles for project and issue type
		List responsibleList = null;
		if (projectID == null) {
			// responsible in any of the project the user has edit/create right
			int[] arrRights = new int[] { AccessFlagIndexes.RESPONSIBLE, AccessFlagIndexes.PROJECTADMIN };
			Set<Integer> responsiblesByRoleSet = AccessBeans.getPersonSetByProjectsRights(possibleProjects, arrRights);
			responsibleList = PersonBL.getDirectAndIndirectPersonsAndGroups(GeneralUtils.createIntegerListFromCollection(responsiblesByRoleSet), true, true,
					null);
		} else {
			// issue type may be null: in this case the users with responsible
			// role in the project will be taken, independently of issue type
			responsibleList = PersonBL.loadResponsiblesByProjectAndIssueType(projectID, issueTypeID);
		}
		possibleValuesMap.put(SystemFields.INTEGER_RESPONSIBLE, responsibleList);

		// priorities for project (project type)/issue type
		List priorityList = null;
		if (projectID != null && issueTypeID != null) {
			priorityList = PriorityBL.loadByProjectAndIssueType(projectID, issueTypeID, null);
		} else {
			if (projectID != null) {
				TProjectBean projectBean = LookupContainer.getProjectBean(projectID);
				if (projectBean != null) {
					priorityList = PriorityBL.loadAllowedByProjectTypesAndIssueTypes(new Integer[] { projectBean.getProjectType() }, null);
				}
			} else {
				priorityList = PriorityBL.loadAll();
			}
		}
		possibleValuesMap.put(SystemFields.INTEGER_PRIORITY, LocalizeUtil.localizeDropDownList(priorityList, locale));
		return possibleValuesMap;
	}

	/**
	 * Get the mapped fields
	 * 
	 * @param columNameToFieldIDMap
	 * @return
	 */
	static Set<Integer> getPresentFields(Map<String, Integer> columNameToFieldIDMap) {
		Set<Integer> presentFields = new HashSet<Integer>();
		for (Integer fieldID : columNameToFieldIDMap.values()) {
			if (fieldID != null) {
				// add the field only if a mapping is selected
				// negative fieldIDs (pseudo fields) should not be added
				if (fieldID.intValue() > 0) {
					presentFields.add(fieldID);
				}
			}
		}
		return presentFields;
	}

	/**
	 * A first level validate for required columns if no identifier columns are
	 * set (means create always a new workItem) or 
	 * not all identifierFieldIDIsSet values are set (means for at least one row create a new workItem)
	 * @param workbook
	 * @param selectedSheet
	 * @param fieldIDToColumnIndexMap
	 * @param identifierFieldIDIsSet
	 * @param invalidValueHandlingMap
	 * @param defaultValuesMap
	 * @param locale
	 * @return
	 */
	static List<ErrorData> validateRequiredColumns(Workbook workbook, Integer selectedSheet, Map<Integer, Integer> fieldIDToColumnIndexMap,
			Set<Integer> identifierFieldIDIsSet, Map<Integer, Integer> invalidValueHandlingMap, Map<Integer, Integer> defaultValuesMap, Locale locale) {
		List<ErrorData> errorData = new LinkedList<ErrorData>();
		if (identifierFieldIDIsSet != null && !identifierFieldIDIsSet.isEmpty()) {
			// we could return the empty arrorData right now and
			// postpone the required validation at the row level
			boolean anySpecified = false;
			for (Integer fieldID : identifierFieldIDIsSet) {
				if (anyFieldCellsSpecified(workbook, selectedSheet, fieldIDToColumnIndexMap.get(fieldID))) {
					anySpecified = true;
					break;
				}
			}
			// do not verify the required columns if all the
			// identifierFieldIDIsSet columns are
			// present because by update there are no required fields.
			// if there are also new issue rows (for which by
			// identifierFieldIDIsSet
			// no existing workItem can be found) and not all required columns
			// are present it will be validated later anyway separately for each
			// row
			if (anySpecified) {
				// at least one existing item was found: postpone the required
				// validation at the row level
				return errorData;
			}
		}
		for (Map.Entry<Integer, Integer> entry : invalidValueHandlingMap.entrySet()) {
			Integer fieldID = entry.getKey();
			Integer handlingValue = entry.getValue();
			if (!fieldIDToColumnIndexMap.containsKey(fieldID) && // a required column is not present in the excel sheet
					(REJECT_IF_NOT_EXIST_OR_EMPTY.equals(handlingValue)
					 || (DEFAULT_IF_NOT_EXIST_OR_EMPTY.equals(handlingValue) && defaultValuesMap.get(fieldID) == null))) {
				errorData.add(new ErrorData("admin.actions.importExcel.err.columnNotExist", FieldRuntimeBL.getDefaultFieldConfig(fieldID, locale).getLabel()));
			}

		}
		// the synopsis is not exposed for default values but it is also
		// required
		if (!fieldIDToColumnIndexMap.containsKey(SystemFields.INTEGER_SYNOPSIS)) {
			errorData.add(new ErrorData("admin.actions.importExcel.err.columnNotExist", FieldRuntimeBL.getDefaultFieldConfig(SystemFields.INTEGER_SYNOPSIS,
					locale).getLabel()));
		}
		return errorData;
	}

	/**
	 * Whether an uniqueIdentifierField is specified for any row If not the
	 * corresponding row will be considered new and then we should verify
	 * whether all required fields are present
	 * 
	 * @param workbook
	 * @param selectedSheet
	 * @param columnIndex
	 * @return
	 */
	private static boolean anyFieldCellsSpecified(Workbook workbook, Integer selectedSheet, Integer columnIndex) {
		Sheet sheet = workbook.getSheetAt(selectedSheet.intValue());
		for (Row row : sheet) {
			int rowNum = row.getRowNum();
			if (rowNum == 0) {
				// only the data rows are processed (the header row is not
				// important now)
				continue;
			}
			// not very rigorous but here suffice
			Object attribute = getStringCellValue(row.getCell(columnIndex));
			if (attribute != null && !"".equals(attribute)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Loads the custom report specific selects in the DropDownContainer
	 * 
	 * @param person
	 * @param locale
	 * @return
	 */
	public static Map<Integer, Map<String, ILabelBean>> loadBaseLookups(Integer person, Locale locale) {
		Map<Integer, Map<String, ILabelBean>> labelBasedBeans = new HashMap<Integer, Map<String, ILabelBean>>();
		labelBasedBeans.put(SystemFields.INTEGER_ISSUETYPE, createLabelBasedMapFromList(IssueTypeBL.loadAll(locale), false));
		labelBasedBeans.put(SystemFields.INTEGER_STATE, createLabelBasedMapFromList(StatusBL.loadAll(locale), false));
		labelBasedBeans.put(SystemFields.INTEGER_PRIORITY, createLabelBasedMapFromList(PriorityBL.loadAll(locale), false));
		labelBasedBeans.put(SystemFields.INTEGER_SEVERITY, createLabelBasedMapFromList(SeverityBL.loadAll(locale), false));
		labelBasedBeans.put(SystemFields.INTEGER_PERSON, createLabelBasedMapFromList((List) PersonBL.loadPersonsAndGroups(), true));
		List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(person);
		labelBasedBeans.put(SystemFields.INTEGER_PROJECT, createLabelBasedMapFromList((List) projectBeans, false));
		return labelBasedBeans;
	}

	/**
	 * Creates a map of beans from a list of beans which implement the IBeanID
	 * key: the result of getObjectID() value: the bean
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, ILabelBean> createLabelBasedMapFromList(List<ILabelBean> list, boolean person) {
		Map<String, ILabelBean> map = new HashMap<String, ILabelBean>();
		if (list!=null) {
			for (ILabelBean labelBean : list) {
				String label = null;
				if (person) {
					label = ((TPersonBean) labelBean).getName();// not add the admin/disabled etc. extension characters
				} else {
					label = labelBean.getLabel();
				}
				if (label != null) {
					map.put(label, labelBean);
				}
			}
		}
		return map;
	}

	/**
	 * Loads the project specific lookups
	 * 
	 * @param projectIDs
	 * @return
	 */
	public static Map<Integer, Map<Integer, Map<String, ILabelBean>>> loadProjectLookups(List<Integer> projectIDs) {
		Map<Integer, Map<Integer, Map<String, ILabelBean>>> labelBasedBeans = new HashMap<Integer, Map<Integer, Map<String, ILabelBean>>>();
		List<TReleaseBean> releases = ReleaseBL.loadAllByProjects(projectIDs);
		for (Iterator<TReleaseBean> iterator = releases.iterator(); iterator.hasNext();) {
			TReleaseBean releaseBean = iterator.next();
			String label = releaseBean.getLabel();
			Integer projectID = releaseBean.getProjectID();
			Map<Integer, Map<String, ILabelBean>> projectLookups = labelBasedBeans.get(projectID);
			if (projectLookups == null) {
				projectLookups = new HashMap<Integer, Map<String, ILabelBean>>();
				labelBasedBeans.put(projectID, projectLookups);
			}
			Map<String, ILabelBean> releaseLookups = projectLookups.get(SystemFields.INTEGER_RELEASE);
			if (releaseLookups == null) {
				releaseLookups = new HashMap<String, ILabelBean>();
				projectLookups.put(SystemFields.INTEGER_RELEASE, releaseLookups);
			}
			releaseLookups.put(label, releaseBean);
		}
		return labelBasedBeans;
	}

	/**
	 * Get the workItems list and validate if all the fields of the excel sheet
	 * are correct
	 * 
	 * @param workbook
	 * @param selectedSheet
	 * @param personID
	 * @param locale
	 * @param columnIndexToFieldIDMap
	 * @param fieldIDToColumnIndexMap
	 * @param lastSavedIdentifierFieldIDIsSet
	 * @param defaultValuesMap
	 * @param invalidValueHandlingMap
	 * @param gridErrorsMap
	 * @param rowErrorsMap
	 * @return
	 */
	static SortedMap<Integer, TWorkItemBean> getAndValidateGridData(Workbook workbook, Integer selectedSheet, Integer personID, Locale locale,
			Map<Integer, Integer> columnIndexToFieldIDMap, Map<Integer, Integer> fieldIDToColumnIndexMap, Set<Integer> lastSavedIdentifierFieldIDIsSet,
			Map<Integer, Integer> defaultValuesMap, Map<Integer, Integer> invalidValueHandlingMap,
			Map<Integer, Map<Integer, List<Integer>>> rowNoToPseudoFieldsOriginal, Map<Integer, Map<Integer, List<Integer>>> rowNoToPseudoFieldsExcel,
			Map<Integer, SortedMap<Integer, SortedMap<String, ErrorData>>> gridErrorsMap, Map<Integer, SortedSet<Integer>> rowErrorsMap,
			Map<Integer, SortedSet<Integer>> requiredFieldErrorsMap, Map<Integer, Integer> rowToParentRow) {
		SortedMap<Integer, TWorkItemBean> workItemBeansMap = new TreeMap<Integer, TWorkItemBean>();
		Sheet sheet = workbook.getSheetAt(selectedSheet.intValue());
		// get the column indexes for project and issueType
		Integer projectColumn = fieldIDToColumnIndexMap.get(SystemFields.INTEGER_PROJECT);
		Integer issueTypeColumn = fieldIDToColumnIndexMap.get(SystemFields.INTEGER_ISSUETYPE);
		// Maps to spare additional database accesses for default values
		Map<Integer, String> defaultShowValuesMap = new HashMap<Integer, String>();
		Map<Integer, String> defaultLocalizedFieldLabels = new HashMap<Integer, String>();
		Integer originalProject = null;
		Integer originalIssueType = null;
		Set<Integer> mandatoryIdentifierFields = ExcelFieldMatchBL.getMandatoryIdentifierFields();
		Map<Integer, Map<String, ILabelBean>> systemLookups = loadBaseLookups(personID, locale);
		Map<String, ILabelBean> projectLookups = systemLookups.get(SystemFields.INTEGER_PROJECT);
		Map<Integer, Map<Integer, Map<String, ILabelBean>>> projectSpecificLookups = null;
		if (projectLookups != null) {
			projectSpecificLookups = loadProjectLookups(GeneralUtils.createIntegerListFromBeanList(GeneralUtils.createListFromCollection(projectLookups
					.values())));
		}
		boolean projectSpecificIDsActive = ApplicationBean.getApplicationBean().getSiteBean().getProjectSpecificIDsOn();
		Map<Integer, TProjectBean> projectBeansMap = new HashMap<Integer, TProjectBean>();
		if (projectSpecificIDsActive) {
			List<TProjectBean> projectBeans = ProjectBL.loadUsedProjectsFlat(personID);
			if (projectBeans != null) {
				for (TProjectBean projectBean : projectBeans) {
					Integer projectID = projectBean.getObjectID();
					projectBeansMap.put(projectID, projectBean);
					String label = projectBean.getLabel();
					String projectPrefix = projectBean.getPrefix();
					if (projectPrefix == null || "".equals(projectPrefix)) {
						LOGGER.info("The project " + label + " with ID " + projectID
								+ " has no prefix, consquently project specific item numbers might not be recognized");
					}
				}
			}
		}
		/**
		 * Process the rows only to gather the projects to issueTypes to get the
		 * roles and restrictions once for all issues
		 */
		Map<Integer, Set<Integer>> projectToIssueTypesMap = new HashMap<Integer, Set<Integer>>();
		for (Row row : sheet) {
			int rowNum = row.getRowNum();
			if (rowNum == 0) {
				// only the data rows are processed (the header row is not
				// important now)
				continue;
			}
			SerializableBeanAllowedContext serializableBeanAllowedContext = new SerializableBeanAllowedContext();
			serializableBeanAllowedContext.setPersonID(personID);
			serializableBeanAllowedContext.setNew(true);
			// get the project and issueType first because the other fields
			// could depend on these issueTypes
			// process the project column
			Integer projectID = null;
			if (projectColumn != null) {
				try {
					projectID = (Integer) getAttributeValue(row.getCell(projectColumn), SystemFields.INTEGER_PROJECT, null, serializableBeanAllowedContext,
							locale, invalidValueHandlingMap, systemLookups, projectSpecificLookups);
				} catch (Exception e) {
				}
			}
			if (projectID == null) {
				// no project column exists on the sheet: take the default value
				// which is
				// surely specified, otherwise it would fail at
				// validateRequiredColumns()
				projectID = defaultValuesMap.get(SystemFields.INTEGER_PROJECT);
			}
			if (projectID != null) {
				serializableBeanAllowedContext.setProjectID(projectID);
			}
			// process the issueType column
			Integer issueTypeID = null;
			if (issueTypeColumn != null) {
				try {
					issueTypeID = (Integer) getAttributeValue(row.getCell(issueTypeColumn), SystemFields.INTEGER_ISSUETYPE, null,
							serializableBeanAllowedContext, locale, invalidValueHandlingMap, systemLookups, projectSpecificLookups);
				} catch (Exception e) {
				}
			}
			if (issueTypeID == null) {
				// no issue type column exists on the sheet: take the default
				// value which is
				// surely specified, otherwise it would fail at
				// validateRequiredColumns()
				issueTypeID = defaultValuesMap.get(SystemFields.INTEGER_ISSUETYPE);
			}
			if (projectID != null) {
				Set<Integer> issueTypes = projectToIssueTypesMap.get(projectID);
				if (issueTypes == null) {
					issueTypes = new HashSet<Integer>();
					projectToIssueTypesMap.put(projectID, issueTypes);
				}
				if (issueTypeID != null) {
					issueTypes.add(issueTypeID);
				}
			}
		}
		Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsToIssueTypesToFieldConfigsMapForBottomUpFields = null;
		Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMapForBottomUpFields = null;
		Map<Integer, Map<Integer, Map<Integer, Integer>>> fieldRestrictions = AccessBeans.getFieldRestrictions(personID, projectToIssueTypesMap, null, true);
		Set<Integer> possibleBottomUpFields = FieldRuntimeBL.getPossibleBottomUpFields();
		for (Iterator<Integer> iterator = possibleBottomUpFields.iterator(); iterator.hasNext();) {
			if (!fieldIDToColumnIndexMap.containsKey(iterator.next())) {
				// remove possible bottom up field if not mapped
				iterator.remove();
			}
			if (!possibleBottomUpFields.isEmpty()) {
				// at least one bottom up date was mapped
				projectsToIssueTypesToFieldConfigsMapForBottomUpFields = FieldRuntimeBL.loadFieldConfigsInContextsAndTargetProjectAndIssueType(
						projectToIssueTypesMap, possibleBottomUpFields, locale, null, null);
				projectsIssueTypesFieldSettingsMapForBottomUpFields = FieldRuntimeBL
						.getFieldSettingsForFieldConfigs(projectsToIssueTypesToFieldConfigsMapForBottomUpFields);
			}
		}

		/**
		 * now process the rows in detail one by one
		 */
		Stack<Integer> parentStack = new Stack<Integer>();
		Map<Integer, Integer> rowToIndent = new HashMap<Integer, Integer>();
		for (Row row : sheet) {
			int rowNum = row.getRowNum();
			if (rowNum == 0) {
				// only the data rows are processed (the header row is not
				// important now)
				continue;
			}
			boolean excelValueFound = false;
			// whether the project column is mapped and excel value if found for
			// project
			boolean mappedProject = false;
			SerializableBeanAllowedContext serializableBeanAllowedContext = new SerializableBeanAllowedContext();
			serializableBeanAllowedContext.setPersonID(personID);
			serializableBeanAllowedContext.setNew(true);
			// get the project and issueType first because the other fields
			// could depend on these issueTypes
			// process the project column
			Integer projectID = null;
			if (projectColumn != null) {
				try {
					projectID = (Integer) getAttributeValue(row.getCell(projectColumn), SystemFields.INTEGER_PROJECT, null, serializableBeanAllowedContext,
							locale, invalidValueHandlingMap, systemLookups, projectSpecificLookups);
					if (projectID != null) {
						mappedProject = true;
						excelValueFound = true;
					}
				} catch (ExcelImportNotExistingCellValueException e) {
					addGridError(gridErrorsMap, NOT_EXISTING_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(projectColumn), SystemFields.INTEGER_PROJECT,
							e.getMessage());
				} catch (ExcelImportNotAllowedCellValueException e) {
					addGridError(gridErrorsMap, NOT_ALLOWED_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(projectColumn), SystemFields.INTEGER_PROJECT,
							e.getMessage());
				} catch (ExcelImportInvalidCellValueException e) {
					addGridError(gridErrorsMap, INVALID_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(projectColumn), SystemFields.INTEGER_PROJECT,
							e.getMessage());
				}
			}
			if (projectID == null) {
				// no project column exists on the sheet: take the default value
				// which is
				// surely specified, otherwise it would fail at
				// validateRequiredColumns()
				projectID = defaultValuesMap.get(SystemFields.INTEGER_PROJECT);
			}
			if (projectID != null) {
				serializableBeanAllowedContext.setProjectID(projectID);
			}
			// process the issueType column
			Integer issueTypeID = null;
			if (issueTypeColumn != null) {
				try {
					issueTypeID = (Integer) getAttributeValue(row.getCell(issueTypeColumn), SystemFields.INTEGER_ISSUETYPE, null,
							serializableBeanAllowedContext, locale, invalidValueHandlingMap, systemLookups, projectSpecificLookups);
					if (issueTypeID != null) {
						excelValueFound = true;
					}
				} catch (ExcelImportNotExistingCellValueException e) {
					addGridError(gridErrorsMap, NOT_EXISTING_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(issueTypeColumn),
							SystemFields.INTEGER_ISSUETYPE, e.getMessage());
				} catch (ExcelImportNotAllowedCellValueException e) {
					addGridError(gridErrorsMap, NOT_ALLOWED_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(issueTypeColumn),
							SystemFields.INTEGER_ISSUETYPE, e.getMessage());
				} catch (ExcelImportInvalidCellValueException e) {
					addGridError(gridErrorsMap, INVALID_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(issueTypeColumn), SystemFields.INTEGER_ISSUETYPE,
							e.getMessage());
				}
			}
			if (issueTypeID == null) {
				// no issue type column exists on the sheet: take the default
				// value which is
				// surely specified, otherwise it would fail at
				// validateRequiredColumns()
				issueTypeID = defaultValuesMap.get(SystemFields.INTEGER_ISSUETYPE);
			}
			if (issueTypeID != null) {
				serializableBeanAllowedContext.setIssueTypeID(issueTypeID);
			}
			/*
			 * gather the values for the identifier fields and try to get an
			 * existing workItem by these fields
			 */
			Map<Integer, Object> identifierFieldValues = new HashMap<Integer, Object>();
			if (lastSavedIdentifierFieldIDIsSet != null && !lastSavedIdentifierFieldIDIsSet.isEmpty()) {
				for (Integer fieldID : lastSavedIdentifierFieldIDIsSet) {
					Integer attributeFieldID = fieldID;
					if (SystemFields.INTEGER_ISSUENO.equals(fieldID) && projectSpecificIDsActive) {
						attributeFieldID = SystemFields.INTEGER_PROJECT_SPECIFIC_ISSUENO;
					}
					Object attributeValue = null;
					Integer columnIndex = null;
					try {
						columnIndex = fieldIDToColumnIndexMap.get(fieldID);
						attributeValue = getAttributeValue(row.getCell(columnIndex), attributeFieldID, null, serializableBeanAllowedContext, locale,
								invalidValueHandlingMap, systemLookups, projectSpecificLookups);
						if (attributeValue != null) {
							identifierFieldValues.put(fieldID, attributeValue);
							excelValueFound = true;
						}
					} catch (ExcelImportNotExistingCellValueException e) {
						if (!SystemFields.INTEGER_PROJECT.equals(fieldID) && !SystemFields.INTEGER_ISSUETYPE.equals(fieldID)) {
							// if project or issueType are set as identifier
							// fields and
							// have grid error they should be already collected
							// in gridErrorsMap
							addGridError(gridErrorsMap, NOT_EXISTING_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, e.getMessage());
						}
					} catch (ExcelImportNotAllowedCellValueException e) {
						if (!SystemFields.INTEGER_PROJECT.equals(fieldID) && !SystemFields.INTEGER_ISSUETYPE.equals(fieldID)) {
							// if project or issueType are set as identifier
							// fields and
							// have grid error they should be already collected
							// in gridErrorsMap
							addGridError(gridErrorsMap, NOT_ALLOWED_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, e.getMessage());
						}
					} catch (ExcelImportInvalidCellValueException e) {
						if (!SystemFields.INTEGER_PROJECT.equals(fieldID) && !SystemFields.INTEGER_ISSUETYPE.equals(fieldID)) {
							// if project or issueType are set as identifier
							// fields and
							// have grid error they should be already collected
							// in gridErrorsMap
							addGridError(gridErrorsMap, INVALID_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, e.getMessage());
						}
					}
				}
			}
			// always initialize the next workItem to null
			TWorkItemBean workItemBean = null;
			boolean itemIsNew = false;
			if (!identifierFieldValues.isEmpty()) {
				if (identifierFieldValues.get(SystemFields.INTEGER_ISSUENO) != null) {
					// is issueNo field mapped?
					if (projectSpecificIDsActive) {
						// get by project specific itemID
						String projectSpecificID = null;
						try {
							projectSpecificID = (String) identifierFieldValues.get(SystemFields.INTEGER_ISSUENO);
						} catch (Exception e) {
						}
						if (projectSpecificID != null) {
							// it should be trimmed because in excel the child
							// issues are indented
							workItemBean = ItemBL.loadWorkItemByProjectSpecificID(projectID, mappedProject, projectBeansMap, projectSpecificID.trim());
							if (workItemBean != null && LOGGER.isDebugEnabled()) {
								LOGGER.debug("WorkItem " + projectSpecificID + " from row " + rowNum + " found by projectSpecificID");
							}
						}
					} else {
						// get by "global" workItemID
						Integer workItemID = null;
						try {
							workItemID = (Integer) identifierFieldValues.get(SystemFields.INTEGER_ISSUENO);
						} catch (Exception e) {
						}
						if (workItemID != null) {
							workItemBean = ItemBL.loadWorkItemSystemAttributes(workItemID);
						}
						if (workItemBean != null && LOGGER.isDebugEnabled()) {
							LOGGER.debug("WorkItem " + workItemID + " from row " + rowNum + " found by workItemID");
						}
					}
					if (workItemBean == null) {
						// the issueNo field is set as identifier and the
						// corresponding issue does't exist, report as error
						addGridError(gridErrorsMap, WORKITEM_NOTEXIST_ERRORS, rowNum,
								ExcelFieldMatchBL.colNumericToLetter(fieldIDToColumnIndexMap.get(SystemFields.INTEGER_ISSUENO)), SystemFields.INTEGER_ISSUENO,
								identifierFieldValues.get(SystemFields.INTEGER_ISSUENO).toString());
						continue;
					}
				}
				if (workItemBean == null) {
					// workItem was not found by issueNo
					// (issueNo field was not mapped or issueNo value is missing
					// from excel or
					// the issue's project is not accessible if
					// projectSpecificIDsActive)
					// try with user defined identifier fields
					try {
						workItemBean = ItemBL.loadWorkItemSystemAttributes(identifierFieldValues);
					} catch (ExcelImportNotUniqueIdentifiersException e) {
						addRowError(rowErrorsMap, WORKITEM_MORE_THAN_ONE_EXIST, rowNum);
						continue;
					}
					if (workItemBean != null && LOGGER.isDebugEnabled()) {
						LOGGER.debug("WorkItem from row " + rowNum + " found by user defined identifier fields");
					}
				}
				if (workItemBean != null) {
					// existing workItem
					originalProject = workItemBean.getProjectID();
					originalIssueType = workItemBean.getListTypeID();
					// is it editable by the current person?
					if (!AccessBeans.isAllowedToChange(workItemBean, personID)) {
						addRowError(rowErrorsMap, WORKITEM_NO_EDIT_RIGHT, rowNum);
						continue;
					}
					// load also the custom attributes because when the workItem
					// will be updated
					// the custom attributes will also be compared to the
					// original value
					ItemBL.loadWorkItemCustomAttributes(workItemBean);
					serializableBeanAllowedContext.setWorkItemBeanOriginal(workItemBean);
					serializableBeanAllowedContext.setNew(false);
					// LOGGER.debug("WorkItem " + workItemBean.getObjectID() +
					// " from row " + rowNum + " found");
				}
			}
			boolean missingRequiredFound = false;
			if (workItemBean == null) {
				// not existing found by identifier fields, create a new one
				workItemBean = new TWorkItemBean();
				if (identifierFieldValues != null) {
					// preset the new workItem with the processed identifier
					// values
					for (Map.Entry<Integer, Object> identifierEntry : identifierFieldValues.entrySet()) {
						workItemBean.setAttribute(identifierEntry.getKey(), identifierEntry.getValue());
					}
				}
				itemIsNew = true;
				LOGGER.debug("WorkItem from row " + rowNum + " not found. A new one will be created.");
			}
			if (projectID != null) {
				workItemBean.setAttribute(SystemFields.INTEGER_PROJECT, null, projectID);
			} else {
				if (itemIsNew) {
					// project column not mapped
					addRowError(requiredFieldErrorsMap, SystemFields.INTEGER_PROJECT, rowNum);
					missingRequiredFound = true;
				}
			}
			if (issueTypeID != null) {
				workItemBean.setAttribute(SystemFields.INTEGER_ISSUETYPE, null, issueTypeID);
			} else {
				if (itemIsNew) {
					// project column not mapped
					addRowError(requiredFieldErrorsMap, SystemFields.INTEGER_ISSUETYPE, rowNum);
					missingRequiredFound = true;
				}
			}
			if (missingRequiredFound) {
				continue;
			}
			Map<Integer, Integer> restrictedFields = null;
			projectID = workItemBean.getProjectID();
			issueTypeID = workItemBean.getListTypeID();
			if (projectID != null && issueTypeID != null) {
				Map<Integer, Map<Integer, Integer>> issueTypeRestrictions = fieldRestrictions.get(projectID);
				if (issueTypeRestrictions != null) {
					restrictedFields = issueTypeRestrictions.get(issueTypeID);
				}
				if (restrictedFields == null) {
					// no project or issue type mapped get the restriction now
					restrictedFields = AccessBeans.getFieldRestrictions(personID, projectID, issueTypeID, true);
					issueTypeRestrictions = new HashMap<Integer, Map<Integer, Integer>>();
					issueTypeRestrictions.put(issueTypeID, restrictedFields);
					fieldRestrictions.put(projectID, issueTypeRestrictions);
				}
				// new values exist
				if (originalProject != null && originalIssueType != null) {
					// workItem existed
					if (!projectID.equals(originalProject) || !issueTypeID.equals(originalIssueType)) {
						if (!AccessBeans.isAllowedToChange(workItemBean, personID)) {
							// move not allowed
							addRowError(rowErrorsMap, WORKITEM_NO_EDIT_RIGHT, rowNum);
							continue;
						}
					}
				} else {
					// new workItem
					if (!AccessBeans.isAllowedToCreate(personID, projectID, issueTypeID)) {
						// create not allowed
						addRowError(rowErrorsMap, WORKITEM_NO_CREATE_RIGHT, rowNum);
						continue;
					}
				}
			}
			// process the remaining cells
			Map<Integer, Integer> rowNoToIndentLevel = new HashMap<Integer, Integer>();
			for (Cell cell : row) {
				boolean attributeChanged = false;
				int columnIndex = cell.getColumnIndex();
				Integer fieldID = columnIndexToFieldIDMap.get(columnIndex);
				Integer fieldForRestriction = fieldID;
				if (fieldID == null) {
					// LOGGER.debug("No mapping found for column " +
					// columnIndex);
					continue;
				}
				if (fieldID.equals(SystemFields.INTEGER_PROJECT) || fieldID.equals(SystemFields.INTEGER_ISSUETYPE)
						|| identifierFieldValues.containsKey(fieldID) || mandatoryIdentifierFields.contains(fieldID)) {
					// these values are already read
					continue;
				}
				if (fieldID.intValue() < 0) {
					// pseudo field: now only watchers
					if (fieldID.intValue() == TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST
							|| fieldID.intValue() == TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST) {
						fieldForRestriction = FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS;
						String watcherValue = getStringCellValue(cell);
						if (watcherValue == null || "".equals(watcherValue.trim())) {
							continue;
						}
						Map<Integer, List<Integer>> watcherMapOriginal = rowNoToPseudoFieldsOriginal.get(rowNum);
						if (watcherMapOriginal == null) {
							watcherMapOriginal = new HashMap<Integer, List<Integer>>();
							rowNoToPseudoFieldsOriginal.put(rowNum, watcherMapOriginal);
						}
						List<Integer> watcherListOriginal = null;
						TWorkItemBean workItemBeanOriginal = serializableBeanAllowedContext.getWorkItemBeanOriginal();
						if (workItemBeanOriginal != null) {
							if (fieldID.intValue() == TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST) {
								watcherListOriginal = GeneralUtils.createIntegerListFromBeanList(PersonBL.getDirectInformants(workItemBeanOriginal
										.getObjectID()));
							} else {
								watcherListOriginal = GeneralUtils.createIntegerListFromBeanList(PersonBL.getDirectConsultants(workItemBeanOriginal
										.getObjectID()));
							}
							watcherMapOriginal.put(fieldID, watcherListOriginal);
						}
						List<Integer> watcherListExcel = new LinkedList<Integer>();
						String[] watcherNames = watcherValue.split("\\" + ConsultedInformedLoaderBL.WATCHER_SPLITTER_VALUES_STRING);
						if (watcherNames != null) {
							Map<Integer, List<Integer>> watcherMapExcel = rowNoToPseudoFieldsExcel.get(rowNum);
							if (watcherMapExcel == null) {
								watcherMapExcel = new HashMap<Integer, List<Integer>>();
								rowNoToPseudoFieldsExcel.put(rowNum, watcherMapExcel);
							}
							watcherMapExcel.put(fieldID, watcherListExcel);
							for (int i = 0; i < watcherNames.length; i++) {
								String watcherName = watcherNames[i];
								Integer objectID = null;
								try {
									objectID = getWatcherValue(watcherName, fieldID, systemLookups, watcherListOriginal, serializableBeanAllowedContext, locale);
								} catch (ExcelImportNotExistingCellValueException e) {
									addGridError(gridErrorsMap, NOT_EXISTING_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID,
											e.getMessage());
								} catch (ExcelImportNotAllowedCellValueException e) {
									addGridError(gridErrorsMap, NOT_ALLOWED_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID,
											e.getMessage());
								}
								if (objectID != null) {
									watcherListExcel.add(objectID);
									excelValueFound = true;
								}
							}
						}
						attributeChanged = ConsInfBL.watcherChanged(watcherListOriginal, watcherListExcel);
					} else {
						if (fieldID.intValue() == ExcelFieldMatchBL.LOCAL_PARENT_PSEUDO_COLUMN) {
							// local parent - child hierarchy (for new items)
							Integer pseudoHierarchyColumn = fieldIDToColumnIndexMap.get(ExcelFieldMatchBL.LOCAL_PARENT_PSEUDO_COLUMN);
							if (pseudoHierarchyColumn != null) {
								String hierarchyColumn = getStringCellValue(row.getCell(pseudoHierarchyColumn));
								if (hierarchyColumn != null && hierarchyColumn.length() > 0) {
									int previousIndent = 0;
									if (!parentStack.isEmpty()) {
										Integer previousRow = parentStack.peek();
										if (rowToIndent.get(previousRow) != null) {
											previousIndent = rowToIndent.get(previousRow).intValue();
										}
									}
									int actualIndent = hierarchyColumn.length();
									rowToIndent.put(rowNum, actualIndent);
									rowNoToIndentLevel.put(rowNum, actualIndent);
									if (previousIndent == actualIndent) {
										// sibling: same parent as the sibling's
										// parent
										if (!parentStack.isEmpty()) {
											// remove the sibling from stack
											parentStack.pop();
											if (!parentStack.isEmpty()) {
												// if the stack is still not
												// empty then the peek is teh
												// parent
												Integer parentRow = parentStack.peek();
												rowToParentRow.put(rowNum, parentRow);
											}
										}
									} else {
										if (actualIndent > previousIndent) {
											// child of the previous row
											if (actualIndent - previousIndent > 1) {
												// jump more than one in deep is
												// error
												addGridError(gridErrorsMap, INCONSISTENT_HIERARCHY_ERRORS, rowNum,
														ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, hierarchyColumn);
											}
											if (!parentStack.isEmpty()) {
												// add previous row as parent
												Integer parentRow = parentStack.peek();
												rowToParentRow.put(rowNum, parentRow);
											}
										} else {
											// new hierarchy: nothing to do with
											// the previous row
											int difference = previousIndent - actualIndent;
											for (int i = 0; i <= difference; i++) {
												// pop to find the parent
												if (!parentStack.isEmpty()) {
													parentStack.pop();
												}
											}
											if (!parentStack.isEmpty()) {
												Integer parentRow = parentStack.peek();
												rowToParentRow.put(rowNum, parentRow);
											}
										}
									}
								} else {
									// no hierarchy string: top level item
									while (!parentStack.isEmpty()) {
										// empty the stack
										parentStack.pop();
									}
								}
								// add row to stack for possible children
								parentStack.push(rowNum);
							}
						}
					}
				} else {
					IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
					Object attributeValue = null;
					if (fieldTypeRT.isComposite() || fieldTypeRT.isMultipleValues()) {
						String compositeOrMultipleValue = getStringCellValue(cell);
						if (compositeOrMultipleValue == null || "".equals(compositeOrMultipleValue.trim())) {
							workItemBean.setAttribute(fieldID, null, null);
							continue;
						}
						// we suppose that all composite and multiple values are
						// lookup values
						// TODO refactor if that is not true
						String[] parts;
						if (fieldTypeRT.isMultipleValues()) {
							parts = compositeOrMultipleValue.split(CustomSelectBaseRT.OPTION_SPLITTER_VALUES_STRING);
							List<Integer> multipleValues = new ArrayList<Integer>();
							for (int i = 0; i < parts.length; i++) {
								String part = parts[i];
								Integer objectID = null;
								try {
									objectID = getLookupValue(part, fieldTypeRT, fieldID, systemLookups, projectSpecificLookups,
											serializableBeanAllowedContext, null, invalidValueHandlingMap, locale);
								} catch (ExcelImportNotExistingCellValueException e) {
									addGridError(gridErrorsMap, NOT_EXISTING_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID,
											e.getMessage());
								} catch (ExcelImportNotAllowedCellValueException e) {
									addGridError(gridErrorsMap, NOT_ALLOWED_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID,
											e.getMessage());
								}
								if (objectID != null) {
									multipleValues.add(objectID);
								}
							}
							if (!multipleValues.isEmpty()) {
								attributeValue = multipleValues.toArray();
								excelValueFound = true;
							} 
						} else {
							int numberOfParts = ((CustomCompositeBaseRT) fieldTypeRT).getNumberOfParts();
							parts = compositeOrMultipleValue.split("\\" + CustomCompositeBaseRT.PART_SPLITTER_VALUES_STRING);
							if (parts != null && parts.length > numberOfParts) {
								addGridError(gridErrorsMap, WRONG_COMPOSITE_SIZE, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID,
										compositeOrMultipleValue);
							}
							Map<Integer, Integer> componentPartsMap = new HashMap<Integer, Integer>();
							attributeValue = new HashMap<Integer, Object>();
							if (parts != null) {
								for (int i = 0; i < parts.length; i++) {
									String part = parts[i];
									Integer objectID = null;
									IFieldTypeRT componentFieldType = ((CustomCompositeBaseRT) fieldTypeRT).getCustomFieldType(i + 1);
									if (componentFieldType != null) {
										try {
											objectID = getLookupValue(part, componentFieldType, fieldID, systemLookups, projectSpecificLookups,
													serializableBeanAllowedContext, componentPartsMap, invalidValueHandlingMap, locale);
										} catch (ExcelImportNotExistingCellValueException e) {
											addGridError(gridErrorsMap, NOT_EXISTING_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex),
													fieldID, e.getMessage());
										} catch (ExcelImportNotAllowedCellValueException e) {
											addGridError(gridErrorsMap, NOT_ALLOWED_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID,
													e.getMessage());
										}
										if (objectID == null) {
											// workItemBean.setAttribute(fieldID,
											// Integer.valueOf(i+1), null);
											((Map<Integer, Object>) attributeValue).put(Integer.valueOf(i + 1), null);
										} else {
											componentPartsMap.put(Integer.valueOf(i + 1), objectID);
											// workItemBean.setAttribute(fieldID,
											// Integer.valueOf(i+1), new
											// Object[] {objectID});
											((Map<Integer, Object>) attributeValue).put(Integer.valueOf(i + 1), new Object[] { objectID });
											excelValueFound = true;
										}
									}
								}
							}
						}
					} else {
						// simple field
						// Object attributeValue = null;
						try {
							attributeValue = getAttributeValue(cell, fieldID, null, serializableBeanAllowedContext, locale, invalidValueHandlingMap,
									systemLookups, projectSpecificLookups);
						} catch (ExcelImportNotExistingCellValueException e) {
							addGridError(gridErrorsMap, NOT_EXISTING_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, e.getMessage());
						} catch (ExcelImportNotAllowedCellValueException e) {
							addGridError(gridErrorsMap, NOT_ALLOWED_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, e.getMessage());
						} catch (ExcelImportInvalidCellValueException e) {
							addGridError(gridErrorsMap, INVALID_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, e.getMessage());
						}
						if (attributeValue != null) {
							excelValueFound = true;
							if (possibleBottomUpFields.contains(fieldID)) {
								TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getFieldConfigForProjectIssueTypeField(
										projectsToIssueTypesToFieldConfigsMapForBottomUpFields, projectID, issueTypeID, fieldID);
								Object fieldSettings = FieldRuntimeBL.getFieldSettingsForProjectIssueTypeField(
										projectsIssueTypesFieldSettingsMapForBottomUpFields, projectID, issueTypeID, fieldID);
								if (fieldTypeRT.getHierarchicalBehavior(fieldID, fieldConfigBean, fieldSettings) == HIERARCHICAL_BEHAVIOR_OPTIONS.COMPUTE_BOTTOM_UP
										&& ItemBL.hasChildren(workItemBean.getObjectID())) {
									Date trackPlusAttributeValue = (Date) workItemBean.getAttribute(fieldID);
									if (EqualUtils.notEqual(trackPlusAttributeValue, (Date) attributeValue)) {
										// add read only restrictions for start
										// and end date for non leaf workItems
										LOGGER.debug("Parent change restriction for bottom up date " + fieldID);
										addGridError(gridErrorsMap, NOT_EDITABLE_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID,
												getStringCellValue(cell));
									}
								}
								/*
								 * if
								 * (ApplicationBean.getApplicationBean().getSiteBean
								 * ().getSummaryItemsBehavior() &&
								 * ItemBL2.hasChildren
								 * (workItemBean.getObjectID())) { Date
								 * trackPlusAttributeValue =
								 * (Date)workItemBean.getAttribute(fieldID); if
								 * (EqualUtils.notEqual(trackPlusAttributeValue,
								 * (Date)attributeValue)) { //add read only
								 * restrictions for start and end date for non
								 * leaf workItems LOGGER.debug(
								 * "Summary parent change restriction for date "
								 * + fieldID); addGridError(gridErrorsMap,
								 * NOT_EDITABLE_ERRORS, rowNum,
								 * ExcelFieldMatchBL
								 * .colNumericToLetter(columnIndex), fieldID,
								 * getStringCellValue(cell)); } }
								 */
							}
						}
					}
					attributeChanged = fieldTypeRT.valueModified(attributeValue, workItemBean.getAttribute(fieldID));
					workItemBean.setAttribute(fieldID, null, attributeValue);
				}
				if (attributeChanged) {
					try {
						verifyFieldRestrictions(fieldForRestriction, restrictedFields, cell, locale);
					} catch (ExcelImportNotModifiableCellValueException e) {
						addGridError(gridErrorsMap, NOT_EDITABLE_ERRORS, rowNum, ExcelFieldMatchBL.colNumericToLetter(columnIndex), fieldID, e.getMessage());
					}
				}
			}
			if (!excelValueFound) {
				// not a single excel value found in any cell from the row
				// simply neglect this row.
				// expanded row count can be greater than the number of real
				// workItem rows
				// for example when the content of some rows is deleted but the
				// rows are not deleted
				// and empty rows may remain in the excel
				LOGGER.info("The row number " + (rowNum + 1) + " contains only empty cells and will be neglected");
				continue;
			}
			// add the default values for those fields which didn't have column
			// in
			// excel sheet or have column but the value is empty or not valid
			Iterator<Integer> itrDefaultValueFields = defaultValuesMap.keySet().iterator();
			while (itrDefaultValueFields.hasNext()) {
				Integer fieldID = itrDefaultValueFields.next();
				if (/*!fieldIDToColumnIndexMap.containsKey(fieldID) ||*/workItemBean.getAttribute(fieldID, null) == null) {
					if (invalidValueHandlingMap.containsKey(fieldID)) {
						if (DEFAULT_IF_NOT_EXIST_OR_EMPTY.equals(invalidValueHandlingMap.get(fieldID))) {
							IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID, null);
							ILookup lookup = (ILookup) fieldTypeRT;
							Integer defaultObjectID = defaultValuesMap.get(fieldID);
							if (defaultObjectID != null) {
								boolean allowed = lookup.lookupBeanAllowed(defaultObjectID, serializableBeanAllowedContext);
								if (allowed) {
									workItemBean.setAttribute(fieldID, null, defaultObjectID);
								} else {
									// for example when no default project
									// and/or issue type is specified the
									// default manager and responsible
									// lists contain the users which are manager
									// or responsible in any of the projects
									// (but maybe not in all)
									LOGGER.debug("The default value is not allowed for field " + fieldID + " on row " + rowNum);
									// cache the show values and localized
									// labels to spare additional database
									// accesses
									String showValue;
									if (defaultShowValuesMap.containsKey(fieldID)) {
										showValue = defaultShowValuesMap.get(fieldID);
									} else {
										showValue = fieldTypeRT.getShowValue(defaultObjectID, locale);
										defaultShowValuesMap.put(fieldID, showValue);
									}
									String localizedLabel;
									if (defaultLocalizedFieldLabels.containsKey(fieldID)) {
										localizedLabel = defaultLocalizedFieldLabels.get(fieldID);
									} else {
										localizedLabel = FieldRuntimeBL.getLocalizedDefaultFieldLabel(fieldID, locale);
										defaultLocalizedFieldLabels.put(fieldID, localizedLabel);
									}
									addGridError(gridErrorsMap, NOT_ALLOWED_DEFAULT_VALUES_ERRORS, rowNum, localizedLabel, fieldID, showValue);
								}
							}
						}
					}
				}
			}
			workItemBeansMap.put(rowNum, workItemBean);
		}
		return workItemBeansMap;
	}

	/**
	 * Transform the workItemID$fieldID keyed map into a workItemID to fieldID
	 * to overwrite flag map
	 * 
	 * @param conflictResoultionMap
	 * @return
	 */
	private static Map<Integer, Map<Integer, Boolean>> getWorkItemAndFieldBasedMap(Map<String, Boolean> conflictResoultionMap) {
		Map<Integer, Map<Integer, Boolean>> workItemAndFieldBasedMap = new HashMap<Integer, Map<Integer, Boolean>>();
		if (conflictResoultionMap != null) {
			for (Iterator<String> iterator = conflictResoultionMap.keySet().iterator(); iterator.hasNext();) {
				String workItemAndKey = iterator.next();
				Boolean overwrite = conflictResoultionMap.get(workItemAndKey);
				String[] strArr = workItemAndKey.split(SPLITTER);
				Integer workItemID = null;
				Integer fieldID = null;
				if (strArr != null && strArr.length > 1) {
					workItemID = Integer.valueOf(strArr[0]);
					fieldID = Integer.valueOf(strArr[1]);
				}
				if (workItemID != null && fieldID != null) {
					Map<Integer, Boolean> fieldsMap = workItemAndFieldBasedMap.get(workItemID);
					if (fieldsMap == null) {
						fieldsMap = new HashMap<Integer, Boolean>();
						workItemAndFieldBasedMap.put(workItemID, fieldsMap);
					}
					fieldsMap.put(fieldID, overwrite);
				}
			}
		}
		return workItemAndFieldBasedMap;
	}

	/**
	 * Prepare the conflicts
	 * 
	 * @param workItemBeansList
	 * @param presentFieldIDs
	 * @param personID
	 * @param locale
	 * @return
	 */
	static SortedMap<Integer, SortedMap<Integer, Map<Integer, Object>>> conflictResolutionWorkItems(Collection<TWorkItemBean> workItemBeansList,
			Set<Integer> presentFieldIDs, Map<Integer, WorkItemContext> existingIssueContextsMap,
			Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap, Map<Integer, String> columnIndexToColumNameMap,
			Map<Integer, Integer> fieldIDToColumnIndexMap, Integer personID, Locale locale, Map<String, Boolean> overwriteMap) {

		Map<Integer, Map<Integer, Boolean>> workItemAndFieldBasedMap = getWorkItemAndFieldBasedMap(overwriteMap);

		SortedMap<Integer, SortedMap<Integer, Map<Integer, Object>>> conflictsMap = new TreeMap<Integer, SortedMap<Integer, Map<Integer, Object>>>();
		Iterator<TWorkItemBean> itrWorkItemBean = workItemBeansList.iterator();
		Set<Integer> hardcodedExplicitHistoryFields = HistorySaverBL.getHardCodedExplicitHistoryFields();
		int row = 1;
		Calendar calendar = Calendar.getInstance();
		while (itrWorkItemBean.hasNext()) {
			TWorkItemBean workItemBean = itrWorkItemBean.next();
			Integer workItemID = workItemBean.getObjectID();
			row++;
			Map<Integer, TFieldConfigBean> fieldConfigsMap = projectsIssueTypesFieldConfigsMap.get(workItemBean.getProjectID()).get(
					workItemBean.getListTypeID());
			WorkItemContext workItemContext = existingIssueContextsMap.get(workItemID);
			if (workItemContext != null) {
				// conflicts can happen only for existing workItems
				TWorkItemBean workItemBeanOriginal = workItemContext.getWorkItemBeanOriginal();
				Date excelLastEdited = null;
				if (fieldIDToColumnIndexMap.get(SystemFields.INTEGER_LASTMODIFIEDDATE) != null) {
					// it was overwritten in workItemBean from excel only if the
					// field was mapped
					excelLastEdited = workItemBean.getLastEdit();
				}
				if (workItemBeanOriginal != null) {
					List<Integer> changedFields = getFieldsChanged(workItemBean, workItemBeanOriginal, presentFieldIDs);
					if (changedFields == null || changedFields.isEmpty()) {
						// no field change at all -> no conflict
						continue;
					}
					SortedMap<Integer, Map<Integer, HistoryValues>> workItemHistoryChanges = null;
					Map<Integer, Boolean> fieldForWorkItemOverwrite = workItemAndFieldBasedMap.get(workItemID);
					/*
					 * if (fieldForWorkItemOverwrite!=null) { //after submitting
					 * the overwrite map (the conflict handling is done by the
					 * user) for (Iterator<Integer> iterator =
					 * changedFields.iterator(); iterator.hasNext();) { Integer
					 * fieldID = iterator.next(); Boolean overwrite =
					 * fieldForWorkItemOverwrite.get(fieldID); if
					 * (overwrite==null || !overwrite.booleanValue()) {
					 * //overwrite==null there was no conflict at all (no
					 * checkbox was rendered) //if user decided to leave the
					 * track+ value change back to original
					 * workItemBean.setAttribute(fieldID,
					 * workItemBeanOriginal.getAttribute(fieldID)); } } //once
					 * fieldOverwrite is already specified no further conflict
					 * processing is needed continue; }
					 */
					if (excelLastEdited == null) {
						// no last edit field specified, no usable history data
						// available at all: each field change means conflict
						for (Iterator<Integer> itrField = changedFields.iterator(); itrField.hasNext();) {
							Integer fieldID = itrField.next();
							addAsConfict(conflictsMap, row, fieldID, workItemBean, workItemBeanOriginal, columnIndexToColumNameMap, fieldIDToColumnIndexMap,
									overwriteMap, fieldForWorkItemOverwrite, locale);
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("WorkItem " + workItemBean.getObjectID() + " row " + row + " fieldID " + fieldID
										+ " has conficts (no lastEdit specified)");
							}
						}
					} else {
						// last edited is specified in excel: search the history
						// changed fields are either with explicit history or
						// not
						List<Integer> changedFieldsWithExplicitHistory = new LinkedList<Integer>();
						List<Integer> changedFieldsWithoutExplicitHistory = new LinkedList<Integer>();
						for (Iterator<Integer> iterator = changedFields.iterator(); iterator.hasNext();) {
							Integer fieldID = iterator.next();
							TFieldConfigBean fieldConfigBean = fieldConfigsMap.get(fieldID);
							if (fieldConfigBean.isHistoryString() || hardcodedExplicitHistoryFields.contains(fieldID)) {
								changedFieldsWithExplicitHistory.add(fieldID);
							} else {
								changedFieldsWithoutExplicitHistory.add(fieldID);
							}
						}
						if (!changedFieldsWithoutExplicitHistory.isEmpty()) {
							// if at least one changed field hat no explicit
							// history then take the commons history field also
							changedFieldsWithExplicitHistory.add(TFieldChangeBean.COMPOUND_HISTORY_FIELD);
						}

						Integer[] changedFieldIDs = GeneralUtils.createIntegerArrFromIntArr(GeneralUtils
								.createIntArrFromIntegerList(changedFieldsWithExplicitHistory));
						if (changedFieldsWithExplicitHistory != null && !changedFieldsWithExplicitHistory.isEmpty()) {
							// get the changes for fields since excelLastEdited:
							// explicit fields and
							// and the common field if at least one field hasn't
							// explicit history
							// TODO not really correct to add a minute but:
							// the last edited date from excel
							// (DateFormat.SHORT) doesn't contain seconds,
							// while the last history entry from the Genji
							// issue contains even milliseconds.
							// Consequently the history entry's date from Genji
							// is after the one exported to excel
							// even if in track+ no further history entry exists
							// (no further change was made).
							// Consequently we would generate false conflicts.
							// If we add this extra minute then we get rid of
							// those false conflicts but there is a small risk
							// that if also another change was made in the same
							// minute, it will be overwritten by excel value
							// without conflict warning
							calendar.setTime(excelLastEdited);
							calendar.add(Calendar.MINUTE, 1);
							workItemHistoryChanges = HistoryLoaderBL.getWorkItemRawHistory(workItemBean.getObjectID(), changedFieldIDs, null,
									calendar.getTime(), null);
						}
						if (workItemHistoryChanges != null) {
							// there is some history data
							for (Iterator<Integer> itrField = changedFieldsWithExplicitHistory.iterator(); itrField.hasNext();) {
								IFieldTypeRT fieldTypeRT = null;
								Integer fieldID = itrField.next();
								if (!fieldID.equals(TFieldChangeBean.COMPOUND_HISTORY_FIELD)) {
									fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
								}
								HistoryValues historyValues = null;
								for (Iterator<Integer> itrTransaction = workItemHistoryChanges.keySet().iterator(); itrTransaction.hasNext();) {
									// get the first entry from the history
									// after excelLastEdited
									Integer transactionID = itrTransaction.next();
									Map<Integer, HistoryValues> historyValuesMap = workItemHistoryChanges.get(transactionID);
									if (historyValuesMap.containsKey(fieldID)) {
										historyValues = historyValuesMap.get(fieldID);
										break;
									}
								}
								// changedFields contains fields with explicit
								// history, without explicit history and commons
								// field
								if (historyValues != null) {// if no history
															// value, no
															// conflict
									if (fieldTypeRT != null) {// explicit
																// history
										// the actual excel value differs from
										// the first oldValue -> the field was
										// probably changed in excel also
										if (fieldTypeRT.valueModified(workItemBean.getAttribute(fieldID), historyValues.getOldValue())) {
											// field with explicit history
											// changed in excel and track+: that
											// is a conflict
											addAsConfict(conflictsMap, row, fieldID, workItemBean, workItemBeanOriginal, columnIndexToColumNameMap,
													fieldIDToColumnIndexMap, overwriteMap, fieldForWorkItemOverwrite, locale);
											if (LOGGER.isDebugEnabled()) {
												LOGGER.debug("WorkItem " + workItemBean.getObjectID() + " row " + row + " fieldID " + fieldID
														+ " has conficts (explicit history found)");
											}
										} else {
											// the excel value is the same with
											// the first old value from the
											// history
											// since the excel last modified
											// date: the value was modified only
											// in track+,
											// leave the track+ version without
											// conflict resolution
											workItemBean.setAttribute(fieldID, workItemBeanOriginal.getAttribute(fieldID));
											if (LOGGER.isDebugEnabled()) {
												LOGGER.debug("WorkItem " + workItemBean.getObjectID() + " row " + row + " fieldID " + fieldID
														+ "no conflict (value changed only in Genji)");
											}
										}
									} else {
										// common history: history for fields
										// without explicit history
										// add a conflict for each field without
										// explicit history
										for (Iterator<Integer> iterator = changedFieldsWithoutExplicitHistory.iterator(); iterator.hasNext();) {
											Integer fieldWithoutExplicitHistory = iterator.next();
											addAsConfict(conflictsMap, row, fieldWithoutExplicitHistory, workItemBean, workItemBeanOriginal,
													columnIndexToColumNameMap, fieldIDToColumnIndexMap, overwriteMap, fieldForWorkItemOverwrite, locale);
											if (LOGGER.isDebugEnabled()) {
												LOGGER.debug("WorkItem " + workItemBean.getObjectID() + " row " + row + " fieldID " + fieldID
														+ " has conficts (common history found)");
											}
										}
									}
								} else {
									// no history entry found for field: the
									// field was modified only in excel, no
									// conflict handling needed
									if (LOGGER.isDebugEnabled()) {
										LOGGER.debug("WorkItem " + workItemBean.getObjectID() + " row " + row + " fieldID " + fieldID
												+ " no conflict: no fieldID history found, value changed only in Excel)");
									}
								}
							}
						} else {
							// no history entry found for the entire workItem:
							// the workItem was modified only in excel, no
							// conflict handling needed
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("WorkItem " + workItemBean.getObjectID() + " row " + row
										+ " no conflict: no workItem history found, value changed only in Excel");
							}
						}
					}
				}
			}
		}
		return conflictsMap;
	}

	/**
	 * Add a new conflict for a field from a row
	 * 
	 * @param conflictsMap
	 * @param row
	 * @param fieldID
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param locale
	 */
	private static void addAsConfict(SortedMap<Integer, SortedMap<Integer, Map<Integer, Object>>> conflictsMap, Integer row, Integer fieldID,
			TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Map<Integer, String> columnIndexToColumNameMap,
			Map<Integer, Integer> fieldIDToColumnIndexMap, Map<String, Boolean> overwriteMap, Map<Integer, Boolean> fieldForWorkItemOverwrite, Locale locale) {

		Boolean overwrite = null;
		if (fieldForWorkItemOverwrite != null) {
			// overwrite flag submitted from the user
			overwrite = fieldForWorkItemOverwrite.get(fieldID);
		}
		if (overwrite != null) {
			// was already submitted
			if (!overwrite.booleanValue()) {
				// user decided to leave the track+ value change back to
				// original: reset the changes made from excel
				workItemBean.setAttribute(fieldID, workItemBeanOriginal.getAttribute(fieldID));
			}
			// do not render conflict again once the user submitted his conflict
			// resolution choice
			return;
		}

		SortedMap<Integer, Map<Integer, Object>> fieldConflictsForRow = conflictsMap.get(row);
		if (fieldConflictsForRow == null) {
			fieldConflictsForRow = new TreeMap<Integer, Map<Integer, Object>>();
			conflictsMap.put(row, fieldConflictsForRow);
		}
		// add map for field
		Map<Integer, Object> fieldConflictMap = new HashMap<Integer, Object>();
		fieldConflictsForRow.put(fieldID, fieldConflictMap);
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
		String excelShowValue = fieldTypeRT.getShowValue(workItemBean.getAttribute(fieldID), locale);
		String trackPlusShowValue = fieldTypeRT.getShowValue(workItemBeanOriginal.getAttribute(fieldID), locale);
		fieldConflictMap.put(IConflictMapEntry.WORKITEMID, workItemBean.getObjectID());
		Integer columnIndex = fieldIDToColumnIndexMap.get(fieldID);
		if (columnIndex != null) {
			// column is mapped
			fieldConflictMap.put(IConflictMapEntry.COLUMN_LETTER, ExcelFieldMatchBL.colNumericToLetter(columnIndex));
			fieldConflictMap.put(IConflictMapEntry.FELED_NAME, columnIndexToColumNameMap.get(columnIndex));
		} else {
			// (probably mandatory) column not mapped, take the track+ field
			// label
			fieldConflictMap.put(IConflictMapEntry.FELED_NAME, FieldRuntimeBL.getLocalizedDefaultFieldLabel(fieldID, locale));
		}
		fieldConflictMap.put(IConflictMapEntry.EXCEL_VALUE, excelShowValue);
		fieldConflictMap.put(IConflictMapEntry.TRACKPLUS_VALUE, trackPlusShowValue);
		String combinedField = workItemBean.getObjectID() + SPLITTER + fieldID;
		fieldConflictMap.put(IConflictMapEntry.WORKITEMID_FIELDID, combinedField);
		overwriteMap.put(combinedField, IConflictResolution.OVERWRITE);
	}

	/**
	 * Whether the some fields have different values in excel and Genji
	 * 
	 * @param workItemBean
	 * @param workItemBeanOriginal
	 * @param presentFields
	 * @return
	 */
	private static List<Integer> getFieldsChanged(TWorkItemBean workItemBean, TWorkItemBean workItemBeanOriginal, Set<Integer> presentFields) {
		List<Integer> changedFields = new ArrayList<Integer>();
		if (workItemBeanOriginal == null || presentFields == null) {
			return changedFields;
		}
		Iterator<Integer> itrPresentFields = presentFields.iterator();
		while (itrPresentFields.hasNext()) {
			Integer fieldID = (Integer) itrPresentFields.next();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT == null) {
				// not a real field (but a pseudo field or cons/inf from the
				// bulk operation)
				continue;
			}
			Object newValue = workItemBean.getAttribute(fieldID);
			Object oldValue = workItemBeanOriginal.getAttribute(fieldID);
			if (fieldTypeRT.valueModified(newValue, oldValue)) {
				changedFields.add(fieldID);
			}
		}
		return changedFields;
	}

	/**
	 * Add the error in the map
	 * 
	 * @param errorsMap
	 * @param rowIndex
	 * @param columIndex
	 * @param value
	 */
	private static void addGridError(Map<Integer, SortedMap<Integer, SortedMap<String, ErrorData>>> errorsMap, Integer errorType, int rowIndex,
			String columIndex, Integer fieldID, String value) {
		SortedMap<Integer, SortedMap<String, ErrorData>> errorsOfType = errorsMap.get(errorType);
		if (errorsOfType == null) {
			errorsOfType = new TreeMap<Integer, SortedMap<String, ErrorData>>();
			errorsMap.put(errorType, errorsOfType);
		}
		Integer newRowIndex = new Integer(rowIndex + 1);
		SortedMap<String, ErrorData> rowErrors = errorsOfType.get(newRowIndex);
		if (rowErrors == null) {
			rowErrors = new TreeMap<String, ErrorData>();
			errorsOfType.put(newRowIndex, rowErrors);
		}
		ErrorData errorData = rowErrors.get(columIndex);
		if (errorData == null) {
			rowErrors.put(columIndex, new ErrorData(fieldID, value));
		} else {
			// for composite parts when more than one part value is invalid
			String existingValue = errorData.getResourceKey();
			if (existingValue != null && value != null && !"".equals(value.trim())) {
				errorData.setResourceKey(existingValue + CustomCompositeBaseRT.PART_SPLITTER_STRING + value);
			}
		}
	}

	/**
	 * Add the error in the map
	 * 
	 * @param errorsMap
	 * @param errorType
	 * @param rowIndex
	 */
	private static void addRowError(Map<Integer, SortedSet<Integer>> errorsMap, Integer errorType, int rowIndex) {
		SortedSet<Integer> errorsOfType = errorsMap.get(errorType);
		if (errorsOfType == null) {
			errorsOfType = new TreeSet<Integer>();
			errorsMap.put(errorType, errorsOfType);
		}
		errorsOfType.add(Integer.valueOf(rowIndex + 1));
	}

	/**
	 * Test edit restrictions for some hardcoded fields
	 * 
	 * @param fieldID
	 * @param restrictedFields
	 * @param cell
	 * @param locale
	 * @throws ExcelImportNotModifiableCellValueException
	 */
	private static void verifyFieldRestrictions(Integer fieldID, Map<Integer, Integer> restrictedFields, Cell cell, Locale locale)
			throws ExcelImportNotModifiableCellValueException {
		if (restrictedFields != null && restrictedFields.containsKey(fieldID)) {
			throw new ExcelImportNotModifiableCellValueException(getStringCellValue(cell));
		}
	}

	private static Object getAttributeValue(Cell cell, Integer fieldID, Integer parameterCode, SerializableBeanAllowedContext serializableBeanAllowedContext,
			Locale locale, Map<Integer, Integer> invalidValueHandlingMap, Map<Integer, Map<String, ILabelBean>> systemLookups,
			Map<Integer, Map<Integer, Map<String, ILabelBean>>> projectSpecificLookups) throws ExcelImportNotExistingCellValueException,
			ExcelImportNotAllowedCellValueException, ExcelImportInvalidCellValueException {

		if (cell == null) {
			return null;
		}
		IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID, parameterCode);
		Object attributeValue = null;
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_BLANK || cellType == Cell.CELL_TYPE_FORMULA || cellType == Cell.CELL_TYPE_ERROR) {
			return null;
		}
		int valueType;
		if (SystemFields.INTEGER_ISSUENO.equals(fieldID)) {
			// althogh it is ValueType.TEXT from fieldType it should be set to
			// ValueType.INTEGER because otherwise it is read as string and
			// interpreted as date
			valueType = ValueType.INTEGER;
		} else {
			valueType = fieldTypeRT.getValueType();
		}
		switch (valueType) {
		case ValueType.CUSTOMOPTION:
		case ValueType.SYSTEMOPTION:
			if (SystemFields.INTEGER_ISSUENO.equals(fieldTypeRT.getSystemOptionType())) {
				// extra care for issueNo and parentNo because although they are
				// of type ValueType.SYSTEMOPTION but
				// the direct integer value appears in the excel sheet instead
				// of label values
				attributeValue = getIntegerCellValue(cell);
			} else {
				// lookups
				String stringValue = getStringCellValue(cell);
				attributeValue = getLookupValue(stringValue, fieldTypeRT, fieldID, systemLookups, projectSpecificLookups, serializableBeanAllowedContext, null,
						invalidValueHandlingMap, locale);
			}
			break;
		case ValueType.BOOLEAN:
			attributeValue = getBooleanCellValue(cell);
			break;
		case ValueType.LONGTEXT:
		case ValueType.TEXT:
		case ValueType.EXTERNALID:
			attributeValue = getStringCellValue(cell);
			if (valueType == ValueType.LONGTEXT) {
				attributeValue = Text2HTML.addHTMLBreaks((String) attributeValue);
			}
			break;
		case ValueType.DATE:
			attributeValue = getDateCellValue(cell, locale);
			break;
		case ValueType.DATETIME:
			attributeValue = getDateTimeCellValue(cell, locale);
			break;
		case ValueType.DOUBLE:
			attributeValue = getDoubleCellValue(cell, locale);
			break;
		case ValueType.INTEGER:
			attributeValue = getIntegerCellValue(cell);
			break;
		}
		return attributeValue;
	}

	/**
	 * Return the lookup objectID based in the label
	 * 
	 * @param stringValue
	 * @param fieldTypeRT
	 * @param fieldID
	 * @param systemLookups
	 * @param projectSpecificLookups
	 * @param serializableBeanAllowedContext
	 * @param componentPartsMap
	 * @param invalidValueHandlingMap
	 * @param locale
	 * @return
	 * @throws ExcelImportNotExistingCellValueException
	 * @throws ExcelImportNotAllowedCellValueException
	 * @throws ExcelImportException
	 */
	private static Integer getLookupValue(String stringValue, IFieldTypeRT fieldTypeRT, Integer fieldID, Map<Integer, Map<String, ILabelBean>> systemLookups,
			Map<Integer, Map<Integer, Map<String, ILabelBean>>> projectSpecificLookups, SerializableBeanAllowedContext serializableBeanAllowedContext,
			Map<Integer, Integer> componentPartsMap, Map<Integer, Integer> invalidValueHandlingMap, Locale locale)
			throws ExcelImportNotExistingCellValueException, ExcelImportNotAllowedCellValueException {
		if (stringValue != null && !"".equals(stringValue)) {
			ILookup lookup = (ILookup) fieldTypeRT;
			Integer lookupKey = lookup.getDropDownMapFieldKey(fieldID);
			Map<String, ILabelBean> lookupBeansMap = null;
			// try to find the objectID from dropDownContainer, to avoid to go
			// to database for each entry in turn
			if (lookupKey.equals(SystemFields.RELEASE)) {
				// project specific: can be that two
				// subprojects/classes/releases have the same
				// label in different projects: we should work project specific
				if (projectSpecificLookups != null) {
					Map<Integer, Map<String, ILabelBean>> projectspecificLookup = projectSpecificLookups.get(serializableBeanAllowedContext.getProjectID());
					if (projectspecificLookup != null) {
						lookupBeansMap = projectspecificLookup.get(lookupKey);
					}
				}
			} else {
				lookupBeansMap = systemLookups.get(lookupKey);
			}
			String trimmedString = stringValue.trim();
			// first try to match the trimmed string values
			Integer objectIDByLabel = lookup.getLookupIDByLabel(fieldID, serializableBeanAllowedContext.getProjectID(),
					serializableBeanAllowedContext.getIssueTypeID(), locale, trimmedString, lookupBeansMap, componentPartsMap);
			if (objectIDByLabel == null) {
				// now try the original (non trimmed) value also
				//stringValue = stringValue.trim();
				objectIDByLabel = lookup.getLookupIDByLabel(fieldID, serializableBeanAllowedContext.getProjectID(),
						serializableBeanAllowedContext.getIssueTypeID(), locale, stringValue, lookupBeansMap, componentPartsMap);
			}
			if (objectIDByLabel == null) {
				// lookup entity does not exist, but we may have default values
				if (invalidValueHandlingMap.containsKey(fieldID)) {
					if (REJECT_IF_NOT_EXIST_OR_EMPTY.equals(invalidValueHandlingMap.get(fieldID))) {
						LOGGER.debug("Reject field " + fieldID + " with label " + stringValue);
						throw new ExcelImportNotExistingCellValueException(stringValue);
					} else {
						// get the default value
						LOGGER.debug("Field " + fieldID + " with label " + stringValue + " does not exist. The default value will be taken");
						// attributeValue = defaultValuesMap.get(fieldID);
					}
				} else {
					LOGGER.debug("The value " + stringValue + " does not exist for field " + fieldID);
					throw new ExcelImportNotExistingCellValueException(stringValue);
				}
			} else {
				boolean allowed = lookup.lookupBeanAllowed(objectIDByLabel, serializableBeanAllowedContext);
				if (allowed) {
					return objectIDByLabel;
				} else {
					if (invalidValueHandlingMap.containsKey(fieldID)) {
						if (REJECT_IF_NOT_EXIST_OR_EMPTY.equals(invalidValueHandlingMap.get(fieldID))) {
							LOGGER.debug("Reject field " + fieldID + " with label " + stringValue);
							throw new ExcelImportNotAllowedCellValueException(stringValue);
						} else {
							// get the default value
							LOGGER.debug("Field " + fieldID + " with label " + stringValue + " does not exist. The default value will be taken");
						}
					} else {
						LOGGER.debug("The value " + stringValue + " is not allowed for field " + fieldID);
						throw new ExcelImportNotAllowedCellValueException(stringValue);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Return the lookup objectID based in the label
	 * 
	 * @param stringValue
	 * @param fieldTypeRT
	 * @param fieldID
	 * @param systemLookups
	 * @param projectSpecificLookups
	 * @param serializableBeanAllowedContext
	 * @param componentPartsMap
	 * @param invalidValueHandlingMap
	 * @param locale
	 * @return
	 * @throws ExcelImportNotExistingCellValueException
	 * @throws ExcelImportNotAllowedCellValueException
	 * @throws ExcelImportException
	 */
	private static Integer getWatcherValue(String stringValue, Integer fieldID, Map<Integer, Map<String, ILabelBean>> systemLookups,
			List<Integer> watcherListOriginal, SerializableBeanAllowedContext serializableBeanAllowedContext, Locale locale)
			throws ExcelImportNotExistingCellValueException, ExcelImportNotAllowedCellValueException {
		if (stringValue != null && !"".equals(stringValue)) {
			Integer projectID = serializableBeanAllowedContext.getProjectID();
			Integer itemTypeID = serializableBeanAllowedContext.getIssueTypeID();
			ILookup lookup = new SystemManagerRT();
			// Integer lookupKey = lookup.getDropDownMapFieldKey(fieldID);
			Map<String, ILabelBean> lookupBeansMap = null;
			// try to find the objectID from dropDownContainer, to avoid to go
			// to database for each entry in turn
			lookupBeansMap = systemLookups.get(SystemFields.INTEGER_PERSON);
			String trimmedString = stringValue.trim();
			// first try to match the trimmed string values
			Integer objectIDByLabel = lookup.getLookupIDByLabel(fieldID, projectID, itemTypeID, locale, trimmedString, lookupBeansMap, null);
			if (objectIDByLabel == null) {
				// now try the original (non trimmed) value also
				stringValue = stringValue.trim();
				objectIDByLabel = lookup.getLookupIDByLabel(fieldID, projectID, itemTypeID, locale, stringValue, lookupBeansMap, null);
			}
			if (objectIDByLabel == null) {
				// lookup entity does not exist
				LOGGER.debug("The value " + stringValue + " does not exist for field " + fieldID);
				throw new ExcelImportNotExistingCellValueException(stringValue);
			} else {
				if (watcherListOriginal != null && watcherListOriginal.contains(objectIDByLabel)) {
					return objectIDByLabel;
				}
				int accessFlag;
				if (fieldID.equals(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST)) {
					accessFlag = AccessFlagIndexes.INFORMANT;
				} else {
					accessFlag = AccessFlagIndexes.CONSULTANT;
				}
				boolean allowed = AccessBeans.hasPersonRightInProjectForIssueType(objectIDByLabel, projectID, itemTypeID, accessFlag, false, false);
				if (allowed) {
					return objectIDByLabel;
				} else {
					LOGGER.debug("The value " + stringValue + " is not allowed for field " + fieldID);
					// /throw an exception only if the not allowed value not
					// exist already
					throw new ExcelImportNotAllowedCellValueException(stringValue);
				}
			}
		}
		return null;
	}

	/**
	 * Gets the string value of a cell
	 * 
	 * @param cell
	 * @return
	 */
	static String getStringCellValue(Cell cell) {
		try {
			int cellType = cell.getCellType();
			switch (cellType) {
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_ERROR:
				return String.valueOf(cell.getErrorCellValue());
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_NUMERIC:
				try {
					double doubleValue = cell.getNumericCellValue();
					int intValue = (int) doubleValue;
					double fracPart = doubleValue - intValue;
					if (Math.abs(fracPart) <= Double.MIN_VALUE) {
						return String.valueOf(intValue);
					} else {
						return String.valueOf(doubleValue);
					}
				} catch (Exception e) {
				}
			case Cell.CELL_TYPE_STRING:
				RichTextString richTextString = cell.getRichStringCellValue();
				if (richTextString != null) {
					return richTextString.getString();
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Getting the string value failed with " + e.getMessage(), e);
		}
		return "";
	}

	/**
	 * Gets the Integer value of a cell
	 * 
	 * @param cell
	 * @return
	 */
	private static Integer getIntegerCellValue(Cell cell) throws ExcelImportInvalidCellValueException {
		Integer integerValue = null;
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			Double doubleValue = null;
			try {
				double numericValue = cell.getNumericCellValue();
				doubleValue = new Double(numericValue);
				integerValue = new Integer(doubleValue.intValue());
			} catch (Exception e) {
				if (doubleValue == null) {
					doubleValue = new Double(Double.NaN);
				}
				throw new ExcelImportInvalidCellValueException(doubleValue.toString());
			}
		} else {
			if (cellType == Cell.CELL_TYPE_STRING) {
				RichTextString richTextString = null;
				richTextString = cell.getRichStringCellValue();
				if (richTextString != null) {
					String stringValue = richTextString.getString();
					if (stringValue != null && !"".equals(stringValue)) {
						stringValue = stringValue.trim();
						if (stringValue != null) {
							try {
								integerValue = Integer.valueOf(stringValue);
							} catch (Exception e) {
								throw new ExcelImportInvalidCellValueException(stringValue);
							}
						}
					}
				}
			} else {
				throw new ExcelImportInvalidCellValueException(getStringCellValue(cell));
			}
		}
		return integerValue;
	}

	/**
	 * Gets the Double value of a cell
	 * 
	 * @param cell
	 * @return
	 */
	private static Double getDoubleCellValue(Cell cell, Locale locale) throws ExcelImportInvalidCellValueException {
		Double doubleValue = null;
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			double numericValue = cell.getNumericCellValue();
			try {
				doubleValue = new Double(numericValue);
			} catch (Exception e) {
				throw new ExcelImportInvalidCellValueException(String.valueOf(numericValue));
			}
		} else {
			if (cellType == Cell.CELL_TYPE_STRING) {
				RichTextString richTextString = cell.getRichStringCellValue();
				if (richTextString != null) {
					String stringValue = richTextString.getString();
					if (stringValue != null) {
						stringValue = stringValue.trim();
						if (!"".equals(stringValue)) {
							doubleValue = DoubleNumberFormatUtil.getInstance().parseGUI(stringValue, locale);
							if (doubleValue == null) {
								doubleValue = DoubleNumberFormatUtil.parseISO(stringValue);
								if (doubleValue == null) {
									throw new ExcelImportInvalidCellValueException(stringValue);
								}
							}
						}
					}
				}
			} else {
				throw new ExcelImportInvalidCellValueException(getStringCellValue(cell));
			}
		}
		return doubleValue;
	}

	/**
	 * Gets the Double value of a cell
	 * 
	 * @param cell
	 * @return
	 */
	private static Date getDateCellValue(Cell cell, Locale locale) throws ExcelImportInvalidCellValueException {
		Date dateValue = null;
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			try {
				dateValue = cell.getDateCellValue();
			} catch (Exception e) {
				throw new ExcelImportInvalidCellValueException(String.valueOf(cell.getNumericCellValue()));
			}
		} else {
			if (cellType == Cell.CELL_TYPE_STRING) {
				RichTextString richTextString = cell.getRichStringCellValue();
				if (richTextString != null) {
					String stringValue = richTextString.getString();
					if (stringValue != null) {
						stringValue = stringValue.trim();
						if (!"".equals(stringValue)) {
							dateValue = DateTimeUtils.getInstance().parseGUIDate(stringValue, locale);
							if (dateValue == null) {
								dateValue = DateTimeUtils.getInstance().parseShortDate(stringValue, locale);
								if (dateValue == null) {
									dateValue = DateTimeUtils.getInstance().parseISODate(stringValue);
									if (dateValue == null) {
										throw new ExcelImportInvalidCellValueException(stringValue);
									}
								}
							}
						}
					}
				}
			} else {
				throw new ExcelImportInvalidCellValueException(getStringCellValue(cell));
			}
		}
		return dateValue;
	}

	/**
	 * Gets the Double value of a cell
	 * 
	 * @param cell
	 * @return
	 */
	private static Date getDateTimeCellValue(Cell cell, Locale locale) throws ExcelImportInvalidCellValueException {
		Date dateValue = null;
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_NUMERIC) {
			try {
				dateValue = cell.getDateCellValue();
			} catch (Exception e) {
				throw new ExcelImportInvalidCellValueException(String.valueOf(cell.getNumericCellValue()));
			}
		} else {
			if (cellType == Cell.CELL_TYPE_STRING) {
				RichTextString richTextString = cell.getRichStringCellValue();
				if (richTextString != null) {
					String stringValue = richTextString.getString();
					if (stringValue != null) {
						stringValue = stringValue.trim();
						if (!"".equals(stringValue)) {
							dateValue = DateTimeUtils.getInstance().parseGUIDateTime(stringValue, locale);
							if (dateValue == null) {
								dateValue = DateTimeUtils.getInstance().parseShortDateTime(stringValue, locale);
								if (dateValue == null) {
									dateValue = DateTimeUtils.getInstance().parseISODateTime(stringValue);
								}
							}
						}
					}
				}
			} else {
				throw new ExcelImportInvalidCellValueException(getStringCellValue(cell));
			}
		}
		return dateValue;
	}

	/**
	 * Gets the Double value of a cell
	 * 
	 * @param cell
	 * @return
	 */
	private static Boolean getBooleanCellValue(Cell cell) throws ExcelImportInvalidCellValueException {
		Boolean booleanValue = null;
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_BOOLEAN) {
			boolean boolCellValue = cell.getBooleanCellValue();
			booleanValue = new Boolean(boolCellValue);
		} else {
			if (cellType == Cell.CELL_TYPE_STRING) {
				RichTextString richTextString = cell.getRichStringCellValue();
				if (richTextString != null) {
					String stringValue = richTextString.getString();
					if (stringValue != null) {
						stringValue = stringValue.trim();
						if (!"".equals(stringValue)) {
							if ("true".equalsIgnoreCase(stringValue) || BooleanFields.TRUE_VALUE.equalsIgnoreCase(stringValue)) {
								booleanValue = new Boolean(true);
							} else {
								if ("false".equalsIgnoreCase(stringValue) || BooleanFields.FALSE_VALUE.equalsIgnoreCase(stringValue)) {
									booleanValue = new Boolean(false);
								} else {
									if (stringValue != null && !"".equals(stringValue.trim())) {
										throw new ExcelImportInvalidCellValueException(stringValue);
									}
								}
							}
						}
					}
				}
			} else {
				throw new ExcelImportInvalidCellValueException(getStringCellValue(cell));
			}
		}
		return booleanValue;
	}

	/**
	 * Get the columnIndexToFieldIDMap map from columNameToFieldIDMap and
	 * columnIndexToColumNameMap
	 * 
	 * @param columNameToFieldIDMap
	 * @param columnIndexToColumNameMap
	 * @return
	 */
	public static Map<Integer, Integer> getColumnIndexToFieldID(Map<String, Integer> columNameToFieldIDMap, Map<Integer, String> columnIndexToColumNameMap) {
		Map<Integer, Integer> columnIndexToFieldIDMap = new HashMap<Integer, Integer>();
		String columName = null;
		Integer fieldID;
		Iterator<String> itrColumNameToFieldIDMap = columNameToFieldIDMap.keySet().iterator();
		while (itrColumNameToFieldIDMap.hasNext()) {
			columName = itrColumNameToFieldIDMap.next();
			fieldID = columNameToFieldIDMap.get(columName);
			if (fieldID != null) {
				// the field was matched
				Iterator<Integer> itrColumnIndexToColumNameMap = columnIndexToColumNameMap.keySet().iterator();
				while (itrColumnIndexToColumNameMap.hasNext()) {
					Integer columnIndex = itrColumnIndexToColumNameMap.next();
					String crtColumnName = columnIndexToColumNameMap.get(columnIndex);
					if (columName.equals(crtColumnName)) {
						columnIndexToFieldIDMap.put(columnIndex, fieldID);
					}
				}
			}
		}
		return columnIndexToFieldIDMap;
	}

	/**
	 * Return a new map with reversed keys and values
	 * 
	 * @param columnIndexToFieldIDMap
	 * @return
	 */
	public static Map<Integer, Integer> reverseMap(Map<Integer, Integer> columnIndexToFieldIDMap) {
		Map<Integer, Integer> fieldIDToColumnIndexToMap = new HashMap<Integer, Integer>();
		Iterator<Map.Entry<Integer, Integer>> iterator = columnIndexToFieldIDMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = iterator.next();
			fieldIDToColumnIndexToMap.put(entry.getValue(), entry.getKey());
		}
		return fieldIDToColumnIndexToMap;
	}

	static List<Integer> getExistingWorkItemRows(Collection<TWorkItemBean> excelWorkItemsList) {
		List<Integer> existingWorkItemRows = new ArrayList<Integer>();
		List<TWorkItemBean> savedWorkItemBeansList = new ArrayList<TWorkItemBean>();
		if (excelWorkItemsList != null) {
			List<String> synopsisList = new ArrayList<String>();
			Iterator<TWorkItemBean> itrExcelWorkItemBeans = excelWorkItemsList.iterator();
			while (itrExcelWorkItemBeans.hasNext()) {
				TWorkItemBean excelWorkItemBean = itrExcelWorkItemBeans.next();
				// verify the duplicates only for new workItems, the existing
				// ones will only be modified
				if (excelWorkItemBean.getObjectID() == null) {
					if (excelWorkItemBean.getSynopsis() != null && !"".equals(excelWorkItemBean.getSynopsis())) {
						synopsisList.add(excelWorkItemBean.getSynopsis());
					}
				}
			}
			savedWorkItemBeansList = workItemDAO.loadBySynopsisList(synopsisList);
			Map<String, List<TWorkItemBean>> savedSynopsisBasedWorkItemMap = getSynopsisBasedWorkItemMap(savedWorkItemBeansList);
			itrExcelWorkItemBeans = excelWorkItemsList.iterator();
			int i = 1; // the header row is not a workItem
			while (itrExcelWorkItemBeans.hasNext()) {
				i++;
				TWorkItemBean excelWorkItemBean = itrExcelWorkItemBeans.next();
				// verify the duplicates only for new workItems, the existing
				// ones will only be modified
				if (excelWorkItemBean.getObjectID() == null) {
					String excelSynopsis = excelWorkItemBean.getSynopsis();
					if (excelSynopsis != null) {
						List<TWorkItemBean> savedWorkItemBeansWithSynopsis = savedSynopsisBasedWorkItemMap.get(excelSynopsis);
						if (savedWorkItemBeansWithSynopsis != null) {
							Iterator<TWorkItemBean> itrSavedWorkItemBeansWithSynopsis = savedWorkItemBeansWithSynopsis.iterator();
							while (itrSavedWorkItemBeansWithSynopsis.hasNext()) {
								TWorkItemBean savedWorkItemBean = itrSavedWorkItemBeansWithSynopsis.next();
								if (considerAsSame(excelWorkItemBean, savedWorkItemBean)) {
									existingWorkItemRows.add(Integer.valueOf(i));
									break;
								}
							}
						}
					}
				}
			}
		}
		return existingWorkItemRows;
	}

	/**
	 * When are two items considered as same, consequently prevent to import it
	 * again
	 * 
	 * @param excelWorkItemBean
	 * @param savedWorkItemBean
	 * @return
	 */
	private static boolean considerAsSame(TWorkItemBean excelWorkItemBean, TWorkItemBean savedWorkItemBean) {
		return EqualUtils.equal(excelWorkItemBean.getProjectID(), savedWorkItemBean.getProjectID())
				&& EqualUtils.equal(excelWorkItemBean.getListTypeID(), savedWorkItemBean.getListTypeID())
				&& EqualUtils.equal(excelWorkItemBean.getReleaseScheduledID(), savedWorkItemBean.getReleaseScheduledID());
	}

	/**
	 * 
	 * @return
	 */
	private static Map<String, List<TWorkItemBean>> getSynopsisBasedWorkItemMap(List<TWorkItemBean> workItemBeansList) {
		Map<String, List<TWorkItemBean>> synopsisBasedWorkItemMap = new HashMap<String, List<TWorkItemBean>>();
		if (workItemBeansList != null) {
			Iterator<TWorkItemBean> iterator = workItemBeansList.iterator();
			while (iterator.hasNext()) {
				TWorkItemBean workItemBean = iterator.next();
				String synopsis = workItemBean.getSynopsis();
				List<TWorkItemBean> workItemBeansWithSynopsis = synopsisBasedWorkItemMap.get(synopsis);
				if (workItemBeansWithSynopsis == null) {
					workItemBeansWithSynopsis = new ArrayList<TWorkItemBean>();
					synopsisBasedWorkItemMap.put(synopsis, workItemBeansWithSynopsis);
				}
				workItemBeansWithSynopsis.add(workItemBean);
			}
		}
		return synopsisBasedWorkItemMap;
	}

	static Map<String, String> getRowErrorsForJsonMap(Map<Integer, SortedSet<Integer>> rowErrorsMap) {
		Map<String, String> rowErrorsForJsonMap = new HashMap<String, String>();
		List<Integer> rowErrorTypes = new LinkedList<Integer>();
		rowErrorTypes.add(ExcelImportBL.WORKITEM_MORE_THAN_ONE_EXIST);
		rowErrorTypes.add(ExcelImportBL.WORKITEM_NO_EDIT_RIGHT);
		rowErrorTypes.add(ExcelImportBL.WORKITEM_NO_CREATE_RIGHT);
		for (Integer rowErrorType : rowErrorTypes) {
			SortedSet<Integer> rowErrorsForType = rowErrorsMap.get(rowErrorType);
			if (rowErrorsForType != null) {
				rowErrorsForJsonMap.put(getRowErrorMessageKey(rowErrorType), MergeUtil.getMergedString(rowErrorsForType, ", "));
			}
		}
		return rowErrorsForJsonMap;
	}

	private static String getRowErrorMessageKey(Integer gridErrorType) {
		switch (gridErrorType) {
		case ExcelImportBL.WORKITEM_MORE_THAN_ONE_EXIST:
			return "admin.actions.importExcel.err.workItemMoreThanOne";
		case ExcelImportBL.WORKITEM_NO_EDIT_RIGHT:
			return "admin.actions.importExcel.err.noEdit";
		case ExcelImportBL.WORKITEM_NO_CREATE_RIGHT:
			return "admin.actions.importExcel.err.noCreate";
		}
		return "";
	}

	/**
	 * Gets the rows with missing required fields
	 * 
	 * @param missingFieldErrorsMap
	 * @param locale
	 * @return
	 */
	static List<String> getMissingRequiredFieldErrorsForJsonMap(Map<Integer, SortedSet<Integer>> missingFieldErrorsMap, Locale locale) {
		List<String> missingFieldErrors = new ArrayList<String>();
		for (Integer fieldID : missingFieldErrorsMap.keySet()) {
			SortedSet<Integer> missingFieldRows = missingFieldErrorsMap.get(fieldID);
			if (missingFieldRows != null && !missingFieldRows.isEmpty()) {
				TFieldConfigBean fieldConfigBean = FieldRuntimeBL.getDefaultFieldConfig(fieldID, locale);
				String fieldLabel = null;
				if (fieldConfigBean != null) {
					fieldLabel = FieldRuntimeBL.getDefaultFieldConfig(fieldID, locale).getLabel();
				}
				String mergedRowsString = MergeUtil.getMergedString(missingFieldRows, ", ");
				missingFieldErrors.add(LocalizeUtil.getParametrizedString("admin.actions.importExcel.err.requiredField", new Object[] { fieldLabel,
						mergedRowsString }, locale));
			}

		}
		return missingFieldErrors;
	}

	/**
	 * Gets the grid errors
	 * 
	 * @param errorsMap
	 * @param locale
	 * @return
	 */
	static Map<Integer, List<String>> getGridErrorsForJsonMap(Map<Integer, SortedMap<Integer, SortedMap<String, ErrorData>>> errorsMap, Locale locale) {
		Map<Integer, List<String>> gridErrorsForJsonMap = new HashMap<Integer, List<String>>();
		List<Integer> gridErrorTypes = new LinkedList<Integer>();
		gridErrorTypes.add(NOT_EXISTING_ERRORS);
		gridErrorTypes.add(NOT_ALLOWED_ERRORS);
		gridErrorTypes.add(INVALID_ERRORS);
		gridErrorTypes.add(WRONG_COMPOSITE_SIZE);
		gridErrorTypes.add(NOT_EDITABLE_ERRORS);
		gridErrorTypes.add(NOT_ALLOWED_DEFAULT_VALUES_ERRORS);
		gridErrorTypes.add(WORKITEM_NOTEXIST_ERRORS);
		gridErrorTypes.add(INCONSISTENT_HIERARCHY_ERRORS);
		for (Integer gridErrorType : gridErrorTypes) {
			SortedMap<Integer, SortedMap<String, ErrorData>> gridErrorsForType = errorsMap.get(gridErrorType);
			List<String> locations = renderGridErrors(gridErrorsForType, locale);
			if (locations != null && !locations.isEmpty()) {
				gridErrorsForJsonMap.put(gridErrorType, locations);
			}
		}
		return gridErrorsForJsonMap;
	}

	/**
	 * Gets the error message by type
	 * @param gridErrorType
	 * @param locale
	 * @return
	 */
	static String getGridErrorMessage(Integer gridErrorType, Locale locale) {
		switch (gridErrorType) {
		case NOT_EXISTING_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notExistingValue", locale);
		case NOT_ALLOWED_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notAllowedValue", locale);
		case INVALID_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.invalidValue", locale);
		case WRONG_COMPOSITE_SIZE:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.wrongCompositeSize", locale);
		case NOT_EDITABLE_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notEditableValue", locale);
		case NOT_ALLOWED_DEFAULT_VALUES_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notAllowedDefaultValue", locale);
		case WORKITEM_NOTEXIST_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.workItemNotExist", locale);
		case INCONSISTENT_HIERARCHY_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.inconsitentHierarchy", locale);
		}
		return "";
	}
	
	/**
	 * Gets the solution message by type
	 * @param gridErrorType
	 * @param locale
	 * @return
	 */
	static String getGridSolutionMessage(Integer gridErrorType, Locale locale) {
		switch (gridErrorType) {
		/*case NOT_EXISTING_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notExistingValue.solution", locale);
		case NOT_ALLOWED_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notAllowedValue.solution", locale);
		case INVALID_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.invalidValue.solution", locale);
		case WRONG_COMPOSITE_SIZE:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.wrongCompositeSize.solution", locale);
		case NOT_EDITABLE_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notEditableValue.solution", locale);
		case NOT_ALLOWED_DEFAULT_VALUES_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.notAllowedDefaultValue.solution", locale);*/
		case WORKITEM_NOTEXIST_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.workItemNotExist.solution", locale);
		/*case INCONSISTENT_HIERARCHY_ERRORS:
			return LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.err.inconsitentHierarchy.solution", locale);*/
		}
		return null;
	}


	/**
	 * Render grid errors
	 * 
	 * @param errorsMap
	 * @param locale
	 * @return
	 */
	private static List<String> renderGridErrors(SortedMap<Integer, SortedMap<String, ErrorData>> errorsMap, Locale locale) {
		List<String> gridErrors = new LinkedList<String>();
		if (errorsMap != null) {
			Iterator<Integer> itrRows = errorsMap.keySet().iterator();
			while (itrRows.hasNext()) {
				Integer row = itrRows.next();
				Map<String, ErrorData> errorsOnRow = errorsMap.get(row);
				if (errorsOnRow != null) {
					Iterator<String> itrColumns = errorsOnRow.keySet().iterator();
					while (itrColumns.hasNext()) {
						String column = itrColumns.next();
						ErrorData originalErrorData = errorsOnRow.get(column);
						List<ErrorParameter> resourceParameters = new ArrayList<ErrorParameter>();
						resourceParameters.add(new ErrorParameter(row));
						resourceParameters.add(new ErrorParameter(column));
						resourceParameters.add(new ErrorParameter(originalErrorData.getResourceKey()));
						ErrorData modifiedErrorData = new ErrorData("admin.actions.importExcel.err.gridDataError", resourceParameters);
						gridErrors.add(ErrorHandlerJSONAdapter.createMessage(modifiedErrorData, locale));
					}
				}
			}
		}
		return gridErrors;
	}

	/**
	 * Render grid/row errors
	 * 
	 * @param errorsMap
	 * @param fieldIDToColumnIndexMap
	 * @param locale
	 * @param actionSupport
	 * @return
	 */
	static List<String> renderRowErrors(SortedMap<Integer, List<ErrorData>> errorsMap, Map<Integer, Integer> fieldIDToColumnIndexMap, Locale locale) {
		Map<Integer, TFieldConfigBean> fieldConfigsMap = FieldRuntimeBL.getLocalizedDefaultFieldConfigsMap(locale);
		List<String> rowErrors = new LinkedList<String>();
		for (Map.Entry<Integer, List<ErrorData>> entry : errorsMap.entrySet()) {
			Integer row = entry.getKey();
			List<ErrorData> errorsOnRow = entry.getValue();
			if (errorsOnRow != null) {
				for (ErrorData errorData : errorsOnRow) {
					// the fieldID if present was set in
					// FieldManagerRT.validate()
					Integer fieldID = errorData.getFieldID();
					// field id is not needed as extra resource parameter
					errorData.setFieldID(null);
					String validationError = ErrorHandlerJSONAdapter.createFieldMessage(errorData, fieldConfigsMap, locale);
					List<ErrorParameter> resourceParameters = new ArrayList<ErrorParameter>();
					// row always known
					resourceParameters.add(new ErrorParameter(row));
					ErrorData modifiedErrorData = null;
					if (fieldID != null && fieldIDToColumnIndexMap.get(fieldID) != null) {
						// column known only if field is known and column is in
						// the excel sheet
						resourceParameters.add(new ErrorParameter(ExcelFieldMatchBL.colNumericToLetter(fieldIDToColumnIndexMap.get(fieldID))));
						modifiedErrorData = new ErrorData("admin.actions.importExcel.err.validationErrorGrid", resourceParameters);
					} else {
						modifiedErrorData = new ErrorData("admin.actions.importExcel.err.validationErrorRow", resourceParameters);
					}
					resourceParameters.add(new ErrorParameter(validationError));
					rowErrors.add(ErrorHandlerJSONAdapter.createMessage(modifiedErrorData, locale));
				}
			}
		}
		return rowErrors;
	}
}
