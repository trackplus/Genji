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

package com.aurel.track.dbase;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.InlineLinkType;
import com.aurel.track.linkType.LinkTypeBL;

/**
 * Migration code from 500 to 502
 * @author Tamas
 *
 */
public class Migrate500To502 {

	private static final Logger LOGGER = LogManager.getLogger(Migrate500To502.class);
	/**
	 * Add inline linked link type
	 */
	static void addInlineLinkedLinkType() {
		List<TLinkTypeBean> inlinelinkTypeBeans = LinkTypeBL.loadByLinkType(InlineLinkType.getInstance().getPluginID(), null);
		if (inlinelinkTypeBeans==null || inlinelinkTypeBeans.isEmpty()) {
			TLinkTypeBean linkTypeBean = LinkTypeBL.loadByPrimaryKey(13);
			if (linkTypeBean==null) {
				LOGGER.info("Add 'inline linked' link type with fixed ID");
				Connection cono = null;
				try {
					cono = InitDatabase.getConnection();
					Statement ostmt = cono.createStatement();
					cono.setAutoCommit(false);
					String inlineLinkTypeStmt = addInlineLinkTypeStmt(13, "is inline linked in",  "the filtered items are inline linked to", LINK_DIRECTION.RIGHT_TO_LEFT, InlineLinkType.class.getName(), "0123456789");
					ostmt.executeUpdate(inlineLinkTypeStmt);
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
			} else {
				LOGGER.info("Add 'inline linked' link type at end");
				UpgradeDatabase.addLinkType("is inline linked in", "the filtered items are inline linked to", LINK_DIRECTION.RIGHT_TO_LEFT, InlineLinkType.class.getName());
			}
		}
	}
	
	/**
	 * Add deleted basket
	 * @param objectID
	 * @param lastName
	 * @param UUID
	 * @return
	 */
	private static String addInlineLinkTypeStmt(int objectID, String name, String leftToRightFirst, int linkTypeDirection, String linkTypePlugin, String UUID) {
		return "INSERT INTO TLINKTYPE (OBJECTID, NAME, LEFTTORIGHTFIRST, LINKDIRECTION, LINKTYPEPLUGIN, TPUUID)"
				+ "VALUES (" + objectID + ",'" + name + "', '" + leftToRightFirst + "', " + linkTypeDirection + ", '" + linkTypePlugin +"', '"+UUID+"')";
	}
}
