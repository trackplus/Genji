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

Ext.define('js.ext.com.track.dashboard.Meetings',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	initComponent : function(){
		var me=this;
		var meetings=me.jsonData.meetings;
		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'date'},
				{name: 'synopsis'},
				{name: 'linkOpen'},
				{name: 'linkResolved'},
				{name: 'linkAll'},
				{name: 'linkReportMeeting'},
				{name: 'objectID',  type: 'int'},
				{name: 'number',  type: 'int'},
				{name: 'numberResolved',  type: 'int'},
				{name: 'numberOpen',  type: 'int'},
				{name: 'widthResolved',  type: 'int'},
				{name: 'widthOpen',  type: 'int'}
			],
			data: meetings
		});
		me.grid = Ext.create('Ext.grid.Panel', {
			store: store,
			columns: [
				{
					text	 : getText('common.history.lbl.lastEdit'),
					width	 : 75,
					sortable : true,
					dataIndex: 'date'
				},{
					text	 :getText('field.label17'),
					flex	: 1,
					sortable : true,
					dataIndex: 'synopsis'
				},{
					text	 :getText('meetings.all')+' ('+getText('releaseNotes.resolved')+'/'+getText('releaseNotes.unresolved')+')',
					width	: 165,
					sortable : true,
					dataIndex: 'number',
					renderer : me.formatNumbers,
					scope:me
				}
			],
			border:false,
			bodyBorder:false,
			columnLines :true,
			region:'center',
			margins: '0 0 0 0',
			lines :true,
			viewConfig: {
				stripeRows: true
			}
		});
		me.items=[me.grid];
		me.callParent();
	},
	formatNumbers:function(value,metaData,record,rowIndex,colIndex,store,view){
		var me=this;
		var baseURL=com.trackplus.TrackplusConfig.contextPath+'/itemNavigator.action?queryType=2';
		baseURL+='&queryID='+me.jsonData.dashboardID;
		baseURL+='&dashboardParams.dashboardID='+me.jsonData.dashboardID;
		var projectID=null;
		var entityFlag=null;
		//overwrite projectID and entity type if dashboard is in browse projects page
		if(me.jsonData&&me.jsonData.projectID){
			projectID=me.jsonData.projectID;
			entityFlag=me.jsonData.entityType;
			baseURL+='&dashboardParams.projectID='+projectID;
			baseURL+='&dashboardParams.entityFlag='+entityFlag;
		}
		if(me.jsonData&&me.jsonData.releaseID){
			projectID=me.jsonData.releaseID;
			entityFlag=me.jsonData.entityType;
			baseURL+='&dashboardParams.projectID='+releaseID;
			baseURL+='&dashboardParams.entityFlag='+entityFlag;
		}
		var issue=record.data['objectID'];
		baseURL+='&dashboardParams.issue='+issue;

		var urlNrO=baseURL;
		urlNrO+='&dashboardParams.resolved='+false;
		urlNrO+='&dashboardParams.showAll='+false;
		urlNrO+='&dashboardParams.reportMeeting='+false;

		//true, false,false
		var urlNrR=baseURL;
		urlNrR+='&dashboardParams.resolved='+true;
		urlNrR+='&dashboardParams.showAll='+false;
		urlNrR+='&dashboardParams.reportMeeting='+false;

		//false, true,false
		var urlNrA=baseURL;
		urlNrA+='&dashboardParams.resolved='+false;
		urlNrA+='&dashboardParams.showAll='+true;
		urlNrA+='&dashboardParams.reportMeeting='+false;

		var numbersStr='<a href="'+urlNrA+'" class="synopsis_blue">'+record.data.number+'</a>';
		numbersStr+=" ( ";
		if(record.data.numberResolved>0){
			numbersStr+='<a href="'+urlNrR+'" class="synopsis_blue">'+record.data.numberResolved+'</a>';
		}else{
			numbersStr+=record.data.numberResolved
		}
		numbersStr+=' / <a href="'+urlNrO+'" class="synopsis_blue">'+record.data.numberOpen+'</a>'+' ) ';
		var imgStr='<div class="barWrapper"><img src="'+com.trackplus.TrackplusConfig.iconsPath+'GreenBar.gif" '+
			'width="'+record.data.widthResolved+'" height="10" alt="GreenBar">'+
			'<img src="'+com.trackplus.TrackplusConfig.iconsPath+'LightBlueBar.gif" '+
			'width="'+record.data.widthOpen+'" height="10" alt="LightBlueBar"></div>';
		return '<table cellpadding="0" cellspacing="0" border="0" style="width:100%;"><tr><td>'+numbersStr+'</td><td width="65">'+imgStr+'</td></tr></table>';
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		var meetings=me.jsonData.meetings;
		me.grid.getStore().loadData(meetings);
	}
});
