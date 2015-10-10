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

Ext.define('com.trackplus.admin.CrudBase',{
	extend:'Ext.Base',
	config: {
		/**
		 * Whether to request a confirmation when deleting an entity.
		 */
		confirmDeleteEntity:true,
		/**
		 * Whether to enable multiple selections for delete, drag and drop.
		 */
		allowMultipleSelections:false,
		/**
		 * Whether to allow de-selecting (only applies if allowMultipleSelections is false).
		 */
		allowDeselect: false,
		/**
		 * The width of the edit window.
		 * This is a mandatory field.
		 */
		editWidth:600,
		/**
		 * The height of the edit window
		 * This is a mandatory field.
		 */
		editHeight:400,
		/**
		 * With of replacement window
		 */
		replacementWidth: 500,
		/**
		 * Height of replacement window
		 */
		replacementHeight: 200,
		/**
		 * The base struts action name (without method name) in struts.xml
		 * Mandatory field
		 */
		baseAction:'',
		/**
		 * The selection model for grid
		 */
		gridSelectionModel: null
	},
	/**
	 * The base struts action name (without method name) in struts.xml
	 * Mandatory field
	 */
	baseAction:'',
	/**
	 * Whether to allow multiple selections (for delete, drag and drop)
	 */
	allowMultipleSelections:false,
	/**
	 * Whether to allow deselecting (applies if allowMultipleSelections is false)
	 */
	allowDeselect: false,
	/**
	 * The width of the edit window
	 * Mandatory field
	 */
	editWidth:600,
	/**
	 * The height of the edit window
	 * Mandatory field
	 */
	editHeight:400,
	/**
	 * Whether by deleting an entity should be asked for a confirmation, independently of any dependency
	 */
	confirmDeleteEntity:true,
	/**
	 * Whether to ask for extra confirmation if the entity to be deleted has dependencies
	 */
	confirmDeleteNotEmpty:false,
	/**
	 * Whether the deletion of a not empty entity was already confirmed
	 * This is also the name of the request parameter
	 * Makes sense only if confirmDeleteNotEmpty is true
	 */
	deleteConfirmed:false,
	/**
	 * Width of the replacement window
	 */
	replacementWidth: 500,
	/**
	 * Height of the replacement window
	 */
	replacementHeight: 200,
	/**
	 * The simple grid or grid part of tree-grid
	 */
	grid:null,
	/**
	 * The possible toolbar and context menu actions
	 */
	actions:null,
	/**
	 * The popup window for editing an entity
	 * (it is not used here directly but may be used in the overridden methods
	 * of the derived classes to access for ex. the toolbar of the window for disabling som buttons)
	 */
	win:null,
	/**
	 * The form for editing an entity. Created in the createEditForm() method
	 */
	formEdit:null,

	ERROR_CODE_NEED_REPLACE: 1,
	NOT_EMPTY_WARNING: 3,

	constructor: function(config) {
		var me = this;
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	/**
	 * The title for a failure message
	 */
	failureTitle:getText('common.err.failure'),


	/**********************************************label, tooltip, icon values***********************************************/
	/**
	 * The localized entity name based on the localization key: should be implemented
	 */
	/*protected abstract*/getEntityLabel: function(extraConfig) {
		return "";
		//return getText('...');
	},

	/**
	 * The iconCls for the add button
	 */
	/*protected*/getAddIconCls: function() {
		return 'add';
	},
	/**
	 * The key for "add" button text
	 */
	/*protected*/getAddButtonKey: function() {
		return 'common.btn.add';
	},
	/**
	 * The title for "add" popup and "add" action tooltip
	 */
	/*protected*/getAddTitleKey: function() {
		return 'common.lbl.add';
	},

	/**
	 * The iconCls for the edit button
	 */
	/*protected*/getEditIconCls: function() {
		return 'edit';
	},
	/**
	 * The key for "edit" button text
	 */
	/*protected*/getEditButtonKey: function() {
		return 'common.btn.edit';
	},
	/**
	 * The title for "edit" popup and "edit" action tooltip
	 */
	/*protected*/getEditTitleKey: function() {
		return 'common.lbl.edit';
	},

	/**
	 * The iconCls for the copy button
	 */
	/*protected*/getCopyIconCls: function() {
		return 'copy';
	},
	/**
	 * The key for "copy" button text
	 */
	/*protected*/getCopyButtonKey: function() {
		return 'common.btn.copy';
	},
	/**
	 * The title for "copy" popup and "copy" action tooltip
	 */
	/*protected*/getCopyTitleKey: function() {
		return 'common.lbl.copy';
	},

	/**
	 * The iconCls for the delete button
	 */
	/*protected*/getDeleteIconCls: function() {
		return 'delete';
	},
	/**
	 * The key for "delete" button text
	 */
	/*protected*/getDeleteButtonKey: function() {
		return 'common.btn.delete';
	},
	/**
	 * The title for "delete" popup and "delete" action tooltip
	 */
	/*protected*/getDeleteTitleKey: function() {
		return 'common.lbl.delete';
	},
	/**
	 * The delete confirmation text (if it will be asked for confirmation)
	 */
	/*protected*/getRemoveWarningKey: function() {
		return 'common.lbl.removeWarning';
	},

	/**
	 * Get the title for add/edit/copy/delete window used also as tooltip for toolbar buttons
	 */
	/*protected*/getTitle: function(tooltipKey, extraConfig) {
		if (tooltipKey!=null) {
			//add tooltipKey to extraConfig to identify the action
			//can be that the entity label depends also on the action to be executed
			//(for example add "status" but upload "icon")
			if (extraConfig==null) {
				extraConfig = new Object();
			}
			extraConfig['tooltipKey'] = tooltipKey;
		}
		return getText(tooltipKey, this.getEntityLabel(extraConfig));
	},

	/**********************************************action/context menu related methods***********************************************/
	/**
	 * Initialize the actions: should be overridden
	 */
	/*protected abstract*/initActions: function(){
		this.actions=[];
	},
	/**
	 * Get the toolbar actions.
	 * By default all actions, override if not all actions should appear in toolbar
	 */
	/*protected*/getToolbarActions: function(){
		return this.actions;
	},

	/**
	 * Get the actions available in context menu depending on the currently selected row
	 * By default all actions, override if not all actions should appear in the grid context menu
	 */
	/*protected*/getGridContextMenuActions: function(selectedRecord) {
		return this.actions;
	},

	/**
	 * Create a new Ext.Action
	 * labelKey the label key of the operation
	 * iconCls the icon class for the action
	 * handler the action handler
	 * disabled whether by creation it should be disabled
	 * tooltipKey makes sense only for toolbar buttons (not for context menu items)
	 * itemId for finding those actions which should have different labels depending on the context
	 * important only for tree with grid actions: for example edit action should be editLeaf entity or
	 * editFolder entity depending on the current selection
	 */
	/*public*/createAction: function(labelKey, iconCls, handler, disabled, tooltipKey, itemId, otherSettings) {
		var me = this;
		var actionConfig = {
				text: getText(labelKey),
				overflowText: getText(labelKey),
				iconCls: iconCls,
				scope: me,
				handler: handler
		};

		if (disabled!=null) {
			actionConfig.disabled = disabled;
		}
		if (tooltipKey!=null) {
			//tooltip is not an Ext.Action config field,
			//but it should be available when a button is created based on action
			//for simple grid actions the tooltip remains the same, so it is set and tooltipKey is not needed any more
			//actionConfig.tooltip = me.getTitle(tooltipKey);
			//but for tree with grid the tooltip might change based on the selected entity: "edit"/"delete" "folder"/"leaf"
			//that's why we store also the tooltipKey to be able to dynamically change the tooltips based on the current selection
			actionConfig.tooltipKey = tooltipKey;
			actionConfig.tooltip = this.getTitle(tooltipKey);
		}
		if (itemId!=null) {
			actionConfig.itemId = itemId;
		}
		if (otherSettings!=null) {
			//for example enableToggle should be set here
			for (propertyName in otherSettings) {
				actionConfig[propertyName] = otherSettings[propertyName];
			}
		}
		return Ext.create('Ext.Action', actionConfig);
	},

	/**
	 * Create a new Ext.Action action with localized text and tooltip
	 * label the localized label of the operation
	 * iconCls the icon class for the action
	 * handler the action handler
	 * tooltip makes sense only for toolbar buttons (not for context menu items)
	 * 		this is the localized tooltip not the tooltip key, because the tooltip for localized actions should not change
	 * 		depending on the context
	 * disabled whether by creation it should be disabled
	 */
	/*public*/createLocalizedAction: function(label, iconCls, handler, tooltip, disabled) {
		var me = this;
		var actionConfig = {
				text: label,
				overflowText: label,
				iconCls: iconCls,
				scope: me,
				handler: handler
		};
		if (disabled!=null) {
			actionConfig.disabled = disabled;
		}
		if (tooltip!=null) {
			//tooltip is not an Ext.Action config field,
			//but it should be available when a button is created based on action
			//for "add" action(s) the tooltip remains the same, tooltipKey is not needed any more
			actionConfig.tooltip = tooltip;
		}
		return Ext.create('Ext.Action', actionConfig);
	},

	/**
	 * Creates a context menu based on an array of actions
	 */
	/*public*/createContextMenu: function(record, actions) {
		var contextMenu = new Ext.menu.Menu({
			items: []
		});
		if (actions!=null) {
			for(var i=0;i<actions.length;i++){
				var action=actions[i];
				var contextMenuItemCfg = {
					text:action.getText(),
					iconCls:action.getIconCls()+"16",
					scope:action,
					tooltip:action.initialConfig.tooltip,
					handler:action.execute,
					itemId:action.itemId };
				contextMenu.add(contextMenuItemCfg);
				if (action.itemId!=null) {
					var contextMenuItem = contextMenu.getComponent(action.itemId);
					contextMenuItem.tooltipKey = action.initialConfig.tooltipKey;
				}
			}
		}
		return contextMenu;
	},

	/**********************************************reload methods***********************************************/
	/**
	 * Reload after a change operation
	 * In the implementations set to the corresponding com.trackplus.util.RefreshAfterSubmit method
	 */
	/*protected abstract*/reload:null,

	/**
	 * Parameters for reloading after a delete operation
	 * By delete the reload and select parameters are known before
	 */
	/*protected abstract*/getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return responseJson;
	},

	/**********************************************popup content and handler methods***********************************************/
	/**
	 * The form in the popup window.
	 * Either this or getEditPanelItems() should be implemented
	 * recordData: the selected entity data
	 * action: the submit action
	 */
	/*protected abstract*/createEditForm: function(recordData, action){
		return null;
	},

	/**
	 * The items in the popup window.
	 * Either this or getEditFormItems() should be implemented
	 * recordData: the selected entity data
	 * action: the submit action
	 */
	/*protected abstract*/getEditPanelItems:function(recordData, action){
		return null;
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


	/**********************************************popup URL and params***********************************************/

	/**
	 * Get the base action name from struts configuration
	 * Should be overridden
	 */
	/*public abstract*/getStrutsBaseAction: function(extraConfig) {
		return null;
	},

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

	/**
	 * Get the ID based from the recordData and extraConfig
	 * recordData: the data for a single record (for multiple selection this method is applied for each record separately)
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected abstract*/getRecordID: function(recordData, extraConfig) {
		return null;
	},

	/**
	 * Whether a new entity can be created
	 */
	/*protected*/canCreateEntity: function(){
		return true;
	},

	/**
	 * The struts action for delete/replace
	 */
	/*protected*/getDeleteUrlBase: function(extraConfig) {
		return this.getStrutsBaseAction(extraConfig);
	},

	/**
	 * Url for deleting an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getDeleteUrl: function(extraConfig){
		return this.getDeleteUrlBase(extraConfig)+'!delete.action';
	},

	/**
	 * Parameter name for the submitted id(s) by delete
	 * If allowMultipleSelections==false then the same this.entityID (simple grid) or
	 * 	node (tree with grid) can be used on the server side for both delete and edit submits
	 * If allowMultipleSelections==true this should return another name
	 * 	the submitted value will be stored on the server typically
	 * 	in an Integer[] (simple grid) or String[] (tree with grid) variable
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 * Must be overridden if allowMultipleSelections==true
	 */
	/*protected abstract*/getDeleteParamName: function(extraConfig) {
		return null;
	},

	/**
	 * Parameters for deleting entity
	 * recordData: the selected entity data
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getDeleteParams: function(selectedRecords, extraConfig) {
		var params=new Object();
		var selectionParam = this.getSelectionParam(selectedRecords, extraConfig);
		params[this.getDeleteParamName(extraConfig)]=selectionParam;
		if (this.confirmDeleteNotEmpty) {
			params['deleteConfirmed'] = this.deleteConfirmed;
		}
		return params;
	},

	/**
	 * Url for preparing the replacement data rendering
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getRenderReplaceUrl: function(extraConfig){
		return this.getDeleteUrlBase(extraConfig)+'!renderReplace.action';
	},
	/**
	 * Parameters for preparing the replacement data
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getRenderReplaceParams: function(selectedRecords, extraConfig) {
		return this.getDeleteParams(selectedRecords, extraConfig);
	},
	/**
	 * Url for replacing and deleting of an entity
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getReplaceAndDeleteUrl: function(extraConfig){
		return this.getDeleteUrlBase(extraConfig)+'!replaceAndDelete.action';
	},
	/**
	 * Parameters for replacing and deleting of an entity
	 * Even if there is more than one entity selected for delete
	 * this method is called for each selected entity separately
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 */
	/*protected*/getReplaceAndDeleteParams: function(selectedRecords, extraConfig) {
		return this.getDeleteParams(selectedRecords, extraConfig);
	},

	/**
	 * Function to be called before submit to add dynamic parameters
	 * to existing submitUrlParams based on the panel's content*/
	/*protected*/preSubmitProcess: /* function(submitUrlParams, panel) {
	 	//add parameters to submitUrlParams based on panel
		return submitUrlParams;
	},*/null,

	/**
	 * The label for the save button
	 */
	/*protected*/getSaveLabel: function(operation) {
		return getText('common.btn.save');
	},
	/**
	 * The label for the delete button
	 */
	/*protected*/getDeleteLabel: function() {
		return getText('common.btn.delete');
	},
	/**
	 * The label for the cancel button
	 */
	/*protected*/getCancelLabel: function() {
		return getText('common.btn.cancel');
	},


	/************************************delete related fields ***************************************/

	/**
	 * The localized entity name to be deleted
	 * Should be implemented only if differs from entity name
	 * (for example in field configuration: entity label is "field configuration" but delete label is "field")
	 */
	/*protected*/getDeleteEntityLabel:function(extraConfig) {
		return this.getEntityLabel(extraConfig);
	},

	/**
	 * The a message patameterized with the deleteEntityLabel
	 */
	getDeleteEntityMessage: function(titleKey, extraConfig) {
		return getText(titleKey, this.getDeleteEntityLabel(extraConfig));
	},

	/**
	 * Handler for deleting the selected data.
	 * It can be single or multiple selection
	 * title:
	 */
	/*private*/deleteHandler: function(selectedRecords, extraConfig){
		var me=this;
		if (selectedRecords==null) {
			return true;
		}
		this.deleteConfirmed = false;
		if (me.confirmDeleteEntity) {
			Ext.MessageBox.confirm(me.getDeleteEntityMessage(me.getDeleteTitleKey(), extraConfig),
				me.getDeleteEntityMessage(me.getRemoveWarningKey(), extraConfig),
					function(btn){
						if (btn=="no") {
							return false;
						} else {
							me.deleteSelected.call(me, selectedRecords, extraConfig);
						}
					});
		} else {
			me.deleteSelected(selectedRecords, extraConfig);
		}
	},

	/**
	 * Delete handler
	 * selectedRecords the selected data (node(s)/row(s)) to be deleted
	 * extraConfig implementation specific extra configuration object
	 */
	/*private*/deleteSelected: function(selectedRecords, extraConfig) {
		var me=this;
		var deleteParams = me.getDeleteParams(selectedRecords, extraConfig);
		Ext.Ajax.request({
			url: me.getDeleteUrl(extraConfig),
			params: deleteParams,
			disableCaching:true,
			scope: me,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success==true) {
					//delete done (no replacement were needed)
					me.reload.call(me, me.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson));
				} else {
					var errorCode = responseJson.errorCode;
					if (errorCode!=null) {
						if (errorCode==me.ERROR_CODE_NEED_REPLACE) {
							//render dialog for selecting the replacement
							var windowItems = me.getReplacementItems(responseJson, selectedRecords, extraConfig);
							var title = me.getTitle(me.getDeleteTitleKey(), extraConfig);
							var load = {loadUrl:me.getRenderReplaceUrl(extraConfig),
									loadUrlParams:me.getRenderReplaceParams(selectedRecords, extraConfig)};
							var submit = {	submitUrl:me.getReplaceAndDeleteUrl(extraConfig),
											submitUrlParams:me.getReplaceAndDeleteParams(selectedRecords, extraConfig),
											submitButtonText:me.getDeleteLabel(),
											//deleting more users can be a lengthy operation
	                                        timeout:300,
											refreshAfterSubmitHandler:me.reload,
											refreshParametersBeforeSubmit:me.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson)
										};
							var windowConfig = Ext.create('com.trackplus.util.WindowConfig',
									{postDataProcess:me.replaceOptionPostDataProcess, extraConfig:extraConfig});
							windowConfig.showWindow(me, title, me.replacementWidth, me.replacementHeight, load, submit, /*refresh,*/ windowItems);
						} else {
							//the entity to be deleted is not empty an extra confirmation box is shown
							if (errorCode==me.NOT_EMPTY_WARNING) {
								var errorMessage = responseJson.errorMessage;
								Ext.MessageBox.confirm(me.getDeleteEntityMessage(me.getDeleteTitleKey(), extraConfig),
									errorMessage,
									function(btn){
										if (btn=="no") {
											return false;
										} else {
											this.deleteConfirmed = true;
											me.deleteSelected.call(me, selectedRecords, extraConfig);
										}
									}, this);
							} else {
								Ext.MessageBox.alert(this.failureTitle, responseJson.errorMessage);
							}
						}
					} else {
						//no right to delete (for ex. with fake URL-Params)
						//var errorMessage=responseJson.errorMessage;
						//me.errorHandlerDelete(selectedRecords, errorCode, errorMessage);
						me.errorHandlerDelete(responseJson, selectedRecords);
					}
				}
			},
			failure: function(response){
				com.trackplus.util.requestFailureHandler(response);
			},
			method:"POST"
		});
	},

	/**
	 * Gather selected ID as a comma separated string
	 * selectedRecords - the selected record(s): simple or multiple selection:
	 * 	if simple the selection the record, if multiple an array of records
	 * getIDFormEntityFunction - function for getting the ID from an entity JSON
	 * extraConfig
	 */
	/*private*/getSelectionParam: function(selectedRecords, extraConfig) {
		if (this.allowMultipleSelections) {
			var idArray = [];
			Ext.Array.forEach(selectedRecords, function(record) {
				idArray.push(this.getRecordID(record.data, extraConfig));
			}, this);
			return idArray.join();
		} else {
			return this.getRecordID(selectedRecords.data, extraConfig);
		}
	},

	/**
	 * Error handler for delete. In the overridden versions depending on error code
	 * the deleteSelected() might be called again with extra/modified submit parameters
	 */
	/*protected*/errorHandlerDelete: function(result, selectedRecords){
		com.trackplus.util.showError(result);
	},

	/**
	 * The replacement items for the deleted entity
	 * (The replacement panel will be created with on this items)
	 */
	/*protected*/getReplacementItems: function(responseJson, selectedRecords, extraConfig) {
		return [{xtype : 'label',
				itemId: 'replacementWarning'},
				CWHF.createCombo('Replacement',
						'replacementID',
						{labelWidth:200,
						allowBlank:false,
						blankText: getText('common.err.replacementRequired',
								this.getEntityLabel(extraConfig))})];
	},

	/**
	 * Load the data in the replacement panel when it arrives from server
	 * the complete replacementWarning could be composed on the server
	 * If not, it will be composed on the client, but at least
	 * the label of the entity to be deleted should be specified
	 */
	/*private*/replaceOptionPostDataProcess: function(data, panel, extraConfig) {
		var replacementWarning = panel.getComponent('replacementWarning');
		var replacementWarningText = data['replacementWarning'];
		if (replacementWarningText==null) {
			var label = data['label'];
			replacementWarningText = getText("common.lbl.replacementWarning", this.getEntityLabel(extraConfig), label);
			replacementWarningText = replacementWarningText + getText("common.lbl.cancelDeleteAlert");
		}
		replacementWarning.setText(replacementWarningText, false);
		var replacementList = panel.getComponent('replacementID');
		this.loadReplacementOptionData(replacementList, data);
		var replacementListLabel = data.replacementListLabel;
		if (replacementListLabel==null) {
			replacementListLabel =  this.getTitle('common.lbl.replacement', extraConfig);
		}
		replacementList.labelEl.dom.innerHTML = replacementListLabel;
	},

	/**
	 * Load the data source and value for the replacement options combo
	 * Override this for different tree based pickers
	 */
	/*protected*/loadReplacementOptionData: function(replacementControl, data) {
		replacementControl.store.loadData(data["replacementList"]);
		replacementControl.setValue(null);
	},

	/************************************selection utility methods***************************************/

	/**
	 * Get the configuration for selection model
	 */
	/*protected*/getGridSelectionModel: function() {
		var selectionModelConfig = new Object;
		if (this.allowMultipleSelections) {
			selectionModelConfig.mode="MULTI";
		} else {
			selectionModelConfig.mode="SINGLE";
			if (this.allowDeselect) {
				selectionModelConfig.allowDeselect=this.allowDeselect;
			}
		}
		if (this.gridSelectionModel==null) {
			return Ext.create("Ext.selection.RowModel", selectionModelConfig);
		} else {
			return this.gridSelectionModel;
		}
	},

	//grid specific selection utility methods
	/**
	 * The the last selected grid row (only one row)
	 */
	/*public*/getLastSelectedGridRow: function() {
		if (this.grid!=null) {
			return this.grid.getSelectionModel().getLastSelected();
		}
		return null;
	},
	/**
	 * The the selected grid rows (possibly more than one)
	 */
	/*public*/getGridSelection: function() {
		if (this.grid!=null) {
			return this.grid.getSelectionModel().getSelection();
		}
		return null;
	},
	/**
	 * Select and returns a record in the grid
	 * (used by activating the context menu)
	 */
	/*public*/selectGridRow: function(record) {
		//false to not retain existing selections
		if (this.grid!=null) {
			this.grid.getSelectionModel().select(record, false);
		}
		return record;
	},

	//general selection utility methods
	/**
	 * Gets the last selected data (either tree node or grid row)
	 */
	/*public*/getLastSelected: function() {
		return this.getLastSelectedGridRow();
	},

	/**
	 * Get the selection(s) (either in tree or in grid )
	 */
	/*public*/getSelection: function() {
		return this.getGridSelection();
	},

	/**
	 * Gets the selected data
	 * it can be simple or multiple selection
	 */
	/*public*/getSelectedRecords: function() {
		var me=this;
		if (me.allowMultipleSelections) {
			return me.getSelection();
		} else {
			return me.getLastSelected();
		}
	},

	/**
	 * Whether the selection is simple or multiple
	 */
	/*public*/selectionIsSimple: function() {
		if (this.allowMultipleSelections) {
			var selectedRecords = this.getSelection();
			return selectedRecords!=null && selectedRecords.length==1;
		} else {
			var selectedRecord = this.getLastSelected();
			return selectedRecord!=null;
		}
	},

	/**
	 * Get the data of the single selected record
	 * For no selection or multiple selection return null
	 * param: typically fromTree
	 */
	/*private*/getSingleSelectedRecordData: function(param) {
		var selectedRecord = this.getSingleSelectedRecord(param);
		if (selectedRecord!=null) {
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
		if (selectedRecords!=null) {
			if (this.allowMultipleSelections) {
				if (selectedRecords!=null && selectedRecords.length==1) {
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
	},

	/**
	 * Select and return a record by activating the context menu
	 */
	/*public*/selectRecord: function(record) {
		return this.selectGridRow(record);
	}
});
