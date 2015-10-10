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
import java.util.Map;

import com.aurel.track.beans.TPersonBasketBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;

public class ReminderEventParam {
	
	protected List<TWorkItemBean> originatorItems;
	protected List<TWorkItemBean> responsibleItems;
	protected List<TWorkItemBean> managerItems;
	protected Map<Integer, List<TPersonBasketBean>> personDateBasketItemsMap;
	protected Map<Integer, List<TPersonBasketBean>> personTimeBasketItemsMap;
	//protected List<TWorkItemBean> reminderBasketItems;
	//protected List<TWorkItemBean> delegatedBasketItems;
	//private Map<Integer, TProjectBean> projects;
	protected TPersonBean receiver;
	
	/*public Map<Integer, TProjectBean> getProjects() {
		return projects;
	}
	public void setProjects(Map<Integer, TProjectBean> projects) {
		this.projects = projects;
	}*/
	
	public TPersonBean getReceiver() {
		return receiver;
	}
	public void setReceiver(TPersonBean receiver) {
		this.receiver = receiver;
	}
	
	public List<TWorkItemBean> getOriginatorItems() {
		return originatorItems;
	}
	public void setOriginatorItems(List<TWorkItemBean> originatorItems) {
		this.originatorItems = originatorItems;
	}
	public List<TWorkItemBean> getResponsibleItems() {
		return responsibleItems;
	}
	public void setResponsibleItems(List<TWorkItemBean> responsibleItems) {
		this.responsibleItems = responsibleItems;
	}
	public List<TWorkItemBean> getManagerItems() {
		return managerItems;
	}
	public void setManagerItems(List<TWorkItemBean> managerItems) {
		this.managerItems = managerItems;
	}
	public Map<Integer, List<TPersonBasketBean>> getPersonDateBasketItemsMap() {
		return personDateBasketItemsMap;
	}
	public void setPersonDateBasketItemsMap(
			Map<Integer, List<TPersonBasketBean>> personDateBasketItemsMap) {
		this.personDateBasketItemsMap = personDateBasketItemsMap;
	}
	public Map<Integer, List<TPersonBasketBean>> getPersonTimeBasketItemsMap() {
		return personTimeBasketItemsMap;
	}
	public void setPersonTimeBasketItemsMap(
			Map<Integer, List<TPersonBasketBean>> personTimeBasketItemsMap) {
		this.personTimeBasketItemsMap = personTimeBasketItemsMap;
	}
	
}
