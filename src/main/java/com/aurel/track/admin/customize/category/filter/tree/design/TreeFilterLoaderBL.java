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

import java.util.List;
import java.util.Stack;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;

/**
 * Loads a report tree or notification tree: the QNodes into FilterExpressionTOs 
 * @author Tamas
 *
 */
public class TreeFilterLoaderBL {

	static final Logger LOGGER = LogManager.getLogger(TreeFilterLoaderBL.class);
	
	
	/***************************************************
	 * Common for report tree and notification tree 
	 ***************************************************/
	
	/**
	 * Transform a tree into a list of QueryExpressions with parenthesis
	 * @param node
	 * @param operationStack
	 * @param expresionStack
	 * @param expressionList
	 */
	public static void transformTreeToExpressionList(QNode node, 
			Stack<QNode> operationStack, Stack<FieldExpressionInTreeTO> expresionStack,
			List<FieldExpressionInTreeTO> expressionList){
		if(node==null) {
			return;
		}
		//push each operation on the stack
		if (node.getType()!=QNode.EXP) {
			operationStack.push(node);
		}
		List<QNode> children=node.getChildren();
		if (children!=null && !children.isEmpty()) {
			//leftmost child
			QNode leftmostChild = children.get(0);
			if (leftmostChild.getType()!=QNode.EXP){
				//folder -> operation (AND OR)
				transformTreeToExpressionList(leftmostChild, operationStack, expresionStack, expressionList);
			} else {
				//leaf -> expression
				QNodeExpression leftmostNodeExpression = (QNodeExpression)leftmostChild;
				//push the leftmost always on the stack 
				FieldExpressionInTreeTO leftmostExpression = new FieldExpressionInTreeTO(leftmostNodeExpression);
				expresionStack.push(leftmostExpression);
				//and also add to the list. The selectedOperation for this expression is not yet known
				expressionList.add(leftmostExpression);
			}
			for (int i = 1; i < children.size()-1; i++) {
				QNode middleNode = children.get(i);
				if (middleNode.getType()!=QNode.EXP){
					//folder -> operation (AND OR)
					transformTreeToExpressionList(middleNode, operationStack, expresionStack, expressionList);
				} else {
					//leaf -> expression
					FieldExpressionInTreeTO middleExpression = new FieldExpressionInTreeTO((QNodeExpression)middleNode);
					//the operation for a middle expression is always his parent's node type
					middleExpression.setSelectedOperation(node.getDisplayType());
					expressionList.add(middleExpression);
				}
			}
			//rightmost child
			if (children.size()>1) {
				//there are more than one children 
				QNode rightmostChild = children.get(children.size()-1);
				if (rightmostChild.getType()!=QNode.EXP){
					//folder -> operation (AND OR)
					transformTreeToExpressionList(rightmostChild, operationStack, expresionStack, expressionList);
					//recursively climbing up on the leftmost operation nodes 
					QNode child = operationStack.pop();
					FieldExpressionInTreeTO leftChild;
					if (!operationStack.empty()) {
						QNode parent = operationStack.peek();
						List<QNode> rightNodeChildren = parent.getChildren();
						if (rightNodeChildren.get(0)!=child) {
							//if operation is not the leftmost child of his parent then the actual left expression can be popped
							leftChild = expresionStack.pop(); 
							leftChild.setSelectedOperation(parent.getDisplayType());
						} else {
							leftChild = expresionStack.peek();
						}
						leftChild.setParenthesisOpen(leftChild.getParenthesisOpen()+1);
						FieldExpressionInTreeTO  rightMostExpression = expressionList.get(expressionList.size()-1);
						rightMostExpression.setParenthesisClosed(rightMostExpression.getParenthesisClosed()+1);
					} else {
						//finished parsing the entire tree: set the operation of the leftmost node
						leftChild = expresionStack.pop();
						leftChild.setSelectedOperation(child.getDisplayType());
					}
				} else {
					//leaf -> expression
					QNodeExpression rightMostNodeExpression = (QNodeExpression)rightmostChild;
					FieldExpressionInTreeTO rightMostExpression = new FieldExpressionInTreeTO(rightMostNodeExpression);
					//the operation for the rightmost expression is always his parent's node type
					rightMostExpression.setSelectedOperation(node.getDisplayType());
					//parent always popped when his rightmost leaf is processed  
					QNode child = operationStack.pop();
					FieldExpressionInTreeTO leftChild;
					if (!operationStack.empty()) {
						QNode parent = operationStack.peek();
						List<QNode> rightNodeChildren = parent.getChildren();
						if (rightNodeChildren.get(0)!=child) {
							//if operation is not the leftmost child then the actual left expression can be popped
							leftChild = expresionStack.pop(); 
							leftChild.setSelectedOperation(parent.getDisplayType());
						} else {
							leftChild = expresionStack.peek();
						}
						leftChild.setParenthesisOpen(leftChild.getParenthesisOpen()+1);
						rightMostExpression.setParenthesisClosed(rightMostExpression.getParenthesisClosed()+1);
					} else {
						//finished parsing the entire tree: set the operation of the leftmost node
						leftChild = expresionStack.pop();
						leftChild.setSelectedOperation(child.getDisplayType());
					}
					expressionList.add(rightMostExpression);
				}
			} else {
				//one single child, but it was partially processed as left child
				//pop the operations and add the parenthesis but it is added already in expressionList
				QNode child = operationStack.pop();
				FieldExpressionInTreeTO leftChild;
				if (!operationStack.empty()) {
					QNode parent = operationStack.peek();
					List<QNode> rightNodeChildren = parent.getChildren();
					if (rightNodeChildren.get(0)!=child) {
						//if operation is not the leftmost child then the actual left expression can be popped
						leftChild = expresionStack.pop(); 
						leftChild.setSelectedOperation(parent.getDisplayType());
					} else {
						leftChild = expresionStack.peek();
					}
					leftChild.setParenthesisOpen(leftChild.getParenthesisOpen()+1);	
					FieldExpressionInTreeTO  rightMostExpression = expressionList.get(expressionList.size()-1);
					rightMostExpression.setParenthesisClosed(rightMostExpression.getParenthesisClosed()+1);
				} else {
					//finished parsing the entire tree: set the operation of the leftmost node
					leftChild = expresionStack.pop();
					leftChild.setSelectedOperation(child.getDisplayType());
				}
			}
		}
	}
	
	/*************************************
	 * Report tree specific code
	 *************************************/
	
	/**
	 * Extract the information regarding the original tree
	 * By convention the second child of the root AND node will be the original tree
	 * @param qNode
	 * @return
	 */
	public static QNode getOriginalTree(QNode qNode) {
		if (qNode!=null && qNode.getChildren()!=null  && qNode.getChildren().size()>1) {
			return qNode.getChildren().get(1);
		} else {
			return null;
		}
	}
}
