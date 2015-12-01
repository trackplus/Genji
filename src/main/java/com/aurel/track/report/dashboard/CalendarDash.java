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

package com.aurel.track.report.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.screen.dashboard.bl.runtime.DashboardFieldRuntimeBL;
import com.aurel.track.util.LabelValueBean;


/**
 * This class provides the dashboard calendar with the needed parameters of week view and 
 * @author Andreas Meister
 *
 */

public class CalendarDash extends BasePluginDashboardView{

	private static final String MARKED_DAY="markedDay";
	private static final String LINK_NO="linkNo";
	private static final String CURRENT_DAY="currentDay";
	public static final String TODAY="today";
	public static final String WEEK="week";
	public static final String MONTH="month";

	@Override
	public Map<String,String> convertMapFromPageConfig(Map<String,String> properties, TPersonBean user,Locale locale,Integer entityId, Integer entityType,List<LabelValueBean> errors) {
		Map<String,String> map=super.convertMapFromPageConfig(properties, user, locale,entityId,entityType, errors);
		DataSourceDashboardBL.validate(properties,user.getObjectID(),locale,errors);
		return map;
	}

	@Override
	protected boolean isUseConfig(){
		return true;
	}

	@Override
	protected String encodeJSONExtraData(Integer dashboardID,Map<String, Object> session, Map<String, String> parameters,
										 Integer projectID, Integer releaseID,Map<String,String> ajaxParams) throws TooManyItemsToLoadException{
		TPersonBean user = (TPersonBean) session.get(Constants.USER_KEY);
		Locale locale = user.getLocale();// get locale from TPersonBean

		StringBuilder sb=new StringBuilder();

		Calendar calendarToday =getCleanTodayCalendar();
		Date today = calendarToday.getTime();
		//put today
		JSONUtility.appendStringValue(sb,TODAY, Long.toString(today.getTime()));
		// check if today is weekend
		if(calendarToday.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || calendarToday.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
			JSONUtility.appendStringValue(sb,"todayIsWE", "1");
		}
		else {
			JSONUtility.appendStringValue(sb,"todayIsWE", "0");
		}

		int linkNo=0;
		long currentDayAjax=0;
		if(ajaxParams!=null){
			linkNo = parseInt(ajaxParams.get(LINK_NO));
			currentDayAjax=parseLong(ajaxParams.get(CURRENT_DAY));
		}
		String view=CalendarDashBL.getViewMode(linkNo,parameters.get("view"));
		long currentDay=CalendarDashBL.getCurrentDay(linkNo,currentDayAjax);
		JSONUtility.appendStringValue(sb,CURRENT_DAY, Long.toString(currentDay));

		if (WEEK.equals(view)) {
			CalendarDashBL.Week week=CalendarDashBL.getWeek(parameters,currentDay,user,locale,projectID,releaseID);
			JSONUtility.appendJSONValue(sb,"week",CalendarDashBL.encodeJSONWeek(week,locale));
		}
		else {
			CalendarDashBL.Month month=CalendarDashBL.getMonth(parameters,currentDay,user,locale,projectID,releaseID);
			JSONUtility.appendJSONValue(sb,"month",CalendarDashBL.encodeJSONMonth(currentDay, month,locale));
		}
		JSONUtility.appendStringValue(sb,"calendarView", view);
		parameters.put("view",view);
		TDashboardFieldBean dashboardItem=(TDashboardFieldBean)DashboardFieldRuntimeBL.getInstance().loadField(dashboardID);
		dashboardItem.setParametres(parameters);
		DashboardFieldRuntimeBL.getInstance().saveDashBoardField(dashboardItem);
		JSONUtility.appendStringValue(sb,CURRENT_DAY, Long.toString(currentDay));
		return sb.toString();
	}

	@Override
	public String encodeJSONExtraDataConfig(Map<String,String> parameters, TPersonBean user, Integer entityId, Integer entityType){
		StringBuilder sb=new StringBuilder();

		DashboardDescriptor dashboardDescriptor = getDescriptor();
		DataSourceDashboardBL.appendJSONExtraDataConfig_DataSource(sb, dashboardDescriptor, parameters, user, entityId, entityType);
		String view=parameters.get("view");
		if(view==null){
			view=MONTH;
		}
		JSONUtility.appendStringValue(sb,"view", view);

		int prefWidth=500;
		int prefHeight=250;
		JSONUtility.appendIntegerValue(sb,"prefWidth",prefWidth);
		JSONUtility.appendIntegerValue(sb,"prefHeight",prefHeight);

		return  sb.toString();
	}





	private long parseLong(String s){
		if(s==null||s.length()==0){
			return 0;
		}
		try{
			return Long.parseLong(s);
		}catch (Exception e) {
			return 0;
		}
	}
	private int parseInt(String s){
		if(s==null||s.length()==0){
			return 0;
		}
		try{
			return Integer.parseInt(s);
		}catch (Exception e) {
			return 0;
		}
	}
	private Calendar getCleanTodayCalendar(){
		Calendar calendar =Calendar.getInstance();// get calendar-instance
		//clean calendar of hourOfDay, minute, second, milliseconds
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
}
