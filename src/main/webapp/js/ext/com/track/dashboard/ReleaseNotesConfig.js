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

Ext.define('js.ext.com.track.dashboard.ReleaseNotesConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	cmbProjects:null,
	cmbIssueTypes:null,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		var projectID=null;
		if(typeof(me.projectID) !== 'undefined' && me.projectID ) {
			projectID=me.projectID;
		}
		if(CWHF.isNull(projectID)){
			me.cmbProjects=com.trackplus.dashboard.createReleaseTreeCfg("releaseNotes.projects",
				"params.SelectedProjects",me.jsonData.selectedProjects, {width:400, margin:'0 0 5 0'});
			items.push(me.cmbProjects);
		}

		me.cmbIssueTypes=com.trackplus.dashboard.createMultiSelectConfig(getText('releaseNotes.issueTypes'),
			'params.selectedIssueTypes',me.jsonData.issueTypes,me.jsonData.selectedIssueTypes);

		items.push(me.cmbIssueTypes);
		return items;
	}
});
