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

package com.aurel.track.lucene.util.poi;

import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aurel.track.lucene.index.associatedFields.LuceneFileExtractor;

/**
 * <a href="XLSTextStripper.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Mirco Tamburini
 * @version $Revision: 1229 $
 *
 */
public class XLSTextStripper {
	
	private static final Logger LOGGER = LogManager.getLogger(XLSTextStripper.class);

	public XLSTextStripper(FileInputStream fis, String fileExtension) {
		try {
			StringBuffer sb = new StringBuffer();
			Workbook workbook = null;
			if (LuceneFileExtractor.INDEXABLE_EXTENSIONS.XLS.equalsIgnoreCase(fileExtension)) {
				workbook = new HSSFWorkbook(fis);
			} else {
				if (LuceneFileExtractor.INDEXABLE_EXTENSIONS.XLSX.equalsIgnoreCase(fileExtension)) {
					workbook = new XSSFWorkbook(fis);
				}
			}
			if (workbook!=null) {
				int numOfSheets = workbook.getNumberOfSheets();
				for(int i = 0; i < numOfSheets; i++) {
					Sheet sheet = workbook.getSheetAt(i);
					Iterator<Row> rowIterator = sheet.rowIterator();
					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							String cellStringValue = null;
							if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
								boolean booleanValue = cell.getBooleanCellValue();
								cellStringValue = Boolean.toString(booleanValue);
							}
							else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								double doubleValue = cell.getNumericCellValue();
								cellStringValue = Double.toString(doubleValue);
							}
							else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								cellStringValue = cell.getStringCellValue();
							}
							if (cellStringValue != null) {
								sb.append(cellStringValue);
								sb.append("\t");
							}
						}
						sb.append("\n");
					}
				}
			}
			_text = sb.toString();
		}
		catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public String getText() {
		return _text;
	}

	private String _text;

}
