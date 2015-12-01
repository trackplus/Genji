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

Ext.define("com.trackplus.admin.GridBase",{
	extend:"Ext.grid.Panel",
	mixins:{
		actionsBase: "com.trackplus.admin.ActionBase"
	},
	//autoLoadGrid:true,
	fields:null,
	storeUrl: null,
	autoLoadStore: true,
	enableColumnHide: false,
	enableColumnMove: false,
	//general CRUD actions in grid
	actionAdd:null,
	actionEdit:null,
	actionCopy:null,
	actionDelete:null,
	actions:null,
	
	confirmDeleteEntity:true,
	confirmDeleteNotEmpty:true,
	//has Edit action
	useEdit: true,
	//has copy action
	useCopy:true,
	
	initComponent : function() {
		var me = this;
		me.initBase();
		this.store = Ext.create('Ext.data.Store', {
			fields:this.fields,
			proxy: {
				type: 'ajax',
				url: this.storeUrl,
				extraParams:this.getLoadGridParams(),
				reader: {
					type: 'json'
				}
			},
			listeners: {
				//this event is not dispatched to viewController (store is not Ext.Component?) only in view
				load: {fn: me.onGridStoreLoad, scope:me}
			},
			autoLoad:this.autoLoadStore
		});
		this.plugins = ["gridfilters"];
		this.selModel = this.getGridSelectionModel();
		this.region = "center";
		this.border = false;
		this.bodyBorder = false;
		this.cls = "gridNoBorder";
		this.columnLines = true;
		this.viewConfig = this.getViewConfig();
		this.dockedItems = this.getDockedItems();
		/*this.listeners = {	
			itemdblclick: {fn:me.onItemDblClick, scope:me},
			itemcontextmenu: {fn:me.onGridRowCtxMenu, scope:me},
			selectionchange: {fn:me.onGridSelectionChange, scope:me}	 
		};*/
		this.callParent();
	},

	/**
	 * Initialization method
	 */
	/*protected*/initBase: function() {
		this.initActions();
	},

	/**********************************************action/context menu related methods***********************************************/

	/**
	 * Get the title for add/edit/copy/delete window used also as tooltip for toolbar buttons
	 */
	/*protected*/getActionTooltip: function(tooltipKey) {
		return getText(tooltipKey, this.getEntityLabel());
	},
	
	/**
	 * The localized entity name based on the localization key: should be implemented
	 */
	/*protected abstract*/getEntityLabel: function() {
		return "";
		//return getText('...');
	},
	
	/**
	 * Initialize all possible actions
	 */
	/*protected*/initActions:function() {
		this.actionAdd = CWHF.createAction(this.getAddButtonKey(), this.getAddIconCls(), "onAdd", {tooltip:this.getActionTooltip(this.getAddTitleKey())});
		this.actions = [this.actionAdd];
		if (this.useEdit===true) {
			this.actionEdit = CWHF.createAction(this.getEditButtonKey(), this.getEditIconCls(), "onEdit", {tooltip:this.getActionTooltip(this.getEditTitleKey()), disabled:true});
			this.actions.push(this.actionEdit);
		}
		this.actionDelete = CWHF.createAction(this.getDeleteButtonKey(), this.getDeleteIconCls(), "onDelete", {tooltip:this.getActionTooltip(this.getDeleteTitleKey()), disabled:true});
		this.actions.push(this.actionDelete);
		if (this.useCopy===true) {
			this.actionCopy = CWHF.createAction(this.getCopyButtonKey(), this.getCopyIconCls(), "onCopy", {tooltip:this.getActionTooltip(this.getCopyTitleKey()), disabled:true});
			this.actions.push(this.actionCopy);
		}
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	/*protected*/getGridContextMenuActions: function(selectedRecords, selectionIsSimple) {
		var actions = [];
		if (selectionIsSimple) {
			if (this.useEdit && this.actionEdit) {
				actions.push(this.actionEdit);
			}
			if (this.useCopy && this.actionCopy) {
				actions.push(this.actionCopy);
			}
		}
		if (this.actionDelete) {
			actions.push(this.actionDelete);
		}
		return actions;
	},

	
	/**
	 * The url to load the grid this should be overridden
	 */
	getLoadStoreUrl:function() {
		return this.baseAction + ".action";
	},
	/**
	 * Get extra parameters for grid load
	 * (like "defaultSettings" for "my" and "default" automail settings)
	 */
	/*protected*/getLoadGridParams:function() {
		return null;
	},

	/**
	 * Handler to execute after loading the store
	 */
	/*protected*/onGridStoreLoad: function(store, records) {
	},
	
	/**
	 * Defined typically if there is no "classic" toolbar available (like grid is in a popup)
	 */
	/*protected*/getDockedItems: function() {
		return [{
			xtype: 'toolbar',
			dock: 'top',
			items: this.getToolbarActionButtons(this.actions) 
		}]
	},

	
	getToolbarActionButtons:function(actionList){
		var me=this;
		var toolbarList=[];
		if(actionList){
			for(var i=0;i<actionList.length;i++) {
				toolbarList.push(new Ext.button.Button(actionList[i]));
			}
		}
		return toolbarList;
	},
	
	/*protected*/getViewConfig: function() {
		return {
			forceFit: true,
			stripeRows: true,
			listeners: {
					//although these event listeners could be attached to the gridPanel directly but then the corresponding listeners of the viewController are not called
					itemdblclick: "onItemDblClick",
					itemcontextmenu: "onItemContextMenu",
					selectionchange: "onSelectionChange"
						 
				}
		};
	},

	

	/**
	 * Get the configuration for selection model
	 */
	/*protected*/getGridSelectionModel: function() {
		var selectionModelConfig = new Object();
		if (this.allowMultipleSelections) {
			selectionModelConfig.mode="MULTI";
		} else {
			selectionModelConfig.mode="SINGLE";
			if (this.allowDeselect) {
				selectionModelConfig.allowDeselect=this.allowDeselect;
			}
		}
		if (CWHF.isNull(this.gridSelectionModel)) {
			return Ext.create("Ext.selection.RowModel", selectionModelConfig);
		} else {
			return this.gridSelectionModel;
		}
	},
	
	/**
	 * Whether the selection is simple or multiple
	 */
	/*public*/selectionIsSimple: function() {
		if (this.allowMultipleSelections) {
			var selectedRecords = this.getGridSelection();
			return selectedRecords && selectedRecords.length===1;
		} else {
			var selectedRecord = this.getLastSelectedGridRow();
			return selectedRecord!==null;
		}
	},

	/**
	 * The the selected grid rows (possibly more than one)
	 */
	/*public*/getGridSelection: function() {
		return this.getSelectionModel().getSelection();
	},
	
	/**
	 * The the last selected grid row (only one row)
	 */
	getLastSelectedGridRow: function() {
		return this.getSelectionModel().getLastSelected();
	},
	
	/**
	 * Gets the selected data
	 * it can be simple or multiple selection
	 */
	/*public*/getSelectedRecords: function() {
		var me=this;
		if (me.allowMultipleSelections) {
			return me.getGridSelection();
		} else {
			return me.getLastSelectedGridRow();
		}
	},
	
	/**
	 * Get the data of the single selected record
	 * For no selection or multiple selection return null
	 * param: typically fromTree
	 */
	/*private*/getSingleSelectedRecordData: function(param) {
		var selectedRecord = this.getSingleSelectedRecord(param);
		if (selectedRecord) {
			return selectedRecord.data;
		}
		return null;
	},

	/**
	 * Get the data of the single selected record
	 * For no selection or multiple selection return null
	 */
	/*private*/getSingleSelectedRecord: function(param) {
		var selectedRecords = this.getSelectedRecords(param);
		if (selectedRecords) {
			if (this.allowMultipleSelections) {
				if (selectedRecords && selectedRecords.length===1) {
					return selectedRecords[0];
				} else {
					return null;
				}
			} else {
				return selectedRecords;
			}
		} else {
			return null;
		}
	}
	
});
