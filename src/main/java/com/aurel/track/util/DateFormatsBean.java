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


package com.aurel.track.util;

import java.text.DateFormat;

/**
 * Date and date time formats used in the application
 * @author Tamas Ruff
 *
 */
public class DateFormatsBean {
	private DateFormat shortDateFormat = null;
	private DateFormat shortDateTimeFormat = null;
	private DateFormat guiDateFormat = null;
	private DateFormat guiDateTimeFormat = null;
	private DateFormat guiDateTimeNoSecondsFormat = null;
	//format for rendering date in Ext js
	private String extJsDateFormat = null;
	//format for submitting Ext js date: two digits year set as submitFormat for Ext.form.field.Date to be interpretable by struts2
	private String extJsSubmitDateFormat = null;
	//format for rendering date and time in Ext js
	private String extJsDateTimeFormat;
	private String extJsTimeFormat;
	private String extJsTimeFormatNoSeconds;
		
	/**
	 * @return the guiCalendarDateFormat
	 */
	public DateFormat getGuiDateFormat() {
		return guiDateFormat;
	}
	/**
	 * @param guiCalendarDateFormat the guiCalendarDateFormat to set
	 */
	public void setGuiDateFormat(DateFormat guiCalendarDateFormat) {
		this.guiDateFormat = guiCalendarDateFormat;
	}
	/**
	 * @return the guiDateFormat
	 */
	public DateFormat getShortDateFormat() {
		return shortDateFormat;
	}
	/**
	 * @param guiDateFormat the guiDateFormat to set
	 */
	public void setShortDateFormat(DateFormat guiDateFormat) {
		this.shortDateFormat = guiDateFormat;
	}
	/**
	 * @return the guiDateTimeFormat
	 */
	public DateFormat getShortDateTimeFormat() {
		return shortDateTimeFormat;
	}
	/**
	 * @param guiDateTimeFormat the guiDateTimeFormat to set
	 */
	public void setShortDateTimeFormat(DateFormat guiDateTimeFormat) {
		this.shortDateTimeFormat = guiDateTimeFormat;
	}
		public String getExtJsDateFormat() {
		return extJsDateFormat;
	}
	public void setExtJsDateFormat(String extJsDateFormat) {
		this.extJsDateFormat = extJsDateFormat;
	}
    
    public String getExtJsDateTimeFormat() {
    	return extJsDateTimeFormat;
    }
    
    public void setExtJsDateTimeFormat(String fmt) {
    	extJsDateTimeFormat = fmt;
    }
	public String getExtJsSubmitDateFormat() {
		return extJsSubmitDateFormat;
	}
	public void setExtJsSubmitDateFormat(String extJsSubmitDateFormat) {
		this.extJsSubmitDateFormat = extJsSubmitDateFormat;
	}
	public DateFormat getGuiDateTimeFormat() {
		return guiDateTimeFormat;
	}
	public void setGuiDateTimeFormat(DateFormat guiCalendarDateTimeFormat) {
		this.guiDateTimeFormat = guiCalendarDateTimeFormat;
	}
	public String getExtJsTimeFormat() {
		return extJsTimeFormat;
	}
	public void setExtJsTimeFormat(String extJsTimeFormat) {
		this.extJsTimeFormat = extJsTimeFormat;
	}
	public String getExtJsTimeFormatNoSeconds() {
		return extJsTimeFormatNoSeconds;
	}
	public void setExtJsTimeFormatNoSeconds(String extJsTimeFormatNoSeconds) {
		this.extJsTimeFormatNoSeconds = extJsTimeFormatNoSeconds;
	}
	public DateFormat getGuiDateTimeNoSecondsFormat() {
		return guiDateTimeNoSecondsFormat;
	}
	public void setGuiDateTimeNoSecondsFormat(DateFormat guiDateTimeNoSecondsFormat) {
		this.guiDateTimeNoSecondsFormat = guiDateTimeNoSecondsFormat;
	}
	
	
}
