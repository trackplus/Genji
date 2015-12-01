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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aurel.track.lucene.index.associatedFields.LuceneFileExtractor.INDEXABLE_EXTENSIONS;
import com.aurel.track.lucene.util.openOffice.OOIndexer;

/**
 * Text extractor from different open office file content
 * @author Tamas
 *
 */
public class OpenOfficeExtractor implements ITextExtractor {
	private static final Log LOGGER = LogFactory.getLog(OpenOfficeExtractor.class);
	
	/**
	 * Gets the text from file content 
	 * @param file
	 * @param fileExtension
	 * @return
	 */
	@Override
	public String getText(File file, String fileExtension) {
		try {
			OOIndexer ooIndexer = new OOIndexer();
			return ooIndexer.parseOpenOffice(file);
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Extracting text from the open office file " + file.getName() + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}

	/**
	 * Gets the file types this text extractor implementation can process
	 */
	@Override
	public List<String> getFileTypes() {
		List<String> fileTypes = new LinkedList<String>(); 
		fileTypes.add(".sxw");
		fileTypes.add(".stv");
		fileTypes.add(INDEXABLE_EXTENSIONS.ODT);
		fileTypes.add(".ott");
		
		fileTypes.add(INDEXABLE_EXTENSIONS.ODS);
		fileTypes.add(".ots");
		fileTypes.add(".sxc");
		fileTypes.add(".stc");
		
		fileTypes.add(INDEXABLE_EXTENSIONS.ODP);
		fileTypes.add(".otp");
		fileTypes.add(".sxi");
		fileTypes.add(".sti");
		return fileTypes;
	}

}
