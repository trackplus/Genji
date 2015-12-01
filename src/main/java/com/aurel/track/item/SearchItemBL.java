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

package com.aurel.track.item;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemIDListItems;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.util.GeneralUtils;


public class SearchItemBL {
	
	/**
	 * Gets the highlighted fragments and titles for documents (not for document sections!)
	 * @param personID
	 * @param highlightedTextMap
	 * @param highlightedFragmentsMap
	 * @param highlightedTitlesMap
	 */
	static void getDocumentsToFragments(Integer personID, Map<Integer, String> highlightedTextMap, Map<Integer, String> highlightedFragmentsMap, Map<Integer, String> highlightedTitlesMap) {
		if (highlightedTextMap!=null && highlightedTextMap.size()>0) {
			List<TListTypeBean> documentTypeBeans = IssueTypeBL.loadStrictDocumentTypes();
			if (documentTypeBeans!=null && documentTypeBeans.size()>0) {
				Set<Integer> allItemIDs = new HashSet<Integer>(); 
				Set<Integer> documentTypeIDs = GeneralUtils.createIntegerSetFromBeanList(documentTypeBeans);
				Set<Integer> itemIDsSet = highlightedTextMap.keySet();
				while (itemIDsSet!=null && itemIDsSet.size()>0) {
					int[] itemIDs = GeneralUtils.createIntArrFromIntegerCollection(itemIDsSet);
					List<TWorkItemBean> highlightedItems = LoadItemIDListItems.getWorkItemBeansByWorkItemIDs(itemIDs, personID, false, false, false);
					itemIDsSet = new HashSet<Integer>();
					for (TWorkItemBean workItemBean : highlightedItems) {
						Integer objectID = workItemBean.getObjectID();
						Integer itemTypeID = workItemBean.getListTypeID();
						if (documentTypeIDs.contains(itemTypeID)) {
							highlightedFragmentsMap.put(objectID, highlightedTextMap.get(objectID));
							highlightedTitlesMap.put(objectID, workItemBean.getSynopsis());
						} else {
							Integer parentID = workItemBean.getSuperiorworkitem();
							if (parentID!=null && !allItemIDs.contains(parentID)) {
								itemIDsSet.add(parentID);
								highlightedTextMap.put(parentID, highlightedTextMap.get(objectID));
								highlightedTextMap.remove(objectID);
							}
						}
						allItemIDs.add(objectID);
					}
				}
			}
		}
	}
	
}
