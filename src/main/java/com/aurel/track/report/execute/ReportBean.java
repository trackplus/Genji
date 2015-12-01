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


package com.aurel.track.report.execute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.prop.ApplicationBean;

/**
 * This class represents a single row of a report.
 * @author Tamas Ruff
 */
public class ReportBean extends ShowableWorkItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	protected String shortPackageDescription = null;
		
	/**
	 * Map with show ISO values: typically the show ISO value is the show value
	 * This map is typically used for date and number values formatted by iso format
	 * -	key: fieldID (Integer)
	 * -	value: iso formatted string for date, number etc. 
	 */
	protected Map<Integer, String> showISOValuesMap = new HashMap<Integer, String>();
	
	/**
	 * whether the report bean is editable (readable is it anyway)
	 */
	protected boolean editable = false;
	/**
	 * might add link
	 */
	protected boolean linking = false;
	/**
	 * the not editable fields
	 */
	protected Set<Integer> notEditableFields = new HashSet<Integer>();
	
	protected boolean committedDateConflict = false;
	
	protected boolean targetDateConflict = false;
	
	protected boolean budgetConflict = false;
	
	protected boolean plannedValueConflict = false;
	
	protected boolean notClosedConflictingAncestor = false; 
	
	protected boolean summary = false;
	
	protected int bottomUpDateDueFlag;
	
	protected int topDownDateDueFlag;
	
	protected String projectType;
	
	//Holds value of property level
	protected int level = 0;

	//Holds value of property hasSons
	protected boolean hasSons;

	//is the item expanded or not
	protected boolean expanded;

	//was item added already to report-list
	protected boolean isInReportList;

	private List<ReportBean> children = null;
	
	private ReportBean parent;
	
	/**
	 * Whether the item is part of original filter result (false) or was loaded as a related item (ancestor, descendant, linked item) to the original base result 
	 */
	private boolean extendedItem = false;
	
	/**
	 * the workItems linked with the selected linkType (a history-like row in the report overview)
	 */
	private SortedSet<Integer> linkedByLinkTypeWorkItemIDs = null;
	
	/**
	 * Linked workItems (a column in the item navigator)
	 */
	private SortedSet<ReportBeanLink> reportBeanLinksSet = null;
	
	/**
	 * MsProject report bean links for Gantt chart: it contains the successor items (of the predecessor workItem)
	 * It is conversely stored as the MS Project links (the successor workItem contains the list of predecessor items) 
	 */
	private SortedSet<ReportBeanLink> ganntMSProjectReportBeanLinksSet = null;
	
	/**
	 * The personIDs of the consulted persons
	 */
	private Set<Integer> consultedList = null;
	
	/**
	 * The personIDs of the informed persons
	 */
	private Set<Integer> informedList = null;
	
	
	/**
	 * Whether the ReportBean should be showed when grouping is active
	 * When grouping is not active it has to be true
	 */
	private boolean inGroupedList = true;
	
	/**
	 * The attachments ids separated by ";" 
	 */
	private String attachmentIds;
	
	
	public ReportBean() {
		super();
	}

	/**
	 * A report bean based on a project (used in Gantt chart)
	 * @param projectBean
	 */
	public ReportBean(TProjectBean projectBean) {
		super();
	}
	
	/**
	 * Gets the ISO show value to be exported in the xml 
	 * (in a locale independent format for date and numbers to support further processing)  
	 * @param fieldID
	 * @return
	 */
	public Object getShowISOValue(Integer fieldID) {
		//first try the explicit ISO value 
		Object explicitISOValue = showISOValuesMap.get(fieldID);
		if (explicitISOValue != null) {
			return explicitISOValue;
		} else {
			//otherwise fall back to the showValue because only those ISO values are 
			//explicitly stored which differs form the showValue (in order to save space)
			return getShowValue(fieldID);	
		}
	}
	
	public void setPackageDescription(String ds) {
		if (ds == null) {
			ds = "";
		}
		getWorkItemBean().setDescription(ds);
		if (ds.length() <= ApplicationBean.getInstance().getSiteBean().getDescriptionLength()) {
			setShortPackageDescription(ds);
		} else {
			String ws = ".,;!? "; // recognized delimiters
			int sl = ApplicationBean.getInstance().getSiteBean().getDescriptionLength() - 1;
			for (int i = sl; i < ds.length(); ++i) {
				for (int j = 0; j < ws.length(); ++j) {
					if (ds.substring(i, i + 1).equals(ws.substring(j, j + 1))) {
						sl = i;
						break;
					}
				}
				if (sl != ApplicationBean.getInstance().getSiteBean().getDescriptionLength() - 1)
					break;
			}
			setShortPackageDescription(ds.substring(0, sl) + "...");
		}
	}

	/** Add a child
	 * @param child
	 */
	public void addChild(ReportBean child) {
		if (children == null) {
			children = new ArrayList<ReportBean>();
		}
		children.add(child);
		child.parent = this; 
	}

	/** Get the list of children
	 * @return
	 */
	public List<ReportBean> getChildren() {
		return children;
	}

	/**
	 * Remove all children 
	 */
	public void removeChildren() {
		children = null;
	}

	public Set<Integer> getDescendentsSet() {
		Set<Integer> descendents = new HashSet<Integer>();
		if (children==null) {
			return descendents;
		}
		Iterator<ReportBean> itrReportBean = children.iterator();
		while (itrReportBean.hasNext()) {
			ReportBean childReportBean = itrReportBean.next();
			descendents.add(childReportBean.getWorkItemBean().getObjectID());
			if (childReportBean.getChildren()!=null) {
				descendents.addAll(childReportBean.getDescendentsSet());
			}
		}
		return descendents;
	}

	/**
	 * @return Returns the shortPackageDescription.
	 */
	public String getShortPackageDescription() {
		return shortPackageDescription;
	}

	/**
	 * @param shortPackageDescription The shortPackageDescription to set.
	 */
	public void setShortPackageDescription(String shortPackageDescription) {
		this.shortPackageDescription = shortPackageDescription;
	}

	/** Getter for property level.
	 * @return Value of property level.
	 */
	public int getLevel() {
		return this.level;
	}

	/** Setter for property level.
	 * @param level New value of property level.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/** Getter for property hasSons.
	 * @return Value of property hasSons.
	 *
	 */
	public boolean isHasSons() {
		return this.hasSons;
	}

	/** Setter for property hasSons.
	 * @param hasSons New value of property hasSons.
	 *
	 */
	public void setHasSons(boolean hasSons) {
		this.hasSons = hasSons;
	}

	/**
	 * @return Returns the expanded.
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * @param expanded The expanded to set.
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	/**
	 * @return Returns the isInReportList.
	 */
	public boolean isInReportList() {
		return isInReportList;
	}

	/**
	 * @param isInReportList The isInReportList to set.
	 */
	public void setInReportList(boolean isInReportList) {
		this.isInReportList = isInReportList;
	}
	
	/**
	 * @return the showISOValues
	 */
	public Map<Integer, String> getShowISOValuesMap() {
		return showISOValuesMap;
	}

	/**
	 * @param showISOValues the showISOValues to set
	 */
	public void setShowISOValuesMap(Map<Integer, String> showISOValues) {
		this.showISOValuesMap = showISOValues;
	}
	
	public Set<Integer> getNotEditableFields() {
		return notEditableFields;
	}

	public void setNotEditableFields(Set<Integer> notEditableFields) {
		this.notEditableFields = notEditableFields;
	}

	/**
	 * @return Returns the onPlan.
	 */
	public boolean isCommittedDateConflict() {
		return committedDateConflict;
	}
	
	/**
	 * @param onPlan the onPlan to set
	 */
	public void setCommittedDateConflict(boolean withinCommittedDate) {
		this.committedDateConflict = withinCommittedDate;
	}
	
	public boolean isTargetDateConflict() {
		return targetDateConflict;
	}

	public void setTargetDateConflict(boolean targetDateConflict) {
		this.targetDateConflict = targetDateConflict;
	}
	
	public boolean isDateConflict() {
		return committedDateConflict || targetDateConflict;
	}
	
	/**
	 * @return the onBudgetPlan
	 */
	public boolean isPlannedValueConflict() {
		return plannedValueConflict;
	}

	/**
	 * @param onBudgetPlan the onBudgetPlan to set
	 */
	public void setPlannedValueConflict(boolean plannedValueConflict) {
		this.plannedValueConflict = plannedValueConflict;
	}

	public boolean isBudgetConflict() {
		return budgetConflict;
	}

	public void setBudgetConflict(boolean budgetConflict) {
		this.budgetConflict = budgetConflict;
	}

	public boolean isBudgetOrPlanConflict() {
		return plannedValueConflict || budgetConflict;
	}
	
	public boolean isSummary() {
		return summary;
	}

	public void setSummary(boolean summary) {
		this.summary = summary;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	
	
	/**
	 * Set the over budget values
	 */
	public void setWithinBudgetAndPlan(boolean budgetActive, boolean workTracking, boolean costTracking) {
		Double budgetTime = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_TIME);
		Double budgetCost = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.BUDGET_COST);
		
		Double totalTimeExpense = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_TIME);
		Double totalCostExpense = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_EXPENSE_COST);

		Double totalPlannedTime = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_TIME);
		Double totalPlannedCost = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.TOTAL_PLANNED_COST);

		Double remainingPlannedTime = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_TIME);
		Double remainingPlannedCost = (Double)sortOrderValuesMap.get(TReportLayoutBean.PSEUDO_COLUMNS.REMAINING_PLANNED_COST);
		
		setPlannedValueConflict((isPlannedValueConflict(totalPlannedTime, totalTimeExpense, remainingPlannedTime, true) && workTracking) || 
				(isPlannedValueConflict(totalPlannedCost, totalCostExpense, remainingPlannedCost, false) && costTracking));
		if (budgetActive) {
			setBudgetConflict((isBudgetConflict(budgetTime, totalPlannedTime) && workTracking) || 
				(isBudgetConflict(budgetCost, totalPlannedCost) && costTracking));
		}
	}
	
	private static boolean isPlannedValueConflict(Double totalPlan, Double totalExpense, Double remainingPlan, boolean hours) {
		if (totalPlan!=null) {
			if (totalExpense==null) {
				totalExpense = new Double(0);
			}
			if (remainingPlan==null || remainingPlan.doubleValue()<0) {
				remainingPlan = new Double(0);
			}
			if (AccountingBL.roundToDecimalDigits(totalExpense.doubleValue()+remainingPlan.doubleValue(), hours).doubleValue()>totalPlan.doubleValue()) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isBudgetConflict(Double totalBudget, Double totalPlan) {
		if (totalBudget!=null) {
			if (totalPlan==null) {
				totalPlan = new Double(0);
			}
			if (totalPlan.doubleValue()>totalBudget.doubleValue()) {
				return true;
			}
		}
		return false;
	}

	public boolean isInGroupedList() {
		return inGroupedList;
	}

	public void setInGroupedList(boolean inGroupedList) {
		this.inGroupedList = inGroupedList;
	}

	public SortedSet<Integer> getLinkedByLinkTypeWorkItemIDs() {
		return linkedByLinkTypeWorkItemIDs;
	}

	public void setLinkedByLinkTypeWorkItemIDs(SortedSet<Integer> linkedByLinkTypeWorkItemIDs) {
		this.linkedByLinkTypeWorkItemIDs = linkedByLinkTypeWorkItemIDs;
	}

	public SortedSet<ReportBeanLink> getReportBeanLinksSet() {
		return reportBeanLinksSet;
	}

	public void setReportBeanLinksSet(SortedSet<ReportBeanLink> reportBeanLinksSet) {
		this.reportBeanLinksSet = reportBeanLinksSet;
	}

	public SortedSet<ReportBeanLink> getGanntMSProjectReportBeanLinksSet() {
		return ganntMSProjectReportBeanLinksSet;
	}

	public void setGanntMSProjectReportBeanLinksSet(
			SortedSet<ReportBeanLink> ganntMSProjectReportBeanLinksSet) {
		this.ganntMSProjectReportBeanLinksSet = ganntMSProjectReportBeanLinksSet;
	}
	
	public String getLinkedItems() {
		StringBuilder stringBuilder = new StringBuilder();
		if (this.reportBeanLinksSet!=null) {
			for (Iterator<ReportBeanLink> iterator = this.reportBeanLinksSet.iterator(); iterator.hasNext();) {
				ReportBeanLink reportBeanLink = iterator.next();
				stringBuilder.append(reportBeanLink.getWorkItemID());
				if (iterator.hasNext()) {
					stringBuilder.append(" | ");
				}
			}
		}
		return stringBuilder.toString();
	}
	
	public boolean isExtendedItem() {
		return extendedItem;
	}

	public void setExtendedItem(boolean extendedItem) {
		this.extendedItem = extendedItem;
	}

	public Set<Integer> getConsultedList() {
		return consultedList;
	}

	public void setConsultedList(Set<Integer> consultedList) {
		this.consultedList = consultedList;
	}

	public Set<Integer> getInformedList() {
		return informedList;
	}

	public void setInformedList(Set<Integer> informedList) {
		this.informedList = informedList;
	}

	public String getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(String attachmentIds) {
		this.attachmentIds = attachmentIds;
	}
	
	public ReportBean getParent() {
		return parent;
	}

	public void setParent(ReportBean parent) {
		this.parent = parent;
	}

	public int getBottomUpDateDueFlag() {
		return bottomUpDateDueFlag;
	}

	public void setBottomUpDateDueFlag(int bottomUpDateDueFlag) {
		this.bottomUpDateDueFlag = bottomUpDateDueFlag;
	}
	
	public int getTopDownDateDueFlag() {
		return topDownDateDueFlag;
	}

	public void setTopDownDateDueFlag(int topDownDateDueFlag) {
		this.topDownDateDueFlag = topDownDateDueFlag;
	}

	public boolean isNotClosedConflictingAncestor() {
		return notClosedConflictingAncestor;
	}

	public void setNotClosedConflictingAncestor(boolean notClosedConflictingAncestor) {
		this.notClosedConflictingAncestor = notClosedConflictingAncestor;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isLinking() {
		return linking;
	}

	public void setLinking(boolean linking) {
		this.linking = linking;
	}

	
}
