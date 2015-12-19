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
	xtype: "person",
    controller: "person",
	config: {
		//context: null,
		isUser: null,
		userLevels:null,
		featureList: null
	},
	storeUrl: "person.action",
	enableColumnHide: true,
	enableColumnMove: true,
	allowMultipleSelections:true,
	//profile:null,
	actionActivate:null,
	actionDeactivate:null,
	actionUserLevel:null,
	actionRoleAssignments: null,
	actionFilterAssignments: null,
	actionCokpitAssignment:null,
	actionSyncLdap: null,

	initComponent : function() {
		this.fields = this.getGridFields();
		this.columns = this.getColumnModel();
		//var param = "?isUser=" + this.getIsUser();
		//this.storeUrl = "person.action" + param;
		this.callParent();
		//this.profile=Ext.create("com.trackplus.admin.user.Profile",{context:this.getContext(), isUser: this.getIsUser()});
		this.initPerson();
	},

	/**
	 * Get extra parameters for grid load
	 */
	getStoreExtraParams:function() {
		return {isUser:this.getIsUser()};
	},
	
	getEntityLabel: function() {
		return getText("admin.user.lbl.user");
	},

	initPerson:function(){
		Ext.Ajax.request({
			url: "person!getLdapIsOn.action",
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
		this.actionAdd = CWHF.createAction(this.getAddButtonKey(), this.getAddIconCls(), "onAdd", {tooltip:this.getActionTooltip(this.getAddTitleKey())});
		this.actionEdit = CWHF.createAction(this.getEditButtonKey(), this.getEditIconCls(), "onEdit", {tooltip:this.getActionTooltip(this.getEditTitleKey()), disabled:true});
		this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(), "onDelete", {tooltip:this.getActionTooltip(this.getDeleteTitleKey()), disabled:true});
		this.actionActivate = CWHF.createAction("common.btn.activate", this.getActivateIconCls(), "onActivate", {tooltip:this.getActionTooltip("common.lbl.activate"), disabled:true});
		this.actionDeactivate = CWHF.createAction("common.btn.deactivate", this.getDeactivateIconCls(), "onDeactivate", {tooltip:this.getActionTooltip("common.lbl.deactivate"), disabled:true});
		this.actionUserLevel = CWHF.createAction("common.btn.userLevel", "personSystemAdmin", "onUserLevel", {tooltip:getText("common.btn.userLevel"), disabled:true});
		this.actionRoleAssignments = CWHF.createAction("admin.user.manage.lbl.roleAssignments",
				this.getRoleAssignCls(), "onShowAssignments", {tooltip:getText("admin.user.manage.lbl.roleAssignments"), disabled:true});
	    this.actionFilterAssignments = CWHF.createAction("admin.user.manage.lbl.filterAssignments",
	        this.getFilterAssignCls(), "onFilterAssignments", {tooltip:getText("admin.user.manage.lbl.filterAssignments"), disabled:true});
		this.actionCokpitAssignment=CWHF.createAction("admin.user.manage.lbl.cokpitAssignment", this.getCokpitAssignmentIconCls(), "onCokpitAssignment",
				{tooltip:getText("admin.user.manage.lbl.cokpitAssignment"), disabled:true});
		this.actionSyncLdap = CWHF.createAction("common.btn.syncLdap", this.getAddIconCls(), "onSyncLdap", {tooltip:getText("common.btn.syncLdap.tt")});
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
			markDirty: false,
			stripeRows: true,
			listeners: {
				//although these event listeners could be attached to the gridPanel directly but then the corresponding listeners of the viewController are not called
				itemdblclick: "onItemDblClick",
				itemcontextmenu: "onItemContextMenu",
				selectionchange: "onSelectionChange"
			}
		};
	},


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
		        	  listeners: {"checkchange": {fn: "onCheckChange", feature:feature},
		  		  				"beforecheckchange": {fn: "onBeforeCheckChange", feature:feature}}
				});

				}, this);
		}
		return columnModel;
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
	}
});
