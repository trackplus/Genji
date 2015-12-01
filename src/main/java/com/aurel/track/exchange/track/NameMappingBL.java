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

package com.aurel.track.exchange.track;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.util.GeneralUtils;

public class NameMappingBL {

	public static Integer getExactMatch(String label, Map<String, ILabelBean> lookupBeansMap) {
		if (lookupBeansMap!=null && label!=null) {
			ILabelBean labelBean = lookupBeansMap.get(label);
			if (labelBean!=null) {
				return labelBean.getObjectID();
			}			
		}
		return null;
	}
	
	public static Integer getExactOrSimilarMatch(String label, Map<String, ILabelBean> lookupBeansMap) {
		if (lookupBeansMap!=null && label!=null) {
			ILabelBean labelBean = lookupBeansMap.get(label);
			if (labelBean!=null) {
				return labelBean.getObjectID();
			}				
			String similarName = getSimilarName(label, lookupBeansMap.keySet());
			if (similarName!=null) {
				labelBean = lookupBeansMap.get(similarName);
				if (labelBean!=null) {
					return labelBean.getObjectID();
				}
			}
		}
		return null;
	}

	public static String getSimilarName(String leftName,  Set<String> names) {		
		String[] leftNameParts = leftName.split("\\s|,");
		List<String> leftNamePartsList = GeneralUtils.createStringListFromStringArr(leftNameParts);
		if (leftNameParts!=null) {
			Iterator<String> iterator = names.iterator();
			while (iterator.hasNext()) {
				String rightName = iterator.next();
				String[] rightNameParts = rightName.split("\\s|,");
				List<String> rightNamePartsList = GeneralUtils.createStringListFromStringArr(rightNameParts);
				if (doesNamePartsMatch(leftNamePartsList, rightNamePartsList)) {
					return rightName;
				}								
			}			
		}
		return null;
	}

	/**
	 * Whether the name parts match in any order
	 * @param leftNamePartsList
	 * @param rightNamePartsList
	 * @return
	 */
	public static boolean doesNamePartsMatch(List<String> leftNamePartsList, List<String> rightNamePartsList) {		
		List<String> removedParts = null;
		if (leftNamePartsList!=null && rightNamePartsList!=null) {
			boolean found = true;
			while (found) {
				found = false;
				for (int i = 0; i < leftNamePartsList.size(); i++) {
					String leftNamePart = leftNamePartsList.get(i);
					found = false;
					if (leftNamePart!=null) {
						for (int j = 0; j < rightNamePartsList.size(); j++) {						
							if (leftNamePart.equals(rightNamePartsList.get(j))) {
								if (removedParts==null) {
									removedParts = new LinkedList<String>();	
								}
								removedParts.add(leftNamePart);
								found = true;
								leftNamePartsList.remove(leftNamePart);
								rightNamePartsList.remove(rightNamePartsList.get(j));
							}
						}
					}
				}			
			}
		}
		boolean isEmpty = leftNamePartsList.isEmpty(); /*&& rightNamePartsList.isEmpty()*/
		if (!isEmpty && removedParts!=null) {
			//put the partial match back for further matches (do not change the leftNamePartsList as side effect when only a firstName or lastName match is found)
			leftNamePartsList.addAll(0, removedParts);
		}
		return isEmpty;
	}

}
