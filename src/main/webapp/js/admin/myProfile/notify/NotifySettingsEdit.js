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


Ext.define("com.trackplus.admin.NotifySettingsEdit", {
	extend:"com.trackplus.admin.WindowBase",
	xtype: "notifySettingsEdit",
    controller: "notifySettingsEdit",
	
    width: 500,
    height: 210,
    
    loadUrl: "notifySettings!edit.action",
    
    addNotifySetting: false,
    record: null,
    inherited: false,
    operation: null,
    defaultSettings: false,
    
	//the exclusiveProject set only for automail in project settings
	exclusiveProjectID: null,

    
    initBase: function() {
    	var entityContext = this.getEntityContext();
		this.record = entityContext.record;
		if (this.record) {
			this.inherited = this.record.get("inherited");
		}
    	this.operation = entityContext.operation;
    	this.addNotifySetting = (this.operation==="add");
    	var parentViewConfig = entityContext.config;
    	if (parentViewConfig) {
    		this.defaultSettings = parentViewConfig.defaultSettings;
    		this.exclusiveProjectID = parentViewConfig.exclusiveProjectID;
    	}
    	this.initActions();
    },
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    
	getLoadUrlParams: function() {
		var params = {};
		if (this.record) {
			params["notifySettingsID"] = this.record.get("id");
		}
		params["defaultSettings"] = this.defaultSettings;
		if (this.exclusiveProjectID) {
			params["exclusiveProjectID"] = this.exclusiveProjectID;
		}
		return params;
	},
	
	getSubmitUrlParams: function() {
		var params = {};
		if (this.record) {
			params["notifySettingsID"] = this.record.get("id");
		}
		params["defaultSettings"] = this.defaultSettings;
		if (this.exclusiveProjectID) {
			params["exclusiveProjectID"] = this.exclusiveProjectID;
		}
		return params;
	},
	
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	var labelWidth = 150;
		var width = 400;
	    return [CWHF.createSingleTreePicker("admin.project.lbl.project", "project", [], null,
			        	{itemId:"project",
						reference: "project",
						disabled: this.inherited,
			    		allowBlank:false,
			            labelWidth: labelWidth,
			            width:width,
			            margin:'0 0 5 0'
			        	},
			        	{select:"onProjectSelect"}),
				CWHF.createCombo("admin.customize.automail.trigger.lblAlone", "trigger", 
						{itemId:"trigger",
						reference: "trigger",
						width:width,
						labelWidth:labelWidth,
						allowBlank:false}),
				CWHF.createSingleTreePicker("admin.customize.automail.filter.lblAlone", "filter", [], null,
				        {itemId:"filter",
						reference: "filter",
				    	allowBlank:false,
				        labelWidth:labelWidth,
				        width:width,
				        margin:'0 0 5 0'})];
    },
    
    postDataProcess: function(data, panel) {
    	var filterPicker = panel.getComponent("filter");
	    filterPicker.updateMyOptions(data["filterTree"]);
	    filterPicker.setValue(data["filter"]);
	    var projectPicker = panel.getComponent("project");
	    projectPicker.updateMyOptions(data["projectTree"]);
	    projectPicker.setValue(data["project"]);
		var triggersList = panel.getComponent("trigger");
		triggersList.store.loadData(data["triggersList"]);
		triggersList.setValue(data["trigger"]);
	}
  
});
