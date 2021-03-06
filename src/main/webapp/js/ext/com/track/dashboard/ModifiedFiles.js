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

Ext.define('js.ext.com.track.dashboard.ModifiedFiles',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	initComponent : function(){
		var me=this;
		var items=me.jsonData.files;
		var store = Ext.create('Ext.data.Store', {
			fields: [
				{name: 'type'},
				{name: 'name'},
				{name: 'link'}
			],
			data: items
		});
		me.grid = Ext.create('Ext.grid.Panel', {
			store: store,
			columns: [
				{
					text	 : 'type',
					sortable : true,
					dataIndex: 'type'
				},{
					text	 :'link',
					flex	: 1,
					sortable : true,
					dataIndex:'name'
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
		me.callParent();
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		var items=me.jsonData.files;
		me.grid.getStore().loadData(items);
	}
});
