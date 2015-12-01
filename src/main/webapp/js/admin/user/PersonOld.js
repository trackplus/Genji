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

Ext.define("com.trackplus.admin.user.Person", {
	extend:	"com.trackplus.admin.GridBase",
	config: {
		context: null,
		isUser: null,
		userLevels:null,
		featureList: null
	},
	autoLoadGrid:false,
	enableColumnHide: true,
	enableColumnMove: true,
	allowMultipleSelections:true,
	confirmDeleteEntity:true,
	baseAction:"person",
	entityID:"objectID",
	editWidth:780,
	editHeight:500,
	profile:null,
	actionActivate:null,
	actionDeactivate:null,
	actionUserLevel:null,
	actionRoleAssignments: null,
	actionFilterAssignments: null,
	actionCokpitAssignment:null,
	actionSyncLdap: null,

	constructor: function(config) {
		var config = config || {};
		this.profile=Ext.create("com.trackplus.admin.user.Profile",{context:config.context, isUser: config.isUser});
		this.initConfig(config);
		this.initPerson();
	},
	
	getEntityLabel: function() {
		return getText("admin.user.lbl.user");
	},
	
	initPerson:function(){
		this.initActions();
		Ext.Ajax.request({
			url: this.getBaseAction() + "!getLdapIsOn.action",
			scope: this,
			success: function(response) {
				var result = Ext.decode(response.responseText);
				var ldapIsDisabled;
				if (result.value) {
					ldapIsDisabled = false;
				} else {
					ldapIsDisabled = true;
				}
				this.actionSyncLdap.setDisabled(ldapIsDisabled);
			},
			failure: function(response) {
				Ext.MessageBox.alert(this.failureTitle, response.responseText);
			}
		});
	},

	initActions:function() {
		this.actionAdd = this.createAction(this.getAddButtonKey(), this.getAddIconCls(), this.onAdd, false, this.getAddTitleKey());
		this.actionEdit = this.createAction(this.getEditButtonKey(), this.getEditIconCls(), this.onEdit, true, this.getEditTitleKey());
		this.actionDelete = this.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(), this.onDelete, true, this.getDeleteTitleKey());
		this.actionActivate = this.createAction("common.btn.activate", this.getActivateIconCls(), this.onActivate, true, "common.lbl.activate");
		this.actionDeactivate = this.createAction("common.btn.deactivate", this.getDeactivateIconCls(), this.onDeactivate, true, "common.lbl.deactivate");
		this.actionUserLevel = this.createAction("common.btn.userLevel", "personSystemAdmin", this.onUserLevel, true, "common.btn.userLevel");
		this.actionRoleAssignments = this.createAction("admin.user.manage.lbl.roleAssignments",
				this.getRoleAssignCls(), this.onShowAssignments, true, "admin.user.manage.lbl.roleAssignments");
	    this.actionFilterAssignments = this.createAction("admin.user.manage.lbl.filterAssignments",
	        this.getFilterAssignCls(), this.onFilterAssignments, true, "admin.user.manage.lbl.filterAssignments");
		this.actionCokpitAssignment=this.createAction("admin.user.manage.lbl.cokpitAssignment", this.getCokpitAssignmentIconCls(), this.onCokpitAssignment, true, "admin.user.manage.lbl.cokpitAssignment");
		this.actionSyncLdap = this.createAction("common.btn.syncLdap",this.getAddIconCls(),this.onSyncLdap, false, "common.btn.syncLdap.tt");
		this.actions=[this.actionAdd, this.actionEdit, this.actionDelete,
					this.actionActivate, this.actionDeactivate,
					this.actionUserLevel];
		if(this.getIsUser()) {
			this.actions.push(this.actionRoleAssignments);
		}
	    this.actions.push(this.actionFilterAssignments);
	    if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
	    	this.actions.push(this.actionCokpitAssignment);
	    }
		if(this.getIsUser()) {
			this.actions.push(this.actionSyncLdap);
		}
	},

	getGridFields: function() {
		var gridFields = [{name: "id", type: "int"},
			{name: "userName", type: "string"},
			{name: "name", type: "string"},
			{name: "userEmail",	type: "string"},
			{name: "active", type: "boolean"},
			{name: "activeLabel", type: "string"},
			{name: "userLevelLabel", type: "string"},
			{name: "locale", type: "string"},
			{name: "phone", type: "string"},
			{name: "employeeId", type: "string"},
			{name: "lastLogin",	type: "date", dateFormat: com.trackplus.TrackplusConfig.ISODateTimeFormat},
			{name: "department", type: "string"},
			{name: "substituteName", type: "string"}];
		if (this.getIsUser() && this.getFeatureList()) {
			Ext.Array.forEach(this.getFeatureList(), function(feature) {
				var featureID = feature["featureID"];
				gridFields.push({name: featureID, type:"boolean"});
				}, this);
		}
		return gridFields;
	},

	getViewConfig: function() {
		return {
			forceFit: true,
			markDirty: false
		};
	},

	/*getGridFeatures: function(userLevels) {
		var userLevelLabels = [];
		if (userLevels) {
			Ext.Array.forEach(userLevels, function(userLevel) {
				var label = userLevel["label"];
				userLevelLabels.push(label);
				}, this);
		}
		return [{
			ftype: "filters",
			encode: false, // json encode the filter query
			local: true, // defaults to false (remote filtering)
			filters: [{
					type: "string",
					dataIndex: "userName"
				},{
					type: "string",
					dataIndex: "name"
				},{
					type: "string",
					dataIndex: "userEmail"
				},{
					type: "boolean",
					dataIndex: "active"
				},{
					type: "list",
					dataIndex: "userLevelLabel",
					options: userLevelLabels
				},{
					type: "list",
					dataIndex: "activeLabel",
					options: [getText("common.boolean.Y"),
						getText("common.boolean.N")]
				},{
					type: "string",
					dataIndex: "department"
				}]
		}}];
	},*/

	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls: function() {
		return "personAdd";
	},

	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getEditIconCls: function() {
		return "personEdit";
	},

	/**
	 * The iconCls for the activate button
	 */
	getActivateIconCls: function() {
		return "personActivate";
	},

	/**
	 * The iconCls for the deactivate button
	 */
	getDeactivateIconCls: function() {
		return "personDeactivate";
	},

	getRoleAssignCls: function() {
		return "roleAssign";
	},

	getFilterAssignCls: function() {
	    return "filter";
	},

	getCokpitAssignmentIconCls: function() {
		return "cockpitReset";
	},

	getSelectedPersonIDs: function() {
		var selectedPersonIDs = new Array();
		var selectedRecordsArr = this.getGridSelection();
		if (selectedRecordsArr) {
			Ext.Array.forEach(selectedRecordsArr, function(record, index, allItems)
					{selectedPersonIDs.push(record.data["id"]);},
					this);
		}
		return selectedPersonIDs.join(",");
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
			data = this.getUserLevels();
		} else {
			//a system manager might not set new system admins
			data = [];
			Ext.Array.forEach(this.getUserLevels(), function(record) {
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
		var recordData = this.getSingleSelectedRecordData(true);
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
		var grid = gridComponent.getGrid();
		//grid.store.load();
		return [grid];
	},

	getFilterAssignmentGrid: function(personIDs) {
	    var gridComponent = Ext.create("com.trackplus.admin.user.FiltersInUserMenu",
	        {personIDs:personIDs});
	    var grid = gridComponent.getGrid();
	    return [grid];
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
	 * Specify the columnModel implementing the getColumnModel() instead of columnModel config parameter
	 * because "this" is not available in config
	 */
	getColumnModel:function() {
		var userLevelLabels = [];
		if (this.getUserLevels()) {
			Ext.Array.forEach(this.getUserLevels(), function(userLevel) {
				var label = userLevel["label"];
				userLevelLabels.push(label);
				}, this);
		}
		var columnModel = [{text: getText("admin.user.profile.lbl.userName"),
				flex:2, dataIndex: "userName", sortable:true, renderer:this.renderer,
				filter: {
		            type: "string"
		        }
			}, {
				text: getText("common.lbl.name"),
				flex:2, dataIndex: "name", sortable:true,
				renderer:this.renderer,
				filter: {
		            type: "string"
		        }
			}, {
				text: getText("admin.user.profile.lbl.userEmail"),
				flex:2, dataIndex: "userEmail", sortable:true,
				renderer:this.renderer,
				filter: {
		            type: "string"
		        }
			}, {
				text: getText("admin.user.manage.lbl.userLevel"),
				flex:1, dataIndex: "userLevelLabel", sortable:true,
				renderer:this.renderer,
				filter: {
					type: "list",
					options: userLevelLabels
				}
			}, {
				text: getText("admin.user.profile.lbl.department"),
				flex:2, dataIndex: "department", sortable:true,
				renderer:this.renderer,
				filter: {
		            type: "string"
		        }
			}, {
				text: getText("admin.user.manage.lbl.activ"),
				flex:1, dataIndex: "activeLabel", sortable:true, hidden: true,
				renderer:this.renderer,
				filter: {
					type: "list",
					options: [getText("common.boolean.Y"),
								getText("common.boolean.N")]
				}
			}, {
				text: getText("admin.user.profile.lbl.locale"),
				flex:1, dataIndex: "locale", sortable:true, hidden: true,
				renderer:this.renderer,
				filter: {
		            type: "string"
		        }
			}, {
				text: getText("admin.user.profile.lbl.phone"),
				flex:1, dataIndex: "phone", sortable:true, hidden: true,
				renderer:this.renderer,
				filter: {
		            type: "string"
		        }
			}, {
				text: getText("admin.user.profile.lbl.employeeId"),
				flex:1, dataIndex: "employeeId", sortable:true, hidden: true,
				renderer:this.renderer,
				filter: {
		            type: "string"
		        }
			}, {
				text: getText("admin.user.profile.lbl.lastLogin"),
				flex:1, dataIndex: "lastLogin", sortable:true, hidden: true,
				renderer:this.dateRenderer,
				filter: {
		            type: "date"
		        }
			}, {
	            text: getText("admin.user.profile.lbl.substitutePerson"),
	            flex:2, dataIndex: "substituteName", sortable:true, hidden: true,
	            renderer:this.renderer,
				filter: {
		            type: "string"
		        }
	          }];
		if (this.getIsUser() && this.getFeatureList()) {
			Ext.Array.forEach(this.getFeatureList(), function(feature) {
				var featureID = feature["featureID"];
				var featureName = feature["featureName"];
				columnModel.push({
		          	  text: featureName,
		        	  xtype: "checkcolumn",
		        	  flex:1,
		        	  dataIndex: featureID,
		        	  sortable:true,
		        	  hidden: false,
		        	  listeners: {"checkchange": {fn: this.onCheckChange, scope:this, feature:feature},
		  		  				"beforecheckchange": {fn: this.onBeforeCheckChange, scope:this, feature:feature}}
				});

				}, this);
		}
		return columnModel;
	},

	onCheckChange: function(checkBox, rowIndex, checked, eOpts) {
		var record = this.grid.getStore().getAt(rowIndex);
		var feature = eOpts.feature;
		var featureID = feature.featureID;
		if (record && feature) {
			var params = {personID:record.data["id"], featureID:featureID, featureValue:checked};
			Ext.Ajax.request({
				url: this.getBaseAction() + "!changeFeature.action",
				params: params,
				scope: this,
				success: function(response) {
					var result = Ext.decode(response.responseText);
					if (result.success) {
						feature["activeWithFeature"] = result["activeWithFeature"];
					} else {
						com.trackplus.util.showError(result);
					}
				},
				failure: function(response) {
					Ext.MessageBox.alert(this.failureTitle, response.responseText);
				}
			});
		}
	},

	onBeforeCheckChange: function(checkBox, rowIndex, checked, eOpts) {
		var feature = eOpts.feature;
		var record = this.grid.getStore().getAt(rowIndex);
		var featureName = feature["featureName"];
		var maxWithFeature = feature["maxWithFeature"];
		var activeWithFeature = feature["activeWithFeature"];
		if (checked===true && activeWithFeature >= maxWithFeature && record.data["active"]) {
			Ext.MessageBox.alert(this.failureTitle, getText("admin.user.manage.err.featureExceeded", maxWithFeature, activeWithFeature, featureName));
			return false;
		}
	},

	dateRenderer: function(value, metadata, record) {
		if (!record.data["active"]) {
			metadata.style = 'color:#909090';
		}
		return Ext.util.Format.date(value, com.trackplus.TrackplusConfig.DateTimeFormat);
	},

	/**
	 * Render the inherited rows as grey
	 */
	renderer: function(value, metadata, record) {
		if (!record.data["active"]) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	getGridSelectionModel: function() {
		return Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"});
	},

	/**
	 * Enable change only if not inherited
	 */
	onGridSelectionChange: function(view, arrSelections) {
		if (CWHF.isNull(arrSelections) || arrSelections.length===0){
			this.actionDelete.setDisabled(true);
			this.actionEdit.setDisabled(true);
			this.actionActivate.setDisabled(true);
			this.actionDeactivate.setDisabled(true);
			this.actionUserLevel.setDisabled(true);
			this.actionRoleAssignments.setDisabled(true);
	        this.actionFilterAssignments.setDisabled(true);
			this.actionCokpitAssignment.setDisabled(true);
		} else {
			if (arrSelections.length===1) {
				this.actionEdit.setDisabled(false);
				this.actionRoleAssignments.setDisabled(false);
			} else {
				this.actionEdit.setDisabled(true);
				this.actionRoleAssignments.setDisabled(true);
	        }
	        this.actionDelete.setDisabled(false);
			this.actionActivate.setDisabled(false);
			this.actionDeactivate.setDisabled(false);
			this.actionUserLevel.setDisabled(false);
	        this.actionFilterAssignments.setDisabled(false);
			this.actionCokpitAssignment.setDisabled(false);
		}
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getGridContextMenuActions: function(selectedRecords, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			actions.push(this.actionEdit);
		}
		actions.push(this.actionDelete);
		actions.push(this.actionActivate);
		actions.push(this.actionDeactivate);
		actions.push(this.actionUserLevel);
	    actions.push(this.actionFilterAssignments);
		if (selectionIsSimple) {
			actions.push(this.actionRoleAssignments);
		}
		return actions;
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
		return {isUser:this.getIsUser(), context:this.getContext(recordData, operation),
			personId: recordData['id']};
	},

	getContext: function(recordData, operation) {
		var objectID = recordData['id'];
		if (CWHF.isNull(objectID)) {
			return this.profile.CONTEXT.USERADMINADD;
		} else {
			return this.profile.CONTEXT.USERADMINEDIT
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
		return this.profile.getEditUrl(context);
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
		this.profile.context = context;
		this.profile.personId = recordData['id'];
		return this.profile.createMainForm({
			/*style:{
				borderBottom:'1px solid #D0D0D0'
			}*/
		});
	},

	/**
	 * Extra processing that should be done in the edit panel after the data is loaded from the server
	 * like load combo data, show/hide contols
	 */
	afterLoadForm: function(data, panel) {
		this.profile.postDataLoadCombos(data, panel);
	},

	getLoadStoreUrl:function() {
		var param = "?isUser=" + this.getIsUser();
		return this.baseAction + ".action" + param;
	}
});
