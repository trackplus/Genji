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

package com.aurel.track.fieldType.runtime.matchers.converter;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.item.budgetCost.AccountingBL.TIMEUNITS;
import com.aurel.track.item.budgetCost.AccountingTimeTO;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * Used to convert a value from String (XML or displayValue) to (Double) value + (Integer) unit and back
 */
public class AccountingTimeMatcherConverter implements MatcherConverter {
	private static final Logger LOGGER = LogManager.getLogger(AccountingTimeMatcherConverter.class);
	private static String SPLITTER_STRING = "#";
	private static AccountingTimeMatcherConverter instance;
	public static AccountingTimeMatcherConverter getInstance(){
		if(instance==null){
			instance=new AccountingTimeMatcherConverter();
		}
		return instance;
	}
	
	/**
	 * Convert the object value to xml string for save
	 * @param value
	 * @param matcherRelation
	 * @return
	 */
	@Override
	public String toXMLString(Object value, Integer matcherRelation) {
		if (value==null || matcherRelation==null) {
			return null;
		}
		AccountingTimeTO accountingTimeTO = (AccountingTimeTO)value;
		if (accountingTimeTO.getValue()==null) {
			return null;
		}
		if (accountingTimeTO.getUnit()==null) {
			accountingTimeTO.setUnit(TIMEUNITS.HOURS);
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
		case MatchRelations.GREATHER_THAN:
		case MatchRelations.GREATHER_THAN_EQUAL:
		case MatchRelations.LESS_THAN:
		case MatchRelations.LESS_THAN_EQUAL:
			return accountingTimeTO.getValue() + SPLITTER_STRING + accountingTimeTO.getUnit();
		}
		return null;
	}

	/**
	 * Convert the xml string to object value after load
	 * @param value
	 * @param matcherRelation
	 * @return
	 */
	@Override
	public Object fromXMLString(String value, Integer matcherRelation) {
		if (value==null || "".equals(value) || matcherRelation==null) {
			return null;
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
		case MatchRelations.GREATHER_THAN:
		case MatchRelations.GREATHER_THAN_EQUAL:
		case MatchRelations.LESS_THAN:
		case MatchRelations.LESS_THAN_EQUAL:
			AccountingTimeTO accountingTimeTO = new AccountingTimeTO();
			String[] partsArr = value.split(SPLITTER_STRING);
			if (partsArr!=null && partsArr.length>0) {
				try {
					String doubleStr = partsArr[0];
					if (doubleStr!=null && !"".equals(doubleStr) && !"null".equals(doubleStr)) {
						accountingTimeTO.setValue(Double.valueOf(doubleStr));
					}
				} catch (Exception e) {
					LOGGER.warn("Converting the " + partsArr[0] +  " to Double from xml string failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				if (partsArr.length>1) {
					try {
						String unitStr = partsArr[1];
						if (unitStr!=null && !"".equals(unitStr) && !"null".equals(unitStr)) {
							accountingTimeTO.setUnit(Integer.valueOf(unitStr));
						}
					} catch (Exception e) {
						LOGGER.warn("Converting the " + partsArr[1] +  " to Integer from xml string failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			return accountingTimeTO;
		}
		return null;
	}

	
	/**
	 * Convert a string from displayStringMap to object value
	 * Called after submitting the filter's form
	 * @param displayStringMap
	 * @param fieldID
	 * @param locale
	 * @param matcherRelation
	 * @return
	 */
	@Override
	public Object fromDisplayString(Map<String, String> displayStringMap,
			Integer fieldID, Locale locale, Integer matcherRelation) {
		if (displayStringMap == null) {
			return null;
		}
		if (matcherRelation!=null) {
			switch (matcherRelation.intValue()) {
			case MatchRelations.EQUAL:
			case MatchRelations.NOT_EQUAL:
			case MatchRelations.GREATHER_THAN:
			case MatchRelations.GREATHER_THAN_EQUAL:
			case MatchRelations.LESS_THAN:
			case MatchRelations.LESS_THAN_EQUAL:
				AccountingTimeTO accountingTimeTO = new AccountingTimeTO();
				String valueKey = MergeUtil.mergeKey(fieldID.toString(), "value");
				String valueStr = displayStringMap.get(valueKey);
				if (valueStr!=null) {
					accountingTimeTO.setValue(DoubleNumberFormatUtil.getInstance().parseGUI(valueStr, locale));
				}
				String unitKey = MergeUtil.mergeKey(fieldID.toString(), "unit");
				String unitStr = displayStringMap.get(unitKey);
				if (unitStr!=null) {
					try {
						accountingTimeTO.setUnit(Integer.valueOf(unitStr));
					} catch(Exception ex) {
						LOGGER.warn("Converting the unit " + unitStr +  " to Integer from display string failed with " + ex.getMessage());
					}
				}
				return accountingTimeTO;
			}
		}
		return null;
	}
	
	/**
	 * Convert a string to object value
	 * By changing the matcherID for a field expression locally
	 * (not after submitting a form). The reason for this is to maintain the existing
	 * value if the new matcher is "compatible" with the old matcher 
	 * @param valueString
	 * @param locale
	 * @param matcherRelation
	 * @return
	 */
	@Override
	public Object fromValueString(String valueString, Locale locale, Integer matcherRelation) {
		if (valueString==null || "".equals(valueString) || matcherRelation==null) {
			return null;
		}
		switch (matcherRelation.intValue()) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
		case MatchRelations.GREATHER_THAN:
		case MatchRelations.GREATHER_THAN_EQUAL:
		case MatchRelations.LESS_THAN:
		case MatchRelations.LESS_THAN_EQUAL:
			AccountingTimeTO accountingTimeTO = new AccountingTimeTO();
			String[] partsArr = valueString.split(SPLITTER_STRING);
			if (partsArr!=null && partsArr.length>0) {
				try {	
					accountingTimeTO.setValue(DoubleNumberFormatUtil.getInstance().parseGUI(partsArr[0], locale));
				} catch (Exception e) {
					LOGGER.warn("Converting the " + partsArr[0] +  " to Double from xml string failed with " + e.getMessage());
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				if (partsArr.length>1) {
					try {	
						accountingTimeTO.setUnit(Integer.valueOf(partsArr[1]));
					} catch (Exception e) {
						LOGGER.warn("Converting the " + partsArr[1] +  " to Integer from xml string failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
			return accountingTimeTO;
		}
		return null;
	}
}
