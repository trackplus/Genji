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

/**
 * Base for all ViewControllers used for GridBase implementations
 */
Ext.define("com.trackplus.admin.GridBaseController",{
	extend:"com.trackplus.admin.DeleteController",
	/**
	 * The following parameters must be specified for a derived GridBaseController
	 */
	//the base action name
	//baseAction:null,
	//the name of the request parameter the selected record's ID will be sent to the server 
	//entityID:null,
	//the with of the edit dialog
	//editWidth:800,
	//the height of the edit dialog
	//editHeight:600,
	
	getBaseAction:function() {
		return this.baseAction;
	},
	
	getNodeBaseAction: function() {
		return this.getBaseAction();
	},
	 
	onAdd: function(button, event) {
		var recordData=new Object();
	 	this.openEditEntity.call(this, this.getView().getActionTooltip(this.getView().getAddTitleKey()), recordData,"add");
	},
	
	onEdit: function(button, event) {
		var recordData = this.getView().getSingleSelectedRecordData();
		if(CWHF.isNull(recordData)){
			return true;
		}
		this.openEditEntity.call(this, this.getView().getActionTooltip(this.getView().getEditTitleKey()), recordData,"edit");
	},

	onCopy: function(button, event) {
		var recordData = this.getView().getSingleSelectedRecordData();
		if(CWHF.isNull(recordData)){
			return true;
		}
	 	this.openEditEntity.call(this, this.getView().getActionTooltip(this.getView().getCopyTitleKey()), recordData,"copy");
	},
	
	onDelete: function(button, event) {
		var selectedRecords = this.getView().getSelectedRecords();
		if (selectedRecords) {
			var entityLabel = this.getView().getEntityLabel();
			this.deleteHandler(entityLabel,  selectedRecords);
		}
	},
	
	/**
	 *  Handler for double click
	 */
	/*protected*/onItemDblClick: function(view, record){
		this.onEdit();
	},
	
	/**
	 * Handler for grid selection change: enable/disable actions based on the actual selection
	 */
	/*protected*/onSelectionChange: function(selectionModel, selections) {
		if (CWHF.isNull(selections) || selections.length===0){
			//no selection: disable edit/copy/delete
			if (this.getView().actionDelete) {
				this.getView().actionDelete.setDisabled(true);
			}
			if (this.getView().actionEdit) {
				this.getView().actionEdit.setDisabled(true);
			}
			if (this.getView().actionCopy) {
				this.getView().actionCopy.setDisabled(true);
			}
		} else {
			//activate delete for any selection
			if (this.getView().actionDelete) {
				this.getView().actionDelete.setDisabled(false);
			}
			if (selections.length===1) {
				//activate edit/copy only if there is exactly one selection
				if (this.getView().actionEdit) {
					this.getView().actionEdit.setDisabled(false);
				}
				if (this.getView().actionCopy) {
					this.getView().actionCopy.setDisabled(false);
				}
			} else {
				//disable edit/copy only if there is more than one selection
				if (this.getView().actionEdit) {
					this.getView().actionEdit.setDisabled(true);
				}
				if (this.getView().actionCopy) {
					this.getView().actionCopy.setDisabled(true);
				}
			}
		}
	},
	
	/**
	 * Show the context menu in grid
	 */
	/*private*/onItemContextMenu: function(view, record, item, index, evtObj) {
		evtObj.stopEvent();
		view.getSelectionModel().select(record);
		var selectedRecords = this.getView().getSelectedRecords();
		var selectionIsSimple = this.getView().selectionIsSimple();
		var actions=this.getView().getGridContextMenuActions(selectedRecords, selectionIsSimple);
		var gridRowCtxMenu = this.createContextMenu(record, actions);
		gridRowCtxMenu.showAt(evtObj.getXY());
		return false;
	},
	
	openEditEntity: function(title, recordData, operation) {
		var me=this;
		var load = {loadUrl:this.getEditUrl(recordData, operation), loadUrlParams:this.getLoadParams(recordData, operation), loadHandler:this.loadHandler};
		var submitParams = this.getSaveParams(recordData, operation);
		var submit = {	submitUrl:this.getSaveUrl(),
						submitUrlParams:submitParams,
						submitButtonText:this.getSaveLabel(operation),
						submitHandler:this.submitHandler,
						submitAction:operation,
						refreshAfterSubmitHandler:this.reload,
						refreshParametersBeforeSubmit:this.getReloadParamsAfterSave(),
						refreshParametersAfterSubmit:this.getReloadParamsAfterSaveFromResult()
					};
		var additionalActions = this.getAdditionalActions(recordData, submitParams, operation);
		if (additionalActions) {
			additionalActions.push(submit);
			submit = additionalActions;
		}
		//either formPanel or items should be set
		var formEdit = this.createEditForm(recordData, operation);
		var items = this.getEditPanelItems(recordData, operation);
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig',
			{title:title, width:this.editWidth, height:this.editHeight,
				load:load, submit:submit,
				formPanel: formEdit, items:items,
				postDataProcess:this.afterLoadForm, preSubmitProcess:this.preSubmitProcess});
		windowConfig.showWindowByConfig(me);
	},
		
	/**
	 * Get the ID based from the recordData and extra config
	 *  Should be overridden
	 */
	/*protected*/getRecordID: function(recordData, extraConfig) {
		if (recordData) {
			return recordData.id;
		}
		return null;
	},
	
	/**
	 * Url for editing an entity
	 * We suppose that add/edit/copy use the same edit method on server side
	 * Differentiation is made based on this.entityID and copy request parameter
	 * add: this.entityID===null
	 * edit: this.entityID && copy===false
	 * copy: this.entityID && copy===true
	 */
	/*protected*/getEditUrl: function(recordData, operation){
		return this.getBaseAction()+'!edit.action';
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
		if (CWHF.isNull(action)){
			action="add";
		}
		if (action==="add") {
			return this.getAddParams();
		}
		if (action==="edit") {
			return this.getEditParams(recordData);
		}
		if (action==="copy") {
			return this.getCopyParams(recordData);
		}
		//for extra actions default to editParams
		return this.getEditParams(recordData);
	},
	
	/**
	 * Url for saving of an entity
	 */
	/*protected*/getSaveUrl: function(){
		return this.getBaseAction()+'!save.action';
	},

	/**
	 * Parameters for saving an entity (extra parameters additionally to submit parameters)
	 */
	/*protected*/getSaveParams: function(recordData, action) {
		var params = null;
		if (CWHF.isNull(action)){
			action="add";
		}
		if (action==="add"){
			params=this.getAddParams();
		}
		if (action==="edit"){
			params=this.getEditParams(recordData);
		}
		if (action==="copy"){
			params=this.getCopyParams(recordData);
		}
		if (CWHF.isNull(params)) {
			//should not be null because dynamic parameters might be added right before submit
			//(in either submitHandler()  or preSubmitProcess() implementations)
			params = new Object();
		}
		return params;
	},
	
	/**
	 * Get extra parameters for grid load
	 * (like "defaultSettings" for "my" and "default" automail settings)
	 */
	/*protected*/getLoadGridParams:function() {
		return null;
	},
	
	/**
	 * Reload the grid
	 */
	reload:function(refreshParametersObject) {
		this.getView().getStore().load({
			scope:this,
			callback:function(){
				if (refreshParametersObject) {
					var rowToSelect = refreshParametersObject.rowToSelect;
					if (rowToSelect) {
						var row = this.getView().getStore().getById(rowToSelect);
						if (row) {
							var gridSelectionModel = this.getView().getSelectionModel();
							gridSelectionModel.select(row);
						}
					}
				}
			}
		})
	},
	
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
	
	/**
	 * The label for the save button
	 */
	/*protected*/getSaveLabel: function(operation) {
		return getText('common.btn.save');
	},
	
	/**
	 * The label for the cancel button
	 */
	/*protected*/getCancelLabel: function() {
		return getText('common.btn.cancel');
	},
	
	/**
	 * Handler for loading the form data
	 * Should be implemented only when panel is not form panel (for ex. a grid)
	 * and the load is made "manually"
	 */
	/*protected*/loadHandler:null,
	/**
	 * Handler for sending the form data to the sever
	 * Should be implemented only when panel is not form panel (for ex. a grid)
	 * and the save is made "manually"
	 */
	/*protected*/submitHandler:null,
	
	/**
	 * An array of of additional actions are needed beyond save and cancel
	 * The objects in the array should be composed in the same manner as the submit object for save
	 * the submit object contains:
	 * submitUrl: url the submit is sent to
	 * submitUrlParams: extra parameters for submit
	 * submitButtonText: the label for submit button (defaults to the label keyed by 'common.btn.save')
	 * refreshAfterSubmit: true if not specified, if false the refresh is not called in the submit handler
	 * closeAfterSubmit: true if not specified, if false the window is not closed in the submit handler
	 * submitHandler: an alternative submit handler used when isFormPanel is false (probably manual submit of a grid)
	 * 	refreshAfterSubmit and closeAfterSubmit has no relevance if submitHandler is specified
	 * submitAction: the action for submit ("add", "edit", "copy", "overwrite"). Not required
	 * if there are more actions which need a submit the submit object can be an array of such submit objects
	 *
	 */
	/*protected*/getAdditionalActions: function(recordData, submitParams, operation) {
		return null;
	},
	
	createEditForm:function(entityJS,type){
		return null;
	},
	
	/**
	 * The items in the popup window.
	 * Either this or getEditFormItems() should be implemented
	 * recordData: the selected entity data
	 * action: the submit action
	 */
	getEditPanelItems:function(recordData, action){
		return null;
	},
	
	/**
	 * Gather selected ID as a comma separated string
	 * selectedRecords - the selected record(s): simple or multiple selection:
	 * 	if simple the selection the record, if multiple an array of records
	 * getIDFormEntityFunction - function for getting the ID from an entity JSON
	 * extraConfig
	 */
	/*private*/getSelectedIDs: function(selectedRecords, extraConfig) {
		if (this.getView().allowMultipleSelections) {
			var idArray = [];
			Ext.Array.forEach(selectedRecords, function(record) {
				idArray.push(this.getRecordID(record.data, extraConfig));
			}, this);
			return idArray.join();
		} else {
			return this.getRecordID(selectedRecords.data, extraConfig);
		}
	}
});
