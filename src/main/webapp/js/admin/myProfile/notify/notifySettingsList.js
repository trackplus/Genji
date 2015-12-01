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

Ext.define("com.trackplus.admin.NotifySettingsList", {
	extend:"com.trackplus.admin.GridBase",
	xtype: "notifySettingsList",
    controller: "notifySettingsList",
	config: {
		defaultSettings:false,
		//the exclusiveProject set only for automail in project settings
		exclusiveProjectID: null
	},
	columns: [{
			text: getText('common.lbl.name'),
			flex:1, dataIndex: 'projectLabel', sortable:true, renderer:"inheritedRenderer",
			filter: {
	            type: "string"
	        }
		}, {
			text: getText('admin.customize.automail.trigger.lblAlone'),
			flex:1, dataIndex: 'triggerLabel', sortable:true, renderer:"inheritedRenderer",
			filter: {
	            type: "string"
	        }
		}, {
			text: getText('admin.customize.automail.filter.lblAlone'),
			flex:1, dataIndex: 'filterLabel', sortable:true, renderer:"inheritedRenderer",
			filter: {
	            type: "string"
	        }
		}],
		
	initComponent : function() {
		this.fields = [{name: 'id',	type: 'int'},
						{name: 'project',	type: 'int'},
						{name: 'projectLabel',	type: 'string'},
						{name: 'triggerLabel',	type: 'string'},
						{name: 'filterLabel',	type: 'string'},
						{name: 'inherited',	type: 'boolean'}];
		this.storeUrl = "notifySettings!loadList.action";
		this.callParent();
	},
	
	
	getEntityLabel: function() {
		return getText('admin.customize.automail.assignments.lbl.assignment');
	},
	
	actionOverwrite:null,

	/**
	 * The iconCls for the add button, overwrites base class icon
	 */
	getAddIconCls: function() {
		return 'automailAdd';
	},
	/**
	 * The iconCls for the edit button, overwrites base class icon
	 */
	getEditIconCls: function() {
		return 'automailEdit';
	},
	
	/**
	 * The iconCls for the overwrite button
	 */
	getOverwriteIconCls: function() {
		return 'copy';
	},

	/**
	 * The key for overwrite button text
	 */
	getOverwriteButtonKey: function() {
		return 'common.btn.overwrite';
	},

	/**
	 * The title for "add" popup and "add" action tooltip
	 */
	getOverwriteTitleKey: function() {
		return 'common.lbl.overwrite';
	},

	/**
	 * Get extra parameters for grid load
	 */
	getLoadGridParams:function() {
		return {defaultSettings: this.getDefaultSettings(), exclusiveProjectID:this.getExclusiveProjectID()};
	},

	//actions
	initActions:function(){
		this.actionAdd = CWHF.createAction(this.getAddButtonKey(), this.getAddIconCls(), "onAdd", {tooltip:this.getActionTooltip(this.getAddTitleKey())});
		this.actionEdit = CWHF.createAction(this.getEditButtonKey(), this.getEditIconCls(), "onEdit", {tooltip:this.getActionTooltip(this.getEditTitleKey()), disabled:true});
		this.actions = [this.actionAdd, this.actionEdit];
		if (this.getDefaultSettings()===false) {
			this.actionOverwrite = CWHF.createAction(this.getOverwriteButtonKey(), this.getOverwriteIconCls(), "onOverwrite", {tooltip:this.getActionTooltip(this.getOverwriteTitleKey()), disabled:true});
			this.actions.push(this.actionOverwrite);
		}
		this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(), "onDelete", {tooltip:this.getActionTooltip(this.getDeleteTitleKey()), disabled:true});
		this.actions.push(this.actionDelete);
		
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	getGridContextMenuActions: function(selectedRecords, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			var inherited = selectedRecords.data['inherited'];
			if (inherited && this.actionOverwrite) {
				actions.push(this.actionOverwrite);
			} else {
				if (this.actionEdit) {
					actions.push(this.actionEdit);
				}
				if (this.actionDelete) {
					actions.push(this.actionDelete);
				}
			}
		}
		return actions;
	},
	
	onGridStoreLoad: function(store, records) {
		if (this.getExclusiveProjectID() && records && records.length>0) {
			this.actionAdd.setDisabled(true);
		} else {
			this.actionAdd.setDisabled(false);
		}
	}
	
});
