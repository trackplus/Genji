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
Ext.define('com.trackplus.admin.customize.treeConfig.MailTemplateConfig',{
	extend:'com.trackplus.admin.customize.treeConfig.AssignmentConfig',
	config: {
		rootID:'_'
	},
	baseAction: "mailTemplateConfigItemDetail",
	/**
	 * The width of the screen edit window
	 */
	editWidth:500,
	/**
	 * The height of the scree edit window
	 */
	editHeight:350,

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.initBase();
	},

	getGridFields: function() {
		return [
			{name: 'id',type:'int'},
			{name: 'name',type:'string'},
			{name: 'tagLabel',type:'string'},
			{name: 'description', type: 'string'}
		];
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
		        }}
		];
	},


	/*initCenterPanel: function(centerPanel) {
		this.initTree();
		this.showGrid();
	},*/

	/**
	 * The message to appear first time after selecting this menu entry
	 * Is should be shown by selecting the root but the root is typically not visible
	 */
	getRootMessage: function(rootID) {
		return getText('admin.customize.mailTemplate.config.lbl.description');
	},

	/**
	 * The localized leaf name
	 */
	getEntityLabel: function() {
		return getText('admin.customize.mailTemplate.config.lbl.mailTemplateAssignment');
	},


	getEntityConfigLabelSingular: function() {
		return getText("admin.customize.mailTemplate.config.lbl.mailTemplate");
	},

	getEntityConfigLabelPlural: function() {
		return getText("admin.customize.mailTemplate.config.lbl.mailTemplates");
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
			if (!inheritedConfig && !defaultConfig) {
				actions.push(this.actionReset);
			}
			if (!leaf) {
				actions.push(this.actionReload);
			}
		}
		return actions;
	},

	getGridListURL: function() {
		return "mailTemplate.action";
	},

	getGridRowEditURL: function() {
		return "mailTemplate!edit.action";
	},

	getGridRowSaveURL: function() {
		return "mailTemplate!save.action";
	},

	getObjectIDName: function() {
		return "objectID";
	},

	getImportURL: function() {
		return "mailTemplate!importTemplates.action";
	},

	getExportURL: function(selectedObjectIDs) {
		return "mailTemplate!export.action?selectedObjectIDs="+selectedObjectIDs;
	},

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteUrl: function(extraConfig){
		return 'mailTemplate!delete.action';
	},

	getConfigGridRowURL: function(id) {
		return 'mailTemplateEdit.action?templateID='+id;
	},

	/*getDeleteParams: function(selectedRecords, extraConfig) {
		return this.getEditParams(false);
	},*/

	getDeleteParamName: function() {
	    return "mailTemplateIDs";
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
		return "admin.customize.mailTemplate.lbl.uploadFile";
	},


	onImport: function() {
		var submit = [{submitUrl:"mailTemplate!importMailTemplates.action",
			submitButtonText:getText('common.btn.upload'),
			validateHandler: Upload.validateUpload,
			expectedFileType: /^.*\.(xml)$/,
			refreshAfterSubmitHandler:this.reload}];
		var title = getText('common.lbl.upload', getText('admin.customize.mailTemplate.lbl.uploadFile'));
		var windowParameters = {title:title,
			width:600,
			height:230,
			submit:submit,
			formPanel: this.getImportPanel(),
			cancelButtonText: getText('common.btn.done')};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	onExport: function() {
		attachmentURI = 'mailTemplate!exportMailTemplates.action?mailTemplateIDs='+this.getSelectedTemplateIDs();
		window.open(attachmentURI);
	},
	getImportPanel:function() {
		var me=this;
		this.formPanel= new Ext.form.FormPanel({
			region:'center',
			border:false,
			bodyStyle: 'padding:5px',
	        autoScroll:true,
			defaults: {
				labelStyle:'overflow: hidden;',
				margin:"5 5 0 0",
				msgTarget:	'side',
				anchor:	'-20'
			},
			method: 'POST',
			fileUpload: true,
			items: [
				CWHF.createCheckboxWithHelp('admin.customize.mailTemplate.lbl.overwriteExisting', 'overwriteExisting', {itemId:'overwriteExisting', labelWidth:250,labelStyle:{overflow:'hidden'},width:300},
					{change: {fn: this.onOverwriteExistingChange, scope:this}}),
				CWHF.createCheckboxWithHelp('admin.customize.mailTemplate.lbl.clearTemplateDefs', 'clearChildren', {itemId:'clearChildren', labelWidth:250,labelStyle:{overflow:'hidden'},width:300,disabled:true}),
				CWHF.createFileField(this.getUploadFileLabel(), 'uploadFile',
					{itemId:"uploadFile",allowBlank:false, labelWidth:250})]
		});
		return this.formPanel;
	},
	onOverwriteExistingChange:function(){
		var checkOverwriteExisting=CWHF.getControl.apply(this.formPanel,['overwriteExisting']);
		var checkClearChildren=CWHF.getWrappedControl.apply(this.formPanel,['clearChildren']);
		if(checkOverwriteExisting.getValue()){
			checkClearChildren.setDisabled(false);
		}else{
			checkClearChildren.setDisabled(true);
			checkClearChildren.setValue(false);
		}
	},


	getSelectedTemplateIDs: function() {
		var selectedTemplateIDs = new Array();
		var selectedRecordsArr = this.getSelection();
		if (selectedRecordsArr) {
			Ext.Array.forEach(selectedRecordsArr, function(record, index, allItems)
			{selectedTemplateIDs.push(record.data['id']);}, this);
		}
		return selectedTemplateIDs.join(",");
	}
});
