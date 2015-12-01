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

package com.aurel.track.lucene.index.associatedFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 6385 $
 *
 */
public class LuceneFileExtractor {

	private static final Log LOGGER = LogFactory.getLog(LuceneFileExtractor.class);

	public static class INDEXABLE_EXTENSIONS {
		//MS Word
		public static String DOC = ".doc";
		public static String DOCX = ".docx";
		public static String HTM = ".htm";
		public static String HTML = ".html";
		public static String PDF = ".pdf";
		//rich text format
		public static String RTF = ".rtf";
		//MS Excel
		public static String XLS = ".xls";
		public static String XLSX = ".xlsx";
		public static String TXT = ".txt";
		public static String PROPERTIES = ".properties";
		//general XML
		public static String XML = ".xml";
		//open office text
		//OpenDocument Text
		public static String ODT = ".odt";
		//StarWriter

		//open office sheet
		//OpenDocument Table
		public static String ODS = ".ods";
		//StarCalc
		//open office impress
		//OpenDocument presentation
		public static String ODP = ".odp";
		//StarImpress
	}

	private static String[] STRINGEXTENSIONS = new String[] {
		INDEXABLE_EXTENSIONS.DOC,
		INDEXABLE_EXTENSIONS.DOCX,
		INDEXABLE_EXTENSIONS.HTM,
		INDEXABLE_EXTENSIONS.HTML,
		INDEXABLE_EXTENSIONS.PDF,
		INDEXABLE_EXTENSIONS.RTF,
		INDEXABLE_EXTENSIONS.XLS,
		INDEXABLE_EXTENSIONS.XLSX,
		INDEXABLE_EXTENSIONS.XML,
		INDEXABLE_EXTENSIONS.ODT,
		//INDEXABLE_EXTENSIONS.SDV,
		INDEXABLE_EXTENSIONS.ODS,
		//INDEXABLE_EXTENSIONS.SDC,
		INDEXABLE_EXTENSIONS.ODP,
		//INDEXABLE_EXTENSIONS.SDD,
	};

	private static String[] READEREXTENSIONS = new String[] {
		INDEXABLE_EXTENSIONS.TXT,
		INDEXABLE_EXTENSIONS.PROPERTIES
	};


	/**
	 * Gets the extension if it is recognized 
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (fileName==null) {
			return "";
		}
		int extensionIndex = fileName.lastIndexOf(".");
		if (extensionIndex<=0) {
			return "";
		}
		return fileName.substring(extensionIndex);
	}

	public static boolean isStringExtension(String extension) {
		for (int i=0; i<STRINGEXTENSIONS.length; i++) {
			if (STRINGEXTENSIONS[i].equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isReaderExtension(String extension) {
		for (int i=0; i<READEREXTENSIONS.length; i++) {
			if (READEREXTENSIONS[i].equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	public static Reader getReader(File file) {
		Reader reader = null;
		if (file==null || !file.exists()) {
			return null;
		}
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			LOGGER.info("File " + file.getName() + " not found. " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		reader = new BufferedReader(reader);
		return reader;
	}


}
