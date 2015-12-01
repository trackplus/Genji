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

package com.aurel.track.item.budgetCost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.CostDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.history.FlatHistoryBean;
import com.aurel.track.item.history.HistoryBean;
import com.aurel.track.item.history.HistoryEntry;
import com.aurel.track.item.history.HistoryLoaderBL;
import com.aurel.track.lucene.index.associatedFields.ExpenseIndexer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.IEventSubscriber;
import com.aurel.track.util.event.parameters.AfterBudgetExpenseChangeEventParam;

/**
 * Business logic for expenses (costBeans)
 * 
 * @author Tamas Ruff
 * 
 */
public class ExpenseBL {

	private static CostDAO costDAO = DAOFactory.getFactory().getCostDAO();

	/**
	 * Loads the expense from the database
	 * 
	 * @param effortID
	 */
	public static TCostBean loadByPrimaryKey(Integer effortID) {
		return costDAO.loadByPrimaryKey(effortID);
	}

	/**
	 * Gets all expenses
	 * 
	 * @return
	 */
	public static List<TCostBean> loadAll() {
		return costDAO.loadAll();
	}

	/**
	 * Loads the cost beans by objectIDs
	 * 
	 * @param objectIDs
	 * @return
	 */
	public static List<TCostBean> loadByKeys(List<Integer> objectIDs) {
		return costDAO.loadByKeys(objectIDs);
	}

	/**
	 * Gets all indexable expenses
	 * 
	 * @return
	 */
	public static List<TCostBean> loadAllIndexable() {
		return costDAO.loadAllIndexable();
	}

	/**
	 * Loads a the CostBeans by workItemKeys Loads all costs independently
	 * whether the user has or not "view all expenses" role The list will be
	 * filtered out later on because it is too costly to filter them for each
	 * workItem
	 * 
	 * @param workItemKeys
	 * @param personIDsArr
	 * @param fromDate
	 * @param toDate
	 * @param accounts
	 * @param ascendingDate
	 * @return
	 */
	public static List<TCostBean> loadByWorkItemKeys(int[] workItemKeys, Integer[] personIDsArr, Date fromDate, Date toDate, List<Integer> accounts,
			boolean ascendingDate) {
		return costDAO.loadByWorkItemKeys(workItemKeys, personIDsArr, fromDate, toDate, accounts, ascendingDate);
	}

	/**
	 * Gets the cost list by workItems
	 * @param costBeanList
	 * @return
	 */
	public static Map<Integer, List<TCostBean>> getCostBeansByWorkItemMap(List<TCostBean> costBeanList) {
		Map<Integer, List<TCostBean>> costBeanMap = new HashMap<Integer, List<TCostBean>>();
		if (costBeanList != null) {
			for (TCostBean costBean : costBeanList) {
				Integer workItemID = costBean.getWorkitem();
				List<TCostBean> workItemCosts = costBeanMap.get(workItemID);
				if (workItemCosts == null) {
					workItemCosts = new LinkedList<TCostBean>();
					costBeanMap.put(workItemID, workItemCosts);
				}
				workItemCosts.add(costBean);
			}
		}
		return costBeanMap;
	}

	/**
	 * Gets the costs by tree filter for activity stream
	 * @param filterSelectsTO
	 * @param raciBean
	 * @param personID
	 * @param limit
	 * @param fromDate
	 * @param toDate
	 * @param changeTypes
	 * @param changedByPersons
	 * @return
	 */
	public static List<TCostBean> loadActivityStreamCosts(FilterUpperTO filterSelectsTO, RACIBean raciBean, Integer personID, Integer limit, Date fromDate,
			Date toDate, List<Integer> changedByPersons) {
		List<TCostBean> costBeanList = costDAO.loadActivityStreamCosts(filterSelectsTO, raciBean, personID, limit, fromDate, toDate, changedByPersons);
		Set<Integer> workItemIDsSet = new HashSet<Integer>();
		for (TCostBean costBean : costBeanList) {
			workItemIDsSet.add(costBean.getWorkItemID());
		}
		List<TWorkItemBean> worItemsWithCost = ItemBL.loadByWorkItemKeys(GeneralUtils.createIntArrFromSet(workItemIDsSet));
		Map<Integer, TWorkItemBean> workItemBeansMap = GeneralUtils.createMapFromList(worItemsWithCost);
		AccessBeans.filterCostBeans(costBeanList, personID, workItemBeansMap);
		return populateCostBeans(costBeanList, workItemBeansMap);
	}

