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


package com.aurel.track.item.history;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.util.EqualUtils;

public class HistoryDAOUtils {
	
	private static final Logger LOGGER = LogManager.getLogger(HistoryDAOUtils.class);
	
	/**
	 * Insert a fieldChange
	 * @param fieldID
	 * @param parameterCode
	 * @param historyTransactionID
	 * @param newValue
	 * @param oldValue
	 * @param valueType
	 * @param systemOptionType
	 */
	public static Integer insertFieldChange(Integer fieldID, Integer parameterCode, Integer historyTransactionID,  			
			Object newValue, Object oldValue, int valueType, Integer systemOptionType) throws ItemPersisterException {
			TFieldChangeBean fieldChangeBean = new TFieldChangeBean(); 
			fieldChangeBean.setFieldKey(fieldID);
			fieldChangeBean.setHistoryTransaction(historyTransactionID);
			HistoryDAOUtils.setSpecificAttribute(fieldChangeBean, newValue, oldValue, valueType);
			fieldChangeBean.setSystemOptionType(systemOptionType);		
			fieldChangeBean.setParameterCode(parameterCode);
			fieldChangeBean.setValidValue(Integer.valueOf(valueType));
			return FieldChangeBL.save(fieldChangeBean);					
	}
	
	/**
	 * Update a fieldChange: now possible only for editing a comment
	 * (the other history entries are read only) 
	 * @param fieldChangeID
	 * @param newValue
	 * @param valueType
	 */
	public static Integer updateFieldChange(TFieldChangeBean fieldChangeBean, Object newValue, int valueType) throws ItemPersisterException {
		//if (fieldChangeID!=null) {
			//TFieldChangeBean fieldChangeBean = FieldChangeBL.loadByPrimaryKey(fieldChangeID);
			if (fieldChangeBean!=null) {
				Object oldValue = getSpecificNewAttribute(fieldChangeBean, valueType);
				if (!EqualUtils.valueModified(newValue, oldValue)) {
					HistoryDAOUtils.deleteFieldChange(fieldChangeBean);
				} else {
					Integer timesEdited =  fieldChangeBean.getTimesEdited();
					if (timesEdited==null) {
						fieldChangeBean.setTimesEdited(Integer.valueOf(1));
					} else {
						fieldChangeBean.setTimesEdited(Integer.valueOf(timesEdited.intValue()+1));
					}				
					HistoryDAOUtils.setSpecificAttribute(fieldChangeBean, newValue, oldValue, valueType);
					return FieldChangeBL.save(fieldChangeBean);
				}
			}	
		//}
		return null;
	}
	
	/**
	 * Delete a fieldChange: now possible only for deleting a comment
	 * (the other history entries are not removable) 
	 * @param objectID	
	 */
	public static void deleteFieldChange(TFieldChangeBean fieldChangeBean)  {
		if (fieldChangeBean!=null) {
			Integer historyTransaction = fieldChangeBean.getHistoryTransaction();
			FieldChangeBL.delete(fieldChangeBean.getObjectID());
			if (!FieldChangeBL.hasFieldChanges(historyTransaction)) {
				//if only one field (comment or recent field change) was included in the
				//history transaction then delete the history transaction also 
				HistoryTransactionBL.delete(historyTransaction);
			}
		}
	}
	
	/**
	 * Delete fieldChanges for a list of fieldIDs (for deleting fields)
	 * @param objectID	
	 */
	public static void deleteFieldChangesByFieldID(Integer fieldID)  {
		//any entries with explicit history for the fields
		List<TFieldChangeBean> fieldChangeBeanList = FieldChangeBL.getByFieldID(fieldID);
		Set<Integer> historyTransactionsSet = new HashSet<Integer>();
		if (fieldChangeBeanList!=null) {
			for (TFieldChangeBean fieldChangeBean : fieldChangeBeanList) {
				historyTransactionsSet.add(fieldChangeBean.getHistoryTransaction());
			}
		}
		//delete the field changes by fieldIDs
		FieldChangeBL.deleteByFieldID(fieldID);
		for (Integer historyTransactionID : historyTransactionsSet) {
			if (!FieldChangeBL.hasFieldChanges(historyTransactionID)) {
				//if only the already deleted fields were included in the
				//history transaction then delete the history transaction also 
				HistoryTransactionBL.delete(historyTransactionID);
			}	
		}	
	}
	
