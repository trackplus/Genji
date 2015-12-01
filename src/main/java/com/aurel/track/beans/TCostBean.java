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

import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.item.budgetCost.AccountingBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
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
public class TCostBean
	extends com.aurel.track.beans.base.BaseTCostBean
	implements Serializable, BudgetCostHistoryBean, ISerializableLabelBean {
	
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TCostBean.class);
	private String currency;
	private String changedByName;
	private String accountName;
	private Integer timeUnit = AccountingBL.TIMEUNITS.HOURS; //the default is hour;	
	
	@Override
	public String getLabel() {
		return getSubject();
	}
	
	/**
	 * Get the type of the history bean
	 * It should be one of the HISTORY_TYPE constants
	 * @return
	 */
	@Override
	public int getType() {
		return HistoryBean.HISTORY_TYPE.COST;
	}
	
	/*public Double getRoundHours() {
		return getHours();
	}

	public void setRoundHours(Double hours) {
		this.setHours(AccountingBL.roundToDecimalDigits(hours, true));
	}
	
	public Double getRoundCost() {
		return getCost();
	}
	
	public void setRoundCost(Double cost) {
		this.setCost(AccountingBL.roundToDecimalDigits(cost, false));
	}*/

	public boolean hasChanged(TCostBean costBean) {
		if (costBean == null) {
			return true;
		}
		if (EqualUtils.notEqual(this.getHours(), costBean.getHours())) {
			return true;
		}		
		if (EqualUtils.notEqual(this.getCost(), costBean.getCost())) {
			return true;
		}		
		if (EqualUtils.notEqual(this.getSubject(), costBean.getSubject())) {
			return true;
		}		
		if (EqualUtils.notEqual(this.getAccount(), costBean.getAccount())) {
			return true;
		}		
		if (EqualUtils.notEqual(this.getEffortdate(), costBean.getEffortdate())) {
			return true;
		}		
		if (EqualUtils.notEqual(this.getDescription(), costBean.getDescription())) {
			return true;
		}		
		return false;
	}

	/**
	 * Make a copy of the expense values into a new object 
	 */
	public TCostBean copyExpensesToNew() {
		TCostBean costBeanNew = new TCostBean();
		costBeanNew.setHours(getHours());
		costBeanNew.setCost(getCost());
		return costBeanNew;
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
	 * The ID of the person who made the modification on a workItem
	 * @return
	 */
	@Override
	public Integer getChangedByID() { 
		return getPerson();
	}

	/**
	 * The ID of the workItem, the the history bean refers to
	 * @return
	 */
	@Override
	public Integer getWorkItemID() {
		return getWorkitem();
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Integer getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(Integer timeUnit) {
		this.timeUnit = timeUnit;
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
		Integer accountID = getAccount();
		if (accountID!=null) {
			attributesMap.put("account", accountID.toString());
		}
		Integer personID = getPerson();
		if (personID!=null) {
			attributesMap.put("person", personID.toString());
		}
		//not needed because it is "embedded" in the workItem element anyway 
		/*Integer workItemID = getWorkItemID();
		if (workItemID!=null) {
			attributesMap.put("workItemID", workItemID.toString());
		}*/
		Double hours = getHours();
		if (hours!=null) {
			attributesMap.put("hours", DoubleNumberFormatUtil.formatISO(hours));
		}		
		Double cost = getCost();
		if (cost!=null) {
			attributesMap.put("cost", DoubleNumberFormatUtil.formatISO(cost));
		}
		String subject = getSubject();
		if (subject!=null && !"".equals(subject)) {
			attributesMap.put("subject", subject);
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
		Date effortdate = getEffortdate();
		if (effortdate!=null) {
			attributesMap.put("effortdate", DateTimeUtils.getInstance().formatISODate(effortdate));
		}
		String invoicenumber = getInvoicenumber();
		if (invoicenumber!=null && !"".equals(invoicenumber)) {
			attributesMap.put("invoicenumber", invoicenumber);
		}
		Date invoiceDate = getInvoicedate();
		if (invoiceDate!=null) {
			attributesMap.put("invoiceDate", DateTimeUtils.getInstance().formatISODate(invoiceDate));
		}
		String invoicepath = getInvoicepath();
		if (invoicepath==null && !"".equals(invoicepath)) {
			attributesMap.put("invoicepath", invoicepath);
		}
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		String moreProps = getMoreProps();
		if (moreProps!=null && !"".equals(moreProps)) {
			attributesMap.put("moreProps", moreProps);
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
	public TCostBean deserializeBean(Map<String, String> attributes) {
		TCostBean costBean = new TCostBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			costBean.setObjectID(new Integer(strObjectID));
		}
		String strAccount = attributes.get("account");
		if (strAccount!=null) {
			costBean.setAccount(new Integer(strAccount));
		}
		String strPerson = attributes.get("person");
		if (strPerson!=null) {
			costBean.setPerson(new Integer(strPerson));
		}
		//not needed because it is "embedded" in the workItem element anyway 
		/*String strWorkItemID = attributes.get("workItemID");
		if (strWorkItemID!=null) {
			costBean.setWorkitem(new Integer(strWorkItemID));
		}*/
		String strHours = attributes.get("hours");
		if (strHours!=null) {
			costBean.setHours(DoubleNumberFormatUtil.parseISO(strHours));
		}		
		String strCost = attributes.get("cost");
		if (strCost!=null) {
			costBean.setCost(DoubleNumberFormatUtil.parseISO(strCost));
		}
		costBean.setSubject(attributes.get("subject"));
		/*String strEfforttype = attributes.get("efforttype");
		if (strEfforttype!=null) {
			budgetBean.setEfforttype(new Integer(strEfforttype));
		}
		String strEffortvalue = attributes.get("effortvalue");
		if (strEffortvalue!=null) {
			budgetBean.setEffortvalue(new Double(strEffortvalue));
		}*/
		String strEffortDate = attributes.get("effortdate");
		if (strEffortDate!=null) {
			costBean.setEffortdate(DateTimeUtils.getInstance().parseISODate(strEffortDate));
		}
		costBean.setInvoicenumber(attributes.get("invoicenumber"));
		String strInvoiceDate = attributes.get("invoicedate");
		if (strInvoiceDate!=null) {
			costBean.setInvoicedate(DateTimeUtils.getInstance().parseISODate(strInvoiceDate));
		}
		costBean.setInvoicepath(attributes.get("invoicepath"));	
		costBean.setDescription(attributes.get("description"));
		costBean.setMoreProps(attributes.get("moreProps"));

		String strLastEdit = attributes.get("lastEdit");
		if (strLastEdit!=null) {
			costBean.setLastEdit(DateTimeUtils.getInstance().parseISODateTime(strLastEdit));
		}
		costBean.setUuid(attributes.get("uuid"));
		return costBean;
	}
	
	/**
	 * Copy the direct data from the actual bean 
	 */
	public TCostBean copy(TCostBean costBean) {
		costBean.setPerson(getPerson());
		costBean.setAccount(getAccount());
		costBean.setHours(getHours());
		costBean.setCost(getCost());
		costBean.setSubject(getSubject());
		costBean.setEffortdate(getEffortdate());
		costBean.setInvoicenumber(getInvoicenumber());
		costBean.setInvoicedate(getInvoicedate());
		costBean.setInvoicepath(getInvoicepath());
		costBean.setDescription(getDescription());
		costBean.setMoreProps(getMoreProps());
		costBean.setLastEdit(getLastEdit());
		return costBean;
	}
	
	/**
	 * Replace the lookup values 
	 */
	public void replaceLookup(Map<String, Map<Integer, Integer>> matcherMap) {
		Map<Integer, Integer> workItemIDsMap = matcherMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));		
		if (workItemIDsMap!=null && workItemIDsMap.get(getWorkitem())!=null) {
			setWorkitem(workItemIDsMap.get(getWorkitem()));
		} else {
			LOGGER.warn("No internal workItemID found for external workItemID " + getChangedByID() + " in costBean ");
		}
		Map<Integer, Integer> personMap = matcherMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
		if (personMap!=null && personMap.get(getPerson())!=null) {
			setPerson(personMap.get(getPerson()));
		} else {
			LOGGER.warn("No internal person found for external person " + getPerson() + " in costBean ");
		}
		Map<Integer, Integer> accountMap = matcherMap.get(ExchangeFieldNames.ACCOUNT);
		if (accountMap!=null && accountMap.get(getAccount())!=null) {
			setAccount(accountMap.get(getAccount()));
		} else {
			LOGGER.warn("No internal account found for external account " + getAccount() + " in costBean ");
		}
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		//never same (two costs are the same only if the uuids are same)
		//this method will never be called anyway
		return false;
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TCostBean costBean = (TCostBean)serializableLabelBean;
		Map<Integer, Integer> workItemIDsMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_ISSUENO, null));
		if (workItemIDsMap!=null && workItemIDsMap.get(getWorkitem())!=null) {
			costBean.setWorkitem(workItemIDsMap.get(getWorkitem()));
		} else {
			LOGGER.warn("No internal workItemID found for external workItemID " + getWorkitem() + " in costBean ");
		}
		Map<Integer, Integer> personMap = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
		if (personMap!=null && personMap.get(getPerson())!=null) {
			costBean.setPerson(personMap.get(getPerson()));
		} else {
			LOGGER.warn("No internal person found for external person " + getPerson() + " in costBean ");
		}
		Map<Integer, Integer> accountMap = matchesMap.get(ExchangeFieldNames.ACCOUNT);
		if (accountMap!=null && accountMap.get(getAccount())!=null) {
			costBean.setAccount(accountMap.get(getAccount()));
		} else {
			LOGGER.warn("No internal account found for external account " + getAccount() + " in costBean ");
		}
		return ExpenseBL.saveCostBean(costBean);
	}
}
