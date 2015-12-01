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


package com.cynertia.track.iCalendar;

import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ApplicationAware;

import com.aurel.track.ApplicationStarter;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadResponsibleItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.util.emailHandling.Html2Text;

public class ICalendarSynchronizeAction extends Action implements ApplicationAware {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ICalendarSynchronizeAction.class);
	private Map application;
	
	public String sendIcs(){
		if (!ApplicationStarter.getInstance().isServerReady()) {
			return "";
		}
		HttpServletRequest request = org.apache.struts2.ServletActionContext.getRequest();
		TPersonBean user = this.authenticate(request);
		if (user == null){
			LOGGER.error("User not found or invalid password");
			return null;
		}
		String project = "";
		try {
			project = request.getParameter("project").trim();
		}
		catch (Exception ex) {
			LOGGER.error("Missing project parameter in iCalendarURL of user " + user.getFullName());
		}
			
		Calendar calendar = null;
		
		try {
			calendar = generateCalendar(user, project);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		
		if (calendar == null){
			LOGGER.error("Error generating the calendar");
			return null;
		}
		
		HttpServletResponse response = org.apache.struts2.ServletActionContext.getResponse();
		response.setContentType("text/calendar");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition","inline; filename=\"TrackPlusCalendar.ics\"");
		
		final CalendarOutputter calendarOutputter = new CalendarOutputter(false);
		
		try {
			calendarOutputter.output(calendar, response.getOutputStream());
			LOGGER.debug("ICalendar Plugin: Sent Calendar for Projects '" + project +"' to " + user.getFullName());
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Authenticate the user passed in the request 
	 * @param request
	 * @return TPerson of the user
	 * 		   null if the password is not correct
	 */
	private TPersonBean authenticate (HttpServletRequest request){
		String userRequest = request.getParameter("user");
		String passwordRequest = request.getParameter("pwd");
		TPersonBean user = null;
		try {
			user = PersonBL.loadByLoginName(userRequest);
			if (!passwordRequest.equals(user.getPasswd())){
				user = null;
			}			
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return user;
	}
	
	/**
	 * Generates a Calendar Object
	 * @param user 
	 * @param project
	 * @return Calendar of the user
	 * 		   null if the calendar has not got events
	 * @throws TooManyItemsToLoadException 
	 */
	private Calendar generateCalendar(TPersonBean user, String project) throws IOException, TooManyItemsToLoadException {
		Calendar calendar = new Calendar();
		
		calendar.getProperties().add(new ProdId("Calendar of Projects: "+ project));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getProperties().add(Method.PUBLISH);
		
		List<TWorkItemBean> workItemList = obtainIssues(user, project);
		
		if (workItemList!=null&&workItemList.size()!=0){
			addEventsFromWorkItemList(workItemList, calendar, user.getLocale());
			return calendar;
		}	
		else {
			return calendar;
		}
	}
	
	/**
	 * Obtain all the issues of for one user and some projects. 
	 * If project is 'ALL', it refers to all his projects
	 * @param user 
	 * @param projects
	 * @return List of workItems that are in some project for the user
	 * @throws TooManyItemsToLoadException 
	 */
	private List<TWorkItemBean> obtainIssues(TPersonBean user, String projects) throws TooManyItemsToLoadException{
		if (projects.equalsIgnoreCase("ALL")){
			return LoadResponsibleItems.loadResponsibleWorkItems(user);
		}
		List<TWorkItemBean>  workItemList=null;
		Integer[] selectedProjects=null;
		if (projects.equalsIgnoreCase("ACTIVE")){
			List<TProjectBean> projectList = ProjectBL.loadActiveProjectsWithReadIssueRight(user.getObjectID());
			selectedProjects=GeneralUtils.createIntegerArrFromCollection(GeneralUtils.createIntegerListFromBeanList(projectList));
		}else{
			selectedProjects= StringArrayParameterUtils.splitSelectionAsIntegerArray(projects,"-");
		}
		if(selectedProjects!=null){
			FilterUpperTO filterUpperTO=new FilterUpperTO();
			filterUpperTO.setSelectedProjects(selectedProjects);
			filterUpperTO.setSelectedResponsibles(new Integer[]{user.getObjectID()});
			workItemList=LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO,user, user.getLocale(), false);
		}
		return workItemList;
	}
	/**
	 * Put in the calendar an Event for each workitem
	 * @param workItemList 
	 * @param calendar
	 */
	private void addEventsFromWorkItemList(List<TWorkItemBean>  workItemList, Calendar calendar, Locale locale) throws IOException {
		
		for (Iterator<TWorkItemBean> it = workItemList.iterator(); it.hasNext();) {
			
			TWorkItemBean issue =it.next();
			
			//If it hasn't got any date, do nothing
			if (issue.getStartDate() != null || issue.getEndDate() != null ){
					
				// Create a TimeZone
				// TODO: It is needed to tests if it manages the daylight times
				final java.util.TimeZone serverTimeZone =  java.util.TimeZone.getDefault();
				
				TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
				TimeZone timezone = registry.getTimeZone(serverTimeZone.getID());
				VTimeZone tz = timezone.getVTimeZone();

				//set the event depending if it has start date and/or end date
				//always mark it as Full Day Task (it appears in the header of the day)
				VEvent issueEvent;
				java.util.Calendar startDate = new GregorianCalendar();
				startDate.setTimeZone(timezone);
				
				java.util.Calendar endDate = new GregorianCalendar();
				endDate.setTimeZone(timezone);
				
				if (issue.getStartDate() != null) {
					startDate.setTime(issue.getStartDate());
					if (issue.getEndDate() != null) { // startDate and endDate => use both
						endDate.setTime(issue.getEndDate());
					} else { // startDate and !endDate => use startDate
						endDate.setTime(issue.getStartDate());
					}
				}else{ // !startDate and endDate => use endDate
					startDate.setTime(issue.getEndDate());
					endDate.setTime(issue.getEndDate());
				}
				Integer responsibleID = issue.getResponsibleID();
				String description = issue.getDescription();
				if (description!=null) {
					try {
						description = Html2Text.getNewInstance().convert(description);
					} catch (Exception e) {
					}
				}
				if (startDate.equals(endDate)) {
					startDate.add(java.util.Calendar.DAY_OF_MONTH,1); 
					issueEvent = createEvent(new Date(startDate.getTime()),  new Date(startDate.getTime()), issue.getSynopsis(), issue.getObjectID(), description, responsibleID, tz);
					calendar.getComponents().add(issueEvent);
				} else {
					startDate.add(java.util.Calendar.DAY_OF_MONTH,1); 
					String title = LocalizeUtil.getParametrizedString("admin.myprefs.iCalendar.start", new Object[] {issue.getSynopsis()}, locale);
					issueEvent = createEvent(new Date(startDate.getTime()),  new Date(startDate.getTime()), title, issue.getObjectID(), description, responsibleID, tz);
					calendar.getComponents().add(issueEvent);
					endDate.add(java.util.Calendar.DAY_OF_MONTH,1);
					title = LocalizeUtil.getParametrizedString("admin.myprefs.iCalendar.end", new Object[] {issue.getSynopsis()}, locale);
					issueEvent = createEvent(new Date(endDate.getTime()),  new Date(endDate.getTime()), title, issue.getObjectID(), description, responsibleID, tz);
					calendar.getComponents().add(issueEvent);
					
				}
				// the period is [StartDate,EndDate)
			}
		}
	}

	private static VEvent createEvent(Date startDate, Date endDate, String title, Integer itemID, String description, Integer responsibleID, VTimeZone tz) throws SocketException {
		VEvent issueEvent = new VEvent(new Date(startDate.getTime()), new Date(endDate.getTime()), title);
		
		// add timezone info..
		issueEvent.getProperties().add(tz.getTimeZoneId());

		// add description
		issueEvent.getProperties().add(new Description(description));

		// TODO: more adds
		
		// generate unique identifier..
		UidGenerator ug = new UidGenerator(itemID.toString());
		issueEvent.getProperties().add(ug.generateUid());
		
		//add organizer to method=PUBLISH for Outlook
		try {
			TPersonBean personBean = LookupContainer.getPersonBean(responsibleID);
			if (personBean!=null) {
				String email = personBean.getEmail();
				if (email!=null) {
					URI mailToURI = new URI("MAILTO", email, null);
					issueEvent.getProperties().add(new Organizer(mailToURI));
				}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return issueEvent;
	}
	
	/**
	 * @return the application
	 */
	public Map getApplication() {
		return application;
	}

	/**
	 * @param application the application to set
	 */
	@Override
	public void setApplication(Map application) {
		this.application = application;
	}

}
