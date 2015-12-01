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


package com.aurel.track.beans;

import java.io.Serializable;

import com.aurel.track.fieldType.constants.SystemFields;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TReportLayoutBean
    extends com.aurel.track.beans.base.BaseTReportLayoutBean
    implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public interface FIELD_TYPE {
		public static final int VISIBLE=0;
		public static final int HIDDEN=1;
		public static final int GROUP=2;
	}
	
	public interface LAYOUT_TYPE {
		public static final int REPORT_OVERVIEW=0;
		public static final int HISTORY=1;
		public static final int COMMENT=2;
		public static final int ATTACHMENT=3;
		public static final int WORKLOG=4;
		public static final int WATCHER=5;
		public static final int VERSION_CONTROL=6;
		public static final int LINK=7;
	}
	public interface HISTORY {
		public static final int LAST_EDIT=0;
		public static final int CHANGE_BY=1;
		public static final int TYPE_OF_CHANGE=2;
		public static final int NEW_VALUE=3;
		public static final int OLD_VALUE=4;
		public static final int COMMENT=5;
		public static final int DIFF=6;
	}
	public interface COMMENT {
		public static final int LAST_EDIT=0;
		public static final int CHANGE_BY=1;
		public static final int COMMENT=2;
	}
	public interface WORKLOG {
		public static final int LAST_EDIT=0;
		public static final int CHANGE_BY=1;
		public static final int SUBJECT=2;
		public static final int WORK=3;
		public static final int COST=4;
		public static final int ACCOUNT=5;
		public static final int DESCRIPTION=6;
		public static final int EFFORT_DATE=7;
	}
	public interface ATTACHMENT {
		public static final int LAST_EDIT=0;
		public static final int CHANGE_BY=1;
		public static final int FILE_NAME=2;
		public static final int DESCRIPTION=3;
		public static final int FILE_SIZE=4;
		public static final int THUMBNAIL=5;
	}
	public interface WATCHER {
		public static final int TYPE=0;
		public static final int PERSON=1;
		//public static final int IS_GROUP=2;
	}
	public interface VERSION_CONTROL {
		public static final int REVISION_NO=0;
		public static final int REPOSITORY=1;
		public static final int USER=2;
		public static final int MESSAGE=3;
		public static final int DATE=4;
	}
	
	public interface LINK {
		public static final int LINK_TYPE=0;
		public static final int ITEM_ID=1;
		public static final int ITEM_TITLE=2;
		public static final int PARAMETERS=3;
		public static final int COMMENT=4;
		public static final int ITEM_STATUS=5;
		public static final int ITEM_RESPONSIBLE=6;
	}
	
	/**
	 * the Pseudo column identifiers 
	 * They should be different from any existing field number 
	 */	
	public interface PSEUDO_COLUMNS {
		
		public static final int CHECKBOX_FIELD_ID = -1000;
		public static final int ATTACHMENT_SYMBOL = -1001;
		//watchers lists
		public static final int CONSULTANT_LIST = -1003;
		public static final int INFORMANT_LIST = -1004;
		
		public static final int HISTORY_LIST = -1005;
		public static final int PRIVATE_SYMBOL = -1006;
		
		public static final int BUDGET_HISTORY_LIST = -1007;
		public static final int PLAN_HISTORY_LIST = -1011;
		public static final int COST_LIST = -1008;
		
		public static final int LINKED_ITEMS = -1009;
		
		public static final int OVERFLOW_ICONS = -1010;
		
		public static final int PRIORITY_SYMBOL = -SystemFields.PRIORITY;
		public static final int SEVERITY_SYMBOL = -SystemFields.SEVERITY;
		public static final int STATUS_SYMBOL = -SystemFields.STATE;
		public static final int ISSUETYPE_SYMBOL = -SystemFields.ISSUETYPE;

		public static final int ORIGINATOR_SYMBOL = -SystemFields.ORIGINATOR;
		public static final int MANAGER_SYMBOL = -SystemFields.MANAGER;
		public static final int RESPONSIBLE_SYMBOL = -SystemFields.RESPONSIBLE;

		
		//total expense fields
		public static final int TOTAL_EXPENSE_COST = -1100;
		public static final int TOTAL_EXPENSE_TIME = -1101;
		
		//my expense fields
		public static final int MY_EXPENSE_COST = -1200;
		public static final int MY_EXPENSE_TIME = -1201;
		
		//total planned fields (bottom up)
		public static final int TOTAL_PLANNED_COST = -1300;
		public static final int TOTAL_PLANNED_TIME = -1301;
		
		//remaining budget fields
		public static final int REMAINING_PLANNED_COST = -1400;
		public static final int REMAINING_PLANNED_TIME = -1401;
		
		//budget fields (top down)
		public static final int BUDGET_COST = -1600;
		public static final int BUDGET_TIME = -1601;

		public static final int INDEX_NUMBER=-1700;

	}
	
	/**
	 * the Pseudo column names 
	 * to be serialized in the XML and used in reports  
	 */	
	public interface PSEUDO_COLUMN_NAMES {

		public static final String CHECKBOX_FIELD_ID = "MassOperation";
		public static final String INDEX_NUMBER = "IndexNumber";

		public static final String ATTACHMENT_SYMBOL = "AttachmentSymbol";
		//watchers lists
		public static final String CONSULTANT_LIST = "Consultants";
		public static final String INFORMANT_LIST = "Informants";
		
		public static final String PRIVATE_SYMBOL = "PrivateSymbol";
		public static final String PRIORITY_SYMBOL = "PrioritySymbol";
		public static final String SEVERITY_SYMBOL = "SeveritySymbol";
		public static final String STATUS_SYMBOL = "StatusSymbol";
		public static final String ISSUETYPE_SYMBOL = "IssueTypeSymbol";
		public static final String CUSTOM_OPTION_SYMBOL = "CustomSymbol";
		public static final String OVERFLOW_SYMBOL = "OverflowSymbol";
		
		//total expense fields
		public static final String TOTAL_EXPENSE_COST = "TotalExpenseCost";
		public static final String TOTAL_EXPENSE_TIME = "TotalExpenseTime";
		
		//my expense fields
		public static final String MY_EXPENSE_COST = "MyExpenseCost";
		public static final String MY_EXPENSE_TIME = "MyExpenseTime";
		
		//total planned fields
		//TODO is this change in field name in xml backward compatible?
		public static final String TOTAL_PLANNED_COST = "TotalPlannedCost";//"TotalBudgetCost";
		public static final String TOTAL_PLANNED_TIME = "TotalPlannedTime";//"TotalBudgetTime";
		
		//remaining budget fields
		public static final String REMAINING_PLANNED_COST = "RemainingBudgetCost";
		public static final String REMAINING_PLANNED_TIME = "RemainingBudgetTime";
			
		//total budget fields
		public static final String BUDGET_COST = "BudgetCost";
		public static final String BUDGET_TIME = "BudgetTime";
				
		public static final String HISTORY = "history";
		public static final String BUDGETHISTORY = "budgetHistory";
		public static final String PLANHISTORY = "planHistory";
		public static final String COSTS = "costs";
		public static final String LINKED_ITEMS = "linkedItems";
		
		public static final String STATUS_FLAG = "StateFlag";
		public static final String INDENT_LEVEL = "indent-level";
		public static final String COMMITTED_DATE_CONFLICT = "committedDateConflict";
		public static final String TARGET_DATE_CONFLICT = "targetDateConflict";
		public static final String PLANNED_VALUE_CONFLICT = "plannedValueConflict"; 
		public static final String BUDGET_CONFLICT = "budgetConflict";
		public static final String SUMMARY = "summary";
		
		//the suffix for order
		public static final String ORDER = "Order";
		
		public static final String ARCHIVE_LEVEL = "ArchiveLevel";
		
		public static final String PROJECT_TYPE = "ProjectType";
		public static final String GLOBAL_ITEM_NO = "GlobalItemNo";
	}
	
	public interface PSEUDO_COLUMN_LABELS {
		public static final String MASS_OPERATION = "item.pseudoColumn.massOp";
		public static final String INDEX_NUMBER = "item.pseudoColumn.indexNumber";
		public static final String ATTACHMENT_SYMBOL = "item.pseudoColumn.attachment";
		public static final String CONSULTANT_LIST = "item.pseudoColumn.consultant";
		public static final String INFORMANT_LIST = "item.pseudoColumn.informant";
		
		public static final String ISSUETYPE_SYMBOL = "item.pseudoColumn.issueTypeSymbol";
		public static final String STATUS_SYMBOL = "item.pseudoColumn.stateSymbol";
		public static final String PRIORITY_SYMBOL = "item.pseudoColumn.prioritySymbol";
		public static final String SEVERITY_SYMBOL = "item.pseudoColumn.serveritySymbol";
		public static final String CUSTOM_OPTION_SYMBOL = "item.pseudoColumn.customSymbol";
		
		public static final String MY_EXPENSE_TIME = "item.pseudoColumn.myExpenseTime";
		public static final String MY_EXPENSE_COST = "item.pseudoColumn.myExpenseCost";
		public static final String TOTAL_EXPENSE_TIME = "item.pseudoColumn.totalExpenseTime";
		public static final String TOTAL_EXPENSE_COST = "item.pseudoColumn.totalExpenseCost"; 
		public static final String BUDGET_TIME = "item.pseudoColumn.budgetTime";
		public static final String BUDGET_COST = "item.pseudoColumn.budgetCost";
		public static final String TOTAL_PLANNED_TIME = "item.pseudoColumn.totalPlanTime";
		public static final String TOTAL_PLANNED_COST = "item.pseudoColumn.totalPlanCost";
		public static final String REMAINING_PLANNED_TIME = "item.pseudoColumn.remainingPlanTime";
		public static final String REMAINING_PLANNED_COST = "item.pseudoColumn.remainingPlanCost";
		public static final String LINKED_ITEMS = "item.pseudoColumn.linkedItems";
		public static final String PRIVATE_SYMBOL = "item.pseudoColumn.privateSymbol";
		public static final String OVERFLOW_ICONS = "item.pseudoColumn.overflowSymbols";
		
		public static final String HISTORY_LIST = "item.pseudoColumn.fieldChangeHistory";
		public static final String BUDGET_HISTORY_LIST = "item.pseudoColumn.budgetHistory";
		public static final String PLAN_HISTORY_LIST = "item.pseudoColumn.planHistory";
		public static final String COST_LIST = "item.pseudoColumn.costs";
	}
	
	private String label;
	
	/**
	 * @return the attributeName
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setLabel(String attributeName) {
		this.label = attributeName;
	}


	public static String getPseudoColumnName(Integer fieldID){
		if(fieldID==null){
			return null;
		}
		switch (fieldID.intValue()){
			case TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.CHECKBOX_FIELD_ID;
			}

			case TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.ATTACHMENT_SYMBOL;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.CONSULTANT_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.INFORMANT_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.HISTORY;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.PRIVATE_SYMBOL;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_HISTORY_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGETHISTORY;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.PLAN_HISTORY_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.PLANHISTORY;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.COSTS;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.LINKED_ITEMS;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.OVERFLOW_SYMBOL;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_EXPENSE_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.MY_EXPENSE_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.TOTAL_PLANNED_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.REMAINING_PLANNED_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.BUDGET_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.INDEX_NUMBER;
			}


			default:
				return null;
		}

	}


	public static String getPseudoColumnLabel(Integer fieldID){
		if(fieldID==null){
			return null;
		}
		switch (fieldID.intValue()){
			case TReportLayoutBean.PSEUDO_COLUMNS.CHECKBOX_FIELD_ID:{
				return TReportLayoutBean.PSEUDO_COLUMN_NAMES.CHECKBOX_FIELD_ID;
			}

			case TReportLayoutBean.PSEUDO_COLUMNS.ATTACHMENT_SYMBOL:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.ATTACHMENT_SYMBOL;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.CONSULTANT_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.INFORMANT_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.HISTORY_LIST:{
				return PSEUDO_COLUMN_LABELS.HISTORY_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.PRIVATE_SYMBOL:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.PRIVATE_SYMBOL;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_HISTORY_LIST:{
				return PSEUDO_COLUMN_LABELS.BUDGET_HISTORY_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.PLAN_HISTORY_LIST:{
				return PSEUDO_COLUMN_LABELS.PLAN_HISTORY_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.COST_LIST:{
				return PSEUDO_COLUMN_LABELS.COST_LIST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.LINKED_ITEMS:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.LINKED_ITEMS;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.OVERFLOW_ICONS:{
				return PSEUDO_COLUMN_LABELS.OVERFLOW_ICONS;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_EXPENSE_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.MY_EXPENSE_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.TOTAL_PLANNED_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.REMAINING_PLANNED_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_COST;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.BUDGET_TIME;
			}
			case TReportLayoutBean.PSEUDO_COLUMNS.INDEX_NUMBER:{
				return TReportLayoutBean.PSEUDO_COLUMN_LABELS.INDEX_NUMBER;
			}


			default:
				return null;
		}

	}


}
