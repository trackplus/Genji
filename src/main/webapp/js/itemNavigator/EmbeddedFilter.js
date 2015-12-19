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

Ext.define("com.trackplus.itemNavigator.EmbeddedFilter",{
	extend: "Ext.form.Panel",
	mixins: {
		observableMethods: "Ext.util.Observable",
		actionsBase: "com.trackplus.admin.ActionBase"
	},
	xtype: "embeddedFilter",
    controller: "embeddedFilter",
    
	config: {
		filterName: null,
		filterType: null,
		filterID: null,
		queryContextID: null,
		viewID: null,
		fromSession:false
	},
	//filterPanel: null,
	//add new selection field: instant should be true to not add $Paramater
	instant: true,
	indexMax: 0,
	issueFilter: true,
	/*constructor : function(cfg) {
		var config = cfg || {};
		this.initialConfig = config;
		this.events=[];
		//this.addEvents("applyFilter");
		//this.addEvents("cleanFilter");
		this.listeners = config.listeners;
		Ext.apply(this, config);
		this.initConfig(config);
		this.callParent(arguments);
	},*/
	
	initComponent: function() {
		this.initActions(),
		this.border=false,
		this.bodyBorder=false,
		this.autoScroll=true,
		this.region="center",
		this.autoScroll=true,
		this.style={
			borderBottom:"1px solid #d0d0d0"
		}
		this.dockedItems = this.getToolbar();
		this.callParent();
	},
	
	initActions: function() {
		this.actionApply = CWHF.createAction("common.btn.apply", "filterInst", "onApply",
				{tooltip:getText("itemov.btn.applyFilter.tt")});
		this.actionReset = CWHF.createAction(this.getResetButtonKey(), "clear", "onClean",
				{tooltip:"itemov.btn.clearFilter.tt"});
        this.actionSave = CWHF.createAction(this.getSaveButtonKey(), this.getSaveIconCls(), "onSave",
        		{tooltip:"itemov.btn.saveFilter.tt", disabled:true});
        this.actionSaveAs = CWHF.createAction("common.btn.saveAs", this.getSaveIconCls(), "onSaveAs",
        		{tooltip:"itemov.btn.saveAsFilter.tt"});
		this.actions = [this.actionApply, this.actionReset, this.actionSave, this.actionSaveAs];
    },
    
	/**
	 * Creates the filter panel
	 */
	/*createPanel: function() {
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
	},*/

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
		this.setLoading(true);
		this.getForm().load({
			url : "filterConfig!edit.action",
			params: params,
			scope: me,
			success: function(form, action) {
				this.setLoading(false);
				var data = action.result.data;
				data.selectColumnCount = 4;
				var modifiable = data["modifiable"];
				//force the filter fields to be modifiable (save as is always possible)
				data["modifiable"] = true;
				var toolbars = this.getDockedItems('toolbar[dock="top"]');
				if (toolbars) {
					//if not modifiable disbale the save button
					toolbars[0].getComponent(2).setDisabled(!modifiable);
				}
				me.issueFilter = true;//to avoid rendering new/old value (used only for notification filter)
				com.trackplus.admin.Filter.postLoadProcessTreeFilter.call(this, data, this);
			},
			failure: function(form, action) {
				this.setLoading(false);
			}
		});
	},

	/**
	 * Initialize the filter toolbar
	 */
	/*getFilterToolbar: function() {
		return [{
			xtype: 'toolbar',
			dock: 'top',
			margin:'4 0 0 0',
			items: [this.getApplyFilterAction(), this.getCleanFilterAction(), this.getSaveFilterAction(), this.getSaveFilterAsAction()]
		}];
	},*/

	/**
	 * Creates the apply filter action
	 */
	/*getApplyFilterAction: function() {
		var actionConfig = {
				text: getText("common.btn.apply"),
				overflowText: getText("common.btn.applyFilter"),
				tooltip:getText("itemov.btn.applyFilter.tt"),
				iconCls: "filterExec",
				scope: this,
				handler: this.onApply
		};
		return Ext.create('Ext.Action', actionConfig);
	},*/

	/**
	 * Creates the clean filter action
	 */
	/*getCleanFilterAction: function() {
		var actionConfig = {
				text: getText("common.btn.reset"),
				overflowText: getText("common.btn.reset"),
				tooltip:getText("itemov.btn.clearFilter.tt"),
				iconCls: "clear16",
				scope: this,
				handler: this.onClean
		};
		return Ext.create('Ext.Action', actionConfig);
	},*/

	/**
	 * Creates the save action
	 */
	/*getSaveFilterAction: function() {
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
	},*/

	/**
	 * Creates the save as action
	 */
	/*getSaveFilterAsAction: function() {
		var actionConfig = {
				text: getText("common.btn.saveAs"),
				overflowText: getText("common.btn.saveAs"),
				tooltip:getText("itemov.btn.saveAsFilter.tt"),
				iconCls: "save",
				scope: this,
				handler: this.onSaveAs
		};
		return Ext.create('Ext.Action', actionConfig);
	},*/

	
});
