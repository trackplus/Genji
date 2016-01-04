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

Ext.define('com.trackplus.admin.server.logging.LoggingConfigView',{
	extend:'Ext.panel.Panel',
	controller:'loggingConfig',
	config:{
		levels:[]
	},

	region:'center',
	bodyPadding: 0,
	margin: '0 0 0 0',
	border: false,
	layout:'border',

	listeners : {
		scope: 'controller',
		afterrender:'onAfterRender'
	},

	levelsMap:null,

	initComponent : function() {
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

		var storeLevel=Ext.create('Ext.data.Store', {
			autoLoad: false,
			fields: [{name:'id',type:'int'},{name:'label',type:'string'}],
			data:this.getLevels()
		});

		me.levelsMap=new Object();
		var levels=this.getLevels();
		for(var i=0;i<levels.length;i++){
			me.levelsMap[''+levels[i].id]=levels[i].label;
		}

		var cmbValue=Ext.create('Ext.form.ComboBox', {
			store: storeLevel,
			reference:'cmbValue',
			name: 'name',
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
			reference:'filterBox',
			width: 300,
			labelWidth: 80,
			labelAlign: 'right',
			enableKeyEvents:true,
			listeners: {
				scope:'controller',
				keyup:'onFilterBoxKeyUp'
			}
		});

		var topPanel = Ext.create("Ext.panel.Panel",  {
			bodyPadding: 5,
			margin: '0 0 0 0',
			border: false,
			bodyBorder:false,
			width: '100%',
			region:'north',
			layout: 'hbox',
			items: [filterBox,
				{
					xtype:'button',
					style:{ marginBottom: '5px', marginLeft: '5px'},
					enableToggle:false,
					text:com.trackplus.TrackplusConfig.getText('common.btn.clear'),
					handler:'clearFilter'
				}
			]
		});

		//me.filter="aurel";

		var grid = Ext.create('Ext.grid.Panel', {
			store: store,
			reference:'grid',
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
					sortable: true, hideable:false, editor:cmbValue,
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
			width:'100%',
			autoheight:true,
			plugins: [cellEditing],
			features:[groupingFeature],
			listeners:{
				scope:'controller',
				'edit':'onGridEdit'
			}
		});

		this.items=[topPanel, grid];

		this.callParent();
	}
});

