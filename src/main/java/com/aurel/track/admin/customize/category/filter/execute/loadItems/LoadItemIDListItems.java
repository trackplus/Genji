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

package com.aurel.track.admin.customize.category.filter.execute.loadItems;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TActualEstimatedBudgetBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TAttributeValueBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TNotifyBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.fieldType.runtime.bl.AttributeValueBL;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
import com.aurel.track.item.consInf.ConsInfBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.util.GeneralUtils;

/**
 * Loads the items by ID list
 * @author Tamas
 *
 */
public class LoadItemIDListItems {
    static final Logger LOGGER = LogManager.getLogger(LoadMeetingItems.class);

    /**
     * Get a list of workItemBeans by workItemIDs
     * @param workItemIDs
     * @param personID
     * @param includeArchivedDeleted
     * @param withCustomAttributes
     * @return
     */
    public static List<TWorkItemBean> getWorkItemBeansByWorkItemIDs(int[] workItemIDs,
            Integer personID, boolean includeArchivedDeleted, boolean withCustomAttributes, boolean editFlagNeeded) {
        List<TWorkItemBean> workItemBeanList = prepareByWorkItemIDsItems(workItemIDs, personID, includeArchivedDeleted, editFlagNeeded);
        if (withCustomAttributes) {
            List<TAttributeValueBean> attributeValueBeanList = AttributeValueBL.loadByWorkItemKeys(workItemIDs);
            return LoadItemsUtil.loadCustomFields(workItemBeanList, attributeValueBeanList);
        } else {
            return workItemBeanList;
        }
    }

    /**
     * Get a list of ReportBeans by workItemIDs
     * @param workItemIDs
     * @param includeArchivedDeleted
     * @param personID
     * @param locale
     * @return
     */
    public static List<ReportBean> getReportBeansByWorkItemIDs(int[] workItemIDs, boolean includeArchivedDeleted, Integer personID, Locale locale, boolean editFlagNeeded) {
        List<TWorkItemBean> workItemBeanList = prepareByWorkItemIDsItems(workItemIDs, personID, includeArchivedDeleted, editFlagNeeded);
        return getReportBeansByWorkItems(workItemBeanList, personID, locale, true, true, true, true, true, true, true, true, true);
    }

    /**
     * Gets the ReportBeans by workItemIDs
     * @param workItemIDs
     * @param personID
     * @param locale
     * @param projectID
     * @param entityFlag
     * @return
     */
    public static List<ReportBean> getReportBeansByWorkItemIDs(int[] workItemIDs, boolean editFlagNeeded, Integer personID, Locale locale, Integer projectID, Integer entityFlag) {
        List<TWorkItemBean> workItemBeanListDB = prepareByWorkItemIDsItems(workItemIDs, personID, false, editFlagNeeded);
        List<TWorkItemBean> workItemBeanList = LoadItemsUtil.filterByProjectAndEntityFlag(workItemBeanListDB, projectID, entityFlag);
        return getReportBeansByWorkItems(workItemBeanList, personID, locale, true, true, true, true, true, true, true, true, true);
    }

    /**
     * Gets the ReportBeans by workItemIDs
     * @param workItemIDs
     * @param personID
     * @param locale
     * @param projectID
     * @param entityFlag
     * @return
     */
    public static List<ReportBean> getReportBeansByWorkItemIDs(int[] workItemIDs, boolean editFlagNeeded,
            Integer personID, Locale locale, Integer projectID, Integer entityFlag,
            boolean withCustomAttributes, boolean withWatchers,
            boolean withMyExpenses, boolean withTotalExpenses,
            boolean withBudgetPlan, boolean withRemainingPlan,
            boolean withAttachment, boolean withLinks, boolean withParents) {
        List<TWorkItemBean> workItemBeanListDB = prepareByWorkItemIDsItems(workItemIDs, personID, false, editFlagNeeded);
        List<TWorkItemBean> workItemBeanList = LoadItemsUtil.filterByProjectAndEntityFlag(workItemBeanListDB, projectID, entityFlag);
        return getReportBeansByWorkItems(workItemBeanList, personID, locale,
                withCustomAttributes, withWatchers,
                withMyExpenses, withTotalExpenses,
                withBudgetPlan, withRemainingPlan,
                withAttachment, withLinks, withParents);
    }