	/**
	 * Saves the cost bean
	 * @param costBean
	 * @return
	 */
	public static Integer saveCostBean(TCostBean costBean) {
		boolean isNew = costBean.getObjectID() == null;
		Integer expenseID = costDAO.save(costBean);
		ExpenseIndexer.getInstance().addToIndex(costBean, isNew);
		// possible lucene update in other cluster nodes
		ClusterMarkChangesBL.markDirtyExpenseInCluster(expenseID, ClusterMarkChangesBL.getChangeTypeByAddOrUpdateIndex(isNew));
		return expenseID;
	}

	/**
	 * Gets a list of TComputedValuesBean with the sum of cost/time values
	 * grouped by workItems
	 * 
	 * @return
	 */
	public static List<TComputedValuesBean> loadExpenseGroupedByWorkItem() {
		return costDAO.loadExpenseGroupedByWorkItem();
	}

	/**
	 * Gets a list of TComputedValuesBean with the sum of cost/time values
	 * grouped by workItems and persons
	 * 
	 * @return
	 */
	public static List<TComputedValuesBean> loadExpenseGroupedByWorkItemAndPerson() {
		return costDAO.loadExpenseGroupedByWorkItemAndPerson();
	}

	/**
	 * Sums up the efforts in the session
	 * 
	 * @param costBeans
	 */
	public static double getSumExpenseFromSession(Collection<TCostBean> costBeans, boolean hours) {
		double expenseSum = 0.0;
		if (costBeans != null) {
			for (TCostBean costBean : costBeans) {
				Double currentExpense = null;
				if (hours) {
					currentExpense = costBean.getHours();
				} else {
					currentExpense = costBean.getCost();
				}
				if (currentExpense != null) {
					expenseSum += currentExpense.doubleValue();
				}
			}
		}
		return AccountingBL.roundToDecimalDigits(expenseSum, hours);
	}

	/**
	 * Prepares a costBean for save
	 * @param costBean
	 * @param personID
	 * @param workItemID
	 * @return
	 */
	public static TCostBean prepareCostBean(TCostBean costBean, Integer personID, Integer workItemID) {
		if (costBean != null) {
			if (costBean.getPerson() == null) {
				// leave the original person: for example the project manager
				// may change
				// the value but the person remains who created the expense
				costBean.setPerson(personID);
			}
			Date now = new Date();
			costBean.setLastEdit(now);
			Date effortDate = costBean.getEffortdate();
			if (effortDate == null) {
				costBean.setEffortdate(now);
			} else {
				costBean.setEffortdate(effortDate);
			}
			costBean.setWorkitem(workItemID);
		}
		return costBean;
	}

	/**
	 * Saves the added cost/efforts to database after saving the item No
	 * notification is needed
	 * 
	 * @param sessionCostBeans
	 * @param personID
	 * @param workItemID
	 */
	static void saveCostsToDb(SortedMap<Integer, TCostBean> sessionCostBeans, TPersonBean personBean, TWorkItemBean workItemBean) {
		if (sessionCostBeans != null) {
			List<TCostBean> costBeans = reverseList(sessionCostBeans.values());
			// save the expenses in the order they were entered (last from the
			// list was first entered)
			for (int i = costBeans.size() - 1; i >= 0; i--) {
				TCostBean costBean = costBeans.get(i);
				prepareCostBean(costBean, personBean.getObjectID(), workItemBean.getObjectID());
				saveCostBean(costBean);
				if (i == 0) {
					// for the last save the calculated values also
					// the person is the same for all costBeans
					ComputedValueBL.computeExpenses(costBean.getWorkitem(), costBean.getPerson());
				}
			}
		}
	}

