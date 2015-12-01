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

Ext.define('com.trackplus.admin.DeleteController',{
	extend:'Ext.Base',
	config: {},
	
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
	
	
	ERROR_CODE_NEED_REPLACE: 1,
	NOT_EMPTY_WARNING: 3,

	constructor: function(config) {
		var me = this;
		var config = config || {};
		this.initConfig(config);
	},
	/**
	 * The title for a failure message
	 */
	failureTitle:getText('common.err.failure'),

	/**
	 * Creates a context menu based on an array of actions
	 */
	/*public*/createContextMenu: function(record, actions) {
		var contextMenu = new Ext.menu.Menu({
			items: []
		});
		if (actions) {
			for(var i=0;i<actions.length;i++){
				var action=actions[i];
				if (action) {
					var contextMenuItemCfg = {
						text:action.getText(),
						iconCls:action.getIconCls()+"16",
						//scope is the viewController
						scope:this,
						//Ext.Action does not have tooltip field: take from initialConfig
						tooltip:action.initialConfig.tooltip,
						//the handler is not the name as in view but the function object from the viewController
						handler: this[action.initialConfig.handler],
						itemId: action.itemId};
					contextMenu.add(contextMenuItemCfg);
					if (action.itemId) {
						var contextMenuItem = contextMenu.getComponent(action.itemId);
						contextMenuItem.tooltipKey = action.initialConfig.tooltipKey;
					}
				}
			}
		}
		return contextMenu;
	},

	/**********************************************label, tooltip, icon values***********************************************/
	
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
	
	/*protected*/getReplacementKey: function() {
		return 'common.lbl.replacement';
	},
	
	/**
	 * Parameters for reloading after a delete operation
	 * By delete the reload and select parameters are known before
	 */
	/*protected abstract*/getReloadParamsAfterDelete: function(selectedRecords, extraConfig, responseJson) {
		return responseJson;
	},

	/**
	 * The struts action for delete/replace
	 */
	/*protected*/getDeleteUrlBase: function(extraConfig) {
		return this.getNodeBaseAction(extraConfig);
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
	 * If allowMultipleSelections===false then the same this.entityID (simple grid) or
	 * 	node (tree with grid) can be used on the server side for both delete and edit submits
	 * If allowMultipleSelections===true this should return another name
	 * 	the submitted value will be stored on the server typically
	 * 	in an Integer[] (simple grid) or String[] (tree with grid) variable
	 * extraConfig: for simple grid nothing, for tree with grid {fromTree:fromTree, isLeaf:isLeaf}
	 * Must be overridden if allowMultipleSelections===true
	 */
	/*protected abstract*/getDeleteParamName: function(extraConfig) {
		return this.entityID;
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
		var selectionParam = this.getSelectedIDs(selectedRecords, extraConfig);
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
	 * The label for the delete button
	 */
	/*protected*/getDeleteLabel: function() {
		return getText('common.btn.delete');
	},

	/************************************delete related fields ***************************************/

	/**
	 * The localized entity name to be deleted
	 * Should be implemented only if differs from entity name
	 * (for example in field configuration: entity label is "field configuration" but delete label is "field")
	 */
	/*protected*//*getDeleteEntityLabel:function(extraConfig) {
		return this.getEntityLabel(extraConfig);
	},*/

	/**
	 * The a message patameterized with the deleteEntityLabel
	 */
	getMessage: function(titleKey, entityLabel) {
		return getText(titleKey, entityLabel);
	},

	/**
	 * Handler for deleting the selected data.
	 * It can be single or multiple selection
	 * title:
	 */
	/*private*/deleteHandler: function(entityLabel, selectedRecords, extraConfig){
		var me=this;
		if (CWHF.isNull(selectedRecords)) {
			return true;
		}
		this.deleteConfirmed = false;
		if (me.confirmDeleteEntity) {
			Ext.MessageBox.confirm(me.getMessage(me.getDeleteTitleKey(), entityLabel),
				me.getMessage(me.getRemoveWarningKey(), entityLabel),
					function(btn){
						if (btn==="no") {
							return false;
						} else {
							me.deleteSelected.call(me, entityLabel, selectedRecords, extraConfig);
						}
					});
		} else {
			me.deleteSelected(entityLabel, selectedRecords, extraConfig);
		}
	},

	/**
	 * Delete handler
	 * selectedRecords the selected data (node(s)/row(s)) to be deleted
	 * extraConfig implementation specific extra configuration object
	 */
	/*private*/deleteSelected: function(entityLabel, selectedRecords, extraConfig) {
		var me=this;
		var deleteParams = me.getDeleteParams(selectedRecords, extraConfig);
		Ext.Ajax.request({
			url: me.getDeleteUrl(extraConfig),
			params: deleteParams,
			disableCaching:true,
			scope: me,
			success: function(response){
				var responseJson = Ext.decode(response.responseText);
				if (responseJson.success===true) {
					//delete done (no replacement were needed)
					me.reload.call(me, me.getReloadParamsAfterDelete(selectedRecords, extraConfig, responseJson));
				} else {
					var errorCode = responseJson.errorCode;
					if (errorCode) {
						if (errorCode===me.ERROR_CODE_NEED_REPLACE) {
							//render dialog for selecting the replacement
							var windowItems = me.getReplacementItems(entityLabel, responseJson, selectedRecords);
							var title = me.getMessage(me.getDeleteTitleKey(), entityLabel);
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
									{postDataProcess:me.replaceOptionPostDataProcess, extraConfig:{entityLabel:entityLabel}});
							windowConfig.showWindow(me, title, me.replacementWidth, me.replacementHeight, load, submit, /*refresh,*/ windowItems);
						} else {
							//the entity to be deleted is not empty an extra confirmation box is shown
							if (errorCode===me.NOT_EMPTY_WARNING) {
								var errorMessage = responseJson.errorMessage;
								Ext.MessageBox.confirm(me.getMessage(me.getDeleteTitleKey(), entityLabel),
									errorMessage,
									function(btn){
										if (btn==="no") {
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
	/*protected*/getReplacementItems: function(entityLabel, responseJson, selectedRecords, extraConfig) {
		return [{xtype : 'label',
				itemId: 'replacementWarning'},
				CWHF.createCombo('Replacement',
						'replacementID',
						{itemId:"replacementID",
						labelWidth:200,
						allowBlank:false,
						blankText: this.getMessage('common.err.replacementRequired',
								entityLabel)})];
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
		if (CWHF.isNull(replacementWarningText)) {
			var label = data['label'];
			replacementWarningText = getText("common.lbl.replacementWarning", extraConfig.entityLabel, label);
			replacementWarningText = replacementWarningText + getText("common.lbl.cancelDeleteAlert");
		}
		replacementWarning.setText(replacementWarningText, false);
		var replacementList = panel.getComponent('replacementID');
		this.loadReplacementOptionData(replacementList, data);
		var replacementListLabel = data.replacementListLabel;
		if (CWHF.isNull(replacementListLabel)) {
			replacementListLabel = this.getMessage('common.lbl.replacement', extraConfig.entityLabel);
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
	}
});
