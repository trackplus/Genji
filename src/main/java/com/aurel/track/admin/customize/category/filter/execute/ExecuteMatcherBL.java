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

package com.aurel.track.admin.customize.category.filter.execute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.FilterBL;
import com.aurel.track.admin.customize.category.filter.QNode;
import com.aurel.track.admin.customize.category.filter.QNodeExpression;
import com.aurel.track.admin.customize.category.filter.TreeFilterFacade;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TQueryRepositoryBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.IFieldTypeRT;
import com.aurel.track.fieldType.runtime.matchers.run.AccountingTimeMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.DoubleMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.IMatcherRT;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.fieldType.runtime.system.select.SystemManagerRT;
import com.aurel.track.fieldType.types.FieldTypeManager;
import com.aurel.track.item.budgetCost.AccountingTimeTO;
import com.aurel.track.item.consInf.RaciRole;

/**
 * Executes the mathers from the tree
 * @author Tamas
 *
 */
public class ExecuteMatcherBL {
	
	private static final Logger LOGGER = LogManager.getLogger(ExecuteMatcherBL.class);
	
	/**
	 * Filter by matcher expressions
	 * @param workitemList
	 * @param filterUpperTO
	 * @param qNode
	 * @return
	 */
	public static List<TWorkItemBean> filterByMatcherExpressions(List<TWorkItemBean> workitemList,
			FilterUpperTO filterUpperTO, QNode qNode, Integer personID, List<TNotifyBean> watcherList, 
			List<TComputedValuesBean> myExpenseList, List<TComputedValuesBean> totalExpenseList,
			List<TComputedValuesBean> budgetAndPlanList, List<TActualEstimatedBudgetBean> remainingPlanList) {
		if (workitemList!=null && qNode!=null) {
			Set<Integer> pseudoFields = new HashSet<Integer>();
			gatherPseudoFieldsInTree(qNode, pseudoFields);
			Map<Integer, Map<Integer, Object>> pseudoFieldValueMap = new HashMap<Integer, Map<Integer,Object>>();
			
			if (pseudoFields!=null && pseudoFields.size()>0) {
				addWatchers(pseudoFields, watcherList, pseudoFieldValueMap);
				addComputedValues(pseudoFields, myExpenseList, TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME,
						TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, pseudoFieldValueMap);
				addComputedValues(pseudoFields, totalExpenseList, TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME,
						TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, pseudoFieldValueMap);
				addComputedValues(pseudoFields, budgetAndPlanList, TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME,
						TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.BUDGET, pseudoFieldValueMap);
				addComputedValues(pseudoFields, budgetAndPlanList, TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME,
						TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST,
						TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, pseudoFieldValueMap);
				addRemainingValues(pseudoFields, remainingPlanList, TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME,
						TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST, pseudoFieldValueMap);
			}
			for (Iterator<TWorkItemBean> iterator = workitemList.iterator(); iterator.hasNext();) {
				TWorkItemBean workItemBean = iterator.next();
				if (!ExecuteMatcherBL.matchTreePart(qNode, workItemBean, filterUpperTO.getMatcherContext(), pseudoFieldValueMap)) {
					LOGGER.debug(workItemBean.getObjectID() + " does not match the tree machers ");
					iterator.remove();
				}
			}
		}
		return workitemList;
	}
	
	/**
	 * Adds the watchers in the corresponding map
	 * @param pseudoFields
	 * @param watcherList
	 * @param pseudoFieldValueMap
	 */
	private static void addWatchers(Set<Integer> pseudoFields, List<TNotifyBean> watcherList,
			Map<Integer, Map<Integer, Object>> pseudoFieldValueMap) {
		if (watcherList!=null) {
			Map<Integer, Object> consultedMap = null;
			Map<Integer, Object> informedMap = null;
			boolean hasConsultedMatcher = pseudoFields.contains(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST);
			if (hasConsultedMatcher) {
				consultedMap = new HashMap<Integer, Object>();
			}
			boolean hasInformedMatcher = pseudoFields.contains(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST);
			if (hasInformedMatcher) {
				informedMap = new HashMap<Integer, Object>();
			}
			if (hasConsultedMatcher || hasInformedMatcher) {
				for (TNotifyBean notifyBean : watcherList) {
					String raciRole = notifyBean.getRaciRole();
					Integer itemID = notifyBean.getWorkItem();
					if (itemID!=null && raciRole!=null) {
						if (hasConsultedMatcher && RaciRole.CONSULTANT.equals(raciRole)) {
							consultedMap.put(itemID, notifyBean.getPersonID());
						} else {
							if (hasInformedMatcher && RaciRole.INFORMANT.equals(raciRole)) {
								informedMap.put(itemID, notifyBean.getPersonID());
							} 
						}
					}
				}
				if (hasConsultedMatcher) {
					pseudoFieldValueMap.put(TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST, consultedMap);
				}
				if (hasInformedMatcher) {
					pseudoFieldValueMap.put(TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST, informedMap);
				}
			}
		}
	}
	
