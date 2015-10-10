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


package com.aurel.track.util.event.parameters;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TFieldConfigBean;
import com.aurel.track.beans.TWorkItemBean;

public class AfterItemSaveEventParam {
	
	protected TWorkItemBean workItemNew;
	
	protected TWorkItemBean workItemOld;
	
	protected Set<Integer> interestingFields;
	
	protected Map<Integer, TFieldConfigBean> fieldConfigs;
	
	//the locale of the user triggering the event 
	protected Locale locale; 
	
	//by adding watchers to an existing item
	protected List<Integer> selectedPersons;
	
	/**
	 * @return the specifiedFields
	 */
	public Set<Integer> getInterestingFields() {
		return interestingFields;
	}
	/**
	 * @param specifiedFields the specifiedFields to set
	 */
	public void setInterestingFields(Set<Integer> specifiedFields) {
		this.interestingFields = specifiedFields;
	}
	/**
	 * @return the workItemNew
	 */
	public TWorkItemBean getWorkItemNew() {
		return workItemNew;
	}
	/**
	 * @param workItemNew the workItemNew to set
	 */
	public void setWorkItemNew(TWorkItemBean workItemNew) {
		this.workItemNew = workItemNew;
	}
	/**
	 * @return the workItemOld
	 */
	public TWorkItemBean getWorkItemOld() {
		return workItemOld;
	}
	/**
	 * @param workItemOld the workItemOld to set
	 */
	public void setWorkItemOld(TWorkItemBean workItemOld) {
		this.workItemOld = workItemOld;
	}
	/**
	 * @return the fieldConfigs
	 */
	public Map<Integer, TFieldConfigBean> getFieldConfigs() {
		return fieldConfigs;
	}
	/**
	 * @param fieldConfigs the fieldConfigs to set
	 */
	public void setFieldConfigs(Map<Integer, TFieldConfigBean> fieldConfigs) {
		this.fieldConfigs = fieldConfigs;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public List<Integer> getSelectedPersons() {
		return selectedPersons;
	}
	public void setSelectedPersons(List<Integer> selectedPersons) {
		this.selectedPersons = selectedPersons;
	}
	
}
