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

Ext.define('js.ext.com.track.dashboard.MyItems',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	initComponent : function(){
		var me=this;
		if(me.jsonData.tooManyItems==true){
			me.items=[me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}else{
			var projectID=null;
			if(typeof(me.jsonData.projectID) !== 'undefined' && me.jsonData.projectID != null) {
				projectID=me.jsonData.projectID;
			}
			if(projectID==null){
				me.layout='column';
				me.defaults={
					border:true,
					unstyled: true,
					bodyPadding: 0
				};
			}
			me.items=me.createChildren();
		}
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		var projectID=null;
		if(typeof(me.jsonData.projectID) !== 'undefined' && me.jsonData.projectID != null) {
			projectID=me.jsonData.projectID;
		}
		var items=[];
		if(projectID==null) {
			var myItemsList = me.jsonData.myItemsList;
			if (myItemsList!=null) {
				var columnWith = 1/myItemsList.length;
				if(Ext.isIE9){
					//trick for ie to not put columns below
					columnWith=columnWith-0.002;
				}
				Ext.Array.forEach(myItemsList, function(record, index, allItems) {
					var grid = me.createProjectSet(record["label"], record["totalInRaciRole"],
							record["projectJSON"], record["queryID"]);
					if (index==0) {
						grid.margin = '5 5 5 5';
					} else {
						grid.margin = '5 5 5 0';
					}
					grid.columnWidth=columnWith;
					items.push(grid);
					}, this);
			}
		}else{
			var data=[];
			var number=1;
			Ext.Array.forEach(me.jsonData.projectJSON, function(record, index, allItems) {
				data.push(record);
				if(number<record.numItems){
					number=record.numItems;
				}
			}, this);
			var grid=me.createGrid(data,number);
			grid.border=false;
			grid.bodyBorder=false;
			items=[grid];
		}
		return items;
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	},
	createGrid:function(data,number){
		var me=this;
		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'queryID'},
				{name: 'projName'},
				{name: 'projId',type:'int'},
				{name: 'numItems',type:'int'},
				{name: 'numItemsOverdue',type:'int'},
				{name: 'dlate',type: 'float'},
				{name: 'greenWidth',type: 'int'},
				{name: 'redWidth',type: 'int'}
			],
			data: data
		});
		var numberSize=(number+'').length;
		var charSize=10;
		var nrColumnWidth=numberSize*charSize+12;
		var nrOverdueColumnWidth=numberSize*charSize+2*charSize+12;
		return Ext.create('Ext.grid.Panel', {
			store: store,
			columns: [
				{
					xtype:'linkcolumn',
					text	 :'projName',
					width:150,//flex:1,
					sortable:false,
					menuDisabled:true,
					dataIndex: 'projName',
					//align:'right',
					handler:me.clickOnProjectName,
					scope:me,
					isLink:me.isProjectNameLink,
					postProcessRenderer:me.postProcessRendererProjectName
				},{
					xtype:'linkcolumn',
					text	 :'numItems',
					width	: nrColumnWidth,
					sortable:false,
					menuDisabled:true,
					align:'right',
					dataIndex: 'numItems',
					handler:me.clickOnNumber,
					scope:me
				},{
					xtype:'linkcolumn',
					text	 :'numItemsOverdue',
					clsLink:'synopsis_red',
					width	: nrOverdueColumnWidth,
					sortable:false,
					menuDisabled:true,
					align:'left',
					dataIndex: 'numItemsOverdue',
					handler:me.clickOnNumberOverdue,
					scope:me,
					postProcessRenderer:me.postProcessRendererNumberOverdue
				},{
					text	 :'dlate',
					//width	: 75,
					flex:1,
					align:'right',
					sortable:false,
					menuDisabled:true,
					renderer : me.formatBars,
					dataIndex: 'dlate'
				}
			],
			hideHeaders:true,
			columnLines :false,
			disableSelection: true,
			viewConfig: {
				stripeRows: true,
				trackOver  :false
			},
			autoHeight:true
		});
	},
	createProjectSet:function(title,number,projects,queryID){
		var me=this;
		var data=projects;
		data.unshift({
			queryID:queryID,
			projName: title,
			numItems:number
		});
		return me.createGrid(data,number);
	},
	isProjectNameLink:function(value,metaData,record,rowIndex,colIndex,store,view){
		var me=this;
		return (record.data.greenWidth<=0&&record.data.redWidth<=0);
	},
	postProcessRendererProjectName:function(value,metaData,record,rowIndex,colIndex,store,view){
		if(record.data.greenWidth>0||record.data.redWidth>0){
			return value+":";
		}else{
			return value;
		}
	},
	executeQuery:function(record,cellIndex,projectID, entityFlag,lateOnly){
		var me=this;
		var urlStr='itemNavigator.action';
		var params={
			'queryType':2,//dashboard
			'queryID':me.jsonData.dashboardID,
			'dashboardParams.queryID':record.data.queryID,
			'dashboardParams.dashboardID':me.jsonData.dashboardID
		};
		//overwrite projectID and entity type if dashboard is in browse projects page
		if(me.jsonData.projectID!=null){
			projectID=me.jsonData.projectID;
			entityFlag=me.jsonData.entityType;
		}
		if(me.jsonData!=null&&me.jsonData.releaseID!=null){
			projectID=me.jsonData.releaseID;
			entityFlag=me.jsonData.entityType;
		}
		if(lateOnly==null){
			lateOnly=false;
		}
		if(projectID!=null){
			params['dashboardParams.projectID']=projectID;
			params['dashboardParams.entityFlag']=entityFlag;
			params['dashboardParams.lateOnly']=lateOnly;
		}
		var dummyForm = Ext.create('Ext.form.Panel', {
			items:[],
			url:urlStr,
			standardSubmit:true
		});
		dummyForm.getForm().submit({
			params:params
		});
	},
	clickOnProjectName:function(record,cellIndex){
		var me=this;
		me.executeQuery(record,cellIndex);
	},
	clickOnNumber:function(record,cellIndex){
		var me=this;
		var projectID=null;
		if(record.data.projId!=0){
			projectID=record.data.projId;
		}
		me.executeQuery(record,cellIndex,projectID,1,false);
	},
	clickOnNumberOverdue:function(record,cellIndex){
		var me=this;
		var projectID=null;
		if(record.data.projId!=0){
			projectID=record.data.projId;
		}
		me.executeQuery(record,cellIndex,projectID,1,true);
	},
	postProcessRendererNumberOverdue:function(value,metaData,record,rowIndex,colIndex,store,view){
		if(record.data.numItemsOverdue>0){
			return '(&nbsp;'+value+'&nbsp;)';
		}else{
			return "";
		}
	},
	formatBars:function(value,metaData,record,rowIndex,colIndex,store,view){
		var iconsHtml="<div class='barWrapper'>";
		if(record.data.greenWidth>0||record.data.redWidth>0){
			iconsHtml+='<img width="'+record.data.greenWidth+'" height="10" alt="GreenBar" src="'+
				com.trackplus.TrackplusConfig.iconsPath+'GreenBar.gif"/>';
			if(record.data.dlate>0){
				iconsHtml+='<img width="'+record.data.redWidth+'" height="10" alt="RedBar" src="'+
					com.trackplus.TrackplusConfig.iconsPath+'RedBar.gif"/>';
			}
		}
		iconsHtml += "</div>";
		return iconsHtml;
	}
});
