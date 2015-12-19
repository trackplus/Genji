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


Ext.define("com.trackplus.admin.user.UserRolesInProjectEditController",{
	extend: "Ext.app.ViewController",
	alias: "controller.userRolesInProjectEdit",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	onSave: function(button, event) {
		this.submitHandler(button, "rolesInProjects!save.action");
	},
	
	onDone: function(button, event) {
		this.cancelHandler(button);
	},
	
	onProjectSelect: function(projectPicker, selectedProject, options) {
		var roleCombo = this.lookupReference("roleID");
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
