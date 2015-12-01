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

/**
 * Class for role and account assignments for project
 */
Ext.define('com.trackplus.admin.SimpleAssignment',{
	extend:'Ext.Base',
	config: {
		//the id of the selected object to assign to
		objectID: null,
		//the request parameter name to submit objectID
		objectIDParamName: null,
		//the type to identify the assigned entity type: used only if more than one assignment type may exist, and they are handled by the same struts action
		assignmentType: null,
		//the request parameter name to submit assignmentType
		assignmentTypeParameterName: null,
		dynamicIcons: false
	},
	leafDetailByFormLoad: false,
	assignedGrid: null,
	availableGrid: null,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
	},

	/**
	 * Gets the base struts action
	 */
	getBaseAction: function() {
		return this.baseAction;
	},

	getDetailUrl: function() {
		return this.getBaseAction() + ".action";
	},

	/**
	 * Gets the parameters for loading the assignment details
	 */
	getDetailParams: function(node) {
		var params = new Object();
		params[this.getObjectIDParamName()] = this.getObjectID();
		if (this.getAssignmentType() && this.getAssignmentTypeParameterName()) {
			params[this.getAssignmentTypeParameterName()] = this.getAssignmentType();
		}
		return params;
	},

	getToolbarActions: function() {
		return [];
	},

	getToolbarActionChangesForTreeNodeSelect: function(node) {
	},

	adjustToolbarButtonsTooltip: function(node) {
	},

	getDetailPanel: function(node) {
		var panel = Ext.create("Ext.Panel", {
			layout: "border",
			region: "center",
			border:false,
			items: [],
			autoScroll: true
		});
		Ext.Ajax.request({
			url: this.getDetailUrl(),
			params: this.getDetailParams(node),
			scope: this,
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				var items = this.getDetailItems(node, false, responseJson);
				panel.add(items);
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText);
			},
			method:"POST"
		});
		return panel;
	},

	getDetailItems: function(record, add, responseJson){
		var northPanel = {
			region:"north",
			xtype:'component',
			cls: 'infoBox1',
			border:true,
			html: responseJson["assignmentInfo"]
		};
		var panelGrids = this.createAssignmentGrids(record, responseJson);
		return [northPanel, panelGrids];
	},

	reloadAssigned: function(urlStr,params) {
		Ext.Ajax.request({
			url:urlStr,
			params:params,
			disableCaching:true,
			scope: this,
			success: function(result) {
				var jsonData=Ext.decode(result.responseText);
				if(jsonData.success===true) {
					this.assignedGrid.store.loadData(jsonData['assigned'], false);
					this.availableGrid.store.loadData(jsonData['unassigned'], false);
				} else {
					com.trackplus.util.requestFailureHandler(result);
				}
			},
			failure: function(result){
				com.trackplus.util.requestFailureHandler(result);
			}
		});
	},

	getIconField: function() {
		if (this.getDynamicIcons()) {
			return "icon";
		} else {
			return "iconCls";
		}
	},

	getGridFields: function(record) {
		return [{name:'id', type:'int'},
				{name:'text', type:'string'},
				{name:this.getIconField(), type:'string'}];
	},

	iconClsRenderer: function(value, metadata, record) {
		metadata.tdCls = value;
		return null;
	},

	iconRenderer: function(value, metadata, record) {
		return '<img src="' + value + '"/>';
	},

	getColumnModel: function() {
		var renderer;
		if (this.getDynamicIcons()) {
			renderer = this.iconRenderer;
		} else {
			renderer = this.iconClsRenderer;
		}
		return [{text: getText('admin.customize.list.lbl.icon'), width:28,
				sortable: true, dataIndex: this.getIconField(), renderer: renderer},
			{text: getText('common.lbl.name'), flex: 1, sortable: true, dataIndex: 'text'}];
	},

	createAssignmentGrids: function(record, response) {
		var items = [];
		if (response['assigned']) {
			var assignedGridStore = Ext.create('Ext.data.Store', {
				fields:this.getGridFields(record),
				data: response['assigned']
			});
			this.assignedGrid = Ext.create('Ext.grid.Panel', {
				itemId:'assignedGrid',
				title: getText('common.lbl.assigned'),
				hideHeaders:true,
				store: assignedGridStore,
				columns: this.getColumnModel(),
				stripeRows:	true,
				multiSelect: true,
				enableColumnHide: false,
				enableColumnMove: false,
				border:false,
				bodyBorder:false,
				cls:'gridNoBorder',
				viewConfig: {
					plugins: {
						ptype: 'gridviewdragdrop',
						dragGroup: 'secondGridDDGroup',
						dropGroup: 'firstGridDDGroup'
					},
					listeners: {
						drop: {scope:this,
							fn: function(node, data, dropRec, dropPosition) {
								if(data && data.records && data.records.length>0){
									var idsArray = new Array();
									for ( var i = 0; i < data.records.length; i++) {
										idsArray[i] = data.records[i].data.id;
									}
									var params=this.getDetailParams();
									if (CWHF.isNull(params)) {
										params=new Object();
									}
									params['assign']=idsArray.join();
									this.reloadAssigned(this.getBaseAction()+"!assign.action", params);
								}
							}
						}
					}
				}
			});
			items.push(this.assignedGrid);
		}

		if (response['unassigned']) {
			var availableGridStore = Ext.create('Ext.data.Store', {
				fields:this.getGridFields(record),
				data: response['unassigned']
			});
			this.availableGrid =  Ext.create('Ext.grid.Panel', {
				itemId:'availableGrid',
				title: getText('common.lbl.unassigned'),
				hideHeaders:true,
				store: availableGridStore,
				columns: this.getColumnModel(),
				stripeRows:	true,
				multiSelect: true,
				enableColumnHide: false,
				enableColumnMove: false,
				border:false,
				bodyBorder:false,
				cls:'gridNoBorder',
				style:{
					borderLeft:'1px solid #D0D0D0'
				},
				viewConfig: {
					plugins: {
						ptype: 'gridviewdragdrop',
						dragGroup: 'firstGridDDGroup',
						dropGroup: 'secondGridDDGroup'
						},
					listeners: {
						drop: {scope:this,
							fn: function(node, data, dropRec, dropPosition) {
								if(data && data.records && data.records.length>0){
									var idsArray = new Array();
									for ( var i = 0; i < data.records.length; i++) {
										idsArray[i] = data.records[i].data.id;
									}
									var params=this.getDetailParams();
									if (CWHF.isNull(params)) {
										params=new Object();
									}
									params['unassign']=idsArray.join();
									this.reloadAssigned(this.getBaseAction()+"!unassign.action", params);
								}
							}
						}
					}
				}
			});
			items.push(this.availableGrid);
		}
		//Simple 'border layout' panel to house both grids
		var displayPanel = Ext.create('Ext.Panel', {
			region: 'center',
			margin:'0 0 0 0',
			border: false,
			bodyBorder: false,
			layout: {
				type: 'hbox',
				align: 'stretch'
			},
			defaults: { flex: 1 },
			items: items
		});
		return displayPanel;
	}
});
