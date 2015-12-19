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

Ext.define("com.trackplus.admin.user.DepartmentEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "departmentEdit",
    controller: "departmentEdit",
    //scope for the static filter operations 
   
	width: 400,
    height: 150,
    
    loadUrl: "department!edit.action",
    
    
    initActions: function() {
		this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey())});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    /**
     * Get the panel items
     * Empty at the beginning: add the fields dynamically in postDataProcess 
     */
    getFormFields: function() {
    	return [CWHF.createTextField("common.lbl.name","name",
				{anchor:'100%', allowBlank:false, labelWidth:80, maxLength:255, enforceMaxLength:true})];
    }
});
