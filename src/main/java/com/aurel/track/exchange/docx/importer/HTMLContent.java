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

package com.aurel.track.exchange.docx.importer;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the useful data parsed from html needed for items
 * @author Tamas
 *
 */
public class HTMLContent {
	private String style;
	
	private List<ItemNode> chapters;
	

	public HTMLContent() {
		chapters = new ArrayList<ItemNode>();
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public List<ItemNode> getChapters() {
		return chapters;
	}
	public void setChapters(List<ItemNode> chapters) {
		this.chapters = chapters;
	}
	
	
}
