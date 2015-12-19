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


Ext.define("com.trackplus.admin.customize.projectType.ProjectType",{
	extend:"com.trackplus.admin.TreeDetail",
	xtype: "projectType",
    controller: "projectType",
	treeWidth: 250,
	treeFields: [{name : 'id', mapping : 'id', type: 'string'},
					{ name : 'text', mapping : 'text', type: 'string'},
					{ name : 'leaf', mapping : 'leaf', type: 'boolean'},
					{ name : 'iconCls', mapping : 'iconCls', type: 'string'},
					{ name : 'icon', mapping : 'icon', type: 'string'},
					{ name : 'configType', mapping : 'configType', type: 'int'},
					{ name : 'projectTypeID', mapping : 'projectTypeID', type: 'int'},
					{ name : 'branchRoot', mapping : 'branchRoot', type: 'string'}],
	treeStoreUrl: "projectType!expand.action",
	baseServerAction: "projectType",
	
	//actions
	actionAdd: null,
	//actionSave: null,
	actionDelete: null,
	actionImport: null,
	actionExport: null,

	
	/**
	 * The localized entity name
	 */
	getEntityLabel: function(extraConfig) {
		return getText("common.lbl.projectType");
	},
	
	initActions: function() {
		var addText = this.getActionTooltip(this.getAddTitleKey());
		this.actionAdd = CWHF.createAction(addText, this.getAddIconCls(), "onAdd", {labelIsLocalized:true});
		/*this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(),
				"onSave", {tooltip:this.getActionTooltip(this.getSaveTitleKey()), disabled:true});*/
		this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(),
	    		"onDelete", {tooltip:this.getActionTooltip(this.getDeleteTitleKey()), disabled:true});
		this.actionImport = CWHF.createAction(this.getImportButtonKey(), this.getImportIconCls(),
	    		"onImport", {tooltip:getText(this.getImportTitleKey(), getText("menu.admin.custom.projectType"))});
		this.actionExport = CWHF.createAction(this.getExportButtonKey(), this.getExportIconCls(),
	    		"onExport", {tooltip:getText(this.getExportTitleKey(), getText("menu.admin.custom.projectType")), disabled:true});
		if (com.trackplus.TrackplusConfig.appType !== APPTYPE_BUGS) {
		    this.actions = [this.actionAdd, /*this.actionSave,*/ this.actionDelete, this.actionImport, this.actionExport];
		} else {
			this.actions = [/*this.actionSave,*/ this.actionExport];
		}
	}

});
