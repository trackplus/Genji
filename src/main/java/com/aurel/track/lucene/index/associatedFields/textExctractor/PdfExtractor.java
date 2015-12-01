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
import java.io.StringWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.aurel.track.lucene.index.associatedFields.LuceneFileExtractor.INDEXABLE_EXTENSIONS;

public class PdfExtractor extends SingleExtensionTextExtractor implements ITextExtractor {
	private static final Log LOGGER = LogFactory.getLog(PdfExtractor.class);
	
	/**
	 * Gets the text from file content 
	 * @param file
	 * @param fileExtension
	 * @return
	 */
	@Override
	public String getText(File file, String fileExtension) {
		FileInputStream fis = null;
		PDDocument pdDoc = null;
		StringWriter stringWriter = null;
		try {
			fis = new FileInputStream(file);
			PDFParser parser = new PDFParser(fis);
			parser.parse();
			pdDoc = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setLineSeparator("\n");
			stringWriter = new StringWriter();
			stripper.writeText(pdDoc, stringWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Extracting text from the .pdf  file " + file.getName() + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		} finally {
			try {
				if (stringWriter!=null) {
					stringWriter.close();
				}
			} catch (Exception e) {
			}
			try {
				if (pdDoc!=null) {
					pdDoc.close();
				}
			} catch (Exception e) {
				LOGGER.info("Closing pdDoc for " + file + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			try {
				if (fis!=null) {
					fis.close();
				}
			} catch (Exception e) {
				LOGGER.info("Closing the FileInputStream for " + file +  " failed with " + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Gets the file type this text extractor implementation can process
	 */
	@Override
	public String getFileType() {
		return INDEXABLE_EXTENSIONS.PDF;
	}

}
