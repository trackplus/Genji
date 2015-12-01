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

Ext.define('com.trackplus.admin.server.LoggingConfig',{
	extend:'Ext.Base',
	constructor: function(config) {
	},

	grid:null,
	storeLevel:null,
	cmbLevel:null,
	filter:null,

	jsonData:null,
	levelsMap:null,

	createMainComponent:function(){
		var me=this;

		var store = Ext.create('Ext.data.Store', {
			autoLoad: false,
			fields: [
			         {name:'className', type:'string'},
			         {name:'level', type:'string'},
			         {name:'value', type:'int'}
			         ],
			         groupField: 'level',
			         data:[]
		});

		me.storeLevel=Ext.create('Ext.data.Store', {
			autoLoad: false,
			fields: [{name:'id',type:'int'},{name:'label',type:'string'}],
			data:[]
		});


		me.cmbValue=Ext.create('Ext.form.ComboBox', {
			store: me.storeLevel,
			name: name,
			queryMode: 'local',
			displayField: 'label',
			valueField: 'id',
			editable:false
		});

		var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit: 1
		});

		var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
			groupHeaderTpl: '{name} ({rows.length} {[values.rows.length > 1 ? getText("admin.server.logging.items") : getText("admin.server.logging.item")]})',
			startCollapsed: false // start all groups collapsed
		});

		var txtSubject=Ext.create('Ext.form.field.Text',{
			name:'filterBox',
			fieldLabel:getText('admin.server.logging.filter'),
			width:515
		});


		var filterBox = Ext.create("Ext.form.field.Text", {
			fieldLabel:getText('admin.server.logging.filter'),
			width: 300,
			labelWidth: 80,
			labelAlign: 'right',
			enableKeyEvents:true,
			listeners: {
				keyup: {
					element: 'el',
					fn: function() {
						var callme = false;
						if (filterBox.getRawValue().length > 3) callme=true;
						if (filterBox.getRawValue().length < me.filter.length) callme = true;
						if (filterBox.getRawValue().length <= 3) {
							me.filter="";
						} else {
							me.filter = filterBox.getRawValue();
						}
						if (callme) {
							me.reload.call(me);
						}
					}
				}
			}
		});

		var topPanel = Ext.create("Ext.panel.Panel",  {
			bodyPadding: 5,
			margin: '0 0 0 0',
			border: false,
			bodyBorder:false,
			width: '100%',
			region:'north',
			id: 'topLogPanel',
			layout: 'hbox',
			items: [filterBox,
				{
					xtype:'button',
					style:{ marginBottom: '5px', marginLeft: '5px'},
					enableToggle:false,
					id:'ClearBtn',
					text:com.trackplus.TrackplusConfig.getText('common.btn.clear'),
					handler:function(){
						filterBox.setRawValue("");
						me.filter=filterBox.getRawValue();
						me.reload.call(me);
					}
				}
			]
		});

		me.filter="aurel";

		me.grid = Ext.create('Ext.grid.Panel', {
			store: store,
			id: 'logGrid',
			region:'center',
			style:{
				borderTop:'1px solid #D0D0D0'
			},
			bodyBorder:false,
			cls:'gridNoBorder',
			columns: [
			          {text: getText('admin.server.logging.className'), width: 400, dataIndex: 'className',
			        	  sortable: true, hideable:false, groupable:false},
			        	  {text: getText('admin.server.logging.level'), width: 100, dataIndex: 'value',
			        		  sortable: true, hideable:false, editor:me.cmbValue,
			        		  renderer : function(value){
			        			  if(me.levelsMap){
			        				  return me.levelsMap[''+value];
			        			  }
			        			  return value;
			        		  }
			        	  }
			        	  ],
			        	  margin:'0 0 0 0',
			        	  border:false,
			        	  scroll: false,
			        	  viewConfig: {
			        		  style: { overflow: 'auto', overflowX: 'hidden' }
			        	  },
			        	  region:'center',
			        	  width:'100%',
			        	  autoheight:true,
			        	  plugins: [cellEditing],
			        	  features:[groupingFeature]
		});

		me.grid.on('edit', function(editor, e) {
			e.record.commit();
			var className=e.record.data['className'];
			var value=e.value;
			var oldValue=e.originalValue;
			if(value!==oldValue){
				me.changeLevel.call(me,className,value);
			}
		});
		/*me.grid.on('validateedit', function(editor, e) {
		e.record.data[e.field] = e.value;
		e.cancel = true;
	});*/

		var mainPanel = Ext.create("Ext.panel.Panel",  {
			bodyPadding: 0,
			id: 'mainPanel',
			margin: '0 0 0 0',
			border: false,
			width: '100%',
			layout:'border',
			items: [topPanel, me.grid]
		});

		return mainPanel;
	},

	changeLevel:function(className,value){
		var me=this;
		me.grid.setLoading(true);
		Ext.Ajax.request({
			url: "editAdminLoggingConfig!save.action",
			params:{className:className,level:value},
			disableCaching:true,
			scope: me,
			success: function(response){
				me.grid.setLoading(false);
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success === false) {
					Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
				}
			},
			failure: function(response){
				me.grid.setLoading(false);
				Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
			},
			method:"POST"
		});
	},

	reload:function(){
		var me=this;
		Ext.Ajax.request({
			url: "editAdminLoggingConfig!load.action",
			params: {filter: me.filter},
			disableCaching:true,
			scope: me,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success === true) {
					me.jsonData=responseJson.data;
					me.levelsMap=new Object();
					var levels=responseJson.data.levels;
					for(var i=0;i<levels.length;i++){
						me.levelsMap[''+levels[i].id]=levels[i].label;
					}
					me.grid.store.loadData(responseJson.data.loggers);
					me.storeLevel.loadData(responseJson.data.levels);
				}else {
					if (responseJson.errorMessage) {
						var errorCode=responseJson.errorCode;
						var errorMessage=responseJson.errorMessage;
					}
				}
			},
			failure: function(response){
				Ext.MessageBox.alert(getText('common.err.failure'), response.responseText);
			},
			method:"POST"
		});
	}
});

