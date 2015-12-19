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
Ext.define("com.trackplus.admin.project.Release",{
	extend:"com.trackplus.admin.TreeWithGrid",
	xtype: "release",
    controller: "release",
	config: {
		/**
		 * The ID of the selected project, used only by localization
		 */
		//projectID: null,
		mainRelease: null,
		childRelease: null,
		showClosedReleases: null,
		rootID: "_"
	},
	treeStoreUrl: "release!expand.action",
	
	/**
	 * TODO refactor into own window
	 */
	/*statics:{
		createEditPanelItems:function(lw){
			return [CWHF.createTextField('common.lbl.name',
				'releaseDetailTO.label', {allowBlank:false, labelWidth:lw, maxLength:255}),
				CWHF.createCombo('admin.customize.localeEditor.type.releaseStatus',
					'releaseDetailTO.releaseStatusID', {itemId:"releaseStatusID", labelWidth:lw}),
				CWHF.createDateField('admin.project.release.lbl.dueDate', 'releaseDetailTO.dueDate', {labelWidth:lw}),
				CWHF.createCheckboxWithHelp('admin.project.release.lbl.defaultReleaseNoticed', 'releaseDetailTO.defaultNoticed', {labelWidth:lw, width:lw+50}),
				CWHF.createCheckboxWithHelp('admin.project.release.lbl.defaultReleaseScheduled', 'releaseDetailTO.defaultScheduled',{labelWidth:lw, width:lw+50}),
				CWHF.createTextAreaField('common.lbl.description', 'releaseDetailTO.description', {maxLength:255, labelWidth:lw})];
		},
		
		postDataLoadCombo: function(data, panel) {
			var statusCombo = panel.getComponent('releaseStatusID');
			if (statusCombo) {
				//no statusCombo for phase
				statusCombo.store.loadData(data['statusList']);
				statusCombo.setValue(data['releaseDetailTO.statusID']);
			}
		}
	},*/
	baseServerAction: "release",

	dragAndDropOnGrid: true,
	dragAndDropOnTree: true,
	/**
	 * The tree, set in projectConfig.js
	 */
	//tree: null,
	/*folderEditWidth:500,
	folderEditHeight:400,
	labelWidth: 120,
*/
	//localizedMain: null,
	//localizedChild: null,
	//showClosedReleases: true,

	//by adding a release whether to add a main release or a child release
	//addAsChild: false,
	//actions
	actionAddMainRelease: null,
	actionAddChildRelease: null,
	actionEditGridRow: null,
	actionEditTreeNode: null,
	actionDeleteGridRow: null,
	actionDeleteTreeNode: null,
	actionDetachFromParentRelease: null,
	actionMoveUp: null,
	actionMoveDown: null,
	actionShowClosed: null,
	actionReload: null,

	/**
	 * Initialization method
	 */
	/*initBase: function() {
		this.initLocalizedLabels(this.getProjectID());
		this.initActions();
	},*/

	/*initLocalizedLabels: function(projectID) {
		Ext.Ajax.request({
			fromCenterPanel:true,
			url: "release!localizedLabels.action",
			scope: this,
			params: {node:projectID},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				//this.localizedMain = result.localizedMain;
				//this.localizedChild = result.localizedChild;
				//this.showClosedReleases = result.showClosedReleases;
			},
			failure: function(response) {
				Ext.MessageBox.alert(this.failureTitle, response.responseText);
			}
		});
	},*/

	/**
	 * to show the release grid with all main releases
	 * even if no release is selected to allow changing the order of the main releases
	 */
	postInitCenterPanel: function(rootNode) {
		this.getGridPanel(rootNode);
	},

	/**
	 * The localized entity name
	 */
	getEntityLabel: function(extraConfig) {
		var entityLabel = null;
		if (extraConfig) {
			var selectedRecord = extraConfig.selectedRecord;
			var fromTree = extraConfig.fromTree;
			if (selectedRecord) {
				var isChild = selectedRecord.data['isChild'];
				if (isChild) {
					//a release node in grid or in tree
					return this.getChildRelease();
				}
			}
		}
		return this.getMainRelease();
	},

	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls: function() {
		return "releaseAdd";
	},
	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getEditIconCls: function() {
		return "releaseEdit";
	},

	getAddMainLabel: function() {
		return getText(this.getAddTitleKey(), this.getMainRelease());
	},

	/**
	 * Add "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getAddChildLabel: function() {
		return getText(this.getAddTitleKey(), this.getChildRelease());
	},

	initActions: function() {
		this.actionAddMainRelease = CWHF.createAction(this.getAddMainLabel(), this.getAddIconCls(),
				"onAddMainRelease", {labelIsLocalized:true});
		this.actionAddChildRelease = CWHF.createAction(this.getAddChildLabel(), this.getAddIconCls(),
				"onAddChildRelease", {labelIsLocalized:true, disabled:true});
		this.actionEditGridRow = CWHF.createContextAction(this.getEditButtonKey(), this.getEditIconCls(),
				"onEditGridRow", this.getEditTitleKey(), {disabled:true, itemId:"editGridRow"});
		this.actionEditTreeNode = CWHF.createContextAction(this.getEditButtonKey(), this.getEditIconCls(),
				"onEditTreeNode", this.getEditTitleKey(), {itemId:"editTreeNode"});
		this.actionDeleteGridRow = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
				"onDeleteFromGrid", this.getDeleteTitleKey(), {disabled:true, itemId:"deleteGridRow"});
		this.actionDeleteTreeNode = CWHF.createContextAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
				"onDeleteFromTree", this.getDeleteTitleKey(), {itemId:"deleteTreeNode"});
		this.actionDetachFromParentRelease = CWHF.createAction("common.btn.detachFromParent", "clear",
				"onDetachFromParentRelease");
		this.actionMoveUp = CWHF.createContextAction(this.getMoveUpButtonKey(), this.getMoveUpIconCls(),
				"onMoveUpGridRow",  this.getMoveUpTitleKey(), {itemId: "moveUp", disabled:true});
		this.actionMoveDown = CWHF.createContextAction(this.getMoveDownButtonKey(), this.getMoveDownIconCls(),
				"onMoveDownGridRow", this.getMoveDownTitleKey(), {itemId: "moveDown", disabled:true});
		var showHideLabelKey = this.getShowHideLabelKey();
		this.actionShowClosed = CWHF.createAction(showHideLabelKey, "showHide",
				"onShowHide", {tooltip:getText("showHideLabelKey"), enableToggle:true, pressed:!this.getShowClosedReleases()});
		this.actionReload = CWHF.createAction(this.getReloadButtonKey(), this.getReloadIconCls(), "onReload");
		this.actions = [this.actionAddMainRelease, this.actionAddChildRelease,
						this.actionEditGridRow, this.actionDeleteGridRow,
						this.actionMoveUp, this.actionMoveDown, this.actionShowClosed];
	},

	getShowHideLabelKey: function() {
		if (this.getShowClosedReleases()) {
			return "common.btn.hide";
		} else {
			return "common.btn.show";
		}
	},

	/*onShowHide: function() {
		var toolbar = borderLayout.getActiveToolbarList();
		var toolbarButton = toolbar.getComponent('showHide');
		//this.showClosedReleases = !this.showClosedReleases;
		this.showClosedReleases = !toolbarButton.pressed;
		var showHideLabel = getText(this.getShowHideLabelKey());
		toolbarButton.setText(showHideLabel);
		toolbarButton.setTooltip(showHideLabel);
		this.onTreeNodeSelect(null, null, null, {showClosedReleases:this.showClosedReleases});
	},*/

	/**
	 * Reload the selected branch after showHide
	 */
	/*onReload: function() {
		var node = this.getLastSelectedTreeNode();
		var treeStore = this.tree.getStore();
		treeStore.load({node:node});
	},*/

	/**
	 * Expanding the node
	 */
	getTreeStoreExtraParams : function(node) {
	    var extraParams = {
	    	showClosedReleases: this.getShowClosedReleases()
	    };
	    return extraParams;
	},
	
	/**
	 * Get the extra parameters for the gridStore
	 */
	getGridStoreExtraParams: function(node, opts) {
		if (CWHF.isNull(node)) {
			//called manually
			node = this.selectedNode;
		}
		var params = {
			node: node.get("id")
		};
		if (opts && opts["showClosedReleases"]) {
			//called manually from onShowHide handler
			params["showClosedReleases"] = opts["showClosedReleases"];
		}
		return params;
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 *
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectedRecord) {
			var actions = [this.actionAddChildRelease, this.actionEditTreeNode, this.actionDeleteTreeNode, this.actionReload];
			if (selectedRecord.parentNode && !selectedRecord.parentNode.isRoot()) {
				actions.push(this.actionDetachFromParentRelease);
			}
			return actions;
		}
		return [];
	},


	/**
	 * Gets the grid store's fields for the selected node
	 */
	getTreeFields: function(node) {
			return [{name : 'id', mapping : 'id', type: 'string'},
					{ name : 'text', mapping : 'text', type: 'string'},
					{ name : 'leaf', mapping : 'leaf', type: 'boolean'},
					{ name: 'isChild',	type: 'boolean'},
					{ name : 'iconCls', mapping : 'iconCls', type: 'string'}];
	},

	/**
	 * Gets the grid store's fields for the selected node
	 */
	getGridFields: function(node) {
			return [{name: 'node',	type: 'string'},
				{name : 'label', type: 'string'},
				{name: 'statusFlag', type: 'int'},
				{name: 'statusLabel', type: 'string'},
				{name: 'dueDate',	type: 'date', dateFormat: com.trackplus.TrackplusConfig.ISODateFormat},
				{name: 'defaultNoticed',	type: 'boolean'},
				{name: 'defaultScheduled',	type: 'boolean'},
				{name: 'isChild',	type: 'boolean'}];
	},

	/**
	 * Gets the grid columns for the selected node
	 */
	getGridColumns: function(node) {
			return [{text: getText('common.lbl.name'),
				flex:1, dataIndex: 'label', sortable: false, renderer:this.renderer},
			{text: getText('admin.project.release.lbl.status'),
				flex:1, dataIndex: 'statusLabel', sortable: false, renderer:this.renderer},
			{text: getText('admin.project.release.lbl.dueDate'),
				flex:1, dataIndex: 'dueDate', sortable: false,
				renderer:this.dateRenderer},
			{xtype: 'checkcolumn', text: getText('admin.project.release.lbl.defaultReleaseNoticed'),
				flex:1, dataIndex: 'defaultNoticed', sortable: false},
			{xtype: 'checkcolumn', text: getText('admin.project.release.lbl.defaultReleaseScheduled'),
				flex:1, dataIndex: 'defaultScheduled', sortable: false}];
	},

	dateRenderer: function(value, metadata, record) {
		if (record.data.statusFlag===2) {
			metadata.style = 'color:#909090';
		}
		return Ext.util.Format.date(value, com.trackplus.TrackplusConfig.DateFormat);
	},

	/**
	 * Renderer for grid columns
	 */
	renderer: function(value, metadata, record){
		if (record.data.statusFlag===2) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	/**
	 * Return false if dragging this node is not allowed
	 */
	canDragDropNode: function(nodeToDrag, copy, overModel) {
		return nodeToDrag && !nodeToDrag.isAncestor(overModel);
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(selectedNode) {
		this.actionAddChildRelease.setDisabled(CWHF.isNull(selectedNode) || this.rootID===selectedNode.data['id']);
		this.actionEditGridRow.setDisabled(true);
		this.actionDeleteGridRow.setDisabled(true);
		this.actionMoveUp.setDisabled(true);
		this.actionMoveDown.setDisabled(true);
	}/*,

	getRootID: function() {
		return this.getProjectID();
	}*/

});
