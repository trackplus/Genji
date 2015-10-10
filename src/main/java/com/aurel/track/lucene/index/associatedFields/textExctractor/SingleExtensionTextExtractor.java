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

import java.util.LinkedList;
import java.util.List;

public abstract class SingleExtensionTextExtractor implements ITextExtractor {

	/**
	 * Gets the file types this text extractor implementation can process
	 * @return
	 */
	public List<String> getFileTypes() {
		List<String> fileTypes = new LinkedList<String>();
		String fileType = getFileType();
		if (fileType!=null) {
			fileTypes.add(fileType);
		}
		return fileTypes;
	}
	
	/**
	 * Gets the file type if the text extractor refers to a single file extension
	 * @return
	 */
	public abstract String getFileType();
}
