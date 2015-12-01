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
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.dao.AttributeValueDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.tql.TqlBL;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TAttributeValuePeer
    extends com.aurel.track.persist.BaseTAttributeValuePeer implements AttributeValueDAO
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TAttributeValuePeer.class);
	
	/**
	 * Loads the attribute value by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TAttributeValueBean loadByPrimaryKey(Integer objectID) {
		TAttributeValue tAttributeValue = null;
		try {
			tAttributeValue = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.info("Loading of an attribute value by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tAttributeValue!=null) {
			return tAttributeValue.getBean();
		}
		return null;
	}
	
	/**
	 * Loads an attributeValueBean from the TAttributeValue table by field, parameter and workItem 
	 * @param field
	 * @param workItem
	 * @param parameterCode 
	 * @return
	 */
	@Override
	public TAttributeValueBean loadBeanByFieldAndWorkItemAndParameter(Integer field, Integer workItem, Integer parameterCode) {
		List<TAttributeValue> attributeValueList =  null;
		Criteria criteria = new Criteria();
		criteria.add(FIELDKEY, field);
		criteria.add(WORKITEM, workItem);
		criteria.addDescendingOrderByColumn(LASTEDIT);
		if (parameterCode==null) {
			criteria.add(PARAMETERCODE, (Object)null, Criteria.ISNULL);
		} else {
			criteria.add(PARAMETERCODE, parameterCode);
		}
		try {
			attributeValueList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("Loading the attributevaluebean by field by field " + field + " parameterCode " + parameterCode +
					" and workItem " + workItem + " failed with " + e.getMessage(), e);
		}
		if (attributeValueList==null || attributeValueList.isEmpty()) {
			LOGGER.debug("No attributevaluebean found for field " + field + " parameterCode " + parameterCode + " and workItem" + workItem);
			return null; 
		}
		if (attributeValueList.size()>1) {
			LOGGER.warn("More than one attributevaluebean found for field " + field + " parameterCode " + parameterCode + " and workItem " + workItem + ". Delete them...");
			boolean first = true;
			for (Iterator<TAttributeValue> iterator = attributeValueList.iterator(); iterator.hasNext();) {
				if (!first) {
					TAttributeValue attributeValue = iterator.next();
					iterator.remove();
					deleteByObjectID(attributeValue.getObjectID());
				}
				first = false;
			}
		}
		return ((TAttributeValue)attributeValueList.get(0)).getBean();
	}

	/**
	 * Loads a list of attributeValueBeans from the TAttributeValue table by workItem 
	 * @param workItem
	 * @return
	 */
	@Override
	public List<TAttributeValueBean> loadByWorkItem(Integer workItem) {
		Criteria criteria = new Criteria();
		criteria.add(WORKITEM, workItem);
		//just in case there are accidentally more entries in the database
		criteria.addDescendingOrderByColumn(LASTEDIT);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the attributevaluebeans by workItem " + workItem + " failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Loads a list of user picker attributes by workItem 
	 * @param workItem
	 * @return
	 */
	@Override
	public List<TAttributeValueBean> loadUserPickerByWorkItem(Integer workItemID) {
		Criteria criteria = new Criteria();
		criteria.add(WORKITEM, workItemID);
		criteria.add(VALIDVALUE, ValueType.SYSTEMOPTION);
		criteria.add(SYSTEMOPTIONTYPE, SystemFields.INTEGER_PERSON);
		criteria.addDescendingOrderByColumn(LASTEDIT);
		try {
			return convertTorqueListToBeanList(doSelect(criteria));
		} catch (TorqueException e) {
			LOGGER.error("Loading the user picker attributevaluebeans by workItem " + workItemID + " failed with " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gets the user picker attributes by projects/releases
	 * @param projectReleaseIDList
	 * @param isProject
	 * @return
	 */
	/*public List<TAttributeValueBean> getProjectIDsReleaseIDsUserPickerAttributes(
			List<Integer> projectReleaseIDList, boolean isProject) {
		Criteria crit;
		if (isProject) {
			crit = ReportBeanLoader.prepareProjectIDsCriteria(projectReleaseIDList);
		} else {
			crit = ReportBeanLoader.prepareReleaseIDsCriteria(projectReleaseIDList);
		}
		addUserPickerConditions(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the user pickers for project/release failed with failed with " + e.getMessage());
			return null;
		}
	}*/
	
	/**
	 * Gets the user picker attributes by FilterSelectsTO
	 * @param filterSelectsTO
	 * @return
	 */
	/*public List<TAttributeValueBean> getCustomFilterUserPickerAttributes(FilterUpperTO filterSelectsTO) {
		Criteria crit = ReportBeanLoader.prepareCustomReportCriteria(filterSelectsTO);
		addUserPickerConditions(crit);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading the user pickers for custom filter failed with failed with " + e.getMessage());
			return null;
		}
	}*/

	/**
	 * Gets the user picker attributes by a list of workItemIDs
	 * @param allWorkItemIDs
	 * @return
	 */
	@Override
	public List<TAttributeValueBean> getUserPickerAttributesByWorkItems(List<Integer> workItemIDs) {
		List<TAttributeValueBean> attributeValueBeans = new LinkedList<TAttributeValueBean>();
		if (workItemIDs==null || workItemIDs.isEmpty()) {
			return attributeValueBeans;
		}
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		Iterator<int[]> chunkIterator = workItemIDChunksList.iterator();
		while (chunkIterator.hasNext()) {
			int[] workItemIDChunk = chunkIterator.next();
			Criteria crit = new Criteria();
			crit.addIn(WORKITEM, workItemIDChunk);
			crit.add(VALIDVALUE, ValueType.SYSTEMOPTION);
			crit.add(SYSTEMOPTIONTYPE, SystemFields.INTEGER_PERSON); 
			try {
				attributeValueBeans.addAll(convertTorqueListToBeanList(doSelect(crit)));
			} catch (TorqueException e) {
				LOGGER.error("Loading the user pickers by workItemIDs failed with failed with " + e.getMessage());
			}
		}
		return attributeValueBeans;
	}
	
	
	/**
	 * Whether a system option from list appears as custom attribute
	 * The reflection does not work because an additional condition
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param objectIDs
	 * @param fieldID
	 */
	@Override
	public boolean isSystemOptionAttribute(List<Integer> objectIDs, Integer fieldID) {
		if (objectIDs==null || objectIDs.isEmpty()) {
			return false;
		}
		List attributes = null;
		Criteria selectCriteria;
		List<int[]> chunkList = GeneralUtils.getListOfChunks(objectIDs);
		Iterator<int[]> iterator = chunkList.iterator();
		while (iterator.hasNext()) {
			int[] idChunk = iterator.next();
			selectCriteria = new Criteria();
			selectCriteria.addIn(SYSTEMOPTIONID, idChunk);
			selectCriteria.add(SYSTEMOPTIONTYPE, fieldID);
			selectCriteria.setDistinct();
			try {
				attributes = doSelect(selectCriteria);
			} catch (Exception e) {
				LOGGER.error("Verifiying the dependent " +
						"oldPersonIDs " + objectIDs.size() + " for the user picker failed with " + e.getMessage(), e);
			}
			if (attributes!=null && !attributes.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 *  Saves an attributeValueBean in the TAttributeValue table
	 * @param attributeValueBean
	 * @return
	 */
	@Override
	public Integer save(TAttributeValueBean attributeValueBean) {
		try {
			TAttributeValue tAttributeValue = BaseTAttributeValue.createTAttributeValue(attributeValueBean);
			tAttributeValue.save();
			return tAttributeValue.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of an attribute value failed with " + e.getMessage());
			return null;
		}
	}
		
	/**
	 * Deletes an attributeValueBean from the TAttributeValue table by ID
	 * @param objectID
	 */
	@Override
	public void deleteByObjectID(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting an attribute value by ID " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes an attribute value by primary key
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemID
	 */
	@Override
	public void delete(Integer fieldID, Integer parameterCode, Integer workItemID) {
		Criteria crit = new Criteria();
		crit.add(FIELDKEY, fieldID);
		crit.add(WORKITEM, workItemID);
		if (parameterCode == null) {
			crit.add(PARAMETERCODE, (Object)null, Criteria.ISNULL);
		} else {
			crit.add(PARAMETERCODE, parameterCode);
		}
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting an attribute value by fieldID" + fieldID + " and workItemID " + workItemID + " failed with: " + e);
		}
	}

	/**
	 * Deletes the attributeValueBean(s) by field and a systemOption
	 * @param fieldID
	 * @param systemOptionID
	 * @param systemOptionType
	 */
	@Override
	public void deleteBySystemOption(Integer fieldID, Integer systemOptionID, Integer systemOptionType) {
		Criteria crit = new Criteria();
		crit.add(FIELDKEY, fieldID);
		crit.add(SYSTEMOPTIONID, systemOptionID);
		crit.add(SYSTEMOPTIONTYPE, systemOptionType);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting by fieldID " + fieldID + ", systemOptionID " + systemOptionID + " and systemOptionType " + systemOptionType + " failed with: " + e);
		}
	}
	
	/**
	 * Gets the attributeValueBeans for a prepared criteria
	 * @param preparedCriteria
	 * @return
	 * @throws TorqueException
	 */
	private static List<TAttributeValueBean> getCustomAttributeValues(Criteria preparedCriteria) throws TorqueException {
		preparedCriteria.addJoin(TWorkItemPeer.WORKITEMKEY,  TAttributeValuePeer.WORKITEM);
		return convertTorqueListToBeanList(doSelect(preparedCriteria));
	}
	
	/**
	 * Gets the custom option attributeValueBeans for a prepared criteria 
	 * @param preparedCriteria
	 * @return
	 * @throws TorqueException
	 */
	private static List<TAttributeValueBean> getReportCustomOptionAttributeValues(Criteria preparedCriteria) throws TorqueException {
		preparedCriteria.addJoin(BaseTWorkItemPeer.WORKITEMKEY,  BaseTAttributeValuePeer.WORKITEM);
		preparedCriteria.addJoin(BaseTAttributeValuePeer.CUSTOMOPTIONID, BaseTOptionPeer.OBJECTID);
		return convertTorqueListToBeanList(doSelect(preparedCriteria));
	}
	
	/**
	 * Gets the custom attribute values filtered by filterSelectsTO and raciBean
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	@Override
	public List<TAttributeValueBean> loadTreeFilterAttributes(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID) {
		Integer[] selectedProjects = filterSelectsTO.getSelectedProjects();
		if (selectedProjects==null  || selectedProjects.length==0) {
			//at least one selected project needed
			return new ArrayList<TAttributeValueBean>();
		}		
		Criteria crit = TreeFilterCriteria.prepareTreeFilterCriteria(filterSelectsTO, raciBean, personID);
		try {
			return getCustomAttributeValues(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading the custom attributes for tree filter failed with " + e.getMessage());
			return new ArrayList<TAttributeValueBean>();
		}
	}
	
	/**
	 * Get the attribute values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	@Override
	public List<TAttributeValueBean> loadTQLFilterAttributeValues(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors) {
		try {
			return getCustomAttributeValues(TqlBL.createCriteria(tqlExpression, personBean, locale, errors));
		} catch (TorqueException e) {
			LOGGER.error("Loading the custom option attribute values for TQL report failed with " + e.getMessage());
			return new ArrayList<TAttributeValueBean>();
		}	
	}

	
	/**
	 * Loads a list of attributeValueBeans from the TAttributeValue table by an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	@Override
	public List<TAttributeValueBean> loadByWorkItemKeys(int[] workItemIDs) {
		List<TAttributeValueBean> attributeValueBeansList = new ArrayList<TAttributeValueBean>();
		List<TAttributeValue> tav = new ArrayList<TAttributeValue>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return attributeValueBeansList;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return attributeValueBeansList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(WORKITEM, workItemIDChunk);
			try {
				tav.addAll(doSelect(criteria));
			} catch(Exception e) {
	        	LOGGER.error("Loading the attributeValueBeans by workItemIDs failed with " + e.getMessage());
	        }			
		}
        return convertTorqueListToBeanList(tav);	
	}
	
	/**
	 * Get the custom option type attributeValueBeans for an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	@Override
	public List<TAttributeValueBean> loadLuceneCustomOptionAttributeValues(int[] workItemIDs) {
		List<TAttributeValueBean> attributeValueBeansList = new ArrayList<TAttributeValueBean>();
		if (workItemIDs==null || workItemIDs.length==0) {
			return attributeValueBeansList;
		}
		Criteria criteria;
		List<int[]> workItemIDChunksList = GeneralUtils.getListOfChunks(workItemIDs);
		if (workItemIDChunksList==null) {
			return attributeValueBeansList;
		}
		Iterator<int[]> iterator = workItemIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] workItemIDChunk = iterator.next();
			criteria = new Criteria();
			criteria.addIn(WORKITEM, workItemIDChunk);
			try {
				attributeValueBeansList.addAll(getReportCustomOptionAttributeValues(criteria));
			} catch(Exception e) {
				LOGGER.error("Loading the custom option type attributeValueBeans by workItemIDs failed with " + e.getMessage());
			}
		}
		return attributeValueBeansList;
	}
		
	
    /**
	 * Converts a list of TOption torque objects to a list of TOptionBean objects 
	 * @param torqueList
	 * @return
	 */
	private static List<TAttributeValueBean> convertTorqueListToBeanList(List<TAttributeValue> torqueList) {
		List<TAttributeValueBean> beanList = new ArrayList<TAttributeValueBean>();
		if (torqueList!=null) {
			Iterator<TAttributeValue> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()) {
				TAttributeValue tAttributeValue = itrTorqueList.next();
				beanList.add(tAttributeValue.getBean());
			}
		}
		return beanList;
	}
}
