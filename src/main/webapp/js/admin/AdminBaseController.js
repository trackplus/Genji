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

Ext.define("com.trackplus.admin.AdminBaseController",{
	extend:'Ext.Base',
	config: {},
	
	/**
	 * Whether by deleting an entity should be first asked for a confirmation, independently of any dependency
	 */
	confirmDeleteEntity:true,
	
	/**
	 * Whether the possible replacements should be selected form a list or from a tree  
	 */
	replacementIsTree: false,
	
	/**
	 * Whether to ask for extra confirmation if the entity to be deleted has dependencies
	 */
	//confirmDeleteNotEmpty:false,
	/**
	 * Whether the deletion of a not empty entity was already confirmed
	 * This is also the name of the request parameter
	 * Makes sense only if confirmDeleteNotEmpty is true
	 */
	deleteConfirmed:false,
	
	
	ERROR_CODE_NEED_REPLACE: 1,
	NOT_EMPTY_WARNING: 3,

	/**
	 * The title for a failure message
	 */
	failureTitle:getText("common.err.failure"),

	/**
	 * Creates a context menu based on an array of actions
	 */
	/*public*/createContextMenu: function(record, actions) {
		var contextMenu = new Ext.menu.Menu({
			items: []
		});
		if (actions) {
			for(var i=0;i<actions.length;i++){
				var action=actions[i];
				if (action) {
					var contextMenuItemCfg = {
						text:action.getText(),
						iconCls:action.getIconCls()+"16",
						//scope is the viewController
						scope:this,
						//Ext.Action does not have tooltip field: take from initialConfig
						tooltip:action.initialConfig.tooltip,
						//the handler is not the name as in view but the function object from the viewController
						handler: this[action.initialConfig.handler],
						itemId: action.itemId};
					contextMenu.add(contextMenuItemCfg);
					if (action.itemId) {
						var contextMenuItem = contextMenu.getComponent(action.itemId);
						contextMenuItem.tooltipKey = action.initialConfig.tooltipKey;
					}
				}
			}
		}
		return contextMenu;
	},

	/**
	 * Reload the grid
	 */
	reloadGrid: function(grid, refreshParametersObject) {
		grid.getStore().load({
			//scope:this,
			callback:function(){
				if (refreshParametersObject) {
					var rowToSelect = refreshParametersObject.rowToSelect;
					if (rowToSelect) {
						var row = grid.getStore().getById(rowToSelect);
						if (row) {
							var gridSelectionModel = grid.getSelectionModel();
							if (gridSelectionModel.isSelected(row)) {
								//the row is already selected: deselect first, otherwise the selection code is not triggered (for ex. enable/disable move up/down if first/last etc.)
								gridSelectionModel.deselectAll(true);
							}
							gridSelectionModel.select(row);
						}
					}
				}
			}
		})
	},
	
	reloadTree: function(tree, refreshParamsObject) {
		//nodeID is null after delete, and not null after add/edit
		var nodeID = refreshParamsObject.node;
		var reloadTree = refreshParamsObject.reloadTree;
		if (reloadTree) {
			//tree reload is needed: after delete, add and edit with label change
			var treeStore = tree.getStore();
			var options = {};
			if (CWHF.isNull(nodeID) && treeStore.getRootNode().hasChildNodes()) {
				var childNodes = treeStore.getRootNode().childNodes;
				if (childNodes) {
					var numberOfChildren = childNodes.length;
					if (numberOfChildren>0) {
						//select the first row after delete
						var firstNode = treeStore.getRootNode().getChildAt(0);
						if (firstNode) {
							var lastSelectedTreeNode = tree.getSelectionModel().getLastSelected();
							var selectedNodeID = null;
							if (lastSelectedTreeNode) {
								selectedNodeID = lastSelectedTreeNode.get("id");
							}
							nodeID = firstNode.get("id");
							if (nodeID===selectedNodeID) {
								//the first row was deleted, try to select the second row if exists
								nodeID = null;
								if (numberOfChildren>1) {
									var secondNode = treeStore.getRootNode().getChildAt(1);
									if (secondNode) {
										nodeID = secondNode.get("id");
									}
								}
							}
						}
					}
				}
			}
			/*if (nodeID) {
				options.callback = this.getView().selectNode;
				options.scope = {tree:tree, nodeIdToSelect:nodeID}
			} else {
				//no node to select reset the detail panel and toolbar
				options.callback = this.getView().resetDetailPanel;
				options.scope = this;
			}
			treeStore.load(options);*/
			
			var scope={tree: tree, nodeIDToSelect:nodeID};
			tree.store.load({//node:nodeToReload,
							callback:this.selectTreeNodeAfterReload,
							synchronous:false,
							scope:scope});
			
		} else {
			//edit without label change
			var selectionModel = this.getView().tree.getSelectionModel();
			if (nodeID) {
				//get the node by id
				var nodeToSelect = this.getView().tree.getStore().getNodeById(nodeID);
				if (nodeToSelect) {
					selectionModel.select(nodeToSelect);
				}
			}
		}
	},
	
	/**
	 * Refresh the tree after submit
	 */
	refreshTreeAfterSubmit: function(tree, refreshParametersObject) {
		var nodeIDToSelect = refreshParametersObject.nodeIDToSelect;
		var nodeIDToReload = refreshParametersObject.nodeIDToReload;
		var reloadTree = refreshParametersObject.reloadTree;
	    var resetDetail = refreshParametersObject.resetDetail;
		if (reloadTree) {
			//tree reload is needed: after delete, add and edit with label change
			var treeStore = tree.getStore();
			var options = {};
			if (nodeIDToReload) {
				options.node = tree.getStore().getNodeById(nodeIDToReload);
			}
			if (nodeIDToSelect) {
				options.callback = this.selectTreeNodeAfterReload;
				options.scope = {tree:tree, nodeIDToSelect:nodeIDToSelect}
			} else {
				if (resetDetail) {
					options.callback = this.resetDetailPanel;
					options.scope = this;
				}
			}
			treeStore.load(options);
		} else {
			//edit without label change
			this.selectTreeNodeAfterReload.call({tree:tree, nodeIDToSelect:nodeIDToSelect});
		}
	},
	
	/**
	 * Reload method
	 */
	/*protected*/reloadTreeWithGrid: function(tree, grid, refreshParametersObject) {
		var nodeIDToReload = null;
		var nodeIDToSelect = null;
		var rowToSelect = null;
		if (refreshParametersObject) {
			nodeIDToReload = refreshParametersObject.nodeIDToReload;
			nodeIDToSelect = refreshParametersObject.nodeIDToSelect;
			rowToSelect = refreshParametersObject.rowToSelect;
		}
		var nodeToReload;
		if (nodeIDToReload) {
			nodeToReload=tree.getStore().getNodeById(nodeIDToReload);
		} else {
			var lastSelectedTreeNode = tree.getSelectionModel().getLastSelected();
			if (lastSelectedTreeNode) {
				//typically no nodeIDToReload specified when this.selectedNode is the one to reload
				nodeToReload = lastSelectedTreeNode;
				nodeIDToReload = lastSelectedTreeNode.get("id");
			} else {
				//no node was selected to add to means adding to the root
				//(for example by project configuration add a project specific filter directly to the "branch" root)
				nodeToReload = tree.getRootNode();
				nodeIDToReload = nodeToReload.get("id");
			}
		}
		if (nodeToReload) {
			if (nodeToReload.isLoaded()) {
				//reload the node only if the node was already loaded (expanded)
				//set the global this.rowToSelect for selecting a row after the grid has been reloaded. See treeWithGrid.onGridStoreLoad()
				//(in selectTreeNodeAfterReload the grid row can't be selected: see selectTreeNodeAfterReload
				//this.getView().rowIdToSelect = rowToSelect;
				if (CWHF.isNull(nodeIDToSelect)) {
					nodeIDToSelect = nodeIDToReload;
				}
				var scope={tree: tree, nodeIDToSelect:nodeIDToSelect};
				tree.store.load({node:nodeToReload,
								callback:this.selectTreeNodeAfterReload,
								synchronous:false,
								scope:scope});
			} else {
				//tree node was not loaded previously: refresh the grid only
				if (grid) {
					this.reloadGrid(grid, {rowToSelect:rowToSelect});
				}
			}
		}
	},
	
	/**
	 * Callback after the branch is reloaded
	 */
	selectTreeNodeAfterReload: function() {
		//re-selection should be made by code because after reload the tree node selection gets lost:
		//1. if the node to select is the reloaded node: the reloaded node looses the selection by the this.tree.store.load()
		//2. if the node to select is a child of the reloaded node it looses the selection anyway
		//Although this tree node selection by code will reload the grid (because it triggers the treeNodeSelect()),
		//but the corresponding grid row can't be selected here directly (if it would be the case) that's why it is selected
		//in treeWithGrid.onGridStoreLoad() after the global this.rowToSelect was set
		var treeSelectionModel = this.tree.getSelectionModel();
		if (this.nodeIDToSelect) {
			var nodeToSelect = this.tree.getStore().getNodeById(this.nodeIDToSelect);
			if (nodeToSelect) {
				if (treeSelectionModel.isSelected(nodeToSelect)) {
					//if already selected deselect it first because
					//otherwise the select does not trigger the select handler (for refreshing the detail part)
					treeSelectionModel.deselect(nodeToSelect);
				}
				treeSelectionModel.select(nodeToSelect);
				if (!nodeToSelect.isVisible()) {
					this.tree.expandPath(nodeToSelect.getPath());
				}
			} else {
				//the node to select is not found in the reloaded tree: select the root node
				treeSelectionModel.select(this.tree.getRootNode());
			}
		}
	},
	
	/**********************************************label, tooltip, icon values***********************************************/
	
	/**
	 * The iconCls for the delete button
	 */
	/*protected*//*getDeleteIconCls: function() {
		return "delete";
	},*/
	/**
	 * The key for "delete" button text
	 */
	/*protected*//*getDeleteButtonKey: function() {
		return "common.btn.delete";
	},*/
	/**
	 * The title for "delete" popup and "delete" action tooltip
	 */
	/*protected*//*getDeleteTitleKey: function() {
		return "common.lbl.delete";
	},*/
	/**
	 * The delete confirmation text (if it will be asked for confirmation)
	 */
	/*protected*//*getRemoveWarningKey: function() {
		return "common.lbl.removeWarning";
	},*/
	
	/*protected*//*getReplacementKey: function() {
		return "common.lbl.replacement";
	},*/
	
	/**
	 * Parameters for reloading after a delete operation
	 * By delete the reload and select parameters are known before
	 */
	/*protected abstract*/getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return responseJson;
	},

	/**
	 * The struts action for delete/replace
	 */
	/*protected*/getDeleteUrlBase: function(extraConfig) {
		return this.getNodeBaseAction(extraConfig);
	},

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getDeleteUrl: function(extraConfig){
		return this.getDeleteUrlBase(extraConfig)+"!delete.action";
	},

	/**
	 * Parameter name for the submitted id(s) by delete
	 * If allowMultipleSelections===false then the same this.entityID (simple grid) or
	 * 	node (tree with grid) can be used on the server side for both delete and edit submits
	 * If allowMultipleSelections===true this should return another name
	 * 	the submitted value will be stored on the server typically
	 * 	in an Integer[] (simple grid) or String[] (tree with grid) variable
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 * Must be overridden if allowMultipleSelections===true
	 */
	/*protected abstract*/getDeleteParamName: function(extraConfig) {
		return this.entityID;
	},

	/**
	 * Parameters for deleting entity
	 * recordData: the selected entity data
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getDeleteParams: function(selectedRecords, extraConfig) {
		var params=new Object();
		var selectionParam = this.getSelectedIDs(selectedRecords, extraConfig);
		params[this.getDeleteParamName(extraConfig)]=selectionParam;
		//if (this.confirmDeleteNotEmpty) {
			params["deleteConfirmed"] = this.deleteConfirmed;
		//}
		return params;
	},

	/**
	 * Url for preparing the replacement data rendering
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*//*getRenderReplaceUrl: function(extraConfig){
		return this.getDeleteUrlBase(extraConfig)+"!renderReplace.action";
	},*/
	/**
	 * Parameters for preparing the replacement data
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getRenderReplaceParams: function(selectedRecords, extraConfig) {
		return this.getDeleteParams(selectedRecords, extraConfig);
	},
	/**
	 * Url for replacing and deleting of an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*//*getReplaceAndDeleteUrl: function(extraConfig){
		return this.getDeleteUrlBase(extraConfig)+"!replaceAndDelete.action";
	},*/
	/**
	 * Parameters for replacing and deleting of an entity
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getReplaceAndDeleteParams: function(selectedRecords, extraConfig) {
		return this.getDeleteParams(selectedRecords, extraConfig);
	},

	
	/**
	 * The label for the delete button
	 */
	/*protected*//*getDeleteLabel: function() {
		return getText("common.btn.delete");
	},*/

	/************************************delete related fields ***************************************/

	/**
	 * The localized entity name to be deleted
	 * Should be implemented only if differs from entity name
	 * (for example in field configuration: entity label is "field configuration" but delete label is "field")
	 */
	/*protected*//*getDeleteEntityLabel:function(extraConfig) {
		return this.getEntityLabel(extraConfig);
	},*/

	/**
	 * The a message patameterized with the deleteEntityLabel
	 */
	/*getMessage: function(titleKey, entityLabel) {
		return getText(titleKey, entityLabel);
	},*/

	/**
	 * Handler for deleting the selected data.
	 * It can be single or multiple selection
	 * title:
	 */
	deleteHandler: function(entityLabel, selectedRecords, extraConfig){
		if (CWHF.isNull(selectedRecords)) {
			return true;
		}
		this.deleteConfirmed = false;
		if (this.confirmDeleteEntity) {
			Ext.MessageBox.confirm(getText(this.getView().getDeleteTitleKey(), entityLabel),
					getText(this.getView().getRemoveWarningKey(), entityLabel),
					function(btn){
						if (btn==="no") {
							return false;
						} else {
							this.deleteSelected(entityLabel, selectedRecords, extraConfig);
						}
					}, this);
		} else {
			this.deleteSelected(entityLabel, selectedRecords, extraConfig);
		}
	},

	/**
	 * Delete handler
	 * selectedRecords the selected data (node(s)/row(s)) to be deleted
	 * extraConfig implementation specific extra configuration object
	 */
	/*private*/deleteSelected: function(entityLabel, selectedRecords, extraConfig) {
		var deleteParams = this.getDeleteParams(selectedRecords, extraConfig);
		Ext.Ajax.request({
			url: this.getDeleteUrl(extraConfig),
			params: deleteParams,
			disableCaching:true,
			scope: this,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success===true) {
					//delete done (no replacement were needed)
					this.reload.call(this, this.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson));
				} else {
					var errorCode = responseJson.errorCode;
					if (errorCode) {
						if (errorCode===this.ERROR_CODE_NEED_REPLACE) {
							//render dialog for selecting the replacement
							/*var windowItems = this.getReplacementItems(entityLabel, responseJson, selectedRecords);
							var load = {loadUrl:this.getRenderReplaceUrl(extraConfig),
									loadUrlParams:this.getRenderReplaceParams(selectedRecords, extraConfig)};
							var submit = {	submitUrl:this.getReplaceAndDeleteUrl(extraConfig),
											submitUrlParams:this.getReplaceAndDeleteParams(selectedRecords, extraConfig),
											submitButtonText:getText(this.getDeleteButtonKey()),
											//deleting more users can be a lengthy operation
	                                        timeout:300,
											refreshAfterSubmitHandler:this.reload,
											refreshParametersBeforeSubmit:this.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson)
										};
							var windowConfig = Ext.create('com.trackplus.util.WindowConfig',
									{postDataProcess:this.replaceOptionPostDataProcess, extraConfig:{entityLabel:entityLabel}});
							windowConfig.showWindow(this, title, this.replacementWidth, this.replacementHeight, load, submit, windowItems);*/
							
							var title = getText(this.getView().getDeleteTitleKey(), entityLabel);
							//var loadAndSubmitParams = this.getEditParams(fromTree);
					        var windowParameters = {
					        	callerScope:this,
					        	windowTitle:title,
					        	loadUrlParams: this.getRenderReplaceParams(selectedRecords, extraConfig),
					        	submitUrlParams: this.getReplaceAndDeleteParams(selectedRecords, extraConfig),
					        	//refreshParametersBeforeSubmit: refreshParams,
					        	refreshParametersBeforeSubmit: this.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson),
					        	refreshAfterSubmitHandler: this.reload,
					        	/*extraConfig : {
					                fromTree : fromTree
					            },*/
					        	entityContext: {
					        		deleteUrlBase: this.getDeleteUrlBase(),
					        		entityLabel: entityLabel,
					        		replacementIsTree: this.replacementIsTree,
					        		//the last selected tree node
					        		//selectedTreeNode: this.getView().getLastSelectedTreeNode(),
					        		//the config object passed to view
					        		//config: this.getView().getConfig(),
					        		//the record data of the actually selected node/row
					        		//recordData: recordData,
					        		//the selection is leaf
					        		//isLeaf: isLeaf,
					        		//the operation is add
					            	//add: add,
					            	//whether the operation refres to tree node or grid row
					            	//fromTree: fromTree,
					            	//the name of the operation
					            	//operation: operation
					        	}
					        };
							var windowConfig = Ext.create("com.trackplus.admin.DeleteWindow", windowParameters);
							windowConfig.showWindowByConfig(this);
						} else {
							//the entity to be deleted is not empty an extra confirmation box is shown
							if (errorCode===this.NOT_EMPTY_WARNING) {
								var errorMessage = responseJson.errorMessage;
								Ext.MessageBox.confirm(getText(this.getView().getDeleteTitleKey(), entityLabel),
									errorMessage,
									function(btn){
										if (btn==="no") {
											return false;
										} else {
											this.deleteConfirmed = true;
											this.deleteSelected.call(this, entityLabel, selectedRecords, extraConfig);
										}
									}, this);
							} else {
								Ext.MessageBox.alert(this.failureTitle, responseJson.errorMessage);
							}
						}
					} else {
						//no right to delete (for ex. with fake URL-Params)
						com.trackplus.util.showError(result);
					}
				}
			},
			failure: function(response){
				com.trackplus.util.requestFailureHandler(response);
			},
			method:"POST"
		});
	},

	

	/**
	 * Error handler for delete. In the overridden versions depending on error code
	 * the deleteSelected() might be called again with extra/modified submit parameters
	 */
	/*protected*//*errorHandlerDelete: function(result, selectedRecords){
		com.trackplus.util.showError(result);
	},*/

	/**
	 * The replacement items for the deleted entity
	 * (The replacement panel will be created with on this items)
	 */
	/*protected*//*getReplacementItems: function(entityLabel, responseJson, selectedRecords, extraConfig) {
		return [{xtype : 'label',
				itemId: 'replacementWarning'},
				CWHF.createCombo('Replacement',
						'replacementID',
						{itemId:"replacementID",
						labelWidth:200,
						allowBlank:false,
						blankText: this.getMessage('common.err.replacementRequired',
								entityLabel)})];
	},*/

	/**
	 * Load the data in the replacement panel when it arrives from server
	 * the complete replacementWarning could be composed on the server
	 * If not, it will be composed on the client, but at least
	 * the label of the entity to be deleted should be specified
	 */
	/*private*//*replaceOptionPostDataProcess: function(data, panel, extraConfig) {
		var replacementWarning = panel.getComponent('replacementWarning');
		var replacementWarningText = data['replacementWarning'];
		if (CWHF.isNull(replacementWarningText)) {
			var label = data['label'];
			replacementWarningText = getText("common.lbl.replacementWarning", extraConfig.entityLabel, label);
			replacementWarningText = replacementWarningText + getText("common.lbl.cancelDeleteAlert");
		}
		replacementWarning.setText(replacementWarningText, false);
		var replacementList = panel.getComponent('replacementID');
		this.loadReplacementOptionData(replacementList, data);
		var replacementListLabel = data.replacementListLabel;
		if (CWHF.isNull(replacementListLabel)) {
			replacementListLabel = this.getMessage('common.lbl.replacement', extraConfig.entityLabel);
		}
		replacementList.labelEl.dom.innerHTML = replacementListLabel;
	},*/

	/**
	 * Load the data source and value for the replacement options combo
	 * Override this for different tree based pickers
	 */
	/*protected*//*loadReplacementOptionData: function(replacementControl, data) {
		replacementControl.store.loadData(data["replacementList"]);
		replacementControl.setValue(null);
	}*/
});
