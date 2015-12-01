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

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.StringArrayParameterUtils;

/**
 * 
 * A renderer for combo at designTime
 *
 */
public class SelectMultipleRendererRT extends AbstractTypeRendererRT {
	//singleton isntance
	private static SelectMultipleRendererRT instance;

	/**
	 * get a singleton instance
	 * @return singleton instance
	 */
	public static SelectMultipleRendererRT getInstance() {
		if (instance == null) {
			instance = new SelectMultipleRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public SelectMultipleRendererRT() {
	}

	@Override
	public String getExtClassName(){
		return  "com.aurel.trackplus.field.MultipleSelectPickerRenderer";
	}
	@Override
	public String encodeJsonValue(Object value){
		return value==null?null:JSONUtility.encodeObjectArrayAsArray((Object[]) value);
	}

	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Object[] result=null;
		if(value!=null){
			List<Integer> integerList=StringArrayParameterUtils.splitSelectionAsInteger(value);
			result=new Object[integerList.size()];
			for(int i=0;i< integerList.size();i++){
				Integer intValue=integerList.get(i);
				result[i]=intValue;
			}
		}
		return result;
	}

	@Override
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		String jsReload=getOnChangeJS();
		JSONUtility.appendStringValue(sb,"jsReload",jsReload);
		Integer fieldID=field.getObjectID();
		Boolean hasHeader = hasHeader(workItemContext, fieldID);
		JSONUtility.appendBooleanValue(sb, "hasHeader", hasHeader);
		List possibleValues=getPossibleValues(field, workItemContext);
		List<IntegerStringBean> options=conevertPossibleValues(possibleValues);				
		JSONUtility.appendIntegerStringBeanList(sb, "list", options, true);
		sb.append("}");
		return sb.toString();
	}


	protected List getPossibleValues(TFieldBean field,WorkItemContext workItemContext){
		return workItemContext.getDropDownContainer().getDataSourceList(MergeUtil.mergeKey(field.getObjectID(), null));
	}
	protected List<IntegerStringBean> conevertPossibleValues(List list){
		List<IntegerStringBean> result=new ArrayList<IntegerStringBean>();
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Object bean=list.get(i);
				if(bean instanceof IntegerStringBean){
					result.add((IntegerStringBean)bean);
				}else if(bean instanceof ILabelBean){
					ILabelBean labelBean=(ILabelBean)bean;
					result.add(new IntegerStringBean(labelBean.getLabel(),labelBean.getObjectID()));
				}else{
					//FIXME implement me!
					System.out.println("! class:"+bean.getClass());
				}
			}
		}
		return result;

	}
	private Boolean hasHeader(WorkItemContext workItemContext, Integer fieldID) {
		Boolean hasHeader = new Boolean(false);
		TFieldConfigBean fieldConfigBean = null;
		Map fieldConfigs = workItemContext.getFieldConfigs();
		if (fieldConfigs!=null && fieldConfigs.get(fieldID)!=null) {
			fieldConfigBean = (TFieldConfigBean)fieldConfigs.get(fieldID);
			if (!BooleanFields.TRUE_VALUE.equals(fieldConfigBean.getRequired())) {
				//set header if it is not required
				hasHeader = new Boolean(true);
			}
		}
		return hasHeader;
	}
	protected String getOnChangeJS(){
		return "javascript:doReload()";
	}
}
