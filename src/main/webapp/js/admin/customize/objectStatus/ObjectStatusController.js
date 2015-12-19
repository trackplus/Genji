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

Ext.define('com.trackplus.admin.customize.objectStatus.ObjectStatusController',{
	extend: "Ext.app.ViewController",
	alias: "controller.objectStatus",
	mixins: {
		baseController: "com.trackplus.admin.TreeWithGridController"
	},
	baseServerAction: "objectStatus",
	entityDialog: "com.trackplus.admin.customize.objectStatus.ObjectStatusEdit",
	
	//listOptionWindowWidth: 500,
	//listOptionWindowHeight: 150,


	onTreeNodeDblClick: function(view, record) {
		//nothing: do not try to edit a tree node
	},


	enableDisableToolbarButtons: function(view, arrSelections) {
		var selectedRecord = arrSelections[0];
		if (CWHF.isNull(selectedRecord)) {
			this.getView().actionDeleteGridRow.setDisabled(true);
			this.getView().actionEditGridRow.setDisabled(true);
			this.getView().actionMoveUp.setDisabled(true);
			this.getView().actionMoveDown.setDisabled(true);
		} else {
			var modifiable = selectedRecord.data['modifiable'];
			this.getView().actionDeleteGridRow.setDisabled(!modifiable);
			this.getView().actionEditGridRow.setDisabled(!modifiable);
			this.getView().actionMoveDown.setDisabled(!modifiable);
			this.getView().actionMoveUp.setDisabled(!modifiable);
			if (modifiable) {
				var store = this.getView().grid.getStore();
				if (store) {
					this.getView().actionMoveDown.setDisabled(selectedRecord===store.last());
					this.getView().actionMoveUp.setDisabled(selectedRecord===store.first());
				}
			}
		}
	},

	
	/**
	 * Handler for adding a node (folder or leaf)
	 */
	onAdd: function() {
		var operation = "add";
		var title = this.getTitle(this.getAddTitleKey(), {fromTree:true});
		var loadParams = this.getAddLeafParams();
		var submitParams = this.getAddLeafParams();
		var reloadParams = this.getAddReloadParamsAfterSave(true);
		var reloadParamsFromResult = this.getAddSelectionAfterSaveFromResult();
		var selectedRecord = this.getSingleSelectedRecord(true);
		return this.onAddEdit(title, selectedRecord, operation, true, true, true,
				loadParams, submitParams, reloadParams, reloadParamsFromResult, this.reload);
	},

	/**
	 * Parameters for adding a new entry to a leaf:
	 * add a child to a leaf node: exclusively by extending custom selects:
	 * simple -> parent child or parent child -> parent child grandchild
	 * without explicitly asking for the exact type of the composite select
	 */
	/*protected*/getAddLeafParams: function() {
		//false as it would be added to a folder to send the leaf's id, not the parent folder's id
		return this.getAddParams(false);
	},

	/**
	 * Move the selected grid row up
	 */
	onMoveUpGridRow: function() {
		var gridRow = this.getView().getLastSelectedGridRow();
		if (gridRow) {
			nodeID = gridRow.data.node;
			this.onOrderChange("objectStatus!moveUp.action", {node:nodeID});
		}
	},

	/**
	 * Move the selected grid row down
	 */
	onMoveDownGridRow: function() {
		var gridRow = this.getView().getLastSelectedGridRow();
		if (gridRow) {
			var nodeID = gridRow.data.node;
			this.onOrderChange("objectStatus!moveDown.action", {node:nodeID});
		}
	},

	/**
	 * Private function foe changing the order by drag and drop or move up/down
	 */
	onOrderChange: function(url, params) {
		Ext.Ajax.request({
			url: url,
			disableCaching: true,
			scope: this,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success !== true) {
					Ext.MessageBox.alert(this.failureTitle, responseJson.errorMessage);
				}
				//com.trackplus.util.RefreshAfterSubmit.refreshGridAndTreeAfterSubmit.call(this, {rowToSelect:responseJson.node});
				this.reload({rowToSelect:responseJson.id});
			},
			failure: function(reponse){
				Ext.MessageBox.alert(this.failureTitle, reponse.responseText);
			},
			isUpload: false,
			method:'POST',
			params: params
		});
	},

	/**
	 * Reload after a change operation
	 */
	reload: function(refreshParamsObject) {
		this.reloadGrid(this.getView().grid, refreshParamsObject);
		//this.enableDisableToolbarButtons(null, this.getView().getGridSelection());
	},

	/**
	 * Parameters for reloading after a delete operation
	 * By delete the reload and select parameters are known before
	 */
	/*protected abstract*/getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return null;
	},

	/**
	 * Get the itemId of those actions whose context menu text or
	 * toolbar button tooltip should be changed according to the current selection
	 */
	getActionItemIdsWithContextDependentLabel: function() {
		return ["add", "editGridRow", "deleteGridRow", "moveUp", "moveDown"];
	},

	onGridRowDblClick: function(view, record) {
		var modifiable = record.data["modifiable"];
		if (modifiable) {
			this.onEditGridRow();
		}
	}
});