	/**
	 * Adds the computed values to the corresponding maps
	 * @param pseudoFields
	 * @param computedValueBeans
	 * @param timeField
	 * @param costField
	 * @param valueType
	 * @param pseudoFieldValueMap
	 */
	private static void addComputedValues(Set<Integer> pseudoFields, List<TComputedValuesBean> computedValueBeans,
			int timeField, int costField, int valueType, Map<Integer, Map<Integer, Object>> pseudoFieldValueMap) {
		Map<Integer, Object> timeMap = null;
		Map<Integer, Object> costMap = null;
		boolean hasTimeMatcher = pseudoFields.contains(timeField);
		if (hasTimeMatcher) {
			timeMap = new HashMap<Integer, Object>();
		}
		boolean hasCostMatcher = pseudoFields.contains(costField);
		if (hasCostMatcher) {
			costMap = new HashMap<Integer, Object>();
		}
		if (hasTimeMatcher || hasCostMatcher) {
			for (TComputedValuesBean computedValuesBean : computedValueBeans) {
				Integer effortType = computedValuesBean.getEffortType();
				Integer computedValueType = computedValuesBean.getComputedValueType();
				Integer itemID = computedValuesBean.getWorkitemKey();
				if (itemID!=null && effortType!=null && computedValueType!=null && computedValueType.intValue()==valueType) {
					if (hasTimeMatcher && effortType.intValue()==TComputedValuesBean.EFFORTTYPE.TIME) {
						timeMap.put(itemID, new AccountingTimeTO(computedValuesBean.getComputedValue(), computedValuesBean.getMeasurementUnit()));
					} else {
						if (hasCostMatcher && effortType.intValue()==TComputedValuesBean.EFFORTTYPE.COST) {
							costMap.put(itemID, computedValuesBean.getComputedValue());
						} 
					}
				}
			}
			if (hasTimeMatcher) {
				pseudoFieldValueMap.put(timeField, timeMap);
			}
			if (hasCostMatcher) {
				pseudoFieldValueMap.put(costField, costMap);
			}
		}
	}
	
	/**
	 * Adds the computed values to the corresponding maps
	 * @param pseudoFields
	 * @param remainingPlanBeans
	 * @param timeField
	 * @param costField
	 * @param valueType
	 * @param pseudoFieldValueMap
	 */
	private static void addRemainingValues(Set<Integer> pseudoFields, List<TActualEstimatedBudgetBean> remainingPlanBeans,
			int timeField, int costField, Map<Integer, Map<Integer, Object>> pseudoFieldValueMap) {
		Map<Integer, Object> timeMap = null;
		Map<Integer, Object> costMap = null;
		boolean hasTimeMatcher = pseudoFields.contains(timeField);
		if (hasTimeMatcher) {
			timeMap = new HashMap<Integer, Object>();
		}
		boolean hasCostMatcher = pseudoFields.contains(costField);
		if (hasCostMatcher) {
			costMap = new HashMap<Integer, Object>();
		}
		if (hasTimeMatcher || hasCostMatcher) {
			for (TActualEstimatedBudgetBean actualEstimatedBudgetBean : remainingPlanBeans) {
				Double time = actualEstimatedBudgetBean.getEstimatedHours();
				Double cost = actualEstimatedBudgetBean.getEstimatedCost();
				Integer itemID = actualEstimatedBudgetBean.getWorkItemID();
				if (itemID!=null) {
					if (hasTimeMatcher && time!=null) {
						timeMap.put(itemID, new AccountingTimeTO(time, actualEstimatedBudgetBean.getTimeUnit()));
					} else {
						if (hasCostMatcher && cost!=null) {
							costMap.put(itemID, cost);
						} 
					}
				}
			}
			if (hasTimeMatcher) {
				pseudoFieldValueMap.put(timeField, timeMap);
			}
			if (hasCostMatcher) {
				pseudoFieldValueMap.put(costField, costMap);
			}
		}
	}
	