	/**
	 * Gets all the effort/cost entries for a workItem depending on the rights
	 * it loads all the costs/efforts or just the own ones
	 * 
	 * @param workItemBean
	 * @return
	 */
	public static List<TCostBean> loadCostsFromDb(TWorkItemBean workItemBean, Integer personID) {
		Integer projectID = workItemBean.getProjectID();
		Integer issueTypeID = workItemBean.getListTypeID();
		Integer workItemID = workItemBean.getObjectID();
		Integer personOID = personID;
		boolean viewAllExpenses = AccessBeans.viewAllExpenses(personOID, projectID, issueTypeID);
		if (viewAllExpenses) {
			personOID = null;
		}
		List<TCostBean> costBeanList = costDAO.getByWorkItemAndPerson(workItemID, personOID);
		Map<Integer, TWorkItemBean> workItemBeansMap = new HashMap<Integer, TWorkItemBean>();
		workItemBeansMap.put(workItemID, workItemBean);
		return populateCostBeans(costBeanList, workItemBeansMap);
	}

	/**
	 * Gets all the effort/cost entries for a workItem depending on the rights
	 * it loads all the costs/efforts or just the own ones
	 * 
	 * @param workItemKey
	 * @return
	 */
	public static List<TCostBean> loadCostsForWorkItems(int[] workItemIDs, Map<Integer, TWorkItemBean> workItemBeansMap, Integer personID) {
		List<TCostBean> costBeanList = loadByWorkItemKeys(workItemIDs, null, null, null, null, false);
		AccessBeans.filterCostBeans(costBeanList, personID, workItemBeansMap);
		return populateCostBeans(costBeanList, workItemBeansMap);
	}

	/**
	 * Gets all the effort/cost entries for a workItem depending on the rights
	 * it loads all the costs/efforts or just the own ones
	 * 
	 * @param workItemKey
	 * @return
	 */
	public static List<TCostBean> loadCostsForWorkItems(int[] workItemIDs, Map<Integer, TWorkItemBean> workItemBeansMap, Date fromDate, Date toDate,
			Integer loggedInPersonID, Integer[] selectedPersonsArr, List<Integer> accounts) {
		List<TCostBean> costBeanList = loadByWorkItemKeys(workItemIDs, selectedPersonsArr, fromDate, toDate, accounts, false);
		AccessBeans.filterCostBeans(costBeanList, loggedInPersonID, workItemBeansMap);
		return populateCostBeans(costBeanList, workItemBeansMap);
	}

	/**
	 * Populate the costBeans with extra text content for rendering
	 * 
	 * @param costBeanList
	 * @param currency
	 * @param accountsMap
	 * @param personsMap
	 * @return
	 */
	private static List<TCostBean> populateCostBeans(List<TCostBean> costBeanList, Map<Integer, TWorkItemBean> workItemBeansMap) {
		if (costBeanList != null) {
			List<TAccountBean> accountBeanList = AccountBL.getAllAccounts();
			Map<Integer, TAccountBean> accountsMap = GeneralUtils.createMapFromList(accountBeanList);
			Map<Integer, ProjectAccountingTO> projectAccountingTOMap = new HashMap<Integer, ProjectAccountingTO>();
			for (TCostBean costBean : costBeanList) {
				TWorkItemBean workItemBean = workItemBeansMap.get(costBean.getWorkItemID());
				if (workItemBean != null) {
					Integer projectID = workItemBean.getProjectID();
					ProjectAccountingTO projectAccountingTO = projectAccountingTOMap.get(projectID);
					if (projectAccountingTO == null) {
						projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
						projectAccountingTOMap.put(projectID, projectAccountingTO);
					}
					costBean.setCurrency(projectAccountingTO.getCurrency());
				}
				TAccountBean accountBean = accountsMap.get(costBean.getAccount());
				if (accountBean != null) {
					costBean.setAccountName(accountBean.getFullName());
				}
				TPersonBean personBean = LookupContainer.getPersonBean(costBean.getChangedByID());
				if (personBean != null) {
					costBean.setChangedByName(personBean.getLabel());
				}
			}
		}
		return costBeanList;
	}

	
	public static List<TCostBean> reverseList(Collection<TCostBean> collection) {
		List<TCostBean> reverseList = new LinkedList<TCostBean>();
		if (collection != null) {
			for (TCostBean costBean : collection) {
				reverseList.add(0, costBean);
			}
		}
		return reverseList;
	}

	

