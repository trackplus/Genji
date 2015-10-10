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

package com.aurel.track.exchange.docx.exporter;

/**
 * Base class for pre-processing images and tables
 * @author Tamas
 *
 */
public class PreprocessBase {
	/**
	 * global counter for images: makes the correspondence between imageElement's "alt" attribute and the map key
	 */
	protected int globalCounter = 0;
	/**
	 * The actual chapter no (heading 1) counter
	 */
	protected int chapterNo;
	/**
	 * The images/tables sequences are reseted by starting of a new chapter (heading 1) 
	 */
	protected int counterWithinChapter;
	
	
	public void setChapterNo(int chapterNo) {
		this.chapterNo = chapterNo;
	}

	public void setCounterWithinChapter(int counterWithinChapter) {
		this.counterWithinChapter = counterWithinChapter;
	}
}
