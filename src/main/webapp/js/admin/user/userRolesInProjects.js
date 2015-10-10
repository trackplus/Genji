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


Ext.define('com.trackplus.admin.user.UserRolesInProject',{
	extend:'com.trackplus.admin.GridConfig',
	config: {
			urlStore:null,
			//person or group ID
			personID: null,
			//whether it is group or person
			group: null
			},
	urlStore:null,
	allowMultipleSelections:true,
	personID: null,
	group: null,
	fields:[{name: 'projectID',type:'int'},
			{name: 'projectLabel',type:'string'},
			{name: 'roleID',type:'int'},
			{name: 'roleLabel', type: 'string'},
			{name: 'direct', type: 'boolean'},
			{name: 'first', type: 'boolean'}
	],
	confirmDeleteEntity:true,
	baseAction: "rolesInProjects",
	labelWidth: 100,
	editWidth:400,
	editHeight:175,

	constructor: function(config) {
		var config = config || {};
		this.initialConfig = config;
		config["features"] = this.getGridFeatures();
		config['region']='center';
		config['style']={
			borderTop:'1px solid #D0D0D0',
			borderBottom:'1px solid #D0D0D0'
		};
		Ext.apply(this, config);
		this.init();
	},

	getEntityLabel:function(){
		return getText("admin.user.group.lbl.roleAssignment");
	},

	getAddIconCls: function() {
		return "add16";
	},

	getDeleteIconCls: function() {
		return "delete16";
	},

	/**
	 * Gets the column model array for the grid.
	 * Alternative to the columnModel config property (if "this" keyword is needed in column model)
	 */
	getColumnModel:function() {
		return [{header: getText('admin.project.lbl.project'), flex:1,
					dataIndex: 'projectLabel', sortable:true, renderer:this.projectRenderer},
				{header:getText('admin.customize.role.lbl.role'),
					flex:1, dataIndex:'roleLabel', sortable:false, renderer:this.roleRenderer}];
	},

	/**
	 * Render the inherited rows as grey
	 */
	projectRenderer: function(value, metadata, record) {
		if (!record.data["first"]) {
			return "";
		}
		return value;
	},

	/**
	 * Render the inherited rows as grey
	 */
	roleRenderer:function(value, metadata, record) {
		if (!record.data["direct"]) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	getGridSelectionModel: function() {
		return Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"});
	},

	/**
	 * Defined typically if there is no "classic" toolbar available (like grid is in a popup)
	 */
	getDockedItems: function() {
		return [{
			xtype: 'toolbar',
			dock: 'top',
			margin:'4 0 0 0',
			items: [this.actionAdd, this.actionEdit, this.actionDelete]
		}];
	},

	/**
	 * Get extra parameters for grid load
	 */
	getLoadGridParams:function() {
		return {personID:this.personID, group:this.group};
	},

	/**
	 * Parameters for adding a new entity
	 * Specify extra parameters if needed
	 * (like "defaultSettings" for "my" and "default" automail settings)
	 */
	getAddParams: function() {
		return {personID:this.personID, add:true};
	},

	getEditParams: function(recordData) {
		return {personID:this.personID, projectID:recordData["projectID"], roleID:recordData["roleID"]};
	},

	getGridFeatures: function() {
		return [{
			ftype: 'filters',
			encode: false, // json encode the filter query
			local: true,   // defaults to false (remote filtering)
			filters: [{
					type: 'string',
					dataIndex: 'projectLabel'
				}/*,{
					type: 'string',
					dataIndex: 'roleLabel'
				}*/
			]
		}];
	},

	/**
	 * Get the ID based from the recordData
	 *  No objectID primary key in tacl table
	 */
	getRecordID: function(recordData) {
		if (recordData!=null) {
			return recordData["projectID"] + "|" + recordData["roleID"];
		}
		return null;
	},

	/**
	 * Parameters for deleting entity
	 * recordData: the selected entity data
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	getDeleteParams: function(selectedRecords, extraConfig) {
		var params=new Object();
		var selectionParam = this.getSelectionParam(selectedRecords, extraConfig);
		params["unassign"]= selectionParam;
		params["personID"] = this.personID;
		return params;
	},

	createEditForm:function(entityJS,operation){
		var disabled = false;
		if (operation!="add" && !entityJS["direct"]) {
			disabled = true;
		}
		var roleCombo = CWHF.createCombo("admin.customize.role.lbl.role", "roleID",
				{allowBlank:false, labelWidth:this.labelWidth, disabled:disabled});
		return Ext.create('Ext.form.FormPanel',{
			url:this.baseAction + '!save.action',
			autoScroll: true,
			border: false,
			margin: '0 0 0 0',
			bodyStyle:{
				padding:'10px'
			},
			style:{
				borderBottom:'1px solid #D0D0D0'
			},
			defaults : {
				labelStyle:'overflow: hidden;',
				msgTarget : 'side',
				anchor : '100%'
			},
			items: [CWHF.createSingleTreePicker("admin.project.lbl.project",
					"projectID", [], null,
					{allowBlank:false,
					disabled:disabled,
	                margin:'0 0 5 0'},
	                {select:{fn: this.onProjectSelect, scope:this, roleCombo:roleCombo}}),
	            roleCombo,
				CWHF.createHiddenField("projectIDOld", {value: entityJS["projectID"]}),
				CWHF.createHiddenField("roleIDOld", {value: entityJS["roleID"]})]
		});
	},

	afterLoadForm:function(data, panel) {
		projectControl = panel.getComponent("projectID");
	    projectControl.updateData(data["projects"]);
	    projectControl.setValue(data["projectID"]);

		roleControl = panel.getComponent("roleID");
		if (roleControl!=null) {
			roleControl.store.loadData(data["roles"]);
		}
		roleControl.setValue(data["roleID"]);
	},

	/**
	 * Change event handler
	 * @param projectPicker
	 * @param selectedProjects
	 * @param options
	 */
	onProjectSelect: function(projectPicker, selectedProject, options) {
	    var roleCombo = options["roleCombo"];
	    Ext.Ajax.request({
	        url: "rolesInProjects!projectChange.action",
	        params:{projectID: projectPicker.getValue(), roleID: roleCombo.getValue()},
	        scope:this,
	        success: function(response){
	            var responseJSON = Ext.decode(response.responseText);
	            roleCombo.store.loadData(responseJSON["roles"]);
	            roleCombo.setValue(responseJSON["roleID"]);
	        },
	        failure: function(response){
	            com.trackplus.util.requestFailureHandler(response);
	        }
	    })
	}
});
