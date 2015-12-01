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

package com.aurel.track.lucene.index.associatedFields.textExctractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.textmining.extraction.word.WordTextExtractorFactory;

import com.aurel.track.lucene.index.associatedFields.LuceneFileExtractor.INDEXABLE_EXTENSIONS;

public class DocExtractor extends SingleExtensionTextExtractor implements ITextExtractor {
	private static final Log LOGGER = LogFactory.getLog(DocExtractor.class);
	
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
			WordTextExtractorFactory wordTextExtractorFactory = new WordTextExtractorFactory();
			return wordTextExtractorFactory.textExtractor(fis).getText();
		} catch (FileNotFoundException e) {
				LOGGER.info("File " + file.getName() + " not found. " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			LOGGER.debug("Extracting text from the .doc file " + file.getName() + " failed with " + e.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (fis!=null) {
					fis.close();
				}
			} catch (IOException e) {
				LOGGER.debug("Closing the FileInputStream for file " + file.getName() + " failed with " + e.getMessage());
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}

	/**
	 * Gets the file type this text extractor implementation can process
	 */
	@Override
	public String getFileType() {
		return INDEXABLE_EXTENSIONS.DOC;
	}

}