	/**
	 * Gets the flat expenses for the current issue
	 * 
	 * @return
	 */
	public static List<FlatHistoryBean> getFlatExpense(TWorkItemBean workItemBean, TPersonBean personBean, Locale locale) {
		List<TCostBean> costBeans = ExpenseBL.loadCostsFromDb(workItemBean, personBean.getObjectID());
		return initCostFlatHistoryBeans(costBeans, null, locale);
	}

	/**
	 * Gets the flat expenses (costs) for the issue and all his descendants
	 * 
	 * @param workItemIDs
	 * @param workItemBeansMap
	 * @return
	 */
	public static List<FlatHistoryBean> getFlatExpensesWithChildren(int[] workItemIDs, Map<Integer, TWorkItemBean> workItemBeansMap, Integer personID,
			Locale locale) {
		List<TCostBean> costBeans = ExpenseBL.loadCostsForWorkItems(workItemIDs, workItemBeansMap, personID);
		return initCostFlatHistoryBeans(costBeans, workItemBeansMap, locale);
	}

	/**
	 * Initialize the FlatHistoryBeans with expenses
	 * 
	 * @param costBeans
	 * @param workItemsMap
	 * @param locale
	 * @return
	 */
	public static List<FlatHistoryBean> initCostFlatHistoryBeans(List<TCostBean> costBeans, Map<Integer, TWorkItemBean> workItemsMap, Locale locale) {
		List<FlatHistoryBean> flatCosts = new ArrayList<FlatHistoryBean>();
		String subjectLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("item.tabs.expense.editExpense.lbl.subject", locale);
		String effortLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.effort", locale);
		String costLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cost", locale);
		String descriptionLabel = LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.description", locale);
		for (TCostBean costBean : costBeans) {
			FlatHistoryBean flatHistoryBean = new FlatHistoryBean();
			List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();
			flatHistoryBean.setHistoryEntries(historyEntries);
			flatHistoryBean.setChangedByName(costBean.getChangedByName());
			flatHistoryBean.setPersonID(costBean.getChangedByID());
			flatHistoryBean.setLastEdit(costBean.getLastEdit());
			flatHistoryBean.setType(HistoryBean.HISTORY_TYPE.COST);
			flatHistoryBean.setIconName("expense.png");
			HistoryEntry historyEntry;
			String subject = costBean.getSubject();
			if (subject != null && !"".equals(subject)) {
				historyEntry = new HistoryEntry();
				historyEntry.setFieldLabel(subjectLabel);
				historyEntry.setNewValue(subject);
				historyEntries.add(historyEntry);
			}
			if (costBean.getHours() != null) {
				historyEntry = new HistoryEntry();
				historyEntry.setFieldLabel(effortLabel);
				historyEntry.setNewValue(HistoryLoaderBL.formatEffort(costBean, locale));
				historyEntries.add(historyEntry);
			}
			if (costBean.getCost() != null) {
				historyEntry = new HistoryEntry();
				historyEntry.setFieldLabel(costLabel);
				historyEntry.setNewValue(HistoryLoaderBL.formatCost(costBean, locale));
				historyEntries.add(historyEntry);
			}
			String description = costBean.getDescription();
			if (description != null && !"".equals(description)) {
				historyEntry = new HistoryEntry();
				historyEntry.setFieldLabel(descriptionLabel);
				historyEntry.setNewValue(description);
				historyEntries.add(historyEntry);
			}
			HistoryLoaderBL.addWorkItemToFlatHistoryBean(flatHistoryBean, workItemsMap, costBean.getWorkItemID(), FlatHistoryBean.RENDER_TYPE.ACTUAL_VALUES);
			flatCosts.add(flatHistoryBean);
		}
		return flatCosts;
	}

