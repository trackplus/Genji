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

Ext.define('js.ext.com.track.dashboard.MyFilters',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	bodyPadding:'0 0 0 0',
	initComponent : function(){
		var me=this;
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		me.store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'id'},
				{name: 'label'}
			],
			data:me.jsonData.tqlList
		});
		var grid = Ext.create('Ext.grid.Panel', {
			border:false,
			bodyBorder:false,
			store: me.store,
			margin:'0 0 0 0',
			columns: [{
				text	 :'label',
				flex:1,
				sortable:false,
				menuDisabled:true,
				dataIndex: 'label',
				renderer : me.formatLabel,
				scope:me
			}],
			hideHeaders:true,
			columnLines :false,
			disableSelection: true,
			viewConfig: {
				stripeRows: true
			},
			autoHeight:true
		});
		return [grid];
	},
	formatLabel:function(value,metaData,record,rowIndex,colIndex,store,view){
		var me=this;
		var urlStr='itemNavigator.action';
		var projectID=null;
		var entityFlag=null;
		if(me.jsonData.projectID!=null){
			projectID=me.jsonData.projectID;
			entityFlag=me.jsonData.entityType;
		}
		if(me.jsonData!=null&&me.jsonData.releaseID!=null){
			projectID=me.jsonData.releaseID;
			entityFlag=me.jsonData.entityType;
		}
		if(projectID!=null){
			urlStr+="?queryType=2";//dashboard
			urlStr+='&queryID='+me.jsonData.dashboardID;
			urlStr+='&dashboardParams.queryID='+record.data.id;
			urlStr+='&dashboardParams.dashboardID='+me.jsonData.dashboardID;
			urlStr+='&dashboardParams.projectID='+projectID;
			urlStr+='&dashboardParams.entityFlag='+entityFlag;
		} else {
			//if not project/release specific execute as QUERY_TYPE.SAVED (as last executed it will be the Filter symbol instead of Dashboard symbol)
			//because only saved filters enforce the coloring by style field
			urlStr+="?queryType=1",//saved query
			urlStr+='&queryID='+record.data.id;
		}
		return '<a class="synopsis_blue" href="'+urlStr+'">'+record.data.label+'</a>';
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.store.loadData(me.jsonData.tqlList);
	}
});
