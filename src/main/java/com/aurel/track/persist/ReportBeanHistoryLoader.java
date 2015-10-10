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


package com.aurel.track.persist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TBudgetBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.item.history.BudgetCostHistoryBean;
import com.aurel.track.item.history.HistoryBean;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.item.history.HistoryLoaderBL.LONG_TEXT_TYPE;
import com.aurel.track.item.history.HistoryValues;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.report.execute.ReportBeanWithHistory;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;

/**
 * Add history data to ReportBeans
 * @author Tamas Ruff
 *
 */
public class ReportBeanHistoryLoader {	
	
	/**
	 * Adds a HistoryBean to a historyMap by workItemID
	 * @param historyMap
	 * @param historyBean
	 */
	private static Map<Integer, List<HistoryBean>> addToHistoryMap(Map<Integer, List<HistoryBean>> historyMap, HistoryBean historyBean) {
		List<HistoryBean> workItemHistoryList;
		if (historyMap.get(historyBean.getWorkItemID()) == null) {
			workItemHistoryList = new ArrayList<HistoryBean>();
			historyMap.put(historyBean.getWorkItemID(), workItemHistoryList);
		} else {
			workItemHistoryList = historyMap.get(historyBean.getWorkItemID());
		}
		workItemHistoryList.add(historyBean);
		return historyMap;
	}
	
	
	/**
	 * Retrieve a map of TBudgetBean objects for an array of workItem keys
	 * @param workItemKeys
	 * @param personsMap
	 * @return
	 */
	private static Map<Integer, List<HistoryBean>> getBudgetChangeHistoryMap(List<BudgetCostHistoryBean> historyBeanList, 
			/*Map<Integer, TPersonBean> personsMap, Map<Integer, TWorkItemBean> workItemMap,*/
			Map<Integer, ProjectAccountingTO> projectAccountingTOMap, Boolean plan) {
		Map<Integer, List<HistoryBean>> historyMap = new HashMap<Integer, List<HistoryBean>>();
		if (historyBeanList!=null) {
			Set<Integer> workItemIDs = new HashSet<Integer>();
			for (BudgetCostHistoryBean budgetCostHistoryBean : historyBeanList) {
				Integer itemID = budgetCostHistoryBean.getWorkItemID();
				if (itemID!=null) {
					workItemIDs.add(itemID);
				}
			}
			Map<Integer, TWorkItemBean> workItemMap = GeneralUtils.createMapFromList(ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(workItemIDs)));
			for (BudgetCostHistoryBean budgetCostHistoryBean : historyBeanList) {
				if (plan!=null) {
					TBudgetBean budgetBean = (TBudgetBean)budgetCostHistoryBean;
					Integer budgetType = budgetBean.getBudgetType();
					if (plan.booleanValue() && budgetType!=null && budgetType.equals(TBudgetBean.BUDGET_TYPE.BUDGET)) {
						//search for plan history but this is a budget change
						continue;
					} else {
						if (!plan.booleanValue() && (budgetType==null || budgetType.equals(TBudgetBean.BUDGET_TYPE.PLANNED_VALUE))) {
							//search for budget history but this is a plan change
							continue;
						}
					}
				}
				TPersonBean personBean = LookupContainer.getPersonBean(budgetCostHistoryBean.getChangedByID()); //personsMap.get();
				if (personBean!=null) {
					budgetCostHistoryBean.setChangedByName(personBean.getFullName());
				}
				TWorkItemBean workItemBean = workItemMap.get(budgetCostHistoryBean.getWorkItemID());
				if (workItemBean!=null) {
					Integer projectID = workItemBean.getProjectID();
					ProjectAccountingTO projectAccountingTO = projectAccountingTOMap.get(projectID);
					if (projectAccountingTO==null) {
						projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
						projectAccountingTOMap.put(projectID, projectAccountingTO);
					}
					budgetCostHistoryBean.setCurrency(projectAccountingTO.getCurrency());
				}
				addToHistoryMap(historyMap, budgetCostHistoryBean); 
			}
		}
		return historyMap;
	}
	
	/**
	 * Retrieve a map of TCostBean objects for an array of workItem keys
	 * @param workItemKeys
	 * @param personsMap
	 * @return
	 */
	private static Map<Integer, List<HistoryBean>> getExpenseMap(List<TCostBean> historyBeanList, 
			/*Map<Integer, TPersonBean> personsMap, Map<Integer, TWorkItemBean> workItemMap, */
			Map<Integer, ProjectAccountingTO> projectAccountingTOMap, Map<Integer, TAccountBean> accountsMap) {
		Map<Integer, List<HistoryBean>> historyMap = new HashMap<Integer, List<HistoryBean>>();
		if (historyBeanList!=null) {
			Set<Integer> workItemIDs = new HashSet<Integer>();
			for (TCostBean costBean : historyBeanList) {
				Integer itemID = costBean.getWorkItemID();
				if (itemID!=null) {
					workItemIDs.add(itemID);
				}
			}
			Map<Integer, TWorkItemBean> workItemMap = GeneralUtils.createMapFromList(ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(workItemIDs)));
			for (TCostBean costBean : historyBeanList) {
				TPersonBean personBean = LookupContainer.getPersonBean(costBean.getChangedByID());//personsMap.get(costBean.getChangedByID());
				if (personBean!=null) {
					costBean.setChangedByName(personBean.getFullName());
				}
				TWorkItemBean workItemBean = workItemMap.get(costBean.getWorkItemID());
				if (workItemBean!=null) {
					
					Integer projectID = workItemBean.getProjectID();
					ProjectAccountingTO projectAccountingTO = projectAccountingTOMap.get(projectID);
					if (projectAccountingTO==null) {
						projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
						projectAccountingTOMap.put(projectID, projectAccountingTO);
					}
					costBean.setCurrency(projectAccountingTO.getCurrency());
				}
				TAccountBean accountBean = accountsMap.get(costBean.getAccount());
				if (accountBean!=null) {
					costBean.setAccountName(accountBean.getAccountNumber());
				}
				addToHistoryMap(historyMap, costBean); 
			}
		}
		return historyMap;
	}
	
	/**
	 * Given the ReportBeans list create the ReportBeanWithHistory list
	 * @param reportBeanList
	 * @param locale
	 * @param commentsAndFieldChangeTogether
	 * @param includeComments
	 * @param includeFieldChange
	 * @param fieldIDs
	 * @param includeBudget
	 * @param includeCost
	 * @param filterCost
	 * @param includeAttachments
	 * @param personID
	 * @param filterByPerson
	 * @param fromDate
	 * @param toDate
	 * @param isISO
	 * @param longtextType
	 * @return
	 */
	public static List<ReportBeanWithHistory> getReportBeanWithHistoryList(
			List<ReportBean> reportBeanList, Locale locale, boolean commentsAndFieldChangeTogether,
			boolean includeComments, boolean includeFieldChange, Integer[] fieldIDs, 
			boolean includeBudget, boolean includePlan, boolean includeCost,
			boolean filterCost,	boolean includeAttachments, 
			Integer personID, List<Integer> filterByPersons,
			Date fromDate, Date toDate, boolean isISO, LONG_TEXT_TYPE longtextType) {
		Integer filterPerson = null;
		if (filterByPersons!=null && !filterByPersons.isEmpty()) {
			filterPerson = filterByPersons.get(0);
		}
		List<ReportBeanWithHistory> reportBeanWithHistoryList = new ArrayList<ReportBeanWithHistory>();
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> workItemsFieldChangeHistoryValues = 
			new HashMap<Integer, SortedMap<Integer,Map<Integer,HistoryValues>>>();
		Map<Integer, SortedMap<Integer, Map<Integer, HistoryValues>>> workItemsCommentHistoryValues = 
			new HashMap<Integer, SortedMap<Integer,Map<Integer,HistoryValues>>>();
		List<TBudgetBean> budgetChangeList = new ArrayList<TBudgetBean>();
		List<TActualEstimatedBudgetBean> estimatedBudgetList = new ArrayList<TActualEstimatedBudgetBean>();
		List<TCostBean> costList = new ArrayList<TCostBean>();
		List<TAttachmentBean> attachmentList = new ArrayList<TAttachmentBean>();
		if (reportBeanList!=null && !reportBeanList.isEmpty()) {
			int[] workItemIDs = new int[reportBeanList.size()];
			for (int i=0; i<reportBeanList.size(); i++) {
				ReportBean reportBean = reportBeanList.get(i);
				workItemIDs[i] = reportBean.getWorkItemBean().getObjectID().intValue();
			}
			if (workItemIDs.length > GeneralUtils.ITEMS_PRO_STATEMENT) {
				int numberOfStatements = workItemIDs.length/GeneralUtils.ITEMS_PRO_STATEMENT;	
				if (numberOfStatements*GeneralUtils.ITEMS_PRO_STATEMENT<workItemIDs.length) {
					numberOfStatements++;
				}
				int[] buffer;
				for (int i=0;i<numberOfStatements; i++) {
					if (i==numberOfStatements-1) {
						buffer = new int[workItemIDs.length - i*GeneralUtils.ITEMS_PRO_STATEMENT];
					} else {
						buffer = new int[GeneralUtils.ITEMS_PRO_STATEMENT];
					}
					for (int j = 0; j < buffer.length; j++) {
						buffer[j] = workItemIDs[i*GeneralUtils.ITEMS_PRO_STATEMENT+j];
					}
					if (commentsAndFieldChangeTogether==true) {
						workItemsFieldChangeHistoryValues.putAll(HistoryLoaderBL.getWorkItemsHistory(buffer, 
								null, false, filterByPersons, fromDate, toDate, locale, isISO, longtextType, true, personID));
					} else {
						if (includeComments) {
							workItemsCommentHistoryValues.putAll(HistoryLoaderBL.getWorkItemsHistory(buffer, 
								new Integer[]{SystemFields.INTEGER_COMMENT}, true, filterByPersons, fromDate, toDate, locale, isISO, longtextType, true, personID));
						}
						if (includeFieldChange) {
							Integer[] newFieldIDs;
							boolean includeFields;
							if (includeComments && fieldIDs==null) {
								//include all fields except comment because they are included by includeComments
								newFieldIDs = new Integer[] {SystemFields.INTEGER_COMMENT};
								includeFields = false;
							} else {
								//leave as it is
								newFieldIDs = fieldIDs;
								includeFields = true;
							}
							workItemsFieldChangeHistoryValues.putAll(HistoryLoaderBL.getWorkItemsHistory(buffer, 
									newFieldIDs, includeFields, filterByPersons, fromDate, toDate, locale, isISO, longtextType, true, personID));
						}
					}
					if (includeCost) {
						Integer[] filterPersonsArr = null;
						if (filterPerson!=null) {
							filterPersonsArr = new Integer[] {filterPerson};
						}
						costList.addAll(ExpenseBL.loadByWorkItemKeys(buffer, filterPersonsArr, fromDate, toDate, null, false));
					}
					if (includeAttachments) {
						attachmentList.addAll(AttachBL.getAttachments(buffer, false));
					}
				}
			} else {
				if (commentsAndFieldChangeTogether==true) {
					workItemsFieldChangeHistoryValues.putAll(HistoryLoaderBL.getWorkItemsHistory(workItemIDs, 
							null, false, filterByPersons, fromDate, toDate, locale, isISO, longtextType, true, personID));
				} else {
					if (includeComments) {
						workItemsCommentHistoryValues = HistoryLoaderBL.getWorkItemsHistory(workItemIDs, 
								new Integer[]{SystemFields.INTEGER_COMMENT}, true, filterByPersons, fromDate, toDate, locale, isISO, longtextType, true, personID);
					}
					if (includeFieldChange) {
						Integer[] newFieldIDs;
						boolean includeFields;
						if (includeComments && fieldIDs==null) {
							//include all fields except comment because they are included by includeComments
							newFieldIDs = new Integer[] {SystemFields.INTEGER_COMMENT};
							includeFields = false;
						} else {
							//leave as it is
							newFieldIDs = fieldIDs;
							includeFields = true;
						}
						workItemsFieldChangeHistoryValues = HistoryLoaderBL.getWorkItemsHistory(workItemIDs, 
								newFieldIDs, includeFields, filterByPersons, fromDate, toDate, locale, isISO, longtextType, true, personID);
					}
				}
				if (includeCost) {
					Integer[] filterPersonsArr = null;
					if (filterPerson!=null) {
						filterPersonsArr = new Integer[] {filterPerson};
					}
					costList = ExpenseBL.loadByWorkItemKeys(workItemIDs, filterPersonsArr, fromDate, toDate, null, false);
				}
				if (includeAttachments) {
					attachmentList.addAll(AttachBL.getAttachments(workItemIDs, false));
				}
			}
			Map<Integer, List<HistoryBean>> budgetHistoryMap = null;
			Map<Integer, List<HistoryBean>> plannedValueHistoryMap = null;
			Map<Integer, List<HistoryBean>> estimatedBudgetMap = null;
			Map<Integer, List<HistoryBean>> costsMap = null;
			Map<Integer, ProjectAccountingTO> projectAccountingTOMap = new HashMap<Integer, ProjectAccountingTO>();
			if (includeBudget || includePlan || includeCost) {
				if (includeBudget) {
					budgetHistoryMap = getBudgetChangeHistoryMap((List)budgetChangeList, projectAccountingTOMap, Boolean.FALSE);
				}
				if (includePlan) {
					plannedValueHistoryMap = getBudgetChangeHistoryMap((List)budgetChangeList, projectAccountingTOMap, Boolean.TRUE);
					estimatedBudgetMap = getBudgetChangeHistoryMap((List)estimatedBudgetList, projectAccountingTOMap, null);
				}
				if (includeCost) {
					Map<Integer, TAccountBean> accountMap = GeneralUtils.createMapFromList(AccountBL.getAllAccounts());
					costsMap = getExpenseMap(costList, projectAccountingTOMap, accountMap);
				}
			}
			Map<Integer, List<TAttachmentBean>> attachmentsMap = null;
			if (includeAttachments) {
				attachmentsMap = new HashMap<Integer, List<TAttachmentBean>>();
				Iterator<TAttachmentBean> itrAttachments = attachmentList.iterator();
				while (itrAttachments.hasNext()) {
					TAttachmentBean attachmentBean = itrAttachments.next();
					Integer workItemID = attachmentBean.getWorkItem();
					List<TAttachmentBean> attachmentListForWorkItem = attachmentsMap.get(workItemID);
					if (attachmentListForWorkItem==null) {
						attachmentListForWorkItem = new ArrayList<TAttachmentBean>();
						attachmentsMap.put(workItemID, attachmentListForWorkItem);
					}
					attachmentListForWorkItem.add(attachmentBean);
				}
			}
			for (int i=0; i<reportBeanList.size(); i++) {
				ReportBean reportBean = reportBeanList.get(i);
				ReportBeanWithHistory reportBeanWithHistory = new ReportBeanWithHistory(reportBean);
				Integer workItemID = reportBeanWithHistory.getWorkItemBean().getObjectID();
				if (includeComments) {
					reportBeanWithHistory.setComments(HistoryLoaderBL.getHistoryValuesList(workItemsCommentHistoryValues.get(workItemID), true));
				}
				if (!workItemsFieldChangeHistoryValues.isEmpty()) {
					reportBeanWithHistory.setHistoryValuesMap(workItemsFieldChangeHistoryValues.get(workItemID));
				}
				
				if (plannedValueHistoryMap!=null) {
					List<TBudgetBean> workItemPlannedValueHistoryList = (List)plannedValueHistoryMap.get(workItemID);
					removeSameValues(workItemPlannedValueHistoryList);
					reportBeanWithHistory.setPlannedValueHistory(workItemPlannedValueHistoryList);
				}
				if (budgetHistoryMap!=null) {
					List<TBudgetBean> workItemBudgetHistoryList = (List)budgetHistoryMap.get(workItemID);
					removeSameValues(workItemBudgetHistoryList);
					reportBeanWithHistory.setBudgetHistory(workItemBudgetHistoryList);
				}
				if (estimatedBudgetMap!=null) {
					List<HistoryBean> workItemEstimatedBudgetList = estimatedBudgetMap.get(workItemID);
					if (workItemEstimatedBudgetList!=null && !workItemEstimatedBudgetList.isEmpty()) {
						reportBeanWithHistory.setActualEstimatedBudgetBean(
							(TActualEstimatedBudgetBean)workItemEstimatedBudgetList.get(0));
					}
				}
				
				if (includeCost && costsMap != null) {
					reportBeanWithHistory.setCosts((List)costsMap.get(workItemID));
				}
				if (includeAttachments && attachmentsMap != null) {
					reportBeanWithHistory.setAttachments(attachmentsMap.get(workItemID));
				}
				reportBeanWithHistoryList.add(reportBeanWithHistory);
			}
		}
		//filter out the cost one hasn't right to see (didn't has "view all expenses" role)
		if (includeCost && filterCost) {
			AccessBeans.filterCostBeans(personID, 
				reportBeanWithHistoryList);
		}
		return reportBeanWithHistoryList;
	}
	
	private static List<TBudgetBean> removeSameValues(List<TBudgetBean> workItemBudgetHistoryList) {
		if (workItemBudgetHistoryList!=null) {
			String description = null;
			for (int i = workItemBudgetHistoryList.size()-1; i >= 0; i--) {
				TBudgetBean budgetBean =workItemBudgetHistoryList.get(i);
				if (EqualUtils.notEqual(description, budgetBean.getDescription())) {
					description = budgetBean.getDescription();
				} else {
					budgetBean.setDescription(null);
				}
			}
		}
		return workItemBudgetHistoryList;
	}

	
	
	public static Criteria addFilterConditions(Criteria criteria, Integer[] personIDsArr,
			Date fromDate, Date toDate, String changedByField, String lastEditField) {
		if (personIDsArr!=null && personIDsArr.length>0) {
			criteria.addIn(changedByField, personIDsArr);
		}
		if (fromDate!=null && toDate!=null) {
			Criterion criterionFrom = criteria.getNewCriterion(lastEditField, fromDate, Criteria.GREATER_EQUAL);
			Criterion criterionTo = criteria.getNewCriterion(lastEditField, toDate, Criteria.LESS_THAN);
			criteria.add(criterionFrom.and(criterionTo));
		} else {
			if (fromDate!=null) {
				criteria.add(lastEditField, fromDate, Criteria.GREATER_EQUAL);
			} else {
				if (toDate!=null) {
					criteria.add(lastEditField, toDate, Criteria.LESS_THAN);
				}
			}
		}
		return criteria;
	}

}