	/**
	 * Send notification about adding an expense
	 * @param personBean
	 * @param workItemBean
	 * @param costBean
	 */
	public static void notifyCostAdded(TPersonBean personBean, TWorkItemBean workItemBean, TCostBean costBean) {
		// send notification mail after saving
		AfterBudgetExpenseChangeEventParam afterBudgetExpenseChangeEventParam = new AfterBudgetExpenseChangeEventParam();
		afterBudgetExpenseChangeEventParam.setNewExpense(costBean);
		afterBudgetExpenseChangeEventParam.setWorkItemBean(workItemBean);
		afterBudgetExpenseChangeEventParam.setPersonBean(personBean);
		EventPublisher evp = EventPublisher.getInstance();
		if (evp != null) {
			List<Integer> events = new ArrayList<Integer>();
			events.add(new Integer(IEventSubscriber.EVENT_POST_ISSUE_ADDEXPENSE));
			evp.notify(events, afterBudgetExpenseChangeEventParam);
		}
	}

	/**
	 * Send a notification about editing an expense
	 * @param personBean
	 * @param workItemBean
	 * @param oldCostBean
	 * @param newCostBean
	 */
	public static void notifyCostModified(TPersonBean personBean, TWorkItemBean workItemBean, TCostBean oldCostBean, TCostBean newCostBean) {
		// send notification mail after saving
		AfterBudgetExpenseChangeEventParam afterBudgetExpenseChangeEventParam = new AfterBudgetExpenseChangeEventParam();
		afterBudgetExpenseChangeEventParam.setOldExpense(oldCostBean);
		afterBudgetExpenseChangeEventParam.setNewExpense(newCostBean);
		afterBudgetExpenseChangeEventParam.setWorkItemBean(workItemBean);
		afterBudgetExpenseChangeEventParam.setPersonBean(personBean);
		EventPublisher evp = EventPublisher.getInstance();
		if (evp != null) {
			List<Integer> events = new ArrayList<Integer>();
			events.add(new Integer(IEventSubscriber.EVENT_POST_ISSUE_UPDATEEXPENSE));
			evp.notify(events, afterBudgetExpenseChangeEventParam);
		}
	}

	/**
	 * Notify about deleting a cost
	 * @param workItemBean
	 * @param personBean
	 * @param oldCostBean
	 */
	public static void notifyCostDeleted(TWorkItemBean workItemBean, TPersonBean personBean, TCostBean oldCostBean) {
		// send notification mail after deleting the expense
		AfterBudgetExpenseChangeEventParam afterBudgetExpenseChangeEventParam = new AfterBudgetExpenseChangeEventParam();
		afterBudgetExpenseChangeEventParam.setOldExpense(oldCostBean);
		afterBudgetExpenseChangeEventParam.setWorkItemBean(workItemBean);
		afterBudgetExpenseChangeEventParam.setPersonBean(personBean);
		EventPublisher evp = EventPublisher.getInstance();
		if (evp != null) {
			List<Integer> events = new ArrayList<Integer>();
			events.add(new Integer(IEventSubscriber.EVENT_POST_ISSUE_DELETEEXPENSE));
			evp.notify(events, afterBudgetExpenseChangeEventParam);
		}
	}

}
