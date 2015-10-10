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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.account.AccountBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TAccountBean
	extends com.aurel.track.beans.base.BaseTAccountBean
	implements Serializable, IBeanID, ISerializableLabelBean
{	
	private static final long serialVersionUID = 1L;
	public static class STATEFLAG
	{
		public static final int AVAILABLE = 0;
		public static final int CLOSED = 1;
	}
	
	Integer stateFlag;
	
	public String getFullName() {
		String accountNumber = getAccountNumber();
		String accountName = getAccountName();
		String label;
		if (accountNumber==null) {
			accountNumber = "";
		}
		if (accountName==null) {
			accountName = "";
		}
		label = accountNumber;
		if (accountName!=null && accountName.length()>0) {
			if (label!=null && label.length()>0) {
				label = label + " - " + accountName;
			} else {
				label = accountName;
			}
		}
		if (stateFlag!=null && stateFlag.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE) {
			label += " *";
		}
		return label;
	}

	/*public Integer getStateFlag() {
		return stateFlag;
	}*/

	/*public void setStateFlag(Integer stateFlag) {
		this.stateFlag = stateFlag;
	}*/

	public String getLabel() {
		return getFullName();
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("accountNumber", getAccountNumber());
		String accountName = getAccountName();
		if (accountName!=null && !"".equals(accountName)) {
			attributesMap.put("accountName", accountName);
		}
		Integer status = getStatus();
		if (status!=null) {
			attributesMap.put("status", status.toString());
		}
		Integer costCenter = getCostCenter();
		if (costCenter!=null) {
			attributesMap.put("costCenter", costCenter.toString());
		}
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);
		}
		String moreProps = getMoreProps();
		if (moreProps!=null && !"".equals(moreProps)) {
			attributesMap.put("moreProps", moreProps);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialize the labelBean 
	 * @param attributes
	 * @return
	 */
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TAccountBean accountBean = new TAccountBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			accountBean.setObjectID(new Integer(strObjectID));
		}		
		accountBean.setAccountNumber(attributes.get("accountNumber"));
		accountBean.setAccountName(attributes.get("accountName"));		
		String strStatus = attributes.get("status");
		if (strStatus!=null) {
			accountBean.setStatus(new Integer(strStatus));
		}
		String strCostCenter = attributes.get("costCenter");
		if (strCostCenter!=null) {
			accountBean.setCostCenter(new Integer(strCostCenter));
		}		
		accountBean.setDescription(attributes.get("description"));
		accountBean.setMoreProps(attributes.get("moreProps"));
		accountBean.setUuid(attributes.get("uuid"));
		return accountBean;
	}
	
	
	/**
	 * Whether two label beans are equivalent  
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TAccountBean accountBean = (TAccountBean) serializableLabelBean;
		String externalAccountNumber = getAccountNumber();
		String internalAccountNumber = accountBean.getAccountNumber();
		Integer externalCostcenter = getCostCenter();
		Integer internalCostcenter = accountBean.getCostCenter();
		//an account matches only if the costcenter matches and the  label matches 
		Map<Integer, Integer> costCenterMatches = matchesMap.get(ExchangeFieldNames.COSTCENTER); 
		if (costCenterMatches!=null && costCenterMatches.get(externalCostcenter)!=null && 
				externalAccountNumber!=null && internalAccountNumber!=null) {
			return costCenterMatches.get(externalCostcenter).equals(internalCostcenter)  &&
				externalAccountNumber.equals(internalAccountNumber);
		}		
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		TAccountBean accountBean = (TAccountBean)serializableLabelBean;
		Integer accountStatus = accountBean.getStatus();
		if (accountStatus!=null) {
			Map<Integer, Integer> systemStatusMap = 
				matchesMap.get(ExchangeFieldNames.SYSTEMSTATE);
			accountBean.setStatus(systemStatusMap.get(accountStatus));
		}
		Integer costCenter = accountBean.getCostCenter();
		if (costCenter!=null) {
			Map<Integer, Integer> costCentersMap = matchesMap.get(ExchangeFieldNames.COSTCENTER);
			accountBean.setCostCenter(costCentersMap.get(costCenter));
		}
		return AccountBL.saveAccount(accountBean);
	}
}
