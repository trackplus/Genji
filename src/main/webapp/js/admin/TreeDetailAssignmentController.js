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
Ext.define("com.trackplus.admin.TreeDetailAssignmentController",{
	extend: "Ext.app.ViewController",
	alias: "controller.treeDetailAssignment",
	mixins: {
		baseController: "com.trackplus.admin.TreeDetailController"
	},
	
	
	leafDetailByFormLoad: false,

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
				if (this.getView().centerPanel) {
					this.getView().mainPanel.remove(this.getView().centerPanel, true);
				}
				var northPanel = {
						region:"north",
						xtype:"component",
						cls: "infoBox1",
						border:true,
					 	html: responseJson["assignmentInfo"]
					};
				var panelGrids = this.getView().createAssignmentGrids(node, responseJson);
				this.getView().centerPanel = Ext.create("Ext.panel.Panel", {
					layout: 'border',
					region:'center',
					cls:'gridNoBorder',
					border: false,
					bodyBorder:false,
					bodyPadding:0,
					autoScroll: false,
					items:[northPanel, panelGrids]
				});
				this.getView().mainPanel.add(this.getView().centerPanel);
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText);
			},
			method:"POST"
		});
	},

	getDetailUrl: function(node, add) {
		return this.getView().getBaseServerAction() + ".action";
	},

	dropAssign: function(node, data, dropRec, dropPosition) {
		if (data && data.records && data.records.length>0){
			var idsArray = new Array();
			for ( var i = 0; i < data.records.length; i++) {
				idsArray[i] = data.records[i].data.id;
			}
			var params=new Object();
			params["assign"]=idsArray.join();
			this.reloadAssigned(this.getView().getBaseServerAction()+"!assign.action", params);
		}
	},
	
	dropUnassign: function(node, data, dropRec, dropPosition) {
		if (data && data.records && data.records.length>0){
			var idsArray = new Array();
			for ( var i = 0; i < data.records.length; i++) {
				idsArray[i] = data.records[i].data.id;
			}
			var params=new Object();
			params["unassign"]=idsArray.join();
			this.reloadAssigned(this.getView().getBaseServerAction()+"!unassign.action", params);
		}
	},
	
	reloadAssigned: function(urlStr,params) {
		params["node"] = this.getView().selectedNodeID;
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
					var treeStore = this.getView().tree.getStore();
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
						url: this.getView().getBaseServerAction() + "!refreshParent.action",
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
											var options = this.getView().getTreeStoreExtraParams(nodeToReload);
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
					if (this.getView().getReloadGrids()) {
						this.getView().assignedGrid.store.loadData(jsonData["assigned"], false);
						this.getView().availableGrid.store.loadData(jsonData["unassigned"], false);
					}
				} else {
					com.trackplus.util.requestFailureHandler(result);
				}
			},
			failure: function(result){
				com.trackplus.util.requestFailureHandler(result);
			}
		});
	}
});
