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

Ext.define("com.trackplus.admin.customize.projectType.ProjectTypeImport", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "projectTypeImport",
    controller: "projectTypeImport",
    
    width: 500,
    height: 200,
    
    loadHandler:function() {
		//nothing to load	
	},
	
    initActions: function() {
    	this.actionUpload = CWHF.createAction(this.getImportButtonKey(), this.getImportIconCls(),
			"onImport", {tooltip:this.title});
    	this.actionDone = CWHF.createAction(this.getDoneButtonKey(), this.getCancelIconCls(), "onDone");
    	this.actions = [this.actionUpload, this.actionDone];
    },
    
    getFormFields: function() {
    	return [CWHF.createCheckbox('common.lbl.overwriteExisting', 'overwriteExisting', {labelWidth:180}),
    	        CWHF.createFileField('common.lbl.attachment.file', 'uploadFile', {itemId:"uploadFile", allowBlank:false, labelWidth:180})];
    }
    
});
