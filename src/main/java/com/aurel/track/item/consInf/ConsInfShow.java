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


package com.aurel.track.item.consInf;


import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.aurel.track.beans.TPersonBean;

/**
 * Bean storing the consultant/informant data for the consultant/informant tab
 * @author Tamas Ruff
 *
 */
public class ConsInfShow {
	private List<TPersonBean> realConsultantPersons = new LinkedList<TPersonBean>();
	private List<TPersonBean> realConsultantGroups = new LinkedList<TPersonBean>();
	private List<TPersonBean> realInformantPersons = new LinkedList<TPersonBean>();
	private List<TPersonBean> realInformantGroups = new LinkedList<TPersonBean>();
	private Integer[] selectedConsultantPersons = new Integer[0];
	private Integer[] selectedConsultantGroups = new Integer[0];
	private Integer[] selectedInformantPersons = new Integer[0];
	private Integer[] selectedInformantGroups = new Integer[0];
	
	private boolean amIConsultant;
	private boolean amIInformant;
	
	
	public boolean isAmIConsultant() {
		return amIConsultant;
	}


	public void setAmIConsultant(boolean amIConsultant) {
		this.amIConsultant = amIConsultant;
	}


	public boolean isAmIInformant() {
		return amIInformant;
	}


	public void setAmIInformant(boolean amIInformant) {
		this.amIInformant = amIInformant;
	}

	
	public List<TPersonBean> getRealConsultantGroups() {
		return realConsultantGroups;
	}


	public void setRealConsultantGroups(List<TPersonBean> realConsultantGroups) {
		this.realConsultantGroups = realConsultantGroups;
	}


	public List<TPersonBean> getRealConsultantPersons() {
		return realConsultantPersons;
	}


	public void setRealConsultantPersons(List<TPersonBean> realConsultantPersons) {
		this.realConsultantPersons = realConsultantPersons;
	}


	public List<TPersonBean> getRealInformantGroups() {
		return realInformantGroups;
	}


	public void setRealInformantGroups(List<TPersonBean> realInformantGroups) {
		this.realInformantGroups = realInformantGroups;
	}


	public List<TPersonBean> getRealInformantPersons() {
		return realInformantPersons;
	}


	public void setRealInformantPersons(List<TPersonBean> realInformantPersons) {
		this.realInformantPersons = realInformantPersons;
	}


	public Integer[] getSelectedConsultantGroups() {
		return selectedConsultantGroups;
	}


	public void setSelectedConsultantGroups(Integer[] selectedConsultantGroups) {
		this.selectedConsultantGroups = selectedConsultantGroups;
	}


	public Integer[] getSelectedConsultantPersons() {
		return selectedConsultantPersons;
	}


	public void setSelectedConsultantPersons(Integer[] selectedConsultantPersons) {
		this.selectedConsultantPersons = selectedConsultantPersons;
	}


	public Integer[] getSelectedInformantGroups() {
		return selectedInformantGroups;
	}


	public void setSelectedInformantGroups(Integer[] selectedInformantGroups) {
		this.selectedInformantGroups = selectedInformantGroups;
	}


	public Integer[] getSelectedInformantPersons() {
		return selectedInformantPersons;
	}


	public void setSelectedInformantPersons(Integer[] selectedInformantPersons) {
		this.selectedInformantPersons = selectedInformantPersons;
	}


	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		selectedConsultantPersons = new Integer[0];
		selectedInformantPersons = new Integer[0];
	}

	
}
