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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dao.BudgetDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.history.BudgetCostHistoryBean;
import com.aurel.track.item.history.HistoryBean;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.numberFormatter.DoubleNumberFormatUtil;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TBudgetBean
	extends com.aurel.track.beans.base.BaseTBudgetBean
	implements Serializable, BudgetCostHistoryBean, ISerializableLabelBean {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TBudgetBean.class);
	private static BudgetDAO budgetDAO = DAOFactory.getFactory().getBudgetDAO();
	private String currency;
	private String changedByName;
	
	@Override
	public String getLabel() {
		return getChangeDescription();
	}
	
	public static interface BUDGET_TYPE {
		//bottom up planned value
		public final static Integer PLANNED_VALUE = Integer.valueOf(1);
		//top down budget
		public final static Integer BUDGET = Integer.valueOf(2);
	}
	
	
	/**
	 * Get the type of the history bean
	 * It should be one of the HISTORY_TYPE constants
	 * @return
	 */
	@Override
	public int getType() {
		Integer budgetType = getBudgetType();
		if (budgetType==null || budgetType.equals(BUDGET_TYPE.PLANNED_VALUE)) {
			return HistoryBean.HISTORY_TYPE.PLANNED_VALUE_CHANGE;
		} else {
			return HistoryBean.HISTORY_TYPE.BUDGET_CHANGE;
		}
	}
	
	/*public void setRoundHours(Double hours) {
		this.setEstimatedHours(AccountingBL.roundToDecimalDigits(hours, true));
	}
	
	public Double getRoundHours() {
		return getEstimatedHours();
	}

	public void setRoundCost(Double cost) {
		this.setEstimatedCost(AccountingBL.roundToDecimalDigits(cost, false));
	}
	
	public Double getRoundCost() {
		return getEstimatedCost();
	}*/
	
	public boolean hasChanged(TBudgetBean lastBudgetBean) {
		boolean result = false;
		if (lastBudgetBean == null) {
			return true;
		}
		if (EqualUtils.notEqual(this.getEstimatedHours(), lastBudgetBean.getEstimatedHours())) {
			result = true;
		}
		if (EqualUtils.notEqual(this.getTimeUnit(), lastBudgetBean.getTimeUnit())) {
			result = true;
		}
		if (EqualUtils.notEqual(this.getEstimatedCost(), lastBudgetBean.getEstimatedCost())) {
			result = true;
		}		
		if (EqualUtils.notEqual(this.getChangeDescription(), lastBudgetBean.getChangeDescription())) {
			result = true;
		}		
		return result;
	}

	/**
	 * Make a copy of the existing object
	 * Modification means always a new entry in the table 
	 * so a new object is needed
	 */
	public TBudgetBean copyToNew() {
		TBudgetBean budgetBeanNew = new TBudgetBean();
		budgetBeanNew.setWorkItemID(getWorkItemID());
		budgetBeanNew.setEstimatedHours(getEstimatedHours());
		budgetBeanNew.setTimeUnit(getTimeUnit());
		budgetBeanNew.setEstimatedCost(getEstimatedCost());
		budgetBeanNew.setChangeDescription(getChangeDescription());
		budgetBeanNew.setBudgetType(getBudgetType());
		return budgetBeanNew;
	}
	
	public boolean isEmpty() {
		return (getEstimatedHours()==null || getEstimatedHours().doubleValue()==0) && 
		(getEstimatedCost()==null || getEstimatedCost().doubleValue()==0);
	}
	
	@Override
	public String getCurrency() {
		return currency;
	}
	@Override
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * The name of the person who made the modification on a workItem
	 * @return
	 */
	 @Override
	 public String getChangedByName() {
		
		return changedByName;
	}

	/**
	 * @param changedByName the changedByName to set
	 */
	@Override
	public void setChangedByName(String changedByName) {
		this.changedByName = changedByName;
	}
	
	/**
	 *  Get the description of the change
	 */
	@Override
	public String getDescription() {
		return getChangeDescription();
	}

	/**
	 * Set the description of the change
	 * @param description
	 */
	@Override
	public void setDescription(String description) {
		setChangeDescription(description);
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		//not needed because it is "embedded" in the workItem element anyway 
		/*Integer workItemID = getWorkItemID();
		if (workItemID!=null) {
			attributesMap.put("workItemID", workItemID.toString());
		}*/
		Double estimatedHours = getEstimatedHours();
		if (estimatedHours!=null) {
			attributesMap.put("estimatedHours", DoubleNumberFormatUtil.formatISO(estimatedHours));
		}
		Integer timeUnit = getTimeUnit();
		if (timeUnit!=null) {
			attributesMap.put("timeUnit", timeUnit.toString());
		}
		Double estimatedCost = getEstimatedCost();
		if (estimatedCost!=null) {
			attributesMap.put("estimatedCost", DoubleNumberFormatUtil.formatISO(estimatedCost));
		}
		Integer budgetType = getBudgetType();
		if (budgetType!=null) {
			attributesMap.put("budgetType", budgetType.toString());
		}
		/*
		Integer efforttype = getEfforttype();
		if (efforttype!=null) {
			attributesMap.put("efforttype", efforttype.toString());
		}
		Double effortvalue = getEffortvalue();
		if (effortvalue!=null) {
			attributesMap.put("effortvalue", effortvalue.toString());
		}*/		
		attributesMap.put("changeDescription", getChangeDescription());
		attributesMap.put("moreProps", getMoreProps());
		Integer changedByID = getChangedByID();
		if (changedByID!=null) {
			attributesMap.put("changedByID", changedByID.toString());
		}
		Date lastEdit = getLastEdit(); 
		if (lastEdit!=null) {
			attributesMap.put("lastEdit", DateTimeUtils.getInstance().formatISODateTime(lastEdit));
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public TBudgetBean deserializeBean(Map<String, String> attributes) {
		TBudgetBean budgetBean = new TBudgetBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			budgetBean.setObjectID(new Integer(strObjectID));
		}
		//not needed because it is "embedded" in the workItem element anyway 
		/*String strWorkItemID = attributes.get("workItemID");
		if (strWorkItemID!=null) {
			budgetBean.setWorkItemID(new Integer(strWorkItemID));
		}*/
		String strEstimatedHours = attributes.get("estimatedHours");
		if (strEstimatedHours!=null) {
			budgetBean.setEstimatedHours(DoubleNumberFormatUtil.parseISO(strEstimatedHours));
		}
		String strTimeUnit = attributes.get("timeUnit");
		if (strTimeUnit!=null) {
			budgetBean.setTimeUnit(new Integer(strTimeUnit));
		}
		String strEstimatedCost = attributes.get("estimatedCost");
		if (strEstimatedCost!=null) {
			budgetBean.setEstimatedCost(DoubleNumberFormatUtil.parseISO(strEstimatedCost));
		}
		
		String budgetType = attributes.get("budgetType");
		if (budgetType!=null) {
			budgetBean.setBudgetType(Integer.valueOf(budgetType));
		}
		
		/*String strEfforttype = attributes.get("efforttype");
		if (strEfforttype!=null) {
			budgetBean.setEfforttype(new Integer(strEfforttype));
		}
		String strEffortvalue = attributes.get("effortvalue");
		if (strEffortvalue!=null) {
			budgetBean.setEffortvalue(new Double(strEffortvalue));
		}*/
		budgetBean.setChangeDescription(attributes.get("changeDescription"));
		budgetBean.setMoreProps(attributes.get("moreProps"));
		
		String strChangedByID = attributes.get("changedByID");
		if (strChangedByID!=null) {
			budgetBean.setChangedByID(new Integer(strChangedByID));
		}
		
		String strLastEdit = attributes.get("lastEdit");
		if (strLastEdit!=null) {
			budgetBean.setLastEdit(DateTimeUtils.getInstance().parseISODateTime(strLastEdit));
		}
		budgetBean.setUuid(attributes.get("uuid"));
		return budgetBean;
	}
		

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		//never same (two budgets are the same only if the uuids are same)
		//this method will never be called anyway
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TBudgetBean budgetBean = (TBudgetBean)serializableLabelBean;
		Map<Integer, Integer> workItemIDsMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));
		if (workItemIDsMap!=null && workItemIDsMap.get(getWorkItemID())!=null) {
			budgetBean.setWorkItemID(workItemIDsMap.get(getWorkItemID()));
		} else {
			LOGGER.warn("No internal workItemID found for external workItemID " + getWorkItemID() + " in budgetBean ");
		}
		Map<Integer, Integer> personMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
		if (personMap!=null && personMap.get(getChangedByID())!=null) {
			budgetBean.setChangedByID(personMap.get(getChangedByID()));
		} else {
			LOGGER.warn("No internal person found for external person " + getChangedByID() + " in budgetBean ");
		}
		return budgetDAO.save(budgetBean);
	}

}
