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


package com.aurel.track.dao;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;

/**
 * This defines the Data Access Object (DAO) interface for custom fields.
 *
 */
public interface AttributeValueDAO {

	/**
	 * Loads an attributeValueBean from the TAttributeValue table by primary key
	 * @param objetcID
	 * @return
	 */
	TAttributeValueBean loadByPrimaryKey(Integer objetcID);
	
	
	/**
	 * Loads an attributeValueBean from the TAttributeValue table by field, parameter and workItem 
	 * @param field
	 * @param parameterCode
	 * @param workItem
	 * @return
	 */
	TAttributeValueBean loadBeanByFieldAndWorkItemAndParameter(Integer field, Integer workItem, Integer parameterCode);

	/**
	 * Loads a list of attributeValueBeans from the TAttributeValue table by workItem 
	 * @param workItem
	 * @return
	 */
	List<TAttributeValueBean> loadByWorkItem(Integer workItemID);
	
	/**
	 * Loads a list of user picker attributes by workItem 
	 * @param workItem
	 * @return
	 */
	List<TAttributeValueBean> loadUserPickerByWorkItem(Integer workItemID);
	
	/**
	 * Gets the user picker attributes by a list of workItemIDs
	 * @param allWorkItemIDs
	 * @return
	 */
	List<TAttributeValueBean> getUserPickerAttributesByWorkItems(List<Integer> allWorkItemIDs);
	
	/**
	 * Loads a list of attributeValueBeans from the TAttributeValue table by an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	List<TAttributeValueBean> loadByWorkItemKeys(int[] workItemIDs);
	
	/**
	 * Gets the custom attribute values filtered by filterSelectsTO and raciBean
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	List<TAttributeValueBean> loadTreeFilterAttributes(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID);
	
	/**
	 * Get the attribute values for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	List<TAttributeValueBean> loadTQLFilterAttributeValues(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors);
	
	/**
	 * Get the custom option type attributeValueBeans for an array of workItemIDs 
	 * @param workItemIDs
	 * @return
	 */
	List<TAttributeValueBean> loadLuceneCustomOptionAttributeValues(int[] workItemIDs);
	
	/**
	 * Whether a system option from list appears as custom attribute
	 * The reflection does not work because an additional condition
	 * should be satisfied (no direct foreign key relationship exists)
	 * @param objectIDs
	 * @param fieldID
	 */
	boolean isSystemOptionAttribute(List<Integer> objectIDs, Integer fieldID);
	
	/**
	 * Saves an attributeValueBean in the TAttributeValue table
	 * @param tAttributeValueBean
	 * @return
	 */
	Integer save(TAttributeValueBean tAttributeValueBean);
		
	
	/**
	 * Deletes an attributeValueBean(s) from the TAttributeValue table by field and workItem
	 * @param fieldID
	 * @param parameterCode
	 * @param workItemID 
	 */
	void delete(Integer fieldID, Integer parameterCode, Integer workItemID);
	
	/**
	 * Deletes the attributeValueBean(s) by field and a systemOption
	 * @param fieldID
	 * @param systemOptionID
	 * @param systemOptionType
	 */
	void deleteBySystemOption(Integer fieldID, Integer systemOptionID, Integer systemOptionType);
	
	/**
	 * Deletes an attributeValueBean from the TAttributeValue table by ID
	 * @param objectID
	 */
	void deleteByObjectID(Integer objectID);
}
