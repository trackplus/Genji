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


package com.aurel.track.report.execute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.consInf.RaciRole;

public class ConsultedInformedLoaderBL {

	public static String WATCHER_SPLITTER_STRING = " | ";
	public static String WATCHER_SPLITTER_VALUES_STRING = "|";
	/**
	 * Adds the corresponding consulted and informed show values and sort order valuesm in the maps 
	 * @param workItemID
	 * @param consInfIDsMap
	 * @param personsMap
	 * @param showValuesMap
	 * @param sortOrderValuesMap
	 */
	public static void getConsultedInformedValues(Integer workItemID, Map<Integer, Map<String, Set<Integer>>> consInfIDsMap,  
			Map<Integer, String> showValuesMap, Map<Integer, Comparable> sortOrderValuesMap) {
		String showValue;			
		Map<String, Set<Integer>> workItemMap = consInfIDsMap.get(workItemID);
		if (workItemMap!=null) {			
			if (workItemMap.get(RaciRole.CONSULTANT)!=null) {
				showValue = ConsultedInformedLoaderBL.getRaciShowValue(workItemMap.get(RaciRole.CONSULTANT));
				showValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST, showValue);
				sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST, showValue);
			}
			if (workItemMap.get(RaciRole.INFORMANT)!=null) {
				showValue = ConsultedInformedLoaderBL.getRaciShowValue(workItemMap.get(RaciRole.INFORMANT));
				showValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST, showValue);
				sortOrderValuesMap.put(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST, showValue);
			}				
		}	
	}
	
	/**
	 * Gets the show value for a consulted and informed field
	 * @param raciPersonIDsList
	 * @param personsMap
	 * @return
	 */
	private static String getRaciShowValue(Set<Integer> raciPersonIDsList) {				
		StringBuffer stringBuffer = new StringBuffer();		
		if (raciPersonIDsList==null || raciPersonIDsList.isEmpty()) {
			return stringBuffer.toString();
		}
		Iterator<Integer> iterator = raciPersonIDsList.iterator();
		while (iterator.hasNext()) {
			Integer personID = (Integer) iterator.next();
			TPersonBean personBean =  LookupContainer.getPersonBean(personID);
			if (personBean!=null) {
				stringBuffer.append(personBean.getLabel());				
			}
			if (iterator.hasNext()) {
				stringBuffer.append(WATCHER_SPLITTER_STRING);
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Create a map from a list of TNotifyBeans
	 * 	- key: workItemID
	 * 	- value:  map: 
	 * 				- key: 'c' or 'i'
	 * 				- value': list of persons in the specific raci role
	 * @param notifyBeanList
	 * @return
	 */
	public static Map<Integer, Map<String, Set<Integer>>> getConsInfMap(List<TNotifyBean> notifyBeanList) {
		Map<Integer, Map<String, Set<Integer>>> notifyMap = new HashMap<Integer, Map<String, Set<Integer>>>();
		if (notifyBeanList!=null) {
			for (TNotifyBean notifyBean : notifyBeanList) {
				Integer workItemID = notifyBean.getWorkItem();
				Integer personID = notifyBean.getPersonID();				
				Map<String, Set<Integer>> workItemMap = notifyMap.get(workItemID);
				if (workItemMap==null) {
					workItemMap = new HashMap<String, Set<Integer>>();
					notifyMap.put(workItemID, workItemMap);
				}	
				addToRaciList(notifyBean.getRaciRole(), personID, workItemMap);				
			}
		}
		return notifyMap;
	}
	/**
	 * Add  consultant or informant person to the list of raci roles for a workItemID
	 * @param raciRole
	 * @param personID
	 * @param workItemMap
	 */
	private static void addToRaciList(String raciRole, Integer personID, Map<String, Set<Integer>> workItemMap) {		
		Set<Integer> raciSet = workItemMap.get(raciRole); 
		if (raciSet==null) { 
			raciSet = new HashSet<Integer>();
			workItemMap.put(raciRole, raciSet);
		}
		raciSet.add(personID);		
	}	
	
	public static List<Integer> getPersonIDList(List<TNotifyBean> notifyBeanList) {
		Set<Integer>  set=new TreeSet<Integer>();
		for (TNotifyBean notifyBean : notifyBeanList) {
			Integer personID = notifyBean.getPersonID();
			set.add(personID);
		}
		List<Integer> result=new ArrayList<Integer>(set.size());
		result.addAll(set);
		return result;
	}
	
			
}
