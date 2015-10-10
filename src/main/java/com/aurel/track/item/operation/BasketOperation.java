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

package com.aurel.track.item.operation;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.itemNavigator.navigator.MenuItem;
import com.aurel.track.report.execute.ReportBean;
import com.aurel.track.resources.LocalizeUtil;

public class BasketOperation implements ItemOperation {
	public static final String PLUGIN_ID="BasketOperation";
	private List<MenuItem> children;
	private Integer personID;
	private String name;
	public BasketOperation(TPersonBean personBean,Locale locale){
		this.personID=personBean.getObjectID();
		this.children=BasketBL.computeChildren(personBean, locale);
		this.name=LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.basket", locale);
	}
	public String getPluginID() {
		return "BasketOperation";
	}

	public void execute(int[] workItems, Integer nodeObjectID, Map<String, String> params, Integer personID, Locale locale) throws ItemOperationException {
		BasketBL.replaceBasket(workItems, personID, nodeObjectID,params,locale);
	}

	public boolean canDrop(int[] workItemIds, Integer nodeObjectID) {
		return BasketBL.canDrop(workItemIds,nodeObjectID,this.personID);
	}

	public List<MenuItem> getChildren(TPersonBean personBean, Locale locale, Integer nodeID) {
		return children;
	}

	public List<ReportBean> filter(List<ReportBean> reportBeans, Integer nodeObjectID) {
		return BasketBL.filter(reportBeans,nodeObjectID,this.personID);
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return null;
	}

	public String getIconCls() {
		return BasketBL.ICON_CLS.BASKET_ICON;
	}
}
