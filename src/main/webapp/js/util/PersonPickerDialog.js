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

Ext.define('com.trackplus.util.PersonPickerDialog',{
	extend:'Ext.Base',
	config: {
		title:"",
		data:null,
		includeEmail:false,
		includeGroups:false,
		handler:null,
		scope:null,
		ajaxContext:null,
		width:250,
		height:400
	},
	controller:null,
	constructor : function(config) {
		var me = this
		var config = config || {};
		me.initialConfig = config;
		Ext.apply(me, config);
	},
	showDialog:function(){
		var me=this;
		me.view=Ext.create('com.trackplus.util.PersonPickerDialogView',{
			data:me.data
		});

		if(me.win!=null){
			me.win.destroy();
		}

		me.win = Ext.create('Ext.window.Window',{
			layout      : 'fit',
			cls:'bottomButtonsDialog',
			border:false,
			bodyBorder:true,
			margin:'0 0 0 0',
			style:{
				padding:'5px 0px 0px 0px'
			},
			bodyPadding:'0px',
			iconCls:'user16',
			width:me.width,
			height:me.height,
			closeAction :'destroy',
			title:me.title,
			modal:true,
			items:[me.view],
			autoScroll  :false,
			buttons: [
				{
					text : getText('common.btn.ok'),
					formBind: true, //only enabled once the form is valid
					handler  : function(){
						me.okHandler.call(me);
					}
				},{
					text : getText('common.btn.cancel'),
					handler  : function(){
						me.win.hide();
						me.win.destroy();
					}
				}
			]
		});
		me.win.show();
		if(me.data==null){
			me.refreshData();
		}
	},
	refreshData:function(){
		var me=this;
		me.view.setLoading(true);
		var urlStr='personPicker.action';
		Ext.Ajax.request({
			url: urlStr,
			disableCaching:true,
			success: function(result){
				me.view.setLoading(false);
				var responseJson = Ext.decode(result.responseText);
				var data=responseJson.data;
				me.view.replaceData.call(me.view,data);
			},
			failure: function(){
				me.view.setLoading(false);
			},
			method:'POST',
			params:{includeEmail:me.includeEmail,includeGroups:me.includeGroups}
		});
	},

	okHandler:function(){
		var me=this;
		var value=me.view.getValue.call(me.view);
		var displayValue=me.view.getDisplayValue.call(me.view);
		me.win.hide();
		me.win.destroy();
		if(me.handler!=null){
			me.handler.call(me.scope,value,displayValue);
		}
	}
});
Ext.define('com.trackplus.util.PersonPickerDialogView',{
	extend: 'Ext.panel.Panel',
	config:{
		data:[],
		maxSelectionCount : null
	},
	layout:'border',
	border:false,
	bodyBorder:false,
	bodyStyle:{
		padding:'0px 0px 0px 0px'
	},
	lblSelectAll : getText('common.lbl.selectAll'),
	lblClearSelection : getText('common.lbl.clearSelectedItems'),
	allItemSelected : false,
	initComponent: function(){
		var me=this;
		if(me.selectedDatasourceType==null){
			me.selectedDatasourceType=1;
		}
		me.store=me.createStore();
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		me.searchField=Ext.create('Ext.form.field.Text',{
			emptyText:getText('common.btn.search')+"...",
			cls: 'searchfield',
			anchor:'100%',
			//width:me.inputEl.getWidth()-5,
			margin:'5 15 5 5'
		});
		me.searchField.on('change', function(cmp,value){
			me.filter.call(me,value);
		});
		me.linkClearSelection=Ext.create('Ext.ux.LinkComponent',{
			handler:me.clearOrSelectAll,
			anchor:'100%',
			clsLink:'link_blue',
			margin:'2 5 7 5',
			scope:me,
			label : me.getClearSelectionLabel()
		});
		var panelNorth=Ext.create('Ext.panel.Panel',{
			border:false,
			bodyBorder:false,
			region:'north',
			layout:'anchor',
			items:[me.searchField,me.linkClearSelection]
		})

		me.boundList= me.createBoundList();
		me.boundList.addListener('checkchange',me.treeCheckChange,me);
		var items=new Array();
		items.push(panelNorth);
		items.push(me.boundList);
		return items;
	},
	replaceData:function(data){
		var me=this;
		var rootNode=me.store.getRootNode();
		rootNode.removeAll();
		if(data!=null&&data.length>0){
			rootNode.appendChild(data);
		}
	},
	treeCheckChange: function(node, checked, options) {
		var me=this;
		var records = me.boundList.getView().getChecked();

		if (records == null||records.length==0) {
			me.allItemSelected = false;
		} else {
			var allCount =me.getSelectableItemsCount();
			if (allCount == records.length) {
				me.allItemSelected = true;
			} else {
				me.allItemSelected = false;
			}
		}
		if (me.linkClearSelection != null) {
			var oldLabel = me.linkClearSelection.getMyLabel();
			var newLabel = me.getClearSelectionLabel();// me.allItemSelected==true?me.lblClearSelection:me.lblSelectAll;
			if (oldLabel != newLabel) {
				me.linkClearSelection.suspendLayout = true;
				me.linkClearSelection.setLabel(newLabel);
				me.linkClearSelection.suspendLayout = false;
			}
		}
	},
	getSelectableItemsCount:function(){
		var me=this;
		var count=0;
		if (me.boundList!=null) {
			this.boundList.getRootNode().cascadeBy(function(){
				if(this.get('checked')!=undefined){
					count++;
				}
			});
		}
		return count;
	},

	getClearSelectionLabel : function() {
		var me = this;
		return me.allItemSelected == true ? me.lblClearSelection : me.lblSelectAll
	},

	clearOrSelectAll : function() {
		var me = this;
		if ((me.maxSelectionCount != null && me.maxSelectionCount > 0) || me.allItemSelected == true) {
			me.clearSelection();
		} else {
			me.selectAll();
		}
	},

	clearSelection:function(){
		var me=this;
		this.boundList.getRootNode().cascadeBy(function(){
			if(this.get('checked')!=undefined){
				this.set( 'checked', false );
			}
		});
		me.searchField.setValue(null);
		me.allItemSelected = false;
		me.changeLinkClearSelection();
	},
	selectAll : function() {
		var me=this;
		var value=new Array();
		if (me.boundList!=null) {
			this.boundList.getRootNode().cascadeBy(function(){
				if(this.get('checked')!=undefined){
					this.set( 'checked', true);
					value.push(this);
				}
			});
			this.boundList.expandAll();
		}
		me.searchField.setValue(null);
		me.allItemSelected = true;
		me.changeLinkClearSelection();
	},
	changeLinkClearSelection:function(){
		var me=this;
		if (me.linkClearSelection != null) {
			var oldLabel = me.linkClearSelection.getMyLabel();
			var newLabel = me.getClearSelectionLabel();// me.allItemSelected==true?me.lblClearSelection:me.lblSelectAll;
			if (oldLabel != newLabel) {
				me.linkClearSelection.suspendLayout = true;
				me.linkClearSelection.setLabel(newLabel);
				me.linkClearSelection.suspendLayout = false;
			}
		}
	},
	filter : function(value){
		var me = this;
		if (value.length > 0) {
			me.boundList.filter(value);
		}else{
			me.clearFilter();
		}
	},
	clearFilter:function(){
		var me=this;
		me.boundList.clearFilter();
	},
	createStore:function(){
		var me=this;
		return  Ext.create("Ext.data.TreeStore", {
			fields: [
				{name : "id", mapping : "id", type: "string"},
				{name : "text", mapping : "text", type: "string"},
				{name : "leaf", mapping : "leaf", type: "boolean"},
				{name : "iconCls", mapping : "iconCls", type: "string"},
				{name: "selectable", mapping: "selectable", type: "boolean"}
			],
			autoLoad: false,
			root: {
				expanded: true,
				children:(me.data==null?[]:me.data)
			},
			originalData:me.data==null?[]:me.data
		});
	},
	createBoundList:function(){
		var treeConfig = {
			store:this.store,
			rootVisible: false,
			containerScroll: true,
			autoScroll: true,
			cls:"simpleTree",
			border:false,
			region:'center',
			//selModel : Ext.create('Ext.selection.CheckboxModel', {mode:"MULTI"}),
			plugins: [{
				ptype: 'treefilter',
				allowParentFolders: true
			}]
		}
		var tree= Ext.create("Ext.tree.Panel", treeConfig);
		return tree;
	},
	findRecord: function(field, value) {
		var me=this;
		var nodeToSelect=me.store.getNodeById(value);
		if(nodeToSelect!=null){
			return nodeToSelect;
		}else{
			return false;
		}
	},
	getValue:function(){
		var me=this;
		var records = me.boundList.getView().getChecked();
		return records;
	},
	getDisplayValue:function(){
		var me=this;
		var records = me.boundList.getView().getChecked();
		if(records==null||records.length==0){
			return null;
		}
		var displayData=new Array();
		for (var i = 0; i < records.length;i++) {
			var record = records[i];
			displayData.push(record.data.text);
		}
		return displayData;
	}
});

