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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.filter.execute.TreeFilterExecuterFacade;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.emailHandling.Html2Text;

/**
 */
public class CalendarDashBL {

	private static final Logger LOGGER = LogManager.getLogger(CalendarDashBL.class);

	public static Week getWeek(Map<String,String> params,long currentDay,TPersonBean user,Locale locale,Integer projectID,Integer releaseID) throws TooManyItemsToLoadException{
		Week week=new Week();
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(currentDay);
		Integer weekOfYear=calendar.get(Calendar.WEEK_OF_YEAR);
		week.setWeekOfYear(weekOfYear);
		Date[] days=getWeekDays(currentDay);
		Date startDate=days[0];
		Date endDate=days[days.length-1];
		List<CalendarIssue> issues = dbAccess(params, user,locale,startDate,endDate,projectID,releaseID);
		CalendarDay[] calendarDays=new CalendarDay[days.length];
		for (int i=0;i<calendarDays.length;i++){
			CalendarDay cd=new CalendarDay();
			cd.setDate(days[i]);
			cd.setIssues(filterIssues(issues, days[i]));
			calendarDays[i]=cd;
		}
		week.setDays(calendarDays);
		return week;
	}
	private static List<CalendarIssue> filterIssues(List<CalendarIssue> issues,Date date){
		List<CalendarIssue> result=new ArrayList<CalendarIssue>();
		long currentDate=date.getTime();
		long startDate=0;
		long endDate=0;
		long topDownStartDate=0;
		long topDownEndDate=0;
		CalendarIssue issue=null;
 		if(issues!=null && !issues.isEmpty()){
			for (int i=0;i<issues.size();i++){
				issue=issues.get(i);
				startDate=0;
				if(issue.getStartDate()!=null){
					startDate=issue.getStartDate().getTime();
				}
				endDate=0;
				if(issue.getEndDate()!=null){
					endDate=issue.getEndDate().getTime();
				}
				topDownStartDate=0;
				if(issue.getTopDownStartDate()!=null){
					topDownStartDate=issue.getTopDownStartDate().getTime();
				}
				topDownEndDate=0;
				if(issue.getTopDownEndDate()!=null){
					topDownEndDate=issue.getTopDownEndDate().getTime();
				}
				if(startDate==currentDate||endDate==currentDate||
						topDownStartDate==currentDate||topDownEndDate==currentDate){
					result.add(issue);
				}
			}
		}
		return result;
	}

	private static Date[] getWeekDays(long currentDay){
		Date[] result=new Date[7];
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(currentDay);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);

