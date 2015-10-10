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

package com.aurel.track.plugin;

/**
 *
 */
public class IssueListViewDescriptor extends BasePluginDescriptor {
	public static final long serialVersionUID = 400L;

	public static final String SIMPLE_VIEW = "com.trackplus.itemNavigator.FlatGridViewPlugin";//"com.trackplus.itemNavigator.SimpleTreeGridViewPlugin";
	public static final String COMPLEX_VIEW = "com.trackplus.itemNavigator.SimpleTreeGridViewPlugin";//"com.trackplus.itemNavigator.AdvancedGridViewPlugin";
	public static final String WBS = "com.trackplus.itemNavigator.WBSViewPlugin";
	public static final String GANTT = "com.trackplus.itemNavigator.GanttViewPlugin";
	public static final String CARD = "com.trackplus.itemNavigator.CardViewPlugin";
	
	private String iconCls;
	private boolean enabledColumnChoose=true;
	private boolean enabledGrouping=true;
	private boolean useLongFields=false;
	private boolean plainData=false;
	private boolean gantt= false;

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isEnabledColumnChoose() {
		return enabledColumnChoose;
	}

	public void setEnabledColumnChoose(boolean enabledColumnChoose) {
		this.enabledColumnChoose = enabledColumnChoose;
	}

	public boolean isEnabledGrouping() {
		return enabledGrouping;
	}

	public void setEnabledGrouping(boolean enabledGrouping) {
		this.enabledGrouping = enabledGrouping;
	}

	public boolean isUseLongFields() {
		return useLongFields;
	}

	public void setUseLongFields(boolean useLongFields) {
		this.useLongFields = useLongFields;
	}

	public boolean isPlainData() {
		return plainData;
	}

	public void setPlainData(boolean plainData) {
		this.plainData = plainData;
	}

	public boolean isGantt() {
		return gantt;
	}

	public void setGantt(boolean gantt) {
		this.gantt = gantt;
	}
	
	
}
