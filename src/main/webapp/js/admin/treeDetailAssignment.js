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
Ext.define('com.trackplus.admin.TreeDetailAssignment',{
	extend:'com.trackplus.admin.TreeDetail',
	config: {
		rootID: null,
		rootMessage: null,
		dynamicIcons: false,
		//whether to reload the grids after drag and drop:
		//if yes the sort order is enforced but the user filters set on grid fields are not applied after refresh
		//if no the sort order is not enforced (the item remains where it was dropped)
		//so set to yes only if no filter is defined for grids
		reloadGrids: false
	},
	leafDetailByFormLoad: false,
	gridPlugins:["gridfilters"],
	assignedGrid: null,
	availableGrid: null,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.initBase();
	},

	/**
	 * Show the node detail based on  responseJson
	 */
	loadSimpleDetailPanel: function(node, add) {
		Ext.Ajax.request({
			url: this.getDetailUrl(node, add),
			params: this.getDetailParams(node, add),
			scope: this,
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (this.centerPanel) {
					this.mainPanel.remove(this.centerPanel, true);
				}
				var northPanel = {
						region:'north',
						xtype:'component',
						cls: 'infoBox1',
						border:true,
					 	html: responseJson['assignmentInfo']
					};
				var panelGrids = this.createAssignmentGrids(node.id, responseJson);
				this.centerPanel = Ext.create('Ext.panel.Panel', {
					layout: 'border',
					region:'center',
					cls:'gridNoBorder',
					border: false,
					bodyBorder:false,
					bodyPadding:0,
					autoScroll: false,
					items:[northPanel, panelGrids]
				});
				this.mainPanel.add(this.centerPanel);
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText);
			},
			method:"POST"
		});
	},

	getDetailUrl: function() {
		return this.getBaseAction() + ".action";
	},

	reloadAssigned: function(urlStr,params) {
		params['node'] = this.selectedNodeID;
		Ext.Ajax.request({
			url:urlStr,
			params:params,
			disableCaching:true,
			scope: this,
			success: function(result){
				var jsonData=Ext.decode(result.responseText);
				if(jsonData.success===true) {
					var nodeIdToReload = jsonData['nodeToReload'];
					//var nodeIdToSelect = jsonData['nodeToSelect'];
					var treeStore = this.tree.getStore();
					var nodeToReload=treeStore.getNodeById(nodeIdToReload);
					var params = {node:nodeIdToReload};
					var oldParentsToReload = jsonData["parentsToReload"];
					if (oldParentsToReload && oldParentsToReload.length>0) {
						//old parents for department: the persons added to the departments should be automatically removed from other departments
						//for persons in groups or persons in roles parentsToReload will be null
						//(should not actualize old parents because a person can me in more groups/roles at the same time)
						params["oldParentIDs"] = oldParentsToReload.join();
					}
					Ext.Ajax.request({
						url: this.getBaseAction() + "!refreshParent.action",
						params: params,
						scope: this,
						disableCaching:true,
						success: function(response){
							var parents = Ext.decode(response.responseText);
							if (parents && parents.length>0) {
								Ext.Array.forEach(parents, function(parent, index, allItems) {
									var parentNode=treeStore.getNodeById(parent["id"]);
									if (parentNode) {
										parentNode.set("text", parent["label"]);
										parentNode.commit();
										if (parentNode.isLoaded()) {
											var options = this.getTreeExpandExtraParams(nodeToReload);
											treeStore.proxy.extraParams = options;
											treeStore.load({node:parentNode});
										}
									}
								}, this);
							}
						},
						failure: function(result){
							Ext.MessageBox.alert(this.failureTitle, result.responseText);
						},
						method:"POST"
					});
					//the assignment node was not yet expanded: refresh only the grids, the tree remains unmodified
					if (this.getReloadGrids()) {
						this.assignedGrid.store.loadData(jsonData["assigned"], false);
						this.availableGrid.store.loadData(jsonData["unassigned"], false);
					}
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
	
	getGroupingFeature:function(features){
		return null;
	},

	createAssignmentGrids: function(record, response) {
		var items = [];
		if (response['assigned']) {
			var assignedGridStore = Ext.create('Ext.data.Store', {
				fields:this.getGridFields(record),
				data: response['assigned']
			});
			var assignedFeatures=this.getGridFeatures();
			this.assignedGrid =  Ext.create('Ext.grid.Panel', {
				itemId:'assignedGrid',
				title: getText('common.lbl.assigned'),
				hideHeaders: this.hideGridHeaders(),
				store: assignedGridStore,
				plugins: this.gridPlugins,
				columns: this.getColumnModel(),
				stripeRows:	true,
				multiSelect: true,
				enableColumnHide: this.enableColumnHide(),
				enableColumnMove: this.enableColumnMove(),
				border:false,
				bodyBorder:false,
				cls:'gridNoBorder',
				features:assignedFeatures,
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
									var params=new Object();
									params['assign']=idsArray.join();
									this.reloadAssigned(this.getBaseAction()+"!assign.action", params);
								}
							}
						}
					}
				}
			});
			var assignedGroupingFeature=this.getGroupingFeature(assignedFeatures);
			if(assignedGroupingFeature){
				this.assignedGrid.addListener('afterrender', function(){
					assignedGroupingFeature.disable();
				});
			}
			items.push(this.assignedGrid);
		}

		if (response['unassigned']) {
			var availableGridStore = Ext.create('Ext.data.Store', {
				fields:this.getGridFields(record),
				data: response['unassigned']
			});
			var availableGridFeatures=this.getGridFeatures();
			this.availableGrid =  Ext.create('Ext.grid.Panel', {
				itemId:'availableGrid',
				title: getText('common.lbl.unassigned'),
				hideHeaders:this.hideGridHeaders(),
				store: availableGridStore,
				plugins: this.gridPlugins,
				columns: this.getColumnModel(),
				stripeRows:	true,
				multiSelect: true,
				enableColumnHide: this.enableColumnHide(),
				enableColumnMove: this.enableColumnMove(),
				border:false,
				bodyBorder:false,
				cls:'gridNoBorder',
				style:{
					borderLeft:'1px solid #D0D0D0'
				},
				features:availableGridFeatures,
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
									var params=new Object();
									params['unassign']=idsArray.join();
									this.reloadAssigned(this.getBaseAction()+"!unassign.action", params);
								}
							}
						}
					}
				}
			});
			var availableGroupingFeature=this.getGroupingFeature(availableGridFeatures);
			if(availableGroupingFeature){
				this.availableGrid.addListener('afterrender', function(){
					availableGroupingFeature.disable();
				});
			}
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
			defaults: { flex : 1 },
			items: items
		});
		return displayPanel;
	}
});
