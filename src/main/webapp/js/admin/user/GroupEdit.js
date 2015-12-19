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

Ext.define("com.trackplus.admin.user.GroupEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "groupEdit",
    controller: "groupEdit",
    //scope for the static filter operations 
   
	width: 500,
    height: 260,
    labelWidth:160,
    
    loadUrl: "group!edit.action",
    
    
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
    	return [CWHF.createTextField('common.lbl.name','name',
				{anchor:'95%', allowBlank:false, labelWidth:this.labelWidth, maxLength:60, enforceMaxLength:true}),
				CWHF.createCheckbox("admin.user.group.lbl.groupFlagJoinNewUserToThisGroup", "joinNewUserToThisGroup", {labelWidth:this.labelWidth}),
				{xtype: "fieldset",
				title: getText("admin.user.group.lbl.groupFlags"),
					collapsible: false,
					layout: "anchor",
					anchor:'95%',
					items: [CWHF.createCheckbox("admin.user.group.lbl.groupFlagOriginator", "originator", {labelWidth:this.labelWidth}),
							CWHF.createCheckbox("admin.user.group.lbl.groupFlagManager", "manager", {labelWidth:this.labelWidth}),
							CWHF.createCheckbox("admin.user.group.lbl.groupFlagResponsible", "responsible", {labelWidth:this.labelWidth})]}];
    }
});
