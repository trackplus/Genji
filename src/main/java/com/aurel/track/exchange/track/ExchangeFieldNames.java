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


package com.aurel.track.exchange.track;

import com.aurel.track.beans.TReportLayoutBean;

public interface ExchangeFieldNames {
	public static String BUDGET_EXPENSE_UNIT = "unit";
	public static String TRACKPLUS_EXCHANGE = "trackplusExchange";
	public static String ITEM = "item";
	public static String CONSULTANT_LIST = TReportLayoutBean.PSEUDO_COLUMN_NAMES.CONSULTANT_LIST;
	public static String CONSULTANT = "consultant";	
	public static String INFORMANT_LIST = TReportLayoutBean.PSEUDO_COLUMN_NAMES.INFORMANT_LIST;	
	public static String INFORMANT = "informant";
	public static String PERSON = "person";
	public static String DROPDOWN_ELEMENT = "dropdownElement";	
	public static String FIELDID = "fieldID";
	public static String PARAMETERCODE = "parameterCode";
	public static String TIMES_EDITED = "timesEdited";
	public static String ITEM_ATTRIBUTE = "itemAttribute";
	public static String OBJECTID = "objectID";
	public static String HISTORY_LIST = "history";
	public static String TRANSACTION = "transaction";
	public static String FIELDCHANGE = "fieldChange";
	public static String CHANGEDBY = "changedBy";
	public static String CHANGEDAT = "changedAt";
	public static String NEWVALUE = "newValue";
	public static String OLDVALUE = "oldValue";
	public static String UUID = "uuid";
	public static String WORKITEMID = "itemID";
	
	public static String BUDGET_LIST = "budgetList";	
	public static String BUDGET_ELEMENT = "budgetElement";
	public static String PLANNED_VALUE_LIST = "plannedValueList";	
	public static String PLANNED_VALUE_ELEMENT = "plannedValueElement";
	public static String REMAINING_BUDGET_ELEMENT = "remainingBudgetElement";
	public static String EXPENSE_LIST = "expenseList";
	public static String EXPENSE_ELEMENT = "expenseElement";
	public static String ATTACHMENT_LIST = "attachmentList";
	public static String ATTACHMENT_ELEMENT = "attachment";
		
	public static String ACCOUNT = "account";
	public static String COSTCENTER = "costcenter";
	public static String DEPARTMENT = "department";
	public static String SYSTEMSTATE = "systemState";
	public static String PROJECTTYPE = "projetcType";
	public static String LIST = "list";
	public static String OPTION = "option";
	
	public static String FIELD = "field";
	public static String FIELDCONFIG = "fieldConfig";
	public static String OPTIONSETTINGS = "optionSettings";
	public static String TEXTBOXSETTINGS = "texBoxSettings";
	public static String GENERALSETTINGS = "generalSettings";
	public static String ROLE = "role";
	public static String ACL = "acl";
	//now only parent and issues links in long texts, but later also the issue links
	public static String LINKED_ITEMS = "linkedItems";
	
	public static String SCREENPANEL = "screenPanel";
	public static String SCREENTAB = "screenTab";
	public static String SCREEN = "screen";
	
	public static String EXCHANGE_ZIP_ENTRY = "exchange.xml";
}
