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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aurel.track.lucene.index.associatedFields.LuceneFileExtractor.INDEXABLE_EXTENSIONS;

/**
 * Text extractor for rtf files
 * @author Tamas
 *
 */
public class RTFExtractor extends SingleExtensionTextExtractor implements ITextExtractor {
	private static final Log LOGGER = LogFactory.getLog(RTFExtractor.class);
	
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
			DefaultStyledDocument dsd = new DefaultStyledDocument();
			RTFEditorKit rtfEditorKit = new RTFEditorKit();
			
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				LOGGER.info("File " + file.getName() + " not found. " + e.getMessage(), e);
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(fis));
			rtfEditorKit.read(reader, dsd, 0);
			return dsd.getText(0, dsd.getLength());
		}
		catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Extracting text from the .rtf  file " + file.getName() + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		} finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.debug("Closing the reader for file " + file.getName() + "  failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOGGER.debug("Closing the FileInputStream for file " + file.getName() + " failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}

	/**
	 * Gets the file type this text extractor implementation can process
	 */
	@Override
	public String getFileType() {
		return INDEXABLE_EXTENSIONS.RTF;
	}

}
