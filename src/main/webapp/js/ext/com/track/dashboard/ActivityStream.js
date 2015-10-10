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

Ext.define('js.ext.com.track.dashboard.ActivityStream',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	mixins:{
		navigable:'com.trackplus.itemNavigator.GridNavigableItem'
	},
	layout:'anchor',
	gridView:false,
	grid:null,
	usePosition:true,
	constructor: function(config){
		this.callParent(arguments);
		this.mixins.navigable.constructor.call(this);
	},
	getFieldNameItemID:function(){
		return 'itemID';
	},
	initComponent : function(){
		var me=this;
		if(me.jsonData.tooManyItems==true){
			me.items=[me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}else{
			me.gridView=me.jsonData.gridView;
			if(me.gridView==true){
				me.grid=me.createGridView();
				me.items=[me.grid];
			}else{
				me.plainView=me.createPlainView();
				me.items=[me.plainView];
				me.addListener('afterrender',me.refreshPlainView,me);
			}
		}
		me.callParent();
		me.itemDialogManager=Ext.create('com.trackplus.itemNavigator.ItemDialogManager',{
			navigable:me
		});
	},
	refreshErrorHandler:function(){
		var me=this;
		me.callParent(arguments);
		me.gridView=null;
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		var gridView=jsonData.gridView;
		if(me.gridView!=gridView){
			me.removeAll(true);
			if(gridView){
				me.grid=me.createGridView();
				me.add(me.grid);
			}else{
				me.plainView=me.createPlainView();
				me.add(me.plainView);
			}
		}
		me.gridView=gridView;
		if(gridView){
			var items=me.jsonData.items;
			me.grid.getStore().loadData(items);
		}else{
			me.refreshPlainView();
		}
	},
	refreshPlainView:function(){
		var me=this;
		var urlStr='flatActivity.action';
		var params={
			dashboardID:me.jsonData.dashboardID,
			projectID:me.jsonData.projectID,
			entityType:me.jsonData.entityType,
			releaseID:me.jsonData.releaseID
		};
		me.plainView.setLoading(true);
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
            timeout: 300000,     //300 seconds
            success: function(response){
				//var responseJson = Ext.decode(response.responseText);
				//var jsonData=responseJson;
				me.flatData=response.responseText;
				me.plainView.setLoading(false);
				me.plainView.update(me.flatData);
			},
			params:params,
			    failure: function(){
                    me.plainView.setLoading(false);
			},
			method:'POST'
		});
	},
	createPlainView:function(){
		var me=this;
		var plainView=Ext.create('Ext.Component',{
			border:false,
			cls:'flatHistoryPanel'
		});
		return plainView;
	},
	createGridView:function(){
		var me=this;
		var items=me.jsonData.items;
		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'date',type: 'date', dateFormat: com.trackplus.itemDetail.dateTimeIsoFormat},
				{name: 'dateFormatted'},
				{name: 'itemID',type:'int'},
				{name: 'itemPrefixID'},
				{name: 'itemTitle'},
				{name: 'changeByName'},
				{name: 'changes'},
				{name: 'transactionID',  type: 'int'},
				{name: 'projectID',  type: 'int'},
				{name: 'project'}
			],
			data: items
		});
		var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
			groupHeaderTpl: '{name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})'
		});
		var grid = Ext.create('Ext.grid.Panel', {
			anchor:'100%',
			store: store,
			features:[groupingFeature],
			columns: [
				{
					text	 : getText('common.lbl.date'),
					width	 : 115,
					sortable : true,
					dataIndex: 'dateFormatted',
					draggable :false,
					hideable:false
				},{
					text	 :getText('field.label1'),
					width	 : 120,
					sortable : true,
					dataIndex: 'project',
					draggable :false,
					hideable:false
				},{
					text	 :getText('field.label12'),
					width	 : 70,
					sortable : true,
					dataIndex: 'itemPrefixID',
					draggable :false,
					hideable:false,
					groupable:false,
					menuDisabled:true
				},{
					text	 :getText('field.label17'),
					width 	 : 250,
					sortable : true,
					dataIndex: 'itemID',
					renderer:me.formatItemTitle,
					draggable :false,
					hideable:false,
					groupable:false,
					menuDisabled:true
				},{
					text	 :getText('common.history.lbl.changedBy'),
					width	: 175,
					sortable : true,
					dataIndex: 'changeByName',
					draggable :false,
					hideable:false
				},{
					text	 :getText('common.history.lbl.typeOfChange'),
					flex	: 1,
					sortable : true,
					dataIndex:'changes',
					draggable :false,
					hideable:false,
					groupable:false,
					menuDisabled:true
				}
			],
			bodyBorder:false,
			hideHeaders:false,
			border:false,
			columnLines :true,
			margins: '0 0 0 0',
			lines :true,
			viewConfig: {
				stripeRows: true
			}
		});
		grid.addListener('celldblclick',me.onGridItemDblClick,me);
		return grid;
	},
	onGridItemDblClick:function(view, td,cellIndex,record, tr, rowIndex,e){
		var me=this;
		var data=record.data;
		var workItemID=data['itemID'];
		me.openItem.call(me,workItemID,-2,rowIndex);
	},
	successHandler:function(data){
		this.refreshDashboard(data);
	},
	formatItemTitle:function(value,metaData,record,rowIndex,colIndex,store,view){
		return '<a href="printItem.action?key='+record.data.itemID+'" target="printItem'+record.data.itemID+'"  class="synopsis_blue">'+
			 record.data.itemTitle+'</a>';
	}
});
