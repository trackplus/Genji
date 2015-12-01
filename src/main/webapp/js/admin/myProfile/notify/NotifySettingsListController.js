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

Ext.define('com.trackplus.admin.NotifySettingsListController', {
	extend: "Ext.app.ViewController",
	alias: "controller.notifySettingsList",
	mixins: {
		baseController: "com.trackplus.admin.GridBaseController"
	},
	baseAction: "notifySettings",
	entityID:'notifySettingsID',
	//editWidth:500,
	//editHeight:210,
	
	/**
	 * Parameters for adding a new entity
	 */
	getAddParams:function() {
		return {defaultSettings: this.getView().getDefaultSettings(), exclusiveProjectID: this.getView().getExclusiveProjectID()};
	},

	/**
	 * Parameters for editing an existing entity
	 */
	getEditParams:function(recordData) {
		var params = {defaultSettings: this.getView().getDefaultSettings(), exclusiveProjectID: this.getView().getExclusiveProjectID()};
		params[''+this.entityID]=recordData.id;
		return params;
	},

	/**
	 * Parameters for deleting an existing entity
	 */
	getDeleteParams:function(selectedRecord) {
		var params = new Object();
		params[""+this.entityID]=selectedRecord.data.id;
		params["defaultSettings"]=this.getView().getDefaultSettings();
		return params;
	},

	/**
	 * Handler for overwrite
	 */
	onOverwrite: function(){
		var recordData=this.getView().getSingleSelectedRecordData();
		if (recordData) {
			this.openEditEntity(this.getView().getActionTooltip(this.getView().getOverwriteTitleKey()), recordData, "overwrite");
		}
	},

	/**
	 * Parameters for saving an entity (extra parameters additionally to submit parameters)
	 */
	getSaveParams: function(recordData, type){
		var result=null;
		if (type==="add"){
			result = this.getAddParams();
		} else {
			if (type==="edit") {
				result = this.getEditParams(recordData);
			} else {
				if (type==="overwrite") {
					result = new Object();
					//the default (original) notifySettingsID is not needed
					//but the project should be explicitly set because it is disabled (not automatically submitted)
					result["project"]=recordData.project;
				}
			}
		}
		if (CWHF.isNull(result)) {
			//should not be null because dynamic parameters might be added right before submit
			//(in either submitHandler()  or preSubmitProcess() implementations)
			result = new Object();
		}
		return result;
	},

	/**
	 * Function to be called before submit to add dynamic parameters
	 * to existing submitUrlParams based on the panel's content
	 */
	preSubmitProcess: function(submitUrlParams, panel, submitAction) {
	 	//add parameters to submitUrlParams based on panel
		if (submitAction==="overwrite") {
			//if overwrite, the project combo is disabled and consequently not submitted
			//manually add project parameter
			var projectsList = panel.getComponent("projectsList");
			if (projectsList) {
				submitUrlParams["project"]=projectsList.getValue();
			}
		}
		return submitUrlParams;
	},

	/**
	 * Render the inherited rows as grey
	 */
	inheritedRenderer: function(value, metadata, record) {
		if (record.data.inherited) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	/**
	 * Enable change only if not inherited
	 */
	onSelectionChange: function(view, arrSelections) {
		if (CWHF.isNull(arrSelections) || arrSelections.length===0){
			this.getView().actionDelete.setDisabled(true);
			this.getView().actionEdit.setDisabled(true);
			if (this.getView().actionOverwrite) {
				this.getView().actionOverwrite.setDisabled(true);
			}
		} else {
			var selectedRecord = arrSelections[0];
			var inherited = selectedRecord.data["inherited"];
			this.getView().actionDelete.setDisabled(inherited);
			this.getView().actionEdit.setDisabled(inherited);
			if (this.getView().actionOverwrite) {
				this.getView().actionOverwrite.setDisabled(!inherited);
			}
		}
	},

	

	/**
	 *  Handler for double click
	 */
	onItemDblClick:function(view, record) {
		var inherited = record.data["inherited"];
		if (inherited) {
			this.onOverwrite.call(this);
		} else {
			this.onEdit.call(this);
		}
	},

	/**
	 * Populate the combos once data is loaded
	 */
	afterLoadForm: function(data, panel) {
	    //first set the filter, because by onProjectSelect the filterPicker's value will be sent
	    var filterPicker = panel.getComponent("filter");
	    filterPicker.updateMyOptions(data["filterTree"]);
	    filterPicker.setValue(data["filter"]);
	    var projectPicker = panel.getComponent("project");
	    projectPicker.updateMyOptions(data["projectTree"]);
	    projectPicker.setValue(data["project"]);
		var triggersList = panel.getComponent("trigger");
		triggersList.store.loadData(data["triggersList"]);
		triggersList.setValue(data["trigger"]);
	},

	/**
	 * Gets the form items
	 */
	getEditPanelItems: function(recordData, type) {
		var inherited = (type==="overwrite");
		var notifySettingsID = null;
		if (recordData) {
			notifySettingsID = recordData['id'];
		}
		var labelWidth = 150;
		var width = 400;
	    var filterPicker = CWHF.createSingleTreePicker("admin.customize.automail.filter.lblAlone",
	        "filter", [], null,
	        {itemId:"filter",
	    	 allowBlank:false,
	         labelWidth:labelWidth,
	         width:width,
	         margin:'0 0 5 0'
	        })
	    var projectPicker = CWHF.createSingleTreePicker("admin.project.lbl.project",
	        "project", [], null,
	        {	itemId:'project',
	    		allowBlank:false,
	            labelWidth: labelWidth,
	            width:width,
	            margin:'0 0 5 0'
	        }, {select:{fn: this.onProjectSelect, scope:this,
	            filterPicker: filterPicker}});
		return [projectPicker,
				CWHF.createCombo("admin.customize.automail.trigger.lblAlone",
						"trigger", {itemId:"trigger", width:width, labelWidth:labelWidth, allowBlank:false}),
				filterPicker];
	},

	/**
	 * Change event handler
	 * @param projectPicker
	 * @param selectedProjects
	 * @param options
	 */
	onProjectSelect: function(projectPicker, selectedProject, options) {
	    var filterPicker = options["filterPicker"];
	    Ext.Ajax.request({
	        url: this.baseAction + "!projectChange.action",
	        params:{defaultSettings: this.getView().getDefaultSettings(), project: projectPicker.getValue(), filter: filterPicker.getValue()},
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
	}
});
