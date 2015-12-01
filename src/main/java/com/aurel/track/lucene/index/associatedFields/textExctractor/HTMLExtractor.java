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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aurel.track.lucene.index.associatedFields.LuceneFileExtractor.INDEXABLE_EXTENSIONS;

public class HTMLExtractor implements ITextExtractor {
	private static final Log LOGGER = LogFactory.getLog(HTMLExtractor.class);
	
	/**
	 * Gets the text from file content 
	 * @param file
	 * @param fileExtension
	 * @return
	 */
	@Override
	public String getText(File file, String fileExtension) {
		FileInputStream fis = null;
		Reader reader = null;
		try {
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				LOGGER.info("File " + file.getName() + " not found. " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(fis));
			DefaultStyledDocument dsd = new DefaultStyledDocument();
			HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
			htmlEditorKit.read(reader, dsd, 0);
			return dsd.getText(0, dsd.getLength());
		} catch (Exception e) {
			LOGGER.debug("Extracting text from the .htm or .html  file " + file.getName() + " failed with " + e.getMessage());
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (reader!=null) {
					reader.close();
				}
			} catch (Exception e) {
				LOGGER.debug("Closing the reader for file " + file.getName() + " failed with " + e.getMessage());
			}
			try {
				if (fis!=null) {
					fis.close();
				}
			} catch (Exception e) {
				LOGGER.debug("Closing the FileInputStream for file " + file.getName() + " failed with " + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Gets the file types this text extractor implementation can process
	 */
	@Override
	public List<String> getFileTypes() {
		List<String> fileTypes = new LinkedList<String>(); 
		fileTypes.add(INDEXABLE_EXTENSIONS.HTM);
		fileTypes.add(INDEXABLE_EXTENSIONS.HTML);
		return fileTypes;
	}

}
