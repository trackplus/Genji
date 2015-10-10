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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.linkType.CloseDependsOnLinkType;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.LinkTypeBL;
import com.aurel.track.linkType.MsProjectLinkType;

public class Migrate400To410 {

	private static final Logger LOGGER = LogManager.getLogger(Migrate400To410.class);
	
	/**
	 * Change the MsProject and 'Close depends on' linkType's link direction to
	 * right to left For item links of those types change the predecessor with
	 * successor and vice versa and change the link direction
	 */
	public static void inverseLinkDirections(boolean linkPredSuccAlso) {
		// change the direction to inward for MsProjectLinkType and
		// CloseDependsOnLinkType
		List<Integer> linkTypeIDs = new LinkedList<Integer>();
		List<TLinkTypeBean> msProjectlinkTypeBeans = LinkTypeBL.loadByLinkType(MsProjectLinkType.getInstance().getPluginID(), null);
		if (msProjectlinkTypeBeans != null) {
			for (TLinkTypeBean linkTypeBean : msProjectlinkTypeBeans) {
				linkTypeIDs.add(linkTypeBean.getObjectID());
				Integer direction = linkTypeBean.getLinkDirection();
				if (direction == null
						|| direction.intValue() != LINK_DIRECTION.RIGHT_TO_LEFT) {
					linkTypeBean.setLinkDirection(LINK_DIRECTION.RIGHT_TO_LEFT);
					LinkTypeBL.save(linkTypeBean);
					LOGGER.info("'MsProject' link type "
							+ linkTypeBean.getName()
							+ " set to unidirectional inward");
				}
			}
		}
		List<TLinkTypeBean> closeDependsOnLinkBeans = LinkTypeBL.loadByLinkType(CloseDependsOnLinkType.getInstance().getPluginID(), null);
		if (closeDependsOnLinkBeans != null) {
			for (TLinkTypeBean linkTypeBean : closeDependsOnLinkBeans) {
				linkTypeIDs.add(linkTypeBean.getObjectID());
				Integer direction = linkTypeBean.getLinkDirection();
				if (direction == null
						|| direction.intValue() != LINK_DIRECTION.RIGHT_TO_LEFT) {
					linkTypeBean.setLinkDirection(LINK_DIRECTION.RIGHT_TO_LEFT);
					LinkTypeBL.save(linkTypeBean);
					LOGGER.info("'Closed depends on' link type "
							+ linkTypeBean.getName()
							+ "set to unidirectional inward");
				}
			}
		}
		List<TWorkItemLinkBean> workItemLinks = ItemLinkBL
				.loadByLinkTypeAndDirection(linkTypeIDs,
						LINK_DIRECTION.LEFT_TO_RIGHT);
		if (workItemLinks != null) {
			LOGGER.info("Number of item links to reverse "
					+ workItemLinks.size());
			for (TWorkItemLinkBean workItemLinkBean : workItemLinks) {
				if (linkPredSuccAlso) {
					Integer predecessor = workItemLinkBean.getLinkPred();
					Integer successor = workItemLinkBean.getLinkSucc();
					workItemLinkBean.setLinkPred(successor);
					workItemLinkBean.setLinkSucc(predecessor);
				}
				workItemLinkBean.setLinkDirection(LINK_DIRECTION.RIGHT_TO_LEFT);
				ItemLinkBL.saveLink(workItemLinkBean);
			}
		}
		LOGGER.info("'MsProject' and 'Closed depends on' item links reversed");
	}
}
