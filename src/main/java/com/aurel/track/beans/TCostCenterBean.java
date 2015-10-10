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

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TCostCenterBean
	extends com.aurel.track.beans.base.BaseTCostCenterBean
	implements Serializable, ISerializableLabelBean
{

	private static final long serialVersionUID = 1L;
	public String getLabel() {
		String number = getCostcenterNumber();
		String name = getCostcenterName();
		String label;
		if (number==null) {
			number = "";
		}
		if (name==null || "".equals(name)) {
			label = number;
		} else {
			label = number + " - " + name;
		}
		return label;
	}
	
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("costcenterNumber", getCostcenterNumber());
		String costcenterName = getCostcenterName();
		if (costcenterName!=null && !"".equals(costcenterName)) {
			attributesMap.put("costcenterName", costcenterName);
		}
		String moreProps = getMoreProps();
		if (moreProps!=null && !"".equals(moreProps)) {
			attributesMap.put("moreProps", moreProps);
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
	
	/**
	 * Deserialze the labelBean 
	 * @param attributes
	 * @return
	 */
	public  ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TCostCenterBean costCenterBean = new TCostCenterBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			costCenterBean.setObjectID(new Integer(strObjectID));
		}
		costCenterBean.setCostcenterNumber(attributes.get("costcenterNumber"));
		costCenterBean.setCostcenterName(attributes.get("costcenterName"));
		costCenterBean.setMoreProps(attributes.get("moreProps"));
		costCenterBean.setUuid(attributes.get("uuid"));
		return costCenterBean;
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
		TCostCenterBean costcenterBean = (TCostCenterBean) serializableLabelBean;
		String externalCostcenterNumber = getCostcenterNumber();
		String internalCostcenterNumber = costcenterBean.getCostcenterNumber();
		if (externalCostcenterNumber!=null && internalCostcenterNumber!=null) {
			return externalCostcenterNumber.equals(internalCostcenterNumber);
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
		return AccountBL.saveCostCenter((TCostCenterBean)serializableLabelBean);
	}
}
