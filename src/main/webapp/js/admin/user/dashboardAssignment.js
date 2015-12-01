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
 * Class for  dashboard assigment
 */
Ext.define('com.trackplus.admin.user.DashboardAssignment',{
	extend:'Ext.Base',
	baseAction:'dashboardAssignment',
	entityName:getText('menu.admin.users.cockpitDefault'),

	constructor: function(config) {
		var config = config || {};
		this.initConfig(config);
		this.myInit();
	},

	myInit:function(){
		var me=this;
		me.initActions();
		me.initGrid();
	},

	createCenterPanel: function() {
		var me=this;
		return me.grid;

	},

	getGridFields: function() {
		return [
			{name: 'id',type:'int'},
			{name: 'name',type:'string'},
			{name: 'description', type: 'string'},
			{name: 'owner',type:'string'}
		]
	},

	getGridColumns: function() {
		return [{
				text: getText('common.lbl.name'), width:300,
				dataIndex: 'name',sortable:true,
				filter: {
		            type: "string"
		        }
			},{
				text: getText('common.lbl.description'),flex:1,
				dataIndex: 'description',sortable:true,
				filter: {
		            type: "string"
		        }
			},{
				text: getText('admin.customize.form.config.owner'), width:200,
				dataIndex: 'owner',sortable:true,
				filter: {
		            type: "string"
		        }
			}
		];
	},

	initGrid: function() {
		var store = Ext.create('Ext.data.Store', {
			fields: this.getGridFields(),
			proxy: {
				type: 'ajax',
				url: 'dashboardAssign.action',
				reader: {
					type: 'json'
				}
			},
			autoLoad: true
		});
		var gridConfig = {
			xtype: 'grid',
			region:'center',
			store: store,
			selModel: Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"}), //for screen export
			columns: this.getGridColumns(),
			plugins: ["gridfilters"],
			autoWidth: true,
			border: false,
			bodyBorder:false,
			cls:'gridNoBorder',
			stripeRows: true
		};
		var gridListeners = {selectionchange: {fn:this.onGridSelectionChange, scope:this}};
		gridListeners.itemcontextmenu = {fn:this.onGridRowCtxMenu, scope:this};
		gridListeners.itemdblclick = {fn:this.editDashboard, scope:this};

		gridConfig.listeners = gridListeners;
		//create the grid
		this.grid = Ext.create('Ext.grid.Panel', gridConfig);
	},
	onGridSelectionChange: function (view, selections) {
		this.enableDisableToolbarButtons(view, selections);
		var selectedRow = null;
		if (selections && selections.length>0) {
			selectedRow = selections[0];
		}
	},
	enableDisableToolbarButtons: function (view, selections) {
		if (CWHF.isNull(selections) || selections.length===0) {
			this.actionEdit.setDisabled(true);
			this.actionCopy.setDisabled(true);
			this.actionDelete.setDisabled(true);
			this.actionConfig.setDisabled(true);
		} else {
			this.actionEdit.setDisabled(false);
			this.actionCopy.setDisabled(false);
			this.actionDelete.setDisabled(false);
			this.actionConfig.setDisabled(false);
		}
	},

	/**
	 * Show the context menu in grid
	 */
	onGridRowCtxMenu: function(grid, record, item, index, evtObj) {
		this.onCtxMenu(false, record, evtObj);
		return false;
	},

	initActions: function(){
		var me=this;
		me.actions=new Array();
		me.actionAdd=Ext.create('Ext.Action',{
			text: getText('common.btn.add'),
			overflowText: getText('common.btn.add'),
			iconCls: "add",
			scope: me,
			handler: me.addDashboard
		});
		me.actions.push(me.actionAdd);

		me.actionEdit=Ext.create('Ext.Action',{
			text: getText('common.btn.edit'),
			overflowText: getText('common.btn.edit'),
			iconCls: "edit",
			scope: me,
			handler: me.editDashboard
		});
		me.actions.push(me.actionEdit);

		me.actionDelete=Ext.create('Ext.Action',{
			text: getText('common.btn.delete'),
			overflowText: getText('common.btn.delete'),
			iconCls: "delete",
			scope: me,
			handler: me.deleteItems
		});
		me.actions.push(me.actionDelete);

		me.actionCopy=Ext.create('Ext.Action',{
			text: getText('common.btn.copy'),
			overflowText: getText('common.btn.copy'),
			iconCls: "copy",
			scope: me,
			handler: me.copyDashboard
		});
		me.actions.push(me.actionCopy);

		me.actionConfig=Ext.create('Ext.Action',{
			text: getText('common.btn.config'),
			overflowText: getText('common.btn.config'),
			iconCls: "btnConfig",
			scope: me,
			handler: me.configDashboard
		});
		me.actions.push(me.actionConfig);
	},
	getToolbarActions: function(){
		return this.actions;
	},
	addDashboard:function(){
		var me=this;
		var title = getText('common.lbl.add',me.entityName);
		//var reloadParamsFromResult = this.getReloadParamsFromResult();
		return this.onAddEdit(title, true,null, null);
	},
	editDashboard:function(){
		var me=this;
		var title = getText('common.lbl.edit',me.entityName);
		var objectID = this.getSelectedId();
		if(CWHF.isNull(objectID)){
			return null;
		}
		var loadParams = {
			copy:false,
			objectID:objectID
		};
		var submitParams = {
			objectID:objectID,
			copy:false
		};
		return this.onAddEdit(title, false,loadParams, submitParams);
	},
	deleteItems:function(){
		var me=this;
		var urlDeleteItems="dashboardAssign!deleteCockpitTemplate.action";
		var selections=me.grid.getSelectionModel().getSelection();
		var titleNotSelected=getText("common.lbl.messageBox.title.notSelected");
		var messageConfirmDelete=getText("common.lbl.messageBox.removeSelected.confirm");
		var messageNoSelection=getText("common.lbl.messageBox.removeSelected.notSelected");
		var titleDelete=getText("common.btn.delete");
		var okLabel=getText("common.btn.ok");
		if(CWHF.isNull(selections)||selections.length===0){
			Ext.MessageBox.show({
				title:titleNotSelected,
				msg:messageNoSelection ,
				buttons:{ok : okLabel},
				icon: Ext.MessageBox.WARNING
			});
			return;
		}
		Ext.MessageBox.show({
			title:titleDelete,
			msg: messageConfirmDelete,
			//buttons:{yes : yesLabel, no : noLabel},
			buttons: Ext.MessageBox.YESNO,
			fn: function(btn){
				if(btn==="yes"){
					var selections=me.grid.getSelectionModel().getSelection();
					var deletedItems="";
					var row;
					var i;
					for(i=0;i<selections.length;i++){
						//row=selections[i];
						row=selections[i].data;
						deletedItems=deletedItems+row.id+";";
					}
					Ext.Ajax.request({
						url: urlDeleteItems,
						disableCaching:true,
						success : function(result) {
							var jsonData = Ext.decode(result.responseText);
						    if(!jsonData.success) {

						    	Ext.MessageBox.show({
						    		title : getText('common.warning'),
								    msg : jsonData.errorMessage,
								    width : 200,
								    buttons : Ext.MessageBox.OK,
								    icon : Ext.MessageBox.WARNING
								});
						    }
							me.refresh.call(me);
						},
						failure: function(){
						},
						method:'POST',
						params:{"deletedItems":deletedItems}
					});
				}
			},
			//animEl: 'mb4',
			icon: Ext.MessageBox.QUESTION
		});
	},
	copyDashboard:function(){
		var me=this;
		var title = getText('common.lbl.copy',me.entityName);
		var objectID = this.getSelectedId();
		if(CWHF.isNull(objectID)){
			return null;
		}
		var loadParams = {
			copy:true,
			objectID:objectID
		};
		var submitParams = {
			objectID:objectID,
			copy:true
		};
		return this.onAddEdit(title, false,loadParams, submitParams, null);
	},
	getSelectedId:function(){
		var me=this;
		var sel=this.grid.getSelectionModel().getSelection();
		if(sel&&sel.length>0){
			return sel[0].data['id'];
		}
		return null;
	},
	configDashboard:function(){
		var objectID = this.getSelectedId();
		if(CWHF.isNull(objectID)){
			return null;
		}
		var urlEditScreen="dashboardEdit.action?backAction=admin.action&componentID="+objectID;
		window.location.href=urlEditScreen;
	},
	onAddEdit: function(title, add, loadParams, submitParams,
						refreshParamsFromResult) {
		var load = {
			loadUrl:'dashboardAssign!edit.action',
			loadUrlParams:loadParams
		};
		var submit = {
			submitUrl:'dashboardAssign!save.action',
			submitUrlParams:submitParams,
			submitButtonText:getText('common.btn.save'),
			refreshAfterSubmitHandler:com.trackplus.util.RefreshAfterSubmit.refreshGridAfterSubmit,
			refreshParametersAfterSubmit:refreshParamsFromResult
		};
		var items = this.getPanelItems();
		var windowParameters = {title:title,
			width:500,
			height:300,
			load:load,
			submit:submit,
			items:items
		};
		var windowConfig = Ext.create('com.trackplus.util.WindowConfig', windowParameters);
		windowConfig.showWindowByConfig(this);
	},
	refresh:function(){
		this.grid.getStore().load();
	},
	getPanelItems: function() {
		return  [CWHF.createTextField('common.lbl.name','name',
			{anchor:'100%', allowBlank:false, labelWidth:this.labelWidth, width:this.textFieldWidth}),
			CWHF.createTextAreaField('common.lbl.description','description',
				{anchor:'100%', labelWidth:this.labelWidth, width:this.textFieldWidth})];
	}
});
