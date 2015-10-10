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

package com.aurel.track.fieldType.runtime.system.text;

import java.util.Date;

import com.aurel.track.beans.TWorkItemBean;

/**
 * Behavior for TargetDuration field
 * @author Tamas
 *
 */
public class SystemTargetDurationRT extends SystemDurationRT {
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	@Override
	protected Date getStartDate(TWorkItemBean workItemBean) {
		if (workItemBean!=null) {
			return workItemBean.getTopDownStartDate();
		}
		return null;
	}
	
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	/*@Override
	protected void setStartDate(TWorkItemBean workItemBean, Date startDate) {
		if (workItemBean!=null) {
			workItemBean.setTopDownStartDate(startDate);
		}
	}*/

	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	@Override
	protected Date getEndDate(TWorkItemBean workItemBean) {
		if (workItemBean!=null) {
			return workItemBean.getTopDownEndDate();
		}
		return null;
	}
	
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	/*@Override
	protected void setEndDate(TWorkItemBean workItemBean, Date endDate) {
		if (workItemBean!=null) {
			workItemBean.setTopDownEndDate(endDate);
		}
	}*/
	
	/**
	 * Gets the start date of the workitem
	 * @param workItemBean
	 * @param fieldID
	 * @return
	 */
	/*@Override
	protected Double getDuration(TWorkItemBean workItemBean) {
		if (workItemBean!=null) {
			return workItemBean.getTopDownDuration();
		}
		return null;
	}*/
}
