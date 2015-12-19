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


Ext.define("com.trackplus.admin.user.FiltersInUserMenuEdit", {
	extend:"com.trackplus.admin.WindowBase",
	xtype: "filtersInUserMenuEdit",
    controller: "filtersInUserMenuEdit",
	
    width: 400,
    height: 120,
    
    loadUrl: "filtersInMenu!edit.action",
    
    addFiltersInMenu: false,
    record: null,
    operation: null,
    personIDs: null,
    
    initBase: function() {
    	var entityContext = this.getEntityContext();
		this.record = entityContext.record;
		this.operation = entityContext.operation;
    	this.addFiltersInMenu = (this.operation==="add");
    	var parentViewConfig = entityContext.config;
    	if (parentViewConfig) {
    		this.personIDs = parentViewConfig.personIDs;
    	}
    	this.initActions();
    },
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionDone = CWHF.createAction(this.getDoneButtonKey(), this.getCancelIconCls(), "onDone");
    	this.actions = [this.actionSave, this.actionDone];
    },
    
    
	getLoadUrlParams: function() {
		var params = {};
		if (this.personIDs) {
			params["personIDs"] = this.personIDs;
		}
		return params;
	},
	
	getSubmitUrlParams: function() {
		var params = {};
		if (this.personIDs) {
			params["personIDs"] = this.personIDs;
		}
		return params;
	},
	
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	return [CWHF.createMultipleTreePicker(null,
				"filterIDs", [], null,
				{itemId:'filterIDs', allowBlank:false,
                margin:'0 0 5 0',
                useRemoveBtn:false,
					allowNull:true,
                useTooltip:false,
                fieldLabel:getText("common.lbl.filter")
			})];
    },
    
    postDataProcess: function(data, panel) {
    	var filterControl = panel.getComponent("filterIDs");
	    filterControl.updateMyOptions(data["filterTree"]);
	}
  
});
