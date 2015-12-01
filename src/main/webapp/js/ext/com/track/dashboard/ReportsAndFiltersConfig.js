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

Ext.define('js.ext.com.track.dashboard.ReportsAndFiltersConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	cmbProjects:null,
	reportsTree:null,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		var projectID=null;
		if(typeof(me.projectID) !== 'undefined' && me.projectID ) {
			projectID=me.projectID;
		}
		if(CWHF.isNull(projectID)){
			me.cmbProjects=com.trackplus.dashboard.createReleasePickerCfg("reportsAndFilters.project",
				'params.selectedProjectOrRelease',me.jsonData.selectedProjectOrRelease);

			items.push(me.cmbProjects);
		}
		/*me.cmbReports=com.trackplus.dashboard.createMultiSelectConfig(getText('reportsAndFilters.reports'),
			'params.selectedReports',me.jsonData.reports,me.jsonData.selectedReports,{width:400});*/
		me.reportsTree = com.trackplus.dashboard.createReportTreeCfg("reportsAndFilters.reports",
				"params.selectedReports",me.jsonData.selectedReports,projectID);
		items.push(me.reportsTree);
		return items;
	}
});
