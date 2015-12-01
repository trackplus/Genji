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

Ext.define('js.ext.com.track.dashboard.MyWatch',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	mixins:{
		navigable:'com.trackplus.itemNavigator.GridNavigableItem'
	},
	constructor: function(config){
		this.callParent(arguments);
		this.mixins.navigable.constructor.call(this);
	},
	initComponent : function(){
		var me=this;
		var items=me.jsonData.items;
		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'objectID',type:'int'},
				{name: 'synopsis'},
				{name: 'unwatch'},
				{name: 'project'},
				{name: 'state'},
				{name: 'projectID',  type: 'int'},
				{name: 'stateID',  type: 'int'}
			],
			data: items
		});
		me.grid = Ext.create('Ext.grid.Panel', {
			id:'myWatchGrid_'+me.jsonData.dashboardID,
			cls:'dashboardGrid-noBottomBorder',
			store: store,
			columns: [
				{
					text	 : 'objectID',
					width	 : 75,
					sortable : true,
					dataIndex: 'objectID'
				},{
					text	 :'synopsis',
					flex	: 1,
					sortable : true,
					dataIndex: 'synopsis'
				},{
					text	 :'state',
					width	: 165,
					sortable : true,
					dataIndex: 'stateID',
					renderer : me.formatState
				},{
					text	 :'state',
					width	: 150,
					sortable : true,
					renderer : me.formatUnWatch
				}
			],
			hideHeaders:true,
			bodyBorder:false,
			border:false,
			columnLines :true,
			region:'center',
			margins: '0 0 0 0',
			lines :true,
			viewConfig: {
				stripeRows: true
			}
		});
		me.items=[me.grid];
		me.grid.addListener('celldblclick',me.onGridItemDblClick,me);
		me.callParent();
	},
	onGridItemDblClick:function(view, td,cellIndex,record, tr, rowIndex,e){
		var me=this;
		var data=record.data;
		var workItemID=data['objectID'];
		me.openItem.call(me,workItemID,-2,rowIndex);
	},
	itemChangeHandler:function(fields){
		//no refresh
	},

	createParamsMap:function() {
		var me=this;
		var params={};
		if(arguments&&arguments.length>0){
			params['params.itemID']=arguments[0];
		}
		return params;
	},
	formatState:function(value,metaData,record,rowIndex,colIndex,store,view){
		return record.data.state;
	},
	formatUnWatch:function(value,metaData,record,rowIndex,colIndex,store,view){
		var me=this;
		var dashboardID=me.id.substring("myWatchGrid_".length);
		var unwatch = record.data.unwatch;
		if (unwatch) {
			return '<a href="javaScript:com.trackplus.dashboard.reload('+dashboardID+","+record.data.objectID+')" class="synopsis_blue">'+
				getText('myWatch.unwatch')+'</a>';
		} else {
			return "";
		}
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		var items=me.jsonData.items;
		me.grid.getStore().loadData(items);
	}
});
