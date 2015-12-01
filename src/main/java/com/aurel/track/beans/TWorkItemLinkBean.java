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


package com.aurel.track.beans;

import java.io.Serializable;

import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.SortOrderUtil;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TWorkItemLinkBean
    extends com.aurel.track.beans.base.BaseTWorkItemLinkBean
    implements Serializable, Comparable<TWorkItemLinkBean>, IBeanID {
	private static final long serialVersionUID = 1L;

	public boolean hasChanged(TWorkItemLinkBean workItemLinkBean) {
		boolean result = false;
		if (workItemLinkBean == null) {
			return true;
		}
		if (EqualUtils.notEqual(getLinkIsCrossProject(), workItemLinkBean.getLinkIsCrossProject())) {
			result = true;
		}
		if (EqualUtils.notEqual(getLinkPred(), workItemLinkBean.getLinkPred())) {
			result = true;
		}
		if (EqualUtils.notEqual(getLinkSucc(), workItemLinkBean.getLinkSucc())) {
			result = true;
		}
		if (EqualUtils.notEqual(getLinkType(), workItemLinkBean.getLinkType())) {
			result = true;
		}
		if (EqualUtils.notEqual(getLinkLag(), workItemLinkBean.getLinkLag())) {
			result = true;
		}
		if (EqualUtils.notEqual(getLinkLagFormat(), workItemLinkBean.getLinkLagFormat())) {
			result = true;
		}
		if (EqualUtils.notEqual(getIntegerValue1(), workItemLinkBean.getIntegerValue1())) {
			//dependency type for msProject (FS, SS, FF, SF)
			//TODO implement id link type specific
			result = true;
		}
		return result;
	}

	/**
	 * Make a copy of the existing object
	 */
	public TWorkItemLinkBean copyToNew() {
		TWorkItemLinkBean workItemLinkBean = new TWorkItemLinkBean();
		workItemLinkBean.setLinkIsCrossProject(getLinkIsCrossProject());
		workItemLinkBean.setLinkPred(getLinkPred());
		workItemLinkBean.setLinkSucc(getLinkSucc());
		workItemLinkBean.setLinkType(getLinkType());
		workItemLinkBean.setLinkLag(getLinkLag());
		workItemLinkBean.setLinkLagFormat(getLinkLagFormat());
		return workItemLinkBean;
	}

	@Override
	public int compareTo(TWorkItemLinkBean workItemLinkBean) {
		if (workItemLinkBean==null) {
			return 1;
		}
		int compareResult;
		compareResult = SortOrderUtil.compareValue(this.getSortorder(), workItemLinkBean.getSortorder());
		if (compareResult!=0) {
			return compareResult;
		}
		return SortOrderUtil.compareValue(this.getObjectID(), workItemLinkBean.getObjectID());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof TWorkItemLinkBean)) {
			return false;
		}
		if(compareTo((TWorkItemLinkBean)obj) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getSortorder() == null) ? 0 : this.getSortorder().hashCode());
		return result;
	}
}
