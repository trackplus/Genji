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


package com.aurel.track.util.calendar;

public class CalendarDay {
	private String styleId = null;

	private String styleClass = null;

	private StringBuffer lines = new StringBuffer();

	public String getLines() {
		return lines.toString();
	}

	public void addLine(String line) {
		if (this.lines.length() != 0) {
			this.lines.append("<br/>");
		}
		this.lines.append(line);
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<td width=\"14%\"");
		if (styleClass != null) {
			sb.append(" class=\"" + styleClass + "\"");
		}
		if (styleId != null) {
			sb.append(" id=\"" + styleId + "\"");
		}
		sb.append(">" + lines.toString() + "</td>");
		return sb.toString();
	}
}
