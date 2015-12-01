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


package com.aurel.track.exchange.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.exchange.ImportCounts;
import com.aurel.track.exchange.ImportJSON;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.TreeNode;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Rendering the invalid value handling and importing from excel file
 * @author Tamas
 *
 */
public class ExcelImportAction extends ActionSupport
	implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ExcelImportAction.class);
	private static String mappingFileName = "mapping";
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
    private TPersonBean personBean;
	private Integer personID;
	private Locale locale;
	private Integer selectedSheet;
	private String fileName;
	/**
	 * reload the invalid value handling/default values map
	 */
	private boolean reload;
	private Map<Integer, Integer> invalidValueHandlingMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> defaultValuesMap = new HashMap<Integer, Integer>();
	private Map<String, Boolean> overwriteMap = new HashMap<String, Boolean>();


	@Override
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
        personBean =  (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
	}

	/**
	 * Render the invalid value handling page
	 */
	@Override
	public String execute() {
		Map<Integer, List<ILabelBean>> possibleValues = ExcelImportBL.getPossibleValues(personID,
				defaultValuesMap.get(SystemFields.INTEGER_PROJECT),
				defaultValuesMap.get(SystemFields.INTEGER_ISSUETYPE), locale);
		List<IntegerStringBean> fieldBeans = ExcelImportBL.prepareFieldLabels(locale);
		String invalidValueHandlingString = null;
		if (reload) {
			List<Integer> fieldIDsToUpdate = ExcelImportBL.getFieldsToUpdate();
			invalidValueHandlingString = ExcelImportJSON.getExcelInvalidHandlingUpdateJSON(fieldIDsToUpdate, possibleValues, defaultValuesMap);
		} else {
            List<Integer> accessRights = new LinkedList<Integer>();
            accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.PROJECTADMIN));
            accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.MODIFYANYTASK));
            accessRights.add(Integer.valueOf(AccessBeans.AccessFlagIndexes.CREATETASK));
            int[] rights = GeneralUtils.createIntArrFromIntegerList(accessRights);
            List<TreeNode> projectTree = ProjectBL.getProjectNodesByRightEager(
                    false, personBean, false, rights, true, true);
			invalidValueHandlingString = ExcelImportJSON.getExcelInvalidHandlingFirstTimeJSON(fieldBeans,
						ExcelImportBL.getInvalidValueHandlingList(locale),
						invalidValueHandlingMap, possibleValues, projectTree, defaultValuesMap);
		}
		JSONUtility.encodeJSON(servletResponse, invalidValueHandlingString);
		return null;
	}

	/**
	 * Execute the import from excel
	 * @return
	 */
	public String excelImport() {
		String excelMappingsDirectory = AttachBL.getExcelImportDirBase() + personID;
		Workbook workbook = ExcelFieldMatchBL.loadWorkbook(excelMappingsDirectory, fileName);
		Set<Integer> lastSavedIdentifierFieldIDIsSet = null;
		Map<String, Integer> columNameToFieldIDMap = null;
		try {
			File file = new File(excelMappingsDirectory, mappingFileName);
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			columNameToFieldIDMap = (Map<String,Integer>)objectInputStream.readObject();
			lastSavedIdentifierFieldIDIsSet = (Set<Integer>)objectInputStream.readObject();
			objectInputStream.close();
		} catch (FileNotFoundException e) {
			LOGGER.warn("Creating the input stream for mapping failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.warn("Saving the mapping failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			LOGGER.warn("Class not found for  the mapping " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}

		if (workbook==null) {
			JSONUtility.encodeJSON(servletResponse,
					ImportJSON.importErrorMessageJSON(ImportJSON.ERROR_CODES.ERROR_MESSAGE, getText("admin.actions.importTp.err.uploadAgain"), true));
			return null;
		}
		if (columNameToFieldIDMap==null) {
			//for example the sheet contains no columns at all
			columNameToFieldIDMap = new HashMap<String, Integer>();
		}
		try {
			Map<Integer, String> columnIndexToColumNameMap = ExcelFieldMatchBL.getFirstRowHeaders(workbook, selectedSheet);
			Map<Integer, Integer> columnIndexToFieldIDMap = ExcelImportBL.getColumnIndexToFieldID(columNameToFieldIDMap, columnIndexToColumNameMap);
			Map<Integer, Integer> fieldIDToColumnIndexMap = ExcelImportBL.reverseMap(columnIndexToFieldIDMap);
			List<ErrorData> errorDataList = ExcelImportBL.validateRequiredColumns(workbook, selectedSheet,
					fieldIDToColumnIndexMap, lastSavedIdentifierFieldIDIsSet, invalidValueHandlingMap, defaultValuesMap, locale);
			if (!errorDataList.isEmpty()) {
				//required columns are missing: do not disable the Finish button and do not delete
				//the file because it may be solved by stepping back and forth in the wizard
				JSONUtility.encodeJSON(servletResponse, ImportJSON.importErrorMessageListJSON(ErrorHandlerJSONAdapter.handleErrorList(errorDataList,
						locale), ImportJSON.ERROR_CODES.ERROR_MESSAGES, false));
				return null;
			} else {
				//delete the file for this case because it
				//either results in a error which should be resolved in the excel file
				//consequently a new upload cannot be avoided before re-import
				//or everything is fine and in this case no new import is needed
				//with any other return a
				//grid errors
				Map<Integer, SortedMap<Integer, SortedMap<String, ErrorData>>> gridErrorsMap =
					new HashMap<Integer, SortedMap<Integer, SortedMap<String, ErrorData>>>();
				//row errors
				Map<Integer, SortedSet<Integer>> rowErrorsMap = new HashMap<Integer, SortedSet<Integer>>();
				Map<Integer, SortedSet<Integer>> requiredFieldErrorsMap = new HashMap<Integer, SortedSet<Integer>>();
				Map<Integer, Map<Integer, List<Integer>>> rowNoToPseudoFieldsOriginal = new HashMap<Integer, Map<Integer,List<Integer>>>();
				Map<Integer, Map<Integer, List<Integer>>>  rowNoToPseudoFieldsExcel = new HashMap<Integer, Map<Integer,List<Integer>>>();
				Map<Integer, Integer> rowToParentRow = new HashMap<Integer, Integer>();
				SortedMap<Integer, TWorkItemBean> workItemBeansMap = ExcelImportBL.getAndValidateGridData(workbook,
						selectedSheet, personID, locale, columnIndexToFieldIDMap, fieldIDToColumnIndexMap,
						lastSavedIdentifierFieldIDIsSet,
						defaultValuesMap, invalidValueHandlingMap, rowNoToPseudoFieldsOriginal, rowNoToPseudoFieldsExcel,
						gridErrorsMap, rowErrorsMap, requiredFieldErrorsMap, rowToParentRow);
				Collection<TWorkItemBean> workItemBeans = workItemBeansMap.values();
				if (gridErrorsMap.isEmpty() && rowErrorsMap.isEmpty() && requiredFieldErrorsMap.isEmpty()) {
					List<Integer> alreadyExistingRows = ExcelImportBL.getExistingWorkItemRows(workItemBeans);
					//already existing rows with the same synopsis, project, issueType and release scheduled
					//(independently of the identifierFieldIDs) to avoid importing the same new issues more
					//(only the not found i.e. new issues are tested)
					if (!alreadyExistingRows.isEmpty()) {
						JSONUtility.encodeJSON(servletResponse,
								ImportJSON.importErrorMessageJSON(ImportJSON.ERROR_CODES.ERROR_MESSAGE,
								LocalizeUtil.getParametrizedString("admin.actions.importExcel.err.existingRows",
									new String[]{MergeUtil.getMergedString(alreadyExistingRows, ", ")}, locale), true));
						return null;
					} else {
						Set<Integer> presentFieldIDs =  ExcelImportBL.getPresentFields(columNameToFieldIDMap);
						presentFieldIDs.addAll(FieldsManagerRT.getRequiredSystemFieldsList());
						//the explicit change of this field is not allowed
						presentFieldIDs.remove(SystemFields.LASTMODIFIEDDATE);
						presentFieldIDs.remove(SystemFields.CREATEDATE);
						Map<Integer, Map<Integer, Map<Integer, TFieldConfigBean>>> projectsIssueTypesFieldConfigsMap =
							FieldRuntimeBL.getFieldConfigsForWorkItemBeans(workItemBeans, presentFieldIDs, locale);
						Map<Integer, Map<Integer, Map<String, Object>>> projectsIssueTypesFieldSettingsMap =
							FieldRuntimeBL.getFieldSettingsForFieldConfigs(projectsIssueTypesFieldConfigsMap);
						Map<Integer, WorkItemContext> existingIssueContextsMap = FieldsManagerRT.createImportContext(
								workItemBeans, presentFieldIDs, projectsIssueTypesFieldConfigsMap,
								projectsIssueTypesFieldSettingsMap, null, null, personID, locale);
						SortedMap<Integer, List<ErrorData>> validationErrorsMap = FieldsManagerRT.validateWorkItems(workItemBeans,
								presentFieldIDs, existingIssueContextsMap,
								projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap, personID, locale);
                        //validation errors: either grid (workItem and field) or row (workItem) level errors. There is a chance to resolve the problems
                        //without modifying the excel file: for ex. by setting further/other default values
						if (!validationErrorsMap.isEmpty()) {
							List<String> rowErrors = ExcelImportBL.renderRowErrors(validationErrorsMap, fieldIDToColumnIndexMap, locale);
							JSONUtility.encodeJSON(servletResponse,
									ImportJSON.importErrorMessageListJSON(rowErrors, ImportJSON.ERROR_CODES.ERROR_MESSAGES, false));
							return null;
						} else {
								if (overwriteMap==null) {
									overwriteMap = new HashMap<String, Boolean>();
								}
								SortedMap<Integer, SortedMap<Integer, Map<Integer, Object>>> confictsMap =
										ExcelImportBL.conflictResolutionWorkItems(workItemBeans, presentFieldIDs,
										existingIssueContextsMap, projectsIssueTypesFieldConfigsMap,
									columnIndexToColumNameMap, fieldIDToColumnIndexMap, personID, locale, overwriteMap);
							if (confictsMap!=null && !confictsMap.isEmpty()) {
								//render conflicts
								//do not disable Finish and do not delete the file instead resolve the conflicts and import again
								JSONUtility.encodeJSON(servletResponse, ExcelImportJSON.getExcelConflictsJSON(confictsMap, locale, false));
								return null;
							} else {
								//no conflicts or conflict handling is set (overwriteMap was submitted)
								List<ErrorData> errorsList = new ArrayList<ErrorData>();
								ImportCounts importCounts = FieldsManagerRT.saveWorkItems(workItemBeansMap,
										presentFieldIDs, existingIssueContextsMap,
										projectsIssueTypesFieldConfigsMap, projectsIssueTypesFieldSettingsMap,
										rowNoToPseudoFieldsOriginal, rowNoToPseudoFieldsExcel, rowToParentRow,
										personID, locale, errorsList);
								if (!errorsList.isEmpty()) {
									JSONUtility.encodeJSON(servletResponse, ImportJSON.importErrorMessageListJSON(ErrorHandlerJSONAdapter.handleErrorList(errorDataList,
											locale), ImportJSON.ERROR_CODES.ERROR_MESSAGES, true));
									return null;
								}
								JSONUtility.encodeJSON(servletResponse, ImportJSON.importMessageJSON(true,
										LocalizeUtil.getParametrizedString("admin.actions.importExcel.message.importResult",
											new String[] {Integer.valueOf(importCounts.getNoOfCreatedIssues()).toString(),
												Integer.valueOf(importCounts.getNoOfUpdatedIssues()).toString()}, locale), true, locale));
                                //successful import, delete the file
                                File file = new File(excelMappingsDirectory, fileName);
                                file.delete();
								return null;
							}
						}
					}
				} else {
					//grid or row errors
					Map<Integer, List<String>> gridErrorsForJsonMap = null;
					if (!gridErrorsMap.isEmpty()) {
						gridErrorsForJsonMap = ExcelImportBL.getGridErrorsForJsonMap(gridErrorsMap, locale);
					}
					Map<String, String> rowErrorsForJsonMap = null;
					if (!rowErrorsMap.isEmpty()) {
						rowErrorsForJsonMap = ExcelImportBL.getRowErrorsForJsonMap(rowErrorsMap);
					}
					List<String> requiredFieldErrorsList = null;
					if (!requiredFieldErrorsMap.isEmpty()) {
						requiredFieldErrorsList = ExcelImportBL.getMissingRequiredFieldErrorsForJsonMap(requiredFieldErrorsMap, locale);
					}
					JSONUtility.encodeJSON(servletResponse, ExcelImportJSON.getExcelWrongGridValuesJSON(
							gridErrorsForJsonMap, rowErrorsForJsonMap, requiredFieldErrorsList, locale, true));
				}
			}
		} catch (Exception e) {
			addActionError(getText("admin.actions.importTp.err.failed"));
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			JSONUtility.encodeJSON(servletResponse, ImportJSON.importErrorMessageJSON(ImportJSON.ERROR_CODES.ERROR_MESSAGE,
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importTp.err.failed", locale), true));
		}
		//delete the uploaded excel file
		return null;
	}

	/**
	 * The following method remove ISSUE No. from identifier mappings.
	 *  Identifier mapping: Column to issue field
	 */


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<Integer, Integer> getInvalidValueHandlingMap() {
		return invalidValueHandlingMap;
	}

	public void setInvalidValueHandlingMap(
			Map<Integer, Integer> invalidValueHandlingMap) {
		this.invalidValueHandlingMap = invalidValueHandlingMap;
	}

	public Map<Integer, Integer> getDefaultValuesMap() {
		return defaultValuesMap;
	}

	public void setDefaultValuesMap(Map<Integer, Integer> defaultValuesMap) {
		this.defaultValuesMap = defaultValuesMap;
	}

	public Integer getSelectedSheet() {
		return selectedSheet;
	}

	public void setSelectedSheet(Integer selectedSheet) {
		this.selectedSheet = selectedSheet;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, Boolean> getOverwriteMap() {
		return overwriteMap;
	}

	public void setOverwriteMap(Map<String, Boolean> overwriteMap) {
		this.overwriteMap = overwriteMap;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}


}
