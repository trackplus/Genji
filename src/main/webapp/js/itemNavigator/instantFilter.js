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

Ext.define("com.trackplus.itemNavigator.InstantFilter",{
	extend:"Ext.util.Observable",
	config: {
		filterName: null,
		filterType: null,
		filterID: null,
		queryContextID: null,
		viewID: null,
		fromSession:false
	},
	filterPanel: null,
	//add new selection field: instant should be true to not add $Paramater
	instant: true,
	indexMax: 0,
	constructor : function(cfg) {
		var config = cfg || {};
		this.initialConfig = config;
		this.events=[];
		//this.addEvents("applyFilter");
		//this.addEvents("cleanFilter");
		this.listeners = config.listeners;
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent(arguments);
	},
	/**
	 * Creates the filter panel
	 */
	createPanel: function() {
		var me=this;
		me.filterPanel=Ext.create("Ext.form.Panel",{
			border:false,
			bodyBorder:false,
			autoScroll:true,
			region:"center",
			dockedItems: me.getFilterToolbar(),
			style:{
				borderBottom:"1px solid #d0d0d0"
			}
		});
		return me.filterPanel;
	},

	/**
	 * Loads the filter panel with filter expressions
	 */
	loadFilter: function() {
		var me=this;
		var	 params = {};
		if (CWHF.isNull(this.filterID) || CWHF.isNull(this.filterType)) {
			params.add = true;
		} else {
			params.filterID = this.filterID;
			params.filterType = this.filterType;
			params.queryContextID = this.queryContextID;
		}
		params.fromSession=this.fromSession;
		me.filterPanel.setLoading(true);
		me.filterPanel.getForm().load({
			url : "filterConfig!edit.action",
			params: params,
			scope: me,
			success: function(form, action) {
				me.filterPanel.setLoading(false);
				var data = action.result.data;
				data.selectColumnCount = 4;
				var modifiable = data["modifiable"];
				//force the filter fields to be modifiable (save as is always possible)
				data["modifiable"] = true;
				var toolbars = me.filterPanel.getDockedItems('toolbar[dock="top"]');
				if (toolbars) {
					//if not modifiable disbale the save button
					toolbars[0].getComponent(2).setDisabled(!modifiable);
				}
				me.issueFilter = true;//to avoid rendering new/old value (used only for notification filter)
				com.trackplus.admin.Filter.postLoadProcessTreeFilter.call(me, data, me.filterPanel);
			},
			failure: function(form, action) {
				me.filterPanel.setLoading(false);
				/*Ext.MessageBox.show({
					title: this.failureTitle,
					msg: action.response.responseText,
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.ERROR
				})*/
			}
		});
	},

	/**
	 * Initialize the filter toolbar
	 */
	getFilterToolbar: function() {
		return [{
			xtype: 'toolbar',
			dock: 'top',
			margin:'4 0 0 0',
			items: [this.getApplyFilterAction(), this.getCleanFilterAction(), this.getSaveFilterAction(), this.getSaveFilterAsAction()]
		}];
	},

	/**
	 * Creates the apply filter action
	 */
	getApplyFilterAction: function() {
		var actionConfig = {
				text: getText("common.btn.apply"),
				overflowText: getText("common.btn.applyFilter"),
				tooltip:getText("itemov.btn.applyFilter.tt"),
				iconCls: "filterExec",
				scope: this,
				handler: this.onApply
		};
		return Ext.create('Ext.Action', actionConfig);
	},

	/**
	 * Creates the clean filter action
	 */
	getCleanFilterAction: function() {
		var actionConfig = {
				text: getText("common.btn.reset"),
				overflowText: getText("common.btn.reset"),
				tooltip:getText("itemov.btn.clearFilter.tt"),
				iconCls: "clear16",
				scope: this,
				handler: this.onClean
		};
		return Ext.create('Ext.Action', actionConfig);
	},

	/**
	 * Creates the save action
	 */
	getSaveFilterAction: function() {
		var actionConfig = {
				text: getText("common.btn.save"),
				overflowText: getText("common.btn.save"),
				tooltip:getText("itemov.btn.saveFilter.tt"),
				iconCls: "save",
				disabled: true,
				scope: this,
				handler: this.onSave
		};
		return Ext.create('Ext.Action', actionConfig);
	},

	/**
	 * Creates the save as action
	 */
	getSaveFilterAsAction: function() {
		var actionConfig = {
				text: getText("common.btn.saveAs"),
				overflowText: getText("common.btn.saveAs"),
				tooltip:getText("itemov.btn.saveAsFilter.tt"),
				iconCls: "save",
				scope: this,
				handler: this.onSaveAs
		};
		return Ext.create('Ext.Action', actionConfig);
	},

	/**
	 * Applies the filter
	 */
	onApply: function() {
		var me=this;
		submitUrlParams = {instant:true, ajax:true, filterID:me.filterID, filterType:me.filterType};
		com.trackplus.admin.Filter.preSubmitProcessIssueFilter(submitUrlParams, me.filterPanel);
		me.filterPanel.setLoading(true);
		me.filterPanel.getForm().submit({
			url: "savedFilterExecute!applyInstant.action",
			params: submitUrlParams,
			method: "POST",
			scope: me,
			success: function(form, action) {
				me.filterPanel.setLoading(false);
				var result = action.result;
				if (result) {
					if (result.success) {
						me.fireEvent.call(me,"applyFilter",{
							filterType: me.filterType,
							filterID: me.filterID,
							queryContextID: me.queryContextID
						});
					} else {
						com.trackplus.util.showError(result);
					}
				}
			},
			failure: function(form, action) {
				me.filterPanel.setLoading(false);
				result = action.result;
				if (result) {
					var errorMessage = result.errorMessage;
					if (errorMessage) {
						//only error message, no errorCode
						com.trackplus.util.showError(result);
					} else {
						com.trackplus.util.submitFailureHandler(form, action);
					}
				}
			}
		});
	},

	/**
	 * Cleans the filter conditions
	 */
	onClean: function() {
		var me=this;
		var params = {clearFilter:true};
		if (CWHF.isNull(this.filterID) || CWHF.isNull(this.filterType)) {
			params.instant = true;
		} else {
			params.filterID = this.filterID;
			params.filterType = this.filterType;
			params.queryContextID = this.queryContextID;
		}
		me.filterPanel.setLoading(true);
		me.filterPanel.getForm().load({
			url : "filterConfig!edit.action",
			params: params,
			scope: me,
			success: function(form, action) {
				me.filterPanel.setLoading(false);
				var data = action.result.data;
				data.selectColumnCount = 7;
				//force to be modifiable
				data["modifiable"] = true;
				com.trackplus.admin.Filter.clearTreeFilter.call(me, me.filterPanel, data);
				me.fireEvent.call(me,"clearFilter",{
					filterType: me.filterType,
					filterID: me.filterID,
					queryContextID: me.queryContextID
				});
			},
			failure: function(form, action) {
				me.filterPanel.setLoading(false);
			}
		});

	},

	onSave: function() {
		var me=this;
		var params = {};
		if (CWHF.isNull(this.filterID) || CWHF.isNull(this.filterType)) {
			//params.instant = true;
			params.add = true;
		} else {
			params.filterID = this.filterID;
			params.filterType = this.filterType;
			params.queryContextID = this.queryContextID;
		}
		com.trackplus.admin.Filter.preSubmitProcessIssueFilter(params, me.filterPanel);
		me.filterPanel.setLoading(true);
		me.filterPanel.getForm().submit({
			url: "filterConfig!save.action",
			params: params,
			method: "POST",
			scope: me,
			success: function(form, action) {
				me.filterPanel.setLoading(false);
				var result = action.result;
				if (result) {
					if (result.success) {
						CWHF.showMsgInfo(getText("admin.customize.queryFilter.successSave"));
						//TODO reload content
					} else {
						CWHF.showMsgError(getText("admin.customize.queryFilter.errorSave"));
					}
				}
			},
			failure: function(form, action) {
				me.filterPanel.setLoading(false);
				result = action.result;
				if (result) {
					var errorMessage = result.errorMessage;
					if (errorMessage) {
						//only error message, no errorCode
						com.trackplus.util.showError(result);
					} else {
						com.trackplus.util.submitFailureHandler(form, action);
					}
				}
			}
		});
	},



	onSaveAs: function() {
		var nameAndPathPicker=Ext.create("com.trackplus.util.NameAndPathPicker",{
			title:getText("common.btn.saveAs"),
			loadUrl: "categoryPathPicker.action",
			loadParams:  {categoryType:"issueFilter", add: true},
			//pathTree: data,
			handler:this.saveWithNameAndPath,
			//validateHandler: this.validateHandler,
			scope:this
		});
		nameAndPathPicker.showDialog();
	},

	/*validateHandler: function(submit) {
	var me=this;
	var filled = me.formEdit.isValid();
	if (!filled) {
		return false;
	}
	var pathNodeID = me.formEdit.getComponent("path").getValue();
	var label = me.formEdit.getComponent("name").getValue();
	Ext.Ajax.request({
		url: 'filterConfig!isValidLabel.action',
		params: {node:pathNodeID, label:label},
		scope: this,
		disableCaching:true,
		success: function(response){
			this.handler(label, pathNodeID);
		},
		failure: function(result){
			Ext.MessageBox.alert(this.failureTitle, result.responseText)
		},
		method:"POST"
	})
},*/

	saveWithNameAndPath:function(window, name, parentID, includeInMenu) {
		var me = this;
		var submitParams = {node:parentID, label:name, includeInMenu:includeInMenu, add:true, viewID: me.viewID};
		me.filterPanel.getForm().submit({
			url: "filterConfig!save.action",
			params: submitParams,
			method: "POST",
			scope: this,
			success: function(form, action) {
				me.filterPanel.setLoading(false);
				var result = action.result;
				if (result) {
					if (result.success) {
						CWHF.showMsgInfo(getText("admin.customize.queryFilter.successSave"));
						window.close();
						//TODO reload content
					} else {
						CWHF.showMsgError(getText("admin.customize.queryFilter.errorSave"));
					}
				}
			},
			failure: function(form, action) {
				me.filterPanel.setLoading(false);
				result = action.result;
				if (result) {
					var errorMessage = result.errorMessage;
					if (errorMessage) {
						//only error message, no errorCode
						com.trackplus.util.showError(result);
					} else {
						com.trackplus.util.submitFailureHandler(form, action);
					}
				}
			}
		});
	}
});