	public static Object getSpecificNewAttribute(TFieldChangeBean fieldChangeBean, int valueType) {
		if (fieldChangeBean==null) {
			return null;
		}
		switch (valueType) {
		case ValueType.BOOLEAN:
			String charValue = fieldChangeBean.getNewCharacterValue();
			Boolean booleanValue = Boolean.FALSE;
			if (charValue!=null) {
				if (BooleanFields.TRUE_VALUE.equals(charValue)) {
					booleanValue = Boolean.TRUE;
				}
			}
			return booleanValue;
		case ValueType.CUSTOMOPTION:
			return fieldChangeBean.getNewCustomOptionID();
		case ValueType.DATE:
		case ValueType.DATETIME:
			return fieldChangeBean.getNewDateValue();
		case ValueType.DOUBLE:
			return fieldChangeBean.getNewDoubleValue();
		case ValueType.INTEGER:
		case ValueType.EXTERNALID:
			return fieldChangeBean.getNewIntegerValue();
		case ValueType.LONGTEXT:
			return fieldChangeBean.getNewLongTextValue();
		case ValueType.SYSTEMOPTION:
			return fieldChangeBean.getNewSystemOptionID();
		case ValueType.TEXT:
			return fieldChangeBean.getNewTextValue();
		default:
			return null;
		}
	}
		
	public static Object getSpecificOldAttribute(TFieldChangeBean fieldChangeBean, int valueType) {
		if (fieldChangeBean==null) {
			return null;
		}
		switch (valueType) {
		case ValueType.BOOLEAN:
			String charValue = fieldChangeBean.getOldCharacterValue();
			Boolean booleanValue = Boolean.FALSE;
			if (charValue!=null) {
				if (BooleanFields.TRUE_VALUE.equals(charValue)) {
					booleanValue = Boolean.TRUE;
				}
			}
			return booleanValue;
		case ValueType.CUSTOMOPTION:
			return fieldChangeBean.getOldCustomOptionID();
		case ValueType.DATE:
		case ValueType.DATETIME:
			return fieldChangeBean.getOldDateValue();
		case ValueType.DOUBLE:
			return fieldChangeBean.getOldDoubleValue();
		case ValueType.INTEGER:
		case ValueType.EXTERNALID:
			return fieldChangeBean.getOldIntegerValue();
		case ValueType.LONGTEXT:
			return fieldChangeBean.getOldLongTextValue();
		case ValueType.SYSTEMOPTION:
			return fieldChangeBean.getOldSystemOptionID();
		case ValueType.TEXT:
			return fieldChangeBean.getOldTextValue();
		default:
			return null;
		}
	}
	
