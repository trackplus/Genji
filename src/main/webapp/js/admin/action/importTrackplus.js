/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

Ext.define('com.trackplus.admin.action.ImportTrackplus',{
	extend:'Ext.Base',
	config: {
	},
	mainComponent: null,

	createToolbar:function(){
		var btnImport=new Ext.Button({
				text:getText('common.btn.import'),
				overflowText:getText('common.btn.import'),
				tooltip:getText('common.btn.import'),
				iconCls: 'import',
				disabled:false,
				scope:this,
				handler:function(){
					this.startImport();
				}
			});
		return [btnImport];
	},

	createMainComponent: function() {
		var panelConfig = {
			fileUpload:	true,
			//bodyStyle:	'padding: 5px',
			border: false,
			/*defaults: {
				labelStyle:'overflow: hidden;',
				//margin:"5 5 0 0",
				msgTarget:	'side',
				anchor:	'-20',
			},*/
			method: "POST",
			autoScroll:	true,
			items: [{xtype: 'component',
					cls:"infoBox_bottomBorder",
					border:true,
					html: getText("admin.actions.importTp.warning")
					},
					CWHF.createFileField(
						getText("common.lbl.file", getText("admin.actions.importTp.lbl.uploadFile")), "uploadFile",
						{allowBlank:false, labelWidth:250, width: 700, labelIsLocalized: true, padding: '5 5 5 5'})]
			};
		this.mainComponent = Ext.create('Ext.form.Panel', panelConfig);
		return this.mainComponent;
	},

	validateFileExtension: function(fileName) {
		var exp = /^.*\.(zip|ZIP)$/;
		return exp.test(fileName);
	},

	startImport: function() {
		if (!this.mainComponent.getForm().isValid()) {
			Ext.MessageBox.alert(getText('admin.actions.importTp.lbl.uploadFileNotSpecified'),
					getText('admin.actions.importTp.lbl.uploadFileNotSpecified'));
			return false;
		}
		var importFile = this.mainComponent.getComponent('uploadFile');
		if (!this.validateFileExtension(importFile.getRawValue())) {
			Ext.MessageBox.alert(getText('admin.actions.importTp.lbl.uploadFileWrongType'),
				getText('common.err.fileExpectedType', "zip"));
			return false;
		}
		this.mainComponent.setLoading(getText("admin.actions.importTp.lbl.waitMessage"));
		this.mainComponent.getForm().submit({
			url: "trackplusUpload.action",
			scope: this,
			method: "POST",
			success: function(form, action) {
				this.mainComponent.setLoading(false);
				Ext.MessageBox.alert(action.result["title"], action.result["message"]);
			},
			failure: function(form, action) {
				this.mainComponent.setLoading(false);
				com.trackplus.util.submitFailureHandler(form, action);
			}
		})
	}
});
