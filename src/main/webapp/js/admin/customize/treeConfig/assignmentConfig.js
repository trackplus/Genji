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
 * Although it inherits from TreeConfig which is a treeBase,
 * some behavior is similar to treeWithGrid that's why some methods of
 * treeWithGrid are implemented again (alternatively use multiple inheritance?)
 *
 */
Ext.define("com.trackplus.admin.customize.treeConfig.AssignmentConfig",{
	extend:"com.trackplus.admin.customize.treeConfig.TreeConfig",
	/*config: {
		rootID:'_'
	},*/
	allowMultipleSelections:true,//for delete from grid
	/*textFieldWidth: 350,
	labelWidth: 120,*/
	//actions
	actionApply: null,
	actionAdd: null,
	actionEdit: null,
	actionCopy: null,
	actionDelete: null,
	actionDesign: null,
	actionImport: null,
	actionExport: null,
	baseServerAction: null,

	initCenterPanel: function(centerPanel) {
		this.initTree();
		this.showGrid();
	},

	getEntityConfigLabelSingular: function() {
		return "";
	},

	getEntityConfigLabelPlural: function() {
		return "";
	},

	/**
	 * Add "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getAddLabel: function() {
		return getText(this.getAddTitleKey(), this.getEntityConfigLabelSingular());
	},

	/**
	 * Edit "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getEditLabel: function() {
		return getText(this.getEditTitleKey(), this.getEntityConfigLabelSingular());
	},

	/**
	 * Copy "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getCopyLabel: function() {
		return getText(this.getCopyTitleKey(), this.getEntityConfigLabelSingular());
	},

	/**
	 * Delete "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getDeleteLabel: function() {
		return getText(this.getDeleteTitleKey(), this.getEntityConfigLabelSingular());
	},

	/**
	 * The a message patameterized with the deleteEntityLabel
	 */
	getDeleteEntityMessage: function(titleKey, extraConfig) {
		return getText(titleKey, this.getEntityConfigLabelSingular());
	},

	/**
	 * Design "Form" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getDesignLabel: function() {
		return getText("common.lbl.configure", this.getEntityConfigLabelSingular());
	},

	/**
	 * Import "Forms" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getImportLabel: function() {
		return getText("common.lbl.import", this.getEntityConfigLabelPlural());
	},

	/**
	 * Export "Forms" not "Form assignment" (getEntityLabel() does not fit here)
	 */
	getExportLabel: function() {
		return getText("common.lbl.export", this.getEntityConfigLabelPlural());
	},
	isDisabledAddAction:function(){
		return false;
	},
	isDisabledImportAction:function(){
		return false;
	},
	
	initActions: function() {
		this.callParent();
		this.actionApply = CWHF.createAction("common.btn.apply", "apply",
				"onApply", {tooltip:this.getActionTooltip("common.lbl.apply"), disabled:true});
		var addlabel = this.getAddLabel();
		this.actionAdd = CWHF.createAction(addlabel, this.getAddIconCls(),
				"onAdd", {tooltip:addlabel, labelIsLocalized:true, disabled:this.isDisabledAddAction()});
		this.actionEdit = CWHF.createAction(this.getEditButtonKey(), this.getEditIconCls(),
				"onEdit", {tooltip:this.getEditLabel(), disabled:true});
		this.actionCopy = CWHF.createAction(this.getCopyButtonKey(), this.getCopyIconCls(),
				"onCopy", {tooltip:this.getCopyLabel(), disabled:true});
		this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
				"onDelete", {tooltip:this.getDeleteLabel(), disabled:true});
		this.actionDesign = CWHF.createAction("common.btn.config", "btnConfig",
				"onConfig", {tooltip:this.getDesignLabel(), disabled:true});
		this.actionImport = CWHF.createAction(this.getImportButtonKey(), this.getImportIconCls(),
				"onImport", {tooltip:this.getImportLabel(), disabled:this.isDisabledImportAction()});
		this.actionExport = CWHF.createAction(this.getExportButtonKey(), this.getExportIconCls(),
				"onExport", {tooltip:this.getExportLabel(), disabled:true});
		this.actions = [this.actionApply, this.actionReset, {xtype: 'tbspacer', width: 45, disabled:true}, this.actionAdd,
						this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign, this.actionImport, this.actionExport];
	},

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	/*getToolbarActions: function() {
		return [this.actionApply, this.actionReset, {xtype: 'tbspacer', width: 45, disabled:true}, this.actionAdd,
				this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign, this.actionImport, this.actionExport];
	},*/

	/**
	 * Same as it treeWithGrid
	 */
	getContextMenuActions: function(selectedRecords, selectionIsSimple, fromTree) {
		if (fromTree) {
			return this.getTreeContextMenuActions(selectedRecords, selectionIsSimple);
		} else {
			return this.getGridContextMenuActions(selectedRecords, selectionIsSimple);
		}
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
		if (selectionIsSimple) {
			var selectedTreeNode = this.getSingleSelectedRecord(true);
			if (selectedTreeNode && selectedTreeNode.isLeaf()) {
				return [this.actionApply, this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign];
			} else {
				return [this.actionEdit, this.actionCopy, this.actionDelete, this.actionDesign];
			}
		} else {
			return [this.actionDelete];
		}
	},

	/**
	 * Paste a node in the tree after copy/cut
	 */
	/*saveAssignment: function(draggedObjectID, droppedNodeID) {
		Ext.Ajax.request({
			url: this.getBaseAction() + '!save.action',
			params: {
				assignedID: draggedObjectID,
				node: droppedNodeID
			},
			scope: this,
			disableCaching:true,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success === true) {
					this.refreshAfterSaveSuccess(responseJson);
				} else {
					com.trackplus.util.showError(responseJson);
				}
			},
			failure: function(result){
				Ext.MessageBox.alert(this.failureTitle, result.responseText)
			},
			method:"POST"
		});
	},*/

	/*onTreeNodeSelect: function(rowModel, node, index) {
		var record = this.grid.getStore().getById(node.data['assignedID']);
		if (record) {
			this.selectGridRow(record);
		}
		this.getToolbarActionChangesForTreeNodeSelect(node);
	},*/

	/*isAssignable: function(node) {
		return false;
	},*/

	dragAndDropOnTree:false,
	
	initTree:function(){
		this.callParent(arguments);
		this.initMyListeners();
	},
	
	initMyListeners:function(){
		this.tree.view.addListener("afterrender", "onTreeViewAfterRenderer");
		this.tree.view.addListener("beforedrop", "onTreeViewBeforeDrop");
		this.tree.getView().addListener("drop", "onTreeViewDrop");
	},

	/*onTreeViewAfterRenderer:function(){
		var me=this;
		me.dropZone = Ext.create('com.trackplus.util.TreeDropZone', {
			view: me.tree.view,
			ddGroup: this.baseServerAction,
			isValidNode:function(node) {
				return this.isAssignable(node);
			},
			isValidNodeScope: me
		});
	},*/

	/*onTreeViewBeforeDrop: function(node,data, overModel, dropFunction,eOpts) {
		return this.isAssignable(overModel);
	},
	
	onTreeViewDrop: function(node,data, overModel, dropFunction,eOpts){
		this.onDropTreeNode.call(this,node,data, overModel,dropFunction,eOpts);
		return true;
	},
	
	onDropTreeNode: function(node, data, overModel) {
		var draggedNodeID=data.records[0].data['id'];
		var droppedNodeID=overModel.data['id'];
		if (draggedNodeID && droppedNodeID) {
			this.saveAssignment(draggedNodeID, droppedNodeID);
		}
		return true;
	},*/
	
	
	getGridFields: function() {
		return [
				{name: 'id',type:'int'},
				{name: 'name',type:'string'},
				{name: 'tagLabel',type:'string'},
				{name: 'description', type: 'string'},
				{name: 'owner', type: 'string'}];
	},

	getGridColumns: function() {
		return [{text: getText('common.lbl.name'), flex:1,
			dataIndex: 'name',id:'name',sortable:true,
			filter: {
	            type: "string"
	        }},
		{text: getText('common.lbl.tagLabel'), flex:1,
			dataIndex: 'tagLabel',id:'tagLabel',sortable:true,
			filter: {
	            type: "string"
	        }},
		{text: getText('common.lbl.description'),flex:1,
			dataIndex: 'description',id:'description',sortable:true,
			filter: {
	            type: "string"
	        }},
		{text: getText('admin.customize.form.config.owner'),flex:1,
			dataIndex: 'owner',id:'owner',sortable:true,
			filter: {
	            type: "string"
	        }}]
	},

	/*getSelectedObjectIDs: function() {
		var selectedObjectIDs = new Array();
		var selectedRecordsArr = this.getSelection();
		if (selectedRecordsArr) {
			Ext.Array.forEach(selectedRecordsArr, function(record, index, allItems)
					{selectedObjectIDs.push(record.data['id']);}, this);
		}
		return selectedObjectIDs.join(",");
	},*/

	/*protected abstract*/getGridListURL:function() {
		return "";
	},

	
	/**
	 * Shows the grid according to the selected node
	 */
	showGrid: function() {
		var store = Ext.create('Ext.data.Store', {
			proxy: {
				type: 'ajax',
				url: this.getGridListURL(),
				reader: {
					type: 'json'
				}
			},
			fields: this.getGridFields(),
			idProperty: 'id',
			autoLoad: true
		});
		var gridConfig = {
				xtype: 'grid',
				store: store,
				selModel: Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"}), //for delete/export
				columns: this.getGridColumns(),
				plugins: ["gridfilters"],
				autoWidth: true,
				border: false,
				bodyBorder:false,
				cls:'gridNoBorder',
				stripeRows: true
			};
		gridConfig.viewConfig = {
			plugins: {
				ptype: 'gridviewdragdrop',
				ddGroup: this.baseServerAction,
				enableDrag: true,
				enableDrop: false
			}
		};
		gridConfig.listeners = {selectionchange: "onGridSelectionChange",
								itemcontextmenu:"onGridRowCtxMenu",
								itemdblclick:"onEdit"};
		//create the grid
		this.grid = Ext.create("Ext.grid.Panel", gridConfig);
		this.centerPanel.add(this.grid);
	},

	/**
	 * Enable/disable actions based on the actual selection
	 */
	/*onGridSelectionChange: function (view, selections) {
		this.enableDisableToolbarButtons(view, selections);
		var selectedRow = null;
		if (selections && selections.length>0) {
			selectedRow = selections[0];
		}
		this.adjustToolbarButtonsTooltip(selectedRow, false);
	},*/

	/**
	 * Show the context menu in grid
	 */
	/*onGridRowCtxMenu: function(grid, record, item, index, evtObj) {
		this.onCtxMenu(false, record, evtObj);
		return false;
	},*/

	/**
	 * Enable/disable actions based on the actual selection
	 */
	/*enableDisableToolbarButtons: function (view, selections) {
		if (CWHF.isNull(selections) || selections.length===0) {
			this.actionEdit.setDisabled(true);
			this.actionApply.setDisabled(true);
			this.actionCopy.setDisabled(true);
			this.actionDelete.setDisabled(true);
			this.actionDesign.setDisabled(true);
			this.actionExport.setDisabled(true);
		} else {
			var selectedRecord = selections[0];
			var isLeaf = selectedRecord.data['leaf'];
			this.actionDelete.setDisabled(false);
			this.actionExport.setDisabled(false);
			if (selections.length===1) {
				var selectedTreeNode = this.getSingleSelectedRecord(true);
				if (selectedTreeNode && this.isAssignable(selectedTreeNode)) {
					this.actionApply.setDisabled(false);
				}
				this.actionEdit.setDisabled(false);
				this.actionCopy.setDisabled(false);
				this.actionDesign.setDisabled(false);
			} else {
				this.actionApply.setDisabled(true);
				this.actionEdit.setDisabled(true);
				this.actionCopy.setDisabled(true);
				this.actionDesign.setDisabled(true);
			}
		}
	},*/


	/**
	 * Get the node to select after save after add operation
	 */
	/*getReloadParamsFromResult: function() {
		return [{parameterName:'rowToSelect', fieldNameFromResult:'id'}];

	},*/

	/**
	 * Save the detail part
	 * The 'add' parameter is added as hidden field to the corresponding form detail items
	 * Alternatively this should be included as extra parameter here if 'add' were declared
	 * as class level variable set in onAdd action and reset on treeNode select
	 */
	/*onApply: function() {
		var selectedTreeNode = this.getSingleSelectedRecord(true);
		var selectedGridRow = this.getSingleSelectedRecord(false);
		if (selectedTreeNode && selectedGridRow) {
			this.saveAssignment(selectedGridRow.data['id'], selectedTreeNode.data['id']);
		}
	},*/

	/**
	 * Handler for adding a new screen
	 */
	/*onAdd: function() {
		var title = this.getAddLabel();
		var reloadParamsFromResult = this.getReloadParamsFromResult();
		return this.onAddEdit(title, true,
				null, null, reloadParamsFromResult);
	},*/

	/**
	 * Handler for editing a screen
	 */
	/*onEdit: function() {
		var title = this.getEditLabel();
		var loadParams = this.getEditParams(false);
		var submitParams = this.getEditParams(false);
		var reloadParamsFromResult = this.getReloadParamsFromResult();
		return this.onAddEdit(title, false,
				loadParams, submitParams, reloadParamsFromResult);
	},*/


	/**
	 * Handler for copying a screen
	 */
	/*onCopy: function() {
		var title = this.getCopyLabel();
		var loadParams = this.getEditParams(true);
		var submitParams = this.getEditParams(true);
		var reloadParamsFromResult = this.getReloadParamsFromResult();
		return this.onAddEdit(title, false,
				loadParams, submitParams, reloadParamsFromResult);
	},*/

	/*getObjectIDName: function() {
		return "objectID";
	},*/

	/**
	 * Parameters for editing an existing entity
	 * recordData: the selected entity data
	 */
	/*getEditParams: function(copy) {
		var params = {copy:copy};
		var recordData = this.getSingleSelectedRecordData(false);
		if (recordData) {
			var objectID = recordData['id'];
			if (objectID) {
				params[this.getObjectIDName()] = objectID;
			}
		}
		return params;
	},*/

	/**
	 * Handler for add/edit a node/row
	 * title: 'add'/'edit'/'copy'
	 * recordData: the selected record (tree node data or grid row data)
	 * isLeaf: whether to add a leaf or a folder
	 * add: whether it is add or edit
	 * fromTree: operations started from tree or from grid
	 * loadParams
	 * submitParams
	 * refreshParams
	 * refreshParamsFromResult
	 */
	/*onAddEdit: function(title, add, loadParams, submitParams,
			refreshParamsFromResult) {
		var loadUrl = this.getGridRowEditURL();
		var load = {loadUrl:loadUrl, loadUrlParams:loadParams};
		var submitUrl = this.getGridRowSaveURL();
		var submit = {	submitUrl:submitUrl,
						submitUrlParams:submitParams,
						submitButtonText:getText('common.btn.save'),
						refreshAfterSubmitHandler:com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit,
						refreshParametersAfterSubmit:refreshParamsFromResult};
		var items = this.getPanelItems();
		var windowParameters = {title:title,
			width:this.editWidth,
			height:this.editHeight,
			load:load,
			submit:submit,
			items:items};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},*/

	//reload: com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit,

	/**
	 * Helper for preparing the params
	 * get the ID based on the record and extra config
	 */
	/*getRecordID: function(recordData, extraConfig) {
		//id for both tree or grid/ used maily for delete
		return recordData['id'];
	},*/

	/*onDelete: function() {
		var selectedRecords = this.getSelectedRecords(false);
		if (selectedRecords) {
			this.deleteHandler(selectedRecords);
		}
	},*/

	/*onConfig:function(){
		var me=this;
		var recordData=me.getSingleSelectedRecordData();
		if(CWHF.isNull(recordData)){
			return false;
		}
		var id=recordData.id;
		window.location.href=this.getConfigGridRowURL(id);
	},*/


	/*getPanelItems: function() {
		return [CWHF.createTextField('common.lbl.name','name', {anchor:'100%', allowBlank:false}),
				CWHF.createTextField('common.lbl.tagLabel','tagLabel', {anchor:'100%'}),
				CWHF.createTextAreaField('common.lbl.description','description', {anchor:'100%'})]
	},*/

	/*protected abstract*//*getUploadFileLabel: function() {
		return null;
	},*/

	/**
	 * panel for importing the e-mail templates
	 */
	/*getImportPanel:function() {
		this.formPanel= new Ext.form.FormPanel({
			region:'center',
			border:false,
			bodyStyle: 'padding:5px',
			defaults: {
				labelStyle:'overflow: hidden;',
				margin:"5 5 0 0",
				msgTarget:	'side',
				anchor:	'-20'
			},
			method: 'POST',
			fileUpload: true,
			items: [CWHF.createCheckbox('common.lbl.overwriteExisting', 'overwriteExisting', {labelWidth:200}),
					CWHF.createFileField(this.getUploadFileLabel(), 'uploadFile',
					{itemId:"uploadFile", allowBlank:false, labelWidth:200})]
		});
		return this.formPanel;
	},*/

	/*onImport: function() {
		var submit = [{submitUrl:this.getImportURL(),
				submitButtonText:getText('common.btn.upload'),
				validateHandler: Upload.validateUpload,
				expectedFileType: /^.*\.(xml)$/,
				refreshAfterSubmitHandler:this.reload}];
		var title = getText('common.lbl.upload', this.getEntityConfigLabelPlural());
		var windowParameters = {title:title,
				width:550,
				height:150,
				submit:submit,
				formPanel: this.getImportPanel(),
				cancelButtonText: getText('common.btn.done')};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},*/

	/*onExport: function() {
		var selectedObjectIDs = this.getSelectedObjectIDs();
		attachmentURI=this.getExportURL(selectedObjectIDs);
		window.open(attachmentURI);
	},*/

	/**
	 * Refresh after a successful save
	 */
	/*refreshAfterSaveSuccess: function(result) {
		var refreshTree = result.refreshTree;
		var nodeIdToSelect = result.node;
		var node = this.tree.getStore().getNodeById(nodeIdToSelect);
		var parentNode = null;
		if (node) {
			parentNode = node.parentNode;
		}
		if (refreshTree) {
			this.refreshTreeOpenBranches(this.tree.getRootNode(), false, false, parentNode, nodeIdToSelect);
		}
	}*/
});
