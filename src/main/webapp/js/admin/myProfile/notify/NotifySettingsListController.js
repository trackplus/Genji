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

Ext.define("com.trackplus.admin.NotifySettingsListController", {
	extend: "Ext.app.ViewController",
	alias: "controller.notifySettingsList",
	mixins: {
		baseController: "com.trackplus.admin.GridBaseController"
	},
	
	entityDialog: "com.trackplus.admin.NotifySettingsEdit",
	
	getDeleteUrl: function() {
		return "notifySettings!delete.action";
	},
	
	/**
	 * Parameters for deleting an existing entity
	 */
	getDeleteParams:function(selectedRecord) {
		var params = new Object();
		params["notifySettingsID"]=selectedRecord.get("id");
		params["defaultSettings"]=this.getView().getDefaultSettings();
		return params;
	},

	/**
	 * Handler for overwrite
	 */
	onOverwrite: function(){
		var record=this.getView().getSingleSelectedRecord();
		if (record) {
			this.onAddEdit(this.getView().getActionTooltip(this.getView().getOverwriteTitleKey()), record, "overwrite");
		}
	},

	/**
	 * Render the inherited rows as grey
	 */
	inheritedRenderer: function(value, metadata, record) {
		if (record.data.inherited) {
			metadata.style = 'color:#909090';
		}
		return value;
	},

	/**
	 * Enable change only if not inherited
	 */
	onSelectionChange: function(view, arrSelections) {
		if (CWHF.isNull(arrSelections) || arrSelections.length===0){
			this.getView().actionDelete.setDisabled(true);
			this.getView().actionEdit.setDisabled(true);
			if (this.getView().actionOverwrite) {
				this.getView().actionOverwrite.setDisabled(true);
			}
		} else {
			var selectedRecord = arrSelections[0];
			var inherited = selectedRecord.data["inherited"];
			this.getView().actionDelete.setDisabled(inherited);
			this.getView().actionEdit.setDisabled(inherited);
			if (this.getView().actionOverwrite) {
				this.getView().actionOverwrite.setDisabled(!inherited);
			}
		}
	},

	/**
	 *  Handler for double click
	 */
	onItemDblClick:function(view, record) {
		var inherited = record.data["inherited"];
		if (inherited) {
			this.onOverwrite.call(this);
		} else {
			this.onEdit.call(this);
		}
	}
});
