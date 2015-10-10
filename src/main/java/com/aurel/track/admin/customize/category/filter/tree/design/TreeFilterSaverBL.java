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

package com.aurel.track.admin.customize.category.filter.tree.design;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.matchers.MatchRelations;

/**
 * Saves a report tree or notification tree: process the FilterExpressionTOs into QNode
 * @author Tamas
 *
 */
public class TreeFilterSaverBL {
	
	/**
	 * Gather the original tree and the dropdowns in a new tree to be saved
	 * @param qNodeOriginalRoot
	 * @param filterSelectsTO
	 * @return
	 */
	public static QNode createQNodeWithQueryListsTO(QNode qNodeOriginalRoot,
			FilterUpperTO filterSelectsTO, Locale locale) {	
		QNode qNodeReportBean = createNode(QNode.AND);
		qNodeReportBean.setChildren(new ArrayList<QNode>());
		qNodeReportBean.setType(QNode.AND);
		QNode qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_PROJECT, filterSelectsTO.getSelectedProjects());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_RELEASESCHEDULED, filterSelectsTO.getSelectedReleases());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
			//save the closed release flag only if release is specified, otherwise by creating the textual filter expression (for tooltip) confuses the user
			boolean showClosedReleases = filterSelectsTO.isShowClosedReleases();
			if (showClosedReleases) {
				qNode = createQueryNodeExpressionWithEqualMatcher(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.SHOW_CLOSED_RELEASES), 
					Boolean.valueOf(filterSelectsTO.isShowClosedReleases()));
				qNodeReportBean.getChildren().add(qNode);
			}
			//set the release type selector only if release is specified, otherwise by creating the textual filter expression (for tooltip) confuses the user
			Integer releaseTypeSelector = filterSelectsTO.getReleaseTypeSelector();
			if (releaseTypeSelector!=null) {
				qNode = createQueryNodeExpressionWithEqualMatcher(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.RELEASE_TYPE_SELECTOR),
					releaseTypeSelector);
				qNodeReportBean.getChildren().add(qNode);
			}
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_MANAGER, filterSelectsTO.getSelectedManagers());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_RESPONSIBLE, filterSelectsTO.getSelectedResponsibles());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_ORIGINATOR, filterSelectsTO.getSelectedOriginators());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_CHANGEDBY, filterSelectsTO.getSelectedChangedBys());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_STATE, filterSelectsTO.getSelectedStates());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_ISSUETYPE, filterSelectsTO.getSelectedIssueTypes());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_PRIORITY, filterSelectsTO.getSelectedPriorities());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		qNode = createQueryNodeExpressionWithEqualMatcher(SystemFields.INTEGER_SEVERITY, filterSelectsTO.getSelectedSeverities());
		if (qNode!=null) {
			qNodeReportBean.getChildren().add(qNode);
		}
		//set the consultants/informants
		qNode = createQueryNodeExpressionWithEqualMatcher(Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_INFORMNAT_FIELD_ID),
				filterSelectsTO.getSelectedConsultantsInformants());
		if (qNode!=null) {
			//at least one consultant or informant is selected
			qNodeReportBean.getChildren().add(qNode);
			//the consultant or informant selector only if any watcher is specified, otherwise by creating the textual filter expression (for tooltip) confuses the user
			Integer watcherSelector = filterSelectsTO.getWatcherSelector();
			if (watcherSelector!=null) {
				qNode = createQueryNodeExpressionWithEqualMatcher( 
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.CONSULTANT_OR_INFORMANT_SELECTOR),
					watcherSelector);
				qNodeReportBean.getChildren().add(qNode);
			}
		}
		//get the custom selects
		Map<Integer, String> selectedCustomSelectsMap = filterSelectsTO.getSelectedCustomSelectsStr();
		if (selectedCustomSelectsMap!=null && !selectedCustomSelectsMap.isEmpty())
		for (Integer fieldID : selectedCustomSelectsMap.keySet()) {
			String selectedCustomSelects = selectedCustomSelectsMap.get(fieldID);
			if (selectedCustomSelects!=null && selectedCustomSelects.length()>0) {
				qNode = createQueryNodeExpressionWithEqualMatcher(fieldID, FilterUpperTO.createIntegerArrFromString(selectedCustomSelects));
				if (qNode!=null) {
					qNodeReportBean.getChildren().add(qNode);
				}
			}
		}
		//set the keyword
		String keyword = filterSelectsTO.getKeyword();
		if (keyword!=null && keyword.length()>0) {
			//make it xml compatible
			keyword = StringEscapeUtils.escapeXml(keyword);
			qNode = createQueryNodeExpressionWithEqualMatcher(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.KEYWORD_FIELD_ID), keyword);
			qNodeReportBean.getChildren().add(qNode);
		}
		Integer archived = filterSelectsTO.getArchived();
		if (archived!=null) {
			qNode = createQueryNodeExpressionWithEqualMatcher(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.ARCHIVED_FIELD_ID), archived);
			qNodeReportBean.getChildren().add(qNode);
		}
		Integer deleted = filterSelectsTO.getDeleted();
		if (deleted!=null) {
			qNode = createQueryNodeExpressionWithEqualMatcher(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.DELETED_FIELD_ID), deleted);
			qNodeReportBean.getChildren().add(qNode);
		} 
		String linkTypeFilterSuperset = filterSelectsTO.getLinkTypeFilterSuperset();
		if (linkTypeFilterSuperset!=null && !"".equals(linkTypeFilterSuperset)) {
			qNode = createQueryNodeExpressionWithEqualMatcher(
					Integer.valueOf(FilterUpperTO.PSEUDO_FIELDS.LINKTYPE_FILTER_SUPERSET), linkTypeFilterSuperset);
			qNodeReportBean.getChildren().add(qNode);
		}
		List<FieldExpressionSimpleTO> fieldExpressionSimpleTOList = filterSelectsTO.getFieldExpressionSimpleList();
		if (fieldExpressionSimpleTOList!=null) {
			for (FieldExpressionSimpleTO fieldExpressionSimpleTO : fieldExpressionSimpleTOList) {
				QNodeExpression qNodeExpression = createNodeORsFromFilterExpression(fieldExpressionSimpleTO);
				if (qNodeExpression!=null) {
					qNodeReportBean.getChildren().add(qNodeExpression);
				}
			}
		}
		QNode qNewRoot =  createNode(QNode.AND);
		qNewRoot.setChildren(new ArrayList<QNode>());
		qNewRoot.getChildren().add(qNodeReportBean);
		if (qNodeOriginalRoot!=null) {
			qNewRoot.getChildren().add(qNodeOriginalRoot);
		}
		return qNewRoot;
	}
	
	/**
	 * Create an OR node with childs from an array of strings representing integers
	 * @param entityIDs
	 * @param fieldID
	 * @return
	 */
	private static QNodeExpression createNodeORsFromFilterExpression(
			FieldExpressionSimpleTO filterExpressionSimpleTO) {
		if (filterExpressionSimpleTO!=null) {
				QNodeExpression qNodeExpression = (QNodeExpression)createNode(QNode.EXP);
				qNodeExpression.setField(filterExpressionSimpleTO.getField());
				qNodeExpression.setMatcherID(filterExpressionSimpleTO.getSelectedMatcher());
				qNodeExpression.setValue(filterExpressionSimpleTO.getValue());
				return qNodeExpression;
			
		}
		return null;
	}
	
	private static QNodeExpression createQueryNodeExpressionWithEqualMatcher(Integer fieldID, Object value) {
		if (fieldID!=null && value!=null) {
			QNodeExpression qNodeExpression = (QNodeExpression)createNode(QNode.EXP);
			qNodeExpression.setField(fieldID);
			qNodeExpression.setValue(value);
			qNodeExpression.setMatcherID(Integer.valueOf(MatchRelations.EQUAL));
			return qNodeExpression;
		}
		return null;
	}
	

	

	
	/**
	 * Create a insatnce of query node, dependes on type 
	 * @param parentID
	 * @param type
	 * @return
	 */
	private static QNode createNode(int type){
		QNode node=null;
		switch(type){
			case QNode.AND:{
				node=new QNode();
				node.setType(QNode.AND);
				break;
			}
			case QNode.OR:{
				node=new QNode();
				node.setType(QNode.OR);
				break;
			}
			case QNode.EXP:{
				node=new QNodeExpression();
				node.setType(QNode.EXP);
				break;
			}
		}
		return node;
	}
	
	/**
	 * Transform a list of QueryExpressions with parenthesis into a tree
	 * @param expressionList
	 * @param node
	 * @param operationStack
	 */
	public static QNode transformExpressionListToTree(List<FieldExpressionInTreeTO> expressionList, 
			Stack<QNode> operationStack) throws Exception {
		if (expressionList==null || expressionList.isEmpty()) {
			return null;
		}
		QNode root = new QNode();
		root.setType(QNode.AND);
		operationStack.push(root);
		if (expressionList!=null) {
			Iterator<FieldExpressionInTreeTO> iterator = expressionList.iterator();
			boolean first = true;
			while (iterator.hasNext()) {
				FieldExpressionInTreeTO fieldExpressionInTree = iterator.next();
				if (operationStack.isEmpty()) {
					throw new Exception("admin.customize.queryFilter.err.closedGtOpened");
				}
				QNode peekNode = operationStack.peek();
				if (!first) {
					//the first operation (the hidden one) is not significant
					Integer operation = fieldExpressionInTree.getSelectedOperation();
					if (operation!=null) {
						if (peekNode.isTypeAlreadySet()) {
							if (!equalOperation(peekNode, operation)) {
								throw new Exception("admin.customize.queryFilter.err.differentOperationsInParenthesis");
							}
						} else {
							//in the outermost level the second filter expression sets the operation,
							//inside internal parenthesis the first one
							setOperation(peekNode, operation.intValue());
							peekNode.setTypeAlreadySet(true);
						}
					}
				} else {
					first = false;
					if (!iterator.hasNext()) {
						//it can be also AND, it doesn't have importance  because it is a single expression
						peekNode.setType(QNode.OR);
					}
				}
				int leftParenthesis = fieldExpressionInTree.getParenthesisOpen();
				for (int i = 0; i < leftParenthesis; i++) {
					//unknown node type (AND or OR)
					QNode qNode = new QNode();
					peekNode.addChild(qNode);
					operationStack.push(qNode);
					peekNode = operationStack.peek();
				}
				peekNode.addChild(new QNodeExpression(fieldExpressionInTree));
				int rightParenthesis = fieldExpressionInTree.getParenthesisClosed();
				if (rightParenthesis>0) {
					for (int i = 0; i < rightParenthesis; i++) {
						if (operationStack.isEmpty()) {
							throw new Exception("admin.customize.queryFilter.err.closedGtOpened");
						}
						operationStack.pop();
					}
				}
			}
			//pop the root
			if (operationStack.isEmpty()) {
				throw new Exception("admin.customize.queryFilter.err.closedGtOpened");
			}
			operationStack.pop();
			if (!operationStack.isEmpty()) {
				throw new Exception("admin.customize.queryFilter.err.closedLtOpened");
			}
		}
		return root;
	}
	
	/**
	 * Transform the negated logical operations into node negations 
	 * @param qNode
	 * @param operation
	 */
	private static void setOperation(QNode qNode, int operation) {
		switch (operation) {
		case QNode.OR:
		case QNode.AND:
			qNode.setType(operation);
			break;
		case QNode.NOT_AND:
			qNode.setType(QNode.AND);
			qNode.setNegate(true);
			break;
		case QNode.NOT_OR:
			qNode.setType(QNode.OR);
			qNode.setNegate(true);
			break;
		}
	}

	/**
	 * Transform the negated logical operations into node negations 
	 * @param qNode
	 * @param operation
	 */
	private static boolean equalOperation(QNode qNode, int operation) {
		switch (operation) {
		case QNode.OR:
		case QNode.AND:
			return operation==qNode.getType();
		case QNode.NOT_AND:
			return qNode.getType()==QNode.AND && qNode.isNegate();
		case QNode.NOT_OR:			
			return qNode.getType()==QNode.OR && qNode.isNegate();
		}
		return false;
	}
}

