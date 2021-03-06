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

package com.aurel.track.fieldType.runtime.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;

/**
 * 
 * A renderer for cascade select at runTime
 *
 */
public class SelectParentChildGrandchildRendererRT extends CompositeTypeRendererRT {
	//singleton isntance
	private static SelectParentChildGrandchildRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static SelectParentChildGrandchildRendererRT getInstance() {
		if (instance == null) {
			instance = new SelectParentChildGrandchildRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public SelectParentChildGrandchildRendererRT() {
	}
	@Override
	public int getNumberOfParts() {
		return 3;
	}
	@Override
	public boolean isCascading(){
		return true;
	}
	@Override
	public TypeRendererRT getCustomTypeRenderer(int index) {
		switch(index){
		case 1:
			return CustomSelectRendererRT.getInstance();
		case 2:
			return CustomSelectRendererRT.getInstance();
		case 3:
			return CustomSelectRendererRT.getInstance();
		}
		return null;
	}
	
	protected void updateCustomPropertiesParameter(Map map,TFieldBean fieldBean,WorkItemContext workItemContext,int index){
		List list=workItemContext.getDropDownContainer().getDataSourceList(MergeUtil.mergeKey(fieldBean.getObjectID(),new Integer(index)));
		Integer fieldID = fieldBean.getObjectID();
		if (list==null) {
			list=new ArrayList();
		}
		//set the hasHeader flag depending on the fieldConfigBean
		TFieldConfigBean fieldConfigBean = null;
		//implicitely no header -> means required
		Boolean hasHeader = new Boolean(false);
		Map fieldConfigs = workItemContext.getFieldConfigs();
		if (fieldConfigs!=null && fieldConfigs.get(fieldID)!=null) {
			fieldConfigBean = (TFieldConfigBean)fieldConfigs.get(fieldID);
			if (!fieldConfigBean.isRequiredString()) {
				//set header if it is not required  
				hasHeader = new Boolean(true);
			}
		}		
		map.put("hasHeader", hasHeader);
		
		map.put("list"+index, list);
		String listKey="objectID";
		String listValue="label";
		String nameMapping="integerArrayMap.f"+fieldID+TWorkItemBean.LINKCHAR;
		String jsReload="";
		
		if(index==1){
			String parentArray = "new Array('1','2')";
			String childArray = "new Array('2','3')";
			jsReload="cascadeSelectChange(" + fieldID + ", " + parentArray + ", " + childArray + 
				", '" + nameMapping + "', " + hasHeader.booleanValue() + ")";						
		}					
		if(index==2){
			String parentArray = "new Array('2')";
			String childArray = "new Array('3')";
			jsReload="cascadeSelectChange(" + fieldID + ", " + parentArray + ", " + childArray + 
				", '" + nameMapping + "', " + hasHeader.booleanValue() + ")";
		}
		map.put("listKey", listKey);
		map.put("listValue", listValue);
		map.put("jsReload"+index, jsReload);
		
		map.put("nameMapping",nameMapping);
				
	}
}

