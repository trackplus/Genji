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

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.budgetCost.RemainingPlanBL;
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
public class TActualEstimatedBudgetBean
    extends com.aurel.track.beans.base.BaseTActualEstimatedBudgetBean
    implements Serializable, BudgetCostHistoryBean, ISerializableLabelBean 
{	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TActualEstimatedBudgetBean.class);
	//possible adjust options
	public static class ADJUSTOPTIONS {
		public static int LEAVEEXISTING = 1;
		public static int AUTOADJUST = 2;
		public static int CHANGEESTIMATE = 3;
	}
	
	//int array of possible adjust options for getting the localised values from resources
	//(populating adjustOptions list)
	public static int[] ADJUSTOPTIONSARRAY  = new int[]{ADJUSTOPTIONS.LEAVEEXISTING, 
														ADJUSTOPTIONS.AUTOADJUST/*,
														ADJUSTOPTIONS.CHANGEESTIMATE*/};


	private String changedByName;
	private String currency;
	//TODO remove it if field added in the database
	//private Integer adjust = new Integer(TActualEstimatedBudgetBean.ADJUSTOPTIONS.AUTOADJUST); //the default is leave existing
	
	@Override
	public String getLabel() {
		return null;
	}
	
	public boolean isEmpty() {
		return getEstimatedCost()==null && getEstimatedHours()==null;
	}
	
	/**
	 * Get the type of the history bean
	 * It should be one of the HISTORY_TYPE constants
	 * @return
	 */
	@Override
	public int getType() {
		return HistoryBean.HISTORY_TYPE.REMAINING_PLAN_CHANGE;
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
	
	public void copyValues(TActualEstimatedBudgetBean lastEstimatedRemainingBudgetBean)
	{
		if (lastEstimatedRemainingBudgetBean!=null)
		{
			this.setEstimatedHours(lastEstimatedRemainingBudgetBean.getEstimatedHours());
			this.setTimeUnit(lastEstimatedRemainingBudgetBean.getTimeUnit());
			this.setEstimatedCost(lastEstimatedRemainingBudgetBean.getEstimatedCost());
		}
	}
	
	public boolean hasChanged(TActualEstimatedBudgetBean oldEstimatedRemainingBudgetBean) {
		boolean result = false;
		if (oldEstimatedRemainingBudgetBean == null) {
			return true;
		}
		if (EqualUtils.notEqual(this.getEstimatedHours(), oldEstimatedRemainingBudgetBean.getEstimatedHours())) {
			result = true;
		}
		if (EqualUtils.notEqual(this.getTimeUnit(), oldEstimatedRemainingBudgetBean.getTimeUnit())) {
			result = true;
		}
		if (EqualUtils.notEqual(this.getEstimatedCost(), oldEstimatedRemainingBudgetBean.getEstimatedCost())) {
			result = true;
		}
		return result;
	}

	/**
	 * @return the changedByName
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
	 * @return the currency
	 */
	@Override
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	@Override
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 *  Get the description of the change
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/**
	 * Set the description of the change
	 * @param description
	 */
	@Override
	public void setDescription(String description) {
	}

	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
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
		/*
		Integer efforttype = getEfforttype();
		if (efforttype!=null) {
			attributesMap.put("efforttype", efforttype.toString());
		}
		Double effortvalue = getEffortvalue();
		if (effortvalue!=null) {
			attributesMap.put("effortvalue", effortvalue.toString());
		}*/			
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
	public TActualEstimatedBudgetBean deserializeBean(Map<String, String> attributes) {
		TActualEstimatedBudgetBean actualEstimatedBudgetBean = new TActualEstimatedBudgetBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			actualEstimatedBudgetBean.setObjectID(new Integer(strObjectID));
		}
		//not needed because it is "embedded" in the workItem element anyway
		/*String strWorkItemID = attributes.get("workItemID");
		if (strWorkItemID!=null) {
			actualEstimatedBudgetBean.setWorkItemID(new Integer(strWorkItemID));
		}*/
		String strEstimatedHours = attributes.get("estimatedHours");
		if (strEstimatedHours!=null) {
			actualEstimatedBudgetBean.setEstimatedHours(DoubleNumberFormatUtil.parseISO(strEstimatedHours));
		}
		String strTimeUnit = attributes.get("timeUnit");
		if (strTimeUnit!=null) {
			actualEstimatedBudgetBean.setTimeUnit(new Integer(strTimeUnit));
		}
		String strEstimatedCost = attributes.get("estimatedCost");
		if (strEstimatedCost!=null) {
			actualEstimatedBudgetBean.setEstimatedCost(DoubleNumberFormatUtil.parseISO(strEstimatedCost));
		}
		/*String strEfforttype = attributes.get("efforttype");
		if (strEfforttype!=null) {
			budgetBean.setEfforttype(new Integer(strEfforttype));
		}
		String strEffortvalue = attributes.get("effortvalue");
		if (strEffortvalue!=null) {
			budgetBean.setEffortvalue(new Double(strEffortvalue));
		}*/		
		String strChangedByID = attributes.get("changedByID");
		if (strChangedByID!=null) {
			actualEstimatedBudgetBean.setChangedByID(new Integer(strChangedByID));
		}
		
		String strLastEdit = attributes.get("lastEdit");
		if (strLastEdit!=null) {
			actualEstimatedBudgetBean.setLastEdit(DateTimeUtils.getInstance().parseISODateTime(strLastEdit));
		}
		actualEstimatedBudgetBean.setUuid(attributes.get("uuid"));
		return actualEstimatedBudgetBean;
	}
	
	/**
	 * Copy the data from the actual bean 
	 */
	public TActualEstimatedBudgetBean copy(TActualEstimatedBudgetBean remainingBudgetBean) {
		remainingBudgetBean.setEstimatedHours(getEstimatedHours());
		remainingBudgetBean.setTimeUnit(getTimeUnit());
		remainingBudgetBean.setEstimatedCost(getEstimatedCost());
		remainingBudgetBean.setChangedByID(getChangedByID());
		remainingBudgetBean.setLastEdit(getLastEdit());
		return remainingBudgetBean;
	}
	
	/**
	 * Copy the direct data from the actual bean 
	 */
	public void replaceLookup(Map<String, Map<Integer, Integer>> matcherMap) {
		Map<Integer, Integer> workItemIDsMap = matcherMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));
		if (workItemIDsMap!=null && workItemIDsMap.get(getWorkItemID())!=null) {
			setWorkItemID(workItemIDsMap.get(getWorkItemID()));
		} else {
			LOGGER.warn("No internal workItemID found for external workItemID " + getChangedByID() + " in actualEstimatedBudgetBean");
		}
		Map<Integer, Integer> personMap = matcherMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
		if (personMap!=null && personMap.get(getChangedByID())!=null) {
			setChangedByID(personMap.get(getChangedByID()));
		} else {
			LOGGER.warn("No internal person found for external person " + getChangedByID() + " in actualEstimatedBudgetBean");
		}
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TActualEstimatedBudgetBean actualEstimatedBudgetBean = (TActualEstimatedBudgetBean)serializableLabelBean;
		Map<Integer, Integer> workItemIDsMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));
		if (workItemIDsMap!=null && workItemIDsMap.get(getWorkItemID())!=null) {
			actualEstimatedBudgetBean.setWorkItemID(workItemIDsMap.get(getWorkItemID()));
		} else {
			LOGGER.warn("No internal workItemID found for external workItemID " + getWorkItemID() + " in remaining budget");
		}
		Map<Integer, Integer> personMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
		if (personMap!=null && personMap.get(getChangedByID())!=null) {
			actualEstimatedBudgetBean.setChangedByID(personMap.get(getChangedByID()));
		} else {
			LOGGER.warn("No internal person found for external person " + getChangedByID() + " in remaining budget");
		}
		return RemainingPlanBL.save(actualEstimatedBudgetBean);
	}
}
