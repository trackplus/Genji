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

Ext.define('js.ext.com.track.dashboard.ReportsAndFilters',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	layout:'anchor',
	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	defaults:{
		anchor:'100%'
	},
	createChildren:function(){
		var me=this;
		if(me.jsonData.tooManyItems==true){
			return [me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}
		var items=[];
		var gridData=[];
		if(me.jsonData.selectedReports!=null&&me.jsonData.selectedReports.length>0){
			var reportsData=me.jsonData.selectedReports;
			gridData.push({id:null,report:false,label:getText('reportsAndFilters.reports')});
			for(var i=0;i<reportsData.length;i++){
				gridData.push({
					id:"r_"+reportsData[i].id,//for reports prefix with "r_" to avoid collision with filters id
					label:reportsData[i].label,
					reportConfigNeeded:reportsData[i].reportConfigNeeded,
					projectID:reportsData[i].projectID,
					entityFlag:reportsData[i].entityFlag,
					report:true
				});
			}
		}
		if(me.jsonData.filters!=null&&me.jsonData.filters.length>0){
			var filtersData=me.jsonData.filters;
			gridData.push({id:null,report:false,label:getText('reportsAndFilters.filters')});
			gridData=me.addAll(gridData,filtersData);
		}
		if(gridData.length>0){
			items.push(me.createReportsAndFiltersGrid(gridData));
		}
		if(me.jsonData.results!=null&&me.jsonData.results.length>0){
			items.push(me.createOpenedIssuesPanel(me.jsonData));
		}
		return items;
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	},
	createReportsAndFiltersGrid:function(data,localized){
		var me=this;
		me.store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'id'},
				{name: 'label', type:'string'},
				{name:'report',type:'boolean'},
				{name:'reportConfigNeeded',type:'boolean'},
				{name:'projectID',type:'int'}/*,
				{name:'entityFlag',type:'int'}*/
			],
			data:data
		});
		return Ext.create('Ext.grid.Panel', {
			border:false,
			bodyBorder:false,
			store: me.store,
			margin:'0 0 0 0',
			columns: [{
				xtype:'linkcolumn',
				text :'label',
				flex:1,
				sortable:false,
				menuDisabled:true,
				dataIndex: 'label',
				handler:me.clickOnLabel,
				scope:me,
				isLink:me.isLabelLink,
				postProcessRenderer:me.postProcessRendererLabel
			}],
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
	isLabelLink:function(value,metaData,record,rowIndex,colIndex,store,view){
		return (record.data.id!=null);
	},
	postProcessRendererLabel:function(value,metaData,record,rowIndex,colIndex,store,view){
		var htmlStr='';
		if(record.data.id==null){
			htmlStr+='<h2>'+value+'</h2>';
		}else{
			htmlStr=value;
		}
		return htmlStr;
	},

	clickOnLabel:function(record,cellIndex){
		var me=this;
		var report=record.data['report'];
		var recordID=record.data['id'];
		var urlStr='';
		var params=null;
		var projectID=null;
		var entityFlag=null;
		var reportProjectOrRelease = record.data["projectID"];
		//overwrite projectID and entity type if dashboard is in browse projects page
		if(me.jsonData!=null&&me.jsonData.projectID!=null){
			projectID=me.jsonData.projectID;
			entityFlag=me.jsonData.entityType;
			reportProjectOrRelease = -projectID;
		}
		if(me.jsonData!=null&&me.jsonData.releaseID!=null){
			projectID=me.jsonData.releaseID;
			entityFlag=me.jsonData.entityType;
			reportProjectOrRelease = projectID;
		}
		if(report===true){
			//the report id starts with "r_";
			recordID=recordID.substring(2);
			com.trackplus.admin.Report.executeReport(this, recordID,
					record.data["reportConfigNeeded"], false, null, reportProjectOrRelease, me.jsonData.dashboardID);
			return true;
		}else{
			urlStr='itemNavigator.action';
			params={
				'queryType':2,//dashboard
				'queryID':me.jsonData.dashboardID,
				'dashboardParams.dashboardID':me.jsonData.dashboardID
			};
			if(projectID!=null){
				params['dashboardParams.projectID']=projectID;
				params['dashboardParams.entityFlag']=entityFlag;
			}

			params['dashboardParams.filterIdentifier']=recordID;
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
	createOpenedIssuesPanel:function(data){
		var me=this;
		var myGridData=[];
		myGridData.push({
			label:getText('reportsAndFilters.openStatus'),
			category:true
		});
		myGridData=me.addAll(myGridData,
					com.trackplus.dashboard.updateElementWrapperList(data.results[0].list,data.results[0]));
		myGridData.push({
			label:getText('reportsAndFilters.openPriority'),
			category:true
		});
		myGridData=me.addAll(myGridData,
					com.trackplus.dashboard.updateElementWrapperList(data.resultp[0].list,data.resultp[0]));
		myGridData.push({
			label:getText('reportsAndFilters.openResponsible'),
			category:true
		});
		myGridData=me.addAll(myGridData,
					com.trackplus.dashboard.updateElementWrapperList(data.resultr[0].list,data.resultr[0]));
		return com.trackplus.dashboard.createPercentGroup(
			getText('reportsAndFilters.open')+' '+data.release,myGridData,me);
	}
});
