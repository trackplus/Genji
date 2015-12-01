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

package com.aurel.track.admin.customize.category.filter.tree.design;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.util.IntegerStringBean;

/**
 * Filter expression for lower fields: the field, matcher is not hardcoded parenthesis are needed
 * @author Tamas
 *
 */
public class FieldExpressionInTreeTO extends FieldExpressionSimpleTO {
	
	private List<IntegerStringBean> fieldsList = new ArrayList<IntegerStringBean>();
	//the name for the fields combo on the client side (used by submit)	
	private String fieldName;
	//the itemId in the ext js client side to find by getComponent() 
	private String fieldItemId;
	private boolean withFieldMoment;
	private Integer fieldMoment;
	private List<IntegerStringBean> fieldMomentsList = new ArrayList<IntegerStringBean>();
	private String fieldMomentName;
		
	private int parenthesisOpen;
	private List<IntegerStringBean> parenthesisOpenList = new ArrayList<IntegerStringBean>();
	private String parenthesisOpenName;
	
	private int parenthesisClosed;
	private List<IntegerStringBean> parenthesisClosedList = new ArrayList<IntegerStringBean>();
	private String parenthesisClosedName;
	
	private Integer selectedOperation;
	private List<IntegerStringBean> operationsList = new ArrayList<IntegerStringBean>();	
	private String operationName;
	//the itemId in the ext js client side to find by getComponent() 
	private String operationItemId;
	
	public FieldExpressionInTreeTO() {
		super();
	}
	
	public FieldExpressionInTreeTO(QNodeExpression qNodeExpression) {
		super(qNodeExpression);
		this.fieldMoment = qNodeExpression.getFieldMoment();		
	}
   
	public Integer getFieldMoment() {
		return fieldMoment;
	}

	public void setFieldMoment(Integer fieldMoment) {
		this.fieldMoment = fieldMoment;
	}
	
	public int getParenthesisOpen() {
		return parenthesisOpen;
	}

	public void setParenthesisOpen(int parenthesisOpen) {
		this.parenthesisOpen = parenthesisOpen;
	}

	public int getParenthesisClosed() {
		return parenthesisClosed;
	}

	public void setParenthesisClosed(int parenthesisClosed) {
		this.parenthesisClosed = parenthesisClosed;
	}

	public Integer getSelectedOperation() {
		return selectedOperation;
	}

	public void setSelectedOperation(Integer selectedOperation) {
		this.selectedOperation = selectedOperation;
	}

	public List<IntegerStringBean> getFieldsList() {
		return fieldsList;
	}

	public void setFieldsList(List<IntegerStringBean> fieldsList) {
		this.fieldsList = fieldsList;
	}

	public List<IntegerStringBean> getFieldMomentsList() {
		return fieldMomentsList;
	}

	public void setFieldMomentsList(List<IntegerStringBean> fieldMomentsList) {
		this.fieldMomentsList = fieldMomentsList;
	}

	public List<IntegerStringBean> getParenthesisOpenList() {
		return parenthesisOpenList;
	}

	public void setParenthesisOpenList(List<IntegerStringBean> parenthesisOpenList) {
		this.parenthesisOpenList = parenthesisOpenList;
	}

	public List<IntegerStringBean> getParenthesisClosedList() {
		return parenthesisClosedList;
	}

	public void setParenthesisClosedList(
			List<IntegerStringBean> parenthesisClosedList) {
		this.parenthesisClosedList = parenthesisClosedList;
	}

	public List<IntegerStringBean> getOperationsList() {
		return operationsList;
	}

	public void setOperationsList(List<IntegerStringBean> operationsList) {
		this.operationsList = operationsList;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldItemId() {
		return fieldItemId;
	}

	public void setFieldItemId(String fieldItemId) {
		this.fieldItemId = fieldItemId;
	}

	public boolean isWithFieldMoment() {
		return withFieldMoment;
	}

	public void setWithFieldMoment(boolean withFieldMoment) {
		this.withFieldMoment = withFieldMoment;
	}

	public String getFieldMomentName() {
		return fieldMomentName;
	}

	public void setFieldMomentName(String fieldMomentName) {
		this.fieldMomentName = fieldMomentName;
	}

	public String getParenthesisOpenName() {
		return parenthesisOpenName;
	}

	public void setParenthesisOpenName(String parenthesisOpenName) {
		this.parenthesisOpenName = parenthesisOpenName;
	}

	public String getParenthesisClosedName() {
		return parenthesisClosedName;
	}

	public void setParenthesisClosedName(String parenthesisClosedName) {
		this.parenthesisClosedName = parenthesisClosedName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationItemId() {
		return operationItemId;
	}

	public void setOperationItemId(String operationItemId) {
		this.operationItemId = operationItemId;
	}
	
	
}
