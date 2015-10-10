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

Ext.define('com.trackplus.admin.ICalendarURL',{
	extend:'Ext.Base',
	config:{
		data:{}
	},
	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	view:null,
	btnGenerate:null,
	btnDownload:null,
	tree:null,
	createView:function(){
		var me=this;
		var store = Ext.create('Ext.data.TreeStore', {
			proxy: {
				type: 'ajax',
				url: 'projectPicker.action',
				extraParams: {
					useChecked: true
				}
			}
		});
		me.tree = Ext.create('Ext.tree.Panel', {
			store: store,
			rootVisible: false,
			useArrows: true,
			margin:'10 10 10 10',
			frame: true,
			title: getText('admin.project.lbl.project'),
			width: 300,
			height: 200
		});
		var infoBox={
			xtype: 'component',
			cls: 'infoBox_bottomBorder',
			border:true,
			html: getText('admin.myprefs.iCalendar.defaultUrlMessage')
		};
		me.boxURL=Ext.create('Ext.Component',{
			border:true,
			html:'...',
			margin:'10 0 0 10',
			anchor:'100%',
			hidden:true
		});

		me.view=Ext.create('Ext.panel.Panel',{
			layout:'anchor',
			margin:'0 0 0 0',
			border:false,
			bodyBorder:false,
			items:[infoBox,me.tree,me.boxURL]
		});
		return me.view;
	},
	getToolbarButtons:function(){
		var me=this;
		if(me.btnGenerate==null){
			me.btnGenerate=Ext.create('Ext.button.Button',{
				overflowText:getText('common.btn.generateUrl'),
				tooltip:getText('common.btn.generateUrl'),
				text: getText('common.btn.generateUrl'),
				iconCls: 'save',
				disabled:false,
				handler:function(){
					me.generateURL.call(me);
				}
			});
			me.btnDownload=Ext.create('Ext.button.Button',{
				overflowText:getText('common.btn.download'),
				tooltip:getText('common.btn.download'),
				text: getText('common.btn.download'),
				iconCls: 'download',
				disabled:false,
				handler:function(){
					me.download();
				}
			});
		}
		return [me.btnGenerate];
	},
	generateURL:function(){
		var me=this;
		if(me.tree==null){
			return false;
		}
		var records = me.tree.getView().getChecked();
		if(records==null||records.length==0){
			Ext.MessageBox.show({
				title:getText('admin.myprefs.iCalendar.title'),
				msg: getText('common.err.required',getText('admin.project.lbl.projectForOp')),
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.ERROR
			});
			return false;
		}
		var selectedProjects="";
		for(var i=0;i<records.length;i++){
			selectedProjects+=records[i].data.id;
			if(i<records.length-1){
				selectedProjects+='-';
			}
		}
		me.view.setLoading(true);
		Ext.Ajax.request({
			url: "iCalendar!generateURL.action",
			params:{
				selectedProjects:selectedProjects,
				allProjects:false
			},
			success: function(response){
				var jsonData=Ext.decode(response.responseText);
				me.boxURL.update(jsonData.data);
				me.boxURL.setVisible(true);
				me.view.setLoading(false);
			},
			failure:function(response){
				alert("failure");
				me.view.setLoading(false);
			}
		});
	},
	download:function(){
		alert("download");
	}
});
