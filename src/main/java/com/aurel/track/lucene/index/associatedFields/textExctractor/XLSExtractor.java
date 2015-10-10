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

package com.aurel.track.lucene.index.associatedFields.textExctractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aurel.track.lucene.index.associatedFields.LuceneFileExtractor.INDEXABLE_EXTENSIONS;
import com.aurel.track.lucene.util.poi.XLSTextStripper;

/**
 * Text extractor for excel files
 * @author Tamas
 *
 */
public class XLSExtractor implements ITextExtractor {
	private static final Log LOGGER = LogFactory.getLog(XLSExtractor.class);
	
	/**
	 * Gets the text from file content 
	 * @param file
	 * @param fileExtension
	 * @return
	 */
	@Override
	public String getText(File file, String fileExtension) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LOGGER.info("File " + file.getName() + " not found. " + e.getMessage(), e);
			return null;
		}
		try {
			XLSTextStripper stripper = new XLSTextStripper(fis, fileExtension);
			return stripper.getText();
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Extracting text from the .xls  file " + file.getName() + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		} finally {
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOGGER.debug("Closing the file input stream failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * Gets the file types this text extractor implementation can process
	 */
	@Override
	public List<String> getFileTypes() {
		List<String> fileTypes = new LinkedList<String>(); 
		fileTypes.add(INDEXABLE_EXTENSIONS.XLS);
		fileTypes.add(INDEXABLE_EXTENSIONS.XLSX);
		return fileTypes;
	}

}
