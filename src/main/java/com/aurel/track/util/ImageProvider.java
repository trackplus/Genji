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

package com.aurel.track.util;

import java.io.OutputStream;
import java.util.Map;


/**
 * This interface describes an image provider, a class that writes
 * image information in the format given to an output stream.
 * <p>
 * Typical formats are "png" and "jpg".
 * <p>
 * A typical use is in dashboard views. The dashboard view may provide a link
 * pointing to an action with a graph provider as a parameter.
 */
public interface ImageProvider {
	
	/**
	 * Write the image generated according to the imageParametersMap, in the request's output stream 
	 * @param outputStream
	 * @param imageParametersMap
	 * @param imageFormat
	 */
	public void writeImage(OutputStream outputStream, Map imageParametersMap, String imageFormat);

	/**
	 * Write the raw JSON image data generated according to the imageParametersMap, in the request's output stream 
	 * @param outputStream
	 * @param imageParametersMap
	 * @param imageFormat
	 */
	public void loadJSON(OutputStream outputStream, Map imageParametersMap, String imageFormat);

}
