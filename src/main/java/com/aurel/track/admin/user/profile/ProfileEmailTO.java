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

package com.aurel.track.admin.user.profile;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

/**
 * Transfer object used for user profile reminder email tab.
 * @author Joerg Friedrich
 */
public class ProfileEmailTO implements Serializable{

	private static final long serialVersionUID = 400L;
	private Boolean noEmail;
	private Integer emailLead;
	private Boolean remindMeAsOriginator;
	private Boolean remindMeAsManager;
	private Boolean remindMeAsResponsible;
	private List<IntegerStringBean> remindPriorityLevels;
	private Integer remindPriorityLevel;
	private List<IntegerStringBean> remindSeverityLevels;
	private Integer remindSeverityLevel;
	private String prefEmailType;
	private List<Integer> remindMeOnDays;
	private List<LabelValueBean> remindMeOnDaysList;

	public Boolean isNoEmail() {
		return (noEmail==null ? new Boolean(false) : noEmail);
	}
	
	public void setNoEmail(Boolean noEmail) {
		this.noEmail = noEmail;
	}
	
	public Integer getEmailLead() {
		return emailLead;
	}
	
	public void setEmailLead(Integer emailLead) {
		this.emailLead = emailLead;
	}
	
	public Boolean isRemindMeAsOriginator() {
		return (remindMeAsOriginator==null ? new Boolean(false) : remindMeAsOriginator);
	}
	
	public void setRemindMeAsOriginator(Boolean remindMeAsOriginator) {
		this.remindMeAsOriginator = remindMeAsOriginator;
	}
	
	public Boolean isRemindMeAsManager() {
		return (remindMeAsManager==null ? new Boolean(false) : remindMeAsManager);
	}
	
	public void setRemindMeAsManager(Boolean remindMeAsManager) {
		this.remindMeAsManager = remindMeAsManager;
	}
	
	public Boolean isRemindMeAsResponsible() {
		return (remindMeAsResponsible==null ? new Boolean(false) : remindMeAsResponsible);
	}
	
	public void setRemindMeAsResponsible(Boolean remindMeAsResponsible) {
		this.remindMeAsResponsible = remindMeAsResponsible;
	}
	
	public void setRemindPriorityLevels(List<IntegerStringBean> remindPriorityLevels) {
		this.remindPriorityLevels = remindPriorityLevels;
	}

	public void setRemindSeverityLevels(List<IntegerStringBean> remindSeverityLevels) {
		this.remindSeverityLevels = remindSeverityLevels;
	}
	
	public List<IntegerStringBean> getRemindPriorityLevels() {
		return remindPriorityLevels;
	}

	public List<IntegerStringBean> getRemindSeverityLevels() {
		return remindSeverityLevels;
	}
	
	public String getPrefEmailType() {
		return prefEmailType;
	}
	
	public void setPrefEmailType(String prefEmailType) {
		this.prefEmailType = prefEmailType;
	}
	
	public List<Integer> getRemindMeOnDays() {
		return remindMeOnDays;
	}
	
	public void setRemindMeOnDays(List<Integer> remindMeOnDays) {
		this.remindMeOnDays = remindMeOnDays;
	}
	
	public List<LabelValueBean> getRemindMeOnDaysList() {
		return remindMeOnDaysList;
	}
	
	public void setRemindMeOnDaysList(List<LabelValueBean> _remindMeOnDaysList) {
		this.remindMeOnDaysList = _remindMeOnDaysList;
	}

	public Integer getRemindPriorityLevel() {
		return remindPriorityLevel;
	}

	public void setRemindPriorityLevel(Integer remindPriorityLevel) {
		this.remindPriorityLevel = remindPriorityLevel;
	}

	public Integer getRemindSeverityLevel() {
		return remindSeverityLevel;
	}

	public void setRemindSeverityLevel(Integer remindSeverityLevel) {
		this.remindSeverityLevel = remindSeverityLevel;
	}
	
	
}