	/**
	 * Gather the pseudo fields
	 * @param qNode
	 * @param workItemBean
	 * @param matcherContext
	 * @return
	 */
	public static void gatherPseudoFieldsInTree(QNode qNode, Set<Integer> pseudoFields) {
		if (qNode==null) {
			return;
		}
		List<QNode> children;
		switch (qNode.getType()) {
		case QNode.OR:
			children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					gatherPseudoFieldsInTree(childNode, pseudoFields);
				}	
			}
			break;
		case QNode.AND:
			children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					gatherPseudoFieldsInTree(childNode, pseudoFields);
				}
			}
			break;
		case QNode.EXP:
			QNodeExpression qNodeExpression = (QNodeExpression)qNode;
			Integer fieldID = qNodeExpression.getField();
			if (fieldID!=null && fieldID.intValue()<0) {
				LOGGER.debug("Pseudo field " + fieldID + " found");
				pseudoFields.add(fieldID);
			}
			break;
		}
	}
	
	/**
	 * Execute the matcher tree for a workItem
	 * @param qNode
	 * @param workItemBean
	 * @param matcherContext
	 * @return
	 */
	public static boolean matchTreePart(QNode qNode, TWorkItemBean workItemBean, MatcherContext matcherContext, Map<Integer, Map<Integer, Object>> pseudoFieldValueMap) {
		if (qNode==null) {
			return true;
		}
		List<QNode> children;
		boolean isNegate = qNode.isNegate();
		switch (qNode.getType()) {
		case QNode.OR:
			children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					if ((matchTreePart(childNode, workItemBean, matcherContext, pseudoFieldValueMap) && isNegate==false) ||
							(!matchTreePart(childNode, workItemBean, matcherContext, pseudoFieldValueMap) && isNegate==true)) {
						return true;
					}
				}
				return false;
			} else {
				return true;
			}
		case QNode.AND:
			children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				for (QNode childNode : children) {
					if ((!matchTreePart(childNode, workItemBean, matcherContext, pseudoFieldValueMap) && isNegate==false) ||
						(matchTreePart(childNode, workItemBean, matcherContext, pseudoFieldValueMap) && isNegate==true)) {
						return false;
					}
				}
				return true;
			} else {
				return true;
			}
		case QNode.EXP:
			QNodeExpression qNodeExpression = (QNodeExpression)qNode;
			Integer fieldID = qNodeExpression.getField();
			Integer matcherID = qNodeExpression.getMatcherID();
			Object matcherValue = qNodeExpression.getValue();
			if (fieldID==null || matcherID==null || matcherID.intValue()==MatcherContext.PARAMETER) {
				//probably an unknown expression (not yet specified): do not filter by this expression
				//(although at this moment the $PARAMETER's should be already replaced by user entered values,
				//there are cases where the user is not prompted for parameters (for ex. filter called from dashboard)
				//in this case the $PARAMETER value should be neglected otherwise the result will be empty)
				return true;
			}
			if (fieldID>0) {
				//system or custom field
				IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
				if (fieldTypeRT!=null) {
					Object fieldValue = workItemBean.getAttribute(fieldID);
					IMatcherRT matcherRT = fieldTypeRT.processLoadMatcherRT(fieldID,
								matcherID.intValue(), matcherValue, matcherContext);
					if (matcherRT!=null) {
						return matcherRT.match(fieldValue);
					}
				}
			} else {
				//needed the projectID in matcher which will be get from workItemBean
				matcherContext.setWorkItemBean(workItemBean);
				IMatcherRT matcherRT = getPseudoMatcherRT(fieldID, matcherID, matcherValue, matcherContext);
				if (matcherRT!=null) {
					Object fieldValue = null;
					Map<Integer, Object> fieldValueMap = pseudoFieldValueMap.get(fieldID);
					if (fieldValueMap!=null) {
						fieldValue = fieldValueMap.get(workItemBean.getObjectID());
					}
					return matcherRT.match(fieldValue);
				}
			}
		default:
			break;
		}
		return false;
	}

	/**
	 * Gets the pseudo field matcher
	 * @param fieldID
	 * @param matcherID
	 * @param matcherExpression
	 * @param matcherContext
	 * @return
	 */
	private static IMatcherRT getPseudoMatcherRT(Integer fieldID, Integer matcherID, Object matcherExpression, MatcherContext matcherContext) {
		if (fieldID!=null && matcherID!=null) {
			switch (fieldID.intValue()) {
			case TReportLayoutBean.PSEUDO_COLUMNS.CONSULTANT_LIST:
			case TReportLayoutBean.PSEUDO_COLUMNS.INFORMANT_LIST:
				 return new SystemManagerRT().getMatcherRT(fieldID, matcherID.intValue(), matcherExpression, matcherContext);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME:
				return new AccountingTimeMatcherRT(fieldID, matcherID.intValue(), matcherExpression, matcherContext);
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.MY_EXPENSE_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST:
			case TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST:
				return new DoubleMatcherRT(fieldID, matcherID.intValue(), matcherExpression, matcherContext);
			}
		}
		return null;
	}
	
	/**
	 * Returns whether the workItem change matches the notification filter
	 * @param queryID
	 * @param workItemBeanOld
	 * @param workItemBeanNew
	 * @param matcherContext
	 * @param locale
	 * @return
	 */
	public static boolean matchNotifyFilter(Integer queryID, TWorkItemBean workItemBeanOld,
		TWorkItemBean workItemBeanNew, MatcherContext matcherContext) {
		TQueryRepositoryBean queryRepositoryBean = (TQueryRepositoryBean)TreeFilterFacade.getInstance().getByKey(queryID);
		if (queryRepositoryBean!=null) {
			QNode qNode = FilterBL.loadNode(queryRepositoryBean);
			return matchNotifyFilterForNode(qNode, workItemBeanOld, workItemBeanNew, matcherContext);
		}
		return true;
	}

	/**
	 * Returns whether the workItem change matches the QNode tree
	 * @param qNode
	 * @param workItemBeanOld
	 * @param workItemBeanNew
	 * @param matcherContext
	 * @return
	 */
	private static boolean matchNotifyFilterForNode(QNode qNode,
			TWorkItemBean workItemBeanOld, TWorkItemBean workItemBeanNew, MatcherContext matcherContext) {
		if (qNode==null) {
			return true;
		}
		List<QNode> children = qNode.getChildren();
		boolean isNegate = qNode.isNegate();
		Iterator<QNode> iterator;
		QNode childNode;
		switch (qNode.getType()) {
		case QNode.OR:
			if (children!=null && !children.isEmpty()) {
				iterator = children.iterator();
				while (iterator.hasNext()) {
					childNode = (QNode) iterator.next();
					if ((isNegate==false && matchNotifyFilterForNode(childNode, workItemBeanOld, workItemBeanNew, matcherContext)) ||
							(isNegate==true && !matchNotifyFilterForNode(childNode,  workItemBeanOld, workItemBeanNew, matcherContext))) {
						return true;
					}
				}
				return false;
			} else {
				return true;
			}
		case QNode.AND:
			children = qNode.getChildren();
			if (children!=null && !children.isEmpty()) {
				iterator = children.iterator();
				while (iterator.hasNext()) {
					childNode = (QNode) iterator.next();
					if ((!isNegate && !matchNotifyFilterForNode(childNode, workItemBeanOld, workItemBeanNew, matcherContext))  ||
						(isNegate && matchNotifyFilterForNode(childNode, workItemBeanOld, workItemBeanNew, matcherContext))) {
						return false;
					}
				}
				return true;
			} else {
				return true;
			}
		case QNode.EXP:
			QNodeExpression qNodeExpression = (QNodeExpression)qNode;
			Integer fieldID = qNodeExpression.getField();
			Integer fieldMoment = qNodeExpression.getFieldMoment();
			Object fieldValue = null;
			if (fieldMoment==null || TQueryRepositoryBean.FIELD_MOMENT.NEW==fieldMoment.intValue()) {
				fieldValue = workItemBeanNew.getAttribute(fieldID);
			} else {
				if (workItemBeanOld!=null) {
					fieldValue = workItemBeanOld.getAttribute(fieldID);
				}
			}
			IFieldTypeRT fieldTypeRT = FieldTypeManager.getFieldTypeRT(fieldID);
			IMatcherRT matcherRT =
				fieldTypeRT.processLoadMatcherRT(fieldID,
						qNodeExpression.getMatcherID().intValue(), qNodeExpression.getValue(), matcherContext);
			return matcherRT.match(fieldValue);
		default:
			break;
		}
		return false;
	}
}
