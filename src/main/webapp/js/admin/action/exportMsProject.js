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

Ext.define('com.trackplus.admin.action.ExportMsProject',{
	extend:'Ext.Base',
	config: {
		projectID: null
	},

	constructor : function(config) {
		var me = this;
		var config = config || {};
		this.initConfig(config);
	},
	exportButton:null,
	panelForm:null,
	dialogHeight:470,
	dialogWidth:785,

	/**
	 * Initialize all actions and return the toolbar actions
	 */
	getToolbarActions: function() {
		if (CWHF.isNull(this.exportButton)) {
			this.exportButton = new Ext.Button({
					text:getText('common.btn.export'),
					tooltip:getText('common.btn.export'),
					iconCls: 'export',
					disabled:false,
					scope: this,
					handler:function(){
						this.exportToMsProject.call(this);
					}
				});
			}
		return [this.exportButton];
	},

	exportToMsProject:function() {
		//standardSubmit, no success and failure handlers are called
		this.panelForm.getForm().submit({
			method :'POST',
			scope:this
			/*success: function(form, action) {
			},
			failure: function(form, action) {
				Ext.Msg.alert(getText("common.err.failure"), action.result.errorMessage);
			}*/
		});
	},

	getDetailPanel: function() {
	    var releasePicker = CWHF.createSingleTreePicker("admin.actions.importTp.lbl.fromProjectRelease",
	        "projectOrReleaseID", [], null,
	        {itemId:'projectOrReleaseID',
	    	allowBlank:false,
	         labelWidth:200,
	         width:500,
	         padding: '10 0 0 0'
	        }, {select:{fn: this.onProjectReleaseSelect, scope:this}})
		var notClosedCheckBox = CWHF.createCheckbox("admin.actions.exportMSProject.lbl.notClosed", "notClosed", {itemId:"notClosed", labelWidth:200, width:250});
		var importFileInfo = {xtype:"label", itemId:"importFileInfo", cls:"infoBox_bottomBorder",
				border:true, html: "MsProject", anchor:'100%'};
		this.panelForm= new Ext.form.FormPanel({
			url:"msProjectExport!export.action",
			region: 'center',
			border: false,
			//autoScroll: true,
			standardSubmit: true,
			//bodyStyle: 'padding:10px',
			margin:'-10 0 0 -10',
			items: [importFileInfo, releasePicker, notClosedCheckBox]
		});
		this.panelLoad();
		return this.panelForm;
	},

	/**
	 * Change event, not
	 * @param projectPicker
	 * @param selectedProjects
	 * @param oldValue
	 * @param options
	 */
	onProjectReleaseSelect: function(releasePicker, selectedRelease, options) {
	    Ext.Ajax.request({
	            url:"msProjectExport!importFileInfo.action",
	            params: {projectOrReleaseID: releasePicker.getValue()},
	            scope: this,
	            disableCaching:true,
	            success: function(response) {
	                var panel = releasePicker.up("panel");
	                var data = Ext.decode(response.responseText);
	                this.replaceImportFileInfo(panel, data["importFileInfo"]);
	            },
	            failure:function(result){
	                com.trackplus.util.requestFailureHandler(response);
	            }
	        }
	    );
	},

	panelLoad: function() {
		params = {projectOrReleaseID:-this.getProjectID()};
		this.panelForm.getForm().load({
			url: "msProjectExport.action",
			scope: this,
			params: params,
			success : function(form, action) {
				try{
					this.postLoadProcess(this.panelForm, action.result.data);
				}catch(ex){}
			},
			failure: function(form, action) {
				Ext.MessageBox.show(getText("common.err.failure"), action.response.responseText);
			}
		})
	},

	postLoadProcess: function(panel, data) {
		var projectReleasesPicker = panel.getComponent("projectOrReleaseID");
	    //projectReleasesPicker.setSubmitValue(data["selectedProjectReleaseID"]);
	    projectReleasesPicker.updateMyOptions(data["projectReleaseTree"]);
	    projectReleasesPicker.setValue(data["selectedProjectReleaseID"]);
	    this.replaceImportFileInfo(panel, data["importFileInfo"]);
	    //this.replaceImportFileInfo(panel, data);
		/*var importFileInfo = panel.getComponent("importFileInfo");
		if (importFileInfo) {
			panel.remove(importFileInfo);
		}
		panel.insert(0, {
			xtype: "component",
			itemId: "importFileInfo",
			cls:"infoBox_bottomBorder",
			border:true,
			html: data["importFileInfo"]
		});
		//importFileInfo = panel.getComponent("importFileInfo");
		//importFileInfo.setText(data["importFileInfo"]);*/
	},

	replaceImportFileInfo: function(panel, importFileInfo) {
	    var importFileInfoComponent = panel.getComponent("importFileInfo");
	    if (importFileInfoComponent) {
	        panel.remove(importFileInfoComponent);
	    }
	    panel.insert(0, {
	        xtype: "component",
	        itemId: "importFileInfo",
	        cls:"infoBox_bottomBorder",
	        border:true,
	        html: importFileInfo
	    });
	},

	/**
	 * This function initialize necessary components for showing pop
	 * up dialog
	 */
	createPopUpDialog: function() {
		var me = this;
			me.isFromDragAndDrop = false;
			var windowParameters = {
				title: getText('itemov.btn.exportMSProject'),
				width: me.dialogWidth,
				height: me.dialogHeight,
				overrideButtons: me.getToolbarActions(),
				items: me.getDetailPanel()
			};
			var window = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
			window.showWindowByConfig(me);
//			me.actualizeToolbar(me.wizardPanel);

	}
});
