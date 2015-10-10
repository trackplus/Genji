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


package com.aurel.track.fieldType.runtime.matchers.design;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.budgetCost.AccountingTimeTO;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;


public class AccountingTimeMatcherDT extends DoubleMatcherDT {
	private static final Logger LOGGER = LogManager.getLogger(AccountingTimeMatcherDT.class);
	
	public AccountingTimeMatcherDT(Integer fieldID) {
		super(fieldID);
	}
	
	/**
	 * The name of the jsp fragment which contains 
	 * the control for rendering the matcher value
	 * @return
	 */
	public String getMatchValueControlClass() {
		switch (relation) {
		case MatchRelations.EQUAL:
		case MatchRelations.NOT_EQUAL:
		case MatchRelations.GREATHER_THAN:
		case MatchRelations.GREATHER_THAN_EQUAL:
		case MatchRelations.LESS_THAN:
		case MatchRelations.LESS_THAN_EQUAL:		
			return MatchValueJSPNames.TIME_MATCHER_CLASS;
		}
		return MatchValueJSPNames.NONE_MATCHER_CLASS;		
	}
	
	/**
	 * The json configuration object for configuring the js control containing the value
	 * @param fieldID
	 * @param baseName: the name of the control: important by submit
	 * @param index
	 * @param value: the value to be set by render
	 * @param disabled: whether the control is disabled
	 * @param dataSource: for combos, lists etc. the datasource is loaded in getMatcherDataSource()
	 * @param projectIDs: the selected projects for getting the datasource for project dependent pickers/trees (releases)
	 * @param matcherRelation: the value might depend on matcherRelation
	 * @param locale
	 * @param personID
	 * @return
	 */
	@Override
	public String getMatchValueJsonConfig(Integer fieldID, String baseName, Integer index, Object value,
			boolean disabled, Object dataSource, Integer[] projectIDs, Integer matcherRelation, Locale locale, Integer personID) {
		StringBuilder stringBuilder = new StringBuilder("{");
		JSONUtility.appendStringValue(stringBuilder, JSONUtility.JSON_FIELDS.NAME, getName(baseName, index));
		JSONUtility.appendStringValue(stringBuilder, "valueName", getMergedName(baseName, index, "value"));
		JSONUtility.appendStringValue(stringBuilder, "unitName", getMergedName(baseName, index, "unit"));
		JSONUtility.appendIntegerValue(stringBuilder, "decimalPrecision", AccountingBL.EFFORT_DECIMAL_DIGITS);
		if (value!=null) {
			AccountingTimeTO accountingTimeTO = null;
			try {
				accountingTimeTO = (AccountingTimeTO)value;
			} catch(Exception ex) {
				LOGGER.warn("Converting the value to accountingTimeTO failed with " + ex.getMessage());
			}
			if (accountingTimeTO!=null) {
				JSONUtility.appendDoubleValue(stringBuilder, JSONUtility.JSON_FIELDS.VALUE, accountingTimeTO.getValue());
				JSONUtility.appendIntegerValue(stringBuilder, "unit", accountingTimeTO.getUnit());
			}
		}
		//JSONUtility.appendBooleanValue(stringBuilder, "allowDecimals", true);
		List<IntegerStringBean> unitOptions = AccountingBL.getTimeUnitOptionsList(locale);
		JSONUtility.appendIntegerStringBeanList(stringBuilder, "unitList", unitOptions);
		JSONUtility.appendBooleanValue(stringBuilder, JSONUtility.JSON_FIELDS.DISABLED, disabled, true);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * Builds the name of the client side controls for submit
	 * @param baseName
	 * @param index
	 * @return
	 */
	public static String getMergedName(String baseName, Integer index, String name) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(baseName).append("['").append(MergeUtil.mergeKey(index.toString(), name)).append("']").toString();		
	}
}