	private static void setSpecificAttribute(TFieldChangeBean fieldChangeBean, 
			Object newValue, Object oldValue, int valueType) {
		switch (valueType) {
		case ValueType.BOOLEAN:
			Boolean newBooleanAttribute = null;
			try {
				newBooleanAttribute = (Boolean)newValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for new boolean " + e.getMessage(), e);
			}		
			if (newBooleanAttribute!=null && newBooleanAttribute.booleanValue()==true) {
				fieldChangeBean.setNewCharacterValue(BooleanFields.TRUE_VALUE);			
			} else {
				fieldChangeBean.setNewCharacterValue(BooleanFields.FALSE_VALUE);
			}
			Boolean oldBooleanAttribute = null;
			try {
				oldBooleanAttribute = (Boolean)oldValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for old boolean " + e.getMessage(), e);
			}		
			if (oldBooleanAttribute!=null && oldBooleanAttribute.booleanValue()==true) {
				fieldChangeBean.setOldCharacterValue(BooleanFields.TRUE_VALUE);			
			} else {
				fieldChangeBean.setOldCharacterValue(BooleanFields.FALSE_VALUE);
			}
			break;
		case ValueType.CUSTOMOPTION:
			Integer newCustomOptionAttribute = null;
			try {
				newCustomOptionAttribute = (Integer)newValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for new custom option " + e.getMessage(), e);
			}		
			fieldChangeBean.setNewCustomOptionID(newCustomOptionAttribute);
			Integer oldCustomOptionAttribute = null;
			try {
				oldCustomOptionAttribute = (Integer)oldValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for old custom option " + e.getMessage(), e);
			}		
			fieldChangeBean.setOldCustomOptionID(oldCustomOptionAttribute);
			break;
		case ValueType.DATE:
		case ValueType.DATETIME:	
			Date newDateAttribute = null;
			try {
				newDateAttribute = (Date)newValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for new date option " + e.getMessage(), e);
			}		
			fieldChangeBean.setNewDateValue(newDateAttribute);
			Date oldDateAttribute = null;
			try {
				oldDateAttribute = (Date)oldValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for old date option " + e.getMessage(), e);
			}		
			fieldChangeBean.setOldDateValue(oldDateAttribute);
			break;
		case ValueType.DOUBLE:
			Double newDoubleAttribute = null;
			try {
				newDoubleAttribute = (Double)newValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for new double option " + e.getMessage(), e);
			}
			fieldChangeBean.setNewDoubleValue(newDoubleAttribute);
			Double oldDoubleAttribute = null;
			try {
				oldDoubleAttribute = (Double)oldValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for old double option " + e.getMessage(), e);
			}
			fieldChangeBean.setOldDoubleValue(oldDoubleAttribute);
			break;
		case ValueType.INTEGER:
		case ValueType.EXTERNALID:
			Integer newIntegerAttribute = null;
			try {
				newIntegerAttribute = (Integer)newValue;
			}	
			catch (Exception e) {
				LOGGER.warn("Wrong attribute type for new integer option " + e.getMessage(), e);
			}
			fieldChangeBean.setNewIntegerValue(newIntegerAttribute);
			Integer oldIntegerAttribute = null;
			try {
				oldIntegerAttribute = (Integer)oldValue;
			}	
			catch (Exception e) {
				LOGGER.warn("Wrong attribute type for old integer option " + e.getMessage(), e);
			}
			fieldChangeBean.setOldIntegerValue(oldIntegerAttribute);
			break;
		case ValueType.LONGTEXT:
			String newLongTextAttribute = null;
			int maximalLength = ApplicationBean.getInstance().getCommentMaxLength();
			try {
				newLongTextAttribute = (String)newValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type new longTextAttribute " + e.getMessage(), e);
			}
			if (newLongTextAttribute!=null && newLongTextAttribute.length()>maximalLength) {
				LOGGER.debug("New long text is of length " + newLongTextAttribute.length() + " and will be cut to length " + maximalLength);				
				newLongTextAttribute = newLongTextAttribute.substring(0, maximalLength-1);
			}
			fieldChangeBean.setNewLongTextValue(newLongTextAttribute);
			String oldLongTextAttribute = null;
			try {
				oldLongTextAttribute = (String)oldValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type old longTextAttribute " + e.getMessage(), e);
			}
			if (oldLongTextAttribute!=null && oldLongTextAttribute.length()>maximalLength) {
				LOGGER.debug("Old long text is of length " + oldLongTextAttribute.length() + " and will be cut to length " + maximalLength);
				oldLongTextAttribute = oldLongTextAttribute.substring(0, maximalLength-1);
			}
			fieldChangeBean.setOldLongTextValue(oldLongTextAttribute);
			break;
		case ValueType.SYSTEMOPTION:
			Integer newSystemOptionAttribute = null;
			try {
				newSystemOptionAttribute = (Integer)newValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for new system option " + e.getMessage(), e);
			}		
			fieldChangeBean.setNewSystemOptionID(newSystemOptionAttribute);
			Integer oldSystemOptionAttribute = null;
			try {
				oldSystemOptionAttribute = (Integer)oldValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type for old system option " + e.getMessage(), e);
			}		
			fieldChangeBean.setOldSystemOptionID(oldSystemOptionAttribute);
			break;
		case ValueType.TEXT:
			String newShortTextAttribute = null;
			try {
				newShortTextAttribute = (String)newValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type new shortTextAttribute " + e.getMessage(), e);
			}
			if (newShortTextAttribute!=null && newShortTextAttribute.length()>255) {
				LOGGER.debug("New short text is of length " + newShortTextAttribute.length() + " and will be cut to length " + 255);
				newShortTextAttribute = newShortTextAttribute.substring(0, 255);
			}
			fieldChangeBean.setNewTextValue(newShortTextAttribute);
			String oldShortTextAttribute = null;
			try {
				oldShortTextAttribute = (String)oldValue;
			} catch (Exception e) {
				LOGGER.warn("Wrong attribute type old shortTextAttribute " + e.getMessage(), e);
			}
			if (oldShortTextAttribute!=null && oldShortTextAttribute.length()>255) {
				LOGGER.debug("Old short text is of length " + oldShortTextAttribute.length() + " and will be cut to length " + 255);
				oldShortTextAttribute = oldShortTextAttribute.substring(0, 255);
			}
			fieldChangeBean.setOldTextValue(oldShortTextAttribute);
			break;
		}
	}

	/*public static Integer saveHistoryTransaction(Integer workItemID, Integer personID) {
		THistoryTransactionBean historyTransactionBean = new THistoryTransactionBean();
		historyTransactionBean.setWorkItem(workItemID);
		historyTransactionBean.setChangedByID(personID);
		historyTransactionBean.setLastEdit(new Date());
		return HistorySaverBL.historyTransactionDAO.save(historyTransactionBean);
	}*/

}
