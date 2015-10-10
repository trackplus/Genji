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

package com.aurel.track.item.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorHandlerJSONAdapter;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.ItemBL;
import com.aurel.track.itemNavigator.ItemNavigatorFilterBL;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;

/*
 */
public class ItemOperationScheduling implements ItemOperation{


	private String pluginID;
	private String name;
 
	public static interface ICON_CLS {
		//general schedule icon
		static String SCHEDULE_ICON = "schedule-ticon";
		//period specific schedule icons
		static String SCHEDULE_TODAY = "schedule_today-ticon";
		static String SCHEDULE_TOMORROW = "schedule_tomorrow-ticon";
		static String SCHEDULE_NEXT_WEEK = "schedule_nextWeek-ticon";
		static String SCHEDULE_NEXT_MONTH = "schedule_nextMonth-ticon";
		static String NOT_SCHEDULED = "schedule_notScheduled-ticon";
		static String OVERDUE = "schedule_overdue-ticon";
	}
	
	public ItemOperationScheduling(Locale locale){
		this.pluginID="ItemOperationScheduling";
		this.name= LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.scheduled", locale);
	}
	public String getPluginID() {
		return pluginID;
	}
	public String getName() {
		return name;
	}

	public String getIcon() {
		return null;
	}

	public String getIconCls() {
		return ICON_CLS.SCHEDULE_ICON;
	}

	public List<MenuItem> getChildren(TPersonBean personBean, Locale locale, Integer nodeID) {
		List<MenuItem> menuItems=new ArrayList<MenuItem>();
		String type=ItemNavigatorFilterBL.NODE_TYPE_PREFIX.ITEM_OPERATION+ItemNavigatorFilterBL.NODE_TYPE_SEPARATOR+pluginID;
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.scheduled.today", locale),
				"schedule_today-ticon"));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.scheduled.tomorrow", locale),
				"schedule_tomorrow-ticon"));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_WEEK,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.scheduled.nextWeek", locale),
				"schedule_nextWeek-ticon"));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_MONTH,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.scheduled.nextMonth", locale),
				"schedule_nextMonth-ticon"));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.scheduled.notScheduled", locale),
				"schedule_notScheduled-ticon"));
		menuItems.add(new MenuItem(type,ItemNavigatorFilterBL.SCHEDULE_TYPE.OVERDUE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("itemov.lbl.scheduled.overdue", locale),
				"schedule_overdue-ticon",true,false));
		return menuItems;
	}

	public void execute(int[] workItemIds, Integer nodeObjectID, Map<String, String> params, Integer personID, Locale locale) throws ItemOperationException {
		for(int i=0;i<workItemIds.length;i++){
			WorkItemContext ctx= ItemBL.editWorkItem(workItemIds[i], personID, locale,true);
			TWorkItemBean workItemBean=ctx.getWorkItemBean();
			Date startDate=workItemBean.getStartDate();
			Date endDate=workItemBean.getEndDate();
			switch (nodeObjectID){
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY:{
					startDate=DateTimeUtils.getToday();
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW:{
					startDate=DateTimeUtils.getTomorrow();
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_WEEK:{
					startDate=DateTimeUtils.getStartOfNextWeek();
					endDate=DateTimeUtils.getEndOfNextWeek();
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_MONTH:{
					startDate=DateTimeUtils.getStartOfNextMonth();
					endDate=DateTimeUtils.getEndOfNextMonth();
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED:{
					startDate=null;
					endDate=null;
					break;
				}
				default:
					break;
			}
			if(endDate!=null&&endDate.before(startDate)){
				endDate=startDate;
			}
			workItemBean.setStartDate(startDate);
			workItemBean.setEndDate(endDate);
			List errorList=new ArrayList();
			ItemBL.saveWorkItem(ctx, errorList, false);
			if(!errorList.isEmpty()){
				String message=ErrorHandlerJSONAdapter.handleErrorListAsString(errorList, locale, ";");
				throw new ItemOperationException(message);
			}
		}
	}

	public boolean canDrop(int[] workItemIds, Integer nodeObjectID) {
		boolean result=false;
		switch (nodeObjectID){
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY:
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW:
			case ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED:{
				result=true;
				break;
			}
			default:
				result=false;
				break;
		}
		return result;
	}

	public List<ReportBean> filter(List<ReportBean> reportBeans, Integer nodeObjectID){
		List<ReportBean> result=new ArrayList<ReportBean>();
		TWorkItemBean workItemBean=null;
		ReportBean reportBean;
		for(int i=0;i<reportBeans.size();i++){
			reportBean=reportBeans.get(i);
			workItemBean=reportBean.getWorkItemBean();
			Date startDate= workItemBean.getStartDate();
			if(startDate!=null){
				startDate=DateTimeUtils.clearDate(startDate);
			}
			Date endDate= workItemBean.getEndDate();
			if(endDate!=null){
				endDate=DateTimeUtils.clearDate(endDate);
			}
			switch (nodeObjectID){
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.TODAY:{
					if(startDate!=null&&startDate.getTime()==DateTimeUtils.getToday().getTime()){
						result.add(reportBean);
					}
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.TOMORROW:{
					if(startDate!=null&&startDate.getTime()==DateTimeUtils.getTomorrow().getTime()){
						result.add(reportBean);
					}
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_WEEK:{
					Date startOfNextWeek= DateTimeUtils.getStartOfNextWeek();
					Date endOfNextWeek= DateTimeUtils.getEndOfNextWeek();
					if(startDate!=null&&
							(startDate.equals(startOfNextWeek)||startDate.after(startOfNextWeek))&&
							(startDate.equals(endOfNextWeek)|| startDate.before(endOfNextWeek))){
						result.add(reportBean);
					}
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.NEXT_MONTH:{
					Date startOfNextMonth= DateTimeUtils.getStartOfNextMonth();
					Date endOfNextMonth= DateTimeUtils.getEndOfNextMonth();
					if(startDate!=null&&
						(startDate.equals(startOfNextMonth)||startDate.after(startOfNextMonth))&&
						(startDate.equals(endOfNextMonth)||startDate.before(endOfNextMonth))){
						result.add(reportBean);
					}
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.NOT_SCHEDULED:{
					if(startDate==null&&endDate==null){
						result.add(reportBean);
					}
					break;
				}
				case ItemNavigatorFilterBL.SCHEDULE_TYPE.OVERDUE:{
					if(endDate!=null&&endDate.getTime()<DateTimeUtils.getToday().getTime()){
						result.add(reportBean);
					}
					break;
				}
				default:
					break;
			}
		}
		return result;
	}
}
