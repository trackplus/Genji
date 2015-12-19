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
Ext.define('com.trackplus.admin.customize.treeConfig.MailTemplateConfigController',{
	extend: "Ext.app.ViewController",
	alias: "controller.mailTemplateConfig",
	mixins: {
		baseController: "com.trackplus.admin.customize.treeConfig.AssignmentConfigController"
	},
	baseServerAction: "mailTemplateConfigItemDetail",
	
	
	
	getGridRowEditURL: function() {
		return "mailTemplate!edit.action";
	},

	getGridRowSaveURL: function() {
		return "mailTemplate!save.action";
	},

	getImportURL: function() {
		return "mailTemplate!importTemplates.action";
	},

	getExportURL: function(selectedObjectIDs) {
		return "mailTemplate!exportMailTemplates.action?mailTemplateIDs="+selectedObjectIDs;
	},

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteUrl: function(extraConfig){
		return "mailTemplate!delete.action";
	},

	getConfigGridRowURL: function(id) {
		return 'mailTemplateEdit.action?templateID='+id;
	},

	getDeleteParamName: function() {
	    return "mailTemplateIDs";
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
	}

});
