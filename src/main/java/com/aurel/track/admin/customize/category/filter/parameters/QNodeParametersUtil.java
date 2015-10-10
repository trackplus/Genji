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

package com.aurel.track.admin.customize.category.filter.parameters;

import java.util.List;

import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.admin.customize.category.filter.tree.design.FieldExpressionSimpleTO;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;

public class QNodeParametersUtil {

	/**
	 * Whether any node from the the saved report tree contains parameter
	 * @param qNode
	 * @return
	 */
	public static boolean containsParameter(QNode qNode) {
		if (qNode==null) {
			return false;
		}
		switch (qNode.getType()) {
		case QNode.OR:
		case QNode.AND:
			List<QNode> children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					if (containsParameter(childNode)) {
						return true;
					}
				}
			}
			break;
		case QNode.EXP:
			QNodeExpression qNodeExpression = (QNodeExpression)qNode;
			Integer fieldID = qNodeExpression.getField();
			Integer matcherID = qNodeExpression.getMatcherID();
			if (fieldID!=null && matcherID!=null && matcherID.equals(MatcherContext.PARAMETER)) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * Gather the parameterized nodes from report tree into a list
	 * @param qNode
	 * @param parametrizedNodes
	 */
	public static void gatherNodesWithParameter(QNode qNode, List<QNodeExpression> parametrizedNodes) {
		if (qNode==null) {
			return;
		}
		List<QNode> children = qNode.getChildren();
		switch (qNode.getType()) {
		case QNode.OR:
		case QNode.AND:
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					gatherNodesWithParameter(childNode, parametrizedNodes);
				}
			} 	
			break;
		case QNode.EXP:
			QNodeExpression qNodeExpression = (QNodeExpression)qNode;
			Integer fieldID = qNodeExpression.getField();
			Integer matcherID = qNodeExpression.getMatcherID();
			if (fieldID!=null && matcherID!=null && matcherID.equals(MatcherContext.PARAMETER)) {
				parametrizedNodes.add(qNodeExpression);
			}
			break;
		}
	}

	/**
	 * Replace the parameterized nodes with real values 
	 * after the user selected the values for parameters
	 * @param qNode
	 * @param fieldExpressionSimpleList
	 * @return
	 */
	public static QNode replaceParameters(QNode qNode, List<FieldExpressionSimpleTO> fieldExpressionSimpleList) {
		if (qNode==null || fieldExpressionSimpleList==null || fieldExpressionSimpleList.isEmpty()) {
			return qNode;
		}
		switch (qNode.getType()) {
		case QNode.OR:
		case QNode.AND:
			List<QNode> children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					replaceParameters(childNode, fieldExpressionSimpleList);
				}
			} 	
			break;
		case QNode.EXP:
			QNodeExpression qNodeExpression = (QNodeExpression)qNode;
			Integer fieldID = qNodeExpression.getField();
			Integer matcherID = qNodeExpression.getMatcherID();
			if (fieldID!=null && matcherID!=null && matcherID.equals(MatcherContext.PARAMETER)) {
				FieldExpressionSimpleTO fieldExpressionSimpleTO = fieldExpressionSimpleList.remove(0);
				//set the matcher and the value in the original node
				qNodeExpression.setMatcherID(fieldExpressionSimpleTO.getSelectedMatcher());
				qNodeExpression.setValue(fieldExpressionSimpleTO.getValue());
			}
			break;
		}
		return qNode;
	}

}
