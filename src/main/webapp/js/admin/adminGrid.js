/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

Ext.define('com.trackplus.admin.GridConfig',{
	extend:'com.trackplus.admin.CrudBase',
	config: {
		urlStore:null,
		autoLoadGrid:true,
		fields:null,
		columnModel:null,
		useCopy:false,
		enableColumnHide: false,
		enableColumnMove: false,
		features:[],
		/**
		 * The name of the struts action field for the ID of the currently modified entityID
		 * Mandatory field
		 */
		entityID:'objectID'
	},
	enableColumnHide: false,
	enableColumnMove: false,
	actionAdd:null,
	actionEdit:null,
	actionCopy:null,
	actionDelete:null,

	constructor : function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
		me.init();
	},

	/**
	 * Initialization method
	 */
	/*protected*/init: function() {
		this.initActions();
	},

	/**********************************************action/context menu related methods***********************************************/

	/**
	 * Initialize all possible actions
	 */
	/*protected*/initActions:function(){
		var me=this;
		me.actionAdd = me.createAction(me.getAddButtonKey(), me.getAddIconCls(), me.onAdd, false, me.getAddTitleKey());
		me.actionEdit = me.createAction(me.getEditButtonKey(), me.getEditIconCls(), me.onEdit, true, me.getEditTitleKey());
		me.actionDelete = me.createAction(me.getDeleteButtonKey(), me.getDeleteIconCls(), me.onDelete, true, me.getDeleteTitleKey());
		if (me.useCopy==true) {
			me.actionCopy = me.createAction(me.getCopyButtonKey(), me.getCopyIconCls(), me.onCopy, true, me.getCopyTitleKey());
			me.actions=[me.actionAdd,me.actionEdit, me.actionCopy, me.actionDelete];
		}else{
			me.actions=[me.actionAdd,me.actionEdit, me.actionDelete];
		}
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 */
	/*protected*/getGridContextMenuActions: function(selectedRecords, selectionIsSimple) {
		var me=this;
		var actions = [];
		if (selectionIsSimple) {
			actions.push(me.actionEdit);
			if (me.useCopy) {
				actions.push(me.actionCopy);
			}
		}
		actions.push(me.actionDelete);
		return actions;
	},

	/**
	 * Show the context menu in grid
	 */
	/*private*/onGridRowCtxMenu: function(grid, record, item, index, evtObj) {
		var me=this;
		evtObj.stopEvent();
		grid.getSelectionModel().select(record);
		var selectedRecords = me.getSelectedRecords();
		var selectionIsSimple = me.selectionIsSimple();
		var actions=me.getGridContextMenuActions(selectedRecords, selectionIsSimple);
		var gridRowCtxMenu = me.createContextMenu(record, actions);
		gridRowCtxMenu.showAt(evtObj.getXY());
		return false;
	},
	/**********************************************grid methods***********************************************/
	/**
	 * Gets the grid store's fields for the selected node
	 * either override this method or set the "fields" field in the derived class
	 */
	/*protected abstract*/getGridFields: function() {
		return this.fields;
	},

	/**
	 * Gets the column model array for the grid.
	 * Alternative to the columnModel config property (if "this" keyword is needed in column model)
	 */
	/*protected abstract*/getColumnModel:function() {
		return this.columnModel;
	},
	/**
	 * The url to load the grid
	 * either this should be overridden or the urlStore should be specified in the config
	 */
	/*protected*/getLoadGridUrl:function() {
		return this.urlStore;
	},
	/**
	 * Get extra parameters for grid load
	 * (like "defaultSettings" for "my" and "default" automail settings)
	 */
	/*protected*/getLoadGridParams:function() {
		return null;
	},

	/**
	 * Defined typically if there is no "classic" toolbar available (like grid is in a popup)
	 */
	/*protected*/getDockedItems: function() {
		return null;
		/*return [{
			xtype: 'toolbar',
			dock: 'top',
			items: [];
		}]*/
	},

	/**
	 * Initialize the grid
	 */
	/*private*/initGrid:function(){
		var me=this;
		var store = Ext.create('Ext.data.Store', {
			fields:me.getGridFields(),
			proxy: {
				type: 'ajax',
				url: me.getLoadGridUrl(),
				extraParams:me.getLoadGridParams(),
				reader: {
					type: 'json'
				}
			},
			listeners: {
				load: {fn: me.onGridStoreLoad, scope:me}
			},
			autoLoad:me.autoLoadGrid
		});
		me.grid =  Ext.create('Ext.grid.Panel', {
			store:	store,
			selModel:	me.getGridSelectionModel(),
			columns:	me.getColumnModel(),
			//cls: 'adminGrid',
			region:'center',
			stripeRows:	true,
			border:	false,
			margin:'0 0 0 0',
			bodyBorder:false,
			cls:'gridNoBorder',
			style:me.style,
			enableColumnHide: this.enableColumnHide,
			enableColumnMove: this.enableColumnMove,
			columnLines: true,
			viewConfig:	this.getViewConfig(),
			dockedItems: this.getDockedItems(),
			features:me.features,
			listeners: {
				itemdblclick: {fn:me.onItemDblClick, scope:me},
				itemcontextmenu: {fn:me.onGridRowCtxMenu, scope:me},
				selectionchange: {fn:me.onGridSelectionChange, scope:me}
			}
		});
	},

	/*protected*/getViewConfig: function() {
		return {
			forceFit: true
		};
	},

	/*protected*/onGridStoreLoad: function(store, records) {

	},

	/**
	 * Get the grid instance
	 */
	/*public*/getGrid:function(){
		var me=this;
		if(me.grid==null){
			me.initGrid();
		}
		return me.grid;
	},

	/**********************************************popup methods: URL and params***********************************************/

	/**
	 * Get the base action name from struts configuration
	 * Should be overridden
	 */
	/*public*/getStrutsBaseAction: function() {
		return this.baseAction;
	},

	/**
	 * Url for editing an entity
	 * We suppose that add/edit/copy use the same edit method on server side
	 * Differentiation is made based on this.entityID and copy request parameter
	 * add: this.entityID==null
	 * edit: this.entityID!=null && copy==false
	 * copy: this.entityID!=null && copy==true
	 */
	/*protected*/getEditUrl: function(recordData, operation){
		return this.baseAction+'!edit.action';
	},
	/**
	 * Parameters for adding a new entity
	 * Specify extra parameters if needed
	 * (like "defaultSettings" for "my" and "default" automail settings)
	 */
	/*protected*/getAddParams: function() {
		return null;
	},
	/**
	 * Parameters for editing an existing entity
	 * recordData: the selected entity data
	 */
	/*protected*/getEditParams: function(recordData) {
		var result=new Object();
		result[''+this.entityID]=this.getRecordID(recordData);
		return result;
	},
	/**
	 * Parameters for editing a copy of an existing entity
	 * recordData: the selected entity data
	 */
	/*protected*/getCopyParams: function(recordData) {
		var result=this.getEditParams(recordData);
		result['copy']=true;
		return result;
	},

	/**
	 * Parameters for loading the edited entity
	 * recordData: the selected entity data
	 * action: the submit action
	 */
	/*private*/getLoadParams: function(recordData, action) {
		var me=this;
		if (action==null){
			action="add";
		}
		if (action=="add") {
			return me.getAddParams();
		}
		if (action=="edit") {
			return me.getEditParams(recordData);
		}
		if (action=="copy") {
			return me.getCopyParams(recordData);
		}
		//for extra actions default to editParams
		return me.getEditParams(recordData);
	},

	/**
	 * Url for saving of an entity
	 */
	/*protected*/getSaveUrl: function(){
		return this.baseAction+'!save.action';
	},

	/**
	 * Parameters for saving an entity (extra parameters additionally to submit parameters)
	 */
	/*protected*/getSaveParams: function(recordData, action) {
		var me=this;
		var params = null;
		if (action==null){
			action="add";
		}
		if (action=="add"){
			params=me.getAddParams();
		}
		if (action=="edit"){
			params=me.getEditParams(recordData);
		}
		if (action=="copy"){
			params=me.getCopyParams(recordData);
		}
		if (params==null) {
			//should not be null because dynamic parameters might be added right before submit
			//(in either submitHandler()  or preSubmitProcess() implementations)
			params = new Object();
		}
		return params;
	},

	/**
	 * Get the ID based from the recordData and extra config
	 *  Should be overridden
	 */
	/*protected*/getRecordID: function(recordData, extraConfig) {
		if (recordData!=null) {
			return recordData.id;
		}
		return null;
	},

	/**
	 * Parameter name for the submitted entityID(s) by delete
	 * If allowMultipleSelections==false then the same this.entityID can be used for delete and edit submits
	 * If allowMultipleSelections==true this should return another name (the submitted value will be stored on the server typically in an Integer[])
	 */
	/*protected*/getDeleteParamName: function() {
		return this.entityID;
	},

	/**
	 * Reload after a change operation
	 */
	/*protected*/reload:com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit,

	/**
	 * Parameters for reload/select after a save operation (following an add/edit/copy)
	 * which are known before the save operation is executed.
	 * Typically same as getLoadGridParams();
	 */
	/*protected abstract*/getReloadParamsAfterSave:function() {
		return this.getLoadGridParams();
	},
	/**
	 * Get parameters for reload/select after a save operation (following an add/edit/copy)
	 * which are known only after the save operation is executed.
	 * These parameter values are extracted form the JSON object resulting from save
	 * The format of the expected object is:
	 * {parameterName:'<parameterName>', fieldNameFromResult:'<fieldNameFromResult>'}
	 * For save after edit the parameters are probably known even before save
	 * (supposed that the edit does not modify the record id)
	 * but for save after add/copy not (the new id is needed for selection).
	 * It is not differentiated for add/edit/copy because all use the same edit method on the server side
	 */
	/*protected abstract*/getReloadParamsAfterSaveFromResult:function() {
		return {parameterName:'rowToSelect', fieldNameFromResult:'id'};
	},

	/**********************************************handler methods***********************************************/
	/**
	 *  Handler for double click
	 */
	/*protected*/onItemDblClick:function(view, record){
		var me=this;
		me.onEdit.call(me);
	},
	/**
	 * Handler for grid selection change: enable/disable actions based on the actual selection
	 */
	/*protected*/onGridSelectionChange: function (view, selections) {
		var me=this;
		if (selections==null || selections.length==0){
			//no selection: disable edit/copy/delete
			me.actionDelete.setDisabled(true);
			me.actionEdit.setDisabled(true);
			if (me.actionCopy!=null) {
				me.actionCopy.setDisabled(true);
			}
		} else {
			//activate delete for any selection
			me.actionDelete.setDisabled(false);
			if (selections.length==1) {
				//activate edit/copy only if there is exactly one selection
				me.actionEdit.setDisabled(false);
				if (me.actionCopy!=null) {
					me.actionCopy.setDisabled(false);
				}
			} else {
				//disable edit/copy only if there is more than one selection
				me.actionEdit.setDisabled(true);
				if (me.actionCopy!=null) {
					me.actionCopy.setDisabled(true);
				}
			}
		}
	},

	/**
	 * Handler for adding a new entity
	 */
	/*private*/onAdd: function(){
		var me=this;
		if(!me.canCreateEntity()){
			//alert("can't create entity");
			return;
		}
		var recordData=new Object();
		me.openEditEntity.call(me,me.getAddTitleKey(),recordData,"add");
	},
	/**
	 * Handler for editing an existing entity
	 */
	/*private*/onEdit: function(){
		var me=this;
		var recordData=me.getSingleSelectedRecordData();
		if(recordData==null){
			return true;
		}
		me.openEditEntity(me.getEditTitleKey(),recordData,"edit");
	},
	/**
	 * Handler for copying an existing entity
	 */
	/*private*/onCopy: function(){
		var me=this;
		var recordData=me.getSingleSelectedRecordData();
		if(recordData==null){
			return true;
		}
		me.openEditEntity(me.getCopyTitleKey(),recordData,"copy");
	},

	/**
	 * Common handler for add/edit/copy operations
	 */
	/*private*/openEditEntity: function(titleKey, recordData, operation) {
		var me=this;
		var load = {loadUrl:me.getEditUrl(recordData, operation), loadUrlParams:me.getLoadParams(recordData, operation), loadHandler:me.loadHandler};
		var submitParams = me.getSaveParams(recordData, operation);
		var submit = {	submitUrl:me.getSaveUrl(),
						submitUrlParams:submitParams,
						submitButtonText:me.getSaveLabel(operation),
						submitHandler:me.submitHandler,
						submitAction:operation,
						refreshAfterSubmitHandler:me.reload,
						refreshParametersBeforeSubmit:me.getReloadParamsAfterSave(),
						refreshParametersAfterSubmit:me.getReloadParamsAfterSaveFromResult()
					};
		var additionalActions = me.getAdditionalActions(recordData, submitParams, operation);
		if (additionalActions!=null) {
			additionalActions.push(submit);
			submit = additionalActions;
		}
		//either me.formEdit or items should be set
		me.formEdit = me.createEditForm(recordData, operation);
		var items = me.getEditPanelItems(recordData, operation);
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig',
			{title:me.getTitle(titleKey), width:me.editWidth, height:me.editHeight,
				load:load, submit:submit,
				formPanel:me.formEdit, items:items,
				postDataProcess:me.afterLoadForm, preSubmitProcess:me.preSubmitProcess});
		windowConfig.showWindowByConfig(me);
	},

	/**
	 * Extra processing that should be done in the edit panel after the data is loaded from the server
	 * like load combo data, show/hide contols
	 */
	/*protected*/afterLoadForm: function(data, panel) {
	},

	/**
	 * Handler for delete
	 */
	/*private*/onDelete: function() {
		var me=this;
		var selectedRecords = me.getSelectedRecords();
		if (selectedRecords!=null) {
			me.deleteHandler(selectedRecords);
		}
	}

});
