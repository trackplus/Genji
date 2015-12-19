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

Ext.define("com.trackplus.admin.project.ReleaseEdit", {
	extend: "com.trackplus.admin.WindowBase",
	xtype: "releaseEdit",
    controller: "releaseEdit",
    
  
    loadUrl: "release!edit.action",
    
    width:500,
	height:400,
    labelWidth: 120,
   
    initActions: function() {
    	this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave", {tooltip:this.title});
    	this.actionCancel = CWHF.createAction(this.getCancelButtonKey(), this.getCancelIconCls(), "onCancel");
    	this.actions = [this.actionSave, this.actionCancel];
    },
    
    /**
     * Get the panel items
     */
    getFormFields: function() {
    	return [CWHF.createTextField("common.lbl.name", "releaseDetailTO.label", {allowBlank:false, labelWidth:this.labelWidth, maxLength:255}),
		CWHF.createCombo("admin.customize.localeEditor.type.releaseStatus",
					"releaseDetailTO.releaseStatusID", {itemId:"releaseStatusID", labelWidth:this.labelWidth}),
		CWHF.createDateField("admin.project.release.lbl.dueDate", "releaseDetailTO.dueDate", {labelWidth:this.labelWidth}),
		CWHF.createCheckboxWithHelp("admin.project.release.lbl.defaultReleaseNoticed", "releaseDetailTO.defaultNoticed", {labelWidth:this.labelWidth, width:this.labelWidth+50}),
		CWHF.createCheckboxWithHelp("admin.project.release.lbl.defaultReleaseScheduled", "releaseDetailTO.defaultScheduled",{labelWidth:this.labelWidth, width:this.labelWidth+50}),
		CWHF.createTextAreaField("common.lbl.description", "releaseDetailTO.description", {maxLength:255, labelWidth:this.labelWidth})];
    },
    
	postDataProcess: function(data, panel) {
		var statusCombo = panel.getComponent("releaseStatusID");
		if (statusCombo) {
			//no statusCombo for phase
			statusCombo.store.loadData(data["statusList"]);
			statusCombo.setValue(data["releaseDetailTO.statusID"]);
		}
	}
});
