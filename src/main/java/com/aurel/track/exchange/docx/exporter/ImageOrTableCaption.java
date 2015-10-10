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
 * The caption information for image
 * @author Tamas
 *
 */
public class ImageOrTableCaption {
	/**
	 * The actual chapter no (heading 1) counter
	 */
	private int chapterNo;
	/**
	 * The images/tables sequences are reseted by starting of a new chapter (heading 1) 
	 */
	private int counterWithinChapter;
	/**
	 * The actual caption text
	 */
	private String caption;
	
	private ALIGN align;
	
	public static enum ALIGN { LEFT, RIGHT };
	
	public ImageOrTableCaption(int chapterNo, int counterWithinChapter,
			String caption, ALIGN align) {
		super();
		this.chapterNo = chapterNo;
		this.counterWithinChapter = counterWithinChapter;
		this.caption = caption;
		this.align = align;
	}
	
	public int getChapterNo() {
		return chapterNo;
	}
	public void setChapterNo(int chapterNo) {
		this.chapterNo = chapterNo;
	}
	public int getCounterWithinChapter() {
		return counterWithinChapter;
	}
	public void setCounterWithinChapter(int counterWithinChapter) {
		this.counterWithinChapter = counterWithinChapter;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public ALIGN getAlign() {
		return align;
	}

	public void setAlign(ALIGN align) {
		this.align = align;
	}
	
	
}
