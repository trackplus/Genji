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



Ext.define('com.trackplus.item.PrintItemAction',{
	extend:'com.trackplus.item.ItemAction',
	config:{
	},
	initData:null,
	constructor : function(config) {
		var me=this;
		me.callParent(arguments);
		me.centerPanel= Ext.create('Ext.panel.Panel',{
			layout:'border',
			margin:'0 0 0 0',
			border:false,
			bodyBorder  :false,
			cls:'printItem',
			items:me.createChildren()
		});
	},
	reExecute:function(){
		var me=this;
		var workItemID=me.initData.workItemID;
		window.location.href="printItem.action?workItemID="+workItemID;
	},
	execute:function(data){
		var me=this;
		me.initData=data;
		me.loadSuccess.call(me,data);
	},
	reload:function(item){
		var me=this;
		me.reExecute();
	},
	isVisibleAction:function(toolbarItem){
		var id=toolbarItem.id;
		if(id==com.trackplus.item.ToolbarItem.NAVIGATION_NEXT||
				id==com.trackplus.item.ToolbarItem.NAVIGATION_PREV){
			return false;
		}
		return true;
	},
	createToolbarAction:function(toolbarItem){
		var me=this;
		return {
			overflowText:getText(toolbarItem.labelKey),
			tooltip:getText(toolbarItem.tooltipKey),
			iconCls: toolbarItem.cssClass+'20',
			disabled:toolbarItem.condition==false,
			text:getText(toolbarItem.labelKey),
			handler:function(btn){
				me.executeToolbarAction.call(me,toolbarItem);
			}
		};
	},
	updateTitle:function(data){
	},
	saveSuccess:function(form, action){
		var me=this;
		if(me.successHandler!=null){
			me.successHandler.call(me.scope!=null?me.scope:me,action.result.data,me.successExtra);
		}
	},
	saveFailure:function(form, action){
		//me.callParent(arguments);
	},
	executeItemAction:function(jsonData){
		var me=this;
		var actionID=jsonData.actionID;
		var parentID=jsonData.parentID;
		/*if(me.successExtra==null){
			me.successExtra={};
		}
		me.successExtra['actionID']=me.actionID;
		me.successExtra['workItemID']=me.workItemID;*/

		var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',{
			workItemID:me.workItemID,
			actionID:actionID,
			parentID:parentID,
			successHandler:me.successHandler,
			scope:me,
			modal:true
		});
		itemAction.execute.call(itemAction);
	},
	clickOnChild:function(workItemID,extraAction){
		var me=this;
		me.clickOnExtraItem(workItemID,extraAction);
	},
	clickOnLink:function(workItemID,extraAction){
		var me=this;
		me.clickOnExtraItem(workItemID,extraAction);
	},
	clickOnParent:function(parentID,extraAction){
		var me=this;
		me.clickOnExtraItem(parentID,extraAction);
	},
	clickOnExtraItem:function(itemID,extraAction){
		var me=this;
		var itemAction=Ext.create('com.trackplus.item.ItemActionDialog',{
			workItemID:itemID,
			actionID:-2,//printItem
			successHandler:extraAction!=null?extraAction.refreshChildren:me.refreshChildren,
			scope:extraAction!=null?extraAction:me,
			modal:false
		});
		itemAction.execute.call(itemAction);
		itemAction.addListener('clickOnChild',me.clickOnChild,me,itemAction);
		itemAction.addListener('clickOnLink',me.clickOnLink,me,itemAction);
		itemAction.addListener('clickOnParent',me.clickOnParent,me,itemAction);
	}
});


Ext.define('com.trackplus.layout.PrintItemLayout',{
	extend:'com.trackplus.layout.BaseLayout',
	useToolbar:false,
	constructor : function(config) {
		var me = this;
		me.callParent(arguments);
		var data=me.initData;
		if(data.workItemID!=null){
			me.printItemAction=Ext.create('com.trackplus.item.PrintItemAction',{
				workItemID:data.workItemID,
				successHandler:me.successHandler,
				actionID:-2,//PRINT
				scope:me
			});
			me.onReady(function(){
				var data=me.initData;
				me.printItemAction.execute.call(me.printItemAction,data);
			});
		}
	},
	createCenterPanel:function(){
		var me=this;
		if(me.initData.workItemID==null){
			return Ext.create('Ext.Component',{
				cls:'errorDiv ulist',
				html:'<ul><li>'+me.initData.error+'</li></ul>'
			});
		}else{
			return me.printItemAction.centerPanel;
		}
	},
	successHandler:function(data){
		//var newWorkItemID=data.workItemID;
		var me=this;
		var workItemID=me.initData.workItemID;
		window.location.href="printItem.action?workItemID="+workItemID;
	},
	reload:function(){
		var me=this;
		var workItemID=me.initData.workItemID;
		window.location.href="printItem.action?workItemID="+workItemID;
	}
});
