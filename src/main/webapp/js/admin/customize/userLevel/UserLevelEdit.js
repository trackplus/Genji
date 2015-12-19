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

Ext.define("com.trackplus.admin.customize.userLevel.UserLevelEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "userLevelEdit",
    controller: "userLevelEdit",
    
    width: 400,
    height: 200,
    
    loadUrl: "userLevel!edit.action",
    
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.title});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    /**
     * Get the panel items
     * recordData: the record data (for the record to be edited or added to)
     * isLeaf: whether add a leaf or a folder
     * add: whether it is add or edit
     * fromTree: operations started from tree or from grid
     * operation:  the name of the operation
     */
    getFormFields : function() {
    	return [CWHF.createTextField("common.lbl.name",
				"name", {labelWidth:100}),
			CWHF.createTextAreaField('common.lbl.description',
				"description", {labelWidth:100})]
    }
});
