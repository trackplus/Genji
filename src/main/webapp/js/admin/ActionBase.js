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

Ext.define("com.trackplus.admin.ActionBase",{
	extend:"Ext.Base",
	
	/**
	 * The localized entity name based on the localization key: should be implemented
	 */
	/*protected abstract*/getEntityLabel: function(config) {
		return "";
		//return getText('...');
	},
	
	/**
	 * Get the title for add/edit/copy/delete dialogs used also as tooltip for toolbar buttons
	 */
	/*protected*/getActionTooltip: function(tooltipKey, config) {
		return getText(tooltipKey, this.getEntityLabel(config));
	},
	
	/**
	 * Gets the docked items
	 */
	/*protected*/getToolbar: function(toolbarDock) {
		if (CWHF.isNull(toolbarDock)) {
			toolbarDock = "top";
		}
		return [{
			xtype: "toolbar",
			dock: toolbarDock,
			items: this.getToolbarActionButtons(this.actions) 
		}]
	},
	
	
	
	/**
	 * Create toolbar buttons from actions
	 */
	/*protected*/getToolbarActionButtons:function(actionList){
		var toolbarList=[];
		if(actionList){
			for(var i=0;i<actionList.length;i++) {
				toolbarList.push(new Ext.button.Button(actionList[i]));
			}
		}
		return toolbarList;
	},
	
	/**
	 * The title for a failure message
	 */
	//failureTitle:getText("common.err.failure"),

	/**
	 * The iconCls for the add button
	 */
	/*protected*/getAddIconCls: function() {
		return "add";
	},
	
	/**
	 * The iconCls for the add folder button
	 */
	/*protected*/getAddFolderIconCls: function() {
		return "add";
	},
	
	/**
	 * The key for "add" button text
	 */
	/*protected*/getAddButtonKey: function() {
		return "common.btn.add";
	},
	/**
	 * The title for "add" popup and "add" action tooltip
	 */
	/*protected*/getAddTitleKey: function() {
		return "common.lbl.add";
	},

	/**
	 * The iconCls for the edit button
	 */
	/*protected*/getEditIconCls: function() {
		return "edit";
	},
	/**
	 * The key for "edit" button text
	 */
	/*protected*/getEditButtonKey: function() {
		return "common.btn.edit";
	},
	/**
	 * The title for "edit" popup and "edit" action tooltip
	 */
	/*protected*/getEditTitleKey: function() {
		return "common.lbl.edit";
	},

	/**
	 * The iconCls for the "copy" button
	 */
	/*protected*/getCopyIconCls: function() {
		return "copy";
	},
	/**
	 * The key for "copy" button text
	 */
	/*protected*/getCopyButtonKey: function() {
		return "common.btn.copy";
	},
	/**
	 * The title for "copy" popup and "copy" action tooltip
	 */
	/*protected*/getCopyTitleKey: function() {
		return "common.lbl.copy";
	},

	/**
	 * The iconCls for the "cut" button
	 */
	/*protected*/getCutIconCls: function() {
		return "cut";
	},
	/**
	 * The key for "cut" button text
	 */
	/*protected*/getCutButtonKey: function() {
		return "common.btn.cut";
	},
	/**
	 * The the key for "cut" action tooltip
	 */
	/*protected*/getCutTitleKey: function() {
		return "common.lbl.cut";
	},
	
	/**
	 * The iconCls for the "paste" button
	 */
	/* protected */getPasteIconCls : function() {
	    return 'paste';
	},
	/**
	 * The key for "paste" button text
	 */
	/* protected */getPasteButtonKey : function() {
	    return 'common.btn.paste';
	},
	/**
	 * The key for "paste" context menu button' text
	 */
	/* protected */getPasteTitleKey : function() {
	    return 'common.lbl.paste';
	},
	
	/**
	 * The iconCls for "save" button
	 */
	/*protected*/getSaveIconCls: function() {
		return "save";
	},
	/**
	 * The key for "save" button text
	 */
	/*protected*/getSaveButtonKey: function() {
		return "common.btn.save";
	},
	/**
	 * The tooltip for "save" button
	 */
	/*protected*/getSaveTitleKey: function() {
		return "common.lbl.save";
	},
	
	
	/**
	 * The iconCls for "cancel" button
	 */
	/*protected*/getCancelIconCls: function() {
		return "cancel";
	},
	/**
	 * The key for "cancel" button text
	 */
	/*protected*/getCancelButtonKey: function() {
		return "common.btn.cancel";
	},
	
	/**
	 * The key for "cancel" button text
	 */
	/*protected*/getDoneButtonKey: function() {
		return "common.btn.done";
	},
	
	/**
	 * The key for "cancel" button text
	 */
	/*protected*/getResetIconCls: function() {
		return "reset";
	},
	
	/**
	 * The key for "cancel" button text
	 */
	/*protected*/getResetButtonKey: function() {
		return "common.btn.reset";
	},
	
	/**
	 * The iconCls for "import" button
	 */
	/*protected*/getImportIconCls: function() {
		return "import";
	},
	/**
	 * The key for "import" button text
	 */
	/*protected*/getImportButtonKey: function() {
		return "common.btn.import";
	},
	/**
	 * The tooltip for "import" button
	 */
	/*protected*/getImportTitleKey: function() {
		return "common.lbl.import";
	},
	
	/**
	 * The iconCls for "export" button
	 */
	/*protected*/getExportIconCls: function() {
		return "export";
	},
	/**
	 * The key for "export" button text
	 */
	/*protected*/getExportButtonKey: function() {
		return "common.btn.export";
	},
	/**
	 * The tooltip for "export" button
	 */
	/*protected*/getExportTitleKey: function() {
		return "common.lbl.export";
	},
	
	/**
	 * The iconCls for the delete button
	 */
	/*protected*/getDeleteIconCls: function() {
		return "delete";
	},
	/**
	 * The key for "delete" button text
	 */
	/*protected*/getDeleteButtonKey: function() {
		return "common.btn.delete";
	},
	/**
	 * The title for "delete" popup and "delete" action tooltip
	 */
	/*protected*/getDeleteTitleKey: function() {
		return "common.lbl.delete";
	},
	
	/*protected*/getReplacementTitleKey: function() {
		return "common.lbl.replacement";
	},
	
	/**
	 * The key for "delete" button text
	 */
	/*protected*/getRemoveButtonKey: function() {
		return "common.btn.remove";
	},
	
	/**
	 * The title for "delete" popup and "delete" action tooltip
	 */
	/*protected*/getRemoveTitleKey: function() {
		return "common.lbl.remove";
	},
	
	/**
	 * The delete confirmation text (if it will be asked for confirmation)
	 */
	/*protected*/getRemoveWarningKey: function() {
		return "common.lbl.removeWarning";
	},
	
	/**
	 * The iconCls for "download" button
	 */
	/*protected*/getDownloadIconCls: function() {
		return "download";
	},
	/**
	 * The key for "download" button text
	 */
	/*protected*/getDownloadButtonKey: function() {
		return "common.btn.download";
	},
	/**
	 * The tooltip for "download" button
	 */
	/*protected*/getDownloadTitleKey: function() {
		return "common.lbl.download";
	},
	
	/**
	 * The iconCls for "upload" button
	 */
	/*protected*/getUploadIconCls: function() {
		return "upload";
	},
	/**
	 * The key for "upload" button text
	 */
	/*protected*/getUploadButtonKey: function() {
		return "common.btn.upload";
	},
	/**
	 * The tooltip for "upload" button
	 */
	/*protected*/getUploadTitleKey: function() {
		return "common.lbl.upload";
	},
	
	/**
	 * The iconCls for "moveUp" button
	 */
	/*protected*/getMoveUpIconCls: function() {
		return "moveUp";
	},
	/**
	 * The key for "moveUp" button text
	 */
	/*protected*/getMoveUpButtonKey: function() {
		return "common.btn.up";
	},
	/**
	 * The tooltip for "moveUp" button
	 */
	/*protected*/getMoveUpTitleKey: function() {
		return "common.lbl.up";
	},
	
	/**
	 * The iconCls for "moveDown" button
	 */
	/*protected*/getMoveDownIconCls: function() {
		return "moveDown";
	},
	/**
	 * The key for "moveDown" button text
	 */
	/*protected*/getMoveDownButtonKey: function() {
		return "common.btn.down";
	},
	/**
	 * The tooltip for "moveDown" button
	 */
	/*protected*/getMoveDownTitleKey: function() {
		return "common.lbl.down";
	},
	
	/**
	 * The iconCls for "moveDown" button
	 */
	/*protected*/getReloadIconCls: function() {
		return "reload";
	},
	
	/**
	 * The key for "moveDown" button text
	 */
	/*protected*/getReloadButtonKey: function() {
		return "common.btn.reload";
	},
	
	/**
	 * The iconCls for "applyFilter" button
	 */
	/*protected*/getApplyFilterIconCls: function() {
		return "filterExec";
	},
	
	/**
	 * The key for "applyFilter" button text
	 */
	/*protected*/getApplyFilterButtonKey: function() {
		return "common.btn.applyFilter";
	},
	
	/**
	 * The iconCls for "executeReport" button
	 */
	/*protected*/getExecuteReportIconCls: function() {
		return "rtemplateExec";
	},
	
	/**
	 * The key for "executeReport" button text
	 */
	/*protected*/getExecuteReportButtonKey: function() {
		return "common.btn.executeReport";
	}
	
});
