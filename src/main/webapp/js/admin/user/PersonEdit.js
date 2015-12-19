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


Ext.define("com.trackplus.admin.user.PersonEdit", {
	extend:"com.trackplus.admin.WindowBase",
	xtype: "personEdit",
    controller: "personEdit",
	
    width: 780,
    height: 500,
    labelWidth: 100,
    
    record: null,
    personId: null,
    context: null,
    isUser: false,
    operation: null,
    addUser: false,
    profile: null,
    
    initBase: function() {
    	var entityContext = this.getEntityContext();
		this.record = entityContext.record;
		if (this.record) {
			this.personId = this.record.get("id");
		}
		if (CWHF.isNull(this.personId)) {
			this.context = 2;//profile.CONTEXT.USERADMINADD
		} else {
			this.context = 3;//profile.CONTEXT.USERADMINEDIT
		}
		this.operation = entityContext.operation;
    	this.addUser = (this.operation==="add");
    	var parentViewConfig = entityContext.config;
    	if (parentViewConfig) {
    		this.isUser = parentViewConfig.isUser;
    	}
    	this.profile=Ext.create("com.trackplus.admin.user.Profile", {context:this.context, personId:this.personId, isUser: this.isUser});
    	
    	//this.formPanel = this.profile.createMainForm();
    	
    	this.initActions();
    },
    
    /**
	 * specify only if special loading is needed
	 */
	loadHandler: function() {
		this.add(this.profile.loadProfile());
	},
	
    /*getLoadUrl: function() {
    	this.profile.getEditUrl(this.context);
    },*/
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
	/*getLoadUrlParams: function() {
		params = {context:this.context};
		if (this.personId) {
			params["personId"] = this.personId;
		}
		return params;
	},*/
	
	/*getSubmitUrlParams: function() {
		params = {context:this.context, isUser: this.isUser};
		if (this.personId) {
			params["personId"] = this.personId;
		}
		return params;
	},
	
    postDataProcess: function(data, panel) {
    	this.profile.postDataLoadCombos(data, panel);
	}*/
  
});
