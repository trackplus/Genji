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

package com.aurel.track.item.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.item.massOperation.MassOperationBL;
import com.aurel.track.item.massOperation.MassOperationException;
import com.aurel.track.itemNavigator.ItemNavigatorFilterBL;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;

/**
 *
 */
public abstract class ItemOperationSelect implements ItemOperation{
	
	private static final Logger LOGGER = LogManager.getLogger(ItemOperationSelect.class);
	
	protected String pluginID;
	private Integer fieldID;
	protected boolean allowDrop=true;
	private boolean  useFilter=true;
	boolean tree=false;

	protected String name;
	protected String iconName;

	public ItemOperationSelect(Integer fieldID,boolean allowDrop,boolean useFilter,boolean tree,Locale locale){
		this.pluginID="ItemOperationSelect_"+fieldID;
		this.fieldID=fieldID;
		this.allowDrop=allowDrop;
		this.useFilter=useFilter;
		this.tree=tree;
		this.name= LocalizeUtil.getLocalizedEntity(LocalizationKeyPrefixes.FIELD_LABEL_KEY_PREFIX, fieldID, locale);
	}
	public ItemOperationSelect(Integer fieldID,Locale locale){
		this(fieldID,true,true,false,locale);
		
	}
	@Override
	public String getPluginID() {
		return pluginID;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIcon() {
		/*if(iconName!=null){
			return IconPathHelper.getListIconPathForURL(iconName,contextPath);
		}*/
		return null;
	}
	@Override
	public String getIconCls() {
		return iconName;
	}
	protected abstract List<ILabelBean> getOptionsInternal(TPersonBean personBean,Locale locale,Integer nodeID);
	protected int findLevel(Integer nodeID){
		if(nodeID==null){
			return 1;
		}
		return 2;
	}
	@Override
	public List<MenuItem> getChildren(TPersonBean personBean, Locale locale, Integer nodeID) {
		List<MenuItem> menuItems=new ArrayList<MenuItem>();
		List<ILabelBean> currentOptions=getOptionsInternal(personBean,locale,nodeID);
		for (int i = 0; i < currentOptions.size(); i++) {
			ILabelBean labelBean =  currentOptions.get(i);
			MenuItem mit=new MenuItem();
			mit.setType(ItemNavigatorFilterBL.NODE_TYPE_PREFIX.ITEM_OPERATION+ItemNavigatorFilterBL.NODE_TYPE_SEPARATOR+pluginID);
			mit.setObjectID(labelBean.getObjectID());
			mit.setName(labelBean.getLabel());
			mit.setAllowDrop(getAllowDrop(labelBean));
			mit.setUseFilter(useFilter);
			mit.setLazyChildren(tree);
			mit.setLevel(findLevel(nodeID));
			mit.setId(pluginID+".item_"+labelBean.getObjectID());
			String iconName = null;
			try{
				iconName=getSymbol(labelBean);
			}catch (Exception e){
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			if(iconName!=null){
				//mit.setIcon(IconPathHelper.getListOptionIconPathForURL(iconName,listID, contextPath));				
				mit.setIcon("itemNavigator!serveIconStream.action?nodeType=" + pluginID + "&nodeObjectID=" + labelBean.getObjectID() + "&time="+new Date().getTime());
			}
			String iconCls = null;
			try{
				iconCls=getIconCls(labelBean);
			}catch (Exception e){
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
			if(iconCls!=null){
				//mit.setIcon(IconPathHelper.getListOptionIconPathForURL(iconName,listID, contextPath));
				mit.setIconCls(iconCls);
			}
			menuItems.add(mit);
		}
		return menuItems;
	}
	protected boolean getAllowDrop(ILabelBean labelBean){
		return allowDrop;
	}
	protected abstract String getSymbol(ILabelBean labelBean);
	protected String getIconCls(ILabelBean labelBean){
		return null;
	}

	@Override
	public void execute(int[] workItemIds, Integer nodeObjectID, Map<String, String> params, Integer personID, Locale locale) throws ItemOperationException {
		executeInternal(fieldID,workItemIds,nodeObjectID,params,personID,locale);
	}
	protected void executeInternal(Integer fieldID,int[] workItemIds, Object value, Map<String, String> params, Integer personID, Locale locale) throws ItemOperationException {
		boolean confirmSave=false;
		String confirmSaveStr=null;
		if(params!=null){
			confirmSaveStr=params.get("confirmSave");
		}
		if(confirmSaveStr!=null&&confirmSaveStr.equalsIgnoreCase("true")){
			confirmSave=true;
		}
		try{
			MassOperationBL.saveExtern(workItemIds,fieldID,value,personID,locale,confirmSave);
		}catch (MassOperationException ex){
			throw  new ItemOperationException(ItemOperationException.TYPE_MASS_OPERATION, ex);
		}
	}

	@Override
	public boolean canDrop(int[] workItemIds, Integer nodeObjectID) {
		//Object fieldValue= workItemIds.getAttribute(fieldID);
		if(nodeObjectID==null){
			//if null no filter apply
			return false;
		}
		return true;//!nodeObjectID.equals(fieldValue);
	}

	@Override
	public List<ReportBean> filter(List<ReportBean> reportBeans, Integer nodeObjectID){
		List<ReportBean> result=new ArrayList<ReportBean>();
		if(nodeObjectID==null){
			result.addAll(reportBeans);
		}else{
			ReportBean reportBean;
			TWorkItemBean workItemBean;
			for(int i=0;i<reportBeans.size();i++){
				reportBean=reportBeans.get(i);
				workItemBean=reportBean.getWorkItemBean();
				Object fieldValue= workItemBean.getAttribute(fieldID);
				if(nodeObjectID.equals(fieldValue)){
					result.add(reportBean);
				}
			}
		}
		return result;
	}
}
