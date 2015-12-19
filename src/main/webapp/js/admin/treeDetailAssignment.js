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
Ext.define("com.trackplus.admin.TreeDetailAssignment",{
	extend:"com.trackplus.admin.TreeDetail",
	xtype: "treeDetailAssignment",
    controller: "treeDetailAssignment",
	config: {
		rootID: null,
		rootMessageKey: null,
		dynamicIcons: false,
		//whether to reload the grids after drag and drop:
		//if yes the sort order is enforced but the user filters set on grid fields are not applied after refresh
		//if no the sort order is not enforced (the item remains where it was dropped)
		//so set to yes only if no filter is defined for grids
		reloadGrids: false,
		baseServerAction: null
	},

	getRootMessage: function() {
		if (this.getRootMessageKey()) {
			return getText(this.getRootMessageKey());
		}
		return "";
	},

	getTreeStoreUrl: function() {
		return this.getBaseServerAction() + "!expand.action";
	},

	//dynamicIcons: false,
	//reloadGrids: false,

	//leafDetailByFormLoad: false,
	//gridPlugins:["gridfilters"],
	assignedGrid: null,
	availableGrid: null,

	getIconField: function() {
		if (this.getDynamicIcons()) {
			return "icon";
		} else {
			return "iconCls";
		}
	},

	/**
	 * Gets the tree's fields
	 */
	getTreeFields: function() {
		var iconField = this.getIconField();
		return [{name : 'id', mapping : 'id', type: 'string'},
				{ name : 'text', mapping : 'text', type: 'string'},
				{ name : 'leaf', mapping : 'leaf', type: 'boolean'},
				{ name : iconField, mapping : iconField, type: 'string'}];
	},

	getGridFields: function(record) {
		return [{name:'id', type:'int'}, {name:'text', type:'string'},{name:this.getIconField(), type:'string'}];
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
		return [{text: getText('admin.customize.list.lbl.icon'), width:28, sortable: true,
				dataIndex: this.getIconField(), renderer: renderer},
			{text: getText('common.lbl.name'), flex: 1, sortable: true, dataIndex: 'text'}];
	},

	hideGridHeaders: function() {
		return true;
	},

	enableColumnHide: function() {
		return false;
	},

	enableColumnMove: function() {
		return false;
	},

	getGridFeatures: function() {
		return null;
	},

	getGridPlugins: function() {
		return ["gridfilters"];
	},

	getGroupingFeature:function(features){
		return null;
	},

	createAssignmentGrids: function(record, response) {
		var items = [];
		var gridFeatures = this.getGridFeatures();
		this.assignedGrid = this.createGrid("common.lbl.assigned", response["assigned"], gridFeatures,  "secondGridDDGroup", "firstGridDDGroup", "dropAssign", false);
		items.push(this.assignedGrid);
		this.availableGrid = this.createGrid("common.lbl.unassigned", response["unassigned"], gridFeatures, "firstGridDDGroup", "secondGridDDGroup", "dropUnassign", true);
		items.push(this.availableGrid);

		var assignedGroupingFeature=this.getGroupingFeature(gridFeatures);
		if(assignedGroupingFeature){
			this.assignedGrid.addListener('afterrender', function(){
				assignedGroupingFeature.disable();
			});
		}
		var availableGroupingFeature=this.getGroupingFeature(gridFeatures);
		if(availableGroupingFeature){
			this.availableGrid.addListener('afterrender', function(){
				availableGroupingFeature.disable();
			});
		}
		//simple panel to house both grids
		var displayPanel = Ext.create("Ext.panel.Panel", {
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
		return displayPanel;
	},

	createGrid: function(titleKey, storeData, gridFeatures, dargGroup, dropGroup, dropMethodName, withBorderLeft) {
		var gridConfig = {
			title: getText(titleKey),
			hideHeaders:true,
			store: Ext.create("Ext.data.Store", {
				fields:this.getGridFields(),
				data: storeData
			}),
			hideHeaders: this.hideGridHeaders(),
			plugins: this.getGridPlugins(),
			features: gridFeatures,
			columns: this.getColumnModel(),
			stripeRows:	true,
			multiSelect: true,
			enableColumnHide: this.enableColumnHide(),
			enableColumnMove: this.enableColumnMove(),
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
