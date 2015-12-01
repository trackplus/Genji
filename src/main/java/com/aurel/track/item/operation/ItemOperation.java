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

package com.aurel.track.item.operation;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.report.execute.ReportBean;

/**
 * Operation used for item Navigator left part
 * @author Adrian Bojani
 */
public interface ItemOperation {
	/**
	 * The id of operation
	 * @return
	 */
	public String getPluginID();

	/**
	 * Execute operation for items
	 *
	 * @param workItemIds
	 * @param nodeObjectID
	 * @param params
	 * @param personID
	 *@param locale @throws ItemOperationException
	 */
	public void execute(int[] workItemIds, Integer nodeObjectID, Map<String, String> params, Integer personID, Locale locale) throws ItemOperationException;
	public boolean canDrop(int[] workItemIds,Integer nodeObjectID);
	public List<MenuItem> getChildren(TPersonBean personBean, Locale locale, Integer nodeID);
	public List<ReportBean> filter(List<ReportBean> reportBeans, Integer nodeObjectID);
	public String getName();
	public String getIcon();
	public String getIconCls();
}
