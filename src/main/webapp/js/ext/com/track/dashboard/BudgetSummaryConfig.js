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

Ext.define('js.ext.com.track.dashboard.BudgetSummaryConfig',{
	extend: 'js.ext.com.track.dashboard.BaseConfig',
	cmbReports:null,
	layout:{
		type:'column'
	},
	autoScroll:true,
	//bodyPadding: 0,
	createChildren:function(){
		var me=this;
		var items=me.callParent();
		var projectID=null;
		if(typeof(me.projectID) !== 'undefined' && me.projectID != null) {
			projectID=me.projectID;
		}
		var entityType=null;
		if(typeof(me.entityType) !== 'undefined' && me.entityType != null) {
			entityType=me.entityType;
		}
		if(projectID===null){
			var selectedProjectReleases=null;
			if(me.jsonData.selectedProjects!=null&&me.jsonData.selectedProjects.length>0){
				selectedProjectReleases=me.jsonData.selectedProjects.join();
			}
			var releaseTreeObject=com.trackplus.dashboard.createReleaseTreeCfg("budgetSummary.prompt.projectsAndReleases","params.selectedProjects",me.jsonData.selectedProjects,
			{/*height:125,*/labelAlign:'left',labelWidth:100, margin:'0 0 5 0'});
			items.push(releaseTreeObject);
		}

		me.cmbGroupBy=com.trackplus.dashboard.createSelectConfig(getText('budgetSummary.prompt.groupBy'),
			'params.selectedGrouping',me.jsonData.grouping,me.jsonData.selectedGrouping);
		items.push(me.cmbGroupBy);

		me.cmbFilterByState=com.trackplus.dashboard.createMultiSelectConfig(getText('budgetSummary.prompt.filterByState'),
			'params.selectedStates',me.jsonData.states,me.jsonData.selectedStates);
		items.push(me.cmbFilterByState);


		me.cmbFilterByPriority=com.trackplus.dashboard.createMultiSelectConfig(getText('budgetSummary.prompt.filterByPriority'),
			'params.selectedPriorities',me.jsonData.priorities,me.jsonData.selectedPriorities);
		items.push(me.cmbFilterByPriority);

		var panelW=Ext.create('Ext.panel.Panel',{
			columnWidth: .50,
			border: false,
			margin: '0 0 0 0',
			frame: false,
			collapsible:false,
			bodyPadding: 0,
			padding:0,
			layout:'anchor',
			defaults:{
				anchor:'100%'
			},
			items:items
		});
		var htmlStr="";
		htmlStr+='<div class="motd">';
		if(projectID==null){
			htmlStr+='<b>'+getText('budgetSummary.prompt.projectsAndReleases')+'</b>';
			htmlStr+='<ul><li>'+getText('budgetSummary.description.projectsAndReleases')+'</li></ul>';
		}

		htmlStr+='<b>'+getText('budgetSummary.prompt.groupBy')+'</b><ul>';
		if(projectID==null){
			htmlStr+='<li>'+getText('budgetSummary.description.projectAndRelease')+'</li>';
		}else{
			if(entityType==null||entityType==1){
				//project config
				htmlStr+='<li>'+getText('budgetSummary.description.release')+'</li>';
			}
		}
		htmlStr+='<li>'+getText('budgetSummary.description.state')+'</li>';
		htmlStr+='<li>'+getText('budgetSummary.description.priority')+'</li>';
		htmlStr+='<li>'+getText('budgetSummary.description.none')+'</li></ul>';

		htmlStr+='<b>'+getText('budgetSummary.prompt.filterByState')+'</b>';
		htmlStr+='<ul><li>'+getText('budgetSummary.description.filterByState')+'</li></ul>';

		htmlStr+='<b>'+getText('budgetSummary.prompt.filterByPriority')+'</b>';
		htmlStr+='<ul><li>'+getText('budgetSummary.description.filterByPriority')+'</li></ul>';
		htmlStr+='</div>';
		var panelE={
			xtype: 'component',
			cls: 'infoBox',
			border:true,
			columnWidth: .50,
			margin: '0 0 0 5',
			pading:'0 0 0 0',
			html:htmlStr
		};
		return [panelW,panelE];
	}
});
