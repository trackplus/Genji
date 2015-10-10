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

package com.aurel.track.fieldType.runtime.renderer;

import java.util.List;
import java.util.Map;

import com.aurel.track.admin.customize.lists.customOption.CustomOptionSimpleSelectBL;
import com.aurel.track.admin.customize.lists.customOption.OptionBL;
import com.aurel.track.admin.customize.treeConfig.field.OptionSettingsBL;
import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.beans.TOptionSettingsBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.OptionSettingsDAO;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;

/**
 * 
 * A renderer for editable select at runTime
 * As renderer it is a composite field, but as fieldTypeRT it is not a composite
 *
 */
public class ExtensibleSelectRendererRT  extends SelectRendererRT {

	
	//singleton isntance
	private static ExtensibleSelectRendererRT instance;

	/**
	 * get a singleton instance
	 * @return singleton instance
	 */
	public static ExtensibleSelectRendererRT getInstance() {
		if (instance == null) {
			instance = new ExtensibleSelectRendererRT();
		}
		return instance;
	}
	private ExtensibleSelectRendererRT(){
	}
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.ExtensibleSelectTypeRenderer";
	}
	@Override
	public String encodeJsonValue(Object value){
		Object selection=null;
		if(value!=null){
			Object[] arr=(Object[])value;
			if(arr.length>0){
				selection=arr[0];
			}
		}
		return selection==null?null: selection.toString();
	}

	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Integer result=null;
		if(value!=null){
			if(value.startsWith("'")){
				//if the value id like 'some value' then we need to add a new option
				Integer newOption=insertValue(fieldID,value.substring(1,value.length()-1),workItemContext);
				if(newOption==null){
					throw new TypeConversionException("Can't create option");
				}
				return new Object[]{newOption};
			}
			try{
				result=Integer.decode(value);
			}catch (NumberFormatException ex){
				throw new TypeConversionException("common.err.invalid.number",ex);
			}
		}
		if(result==null){
			return null;
		}else{
			return new Object[]{result};
		}
	}

	private Integer insertValue(Integer fieldID,String value,WorkItemContext ctx){
		Map<Integer, TFieldConfigBean> fieldConfigs = ctx.getFieldConfigs();
		TFieldConfigBean validConfig = (TFieldConfigBean)fieldConfigs.get(fieldID);
		TOptionSettingsBean optionSettingsBean = OptionSettingsBL.loadByConfigAndParameter(validConfig.getObjectID(), null);
		if (optionSettingsBean!=null) {
			Integer listID = optionSettingsBean.getList();
			if (listID!=null) {
				TOptionBean optionBean = null;
				List<TOptionBean> optionBeans = OptionBL.loadByLabel(listID, null, value);
				if (optionBeans!=null && !optionBeans.isEmpty()) {
					optionBean = optionBeans.get(0);
				}
				if (optionBean!=null) {
					return optionBean.getObjectID();
				} else {
					//the label is new and
					optionBean = new TOptionBean();
					optionBean.setList(listID);
					optionBean.setLabel(value);
					//optionBean.setSortOrder(optionDAO.getNextSortOrder(optionBean.getList()));
					Integer optionID = CustomOptionSimpleSelectBL.getInstance().save(optionBean, false, ctx.getPerson(), ctx.getLocale());
					return optionID;
				}
			}
		}
		return null;
	}

}

