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

package com.aurel.track.fieldType.design.renderer;

/**
 * 
 * Renderer for text components in design time envirorment
 *
 */
public class TextBoxRendererDT extends AbstractTypeRendererDT{
	//singleton isntance
	private static TextBoxRendererDT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static TextBoxRendererDT getInstance() {
		if (instance == null) {
			instance = new TextBoxRendererDT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public TextBoxRendererDT() {
	}
	/**
	 * Get the encode htm as a disabled text input
	 */
	@Override
	public String getHtml() {
		return "<input disabled=\"disabled\" type=\"text\">";
	}
	@Override
	public String getImageName() {
		//no image for text
		return null;
	}
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.design.TextFieldTypeRenderer";
	}
}