		// generate dates of week
		for (int i=0; i<7; i++) {
			// save formated date to map
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY+i);
			result[i]=calendar.getTime();
		}
		return result;
	}
	private static void printWeek(Date[] week){
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
		System.out.println("Mo---- Tu---- We---- Th---- Fr---- Sa---- Su-----");
		for (int i = 0; i < week.length; i++) {
			System.out.print(sdf.format(week[i])+" ");
		}
		System.out.println("\n--------------------------------------------------");
	}
	
	
	public static String encodeJSONWeek(Week week,Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		//Formatter
		String pattern = new String();						// String for format pattern
		String fm = new String();							//String to save formatted dates
		// set string format pattern depending on Locale
		pattern = "E";  // just the day of the week (Mo, Tu, Wed, ...)

		SimpleDateFormat formatterDOW = new SimpleDateFormat(pattern , locale); // get SimpleDateFormat-instance to generate formatted dates
		SimpleDateFormat formatterDOM = new SimpleDateFormat("d", locale);
		CalendarDay[] weekDays=week.getDays();
		
		String fromPattern = "";
		String toPattern = "";
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		
		String from = df.format(weekDays[0].getDate());
		String to = df.format(weekDays[6].getDate());		
		
		JSONUtility.appendIntegerValue(sb,"weekOfYear",week.getWeekOfYear());
		JSONUtility.appendStringValue(sb,"fromTo",from+"&nbsp;&ndash;&nbsp;"+to);
		sb.append("\"days\":[");
		for (int i=0; i<weekDays.length; i++) {
			sb.append(encodeJSONDay(weekDays[i], formatterDOW, formatterDOM ));
			if(i<weekDays.length-1){
				sb.append(",");
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
	
	
	public static  Month getMonth(Map params,long currentDay,TPersonBean user,Locale locale,Integer projectID,Integer releaseID) throws TooManyItemsToLoadException{
		Month month=new Month();


		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(currentDay);

		// save last year and next year to map
		Integer lastYear =calendar.get(Calendar.YEAR)-1;
		Integer nextYear=calendar.get(Calendar.YEAR)+1;
		Integer currentMonth=calendar.get(Calendar.MONTH);
		month.setCurrentMonth(currentMonth);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		long firstDay=calendar.getTimeInMillis();
		month.setFirstDay(firstDay);
		calendar.set(Calendar.MONTH, currentMonth + 1);
		long lastDay=calendar.getTimeInMillis();
		month.setLastDay(lastDay);
		month.setLastYear(lastYear);
		month.setNextYear(nextYear);

		Date[][] days=getMonthDays(currentDay);
		Date startDate=days[0][0];
		Date endDate=days[days.length-1][days[0].length-1];
		List<CalendarIssue> issues = dbAccess(params, user,locale,startDate,endDate,projectID,releaseID);
		
		CalendarDay[][] calendarDays=new CalendarDay[days.length][days[0].length];
		for (int i=0;i<days.length;i++){
			for (int j=0;j<days[i].length;j++){
				CalendarDay cd=new CalendarDay();
				cd.setDate(days[i][j]);
				cd.setIssues(filterIssues(issues, days[i][j]));
				calendarDays[i][j]=cd;
			}
		}
		month.setDays(calendarDays);

		return month;
	}
	
	
	private static Date[][] getMonthDays(long currentDay){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(currentDay);
		ca.setMinimalDaysInFirstWeek(1);
		ca.setFirstDayOfWeek(Calendar.MONDAY);
		// get number of weeks and save it to map
		int numberWeeks =ca.getActualMaximum(Calendar.WEEK_OF_MONTH);
		// generate dates of month
		Date[][] result=new Date[numberWeeks][7];
		for (int i=1; i<=numberWeeks; i++){
			ca.set(Calendar.WEEK_OF_MONTH, i);
			for (int j=0; j<7; j++){
				ca.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY+j);
				result[i-1][j]=ca.getTime();
			}
		}
		return result;
	}

	public static String encodeJSONMonth(long currentDay,Month month,Locale locale){
		StringBuilder sb=new StringBuilder();
		
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"lastYear",month.getLastYear());
		JSONUtility.appendIntegerValue(sb,"nextYear",month.getNextYear());
		JSONUtility.appendStringValue(sb,"firstDay",Long.toString(month.getFirstDay()));
		JSONUtility.appendStringValue(sb,"lastDay",Long.toString(month.getLastDay()));
		JSONUtility.appendIntegerValue(sb,"currentMonth", month.getCurrentMonth());

		String datePattern = new String();// "dd. MMM";
		String headPattern = "MMMM yyyy";
		if (locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
			datePattern = "dd. MMM";
		}
		else {
			datePattern = "MMM dd";
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern , locale); 		// format string for dates
		SimpleDateFormat headFormatter = new SimpleDateFormat(headPattern, locale);// save headline format

		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(currentDay);
		JSONUtility.appendStringValue(sb,"header", headFormatter.format(calendar.getTime()));

		// save names of days to map
		String[] wd = new DateFormatSymbols(locale).getWeekdays();		// save long weekday names
		String[] wdShort = new DateFormatSymbols(locale).getShortWeekdays();// save short weekday names
		List<String> weekDays=new ArrayList<String>();
		weekDays.add(wd[Calendar.MONDAY]);
		weekDays.add(wd[Calendar.TUESDAY]);
		weekDays.add(wd[Calendar.WEDNESDAY]);
		weekDays.add(wd[Calendar.THURSDAY]);
		weekDays.add(wd[Calendar.FRIDAY]);
		weekDays.add(wdShort[Calendar.SATURDAY]);
		weekDays.add(wdShort[Calendar.SUNDAY]);
		JSONUtility.appendStringList(sb,"weekDays",weekDays);

		CalendarDay[][] monthDates=month.getDays();
		sb.append("\"days\":[");
		for (int i=0; i<monthDates.length; i++) {
			sb.append("[");
			for(int j=0;j<monthDates[i].length;j++){
				sb.append(encodeJSONDay(monthDates[i][j],formatter,null));
				if(j<monthDates[i].length-1){
					sb.append(",");
				}
			}
			sb.append("]") ;
			if(i<monthDates.length-1){
				sb.append(",");
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}

	public static String encodeJSONDay(CalendarDay day, SimpleDateFormat formatter, SimpleDateFormat dayFormatter){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendStringValue(sb, "label", formatter.format(day.getDate()));
		if (dayFormatter != null) {
			JSONUtility.appendStringValue(sb, "dom", dayFormatter.format(day.getDate()));
		}
		JSONUtility.appendJSONValue(sb,"issues",encodeCalendarIssues(day.getIssues()));
		JSONUtility.appendStringValue(sb, "value", Long.toString(day.getDate().getTime()), true);
		sb.append("}");
		return sb.toString();
	}

	public static String encodeCalendarIssues(List<CalendarIssue> issues){
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		if(issues!=null){
			for (Iterator<CalendarIssue> iterator = issues.iterator(); iterator.hasNext();) {
				CalendarIssue issue = iterator.next();
				sb.append(encodeCalendarIssue(issue));
				if (iterator.hasNext()) {
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static String encodeCalendarIssue(CalendarIssue issue){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(issue!=null){
			JSONUtility.appendIntegerValue(sb,"objectID",issue.getObjectID());
			if(issue.getStartDate()!=null){
				JSONUtility.appendStringValue(sb,"startDate",Long.toString(issue.getStartDate().getTime()));
			}
			if(issue.getEndDate()!=null){
				JSONUtility.appendStringValue(sb,"endDate",Long.toString(issue.getEndDate().getTime()));
			}
			if(issue.getTopDownStartDate()!=null){
				JSONUtility.appendStringValue(sb,"topDownStartDate",Long.toString(issue.getTopDownStartDate().getTime()));
			}
			if(issue.getTopDownEndDate()!=null){
				JSONUtility.appendStringValue(sb,"topDownEndDate",Long.toString(issue.getTopDownEndDate().getTime()));
			}

			JSONUtility.appendStringValue(sb, "title", issue.getTitle());
			JSONUtility.appendStringValue(sb, "tooltip", issue.getTooltip());
			JSONUtility.appendStringValue(sb,"cssColorClass",issue.getCssColorClass(),true);
		}
		sb.append("}");
		return sb.toString();
	}

	public static String getViewMode(int linkNo,String cfgView){
		String view=CalendarDash.MONTH;
		if(linkNo==0){
			view=cfgView;
			if(view==null){
				view=CalendarDash.MONTH;
			}
		}else{
			switch (linkNo){
				case 11: // todayButton in week view
				case 21:// last week button
				case 22:// next week button
				case 32: // weekButton in month view
					view=CalendarDash.WEEK;
					break;

				case 12: 	// monthButton in week view
				case 31: // todayButton in month view
				case 41:	//last month button
				case 42: 	//last year button
				case 43: 	//next year button
				case 44: 	//next month button
					view=CalendarDash.MONTH;
					break;
			}
		}
		return view;
	}
	
	public static long getCurrentDay(int linkNo,long currentDayAjax){
		Calendar currentDayCalendar =getCleanTodayCalendar();
		if(currentDayAjax!=0){
			currentDayCalendar.setTimeInMillis(currentDayAjax);
		}
		if(linkNo!=0){
			switch (linkNo){
				case 11: // todayButton in week view
					break;
				case 12: 	// monthButton in week view
					break;
				case 21:// last week button
					currentDayCalendar.add(Calendar.WEEK_OF_MONTH, -1);
					break;
				case 22:// next week button
					currentDayCalendar.add(Calendar.WEEK_OF_MONTH, 1);
					break;
				case 31: // todayButton in month view
				case 32: // weekButton in month view
					break;
				case 41:	//last month button
					currentDayCalendar.add(Calendar.MONTH, -1);
					break;
				case 42: 	//last year button
					currentDayCalendar.add(Calendar.MONTH, -12);
					break;
				case 43: 	//next year button
					currentDayCalendar.add(Calendar.MONTH, 12);
					break;
				case 44: 	//next month button
					currentDayCalendar.add(Calendar.MONTH, 1);
					break;
			}
		}
		long currentDay=currentDayCalendar.getTimeInMillis();
		return currentDay;
	}
	
	public static Calendar getCleanTodayCalendar(){
		Calendar calendar =Calendar.getInstance();// get calendar-instance
		//clean calendar of hourOfDay, minute, second, milliseconds
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}


	private static List<CalendarIssue> dbAccess(Map configParameters, TPersonBean personBean,Locale locale, Date startDate,Date endDate,Integer projectID,Integer releaseID) throws TooManyItemsToLoadException{
		LOGGER.debug("dbAccess for issues...");
		LOGGER.debug("startDate="+ DateTimeUtils.getInstance().formatISODate(startDate));
		LOGGER.debug("endDate="+DateTimeUtils.getInstance().formatISODate(endDate));
		LOGGER.debug("projectID="+projectID+", releaseID="+releaseID);
		List<CalendarIssue> result=new ArrayList<CalendarIssue>();
		TWorkItemBean workItemBean = new TWorkItemBean();// bean to get db entries
		List<TWorkItemBean> workItemBeansList = new ArrayList<TWorkItemBean>();	// list for all needet work items to show
		if(!DataSourceDashboardBL.haveDataSource(configParameters,personBean)&&projectID==null&&releaseID==null){
			LOGGER.debug("No dataSource!");
			workItemBeansList = TreeFilterExecuterFacade.getSavedFilterWorkItemBeans(PredefinedQueryBL.PREDEFINED_QUERY.MY_ITEMS, locale, personBean, new LinkedList<ErrorData>(), false); 
			//workItemBeansList=ReportBeanLoader.getMyWorkItemBeans(personBean.getObjectID(),null,false,startDate,endDate);
		}else{
			workItemBeansList=DataSourceDashboardBL.getWorkItemBeans(configParameters,personBean, locale,projectID,releaseID, startDate,endDate);
		}
		// save workItems of current month to map
		Iterator<TWorkItemBean> workItemIter = workItemBeansList.iterator();
		while (workItemIter.hasNext()) {
			workItemBean = workItemIter.next();
			if (workItemBean.getStartDate() != null || workItemBean.getEndDate() != null||
					workItemBean.getTopDownStartDate() != null || workItemBean.getTopDownEndDate() != null){
				//++projectIndex;

				CalendarIssue calendarIssue=new CalendarIssue();
				calendarIssue.setStartDate(workItemBean.getStartDate());
				calendarIssue.setEndDate(workItemBean.getEndDate());
				calendarIssue.setTopDownStartDate(workItemBean.getTopDownStartDate());
				calendarIssue.setTopDownEndDate(workItemBean.getTopDownEndDate());

				String label = new String();
				String description = new String();
				String objectID = new String();
				StringBuffer labM = new StringBuffer();
				StringBuffer labW = new StringBuffer();

				try {
					label = workItemBean.getLabel();
					description = workItemBean.getDescription();
					objectID = workItemBean.getObjectID().toString();
				}
				catch(Exception e)
				{
					LOGGER.warn(e.getMessage());
				}

				// get substring for month view
				if (label.length() > 23) {
					labM.append(label.substring(0, 19));
					labM.append("...");
				}
				else {
					labM.append(label);
				}

				// get substring for week view
				if (label.length() > 30) {
					labW.append(label.substring(0, 26));
					labW.append("...");
				}
				else {
					labW.append(label);
				}

				// cut out all Tags of the description				
				StringBuffer desc = new StringBuffer();
				if (description!=null) {
					try {
						description = Html2Text.getNewInstance().convert(description);
					} catch (Exception e) {
						description = Html2Text.getHtmlDecodedString(description);
					}
					Pattern p = Pattern.compile("<[.[^>]]*>");
					description = description.replaceAll(p.toString(),"");
					//				p = Pattern.compile("[\\s]++");
					//				description = description.replaceAll(p.toString()," ");

					// get substring for week view					
					if (description.length() > 100) {
						desc.append(description.substring(0, 96));
						desc.append("...");
					}
					else {
						desc.append(description);
					}
				}

				// generate tool tip
				StringBuffer toolTip = new StringBuffer();  // StringBuffer to get the tool tip of the links
				toolTip.append(objectID);
				toolTip.append(" : ");
				toolTip.append(label);
				toolTip.append(" : ");
				toolTip.append(desc);

				// save parameters of workItems to listMap
				calendarIssue.setObjectID(workItemBean.getObjectID());
				calendarIssue.setTitle(workItemBean.getSynopsis());
				calendarIssue.setTooltip(toolTip.toString());
				calendarIssue.setCssColorClass(getCssColorClass(workItemBean, personBean, locale));
				//listMap.put("labelM", labM.toString());
				//listMap.put("labelW", labW.toString());
				result.add(calendarIssue);
			}
		}

		// save list of work items to map
		LOGGER.debug("dbAccess for issues ready!");
		return result;
	}
	private static String getCssColorClass(TWorkItemBean workItemBean, TPersonBean personBean, Locale locale){
		String cssColorClass = "synopsis_blue";
		TStateBean stateBean = LookupContainer.getStatusBean(workItemBean.getStateID(), locale);
		Integer daysLead = null;
		daysLead = personBean.getEmailLead();
		
		Integer stateFlag=stateBean.getStateflag();
		int bottomUpDateDueFlag = workItemBean.calculateBottomUpDueDateOnPlan(stateFlag, daysLead);
		int topDownDateDueFlag = workItemBean.calculateTopDownDueDateOnPlan(stateFlag, daysLead);
		boolean dateConflict=TWorkItemBean.isDateConflict(bottomUpDateDueFlag) || TWorkItemBean.isDateConflict(topDownDateDueFlag) ;
		boolean onBudgetPlan=true;
		if (!workItemBean.isArchived()&&!workItemBean.isDeleted()) {
			if (Integer.valueOf(TStateBean.STATEFLAGS.CLOSED).equals(stateFlag)) {
				if (dateConflict) {
					cssColorClass = "synopsis_red";
				}
			} else {
				if (!dateConflict && onBudgetPlan) {
					if (bottomUpDateDueFlag==TWorkItemBean.DUE_FLAG.DUE_SOON) {
						cssColorClass = "synopsis_orange";
					}
				} else {
					cssColorClass = "synopsis_red";
				}
			}
		}
		return cssColorClass;
	}
	public static class CalendarIssue{
		public static final int TYPE_START=0;
		public static final int TYPE_END=1;
		public static final int TYPE_START_END=2;

		private Integer objectID;
		private String title;
		private String tooltip;
		private Date startDate;
		private Date endDate;
		private Date topDownStartDate;
		private Date topDownEndDate;

		private String cssColorClass;

		public Integer getObjectID() {
			return objectID;
		}
		public void setObjectID(Integer objectID) {
			this.objectID = objectID;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTooltip() {
			return tooltip;
		}
		public void setTooltip(String tooltip) {
			this.tooltip = tooltip;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}

		public Date getTopDownStartDate() {
			return topDownStartDate;
		}

		public void setTopDownStartDate(Date topDownStartDate) {
			this.topDownStartDate = topDownStartDate;
		}

		public Date getTopDownEndDate() {
			return topDownEndDate;
		}

		public void setTopDownEndDate(Date topDownEndDate) {
			this.topDownEndDate = topDownEndDate;
		}

		public String getCssColorClass() {
			return cssColorClass;
		}

		public void setCssColorClass(String cssColorClass) {
			this.cssColorClass = cssColorClass;
		}
	}
	

	public static class Week{
		private Integer weekOfYear;
		private CalendarDay[] days;
		//the index of today is in current week, used for mark current day
		public CalendarDay[] getDays() {
			return days;
		}
		public void setDays(CalendarDay[] days) {
			this.days = days;
		}
		public Integer getWeekOfYear() {
			return weekOfYear;
		}
		public void setWeekOfYear(Integer weekOfYear) {
			this.weekOfYear = weekOfYear;
		}
	}
	public static class Month{
		private Integer lastYear;
		private Integer nextYear;
		private Integer currentMonth;
		private CalendarDay[][] days;
		private long firstDay;
		private long lastDay;
		public long getFirstDay() {
			return firstDay;
		}
		public void setFirstDay(long firstDay) {
			this.firstDay = firstDay;
		}
		public long getLastDay() {
			return lastDay;
		}
		public void setLastDay(long lastDay) {
			this.lastDay = lastDay;
		}
		public Integer getLastYear() {
			return lastYear;
		}
		public void setLastYear(Integer lastYear) {
			this.lastYear = lastYear;
		}
		public Integer getNextYear() {
			return nextYear;
		}
		public void setNextYear(Integer nextYear) {
			this.nextYear = nextYear;
		}
		public Integer getCurrentMonth() {
			return currentMonth;
		}
		public void setCurrentMonth(Integer currentMonth) {
			this.currentMonth = currentMonth;
		}
		public CalendarDay[][] getDays() {
			return days;
		}
		public void setDays(CalendarDay[][] days) {
			this.days = days;
		}
	}
	public static class CalendarDay{
		private Date date;
		private List<CalendarIssue> issues;
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public List<CalendarIssue> getIssues() {
			return issues;
		}
		public void setIssues(List<CalendarIssue> issues) {
			this.issues = issues;
		}
	}
}
