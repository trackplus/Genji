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

package com.aurel.track.plugin;

import java.io.Serializable;

/**
 * A descriptor of a plugin
 */
public interface PluginDescriptor extends Serializable {
	String getId();

	void setId(String id);

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

	String getTheClassName();
	
	void setTheClassName(String theClassName);
	
	String getJsConfigClass();

	void setJsConfigClass(String jsConfigClass);

	String getBundleName();

	void setBundleName(String bundleName);
}
