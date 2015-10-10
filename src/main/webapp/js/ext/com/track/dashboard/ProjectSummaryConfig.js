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

Ext.define('js.ext.com.track.dashboard.ProjectSummaryConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	cmbProjects:null,
	cmbGroupBy:null,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		var projectID=null;
		if(typeof(me.projectID) !== 'undefined' && me.projectID != null) {
			projectID=me.projectID;
		}

		var issueTypeSelector = me.getIssueTypeSelector();
		if(me.jsonData.userHasWiki) {
			items.push(issueTypeSelector);
		}

		if(projectID==null){
			me.cmbProjects=com.trackplus.dashboard.createReleaseTreeCfg("releaseNotes.projects",
				"params.SelectedProjects",me.jsonData.SelectedProjects, {width:400, margin:'0 0 5 0'});
			items.push(me.cmbProjects);
		}
		me.cmbGroupBy=com.trackplus.dashboard.createSelectConfig(getText('projectSummary.groupBy'),
				'params.selectedGroupByField',me.jsonData.groupByFields,me.jsonData.selectedGroupByField);

		items.push(me.cmbGroupBy);
		return items;
	},

	getIssueTypeSelector: function() {
		var me = this;
		timeUnitSelector = {
			xtype: 'fieldcontainer',
			fieldLabel: getText('projectSummary.prompt.issuType'),
			defaultType: 'radiofield',
	        labelAlign: me.labelAlign,
			labelStyle: me.labelStyle,
			labelSeparator: me.labelSeparator,
			width: 315,
	        defaults: {
	        	flex: 1
	        },
	        layout: 'hbox',
	        items: [{
	        	  boxLabel: getText('projectSummary.prompt.issuType.general'),
	        	  name: 'params.issueType',
	        	  inputValue: 1,
	        	  id: 'radio2',
	        	  checked: me.jsonData.selectedIssueType == 1

	          },{
	        	boxLabel: getText('projectSummary.prompt.issuType.document'),
	        	name: 'params.issueType',
	        	inputValue: 2,
	            id: 'radio1',
	            checked: me.jsonData.selectedIssueType == 2
	          }]
		};
		return timeUnitSelector;
	}
});
