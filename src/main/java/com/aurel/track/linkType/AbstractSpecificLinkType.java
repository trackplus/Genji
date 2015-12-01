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


package com.aurel.track.linkType;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.link.ItemLinkBL;

public abstract class AbstractSpecificLinkType extends AbstractLinkType {

	/**
	 * Validates the workItemLink before save
	 * @param workItemLinkBean
	 * @param workItemLinkOriginal
	 * @param workItemsLinksMap
	 * @param predWorkItem
	 * @param succWorkItem
	 * @param personBean
	 * @param locale
	 * @return
	 */
	@Override
	public List<ErrorData> validateBeforeSave(TWorkItemLinkBean workItemLinkBean, TWorkItemLinkBean workItemLinkOriginal,
			SortedMap<Integer, TWorkItemLinkBean> workItemsLinksMap,
			TWorkItemBean predWorkItem, TWorkItemBean succWorkItem, TPersonBean personBean, Locale locale) {
		List<ErrorData> errorDataList = super.validateBeforeSave(workItemLinkBean, workItemLinkOriginal, workItemsLinksMap, predWorkItem, succWorkItem, personBean, locale);
		if (errorDataList==null) {
			errorDataList = new LinkedList<ErrorData>();
		}
		Integer linkPred = workItemLinkBean.getLinkPred();
		Integer linkSucc = workItemLinkBean.getLinkSucc();
		if (linkPred==null || linkSucc==null) {
			//add link to a new item
			return errorDataList;
		}
		//circular dependency
		boolean circularDependency = ItemLinkBL.isDescendent(linkPred,
				linkSucc, getPossibleDirection(), this);
		if (circularDependency) {
			errorDataList.add(new ErrorData("item.tabs.itemLink.err.circularDependency"));
		}
		boolean descendant = ItemBL.isAscendant(linkSucc, linkPred);
		if (descendant) {
			errorDataList.add(new ErrorData("item.tabs.itemLink.err.parentIsDescendant"));
		}
		return errorDataList;
	}
	
}
