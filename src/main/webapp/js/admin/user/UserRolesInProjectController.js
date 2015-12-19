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


Ext.define('com.trackplus.admin.user.UserRolesInProjectController',{
	extend: "Ext.app.ViewController",
	alias: "controller.userRolesInProject",
	mixins: {
		baseController: "com.trackplus.admin.GridBaseController"
	},
	
	confirmDeleteEntity:false,
	
	entityDialog: "com.trackplus.admin.user.UserRolesInProjectEdit",
	
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
	 * Render the inherited rows as grey
	 */
	projectRenderer: function(value, metadata, record) {
		if (!record.data["first"]) {
			return "";
		}
		return value;
	},
	
	roleRenderer:function(value, metadata, record) {
		if (!record.data["direct"]) {
			metadata.style = 'color:#909090';
		}
		return value;
	},
	
	/**
	 * Get the ID based from the recordData
	 *  No objectID primary key in tacl table
	 */
	getRecordID: function(recordData) {
		if (recordData) {
			return recordData["projectID"] + "|" + recordData["roleID"];
		}
		return null;
	},

	getDeleteUrl: function() {
		return "rolesInProjects!delete.action";
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
		var selectionParam = this.getSelectedIDs(selectedRecords, extraConfig);
		params["unassign"]= selectionParam;
		params["personID"] = this.getView().getPersonID();
		return params;
	}

	/*createEditForm:function(entityJS,operation){
		var disabled = false;
		if (operation!=="add" && !entityJS["direct"]) {
			disabled = true;
		}
		var roleCombo = CWHF.createCombo("admin.customize.role.lbl.role", "roleID",
				{itemId:"roleID", allowBlank:false, labelWidth:this.labelWidth, disabled:disabled});
		return Ext.create('Ext.form.FormPanel',{
			url:this.getBaseAction() + '!save.action',
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
					{itemId:'projectID',
					allowBlank:false,
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
	    projectControl.updateMyOptions(data["projects"]);
	    projectControl.setValue(data["projectID"]);

		roleControl = panel.getComponent("roleID");
		if (roleControl) {
			roleControl.store.loadData(data["roles"]);
		}
		roleControl.setValue(data["roleID"]);
	},*/

	/**
	 * Change event handler
	 * @param projectPicker
	 * @param selectedProjects
	 * @param options
	 */
	/*onProjectSelect: function(projectPicker, selectedProject, options) {
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
	}*/
});
