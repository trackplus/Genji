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
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.user.department.DepartmentBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TDepartmentBean
    extends com.aurel.track.beans.base.BaseTDepartmentBean
    implements Serializable, ISerializableLabelBean
{	
	private static final long serialVersionUID = 1L;
	/**
	 * Serialize a label bean to a dom element
	 * @param labelBean
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("label", getLabel());
		Integer costCenter = getCostcenter();
		if (costCenter!=null) {
			attributesMap.put("costCenter", costCenter.toString());
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
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TDepartmentBean departmentBean = new TDepartmentBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			departmentBean.setObjectID(new Integer(strObjectID));
		}
		departmentBean.setLabel(attributes.get("label"));
		String strCostCenter = attributes.get("costCenter");
		if (strCostCenter!=null) {
			departmentBean.setCostcenter(new Integer(strCostCenter));
		}
		departmentBean.setUuid(attributes.get("uuid"));
		return departmentBean;
	}
	
	/**
	 * Whether two label beans are equivalent
	 * @param serializableLabelBean 
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TDepartmentBean departmentBean = (TDepartmentBean) serializableLabelBean;
		String externLabel = getLabel();
		String internLabel = departmentBean.getLabel();
		if (externLabel!=null && internLabel!=null) {
			return externLabel.equals(internLabel);
		}
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		TDepartmentBean departmentBean = (TDepartmentBean)serializableLabelBean;
		Integer costCenter = departmentBean.getCostcenter();
		if (costCenter!=null) {
			Map<Integer, Integer> costCentersMap = matchesMap.get(ExchangeFieldNames.COSTCENTER);
			departmentBean.setCostcenter(costCentersMap.get(costCenter));
		}
		return DepartmentBL.save(departmentBean);
	}
}
