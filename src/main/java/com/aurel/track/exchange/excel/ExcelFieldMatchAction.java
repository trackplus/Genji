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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action for matching the excel columns to Genji fields
 * @author Tamas
 *
 */
public class ExcelFieldMatchAction extends ActionSupport
	implements Preparable, SessionAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ExcelFieldMatchAction.class);
	private static String mappingFileName = "mapping";
	private static String NEXT = "next";
	private Map<String, Object> session;
	private HttpServletResponse servletResponse;
	private Integer personID;
	private Locale locale;
	private Integer selectedSheet;
	private String excelMappingsDirectory;
	private String fileName;
	private Map<Integer, Integer> columnIndexToFieldIDMap;
	//(typically workItemID or title and something)
	private Map<Integer, Boolean> columnIndexIsIdentifierMap;
	private Workbook workbook;

	@Override
	public void prepare() throws Exception {
		locale = (Locale)session.get(Constants.LOCALE_KEY);
		personID = ((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
		excelMappingsDirectory = AttachBL.getExcelImportDirBase() + personID;
	}

	/**
	 * Render the field match: first time and after a sheet change
	 */
	@Override
	public String execute() {
		File fileOnDisk = new File(excelMappingsDirectory, fileName);
		workbook = ExcelFieldMatchBL.loadWorkbook(excelMappingsDirectory, fileName);
		if (workbook==null) {
			JSONUtility.encodeJSON(servletResponse,
					JSONUtility.encodeJSONFailure(getText("admin.actions.importExcel.err.noWorkbook")), false);
			fileOnDisk.delete();
			return null;
		}
		if (selectedSheet==null) {
			//first rendering (not submit because of sheet change)
			selectedSheet = Integer.valueOf(0);
		}
		//get the previous field mappings
		Map<String, Integer> columNameToFieldIDMap = null;
		Set<Integer> lastSavedIdentifierFieldIDIsSet = null;
		try {
			FileInputStream fis = new FileInputStream(new File(excelMappingsDirectory, mappingFileName));

			ObjectInputStream objectInputStream = new ObjectInputStream(fis);
			columNameToFieldIDMap = (Map<String,Integer>)objectInputStream.readObject();
			lastSavedIdentifierFieldIDIsSet = (Set<Integer>)objectInputStream.readObject();
			objectInputStream.close();
		} catch (FileNotFoundException e) {
			LOGGER.info("Creating the input stream for mapping failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.warn("Saving the mapping failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			LOGGER.warn("Class not found for  the mapping " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		//get the column index to header names from the excel sheet
		SortedMap<Integer, String> columnIndexToColumNameMap = ExcelFieldMatchBL.getFirstRowHeaders(workbook, selectedSheet);

		SortedSet<String> excelColumnNames = new TreeSet<String>();
		excelColumnNames.addAll(columnIndexToColumNameMap.values());
		//prepare the best field matching
		if (columNameToFieldIDMap==null) {
			columNameToFieldIDMap = new HashMap<String, Integer>();
		}
		ExcelFieldMatchBL.prepareBestMatchByLabel(excelColumnNames, columNameToFieldIDMap, locale);
		columnIndexToFieldIDMap = ExcelFieldMatchBL.getColumnIndexToFieldIDMap(columNameToFieldIDMap, columnIndexToColumNameMap);
		columnIndexIsIdentifierMap = new HashMap<Integer, Boolean>();
		//the saved identifier columns
		if (lastSavedIdentifierFieldIDIsSet!=null && !lastSavedIdentifierFieldIDIsSet.isEmpty()) {
			for (Integer columnIndex : columnIndexToFieldIDMap.keySet()) {
				Integer fieldID = columnIndexToFieldIDMap.get(columnIndex);
				columnIndexIsIdentifierMap.put(columnIndex,
						new Boolean(lastSavedIdentifierFieldIDIsSet.contains(fieldID)));
			}
		}
		//if issueNo is present it is always identifier (first time it should be preselected and any time when mapped also preselected)
		if (columnIndexToFieldIDMap.values().contains(SystemFields.INTEGER_ISSUENO)) {
			for (Integer columnIndex : columnIndexToFieldIDMap.keySet()) {
				Integer fieldID = columnIndexToFieldIDMap.get(columnIndex);
				if (SystemFields.INTEGER_ISSUENO.equals(fieldID)) {
					columnIndexIsIdentifierMap.put(columnIndex, new Boolean(true));
				}
			}
		}
		List<IntegerStringBean> sheetNames = ExcelFieldMatchBL.loadSheetNames(workbook);
		List<IntegerStringBean> matchableFieldsList = ExcelFieldMatchBL.getFieldConfigs(personID, locale);
		Map<Integer, String> columnIndexNumericToLetter = ExcelFieldMatchBL.getFirstRowNumericToLetter(workbook, selectedSheet);
		Set<Integer> possibleIdentifiersSet = ExcelFieldMatchBL.getPossibleIdentifierFields();
		Set<Integer> mandatoryIdentifiersSet = ExcelFieldMatchBL.getMandatoryIdentifierFields();
		JSONUtility.encodeJSON(servletResponse,
				ExcelImportJSON.getExcelFieldMatcherJSON(fileName, selectedSheet, sheetNames, matchableFieldsList,
						columnIndexToColumNameMap, columnIndexNumericToLetter,
						columnIndexToFieldIDMap, columnIndexIsIdentifierMap,
						possibleIdentifiersSet, mandatoryIdentifiersSet), false);
		return null;
	}

	/**
	 * Save the field mappings for the next use
	 * @return
	 */
	public String save() {
		workbook = ExcelFieldMatchBL.loadWorkbook(excelMappingsDirectory, fileName);
		SortedMap<Integer, String> columnIndexToColumNameMap = ExcelFieldMatchBL.getFirstRowHeaders(workbook, selectedSheet);
		Map<String, Integer> columNameToFieldIDMap = ExcelFieldMatchBL.getColumnNameToFieldIDMap(columnIndexToFieldIDMap, columnIndexToColumNameMap);
		Set<Integer> lastSavedIdentifierFieldIDIsSet = new HashSet<Integer>();
		//add the explicitly selected field identifiers
		if (columnIndexIsIdentifierMap!=null) {
			//at least a field was set as unique identifier
			Iterator<Integer> iterator = columnIndexIsIdentifierMap.keySet().iterator();
			while (iterator.hasNext()) {
				Integer columnIndex = iterator.next();
				Integer fieldID =  columnIndexToFieldIDMap.get(columnIndex);
				Boolean isIdentifier = columnIndexIsIdentifierMap.get(columnIndex);
				if ((isIdentifier!=null && isIdentifier.booleanValue())) {
					lastSavedIdentifierFieldIDIsSet.add(fieldID);
				}
			}
		}
		//add the implicitly selected field identifiers
		//the mandatory identifiers are disabled (to forbid unselecting them),
		//but it means that they will not be submitted by columnIndexIsIdentifierMap
		//so we should add them manually if a mandatoryIdentifierFields is mapped
		Set<Integer> mandatoryIdentifierFields = ExcelFieldMatchBL.getMandatoryIdentifierFields();
		Iterator<Integer> iterator = mandatoryIdentifierFields.iterator();
		Collection<Integer> submittedFieldIDs = columnIndexToFieldIDMap.values();
		while (iterator.hasNext()) {
			Integer mandatoryIdentifierField = iterator.next();
			if (submittedFieldIDs.contains(mandatoryIdentifierField)) {
				lastSavedIdentifierFieldIDIsSet.add(mandatoryIdentifierField);
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream (new File(excelMappingsDirectory, mappingFileName));
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(columNameToFieldIDMap);
			out.writeObject(lastSavedIdentifierFieldIDIsSet);
			out.close();
		} catch (FileNotFoundException e) {
			LOGGER.warn("Creating the output stream for mapping failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.warn("Saving the mapping failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return NEXT;
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<Integer, Integer> getColumnIndexToFieldIDMap() {
		return columnIndexToFieldIDMap;
	}

	public void setColumnIndexToFieldIDMap(
			Map<Integer, Integer> columnIndexToFieldIDMap) {
		this.columnIndexToFieldIDMap = columnIndexToFieldIDMap;
	}

	public Integer getSelectedSheet() {
		return selectedSheet;
	}

	public void setSelectedSheet(Integer selectedSheet) {
		this.selectedSheet = selectedSheet;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<Integer, Boolean> getColumnIndexIsIdentifierMap() {
		return columnIndexIsIdentifierMap;
	}

	public void setColumnIndexIsIdentifierMap(
			Map<Integer, Boolean> columnIndexIsIdentifierMap) {
		this.columnIndexIsIdentifierMap = columnIndexIsIdentifierMap;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
}
