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


package com.aurel.track.persist;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.om.Persistent;

/**
 * Class for managing the configuration of a single Genji instance.
 * It contains e-mail server settings, license key field, and similar
 * configuration data which pertains to the instance.
 */
public  class TSite
    extends com.aurel.track.persist.BaseTSite
    implements Persistent {
	private static final Logger LOGGER = LogManager.getLogger(TSite.class);
	public static final long serialVersionUID = 400L;
	
	@Override
	public void save() {
        try {
            LOGGER.debug(this.toString());
            super.save();
        }
        catch (Exception e) {
            LOGGER.error("Saving the TSite object failed with " + e.getMessage());
            LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
    }
}