    /**
     * Get a list of ReportBeans as a result of selecting some items, not intended to be shown in item navigator
     * @param workItemIDs
     * @param personID
     * @param locale
     * @return list of ReportBeans
     */
    public static List<ReportBean> getReportBeansByWorkItemIDs(int[] workItemIDs, boolean editFlagNeeded, Integer personID, Locale locale,
            boolean withCustomAttributes, boolean withWatchers,
            boolean withMyExpenses, boolean withTotalExpenses,
            boolean withBudgetPlan, boolean withRemainingPlan,
            boolean withAttachment, boolean withLinks, boolean withParents) {
        //true: if archived/deleted is selected then do not filter out
        List<TWorkItemBean> workItemBeanList = prepareByWorkItemIDsItems(workItemIDs, personID, true, editFlagNeeded);
        return getReportBeansByWorkItems(workItemBeanList, personID, locale,
                withCustomAttributes, withWatchers,
                withMyExpenses, withTotalExpenses,
                withBudgetPlan, withRemainingPlan,
                withAttachment, withLinks, withParents);
    }

    /**
     * Gets the report beans by workItemIDs
     * @param workItemBeanList
     * @param personID
     * @param locale
     * @return
     */
    public static List<ReportBean> getReportBeansByWorkItems(List<TWorkItemBean> workItemBeanList, Integer personID, Locale locale,
            boolean withCustomAttributes, boolean withWatchers,
            boolean withMyExpenses, boolean withTotalExpenses,
            boolean withBudgetPlan, boolean withRemainingPlan,
            boolean withAttachment, boolean withLinks, boolean withParents) {
        int[] workItemIDs = GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromBeanList(workItemBeanList));
        List<TAttributeValueBean> attributeValueBeanList = null;
        if (withCustomAttributes) {
            attributeValueBeanList = AttributeValueBL.loadByWorkItemKeys(workItemIDs);
        }
        List<TNotifyBean> consInfList = null;
        if (withWatchers) {
            consInfList = ConsInfBL.loadLuceneConsInf(workItemIDs);
        }
        List<TComputedValuesBean> myExpenseList = null;
        if (withMyExpenses) {
            myExpenseList = ComputedValueBL.loadByWorkItemsAndTypesAndPerson(workItemIDs, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, personID);
        }
        List<TComputedValuesBean> totalExpenseList = null;
        if (withTotalExpenses) {
            totalExpenseList = ComputedValueBL.loadByWorkItemsAndTypesAndPerson(workItemIDs, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);
        }
        List<TComputedValuesBean> budgetAndPlanList = null;
        if (withBudgetPlan) {
            int[] computedValueTypes = null;
            boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
            if (budgetActive) {
                computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, TComputedValuesBean.COMPUTEDVALUETYPE.BUDGET};
            } else {
                computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN};
            }
            budgetAndPlanList = ComputedValueBL.loadByWorkItemsAndTypes(workItemIDs, computedValueTypes);
        }
        List<TActualEstimatedBudgetBean> remainingPlanList = null;
        if (withRemainingPlan) {
            remainingPlanList = RemainingPlanBL.loadByWorkItemKeys(workItemIDs);
        }
        List<TAttachmentBean> attachmentList = null;
        if (withAttachment) {
            attachmentList = AttachBL.getAttachments(workItemIDs, false);
        }
        List<TWorkItemLinkBean> itemLinkList = null;
        if (withLinks) {
            itemLinkList = ItemLinkBL.loadByWorkItems(workItemIDs);
        }
        Set<Integer> parentIDsSet = null;
        if (withParents) {
            boolean summaryItemsBehavior = ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior();
            parentIDsSet = new HashSet<Integer>();
            if (summaryItemsBehavior) {
                parentIDsSet = getParentIDsSet(workItemBeanList);
            }
        }
        return ReportBeanLoader.populateReportBeans(workItemBeanList, attributeValueBeanList, personID, locale,
                consInfList, myExpenseList, totalExpenseList, budgetAndPlanList, remainingPlanList,
                attachmentList, itemLinkList, parentIDsSet);
    }

    /**
     * Gets the itemIDs from the list which are parents
     * @param workItemBeanList
     * @return
     */
    private static Set<Integer> getParentIDsSet(List<TWorkItemBean> workItemBeanList) {
        boolean summaryItemsBehavior = ApplicationBean.getInstance().getSiteBean().getSummaryItemsBehavior();
        Set<Integer> parentIDsSet = new HashSet<Integer>();
        if (summaryItemsBehavior) {
            List<Integer> workItemIDs = new LinkedList<Integer>();
            for (TWorkItemBean workItemBean : workItemBeanList) {
                Integer workItemID = workItemBean.getObjectID();
                workItemIDs.add(workItemID);
                Integer parentID = workItemBean.getSuperiorworkitem();
                if (parentID!=null) {
                    //parents from the result set
                    parentIDsSet.add(parentID);
                }
            }
            workItemIDs.removeAll(parentIDsSet);
            //get those parents whose children are not present in the result set
            List<TWorkItemBean> children = ItemBL.getChildren(GeneralUtils.createIntArrFromIntegerList(workItemIDs), false, null, null, null);
            for (TWorkItemBean child : children) {
                parentIDsSet.add(child.getSuperiorworkitem());
            }
        }
        return parentIDsSet;
    }



    /**
     * Loads the workItemBeans by item IDs
     * @param workItemIDs
     * @param personID
     * @param includeArchivedDeleted
     * @param startDate
     * @param endDate
     * @return
     */
    private static List<TWorkItemBean> prepareByWorkItemIDsItems(int[] workItemIDs, Integer personID, boolean includeArchivedDeleted, boolean editFlagNeeded) {
        List<TWorkItemBean> workItemBeanList = ItemBL.loadByWorkItemKeys(workItemIDs);
        if (workItemBeanList==null || workItemBeanList.isEmpty()) {
            return new LinkedList<TWorkItemBean>();
        }
        LOGGER.debug("Number of workItems before filtering " + workItemBeanList.size());
        if (!includeArchivedDeleted) {
            LoadItemsUtil.removeArchivedOrDeleted(workItemBeanList);
            LOGGER.debug("Number of workItems after removing archived/deleted ones " + workItemBeanList.size());
        }
        if (!workItemBeanList.isEmpty()) {
            workItemIDs=GeneralUtils.createIntArrFromIntegerList(GeneralUtils.createIntegerListFromBeanList(workItemBeanList));
            workItemBeanList = AccessBeans.filterWorkItemBeans(personID, workItemBeanList, editFlagNeeded);
            LOGGER.debug("Number of workItems after filtering " + workItemBeanList.size());
        }
        return workItemBeanList;
    }

    /**
     * Gets the items from the database
     * @param personID
     * @param filterUpperTO
     * @param raciBean
     * @param withCustomAttributes
     * @param attributeValueBeanList
     * @param startDate
     * @param endDate
     * @param qNode
     */
    public static List<TWorkItemBean> getItems(Integer personID, int[] workItemIDs, boolean includeArchivedDeleted, boolean editFlagNeeded,
            boolean withCustomAttributes, List<TAttributeValueBean> attributeValueBeanList, boolean withWatchers, List<TNotifyBean> watcherList,
            boolean withMyExpenses, List<TComputedValuesBean> myExpenseList,
            boolean withTotalExpenses, List<TComputedValuesBean> totalExpenseList,
            boolean withBudgetPlan, List<TComputedValuesBean> budgetAndPlanList,
            boolean withRemainingPlan, List<TActualEstimatedBudgetBean> remainingPlanList,
            boolean withAttachment, List<TAttachmentBean> attachmentList,
            boolean withLinks, List<TWorkItemLinkBean> itemLinkList,
            boolean withParents, Set<Integer> parentIDsSet) {
        Date start = null;
        if (LOGGER.isDebugEnabled()) {
            start = new Date();
        }
        List<TWorkItemBean> workItemBeanList = prepareByWorkItemIDsItems(workItemIDs, personID, includeArchivedDeleted, editFlagNeeded);
        Date itemLoadTime = null;
        if (LOGGER.isDebugEnabled() && start!=null) {
            itemLoadTime = new Date();
            LOGGER.debug("Loading " + workItemBeanList.size() + " items lasted " + new Long(itemLoadTime.getTime()-start.getTime()).toString() + " ms");
        }
        Date customAttributesTime = null;
        if (withCustomAttributes) {
            //the custom attributes are needed also if the filter has tree part to apply the matchers to potential custom fields
            List<TAttributeValueBean> customAttributesList = AttributeValueBL.loadByWorkItemKeys(workItemIDs);
            if (attributeValueBeanList!=null && customAttributesList!=null) {
                attributeValueBeanList.addAll(customAttributesList);
                LoadItemsUtil.loadCustomFields(workItemBeanList, attributeValueBeanList);
                if (LOGGER.isDebugEnabled() && itemLoadTime!=null) {
                    customAttributesTime = new Date();
                    LOGGER.debug("Loading " + customAttributesList.size() + " custom attributes lasted " + new Long(customAttributesTime.getTime()-itemLoadTime.getTime()).toString() + " ms");
                }
            }
        } else {
            customAttributesTime = itemLoadTime;
        }
        Date watchersTime = null;
        if (withWatchers) {
            List<TNotifyBean> watchers = ConsInfBL.loadLuceneConsInf(workItemIDs);
            if (watcherList!=null && watchers!=null) {
                watcherList.addAll(watchers);
                if (LOGGER.isDebugEnabled() && customAttributesTime!=null) {
                    watchersTime = new Date();
                    LOGGER.debug("Loading " + watcherList.size() + " watchers lasted " + new Long(watchersTime.getTime()-customAttributesTime.getTime()).toString() + " ms");
                }
            }
        } else {
            watchersTime = customAttributesTime;
        }
        Date myExpensesTime = null;
        if (withMyExpenses) {
            List<TComputedValuesBean> myExpenses = ComputedValueBL.loadByWorkItemsAndTypesAndPerson(workItemIDs, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, personID);
            if (myExpenseList!=null && myExpenses!=null) {
                myExpenseList.addAll(myExpenses);
                if (LOGGER.isDebugEnabled() && watchersTime!=null) {
                    myExpensesTime = new Date();
                    LOGGER.debug("Loading " + myExpenses.size() + " my expenses lasted " + new Long(myExpensesTime.getTime()-watchersTime.getTime()).toString() + " ms");
                }
            }
        } else {
            myExpensesTime = watchersTime;
        }
        Date totalExpensesTime = null;
        if (withTotalExpenses) {
            List<TComputedValuesBean> totalExpenses = ComputedValueBL.loadByWorkItemsAndTypesAndPerson(workItemIDs, TComputedValuesBean.COMPUTEDVALUETYPE.EXPENSE, null);

            if (totalExpenseList!=null && totalExpenses!=null) {
                totalExpenseList.addAll(totalExpenses);
                if (LOGGER.isDebugEnabled() && myExpensesTime!=null) {
                    totalExpensesTime = new Date();
                    LOGGER.debug("Loading " + totalExpenses.size() + " total expenses lasted " + new Long(totalExpensesTime.getTime()-myExpensesTime.getTime()).toString() + " ms");
                }
            }
        } else {
            totalExpensesTime = myExpensesTime;
        }
        Date budgetPlanTime = null;
        if (withBudgetPlan) {
            int[] computedValueTypes = null;
            boolean budgetActive = ApplicationBean.getInstance().getBudgetActive();
            if (budgetActive) {
                computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN, TComputedValuesBean.COMPUTEDVALUETYPE.BUDGET};
            } else {
                computedValueTypes = new int[] {TComputedValuesBean.COMPUTEDVALUETYPE.PLAN};
            }
            List<TComputedValuesBean> budgetsAndPlans = ComputedValueBL.loadByWorkItemsAndTypes(workItemIDs, computedValueTypes);
            if (budgetAndPlanList!=null && budgetsAndPlans!=null) {
                budgetAndPlanList.addAll(budgetsAndPlans);
                if (LOGGER.isDebugEnabled() && totalExpensesTime!=null) {
                    budgetPlanTime = new Date();
                    LOGGER.debug("Loading " + budgetsAndPlans.size() + " bugets and plans (" + computedValueTypes.length + ") lasted " + Long.toString(new Long(budgetPlanTime.getTime()-totalExpensesTime.getTime())) + " ms");
                }
            }
        } else {
            budgetPlanTime = totalExpensesTime;
        }
        Date remainingPlanTime = null;
        if (withRemainingPlan) {
            List<TActualEstimatedBudgetBean> remainingPlans = RemainingPlanBL.loadByWorkItemKeys(workItemIDs);
            if (remainingPlanList!=null && remainingPlans!=null) {
                remainingPlanList.addAll(remainingPlans);
                if (LOGGER.isDebugEnabled() && budgetPlanTime!=null) {
                    remainingPlanTime = new Date();
                    LOGGER.debug("Loading " + remainingPlans.size() + " remaining plans lasted " + Long.toString(new Long(remainingPlanTime.getTime()-budgetPlanTime.getTime())) + " ms");
                }
            }
        } else {
            remainingPlanTime = budgetPlanTime;
        }
        Date attachmentTime = null;
        if (withAttachment) {
            List<TAttachmentBean> attachmentsBeans = AttachBL.loadByWorkItems(workItemIDs);
            if (attachmentList!=null && attachmentsBeans!=null) {
                attachmentList.addAll(attachmentsBeans);
                if (LOGGER.isDebugEnabled() && budgetPlanTime!=null) {
                    attachmentTime = new Date();
                    LOGGER.debug("Loading " + attachmentsBeans.size() + " attachment beans lasted " + Long.toString(new Long(attachmentTime.getTime()-remainingPlanTime.getTime())) + " ms");
                }
            }
        } else {
            attachmentTime = remainingPlanTime;
        }
        Date linkTime = null;
        if (withLinks) {
            List<TWorkItemLinkBean> itemLinkBeans = ItemLinkBL.loadByWorkItems(workItemIDs);
            if (itemLinkList!=null && itemLinkBeans!=null) {
                itemLinkList.addAll(itemLinkBeans);
                if (LOGGER.isDebugEnabled() && budgetPlanTime!=null) {
                    linkTime = new Date();
                    LOGGER.debug("Loading " + itemLinkBeans.size() + " item link beans lasted " + Long.toString(new Long(linkTime.getTime()-attachmentTime.getTime())) + " ms");
                }
            }
        } else {
            linkTime = attachmentTime;
        }
        Date parentTime = null;
        if (withParents) {
            Set<Integer> parentIDs = getParentIDsSet(workItemBeanList);
            if (parentIDsSet!=null && parentIDs!=null) {
                parentIDsSet.addAll(parentIDs);
            }
            if (LOGGER.isDebugEnabled() && linkTime!=null) {
                parentTime = new Date();
                LOGGER.debug("Loading " + parentIDs.size() + " parents lasted " + Long.toString((parentTime.getTime()-linkTime.getTime())) + " ms");
            }
        }
        if (LOGGER.isDebugEnabled() && start!=null) {
            Date end = new Date();
            LOGGER.debug("Loading " + workItemBeanList.size() + " items with associated entities lasted " + Long.toString(new Long(end.getTime()-start.getTime())) + " ms");
        }
        return workItemBeanList;
    }
}
