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
Ext.define("com.trackplus.admin.SimpleAssignment", {
	extend: "Ext.panel.Panel",
	xtype: "simpleAssignment",
    controller: "simpleAssignment",
	/*mixins:{
		actionsBase: "com.trackplus.admin.ActionBase"
	},*/
	config: {
		//the id of the selected object to assign to
		objectID: null,
		//the request parameter name to submit objectID
		objectIDParamName: null,
		//the type to identify the assigned entity type: used only if more than one assignment type may exist, and they are handled by the same struts action
		assignmentType: null,
		//the request parameter name to submit assignmentType
		assignmentTypeParameterName: null,
		dynamicIcons: false,
		baseServerAction: null
	},
	
	infoBox: null,
	assignedGrid: null,
	availableGrid: null,
	
	initComponent : function() {
		//this.initBase();
		this.layout = "border";
		this.region = "center";
		this.autoScroll = true;
		//defaults: {flex: 1},
		this.items = this.getDetailItems();
		/*this.listeners = {
			beforerender: function() {
				Ext.Ajax.request({
					url: this.getDetailUrl(),
					params: this.getDetailParams(),
					scope: this,
					disableCaching: true,
					success: function(response){
						var responseJson = Ext.decode(response.responseText);
						this.loadDetailItems(responseJson);
					},
					failure: function(result){
						Ext.MessageBox.alert(this.failureTitle, result.responseText);
					},
					method:"POST"
				});
				
			}
		}*/
		this.callParent();
		this.loadDetailData();
	},

	loadDetailData: function() {
		Ext.Ajax.request({
			url: this.getDetailUrl(),
			params: this.getDetailParams(),
			scope: this,
			disableCaching: true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				this.loadDetailItems(responseJson);
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText);
			},
			method:"POST"
		});
	},
	
	getDetailUrl: function() {
		return this.getBaseServerAction() + ".action";
	},

	/**
	 * Gets the parameters for loading the assignment details
	 */
	getDetailParams: function() {
		var params = new Object();
		params[this.getObjectIDParamName()] = this.getObjectID();
		if (this.getAssignmentType() && this.getAssignmentTypeParameterName()) {
			params[this.getAssignmentTypeParameterName()] = this.getAssignmentType();
		}
		return params;
	},

	
	/*getDetailPanel: function(node) {
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
	},*/

	getDetailItems: function(){
		this.infoBox = Ext.create("Ext.Component", {
			region:"north",
			xtype:'component',
			cls: 'infoBox1',
			border: true,
			html: "" //responseJson["assignmentInfo"]
		});
		var panelGrids = this.createAssignmentGrids();
		return [this.infoBox, panelGrids];
	},

	loadDetailItems: function(jsonData) {
		this.infoBox.update(jsonData["assignmentInfo"]);
		this.assignedGrid.store.loadData(jsonData["assigned"], false);
		this.availableGrid.store.loadData(jsonData["unassigned"], false);
	},

	getIconField: function() {
		if (this.getDynamicIcons()) {
			return "icon";
		} else {
			return "iconCls";
		}
	},

	getGridFields: function() {
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
		return [{text: getText("admin.customize.list.lbl.icon"), width:28,
				sortable: true, dataIndex: this.getIconField(), renderer: renderer},
			{text: getText("common.lbl.name"), flex: 1, sortable: true, dataIndex: "text"}];
	},

	createAssignmentGrids: function() {
		var items = [];
		this.assignedGrid = this.createGrid("common.lbl.assigned", "secondGridDDGroup", "firstGridDDGroup", "dropAssign", false);
		items.push(this.assignedGrid);
		this.availableGrid = this.createGrid("common.lbl.unassigned", "firstGridDDGroup", "secondGridDDGroup", "dropUnassign", true);
		items.push(this.availableGrid);
		//Simple panel to house both grids
		return Ext.create("Ext.panel.Panel", {
			region: "center",
			margin:'0 0 0 0',
			border: false,
			bodyBorder: false,
			layout: {
				type: "hbox",
				align: "stretch"
			},
			defaults: {flex: 1},
			items: items
		});
	},
	
	createGrid: function(titleKey, dargGroup, dropGroup, dropMethodName, withBorderLeft) {
		var gridConfig = {
			title: getText(titleKey),
			hideHeaders:true,
			store: Ext.create("Ext.data.Store", {
				fields:this.getGridFields()
			}),
			columns: this.getColumnModel(),
			stripeRows:	true,
			multiSelect: true,
			enableColumnHide: false,
			enableColumnMove: false,
			border:false,
			bodyBorder:false,
			cls:"gridNoBorder",
			viewConfig: {
				plugins: {
					ptype: "gridviewdragdrop",
					dragGroup: dargGroup,
					dropGroup: dropGroup
				},
				listeners: {
					drop: dropMethodName
				}
			}
		};
		if (withBorderLeft) {
			gridConfig.style = {
					borderLeft:'1px solid #D0D0D0'
			};
		}
		return Ext.create("Ext.grid.Panel", gridConfig);
	}
});
