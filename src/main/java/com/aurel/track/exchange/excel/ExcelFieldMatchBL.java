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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.role.FieldsRestrictionsToRoleBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldBL;
import com.aurel.track.admin.customize.treeConfig.field.FieldConfigBL;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TRoleFieldBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;


public class ExcelFieldMatchBL {

	private static final Logger LOGGER = LogManager.getLogger(ExcelFieldMatchBL.class);

	public static int LOCAL_PARENT_PSEUDO_COLUMN = -1;

	/**
	 * Get the workbook and returns its sheets
	 * @param excelMappingsDirectory
     * @param fileName
	 * @return Map<SheetID, SheetName>
	 */
	static Workbook loadWorkbook(String excelMappingsDirectory, String fileName) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(excelMappingsDirectory, fileName));
		} catch (FileNotFoundException e) {
			LOGGER.warn("Loading the workbook from directory " +excelMappingsDirectory +
					" and file " + fileName + "  failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
		try {
			if (fileName.endsWith("xls") || fileName.endsWith("XLS")) {
				return new HSSFWorkbook(inputStream);
			} else {
				if (fileName.endsWith("xlsx") || fileName.endsWith("XLSX")) {
					return new XSSFWorkbook(inputStream);
				}
			}
		} catch (IOException e) {
			LOGGER.warn("Getting the excel sheets failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Get the workbook and returns its sheets
	 * @param workbook
	 * @return Map<SheetID, SheetName>
	 */
	static List<IntegerStringBean> loadSheetNames(Workbook workbook) {
		List<IntegerStringBean> sheetList = new ArrayList<IntegerStringBean>();
		if (workbook!=null) {
			for (int i = 0; i<workbook.getNumberOfSheets(); i++){
				sheetList.add(new IntegerStringBean(workbook.getSheetName(i), Integer.valueOf(i)));
			}
		}
		return sheetList;
	}

	/**
	 * Returns the first row headers (field names) mapped to the column indexes
	 * @return  Map<ColumnNumber, ColumnHeader>
	 */
	static SortedMap<Integer, String> getFirstRowHeaders(Workbook hSSFWorkbook, Integer sheetID) {
		SortedMap<Integer, String> firstRowMap = new TreeMap<Integer, String>();
		if (hSSFWorkbook==null || sheetID==null) {
			return firstRowMap;
		}
		//first search for duplicate columns
		Set<String> sameColumnNames = new HashSet<String>();
		Set<String> columnNames = new HashSet<String>();
		Sheet sheet = hSSFWorkbook.getSheetAt(sheetID.intValue());
		Row firstRow = sheet.getRow(0);
		if (firstRow!=null) {
			for (Cell cell : firstRow) {
				String columnHeader = ExcelImportBL.getStringCellValue(cell);
				if (columnHeader!=null && !"".equals(columnHeader)) {
					if (columnNames.contains(columnHeader)) {
						sameColumnNames.add(columnHeader);
					} else {
						columnNames.add(columnHeader);
					}
				}
			}
		}
		sheet  = hSSFWorkbook.getSheetAt(sheetID.intValue());
		firstRow = sheet.getRow(0);
		if (firstRow!=null) {
			for (Cell cell: firstRow) {
				String columnHeader = ExcelImportBL.getStringCellValue(cell);
				if (columnHeader!=null && !"".equals(columnHeader)) {
					if (sameColumnNames.contains(columnHeader)) {
						//for duplicate columns add also the column index
						columnHeader += " (" + cell.getColumnIndex() + ")";
					}
					firstRowMap.put(Integer.valueOf(cell.getColumnIndex()), columnHeader);
				}
			}
		}
		return firstRowMap;
	}

	/**
	 * Returns the first row of a sheet where there is the fields name
	 * @return  Map<ColumnNumber, FieldLabelName>
	 */
	static SortedMap<Integer, String> getFirstRowNumericToLetter(Workbook hSSFWorkbook, Integer sheetID) {
		SortedMap<Integer, String> firstRowMap = new TreeMap<Integer, String>();
		if (hSSFWorkbook==null || sheetID==null) {
			return firstRowMap;
		}
		Sheet sheet = hSSFWorkbook.getSheetAt(sheetID.intValue());
		Row firstRow = sheet.getRow(0);
		if (firstRow!=null) {
			for (Cell cell : firstRow) {
				firstRowMap.put(Integer.valueOf(cell.getColumnIndex()), colNumericToLetter(cell.getColumnIndex()));
			}
		}
		return firstRowMap;
	}

	/**
	 * Get the excel letter for the column
	 * @param colNum
	 * @return
	 */
	static String colNumericToLetter(int colNum) {
		StringBuffer sb = new StringBuffer();
		int cycleNum = colNum / 26;
		int withinNum = colNum - (cycleNum * 26);
		if (cycleNum > 0) {
			sb.append((char) ((cycleNum - 1) + 'A'));
		}
		sb.append((char) (withinNum + 'A'));
		return (sb.toString());
	}

	/**
	 * Get the localized TFieldConfigBeans
	 * @param locale
	 * @return
	 */
	static List<IntegerStringBean> getFieldConfigs(Integer personID, Locale locale) {
		List<TFieldConfigBean> fieldConfigsList = FieldRuntimeBL.getLocalizedDefaultFieldConfigs(locale);
		List<IntegerStringBean> matchableFieldsList = new LinkedList<IntegerStringBean>();
		//null fieldID: by selecting the empty entry the field be ignored
		matchableFieldsList.add(new IntegerStringBean("-", 0));
		for (TFieldConfigBean fieldConfigBean : fieldConfigsList) {
			Integer fieldID = fieldConfigBean.getField();
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			if (fieldTypeRT!=null && fieldTypeRT.mightMatchExcelColumn()) {
				matchableFieldsList.add(new IntegerStringBean(fieldConfigBean.getLabel(), fieldID));
			}
		}
		List<Integer> fieldIDs = new LinkedList<Integer>();
		fieldIDs.add(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
		Map<Integer, Integer> fieldRestrictions = AccessBeans.getFieldRestrictions(personID, null, null, fieldIDs, false);
		Integer watchersFlag = fieldRestrictions.get(FieldsRestrictionsToRoleBL.PSEUDO_COLUMNS.WATCHERS);
		if (watchersFlag==null || watchersFlag.intValue()==TRoleFieldBean.ACCESSFLAG.READ_WRITE) {
			matchableFieldsList.add(new IntegerStringBean(
					LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.INFORMANT_LIST, locale),
					TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST));
			matchableFieldsList.add(new IntegerStringBean(
					LocalizeUtil.getLocalizedTextFromApplicationResources(TReportLayoutBean.PSEUDO_COLUMN_LABELS.CONSULTANT_LIST, locale),
					TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST));
		}
		matchableFieldsList.add(new IntegerStringBean(
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.actions.importExcel.fieldMatch.hierarchyColumn", locale),
				LOCAL_PARENT_PSEUDO_COLUMN));
		Collections.sort(matchableFieldsList);
		return matchableFieldsList;
	}

	/**
	 * Fields which might be used as identifier columns
	 * the other are not implemented at the persistence level
	 * @return
	 */
	static Set<Integer> getPossibleIdentifierFields() {
		Set<Integer> identifierFieldSet = new HashSet<Integer>();
		identifierFieldSet.add(SystemFields.INTEGER_PROJECT);
		identifierFieldSet.add(SystemFields.INTEGER_RELEASESCHEDULED);
		identifierFieldSet.add(SystemFields.INTEGER_RELEASENOTICED);
		identifierFieldSet.add(SystemFields.INTEGER_MANAGER);
		identifierFieldSet.add(SystemFields.INTEGER_RESPONSIBLE);
		identifierFieldSet.add(SystemFields.INTEGER_ORIGINATOR);
		identifierFieldSet.add(SystemFields.INTEGER_CHANGEDBY);
		identifierFieldSet.add(SystemFields.INTEGER_ISSUETYPE);
		identifierFieldSet.add(SystemFields.INTEGER_STATE);
		identifierFieldSet.add(SystemFields.INTEGER_PRIORITY);
		identifierFieldSet.add(SystemFields.INTEGER_SEVERITY);
		identifierFieldSet.add(SystemFields.INTEGER_SYNOPSIS);
		identifierFieldSet.add(SystemFields.INTEGER_CREATEDATE);
		identifierFieldSet.add(SystemFields.INTEGER_STARTDATE);
		identifierFieldSet.add(SystemFields.INTEGER_ENDDATE);
		identifierFieldSet.add(SystemFields.INTEGER_ISSUENO);
		identifierFieldSet.add(SystemFields.INTEGER_BUILD);
		identifierFieldSet.add(SystemFields.INTEGER_SUBMITTEREMAIL);
		identifierFieldSet.add(SystemFields.INTEGER_SUPERIORWORKITEM);
		return identifierFieldSet;
	}

	/**
	 * Fields which if mapped are mandatory identifier fields
	 * (their values can't be changed)
	 * @return
	 */
	static Set<Integer> getMandatoryIdentifierFields() {
		Set<Integer> mandatoryIdentifierFields = new HashSet<Integer>();
		mandatoryIdentifierFields.add(SystemFields.INTEGER_ORIGINATOR);
		//mandatoryIdentifierFields.add(SystemFields.INTEGER_CREATEDATE);
		mandatoryIdentifierFields.add(SystemFields.INTEGER_ISSUENO);
		return mandatoryIdentifierFields;
	}

	/**
	 * Get a label based map with the localized default field configs
	 * @param locale
	 * @return
	 */
	private static Map<String, TFieldConfigBean> getLocalizedDefaultFieldConfigsMap(Locale locale) {
		return getLabelBasedFieldConfigsMap(FieldRuntimeBL.getLocalizedDefaultFieldConfigs(locale));
	}

	/**
	 * Get a label based map with the not localized default field configs
	 * @return
	 */
	private static Map<String, TFieldConfigBean> getDefaultFieldConfigsMap() {
		return getLabelBasedFieldConfigsMap(FieldConfigBL.loadDefault());
	}

	/**
	 * Get a label based map of the field configs
	 * @param fieldConfigBeansList
	 * @return
	 */
	private static Map<String, TFieldConfigBean> getLabelBasedFieldConfigsMap(
			List<TFieldConfigBean> fieldConfigBeansList) {
		Map<String, TFieldConfigBean> fieldConfigBeansMap = new HashMap<String, TFieldConfigBean>();
		if (fieldConfigBeansList!=null) {
			Iterator<TFieldConfigBean> iterator = fieldConfigBeansList.iterator();
			while (iterator.hasNext()) {
				TFieldConfigBean fieldConfigBean = iterator.next();
				String configLabel = fieldConfigBean.getLabel();
				if (configLabel!=null) {
					fieldConfigBeansMap.put(configLabel, fieldConfigBean);
				}
			}
		}
		return fieldConfigBeansMap;
	}

	/**
	 * Get a label based map of all defined fields
	 * @return
	 */
	private static Map<String, TFieldConfigBean> getFieldNameBasedFieldConfigsMap() {
		Map<String, TFieldConfigBean> fieldConfigBeansMap = new HashMap<String, TFieldConfigBean>();
		Map<Integer, TFieldBean> fieldMap = GeneralUtils.createMapFromList(FieldBL.loadAll());
		List<TFieldConfigBean> fieldConfigBeansList = FieldConfigBL.loadDefault();
		Iterator<TFieldConfigBean> iterator = fieldConfigBeansList.iterator();
		while (iterator.hasNext()) {
			TFieldConfigBean fieldConfigBean = iterator.next();
			TFieldBean fieldBean = fieldMap.get(fieldConfigBean.getField());
			if (fieldBean!=null) {
				fieldConfigBeansMap.put(fieldBean.getName(), fieldConfigBean);
			}
		}
		return fieldConfigBeansMap;
	}

	static Map<String, Integer> prepareBestMatchByLabel(Set<String> excelColumnNames,
			Map<String, Integer> previousMappings, Locale locale) {
		if (excelColumnNames==null) {
			return previousMappings;
		}
		//do not match the previously matched columns
		excelColumnNames.removeAll(previousMappings.keySet());

		if (!excelColumnNames.isEmpty()) {
			//match by localized config labels
			addMatch(excelColumnNames, getLocalizedDefaultFieldConfigsMap(locale), previousMappings);
		}
		if (!excelColumnNames.isEmpty()) {
			//match by not localized config labels
			addMatch(excelColumnNames, getDefaultFieldConfigsMap(), previousMappings);
		}
		if (!excelColumnNames.isEmpty()) {
			//match by field names
			addMatch(excelColumnNames, getFieldNameBasedFieldConfigsMap(), previousMappings);
		}
		return previousMappings;
	}

	/**
	 * Match by fieldConfigBean labels
	 * @param excelColumnNames
	 * @param fieldConfigsMap
	 * @param previousMappings
	 */
	private static void addMatch(Set<String> excelColumnNames,
			Map<String, TFieldConfigBean> fieldConfigsMap,
			Map<String, Integer> previousMappings) {
		if (!excelColumnNames.isEmpty()) {
			Iterator<String> itrExcelColumNames = excelColumnNames.iterator();
			while (itrExcelColumNames.hasNext()) {
				String columName = itrExcelColumNames.next();
				if (fieldConfigsMap.containsKey(columName)) {
					TFieldConfigBean fieldConfigBean = fieldConfigsMap.get(columName);
					if (fieldConfigBean!=null) {
						previousMappings.put(columName, fieldConfigBean.getObjectID());
						itrExcelColumNames.remove();
					}
				}
			}
		}
	}

	/**
	 * Gets a columnIndex to fieldID map
	 * @param columNameToFieldIDMap
	 * @param columnIndexToColumNameMap
	 * @return
	 */
	static Map<Integer, Integer> getColumnIndexToFieldIDMap(Map<String, Integer> columNameToFieldIDMap,
			SortedMap<Integer, String> columnIndexToColumNameMap) {
		Map<Integer, Integer> columnIndexToFieldIDMap = new HashMap<Integer, Integer>();
		Iterator<Integer> iterator = columnIndexToColumNameMap.keySet().iterator();
		while (iterator.hasNext()) {
			Integer columnIndex = iterator.next();
			String columnName = columnIndexToColumNameMap.get(columnIndex);
			if (columNameToFieldIDMap.containsKey(columnName)) {
				columnIndexToFieldIDMap.put(columnIndex, columNameToFieldIDMap.get(columnName));
			}
		}
		return columnIndexToFieldIDMap;
	}

	/**
	 * Gets a columnName to fieldID map
	 * @param columnIndexToFieldIDMap
	 * @param columnIndexToColumNameMap
	 * @return
	 */
	static Map<String, Integer> getColumnNameToFieldIDMap(Map<Integer, Integer> columnIndexToFieldIDMap,
			SortedMap<Integer, String> columnIndexToColumNameMap) {
		Map<String, Integer> columNameToFieldIDMap = new HashMap<String, Integer>();
		Iterator<Integer> iterator = columnIndexToColumNameMap.keySet().iterator();
		while (iterator.hasNext()) {
			Integer columnIndex = iterator.next();
			String columnName = columnIndexToColumNameMap.get(columnIndex);
			if (columnIndexToFieldIDMap.containsKey(columnIndex)) {
				Integer fieldID = columnIndexToFieldIDMap.get(columnIndex);
				if (fieldID!=null && fieldID.intValue()!=0) {
					columNameToFieldIDMap.put(columnName, columnIndexToFieldIDMap.get(columnIndex));
				}
			}
		}
		return columNameToFieldIDMap;
	}
}
