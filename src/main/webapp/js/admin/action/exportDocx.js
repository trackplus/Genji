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

Ext.define("com.trackplus.admin.action.ExportDocx",{
	extend:"Ext.container.Container",
	config: {
		//selected document to export from wiki
		workItemID: null,
		//selected items to export from item navigator
		workItemIDs: null
	},

	initComponent: function() {
		this.callParent();
	},


	createExportForm: function(fromTree, record) {
		var recordData = null;
		if (record) {
			recordData = record.data;
		}
		var width = 500;
		var height = 150;

		var loadUrl = "docxTemplate.action";
		//var loadParams = this.getEditParams(fromTree);
		var load = {loadUrl:loadUrl/*, loadUrlParams:loadParams*/};
		var submitParams = {workItemID:this.workItemID, workItemIDs: this.workItemIDs};
		var submit = [{
						submitUrl:"docxExport.action",
						submitUrlParams:submitParams,
						submitButtonText:getText("common.btn.export"),
						submitHandler:this.exportHandler
					},
		            {
						submitUrl:"docxTemplate!upload.action",
						submitButtonText:getText("common.btn.upload"),
						submitHandler:this.uploadFileHandler
					},
					{
						submitUrl:"docxTemplate!download.action",
						submitButtonText:getText("common.btn.download"),
						submitHandler:this.downloadFileHandler
					},
					{
						submitUrl:"docxTemplate!delete.action",
						submitButtonText:getText("common.btn.delete"),
						submitHandler:this.deleteUploadedFileHandler
					}];
		var postDataProcess = this.renderUploadPostDataProcess;
		var title = getText("admin.actions.exportDocx.chooseTemplate");
		var windowParameters = {title:title,
			width:width,
			height:height,
			load:load, submit:submit,
			formPanel: this.getFormPanel(),
			postDataProcess:postDataProcess,
			cancelButtonText: getText('common.btn.cancel')
			};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	getFormPanel: function(fromTree, selectedEntryID) {
	  	var items = [CWHF.createCombo("admin.actions.exportDocx.templates", "docxTemplateList",
	  					{idType:"string", labelWidth:150, blankText:getText("admin.actions.exportDocx.docxTemplateNotSelected"), itemId:"docxTemplateList"}),
					CWHF.createFileField("admin.actions.exportDocx.newTemplate", "docxTemplate",
						{itemId:"docxTemplate", labelWidth:150, blankText:getText("admin.actions.exportDocx.docxTemplateNotBrowsed")})];
		return Ext.create('Ext.form.Panel', {
			bodyStyle: 'padding:5px',
			//url: 'listOptionIcon!upload.action',
			defaults: {
				labelStyle:'overflow: hidden;',
				margin:"5 5 0 0",
				msgTarget:	'side',
				anchor:	'-20'
			},
			method: 'POST',
			fileUpload: true,
			items: items
			}
		);
	},

	exportHandler:function(win, submitUrl, submitUrlParams) {
		var submitUrl = this.getUrlWithTemplate(submitUrl);
		if (submitUrlParams) {
			for (submitUrlParam in submitUrlParams) {
				submitUrl = submitUrl + "&" + submitUrlParam + "=" + submitUrlParams[submitUrlParam];
			}
		}
		window.open(submitUrl);
		win.close();
	},

	downloadFileHandler: function(win, submitUrl) {
		var submitUrl = this.getUrlWithTemplate(submitUrl);
		window.open(submitUrl);
	},

	getUrlWithTemplate: function(submitUrl) {
		var theForm = this.formEdit.getForm();
		var templatesCombo = this.formEdit.getComponent("docxTemplateList");
		var selectedTemplate = templatesCombo.getValue();
		if (CWHF.isNull(selectedTemplate)) {
			templatesCombo.allowBlank = false;
			var docxTemplate = this.formEdit.getComponent("docxTemplate");
			if (docxTemplate) {
				docxTemplate.allowBlank = true;
			}
			theForm.isValid();
			Ext.MessageBox.alert(getText("admin.actions.exportDocx.chooseTemplate"),
					getText("admin.actions.exportDocx.docxTemplateNotSelected"));
			templatesCombo.allowBlank = true;
			return;
		}
		submitUrl = submitUrl + "?docxTemplateFileName="+selectedTemplate;
		return submitUrl;
	},

	renderUploadPostDataProcess: function(data, formPanel) {
	    var templatesCombo = formPanel.getComponent("docxTemplateList");
	    templatesCombo.store.loadData(data["existingTemplates"]);
	    var uploadedTemplate = data["docxTemplateList"];
	    //if (uploadedTemplate) {
	    	templatesCombo.setValue(uploadedTemplate);
	    //}
		formPanel.getComponent("docxTemplate").setValue('');
		var toolbars = this.win.getDockedItems('toolbar[dock="bottom"]');
		if (toolbars) {
			//disable delete button if no icon is specified
			toolbars[0].getComponent(0).setDisabled(CWHF.isNull(uploadedTemplate));
			toolbars[0].getComponent(2).setDisabled(CWHF.isNull(uploadedTemplate));
			toolbars[0].getComponent(3).setDisabled(CWHF.isNull(uploadedTemplate));
		}
	},

	uploadFileHandler: function(window, submitUrl, submitUrlParams) {
		var theForm = this.formEdit.getForm();
		var docxTemplate = this.formEdit.getComponent("docxTemplate");
		var docxTemplateValue = docxTemplate.getValue();
		if (CWHF.isNull(docxTemplateValue) || docxTemplateValue==="") {
			docxTemplate.allowBlank = false;
			var templatesCombo = this.formEdit.getComponent("docxTemplateList");
			if (templatesCombo) {
				templatesCombo.allowBlank = true;
			}
			theForm.isValid();
			Ext.MessageBox.alert(getText("admin.actions.exportDocx.chooseTemplate"),
					getText("admin.actions.exportDocx.docxTemplateNotBrowsed"));
			docxTemplate.allowBlank = true;
			return;
		}
		var expr = /^.*\.(docx|DOCX|tex|TEX|tlx|zip)$/;
		if (!expr.test(docxTemplateValue)) {
			Ext.MessageBox.alert(getText("admin.actions.exportDocx.chooseTemplate"),
					getText("admin.actions.exportDocx.docxTemplateWrongType"));
				return;
		}
		theForm.submit({
			scope: this,
			url: submitUrl,
			params: submitUrlParams,
			success: function(form, action) {
				this.renderUploadPostDataProcess(action.result.data, this.formEdit);
			},
			failure: function(form, action) {
				com.trackplus.util.submitFailureHandler(form, action);
			}
		})
	},

	deleteUploadedFileHandler : function(window, submitUrl, submitUrlParams) {
		var theForm = this.formEdit.getForm();
		var templatesCombo = this.formEdit.getComponent("docxTemplateList");
		var selectedTemplate = templatesCombo.getValue();
		if (CWHF.isNull(selectedTemplate)) {
			templatesCombo.allowBlank = false;
			var docxTemplate = this.formEdit.getComponent("docxTemplate");
			if (docxTemplate) {
				docxTemplate.allowBlank = true;
			}
			theForm.isValid();
			//templatesCombo.validate();
			Ext.MessageBox.alert(getText("admin.actions.exportDocx.chooseTemplate"),
					getText("admin.actions.exportDocx.docxTemplateNotSelected"));
			templatesCombo.allowBlank = true;
			return;
		}
		theForm.submit({
			scope: this,
			url: submitUrl,
			//params: submitUrlParams,
			success: function(form, action) {
				this.renderUploadPostDataProcess(action.result.data, this.formEdit);
			},
			failure: function(form, action) {
				com.trackplus.util.submitFailureHandler(form, action);
			}
		})
	}
});

