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

Ext.define("com.trackplus.admin.customize.list.ListImport", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "listImport",
    controller: "listImport",
    
    width: 600,
    height: 180,
    //fieldLabelWidth: 180,
    
    loadUrl: "listOptions!importList.action",
	
    initTitle: function() {
    	return getText(this.getUploadTitleKey(), getText("admin.customize.list.lbl.icon"));
    },
    
    initActions: function() {
    	this.actionUpload = CWHF.createAction(this.getUploadButtonKey(), this.getUploadIconCls(),
			"onUpload", {tooltip:getText(this.getUploadTitleKey(), getText("admin.customize.list.lbl.iconOp"))});
    	this.actionDone = CWHF.createAction(this.getDoneButtonKey(), this.getCancelIconCls(), "onDone");
    	this.actions = [this.actionUpload, this.actionDone];
    },
    
    getFormFields: function() {
    	return [CWHF.createFileField("admin.customize.list.import.lbl.uploadFile", "uploadFile", {
                    allowBlank: false,
                    itemId: "uploadFile"
                }),
                CWHF.createCheckbox("common.lbl.overwriteExisting", "overwriteExisting"),
                CWHF.createCheckbox("common.lbl.clearChildren", "clearChildren")];
    }
});
