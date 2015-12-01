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

Ext.define("com.trackplus.admin.user.PersonController", {
	extend: "Ext.app.ViewController",
	alias: "controller.person",
	mixins: {
		baseController: "com.trackplus.admin.GridBaseController"
	},
	baseAction:"person",
	entityID:"objectID", 
	editWidth : 780,
	editHeight : 500,
	
	getSelectedPersonIDs: function() {
		var selectedRecords = this.getView().getSelectedRecords();
		return this.getSelectedIDs(selectedRecords);
	},

	/**
	 * Enable change only if not inherited
	 */
	onSelectionChange: function(view, arrSelections) {
		if (CWHF.isNull(arrSelections) || arrSelections.length===0){
			this.getView().actionDelete.setDisabled(true);
			this.getView().actionEdit.setDisabled(true);
			this.getView().actionActivate.setDisabled(true);
			this.getView().actionDeactivate.setDisabled(true);
			this.getView().actionUserLevel.setDisabled(true);
			this.getView().actionRoleAssignments.setDisabled(true);
	        this.getView().actionFilterAssignments.setDisabled(true);
			this.getView().actionCokpitAssignment.setDisabled(true);
		} else {
			if (arrSelections.length===1) {
				this.getView().actionEdit.setDisabled(false);
				this.getView().actionRoleAssignments.setDisabled(false);
			} else {
				this.getView().actionEdit.setDisabled(true);
				this.getView().actionRoleAssignments.setDisabled(true);
	        }
	        this.getView().actionDelete.setDisabled(false);
			this.getView().actionActivate.setDisabled(false);
			this.getView().actionDeactivate.setDisabled(false);
			this.getView().actionUserLevel.setDisabled(false);
	        this.getView().actionFilterAssignments.setDisabled(false);
			this.getView().actionCokpitAssignment.setDisabled(false);
		}
	},
	
	/*protected*/getDeleteParamName: function() {
	    return "selectedPersonIDs";
	},

	onActivate: function() {
		Ext.Ajax.request({
			url: this.getBaseAction() + "!activate.action",
			params: {selectedPersonIDs: this.getSelectedPersonIDs()},
			scope: this,
			success: function(response) {
				var result = Ext.decode(response.responseText);
				if (result.success) {
					this.reload();
				} else {
					com.trackplus.util.showError(result);
				}
			},
			failure: function(response) {
				Ext.MessageBox.alert(this.failureTitle, response.responseText);
			}
		});
	},

	onDeactivate: function() {
		Ext.Ajax.request({
			url: this.getBaseAction() + "!deactivate.action",
			params: {selectedPersonIDs: this.getSelectedPersonIDs()},
			scope: this,
			success: function(response) {
				var result = Ext.decode(response.responseText);
				if (result.success) {
					this.reload();
				} else {
					com.trackplus.util.showError(result);
				}
			},
			failure: function(response) {
				Ext.MessageBox.alert(this.failureTitle, response.responseText);
			}
		})
	},

	onUserLevel: function(userLevel) {
		var submitUrl = this.getBaseAction() + "!userLevel.action";
		var submitParams = {selectedPersonIDs: this.getSelectedPersonIDs()}
		var submit = {submitUrl:submitUrl,
					submitUrlParams:submitParams,
					submitButtonText:getText("common.btn.save"),
					refreshAfterSubmitHandler:this.reload
					};
		var items = this.getUserLevelItems();
		var windowParameters = {title:getText("common.btn.userLevel"),
			width:300,
			height:120,
			submit:submit,
			items:items};
		var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	getUserLevelItems: function() {
		var sysAdmin=com.trackplus.TrackplusConfig.user.sysAdmin;
		var data = null;
		if (sysAdmin) {
			data = this.getView().getUserLevels();
		} else {
			//a system manager might not set new system admins
			data = [];
			Ext.Array.forEach(this.getView().getUserLevels(), function(record) {
					var userLevelID = record["id"];
					if (userLevelID && userLevelID!==2) {
						data.push(record);
					}
					},
					this);
		}
		return [CWHF.createCombo("admin.user.manage.lbl.userLevel", "userLevel",
				{data:data, labelWidth:100, allowBlank:false})];
	},

	onShowAssignments: function() {
		var title = getText("admin.user.manage.lbl.roleAssignments");
		var submitParams = this.getEditParams();
		var load = {loadHandler: this.showAssignmentsLoadHandler};
		var personID = null;
		var recordData = this.getView().getSingleSelectedRecordData();
		if (recordData) {
			personID = recordData["id"];
		}
		var windowParameters = {title:title,
			width:500,
			height:350,
			load:load,
			//submit:submit, no submit
			cancelButtonText: getText("common.btn.done"),
			layout:"border",
			formPanel: this.getRoleAssignmentGrid(personID)};
		var windowConfig = Ext.create("com.trackplus.util.WindowConfig", windowParameters);
		windowConfig.showWindowByConfig(this);
	},

	onFilterAssignments: function() {
	    var title = getText("admin.user.manage.lbl.filterAssignments");
	    var submitParams = this.getEditParams();
	    var load = {loadHandler: this.showAssignmentsLoadHandler};
	    var personIDs = this.getSelectedPersonIDs();
	    var windowParameters = {title:title,
	        width:500,
	        height:350,
	        load:load,
	        //submit:submit, no submit
	        cancelButtonText: getText("common.btn.done"),
	        layout:"border",
	        formPanel: this.getFilterAssignmentGrid(personIDs)};
	    var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
	    windowConfig.showWindowByConfig(this);
	},

	onCokpitAssignment: function() {
		var me=this;
		com.trackplus.dashboard.resetDashboard(this.getBaseAction() + "!cokpitAssignment.action",
			{selectedPersonIDs: this.getSelectedPersonIDs()}, me.cokpitAssignmentSuccess,me);
	},

	cokpitAssignmentSuccess: function() {
		this.reload();
	},

	/**
	 * Empty (but not null) function to avoid the calling of getForm().load() in windowConfig
	 * loadUrl and loadParams is not specified because the data will be loaded through the grid's store
	 */
	showAssignmentsLoadHandler: function(loadUrl, loadParams) {

	},

	getRoleAssignmentGrid: function(personID) {
		var gridComponent = Ext.create("com.trackplus.admin.user.UserRolesInProject",
				{personID:personID, group:false});
		//var grid = gridComponent.getGrid();
		//grid.store.load();
		return [gridComponent];
	},

	getFilterAssignmentGrid: function(personIDs) {
	    var gridComponent = Ext.create("com.trackplus.admin.user.FiltersInUserMenu",
	        {personIDs:personIDs});
	    //var grid = gridComponent.getGrid();
	    return [gridComponent];
	},

	onSyncLdap: function() {
		Ext.Ajax.request({
			url: this.getBaseAction() + "!syncldap.action",
			scope: this,
			success: function(response) {
				var result = Ext.decode(response.responseText);
				if (result.success) {
					this.reload();
				} else {
					com.trackplus.util.showError(result);
				}
			},
			failure: function(response) {
				Ext.MessageBox.alert(this.failureTitle, response.responseText);
			}
		})
	},

	/**
	 * Parameters for loading the edited entity
	 * recordData: the selected entity data
	 * action: the submit action
	 */
	getLoadParams: function(recordData, operation) {
		return {context:this.getContext(recordData, operation),
			personId: recordData['id']};
	},

	getSaveParams: function(recordData, operation){
		return {isUser:this.getView().getIsUser(), context:this.getContext(recordData, operation),
			personId: recordData['id']};
	},

	getContext: function(recordData, operation) {
		var objectID = recordData['id'];
		if (CWHF.isNull(objectID)) {
			return this.getView().profile.CONTEXT.USERADMINADD;
		} else {
			return this.getView().profile.CONTEXT.USERADMINEDIT
		}
	},

	/**
	 * Url for editing an entity
	 * We suppose that add/edit/copy use the same edit method on server side
	 * Differentiation is made based on this.entityID and copy request parameter
	 * add: this.entityID===null
	 * edit: this.entityID && copy===false
	 * copy: this.entityID && copy===true
	 */
	getEditUrl: function(recordData, operation) {
		var context = this.getContext(recordData, operation);
		return this.getView().profile.getEditUrl(context);
	},

	/**
	 * Url for editing an entity
	 * We suppose that add/edit/copy use the same edit method on server side
	 * Differentiation is made based on this.entityID and copy request parameter
	 * add: this.entityID===null
	 * edit: this.entityID && copy===false
	 * copy: this.entityID && copy===true
	 */
	getSaveUrl: function(recordData, operation) {
		return "userProfile!save.action";
	},

	/**
	 * Create the form to edit
	 */
	createEditForm: function(recordData, operation) {
		var context = this.getContext(recordData, operation);
		this.getView().profile.context = context;
		this.getView().profile.personId = recordData['id'];
		return this.getView().profile.createMainForm();
	},

	/**
	 * Extra processing that should be done in the edit panel after the data is loaded from the server
	 * like load combo data, show/hide contols
	 */
	afterLoadForm: function(data, panel) {
		this.getView().profile.postDataLoadCombos(data, panel);
	}
});
