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


package com.aurel.track.util.calendar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag for displaying a calendar. By default the current month is displayed.
 * if a request parameter named 'calDate' (Long) is present and is correctly 
 * translated into a java.util.Date the calendar shows the corresponding month.
 *
 * @author thogau
 *
 * @jsp.tag name="calendar" bodycontent="empty"
 */
public class CalendarTag extends TagSupport {
	private final static String DATE_PARAMETER_NAME = "calDate";

	private static final long serialVersionUID = 3905528206810115095L;

	protected String requestURI = null;

	protected String linkURI = null;

	protected String name;

	protected String title;

	protected String styleId = "calendar";

	protected boolean linkAll = false;

	protected boolean showToday = false;

	/**
	 * @param requestURI the request URI to use in next/prev links.
	 *
	 * @jsp.attribute required="true" rtexprvalue="true"
	 */
	public void setRequestURI(String uri) {
		this.requestURI = uri;
	}

	/**
	 * @param linkURI the request URI to use if linkAll is set to 'true'.
	 *
	 * @jsp.attribute required="false" rtexprvalue="true"
	 */
	public void setLinkURI(String uri) {
		this.linkURI = uri;
	}

	/**
	 * @param name name of the request scope attribute that holds the
	 * java.util.List of CalendarEvents to add to calendar view.
	 *
	 * @jsp.attribute required="false" rtexprvalue="true"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param styleId name of the css style id for the calendar container.
	 *
	 * @jsp.attribute required="false" rtexprvalue="true"
	 */
	public void setStyleId(String id) {
		this.styleId = id;
	}

	/**
	 * @param title title to display on the calendar.
	 *
	 * @jsp.attribute required="false" rtexprvalue="true"
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param linkAll if true, a link is generated for every day.
	 *
	 * @jsp.attribute required="false" rtexprvalue="true"
	 */
	public void setLinkAll(String b) {
		if (b == null) {
			this.linkAll = false;
			return;
		}
		if (b.toLowerCase().equals("true")) {
			this.linkAll = true;
		} else {
			this.linkAll = false;
		}
		// The following works for JDK 1.5 and up only...
		// this.linkAll = Boolean.parseBoolean(b);
	}

	/**
	 * @param showToday if true, the actual day is generated with a special
	 * css style id.
	 *
	 * @jsp.attribute required="false" rtexprvalue="true"
	 */
	public void setShowToday(String b) {
		if (b == null) {
			this.showToday = false;
			return;
		}
		if (b.toLowerCase().equals("true")) {
			this.showToday = true;
		} else {
			this.showToday = false;
		}
		// The following works for JDK 1.5 and up only...
		// this.showToday = Boolean.parseBoolean(b);
	}

