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


Ext.define("com.trackplus.admin.user.UserRolesInProjectEdit", {
	extend:"com.trackplus.admin.WindowBase",
	xtype: "userRolesInProjectEdit",
    controller: "userRolesInProjectEdit",
	
    width: 400,
    height: 175,
    labelWidth: 100,
    
    loadUrl: "rolesInProjects!edit.action",
    
    addUserRole: false,
    record: null,
    projectID: null,
    roleID: null,
    direct:false,
    operation: null,
    personID: null,
    //group: false,
    
    initBase: function() {
    	var entityContext = this.getEntityContext();
		this.record = entityContext.record;
		if (this.record) {
			this.projectID = this.record.get("projectID");
			this.roleID = this.record.get("roleID");
			this.direct = this.record.get("direct");
		}
		this.operation = entityContext.operation;
    	this.addUserRole = (this.operation==="add");
    	var parentViewConfig = entityContext.config;
    	if (parentViewConfig) {
    		this.personID = parentViewConfig.personID;
    		//this.group = parentViewConfig.group;
    	}
    	this.initActions();
    },
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionDone = CWHF.createAction(this.getDoneButtonKey(), this.getCancelIconCls(), "onDone");
    	this.actions = [this.actionSave, this.actionDone];
    },
    
    
	getLoadUrlParams: function() {
		var params = {add: this.addUserRole};
		if (this.personID) {
			params["personID"] = this.personID;
		}
		if (this.projectID) {
			params["projectID"] = this.projectID;
		}
		if (this.roleID) {
			params["roleID"] = this.roleID;
		}
		return params;
	},
	
	getSubmitUrlParams: function() {
		var params = {add: this.addUserRole};
		if (this.personID) {
			params["personID"] = this.personID;
		}
		if (this.projectID) {
			params["projectID"] = this.projectID;
		}
		if (this.roleID) {
			params["roleID"] = this.roleID;
		}
		return params;
	},
	
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	var disabled = false;
    	if (!this.addUserRole && !this.direct) {
    		disabled = true;
    	}
    	return [CWHF.createSingleTreePicker("admin.project.lbl.project",
				"projectID", [], null,
				{itemId:"projectID",
				allowBlank:false,
				labelWidth:this.labelWidth,
				disabled:disabled,
                margin:'0 0 5 0'},
                {select:"onProjectSelect"}),
            CWHF.createCombo("admin.customize.role.lbl.role", "roleID",
        				{itemId:"roleID", reference: "roleID", allowBlank:false, labelWidth:this.labelWidth, disabled:disabled}),
			CWHF.createHiddenField("projectIDOld", {itemId:"projectIDOld"}),
			CWHF.createHiddenField("roleIDOld", {itemId:"roleIDOld"})];
    },
    
    postDataProcess: function(data, panel) {
    	var projectControl = panel.getComponent("projectID");
	    projectControl.updateMyOptions(data["projects"]);
	    projectControl.setValue(data["projectID"]);
		var roleControl = panel.getComponent("roleID");
		if (roleControl) {
			roleControl.store.loadData(data["roles"]);
		}
		roleControl.setValue(data["roleID"]);
		var projectIDOld = panel.getComponent("projectIDOld");
		projectIDOld.setValue(data["projectID"]);
		var roleIDOld = panel.getComponent("roleIDOld");
		roleIDOld.setValue(data["roleID"]);
		
		
	}
  
});
