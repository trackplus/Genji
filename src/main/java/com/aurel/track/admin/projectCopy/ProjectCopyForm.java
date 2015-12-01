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


package com.aurel.track.admin.projectCopy;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Form bean for the copy project page for copy the issues. The old and new
 * properties are defined here.
 */
public final class ProjectCopyForm {

	private Integer projectOld;
	private Integer projectNew;
	private Integer initialState;
	private Date earliestDateNew;

	private List managerOldList;
	private List managerNewList;
	private List responsibleOldList;
	private List responsibleNewList;
	private List initialStateList;

	private Map managerMap;
	private Map responsibleMap;

	private boolean copyAttachments;
	private boolean deepCopy;

	/**
	 * @return the projectOld
	 */
	public Integer getProjectOld() {
		return this.projectOld;
	}

	/**
	 * @param projectOld
	 *            the projectOld to set
	 */
	public void setProjectOld(Integer projectOld) {
		this.projectOld = projectOld;
	}

	/**
	 * @return the projectNew
	 */
	public Integer getProjectNew() {
		return this.projectNew;
	}

	/**
	 * @param projectNew
	 *            the projectNew to set
	 */
	public void setProjectNew(Integer projectNew) {
		this.projectNew = projectNew;
	}

	/**
	 * @return the initialState
	 */
	public Integer getInitialState() {
		return this.initialState;
	}

	/**
	 * @param initialState
	 *            the initialState to set
	 */
	public void setInitialState(Integer initialState) {
		this.initialState = initialState;
	}

	/**
	 * @return the earliestDateNew
	 */
	public Date getEarliestDateNew() {
		return this.earliestDateNew;
	}

	/**
	 * @param earliestDateNew
	 *            the earliestDateNew to set
	 */
	public void setEarliestDateNew(Date earliestDateNew) {
		this.earliestDateNew = earliestDateNew;
	}

	/**
	 * @return the managerOldList
	 */
	public List getManagerOldList() {
		return this.managerOldList;
	}

	/**
	 * @param managerOldList
	 *            the managerOldList to set
	 */
	public void setManagerOldList(List managerOldList) {
		this.managerOldList = managerOldList;
	}

	/**
	 * @return the managerNewList
	 */
	public List getManagerNewList() {
		return this.managerNewList;
	}

	/**
	 * @param managerNewList
	 *            the managerNewList to set
	 */
	public void setManagerNewList(List managerNewList) {
		this.managerNewList = managerNewList;
	}

	/**
	 * @return the responsibleOldList
	 */
	public List getResponsibleOldList() {
		return this.responsibleOldList;
	}

	/**
	 * @param responsibleOldList
	 *            the responsibleOldList to set
	 */
	public void setResponsibleOldList(List responsibleOldList) {
		this.responsibleOldList = responsibleOldList;
	}

	/**
	 * @return the responsibleNewList
	 */
	public List getResponsibleNewList() {
		return this.responsibleNewList;
	}

	/**
	 * @param responsibleNewList
	 *            the responsibleNewList to set
	 */
	public void setResponsibleNewList(List responsibleNewList) {
		this.responsibleNewList = responsibleNewList;
	}

	/**
	 * @return the initialStateList
	 */
	public List getInitialStateList() {
		return this.initialStateList;
	}

	/**
	 * @param initialStateList
	 *            the initialStateList to set
	 */
	public void setInitialStateList(List initialStateList) {
		this.initialStateList = initialStateList;
	}

	/**
	 * @return the managerMap
	 */
	public Map getManagerMap() {
		return this.managerMap;
	}

	/**
	 * @param managerMap
	 *            the managerMap to set
	 */
	public void setManagerMap(Map managerMap) {
		this.managerMap = managerMap;
	}

	/**
	 * @return the responsibleMap
	 */
	public Map getResponsibleMap() {
		return this.responsibleMap;
	}

	/**
	 * @param responsibleMap
	 *            the responsibleMap to set
	 */
	public void setResponsibleMap(Map responsibleMap) {
		this.responsibleMap = responsibleMap;
	}

	/**
	 * @return the copyAttachments
	 */
	public boolean isCopyAttachments() {
		return this.copyAttachments;
	}

	/**
	 * @param copyAttachments
	 *            the copyAttachments to set
	 */
	public void setCopyAttachments(boolean copyAttachments) {
		this.copyAttachments = copyAttachments;
	}

	/**
	 * @return the deepCopy
	 */
	public boolean isDeepCopy() {
		return this.deepCopy;
	}

	/**
	 * @param deepCopy
	 *            the deepCopy to set
	 */
	public void setDeepCopy(boolean deepCopy) {
		this.deepCopy = deepCopy;
	}

}
