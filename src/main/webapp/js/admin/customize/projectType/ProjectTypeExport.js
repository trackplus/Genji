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

Ext.define("com.trackplus.admin.customize.projectType.ProjectTypeExport", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "projectTypeExport",
    controller: "projectTypeExport",
    
	width: 500,
    height: 200,
    node: null,
   
    loadHandler:function() {
		//nothing to load	
	},
    
	/**
	 * Initialization method
	 */
	initBase : function() {
		var entityContext = this.getEntityContext();
		this.node = entityContext.node;
		this.initActions();
	},
	
    initActions: function() {
    	this.actionExport = CWHF.createAction(this.getExportButtonKey(), this.getExportIconCls(),
    			"onExport", {tooltip:this.title});
		this.actionDone = CWHF.createAction(this.getDoneButtonKey(), this.getCancelIconCls(), "onDone");
    	this.actions = [this.actionExport, this.actionDone];
    },
    
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	return [CWHF.createLabelComponent("common.lbl.projectType", null,{labelWidth:200,value:this.node.get("text")}),
				CWHF.createCheckboxWithHelp("admin.customize.projectType.lbl.includeGlobal", "includeGlobal", {itemId:"includeGlobal", labelWidth:200,width:250})];
    },
    
    getWrappedControl: function() {
		return CWHF.getWrappedControl.apply(this.formPanel, arguments);
	}
});