	/**
	 * Process the start of this tag.
	 *
	 * @return
	 *
	 * @exception JspException if a JSP exception has occurred
	 *
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		Locale locale = pageContext.getRequest().getLocale();
		Calendar cal = Calendar.getInstance(locale);
		Calendar date = Calendar.getInstance(locale);
		List events = null;
		DateFormat df;
		StringBuffer sb = new StringBuffer();

		// set requestURI
		HttpServletRequest req = ((HttpServletRequest) pageContext.getRequest());
		if (!requestURI.startsWith("http://")) {
			if (!requestURI.startsWith(req.getContextPath())) {
				requestURI = req.getContextPath() + requestURI;
			}
		}

		// set linkURI
		if (linkURI != null) {
			if (!linkURI.startsWith("http://")) {
				if (!linkURI.startsWith(req.getContextPath())) {
					linkURI = req.getContextPath() + linkURI;
				}
			}
		} else {
			linkURI = requestURI;
		}

		// set date if any
		try {
			String dateParameter = pageContext.getRequest().getParameter(
					DATE_PARAMETER_NAME);
			date.setTimeInMillis(Long.parseLong(dateParameter));
		} catch (Exception e) {
		}

		// set events if any
		try {
			events = (List) pageContext.getRequest().getAttribute(name);
		} catch (Exception e) {
		}

		// prepare output
		sb.append("<div id=\"" + styleId + "\">\n");

		if (title != null && title.length() > 0) {
			sb.append(title);
		}

		sb
				.append("<table class=\"calTable\" cellspacing=\"3\" cellpadding=\"2\">\n");

		// line : header
		df = new SimpleDateFormat("MMMMM yyyy", locale);
		cal.setTime(date.getTime());
		cal.getTimeInMillis();// to force cal to update
		sb.append("<tr class=\"calendarHeader\">");
		sb.append("<th>");
		cal.add(Calendar.MONTH, -1);
		sb.append("<a class=\"calPrevious\" href=\"" + requestURI + "?calDate="
				+ cal.getTimeInMillis() + "\">");
		sb
				.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>");
		sb.append("</th>\n");
		sb.append("<th colspan=\"5\" class=\"calTitle\">");
		sb.append(df.format(date.getTime()));
		sb.append("</th>\n");
		sb.append("<th style=\"text-align: right\">");
		cal.add(Calendar.MONTH, 2);
		sb.append("<a class=\"calNext\" href=\"" + requestURI + "?calDate="
				+ cal.getTimeInMillis() + "\">");
		sb
				.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>");
		sb.append("</th>\n");
		sb.append("</tr>\n");
		cal.add(Calendar.MONTH, -1);

		// line : days of week
		df = new SimpleDateFormat("EEE", locale);
		sb.append("<tr class=\"calWeekDays\">\n");
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		for (int i = 1; i <= 7; i++) {
			sb.append("<td width=\"14%\" class=\"calWeekDay\">");
			sb.append(df.format(cal.getTime()));
			sb.append("</td>\n");
			cal.add(Calendar.DATE, 1);
		}
		sb.append("</tr>\n");

		// lines : content
		CalendarDay[] days = preprocess(events, date);
		sb.append("<tr>\n");
		for (int i = 0; i < days.length && days[i] != null; i++) {
			// new line every 7
			if (i % 7 == 0 && i != 0) {
				sb.append("</tr>\n<tr>");
			}
			sb.append(days[i].toString());
		}
		sb.append("</tr>\n");

		sb.append("</table>\n");
		sb.append("</div>\n");

		// writes output
		try {
			pageContext.getOut().write(sb.toString());
		} catch (IOException io) {
			throw new JspException(io);
		}

		return super.doStartTag();
	}

	/**
	 * Release aquired resources to enable tag reusage.
	 *
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	@Override
	public void release() {
		super.release();
	}

	protected CalendarDay[] preprocess(List events, Calendar date) {
		CalendarDay[] days = new CalendarDay[42];
		CalendarDay day = null;
		CalendarEvent evt = null;
		Calendar tmpCal = Calendar.getInstance();
		int tmpInt = 0;
		int offset = 0;
		int month, year;

		tmpCal.setTime(date.getTime());
		month = tmpCal.get(Calendar.MONTH);
		year = tmpCal.get(Calendar.YEAR);

		// uggly way to reach the last monday of former month
		tmpCal.getTimeInMillis();// to force cal to update
		tmpCal.set(Calendar.DATE, 1);
		tmpCal.getTimeInMillis();// to force cal to update
		tmpCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		// step 1 : days before
		while (tmpCal.get(Calendar.DATE) != 1) {
			day = new CalendarDay();
			day.setStyleClass("calDayOut");
			day.addLine(tmpCal.get(Calendar.DATE) + "");
			days[tmpInt++] = day;
			offset++;
			tmpCal.add(Calendar.DATE, 1);
		}

		// step 2 : days of the month
		while (tmpCal.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
			day = new CalendarDay();
			day.setStyleClass("calDayIn");
			if (linkAll) {
				// link for every day
				day.addLine("<a href=\"" + linkURI + "?calDate="
						+ tmpCal.getTimeInMillis() + "\" class=\"calDayLink\">"
						+ tmpCal.get(Calendar.DATE) + "</a>");
			} else {
				// just the date
				day.addLine(tmpCal.get(Calendar.DATE) + "");
			}
			if (tmpCal.get(Calendar.DATE) == date.get(Calendar.DATE)) {
				// selected date
				day.setStyleId("calSelected");
			}
			if (tmpCal.get(Calendar.DATE) == Calendar.getInstance().get(
					Calendar.DATE)) {
				// today
				if (showToday) {
					day.setStyleId("calToday");
				}
			}
			days[tmpInt++] = day;
			tmpCal.add(Calendar.DATE, 1);
		}

		// step 3 : days after the month
		while (tmpInt % 7 != 0) {
			day = new CalendarDay();
			day.addLine(tmpCal.get(Calendar.DATE) + "");
			day.setStyleClass("calDayOut");
			days[tmpInt++] = day;
			tmpCal.add(Calendar.DATE, 1);
		}

		if (events != null && events.size() > 0) {
			// step 4 : insert events info
			for (Iterator iter = events.iterator(); iter.hasNext();) {
				evt = (CalendarEvent) iter.next();
				tmpCal.setTime(evt.getStart());
				tmpInt = 1;
				if (evt.getStart().getTime() == evt.getEnd().getTime()) {
					if (tmpCal.get(Calendar.MONTH) == month
							&& tmpCal.get(Calendar.YEAR) == year) {
						day = days[tmpCal.get(Calendar.DATE) + offset - 1];
						day.setStyleClass("calEvent");
						if (evt.getIcon() != null) {
							day.addLine("<img class=\"CalIcon\" src=\""
									+ evt.getIcon() + "\"/>" + "<a href =\""
									+ evt.getLink()
									+ "\" class=\"calDayLink\">"
									+ evt.getTitle() + "</a>");
						} else {
							day.addLine("<a href =\"" + evt.getLink()
									+ "\" class=\"calDayLink\">"
									+ evt.getTitle() + "</a>");
						}
					}
					tmpCal.add(Calendar.DATE, 1);
				} else {
					while (!tmpCal.getTime().after(evt.getEnd())) {
						if (tmpCal.get(Calendar.MONTH) == month
								&& tmpCal.get(Calendar.YEAR) == year) {
							day = days[tmpCal.get(Calendar.DATE) + offset - 1];
							day.setStyleClass("calEvent");
							if (evt.getIcon() != null) {
								day.addLine("<img class=\"CalIcon\" src=\""
										+ evt.getIcon() + "\"/>"
										+ "<a href =\"" + evt.getLink()
										+ "\" class=\"calDayLink\">"
										+ evt.getTitle() + " (" + tmpInt++
										+ ")</a>");
							} else {
								day.addLine("<a href =\"" + evt.getLink()
										+ "\" class=\"calDayLink\">"
										+ evt.getTitle() + " (" + tmpInt++
										+ ")</a>");
							}
						}
						tmpCal.add(Calendar.DATE, 1);
					}
				}
			}
		}

		return days;
	}

}
