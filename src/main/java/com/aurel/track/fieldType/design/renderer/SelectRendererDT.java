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

package com.aurel.track.fieldType.design.renderer;

/**
 * 
 * A renderer for combo at designTime
 *
 */
public class SelectRendererDT extends AbstractTypeRendererDT {
	//singleton isntance
	private static SelectRendererDT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static SelectRendererDT getInstance() {
		if (instance == null) {
			instance = new SelectRendererDT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public SelectRendererDT() {
	}
	public String getHtml() {
		return "<select style=\"width:120px;\" disabled=\"disabled\"><option value=\"1\">...</option></select>";
	}

	public String getImageName() {
		//no image
		return null;
	}
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.design.SelectTypeRenderer";
	}

}
