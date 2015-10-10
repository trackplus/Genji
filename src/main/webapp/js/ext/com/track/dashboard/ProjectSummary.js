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

Ext.define('js.ext.com.track.dashboard.ProjectSummary',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	layout:'anchor',
	defaults:{
		anchor:'100%'
	},
	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		if(me.jsonData.tooManyItems==true){
			return [me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}
		var issueTypeFlag=me.jsonData.issueTypeFlag;
		var projects=me.jsonData.projects;
		var project=me.jsonData.project;
		var myGridData=[];
		if(projects!=null){
			for(var i=0;i<projects.length;i++){
				myGridData.push({
					label:projects[i].label,
					category:true
				});
				myGridData=me.addAll(myGridData,
						com.trackplus.dashboard.updateElementWrapperList(projects[i].list,projects[i]));

			}
		}else{
			if(project!=null){
				myGridData=com.trackplus.dashboard.updateElementWrapperList(project.list,project)
			}
		}
		return [me.createPercentGroup(null,myGridData,me,issueTypeFlag)];
	},

	createPercentGroup: function(title,list,dashboard, issueTypeFlag){
		var gridData=list;
		if(title!=null){
			gridData.unshift({
				label:title,
				h2:true,
				category:true
			});
		}
		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'label'},
				{name: 'number',type:'int'},
				{name: 'width',type:'int'},
				{name: 'percent',type: 'int'},

				{name: 'groupByFieldType',type: 'int'},
				{name: 'projectID',type: 'int',useNull:true},
				{name: 'releaseID',type: 'int',useNull:true},
				{name: 'openOnly',type: 'boolean'},
				{name: 'groupByField',type: 'int'},
				{name: 'icon'},

				{name: 'percentLate',type: 'int'},
				{name: 'category',type: 'boolean'},
				{name: 'h2',type: 'boolean'},
				{name: 'widthLate',type: 'int'}
			],
			data: gridData
		});
		var columns = [];
		if(issueTypeFlag == 1) {
			columns.push({
				xtype:'linkcolumn',
				text	 :'label',
				width:175,
				sortable:false,
				menuDisabled:true,
				dataIndex: 'label',
				align:'right',
				handler:com.trackplus.dashboard.clickOnLabel,
				scope:dashboard,
				isLink:com.trackplus.dashboard.isLabelLink,
				postProcessRenderer:com.trackplus.dashboard.postProcessRendererLabel
			});
		}else {
			columns.push({
				text	 :'label',
				width:175,
				sortable:false,
				menuDisabled:true,
				dataIndex: 'label',
				align:'right',
				scope:dashboard,
			});

		}
		columns.push({
			text	 :'number',
			width	: 70,
			sortable:false,
			menuDisabled:true,
			renderer : com.trackplus.dashboard.formatNumber,
			align:'right',
			dataIndex: 'number'
		});
		columns.push({
			text	 :'',
			width	: 75,
			sortable:false,
			menuDisabled:true,
			renderer : com.trackplus.dashboard.formatBars,
			dataIndex: 'percent'
		});
		columns.push({
			text	 :'',
			width	: 50,
			sortable:false,
			menuDisabled:true,
			renderer : com.trackplus.dashboard.formatPercent,
			dataIndex: 'percent'
		});

		columns.push({
			text	 :'',
			width:10,
			flex:1,
			sortable:false,
			menuDisabled:true
		});

		var grid = Ext.create('Ext.grid.Panel', {
			store: store,
			columns: columns,
			border:false,
			bodyBorder:false,
			hideHeaders:true,
			columnLines :false,
			disableSelection: true,
			viewConfig: {
				stripeRows: true,
				trackOver  :false,
				style:{
					overflow:'hidden'
				},
				getRowClass: function(record, rowIndex, rp, ds){
					var cls="";
					if(record.data['category']==true){//grouping
						cls+="dashboardCategoryPercent";
					}
					return cls;
				}
			},
			autoHeight:true
		});
		return grid;
	},

	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	}
});
