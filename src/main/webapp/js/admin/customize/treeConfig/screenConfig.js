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
 * treeWithGrid are implemented again (alternatively multiple inheritance?)
 *
 */
Ext.define('com.trackplus.admin.customize.treeConfig.ScreenConfig',{
	extend:'com.trackplus.admin.customize.treeConfig.AssignmentConfig',
	config: {
		rootID:'_'
	},
	baseAction: "screenConfigItemDetail",
	/**
	 * The width of the screen edit window
	 */
	editWidth:500,
	/**
	 * The height of the screen edit window
	 */
	editHeight:350,
	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.initBase();
	},

	getToolbarActions: function() {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		this.actionImport.setDisabled(!sysAdmin);
		var actions = [this.actionApply, this.actionReset, {xtype: 'tbspacer', width: 45, disabled:true},
					this.actionEdit, this.actionDesign, this.actionImport, this.actionExport];
		return actions;

	},

	enableDisableToolbarButtons: function (view, selections) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		if (CWHF.isNull(selections) || selections.length===0) {
			this.actionEdit.setDisabled(true);
			this.actionApply.setDisabled(true);
			this.actionDesign.setDisabled(true);
			this.actionExport.setDisabled(true);
		} else {
			var selectedRecord = selections[0];
			var isLeaf = selectedRecord.data['leaf'];
			this.actionExport.setDisabled(!sysAdmin);
			if (selections.length===1) {
				var selectedTreeNode = this.getSingleSelectedRecord(true);
				if (selectedTreeNode && this.isAssignable(selectedTreeNode)/*selectedTreeNode.isLeaf()*/) {
					this.actionApply.setDisabled(false);
				}
				this.actionEdit.setDisabled(!sysAdmin);
				this.actionDesign.setDisabled(!sysAdmin);
			} else {
				this.actionApply.setDisabled(true);
				this.actionEdit.setDisabled(true);
				this.actionDesign.setDisabled(true);
			}
		}
	},

	getGridContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sys;
		if (selectionIsSimple) {
			var selectedTreeNode = this.getSingleSelectedRecord(true);
			if (selectedTreeNode && selectedTreeNode.isLeaf()) {
				if (sysAdmin) {
					var actions = [this.actionApply, this.actionEdit, this.actionDesign];
					return actions;
				}else{
					return [this.actionApply];
				}
			} else {
				if(sysAdmin) {
					var actions =  [this.actionEdit, this.actionDesign];
					return actions;
				}
				return [];
			}
		} else {
			if (sysAdmin) {
				var actions = [];
				return actions;

			} else {
				return [];
			}
		}
	},

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText('admin.customize.form.config.lbl.description');
	},

	/**
	 * The localized leaf name
	 */
	getEntityLabel: function() {
		return getText('admin.customize.form.config.lbl.formAssignment');
	},


	getEntityConfigLabelSingular: function() {
		return getText("admin.customize.form.config.lbl.form");
	},

	getEntityConfigLabelPlural: function() {
		return getText("admin.customize.form.config.lbl.forms");
	},

	/**
	 * Which actions to enable/disable depending on tree selection
	 */
	getToolbarActionChangesForTreeNodeSelect: function(node) {
		if (node ) {
			var inheritedConfig=node.data['inheritedConfig'];
			var defaultConfig=node.data['defaultConfig'];
			this.actionReset.setDisabled(inheritedConfig || defaultConfig);
			this.actionApply.setDisabled(!this.isAssignable(node));
		}
	},

	isAssignable: function(node) {
		return node.isLeaf();
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getTreeContextMenuActions: function(selectedRecord, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inheritedConfig = selectedRecord.data['inheritedConfig'];
			var defaultConfig = selectedRecord.data['defaultConfig'];
			var leaf = selectedRecord.isLeaf();
			var selectedTreeNode = this.getSingleSelectedRecord(true);
			if (!inheritedConfig && !defaultConfig&&selectedTreeNode && this.isAssignable(selectedTreeNode)) {
				actions.push(this.actionReset);
			}
			if (!leaf) {
				actions.push(this.actionReload);
			}
		}
		return actions;
	},

	getGridListURL: function() {
		return "indexScreens.action";
	},

	getGridRowEditURL: function() {
		return "indexScreens!edit.action";
	},

	getGridRowSaveURL: function() {
		return "indexScreens!save.action";
	},

	getObjectIDName: function() {
		return "screenID";
	},

	getImportURL: function() {
		return this.getBaseAction() + "!importScreens.action";
	},

	getExportURL: function(selectedObjectIDs) {
		return this.getBaseAction() + "!export.action?selectedObjectIDs="+selectedObjectIDs;
	},

	/**
	 * The struts action for delete/replace
	 */
	/*protected*/getDeleteUrlBase: function(extraConfig) {
		return "indexScreens";
	},

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*getDeleteUrl: function(extraConfig){
		return 'indexScreens!delete.action';
	},*/

	getConfigGridRowURL: function(id) {
		return 'screenEdit.action?componentID='+id;
	},

	/*getDeleteParams: function(selectedRecords, extraConfig) {
		return this.getEditParams(false);
	},*/

	getDeleteParamName: function() {
	    return "selectedScreenIDs";
	},

	getPanelItems: function() {
		return  [CWHF.createTextField('common.lbl.name','name',
					{anchor:'100%', allowBlank:false, labelWidth:this.labelWidth, width:this.textFieldWidth}),
				CWHF.createTextField('common.lbl.tagLabel','tagLabel',
					{anchor:'100%', labelWidth:this.labelWidth, width:this.textFieldWidth,itemId:'tagLabel'}),
				CWHF.createTextAreaField('common.lbl.description','description',
					{anchor:'100%', labelWidth:this.labelWidth, width:this.textFieldWidth})];
	},

	getUploadFileLabel: function() {
		return "admin.customize.form.import.lbl.uploadFile";
	},

	onImport: function() {
		var submit = [{submitUrl:this.getBaseAction() + "!importScreens.action",
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
	},

	onExport: function() {
		var attachmentURI=this.getBaseAction() + "!export.action?selectedScreenIDs="+this.getSelectedScreenIDs();
		window.open(attachmentURI);
	},

	getSelectedScreenIDs:function(){
		var selectedTemplateIDs = new Array();
		var selectedRecordsArr = this.getSelection();
		if (selectedRecordsArr) {
			Ext.Array.forEach(selectedRecordsArr, function(record, index, allItems)
			{selectedTemplateIDs.push(record.data['id']);}, this);
		}
		return selectedTemplateIDs.join(",");
	}
});
