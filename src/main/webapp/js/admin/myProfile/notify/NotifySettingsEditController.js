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


Ext.define("com.trackplus.admin.NotifySettingsEditController",{
	extend: "Ext.app.ViewController",
	alias: "controller.notifySettingsEdit",
	mixins: {
		baseController: "com.trackplus.admin.WindowBaseController"
	},
	
	onSave: function(button, event) {
		this.submitHandler(button, "notifySettings!save.action");
	},
	
	onCancel: function(button, event) {
		this.cancelHandler(button);
	},
	
	/**
	 * Change event handler
	 * @param projectPicker
	 * @param selectedProjects
	 * @param options
	 */
	onProjectSelect: function(projectPicker, selectedProject, options) {
	    var filterPicker = this.lookupReference("filter");
	    Ext.Ajax.request({
	        url: "notifySettings!projectChange.action",
	        params:{defaultSettings: this.getView().defaultSettings, project: projectPicker.getValue(), filter: filterPicker.getValue()},
	        scope:this,
	        success: function(response){
	            var responseJSON = Ext.decode(response.responseText);
	            filterPicker.updateMyOptions(responseJSON["filterTree"]);
	            filterPicker.setValue(responseJSON["filter"]);
	        },
	        failure: function(response){
	            com.trackplus.util.requestFailureHandler(response);
	        }
	    })
	},
	
	/**
	 * Function to be called before submit to add dynamic parameters
	 * to existing submitUrlParams based on the panel's content
	 */
	preSubmitProcess: function(submitUrlParams, panel, submitAction) {
	 	//add parameters to submitUrlParams based on panel
		if (this.getView().operation==="overwrite") {
			//if overwrite, the project combo is disabled and consequently not submitted
			//manually add project parameter
			var projectPicker = this.lookupReference("project");
			if (projectPicker) {
				submitUrlParams["project"]=projectPicker.getValue();
			}
		}
		return submitUrlParams;
	},
	
});
