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

package com.aurel.track.dbase;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TBasketBean;
import com.aurel.track.item.operation.BasketBL;

public class Migrate416To417 {

	private static final Logger LOGGER = LogManager.getLogger(Migrate416To417.class);
	
	
	/**
	 * Add the document and document section
	 * Use JDBC because negative objectIDs should be added
	 */
	public static void addDeletedBasket() {
		TBasketBean basketBean = BasketBL.getBasketByID(TBasketBean.BASKET_TYPES.DELETED);
		if (basketBean==null) {
			LOGGER.info("Add 'Deleted basket' basket");
			Connection cono = null;
			try {
				cono = InitDatabase.getConnection();
				Statement ostmt = cono.createStatement();
				cono.setAutoCommit(false);
				String deletedBasketStmt = addDeletedBasketStmt(TBasketBean.BASKET_TYPES.DELETED, "basket.label.-1", "-1001");
				ostmt.executeUpdate(deletedBasketStmt);
				cono.commit();
				cono.setAutoCommit(true);
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (cono != null) {
						cono.close();
					}
				} catch (Exception e) {
					LOGGER.info("Closing the connection failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
	
	/**
	 * Add deleted basket
	 * @param objectID
	 * @param label
	 * @param UUID
	 * @return
	 */
	private static String addDeletedBasketStmt(int objectID, String label, String UUID) {
		return "INSERT INTO TBASKET (OBJECTID,LABEL,TPUUID)"
				+ "VALUES (" + objectID + ", '" + label + "','"  + UUID +"')";
	}

	
}
