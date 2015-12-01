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
 * This interface define functions to render a type in the design time
 * 
 *
 */
public interface TypeRendererDT {
	/**
	 * Get the html contend for type
	 * @return
	 */
	public String getHtml();
	/**
	 * Give a alternative image to html contend
	 * If the image is provided the html conted is not take in account anymore.
	 * 
	 * The name must be a fileName in the image dir of the application
	 * 
	 * @return
	 */
	public String getImageName();
	public String getExtClassName();
}
