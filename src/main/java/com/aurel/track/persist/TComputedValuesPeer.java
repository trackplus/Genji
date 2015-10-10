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


package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.category.filter.execute.loadItems.criteria.TreeFilterCriteria;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.ComputedValuesDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.tql.TqlBL;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TComputedValuesPeer
	extends com.aurel.track.persist.BaseTComputedValuesPeer
	implements ComputedValuesDAO
{
	private static final Logger LOGGER = LogManager.getLogger(TComputedValuesPeer.class);
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table
	 * @param objectID
	 * @return
	 */
	public TComputedValuesBean loadByPrimaryKey(Integer objectID) {
		TComputedValues tComputedValues = null;
		try {
			tComputedValues = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading of a computed value by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tComputedValues!=null) {
			return tComputedValues.getBean();
		}
		return null;
	}	
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table by workItem and types
	 * @param workItemID
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public TComputedValuesBean loadByWorkItemAndTypesAndPerson(Integer workItemID, int effortType, int computedValueType, Integer person) {
		List<TComputedValues> computedValuesList = null;
		Criteria criteria = new Criteria();
		criteria .add(WORKITEMKEY, workItemID);
		criteria.add(EFFORTTYPE, effortType);
		criteria.add(COMPUTEDVALUETYPE, computedValueType);
		if (person==null) {
			criteria.add(PERSON, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PERSON, person);
		}
		try {
			computedValuesList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading a computed value bean by workItem " + workItemID + " and effortType " + effortType +
					" and computedValue " + computedValueType + " failed with " + e.getMessage(), e); 
		}
		if (computedValuesList==null || computedValuesList.isEmpty()) {
			return null;
		}
		if (computedValuesList.size()>1) {
			LOGGER.warn("More than one value found for workItem " + workItemID + " and effortType " + effortType +
					" and computedValue " + computedValueType + " person " + person);
		}
		return ((TComputedValues)computedValuesList.get(0)).getBean();
	}
	
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItems and types
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public List<TComputedValuesBean> loadByWorkItemsAndTypesAndPerson(int[] workItemIDs, int effortType, int computedValueType, Integer person) {
		List<TComputedValuesBean> computedValueList = new LinkedList<TComputedValuesBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return computedValueList;
		}	
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return computedValueList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.add(EFFORTTYPE, effortType);
			criteria.add(COMPUTEDVALUETYPE, computedValueType);
			if (person==null) {
				criteria.add(PERSON, (Object)null, Criteria.ISNULL);
			} else {
				criteria.add(PERSON, person);
			}
			criteria.addIn(WORKITEMKEY, workItemIDChunk);
			try {
				computedValueList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the computedValues list by workItemIDs and effortType " + effortType +
						" and computedValue " + computedValueType + " and person " + person + " failed with " + e.getMessage(), e);
			}
		}
		return computedValueList;
	}
	
	
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItems and types
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public List<TComputedValuesBean> loadByWorkItemsAndTypes(int[] workItemIDs, int effortType, int computedValueType) {
		List<TComputedValuesBean> computedValueList = new LinkedList<TComputedValuesBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return computedValueList;
		}	
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return computedValueList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.add(EFFORTTYPE, effortType);
			criteria.add(COMPUTEDVALUETYPE, computedValueType);
			criteria.addIn(WORKITEMKEY, workItemIDChunk);
			try {
				computedValueList.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
				LOGGER.error("Loading the computedValues list by workItemIDs and effortType " + effortType +
						" and computedValue " + computedValueType + " failed with " + e.getMessage(), e);
			}
		}
		return computedValueList;
	}
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItemIDs, computedValueType and person
	 * @param workItemIDs
	 * @param computedValueType
	 * @param person
	 * @return
	 */
	public List<TComputedValuesBean> loadByWorkItemsAndTypesAndPerson(int[] workItemIDs, int computedValueType, Integer person) {
		List<TComputedValuesBean> computedValueList = new LinkedList<TComputedValuesBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return computedValueList;
		}	
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return computedValueList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(WORKITEMKEY, workItemIDChunk);
			try {
				computedValueList.addAll(getFilterComputedValuesForPerson(criteria, person, computedValueType));
			} catch(Exception e) {
				LOGGER.error("Loading the computedValues list by workItemIDs " + workItemIDChunk.length +  " computedValue " + computedValueType + " and person " + person + " failed with " + e.getMessage(), e);
			}
		}
		return computedValueList;
	}
	
	/**
	 * Loads a computedValuesBean from the TComputedValues table for workItemIDs and types
	 * @param workItemIDs
	 * @param effortType
	 * @param computedValueTypes
	 * @param person
	 * @return
	 */
	public List<TComputedValuesBean> loadByWorkItemsAndTypes(int[] workItemIDs, int[] computedValueTypes) {
		List<TComputedValuesBean> computedValueList = new LinkedList<TComputedValuesBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return computedValueList;
		}	
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return computedValueList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(WORKITEMKEY, workItemIDChunk);
			try {
				computedValueList.addAll(getFilterComputedValues(criteria, computedValueTypes));
			} catch(Exception e) {
				LOGGER.error("Loading the computedValues list by workItemIDs " + workItemIDChunk.length +
						" and computedValues " + computedValueTypes.length + " failed with " + e.getMessage(), e);
			}
		}
		return computedValueList;
	}
	
	/**
	 * Loads the computed values for a tree filter for a person
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param computedValueType
	 * @param myExpenses
	 * @return
	 */
	public List<TComputedValuesBean> loadByTreeFilterForPerson(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, int computedValueType, boolean myExpenses) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TComputedValuesBean>();
		}		
		Criteria criteria = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		Integer personIDToFilter = null;
		if (myExpenses) {
			personIDToFilter = personID;
		}
		try {
			return getFilterComputedValuesForPerson(criteria, personIDToFilter, computedValueType);
		} catch (TorqueException e) {
			LOGGER.error("Loading the computed values for tree filter with computedValueType " + computedValueType + " and personID " + personIDToFilter + " failed with " + e.getMessage(), e);
			return new ArrayList<TComputedValuesBean>();
		}
	}
	
	/**
	 * Loads the computed values for a tree filter
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @param computedValueTypes
	 * @return
	 */
	public List<TComputedValuesBean> loadByTreeFilter(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID, int[] computedValueTypes) {
		Integer[] selectedProjects = filterUpperTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TComputedValuesBean>();
		}		
		Criteria criteria = TreeFilterCriteria.prepareTreeFilterCriteria(filterUpperTO, raciBean, personID);
		try {
			return getFilterComputedValues(criteria, computedValueTypes);
		} catch (TorqueException e) {
			LOGGER.error("Loading the computed values for tree filter with computedValueTypes " + computedValueTypes.length + " failed with " + e.getMessage(), e);
			return new ArrayList<TComputedValuesBean>();
		}
	}
	
	/**
	 * Get the computed values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueType
	 * @param myExpenses
	 * @return
	 */
	public List<TComputedValuesBean> loadByTQLFilterForPerson(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors, int computedValueType, boolean myExpenses) {
		Criteria criteria = TqlBL.createCriteria(tqlExpression, personBean, locale, errors);
		Integer personID = null;
		if (myExpenses) {
			personID = personBean.getObjectID();
		}
		try {
			return getFilterComputedValuesForPerson(criteria, personID, computedValueType);
		} catch (TorqueException e) {
			LOGGER.error("Loading the computed values for TQL filter " + tqlExpression + " and person "  + personID + " failed with " + e.getMessage(), e);
			return new ArrayList<TComputedValuesBean>();
		}
	}
	
	/**
	 * Get the computed values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @param computedValueTypes
	 * @return
	 */
	public List<TComputedValuesBean> loadByTQLFilter(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors, int[] computedValueTypes) {
		Criteria criteria = TqlBL.createCriteria(tqlExpression, personBean, locale, errors);
		try {
			return getFilterComputedValues(criteria, computedValueTypes);
		} catch (TorqueException e) {
			LOGGER.error("Loading the computed values for TQL filter " + tqlExpression + " failed with " + e.getMessage(), e);
			return new ArrayList<TComputedValuesBean>();
		}
	}
	
	
	/**
	 * Gets the computed values for a filter for a person
	 * @param criteria
	 * @param personID
	 * @param computedValueType
	 * @return
	 * @throws TorqueException
	 */
	private static List<TComputedValuesBean> getFilterComputedValuesForPerson(Criteria criteria, Integer personID, int computedValueType) throws TorqueException {
		criteria.addJoin(TWorkItemPeer.WORKITEMKEY, WORKITEMKEY);
		criteria.add(COMPUTEDVALUETYPE, computedValueType);
		if (personID==null) {
			criteria.add(PERSON, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PERSON, personID);
		}
		return convertTorqueListToBeanList(doSelect(criteria));
	}
	
	/**
	 * Gets the computed values for a filter for a person
	 * @param criteria
	 * @param computedValueTypes
	 * @return
	 * @throws TorqueException
	 */
	private static List<TComputedValuesBean> getFilterComputedValues(Criteria criteria, int[] computedValueTypes) throws TorqueException {
		criteria.addJoin(TWorkItemPeer.WORKITEMKEY, WORKITEMKEY);
		criteria.addIn(COMPUTEDVALUETYPE, computedValueTypes);
		return convertTorqueListToBeanList(doSelect(criteria));
	}
	
	/**
	 * Saves a new/existing computedValuesBean in the TComputedValues table
	 * @param listBean
	 * @return the created optionID
	 */
	public Integer save(TComputedValuesBean tComputedValuesBean) {
		TComputedValues tComputedValues;
		try {
			tComputedValues = BaseTComputedValues.createTComputedValues(tComputedValuesBean);
			tComputedValues.save();
			return tComputedValues.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a computed value failed with " + e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Deletes a record from the TComputedValues table
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(PKEY, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting a computed value by objectID " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes all records from the TComputedValues table
	 * @param objectID
	 */
	public void deleteAll() {
		Criteria crit = new Criteria();
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting all computed values failed with: " + e);
		}
	}
	
	/**
	 * Converts a list of TComputedValues torque objects to a list of TComputedValuesBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TComputedValuesBean> convertTorqueListToBeanList(List<TComputedValues> torqueList) {
		List<TComputedValuesBean> beanList = new LinkedList<TComputedValuesBean>();
		if (torqueList!=null) {
			for (TComputedValues tComputedValues : torqueList) {
				beanList.add(tComputedValues.getBean());
			}
		}
		return beanList;
	}
}
